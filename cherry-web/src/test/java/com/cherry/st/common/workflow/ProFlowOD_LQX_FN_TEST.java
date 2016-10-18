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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.st.sfh.action.BINOLSTSFH03_Action;
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;
import com.opensymphony.workflow.Workflow;

public class ProFlowOD_LQX_FN_TEST extends CherryJunitBase{
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Resource(name="binBEMQMES99_BL")
    private BINBEMQMES99_BL binBEMQMES99_BL;
    
    private BINOLSTSFH03_Action binOLSTSFH03_Action;

    /**
     * 执行MQ接收
     * @param msg
     * @throws Exception
     */
    public void tran_analyzeMessage(String msg) throws Exception{
        // 调用共通将消息体解析成Map
        String msgFlag = "Old";
        Map<String,Object> map = MessageUtil.message2Map(msg);
        Object mainDataDTO = null;
        if(null == map){
            mainDataDTO = Message2Bean.parseMessage((String) msg);//消息转化成DTO
            map = (Map<String, Object>) Bean2Map.toHashMap(mainDataDTO);//DTO转化成map
        }else{
            msgFlag = "New";
        }
        if("Old".equals(msgFlag)){
            //老消息体
            binBEMQMES99_BL.tran_analyzeMessage(mainDataDTO, map);
        }else{
            //新消息体
            binBEMQMES99_BL.tran_analyzeMessage(map);
        }
    }
    
    /**
     * 组成消息体
     * @param messageBody
     * @return
     */
    public String getMessageBody(Map<String,Object> messageBody){
        StringBuffer msg = new StringBuffer();
        for(int i = 0;i<messageBody.size();i++){
            int index = i + 1;
            String messageBodyKey = "messageBody" + index;
            if(i < messageBody.size()-1){
                msg.append(messageBody.get(messageBodyKey)).append("\r\n");
            }else{
                msg.append(messageBody.get(messageBodyKey));
            }
        }
        return msg.toString();
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testWorkFlowOD_LQX1() throws Exception {
        //林清轩工作流（三审第三方）
        String caseName = "testWorkFlowOD_LQX1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        List<Map<String,Object>> insertList = (List<Map<String,Object>>) dataMap.get("insertList");
        
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = insertList.get(0);
        int organizationInfoID = testCOM_Service.insertTableData(insertOrganizationInfoMap);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = insertList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int brandInfoID = testCOM_Service.insertTableData(insertBrandInfoMap);
        
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>) dataMap.get("sqlList");
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> tableParam = new HashMap<String,Object>();
        tableParam.put("tableName", "Basis.BIN_Organization");
        tableParam.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(tableParam);
        String organizationID = ConvertUtil.getString(organizationList.get(0).get("BIN_OrganizationID"));

        //Tools.BIN_SystemConfig
        String sql1 = ConvertUtil.getString(sqlList.get(2).get("sql"));
        sql1 = sql1.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql1 = sql1.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql1);
        
        //Tools.BIN_SystemConfig
        String sql2 = ConvertUtil.getString(sqlList.get(3).get("sql"));
        sql2 = sql2.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql2 = sql2.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql2);
        
        //Basis.BIN_Employee
        String sql3 = ConvertUtil.getString(sqlList.get(4).get("sql"));
        sql3 = sql3.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql3 = sql3.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql3);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoMap = insertList.get(2);
        insertDepotInfoMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertDepotInfoMap.put("BIN_OrganizationID", organizationID);
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoMap);
      
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = insertList.get(3);
        insertInventoryInfoMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertInventoryInfoMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap1 = insertList.get(4);
        insertLogicInventoryMap1.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap1.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryID1 = testCOM_Service.insertTableData(insertLogicInventoryMap1);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap2 = insertList.get(5);
        insertLogicInventoryMap2.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap2.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryID2 = testCOM_Service.insertTableData(insertLogicInventoryMap2);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusinessMap = insertList.get(6);
        insertLogicDepotBusinessMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusinessMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusinessMap.put("BIN_LogicInventoryInfoID", logicInventoryID2);
        testCOM_Service.insertTableData(insertLogicDepotBusinessMap);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertList.get(7);
        insertProductMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap1.put("BIN_BrandInfoID", brandInfoID);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertList.get(8);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertList.get(9);
        insertProductMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertProductMap2.put("BIN_BrandInfoID", brandInfoID);
        int productID = testCOM_Service.insertTableData(insertProductMap2);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertList.get(10);
        insertProductVendorMap2.put("BIN_ProductID", productID);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertCounterInfoMap = insertList.get(11);
        insertCounterInfoMap.put("BIN_OrganizationID", organizationID);
        insertCounterInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertCounterInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertCounterInfoMap);

        //加载工作流文件
        String orgCode = "TCWFLQX1";
        String brandCode = "TCWFLQX1";
        String flowName = "proFlowOD";
        String flowFile = "proFlowOD_LQX.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //设置订单一审
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowName);
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(50).getMetaAttributes();
        List<Map<String,Object>> osRuleList = (List<Map<String, Object>>) dataMap.get("OS_Rule");
        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step52")));
        workflow.getWorkflowDescriptor(wfName).getStep(52).setMetaAttributes(metaAttributes);
        workflow.getWorkflowDescriptor(wfName).getAction(521).setMetaAttributes(metaAttributes);

