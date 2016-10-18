package com.cherry.ss.common.interfaces;

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

public class BINOLSSCM07_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSSCM07_BL")
    private BINOLSSCM07_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInsertPromotionStockInOutAll1() throws Exception {
        String caseName = "testInsertPromotionStockInOutAll1";
        bl = applicationContext.getBean(BINOLSSCM07_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertPromotionStockInOutAll(mainData, detailList);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionStockInOut");
        
        Map<String,Object> actualMainMap = bl.getPromotionStockInOutMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getPromotionStockDetailData(billID, null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionStockDetail");
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
    public void testUpdatePromotionStockInOutMain1() throws Exception {
        String caseName = "testUpdatePromotionStockInOutMain1";
        bl = applicationContext.getBean(BINOLSSCM07_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertPromotionStockInOutAll(mainData, detailList);
        
        List<Map<String,Object>> updateList = (List<Map<String, Object>>) dataMap.get("updateList");
        Map<String,Object> updateMap = updateList.get(0);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_PromotionStockInOutID", billID);
        for(Map.Entry<String, Object> en : updateMap.entrySet()){
            praMap.put(en.getKey(), en.getValue());
        }
        int cnt = bl.updatePromotionStockInOutMain(praMap);
        assertEquals(1,cnt);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionStockInOut");
        Map<String,Object> actualMainMap = bl.getPromotionStockInOutMainData(billID, null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(updateMap.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStock1() throws Exception {
        String caseName = "testChangeStock1";
        bl = applicationContext.getBean(BINOLSSCM07_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertPromotionStockInOutAll(mainData, detailList);
                
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_PromotionStockInOutID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_PromotionProductVendorID", detailData1.get("BIN_PromotionProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData1.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData1.get("BIN_LogicInventoryInfoID"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expected");
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(0).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionStock");
        paramMap.put("BIN_PromotionProductVendorID", detailData2.get("BIN_PromotionProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData2.get("BIN_InventoryInfoID"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData2.get("BIN_LogicInventoryInfoID"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(1).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetPromotionStockInOutMainData1() throws Exception {
        String caseName = "testGetPromotionStockInOutMainData1";
        bl = applicationContext.getBean(BINOLSSCM07_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_PromotionStockInOutID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_PromotionStockInOutID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        Map<String,Object> actualMap = bl.getPromotionStockInOutMainData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionStockInOut");
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
    public void testGetPromotionStockDetailData1() throws Exception {
        String caseName = "testGetPromotionStockDetailData1";
        bl = applicationContext.getBean(BINOLSSCM07_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_PromotionStockInOutID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_PromotionStockInOutID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        List<Map<String,Object>> actualList = bl.getPromotionStockDetailData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_PromotionStockDetail");

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
}