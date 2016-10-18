/*  
 * @(#)BINOLPTUNQ03_Action    1.0 2016-06-17     
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

package com.cherry.pt.unq.action;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.unq.form.BINOLPTUNQ03_Form;
import com.cherry.pt.unq.interfaces.BINOLPTUNQ03_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
 import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 唯一码管理  唯一码维护Action
 * 
 * @author zw
 * @version 1.0 2016.06.17
 */
public class BINOLPTUNQ03_Action extends BaseAction implements ModelDriven<BINOLPTUNQ03_Form> {

	private static final long serialVersionUID = 6425150136531877774L;
	private static Logger logger = LoggerFactory.getLogger(BINOLPTUNQ03_Action.class.getName());

	/** 参数FORM */
	private BINOLPTUNQ03_Form form = new BINOLPTUNQ03_Form();
	
	/** 接口 */
	@Resource
	private BINOLPTUNQ03_IF binOLPTUNQ03_BL;
	
	//处理总条数
	private  int totalCount;
	// 成功维护条数
	private  int successCount;
	// 维护失败条数
	private  int failCount;
	
	
	@Override
	public BINOLPTUNQ03_Form getModel() {
		return form;
	}
	
	private List<Map<String, Object>> errorInfo;
	
	/** Excel输入流 */
    private transient InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    @Resource
    private transient  BINOLMOCOM01_IF binOLMOCOM01_BL;

	/**
	 * 单码查询页面初始化
	 * 
	 * @return 
	 * @throws Exception 
	 */
	public String init() throws Exception{
	
			return SUCCESS;
	}
	
