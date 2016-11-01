package com.cherry.wp.wr.srp.interfaces;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

import java.util.List;
import java.util.Map;

/**
 * 支付构成报表IF
 *
 * @author fengxuebo
 * @version 1.0 2016/10/31
 */
public interface BINOLWRSRP09_IF extends BINOLCM37_IF {
	
	public Map<String, Object> getPayTypeCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getPayTypeSaleList(Map<String, Object> map);

}
