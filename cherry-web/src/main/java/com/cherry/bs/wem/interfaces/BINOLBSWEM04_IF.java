package com.cherry.bs.wem.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLBSWEM04_IF {

	public int getWechatMerchantCount(Map<String, Object> map);

	public List<Map<String, Object>> getWechatMerchantList(Map<String, Object> map);

	public List getAgentLevelList(List codeList);

	public void takeAgentInterface(Map<String, Object> map) throws Exception;

	public int getReservedCodeCount(Map<String, Object> map);

	public List<Map<String, Object>> getReservedCodeList(Map<String, Object> map);

	public void updatePHPDepartName(Map<String, Object> map) throws Exception;

	public void tran_setReservedCodeInvalid(Map<String, Object> map);

	public List<Map<String, Object>> getAgentAccountInfoList(Map<String, Object> tempMap);

	public Map<String, Object> getMobExistsInAgentApply(Map<String, Object> map);

}
