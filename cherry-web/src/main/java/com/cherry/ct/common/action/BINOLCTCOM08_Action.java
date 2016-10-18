package com.cherry.ct.common.action;

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
import com.cherry.ct.common.form.BINOLCTCOM08_Form;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTCOM08_Action extends BaseAction implements ModelDriven<BINOLCTCOM08_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLCTCOM08_Form form = new BINOLCTCOM08_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM08_Action.class);

	@Resource
	private BINOLCTTPL01_IF binolcttpl01_IF;
	
	/** 沟通模板List */
	private List<Map<String, Object>> templateList;
	
	public List<Map<String, Object>> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Map<String, Object>> templateList) {
		this.templateList = templateList;
	}

	public String init() throws Exception{
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binolcttpl01_IF.getTemplateCount(map);
			if(count > 0){
				List<Map<String, Object>> tplList = binolcttpl01_IF.getTemplateList(map);
				// 取得List
				this.setTemplateList(tplList);
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
	
	private Map<String, Object> getSearchMap() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 适用客户类型
		map.put("status", form.getStatus());
		// 适用客户类型
		map.put("templateUse", form.getTemplateUse());
		// 适用客户类型
		map.put("templateType", form.getMessageType());
		// 模板名称
		map.put("templateName", form.getTemplateName());
		// 适用客户类型
		map.put("customerType", form.getCustomerType());
		
		return map;
	}
	
	@Override
	public BINOLCTCOM08_Form getModel() {
		return form;
	}
}
