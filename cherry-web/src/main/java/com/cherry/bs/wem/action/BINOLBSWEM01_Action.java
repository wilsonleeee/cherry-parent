/*  
 * @(#)BINOLBSWEM01_Action.java     1.0 2015/08/03      
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
package com.cherry.bs.wem.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.form.BINOLBSWEM01_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM01_IF;
import com.cherry.bs.wem.interfaces.BINOLBSWEM04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM43_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 微商申请一览
 * 
 * @author hujh
 * @version 1.0 2015/08/03
 */
public class BINOLBSWEM01_Action extends BaseAction implements ModelDriven<BINOLBSWEM01_Form> {
	
	private static final long serialVersionUID = -8300900314315505614L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM01_Action.class);

	private BINOLBSWEM01_Form form = new BINOLBSWEM01_Form();

	private List<Map<String, Object>> resultList;

	private List<Map<String, Object>> empList;
	
	@Resource(name = "binOLBSWEM01_BL")
	private BINOLBSWEM01_IF binOLBSWEM01_BL;
	
	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLBSWEM04_BL")
	private BINOLBSWEM04_IF binOLBSWEM04_BL;	

	@Resource
	private BINOLCM43_BL binOLCM43_BL;
	
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	/** 区域List */
	@SuppressWarnings("rawtypes")
	private List reginList;
	
	private List agentLevelList;
	
	private Map<String, Object> agentInfoMap = new HashMap<String, Object>();
	
	private Map<String, Object> superAgentInfoMap;
	
	private Map<String, Object> oldSuperAgentInfoMap;
	
	/**
	 * 初始化画面
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
	    String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoID);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		form.setHolidays(binOLCM00_BL.getHolidays(map));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 开始日期
		form.setStartDate(DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, form.getEndDate(), -7));
		return SUCCESS;
	}

	/**
	 * 查询
	 */
	public String search() {
		Map<String, Object> paramsMap = this.getSearchMap();
		int count = binOLBSWEM01_BL.getWechatMerchantApplyCount(paramsMap);
		if(count > 0) {
			resultList = binOLBSWEM01_BL.getWechatMerchantApplyList(paramsMap);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		form.setCsrftoken(form.getCsrftoken());
		return SUCCESS;
	}

	/**
	 * 获得查询参数
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("BillCode", form.getBillCode());
		map.put("ApplyName", form.getApplyName());
		map.put("ApplyMobile", form.getApplyMobile());
		map.put("ApplyType", form.getApplyType());
		map.put("Status", form.getStatus());
		String startDate = CherryUtil.suffixDate(form.getStartDate(), 0);
		String endDate = CherryUtil.suffixDate(form.getEndDate(), 1);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		ConvertUtil.setForm(form, map);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	/**
	 * 分配初始化
	 */
	public String assignInit() {
		
		return SUCCESS;
	}
	
	/**
	 * 分配
	 */
	public String assignSearch() {
		List superCodeListTemp = new ArrayList();
		List superCodeList = new ArrayList();
		List superTypeList = new ArrayList();//级别
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("employeeName", form.getEmployeeName());
		String agentLevel = request.getParameter("agentLevel");
		//过滤直属下级
		String mainEmployeeId = request.getParameter("mainEmployeeId");
		if(!CherryChecker.isNullOrEmpty(mainEmployeeId)) {
			List<Map<String, Object>> employeeIdList = binOLBSWEM01_BL.getSubEmployeeIdList(mainEmployeeId);
			List<String> employeeIdStrList = new ArrayList<String>();
			employeeIdStrList.add(mainEmployeeId);
			if(null != employeeIdList && employeeIdList.size() > 0) {
				for(Map<String, Object> tempMap:employeeIdList) {
					employeeIdStrList.add(ConvertUtil.getString(tempMap.get("employeeId")));
				}
			}
			map.put("employeeIdArr", employeeIdStrList);
		}
		//如果为空，则取所有的
		if(!CherryChecker.isNullOrEmpty(agentLevel, true)) {//微商一览页面
			superCodeListTemp = codeTable.getSupCodesByKey("1000", agentLevel);
		} else {
			superCodeListTemp = codeTable.getAllByCodeType("1000");
			//微商申请页面，分配上级，去除总部和最低的级别的
			List excludeList = new ArrayList();
			excludeList.add("0");
			excludeList.add("1");
			excludeList.add("E");
			map.put("excludeList", excludeList);
		}
		if(superCodeListTemp.size() > 0) {
			for(int i = 0; i < superCodeListTemp.size(); i++) {
				Map tempMap = (Map) superCodeListTemp.get(i);
				String codeKey = ConvertUtil.getString(tempMap.get("codeKey"));
				if(!CherryChecker.isNullOrEmpty(codeKey, true)) {
					superCodeList.add(codeKey);
				}
				if(!"".equals(form.getEmployeeName()) && null != form.getEmployeeName()) {
					String value = ConvertUtil.getString(tempMap.get("value1"));
					if(value.contains(form.getEmployeeName())) {
						superTypeList.add(codeKey);
					}
				}
				
			}
			map.put("superCodeList", superCodeList);
			map.put("superTypeList", superTypeList);
		}
		ConvertUtil.setForm(form, map);
		CherryUtil.removeEmptyVal(map);
		int count = binOLBSWEM01_BL.getEmpCount(map);
		if(count > 0) {
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			empList = binOLBSWEM01_BL.getEmpList(map);
		}
		return SUCCESS;
	}
	
	public String assign() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
 		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
 		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
 		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put("mobilePhone", form.getMobilePhone());
		map.put("agentApplyId", form.getAgentApplyId());
		map.put("billCode", form.getBillCode());
		map.put("status", "2");
		map.put("assigner", userInfo.getEmployeeCode());
		map.put("assignTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSWEM01_Action");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSWEM01_Action");
		try {
			//判断下属个数
			String employeeId = ConvertUtil.getString(request.getParameter("employeeId"));
			if(null != employeeId && !"".equals(employeeId)) {
				map.put("employeeId", employeeId);
				//获取该员工对应部门类型限制的人数
				int limit = binOLBSWEM01_BL.getLimitAmout(map);
				if(binOLBSWEM01_BL.getSubAmount(employeeId) > limit) {
					map.put("resultCode", "-1");
					ConvertUtil.setResponseByAjax(response, map);
					return null;
				} ;
			}
			//更新上级号码
			binOLBSWEM01_BL.tran_assignSuperMobile(map);
			this.addActionMessage(getText("ICM00002"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(e.getMessage());
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 审核初始化
	 */
	public String auditInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		List codeList = codeTable.getAllByCodeType("1000");
		setAgentLevelList(binOLBSWEM04_BL.getAgentLevelList(codeList));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		setReginList(binOLCM08_BL.getReginList(map));
		String agentMobile = form.getApplyMobile();
		agentInfoMap = binOLCM43_BL.getAgentInfo(agentMobile);
		//未找到微商信息
		if(CherryChecker.isNullOrEmpty(agentInfoMap)) {
			this.addActionError("未找到微商信息!");
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} else {
			agentInfoMap.put("agentApplyId", form.getAgentApplyId());
			agentInfoMap.put("billCode", form.getBillCode());
			agentInfoMap.put("reason", form.getReason());
			//获取申请表中的数据，因为申请表中的数据和微商信息不一致的
			Map<String, Object> agentApplyMap = binOLBSWEM01_BL.getApplyInfoById(agentInfoMap);
			//申请类型，根据不同的申请类型显示不同的内容
			String applyType = ConvertUtil.getString(agentApplyMap.get("applyType"));
			agentInfoMap.put("applyType", applyType);
			if("4".equals(applyType) || "5".equals(applyType)) {//如果是升级申请的时候，显示原级别和申请的级别
				String agentLevel = ConvertUtil.getString(agentApplyMap.get("applyLevel"));
				String oldAgentLevel = ConvertUtil.getString(agentInfoMap.get("agentLevel"));
				agentInfoMap.put("agentLevel", agentLevel);
				agentInfoMap.put("oldAgentLevel", oldAgentLevel);
			}
			//申请单据中的上级手机号和原上级手机号
			String superMobile = ConvertUtil.getString(agentApplyMap.get("superMobile"));
			String oldSuperMobile = ConvertUtil.getString(agentApplyMap.get("oldSuperMobile"));
			String superName = "";
			String superAgentLevelName = "";
			//获取上级的等级信息
			if(null != superMobile && !"".equals(superMobile)) {
				superAgentInfoMap = binOLCM43_BL.getAgentInfo(superMobile);
				if(null != superAgentInfoMap) {
					superName = ConvertUtil.getString(superAgentInfoMap.get("agentName"));
					String superAgentLevel = ConvertUtil.getString(superAgentInfoMap.get("agentLevel"));
					superAgentLevelName = codeTable.getVal("1000", superAgentLevel);
				}
			}
			agentInfoMap.put("superMobile", superMobile);
			agentInfoMap.put("superName", superName);
			agentInfoMap.put("superAgentLevelName", superAgentLevelName);
			//获取原上级信息
			if(null != oldSuperMobile && !"".equals(oldSuperMobile)) {
				oldSuperAgentInfoMap = binOLCM43_BL.getAgentInfo(oldSuperMobile);
				if(null != oldSuperAgentInfoMap) {
					String oldAgentLevel = ConvertUtil.getString(oldSuperAgentInfoMap.get("agentLevel"));
					String agentLevelName = codeTable.getVal("1000", oldAgentLevel);
					oldSuperAgentInfoMap.put("agentLevelName", agentLevelName);
				}
			}
			return SUCCESS;
		}
		
	}
	
	@Override
	public BINOLBSWEM01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public List<Map<String, Object>> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Map<String, Object>> empList) {
		this.empList = empList;
	}

	public List getAgentLevelList() {
		return agentLevelList;
	}

	public void setAgentLevelList(List agentLevelList) {
		this.agentLevelList = agentLevelList;
	}

	public Map<String, Object> getAgentInfoMap() {
		return agentInfoMap;
	}

	public void setAgentInfoMap(Map<String, Object> agentInfoMap) {
		this.agentInfoMap = agentInfoMap;
	}

	public List getReginList() {
		return reginList;
	}

	public void setReginList(List reginList) {
		this.reginList = reginList;
	}

	public Map<String, Object> getSuperAgentInfoMap() {
		return superAgentInfoMap;
	}

	public void setSuperAgentInfoMap(Map<String, Object> superAgentInfoMap) {
		this.superAgentInfoMap = superAgentInfoMap;
	}

	public Map<String, Object> getOldSuperAgentInfoMap() {
		return oldSuperAgentInfoMap;
	}

	public void setOldSuperAgentInfoMap(Map<String, Object> oldSuperAgentInfoMap) {
		this.oldSuperAgentInfoMap = oldSuperAgentInfoMap;
	}

	

}
