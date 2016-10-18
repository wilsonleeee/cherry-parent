package com.cherry.mo.pmc.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.interfaces.BINOLMOPMC04_IF;
import com.cherry.mo.pmc.service.BINOLMOPMC04_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.bl.PosMenuSynchro;

/**
 * POS品牌菜单管理BL
 * @author menghao
 *
 */
public class BINOLMOPMC04_BL extends SsBaseBussinessLogic implements
		BINOLMOPMC04_IF {

	@Resource(name="binOLMOPMC04_Service")
    private BINOLMOPMC04_Service binOLMOPMC04_Service;
	
	@Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
	@Resource(name="posMenuSynchro")
	private PosMenuSynchro posMenuSynchro;
	
	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS品牌菜单管理List
     */
	@Override
	public List<Map<String, Object>> getPosMenuBrandInfoList(
			Map<String, Object> map) {
		
		return binOLMOPMC04_Service.getPosMenuBrandInfoList(map);
	}
	
	/**
     *  修改POS品牌菜单管理
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	public void tran_updatePosMenuBrand(Map<String, Object> map) throws Exception{
		
		binOLMOPMC04_Service.updatePosMenuBrand(map);
	}
	
	/**
     *  修改POS品牌菜单管理MenuStatus
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	public void tran_updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception{
		// 更新菜单状态
		binOLMOPMC04_Service.updatePosMenuBrandMenuStatus(map);
		/**
		 *  根据posMenuID删除新后台柜台菜单配置差分结果表中的当前菜单的所有配置信息
		 *  原因：菜单共通配置的每一次更改都将会特殊配置失效，统一以共通配置为准。
		 */
		binOLMOPMC04_Service.deletePosMenuBrandCounterInfo(map);
		
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		Map<String, Object> synchroMap = new HashMap<String, Object>();
		synchroMap.putAll(map);
		synchroMap.put("BrandCode", brand_code);
		/**
		 * 更改存储过程：在更新共通菜单显示状态后，个性化菜单的当前菜单CODE对应的配置清空【结果表清空】
		 */
		// 调用存储过程下发菜单显示状态
		posMenuSynchro.updPosMenuBrand(synchroMap);
	}
	
	/**
     *  新增POS品牌菜单管理
     * 
     * @param map
	 * @throws Exception 
     */
	@Override
	public void tran_createPosMenuBrand(Map<String, Object> map) throws Exception{
		// 将新创建的品牌菜单管理信息写入新后台相关表中
		binOLMOPMC04_Service.addPosMenuBrand(map);
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		Map<String, Object> synchroMap = new HashMap<String, Object>();
		synchroMap.putAll(map);
		synchroMap.put("BrandCode", brand_code);
		// 调用存储过程将新创建菜单管理信息写入到终端
		posMenuSynchro.addPosMenuBrand(synchroMap);
	}
	
	/**
	 * 新增菜单并加入到品牌菜单管理中
	 * 注解@Transactional会在表更新出现异常时回滚该方法中的全部事务
	 * 
	 */
	@Override
	@Transactional(value="txCherryConfigManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void tran_addPosMenu(Map<String, Object> map) throws Exception {
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		Map<String, Object> menuMap = new HashMap<String, Object>();
		// 上级菜单(画面选择的上级菜单)
		String parentMenuID = ConvertUtil.getString(map.get("parentMenuID"));
		// 有上级菜单的场合
		if(null != parentMenuID && !"".equals(parentMenuID)){
			menuMap.put("parentMenuID", parentMenuID);
		} else {
			// 无上级菜单的场合
			menuMap.put("parentMenuID", "-1");
		}
		menuMap.putAll(map);
		// 添加菜单
		int posMenuID = binOLMOPMC04_Service.addPosMenu(menuMap);
		menuMap.put("posMenuID", posMenuID);
		menuMap.put("brandMenuNameCN", map.get("brandMenuNameCN"));
		menuMap.put("brandMenuNameEN", map.get("brandMenuNameEN"));
		menuMap.put("menuOrder", ConvertUtil.getInt(binOLMOPMC04_Service.getMaxChildOrder(menuMap).get("maxOrder"))+1);
		// 默认隐藏
		menuMap.put("menuStatus", "HIDE");
		// 添加菜单管理
		binOLMOPMC04_Service.addPosMenuBrand(menuMap);
		
		// 调用存储过程将此菜单节点写到终端数据库菜单表
		posMenuSynchro.addPosMenu(menuMap);
		menuMap.put("BrandCode", brand_code);
		// 调用存储过程将此菜单节点写到终端数据库品牌菜单管理表
		posMenuSynchro.addPosMenuBrand(menuMap);
		
	}
	
	/**
	 * 取得指定POS品牌菜单详细
	 * 
	 */
	@Override
	public Map<String, Object> getPosMenuDetail(Map<String, Object> map) throws Exception {
		
		return binOLMOPMC04_Service.getPosMenuDetail(map);
	}
	
	/**
	 * 更新对品牌菜单详细信息的更改,包括菜单管理表与菜单表
	 * 
	 */
	@Override
	public void tran_updateMenuBrand(Map<String, Object> map) throws Exception {
		int result = binOLMOPMC04_Service.updatePosMenuBrand(map);
		if(0 == result){
			// 保存失败
			throw new CherryException("ECM00005");
		} else {
			int res = binOLMOPMC04_Service.updatePosMenu(map);
			if(0 == res) {
				throw new CherryException("ECM00005");
			}
		}
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		Map<String, Object> synchroMap = new HashMap<String, Object>();
		synchroMap.putAll(map);
		synchroMap.put("BrandCode", brand_code);
		// 更新成功后调用存储过程接口下发菜单相关配置
		posMenuSynchro.updPosMenuBrand(synchroMap);
	}
	
	/**
	 * 停用或者启用品牌菜单
	 */
	@Override
	public void tran_disOrEnablePosMenuBrand(Map<String, Object> map) throws Exception {
		int result = binOLMOPMC04_Service.disOrEnableMenuBrand(map);
		if(0 == result) {
			// 操作失败！
			throw new CherryException("ECM00089");
		}
	}
	
	/**
	 * 取得非终结点的菜单信息总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getNoLeafPosMenuCount(Map<String, Object> map) throws Exception {
		
		return binOLMOPMC04_Service.getNoLeafPosMenuCount(map);
	}
	
	/**
	 * 取得非终结点的菜单信息List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getNoLeafPosMenuList(Map<String, Object> map) throws Exception {
		
		return binOLMOPMC04_Service.getNoLeafPosMenuList(map);
	}
	
	@Override
	public String getSamePosMenuCodeCheck(Map<String, Object> map) {
		return binOLMOPMC04_Service.getSamePosMenuCodeCheck(map);
	}

	@Override
	public String getPosMenuInfo(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> resultList = binOLMOPMC04_Service.getPosMenuInfo(map);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("menuName"));
			sb.append("|");
			sb.append(ConvertUtil.getString(tempMap.get("posMenuID")));
			
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
