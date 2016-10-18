/*  
 * @(#)BINOLSSPRM66_Action.java     1.0 2013/09/17      
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.ss.prm.form.BINOLSSPRM66_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM66_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS09_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 入库导入Action
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM66_Action extends BaseAction implements ModelDriven<BINOLSSPRM66_Form> {
	
	private static final long serialVersionUID = 3626901483315250462L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM66_Action.class);
	
	private BINOLSSPRM66_Form form = new BINOLSSPRM66_Form();
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLSSPRM66_BL")
	private BINOLSSPRM66_IF binOLSSPRM66_BL;
	
	@Resource(name="binOLSTIOS09_BL")
	private BINOLSTIOS09_IF binOLSTIOS09_BL;
	
	/** 品牌List*/
	@SuppressWarnings("rawtypes")
	private List brandInfoList;
	/** 导入批次List*/
	@SuppressWarnings("rawtypes")
	private List prmInDepotExcelBatchList;
	/** 上传的文件 */
	private File upExcel;
	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	/** 产品品牌ID */
	private int brandInfoId;
	
	/**
	 * 初始化画面
	 * @return
	 */
	public String init() {
		return SUCCESS;
	}
	
	/**
	 * 导入批次查询
	 * @return
	 */
	public String search() {
		try {
			Map<String, Object> map = getSessionInfo();
			map.put("importStartTime", form.getImportStartTime());
			map.put("importEndTime", form.getImportEndTime());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put("type", "MR");
			map.put("validFlag", "1");
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTIOS09_BL.getImportBatchCount(map);
			if(count != 0){
				prmInDepotExcelBatchList = binOLSTIOS09_BL.getImportBatchList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 导入初始画面
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String importInit() {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		return SUCCESS;
	}	
	
	/**
	 * 入库Excel导入
	 * @return
	 * @throws Exception 
	 */
	public String importPrmInDepot() throws Exception {
		// 登录用户参数MAP
		Map<String, Object> sessionMap = getSessionInfo();
		try {
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			Map<String, Object> importDataMap = binOLSSPRM66_BL.resolveExcel(sessionMap);
			Map<String, Object> resultMap=binOLSSPRM66_BL.tran_excelHandle(importDataMap, sessionMap);
			//导入结果，生成返回信息
			String totalCount = ConvertUtil.getString(resultMap.get("totalCount"));
			String successCount = ConvertUtil.getString(resultMap.get("successCount"));
			String errorCount = ConvertUtil.getString(resultMap.get("errorCount"));
			String currentImportBatchCode =  ConvertUtil.getString(sessionMap.get("currentImportBatchCode"));
			if(CherryChecker.isNullOrEmpty(errorCount, true) || "0".equals(errorCount)){
				this.addActionMessage(getText("SSM00028",new String[]{currentImportBatchCode,totalCount,successCount,errorCount}));
			}else{
				this.addActionError(getText("SSM00028",new String[]{currentImportBatchCode,totalCount,successCount,errorCount}));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;       
				this.addActionError(temp.getErrMessage());
			}else{
				this.addActionError(getText("EMO00079"));
			}
		}	
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 验证导入条件
	 */
	public void validateImportPrmInDepot() {
		String isChecked = form.getIsChecked();
		String importBatchCode = form.getImportBatchCode();
		if(CherryChecker.isNullOrEmpty(isChecked, true)){
			//若从画面输入
			if(!CherryChecker.isAlphanumeric(importBatchCode)){
				//数据格式校验
				this.addActionError(getText("ECM00031",new String[]{PropertiesUtil.getText("STM00012")}));
			}else if(importBatchCode.length() > 25){
				//长度校验
				this.addActionError(getText("ECM00020",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
			}else {
				//重复校验
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				map.put("importBatchCodeR", importBatchCode);
				map.put("type", "MR");
				if(binOLSTIOS09_BL.getImportBatchCount(map) > 0){
					this.addActionError(getText("ECM00032",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(form.getComments(), true)){
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("SSM00013")}));
		}
	} 
	
	/**
	 * 获取共通map
	 * @return
	 */
	private Map<String, Object> getSessionInfo(){
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoId.equals("-9999")){
			map.put("brandInfoId", brandInfoId);
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		map.put("language", userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		map.put("BIN_BrandInfoID", brandInfoId);
		map.put("Comments", form.getComments());
		map.put("ImportBatchCode", form.getImportBatchCode());
		map.put("BIN_ImportBatchID", form.getImportBatchId());
		map.put("CreatedBy", map.get(CherryConstants.USERID));
		map.put("CreatePGM", "BINOLSSPRM66");
		map.put("UpdatedBy", map.get(CherryConstants.USERID));
		map.put("UpdatePGM", "BINOLSSPRM66");
		map.put("Type", "MR");
		map.put("importRepeat", form.getImportRepeat());
		map.put("isChecked", form.getIsChecked());
		map.put("type", "MR");
		return map;
	}
	
	@Override
	public BINOLSSPRM66_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public List getBrandInfoList() {
		return brandInfoList;
	}

	@SuppressWarnings("rawtypes")
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@SuppressWarnings("rawtypes")
	public List getPrmInDepotExcelBatchList() {
		return prmInDepotExcelBatchList;
	}

	@SuppressWarnings("rawtypes")
	public void setPrmInDepotExcelBatchList(List prmInDepotExcelBatchList) {
		this.prmInDepotExcelBatchList = prmInDepotExcelBatchList;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

  
}