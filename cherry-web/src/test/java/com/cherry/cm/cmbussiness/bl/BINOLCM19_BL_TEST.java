package com.cherry.cm.cmbussiness.bl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

 
import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.CherryTaskInstance;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.workflow.ProFlowOD_FN;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;

public class BINOLCM19_BL_TEST extends CherryJunitBase{
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_BL binOLSTCM00_BL;
    
    @Resource(name="binOLSSCM00_BL")
    private BINOLSSCM00_BL binOLSSCM00_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL bl;
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binolcm30IF")
    private CherryFileStore binolcm30IF;
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
//    @Test
//    @Rollback(true)
//    @Transactional
//    public void testHasBossByPosition1() throws Exception{
//        String caseName = "testHasBossByPosition1";
//        bl = applicationContext.getBean(BINOLCM19_BL.class);
//        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//        
//        Map<String,Object> insertData = new HashMap<String,Object>();
//        insertData.put("tableName", "Privilege.BIN_EmployeePrivilege");
//        insertData.put("BIN_UserID", "6");
//        insertData.put("BIN_EmployeeID", "4");
//        insertData.put("BIN_SubEmployeeID", "27");
//        insertData.put("BusinessType", "A");
//        insertData.put("OperationType", "0");
//        insertData.put("PrivilegeFlag", "0");
//        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
//        testCOM_Service.insertTableDataNoReturnID(insertData);
//        
//        //测试私有方法
//        Class<?>[] args = new Class[]{String.class,String.class,String.class,Map.class};// 建立参数
//        Method method = bl.getClass().getDeclaredMethod("hasBossByPosition",args);
//        method.setAccessible(true);// 允许处理私有方法
//        
//        for(int index = 0;index<dataList.size();index++){
//            String employeeID = ConvertUtil.getString(dataList.get(index).get("employeeID"));
//            String positionID = ConvertUtil.getString(dataList.get(index).get("positionID"));
//            String privilegeFlag = ConvertUtil.getString(dataList.get(index).get("privilegeFlag"));
//            String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
//            Object result = method.invoke(bl, new Object[] {employeeID,positionID,privilegeFlag,new HashMap()});// 调用方法
//            assertEquals(expected,result.toString());
//        }
//        
//        method.setAccessible(false);
//    }

