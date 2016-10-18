/*	
 * @(#)BINOLCPCOM04_BL.java     1.0 2011/7/18		
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
package com.cherry.cp.common.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleTestDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.BINOLCPCOM04_IF;
import com.cherry.cp.common.interfaces.CreateRule_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.service.BINOLCPCOM04_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.RuleToolUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleResultDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM03_IF;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 规则测试共通 BL
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM04_BL implements BINOLCPCOM04_IF, ApplicationContextAware{
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
    private BINBEDRCOM03_IF binbedrcom03IF;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLCPCOM04_Service binolcpcom04_Service;
	
	protected static final Logger logger = LoggerFactory.getLogger(BINOLCPCOM04_BL.class);
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/**
	 * 取得测试画面对应的模板List
	 * 
	 * @param map
	 * @return 模板List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getTemplateList(Map<String, Object> map) throws Exception {
		// 活动类型
		String campaignType = (String) map.get("campaignType");
		// 模板类型
		String templateType = "";
		if("3".equals(campaignType) && map.get("templateType") != null){
			// 模板类型
			templateType = (String) map.get("templateType");
		}else{
			// 模板类型
			templateType = "CP" + campaignType;
		}
		map.put("templateType", templateType);
		// 页面ID
		map.put("pageId", "P00000000010");
		// 取得页面对应的活动模板List
		List<Map<String, Object>> templateList = binolcpcom02IF.searchCamTempList(map);
		return templateList;
	}
	
	/**
	 * 测试规则
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public RuleTestDTO executeRule(Map<String, Object> map) throws Exception {
		try {
			// 取得测试规则的会话
			StatelessKnowledgeSession ksession = loadRule(map);
			if (null == ksession) {
				throw new CherryException("ECP00001");
			}
			// 会员活动基础 DTO
			CampBaseDTO campBaseDTO = new CampBaseDTO();
			// 活动信息
			String camTemps = (String) map.get("camTemps");
			List<Map<String, Object>> camTempList = (List<Map<String, Object>>) 
			JSONUtil.deserialize(camTemps);
			// 加载区分
			String loadKbn = (String) map.get("loadKbn");
			if ("0".equals(loadKbn)) {
				String ruleFileBean = CampUtil.TEMPLATE_BYTYPE.getRuleBeanName(map.get("campaignType"));
				CreateRule_IF createRuleIF = getBeanByName(ruleFileBean);
				if (null == createRuleIF) {
					throw new CherryException("ECP00007");
				}
				map.put("createRuleIF", createRuleIF);
			}
			// 取得测试对象
			getTestDto(camTempList, map, campBaseDTO);
			// 业务日期
			String businessDate = binolcpcom04_Service.getBussinessDate(map);
			campBaseDTO.setBusinessDate(businessDate);
			RuleTestDTO ruleTestDTO = new RuleTestDTO();
			// 会员等级List
			List<Map<String, Object>> LevelList = binolcpcom04_Service.getMemLevelList(map);
			if (null == LevelList) {
				// 匹配规则失败
				ruleTestDTO.setResultFlag(CampUtil.RESULTFLAG_E);
				return ruleTestDTO;
			}
			campBaseDTO.setMemberLevels(LevelList);
			// 测试区分
			campBaseDTO.setTestFlag(CampUtil.TESTFLAG_1);
			List<RuleResultDTO> ruleResultList = new ArrayList<RuleResultDTO>();
			ksession.setGlobal("ruleResultList", ruleResultList);
			List execList = new ArrayList();
			execList.add(campBaseDTO);
		//	List<RuleFilterDTO> filterList = binbedrcom03IF.executeFilter((List) map.get("RuleList"), campBaseDTO);
//			if (null != filterList) {
//				execList.addAll(filterList);
//			}
			// 执行规则
			ksession.execute(execList);
			
			// 匹配到规则
			if (campBaseDTO.isMatchRule()) {
				// 匹配规则成功
				ruleTestDTO.setResultFlag(CampUtil.RESULTFLAG_S);
				// 结果描述
				String resultDpt = campBaseDTO.getResultDpt();
				// 结果描述
				ruleTestDTO.setResultDpt(resultDpt);
				if ("1".equals(loadKbn)) {
					// 规则名称
					String campaignName = (String) map.get("campaignName");
					ruleTestDTO.setResultDetail(campaignName);
				} else {
					// 活动ID组
//					String campaignIds = campBaseDTO.getCampaignIds();
//					if (!CherryChecker.isNullOrEmpty(campaignIds)) {
//						String[] campaignIdArr = campaignIds.split(",");
//						map.put("campaignIdArr", campaignIdArr);
//						// 取得匹配到的规则名称List
//						List<Map<String, Object>> campaignNameList = binolcpcom04_Service.getCampaignNameList(map);
//						StringBuffer buffer = new StringBuffer();
//						if (null != campaignNameList) {
//							for (int i = 0; i < campaignNameList.size(); i++) {
//								Map<String, Object> ruleContentMap = campaignNameList.get(i);
//								if (0 != i) {
//									buffer.append(CampUtil.MARK_COMMA);
//								}
//								buffer.append(ruleContentMap.get("campaignName"));
//							}
//							// 变化原因
//							String resultDetail = buffer.toString();
//							ruleTestDTO.setResultDetail(resultDetail);
//						}
//					}
				}
			} else {
				// 匹配规则失败
				ruleTestDTO.setResultFlag(CampUtil.RESULTFLAG_E);
			}
			return ruleTestDTO;
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				Throwable t = ((InvocationTargetException) e).getTargetException();
				if (t instanceof CherryException) {
					CherryException ce = (CherryException) t;
					logger.error(ce.getErrMessage());
				} else {
					logger.error(t.getMessage(),e);
				}
			} else if (e instanceof CherryException) {
				logger.error(((CherryException) e).getErrMessage());
			} else {
				logger.error(e.getMessage(),e);
			}
			throw new CherryException("ECP00009");
		}
	}
	
	/**
	 * 取得测试对象
	 * 
	 * @param camTempList
	 * 			规则设置内容
	 * @param map
	 * 			参数集合
	 * @param campBaseDTO
	 * 			测试对象
	 * @return 无
	 * @throws Exception 
	 */
	private void getTestDto(List<Map<String, Object>> camTempList, Map<String, Object> map, CampBaseDTO campBaseDTO) throws Exception {
		if (null != camTempList) {
			// 循环规则设置内容
			for (Map<String, Object> camTemp : camTempList) {
				// 规则模板区分
				String ruleTempFlag = (String) camTemp.get("RULE-TEST-FLAG");
				// 有规则模板
				if ("1".equals(ruleTempFlag)) {
					// 创建规则处理 IF
					CreateRule_IF createRuleIF = (CreateRule_IF) map.get("createRuleIF");
					if (null != createRuleIF) {
						// 集合标识符
						String groupCode = (String) camTemp.get("groupCode");
						createRuleIF.invokeTestMd(groupCode + "_Test", map, camTemp, campBaseDTO);
					}
				} else {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
					if (null != combTempList && !combTempList.isEmpty()) {
						// 递归调用
						getTestDto(combTempList, map, campBaseDTO);
					}
				}
			}
		}
	}
	
	/**
	 * 取得测试规则的会话
	 * 
	 * @param map
	 * @return StatelessKnowledgeSession
	 * @throws Exception 
	 */
	private StatelessKnowledgeSession loadRule (Map<String, Object> map) throws Exception {
		// 加载区分
		String loadKbn = (String) map.get("loadKbn");
		// 规则生成bean
		String ruleFileBean = null;
		// 规则详细List
		List<Map<String,Object>> ruleDetailList = null;
		if ("1".equals(loadKbn)) {
			ruleFileBean = CampUtil.TEMPLATE_BYTYPE.getRuleBeanName(map.get("campaignType"));
			ruleDetailList = (List<Map<String,Object>>) map.get("ruleDetailList");
		} else {
			ruleFileBean = "grpCreateRule" + map.get("campaignType");
			// 规则详细
			String ruleDetail = (String) map.get("ruleDetail");
			if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
				ruleDetailList = (List<Map<String,Object>>) 
				JSONUtil.deserialize(ruleDetail);
				
			}
		}
		CreateRule_IF createRuleIF = getBeanByName(ruleFileBean);
		if (null == createRuleIF) {
			throw new CherryException("ECP00007");
		}
		map.put("createRuleIF", createRuleIF);
		
		List<RuleBodyDTO> ruleBodyList = new ArrayList<RuleBodyDTO>();
		// 取得规则内容
		binolcpcom02IF.getRuleContents(ruleDetailList, map, ruleBodyList);
		if (ruleBodyList.isEmpty()) {
			throw new CherryException("ECP00001");
		}
		List<Map<String, Object>> ruleList = new ArrayList<Map<String, Object>>();
		map.put("RuleList", ruleList);
		// 取得规则内容
		InputStream in = getRule(map, ruleBodyList, loadKbn);
		if (null == in) {
			throw new CherryException("ECP00001");
		}
	
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		try {
			kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
			in.close();
			if ("1".equals(loadKbn)) {
				// 取得会员活动组规则
				String grpRule = binolcpcom04_Service.getGrpRuleContent(map);
				if (CherryChecker.isNullOrEmpty(grpRule)) {
					throw new CherryException("ECP00008");
				}
				in = new ByteArrayInputStream(grpRule.getBytes("UTF-8"));
				kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
				in.close();
			} else {
				// 取得规则内容
				List<Map<String, Object>> ruleContentList = binolcpcom04_Service.getRuleContentList(map);
				if (null != ruleContentList) {
					for (Map<String, Object> ruleContentMap : ruleContentList) {
						// 规则内容
						String ruleContent = (String) ruleContentMap.get("ruleFileContent");
						// 会员子活动ID
						String campRuleId = String.valueOf(ruleContentMap.get("campRuleId"));
						// 规则过滤器
						String ruleFilter = (String) ruleContentMap.get("ruleFilter");
						if (!CherryChecker.isNullOrEmpty(ruleContent)) {
							in = new ByteArrayInputStream(ruleContent.getBytes("UTF-8"));
							kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
							in.close();
						}
						if (!CherryChecker.isNullOrEmpty(ruleFilter)) {
							List<Map<String, Object>> filterList = (List<Map<String, Object>>) JSONUtil.deserialize(ruleFilter);
							Map<String, Object> ruleInfo = new HashMap<String, Object>();
							ruleInfo.put("campRuleId", campRuleId);
							ruleInfo.put("filterList", filterList);
							ruleList.add(ruleInfo);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new CherryException("ECP00001");
		} finally {
			if (null != in) {
				in.close();
			}
		}
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					logger.error(error.getMessage(),error);
				}
				throw new CherryException("ECP00001");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
		return ksession;
	}
	
	/**
	 * 取得规则内容
	 * 
	 * @param map
	 * @return StatelessKnowledgeSession
	 * @throws Exception 
	 */
	private InputStream getRule (Map<String, Object> map, List<RuleBodyDTO> ruleBodyList, String loadKbn) throws Exception {
		if (null != ruleBodyList) {
			// 模板类型
			String templateType = null;
			// 包名称
			String packageName = null;
			// 默认规则名
			String preRuleName = null;
			// 默认agenda-group名
			String agendaGrpName = "AgenadaGrp_TEST";
			// 规则测试
			if ("1".equals(loadKbn)) {
				templateType = CherryConstants.TEMPLATE_TYPE_0;
				packageName = "PKG_TEST";
				preRuleName = "Camp_TEST_";
			} else {
				templateType = CherryConstants.TEMPLATE_TYPE_1;
				packageName = "PKG_GRP_TEST";
				preRuleName = "GRP_TEST_";
			}
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
			tempSearch.put("templateType", templateType);
			// 取得文件头的规则模板
			String headerTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
			if (CherryChecker.isNullOrEmpty(headerTemplate)) {
				throw new CherryException("ECP00005");
			}
			Map<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("packageName", packageName);
			String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
			if (CherryChecker.isNullOrEmpty(headerRule)) {
				throw new CherryException("ECP00006");
			}
			buffer.append(headerRule).append("\n\n");
			List<RuleFilterDTO> filterList = new ArrayList<RuleFilterDTO>();
			for(int i = 0; i < ruleBodyList.size(); i++) {
				RuleBodyDTO ruleBody = ruleBodyList.get(i);
				// 扩展参数集合
				Map<String, Object> extParams = ruleBody.getExtParams();
				if (null == extParams) {
					extParams = new HashMap<String, Object>();
				}
				// 默认规则名
				String ruleName = preRuleName + i;
				// 规则过滤器 DTO
				RuleFilterDTO filter = ruleBody.getRuleFilterVal();
				if ("1".equals(loadKbn)) {
					if (null == filter) {
						extParams.put("RULE_FILTER", "");
					} else {
						filter.setRuleName(ruleName);
						filterList.add(filter);
				//		extParams.put("RULE_FILTER", RuleFilterUtil.getRuleFilterVal(ruleName));
					}
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
				
				extParams.put("ruleName", ruleName);
				// 规则测试
				if ("1".equals(loadKbn)) {
					extParams.put("ruleId", String.valueOf(i));
					extParams.put("campaignId", "0");
				}
				extParams.put("ruleSalience", String.valueOf(-1 - i));
				extParams.put("agendaGroupName", agendaGrpName);
				extParams.put("condition", ruleBody.getCondition());
				// 生成规则
				String rule = RuleToolUtil.getRule(extParams, ruleTemplate);
				if (CherryChecker.isNullOrEmpty(rule)) {
					throw new CherryException("ECP00006");
				}
				buffer.append(rule).append("\n\n");
			}
			if (!filterList.isEmpty()) {
				//map.put("FilterList", filterList);
				List<Map<String, Object>> ruleList = (List<Map<String, Object>>) map.get("RuleList");
				Map<String, Object> ruleInfo = new HashMap<String, Object>();
				ruleInfo.put("campRuleId", "0");
				ruleInfo.put("filterList", filterList);
				ruleList.add(ruleInfo);
			}
			InputStream in = new ByteArrayInputStream(buffer.toString().getBytes("UTF-8"));
			return in;
		}
		return null;
	}
	
	private <T> T getBeanByName(String name) {
		if (!CherryChecker.isNullOrEmpty(name)) {
			T t = (T) this.applicationContext.getBean(name);
			return t;
		}
		return null;
	}
}
