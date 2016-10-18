/*
 * @(#)BINOLSSPRM25_Action.java     1.0 2010/10/27
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.bl.BINOLSSCM02_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM25_BL;
import com.cherry.ss.prm.form.BINOLSSPRM25_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点查询Action
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM25_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM25_Form> {

	private static final long serialVersionUID = 5434544795840258311L;

	@Resource
	private BINOLCM00_BL binolcm00BL;
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource
	private BINOLSSPRM25_BL binolssprm25BL;

	@Resource(name="binOLSSCM02_BL")
	private BINOLSSCM02_BL binolsscm02BL;
	
	/** 参数FORM */
	private BINOLSSPRM25_Form form = new BINOLSSPRM25_Form();
	
	/** 盘点单List */
	private List takingList;

	/** 节日 */
	private String holidays;

    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    

	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	public BINOLSSPRM25_Form getModel() {

		return form;
	}

	public List getTakingList() {
		return takingList;
	}

	public void setTakingList(List takingList) {
		this.takingList = takingList;
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
		form.setOrganizationId(organizationInfoID);
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		//取得大分类列表
    	form.setLargeCategoryList(binolsscm02BL.getPrimaryCategory(userInfo));
		return SUCCESS;
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
		
		form.setProfitKbn(form.getProfitKbn());
		
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得盘点单总数
		int count = binolssprm25BL.searchTakingCount(searchMap);
		if (count > 0) {
			// 取得盘点单List
			takingList = binolssprm25BL.searchTakingList(searchMap);
		}
//      ================= LuoHong修改：显示统计信息 ================== //
//            String prmVendorId = ConvertUtil.getString(searchMap.get("prmVendorId"));
//            if (!CherryConstants.BLANK.equals(prmVendorId)) {
                sumInfo = binolssprm25BL.getSumInfo(searchMap);
//            }
//		}else{
//            String prmVendorId = ConvertUtil.getString(searchMap.get("prmVendorId"));
//            if (!CherryConstants.BLANK.equals(prmVendorId)) {
//                sumInfo = new HashMap<String,Object>();
//                sumInfo.put("sumQuantity", 0);
//                sumInfo.put("sumAmount", 0);
//            }
//		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM25_1";
	}
	
	/**
	 * 导出一览数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String exportTakingInfo() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得库存信息List
		try {
			exportName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM25",
					language, "PRM25_exportName")
					+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
			excelStream = new ByteArrayInputStream(binolssprm25BL
					.exportTakingInfo(map));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binolssprm25BL.getTakingStockCount(map);
		if(count <= 0) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00099"));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 导出一览明细数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得库存信息List
		try {
			exportName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM26",
					language, "PRM26_exportName")
					+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
			excelStream = new ByteArrayInputStream(binolssprm25BL
					.exportExcel(map));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 盘点单号
		map.put("stockTakingNo", form.getStockTakingNo());
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 盈亏
		map.put("profitKbn", form.getProfitKbn());
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		//促销产品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		// 促销产品名称
		map.put(CherryConstants.NAMETOTAL,form.getPromotionProductName());
		//大中小分类
		map.put("PrimaryCategoryCode", form.getLargeCategory());
		map.put("SecondryCategoryCode", form.getMiddleCategory());
		map.put("SmallCategoryCode", form.getSmallCategory());
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
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
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
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

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}