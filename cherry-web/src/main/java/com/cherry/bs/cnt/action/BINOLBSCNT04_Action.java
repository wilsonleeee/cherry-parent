/*	
 * @(#)BINOLBSCNT04_Action.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.cnt.bl.BINOLBSCNT04_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 创建柜台画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT04_Action extends BaseAction implements
		ModelDriven<BINOLBSCNT04_Form> {

	private static final long serialVersionUID = 2181371041826003987L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLBSCNT04_Action.class);

	/** 创建柜台画面Form */
	private BINOLBSCNT04_Form form = new BINOLBSCNT04_Form();

	/** 共通BL */
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** 共通BL */
	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 各类编号取号共通BL */
	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/** 创建柜台画面BL */
	@Resource(name = "binOLBSCNT04_BL")
	private BINOLBSCNT04_BL binOLBSCNT04_BL;

	@Resource(name = "CodeTable")
	private CodeTable code;

	/**
	 * 
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String 创建柜台画面
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 总部用户的场合
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		// 品牌存在的场合
		if (brandInfoList != null && !brandInfoList.isEmpty()) {
			String brandInfoId = String.valueOf(brandInfoList.get(0).get(
					CherryConstants.BRANDINFOID));
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoId);

			// 柜台编号生成规则
			String cntCodeRule = binOLCM14_BL.getConfigValue("1139",
					String.valueOf(userInfo.getBIN_OrganizationInfoID()),
					brandInfoId);
			form.setCntCodeRule(cntCodeRule);

			// 普通规则
			if (CherryConstants.CNTCODE_RULE1.equals(cntCodeRule)) {

				// 是否自动生成柜台编号
				if (binOLCM14_BL.isConfigOpen("1009",
						String.valueOf(userInfo.getBIN_OrganizationInfoID()),
						brandInfoId)) {
					Map codeMap = code.getCode("1120", "2");
					map.put("type", "2");
					map.put("length", codeMap.get("value2"));
					// 作成者
					map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
					// 更新者
					map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
					// 作成模块
					map.put(CherryConstants.CREATEPGM, "BINOLBSCNT04");
					// 更新模块
					map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT04");
					// 自动生成柜台号
					form.setCounterCode((String) codeMap.get("value1")
							+ binOLCM15_BL.getSequenceId(map));
				}
			}
			// 自然堂规则
			// 一位首数字+4位国家省份区号+3位递增数字，共8位数字。 首数字：2；
			// 省份区号：不同的省份区号不同，如上海为021，不足4位则在前面补0成0021； 递增数字：从001开始递增。
			else if (CherryConstants.CNTCODE_RULE2.equals(cntCodeRule)) {
				// 新增柜台画面保存时，自动生成
			}

			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
			// 取得渠道List
			channelList = binolcm00BL.getChannelList(map);
			// 取得商场List
			mallInfoList = binolcm00BL.getMallInfoList();
			// 取得经销商List
			resellerInfoList = binolcm00BL.getResellerInfoList(map);

			// 是否维护柜台密码
			form.setMaintainPassWord(binOLCM14_BL.isConfigOpen("1049",
					userInfo.getBIN_OrganizationInfoID(),
					ConvertUtil.getInt(brandInfoId)));

			// 是否支持柜台协同
			form.setMaintainCoutSynergy(binOLCM14_BL.isConfigOpen("1050",
					userInfo.getBIN_OrganizationInfoID(),
					ConvertUtil.getInt(brandInfoId)));
			// 到期日 : 默认 2100-01-01 23:59:59
			// form.setExpiringDate_date(CherryConstants.longLongAfter);
			// form.setExpiringDate_time(CherryConstants.maxTime);
		}

		return SUCCESS;
	}

	/**
	 * 根据品牌ID筛选下拉列表
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String filterByBrandInfo() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 取得区域List
		resultMap.put("reginList", binolcm00BL.getReginList(map));
		// 取得渠道List
		resultMap.put("channelList", binolcm00BL.getChannelList(map));
		// 取得商场List
		resultMap.put("mallInfoList", binolcm00BL.getMallInfoList());
		// 取得经销商List
		resultMap.put("resellerInfoList", binolcm00BL.getResellerInfoList(map));
		if (binOLCM14_BL.isConfigOpen("1009",
				String.valueOf(userInfo.getBIN_OrganizationInfoID()),
				form.getBrandInfoId())) {
			Map codeMap = code.getCode("1120", "2");
			map.put("type", "2");
			map.put("length", codeMap.get("value2"));
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLBSCNT04");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT04");
			// 自动生成柜台号
			resultMap.put("counterCode", (String) codeMap.get("value1")
					+ binOLCM15_BL.getSequenceId(map));
		}

		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}

	/**
	 * 取得柜台编号(自然堂)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getCntCode() throws Exception {
		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);

		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());

		// 取得City区号
		String teleCode = binOLBSCNT04_BL.getCntTeleCode(map);

		// 取得递增序号
		// Map codeMap = code.getCode("1120","D");
		// map.put("type", "D");
		// map.put("length", codeMap.get("value2"));
		// // 作成者
		// map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// // 更新者
		// map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// // 作成模块
		// map.put(CherryConstants.CREATEPGM, "BINOLBSCNT04");
		// // 更新模块
		// map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT04");

		StringBuffer counterCode = new StringBuffer(20);
		if (null != teleCode) {

			// 取得递增序号
			map.put("cntCodePreFive", "2" + teleCode.trim());
			String seq = binOLBSCNT04_BL.getCntCodeRightTree(map);

			counterCode.append("2").append(teleCode.trim()).append(seq);
		} else {
			counterCode.append("");
		}

		ConvertUtil.setResponseByAjax(response, counterCode.toString());

	}

	/**
	 * 
	 * 创建柜台处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String addCounterInfo() throws Exception {
		try {
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			map.put(CherryConstants.SESSION_USERINFO, userInfo);
			// 语言
			map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLBSCNT04");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT04");
			// 组织代号
			map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 登录名
			map.put("loginName", userInfo.getLoginName());
			// 到期日
			if (CherryChecker.isNullOrEmpty(form.getExpiringDateDate())
					&& CherryChecker.isNullOrEmpty(form.getExpiringDateTime())) {
				form.setExpiringDateDate(CherryConstants.longLongAfter);
				form.setExpiringDateTime(CherryConstants.maxTime);
			}
			map.put("expiringDate", form.getExpiringDateDate().trim() + " "
					+ form.getExpiringDateTime());
			// 创建柜台处理
			binOLBSCNT04_BL.tran_addCounterInfo(map);
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 
	 * 添加柜台前字段验证处理
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void validateAddCounterInfo() throws Exception {

		// 柜台编号规则为普通规则时
		if (form.getCntCodeRule().equals(CherryConstants.CNTCODE_RULE1)) {
			// 柜台号必须入力验证
			if (CherryChecker.isNullOrEmpty(form.getCounterCode())) {
				this.addFieldError(
						"counterCode",
						getText("ECM00009",
								new String[] { getText("PBS00051") }));
			} else {
				// 柜台号不能超过15位验证
				if (form.getCounterCode().length() > 15) {
					this.addFieldError(
							"counterCode",
							getText("ECM00020", new String[] {
									getText("PBS00051"), "15" }));
				}
			}
		}
		// 品牌必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId",
					getText("ECM00009", new String[] { getText("PBS00048") }));
		}
		// 测试区分必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getCounterKind())) {
			this.addFieldError("counterKind",
					getText("ECM00009", new String[] { getText("PBS00061") }));
		}
		// 柜台名称必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getCounterNameIF())) {
			this.addFieldError("counterNameIF",
					getText("ECM00009", new String[] { getText("PBS00052") }));
		} else {
			// 柜台名称不能超过50位验证
			if (form.getCounterNameIF().length() > 50) {
				this.addFieldError(
						"counterNameIF",
						getText("ECM00020", new String[] { getText("PBS00052"),
								"50" }));
			}
			// 柜台类型为测试柜台的场合，柜台名称中必须包含【测试】两个字，否则不能包含【测试】两个字
			if (form.getCounterKind() != null
					&& "1".equals(form.getCounterKind())) {
				if (!form.getCounterNameIF().contains(
						CherryConstants.COUNTERNAME_TEST)
						&& !form.getCounterNameIF().contains(
								CherryConstants.COUNTERNAME_TEST_TW)) {
					this.addFieldError("counterNameIF", getText("EBS00045"));
				}
			} else {
				if (form.getCounterNameIF().contains(
						CherryConstants.COUNTERNAME_TEST)
						|| form.getCounterNameIF().contains(
								CherryConstants.COUNTERNAME_TEST_TW)) {
					this.addFieldError("counterNameIF", getText("EBS00046"));
				}
			}
		}
		// 柜台简称不能超过20位验证
		if (form.getCounterNameShort() != null
				&& form.getCounterNameShort().length() > 20) {
			this.addFieldError(
					"counterNameShort",
					getText("ECM00020", new String[] { getText("PBS00053"),
							"20" }));
		}
		// 柜台英文名称不能超过50位验证
		if (form.getNameForeign() != null
				&& form.getNameForeign().length() > 50) {
			this.addFieldError(
					"nameForeign",
					getText("ECM00020", new String[] { getText("PBS00054"),
							"50" }));
		}
		// 银联设备号不能超过50位验证
		if (form.getEquipmentCode() != null
				&& form.getEquipmentCode().length() > 50) {
			this.addFieldError(
					"equipmentCode",
					getText("ECM00020", new String[] { getText("PBS00054"),
							"50" }));
		}
		// 电话验证
		if (form.getCounterTelephone() != null
				&& !"".equals(form.getCounterTelephone())) {
			if (!CherryChecker.isCounterTelValid(form.getCounterTelephone())) {
				this.addFieldError("counterTelephone", getText("ECM00085"));
			}
		}
		// 柜台占地面积验证
		if (form.getCounterSpace() != null
				&& !"".equals(form.getCounterSpace())) {
			if (!CherryChecker.isFloatValid(form.getCounterSpace(), 4, 2)) {
				this.addFieldError(
						"counterSpace",
						getText("ECM00024", new String[] { getText("PBS00012"),
								"4", "2" }));
			}
		}

		// 柜台员工人数验证
		if (form.getEmployeeNum() != null && !"".equals(form.getEmployeeNum())) {
			if (!CherryChecker.isNumeric(form.getEmployeeNum())) {
				this.addFieldError(
						"employeeNum",
						getText("ECM00021",
								new String[] { getText("PBS00094") }));
			}
		}

		// 到期日（年月日）
		boolean bolExpir_date = CherryChecker.isNullOrEmpty(form
				.getExpiringDateDate());
		boolean bolExpir_time = CherryChecker.isNullOrEmpty(form
				.getExpiringDateTime());

		if (bolExpir_date && !bolExpir_time) {
			this.addFieldError("expiringDateTime",
					getText("ECM00097", new String[] { getText("PBS00078") }));
		}
		if (!bolExpir_date && bolExpir_time) {
			this.addFieldError("expiringDateTime",
					getText("ECM00097", new String[] { getText("PBS00078") }));
		}

		// // 到期日（年月日）
		// if(form.getExpiringDateDate() != null &&
		// !"".equals(form.getExpiringDateDate())){
		// if(!CherryChecker.checkDate(form.getExpiringDateDate().trim())){
		// // 日期格式验证
		// this.addFieldError("expiringDate_date", getText("ECM00022",new
		// String[] { getText("PBS00076") }));
		// }
		// }
		// // 到期日（时分秒）
		// if(form.getExpiringDateTime() != null &&
		// !"".equals(form.getExpiringDateTime())){
		// if(!CherryChecker.checkTime(form.getExpiringDateTime())){
		// // 时间格式验证
		// this.addFieldError("expiringDate_time", getText("ECM00026",new
		// String[] { getText("PBS00077") }));
		// }
		// }
		// 柜台地址不能超过100位验证
		if (form.getCounterAddress() != null
				&& form.getCounterAddress().length() > 100) {
			this.addFieldError(
					"counterAddress",
					getText("ECM00020", new String[] { getText("PBS00056"),
							"100" }));
		}
		// 省份必须入力验证
		if (form.getProvinceId() == null || "".equals(form.getProvinceId())) {
			this.addFieldError("provinceId", getText("EBS00043"));
		}
		// 城市必须入力验证
		if (form.getCityId() == null || "".equals(form.getCityId())) {
			this.addFieldError("cityId", getText("EBS00043"));
		} else {
			// 柜台编号规则为自然堂规则时，检查城市区号是否存在
			if (form.getCntCodeRule().equals(CherryConstants.CNTCODE_RULE2)) {

				// 柜台编号为空，说明通过城市Id没有取得柜台编号
				if (CherryChecker.isNullOrEmpty(form.getCounterCode())) {
					this.addFieldError("cityId", getText("EBS00115"));
				}

			}

		}

		// 柜台密码长度不能超过15
		if (form.getPassWord() != null && form.getPassWord().length() > 15
				|| form.getPassWord() != null
				&& form.getPassWord().getBytes().length > 15) {
			this.addFieldError(
					"passWord",
					getText("ECM00020", new String[] { getText("PBS00068"),
							"15" }));
		}
		// 是否有pos机必须校检
		if (CherryChecker.isNullOrEmpty(form.getPosFlag())) {
			this.addFieldError("posFlag",
					getText("ECM00054", new String[] { getText("PBS00095") }));
		}
		if (!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 柜台号
			map.put("counterCode", form.getCounterCode());
			// 柜台号唯一验证
			String counterCode = binOLBSCNT04_BL.getCounterInfoId(map);
			if (counterCode != null && !"".equals(counterCode)) {
				this.addFieldError("counterCode", getText("EBS00022"));
			}
		}
		// 业务负责人不能超过50位验证
				if (form.getBusniessPrincipal() != null
						&& form.getBusniessPrincipal().length() > 50) {
					this.addFieldError(
							"busniessPrincipa1",
							getText("ECM00020", new String[] { getText("PBS00098"),
									
									"50" }));
				}
	}

	/**
	 * 取得城市区号
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getTeleCode() throws Exception {
		String teleCode = null;

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);

		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());

		// 取得City区号
		teleCode = binOLBSCNT04_BL.getCntTeleCode(map);

		return teleCode;
	}

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 区域List */
	private List<Map<String, Object>> reginList;

	/** 渠道List */
	private List<Map<String, Object>> channelList;

	/** 商场List */
	private List<Map<String, Object>> mallInfoList;

	/** 经销商List */
	private List<Map<String, Object>> resellerInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

	public List<Map<String, Object>> getMallInfoList() {
		return mallInfoList;
	}

	public void setMallInfoList(List<Map<String, Object>> mallInfoList) {
		this.mallInfoList = mallInfoList;
	}

	public List<Map<String, Object>> getResellerInfoList() {
		return resellerInfoList;
	}

	public void setResellerInfoList(List<Map<String, Object>> resellerInfoList) {
		this.resellerInfoList = resellerInfoList;
	}

	@Override
	public BINOLBSCNT04_Form getModel() {
		return form;
	}

}
