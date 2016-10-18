package com.cherry.mb.vis.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.vis.service.BINOLMBVIS03_Service;

/**
 * 会员回访类型管理BL
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS03_BL {
	
	@Resource
	private BINOLMBVIS03_Service binOLMBVIS03_Service;
	
	
	/**
	 * 取得会员回访类型总数
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型总数
	 */
	public int getVisitCategoryCount(Map<String, Object> map) {
		return binOLMBVIS03_Service.getVisitCategoryCount(map);
	}
	
	/**
	 * 取得会员回访类型List
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型List
	 */
	public List<Map<String, Object>> getVisitCategoryList(Map<String, Object> map) {
		return binOLMBVIS03_Service.getVisitCategoryList(map);
	}
	
	/**
	 * 取得会员回访类型信息
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型信息
	 */
	public Map<String, Object> getVisitCategoryInfo(Map<String, Object> map) {
		return binOLMBVIS03_Service.getVisitCategoryInfo(map);
	}
	
	/**
	 * 添加会员回访类型
	 * 
	 * @param map 添加内容
	 */
	public void tran_addVisitCategory(Map<String, Object> map) {
		binOLMBVIS03_Service.addVisitCategory(map);
	}
	
	/**
	 * 更新会员回访类型
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int tran_updateVisitCategory(Map<String, Object> map) {
		return binOLMBVIS03_Service.updateVisitCategory(map);
	}
	
	/**
	 * 启用停用会员回访类型
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int tran_updVisitCategoryValid(Map<String, Object> map) {
		return binOLMBVIS03_Service.updVisitCategoryValid(map);
	}
	
	/**
	 * 通过回访类型代码取得会员回访类型ID
	 * 
	 * @param map 查询条件
	 * @return 会员回访类型ID
	 */
	public String getVisitCategoryByCode(Map<String, Object> map) {
		return binOLMBVIS03_Service.getVisitCategoryByCode(map);
	}

}
