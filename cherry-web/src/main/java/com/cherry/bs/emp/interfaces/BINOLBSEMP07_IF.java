package com.cherry.bs.emp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 生成代理商优惠券IF
 * @author menghao
 * @version 1.0 2014-08-27
 */
public interface BINOLBSEMP07_IF extends ICherryInterface {

	/**
	 * 取得BA模式下的一览明细数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getBaModelCouponCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得BA模式下的一览LIST
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBaModelCouponList(Map<String, Object> map) throws Exception;

	/**
	 * 取得生成优惠券的批次数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getBatchCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得生成优惠券的批次LIST
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBatchList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询是否存在指定批次号的生成优惠券批次
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBatchCodeByCode(Map<String, Object> map) throws Exception;
	
	/**
	 * 当前批次生成后还可生成的优惠券数量【即此批次后剩余优惠券资源】
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getMaxBatchCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 为指定代理商生成固定数量的优惠券
	 * @param map
	 * @throws Exception
	 */
	public void tran_createBaCoupon(Map<String, Object> map) throws Exception;
	
	/**
	 * 删除未下发的批次代理商优惠券
	 * @param map
	 * @throws Exception
	 */
	public void tran_deleteBatchCoupon(Map<String, Object> map) throws Exception;
}
