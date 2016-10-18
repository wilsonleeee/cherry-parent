/*  
 * @(#)ConflictManipulationUtil.java     1.0 2011/05/31      
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cherry.ps.act.dto.ActiveChoiceDTO;
import com.cherry.ps.act.dto.ChoiceDTO;
import com.cherry.ps.act.dto.GroupChoiceDTO;
import com.cherry.ps.act.dto.RuleActiveDTO;



@SuppressWarnings("unchecked")
public class ConflictManipulationUtil {
public static void conflictManipulation (ChoiceDTO choiceDTO ){
		
		List<RuleActiveDTO> list = choiceDTO.getActList(); 
		
		List<GroupChoiceDTO> groupChoiseList = choiceDTO.getGrpChoiceList();
		
		List<ActiveChoiceDTO> actChoiseList = choiceDTO.getActChoiceList();
		// 将list按照活动组优先级降序排序
		sortListByGroup(list);
		int firstGroup = 0;
		for (int i=0;i<list.size();i++){
			RuleActiveDTO ruleActiveDTO = list.get(i);
			if (i==0){
				firstGroup = ruleActiveDTO.getGroupPriority();
			}else{
				int tmpGroup = ruleActiveDTO.getGroupPriority();
				if (firstGroup!=tmpGroup){
					//过滤掉不同优先级的活动组后，list中剩下的就是相同活动组的活动
					list.remove(i);
				}
			}
		}
		
		// 将剩余同组中的活动按活动名分装不同的list
		sortListByActive(list);
		HashMap activeMap = new HashMap();
		String compareActName = "";
		for (int i=0;i<list.size();i++){
			RuleActiveDTO ruleActiveDTO = list.get(i);
			if (i==0){
				compareActName = ruleActiveDTO.getActiveName();
				List actList = new ArrayList();
				actList.add(ruleActiveDTO);
				activeMap.put(compareActName, actList);
			}else{
				String tmpActName = ruleActiveDTO.getActiveName();
				if (tmpActName.equals(compareActName)){
					List actList = (List)activeMap.get(tmpActName);
					actList.add(ruleActiveDTO);
				}else{
					List actList = new ArrayList();
					actList.add(ruleActiveDTO);
					activeMap.put(ruleActiveDTO.getActiveName(), actList);
				}
			}
			
		}
		
		// 遍历每个活动，根据每个活动的取舍规则算出每个活动的积分
		Iterator iter = activeMap.entrySet().iterator(); 
		List<RuleActiveDTO> actListByGroup = new ArrayList();
		int groupPriority = 0;
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String key = (String)entry.getKey(); 
		    List<RuleActiveDTO> actList = (List)entry.getValue(); 
		    RuleActiveDTO act = (RuleActiveDTO)actList.get(0);
		    groupPriority = act.getGroupPriority();
		    if (actChoiseList.size()>1){
			    for (int i=0;i<actChoiseList.size();i++){
			    	ActiveChoiceDTO activeChoiceBean = actChoiseList.get(i);
			    	if (act.getActiveName().equals(activeChoiceBean.getActiveName())){
			    		String actChoice = activeChoiceBean.getChoice();
			    		// 将同一活动不同规则进行积分值的降序
			    		sortListByPoint(actList);
			    		if ("max".equals(actChoice)){
			    			actListByGroup.add(actList.get(0));
			    		}else if ("min".equals(actChoice)){
			    			actListByGroup.add(actList.get(actList.size()-1));
			    		}
			    		break;
			    	}
			    }
		    }else{
		    	actListByGroup.add(act);
		    }
		} 
		RuleActiveDTO ruleActiveDTOFinall = new RuleActiveDTO();
		if (actListByGroup.size()>1){
			RuleActiveDTO act = (RuleActiveDTO)actListByGroup.get(0);
			 for (int i=0;i<groupChoiseList.size();i++){
				 GroupChoiceDTO groupChoiceBean = groupChoiseList.get(i);
				 if (act.getGroupPriority()==groupChoiceBean.getGroupPriority()){
					 String grpChoice = groupChoiceBean.getChoice();
			    		sortListByPoint(actListByGroup);
			    		if ("max".equals(grpChoice)){
			    			ruleActiveDTOFinall = actListByGroup.get(0);
			    		}else if ("min".equals(grpChoice)){
			    			ruleActiveDTOFinall = actListByGroup.get(actListByGroup.size()-1);
			    		}
			    		break;
				 }
			 }
		}
		
		//choiceDTO.setRuleActiveBeanFinally(ruleActiveBeanFinall) ;
	}
	
	/**
	 * 按照活动名降序
	 * @param list
	 */
	public static void sortListByActive(List list){
		 Collections.sort(list, new Comparator<RuleActiveDTO>() {
	            public int compare(RuleActiveDTO r1, RuleActiveDTO r2) {
	            	int flag = r1.getActiveName().compareTo(r2.getActiveName());
	                if (flag==0){
	                    return r1.getActiveName().compareTo(r2.getActiveName());
	                }else{
	                	return flag; 
	                }
	            }
	        });
	}
	
	/**
	 * 按照活动名降序
	 * @param list
	 */
	public static void sortListByPoint(List list){
		 Collections.sort(list, new Comparator<RuleActiveDTO>() {
	            public int compare(RuleActiveDTO r1, RuleActiveDTO r2) {
	                if (r1.getPointValue()> r2.getPointValue()){
	                    return -1;
	                } else if (r1.getPointValue()< r2.getPointValue()){
	                    return 1;
	                }else{
	                	return 0; 
	                }
	            }
	        });
	}
	
	/**
	 * 按照活动组的优先级降序
	 * @param list
	 */
	public static void sortListByGroup(List list){
		 Collections.sort(list, new Comparator<RuleActiveDTO>() {
	            public int compare(RuleActiveDTO r1, RuleActiveDTO r2) {
	                if (r1.getGroupPriority()> r2.getGroupPriority()){
	                    return -1;
	                } else if (r1.getGroupPriority()< r2.getGroupPriority()){
	                    return 1;
	                }else{
	                	return 0; 
	                }
	            }
	        });
	}
}
