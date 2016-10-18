/*
 * @(#)BINOLPLSCF01_Action.java     1.0 2010/10/27
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

package com.cherry.pl.scf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.pl.scf.bl.BINOLPLSCF01_BL;
import com.cherry.pl.scf.form.BINOLPLSCF01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 基本配置管理Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF01_Action extends BaseAction implements ModelDriven<BINOLPLSCF01_Form> {
	
	private static final long serialVersionUID = -7697985562316587723L;
	
	/** 基本配置管理Form */
	private BINOLPLSCF01_Form form = new BINOLPLSCF01_Form();
	
	/** 基本配置管理BL */
	@Resource
	private BINOLPLSCF01_BL binOLPLSCF01_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**
	 * 基本配置管理画面初期化
	 * 
	 * @return 基本配置管理画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		List<Map<String,Object>> brandInfoList = new ArrayList<Map<String,Object>>();
		//admin登录 场合
		if(isAdmin()){
            Map mapParam = new HashMap();
            mapParam.put(CherryConstants.BRANDINFOID,CherryConstants.BRAND_INFO_ID_VALUE);
            mapParam.put(CherryConstants.ORGANIZATIONINFOID,CherryConstants.ORGANIZATION_CODE_DEFAULT);
            // 取得基本配置信息List【新增的系统配置项是通过脚本直接写入数据库的】
            List<Map<String, Object>> systemConfigList = binOLPLSCF01_BL.getAdminSystemConfigList(mapParam);
            form.setSystemConfigList(systemConfigList);
		}else{
		      // 总部用户的场合
	        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
	            // 所属品牌
	            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	            // 取得品牌List
	            brandInfoList = binOLCM05_BL.getBrandInfoList(map);
	        } else {
	            Map<String, Object> brandMap = new HashMap<String, Object>();
	            // 品牌ID
	            brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	            // 品牌名称
	            brandMap.put("brandName", userInfo.getBrandName());
	            brandInfoList.add(brandMap);
	        }
//	          Map<String, Object> brandMap = new HashMap<String, Object>();
//	          // 品牌ID
//	          brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
//	          // 品牌名称
//	          brandMap.put("brandName", getText("PPL00006"));
//	          if (null != brandInfoList && !brandInfoList.isEmpty()) {
//	              brandInfoList.add(0, brandMap);
//	          } else {
//	              brandInfoList = new ArrayList<Map<String, Object>>();
//	              brandInfoList.add(brandMap);
//	          }
	        
	        // 所属品牌
	        if (brandInfoList!=null && !brandInfoList.isEmpty()){
	            map.put(CherryConstants.BRANDINFOID, brandInfoList.get(0).get(CherryConstants.BRANDINFOID));
	            // 取得基本配置信息List【此处取的基本配置是以共通系统配置为基础的数据】
	            List<Map<String,Object>> systemConfigList = binOLPLSCF01_BL.getSystemConfigList(map);
		        // 取不到基本配置信息的场合
//		        if(systemConfigList == null || systemConfigList.isEmpty()) {
//			        // 作成者
//			        map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
//			        // 更新者
//			        map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
//			        // 给品牌添加默认的基本配置信息
//			        binOLPLSCF01_BL.tran_addSystemConfig(map);
//			        // 重新取得基本配置信息List
//			        systemConfigList = binOLPLSCF01_BL.getSystemConfigList(map);
//		        }
	            
	            form.setBrandInfoList(brandInfoList);
	            form.setSystemConfigList(systemConfigList);
	        }
		}

		return SUCCESS;
	}
	
	/**
	 * 通过品牌ID取得基本配置信息
	 * 
	 * @return 基本配置管理画面
	 */
	public String searchBsCf() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得基本配置信息List
		List<Map<String,Object>> systemConfigList = binOLPLSCF01_BL.getSystemConfigList(map);
//		// 取不到基本配置信息的场合
//		if(systemConfigList == null || systemConfigList.isEmpty()) {
//			// 作成者
//			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
//			// 更新者
//			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
//			// 给品牌添加默认的基本配置信息
//			binOLPLSCF01_BL.tran_addSystemConfig(map);
//			// 重新取得基本配置信息List
//			systemConfigList = binOLPLSCF01_BL.getSystemConfigList(map);
//		}
		
		form.setSystemConfigList(systemConfigList);
		return SUCCESS;
	}
	
	/**
	 * 保存基本配置信息处理
	 * 
	 * @return 基本配置管理画面
	 * @throws Exception 
	 */
	public String saveBsCf() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 去除空值
		map = CherryUtil.removeEmptyVal(map);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
	    // 创建者
        map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
        // 创建程序名
        map.put(CherryConstants.CREATEPGM, "BINOLPLSCF01");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF01");
		try {
			// 更新基本配置信息
			//binOLPLSCF01_BL.tran_updateSystemConfig(map);
		    
		    //Admin登录
		    if(isAdmin()){
		        //插入默认配置信息
		        binOLPLSCF01_BL.tran_updateSystemConfig2(map);
		    }else{
		        //插入基本配置信息
	            binOLPLSCF01_BL.tran_insertSystemConfig2(map); 
		    }
		    if("1334".equals(form.getConfigCode()) || "1346".equals(form.getConfigCode())){
		    	map.put("codeType", form.getConfigCode());
		    	binOLPLSCF01_BL.tran_insertSchedules(map);
		    	String brandCode=userInfo.getBrandCode();
		    	// 发送MQ
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 品牌代码
				mqInfoDTO.setBrandCode(brandCode);
				
				String billType = CherryConstants.MESSAGE_TYPE_RT;
				
				String billCode = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID(), "", billType);
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYSCHEDULETASKMSGQUEUE);
				
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_GT);
				// 设定消息命令类型
				msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1006);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 业务类型
				mainData.put("TradeType", billType);
				// 单据号
				mainData.put("TradeNoIF", billCode);
				dataLine.put("MainData", mainData);
				msgDataMap.put("DataLine", dataLine);
				mqInfoDTO.setMsgDataMap(msgDataMap);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
		    }

		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	@Override
	public BINOLPLSCF01_Form getModel() {
		return form;
	}
	
	private boolean isAdmin(){
	       // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE && CherryConstants.ORGANIZATION_CODE_DEFAULT.equals(String.valueOf(userInfo.getBIN_OrganizationInfoID()))){
            return true;
        }else{
            return false;
        }
	}

}
