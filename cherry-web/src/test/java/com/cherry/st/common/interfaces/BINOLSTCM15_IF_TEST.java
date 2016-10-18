package com.cherry.st.common.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSTCM15_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM15_BL")
    private BINOLSTCM15_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetShowRecStockFlag1() throws Exception {
        String caseName = "testGetShowRecStockFlag1";
        bl = applicationContext.getBean(BINOLSTCM15_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode","TCDepart001");
        List<Map<String,Object>> organList = testCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationID"));
        
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(0);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        Map<String,Object> mainData = dataList.get(0);
        mainData.put("BIN_OrganizationID", organizationID);
        Map<String,Object> paramMap = new HashMap<String,Object> ();
        paramMap.put("mainData", mainData);
        paramMap.put("userInfo", userInfo);
        String flag = bl.getShowRecStockFlag(paramMap);
        assertEquals("true",flag);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetShowRecStockFlag2() throws Exception {
        String caseName = "testGetShowRecStockFlag2";
        bl = applicationContext.getBean(BINOLSTCM15_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode","TCDepart001");
        List<Map<String,Object>> organList = testCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationID"));
        
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(0);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        Map<String,Object> mainData = dataList.get(0);
        mainData.put("BIN_OrganizationIDReceive", organizationID);
        Map<String,Object> paramMap = new HashMap<String,Object> ();
        paramMap.put("mainData", mainData);
        paramMap.put("userInfo", userInfo);
        String flag = bl.getShowRecStockFlag(paramMap);
        assertEquals("true",flag);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetShowRecStockFlag3() throws Exception {
        String caseName = "testGetShowRecStockFlag3";
        bl = applicationContext.getBean(BINOLSTCM15_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode","TCDepart001");
        List<Map<String,Object>> organList = testCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationID"));
        
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(0);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        Map<String,Object> mainData = dataList.get(0);
        mainData.put("BIN_OrganizationID", 0);
        Map<String,Object> paramMap = new HashMap<String,Object> ();
        paramMap.put("mainData", mainData);
        paramMap.put("userInfo", userInfo);
        String flag = bl.getShowRecStockFlag(paramMap);
        assertEquals("false",flag);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetShowRecStockFlag4() throws Exception {
        String caseName = "testGetShowRecStockFlag4";
        bl = applicationContext.getBean(BINOLSTCM15_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode","TCDepart001");
        List<Map<String,Object>> organList = testCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationID"));
        
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(0);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        Map<String,Object> mainData = dataList.get(0);
        mainData.put("BIN_OrganizationIDReceive", 0);
        Map<String,Object> paramMap = new HashMap<String,Object> ();
        paramMap.put("mainData", mainData);
        paramMap.put("userInfo", userInfo);
        String flag = bl.getShowRecStockFlag(paramMap);
        assertEquals("false",flag);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetShowRecStockFlag5() throws Exception {
        String caseName = "testGetShowRecStockFlag5";
        bl = applicationContext.getBean(BINOLSTCM15_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List<Map<String, Object>>) dataMap.get("sqlList");
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        testCOM_Service.insert(sql);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Basis.BIN_Organization");
        param.put("DepartCode","TCDepart001");
        List<Map<String,Object>> organList = testCOM_Service.getTableData(param);
        String organizationID = ConvertUtil.getString(organList.get(0).get("BIN_OrganizationID"));
        
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(0);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        Map<String,Object> mainData = dataList.get(0);
        mainData.put("BIN_OrganizationIDReceive", 0);
        Map<String,Object> paramMap = new HashMap<String,Object> ();
        paramMap.put("mainData", mainData);
        paramMap.put("userInfo", userInfo);
        String flag = bl.getShowRecStockFlag(paramMap);
        assertEquals("false",flag);
    }
}