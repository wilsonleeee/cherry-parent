/*		
 * @(#)BINOLSSPRM73_Action.java     1.0 2016/03/28		
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
package com.cherry.ss.prm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM99_BL;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.BillInfo;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.form.BINOLSSPRM73_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.rule.CouponEngine;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 优惠券规则Action
 * @author hub
 * @version 1.0 2016.03.28
 */
public class BINOLSSPRM73_Action extends BaseAction implements ModelDriven<BINOLSSPRM73_Form>{

	private static final long serialVersionUID = 5565301651534313429L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM73_Action.class);

	/** 参数FORM */
	private BINOLSSPRM73_Form form = new BINOLSSPRM73_Form();
	
	/** 优惠券规则BL */
	@Resource
	private BINOLSSPRM73_IF binOLSSPRM73_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private CouponEngine coupEngine;
	
	@Resource(name="coupon_IF")
	private BINOLSSPRM99_BL coupon_IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Override
	public BINOLSSPRM73_Form getModel() {
		return form;
	}
	
	private List<Map<String, Object>> ruleList;
	
	
	public List<Map<String, Object>> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Map<String, Object>> ruleList) {
		this.ruleList = ruleList;
	}
	

	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public String init () throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = binOLCM05_BL.getBrandInfoList(map);
			form.setBrandList(brandList);
		} else {
			form.setBrandInfoId(String.valueOf(brandInfoId));
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX查询优惠券规则
	 * 
	 * @return 查询优惠券规则画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 取得优惠券规则总数
		int count = binOLSSPRM73_BL.getRuleInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得优惠券规则List
			ruleList = binOLSSPRM73_BL.getRuleInfoList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑画面初始显示
	 * 
	 * @return 查询积分信息画面
	 */
	public String editInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", form.getRuleCode());
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		try {
			// 使用券时间JSON
			String useTimeJson = couponRule.getUseTimeJson();
			if (!CherryChecker.isNullOrEmpty(useTimeJson)) {
				Map<String, Object> useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTimeJson);
				// 使用券时间模式
				String useTimeType = (String) useTimeInfo.get("useTimeType");
				form.setUseTimeType(useTimeType);
				form.setUseTimeInfo(useTimeInfo);
			}
		} catch (Exception e) {
			logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		int brandInfoId = couponRule.getBrandInfoId();
		List<Map<String, Object>> levelList = binOLSSPRM73_BL.getLevelList(brandInfoId);
		List<Map<String, Object>> useLevelList = ConvertUtil.copyList(levelList);
		try {
			// 发送门槛
			String sendCond = couponRule.getSendCond();
			if (!CherryChecker.isNullOrEmpty(sendCond)) {
				Map<String, Object> sendCondInfo = (Map<String, Object>) JSONUtil.deserialize(sendCond);
				sendCondInfo.put("levelList", levelList);
				binOLSSPRM73_BL.condSetting(sendCondInfo, brandInfoId);
				form.setSendCondInfo(sendCondInfo);
			} else {
				form.getSendCondInfo().put("levelList", levelList);
			}
		} catch (Exception e) {
			logger.error("发送门槛JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		try {
			// 使用门槛
			String useCond = couponRule.getUseCond();
			if (!CherryChecker.isNullOrEmpty(useCond)) {
				Map<String, Object> useCondInfo = (Map<String, Object>) JSONUtil.deserialize(useCond);
				useCondInfo.put("levelList", useLevelList);
				binOLSSPRM73_BL.condSetting(useCondInfo, brandInfoId);
				form.setUseCondInfo(useCondInfo);
			} else {
				form.getUseCondInfo().put("levelList", useLevelList);
			}
		} catch (Exception e) {
			logger.error("使用门槛JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		try {
			// 规则内容
			String content = couponRule.getContent();
			if (!CherryChecker.isNullOrEmpty(content)) {
				List<Map<String, Object>> contentList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
				if (null != contentList) {
					int maxContentNo = 0;
					for (Map<String, Object> contentInfo : contentList) {
						binOLSSPRM73_BL.contentSetting(contentInfo, brandInfoId);
						if (contentInfo.containsKey("contentNo")) {
							int contentNo = Integer.parseInt(String.valueOf(contentInfo.get("contentNo")));
							if (contentNo > maxContentNo) {
								maxContentNo = contentNo;
							}
						}
					}
					couponRule.setMaxContentNo(maxContentNo);
				}
				form.setContentInfoList(contentList);
			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		form.setCouponRule(couponRule);
		return SUCCESS;
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
    public String save() throws Exception{
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			commParams(map);
			CouponRuleDTO couponRule = form.getCouponRule(); 
			couponRule.setOrganizationInfoId(Integer.parseInt(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID))));
			// 保存优惠券规则
			binOLSSPRM73_BL.tran_saveCouponRule(couponRule);
			coupEngine.refreshRule(couponRule.getRuleCode());
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			logger.error(e.getMessage(),e);
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
   		CouponRuleDTO couponRule = form.getCouponRule();
   		// 验证结果
   		boolean isCorrect = true;
   		// 规则名称
   		String ruleName = couponRule.getRuleName();
   		// 规则名称必须入力验证
   		if (CherryChecker.isNullOrEmpty(ruleName, true)) {
   			this.addFieldError("couponRule.ruleName",
   					getText("ECM00009", new String[] { getText("PSS01000") }));
   			isCorrect = false;
   		} else if (ruleName.length() > 40) {
   			// 规则名称不能超过40位验证
   			this.addFieldError(
   					"couponRule.ruleName",
   					getText("ECM00020", new String[] { getText("PSS01000"),
   							"40" }));
   			isCorrect = false;
   		}
   		// 发券开始日期
		String sendStartTime = couponRule.getSendStartTime();
		// 发券结束日期
		String sendEndTime = couponRule.getSendEndTime();
		boolean dateFlg = true;
		// 发券开始日期验证
		if (CherryChecker.isNullOrEmpty(sendStartTime, true)) {
			this.addFieldError("couponRule.sendStartTime",
   					getText("ECM00009", new String[] { getText("PSS01001") }));
   			isCorrect = false;
   			dateFlg = false;
		} else {
			// 日期格式验证
			if (!CherryChecker.checkDate(sendStartTime)) {
				this.addFieldError(
						"couponRule.sendStartTime",
						getText("ECM00008",
								new String[] { getText("PSS01001") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		// 发券结束日期验证
		if (CherryChecker.isNullOrEmpty(sendEndTime, true)) {
			this.addFieldError("couponRule.sendEndTime",
   					getText("ECM00009", new String[] { getText("PSS01002") }));
   			isCorrect = false;
   			dateFlg = false;
		} else {
			// 日期格式验证
			if (!CherryChecker.checkDate(sendEndTime)) {
				this.addFieldError(
						"couponRule.sendEndTime",
						getText("ECM00008",
								new String[] { getText("PSS01002") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		if (dateFlg) {
			// 发券开始日期在发券结束日期之后
			if (CherryChecker.compareDate(sendStartTime, sendEndTime) > 0) {
				this.addFieldError(
						"couponRule.sendEndTime",
						getText("ECM00033", new String[] { getText("PSS01002"),
								getText("PSS01001") }));
				isCorrect = false;
			}
		}
		String useTime = couponRule.getUseTimeJson();
		Map<String, Object> useTimeInfo = null;
		if (!CherryChecker.isNullOrEmpty(useTime, true)) {
			try {
				useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTime);
			} catch (Exception e) {
				logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			}
		}
		if (null == useTimeInfo || useTimeInfo.isEmpty()) {
			this.addActionError(getText("ECM00009", new String[] { getText("PSS01003") }));
			isCorrect = false;
		} else {
			useTimeInfo.put("useTimeType", form.getUseTimeType());
			couponRule.setUseTimeJson(JSONUtil.serialize(useTimeInfo));
			// 指定日期
			if (CouponConstains.USETIMETYPE_0.equals(form.getUseTimeType())) {
				// 券使用开始日期
				String useStartTime = (String) useTimeInfo.get("useStartTime");
				// 券使用结束日期
				String useEndTime = (String) useTimeInfo.get("useEndTime");
				dateFlg = true;
				// 券使用开始日期验证
				if (CherryChecker.isNullOrEmpty(useStartTime, true)) {
					this.addFieldError("useStartTime",
		   					getText("ECM00009", new String[] { getText("PSS01004") }));
		   			isCorrect = false;
		   			dateFlg = false;
				} else {
					// 日期格式验证
					if (!CherryChecker.checkDate(useStartTime)) {
						this.addFieldError(
								"useStartTime",
								getText("ECM00008",
										new String[] { getText("PSS01004") }));
						isCorrect = false;
						dateFlg = false;
					}
				}
				// 券使用结束日期验证
				if (CherryChecker.isNullOrEmpty(useEndTime, true)) {
					this.addFieldError("useEndTime",
		   					getText("ECM00009", new String[] { getText("PSS01005") }));
		   			isCorrect = false;
		   			dateFlg = false;
				} else {
					// 日期格式验证
					if (!CherryChecker.checkDate(useEndTime)) {
						this.addFieldError(
								"useEndTime",
								getText("ECM00008",
										new String[] { getText("PSS01005") }));
						isCorrect = false;
						dateFlg = false;
					}
				}
				if (dateFlg) {
					// 券使用开始日期在券使用结束日期之后
					if (CherryChecker.compareDate(useStartTime, useEndTime) > 0) {
						this.addFieldError(
								"useEndTime",
								getText("ECM00033", new String[] { getText("PSS01005"),
										getText("PSS01004") }));
						isCorrect = false;
					}
				}
			}
		}
   		return isCorrect;
   	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		commParams(map);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		return map;
	}
	
	private void commParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	}
	
	/**
	 * 批量生成优惠券
	 * 
	 * @return 
	 */
	public String couponBatch() throws Exception {
		// 用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = getSearchMap();
		String ruleCode=ConvertUtil.getString(map.get("ruleCode"));
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		// 批量生成优惠券(非会员)
		String batchMode=ConvertUtil.getString(map.get("batchMode"));
		try {
			if ("0".equals(batchMode) && !validateBatch(couponRule) ) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			if("0".equals(batchMode)){
				// 批量生成优惠券(非会员)
				binOLSSPRM73_BL.tran_couponBatch(couponRule, form.getBatchCount());
			}else if("1".equals(batchMode)){
				//获取该活动对应导入的会员活动信息
				map.put("conditionType", 3);
				List<Map<String, Object>> member_list=binOLSSPRM73_BL.getMemberInfoListWithoutPage(map);
				if(member_list != null && member_list.size()>0){
					List<Map<String,Object>> couponResultList=new ArrayList<Map<String,Object>>();
					binOLSSPRM73_BL.tran_couponBatch(couponRule, member_list.size(),member_list,couponResultList);
					//批量发短信操作
					if(couponResultList != null && couponResultList.size() > 0 && binOLCM14_BL.isConfigOpen("1370", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID())){
						for(Map<String,Object> coupon:couponResultList){
							BillInfo billInfo=new BillInfo();
							billInfo.setAllCoupon(ConvertUtil.getString(coupon.get("couponCode")));
							billInfo.setMobile(ConvertUtil.getString(coupon.get("mobile")));
							billInfo.setBrandInfoId(userInfo.getBIN_BrandInfoID());
							billInfo.setOrganizationInfoId(userInfo.getBIN_OrganizationInfoID());
							coupon_IF.sendSms(billInfo);
						}
					}
				}else{
					this.addActionError("没有查询到需要生成券的相关会员信息");
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}else{
				logger.error("批量生成券：无效的batchMode，值为:"+batchMode);
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			this.addActionMessage(getText("ICM00002"));
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError(getText("ECM00089"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}
	
	/**
   	 * 验证提交的参数
   	 * 
   	 * @param 无
   	 * @return boolean 验证结果
   	 * @throws Exception
   	 * 
   	 */
   	private boolean validateBatch(CouponRuleDTO couponRule) throws Exception {
   		// 验证结果
   		boolean isCorrect = true;
   		if (!CouponConstains.ISGIVE_1.equals(couponRule.getIsGive())) {
   			this.addActionError("不限会员的模式下，优惠券规则必须设置为允许转赠");
   			isCorrect = false;
   		}
   		return isCorrect;
   	}
	
	/**
	 * 批量生成优惠券对话框
	 * @return
	 * @throws Exception
	 */
	public String batchInit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", form.getRuleCode());
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		form.setRuleName(couponRule.getRuleName());
		return SUCCESS;
	}
	
	/**
	 * 审核通过
	 * @throws Exception
	 */
	public void checkRule() throws Exception{
		Map<String, Object> msgMap = new HashMap<String, Object>();
		String errCode = "0";
		try {
			// 更新审核状态
			binOLSSPRM73_BL.tran_check(form.getRuleCode());
			coupEngine.refreshRule(form.getRuleCode());
		} catch (Exception e) {
			logger.error("**********"+e.getMessage()+"**********",e);
			errCode = "201";
		}
		msgMap.put("ERRORCODE", errCode);
		ConvertUtil.setResponseByAjax(response, msgMap);
	}
	
	/**
	 * <p>
	 * 导入门店处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCounter() throws Exception {
		try {
			CouponRuleDTO couponRule = form.getCouponRule();
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			// 活动编码
			map.put("ruleCode", couponRule.getRuleCode());
			// 品牌ID
			map.put("brandInfoId", couponRule.getBrandInfoId());
			// 条件类型
			map.put("conditionType", form.getConditionType());
			// 导入模式
			map.put("upMode", form.getUpMode());
			// 导入柜台处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importCounter(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * <p>
	 * 批量生成券的时候导入会员手机号
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCouponByMemberPhone() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			map.put("ruleCode", form.getRuleCode());
			map.put("conditionType", form.getConditionType());
			map.put("upMode", form.getUpMode());
			// 导入会员处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importCouponMember(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	
	/**
	 * 初始化柜台结果一览
	 * @return
	 * @throws Exception
	 */
	public String counterInit() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 柜台结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String counterDialog() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("counterKey", form.getSSearch());
		}
		// 取得柜台总数
		int count = binOLSSPRM73_BL.getCounterDialogCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得柜台信息List
			form.setCounterList(binOLSSPRM73_BL.getCounterDialogList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 导入会员处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importMember() throws Exception {
		try {
			CouponRuleDTO couponRule = form.getCouponRule();
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			// 活动编码
			map.put("ruleCode", couponRule.getRuleCode());
			// 品牌ID
			map.put("brandInfoId", couponRule.getBrandInfoId());
			// 条件类型
			map.put("conditionType", form.getConditionType());
			// 导入模式
			map.put("upMode", form.getUpMode());
			// 导入柜台处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importMember(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 初始化会员结果一览
	 * @return
	 * @throws Exception
	 */
	public String memberInit() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 会员结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String memberDialog() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("memberKey", form.getSSearch());
		}
		// 取得柜台总数
		int count = binOLSSPRM73_BL.getMemberDialogCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得会员信息List
			form.setMemberList(binOLSSPRM73_BL.getMemberDialogList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化渠道一览
	 * @return
	 * @throws Exception
	 */
	public String channelInit() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 渠道一览查询
	 * @return
	 * @throws Exception
	 */
	public void channelDialog() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		List<Map<String, Object>> resultList = binOLSSPRM73_BL.getChannelList(map);
		
		if(null != resultList && resultList.size() > 0){
			List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = { "channelId", "name" };
			String[] keys2 = { "memCounterId", "counterName" };
			keysList.add(keys1);
			keysList.add(keys2);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
			ConvertUtil.setResponseByAjax(response, resultTreeList);
		}
		
	}
}
