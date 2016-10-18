package com.cherry.mo.cio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOCIO06_Service extends BaseService {

	/**
	 * 页面初始化时获取柜台和区域信息
	 * @param map 存放的是查询条件，条件保存“品牌”和“组织”即可
	 * @return list 返回查询结果
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getAllCounterAndRegion(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO06.getAllCounterAndRegion");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String,Object>> getCounterOrganiztionId(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO06.getCounterOrganiztionId");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据问卷ID删除问卷禁止表中已有的该问卷信息
	 * @param map
	 * 
	 * 
	 * */
	public void deletePaper(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLMOCIO06.deletePaper");
	}
	
	/**
	 * 获取发布者姓名
	 * 
	 * 
	 * */
	public Map<String,Object> getPublisher(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLMOCIO06.getPublisher");
	}
	
	/**
	 * 将发布信息插入到问卷禁止表中
	 * @param list
	 * 
	 * */
	public void insertPaperForbidden(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMOCIO06.insertPaperForbidden");
	}
	
	/**
	 * 到问卷主表中更新发布后的问卷信息
	 * @param map
	 * 
	 * 
	 * */
	public void updatePaper(Map<String,Object> map){
		baseServiceImpl.update(map, "BINOLMOCIO06.updatePaper");
	}
	
	/**
	 * 根据柜台对应的部门ID获取柜台Code
	 * 
	 * 
	 * */
	public String getCounterCode(String organizationId){
		return (String)baseServiceImpl.get(organizationId, "BINOLMOCIO06.getCounterCode");
	}
	
	
	/**
	 * 柜台存在验证【验证的范围为"页面上的柜台树的柜台节点"】
	 * 
	 * @param map 查询条件
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO06.getCounterInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得与导入柜台下发类型相对立的柜台的组织ID
	 * @param map
	 * @return
	 */
	public List<String> getContraryOrgID(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO06.getContraryOrgID");
		return baseServiceImpl.getList(paramMap);
	}
}
