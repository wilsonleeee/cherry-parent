package com.cherry.ss.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSSCM01_BL_TEST extends CherryJunitBase{
    private TESTCOM_Service testCOM_Service;
        
    private BINOLSSCM01_BL bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    @After
    public void tearDown() throws Exception {

    }
    //1
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByDeliverID1() throws Exception{
        String caseName = "testInsertStockInOutByDeliverID1";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionDeliverID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData2);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByDeliverID(promotionDeliverID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(actualMap);
        
        //取期望数据
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        Map<String,Object> expectedMap = expectedList.get(0);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(1);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(2);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(1).get(en.getKey())));
        }
    }
    
    //2
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByDeliverID2() throws Exception{
        String caseName = "testInsertStockInOutByDeliverID2";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionDeliverID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData2);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByDeliverID(promotionDeliverID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(actualMap);
        
        //取期望数据
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        Map<String,Object> expectedMap = expectedList.get(0);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(1);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(2);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(1).get(en.getKey())));
        }
    }
    //3
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByDeliverID3() throws Exception{
        String caseName = "testInsertStockInOutByDeliverID3";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionDeliverID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionDeliverID", promotionDeliverID);
        testCOM_Service.insertTableData(detailData2);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByDeliverID(promotionDeliverID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        assertEquals(0,promotionStockInOutID);
    }
    
    //4
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByAllocationIDOut1() throws Exception{
        String caseName = "testInsertStockInOutByAllocationIDOut1";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionAllocationID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByAllocationIDOut(promotionAllocationID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(actualMap);
        
        //取期望数据
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        Map<String,Object> expectedMap = expectedList.get(0);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(1);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(2);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(1).get(en.getKey())));
        }
    }
    
    //5
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByAllocationIDOut2() throws Exception{
        String caseName = "testInsertStockInOutByAllocationIDOut2";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionAllocationID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByAllocationIDOut(promotionAllocationID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        assertEquals(0,promotionStockInOutID);
    }
    
    //6
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByAllocationIDIn1() throws Exception{
        String caseName = "testInsertStockInOutByAllocationIDIn1";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionAllocationID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByAllocationIDIn(promotionAllocationID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockDetail");
        actualMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        List<Map<String,Object>> actualDetailList = testCOM_Service.getTableData(actualMap);
        
        //取期望数据
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(), caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        Map<String,Object> expectedMap = expectedList.get(0);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(1);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(0).get(en.getKey())));
        }
        expectedMap = expectedList.get(2);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(1).get(en.getKey())));
        }
    }
    
    //7
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertStockInOutByAllocationIDIn2() throws Exception{
        String caseName = "testInsertStockInOutByAllocationIDIn2";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionAllocationID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionAllocationID", promotionAllocationID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM01_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int promotionStockInOutID = bl.insertStockInOutByAllocationIDIn(promotionAllocationID, "BINOLSSCM01_BL_TEST", userInfo.getBIN_UserID());
        assertEquals(0,promotionStockInOutID);
    }
}
