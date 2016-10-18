/*	
 * @(#)BINOLPLSCF17_Action.java     1.0 2013/08/27	
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
package com.cherry.pl.scf.action;

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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.form.BINOLPLSCF17_Form;
import com.cherry.pl.scf.interfaces.BINOLPLSCF17_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 业务处理器管理画面Action
 * 
 * @author hub
 * @version 1.0 2013/08/27
 */
public class BINOLPLSCF17_Action extends BaseAction implements ModelDriven<BINOLPLSCF17_Form>{
	
	private static final long serialVersionUID = 7380507460143759072L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPLSCF17_Action.class);
	
	/** 业务处理器管理画面Form */
	private BINOLPLSCF17_Form form = new BINOLPLSCF17_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLPLSCF17_IF binolplscf17_IF;

	@Override
	public BINOLPLSCF17_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	/**
	 * <p>
	 * 业务处理器管理画面初期显示
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
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 取得所管辖的品牌List
		List<Map<String, Object>> brandList = null;
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			// 取得所管辖的品牌List
			brandList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 取得品牌名称
			//form.setBrandName(binOLCM05_BL.getBrandName(map));
			// 品牌代码
			//form.setBrandCode(userInfo.getBrandCode());
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌代码
			brandMap.put("brandCode", userInfo.getBrandCode());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandList = new ArrayList<Map<String, Object>>();
			brandList.add(brandMap);
		}
		form.setBrandInfoList(brandList);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 业务处理器查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String search() throws Exception{
		Map<String, Object> baseMap = (Map<String, Object>) Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, baseMap);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织代码
		baseMap.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
		// 品牌代码
		baseMap.put(CherryConstants.BRAND_CODE, form.getBrandCode());
		// 取得业务处理器件数 
		int count = binolplscf17_IF.getHandlerCount(baseMap);
		if(count > 0){
			// 取得业务处理器List
			List<Map<String, Object>> handlerList = binolplscf17_IF.getHandlerList(baseMap);
			// 业务处理器List
			form.setHandlerList(handlerList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 停用/启用处理器
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String handValid() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 有效区分
    	map.put("validFlag", form.getValidFlag());
    	// 配置ID
    	map.put("upInfo", form.getUpInfo());
    	// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
    	// 所属组织代码
    	map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
		// 品牌代码
    	map.put(CherryConstants.BRAND_CODE, form.getBrandCode());
    	try{
			// 停用或者启用处理器
    		binolplscf17_IF.tran_editValid(map);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			// 操作失败
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 操作成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
	 * <p>
	 * 刷新处理器
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 组合规则信息
	 * @throws Exception 
	 * 
	 */
    public void handRefresh() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
    	// 所属组织代码
    	map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
		// 品牌代码
    	map.put(CherryConstants.BRAND_CODE, form.getBrandCode());
    	// 取得有效的规则配置信息
    	Map<String, Object> resultInfo = new HashMap<String, Object>();
    	boolean errFlag = false;
    	try {
			// 发送刷新业务处理器MQ
    		binolplscf17_IF.sendRfHandlerMsg(map);
		} catch (Exception e){
			errFlag = true;
			logger.error(e.getMessage(),e);
		}
    	if (errFlag) {
    		resultInfo.put("RESULT", "NG");
    	} else {
    		resultInfo.put("RESULT", "OK");
    	}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultInfo));
    }
}
