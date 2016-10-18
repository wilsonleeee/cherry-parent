package com.cherry.mb.tif.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.service.BaseService;

public class BINOLMBTIF01_Service extends BaseService{
	
	/**
	 * 取得配置数据库品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			配置数据库品牌List
	 */
	public List<Map<String, Object>> getConfBrandInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getConfBrandList");
		return baseConfServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			品牌信息List
	 */
	public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getBrandInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员绑定信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员绑定信息List
	 */
	public List<Map<String, Object>> getTmallBindList(Map<String, Object> map) {
		String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
			map.put("tmallCounterArr", tmallCounters.split(","));
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getTmallBindList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得相同昵称的会员信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			相同昵称的会员信息List
	 */
	public List<Map<String, Object>> getSameNickMemList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getSameNickMemList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 新会员绑定处理
	 * 
	 * @param map
	 * @return int
	 */
	public int updateRegbindInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateRegbindInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 会员绑定处理
	 * 
	 * @param map
	 * @return int
	 */
	public int updateBindInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateBindInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 会员解绑处理
	 * 
	 * @param map
	 * @return int
	 */
	public int updateUnbindInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateUnbindInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 
	 * 插入新会员注册表
	 * 
	 * @param map
	 */
	public int addMemRegisterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addMemRegisterInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得会员信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			会员信息
	 */
	public Map<String, Object> getMemInfo(Map<String, Object> map) {
		String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
			map.put("tmallCounterArr", tmallCounters.split(","));
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getMemInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			会员信息
	 */
	public Map<String, Object> getRegisterInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getRegisterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 插入会员注册历史表
	 * 
	 * @param map
	 */
	public void addRegHisInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addRegHisInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * 插入天猫积分兑换错误信息表 
	 * 
	 * @param map
	 */
	public void addTmallPointErrInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addTmallPointErrInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * 插入天猫积分兑换履历表
	 * 
	 * @param map
	 */
	public void addTmallPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addTmallPointInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得积分兑换记录数 
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			积分兑换记录数 
	 * 
	 */
	public int getTmallPointCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBTIF01.getTmallPointCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 插入会员信息表
	 * @param map
	 * @return
	 */
	public int addMemberInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addTmallMemInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 
	 * 插入会员持卡信息表
	 * 
	 * @param map
	 */
	public void addMemCardInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.addTmallMemCardInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 临时会员绑定处理
	 * 
	 * @param map
	 * @return int
	 */
	public int updateTempMemRegInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTempMemRegInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 
	 * 更新会员天猫积分信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTMPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTmallzPointInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除假登陆会员信息
	 * @param map
	 */
	public int delPreMemberInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.delPreMemberInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除假登陆会员卡信息
	 * @param map
	 */
	public int delPreMemCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.delPreMemCode");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除假登陆会员积分信息 
	 * @param map
	 */
	public int delPreMemPoint(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.delPreMemPoint");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * 变更会员使用化妆次数积分明细记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmMemUsedMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTmMemUsedMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更规则执行履历记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmRuleRecordMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTmRuleRecordMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更积分变化记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmPointChangeMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTmPointChangeMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更销售表的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmSaleRecordMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateTmSaleRecordMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更订单表的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateESOrderMainMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.updateESOrderMainMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询新卡对应的会员产生的最早业务时间
	 * @param map
	 * @return
	 */
	public String getMinTicketDate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.getTMMinTicketDate");
		return  (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入重算信息表
	 * @param map
	 */
	public void insertReCalcInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBTIF01.insertTMReCalcInfo");
		baseServiceImpl.save(map);
	}
}
