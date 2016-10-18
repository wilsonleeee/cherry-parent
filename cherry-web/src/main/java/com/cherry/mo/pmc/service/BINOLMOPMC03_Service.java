package com.cherry.mo.pmc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOPMC03_Service extends BaseService {

	/**
	 * 按组织结构查询柜台菜单配置信息List（用于显示柜台树）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterConfInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getCounterConfInfo");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得已经配置过其他菜单组的柜台
	 * @param map
	 * @return
	 */
	public List<String> getHaveOtherMenuGrpCnt(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getHaveOtherMenuGrpCnt");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得已经配置了当前菜单组的柜台
	 * 
	 * @param map
	 * @return
	 */
	public List<String> getOldMenuGrpCnt(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getOldMenuGrpCnt");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据此次下发到的柜台ID组取得CODE组
	 * @param map
	 * @return
	 */
	public List<String> getNewCounterCodeById(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getNewCounterCodeById");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 删除分组菜单柜台对应表
	 * @param map
	 * @return
	 */
	public int delPosMenuGrpCounter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.delPosMenuGrpCounter");
   	 	return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 插入分组菜单柜台对应表
	 * @param map
	 */
	public void insertPosMenuGrpCounter(List<Map<String, Object>> list) {
   	 	baseServiceImpl.saveAll(list,"BINOLMOPMC03.insertPosMenuGrpCounter");
	}
	
	/**
	 * 取得指定柜台的菜单配置（差分）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterMenuConf(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getCounterMenuConf");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
     * 取得系统日期
     * 
     * @param
     * @return 系统日期
     */
    public String getPublishDate() {
        return baseServiceImpl.getDateYMD();
    }
	
	/**
	 * 取得指定菜单组的菜单配置
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGrpMenuConf(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getGrpMenuConf");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得新后台的柜台菜单个性化配置差分表能确定所属菜单组的信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getModifyTimeForSpecials(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getModifyTimeForSpecials");
   	 	return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 删除指定菜单组的老配置信息
	 * @param map
	 * @return
	 */
	public int delPosMenuBrandCounter (Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.delPosMenuBrandCounter");
   	 	return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 插入品牌柜台菜单特殊配置表（差分）
	 * @param list
	 */
	public void insertPosMenuBrandCounter(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list,"BINOLMOPMC03.insertPosMenuBrandCounter");
	}
	
	/**
	 * 取得菜单ID对应的菜单编号及名称【此处是临时方法，以后还须优化】
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPosMenuCodeName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getPosMenuCodeName");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得渠道柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getChannelCntList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得区域柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionCntList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getRegionCntList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 柜台存在验证
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.getCounterInfo");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
     * 更新发布时间及状态
     * 
     * @param map
     * @return 
     */
    public int modifyPublishDate(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC03.modifyPublishDate");
        return baseServiceImpl.update(parameterMap);
    }
}
