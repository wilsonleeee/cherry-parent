/*		
 * @(#)BINOLCM14_BL.java     1.0 2011/03/29		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.service.BINOLCM14_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 系统配置项 共通
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM14_BL {
	
	@Resource
	private BINOLCM14_Service binOLCM14_Service;

	
	/**
	 * 指定code查询系统配置项的值
	 * @param map
	 * @return
	 */
	public String getConfigValue(String code, String organizationInfoID,String brandInfoID){
		String ret="";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ConfigCode", code);
		map.put("BIN_OrganizationInfoID", organizationInfoID);
		map.put("BIN_BrandInfoID", brandInfoID);
		List<Map<String, Object>> list = binOLCM14_Service.getConfigValue(map);
		
		if(list!=null&&list.size()>0){
			ret = String.valueOf(list.get(0).get("ConfigValue"));
			return ret;
		}
		map.put("BIN_OrganizationInfoID", "-9999");
		map.put("BIN_BrandInfoID", "-9999");
		list = binOLCM14_Service.getConfigValue(map);
		if(list!=null&&list.size()>0){
			ret = String.valueOf(list.get(0).get("ConfigValue"));			
		}
		return ret;
	}
	

	
	/**
	 * 指定code查询该配置项是禁用还是启用
	 * 仅限 开关类的配置项
	 * @param code
	 * @param userInfo
	 * @return
	 */
	 public boolean isConfigOpen(String code,String organizationInfoID,String brandInfoID){
		String ret =  getConfigValue(code,organizationInfoID,brandInfoID);
		
		//默认的情况下，开关类配置项 1 表示可以，0表示不可以
		//此处使用默认的值做判断，有特别情况，可以自行在此处添加代码
		if(CherryConstants.SYSTEM_CONFIG_ENABLE.equals(ret)){
			return true;
		}		
		return false;
	}
		/**
		 * 指定code查询该配置项是禁用还是启用
		 * 仅限 开关类的配置项
		 * @param code
		 * @param userInfo
		 * @return
		 */
		 public boolean isConfigOpen(String code,int organizationInfoID,int brandInfoID){
			return isConfigOpen(code,String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
		}
	/**
	 * 查询单据的审核功能是否开启
	 * 
	 * @param code
	 * @param userInfo
	 * @return
	 */
	 public boolean isAuditOpen(String code,String value,String organizationInfoID,String brandInfoID){
		String ret="";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ConfigCode", code);
		map.put("ConfigValue", value);
		map.put("BIN_OrganizationInfoID", organizationInfoID);
		map.put("BIN_BrandInfoID", brandInfoID);
		List<Map<String, Object>> list = binOLCM14_Service.getConfigValueEfficient(map);
		
		if(list!=null&&list.size()>0){
			ret = String.valueOf(list.get(0).get("ConfigEfficient"));
			
		}else{
			map.put("BIN_OrganizationInfoID", "-9999");
			map.put("BIN_BrandInfoID", "-9999");
			list = binOLCM14_Service.getConfigValueEfficient(map);
			if(list!=null&&list.size()>0){
				ret = String.valueOf(list.get(0).get("ConfigEfficient"));			
			}
		}

		if("1".equals(ret)){
			return true;
		}		
		return false;
	}
	 
		/**
		 * 查询单据的审核功能是否开启
		 * 
		 * @param code
		 * @param userInfo
		 * @return
		 */
		 public boolean isAuditOpen(String code,String value, int organizationInfoID,int brandInfoID){
			return isAuditOpen(code,value,String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
		}
			/**
			 * 查询系统配置中设定的指定业务任务的（参与者）
			 * @param businessType 业务类型
			 * @param userInfo 用户信息
			 */
			public String[] getActors(String businessType,UserInfo userInfo){
				
				HashMap<String,Object> map = new HashMap<String,Object>();
				//取得审核人
				map.put("BussinessTypeCode", businessType);
				map.put("InitiatorType3", CherryConstants.JBPM_INITIATOR_ORG_CODE);
				map.put("InitiatorID3", userInfo.getCurrentOrganizationID());
				map.put("InitiatorType2", CherryConstants.JBPM_INITIATOR_POS_CODE);
				map.put("InitiatorID2", userInfo.getBIN_PositionCategoryID());
				map.put("InitiatorType1", CherryConstants.JBPM_INITIATOR_PER_CODE);
				map.put("InitiatorID1", userInfo.getBIN_UserID());
				map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
				map.put("BIN_BrandInfoID", userInfo.getCurrentBrandInfoID());
				
				// 查询审核人
				List list = binOLCM14_Service.getActorsAllType(map);
				String[] arr = new String[list.size()];
				String auditorType;
				for (int i = 0;i<list.size();i++){
					HashMap temp = (HashMap)list.get(i);
					auditorType=String.valueOf(temp.get("AuditorType"));
					if(CherryConstants.JBPM_AUDITOR_PER_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(temp.get("AuditorID"));	
					}else if(CherryConstants.JBPM_AUDITOR_POS_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_POSITION+String.valueOf(temp.get("AuditorID"));	
					}else if(CherryConstants.JBPM_AUDITOR_ORG_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_DEPART+String.valueOf(temp.get("AuditorID"));	
					}						
				}
				return arr;
			}
			
			/**
			 * 查询系统配置中设定的指定业务任务的（参与者），返回值的形式是逗号分隔的长字符串
			 * @param businessType 业务类型
			 * @param userInfo 用户信息
			 */
			public String getActorsString(String businessType,UserInfo userInfo){
				
				HashMap<String,Object> map = new HashMap<String,Object>();
				//取得审核人
				map.put("BussinessTypeCode", businessType);
				map.put("InitiatorType3", CherryConstants.JBPM_INITIATOR_ORG_CODE);
				map.put("InitiatorID3", userInfo.getCurrentOrganizationID());
				map.put("InitiatorType2", CherryConstants.JBPM_INITIATOR_POS_CODE);
				map.put("InitiatorID2", userInfo.getBIN_PositionCategoryID());
				map.put("InitiatorType1", CherryConstants.JBPM_INITIATOR_PER_CODE);
				map.put("InitiatorID1", userInfo.getBIN_UserID());
				map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
				map.put("BIN_BrandInfoID", userInfo.getCurrentBrandInfoID());
				
				// 查询审核人
				List list = binOLCM14_Service.getActorsAllType(map);
				
				String auditorType;
				StringBuffer ret= new StringBuffer();
				ret.append("");
				for (int i = 0;i<list.size();i++){
					HashMap temp = (HashMap)list.get(i);
					auditorType=String.valueOf(temp.get("AuditorType"));
					if(ret.length()>0){
						ret.append(",");
					}
					if(CherryConstants.JBPM_AUDITOR_PER_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_USER);
					}else if(CherryConstants.JBPM_AUDITOR_POS_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_POSITION);
					}else if(CherryConstants.JBPM_AUDITOR_ORG_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_DEPART);
					}	
					ret.append(String.valueOf(temp.get("AuditorID")));
				}
				return ret.toString();
			}

			/**
			 *  查询系统配置中设定的指定业务任务的（参与者），返回值的形式是逗号分隔的长字符串
			 * @param pramMap
			 * @return
			 */
			public String getActorsString(Map<String,Object> pramMap){
				
				HashMap<String,Object> map = new HashMap<String,Object>();
				//取得审核人
				map.put("BussinessTypeCode", pramMap.get(CherryConstants.OS_MAINKEY_CURRENT_OPERATE));
				map.put("InitiatorType3", CherryConstants.JBPM_INITIATOR_ORG_CODE);
				map.put("InitiatorID3", pramMap.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
				map.put("InitiatorType2", CherryConstants.JBPM_INITIATOR_POS_CODE);
				map.put("InitiatorID2", pramMap.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
				map.put("InitiatorType1", CherryConstants.JBPM_INITIATOR_PER_CODE);
				map.put("InitiatorID1", pramMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
				//组织信息ID没什么用，品牌ID就已经足够
				map.put("BIN_OrganizationInfoID", pramMap.get("BIN_OrganizationInfoID"));
				map.put("BIN_BrandInfoID", pramMap.get("BIN_BrandInfoID"));
				
				// 查询审核人
				List list = binOLCM14_Service.getActorsAllType(map);
				
				String auditorType;
				StringBuffer ret= new StringBuffer();
				ret.append("");
				for (int i = 0;i<list.size();i++){
					HashMap temp = (HashMap)list.get(i);
					auditorType=String.valueOf(temp.get("AuditorType"));
					if(ret.length()>0){
						ret.append(",");
					}
					if(CherryConstants.JBPM_AUDITOR_PER_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_USER);
					}else if(CherryConstants.JBPM_AUDITOR_POS_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_POSITION);
					}else if(CherryConstants.JBPM_AUDITOR_ORG_CODE.equals(auditorType)){
						ret.append(CherryConstants.OS_ACTOR_TYPE_DEPART);
					}	
					ret.append(String.valueOf(temp.get("AuditorID")));
				}
				return ret.toString();
			}
			/**
			 * 查询任务的审核者（参与者）
			 * @param businessType
			 * @param userInfo
			 * @param defaultActors 默认的参与者
			 * @return
			 */
			public String[] getActors(String businessType,UserInfo userInfo,String[] defaultActors){
				
				HashMap<String,Object> map = new HashMap<String,Object>();
				//取得发货单审核人
				map.put("BussinessTypeCode", businessType);
				map.put("InitiatorType3", CherryConstants.JBPM_INITIATOR_ORG_CODE);
				map.put("InitiatorID3", userInfo.getCurrentOrganizationID());
				map.put("InitiatorType2", CherryConstants.JBPM_INITIATOR_POS_CODE);
				map.put("InitiatorID2", userInfo.getBIN_PositionCategoryID());
				map.put("InitiatorType1", CherryConstants.JBPM_INITIATOR_PER_CODE);
				map.put("InitiatorID1", userInfo.getBIN_UserID());
				
				// 查询审核人
				List list = binOLCM14_Service.getActorsAllType(map);
				if(list==null||list.size()==0){
					return defaultActors;
				}
				
				String[] arr = new String[list.size()];
				String auditorType;
				for (int i = 0;i<list.size();i++){
					HashMap temp = (HashMap)list.get(i);
					auditorType=String.valueOf(temp.get("AuditorType"));
					if(CherryConstants.JBPM_AUDITOR_PER_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(temp.get("AuditorID"));	
					}else if(CherryConstants.JBPM_AUDITOR_POS_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_POSITION+String.valueOf(temp.get("AuditorID"));	
					}else if(CherryConstants.JBPM_AUDITOR_ORG_CODE.equals(auditorType)){
						arr[i]=CherryConstants.OS_ACTOR_TYPE_DEPART+String.valueOf(temp.get("AuditorID"));	
					}					
				}
				return arr;
			}

}
