/*
 * @(#)BINOLMBPTM05_Action.java     1.0 2013/05/23
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mb.ptm.bl.BINOLMBPTM05_BL;
import com.cherry.mb.ptm.form.BINOLMBPTM05_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分Excel导入Action
 * 
 * @author LUOHONG
 * @version 1.0 2013/05/23
 */
public class BINOLMBPTM05_Action extends BaseAction implements
		ModelDriven<BINOLMBPTM05_Form> {

	private static final long serialVersionUID = 7818038651340156457L;
	
	/** 积分Excel导入Form */
	private BINOLMBPTM05_Form form = new BINOLMBPTM05_Form();
	
	/** 积分Excel导入BL */
	@Resource(name = "binOLMBPTM05_BL")
	private BINOLMBPTM05_BL binOLMBPTM05_BL;

	/** 取得品牌共通 */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBPTM05_Action.class);

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 上传的文件 */
	private File upExcel;


	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	
	private String clubMod;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	/**
	 * 积分Excel导入画面初期处理
	 * 
	 * @return 积分Excel导入信息画面
	 */
	public String importInit() throws Exception {
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
			int brandId = userInfo.getBIN_BrandInfoID();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, brandId);
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
			clubMod =  binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
					.getBIN_OrganizationInfoID()), String.valueOf(brandId));
			if (!"3".equals(clubMod)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, brandId);
				// 语言类型
				map.put(CherryConstants.SESSION_LANGUAGE, session
						.get(CherryConstants.SESSION_LANGUAGE));
				clubList = binOLCM05_BL.getClubList(map);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 积分Excel导入
	 * @return
	 * @throws Exception
	 */
	public void importPoint() throws Exception{
		Map<String, Object> map = getSessionInfo();
		Map<String, Object> msgMap = getSessionInfo();
		try {
			// 上传的文件
			map.put("upExcel", upExcel);
			// 积分类型
			map.put("pointType",form.getPointType());
			// 导入类型
			map.put("importType",form.getImportType());
			// 导入原因
			map.put("reason",form.getReason());
			// 导入批次名称
			map.put("importName",form.getImportName());
			// 会员俱乐部ID
			map.put("memberClubId",form.getMemberClubId());
			// 积分导入处理
			List<Map<String, Object>> resultList = binOLMBPTM05_BL.ResolveExcel(map);
			Map<String, Object> infoMap = binOLMBPTM05_BL.tran_excelHandle(resultList,map);
			 //发送Mq
			binOLMBPTM05_BL.sendMqAll(infoMap);
			msgMap.put("suessMsg", getText("ICM00002"));
			msgMap.put("totalCount", resultList.size());
			msgMap.put("billNo", infoMap.get("pointBillNo"));
			msgMap.put("memPointImportId", infoMap.get("memPointImportId"));
		} catch (CherryException e) {
			logger.error(e.getMessage(), e);
			// 导入失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                msgMap.put("errorMsg", temp.getErrMessage());
            }else{
            	msgMap.put("errorMsg", getText("PTM00024"));
            }
		}
		ConvertUtil.setResponseByAjax(response, msgMap);
	}

	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	private Map<String, Object> getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoId", brandInfoID);
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		//员工ID
		map.put("employeeId",userInfo.getBIN_EmployeeID());
		//员工code
		map.put("employeeCode",userInfo.getEmployeeCode());
		// 组织code
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		map.put("CreatedBy", map.get(CherryConstants.USERID));
		map.put("CreatePGM", "BINOLMBPTM05");
		map.put("UpdatedBy", map.get(CherryConstants.USERID));
		map.put("UpdatePGM", "BINOLMBPTM05");
		return map;
	}	
	/**
	 * 验证导入条件
	 * @return
	 * @throws Exception
	 */
	public void validateImportPoint() throws Exception{
		//积分类型不能为空
		if (CherryChecker.isNullOrEmpty(form.getPointType())) {
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("PTM00018")}));
		}else if("0".equals(form.getPointType())){
			if(CherryChecker.isNullOrEmpty(form.getImportType())){
			//导入类型不能为空
			this.addActionError(getText("ECM00054",new String[]{PropertiesUtil.getText("PTM00019")}));
			}
		}
		//导入原因不能为空
		if (CherryChecker.isNullOrEmpty(form.getReason())) {
			//导入类型不能为空
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("PTM00020")}));
		}
		//导入名称不能为空
		if (CherryChecker.isNullOrEmpty(form.getImportName())) {
			//导入名称不能为空
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("PTM00022")}));
		}
	}
	@Override
	public BINOLMBPTM05_Form getModel() {
		return form;
	}
	
}
