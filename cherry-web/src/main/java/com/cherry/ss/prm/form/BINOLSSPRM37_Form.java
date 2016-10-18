/*  
 * @(#)BINOLSSPRM37_Form.java     1.0 2011/05/31      
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
package com.cherry.ss.prm.form;

import java.util.List;
import java.util.Map;
@SuppressWarnings("unchecked")
public class BINOLSSPRM37_Form extends BINOLSSPRM13_Form{
	
	/** 促销活动ID */
	private String activeID;
	
	/** 促销活动ID */
	private String activityCode;
	
	/** 促销活动时间分组List */
	private List actGrpTimeList;
	
	/** 促销活动地点分组List */
	private List actGrpLocationList;
	
	/** 选择树类型*/
	private String treeType;
	
	/** 地点分组块ID*/
	private String grpID;
	
	/** 更新时间 */
	private String updTime;
	
	/** 更新次数 */
	private String modCount;
	
	/** 规则明细ID*/
	private String ruleID;
	
	/** 显示类型 */
	private String showType;
	
	private String sendFlag;
	
	private Map map;
	
	/** 活动设定者 */
	private String activitySetBy;
	
	/** 柜台名称 */
	private String counterName;
	
	private int check;
	
	/** 促销活动结果信息List  */
	private List<Map<String, Object>> prmActiveRelList;
	
	
	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public List getActGrpTimeList() {
		return actGrpTimeList;
	}

	public void setActGrpTimeList(List actGrpTimeList) {
		this.actGrpTimeList = actGrpTimeList;
	}

	public String getActiveID() {
		return activeID;
	}

	public void setActiveID(String activeID) {
		this.activeID = activeID;
	}

	public List getActGrpLocationList() {
		return actGrpLocationList;
	}

	public void setActGrpLocationList(List actGrpLocationList) {
		this.actGrpLocationList = actGrpLocationList;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getGrpID() {
		return grpID;
	}

	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}

	public List<Map<String, Object>> getPrmActiveRelList() {
		return prmActiveRelList;
	}

	public void setPrmActiveRelList(List<Map<String, Object>> prmActiveRelList) {
		this.prmActiveRelList = prmActiveRelList;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getUpdTime() {
		return updTime;
	}

	public void setUpdTime(String updTime) {
		this.updTime = updTime;
	}

	public String getModCount() {
		return modCount;
	}

	public void setModCount(String modCount) {
		this.modCount = modCount;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getRuleID() {
		return ruleID;
	}

	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getActivitySetBy() {
		return activitySetBy;
	}

	public void setActivitySetBy(String activitySetBy) {
		this.activitySetBy = activitySetBy;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}
}
