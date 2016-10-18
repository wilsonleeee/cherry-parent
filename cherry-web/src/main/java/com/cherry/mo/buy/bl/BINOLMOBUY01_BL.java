package com.cherry.mo.buy.bl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.buy.interfaces.BINOLMOBUY01_IF;
import com.cherry.mo.buy.service.BINOLMOBUY01_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/*  
 * @(#)BINOLMOBUY01_BL.java    1.0 2012-5-28     
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
public class BINOLMOBUY01_BL implements BINOLMOBUY01_IF {

	@Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOBUY01_Service binOLMOBUY01_Service;
	
    /**
     * 取得U盘考勤统计信息，画面使用
     * 
     * 
     * */
	@Override
	public Map<String, Object> getUdiskAttendanceStatisticsList(
			Map<String, Object> map) throws Exception{
		
		//拼接关键时间段在柜天数SQL语句
    	//关键时间段开始时间
    	String[] importTimeStartArr = (String[]) map.get("importTimeStartArr");
    	//关键时间段结束时间
    	String[] importTimeEndArr = (String[]) map.get("importTimeEndArr");
    	
    	if(null != importTimeStartArr && importTimeStartArr.length > 0){
    		int length = importTimeStartArr.length;
        	List<Map<String,Object>> importTimeList = new ArrayList<Map<String,Object>>();
        	for(int i = 0 ; i < length ; i++){
        		Map<String,Object> tepmap = new HashMap<String,Object>();
        		tepmap.put("importTimeStart", importTimeStartArr[i]);
        		tepmap.put("importTimeEnd", importTimeEndArr[i]);
        		importTimeList.add(tepmap);
        	}
        	String SqlStr = getImportTimeSql(importTimeList,0);
        	map.put("ImportTimeDaysSQL", SqlStr);
    	}else{
    		map.put("ImportTimeDaysSQL", null);
    	}
    	
    	//调用Service取得查询结果
    	List<Map<String,Object>> retList = binOLMOBUY01_Service.getUdiskAttendanceStatisticsList(map);
    	//调用Service取得查询总数
    	int sum = binOLMOBUY01_Service.getUdiskAttendanceStatisticsCount(map);
    	int days = 0;
    	try{
    		//计算查询日期之间的间隔天数
        	//开始日期
        	Date startDay = DateUtil.coverString2Date((String)map.put("startAttendanceDate", "yyyy-MM-dd"));
        	//结束日期
        	Date endDay = DateUtil.coverString2Date((String)map.put("endAttendanceDate", "yyyy-MM-dd"));
        	//计算间隔天数
        	days = DateUtil.getIntervalDays(startDay,endDay);
        	
    	}catch(Exception e){
    		throw new CherryException("EMO00068");
    	}
    	days++;
    	//每天拜访柜台数或者每个柜台停留时间格式化（两位小数）
    	DecimalFormat df = new DecimalFormat("0.00");
    	//对统计结果进行处理
    	for(Map<String,Object> tempMap : retList){
    		
    		//一共拜访的柜台数
    		int totalArriNum = ConvertUtil.getInt(tempMap.get("ArriveCountersNm"));
    		String arriNumPerDay = df.format(Double.parseDouble(ConvertUtil.getString(totalArriNum))/days);
    		tempMap.put("ArriNumPerDay", arriNumPerDay);
    		
    		//每个柜台停留时间（分钟）转化成小时
    		int perCoutArriTime = ConvertUtil.getInt(tempMap.get("PerCoutArriTime"));
    		String perCoutArriTime1 = df.format(Double.parseDouble(ConvertUtil.getString(perCoutArriTime))/60);
    		tempMap.put("PerCoutArriTime", perCoutArriTime1);
    		
    	}
    	
    	//将查询结果放到map中返回
    	Map<String,Object> retMap = new HashMap<String,Object>();
    	retMap.put("retList", retList);
    	retMap.put("sum", sum);
    	
    	return retMap;
	}

	/**
     * 导出考勤统计信息
     * 
     * 
     * */
	@Override
	public byte[] statisticsExportExcel(Map<String, Object> map)
			throws Exception {
		
		//拼接关键时间段在柜天数SQL语句
    	//关键时间段开始时间
    	String[] importTimeStartArr = (String[]) map.get("importTimeStartArr");
    	//关键时间段结束时间
    	String[] importTimeEndArr = (String[]) map.get("importTimeEndArr");
    	
    	if(null != importTimeStartArr && importTimeStartArr.length > 0){
    		int length = importTimeStartArr.length;
        	List<Map<String,Object>> importTimeList = new ArrayList<Map<String,Object>>();
        	for(int i = 0 ; i < length ; i++){
        		Map<String,Object> tepmap = new HashMap<String,Object>();
        		tepmap.put("importTimeStart", importTimeStartArr[i]);
        		tepmap.put("importTimeEnd", importTimeEndArr[i]);
        		importTimeList.add(tepmap);
        	}
        	String SqlStr = getImportTimeSql(importTimeList,0);
        	map.put("ImportTimeDaysSQL", SqlStr);
    	}
    	
    	//调用Service取得查询结果
    	List<Map<String,Object>> retList = binOLMOBUY01_Service.getUdiskAttendanceStatisticsForImport(map);
    	
    	int days = 0;
    	try{
    		//计算查询日期之间的间隔天数
        	//开始日期
        	Date startDay = DateUtil.coverString2Date((String)map.put("startAttendanceDate", "yyyy-MM-dd"));
        	//结束日期
        	Date endDay = DateUtil.coverString2Date((String)map.put("endAttendanceDate", "yyyy-MM-dd"));
        	//计算间隔天数
        	days = DateUtil.getIntervalDays(startDay,endDay);
        	
    	}catch(Exception e){
    		throw new CherryException("EMO00068");
    	}
    	days++;
    	
    	//每天拜访柜台数或者每个柜台停留时间格式化（两位小数）
    	DecimalFormat df = new DecimalFormat("0.00");
    	//对统计结果进行处理
    	for(Map<String,Object> tempMap : retList){
    		
    		//一共拜访的柜台数
    		int totalArriNum = ConvertUtil.getInt(tempMap.get("ArriveCountersNm"));
    		String arriNumPerDay = df.format(Double.parseDouble(ConvertUtil.getString(totalArriNum))/days);
    		tempMap.put("ArriNumPerDay", arriNumPerDay);
    		
    		//每个柜台停留时间（分钟）转化成小时
    		int perCoutArriTime = ConvertUtil.getInt(tempMap.get("PerCoutArriTime"));
    		String perCoutArriTime1 = df.format(Double.parseDouble(ConvertUtil.getString(perCoutArriTime))/60);
    		tempMap.put("PerCoutArriTime", perCoutArriTime1);
    		
    	}
    	
    	//设定excel中的每行显示的数据
    	String[][] array = {
                { "UdiskSN", "BUY01_udiskSN", "25", "", "" },										//U盘序列号
                { "EmployeeCodeName", "BUY01_employeeName", "25", "", "" },							//员工名称
                { "CategoryName", "BUY01_categoryName", "15", "", "" },								//岗位
                { "DepartCodeName", "BUY01_reginDepart", "20", "", "" },							//大区
                { "RegionNameChinese", "BUY01_city", "15", "", "" },								//城市
                { "ArriveCountersNm", "BUY01_arriveCountersNm", "15", "", "" },						//巡柜柜台数
                { "ArriveDays", "BUY01_arriveDays", "15", "", "" },									//巡柜天数
                { "ArriNumPerDay", "BUY01_arriNumPerDay", "22", "", "" },							//平均每天拜访柜台数
                { "PerCoutArriTime", "BUY01_perCoutArriTime", "28", "", "" },						//平均每家柜台停留时间(小时)
                { "ImportTimeDays", "BUY01_importTimeDays", "22", "", "" },							//关键时间段内在柜天数
                { "ArriveDiffCountersNm", "BUY01_arriveDiffCountersNm", "20", "", "" },				//拜访不同柜台数
                { "DepartNum", "BUY01_departNum", "20", "", "" },									//应拜访不同柜台数
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        //资源文件
        ep.setBaseName("BINOLMOBUY01");
        //sheet名称
        ep.setSheetLabel("sheetName1");
        //明细行数据
        ep.setDataList(retList);
        //明细行显示边框
        ep.setShowBorder(true);
        ep.setSearchCondition(getTableTitle((String)map.get(CherryConstants.SESSION_LANGUAGE)));
        return binOLMOCOM01_BL.getExportExcel(ep);
	}

	/**
     * 拼接关键时间段在柜天数SQL语句
     * @param importTimeList
     * @return SqlString
     * 
     * */
	private String getImportTimeSql(List<Map<String,Object>> importTimeList,int deep){
    	String sqlStr = "";
    	if(null == (Integer)deep){
    		deep = 0;
    	}
    	if(importTimeList.size()-1 < deep){
    		return deep>0? "NULL" : "";
    	}else{
    		sqlStr = "CASE WHEN ((DATEDIFF(N,CONVERT(varchar(10),A.ArriveTime,8),'"+importTimeList.get(deep).get("importTimeEnd")+"' ) > 0 AND DATEDIFF(N,CONVERT(varchar(10),ISNULL(A.LeaveTime,A.ArriveTime),8),'"+importTimeList.get(deep).get("importTimeStart")+"') < 0))";
    		sqlStr = sqlStr + " THEN A.AttendanceDate ELSE ";
    		String str = getImportTimeSql(importTimeList,++deep);
    		sqlStr = sqlStr + str + " END";
    	}
    	return sqlStr;
    }
	
	/**
	 * 取得表头信息
	 * 
	 * */
	private String getTableTitle(String language){
		String str = "";
		//取得资源文件中的数据
		str = binOLMOCOM01_BL.getResourceValue("BINOLMOBUY01",language,"BUY01_tableTitle");
		
		Date now = new Date(); 
		DateFormat d1 = DateFormat.getDateTimeInstance();
		str = str+"  "+ d1.format(now);
		return str;
	}
}
