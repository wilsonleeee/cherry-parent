package com.cherry.bs.wem.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLBSWEM06_IF {

	public void profitRebateReset(List<Map<String, Object>> salerecordCodeList) throws Exception;

	public List<Map<String, Object>> search(Map<String, Object> map);

	public int getSalListCount(Map<String, Object> map);

	public void delRebateDivide(List<Map<String, Object>> salerecordCodeList);

}