//        //设置二审第三方
//        metaAttributes = workflow.getWorkflowDescriptor(wfName).getStep(52).getMetaAttributes();
//        metaAttributes.put("OS_Rule",ConvertUtil.getString(osRuleList.get(0).get("Step52")));
//        workflow.getWorkflowDescriptor(wfName).getStep(52).setMetaAttributes(metaAttributes);
        
        //组成MQ消息体
        List<Map<String,Object>> mqList = (List<Map<String,Object>>)dataMap.get("MQList");
        
        //订货
        String msgBody = getMessageBody(mqList.get(0));
        tran_analyzeMessage(msgBody);

        Map<String,Object> otherInfo = (Map<String,Object>)dataMap.get("otherInfo");
        String orderNoIF = ConvertUtil.getString(otherInfo.get("OrderNoIF"));
        
        Map<String,Object> actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrder");
        actualParamMap.put("OrderNoIF", orderNoIF);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("5",actualList.get(0).get("VerifiedFlag"));
        
        String orderID = ConvertUtil.getString(actualList.get(0).get("BIN_ProductOrderID"));
        String workFlowID = ConvertUtil.getString(actualList.get(0).get("WorkFlowID"));
        
        List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrderDetail");
        actualParamMap.put("BIN_ProductOrderID", orderID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(expectList.get(0).get("price1"),ConvertUtil.getString(actualList.get(0).get("Price")));
        assertEquals(expectList.get(0).get("price2"),ConvertUtil.getString(actualList.get(0).get("Price")));
        
        //执行一审通过
        request.addParameter("entryid", ConvertUtil.getString(workFlowID));
        request.addParameter("actionid", "521");
        binOLSTSFH03_Action = createAction(BINOLSTSFH03_Action.class, "/st","BINOLSTSFH03_doaction");
        
        BINOLSTSFH03_Form form = binOLSTSFH03_Action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        form.setProductOrderID(orderID);
        form.setProductVendorIDArr(new String[]{ConvertUtil.getString(productVendorID1),ConvertUtil.getString(productVendorID2)});
        form.setInventoryInfoIDArr(new String[]{"1","1"});
        form.setLogicInventoryInfoIDArr(new String[]{"1","1"});
        form.setInventoryInfoIDAcceptArr(new String[]{"2","2"});
        form.setLogicInventoryInfoIDAcceptArr(new String[]{"2","2"});
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTSFH03_Action.setSession(session);
        binOLSTSFH03_Action.setServletRequest(request);
        binOLSTSFH03_Action.doaction();
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrder");
        actualParamMap.put("OrderNoIF", orderNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("13",actualList.get(0).get("VerifiedFlag"));//三审中
        
        
        //执行三审通过
        request.removeParameter("actionid");
        request.addParameter("actionid", "611");
        binOLSTSFH03_Action = createAction(BINOLSTSFH03_Action.class, "/st","BINOLSTSFH03_doaction");
        
        form = binOLSTSFH03_Action.getModel();
        otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        form.setOperateType("37");
        form.setProductOrderID(orderID);
        form.setProductVendorIDArr(new String[]{ConvertUtil.getString(productVendorID1),ConvertUtil.getString(productVendorID2)});
        form.setInventoryInfoIDArr(new String[]{"1","1"});
        form.setLogicInventoryInfoIDArr(new String[]{"1","1"});
        form.setInventoryInfoIDAcceptArr(new String[]{"2","2"});
        form.setLogicInventoryInfoIDAcceptArr(new String[]{"2","2"});
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        binOLSTSFH03_Action.setSession(session);
        binOLSTSFH03_Action.setServletRequest(request);
        binOLSTSFH03_Action.doaction();
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrder");
        actualParamMap.put("OrderNoIF", orderNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("16",actualList.get(0).get("VerifiedFlag"));//四审中
        assertEquals("1",actualList.get(0).get("SynchFlag"));
        
        //K3导入发货单
        msgBody = getMessageBody(mqList.get(1));
        tran_analyzeMessage(msgBody);
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrder");
        actualParamMap.put("OrderNoIF", orderNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("17",actualList.get(0).get("VerifiedFlag"));//四审通过
        
        String deliverNoIF = ConvertUtil.getString(otherInfo.get("DeliverNoIF"));
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductDeliver");
        actualParamMap.put("DeliverNoIF", deliverNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("2",actualList.get(0).get("VerifiedFlag"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Interfaces.BIN_MQLog");
        actualParamMap.put("BillCode", deliverNoIF);
        actualParamMap.put("BillType", "IS");
        actualParamMap.put("BIN_OrganizationInfoID", organizationInfoID);
        actualParamMap.put("BIN_BrandInfoID", brandInfoID);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(1,actualList.size());
        String actualData = ConvertUtil.getString(actualList.get(0).get("Data"));
        assertTrue(actualData.indexOf("[MainDataLine],"+brandCode+","+deliverNoIF+",")>0);
        assertTrue(actualData.indexOf("[DetailDataLine],"+deliverNoIF+",")>0);
        
        //收货
        msgBody = getMessageBody(mqList.get(2));
        tran_analyzeMessage(msgBody);
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductOrder");
        actualParamMap.put("OrderNoIF", orderNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("13",actualList.get(0).get("TradeStatus"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductDeliver");
        actualParamMap.put("DeliverNoIF", deliverNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals("13",actualList.get(0).get("TradeStatus"));
        
        actualParamMap = new HashMap<String,Object>();
        actualParamMap.put("tableName", "Inventory.BIN_ProductReceive");
        actualParamMap.put("RelevanceNo", deliverNoIF);
        actualList = testCOM_Service.getTableData(actualParamMap);
        assertEquals(null,actualList.get(0).get("SynchFlag"));
        
        //流程结束
        assertEquals("999",workflow.getPropertySet(Long.parseLong(workFlowID)).getString("OS_Current_Operate"));
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