/*	
 * @(#)BINOLPTJCS06_Action.java     1.0 2011/04/26		
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
package com.cherry.pt.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM12_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS26_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS33_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS34_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS36_IF;

/**
 * 
 * 产品详细Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.04.26
 */
public class BINOLPTJCS36_Action extends BaseAction{
			
	private static final long serialVersionUID = 7052727878284081396L;

	@Resource(name="binOLPTJCS36_IF")
	private BINOLPTJCS36_IF binolptjcs36_IF;
	
	@Resource(name="binOLPTJCS33_IF")
	private BINOLPTJCS33_IF binolptjcs33_IF;
	
	@Resource(name="binOLCM12_BL")
	private BINOLCM12_BL binOLCM12_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLPTJCS34_IF")
	private BINOLPTJCS34_IF binOLPTJCS34_IF;
	
	/** 品牌模式 */
	private String brandMode;
	
	/** 产品ID */
	private int productId;
	
	/** 产品有效区分 */
	private String validFlag;
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 切换tabs */
	private String tabsIndex = "";
	
	/** 产品Map */
	@SuppressWarnings("unchecked")
	private Map proMap;

	/** 分类List */
	private List<Map<String, Object>> cateList;
	
	/** 销售价格List */
	private List<Map<String, Object>> sellPriceList;
	
	/** 产品条码List */
	private List<Map<String, Object>> barCodeList;
	
	/** 产品图片List */
	private List<Map<String, Object>> imgList;
	
	/** BOM组件List */
	private List<Map<String, Object>> bomList;
	
	/** 产品扩展属性个数 */
	private int productExtCount;
	
	/** 是否一品多码 */
	private boolean isU2M = false;
	
	/** 产品编码条码修改履历List */
	private List<Map<String, Object>> prtBCHistoryList;
	
	/** 部门价格规则List */
	private List<Map<String, Object>> priceRuleList;
	
	public int getProductExtCount() {
		return productExtCount;
	}

	public void setProductExtCount(int productExtCount) {
		this.productExtCount = productExtCount;
	}

	public List<Map<String, Object>> getImgList() {
		return imgList;
	}

	public void setImgList(List<Map<String, Object>> imgList) {
		this.imgList = imgList;
	}

	public String getBrandMode() {
		return brandMode;
	}

