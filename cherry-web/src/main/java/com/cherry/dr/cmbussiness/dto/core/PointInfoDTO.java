/*	
 * @(#)PointInfoDTO.java     1.0 2012/03/16	
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
package com.cherry.dr.cmbussiness.dto.core;

import java.util.List;
import java.util.Map;

/**
 * 积分信息 DTO
 * 
 * @author hub
 * @version 1.0 2012.03.16
 */
public class PointInfoDTO {
	
	/** 会员积分类别ID */
	private String pointTypeId;
	
	/** 积分 DTO */
	private PointDTO point;
	
	/** 会员积分变化主 DTO */
	private List<PointChangeDTO> pointChanges;
	
	/** 每天的各类型积分合计 */
	private List<Map<String, Object>> dayPoints;
	
	/** 每天的积分合计 */
	private Map<String, Object> allDayPoint;
	
	/** 活动期间各类型的积分合计  */
	private List<Map<String, Object>> datePoints;
	
	public String getPointTypeId() {
		return pointTypeId;
	}

	public void setPointTypeId(String pointTypeId) {
		this.pointTypeId = pointTypeId;
	}
	
	public PointDTO getPoint() {
		return point;
	}

	public void setPoint(PointDTO point) {
		this.point = point;
	}

	public List<PointChangeDTO> getPointChanges() {
		return pointChanges;
	}

	public void setPointChanges(List<PointChangeDTO> pointChanges) {
		this.pointChanges = pointChanges;
	}

	public List<Map<String, Object>> getDayPoints() {
		return dayPoints;
	}

	public void setDayPoints(List<Map<String, Object>> dayPoints) {
		this.dayPoints = dayPoints;
	}

	public Map<String, Object> getAllDayPoint() {
		return allDayPoint;
	}

	public void setAllDayPoint(Map<String, Object> allDayPoint) {
		this.allDayPoint = allDayPoint;
	}

	public List<Map<String, Object>> getDatePoints() {
		return datePoints;
	}

	public void setDatePoints(List<Map<String, Object>> datePoints) {
		this.datePoints = datePoints;
	}
}
