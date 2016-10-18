package com.cherry.mo.cio.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.cio.form.BINOLMOCIO07_Form;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLMOCIO07_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	private BINOLMOCIO07_Action action;
	
	private int insertOrganization(String newOrgNodeId,Map<String,Object> insertOrgSQLMap,int organizationInfoID,int brandInfoID){
        String sql = ConvertUtil.getString(insertOrgSQLMap.get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        sql = sql.replaceAll("#NewPath#", newOrgNodeId);
        String path = "";
        Pattern pattern = Pattern.compile("'/.*/'");
        Matcher matcher = pattern.matcher(sql);
        if(matcher.find()){             
            path = matcher.group();
            path = path.replace("'", "");
        }
        testService.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("Path", path);
        List<Map<String,Object>> tableList = testService.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(tableList.get(0).get("BIN_OrganizationID"));
        return organizationID;
	}
	
    private int insertEmployee(String newEmpNodeIDSql,Map<String,Object> insertEmpSQLMap,int organizationInfoID,int brandInfoID,int positionCategoryID){
        List<Map<String,Object>> nodeList = testService.select(newEmpNodeIDSql);
        String newOrgNodeId = ConvertUtil.getString(nodeList.get(0).get("newNodeId"));
        String sql = ConvertUtil.getString(insertEmpSQLMap.get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", String.valueOf(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", String.valueOf(brandInfoID));
        sql = sql.replaceAll("#BIN_PositionCategoryID#", String.valueOf(positionCategoryID));
        sql = sql.replaceAll("#NewPath#", newOrgNodeId);
        String path = "";
        Pattern pattern = Pattern.compile("'/.*/'");
        Matcher matcher = pattern.matcher(sql);
        if(matcher.find()){             
            path = matcher.group();
            path = path.replace("'", "");
        }
        testService.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("Path", path);
        List<Map<String,Object>> tableList = testService.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(tableList.get(0).get("BIN_EmployeeID"));
        return employeeID;
    }
	
    public Map<String,Object> insertBaseData(UserInfo userInfo) throws Exception{
        Map<String,Object> returnMap = new HashMap<String,Object>();
        //插入数据
        Map<String,Object> insertDBMap = DataUtil.getDataMap(this.getClass(),"insertDB");
        List<Map<String,Object>> insertList = (List<Map<String,Object>>) insertDBMap.get("insertList");
        
        //Basis.BIN_OrganizationInfo
        Map<String,Object> insertOrganizationInfoMap = insertList.get(0); 
        int organizationInfoID = testService.insertTableData(insertOrganizationInfoMap);
        userInfo.setBIN_OrganizationInfoID(organizationInfoID);
        
        //Basis.BIN_BrandInfo
        Map<String,Object> insertBrandInfoMap = insertList.get(1);
        insertBrandInfoMap.put("BIN_OrganizationInfoID",organizationInfoID);
        int brandInfoID = testService.insertTableData(insertBrandInfoMap);
        userInfo.setBIN_BrandInfoID(brandInfoID);
        
        List<Map<String,Object>> searchSQLList = (List<Map<String,Object>>) insertDBMap.get("searchSQLList");
        String newOrgNodeIDSql = ConvertUtil.getString(searchSQLList.get(0).get("sql"));
        String newEmpNodeIDSql = ConvertUtil.getString(searchSQLList.get(1).get("sql"));

        List<Map<String,Object>> insertOrgSQLList = (List<Map<String,Object>>) insertDBMap.get("insertOrgSQLList");
        List<Map<String,Object>> nodeList = testService.select(newOrgNodeIDSql);
        String newOrgNodeId = ConvertUtil.getString(nodeList.get(0).get("newNodeId"));
        int organizationID1 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(0),organizationInfoID,brandInfoID);//品牌
        returnMap.put("organizationID1", organizationID1);
        int organizationID2 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(1),organizationInfoID,brandInfoID);//大区
        returnMap.put("organizationID2", organizationID2);
        int organizationID3 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(2),organizationInfoID,brandInfoID);//大区
        returnMap.put("organizationID3", organizationID3);
        int organizationID4 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(3),organizationInfoID,brandInfoID);//办事处
        returnMap.put("organizationID4", organizationID4);
        int organizationID5 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(4),organizationInfoID,brandInfoID);//办事处
        returnMap.put("organizationID5", organizationID5);
        int organizationID6 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(5),organizationInfoID,brandInfoID);//柜台
        returnMap.put("organizationID6", organizationID6);
        int organizationID7 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(6),organizationInfoID,brandInfoID);//柜台
        returnMap.put("organizationID7", organizationID7);
        int organizationID8 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(7),organizationInfoID,brandInfoID);//柜台
        returnMap.put("organizationID8", organizationID8);
        int organizationID9 = insertOrganization(newOrgNodeId,insertOrgSQLList.get(8),organizationInfoID,brandInfoID);//柜台
        returnMap.put("organizationID9", organizationID9);
        
        //Privilege.BIN_PositionCategory
        Map<String,Object> insertPositionCategoryMap = insertList.get(2);
        insertPositionCategoryMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPositionCategoryMap.put("BIN_BrandInfoID", brandInfoID);
        int positionCategoryID = testService.insertTableData(insertPositionCategoryMap);
        returnMap.put("positionCategoryID", positionCategoryID);
        
        List<Map<String,Object>> insertEmpSQLList = (List<Map<String,Object>>) insertDBMap.get("insertEmpSQLList");
        int employeeID1 = insertEmployee(newEmpNodeIDSql, insertEmpSQLList.get(0), organizationInfoID, brandInfoID, positionCategoryID);
        returnMap.put("employeeID1", employeeID1);
        int employeeID2 = insertEmployee(newEmpNodeIDSql, insertEmpSQLList.get(1), organizationInfoID, brandInfoID, positionCategoryID);
        returnMap.put("employeeID2", employeeID2);
        int employeeID3 = insertEmployee(newEmpNodeIDSql, insertEmpSQLList.get(2), organizationInfoID, brandInfoID, positionCategoryID);
        returnMap.put("employeeID3", employeeID3);
        
        //Basis.BIN_BaInfo
        Map<String,Object> insertBaInfoMap1 = insertList.get(3);
        insertBaInfoMap1.put("BIN_OrganizationInfoID", organizationInfoID);
        insertBaInfoMap1.put("BIN_BrandInfoID", brandInfoID);
        insertBaInfoMap1.put("BIN_EmployeeID", employeeID1);
        int baInfoID1 = testService.insertTableData(insertBaInfoMap1);
        returnMap.put("baInfoID1", baInfoID1);
        
        //Basis.BIN_BaInfo
        Map<String,Object> insertBaInfoMap2 = insertList.get(4);
        insertBaInfoMap2.put("BIN_OrganizationInfoID", organizationInfoID);
        insertBaInfoMap2.put("BIN_BrandInfoID", brandInfoID);
        insertBaInfoMap2.put("BIN_EmployeeID", employeeID2);
        int baInfoID2 = testService.insertTableData(insertBaInfoMap2);
        returnMap.put("baInfoID2", baInfoID2);
        
        //Basis.BIN_BaInfo
        Map<String,Object> insertBaInfoMap3 = insertList.get(5);
        insertBaInfoMap3.put("BIN_OrganizationInfoID", organizationInfoID);
        insertBaInfoMap3.put("BIN_BrandInfoID", brandInfoID);
        insertBaInfoMap3.put("BIN_EmployeeID", employeeID3);
        int baInfoID3 = testService.insertTableData(insertBaInfoMap3);
        returnMap.put("baInfoID3", baInfoID3);
        
        //Privilege.BIN_User
        Map<String,Object> insertUserMap = insertList.get(6);
        insertUserMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int userID = testService.insertTableData(insertUserMap);
        userInfo.setBIN_UserID(userID);
        returnMap.put("userID", userID);
        
        //Privilege.BIN_DepartPrivilege
        Map<String,Object> insertDepartPrivilegeMap = insertList.get(7);
        insertDepartPrivilegeMap.put("BIN_UserID", userID);
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID2);
        testService.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID6);
        testService.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        insertDepartPrivilegeMap.put("BIN_OrganizationID", organizationID7);
        testService.insertTableDataNoReturnID(insertDepartPrivilegeMap);
        
        //Privilege.BIN_EmployeePrivilege
        Map<String,Object> insertEmployeePrivilegeMap = insertList.get(8);
        insertEmployeePrivilegeMap.put("BIN_UserID", userID);
        insertEmployeePrivilegeMap.put("BIN_SubEmployeeID", employeeID1);
        testService.insertTableDataNoReturnID(insertEmployeePrivilegeMap);
        
        insertEmployeePrivilegeMap.put("BIN_SubEmployeeID", employeeID3);
        testService.insertTableDataNoReturnID(insertEmployeePrivilegeMap);
        
        return returnMap;
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSearch1() throws Exception {
        String caseName = "testSearch1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        String language = userInfo.getLanguage();

        //插入基础数据
        Map<String,Object> baseData = insertBaseData(userInfo);
        
        //区域
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_search");
        BINOLMOCIO07_Form form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setType("1");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOCIO07_1", proxy.execute());
        assertEquals(1,form.getSaleTargetList().size());
        assertEquals(baseData.get("organizationID2"),form.getSaleTargetList().get(0).get("parameterID"));
        
        //柜台
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_search");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setType("2");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOCIO07_1", proxy.execute());
        assertEquals(2,form.getSaleTargetList().size());
        assertEquals(baseData.get("organizationID6"),form.getSaleTargetList().get(0).get("parameterID"));
        assertEquals(baseData.get("organizationID7"),form.getSaleTargetList().get(1).get("parameterID"));
        
        //美容顾问
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_search");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setType("3");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("BINOLMOCIO07_1", proxy.execute());
        assertEquals(2,form.getSaleTargetList().size());
        assertEquals(baseData.get("baInfoID1"),form.getSaleTargetList().get(0).get("parameterID"));
        assertEquals(baseData.get("baInfoID3"),form.getSaleTargetList().get(1).get("parameterID"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetTreeNodes1() throws Exception {
        String caseName = "testGetTreeNodes1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        String language = userInfo.getLanguage();
        
        //插入基础数据
        Map<String,Object> baseData = insertBaseData(userInfo);

        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expectedList");
        
        //区域
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_getTreeNodes");
        BINOLMOCIO07_Form form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddtype("1");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        proxy.execute();
        List<Map<String, Object>> treeList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
        assertEquals(expectedList.get(0).get("differentArr1"),treeList.get(0).get("differentArr"));
        List<Map<String,Object>> nodes = (List<Map<String, Object>>) treeList.get(0).get("nodes");
        assertEquals(expectedList.get(0).get("differentArr2"),nodes.get(0).get("differentArr"));

        //柜台
        response = new MockHttpServletResponse();
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_getTreeNodes");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddtype("2");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        proxy.execute();
        treeList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
        assertEquals(expectedList.get(1).get("differentArr1"),treeList.get(0).get("differentArr"));
        nodes = (List<Map<String, Object>>) treeList.get(0).get("nodes");
        assertEquals(expectedList.get(1).get("differentArr2"),nodes.get(0).get("differentArr"));
        nodes = (List<Map<String, Object>>) nodes.get(0).get("nodes");
        assertEquals(expectedList.get(1).get("differentArr3"),nodes.get(0).get("differentArr"));
        nodes = (List<Map<String, Object>>) nodes.get(0).get("nodes");
        assertEquals(expectedList.get(1).get("differentArr4"),nodes.get(0).get("differentArr"));
        assertEquals(expectedList.get(1).get("differentArr5"),nodes.get(1).get("differentArr"));

        //美容顾问
        response = new MockHttpServletResponse();
        action = createAction(BINOLMOCIO07_Action.class, "/mo","BINOLMOCIO07_getTreeNodes");
        form = action.getModel();
        form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAddtype("3");
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        proxy.execute();
        treeList = (List<Map<String, Object>>)JSONUtil.deserialize(response.getContentAsString());
        assertEquals(expectedList.get(2).get("differentArr1"),treeList.get(0).get("differentArr"));
        assertEquals(expectedList.get(2).get("differentArr2"),treeList.get(1).get("differentArr"));
    }
}