package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH19_IF extends ICherryInterface {
	
	/**
	 * 获取后台销售单（Excel导入）批次总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map);

	/**
	 * 获取后台销售单（Excel导入）批次信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map);
	
	/**
	 * 解析导入的Excel数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception;

	/**
	 * 后台销售导入处理
	 * @param importDataMap
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> sessionMap)
			throws Exception;

	/**
	 * 插入导入批次
	 * @param map
	 * @return
	 */
	public int insertImportBatch(Map<String, Object> map);
}
