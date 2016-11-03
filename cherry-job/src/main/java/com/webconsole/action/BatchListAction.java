/*
 * @(#)BatchListAction.java     1.0 2011/07/18
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
package com.webconsole.action;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.CustomerSmsContextHolder;
import com.cherry.cm.core.CustomerTpifContextHolder;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.googlecode.jsonplugin.JSONUtil;
import com.webconsole.bl.BatchListBL;

/**
 * 
 * batch一览查询Action
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/18
 */
public class BatchListAction extends BaseAction {

	private static final long serialVersionUID = 6814660875444978640L;
	
	/** batch一览查询BL */
	@Resource
	private BatchListBL batchListBL;
	
	/** 刷新工作流文件BL */
	@Resource
	private CherryFileStore binolcm30IF;
	
	/** batch一览查询BL */
	@Resource
	private KnowledgeEngine knowledgeEngine;
	protected static final Logger logger = LoggerFactory.getLogger(BatchListAction.class);
	
	/**
	 * 
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String batch查询画面
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryBatchConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		try {
			// 取得品牌信息List
			brandInfoList = batchListBL.getBrandInfoList(map);
			if(brandInfoList != null && !brandInfoList.isEmpty()) {
				String brandInfoTemp = (String)brandInfoList.get(0).get("brandInfo");
				String[] brandInfos = brandInfoTemp.split("_");
				brandInfoId = brandInfos[0];
				brandCode = brandInfos[1];
				map.put("orgCode", userInfo.getOrgCode());
				map.put("brandCode", brandInfos[1]);
				// 取得老后台数据源信息
				Map<String, Object> confBrandInfo = batchListBL.getConfBrandInfo(map);
				// 设定新后台数据源
				session.put("CHERRY_SECURITY_CONTEXT",confBrandInfo.get("dataSourceName"));
				CustomerContextHolder.setCustomerDataSourceType((String)confBrandInfo.get("dataSourceName"));
				// 设定老后台数据源
				session.put("CHERRY_WIT_SECURITY_CONTEXT",confBrandInfo.get("oldDataSourceName"));
				CustomerWitContextHolder.setCustomerWitDataSourceType((String)confBrandInfo.get("oldDataSourceName"));
				// 设定第三方接口数据源
				session.put("CHERRY_TPIF_SECURITY_CONTEXT",confBrandInfo.get("ifDataSourceName"));
				CustomerTpifContextHolder.setCustomerTpifDataSourceType((String)confBrandInfo.get("ifDataSourceName"));
				// 设定短信接口数据源
				session.put("CHERRY_SMS_SECURITY_CONTEXT",confBrandInfo.get("SMSDataSourceName"));
				CustomerSmsContextHolder.setCustomerSmsDataSourceType((String)confBrandInfo.get("SMSDataSourceName"));
				// 所属品牌
				map.put(CherryBatchConstants.BRANDINFOID, brandInfos[0]);
				// 取得batch一览
				batchList = batchListBL.getBatchList(map);
				
				bussinessDate = batchListBL.getBussinessDate(map);
			}
		} catch (Exception e) {
			this.addActionError(e.getMessage());
		}
		try{
			// 调用刷新工作流文件的方法
			binolcm30IF.reloadAllFiles();
		}catch (Exception e){
			this.addActionError("刷新工作流文件异常,请勿执行MQ同步操作！");
		}
		try{
			// 刷新所有规则
			knowledgeEngine.refreshAllRule(Integer.parseInt(map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString()), 
					Integer.parseInt(brandInfoId));
		}catch (Exception e){
			this.addActionError("刷新规则发生异常");
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 日志显示
	 * 
	 * @param 无
	 * @return String batch查询画面
	 * 
	 */
	public String viewLog() throws Exception {
		try {
			File file = new File(PropertiesUtil.pps.getProperty("ruleLogs.destination"));
			File[] fileArr = file.listFiles();
			if (null != fileArr) {
				// 文件访问路径
				String fileUrl = PropertiesUtil.pps.getProperty("uploadFilePath.upImage");
				logList = new ArrayList<Map<String, Object>>();
				for (File f : fileArr) {
					if (f.isFile()) {
						// 文件名称
						String fileName = f.getName();
						if (fileName.indexOf("_temp") == -1 && fileName.indexOf(".html") == fileName.length() - 5) {
							//String name = f.getName().replace(".html", "");
							Map<String, Object> logMap = new HashMap<String, Object>();
							// 日志名称
							logMap.put("logName", fileName);
							logMap.put("fileUrl", fileUrl);
							String[] nameArr = fileName.split("_");
							if (nameArr.length == 2) {
								// 创建日期
								logMap.put("createDate", nameArr[0]);
							}
							// 日志类型
							logMap.put("logType", CherryBatchConstants.LOG_TYPE0);
							// 日志类型
							logMap.put("logSize", FormetFileSize(f.length()));
							boolean useFlag = f.renameTo(f);
							if (useFlag) {
								logMap.put("useFlag", "1");
							} else {
								logMap.put("useFlag", "0");
							}
							logList.add(logMap);
						}
						
					}
				}
				if (!logList.isEmpty()) {
					// 按创建日期排序
					sortBydate(logList);
				}
			} 
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 拷贝日志
	 * 
	 * @param 无
	 * @return String batch查询画面
	 * 
	 */
	public void copyLog() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		if (!CherryChecker.isNullOrEmpty(this.logFileName)) {
			try {
				String filePath = PropertiesUtil.pps.getProperty("ruleLogs.destination");
				String srcName = this.logFileName + ".html";
				String dstName = this.logFileName + "_temp.html";
				File preDstFile = new File(PropertiesUtil.pps.getProperty("ruleLogs.destination") + "/" + dstName);
				// 如果已存在目标文件，进行删除
				if (preDstFile.isFile() && preDstFile.exists()) {
					preDstFile.delete();
				}
				// 原始文件
				File srcFile = new File(filePath, srcName);
				// 目标文件
				File dstFile = new File(filePath, dstName);
				// 复制文件
				CherryUtil.copyFileByChannel(srcFile, dstFile);
				resultMap.put("RESULT", "OK");
				resultMap.put("fileName", dstName);
				flag = false;
			} catch (Exception e){
				logger.error(e.getMessage(),e);
			}
		}
		if (flag) {
			resultMap.put("RESULT", "NG");
			resultMap.put("RESULTMSG", "无法打开该文件");
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultMap));
	}
	
