package com.cherry.st.sfh.action;

import java.util.HashMap;
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
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;

public class BINOLSTSFH03_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	private BINOLSTSFH03_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTSFH03_Action.class, "/st","BINOLSTSFH03_init");
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
        
        //Basis.BIN_LogicInventory
        Map<String,Object> insertLogicInventoryMap = dataList.get(1);
        insertLogicInventoryMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicInventoryMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        int logicInventoryInfoID = testService.insertTableData(insertLogicInventoryMap);
        
        //Basis.BIN_LogicDepotBusiness
        Map<String,Object> insertLogicDepotBusiness = dataList.get(2);
        insertLogicDepotBusiness.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        insertLogicDepotBusiness.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        insertLogicDepotBusiness.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        testService.insertTableData(insertLogicDepotBusiness);
        
        BINOLSTSFH03_Form form = action.getModel();
        form.setProductOrderID(ConvertUtil.getString(productOrderID));
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertEquals("false",form.getProductOrderMainData().get("showRecStockFlag"));
    }
}