package com.cherry.mo.pmc.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * POS品牌菜单管理IF
 * @author menghao
 *
 */
public interface BINOLMOPMC04_IF extends ICherryInterface {
	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS配置项List
     */
    public List<Map<String, Object>> getPosMenuBrandInfoList(Map<String, Object> map);
    
    /**
     * 修改POS品牌菜单管理
     * 
     * @param map
     */
    public void  tran_updatePosMenuBrand(Map<String, Object> map) throws Exception;
    
    /**
     * 修改POS品牌菜单管理MenuStatus
     * 
     * @param map
     */
    public void  tran_updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception;
    
    /**
     * 新增POS品牌菜单管理
     * 
     * @param map
     */
    public void tran_createPosMenuBrand(Map<String, Object> map) throws Exception;
    
    /**
     * 新增一条菜单信息及其品牌菜单管理信息
     * @param map
     * @return
     * @throws Exception
     */
    public void tran_addPosMenu(Map<String, Object> map) throws Exception;
    
    /**
     * 取得指定POS菜单详细
     * @param map
     * @throws Exception
     */
    public Map<String, Object> getPosMenuDetail(Map<String, Object> map) throws Exception;

    /**
     * 更新对品牌菜单信息的修改
     * @param map
     * @throws Exception
     */
	public void tran_updateMenuBrand(Map<String, Object> map) throws Exception;

	/**
	 * 停用或者启用品牌菜单
	 * @param map
	 * @throws Exception
	 */
	public void tran_disOrEnablePosMenuBrand(Map<String, Object> map) throws Exception;

	/**
	 * 取得非终结点的菜单信息总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getNoLeafPosMenuCount(Map<String, Object> map) throws Exception;

	/**
	 * 取得非终结点的菜单信息List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNoLeafPosMenuList(Map<String, Object> map)
			throws Exception;

	/**
	 * 验证同一组织品牌中是否存在同样的菜单编码
	 * @param map
	 * @return
	 */
	public String getSamePosMenuCodeCheck(Map<String, Object> map);
	
	/**
	 * 取得菜单信息（用于快捷定位 ）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getPosMenuInfo(Map<String, Object> map) throws Exception;
	
}
