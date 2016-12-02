/*	
 * @(#)ConvertUtil.java     1.0 2010/10/12		
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
package com.cherry.cm.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cherry.cm.cmbussiness.form.SaleMainEntity;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.form.DataTable_BaseForm;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * <p>
 * 共通 转换Util
 * </p>
 * 
 * @author lipc
 * 
 */
@SuppressWarnings("unchecked")
public class ConvertUtil {
	
	/**
	 * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 层级List转换(支持多层转换)
	 * </p>
	 *  
	 * @param List
	 *          	转换前List
	 * @param List
	 *          	转换后List
	 * @param List
	 *          	多层key
	 * @param int
	 *          	层级      
	 * @return 无
	 * 
	 * @throws 无
	 */
	public static void convertList2DeepList(List<Map<String, Object>> list,
			List<Map<String, Object>> rootList, List<String[]> keysList,
			int deep) {
		if (list != null && rootList != null && keysList != null
				&& keysList.size() > deep) {
			String[] keys = keysList.get(deep);
			String id = null;
			if (keys != null && keys.length > 0) {
				id = keys[0];
			}
			for (Map<String, Object> map : list) {
				// 判断是否已经存在了
				boolean isNotEqual = true;
				for (Map<String, Object> rootMap : rootList) {
					// 外层List的id
					Object value = rootMap.get(id);
					/* id相等的时候说明已经存在 */
					if (value != null && value.equals(map.get(id))) {
						// 更新外层List已存在的map
						updateRootList(2, map, rootMap, "list", id, keys);
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootList.add(rootMap);
					// 往外层List添加map
					updateRootList(1, map, rootMap, "list", id, keys);
				}
			}
			if (deep < keysList.size() - 1) {
				deep++;
				for (Map<String, Object> rootMap : rootList) {
					List<Map<String, Object>> deepList = (List<Map<String, Object>>) rootMap
							.get("list");
					List<Map<String, Object>> deepListNew = new ArrayList<Map<String, Object>>();
					rootMap.put("list", deepListNew);
					convertList2DeepList(deepList, deepListNew, keysList, deep);
				}
			}
		}
	}

	/**
	 * <p>
	 * 层级List转换(两层)
	 * </p>
	 * 
	 * @param List
	 *            	转换前List
	 * @param String
	 *            	里面一层List的名字
	 * @param String
	 *            	外层List的id属性
	 * @param String[] 
	 * 				外层List其它的属性
	 * @return String 
	 * 				转换好的层级List
	 * 
	 * @throws 无
	 */
	public static List<Map<String, Object>> convertList2HierarchyList(
			List<Map<String, Object>> list, String depName, String id,
			String... keys) {
		// 外层List
		List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				// 判断是否已经存在了
				boolean isNotEqual = true;
				for (Map<String, Object> rootMap : rootList) {
					// 外层List的id
					Object value = rootMap.get(id);
					/* id相等的时候说明已经存在 */
					if (value != null && value.equals(map.get(id))) {
						// 更新外层List已存在的map
						updateRootList(2, map, rootMap, depName, id, keys);
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootList.add(rootMap);
					// 往外层List添加map
					updateRootList(1, map, rootMap, depName, id, keys);
				}

			}
		}
		return rootList;
	}

	/**
	 * <p>
	 * 更新外层List
	 * </p>
	 * 
	 * @param int 
	 * 				更新区分
	 * @param Map
	 *            	读取的map
	 * @param String
	 *            	外层List的map
	 * @param String
	 *            	里面一层List的名字
	 * @param String
	 *            	外层List的id属性
	 * @param String[] 
	 * 				外层List其它的属性
	 * @return 无
	 * 
	 * @throws 无
	 */

