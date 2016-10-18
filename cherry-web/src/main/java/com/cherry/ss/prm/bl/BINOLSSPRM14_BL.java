/*		
 * @(#)BINOLSSPRM14_BL.java     1.0 2010/11/10		
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.activemq.bl.BINOLMQCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.service.BINOLSSPRM14_Service;
@SuppressWarnings("unchecked")
public class BINOLSSPRM14_BL {
	@Resource
	private BINOLSSPRM14_Service binOLSSPRM14_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLBSCOM01_BL binOLBSCOM01_BL;

	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_BL binOLMQCOM01_BL;
	
	public Map<String, Object> getCounterInfo (int id){
		return binOLSSPRM14_Service.getCounterInfo(id);
	}
	
	/**
	 * 取得活动数量
	 * @param map
	 * @return
	 */
	public int getActiveCount (Map<String, Object> map){
		int count = binOLSSPRM14_Service.getActiveCount(map);
		return count;
	}
	
	/**
	 * 取得活动信息
	 * @param map
	 * @return
	 */
	public List getActiveList (Map<String, Object> map){
		List resultList = binOLSSPRM14_Service.getActiveList(map);
		return resultList;
	}
	
	/**
	 * 促销品信息模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmPrt (Map<String, Object> map){
		//TZZK code 
		map.put("TZZK_UNIT_CODE", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 设定查询最大件数
		map.put("INDSEARCH_COUNT", PromotionConstants.PROMOTION_INDSEARCH_COUNT);
		List resultList = binOLSSPRM14_Service.indSearchPrmPrt(map);
		return resultList;
	}
	
	/**
	 * 促销活动名模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmName (Map<String, Object> map){
		// 设定查询最大件数
		map.put("INDSEARCH_COUNT", PromotionConstants.PROMOTION_INDSEARCH_COUNT);
		List resultList = binOLSSPRM14_Service.indSearchPrmName(map);

		return resultList;
	}
	
	/**
	 * 促销主活动模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmGrpName (Map<String, Object> map){
		return binOLSSPRM14_Service.indSearchPrmGrpName(map);
	}
	
	/**
	 * 促销地点信息模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmLocation (Map<String, Object> map){
		// 设定查询最大件数
		map.put("INDSEARCH_COUNT", PromotionConstants.PROMOTION_INDSEARCH_COUNT);
		List counterList = null ;
		List cityList =null;
		List channelList =null;
		String locationType = (String)map.get("locationType");
		List prmLocationList = new ArrayList();
		if (locationType!=null && !"".equals(locationType)){
			if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION)){
				// 促销城市查询
				cityList = binOLSSPRM14_Service.indSearchPrmCity(map);
			}else if (locationType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)){
				// 促销柜台查询
				counterList = binOLSSPRM14_Service.indSearchPrmCounter(map);
				// 促销城市查询
				cityList = binOLSSPRM14_Service.indSearchPrmCity(map);
			}else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)){
				// 促销渠道查询
				channelList = binOLSSPRM14_Service.indSearchPrmChannel(map);
			}else if (locationType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)){
				// 促销渠道查询
				channelList = binOLSSPRM14_Service.indSearchPrmChannel(map);
				// 促销柜台查询
				counterList = binOLSSPRM14_Service.indSearchPrmCounter(map);
			}
		}else if (locationType == null){
			// 促销柜台查询
			counterList = binOLSSPRM14_Service.indSearchPrmCounter(map);
			// 促销城市查询
			cityList = binOLSSPRM14_Service.indSearchPrmCity(map);
			// 促销渠道查询
			channelList = binOLSSPRM14_Service.indSearchPrmChannel(map);
		}
		
		if (counterList!=null){
			prmLocationList.addAll(counterList);
		}
		if (cityList!=null){
			prmLocationList.addAll(cityList);
		}
		if (channelList!=null){
			prmLocationList.addAll(channelList);
		}
		return prmLocationList;
	}
	
	/**
	 * 审核活动
	 * @param map
	 */
	public void tran_check(Map<String,Object> map){
		binOLSSPRM14_Service.checkActive1(map);
		binOLSSPRM14_Service.checkActive2(map);
	}
	
	/**
	 * 转换促销品信息String
	 * @param resultList
	 * @return
	 */
	public String parseStringByPrmPrt (List resultList){
		StringBuffer sb= new StringBuffer();
		for (int i = 0;i<resultList.size();i++){
			HashMap resultMap = (HashMap)resultList.get(i);
			sb.append((String)resultMap.get("nameTotal"));
			sb.append("|");
			sb.append((String)resultMap.get("unitCode"));
			if (i!=resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转换促销活动名String
	 * @param resultList
	 * @return
	 */
	public String parseStringByPrmName (List resultList){
		// 转化List为所需要的String类型
		StringBuffer sb= new StringBuffer();
		for (int i = 0;i<resultList.size();i++){
			HashMap resultMap = (HashMap)resultList.get(i);
			sb.append((String)resultMap.get("activityName"));
			sb.append("|");
			sb.append((String)resultMap.get("activityCode"));
			sb.append("|");
			sb.append((String)resultMap.get("activityCode"));
			if (i!=resultList.size()){
				sb.append("\n");
			}
		
		}
		return sb.toString();
	}
	
	/**
	 * 转换促销活动名String
	 * @param resultList
	 * @return
	 */
	public String parseStringByPrmGrpName (List resultList){
		StringBuffer sb= new StringBuffer();
		for (int i = 0;i<resultList.size();i++){
			HashMap resultMap = (HashMap)resultList.get(i);
			sb.append(resultMap.get("groupId"));
			sb.append("|");
			sb.append((String)resultMap.get("groupName"));
			sb.append("|");
			sb.append((String)resultMap.get("groupCode"));
			if (i!=resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转换促销地点信息String
	 * @param resultList
	 * @return
	 */
	public String parseStringByLocation (List resultList){
		StringBuffer sb= new StringBuffer();
		for (int i=0;i<resultList.size();i++){
			HashMap resultMap = (HashMap)resultList.get(i);
			if (null!=resultMap.get("counterCode") && !"".equals("counterCode")){
				// 取得柜台信息
				sb.append((String)resultMap.get("departName"));
				sb.append("|");
				// 取得柜台Code
				sb.append((String)resultMap.get("counterCode"));
				sb.append("|");
				sb.append("counter");

			}
			if (null!=resultMap.get("cityName") && !"".equals("cityName")){
				// 取得区域市信息
				sb.append((String)resultMap.get("cityName"));
				sb.append("|");
				// 取得区域市ID
				sb.append((String.valueOf(resultMap.get("regionID"))));
				sb.append("|");
				sb.append("region");
			}
			if (null!=resultMap.get("channelName") && !"".equals("channelName")){
				// 取得渠道信息
				sb.append((String)resultMap.get("channelName"));
				sb.append("|");
				// 取得渠道ID
				sb.append((String.valueOf(resultMap.get("channelID"))));
				sb.append("|");
				sb.append("channel");
			}
			if (i!=resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得活动时间
	 * @param activeList
	 */
//	public void getActiveTime (List activeList,Map map){
//		
//		// 遍历活动List
//		for (int i = 0;i<activeList.size();i++){
//			HashMap activeMap = (HashMap)activeList.get(i);
//			// 取得基础属性时间id
//			activeMap.put(PromotionConstants.BASE_PROP_TIME,map.get(PromotionConstants.BASE_PROP_TIME));
//			this.setTimeConditionByFilter(map, activeMap);
//			// 可否flag区分
//			activeMap.put("actValidFlag", map.get("actValidFlag"));
//			activeMap.put("sysTime", map.get("sysTime"));
//			// 取得时间List
//			List resultList = binOLSSPRM14_Service.getActiveTime(activeMap);
//			if (!resultList.isEmpty()){
//				activeMap.put("timeList", resultList);
//				
//			}else{
//				activeList.remove(i);
//				i--;
//			}
//		}
//	}
	
	/**
	 * 根据过滤条件设定时间条件
	 * @param filter
	 */
//	public void setTimeConditionByFilter(Map map,Map activeMap){
//		String filterType = (String)map.get("FILTER_VALUE");
//		// 促销活动查询开始时间
//		String searchPrmStartDate = (String)map.get("searchPrmStartDate");
//		// 促销活动查询结束时间
//		String searchPrmEndDate = (String)map.get("searchPrmEndDate");
//		String sysDateStr = DateUtil.date2String(new Date(), CherryConstants.DATE_PATTERN);
//		if (null!=filterType && !"".equals(filterType)){
//			// 全部时间
//			if ("all_time".equals(filterType)){
//				if (searchPrmStartDate==null || "".equals(searchPrmStartDate)&& (searchPrmEndDate==null || "".equals(searchPrmEndDate))){
//					searchPrmStartDate = sysDateStr;
//					searchPrmEndDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmStartDate, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//				}
//				if ((searchPrmStartDate!=null && !"".equals(searchPrmStartDate)) && (searchPrmEndDate==null || "".equals(searchPrmEndDate))){
//					searchPrmEndDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmStartDate, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//				}
//				if ((searchPrmStartDate==null || "".equals(searchPrmStartDate))&& (searchPrmEndDate!=null && !"".equals(searchPrmEndDate))){
//					searchPrmStartDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmEndDate, (-1)*Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//				}
//				activeMap.remove("FILTER_VALUE");
//			}else{
//				activeMap.put("maxDate", DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, sysDateStr, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME)));
//				activeMap.put("minDate", DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, sysDateStr, (-1)*Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME)));
//				activeMap.put("FILTER_VALUE", filterType);
//			}
//		}else{
//			if (searchPrmStartDate==null || "".equals(searchPrmStartDate)&& (searchPrmEndDate==null || "".equals(searchPrmEndDate))){
//				searchPrmStartDate = sysDateStr;
//				searchPrmEndDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmStartDate, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//			}
//			if ((searchPrmStartDate!=null && !"".equals(searchPrmStartDate)) && (searchPrmEndDate==null || "".equals(searchPrmEndDate))){
//				searchPrmEndDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmStartDate, Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//			}
//			if ((searchPrmStartDate==null || "".equals(searchPrmStartDate))&& (searchPrmEndDate!=null && !"".equals(searchPrmEndDate))){
//				searchPrmStartDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchPrmEndDate, (-1)*Integer.parseInt(PromotionConstants.PRM_ACT_SPACE_OF_TIME));
//			}
//		}
//		
//		activeMap.put("searchPrmStartDate", searchPrmStartDate);
//		activeMap.put("searchPrmEndDate", searchPrmEndDate);
//	}
	
	
	/**
	 * 取得活动数据(ActivityTable_CHY数据)
	 * @param map
	 */
	public void getActiveData (Map<String, Object> map){		
		// 查询活动条件和结果
		List actConResultList = binOLSSPRM14_Service.getActConResultList(map);
		
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
				List resultList = binOLSSPRM14_Service.getCounterByIdCity(map);
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
				List resultList = binOLSSPRM14_Service.getCounterByIdChannel(map);
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
			actConResultMap.put("activityName", map.get("activityName"));
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
				actConResultNewList.add(actConResultNewMap);
				actConResultMap.put("price", "0");
				actConResultNewList.add(actConResultMap);
			}else{
				// 取得活动编码
				String code = binOLCM03_BL.getTicketNumber(String.valueOf(map.get("bin_OrganizationInfoID")), String.valueOf(map.get("brandInfoID")), "", "9");
				actConResultMap.put("activeCode", code.substring(4, 10)+code.substring(code.length()-4, code.length()));
				actConResultNewList.add(actConResultMap);
			}
		}
		
		map.put("activityTableCHYList", actConResultNewList);
	}
	
	/**
	 * 取得活动结果数据(PromotionTable_CHY数据)
	 * @param map
	 */
	public void getActiveResultData (Map<String, Object> map){
		// 查询活动结果
		List actResultList = binOLSSPRM14_Service.getActiveResultList(map);
		List promotionTableCHYList = new ArrayList();
		for (int i=0;i<actResultList.size();i++){
			
			HashMap actResultMap = (HashMap)actResultList.get(i);
			//String price = (String)actResultMap.get("price");
			String unitCode  = (String)actResultMap.get("promotionPrtUnitcode");
			// 如果价格不为空 --TZZK
			if ((null!=actResultMap.get("price"))){
				HashMap promotionTableMap = new HashMap();
				// 品牌Code
				promotionTableMap.put("brandCode", map.get("brandCode"));
				// 促销产品成本价
				promotionTableMap.put("salePrice", "0");
				// 套装折扣类别码
				promotionTableMap.put("promotionClassCode", PromotionConstants.PROMOTION_TZZK_TYPE_CODE);
				// 套装折扣类别名称
				promotionTableMap.put("promotionClassName", PromotionConstants.PROMOTION_TZZK_NAME);
				// 套装折扣UnitCode
				promotionTableMap.put("promotionPrtCode", PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
				// 库存管理
				promotionTableMap.put("promotionPrtStock", "0");
				promotionTableCHYList.add(promotionTableMap);
			}
			
			// 产品代码不为空 --CXLP
			if (null!=unitCode && !"".equals(unitCode) && !PromotionConstants.PROMOTION_TZZK_UNIT_CODE.equals(unitCode)){
				HashMap resultMap = binOLSSPRM14_Service.getPromotionPrtInfo(actResultMap);
				
				HashMap promotionTableMap = new HashMap();
				// 品牌Code
				promotionTableMap.put("brandCode", map.get("brandCode"));
				// 促销产品名称
				String nameTotal = (String)resultMap.get("nameTotal");
				if (resultMap.get("salePrice")!=null){
					// 促销产品成本价
					promotionTableMap.put("salePrice",(int)Float.parseFloat((String.valueOf(resultMap.get("salePrice")))));
				}else{
					// 促销产品成本价
					promotionTableMap.put("salePrice", "0");
				}
				// 促销产品名称
				promotionTableMap.put("nameTotal", nameTotal==null?"":nameTotal);
				// 套装折扣类别码
				promotionTableMap.put("promotionClassCode", PromotionConstants.PROMOTION_CXLP_TYPE_CODE);
				// 套装折扣类别名称
				promotionTableMap.put("promotionClassName", PromotionConstants.PROMOTION_CXLP_NAME);
				// 套装折扣UnitCode
				promotionTableMap.put("promotionPrtCode",unitCode);
				// 库存管理
				promotionTableMap.put("promotionPrtStock", "1");
				promotionTableCHYList.add(promotionTableMap);
			}
		}
		map.put("promotionTableCHYList", promotionTableCHYList);
	}
	
	
	/**
	 * 查询活动信息数据(ActivityAssociateTable_CHY数据)
	 * @param map
	 */
	public void getActiveInfoData(Map<String, Object> map){
		// 查询活动信息
		List actInfoList = binOLSSPRM14_Service.getActiveInfoList(map);
		for (int i = 0;i<actInfoList.size();i++){
			HashMap actInfoMap = (HashMap)actInfoList.get(i);
			HashMap brandInfoMap = binOLSSPRM14_Service.getBrandCode(actInfoMap);
			actInfoMap.put("brandCode", brandInfoMap.get("actBrandCode"));
			// 设定品牌Code
			map.put("brandCode", brandInfoMap.get("actBrandCode"));
			// 设定品牌ID
			map.put("brandInfoID", actInfoMap.get("actBrandInfoID"));
		}
		map.put("activityAssociateTableCHYList", actInfoList);
	}
	
	/**
	 * 将促销活动下发给终端接口表
	 * @param map
	 * @throws Exception 
	 */
	public void tran_publicProActive (Map<String, Object> map) throws Exception{
//		binOLSSPRM14_Service.addActivityAssociateTable_CHY(map);
//		binOLSSPRM14_Service.addActivityTable_CHY(map);
	}
	
	/**
	 * 根据活动ID取得活动信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActivityInfo (String activeID){
		return binOLSSPRM14_Service.getActivityInfo(activeID);
	}
	
	/**
	 * 组装产品下发通知的MQ消息
	 * @param map
	 * @param subType 子类型：PRT、DPRT	 必填，用于区分该消息体发送的是产品信息还是柜台产品信息
	 * @return
	 * @throws Exception
	 */
	public int sendMqNotice(Map<String,Object> map,Map<String,Object> mainData){
		int result = CherryConstants.SUCCESS;
		//申明要返回的map
		Map<String,Object> mqMap = new HashMap<String,Object>();
		//组装消息体版本	Version
		mqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.015.001");
		//组装消息体数据类型	DataType
		mqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
		Map<String,Object> dataLine = new HashMap<String,Object>();
		//将主数据放入dataLine中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		mqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		//设定MQInfoDTO
		MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(mqMap,map);
		//调用共通发送MQ消息
		mqDTO.setMsgQueueName("cherryToPosCMD");
		try {
			binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
		} catch (Exception e) {
			result = CherryConstants.ERROR;
		}
		return result;
	}

}
