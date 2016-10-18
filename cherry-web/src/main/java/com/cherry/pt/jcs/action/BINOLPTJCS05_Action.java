/*	
 * @(#)BINOLPTJCS05_Action.java     1.0 2010/5/18		
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
package com.cherry.pt.jcs.action;

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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS05_IF;

/**
 * 
 * 产品Excel导入Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.5.18
 */
public class BINOLPTJCS05_Action extends BaseAction {

	/** */
	private static final long serialVersionUID = -5515185790225353101L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS05_Action.class.getName());

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name="binOLPTJCS05_IF")
	private BINOLPTJCS05_IF binolptjcs05IF;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	/** 产品品牌ID */
	private int brandInfoId;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

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

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {

		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			brandInfoId = userInfo.getBIN_BrandInfoID();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 品牌名称
			brandInfo.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 产品批量导入
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importPros() throws Exception {
		// 解析成功后的产品list
		List<Map<String, Object>> list = null;
		try {

			// 用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 解析文件
			list = binolptjcs05IF.parseFile(upExcel, userInfo);

			// 参数MAP
			Map<String, Object> map = getSearchMap();
			map.put(ProductConstants.LIST, list);
			// 批量导入产品
			Map<String, Integer> infoMap = binolptjcs05IF.tran_import(map);
			// 导入成功
			this.addActionMessage(getText("EBS00039", new String[] {
					ConvertUtil.getString(infoMap
							.get(ProductConstants.OPTCOUNT)),
					ConvertUtil.getString(infoMap
							.get(ProductConstants.ADDCOUNT)),
					ConvertUtil.getString(infoMap
							.get(ProductConstants.UPDCOUNT))}));
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			this.addActionError(e.getErrMessage());
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		return map;
	}
}
