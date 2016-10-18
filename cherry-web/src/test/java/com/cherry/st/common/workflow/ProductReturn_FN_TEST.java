package com.cherry.st.common.workflow;

import java.io.File;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.action.BINOLSTBIL12_Action;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.opensymphony.workflow.Workflow;

public class ProductReturn_FN_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    private BINOLSTBIL12_Action binOLSTBIL12_Action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow1() throws Exception {
        //人工确认
        String caseName = "testWorkFlow1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //插入数据
        //Inventory.BIN_ProductReturn
        Map<String,Object> mainDataRR = dataList.get(0);
        int billID = testCOM_Service.insertTableData(mainDataRR);
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_ProductReturnID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(detailData1);
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_ProductReturnID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(detailData2);
        
        //判断业务日期是否存在，不存在插入一个
        Map<String,Object> bussinessDateParam = new HashMap<String,Object>();
        bussinessDateParam.put("organizationInfoId", mainDataRR.get("BIN_OrganizationInfoID"));
        bussinessDateParam.put("brandInfoId", mainDataRR.get("BIN_BrandInfoID"));
        String bussinessDate = testCOM_Service.getBussinessDate(bussinessDateParam);
        Map<String,Object> bussinessDateInsertMap = dataList.get(3);
        if(null == bussinessDate || "".equals(bussinessDate)){
            testCOM_Service.insertTableData(bussinessDateInsertMap);
            bussinessDate = ConvertUtil.getString(bussinessDateInsertMap.get("ControlDate"));
        }
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "RR");
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        mainData.put("CurrentUnit", "MQ");
        mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "productReturn";
        String flowFile = "productReturn.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);

        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        assertTrue(workFlowID>0);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductReturn");
        paramMap.put("ReturnNoIF", mainDataRR.get("ReturnNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(CherryConstants.AUDIT_FLAG_SUBMIT,actualList.get(0).get("VerifiedFlag"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", mainDataRR.get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertTrue(actualList.size()>0);
        assertEquals(mainDataRR.get("ReturnDate"),actualList.get(0).get("StockInOutDate"));
        
        //确认退库
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "501");
        binOLSTBIL12_Action = createAction(BINOLSTBIL12_Action.class, "/st","BINOLSTBIL12_doaction");

        BINOLSTBIL12_Form form = binOLSTBIL12_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        String[] productVendorIDArr = {String.valueOf(productVendorID1),String.valueOf(productVendorID2)};
        form.setProductVendorIDArr(productVendorIDArr);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTBIL12_Action.setSession(session);
        binOLSTBIL12_Action.setServletRequest(request);
        binOLSTBIL12_Action.doaction();
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductReturn");
        paramMap.put("ReturnNoIF", mainDataRR.get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));
        assertEquals(mainDataRR.get("ReturnDate"),actualList.get(0).get("ReturnDate"));

        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductReturn");
        paramMap.put("RelevanceNo", mainDataRR.get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));
        assertEquals(bussinessDate,actualList.get(0).get("ReturnDate"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", actualList.get(0).get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertTrue(actualList.size()>0);
        assertEquals(bussinessDate,actualList.get(0).get("StockInOutDate"));
        
        assertEquals("999",workflow.getPropertySet(workFlowID).getString("OS_Current_Operate"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow2() throws Exception {
        //自动确认
        String caseName = "testWorkFlow2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //插入数据
        //Inventory.BIN_ProductReturn
        Map<String,Object> mainDataRR = dataList.get(0);
        int billID = testCOM_Service.insertTableData(mainDataRR);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_ProductReturnID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(detailData1);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_ProductReturnID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(detailData2);
        
        //判断业务日期是否存在，不存在插入一个
        Map<String,Object> bussinessDateParam = new HashMap<String,Object>();
        bussinessDateParam.put("organizationInfoId", mainDataRR.get("BIN_OrganizationInfoID"));
        bussinessDateParam.put("brandInfoId", mainDataRR.get("BIN_BrandInfoID"));
        String bussinessDate = testCOM_Service.getBussinessDate(bussinessDateParam);
        Map<String,Object> bussinessDateInsertMap = dataList.get(3);
        if(null == bussinessDate || "".equals(bussinessDate)){
            testCOM_Service.insertTableData(bussinessDateInsertMap);
            bussinessDate = ConvertUtil.getString(bussinessDateInsertMap.get("ControlDate"));
        }
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(4);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = testCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(5);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        testCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "RR");
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        mainData.put("CurrentUnit", "MQ");
        mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "productReturn";
        String flowFile = "productReturn.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置自动审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(501).setMetaAttributes(metaAttributes);
        
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        assertTrue(workFlowID>0);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", mainDataRR.get("ReturnNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertTrue(actualList.size()>0);
        assertEquals(mainDataRR.get("ReturnDate"),actualList.get(0).get("StockInOutDate"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductReturn");
        paramMap.put("ReturnNoIF", mainDataRR.get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));
        assertEquals(mainDataRR.get("ReturnDate"),actualList.get(0).get("ReturnDate"));

        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductReturn");
        paramMap.put("RelevanceNo", mainDataRR.get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));
        assertEquals(bussinessDate,actualList.get(0).get("ReturnDate"));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductInOut");
        paramMap.put("RelevanceNo", actualList.get(0).get("ReturnNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertTrue(actualList.size()>0);
        assertEquals(bussinessDate,actualList.get(0).get("StockInOutDate"));
        
        assertEquals("999",workflow.getPropertySet(workFlowID).getString("OS_Current_Operate"));
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = ProFlowRA_FN.class.getResource("/").getPath();
        rootpath = rootpath.replace("test-classes", "classes");
        String path = rootpath + "worflowfile/st/"+fileName;
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
