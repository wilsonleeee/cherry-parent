package com.cherry.ss.common.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.prm.action.BINOLSSPRM62_Action;
import com.cherry.ss.prm.form.BINOLSSPRM62_Form;
import com.opensymphony.workflow.Workflow;

public class PrmFlowYK_FN_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Resource(name="binOLSSCM00_BL")
    private BINOLSSCM00_BL binOLSSCM00_BL;
    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    private BINOLSSPRM62_Action binOLSSPRM62_Action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow1() throws Exception {
        String caseName = "testWorkFlow1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> mainTableData = dataList.get(0);
        Map<String,Object> detailTableData1 = dataList.get(1);
        Map<String,Object> detailTableData2 = dataList.get(2);
        List<Map<String,Object>> detailTableList = new ArrayList<Map<String,Object>>();
        detailTableList.add(detailTableData1);
        detailTableList.add(detailTableData2);
        int billID = binOLSSCM08_BL.insertPrmShiftAll(mainTableData, detailTableList);
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "MV");
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        
        //加载工作流文件
        String orgCode = "bingkun";
        String brandCode = "FBC";
        String flowName = "prmFlowYK";
        String flowFile = "prmFlowYK.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);

        long workFlowID = binOLSSCM00_BL.StartOSWorkFlow(mainData);
        assertTrue(workFlowID>0);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionShift");
        paramMap.put("BillNo", mainTableData.get("BillNo"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_SUBMIT,actualList.get(0).get("VerifiedFlag"));
        
        //审核通过
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "501");
        binOLSSPRM62_Action = createAction(BINOLSSPRM62_Action.class, "/ss","BINOLSSPRM62_doaction");

        BINOLSSPRM62_Form form = binOLSSPRM62_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        form.setPromotionShiftID(ConvertUtil.getString(billID));
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSSPRM62_Action.setSession(session);
        binOLSSPRM62_Action.setServletRequest(request);
        binOLSSPRM62_Action.doaction();
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionShift");
        paramMap.put("BillNo", mainTableData.get("BillNo"));
        actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));

        assertEquals("999",workflow.getPropertySet(workFlowID).getString("OS_Current_Operate"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionShiftDetail");
        paramMap.put("BIN_PromotionShiftID", billID);
        actualList = testCOM_Service.getTableData(paramMap);
        
        int fromDepotInfoID = CherryUtil.obj2int(actualList.get(0).get("FromDepotInfoID"));
        int fromLogicInventoryInfoID = CherryUtil.obj2int(actualList.get(0).get("FromLogicInventoryInfoID"));
        int toDepotInfoID = CherryUtil.obj2int(actualList.get(0).get("ToDepotInfoID"));
        int toLogicInventoryInfoID = CherryUtil.obj2int(actualList.get(0).get("ToLogicInventoryInfoID"));
        int prmVendorID1 = CherryUtil.obj2int(actualList.get(0).get("BIN_PromotionProductVendorID"));
        int prmVendorID2 = CherryUtil.obj2int(actualList.get(1).get("BIN_PromotionProductVendorID"));
        
        List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_InventoryInfoID", fromDepotInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", fromLogicInventoryInfoID);
        paramMap.put("BIN_PromotionProductVendorID", prmVendorID1);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(ConvertUtil.getString(expectList.get(0).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_InventoryInfoID", toDepotInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", toLogicInventoryInfoID);
        paramMap.put("BIN_PromotionProductVendorID", prmVendorID1);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(ConvertUtil.getString(expectList.get(1).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_InventoryInfoID", fromDepotInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", fromLogicInventoryInfoID);
        paramMap.put("BIN_PromotionProductVendorID", prmVendorID2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(ConvertUtil.getString(expectList.get(2).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));

        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_InventoryInfoID", toDepotInfoID);
        paramMap.put("BIN_LogicInventoryInfoID", toLogicInventoryInfoID);
        paramMap.put("BIN_PromotionProductVendorID", prmVendorID2);
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(ConvertUtil.getString(expectList.get(3).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow2() throws Exception {
        String caseName = "testWorkFlow2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> mainTableData = dataList.get(0);
        Map<String,Object> detailTableData1 = dataList.get(1);
        Map<String,Object> detailTableData2 = dataList.get(2);
        List<Map<String,Object>> detailTableList = new ArrayList<Map<String,Object>>();
        detailTableList.add(detailTableData1);
        detailTableList.add(detailTableData2);
        int billID = binOLSSCM08_BL.insertPrmShiftAll(mainTableData, detailTableList);
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "MV");
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        
        //加载工作流文件
        String orgCode = "bingkun";
        String brandCode = "FBC";
        String flowName = "prmFlowYK";
        String flowFile = "prmFlowYK.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);

        long workFlowID = binOLSSCM00_BL.StartOSWorkFlow(mainData);
        assertTrue(workFlowID>0);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionShift");
        paramMap.put("BillNo", mainTableData.get("BillNo"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_SUBMIT,actualList.get(0).get("VerifiedFlag"));
        
        //废弃
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "504");
        binOLSSPRM62_Action = createAction(BINOLSSPRM62_Action.class, "/ss","BINOLSSPRM62_doaction");

        BINOLSSPRM62_Form form = binOLSSPRM62_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        form.setPromotionShiftID(ConvertUtil.getString(billID));
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSSPRM62_Action.setSession(session);
        binOLSSPRM62_Action.setServletRequest(request);
        binOLSSPRM62_Action.doaction();
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionShift");
        paramMap.put("BillNo", mainTableData.get("BillNo"));
        actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_DISCARD,actualList.get(0).get("VerifiedFlag"));

        assertEquals("999",workflow.getPropertySet(workFlowID).getString("OS_Current_Operate"));
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = PrmFlowYK_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = rootpath + "worflowfile/ss/"+fileName;
        return path;
    }
    
    /**
     * 加载工作流文件到内存中
     * @param filePath
     * @param orgCode
     * @param brandCode
     * @param fileCode
     * @throws Exception
     */
    public void loadWorkFlowDescriptor(String filePath,String fileCode,String orgCode,String brandCode) throws Exception{
        String path = getWorkFlowFilePath(filePath);
        String fileContentNew = FileUtils.readFileToString(new File(path),"UTF-8");
        FileStoreDTO fileStoreNew = null;
        FileStoreDTO fileStoreDTO = binolcm30IF.getFileStoreByCode(fileCode, orgCode, brandCode);
        fileStoreNew = fileStoreDTO;
        ConvertUtil.convertDTO(fileStoreNew, fileStoreDTO, true);
        fileStoreNew.setFileStoreId(0);
        fileStoreNew.setFileCode(fileCode);
        fileStoreNew.setOrgCode(orgCode);
        fileStoreNew.setBrandCode(brandCode);
        // 刷新内存的工作流文件内容
        fileStoreNew.setFileContent(fileContentNew);
    }
}