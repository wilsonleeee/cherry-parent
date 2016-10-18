/*  
 * @(#)BINOLCM02_Action.java     1.0 2013/01/16      
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
package com.cherry.cm.cmbussiness.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM32_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模块共通Action
 * @author ZhangGS
 *
 */
public class BINOLCM32_Action extends BaseAction implements ModelDriven<BINOLCM32_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5505604461653892758L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM32_Action.class);
	
	/** 参数FORM */
	private BINOLCM32_Form form = new BINOLCM32_Form();
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	/**
	 * 初始化沟通模板弹出框
	 * @return
	 * @throws Exception
	 */
	public String initMsgTemplateDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 取得沟通模板信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popMsgTemplateDialog() throws Exception {
		try{
			Map<String,Object> map = getTemplateMap();
			//取得模板数量
			int count = binOLCM32_BL.getMsgTemplateDialogCount(map);
			if(count > 0){
				List<Map<String, Object>> popMsgTemplateList = binOLCM32_BL.getMsgTemplateDialogList(map);
				// 取得List
				form.setPopMsgTemplateList(popMsgTemplateList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> getTemplateMap() throws Exception{
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	@Override
	public BINOLCM32_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
}
