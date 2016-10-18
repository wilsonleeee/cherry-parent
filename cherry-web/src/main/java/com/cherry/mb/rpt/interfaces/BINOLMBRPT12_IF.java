package com.cherry.mb.rpt.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员连带销售报表IF
 * 
 * @author hub
 * @version 1.0 2016-04-12
 */
public interface BINOLMBRPT12_IF {
	
	public String getBussinessDate(Map<String, Object> map);
	
	/**
	 * 取得产品总数
	 * @param map
	 * @return
	 */
	public int getProductInfoCount (Map<String, Object> map);
	
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoList(Map<String, Object> map);
	
	/**
	 * 查询连带产品总数
	 * @param map
	 * @return
	 */
	public int getJointPrtInfoCount (Map<String, Object> map);
	
	/**
	 * 查询连带产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointPrtList(Map<String, Object> map);
	
	/**
	 * 取得大类总数
	 * @param map
	 * @return
	 */
	public int getCateCount (Map<String, Object> map);
	
	/**
	 * 取得大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCateList(Map<String, Object> map);
	
	/**
	 * 查询连带大类总数
	 * @param map
	 * @return
	 */
	public int getJointCateCount (Map<String, Object> map);
	
	/**
	 * 查询连带大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointCateList(Map<String, Object> map);
	
	/**
	 * 查询会员总数
	 * @param map
	 * @return
	 */
	public int getMemberCount(Map<String, Object> map);
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberList(Map<String, Object> map);
	
	/**
	 * 导出Excel
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	/**
	 * 导出CSV
	 * 
	 */
	public String export(Map<String, Object> map) throws Exception;

}
