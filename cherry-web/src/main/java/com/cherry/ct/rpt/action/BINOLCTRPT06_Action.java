/*
 * @(#)BINOLCTRPT01_Action.java     1.0 2014/11/11
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
package com.cherry.ct.rpt.action;

import java.util.ArrayList;
import java.util.Date;
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.rpt.form.BINOLCTRPT06_Form;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT06_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员沟通效果报表Action
 * 
 * @author menghao
 * @version 2014.11.11
 * 
 */
public class BINOLCTRPT06_Action extends BaseAction implements
		ModelDriven<BINOLCTRPT06_Form> {

	private static final long serialVersionUID = 8735375426516324792L;

	/** 沟通模板一览Form */
	private BINOLCTRPT06_Form form = new BINOLCTRPT06_Form();

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLCTRPT06_Action.class);

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	@Resource(name = "binOLCTRPT06_BL")
	private BINOLCTRPT06_IF binOLCTRPT06_BL;
	
	/** 共通BL */
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	private List<Map<String, Object>> memCommunStatisticsList;
 
	/**会员沟通效果详细List*/
	private List<Map<String, Object>> memCommunResultDetailList;
	/**会员沟通效果*/
	private Map<String, Object> memCommunResultInfo;

	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();

			// 总部用户登录的时候
			if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
				// 取得所管辖的品牌List
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
			} else {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				// 品牌ID
				brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
				// 品牌名称
				brandMap.put("brandName", userInfo.getBrandName());
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, brandMap);
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(brandMap);
				}
			}

			// 开始日期
	        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
	          .getBIN_OrganizationInfoID(), new Date()));
	        // 截止日期
	        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
	        // 取得渠道List
			setChannelList(binolcm00BL.getChannelList(map));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
		return SUCCESS;
	}

	/**
	 * 查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSearchMap();
			// 取得模板数量
			int count = binOLCTRPT06_BL.getMemCommunStatisticsCount(map);
			// 取得统计信息
			if (count > 0) {
				// 取得List
				this.setMemCommunStatisticsList(binOLCTRPT06_BL.getMemCommunStatisticsList(map));
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 沟通详细查询初始化
	 * @return
	 * @throws Exception
	 */
	public String detailInit() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 开始日期
        form.setStartDateDetail(binOLCM00_BL.getFiscalDate(userInfo
          .getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDateDetail(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * 沟通详细查询
	 * @return
	 * @throws Exception
	 */
	public String detailSearch() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			Map<String, Object> map = getDetailSearchMap();
			// 取得模板数量
			int count = binOLCTRPT06_BL.getCommunEffectDetailCount(map);
			// 取得统计信息
			this.setMemCommunResultInfo(binOLCTRPT06_BL.getMemCommunResultInfo(map));
			if (count > 0) {
				// 取得List
				this.setMemCommunResultDetailList(binOLCTRPT06_BL.getCommunEffectDetailList(map));
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询表单后台校验
	 * @throws Exception
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		String price = ConvertUtil.getString(form.getPrice());
		if("".equals(price)) {
			// 短信成本不能为空，请输入
			this.addActionError(getText("ECM00009",new String[]{getText("CTM00022")}));
			isCorrect = false;
		} else if(!CherryChecker.isFloatValid(price, 6, 3)){
			// {0}必须为数字类型，整数不能超过{1}位，小数不能超过{2}位。
			this.addActionError(getText("ECM00049",new String[]{getText("CTM00022"),"6","3"}));
			isCorrect = false;
		}
		return isCorrect;
	}
	
	/**
	 * 明细查询参数获取
	 * @return
	 */
	private Map<String, Object> getDetailSearchMap() {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if (form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		
		// 开始时间
		if (!CherryChecker.isNullOrEmpty(form.getEndDateDetail(), true)) {
			map.put("startDate", form.getStartDateDetail());
		}
		// 结束时间
		if (!CherryChecker.isNullOrEmpty(form.getEndDateDetail(), true)) {
			map.put("endDate",
					DateUtil.addDateByDays("yyyy-MM-dd", form.getEndDateDetail(), 1));
		}
		// 短信成本
		map.put("price", form.getPrice().trim());
		map.put("communicationCode", form.getCommunicationCode());
		
		return map;
	}

	/**
	 * 获取查询参数
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo
				.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if (form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID,
						userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));

		// 开始时间
		if (!CherryChecker.isNullOrEmpty(form.getStartDate(), true)) {
			map.put("startDate", form.getStartDate());
		}
		// 结束时间
		if (!CherryChecker.isNullOrEmpty(form.getEndDate(), true)) {
			map.put("endDate",
					DateUtil.addDateByDays("yyyy-MM-dd", form.getEndDate(), 1));
		}
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
				
		// 短信成本
//		map.put("price", form.getPrice().trim());
		// 沟通名称
		map.put("communicationName", form.getCommunicationName().trim());
		map.put("channelId", form.getChannelId());
		map.put("counterCode", form.getCounterCode());
		
		if("1".equals(pvgFlag)){
			map.put("privilegeFlag", "1");
		}else{
			map.put("privilegeFlag", "0");
		}
		// 业务类型
		map.put("businessType", "4");
		// 操作类型
		map.put("operationType", "1");
		return map;
	}

	@Override
	public BINOLCTRPT06_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public Map<String, Object> getMemCommunResultInfo() {
		return memCommunResultInfo;
	}

	public void setMemCommunResultInfo(Map<String, Object> memCommunResultInfo) {
		this.memCommunResultInfo = memCommunResultInfo;
	}

	public List<Map<String, Object>> getMemCommunStatisticsList() {
		return memCommunStatisticsList;
	}

	public void setMemCommunStatisticsList(List<Map<String, Object>> memCommunStatisticsList) {
		this.memCommunStatisticsList = memCommunStatisticsList;
	}

	public List<Map<String, Object>> getMemCommunResultDetailList() {
		return memCommunResultDetailList;
	}

	public void setMemCommunResultDetailList(
			List<Map<String, Object>> memCommunResultDetailList) {
		this.memCommunResultDetailList = memCommunResultDetailList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

}
