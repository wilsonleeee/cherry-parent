/*
 * @(#)BINOLMBMBM15_Action.java     1.0 2013/04/26
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
package com.cherry.mb.mbm.action;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.mbm.bl.BINOLMBMBM15_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM15_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员发卡柜台变更处理Action
 * 
 * @author WangCT
 * @version 1.0 2013/04/26
 */
public class BINOLMBMBM15_Action extends BaseAction implements ModelDriven<BINOLMBMBM15_Form> {

	private static final long serialVersionUID = -3574601074344434494L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM15_Action.class);
	
	/** 会员发卡柜台变更处理BL **/
	@Resource
	private BINOLMBMBM15_BL binOLMBMBM15_BL;
	
	/**
	 * 会员发卡柜台转柜处理
	 */
	public String moveMemCounter() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织代码
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌代码
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLMBMBM15");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM15");
		// 操作员工
		map.put("modifyEmployee", userInfo.getEmployeeCode());
		
		// 转柜方式为正常转柜的场合
		if("1".equals(form.getSubType())) {
			// 验证原柜台和新柜台不能为空
			if((form.getOldOrgId() == null || "".equals(form.getOldOrgId())) 
					|| (form.getNewOrgId() == null || "".equals(form.getNewOrgId()))) {
				this.addActionError(getText("EMB00019"));
			} else {
				// 验证原柜台和新柜台不能相同
				if(form.getOldOrgId().equals(form.getNewOrgId())) {
					this.addActionError(getText("EMB00020"));
				}
			}
			
			if(!this.hasActionErrors()) {
				try {
					// 会员发卡柜台转柜处理
					binOLMBMBM15_BL.tran_moveMemCounter(map);
					this.addActionMessage(getText("ICM00002"));
				} catch (Exception e) {
					if(e instanceof CherryException){
		                CherryException temp = (CherryException)e;            
		                this.addActionError(temp.getErrMessage());
		            }else{
		            	this.addActionError(getText("ECM00089"));
		            	logger.error(e.getMessage(), e);
		            }
				}
			}
		} else { // 转柜方式为转柜撤销的场合
			// 验证转柜台批次号不能为空
			if(form.getBatchCode() == null || "".equals(form.getBatchCode())) {
				this.addActionError(getText("EMB00024"));
			} else {
				// 根据转柜台批次号查询转柜信息
				memInfoRecordInfo = binOLMBMBM15_BL.getMemInfoRecordInfo(map);
				// 验证转柜台批次号是否存在记录
				if(memInfoRecordInfo != null) {
					return "moveCouConfirm";
				} else {
					this.addActionError(getText("EMB00025"));
				}
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 撤销会员发卡柜台转柜处理
	 */
	public String reMoveMemCounter() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织代码
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌代码
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLMBMBM15");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM15");
		// 操作员工
		map.put("modifyEmployee", userInfo.getEmployeeCode());
		// 备注信息
		map.put("remark", getText("EMB00026"));
		
		try {
			// 撤销会员发卡柜台转柜处理
			binOLMBMBM15_BL.tran_reMoveMemCounter(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00089"));
            	logger.error(e.getMessage(), e);
            }
		}
		
		return SUCCESS;
	}
	
	/** 转柜信息 */
	public Map memInfoRecordInfo;
	
	public Map getMemInfoRecordInfo() {
		return memInfoRecordInfo;
	}

	public void setMemInfoRecordInfo(Map memInfoRecordInfo) {
		this.memInfoRecordInfo = memInfoRecordInfo;
	}

	/** 会员发卡柜台变更处理Form */
	private BINOLMBMBM15_Form form = new BINOLMBMBM15_Form();

	@Override
	public BINOLMBMBM15_Form getModel() {
		return form;
	}

}
