package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO17_IF extends ICherryInterface {
	/**
	 * 取得柜台消息导入的柜台消息数量
	 * @param map
	 * @return
	 */
	public int getCntMsgImportCount(Map<String, Object> map);

	/**
	 * 取得柜台消息导入的柜台消息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCntMsgImportList(
			Map<String, Object> map);

	/**
	 * 一条柜台消息的明细（有下发时显示下发到的柜台）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCntMsgImportDetailList(
			Map<String, Object> map);

	/**
	 * 取柜台消息主体信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCntMsgImportInfo(Map<String, Object> map);

	/**
	 * 导出的柜台消息导入明细信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
