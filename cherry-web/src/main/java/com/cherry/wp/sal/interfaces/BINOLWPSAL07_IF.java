package com.cherry.wp.sal.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWPSAL07_IF {

	public int getFinishedBillsCount(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getFinishedBillList (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getFinishedBillMap (Map<String, Object> map) throws Exception;
	
	public int getReturnHistoryBillCount(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getReturnHistoryBillList (Map<String, Object> map) throws Exception;
	
	public String tran_returnsBill(Map<String, Object> map) throws Exception;
	
	public String tran_returnsGoods(Map<String, Object> map) throws Exception;
	
	public boolean tran_getAllBillsToWebPos(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> tran_getBillDetailByCode(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> tran_getSrBillDetailByCode(Map<String, Object> map) throws Exception;

	public List<Map<String, Object>> tran_getBillDetailSavings(Map<String, Object> map) throws Exception;

	public List<Map<String, Object>> getPaymentTypeList(Map<String, Object> map);
	/** 获取会员当前总积分和对应销售所得积分，用于计算退货时积分是否足够 **/
	public Map<String,Object> getSaleMemPointInfo(Map<String, Object> map);
}