	/**
	 * 
	 * 导入唯一码激活明细
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String importUnqCodeDetails() throws Exception {
		
		try {
			// 参数MAP
			Map<String, Object> map = getSearchMap();
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入唯一码激活明细处理
			Map<String, Object> infoMap = binOLPTUNQ03_BL.resolveExcel(map);
			// 前后台交互Map
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(infoMap!=null){
			Map<String,Object> statisticsInfo = (Map<String, Object>) infoMap.get("statisticsInfo");
			if(statisticsInfo != null){
			// 维护总条数
			totalCount=(Integer) statisticsInfo.get("totalCount");
			// 维护成功条数
			successCount=(Integer) statisticsInfo.get("successCount");
		    // 维护失败条数
			failCount=(Integer) statisticsInfo.get("failCount");
			}
			//取出错误信息List
			Object errorInfos =  infoMap.get("errorInfo");
			errorInfo = (List) errorInfos;
			List<Map<String, Object>> errorUnqLists = (List)(infoMap.get("errorUnqList"));
			List<Map<String, Object>> successUnqLists = (List)(infoMap.get("successUnqList"));
			if(errorUnqLists!=null){
				 errorList(errorInfo,errorUnqLists);
			 }

			// 存在维护失败的数据时，导出维护失败的数据
			if(null != errorUnqLists){
				Map exportMapParam=getSearchMap();
				// 从维护失败数据中取出需要的数据字段导出
				 List<Map<String, Object>> exportUnqLists = new ArrayList<Map<String, Object>>();
				 for(int i= 0;i<errorUnqLists.size();i++){
					 Map<String, Object> exportMap= new HashMap<String, Object>();
					 exportMap.put("unitCode",errorUnqLists.get(i).get("unitCode"));
					 exportMap.put("barCode",errorUnqLists.get(i).get("barCode"));
					 exportMap.put("nameTotal",errorUnqLists.get(i).get("nameTotal"));
					 exportMap.put("pointUniqueCode",errorUnqLists.get(i).get("pointUniqueCode"));
					 exportMap.put("relUniqueCode",errorUnqLists.get(i).get("relUniqueCode"));
					 exportMap.put("boxCode",errorUnqLists.get(i).get("boxCode"));
					 exportMap.put("activationStatus",errorUnqLists.get(i).get("activationStatus"));
					 exportMap.put("errorInfoList",errorUnqLists.get(i).get("errorInfoList"));
					 exportUnqLists.add(exportMap);
				 }
				
				// 导出维护失败的Excel
		        try {
		            String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		            // 导出文件名称
		            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLPTUNQ03", language, "downloadFileName");
		            downloadFileName = downloadFileName+".xls";
		            // 设置编码，防止乱码
//		            downloadFileName = new String(downloadFileName.getBytes("gb2312"), "iso8859-1");
		            setExcelStream(new ByteArrayInputStream(binOLPTUNQ03_BL.exportExcel(exportMapParam,exportUnqLists,totalCount,successCount,failCount)));

		        } catch (Exception e) {
		            this.addActionError(getText("EMO00022"));
		            e.printStackTrace();
		            return CherryConstants.GLOBAL_ACCTION_RESULT;
		        }
		        
               // 由于数据量可能较大，程序结束时清空相关List || successUnqLists.size() != 0
		        errorUnqLists.clear();
		        if(null != successUnqLists ){
		        	successUnqLists.clear();
		        	}
		        if(null != infoMap || infoMap.size() != 0){
		        	infoMap.clear();
		        	}
		        if(null != errorInfo ){
		        	errorInfo.clear();
		        	}
		        
		        return "BINOLPTUNQ03_excel";

			}else{
				// 维护成功时
		        // 由于数据量可能较大，程序结束时清空相关List || successUnqLists.size() != 0
		        if(null != successUnqLists ){
		        	successUnqLists.clear();
		        }else{
		        	// 导入数据为空
		        	resultMap.put("errorCode", "1");
		        	ConvertUtil.setResponseByAjax(response, resultMap);
		        	return null;
		        }
		        if(null != infoMap || infoMap.size() != 0){
		        	infoMap.clear();
		        	}
		        if(null != errorInfo){
		        	errorInfo.clear();
		        	}
		     	resultMap.put("errorCode", "0");
	        	ConvertUtil.setResponseByAjax(response, resultMap);
	        	return null;
			 }
			}else{
				// 取到的InfoMap为空
				return SUCCESS;
			}
			
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			this.addActionMessage(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		

	}
	
	/**
	 * 导入错误信息筛选
	 * 
	 * @return
	 */
	private void errorList(List<Map<String, Object>> errorInfo,List<Map<String, Object>> errorUnqLists) {
		
		// 数据行号
		String rowNo ="";
		// 有错误的数据行号
		String errorRowNo ="";

		for(int i = 0;i<errorInfo.size();i++){
			//错误信息集合
			String errorInfoList = "";
			
			rowNo =errorInfo.get(i).get("rowNo").toString();
			
			if(errorInfo.get(i).get("pCodeAndRCodeNullError")!=null){
				//积分唯一码和关联唯一不能同时为空
				errorInfoList += getText("UNQ00001");
			}
			
			if(errorInfo.get(i).get("relUniqueCodeError")!=null){
				//关联唯一码在系统中不存在
				errorInfoList += getText("UNQ00002");
			}
			if(errorInfo.get(i).get("pointUniqueCodeError")!=null){
				// 积分唯一码在系统中不存在
				errorInfoList += getText("UNQ00003");
			}
			
			if(errorInfo.get(i).get("pCodeAndRCodeError")!=null){
				//  积分唯一码和关联唯一码不匹配
				errorInfoList += getText("UNQ00004");
			}
			
			if(errorInfo.get(i).get("useStatusError")!=null){
				// 唯一码已使用无法维护
				errorInfoList += getText("UNQ00005");
			}
			
			if(errorInfo.get(i).get("activationStatusSError")!=null){
				// 激活状态为已激活的唯一码明细无法维护
				errorInfoList += getText("UNQ00006");
			}
			if(errorInfo.get(i).get("activationStatusError")!=null){
				// 激活状态必须为Code为1395对应的值
				errorInfoList += getText("UNQ00007");
			}
			
			if(errorInfo.get(i).get("unitCodeError")!=null){
				// 厂商编码在系统中不存在
				errorInfoList += getText("UNQ00008");
			}
			
			if(errorInfo.get(i).get("unitCodeAndActError")!=null){
				// 产商编码为空时，激活状态不能为激活
				errorInfoList += getText("UNQ00010");
			}
			
			for(int j = 0;j<errorUnqLists.size();j++){
				errorRowNo =errorUnqLists.get(j).get("rowNo").toString();
				Map<String, Object> map = errorUnqLists.get(j);
				if(rowNo.equals(errorRowNo)){
					map.put("errorInfoList",errorInfoList.length() > 0 ? errorInfoList.subSequence(0, errorInfoList.length() ):errorInfoList);
				}

			}

		}

	}
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_EmployeeID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTUNQ03");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTUNQ03");
		
		return map;
	}
	
	
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;

	/** 产品品牌ID */
	private int brandInfoId;
	


	public List<Map<String, Object>> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<Map<String, Object>> errorInfo) {
		this.errorInfo = errorInfo;
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

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}
	
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	
	public String getDownloadFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request,downloadFileName);
	}
	
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	
	
}
