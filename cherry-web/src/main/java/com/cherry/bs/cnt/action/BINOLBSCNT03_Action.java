/*	
 * @(#)BINOLBSCNT03_Action.java     1.0 2011/05/09		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.cnt.bl.BINOLBSCNT02_BL;
import com.cherry.bs.cnt.bl.BINOLBSCNT03_BL;
import com.cherry.bs.cnt.bl.BINOLBSCNT04_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT03_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 	更新柜台画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
@SuppressWarnings("unchecked")
public class BINOLBSCNT03_Action extends BaseAction implements ModelDriven<BINOLBSCNT03_Form> {

	private static final long serialVersionUID = -2915965965604295687L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT03_Action.class);
	
	/** 更新柜台画面Form */
	private BINOLBSCNT03_Form form = new BINOLBSCNT03_Form();
	
	/** 更新柜台画面BL */
	@Resource(name="binOLBSCNT03_BL")
	private BINOLBSCNT03_BL binOLBSCNT03_BL;
	
	/** 柜台详细画面BL */
	@Resource(name="binOLBSCNT02_BL")
	private BINOLBSCNT02_BL binOLBSCNT02_BL;
	
	/** 创建柜台画面BL */
	@Resource(name="binOLBSCNT04_BL")
	private BINOLBSCNT04_BL binOLBSCNT04_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String 更新柜台画面
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 柜台ID
		map.put("counterInfoId", form.getCounterInfoId());
		// 取得柜台详细信息
		counterInfo = binOLBSCNT02_BL.getCounterInfo(map);
		
		if(counterInfo != null && !counterInfo.isEmpty()) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, counterInfo.get(CherryConstants.BRANDINFOID));
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
			// 取得渠道List
			channelList = binolcm00BL.getChannelList(map);
			// 取得商场List
			mallInfoList = binolcm00BL.getMallInfoList();
			// 取得经销商List
			resellerInfoList = binolcm00BL.getResellerInfoList(map);
			// 取得省ID
			Object provinceId = (Object)counterInfo.get("provinceId");
			// 取得城市ID
			Object cityId = (Object)counterInfo.get("cityId");
			// 省存在的场合，取得城市List
			if(provinceId != null && !"".equals(provinceId.toString())) {
				// 区域Id
				map.put("regionId", provinceId);
				cityList = binolcm00BL.getChildRegionList(map);
			}
			// 城市存在的场合，取得县级市List
			if(cityId != null && !"".equals(cityId.toString())) {
				// 区域Id
				map.put("regionId", cityId);
				countyList = binolcm00BL.getChildRegionList(map);
			}
			
			//是否维护柜台密码
			form.setMaintainPassWord(binOLCM14_BL.isConfigOpen("1049", userInfo.getBIN_OrganizationInfoID(), (Integer)counterInfo.get(CherryConstants.BRANDINFOID)));
			
			//是否支持柜台协同
			form.setMaintainCoutSynergy(binOLCM14_BL.isConfigOpen("1050", userInfo.getBIN_OrganizationInfoID(), (Integer)counterInfo.get(CherryConstants.BRANDINFOID)));
		} else {
			this.addActionError(getText("EBS00020"));
            return SUCCESS;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 更新部门信息处理
	 * 
	 * @return 更新完了画面
	 */
	public String updateCounterInfo() throws Exception {
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			map.put(CherryConstants.SESSION_USERINFO, userInfo);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			//语言
			map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLBSCNT03");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT03");
			// 组织代号
			map.put("orgCode", userInfo.getOrganizationInfoCode());
			//登录名
			map.put("loginName", userInfo.getLoginName());
			// 到期日
			if(CherryChecker.isNullOrEmpty(form.getExpiringDateDate()) && CherryChecker.isNullOrEmpty(form.getExpiringDateTime())){
				form.setExpiringDateDate(CherryConstants.longLongAfter);
				form.setExpiringDateTime(CherryConstants.maxTime);
			}
			map.put("expiringDate", form.getExpiringDateDate().trim() + " " + form.getExpiringDateTime());
			// 更新部门信息
			binOLBSCNT03_BL.tran_updateCounterInfo(map);
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
            }   
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 更新柜台前字段验证处理
	 * 
	 */
	public void validateUpdateCounterInfo() throws Exception {
		
		// 柜台号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCounterCode())) {
			this.addFieldError("counterCode", getText("ECM00009",new String[]{getText("PBS00051")}));
		} else {
			// 柜台号不能超过15位验证
			if(form.getCounterCode().length() > 15) {
				this.addFieldError("counterCode", getText("ECM00020",new String[]{getText("PBS00051"),"15"}));
			}
		}
		// 品牌必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId", getText("ECM00009",new String[]{getText("PBS00048")}));
		}
		// 测试区分必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCounterKind())) {
			this.addFieldError("counterKind", getText("ECM00009",new String[]{getText("PBS00061")}));
		}
		// 柜台名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCounterNameIF())) {
			this.addFieldError("counterNameIF", getText("ECM00009",new String[]{getText("PBS00052")}));
		} else {
			// 柜台名称不能超过50位验证
			if(form.getCounterNameIF().length() > 50) {
				this.addFieldError("counterNameIF", getText("ECM00020",new String[]{getText("PBS00052"),"50"}));
			}
			// 柜台类型为测试柜台的场合，柜台名称中必须包含【测试】两个字，否则不能包含【测试】两个字
			if(form.getCounterKind() != null && "1".equals(form.getCounterKind())) {
				if(!form.getCounterNameIF().contains(CherryConstants.COUNTERNAME_TEST) && !form.getCounterNameIF().contains(CherryConstants.COUNTERNAME_TEST_TW)) {
					this.addFieldError("counterNameIF", getText("EBS00045"));
				}
			} else {
				if(form.getCounterNameIF().contains(CherryConstants.COUNTERNAME_TEST) || form.getCounterNameIF().contains(CherryConstants.COUNTERNAME_TEST_TW)) {
					this.addFieldError("counterNameIF", getText("EBS00046"));
				}
			}
		}
		// 柜台简称不能超过20位验证
		if(form.getCounterNameShort() != null && form.getCounterNameShort().length() > 20) {
			this.addFieldError("counterNameShort", getText("ECM00020",new String[]{getText("PBS00053"),"20"}));
		}
		// 柜台英文名称不能超过50位验证
		if(form.getNameForeign() != null && form.getNameForeign().length() > 50) {
			this.addFieldError("nameForeign", getText("ECM00020",new String[]{getText("PBS00054"),"50"}));
		}
		// 银联设备号不能超过50位验证
		if(form.getEquipmentCode() != null && form.getEquipmentCode().length() > 50) {
			this.addFieldError("equipmentCode", getText("ECM00020",new String[]{getText("PBS00054"),"50"}));
		}
		// 电话验证
		if(form.getCounterTelephone() != null && !"".equals(form.getCounterTelephone())) {
			if(!CherryChecker.isCounterTelValid(form.getCounterTelephone())) {
				this.addFieldError("counterTelephone", getText("ECM00085"));
			}
		}
		// 柜台占地面积验证
		if(form.getCounterSpace() != null && !"".equals(form.getCounterSpace())) {
			if(!CherryChecker.isFloatValid(form.getCounterSpace(),4,2)) {
				this.addFieldError("counterSpace", getText("ECM00024",new String[]{getText("PBS00012"),"4","2"}));
			}
		}
		
		// 柜台员工人数验证
		if(form.getEmployeeNum() != null && !"".equals(form.getEmployeeNum())) {
			if(!CherryChecker.isNumeric(form.getEmployeeNum())) {
				this.addFieldError("employeeNum", getText("ECM00021",new String[]{getText("PBS00094")}));
			}
		}
		// 到期日（年月日）
		boolean bolExpir_date = CherryChecker.isNullOrEmpty(form.getExpiringDateDate());
		boolean bolExpir_time = CherryChecker.isNullOrEmpty(form.getExpiringDateTime());
		
		if(bolExpir_date && !bolExpir_time){
			this.addFieldError("expiringDateTime", getText("ECM00097",new String[] { getText("PBS00078") }));
		}
		if(!bolExpir_date && bolExpir_time){
			this.addFieldError("expiringDateTime", getText("ECM00097",new String[] { getText("PBS00078") }));
		}
		
