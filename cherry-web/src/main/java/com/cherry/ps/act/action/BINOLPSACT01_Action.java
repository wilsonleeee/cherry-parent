/*  
 * @(#)BINOLPSACT01_Action.java     1.0 2011/05/31      
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
package com.cherry.ps.act.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.parser.ParseException;

import com.cherry.cm.core.BaseAction;
import com.cherry.ps.act.bl.BINOLPSACT01_BL;
/**
 * 积分设定Action
 * @author huzude
 * @version 1.0 2010.08.06
 */
@SuppressWarnings("unchecked")
public class BINOLPSACT01_Action extends BaseAction{
	@Resource
	private BINOLPSACT01_BL binOLPSACT01_BL;
	/**  页面属性定义   **/
	// 规则drl数组
	private String[] jsDrlArr;
	// 活动名
	private String activeName;
	// 活动描述 
	private String activeDescribe;
	// 活动取舍
	private String activeDeside;
	// 活动组id
	private String activeGroupId;
	// 积分上限
	private String pointsLimit;
 
	/**
	 * 活动创建
	 * @return String 
	 * @throws ParseException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String createAct() throws ParseException{
		// 新建转换后规则drlList
		List parseDrlList = new ArrayList();
		// 新建关联规则List
		List<String> joinRuleIdList =new ArrayList();
		// fact变量key
		HashMap factKeyMap = new HashMap();
		// 多时间关联子规则fact所需变量
		List<HashMap> joinFactList = new ArrayList();
		jsDrlArr = new String[1];
		jsDrlArr[0] = "when \n \t [#buydate# == <2010/10/01> && #price# = 300 ] && [#buydate# == <2010/05/01> && #buyCount# = 1 ] && #buydate#==2010/03/08 && #buyCount# = 1 && #barCode#=99085474 \n then \n \t point = point*2";
		//List jsDrlList = binOLPSACT01_BL.resolveJson(jsDrlArr);
		// 对js规则drl进行循环
		for (int i=0;i<jsDrlArr.length;i++){
			// 取得js规则drl字符
			String jsDrlStr = (String)jsDrlArr[i];
			// 新建一个关键字转化List
			jsDrlStr = binOLPSACT01_BL.parseJsDrl(jsDrlStr, parseDrlList,joinRuleIdList,factKeyMap,joinFactList);
			parseDrlList.add(jsDrlStr);
		}
		
		// 生成drl文件
		binOLPSACT01_BL.createDrlStr(parseDrlList, factKeyMap,joinRuleIdList,joinFactList);
		
		return SUCCESS;
	}

	public BINOLPSACT01_BL getBinOLPSACT01_BL() {
		return binOLPSACT01_BL;
	}

	public void setBinOLPSACT01_BL(BINOLPSACT01_BL binOLPSACT01BL) {
		binOLPSACT01_BL = binOLPSACT01BL;
	}

	public String[] getJsDrlArr() {
		return jsDrlArr;
	}

	public void setJsDrlArr(String[] jsDrlArr) {
		this.jsDrlArr = jsDrlArr;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getActiveDescribe() {
		return activeDescribe;
	}

	public void setActiveDescribe(String activeDescribe) {
		this.activeDescribe = activeDescribe;
	}

	public String getActiveDeside() {
		return activeDeside;
	}

	public void setActiveDeside(String activeDeside) {
		this.activeDeside = activeDeside;
	}

	public String getActiveGroupId() {
		return activeGroupId;
	}

	public void setActiveGroupId(String activeGroupId) {
		this.activeGroupId = activeGroupId;
	}

	public String getPointsLimit() {
		return pointsLimit;
	}

	public void setPointsLimit(String pointsLimit) {
		this.pointsLimit = pointsLimit;
	}
	
	

	
}
