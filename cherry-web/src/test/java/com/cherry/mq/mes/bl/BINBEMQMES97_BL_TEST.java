package com.cherry.mq.mes.bl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINBEMQMES97_BL_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    /**
     * 手工删除不能回滚的MongoDB警告日志
     * @param errInfo
     */
    private void removeMongoDBMQWarn(String tradeNoIF){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("TradeNoIF", tradeNoIF);
        try {
            MongoDB.removeAll(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
            MongoDB.findAll(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
        } catch (Exception ex) {

        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetOrganizationInfo1() throws Exception {
        String caseName = "testGetOrganizationInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TestCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertMap = dataList.get(0);
        insertMap.put("BIN_OrganizationID", organizationID);
        insertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertMap);
                
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", "BH201201010101001");
        map.put("tradeType", "HM");
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("CounterCode", dataList.get(0).get("CounterCode"));
        Map<String,Object> organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
        assertEquals(organizationID,organizationInfo.get("BIN_OrganizationID"));
        
        map.put("CounterCode", "errorCounterCode");
        organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, false);
        assertEquals(null,organizationInfo);
        
        map.put("CounterCode", "errorCounterCode");
        try{
            organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            String errMsg3 = (String) expectList.get(0).get("errMsg3");
            assertEquals(errMsg1+errMsg2+errMsg3,e.getMessage());
            removeMongoDBMQWarn(ConvertUtil.getString(map.get("tradeNoIF")));
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetEmployeeInfo1() throws Exception {
        String caseName = "testGetEmployeeInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        List<Map<String,Object>> otherList = (List)dataMap.get("otherList");
        
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Basis.BIN_Employee
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Employee");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("EmployeeCode", otherList.get(0).get("EmployeeCode"));
        List<Map<String,Object>> employeeList = testCOM_Service.getTableData(paramMap);
        int employeeID = CherryUtil.obj2int(employeeList.get(0).get("BIN_EmployeeID"));
                
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", "BH201201010101001");
        map.put("tradeType", "HM");
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("EmployeeCode", otherList.get(0).get("EmployeeCode"));
        Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, true);
        assertEquals(employeeID,employeeInfo.get("BIN_EmployeeID"));
        
        map.put("EmployeeCode", "errorEmployeeCode");
        employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, false);
        assertEquals(null,employeeInfo);
        
        map.put("EmployeeCode", "errorEmployeeCode");
        try{
            employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            String errMsg3 = (String) expectList.get(0).get("errMsg3");
            assertEquals(errMsg1+errMsg2+errMsg3,e.getMessage());
            removeMongoDBMQWarn(ConvertUtil.getString(map.get("tradeNoIF")));
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetInventoryInfoID1() throws Exception {
        String caseName = "testGetInventoryInfoID1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Basis.BIN_Organization
        String sql = ConvertUtil.getString(sqlList.get(0).get("sql"));
        sql = sql.replaceAll("#BIN_OrganizationInfoID#", ConvertUtil.getString(organizationInfoID));
        sql = sql.replaceAll("#BIN_BrandInfoID#", ConvertUtil.getString(brandInfoID));
        testCOM_Service.insert(sql);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Basis.BIN_Organization");
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("DepartCode", "TestCounter001");
        List<Map<String,Object>> organizationList = testCOM_Service.getTableData(paramMap);
        int organizationID = CherryUtil.obj2int(organizationList.get(0).get("BIN_OrganizationID"));
        
        //Basis.BIN_CounterInfo
        Map<String,Object> insertMap = dataList.get(0);
        insertMap.put("BIN_OrganizationID", organizationID);
        insertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMap.put("BIN_BrandInfoID", brandInfoID);
        int counterInfoID = testCOM_Service.insertTableData(insertMap);
        
        //Basis.BIN_DepotInfo
        Map<String,Object> depotInfoInsertMap = dataList.get(1);
        depotInfoInsertMap.put("BIN_OrganizationID", organizationID);
        depotInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        int inventoryInfoID = testCOM_Service.insertTableData(depotInfoInsertMap);
        
        //Basis.BIN_InventoryInfo
        Map<String,Object> inventoryInfoInsertMap = dataList.get(2);
        inventoryInfoInsertMap.put("BIN_InventoryInfoID", inventoryInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryInfoInsertMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryInfoInsertMap.put("BIN_OrganizationID", organizationID);
        testCOM_Service.insertTableData(inventoryInfoInsertMap);
                
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", "BH201201010101001");
        map.put("tradeType", "HM");
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("BIN_OrganizationID", organizationID);
        map.put("CounterCode", dataList.get(0).get("CounterCode"));
        int id = binBEMQMES97_BL.getInventoryInfoID(map, true);
        assertEquals(inventoryInfoID,id);
        
        map.put("BIN_OrganizationID", -1);
        id = binBEMQMES97_BL.getInventoryInfoID(map, false);
        assertEquals(0,id);
        
        map.put("CounterCode", "errorCounterCode");
        try{
            map.put("BIN_OrganizationID", -1);
            binBEMQMES97_BL.getInventoryInfoID(map, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            String errMsg3 = (String) expectList.get(0).get("errMsg3");
            assertEquals(errMsg1+errMsg2+errMsg3,e.getMessage());
            removeMongoDBMQWarn(ConvertUtil.getString(map.get("tradeNoIF")));
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetLogicInventoryInfoID1() throws Exception {
        String caseName = "testGetLogicInventoryInfoID1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        List<Map<String,Object>> sqlList = (List)dataMap.get("sqlList");
        
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Basis.BIN_LogicInventory
        Map<String,Object> logicInventoryInsertMap = dataList.get(0);
        logicInventoryInsertMap.put("BIN_OrganizationInfoID", organizationInfoID);
        logicInventoryInsertMap.put("BIN_BrandInfoID", brandInfoID);
        int logicInventoryInfoID = testCOM_Service.insertTableData(logicInventoryInsertMap);
                
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", "BH201201010101001");
        map.put("tradeType", "HM");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("LogicInventoryCode", dataList.get(0).get("LogicInventoryCode"));
        int id = binBEMQMES97_BL.getLogicInventoryInfoID(map,param, true);
        assertEquals(logicInventoryInfoID,id);
        
        param.put("LogicInventoryCode", "ErrorLogicInventoryCode");
        id = binBEMQMES97_BL.getLogicInventoryInfoID(map, param, false);
        assertEquals(0,id);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductVendorID1() throws Exception {
        String caseName = "testGetProductVendorID1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap = dataList.get(2);
        prtBarCodeInsertMap.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(prtBarCodeInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-8-28");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-8-28 13:01:30");
        map.put("tradeType", "GR");
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("BarCode", prtBarCodeInsertMap.get("OldBarCode"));
        param.put("UnitCode", prtBarCodeInsertMap.get("OldUnitCode"));
        param.put("TradeDateTime", "2012-8-28 13:01:30");
        int actualProductVendorID = binBEMQMES97_BL.getProductVendorID(map, param, true);
        
        assertEquals(productVendorID,actualProductVendorID);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductVendorID2() throws Exception {
        String caseName = "testGetProductVendorID2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap1 = dataList.get(2);
        prtBarCodeInsertMap1.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(prtBarCodeInsertMap1);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap2 = dataList.get(3);
        testCOM_Service.insertTableData(prtBarCodeInsertMap2);
        
        //Basis.BIN_PrtBarCode
        Map<String,Object> prtBarCodeInsertMap3 = dataList.get(4);
        testCOM_Service.insertTableData(prtBarCodeInsertMap3);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("BarCode", prtBarCodeInsertMap1.get("OldBarCode"));
        param.put("UnitCode", prtBarCodeInsertMap1.get("OldUnitCode"));
        param.put("TradeDateTime", "2012-9-3 13:01:30");
        int actualProductVendorID = binBEMQMES97_BL.getProductVendorID(map, param, true);
        
        assertEquals(productVendorID,actualProductVendorID);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductVendorID3() throws Exception {
        String caseName = "testGetProductVendorID3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_Product
        Map<String,Object> productInsertMap = dataList.get(0);
        int productID = testCOM_Service.insertTableData(productInsertMap);
        
        //Basis.BIN_ProductVendor
        Map<String,Object> productVendorInsertMap = dataList.get(1);
        productVendorInsertMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(productVendorInsertMap);
       
        //更新数据
        testCOM_Service.update("Update Basis.BIN_Product Set ValidFlag = '0' WHERE BIN_ProductID="+productID);
        testCOM_Service.update("Update Basis.BIN_ProductVendor Set ValidFlag = '0' WHERE BIN_ProductVendorID="+productVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            param.put("BarCode", productVendorInsertMap.get("BarCode"));
            param.put("UnitCode", productInsertMap.get("UnitCode"));
            param.put("TradeDateTime", "2012-9-3 13:01:30");
            int actualProductVendorID = binBEMQMES97_BL.getProductVendorID(map, param, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
            String expectException = ConvertUtil.getString(expectList.get(0).get("expectException"));
            expectException = expectException.replaceAll("UnitCode", ConvertUtil.getString(productInsertMap.get("UnitCode")));
            expectException = expectException.replaceAll("BarCode", ConvertUtil.getString(productVendorInsertMap.get("BarCode")));
            expectException = expectException.replaceAll("TradeType", ConvertUtil.getString(map.get("tradeType")));
            assertEquals(expectException,e.getMessage());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetPrmInfo1() throws Exception {
        String caseName = "testGetPrmInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> promotionProductVendorVendorInsertMap = dataList.get(1);
        promotionProductVendorVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorVendorInsertMap);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object> promotionPrtBarCodeInsertMap = dataList.get(2);
        promotionPrtBarCodeInsertMap.put("BIN_PromotionProductVendorID", promotionProductVendorID);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-8-28");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-8-28 13:01:30");
        map.put("tradeType", "GR");
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("BarCode", promotionPrtBarCodeInsertMap.get("OldBarCode"));
        param.put("UnitCode", promotionPrtBarCodeInsertMap.get("OldUnitCode"));
        param.put("TradeDateTime", "2012-8-28 13:01:30");
        Map<String,Object> actualPrmInfo = binBEMQMES97_BL.getPrmInfo(map, param, true);
        
        assertEquals(promotionProductVendorID,actualPrmInfo.get("BIN_PromotionProductVendorID"));
        assertEquals(promotionProductInsertMap.get("ExPoint"),ConvertUtil.getString(actualPrmInfo.get("ExPoint")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetPrmInfo2() throws Exception {
        String caseName = "testGetPrmInfo2";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> promotionProductVendorInsertMap = dataList.get(1);
        promotionProductVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorInsertMap);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object> promotionPrtBarCodeInsertMap1 = dataList.get(2);
        promotionPrtBarCodeInsertMap1.put("BIN_PromotionProductVendorID", promotionProductVendorID);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap1);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object> promotionPrtBarCodeInsertMap2 = dataList.get(3);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap2);
        
        //Basis.BIN_PromotionPrtBarCode
        Map<String,Object> promotionPrtBarCodeInsertMap3 = dataList.get(4);
        testCOM_Service.insertTableData(promotionPrtBarCodeInsertMap3);
        
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("BarCode", promotionPrtBarCodeInsertMap1.get("OldBarCode"));
        param.put("UnitCode", promotionPrtBarCodeInsertMap1.get("OldUnitCode"));
        param.put("TradeDateTime", "2012-9-3 13:01:30");
        Map<String,Object> actualPrmInfo = binBEMQMES97_BL.getPrmInfo(map, param, true);
        
        assertEquals(promotionProductVendorID,actualPrmInfo.get("BIN_PromotionProductVendorID"));
        assertEquals(promotionProductInsertMap.get("ExPoint"),ConvertUtil.getString(actualPrmInfo.get("ExPoint")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetPrmInfo3() throws Exception {
        String caseName = "testGetPrmInfo3";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Basis.BIN_PromotionProduct
        Map<String,Object> promotionProductInsertMap = dataList.get(0);
        int promotionProductID = testCOM_Service.insertTableData(promotionProductInsertMap);
        
        //Basis.BIN_PromotionProductVendor
        Map<String,Object> promotionProductVendorInsertMap = dataList.get(1);
        promotionProductVendorInsertMap.put("BIN_PromotionProductID", promotionProductID);
        int promotionProductVendorID = testCOM_Service.insertTableData(promotionProductVendorInsertMap);
       
        //更新数据
        testCOM_Service.update("Update Basis.BIN_PromotionProduct Set ValidFlag = '0' WHERE BIN_PromotionProductID="+promotionProductID);
        testCOM_Service.update("Update Basis.BIN_PromotionProductVendor Set ValidFlag = '0' WHERE BIN_PromotionProductVendorID="+promotionProductVendorID);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationInfoID", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoID", userInfo.getBIN_BrandInfoID());
        map.put("tradeDate", "2012-9-3");
        map.put("tradeTime", "13:01:30");
        map.put("organizationID", userInfo.getBIN_OrganizationID());
        map.put("tradeDateTime", "2012-9-3 13:01:30");
        map.put("tradeType", "GR");
        
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            param.put("BarCode", promotionProductVendorInsertMap.get("BarCode"));
            param.put("UnitCode", promotionProductInsertMap.get("UnitCode"));
            param.put("TradeDateTime", "2012-9-3 13:01:30");
            Map<String,Object> actualPrmInfo = binBEMQMES97_BL.getPrmInfo(map, param, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String, Object>>) dataMap.get("expectList");
            String expectException = ConvertUtil.getString(expectList.get(0).get("expectException"));
            expectException = expectException.replaceAll("UnitCode", ConvertUtil.getString(promotionProductInsertMap.get("UnitCode")));
            expectException = expectException.replaceAll("BarCode", ConvertUtil.getString(promotionProductVendorInsertMap.get("BarCode")));
            expectException = expectException.replaceAll("TradeType", ConvertUtil.getString(map.get("tradeType")));
            assertEquals(expectException,e.getMessage());
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetMemberInfo1() throws Exception {
        String caseName = "testGetMemberInfo1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
                
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Members.BIN_MemberInfo
        Map<String,Object> insertMemberInfoMap = dataList.get(0);
        insertMemberInfoMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertMemberInfoMap.put("BIN_BrandInfoID", brandInfoID);
        int memberInfoID = testCOM_Service.insertTableData(insertMemberInfoMap);
        
        //Members.BIN_MemCardInfo
        Map<String,Object> insertMemCardInfoMap = dataList.get(1);
        insertMemCardInfoMap.put("BIN_MemberInfoID", memberInfoID);
        testCOM_Service.insertTableData(insertMemCardInfoMap);
                
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        String tradeNoIF = ConvertUtil.getString(otherInfo.get("TradeNoIF"));
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", tradeNoIF);
        map.put("tradeType", "MV");
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("MemCode", insertMemCardInfoMap.get("MemCode"));
        Map<String,Object> memberInfo = binBEMQMES97_BL.getMemberInfo(map, true);
        assertEquals(memberInfoID,memberInfo.get("BIN_MemberInfoID"));
        
        map.put("MemCode", "errorMemCode");
        memberInfo = binBEMQMES97_BL.getMemberInfo(map, false);
        assertEquals(null,memberInfo);
        
        try{
            memberInfo = binBEMQMES97_BL.getMemberInfo(map, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            String errMsg3 = (String) expectList.get(0).get("errMsg3");
            errMsg3 = errMsg3.replaceAll("#TradeNoIF#", tradeNoIF);
            assertEquals(errMsg1+errMsg2+errMsg3,e.getMessage());
            removeMongoDBMQWarn(tradeNoIF);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetQuestionID1() throws Exception {
        String caseName = "testGetQuestionID1";
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
                
        //插入数据        
        int organizationInfoID = userInfo.getBIN_OrganizationInfoID();
        int brandInfoID = userInfo.getBIN_BrandInfoID();
        //Monitor.BIN_Paper
        Map<String,Object> insertPaperMap = dataList.get(0);
        insertPaperMap.put("BIN_OrganizationInfoID", organizationInfoID);
        insertPaperMap.put("BIN_BrandInfoID", brandInfoID);
        int paperID = testCOM_Service.insertTableData(insertPaperMap);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap1 = dataList.get(1);
        insertPaperQuestionMap1.put("BIN_PaperID", paperID);
        int paperQuestionID1 = testCOM_Service.insertTableData(insertPaperQuestionMap1);
        
        //Monitor.BIN_PaperQuestion
        Map<String,Object> insertPaperQuestionMap2 = dataList.get(2);
        insertPaperQuestionMap2.put("BIN_PaperID", paperID);
        int paperQuestionID2 = testCOM_Service.insertTableData(insertPaperQuestionMap2);
                
        Map<String,Object> otherInfo = (Map<String, Object>) dataMap.get("otherInfo");
        String tradeNoIF = ConvertUtil.getString(otherInfo.get("TradeNoIF"));
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tradeNoIF", tradeNoIF);
        map.put("tradeType", "MV");

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        param.put("BIN_PaperID", paperID);
        param.put("DisplayOrder", insertPaperQuestionMap1.get("DisplayOrder"));
        int paperQuestionID = binBEMQMES97_BL.getQuestionID(map, param, true);
        assertEquals(paperQuestionID1,paperQuestionID);
        
        param.put("DisplayOrder", insertPaperQuestionMap2.get("DisplayOrder"));
        paperQuestionID = binBEMQMES97_BL.getQuestionID(map, param, true);
        assertEquals(paperQuestionID2,paperQuestionID);
        
        param.put("DisplayOrder", "99");
        paperQuestionID = binBEMQMES97_BL.getQuestionID(map, param, false);
        assertEquals(0,paperQuestionID);
        
        try{
            binBEMQMES97_BL.getQuestionID(map,param, true);
        }catch(Exception e){
            List<Map<String,Object>> expectList = (List<Map<String,Object>>)dataMap.get("expectList");
            String errMsg1 = (String) expectList.get(0).get("errMsg1");
            String errMsg2 = (String) expectList.get(0).get("errMsg2");
            String errMsg3 = (String) expectList.get(0).get("errMsg3");
            errMsg1 = errMsg1.replaceAll("#BIN_PaperID#", ConvertUtil.getString(paperID));
            errMsg1 = errMsg1.replaceAll("#DisplayOrder#", ConvertUtil.getString(param.get("DisplayOrder")));
            errMsg3 = errMsg3.replaceAll("#TradeNoIF#", tradeNoIF);
            assertEquals(errMsg1+errMsg2+errMsg3,e.getMessage());
            removeMongoDBMQWarn(tradeNoIF);
        }
    }
}