/*  
 * @(#)BINOLCM09_BL.java     1.0 2011/05/31      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM09_Service;
import com.cherry.ss.common.PromotionConstants;

@SuppressWarnings("unchecked")
public class BINOLCM89_BL {
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM09_Service binOLCM09_Service;
	
	/**
	 * 查询活动信息数据(ActivityAssociateTable_CHY数据)
	 * @param map
	 */
	public void getActiveInfoData(Map<String, Object> map){
		// 查询活动信息
		List actInfoList = binOLCM09_Service.getActiveInfoList(map);
		for (int i = 0;i<actInfoList.size();i++){
			HashMap actInfoMap = (HashMap)actInfoList.get(i);
			// 设定品牌Code
			HashMap brandInfoMap = binOLCM09_Service.getBrandCode(actInfoMap);
			actInfoMap.put("brandCode", brandInfoMap.get("actBrandCode"));
		}
		map.put("activityAssociateTableCHYList", actInfoList);
	}
	
	/**
	 * 取得活动数据(ActivityTable_CHY数据)
	 * @param map
	 */
	public void getActiveData (Map<String, Object> map){		
		// 查询活动条件和结果
		List actConResultList = binOLCM09_Service.getActConResultList(map);
		// 遍历活动条件结果List
		for (int i=0;i<actConResultList.size();i++){
			HashMap actConResultMap = (HashMap)actConResultList.get(i);
			// 市ID
			String cityID = (String)(actConResultMap.get("cityID"));
			// 渠道ID
			String channelID = (String)(actConResultMap.get("channelID"));
			// 柜台ID
			String counterID = (String)(actConResultMap.get("counterID"));
			if (cityID!=null && !"".equals(cityID) && counterID == null){
				map.put("cityID", cityID);
				List resultList = binOLCM09_Service.getCounterByIdCity(map);
				actConResultList.remove(actConResultMap);
				for (int j = 0;j<resultList.size();j++){
					HashMap resultMap  = (HashMap)resultList.get(j);
					HashMap actConResultMapNew  = new HashMap();
					actConResultMapNew.putAll(actConResultMap);
					actConResultMapNew.put("counterID", resultMap.get("counterID"));
					actConResultList.add(actConResultMapNew);
				}
				i--;
			}
			if (channelID!=null && !"".equals(channelID) && counterID == null){
				// 根据渠道ID查询柜台
				map.put("channelID", channelID);
				List resultList = binOLCM09_Service.getCounterByIdChannel(map);
				actConResultList.remove(actConResultMap);
				for (int j = 0;j<resultList.size();j++){
					HashMap resultMap  = (HashMap)resultList.get(j);
					HashMap actConResultMapNew  = new HashMap();
					actConResultMapNew.putAll(actConResultMap);
					actConResultMapNew.put("counterID", resultMap.get("counterID"));
					actConResultList.add(actConResultMapNew);
				}
				i--;
			}
		}	
		
		List actConResultNewList = new ArrayList();
		// 将礼品和折扣拆成2条记录
		for (int i=0;i<actConResultList.size();i++){
			HashMap actConResultMap = (HashMap)actConResultList.get(i);
			// 品牌Code
			actConResultMap.put("brandCode", map.get("brandCode"));
			// 活动名
			actConResultMap.put("mainName", map.get("mainName"));
			if (actConResultMap.get("price")!=null && !PromotionConstants.PROMOTION_TZZK_UNIT_CODE.equals(actConResultMap.get("promotionPrtCode"))){
				// 取得活动编码
				String code = binOLCM03_BL.getTicketNumber(String.valueOf(map.get("bin_OrganizationInfoID")), String.valueOf(map.get("brandInfoID")), "", "9");
				actConResultMap.put("activeCode", code.substring(4, 10)+code.substring(code.length()-4, code.length()));
				HashMap actConResultNewMap = new HashMap();
				actConResultNewMap.putAll(actConResultMap);
				// 礼品促销
				actConResultNewMap.put("promotionPrtCode", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
				// 取得活动编码
				code = binOLCM03_BL.getTicketNumber(String.valueOf(map.get("bin_OrganizationInfoID")), String.valueOf(map.get("brandInfoID")), "", "9");
				actConResultNewMap.put("activeCode", code.substring(4, 10)+code.substring(code.length()-4, code.length()));
				// 活动数量默认为1
				actConResultNewMap.put("activityQty", "1");
				actConResultNewList.add(actConResultNewMap);
				actConResultMap.put("price", "0");
				actConResultNewList.add(actConResultMap);
			}else{
				// 取得活动编码
				String code = binOLCM03_BL.getTicketNumber(String.valueOf(map.get("bin_OrganizationInfoID")), String.valueOf(map.get("brandInfoID")), "", "9");
				actConResultMap.put("activeCode", code.substring(4, 10)+code.substring(code.length()-4, code.length()));
				// 如果是促销礼品,则将价格设为0
				if (!PromotionConstants.PROMOTION_TZZK_UNIT_CODE.equals(actConResultMap.get("promotionPrtCode"))){
					actConResultMap.put("price", "0");
				}else{
					actConResultMap.put("activityQty", "1");
				}
				actConResultNewList.add(actConResultMap);
			}
		}
		
		map.put("activityTableCHYList", actConResultNewList);
	}
	
	/**
	 * 设定促销活动基础属性值
	 * @param map
	 */
	public HashMap setBaseProp (Map<String, Object> map){
		HashMap basePropKeyMap = new HashMap();
		List basePropList = binOLCM09_Service.getPrmBasePropInfoList(map);
		for (int i = 0; i < basePropList.size(); i++) {
			HashMap basePropMap = (HashMap) basePropList.get(i);
			String propertyName = (String) basePropMap.get("propertyName");
			// 基础规则属性 -- 时间
			if (propertyName.equals(PromotionConstants.BASE_PROP_TIME)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_TIME, basePropMap.get("bin_PromotionBasePropID"));
			}
			// 基础规则属性 -- 区域市
			if (propertyName.equals(PromotionConstants.BASE_PROP_CITY)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_CITY, basePropMap.get("bin_PromotionBasePropID"));
			}
			// 基础规则属性 -- 渠道
			if (propertyName.equals(PromotionConstants.BASE_PROP_CHANNEL)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_CHANNEL, basePropMap.get("bin_PromotionBasePropID"));
			}
			// 基础规则属性 -- 柜台
			if (propertyName.equals(PromotionConstants.BASE_PROP_COUNTER)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_COUNTER, basePropMap.get("bin_PromotionBasePropID"));
			}
			// 基础规则属性 -- 系统
			if (propertyName.equals(PromotionConstants.BASEPROP_FACTION)) {
				basePropKeyMap.put(PromotionConstants.BASEPROP_FACTION, basePropMap.get("bin_PromotionBasePropID"));
			}
			// 基础规则属性 -- 组织
			if (propertyName.equals(PromotionConstants.BASE_PROP_ORGANIZATION)) {
				basePropKeyMap.put(PromotionConstants.BASE_PROP_ORGANIZATION, basePropMap.get("bin_PromotionBasePropID"));
			}
		}
		return basePropKeyMap;
	}
	
	/**
	 * 将促销活动下发给终端接口表
	 * @param map
	 * @throws Exception 
	 */
	public void tran_publicProActive (Map<String, Object> map) throws Exception{
		// 查询规则基础属性
		HashMap basePropKeyMap  = setBaseProp(map);
		// 查询活动信息数据(ActivityAssociateTable_CHY数据)
		getActiveInfoData(map);
		List activityList  = (List)map.get("activityAssociateTableCHYList");
		for (int i=0;i<activityList.size();i++){
			HashMap activityMap = (HashMap)activityList.get(i);
//			binOLCM09_Service.delActivityAssociateTable_CHY(activityMap);
//			binOLCM09_Service.delActivityTable_CHY(activityMap);
			// 将基础属性设定到活动Map中
			activityMap.putAll(basePropKeyMap);			
			// 取得活动数据(ActivityTable_CHY数据)
			getActiveData(activityMap);
			// 下发促销活动
//			binOLCM09_Service.addActivityAssociateTable_CHY(activityMap);
//			binOLCM09_Service.addActivityTable_CHY(activityMap);
		}
	}
}
