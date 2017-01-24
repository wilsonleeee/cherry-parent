/*		
 * @(#)BINOLCM00_BL.java     1.0 2010/10/12		
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 共通BL
 * 
 * @author lipc
 * 
 */
public class BINOLCM00_BL {

	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binolcm00Service;

	/**
	 * 取得区域List
	 * 
	 * @param map
	 *            (所属品牌,语言)
	 * @return
	 */
	public List<Map<String, Object>> getReginList(Map<String, Object> map) {
		return ConvertUtil.convertList2HierarchyList(binolcm00Service
				.getProvinceList(map), "provinceList", "reginId", "reginName");
	}

	/**
	 * 取得regionId下属区域List
	 * 
	 * @param map
	 *            (区域ID,语言)
	 * @return
	 */
	public List<Map<String, Object>> getChildRegionList(Map<String, Object> map) {

		return binolcm00Service.getChildRegionList(map);
	}

	/**
	 * 取得登陆用户
	 * 
	 * @param map
	 *            
	 * @return
	 */
	public Map<String, Object> UserDetail(Map<String, Object> map) {
		return binolcm00Service.UserDetail(map);
	}

	/**
	 * 取得岗位List
	 * 
	 * @param map
	 *            (部门ID,语言)
	 * @return
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {

		return binolcm00Service.getPositionList(map);
	}

	/**
	 * 取得部门类型List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 部门类型List
	 */
	public List<Map<String, Object>> getDepartTypeList(Map<String, Object> map) {
		return binolcm00Service.getDepartTypeList(map);
	}

	/**
	 * 取得权限部门List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		return binolcm00Service.getDepartList(map);
	}

	/**
	 * 取得部门List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getOrgList(Map<String, Object> map) {
		return binolcm00Service.getOrgList(map);
	}

	/**
	 * 取得实体仓库List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 实体仓库List
	 */
	public List<Map<String, Object>> getInventoryList(Map<String, Object> map) {
		return binolcm00Service.getInventoryList(map);
	}

	/**
	 * 查询假日
	 * 
	 * @param map
	 * @return String
	 * @exception JSONException
	 * 
	 */
	public String getHolidays(Map<String, Object> map) throws JSONException {
		List<Map<String, Object>> list = binolcm00Service.getHolidays(map);
		if (list != null && !list.isEmpty()) {
			return JSONUtil.serialize(list);
		}
		return null;
	}

	/**
	 * 取得给定自然日的财务月第一天对应的自然日
	 * 
	 * @param orgInfoId
	 *            组织ID
	 * @param date
	 *            日期
	 * @return String
	 */
	public String getFiscalDate(int orgInfoId, Date date) {
		return binolcm00Service.getFiscalDate(orgInfoId, date);
	}

	/**
	 * 取得给定自然日对应最近的库存历史截止时间
	 * 
	 * @param orgInfoId
	 *            组织ID
	 * @param date
	 *            日期
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCutOfDate(String flag, int orgInfoId, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
		map.put(CherryConstants.DATE, date);
		return (Map<String, Object>) binolcm00Service.getCutOfDate(map,flag);
	}
	
	/**
	 * 取得渠道List
	 * 
	 * @param map 查询条件
	 * @return 渠道List
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) {
		
		return binolcm00Service.getChannelList(map);
	}
	
	/**
	 * 取得商场List
	 * 
	 * @return 商场List
	 */
	public List<Map<String, Object>> getMallInfoList() {
		
		return binolcm00Service.getMallInfoList();
	}
	
	/**
	 * 取得商场List
	 * 
	 * @return 商场List
	 */
	public List<Map<String, Object>> getMallInfoList(Map<String, Object> paramMap){
		return binolcm00Service.getMallInfoList(paramMap);
	}
	/**
	 * 取得经销商List
	 * 
	 * @param map 查询条件
	 * @return 经销商List
	 */
	public List<Map<String, Object>> getResellerInfoList(Map<String, Object> map) {

		return binolcm00Service.getResellerInfoList(map);
	}
	
	/**
	 * 取得柜台主管List
	 * 
	 * @param map 查询条件
	 * @return 柜台主管List
	 */
	public List<Map<String, Object>> getCounterPositionList(Map<String, Object> map) {

		return binolcm00Service.getCounterPositionList(map);
	}
	
	/**
	 * 根据员工code/员工姓名查询员工信息
	 * 
	 * */
	public List<Map<String,Object>>getEmplyessInfo(Map<String,Object> map){
		return binolcm00Service.getEmplyessInfo(map);
	}
	
