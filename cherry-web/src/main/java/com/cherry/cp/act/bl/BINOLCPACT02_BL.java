package com.cherry.cp.act.bl;

import java.util.*;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINOLCPACT02_Service;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM68_BL;
import com.googlecode.jsonplugin.JSONUtil;


public class BINOLCPACT02_BL {
	@Resource
	private BINOLCPACT02_Service binOLCPACT02_Service;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
    private CodeTable codeTable;
	
	@Resource
    private BINOLCPCOM03_IF binolcpcom03IF;

	@Resource
	private BINOLSSPRM68_BL binolssprm68Bl;
	
	 /**
     * 取得子活动菜单
     * 
     * @param map
     * @return List
     * 		子活动菜单
     */
    public List<Map<String, Object>> getSubMenuList(Map<String, Object> map) {
    	// 取得子活动菜单
    	List<Map<String, Object>> subMenuList= binOLCPACT02_Service.getSubMenuList(map);
    	getsubName(subMenuList);
    	return subMenuList;
    }
    /**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
    public CampaignDTO getCampaignInfo(Map<String, Object> map) {
		// 取得规则体详细信息
		CampaignDTO dto = binolcpcom02_Service.getCampaignInfo(map);
		if(!CherryChecker.isNullOrEmpty(dto.getObtainRule())){
			Map<String,Object> rule = ConvertUtil.json2Map(dto.getObtainRule());
			dto.setReferType(ConvertUtil.getString(rule.get(CampConstants.REFER_TYPE)));
			dto.setAttrA(ConvertUtil.getString(rule.get(CampConstants.ATTR_A)));
			dto.setAttrB(ConvertUtil.getString(rule.get(CampConstants.ATTR_B)));
			dto.setAttrC(ConvertUtil.getString(rule.get(CampConstants.ATTR_C)));
			dto.setValA(ConvertUtil.getString(rule.get(CampConstants.VAL_A)));
			dto.setValB(ConvertUtil.getString(rule.get(CampConstants.VAL_B)));
		}
    	return dto;
    }
    /**
     * 取得会员活动扩展信息
     * 
     * @param map
     * @return 
     */
    public Map<String, Object> getCampaignExtInfo(Map<String, Object> map) throws Exception{
		// 取得会员活动扩展信息
    	return binOLCPACT02_Service.getCampaignExtInfo(map);
    }
    /**
	 * 取得活动详细
	 * @param map 
	 * @return subInfoMap
	 * */
	public Map<String, Object> getSubInfo(Map<String, Object> map) throws Exception {
		Map<String,Object> subInfoMap = new HashMap<String, Object>();
		//活动基础信息baseMap
		Map<String,Object> baseMap = binOLCPACT02_Service.getSubBaseInfo(map);
		subInfoMap.putAll(baseMap);
		//品牌
		subInfoMap.put("brandInfoId", map.get("brandInfoId"));
		//活动奖励信息
		List<Map<String,Object>> subResultInfoList = binOLCPACT02_Service.getSubResultList(map);
		//分组结果List
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		//奖励信息List
		List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
		//虚拟促销品List
		List<Map<String, Object>> virtList = new ArrayList<Map<String,Object>>();
		if(null != subResultInfoList && subResultInfoList.size()>0){
			for(Map<String,Object> result : subResultInfoList){
				String deliveryType = ConvertUtil.getString(result.get(CampConstants.DELIVERYTYPE));
				List<Map<String,Object>> deliveryList = codeTable.getCodes("1328");
				if(!"".equals(deliveryType)){
					List<String> deList = (List<String>)JSONUtil.deserialize(deliveryType);
					for(Map<String,Object> delivery: deliveryList){
						String key = ConvertUtil.getString(delivery.get("CodeKey"));
						if(deList.contains(key)){
							delivery.put("checked", true);
						}
					}
				}
				result.put("deliveryList", deliveryList);
			}
			// 奖励类型
			subInfoMap.put(CampConstants.REWARD_TYPE,subResultInfoList.get(0).get(CampConstants.REWARD_TYPE));
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key = {CampConstants.GROUP_NO,CampConstants.GROUP_TYPE,CampConstants.LOGIC_OPT};
			keyList.add(key);
			ConvertUtil.convertList2DeepList(subResultInfoList,resultList,keyList,0);
		}
		for(Map<String, Object> tempMap :resultList){
			int groupNo = ConvertUtil.getInt(tempMap.get(CampConstants.GROUP_NO));
			if(groupNo > 0){//分组号大于0的为奖励信息List
				prtList.add(tempMap);
			}else{//分组号小于0的为虚拟礼品List
				virtList.add(tempMap);
			}
		}
		//奖励礼品List
		subInfoMap.put("subResultInfoList",subResultInfoList);
		//奖励信息List
		subInfoMap.put("prtList",prtList);
		//虚拟促销品List
		subInfoMap.put("virtList",virtList);
		//子活动条件信息
		List<Map<String,Object>> subConditionList = binOLCPACT02_Service.getSubConditionList(map);
		
		if (null != subConditionList && !subConditionList.isEmpty()) {
			for(Map<String,Object>subCon : subConditionList){
				//活动条件类型
				String conType = ConvertUtil.getString(subCon.get("conType"));
				// 活动条件属性名
				String propName= ConvertUtil.getString(subCon.get("propName"));
				if(CampUtil.BASEPROP_CUSTOMER.equals(propName)){// 活动对象
					// 活动对象
					Map<String, Object> campMebMap = new HashMap<String, Object>();
					campMebMap.put(CampConstants.CAMP_MEB_TYPE, conType);
					campMebMap.put(CampConstants.SEARCH_CODE, subCon.get("propValue"));
					campMebMap.put("conInfo", subCon.get("conInfo"));
					// 取得活动对象数量
					Map<String,Object> param = new HashMap<String, Object>();
					param.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					param.put(CampConstants.SEARCH_CODE, campMebMap.get(CampConstants.SEARCH_CODE));
					param.put(CampConstants.CAMP_MEB_TYPE, conType);
					param.put("conInfo", campMebMap.get("conInfo"));
					int total = binolcpcom03IF.searchMemCount(param);
					campMebMap.put("total", total);
					subInfoMap.put("campMebMap", campMebMap);
				}else if(CampUtil.BASEPROP_CHANNAL.equals(propName)
							|| CampUtil.BASEPROP_CITY.equals(propName)
							|| CampUtil.BASEPROP_COUNTER.equals(propName)
							|| CampUtil.BASEPROP_FACTION.equals(propName)){// 活动地点
					//活动地点List
					List<Map<String,Object>> campPlaceList = null;
					Map<String,Object> param = new HashMap<String, Object>();
					param.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					param.put("subCampId", map.get("subCampId"));
					param.put("propId", subCon.get("propId"));
					param.put("conType", conType);
					if(CampConstants.LOTION_TYPE_2.equals(conType)
							||CampConstants.LOTION_TYPE_4.equals(conType)
							||CampConstants.LOTION_TYPE_5.equals(conType)
							||CampConstants.LOTION_TYPE_8.equals(conType)){//区域指定柜台，渠道指定柜台，导入柜台
						campPlaceList = binOLCPACT02_Service.getCntList(param);
					}else if(CampConstants.LOTION_TYPE_1.equals(conType)){//按区域
						campPlaceList = binOLCPACT02_Service.getCityList(param);
						//根据ID查询名称
					}else if(CampConstants.LOTION_TYPE_3.equals(conType)){//按渠道
						campPlaceList = binOLCPACT02_Service.getChannelList(param);
					}else if(CampConstants.LOTION_TYPE_7.equals(conType)){//按系统
						List<Map<String, Object>> list = codeTable.getCodes("1309");
						if(null != list && list.size() > 0){
							Map<Object, Object> codeMap = new HashMap<Object, Object>();
							// list2map
							for(Map<String, Object> item : list){
								codeMap.put(item.get("CodeKey"), item.get("Value"));
							}
							campPlaceList = binOLCPACT02_Service.getFactionList(param);
							if(null != campPlaceList && campPlaceList.size() > 0){
								for(Map<String, Object> palce : campPlaceList){
									palce.put("placeName", codeMap.get(palce.get("id")));
								}
							}
						}
					}
					subInfoMap.put("placeType", conType);
					subInfoMap.put("campPlaceList", campPlaceList);
				}
			}
		}
		return subInfoMap;
	}
	/**
	 * 将获取的活动名称限定固定长度
	 * @param list
	 */
	private void getsubName(List<Map<String, Object>> list){
		// 取得活动信息List
		if(null != list && list.size() > 0){
    		int size = list.size();
    		for(int i = 0 ; i < size ; i++){
    			Map<String,Object> temp = list.get(i);
    			//取得活动名称
    			String subCampaignName = (String)temp.get("subCampaignName");
    			if(null == subCampaignName){
    				subCampaignName="";
    			}
    			String subCampaignName_temp = "";
    			char[] messageArr = subCampaignName.toCharArray();
    			//取得备注信息的长度
    			int  messageBodyLength = messageArr.length;
    			//
    			int count = 0;
    			label2:
    			for(int j = 0 ; j < messageBodyLength ; j++){
    				//控制汉字长度
    				if(count >= 19){
    					subCampaignName_temp = subCampaignName.substring(0, j-2)+"...";
    					break label2;
    				}
    				//如果是汉字则加2，否则加1
    				if(messageArr[j] >= 0x0391 && messageArr[j] <= 0xFFE5){
    					count += 2;
    				}else{
    					count ++;
    				}
    			}
    			if("".equals(subCampaignName_temp)){
    				subCampaignName_temp = subCampaignName;
    			}
    			temp.put("subCampaignName_temp", subCampaignName_temp);
    		}
    	}
	}
	
