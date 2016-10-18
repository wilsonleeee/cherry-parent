/*	
 * @(#)BINOLCPCOM02_FN.java     1.0 2011/7/18		
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
package com.cherry.cp.common.function;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.CreateRule_IF;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;

/**
 * 会员活动共通 Function
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM02_FN implements FunctionProvider, ApplicationContextAware {
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
    private BINOLJNCOM03_IF binoljncom03IF;
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String init(Map<String, Object> map) throws Exception {
		// 取得页面显示的活动模板List
		List<Map<String, Object>> camTempList = null;
		// 需要转换
		if ("1".equals(map.get("covertKbn"))) {
			camTempList = binolcpcom02IF.convertCamTempList(map);
		} else {
			// 取得页面显示的活动模板List
			camTempList = binolcpcom02IF.searchCamTempList(map);
		}
		map.put("newCamTempList", camTempList);
		return "success";
	}
	
	/**
	 * <p>
	 * 返回画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String backInit(Map<String, Object> map) throws Exception {
		// 模板初期显示区分
		map.put("tempInitKbn", "1");
		// 升级是否选择标志
		map.put("checkFlag", "1");
		// 取得页面显示的活动模板List
		List<Map<String, Object>> camTempList = binolcpcom02IF.convertCamTempList(map);
		map.put("newCamTempList", camTempList);
		return "success";
	}
	
	/**
	 * <p>
	 * 返回创建活动首页
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String backTop(Map<String, Object> map) {
		return "BINOLCPCOM01";
	}
	
	/**
	 * <p>
	 * 保存会员活动
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String save(Map<String, Object> map) {
		String saveResult = "0";
		try {
			if("1".equals(map.get("configKbn"))){
				// 会员配置规则保存处理
				binoljncom03IF.tran_saveGrp(map);
			}else{
				// 会员活动保存处理
				binolcpcom02IF.tran_saveCampaign(map);
			}
			// 取得组织品牌代码信息
			//Map<String, Object> orgBrandMap = binolcpcom02IF.getOrgBrandCodeInfo(map);
			// 刷新内存中的规则文件
			//knowledgeEngine.refreshRules((String) orgBrandMap.get("orgCode"), (String) orgBrandMap.get("brandCode"));
		} catch (CherryException e) {
			map.put("cherryException", e);
			saveResult = "1";
		} catch (Exception e) {
			map.put("exception", e);
			saveResult = "1";
		}
		
		// 保存成功
		map.put("saveResult", saveResult);
		return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
	}
	
	/**
	 * <p>
	 * 确认画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String confirmInit(Map<String, Object> map) throws Exception {
		// 模板初期显示区分
		map.put("tempInitKbn", "1");
		// 取得页面显示的活动模板List
		List<Map<String, Object>> camTempList = binolcpcom02IF.convertCamTempList(map);
		map.put("newCamTempList", camTempList);
		return "success";
	}
	
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		String destPage = null;
		Map<String, Object> stepMap = (Map<String, Object>) transientVars
				.get("stepMap");
		WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor)transientVars.get("descriptor");
		Map workflowMetas = workflowDescriptor.getMetaAttributes();
		if (null != workflowMetas) {
			args.putAll(workflowMetas);
		}
		List<Step> stepList = (List<Step>) transientVars.get("currentSteps");
		if (null != stepList && !stepList.isEmpty()) {
			Step step = stepList.get(0);
			StepDescriptor stepDescriptor = workflowDescriptor.getStep(step.getStepId());
			Map stepMetas = stepDescriptor.getMetaAttributes();
			if (null != stepMetas) {
				args.putAll(stepMetas);
			}
		}
		stepMap.putAll(args);
		String templateInitBean = (String) stepMap.get("templateInitBean");
		String ruleFileBean = (String) stepMap.get("ruleFileBean");
		if (null != templateInitBean) {
			// 会员活动基础模板初始化处理
			TemplateInit_IF templateInitIF = (TemplateInit_IF) applicationContext.getBean(templateInitBean);
			stepMap.put("templateInitIF", templateInitIF);
		}
		if (null != ruleFileBean) {
			// 创建规则处理 IF
			CreateRule_IF createRuleIF = (CreateRule_IF) applicationContext.getBean(ruleFileBean);
			stepMap.put("createRuleIF", createRuleIF);
		}
		String method = (String) args.get("method");
		try {
			if ("init".equals(method)) {
				destPage = init(stepMap);
			} else if ("backInit".equals(method)) {
				destPage = backInit(stepMap);
			} else if ("confirmInit".equals(method)) {
				destPage = confirmInit(stepMap);
			} else if ("backTop".equals(method)) {
				destPage = backTop(stepMap);
			} else if ("save".equals(method)) {
				//ps.setInt(arg0, arg1);
				destPage = save(stepMap);
				// 保存时发生异常
				if ("-1".equals(stepMap.get("actionId")) && 
						"1".equals(stepMap.get("saveResult"))) {
					transientVars.put("SaveStatus", -1);
				}
			} 
		} catch (Exception e) {
			stepMap.put("exception", e);
		}
		// 目标页面
		stepMap.put("DEST_PAGE", destPage);
	}

}
