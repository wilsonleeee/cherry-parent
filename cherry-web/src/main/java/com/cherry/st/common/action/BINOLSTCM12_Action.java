/*	
 * @(#)BINOLSTCM12_Action     1.0 2012/06/26		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.st.common.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM12_BL;
import com.cherry.st.common.form.BINOLSTCM12_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品发货单弹出table共通Action
 * @author LuoHong
 *
 */
public class BINOLSTCM12_Action extends BaseAction implements ModelDriven<BINOLSTCM12_Form>{	

    private static final long serialVersionUID = 8744066279654756557L;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLSTCM12_BL")
	private BINOLSTCM12_BL binOLSTCM12_BL;
    
    @Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;

	/** 参数FORM */
    private BINOLSTCM12_Form form = new BINOLSTCM12_Form();
	
    @Override
    public BINOLSTCM12_Form getModel() {
        return form;
    }

    /**
     * 取得发货单List
     * @return
     * @throws Exception 
     */
    public String getDeliver() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = this.getSessionInfo();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);

        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("T1.DeliverNoIF");
        filterList.add("B.NameForeign");
        filterList.add("G.NameForeign");
        filterList.add("B.DepartCode");
        filterList.add("B.DepartName");
        filterList.add("G.DepartName");
        filterList.add("G.DepartCode");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
       
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        
        //测试类型
        String testType = binOLCM00_BL.getDepartTestType(request.getParameter("organizationId"));
        map.put("testType", testType);
        
        int count = binOLSTCM12_BL.getDeliverCount(map);
        if(count>0){
            form.setDeliverList(binOLSTCM12_BL.getDeliverList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSTCM12_1";
    }
    
    /**
     * 取得发货单详细List
     * @return
     * @throws Exception 
     */
    public void getDeliverDetail() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        // 收货方部门ID
        map.put("BIN_OrganizationIDReceive", form.getOrganizationIdReceive());
        map.put("BIN_ProductDeliverID", form.getDeliverId());
        map.put("depotInfoId", form.getDepotInfoId());
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        List<Map<String,Object>> list = binOLSTCM12_BL.getDeliverDetailList(map);
        
        for(Map<String, Object> detailsMap : list){
        	detailsMap.put("sort", "asc");
        	detailsMap.put("depotInfoId", form.getDepotInfoId());
        	detailsMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));

        	DecimalFormat df=new DecimalFormat("0.00");
        	
        	List<Map<String,Object>> proNewBatchStockList =binOLSTSFH06_BL.getProNewBatchStockList(detailsMap);
        	if(CherryUtil.isBlankList(proNewBatchStockList)){
        		detailsMap.put("costPrice", "");
        		detailsMap.put("totalCostPrice", "");
        	}else{
        		int quantity=ConvertUtil.getInt(detailsMap.get("quantity"));//发货单明细数量
				int tempQuantity=0;//总数量				
				double totalCostPrice = 0.00;//总成本价
				
				for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
					int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
					double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
					
					if(amount<quantity){//表示库存不够扣减
						totalCostPrice+=amount*costPrice;
						tempQuantity+=amount;
						quantity=quantity-amount;
					}
					else{//表示库存不够扣减此时停止循环
						totalCostPrice+=quantity*costPrice;
						tempQuantity+=quantity;
						break;
					}
				}	
				if(quantity == 0){
					detailsMap.put("costPrice", "");
					detailsMap.put("totalCostPrice", "");
				}else{					
					detailsMap.put("costPrice", df.format(totalCostPrice/tempQuantity));//默认四舍五入
					detailsMap.put("totalCostPrice", totalCostPrice);
				}
        	}
        }
        
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 取得session的信息
     * @param map
     * @throws Exception 
     */
    private Map<String,Object> getSessionInfo() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
        // 取得所属组织
        map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = (String)(userInfo.getCurrentBrandInfoID());
        if (brandInfoID!=null && !brandInfoID.equals("-9999")){
            // 取得所属品牌
            map.put("brandInfoID", brandInfoID);
        }
        // 取得语言区分
        map.put("language", userInfo.getLanguage());
        // 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 操作类型
        map.put("operationType", "1");
        // 业务类型
        map.put("businessType", "1");
        return map;
    }
}
