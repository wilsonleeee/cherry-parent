/*	
 * @(#)SwapValidate.java     1.0 2012/04/18
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
package com.cherry.cp.act.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM15_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.annota.BeanReso;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.cherry.cp.common.validate.BaseValidate;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;

/**
 * 会员积分兑礼 Validate
 * 
 * @author lipc
 * @version 1.0 2012/04/18
 */
public class ActValidate extends BaseValidate {

	@BeanReso(name="binOLCM33_BL")
	public BINOLCM33_BL binOLCM33_BL;
	
	@BeanReso(name="binOLCM15_Service")
	public BINOLCM15_Service binOLCM15_Service;
	
	/**
	 * 验证子活动模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000053_check(Map<String, Object> map,
			Map<String, Object> baseMap) throws JSONException{
		if (null != map) {
			List<Map<String, Object>> campList=(List<Map<String, Object>>) map.get(CampConstants.CAMP_LIST);
			if (campList != null) {
				int sum = 0;
				for (int i = 0; i < campList.size(); i++) {
					Map<String, Object> camp = campList.get(i);
					//子活动编号
					String campNo = ConvertUtil.getString(camp
							.get(CampConstants.CAMP_NO));
					//前几页活动参数
					Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
					//活动名称
					String campName = ConvertUtil.getString(camp
							.get(CampConstants.CAMP_NAME));
					//活动描述
					String desCription = ConvertUtil.getString(camp
							.get(CampConstants.DESCRIPTION));
					//预约数量上限
					String topLimit = ConvertUtil.getString(camp
							.get(CampConstants.TOPLIMIT));
					
					//会员预约次数
					String times = ConvertUtil.getString(camp
							.get(CampConstants.TIMES));
//					if(null!=commMap){
//						//子活动状态
//						String state = ConvertUtil.getString(commMap
//								.get("state"));
//						if("1".equals(state)||"2".equals(state)){
//							sum++;
//						}
//					}
					//活动名称不为空验证
					if ("".equals(campName)) {
						actionErrorList.add(new ActionErrorDTO(1,
								CampConstants.CAMP_NAME + CampConstants.CAMP_LINE + (i-sum), "ECM00009",
								new String[] {PropertiesUtil.getText("ACT00006")}));
					}else if(campName.length() > 20){
						//活动名称长度不能超过20
						actionErrorList.add(new ActionErrorDTO(1,
								CampConstants.CAMP_NAME + CampConstants.CAMP_LINE + (i-sum), "ECM00020",
								new String[] { PropertiesUtil.getText("ACT00006"),"20"}));
					}else{
//						int id = binolcpcom02IF.getActIdByName(campName, brandId);
//						int oldId = ConvertUtil.getInt(camp.get(CampConstants.SUB_CAMP_ID));
//						if(id != oldId){
//			    			actionErrorList.add(new ActionErrorDTO(1,
//									CampConstants.CAMP_NAME + CampConstants.CAMP_LINE + (i-sum), "ECM00032",
//									new String[] {PropertiesUtil.getText("ACT00006")}));
//			    		}
					}
					//活动描述验证
					if(desCription.length()>300){
						//活动名称长度不能超过50
						actionErrorList.add(new ActionErrorDTO(1,
								CampConstants.DESCRIPTION + CampConstants.CAMP_LINE + (i-sum), "ECM00020",
								new String[] { PropertiesUtil.getText("ACT00010"),"300"}));
					}
					
					//预约数量上限
					if(!CherryChecker.isNullOrEmpty(topLimit)){
						if(!CherryChecker.isNumeric(topLimit)){
							//预约数量上限应该为正整数
							actionErrorList.add(new ActionErrorDTO(1,
									CampConstants.TOPLIMIT + CampConstants.CAMP_LINE + (i-sum), "ACT000101",
									new String[] {}));
						}
						
						if(topLimit.length()>8){
							//预约数量上限不能超过6
							actionErrorList.add(new ActionErrorDTO(1,
									CampConstants.TOPLIMIT + CampConstants.CAMP_LINE + (i-sum), "ECM00020",
									new String[] { PropertiesUtil.getText("ACT000105"),"8"}));
						}
					}
					
					//会员预约次数
					if(!CherryChecker.isNullOrEmpty(times)){
						if(!CherryChecker.isNumeric(times)){
							//预约数量上限应该为正整数
							actionErrorList.add(new ActionErrorDTO(1,
									CampConstants.TIMES + CampConstants.CAMP_LINE + (i-sum), "ACT000101",
									new String[] {}));
						}
						
						if(times.length()>6){
							//预约数量上限不能超过6
							actionErrorList.add(new ActionErrorDTO(1,
									CampConstants.TIMES + CampConstants.CAMP_LINE + (i-sum), "ECM00020",
									new String[] { PropertiesUtil.getText("ACT000106"),"6"}));
						}
					}
				}
			}
		}
		if(!actionErrorList.isEmpty()){
			isCorrect = false;
		}
	}
	/**
	 * 验证活动范围模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000059_check(Map<String, Object> map,
			Map<String, Object> baseMap) throws JSONException {
		List<Map<String, Object>> campList=(List<Map<String, Object>>) map.get(CampConstants.CAMP_LIST);
		if (campList != null) {
			for (int i = 0; i < campList.size(); i++) {
				Map<String, Object> tempMap = campList.get(i);
				//活动地点
				String placelist = ConvertUtil.getString(tempMap
						.get(CampConstants.PLACE_JSON));
				//子活动编号
				String campNo = ConvertUtil.getString(tempMap
						.get(CampConstants.CAMP_NO));
				//前几页活动参数
				Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
				//活动名称
				String campName = ConvertUtil.getString(commMap
						.get(CampConstants.CAMP_NAME));
				List<Map<String, Object>> list = ConvertUtil.json2List(placelist);
				if(list==null||list.size()==0){
					 //活动地点不能为空验证
					 actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {ConvertUtil.getString(campName),PropertiesUtil.getText("ACT00008")}));
				}
			}
		}
		if(!actionErrorList.isEmpty()){
			isCorrect = false;
		}
	}
	
	/**
	 * 验证活动对象模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000060_check(Map<String, Object> map,
			Map<String, Object> baseMap) throws Exception {
		List<Map<String, Object>> campList=(List<Map<String, Object>>) map.get(CampConstants.CAMP_LIST);
		if (campList != null) {
			for (int i = 0; i < campList.size(); i++) {
				Map<String, Object> tempMap = campList.get(i);
				//活动对象类型
				String campMebType = ConvertUtil.getString(tempMap.get(CampConstants.CAMP_MEB_TYPE));
				if("".equals(campMebType)){
					//子活动编号
					String campNo = ConvertUtil.getString(tempMap
							.get(CampConstants.CAMP_NO));
					//前几页活动参数
					Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
					//活动名称
					String campName = ConvertUtil.getString(commMap
							.get(CampConstants.CAMP_NAME));
					//活动对象不能为空验证
					 actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {ConvertUtil.getString(campName),PropertiesUtil.getText("ACT00011")}));	
				}else{
					if(CampConstants.CAMP_MEB_TYPE_1.equals(campMebType)||CampConstants.CAMP_MEB_TYPE_2.equals(campMebType)
							||CampConstants.CAMP_MEB_TYPE_3.equals(campMebType)){
						//子活动编号
						String campNo = ConvertUtil.getString(tempMap
								.get(CampConstants.CAMP_NO));
						//前几页活动参数
						Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
						//活动名称
						String campName = ConvertUtil.getString(commMap
								.get(CampConstants.CAMP_NAME));
						String searchCode = ConvertUtil.getString(tempMap
								.get(CampConstants.SEARCH_CODE)); 
						if(CherryChecker.isNullOrEmpty(searchCode)){
							 //活动对象不能为空验证
							 actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {ConvertUtil.getString(campName),PropertiesUtil.getText("ACT00011")}));	
						}else{
							//活动对象json
							String campMebInfo = ConvertUtil.getString(tempMap.get(CampConstants.CAMP_MEB_INFO));
							if(!CherryChecker.isNullOrEmpty(campMebInfo)){
								// 会员搜索条件map
								Map<String,Object> searchMap = ConvertUtil.json2Map(campMebInfo);
								if(CampConstants.CAMP_MEB_TYPE_2.equals(campMebType)){//当前搜索结果
									//活动对象结果List
									Map<String, Object> resMap = binOLCM33_BL.searchMemList(searchMap);
									if(null==resMap || ConvertUtil.getInt(resMap.get("total")) == 0){
										actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {ConvertUtil.getString(campName),PropertiesUtil.getText("ACT00011")}));
									}
								}
							}
						}
					}
				}
			}
				
		}
		if(!actionErrorList.isEmpty()){
			isCorrect = false;
		}
	}
	/**
	 * 取得共通Map
	 * 
	 * @param map
	 * @return
	 */
	protected Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		comMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		comMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		comMap.put("userID", map.get(CherryConstants.USERID));
		return comMap;
	}
	
	/**
	 * 验证礼品格式
	 * @param campName
	 * @param rewardInfo
	 */
	protected void validatePrmORPro(String campName,List<Map<String,Object>> prtlist){
		if(prtlist==null||prtlist.size()==0){
			//兑换礼品不能为空验证
			actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {campName,PropertiesUtil.getText("ACT00014")}));
		}else{
			for(Map<String, Object> proMap :prtlist){
				//礼品数量
				String  quantity = ConvertUtil.getString(proMap.get(CampConstants.QUANTITY));
				if("".equals(quantity)||"0".equals(quantity)){
					//礼品数量为空验证
					actionErrorList.add(new ActionErrorDTO(2, null, "ACT00001", new String[] {campName, PropertiesUtil.getText("ACT00015")}));
					break;
				}else{
					if(!CherryChecker.isNumeric(quantity)){
					//礼品数量是否为数字
					actionErrorList.add(new ActionErrorDTO(2, null, "ACT00005", new String[] {campName, PropertiesUtil.getText("ACT00015")}));
					break;
					}else{
						if(quantity.length()>10){
							//礼品数量长度验证
							actionErrorList.add(new ActionErrorDTO(2, null, "ECM00079", new String[] {campName, PropertiesUtil.getText("ACT00015"),"10"}));
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 验证虚拟促销品条码
	 * @param campName
	 * @param rewardInfo
	 */
	protected void validateVirtBarCode(int i,String barCode, String campName,List<Map<String,Object>> campList){
		Map<String,Object> camp = campList.get(i);
		//虚拟促销品barCode
		if("".equals(barCode)){
			//barCode为空验证
			actionErrorList.add(new ActionErrorDTO(2, null, "ACT00004", new String[] {campName, PropertiesUtil.getText("ACT00012")}));
		}else{
			//barCode格式验证
			if(!CherryChecker.isPrmCode(barCode)){
				actionErrorList.add(new ActionErrorDTO(2, null, "ACT00018", new String[] {campName, PropertiesUtil.getText("ACT00012")}));
			}
			//barCode长度验证
			if(barCode.length()>13){
				actionErrorList.add(new ActionErrorDTO(2, null, "ECM00079", new String[] {campName, PropertiesUtil.getText("ACT00012"),"13"}));
			}
			// 虚拟促销品条码数促销品表重复验证
			Map<String,Object> map = new HashMap<String, Object>();
			map.put(PromotionConstants.PRMVENDORID, camp.get(PromotionConstants.PRMVENDORID));
			map.put(CherryConstants.BARCODE, barCode);
			int count = binOLCM15_Service.getBarCodeCount(map);
			if(count > 0){
				actionErrorList.add(new ActionErrorDTO(2, null, "ECM00067", null));
			}
			for(int j = 0; j < campList.size(); j++){
				if(j!=i){
					String repBarCode = ConvertUtil.getString(campList.get(j).get(CherryConstants.BARCODE));
					//虚拟促销品条码重复验证
					if(barCode.equals(repBarCode)){
						//虚拟促销品条码重复验证
						actionErrorList.add(new ActionErrorDTO(2, null, "ACT00002", new String[] {campName, PropertiesUtil.getText("ACT00012")}));
						break;
					}
				}
			}
		}
	}
}
