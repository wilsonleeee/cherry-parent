package com.cherry.st.jcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTJCS06_Service extends BaseService{
	/**
	 * 取得逻辑仓库List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getLogInvByBrand(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.getLogInvByBrand");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得逻辑仓库
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getLogInvByLogInvId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.getLogInvByLogInvId");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新逻辑仓库
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int updateLogInv(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.updateLogInv");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 增加逻辑仓库
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int insertLogInv(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.insertLogInv");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 停用或启用逻辑仓库
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void disOrEnableLogInv(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.disOrEnableLogInv");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 取消原有默认仓库
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void cancleDefaultFlag(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.cancleDefaultFlag");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 取得逻辑仓库List(WS结构组装使用)
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getLogInvByBrandWithWS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS06.getLogInvByBrandWithWS");
		return baseServiceImpl.getList(map);
	}
	
}
