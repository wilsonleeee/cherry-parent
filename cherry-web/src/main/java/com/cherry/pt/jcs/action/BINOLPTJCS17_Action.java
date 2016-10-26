/*
 * @(#)BINOLPTJCS14_Action.java v1.0 2014-8-7
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
package com.cherry.pt.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS14_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS04_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS16_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS17_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品方案柜台分配维护Action
 * 
 * @author JiJW
 * @version 1.0 2014-8-7
 */
public class BINOLPTJCS17_Action extends BaseAction implements
ModelDriven<BINOLPTJCS14_Form>{

	private static final long serialVersionUID = 1324480801173680548L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS17_Action.class.getName());
	
	/** 参数FORM */
	private BINOLPTJCS14_Form form = new BINOLPTJCS14_Form();
	
	@Resource(name="binOLPTJCS16_IF")
	private BINOLPTJCS16_IF binOLPTJCS16_IF;
	
	@Resource(name="binOLPTJCS17_IF")
	private BINOLPTJCS17_IF binOLPTJCS17_IF;
	
	@Resource(name="binOLPTJCS04_IF")
	private BINOLPTJCS04_IF binolptjcs04_IF;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 产品品牌ID */
	private int brandInfoId;
	
	/**
	 * 产品价格方案
	 */
	private List prtPriceSolutionList;
	
	/**
	 * 柜台产品配置Map
	 */
	private Map configMap = new HashMap();
	
	/**导入结果*/
	@SuppressWarnings("rawtypes")
	private Map resultMap;

	/** 校验结果 */
	private Map chkExistCntMap;
	
	/**
	 * 分配初始化画画
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 总部的场合
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				// 取得品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			} else {
				// 品牌信息
				Map<String, Object> brandInfo = new HashMap<String, Object>();
				brandInfoId = Integer.valueOf(ConvertUtil.getString(map.get("brandInfoId")));
				// 品牌ID
				brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
				// 品牌Code
				brandInfo.put("brandCode", userInfo.getBrandCode());
				// 品牌名称
				brandInfo.put("brandName", userInfo.getBrandName());
				// 品牌List
				brandInfoList = new ArrayList();
				brandInfoList.add(brandInfo);
			}
			
			// 所有产品价格方案
			prtPriceSolutionList = binOLPTJCS16_IF.getPrtPriceSolutionList(map);
			
			String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
			configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
			
			// 取得柜台产品地点选择范围
			String placeConfig = binOLCM14_BL.getConfigValue("1291", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("placeConfig", placeConfig);
			
			
			// 是否小店云系统模式 1:是  0:否
			String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("isPosCloud", isPosCloud);
			
			// 业务日期
//			String businessDate = binOLPTJCS16_IF.getBussinessDate(map);
//			configMap.put("businessDate",businessDate);
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 分配初始化画画（门店专用）
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String initCnt() throws Exception {
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 总部的场合
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				// 取得品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			} else {
				// 品牌信息
				Map<String, Object> brandInfo = new HashMap<String, Object>();
				brandInfoId = Integer.valueOf(ConvertUtil.getString(map.get("brandInfoId")));
				// 品牌ID
				brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
				// 品牌Code
				brandInfo.put("brandCode", userInfo.getBrandCode());
				// 品牌名称
				brandInfo.put("brandName", userInfo.getBrandName());
				// 品牌List
				brandInfoList = new ArrayList();
				brandInfoList.add(brandInfo);
			}
			
			// 取得当前用户是否是柜台用户
			if(userInfo.getDepartType().equals("4")){
				map.put("isCntDepart", 1);
				
				map.put("counter", 1);	
				String [] locationList = new String[]{userInfo.getDepartCode()};
				map.put("locationList", locationList);
				
				map.put("placeType", "7");
			}
			
			// 所有产品价格方案
			prtPriceSolutionList = binOLPTJCS16_IF.getPrtPriceSolutionList(map);
			
			String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
			configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
			
			// 取得柜台产品地点选择范围
			String placeConfig = binOLCM14_BL.getConfigValue("1291", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("placeConfig", placeConfig);
			
			// 业务日期
//			String businessDate = binOLPTJCS16_IF.getBussinessDate(map);
//			configMap.put("businessDate",businessDate);
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 通过Ajax方案对应的配置信息
	 * @throws Exception 
	 */
	public void getDPConfigDetailBySolu() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		Map<String, Object> dpConfigMap = binOLPTJCS17_IF.getDPConfigDetailBySolu(map);
		ConvertUtil.setResponseByAjax(response, dpConfigMap);
	}
	
	/**
	 * 通过Ajax取得分配树地点
	 * @throws Exception 
	 */
	public void getAllotLocation() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		try{
			map.put("productPriceSolutionID", form.getProductPriceSolutionID());
			map.put("placeTypeOld", form.getPlaceTypeOld());
			map.put("cntOwnOpt", form.getCntOwnOpt()); //  是否门店自设
			
			Map<String, Object> lrTreeMap = new HashMap<String, Object>();
			if(null != form.getCntOwnOpt() && !"".equals(form.getCntOwnOpt())){
				// 门店自设
				// 用户信息
				UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
				CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
				map.put("cntOwnInfo", counterInfo);
				lrTreeMap = binOLPTJCS17_IF.getAllotLocationCnt(map);
				
			}else{
				lrTreeMap = binOLPTJCS17_IF.getAllotLocation(map);
			}
			
			String rightRootNodes = ConvertUtil.getString(lrTreeMap.get("rightRootNodes"));
			String leftTreNodes = ConvertUtil.getString(lrTreeMap.get("leftTreNodes"));
			
			// 将上面的节点拼接起来放进response中
			String all = leftTreNodes + "*|" + rightRootNodes;
			ConvertUtil.setResponseByAjax(response, all);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		
	}
	
	/**
	 * 添加保存（配置明细）
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addSave() throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		Map<String,Object> map = getSessionInfo();
		map.put("locationArr", "["+form.getLocationArr()+"]");
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		try{
			int result = binOLPTJCS17_IF.tran_addConfigDetailSave(map);
			resultMap.put("result", result);
		}catch(CherryException e){
			logger.error(e.getMessage(),e);
			resultMap.put("result", 0);
			this.addActionError(e.getErrMessage());
//			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			resultMap.put("result", 0);
			this.addActionError(e.getMessage());
//			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentBrandInfoID(null);
		this.addActionMessage(getText("ICM00001"));
		
//		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 检查当前分配的地点是否已被其他方案分配过(包括地点或地点指定的柜台等)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chkExistCnt() throws Exception{
		
//		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		Map<String,Object> map = getSessionInfo();
		map.put("locationArr", form.getLocationArr());
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		try{
			// 取得柜台产品地点选择范围
			String placeConflict = binOLCM14_BL.getConfigValue("1292", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("placeConflict", "placeConflict_"+placeConflict);
			// 取得当前方案分配的地点是否已被其他方案分配过的List
			chkExistCntMap = binOLPTJCS17_IF.getExistCnt(map);
			chkExistCntMap.put("locationType", map.get("locationType"));
			
		}catch(Exception e){
			chkExistCntMap.put("result", "result1");
			logger.error(e.getMessage(),e);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 产品实时下发
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void issuePrt() throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 登录用户参数MAP
		Map<String, Object> sessionMap = this.getSessionInfo();
		Map<String, Object> map = new HashMap<String, Object>();
//		map = CherryUtil.remEmptyVal(map);
		map.putAll(sessionMap);
		
		// 实时下发
		try{
			
			// 品牌是否支持产品下发
			boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if(isPrtIss){
				// 产品实时下发
//				resultMap = binolptjcs04_IF.tran_issuedPrt(map);
				
				//通过WebService进行产品实时下发
				resultMap = binolptjcs04_IF.tran_issuedPrtByWS(map);
			}
			
//			String result = ConvertUtil.getString(resultMap.get("result"));
//			if("0".equals(result)){
			resultMap = binOLPTJCS17_IF.tran_issuedCntPrt(map);
//			}
			
			ConvertUtil.setResponseByAjax(response, resultMap);
		} catch(Exception e){
			resultMap.put("result", "1");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
	}

	/**
	 * 产品实时下发（颖通）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void issuePrtYT() throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 登录用户参数MAP
		Map<String, Object> sessionMap = this.getSessionInfo();
		Map<String, Object> map = new HashMap<String, Object>();
//		map = CherryUtil.remEmptyVal(map);
		map.putAll(sessionMap);
		
		// 实时下发
		try{
			
			// 品牌是否支持产品下发
			boolean isPrtIss = binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			if(isPrtIss){
				// 产品实时下发
//				resultMap = binolptjcs04_IF.tran_issuedPrt(map);
				
				//通过WebService进行产品实时下发
				resultMap = binolptjcs04_IF.tran_issuedPrtByWS(map);
			}
			
//			String result = ConvertUtil.getString(resultMap.get("result"));
//			if("0".equals(result)){
			resultMap = binOLPTJCS17_IF.tran_issuedCntPrtYT(map);
//			}
			
			ConvertUtil.setResponseByAjax(response, resultMap);
		} catch(Exception e){
			resultMap.put("result", "1");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
	}
	
	/**
	 * 验证提交的参数
	 * @throws Exception
	 */
	public void validateAddSave() throws Exception {
		
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		String businessDate = ConvertUtil.getString(map.get("businessDate"));
		// 失效日
		String soluEndTime = form.getEndTime();
		// 判断生效区间已过期 
		if (null!= soluEndTime && !CherryConstants.BLANK.equals(soluEndTime)) {
			
			int result = CherryChecker.compareDate(soluEndTime, businessDate);
			
			if(result < 0){
				this.addFieldError("productPriceSolutionID", getText(
						"ESS00092"));
			}
			
		}
		
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		
		map.put("brandName", userInfo.getBrandName());
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		// 操作者
		map.put("EmployeeId", userInfo.getBIN_EmployeeID()+ "");
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
		map.put("locationType", form.getPlaceType()); // 区域柜台
		map.put("loadingCnt", "1"); // 加载柜台
		
		// 业务日期
		String businessDate = binOLPTJCS17_IF.getBussinessDate(map);
		map.put("businessDate", businessDate);
		map.put("priceDate", businessDate);
		configMap.put("businessDate",businessDate);
		
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS17");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS17");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}	
	
	public List getPrtPriceSolutionList() {
		return prtPriceSolutionList;
	}

	public void setPrtPriceSolutionList(List prtPriceSolutionList) {
		this.prtPriceSolutionList = prtPriceSolutionList;
	}
	
	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}
	
	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}

	@Override
	public BINOLPTJCS14_Form getModel() {
		return form;
	}
	public int getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}
	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public Map getChkExistCntMap() {
		return chkExistCntMap;
	}

	public void setChkExistCntMap(Map chkExistCntMap) {
		this.chkExistCntMap = chkExistCntMap;
	}

}
