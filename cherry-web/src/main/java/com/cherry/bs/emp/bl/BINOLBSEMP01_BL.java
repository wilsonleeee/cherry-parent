/*		
 * @(#)BINOLBSEMP01_BL.java     1.0 2010/10/12		
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
package com.cherry.bs.emp.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.service.BINOLBSEMP01_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工管理 BL
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 */
public class BINOLBSEMP01_BL {

	@Resource(name="binOLBSEMP01_Service")
	private BINOLBSEMP01_Service binolbsemp01Service;
	
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	// Excel导出列数组
	private final static String[][] proArray = {
		
		{ "employeeCode", "employee.employeeCode", "15", "", "" },
		
		{ "employeeName", "employee.employeeName", "12", "", "" },
		
		{ "departNameEX", "employee.section", "18", "", "" },
		
		{ "categoryName", "employee.post", "12", "", "" },
		
		{ "longinName", "employee.longinName", "12", "", "" },
		
		{ "mobilePhone", "employee.mobilePhone", "12", "", "" },
		
		{ "email", "employee.email", "12", "", "" },
		
		{ "ValidFlag", "employee.flag", "12", "", "1137" }
	};
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "makeDate", "startDate",
				"endDate" };
		
	/**
	 * 取得员工总数
	 * 
	 * @param map
	 * @return
	 */
	public int getEmpCount(Map<String, Object> map) {

		return binolbsemp01Service.getEmpCount(map);
	}

	/**
	 * 取得员工信息List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchEmployeeInfo(Map<String, Object> map) throws Exception {

		// 取得员工信息List
		List<Map<String, Object>> employeeList = binolbsemp01Service.getEmployeeList(map);
		// 解密其中的手机与邮箱信息
		decryptListData(employeeList);
		return employeeList;
	}
	
	/**
	 * 取得直属下级雇员List
	 * 
	 * @param map 检索条件
	 * @return 雇员List
	 */
	public String getNextEmployeeList(Map<String, Object> map) throws Exception {
		
		// 部门节点位置
		String path = (String)map.get(CherryConstants.PATH);
		List<Map<String, Object>> list =  new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> otherList =  new ArrayList<Map<String, Object>>();
		if(path == null || "".equals(path) || "-9".equals(path)) {
			// 取得用户权限中的所有岗位级别信息List
			List<Map<String, Object>> posCategoryList = binolbsemp01Service.getPosCategoryList(map);
			if(posCategoryList != null && !posCategoryList.isEmpty()) {
				List<Map<String, Object>> firstPosCategoryList = new ArrayList<Map<String,Object>>();
				List<Map<String, Object>> otherPosCategoryList = new ArrayList<Map<String,Object>>();
				int grade = (Integer)posCategoryList.get(0).get("grade");
				for(int i = 0; i < posCategoryList.size(); i++) {
					if(grade == (Integer)posCategoryList.get(i).get("grade")) {
						firstPosCategoryList.add(posCategoryList.get(i));
					} else {
						otherPosCategoryList.add(posCategoryList.get(i));
					}
				}
				map.put("firstPosCategoryList", firstPosCategoryList);
				map.put("otherPosCategoryList", otherPosCategoryList);
				if(path == null || "".equals(path)) {
					// 取得顶层雇员List
					List<Map<String, Object>> firstEmployeeList = binolbsemp01Service.getFirstEmployeeList(map);
					if(firstEmployeeList != null && !firstEmployeeList.isEmpty()) {
						int levelFirst = 0;
						for(int i = 0; i < firstEmployeeList.size(); i++) {
							int level = (Integer)firstEmployeeList.get(i).get("level");
							if(i > 0) {
								if(level != levelFirst) {
									break;
								}
							} else {
								levelFirst = level;
							}
							list.add(firstEmployeeList.get(i));
						}
					}
				} else {
					// 取得未知节点的下级雇员List
					list = binolbsemp01Service.getRootNextEmpByplList(map);
				}
			}
		} else {
			// 取得直属下级雇员List
			list = binolbsemp01Service.getNextEmployeeList(map);
		}
//		List<Map<String, Object>> employeeList =  new ArrayList<Map<String, Object>>();
//		List<String> hasLowerEmpList = new ArrayList<String>();
//		employeeList.addAll(list);
//		employeeList.addAll(otherList);
//		if(employeeList != null && !employeeList.isEmpty()) {
//			map.put("employeeList", employeeList);
//			// 取得存在下级节点的员工List
//			hasLowerEmpList = binolbsemp01Service.getHasLowerEmpList(map);
//		}
		StringBuffer jsonTree = new StringBuffer();
		StringBuffer jsonSubTree = new StringBuffer();
		if(otherList != null && !otherList.isEmpty()) {
			for(int i = 0; i < otherList.size(); i++) {
				Map<String, Object> employeeMap = otherList.get(i);
				String employeeId = String.valueOf(employeeMap.get("employeeId"));
				String validFlag = (String)employeeMap.get("validFlag");
				int childCount = (Integer)employeeMap.get("childCount");
				String childEmployeeId = String.valueOf(employeeMap.get("childEmployeeId"));
				// 是否为无效员工
				String className = "";
				// 无效员工的场合
				if("0".equals(validFlag)) {
					className = "jstree-disable";
				}
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(childCount > 1 || !employeeId.equals(childEmployeeId)) {
					child = "closed";
				}
				String employeeName = "(" + (String)employeeMap.get("employeeCode") + ")" + (String)employeeMap.get("employeeName");
				employeeName = JSONUtil.serialize(employeeName);
				// 树结构作成
				jsonSubTree.append("{\"data\":{\"title\":"+employeeName+",\"attr\":{\"id\":\""+employeeId+"\", \"class\":\""+className+"\"}},\"attr\":{\"id\":\""+"node_"+employeeId+"\"},\"state\":\""+child+"\"},");
			}
			jsonSubTree = jsonSubTree.deleteCharAt(jsonSubTree.length()-1);
		}
		// 把取得的雇员List转换成树结构的字符串
		if(list != null && !list.isEmpty()) {
			jsonTree.append("[");
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> employeeMap = list.get(i);
				String employeeId = String.valueOf(employeeMap.get("employeeId"));
				String validFlag = (String)employeeMap.get("validFlag");
				int childCount = (Integer)employeeMap.get("childCount");
				String childEmployeeId = String.valueOf(employeeMap.get("childEmployeeId"));
				// 是否为无效员工
				String className = "";
				// 无效员工的场合
				if("0".equals(validFlag)) {
					className = "jstree-disable";
				}
				// 是否有子节点
				String child = "";
				// 有子节点的场合
				if(childCount > 1 || !employeeId.equals(childEmployeeId)) {
					child = "closed";
				}
				String employeeName = "(" + (String)employeeMap.get("employeeCode") + ")" + (String)employeeMap.get("employeeName");
				employeeName = JSONUtil.serialize(employeeName);
				// 树结构作成
				jsonTree.append("{\"data\":{\"title\":"+employeeName+",\"attr\":{\"id\":\""+employeeId+"\", \"class\":\""+className+"\"}},\"attr\":{\"id\":\""+"node_"+employeeId+"\"},\"state\":\""+child+"\"},");
			}
			if(path == null || "".equals(path)) {
				jsonTree.append("{\"data\":{\"title\":\""+CherryConstants.UNKNOWN_DEPARTNAME+"\",\"attr\":{\"id\":\"-9\",\"class\":\"\"}},\"attr\":{\"id\":\""+"node_-9\"},\"state\":\"closed\"}");
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
	
	public List<String> getLocationHigher(Map<String, Object> map) {
		
		List<String> resultList = new ArrayList<String>();
		String employeeId = binolbsemp01Service.getLocationEmployeeId(map);
		if(employeeId != null) {
			map.put("employeeId", employeeId);
			List<String> locationHigher = binolbsemp01Service.getLocationHigher(map);
			if(locationHigher != null) {
				for(int i = 0; i < locationHigher.size(); i++) {
					resultList.add("#node_"+locationHigher.get(i));
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
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLBSEMP01",
				language, "downloadFileName")
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
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
		List<Map<String, Object>> dataList = binolbsemp01Service
				.getEmployeeExcelList(map);
		// 解密其中的手机与邮箱信息
		decryptListData(dataList);
		
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLBSEMP01");
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
		condition.append(binOLMOCOM01_BL.getResourceValue("BINOLBSEMP01",
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
	
	/**
	 * 解密指定的人员信息List
	 * 
	 * @param employeeList
	 * @throws Exception
	 */
	private void decryptListData(List<Map<String, Object>> employeeList)
			throws Exception {
		if (null != employeeList && employeeList.size() > 0) {
			for (Map<String, Object> employeeInfo : employeeList) {
				// 电话、email解密显示
				if (!CherryChecker.isNullOrEmpty(
						employeeInfo.get("mobilePhone"), true)) {
					employeeInfo.put("mobilePhone",
							CherrySecret.decryptData(ConvertUtil
									.getString(employeeInfo.get("brandCode")),
									ConvertUtil.getString(employeeInfo
											.get("mobilePhone"))));
				}
				if (!CherryChecker.isNullOrEmpty(employeeInfo.get("email"),
						true)) {
					employeeInfo.put("email",
							CherrySecret.decryptData(ConvertUtil
									.getString(employeeInfo.get("brandCode")),
									ConvertUtil.getString(employeeInfo
											.get("email"))));
				}
			}
		}
	}
	
	/**
	 * 取得系统年月日
	 * @return 
	 * @throws Exception
	 */
	public String getDateYMD() throws Exception {
		return binolbsemp01Service.getDateYMD();
	}
}
