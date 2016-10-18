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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSSCM02_BL_TEST extends CherryJunitBase{
    private TESTCOM_Service testCOM_Service;
        
    private BINOLSSCM02_BL bl;
    
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
    public void testInsertStockInOutAndDetail1() throws Exception{
        String caseName = "testInsertStockInOutAndDetail1";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionStockTakingID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM02_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int[] arr = new int[1];
        arr[0] = promotionStockTakingID;
        bl.insertStockInOutAndDetail(arr, userInfo);
        
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("RelevantNo", mainData.get("StockTakingNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        int promotionStockInOutID = CherryUtil.obj2int(actualList.get(0).get("BIN_PromotionStockInOutID"));
        
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
    public void testInsertStockInOutAndDetail2() throws Exception{
        String caseName = "testInsertStockInOutAndDetail2";
        //插入主从数据
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int promotionStockTakingID = testCOM_Service.insertTableData(mainData);
        Map<String,Object> detailData1 = dataList.get(1);
        detailData1.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> detailData2 = dataList.get(2);
        detailData2.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData2);
        Map<String,Object> detailData3 = dataList.get(3);
        detailData3.put("BIN_PromotionStockTakingID", promotionStockTakingID);
        testCOM_Service.insertTableData(detailData3);
        
        bl = applicationContext.getBean(BINOLSSCM02_BL.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        int[] arr = new int[1];
        arr[0] = promotionStockTakingID;
        bl.insertStockInOutAndDetail(arr, userInfo);
        
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_PromotionStockInOut");
        actualMap.put("RelevantNo", mainData.get("StockTakingNoIF"));
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        assertEquals(0,actualList.size());
    }
}
