package com.cherry.st.common.interfaces;

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
import com.cherry.st.common.bl.BINOLSTCM07_BL;

public class BINOLSTCM07_IF_TEST extends CherryJunitBase{

    private TESTCOM_Service testCOM_Service;
    
    private BINOLSTCM07_BL bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSendMQDeliverSend1() throws Exception{
        String caseName = "testSendMQDeliverSend1";
        bl = applicationContext.getBean(BINOLSTCM07_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //插入Basis.BIN_Product
        Map<String,Object> insertProductMap = insertList.get(0);
        insertProductMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertProductMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int productID = testCOM_Service.insertTableData(insertProductMap);
        
        //插入Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = insertList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //插入产品发货主表、从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductDeliverID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(detailData1);

        int[] deliverIDArr = {billID};
        Map<String,String> pramMap = new HashMap<String,String>();
        pramMap.put("BIN_OrganizationInfoID", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        pramMap.put("BIN_BrandInfoID", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        pramMap.put("CurrentUnit", "TestCase");
        pramMap.put("BIN_UserID", ConvertUtil.getString(userInfo.getBIN_UserID()));
        pramMap.put("BrandCode", ConvertUtil.getString(userInfo.getBrandCode()));
        pramMap.put("OrganizationCode", ConvertUtil.getString(userInfo.getOrganizationInfoCode()));
        bl.sendMQDeliverSend(deliverIDArr, pramMap);
        
        Map<String,Object> mq = new HashMap<String,Object>();
        mq.put("tableName", "Interfaces.BIN_MQLog");
        mq.put("BillCode", mainData.get("DeliverNoIF"));
        List<Map<String,Object>> mqList = testCOM_Service.getTableData(mq);
        
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        assertEquals(expectedList.get(0).get("MQLogData"),mqList.get(0).get("Data"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testSendMQDeliverSend2() throws Exception{
        String caseName = "testSendMQDeliverSend2";
        bl = applicationContext.getBean(BINOLSTCM07_BL.class);
        
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        List<Map<String,Object>> insertList = (List<Map<String, Object>>) dataMap.get("insertList");
        
        //插入Basis.BIN_Product
        Map<String,Object> insertProductMap = insertList.get(0);
        insertProductMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertProductMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int productID = testCOM_Service.insertTableData(insertProductMap);
        
        //插入Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = insertList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testCOM_Service.insertTableData(insertProductVendorMap);
        
        //插入产品发货主表、从表
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductDeliverID", billID);
        detailData1.put("BIN_ProductVendorID", productVendorID);
        testCOM_Service.insertTableData(detailData1);

        int[] deliverIDArr = {billID};
        Map<String,String> pramMap = new HashMap<String,String>();
        pramMap.put("BIN_OrganizationInfoID", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
        pramMap.put("BIN_BrandInfoID", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        pramMap.put("CurrentUnit", "TestCase");
        pramMap.put("BIN_UserID", ConvertUtil.getString(userInfo.getBIN_UserID()));
        pramMap.put("BrandCode", ConvertUtil.getString(userInfo.getBrandCode()));
        pramMap.put("OrganizationCode", ConvertUtil.getString(userInfo.getOrganizationInfoCode()));
        bl.sendMQDeliverSend(deliverIDArr, pramMap);
        
        Map<String,Object> mq = new HashMap<String,Object>();
        mq.put("tableName", "Interfaces.BIN_MQLog");
        mq.put("BillCode", mainData.get("DeliverNoIF"));
        List<Map<String,Object>> mqList = testCOM_Service.getTableData(mq);
        
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        assertEquals(expectedList.get(0).get("MQLogData"),mqList.get(0).get("Data"));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetDeliverMQMessage1() throws Exception{
        String caseName = "testGetDeliverMQMessage1";
        bl = applicationContext.getBean(BINOLSTCM07_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        String message = bl.getDeliverMQMessage(dataList, userInfo.getBrandCode(), userInfo.getOrganizationInfoCode());
        assertEquals(expectedList.get(0).get("MQLogData"), message);
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetDeliverMQMessage2() throws Exception{
        String caseName = "testGetDeliverMQMessage2";
        bl = applicationContext.getBean(BINOLSTCM07_BL.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = DataUtil.getDataMap(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        List<Map<String,Object>> expectedList = (List<Map<String,Object>>) dataMap.get("expectedList");
        String message = bl.getDeliverMQMessage(dataList, userInfo.getBrandCode(), userInfo.getOrganizationInfoCode());
        assertEquals(expectedList.get(0).get("MQLogData"), message);
    }
    
    @After
    public void tearDown() throws Exception {

    }

}
