package com.cherry.st.ios.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.ios.form.BINOLSTIOS02_Form;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLSTIOS02_Action_TEST extends CherryJunitBase {

    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service TESTCOM_Service;
        
    private BINOLSTIOS02_Action action;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTIOS02_Action.class, "/st","BINOLSTIOS02_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(0);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = TESTCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(1);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        TESTCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception {
        String caseName = "testInit2";
        action = createAction(BINOLSTIOS02_Action.class, "/st","BINOLSTIOS02_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql1 = sql1.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql1 = sql1.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql1);
        String sql2 = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql2 = sql2.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql2 = sql2.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql2);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(0);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = TESTCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(1);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        TESTCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertEquals("false",action.getModel().getCounterShiftFlag());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit3() throws Exception {
        String caseName = "testInit3";
        action = createAction(BINOLSTIOS02_Action.class, "/st","BINOLSTIOS02_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql1 = sql1.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql1 = sql1.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql1);
        String sql2 = ConvertUtil.getString(sqlList.get(1).get("sql"));
        sql2 = sql2.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql2 = sql2.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql2);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(0);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = TESTCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(1);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        TESTCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertEquals("true",action.getModel().getCounterShiftFlag());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetLogicInfo1() throws Exception {
        String caseName = "testGetLogicInfo1";
        action = createAction(BINOLSTIOS02_Action.class, "/st","BINOLSTIOS02_getLogicInfo");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql1 = sql1.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql1 = sql1.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql1);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode", "TCDepart001");
        List<Map<String,Object>> organList = TESTCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationInfoID"));
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(0);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = TESTCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(1);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        TESTCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        BINOLSTIOS02_Form form = action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        form.setDepartId(organizationID);
        
        //proxy代理
        proxy.execute();
        List<Map<String, Object>> logicList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
        assertEquals(1,logicList.size());
        assertEquals(insertLogicInventoryMap.get("LogicInventoryCode"),logicList.get(0).get("LogicInventoryCode"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetLogicInfo2() throws Exception {
        String caseName = "testGetLogicInfo2";
        action = createAction(BINOLSTIOS02_Action.class, "/st","BINOLSTIOS02_getLogicInfo");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql1 = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql1 = sql1.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql1 = sql1.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        TESTCOM_Service.insert(sql1);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode", "TCCounter001");
        List<Map<String,Object>> organList = TESTCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationInfoID"));
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(0);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = TESTCOM_Service.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(1);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        TESTCOM_Service.insertTableData(insertLogicDepotBusiness);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        BINOLSTIOS02_Form form = action.getModel();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        form.setDepartId(organizationID);
        
        //proxy代理
        proxy.execute();
        List<Map<String, Object>> logicList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
        assertEquals(1,logicList.size());
        assertEquals(insertLogicInventoryMap.get("LogicInventoryCode"),logicList.get(0).get("LogicInventoryCode"));
    }
}