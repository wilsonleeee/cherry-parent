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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS14_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS16_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品方案导入Action
 * 
 * @author JiJW
 * @version 1.0 2014-8-7
 */
public class BINOLPTJCS16_Action extends BaseAction implements
ModelDriven<BINOLPTJCS14_Form>{

	private static final long serialVersionUID = 1324480801173680548L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS16_Action.class.getName());
	
	
	/** 参数FORM */
	private BINOLPTJCS14_Form form = new BINOLPTJCS14_Form();
	
	@Resource(name="binOLPTJCS16_IF")
	private BINOLPTJCS16_IF binOLPTJCS16_IF;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 产品品牌ID */
	private int brandInfoId;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
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
	
	/**
	 * 导入画面
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
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
			
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("soluAddMode", config);
			
			// ******************* WITPOSQA-15735票  start *******************
			// 取得当前用户是否是柜台用户
			if(userInfo.getDepartType().equals("4")){
				map.put("isCntDepart", null); // 因为不是门店自己创建的方案，所以无权导入。只可以查看 。
				
				map.put("counter", 1);	
				String [] locationList = new String[]{userInfo.getDepartCode()};
				map.put("locationList", locationList);
				
				map.put("placeType", "7");
			}
			// ******************* WITPOSQA-15735票 end *******************
			
			// 所有产品价格方案
			prtPriceSolutionList = binOLPTJCS16_IF.getPrtPriceSolutionList(map);
			
			String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
			configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
			
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
	 * 导入画面(门店用)
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
			
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			configMap.put("soluAddMode", config);
			
			// ******************* WITPOSQA-15735票  start *******************
			// 取得当前用户是否是柜台用户
			if(userInfo.getDepartType().equals("4")){
				map.put("isCntDepart", null); // 因为不是门店自己创建的方案，所以无权导入。只可以查看 。
				
				map.put("counter", 1);	
				String [] locationList = new String[]{userInfo.getDepartCode()};
				map.put("locationList", locationList);
				
				map.put("placeType", "7");
			}
			// ******************* WITPOSQA-15735票 end *******************
			
			// 所有产品价格方案
			prtPriceSolutionList = binOLPTJCS16_IF.getPrtPriceSolutionList(map);
			
			String prtPriceSolutionListJson = JSONUtil.serialize(prtPriceSolutionList);
			configMap.put("prtPriceSolutionListJson", prtPriceSolutionListJson);
			
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
	 * 方案Excel导入
	 * @return
	 */
	public String importSoluDeatil() {
		try {
			// 登录用户参数MAP
			Map<String, Object> sessionMap = this.getSessionInfo();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			// 方案ID
			sessionMap.put("productPriceSolutionID", form.getProductPriceSolutionID());
			sessionMap.put("soluStartDate", form.getStartTime());
			sessionMap.put("soluEndDate", form.getEndTime());
			
			//导入的数据
			Map<String, Object> importDataMap = binOLPTJCS16_IF.ResolveExcel(sessionMap);
			
			Map<String, Object> resultMap = binOLPTJCS16_IF.tran_excelHandle(importDataMap, sessionMap);
			resultMap.put("solutionName", form.getSolutionName());
			// 导入成功
//			this.addActionMessage(getText("SSM00014"));
			
			// 导入成功
			this.addActionMessage(getText("EBS00039", new String[] {
					ConvertUtil.getString(resultMap
							.get(ProductConstants.OPTCOUNT)),
					ConvertUtil.getString(resultMap
							.get(ProductConstants.ADDCOUNT)),
					ConvertUtil.getString(resultMap
							.get(ProductConstants.UPDCOUNT))}));
			
			setResultMap(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00079"));
			}
		}	
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 方案Excel导入(门店用)
	 * @return
	 */
	public String importSoluDeatilCnt() {
		try {
			// 登录用户参数MAP
			Map<String, Object> sessionMap = this.getSessionInfo();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			// 方案ID
			sessionMap.put("productPriceSolutionID", form.getProductPriceSolutionID());
			sessionMap.put("soluStartDate", form.getStartTime());
			sessionMap.put("soluEndDate", form.getEndTime());
			
			//导入的数据
			Map<String, Object> importDataMap = binOLPTJCS16_IF.ResolveExcelCnt(sessionMap);
			
			Map<String, Object> resultMap = binOLPTJCS16_IF.tran_excelHandleCnt(importDataMap, sessionMap);
			resultMap.put("solutionName", form.getSolutionName());
			// 导入成功
//			this.addActionMessage(getText("SSM00014"));
			
			// 导入成功
			this.addActionMessage(getText("EBS00039", new String[] {
					ConvertUtil.getString(resultMap
							.get(ProductConstants.OPTCOUNT)),
							ConvertUtil.getString(resultMap
									.get(ProductConstants.ADDCOUNT)),
									ConvertUtil.getString(resultMap
											.get(ProductConstants.UPDCOUNT))}));
			
			setResultMap(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00079"));
			}
		}	
		return CherryConstants.GLOBAL_ACCTION_RESULT;
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
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
		map.put("locationType", "2"); // 区域柜台
		map.put("loadingCnt", "1"); // 加载柜台
		
		// 业务日期
		String businessDate = binOLPTJCS16_IF.getBussinessDate(map);
		map.put("businessDate", businessDate); 
		configMap.put("businessDate",businessDate);
		
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS16");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS16");
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
	public File getUpExcel() {
		return upExcel;
	}
	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	public String getUpExcelFileName() {
		return upExcelFileName;
	}
	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

}
