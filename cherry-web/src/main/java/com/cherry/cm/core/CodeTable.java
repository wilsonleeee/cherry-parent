/*
 * @(#)CodeTable.java     1.0 2010/11/16
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
package com.cherry.cm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * CodeTable
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.16
 */
@SuppressWarnings("unchecked")
public class CodeTable {

	/** code值一览 */
	private Map codesMap;

	public Map getCodesMap() {
		return codesMap;
	}

	public synchronized void setCodesMap(List codesList) {
		this.codesMap = new HashMap();
		if (codesList != null) {
			String preKey = null;
			int fromIndex = 0;
			int flg = 0;
			for (int i = 0; i < codesList.size(); i++) {
				Map codeMap = (Map) codesList.get(i);
				// 组织Code
				Object orgCode = codeMap
						.get(CherryConstants.ORG_CODE);
				// 品牌Code
				Object brandCode = codeMap
						.get(CherryConstants.BRAND_CODE);
				// Code类别
				String codeTypeStr = (String) codeMap.get("codeType");
				// 组合key
				String key = orgCode + "_" + brandCode + "_"
						+ codeTypeStr.trim();
				if (!key.equals(preKey)) {
					flg++;
					if (flg > 1) {
						this.codesMap.put(preKey, codesList.subList(fromIndex,
								i));
					}
					fromIndex = i;
					preKey = key;
				}
				if (i == codesList.size() - 1) {
					this.codesMap.put(key, codesList.subList(fromIndex,
							codesList.size()));
				}
			}
		}
	}

