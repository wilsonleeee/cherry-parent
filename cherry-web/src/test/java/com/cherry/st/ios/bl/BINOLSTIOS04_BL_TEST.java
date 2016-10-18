package com.cherry.st.ios.bl;

import java.util.ArrayList;
import java.util.Date;
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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.st.ios.interfaces.BINOLSTIOS04_IF;

public class BINOLSTIOS04_BL_TEST extends CherryJunitBase{
    private BINOLSTIOS04_IF bl;
    
    private TESTCOM_Service testCOM_Service;
    
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
    public void testTran_save1() throws Exception{
        String caseName = "testTran_save1";
        //插入产品批次号
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(1);
        testCOM_Service.insertTableData(mainData);
        mainData = dataList.get(2);
        testCOM_Service.insertTableData(mainData);
        mainData = dataList.get(3);
        testCOM_Service.insertTableData(mainData);
        
        bl = applicationContext.getBean(BINOLSTIOS04_IF.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CherryConstants.CREATEDBY, dataList.get(0).get(CherryConstants.CREATEDBY));
        map.put(CherryConstants.CREATEPGM, dataList.get(0).get(CherryConstants.CREATEPGM));
        map.put(CherryConstants.UPDATEDBY, dataList.get(0).get(CherryConstants.UPDATEDBY));
        map.put(CherryConstants.UPDATEPGM, dataList.get(0).get(CherryConstants.UPDATEPGM));
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        Date d = new Date();
        String Comments = testCOM_Service.getDbTime();
        map.put("Comments", Comments);
        map.put("blindFlag", dataList.get(0).get("blindFlag"));
        map.put("IsBatch", dataList.get(0).get("IsBatch"));
        map.put("organizationId", dataList.get(0).get("organizationId"));
        map.put("depotInfoId", dataList.get(0).get("depotInfoId"));
        List<String[]> list = new ArrayList<String[]>();
        String[] array = convertListToArray((List<String>)dataList.get(0).get("productVendorIdArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("batchNoArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("quantityArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("reasonArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("priceArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("gainCountArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("bookCountArr"));
        list.add(array);
        bl.tran_save(map, list, userInfo);
        
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_ProductStockTaking");  
        actualMap.put("Comments", Comments);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        int productStockTakingID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductStockTakingID"));
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_ProductTakingDetail");
        actualMap.put("BIN_ProductTakingID", productStockTakingID);
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
        
        expectedMap = expectedList.get(3);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(2).get(en.getKey())));
        }
    }
    
    //2
    @Test
    @Rollback(true)
    @Transactional
    public void testTran_save2() throws Exception{
        String caseName = "testTran_save2";
//        //插入产品批次号
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
//        Map<String,Object> mainData = dataList.get(1);
//        testCOM_Service.insertTableData(mainData);
//        mainData = dataList.get(2);
//        testCOM_Service.insertTableData(mainData);
//        mainData = dataList.get(3);
//        testCOM_Service.insertTableData(mainData);
        
        bl = applicationContext.getBean(BINOLSTIOS04_IF.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CherryConstants.CREATEDBY, dataList.get(0).get(CherryConstants.CREATEDBY));
        map.put(CherryConstants.CREATEPGM, dataList.get(0).get(CherryConstants.CREATEPGM));
        map.put(CherryConstants.UPDATEDBY, dataList.get(0).get(CherryConstants.UPDATEDBY));
        map.put(CherryConstants.UPDATEPGM, dataList.get(0).get(CherryConstants.UPDATEPGM));
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        Date d = new Date();
        String Comments = testCOM_Service.getDbTime();
        map.put("Comments", Comments);
        map.put("blindFlag", dataList.get(0).get("blindFlag"));
        map.put("IsBatch", dataList.get(0).get("IsBatch"));
        map.put("organizationId", dataList.get(0).get("organizationId"));
        map.put("depotInfoId", dataList.get(0).get("depotInfoId"));
        List<String[]> list = new ArrayList<String[]>();
        String[] array = convertListToArray((List<String>)dataList.get(0).get("productVendorIdArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("batchNoArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("quantityArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("reasonArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("priceArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("gainCountArr"));
        list.add(array);
        array = convertListToArray((List<String>)dataList.get(0).get("bookCountArr"));
        list.add(array);
        bl.tran_save(map, list, userInfo);
        
        //取数据库中数据
        Map<String,Object> actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_ProductStockTaking");
        actualMap.put("Comments", Comments);
        List<Map<String,Object>> actualList = testCOM_Service.getTableData(actualMap);
        int productStockTakingID = CherryUtil.obj2int(actualList.get(0).get("BIN_ProductStockTakingID"));
        
        actualMap = new HashMap<String,Object>();
        actualMap.put("tableName", "Inventory.BIN_ProductTakingDetail");
        actualMap.put("BIN_ProductTakingID", productStockTakingID);
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
        
        expectedMap = expectedList.get(3);
        for(Map.Entry<String, Object> en : expectedMap.entrySet()){
            assertEquals(en.getKey(),ConvertUtil.getString(en.getValue()),ConvertUtil.getString(actualDetailList.get(2).get(en.getKey())));
        }
    }
    
    public String[] convertListToArray(List<String> list){
        String[] array = new String[list.size()];
        for(int i=0;i<list.size();i++){
            array[i] = ConvertUtil.getString(list.get(i));
        }
        return array;
    }
}
