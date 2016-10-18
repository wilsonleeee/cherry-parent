/*
 * @(#)CherryBaseResource.java     1.0 2013/01/24
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
package com.cherry.webservice.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.service.BaseService;


/**
 * Webservice中处理数据源等功能
 * @author dingyongchang
 * dingyongchang C 2013-05-22
 *
 */
public class WebserviceDataSource extends BaseService {
	
	/** 数据源信息List **/
	private static List<Map<String,Object>> datasourceList;
	
	/** 品牌信息 **/
	private static Map<String,String[]> brandMap = new HashMap<String,String[]>();
		
	/**
	 * 
	 * 设定数据源
	 * 
	 * @param map 
	 * @throws Exception 
	 */
	public boolean setBrandDataSource(String brandCode) throws Exception {
		if(datasourceList==null||datasourceList.size()<1){
			setDatasourceList();
		}
		for (int i=0;i<datasourceList.size();i++){
			Map datasourceMap = (Map)datasourceList.get(i);
			String datasourceBrandCode = (String)datasourceMap.get("brandCode");
			if (datasourceBrandCode!=null && datasourceBrandCode.equals(brandCode)){
				String dataSourceName = (String)datasourceMap.get("dataSourceName");
				// 将获取的数据源名设定到线程本地变量contextHolder中
				CustomerContextHolder.setCustomerDataSourceType(dataSourceName);
				return true;
			}
		}
		
		return false;
	}	

	
	public String getAESKey(String brandCode) throws Exception {
		if(datasourceList==null||datasourceList.size()<1){
			setDatasourceList();
		}
		String AESKey = "";
		for (int i=0;i<datasourceList.size();i++){
			Map datasourceMap = (Map)datasourceList.get(i);
			String datasourceBrandCode = (String)datasourceMap.get("brandCode");
			if (datasourceBrandCode!=null && datasourceBrandCode.equals(brandCode)){
				AESKey = (String)datasourceMap.get("AESKey");
			}
		}
		return AESKey;
	}
	
	private synchronized void setDatasourceList() throws Exception {
		Map<String,Object> map = new HashMap();
		map.put(CherryConstants.IBATIS_SQL_ID, "WebserviceDataSource.getBrandDataSourceConfigList");		
		List resultList = baseConfServiceImpl.getList(map);
		if (null==resultList || resultList.isEmpty()){
			throw new Exception("找不到数据源");
		}else{
			datasourceList = resultList;
		}
	}
	
	public void getBrandInfo(Map map) throws Exception{
		
		String brandCode =  String.valueOf(map.get("brandCode"));
		
		
		if(brandMap.containsKey(brandCode)){
			String[] tmpArr = brandMap.get(brandCode);
			map.put("brandInfoID", tmpArr[0]);
			map.put("organizationInfoID", tmpArr[1]);
			map.put("orgCode", tmpArr[2]);
		}else{
			String[] tmpArr = new String[]{"","",""};
			// 查询品牌信息			
			HashMap resultMap = (HashMap)baseServiceImpl.get(map,"WebserviceDataSource.getBrandInfo");

			if (resultMap != null) {
				
				tmpArr[0] = String.valueOf(resultMap.get("BIN_BrandInfoID"));
				tmpArr[1] = String.valueOf(resultMap.get("BIN_OrganizationInfoID"));
				tmpArr[2] = String.valueOf(resultMap.get("OrgCode"));
				
				// 设定品牌ID
				map.put("brandInfoID", resultMap.get("BIN_BrandInfoID"));
				// 设定组织ID
				map.put("organizationInfoID", resultMap.get("BIN_OrganizationInfoID"));
				//组织code
				map.put("orgCode", resultMap.get("OrgCode"));
			} else {
				// 没有查询到相关组织品牌信息
				throw new Exception("没有查询到相关组织品牌信息："+brandCode);
			}
			brandMap.put(brandCode, tmpArr);			
		}
	}
	public HashMap<String,ThirdPartyConfig> getThirdPartyConfigList(){
		HashMap<String,ThirdPartyConfig> retMap = new HashMap<String,ThirdPartyConfig>();
		Map<String,Object> map = new HashMap();
		map.put(CherryConstants.IBATIS_SQL_ID, "WebserviceDataSource.getThirdPartyConfigList");		
		List resultList = baseConfServiceImpl.getList(map);
		if (null==resultList || resultList.isEmpty()){
			//返回空的map
			return retMap;
		}else{
			ThirdPartyConfig config;
			for (int i=0;i<resultList.size();i++){
				Map tmp = (Map)resultList.get(i);
				config = new ThirdPartyConfig();
				config.setBrandCode(String.valueOf(tmp.get("BrandCode")));
				config.setAppID(String.valueOf(tmp.get("AppID")));
				config.setAppName(String.valueOf(tmp.get("AppName")));
				config.setAppSecret(String.valueOf(tmp.get("AppSecret")));
				config.setDynamicAESKey(String.valueOf(tmp.get("DynamicAESKey")));
				config.setAesKeyExpireTime(String.valueOf(tmp.get("AESKeyExpireTime")));
				retMap.put(config.getAppID(), config);
			}
			return retMap;
		}
	}
	
	public int refreshDynamicAESKey(Map<String, Object> pamMap) {
		pamMap.put(CherryConstants.IBATIS_SQL_ID, "WebserviceDataSource.refreshDynamicAESKey");
		int i = baseConfServiceImpl.update(pamMap);
		return i;
	}
}
