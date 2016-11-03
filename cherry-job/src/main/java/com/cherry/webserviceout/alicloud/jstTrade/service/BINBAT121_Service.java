/*	
 * @(#)BINBAT121_Service.java     1.0 @2015-9-16
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
package com.cherry.webserviceout.alicloud.jstTrade.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
*
* 聚石塔接口：订单(销售)数据导入Service
* 
* 从聚石塔获取订单数据并存入新后台电商接口表及发送销售MQ
*
* @author jijw
*
* @version  2015-9-16
*/
public class BINBAT121_Service extends BaseService {
    /**
     * 查询电商接口配置信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESInterfaceInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getESInterfaceInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查询指定的员工信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getEmployeeInfo(Map<String,Object> map){
    	 Map<String, Object> paramMap = new HashMap<String, Object>();
    	 paramMap.putAll(map);
         paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getEmployeeInfo");
    	return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找部门信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepartInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getDepartInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找会员信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMemberInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getMemberInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找电商订单信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderMain(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getESOrderMain");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入会员信息表，返回主表ID
     * @param map
     * @return
     */
    public int addMemberInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT121.addMemberInfo");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入会员持卡信息表
     * @param map
     * @return
     */
    public void addMemCardInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.addMemCardInfo");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 添加地址信息
     * 
     * @param map 添加内容
     * @return 地址ID
     */
    public int addAddressInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT121.addAddressInfo");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 添加会员地址
     * 
     * @param map 添加内容
     */
    public void addMemberAddress(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT121.addMemberAddress");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 查找产品信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProductInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.getProductInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 更新电商接口信息表
     * @param map
     * @return
     */
    public int updateESInterfaceInfoLastTime(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT121.updateESInterfaceInfoLastTime");
        return baseServiceImpl.update(paramMap);
    }
}
