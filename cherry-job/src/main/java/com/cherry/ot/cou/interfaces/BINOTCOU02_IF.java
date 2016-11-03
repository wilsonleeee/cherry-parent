package com.cherry.ot.cou.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOTCOU02_IF extends ICherryInterface {

	/**
	 * 获取薇诺娜优惠劵
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	public int tran_getCouponCode(Map<String, Object> map) throws CherryBatchException, Exception;
}
