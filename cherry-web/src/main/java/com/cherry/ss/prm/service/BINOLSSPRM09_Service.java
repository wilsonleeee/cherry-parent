/*
 * @(#)BINOLSSPRM09_Service.java     1.0 2010/10/27
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 促销品类别查询
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM09_Service extends BaseService {
   /**
    * 取得某一用户能访问的顶层促销品类别级别
    * 
    * @param map 检索条件
    * @return 促销品类别级别
    */
   public Integer getFirstCategoryLevel(Map<String, Object> map) {
	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   parameterMap.putAll(map);
	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getFirstCategoryLevel");
	   return (Integer)baseServiceImpl.get(parameterMap);
   }

   /**
    * 取得某一用户能访问的顶层顶层促销品类别级别List
    * 
    * @param map 检索条件
    * @return 促销品类别级别List
    */
   public List getFirstCategoryList(Map<String, Object> map) {
	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   parameterMap.putAll(map);
	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getFirstCategoryList");
	   return baseServiceImpl.getList(parameterMap);
   }

   /**
    * 取得某促销品类别的直属下级类别
    * 
    * @param map 检索条件
    * @return 促销品类别List
    */
    public List getNextCategoryInfoList(Map<String, Object> map) {
	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   parameterMap.putAll(map);
	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getNextCategoryInfoList");
	   return baseServiceImpl.getList(parameterMap);
    }
	
    /**
     * 取得某促销品类别的上级类别
     * 
     * @param map 检索条件
     * @return 促销品类别List
     */
     public List getHigherCategoryList(Map<String, Object> map) {
 	   Map<String, Object> parameterMap = new HashMap<String, Object>();
 	   parameterMap.putAll(map);
 	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getHigherCategoryList");
 	   return baseServiceImpl.getList(parameterMap);
     }
     
	/**
	 * 取得促销产品类别总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmCateCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getPrmCateCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品类别信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmCategoryList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getPrmCategoryList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销产品类别信息
	 * 
	 * @param promotionProId
	 * @return
	 */
	public Map getPrmCategoryInfo (String prmCategoryId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prmCategoryId", prmCategoryId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.getPrmCategoryInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 伦理删除促销产品类别信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePrmCategory (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM09.invalidPrmCategory");
		return baseServiceImpl.update(map);
	}
	
}