    @Test
    @Rollback(true)
    @Transactional
    public void testHasChildUser1() throws Exception{
        String caseName = "testHasChildUser1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.put("tableName", "Privilege.BIN_EmployeePrivilege");
        insertData.put("BIN_UserID", "6");
        insertData.put("BIN_EmployeeID", "4");
        insertData.put("BIN_SubEmployeeID", "27");
        insertData.put("BusinessType", "A");
        insertData.put("OperationType", "0");
        insertData.put("PrivilegeFlag", "0");
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        testCOM_Service.insertTableDataNoReturnID(insertData);
        
        //测试私有方法
        Class<?>[] args = new Class[]{String.class,String.class,String.class,Map.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("hasChildUser",args);
        method.setAccessible(true);// 允许处理私有方法
        
        for(int index = 0;index<dataList.size();index++){
            String actorUserID = ConvertUtil.getString(dataList.get(0).get("actorUserID"));
            String creatEmployeeID = ConvertUtil.getString(dataList.get(0).get("creatEmployeeID"));
            String privilegeFlag = ConvertUtil.getString(dataList.get(0).get("privilegeFlag"));
            String expected = ConvertUtil.getString(dataList.get(0).get("expected"));
            Object result = method.invoke(bl, new Object[] {actorUserID,creatEmployeeID,privilegeFlag,new HashMap()});// 调用方法
            assertEquals(expected,result.toString());
        }
        
        method.setAccessible(false);
    }
//    //3
//    @Test
//    @Rollback(true)
//    @Transactional
//    public void testMatchingAuditRule1() throws Exception{
//        String caseName = "testMatchingAuditRule1";
//        bl = applicationContext.getBean(BINOLCM19_BL.class);
//        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//      
//        Map<String,String> map = new HashMap<String,String>();
//        for (int index = 0; index < 4; index++) {
//            map.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_USER)));
//            map.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_POSITION)));
//            map.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
//            map.put(CherryConstants.OS_META_Rule, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_META_Rule)));
//            map.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE)));
//            assertEquals(dataList.get(index).get("expected"),bl.matchingAuditRule(map,new HashMap()));
//        }
//    }
  	//4
//    @Test
//    @Rollback(true)
//    @Transactional
//    public void testIsNeedAudit1() throws Exception{
//        String caseName = "testIsNeedAudit1";
//        bl = applicationContext.getBean(BINOLCM19_BL.class);
//        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//      
//        //测试私有方法
//        Class<?>[] args = new Class[]{String.class,String.class,String.class,String.class,Map.class,Map.class};// 建立参数
//        Method method = bl.getClass().getDeclaredMethod("isNeedAudit",args);
//        method.setAccessible(true);// 允许处理私有方法
//      
//        for (int index = 0; index < dataList.size(); index++) {
//            String userID = ConvertUtil.getString(dataList.get(index).get("userID"));
//            String positionID = ConvertUtil.getString(dataList.get(index).get("positionID"));
//            String departID = ConvertUtil.getString(dataList.get(index).get("departID"));
//            String employeeID = ConvertUtil.getString(dataList.get(index).get("employeeID"));
//            Map ruleMap = (Map) dataList.get(index).get("ruleMap");
//            String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
//            Object[] obj = new Object[]{userID,positionID,departID,employeeID,ruleMap,new HashMap()};
//            Object result = method.invoke(bl, obj);// 调用方法
//            assertEquals(expected,result.toString());
//        }
//      
//        method.setAccessible(false);
//    }
//  	//5
//    @Test
//    @Rollback(true)
//    @Transactional
//    public void testIsEfficientTypeTwo1() throws Exception{
//        String caseName = "testIsEfficientTypeTwo1";
//        bl = applicationContext.getBean(BINOLCM19_BL.class);
//        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//        //测试私有方法
//        Class<?>[] args = new Class[]{String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,Map.class};// 建立参数
//        Method method = bl.getClass().getDeclaredMethod("isEfficientTypeTwo",args);
//        method.setAccessible(true);// 允许处理私有方法
//      
//        for (int index = 0; index < dataList.size(); index++) {
//            String creatU = ConvertUtil.getString(dataList.get(index).get("creatU"));
//            String creatP = ConvertUtil.getString(dataList.get(index).get("creatP"));
//            String creatD = ConvertUtil.getString(dataList.get(index).get("creatD"));
//            String ruleCType = ConvertUtil.getString(dataList.get(index).get("ruleCType"));
//            String ruleCValue = ConvertUtil.getString(dataList.get(index).get("ruleCValue"));
//            String rulePType = ConvertUtil.getString(dataList.get(index).get("rulePType"));
//            String rulePValue = ConvertUtil.getString(dataList.get(index).get("rulePValue"));
//            String creatE = ConvertUtil.getString(dataList.get(index).get("creatE"));
//            String privilegeFlag = ConvertUtil.getString(dataList.get(index).get("privilegeFlag"));
//          
//            String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
//            Object[] obj = new Object[]{creatU,creatP,creatD,ruleCType,ruleCValue,rulePType,rulePValue,creatE,privilegeFlag,new HashMap()};
//            Object result = method.invoke(bl, obj);// 调用方法
//            assertEquals(expected,result.toString());
//        }
//      
//        method.setAccessible(false);
//    }
  
    //6
    @Test
    @Rollback(true)
    @Transactional
    public void testMacthCherryShowRule1() throws Exception{
        String caseName = "testMacthCherryShowRule1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
        workflow = applicationContext.getBean(Workflow.class);
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        String wfName = ConvertUtil.getWfName(userInfo.getOrganizationInfoCode(), userInfo.getBrandCode(), "promotionSDConfig");
        Map<String,Object> metaAttributes = new HashMap<String,Object>();
        for(int index=0;index<dataList.size();index++){
            String menuID = ConvertUtil.getString(dataList.get(index).get("menuID"));
            String OS_Rule = ConvertUtil.getString(dataList.get(index).get("OS_Rule"));
            int expected = CherryUtil.obj2int(dataList.get(index).get("expected"));
            if(!"".equals(OS_Rule)){
                metaAttributes.put("OS_Rule", OS_Rule);
            }else{
                metaAttributes.remove("OS_Rule");
            }
            workflow.getWorkflowDescriptor(wfName).getStep(10).setMetaAttributes(metaAttributes);
            assertEquals(expected,bl.macthCherryShowRule(userInfo, menuID));
        }
    }
    //7
    @Test
    @Rollback(true)
    @Transactional
    public void testGetCurrActionByOSID1() throws Exception{
        String caseName = "testGetCurrActionByOSID1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        binOLSTCM00_BL = applicationContext.getBean(BINOLSTCM00_BL.class);
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        //插入数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int billID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData = dataList.get(1);
        detailData.put("BIN_ProductOrderID", billID);
        testCOM_Service.insertTableData(detailData);
      
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID,billID);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, "3");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "1");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "1");
        pramMap.put("CurrentUnit", "TestCase");
        pramMap.put("BIN_BrandInfoID", "1");
        pramMap.put("UserInfo", userInfo);
        long wfID = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        ActionDescriptor[] ad = bl.getCurrActionByOSID(wfID, userInfo);
        assertEquals(null,ad);
    }
	//8
    @Test
    @Rollback(true)
    @Transactional
    public void testGetCurrActionByOSID2() throws Exception{
        String caseName = "testGetCurrActionByOSID2";
      
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        binOLSTCM00_BL = applicationContext.getBean(BINOLSTCM00_BL.class);
        workflow = applicationContext.getBean(Workflow.class);
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        //插入数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int billID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData = dataList.get(1);
        detailData.put("BIN_ProductOrderID", billID);
        testCOM_Service.insertTableData(detailData);
      
        //加载工作流文件
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        String flowName = "proFlowOD";
        String flowFile = "proFlowOD.xml";
        loadWorkFlowDescriptor(flowFile,flowName,orgCode,brandCode);
        
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID,billID);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, "1");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, "3");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, "1");
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, "1");
        pramMap.put("CurrentUnit", "TestCase");
        pramMap.put("BIN_BrandInfoID", "1");
        pramMap.put("UserInfo", userInfo);
        
        long wfID = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        CherryTaskInstance cherryTaskInstance = new CherryTaskInstance();
        cherryTaskInstance.setBillCode("OD2012");
        cherryTaskInstance.setBillCreator("2");
        cherryTaskInstance.setBillCreator_User("3");
        cherryTaskInstance.setBillCreator_Depart("1");
        cherryTaskInstance.setBillCreator_Position("1");
        cherryTaskInstance.setBillID(ConvertUtil.getString(billID));
        cherryTaskInstance.setBillType("OD");
        cherryTaskInstance.setCurrentOperate("50");
      
        int[] actionIDArr = workflow.getAvailableActions(wfID,null);
      
        String wfName = ConvertUtil.getWfName(userInfo.getOrganizationInfoCode(), userInfo.getBrandCode(), "proFlowOD");
        Map<String,Object> metaAttributes = workflow.getWorkflowDescriptor(wfName).getAction(actionIDArr[0]).getMetaAttributes();
        metaAttributes.put("OS_Rule", "{\"ThirdPartyFlag\":\"0\",\"RuleType\":\"2\",\"RuleContext\":[]}");
        workflow.getWorkflowDescriptor(wfName).getAction(actionIDArr[0]).setMetaAttributes(metaAttributes);
      
        PropertySet ps = workflow.getPropertySet(wfID);
        ps.setString("participant50", "userID3");
        ActionDescriptor[] ad = bl.getCurrActionByOSID(wfID, userInfo,cherryTaskInstance,new HashMap<String,Object>());
        assertEquals("icon-confirm",ad[0].getMetaAttributes().get("OS_ButtonClass"));
        assertEquals("50",ad[0].getMetaAttributes().get("OS_OperateCode"));
        assertEquals("os.receive",ad[0].getMetaAttributes().get("OS_ButtonNameCode"));
        assertEquals("999",ad[0].getMetaAttributes().get("OS_OperateResultCode"));
      
        ps.setString("participant50", "organizationID1");
        ad = bl.getCurrActionByOSID(wfID, userInfo,cherryTaskInstance,new HashMap<String,Object>());
        assertEquals("icon-confirm",ad[0].getMetaAttributes().get("OS_ButtonClass"));
        assertEquals("50",ad[0].getMetaAttributes().get("OS_OperateCode"));
        assertEquals("os.receive",ad[0].getMetaAttributes().get("OS_ButtonNameCode"));
        assertEquals("999",ad[0].getMetaAttributes().get("OS_OperateResultCode"));
      
        ps.setString("participant50", "positionID1");
        ad = bl.getCurrActionByOSID(wfID, userInfo,cherryTaskInstance,new HashMap<String,Object>());
        assertEquals("icon-confirm",ad[0].getMetaAttributes().get("OS_ButtonClass"));
        assertEquals("50",ad[0].getMetaAttributes().get("OS_OperateCode"));
        assertEquals("os.receive",ad[0].getMetaAttributes().get("OS_ButtonNameCode"));
        assertEquals("999",ad[0].getMetaAttributes().get("OS_OperateResultCode"));
    }
