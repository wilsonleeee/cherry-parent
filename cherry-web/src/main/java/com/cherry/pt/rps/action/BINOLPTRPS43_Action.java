/*  
 * @(#)BINOLPTRPS43_Action.java     1.0 2011/05/31      
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
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.rps.form.BINOLPTRPS43_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS13_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS43_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 *
 * 产品销售记录查询Action
 *
 * @author jijw
 *
 * @version  2013-3-6
 */
public class BINOLPTRPS43_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS43_Form> {

	private static final long serialVersionUID = 8947628908345198289L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS43_Action.class.getName());

	// FROM
	private BINOLPTRPS43_Form form = new BINOLPTRPS43_Form();

	// 注入BL
	@Resource(name="binOLPTRPS43_BL")
	private BINOLPTRPS43_IF binOLPTRPS43_BL;
	// 注入BL
	@Resource(name="binOLPTRPS13_BL")
	private BINOLPTRPS13_IF binOLPTRPS13_BL;	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	/** 导出excel共通BL **/

	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// 销售记录LIST
	private List<Map<String, Object>> saleRecordList;

	// 销售记录总数
	private int count = 0;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	// 假日
	private String holidays;
	
	/**  各商品统计详细（各商品的总数量及总金额） */
	private List<Map<String, Object>> saleProPrmDetailList;
	
	/**  销售统计 */
	private Map<String, Object> sumInfo;
	
	/**  模式 */
	private int selectMode;
	
	/**  销售商品统计（计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品） */
	private Map<String, Object> saleSumInfo = new HashMap<String, Object>();
	
	/**  销售统计（只统计销售日期区间，不包含其他查询条件） */
	private Map<String, Object> sumInfoBySaleDateArea;

	/**  显示商品详细DIV按钮 ,0:不显示  */
	private String disSaleID = "0";
	
	/** 支付方式List */
	private List<Map<String, Object>> payTypeList;
	
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "3");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
//		// 开始日期
//		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
//				.getBIN_OrganizationInfoID(), new Date()));
//		// 截止日期
//		form.setEndDate(CherryUtil
//				.getSysDateTime(CherryConstants.DATE_PATTERN));
		// ======================= lipc修改开始：开始截止日期当天 ======================== //
		String date = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		// 开始日期
		form.setStartDate(date);
		// 截止日期
		form.setEndDate(date);
		// ======================= lipc修改结束：开始截止日期当天 ======================== //
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		
		// 支付方式
