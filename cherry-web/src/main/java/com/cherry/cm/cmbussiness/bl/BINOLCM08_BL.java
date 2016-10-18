/*
 * @(#)BINOLCM08_BL.java     1.0 2010/12/08
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM08_Service;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 标准区域共通 BL
 * 
 * @author hub
 * @version 1.0 2010.12.08
 */
@SuppressWarnings("unchecked")
public class BINOLCM08_BL {
	
	@Resource
	private BINOLCM08_Service binOLCM08_Service;
	
	private static List<Map<String, Object>> regionTreeList;
	
	/**
	 * 取得区域List
	 * 
	 * @param map
	 *            (所属品牌,语言)
	 * @return
	 */
	public List getReginList(Map<String, Object> map) {
		return ConvertUtil.convertList2HierarchyList(binOLCM08_Service
				.getProvinceList(map), "provinceList", "reginId", "reginName");
	}
	
	/**
	 * 取得省份List
	 * 
	 * @param map
	 *            (所属品牌,语言)
	 * @return
	 */
	public List getProvinceList(Map<String, Object> map) {
		return binOLCM08_Service.getProvinceList(map);
	}
	
	/**
	 * 取得regionId下属区域List
	 * 
	 * @param map
	 *            (区域ID,语言)
	 * @return
	 */
	public List getChildRegionList(Map<String, Object> map) {

		return binOLCM08_Service.getChildRegionList(map);
	}
	
	/**
	 * 取得所有下属区域List
	 * 
	 * @param map
	 *            (区域ID,语言)
	 * @return
	 */
	public List getAllChildStandRegionList(Map<String, Object> map) {

		return binOLCM08_Service.getAllChildStandRegionList(map);
	}
	
	/**
	 * 根据区域code或者区域名称以及区域类型去匹配区域
	 * 	code是完全匹配，名称是模糊匹配
	 * 
	 * */
	public List getRegionInfoList(Map<String,Object> map){
		return binOLCM08_Service.getRegionInfoList(map);
	}
	
