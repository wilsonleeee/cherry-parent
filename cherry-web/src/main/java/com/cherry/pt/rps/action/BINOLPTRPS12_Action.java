/*	
 * @(#)BINOLPTRPS12_Action.java     1.0 2011/03/16		
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
package com.cherry.pt.rps.action;

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
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.bl.BINOLPTRPS12_BL;
import com.cherry.pt.rps.form.BINOLPTRPS12_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品详细库存Action
 * 
 * @author lipc
 * @version 1.0 2011.03.16
 */
public class BINOLPTRPS12_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS12_Form> {

	private static final long serialVersionUID = 6468389889955482732L;

	@Resource
	private BINOLCM00_BL binolcm00BL;

	@Resource
	private BINOLPTRPS12_BL binolptrps12BL;

	/** 参数FORM */
	private BINOLPTRPS12_Form form = new BINOLPTRPS12_Form();

	/** 产品信息 */
	@SuppressWarnings("unchecked")
	private Map proInfo;

	/** 产品库存详细 */
	@SuppressWarnings("unchecked")
	private List stockList;
	
	/** 产品库存详细单据List */
	private List<Map<String, Object>> detailList;

	/** 期初总结存 */
	private long startQuantity;

	/** 期末总结存 */
	private long endQuantity;

	/** 本期总收入 */
	private long currentIn;

	/** 本期总发出 */
	private long currentOut;

	@Override
	public BINOLPTRPS12_Form getModel() {
		return form;
	}
	@SuppressWarnings("unchecked")
	public Map getProInfo() {
		return proInfo;
	}
	@SuppressWarnings("unchecked")
	public void setProInfo(Map proInfo) {
		this.proInfo = proInfo;
	}
	@SuppressWarnings("unchecked")
	public List getStockList() {
		return stockList;
	}
	@SuppressWarnings("unchecked")
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
	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
	}
	public List<Map<String, Object>> getDetailList() {
		return detailList;
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
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		// 参数MAP
		Map<String, Object> map = getSearchMap();
		// 取得产品信息
		proInfo = binolptrps12BL.getProProduct(map);
		// 取得产品库存详细
		stockList = binolptrps12BL.getProStockDetails(map);
		
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
					+ (Long) map.get("inQuantity7")
					+ (Long) map.get("inQuantity8"));
			currentOut += ((Long) map.get("outQuantity1")
					+ (Long) map.get("outQuantity2")
					+ (Long) map.get("outQuantity3")
					+ (Long) map.get("outQuantity4") 
					+ (Long) map.get("outQuantity5")
					+ (Long) map.get("outQuantity6")
					+ (Long) map.get("outQuantity7")
					+ (Long) map.get("outQuantity8")
					+ (Long) map.get("outQuantity9")
					+ (Long) map.get("outQuantity10")
					+ (Long) map.get("outQuantity11"));
		}
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 库存相关的日期参数设置到paramMap中
		binolcm00BL.setParamsMap(map, orgInfoId, form.getStartDate(), form
				.getEndDate(),"Pro");
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
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
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
		map.put("prtVendorId", form.getPrtVendorId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		
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
		if(CherryConstants.BUSINESS_TYPE_NS.equals(tradeType)||CherryConstants.BUSINESS_TYPE_SR.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_MV.equals(tradeType)||CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){
			int stockType =form.getStockType();
			map.put("stockType", stockType);
		}
		
		detailList=binolptrps12BL.getdetailed(map);
		
		
		//发货 、退库、自由出库、调出、报损、礼品领用入出库区分
		if(CherryConstants.BUSINESS_TYPE_SD.equals(tradeType)||CherryConstants.BUSINESS_TYPE_RR.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_LG.equals(tradeType)||CherryConstants.BUSINESS_TYPE_OT.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_LS.equals(tradeType)||CherryConstants.BUSINESS_TYPE_SP.equals(tradeType)){
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
		//收货 、接收退库、调入、  自由入库、入出库区分
		}else if(CherryConstants.BUSINESS_TYPE_RD.equals(tradeType)||CherryConstants.BUSINESS_TYPE_AR.equals(tradeType)
				||CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)||CherryConstants.BUSINESS_TYPE_GR.equals(tradeType)){ 
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
