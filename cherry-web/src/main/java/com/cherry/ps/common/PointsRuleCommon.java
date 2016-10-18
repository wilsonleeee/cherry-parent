/*  
 * @(#)PointsRuleCommon.java     1.0 2011/05/31      
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
package com.cherry.ps.common;

import java.util.HashMap;
import java.util.List;

/**
 * 积分规则共通类
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class PointsRuleCommon {
	
	public static void pointsRuleSubsection (List<HashMap> subsectionList,float point){
		// 剩余原始积分
		float remainPoint = point;
		// 合计奖励积分
		float sumPoint = 0;
		// 遍历积分分段list
		for (int i=0;i<subsectionList.size();i++){
			// 取得积分分段Map
			HashMap subsectionMap = subsectionList.get(i);
			// 积分起始点
			float startPoint =Float.parseFloat(String.valueOf(subsectionMap.get("startPoint")));
			// 积分结束点
			float endPoint = Float.parseFloat(String.valueOf(subsectionMap.get("endPoint")));
			// 积分倍数
			float multiple = Float.parseFloat(String.valueOf(subsectionMap.get("multiple")));
			// 奖励积分值
			float rewardPoint = Float.parseFloat(String.valueOf(subsectionMap.get("rewardPoint")));
			
			// 当积分在范围之内时
			if (remainPoint>startPoint){
				// 当endPoint设成0时表示不设定最大边界点
				if (remainPoint>=endPoint && endPoint!=0){
					remainPoint -=  endPoint;
					// 计算奖励积分
					sumPoint+=(endPoint-startPoint)*multiple;
					sumPoint+=rewardPoint;
				}else{
					// 计算奖励积分
					sumPoint+=remainPoint*multiple;
					sumPoint+=rewardPoint;
				}
			}
		}
	}
	
	
	
	
	
}
