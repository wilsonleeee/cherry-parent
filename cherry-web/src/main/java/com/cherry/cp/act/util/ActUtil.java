package com.cherry.cp.act.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class ActUtil {
	/**
	 * 取得活动列表
	 * 
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getCampList(Map<String, Object> map)
			throws JSONException {
		CampaignDTO campDTO = (CampaignDTO) map.get(CampConstants.CAMP_INFO);
		String extraInfoStr = ConvertUtil.getString(map.get(CampConstants.EXTRA_INFO));
		int curIndex = 0;
		if(!"".equals(extraInfoStr)){
			Map<String, Object> extraInfo = ConvertUtil.json2Map(extraInfoStr);
			curIndex = ConvertUtil.getInt(extraInfo.get("curIndex"));
		}
		// 活动列表List
		Map<String, Object> allSubCamp = (Map<String, Object>)map.get(CampConstants.ALLSUBCAMP);
		// 活动列表List
		List<Map<String, Object>> list = ConvertUtil.map2List(allSubCamp);
		if(null != list){
			for(Map<String, Object> camp: list){
				if(!"".equals(campDTO.getCampaignOrderFromDate())){
					camp.put(CampConstants.ORDER_FROMDATE, campDTO.getCampaignOrderFromDate());
				}
				camp.put("curIndex", curIndex);
			}
		}
		// 按照序号排序
		Collections.sort(list, new CampComparator(CampConstants.CAMP_NO));
		return list;
	}
	
	/**
	 * 取得活动列表
	 * 
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCampMap(Map<String, Object> map,String campNo){
		// 活动列表List
		Map<String, Object> allSubCamp = (Map<String, Object>)map.get(CampConstants.ALLSUBCAMP);
		if (null != allSubCamp) {
			return (Map<String, Object>)allSubCamp.get(CampConstants.SUBCAMP + campNo);
		}
		return null;
	}
	
	/**
	 * list 比较器
	 * 
	 */
	public static class CampComparator implements Comparator<Map<String, Object>> {
		String orderKey = null;

		public CampComparator(String orderKey) {
			super();
			this.orderKey = orderKey;
		}

		@Override
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			int temp1 = ConvertUtil.getInt(map1.get(orderKey));
			int temp2 = ConvertUtil.getInt(map2.get(orderKey));
			if (temp1 > temp2) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	/**
	 * 设置节点选中状态
	 * 
	 * @param nodes 节点数组
	 * @param checkedNode 选中节点
	 */
	@SuppressWarnings("unchecked")
	public static void setNodes(List<Map<String, Object>> nodes, Map<String, Object> checkedNode) {
		String checkedId = ConvertUtil.getString(checkedNode.get(CampConstants.ID));
		boolean half = (Boolean)checkedNode.get(CampConstants.HALF);
		if (null != nodes && nodes.size() > 0) {
			for (Map<String, Object> node : nodes) {
				String nodeId = ConvertUtil.getString(node.get(CampConstants.ID));
				// 当前节点为选中的节点
				if (nodeId.equals(checkedId)) {
					// 设置节点及子节点选中状态
					setChildrenNodes(node, half);
				} else {
					// 当前节点不等选中的节点
					// 取当前节点的所有子节点
					List<Map<String, Object>> nodeList = (List<Map<String, Object>>) node
							.get(CampConstants.NODES);
					setNodes(nodeList, checkedNode);
				}
			}
		}
	}

	/**
	 * 设置节点选中状态
	 *
	 * @param nodes 节点数组
	 * @param checkedNode 选中节点
	 */
	@SuppressWarnings("unchecked")
	public static void setNodes2(List<Map<String, Object>> nodes, Map<String, Object> checkedNode) {
		String checkedId = ConvertUtil.getString(checkedNode.get(CampConstants.ID));
		boolean half = (Boolean)checkedNode.get(CampConstants.HALF);
		if (null != nodes && nodes.size() > 0) {
			for (Map<String, Object> node : nodes) {
				String nodeId = ConvertUtil.getString(node.get(CampConstants.ID));
				// 当前节点为选中的节点
				if (nodeId.equals(checkedId)) {
					node.put(CampConstants.CHECKED, true);
					node.put("halfCheck", half);
				} else {
					// 当前节点不等选中的节点
					// 取当前节点的所有子节点
					List<Map<String, Object>> nodeList = (List<Map<String, Object>>) node
							.get(CampConstants.NODES);
					setNodes(nodeList, checkedNode);
				}
			}
		}
	}

	/**
	 * 设置节点以及子节点状态
	 * 
	 * @param node
	 * @param half
	 */
	@SuppressWarnings("unchecked")
	public static void setChildrenNodes(Map<String, Object> node, boolean half) {
		if (null != node) {
			node.put(CampConstants.CHECKED, true);
			node.put("halfCheck", half);
			// 全选时，设置子节点状态
			if(!half){
				List<Map<String, Object>> nodes = (List<Map<String, Object>>) node
						.get(CampConstants.NODES);
				if (null != nodes) {
					for (Map<String, Object> n : nodes) {
						setChildrenNodes(n, false);
					}
				}
			}
		}
	}
	
	public static CampRuleResultDTO map2Dto(Map<String, Object> map){
		if(null!=map){
			//设置活动奖励的条件
			CampRuleResultDTO dto = new CampRuleResultDTO();
			//取得活动奖励的条件
			int proId =ConvertUtil.getInt(map.get(CampConstants.PRO_ID));
			if(0 == proId){
				proId =ConvertUtil.getInt(map.get(PromotionConstants.PRMVENDORID));
			}
			String barCode =ConvertUtil.getString(map.get(CherryConstants.BARCODE));
			String unitCode=ConvertUtil.getString(map.get(CherryConstants.UNITCODE));
			String prtType=ConvertUtil.getString(map.get(CampConstants.PRT_TYPE));
			String groupType=ConvertUtil.getString(map.get(CampConstants.GROUP_TYPE));
			String deliveryType=ConvertUtil.getString(map.get(CampConstants.DELIVERYTYPE));
			
			int groupNo=ConvertUtil.getInt(map.get(CampConstants.GROUP_NO));
			String logicOpt=ConvertUtil.getString(map.get(CampConstants.LOGIC_OPT));
			int quantity =ConvertUtil.getInt(map.get(CampConstants.QUANTITY));
			float price =ConvertUtil.getFloat(map.get(CampConstants.PRICE));	
			dto.setProductVendorId(proId);
			dto.setBarCode(barCode);
			dto.setUnitCode(unitCode);
			dto.setSaleType(prtType);
			dto.setQuantity(quantity);
			dto.setPrice(price);
			dto.setGroupType(groupType);
			dto.setGroupNo(groupNo);
			dto.setLogicOpt(logicOpt);
			dto.setDeliveryType(toJson(deliveryType));
			return dto;
		}else{
			return null;
		}
	}
	/**
	 * json字符处理
	 * @param json
	 * @return
	 */
	public static String replaceJson(String json){
		return json.replace("\"[", "[").replace("]\"", "]");
	}
	
	public static String toJson(String deliveryType){
		String re = null;
		
		if(!"".equals(deliveryType)){
			List<String> list = new ArrayList<String>();
			String[] arr = deliveryType.substring(1).split("_");
			for(String s : arr){
				list.add(s);
			}
			try {
				re = JSONUtil.serialize(list);
			} catch (JSONException e) {
			}
		}
		return re;
	}
}
