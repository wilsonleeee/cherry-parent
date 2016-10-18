package com.cherry.st.common.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSTCM01_IF_TEST extends CherryJunitBase{

    private TESTCOM_Service testCOM_Service;
    
    private BINOLSTCM01_IF bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    //1
    @Test
    @Rollback(true)
    @Transactional
    public void testChangeStock1() throws Exception{
        String caseName = "testChangeStock1";
        bl = applicationContext.getBean(BINOLSTCM01_IF.class);
        //插入产品入出库主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> detailData3 = dataList.get(3);
        Map<String,Object> detailData4 = dataList.get(4);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData1);
        detailData2.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData2);
        detailData3.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData3);
        detailData4.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData4);
        
        //第三条、第四条产品插入库存
        Map<String,Object> stockData3 = dataList.get(5);
        testCOM_Service.insertTableData(stockData3);
        Map<String,Object> stockData4 = dataList.get(6);
        testCOM_Service.insertTableData(stockData4);
                
        //第三条、第四条产品插入批次库存
        Map<String,Object> batchstockData3 = dataList.get(7);
        testCOM_Service.insertTableData(batchstockData3);
        Map<String,Object> batchstockData4 = dataList.get(8);
        testCOM_Service.insertTableData(batchstockData4);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        //验证数据
        Map<String,Object> stockData = dataList.get(9);
        List<Map<String,Object>> actualData = testCOM_Service.getTableData(stockData);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        List<Map<String,Object>> expectedList = (List) dataMap.get("expectedList1");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productVendorID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductVendorID"));
                String productVendorID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductVendorID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productVendorID1.equals(productVendorID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
        
        Map<String,Object> batchstockData = dataList.get(10);
        actualData = testCOM_Service.getTableData(batchstockData);
        expectedList = (List) dataMap.get("expectedList2");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productBatchID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductBatchID"));
                String productBatchID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductBatchID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productBatchID1.equals(productBatchID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
        
    }
    //2
    @Test
    @Rollback(true)
    @Transactional 
    public void testChangeStock2() throws Exception{
        String caseName = "testChangeStock2";
        bl = applicationContext.getBean(BINOLSTCM01_IF.class);
        //插入产品入出库主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> detailData3 = dataList.get(3);
        Map<String,Object> detailData4 = dataList.get(4);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData1);
        detailData2.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData2);
        detailData3.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData3);
        detailData4.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData4);
        
        //第三条、第四条产品插入库存
        Map<String,Object> stockData3 = dataList.get(5);
        testCOM_Service.insertTableData(stockData3);
        Map<String,Object> stockData4 = dataList.get(6);
        testCOM_Service.insertTableData(stockData4);
                
        //第三条、第四条产品插入批次库存
        Map<String,Object> batchstockData3 = dataList.get(7);
        testCOM_Service.insertTableData(batchstockData3);
        Map<String,Object> batchstockData4 = dataList.get(8);
        testCOM_Service.insertTableData(batchstockData4);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        //验证数据
        Map<String,Object> stockData5 = dataList.get(9);
        List<Map<String,Object>> actualData = testCOM_Service.getTableData(stockData5);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        List<Map<String,Object>> expectedList = (List) dataMap.get("expectedList1");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productVendorID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductVendorID"));
                String productVendorID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductVendorID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productVendorID1.equals(productVendorID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
        
        Map<String,Object> batchstockData = dataList.get(10);
        actualData = testCOM_Service.getTableData(batchstockData);
        expectedList = (List) dataMap.get("expectedList2");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productBatchID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductBatchID"));
                String productBatchID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductBatchID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productBatchID1.equals(productBatchID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
    }
    
    //3
    @Test
    @Rollback(true)
    @Transactional 
    public void testChangeStock3() throws Exception{
        String caseName = "testChangeStock3";
        bl = applicationContext.getBean(BINOLSTCM01_IF.class);
        //插入产品入出库主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        Map<String,Object> detailData2 = dataList.get(2);
        Map<String,Object> detailData3 = dataList.get(3);
        Map<String,Object> detailData4 = dataList.get(4);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData1);
        detailData2.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData2);
        detailData3.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData3);
        detailData4.put("BIN_ProductInOutID", billID);
        testCOM_Service.insertTableData(detailData4);
        
        //第三条、第四条产品插入库存
        Map<String,Object> stockData3 = dataList.get(5);
        testCOM_Service.insertTableData(stockData3);
        Map<String,Object> stockData4 = dataList.get(6);
        testCOM_Service.insertTableData(stockData4);
                
        //第三条、第四条产品插入批次库存
        Map<String,Object> batchstockData3 = dataList.get(7);
        testCOM_Service.insertTableData(batchstockData3);
        Map<String,Object> batchstockData4 = dataList.get(8);
        testCOM_Service.insertTableData(batchstockData4);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        bl.changeStock(praMap);
        
        //验证数据
        Map<String,Object> stockData5 = dataList.get(9);
        List<Map<String,Object>> actualData = testCOM_Service.getTableData(stockData5);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        List<Map<String,Object>> expectedList = (List) dataMap.get("expectedList1");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productVendorID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductVendorID"));
                String productVendorID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductVendorID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productVendorID1.equals(productVendorID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
        
        Map<String,Object> batchstockData = dataList.get(10);
        actualData = testCOM_Service.getTableData(batchstockData);
        expectedList = (List) dataMap.get("expectedList2");
        assertEquals(expectedList.size(),actualData.size());
        for(int i=0;i<actualData.size();i++){
            for(int j=0;j<expectedList.size();j++){
                String productBatchID1 = ConvertUtil.getString(actualData.get(i).get("BIN_ProductBatchID"));
                String productBatchID2 = ConvertUtil.getString(expectedList.get(j).get("BIN_ProductBatchID"));
                String actualQuantity = ConvertUtil.getString(actualData.get(i).get("Quantity"));
                String expectedQuantity = ConvertUtil.getString(expectedList.get(j).get("Quantity"));
                if(productBatchID1.equals(productBatchID2)){
                    assertEquals(expectedQuantity,actualQuantity);
                }
            }
        }
    }
    
    //4
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertProductInOutAll1() throws Exception{
        String caseName = "testInsertProductInOutAll1";
        bl = applicationContext.getBean(BINOLSTCM01_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(dataList.get(1));
        detailList.add(dataList.get(2));
        detailList.add(dataList.get(3));
        detailList.add(dataList.get(4));
        detailList.add(dataList.get(5));
        int id = bl.insertProductInOutAll(mainData, detailList);
        assertEquals(0,id);
    }
    
    //5
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertProductInOutAll2() throws Exception{
        String caseName = "testInsertProductInOutAll2";
        bl = applicationContext.getBean(BINOLSTCM01_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        detailList.add(dataList.get(1));
        detailList.add(dataList.get(2));
        detailList.add(dataList.get(3));
        detailList.add(dataList.get(4));
        detailList.add(dataList.get(5));
        int id = bl.insertProductInOutAll(mainData, detailList);
        assertTrue(id>0);
        List<Map<String,Object>> actualDetailList = bl.getProductInOutDetailData(id, "");
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        List<Map<String,Object>> expectedList = (List<Map<String, Object>>) dataMap.get("expectedList");
        assertEquals(CherryUtil.obj2int(expectedList.get(0).get("size")),actualDetailList.size());
        assertEquals(1,CherryUtil.obj2int(actualDetailList.get(0).get("DetailNo")));
        assertEquals(2,CherryUtil.obj2int(actualDetailList.get(1).get("DetailNo")));
        assertEquals(CherryUtil.obj2int(expectedList.get(1).get("BIN_ProductVendorID")),CherryUtil.obj2int(actualDetailList.get(0).get("BIN_ProductVendorID")));
        assertEquals(CherryUtil.obj2int(expectedList.get(2).get("BIN_ProductVendorID")),CherryUtil.obj2int(actualDetailList.get(1).get("BIN_ProductVendorID")));
    }
    
    @After
    public void tearDown() throws Exception {

    }

}
