/*	
 * @(#)BINOLCPCOM03_BL.java     1.0 2011/7/18		
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
package com.cherry.cp.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.service.BINOLCPCOM03_Service;

/**
 * 会员活动共通调用方法 BL
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM03_BL implements BINOLCPCOM03_IF{
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLCPCOM03_BL.class);
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLCPCOM03_Service binolcpcom03_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM02_BL binOLCM02_BL;
	
	@Resource
    private BINOLCM33_BL binOLCM33_BL;
	
	/**
     * 取得会员等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
	@Override
    public List<Map<String, Object>> getMemberLevelList(Map<String, Object> map) {
    	Map<String, Object> dateMap = new HashMap<String, Object>();
		// 组织信息ID
		dateMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		dateMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 取得业务系统时间
		String busDate = binolcpcom02_Service.getBussinessDate(dateMap);
		map.put("busDate", busDate);
		// 取得会员等级List
		return binolcpcom02_Service.getMemberLevelList(map);

    }
	
	/**
     * 取得会员活动组信息List
     * 
     * @param map
     * @return
     * 		会员活动组信息List
     */
	@Override
    public List<Map<String, Object>> getCampaignGrpList(Map<String, Object> map) {
		// 取得会员活动组信息List
		return binolcpcom03_Service.getCampaignGrpList(map);
	}
	
	/**
	 * 保存活动对象
	 * @param map
	 * @param list
	 * @return
	 */
	@Override
	public Map<String, Object> tran_SaveMember(Map<String, Object> map,List<Map<String, Object>> list)throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>(map);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> commMap = getCommMap(map);
		params.putAll(commMap);
		// 对象记录类型【结果】
		params.put(CampConstants.RECORD_TYPE,CampConstants.RECORD_TYPE_2);
		if(null != list && list.size() > 0){
			int newSize = list.size();
			result.put("newSize", newSize);
			//对象类型
			String customerType =  ConvertUtil.getString(list.get(0).get("customerType"));
			params.put("customerType", customerType);
			result.put("customerType", customerType);
			// 查询批次号
			String searchCode = ConvertUtil.getString(map.get(CampConstants.SEARCH_CODE));
			if("".equals(searchCode)){
				int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
				int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
				// 查询批次号
				searchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
				// 搜索记录code
				params.put("recordCount", newSize);
				// 搜索记录code
				params.put(CampConstants.SEARCH_CODE, searchCode);
				// 新增沟通对象搜索记录
				binolcpcom03_Service.addMemSearchLog(params);
			}
			//导入类型
			String importType = ConvertUtil.getString(params.get("importType")); 
			// ===============MemSearchLog表处理===================//
			if("1".equals(importType)){//累加方式
				Map<String, Object> tempMap = binolcpcom03_Service.getRecordCount(params);
				int oldSize = ConvertUtil.getInt(tempMap.get("RecordCount"));
				params.put("recordCount", oldSize + newSize);	
				result.put("oldSize", oldSize);
				result.put("totalSize", oldSize + newSize);
			}else if("2".equals(importType)){//覆盖方式
				params.put("recordCount", newSize);
				result.put("totalSize",  newSize);
			}
			// 更新沟通对象搜索记录
			binolcpcom03_Service.updMemSearchLog(params);
			// ===============CustomerInfo表处理===================//
			for(Map<String, Object> memMap : list){
				memMap.putAll(commMap);
				memMap.put(CampConstants.SEARCH_CODE, searchCode);
			}
			if("2".equals(importType)){//覆盖方式
				binolcpcom03_Service.delCustomer(params);
			}
			// 新增沟通结果对象
			binolcpcom03_Service.addCustomerInfo(list);
			result.put(CampConstants.SEARCH_CODE, searchCode);
		}
		return result;
	}
	
	/**
	 * 保存coupon信息
	 * @param map
	 * @param list
	 * @return
	 */
	@Override
	public Map<String, Object> tran_SaveCoupon(Map<String, Object> map,List<Map<String, Object>> list)throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>(map);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> commMap = getCommMap(map);
		params.putAll(commMap);
		if(null != list && list.size() > 0){
			int newSize = list.size();
			result.put("newSize", newSize);
			// 查询批次号
			String batchCode = ConvertUtil.getString(map.get("batchCode"));
			if("".equals(batchCode)){
				int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
				int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
				// 查询批次号
				batchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "CP");
			}
			//导入类型
			String importType = ConvertUtil.getString(params.get("importType")); 
			for(Map<String, Object> memMap : list){
				memMap.putAll(commMap);
				memMap.put("batchId", batchCode);
				memMap.put("campCode", params.get("campaignCode"));
				//结束时间转化为23:59:59.000格式
				String endTimeUtil = DateUtil.suffixDate(ConvertUtil.getString(params.get("obtainToDate")), 1);
				memMap.put("expiredTime", endTimeUtil);
				memMap.put("receiverCode", "0");
			}
			if("1".equals(importType)){
				params.put("batchCode", batchCode);
				int couponCount =binOLCM02_BL.getCouponCount(params);
				result.put("totalSize", couponCount+newSize);
			}
			if("2".equals(importType)){//覆盖
				// 搜索记录code
				params.put("batchId", batchCode);
				binolcpcom03_Service.delCouponCreateLog(params);
			}
			// 新增Coupon信息
			binolcpcom03_Service.addCouponCreateLog(list);
			result.put("batchCode", batchCode);
		}
		return result;
	}
	
	/**
	 * 取得SQL操作共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getCommMap(Map<String, Object> map){
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put(CherryConstants.ORGANIZATIONINFOID,(map.get(CherryConstants.ORGANIZATIONINFOID)));
		infoMap.put(CherryConstants.BRANDINFOID,(map.get(CherryConstants.BRANDINFOID)));
		infoMap.put(CherryConstants.ORGANIZATIONID,(map.get(CherryConstants.ORGANIZATIONID)));
		infoMap.put(CherryConstants.CREATEPGM,"BINOLCPCOM03");
		infoMap.put(CherryConstants.UPDATEPGM,"BINOLCPCOM03");
		infoMap.put(CherryConstants.CREATEDBY,map.get(CherryConstants.USERID));
		infoMap.put(CherryConstants.UPDATEDBY,map.get(CherryConstants.USERID));
		String time = binolcpcom03_Service.getSYSDate();
		infoMap.put(CherryConstants.CREATE_TIME,time);
		infoMap.put(CherryConstants.UPDATE_TIME,time);
		return infoMap;
	}
	/**
     * 取得会员活动组信息List
     * 
     * @param map
     * @return
     * 		会员活动组信息List
     */
	@Override
    public List<Map<String, Object>> getMemInfoList(Map<String, Object> map) {
		// 取得会员信息List
		return binolcpcom03_Service.getMemInfoList(map);
	}
	 /**
     * 总数
     * 
     * @param map
     * @return 
     */
	@Override
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		return binolcpcom03_Service.getMemberInfo(map);
	}
	
	/**
	 * 新增沟通对象搜索记录
	 * @param map
	 */
	@Override
	public String addMemSearchLog(Map<String, Object> map, String recordType) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>(map);
		Map<String, Object> commMap = getCommMap(map);
		params.putAll(commMap);
		int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		// 查询批次号
		String searchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
		
		params.put(CampConstants.SEARCH_CODE,searchCode);
		// 对象记录类型
		params.put(CampConstants.RECORD_TYPE,recordType);
		binolcpcom03_Service.addMemSearchLog(params);
		return searchCode;
	}
	
	/**
	 * 新增沟通对象
	 * @param list
	 */
	@Override
	public void addCustomerInfo(List<Map<String, Object>> list) throws Exception{
		binolcpcom03_Service.addCustomerInfo(list);
	}
	
	/**
	 * 更新对象记录数
	 * @param map
	 */
	@Override
	public void updRecordCount(Map<String, Object> map) throws Exception{
		binolcpcom03_Service.updRecordCount(map);
	}
	
	 /**
     * 活动对象总数
     * 
     * @param map
     * @return 
     */
	@Override
	public Map<String, Object> getRecordCount(Map<String, Object> map) {
		return binolcpcom03_Service.getRecordCount(map);
	}
	
	/**
     * 取得活动档次ID
     * 
     * @param map
     * @return 
     */
	@Override
	public List<String> getCampRuleIdList(Map<String, Object> map) {
		return binolcpcom03_Service.getCampRuleIdList(map);
	}
	
	/**
     * 查询活动对象数量
     * @return
     * @throws Exception
     */
	@Override
    public int searchMemCount(Map<String, Object> map) throws Exception{
		int total = 0;
		String campMebType = ConvertUtil.getString(map.get(CampConstants.CAMP_MEB_TYPE));
		try {
			if(CampConstants.CAMP_MEB_TYPE_1.equals(campMebType)|| "".equals(campMebType)){
				String conInfo = ConvertUtil.getString(map.get("conInfo"));
				if("".equals(conInfo)){
					// 取得searchLog信息
					Map<String, Object> searchLogMap = getRecordCount(map);
					if(null!=searchLogMap){
						conInfo = ConvertUtil.getString(searchLogMap.get("conditionInfo"));
					}
				}
				total = getMemCntByJSON(conInfo);
			}else if(CampConstants.CAMP_MEB_TYPE_2.equals(campMebType) 
					|| CampConstants.CAMP_MEB_TYPE_3.equals(campMebType)){
				String conInfo = null;
				// 取得searchLog信息
				Map<String, Object> searchLogMap = getRecordCount(map);
				if(null!=searchLogMap){
	    			total = ConvertUtil.getInt(searchLogMap.get("RecordCount"));
	    			conInfo = ConvertUtil.getString(searchLogMap.get("conditionInfo"));
				}else{
					total = 0;
				}
				if(total == 0){// 尚未保存到数据库
					total = getMemCntByJSON(conInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return total;
    }
	
	private int getMemCntByJSON(String json){
		int total = 0;
		if(!CherryChecker.isNullOrEmpty(json)){
			Map<String, Object> conMap = ConvertUtil.json2Map(json);
			conMap.put("SORT_ID", "memId");
			conMap.put("START",1);
			conMap.put("END",1);
			List<String> selectors = new ArrayList<String>();
			selectors.add("memId");
			conMap.put("selectors", selectors);
			Map<String, Object> resMap = binOLCM33_BL.searchMemList(conMap);
			if(null != resMap){
				total = ConvertUtil.getInt(resMap.get("total"));
			}
		}
		return total;
	}
	
	/**
	 * 生成新的SearchCode
	 * @param paramMap
	 * @return
	 */
	@Override
	public String getNewSearchCode(Map<String , Object> paramMap) {
		// 组织Id
		int orgId = ConvertUtil.getInt(paramMap.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌Id
		int brandId = ConvertUtil.getInt(paramMap.get(CherryConstants.BRANDINFOID));
		// 生成新的对象批次号
		String newSearchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC"); 
		paramMap.put("newSearchCode", newSearchCode);
		// 复制SearchLog表信息
		binolcpcom03_Service.copySearchLogInfo(paramMap);
		// 复制CustomerInfo表信息
		binolcpcom03_Service.copyCustomerInfo(paramMap);
		return newSearchCode;
	}
	
	
	/**
     * 取得Coupon信息List
     * 
     * @param map
     * @return
     * 		会员活动组信息List
     */
	@Override
    public List<Map<String, Object>> getCouponList(Map<String, Object> map) {
		// 取得会员信息List
		return binolcpcom03_Service.getCouponList(map);
	}
	
	/**
	 * 会员Coupon数
	 * 
	 * @param map
	 * @return
	 */
	public int getCouponCount(Map<String, Object> map) {
		// 会员主题活动数
		return binolcpcom03_Service.getCouponCount(map);
	}
	
	/**
	 * 取得已经存在的Coupon记录
	 * @param map
	 * @return
	 */
	public String getCouponNum(Map<String, Object> map) {
		// 会员主题活动数
		return binolcpcom03_Service.getCouponNum(map);
	}
}