//		// 到期日（年月日）
//		if(form.getExpiringDateDate() != null && !"".equals(form.getExpiringDateDate().trim())){
//			if(!CherryChecker.checkDate(form.getExpiringDateDate().trim())){
//				// 日期格式验证
//				this.addFieldError("expiringDate_date", getText("ECM00022",new String[] { getText("PBS00076") }));
//			}
//		}
//		// 到期日（时分秒）
//		if(form.getExpiringDateTime() != null && !"".equals(form.getExpiringDateTime().trim())){
//			if(!CherryChecker.checkTime(form.getExpiringDateTime().trim())){
//				// 时间格式验证
//				this.addFieldError("expiringDate_time", getText("ECM00026",new String[] { getText("PBS00077") }));
//			}
//		}
		// 柜台地址不能超过100位验证
		if(form.getCounterAddress() != null && form.getCounterAddress().length() > 100) {
			this.addFieldError("counterAddress", getText("ECM00020",new String[]{getText("PBS00056"),"100"}));
		}
		// 省份必须入力验证
		if(form.getProvinceId() == null || "".equals(form.getProvinceId())) {
				this.addFieldError("provinceId", getText("EBS00043"));
		}
		// 城市必须入力验证
		if(form.getCityId() == null || "".equals(form.getCityId())) {
			this.addFieldError("cityId", getText("EBS00043"));
		}
		//柜台密码长度不能超过15
		if(form.getPassWord() != null && form.getPassWord().length() > 15||form.getPassWord() != null && form.getPassWord().getBytes().length >15){
			this.addFieldError("passWord", getText("ECM00020",new String[]{getText("PBS00068"),"15"}));
		}
		//是否有pos机必须校检
		if(CherryChecker.isNullOrEmpty(form.getPosFlag())){
			this.addFieldError("posFlag", getText("ECM00054",new String[] { getText("PBS00095") }));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 柜台号
			map.put("counterCode", form.getCounterCode());
			// 柜台号唯一验证
			String counterCode = binOLBSCNT04_BL.getCounterInfoId(map);
			if(counterCode != null && !form.getCounterInfoId().equals(counterCode)) {
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

		//柜台所属名称不存在
		if(ConvertUtil.isBlank(form.getBelongFaction()) && !ConvertUtil.isBlank(form.getBelongFactionName())){
			this.addFieldError("belongFactionName", getText("EBS00122"));
		}
	}
	
	/** 柜台详细信息 */
	private Map counterInfo;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	/** 商场List */
	private List<Map<String, Object>> mallInfoList;
	
	/** 经销商List */
	private List<Map<String, Object>> resellerInfoList;
	
	/** 城市List */
	private List<Map<String, Object>> cityList;
	
	/** 县级市List */
	private List<Map<String, Object>> countyList;

	public Map getCounterInfo() {
		return counterInfo;
	}

	public void setCounterInfo(Map counterInfo) {
		this.counterInfo = counterInfo;
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

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<Map<String, Object>> countyList) {
		this.countyList = countyList;
	}

	@Override
	public BINOLBSCNT03_Form getModel() {
		return form;
	}

}
