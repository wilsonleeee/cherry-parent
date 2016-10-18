/*	
 * @(#)BINOLCPACT12_Action.java     1.0 @2014-12-16	
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
package com.cherry.cp.act.action;

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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.form.BINOLCPACT13_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT13_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 活动产品库存一览Action
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT13_Action extends BaseAction implements
		ModelDriven<BINOLCPACT13_Form> {
			
	private static final long serialVersionUID = -4890692601603482123L;
	
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT13_Action.class);

	/** 活动产品库存参数FORM */
	private BINOLCPACT13_Form form = new BINOLCPACT13_Form();

	/** 活动产品库存接口 */
	@Resource(name = "binOLCPACT13_BL")
	private BINOLCPACT13_IF binOLCPACT13_BL;
	
	/** 取得品牌共通 */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** 活动产品库存List */
	private List<Map<String,Object>> campaignStockList;
	
	/*** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 上传的文件 */
	private File upExcel;

	/**
	 * 初始化页面
	 * @return
	 */
	public String init() {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 总部的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
		return SUCCESS;
	}

	/**
	 * 查询
	 * @return
	 * @throws Exception 
	 */
	public void importStock() throws Exception {
		Map<String, Object> map = getCommonMap();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入方式
			map.put("importType",form.getImportType());
			// 导入处理
			List<Map<String, Object>> resultList = binOLCPACT13_BL.resolveExcel(map);
			binOLCPACT13_BL.tran_saveImportCampaignStock(resultList,map);
			
			msgMap.put("suessMsg", getText("ICM00002"));
			msgMap.put("totalCount", resultList.size());
		} catch (CherryException e) {
			logger.error(e.getMessage(), e);
			// 导入失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                msgMap.put("errorMsg", temp.getErrMessage());
            }else{
            	msgMap.put("errorMsg", getText("ECM00005"));
            }
		}
		ConvertUtil.setResponseByAjax(response, msgMap);
		
	}
	
	/**
	 * 取得查询参数
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getCommonMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 作成者
        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
        // 作成程序名
        map.put(CherryConstants.CREATEPGM, "BINOLCPACT13");
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        // 更新程序名
        map.put(CherryConstants.UPDATEPGM, "BINOLCPACT13");
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}

	@Override
	public BINOLCPACT13_Form getModel() {
		return form;
	}

	public List<Map<String,Object>> getCampaignStockList() {
		return campaignStockList;
	}

	public void setCampaignStockList(List<Map<String,Object>> campaignStockList) {
		this.campaignStockList = campaignStockList;
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
	
}
