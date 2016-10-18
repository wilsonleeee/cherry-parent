package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO16_IF extends ICherryInterface {
	
	/**
	 * 取得导入批次的总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map);

	/**
	 * 取得导入批次LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map);
	
	/**
	 * 解析EXCEL文件
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception;
	
	/**
	 * 处理导入数据
	 * @param importDataMap : 经解析后的EXCEL导入数据
	 * @param map ： 主参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap,Map<String, Object> map) throws Exception;
	
	/**
	 * 插入导入批次表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int insertImportBatch(Map<String, Object> map) throws Exception;
}
