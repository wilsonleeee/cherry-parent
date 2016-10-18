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

public class BINOLSTCM03_IF_TEST extends CherryJunitBase{

    private TESTCOM_Service testCOM_Service;
    
    private BINOLSTCM03_IF bl;
    
    @Before
    public void setUp() throws Exception {
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testCreateProductDeliverByOrder1() throws Exception{
        String caseName = "testCreateProductDeliverByOrder1";
        bl = applicationContext.getBean(BINOLSTCM03_IF.class);
        //插入产品订单主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductOrderID", billID);
        testCOM_Service.insertTableData(detailData1);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductOrderID", billID);
        praMap.put("CreatedBy", "Test");
        praMap.put("CreatePGM", "TestCase");
        int deliverID = bl.createProductDeliverByOrder(praMap);
        assertTrue(deliverID>0);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("tableName", "Inventory.BIN_ProductDeliver");
        paramMap.put("BIN_ProductDeliverID", deliverID);
        List<Map<String,Object>> productDeliver = testCOM_Service.getTableData(paramMap);
        assertEquals(ConvertUtil.getString(mainData.get("OrderType")),ConvertUtil.getString(productDeliver.get(0).get("DeliverType")));
    }

    @Test
    @Rollback(true)
    @Transactional
    public void testSelPrtDeliverList1() throws Exception{
        String caseName = "testSelPrtDeliverList1";
        bl = applicationContext.getBean(BINOLSTCM03_IF.class);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        //插入产品发货单主表、从表
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> mainData = dataList.get(0);
        Map<String,Object> detailData1 = dataList.get(1);
        int billID = testCOM_Service.insertTableData(mainData);
        detailData1.put("BIN_ProductDeliverID", billID);
        testCOM_Service.insertTableData(detailData1);

        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("DeliverNoIF", mainData.get("DeliverNoIF"));
        praMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        praMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        List<Map<String,Object>> actualList = bl.selPrtDeliverList(praMap);
        
        assertEquals(1,actualList.size());
        
        praMap.put("DeliverNoIF", "NOTFOUND");
        praMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        praMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        actualList = bl.selPrtDeliverList(praMap);
        assertEquals(0,actualList.size());
    }
    
    @After
    public void tearDown() throws Exception {

    }

}
