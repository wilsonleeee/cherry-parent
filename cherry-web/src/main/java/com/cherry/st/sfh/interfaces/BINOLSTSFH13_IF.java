package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH13_IF extends ICherryInterface {

	/**
	 * 获取发货单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getPrtDeliverExcelCount(Map<String, Object> map);
	
	/**
	 * 获取发货单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtDeliverExcelList(Map<String, Object> map);
	
	/**
	 * 通过ID获取单条发货单（Excel导入）主信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtDeliverExcelDetailList(Map<String, Object> map);
	
	/**
	 * 获取发货单产品明细（Excel导入）
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtDeliverExcelInfo(Map<String, Object> map);
	
	/**
	 * 获得导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	
	
	
}
