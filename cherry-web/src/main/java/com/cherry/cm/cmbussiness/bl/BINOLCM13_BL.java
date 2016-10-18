/*		
 * @(#)BINOLCM13_BL.java     1.0 2011/03/29		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM13_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;

/**
 * 组织结构共通BL
 * 
 * @author lipc
 * 
 */
public class BINOLCM13_BL {

	@Resource
	private CodeTable codeTable;

	@Resource
	private BINOLCM13_Service binOLCM13_Service;
	
	/**
	 * 取得用户权限部门数量
	 * 
	 * @param paramMap
	 * @return
	 */
//	public int getDepartCount(Map<String, Object> paramMap,String testType) {
//		Map<String, Object> map = new HashMap<String, Object>(paramMap);
//		map.put("counterKind", testType);
//		return binOLCM13_Service.getDepartCount(map);
//	}

	/**
	 * 取得组织结构子集List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> paramMap) {

		return binOLCM13_Service.getDepartList(paramMap);
	}
	
	/**
	 * 取得权限部门Info
	 * 
	 * @param paramMap
	 * @return
	 */
	public String getDepartInfo(Map<String, Object> paramMap) {
		List<Map<String,Object>> resultList = binOLCM13_Service.getDepartList2(paramMap);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			String departId = ConvertUtil.getString(tempMap.get("departId"));
			String departCode = ConvertUtil.getString(tempMap.get("departCode"));
			String departName = ConvertUtil.getString(tempMap.get("departName"));
			sb.append(departName.trim());
			sb.append("|");
			sb.append(departCode.trim());
			sb.append("|");
			sb.append(departId);
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 根据部门Id取得柜台主管List
	 * 
	 * @param paramMap
	 * @return
	 */
//	public List<Map<String, Object>> getEmployeeList(
//			Map<String, Object> paramMap) {
//
//		return binOLCM13_Service.getEmployeeList(paramMap);
//	}

	/**
	 * 取得渠道List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> paramMap) {

		return binOLCM13_Service.getChannelList(paramMap);
	}

	/**
	 * 取得第一级部门List
	 * 
	 * @param paramMap
	 * @return
	 */
//	public List<Map<String, Object>> getFirstDepartList(
//			Map<String, Object> paramMap) {
//		return binOLCM13_Service.getFirstDepartList(paramMap);
//	}

	/**
	 * 根据柜台主管ID取得该柜台主管的部门ID
	 * 
	 * @param paramMap
	 * @return
	 */
//	public String getDepartId(Map<String, Object> paramMap) {
//
//		return binOLCM13_Service.getDepartId(paramMap);
//	}

	/**
	 * 取得实体仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public String getDepotInfo(Map<String, Object> paramMap) {
		List<Map<String,Object>> resultList = binOLCM13_Service.getDepotList(paramMap);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			String depotId = ConvertUtil.getString(tempMap.get("depotId"));
			String depotCode = ConvertUtil.getString(tempMap.get("depotCode"));
			String depotName = ConvertUtil.getString(tempMap.get("depotName"));
			sb.append(depotName.trim());
			sb.append("|");
			sb.append(depotCode.trim());
			sb.append("|");
			sb.append(depotId);
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得逻辑仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getLgcInventoryList(Map<String, Object> paramMap) {
		return binOLCM13_Service.getLgcInventoryList(paramMap);
	}

	

	/**
	 * 取得部门显示等级list
	 * 
	 * @param codeKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGradeList(Map<String, Object> map) {
		List<Map<String, Object>> gradeList = codeTable.getCodesByGrade("1000");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		if (null != gradeList && !gradeList.isEmpty()) {
			List<String[]> keysList = new ArrayList<String[]>();
			keysList.add(new String[] { "Grade" });
			ConvertUtil.convertList2DeepList(gradeList, list, keysList, 0);
			Map<String, Integer> indexMap = getGradeIndex(map, list);
			int start = indexMap.get("start");
			int end = indexMap.get("end");
			if(start != -1 && end != -1){
				for(int i = start; i<= end; i++){
					resList.add(list.get(i));
				}
			}
		}
		return resList;
	}
	
	/**
	 * 填充部门等级list
	 * 
	 * @param paramMap
	 * @param gradeList
	 */
	@SuppressWarnings("unchecked")
	public void setDepartListByGrade(Map<String, Object> paramMap,
			List<Map<String, Object>> gradeList) {

		// 全部部门List
		List<Map<String, Object>> list = getDepartList(paramMap);
		// 按照部门类型分组后的list
		List<Map<String, Object>> departList = new ArrayList<Map<String, Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		keysList.add(new String[] { CherryConstants.DEPART_TYPE });
		ConvertUtil.convertList2DeepList(list, departList, keysList, 0);
		for (int i = 0; i < gradeList.size(); i++) {
			List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> grade = (List<Map<String, Object>>) gradeList
					.get(i).get("list");
			for (Map<String, Object> map : grade) {
				String codeKey = ConvertUtil.getString(map.get("CodeKey"));
				for (Map<String, Object> depart : departList) {
					String departType = ConvertUtil.getString(depart
							.get(CherryConstants.DEPART_TYPE));
					if (departType.equals(codeKey)) {
						List<Map<String, Object>> temp = (List<Map<String, Object>>) depart
								.get("list");
						tempList.addAll(temp);
						break;
					}
				}
			}
			gradeList.get(i).put("departList", tempList);
		}
	}

	/**
	 * 取得查询模式
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String[] getMode(Map<String, Object> map) {
		String mode = ConvertUtil.getString(map.get("mode"));
		mode.replaceAll(" ", "");
		if (!"".equals(mode)) {
			return mode.split(CherryConstants.COMMA);
		} else {
			String[] defMode=null;
			// 取得默认显示模式
			List modeList = codeTable.getCodes("1145");
			if(null != modeList){
				defMode = new String[modeList.size()];
				for(int i=0; i< modeList.size(); i++){
					Map modeMap = (Map)modeList.get(i);
					String modeKey = ConvertUtil.getString(modeMap.get("CodeKey"));
					defMode[i] = modeKey;
				}
			}
			return defMode;
		}
	}
	/**
	 * 取得部门等级结束索引
	 * 
	 * @param map
	 * @param gradeList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> getGradeIndex(Map<String, Object> map,
			List gradeList) {
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		List<Integer> indexList = new ArrayList<Integer>();
		int start = -1;
		int end = -1;
		List<String> list = binOLCM13_Service.getDepartType(map);
		if (null != list) {
			for (String type : list) {
				for (int i = 0; i < gradeList.size(); i++) {
					boolean isBreak = false;
					Map<String, Object> gradeMap = (Map<String, Object>) gradeList
							.get(i);
					List<Map<String, Object>> itemList = (List<Map<String, Object>>) gradeMap
							.get("list");
					for (Map<String, Object> item : itemList) {
						String key = ConvertUtil.getString(item.get("CodeKey"));
						if (key.equals(type)) {
							indexList.add(i);
							isBreak = true;
							break;
						}
					}
					if (isBreak) {
						break;
					}
				}
			}
		}
		if (indexList.size() == 1) {
			start = indexList.get(0);
			end = start;
		} else if (indexList.size() > 1) {
			Collections.sort(indexList);
			start = indexList.get(0);
			end = indexList.get(indexList.size() - 1);
		}
		indexMap.put("start", start);
		indexMap.put("end", end);
		return indexMap;
	}
	
	/**
	 * 取得渠道柜台信息List
	 * 
	 * @param map 查询条件
	 * @return 渠道柜台信息List
	 */
	public List<Map<String, Object>> getChannelCounterList(Map<String, Object> map) {
		// 取得渠道柜台信息List
		List<Map<String, Object>> channelCounterList = binOLCM13_Service.getChannelCounterList(map);
		if(channelCounterList != null && !channelCounterList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"channelId","channelName"};
			keyList.add(key1);
			ConvertUtil.convertList2DeepList(channelCounterList,list,keyList,0);
			return list;
		}
		return null;
	}
}
