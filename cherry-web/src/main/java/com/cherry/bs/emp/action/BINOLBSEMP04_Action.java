/*	
 * @(#)BINOLBSEMP04_Action.java     1.0 2010/12/22	
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.bl.BINOLBSEMP04_BL;
import com.cherry.bs.emp.form.BINOLBSEMP04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.JsclPBKDF2WithHMACSHA256;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 添加员工Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.12.22
 */

@SuppressWarnings("unchecked")
public class BINOLBSEMP04_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP04_Form> {

	private static final long serialVersionUID = 7904515441934092270L;

	/** 参数FORM */
	private BINOLBSEMP04_Form form = new BINOLBSEMP04_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP04_Action.class);

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name="binOLCM08_BL")
	private BINOLCM08_BL binOLCM08_BL;

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	@Resource(name="binOLBSEMP04_BL")
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/** 品牌List */
	private List brandInfoList;

	/** 省份List */
	private List provinceList;

	/** 部门List */
	private List departList;

	/** 假日信息 */
	private String holidays;
	
	/** 岗位类别信息List */
	private List positionCategoryList;
	
	/** 是否对BI用户进行操作 */
	private String createBIUser;
	
	private String mobileRule;
	
	@Resource
	private CodeTable code;

	@Override
	public BINOLBSEMP04_Form getModel() {
		return form;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}

	public List getDepartList() {
		return departList;
	}

	public void setDepartList(List departList) {
		this.departList = departList;
	}

	public List getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(List positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}

	public String getCreateBIUser() {
		return createBIUser;
	}

	public void setCreateBIUser(String createBIUser) {
		this.createBIUser = createBIUser;
	}

	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
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
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织ID
			String orgId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			String brandId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 手机号校验规则
			mobileRule = binOLCM14_BL.getConfigValue("1090", orgId, brandId);
						
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			int brandInfoId = 0;
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 总部用户登录时
			if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
					.getBIN_BrandInfoID()) {
				// 取得品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
				// 品牌名称
				brandMap.put("brandName", getText("PPL00006"));
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
				brandInfoId = (Integer)((Map)brandInfoList.get(0)).get("brandInfoId");
			} else {
				// 品牌信息
				Map<String, Object> brandInfo = new HashMap<String, Object>();
				// 品牌ID
				brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 品牌名称
				brandInfo.put("brandName", userInfo.getBrandName());
				// 品牌List
				brandInfoList = new ArrayList();
				brandInfoList.add(brandInfo);
				brandInfoId = userInfo.getBIN_BrandInfoID();
			}
			// 取得部门List
			departList = binolbsemp04BL.getOrgList(map);
			// 取得岗位类别信息List
			//取得新系统配置信息是否维护BA信息
			boolean maintainBa = binOLCM14_BL.isConfigOpen("1038", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
			form.setMaintainBa(maintainBa);
			map.put("maintainBa", maintainBa);
			positionCategoryList = binolbsemp04BL.getPositionCategoryList(map);
			// 取得区域List
			provinceList = binOLCM08_BL.getProvinceList(map);
			// 查询假日
			holidays = binolcm00BL.getHolidays(map);
			
			if(binOLCM14_BL.isConfigOpen("1008", String.valueOf(userInfo.getBIN_OrganizationInfoID()),String.valueOf(userInfo.getBIN_BrandInfoID()))) {
				Map codeMap = code.getCode("1120","1");
				map.put("type", "1");
				map.put("length", codeMap.get("value2"));
				// 作成者
				map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
				// 更新者
				map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
				// 作成模块
				map.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
				// 更新模块
				map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
				// 自动生成员工ID
				form.setEmployeeCode((String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
			}
			
			// 对BI用户进行操作的场合
			if(binolbsemp04BL.isCreateBIUser(orgId, brandId)) {
				createBIUser = "true";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
            }else{
            	this.addActionError(getText("ECM00036"));
            	return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
            }
		}
		
		return SUCCESS;
	}
	
	/**
	 * 根据品牌ID过滤下拉列表数据
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
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
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得部门List
		resultMap.put("orgList", binolbsemp04BL.getOrgList(map));
		// 取得岗位类别信息List
		resultMap.put("positionCategoryList", binolbsemp04BL.getPositionCategoryList(map));
		
		if(binOLCM14_BL.isConfigOpen("1008", String.valueOf(userInfo.getBIN_OrganizationInfoID()),form.getBrandInfoId())) {
			Map codeMap = code.getCode("1120","1");
			map.put("type", "1");
			map.put("length", codeMap.get("value2"));
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
			// 自动生成员工ID
			resultMap.put("employeeId", (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
		}
		
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}

	/**
	 * ajax通过岗位类别ID取得岗位类别信息
	 * 
	 * 
	 * */
	public void getPositionCategoryInfo() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		map.put("positionCategoryId", form.getPositionCategoryId());
		Map<String,Object> positionCategoryInfo = binolbsemp04BL.getPositionCategoryInfo(map);
		ConvertUtil.setResponseByAjax(response, positionCategoryInfo);
	}
	
	
	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unused")
	public String save() throws Exception {
		try{
			// form参数放入Map中
			Map<String, Object> map = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 取得品牌ID对应的品牌CODE【加密解密参数】
			int brandInfoId = ConvertUtil.getInt(map.get("brandInfoId"));
			map.put("brandCode", binOLCM05_BL.getBrandCode(brandInfoId));
			// 添加员工到数据库
			binolbsemp04BL.tran_insertEmployee(userInfo, map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
			}
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	public void validateSave() throws Exception {
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		//取得岗位信息
		Map<String,Object> valiMap = new HashMap<String,Object>();
		valiMap.put("positionCategoryId", form.getPositionCategoryId()); // 岗位ID
		Map<String,Object> positionInfo = binolbsemp04BL.getPositionCategoryInfo(valiMap);
		
		// 员工编号必须验证
		if (CherryChecker.isNullOrEmpty(form.getEmployeeCode())) {
			this.addFieldError("employeeCode", getText("ECM00009",
					new String[] { getText("PBS00020") }));
		}  else {
			// 各岗位员工编号验证规则不一样，BA、BAS根据系统配置项的正式验证，其他岗位根据下面的程序进行验证
			if (!CherryChecker.isNullOrEmpty(positionInfo)) {
				if(CherryConstants.CATRGORY_CODE_BA.equals(positionInfo.get("categoryCode"))){
					// BA编码规则
					String basPattern = binOLCM14_BL.getConfigValue("1075", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					Pattern p = Pattern.compile(basPattern);
					Matcher m = p.matcher(form.getEmployeeCode());
					if(!m.matches()){
						this.addFieldError("employeeCode", getText("EBS00092",
								new String[] { getText("PBS00069",new String[]{String.valueOf(positionInfo.get("categoryName"))})}));
					}
				}else if(CherryConstants.CATRGORY_CODE_BAS.equals(positionInfo.get("categoryCode"))){
					// BAS编码规则
					String basPattern = binOLCM14_BL.getConfigValue("1074", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					Pattern p = Pattern.compile(basPattern);
					Matcher m = p.matcher(form.getEmployeeCode());
					if(!m.matches()){
						this.addFieldError("employeeCode", getText("EBS00091",
								new String[] { getText("PBS00069",new String[]{String.valueOf(positionInfo.get("categoryName"))})}));
					}
				}else {
					// 其他岗位
					if (!CherryChecker.isEmployeeCode(form.getEmployeeCode())) {
						// 员工编号英数验证
						this.addFieldError("employeeCode", getText("ECM00044",
								new String[] { getText("PBS00020") }));
					} else if (form.getEmployeeCode().length() > 15) {
						// 员工编号长度验证
						this.addFieldError("employeeCode", getText("ECM00020",
								new String[] { getText("PBS00020"), "15" }));
					}
				}
			}
			
//			if (!CherryChecker.isNullOrEmpty(positionInfo)) {
//				// 系统配置项中的【是否支持BA维护】是否打开且当前岗位为BA 或 【维护BAS时是否发送MQ】是否打开且当前岗位为BAS
//				boolean maintainBa = binOLCM14_BL.isConfigOpen("1038", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()); // 【是否支持BA维护】
//				boolean maintainBas = binOLCM14_BL.isConfigOpen("1048", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()); // 【维护BAS时是否发送MQ】
//				boolean isConfBA = (maintainBa && CherryConstants.CATRGORY_CODE_BA.equals(positionInfo.get("categoryCode")));
//				boolean isConfBAS= (maintainBas && CherryConstants.CATRGORY_CODE_BAS.equals(positionInfo.get("categoryCode")));
//				if (isConfBA) {
//					// BA编码规则
//					String basPattern = binOLCM14_BL.getConfigValue("1075", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
//					Pattern p = Pattern.compile(basPattern);
//					Matcher m = p.matcher(form.getEmployeeCode());
//					if(!m.matches()){
//						this.addFieldError("employeeCode", getText("EBS00092",
//								new String[] { getText("PBS00069",new String[]{String.valueOf(positionInfo.get("categoryName"))})}));
//					}
//				} else if (isConfBAS) {
//					// BAS编码规则
//					String basPattern = binOLCM14_BL.getConfigValue("1074", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
//					Pattern p = Pattern.compile(basPattern);
//					Matcher m = p.matcher(form.getEmployeeCode());
//					if(!m.matches()){
//						this.addFieldError("employeeCode", getText("EBS00091",
//								new String[] { getText("PBS00069",new String[]{String.valueOf(positionInfo.get("categoryName"))})}));
//					}
//				}
//				
//			}
			
			if(!this.hasFieldErrors()) {
				// 用户信息
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织ID
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
						.getBIN_OrganizationInfoID());
				// 品牌ID
				map.put("brandInfoId", form.getBrandInfoId());
				// 员工编号
				map.put("employeeCode", form.getEmployeeCode());
				String empId = binolbsemp04BL.getEmployeeId(map);
				if (!CherryChecker.isNullOrEmpty(empId)) {
					// 员工编号重复验证
					this.addFieldError("employeeCode", getText("ECM00032",
							new String[] { getText("PBS00020") }));
				}
			}
		}
		// 品牌必须验证
		if (CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId", getText("ECM00009",
					new String[] { getText("PBS00048") }));
		}
		
		// 岗位必须验证
		if (CherryChecker.isNullOrEmpty(form.getPositionCategoryId())) {
			this.addFieldError("positionCategoryId", getText("ECM00009",
					new String[] { getText("PBS00039") }));
		}
		String categoryCode = null;
		//如果添加的员工是营业员，则部门不是必须的，否则部门必填
		if(positionInfo == null || positionInfo.isEmpty() || !CherryConstants.CATRGORY_CODE_BA.equals(positionInfo.get("categoryCode"))){
			categoryCode = ConvertUtil.getString(positionInfo.get("categoryCode"));
			if(!"02".equals(categoryCode)) {
				form.setCreatOrgFlag(null);
			}
			if(form.getCreatOrgFlag() == null || "".equals(form.getCreatOrgFlag())) {
				// 部门必须验证
				if (CherryChecker.isNullOrEmpty(form.getDepartId())) {
					this.addFieldError("departId", getText("ECM00009",
							new String[] { getText("PBS00049") }));
				}
			}
		} else {
			categoryCode = (String)positionInfo.get("categoryCode");
		}
		
		// 登录帐号必须验证
		if (!CherryChecker.isNullOrEmpty(form.getLonginName())) {
			// 登录帐号不能超过30位验证
			if(form.getLonginName().length() > 30) {
				this.addFieldError("longinName", getText("ECM00020",new String[]{getText("PBS00046"),"30"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("longinName", form.getLonginName());
				// 验证登录帐号是否唯一
				String userId = binolbsemp04BL.getUserIdByLgName(map);
				if (!CherryChecker.isNullOrEmpty(userId)) {
					this.addFieldError("longinName", getText("ECM00032",
							new String[] { getText("PBS00046") }));
				} else {
					String loginName = binolbsemp04BL.getLoginConfigByLgName(map);
					if(!CherryChecker.isNullOrEmpty(loginName)) {
						this.addFieldError("longinName", getText("ECM00032",
								new String[] { getText("PBS00046") }));
					}
				}
			}
			// 登录帐号存在的场合，登录密码必须验证
			if (CherryChecker.isNullOrEmpty(form.getPassword())) {
				this.addFieldError("password", getText("ECM00009",
						new String[] { getText("PBS00047") }));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userInfo.getBIN_UserID());
				String currentUserPsd = binolbsemp04BL.getUserPassWord(map);
				String newPassWord = null;
				try {
					newPassWord = JsclPBKDF2WithHMACSHA256.DecrptPBKDF2WithHMACSHA256(currentUserPsd, form.getPassword());
					form.setPassword(newPassWord);
				} catch (Exception e) {
					
				}
				if(newPassWord == null) {
					this.addFieldError("currentUserPsd", getText("EPL00013"));
				} else {
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
							.getBIN_OrganizationInfoID());
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
					// 密码安全配置信息
					Map pswConfig = binolbsemp04BL.getPassWordConfig(map);
					if(pswConfig == null){
						this.addFieldError("password", getText("EPL00012"));
					}else{
						boolean lengthFlg = true;
						// 密码最小长度
						String minLengthStr = String.valueOf(pswConfig.get("minLength"));
						if (null != minLengthStr) {
							int minLength = Integer.parseInt(minLengthStr);
							// 密码不能小于密码最小长度验证
							if (form.getPassword().length() < minLength) {
								this.addFieldError("password", getText("EPL00008",
										new String[] { getText("PBS00047"), minLengthStr }));
								lengthFlg = false;
							}
						}
						// 密码最大长度
						String maxLengthStr = String.valueOf(pswConfig.get("maxLength"));
						if (lengthFlg && null != maxLengthStr) {
							int maxLength = Integer.parseInt(maxLengthStr);
							// 密码不能超过密码最大长度验证
							if (form.getPassword().length() > maxLength) {
								this.addFieldError("password", getText("ECM00020",
										new String[] { getText("PBS00047"), maxLengthStr }));
								lengthFlg = false;
							}
						}
						//根据密码复杂度正则表达式判断密码复杂度
					    if (lengthFlg){
					    	//密码复杂度
						    String complexity = String.valueOf(pswConfig.get("complexity"));
						    if (!CherryChecker.isNullOrEmpty(complexity)){
						    	if (!form.getPassword().matches(complexity)){
						    		this.addFieldError("password", getText("EPL00007"));
						    	}
						    }
						}
					}
				}
			}
		}
		// 员工姓名必须验证
		if (CherryChecker.isNullOrEmpty(form.getEmployeeName())) {
			this.addFieldError("employeeName", getText("ECM00009",
					new String[] { getText("PBS00021") }));
		} else if (form.getEmployeeCode().length() > 30) {
			// 员工姓名长度验证
			this.addFieldError("employeeName", getText("ECM00020",
					new String[] { getText("PBS00021"), "30" }));
		}
		// 员工外文名验证
		if (!CherryChecker.isNullOrEmpty(form.getEmployeeNameForeign())) {
			if (form.getEmployeeCode().length() > 30) {
				// 员工外文名长度验证
				this.addFieldError("employeeNameForeign", getText("ECM00020",
						new String[] { getText("PBS00022"), "30" }));
			}
		}
		// 生日日期格式验证
		if (!CherryChecker.isNullOrEmpty(form.getBirthDay())
				&& !CherryChecker.checkDate(form.getBirthDay())) {
			this.addFieldError("birthDay", getText("ECM00022",
					new String[] { getText("PBS00023") }));
		}
		
		// 所属组织ID
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(form.getBrandInfoId());
		
		// 联系电话入力验证
		if (!CherryChecker.isNullOrEmpty(form.getPhone())
				&& !CherryChecker.isTelValid(form.getPhone())) {
			this.addFieldError("phone", getText("EBS00001"));
		}

		//BA管理身份证/手机必填校验模式
		boolean mustInputFlag = binOLCM14_BL.isConfigOpen("1384", organizationInfoId, brandInfoId);	
		// 手机号码必须验证
		if (CherryChecker.isNullOrEmpty(form.getMobilePhone())) {
			//BA的场合，做以下校验
			if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode) && mustInputFlag) {
				this.addFieldError("mobilePhone", getText("ECM00009",
						new String[] { getText("PBS00070") }));				
			}
			// 手机号码数字验证
		} else if (!CherryChecker.isNullOrEmpty(form.getMobilePhone())) {
			
			String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			
			if(!CherryChecker.isPhoneValid(form.getMobilePhone(), mobileRule)) {
				this.addFieldError("mobilePhone", getText("EBS00004"));
			} else {
				//BA的场合，做以下校验
				if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织ID
					map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
					// 品牌ID
					map.put(CherryConstants.BRANDINFOID, brandInfoId);
					map.put("mobilePhone", form.getMobilePhone());
					// 验证手机是否唯一
					List<String> empIdList = binolbsemp04BL.getEmployeeIdByMobile(map);
					if (empIdList != null && !empIdList.isEmpty()) {
						this.addFieldError("mobilePhone", getText("ECM00032",
								new String[] { getText("PBS00070") }));
					}
				}
			}
		}
		// 电子邮箱格式验证
		if (!CherryChecker.isNullOrEmpty(form.getEmail())) {
			if(form.getEmail().length() > 60) {
				// 电子邮箱长度验证
				this.addFieldError("email", getText("ECM00020",
						new String[] { getText("PBS00071"), "60" }));
			} else if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("EBS00002"));
			} else {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("email", form.getEmail());
//				// 验证邮箱是否唯一
//				List<String> emailList = binolbsemp04BL.getEmployeeIdByEmail(map);
//				if (emailList != null && !emailList.isEmpty()) {
//					this.addFieldError("email", getText("ECM00032",
//							new String[] { getText("PBS00071") }));
//				}
			}
		}
		// 身份证号必须验证
		if (CherryChecker.isNullOrEmpty(form.getIdentityCard())) {
			//BA的场合，做以下校验
			if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode) && mustInputFlag) {
				this.addFieldError("identityCard", getText("ECM00009",
						new String[] { getText("PBS00101") }));
			}
			// 身份证号入力验证
		} else if (!CherryChecker.isNullOrEmpty(form.getIdentityCard())) {
			// 身份证格式验证
			String identityCardPattern = binOLCM14_BL.getConfigValue("1364", organizationInfoId, brandInfoId);
			//String identityCardPattern = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([(0-9)|X]{1}))$";				
			Pattern p = Pattern.compile(identityCardPattern);
			Matcher m = p.matcher(form.getIdentityCard());
			if(!m.matches()){				
				this.addFieldError("identityCard", getText("EBS00003"));
			} else {
				//BA的场合，做以下校验
				if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)) {
					// 校验身份证号是否在其他BA中已经存在 
					Map<String, Object> identityCardParam = new HashMap<String, Object>();
					// 所属组织ID
					identityCardParam.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
					// 品牌ID
					identityCardParam.put(CherryConstants.BRANDINFOID, brandInfoId);
					identityCardParam.put("identityCard", form.getIdentityCard());
					// 验证身份证是否唯一
					List<String> employeeCodeList = binolbsemp04BL.validateIdentityCard(identityCardParam);		
					if (employeeCodeList.size() != 0) {
						this.addFieldError("identityCard", getText("ECM00032",
								new String[] { getText("PBS00101") }));
					}
				}
			}
		}
		// 地址信息入力验证
		if (null != form.getAddressInfo()) {
			// 员工地址List
			List<Map<String, String>> addrList = (List<Map<String, String>>) JSONUtil
					.deserialize(form.getAddressInfo());
			if (null != addrList) {
				for (int i = 0; i < addrList.size(); i++) {
					Map<String, String> addr = addrList.get(i);
					// 邮编格式验证
					if (null != addr.get("zipCode")
							&& !CherryChecker.isZipValid(addr.get("zipCode"))) {
						this.addFieldError("zipCode_" + i, getText("ECM00023",
								new String[] { getText("PBS00011") }));
					}
					// 地址长度验证
					if (null != addr.get("address")
							&& addr.get("address").length() > 100) {
						this.addFieldError("address_" + i, getText("ECM00020",
								new String[] { getText("PBS00024"), "100" }));
					}
					if ((null != addr.get("zipCode") || null != addr.get("provinceId") || 
							null != addr.get("cityId"))&& null == addr.get("address")) {
						// 必须验证
						this.addFieldError("address_" + i, getText(
								"ECM00009", new String[] { getText("PBS00024") }));
					}
				}
			}
		}
		// 入职日期格式验证
		if (!CherryChecker.isNullOrEmpty(form.getCommtDate())
				&& !CherryChecker.checkDate(form.getCommtDate())) {
			this.addFieldError("commtDate", getText("ECM00022",
					new String[] { getText("PBS00026") }));
		}
	}

//	/**
//	 * 对长度小于len的字符串str，在其前加字符ch处理
//	 * 
//	 * @param str
//	 * @param ch
//	 * @param len
//	 * @return
//	 */
//	private String getString(String str, String ch, int len) {
//		if (!CherryChecker.isNullOrEmpty(str)) {
//			int chLen = len - str.length();
//			if (chLen > 0) {
//				for (int i = 0; i < chLen; i++) {
//					str = ch + str;
//				}
//			}
//		}
//		return str;
//	}
}
