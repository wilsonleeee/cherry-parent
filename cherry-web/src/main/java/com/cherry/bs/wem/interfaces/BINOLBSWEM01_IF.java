package com.cherry.bs.wem.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLBSWEM01_IF {

	public int getWechatMerchantApplyCount(Map<String, Object> map);

	public List<Map<String, Object>> getWechatMerchantApplyList(Map<String, Object> map);

	public List<Map<String, Object>> getEmpList(Map<String, Object> map);

	public int getEmpCount(Map<String, Object> map);

	public void tran_assignSuperMobile(Map<String, Object> map) throws Exception;

	public void tran_audit(Map<String, Object> map) throws Exception;

	public int getSubAmount(String employeeId);

	public Map<String, Object> getApplyInfoById(Map<String, Object> agentInfoMap);

	public List<Map<String, Object>> getSubEmployeeIdList(String employeeId);

	public int getLimitAmout(Map<String, Object> map) throws Exception;

}
