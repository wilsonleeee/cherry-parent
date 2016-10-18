package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.form.BINOLCM26_Form;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;

public class BINOLCM26_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
	
	private BINOLCM26_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLCM26_Action.class, "/common","BINOLCM26_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLCM26_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowId(workFlowID);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertEquals("收货",form.getCurrentOperateName());
        assertEquals("2",form.getRuleType());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave1() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLCM26_Action.class, "/common","BINOLCM26_save");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLCM26_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowId(workFlowID);
        form.setAuditorType("1");
        form.setAuditorID("1");
        form.setRuleType("2");
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("WorkFlowID", workFlowID);
        List<Map<String,Object>> actualList = testService.getTableData(paramMap);
        assertEquals("U1,",actualList.get(0).get("CurrentParticipant"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave2() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLCM26_Action.class, "/common","BINOLCM26_save");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLCM26_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowId(workFlowID);
        form.setAuditorType("2");
        form.setAuditorID("1");
        form.setRuleType("2");
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("WorkFlowID", workFlowID);
        List<Map<String,Object>> actualList = testService.getTableData(paramMap);
        assertEquals("P1,",actualList.get(0).get("CurrentParticipant"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave3() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLCM26_Action.class, "/common","BINOLCM26_save");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLCM26_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowId(workFlowID);
        form.setAuditorType("3");
        form.setAuditorID("1");
        form.setRuleType("2");
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("WorkFlowID", workFlowID);
        List<Map<String,Object>> actualList = testService.getTableData(paramMap);
        assertEquals("D1,",actualList.get(0).get("CurrentParticipant"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave4() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLCM26_Action.class, "/common","BINOLCM26_save");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        //Inventory.BIN_ProductOrderDetail
        Map<String,Object> productOrderDetailInsertMap = dataList.get(1);
        productOrderDetailInsertMap.put("BIN_ProductOrderID", productOrderID);
        testService.insertTableData(productOrderDetailInsertMap);
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, 0);
        mainData.put("UserInfo", userInfo);
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "150");
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLCM26_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setWorkFlowId(workFlowID);
        form.setAuditorType("2");
        form.setAuditorID("1");
        form.setRuleType("3");
        form.setPrivilegeFlag("A");
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("WorkFlowID", workFlowID);
        List<Map<String,Object>> actualList = testService.getTableData(paramMap);
        assertEquals(null,actualList.get(0).get("CurrentParticipant"));
        assertEquals("{\"1\":\"A\"}",actualList.get(0).get("ParticipantLimit"));
    }
}