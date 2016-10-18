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
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;

public class BINOLSSCM03_BL_TEST extends CherryJunitBase{
    private TESTCOM_Service testCOM_Service;
    
    private BINOLSSCM03_BL bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    @After
    public void tearDown() throws Exception {

    }
    
    @Test
    @Rollback(true)
    @Transactional 
    public void testInsertAllocationMain1() throws Exception{
        String caseName = "testInsertAllocationMain1";
        bl = applicationContext.getBean(BINOLSSCM03_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        int billid = bl.insertAllocationMain(mainData);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
        paramMap.put("BIN_PromotionAllocationID", billid);
        List<Map<String,Object>> queryData = testCOM_Service.getTableData(paramMap);
        assertEquals(1,queryData.size());
        assertEquals(ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID")),ConvertUtil.getString(queryData.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(ConvertUtil.getString(mainData.get("BIN_BrandInfoID")),ConvertUtil.getString(queryData.get(0).get("BIN_BrandInfoID")));
        assertEquals(ConvertUtil.getString(mainData.get("AllocationFlag")),ConvertUtil.getString(queryData.get(0).get("AllocationFlag")));
        assertEquals(ConvertUtil.getString(mainData.get("TradeStatus")),ConvertUtil.getString(queryData.get(0).get("TradeStatus")));
        assertEquals(ConvertUtil.getString(mainData.get("WorkFlowID")),ConvertUtil.getString(queryData.get(0).get("WorkFlowID")));
        
        mainData = dataList.get(1);
        billid = bl.insertAllocationMain(mainData);
        paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_PromotionAllocation");
        paramMap.put("BIN_PromotionAllocationID", billid);
        queryData = testCOM_Service.getTableData(paramMap);
        assertEquals(1,queryData.size());
        assertEquals(ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID")),ConvertUtil.getString(queryData.get(0).get("BIN_OrganizationInfoID")));
        assertEquals(ConvertUtil.getString(mainData.get("BIN_BrandInfoID")),ConvertUtil.getString(queryData.get(0).get("BIN_BrandInfoID")));
        assertEquals(ConvertUtil.getString(mainData.get("AllocationFlag")),ConvertUtil.getString(queryData.get(0).get("AllocationFlag")));
        assertEquals(ConvertUtil.getString(mainData.get("TradeStatus")),ConvertUtil.getString(queryData.get(0).get("TradeStatus")));
        assertEquals(ConvertUtil.getString(mainData.get("WorkFlowID")),ConvertUtil.getString(queryData.get(0).get("WorkFlowID")));
    }
    
    @Test
    @Rollback(true)
    @Transactional 
    public void testGetPromotionAllocationDetail1() throws Exception{
        String caseName = "testGetPromotionAllocationDetail1";
        bl = applicationContext.getBean(BINOLSSCM03_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);

        //Inventory.BIN_PromotionAllocation
        Map<String,Object> insertPromotionAllocationMap = dataList.get(0);
        int billID = testCOM_Service.insertTableData(insertPromotionAllocationMap);
        
        //Inventory.BIN_PromotionAllocationDetail
        Map<String,Object> insertPromotionAllocationDetailMap = dataList.get(1);
        insertPromotionAllocationDetailMap.put("BIN_PromotionAllocationID", billID);
        testCOM_Service.insertTableData(insertPromotionAllocationDetailMap);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_PromotionAllocationID", billID);
        param.put("language", "zh_CN");
        List<Map<String,Object>> actualList = bl.getPromotionAllocationDetail(param);
        assertEquals(insertPromotionAllocationDetailMap.get("BIN_LogicInventoryInfoID"),ConvertUtil.getString(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("LogicInventoryCodeName")));
        
        param = new HashMap<String,Object>();
        param.put("BIN_PromotionAllocationID", billID);
        param.put("language", "en_US");
        actualList = bl.getPromotionAllocationDetail(param);
        assertEquals(insertPromotionAllocationDetailMap.get("BIN_LogicInventoryInfoID"),ConvertUtil.getString(actualList.get(0).get("BIN_LogicInventoryInfoID")));
        assertEquals("",ConvertUtil.getString(actualList.get(0).get("LogicInventoryCodeName")));
    }
}