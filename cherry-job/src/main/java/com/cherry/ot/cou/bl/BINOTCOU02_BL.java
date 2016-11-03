/*
 * @(#)BINOTCOU02_BL.java     1.0 2014/11/13
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
package com.cherry.ot.cou.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;




import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ot.cou.interfaces.BINOTCOU02_IF;
import com.cherry.ot.cou.service.BINOTCOU02_Service;
import com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortTypeProxy;
import com.cherry.webservice.client.WebserviceClient;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * BATCH薇诺娜优惠劵获取BL
 * 
 * @author menghao
 * @version 2014.11.13
 */
public class BINOTCOU02_BL implements BINOTCOU02_IF {

	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINOTCOU02_BL.class);

	@Resource(name="binOTCOU02_Service")
	private BINOTCOU02_Service binOTCOU02_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;

	/** 第三方WebService地址 */
	private final static String WebResourceURL = "";
	
	/** 接口表TradeCode值 */
	private final static String TradeCode="searchAgentCoupon";
	
	/** 电商接口提供方代号 */
	private final static String ESCode = "win";

	@Override
	public int tran_getCouponCode(Map<String, Object> map)
			throws CherryBatchException, Exception {
		String lastAccessTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
		// 调用第三方WebService取得订单数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("endTimeDefault", lastAccessTime);
		// 查询时会返回一个GetDataEndTime值给paramMap
		Map<String, Object> resultMap = searchAgentCoupon(paramMap);
		if(null != resultMap && resultMap.get("BatchResult").equals(CherryBatchConstants.BATCH_SUCCESS)){
            flag = CherryBatchConstants.BATCH_SUCCESS;
            
            Map<String,Object> esInterfaceInfo = (Map<String, Object>) paramMap.get("ESInterfaceInfo");
            List<Map<String,Object>> couponIFInfos = (List<Map<String,Object>>)esInterfaceInfo.get(TradeCode);
            for(int i=0;i<couponIFInfos.size();i++){
                Map<String,Object> couponIFInfo = couponIFInfos.get(i);
                String esInterfaceInfoID = ConvertUtil.getString(couponIFInfo.get("BIN_ESInterfaceInfoID"));
                //处理完毕，更新电商接口信息表的最后一次访问时间，最后一次获取数据的截止时间
                Map<String,Object> updateESInterfaceInfo = new HashMap<String,Object>();
                updateESInterfaceInfo.put("BIN_OrganizationInfoID", map.get(CherryBatchConstants.ORGANIZATIONINFOID));
                updateESInterfaceInfo.put("BIN_BrandInfoID", map.get(CherryBatchConstants.BRANDINFOID));
                updateESInterfaceInfo.put("BIN_ESInterfaceInfoID", esInterfaceInfoID);
                //最后一次调用该接口的时间，是从JAVA中获取后写入该字段，不是在SQL中直接用GETDATE()
                updateESInterfaceInfo.put("LastAccessTime", lastAccessTime);
                //在以起止时间为参数获取数据时，将截止时间记入该字段，下次从该时间起再获取新的数据
                updateESInterfaceInfo.put("GetDataEndTime", resultMap.get(esInterfaceInfoID+"_GetDataEndTime"));
                setInsertInfoMapKey(updateESInterfaceInfo);
                binOTCOU02_Service.updateESInterfaceInfoLastTime(updateESInterfaceInfo);
            }
        }else{
            flag = CherryBatchConstants.BATCH_ERROR;
        }
        
        outMessage();
        
        return flag;
	}
	
    private void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","BINOTCOU02_BL");
        map.put("CreatePGM","BINOTCOU02_BL");
        map.put("UpdatedBy","BINOTCOU02_BL");
        map.put("UpdatePGM","BINOTCOU02_BL");
        map.put("createdBy","BINOTCOU02_BL");
        map.put("createPGM","BINOTCOU02_BL");
        map.put("updatedBy", "BINOTCOU02_BL");
        map.put("updatePGM", "BINOTCOU02_BL");
    }
    
    /**
     * 输出处理结果信息
     * 
     * @throws CherryBatchException
     */
    private void outMessage() throws CherryBatchException {
        // 总件数
        BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
        batchLoggerDTO1.setCode("IIF00001");
        batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO1.addParam(String.valueOf(totalCount));
        // 成功总件数
        BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
        batchLoggerDTO2.setCode("IIF00002");
        batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
        // 失败件数
        BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
        batchLoggerDTO5.setCode("IIF00005");
        batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO5.addParam(String.valueOf(failCount));
        // 处理总件数
        logger.BatchLogger(batchLoggerDTO1);
        // 成功总件数
        logger.BatchLogger(batchLoggerDTO2);
        // 失败件数
        logger.BatchLogger(batchLoggerDTO5);
    }

	/**
	 * 访问其他系统的WebService得到优惠券信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> searchAgentCoupon(Map<String, Object> paramMap)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//取电商接口信息
        Map<String,Object> esInterfaceInfo = getESInterfaceInfo(paramMap);
        paramMap.put("ESInterfaceInfo", esInterfaceInfo);
        /**优惠券获取WebService连接信息*/
        List<Map<String, Object>> couponIFInfo = (List<Map<String,Object>>)esInterfaceInfo.get(TradeCode);
		
        for(Map<String, Object> couponIFMap : couponIFInfo) {
        	String startTime = ConvertUtil.getString(couponIFMap.get("GetDataEndTime"));
        	int timeStep = CherryUtil.obj2int(couponIFMap.get("TimeStep"));
            String endTime = "".equals(startTime) ? ConvertUtil.getString(paramMap.get("endTimeDefault")) : DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN,startTime, timeStep);
            String sysDateTime = CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);
            if(DateUtil.compareDate(endTime,sysDateTime )>0){
                //控制截止时间不能超过当前服务器时间-1
                endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN, sysDateTime, -1);
            }

    		int pageNo = 1;
    		int pageSize = 100;

    		Map<String, Object> wsParam = new HashMap<String, Object>();
    		wsParam.put("BIN_OrganizationInfoID",
    				paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
    		wsParam.put("BIN_BrandInfoID",
    				paramMap.get(CherryBatchConstants.BRANDINFOID));
    		wsParam.put("BrandCode",
    				paramMap.get(CherryBatchConstants.BRAND_CODE));
    		wsParam.put("url", couponIFMap.get("URL"));
    		wsParam.put("start_time", "".equals(startTime) ? "1900-01-01 00:00:00" : startTime);
    		wsParam.put("end_time", endTime);
    		wsParam.put("page_no", pageNo);
    		wsParam.put("page_size", pageSize);
    		String esInterfaceInfoID = ConvertUtil.getString(couponIFMap.get("BIN_ESInterfaceInfoID"));
    		wsParam.put("BIN_ESInterfaceInfoID", esInterfaceInfoID);
    		paramMap.put(esInterfaceInfoID+"_GetDataEndTime", endTime);
    		searchAgentCouponByPage(wsParam, resultMap);
        
        }
		
		return resultMap;
	}

	/**
	 * 分页获取优惠券并写入新后台代理商优惠券表
	 * 
	 * @param wsParam
	 * @param resultMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void searchAgentCouponByPage(Map<String, Object> wsParam,
			Map<String, Object> resultMap) throws Exception {
		boolean hasNext = true;
		BooleanHolder has_next = new BooleanHolder();
		StringHolder coupons = new StringHolder();
		IntHolder total_results = new IntHolder();
		String esInterfaceInfoID = ConvertUtil.getString(wsParam.get("BIN_ESInterfaceInfoID"));
		String start_time = ConvertUtil.getString(wsParam.get("start_time"));
		String end_time = ConvertUtil.getString(wsParam.get("end_time"));
		int page_no = CherryUtil.obj2int(wsParam.get("page_no"));
		int page_size = CherryUtil.obj2int(wsParam.get("page_size"));
		// SearchAgentCouponPortTypeProxy对象
		SearchAgentCouponPortTypeProxy webResource = new SearchAgentCouponPortTypeProxy();
		
		// 分页调用WebService获取优惠券
		while (true) {
			if (!hasNext) {
				break;
			}
			try {
				// 分页调用接口获取数据
				webResource.searchAgentCoupon(start_time, end_time, page_no, page_size, has_next, coupons, total_results);
				hasNext = has_next.value;
				
				// 开始调用显示查询条件
				if(page_no == 1){
					//查询条件写入日志
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("IOT00002");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
					batchLoggerDTO1.addParam(PropertiesUtil.getMessage("OTC00001",null));
					batchLoggerDTO1.addParam(start_time);
					batchLoggerDTO1.addParam(end_time);
					batchLoggerDTO1.addParam(ConvertUtil.getString(total_results.value));
					logger.BatchLogger(batchLoggerDTO1);
		        }
				// 取得优惠券信息JSON
				String couponsJson = coupons.value;
				// 将JSON数据转化为LIST
				List<Map<String, Object>> couponList = CherryUtil.json2ArryList(couponsJson);
				if(null == couponList || couponList.size() == 0) {
					break;
				}
				// 预处理可能失败的件数
		        int prepFailCount = couponList.size();
				totalCount += prepFailCount;
				
				// 将取得的数据写数据库中
				try {
					for (Map<String, Object> couponMap : couponList) {
						couponMap.putAll(wsParam);
						// 存在相应优惠券则更新，不存在则插入
						binOTCOU02_Service.mergeCouponInfo(couponMap);
					}
					
				} catch(Exception e) {
					failCount += prepFailCount;
					// 有一优惠券写入失败就退出循环不再处理，并将更新的数据进行回滚
					binOTCOU02_Service.manualRollback();
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
	                batchLoggerDTO.setCode("EOT00046");
	                batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
	                batchLoggerDTO.addParam(PropertiesUtil.getMessage("OTC00001",null));
	                batchLoggerDTO.addParam(start_time);
	                batchLoggerDTO.addParam(end_time);
	                batchLoggerDTO.addParam(ConvertUtil.getString(page_no));
	                batchLoggerDTO.addParam(ConvertUtil.getString(page_size));
	                CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
	                cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
	                flag = CherryBatchConstants.BATCH_WARNING;
	                break;
				}
			} catch(Exception ex) {
				// 调用接口返回不成功
	            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
	            batchExceptionDTO.setBatchName(this.getClass());
	            batchExceptionDTO.setErrorCode("EOT00053");
	            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
	            batchExceptionDTO.addErrorParam(PropertiesUtil.getMessage("OTC00001",null));
	            batchExceptionDTO.addErrorParam(TradeCode);
	            batchExceptionDTO.addErrorParam(coupons.value);
	            throw new CherryBatchException(batchExceptionDTO);
			}
			
			// 查询下一页
			page_no ++;
		}
		resultMap.put("BatchResult", flag);
        resultMap.put(esInterfaceInfoID+"_GetDataEndTime", end_time);
	}
	
	/**
	 * 获取电商接口信息表信息
	 * @param paramMap
	 * @return
	 * @throws CherryBatchException 
	 */
	private Map<String, Object> getESInterfaceInfo(Map<String, Object> paramMap) throws CherryBatchException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
        // 查询电商接口信息表
        Map<String, Object> paramESInterfaceInfo = new HashMap<String, Object>();
        paramESInterfaceInfo.put("BIN_OrganizationInfoID", paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
        paramESInterfaceInfo.put("BIN_BrandInfoID", paramMap.get(CherryBatchConstants.BRANDINFOID));
        // 电商接口提供方代号
        paramESInterfaceInfo.put("ESCode", ESCode);
        List<Map<String, Object>> esInterfaceInfoList = binOTCOU02_Service.getESInterfaceInfo(paramESInterfaceInfo);
        // 判断TradeCode是否存在GetCouponCode:优惠券查询 ，不存在抛错
        if (null == esInterfaceInfoList || esInterfaceInfoList.size() == 0) {
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            // {薇诺娜优惠券}获取，需要在电商接口信息表配置接口信息
            batchExceptionDTO.setErrorCode("EOT00049");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            batchExceptionDTO.addErrorParam(PropertiesUtil.getMessage("OTC00001",null));
            throw new CherryBatchException(batchExceptionDTO);
        }
        // 目前应该是只有一条信息
        for (int i = 0; i < esInterfaceInfoList.size(); i++) {
            Map<String, Object> temp = esInterfaceInfoList.get(i);
            String tradeCode = ConvertUtil.getString(temp.get("TradeCode"));
            if (tradeCode.equals(TradeCode)) {
            	// 支持访问多个店铺
            	List<Map<String,Object>> getCouponList = (List<Map<String, Object>>) resultMap.get(TradeCode);
                if(null == getCouponList){
                	getCouponList = new ArrayList<Map<String,Object>>();
                }
                getCouponList.add(temp);
                resultMap.put(TradeCode, getCouponList);
            }
        }

//        checkESInterfaceInfo(resultMap, TradeCode, "AccountName");
//        checkESInterfaceInfo(resultMap, TradeCode, "AccountPWD");
//        checkESInterfaceInfo(resultMap, TradeCode, "URL");
//        checkESInterfaceInfo(resultMap, TradeCode, "MethodName");
//        checkESInterfaceInfo(resultMap, TradeCode, "LastAccessTime");
//        checkESInterfaceInfo(resultMap, TradeCode, "GetDataEndTime");
        // 时间跨度字段必需
        checkESInterfaceInfo(resultMap, TradeCode, "TimeStep");


        return resultMap;
	}

	/**
	 * 校验接口信息是否存在
	 * @param resultMap
	 * @param string
	 * @param string2
	 * @throws CherryBatchException 
	 */
	private void checkESInterfaceInfo(Map<String, Object> dataMap,
			String tradeCode, String fieldName) throws CherryBatchException {
		//GetOrderMain以List形式存在，遍历判断
        List<Map<String,Object>> GetCouponCode = (List<Map<String, Object>>) dataMap.get(TradeCode);
        if(null == GetCouponCode || GetCouponCode.size() == 0){
        	BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00043");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            batchExceptionDTO.addErrorParam(PropertiesUtil.getMessage("OTC00001",null));
            batchExceptionDTO.addErrorParam(TradeCode);
            throw new CherryBatchException(batchExceptionDTO);
        }
        for(int i=0;i<GetCouponCode.size();i++){
            String value = ConvertUtil.getString(GetCouponCode.get(i).get(fieldName));
            if(value.equals("")){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00050");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                batchExceptionDTO.addErrorParam(tradeCode);
                batchExceptionDTO.addErrorParam(fieldName);
                throw new CherryBatchException(batchExceptionDTO);
            }
        }
	}

}
