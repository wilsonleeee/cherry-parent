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
import com.cherry.st.bil.form.BINOLSTBIL14_Form;

public class BINOLSTCM13_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProReturnRequestMainData1() throws Exception {
        String caseName = "testGetProReturnRequestMainData1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProReturnRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProReturnRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        Map<String,Object> actualMap = bl.getProReturnRequestMainData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
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
    public void testGetProReturnReqDetailData1() throws Exception {
        String caseName = "testGetProReturnReqDetailData1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProReturnRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProReturnRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        List<Map<String,Object>> actualList = bl.getProReturnReqDetailData(billID, null,null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnReqDetail");

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
    public void testInsertProReturnRequestAll1() throws Exception {
        String caseName = "testInsertProReturnRequestAll1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
        
        Map<String,Object> actualMainMap = bl.getProReturnRequestMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getProReturnReqDetailData(billID, null,null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnReqDetail");
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
    public void testUpdateProReturnRequest1() throws Exception {
        String caseName = "testUpdateProReturnRequest1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        List<Map<String,Object>> updateList = (List<Map<String, Object>>) dataMap.get("updateList");
        Map<String,Object> updateMap = updateList.get(0);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        for(Map.Entry<String, Object> en : updateMap.entrySet()){
            praMap.put(en.getKey(), en.getValue());
        }
        int cnt = bl.updateProReturnRequest(praMap);
        assertEquals(1,cnt);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("tableName", "Inventory.BIN_ProReturnRequest");
        param.put("BIN_ProReturnRequestID", billID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(param);
        Map<String,Object> actualMainMap = actualList.get(0);
        
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
    public void testCreateProReturnRequestByForm1() throws Exception {
        String caseName = "testCreateProReturnRequestByForm1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        BINOLSTBIL14_Form form = new BINOLSTBIL14_Form();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        praMap.put("ProReturnRequestForm", form);
        int newBillID = bl.createProReturnRequestByForm(praMap);
        
        assertTrue(newBillID>0);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
        Map<String,Object> actualMainMap = bl.getProReturnRequestMainData(newBillID, null);
        List<Map<String,Object>> actualDetailList = bl.getProReturnReqDetailData(newBillID, null,null);
        
        assertEquals("TradeType","RJ",actualMainMap.get("TradeType"));
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnReqDetail");
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
    public void testCreateProReturnRequest1() throws Exception {
        String caseName = "testCreateProReturnRequest1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        int newBillID = bl.createProReturnRequest(praMap);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
        Map<String,Object> actualMainMap = bl.getProReturnRequestMainData(newBillID, null);
        List<Map<String,Object>> actualDetailList = bl.getProReturnReqDetailData(newBillID, null,null);
        
        assertEquals("TradeType","RJ",actualMainMap.get("TradeType"));
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnReqDetail");
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
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData1.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData1.get("BIN_InventoryInfoIDReceive"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData1.get("BIN_LogicInventoryInfoIDReceive"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expected");
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(0).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData2.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData2.get("BIN_InventoryInfoIDReceive"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData2.get("BIN_LogicInventoryInfoIDReceive"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(1).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStockByRR1() throws Exception {
        String caseName = "testChangeStockByRR1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        
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
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        //插入退库主从表
        Map<String,Object> productReturnMainData = dataList.get(3);
        int productReturnID = testCOM_Service.insertTableData(productReturnMainData);
        Map<String,Object> productReturnDetailData1 = dataList.get(4);
        Map<String,Object> productReturnDetailData2 = dataList.get(5);
        productReturnDetailData1.put("BIN_ProductReturnID", productReturnID);
        productReturnDetailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(productReturnDetailData1);
        productReturnDetailData2.put("BIN_ProductReturnID", productReturnID);
        productReturnDetailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(productReturnDetailData2);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        praMap.put("BIN_ProductReturnID", productReturnID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        praMap.put("isCounter", "YES");
        bl.changeStockByRR(praMap);
        
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
    
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStockByRR2() throws Exception {
        String caseName = "testChangeStockByRR2";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        
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
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        //插入退库主从表
        Map<String,Object> productReturnMainData = dataList.get(3);
        int productReturnID = testCOM_Service.insertTableData(productReturnMainData);
        Map<String,Object> productReturnDetailData1 = dataList.get(4);
        Map<String,Object> productReturnDetailData2 = dataList.get(5);
        productReturnDetailData1.put("BIN_ProductReturnID", productReturnID);
        productReturnDetailData1.put("BIN_ProductVendorID", productVendorID1);
        testCOM_Service.insertTableData(productReturnDetailData1);
        productReturnDetailData2.put("BIN_ProductReturnID", productReturnID);
        productReturnDetailData2.put("BIN_ProductVendorID", productVendorID2);
        testCOM_Service.insertTableData(productReturnDetailData2);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProReturnRequestID", billID);
        praMap.put("BIN_ProductReturnID", productReturnID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStockByRR(praMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData1.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData1.get("BIN_InventoryInfoIDReceive"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData1.get("BIN_LogicInventoryInfoIDReceive"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expected");
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(0).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
        
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductStock");
        paramMap.put("BIN_ProductVendorID", detailData2.get("BIN_ProductVendorID"));
        paramMap.put("BIN_InventoryInfoID", detailData2.get("BIN_InventoryInfoIDReceive"));
        paramMap.put("BIN_LogicInventoryInfoID", detailData2.get("BIN_LogicInventoryInfoIDReceive"));
        actualList = testCOM_Service.getTableData(paramMap);
        assertEquals("Quantity",ConvertUtil.getString(expectedList.get(1).get("Quantity")),ConvertUtil.getString(actualList.get(0).get("Quantity")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSelProReturnRequest1() throws Exception {
        String caseName = "testSelProReturnRequest1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
     
        String relevantNo = ConvertUtil.getString(mainData.get("BillNoIF"));
        List<Map<String,Object>> actualList = bl.selProReturnRequest(relevantNo);
        assertEquals(1,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSendMQ1() throws Exception {
        String caseName = "testSendMQ1";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProReturnRequestAll(mainData, detailList);
        
        Map<String,String> pramMap = new HashMap<String,String>();
        pramMap.put("BrandCode", "mgp");
        pramMap.put("OrganizationCode", "testCounter");
        pramMap.put("BIN_UserID", "5");
        pramMap.put("CurrentUnit", "TestCase");
        int[] idArr = new int[1];
        idArr[0] = billID;
        bl.sendMQ(idArr, pramMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Interfaces.BIN_MQLog");
        paramMap.put("BillCode", mainData.get("BillNoIF"));
        paramMap.put("SendOrRece", "S");
        paramMap.put("BillType", "RJ");
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(1,actualList.size());
        assertEquals(pramMap.get("OrganizationCode"),actualList.get(0).get("CounterCode"));
        assertTrue(ConvertUtil.getString(actualList.get(0).get("Data")).length()>0);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testCreateProReturnRequest_K31() throws Exception {
        String caseName = "testCreateProReturnRequest_K31";
        bl = applicationContext.getBean(BINOLSTCM13_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("returnReqMainData", mainData);
        praMap.put("returnReqDetailList", detailList);
        int billID = bl.createProReturnRequest_K3(praMap);
        assertTrue(billID>0);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnRequest");
        
        Map<String,Object> actualMainMap = bl.getProReturnRequestMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getProReturnReqDetailData(billID, null,null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProReturnReqDetail");
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
}
