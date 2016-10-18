/*  
 * @(#)BINOLMOCIO01_BL.java     1.0 2011/06/09      
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
package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO01_IF;
import com.cherry.mo.cio.service.BINOLMOCIO01_Service;
import com.cherry.synchro.mo.interfaces.CounterMessageSynchro_IF;
import com.cherry.synchro.mo.service.CounterMessageSynchroService;


/**
 * 
 * 柜台消息管理BL
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO01_BL implements BINOLMOCIO01_IF {
    
    @Resource
    private BINOLMOCIO01_Service binOLMOCIO01_Service;
    
    @Resource
    private CounterMessageSynchro_IF counterMessageSynchro;
    
    @Override
    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getCounterMessageCount(Map<String, Object> map) {
        return binOLMOCIO01_Service.getCounterMessageCount(map);
    }

    @Override
    /**
     * 取得柜台消息List
     * 
     * @param map
     * @return 柜台消息List
     */
    public List<Map<String, Object>> getCounterMessageList(
            Map<String, Object> map) {
    	List<Map<String, Object>> messageList = binOLMOCIO01_Service.getCounterMessageList(map);
    	
    	if(null != messageList && messageList.size() > 0){
    		int size = messageList.size();
    		for(int i = 0 ; i < size ; i++){
    			Map<String,Object> temp = messageList.get(i);
    			
    			//取得消息体
    			String messageBody = (String)temp.get("messageBody");
    			String messageBody_temp = "";
    			char[] messageBodyArr = messageBody.toCharArray();
    			//取得消息体的长度
    			int  messageBodyLength = messageBodyArr.length;
    			//
    			int count = 0;
    			label2:
    			for(int j = 0 ; j < messageBodyLength ; j++){
    				
    				//控制在30个字节长度之内
    				if(count > 30){
    					messageBody_temp = messageBody.substring(0, j)+" ...";
    					break label2;
    				}
    				//如果是汉字则加2，否则加1
    				if(messageBodyArr[j] >= 0x0391 && messageBodyArr[j] <= 0xFFE5){
    					count += 2;
    				}else{
    					count ++;
    				}
    			}
    			
    			if("".equals(messageBody_temp)){
    				messageBody_temp = messageBody;
    			}
    			
    			temp.put("messageBody_temp", messageBody_temp);
    		}
    	}
    	
        return messageList;
    }
    
    @Override
    /**
     * 新增柜台消息
     * 
     * @param map
     */
    public void tran_addCounterMessage(Map<String, Object> map) throws Exception {
        binOLMOCIO01_Service.addCounterMessage(map);
    }

    @Override
    /**
     * 编辑柜台消息（排他）
     * 
     * @param map
     * @return 
     */
    public int tran_modifyCounterMessage(Map<String, Object> map) throws Exception {
        int count = binOLMOCIO01_Service.modifyCounterMessage(map);
        if(count<1){
            throw new CherryException("ECM00038");
        }
        return count;
    }
    
    @Override
    /**
     * 删除柜台消息
     * 
     * @param map
     * @return 
     */
    public int tran_deleteCounterMessage(Map<String, Object> map) throws Exception {
        return binOLMOCIO01_Service.deleteCounterMessage(map);
    }

    @Override
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    public Map<String, Object> getCounterMessage(Map<String, Object> map) {
        return binOLMOCIO01_Service.getCounterMessage(map);
    }

	@Override
	/**
	 * 取得已下发的消息的下发柜台及其所属区域
	 * 
	 * */
	public List<Map<String, Object>> getMessageRegion(Map<String, Object> map) {
		
		//取得该柜台消息的下发类型
		int messageId = Integer.valueOf((String)map.get("BIN_CounterMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO01_Service.getControlFlag(messageId);
		if(null == controlFlagList || controlFlagList.isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer)controlFlagList.get(0).get("ControlFlag");
		
		List<Map<String,Object>> issumList = binOLMOCIO01_Service.getMessageRegion(map);
		
		if(null == issumList || issumList.isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		
		//存放消息下发禁止表中存在柜台信息
		List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
		//存放消息下发禁止表中不存在柜台信息
		List<Map<String,Object>> list2 = new ArrayList<Map<String, Object>>();
		
		int size = issumList.size();
		for(int i = 0 ; i < size ; i++){
			Map<String,Object> temp = issumList.get(i);
			
			if(null == temp.get("B_BIN_OrganizationID") || "".equals(temp.get("B_BIN_OrganizationID"))){
				list2.add(temp);
			}else{
				list1.add(temp);
			}
		}
		
		//存放消息允许的柜台
		List<Map<String,Object>> allowList = new ArrayList<Map<String, Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "A_BIN_RegionID", "RegionName" };
		String[] keys2 = { "A_BIN_ProvinceID", "ProvinceName" };
		String[] keys3 = { "A_BIN_CityID", "CityName" };
		String[] keys5 = { "A_BIN_OrganizationID", "A_DepartName"};
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys5);
		
		if(1 == controlFlag){
			ConvertUtil.jsTreeDataDeepList(list1, allowList, keysList, 0);
		}else if(2 == controlFlag){
			ConvertUtil.jsTreeDataDeepList(list2, allowList, keysList, 0);
		}else{
			return new ArrayList<Map<String, Object>>();
		}
		
		return allowList;
	}
	
	/**
	 * 取得大区信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		return binOLMOCIO01_Service.getRegionList(map);
	}
	
	/**
	 * 取得已下发的消息的下发柜台及其所属渠道
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getMessageChannel(Map<String, Object> map) {
		// 取得该柜台消息的下发类型
		int messageId = Integer.valueOf((String) map
				.get("BIN_CounterMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO01_Service
				.getControlFlag(messageId);
		if (null == controlFlagList || controlFlagList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer) controlFlagList.get(0).get("ControlFlag");
		List<Map<String, Object>> issumList = binOLMOCIO01_Service
				.getMessageChannel(map);
		if (null == issumList || issumList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}

		// 存放消息下发禁止表中存在柜台信息
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		// 存放消息下发禁止表中不存在柜台信息
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

		int size = issumList.size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> temp = issumList.get(i);

			if (null == temp.get("B_BIN_OrganizationID")
					|| "".equals(temp.get("B_BIN_OrganizationID"))) {
				list2.add(temp);
			} else {
				list1.add(temp);
			}
		}
		// 存放消息允许的柜台
		List<Map<String, Object>> allowList = new ArrayList<Map<String, Object>>();
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "A_BIN_OrganizationID", "departName" };
		keysList.add(keys1);
		keysList.add(keys2);

		if (1 == controlFlag) {
			ConvertUtil.jsTreeDataDeepList(list1, allowList, keysList, 0);
		} else if (2 == controlFlag) {
			ConvertUtil.jsTreeDataDeepList(list2, allowList, keysList, 0);
		} else {
			return new ArrayList<Map<String, Object>>();
		}

		return allowList;
	}

	/**
	 * 取得已下发的消息的下发柜台及其所属组织结构
	 */
	@Override
	public List<Map<String, Object>> getMessageOrganize(Map<String, Object> map) {
		// 取得该柜台消息的下发类型
		int messageId = Integer.valueOf((String) map
				.get("BIN_CounterMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO01_Service
				.getControlFlag(messageId);
		if (null == controlFlagList || controlFlagList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer) controlFlagList.get(0).get("ControlFlag");
		List<Map<String, Object>> issumList = binOLMOCIO01_Service
				.getMessageOrganize(map);
		if (null == issumList || issumList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}

		// 存放要显示的柜台信息
		List<Map<String, Object>> allowList = new ArrayList<Map<String, Object>>();
		// 将线性的结构转化为树结构
		allowList = ConvertUtil.getTreeList(issumList, "nodes");
		if (1 == controlFlag || 2 == controlFlag) {
			// 消息下发禁止表中【存在：1 == controlFlag】或者【不存在：2 == controlFlag】柜台信息
			getTargetOrganize(allowList, controlFlag);
		} else {
			return new ArrayList<Map<String, Object>>();
		}

		return allowList;
	}

	/**
	 * 取得要显示的柜台的组织结构
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean getTargetOrganize(List<Map<String, Object>> list,
			int controlFlag) {
		boolean flag = false;
		// 存在要显示的个数
		int exist = 0;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (map.containsKey("nodes")) {
				List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map
						.get("nodes");
				if (!getTargetOrganize(nodesList, controlFlag)) {// 不存在要显示的节点
					list.remove(map);
					i--;
				} else {
					exist++;
				}
			} else {// 柜台结点
				if (1 == controlFlag) {// 消息下发禁止表中【存在】柜台信息
					if (CherryChecker.isNullOrEmpty(map
							.get("B_BIN_OrganizationID"))) {
						// 此柜台不用于显示
						list.remove(map);
						i--;
					} else {
						exist++;
					}
				} else if (2 == controlFlag) {// 消息下发禁止表中【不存在】柜台信息
					if (!CherryChecker.isNullOrEmpty(map
							.get("B_BIN_OrganizationID"))) {
						// 此柜台不用于显示
						list.remove(map);
						i--;
					} else {
						exist++;
					}
				}
			}
			// 存在要显示的组织
			if (exist > 0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public int tran_disableOrEnable(Map<String, Object> map) throws Exception {
		int result = binOLMOCIO01_Service.disableOrEnable(map);
		if(result < 1){
            throw new CherryException("EMO00080");
        } else {
        	counterMessageSynchro.updCounterMessage(map);
        }
		return result;
	}
	
}
