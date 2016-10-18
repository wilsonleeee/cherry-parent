/*  
 * @(#)BINOLPTRPS13_Action.java     1.0 2011/05/31      
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.rps.form.BINOLPTRPS42_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS42_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 *
 * 预付单查询Action
 *
 * @author nanjunbo
 *
 * @version  2015-07-15
 */
public class BINOLPTRPS42_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS42_Form> {

	private static final long serialVersionUID = 1544406606975742871L;

	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS42_Action.class.getName());

	// FROM
	private BINOLPTRPS42_Form form = new BINOLPTRPS42_Form();

	// 注入BL
	@Resource(name="binOLPTRPS42_BL")
	private BINOLPTRPS42_IF binOLPTRPS42_BL;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	/** 导出excel共通BL **/

	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// 预付单记录LIST
	private List<Map<String, Object>> preInfoList;

	// 预付单记录总数
	private int count = 0;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	/**  预付单统计 */
	private Map<String, Object> sumInfo;
	
	/**  预付单详情明细 */
	private Map<String, Object> preInfoMap;
	
	/**  提货单详情明细 */
	private Map<String, Object> pickUpMap;
	
	/** 预付单详情list */
	private List<Map<String, Object>> preDetailInfoList;
	
	/** 提货单单详情list */
	private List<Map<String, Object>> pickDetailInfoList;
	
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
		
		// 开始日期
		form.setPrePayStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setPrePayEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
		return SUCCESS;
	}
	
	/**
	 * 详情界面
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prePayBillMainId", form.getPrePayBillMainId());
		preInfoMap = binOLPTRPS42_BL.getpreInfoMap(map);
		//得到预付单明细
		preDetailInfoList = binOLPTRPS42_BL.getPreDetailInfoList(map);
		//得到提货单明细
		pickDetailInfoList =  binOLPTRPS42_BL.getPickDetailInfoList(map);
		
		int pickupQuantity = 0;
		if (null != pickDetailInfoList && !pickDetailInfoList.isEmpty()){
			for(Map<String, Object> pickDetailInfo: pickDetailInfoList){
				pickupQuantity +=  ConvertUtil.getInt(pickDetailInfo.get("quantity"));
			}
//			pickupQuantity = ConvertUtil.getInt(pickDetailInfoList.get(0).get("pickupQuantity"));
		}
		pickUpMap = new HashMap<String, Object>();
		pickUpMap.put("pickupQuantity", pickupQuantity);
		
		payTypeList = binOLPTRPS42_BL.getPayTypeList(map);
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binOLPTRPS42_BL.getExportDetailCount(map);
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
		map.put("SORT_ID", "prePayNo desc");
		// 取得销售记录信息List
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS42", language, "RPS42_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTRPS42_BL.exportExcel(map), extName+".xls"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLPTRPS42_excel";
	}
    
	
	@SuppressWarnings("unchecked")
	public String search() throws JSONException{
		
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();

		
		//获取预付单合计总数
		sumInfo = binOLPTRPS42_BL.getSumPreInfo(searchMap);
		
		// 获取预付单总数
		count = binOLPTRPS42_BL.getPreInfoCount(searchMap);

		// 获取预付单明细LIST
		preInfoList = binOLPTRPS42_BL.getPreInfoList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS42_1";
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
				
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		        
		//预付单编号
		map.put("prePayNo", form.getPrePayNo().trim());
		// 预付时间开始日
		map.put("prePayStartDate", form.getPrePayStartDate());
		// 预付时间结束日
		map.put("prePayEndDate", form.getPrePayEndDate());
		//预付金额起始
		map.put("prePayAmountStart", form.getPrePayAmountStart());
		//预付金额结束
		map.put("prePayAmountEnd", form.getPrePayAmountEnd());
		//预留手机号
		map.put("phoneNo",form.getPhoneNo());
		//下次提货起始时间
		map.put("pickUpStartDate", form.getPickUpStartDate());
		//下次提货截止时间
		map.put("pickUpEndDate", form.getPickUpEndDate());		
		// 包含无下次提货时间（1：包含）
		map.put("includeNoPickUpTime", form.getIncludeNoPickUpTime());
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		// map参数trim处理
		CherryUtil.trimMap(map);
				
		
		return map;
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
		// 预付单开始日期
		String prePayStartDate = form.getPrePayStartDate();
		// 预付单结束日期
		String prePayEndDate = form.getPrePayEndDate();
		
		/*开始日期验证*/
		if (prePayStartDate != null && !"".equals(prePayStartDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(prePayStartDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (prePayEndDate != null && !"".equals(prePayEndDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(prePayEndDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && prePayStartDate != null && !"".equals(prePayStartDate)&& 
				prePayEndDate != null && !"".equals(prePayEndDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(prePayStartDate, prePayEndDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		
		// 预付单开始日期
		String pickUpStartDate = form.getPickUpStartDate();
		// 预付单结束日期
		String pickUpEndDate = form.getPickUpEndDate();
		
		
		/*开始日期验证*/
		if (pickUpStartDate != null && !"".equals(pickUpStartDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(pickUpStartDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (pickUpEndDate != null && !"".equals(pickUpEndDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(pickUpEndDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && pickUpStartDate != null && !"".equals(pickUpStartDate)&& 
				pickUpEndDate != null && !"".equals(pickUpEndDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(pickUpStartDate, pickUpEndDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		//预留手机号
		String phoneNo = form.getPhoneNo();
		//预留手机号
		String prePayAmountStart = form.getPrePayAmountStart();
		//预留手机号
		String prePayAmountEnd = form.getPrePayAmountEnd();
		
		if (!CherryChecker.isNullOrEmpty(phoneNo)&&!CherryChecker.isNumeric(phoneNo)) {
			this.addActionError(getText("ECM00086"));
			isCorrect = false;
		}

		if (!CherryChecker.isNullOrEmpty(prePayAmountStart)&&!CherryChecker.isDecimal(prePayAmountStart, 8, 3)) {
			this.addActionError(getText("EMO00075"));
			isCorrect = false;
		}
		if (!CherryChecker.isNullOrEmpty(prePayAmountEnd)&&!CherryChecker.isNumeric(prePayAmountEnd)) {
			this.addActionError(getText("EMO00075"));
			isCorrect = false;
		}
		
		if (!CherryChecker.isNullOrEmpty(prePayAmountStart)&&!CherryChecker.isNullOrEmpty(prePayAmountEnd)){
			if (ConvertUtil.getDouble(prePayAmountStart)>ConvertUtil.getDouble(prePayAmountEnd)){
				this.addActionError(getText("EMO00075"));
				isCorrect = false;
			}
		}
		
		return isCorrect;
	}
	
	

	public List<Map<String, Object>> getPreDetailInfoList() {
		return preDetailInfoList;
	}

	public void setPreDetailInfoList(List<Map<String, Object>> preDetailInfoList) {
		this.preDetailInfoList = preDetailInfoList;
	}

	public List<Map<String, Object>> getPickDetailInfoList() {
		return pickDetailInfoList;
	}

	public void setPickDetailInfoList(List<Map<String, Object>> pickDetailInfoList) {
		this.pickDetailInfoList = pickDetailInfoList;
	}

	public Map<String, Object> getPreInfoMap() {
		return preInfoMap;
	}

	public void setPreInfoMap(Map<String, Object> preInfoMap) {
		this.preInfoMap = preInfoMap;
	}

	
	public Map<String, Object> getPickUpMap() {
		return pickUpMap;
	}

	public void setPickUpMap(Map<String, Object> pickUpMap) {
		this.pickUpMap = pickUpMap;
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


	public List<Map<String, Object>> getPreInfoList() {
		return preInfoList;
	}

	public void setPreInfoList(List<Map<String, Object>> preInfoList) {
		this.preInfoList = preInfoList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public List<Map<String, Object>> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<Map<String, Object>> payTypeList) {
		this.payTypeList = payTypeList;
	}

	@Override
	public BINOLPTRPS42_Form getModel() {
		return form;
	}
}
		
