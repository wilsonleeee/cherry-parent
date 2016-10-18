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

public class BINOLSTCM17_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM17_BL")
    private BINOLSTCM17_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductOutDepotMainData1() throws Exception {
        String caseName = "testGetProductOutDepotMainData1";
        bl = applicationContext.getBean(BINOLSTCM17_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProductOutDepotID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProductOutDepotID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        Map<String,Object> actualMap = bl.getProductOutDepotMainData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductOutDepot");
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProductOutDepotDetailData1() throws Exception {
        String caseName = "testGetProductOutDepotDetailData1";
        bl = applicationContext.getBean(BINOLSTCM17_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProductOutDepotID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProductOutDepotID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        List<Map<String,Object>> actualList = bl.getProductOutDepotDetailData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductOutDepotDetail");

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
    public void testInsertProductOutDepotAll1() throws Exception {
        String caseName = "testInsertProductOutDepotAll1";
        bl = applicationContext.getBean(BINOLSTCM17_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProductOutDepotAll(mainData, detailList);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductOutDepot");
        
        Map<String,Object> actualMainMap = bl.getProductOutDepotMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getProductOutDepotDetailData(billID, null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProductOutDepotDetail");
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
    public void testChangeStock1() throws Exception {
        String caseName = "testChangeStock1";
        bl = applicationContext.getBean(BINOLSTCM17_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProductOutDepotAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductOutDepotID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData1.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData1.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData1.get("BIN_LogicInventoryInfoID"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expected");
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(0).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData2.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData2.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData2.get("BIN_LogicInventoryInfoID"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(1).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
}