package com.cherry.webservice.member.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.webservice.member.resource.MemberInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberInfoService extends BaseService {
	
	public List getMemberInfoByMessageID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberInfoByMessageID");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String,Object>> getMemberInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberInfo");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String,Object>> getMemberInfoOAuth(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberInfoOAuth");
		return baseServiceImpl.getList(map);
	}
	
	public Map getMemberPoint(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberPoint");
		return (Map)baseServiceImpl.get(map);
	}
	
	public List<Map<String,Object>> oAuthCoupon(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.oAuthCoupon");
		return baseServiceImpl.getList(map);
	}
	
	public int getPointChangeCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPointChangeCount");
		return baseServiceImpl.getSum(map);
	}
	
	public List<Map<String,Object>> getPointChange(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPointChange");
		return baseServiceImpl.getList(map);
	}
	
	public Map getMobilePhone(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMobilePhone");
		return (Map)baseServiceImpl.get(map);
	}
	
	public int updateMessageId(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.updateMessageId");
		return baseServiceImpl.update(map);
	}
	
	public int bindWechat(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.bindWechat");
		return baseServiceImpl.update(map);
	}
	
	public Map getMemberCoupon(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberCoupon");
		return (Map)baseServiceImpl.get(map);
	}

	public List getCouponProduct(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getCouponProduct");
		return baseServiceImpl.getList(map);
	}
	
	public Map selCounterInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selCounterInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	public Map selEmployeeInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selEmployeeInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	public Map selReferrerID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selReferrerID");
		return (Map)baseServiceImpl.get(map);
	}
	
	public List selProvinceCityID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selProvinceCityID");
		return baseServiceImpl.getList(map);
	}
	
	public List selProvinceID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selProvinceID");
		return baseServiceImpl.getList(map);
	}
	
	public Map selMemberLevel(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemberLevel");
		return (Map)baseServiceImpl.get(map);
	}
	
	public List selMemSaleRecord(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemSaleRecord");
		return baseServiceImpl.getList(map);
	}
	
	public List getCampaignOrderList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getCampaignOrderList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 根据问卷Id和会员Id获取答卷
	 * @param map
	 * @return
     */
	public List getPaperAnswerByPaperId(Map<String,Object> map){
		Map paramMap = new HashMap();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPaperAnswerByPaperId");
		return baseServiceImpl.getList(paramMap);
	}

	public int unbindMessageId(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.unbindMessageId");
		return baseServiceImpl.update(map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNewestCouponCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getNewestCouponCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	public int extendCouponExpiredTime(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.extendCouponExpiredTime");
		return baseServiceImpl.update(map);
	}
	
	public void addMemBindRecord(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.addMemBindRecord");
		baseServiceImpl.save(map);
	}

	/**
	 * 插入答卷主表
	 * @param map
     */
	public int insertPaperAnswer(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.insertPaperAnswer");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 获取问卷问题
	 * @param map
	 * @return
     */
	public Map getPaperQuestion(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPaperQuestion");
		return (Map) baseServiceImpl.get(paramMap);
	}

	/**
	 * 插入答卷信息明细表
	 * @param
	 * @return
	 */
	public void insertPaperAnswerDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "MemberInfo.insertPaperAnswerDetail");
	}

	/**
	 * 更新答卷主表
	 * @param map
	 * @return
     */
	public void updatePaperAnswer(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.updatePaperAnswer");
		baseServiceImpl.update(paramMap);
	}

	/**
	 * 批量更新答卷明细
	 * @param
	 * @return
     */
	public void updatePaperAnswerDetail(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "MemberInfo.updatePaperAnswerDetail");
	}
	public void addMemBindRelation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.addMemBindRelation");
		baseServiceImpl.save(map);
	}
	
	public void updateMemBindRelation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.updateMemBindRelation");
		baseServiceImpl.save(map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemBindRelation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemBindRelation");
		return baseServiceImpl.getList(map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemBindRecord(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemBindRecord");
		return baseServiceImpl.getList(map);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberPassword(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberPassword");
		return baseServiceImpl.getList(map);
		
	}
	
	public void updateMemberPassword(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.updateMemberPassword");
		baseServiceImpl.update(map);
	}
	
	public List selProvinceCityByCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selProvinceCityByCode");
		return baseServiceImpl.getList(map);
	}
	
	public List selProvinceByCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selProvinceByCode");
		return baseServiceImpl.getList(map);
	}
	
	public Map<String, Object> selMemSaleInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemSaleInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	public Map getMemberInfoByCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemberInfoByCode");
		return (Map)baseServiceImpl.get(map);
	}
	
	public Map getMemInfoByMemCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemInfoByMemCode");
		return (Map)baseServiceImpl.get(map);
	}
	
	public void updateMemActive(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.updateMemActive");
		baseServiceImpl.update(map);
	}
	
	public int selMemSaleCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemSaleCount");
		return baseServiceImpl.getSum(map);
	}
	
	public List<Map<String,Object>> selMemSaleList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemSaleList");
		return baseServiceImpl.getList(map);
	}
	
	public int selMemInfoCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询会员俱乐部ID
	 * @param map
	 * @return 
	 */
	public Integer selMemClubId(String clubCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clubCode", clubCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemberClubId");
		Map<String, Object> clubMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != clubMap && null != clubMap.get("memberClubId")) {
			return Integer.parseInt(clubMap.get("memberClubId").toString());
		}
		return null;
	}
	
	public List<Map<String,Object>> selMemInfoList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.selMemInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 判断手机号是否存在
	 */
	public List<Map<String, Object>> getMemInfoListByMobilePhone (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getMemInfoListByMobilePhone");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 添加潜在会员
	 */
	public void addPotentialMemInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.addPotentialMemInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 通过手机号查询潜在会员
	 */
	public int getPotentialMemInfoCountByMobile(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPotentialMemInfoCountByMobile");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 通过微信号查询潜在会员
	 */
	public int getPotentialMemInfoCountByOpenID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "MemberInfo.getPotentialMemInfoCountByOpenID");
		return baseServiceImpl.getSum(map);
	}
	
}
