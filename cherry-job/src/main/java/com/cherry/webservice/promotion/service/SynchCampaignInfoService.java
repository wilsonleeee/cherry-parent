package com.cherry.webservice.promotion.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

public class SynchCampaignInfoService extends BaseService {
	/**
	 * 取得所有员工数据
	 * @param map
	 * @return
	 */
	public List getEmployeeList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入活动同步明细表
	 * @param map
	 * @return
	 */
	public void insertCampaignSynchDetailInfo(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "SynchCampaignInfo.insertCampaignSynchDetailInfo");
	}
	
	/**
	 * 插入活动同步主表
	 * @param map
	 * @return
	 */
	public int insertCampaignSynchInfo(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertCampaignSynchInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 判断产品是否存在并返回产品厂商Id
	 * @param map
	 * @return
	 */
	public String getProductVendorId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.getProductVendorId");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取促销品
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPromotionProduct(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.getPromotionProduct");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 插入活动信息接口主表
	 * @param map
	 */
	public void insertActivitySubject(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertActivitySubject");
		ifServiceImpl.save(map);
	}
	/**
	 * 插入子活动表接口表
	 * @param map
	 */
	public void insertActivityAssociateTable(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertActivityAssociateTable");
		ifServiceImpl.save(map);
	}
	/**
	 * 插入促销活动接口表
	 * @param map
	 */
	public void insertActivityTable(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertActivityTable");
		ifServiceImpl.save(map);
	}
	/**
	 * 插入促销产品表
	 * @param map
	 * @return
	 */
	public int insertPromotionProductBackId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertPromotionProductBackId");
		return baseServiceImpl.saveBackId(map);
	}
	/**
	 * 插入促销产品厂商表
	 * @param map
	 * @return
	 */
	public int insertPromProductVendor(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.insertPromProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	/**
	 * 更改活动下发状态
	 * @param map
	 * @return
	 */
	public int updateIssued(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.updateIssued");
		return baseServiceImpl.update(map);
	}
    /**
     * 查找员工信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEmployeeInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.getEmployeeInfo");
        return baseServiceImpl.getList(paramMap);
    }
    /**
     *  查找主活动信息是否已存在 
     * @param map
     * @return
     */
    public int getActivityAssociateSubjectInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchCampaignInfo.getActivityAssociateSubjectInfo");
        return ifServiceImpl.getSum(paramMap);
    }
}
