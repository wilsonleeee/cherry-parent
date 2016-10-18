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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO01_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO21_IF;
import com.cherry.mo.cio.service.BINOLMOCIO01_Service;
import com.cherry.mo.cio.service.BINOLMOCIO21_Service;
import com.cherry.synchro.mo.interfaces.CounterMessageSynchro_IF;
import com.cherry.synchro.mo.service.CounterMessageSynchroService;
import com.cherry.webservice.client.WebserviceClient;


/**
 * 
 * 柜台消息管理BL
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO21_BL implements BINOLMOCIO21_IF {
    
    @Resource
    private BINOLMOCIO21_Service binOLMOCIO21_Service;
    
    @Resource
    private CounterMessageSynchro_IF counterMessageSynchro;
    
    /**
     * 上传文件
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, Object> parseFileNew(File file,UserInfo userInfo,Map<String, Object> map) throws Exception{
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
//			String filePath = "D:\\pic.jpg";
//			File file = new File(filePath);
    		if (!file.exists() || !file.isFile()) {
    			throw new Exception("要上传的文件不存在");
    		}
    		
    		String upExcelFileName = ConvertUtil.getString(map.get("upExcelFileName"));
    		String attachmentType = upExcelFileName.substring(upExcelFileName.lastIndexOf(".")+1).toUpperCase();
    		if ("PDF".equals(attachmentType)){
    			long fileSize = file.length();
    			long maxPdfSize = 10 * 1024 * 1024; // 字节
    			if(fileSize > maxPdfSize){
    				throw new Exception("pdf文件最大支持10MB");
    			}
    		}else if ("MP4".equals(attachmentType)){
    			long fileSize = file.length();
    			long maxMp4Size = 100 * 1024 * 1024; // 字节(MB*KB*B)
    			if(fileSize > maxMp4Size){
    				throw new Exception("mp4文件最大支持100MB");
    			}
    		}
    		
    		Map<String, Object> param = new HashMap<String, Object>();
    		// 拼接调用WebService参数
    		Map<String, Object> wsParam = new HashMap<String, Object>();
    		wsParam.put("TradeType", "UploadFile");
    		wsParam.put("SubTradeType", "cntMsg");
    		wsParam.put("OriginalFileName", map.get("upExcelFileName"));

    		wsParam.put("OriginalFileType", attachmentType);
    		wsParam.put("EmployeeCode", userInfo.getEmployeeCode());
    		wsParam.put("DepartCode", userInfo.getDepartCode());
//    		wsParam.put("DepartCode", "0010004s");
    		
    		param.put("wsParam", wsParam);
    		param.put("AESKEY", PropertiesUtil.pps.getProperty("PekonWS_AESKEY"));
    		param.put("AppID", PropertiesUtil.pps.getProperty("PekonWS_AppID"));
    		
//    		resultMap = WebserviceClient.accessBatchWebServiceFile(param, file);
    		resultMap.put("attachmentType", attachmentType);
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		resultMap.put("ERRORCODE","WSE0077");
    		resultMap.put("ERRORMSG",ex.getMessage());
    	}
    	
    	return resultMap;
    }
    
    @Override
    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getDepartmentMessageCount(Map<String, Object> map) {
        return binOLMOCIO21_Service.getDepartmentMessageCount(map);
    }

    @Override
    /**
     * 取得柜台消息List
     * 
     * @param map
     * @return 柜台消息List
     */
    public List<Map<String, Object>> getDepartmentMessageList(
            Map<String, Object> map) {
    	List<Map<String, Object>> messageList = binOLMOCIO21_Service.getDepartmentMessageList(map);    	
        return messageList;
    }
    
    @Override
    /**
     * 新增柜台消息
     * 
     * @param map
     */
    public void tran_addDepartmentMessage(Map<String, Object> map) throws Exception {
        binOLMOCIO21_Service.addDepartmentMessage(map);
    }

    @Override
    /**
     * 编辑柜台消息（排他）
     * 
     * @param map
     * @return 
     */
    public int tran_modifyDepartmentMessage(Map<String, Object> map) throws Exception {
        int count = binOLMOCIO21_Service.modifyDepartmentMessage(map);
//        if(count<1){
//            throw new CherryException("ECM00038");
//        }
        return count;
    }
    
    @Override
    /**
     * 删除柜台消息
     * 
     * @param map
     * @return 
     */
    public int tran_deleteDepartmentMessage(Map<String, Object> map) throws Exception {
        return binOLMOCIO21_Service.deleteDepartmentMessage(map);
    }

    @Override
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    public Map<String, Object> getDepartmentMessage(Map<String, Object> map) {
        return binOLMOCIO21_Service.getDepartmentMessage(map);
    }

	@Override
	/**
	 * 取得已下发的消息的下发柜台及其所属区域
	 * 
	 * */
	public List<Map<String, Object>> getMessageRegion(Map<String, Object> map) {
		
		//取得该柜台消息的下发类型
		int messageId = Integer.valueOf((String)map.get("BIN_DepartmentMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO21_Service.getControlFlag(messageId);
		if(null == controlFlagList || controlFlagList.isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer)controlFlagList.get(0).get("ControlFlag");
		
		List<Map<String,Object>> issumList = binOLMOCIO21_Service.getMessageRegion(map);
		
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
		return binOLMOCIO21_Service.getRegionList(map);
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
				.get("BIN_DepartmentMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO21_Service
				.getControlFlag(messageId);
		if (null == controlFlagList || controlFlagList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer) controlFlagList.get(0).get("ControlFlag");
		List<Map<String, Object>> issumList = binOLMOCIO21_Service
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
				.get("BIN_DepartmentMessageID"));
		List<Map<String, Object>> controlFlagList = binOLMOCIO21_Service
				.getControlFlag(messageId);
		if (null == controlFlagList || controlFlagList.isEmpty()) {
			return new ArrayList<Map<String, Object>>();
		}
		int controlFlag = (Integer) controlFlagList.get(0).get("ControlFlag");
		List<Map<String, Object>> issumList = binOLMOCIO21_Service
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
		int result = binOLMOCIO21_Service.disableOrEnable(map);
		if(result < 1){
            throw new CherryException("EMO00080");
        } else {
//        	counterMessageSynchro.updCounterMessage(map);
        }
		return result;
	}
	
}