//    //9
//    @Test
//    @Rollback(true)
//    @Transactional
//    public void testCanDoAction1() throws Exception{
//        String caseName = "testCanDoAction1";
//        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);
//        bl = applicationContext.getBean(BINOLCM19_BL.class);
//        binOLSTCM00_BL = applicationContext.getBean(BINOLSTCM00_BL.class);
//        binOLSSCM00_BL = applicationContext.getBean(BINOLSSCM00_BL.class);
//        workflow = applicationContext.getBean(Workflow.class);
//        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
//
//        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//      
//        //测试私有方法
//        Class<?>[] args = new Class[]{UserInfo.class,PropertySet.class,String.class,CherryTaskInstance.class,Map.class};// 建立参数
//        Method method = bl.getClass().getDeclaredMethod("canDoAction",args);
//
//        method.setAccessible(true);
//      
//        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
//        List<Map<String,Object>> assertList = (List<Map<String,Object>>)dataMap.get("assertList");
//        for(int index=0;index<assertList.size();index++){
//            int billID = 0;
//            //插入数据
//            List<Map<String,Object>> insertList = (List)assertList.get(index).get("insertList");
//            if(null != insertList && insertList.size()>0){
//                Map<String,Object> mainData = new HashMap<String,Object>();
//                mainData.putAll(insertList.get(0));
//                billID = testCOM_Service.insertTableData(mainData);
//                Map<String,Object> detailData = new HashMap<String,Object>();
//                detailData.putAll(insertList.get(1));
//                detailData.put(ConvertUtil.getString(assertList.get(index).get("BillIDName")), billID);
//                testCOM_Service.insertTableData(detailData);
//            }else{
//                Map<String,Object> mainData = new HashMap<String,Object>();
//                mainData.putAll(dataList.get(0));
//                mainData.put("OrderNo", mainData.get("OrderNo")+ConvertUtil.getString(index));
//                mainData.put("OrderNoIF", mainData.get("OrderNoIF")+ConvertUtil.getString(index));
//                billID = testCOM_Service.insertTableData(mainData);
//                Map<String,Object> detailData = new HashMap<String,Object>();
//                detailData.putAll(dataList.get(1));
//                detailData.put("BIN_ProductOrderID", billID);
//                testCOM_Service.insertTableData(detailData);
//            }
//            //准备参数，开始工作流
//            Map<String,Object> pramMap = new HashMap<String,Object>();
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, ConvertUtil.getString(assertList.get(index).get("BillType")));
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, billID);
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, ConvertUtil.getString(assertList.get(index).get("BillCreator")));
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(assertList.get(index).get("BillCreator_User")));
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(assertList.get(index).get("BillCreator_Depart")));
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(assertList.get(index).get("BillCreator_Position")));
//            pramMap.put("CurrentUnit", "TestCase");
//            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//            pramMap.put("UserInfo", userInfo);
//          
//            long wfID = 0;
//            String proType = ConvertUtil.getString(assertList.get(index).get("ProType"));
//            if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
//                wfID = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
//            }else if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
//                wfID = binOLSSCM00_BL.StartOSWorkFlow(pramMap);
//            }else{
//                fail(index + "测试参数不正确！");
//            }
//          
//            String rule = ConvertUtil.getString(assertList.get(index).get("rule"));
//            PropertySet ps = workflow.getPropertySet(wfID);
//
//            CherryTaskInstance cherryTaskInstance = new CherryTaskInstance();
//            cherryTaskInstance.setBillCode(ConvertUtil.getString(assertList.get(index).get("BillCode")));
//            cherryTaskInstance.setBillCreator(ConvertUtil.getString(assertList.get(index).get("BillCreator")));
//            cherryTaskInstance.setBillCreator_User(ConvertUtil.getString(assertList.get(index).get("BillCreator_User")));
//            cherryTaskInstance.setBillCreator_Depart(ConvertUtil.getString(assertList.get(index).get("BillCreator_Depart")));
//            cherryTaskInstance.setBillCreator_Position(ConvertUtil.getString(assertList.get(index).get("BillCreator_Position")));
//            cherryTaskInstance.setBillID(ConvertUtil.getString(billID));
//            cherryTaskInstance.setBillType(ConvertUtil.getString(assertList.get(index).get("BillType")));
//            cherryTaskInstance.setCurrentOperate(ConvertUtil.getString(assertList.get(index).get("CurrentOperate")));
//            cherryTaskInstance.setProType(ConvertUtil.getString(assertList.get(index).get("ProType")));
//            if(cherryTaskInstance.getCurrentOperate().equals(CherryConstants.OPERATE_LG)){
//                ps.setInt("BIN_OrganizationIDAccept", 100);
//            }
//            Object[] obj = new Object[]{userInfo,ps,rule,cherryTaskInstance,new HashMap()};
//            Object result = method.invoke(bl, obj);// 调用方法
//          
//            assertEquals(String.valueOf(index+1),ConvertUtil.getString(assertList.get(index).get("expected")),result.toString());
//        }
//      
//        method.setAccessible(false);
//    }
    	//  10
    @Test
    @Rollback(true)
    @Transactional
    public void testFindMatchingAuditor1() throws Exception{
        String caseName = "testFindMatchingAuditor1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);

        Map<String,String> map = new HashMap<String,String>();
        for (int index = 0; index < dataList.size(); index++) {
            map.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_USER)));
            map.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_POSITION)));
            map.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
            map.put(CherryConstants.OS_META_Rule, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_META_Rule)));
            map.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, ConvertUtil.getString(dataList.get(index).get(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE)));
            assertEquals(dataList.get(index).get("expected"),bl.findMatchingAuditor(map));
        }
    }
    //  11
    @Test
    @Rollback(true)
    @Transactional
    public void testFindAuditor1() throws Exception{
        String caseName = "testFindAuditor1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
      
        //插入权限
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.put("tableName", "Privilege.BIN_EmployeePrivilege");
        insertData.put("BIN_UserID", "5");
        insertData.put("BIN_EmployeeID", "3");
        insertData.put("BIN_SubEmployeeID", "16");
        insertData.put("BusinessType", "A");
        insertData.put("OperationType", "0");
        insertData.put("PrivilegeFlag", "1");
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        testCOM_Service.insertTableDataNoReturnID(insertData);
        
        //测试私有方法
        Class<?>[] args = new Class[]{String.class,String.class,String.class,String.class,String.class,Map.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("findAuditor",args);
        method.setAccessible(true);// 允许处理私有方法
      
        for (int index = 0; index < dataList.size(); index++) {
            String userID = ConvertUtil.getString(dataList.get(index).get("userID"));
            String positionID = ConvertUtil.getString(dataList.get(index).get("positionID"));
            String departID = ConvertUtil.getString(dataList.get(index).get("departID"));
            String employeeID = ConvertUtil.getString(dataList.get(index).get("employeeID"));
            Map ruleMap = (Map) dataList.get(index).get("ruleMap");
            String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
            Object[] obj = new Object[]{userID,positionID,departID,employeeID,"",ruleMap};
            Object result = method.invoke(bl, obj);// 调用方法
            assertEquals(index+"验证出错",expected,result.toString());
        }
      
        method.setAccessible(false);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInsertInventoryUserTask1() throws Exception{
        String caseName = "testInsertInventoryUserTask1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        Map<String,Object> insertMap = dataList.get(0);
        int inventoryUserTaskID = bl.insertInventoryUserTask(insertMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("BIN_InventoryUserTaskID", inventoryUserTaskID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_InventoryUserTask");

        for(int j=0;j<fieldsList.size();j++){
            String key = ConvertUtil.getString(fieldsList.get(j));
            String expectedValue = ConvertUtil.getString(insertMap.get(key));
            String actualValue = ConvertUtil.getString(actualList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testUpdateInventoryUserTask1() throws Exception{
        String caseName = "testUpdateInventoryUserTask1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        Map<String,Object> insertMap = dataList.get(0);
        int inventoryUserTaskID = bl.insertInventoryUserTask(insertMap);
        
        Map<String,Object> updateMap = dataList.get(0);
        int cnt = bl.updateInventoryUserTask(updateMap);
        assertEquals(1,cnt);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("BIN_InventoryUserTaskID", inventoryUserTaskID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_InventoryUserTask");

        for(int j=0;j<fieldsList.size();j++){
            String key = ConvertUtil.getString(fieldsList.get(j));
            String expectedValue = ConvertUtil.getString(insertMap.get(key));
            String actualValue = ConvertUtil.getString(actualList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testDeleteInventoryUserTask1() throws Exception{
        String caseName = "testDeleteInventoryUserTask1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        Map<String,Object> insertMap = dataList.get(0);
        int inventoryUserTaskID = bl.insertInventoryUserTask(insertMap);
        
        Map<String,Object> deleteMap = new HashMap<String,Object>();
        deleteMap.put("WorkFlowID", insertMap.get("WorkFlowID"));
        int cnt = bl.deleteInventoryUserTask(deleteMap);
        assertEquals(1,cnt);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_InventoryUserTask");
        paramMap.put("BIN_InventoryUserTaskID", inventoryUserTaskID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(0,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetInventoryUserTaskByOSID1() throws Exception{
        String caseName = "testGetInventoryUserTaskByOSID1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        Map<String,Object> insertMap = dataList.get(0);
        int inventoryUserTaskID = bl.insertInventoryUserTask(insertMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("WorkFlowID", insertMap.get("WorkFlowID"));
        List<Map<String,Object>> userTaskList = bl.getInventoryUserTaskByOSID(paramMap);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_InventoryUserTask");

        for(int j=0;j<fieldsList.size();j++){
            String key = ConvertUtil.getString(fieldsList.get(j));
            String expectedValue = ConvertUtil.getString(insertMap.get(key));
            String actualValue = ConvertUtil.getString(userTaskList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetInventoryUserTask1() throws Exception{
        String caseName = "testGetInventoryUserTask1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        Map<String,Object> insertMap = dataList.get(0);
        int inventoryUserTaskID = bl.insertInventoryUserTask(insertMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", "99");
        paramMap.put("BIN_BrandInfoID", "99");
        paramMap.put("userID", "U1,");
        paramMap.put("positionID", "P1,");
        paramMap.put("organizationID", "D1,");
        List<Map<String,Object>> userTaskList = bl.getInventoryUserTask(paramMap);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_InventoryUserTask");

        for(int j=0;j<fieldsList.size();j++){
            String key = ConvertUtil.getString(fieldsList.get(j));
            String expectedValue = ConvertUtil.getString(insertMap.get(key));
            String actualValue = ConvertUtil.getString(userTaskList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testProcessingCommaString1() throws Exception{
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        String participant = "U1";
        assertEquals("U1,",bl.processingCommaString(participant));
        
        participant = "U1,";
        assertEquals("U1,",bl.processingCommaString(participant));
        
        participant = "U1,P1";
        assertEquals("U1,P1,",bl.processingCommaString(participant));
        
        participant = "U1,P1,,,";
        assertEquals("U1,P1,",bl.processingCommaString(participant));
        
        participant = "U1,P1,U1,P1,";
        assertEquals("U1,P1,",bl.processingCommaString(participant));
        
        participant = "U1,,U1,P1,";
        assertEquals("U1,P1,",bl.processingCommaString(participant));
        
        participant = "U1,,U3,P1,D1,";
        assertEquals("U1,U3,P1,D1,",bl.processingCommaString(participant));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetBillTypeByTableName1() throws Exception{
        String caseName = "testGetBillTypeByTableName1";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //测试私有方法
        Class<?>[] args = new Class[]{String.class};// 建立参数
        Method method = bl.getClass().getDeclaredMethod("getBillTypeByTableName",args);
        method.setAccessible(true);// 允许处理私有方法
        
        List<Map<String,Object>> exceptedList = (List<Map<String, Object>>) dataMap.get("exceptedList");
        Map<String,Object> tableMap = exceptedList.get(0);
        for(Map.Entry<String,Object> en:tableMap.entrySet()){
            Object[] obj = new Object[]{en.getKey()};
            Object result = method.invoke(bl, obj);// 调用方法
            assertEquals(en.getValue(),result.toString());
        }

        method.setAccessible(false);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetUserTasks1() throws Exception{
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", 0);
        paramMap.put("BIN_BrandInfoID", 0);
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, 0);
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, 0);
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, 0);
        List<CherryTaskInstance> actualList = bl.getUserTasks(paramMap);
        assertEquals(0,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetUserTasks2() throws Exception{
        String caseName = "testGetUserTasks2";
        bl = applicationContext.getBean(BINOLCM19_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(), caseName);

        //插入数据
        //Inventory.BIN_InventoryUserTask
        Map<String,Object> inventoryUserTaskInsertMap1 = dataList.get(0);
        testCOM_Service.insertTableData(inventoryUserTaskInsertMap1);
        
        Map<String,Object> inventoryUserTaskInsertMap2 = dataList.get(1);
        testCOM_Service.insertTableData(inventoryUserTaskInsertMap2);
        
        Map<String,Object> inventoryUserTaskInsertMap3 = dataList.get(2);
        testCOM_Service.insertTableData(inventoryUserTaskInsertMap3);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        paramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        List<CherryTaskInstance> actualList = bl.getUserTasks(paramMap);
        assertEquals(3,actualList.size());
        assertEquals(inventoryUserTaskInsertMap1.get("BillNo"),actualList.get(0).getBillCode());
        assertEquals(inventoryUserTaskInsertMap2.get("BillNo"),actualList.get(1).getBillCode());
        assertEquals(inventoryUserTaskInsertMap3.get("BillNo"),actualList.get(2).getBillCode());
        assertEquals(inventoryUserTaskInsertMap1.get("BillID"),actualList.get(0).getBillID());
        assertEquals(inventoryUserTaskInsertMap2.get("BillID"),actualList.get(1).getBillID());
        assertEquals(inventoryUserTaskInsertMap3.get("BillID"),actualList.get(2).getBillID());
    }
    
    /**
     * 获取工作流文件路径
     * @param fileName
     * @return
     */
    public String getWorkFlowFilePath(String fileName){
        String rootpath = ProFlowOD_FN.class.getResource("/").getPath();
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
