/*  
 * @(#)BINOLPTJCS19_Action.java     1.0 2014/08/31      
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS06_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS19_IF;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLPTJCS19_Action extends BaseAction implements
ModelDriven<BINOLPTJCS18_Form>{
	private static final long serialVersionUID = 8204570523907177638L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS19_Action.class);
	/** 参数FORM */
	private BINOLPTJCS18_Form form = new BINOLPTJCS18_Form();
	
	@Resource(name="binOLPTJCS19_IF")
	private BINOLPTJCS19_IF binOLPTJCS19_IF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	@Resource(name="binOLPTJCS06_IF")
	private BINOLPTJCS06_IF binolptjcs06_IF;
	
	private int productPriceSolutionID;
	
	private Map	productPriceSolutionInfo;
	
	private List<Map<String, Object>> solutionDetailList;
	
	/** 产品分类List */
	private List<Map<String, Object>> cateList;

	// **************************************************  标准产品方案明细维护  *****************************************************************************//
	
	public String init()  throws Exception {
		
		String result = SUCCESS;
		
		Map<String, Object> map =  getSessionInfo();
		
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		productPriceSolutionInfo = binOLPTJCS19_IF.getSolutionInfo(map);
		
		// 是否小店云系统模式
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		isPosCloud = "0";
		productPriceSolutionInfo.put("isPosCloud", isPosCloud);
		//solutionDetailList = binOLPTJCS19_IF.getSolutionInfoDetail(map);
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
				||ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config) ){
			
			// 取得分类值List
			List<Map<String, Object>> cateValList = binOLPTJCS19_IF.getCateList(map);
			
			// 产品分类List
			cateList = binOLPTJCS03_IF.getCategoryList(map);
			// 产品分类List处理
			addValue(cateList, cateValList);
			
			return "BINOLPTJCS22";
		}
		
		return result;
		
	}
	
	/**
	 * 方案明细
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String search() {
		
		String result = SUCCESS;
		
		try {
			Map<String, Object> map = getSessionInfo();
			// form参数设置到paramMap中
			ConvertUtil.setForm(form, map);
			map.put("productPriceSolutionID", form.getProductPriceSolutionID());
			map.put("productID", form.getProductID());
			
			int count = binOLPTJCS19_IF.getSolutionInfoDetailCount(map);
			if(count != 0){
				solutionDetailList = binOLPTJCS19_IF.getSolutionInfoDetailList(map);
			}
			
			// 是否小店云系统模式
			String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			productPriceSolutionInfo = new HashMap();
			productPriceSolutionInfo.put("isPosCloud", isPosCloud);
			/*
			 * 
			if(!CherryUtil.isBlankList(solutionDetailList)){
				for(Map<String,Object> sdMap : solutionDetailList){
					String priceInfo = ConvertUtil.getString(sdMap.get("PriceJson"));
					if (!CherryConstants.BLANK.equals(priceInfo)) {
						List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
						if (null != priceInfoList) {
							for(Map<String,Object> priceMap : priceInfoList){
								sdMap.put("salePrice", Float.valueOf((String)priceMap.get("salePrice")));
								sdMap.put("memPrice", Float.valueOf((String)priceMap.get("memPrice")));
//							sdMap.put("memPrice", priceMap.get("memPrice"));
							}
						}
					}
				}
			}
			*/
			
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
					|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config) ){
				result = "BINOLPTJCS22_01";
			}
			
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
    /**
     * 添加方案产品明细
     * @return
     * @throws Exception
     */
    public void addRow() throws Exception{
    	Map<String, Object> map = this.getSessionInfo();
        try {
//            if (!validateForm()) {
//                return CherryConstants.GLOBAL_ACCTION_RESULT;
//            }
            binOLPTJCS19_IF.tran_addPrtPriceSoluDetail(map, form);
            map.put("result", 0);
        } catch (Exception ex) {
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            map.put("result", 1);
        }
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, map);
    }
	
	/**
	 * 更新方案产品明细
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void editRow() throws Exception{
		
		// 取得session信息
		Map<String, Object> map = this.getSessionInfo();

		try{
			binOLPTJCS19_IF.tran_editPrtPriceSoluDetail(map, form);
			map.put("result", 0);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			map.put("result", 1);
		}
		
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, map);
	}
	
	/**
	 * 
	 * 批量删除方案产品处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String deleteRow() throws Exception {
		try{
			// 取得session信息
			Map<String, Object> map = this.getSessionInfo();
			map.put("productInfoIds", form.getProductInfoIds());
			map.put("productPriceSolutionID", form.getProductPriceSolutionID());
			// 停用产品处理
			binOLPTJCS19_IF.tran_delPrtPriceSoluDetail(map);
			this.addActionMessage(getText("ICM00002"));
			
		}catch(Exception e){
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
    /**
     * 方案明细添加产品分类
     * @return
     * @throws Exception
     */
    public void addCate() throws Exception{
    	Map<String, Object> map = this.getSessionInfo();
        try {
//            if (!validateForm()) {
//                return CherryConstants.GLOBAL_ACCTION_RESULT;
//            }
        	
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			map.put("soluAddModeConf", config);
			
            binOLPTJCS19_IF.tran_addPrtPriceSoluCateDetail(map, form);
            map.put("result", 0);
        } catch (Exception ex) {
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            map.put("result", 1);
        }
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, map);
    }
	
	
	/**
	 * 产品分类值选中处理
	 * 
	 * @param cateList
	 * @param cateValList
	 */
	private void addValue(List<Map<String, Object>> cateList,List<Map<String, Object>> cateValList) {
		if (null != cateList) {
			for (Map<String, Object> map : cateList) {
				// 分类ID
				int proplId1 = CherryUtil.obj2int(map.get(ProductConstants.PROPID));
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for (Map<String, Object> item : cateValList) {
					// 分类ID
					int proplId2 = CherryUtil.obj2int(item.get(ProductConstants.PROPID));
					if(proplId1 == proplId2){
						list.add(item);
					}
				}
				map.put(ProductConstants.LIST, list);
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
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
		
		
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS19");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS19");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}
	
	public int getProductPriceSolutionID() {
		return productPriceSolutionID;
	}


	public void setProductPriceSolutionID(int productPriceSolutionID) {
		this.productPriceSolutionID = productPriceSolutionID;
	}


	public Map getProductPriceSolutionInfo() {
		return productPriceSolutionInfo;
	}


	public void setProductPriceSolutionInfo(Map productPriceSolutionInfo) {
		this.productPriceSolutionInfo = productPriceSolutionInfo;
	}
	
	public List<Map<String, Object>> getSolutionDetailList() {
		return solutionDetailList;
	}

	public void setSolutionDetailList(List<Map<String, Object>> solutionDetailList) {
		this.solutionDetailList = solutionDetailList;
	}
	
	@Override
	public BINOLPTJCS18_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}
}