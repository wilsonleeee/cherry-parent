package com.cherry.st.yldz.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTYLDZ01_IF {

	public int getSaleListCount(Map<String, Object> map);

	public List<Map<String, Object>> getSaleList(Map<String, Object> map);

	public Map<String, Object> editInit(Map<String, Object> map);

	public int delete(Map<String, Object> map);

	public void addBankBill(Map<String, Object> map);

	public int updateBankBill(Map<String, Object> map);

	public List<Map<String, Object>> ResolveExcel(Map<String, Object> sessionMap) throws Exception;

	public Map<String, Object> tran_excelHandle(List<Map<String, Object>> importDataList,Map<String, Object> sessionMap);

	public int getImportBatchCount(Map<String, Object> map);

	public int checkBillCode(Map<String, Object> map);

}