	private static void updateRootList(int updateFlg, Map<String, Object> map,
			Map<String, Object> rootMap, String depName, String id,
			String[] keys) {
		// 里层List
		List<Map<String, Object>> depList = new ArrayList<Map<String, Object>>();
		if (map != null && rootMap != null) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.putAll(map);
			if (updateFlg == 1) {
				// 外层List的id属性
				rootMap.put(id, tempMap.get(id));
				tempMap.remove(id);
				// 往外层List中添加里层List属性
				rootMap.put(depName, depList);
			} else {
				depList = (List<Map<String, Object>>) rootMap.get(depName);
				tempMap.remove(id);
			}
			if (keys != null) {
				/* 往外层List中添加其它属性 */
				for (String key : keys) {
					if (id != null && !id.equals(key)) {
						if (updateFlg == 1) {
							rootMap.put(key, tempMap.get(key));
						}
						tempMap.remove(key);
					}
				}
			}
			Map<String, Object> depMap = new HashMap<String, Object>();
			depMap.putAll(tempMap);
			if (depList != null) {
				depList.add(depMap);
			}
		}
	}

	/**
	 * <p>
	 * 页面上的层级List转换(支持多层转换)
	 * </p>
	 * 
	 * @param List
	 *          	转换前List
	 * @param List
	 *          	转换后List
	 * @param List
	 *          	多层key
	 * @param int
	 *          	层级
	 * @return 无
	 * 
	 * @throws 无
	 */
	public static void jsTreeDataDeepList(List<Map<String, Object>> list,
			List<Map<String, Object>> rootList, List<String[]> keysList,
			int deep) {
		if (list != null && rootList != null && keysList != null
				&& keysList.size() > deep) {
			String[] keys = keysList.get(deep);
			String id = null;
			String name = null;
			if (keys != null && keys.length > 1) {
				id = keys[0];
				name = keys[1];
			}
			for (Map<String, Object> map : list) {
				// 判断是否已经存在了
				boolean isNotEqual = true;
				for (Map<String, Object> rootMap : rootList) {
					// 外层List的id
					Object value = rootMap.get("id");
					/* id相等的时候说明已经存在 */
					if (value != null && value.equals(map.get(id))) {
						// 更新外层List已存在的map
						updatePageRootList(2, map, rootMap, "nodes", id,
								name);
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootList.add(rootMap);
					// 往外层List添加map
					updatePageRootList(1, map, rootMap, "nodes", id, name);
				}
			}
			if (deep < keysList.size() - 1) {
				deep++;
				for (Map<String, Object> rootMap : rootList) {
					List<Map<String, Object>> deepList = (List<Map<String, Object>>) rootMap
							.get("nodes");
					List<Map<String, Object>> deepListNew = new ArrayList<Map<String, Object>>();
					rootMap.put("nodes", deepListNew);
					jsTreeDataDeepList(deepList, deepListNew, keysList, deep);
				}
			} else {
				for (Map<String, Object> rootMap : rootList) {
					rootMap.remove("nodes");
				}
			}
		}
	}

	/**
	 * <p>
	 * 更新页面外层List
	 * </p>
	 * 
	 * @param int 
	 * 				更新区分
	 * @param Map
	 *            	读取的map
	 * @param String
	 *            	外层List的map
	 * @param String
	 *            	里面一层List的名字
	 * @param String
	 *            	外层List的id属性
	 * @param String 
	 * 				外层List其它的属性
	 * @return 无
	 * 
	 * @throws 无
	 */

	private static void updatePageRootList(int updateFlg,
			Map<String, Object> map, Map<String, Object> rootMap,
			String depName, String id, String name) {
		// 里层List
		List<Map<String, Object>> depList = new ArrayList<Map<String, Object>>();
		if (map != null && rootMap != null) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.putAll(map);
			if (updateFlg == 1) {
				// 外层List的id属性
				rootMap.put("id", tempMap.get(id));
				tempMap.remove(id);
				// 往外层List中添加里层List属性
				rootMap.put(depName, depList);
			} else {
				depList = (List<Map<String, Object>>) rootMap.get(depName);
				tempMap.remove(id);
			}
			rootMap.put("name", tempMap.get(name));
			tempMap.remove(name);
			Map<String, Object> depMap = new HashMap<String, Object>();
			depMap.putAll(tempMap);
			if (depList != null) {
				depList.add(depMap);
			}
		}
	}

	/**
	 * <p>
	 * 判断对象数组中是否存在相同值的对象
	 * </p>
	 * 
	 * 
	 * @param tArray
	 *            对象数组
	 * @param obj
	 *            比较的对象
	 * @return boolean 比较结果
	 * 
	 * @throws 无
	 */
	public static <T> boolean isContain(T[] tArray, T obj) {
		if (tArray != null) {
			for (T t : tArray) {
				if (t != null && t.equals(obj)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * TableParamsDTO 属性设置到paramsMap中
	 * 
	 * @param tableParams
	 * @param paramMap
	 */
	public static void setForm(DataTable_BaseForm baseForm,
			Map<String, Object> paramMap) {

		// 列名字符串
		String sColumns = baseForm.getSColumns();
		// 列名数组
		String[] columns = {};
		// 排序方式
		String sortDir = baseForm.getSSortDir_0();
		// 需要排序的列号
		int sortCol = baseForm.getISortCol_0();
		if (!CherryChecker.isNullOrEmpty(sColumns, true)) {
			columns = sColumns.split(CherryBatchConstants.COMMA);
		}
		if (sortCol < columns.length && baseForm.getSort() == null) {
			baseForm
					.setSort(columns[sortCol] + CherryBatchConstants.SPACE + sortDir);
		}
		paramMap.put("SORT_ID", baseForm.getSort());
		paramMap.put("START", baseForm.getIDisplayStart() + 1);
		paramMap.put("END", baseForm.getIDisplayStart()
				+ baseForm.getIDisplayLength());
		// 如果过滤条件不为空
		if (null!= baseForm.getSSearch() && !"".equals(baseForm.getSSearch())){
			paramMap.put("FILTER_VALUE", baseForm.getSSearch().trim());
		}
		
	}

	/**
	 * obj 转换成字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {
		if (null == obj) {
			return "";
		}
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

	/**
	 * 将json写入response
	 * 
	 * @param response
	 * @param object
	 * @throws Exception
	 */
	public static void setResponseByAjax(HttpServletResponse response,
			Object object) throws Exception {
		response.setCharacterEncoding("utf-8");
		if (object instanceof String){
			response.getWriter().write((String)object);
		}else{
			// 响应JSON对象
			response.getWriter().write(JSONUtil.serialize(object));
		}

	}

	/**
	 * 将Map设置成JsTreeMap
	 * 
	 * @param list
	 * @param key
	 */
	public static void setJsTreeMap(HashMap map, String key, String value,
			List childrenList) {
		HashMap attrMap = new HashMap();
		attrMap.put("id", key);
		map.put("attr", attrMap);
		map.put("data", value);
		map.put("state", "");
		if (null != childrenList) {
			map.put("children", childrenList);
		}
	}
	
	/**
	 * 
	 * 根据树的层级关系把线性的数据转换成List树型结构
	 * 
	 * @param 
	 * 		List 线性数据List
	 * @return 
	 * 		List 树型结构List
	 */
	public static List<Map<String, Object>> getTreeList(List<Map<String, Object>> list,String key) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 取得第一个节点的层级
		double d = Double.parseDouble(list.get(0).get("level").toString());
		// 循环遍历整棵树
		while(!list.isEmpty()) {
			// 取得当前节点的层级
			double _d = Double.parseDouble(list.get(0).get("level").toString());
			// 当前节点的层级与第一个节点的层级不相同时
			if(d != _d) {
				// 当前节点是上一层级的子节点时
				if(d < _d) {
					// 递归调用取得上一层级最后一个节点的所有子节点，然后添加到该节点下
					resultList.get(resultList.size()-1).put(key, getTreeList(list,key));
				} else {// 当前节点不是上一层级的子节点时退出循环
					break;
				}
			} else { // 当前节点的层级与第一个节点的层级相同时
				// 在当前层级上添加一个节点
				addMapToList(list.get(0), resultList);
				//resultList.add(list.get(0));
				// 从树中删除该节点
				list.remove(0);
			}
		}
		return resultList;
	}
	
	public static void addMapToList(Map<String, Object> map, List<Map<String, Object>> list) {
		if(list == null || map == null || map.isEmpty()) {
			return;
		}
		if(list.isEmpty()) {
			list.add(map);
		} else {
			boolean exitMap = false;
			for(Map<String, Object> m: list) {
				if(m.get("path") != null && map.get("path") != null && m.get("path").toString().equals(map.get("path").toString())) {
					exitMap = true;
					break;
				}
			}
			if(!exitMap) {
				list.add(map);
			}
		}
	}
	
	/**
	 * 根据key将list分组
	 * @param list
	 * @param key
	 */
	public static List listGroup (List listAll,String key,String listKey){
		Map<String ,List> groupMap = new HashMap<String ,List>();  
		for (int i=0;i<listAll.size();i++){
			HashMap map = (HashMap)listAll.get(i);
			// 取得分组id
			String grpID = String.valueOf(map.get(key));
			if (groupMap.containsKey(grpID)){
				List listSingle = groupMap.get(grpID);  
				listSingle.add(map);
			}else{
				 List listSingle = new ArrayList();  //重新声明一个list
				 listSingle.add(map);
				 groupMap.put(grpID, listSingle);
			}
		}
		
		// 新建一个分组List
		List groupList = new ArrayList();
		// 遍历分组Map
		Iterator iter = groupMap.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object val = entry.getValue(); 
		    HashMap tmpMap = new HashMap();
		    tmpMap.put(listKey, val);
		    groupList.add(tmpMap);
		    
		} 
		
		Collections.reverse(groupList);
		return groupList;
	}
	
	
	/**
	 * 在最底层的节点中添加一个虚节点,用于上层父节点的展开
	 * @param list
	 */
	public static void addArtificialCounterDeep(List list){
		for (int i=0;i<list.size();i++){
			HashMap map = (HashMap)list.get(i);
			List childList = (List)map.get("nodes");
			if (childList !=null && !childList.isEmpty()){
				addArtificialCounterDeep(childList);
			}else{
				List artificialList = new ArrayList();
				HashMap artificialMap = new HashMap();
				artificialMap.put("name","artificialCounter");
				artificialList.add(artificialMap);
				map.put("nodes", artificialList);
			}
		}
	}
	
	/**
	 * 以二进制方式克隆对象  
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static Object byteClone(Object src) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(src);
		out.close();
		ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream in = new ObjectInputStream(bin);
		Object clone = in.readObject();
		in.close();
		return (clone);
	}   
	
	/**
	 * 用DTO2来覆盖DTO1
	 * 
	 * @param dto1 
	 * 			需要覆盖的DTO
	 * @param dto2
	 * 			用来覆盖的DTO
	 * @param flg 
	 * 			true:空值也覆盖   false:空值不覆盖
	 * @return
	 * 
	 * @throws Exception 
	 */
	public static <T> void convertDTO(T dto1, T dto2, boolean flg) throws Exception{
		if (null != dto1 && null != dto2) {
			Method[] mdArr2 = dto2.getClass().getMethods();
			for (Method md2 : mdArr2) {
				if (md2.getName().indexOf("get") == 0) {
					Object value = md2.invoke(dto2);
					if (!flg && (null == value || "".equals(value))) {
						continue;
					}
					Method[] mdArr1 = dto1.getClass().getMethods();
					String mdName = "set" + md2.getName().substring(3);
					for (Method md1 : mdArr1) {
						if (mdName.equals(md1.getName())) {
							md1.invoke(dto1, value);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 用DTO2来覆盖DTO1
	 * 
	 * @param dto1 
	 * 			需要覆盖的DTO
	 * @param dto2
	 * 			用来覆盖的DTO
	 * @param flg 
	 * 			true:空值也覆盖   false:空值不覆盖
	 * @return
	 * 
	 * @throws Exception 
	 */
	public static <T> void convertNewDTO(T dto1, T dto2, boolean flg) throws Exception{
		if (null != dto1 && null != dto2) {
			Method[] mdArr2 = dto2.getClass().getMethods();
			for (Method md2 : mdArr2) {
				Class[] ptypes = md2.getParameterTypes();
				if (md2.getName().indexOf("get") == 0 && null != ptypes && ptypes.length == 0) {
					Object value = md2.invoke(dto2);
					if (!flg && (null == value || "".equals(value))) {
						continue;
					}
					Method[] mdArr1 = dto1.getClass().getMethods();
					String mdName = "set" + md2.getName().substring(3);
					for (Method md1 : mdArr1) {
						if (mdName.equals(md1.getName())) {
							md1.invoke(dto1, value);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将bean转换为map
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> bean2Map(Object bean){
		try{
			if(null == bean){
				return null;
			}
			Class<? extends Object> entityClass = bean.getClass();
			Map<String, Object> returnMap = new HashMap<String, Object>();
			BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			if (null != propertyDescriptors) {
				for (int i = 0; i < propertyDescriptors.length; i++) {
					PropertyDescriptor descriptor = propertyDescriptors[i];
					String propertyName = descriptor.getName();
					if (!propertyName.equals("class")) {
						Method readMethod = descriptor.getReadMethod();
						Object result = readMethod.invoke(bean, new Object[0]);
						if (result != null) {
							returnMap.put(propertyName, result);
						} else {
							returnMap.put(propertyName, null);
						}
					}
				}
			}
		    return returnMap;  
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * map转换为bean
	 * @param type
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static <T> T  map2Bean(Class<T> entityClass, Map<String, Object> map) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(entityClass); // 获取类属性
		T obj = entityClass.newInstance(); // 创建 JavaBean 对象
		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		if(null != propertyDescriptors){
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if(map.containsKey(propertyName)){//设置属性值
					try{
						descriptor.getWriteMethod().invoke(obj, map.get(propertyName));
					}catch (Exception e) {
						throw e;
					}
				}
			}
		}
		return obj;
	} 
	
	/**
	 * 获取工作流名称(拼接上组织和品牌代码)
	 * 
	 * @param orgCode 组织代码
	 * @param brandCode 品牌代码
	 * @param name 工作流名称(无组织品牌代码)
	 * @return String 转换后的工作流名称
	 * @throws Exception 
	 */
	public static String getWfName(String orgCode, String brandCode, String name) throws Exception{
		if (CherryChecker.isNullOrEmpty(orgCode)) {
			// 组织全体共通
			orgCode = String.valueOf(CherryConstants.ORG_CODE_ALL);
		}
		if (CherryChecker.isNullOrEmpty(brandCode)) {
			// 品牌全体共通
			brandCode = String.valueOf(CherryConstants.Brand_CODE_ALL);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OC", orgCode);
		map.put("BC", brandCode);
		map.put("FC", name);
		String wfName = JSONUtil.serialize(map);
		return wfName;
	}
	
	/**
	 * <p>
	 * 把经过排序的List转换成层级List(支持多层转换)
	 * </p>
	 *  
	 * @param List
	 *          	转换前List
	 * @param List
	 *          	转换后List
	 * @param List
	 *          	多层key
	 * @param int
	 *          	层级      
	 * @return 无
	 * 
	 * @throws 无
	 */
	public static void convertSortedList2DeepList(List<Map<String, Object>> list,
			List<Map<String, Object>> rootList, List<String[]> keysList,
			int deep) {
		if (list != null && rootList != null && keysList != null
				&& keysList.size() > deep) {
			String[] keys = keysList.get(deep);
			String id = null;
			if (keys != null && keys.length > 0) {
				id = keys[0];
			}
			Object preId = null;
			List<Map<String,Object>> detailList = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.putAll(list.get(i));
				boolean isNotEqual = true;
				Object currentId = tempMap.get(id);
				if(i == 0) {
					isNotEqual = true;
				} else {
					if(preId != null) {
						if(currentId != null) {
							if(preId.toString().equals(currentId.toString())) {
								isNotEqual = false;
							} else {
								isNotEqual = true;
							}
						} else {
							isNotEqual = true;
						}
					} else {
						if(currentId != null) {
							isNotEqual = true;
						} else {
							isNotEqual = false;
						}
					}
				}
				if (isNotEqual) {
					Map<String, Object> mainMap = new HashMap<String, Object>();
					detailList = new ArrayList<Map<String, Object>>();
					Map<String, Object> detailMap = new HashMap<String, Object>();
					for(String key : keys) {
						mainMap.put(key, tempMap.get(key));
						tempMap.remove(key);
					}
					detailMap.putAll(tempMap);
					detailList.add(detailMap);
					mainMap.put("list", detailList);
					rootList.add(mainMap);
					preId = currentId;
				} else {
					Map<String, Object> detailMap = new HashMap<String, Object>();
					for(String key : keys) {
						tempMap.remove(key);
					}
					detailMap.putAll(tempMap);
					detailList.add(detailMap);
				}
			}
			if (deep < keysList.size() - 1) {
				deep++;
				for (Map<String, Object> rootMap : rootList) {
					List<Map<String, Object>> deepList = (List<Map<String, Object>>) rootMap.get("list");
					List<Map<String, Object>> deepListNew = new ArrayList<Map<String, Object>>();
					rootMap.put("list", deepListNew);
					convertSortedList2DeepList(deepList, deepListNew, keysList, deep);
				}
			}
		}
	}
	public static float getFloat(Object obj){
		float res = 0;
		if(obj instanceof Integer){
			res = (Integer)obj;
		}else if(obj instanceof Float){
			res = (Float)obj;
		}else if(obj instanceof Number){
			Number num = (Number)obj;
			res = num.floatValue();
		}else{
			try{
				res= Float.parseFloat(getString(obj));
			}catch(Exception e){}
		}
		return res;
	}
	
	/**
	 * obj 转换成整形
	 * @param obj
	 * @return
	 * 
	 * */
	public static int getInt(Object obj){
		//对象为空返回0
		if(null==obj){
			return 0;
		}
		//对像为Integer类型直接返回
		if(obj instanceof Integer){
			return (Integer)obj;
		}
		//对象为String类型
		else if(obj instanceof String){
			try{
				return Integer.parseInt(String.valueOf(obj));
			}catch(Exception e){
				return 0;
			}
		}else if(obj instanceof Number){
			Number num = (Number)obj;
			return num.intValue();
		}else{
			return 0;
		}
	}

	/**
	 * JSON字符串转List
	 * @param json
	 * @return
	 */
	public static List<Map<String, Object>> json2List(String json){
		List<Map<String, Object>> list = null;
		if(null != json && !"".equals(json.trim())){
			try {
				list = (List<Map<String, Object>>)JSONUtil.deserialize(json);
			} catch (JSONException e) {
			}
		}
		return list;
	}

	/**
	 * JSON字符串转Map
	 * @param json
	 * @return
	 */
	public static Map<String, Object> json2Map(String json){
		Map<String, Object> map = null;
		if(null != json && !"".equals(json.trim())){
			try {
				map = (Map<String, Object>)JSONUtil.deserialize(json);
			} catch (JSONException e) {
			}
		}
		return map;
	}
	/**
	 * map转list
	 * @param map
	 * @return
	 */
	public static List<Map<String, Object>> map2List(Map<String, Object> map){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(null != map){
			for(Map.Entry<String,Object> en: map.entrySet()){
				Map<String, Object> temp = (Map<String, Object>)en.getValue();
				list.add(new HashMap<String,Object>(temp));
			}
		}
		return list;
	}
}
