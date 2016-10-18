/*  
 * @(#)BINOLMOWAT08_Action.java    1.0 2014-10-28    
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
package com.cherry.mo.wat.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.wat.form.BINOLMOWAT08_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT08_IF;
import com.mongodb.DBObject;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 终端消息反馈日志查询Action
 * 
 * @author menghao
 * @version 1.0 2014-10-28
 */
public class BINOLMOWAT08_Action extends BaseAction implements
		ModelDriven<BINOLMOWAT08_Form> {

	private static final long serialVersionUID = 2404083490657031980L;
	
	/** MQ消息错误日志查询BL */
	@Resource(name="binOLMOWAT08_BL")
	private BINOLMOWAT08_IF binOLMOWAT08_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通BL */
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
	/**
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String MQ消息错误日志查询画面
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		String brandInfoIdtemp = ConvertUtil.getString(form.getBrandInfoId());
		// 取得品牌List
		if(!"".equals(brandInfoIdtemp)) {
			// 其他页面调用此页面的情况
		} else if (userInfo.getBIN_BrandInfoID() == -9999) {
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
		// 查询假日
        setHolidays(binOLCM00_BL.getHolidays(map));
		// 开始日期
        form.setTimeStart(binOLCM00_BL.getFiscalDate(userInfo
          .getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setTimeEnd(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}

	/**
	 * AJAXMQ消息错误日志查询
	 * 
	 * @return
	 */
	public String search() throws Exception {

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织代码
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 品牌代码
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 查询终端消息反馈日志信息数量
		int count = binOLMOWAT08_BL.getMQNoticeInfoCount(map);
		if(count > 0) {
			mqNoticeList = binOLMOWAT08_BL.getMQNoticeInfoList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/** MQ消息错误日志List */
	private List<DBObject> mqNoticeList;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 节日 */
    private String holidays;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<DBObject> getMqNoticeList() {
		return mqNoticeList;
	}

	public void setMqNoticeList(List<DBObject> mqNoticeList) {
		this.mqNoticeList = mqNoticeList;
	}

	/** MQ消息错误日志查询Form */
	private BINOLMOWAT08_Form form = new BINOLMOWAT08_Form();

	@Override
	public BINOLMOWAT08_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

}