	/**
	 * <p>
	 * 取得对应的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别        
	 * @return List 记录集
	 * 
	 */
	public List getCodes(String codeType) {
		List codes = new ArrayList();
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				// 语言类型
				String language = (String) session
						.getAttribute(CherryConstants.SESSION_CHERRY_LANGUAGE);
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					Map code = new HashMap();
					// 值
					String value = null;
					if (CherryConstants.SESSION_LANGUAGE_CN.equals(language)) {
						value = (String) codeMap.get("value1");
					} else if (CherryConstants.SESSION_LANGUAGE_EN
							.equals(language)) {
						value = (String) codeMap.get("value2");
					} else {
						value = (String) codeMap.get("value3");
					}
					// Key
					String codeKey = (String) codeMap.get("codeKey");
					code.put("CodeKey", codeKey == null ? "" : codeKey);
					// 值
					code.put("Value", value);
					// 级别
					code.put("Grade", codeMap.get("grade"));
					codes.add(code);
				}
			}
		}
		return codes;
	}
	
	/**
	 * <p>
	 * 取得对应的记录(需要组织代号和品牌代号)
	 * </p>
	 * 
	 * @param codeType
	 *            Code类别
	 * @param orgCode
	 *            组织代号
	 * @param brandCode
	 *            品牌代号
	 * @return List 记录集
	 * 
	 */
	public List getCodesByBrand(String codeType, String orgCode, String brandCode) {
		List codes = new ArrayList();
		List codeList = getCodeListByBrand(codeType, orgCode, brandCode);
		if (codeList != null) {
			for (int i = 0; i < codeList.size(); i++) {
				Map codeMap = (Map) codeList.get(i);
				Map code = new HashMap();
				// 值
				String value = (String) codeMap.get("value1");
				// Key
				String codeKey = (String) codeMap.get("codeKey");
				code.put("CodeKey", codeKey == null ? "" : codeKey);
				// 值
				code.put("Value", value);
				// 级别
				code.put("Grade", codeMap.get("grade"));
				codes.add(code);
			}
		}
		return codes;
	}
	
	/**
	 * <p>
	 * 取得对应的值(需要组织代号和品牌代号)
	 * </p>
	 * 
	 * @param codeType
	 *            Code类别
	 * @param key
	 *            key         
	 * @param orgCode
	 *            组织代号
	 * @param brandCode
	 *            品牌代号
	 * @return String 值
	 * 
	 */
	public String getValueByKey(String codeType, String key, String orgCode, String brandCode) {
		List codes = getCodesByBrand(codeType, orgCode, brandCode);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				Map codeMap = (Map) codes.get(i);
				if (key.equals(codeMap.get("CodeKey"))) {
					return String.valueOf(codeMap.get("Value"));
				}
			}
		}
		return "";
	}
	
	/**
	 * <p>
	 * 取得对应的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别        
	 * @return List 记录集
	 * 
	 */
	public List getCodesWithFilter(String codeType, String key) {
		String[] keys = key.split("\\|");
		List codes = new ArrayList();
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				// 语言类型
				String language = (String) session
						.getAttribute(CherryConstants.SESSION_CHERRY_LANGUAGE);
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					// Key
					String codeKey = (String) codeMap.get("codeKey");
					if(codeKey != null && ConvertUtil.isContain(keys, codeKey)) {
						continue;
					}
					Map code = new HashMap();
					// 值
					String value = null;
					if (CherryConstants.SESSION_LANGUAGE_CN.equals(language)) {
						value = (String) codeMap.get("value1");
					} else if (CherryConstants.SESSION_LANGUAGE_EN
							.equals(language)) {
						value = (String) codeMap.get("value2");
					} else {
						value = (String) codeMap.get("value3");
					}
					code.put("CodeKey", codeKey == null ? "" : codeKey);
					// 值
					code.put("Value", value);
					// 级别
					code.put("Grade", codeMap.get("grade"));
					codes.add(code);
				}
			}
		}
		return codes;
	}

	/**
	 * <p>
	 * 取得对应的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param String
	 *            key
	 * @return String 值
	 * 
	 */
	public List getCodesByGrade(String codeType) {
		// codeList
		List list = getCodes(codeType);
		Collections.sort(list, new CherryComparator());
		return list;
	}
	
	/**
	 * list 比较器
	 * @author lipc
	 *
	 */
	private  class CherryComparator implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			Map<String, Object> map1 = (Map<String, Object>)o1;
			Map<String, Object> map2 = (Map<String, Object>)o2;
			int temp1 = CherryUtil.obj2int(map1.get("Grade"));
			int temp2 = CherryUtil.obj2int(map2.get("Grade"));
			if(temp1 > temp2){
				return 1;
			}else{
				return 0;
			}
		}
	}
	/**
	 * <p>
	 * 根据key取得对应的值
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param String
	 *            key
	 * @return String 值
	 * 
	 */
	public String getVal(String codeType, Object key) {
		String keyStr = "";
		if (key != null) {
			keyStr = String.valueOf(key).trim();
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				// 语言类型
				String language = (String) session
						.getAttribute(CherryConstants.SESSION_CHERRY_LANGUAGE);
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					// Key
					String codeKey = (String) codeMap.get("codeKey");
					if (codeKey != null && codeKey.equals(keyStr) ||
							(codeKey == null || "".equals(codeKey)) && "".equals(keyStr)) {
						// 值
						String value = null;
						if (CherryConstants.SESSION_LANGUAGE_CN
								.equals(language)) {
							value = (String) codeMap.get("value1");
						} else if (CherryConstants.SESSION_LANGUAGE_EN
								.equals(language)) {
							value = (String) codeMap.get("value2");
						} else {
							value = (String) codeMap.get("value3");
						}
						return value;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据key取得对应的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param String
	 *            key
	 * @return Map map
	 * 
	 */
	public Map getCode(String codeType, Object key) {
		String keyStr = "";
		if (key != null) {
			keyStr = String.valueOf(key).trim();
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					// Key
					String codeKey = (String) codeMap.get("codeKey");
					if (codeKey != null && codeKey.equals(keyStr) ||
							(codeKey == null || "".equals(codeKey)) && "".equals(keyStr)) {
						return codeMap;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 *  根据value取得对应的key
	 * @param codeType  Code类别
	 * @param val  code值
	 * @return
	 */
	public String getCodeKey(String codeType, Object val) {
		String valStr = "";
		if (val != null) {
			valStr = String.valueOf(val).trim();
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
//					String language = (String) session.getAttribute(CherryConstants.SESSION_LANGUAGE);
					
					String value1 = ConvertUtil.getString(codeMap.get("value1"));
					String value2 = ConvertUtil.getString((String) codeMap.get("value2"));
					String value3 = ConvertUtil.getString((String) codeMap.get("value3"));
					
					if( (!"".equals(value1) && value1.equals(valStr))
					  || (!"".equals(value2) && value2.equals(valStr))
					  || (!"".equals(value3) && value3.equals(valStr))
					){
						return ConvertUtil.getString(codeMap.get("codeKey"));
					}
				}
			}
		}
		return null;
	}
	
	/**
	 *  根据value模糊查询对应的key
	 * @param codeType  Code类别
	 * @param val  code值
	 * @return
	 */
	public List<Map<String,Object>> getLikeCodes(String codeType, Object val) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList2 = new ArrayList<Map<String,Object>>();
		String valStr = "";
		if (val != null) {
			valStr = String.valueOf(val).trim();
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
//					String language = (String) session.getAttribute(CherryConstants.SESSION_LANGUAGE);
					
					String value1 = ConvertUtil.getString(codeMap.get("value1"));
					String value2 = ConvertUtil.getString((String) codeMap.get("value2"));
					String value3 = ConvertUtil.getString((String) codeMap.get("value3"));
					
					if( (!"".equals(value1) && value1.indexOf(valStr) != -1)
							|| (!"".equals(value2) && value2.indexOf(valStr)!= -1)
							|| (!"".equals(value3) && value3.indexOf(valStr)!= -1)
							){
						//return ConvertUtil.getString(codeMap.get("codeKey"));
						resultList.add(codeMap);
					}
				}
			}
		}
		return resultList;
	}
	
	/**
	 * <p>
	 * 根据key取得对应的级别
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param String
	 *            key
	 * @return String 级别
	 * 
	 */
	public String propGrade(String codeType, Object key) {
		String keyStr = "";
		if (key != null) {
			keyStr = String.valueOf(key).trim();
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					// Key
					String codeKey = (String) codeMap.get("codeKey");
					if (codeKey != null && codeKey.equals(keyStr) ||
							(codeKey == null || "".equals(codeKey)) && "".equals(keyStr)) {
						if(codeMap.get("grade") != null) {
							// 级别
							return codeMap.get("grade").toString();
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据级别取得对应的Key
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param Object
	 *            grade
	 * @return String Key
	 * 
	 */
	public String propKeyByGrade(String codeType, Object grade) {
		if (grade == null) {
			return null;
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					if(grade.equals(codeMap.get("grade"))) {
						String codeKey = null;
						// Key
						Object codeKeyObj = codeMap.get("codeKey");
						if (null != codeKeyObj) {
							codeKey = String.valueOf(codeKeyObj);
						}
						return codeKey;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据级别取得对应的名称
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param Object
	 *            grade
	 * @return String 名称
	 * 
	 */
	public String propValByGrade(String codeType, Object grade) {
		if (grade == null) {
			return null;
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session != null) {
			List codeList = getCodeList(codeType, session);
			if (codeList != null) {
				// 语言类型
				String language = (String) session
						.getAttribute(CherryConstants.SESSION_CHERRY_LANGUAGE);
				for (int i = 0; i < codeList.size(); i++) {
					Map codeMap = (Map) codeList.get(i);
					if(grade.equals(codeMap.get("grade"))) {
						// 名称
						String value = null;
						if (CherryConstants.SESSION_LANGUAGE_CN
								.equals(language)) {
							value = (String) codeMap.get("value1");
						} else if (CherryConstants.SESSION_LANGUAGE_EN
								.equals(language)) {
							value = (String) codeMap.get("value2");
						} else {
							value = (String) codeMap.get("value3");
						}
						return value;
					}
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * 根据Code类别取得对应的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param HttpSession
	 *            session
	 * @return List 记录集
	 * 
	 */
	private List getCodeList(String codeType, HttpSession session) {
		if (this.codesMap != null) {
			if (session != null) {
				// 用户信息
				UserInfo userInfo = (UserInfo) session
						.getAttribute(CherryConstants.SESSION_USERINFO);
				// 组织Code
				String orgCode = userInfo.getOrganizationInfoCode();
				// 品牌Code
				String brandCode = userInfo.getBrandCode();
				// 组合key
				String combKey = orgCode + "_" + brandCode + "_" + codeType;
				if (!this.codesMap.containsKey(combKey)) {
					// 组合key(品牌Code为全体共通)
					combKey = orgCode + "_"
							+ CherryConstants.Brand_CODE_ALL + "_" + codeType;
					if (!this.codesMap.containsKey(combKey)) {
						// 组合key(组织Code和品牌Code为全体共通)
						combKey = CherryConstants.ORG_CODE_ALL + "_"
								+ CherryConstants.Brand_CODE_ALL + "_" + codeType;
					}
				}
				return (List) this.codesMap.get(combKey);
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据Code类别取得对应的记录(需要组织代号和品牌代号)
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param orgCode
	 *            组织代号
	 * @param brandCode
	 *            品牌代号
	 * @return List 记录集
	 * 
	 */
	private List getCodeListByBrand(String codeType, String orgCode, String brandCode) {
		if (this.codesMap != null) {
				// 组合key
				String combKey = orgCode + "_" + brandCode + "_" + codeType;
				if (!this.codesMap.containsKey(combKey)) {
					// 组合key(品牌Code为全体共通)
					combKey = orgCode + "_"
							+ CherryConstants.Brand_CODE_ALL + "_" + codeType;
					if (!this.codesMap.containsKey(combKey)) {
						// 组合key(组织Code和品牌Code为全体共通)
						combKey = CherryConstants.ORG_CODE_ALL + "_"
								+ CherryConstants.Brand_CODE_ALL + "_" + codeType;
					}
				}
				return (List) this.codesMap.get(combKey);
		}
		return null;
	}

	/**
	 * <p>
	 * 页面上的属性对应的值 (默认值为空字符串)
	 * </p>
	 * 
	 * @param String
	 *            Code类别
	 * @param String
	 *            key
	 * @return String 值
	 * 
	 */
	public String propVal(String codeType, Object key) {
		// 值
		String value = getVal(codeType, key);
		return (value == null || "".equals(value)) ? "&nbsp;" : value;
	}
	
	/**
	 * <p>
	 * 根据key取得下级code信息(按照grade比较所得)
	 * </p>
	 * 
	 * @param String
	 * 			codeType
	 * 
	 * @param Object
	 * 			codeKey
	 * 
	 * @return List
	 * 
	 * */
	public List getSubCodesByKey(String codeType, Object key){
		//存放返回结果
		List resultList = new ArrayList();
		
		//取得该codeType下的所有code值
		List<Map> codesList = this.getCodes(codeType);
		//取得该key对应的code值得等级
		String gradeStr = this.propGrade(codeType, key);
		//将String类型的等级转换成int型
		int grade = ConvertUtil.getInt(gradeStr);
		
		for(Map codeMap : codesList){
			int _thisGrade = ConvertUtil.getInt(codeMap.get("Grade"));
			if(_thisGrade > grade){
				resultList.add(codeMap);
			}
		}
		return resultList;
	}
	
	/**
	 * <p>
	 * 根据key取得上级code信息(按照grade比较所得)
	 * </p>
	 * 
	 * @param String
	 * 			codeType
	 * 
	 * @param Object
	 * 			codeKey
	 * 
	 * @return List
	 * 
	 * */
	public List getSupCodesByKey(String codeType, Object key){
		//存放返回结果
		List resultList = new ArrayList();
		
		//取得该codeType下的所有code值
		List<Map> codesList = getAllByCodeType(codeType);
		//取得该key对应的code值得等级
		String gradeStr = this.propGrade(codeType, key);
		//将String类型的等级转换成int型
		int grade = ConvertUtil.getInt(gradeStr);
		
		for(Map codeMap : codesList){
			int _thisGrade = ConvertUtil.getInt(codeMap.get("grade"));
			//级别指越小，等级越高
			if(_thisGrade <= grade){
				resultList.add(codeMap);
			}
		}
		return resultList;
	}
	
	/**
	 * <p>
	 * 取得指定索引范围内的记录
	 * </p>
	 * 
	 * @param String
	 *            Code类别        
	 * @return List 记录集
	 * 
	 */
	public List getCodes(String codeType, Object fromIndex, Object toIndex) {
		int fromIndexNum = 0, toIndexNum = 0;
		// 存放返回列表
		List resultList = new ArrayList();
		if (fromIndex != null) {
			fromIndexNum = ConvertUtil.getInt(fromIndex);
		}
		if (toIndex != null) {
			toIndexNum = ConvertUtil.getInt(toIndex);
		}
		List codes = getCodes(codeType);
		for (int i = 0; i < codes.size(); i++) {
			if(i >= fromIndexNum && i <= toIndexNum){
				Map codeMap = (Map) codes.get(i);
				resultList.add(codeMap);
			}
		}
		return resultList;
	}

	/**
	 * 查询所有信息
	 */
	public List getAllByCodeType(String codeType) {
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		return (getCodeList(codeType, session));
	}
	

}