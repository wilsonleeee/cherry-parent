/*
 * @(#)BINOLMBMBM28_Action.java     1.0 2013.09.23
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

import java.util.ArrayList;
import java.util.List;
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
import com.cherry.mb.mbm.bl.BINOLMBMBM28_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM28_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员问题处理画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM28_Action extends BaseAction implements ModelDriven<BINOLMBMBM28_Form> {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM28_Action.class);

	private static final long serialVersionUID = 3964947009649736859L;
	
	/** 会员问题处理画面BL **/
	@Resource
	private BINOLMBMBM28_BL binOLMBMBM28_BL;
	
	/**
	 * 会员问题处理画面
	 * 
	 * @return 会员问题处理画面
	 */
	public String init() throws Exception {
		
		if(form.getBackIssueId() != null && !"".equals(form.getBackIssueId())) {
			form.setIssueId(form.getBackIssueId());
		}
		String issueId = (String)request.getAttribute("issueId");
		if(issueId != null && !"".equals(issueId)) {
			form.setIssueId(issueId);
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String employeeId = String.valueOf(userInfo.getBIN_EmployeeID());
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得会员问题明细
		issueDetailInfo = binOLMBMBM28_BL.getIssueDetail(map);
		form.setCurAuthor(employeeId);
		
		// 以下是返回按钮的处理逻辑：
		// 当前问题票中的描述或者处理意见中存在其他问题票链接的场合，进入内部问题票后会在画面中显示返回到原问题票的返回按钮
		// 通过一个返回队列保存每一个被访问过的问题票ID，如果队列中超过两个问题票ID那么就会设置倒数第二个为返回问题票ID，
		// 当不停的点进内部问题票时返回队列就增加，当点击返回按钮时返回队列就减少
		// 画面点击返回键的场合
		if(form.getBackIssueId() != null && !"".equals(form.getBackIssueId())) {
			// 从返回队列中删除最后一个问题票ID
			form.getBackIssueIdQuene().remove(form.getBackIssueIdQuene().size()-1);
			// 返回队列中大于两个问题票ID的场合，设置倒数第二个为下次返回时的问题票ID，否则清除返回问题票ID
			if(form.getBackIssueIdQuene().size() > 1) {
				form.setBackIssueId(form.getBackIssueIdQuene().get(form.getBackIssueIdQuene().size()-2));
			} else {
				form.setBackIssueId(null);
			}
		} else {
			// 返回队列存在的场合
			if(form.getBackIssueIdQuene() != null && !form.getBackIssueIdQuene().isEmpty()) {
				if(issueDetailInfo != null) {
					String lastBackIssueId = form.getBackIssueIdQuene().get(form.getBackIssueIdQuene().size()-1);
					String curIssueId = issueDetailInfo.get("issueId").toString();
					// 返回队列中的最后一个问题票ID和当前问题票ID不相同的场合，设置最后一个问题票ID为下次返回时的问题票ID，并把当前问题票ID添加到返回队列中
					if(!lastBackIssueId.equals(curIssueId)) {
						form.setBackIssueId(lastBackIssueId);
						form.getBackIssueIdQuene().add(curIssueId);
					} else {// 返回队列中的最后一个问题票ID和当前问题票ID相同的场合
						// 返回队列中大于两个问题票ID的场合，设置倒数第二个为下次返回时的问题票ID，否则清除返回问题票ID
						if(form.getBackIssueIdQuene().size() > 1) {
							form.setBackIssueId(form.getBackIssueIdQuene().get(form.getBackIssueIdQuene().size()-2));
						} else {
							form.setBackIssueId(null);
						}
					}
				}
			} else {
				// 返回队列不存在的场合，预存当前问题票ID到返回队列中
				if(issueDetailInfo != null) {
					List<String> backIssueIdQuene = new ArrayList<String>();
					backIssueIdQuene.add(issueDetailInfo.get("issueId").toString());
					form.setBackIssueIdQuene(backIssueIdQuene);
				}
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 添加问题处理内容
	 * 
	 * @return 会员问题处理画面
	 */
	public String saveIssueAction() throws Exception {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			map.put("author", userInfo.getBIN_EmployeeID());
			map.put("actionRole", userInfo.getBIN_PositionCategoryID());
			// 添加会员问题处理内容
			binOLMBMBM28_BL.tran_addIssueAction(map);
		} catch (Exception e) {
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
	 * 删除问题处理内容
	 * 
	 * @return 会员问题处理画面
	 */
	public String delIssueAction() throws Exception {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 删除会员问题处理内容
			binOLMBMBM28_BL.tran_delIssueAction(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00011"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 删除问题
	 * 
	 * @return 会员问题处理画面
	 */
	public String delIssue() throws Exception {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 删除会员问题
			binOLMBMBM28_BL.tran_delIssue(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00011"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * 编辑问题初始化
	 * 
	 * @return 编辑问题画面
	 */
	public String editIssueInit() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 取得会员问题明细
		issueDetailInfo = binOLMBMBM28_BL.getIssueEditDetail(map);
		return SUCCESS;
	}
	
	/**
	 * 编辑问题处理
	 * 
	 * @return 会员问题处理画面
	 */
	public String editIssue() throws Exception {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			map.put("author", userInfo.getBIN_EmployeeID());
			map.put("actionRole", userInfo.getBIN_PositionCategoryID());
			// 编辑问题处理
			binOLMBMBM28_BL.tran_editIssue(map);
		} catch (Exception e) {
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
	 * 保存问题处理内容前字段验证处理
	 * 
	 */
	public void validateSaveIssueAction() throws Exception {
		// 解决方案 不能超过1000位验证
		if(form.getActionBodyAdd() != null && !"".equals(form.getActionBodyAdd())) {
			// 解决方案 不能超过1000位验证
			if(form.getActionBodyAdd().length() > 1000) {
				this.addActionError(getText("ECM00020",new String[]{getText("PMB00077"),"1000"}));
			}
		}
	}
	
	/**
	 * 
	 * 编辑会员问题前字段验证处理
	 * 
	 */
	public void validateEditIssue() throws Exception {
//		// 摘要必须入力验证
//		if(CherryChecker.isNullOrEmpty(form.getIssueSummaryAdd())) {
//			this.addFieldError("issueSummaryAdd", getText("ECM00009",new String[]{getText("PMB00073")}));
//		} else {
//			// 摘要不能超过100位验证
//			if(form.getIssueSummaryAdd().length() > 100) {
//				this.addFieldError("issueSummaryAdd", getText("ECM00020",new String[]{getText("PMB00073"),"100"}));
//			}
//		}
		// 摘要不能超过100位验证
		if(form.getIssueSummaryAdd() != null && !"".equals(form.getIssueSummaryAdd())) {
			// 摘要不能超过100位验证
			if(form.getIssueSummaryAdd().length() > 100) {
				this.addFieldError("issueSummaryAdd", getText("ECM00020",new String[]{getText("PMB00073"),"100"}));
			}
		}
		// 类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getIssueTypeAdd())) {
			this.addFieldError("issueTypeAdd", getText("ECM00009",new String[]{getText("PMB00074")}));
		}
		// 截止日期日期格式验证
		if(form.getDueDateAdd() != null && !"".equals(form.getDueDateAdd())) {
			// 截止日期日期格式验证
			if(!CherryChecker.checkDate(form.getDueDateAdd())) {
				this.addFieldError("dueDateAdd", getText("ECM00022",new String[]{getText("PMB00075")}));
			}
		}
		// 销售单号不能超过35位验证
		if(form.getBillCodeAdd() != null && !"".equals(form.getBillCodeAdd())) {
			// 销售单号不能超过35位验证
			if(form.getBillCodeAdd().length() > 35) {
				this.addFieldError("billCodeAdd", getText("ECM00020",new String[]{getText("PMB00078"),"35"}));
			}
		}
		// 关联问题票号不能超过22位验证
		if(form.getReIssueNoAdd() != null && !"".equals(form.getReIssueNoAdd())) {
			// 关联问题票号不能超过22位验证
			if(form.getReIssueNoAdd().length() > 22) {
				this.addFieldError("reIssueNoAdd", getText("ECM00020",new String[]{getText("PMB00079"),"22"}));
			}
		}
		// 描述不能超过1000位验证
		if(form.getDescriptionAdd() != null && !"".equals(form.getDescriptionAdd())) {
			// 描述不能超过1000位验证
			if(form.getDescriptionAdd().length() > 1000) {
				this.addFieldError("descriptionAdd", getText("ECM00020",new String[]{getText("PMB00076"),"1000"}));
			}
		}
		// 解决方案 不能超过1000位验证
		if(form.getActionBodyAdd() != null && !"".equals(form.getActionBodyAdd())) {
			// 解决方案 不能超过1000位验证
			if(form.getActionBodyAdd().length() > 1000) {
				this.addFieldError("actionBodyAdd", getText("ECM00020",new String[]{getText("PMB00077"),"1000"}));
			}
		}
	}
	
	/** 会员问题明细 */
	public Map issueDetailInfo;
	
	public Map getIssueDetailInfo() {
		return issueDetailInfo;
	}

	public void setIssueDetailInfo(Map issueDetailInfo) {
		this.issueDetailInfo = issueDetailInfo;
	}

	/** 会员问题处理画面Form */
	private BINOLMBMBM28_Form form = new BINOLMBMBM28_Form();

	@Override
	public BINOLMBMBM28_Form getModel() {
		return form;
	}

}
