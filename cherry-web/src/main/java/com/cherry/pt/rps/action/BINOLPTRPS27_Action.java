/*	
 * @(#)BINOLPTRPS27_Action.java     1.0.0 2013/08/08		
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
package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.rps.form.BINOLPTRPS27_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS27_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author zhangle
 * @version 1.0.1 2013.08.08
 * 
 */
public class BINOLPTRPS27_Action extends BaseAction implements ModelDriven<BINOLPTRPS27_Form>{

	private static final long serialVersionUID = 3539269763650234166L;
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTRPS27_Action.class);
	@Resource
	private BINOLPTRPS27_IF binOLPTRPS27_BL;
    @Resource
    private BINOLCM37_BL binOLCM37_BL;
    @Resource
    private CodeTable codeTable;
    
	private BINOLPTRPS27_Form form = new BINOLPTRPS27_Form();
	
	/**进销存操作统计数据List*/
	@SuppressWarnings("rawtypes")
	private List inventoryOperationStatisticList;
	/**部门类型参数List*/
	@SuppressWarnings("rawtypes")
	private List departList;
	/**部门参数List*/
	@SuppressWarnings("rawtypes")
	private List dpParameterList;
	/**进销存操作统计任务Map*/
	@SuppressWarnings("rawtypes")
	private Map taskMap;
	/**业务类型参数List*/
	@SuppressWarnings({ "rawtypes" })
	private List bussinessParameterList;
	/** Excel输入流 */
    private InputStream excelStream;
	/** 下载文件名 */
    private String downloadFileName;
    /** 最后一次统计的数据日期*/
    private String lastStatisticDate;
    
    /**
     * 初始化进销存操作统计画面
     * @return
     */
	public String init(){
		try{
			Map<String,Object> map = getSearchMap();
			setLastStatisticDate(binOLPTRPS27_BL.getLastStatisticDate(map));
			map.put("parameterType", "DT");
			departList = binOLPTRPS27_BL.getDepartParameter(map);
			map.put("parameterType", "DP");
			dpParameterList = binOLPTRPS27_BL.getDepartParameter(map);
			taskMap = binOLPTRPS27_BL.getSchedules(map);
			map.put("validFlag", "1");
			bussinessParameterList = binOLPTRPS27_BL.getBussnissParameter(map);
			return SUCCESS;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 返回查询结果
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String search() {
		try{
			Map<String, Object> map = getSearchMap();
			map.put("startTime", form.getStartTime());
			map.put("endTime", form.getEndTime());
			map = CherryUtil.removeEmptyVal(map);
			map.put("validFlag", "1");
			//业务参数
			bussinessParameterList = binOLPTRPS27_BL.getBussnissParameter(map);
			if(bussinessParameterList == null || bussinessParameterList.size() == 0){
				throw new CherryException("EMO00081");
			}
			map.put("bussinessParameterList", bussinessParameterList);
			//部门类型参数
			departList = new ArrayList();
			map.put("parameterType", "DT");
			List<Map<String, Object>> parameter = binOLPTRPS27_BL.getDepartParameter(map);
			if(parameter!=null){
				for(Map<String, Object> fm : parameter){
					if(!CherryChecker.isNullOrEmpty(fm.get("parameterData"), true)){
						departList.add(ConvertUtil.getString(fm.get("parameterData")).trim());
					}
				}
			}
			map.put("departList", departList);
			//业务参数与部门类型参数组合转换为完整的列
			List<Object> bussinessParamter = binOLPTRPS27_BL.getLineBussinessParamter(map);
			map.put("bussinessParamter", bussinessParamter);
			int count = 0;
			count = binOLPTRPS27_BL.getCount(map);
			if(count != 0){
				inventoryOperationStatisticList = binOLPTRPS27_BL.getList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		}catch (Exception e) {
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
	 * 初始化部门参数设置画面
	 * @return
	 */
	public String initParameter() {
		return SUCCESS;
	}	
	
	/**
	 * 初始化部门树
	 * @return
	 */
	public String initPopDepartTree() {
		return SUCCESS;
	}
	
	/**
	 * 设置部门参数
	 * @return
	 */
	public String setParameter() {
		try{
			Map<String, Object> map = getSearchMap();
			map.put("dpParameter", form.getDpParameter());
			map.put("dtParameter", form.getDtParameter());
			binOLPTRPS27_BL.tran_setDepartParameter(map);
			addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch (Exception e) {
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
	 * 返回部门类型模式的部门树
	 */
	public void getDepartTree() {
		try {
			List<Map<String, Object>> departTreeList = binOLPTRPS27_BL.getDepartList(getSearchMap());
			ConvertUtil.setResponseByAjax(response, departTreeList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
	}
	
	/**
	 * 初始化时间参数设置画面
	 * @return
	 */
	public String initPopSchedules() {
		try{
			Map<String, Object> map = getSearchMap();
			taskMap = binOLPTRPS27_BL.getSchedules(map);
			return SUCCESS;
		}catch (Exception e) {
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
	 * 设置时间参数
	 * @return
	 */
	public String setSchedules(){
		try {
			Map<String, Object> map = getSearchMap();
			String runTime = form.getRunTime();
			if(!CherryChecker.isNullOrEmpty(runTime, true)){
				runTime = runTime + ":00";
				runTime = CherryUtil.convertDateToCronExp("3", null, runTime, "DD");
				map.put("validFlag", "1");
			}else{
				map.put("validFlag", "0");
			}
			map.put("runTime", runTime);
			map.put("taskCode", form.getRunTime());
	 		binOLPTRPS27_BL.tran_setSchedules(map);
	 		addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 时间参数验证
	 */
	public void validateSetSchedules() {
		String runTime = form.getRunTime();
		if(!CherryChecker.isNullOrEmpty(runTime, true) && !runTime.matches("^([0-1]\\d|2[0-3]):([0-5]\\d)$")){
			this.addActionError(getText("ECM00093",new String[]{"HH:mm"}));
		}
		
	}
	
	/**
	 * 初始化业务参数设置画面
	 * @return
	 */
	public String initBussiness() {
		try {
			Map<String, Object> map = getSearchMap();
			bussinessParameterList = binOLPTRPS27_BL.getBussnissParameter(map);;
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 设置业务参数
	 * @return
	 */
	public String setBussiness() {
		try {
			Map<String, Object> map =getSearchMap();
			map.put("parameterData", form.getParameterData());
			binOLPTRPS27_BL.tran_setBussnissParameter(map);
	 		addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 查询结果Excel导出
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String export() {
        // 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSearchMap();
        	searchMap.put("startTime", form.getStartTime());
        	searchMap.put("endTime", form.getEndTime());
        	searchMap = CherryUtil.removeEmptyVal(searchMap);
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLCM37_BL.getResourceValue("BINOLPTRPS27", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTRPS27_BL.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	/**
	 * 查询共通map
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
    	// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        map.put(CherryConstants.ORG_CODE, userInfo.getOrgCode());
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		if (!CherryChecker.isNullOrEmpty(form.getParams(), true)) {
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
			map.putAll(paramsMap);
		}
		map.put("createdBy", userInfo.getBIN_UserID());
		map.put("createPGM", "BINOLPTRPS27");
		map.put("updatedBy", userInfo.getBIN_UserID());
		map.put("updatePGM", "BINOLPTRPS27");
		return CherryUtil.removeEmptyVal(map);
	}
	
	@Override
	public BINOLPTRPS27_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public List getInventoryOperationStatisticList() {
		return inventoryOperationStatisticList;
	}

	@SuppressWarnings("rawtypes")
	public void setInventoryOperationStatisticList(List inventoryOperationStatisticList) {
		this.inventoryOperationStatisticList = inventoryOperationStatisticList;
	}

	@SuppressWarnings("rawtypes")
	public List getDepartList() {
		return departList;
	}

	@SuppressWarnings("rawtypes")
	public void setDepartList(List departList) {
		this.departList = departList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	@SuppressWarnings("rawtypes")
	public Map getTaskMap() {
		return taskMap;
	}

	@SuppressWarnings("rawtypes")
	public void setTaskMap(Map taskMap) {
		this.taskMap = taskMap;
	}

	@SuppressWarnings("rawtypes")
	public List getBussinessParameterList() {
		return bussinessParameterList;
	}

	@SuppressWarnings("rawtypes")
	public void setBussinessParameterList(List bussinessParameterList) {
		this.bussinessParameterList = bussinessParameterList;
	}

	public String getLastStatisticDate() {
		return lastStatisticDate;
	}

	public void setLastStatisticDate(String lastStatisticDate) {
		this.lastStatisticDate = lastStatisticDate;
	}

	public List getDpParameterList() {
		return dpParameterList;
	}

	public void setDpParameterList(List dpParameterList) {
		this.dpParameterList = dpParameterList;
	}

}
