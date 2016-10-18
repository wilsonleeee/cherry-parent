package com.cherry.mo.pmc.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.interfaces.BINOLMOPMC01_IF;
import com.cherry.mo.pmc.service.BINOLMOPMC01_Service;

public class BINOLMOPMC01_BL implements BINOLMOPMC01_IF {

	@Resource(name="binOLMOPMC01_Service")
	private BINOLMOPMC01_Service binOLMOPMC01_Service;
	
	@Override
	public int getMenuGrpCount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return binOLMOPMC01_Service.getMenuGrpCount(map);
	}

	@Override
	public List<Map<String, Object>> getMenuGrpList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMOPMC01_Service.getMenuGrpList(map);
	}

	@Override
	public void tran_addMenuGrp(Map<String, Object> map) throws Exception {
		// 插入数据
		binOLMOPMC01_Service.addMenuGrp(map);
	}


	@Override
	public Map<String, Object> getMenuGrpInfo(Map<String, Object> map)
			throws Exception {
		return binOLMOPMC01_Service.getMenuGrpInfo(map);
	}
	
	@Override
	public void tran_updateMenuGrp(Map<String, Object> map) throws Exception {
		int result = binOLMOPMC01_Service.updateMenuGrp(map);
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}
	
//	@Override
//	public void tran_copyMenuGrpAndConfig(Map<String, Object> map) throws Exception {
//		// 新增一条菜单组
//		int menuGrpIdNew = binOLMOPMC01_Service.addMenuGrp(map);
//		// 根据菜单源菜单组ID取得源菜单组的菜单配置
//		List<Map<String, Object>> menuGrpConfigList = binOLMOPMC01_Service.getMenuGrpConfig(map);
//		if(null != menuGrpConfigList && menuGrpConfigList.size() > 0) {
//			for(Map<String, Object> configMap : menuGrpConfigList) {
//				configMap.putAll(map);
//				configMap.put("menuGrpIdNew", menuGrpIdNew);
//			}
//			// 插入新的菜单配置信息
//			binOLMOPMC01_Service.insertMenuGrpConfig(menuGrpConfigList);
//		}
//	}

	@Override
	public void tran_deleteMenuGrp(Map<String, Object> map) throws Exception {
		int result = binOLMOPMC01_Service.deleteMenuGrp(map);
		if(result == 0) {
			// 删除失败
			throw new CherryException("ECM00011");
		}
		// 删除菜单分组配置表（Monitor.BIN_PosMenuGrpConfig）
		binOLMOPMC01_Service.deletePosMenuGrpConfig(map);
	}
	
	@Override
	public List<Map<String, Object>> getPosMenuRegion(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> publishList = binOLMOPMC01_Service.getPosMenuRegion(map);
		// 存放要显示的柜台信息
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "regionID", "regionName" };
		String[] keys2 = { "provinceID", "provinceName" };
		String[] keys3 = { "cityID", "cityName" };
		String[] keys4 = { "organizationID", "departName"};
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys4);
		ConvertUtil.jsTreeDataDeepList(publishList, resultList,
				keysList, 0);
		return resultList;
	}
	
	/**
	 * 取得大区信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		return binOLMOPMC01_Service.getRegionList(map);
	}

	@Override
	public List<Map<String, Object>> getPosMenuChannel(Map<String, Object> map)
			throws Exception {
		// 查询区域信息List
		List<Map<String, Object>> channelList = binOLMOPMC01_Service.getPosMenuChannel(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "organizationId", "departName" };
		keysList.add(keys1);
		keysList.add(keys2);
		List<Map<String, Object>> channelTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(channelList, channelTreeList, keysList,
				0);
		return channelTreeList;
	}

	@Override
	public List<Map<String, Object>> getPosMenuOrganize(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> publishList = binOLMOPMC01_Service.getPublishOrganize(map);
		// 存放要显示的柜台信息
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 将线性的结构转化为树结构
		resultList = ConvertUtil.getTreeList(publishList, "nodes");
		getTargetOrganize(resultList);
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> getMenuGrpByName(Map<String, Object> map) throws Exception{
		return binOLMOPMC01_Service.getMenuGrpByName(map);
	}

	/**
	 * 取得要显示的柜台的组织结构
	 * @param resultList
	 */
	private boolean getTargetOrganize(List<Map<String, Object>> list) {
		boolean flag = false;
		int exist = 0;
		for(int i=0; i < list.size(); i++){
			Map<String, Object> map = list.get(i);
			if(map.containsKey("nodes")){
				List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map
						.get("nodes");
				if(!getTargetOrganize(nodesList)){
					// 不存在要显示的节点
					list.remove(map);
					i--;
				}else {
					exist++;
				}
			} else {
				// 此时为柜台节点
				if(CherryChecker.isNullOrEmpty(map.get("configCntID"))){
					// 此柜台未配置菜单
					list.remove(map);
					i--;
				} else {
					exist++;
				}
			}
			// 存在要显示的组织
			if(exist > 0){
				flag = true;
			}
		}
		return flag;
	}

}
