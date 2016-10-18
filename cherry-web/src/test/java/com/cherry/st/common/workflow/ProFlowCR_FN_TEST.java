package com.cherry.st.common.workflow;

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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMessage_IF;
import com.cherry.st.bil.action.BINOLSTBIL16_Action;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.opensymphony.workflow.Workflow;

public class ProFlowCR_FN_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Resource(name="binBEMQMES02_BL")
    private AnalyzeMessage_IF binBEMQMES02_BL;
    
    private BINOLSTBIL16_Action binOLSTBIL16_Action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow1() throws Exception {
        String caseName = "testWorkFlow1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> stocktakeReqMainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "CR");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        mainData.put("stocktakeReqMainData", stocktakeReqMainData);
        mainData.put("stocktakeReqDetailList", detailList);
        mainData.put("CurrentUnit", "MQ");
        
        //加载工作流文件
        String orgCode = "bingkun";
        String brandCode = "FBC";
        String flowName = "proFlowCR";
        String flowFile = "proFlowCR.xml";
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
        
        //审核通过
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "501");
        binOLSTBIL16_Action = createAction(BINOLSTBIL16_Action.class, "/st","BINOLSTBIL16_doaction");

        BINOLSTBIL16_Form form = binOLSTBIL16_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTBIL16_Action.setSession(session);
        binOLSTBIL16_Action.setServletRequest(request);
        binOLSTBIL16_Action.doaction();
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProStocktakeRequest");
        paramMap.put("StockTakingNo", stocktakeReqMainData.get("StockTakingNo"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_AGREE,actualList.get(0).get("VerifiedFlag"));
        
        //盘点申请确认
        Map<String,Object> mqMap = new HashMap<String,Object>();
        List<Map<String,Object>> detailDataDTOList = new ArrayList<Map<String,Object>>();
        Map<String,Object> detailDataDTO = new HashMap<String,Object>();
        detailDataDTO.put("stockType", "1");
        detailDataDTOList.add(detailDataDTO);
        mqMap.put("tradeNoIF", stocktakeReqMainData.get("StockTakingNoIF"));
        mqMap.put("organizationInfoID", 99);
        mqMap.put("brandInfoID", 99);
        mqMap.put("inventoryInfoID", 4029);
        mqMap.put("detailDataDTOList", detailDataDTOList);
        binBEMQMES02_BL.analyzeStocktakeConfirmData(mqMap);
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStockTaking");
        paramMap.put("RelevanceNo", stocktakeReqMainData.get("StockTakingNoIF"));
        actualList = testCOM_Service.getTableData(paramMap);
        
        assertTrue(actualList.size()>0);
        
        assertEquals("999",workflow.getPropertySet(workFlowID).getString("OS_Current_Operate"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlow2() throws Exception {
        String caseName = "testWorkFlow2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> stocktakeReqMainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
         
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, "CR");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "38");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER, "1000");
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "100");
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1000");
        mainData.put("UserInfo", userInfo);
        mainData.put("stocktakeReqMainData", stocktakeReqMainData);
        mainData.put("stocktakeReqDetailList", detailList);
        mainData.put("CurrentUnit", "MQ");
        
        //加载工作流文件
        String orgCode = "bingkun";
        String brandCode = "FBC";
        String flowName = "proFlowCR";
        String flowFile = "proFlowCR.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置审核
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step50")));
        
        workflow.getWorkflowDescriptor(wfName).getStep(50).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(504).setMetaAttributes(metaAttributes);

        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        assertTrue(workFlowID>0);
        
        //废弃
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "504");
        binOLSTBIL16_Action = createAction(BINOLSTBIL16_Action.class, "/st","BINOLSTBIL16_doaction");

        BINOLSTBIL16_Form form = binOLSTBIL16_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTBIL16_Action.setSession(session);
        binOLSTBIL16_Action.setServletRequest(request);
        binOLSTBIL16_Action.doaction();
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProStocktakeRequest");
        paramMap.put("StockTakingNoIF", stocktakeReqMainData.get("StockTakingNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        assertEquals(CherryConstants.AUDIT_FLAG_DISCARD,actualList.get(0).get("VerifiedFlag"));
        
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
