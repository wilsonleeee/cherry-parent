/*	
 * @(#)BINBEDRCOM03_Service.java     1.0 2011/09/02
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
package com.cherry.dr.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.util.DoubleUtil;

/**
 * 加载规则文件共通处理
 * 
 * @author hub
 * @version 1.0 2011.09.02
 */
public class BINBEDRCOM03_Service extends BaseService{
	
	/**
	 * 取得配置数据库品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			配置数据库品牌List
	 */
	public List<Map<String, Object>> getConfBrandInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getConfBrandInfoRuleList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌信息
	 */
	public Map<String, Object> getOSBrandInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织Code
		paramMap.put("orgCode", map.get("orgCode"));
		// 品牌code
		paramMap.put("brandCode", map.get("brandCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getOSBrandRuleInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得组织品牌代码信息 
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌信息
	 */
	public Map<String, Object> getOrgCodeInfo(int orgId, int brandId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织Id
		paramMap.put("organizationInfoId", orgId);
		// 品牌Id
		paramMap.put("brandInfoId", brandId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getOrgCodeInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得会员活动组List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员活动组List
	 */
	public List<Map<String, Object>> getCampaignGrpRuleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getCampaignGrpRuleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
     * 取得单个活动的规则内容信息
     * 
     * @param map
     * @return
     * 		规则内容
     */
    public Map<String, Object> getRuleCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getRuleCampaignInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得规则内容
     * 
     * @param map
     * @return
     * 		规则内容
     */
    public List<Map<String, Object>> getRuleContentList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getRuleContentList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
	 * 取得一段时间内的购买次数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			购买次数
	 * 
	 */
	public int getSaleCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM03.getSaleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
     * 取得一段时间内的购买信息
     * 
     * @param map
     * @return
     * 		购买信息
     */
    public List<Map<String, Object>> getBillCodeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getBillCodeList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
     * 取得某个单号对应的单次
     * 
     * @param map
     * @return int
     * 			单次
     */
    public int getTicketNum(Map<String, Object> map) {
    	// 单次
    	int TicketNum = 0;
    	// 取得一段时间内的购买信息
    	List<Map<String, Object>> billCodeList = getBillCodeList(map);
    	// 关联退货单信息
    	Map<String, Object> srMap = new HashMap<String, Object>();
    	if (null != billCodeList && !billCodeList.isEmpty()) {
    		for (int i = 0; i < billCodeList.size(); i++) {
    			Map<String, Object> billCodeInfo = billCodeList.get(i);
    			// 退货单
    			if ("SR".equals(billCodeInfo.get("saleType"))) {
    				// 关联单号
    				String billCodePre = (String) billCodeInfo.get("billCodePre");
    				if (!CherryChecker.isNullOrEmpty(billCodePre, true)) {
    					// 金额
    					double amount = Double.parseDouble(billCodeInfo.get("amount").toString());
    					// 累计退货金额
    					double totalAmount = 0;
    					if (srMap.containsKey(billCodePre)) {
    						totalAmount = Double.parseDouble(srMap.get(billCodePre).toString());
    					}
    					totalAmount = DoubleUtil.add(totalAmount, amount);
    					srMap.put(billCodePre, totalAmount);
    				}
    				billCodeList.remove(i);
    				i--;
    			}
    		}
	    	// 单号
	    	String billId = (String) map.get("billId");
	    	if (!CherryChecker.isNullOrEmpty(billId, true)) {
	    		for (int i = 0; i < billCodeList.size(); i++) {
	    			boolean addFlag = true;
	    			Map<String, Object> billCodeInfo = billCodeList.get(i);
	    			// 单号
	    	    	String billCode = (String) billCodeInfo.get("billCode");
	    			// 重新计算原单剩余金额
	    			if (srMap.containsKey(billCode)) {
	    				// 累计退货金额
	    				double srAmount = Double.parseDouble(srMap.get(billCode).toString());
	    				// 原单金额
	    				double amount = Double.parseDouble(billCodeInfo.get("amount").toString());
	    				amount = DoubleUtil.sub(amount, srAmount);
	    				if (amount <= 0) {
	    					addFlag = false;
	    				}
	    			}
	    			if (addFlag) {
	    				TicketNum++;
	    			}
	    	    	if (billId.equals(billCode)) {
	    	    		if (!addFlag) {
	    	    			TicketNum = 0;
	    	    		}
	    	    		break;
	    	    	}
	    		}
	    	}
    	}
        return TicketNum;
    }
    
    /**
     * 取得一段时间内的购买金额
     * 
     * @param map
     * @return
     * 		购买金额
     */
    public double getTtlAmount(Map<String, Object> map) {
    	double ttlAmount = 0;
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getTtlAmountInfo");
    	Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
        if (null != resultMap && null != resultMap.get("ttlAmount")) {
        	ttlAmount = Double.parseDouble(resultMap.get("ttlAmount").toString());
        	if (ttlAmount > 0) {
	        	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getTtlSRAmountInfo");
	        	resultMap = (Map<String, Object>) baseServiceImpl.get(map);
	        	if (null != resultMap && null != resultMap.get("ttlSRAmount")) {
	        		double ttlSRAmount = Double.parseDouble(resultMap.get("ttlSRAmount").toString());
	        		if (ttlSRAmount > 0) {
	        			ttlAmount = DoubleUtil.sub(ttlAmount, ttlSRAmount);
	        		}
	        	}
        	}
        }
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getESTtlAmountInfo");
    	resultMap =  (Map<String, Object>) baseServiceImpl.get(map);
    	if (null != resultMap && null != resultMap.get("ttlAmount")) {
    		double esAmount = Double.parseDouble(resultMap.get("ttlAmount").toString());
    		ttlAmount = DoubleUtil.add(ttlAmount, esAmount);
    	}
        return ttlAmount;
    }
    
    
    public int getTtlTimes(Map<String, Object> map) {
    	int times = 0;
    	// 获取购买记录信息
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getTtlTimesList");
    	List<Map<String, Object>> resultList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
    	if (null != resultList && !resultList.isEmpty()) {
    		// 获取关联退货信息
    		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getTtlSRTimesList");
    		List<Map<String, Object>> srList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
    		if (null != srList && !srList.isEmpty()) {
    			Map<String, Object> preBillMap = new HashMap<String, Object>();
    			// 累加关联退货金额
    			for (Map<String, Object> srMap : srList) {
    				String billCodePre = (String) srMap.get("billCodePre");
    				double amount = Double.parseDouble(String.valueOf(srMap.get("srAmount")));
    				if (preBillMap.containsKey(billCodePre)) {
    					double tsrAmount = Double.parseDouble(String.valueOf(preBillMap.get(billCodePre)));
    					preBillMap.put(billCodePre, DoubleUtil.add(tsrAmount, amount));
    				} else {
    					preBillMap.put(billCodePre, amount);
    				}
    			}
    			if (!preBillMap.isEmpty()) {
    				// 和原单对冲如果全退的话，原单不计算
    				for (int i = 0; i < resultList.size(); i++) {
    					Map<String, Object> billMap = resultList.get(i);
    					String billCode = (String) billMap.get("billCode");
    					if (null != preBillMap.get(billCode)) {
    						double tsrAmount = Double.parseDouble(String.valueOf(preBillMap.get(billCode)));
    						double amount = Double.parseDouble(String.valueOf(billMap.get("amount")));
    						if (tsrAmount >= amount) {
    							resultList.remove(i);
    							i--;
    						}
    					}
    				}
    			}
    		}
    		times = resultList.size();
    		// 取得一段时间内的购买次数(订单)
    		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getESTtlTimesCount");
    		int esTimes = baseServiceImpl.getSum(map);
    		times += esTimes;
    	}
    	return times;
    }
    /**
     * 取得积分清零延期产品总数量
     * 
     * @param map
     * @return
     * 		积分清零延期产品总数量
     */
    public double getYanqiQuantity(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getYanqiQuantity");
		Map<String, Object> qtMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != qtMap && null != qtMap.get("quantity")) {
			return Double.parseDouble(qtMap.get("quantity").toString());
		}
        return 0;
    }
    
    /**
     * 取得特定产品数量
     * 
     * @param map
     * @return
     * 		特定产品数量
     */
    public double getSpecPrtNum(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getSpecPrtNum");
		Map<String, Object> qtMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != qtMap && null != qtMap.get("quantity")) {
			return Double.parseDouble(qtMap.get("quantity").toString());
		}
        return 0;
    }
    
    /**
     * 取得维护积分件数(积分清零延期理由)
     * 
     * @param map
     * @return
     * 		维护积分件数
     */
    public int getYanqiPT(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getYanqiPT");
		return baseServiceImpl.getSum(map);
    }
    
}
