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

import java.io.File;
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
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS39_IF;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLPTJCS39_Action extends BaseAction implements
ModelDriven<BINOLPTJCS38_Form>{
	private static final long serialVersionUID = 8204570523907177638L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS39_Action.class);
	/** 参数FORM */
	private BINOLPTJCS38_Form form = new BINOLPTJCS38_Form();
	
	@Resource(name="binOLPTJCS39_IF")
	private BINOLPTJCS39_IF binOLPTJCS39_IF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private Map	prtFunInfo;
	
	private List<Map<String, Object>> prtFunDetailList;

	/** 产品分类List */
	private List<Map<String, Object>> cateList;
	
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
	/**导入结果*/
	@SuppressWarnings("rawtypes")
	private Map resultMap;
	
	/**产品功能开启时间主表有效状态*/
	private String pfValidFlag;

	// **************************************************  标准产品方案明细维护  *****************************************************************************//
	
	public String init()  throws Exception {
		
		String result = SUCCESS;
		
		Map<String, Object> map =  getSessionInfo();
		
		map.put("productFunctionID", form.getProductFunctionID());
		
		prtFunInfo = binOLPTJCS39_IF.getPrtFunInfo(map);
		
		//solutionDetailList = binOLPTJCS19_IF.getSolutionInfoDetail(map);
		
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
			map.put("productFunctionID", form.getProductFunctionID());
			map.put("productID", form.getProductID());
			
			int count = binOLPTJCS39_IF.getPrtFunInfoDetailCount(map);
			if(count != 0){
				prtFunDetailList = binOLPTJCS39_IF.getPrtFunInfoDetailList(map);
			}
			
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			
			// 取得系统配置项产品方案添加模式
//			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//			if(config.equals("2")){
//				result = "BINOLPTJCS42_01";
//			}
			
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
        	// 产品功能开启时间主表有效状态
        	map.put("pfValidFlag", pfValidFlag);
            binOLPTJCS39_IF.tran_addPrtFunDetail(map, form);
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
			binOLPTJCS39_IF.tran_editPrtPriceSoluDetail(map, form);
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
			map.put("productFunctionID", form.getProductFunctionID());
			// 停用产品处理
			binOLPTJCS39_IF.tran_delPrtPriceSoluDetail(map);
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
	
	/**
	 * 产品功能开启时间明细Excel导入
	 * @return
	 */
	public String importPrtFunDeatil() {
		try {
			// 登录用户参数MAP
			Map<String, Object> sessionMap = this.getSessionInfo();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			// 产品功能开启时间主表ID
			sessionMap.put("productFunctionID", form.getProductFunctionID());
        	// 产品功能开启时间主表有效状态
			sessionMap.put("pfValidFlag", pfValidFlag);
			
			//导入的数据
			Map<String, Object> importDataMap = binOLPTJCS39_IF.ResolveExcel(sessionMap);
			
			Map<String, Object> resultMap = binOLPTJCS39_IF.tran_excelHandle(importDataMap, sessionMap);
			// 导入成功
//			this.addActionMessage(getText("SSM00014"));
			
			// 导入成功
			this.addActionMessage(getText("EBS00039", new String[] {
					ConvertUtil.getString(resultMap.get(ProductConstants.OPTCOUNT)),
					ConvertUtil.getString(resultMap.get(ProductConstants.ADDCOUNT)),
					ConvertUtil.getString(resultMap.get(ProductConstants.UPDCOUNT))}));
			
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
            binOLPTJCS39_IF.tran_addPrtPriceSoluCateDetail(map, form);
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
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS39");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS39");
		// 作成者
		map.put(CherryConstants.CREATEDBY, map.get(CherryConstants.USERID));
		// 更新者
		map.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.USERID));
		return map;
	}
	
	@Override
	public BINOLPTJCS38_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}
	
	public Map getPrtFunInfo() {
		return prtFunInfo;
	}

	public void setPrtFunInfo(Map prtFunInfo) {
		this.prtFunInfo = prtFunInfo;
	}
	public List<Map<String, Object>> getPrtFunDetailList() {
		return prtFunDetailList;
	}

	public void setPrtFunDetailList(List<Map<String, Object>> prtFunDetailList) {
		this.prtFunDetailList = prtFunDetailList;
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
	
	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}
	
	public String getPfValidFlag() {
		return pfValidFlag;
	}

	public void setPfValidFlag(String pfValidFlag) {
		this.pfValidFlag = pfValidFlag;
	}
}