package com.cherry.st.bil.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;

public class BINOLSTBIL12_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	private BINOLSTBIL12_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
	public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTBIL12_Action.class, "/st","BINOLSTBIL12_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = insertBaseDataList.get(0);
        int productID = testService.insertTableData(insertProductMap);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = insertBaseDataList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testService.insertTableData(insertProductVendorMap);
        
        //插入数据
        //Inventory.BIN_ProductReturn
        Map<String,Object> productReturnInsertMap1 = dataList.get(0);
        int billID = testService.insertTableData(productReturnInsertMap1);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> productReturnDetailInsertMap1 = dataList.get(1);
        productReturnDetailInsertMap1.put("BIN_ProductReturnID", billID);
        productReturnDetailInsertMap1.put("BIN_ProductVendorID", productVendorID);
        testService.insertTableData(productReturnDetailInsertMap1);
        
        //Inventory.BIN_ProductReturn
        Map<String,Object> productReturnInsertMap2 = dataList.get(2);
        int arBillID = testService.insertTableData(productReturnInsertMap2);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object>productReturnDetailInsertMap2 = dataList.get(3);
        productReturnDetailInsertMap2.put("BIN_ProductVendorID", productVendorID);
        productReturnDetailInsertMap2.put("BIN_ProductReturnID", arBillID);
        
        testService.insertTableData(productReturnDetailInsertMap2);
        
        BINOLSTBIL12_Form form = action.getModel();
        form.setProductReturnID(ConvertUtil.getString(billID));
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap1.get("BIN_InventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_DepotInfoID")));
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap1.get("BIN_LogicInventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_LogicInventoryInfoID")));
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap2.get("BIN_InventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_DepotInfoIDReceive")));
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap2.get("BIN_LogicInventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_LogicInventoryInfoIDReceive")));
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testInit2() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTBIL12_Action.class, "/st","BINOLSTBIL12_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass(),caseName);
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入基础数据
        List<Map<String,Object>> insertBaseDataList = (List<Map<String, Object>>) dataMap.get("insertBaseDataList");
        //Basis.BIN_Product
        Map<String,Object> insertProductMap = insertBaseDataList.get(0);
        int productID = testService.insertTableData(insertProductMap);

        //Basis.BIN_ProductVendor
        Map<String,Object> insertProductVendorMap = insertBaseDataList.get(1);
        insertProductVendorMap.put("BIN_ProductID", productID);
        int productVendorID = testService.insertTableData(insertProductVendorMap);
        
        //插入数据
        //Inventory.BIN_ProductReturn
        Map<String,Object> productReturnInsertMap1 = dataList.get(0);
        int billID = testService.insertTableData(productReturnInsertMap1);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> productReturnDetailInsertMap1 = dataList.get(1);
        productReturnDetailInsertMap1.put("BIN_ProductReturnID", billID);
        productReturnDetailInsertMap1.put("BIN_ProductVendorID", productVendorID);
        testService.insertTableData(productReturnDetailInsertMap1);
        
        //Inventory.BIN_ProductReturn
        Map<String,Object> productReturnInsertMap2 = dataList.get(2);
        int arBillID = testService.insertTableData(productReturnInsertMap2);
        
        //Inventory.BIN_ProductReturnDetail
        Map<String,Object> productReturnDetailInsertMap2 = dataList.get(3);
        productReturnDetailInsertMap2.put("BIN_ProductReturnID", arBillID);
        productReturnDetailInsertMap2.put("BIN_ProductVendorID", productVendorID);
        testService.insertTableData(productReturnDetailInsertMap2);
        
        //Inventory.BIN_ProductInOut
        Map<String,Object> productInOutInsertMap1 = dataList.get(4);
        int ioBillID1 = testService.insertTableData(productInOutInsertMap1);
        
        //Inventory.BIN_ProductInOutDetail
        Map<String,Object> productInOutDetailInsertMap1 = dataList.get(5);
        productInOutDetailInsertMap1.put("BIN_ProductInOutID", ioBillID1);
        testService.insertTableData(productInOutDetailInsertMap1);
        
        //Inventory.BIN_ProductInOut
        Map<String,Object> productInOutInsertMap2 = dataList.get(6);
        int ioBillID2 = testService.insertTableData(productInOutInsertMap2);
        
        //Inventory.BIN_ProductInOutDetail
        Map<String,Object> productInOutDetailInsertMap2 = dataList.get(7);
        productInOutDetailInsertMap2.put("BIN_ProductInOutID", ioBillID2);
        testService.insertTableData(productInOutDetailInsertMap2);
        
        
        BINOLSTBIL12_Form form = action.getModel();
        form.setProductReturnID(ConvertUtil.getString(arBillID));
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //proxy代理
        assertEquals("success", proxy.execute());
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap1.get("BIN_InventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_DepotInfoID")));
        assertEquals(ConvertUtil.getString(productReturnDetailInsertMap1.get("BIN_LogicInventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_LogicInventoryInfoID")));
        assertEquals(ConvertUtil.getString(productInOutDetailInsertMap2.get("BIN_InventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_DepotInfoIDReceive")));
        assertEquals(ConvertUtil.getString(productInOutDetailInsertMap2.get("BIN_LogicInventoryInfoID")), ConvertUtil.getString(form.getProductReturnMainData().get("BIN_LogicInventoryInfoIDReceive")));
    }
}
