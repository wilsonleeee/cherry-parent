/*
 * @(#)BINOLBSDEP01_BL.java     1.0 2010/10/27
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

package com.cherry.bs.dep.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP01_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 部门一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP01_BL {
	
	/** 部门一览画面Service */
	@Resource
	private BINOLBSDEP01_Service binOLBSDEP01_Service;
	
	@Resource
	private CodeTable codeTable;
	
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	// Excel导出列数组
	private final static String[][] proArray = {
			// 1
			{ "RegionName", "regionName", "8", "", "" },
			// 2
			{ "CityName", "cityName", "8", "", "" },
			// 3
			{ "DepartCode", "departCode", "15", "", "" },
			//4
			{ "DepartName", "departName", "15", "", "" },
			//5
			{ "Type", "type", "10", "", "1000" },			
			//6
			{ "channelName", "channelName", "10", "", "" },
			//7
			{ "Status", "status", "10", "", "1030" },
			//8
			{ "ValidFlag", "validFlag", "10", "", "1137" }
						
			};
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "makeDate", "startDate",
			"endDate" };
	
	/**
	 * 取得某一部门的直属下级部门
	 * 
	 * @param map 检索条件
	 * @return 树结构字符串
	 */
	@SuppressWarnings("unchecked")
	public String getNextOrganizationList(Map<String, Object> map) throws Exception {
		// 部门节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> otherList =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path) || "Z".equals(path)) {
			List<String> departTypeList = binOLBSDEP01_Service.getDepartType(map);
			if(departTypeList != null && !departTypeList.isEmpty()) {
				int gradeIndex = -1;
				// 部门等级List
				List gradeList = codeTable.getCodesByGrade("1000");
				List<Map<String, Object>> gradeTempList = new ArrayList<Map<String,Object>>();
				List<String[]> keyList = new ArrayList<String[]>();
				String[] key = {"Grade"};
				keyList.add(key);
				ConvertUtil.convertList2DeepList(gradeList,gradeTempList,keyList,0);
				for(int i = 0 ;i < gradeTempList.size(); i++) {
					List<Map<String, Object>> depTypeList = (List)gradeTempList.get(i).get("list");
					for(int j = 0; j < depTypeList.size(); j++) {
						String codeKey = (String)depTypeList.get(j).get("CodeKey");
						for(String type : departTypeList) {
							if(codeKey.equals(type)) {
								gradeIndex = i;
								break;
							}
						}
						if(gradeIndex >= 0) {
							break;
						}
					}
					if(gradeIndex >= 0) {
						break;
					}
				}
				if(gradeIndex >= 0) {
					// 设置顶层部门类型List作为检索条件
					map.put("departTypeList", gradeTempList.get(gradeIndex).get("list"));
					if(path == null || "".equals(path)) {
						// 取得某一用户能访问的顶层部门List
						List<Map<String, Object>> firstOrgList = binOLBSDEP01_Service.getFirstOrganizationList(map);
						if(firstOrgList != null && !firstOrgList.isEmpty()) {
							int levelFirst = 0;
							for(int i = 0; i < firstOrgList.size(); i++) {
								int level = (Integer)firstOrgList.get(i).get("level");
								if(i > 0) {
									if(level != levelFirst) {
										break;
									}
								} else {
									levelFirst = level;
								}
								list.add(firstOrgList.get(i));
							}
						}
					} else {
						// 取得未知节点下的部门List
						list = binOLBSDEP01_Service.getRootNextOrgList(map);
					}
				}
			}
		} else {
			// 取得某一部门的直属下级部门
			list = binOLBSDEP01_Service.getNextOrganizationInfoList(map);
		}
