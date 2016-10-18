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
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO23_IF;
import com.cherry.mo.cio.service.BINOLMOCIO23_Service;



/**
 * 
 * 柜台消息管理BL
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO23_BL implements BINOLMOCIO23_IF {
    
    @Resource
    private BINOLMOCIO23_Service binOLMOCIO23_Service;
    
    
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
        return binOLMOCIO23_Service.getDepartmentMessageCount(map);
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
    	List<Map<String, Object>> messageList = binOLMOCIO23_Service.getDepartmentMessageList(map);    	
        return messageList;
    }
    


    @Override
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    public Map<String, Object> getDepartmentMessage(Map<String, Object> map) {
        return binOLMOCIO23_Service.getDepartmentMessage(map);
    }


	
}
