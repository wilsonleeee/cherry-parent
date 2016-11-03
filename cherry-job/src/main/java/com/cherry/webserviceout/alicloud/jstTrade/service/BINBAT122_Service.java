/*	
 * @(#)BINBATJST122_Service.java     1.0 @2016-09-26
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
*
* 聚石塔接口：订单(销售)数据导入Service
* 
* 从聚石塔获取订单数据并存入新后台电商接口表及发送销售MQ
*
* @author 
*
* @version  2016-09-26
*/
public class BINBAT122_Service extends BaseService {
	
	/**
     * 查询电商接口配置信息
     * @param map
     * @return
     */
    public Map<String, Object> getESInterfaceInfo(Map<String, Object> map) {
    	map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getESIfConfigInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 查询指定的员工信息
     * @param map
     * @return
     */
    public Map<String,Object> getEmployeeInfo(Map<String,Object> map){
    	map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getEmployeeInfo");
    	return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 查找部门信息
     * @param map
     * @return
     */
    public Map<String, Object> getDepartInfo(Map<String, Object> map) {
        map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getDepartInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 查找电商订单信息
     * @param map
     * @return
     */
    public Map<String, Object> getESOrderMain(Map<String, Object> map) {
        map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getESOrderMain");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 查找会员信息
     * @param map
     * @return
     */
    public Map<String, Object> getMemberInfo(Map<String, Object> map) {
        map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getMemberInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 查找订单明细表
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderDetail(Map<String, Object> map) {
        map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.getESOrderDetail");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 更新订单明细表
     * @param map
     * @return
     */
    public void updateESOrderDetail(List<Map<String,Object>> list){
        baseServiceImpl.updateAll(list,"BINBATJST122.updateESOrderDetail");
    }
    
    /**
     * 更新电商接口信息表
     * @param map
     * @return
     */
    public int updateESConfigLastTime(Map<String, Object> map){
    	map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBATJST122.updateESConfigLastTime");
        return baseServiceImpl.update(map);
    }
    
    public String subStringByLimit(String str, int limit) {
    	if (null != str && str.length() > limit) {
    		str = str.substring(0, limit);
    	}
    	return str;
    }
    
    /**
     * 订单状态： 　　　　
     * NO_PAY：未付款(1)；CUSTOMER_AUDIT：客审(2);FINANCIAL_AUDIT：财审 (2) 　　　　
     * PRINT：打印(2) ;DISTRIBUTION：配货 (2) ;WAREHOUSING：出库 (3);
     * ON_THE_WAY：途中 (3)；SETTLEMENT：结算(3) ；SUCCESS：成功 (4)；
     * CANCEL：取消 (0)；OUT_OF_STOCK：缺货(2) ；UNDEFIND：未定义状态("")；
     * 
     * 1:生成；2：付款；3：发货；4：完成；0：取消 -1:付款后退款成功: -1
     * 
     * 取出最终写入数据库的单据状态
     * @param orderStatus
     * @return
     */
    //以下订单状态转换待定
    public String convertOrderStatus(String orderStatus) {
//    		  * TRADE_NO_CREATE_PAY(没有创建支付宝交易)  (1)
//    		  * WAIT_BUYER_PAY(等待买家付款)  (1)
//    		  * SELLER_CONSIGNED_PART(卖家部分发货)  ???
//    		  * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) (2)
//    		  * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) (3)
//    		  * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) (4)
//    		  * TRADE_FINISHED(交易成功)   (4)
//    		  * TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)   ??? 0
//    		  * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)  0
//    		  * PAY_PENDING(国际信用卡支付付款确认中)  ???
//    		  * WAIT_PRE_AUTH_CONFIRM(0元购合约中) ???
    	
    	String billState = "";
    	if (orderStatus.equals("TRADE_NO_CREATE_PAY") 
    			|| orderStatus.equals("WAIT_BUYER_PAY") ) {
    		billState = "1";// 生成：1
    	} else if (orderStatus.equals("WAIT_SELLER_SEND_GOODS")) {
    		billState = "2";// 付款：2
    	} else if (orderStatus.equals("WAIT_BUYER_CONFIRM_GOODS")) {
    		billState = "3";// 发货：3
    	} else if (orderStatus.equals("TRADE_BUYER_SIGNED")
    			|| orderStatus.equals("TRADE_FINISHED") ) {
    		billState = "4";// 完成：4
    	} else if (orderStatus.equals("TRADE_CLOSED_BY_TAOBAO") || orderStatus.equals("TRADE_CLOSED")) {
    		billState = "0";// 订单取消：0
    	}
    	return billState;
    }
}
