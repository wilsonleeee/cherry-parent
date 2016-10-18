package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.ss.prm.dto.BillInfo;
import com.cherry.ss.prm.dto.CouponCombDTO;
import com.cherry.ss.prm.dto.ResultDTO;

public interface Rule_IF {
	
	public ResultDTO checkUseParams(CouponCombDTO couponComb, int flag) throws Exception;
	
	public ResultDTO checkDwqUseParams(CouponCombDTO couponComb) throws Exception;
	
	public List<Map<String, Object>> getCouponRuleList(BillInfo billInfo, List<Map<String, Object>> actList) throws Exception;
	
	public ResultDTO createCoupon(BillInfo billInfo, List<Map<String, Object>> calculatedRule) throws Exception;
	
	public List<Map<String, Object>> getSendCoupon(Map<String, Object> map);
}
