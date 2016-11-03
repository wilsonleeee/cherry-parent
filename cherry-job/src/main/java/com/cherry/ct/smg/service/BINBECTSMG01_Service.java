/*	
 * @(#)BINBECTSMG01_Service.java     1.0 2013/02/18	
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
package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cp.common.CampConstants;

/**
 * 沟通信息发送Service
 * 
 * @author ZhangGS
 * @version 1.0 2013/02/18	
 */
public class BINBECTSMG01_Service extends BaseService{
	/**
     * 获取沟通设置List
     * 
     * @param map
     * @return
     * 		沟通设置List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCommunicationSetList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCommunicationSetList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取沟通调度List
     * 
     * @param map
     * @return
     * 		沟通调度List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCommSchedulesList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCommSchedulesList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取沟通List
     * 
     * @param map
     * @return
     * 		沟通设置List
     */
	@SuppressWarnings("unchecked")
    public Map<String, Object> getCommunicationList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCommunicationList");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
	
	/**
     * 查询沟通计划状态
     * 
     * @param map
     * @return integer
     * 		
     */
	public int getPlanValidFlag(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getPlanValidFlag");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
     * 获取沟通模板内容
     * 
     * @param map
     * @return
     * 		沟通模板内容
     */
    public String getTemplateContents(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getTemplateContents");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 获取搜索记录信息
     * 
     * @param map
     * @return
     * 		搜索记录信息
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getSearchInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getSearchInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询会员类型的搜索结果
     * 
     * @param map
     * @return
     * 		搜索结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSearchResultList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getSearchResultList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询会员类型的搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getSearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getSearchResultCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 获取沟通计划对应的活动编号
     * 
     * @param map
     * @return
     * 		活动编号
     */
    public String getCommPlanAsCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCommPlanAsCampaign");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 查找调度最后运行时间
     * 
     * @param map
     * @return
     * 		调度最后运行时间
     */
    public String getGtLastSchedulesTime(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getGtLastSchedulesTime");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 获取活动详细信息
     * 
     * @param map
     * @return
     * 		搜索记录信息
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCampaignInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
	 * 根据活动campCode取得主题活动信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCampInfo(String campCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.CAMP_CODE, campCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCampInfo");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据活动campId取得会员活动信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getSubCampList(String campId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campId", campId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getSubCampList");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
    
    /**
     * 获取活动各档次所需积分
     * 
     * @param map
     * @return
     * 		搜索记录信息
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCampaignPoints(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCampaignPoints");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 获取柜台名称
     * 
     * @param map
     * @return
     * 		柜台名称
     */
    public String getCounterName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCounterName");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
	 * 
	 * 更新调度信息
	 * 
	 * @param map 更新条件和内容
	 * 
	 */
	public void setSchedulesInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.setSchedulesInfo");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新沟通计划运行状态
	 * 
	 * @param map 更新条件和内容
	 * 
	 */
	public void updatePlanStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.updatePlanStatus");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 记录调度日志
	 * 
	 * @param map
	 * @return
	 */
	public void addSchedulesLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG01.addSchedulesLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 记录沟通执行日志
	 * 
	 * @param map
	 * @return
	 */
	public void addCommRunLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG01.addCommRunLog");
		baseServiceImpl.save(map);
	}
	
    /**
     * 通过活动编号和活动参与时间查询参与活动的会员列表
     * 
     * @param map
     * @return
     * 		会员结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberListByCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getMemberListByCampaign");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过活动编号和活动参与时间查询参与活动的会员总数
     * 
     * @param map
     * @return
     * 		会员结果总数
     */
    public int getMemberCountByCampaign(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getMemberCountByCampaign");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 查询会员参与活动的单据信息
     * 
     * @param map
     * @return
     * 		单据信息
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getOrderInfoByMember(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getOrderInfoByMember");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 通过活动编号和活动参与时间查询参与活动的会员列表
     * 
     * @param map
     * @return
     * 		会员List
     */
    @SuppressWarnings("unchecked")
	public List<String> getMemberByCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getMemberByCampaign");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过活动编号和会员礼品领取时间查询会员列表
     * 
     * @param map
     * @return
     * 		会员List
     */
    @SuppressWarnings("unchecked")
	public List<String> getMemberByGiftGettime(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getMemberByGiftGettime");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过活动编号和活动参与时间查询参与活动的会员列表
     * 
     * @param map
     * @return
     * 		会员List
     */
    @SuppressWarnings("unchecked")
	public List<String> getCustomerByCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCustomerByCampaign");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查询当前日期积分发生了变化的会员
     * 
     * @param map
     * @return
     * 		会员List
     */
    @SuppressWarnings("unchecked")
	public List<String> getMemberByPointChange(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getMemberByPointChange");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 获取沟通最近一次成功执行的时间
     * 
     * @param map
     * @return
     * 		沟通最近成功执行的时间
     */
    public String getGtLastRunTime(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getGtLastRunTime");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getCampaignOrderCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCampaignOrderCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据搜索记录编号查询非会员类型的搜索结果
     * 
     * @param map
     * @return
     * 		搜索结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNoMemberSearchResultList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getNoMemberSearchResultList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getNoMemberSearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getNoMemberSearchResultCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据搜索记录编号查询不限客户类型的搜索结果
     * 
     * @param map
     * @return
     * 		搜索结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAnySearchResultList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getAnySearchResultList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getAnySearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getAnySearchResultCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 获取沟通变量List
     * 
     * @param map
     * @return
     * 		沟通变量List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTemplateVariableSet(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getTemplateVariableSet");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 查询同一沟通设置同一批次内已生成的Coupon号
     * 
     * @param map
     * @return
     * 		Coupon号
     */
	public String getCommonSetCoupon(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG01.getCommonSetCoupon");
        return (String)baseServiceImpl.get(paramMap);
    }
}
