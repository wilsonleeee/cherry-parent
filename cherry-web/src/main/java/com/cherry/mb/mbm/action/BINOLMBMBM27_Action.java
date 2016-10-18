/*
 * @(#)BINOLMBMBM27_Action.java     1.0 2013.09.23
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM27_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM27_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加会员问题画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM27_Action extends BaseAction implements ModelDriven<BINOLMBMBM27_Form> {

	private static final long serialVersionUID = 7729500224995899206L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM27_Action.class);
	
	/** 添加会员问题画面BL **/
	@Resource
	private BINOLMBMBM27_BL binOLMBMBM27_BL;
	
	/**
	 * 添加会员问题画面
	 * 
	 * @return 添加会员问题画面
	 */
	public String init() throws Exception {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String employeeId = String.valueOf(userInfo.getBIN_EmployeeID());
		String employeeName = userInfo.getEmployeeName();
		String employeeCode = userInfo.getEmployeeCode();
		form.setAssignee(employeeId);
		form.setSpeaker(employeeId);
		if(employeeName != null && !"".equals(employeeName)) {
			form.setAssigneeName("("+employeeCode+")"+employeeName);
			form.setSpeakerName("("+employeeCode+")"+employeeName);
		} else {
			form.setAssigneeName(employeeCode);
			form.setSpeakerName(employeeCode);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加会员问题画面
	 * 
	 * @return 添加会员问题画面
	 */
	public String add() throws Exception {
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM27");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM27");
			// 解决人
			map.put("author", userInfo.getBIN_EmployeeID());
			// 处理内容可见权限设置
			map.put("actionRole", userInfo.getBIN_PositionCategoryID());
			if(form.getCampaignType() == null || "".equals(form.getCampaignType())) {
				map.put("customerType", "1");
			}
			// 添加会员问题
			int issueId = binOLMBMBM27_BL.tran_addIssue(map);
			request.setAttribute("issueId", String.valueOf(issueId));
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加会员问题前字段验证处理
	 * 
	 */
	public void validateAdd() throws Exception {
//		// 摘要必须入力验证
//		if(CherryChecker.isNullOrEmpty(form.getIssueSummary())) {
//			this.addFieldError("issueSummary", getText("ECM00009",new String[]{getText("PMB00073")}));
//		} else {
//			// 摘要不能超过100位验证
//			if(form.getIssueSummary().length() > 100) {
//				this.addFieldError("issueSummary", getText("ECM00020",new String[]{getText("PMB00073"),"100"}));
//			}
//		}
		// 摘要不能超过100位验证
		if(form.getIssueSummary() != null && !"".equals(form.getIssueSummary())) {
			// 摘要不能超过100位验证
			if(form.getIssueSummary().length() > 100) {
				this.addFieldError("issueSummary", getText("ECM00020",new String[]{getText("PMB00073"),"100"}));
			}
		}
		// 类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getIssueType())) {
			this.addFieldError("issueType", getText("ECM00009",new String[]{getText("PMB00074")}));
		}
		// 截止日期日期格式验证
		if(form.getDueDate() != null && !"".equals(form.getDueDate())) {
			// 截止日期日期格式验证
			if(!CherryChecker.checkDate(form.getDueDate())) {
				this.addFieldError("dueDate", getText("ECM00022",new String[]{getText("PMB00075")}));
			}
		}
		// 销售单号不能超过35位验证
		if(form.getBillCode() != null && !"".equals(form.getBillCode())) {
			// 销售单号不能超过35位验证
			if(form.getBillCode().length() > 35) {
				this.addFieldError("billCode", getText("ECM00020",new String[]{getText("PMB00078"),"35"}));
			}
		}
		// 关联问题票号不能超过22位验证
		if(form.getReIssueNo() != null && !"".equals(form.getReIssueNo())) {
			// 关联问题票号不能超过22位验证
			if(form.getReIssueNo().length() > 22) {
				this.addFieldError("reIssueNo", getText("ECM00020",new String[]{getText("PMB00079"),"22"}));
			}
		}
		// 描述不能超过1000位验证
		if(form.getDescription() != null && !"".equals(form.getDescription())) {
			// 描述不能超过1000位验证
			if(form.getDescription().length() > 1000) {
				this.addFieldError("description", getText("ECM00020",new String[]{getText("PMB00076"),"1000"}));
			}
		}
		// 解决方案 不能超过1000位验证
		if(form.getActionBody() != null && !"".equals(form.getActionBody())) {
			// 解决方案 不能超过1000位验证
			if(form.getActionBody().length() > 1000) {
				this.addFieldError("actionBody", getText("ECM00020",new String[]{getText("PMB00077"),"1000"}));
			}
		}
	}
	
	/** 添加会员问题画面Form */
	private BINOLMBMBM27_Form form = new BINOLMBMBM27_Form();

	@Override
	public BINOLMBMBM27_Form getModel() {
		return form;
	}

}