	public Map<String, Object> getRegionInfoByCountyName(String provinceName, String cityName, String countyName) {
		if(countyName == null || "".equals(countyName)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceName = (String)list.get(i).get("name");
				if(provinceName == null || "".equals(provinceName) ||  _provinceName.contains(provinceName)) {
					List<Map<String, Object>> child = (List<Map<String, Object>>)list.get(i).get("child");
					if(child != null && !child.isEmpty()) {
						for(int j = 0; j < child.size(); j++) {
							String _cityName = (String)child.get(j).get("name");
							if(cityName == null || "".equals(cityName) || _cityName.contains(cityName)) {
								List<Map<String, Object>> child1 = (List<Map<String, Object>>)child.get(j).get("child");
								if(child1 != null && !child1.isEmpty()) {
									for(int x = 0; x < child1.size(); x++) {
										String _countyName = (String)child1.get(x).get("name");
										if(_countyName.contains(countyName)) {
											Map<String, Object> result = new HashMap<String, Object>();
											result.put("provinceId", list.get(i).get("id"));
											result.put("provinceName", list.get(i).get("name"));
											result.put("cityId", child.get(j).get("id"));
											result.put("cityName", child.get(j).get("name"));
											result.put("countyId", child1.get(x).get("id"));
											result.put("countyName", child1.get(x).get("name"));
											return result;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Map<String, Object> getRegionInfoByCityName(String provinceName, String cityName) {
		if(cityName == null || "".equals(cityName)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceName = (String)list.get(i).get("name");
				if(provinceName == null || "".equals(provinceName) ||  _provinceName.contains(provinceName)) {
					List<Map<String, Object>> child = (List<Map<String, Object>>)list.get(i).get("child");
					if(child != null && !child.isEmpty()) {
						for(int j = 0; j < child.size(); j++) {
							String _cityName = (String)child.get(j).get("name");
							if(_cityName.contains(cityName)) {
								Map<String, Object> result = new HashMap<String, Object>();
								result.put("provinceId", list.get(i).get("id"));
								result.put("provinceName", list.get(i).get("name"));
								result.put("cityId", child.get(j).get("id"));
								result.put("cityName", child.get(j).get("name"));
								return result;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Map<String, Object> getRegionInfoByProvinceName(String provinceName) {
		if(provinceName == null || "".equals(provinceName)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceName = (String)list.get(i).get("name");
				if(_provinceName.contains(provinceName)) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("provinceId", list.get(i).get("id"));
					result.put("provinceName", list.get(i).get("name"));
					return result;
				}
			}
		}
		return null;
	}
	
	public Map<String, Object> getRegionInfoByCountyCode(String provinceCode, String cityCode, String countyCode) {
		if(countyCode == null || "".equals(countyCode)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceCode = (String)list.get(i).get("code");
				if(provinceCode == null || "".equals(provinceCode) ||  _provinceCode.equals(provinceCode)) {
					List<Map<String, Object>> child = (List<Map<String, Object>>)list.get(i).get("child");
					if(child != null && !child.isEmpty()) {
						for(int j = 0; j < child.size(); j++) {
							String _cityCode = (String)child.get(j).get("code");
							if(cityCode == null || "".equals(cityCode) || _cityCode.equals(cityCode)) {
								List<Map<String, Object>> child1 = (List<Map<String, Object>>)child.get(j).get("child");
								if(child1 != null && !child1.isEmpty()) {
									for(int x = 0; x < child1.size(); x++) {
										String _countyCode = (String)child1.get(x).get("code");
										if(_countyCode.equals(countyCode)) {
											Map<String, Object> result = new HashMap<String, Object>();
											result.put("provinceId", list.get(i).get("id"));
											result.put("provinceName", list.get(i).get("name"));
											result.put("cityId", child.get(j).get("id"));
											result.put("cityName", child.get(j).get("name"));
											result.put("countyId", child1.get(x).get("id"));
											result.put("countyName", child1.get(x).get("name"));
											return result;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Map<String, Object> getRegionInfoByCityCode(String provinceCode, String cityCode) {
		if(cityCode == null || "".equals(cityCode)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceCode = (String)list.get(i).get("code");
				if(provinceCode == null || "".equals(provinceCode) ||  _provinceCode.equals(provinceCode)) {
					List<Map<String, Object>> child = (List<Map<String, Object>>)list.get(i).get("child");
					if(child != null && !child.isEmpty()) {
						for(int j = 0; j < child.size(); j++) {
							String _cityCode = (String)child.get(j).get("code");
							if(_cityCode.equals(cityCode)) {
								Map<String, Object> result = new HashMap<String, Object>();
								result.put("provinceId", list.get(i).get("id"));
								result.put("provinceName", list.get(i).get("name"));
								result.put("cityId", child.get(j).get("id"));
								result.put("cityName", child.get(j).get("name"));
								return result;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Map<String, Object> getRegionInfoByProvinceCode(String provinceCode) {
		if(provinceCode == null || "".equals(provinceCode)) {
			return null;
		}
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _provinceCode = (String)list.get(i).get("code");
				if(_provinceCode.equals(provinceCode)) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("provinceId", list.get(i).get("id"));
					result.put("provinceName", list.get(i).get("name"));
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据区域ID取得区域名称
	 * 
	 * @param id 区域ID
	 * @return 区域名称
	 */
	public String getRegionNameById(String id) {
		List<Map<String, Object>> list = getRegionTreeList();
		return getRegionNameById(list, id);
	}
	
	/**
	 * 根据区域ID取得区域名称
	 * 
	 * @param list 区域List
	 * @param id 区域ID
	 * @return 区域名称
	 */
	private String getRegionNameById(List<Map<String, Object>> list, String id) {
		if(list != null && !list.isEmpty()) {
			for(int i = 0; i < list.size(); i++) {
				String _id = (String)list.get(i).get("id");
				if(_id.equals(id)) {
					return (String)list.get(i).get("name");
				}
				List<Map<String, Object>> child = (List<Map<String, Object>>)list.get(i).get("child");
				if(child != null && !child.isEmpty()) {
					String result = getRegionNameById(child, id);
					if(result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 取得省份城市区县层级数据（JSON格式）
	 * @throws Exception 
	 * 
	 */
	public String getRegionJson() throws Exception {
		List<Map<String, Object>> list = getRegionTreeList();
		if(list != null && !list.isEmpty()) {
			return CherryUtil.obj2Json(list);
		}
		return null;
	}
	
	/**
	 * 取得省份城市区县层级数据List
	 * 
	 */
	public List<Map<String, Object>> creatRegionTreeList() {
		
		List<Map<String, Object>> allRegionList = binOLCM08_Service.getAllRegionList();
		if(allRegionList != null && !allRegionList.isEmpty()) {
			List<Map<String, Object>> provinceCityList = new ArrayList<Map<String,Object>>();
			Map<String, Map<String, Object>> provinceCityMap = new HashMap<String, Map<String, Object>>();
			for(int i = 0; i < allRegionList.size(); i++) {
				Map<String, Object> regionMap = allRegionList.get(i);
				Integer type = (Integer)regionMap.get("type");
				String path = (String)regionMap.get("path");
				if(type == 1) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", String.valueOf(regionMap.get("id")));
					map.put("name", regionMap.get("name"));
					map.put("code", regionMap.get("code"));
					provinceCityList.add(map);
					provinceCityMap.put(path, map);
				}
			}
			for(int i = 0; i < allRegionList.size(); i++) {
				Map<String, Object> regionMap = allRegionList.get(i);
				Integer type = (Integer)regionMap.get("type");
				String path = (String)regionMap.get("path");
				if(type == 2 || type == 3) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", String.valueOf(regionMap.get("id")));
					map.put("name", regionMap.get("name"));
					map.put("code", regionMap.get("code"));
					provinceCityMap.put(path, map);
					String headPath = getHeadPath(path);
					Map<String, Object> headMap = provinceCityMap.get(headPath);
					if(headMap != null) {
						List<Map<String, Object>> child = (List<Map<String, Object>>)headMap.get("child");
						if(child == null) {
							child = new ArrayList<Map<String,Object>>();
							headMap.put("child", child);
						}
						child.add(map);
					}
				}
			}
			for(int i = 0; i < allRegionList.size(); i++) {
				Map<String, Object> regionMap = allRegionList.get(i);
				Integer type = (Integer)regionMap.get("type");
				String path = (String)regionMap.get("path");
				if(type == 4) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", String.valueOf(regionMap.get("id")));
					map.put("name", regionMap.get("name"));
					map.put("code", regionMap.get("code"));
					String headPath = getHeadPath(path);
					Map<String, Object> headMap = provinceCityMap.get(headPath);
					if(headMap != null) {
						List<Map<String, Object>> child = (List<Map<String, Object>>)headMap.get("child");
						if(child == null) {
							child = new ArrayList<Map<String,Object>>();
							headMap.put("child", child);
						}
						child.add(map);
					}
				}
			}
			return provinceCityList;
		}
		return null;
	}
	
	/**
	 * 根据path取得上级path
	 * 
	 * @param path
	 */
	public String getHeadPath(String path) {
		StringBuffer result = new StringBuffer();
		if(path != null && !"".equals(path)) {
			String[] paths = path.split("/");
			for(int i = 0; i < paths.length-1; i++) {
				result.append(paths[i]);
				result.append("/");
			}
		}
		return result.toString();
	}
	
	public List<Map<String, Object>> getRegionTreeList() {
		if(regionTreeList == null) {
			synchronized (this) {
				if(regionTreeList == null) {
					regionTreeList = creatRegionTreeList();
				}
			}
		}
		return regionTreeList;
	}
	
	public void refreshRegionTreeList() {
		regionTreeList = creatRegionTreeList();
	}
}
