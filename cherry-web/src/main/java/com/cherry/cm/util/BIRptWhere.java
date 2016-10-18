/*	
 * @(#)BIRptWhere.java     1.0 2010/10/12		
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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 拼装BI报表检索条件
 * 
 * @author WangCT
 * 
 */
public class BIRptWhere {
	
	/** 存在与关系的单个检索条件 */
	private List<String> andList;
	
	/** 存在或关系的单个检索条件 */
	private List<String> orList;
	
	/** 存在与关系的一组检索条件 */
	private List<BIRptWhere> andWhereList;
	
	/** 存在或关系的一组检索条件 */
	private List<BIRptWhere> orWhereList;
	
	public BIRptWhere() {
		this.andList = new ArrayList<String>();
		this.orList = new ArrayList<String>();
		this.andWhereList = new ArrayList<BIRptWhere>();
		this.orWhereList = new ArrayList<BIRptWhere>();
	}
	
	/**
	 * 
	 * 添加与关系的单个检索条件
	 * 
	 * @param s 单个检索条件
	 * 
	 */
	public void andWhere(String s) {
		this.andList.add(s);
	}
	
	/**
	 * 
	 * 添加或关系的单个检索条件
	 * 
	 * @param s 单个检索条件
	 * 
	 */
	public void orWhere(String s) {
		this.orList.add(s);
	}
	
	/**
	 * 
	 * 添加与关系的一组检索条件
	 * 
	 * @param andWhere 一组检索条件
	 * 
	 */
	public void andWhere(BIRptWhere andWhere) {
		this.andWhereList.add(andWhere);
	}
	
	/**
	 * 
	 * 添加或关系的一组检索条件
	 * 
	 * @param andWhere 一组检索条件
	 * 
	 */
	public void orWhere(BIRptWhere orWhere) {
		this.orWhereList.add(orWhere);
	}
	
	/**
	 * 
	 * 拼装检索条件
	 * 
	 * @return 检索条件
	 * 
	 */
	public String getQuery() {
		
		StringBuffer query = new StringBuffer();
		for(String s : andList) {
			query.append(s + JOINMARK + ANDMARK + JOINMARK);
		}
		for(String s : orList) {
			query.append(s + JOINMARK + ORMARK + JOINMARK);
		}
		for(BIRptWhere where : andWhereList) {
			query.append("(" + where.getQuery() + ")" + JOINMARK + ANDMARK + JOINMARK);
		}
		for(BIRptWhere where : orWhereList) {
			query.append("(" + where.getQuery() + ")" + JOINMARK + ORMARK + JOINMARK);
		}
		String queryResult = query.toString();
		if(!"".equals(queryResult)) {
			if(queryResult.endsWith(JOINMARK + ANDMARK + JOINMARK)) {
				queryResult = queryResult.substring(0,queryResult.length()-ANDMARK.length()-JOINMARK.length()-JOINMARK.length());
			} else {
				queryResult = queryResult.substring(0,queryResult.length()-ORMARK.length()-JOINMARK.length()-JOINMARK.length());
			}
		}
		return queryResult;
	}
	
	/**
	 * 
	 * 拼装钻透检索条件
	 * 
	 * @return 钻透检索条件
	 * 
	 */
	public String getDrillQuery() {
		
		StringBuffer query = new StringBuffer();
		for(String s : andList) {
			query.append(s + JOINMARK + DRILLANDMARK + JOINMARK);
		}
		for(String s : orList) {
			query.append(s + JOINMARK + DRILLORMARK + JOINMARK);
		}
		for(BIRptWhere where : andWhereList) {
			query.append("(" + where.getQuery() + ")" + JOINMARK + DRILLANDMARK + JOINMARK);
		}
		for(BIRptWhere where : orWhereList) {
			query.append("(" + where.getQuery() + ")" + JOINMARK + DRILLORMARK + JOINMARK);
		}
		String queryResult = query.toString();
		if(!"".equals(queryResult)) {
			if(queryResult.endsWith(JOINMARK + DRILLANDMARK + JOINMARK)) {
				queryResult = queryResult.substring(0,queryResult.length()-DRILLANDMARK.length()-JOINMARK.length()-JOINMARK.length());
			} else {
				queryResult = queryResult.substring(0,queryResult.length()-DRILLORMARK.length()-JOINMARK.length()-JOINMARK.length());
			}
		}
		return queryResult;
	}
	
	private static String ANDMARK = "&";
	private static String ORMARK = "||";
	private static String DRILLANDMARK = "AND";
	private static String DRILLORMARK = "OR";
	private static String JOINMARK = " ";

}
