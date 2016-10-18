/*		
 * @(#)BINOLCM00_Service.java     1.0 2010/10/12		
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
package com.cherry.cm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.WitIfServiceImpl;

/**
 * 
 * TEST共通基础处理
 * 
 * @author lipc
 * @version 1.0 2011.12.06
 */
public class TESTCOM_Service extends BaseService {
	
	@Resource
	private WitIfServiceImpl witIfServiceImpl;
	
	/**
	 * 取得系统时间(年月日)
	 * 
	 * @param map
	 * @return
	 */
	public String getDate() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.getDateYMD");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得数据库系统当前时间
	 * 
	 * */
	public String getDbTime(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.getDbTime");
		String time = (String) baseServiceImpl.get(paramMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.getDateYMD");
		String date = (String)baseServiceImpl.get(paramMap);
		return date+" "+time;
	} 
	
	
	/**
	 * 根据表名、开始时间以及结束时间删除数据。
	 * @param tableName 表名,是String类型
	 * @param startTime 开始时间,是String类型
	 * @param endTime 结束时间,是String类型
	 * 
	 * @return
	 * 
	 * */
	public void deleteTableData(String tableName,String startTime,String endTime){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(tableName == null || "".equals(tableName)){
			return;
		}
		paramMap.put("tableName", tableName);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		paramList.add(paramMap);
		baseServiceImpl.deleteAll(paramList, "TESTCOM.deleteTableData");
	}
	
	/**
	 * 根据表名、开始时间以及结束时间删除数据。
	 * @param tableNameArr 表名,是String[]类型
	 * @param startTime 开始时间,是String类型
	 * @param endTime 结束时间,是String类型
	 * 
	 * @return
	 * 
	 * */
	public void deleteTableData(String[] tableNameArr,String startTime,String endTime){
		
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		
		for(String temName : tableNameArr){
			if(temName!=null && !"".equals(temName)){
				Map<String,Object> temMap = new HashMap<String,Object>();
				temMap.put("tableName", temName);
				temMap.put("startTime", startTime);
				temMap.put("endTime", endTime);
				paramList.add(temMap);
			}
		}
		if(paramList.isEmpty()){
			return;
		}
		
		baseServiceImpl.deleteAll(paramList, "TESTCOM.deleteTableData");
	}
	
	/**
	 * SELECT
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> select(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeSelect");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * SELECT CONFIG
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectConfig(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeSelect");
		return baseConfServiceImpl.getList(map);
	}
	
	/**
	 * INSERT
	 * 
	 * @param sql
	 */
	public void insert(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeInsert");
		baseServiceImpl.save(map);
	}
	/**
	 * INSERT CONFIG
	 * 
	 * @param sql
	 */
	public void insertConfig(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeInsert");
		baseConfServiceImpl.save(map);
	}
	
	/**
	 * UPDATE
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeUpdate");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * UPDATE CONFIG
	 * 
	 * @param sql
	 * @return
	 */
	public int updateConfig(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeUpdate");
		return baseConfServiceImpl.update(map);
	}
	
	/**
	 * DELETE
	 * 
	 * @param sql
	 * @return
	 */
	public int delete(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeDelete");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * DELETE CONFIG
	 * 
	 * @param sql
	 * @return
	 */
	public int deleteConfig(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.executeDelete");
		return baseConfServiceImpl.remove(map);
	}
	
	/**
	 * 根据指定条件查询新后台品牌数据库中指定表中的数据,只是单表查询,不支持多表查询.
	 * @param paramMap
	 * 			此参数中必须有两种类型的数据：一种是要查询的表名，另一种是查询条件，不支持无条件查询，因为无条件查询在测试中毫无意义。
	 * 			表名的key必须为"tableName"，否则在动态SQL中无法识别，对应的value值是"模式"+"."+"表名"，例如："Inventory.BIN_ProductDeliverDetail"。
	 * 			查询条件之间都是与的关系，并且都是等于。查询条件对应的key就是表中的字段名，value值就是字段名对应的值。
	 * 			查询条件中有两个比较特殊的同时也是可选的查询条件："startTime"和"endTime"，这两个条件在查询的时候是跟表中的"CreateTime"进行比较的。
	 *
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getTableData(Map<String,Object> paramMap){
		
		List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
		
		for(Map.Entry<String, Object> en : paramMap.entrySet()){
			Map<String,Object> temMap = new HashMap<String,Object>();
			String filedName = en.getKey();
			if(!"tableName".equals(filedName) && !"startTime".equals(filedName) && !"endTime".equals(filedName)){
				temMap.put("fieldName", filedName);
				temMap.put("fieldValue", en.getValue());
				conditionList.add(temMap);
			}
		}
		paramMap.put("conditionList", conditionList);
		
		return baseServiceImpl.getList(paramMap,"TESTCOM.getCherryTableData");
	}
	
	/**
	 * 给新后台品牌数据库中指定的表指定的字段插入指定的值,并返回自增ID
	 * @param paramMap
	 * 			此参数中必须包含两种类型的数据：一种是要插入数据的表名，一种是要插入的字段以及对应的值。
	 * 			表名的key必须为"tableName"，否则在动态SQL中无法识别，对应的value值是"模式"+"."+"表名"，例如："Inventory.BIN_ProductDeliverDetail"。
	 * 			插入的字段的key就是表中的字段名，value值就是字段名对应的值。
	 * 			ValidFlag、CreateTime、UpdateTime三个字段未在paramMap中填写时取默认，分别为1、GETDATE()、GETDATE()
	 * 
	 * @return int
	 * 
	 * */
	public int insertTableData(Map<String,Object> paramMap){
		
		List<String> fieldsName = new ArrayList<String>();
		List<String> fieldsValue = new ArrayList<String>();
		
		for(Map.Entry<String, Object> en : paramMap.entrySet()){
			if(!"tableName".equals(en.getKey()) &&	!"ValidFlag".equals(en.getKey()) &&	!"CreateTime".equals(en.getKey()) && !"UpdateTime".equals(en.getKey()) ){
				fieldsName.add(en.getKey());
				fieldsValue.add(String.valueOf(en.getValue()));
			}
		}
		
		paramMap.put("fieldsName", fieldsName);
		paramMap.put("fieldsValue", fieldsValue);
		
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.insertTableData");
		
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 根据表名、以及其他条件删除老后台配置数据库中的数据，精确删除。
	 * @param 
	 * 		tableName 要删除数据的表名
	 * @param
	 * 		map 存放的是删除条件，此参数为空则删除整张表的数据，map中key对应的该表中的某个字段，而对应的value则是该字段对应的值。
	 * 
	 * */
	public void deleteWitConfData(String tableName,Map<String,Object> map){
		if(null == tableName || "".equals(tableName)){
			return ;
		}
		
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", tableName);
		
		if(null != map && !map.isEmpty()){
			List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
			for(Map.Entry<String, Object> en : map.entrySet()){
				Map<String,Object> temMap = new HashMap<String,Object>();
				temMap.put("fieldName", en.getKey());
				temMap.put("fieldValue", en.getValue());
				conditionList.add(temMap);
			}
			paramMap.put("conditionList", conditionList);
		}
		
		paramList.add(paramMap);
		witIfServiceImpl.deleteAll(paramList, "TESTCOM.deleteData");
	}
	
	/**
	 * 根据指定条件查询新后台品牌数据库中指定表中的数据，只是单表查询，不支持多表查询。
	 * @param 
	 * 		paramMap 此参数中必须有两种类型的数据：一种是要查询的表名，另一种是查询条件，不支持无条件查询，因为无条件查询在测试中毫无意义。
	 * 			表名的key必须为"tableName"，否则在动态SQL中无法识别，对应的value值是"模式"+"."+"表名"，例如："Inventory.BIN_ProductDeliverDetail"。
	 * 			查询条件之间都是与的关系，并且都是等于。查询条件对应的key就是表中的字段名，value值就是字段名对应的值。
	 * 			查询条件中有两个比较特殊的同时也是可选的查询条件："startTime"和"endTime"，这两个条件在查询的时候是跟表中的"CreateTime"进行比较的。
	 *
	 * @return 
	 * 		list 符合条件的数据
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCherryBrandDbData(Map<String,Object> paramMap){
		
		List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
		
		for(Map.Entry<String, Object> en : paramMap.entrySet()){
			Map<String,Object> temMap = new HashMap<String,Object>();
			String filedName = en.getKey();
			if(!"tableName".equals(filedName) && !"startTime".equals(filedName) && !"endTime".equals(filedName)){
				temMap.put("fieldName", filedName);
				temMap.put("fieldValue", en.getValue());
				conditionList.add(temMap);
			}
		}
		paramMap.put("conditionList", conditionList);
		
		return baseServiceImpl.getList(paramMap,"TESTCOM.getCherryTableData");
	}
	
	/**
	 * 根据指定条件查询老后台配置数据库中指定表中的数据，只是单表查询，不支持多表查询。
	 * @param 
	 * 		paramMap 此参数中必须有两种类型的数据：一种是要查询的表名，另一种是查询条件，不支持无条件查询，因为无条件查询在测试中毫无意义。
	 * 			表名的key必须为"tableName"，否则在动态SQL中无法识别，对应的value值是"模式"+"."+"表名"，例如："Inventory.BIN_ProductDeliverDetail"。
	 * 			查询条件之间都是与的关系，并且都是等于。查询条件对应的key就是表中的字段名，value值就是字段名对应的值。
	 * 
	 * @return 
	 * 		list 符合条件的数据
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getWitConfDbData(Map<String,Object> paramMap){
		
		List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
		
		for(Map.Entry<String, Object> en : paramMap.entrySet()){
			Map<String,Object> temMap = new HashMap<String,Object>();
			String filedName = en.getKey();
			if(!"tableName".equals(filedName)){
				temMap.put("fieldName", filedName);
				temMap.put("fieldValue", en.getValue());
				conditionList.add(temMap);
			}
		}
		paramMap.put("conditionList", conditionList);
		
		return witIfServiceImpl.getList(paramMap,"TESTCOM.getWitTableData");
	}
	
   /** 给指定的表指定的字段插入指定的值
    * @param paramMap
    *          此参数中必须包含两种类型的数据：一种是要插入数据的表名，一种是要插入的字段以及对应的值。
    *          表名的key必须为"tableName"，否则在动态SQL中无法识别，对应的value值是"模式"+"."+"表名"，例如："Inventory.BIN_ProductDeliverDetail"。
    *          插入的字段的key就是表中的字段名，value值就是字段名对应的值。
    * 
    * */
   public void insertTableDataNoReturnID(Map<String,Object> paramMap){
       Map<String,Object> map = new HashMap<String,Object>();
       map.putAll(paramMap);
       List<String> fieldsName = new ArrayList<String>();
       List<String> fieldsValue = new ArrayList<String>();
       
       for(Map.Entry<String, Object> en : map.entrySet()){
           if(!"tableName".equals(en.getKey()) && !"ValidFlag".equals(en.getKey())){
               fieldsName.add(en.getKey());
               fieldsValue.add(String.valueOf(en.getValue()));
           }
       }
       
       map.put("fieldsName", fieldsName);
       map.put("fieldsValue", fieldsValue);
       
       map.put(CherryConstants.IBATIS_SQL_ID, "TESTCOM.insertTableData");
       
       baseServiceImpl.save(map);
   }
}
