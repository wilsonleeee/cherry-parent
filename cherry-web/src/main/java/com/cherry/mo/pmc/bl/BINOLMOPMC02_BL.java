package com.cherry.mo.pmc.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.interfaces.BINOLMOPMC02_IF;
import com.cherry.mo.pmc.service.BINOLMOPMC02_Service;

public class BINOLMOPMC02_BL implements BINOLMOPMC02_IF {

	@Resource(name="binOLMOPMC02_Service")
	private BINOLMOPMC02_Service binOLMOPMC02_Service;
	
	@Override
	public Map<String, Object> getPosMenuGrpInfo(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMOPMC02_Service.getPosMenuGrpInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getMenuGrpConfigTree(
			Map<String, Object> map) throws Exception {
		// 具有层级的菜单List
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> configList = binOLMOPMC02_Service.getMenuGrpConfig(map);
		for(int i = 0;i<configList.size();i++){
			configList.get(i).put("open", true);
 			//勾选状态
 			String menuStatus = configList.get(i).get("menuStatus").toString();
 			if(menuStatus.equals("SHOW")){
 				configList.get(i).put("checked", true);
 			}
 		}
		if(configList.size() == 0) {
			return null;
		} else {
			converList2Tree(configList, "-1", menuList);
			return menuList;
		}
	}
	
	/**
	 * 将纯属的数据转换成层级的数据
	 * @param list: 线性数据List
	 * @param menuId: 每一层的父节点ID
	 * @param resultList: 层级数据List
	 */
	private void converList2Tree(List<Map<String, Object>> list,
			String menuId, List<Map<String, Object>> resultList) {
		if(list == null || list.isEmpty()) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			// 把相同父节点ID的数据作为一组数据
			if(menuId.equals(list.get(i).get("pId").toString())) {
				resultList.add(list.get(i));
				list.remove(i);
				i--;
			}
		}
		
		if(resultList != null && !resultList.isEmpty()) {
			for(int i = 0; i < resultList.size(); i++) {
				if(list == null || list.isEmpty()) {
					break;
				}
				String deepMenuId = resultList.get(i).get("id").toString();
				List<Map<String, Object>> deepResultList = new ArrayList<Map<String,Object>>();
				resultList.get(i).put("nodes", deepResultList);
				// 递归取得当前层的下层结构数据
				converList2Tree(list,deepMenuId,deepResultList);
			}			
		}
	}

	@Override
	public void tran_saveMenuGrpConfig(Map<String, Object> map,
			List<Map<String, Object>> diffentList) throws Exception {
		if(null == diffentList || diffentList.size() == 0){
			return;
		}
		// 指定菜单组在菜单组配置差分表中的差分信息
		List<Map<String, Object>> posMenuGrpConfig = binOLMOPMC02_Service.getGrpConfigList(map);
		// 变动菜单节点已经在差分表中的菜单ID
		List<String> changeList = new ArrayList<String>();
		for(Map<String, Object> configMap : posMenuGrpConfig){
			String posMenuID = ConvertUtil.getString(configMap.get("posMenuID"));
			// 将差分表中已经存在的菜单配置从此次新更改的LIST中剔除（保证新增的差分项是新的差分）
			for(int i=0; i<diffentList.size(); i++){
				Map<String, Object> difMap = diffentList.get(i);
				if(posMenuID.equals(ConvertUtil.getString(difMap.get("posMenuID")))){
					// 差分表中已经存在的项（须剔除即恢复标准配置）
					changeList.add(ConvertUtil.getString(configMap.get("posMenuID")));
					difMap.clear();
					diffentList.remove(i);
					// 跳出此次循环
					break;
				}
			}
		}
		if(changeList.size() > 0){
			map.put("changList", changeList);
			// 将菜单组对应的配置差分表中在此次更新中有更改的项清除
			binOLMOPMC02_Service.delPosMenuGrpConfig(map);
		}
		// 将新的个性菜单配置写入差分表中
		List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> difMap : diffentList) {
			difMap.putAll(map);
			insertList.add(difMap);
		}
		binOLMOPMC02_Service.insertPosMenuGrpConfig(insertList);
	}

	@Override
	public String getPosMenuInfo(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> resultList = binOLMOPMC02_Service.getPosMenuInfo(map);
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

	/**
	 * 刷新菜单分组的菜单配置（差分表的差分基础是品牌菜单管理表）
	 * 
	 */
	@Override
	public void refreshPosMenuConfig(Map<String, Object> map) throws Exception {
		// 删除因品牌菜单管理表改变而造成的多余的菜单组的菜单配置
		binOLMOPMC02_Service.refreshPosMenuGrpConfig(map);
	}

}
