/*
 * @(#)BINOLPTRPS01_Action.java     1.0 2012/12/4
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.cherry.pt.rps.form.BINOLPTRPS16_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS16_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点查询Action
 * 
 * 
 * 
 * @author liuminghao
 * @version 1.0 2012.12.04
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS16_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS16_Form> {

	private static final long serialVersionUID = -868604556458448037L;

	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS16_Action.class);
	/** 参数FORM */
	private BINOLPTRPS16_Form form = new BINOLPTRPS16_Form();

	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

    @Resource(name = "binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLPTRPS16_BL")
	private BINOLPTRPS16_IF binOLPTRPS16_BL;

	/** 取得产品分类List */
	@Resource(name = "binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	/** 节日 */
	private String holidays;

	/** 盘点单List */
	private List takingList;

	/** 汇总信息 */
	private Map<String, Object> sumInfo;

	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 产品分类List */
	private List<Map<String, Object>> cateList;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
	    String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
	    String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoID);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		//品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//产品分类List
		cateList = binOLPTJCS03_IF.getCategoryList(map);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(
				userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		//从配置项取默认合并方式
		String configValue = binOLCM14_BL.getConfigValue("1126", organizationInfoID, brandInfoID);
		form.setCodeMergeType(configValue);
		return SUCCESS;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param
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
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 组织ID
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		//产品ID
		String selPrtVendorIdStr = form.getSelPrtVendorIdArr();
		if(!"null".equalsIgnoreCase(selPrtVendorIdStr) && null != selPrtVendorIdStr && !"".equals(selPrtVendorIdStr)) {
			List selPrtVendorIdList = ConvertUtil.json2List(selPrtVendorIdStr);
			if(null != selPrtVendorIdList && selPrtVendorIdList.size() > 0 && !selPrtVendorIdList.isEmpty()) {
				if(!CherryChecker.isNullOrEmpty(form.getCodeMergeType()) && "Custom".equals(form.getCodeMergeType())) {//选择了自定义合并，则读取产品关联表中的数据
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
					tempMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
					tempMap.put("BIN_MerchandiseType", "N");
					tempMap.put("proVendorIdList", selPrtVendorIdList);
					selPrtVendorIdList = binOLPTRPS16_BL.getConjunctionProList(tempMap);
					
				}
				map.put("prtVendorId", selPrtVendorIdList);
			}
		}
		//是否排除产品标志
		map.put("includeFlag", form.getIncludeFlag());
		//合并方式
		map.put("codeMergeType", form.getCodeMergeType());
		if (form.getParams() != null) {
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil
					.deserialize(form.getParams());
			map.putAll(paramsMap);
		}
		//取得大中小分类查询条件
		String cateInfo = form.getCateInfo();
		if(!"null".equalsIgnoreCase(cateInfo) && null != cateInfo && !"".equals(cateInfo)) {
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(cateInfo);
			if (cateInfoList.size() > 0 && !cateInfoList.isEmpty()) {
				//大分类
				StringBuffer bigCateInfoBuffer=new StringBuffer();
				//中分类
				StringBuffer meduimCateInfoBuffer=new StringBuffer();
				//小分类
				StringBuffer samllCateInfoBuffer=new StringBuffer();
				//分类信息list
				List<String> tempList = new ArrayList<String>();
				for (Map<String, Object> cateInfoMap : cateInfoList) {
					String teminalFlag = cateInfoMap.get("teminalFlag").toString();
					List<String> cateList = (List<String>) cateInfoMap.get("propValArr");
					tempList.addAll(cateList);
					if (cateList.size() != 0) {
						if ("1".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								bigCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("bigCateInfo", bigCateInfoBuffer.substring(0, bigCateInfoBuffer.length() - 1));
						} else if ("2".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								samllCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("samllCateInfo",samllCateInfoBuffer.substring(0,samllCateInfoBuffer.length() - 1));
						} else if ("3".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								meduimCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("mediumCateInfo",meduimCateInfoBuffer.substring(0,meduimCateInfoBuffer.length() - 1));
						} 
					}
				}
			}
		}
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	/**
	 * <p>
	 * AJAX盘点单查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得盘点单总数
		int count = binOLPTRPS16_BL.searchTakingCount(searchMap);

		if (count > 0) {
			// 取得盘点单List
			takingList = binOLPTRPS16_BL.searchTakingList(searchMap);

		}

		sumInfo = binOLPTRPS16_BL.getSumInfo(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS16_1";
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

	/**
	 * 导出Excel
	 * 
	 * @throws JSONException
	 */
	public String export() throws JSONException {
		Map<String, Object> map = getSearchMap();
		// 取得库存信息List
		try {
			exportName = binOLPTRPS16_BL.getExportName(map);
			excelStream = new ByteArrayInputStream(
					binOLPTRPS16_BL.exportExcel(map));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
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

	public List getTakingList() {
		return takingList;
	}

	public void setTakingList(List takingList) {
		this.takingList = takingList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	@Override
	public BINOLPTRPS16_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

}
