/*  
 * @(#)BINOLMOMAN04_BL.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.mo.man.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.interfaces.BINOLMOMAN04_IF;
import com.cherry.mo.man.service.BINOLMOMAN04_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.interfaces.MachineSynchro_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLMOMAN04_BL extends SsBaseBussinessLogic implements
		BINOLMOMAN04_IF {

	@Resource
	private BINOLMOMAN04_Service binMOMAN04_Service;
	@Resource
	private MachineSynchro_IF machineSynchro_IF;

	@Resource
	private CodeTable codeTable;
	
	/*
	 * 获取设置了升级状态的树形节点信息
	 * 
	 * @author zgl
	 * 
	 * @param map
	 * 
	 * @result list
	 */
	@Override
	public List<Map<String, Object>> getTreeNodesList(Map<String, Object> map) {
		String regionType = ConvertUtil.getString(map.get("regionType"));
		List<Map<String, Object>> treeNodes;
		//去除id中的“re_”
		scratchReOfId(map);
		if (MonitorConstants.REGIONTYPE_REGION_LEVELED.equals(regionType)) {
			regionType = MonitorConstants.REGIONTYPE_PRIVINCE_LEVELED;
			map.put("regionType", regionType);
			treeNodes = binMOMAN04_Service.getSubRegionList(map);
			markerRegionElement(treeNodes, true);
		} else if (MonitorConstants.REGIONTYPE_PRIVINCE_LEVELED
				.equals(regionType)) {
			String capital = MonitorConstants.REGIONTYPE_CAPITAL_LEVELED;
			String city = MonitorConstants.REGIONTYPE_CITY_LEVELED;
			map.remove("regionType");
			map.put("capital", capital);
			map.put("city", city);
			treeNodes = binMOMAN04_Service.getSubRegionList(map);
			markerRegionElement(treeNodes, true);
		} else {
			treeNodes = binMOMAN04_Service.getCounterList(map);
		}
		if(ConvertUtil.getString(map.get("checked")).equals("true")){
			for(int i = 0 ; i < treeNodes.size() ; i++ ){
				Map<String,Object> mapOfList = treeNodes.get(i);
				mapOfList.put("checked", true);
				
			}
		}
		return treeNodes;
	}

	/**
	 * 向返回的list中添加isParent
	 * 
	 * @author zgl
	 * @param map
	 * @result list
	 * 
	 * */
	public List<Map<String, Object>> addIsParent(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			map.put("isParent", true);
		}
		return list;
	}

	/**
	 * 对返回的区域List中的元素进行标记处理
	 * 
	 * @param list
	 * @param marker
	 * @return list
	 * 
	 * */
	public void markerRegionElement(
			List<Map<String, Object>> list, Boolean marker) {
		for (int i = 0; i < list.size(); i++) {
			
			Map<String, Object> map = list.get(i);
			String id = ConvertUtil.getString(map.get("id"));
			// 对区域的ID进行简单的处理,在区域ID前加上"re_"
			id = "re_" + id;
			map.put("id", id);
			// 如果marker的值等于true那么给map加上isParenet元素
			if (marker == true) {
				map.put("isParent", true);
			}
		}
	}

	/**
	 * 处理节点查询的中的查询条件,将ID中前的"re_"去掉
	 * 
	 * @param map
	 * 
	 * 
	 * */
	public void scratchReOfId(Map<String, Object> map) {
		String id = ConvertUtil.getString(map.get("id"));
		if (id == null || ("").equals(id)) {
			;
		} else {
			String[] idArray = id.split("_");
			map.put("id", idArray[1]);
		}
	}

	/**
	 * 获取含有柜台的大区信息
	 * 
	 * @author zgl
	 * @param map
	 * @result list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		List<Map<String, Object>> resultMap = binMOMAN04_Service
				.getRegionList(map);
		for (int i = 0; i < resultMap.size(); i++) {
			resultMap.get(i).put("isParent", true);
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getSubRegionList(Map<String, Object> map) {
		return null;
	}

	/*
	 * 获取没有升级的柜台的大区信息
	 * @author zgl
	 * @param map
	 * @result list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String getLeftRootNodes(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binMOMAN04_Service
				.getRegionList(map);
		List<Map<String, Object>> leftRootNodesList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> resultMap = resultList.get(i);
			String updateStatus = ConvertUtil.getString(resultMap.get("updateStatus"));
			if (MonitorConstants.UPDATESTATUS_NO_LEVELED.equals(updateStatus)) {
				leftRootNodesList.add(resultMap);
			}
		}
		markerRegionElement(leftRootNodesList, true);
		String leftRootNodes = null;
		try {
			leftRootNodes = JSONUtil.serialize(leftRootNodesList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return leftRootNodes;
	}

	/**
	 * 获取设置了升级状态的柜台的大区信息
	 * @author zgl
	 * @param map
	 * @result list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String getRightRootNodes(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binMOMAN04_Service
				.getRegionList(map);
		List<Map<String, Object>> rightAboveRootNodesList = new ArrayList();
		List<Map<String, Object>> rightUndersideRootNodesList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> resultMap = resultList.get(i);
			String updateStatus = ConvertUtil.getString(resultMap.get("updateStatus"));
			if (MonitorConstants.UPDATESTATUS_OFFICIAL_LEVELED
					.equals(updateStatus)) {
				rightAboveRootNodesList.add(resultMap);
			}else if (MonitorConstants.UPDATESTATUS_TEST_LEVELED.equals(updateStatus)) {
				rightUndersideRootNodesList.add(resultMap);
			}
		}
		markerRegionElement(rightAboveRootNodesList,true);
		markerRegionElement(rightUndersideRootNodesList, true);
		String rightAboveRootNodes = null;
		String rightUndersideRootNodes = null;
		String rightRootNodes = null;
		try {
			rightAboveRootNodes = JSONUtil.serialize(rightAboveRootNodesList);
			rightUndersideRootNodes = JSONUtil.serialize(rightUndersideRootNodesList);
			rightRootNodes = rightAboveRootNodes + "*" + rightUndersideRootNodes;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rightRootNodes;
	}

	@Override
	public void tran_updateCounterUpdateStatus(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception {
		/** updateByRegion,用以存放区域节点信息的list */
		List<Map<String, Object>> updateByRegion = new ArrayList<Map<String, Object>>();

		/** updateByCounter,用以存放柜台节点信息list */
		List<Map<String, Object>> updateByCounter = new ArrayList<Map<String, Object>>();

		/** counterList,用以存放柜台list,以便下发到接口数据库使用 */
		List<Map<String, Object>> counterList = new ArrayList<Map<String, Object>>();

		/** 记录柜台要升级到的状态 */
		String newUpdateStatus = ConvertUtil.getString(list.get(0).get("newUpdateStatus"));
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> updateMap = list.get(i);
			String updateStatus = ConvertUtil.getString(updateMap.get("updateStatus"));
			updateMap.put(CherryConstants.UPDATEDBY, map
					.get(CherryConstants.UPDATEDBY));
			updateMap.put(CherryConstants.UPDATEPGM, map
					.get(CherryConstants.UPDATEPGM));
			updateMap.put(CherryConstants.BRANDINFOID, map
					.get(CherryConstants.BRANDINFOID));
			if (updateStatus == "null" || "".equals(updateStatus)) {
				counterList
						.addAll(binMOMAN04_Service
								.getCounterInformationWhenUpdateStatusByCounterId(updateMap));
				updateByCounter.add(updateMap);
			} else {
				/** 根据区域信息获得柜台信息,然后将这些数据追加到counterList中 */
				counterList
						.addAll(binMOMAN04_Service
								.getCounterInformationWhenUpdateStatusByRegionId(updateMap));
				updateByRegion.add(updateMap);
			}
		}
		// 更新数据库
		binMOMAN04_Service
				.updateCounterUpdateStatusByCounterId(updateByCounter);
		binMOMAN04_Service.updateCounterUpdateStatusByRegionId(updateByRegion);

		/* 下发到接口数据库 */
		// 循环将柜台获取 的柜台list展开并在map中加入相应的key和value然后调用接口
		if (counterList.size() > 0) {
			for (int j = 0; j < counterList.size(); j++) {
				Map<String, Object> counterMap = counterList.get(j);
				counterMap.put("UpdateStatus", newUpdateStatus);
				// 调用接口完成数据的下发
				machineSynchro_IF.synchroMachineUpgrade(counterMap);
				// 终端升级暂时先在存储过程中实现，程序暂还是使用老的synchroMachineUpgrade存储过程。2012-09-03
				//counterMap.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_UPGRADE);
				//machineSynchro_IF.synchroMachine(counterMap);
			}
		}
	}

	/**
	 * 获取未设置升级信息的柜台所在的大区List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	public String getRegionNoUpdateStatus(Map<String, Object> map) {
		List<Map<String, Object>> leftRootNodesList = binMOMAN04_Service.getRegionNoUpdateStatus(map);
		markerRegionElement(leftRootNodesList, true);
		String leftRootNodes = null;
		try {
			leftRootNodes = JSONUtil.serialize(leftRootNodesList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return leftRootNodes;
	}

	/**
	 * 获取未设置升级状态的树形节点信息
	 * 
	 * @author zgl
	 * 
	 * @param map
	 * 
	 * @result list
	 */
	public List<Map<String, Object>> getSubRegionNoUpdateStatus(
			Map<String, Object> map) {
		String regionType = ConvertUtil.getString(map.get("regionType"));
		List<Map<String, Object>> treeNodes;
		scratchReOfId(map);
		if (MonitorConstants.REGIONTYPE_REGION_LEVELED.equals(regionType)) {
			regionType = MonitorConstants.REGIONTYPE_PRIVINCE_LEVELED;
			map.put("regionType", regionType);
			treeNodes = binMOMAN04_Service.getSubRegionNoUpdateStatus(map);
			markerRegionElement(treeNodes, true);
		} else if (MonitorConstants.REGIONTYPE_PRIVINCE_LEVELED
				.equals(regionType)) {
			String capital = MonitorConstants.REGIONTYPE_CAPITAL_LEVELED;
			String city = MonitorConstants.REGIONTYPE_CITY_LEVELED;
			map.remove("regionType");
			map.put("capital", capital);
			map.put("city", city);
			treeNodes = binMOMAN04_Service.getSubRegionNoUpdateStatus(map);
			markerRegionElement(treeNodes, true);
		} else {
			treeNodes = binMOMAN04_Service.getCounterNoUpdateStatus(map);
		}
		if(ConvertUtil.getString(map.get("checked")).equals("true")){
			for(int i = 0 ; i < treeNodes.size() ; i++ ){
				Map<String,Object> mapOfList = treeNodes.get(i);
				mapOfList.put("checked", true);
				
			}
		}
		return treeNodes;
	}

	/**
	 * 将设置了升级状态转换成为未设置状态
	 * 
	 * @param list
	 * @throws Exception
	 * 
	 * 
	 * */
	public void tran_fromUpdateStatusToNone(List<Map<String, Object>> list)
			throws CherryException {
		/** updateByRegion,用以存放区域节点信息的list */
		List<Map<String, Object>> updateByRegion = new ArrayList<Map<String, Object>>();
		/** updateByCounter,用以存放柜台节点信息list */
		List<Map<String, Object>> updateByCounter = new ArrayList<Map<String, Object>>();
		/** 要下发的柜台信息list */
		List<Map<String, Object>> provideCounterList = new ArrayList<Map<String, Object>>();
		// 遍历lsit
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String id = ConvertUtil.getString(map.get("id"));
				String[] idArray = id.split("_");
				if (idArray[0].equals("re")) {
					map.put("id", idArray[1]);
					updateByRegion.add(map);
					provideCounterList
							.addAll(binMOMAN04_Service
									.getCounterInformationWhenUpdateStatusByRegionId(map));
				} else {
					updateByCounter.add(map);
					provideCounterList
							.addAll(binMOMAN04_Service
									.getCounterInformationWhenUpdateStatusByCounterId(map));
				}
			}
		}
		binMOMAN04_Service.deleteUpdateStatusByCounter(updateByCounter);
		binMOMAN04_Service.deleteUpdateStatusByRegion(updateByRegion);
		// 下发到老后台
		if (provideCounterList.size() > 0) {
			for (int j = 0; j < provideCounterList.size(); j++) {
				Map<String, Object> counterMap = provideCounterList.get(j);
				counterMap.put("UpdateStatus", MonitorConstants.UPDATESTATUS_NO_LEVELED);
				// 调用接口完成数据的下发
				machineSynchro_IF.synchroMachineUpgrade(counterMap);
				// 终端升级暂时先在存储过程中实现，程序暂还是使用老的synchroMachineUpgrade存储过程。2012-09-03
				//counterMap.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_UPGRADE);
				//machineSynchro_IF.synchroMachine(counterMap);
			}
		}
	}

	/**
	 *柜台升级
	 * 
	 * @param list
	 * @param map
	 * @throws Exception
	 * 
	 * */
	public void tran_fromNoneToUpdateStatus(List<Map<String, Object>> list,
			Map<String, Object> map) throws CherryException {
		/** updateByCounter,用以存放柜台节点信息list */
		List<Map<String, Object>> updateByCounter = new ArrayList<Map<String, Object>>();
		/** 要下发的柜台信息list */
		// 遍历lsit
		Map<String, Object> brandCodeAndLastCode = binMOMAN04_Service.getBrandCodeLastCode(map);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mapOfList = list.get(i);
				String id = ConvertUtil.getString(mapOfList.get("id"));
				//写升级状态表时使用
				mapOfList.put("brandInfoId", map.get("brandInfoId"));
				mapOfList.put("machineType", map.get("machineType"));
				//供下发时使用
				mapOfList.put("UpdateStatus", mapOfList.get("updateStatus"));
				mapOfList.put("MachineType", map.get("machineType"));
				mapOfList.put("LastCode", brandCodeAndLastCode.get("LastCode"));
				mapOfList.put("BrandCode", brandCodeAndLastCode.get("BrandCode"));
				String[] idArray = id.split("_");
				// 判断该条map信息包含的是区域信息还是柜台信息
				if (idArray[0].equals("re")) {
					mapOfList.put("id", idArray[1]);
					// 根据区域获取该区域下的所有的柜台信息,以便柜台升级操作
					List<Map<String, Object>> counterList = binMOMAN04_Service
							.getCounterNoUpdateStatus(mapOfList);
					for (int j = 0; j < counterList.size(); j++) {
						// 向取得的区域list中加入一些key和value以便数据插入时使用
						counterList.get(j).put("UpdateStatus",
								mapOfList.get("updateStatus"));
						counterList.get(j).put("updateStatus",
								mapOfList.get("updateStatus"));
						counterList.get(j).put("brandInfoId",
								map.get("brandInfoId"));
						counterList.get(j).put("machineType",
								map.get("machineType"));
						counterList.get(j).put(CherryConstants.CREATEDBY,
								map.get(CherryConstants.CREATEDBY));
						counterList.get(j).put(CherryConstants.CREATEPGM,
								map.get(CherryConstants.CREATEPGM));
						counterList.get(j).put(CherryConstants.UPDATEDBY,
								map.get(CherryConstants.UPDATEDBY));
						counterList.get(j).put(CherryConstants.UPDATEPGM,
								map.get(CherryConstants.UPDATEPGM));
					}
					updateByCounter.addAll(counterList);
				} else {
					Map<String, Object> counterCode = binMOMAN04_Service.getCounterCode(mapOfList);
					mapOfList.put("CounterCode", counterCode.get("CounterCode"));
					mapOfList.put(CherryConstants.CREATEDBY, map
							.get(CherryConstants.CREATEDBY));
					mapOfList.put(CherryConstants.CREATEPGM, map
							.get(CherryConstants.CREATEPGM));
					mapOfList.put(CherryConstants.UPDATEDBY, map
							.get(CherryConstants.UPDATEDBY));
					mapOfList.put(CherryConstants.UPDATEPGM, map
							.get(CherryConstants.UPDATEPGM));
					updateByCounter.add(mapOfList);
				}
			}
		}
		// 柜台升级
		binMOMAN04_Service.insertUpdateStatusByCounter(updateByCounter);
		// 下发到老后台
		if (updateByCounter.size() > 0) {
			for (int i = 0; i < updateByCounter.size(); i++) {
				Map<String, Object> counterMap = updateByCounter.get(i);
				// 调用接口完成数据的下发
				machineSynchro_IF.synchroMachineUpgrade(counterMap);
				// 终端升级暂时先在存储过程中实现，程序暂还是使用老的synchroMachineUpgrade存储过程。2012-09-03
				//counterMap.put(CherryConstants.SYNCHROMACHINE_OPERATE, CherryConstants.SYNCHROMACHINE_OPERATE_UPGRADE);
				//machineSynchro_IF.synchroMachine(counterMap);
			}
		}
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getMachineTypeListFilter(
            Map<String, Object> map) {
        List<Map<String,Object>> machineTypeList = binMOMAN04_Service.getMachineTypeList(map);
        List<Map<String,Object>> allMachineTypeList = codeTable.getCodes("1101");
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        //过滤机器信息表里没有的机器类型
        for(int i=0;i<allMachineTypeList.size();i++){
            String machineType = ConvertUtil.getString(allMachineTypeList.get(i).get("CodeKey"));
            for(int j=0;j<machineTypeList.size();j++){
                String curMachineType = ConvertUtil.getString(machineTypeList.get(j).get("MachineType"));
                if(machineType.equals(curMachineType)){
                    returnList.add(allMachineTypeList.get(i));
                    break;
                }
            }
        }
        
        return returnList;
    }

}
