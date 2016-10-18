/*	
 * @(#)BINOLBSEMP01_Action.java     1.0 2010/10/12		
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
package com.cherry.bs.emp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.bl.BINOLBSEMP01_BL;
import com.cherry.bs.emp.form.BINOLBSEMP01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 员工查询Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 */

@SuppressWarnings("unchecked")
public class BINOLBSEMP01_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP01_Form> {

	private static final long serialVersionUID = 1450167666815633771L;

	//打印异常日志
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP01_Action.class);
		
	/** 参数FORM */
	private BINOLBSEMP01_Form form = new BINOLBSEMP01_Form();
	
	@Resource
	private BINOLCM08_BL binOLCM08_BL;

	@Resource(name="binOLBSEMP01_BL")
	private BINOLBSEMP01_BL binolbsemp01BL;
	
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 区域List */
	private List reginList;

	/** 部门List */
	private List<Map<String, Object>> departList;

	/** 员工List */
	private List<Map<String, Object>> employeeList;
	
	/** 是否可维护BA信息*/
	private boolean maintainBa;
	
	/** 岗位类别信息List */
	private List positionCategoryList;
	
	/**系统年月日*/
	private String sysDate;

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	@Override
	public BINOLBSEMP01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List getReginList() {
		return reginList;
	}

	public void setReginList(List reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Map<String, Object>> departList) {
		this.departList = departList;
	}

	public List getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(List positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}
	
	public boolean isMaintainBa() {
		return maintainBa;
	}

	public void setMaintainBa(boolean maintainBa) {
		this.maintainBa = maintainBa;
	}
	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 用户一览（树模式）画面初期处理
	 * 
	 * @return 用户一览画面 
	 */
	public String treeInit() {
		
		return SUCCESS;
	}
	
	/**
	 * 用户一览（列表模式）画面初期处理
	 * 
	 * @return 用户一览画面 
	 * @throws Exception 
	 */
	public String listInit() throws Exception {
		
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得区域List
		reginList = binOLCM08_BL.getReginList(map);
		// 取得岗位类别信息List
		positionCategoryList = binOLCM00_BL.getPositionCategoryList(map);
		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX员工查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		try {
			// 取得参数MAP
			Map<String, Object> searchMap = getSearchMap();
			// 员工总数取得
			int count = binolbsemp01BL.getEmpCount(searchMap);
			if(count != 0) {
				// 取得员工信息List
				employeeList = binolbsemp01BL.searchEmployeeInfo(searchMap);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if((Integer)searchMap.get(CherryConstants.BRANDINFOID)==null){
				maintainBa = binOLCM14_BL.isConfigOpen("1038", (Integer)searchMap.get(CherryConstants.ORGANIZATIONINFOID), (Integer)CherryConstants.BRAND_INFO_ID_VALUE);
			}else{
				maintainBa = binOLCM14_BL.isConfigOpen("1038", (Integer)searchMap.get(CherryConstants.ORGANIZATIONINFOID), (Integer)searchMap.get(CherryConstants.BRANDINFOID));
			}
			//取得系统时间年月日
			sysDate=binolbsemp01BL.getDateYMD();
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 系统发生异常，请联系管理员
			this.addActionError(getText("ECM00036"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * AJAX取得直属下级雇员
	 * 
	 * @return 雇员一览画面 
	 */
	public String next() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 部门节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 有效区分
		map.put("validFlag", form.getValidFlag());
		// 用户ID
		map.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		// 取得直属下级雇员List
		String employeeTree = binolbsemp01BL.getNextEmployeeList(map);
		
		ConvertUtil.setResponseByAjax(response, employeeTree);
		return null;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 有效区分
		map.put("validFlag", form.getValidFlag());
		// 所属省份
		map.put("provinceId", form.getProvinceId());
		// 所属城市
		map.put("cityId", form.getCityId());
		// 姓名
		map.put("employeeName", form.getEmployeeName());
		// 登录帐号
		map.put("longinName", form.getLonginName());
		// 用户代号
		map.put("employeeCode", form.getEmployeeCode());
		// 岗位
		map.put("positionCategoryId", form.getPositionCategoryId());
		// 用户ID
		map.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		
		map.put("organizationId", form.getOrganizationId());
		// 是否带权限查询
		map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		return map;
	}
	
	/**
	 * 查询定位到的员工的所有上级员工位置
	 * 
	 */
	public String locateEmp() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put("locationPosition", form.getLocationPosition());
		// 用户ID
		map.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		List<String> locationHigher = binolbsemp01BL.getLocationHigher(map);
		ConvertUtil.setResponseByAjax(response, locationHigher);
		return null;
	}
	
	/**
	* 导出Excel
	* @throws Exception 
	*/
	public String export() throws Exception {
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE,
					session.get(CherryConstants.SESSION_LANGUAGE));
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}

			// dataTable上传的参数设置到map
			ConvertUtil.setForm(form, map);
			// 组织ID
			map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 取得库存信息List
			try {
				exportName = binolbsemp01BL.getExportName(map);
				excelStream = new ByteArrayInputStream(
						binolbsemp01BL.exportExcel(map));
			} catch (Exception e) {
				this.addActionError(getText("EMO00022"));
				logger.error(getText("EMO00022"));
				logger.error(e.getMessage(),e);
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			return SUCCESS;
		}
}
