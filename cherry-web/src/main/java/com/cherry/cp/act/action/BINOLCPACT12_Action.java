/*	
 * @(#)BINOLCPACT12_Action.java     1.0 @2014-12-16	
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.form.BINOLCPACT12_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT12_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 活动产品库存一览Action
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT12_Action extends BaseAction implements
		ModelDriven<BINOLCPACT12_Form> {
			
	private static final long serialVersionUID = -4890692601603482123L;
	
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT12_Action.class);

	/** 活动产品库存参数FORM */
	private BINOLCPACT12_Form form = new BINOLCPACT12_Form();

	/** 活动产品库存接口 */
	@Resource(name = "binOLCPACT12_BL")
	private BINOLCPACT12_IF binOLCPACT12_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 活动产品库存List */
	private List<Map<String,Object>> campaignStockList;
	
	/**活动产品库存详细主信息*/
	private Map campaignStockDetail;
	
	/**活动产品库存详细明细*/
	private List campaignStockProductDetail;

	/**
	 * 初始化页面
	 * @return
	 */
	public String init() {
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
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
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
		}
		return SUCCESS;
	}

	/**
	 * 查询
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		int count = binOLCPACT12_BL.getCampaignStockCount(map);
		if(count>0){
			campaignStockList = binOLCPACT12_BL.getCampaignStockList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLCPACT12_1";
	}
	
	/**
	 * 活动产品库存详细
	 * @return
	 * @throws Exception
	 */
	public String detailInit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("subCampCode", form.getSubCampCode());
		map.put("organizationId", form.getOrganizationId());
		
		// 取得当前活动指定柜台的库存主信息
		List<Map<String, Object>> returnList = binOLCPACT12_BL.getCampaignStockDetailMain(map);
		if(returnList != null && returnList.size() > 0) {
			campaignStockDetail = returnList.get(0);
			campaignStockProductDetail = binOLCPACT12_BL.getCampaignStockProductDetail(map);
		}
		return "BINOLCPACT12_2";
	}
	
	/**
	 * 编辑活动产品库存页面初始化
	 * @return
	 * @throws Exception
	 */
	public String updateInit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("subCampCode", form.getSubCampCode());
		map.put("organizationId", form.getOrganizationId());
		
		// 取得当前活动指定柜台的库存主信息
		List<Map<String, Object>> returnList = binOLCPACT12_BL.getCampaignStockDetailMain(map);
		if(returnList != null && returnList.size() > 0) {
			campaignStockDetail = returnList.get(0);
			campaignStockProductDetail = binOLCPACT12_BL.getCampaignStockProductDetail(map);
		}
		return "BINOLCPACT12_3";
	}
	
	/**
	 * 保存编辑
	 * @return
	 * @throws Exception
	 */
	public String saveUpdate() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);             
        //用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        //组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        // 作成者为当前用户
        map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
        // 作成程序名为当前程序
        map.put(CherryConstants.CREATEPGM, "BINOLCPACT12");
        // 更新者为当前用户
        map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
        // 更新程序名为当前程序
        map.put(CherryConstants.UPDATEPGM, "BINOLCPACT12");
        map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("subCampCode", form.getSubCampCode());
		map.put("organizationId", form.getOrganizationId());
		
        /**产品厂商ID*/
        String[] productVendorIdArr = form.getProductVendorIDArr();
        /**分配数量*/
    	String[] totalQuantityArr = form.getTotalQuantityArr();
    	/**安全数量*/
    	String[] safeQuantityArr = form.getSafeQuantityArr();
    	List<String[]> arrList = new ArrayList<String[]>();
    	arrList.add(productVendorIdArr);
    	arrList.add(totalQuantityArr);
    	arrList.add(safeQuantityArr);
    	try {
    		binOLCPACT12_BL.tran_save(map, arrList);
    		 // 处理成功
            this.addActionMessage(getText("ICM00002"));
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
            	//系统发生异常，请联系管理人员。
            	this.addActionError(getText("ECM00036"));
            }
    	}
    	
    	return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 根据输入的字符串模糊查询会员活动名称
	 * 
	 * 
	 * */
	public void getCampName() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		if(userInfo.getBIN_BrandInfoID() == -9999){
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		//主题活动名称模糊查询
		map.put("campInfoStr", form.getCampInfoStr().trim());
		String resultStr = binOLCPACT12_BL.getCampName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
		
	}
	/**
	 * 根据输入的字符串模糊查询会员活动名称
	 * 
	 * 
	 * */
	public void getSubCampName() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		//活动名称模糊查询
		map.put("subCampInfoStr", form.getSubCampInfoStr().trim());
		// 联动主题活动进行查询查询
		map.put("campaignCode", form.getCampaignCode().trim());
		String resultStr = binOLCPACT12_BL.getSubCampName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
		
	}
	
	/**
	 * 取得查询参数
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		
		// 部门联动条参数
		String paramsStr = form.getParams();
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(paramsStr);
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	@Override
	public BINOLCPACT12_Form getModel() {
		return form;
	}

	public List<Map<String,Object>> getCampaignStockList() {
		return campaignStockList;
	}

	public void setCampaignStockList(List<Map<String,Object>> campaignStockList) {
		this.campaignStockList = campaignStockList;
	}

	public Map getCampaignStockDetail() {
		return campaignStockDetail;
	}

	public void setCampaignStockDetail(Map campaignStockDetail) {
		this.campaignStockDetail = campaignStockDetail;
	}

	public List getCampaignStockProductDetail() {
		return campaignStockProductDetail;
	}

	public void setCampaignStockProductDetail(List campaignStockProductDetail) {
		this.campaignStockProductDetail = campaignStockProductDetail;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

}
