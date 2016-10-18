package com.cherry.st.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.cherry.st.common.form.BINOLSTCM15_Form;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;

public class BINOLSTCM15_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
	
	private BINOLSTCM15_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTCM15_Action.class, "/st","BINOLSTCM15_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //插入数据
        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        List<Map<String,Object>> insertPrtList = (List<Map<String,Object>>) dataMap.get("insertPrtList");
        for(int i=0;i<insertPrtList.size();i++){
            //Basis.BIN_Product
            Map<String,Object> productInsertMap = new HashMap<String,Object>();
            productInsertMap.put("tableName", "Basis.BIN_Product");
            productInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            productInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            productInsertMap.put("UnitCode", insertPrtList.get(i).get("UnitCode"));
            productInsertMap.put("NameTotal", insertPrtList.get(i).get("NameTotal"));
            int productID = testService.insertTableData(productInsertMap);
            
            //Basis.BIN_ProductVendor
            Map<String,Object> productVendorInsertMap = new HashMap<String,Object>();
            productVendorInsertMap.put("tableName", "Basis.BIN_ProductVendor");
            productVendorInsertMap.put("BIN_ProductID", productID);
            productVendorInsertMap.put("BarCode", insertPrtList.get(i).get("BarCode"));
            int productVendorID = testService.insertTableData(productVendorInsertMap);
            
            //Inventory.BIN_ProductOrderDetail
            Map<String,Object> productOrderDetailInsertMap = new HashMap<String,Object>();
            productOrderDetailInsertMap.put("tableName", "Inventory.BIN_ProductOrderDetail");
            productOrderDetailInsertMap.put("BIN_ProductOrderID",productOrderID);
            productOrderDetailInsertMap.put("BIN_ProductVendorID",productVendorID);
            productOrderDetailInsertMap.put("DetailNo", i+1);
            productOrderDetailInsertMap.put("Quantity", "100");
            productOrderDetailInsertMap.put("BIN_InventoryInfoID", productOrderInsertMap.get("BIN_InventoryInfoID"));
            productOrderDetailInsertMap.put("BIN_LogicInventoryInfoID", productOrderInsertMap.get("BIN_LogicInventoryInfoID"));
            productOrderDetailInsertMap.put("BIN_InventoryInfoIDAccept", productOrderInsertMap.get("BIN_InventoryInfoIDAccept"));
            productOrderDetailInsertMap.put("BIN_LogicInventoryInfoIDAccept", productOrderInsertMap.get("BIN_LogicInventoryInfoIDAccept"));
            int productOrderDetailID = testService.insertTableData(productOrderDetailInsertMap);
        }
        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLSTCM15_Form form = action.getModel();
        form.setEntryID(ConvertUtil.getString(workFlowID));
        DataUtil.getForm(this.getClass(), caseName, form);
        form.setEntryID(ConvertUtil.getString(workFlowID));
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        String sql = "select CONVERT(varchar(10), DateAdd(DAY,-7,GETDATE()), 120) AS startDate";
        List<Map<String,Object>> list = testService.select(sql);
        assertEquals(list.get(0).get("startDate"),action.getModel().getStartDate());
        assertEquals(testService.getDate(),action.getModel().getEndDate());
        
        Map<String,Object> inventoryMap = action.getModel().getInventoryMap();
        assertEquals(ConvertUtil.getInt(productOrderInsertMap.get("BIN_InventoryInfoID")),ConvertUtil.getInt(inventoryMap.get("BIN_InventoryInfoID")));
        assertEquals(ConvertUtil.getInt(productOrderInsertMap.get("BIN_LogicInventoryInfoID")),ConvertUtil.getInt(inventoryMap.get("BIN_LogicInventoryInfoID")));
    }
	
    @Test
    @Rollback(true)
    @Transactional
	public void testGetStock1() throws Exception {
        String caseName = "testGetStock1";
        action = createAction(BINOLSTCM15_Action.class, "/st","BINOLSTCM15_getStock");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        
        //插入数据
        //Inventory.BIN_ProductOrder
        Map<String,Object> productOrderInsertMap = dataList.get(0);
        int productOrderID = testService.insertTableData(productOrderInsertMap);
        
        List<Map<String,Object>> insertPrtList = (List<Map<String,Object>>) dataMap.get("insertPrtList");
        String[] productVendorIDArr = new String[insertPrtList.size()];
        for(int i=0;i<insertPrtList.size();i++){
            //Basis.BIN_Product
            Map<String,Object> productInsertMap = new HashMap<String,Object>();
            productInsertMap.put("tableName", "Basis.BIN_Product");
            productInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            productInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            productInsertMap.put("UnitCode", insertPrtList.get(i).get("UnitCode"));
            productInsertMap.put("NameTotal", insertPrtList.get(i).get("NameTotal"));
            int productID = testService.insertTableData(productInsertMap);
            
            //Basis.BIN_ProductVendor
            Map<String,Object> productVendorInsertMap = new HashMap<String,Object>();
            productVendorInsertMap.put("tableName", "Basis.BIN_ProductVendor");
            productVendorInsertMap.put("BIN_ProductID", productID);
            productVendorInsertMap.put("BarCode", insertPrtList.get(i).get("BarCode"));
            int productVendorID = testService.insertTableData(productVendorInsertMap);
            productVendorIDArr[i] = ConvertUtil.getString(productVendorID);
            
//            //Inventory.BIN_ProductStock
//            Map<String,Object> productStockInsertMap = new HashMap<String,Object>();
//            productStockInsertMap.put("tableName", "Inventory.BIN_ProductStock");
//            productStockInsertMap.put("BIN_ProductVendorID", productVendorID);
//            productStockInsertMap.put("BIN_InventoryInfoID", productOrderInsertMap.get("BIN_InventoryInfoID"));
//            productStockInsertMap.put("BIN_LogicInventoryInfoID", productOrderInsertMap.get("BIN_LogicInventoryInfoID"));
//            productStockInsertMap.put("Quantity", insertPrtList.get(i).get("StockQuantity"));
//            if(null != insertPrtList.get(i).get("StockQuantity") && !"".equals(insertPrtList.get(i).get("StockQuantity"))){
//                int productStockID = testService.insertTableData(productStockInsertMap);
//            }
            
            //Inventory.BIN_ProductOrderDetail
            Map<String,Object> productOrderDetailInsertMap = new HashMap<String,Object>();
            productOrderDetailInsertMap.put("tableName", "Inventory.BIN_ProductOrderDetail");
            productOrderDetailInsertMap.put("BIN_ProductOrderID",productOrderID);
            productOrderDetailInsertMap.put("BIN_ProductVendorID",productVendorID);
            productOrderDetailInsertMap.put("DetailNo", i+1);
            productOrderDetailInsertMap.put("Quantity", "100");
            productOrderDetailInsertMap.put("BIN_InventoryInfoID", productOrderInsertMap.get("BIN_InventoryInfoID"));
            productOrderDetailInsertMap.put("BIN_LogicInventoryInfoID", productOrderInsertMap.get("BIN_LogicInventoryInfoID"));
            productOrderDetailInsertMap.put("BIN_InventoryInfoIDAccept", productOrderInsertMap.get("BIN_InventoryInfoIDAccept"));
            productOrderDetailInsertMap.put("BIN_LogicInventoryInfoIDAccept", productOrderInsertMap.get("BIN_LogicInventoryInfoIDAccept"));
            int productOrderDetailID = testService.insertTableData(productOrderDetailInsertMap);
        
            
            if(null != insertPrtList.get(i).get("StockQuantity") && !"".equals(insertPrtList.get(i).get("StockQuantity"))){
                //Inventory.BIN_ProductInOut
                Map<String,Object> productInOutInsertMap = new HashMap<String,Object>();
                productInOutInsertMap.put("tableName", "Inventory.BIN_ProductInOut");
                productInOutInsertMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
                productInOutInsertMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
                String tradeNo = UUID.randomUUID().toString();
                productInOutInsertMap.put("TradeNo", tradeNo);
                productInOutInsertMap.put("TradeNoIF", tradeNo);
                productInOutInsertMap.put("StockType", 0);
                productInOutInsertMap.put("TradeType", ConvertUtil.getString(insertPrtList.get(i).get("TradeType")));
                productInOutInsertMap.put("StockInOutDate", "2012-9-3");
                int productInOutID = testService.insertTableData(productInOutInsertMap);
                
                //Inventory.BIN_ProductInOutDetail
                Map<String,Object> productInOutDetailInsertMap = new HashMap<String,Object>();
                productInOutDetailInsertMap.put("tableName", "Inventory.BIN_ProductInOutDetail");
                productInOutDetailInsertMap.put("BIN_ProductInOutID", productInOutID);
                productInOutDetailInsertMap.put("BIN_ProductVendorID", productVendorID);
                productInOutDetailInsertMap.put("DetailNo", 1);
                productInOutDetailInsertMap.put("Quantity", ConvertUtil.getString(insertPrtList.get(i).get("StockQuantity")));
                productInOutDetailInsertMap.put("StockType", ConvertUtil.getString(insertPrtList.get(i).get("StockType")));
                productInOutDetailInsertMap.put("BIN_InventoryInfoID", productOrderInsertMap.get("BIN_InventoryInfoID"));
                productInOutDetailInsertMap.put("BIN_LogicInventoryInfoID", productOrderInsertMap.get("BIN_LogicInventoryInfoID"));
                testService.insertTableData(productInOutDetailInsertMap);
            }
        }
        

        
        //启动工作流
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, productOrderID);
        mainData.put("UserInfo", userInfo);
        long workFlowID = binOLSTCM00_BL.StartOSWorkFlow(mainData);
        
        BINOLSTCM15_Form form = action.getModel();
        form.setEntryID(ConvertUtil.getString(workFlowID));
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        
        //设置日期
        form.setStartDate("2012-9-1");
        form.setEndDate("2012-9-7");
        form.setSort("UnitCode");
        form.setIDisplayLength(50);
        form.setProductVendorIDArr(productVendorIDArr);
        form.setInventoryInfoID(ConvertUtil.getString(productOrderInsertMap.get("BIN_InventoryInfoID")));
        form.setLogicInventoryInfoID(ConvertUtil.getString(productOrderInsertMap.get("BIN_LogicInventoryInfoID")));
        
        //proxy代理
        assertEquals("BINOLSTCM15_1", proxy.execute());

        assertEquals(insertPrtList.size(),action.getModel().getStockList().size());
        
        assertEquals(ConvertUtil.getInt(insertPrtList.get(0).get("Quantity")),action.getModel().getStockList().get(0).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(1).get("Quantity")),action.getModel().getStockList().get(1).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(2).get("Quantity")),action.getModel().getStockList().get(2).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(3).get("Quantity")),action.getModel().getStockList().get(3).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(4).get("Quantity")),action.getModel().getStockList().get(4).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(5).get("Quantity")),action.getModel().getStockList().get(5).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(6).get("Quantity")),action.getModel().getStockList().get(6).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(7).get("Quantity")),action.getModel().getStockList().get(7).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(8).get("Quantity")),action.getModel().getStockList().get(8).get("endQuantity"));
        assertEquals(ConvertUtil.getInt(insertPrtList.get(9).get("Quantity")),action.getModel().getStockList().get(9).get("endQuantity"));
    }
}