	public void setBrandMode(String brandMode) {
		this.brandMode = brandMode;
	}
	public int getProductId() {
		return productId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getTabsIndex() {
		return tabsIndex;
	}

	public void setTabsIndex(String tabsIndex) {
		this.tabsIndex = tabsIndex;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	@SuppressWarnings("unchecked")
	public Map getProMap() {
		return proMap;
	}
	@SuppressWarnings("unchecked")
	public void setProMap(Map proMap) {
		this.proMap = proMap;
	}

	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

	public List<Map<String, Object>> getSellPriceList() {
		return sellPriceList;
	}

	public void setSellPriceList(List<Map<String, Object>> sellPriceList) {
		this.sellPriceList = sellPriceList;
	}

	public List<Map<String, Object>> getBarCodeList() {
		return barCodeList;
	}

	public void setBarCodeList(List<Map<String, Object>> barCodeList) {
		this.barCodeList = barCodeList;
	}

	public List<Map<String, Object>> getBomList() {
		return bomList;
	}

	public void setBomList(List<Map<String, Object>> bomList) {
		this.bomList = bomList;
	}
	
	public boolean getIsU2M() {
		return isU2M;
	}

	public void setU2M(boolean isU2M) {
		this.isU2M = isU2M;
	}
	
	public List<Map<String, Object>> getPrtBCHistoryList() {
		return prtBCHistoryList;
	}

	public void setPrtBCHistoryList(List<Map<String, Object>> prtBCHistoryList) {
		this.prtBCHistoryList = prtBCHistoryList;
	}
	
	public List<Map<String, Object>> getPriceRuleList() {
		return priceRuleList;
	}

	public void setPriceRuleList(List<Map<String, Object>> priceRuleList) {
		this.priceRuleList = priceRuleList;
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
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 产品ID
		map.put(ProductConstants.PRODUCTID, productId);
		map.put(CherryConstants.VALID_FLAG, validFlag);
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 取得产品信息
		proMap = binolptjcs36_IF.getDetail(map);
		// 取得产品条码List
		barCodeList = binolptjcs36_IF.getBarCodeList(map);
		// 取得销售价格List
		sellPriceList = binolptjcs36_IF.getSellPriceList(map);
		// 取得图片List
		imgList = binolptjcs36_IF.getImgList(map);
		// 产品类型
		String mode = ConvertUtil.getString(proMap.get(ProductConstants.MODE));
		// BOM,套装类型
		if (mode.equals(ProductConstants.MODE_1)
				|| mode.equals(ProductConstants.MODE_3)) {
			// 查询产品BOM表
			bomList = binolptjcs36_IF.getBOMList(map);
		}
		
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 产品扩展属性
		map.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);
		// 产品扩展属性个数
		productExtCount = binOLCM12_BL.getExtProCount(map);
		
		// 产品编码条码修改履历
		prtBCHistoryList = binolptjcs36_IF.getPrtBCHistoryList(map);
		
		//是否支持一品多码
		isU2M = binOLCM14_BL.isConfigOpen("1077",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		
		// 业务日期
		String businessDate = binOLPTJCS34_IF.getBussinessDate(map);
		map.put("businessDate",businessDate);
		
		// 取得当前用户是否是柜台用户
		if (userInfo.getDepartType().equals("4")) {
			map.put("isCntDepart", 1);
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			map.put("departCode", counterInfo.getCounterCode());
		}

		// 查询当前柜台对应的方案
		Map<String, Object> departSoluMap = binOLPTJCS34_IF.getDepartSolu(map);

		if (null != departSoluMap && !departSoluMap.isEmpty()) {
			String productPriceSolutionID = ConvertUtil.getString(departSoluMap.get("BIN_ProductPriceSolutionID"));
			map.put("productPriceSolutionID", productPriceSolutionID);
		}
		
		// 取得方案产品明细信息
		Map soluDetailPrtInfoMap = binolptjcs36_IF.getSoluDeatilPrtInfo(map);
		if(null != soluDetailPrtInfoMap && !soluDetailPrtInfoMap.isEmpty()){
			proMap.put("nameCN", soluDetailPrtInfoMap.get("SoluProductName"));
			proMap.put("salePrice", soluDetailPrtInfoMap.get("SalePrice"));
			proMap.put("memPrice", soluDetailPrtInfoMap.get("MemPrice"));
			proMap.put("minSalePrice", soluDetailPrtInfoMap.get("MinSalePrice"));
			proMap.put("maxSalePrice", soluDetailPrtInfoMap.get("MaxSalePrice"));
		}
		// 取得分类List
		cateList = binolptjcs33_IF.getCategoryList(map);
		// 获取产品方案明细的产品分类值List
		List<Map<String, Object>> cateValList = binolptjcs36_IF.getPrtSoluDetailCateList(map);
		// 产品分类List处理
		addValue(cateList, cateValList);
		
		return SUCCESS;
	}
	
	/**
	 * 产品分类值选中处理
	 * 
	 * @param cateList
	 * @param cateValList
	 */
	private void addValue(List<Map<String, Object>> cateList,
			List<Map<String, Object>> cateValList) {
		if (null != cateList) {
			for (Map<String, Object> map : cateList) {
				// 分类ID
				int proplId1 = CherryUtil.obj2int(map
						.get(ProductConstants.PROPID));
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for (Map<String, Object> item : cateValList) {
					// 分类ID
					int proplId2 = CherryUtil.obj2int(item
							.get(ProductConstants.PROPID));
					if(proplId1 == proplId2){
						list.add(item);
					}
				}
				map.put(ProductConstants.LIST, list);
			}
		}
	}
}
