package com.cherry.mo.cio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLMOCIO07_Service_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	@Resource(name="binOLMOCIO07_Service")
	private BINOLMOCIO07_Service binOLMOCIO07_Service;
		
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
    public void testSearchDownList1() throws Exception {
        String caseName = "testSearchDownList1";
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入基础数据
        Map<String,Object> baseData = insertBaseData(userInfo);

        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);

        //插入Monitor.BIN_SaleTarget
        Map<String,Object> insertSaleTargetMap1 = new HashMap<String,Object>();
        insertSaleTargetMap1.putAll(dataList.get(0));
        insertSaleTargetMap1.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap1.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap1.put("Parameter", baseData.get("organizationID2"));
        int saleTargetID1 = testService.insertTableData(insertSaleTargetMap1);
        
        Map<String,Object> insertSaleTargetMap2 = new HashMap<String,Object>();
        insertSaleTargetMap2.putAll(dataList.get(0));
        insertSaleTargetMap2.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap2.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap2.put("Parameter", baseData.get("organizationID3"));
        int saleTargetID2 = testService.insertTableData(insertSaleTargetMap2);
        
        Map<String,Object> insertSaleTargetMap3 = new HashMap<String,Object>();
        insertSaleTargetMap3.putAll(dataList.get(1));
        insertSaleTargetMap3.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap3.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap3.put("Parameter", baseData.get("organizationID6"));
        int saleTargetID3 = testService.insertTableData(insertSaleTargetMap3);
        
        Map<String,Object> insertSaleTargetMap4 = new HashMap<String,Object>();
        insertSaleTargetMap4.putAll(dataList.get(1));
        insertSaleTargetMap4.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap4.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap4.put("Parameter", baseData.get("organizationID8"));
        int saleTargetID4 = testService.insertTableData(insertSaleTargetMap4);
        
        Map<String,Object> insertSaleTargetMap5 = new HashMap<String,Object>();
        insertSaleTargetMap5.putAll(dataList.get(2));
        insertSaleTargetMap5.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap5.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap5.put("Parameter", baseData.get("baInfoID1"));
        int saleTargetID5 = testService.insertTableData(insertSaleTargetMap5);
        
        Map<String,Object> insertSaleTargetMap6 = new HashMap<String,Object>();
        insertSaleTargetMap6.putAll(dataList.get(2));
        insertSaleTargetMap6.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertSaleTargetMap6.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertSaleTargetMap6.put("Parameter", baseData.get("baInfoID2"));
        int saleTargetID6 = testService.insertTableData(insertSaleTargetMap6);
        
        //区域
        Map<String,Object> searchParam = new HashMap<String,Object>();
        searchParam.put("targetDate", insertSaleTargetMap1.get("TargetDate"));
        searchParam.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        searchParam.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        searchParam.put("userID", baseData.get("userID"));
        searchParam.put("operationType","1");
        searchParam.put("businessType","3");
        searchParam.put("type", "1");
        List<Map<String,Object>> actualList = new ArrayList<Map<String,Object>>();
        actualList = binOLMOCIO07_Service.searchDownList(searchParam);
        assertEquals(1,actualList.size());
        assertEquals(saleTargetID1,actualList.get(0).get("BIN_SaleTargetID"));

        //柜台
        searchParam.put("type", "2");
        actualList = binOLMOCIO07_Service.searchDownList(searchParam);
        assertEquals(1,actualList.size());
        assertEquals(saleTargetID3,actualList.get(0).get("BIN_SaleTargetID"));
        
        
        //美容顾问
        searchParam.put("type", "3");
        actualList = binOLMOCIO07_Service.searchDownList(searchParam);
        assertEquals(1,actualList.size());
        assertEquals(saleTargetID5,actualList.get(0).get("BIN_SaleTargetID"));
    }
}