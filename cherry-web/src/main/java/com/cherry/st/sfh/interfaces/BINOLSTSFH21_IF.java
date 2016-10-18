package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH21_IF  extends BINOLCM37_IF {
	/**
	 * 查询代商务通后台销售报表记录数
	 * @param map
	 * @return
	 */
	public int getBackSaleReportCount(Map<String, Object> map);
	
	/**
	 * 查询代商务通后台销售报表List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBackSaleReportList(Map<String, Object> map);
	
	/**
	 * 获取导出的map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	/**
	 * 导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCsv(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取业务日期
	 * @param map
	 * @return
	 */
	public String getBussnissDate(Map<String, Object> map);
}
