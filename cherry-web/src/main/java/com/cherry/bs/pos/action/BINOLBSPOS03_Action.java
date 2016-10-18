/*
 * @(#)BINOLBSPOS03_Action.java     1.0 2010/10/27
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

package com.cherry.bs.pos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS02_BL;
import com.cherry.bs.pos.bl.BINOLBSPOS03_BL;
import com.cherry.bs.pos.bl.BINOLBSPOS04_BL;
import com.cherry.bs.pos.form.BINOLBSPOS03_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 更新岗位画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS03_Action extends BaseAction implements ModelDriven<BINOLBSPOS03_Form> {
	
	private static final long serialVersionUID = 4546043960548058464L;
	
	/** 更新岗位画面BL */
	@Resource
	private BINOLBSPOS03_BL binOLBSPOS03_BL;
	
	/** 岗位详细画面BL */
	@Resource
	private BINOLBSPOS02_BL binOLBSPOS02_BL;
	
	/** 添加岗位画面BL */
	@Resource
	private BINOLBSPOS04_BL binOLBSPOS04_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM00_BL binolcm00BL;
	
	/** 更新岗位画面Form */
	private BINOLBSPOS03_Form form = new BINOLBSPOS03_Form();

	@Override
	public BINOLBSPOS03_Form getModel() {
		return form;
	}
	
	/**
	 * 
	 * 更新岗位画面初期处理
	 * 
	 * @return 更新岗位画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 岗位ID
		map.put(CherryConstants.POSITIONID, form.getPositionId());
		// 查询岗位信息
		positionInfo = binOLBSPOS02_BL.getPositionInfo(map);
		// 所属部门
		map.put(CherryConstants.ORGANIZATIONID, positionInfo.get(CherryConstants.ORGANIZATIONID));
		map.put("positionPath", positionInfo.get("positionPath"));
		if(CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			// 取得上级岗位信息List
			higherPositionList = binOLBSPOS04_BL.getPositionByOrg(map);
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, positionInfo.get(CherryConstants.BRANDINFOID));
			// 取得上级岗位信息List
			higherPositionList = binOLBSPOS04_BL.getPositionByOrg(map);
		}
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, positionInfo.get(CherryConstants.BRANDINFOID));
		// 取得岗位类别信息List
		positionCategoryList = binOLBSPOS04_BL.getPositionCategoryList(map);
		// 取得经销商List
		resellerInfoList = binolcm00BL.getResellerInfoList(map);
		return SUCCESS;
	}
	
	/**
	 * 
	 * 更新岗位信息处理
	 * 
	 * @return 更新完了画面
	 */
	public String updatePosition() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPOS03");
		try {
			// 更新岗位信息
			binOLBSPOS03_BL.tran_updatePositionInfo(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 更新岗位前字段验证处理
	 * 
	 */
	public void validateUpdatePosition() throws Exception {
		
		// 岗位名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPositionName())) {
			this.addFieldError("positionName", getText("ECM00009",new String[]{getText("PBS00015")}));
		} else {
			// 岗位名称不能超过50位验证
			if(form.getPositionName().length() > 50) {
				this.addFieldError("positionName", getText("ECM00020",new String[]{getText("PBS00015"),"50"}));
			}
		}
		// 岗位类别ID必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPositionCategoryId())) {
			this.addFieldError("positionCategoryId", getText("ECM00009",new String[]{getText("PBS00039")}));
		}
		// 岗位外文名称不能超过50位验证
		if(form.getPositionNameForeign() != null && form.getPositionNameForeign().length() > 50) {
			this.addFieldError("positionNameForeign", getText("ECM00020",new String[]{getText("PBS00016"),"50"}));
		}
		// 岗位描述不能超过200位验证
		if(form.getPositionDESC() != null && form.getPositionDESC().length() > 200) {
			this.addFieldError("positionDESC", getText("ECM00020",new String[]{getText("PBS00017"),"200"}));
		}
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			// 成立日期日期格式验证
			if(!CherryChecker.checkDate(form.getFoundationDate())) {
				this.addFieldError("foundationDate", getText("ECM00022",new String[]{getText("PBS00008")}));
			}
		}
	}
	
	/** 岗位信息 */
	private Map positionInfo;
	
	/** 上级岗位信息List */
	List<Map<String, Object>> higherPositionList;
	
	/** 经销商List */
	private List<Map<String, Object>> resellerInfoList;
	
	/** 部门List */
	private List<Map<String, Object>> orgList;
	
	/** 岗位类别信息List */
	private List<Map<String, Object>> positionCategoryList;

	public Map getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(Map positionInfo) {
		this.positionInfo = positionInfo;
	}

	public List<Map<String, Object>> getHigherPositionList() {
		return higherPositionList;
	}

	public void setHigherPositionList(List<Map<String, Object>> higherPositionList) {
		this.higherPositionList = higherPositionList;
	}

	public List<Map<String, Object>> getResellerInfoList() {
		return resellerInfoList;
	}

	public void setResellerInfoList(List<Map<String, Object>> resellerInfoList) {
		this.resellerInfoList = resellerInfoList;
	}

	public List<Map<String, Object>> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}

	public List<Map<String, Object>> getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(
			List<Map<String, Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}

}
