package com.cherry.bs.emp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLBSEMP08_IF extends ICherryInterface {

	/**
	 * 取得当前批次的代理商优惠券数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getBaCouponCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得当前批次的代理商优惠券List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBaCouponList(Map<String, Object> map) throws Exception;

	/**
	 * 删除未下发的代理商优惠券
	 * @param map
	 * @throws Exception
	 */
	public void tran_deleteBaCoupon(Map<String, Object> map) throws Exception;
}
