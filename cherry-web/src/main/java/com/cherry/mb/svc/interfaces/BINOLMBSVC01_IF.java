package com.cherry.mb.svc.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.mb.svc.dto.RechargeRuleDTO;
import com.cherry.mb.svc.form.BINOLMBSVC01_Form;

/**
 * 
 * @ClassName: BINOLMBSVC01_IF 
 * @Description: TODO(储值规则管理相关Interface) 
 * @author menghao
 * @version v1.0.0 2016-7-11 
 *
 */
public interface BINOLMBSVC01_IF {
	
	/**
	 * 添加活动
	 * @param allowList 使用此规则的柜台
	 */
	public void tran_addRule(RechargeRuleDTO rechargeRule, List<Map<String, Object>> allowList);
	
	/**
	 * 页面查询
	 */
	public Map<String, Object> getRuleCountInfo(Map<String, Object> map);
	
	/**
	 *获取集合 
	 */
	public List<Map<String, Object>> getRuleList(Map<String, Object> map);
	
	/**
	 * 停用或者启用规则
	 */
	public void tran_enableOrDisableRule(Map<String,Object> map);
	
	/**
	 *获取并设置对应子活动的明细 
	 */
	public Map<String,Object> getRuleDetail(Map<String, Object> map);
	
	/**
	 * 更新规则
	 */
	public int tran_updateRule(BINOLMBSVC01_Form form, UserInfo userInfo) throws Exception;

	/**
	 * 取得所有的区域权限柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllTree(Map<String, Object> map);

	/**
	 * 取得大区信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map);

	/**
	 * 根据大区id取得渠道柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map);
	
}
