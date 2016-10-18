/*	
 * @(#)BINOLMBCLB01_Action.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.clb.form.BINOLMBCLB01_Form;
import com.cherry.mb.clb.interfaces.BINOLMBCLB01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员俱乐部一览Action
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB01_Action extends BaseAction implements
ModelDriven< BINOLMBCLB01_Form>{

	private static final long serialVersionUID = -1797825351456561360L;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLMBCLB01_IF binolmbclb01_IF;
	
	/** 参数FORM */
	private BINOLMBCLB01_Form form = new BINOLMBCLB01_Form();

	@Override
	public BINOLMBCLB01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	/**
	 * <p>
	 * 积分规则配置一览初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
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
		// 取得所管辖的品牌List
		List<Map<String, Object>> brandList = null;
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			brandList = binOLCM05_BL.getBrandInfoList(map);
			if (null != brandList && !brandList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 取得品牌名称
			brandMap.put("brandName", binOLCM05_BL.getBrandName(map));
			// 品牌ID
			brandMap.put("brandInfoId", ConvertUtil.getString(brandInfoId));
			// 取得所管辖的品牌List
			brandList = new ArrayList<Map<String, Object>>();
			brandList.add(brandMap);
		}
		form.setBrandInfoList(brandList);
		return "success";
	}
	
	/**
	 * <p>
	 * 积分规则配置查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String search() throws Exception{
		Map<String, Object> baseMap = (Map<String, Object>) Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, baseMap);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		baseMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		baseMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		int count = 0;
		if (CherryChecker.isNullOrEmpty(form.getPrmCounterId())) {
			// 取得俱乐部件数 
			count = binolmbclb01_IF.getClubCount(baseMap);
			if(count > 0){
				// 取得俱乐部List
				List<Map<String, Object>> clubList = binolmbclb01_IF.getClubList(baseMap);
				// 俱乐部List
				form.setClubList(clubList);
			}
		} else {
			baseMap.put("userID", userInfo.getBIN_UserID());
			baseMap.put("operationType", "1");
			baseMap.put("businessType", "1");
			baseMap.put("DEPARTTYPE", "4");
			// 取得俱乐部件数 
			count = binolmbclb01_IF.getClubWithPrivilCount(baseMap);
			if(count > 0){
				// 取得俱乐部List
				List<Map<String, Object>> clubList = binolmbclb01_IF.getClubWithPrivilList(baseMap);
				// 俱乐部List
				form.setClubList(clubList);
			}
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "success";
	}
	
	/**
	 * <p>
	 * 停用俱乐部
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String clubValid() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 有效区分
    	map.put("reason", form.getReason());
    	// 俱乐部ID
    	map.put("memberClubId", form.getMemberClubId());
    	try{
			// 停用俱乐部
    		binolmbclb01_IF.tran_editValid(map);
		}catch (Exception e){
			// 操作失败
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 操作成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
	 * <p>
	 * 下发俱乐部
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @throws Exception 
	 * 
	 */
    public void sendClub() throws Exception{
    	// 取得有效的规则配置信息
    	Map<String, Object> resultInfo = new HashMap<String, Object>();
    	boolean errFlag = false;
    	try {
    		// 是否上一次下发处理还未执行完成
    		if (binolmbclb01_IF.isBatchExec(form.getBrandInfoId())) {
    			resultInfo.put("RESULT", "NG01");
    			// 是否需要执行本次下发处理
    		} else if (binolmbclb01_IF.isNeedSend(form.getBrandInfoId())) {
    			Map<String, Object> map = new HashMap<String, Object>();
    	    	// 用户信息
    			UserInfo userInfo = (UserInfo) session     
    					.get(CherryConstants.SESSION_USERINFO);
    			// 所属组织
    			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
    							.getBIN_OrganizationInfoID());
    			// 品牌信息ID
    			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
    	    	// 所属组织代码
    	    	map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
    	    	// 品牌代码
    	    	map.put(CherryConstants.BRAND_CODE, binOLCM05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
    	    	// 执行下发处理
    	    	Map<String, Object> rstMap = binolmbclb01_IF.sendClub(map);
    	    	if (!"0".equals(rstMap.get("result"))) {
    	    		errFlag = true;
    	    	}
    		} else {
    			resultInfo.put("RESULT", "NG03");
    		}
		} catch (Exception e){
			errFlag = true;
		}
    	if (errFlag) {
    		resultInfo.put("RESULT", "NG02");
    	} else {
    		resultInfo.put("RESULT", "OK");
    	}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultInfo));
    }

}
