/*  
 * @(#)BINOLMOCIO15_Action.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.form.BINOLMOCIO15_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO15_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO15_Action extends BaseAction implements
ModelDriven<BINOLMOCIO15_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1344933104990379094L;
	
	@Resource
	private BINOLMOCIO15_IF binOLMOCIO15_IF;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	        
	/** 参数FORM */
	private BINOLMOCIO15_Form form = new BINOLMOCIO15_Form();

	private List<Map<String, Object>> brandInfoList;
	
	private List<Map<String, Object>> rivalList;
	
	public List<Map<String, Object>> getRivalList() {
		return rivalList;
	}

	public void setRivalList(List<Map<String, Object>> rivalList) {
		this.rivalList = rivalList;
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
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
//			Map<String, Object> brandMap = new HashMap<String, Object>();
//			// 品牌ID
//			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
//			// 品牌名称
//			brandMap.put("brandName", getText("PPL00006"));
//			if (null != brandInfoList && !brandInfoList.isEmpty()) {
//				brandInfoList.add(0, brandMap);
//			} else {
//				brandInfoList = new ArrayList<Map<String, Object>>();
//				brandInfoList.add(brandMap);
//			}
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		return SUCCESS;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
        // form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));	
		//竞争对手名称
        map.put("rivalNameCN", form.getRivalNameCN().trim());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		return map;
	}
	
	/**
	 * <p>
	 *竞争对手一览
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
	        // 取得参数MAP
	        Map<String, Object> searchMap = getSearchMap();
	        // 取得柜台消息总数
	        int count = binOLMOCIO15_IF.searchRivalCount(searchMap);
	        if (count > 0) {
	            // 取得柜台消息List
	        	rivalList = binOLMOCIO15_IF.searchRivalList(searchMap);
	        }
	        // form表单设置
	        form.setITotalDisplayRecords(count);
	        form.setITotalRecords(count);
	        // AJAX返回至dataTable结果页面
	        return "BINOLMOCIO15_1";
	}
	
	/**
	 * <p>
	 * 新增竞争对手
	 * </p>
	 * 
	 * @return
	 */
	public String addRival() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			if (form.getAddbrandInfoId() == null) {
				map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			} else {
				map.put("brandInfoId", form.getAddbrandInfoId());
			}
			// 竞争对手名称
			map.put("rivalNameCN", form.getAddRivalNameCN().trim());
			// 竞争对手名称
			map.put("rivalNameEN", form.getAddRivalNameEN().trim());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMOCIO15");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO15");
			binOLMOCIO15_IF.tran_addRival(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 编辑竞争对手信息
	 * @return
	 * @throws Exception
	 */
	public String editRival() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			if (form.getEditBrandInfoId() == null) {
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getEditBrandInfoId());
			}
			// 竞争对手ID
			map.put("rivalId", form.getRivalId());
			// 竞争对手名称
			map.put("rivalNameCN", form.getEditRivalNameCN().trim());
			// 竞争对手名称
			map.put("rivalNameEN", form.getEditRivalNameEN().trim());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO15");
			binOLMOCIO15_IF.tran_updateRival(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 逻辑删除竞争对手
	 * @return
	 * @throws Exception
	 */
	public String deleteRival() throws Exception {
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 竞争对手ID
			map.put("rivalId", form.getRivalId());
			 // 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMOCIO15");
			binOLMOCIO15_IF.tran_deleteRival(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 校验竞争对手字段
	 * 
	 * @throws Exception
	 */
	public void validateEditRival() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		if (form.getEditBrandInfoId() == null) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getEditBrandInfoId());
		}
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		map.put("rivalId", form.getRivalId());
		map.put("rivalNameCN", form.getEditRivalNameCN().trim());
		String count = binOLMOCIO15_IF.getCount(map);
		if (count.equals("1")) {
			// 竞争对手已存在
			this.addFieldError("editRivalNameCN",
					getText("ECM00032", new String[] { getText("PBS00058") }));
		}
	}
	
	/**
	 * 新增竞争对手字段校验
	 * @throws Exception
	 */
	public void validateAddRival() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
		.get(CherryConstants.SESSION_USERINFO);
		if(form.getAddbrandInfoId() == null){
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		else{
			map.put("brandInfoId", form.getAddbrandInfoId());
		}
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		map.put("rivalNameCN", form.getAddRivalNameCN().trim());
        String count=binOLMOCIO15_IF.getCount(map);
		if(count.equals("1")) {
			this.addFieldError("addRivalNameCN",getText("ECM00032",new String[]{getText("PBS00058"),"20"}));
		}
	}
	
	@Override
	public BINOLMOCIO15_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
