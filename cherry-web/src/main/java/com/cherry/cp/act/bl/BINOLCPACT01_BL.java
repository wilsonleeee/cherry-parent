package com.cherry.cp.act.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cp.act.service.BINOLCPACT01_Service;


public class BINOLCPACT01_BL {
	/** 会员活动Service */
	@Resource
	private BINOLCPACT01_Service binOLCPACT01_Service;
	/**
	 * 会员活动List
	 * 
	 * @param map 
	 * @return List
	 */
	public List<Map<String, Object>> getMainList(Map<String, Object> map) {
		
		// 会员主题活动List
		return binOLCPACT01_Service.getMainList(map);
	}
	/**
	 * 会员主题活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getMainCount(Map<String, Object> map) {
		// 会员主题活动数
		return binOLCPACT01_Service.getMainCount(map);
	}

	/**
	 * 会员活动List
	 * 
	 * @param map 
	 * @returnList
	 */
	public List<Map<String, Object>> getSubList(Map<String, Object> map) {
		
		// 会员活动List
		return binOLCPACT01_Service.getSubList(map);
	}
	/**
	 * 会员活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getSubCount(Map<String, Object> map) {
		// 会员活动数
		return binOLCPACT01_Service.getSubCount(map);
	}
	/**
	 * 取得主题活动名称
	 * @param map
	 * @return
	 */
	public String getCampName(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCPACT01_Service.getCampName(map);
		return this.getString(map, resultList);
	}
	/**
	 * 取得活动名称
	 * @param map
	 * @return
	 */
	public String getSubCampName(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCPACT01_Service.getSubCampName(map);
		return this.getString(map, resultList);
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String,Object> map,List<Map<String,Object>> list){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			sb.append((String)tempMap.get("code"));
			sb.append("|");
			sb.append((String)tempMap.get("name"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if(i != list.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	/**
	 * 伦理删除主题活动
	 * @param map
	 * @throws Exception 
	 */
	public void tran_stopCampaign (Map<String, Object> map) throws Exception{
		binOLCPACT01_Service.stopCampaign(map);
		binOLCPACT01_Service.stopSubCampaign(map);
		binOLCPACT01_Service.stopSubCampaignPrmRule(map);
	}
	/**
	 * 伦理删除活动
	 * @param map
	 * @throws Exception 
	 */
	public void tran_stopSubCampaign (Map<String, Object> map) throws Exception{
		binOLCPACT01_Service.stopSubCampaign(map);
		binOLCPACT01_Service.stopSubCampaignPrmRule(map);
	}
}
