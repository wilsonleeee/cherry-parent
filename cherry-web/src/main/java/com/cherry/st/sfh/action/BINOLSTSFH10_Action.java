
/*  
 * @(#)BINOLPTODR01_Action.java    1.0 2011-8-10     
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
package com.cherry.st.sfh.action;

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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.sfh.form.BINOLSTSFH10_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH10_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSTSFH10_Action extends BaseAction implements
		ModelDriven<BINOLSTSFH10_Form> {

	private static final long serialVersionUID = 1L;
	
	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH10_Action.class);

	private List<Map<String,Object>> brandInfoList = null;
	
	private BINOLSTSFH10_Form form = new BINOLSTSFH10_Form();
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	@Resource(name="binOLSTSFH10_BL")
	private BINOLSTSFH10_IF binOLSTSFH10_BL;
	
	private List productParamList = null;
	
	private List counterParamList = null;
	
	private List couPrtParamList = null;
	
	private List globalParamList = null;
	
	private int count;
	
	/**
	 * 页面初始化
	 * 
	 * 
	 **/
	public String init(){
		Map<String,Object> map = this.getParamMap();
		brandInfoList = this.getBrandList(map);
		form.setDate(CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		
		return SUCCESS;
	}
	
	/**
	 * 柜台订货参数页面初始化
	 * 
	 * */
	public void counterInit(){
		Map<String,Object> map = this.getParamMap();
		brandInfoList = this.getBrandList(map);
	}
	
	/**
	 * 柜台商品订货参数页面初始化
	 * 
	 * */
	public void couPrtInit(){
		Map<String,Object> map = this.getParamMap();
		brandInfoList = this.getBrandList(map);
	}
	
	public void globalInit() {
		Map<String, Object> map = this.getParamMap();
		brandInfoList = this.getBrandInfoList();
	}
	
	/**
	 * 查询全局订货参数
	 * @return
	 */
	public String globalSearch() {
		Map<String,Object> map = this.getParamMap();
		map.put("brandInfoId", form.getBrandInfoId().trim());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		globalParamList = binOLSTSFH10_BL.getGlobalParameterList(map);
		count = binOLSTSFH10_BL.getGlobalParameterCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTSFH10_04";
	}
	
	/**
	 * 查询产品订货参数
	 * 
	 * */
	public String proSearch(){
		Map<String,Object> map = this.getParamMap();
		map.put("brandInfoId", form.getBrandInfoId().trim());
		map.put("productName", form.getProductName().trim());
		String date = form.getDate();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		productParamList = binOLSTSFH10_BL.getProductParameterList(map,date);
		count = binOLSTSFH10_BL.getProductParameterCount(map, date);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTSFH10_01";
	}
	
	/**
	 * 查询柜台订货参数
	 * 
	 * */
	public String couSearch(){
		Map<String,Object> map = this.getParamMap();
		map.put("departCode", form.getDepartCode().trim());
		map.put("brandInfoId", form.getBrandInfoId());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		counterParamList = binOLSTSFH10_BL.getCounterParameterList(map);
		count = binOLSTSFH10_BL.getCounterParameterCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTSFH10_02";
	}
	
	/**
	 * 查询最低库存天数参数
	 * 
	 * */
	public String couPrtSearch(){
		Map<String,Object> map = this.getParamMap();
		map.put("departCode", form.getDepartCode().trim());
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("productName", form.getProductName().trim());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		couPrtParamList = binOLSTSFH10_BL.getCouPrtParameterList(map);
		count = binOLSTSFH10_BL.getCouPrtParameterCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTSFH10_03";
	}
	
	/**
	 * 批量设定产品订货参数
	 * 
	 * */
	public String setProductParameter() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("adtCoefficient", form.getAdtCoefficient());
			String date = form.getDate();
			String checkedPrt = form.getCheckedPrt();
			List<Map<String,Object>> prtList = (List<Map<String, Object>>) JSONUtil.deserialize(checkedPrt);
			binOLSTSFH10_BL.tran_setProductParameter(map, date,prtList);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 设定所有柜台的订货参数
	 * 
	 * */
	public String setCounterParameter() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("brandInfoId", form.getBrandInfoId().trim());
			map.put("orderDays", form.getOrderDays().trim());
			map.put("intransitDays", form.getIntransitDays().trim());
			String checkedCut = form.getCheckedCut();
			List<Map<String,Object>> cutList = (List<Map<String, Object>>) JSONUtil.deserialize(checkedCut);
			binOLSTSFH10_BL.tran_setCounterParameter(map,cutList);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 设定柜台产品参数
	 * 
	 * 
	 * */
	public String setCouPrtParameter() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("brandInfoId", form.getBrandInfoId().trim());
			map.put("departCode", form.getDepartCode().trim());
			map.put("lowestStockDays", form.getLowestStockDays().trim());
			map.put("growthFactor", form.getGrowthFactor().trim());
			map.put("regulateFactor", form.getRegulateFactor().trim());
			String checkedPrt = form.getCheckedPrt();
			String checkedCut = form.getCheckedCut();
			List<Map<String,Object>> productList = (List<Map<String, Object>>) JSONUtil.deserialize(checkedPrt);
			List<Map<String,Object>> counterList = (List<Map<String, Object>>) JSONUtil.deserialize(checkedCut);
			map.put("productList", productList);
			map.put("counterList", counterList);
			binOLSTSFH10_BL.tran_setCouPrtParameter(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 设定全局订货参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setGlobalParameter() throws Exception {
		try {
			Map<String, Object> map = this.getParamMap();
			map.put("brandInfoId", form.getBrandInfoId().trim());
			map.put("saleDaysOfMonth", form.getSaleDaysOfMonth().trim());
			map.put("daysOfProduct", form.getDaysOfProduct().trim());
			binOLSTSFH10_BL.tran_setGlobalParameter(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw ex;
			}
		}
	}
	
	/**
	 * 编辑全局订货参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editGlobalParam() throws Exception {
		try {
			Map<String, Object> map = this.getParamMap();
			map.put("globalParameterId", form.getGlobalParameterId().trim());
			map.put("saleDaysOfMonth", form.getSaleDaysOfMonth().trim());
			map.put("daysOfProduct", CherryChecker.isNullOrEmpty(form.getDaysOfProduct(), true) ? null : form.getDaysOfProduct());
			binOLSTSFH10_BL.tran_editGlobalParameter(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw ex;
			}
		}
	}
	
	/**
	 * 编辑某柜台某产品的最低库存天数
	 * 
	 * 
	 * */
	public String editCouPtrParam() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("counterPrtOrParameterId", form.getCounterPrtOrParameterId().trim());
			map.put("lowestStockDays", form.getLowestStockDays().trim());
			map.put("growthFactor", CherryChecker.isNullOrEmpty(form.getGrowthFactor(), true) ? null : form.getGrowthFactor());
			map.put("regulateFactor", CherryChecker.isNullOrEmpty(form.getRegulateFactor(),true) ? null : form.getRegulateFactor());
			binOLSTSFH10_BL.tran_editCouPrtParameter(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 编辑柜台订货参数
	 * 
	 * 
	 * */
	public String editCouParam() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("orderDays", form.getOrderDays().trim());
			map.put("intransitDays", form.getIntransitDays().trim());
			map.put("counterOrderParameterId", form.getCounterOrderParameterId().trim());
			binOLSTSFH10_BL.tran_editCounterParameter(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 编辑产品订货参数
	 * 
	 * */
	public String editPrtParam() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("adtCoefficient", form.getAdtCoefficient());
			map.put("productOrderParameterId", form.getProductOrderParameterId().trim());
			binOLSTSFH10_BL.tran_editProductParameter(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	/**
	 * 下发参数
	 * 
	 * 
	 * */
	public String issOrderParam() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("brandInfoId",form.getBrandInfoId());
			binOLSTSFH10_BL.issumOrderParam(map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	public List<Map<String,Object>> getBrandList(Map<String,Object> map){
		List<Map<String,Object>> brandList = null;
		Integer brandInfoId = (Integer) map.get("brandInfoId");
		if(brandInfoId == -9999){
			brandList = binOLCM05_BL.getBrandInfoList(map);
		}else{
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", map.get("brandInfoId"));
			// 品牌名称
			brandMap.put("brandName", map.get("brandName"));
			brandList = new ArrayList<Map<String, Object>>();
			brandList.add(brandMap);
		}
		return brandList;
	}
	
	/**
	 * 获取柜台或者产品树节点
	 * 
	 * */
	public String getTreeNodes() throws Exception{
		try{
			Map<String,Object> map = this.getParamMap();
			map.put("brandInfoId", form.getBrandInfoId());
			String treeNodesFlag = form.getTreeNodesFlag();
			if(("counter").equals(treeNodesFlag)){
				List<Map<String,Object>> list1 = binOLSTSFH10_BL.getCounterTreeNodes(map);
				ConvertUtil.setResponseByAjax(response, list1);
			}else if(("product").equals(treeNodesFlag)){
				List<Map<String,Object>> list2 = binOLSTSFH10_BL.getProductTreeNodes(map);
				ConvertUtil.setResponseByAjax(response, list2);
			}else{
				List<Map<String,Object>> list1 = binOLSTSFH10_BL.getCounterTreeNodes(map);
				List<Map<String,Object>> list2 = binOLSTSFH10_BL.getProductTreeNodes(map);
				Map<String,Object> treeNodesMap = new HashMap<String,Object>();
				treeNodesMap.put("counterNodes", list1);
				treeNodesMap.put("productNodes", list2);
				ConvertUtil.setResponseByAjax(response, treeNodesMap);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				CherryException ex = (CherryException)e;
				this.addActionError(ex.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return null;
	}
	
	public Map<String,Object> getParamMap(){
		Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 语言类型
        String language = (String) session
                .get(CherryConstants.SESSION_LANGUAGE);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//品牌名称
		map.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
        map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
        map.put(CherryConstants.CREATEPGM,"BINOLSTSFH10");
        map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
        map.put(CherryConstants.UPDATEPGM, "BINOLSTSFH10");
		return map;
	}
	
	@Override
	public BINOLSTSFH10_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getProductParamList() {
		return productParamList;
	}

	public void setProductParamList(List productParamList) {
		this.productParamList = productParamList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List getCounterParamList() {
		return counterParamList;
	}

	public List getGlobalParamList() {
		return globalParamList;
	}

	public void setGlobalParamList(List globalParamList) {
		this.globalParamList = globalParamList;
	}

	public void setCounterParamList(List counterParamList) {
		this.counterParamList = counterParamList;
	}

	public List getCouPrtParamList() {
		return couPrtParamList;
	}

	public void setCouPrtParamList(List couPrtParamList) {
		this.couPrtParamList = couPrtParamList;
	}

	
}
