/*	
 * @(#)BINOLBSCNT05_BL.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.cnt.service.BINOLBSCNT05_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.service.BINOLPTJCS04_Service;
import com.cherry.synchro.bs.bl.CounterSynchro;

/**
 * 
 * 	停用启用柜台处理BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT05_BL {
	
	/** 停用启用柜台处理Service */
	@Resource
	private BINOLBSCNT05_Service binOLBSCNT05_Service;
	
	@Resource
	private CounterSynchro counterSynchro;
	
	/** 创建柜台画面Service */
	@Resource
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	@Resource
	private BINOLPTJCS04_Service binOLPTJCS04_Service;
	
	@Resource
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**WebService 共通BL*/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/**
	 * 停用启用柜台
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void tran_updateCounterInfo(Map<String, Object> map) throws Exception {
		
		// 取得启用停用flag
		String validFlag = (String)map.get("validFlag");
		// 停用的场合
		if(validFlag != null && "0".equals(validFlag)) {
			map.put("status", "4");
		} else {
			map.put("status", "0");
		}
		
		// 取得当前部门(柜台)产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("pdTVersion", pdTVersion);
		
		// 产品变动后更新柜台停用/启用后更新产品方案明细表的version字段
		map.put("counterCodeList", map.get("counterCode"));
		binOLBSCNT05_Service.updatePrtSoluDepartRelation(map);
		
		// 停用柜台时，柜台产品对应的数据将会被停用
		
		// 启用柜台时，柜台产品对应的数据重新启用
		
		// 以上停用/启用需要实时下发
		
		// 停用启用柜台
		binOLBSCNT05_Service.updateCounterInfo(map);
		// 停用启用部门表中的柜台
		binOLBSCNT05_Service.updateDepartInfo(map);
		
		// 取得柜台IDList
		List<String> counterList = (List)map.get("counterInfoId");
		if(counterList != null) { 
			// 取得系统时间
			String sysDate = binOLBSCNT05_Service.getSYSDate();
			List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < counterList.size(); i++) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.putAll(map);
				param.put("counterInfoId", counterList.get(i));
				// 事件名称ID
				param.put("eventNameId", map.get("status"));
				// 事件开始日
				param.put("fromDate", sysDate);
				// 事件终了日
				param.put("toDate", sysDate);
//				// 事件原因
//				param.put("eventReason", "");
				paramList.add(param);
			}
			// 添加柜台事件
			binOLBSCNT04_Service.addCounterEvent(paramList);
		}
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoId =ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
			
			// 取得柜台IDList
			List<String> pCounterList = (List)map.get("counterInfoId");
			if(pCounterList != null) {
				Map<String, String> param = new HashMap<String, String>();
				Map<String, Object> searchMap = new HashMap<String, Object>();
				for(int i = 0; i < pCounterList.size(); i++) {
					searchMap.put("counterInfoId", pCounterList.get(i));
					//柜台下发
					Map<String,Object> synchroInfo = counterSynchro.assemblingSynchroInfo(searchMap);
					if(null != synchroInfo){
						// 操作类型--停用(DEL)、启用(IUE)
						synchroInfo.put("Operate", (validFlag != null && "0".equals(validFlag)) ? "DEL" : "IUE");
						counterSynchro.synchroCounter(synchroInfo);
					}
				}
			}
			
			
//			List<String> counterInfoIdArr = (List<String>) map.get("counterInfoId");
//			if(counterInfoIdArr != null){
//				Map<String,Object> tempMap = new HashMap<String,Object>();
//				tempMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
//				tempMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
//				tempMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
//				tempMap.put("loginName", map.get("loginName"));
//				for(String counterInfoId:counterInfoIdArr){
//					tempMap.put("counterInfoId", counterInfoId);
//					//通过WebService方式将柜台信息下发
//					Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(tempMap);
//					if(WSMap.isEmpty()) return;
//					//WebService方式台信息Map
//					Map<String,Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
//					String State = ConvertUtil.getString(resultMap.get("State"));
//					String Data = ConvertUtil.getString(resultMap.get("Data"));
//					if(State.equals("ERROR")){
//						CherryException CherryException = new CherryException("");
//						CherryException.setErrMessage(Data);
//						throw CherryException;
//					}
//				}
//			}
		}
		
	}

}