//		List<Map<String, Object>> departList =  new ArrayList<Map<String, Object>>();
//		List<String> hasLowerOrgList = new ArrayList<String>();
//		departList.addAll(list);
//		departList.addAll(otherList);
//		if(departList != null && !departList.isEmpty()) {
//			map.put("departList", departList);
//			// 取得存在下级节点的部门List
//			hasLowerOrgList = binOLBSDEP01_Service.getHasLowerOrgList(map);
//		}
		StringBuffer jsonTree = new StringBuffer();
		StringBuffer jsonSubTree = new StringBuffer();
		if(otherList != null && !otherList.isEmpty()) {
			for(int i = 0; i < otherList.size(); i++) {
				Map<String, Object> organizationMap = otherList.get(i);
				String organizationId = String.valueOf(organizationMap.get("organizationId"));
				String validFlag = (String)organizationMap.get("validFlag");
				String childOrganizationId = String.valueOf(organizationMap.get("childOrganizationId"));
				int childCount = (Integer)organizationMap.get("childCount");
				// 是否为无效部门
				String className = "";
				// 无效部门的场合
				if("0".equals(validFlag)) {
					className = "jstree-disable";
				}
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(childCount > 1 || !organizationId.equals(childOrganizationId)) {
					child = "closed";
				}
				String departName = "(" + (String)organizationMap.get("departCode") + ")" + (String)organizationMap.get(CherryConstants.DEPARTNAME);
				departName = JSONUtil.serialize(departName);
				// 树结构作成
				jsonSubTree.append("{\"data\":{\"title\":"+departName+",\"attr\":{\"id\":\""+organizationId+"\", \"class\":\""+className+"\"}},\"attr\":{\"id\":\""+"node_"+organizationId+"\"},\"state\":\""+child+"\"},");
			}
			jsonSubTree = jsonSubTree.deleteCharAt(jsonSubTree.length()-1);
		}
		// 把取得的部门List转换成树结构的字符串
		if(list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> organizationMap = list.get(i);
				String organizationId = String.valueOf(organizationMap.get("organizationId"));
				String validFlag = (String)organizationMap.get("validFlag");
				String childOrganizationId = String.valueOf(organizationMap.get("childOrganizationId"));
				int childCount = (Integer)organizationMap.get("childCount");
				// 是否为无效部门
				String className = "";
				// 无效部门的场合
				if("0".equals(validFlag)) {
					className = "jstree-disable";
				}
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(childCount > 1 || !organizationId.equals(childOrganizationId)) {
					child = "closed";
				}
				String departName = "(" + (String)organizationMap.get("departCode") + ")" + (String)organizationMap.get(CherryConstants.DEPARTNAME);
				departName = JSONUtil.serialize(departName);
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":"+departName+",\"attr\":{\"id\":\""+organizationId+"\", \"class\":\""+className+"\"}},\"attr\":{\"id\":\""+"node_"+organizationId+"\"},\"state\":\""+child+"\"},");
			}
			if(path == null || "".equals(path)) {
				jsonTree.append("{\"data\":{\"title\":\""+CherryConstants.UNKNOWN_DEPARTNAME+"\",\"attr\":{\"id\":\"Z\"}},\"attr\":{\"id\":\""+"node_Z\"},\"state\":\"closed\",\"children\":["+jsonSubTree.toString()+"]}");
			} else {
				jsonTree = jsonTree.deleteCharAt(jsonTree.length()-1);
			}
//			if(jsonSubTree.length() > 0) {
//				jsonTree.append("{\"data\":{\"title\":\""+CherryConstants.UNKNOWN_DEPARTNAME+"\"},\"state\":\"closed\",\"children\":["+jsonSubTree.toString()+"]}");
//			} else {
//				jsonTree = jsonTree.deleteCharAt(jsonTree.length()-1);
//			}
			jsonTree.append("]");
		}
		return jsonTree.toString();
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 检索条件
	 * @return 返回部门总数
	 */
	public int getOrganizationInfoCount(Map<String, Object> map) {
		
		// 取得部门总数
		return binOLBSDEP01_Service.getOrganizationInfoCount(map);
	}
	
	/**
	 * 取得部门信息List
	 * 
	 * @param map 检索条件
	 * @return 部门信息List
	 */
	public List<Map<String, Object>> getOrganizationInfoList(Map<String, Object> map) {
		
		// 取得部门信息List
		return binOLBSDEP01_Service.getOrganizationInfoList(map);
	}
	
	/**
	 * 查询定位到的部门的所有上级部门位置
	 * 
	 * @param map 检索条件
	 * @return 定位到的部门的所有上级部门位置
	 */
	public List<String> getLocationHigher(Map<String, Object> map) {
		
		List<String> resultList = new ArrayList<String>();
		String orgId = binOLBSDEP01_Service.getLocationOrgId(map);
		if(orgId != null) {
			map.put("organizationId", orgId);
			List<Map<String, Object>> locationHigher = binOLBSDEP01_Service.getLocationHigher(map);
			if(locationHigher != null && !locationHigher.isEmpty()) {
				for(int i = 0; i < locationHigher.size(); i++) {
					String type = (String)locationHigher.get(i).get("type");
					if("Z".equals(type)) {
						resultList = new ArrayList<String>();
						resultList.add("#node_Z");
						return resultList;
					} else {
						resultList.add("#node_"+locationHigher.get(i).get("organizationId"));
					}
				}
				return resultList;
			}
		}
		return resultList;
	}
	
	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getExportName(Map<String, Object> map) throws Exception {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLBSDEP01",
				language, "downloadFileName")
				+ CherryConstants.POINT
				+ CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}
	
	/**
	 * 导出信息Excel
	 * 
	 * @param map
	 * @return 返回导出信息List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binOLBSDEP01_Service
				.getOrganizationList(map);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLBSDEP01");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}

	/**
	 * 取得条件字符串
	 * 
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		condition.append(binOLMOCOM01_BL.getResourceValue("BINOLBSDEP01",
				language, "takeStockDate")
				+ "："
				+ df.format(new Date())
				+ "\0\0\0\0\0");
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";

				if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}

}
