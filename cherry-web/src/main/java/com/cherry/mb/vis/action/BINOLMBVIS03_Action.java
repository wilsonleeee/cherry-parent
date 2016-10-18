package com.cherry.mb.vis.action;

import java.util.HashMap;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.vis.bl.BINOLMBVIS03_BL;
import com.cherry.mb.vis.form.BINOLMBVIS03_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员回访类型管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS03_Action extends BaseAction implements ModelDriven<BINOLMBVIS03_Form> {
	
	private static final long serialVersionUID = 614384387865891644L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBVIS03_Action.class);
	
	@Resource
	private BINOLMBVIS03_BL binOLMBVIS03_BL;
	
	/**
	 * 查询会员回访类型画面初期处理
	 * 
	 * @return 查询会员回访类型画面
	 */
	public String init() {
		// 默认查询有效数据
		form.setValidFlag("1");
		return SUCCESS;
	}
	
	/**
	 * 查询会员回访类型
	 * 
	 * @return 查询会员回访类型画面
	 * @throws Exception 
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);

		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得会员回访类型总数
		int count = binOLMBVIS03_BL.getVisitCategoryCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 取得会员回访类型List
			visitCategoryList = binOLMBVIS03_BL.getVisitCategoryList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加会员回访类型画面初期处理
	 * 
	 * @return 添加会员回访类型画面
	 */
	public String addInit() {
		return SUCCESS;
	}
	
	/**
	 * 添加会员回访类型
	 * 
	 * @return 添加会员回访类型画面
	 */
	public String add() throws Exception {
		try{
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);

			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLMBVIS03");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS03");
			
			// 添加会员回访类型
			binOLMBVIS03_BL.tran_addVisitCategory(map);
			
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 添加会员回访类型前字段验证处理
	 * 
	 */
	public void validateAdd() throws Exception {
		// 回访类型代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getVisitTypeCode())) {
			this.addFieldError("visitTypeCode", getText("ECM00009",new String[]{getText("PMB00088")}));
		} else {
			// 回访类型代码不能超过20位验证
			if(form.getVisitTypeCode().length() > 20) {
				this.addFieldError("visitTypeCode", getText("ECM00020",new String[]{getText("PMB00088"),"20"}));
			}
		}
		// 回访类型名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getVisitTypeName())) {
			this.addFieldError("visitTypeName", getText("ECM00009",new String[]{getText("PMB00084")}));
		} else {
			// 回访类型名称不能超过20位验证
			if(form.getVisitTypeName().length() > 20) {
				this.addFieldError("visitTypeName", getText("ECM00020",new String[]{getText("PMB00084"),"20"}));
			}
		}
		
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 回访类型代码
			map.put("visitTypeCode", form.getVisitTypeCode());
			// 回访类型代码唯一验证
			String visitCategoryId = binOLMBVIS03_BL.getVisitCategoryByCode(map);
			if(visitCategoryId != null && !"".equals(visitCategoryId)) {
				this.addFieldError("visitTypeCode", getText("EMB00028"));
			}
		}
	}
	
	/**
	 * 更新会员回访类型画面初期处理
	 * 
	 * @return 更新会员回访类型画面
	 */
	public String updateInit() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitCategoryId", form.getVisitCategoryId());
		
		// 取得会员回访类型信息
		visitCategoryInfo = binOLMBVIS03_BL.getVisitCategoryInfo(map);
		
		return SUCCESS;
	}
	
	/**
	 * 更新会员回访类型
	 * 
	 * @return 更新会员回访类型画面
	 */
	public String update() {
		try{
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);

			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS03");
			
			// 更新会员回访类型
			binOLMBVIS03_BL.tran_updateVisitCategory(map);
			
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 添加会员回访类型前字段验证处理
	 * 
	 */
	public void validateUpdate() throws Exception {
//		// 回访类型代码必须入力验证
//		if(CherryChecker.isNullOrEmpty(form.getVisitTypeCode())) {
//			this.addFieldError("visitTypeCode", getText("ECM00009",new String[]{getText("PMB00088")}));
//		} else {
//			// 回访类型代码不能超过20位验证
//			if(form.getVisitTypeCode().length() > 20) {
//				this.addFieldError("visitTypeCode", getText("ECM00020",new String[]{getText("PMB00088"),"20"}));
//			}
//		}
		// 回访类型名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getVisitTypeName())) {
			this.addFieldError("visitTypeName", getText("ECM00009",new String[]{getText("PMB00084")}));
		} else {
			// 回访类型名称不能超过20位验证
			if(form.getVisitTypeName().length() > 20) {
				this.addFieldError("visitTypeName", getText("ECM00020",new String[]{getText("PMB00084"),"20"}));
			}
		}
		
//		if(!this.hasFieldErrors()) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			// 登陆用户信息
//			UserInfo userInfo = (UserInfo) session
//					.get(CherryConstants.SESSION_USERINFO);
//			// 所属组织
//			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//			// 所属品牌
//			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//			// 回访类型代码
//			map.put("visitTypeCode", form.getVisitTypeCode());
//			// 回访类型代码唯一验证
//			String visitCategoryId = binOLMBVIS03_BL.getVisitCategoryByCode(map);
//			if(visitCategoryId != null && !visitCategoryId.equals(form.getVisitCategoryId())) {
//				this.addFieldError("visitTypeCode", getText("EMB00028"));
//			}
//		}
	}
	
	/**
	 * 启用停用会员回访类型
	 * 
	 * @return 处理结果画面
	 * @throws Exception 
	 */
	public void updateValid() throws Exception {
		String code = "ok";
		String errorMes = "";
		try{
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);

			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS03");
			
			// 启用停用会员回访类型
			binOLMBVIS03_BL.tran_updVisitCategoryValid(map);
		} catch (Exception e) {
			code = "error";
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				errorMes = ((CherryException)e).getErrMessage();
			}else{
				//系统发生异常，请联系管理人员。
				errorMes = getText("ECM00036");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/** 会员回访类型管理Form */
	private BINOLMBVIS03_Form form = new BINOLMBVIS03_Form();
	
	private List<Map<String, Object>> visitCategoryList;
	
	private Map visitCategoryInfo;

	public List<Map<String, Object>> getVisitCategoryList() {
		return visitCategoryList;
	}

	public void setVisitCategoryList(List<Map<String, Object>> visitCategoryList) {
		this.visitCategoryList = visitCategoryList;
	}

	public Map getVisitCategoryInfo() {
		return visitCategoryInfo;
	}

	public void setVisitCategoryInfo(Map visitCategoryInfo) {
		this.visitCategoryInfo = visitCategoryInfo;
	}

	@Override
	public BINOLMBVIS03_Form getModel() {
		return form;
	}

}
