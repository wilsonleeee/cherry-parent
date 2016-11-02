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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM42_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;

/**
 * 组织结构共通BL
 *
 * @author lipc
 *
 */
public class BINOLCM42_BL {
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM42_BL.class);
	@Resource
	private CodeTable codeTable;

	@Resource
	private BINOLCM42_Service binOLCM42_Service;

	/**
	 * 取得柜台信息Info
	 *
	 * @param paramMap
	 * @return
	 */
	public String getCounterList(Map<String, Object> paramMap) {
		List<Map<String,Object>> resultList = binOLCM42_Service.getCounterList(paramMap);
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
	 * 取得渠道List
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> paramMap) {

		return binOLCM42_Service.getChannelList(paramMap);
	}

	/**
	 * 取得实体仓库List
	 *
	 * @param paramMap
	 * @return
	 */
	public String getDepotInfo(Map<String, Object> paramMap) {
		List<Map<String,Object>> resultList = binOLCM42_Service.getDepotList(paramMap);
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
		return binOLCM42_Service.getLgcInventoryList(paramMap);
	}



	/**
	 * 取得部门显示等级list
	 *
	 * @param codeKey
	 * @return
	 */
	public List<Map<String, Object>> getGradeList(Map<String, Object> map) {
		List<Map<String, Object>> gradeList = getFilterGradeList(map);
		if (null != gradeList && !gradeList.isEmpty()) {
			// 权限部门树
			List<Map<String, Object>> treeList = getTreeList(map);
			if (null != treeList && !treeList.isEmpty()) {
				Map<String,Object> resultMap = new HashMap<String, Object>();
				ConvertUtil.getDepTypeList(treeList, resultMap);
				for(Map<String, Object> grade : gradeList){
					List<Map<String, Object>> departList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> list = (List<Map<String, Object>>)grade.get("list");
					if(null != list && list.size() > 0){
						for(Map<String, Object> item : list){
							String key = ConvertUtil.getString(item.get("CodeKey"));
							if("4".equals(key)){
								departList.add(new HashMap<String, Object>());
							}else{
								List<Map<String, Object>> itemList = (List<Map<String, Object>>)resultMap.get(key);
								if(null != itemList && itemList.size() > 0){
									// 过滤部门
									filterDepartList(itemList,map);
									departList.addAll(itemList);
								}
							}
						}
					}
					// 排序
					Collections.sort(departList, new MyComparator("departCode"));
					grade.put("departList", departList);
				}
			}
		}
		return gradeList;
	}


	private void filterGradeList(List<Map<String, Object>> gradeList,
			Map<String, Object> map) {
		// 权限部门类型
		List<String> typeList = binOLCM42_Service.getDepartType(map);
		if (null != typeList && !typeList.isEmpty()) {
			// 过滤部门等级
			for(int i=0; i< gradeList.size(); i++){
				boolean remFlag = true;
				List<Map<String, Object>> list = (List<Map<String, Object>>)gradeList.get(i).get("list");
				for(Map<String, Object> code : list){
					String key = ConvertUtil.getString(code.get("CodeKey"));
					if(typeList.contains(key)){
						remFlag = false;
						break;
					}
				}
				if(remFlag){
					gradeList.remove(i);
					i--;
				}else{
					break;
				}
			}
		}else{
			gradeList = null;
		}
	}

	/**
	 * 取得部门显示等级list
	 *
	 * @param codeKey
	 * @return
	 */
	public List<Map<String, Object>> getNextDepartList(Map<String, Object> map) {
		List<Map<String, Object>> nextList = new ArrayList<Map<String,Object>>();
		// 权限部门树
		List<Map<String, Object>> treeList = getTreeList(map);
//		try {
//			System.out.println(JSONUtil.serialize(treeList));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (null != treeList && !treeList.isEmpty()) {
			int departId_p = ConvertUtil.getInt(map.get("departId"));
			for(Map<String, Object> tree : treeList){
				if(departId_p == 0){
					tree2List(nextList,tree);
				}else{
					Map<String, Object> node = getNode(tree, departId_p);
					if(null != node){
						tree2List(nextList,node);
						break;
					}
				}
			}
			// 过滤部门
			filterDepartList(nextList,map);
			// 排序
			Collections.sort(nextList, new MyComparator("departCode"));
		}
		return nextList;
	}

	private Map<String, Object> getNode(Map<String, Object> node, int departId){
		Map<String, Object> returnNode = null;
		int departId_n = ConvertUtil.getInt(node.get("departId"));
		if(departId_n == departId){
			returnNode = node;
		}else{
			List<Map<String, Object>> nodes = (List<Map<String, Object>>)node.get("nodes");
			if(null != nodes && nodes.size() > 0){
				for(Map<String, Object> tempNodes : nodes){
					returnNode = getNode(tempNodes,departId);
					if(null != returnNode){
						break;
					}
				}
			}
		}
		return returnNode;
	}
	/**
	 * 部门树转list
	 * @param list
	 * @param node
	 */
	private void tree2List(List<Map<String, Object>> list, Map<String, Object> node){
		if(null != node){
			String departType = ConvertUtil.getString(node.get("departType"));
			// 非柜台
			if(!"4".equals(departType)){
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("departId", node.get("departId"));
				temp.put("departName", node.get("departName"));
				temp.put("departCode", node.get("departCode"));
				temp.put("departType", departType);
				temp.put("testType", node.get("testType"));
				temp.put("orgValid", node.get("orgValid"));
				list.add(temp);
				List<Map<String, Object>> nodes = (List<Map<String, Object>>)node.get("nodes");
				if(null != nodes && nodes.size() > 0){
					for(Map<String, Object> tempNodes : nodes){
						tree2List(list,tempNodes);
					}
				}
			}
		}
	}
	/**
	 * 过滤部门
	 * @param gradeList
	 * @param map
	 */
	private void filterDepartList(List<Map<String, Object>> departList,Map<String, Object> map){
		// 部门过滤
		if(null != departList && departList.size() > 0){
			String testType_p = ConvertUtil.getString(map.get("testType"));
			String orgValid_p = ConvertUtil.getString(map.get("orgValid"));

				for(int j=0; j<departList.size(); j++){
					Map<String, Object> depart = departList.get(j);
					String testType = ConvertUtil.getString(depart.get("testType"));
					String orgValid = ConvertUtil.getString(depart.get("orgValid"));
					if (!testType_p.equals("")&&!orgValid_p.equals("")){
					if(!testType_p.equals(testType) || !orgValid_p.equals(orgValid)){
						departList.remove(j);
						j--;
					}
				}
			}
		}
	}

	/**
	 * 部门等级处理
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getFilterGradeList(Map<String, Object> map){
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> gradeList = codeTable.getCodesByGrade("1000");
		if (null != gradeList && !gradeList.isEmpty()) {
			list = new ArrayList<Map<String,Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			keysList.add(new String[] { "Grade" });
			ConvertUtil.convertList2DeepList(gradeList, list, keysList, 0);
			// 过滤等级
			filterGradeList(list,map);
		}
		return list;
	}

	private	List<Map<String, Object>> getTreeList(Map<String, Object> map){
		List<Map<String, Object>> treeList = null;
		int brandInfoId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		// 全部部门
		List<Map<String, Object>> allDepartList = binOLCM42_Service.getAllDepartList(brandInfoId);
		List<Map<String, Object>> cloneList = allDepartList;
		try {
			cloneList = (List<Map<String, Object>>)ConvertUtil.byteClone(allDepartList);
		} catch (Exception e) {
			logger.error("################ConvertUtil.byteClone(allDepartList)异常#####################");
			e.printStackTrace();
		}
		// 权限部门
		List<Integer> privilegeList = binOLCM42_Service.getDepartPrivilegeList(map);
		if(null != privilegeList && privilegeList.size() > 0){
			treeList = ConvertUtil.getTreeList(cloneList, privilegeList);
		}
		return treeList;
	}

	/**
	 * 取得查询模式
	 *
	 * @param paramMap
	 * @return
	 */
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
	 * list 比较器
	 *
	 */
	public class MyComparator implements Comparator<Map<String, Object>> {
		String orderKey = null;

		public MyComparator(String orderKey) {
			super();
			this.orderKey = orderKey;
		}

		@Override
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			String temp1 = ConvertUtil.getString(map1.get(orderKey));
			String temp2 = ConvertUtil.getString(map2.get(orderKey));
			return temp1.compareToIgnoreCase(temp2);
		}
	}
}