	/**
	 * 库存相关的日期参数设置
	 * 
	 * @param map
	 * @param orgInfoId
	 * @param startDate
	 * @param endDate
	 * @param flag ["Pro":产品,"Prm":促销品]
	 */
	public void setParamsMap(Map<String, Object> map, int orgInfoId,
			String startDate, String endDate,String flag) {
		// 取得开始日期的前一天
		String startDate1 = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN,
				startDate, -1);
		String twoMonthBeforeCutDay = "2010-01-01";
		// 取得月度库存截止日期
		Map<String, Object> map1 = getCutOfDate(flag,orgInfoId, startDate1);
		Map<String, Object> map2 = getCutOfDate(flag,orgInfoId, endDate);
		// 月度库存历史不为null
		if (null != map1 && null != map2) {
			int days1 = (Integer) map1.get("days");
			int days2 = (Integer) map2.get("days");
			String cutOfDate1 = (String) map1.get("cutOfDate");
			String cutOfDate2 = (String) map2.get("cutOfDate");
			if (days1 <= days2) {
				if (CherryChecker.compareDate(cutOfDate1, startDate1) <= 0) {
					// 月度库存统计日期在集结点前
					map.put("flag", 1);
					map.put("date1", cutOfDate1);
					map.put("date2", startDate1);
				} else {
					// 月度库存统计日期在集结点后
					map.put("flag", -1);
					map.put("date1", startDate1);
					map.put("date2", cutOfDate1);
				}
				// 月度库存统计日期
				map.put("cutOfDate", cutOfDate1);
				//计算月度库存统计日期的前两个月的日期
				twoMonthBeforeCutDay = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN,cutOfDate1,-2);
			} else {
				// 以期末未集结点flag2为-1
				map.put("flagB", 1);
				if (CherryChecker.compareDate(cutOfDate2, endDate) <= 0) {
					// 月度库存统计日期在集结点前
					map.put("flag", 1);
					map.put("date1", cutOfDate2);
					map.put("date2", endDate);
				} else {
					// 月度库存统计日期在集结点后
					map.put("flag", -1);
					map.put("date1", endDate);
					map.put("date2", cutOfDate2);
				}
				// 月度库存统计日期
				map.put("cutOfDate", cutOfDate2);
				//计算月度库存统计日期的前两个月的日期
				twoMonthBeforeCutDay = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN,cutOfDate2,-2);
			}
		} else {
			// ***************************2012-04-27 lipc NEWWITPOS-1328对应开始 *************************** //
			// 截止日期往前倒推一个月，防止库存历史一个月后还没数据，固截止日期倒推60天
//			String cutOfDate = DateUtil.addDateByDays(
//					CherryConstants.DATE_PATTERN, startDate, -60);
			String cutOfDate = "2010-01-01";
			// ***************************2012-04-27 lipc NEWWITPOS-1328对应结束*************************** //
			// 月度库存统计日期
			map.put("cutOfDate", cutOfDate);
			map.put("flag", 1);
			map.put("date1", cutOfDate);
			map.put("date2", startDate1);
		}
		// 开始日期
		map.put("startDate", startDate);
		// 截止日期
		map.put("endDate", endDate);
		//月度库存统计日期的前两个月的日期
		map.put("twoMonthBeforeCutDay",twoMonthBeforeCutDay);
	}
	/**
	 * 取得权限柜台(包含测试柜台)List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		return binolcm00Service.getCounterList(map);
	}
	
	/**
	 * 取得权限柜台(不包含测试柜台)List
	 * 
	 * @param map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getNoTestCounterList(Map<String, Object> map) {
		return binolcm00Service.getNoTestCounterList(map);
	}
	
	/**
	 * 取得系统日期(年月日)
	 * 
	 * 
	 * @return String
	 *			系统日期(年月日)
	 */
	public String getDateYMD() {
		return binolcm00Service.getDateYMD();
	}
	
	/**
     * 取得系统时间(yyyy-MM-dd HH:mm:ss)
     * 
     * 
     * @return String
     *          系统日期(yyyy-MM-dd HH:mm:ss)
     */
    public String getSYSDateTime() {
        return binolcm00Service.getSYSDateTime();
    }
	
	/**
     * 取得业务日期(年月日)
     * 
     * 
     * @return String
     *          业务日期(年月日)
     */
    public String getBussinessDate(Map<String, Object> map) {
        return binolcm00Service.getBussinessDate(map);
    }
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		
		// 取得岗位类别信息List
		return binolcm00Service.getPositionCategoryList(map);
	}
	
	/**
	 * 判断用户是否为最大岗位级别
	 * 
	 * @param map 查询条件
	 * @param userGrade 当前用户岗位级别
	 * 
	 * @return true：当前用户是最大岗位级别，false：当前用户不是最大岗位级别
	 */
	public boolean isMaxPosCategoryGrade(Map<String, Object> map, String userGrade) {
		
		if(userGrade != null && !"".equals(userGrade)) {
			// 取得最大岗位级别
			String grade = binolcm00Service.getMaxPosCategoryGrade(map);
			if(grade != null && !"".equals(grade)) {
				if(userGrade.equals(grade)) {
					return true;
				}
			}
		}
		return false;
		
	}
	
	/**
	 * 根据部门ID取得部门的测试区分
	 * 
	 * */
	public String getDepartTestType(String departId){
		
		return binolcm00Service.getDepartTestType(departId);
		
	}
	
	/**
	 * 根据指定的自然日，返回该自然日所属财物月对应的起止日期(自然日)
	 * 
	 * @param orgInfoId
	 *            组织ID
	 * @param dateString
	 *            自然日，格式为 YYYY-MM-DD
	 * @return 返回一个2个元素的字符串数组，Array[0]对应的是起始日期，Array[1]对应的是截止日期，格式均为 YYYY-MM-DD
	 */
	public String[] getFiscalPeriodByNatural(int orgInfoId, String dateString) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoId", orgInfoId);
		paramMap.put("dateValue", dateString);
		// 自然日所属财物月对应的日期序列
		List<String> fiscalDateList = binolcm00Service
				.getFiscalPeriodByNatural(paramMap);
		List<String> tempList = new ArrayList<String>();
		// 取起止日期
		tempList.add(fiscalDateList.get(0));
		tempList.add(fiscalDateList.get(fiscalDateList.size() - 1));
		return tempList.toArray(new String[tempList.size()]);
	}
	
	/**
     * 取得登陆用户名
     * 
     * */
    public String getLoginName(String employeeID){
        return binolcm00Service.getLoginName(employeeID);
    }
}
