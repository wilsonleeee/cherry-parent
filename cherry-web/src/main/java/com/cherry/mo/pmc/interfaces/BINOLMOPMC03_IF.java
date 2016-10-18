package com.cherry.mo.pmc.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOPMC03_IF extends ICherryInterface {

	/**
	 * 取得菜单组配置的柜台
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCounterConfTree(Map<String, Object> map) throws Exception;

	/**
	 * 保存
	 * @param map
	 * @param list
	 * @throws Exception
	 */
	public void tran_saveCounterConfig(Map<String, Object> map, List<Map<String, Object>> list) throws Exception;

	/**
	 * 解析文件
	 * @param file
	 * @param map
	 * @return
	 * @throws CherryException
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseFile(File file, Map<String, Object> map) throws CherryException, Exception;
}
