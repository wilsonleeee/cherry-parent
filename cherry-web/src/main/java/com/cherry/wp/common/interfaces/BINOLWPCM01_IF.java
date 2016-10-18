package com.cherry.wp.common.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWPCM01_IF {
	
	// 获取会员数量
	/**
	 * 根据会员查询条件获取符合条件的会员数量
	 * @param map（必须参数：organizationInfoId，brandInfoId。可选参数：searchStr，memberName，birthDay）
	 * @return 会员数量
	 */
	public int getMemberCount(Map<String, Object> map) throws Exception;
	
	// 获取会员列表List（分页查询情况）
	/**
	 * 根据会员查询条件获取会员列表
	 * @param map（必须参数：organizationInfoId，brandInfoId。可选参数：searchStr，memberName，birthDay）
	 * @return 会员列表
	 */
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) throws Exception;
	
	// 获取会员信息
	/**
	 * 根据会员查询条件获取符合条件的第一条会员记录详细信息
	 * @param map（必须参数：organizationInfoId，brandInfoId。可选参数：searchStr，memberName，birthDay）
	 * @return 会员详细信息
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception;
	// 获取会员预约活动领取柜台
	/**
	 * 根据会员查询条件获取  预约活动领取柜台
	 * @param map（必须参数：organizationInfoId，brandInfoId。可选参数：searchStr，memberName，birthDay）
	 * @return 预约活动领取柜台
	 */
	public List<Map<String, Object>> getOrderCounterCode(Map<String, Object> map) throws Exception;
	
	// 根据柜台获取柜台BA列表
	public List<Map<String, Object>> getCounterBaList(Map<String, Object> map)	throws Exception;

	/**
	 * 查询会员等级信息List
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map);
	
	/**
	 * 查询柜台营业员信息
	 * 
	 * @param map 查询条件
	 * @return 营业员信息List
	 */
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map);
	
	/**
	 * 查询柜台当班的营业员信息
	 * 
	 * @param map 查询条件
	 * @return 当班的营业员信息List
	 */
	public List<Map<String, Object>> getActiveBAList(Map<String, Object> map);

	
	public String getBussinessDate(Map<String, Object> map);
	
	public String getSYSDate();

}
