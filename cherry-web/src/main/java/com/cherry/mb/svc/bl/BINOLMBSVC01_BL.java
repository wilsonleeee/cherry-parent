package com.cherry.mb.svc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.svc.dto.RechargeRuleDTO;
import com.cherry.mb.svc.form.BINOLMBSVC01_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC01_IF;
import com.cherry.mb.svc.service.BINOLMBSVC01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * @ClassName: BINOLMBSVC01_BL 
 * @Description: TODO(储值规则管理相关BL) 
 * @author menghao
 * @version v1.0.0 2016-7-11 
 *
 */
public class BINOLMBSVC01_BL implements BINOLMBSVC01_IF{

	@Resource
	private BINOLMBSVC01_Service binOLMBSVC01_Service;
	
	@Override
	public void tran_addRule(RechargeRuleDTO rechargeRule, List<Map<String, Object>> allowList) {
		
		Map<String,Object> mainRule=new HashMap<String, Object>();
		mainRule.put(CherryConstants.BRANDINFOID, rechargeRule.getBrandInfoId());
		mainRule.put(CherryConstants.ORGANIZATIONINFOID, rechargeRule.getOrganizationInfoId());
		mainRule.put("discountName", rechargeRule.getRuleName());
		// 默认优惠
		mainRule.put("discountType", "1");
		// 储值类型默认为储值送储值金
		mainRule.put("rechargeType", "".equals(ConvertUtil.getString(rechargeRule.getRechargeType())) ? "GI" : rechargeRule.getRechargeType());
		mainRule.put("discountBeginDate", DateUtil.suffixDate(rechargeRule.getDiscountBeginDate(), 0));
		mainRule.put("discountEndDate", DateUtil.suffixDate(rechargeRule.getDiscountEndDate(), 1));
		mainRule.put("comments", rechargeRule.getComments());
		mainRule.put(CherryConstants.CREATEDBY, rechargeRule.getCreateBy());
		mainRule.put(CherryConstants.CREATEPGM, rechargeRule.getCreatePGM());
		mainRule.put(CherryConstants.UPDATEDBY, rechargeRule.getUpdateBy());
		mainRule.put(CherryConstants.UPDATEPGM, rechargeRule.getUpdatePGM());
		// 插入主规则
		int discountId = binOLMBSVC01_Service.addMainRule(mainRule);
		
		Map<String, Object> detailRule = new HashMap<String, Object>();
		detailRule.put("BIN_DiscountId", discountId);
		detailRule.put("SubDiscountName", rechargeRule.getRuleName());
		detailRule.put("RechargeValueActual", rechargeRule.getRechargeValueActual());
		detailRule.put("RechargeMinValue", rechargeRule.getRechargeValueActual());
		detailRule.put("RechargeMaxValue", rechargeRule.getRechargeValueActual());
		detailRule.put("GiftAmount", rechargeRule.getGiftAmount());
		// 赠送金额模式,注意：暂时只支持 =0 的情况，只有在主表储值方式为【储值送储值金】时有值
		detailRule.put("RechargeModel", "GI".equals(rechargeRule.getRechargeType()) ? "0" : "");
		// 是否额外赠送商品或服务。0:否；1：是（暂不实现，默认为否）
		detailRule.put("IsExtraGift","0");
		detailRule.put("CardType", "1");
		detailRule.put("CardLevel", "1");
		detailRule.put("Channel", "1");
		detailRule.put(CherryConstants.CREATEDBY, rechargeRule.getCreateBy());
		detailRule.put(CherryConstants.CREATEPGM, rechargeRule.getCreatePGM());
		detailRule.put(CherryConstants.UPDATEDBY, rechargeRule.getUpdateBy());
		detailRule.put(CherryConstants.UPDATEPGM, rechargeRule.getUpdatePGM());
		// 插入明细规则
		binOLMBSVC01_Service.addRule(detailRule);
		
		// 写入活动范围相关信息
		for(Map<String, Object> allowMap : allowList) {
			allowMap.put("discountId", discountId);
			// 地点类型，1：柜台
			allowMap.put("placeType", "1");
			allowMap.put(CherryConstants.CREATEDBY, rechargeRule.getCreateBy());
			allowMap.put(CherryConstants.CREATEPGM, rechargeRule.getCreatePGM());
			allowMap.put(CherryConstants.UPDATEDBY, rechargeRule.getUpdateBy());
			allowMap.put(CherryConstants.UPDATEPGM, rechargeRule.getUpdatePGM());
		}
		
		binOLMBSVC01_Service.addRechargeRulePlace(allowList);
		
	}
	
