/*
 * @(#)BINOLMBMBM11_Action.java     1.0 2013/03/05
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
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM09_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM11_Form;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mq.mes.bl.BINBEMQMES03_BL;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加会员画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/03/05
 */
public class BINOLMBMBM11_Action extends BaseAction implements ModelDriven<BINOLMBMBM11_Form> {
	
	private static final long serialVersionUID = 9180229099555103824L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM11_Action.class);
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	/** 会员搜索画面BL */
	@Resource
	private BINOLMBMBM09_BL binOLMBMBM09_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 会员消息数据接收处理BL **/
	@Resource
	private BINBEMQMES03_BL binBEMQMES03_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	/**
	 * 添加会员画面初期处理
	 * 
	 * @return 添加会员画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 取得会员扩展信息List
		extendPropertyList = binOLMBMBM09_BL.getExtendProperty(map);
		
		// 查询会员等级信息List
		memLevelList = binOLMBMBM09_BL.getMemberLevelInfoList(map);
		
//		// 取得区域List
//		reginList = binOLCM08_BL.getReginList(map);
		
		regionJson = binOLCM08_BL.getRegionJson();
		
		sysDate = binOLMBMBM11_Service.getDateYMD();
		return SUCCESS;
	}
	
	/**
	 * 添加会员处理
	 * 
	 * @return 添加会员画面
	 */
	public String add() throws Exception {
		try {
			if(form.getMemLevelConfirm() == null || "".equals(form.getMemLevelConfirm())) {
				if(form.getOldMemberLevel() != null && !"".equals(form.getOldMemberLevel()) 
						&& form.getMemberLevel() != null && !"".equals(form.getMemberLevel())) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("memLevelConfirm", "1");
					resultMap.put("memLevelWarnInfo", getText("EMB00018", new String[]{form.getOldLevelName()}));
					ConvertUtil.setResponseByAjax(response, resultMap);
					return null;
				}
			}
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
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM11");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM11");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			// 添加会员信息
			binOLMBMBM11_BL.tran_addMemberInfo(map);
			this.addActionMessage(getText("ICM00001"));
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加会员前字段验证处理
	 * 
	 */
	public void validateAdd() throws Exception {
		
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
				if(form.getReferrer().equals(form.getMemCode())) {
					this.addFieldError("referrer", getText("EMB00023"));
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
			// 通过会员卡号查询会员信息
			Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
			if(memberInfoMap != null) {
				Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
				if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
					Object memberLevel = memberInfoMap.get("memberLevel");
					if(memberLevel != null) {
						String levelName = (String)memberInfoMap.get("levelName");
						form.setOldLevelName(levelName);
						form.setOldMemberLevel(memberLevel.toString());
					}
					String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
					form.setMemberInfoId(memberInfoId);
				} else {
					this.addFieldError("memCode", getText("EMB00015"));
				}
			}
		}
	}
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 会员扩展信息List */
	private List<Map<String, Object>> extendPropertyList;
	
	/** 会员等级List */
	private List<Map<String, Object>> memLevelList;
	
	/** 系统时间 */
	private String sysDate;
	
	/** 区域Json */
	private String regionJson;
	
	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<Map<String, Object>> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public List<Map<String, Object>> getMemLevelList() {
		return memLevelList;
	}

	public void setMemLevelList(List<Map<String, Object>> memLevelList) {
		this.memLevelList = memLevelList;
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

	/** 添加会员画面Form */
	private BINOLMBMBM11_Form form = new BINOLMBMBM11_Form();

	@Override
	public BINOLMBMBM11_Form getModel() {
		return form;
	}

}
