package com.cherry.mo.cio.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO06_IF extends ICherryInterface {

	public List<Map<String,Object>> getAllCounterAndRegion(Map<String,Object> map);
	
	public String getControlFlag(Map<String,Object> map) throws Exception;
	
	public void tran_issuedPaper(Map<String,Object> map,List<Map<String,Object>> checkedList,List<String> unCheckedList) throws Exception;

	/**
	 * 解析文件
	 * @param file
	 * @param map
	 * @return
	 * @throws CherryException
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseFile(File file, Map<String, Object> map) throws CherryException, Exception;

	/**
	 * 取得与导入柜台下发类型相对立的柜台的组织ID
	 * @param map
	 * @return
	 */
	public List<String> getContraryOrgID(Map<String, Object> map) throws Exception;
}
