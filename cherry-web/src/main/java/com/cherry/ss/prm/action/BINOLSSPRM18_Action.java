/*  
 * @(#)BINOLSSPRM18_Action.java     1.0 2011/05/31      
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM18_BL;
import com.cherry.ss.prm.form.BINOLSSPRM18_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 我的发货单
 * @author dingyc
 *
 */
public class BINOLSSPRM18_Action extends BaseAction implements
ModelDriven<BINOLSSPRM18_Form>{

	private static final long serialVersionUID = 6934346836734458784L;	
	
	@Resource
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource
	private BINOLSSPRM18_BL binOLSSPRM18BL;
	
	/** 参数FORM */
	private BINOLSSPRM18_Form form = new BINOLSSPRM18_Form();

    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 查询假日
		form.setHolidays(binOLCM00BL.getHolidays(map));
//		// 开始日期
//		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo
//				.getBIN_OrganizationInfoID(), new Date()));
//		// 截止日期
//		form.setEndDate(CherryUtil
//				.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * AJAX发货单查询
	 * </p>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得发货单总数
		int count = binOLSSPRM18BL.searchDeliverCount(searchMap);
		if (count > 0) {
			// 取得发货单List
			form.setDeliverList(binOLSSPRM18BL.searchDeliverList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
//      ================= LuoHong修改：显示统计信息 ================== //
//		String prmVendorId = ConvertUtil.getString(searchMap.get("prmVendorId"));
//		if (!CherryConstants.BLANK.equals(prmVendorId)) {
		    sumInfo = binOLSSPRM18BL.getSumInfo(searchMap);
//		}
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM18_1";
	}
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		  //部门ID
        map.put("inOrganizationId", form.getInOrganizationId());
		// 发货单号
		map.put("deliverRecNo", form.getDeliverRecNo());
		// 开始日
		map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
		// 结束日
		map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());		
		map.put("stockInFlag", form.getStockInFlag());
		//制单人
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		//促销产品厂商ID
        map.put("prmVendorId", form.getPrmVendorId());
        // 部门类型
        map.put("departType", form.getDepartType());
        //部门联动条 查询 发货部门/收货部门 标志
        map.put("departInOutFlag", form.getDepartInOutFlag());
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        //部门联动条标记
        map.put("linkageDepartFlag", linkageDepartFlag(map));
        
		return map;
	}
	
	/**
	 * 判断部门联动条查询参数是否为空
	 * 
	 * @return "0":查询参数为空  "1":查询参数不为空
	 */
	private String linkageDepartFlag(Map<String, Object> map) {
		String flag = "0";
		if (map.containsKey("departId") || map.containsKey("channelId")
				|| map.containsKey("departType") || map.containsKey("regionId")
				|| map.containsKey("provinceId") || map.containsKey("cityId")
				|| map.containsKey("countyId")) {
			// 查询参数不为空
			flag = "1";
		}
		return flag;
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{"结束日期"}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
	
	public BINOLSSPRM18_Form getModel() {
		return form;
	}

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}