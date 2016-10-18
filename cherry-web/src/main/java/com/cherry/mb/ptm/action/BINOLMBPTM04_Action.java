/*
 * @(#)BINOLMBPTM04_Action.java     1.0 2013/06/03
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
package com.cherry.mb.ptm.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.ptm.bl.BINOLMBPTM04_BL;
import com.cherry.mb.ptm.form.BINOLMBPTM04_Form;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分导入结果查询Action
 * 
 * @author LUOHONG
 * @version 1.0 2013/06/03
 */
public class BINOLMBPTM04_Action extends BaseAction implements
		ModelDriven<BINOLMBPTM04_Form> {

	private static final long serialVersionUID = 7818038651340156457L;
	
	/** 积分导入结果查询Form */
	private BINOLMBPTM04_Form form = new BINOLMBPTM04_Form();
	
	/** 积分导入结果查询BL */
	@Resource(name = "binOLMBPTM04_BL")
	private BINOLMBPTM04_BL binOLMBPTM04_BL;
	
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 积分导入结果List */
	private  List<Map<String, Object>> pointList;
	
	/** 积分导入结果List */
	private  List<Map<String, Object>> pointDetailList;
	
	/** 积分导入Id */
	private String memPointImportId;
	
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/** 积分导入Id */
	private String pointImportCode;
	
    /** 导入结果信息 */
    private Map<String, Object> sumInfo;
	
	public List<Map<String, Object>> getPointList() {
		return pointList;
	}
	public void setPointList(List<Map<String, Object>> pointList) {
		this.pointList = pointList;
	}
	
	public List<Map<String, Object>> getPointDetailList() {
		return pointDetailList;
	}
	public void setPointDetailList(List<Map<String, Object>> pointDetailList) {
		this.pointDetailList = pointDetailList;
	}
	
	public String getMemPointImportId() {
		return memPointImportId;
	}
	public void setMemPointImportId(String memPointImportId) {
		this.memPointImportId = memPointImportId;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException{
		return FileUtil.encodeFileName(request,exportName);
	}
	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	
	public String getPointImportCode() {
		return pointImportCode;
	}
	public void setPointImportCode(String pointImportCode) {
		this.pointImportCode = pointImportCode;
	}
	
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}
	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}
	/**
	 * 积分导入画面
	 * 
	 * @return 积分Excel导入信息画面
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	/**
	 * 积分导入查询
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//积分类型
		map.put("pointType", form.getPointType());
		//积分开始时间
		map.put("startTime", form.getStartTime());
		//积分结束时间
		map.put("endTime", form.getEndTime());
		//积分批次名称
		map.put("importName", form.getImportBatchName().trim());
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		// 主题活动数
		int maincount = binOLMBPTM04_BL.getPointCount(map);
		// 主题活动list
		if (maincount > 0) {
			pointList = binOLMBPTM04_BL.getPointList(map);
			form.setITotalDisplayRecords(maincount);
			form.setITotalRecords(maincount);
		}
		return "BINOLMBPTM04_1";
	}
	/**
	 * 积分导入画面
	 * 
	 * @return 积分Excel导入信息画面
	 */
	public String detailInit() throws Exception {
		//积分导入Id
		form.setMemPointId(memPointImportId);
		form.setPointBillNo(pointImportCode);
		//积分导入流水号
		return "BINOLMBPTM04_2";
	}
	/**
	 * 积分导入查询
	 * 
	 * @return
	 */
	public String detailSearch() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//积分主表Id
		map.put("memPointImportId", form.getMemPointId());
		//会员卡号
		map.put("memCode", form.getMemCode());
		//结果区分
		map.put("resultFlag", form.getResultFlag());
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		// 主题活动数
		int detailCount = binOLMBPTM04_BL.getPointDetailCount(map);
		// 主题活动list
		if (detailCount > 0) {
			pointDetailList = binOLMBPTM04_BL.getPointDetailList(map);
			form.setITotalDisplayRecords(detailCount);
			form.setITotalRecords(detailCount);
		}
		//导入结果明细画面
		sumInfo = binOLMBPTM04_BL.getSumInfo(map);
		return "BINOLMBPTM04_3";
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
		String startDate = form.getStartTime();
		// 结束日期
		String endDate = form.getEndTime();
		//开始日期验证
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{PropertiesUtil.getText("PCP00033")}));
				isCorrect = false;
			}
		}
		//结束日期验证
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{PropertiesUtil.getText("PCP00034")}));
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
	@Override
	public BINOLMBPTM04_Form getModel() {
		return form;
	}
	
    /**
	 * 导出明细一览数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getCommonMap();
		String language = ConvertUtil.getString(searchMap
				.get(CherryConstants.SESSION_LANGUAGE));
		//积分主表Id
		searchMap.put("memPointImportId", form.getMemPointId());
		//会员卡号
		searchMap.put("memCode", form.getMemCode().trim());
		//结果区分
		searchMap.put("resultFlag", form.getResultFlag());
		//会员卡号
		searchMap.put("pointBillNo", form.getPointBillNo());
		// 取得积分信息List
		try {
			// 下载文件名称
			String zipName = binOLMOCOM01_BL.getResourceValue("BINOLMBPTM04", language, "binolmbptm04_exportName");
			 // 下载文件名称编码格式转换处理
	    	exportName = zipName+".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLMBPTM04_BL.exportExcel(searchMap), zipName+".xls"));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	/**
	 * 共通参数Map
	 * @return
	 */
    public  Map<String, Object> getCommonMap(){
    	// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
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
		return map;
    	
    }
}
