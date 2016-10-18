package com.cherry.st.sfh.action;

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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.sfh.form.BINOLSTSFH01_Form;

public class BINOLSTSFH01_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testCOM_Service;
	
	private BINOLSTSFH01_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTSFH01_Action.class, "/st","BINOLSTSFH01_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCDepart001");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        String departCode = ConvertUtil.getString(orgList.get(0).get("DepartCode"));
        String departName = ConvertUtil.getString(orgList.get(0).get("DepartName"));
        userInfo.setBIN_OrganizationID(organizationID);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(0);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(1);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(2);
        logicInventoryInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        logicInventoryInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = testCOM_Service.insertTableData(logicInventoryInsertMap);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        List<Map<String,Object>> inDepotList = action.getModel().getInDepotList();
        assertEquals(ConvertUtil.getString(depotInfoID),ConvertUtil.getString(inDepotList.get(0).get("BIN_DepotInfoID")));
        
        List<Map<String,Object>> inLogicDepotList = action.getModel().getInLogicDepotList();
        assertTrue(inLogicDepotList.size()>0);
        
        Map<String,Object> initInfoMap = action.getModel().getInitInfoMap();
        assertEquals(ConvertUtil.getString(organizationID),ConvertUtil.getString(initInfoMap.get("defaultDepartID")));
        assertEquals("",ConvertUtil.getString(initInfoMap.get("defaultOutDepartID")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSave1() throws Exception {
        String caseName = "testSave1";
        action = createAction(BINOLSTSFH01_Action.class, "/st","BINOLSTSFH01_save");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCDepart0011");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        userInfo.setBIN_OrganizationID(organizationID);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(0);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(1);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        BINOLSTSFH01_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setInOrganizationId(ConvertUtil.getString(organizationID));
        form.setInDepotId(ConvertUtil.getString(depotInfoID));
        
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
        
        Map<String,Object> paramActual = new HashMap<String,Object>();
        paramActual.put("tableName", "Inventory.BIN_ProductOrder");
        paramActual.put("BIN_OrganizationID", organizationID);
        List<Map<String,Object>> actualMainList = testCOM_Service.getTableData(paramActual);
        int billID = CherryUtil.obj2int(actualMainList.get(0).get("BIN_ProductOrderID"));
        assertEquals(depotInfoID,actualMainList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualMainList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals("0",actualMainList.get(0).get("VerifiedFlag"));
        assertEquals("10",actualMainList.get(0).get("TradeStatus"));
        assertTrue(ConvertUtil.getString(actualMainList.get(0).get("OrderTime")).length()==8);
        
        paramActual = new HashMap<String,Object>();
        paramActual.put("tableName", "Inventory.BIN_ProductOrderDetail");
        paramActual.put("BIN_ProductOrderID", billID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(paramActual);
        assertEquals(form.getProductVendorIDArr()[0],ConvertUtil.getString(actualDetailList.get(0).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[1],ConvertUtil.getString(actualDetailList.get(1).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[2],ConvertUtil.getString(actualDetailList.get(2).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[3],ConvertUtil.getString(actualDetailList.get(3).get("BIN_ProductVendorID")));
        
        assertEquals(depotInfoID,actualDetailList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(2).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(3).get("BIN_InventoryInfoID"));
        
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(2).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(3).get("BIN_LogicInventoryInfoID")));

        assertEquals(form.getQuantityArr()[0],ConvertUtil.getString(actualDetailList.get(0).get("Quantity")));
        assertEquals(form.getQuantityArr()[1],ConvertUtil.getString(actualDetailList.get(1).get("Quantity")));
        assertEquals(form.getQuantityArr()[2],ConvertUtil.getString(actualDetailList.get(2).get("Quantity")));
        assertEquals(form.getQuantityArr()[3],ConvertUtil.getString(actualDetailList.get(3).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSubmit1() throws Exception {
        String caseName = "testSubmit1";
        action = createAction(BINOLSTSFH01_Action.class, "/st","BINOLSTSFH01_submit");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String,Object>>)dataMap.get("sqlList");

        //插入数据
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("tableName", "Basis.BIN_Organization");
        searchParam.put("DepartCode", "TCDepart0011");
        List<Map<String,Object>> orgList = testCOM_Service.getTableData(searchParam);
        int organizationID = CherryUtil.obj2int(orgList.get(0).get("BIN_OrganizationID"));
        userInfo.setBIN_OrganizationID(organizationID);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> insertDepotInfoInfoMap = dataList.get(0);
        insertDepotInfoInfoMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        int depotInfoID = testCOM_Service.insertTableData(insertDepotInfoInfoMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> insertInventoryInfoMap = dataList.get(1);
        insertInventoryInfoMap.put("BIN_InventoryInfoID", depotInfoID);
        insertInventoryInfoMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(insertInventoryInfoMap);
        
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        BINOLSTSFH01_Form form = action.getModel();
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setInOrganizationId(ConvertUtil.getString(organizationID));
        form.setInDepotId(ConvertUtil.getString(depotInfoID));
        
        assertEquals(CherryConstants.GLOBAL_ACCTION_RESULT,proxy.execute());
        
        Map<String,Object> paramActual = new HashMap<String,Object>();
        paramActual.put("tableName", "Inventory.BIN_ProductOrder");
        paramActual.put("BIN_OrganizationID", organizationID);
        List<Map<String,Object>> actualMainList = testCOM_Service.getTableData(paramActual);
        int billID = CherryUtil.obj2int(actualMainList.get(0).get("BIN_ProductOrderID"));
        assertEquals(depotInfoID,actualMainList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualMainList.get(0).get("BIN_LogicInventoryInfoID")));
        assertTrue(ConvertUtil.getString(actualMainList.get(0).get("WorkFlowID")).length()>0);
        assertTrue(ConvertUtil.getString(actualMainList.get(0).get("OrderTime")).length()==8);
        
        paramActual = new HashMap<String,Object>();
        paramActual.put("tableName", "Inventory.BIN_ProductOrderDetail");
        paramActual.put("BIN_ProductOrderID", billID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(paramActual);
        assertEquals(form.getProductVendorIDArr()[0],ConvertUtil.getString(actualDetailList.get(0).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[1],ConvertUtil.getString(actualDetailList.get(1).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[2],ConvertUtil.getString(actualDetailList.get(2).get("BIN_ProductVendorID")));
        assertEquals(form.getProductVendorIDArr()[3],ConvertUtil.getString(actualDetailList.get(3).get("BIN_ProductVendorID")));
        
        assertEquals(depotInfoID,actualDetailList.get(0).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(1).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(2).get("BIN_InventoryInfoID"));
        assertEquals(depotInfoID,actualDetailList.get(3).get("BIN_InventoryInfoID"));
        
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(1).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(2).get("BIN_LogicInventoryInfoID")));
        assertEquals(form.getInLogicDepotId(),ConvertUtil.getString(actualDetailList.get(3).get("BIN_LogicInventoryInfoID")));

        assertEquals(form.getQuantityArr()[0],ConvertUtil.getString(actualDetailList.get(0).get("Quantity")));
        assertEquals(form.getQuantityArr()[1],ConvertUtil.getString(actualDetailList.get(1).get("Quantity")));
        assertEquals(form.getQuantityArr()[2],ConvertUtil.getString(actualDetailList.get(2).get("Quantity")));
        assertEquals(form.getQuantityArr()[3],ConvertUtil.getString(actualDetailList.get(3).get("Quantity")));
    }
}