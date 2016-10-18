/*  
 * @(#)BINOLPTUNQ02_Action    1.0 2016-06-06     
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

package com.cherry.pt.unq.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.unq.form.BINOLPTUNQ02_Form;
import com.cherry.pt.unq.interfaces.BINOLPTUNQ02_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 唯一码管理 单码查询Action
 * 
 * @author zw
 * @version 1.0 2016.06.06
 */
public class BINOLPTUNQ02_Action extends BaseAction implements ModelDriven<BINOLPTUNQ02_Form> {

	private static final long serialVersionUID = 6425150136531877774L;

	/** 参数FORM */
	private BINOLPTUNQ02_Form form = new BINOLPTUNQ02_Form();
	
	/** 接口 */
	@Resource
	private BINOLPTUNQ02_IF binOLPTUNQ02_BL;
	
	@Override
	public BINOLPTUNQ02_Form getModel() {
		return form;
	}
	
	/** 单码查询List */
	private List<Map<String, Object>> singleCodeSearchList;

	/**
	 * 单码查询页面初始化
	 * 
	 * @return 
	 * @throws Exception 
	 */
	public String init() throws Exception{
	
			return SUCCESS;
	}
	
	/**
	 * 单码查询页面初始化查询（开始时页面显示0条数据）
	 * 
	 * @return 
	 * @throws Exception 
	 */
	public String initSearch() throws Exception{
		
        Map<String, Object> map = getSearchMap();
        map.put("pointUniqueCodeSrh", "1");
		// 获取单码查询结果总条数
		int count = binOLPTUNQ02_BL.getSingleCodeSearchCount(map);
		// 获取单码查询结果列表
		singleCodeSearchList = binOLPTUNQ02_BL.getSingleCodeList(map);
		if (count > 0) {
			form.setUnqGenerateList(singleCodeSearchList);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
	
		return SUCCESS;
	}
	
	/**
	 * 单码查询
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String search() throws Exception {	
		
		Map<String, Object> map = getSearchMap();
		
		// 获取单码查询结果总条数
		int count = binOLPTUNQ02_BL.getSingleCodeSearchCount(map);
		// 获取单码查询结果列表
		singleCodeSearchList = binOLPTUNQ02_BL.getSingleCodeList(map);
		if (count > 0) {
			form.setUnqGenerateList(singleCodeSearchList);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
        return SUCCESS;
	}
	
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_EmployeeID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTUNQ02");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTUNQ02");
		
		return map;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return string  
	 * @throws Exception
	 * 
	 */
	public String searchCheck() throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 积分唯一码、箱码、关联唯一码不能全部为空
		if (CherryChecker.isNullOrEmpty(form.getPointUniqueCodeSrh()) && CherryChecker.isNullOrEmpty(form.getRelUniqueCodeSrh()) && CherryChecker.isNullOrEmpty(form.getBoxCodeSrh()) ) {
			this.addFieldError("pointUniqueCodeSrh", getText("ECM00009", new String[] {"积分唯一码和关联唯一码"}));
			resultMap.put("errorCode", "0");
			ConvertUtil.setResponseByAjax(response, resultMap);
			return CherryConstants.GLOBAL_ACCTION_RESULT;
			
		}else{
			resultMap.put("errorCode", "1");
			ConvertUtil.setResponseByAjax(response, resultMap);
			return null;
		}
		
	}

	public List<Map<String, Object>> getSingleCodeSearchList() {
		return singleCodeSearchList;
	}

	public void setSingleCodeSearchList(
			List<Map<String, Object>> singleCodeSearchList) {
		this.singleCodeSearchList = singleCodeSearchList;
	}
	

}
