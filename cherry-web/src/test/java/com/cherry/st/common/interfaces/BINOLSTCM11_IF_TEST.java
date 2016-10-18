package com.cherry.st.common.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSTCM11_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM11_BL")
    private BINOLSTCM11_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductReceiveMainData1() throws Exception {
        String caseName = "testGetProductReceiveMainData1";
        bl = applicationContext.getBean(BINOLSTCM11_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProductReceiveID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProductReceiveID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        Map<String,Object> actualMap = bl.getProductReceiveMainData(billID, null);
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductReceive");
        List<Map<String,Object>> aliasList = (List<Map<String, Object>>) dataMap.get("aliasList");
        Map<String,Object> alias = aliasList.get(0);
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualKey = key;
            String aliasKey = ConvertUtil.getString(alias.get(actualKey));
            if(!"".equals(aliasKey)){
                actualKey = aliasKey;
            }
            String actualValue = ConvertUtil.getString(actualMap.get(actualKey));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductReceiveDetailData1() throws Exception {
        String caseName = "testGetProductReceiveDetailData1";
        bl = applicationContext.getBean(BINOLSTCM11_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProductReceiveID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProductReceiveID", billID);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        List<Map<String,Object>> actualList = bl.getProductReceiveDetailData(billID, null);
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductReceiveDetail");

        for(int j=0;j<fieldsList.size();j++){
            String key = ConvertUtil.getString(fieldsList.get(j));
            String expectedValue = ConvertUtil.getString(detailData1.get(key));
            String actualValue = ConvertUtil.getString(actualList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
            
            expectedValue = ConvertUtil.getString(detailData2.get(key));
            actualValue = ConvertUtil.getString(actualList.get(1).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInsertProductReceiveAll1() throws Exception {
        String caseName = "testInsertProductReceiveAll1";
        bl = applicationContext.getBean(BINOLSTCM11_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());

        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap1 = insertBaseDataList.get(0);
        int productID1 = testCOM_Service.insertTableData(insertProductMap1);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap1 = insertBaseDataList.get(1);
        insertProductVendorMap1.put("BIN_ProductID", productID1);
        int productVendorID1 = testCOM_Service.insertTableData(insertProductVendorMap1);
        
        //Basis.BIN_Product
        Map<String,Object> insertProductMap2 = insertBaseDataList.get(2);
        int productID2 = testCOM_Service.insertTableData(insertProductMap2);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap2 = insertBaseDataList.get(3);
        insertProductVendorMap2.put("BIN_ProductID", productID2);
        int productVendorID2 = testCOM_Service.insertTableData(insertProductVendorMap2);
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailData1.put("BIN_ProductVendorID", productVendorID1);
        detailList.add(detailData1);
        detailData2.put("BIN_ProductVendorID", productVendorID2);
        detailList.add(detailData2);
        int billID = bl.insertProductReceiveAll(mainData, detailList);
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductReceive");
        List<Map<String,Object>> aliasList = (List<Map<String, Object>>) dataMap.get("aliasList");
        Map<String,Object> alias = aliasList.get(0);

        Map<String,Object> actualMainMap = bl.getProductReceiveMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getProductReceiveDetailData(billID, null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualKey = key;
            String aliasKey = ConvertUtil.getString(alias.get(actualKey));
            if(!"".equals(aliasKey)){
                actualKey = aliasKey;
            }
            String actualValue = ConvertUtil.getString(actualMainMap.get(actualKey));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductReceiveDetail");
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(detailData1.get(key));
            String actualValue = ConvertUtil.getString(actualDetailList.get(0).get(key));
            assertEquals(key,expectedValue,actualValue);
            
            expectedValue = ConvertUtil.getString(detailData2.get(key));
            actualValue = ConvertUtil.getString(actualDetailList.get(1).get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testUpdateProductReceiveMain1() throws Exception {
        String caseName = "testUpdateProductReceiveMain1";
        bl = applicationContext.getBean(BINOLSTCM11_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProductReceiveAll(mainData, detailList);
        
        List<Map<String,Object>> updateList = (List<Map<String, Object>>) dataMap.get("updateList");
        Map<String,Object> updateMap = updateList.get(0);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductReceiveID", billID);
        for(Map.Entry<String, Object> en : updateMap.entrySet()){
            praMap.put(en.getKey(), en.getValue());
        }
        int cnt = bl.updateProductReceiveMain(praMap);
        assertEquals(1,cnt);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductReceive");
        Map<String,Object> paramSearch = new HashMap<String,Object>();
        paramSearch.put("tableName", "Inventory.BIN_ProductReceive");
        paramSearch.put("BIN_ProductReceiveID", billID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramSearch);
        Map<String,Object> actualMainMap = actualList.get(0);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(updateMap.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
}