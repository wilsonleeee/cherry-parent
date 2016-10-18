/*	
 * @(#)BINOLPTRPS38_Action.java     1.0 2011/03/15		
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.form.BINOLPTRPS38_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS38_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 库存查询Action
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 */
public class BINOLPTRPS38_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS38_Form> {

	private static final long serialVersionUID = -5113325845359666482L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS11_Action.class.getName());

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	@Resource(name="binOLPTRPS38_BL")
	private BINOLPTRPS38_IF binolptrps38BL;
	
	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	/** 参数FORM */
	private BINOLPTRPS38_Form form = new BINOLPTRPS38_Form();

	/** 库存记录List */
	private List<Map<String, Object>> proStockList;
	
	/** 产品信息 */
	@SuppressWarnings("unchecked")
	private Map proInfo;
	
	/** 库存 */
	private long quantity;

	/** 产品库存详细 */
	@SuppressWarnings("unchecked")
	private List stockList;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	public Map getProInfo() {
		return proInfo;
	}

	public void setProInfo(Map proInfo) {
		this.proInfo = proInfo;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public List getStockList() {
		return stockList;
	}

	public void setStockList(List stockList) {
		this.stockList = stockList;
	}

	@Override
	public BINOLPTRPS38_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
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
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 查询假日
//		holidays = binolcm00BL.getHolidays(map);
		session.get("");
		String type = cm14bl.getConfigValue("1095",	userInfo.getBIN_OrganizationInfoID()+"",userInfo.getBIN_BrandInfoID()+"");
		form.setType(type);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 库存记录查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return
	 * 
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得库存记录总数 
		int count = binolptrps38BL.getProStockCount(searchMap);
		// 取得库存记录List
		proStockList = binolptrps38BL.getProStockList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS38_1";
	}
	
	public String getDetail() throws Exception{
		// 参数MAP
		Map<String, Object> map = getSearchMap();
		// 取得产品信息
		proInfo = binolptrps38BL.getProProduct(map);
		// 取得产品库存详细
		stockList = binolptrps38BL.getProStockDetails(map);
		getTotal(stockList);
		return SUCCESS;
	}
	/**
	 * 数据统计处理
	 * 
	 */
	private void getTotal(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			quantity += Long.parseLong(map.get("quantity").toString());
		}
	}
	
	
	/**
	 * 导出一览概要数据
	 * @return
	 * @throws Exception
	 */
	public String exportSummary() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		try {
			exportName = binOLCM37_BL.getResourceValue("BINOLPTRPS38",
					language, "RPS38_exportSummaryName")
					+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
			excelStream = new ByteArrayInputStream(binolptrps38BL.exportSummaryExcel(map));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
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
//		binolcm00BL.setParamsMap(map, orgInfoId, form.getStartDate(), 
//				form.getEndDate(),"Pro");
		// form中dataTable相关参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		// 子品牌
		map.put("originalBrand", form.getOriginalBrand());
		// 库存统计方式
		map.put("type",form.getType());
		// 产品有效状态
		map.put(CherryConstants.VALID_FLAG,form.getValidFlag());
		map.put("date", CherryUtil.getSysDateTime("yyyy-MM-dd"));
		
		// 大分类
		map.put("catePropValId", form.getCatePropValId());
		map.put("bigClassName", form.getBigClassName());
		// 逻辑仓库
		map.put("lgcInventoryId", form.getLgcInventoryId());
		
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
        // 业务日期
        String businessDate = binolcm00BL.getBussinessDate(map);
        map.put("businessDate",businessDate);
        
		return map;
	}

}