	public String getBusDate(Map<String,Object> map){
		return binolcpcom02_Service.getBussinessDate(map);
	}

	/**
	 * 获取用户权限地点信息
	 * @param campInfo
	 * @param parMap
     */
	public void getUserAuthorityPlace(Map<String,Object> campInfo,Map<String,Object> parMap){
		String placeType = ConvertUtil.getString(campInfo.get("placeType"));
		parMap.put("locationType",placeType);
		List<Map<String,Object>> newPlaceList = new LinkedList<Map<String,Object>>();
		List<Map<String,Object>> userAuthorityList = binolssprm68Bl.getUserAuthorityPlaceList(parMap,placeType);
		if("0".equals(placeType)){
			for(Map<String,Object> userPlace : userAuthorityList){
				Map<String,Object> newPlace = new HashMap<String,Object>();
				newPlace.put("placeCode",userPlace.get("code"));
				newPlace.put("placeName",userPlace.get("name"));
				newPlaceList.add(newPlace);
			}
		}else{
			//将用户权限地点List转为Map
			Map<Object,Object> userAuthorityMap = new HashMap<Object,Object>();
			//将userPlace的code,name以key-value放入map
			for (Map<String,Object> userPlace : userAuthorityList){
				userAuthorityMap.put(userPlace.get("code"),userPlace.get("name"));
			}
			List<Map<String,Object>> placeList = (List<Map<String,Object>>) campInfo.get("campPlaceList");
			if(CampConstants.LOTION_TYPE_7.equals(placeType)){
				for(Map<String,Object> placeInfo : placeList){
					int code = ConvertUtil.getInt(placeInfo.get("id"));
//					for(Map<String,Object> userPlace : userAuthorityList){
//						int userCode = ConvertUtil.getInt(userPlace.get("code"));
//						if(code==userCode){
//							newPlaceList.add(placeInfo);
//							userAuthorityList.remove(userPlace);
//							break;
//						}
//					}
					//如果userAuthorityMap包含了地点id,则将该地点信息放入返回List
					if (userAuthorityMap.containsKey(code)) {
						newPlaceList.add(placeInfo);
					}
				}
			}else if(CampConstants.LOTION_TYPE_2.equals(placeType)
					||CampConstants.LOTION_TYPE_4.equals(placeType)
					||CampConstants.LOTION_TYPE_5.equals(placeType)
					||CampConstants.LOTION_TYPE_8.equals(placeType)
					||CampConstants.LOTION_TYPE_10.equals(placeType)){
				for(Map<String,Object> placeInfo : placeList){
					String code = ConvertUtil.getString(placeInfo.get("placeCode"));
//					for(Map<String,Object> userPlace : userAuthorityList){
//						String userCode = ConvertUtil.getString(userPlace.get("code"));
//						if(code.equals(userCode)){
//							newPlaceList.add(placeInfo);
//							userAuthorityList.remove(userPlace);
//							break;
//						}
//					}
					//如果userAuthorityMap包含了地点id,则将该地点信息放入返回List
					if (userAuthorityMap.containsKey(code)) {
						newPlaceList.add(placeInfo);
					}
				}
			}else{
				for(Map<String,Object> placeInfo : placeList){
					int code = ConvertUtil.getInt(placeInfo.get("placeCode"));
//					for(Map<String,Object> userPlace : userAuthorityList){
//						int userCode = ConvertUtil.getInt(userPlace.get("code"));
//						if(code==userCode){
//							newPlaceList.add(placeInfo);
//							userAuthorityList.remove(userPlace);
//							break;
//						}
//					}
					//如果userAuthorityMap包含了地点id,则将该地点信息放入返回List
					if (userAuthorityMap.containsKey(code)) {
						newPlaceList.add(placeInfo);
					}
				}
			}
		}
		campInfo.put("campPlaceList",newPlaceList);
	}
}
