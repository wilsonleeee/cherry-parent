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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.vis.bl.BINOLMBVIS02_BL;
import com.cherry.mb.vis.form.BINOLMBVIS02_Form;
import com.cherry.mb.vis.service.BINOLMBVIS02_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员回访计划管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/12/15
 */
public class BINOLMBVIS02_Action extends BaseAction implements ModelDriven<BINOLMBVIS02_Form> {

	private static final long serialVersionUID = -8556094329748547192L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBVIS02_Action.class);
	
	@Resource
	private BINOLMBVIS02_BL binOLMBVIS02_BL;
	
	@Resource
	private BINOLMBVIS02_Service binOLMBVIS02_Service;
	
	/**
	 * 查询会员回访计划画面初期处理
	 * 
	 * @return 查询会员回访计划画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得会员回访类型List
		visitCategoryList = binOLMBVIS02_BL.getVisitCategoryList(map);
		
		// 默认查询有效数据
		form.setValidFlag("1");
		return SUCCESS;
	}
	
	/**
	 * 查询会员回访计划
	 * 
	 * @return 查询会员回访计划画面
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
		
		map.put("businessDate", binOLMBVIS02_Service.getDateYMD());

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得会员回访计划总数
		int count = binOLMBVIS02_BL.getVisitPlanCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 取得会员回访计划List
			visitPlanList = binOLMBVIS02_BL.getVisitPlanList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加会员回访计划画面初期处理
	 * 
	 * @return 添加会员回访计划画面
	 */
	public String addInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得会员回访类型List
		visitCategoryList = binOLMBVIS02_BL.getVisitCategoryList(map);
		// 取得会员问卷List
		paperList = binOLMBVIS02_BL.getPaperList(map);
		
		sysDate = binOLMBVIS02_Service.getDateYMD();
		
		return SUCCESS;
	}
	
	/**
	 * 添加会员回访计划
	 * 
	 * @return 添加会员回访计划画面
	 */
	public String add() throws Exception {
		try{
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 创建者
			map.put("employeeId", userInfo.getBIN_EmployeeID());
			// 创建时间
			map.put("planDateTime", binOLMBVIS02_Service.getSYSDateTime());
			map.put("brandCode",userInfo.getBrandCode());
			map.put("orgCode",userInfo.getOrganizationInfoCode());
			
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLMBVIS02");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS02");
			
			// 添加会员回访计划
			binOLMBVIS02_BL.tran_addVisitPlan(map);
			
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
	 * 添加会员回访计划前字段验证处理
	 * 
	 */
	public void validateAdd() throws Exception {
		
		if("1".equals(form.getVisitObjType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitObjJson())) {
				this.addFieldError("visitObjJson", getText("ECM00009",new String[]{getText("PMB00085")}));
			}
		} else if("2".equals(form.getVisitObjType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitObjCode())) {
				this.addFieldError("visitObjCode", getText("ECM00009",new String[]{getText("PMB00085")}));
			}
		}
		
		if("1".equals(form.getVisitDateType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitStartDate()) || CherryChecker.isNullOrEmpty(form.getVisitEndDate())) {
				this.addFieldError("visitEndDate", getText("ECM00009",new String[]{getText("PMB00086")}));
			}
		} else if("2".equals(form.getVisitDateType()) || "3".equals(form.getVisitDateType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitDateValue()) || CherryChecker.isNullOrEmpty(form.getValidValue())) {
				this.addFieldError("validValue", getText("ECM00009",new String[]{getText("PMB00086")}));
			}
		}
		
		if("2".equals(form.getPlanDate())) {
			if(CherryChecker.isNullOrEmpty(form.getEndDate())) {
				this.addFieldError("endDate", getText("ECM00009",new String[]{getText("PMB00087")}));
			}
		}
	}
	
	/**
	 * 更新会员回访计划画面初期处理
	 * 
	 * @return 更新会员回访计划画面
	 */
	public String updateInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitPlanId", form.getVisitPlanId());
		
		// 取得会员回访计划信息
		visitPlanInfo = binOLMBVIS02_BL.getVisitPlanInfo(map);
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 取得会员问卷List
		paperList = binOLMBVIS02_BL.getPaperList(map);
		
		sysDate = binOLMBVIS02_Service.getDateYMD();
		
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
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put("brandCode",userInfo.getBrandCode());
			map.put("orgCode",userInfo.getOrganizationInfoCode());
			
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS02");
			
			// 更新会员回访计划
			binOLMBVIS02_BL.tran_updateVisitPlan(map);
			
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
	 * 更新会员回访计划前字段验证处理
	 * 
	 */
	public void validateUpdate() throws Exception {
		
		if("1".equals(form.getVisitObjType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitObjJson())) {
				this.addFieldError("visitObjJson", getText("ECM00009",new String[]{getText("PMB00085")}));
			}
		} else if("2".equals(form.getVisitObjType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitObjCode())) {
				this.addFieldError("visitObjCode", getText("ECM00009",new String[]{getText("PMB00085")}));
			}
		}
		
		if("1".equals(form.getVisitDateType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitStartDate()) || CherryChecker.isNullOrEmpty(form.getVisitEndDate())) {
				this.addFieldError("visitEndDate", getText("ECM00009",new String[]{getText("PMB00086")}));
			}
		} else if("2".equals(form.getVisitDateType()) || "3".equals(form.getVisitDateType())) {
			if(CherryChecker.isNullOrEmpty(form.getVisitDateValue()) || CherryChecker.isNullOrEmpty(form.getValidValue())) {
				this.addFieldError("validValue", getText("ECM00009",new String[]{getText("PMB00086")}));
			}
		}
		
		if("2".equals(form.getPlanDate())) {
			if(CherryChecker.isNullOrEmpty(form.getEndDate())) {
				this.addFieldError("endDate", getText("ECM00009",new String[]{getText("PMB00087")}));
			}
		}
	}
	
	/**
	 * 更新会员回访计划画面初期处理
	 * 
	 * @return 更新会员回访计划画面
	 */
	public String detail() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitPlanId", form.getVisitPlanId());
		
		// 取得会员回访计划信息
		visitPlanInfo = binOLMBVIS02_BL.getVisitPlanInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 启用停用会员回访计划
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
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS02");
			
			// 启用停用会员回访类型
			binOLMBVIS02_BL.tran_updVisitPlanValid(map);
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
	
	/**
	 * 导入会员信息画面
	 * 
	 * @return 导入会员信息画面
	 */
	public String importInit() {
		if(form.getVisitObjCode() != null && !"".equals(form.getVisitObjCode())) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("visitObjCode", form.getVisitObjCode());
			int count = binOLMBVIS02_BL.getVisitObjCount(map);
			this.addActionMessage(getText("EMB00029", new String[]{String.valueOf(count)}));
		}
		return SUCCESS;
	}
	
	/**
	 * 导入会员信息处理
	 * 
	 * @return 导入会员信息画面
	 */
	public String excelImport() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("visitObjCode", form.getVisitObjCode());
			map.put("visitObjName", form.getVisitObjName());
			map.put("importType", form.getImportType());
			map.put("upExcel", form.getUpExcel());
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLMBVIS02");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLMBVIS02");
			
			// 导入会员回访对象处理
			String visitObjCode = binOLMBVIS02_BL.tran_addVisitObj(map);
			form.setVisitObjCode(visitObjCode);
			map.put("visitObjCode", visitObjCode);
			int count = binOLMBVIS02_BL.getVisitObjCount(map);
			this.addActionMessage(getText("EMB00029", new String[]{String.valueOf(count)}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			String errorMes = "";
			if(e instanceof CherryException){
				errorMes = ((CherryException)e).getErrMessage();
			}else{
				errorMes = getText("MBM00036");
			}
        	this.addActionError(errorMes);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员初始画面
	 * 
	 */
	public String searchMemInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 查询会员
	 * 
	 */
	public String searchMem() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		Map<String, Object> resultMap = binOLMBVIS02_BL.getVisitObj(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memberInfoList = (List)resultMap.get("list");
			}
		}
		return SUCCESS;
	}
	
	private List<Map<String, Object>> visitPlanList;
	
	private List<Map<String, Object>> visitCategoryList;
	
	private List<Map<String, Object>> paperList;
	
	private String sysDate;
	
	private Map visitPlanInfo;
	
	private List<Map<String, Object>> memberInfoList;
	
	public List<Map<String, Object>> getVisitPlanList() {
		return visitPlanList;
	}

	public void setVisitPlanList(List<Map<String, Object>> visitPlanList) {
		this.visitPlanList = visitPlanList;
	}

	public List<Map<String, Object>> getVisitCategoryList() {
		return visitCategoryList;
	}

	public void setVisitCategoryList(List<Map<String, Object>> visitCategoryList) {
		this.visitCategoryList = visitCategoryList;
	}

	public List<Map<String, Object>> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Map<String, Object>> paperList) {
		this.paperList = paperList;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public Map getVisitPlanInfo() {
		return visitPlanInfo;
	}

	public void setVisitPlanInfo(Map visitPlanInfo) {
		this.visitPlanInfo = visitPlanInfo;
	}

	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	/** 会员回访计划管理Form */
	private BINOLMBVIS02_Form form = new BINOLMBVIS02_Form();

	@Override
	public BINOLMBVIS02_Form getModel() {
		return form;
	}

}
