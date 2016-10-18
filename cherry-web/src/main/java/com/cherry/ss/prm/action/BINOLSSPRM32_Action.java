/*	
 * @(#)BINOLSSPRM32_Action.java     1.0 2010/11/16		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM32_BL;
import com.cherry.ss.prm.form.BINOLSSPRM32_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品详细库存Action
 * 
 * @author lipc
 * @version 1.0 2010.11.16
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM32_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM32_Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6468389889955482732L;

	@Resource
	private BINOLCM00_BL binolcm00BL;

	@Resource
	private BINOLSSPRM32_BL binolssprm32BL;

	/** 参数FORM */
	private BINOLSSPRM32_Form form = new BINOLSSPRM32_Form();

	/** 促销品信息 */
	private Map proInfo;
	
	/** 产品库存详细单据List */
	private List<Map<String, Object>> detailList;

	public List<Map<String, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
	}

	/** 产品库存详细 */
	private List stockList;

	/** 期初总结存 */
	private long startQuantity;

	/** 期末总结存 */
	private long endQuantity;

	/** 本期总收入 */
	private long currentIn;

	/** 本期总发出 */
	private long currentOut;

	@Override
	public BINOLSSPRM32_Form getModel() {
		return form;
	}

	public Map getProInfo() {
		return proInfo;
	}

	public void setProInfo(Map proInfo) {
		this.proInfo = proInfo;
	}

	public List getStockList() {
		return stockList;
	}

	public void setStockList(List stockList) {
		this.stockList = stockList;
	}

	public long getStartQuantity() {
		return startQuantity;
	}

	public void setStartQuantity(long startQuantity) {
		this.startQuantity = startQuantity;
	}

	public long getEndQuantity() {
		return endQuantity;
	}

	public void setEndQuantity(long endQuantity) {
		this.endQuantity = endQuantity;
	}

	public long getCurrentIn() {
		return currentIn;
	}

	public void setCurrentIn(long currentIn) {
		this.currentIn = currentIn;
	}

	public long getCurrentOut() {
		return currentOut;
	}

	public void setCurrentOut(long currentOut) {
		this.currentOut = currentOut;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		// 参数MAP
		Map<String, Object> map = getSearchMap();
		// 取得促销品信息
		proInfo = binolssprm32BL.getProProduct(map);
		// 取得促销品库存详细
		stockList = binolssprm32BL.getProStockDetails(map);
		// 统计处理
		getTotal(stockList);
		return SUCCESS;
	}

	/**
	 * 数据统计处理
	 * 
	 */
	private void getTotal(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			startQuantity += (Long) map.get("startQuantity");
			endQuantity += (Long) map.get("endQuantity");
			currentIn += ((Long) map.get("inQuantity1")
					+ (Long) map.get("inQuantity2")
					+ (Long) map.get("inQuantity3")
					+ (Long) map.get("inQuantity4") 
					+ (Long) map.get("inQuantity5")
					+ (Long) map.get("inQuantity6")
					+ (Long) map.get("inQuantity7"));
			currentOut += ((Long) map.get("outQuantity1")
					+ (Long) map.get("outQuantity2")
					+ (Long) map.get("outQuantity3")
					+ (Long) map.get("outQuantity4") 
					+ (Long) map.get("outQuantity5")
					+ (Long) map.get("outQuantity6")
					+ (Long) map.get("outQuantity7")
					+ (Long) map.get("outQuantity8"));
		}
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 库存相关的日期参数设置到paramMap中
		binolcm00BL.setParamsMap(map, orgInfoId, form.getStartDate(), form
				.getEndDate(),"Prm");
		form.setCutOfDate(ConvertUtil.getString(map.get("cutOfDate")));
		form.setDate1(ConvertUtil.getString(map.get("date1")));
		form.setDate2(ConvertUtil.getString(map.get("date2")));
		form.setFlag(ConvertUtil.getString(map.get("flag")));
		form.setFlagB(ConvertUtil.getString(map.get("flagB")));
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 促销品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	/**
	 * 产品库存详细单据
	 * 
	 */
	public String getdetailed () throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//产品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		
		//开始日期
		map.put("startDate", form.getStartDate());
		
		//截止日期
		map.put("endDate", form.getEndDate());
		
		//实体仓库ID
		map.put("inventoryInfoID", form.getInventoryInfoID());
		
		//逻辑仓库ID
		map.put("logicInventoryInfoID", form.getLogicInventoryInfoID());
		
		//业务类型
		map.put("tradeType", form.getTradeType());
		String tradeType =form.getTradeType();
		if(CherryConstants.BUSINESS_TYPE_STOCKTAKE.equals(tradeType)||CherryConstants.BUSINESS_TYPE_GIFT.equals(tradeType)){
			int stockType =form.getStockType();
			map.put("stockType", stockType);
		}
		
		detailList=binolssprm32BL.getdetailed(map);
		
		
		//发货 、退货、调拨确认、出库区分
		if(CherryConstants.BUSINESS_TYPE_DELIVER_SEND.equals(tradeType)||CherryConstants.BUSINESS_TYPE_DELIVER_RETURN.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_ALLOCATION_RESPONSE.equals(tradeType)||CherryConstants.BUSINESS_TYPE_STORAGE_OUT.equals(tradeType)){
			for(Map<String, Object> detailMap : detailList){
				// 入出库区分
				String stockType =ConvertUtil.getString(detailMap.get("StockType"));
				// 入库
				if("0".equals(stockType)){
					long quantity= CherryUtil.obj2Long(detailMap.get("Quantity")) * -1;
					detailMap.put("Quantity", quantity);
					detailMap.put("StockType", "1");
				}
			}
		//收货 、接收退库、调入、  入库区分
		}else if(CherryConstants.BUSINESS_TYPE_DELIVER_RECEIVE.equals(tradeType)||CherryConstants.BUSINESS_TYPE_CANCELLING.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_STORAGE_IN.equals(tradeType)||CherryConstants.BUSINESS_TYPE_ALLOCATION_REQUEST.equals(tradeType)){ 
			for(Map<String, Object> detailMap : detailList){
				// 入出库区分
				String stockType =ConvertUtil.getString(detailMap.get("StockType"));
				// 出库
				if("1".equals(stockType)){
					long quantity= CherryUtil.obj2Long(detailMap.get("Quantity")) * -1;
					detailMap.put("Quantity", quantity);
					detailMap.put("StockType", "0");
				}
			}
		}

		return SUCCESS;
	}
}
