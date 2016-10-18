package com.cherry.mo.pmc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOPMC04_Service extends BaseService {

	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS品牌菜单管理List
     */
    public List<Map<String, Object>> getPosMenuBrandInfoList(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getPosMenuBrandInfoList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /** 修改POS品牌菜单管理
    * 
    * @param map
    */
   public int updatePosMenuBrand(Map<String, Object> map) throws Exception {
   	 	Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.updatePosMenuBrand");
        return baseServiceImpl.update(paramMap);
   }
   
   /**
    * 修改POS菜单信息
    * @param map
    * @return
    * @throws Exception
    */
   	public int updatePosMenu(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.updatePosMenu");
		return baseServiceImpl.update(paramMap);
    }
	 
   /** 修改POS品牌菜单管理MenuStatus
    * 
    * @param map
    */
   public void updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception {
   	 	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.updatePosMenuBrandMenuStatus");
        baseServiceImpl.update(parameterMap);
   }
   
   /**
    * 删除柜台特殊配置差分结果表中当前菜单的所有配置，统一回归以共通配置为准
    * @param map
    * @throws Exception
    */
   public void deletePosMenuBrandCounterInfo(Map<String, Object> map) throws Exception {
	   Map<String, Object> parameterMap = new HashMap<String, Object>();
       parameterMap.putAll(map);
       parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.deletePosMenuBrandCounterInfo");
       baseServiceImpl.remove(parameterMap);
   }
   
   /** 创建POS品牌菜单管理
    * 
    * @param map
    * @return 
    */
   public int addPosMenuBrand(Map<String, Object> map) throws Exception {
   	    Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.addPosMenuBrand");
        return  baseServiceImpl.saveBackId(parameterMap);
   }
   
   /**
    * 新增一条菜单
    * @param map
    * @return
    * @throws Exception
    */
   public int addPosMenu(Map<String, Object> map) throws Exception {
	   Map<String, Object> paramMap = new HashMap<String, Object>();
	   paramMap.putAll(map);
	   paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.addPosMenu");
       return baseServiceImpl.saveBackId(paramMap);
   }
   
   /**
    * 取得指定上级菜单的下级菜单最大【MenuOrder】
    * @param map
    * @return
    * @throws Exception
    */
   public Map<String, Object> getMaxChildOrder(Map<String, Object> map) throws Exception {
	   Map<String, Object> paramMap = new HashMap<String, Object>();
	   paramMap.putAll(map);
	   paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getMaxChildOrder");
	   return (Map<String, Object>)baseServiceImpl.get(paramMap);
   }
   
   /**
	 * 取得配置项数据List(WS结构组装使用)
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuBrandWithWS(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getPosMenuBrandWithWS");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得指定菜单详细
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPosMenuDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getPosMenuDetail");
        return  (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 启用或者停用品牌菜单
	 * @param map
	 * @return
	 */
	public int disOrEnableMenuBrand(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.disOrEnableMenuBrand");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得非终结点的菜单信息总数
	 * @param map
	 * @return
	 */
	public int getNoLeafPosMenuCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getNoLeafPosMenuCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得非终结点的菜单信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getNoLeafPosMenuList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getNoLeafPosMenuList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 验证同一组织品牌中是否存在同样的菜单编码
	 * @param map 检索条件
	 * @return 返回菜单ID
	 */
	public String getSamePosMenuCodeCheck(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getSamePosMenuCodeCheck");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得菜单信息（用于快捷定位 ）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC04.getPosMenuInfo");
        return baseServiceImpl.getList(paramMap);
	}
}