//		payTypeList = binOLPTRPS43_BL.getPayTypeList(map);
		
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binOLPTRPS43_BL.getExportDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "saleTime desc");
		// 取得销售记录信息List
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS13", language, "RPS13_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTRPS43_BL.exportExcel(map), extName+".xls"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLPTRPS43_excel";
	}
	
	/**
     * 导出CSV
     */
    public String exportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "saleTime desc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		map.put("charset", form.getCharset());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		int count = binOLPTRPS43_BL.getExportDetailCount(map);
    		// 导出CSV最大数据量
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		
    		if(count > maxCount) {
    			// 明细数据量大于CSV导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binOLPTRPS43_BL.exportCSV(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
    /**
     * 海明威特殊导出模板（主单）
     * @return
     * @throws Exception
     */
    public String exportMain() throws Exception {
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "saleTime desc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		map.put("charset", form.getCharset());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		int count = binOLPTRPS43_BL.getSaleRecordCount(map);
    		// 导出CSV最大数据量
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		
    		if(count > maxCount) {
    			// 明细数据量大于CSV导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binOLPTRPS43_BL.exportMain(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
    /**
     * 海明威特殊导出模板（明细单）
     * @return
     * @throws Exception
     */
    public String exportDetail() throws Exception {
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "saleTime desc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		map.put("charset", form.getCharset());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		int count = binOLPTRPS43_BL.getExportDetailFilterKDCount(map);
    		// 导出CSV最大数据量
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		
    		if(count > maxCount) {
    			// 明细数据量大于CSV导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binOLPTRPS43_BL.exportDetail(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }
    
	
	@SuppressWarnings("unchecked")
	public String searchOne() throws JSONException{
		
		/**
		 * 修改销售数量和金额统计，将退货的商品从销售总数量和总金额中减去，并且无论查询
		 * 条件如何都要在明细的左上角显示出总数量和总金额
		 * 
		 * 对应问题票：NEWWITPOS-1169
		 * 
		 * @author zhanggl
		 * @date 2012-02-03
		 * 
		 * */
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		selectMode = 1;
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		List<String> prtList = (List<String>)searchMap.get("prtList");
		String[] prtListArr = new String[prtList.size()];
		for(int i =0;i<prtList.size();i++){
			prtListArr[i]=prtList.get(i).toString();
		}
		searchMap.put("prtListArr", prtListArr);
		
		List<String> prmList = (List<String>)searchMap.get("prmList");
		String[] prmListArr = new String[prmList.size()];
		for(int i =0;i<prmList.size();i++){
			prmListArr[i]=prmList.get(i).toString();
		}
		searchMap.put("prmListArr", prmListArr);
		sumInfo = binOLPTRPS43_BL.getSumInfo(searchMap);
		
		// 获取销售记录总数
//		count = binOLPTRPS43_BL.getSaleRecordCount(searchMap);
		count = (Integer)sumInfo.get("sum");
		
		List<Map<String,Object>> saleProPrmListPara = (List<Map<String,Object>>)searchMap.get("saleProPrmList");
		if(saleProPrmListPara.size() != 0){
		
			// 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
			saleSumOne(searchMap, saleProPrmListPara);			
		}
		
		// 获取销售记录LIST
		saleRecordList = binOLPTRPS43_BL.getSaleRecordList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS43_1";
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws JSONException{
		
		/**
		 * 修改销售数量和金额统计，将退货的商品从销售总数量和总金额中减去，并且无论查询
		 * 条件如何都要在明细的左上角显示出总数量和总金额
		 * 
		 * 对应问题票：NEWWITPOS-1169
		 * 
		 * @author zhanggl
		 * @date 2012-02-03
		 * 
		 * */
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		selectMode = 0;
		//是否显示产品入出库成本
		boolean config  = binOLCM14_BL.isConfigOpen("1367",ConvertUtil.getString(searchMap.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(searchMap.get(CherryConstants.BRANDINFOID)));
		if(config){
			searchMap.put("isShow", "1");
			form.setIsShow("1");
		}else{
			searchMap.put("isShow", "0");
			form.setIsShow("0");
		}
		sumInfo = binOLPTRPS13_BL.getSumInfo(searchMap);
		
		// 获取销售记录总数
//		count = binOLPTRPS13_BL.getSaleRecordCount(searchMap);
		count = (Integer)sumInfo.get("sum");
		
		List<Map<String,Object>> saleProPrmListPara = (List<Map<String,Object>>)searchMap.get("saleProPrmList");
		if(saleProPrmListPara.size() != 0){
		
			// 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
			saleSum(searchMap, saleProPrmListPara);			
		}
		
		// 获取销售记录LIST
		saleRecordList = binOLPTRPS13_BL.getSaleRecordList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS43_1";
	}
	
	/**
	 * // 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
	 * @param searchMap
	 * @param saleProPrmListPara 查询框销售商品List
	 */
	private void saleSumOne(Map<String,Object> searchMap ,List<Map<String,Object>> saleProPrmListPara){
		// 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
		searchMap.put("sumQuantity", sumInfo.get("sumQuantity"));
		searchMap.put("sumAmount", sumInfo.get("sumAmount"));
		// 各商品统计详细（各商品的总数量及总金额） 
		saleProPrmDetailList = binOLPTRPS43_BL.getSaleProPrmList(searchMap);
		
		// 销售商品总数量
		BigDecimal quantityBD = new BigDecimal(0);
		// 销售商品总金额
		BigDecimal amountBD = new BigDecimal(0);
		
		// 取得各商品统计详细List中各销售商品的总数量之和、总金额之和
		for(Map<String,Object> itemMap : saleProPrmDetailList){
			String barCode = (String)itemMap.get("barCode");
			String type = (String)itemMap.get("type");
			
			for(Map<String,Object> paraMap : saleProPrmListPara){
				String prmPrtVendorId = (String)paraMap.get("prmPrtVendorId");
				String prtType = (String)paraMap.get("prtType");
				
				if(barCode.equals(prmPrtVendorId) && type.equals(prtType)){
					BigDecimal quantity =  (BigDecimal)itemMap.get("quantity");
					BigDecimal amount =  (BigDecimal)itemMap.get("amount");
					quantityBD = quantityBD.add(quantity);
					amountBD = amountBD.add(amount);
				}
			}
		}
		if(!(quantityBD.equals(new BigDecimal(0)) && amountBD.equals(new BigDecimal(0)))){
			saleSumInfo.put("saleSumQuantity", quantityBD);
			saleSumInfo.put("saleSumAmount", amountBD);
		}
	}
	/**
	 * // 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
	 * @param searchMap
	 * @param saleProPrmListPara 查询框销售商品List
	 */
	private void saleSum(Map<String,Object> searchMap ,List<Map<String,Object>> saleProPrmListPara){
		// 计算销售商品的总数量及总金额（只统计销售商品，不包括连带商品）
		searchMap.put("sumQuantity", sumInfo.get("sumQuantity"));
		searchMap.put("sumAmount", sumInfo.get("sumAmount"));
		// 各商品统计详细（各商品的总数量及总金额） 
		saleProPrmDetailList = binOLPTRPS13_BL.getSaleProPrmList(searchMap);
		
		// 销售商品总数量
		BigDecimal quantityBD = new BigDecimal(0);
		// 销售商品总金额
		BigDecimal amountBD = new BigDecimal(0);
		// 销售商品总成本价
		BigDecimal costPriceBD = new BigDecimal(0);
		
		// 取得各商品统计详细List中各销售商品的总数量之和、总金额之和
		for(Map<String,Object> itemMap : saleProPrmDetailList){
			Integer id = (Integer)itemMap.get("id");
			String type = (String)itemMap.get("type");
			
			for(Map<String,Object> paraMap : saleProPrmListPara){
				String prmPrtVendorId = (String)paraMap.get("prmPrtVendorId");
				String prtType = (String)paraMap.get("prtType");
				
				if(id.toString().equals(prmPrtVendorId) && type.equals(prtType)){
					BigDecimal quantity =  (BigDecimal)itemMap.get("quantity");
					BigDecimal amount =  (BigDecimal)itemMap.get("amount");
					quantityBD = quantityBD.add(quantity);
					amountBD = amountBD.add(amount);
					if(searchMap.get("isShow").equals("1")){//需要显示统计成本价
						BigDecimal costPrice =  (BigDecimal)itemMap.get("costPrice");
						costPriceBD = costPriceBD.add(costPrice);
					}
				}
			}
		}
		if(!(quantityBD.equals(new BigDecimal(0)) && amountBD.equals(new BigDecimal(0)))){
			saleSumInfo.put("saleSumQuantity", quantityBD);
			saleSumInfo.put("saleSumAmount", amountBD);
			if(searchMap.get("isShow").equals("1")){//需要显示统计成本价
				saleSumInfo.put("saleSumCostPrice", costPriceBD);
			}
		}
	}
	
	/**
	 * 取得销售商品详细（各商品的总数量及总金额）
	 * @throws Exception
	 */
	public String getSaleProPrmSum() throws Exception{
		
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		List<String> prtList = (List<String>)searchMap.get("prtList");
		String[] prtListArr = new String[prtList.size()];
		for(int i =0;i<prtList.size();i++){
			prtListArr[i]=prtList.get(i).toString();
		}
		searchMap.put("prtListArr", prtListArr);
		
		List<String> prmList = (List<String>)searchMap.get("prmList");
		String[] prmListArr = new String[prmList.size()];
		for(int i =0;i<prmList.size();i++){
			prmListArr[i]=prmList.get(i).toString();
		}
		searchMap.put("prmListArr", prmListArr);
		//  获取销售日期区间统计（只统计销售日期区间，不包含其他查询条件） 
//		Map<String, Object> searchSDMap = getSearchMapBySDArea(searchMap);
		sumInfoBySaleDateArea = binOLPTRPS43_BL.getSumInfo(searchMap);
		
		searchMap.put("sumQuantity", sumInfoBySaleDateArea.get("sumQuantity")); // 用于计算商品总数量占比
		searchMap.put("sumAmount", sumInfoBySaleDateArea.get("sumAmount")); //用于计算商品总金额占比
		// 查询各商品总数量、总金额、数量占比、金额占比
		searchMap.put("selqPaP", "selqPaP"); // 动态拼接"商品总数量占比"使用，value值无实际意义
		saleProPrmDetailList = binOLPTRPS43_BL.getSaleProPrmList(searchMap);
		
		return "BINOLPTRPS43_2";
	}
	
	/**
	 * 取得销售区间的必要查询条件
	 * @param searchMap
	 * @param searchSDMap
	 */
	private Map<String, Object> getSearchMapBySDArea(Map<String,Object> searchMap){
		
		Map<String, Object> searchSDMap = new HashMap<String, Object>();
		searchSDMap.put("startDate", searchMap.get("startDate"));
		searchSDMap.put("endDate", searchMap.get("endDate"));
		
		searchSDMap.put("organizationInfoId", searchMap.get("organizationInfoId"));
		searchSDMap.put("brandInfoId", searchMap.get("brandInfoId"));
		searchSDMap.put("language", searchMap.get("language"));
		
		searchSDMap.put("userId", searchMap.get("userId"));
		searchSDMap.put("operationType", searchMap.get("operationType"));
		searchSDMap.put("businessType", searchMap.get("businessType"));
		
		searchSDMap.put("START", searchMap.get("START"));
		searchSDMap.put("END", searchMap.get("END"));
		
		return searchSDMap;
		
	}
	
	/**
	 * 查询销售商品ajax
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public void indSearchPrmPrt() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 取得促销品信息结果String
		String resultString = binOLPTRPS43_BL.indSearchPrmPrt(map);
		// 将数据传到页面
		ConvertUtil.setResponseByAjax(response, resultString);
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
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 单号
		map.put("billCode", form.getBillCode().trim());
		// 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 业务类型
		map.put("saleType", form.getSaleType());
		//消费者类型
		map.put("consumerType", form.getConsumerType());
		//发票类型
		map.put("invoiceFlag",form.getInvoiceFlag());
		//会员卡号
		map.put("memCode", form.getMemCode());
		// 销售人员
		map.put("employeeCode", form.getEmployeeCode());
		
		// 活动类型（0：会员活动，1：促销活动）
		map.put("campaignMode", form.getCampaignMode());
		
		// 活动代码
		map.put("campaignCode", form.getCampaignCode());
		// 活动名称（用于显示导出报表的查询条件）
		map.put("campaignName", form.getCampaignName());
		// 支付方式
		map.put("payTypeCode", form.getPayTypeCode());
		// 流水号
		map.put("saleRecordCode", form.getSaleRecordCode());
		// 单据类型
		map.put("ticketType", form.getTicketType());
		// 发货模式
		map.put("deliveryModel", form.getDeliveryModel());
		// 品牌
		map.put("originalBrand", form.getOriginalBrand());
		
		// 查询参数MAP取得:销售商品及连带销售
		getProPrm(map);
		
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		// map参数trim处理
		CherryUtil.trimMap(map);
        
		return map;
	}
	
	/**
	 * 查询参数MAP取得:销售商品及连带销售
	 * 
	 * @param map
	 * 
	 */
	private void getProPrm(Map<String, Object> map){
		// ********* 2013-02-25  改进销售记录查询的查询条件，实现多产品查询(NEWWITPOS-1727) start  *********//
		
		// 销售商品拼接查询条件：AND/OR 
		String saleProPrmConcatStr = form.getSaleProPrmConcatStr();
		// 销售商品厂商ID(id,prtType)
		List<String> salePrtVendorIdList = form.getSalePrtVendorId();
		
		// 用于查询各商品统计的产品查询list
		List<String> prtList = new ArrayList<String>();
		// 用于查询各商品统计的促销品查询list
		List<String> prmList = new ArrayList<String>();
		
		// 销售商品的List<Map<String,Object>> 包括产品厂商ID及销售类型（N/P）
		List<Map<String,Object>> saleProPrmList = new ArrayList<Map<String,Object>>();
		for(String itemString : salePrtVendorIdList){
			Map<String,Object> saleProPrmMap = new HashMap<String, Object>();
			String prmPrtVendorId = itemString.split("_")[0];
			saleProPrmMap.put("prmPrtVendorId", prmPrtVendorId);
			String prtType = itemString.split("_")[1];
			saleProPrmMap.put("prtType", prtType);
			saleProPrmList.add(saleProPrmMap);
			
			if("N".equals(prtType)){
				prtList.add(prmPrtVendorId);
			} 
			else if ("P".equals(prtType)){
				prmList.add(prmPrtVendorId);
			}
			
		}
		
		map.put("saleProPrmConcatStr", saleProPrmConcatStr);
		map.put("saleProPrmList", saleProPrmList);
		
		// 连带销售拼接查询条件：AND/OR 
		String jointProPrmConcatStr = form.getJointProPrmConcatStr();
		// 连带销售厂商ID(id,prtType)
		List<String> joinPrtVendorIdList = form.getJoinPrtVendorId();
		
		// 连带销售的List<Map<String,Object>> 包括产品厂商ID及销售类型（N/P）
		List<Map<String,Object>> jointProPrmList = new ArrayList<Map<String,Object>>();
		for(String itemString : joinPrtVendorIdList){
			Map<String,Object> joinProPrmMap = new HashMap<String, Object>();
			String prmPrtVendorId = itemString.split("_")[0];
			joinProPrmMap.put("prmPrtVendorId", prmPrtVendorId);
			String prtType = itemString.split("_")[1];
			joinProPrmMap.put("prtType", prtType);
			jointProPrmList.add(joinProPrmMap);
			
			if("N".equals(prtType)){
				prtList.add(prmPrtVendorId);
			} 
			else if ("P".equals(prtType)){
				prmList.add(prmPrtVendorId);
			}
		}
		
		map.put("jointProPrmConcatStr", jointProPrmConcatStr);
		map.put("jointProPrmList", jointProPrmList);
		
		// 是否存在商品（销售商品或连带销售）查询，用于SQL动态判定
		if(saleProPrmList.size() != 0 || jointProPrmList.size() != 0){
			map.put("byProPrm", true);
			disSaleID = "1";
		}
		
		map.put("prtList", prtList);
		map.put("prmList", prmList);
		
		// 各商品统计的产品与促销品查询，用于SQL动态判定
		if(prtList.size() > 0 && prmList.size() >0){
			map.put("byPrtPrmList", true);
		}
		if(prtList.size() > 0 || prmList.size() >0){
			map.put("byPrtPrmListOR", true);
		}
		
		// ********* 2013-02-25  改进销售记录查询的查询条件，实现多产品查询(NEWWITPOS-1727) end  *********//
		//销售商品、连带商品详细（编码、条码、名称）
		map.put("saleProPrmNameShow", form.getSaleProPrmName());
		map.put("joinProPrmNameShow", form.getJoinProPrmName());
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
//		if (CherryChecker.isNullOrEmpty(startDate)) {
//			this.addFieldError("startDate", getText("ECM00009",
//					new String[] { getText("PCM00001") }));
//			this.addActionError(getText("ECM00009", new String[] { getText("PCM00001") }));
//			isCorrect = false;
//		}
//		if (CherryChecker.isNullOrEmpty(endDate)) {
//			this.addFieldError("endDate", getText("ECM00009",
//					new String[] { getText("PCM00002") }));
//			this.addActionError(getText("ECM00009",
//					new String[] { getText("PCM00002") }));
//			isCorrect = false;
//		}
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
	
	
	/**
	 * 验证查询条件中是否包含商品信息
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 * 
	 */
	public boolean checkIncludePro()
	{
		boolean inclucde = false;
		String productName = form.getProductName().trim();
		if(productName != null && !"".equals(productName))
		{
			inclucde = true;
		}
		return inclucde;
	}
	
	
	/**
	 * 发票类型批量修改
	 * 
	 * @return String 
	 */
	public String invoiceFlagRevise() {
		try {
			
			//得到页面传来数据
			List<String> saleRecordIdList = form.getSaleRecordId();
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			for(String str : saleRecordIdList){
				Map<String, Object> listMap = new HashMap<String, Object>();
				listMap.put("saleRecordId", str);
				listMap.put("invoiceFlag", form.getInvoiceFlag());
				list.add(listMap);
			} 
			//发票类型修改
			binOLPTRPS43_BL.updateInvoiceFlag(list);
			return SUCCESS;
			
		}catch (Exception e) {	
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	public String getDisSaleID() {
		return disSaleID;
	}

	public void setDisSaleID(String disSaleID) {
		this.disSaleID = disSaleID;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
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

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public List<Map<String, Object>> getSaleRecordList() {
		return saleRecordList;
	}

	public void setSaleRecordList(List<Map<String, Object>> saleRecordList) {
		this.saleRecordList = saleRecordList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}
	
	public Map<String, Object> getSaleSumInfo() {
		return saleSumInfo;
	}

	public void setSaleSumInfo(Map<String, Object> saleSumInfo) {
		this.saleSumInfo = saleSumInfo;
	}
	
	public Map<String, Object> getSumInfoBySaleDateArea() {
		return sumInfoBySaleDateArea;
	}

	public void setSumInfoBySaleDateArea(Map<String, Object> sumInfoBySaleDateArea) {
		this.sumInfoBySaleDateArea = sumInfoBySaleDateArea;
	}
	
	public List<Map<String, Object>> getSaleProPrmDetailList() {
		return saleProPrmDetailList;
	}

	public void setSaleProPrmDetailList(
			List<Map<String, Object>> saleProPrmDetailList) {
		this.saleProPrmDetailList = saleProPrmDetailList;
	}
	
	public List<Map<String, Object>> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<Map<String, Object>> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public int getSelectMode() {
		return selectMode;
	}

	public void setSelectMode(int selectMode) {
		this.selectMode = selectMode;
	}

	@Override
	public BINOLPTRPS43_Form getModel() {
		return form;
	}
}
		
