/*
 * @(#)BINOLMBMBM06_Action.java     1.0 2012.11.27
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM06_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM06_Form;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员资料修改画面Action
 * 
 * @author WangCT
 * @version 1.0 2012.11.27
 */
public class BINOLMBMBM06_Action extends BaseAction implements ModelDriven<BINOLMBMBM06_Form> {
	
	private static final long serialVersionUID = 4403240720565312131L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM06_Action.class);
	
	/** 会员资料修改画面BL **/
	@Resource
	private BINOLMBMBM06_BL binOLMBMBM06_BL;
	
	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	private String clubMod;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * 会员资料修改画面初期显示
	 * 
	 * @return 会员资料修改画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 所属品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 取得会员信息
		memberInfoMap = binOLMBMBM06_BL.getMemberInfo(map);
		
		// 取得会员扩展信息List
		extendPropertyList = (List)memberInfoMap.get("memPagerList");
		
//		// 取得区域List
//		reginList = binOLCM08_BL.getReginList(map);
//		// 会员地址信息
//		Map<String, Object> memberAddressInfo = (Map)memberInfoMap.get("memberAddressInfo");
//		if(memberAddressInfo != null) {
//			// 省ID
//			Object provinceId = memberAddressInfo.get("provinceId");
//			// 省存在的场合，取得城市List
//			if(provinceId != null && !"".equals(provinceId.toString())) {
//				// 区域Id
//				map.put("regionId", provinceId);
//				cityList = binOLCM08_BL.getAllChildStandRegionList(map);
//			}
//		}
		
		regionJson = binOLCM08_BL.getRegionJson();
		
		// 取得是否可编辑入会日期配置项
		if(binOLCM14_BL.isConfigOpen("1046", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID())) {
			if(binOLCM14_BL.isConfigOpen("1047", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID())) {
				editJoinDateFlag="1";
			} else {
				editJoinDateFlag="2";
			}
		} else {
			editJoinDateFlag="0";
		}
		// 修改会员卡号时是否将手机号关联更改为卡号
		memCardMobileSameFlag = binOLCM14_BL.getConfigValue("1320",
				ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),
				ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		
		sysDate = binOLMBMBM11_Service.getDateYMD();
		// 俱乐部模式
		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				// 取得会员已经拥有的俱乐部列表
				clubList = binOLCM05_BL.getMemClubList(map);
				if (null == clubList || clubList.isEmpty()) {
					clubList = clubInfoList;
				} else {
					for (Map<String, Object> clubMap : clubList) {
						// 会员俱乐部ID
						String memberClubId = String.valueOf(clubMap.get("memberClubId"));
						for (int i = 0; i < clubInfoList.size(); i++) {
							Map<String, Object> clubInfo = clubInfoList.get(i);
							if (memberClubId.equals(String.valueOf(clubInfo.get("memberClubId")))) {
								clubMap.putAll(clubInfo);
								clubInfoList.remove(i);
								break;
							}
						}
					}
					clubList.addAll(clubInfoList);
				}
				map.put("memberClubId", clubList.get(0).get("memberClubId"));
				// 查询会员俱乐部信息
				memClubMap = binOLMBMBM06_BL.getMemClubInfo(map);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 切换俱乐部
	 * 
	 * @return 俱乐部信息
	 */
	public String changeClub() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", form.getMemberInfoId());
		map.put("memberClubId", form.getMemberClubId());
		// 查询会员俱乐部信息
		memClubMap = binOLMBMBM06_BL.getMemClubInfo(map);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 保存是否接收短信
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 是否接收短信
	 * @throws Exception 
	 * 
	 */
    public String saveRecMsg() throws Exception{
    	Map<String, Object> resultInfo = new HashMap<String, Object>();
    	try {
    		// 验证提交的参数
			if (!validateForm()) {
				resultInfo.put("RESULT", "NG");
				// 响应JSON对象
				ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultInfo));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
	    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			map.put("memberClubID", form.getMemberClubId());
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM06");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM06");
			// 操作员工
			map.put("employeeCode", userInfo.getEmployeeCode());
			map.put("employeeID", userInfo.getBIN_EmployeeID());
			// 更新会员俱乐部信息
			binOLMBMBM06_BL.tran_updMemClubInfo(map);
    	} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	resultInfo.put("RESULT", "OK");
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultInfo));
    	return null;
    }
	
	/**
	 * 
	 * 会员资料修改处理
	 * 
	 */
	public String update() throws Exception {
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM06");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM06");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			// 更新会员基本信息
			binOLMBMBM06_BL.tran_updMemberInfo(map);
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
   	 * 验证提交的参数
   	 * 
   	 * @param 无
   	 * @return boolean 验证结果
   	 * @throws Exception
   	 * 
   	 */
   	private boolean validateForm() throws Exception {
   		boolean result = true;
	   	// 推荐会员卡号不能超过20位验证
		if(form.getReferrerClub() != null && !"".equals(form.getReferrerClub())) {
			if(form.getReferrerClub().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
				result = false;
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 推荐会员卡号
				map.put("memCode", form.getReferrerClub());
				// 推荐会员卡号是否存在验证
				String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
				if(memberInfoId == null || "".equals(memberInfoId)) {
					this.addFieldError("referrerClub", getText("EMB00016"));
					result = false;
				} else {
					if(memberInfoId.equals(form.getMemberInfoId())) {
						this.addFieldError("referrerClub", getText("EMB00023"));
						result = false;
					}
				}
			}
		}
		// 发卡柜台必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOrganizationIdClub())) {
			result = false;
			this.addFieldError("organizationIdClub", getText("ECM00009",new String[]{getText("PMB01007")}));
		}
		// 发卡BA必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getEmployeeIdClub())) {
			result = false;
			this.addFieldError("employeeIdClub", getText("ECM00009",new String[]{getText("PMB01008")}));
		}
		// 发卡日期必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getJoinTimeClub())) {
			result = false;
			this.addFieldError("joinTimeClub", getText("ECM00009",new String[]{getText("PMB01009")}));
		} else {
			// 发卡日期日期格式验证
			if(!CherryChecker.checkDate(form.getJoinTimeClub())) {
				result = false;
				this.addFieldError("joinTimeClub", getText("ECM00022",new String[]{getText("PMB01009")}));
			}
			// 发卡日期不能大于今天
			if(CherryChecker.compareDate(form.getJoinTimeClub(), binOLMBMBM11_Service.getDateYMD()) > 0) {
				result = false;
				this.addFieldError("joinTimeClub", getText("ECM000102",new String[]{getText("PMB01009")}));
			}
		}
		return result;
   	}
	
	/**
	 * 
	 * 会员资料修改前字段验证处理
	 * 
	 */
	public void validateUpdate() throws Exception {
		
		// 会员卡号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemCode())) {
			this.addFieldError("memCode", getText("ECM00009",new String[]{getText("PMB00055")}));
		} else {
			// 会员卡号不能超过20位验证
			if(form.getMemCode().length() > 20) {
				this.addFieldError("memCode", getText("ECM00020",new String[]{getText("PMB00055"),"20"}));
			} else {
				boolean checkResult = true;
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				String memCodeRule = binOLCM14_BL.getConfigValue("1070", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
				if(memCodeRule != null && !"".equals(memCodeRule)) {
					if (!form.getMemCode().matches(memCodeRule)){
			    		this.addFieldError("memCode", getText("EMB00017"));
			    		checkResult = false;
			    	}
				}
				if(checkResult) {
					String memCodeFunName = binOLCM14_BL.getConfigValue("1133", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					if(memCodeFunName != null && !"".equals(memCodeFunName)) {
						if(!CherryChecker.checkMemCodeByFun(memCodeFunName, form.getMemCode())) {
							this.addFieldError("memCode", getText("EMB00017"));
							checkResult = false;
						}
					}
				}
			}
		}
		// 会员昵称不能超过30位验证
		if(form.getNickname() != null && form.getNickname().length() > 30) {
			this.addFieldError("nickname", getText("ECM00020",new String[]{getText("PMB00083"),"30"}));
		}
		// 会员姓名必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemName())) {
			this.addFieldError("memName", getText("ECM00009",new String[]{getText("PMB00049")}));
		} else {
			// 会员姓名不能超过30位验证
			if(form.getMemName().length() > 30) {
				this.addFieldError("memName", getText("ECM00020",new String[]{getText("PMB00049"),"30"}));
			}
		}
		// 发卡柜台必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOrganizationId())) {
			this.addFieldError("organizationId", getText("ECM00009",new String[]{getText("PMB00062")}));
		}
		// 发卡BA必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getEmployeeId())) {
			this.addFieldError("employeeId", getText("ECM00009",new String[]{getText("PMB00063")}));
		}
		// 发卡日期必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getJoinDate())) {
			this.addFieldError("joinDate", getText("ECM00009",new String[]{getText("PMB00056")}));
		} else {
			// 发卡日期日期格式验证
			if(!CherryChecker.checkDate(form.getJoinDate())) {
				this.addFieldError("joinDate", getText("ECM00022",new String[]{getText("PMB00056")}));
			}
			// 发卡日期不能大于今天
			if(CherryChecker.compareDate(form.getJoinDate(), binOLMBMBM11_Service.getDateYMD()) > 0) {
				this.addFieldError("joinDate", getText("ECM000102",new String[]{getText("PMB00056")}));
			}
		}
		// 手机验证
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			boolean isCheck = true;
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			if(mobileRule != null && !"".equals(mobileRule)) {
				if (!form.getMobilePhone().matches(mobileRule)){
		    		this.addFieldError("mobilePhone", getText("ECM00086"));
		    		isCheck = false;
		    	}
			}
			if(isCheck) {
				if(form.getMobilePhoneOld() == null || !form.getMobilePhone().equals(form.getMobilePhoneOld())) {
					if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 所属组织
						map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
						// 所属品牌
						map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
						// 会员手机号
						map.put("mobilePhone", form.getMobilePhone());
						List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
						if(memMobileList != null && !memMobileList.isEmpty()) {
							this.addFieldError("mobilePhone", getText("MBM00052"));
						}
					}
				}
			}
		}
		// 电话验证
		if(form.getTelephone() != null && !"".equals(form.getTelephone())) {
			if(!CherryChecker.isTelValid(form.getTelephone())) {
				this.addFieldError("telephone", getText("ECM00085"));
			}
		}
		// 手机和电话必须填一项验证
		if((form.getMobilePhone() == null || "".equals(form.getMobilePhone())) 
				&& (form.getTelephone() == null || "".equals(form.getTelephone()))) {
			this.addFieldError("mobilePhone", getText("ECM00087"));
		}
		// 会员QQ不能超过20位验证
		if(form.getTencentQQ() != null && form.getTencentQQ().length() > 20) {
			this.addFieldError("tencentQQ", getText("ECM00020",new String[]{"QQ","20"}));
		}
		// 邮箱验证
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("ECM00069"));
			}
		}
		// 生日必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBirth())) {
			this.addFieldError("birth", getText("ECM00009",new String[]{getText("PMB00051")}));
		} else {
			// 生日日期格式验证
			if(!CherryChecker.checkDate(form.getBirth())) {
				this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
			}
		}
		// 性别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getGender())) {
			this.addFieldError("gender", getText("ECM00009",new String[]{getText("PMB00064")}));
		}
		// 初始累计金额验证
		if(form.getInitTotalAmount() != null && !"".equals(form.getInitTotalAmount())) {
			if(!CherryChecker.isFloatValid(form.getInitTotalAmount(), 16, 2)) {
				this.addFieldError("initTotalAmount", getText("ECM00024",new String[]{getText("PMB00071"),"16","2"}));
			}
		}
		// 推荐会员卡号不能超过20位验证
		if(form.getReferrer() != null && !"".equals(form.getReferrer())) {
			if(form.getReferrer().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 推荐会员卡号
				map.put("memCode", form.getReferrer());
				// 推荐会员卡号是否存在验证
				String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
				if(memberInfoId == null || "".equals(memberInfoId)) {
					this.addFieldError("referrer", getText("EMB00016"));
				} else {
					if(memberInfoId.equals(form.getMemberInfoId())) {
						this.addFieldError("referrer", getText("EMB00023"));
					}
				}
			}
		}
		// 身份证不能超过18位验证
		if(form.getIdentityCard() != null && !"".equals(form.getIdentityCard())) {
			if(form.getIdentityCard().length() > 18) {
				this.addFieldError("identityCard", getText("ECM00020",new String[]{getText("PMB00054"),"18"}));
			}
		}
		// 微博号不能超过30位验证
		if(form.getBlogId() != null && !"".equals(form.getBlogId())) {
			if(form.getBlogId().length() > 30) {
				this.addFieldError("blogId", getText("ECM00020",new String[]{getText("PMB00058"),"30"}));
			}
		}
		// 微信号不能超过30位验证
		if(form.getMessageId() != null && !"".equals(form.getMessageId())) {
			if(form.getMessageId().length() > 30) {
				this.addFieldError("messageId", getText("ECM00020",new String[]{getText("PMB00059"),"30"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo1() != null && !"".equals(form.getMemo1())) {
			if(form.getMemo1().length() > 512) {
				this.addFieldError("memo1", getText("ECM00020",new String[]{getText("PMB00070"),"512"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo2() != null && !"".equals(form.getMemo2())) {
			if(form.getMemo2().length() > 512) {
				this.addFieldError("memo2", getText("ECM00020",new String[]{getText("PMB00072"),"512"}));
			}
		}
		// 会员地址不能超过200位验证
		if(form.getAddress() != null && !"".equals(form.getAddress())) {
			if(form.getAddress().length() > 200) {
				this.addFieldError("address", getText("ECM00020",new String[]{getText("PMB00060"),"200"}));
			}
		}
		// 邮编不能超过10位验证
		if(form.getPostcode() != null && !"".equals(form.getPostcode())) {
			if(form.getPostcode().length() > 10) {
				this.addFieldError("postcode", getText("ECM00020",new String[]{getText("PMB00061"),"10"}));
			}
		}
		if(!this.hasFieldErrors()) {
			// 换卡的场合，新卡唯一验证
			if(!form.getMemCodeOld().equals(form.getMemCode())) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员卡号
				map.put("memCode", form.getMemCode());
				boolean isCheck = true;
				// 通过会员卡号查询会员信息
				Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
				if(memberInfoMap != null) {
					// 不管是否是"资料缺失会员"，只要卡号存在都不允许换卡
					/*
					Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
					if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
						form.setNewMemberInfoId(memberInfoId);
						Object newAddressInfoId = memberInfoMap.get("addressInfoId");
						if(newAddressInfoId != null) {
							form.setNewAddressInfoId(newAddressInfoId.toString());
						}
					} else {
//						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
//						if(form.getMemberInfoId().equals(memberInfoId)) {
//							form.setNewMemberInfoId(memberInfoId);
//						} else {
//							this.addFieldError("memCode", getText("EMB00015"));
//						}
						this.addFieldError("memCode", getText("EMB00015"));
						isCheck = false;
					}
					*/
					this.addFieldError("memCode", getText("EMB00015"));
					isCheck = false;
				}
				if(isCheck) {
					// 根据系统配置项判断在换卡时是否将手机号关联更改为卡号
					if(binOLCM14_BL.isConfigOpen("1320", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID())) {
						// 手机号联动更改为新卡号
						form.setMobilePhone(form.getMemCode());
						// 会员卡号与手机号联动，所以会员卡号应为手机格式
						if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
							boolean isCheckOnly = true;
							String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
							if(mobileRule != null && !"".equals(mobileRule)) {
								if (!form.getMobilePhone().matches(mobileRule)){
									// 更改后的会员卡号必须为手机号格式
						    		this.addFieldError("memCode", getText("ECM000104"));
						    		isCheckOnly = false;
						    	}
							}
							if(isCheckOnly) {
								if(form.getMobilePhoneOld() == null || !form.getMobilePhone().equals(form.getMobilePhoneOld())) {
									// 手机号唯一性验证
									if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
										Map<String, Object> paramMap = new HashMap<String, Object>();
										// 所属组织
										paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
										// 所属品牌
										paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
										// 会员手机号
										paramMap.put("mobilePhone", form.getMobilePhone());
										List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(paramMap);
										if(memMobileList != null && !memMobileList.isEmpty()) {
											// 该会员卡号作为手机号已存在
											this.addFieldError("memCode", getText("MBM00055"));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 城市List */
	private List<Map<String, Object>> cityList;
	
	/** 会员扩展信息List */
	private List<Map<String, Object>> extendPropertyList;
	
	/** 会员基本信息 */
	private Map memberInfoMap;
	
	/** 会员俱乐部信息 */
	private Map memClubMap;
	
	/** 是否可编辑入会日期 */
	private String editJoinDateFlag;
	
	/**会员卡号与手机号联动标识*/
	private String memCardMobileSameFlag;
	
	/** 系统时间 */
	private String sysDate;
	
	/** 区域Json */
	private String regionJson;
	
	public Map getMemberInfoMap() {
		return memberInfoMap;
	}

	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}
	
	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<Map<String, Object>> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public String getEditJoinDateFlag() {
		return editJoinDateFlag;
	}

	public void setEditJoinDateFlag(String editJoinDateFlag) {
		this.editJoinDateFlag = editJoinDateFlag;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getRegionJson() {
		return regionJson;
	}

	public void setRegionJson(String regionJson) {
		this.regionJson = regionJson;
	}

	/** 会员资料修改画面Form */
	private BINOLMBMBM06_Form form = new BINOLMBMBM06_Form();

	@Override
	public BINOLMBMBM06_Form getModel() {
		return form;
	}
	
	public Map getMemClubMap() {
		return memClubMap;
	}

	public void setMemClubMap(Map memClubMap) {
		this.memClubMap = memClubMap;
	}

	public String getMemCardMobileSameFlag() {
		return memCardMobileSameFlag;
	}

	public void setMemCardMobileSameFlag(String memCardMobileSameFlag) {
		this.memCardMobileSameFlag = memCardMobileSameFlag;
	}

}
