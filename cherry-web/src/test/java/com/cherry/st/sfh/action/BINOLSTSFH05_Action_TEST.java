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
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;

public class BINOLSTSFH05_Action_TEST extends CherryJunitBase{
	@Resource(name="TESTCOM_Service")
	private TESTCOM_Service testService;
	
	private BINOLSTSFH05_Action action;
	
    @Test
    @Rollback(true)
    @Transactional
    public void testInit1() throws Exception {
        String caseName = "testInit1";
        action = createAction(BINOLSTSFH05_Action.class, "/st","BINOLSTSFH05_init");
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        Map<String,Object> dataMap = (Map<String,Object>) DataUtil.getDataMap(this.getClass());
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);

        //插入数据
        //Inventory.BIN_ProductDeliver
        Map<String,Object> productDeliverInsertMap = dataList.get(0);
        int productDeliverID = testService.insertTableData(productDeliverInsertMap);
        
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
            
            //Inventory.BIN_ProductDeliverDetail
            Map<String,Object> productOrderDetailInsertMap = new HashMap<String,Object>();
            productOrderDetailInsertMap.put("tableName", "Inventory.BIN_ProductDeliverDetail");
            productOrderDetailInsertMap.put("BIN_ProductDeliverID",productDeliverID);
            productOrderDetailInsertMap.put("BIN_ProductVendorID",productVendorID);
            productOrderDetailInsertMap.put("DetailNo", i+1);
            productOrderDetailInsertMap.put("Quantity", "100");
            productOrderDetailInsertMap.put("BIN_InventoryInfoID", productDeliverInsertMap.get("BIN_DepotInfoID"));
            productOrderDetailInsertMap.put("BIN_LogicInventoryInfoID", productDeliverInsertMap.get("BIN_LogicInventoryInfoID"));
            int productDeliverDetailID = testService.insertTableData(productOrderDetailInsertMap);
        }
                
        BINOLSTSFH05_Form form = action.getModel();
        form.setProductDeliverId(ConvertUtil.getString(productDeliverID));
        DataUtil.getForm(this.getClass(), caseName, form);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        //设置语言
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        //proxy代理
        assertEquals("success", proxy.execute());
        
        assertEquals("false",form.getProductDeliverMainData().get("showRecStockFlag"));
    }
}