/*	
 * @(#)BINOLPTRPS09_Action.java     1.0 2011/03/09		
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
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.form.BINOLPTRPS09_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS09_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 出入库记录查询Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.03.09
 */
public class BINOLPTRPS09_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS09_Form> {

	private static final long serialVersionUID = 5779285107926696629L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS09_Action.class);

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	@Resource(name="binOLPTRPS09_BL")
	private BINOLPTRPS09_IF binolptrps09BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	/** 参数FORM */
	private BINOLPTRPS09_Form form = new BINOLPTRPS09_Form();

	/** 出入库记录List */
	private List<Map<String, Object>> proInOutList;

	/** 汇总信息 */
	private Map<String, Object> sumInfo;

	/** 假日信息 */
	private String holidays;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	public List<Map<String, Object>> getProInOutList() {
		return proInOutList;
	}

	public void setProInOutList(List<Map<String, Object>> proInOutList) {
		this.proInOutList = proInOutList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
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

	@Override
	public BINOLPTRPS09_Form getModel() {
		return form;
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
		// 所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form
				.setEndDate(CherryUtil
						.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 出入库记录查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return
	 * 
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得出入库记录总数
		int count = binolptrps09BL.getProInOutCount(searchMap);
		// 取得出入库记录List
		proInOutList = binolptrps09BL.getProInOutList(searchMap);
		// ================= LuoHong修改：显示统计信息 ================== //
		// 产品厂商ID
//		String prtVendorId = ConvertUtil.getString(searchMap
//				.get(ProductConstants.PRT_VENDORID));
//		if (!CherryConstants.BLANK.equals(prtVendorId)
//				|| !CherryChecker.isNullOrEmpty(form.getNameTotal(), true)) {
			sumInfo = binolptrps09BL.getSumInfo(searchMap);
//		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);

		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS09_1";
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binolptrps09BL.getProInOutDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 一览excel导出
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "time desc");
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binolptrps09BL.exportExcel(map), extName+".xls"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLPTRPS09_excel";
	}
	
	/**
	 * 一览csv导出
	 * @return
	 * @throws Exception
	 */
	public String exportCsv() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		try {
			// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "time desc");
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
    		
    		int count = binolptrps09BL.getProInOutDetailCount(map);
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		if(count > maxCount) {
    			// 明细数据量大于CSV导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binolptrps09BL.exportCSV(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00094"));
		}
		JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
		return null;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws JSONException 
	 */
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
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		// 单号
		map.put("tradeNo", form.getTradeNo().trim());
		//关联单号
		map.put("relevanceNo", form.getRelevanceNo().trim());
		// 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 业务类型
		map.put("tradeType", form.getTradeType());
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/* 开始日期验证 */
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
}
