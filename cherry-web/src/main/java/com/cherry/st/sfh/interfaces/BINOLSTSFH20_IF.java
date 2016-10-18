package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH20_IF extends ICherryInterface {
	/**
	 * 获取销售单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getBackstageSaleExcelCount(Map<String, Object> map);
	
	/**
	 * 获取销售单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBackstageSaleExcelList(Map<String, Object> map);
	
	/**
	 * 获取后台销售单产品明细（Excel导入）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBackstageSaleExcelDetailList(Map<String, Object> map);
	
	/**
	 * 通过ID获取单条后台销售单（Excel导入）主信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBackstageSaleExcelInfo(Map<String, Object> map);

	/**
	 * 获得导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
