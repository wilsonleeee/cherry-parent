package com.cherry.bs.emp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLBSEMP06_IF extends ICherryInterface {

	/**
	 * 导入BA处理
	 * @param map 导入文件等信息
	 * @return 处理结果信息
	 * @throws Exception
	 */
	public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存编辑
	 * @param map
	 * @throws Exception
	 */
	public void tran_saveEdit(Map<String, Object> map) throws Exception;

	/**
	 * 根据手机号取得人员ID
	 * @param map
	 * @return
	 */
	public List<String> getBaCodeByMobile(Map<String, Object> map);
}