	@Override
	public Map<String, Object> getRuleCountInfo(Map<String, Object> map) {
		return binOLMBSVC01_Service.getRuleCountInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getRuleList(Map<String, Object> map) {
		List<Map<String, Object>> ruleList = binOLMBSVC01_Service.getRuleList(map);
		// 获取指定规则的:储值规则限定柜台数、发送储值业务柜台数、参与储值人数
		List<Map<String, Object>> applyCntCountList = binOLMBSVC01_Service.getApplyCntCount(map);
		List<Map<String, Object>> usedCntCountAndInvolveNumberList = binOLMBSVC01_Service.getUsedCntCountAndInvolveNumber(map);
		String discountId = "";
		for(Map<String,Object> ruleMap : ruleList){
			discountId = ConvertUtil.getString(ruleMap.get("discountId"));
			for(Map<String, Object> applyCntCountMap : applyCntCountList) {
				if(discountId.equals(ConvertUtil.getString(applyCntCountMap.get("discountId")))) {
					ruleMap.put("applyCntCount", ConvertUtil.getInt(applyCntCountMap.get("applyCntCount")));
					break;
				}
			}
			for(Map<String, Object> usedCntCountAndInvolveNumberMap : usedCntCountAndInvolveNumberList) {
				if(discountId.equals(ConvertUtil.getString(usedCntCountAndInvolveNumberMap.get("discountId")))) {
					ruleMap.put("usedCntCount", ConvertUtil.getInt(usedCntCountAndInvolveNumberMap.get("usedCntCount")));
					ruleMap.put("involveNumber", ConvertUtil.getInt(usedCntCountAndInvolveNumberMap.get("involveNumber")));
					break;
				}
			}
			
		}
		return ruleList;
	}
	
	@Override
	public void tran_enableOrDisableRule(Map<String, Object> map) {
		// 第一步停用/启用子规则
		binOLMBSVC01_Service.updateRuleVaild(map);
		// 第二步停用/启用主规则
		binOLMBSVC01_Service.updateMainRuleValid(map);
		
	}
	
	@Override
	public Map<String,Object> getRuleDetail(Map<String, Object> map) {
		 return binOLMBSVC01_Service.getRuleDetail(map);
	}
	
	
	@Override
	public int tran_updateRule(BINOLMBSVC01_Form form, UserInfo userInfo) throws Exception {
		Map<String,Object> mainParams = new HashMap<String, Object>();
		mainParams.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		mainParams.put(CherryConstants.UPDATEPGM, "BINOLMBSVC01");
		mainParams.put("discountId", form.getDiscountId());
		mainParams.put("discountName", form.getRechargeRule().getRuleName());
		mainParams.put("discountBeginDate", DateUtil.suffixDate(form.getRechargeRule().getDiscountBeginDate(), 0));
		mainParams.put("discountEndDate", DateUtil.suffixDate(form.getRechargeRule().getDiscountEndDate(), 1));
		mainParams.put("comments", form.getRechargeRule().getComments());
		// 更新主规则信息
		int result = binOLMBSVC01_Service.updateMainRule(mainParams);
		if(result == 0){
			// 更新失败的情况
			return 0;
		}
		
		Map<String, Object> detailRule = new HashMap<String, Object>();
		detailRule.put("subDiscountId", form.getSubDiscountId());
		// 改为与主规则名称一致
		detailRule.put("subDiscountName", form.getRechargeRule().getRuleName());
		// 充值金额在编辑时不可修改
//		detailRule.put("rechargeValueActual", form.getRechargeRule().getRechargeValueActual());
//		detailRule.put("rechargeMinValue", form.getRechargeRule().getRechargeValueActual());
//		detailRule.put("rechargeMaxValue", form.getRechargeRule().getRechargeValueActual());
		detailRule.put("giftAmount", form.getRechargeRule().getGiftAmount());
		detailRule.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		detailRule.put(CherryConstants.UPDATEPGM, "BINOLMBSVC01");
		// 更新子规则
		result = binOLMBSVC01_Service.updateRule(detailRule);
		if(result == 0){
			// 更新失败的情况
			return 0;
		}
		
		// 操作柜台树相关内容
		// 1、先删除旧的规则使用的柜台
		binOLMBSVC01_Service.delRechargeRulePlace(form.getDiscountId());
		// 2、插入新的规则使用的柜台
		String allowArray = form.getAllowNodesArray();
        // 将参数由String类型装换成json类型
        List<Map<String, Object>> allowList = (List<Map<String, Object>>) JSONUtil.deserialize(allowArray);
        for(Map<String, Object> allowMap : allowList) {
			allowMap.put("discountId", form.getDiscountId());
			// 地点类型，1：柜台
			allowMap.put("placeType", "1");
			allowMap.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
			allowMap.put(CherryConstants.CREATEPGM, "BINOLMBSVC01");
			allowMap.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
			allowMap.put(CherryConstants.UPDATEPGM, "BINOLMBSVC01");
		}
		
		binOLMBSVC01_Service.addRechargeRulePlace(allowList);
        
        return 1;
	}
	
	/**
     * 取得所有的区域权限柜台树
     * 
     * @param map
     * @return 区域柜台树
     */
    @Override
	public List<Map<String, Object>> getAllTree(Map<String, Object> map) {
		List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
		String selMode = map.get("selMode").toString();
		List<Map<String, Object>> resultList = null;
		List<Map<String, Object>> counterOrganiztionIdList = binOLMBSVC01_Service
				.getCounterOrganiztionId(map);
		if ("1".equals(selMode)) {
			resultList = binOLMBSVC01_Service.getAllCounter(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			String[] keys4 = { "organizationId", "counterName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			keysList.add(keys4);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
			checkedTarget(resultTreeList, counterOrganiztionIdList);
		} else if ("3".equals(selMode)) {
			resultList = binOLMBSVC01_Service.getDepartCntList(map);
			// 把线性的结构转化为树结构
			resultTreeList = ConvertUtil.getTreeList(resultList, "nodes");
			checkedTarget(resultTreeList, counterOrganiztionIdList);
		}

		return resultTreeList;
	}
    
	/**
	 * 取得大区信息
	 * 
	 * @param map
	 * @return
	 */
    @Override
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		return binOLMBSVC01_Service.getRegionList(map);
	}

	/**
	 * 根据大区id取得渠道柜台树
	 * 
	 * @param map
	 * @return
	 */
    @Override
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
		// 查询区域信息List
		List<Map<String, Object>> channelList = binOLMBSVC01_Service
				.getChannelCntList(map);
		List<Map<String, Object>> counterOrganiztionIdList = binOLMBSVC01_Service
				.getCounterOrganiztionId(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "organizationId", "departName" };
		keysList.add(keys1);
		keysList.add(keys2);
		List<Map<String, Object>> channelTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(channelList, channelTreeList, keysList,
				0);
		checkedTarget(channelTreeList, counterOrganiztionIdList);
		return channelTreeList;
	}
    
	/**
	 * 标记已下发过的柜台
	 * 
	 * @param list
	 *            :柜台树
	 * @param targetList
	 *            ：已下发过的目标柜台
	 */
	private boolean checkedTarget(List<Map<String, Object>> list,
			List<Map<String, Object>> targetList) {
		boolean flag = false;
		if(null == list || 0 == list.size()){
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (map.containsKey("nodes")) {
				List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map
						.get("nodes");
				if (checkedTarget(nodesList, targetList)) {
					map.put("checked", true);
					flag = true;
				}
			} else {
				if(null == targetList || targetList.size() == 0){
					return false;
				}
				for (int l = 0; l < targetList.size(); l++) {
					if (map.get("id").equals(
							targetList.get(l).get("organizationId"))) {
						map.put("checked", true);
						flag = true;
					}
				}
			}
		}
		return flag;
	}

}
