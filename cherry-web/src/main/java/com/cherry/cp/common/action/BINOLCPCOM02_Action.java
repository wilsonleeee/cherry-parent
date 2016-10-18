/*	
 * @(#)BINOLCPCOM02_Action.java     1.0 2011/7/18		
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
package com.cherry.cp.common.action;

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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.form.BINOLCPCOM02_Form;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动共通 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM02_Action extends CampBaseAction implements
ModelDriven<BINOLCPCOM02_Form>, ApplicationContextAware{

	private static final long serialVersionUID = 1702852347901987682L;
	
	private static Logger logger = LoggerFactory
	.getLogger(BINOLCPCOM02_Action.class.getName());
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	/** 参数FORM */
	private BINOLCPCOM02_Form form = new BINOLCPCOM02_Form();
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
    private BINOLJNCOM03_IF binoljncom03IF;
	
	@Resource
    private Workflow workflow;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	protected BINOLCM14_BL binOLCM14_BL;
	
	private static ApplicationContext applicationContext;
	
	@Resource(name="binOLCM33_BL")
	private BINOLCM33_BL binOLCM33_BL;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/** 活动模板List */
	private List<Map<String, Object>> camTempList;
	
	/** 所管辖的品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	/** 会员活动组信息List */
	private List<Map<String, Object>> campaignGrpList;
	
	/** 模板页面名称 */
	private String templateName;
	
	/** 导航条区分 */
	private String campStepKbn;
	
	/** 步骤名称 */
	private String[] stepNames;
	
	/** 当前导航条所在步骤 */
	private String onStep;
	
	/** 页面按钮 */
	private String[] campBtns;
	
	/** 当前保存的步骤 */
	private int saveStep = -1;
	
	/** 按钮区分 */
	private String campBtnKbn;
	
	/** 传递的参数 */
	private Map campParams;
	
	@Override
	public BINOLCPCOM02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public List<Map<String, Object>> getCamTempList() {
		return camTempList;
	}

	public void setCamTempList(List<Map<String, Object>> camTempList) {
		this.camTempList = camTempList;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getCampStepKbn() {
		return campStepKbn;
	}

	public void setCampStepKbn(String campStepKbn) {
		this.campStepKbn = campStepKbn;
	}

	public String getOnStep() {
		return onStep;
	}

	public void setOnStep(String onStep) {
		this.onStep = onStep;
	}

	public String getCampBtnKbn() {
		return campBtnKbn;
	}

	public void setCampBtnKbn(String campBtnKbn) {
		this.campBtnKbn = campBtnKbn;
	}
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public Map getCampParams() {
		return campParams;
	}

	public void setCampParams(Map campParams) {
		this.campParams = campParams;
	}
	
	public int getSaveStep() {
		return saveStep;
	}

	public void setSaveStep(int saveStep) {
		this.saveStep = saveStep;
	}
	
	public List<Map<String, Object>> getCampaignGrpList() {
		return campaignGrpList;
	}

	public void setCampaignGrpList(List<Map<String, Object>> campaignGrpList) {
		this.campaignGrpList = campaignGrpList;
	}
	
	public String[] getStepNames() {
		return stepNames;
	}

	public void setStepNames(String[] stepNames) {
		this.stepNames = stepNames;
	}
	
	public String[] getCampBtns() {
		return campBtns;
	}

	public void setCampBtns(String[] campBtns) {
		this.campBtns = campBtns;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String init() throws Exception {
		long wfId = -1;
		String destPage = null;
        try {
        	Map<String, Object> input = new HashMap<String, Object>();
        	Map<String, Object> stepMap = new HashMap<String, Object>();
        	if (null == campParams && !CherryChecker.isNullOrEmpty(form.getCampParamInfo())) {
    			campParams = (Map) JSONUtil.deserialize(form.getCampParamInfo());
    			stepMap.putAll(campParams);
    		}
        	stepMap.put(CampConstants.APPLI_KEY, applicationContext);
        	CampaignDTO campaign = form.getCampInfo();
        	if (!"fromTop".equals(form.getActionKbn())) {
        		// 取得首页上的信息
    			CampaignDTO campInfo = form.getCampInfo();
    			CampaignDTO campaignDTO = (CampaignDTO) session.get("campInfo");
    			if (null != campInfo) {
    				ConvertUtil.convertDTO(campaignDTO, campInfo, false);
    			}
    			stepMap.put("campInfo", campaignDTO);
        	}
        	// 规则配置标志
        	stepMap.put("configKbn", form.getConfigKbn());
        	// 用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 组织代码
    		String orgCode = userInfo.getOrganizationInfoCode();
    		// 品牌代码
    		String brandCode = userInfo.getBrandCode();
    		stepMap.put("OrgCode", orgCode);
    		stepMap.put("BrandCode", brandCode);
    		// 所属组织
    		stepMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
    				.getBIN_OrganizationInfoID());
    		// 品牌ID
    		stepMap.put("brandInfoId", form.getBrandInfoId());
    		// 会员俱乐部ID
    		stepMap.put("memberClubId", form.getMemberClubId());
    		// 用户ID
    		stepMap.put(CherryConstants.USERID, userInfo.getBIN_UserID());
    		// 作成者
    		stepMap.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
    		// 更新者
    		stepMap.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
    		if(null != form.getExtraInfo()){
    			// 额外信息
    			stepMap.put("extraInfo", form.getExtraInfo());
    		}
    		if (!CherryChecker.isNullOrEmpty(form.getExtArgs())) {
    			stepMap.put("extArgs", form.getExtArgs());
    		}
            // 取得活动组Id
            String grpId = binolcpcom02IF.getRuleConCount(stepMap);
            if(null != grpId){
                // 插入活动组id
                stepMap.put("campaignGrpId", grpId);
                // 取得活动组更新次数和时间
                Map<String, Object> grpInfoMap = binoljncom03IF.getCampaignGrpInfo(stepMap);
                // 跳至配置页时用
                form.setGrpModifyCount(String.valueOf(grpInfoMap.get("grpModifyCount")));
                form.setGrpUpdateTime((String) grpInfoMap.get("grpUpdateTime"));
                // 规则配置页用
                stepMap.put("grpModifyCount", grpInfoMap.get("grpModifyCount"));
                stepMap.put("grpUpdateTime", grpInfoMap.get("grpUpdateTime"));
                // 如果是编辑规则配置页，取得规则配置信息
                if("1".equals(form.getConfigKbn()) && "1".equals(form.getConfEditKbn())){
                	// 获得规则信息
	                String ruleDetail = (String) grpInfoMap.get("ruleDetail");
					if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
						try {
							Map<String,Object> campParamMap = (Map<String,Object>) JSONUtil.deserialize(ruleDetail);
							if (null != campParamMap) {
								int i = -1;
								// 取得当前活动信息，规则配置页没有存草稿，所有每次打开都是第一页
								String camTempsInfo = JSONUtil.serialize(campParamMap.get("camTemps0"));
								stepMap.put("camTemps", camTempsInfo);
								// 模板初期显示区分
		    			    	stepMap.put("tempInitKbn", "1");
		    			    	// 是否需要转换标志，一般只有新增的规则不需要转换(转换即把从已存的步骤信息转换为正确的格式)
		    			    	stepMap.put("covertKbn", "1");
								// 向session中放当前规则的已保存信息
								for(Map.Entry<String, Object> en : campParamMap.entrySet()){
									if("baseParams".equals(en.getKey())){
										continue;
									}
									String value = JSONUtil.serialize(en.getValue());
									// 存放活动信息关键字，便于清空session中的活动信息
									String keyName = en.getKey();
									session.put(en.getKey(), value);
									if(null == session.get("keyName")){
										session.put("keyName", keyName);
									}else{
										session.put("keyName", session.get("keyName") + "_" + keyName);
									}
									i++;
								}
								if (i > -1) {
									// 记录当前保存步骤
									saveStep = i;
								}
							}
						}catch(Exception e) {
							logger.error(e.getMessage(),e);
						}
					}
                }
                
            }
    		int onStepInt = 0;
    		if (!CherryChecker.isNullOrEmpty(onStep) && CherryChecker.isNumeric(onStep)) {
    			onStepInt = Integer.parseInt(onStep);
    		}
			// 获得积分模块类型
			if (null != campaign) {
				stepMap.put("templateType", campaign.getTemplateType());
			}
			if (session.containsKey(CampConstants.ALLSUBCAMP)) {
		        // 所有子活动信息
				stepMap.put(CampConstants.ALLSUBCAMP, session.get(CampConstants.ALLSUBCAMP));
			}
			// 新建 
			String optKbnVal = CampConstants.OPT_KBN1;
			if (2 == form.getOptKbn()) {
				// 复制
				if (1 == form.getCopyFlag()) {
					optKbnVal = CampConstants.OPT_KBN3;
					// 编辑
				} else {
					optKbnVal = CampConstants.OPT_KBN2;
				}
			}
			// 操作区分
			stepMap.put(CampConstants.OPT_KBN, optKbnVal);
    		if ("next".equals(form.getActionKbn())) {
    			String campTemps = form.getCamTemps();
    			// 验证信息
    			stepMap.put("camTempsValidate", campTemps);
    			// 模板Key
        		String camTempsKey = "camTemps" + onStepInt;
        		// 保存当前步骤信息到session
    			session.put(camTempsKey, campTemps);
    			// 记录session中保存的步骤内容，用于关闭页面时清除session中保存的步骤信息
    			if(null == session.get("keyName")){
	    			session.put("keyName", camTempsKey);
    			}else{
    				session.put("keyName", session.get("keyName") + "_" + camTempsKey);
    			}
    			Map<String, Object> preSubCamp = (Map<String, Object>) session.get(CampConstants.ALLSUBCAMP);
    			// 取得所有子活动的内容
    			Map<String, Object> allSubCamp = binolcpcom02IF.getAllsubCamp(preSubCamp, campTemps);
    			if (null != allSubCamp && !allSubCamp.isEmpty()) {
    				session.put(CampConstants.ALLSUBCAMP, allSubCamp);
    				stepMap.put(CampConstants.ALLSUBCAMP, allSubCamp);
    			}
    			// 保存步骤大于当前步骤，表示该步骤的下一步的内容已经保存在session中
    			if (saveStep > onStepInt) {
    				//if(form.getOptKbn() == 2){
    					// 模板初期显示区分
    			    	stepMap.put("tempInitKbn", "1");
    				//}
    			    // 当前步骤加一
    				onStepInt++;
					// 模板Key(取得下一步的内容)
	        		camTempsKey = "camTemps" + onStepInt;
    				if (session.containsKey(camTempsKey)) {
            			stepMap.put("camTemps", session.get(camTempsKey));
            			// 覆盖区分
            			stepMap.put("covertKbn", "1");
            		}
    				// 升级是否选择标志
    				stepMap.put("checkFlag", "1");
    			}
    		} else if ("back".equals(form.getActionKbn())) {
    			// 回退，当前步骤减一
    			int prevStepInt = onStepInt - 1;
    			// 模板Key(取得session中上一步的内容)
        		String camTempsKey = "camTemps" + prevStepInt;
        		if (session.containsKey(camTempsKey)) {
        			stepMap.put("camTemps", session.get(camTempsKey));
        		}
        		// 若当前步骤已经保存过，更新session中该步骤的内容
        		if (saveStep >= onStepInt) {
        			// 把该步骤的内容放到session中
        			String camTemps = "camTemps" + onStepInt;
        			session.put(camTemps, form.getCamTemps());
        			if(null == session.get("keyName")){
    	    			session.put("keyName", camTemps);
        			}else{
        				session.put("keyName", session.get("keyName") + "_" + camTemps);
        			}
    			}
    		} else if ("confirm".equals(form.getActionKbn())) {
    			String campTemps = form.getCamTemps();
    			// 到确认画面的操作
    			stepMap.put("camTempsValidate", campTemps);
    			Map<String, Object> preSubCamp = (Map<String, Object>) session.get(CampConstants.ALLSUBCAMP);
    			// 取得所有子活动的内容
    			Map<String, Object> allSubCamp = binolcpcom02IF.getAllsubCamp(preSubCamp, campTemps);
    			if (null != allSubCamp && !allSubCamp.isEmpty()) {
    				session.put(CampConstants.ALLSUBCAMP, allSubCamp);
    				stepMap.put(CampConstants.ALLSUBCAMP, allSubCamp);
    			}
    			// 取得首页上的信息
    			CampaignDTO campInfo = form.getCampInfo();
    			CampaignDTO campaignDTO = (CampaignDTO) session.get("campInfo");
    			ConvertUtil.convertDTO(campaignDTO, campInfo, false);
    			// 根据品牌id取得品牌名称
    			if (CherryChecker.isNullOrEmpty(campaignDTO.getBrandName())) {
	    			Map<String, Object> paramMap = new HashMap<String, Object>();
	    			// 品牌ID
	    			paramMap.put("brandInfoId", form.getBrandInfoId());
	    			// 语言类型
	    			paramMap.put(CherryConstants.SESSION_LANGUAGE, session
	    					.get(CherryConstants.SESSION_LANGUAGE));
	    			// 品牌名称
	    			String brandName = binolcpcom02IF.getBrandName(paramMap);
	    			campaignDTO.setBrandName(brandName);	
    			}
    			if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
    				Map<String, Object> paramMap = new HashMap<String, Object>();
    				// 语言类型
	    			paramMap.put(CherryConstants.SESSION_LANGUAGE, session
	    					.get(CherryConstants.SESSION_LANGUAGE));
	    			paramMap.put("memberClubId", form.getMemberClubId());
    				campaignDTO.setClubName(binOLCM05_BL.getClubName(paramMap));
    			}
    			// 将首页信息放入form中
    			form.setCampInfo(campaignDTO);
    			// 模板Key(将当前步骤信息放入session中)
        		String camTempsKey = "camTemps" + onStepInt;
    			session.put(camTempsKey, form.getCamTemps());
    			if(null == session.get("keyName")){
	    			session.put("keyName", camTempsKey);
    			}else{
    				session.put("keyName", session.get("keyName") + "_" + camTempsKey);
    			}
    			// 所有步骤模板信息List(从session中取得所有步骤信息)
    			List<Map<String, Object>> allCamTempList = new ArrayList<Map<String, Object>>();
    			for (int i = 0; i <= onStepInt; i++) {
    				// 模板Key
            		String key = "camTemps" + i;
            		String camTempsInfo = (String) session.get(key);
            		if (null != camTempsInfo && !"".equals(camTempsInfo)) {
            			List<Map<String, Object>> camTemps = (List<Map<String, Object>>) 
            			JSONUtil.deserialize(camTempsInfo);
            			if (null != camTemps && !camTemps.isEmpty()) {
            				allCamTempList.addAll(camTemps);
            			}
            		}
    			}
    			stepMap.put("allCamTempList", allCamTempList);
    		} else if ("fromTop".equals(form.getActionKbn())) {
    			// 从首页跳转过来的操作
    			 if(!ValidateCamInfo(form.getCampInfo(), stepMap)){
     				// 显示首页
     				topPage(1);
     				// 会员活动
                	if (CampConstants.TYPE_FLAG_1.equals(form.getCampInfo().getCampaignTypeFlag())) {
                		// 俱乐部模式
            			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
            					.getBIN_OrganizationInfoID()), form.getBrandInfoId());
            			if (!"3".equals(clubMod)) {
            				// 取得会员俱乐部List
            				clubList = queryClubList(form.getBrandInfoId());
            			}
                		return "BINOLCPCOM05";
                	}
     				return "BINOLCPCOM01";
     			}
    			// 会员活动信息
     			CampaignDTO campdto = form.getCampInfo();
    			// 会员活动
             	if (CampConstants.TYPE_FLAG_1.equals(campdto.getCampaignTypeFlag())
             			&& (!"2".equals(String.valueOf(form.getOptKbn())) 
             					|| "1".equals(String.valueOf(form.getCopyFlag())))) {
        			if (CherryChecker.isNullOrEmpty(campdto.getCampaignCode(), true)) {
        				// 主题活动代号
        				campdto.setCampaignCode(binolcpcom02IF.getMainCampainCode(stepMap));
        			}
             	}
    			// 模板Key(从session中取得下一步模板信息，编辑和复制的时候可以取到)
        		String camTempsKey = "camTemps" + onStepInt;
				if (session.containsKey(camTempsKey)) {
        			stepMap.put("camTemps", session.get(camTempsKey));
        			stepMap.put("tempInitKbn","1");
        			// 覆盖区分
        			stepMap.put("covertKbn", "1");
        		}
    			// 会员活动基本信息
    			session.put("campInfo", form.getCampInfo());
    			stepMap.put("campInfo", form.getCampInfo());
    		} else if ("save".equals(form.getActionKbn())) {
    			// 保存规则时的操作，非存草稿
    			stepMap.put("saveStatus", "1");
    			// 工作流结束
    			stepMap.put("workFlowId", "-1");
    			stepMap.put("actionId", "-1");
    			// 插入编辑标志，用于后面判断是否显示优先级设置页面
    			stepMap.put("optKbn", form.getOptKbn());
    			// 保存处理时所需要的参数
    			saveParams(stepMap);
    		}
        	input.put("stepMap", stepMap);
        	// 会员活动类型
			String campType = null;
        	if (null != form.getCampInfo()) {
	        	// 会员活动类型
				campType = form.getCampInfo().getCampaignType();
        	}
        	if (null != session.get("wfId") && 0 != form.getActionId()) {
        		wfId = Long.parseLong(session.get("wfId").toString());
        		// 切换了会员活动类型
        		if (null != campType && 
        				!campType.equals(session.get("campType"))) {
        			if (-1 == saveStep) {
	        			// 结束当前工作流实例
	        			workflow.changeEntryState(wfId, WorkflowEntry.COMPLETED);
        			}
        			session.remove("wfId");
        		} else {
        			// 活动类型
        			String subCampType = campaign.getSubCampType();
        			boolean flag = true;
        			if (null != subCampType && !"".equals(subCampType)) {
        				// 工作流名称
        				String osName = workflow.getWorkflowName(wfId);
        				Map<String, Object> nameMap = (Map<String, Object>) JSONUtil.deserialize(osName);
        				if (null != nameMap) {
        					String newName = "campaign" + "_" + subCampType;
        					if (!newName.equals(nameMap.get("FC"))) {
        						// 结束当前工作流实例
        	        			workflow.changeEntryState(wfId, WorkflowEntry.COMPLETED);
        	        			session.remove("wfId");
        	        			session.remove(CampConstants.ALLSUBCAMP);
        	        			flag = false;
        					}
        				}
        			}
        			if (flag) {
        				workflow.doAction_single(wfId, form.getActionId(), input);
        			}
        		}
        	}
        	if (null == session.get("wfId") || 0 == form.getActionId()) {
        		// 工作流文件名称(基本规则)
        		String wfPrefix = "campaign";
        		// 规则配置
        		if("1".equals(form.getConfigKbn())){
        			wfPrefix = "grpTemp";
        		}
        		// 工作流文件名
        		String wfFile = wfPrefix + campType;
        		if (null != campaign) {
        			// 活动类型
        			String subCampType = campaign.getSubCampType();
        			if (!CherryChecker.isNullOrEmpty(subCampType, true)) {
        				wfFile = wfPrefix + "_" + subCampType;
        			}
        		}
        		// 工作流名称
        		String wfName = ConvertUtil.getWfName(orgCode, brandCode, wfFile);
        		// 初始化工作流
	        	wfId = workflow.initialize(wfName, 1, input);
	            session.put("wfId", wfId);
        	}
        	if (null != campType){
        		// 会员活动类型
    			session.put("campType", campType);
        	}
        	Exception exc = (Exception) stepMap.get("exception");
        	if (null != exc) {
        		logger.error(exc.getMessage(),exc);
        		throw exc;
        	}
        	// 打印错误信息
        	printErrMsg(stepMap);
        	// 异常信息处理
        	CherryException cherryExc = (CherryException) stepMap.get("cherryException");
        	if (null != cherryExc) {
        		this.addActionError(cherryExc.getErrMessage());
        	}
        	// 保存成功
        	if ("save".equals(form.getActionKbn()) && "0".equals(stepMap.get("saveResult"))) {
        		saveStep = Integer.parseInt(onStep);
        		// 处理成功
        		this.addActionMessage(getText("ICM00002"));
        	}
        	// 目标页面
            destPage = (String) stepMap.get("DEST_PAGE");
            // 判断是否跳至优先级配置页面
            if ("save".equals(form.getActionKbn()) && "0".equals(stepMap.get("saveResult"))) {
            	// 读取工作流中配置的优先级设置标志
        		if("1".equals(stepMap.get("priorityPageFlag")) ){
        			CampaignDTO campaignDTO = (CampaignDTO) stepMap.get("campInfo");
                	// 积分规则标志
                	String pointRuleFlag = campaignDTO.getPointRuleType();
        			// 活动组ID
        			form.setCampaignId((String.valueOf(((Map<String, Object>) stepMap.get("campSaveMap")).get("campaignId"))));
        			String campaignType = (String) stepMap.get("campaignType");
        			// 活动类型
        			form.setCampaignType(campaignType);
        			// 积分规则
        			if ("3".equals(campaignType)) {
        				boolean refreshFlag = false; 
        				// 默认规则区分
        				String defaultFlag = campaignDTO.getDefaultFlag();
        				// 如果是新建活动跳至配置页并且不是附属规则
	        			if((!"2".equals(String.valueOf(form.getOptKbn())) || 
	        					"1".equals(String.valueOf(form.getCopyFlag()))) && !"2".equals(pointRuleFlag)){
	        					// 默认积分规则
	        					if ("1".equals(defaultFlag)) {
	        						Map<String, Object> searchMap = new HashMap<String, Object>();
	    	        				// 品牌ID
	    	        				String brandInfoId = (String) stepMap.get("brandInfoId");
	    	        				searchMap.put("brandInfoId", brandInfoId);
	    	        				searchMap.put("memberClubId", stepMap.get("memberClubId"));
	    	        				// 组织ID
	    	        				searchMap.put("organizationInfoId", stepMap.get("organizationInfoId"));
	    	        				// 活动类型
	    	        				searchMap.put("campaignType", campaignType);
	    	        				// 取得规则配置列表
	    	        				List<Map<String, Object>> ruleConfList = binolcpcom02IF.getRuleConfList(searchMap);
	    	        				if (null != ruleConfList && !ruleConfList.isEmpty()) {
	    	        					for (Map<String, Object> ruleConf : ruleConfList) {
	    	        						Map<String, Object> priorityMap = new HashMap<String, Object>();
		    	        					// 规则ID
		    	        					String campaignId = form.getCampaignId();
		    	        					priorityMap.put("campaignId", campaignId);
		    	        					List<String> keys = new ArrayList<String>();
		    	        					keys.add(campaignId);
		    	        					priorityMap.put("keys", keys);
		    	        					// 执行策略
		    	        					if (null != ruleConf.get("execType")) {
		    	        						priorityMap.put("execType", String.valueOf(ruleConf.get("execType")));
		    	        					}
		    	        					// 配置内容
		    	        					String grpRuleDetail = (String) ruleConf.get("grpRuleDetail");
		    	        					if (!CherryChecker.isNullOrEmpty(grpRuleDetail, true)) {
		    	        						Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpRuleDetail);
		    	        						if (null != detailInfo && !detailInfo.isEmpty()) {
		    	        							// 积分上限
		    	        				    		Map<String, Object> pointLimit = (Map<String, Object>) detailInfo.get("limitInfo");
		    	        				    		if (null != pointLimit && !pointLimit.isEmpty()) {
		    	        				    			priorityMap.put("pointLimit", pointLimit);
		    	        				    		}
		    	        						}
		    	        					}
		    	        					priorityMap.put("defaultFlag", "1");
		    	        					// 规则内容
		    	        					String priority = (String) ruleConf.get("priorityRuleDetail");
		    	        					if (CherryChecker.isNullOrEmpty(priority, true)) {
		    	        						List<Map<String, Object>> priorityList = new ArrayList<Map<String, Object>>();
		    	        						priorityList.add(priorityMap);
		    	        						priority = JSONUtil.serialize(priorityList);
		    	        					} else {
		    	        						List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priority);
		    	        						if(null == priorityList){
		    	        							priorityList = new ArrayList<Map<String, Object>>();
		    	        						}
		    	        						// 优先级最低，放在最后
		    	        						priorityList.add(priorityMap);
		    	        						priority = JSONUtil.serialize(priorityList);
		    	        					}
		    	        					ruleConf.put("priorityRuleDetail", priority);
		    	        					// 作成程序名
		    	        					ruleConf.put(CherryConstants.CREATEPGM, "BINOLCPCOM02");
		    	        					// 更新程序名
		    	        					ruleConf.put(CherryConstants.UPDATEPGM, "BINOLCPCOM02");
		    	        					// 作成者
		    	        					ruleConf.put(CherryConstants.CREATEDBY, "BINOLCPCOM02");
		    	        					// 更新者
		    	        					ruleConf.put(CherryConstants.UPDATEDBY, "BINOLCPCOM02");
		    	        					int result = binolcpcom02IF.updateConfPriority(ruleConf);
		    	        					if (0 == result) {
		    	        						throw new CherryException("ECM00038");
		    	        					}
		    	        					if ("1".equals(ruleConf.get("validFlag"))) {
		    	        						refreshFlag = true;
		    	        					}
	    	        					}
	    	        				}
	        					} else {
	        						destPage = "BINOLJNCOM05";
	        					}
	        			}else if ("2".equals(String.valueOf(form.getOptKbn())) && "0".equals(String.valueOf(form.getCopyFlag()))) {
        					// 编辑时未进行配置则跳至配置页
	        				List<Map<String, Object>> priorityList = binolcpcom02IF.getPriorityByIdMap(form.getCampaignId());
	        				if(!binolcpcom02IF.isContain(form.getCampaignId(), priorityList)){
	        					// 非附属规则和默认积分规则
	        					if (!"2".equals(pointRuleFlag) && !"1".equals(defaultFlag)) {
	        						destPage = "BINOLJNCOM05";
	        					}
	        				} else {
	        					refreshFlag = true;
	        				}
        				}
	        			if ("BINOLJNCOM05".equals(destPage)) {
	        				Map<String, Object> searchMap = new HashMap<String, Object>();
	        				// 品牌ID
	        				String brandInfoId = (String) stepMap.get("brandInfoId");
	        				// 会员俱乐部ID
	        				String memberClubId = (String) stepMap.get("memberClubId");
	        				searchMap.put("brandInfoId", brandInfoId);
	        				searchMap.put("memberClubId", memberClubId);
	        				// 组织ID
	        				searchMap.put("organizationInfoId", stepMap.get("organizationInfoId"));
	        				// 活动类型
	        				searchMap.put("campaignType", campaignType);
	        				// 取得规则配置列表
	        				List<Map<String, Object>> ruleConfList = binolcpcom02IF.getRuleConfList(searchMap);
	        				if (null != ruleConfList && !ruleConfList.isEmpty()) {
	        					List<Map<String, Object>> ruleConfigList = new ArrayList<Map<String, Object>>();
	        					for (Map<String, Object> ruleConf : ruleConfList) {
	        						Map<String, Object> ruleConfigMap = new HashMap<String, Object>();
	        						ruleConfigMap.put("campaignGrpId", String.valueOf(ruleConf.get("campaignGrpId")));
	        						ruleConfigMap.put("groupName", ruleConf.get("groupName"));
	        						ruleConfigList.add(ruleConfigMap);
	        					}
	        					form.setRuleConfigList(ruleConfigList);
	        				}
	        				form.setBrandInfoId(brandInfoId);
	        				form.setMemberClubId(memberClubId);
	        			}
	        			if (refreshFlag) {
	        				// 刷新单个规则文件
	        				knowledgeEngine.refreshRule(Integer.parseInt(form.getCampaignId()));
	        			}
        			} else {
        				if (!"8888".equals(campaignType)) {
		        			// 如果是新建活动跳至配置页
		        			if(!"2".equals(String.valueOf(form.getOptKbn())) || "1".equals(String.valueOf(form.getCopyFlag()))){
		        					destPage = "popPriority";
		        			}else if("2".equals(String.valueOf(form.getOptKbn())) && "0".equals(String.valueOf(form.getCopyFlag()))){
		        				// 编辑时若没有进行配置则跳至配置页
		        				List<Map<String, Object>> priorityList = binolcpcom02IF.getPriorityByIdMap(form.getCampaignId());
		        				if(!binolcpcom02IF.isContain(form.getCampaignId(), priorityList)){
			        				destPage = "popPriority";
		        				} 
		        			}
        				} else {
        					if(!"2".equals(String.valueOf(form.getOptKbn())) || "1".equals(String.valueOf(form.getCopyFlag()))){
	        					Map<String, Object> searchMap = new HashMap<String, Object>();
		        				// 品牌ID
		        				String brandInfoId = (String) stepMap.get("brandInfoId");
		        				searchMap.put("brandInfoId", brandInfoId);
		        				searchMap.put("memberClubId", stepMap.get("memberClubId"));
		        				// 组织ID
		        				searchMap.put("organizationInfoId", stepMap.get("organizationInfoId"));
		        				// 活动类型
		        				searchMap.put("campaignType", campaignType);
		        				// 取得规则配置列表
		        				List<Map<String, Object>> ruleConfList = binolcpcom02IF.getRuleConfList(searchMap);
		        				if (null != ruleConfList && !ruleConfList.isEmpty()) {
		        					for (Map<String, Object> ruleConf : ruleConfList) {
			        					if ("1".equals(ruleConf.get("validFlag"))) {
			        						Map<String, Object> priorityMap = new HashMap<String, Object>();
		    	        					// 规则ID
		    	        					String campaignId = form.getCampaignId();
		    	        					priorityMap.put("campaignId", campaignId);
		    	        					List<String> keys = new ArrayList<String>();
		    	        					keys.add(campaignId);
		    	        					priorityMap.put("keys", keys);
		    	        					// 规则内容
		    	        					String priority = (String) ruleConf.get("priorityRuleDetail");
		    	        					if (CherryChecker.isNullOrEmpty(priority, true)) {
		    	        						List<Map<String, Object>> priorityList = new ArrayList<Map<String, Object>>();
		    	        						priorityList.add(priorityMap);
		    	        						priority = JSONUtil.serialize(priorityList);
		    	        					} else {
		    	        						List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priority);
		    	        						if(null == priorityList){
		    	        							priorityList = new ArrayList<Map<String, Object>>();
		    	        						}
		    	        						// 优先级最低，放在最后
		    	        						priorityList.add(priorityMap);
		    	        						priority = JSONUtil.serialize(priorityList);
		    	        					}
		    	        					ruleConf.put("priorityRuleDetail", priority);
		    	        					// 作成程序名
		    	        					ruleConf.put(CherryConstants.CREATEPGM, "BINOLCPCOM02");
		    	        					// 更新程序名
		    	        					ruleConf.put(CherryConstants.UPDATEPGM, "BINOLCPCOM02");
		    	        					// 作成者
		    	        					ruleConf.put(CherryConstants.CREATEDBY, "BINOLCPCOM02");
		    	        					// 更新者
		    	        					ruleConf.put(CherryConstants.UPDATEDBY, "BINOLCPCOM02");
		    	        					int result = binolcpcom02IF.updateConfPriority(ruleConf);
		    	        					if (0 == result) {
		    	        						throw new CherryException("ECM00038");
		    	        					}
		    	        				}
		        					}
		        				}
        					}
        				}
	        			if (!"popPriority".equals(destPage)) {
	        				// 刷新单个规则文件
	        				knowledgeEngine.refreshRule(Integer.parseInt(form.getCampaignId()));
	        			}
        			}
        		}
            }
            // 下一步动作
        	String nextAction = (String) stepMap.get("nextAction");
        	if (!CherryChecker.isNullOrEmpty(nextAction)) {
        		form.setNextAction(Integer.parseInt(nextAction));
        	}
        	// 存草稿
        	String saveAction = (String) stepMap.get("saveAction");
        	if (!CherryChecker.isNullOrEmpty(saveAction)) {
        		form.setSaveActionId(Integer.parseInt(saveAction));
        	}
            if ("BINOLCPCOM01".equals(destPage)) {
            	CampaignDTO campaignDTO = (CampaignDTO) session.get("campInfo");
            	// 跳至首页
            	form.setCampInfo(campaignDTO);
            	// 会员活动
            	if (null != campaignDTO && 
            			CampConstants.TYPE_FLAG_1.equals(campaignDTO.getCampaignTypeFlag())) {
            		// 俱乐部模式
        			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
        					.getBIN_OrganizationInfoID()), form.getBrandInfoId());
        			if (!"3".equals(clubMod)) {
        				// 取得会员俱乐部List
        				clubList = queryClubList(form.getBrandInfoId());
        			}
            		destPage = "BINOLCPCOM05";
            	}
            	// 会员活动首页显示设置
        		topPage(1);
            } else {
            	// 取得所有步骤模板的内容
	        	if (stepMap.containsKey("newCamTempList")) {
	        		camTempList = (List<Map<String, Object>>) stepMap.get("newCamTempList");
	        	}
	        	// 导航条
	        	String navigation = (String) stepMap.get("navigation");
	        	if (!CherryChecker.isNullOrEmpty(navigation)) {
	        		stepNames = navigation.split(",");
	        	}
	        	// 页面按钮
	        	String pageButtons = (String) stepMap.get("pageButtons");
	        	if (!CherryChecker.isNullOrEmpty(pageButtons)){
	        		campBtns = pageButtons.split(",");
	        	}
	        	// 取得模板的名称
	        	templateName = (String) stepMap.get("templateName");
	        	//campStepKbn = (String) stepMap.get("campStepKbn");
	        	// 当前步骤
	        	onStep = (String) stepMap.get("onStep");
	        	//campBtnKbn = (String) stepMap.get("campBtnKbn");
	        	// 上一步动作
	        	String backAction = (String) stepMap.get("backAction");
	        	// 编辑时执行的动作
	        	String editInitAction = (String) stepMap.get("editInitAction");
	        	if (!CherryChecker.isNullOrEmpty(backAction)) {
	        		form.setBackAction(Integer.parseInt(backAction));
	        	}
	        	if (!CherryChecker.isNullOrEmpty(editInitAction)) {
	        		int editInitActionId = Integer.parseInt(editInitAction);
	        		form.setActionId(editInitActionId);
	        		form.setInitActionId(editInitActionId);
	        	}
            }
        }
        catch (Exception e) {
            throw e;
            
        }
		return destPage;
	}
	
	/**
	 * <p>
	 * 画面初期显示(编辑)
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String editInit() throws Exception {
		Map<String, Object> input = new HashMap<String, Object>();
    	Map<String, Object> stepMap = new HashMap<String, Object>();
    	input.put("stepMap", stepMap);
		// 取得页面上传递过来的信息
		if (null == campParams && !CherryChecker.isNullOrEmpty(form.getCampParamInfo())) {
			campParams = (Map) JSONUtil.deserialize(form.getCampParamInfo());
			stepMap.putAll(campParams);
		} else {
			form.setOptKbn(2);
			// 活动ID
			stepMap.put("campaignId", form.getCampaignId());
		}
		// 清空session中原有的活动信息
    	if(null != session.get("keyName")){
    		String camTemps = (String) session.get("keyName");
        	if(!"".equals(camTemps)){
        		String[] camTempsArr = camTemps.split("_");
            	for(String camtemp : camTempsArr){
            		if(null != session.get("keyName")){
            			if(null != session.get(camtemp)){
                			session.remove(camtemp);
            			}
            		}
            	}
        	}
        	session.remove("keyName");
        	session.remove(CampConstants.ALLSUBCAMP);
    	}
		// 取得会员活动信息
		CampaignDTO campaignInfo = binolcpcom02IF.getCampaignInfo(stepMap);
		if (null != campaignInfo) {
			// 会员活动
			if ("1".equals(campaignInfo.getCampaignTypeFlag())) {
				// 取得会员活动扩展信息
				binolcpcom02IF.getCampaignExtInfo(campaignInfo);
			}
			form.setCampInfo(campaignInfo);
	    	if(form.getCopyFlag() != 1){
	    		// 编辑时取得已保存信息
	        	Map<String, Object> saveInfo = binolcpcom02IF.getCampSaveInfo(stepMap);
	        	String campSave = JSONUtil.serialize(saveInfo);
	        	form.setCampSaveInfo(campSave);
	        } else {
	        	campaignInfo.setCampaignId(null);
	        	campaignInfo.setCampaignName("");
	        	campaignInfo.setCampaignCode(null);
	        	campaignInfo.setDescriptionDtl("");
				// 用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 创建者
				campaignInfo.setCampaignSetBy(userInfo.getBIN_UserID());
				// 创建者名称
				campaignInfo.setCampaignSetByName(userInfo.getLoginName());
//				form.getCampInfo().setCampaignFromDate("");
//				form.getCampInfo().setCampaignToDate("");
	        }
	    	CampaignRuleDTO campaignRuleDTO = new CampaignRuleDTO();
	    	form.setCampaignRule(campaignRuleDTO);
			// 取得规则体详细信息
			String ruleDetail = campaignInfo.getRuleDetail();
			if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
				try {
					Map<String,Object> campParamMap = (Map<String,Object>) JSONUtil.deserialize(ruleDetail);
					if (null != campParamMap) {
						int i = -1;
						String camTempsInfo = null;
						// 取得当前活动信息关键字
						Map<String, Object> baseParam = (Map<String, Object>) campParamMap.get("baseParams");
						////////////////////////////////////////////////////////////////////////////////////////
						// 是否开启组合奖励
						String groupFlag = ConvertUtil.getString(baseParam.get(CampConstants.GROUP_FLAG));
						if("".equals(groupFlag)){
							campaignInfo.setGroupFlag("0");
						}else{
							campaignInfo.setGroupFlag(groupFlag);
						}
						////////////////////////////////////////////////////////////////////////////////////////
						String key = (String) baseParam.get("keyName");
						// 取得当前活动信息
						camTempsInfo = JSONUtil.serialize(campParamMap.get(key));
						stepMap.put("camTemps", camTempsInfo);
	//							saveStep = campParamMap.size() - 1;
						// 向session中放当前规则的已保存信息
						for(Map.Entry<String, Object> en : campParamMap.entrySet()){
							if("baseParams".equals(en.getKey())){
								Map<String, Object> baseParams = (Map<String, Object>) en.getValue();
								if (null != baseParams) {
									stepMap.putAll(baseParams);
								}
								continue;
							}
							String value = JSONUtil.serialize(en.getValue());
							if (form.getCopyFlag() == 1) {
								List<Map<String,Object>> ruleList = (List<Map<String,Object>>) JSONUtil.deserialize(value);
								// 清除子活动ID、规则ID
								removeRuleId(ruleList);
								value = JSONUtil.serialize(ruleList);
							}
							
							// 存放活动信息关键字，便于清空session中的活动信息
							String keyName = en.getKey();
							session.put(en.getKey(), value);
							if(null == session.get("keyName")){
								session.put("keyName", keyName);
							}else{
								session.put("keyName", session.get("keyName") + "_" + keyName);
							}
							i++;
						}
						
						if (i > -1) {
							saveStep = i;
							// 取得所有子活动的内容
			    			Map<String, Object> allSubCamp = null;
							for (int j = 0; j <= i; j++) {
								String campKey = "camTemps" + j;
								allSubCamp = binolcpcom02IF.getAllsubCamp(allSubCamp, (String) session.get(campKey));
							}
							if (null != allSubCamp && !allSubCamp.isEmpty()) {
								if (form.getCopyFlag() != 1) {
									// 会员活动状态（未开始）
									String mainState = CampConstants.STATE_0;
									// 取得子活动列表 
									List<Map<String, Object>> subCampList = binolcpcom02IF.getSubCampList(stepMap);
									if (null != subCampList && !subCampList.isEmpty()) {
										for(Map.Entry<String, Object> en : allSubCamp.entrySet()){
											Map<String, Object> allSubCampInfo = (Map<String, Object>) en.getValue();
											// 子活动ID
											Object subCampIdObj = allSubCampInfo.get(CampConstants.SUB_CAMP_ID);
											if (null != subCampIdObj && !"".equals(subCampIdObj)) {
												int subCampId = Integer.parseInt(subCampIdObj.toString());
												for (Map<String, Object> subCampMap : subCampList) {
													// DB中获取的子活动ID
													int subId = Integer.parseInt(subCampMap.get("campRuleId").toString());
													// 同一子活动
													if (subCampId == subId) {
														Object stateObj = subCampMap.get("state");
														if (null != stateObj) {
															String subState = String.valueOf(subCampMap.get("state"));
															if (CampConstants.STATE_1.equals(subState) || CampConstants.STATE_2.equals(subState)) {
																// 会员活动状态 （进行中）
																mainState = CampConstants.STATE_1;
															}
															// 子活动状态
															allSubCampInfo.put("state", subState);
														}
														subCampList.remove(subCampMap);
														break;
													}
												}
											}
										}
									}
									// 活动状态
									campaignInfo.setState(mainState);
								}
								if (form.getCopyFlag() == 1) {
									for(Map.Entry<String, Object> en : allSubCamp.entrySet()){
										Map<String, Object> subCamp = (Map<String, Object>)en.getValue();
										subCamp.remove(CherryConstants.BARCODE);
										//subCamp.remove(CampConstants.SEARCH_CODE);
									}
								}
			    				session.put(CampConstants.ALLSUBCAMP, allSubCamp);
			    				stepMap.put(CampConstants.ALLSUBCAMP, allSubCamp);
			    			}
						}
					}
				}catch(Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
			session.put("campInfo", campaignInfo);
			// 会员活动类型
			session.put("campType", campaignInfo.getCampaignType());
			stepMap.put("campaignType", campaignInfo.getCampaignType());
			stepMap.put("campInfo", campaignInfo);
			stepMap.put("brandInfoId", campaignInfo.getBrandInfoId());
			stepMap.put("memberClubId", campaignInfo.getMemberClubId());
			stepMap.put("organizationInfoId", campaignInfo.getOrganizationInfoId());
			// 工作流实例ID
			long wfId = -1;
			int actionId = -1;
			// 执行工作流
			if (!CherryChecker.isNullOrEmpty(campaignInfo.getWorkFlowId())) {
				wfId = Long.parseLong(String.valueOf(campaignInfo.getWorkFlowId()));
				if (-1 != wfId && null != campaignInfo.getActionId()) {
					actionId = campaignInfo.getActionId();
					if (-1 != actionId) {
						stepMap.put(CampConstants.APPLI_KEY, applicationContext);
						workflow.doAction_single(wfId, actionId, input);
					}
				}
			} 
			// 所属品牌
			form.setBrandInfoId(campaignInfo.getBrandInfoId());
			// 保存后编辑页面时，显示首页
			if (-1 == wfId || -1 == actionId) {
				// 显示首页
				topPage(1);
				// 会员活动
            	if (CampConstants.TYPE_FLAG_1.equals(campaignInfo.getCampaignTypeFlag())) {
            		// 俱乐部模式
        			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(campaignInfo.getOrganizationInfoId()), form.getBrandInfoId());
        			if (!"3".equals(clubMod)) {
        				// 取得会员俱乐部List
        				clubList = queryClubList(form.getBrandInfoId());
        			}
        			form.setMemberClubId(campaignInfo.getMemberClubId());
            		return "BINOLCPCOM05";
            	}
				return "BINOLCPCOM01";
			}
			session.put("wfId", wfId);
			// 取得页面信息list
			if (stepMap.containsKey("newCamTempList")) {
	    		camTempList = (List<Map<String, Object>>) stepMap.get("newCamTempList");
	    	}
			// 导航条
	    	String navigation = (String) stepMap.get("navigation");
	    	if (!CherryChecker.isNullOrEmpty(navigation)) {
	    		stepNames = navigation.split(",");
	    	}
	    	templateName = (String) stepMap.get("templateName");
	    	// 页面按钮
	    	String pageButtons = (String) stepMap.get("pageButtons");
	    	if (!CherryChecker.isNullOrEmpty(pageButtons)){
	    		campBtns = pageButtons.split(",");
	    	}
	    	//当前步骤
	    	onStep = (String) stepMap.get("onStep");
	    	// 下一步动作
	    	String nextAction = (String) stepMap.get("nextAction");
	    	// 上一步动作
	    	String backAction = (String) stepMap.get("backAction");
	    	
	    	// 编辑时执行的动作
	    	String editInitAction = (String) stepMap.get("editInitAction");
	    	if (!CherryChecker.isNullOrEmpty(nextAction)) {
	    		form.setNextAction(Integer.parseInt(nextAction));
	    	}
	    	if (!CherryChecker.isNullOrEmpty(backAction)) {
	    		form.setBackAction(Integer.parseInt(backAction));
	    	}
	    	// 存草稿
	    	String saveAction = (String) stepMap.get("saveAction");
	    	if (!CherryChecker.isNullOrEmpty(saveAction)) {
	    		form.setSaveActionId(Integer.parseInt(saveAction));
	    	}
	    	if (!CherryChecker.isNullOrEmpty(editInitAction)) {
	    		int editInitActionId = Integer.parseInt(editInitAction);
	    		form.setActionId(editInitActionId);
	    		form.setInitActionId(editInitActionId);
	    	}
		}
		return SUCCESS;
	}
	/**
	 * <p>
	 * 清除子活动ID、规则ID
	 * </p>
	 * 
	 * 
	 * @param ruleList 页面参数
	 * @return 
	 * 
	 */
	private void removeRuleId(List<Map<String, Object>> ruleList) throws Exception{
		if (null != ruleList) {
			for (Map<String, Object> map : ruleList) {
				// 规则ID
				if (map.containsKey("ruleId")) {
					map.remove("ruleId");
				}
				// 子活动ID
				if (map.containsKey(CampConstants.SUB_CAMP_ID)) {
					map.remove(CampConstants.SUB_CAMP_ID);
				}
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map.get("combTemps");
				if (null != combTempList && !combTempList.isEmpty()) {
					// 递归调用
					removeRuleId(combTempList);
				}
				 List<Map<String, Object>> campList = (List<Map<String, Object>>) map.get(CampConstants.CAMP_LIST);
			    if (null != campList && !campList.isEmpty()) {
			    	// 递归调用
			    	removeRuleId(campList);
			    }
			}
		}
	}
	
	/**
	 * <p>
	 * 创建会员活动首页初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String topInit() throws Exception {
		// 会员活动首页显示设置
		topPage(0);
		if (CampConstants.TYPE_FLAG_1.equals(form.getTypeFlag())) {
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 读取系统配置项
			String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
			String groupFlag = binOLCM14_BL.getConfigValue("1108",
					userInfo.getBIN_OrganizationInfoID()+"",
					brandId);
			form.getCampInfo().setGroupFlag(groupFlag);
			// 俱乐部模式
			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
					.getBIN_OrganizationInfoID()), brandId);
			if (!"3".equals(clubMod)) {
				// 取得会员俱乐部List
				clubList = queryClubList(brandId);
			}
			return "BINOLCPCOM05";
		}
		return SUCCESS;
	}
	
	/**
	 * 取得会员俱乐部List
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> queryClubList(String brandInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put("brandInfoId", brandInfoId);
		return binOLCM05_BL.getClubList(map);
	}
	
	/**
	 * <p>
	 * 会员活动首页显示设置
	 * </p>
	 * 
	 * 
	 * @param int 区分 0: 初期显示   1: 返回首页
	 * @return 
	 * 
	 */
	private void topPage(int kbn) throws Exception{
		//session.remove("wfId");
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		form.setBrandCode(userInfo.getBrandCode().toUpperCase());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
		// 页面参数
		if (null == campParams && !CherryChecker.isNullOrEmpty(form.getCampParamInfo())) {
			campParams = (Map) JSONUtil.deserialize(form.getCampParamInfo());
			// 活动类型
			form.setCampaignType((String) campParams.get("campaignType"));
			
		}
		if (null != campParams) {
			// 会员俱乐部Id
			String memberClubId = (String) campParams.get("memberClubId");
			if (!CherryChecker.isNullOrEmpty(memberClubId)) {
				form.setMemberClubId(memberClubId);
				map.put("memberClubId", memberClubId);
				Map<String, Object> clubMap = new HashMap<String, Object>();
				clubMap.put("memberClubId", memberClubId);
				// 取得品牌名
				clubMap.put("clubName", binOLCM05_BL.getClubName(map));
				clubList = new ArrayList<Map<String, Object>>();
				clubList.add(clubMap);
			}
		}
		if (0 == kbn) {
			CampaignDTO campInfo = new CampaignDTO();
			// 创建者
			campInfo.setCampaignSetBy(userInfo.getBIN_UserID());
			// 创建者名称
			campInfo.setCampaignSetByName(userInfo.getLoginName());
			// 类型区分
			campInfo.setCampaignTypeFlag(form.getTypeFlag());
			form.setCampInfo(campInfo);
			// 清空session中的规则信息
			if(null != session.get("keyName")){
	    		String camTemps = (String) session.get("keyName");
	        	if(!"".equals(camTemps)){
	        		String[] camTempsArr = camTemps.split("_");
	            	for(String camtemp : camTempsArr){
	            		if(null != session.get("keyName")){
	            			if(null != session.get(camtemp)){
	                			session.remove(camtemp);
	            			}
	            		}
	            	}
	        	}
	        	session.remove("keyName");
	        	session.remove(CampConstants.ALLSUBCAMP);
	    	}
		} else {
			CampaignDTO campInfo = form.getCampInfo();
			if (null != campInfo) {
				// 活动类型
				form.setCampaignType(campInfo.getCampaignType());
				// 预约时间不为空
				if (CherryChecker.isNullOrEmpty(campInfo.getReseFlag(), true)) {
					if (!CherryChecker.isNullOrEmpty(campInfo.getCampaignOrderFromDate(), true)) {
						// 需要预约
						campInfo.setReseFlag(CampConstants.RESE_FLAG_1);
					} else {
						// 不需要预约
						campInfo.setReseFlag(CampConstants.RESE_FLAG_0);
					}
				}
			}
		}
	}
	
	/**
	 * <p>
	 * 保存处理时所需要的参数
	 * </p>
	 * 
	 * 
	 * @param map 参数集合
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public void saveParams(Map<String, Object> map) throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 品牌ID
		map.put("brandInfoId", form.getBrandInfoId());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 保存信息
		map.put("campSaveInfo", form.getCampSaveInfo());
		// 首页信息
		CampaignDTO campInfo = form.getCampInfo();
		CampaignDTO campaignDTO = (CampaignDTO) session.get("campInfo");
		ConvertUtil.convertDTO(campaignDTO, campInfo, false);
		map.put("campInfo", campaignDTO);
		if (null != campaignDTO) {
			if (CampConstants.SAVE_STATUS_1.equals(map.get(CampConstants.SAVE_STATUS))){
				// 未开始
				campaignDTO.setState(CampConstants.STATE_0);
				// 草稿
			} else {
				// 草稿中
				campaignDTO.setState(CampConstants.STATE_3);
			}
		}
		map.put("campaignRule", form.getCampaignRule());
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		int onStepInt = 0;
		if (!CherryChecker.isNullOrEmpty(onStep) && CherryChecker.isNumeric(onStep)) {
			onStepInt = Integer.parseInt(onStep);
		}
		// 当前提交的参数
		String camTempKey = "camTemps" + onStepInt;
		buffer.append("\"").append(camTempKey).append("\"").append(":")
		.append(form.getCamTemps());
		int size = onStepInt;
//		if (saveStep > onStepInt) {
//			size = saveStep;
//		}
		for (int i = 0; i <= size; i++) {
			if (onStepInt == i) {
				continue;
			}
			// 模板Key
    		String key = "camTemps" + i;
    		String camTempsInfo = (String) session.get(key);
			if (null != camTempsInfo && !"".equals(camTempsInfo)) {
				buffer.append(",").append("\"").append(key).append("\"").append(":")
				.append(camTempsInfo);
    		}
		}
		buffer.append(",").append("\"keyName\"").append(":\"").append(camTempKey).append("\"");
		buffer.append("}");
		String ruleDetail = buffer.toString();
		// 规则体详细
		map.put("ruleDetail", ruleDetail);
		Map<String, Object> allSubCampMap = (Map<String, Object>) session.get(CampConstants.ALLSUBCAMP);
		if (session.containsKey(CampConstants.ALLSUBCAMP)) {
			// 所有子活动信息
			map.put(CampConstants.ALLSUBCAMP, allSubCampMap);
		}
		while (true) {
			onStepInt++;
			// 模板Key
    		String key = "camTemps" + onStepInt;
    		String camTempsInfo = (String) session.get(key);
			if (null != camTempsInfo && !"".equals(camTempsInfo)) {
				session.remove(key);
				if (null != allSubCampMap && !allSubCampMap.isEmpty()) {
					// 删除当前页内容
					binolcpcom02IF.delCurContent(allSubCampMap, camTempsInfo);
				}
    		} else {
    			break;
    		}
		}
	}
	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public String draftSave() throws Exception{
    	if (null == campParams && !CherryChecker.isNullOrEmpty(form.getCampParamInfo())) {
			campParams = (Map) JSONUtil.deserialize(form.getCampParamInfo());
		}
    	Map<String, Object> map = new HashMap<String, Object>();
    	Map<String, Object> saveMap = new HashMap<String, Object>();
    	//Map<String, Object> paramMap = new HashMap<String, Object>();
    	Map input = new HashMap();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo
						.getBIN_UserID());
		if (null != campParams) {
			map.putAll(campParams);
		}
		map.put("camTempsValidate", form.getCamTemps());
		// 新建 
		String optKbnVal = CampConstants.OPT_KBN1;
		if (2 == form.getOptKbn()) {
			// 复制
			if (1 == form.getCopyFlag()) {
				optKbnVal = CampConstants.OPT_KBN3;
				// 编辑
			} else {
				optKbnVal = CampConstants.OPT_KBN2;
			}
		}
		// 操作区分
		map.put(CampConstants.OPT_KBN, optKbnVal);
		input.put("stepMap", map);
		long wfId = Long.parseLong(session.get("wfId").toString());
		try {
			map.put("workFlowId", session.get("wfId"));
			map.put("actionId", form.getActionId());
			map.put("saveStatus", "0");
			// 保存处理时所需要的参数
			saveParams(map);
			map.put(CampConstants.APPLI_KEY, applicationContext);
			// ======================================//
			Map<String, Object> preSubCamp = (Map<String, Object>) session.get(CampConstants.ALLSUBCAMP);
			// 取得所有子活动的内容
			Map<String, Object> allSubCamp = binolcpcom02IF.getAllsubCamp(preSubCamp, form.getCamTemps());
			if (null != allSubCamp && !allSubCamp.isEmpty()) {
				session.put(CampConstants.ALLSUBCAMP, allSubCamp);
				map.put(CampConstants.ALLSUBCAMP, allSubCamp);
			}
			// ======================================//
			workflow.doAction_single(wfId, form.getSaveActionId(), input);
			Exception exc = (Exception) map.get("exception");
        	if (null != exc) {
        		logger.error(exc.getMessage(),exc);
        		throw exc;
        	}

        	// 异常信息处理
        	CherryException cherryExc = (CherryException) map.get("cherryException");
        	if (null != cherryExc) {
        		throw cherryExc;
        	}
			// 打印错误信息
	    	printErrMsg(map);
	    	// 错误信息集合
			List<ActionErrorDTO> actionErrorList = (List<ActionErrorDTO>) map.get("actionErrorList");
			if (null != actionErrorList && !actionErrorList.isEmpty()) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			Map<String, Object> campSaveMap = (Map<String, Object>) map.get("campSaveMap");
			if (null != campSaveMap && !campSaveMap.isEmpty()) {
				String campSave = JSONUtil.serialize(campSaveMap);
				saveMap.put("campSaveList", campSave);
			}
		} catch (CherryException e) {
			saveMap.put("message", e.getErrMessage());
			saveMap.put("result", "NG");
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, saveMap);
			return null;
		} catch (Exception e) {
			saveMap.put("message", getText("ECM00005"));
			saveMap.put("result", "NG");
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, saveMap);
			return null;
		}
		int onStepSave = Integer.parseInt(onStep);
		saveStep = onStepSave;
		// 处理成功
		saveMap.put("message", getText("ICM00002"));
		saveMap.put("result", "OK");
		saveMap.put("saveStep", saveStep);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, saveMap);
		return null;
	}
    
    /**
	 * <p>
	 * 画面初期显示(详细画面)
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	
	public String detailInit() throws Exception {
		// 页面基本信息
		if (null == campParams) {
			if (!CherryChecker.isNullOrEmpty(form.getCampParamInfo())) {
				campParams = (Map) JSONUtil.deserialize(form.getCampParamInfo());
			} else {
				campParams = new HashMap();
				// 品牌信息ID
				campParams.put("brandInfoId", form.getBrandInfoId());
				campParams.put("memberClubId", form.getMemberClubId());
				// 活动ID
				campParams.put("campaignId", form.getCampaignId());
			}
		}
    	Map<String, Object> stepMap = new HashMap<String, Object>();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		stepMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
//		if(null != campParams){
//			stepMap.put("campaignId", (String) campParams.get("campaignId"));
//			stepMap.put("brandInfoId", (String) campParams.get("brandInfoId"));
//		}
		stepMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
    	CampaignRuleDTO campaignRuleDTO = new CampaignRuleDTO();
    	form.setCampaignRule(campaignRuleDTO);
		if (null != campParams) {  
			stepMap.putAll(campParams);
			// 取得规则体详细信息
			Map<String, Object> ruleDetailMap = binolcpcom02IF.getRuleDetail(stepMap);
			// 所有步骤模板信息List
			List<Map<String, Object>> allCamTempList = null;
			Map<String, Object> camTemps = (Map<String, Object>) JSONUtil.deserialize((String) ruleDetailMap.get("ruleDetail"));
			if (null != camTemps && !camTemps.isEmpty()) {
				allCamTempList = new ArrayList<Map<String, Object>>();
				Map<String, Object> baseParams = (Map<String, Object>) camTemps.get("baseParams");
				if (null != baseParams) {
					stepMap.putAll(baseParams);
				}
				int i = 0;
				while(camTemps.containsKey("camTemps" + i)) {
					List<Map<String, Object>> camTempsList = (List<Map<String, Object>>) camTemps.get("camTemps" + i);
					allCamTempList.addAll(camTempsList);
					i++;
				}
			}
			// 插入所有步骤信息
			stepMap.put("allCamTempList", allCamTempList);
			// 取得会员活动信息
			CampaignDTO campaignInfo = binolcpcom02IF.getCampaignInfo(stepMap);
			// 品牌名称
			String brandName = binolcpcom02IF.getBrandName(stepMap);
			campaignInfo.setBrandName(brandName);
			if (!CherryChecker.isNullOrEmpty(stepMap.get("memberClubId"))) {
				campaignInfo.setClubName(binOLCM05_BL.getClubName(stepMap));
			}
			form.setCampInfo(campaignInfo);
			stepMap.put("campInfo", campaignInfo);
			// 会员活动类型
			stepMap.put("campType", campaignInfo.getCampaignType());
			// 会员积分类型
			stepMap.put("templateType", stepMap.get("templateType"));
			}
			if(null != campParams.get("brandInfoId")){
				// 所属品牌
				form.setBrandInfoId((String) campParams.get("brandInfoId"));
			}
			if (null != campParams.get("memberClubId")) {
				// 会员俱乐部ID
				form.setMemberClubId((String) campParams.get("memberClubId"));
			}
			// 取得模板名称
            templateName = (String) stepMap.get("templateName");
            // 取得初始化bean
			String templateInitBeanName = (String) stepMap.get("templateInitBean");
			TemplateInit_IF templateInitIF = getBeanByName(templateInitBeanName);
			stepMap.put("templateInitIF", templateInitIF);
			// 模板初期显示区分
			stepMap.put("tempInitKbn", "1");
			// 详细页面显示区分(用于判断是否显示规则描述模块)
			stepMap.put("detailFlag", "1");
			// 返回页面list
        	camTempList = (List<Map<String, Object>>) binolcpcom02IF.convertCamTempList(stepMap);
		return SUCCESS;
	}
	
	/**
	 * 根据优先级配置以json格式保存规则到活动表
	 * 
	 * @param 无
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String savePriorityRule() throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		paramMap.put("brandInfoId", form.getBrandInfoId());
		// 会员俱乐部ID
		paramMap.put("memberClubId", form.getMemberClubId());
		// 会员活动类型
		paramMap.put("campaignType", form.getCampaignType());
		// 作成者
		paramMap.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		Map<String, Object> priorityMap = new HashMap<String, Object>();
		// 活动ID
		priorityMap.put("campaignId", form.getCampaignId());
		String[] keyStr = {form.getCampaignId()};
		priorityMap.put("keys", keyStr);
		// 取得活动组ID
		String grpStr = binolcpcom02IF.getRuleConCount(paramMap);
		String message = null;
		// 取得当前会员类型的规则配置优先级信息
		List<Map<String,Object>> priorityList = binolcpcom02IF.getPriorityByIdMap(form.getCampaignId());
		if(null == priorityList){
			priorityList = new ArrayList<Map<String, Object>>();
		}
		// 优先级最高
		if("0".equals(form.getPrioritySet())){
			// 插入list的第一个
			priorityList.add(0, priorityMap);
		}else if("1".equals(form.getPrioritySet())){
			// 优先级最低，放在最后
			priorityList.add(priorityMap);
			Map<String,Object> defaultMap = null;
			if (priorityList.size() > 1) {
				for (int i = priorityList.size() - 1; i >=0; i--) {
					Map<String, Object> priorityInfo = priorityList.get(i);
					if ("1".equals(priorityInfo.get("defaultFlag"))) {
						defaultMap = priorityInfo;
						priorityList.remove(i);
						break;
					}
				}
			}
			// 将默认规则放置最后
			if (null != defaultMap) {
				priorityList.add(defaultMap);
			}
		}
		// 优先级信息
		String priorityMes = JSONUtil.serialize(priorityList);
		// 规则配置优先级信息
		paramMap.put("priorityMes", priorityMes);
		// 活动组ID
		paramMap.put("grpId", grpStr);
		// 更新会员活动组表
		int result = binolcpcom02IF.updateCampaignConfig(paramMap);
		// 更新失败
		if (0 == result) {
			message = getText("ECM00005");
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, message);
			return null;
		}
		// 刷新单个规则文件
		knowledgeEngine.refreshRule(Integer.parseInt(form.getCampaignId()));
		// 处理成功
		message = getText("ICM00002");
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, message);
		return null;
	}
	
	/**
	 * <p>
	 * 停用规则
	 * </p>
	 * 
	 * @param 五
	 * @return string 页面跳转
	 * 
	 */
	public String editValid() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", form.getBrandInfoId());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		// 活动ID
		map.put("campaignId", form.getCampaignId());
		// 活动类型
		map.put("campaignType", form.getCampaignType());
		// 更新程序
		map.put(CherryConstants.UPDATEPGM, "BINOLCPCOM02");
		// 更新人
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		try{
			// 停用规则并更新优先级信息
			binolcpcom02IF.tran_editValid(map);
		}catch (Exception e){
			// 操作失败
			this.addActionError(getText("ECM00005"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 操作成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	private <T> T getBeanByName(String name) {
		if (!CherryChecker.isNullOrEmpty(name)) {
			T t = (T) this.applicationContext.getBean(name);
			return t;
		}
		return null;
	}
	
	public void getMemberCount(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
		map.put("resultMode", "0");
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		int count = ConvertUtil.getInt(resultMap.get("total"));
		try {
			ConvertUtil.setResponseByAjax(response, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
