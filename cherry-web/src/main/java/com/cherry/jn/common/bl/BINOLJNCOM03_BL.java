/*	
 * @(#)BINOLJNCOM03_BL.java     1.0 2011/4/18		
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
package com.cherry.jn.common.bl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.CreateRule_IF;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.RuleToolUtil;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.cherry.jn.common.service.BINOLJNCOM03_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员活动组添加 BL
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM03_BL implements BINOLJNCOM03_IF, ApplicationContextAware{
	
	protected static final Logger logger = LoggerFactory.getLogger(BINOLJNCOM03_BL.class);
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLJNCOM03_Service binoljncom03_Service;
	
	@Resource
	private BINOLCM05_Service binOLCM05_Service;
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/**
	 * 取得活动组对应的模板List
	 * 
	 * @param map
	 * @return 模板List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getGrpTemplateList(Map<String, Object> map) throws Exception {
		// 活动类型
		String campaignType = (String) map.get("campaignType");
		// 模板类型
		String templateType = "CP" + campaignType;
		map.put("templateType", templateType);
		String templateInitBeanName = "grpJonInit";
		if("3".equals(campaignType)){
			// 创建初始化处理 IF
			templateInitBeanName = "grpPointInit";
		}
		TemplateInit_IF templateInitIF = getBeanByName(templateInitBeanName);
		if (null == templateInitIF) {
			throw new CherryException("ECP00006");
		}
		map.put("templateInitIF", templateInitIF);
		// 模板初期显示区分
		map.put("tempInitKbn", "1");
		// 取得页面对应的活动模板List
		List<Map<String, Object>> templateList = binolcpcom02IF.searchCamTempList(map);
		return templateList;
	}
	
	/**
	 * 会员活动组保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void tran_saveGrp(Map<String, Object> map) throws Exception {
		// 插表时的共通字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		// 系统时间
		String sysDate = binoljncom03_Service.getSYSDate();
		// 作成日时
		insertMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		insertMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		insertMap.put(CherryConstants.CREATEPGM, "BINOLJNCOM03");
		// 更新程序名
		insertMap.put(CherryConstants.UPDATEPGM, "BINOLJNCOM03");
		map.putAll(insertMap);
		// 规则体详细
		String ruleDetail = (String) map.get("ruleDetail");
		// 取得活动基本信息保存到ruleDetail中
		Map<String, Object> baseParams = new HashMap<String, Object>();
		// 初始化bean
		baseParams.put("templateInitBean", map.get("templateInitBean"));
		// 模块位置
		baseParams.put("templateName", map.get("templateName"));
		// 详细画面pageId
		baseParams.put("pageId", map.get("detailPageId"));
		// 规则详细
		Map<String,Object> ruleDetailMap = (Map<String,Object>) JSONUtil.deserialize(ruleDetail);
		// 规则详细中插入基本信息
		ruleDetailMap.put("baseParams", baseParams);
		// keyName移动到基本参数Map
		baseParams.put("keyName", ruleDetailMap.get("keyName"));
		ruleDetailMap.remove("keyName");
		map.put("ruleDetail", JSONUtil.serialize(ruleDetailMap));
		// 活动组名次
		map.put("groupName", "ruleConfig" + map.get("campaignType"));
		// 会员活动组ID
		String campaignGrpId = binolcpcom02IF.getRuleConCount(map);;
		if (CherryChecker.isNullOrEmpty(campaignGrpId)) {
			// 插入会员活动组表并返回会员活动组ID
			int campaignGrpIdInt = binoljncom03_Service.insertCampaignGrp(map);
			campaignGrpId = String.valueOf(campaignGrpIdInt);
			map.put("campaignGrpId", campaignGrpId);
			// 更新日期
			map.put("grpUpdateTime", sysDate);
			// 更新回数
			map.put("grpModifyCount", 0);
			int brandInfoId = 0;
			if (!CherryChecker.isNullOrEmpty(map.get("brandInfoId"))) {
				brandInfoId = Integer.parseInt(String.valueOf(map.get("brandInfoId")));
			}
			// 品牌代码
			String brandCode = binOLCM05_Service.getBrandCode(brandInfoId);
			// 活动组代号
			String groupCode = brandCode + campaignGrpIdInt;
			map.put("groupCode", groupCode);
			// 更新活动组代号
			map.put("updateKbn", "2");
			// 更新会员活动组表
			int result = binoljncom03_Service.updateCampaignGrp(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00005");
			}
			// 更新回数
			map.put("grpModifyCount", 1);
		} else {
			// 更新基本信息
			map.put("updateKbn", "1");
			// 更新会员活动组表
			int result = binoljncom03_Service.updateCampaignGrp(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
			int grpModifyCount = 0;
			if (!CherryChecker.isNullOrEmpty(map.get("grpModifyCount"))) {
				grpModifyCount = Integer.parseInt(map.get("grpModifyCount").toString()) + 1;
				map.put("grpModifyCount", grpModifyCount);
			}
			map.put("grpUpdateTime", sysDate);
		}
		// 取得优先级设置信息
		if(!CherryChecker.isNullOrEmpty(map.get("extraInfo"))){
			// 页面信息
			String priorityMes = (String) map.get("extraInfo");
			// 扩展参数
			String extArgs = (String) map.get("extArgs");
			if (!CherryChecker.isNullOrEmpty(extArgs)) {
				Map<String, Object> extArgsInfo = (Map<String,Object>) 
				JSONUtil.deserialize(extArgs);
				// 规则List
				List<Map<String, Object>> ruleList = (List<Map<String, Object>>) extArgsInfo.get("combinationRule");
				if (null != ruleList && !ruleList.isEmpty()) {
					List<Map<String, Object>> extraList = (List<Map<String, Object>>) 
					JSONUtil.deserialize(priorityMes);
					boolean seriaFlag = false;
					for (Map<String, Object> extraMap : extraList) {
						// 活动ID
						String campaignId = (String) extraMap.get("campaignId");
						// 已配置的规则ID
						List ruleKeys = (List) extraMap.get("keys");
						if (null == campaignId || null == ruleKeys) {
							throw new CherryException("ECP00023");
						}
						for (Map<String, Object> ruleInfo : ruleList) {
							// 活动ID
							String campaignId1 = (String) ruleInfo.get("campaignId");
							if (campaignId.equals(campaignId1)) {
								// 附属规则List
								List<Map<String, Object>> extraRuleList = (List<Map<String, Object>>) ruleInfo.get("extraRule");
								if (null != extraRuleList && !extraRuleList.isEmpty()) {
									for (Map<String, Object> extraRuleMap : extraRuleList) {
										ruleKeys.add((String) extraRuleMap.get("campaignId"));
									}
									seriaFlag = true;
								}
							}
						}
					}
					if (seriaFlag) {
						priorityMes = JSONUtil.serialize(extraList);
					}
				}
			}
			// 规则配置优先级信息
			map.put("priorityMes", priorityMes);
			// 活动组ID
			map.put("grpId", campaignGrpId);
			// 更新会员活动组表（优先级设置信息）
			int res = binolcpcom02IF.updateCampaignConfig(map);
			// 更新失败
			if (0 == res) {
				throw new CherryException("ECM00038");
			}
			int grpModifyCount = 0;
			if (!CherryChecker.isNullOrEmpty(map.get("grpModifyCount"))) {
				grpModifyCount = Integer.parseInt(map.get("grpModifyCount").toString()) + 1;
				map.put("grpModifyCount", grpModifyCount);
			}
		}
		// 保存规则文件
		saveRuleFile(map);
	}
	
	/**
	 * 保存规则文件
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	private void saveRuleFile(Map<String, Object> map) throws Exception {
		try {
			// 创建规则处理 IF
			String ruleBeanName = (String) map.get("ruleFileBean");
			if (CherryChecker.isNullOrEmpty(ruleBeanName)) {
				return;
			}
			CreateRule_IF createRuleIF = getBeanByName(ruleBeanName);
			if (null == createRuleIF) {
				throw new CherryException("ECP00017");
			}
			map.put("createRuleIF", createRuleIF);
			// 规则详细
			String ruleDetail = (String) map.get("ruleDetail");
			// 会员活动组ID
			String campaignGrpId = (String) map.get("campaignGrpId");
			if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
				List<RuleBodyDTO> ruleBodyList = new ArrayList<RuleBodyDTO>();
				// 规则详细List
				Map<String,Object> ruleDetailMap = (Map<String,Object>) JSONUtil.deserialize(ruleDetail);
				// 解析规则体详细信息
				List<Map<String,Object>> ruleDetailList = binolcpcom02IF.deseRuleDetailMap(ruleDetailMap);
				// 取得规则内容
				binolcpcom02IF.getRuleContents(ruleDetailList, map, ruleBodyList);
				if (!ruleBodyList.isEmpty()) {
					
					// 规则主体
					StringBuffer buffer = new StringBuffer();
					// 规则模板集合
					Map<String, Object> ruleTemplates = new HashMap<String, Object>(); 
					// 模板查询条件集合
					Map<String, Object> tempSearch = new HashMap<String, Object>();
					// 所属品牌
					tempSearch.put("brandInfoId", map.get("brandInfoId"));
					// 所属组织
					tempSearch.put("organizationInfoId", map.get("organizationInfoId"));
					// 会员活动类型
					tempSearch.put("campaignType", map.get("campaignType"));
					// 模板区分:文件头
					tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_0);
					// 模板类型:活动组
					tempSearch.put("templateType", CherryConstants.TEMPLATE_TYPE_1);
					// 取得文件头的规则模板
					String headerTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
					if (CherryChecker.isNullOrEmpty(headerTemplate)) {
						throw new CherryException("ECP00005");
					}
					Map<String, Object> headerMap = new HashMap<String, Object>();
					// 包名称
					String packageName = "PKG_GRP_" + campaignGrpId;
					headerMap.put("PACKAGE_NAME", packageName);
					String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
					if (CherryChecker.isNullOrEmpty(headerRule)) {
						throw new CherryException("ECP00006");
					}
					buffer.append(headerRule).append("\n\n");
					// 规则执行顺序
					int salience = CampUtil.MAX_SALIENCE_GROUP;
					int index = 0;
					for(int i = 0; i < ruleBodyList.size(); i++) {
						RuleBodyDTO ruleBody = ruleBodyList.get(i);
						// 扩展参数集合
						Map<String, Object> extParams = ruleBody.getExtParams();
						if (null == extParams) {
							extParams = new HashMap<String, Object>();
						}
						// 集合标识符
						String groupCode = ruleBody.getGroupCode();
						// 规则模板
						String ruleTemplate = (String) ruleTemplates.get(groupCode);
						if (CherryChecker.isNullOrEmpty(ruleTemplate)){
							// 集合标识符
							tempSearch.put("groupCode", groupCode);
							// 模板区分:文件主体
							tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_1);
							// 取得规则模板内容
							ruleTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
							if (CherryChecker.isNullOrEmpty(ruleTemplate)) {
								throw new CherryException("ECP00005");
							}
							ruleTemplates.put(groupCode, ruleTemplate);
						}
						// 默认规则名
						String ruleName = "GRP_" + campaignGrpId + "_" + index;
						index++;
						// 规则名称
						extParams.put("RULE_NAME", ruleName);
						// 规则执行顺序
						extParams.put("RULE_SALIENCE", salience);
						salience--;
						// 生成规则
						String rule = RuleToolUtil.getRuleCondition(ruleBody.getCondition(), ruleTemplate);
						rule = RuleToolUtil.getRule(extParams, rule);
						if (CherryChecker.isNullOrEmpty(rule)) {
							throw new CherryException("ECP00006");
						}
						buffer.append(rule).append("\n\n");
					}
					// 规则文件内容
					String ruleFileContent = buffer.toString();
					map.put("ruleFileContent", ruleFileContent);
					// 更新规则文件内容
					map.put("updateKbn", "0");
					// 更新会员活动组表
					int result = binoljncom03_Service.updateCampaignGrp(map);
					// 更新失败
					if (0 == result) {
						throw new CherryException("ECM00005");
					}
				}
			}
			if (!CherryChecker.isNullOrEmpty(campaignGrpId)) {
				// 刷新一组规则
				knowledgeEngine.refreshGrpRule(Integer.parseInt(campaignGrpId));
			}
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				Throwable t = ((InvocationTargetException) e).getTargetException();
				if (t instanceof CherryException) {
					CherryException ce = (CherryException) t;
					logger.error(ce.getErrMessage());
				}
			} else if (e instanceof CherryException) {
				logger.error(((CherryException) e).getErrMessage());
			} else {
				logger.error(e.getMessage(),e);
			}
			throw new CherryException("ECP00001");
		}
	}
	
	private <T> T getBeanByName(String name) {
		if (!CherryChecker.isNullOrEmpty(name)) {
			T t = (T) this.applicationContext.getBean(name);
			return t;
		}
		return null;
	}
	
	/**
	 * 取得会员活动组信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map getCampaignGrpInfo(Map<String, Object> map) {
		// 取得会员活动组信息
		return binoljncom03_Service.getCampaignGrpInfo(map);
	}
	
	/**
	 * 取得页面显示的规则组模板List
	 * 
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @return 页面显示的规则组模板List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> convertGrpTempList(Map<String, Object> map) throws Exception {
		// 取得页面对应的活动模板List
		List<Map<String, Object>> grpTempList = getGrpTemplateList(map);
		// 规则体详细
		String ruleDetail = (String) map.get("ruleDetail");
		List<Map<String, Object>> camTemps = null;
		try {
			camTemps = (List<Map<String, Object>>) JSONUtil.deserialize(ruleDetail);
		} catch (JSONException e) {
			return grpTempList;
		}
		Map<String, Object> camTempMap = new HashMap<String, Object>();
		// 转换页面提交的活动模板List
		binolcpcom02IF.convertCamTempListToMap(camTemps, camTempMap);
		// 取得页面显示用的活动模板List
		binolcpcom02IF.getCamTempList(grpTempList, camTempMap, map);
		return grpTempList;
	}
	
	/**
	 * 取得会员活动总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getCampaignCount(Map<String, Object> map) {
		return binoljncom03_Service.getCampaignCount(map);
	}
	
	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List getCampaignList (Map<String, Object> map) {
		return binoljncom03_Service.getCampaignList(map);
	}
	
	/**
	 * 取得会员活动总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getPopCampaignCount(Map<String, Object> map) {
		List filterList = new ArrayList();
		// 需要过滤的字段名
		filterList.add("campaignId");
		filterList.add("campaignName");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		return binoljncom03_Service.getCampaignCount(map);
	}
	
	
	/**
	 * 取得会员弹出框中规则信息List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List getPopCampaignList (Map<String, Object> map) throws Exception {
		List filterList = new ArrayList();
		// 需要过滤的字段名
		filterList.add("campaignId");
		filterList.add("campaignName");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		List <Map<String, Object>> campInfoList = binoljncom03_Service.getCampaignList(map);
		for(Map<String, Object> campInfoMap : campInfoList){
			Map<String, Object> campInfo = new HashMap<String, Object>();
			campInfo.put("campaignId", campInfoMap.get("campaignId"));
			campInfo.put("campaignName", campInfoMap.get("campaignName"));
			campInfoMap.put("campInfo", JSONUtil.serialize(campInfo));
		}
		return campInfoList;
	}
	
	/**
	 * 取得规则总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getComCampRuleCount(Map<String, Object> map){
		// 取得规则总数
		return binoljncom03_Service.getComCampRuleCount(map);
	}
	
	/**
	 * 取得规则List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getComCampaignRuleList (Map<String, Object> map) throws Exception{
		// 取得规则List
		List <Map<String, Object>> campInfoList = binoljncom03_Service.getComCampRuleList(map);
		if (null != campInfoList) {
			for(Map<String, Object> campInfoMap : campInfoList){
				campInfoMap.put("campInfo", JSONUtil.serialize(campInfoMap));
			}
		}
		return campInfoList;
	}
	
	/**
     * 取得规则体详细信息
     * 
     * @param map
     * @return
     * 		规则体详细信息
     */
	@Override
    public Map<String, Object> getRuleDetail(Map<String, Object> map) {
    	// 取得规则体详细信息
    	return binoljncom03_Service.getRuleDetail(map);
    }
	
	/**
	 * 取得规则列表
	 * 
	 * @param map
	 * 			brandInfoId : 品牌ID
	 * 			organizationInfoId : 组织ID
	 * 			campaignType : 活动类型
	 * @return List
	 * 			规则列表
	 */
	@Override
	public List<Map<String, Object>> getCampRuleList(Map<String, Object> map) {
		return binoljncom03_Service.getCampRuleList(map);
	}
	
	/**
	 * 取得规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getRuleConfigInfo(Map<String, Object> map) {
		// 取得规则配置信息
		return binoljncom03_Service.getRuleConfigInfo(map);
	}
	
	/**
	 * 取得规则信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getCampRuleInfo (Map<String, Object> map) {
		// 取得规则信息
		return binoljncom03_Service.getCampRuleInfo(map);
	}
	
	/**
	 * <p>
	 * 通过规则ID获取规则信息
	 * </p>
	 * 
	 * 
	 * @param ruleId
	 * 			规则ID
	 * @param ruleList
	 * 			规则列表
	 * @return Map 规则信息
	 * @throws Exception 
	 * 
	 */
	@Override
    public Map<String, Object> findRuleById(String ruleId, List<Map<String, Object>> ruleList) {
    	if (null != ruleId && null != ruleList) {
    		for (Map<String, Object> ruleMap : ruleList) {
    			// 规则ID
    			String campRuleId = String.valueOf(ruleMap.get("campaignId"));
    			if (ruleId.equals(campRuleId)) {
    				return ruleMap;
    			}
    		}
    	}
		return null;
    }
	
	/**
	 * 取得有效的规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getValidConfigInfo(Map<String, Object> map) {
		// 取得有效的规则配置信息
		return binoljncom03_Service.getValidConfigInfo(map);
	}
}
