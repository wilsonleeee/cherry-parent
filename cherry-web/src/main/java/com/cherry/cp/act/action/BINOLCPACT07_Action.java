/*	
 * @(#)BINOLCPACT07_Action.java     1.0 @2013-07-15		
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
package com.cherry.cp.act.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.cp.act.form.BINOLCPACT07_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT07_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 礼品领用报表Action
 * 
 * @author menghao
 * 
 * @version 1.0 2013/07/15
 */
public class BINOLCPACT07_Action extends BaseAction implements
		ModelDriven<BINOLCPACT07_Form> {

	private static final long serialVersionUID = 8401859292573607265L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT07_Action.class);

	 /** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
    
	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** 接口 */
	@Resource(name = "binOLCPACT07_IF")
	private BINOLCPACT07_IF binOLCPACT07_IF;

	/** 参数FORM */
	private BINOLCPACT07_Form form = new BINOLCPACT07_Form();

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	private List<Map<String, Object>> giftDrawList;

	// 假期
	private String holidays;
	
	/** Excel输入流 */
	private InputStream excelStream;
	
	/** 导出文件名 */
	private String exportName;

	/**
	 * 初始化
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
//		// 开始日期
//		form.setStartDate(binOLCM00_BL.getFiscalDate(
//				userInfo.getBIN_OrganizationInfoID(), new Date()));
//		// 截止日期
//		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == -9999) {
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
		return "BINOLCPACT07";
	}

	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		//业务类型
		map.put("businessType", "1");
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【手机号码】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		int count = binOLCPACT07_IF.getGiftDrawCount(map);
		if (count > 0) {
			giftDrawList = binOLCPACT07_IF.getGiftDrawList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLCPACT07_1";
	}

	/**
	 * 取得活动名称下拉框数据
	 * 
	 * @throws Exception
	 */
	public void getActivity() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
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
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 显示行数
		map.put("number", form.getNumber());
		// 显示项
		map.put("selected",form.getSelected());
		// 根据活动输入内容模糊查询相关活动
		map.put("activityCode", form.getActivityCode().trim());
		try {
			String resultStr = binOLCPACT07_IF.getActivity(map);
			ConvertUtil.setResponseByAjax(response, resultStr);
		} catch(Exception e) {
			//打印异常日志
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出礼品领用详细报表 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "giftDrawId desc");
		try {
			String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
			String extName = binOLMOCOM01_BL.getResourceValue("BINOLCPACT07", language, "exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLCPACT07_IF.exportExcel(map), extName+".xls"));
		} catch(Exception e) {
			//打印异常日志
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLCPACT07_excel";
	}

	/**
	 * 取得查询参数
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 品牌
		if (!CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			map.put("brandInfoId", form.getBrandInfoId());
		}
		map.put("brandCode", userInfo.getBrandCode());
		// 开始时间
		map.put("startDate", form.getStartDate());
		// 结束时间
		map.put("endDate", form.getEndDate());
		// coupon码
		map.put("couponCode", form.getCouponCode().trim());
		// 礼品领用单据号
		map.put("billNoIF", form.getBillNoIF().trim());
		// 领用柜台号
		map.put("counterCode", form.getCounterCode().trim());
		// 会员卡号
		map.put("memberCode", form.getMemberCode().trim());
		// 会员手机号
		map.put("mobilePhone", form.getMobilePhone().trim());
		// 主题活动代号
		map.put("activityCode", form.getActivityCode().trim());
		//主题活动名称
		map.put("activityName", form.getActivityName());
		//会员测试区分
		map.put("testTypes", form.getTestType());

		return map;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getGiftDrawList() {
		return giftDrawList;
	}

	public void setGiftDrawList(List<Map<String, Object>> giftDrawList) {
		this.giftDrawList = giftDrawList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	
	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	@Override
	public BINOLCPACT07_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