	/**
	 * 
	 * 获取文件大小信息
	 * 
	 * @param fileS
	 * 				文件大小
	 * @return String 
	 * 				文件大小信息
	 * 
	 */
	private String FormetFileSize(long fileS) {
		// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	/**
	 * 
	 * list按创建日期进行排序
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	private void sortBydate(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
		 	public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            	String createDate1 = (String)map1.get("createDate");
            	String createDate2 = (String)map2.get("createDate");
            	if(null != createDate1 && null != createDate2 && 
            			createDate1.compareTo(createDate2) > 0) {
            		return -1;
            	} else {
            		return 1;
            	}
            }
		});
	}
	/**
	 * 
	 * batch一览查询
	 * 
	 * @param 无
	 * @return String batch一览查询画面
	 * 
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryBatchConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		String[] brandInfos = brandInfo.split("_");
		brandInfoId = brandInfos[0];
		brandCode = brandInfos[1];
		map.put("orgCode", userInfo.getOrgCode());
		map.put("brandCode", brandInfos[1]);
		// 取得老后台数据源信息
		Map<String, Object> confBrandInfo = batchListBL.getConfBrandInfo(map);
		// 设定新后台数据源
		session.put("CHERRY_SECURITY_CONTEXT",confBrandInfo.get("dataSourceName"));
		CustomerContextHolder.setCustomerDataSourceType((String)confBrandInfo.get("dataSourceName"));
		// 设定老后台数据源
		session.put("CHERRY_WIT_SECURITY_CONTEXT",confBrandInfo.get("oldDataSourceName"));
		CustomerWitContextHolder.setCustomerWitDataSourceType((String)confBrandInfo.get("oldDataSourceName"));
		// 设定第三方接口数据源
		session.put("CHERRY_TPIF_SECURITY_CONTEXT",confBrandInfo.get("ifDataSourceName"));
		CustomerTpifContextHolder.setCustomerTpifDataSourceType((String)confBrandInfo.get("ifDataSourceName"));
		// 设定短信接口数据源
		session.put("CHERRY_SMS_SECURITY_CONTEXT",confBrandInfo.get("SMSDataSourceName"));
		CustomerSmsContextHolder.setCustomerSmsDataSourceType((String)confBrandInfo.get("SMSDataSourceName"));
		// 所属品牌
		map.put(CherryBatchConstants.BRANDINFOID, brandInfos[0]);
		// 取得batch一览
		batchList = batchListBL.getBatchList(map);
		try{
			// 刷新所有规则
			knowledgeEngine.refreshAllRule(Integer.parseInt(map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString()), 
					Integer.parseInt(brandInfoId));
		}catch (Exception e){
			this.addActionError("刷新规则发生异常");
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 刷新工作流文件
	 * 
	 * @param 无
	 * @return 无
	 * 
	 */
	public void refresh() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		try {
			binolcm30IF.reloadAllFiles();
			// 刷新工作流文件正常结束
			resultMap.put("result", "刷新工作流文件正常结束");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 刷新工作流文件异常结束
			resultMap.put("result", "刷新工作流文件异常结束");
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/** 品牌信息List */
	public List<Map<String, Object>> brandInfoList;
	
	/** 品牌Id_品牌code */
	public String brandInfo;
	
	/** batch一览 */
	public List<Map<String, Object>> batchList;
	
	/** batch业务日期 */
	public String bussinessDate;
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 日志文件名 */
	private String logFileName;
	
	/** 日志一览 */
	public List<Map<String, Object>> logList;

	public String getBussinessDate() {
		return bussinessDate;
	}

	public void setBussinessDate(String bussinessDate) {
		this.bussinessDate = bussinessDate;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandInfo() {
		return brandInfo;
	}

	public void setBrandInfo(String brandInfo) {
		this.brandInfo = brandInfo;
	}

	public List<Map<String, Object>> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<Map<String, Object>> batchList) {
		this.batchList = batchList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public List<Map<String, Object>> getLogList() {
		return logList;
	}

	public void setLogList(List<Map<String, Object>> logList) {
		this.logList = logList;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
}
