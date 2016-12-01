/*	
 * @(#)BINOLBSCNT06_Action.java     1.0 2011/05/09		
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
package com.cherry.bs.cnt.action;

import com.cherry.bs.cnt.bl.BINOLBSCNT07_BL;
import com.cherry.bs.cnt.bl.BINOLBSCNT08_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT07_Form;
import com.cherry.bs.cnt.form.BINOLBSCNT08_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 	导入经销商额度变更Action
 * 
 * @author chenkuan
 * @version 1.0 2016.11.23
 */
public class BINOLBSCNT08_Action extends BaseAction implements ModelDriven<BINOLBSCNT08_Form>{


	//打印异常日志
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT08_Action.class);

	/** 导入经销商额度变更Form */
	private BINOLBSCNT08_Form form = new BINOLBSCNT08_Form();

	/** 导入经销商额度变更BL */
	@Resource
	private BINOLBSCNT08_BL binOLBSCNT08_BL;

	/** 共通BL */
	@Resource
	private BINOLCM00_BL binolcm00BL;

	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	/** Excel输入流 */
	private InputStream excelStream;


	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** 积分计划柜台List */
	private List<Map<String, Object>> counterPointPlanList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 上传的文件 */
	private File upExcel;

	/**
	 *
	 * 画面初期显示
	 *
	 * @return String 导入经销商额度变更
	 *
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
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
			// 导入原因
			map.put("comment",form.getComment());
			msgMap.put("successMsg", getText("ICM00002"));
			// 经销商额度变更导入处理
			List<Map<String, Object>> resultList = binOLBSCNT08_BL.ResolveExcel(map);
			binOLBSCNT08_BL.tran_excelHandle(resultList);

			List<String> counterList = new ArrayList<String>();
				for(Map<String, Object> counter:resultList){
					if(!counterList.contains(ConvertUtil.getString(counter.get("counterCode")))){
						counterList.add(ConvertUtil.getString(counter.get("counterCode")));
					}else{//导入的时间，就是当前系统时间
						msgMap.put("successMsg", getText("ACT000113"));
					}
				}


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
	 * @param
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
		map.put("createdBy", map.get(CherryConstants.USERID));
		map.put("createPGM", "BINOLBSCNT08");
		map.put("updatedBy", map.get(CherryConstants.USERID));
		map.put("updatePGM", "BINOLBSCNT08");
		return map;
	}


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
	@Override
	public BINOLBSCNT08_Form getModel() {
		return form;
	}


}