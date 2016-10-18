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
import com.cherry.st.bil.form.BINOLSTBIL16_Form;

public class BINOLSTCM14_IF_TEST extends CherryJunitBase{
    @Resource(name="TESTCOM_Service")
    private TESTCOM_Service testCOM_Service;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF bl;
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetProStocktakeRequestMainData1() throws Exception {
        String caseName = "testGetProStocktakeRequestMainData1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        Map<String,Object> actualMap = bl.getProStocktakeRequestMainData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequest");
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
    public void testGetProStocktakeRequestDetailData1() throws Exception {
        String caseName = "testGetProStocktakeRequestDetailData1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> insertData = new HashMap<String,Object>();
        insertData.putAll(mainData);
        int billID = testCOM_Service.insertTableData(insertData);
        detailData1.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData1);
        testCOM_Service.insertTableData(insertData);
        detailData2.put("BIN_ProStocktakeRequestID", billID);
        insertData = new HashMap<String,Object>();
        insertData.putAll(detailData2);
        testCOM_Service.insertTableData(insertData);
        
        List<Map<String,Object>> actualList = bl.getProStocktakeRequestDetailData(billID, null);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequestDetail");

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
    public void testInsertProStocktakeRequestAll1() throws Exception {
        String caseName = "testInsertProStocktakeRequestAll1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequest");
        
        Map<String,Object> actualMainMap = bl.getProStocktakeRequestMainData(billID, null);
        List<Map<String,Object>> actualDetailList = bl.getProStocktakeRequestDetailData(billID, null);
        
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequestDetail");
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
    public void testUpdateProStocktakeRequest1() throws Exception {
        String caseName = "testUpdateProStocktakeRequest1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        
        List<Map<String,Object>> updateList = (List<Map<String, Object>>) dataMap.get("updateList");
        Map<String,Object> updateMap = updateList.get(0);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProStocktakeRequestID", billID);
        for(Map.Entry<String, Object> en : updateMap.entrySet()){
            praMap.put(en.getKey(), en.getValue());
        }
        int cnt = bl.updateProStocktakeRequest(praMap);
        assertEquals(1,cnt);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequest");
        Map<String,Object> actualMainMap = bl.getProStocktakeRequestMainData(billID, null);
        
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
    public void testCreateProStocktakeRequestByForm1() throws Exception {
        String caseName = "testCreateProStocktakeRequestByForm1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        
        BINOLSTBIL16_Form form = new BINOLSTBIL16_Form();
        Map<String,Object> otherFormData = (Map<String, Object>) dataMap.get("otherFormData");
        DataUtil.injectObject(form, otherFormData);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProStocktakeRequestID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        praMap.put("ProStocktakeRequestForm", form);
        int newBillID = bl.createProStocktakeRequestByForm(praMap);
        
        assertTrue(newBillID>0);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequest");
        Map<String,Object> actualMainMap = bl.getProStocktakeRequestMainData(newBillID, null);
        List<Map<String,Object>> actualDetailList = bl.getProStocktakeRequestDetailData(newBillID, null);
        
        assertEquals("TradeType","CJ",actualMainMap.get("TradeType"));
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequestDetail");
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
    public void testCreateProStocktakeRequest1() throws Exception {
        String caseName = "testCreateProStocktakeRequest1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProStocktakeRequestID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        int newBillID = bl.createProStocktakeRequest(praMap);
        
        Map<String,Object> validFields = (Map<String,Object>)dataMap.get("ValidFields");
        List<Map<String,Object>> fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequest");
        Map<String,Object> actualMainMap = bl.getProStocktakeRequestMainData(newBillID, null);
        List<Map<String,Object>> actualDetailList = bl.getProStocktakeRequestDetailData(newBillID, null);
        
        assertEquals("TradeType","CJ",actualMainMap.get("TradeType"));
        for(int i=0;i<fieldsList.size();i++){
            String key = ConvertUtil.getString(fieldsList.get(i));
            String expectedValue = ConvertUtil.getString(mainData.get(key));
            String actualValue = ConvertUtil.getString(actualMainMap.get(key));
            assertEquals(key,expectedValue,actualValue);
        }
        
        fieldsList = (List<Map<String,Object>>) validFields.get("BIN_ProStocktakeRequestDetail");
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
    public void testChangeStockByCA1() throws Exception {
        String caseName = "testChangeStockByCA1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        
        //插入盘点主从表
        Map<String,Object> productStockTakingMainData = dataList.get(3);
        int productStockTakingID = testCOM_Service.insertTableData(productStockTakingMainData);
        Map<String,Object> proStocktakeRequestDetailData1 = dataList.get(4);
        Map<String,Object> proStocktakeRequestDetailData2 = dataList.get(5);
        proStocktakeRequestDetailData1.put("BIN_ProductTakingID", productStockTakingID);
        testCOM_Service.insertTableData(proStocktakeRequestDetailData1);
        proStocktakeRequestDetailData2.put("BIN_ProductTakingID", productStockTakingID);
        testCOM_Service.insertTableData(proStocktakeRequestDetailData2);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProStocktakeRequestID", billID);
        praMap.put("BIN_ProductStockTakingID", productStockTakingID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        praMap.put("isCounter", "YES");
        bl.changeStockByCA(praMap);
        
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
    public void testSelProStocktakeRequest1() throws Exception {
        String caseName = "testSelProStocktakeRequest1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
     
        String relevantNo = ConvertUtil.getString(mainData.get("StockTakingNoIF"));
        List<Map<String,Object>> actualList = bl.selProStocktakeRequest(relevantNo);
        assertEquals(1,actualList.size());
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSendMQ1() throws Exception {
        String caseName = "testSendMQ1";
        bl = applicationContext.getBean(BINOLSTCM14_IF.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        
        //插入主从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(detailData1);
        detailList.add(detailData2);
        int billID = bl.insertProStocktakeRequestAll(mainData, detailList);
        
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
        paramMap.put("BillCode", mainData.get("StockTakingNoIF"));
        paramMap.put("SendOrRece", "S");
        paramMap.put("BillType", "CJ");
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(paramMap);
        assertEquals(1,actualList.size());
        assertEquals(pramMap.get("OrganizationCode"),actualList.get(0).get("CounterCode"));
        assertTrue(ConvertUtil.getString(actualList.get(0).get("Data")).length()>0);
        String sendData = ConvertUtil.getString(actualList.get(0).get("Data"));
        
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        for(int i=0;i<expectedList.size();i++){
            String expected = (String) expectedList.get(i).get("expected");
            assertTrue(sendData.indexOf(expected)>0);
        }
    }
}