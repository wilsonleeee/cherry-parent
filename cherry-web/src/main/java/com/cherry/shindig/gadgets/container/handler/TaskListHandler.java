/*
 * @(#)TaskListHandler.java     1.0 2011/11/01
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
package com.cherry.shindig.gadgets.container.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.cm.cmbeans.CherryTaskInstance;
import com.cherry.cm.cmbussiness.bl.BINOLCM10_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.core.CherryConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 任务取得Handler
 * 
 * @author WangCT
 * @version 1.0 2011/11/01
 */
@Service(name = "taskList")
public class TaskListHandler {
	
	@Resource
	BINOLCM10_BL binOLCM10_BL;
	@Resource
	BINOLCM19_BL binolcm19_bl;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getTaskList(RequestItem request) throws Exception {
		 
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String language = (String)userInfoMap.get("language");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfoMap.get("BIN_PositionCategoryID"));
		map.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfoMap.get("BIN_OrganizationID"));
		map.put("BIN_OrganizationInfoID", userInfoMap.get("BIN_OrganizationInfoID"));
		map.put("BIN_BrandInfoID", userInfoMap.get("BIN_BrandInfoID"));
		
		map.put("language", language);
		//取得任务
    	List<CherryTaskInstance> retList = binolcm19_bl.getUserTasks(map);
//    	if(retList.size()>0){
//            Iterator<CherryTaskInstance> it = retList.iterator();
//            while(it.hasNext()){
//                CherryTaskInstance ret = binolcm19_bl.getTaskInfo((CherryTaskInstance) it.next());
//                //移除接口单据号不存在（没有查到主单）的记录
//                if(null == ret.getBillCode() || "".equals(ret.getBillCode())){
//                    it.remove();
//                }else{
//                    ret.setTaskName(CherryConstants.TASK_NAME.getName(ret.getProType(), language)+CherryConstants.TASK_NAME.getName(ret.getCurrentOperate(), language));
//                }
//            }
////    		for(int i=0;i<retList.size();i++){
////    			CherryTaskInstance ret = binolcm19_bl.getTaskInfo(retList.get(i));
////    			ret.setTaskName(CherryConstants.TASK_NAME.getName(ret.getProType(), language)+CherryConstants.TASK_NAME.getName(ret.getCurrentOperate(), language));    	
////    		}
//    	}
		// 返回结果
		Map data = new HashMap();
		data.put("taskList", retList);
		return ImmediateFuture.newInstance(new DataCollection(data));
	}

}
