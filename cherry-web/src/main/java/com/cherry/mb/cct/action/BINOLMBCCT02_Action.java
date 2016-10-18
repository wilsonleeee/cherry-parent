package com.cherry.mb.cct.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.form.BINOLMBCCT02_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT02_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT02_Action extends BaseAction implements ModelDriven<BINOLMBCCT02_Form>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLMBCCT02_Form form = new BINOLMBCCT02_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT02_Action.class);
	
	@Resource
	private BINOLMBCCT02_IF binolmbcct02_IF;
	
	private List<Map<String, Object>> issueList;
	
	public List<Map<String, Object>> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Map<String, Object>> issueList) {
		this.issueList = issueList;
	}

	public String init() throws Exception{
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌不存在的场合
			if(form.getCcBrandInfoId() == null || "".equals(form.getCcBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getCcBrandInfoId());
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 客户系统ID
			map.put("customerSysId", form.getCustomerSysId());
			//取得模板数量
			int count = binolmbcct02_IF.getIssueCountByCustomer(map);
			if(count > 0){
				List<Map<String, Object>> issList = binolmbcct02_IF.getIssueListByCustomer(map);
				// 取得List
				this.setIssueList(issList);
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
	
	@Override
	public BINOLMBCCT02_Form getModel() {
		return form;
	}
}
