/*	
 * @(#)BINOLCPCOM02_BL.java     1.0 2011/7/18		
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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampBasePropDTO;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.interfaces.CreateRule_IF;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.RuleToolUtil;
import com.cherry.customize.campaign.CampaignCode;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.man.service.BINOLJNMAN06_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 会员活动共通 BL
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM02_BL implements BINOLCPCOM02_IF{
	
	protected static final Logger logger = LoggerFactory.getLogger(BINOLCPCOM02_BL.class);
	
	
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLJNMAN06_Service binoljnman06_Service;
	
	@Resource
	private CodeTable CodeTable;

	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/**
	 * 取得页面对应的活动模板List
	 * 
	 * @param map
	 * @return 活动模板List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> searchCamTempList(Map<String, Object> map) throws Exception {
		// 取得页面对应的模板List
		List<Map<String, Object>> camTempList = binolcpcom02_Service.getCampTempList(map);
		if (null != camTempList && !camTempList.isEmpty()) {
			for (int i = 0; i < camTempList.size(); i++) {
				Map<String, Object> camTemp = (Map<String, Object>) camTempList.get(i);
				// 取得组合模板
				createCombTemps(camTemp, map);
			}
		}
		return camTempList;
	}
	
	/**
	 * 取得组合模板
	 * 
	 * @param map
	 * @return 活动模板List
	 * @throws Exception 
	 */
	private void createCombTemps(Map<String, Object> map, Map<String, Object> baseMap) throws Exception {
		if (null == map || map.isEmpty()) {
			return;
		}
		// 模板编号
		String tempCode = (String) map.get("tempCode");
		// 新增规则时的初期化处理
		if (!"1".equals(baseMap.get("tempInitKbn"))) {
			// 基础模板初期处理
			templateInit(tempCode + "_init", baseMap, map);
		}
		// 模板区分
		String tempflag = (String) map.get("tempflag");
		// 业务模板
		if ("1".equals(tempflag)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageId", baseMap.get("pageId"));
			// 会员活动模板ID
			paramMap.put("campTempLinkId", map.get("campTempLinkId"));
			// 会员活动模板ID
			paramMap.put("templateType", baseMap.get("templateType"));
			// 取得页面对应的模板List
			List<Map<String, Object>> linkCampTempList = binolcpcom02_Service.getLinkCampTempList(paramMap);
			if (null != linkCampTempList) {
				for (int i = 0; i < linkCampTempList.size(); i++) {
					Map<String, Object> linkCampTemp = (Map<String, Object>) linkCampTempList.get(i);
					if(null != map.get("addCopyFlag") && "1".equals(map.get("addCopyFlag"))){
						// 解决编辑时点击添加按钮，添加的模块保留已存在模块的值
						getAddCampList(map, baseMap);
					}
					createCombTemps(linkCampTemp, baseMap);
				}
				map.put("combTemps", linkCampTempList);
			}
		}
		return;
	}
	
	private void getAddCampList(Map<String, Object> map, Map<String, Object> baseMap) throws Exception {
		if (null == map || map.isEmpty()) {
			return;
		}
		// 模板编号
		String tempCode = (String) map.get("tempCode");
		// 基础模板初期处理
		templateInit(tempCode + "_init", baseMap, map);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tempCode", baseMap.get("tempCode"));
		// 会员活动模板ID
		paramMap.put("campTempLinkId", map.get("campTempLinkId"));
		// 会员活动模板ID
		paramMap.put("templateType", baseMap.get("templateType"));
		// 取得页面对应的模板List
		List<Map<String, Object>> linkCampTempList = binolcpcom02_Service.getLinkCampTempList(paramMap);
		if (null != linkCampTempList && !linkCampTempList.isEmpty()) {
			for (int i = 0; i < linkCampTempList.size(); i++) {
				Map<String, Object> linkCampTemp = (Map<String, Object>) linkCampTempList.get(i);
				getAddCampList(linkCampTemp, baseMap);
			}
			map.put("addCombTemps", linkCampTempList);
		}
		return;
	}
	
	/**
	 * 取得页面显示的活动模板List
	 * 
	 * @param List
	 *            数据库取得的活动模板List
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @return 页面显示的活动模板List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> convertCamTempList(Map<String, Object> map) throws Exception {
		// 取得页面对应的活动模板List
		List<Map<String, Object>> camTempList = searchCamTempList(map);
		// 页面提交的模板信息
		String camTempsPage = (String) map.get("camTemps");
		List<Map<String, Object>> camTemps = null;
		if (map.containsKey("allCamTempList")) {
			camTemps = (List<Map<String, Object>>) map.get("allCamTempList");
		} else if (null != camTempsPage && !"".equals(camTempsPage)) {
			try {
				camTemps = (List<Map<String, Object>>) JSONUtil.deserialize(camTempsPage);
			} catch (JSONException e) {
				return camTempList;
			}
		}
		Map<String, Object> camTempMap = new HashMap<String, Object>();
		// 转换页面提交的活动模板List
		convertCamTempListToMap(camTemps, camTempMap);
		// 取得页面显示用的活动模板List
		getCamTempList(camTempList, camTempMap, map);
		return camTempList;
	}
	
	/**
	 * 取得页面显示用的活动模板List
	 * 
	 * @param List
	 *            数据库取得的活动模板List
	 * @param Map
	 *            转换后的模板Map
	 *            
	 * @return 页面显示用的活动模板List
	 * @throws Exception 
	 */
	@Override
	public void getCamTempList(List<Map<String, Object>> camTempDBList, Map<String, Object> camTempMap, Map<String, Object> baseMap) throws Exception {
		if (null != camTempDBList && !camTempMap.isEmpty()) {
			int j = 0;
			for (Map<String, Object> camTempDB : camTempDBList) {
				// 模板集合标识符
				String groupCode = (String) camTempDB.get("groupCode");
				// 页面提交的数据
				Object camTemp = camTempMap.get(groupCode);
				boolean findFlg = false;
				if (null != camTemp) {
					if (camTemp instanceof Map) {
						String parentGroupCodeDB = (String) camTempMap.get("parentGroupCodeDB");
						String parentGroupCode = (String) ((Map) camTemp).get("parentGroupCode");
						boolean putFlg = true;
						String index = (String) ((Map) camTemp).get("index");
						String indexDB = (String) camTempMap.get("indexDB");
						// 有相同的父模板
						if (null != parentGroupCodeDB && null != parentGroupCode) {
							// 数据库取得的模板层级大于等于页面提交的层级时
							if ((parentGroupCodeDB.length() >= parentGroupCode.length()) && (indexDB.length() >= index.length())) {
								if ((parentGroupCodeDB.lastIndexOf(parentGroupCode) !=  
									parentGroupCodeDB.length() - parentGroupCode.length()) || 
									(indexDB.lastIndexOf(index) != indexDB.length() - index.length())) {
									putFlg = false;
								}
							} else {
								// 数据库取得的模板层级小于页面提交的层级时
								if ((parentGroupCode.lastIndexOf(parentGroupCodeDB) !=  
									parentGroupCode.length() - parentGroupCodeDB.length()) ||
									(index.lastIndexOf(indexDB) != index.length() - indexDB.length())) {
									putFlg = false;
								}
							}
						}
						if (putFlg) {
							((Map) camTemp).remove("tempCode");
							camTempDB.putAll((Map<String, Object>) camTemp);
							camTempMap.remove(groupCode);
							findFlg = true;
						}
					} else if (camTemp instanceof List) {
						List<Map<String, Object>> camTempPageList = (List<Map<String, Object>>) camTemp;
						String parentGroupCodeDB = (String) camTempMap.get("parentGroupCodeDB");
						String indexDB = (String) camTempMap.get("indexDB");
						if (null != camTempPageList) {
							boolean flg = false;
							for (Map<String, Object> camTempPage : camTempPageList) {
								String parentGroupCode = (String) camTempPage.get("parentGroupCode");
								String index = (String) camTempPage.get("index");
								// 有相同的父模板
								if (null == parentGroupCodeDB && null == parentGroupCode || 
										null != parentGroupCodeDB && parentGroupCodeDB.equals(parentGroupCode) && 
										(null == index && null == indexDB || null != indexDB && indexDB.equals(index))) {
									camTempPage.remove("tempCode");
									camTempDB.putAll(camTempPage);
									camTempPageList.remove(camTempPage);
									flg = true;
									findFlg = true;
									break;
								}
							}
							if (!flg) {
								for (Map<String, Object> camTempPage : camTempPageList) {
									String parentGroupCode = (String) camTempPage.get("parentGroupCode");
									String index = (String) camTempPage.get("index");
									boolean putFlg = false;
									// 有相同的父模板
									if ((null == parentGroupCodeDB && null != parentGroupCode || 
											null != parentGroupCodeDB && null == parentGroupCode) && 
											(null == indexDB && null != index || null != indexDB && null == index)) {
										putFlg = true;
									} 
									if (!putFlg && null != parentGroupCodeDB && null != parentGroupCode && null != indexDB && null != index) {
										// 数据库取得的模板层级大于等于页面提交的层级时
										if (parentGroupCodeDB.length() >= parentGroupCode.length() && indexDB.length() >= index.length()) {
											if ((parentGroupCodeDB.lastIndexOf(parentGroupCode) ==  
												parentGroupCodeDB.length() - parentGroupCode.length()) && 
												(indexDB.lastIndexOf(index)) == indexDB.length() - index.length()) {
												putFlg = true;
							
											}
										} else {
											// 数据库取得的模板层级小于页面提交的层级时
											if ((parentGroupCode.lastIndexOf(parentGroupCodeDB) ==  
												parentGroupCode.length() - parentGroupCodeDB.length()) && 
												(index.lastIndexOf(indexDB) == index.length() - indexDB.length())) {
												putFlg = true;
											}
										}
									}
									if (putFlg) {
										camTempPage.remove("tempCode");
										camTempDB.putAll(camTempPage);
										camTempPageList.remove(camTempPage);
										findFlg = true;
										break;
									}
								}
							}
						}
					}
				}
				if (!findFlg) {
					// 页面上显示区分   0: 不显示
					camTempDB.put("showFlg", "0");
				}
				if ("1".equals(baseMap.get("tempInitKbn"))) {
					// 模板编号
					String tempCode = (String) camTempDB.get("tempCode");
					// 基础模板初期处理
					templateInit(tempCode + "_init", baseMap, camTempDB);
				}
				if (camTempDB.containsKey("combTemps")) {
					String parentGroupCodeDB = null;
					String indexDB = null;
					// 父模板的模板集合标识符
					if (camTempMap.containsKey("parentGroupCodeDB")) {
						String parentGroupCodeDBStr = (String) camTempMap.get("parentGroupCodeDB");
						parentGroupCodeDB = parentGroupCodeDBStr + "_" + groupCode;
					} else {
						parentGroupCodeDB = groupCode;
					}
					camTempMap.put("parentGroupCodeDB", parentGroupCodeDB);
					if (camTempMap.containsKey("indexDB")) {
						String indexDBStr = (String) camTempMap.get("indexDB");
						indexDB = indexDBStr + "_" + j;
					} else {
						indexDB = String.valueOf(j);
					}
					camTempMap.put("indexDB", indexDB);
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTempDB.get("combTemps");
					if (camTempDB.containsKey("TEMPCOPYSIZE")) {
						// 拷贝模板的数量
						Object tempCopySizeObj = camTempDB.get("TEMPCOPYSIZE");
						if (!CherryChecker.isNullOrEmpty(tempCopySizeObj)) {
							int tempCopySize = Integer.parseInt(tempCopySizeObj.toString());
							if (tempCopySize > 1) {
								List<Map<String, Object>> tempCopyList = new ArrayList<Map<String, Object>>();
								for (int i = 1; i < tempCopySize; i++) {
									List<Map<String, Object>> cloneList = null;
									try {
										cloneList = (List<Map<String, Object>>) ConvertUtil.byteClone(combTempList);
									} catch (Exception e) {
										continue;
									}
									if (null != cloneList) {
										tempCopyList.addAll(cloneList);
									}
								}
								combTempList.addAll(tempCopyList);
							}
						}
					}
					getCamTempList(combTempList, camTempMap, baseMap);
				}
				if(findFlg){
					j++;
				}
			}
			String parentGroupCodeDB = (String) camTempMap.get("parentGroupCodeDB");
			if (null != parentGroupCodeDB && !"".equals(parentGroupCodeDB)) {
				int _index = parentGroupCodeDB.lastIndexOf("_");
				if (_index > 0) {
					parentGroupCodeDB = parentGroupCodeDB.substring(0, _index);
					camTempMap.put("parentGroupCodeDB", parentGroupCodeDB);
				} else {
					camTempMap.remove("parentGroupCodeDB");
				}
			}
			String indexDB = (String) camTempMap.get("indexDB");
			if (null != indexDB && !"".equals(indexDB)) {
				int _index = indexDB.lastIndexOf("_");
				if (_index > 0) {
					indexDB = indexDB.substring(0, _index);
					camTempMap.put("indexDB", indexDB);
				} else {
					camTempMap.remove("indexDB");
				}
			}
		}
	}
	
	/**
	 * 转换页面提交的活动模板List
	 * 
	 * @param List
	 *            页面提交的活动模板List
	 *            
	 * @return 转换后的模板Map
	 */
	@Override
	public void convertCamTempListToMap(List<Map<String, Object>> camTempList, Map<String, Object> camTempMap) {
		// 页面提交的活动模板List
		if (null != camTempList && null != camTempMap) {
			for (int i = 0; i < camTempList.size(); i++) {
				Map<String, Object> camTemp = camTempList.get(i);
				// 模板集合标识符
				String groupCode = (String) camTemp.get("groupCode");
				if (camTemp.containsKey("combTemps")) {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
					String parentGroupCode = null;
					String index = null;
					// 父模板的模板集合标识符
					if (camTempMap.containsKey("parentGroupCode")) {
						String parentGroupCodeStr = (String) camTempMap.get("parentGroupCode");
						//camTempMap.put("parentGroupCode1", parentGroupCodeStr);
						parentGroupCode = parentGroupCodeStr + "_" + groupCode;
					} else {
						parentGroupCode = groupCode;
						//camTempMap.remove("parentGroupCode1");
					}
					if (camTempMap.containsKey("index")) {
						String indexStr = (String) camTempMap.get("index");
						index = indexStr + "_" + i;
					} else {
						index = "" + i;
						//camTempMap.remove("parentGroupCode1");
					}
					camTempMap.put("parentGroupCode", parentGroupCode);
					camTempMap.put("index", index);
					
					camTemp.remove("combTemps");
					convertCamTempListToMap(combTempList, camTempMap);
				}
				if (camTempMap.containsKey("parentGroupCode")) {
					Object parentGroupCode = camTempMap.get("parentGroupCode");
					if (!groupCode.equals(parentGroupCode)) {
						camTemp.put("parentGroupCode", parentGroupCode);
					}
				}
				if (camTempMap.containsKey("index")) {
					Object index = camTempMap.get("index");
					camTemp.put("index", index);
				}
				// 页面上有多个同样的模板
				if (camTempMap.containsKey(groupCode)) {
					Object camTempVal = camTempMap.get(groupCode);
					if (camTempVal instanceof List) {
						List<Map<String, Object>> camTempValList = (List<Map<String, Object>>) camTempVal;
						camTempValList.add(camTemp);
					} else if (camTempVal instanceof Map) {
						List<Map<String, Object>> camTempValList = new ArrayList<Map<String, Object>>();
						camTempValList.add((Map<String, Object>) camTempVal);
						camTempValList.add(camTemp);
						camTempMap.put(groupCode, camTempValList);
					}
				} else {
					camTempMap.put(groupCode, camTemp);
				}
			}
			//if (camTempMap.containsKey("parentGroupCode1")) {
			String parentGroupCode = (String) camTempMap.get("parentGroupCode");
			String index = (String) camTempMap.get("index");
			if (null != parentGroupCode && !"".equals(parentGroupCode)) {
				int _index = parentGroupCode.lastIndexOf("_");
				if (_index > 0) {
					parentGroupCode = parentGroupCode.substring(0, _index);
					camTempMap.put("parentGroupCode", parentGroupCode);
				} else {
					camTempMap.remove("parentGroupCode");
				}
			}
			if (null != index && !"".equals(index)) {
				int _index = index.lastIndexOf("_");
				if (_index > 0) {
					index = index.substring(0, _index);
					camTempMap.put("index", index);
				} else {
					camTempMap.remove("index");
				}
			}
//			} else {
//				camTempMap.remove("parentGroupCode");
//			}
		}
	}
	
	/**
	 * 会员活动保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public String tran_saveCampaign(Map<String, Object> map) throws Exception {
		Map<String, Object> campSaveMap = null;
		// 会员活动保存信息
		String campSaveInfo = (String) map.get("campSaveInfo");
		if (null != campSaveInfo && !campSaveInfo.isEmpty()) {
			campSaveMap = (Map<String, Object>) JSONUtil.deserialize(campSaveInfo);
		}
		BaseDTO baseDto = new BaseDTO();
		// 系统时间
		String sysDate = binolcpcom02_Service.getSYSDate();
		// 作成日时
		baseDto.setCreateTime(sysDate);
		// 更新日时
		baseDto.setUpdateTime(sysDate);
		// 作成程序名
		baseDto.setCreatePGM("BINOLCPCOM02");
		// 更新程序名
		baseDto.setUpdatePGM("BINOLCPCOM02");
		// 作成者
		baseDto.setCreatedBy((String) map.get(CherryConstants.CREATEDBY));
		// 更新者
		baseDto.setUpdatedBy((String) map.get(CherryConstants.UPDATEDBY));
		map.put("BaseDTO", baseDto);
		// 取得更新会员活动相关表时所需要的参数
		getCamTempParams(map);
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) map.get("campInfo");
		// 领用日期非指定日期
		if(!"".equals(campaignDTO.getReferType()) 
				&& !CampConstants.REFER_TYPE_0.equals(campaignDTO.getReferType())){
			Map<String,Object> obtainRule = new HashMap<String, Object>();
			obtainRule.put(CampConstants.REFER_TYPE, campaignDTO.getReferType());
			obtainRule.put(CampConstants.ATTR_A, campaignDTO.getAttrA());
			obtainRule.put(CampConstants.ATTR_B, campaignDTO.getAttrB());
			obtainRule.put(CampConstants.ATTR_C, campaignDTO.getAttrC());
			obtainRule.put(CampConstants.VAL_A, campaignDTO.getValA());
			obtainRule.put(CampConstants.VAL_B, campaignDTO.getValB());
			campaignDTO.setObtainRule(JSONUtil.serialize(obtainRule));
		}
		
		// 会员子活动信息
		List<CampaignRuleDTO> campaignRuleList = (List<CampaignRuleDTO>) map.get("campaignRuleList");
		if (null == campaignDTO || null == campaignRuleList || campaignRuleList.isEmpty()) {
			logger.error(PropertiesUtil.getText("ECP00011"));
			throw new CherryException("ECM00005");
		}
		ConvertUtil.convertDTO(campaignDTO, baseDto, true);
		for (CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
			ConvertUtil.convertDTO(campaignRuleDTO, baseDto, true);
		}
		boolean upFlag = false;
		if (null == campSaveMap || campSaveMap.isEmpty()) {
			// 新增会员活动
			campSaveMap = addCampaign(map);
			if (campaignRuleList.size() == 1) {
				CampaignRuleDTO campaignRule = campaignRuleList.get(0);
				if ("1".equals(campaignRule.getNeedHideId())) {
					upFlag = true;
				}
			} else {
				upFlag = true;
			}
		} else {
			if (null != campSaveMap && !campSaveMap.isEmpty()) {
				// 会员活动ID
				Object campaignIdObj = campSaveMap.get("campaignId");
				if (null != campaignIdObj) {
					int campaignId = Integer.parseInt(campaignIdObj.toString());
					// 会员活动更新次数
					int campModifyCountint = 0;
					if (!CherryChecker.isNullOrEmpty(campSaveMap.get("campModifyCount"))) {
						campModifyCountint = Integer.parseInt(campSaveMap.get("campModifyCount").toString());
					}
					campaignDTO.setCampaignId(campaignId);
					campaignDTO.setCampModifyCount(campModifyCountint);
					campaignDTO.setCampUpdateTime((String) campSaveMap.get("campUpdateTime"));
					if (CherryChecker.isNullOrEmpty(campaignDTO.getCampaignGrpId())) {
						// 取得活动组ID
						String grpStr = binolcpcom02_Service.getRuleConCount(map);
						campaignDTO.setCampaignGrpId(grpStr);
					}
					// 设置活动是否下发区分
//					setSendFlag(campaignDTO);
					// 更新会员活动表
					int result = binolcpcom02_Service.updateCampaign(campaignDTO);
					// 更新失败
					if (0 == result) {
						throw new CherryException("ECM00038");
					}
					// 会员活动更新日时
					campSaveMap.put("campUpdateTime", sysDate);
					// 会员活动更新次数
					campSaveMap.put("campModifyCount", ++campModifyCountint);
					List<Map<String, Object>> campRuleList = (List<Map<String, Object>>) campSaveMap.get("campRuleList");
					if (null == campRuleList || campRuleList.isEmpty()) {
						logger.error(PropertiesUtil.getText("ECP00012"));
						throw new CherryException("ECM00005");
					}
					// 取得更新数据库用的子活动信息
					upFlag = getCampaignRuleDBuse(campaignRuleList, campRuleList);
					List<Map<String, Object>> ruleSaveList = new ArrayList<Map<String, Object>>();
					for(CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
						campaignRuleDTO.setState(campaignDTO.getState());
						// 保存区分
						String upKbn = campaignRuleDTO.getUpKbn();
						// 更新
						if ("1".equals(upKbn)) {
							// 更新会员子活动表
							int ruleResult = binolcpcom02_Service.updateCampaignRule(campaignRuleDTO);
							// 更新失败
							if (0 == ruleResult) {
								logger.error(PropertiesUtil.getText("ECP00013"));
								throw new CherryException("ECM00038");
							}
							Map<String, Object> ruleSaveMap = new HashMap<String, Object>();
							// 会员子活动ID
							ruleSaveMap.put(CampConstants.SUB_CAMP_ID, campaignRuleDTO.getCampaignRuleId());
							// 会员子活动更新日时
							ruleSaveMap.put("ruleUpdateTime", sysDate);
							// 会员子活动更新次数
							int ruleModifyCount = campaignRuleDTO.getRuleModifyCount();
							// 会员子活动更新次数
							ruleSaveMap.put("ruleModifyCount", ++ruleModifyCount);
							if (!CherryChecker.isNullOrEmpty(campaignRuleDTO.getSubCampDetailNo(), true)) {
								// 会员子活动连番
								ruleSaveMap.put("subCampDetailNo", campaignRuleDTO.getSubCampDetailNo());
								// 会员子活动类型
								ruleSaveMap.put("subCampRuleType", campaignRuleDTO.getSubCampRuleType());
							}
							ruleSaveList.add(ruleSaveMap);
							// 新增
						} else if ("0".equals(upKbn)) {
							String brandCode = binOLCM05_BL.getBrandCode(Integer.parseInt(campaignDTO.getBrandInfoId()));
							// 子活动代号
							String subCampainCode = CampaignCode.getSubCampaignCode(brandCode
									, campaignDTO.getOrganizationInfoId()
									, campaignDTO.getBrandInfoId()
									, String.valueOf(campaignDTO.getCampaignSetBy()));
							if (CherryChecker.isNullOrEmpty(subCampainCode)) {
								throw new CherryException("ECP00021");
							}
							campaignRuleDTO.setSubCampaignCode(subCampainCode);
							campaignRuleDTO.setCampaignId(campaignDTO.getCampaignId());
							// 保存子活动内容
							saveSubCampaign(map,campaignRuleDTO, ruleSaveList);
							// 删除
						} else if ("-1".equals(upKbn)) {
							// 停用子活动
							int ruleResult = binolcpcom02_Service.invalidCampaignRule(campaignRuleDTO);
							// 更新失败
							if (0 == ruleResult) {
								logger.error(PropertiesUtil.getText("ECP00015"));
								throw new CherryException("ECM00038");
							}
						}
						// 非停用的情况
						if (!"-1".equals(upKbn)) {
							// 保存活动条件和结果
							saveConditonResult(campaignRuleDTO);
						}
					}
					campSaveMap.put("campRuleList", ruleSaveList);
				}
			}
		}
		if (upFlag) { 
			// 会员子活动更新次数
			int campModifyCountint = 0;
			if (!CherryChecker.isNullOrEmpty(campSaveMap.get("campModifyCount"))) {
				campModifyCountint = Integer.parseInt(campSaveMap.get("campModifyCount").toString());
			}
			campaignDTO.setCampModifyCount(campModifyCountint);
			campaignDTO.setCampUpdateTime((String) campSaveMap.get("campUpdateTime"));
			String ruleDetailNew = JSONUtil.serialize(campaignDTO.getRuleDetailMap());
			// 规则体详细
			campaignDTO.setRuleDetail(ruleDetailNew);
			// 更新规则文件
			int ruleResult = binolcpcom02_Service.updateRuleContent(campaignDTO);
			// 更新失败
			if (0 == ruleResult) {
				throw new CherryException("ECM00038");
			}
			// 会员活动更新次数
			campSaveMap.put("campModifyCount", ++campModifyCountint);
		}
		map.put("campSaveMap", campSaveMap);
		// 完成设置
		if ("1".equals(campaignDTO.getSaveStatus())) {
			// 保存规则文件
			saveRuleFile(map);
		}
		return null;
	}
	
	/**
	 * 取得主题活动代号
	 * 
	 * 
	 * @param Map
	 *            	更新数据库参数集合
	 * @return String
	 * 				主题活动代号
	 * @throws Exception 
	 */
	public String getMainCampainCode(Map<String, Object> map) throws Exception {
		// 会员活动代号
		String campainCode = binOLCM03_BL.getTicketNumber(
				String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				String.valueOf(map.get("brandInfoId")), String.valueOf(map.get(CherryConstants.USERID)), "AS");
		if (CherryChecker.isNullOrEmpty(campainCode)) {
			throw new CherryException("ECP00020");
		}
		return campainCode;
	}
	
	/**
	 * 新增会员活动
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public Map<String, Object> addCampaign(Map<String, Object> map) throws Exception {
		try {
			// 会员活动信息
			CampaignDTO campaignDTO = (CampaignDTO) map.get("campInfo");
			// 会员子活动信息
			List<CampaignRuleDTO> campaignRuleList = (List<CampaignRuleDTO>) map.get("campaignRuleList");
			if (CherryChecker.isNullOrEmpty(campaignDTO.getCampaignCode(), true)) {
				// 会员活动代号
				String campainCode = binOLCM03_BL.getTicketNumber(
						campaignDTO.getOrganizationInfoId(), campaignDTO.getBrandInfoId(), String.valueOf(campaignDTO.getCampaignSetBy()), "AS");
				if (CherryChecker.isNullOrEmpty(campainCode)) {
					throw new CherryException("ECP00020");
				}
				campaignDTO.setCampaignCode(campainCode);
			}
//			if (null != campaignDTO.getTemplateType()) {
//				// 积分规则类型
//				String pointRuleType = CodeTable.getVal("1173", campaignDTO.getTemplateType());
//				campaignDTO.setPointRuleType(pointRuleType);
//			}
			// 读取工作流中配置的优先级设置标志
			boolean isClear = "8888".equals(map.get("campaignType"));
    		if("1".equals(map.get("priorityPageFlag")) || isClear){
				// 取得活动组ID
				String grpStr = binolcpcom02_Service.getRuleConCount(map);
				if(null == grpStr && !"3".equals(campaignDTO.getCampaignType())){
					Map<String, Object> paramMap = new HashMap<String, Object>();
					// 规则配置名称
					paramMap.put("grpName", "ruleConfig" + map.get("campaignType"));
					// 规则类型名称
					paramMap.put("campaignType", map.get("campaignType"));
					// 组织Id
					paramMap.put(CherryConstants.ORGANIZATIONINFOID, campaignDTO.getOrganizationInfoId());
					// 品牌Id
					paramMap.put(CherryConstants.BRANDINFOID, campaignDTO.getBrandInfoId());
					// 会员俱乐部ID
					paramMap.put("memberClubId", campaignDTO.getMemberClubId());
					// 作成日时
					paramMap.put("createTime", campaignDTO.getCreateTime());
					// 更新日时
					String updateTime = campaignDTO.getUpdateTime();
					paramMap.put("updateTime", updateTime);
					// 作成程序名
					paramMap.put("createPGM", campaignDTO.getCreatePGM());
					// 更新程序名
					paramMap.put("updatePGM", campaignDTO.getUpdatePGM());
					// 作成者
					paramMap.put("createdBy", campaignDTO.getCreatedBy());
					// 更新者
					paramMap.put("updatedBy", campaignDTO.getUpdatedBy());
					// 插入会员活动组表
					grpStr = String.valueOf(binolcpcom02_Service.insertCampaignConfig(paramMap));
					if (!isClear) {
						// 规则主体
						StringBuffer buffer = new StringBuffer();
						// 模板查询条件集合
						Map<String, Object> tempSearch = new HashMap<String, Object>();
						// 所属品牌
						tempSearch.put("brandInfoId", map.get("brandInfoId"));
						// 所属组织
						tempSearch.put("organizationInfoId", map.get("organizationInfoId"));
						// 会员活动类型
						String campaignType = (String) map.get("campaignType");
						tempSearch.put("campaignType", campaignType);
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
						String packageName = "PKG_GRP_" + grpStr;
						headerMap.put("PACKAGE_NAME", packageName);
						String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
	//					if (CherryChecker.isNullOrEmpty(headerRule)) {
	//						throw new CherryException("ECP00006");
	//					}
						buffer.append(headerRule).append("\n\n");
						// 集合标识符
						tempSearch.put("groupCode", CampUtil.GROUPCODE_DEFAULTRULE);
						// 模板区分:文件主体
						tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_1);
						// 取得规则模板内容
						String ruleTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
						if (CherryChecker.isNullOrEmpty(ruleTemplate)) {
							throw new CherryException("ECP00005");
						}
						// 扩展参数集合
						Map<String, Object> extParams = new HashMap<String, Object>();
						extParams.put("CAMPAIGN_TYPE", campaignType);
						// 生成规则
						String rule = RuleToolUtil.getRule(extParams, ruleTemplate);
						if (CherryChecker.isNullOrEmpty(rule)) {
							throw new CherryException("ECP00006");
						}
						buffer.append(rule).append("\n\n");
						String ruleFileContent = buffer.toString();
						if (!"".equals(ruleFileContent)) {
							Map<String, Object> updateMap = new HashMap<String, Object>();
							updateMap.put("ruleFileContent", ruleFileContent);
							updateMap.put("campaignGrpId", grpStr);
							updateMap.put("grpUpdateTime", campaignDTO.getUpdateTime());
							updateMap.put("grpModifyCount", 0);
							// 更新会员活动组表(规则文件)
							int result = binolcpcom02_Service.updateConfigRuleFile(updateMap);
							if (0 == result) {
								throw new CherryException("ECP00019");
							}
						}
					}
				}
				if (!CherryChecker.isNullOrEmpty(grpStr)) {
					campaignDTO.setCampaignGrpId(grpStr);
				}
    		}
    		//设置活动是否下发区分
//    		setSendFlag(campaignDTO);
			// 插入会员活动表并返回会员活动ID
			int campaignId = binolcpcom02_Service.insertCampaign(campaignDTO);
			Map<String, Object> campSaveMap = new HashMap<String, Object>();
			// 更新时间
			String updateTime = campaignDTO.getUpdateTime();
			// 会员活动ID
			campSaveMap.put("campaignId", campaignId);
			// 会员活动更新日时
			campSaveMap.put("campUpdateTime", updateTime);
			// 会员活动更新次数
			campSaveMap.put("campModifyCount", 0);
			List<Map<String, Object>> campRuleList = new ArrayList<Map<String, Object>>();
			String brandCode = binOLCM05_BL.getBrandCode(Integer.parseInt(campaignDTO.getBrandInfoId()));
			for (CampaignRuleDTO campaignRule : campaignRuleList) {
				campaignRule.setState(campaignDTO.getState());
				campaignRule.setCampaignId(campaignId);
				// 子活动代号
				String subCampainCode = CampaignCode.getSubCampaignCode(brandCode
						, campaignDTO.getOrganizationInfoId()
						, campaignDTO.getBrandInfoId()
						, String.valueOf(campaignDTO.getCampaignSetBy()));
						//binOLCM03_BL.getTicketNumber(
						//campaignDTO.getOrganizationInfoId(), campaignDTO.getBrandInfoId(), String.valueOf(campaignDTO.getCampaignSetBy()), "AT");
				if (CherryChecker.isNullOrEmpty(subCampainCode)) {
					throw new CherryException("ECP00021");
				}
				campaignRule.setSubCampaignCode(subCampainCode);
				// 保存子活动内容
				saveSubCampaign(map,campaignRule, campRuleList);
				// 保存活动条件和结果
				saveConditonResult(campaignRule);
			}
			// 会员子活动保存信息
			campSaveMap.put("campRuleList", campRuleList);
			return campSaveMap;	
		} catch (Exception e) {
			logger.error(PropertiesUtil.getText("ECP00014") + "：" + e.getMessage(),e);
			throw new CherryException("ECM00005");
		}
	}
	
	/**
	 * 保存活动条件和结果
	 * 
	 * @param campaignRule
	 * 			子活动信息
	 * @param campRuleList
	 * 			子活动更新信息
	 * @throws Exception 
	 * 
	 */
	private void saveConditonResult(CampaignRuleDTO campaignRule) throws Exception {
		// 更新子活动
		if ("1".equals(campaignRule.getUpKbn())) {
			// 停用会员活动规则条件明细
			binolcpcom02_Service.invalidRuleCondition(campaignRule);
//			// 停用会员活动规则结果明细
//			binolcpcom02_Service.invalidRuleResult(campaignRule);
			// 物理删除会员活动规则结果明细表
			binolcpcom02_Service.delRuleResult(campaignRule);
		}
		BaseDTO baseDto = new BaseDTO();
		// 作成者
		baseDto.setCreatedBy(campaignRule.getCreatedBy());
		// 作成程序名
		baseDto.setCreatePGM(campaignRule.getCreatePGM());
		// 作成日时
		baseDto.setCreateTime(campaignRule.getCreateTime());
		// 更新者
		baseDto.setUpdatedBy(campaignRule.getUpdatedBy());
		// 更新程序名
		baseDto.setUpdatePGM(campaignRule.getUpdatePGM());
		// 更新日时
		baseDto.setUpdateTime(campaignRule.getUpdateTime());
		List<CampRuleConditionDTO> ruleCondList = campaignRule.getCampRuleCondList();
		List<CampRuleResultDTO> ruleResultList = campaignRule.getCampRuleResultList();
		int campRuleId = campaignRule.getCampaignRuleId();
		if (!ruleCondList.isEmpty()) {
			for (int i = 0; i < ruleCondList.size(); i++) {
				CampRuleConditionDTO ruleConditon = ruleCondList.get(i);
				if (CherryChecker.isNullOrEmpty(ruleConditon.getBasePropValue1())) {
					ruleCondList.remove(i);
					i--;
				} else {
					ruleConditon.setCampaignRuleId(campRuleId);
					ConvertUtil.convertDTO(ruleConditon, baseDto, false);
				}
			}
			// 插入会员活动规则条件明细表
			binolcpcom02_Service.addRuleConditions(ruleCondList);
		}
		if (!ruleResultList.isEmpty()) {
//			// 取得会员活动规则结果明细信息
//			List<CampRuleResultDTO> prevRuleResultList = binolcpcom02_Service.getRuleResultList(campaignRule);
//			// 取得会员活动规则结果明细信息
//			List<CampRuleResultDTO> updateResultList = new ArrayList<CampRuleResultDTO>();
//			if (null != prevRuleResultList) {
//				for (CampRuleResultDTO ruleResult : prevRuleResultList) {
//					for (CampRuleResultDTO ruleResultNew : ruleResultList) {
//						// 相同条码和编码的产品
//						if (ruleResult.getBarCode().equals(ruleResultNew.getBarCode()) &&
//								ruleResult.getUnitCode().equals(ruleResultNew.getUnitCode())){
//							ConvertUtil.convertDTO(ruleResultNew, baseDto, false);
//							ruleResultNew.setCampaignRuleId(campRuleId);
//							updateResultList.add(ruleResultNew);
//							ruleResultList.remove(ruleResultNew);
//							break;
//						}
//					}
//				}
//			}
			for (CampRuleResultDTO ruleResult : ruleResultList) {
				ruleResult.setCampaignRuleId(campRuleId);
				ConvertUtil.convertDTO(ruleResult, baseDto, false);
			}
			// 插入会员活动规则结果明细表
			binolcpcom02_Service.addRuleResults(ruleResultList);
//			if (!updateResultList.isEmpty()) {
//				// 更新会员活动规则结果明细表
//				binolcpcom02_Service.updateRuleResults(updateResultList);
//			}
		}
	}
	/**
	 * 保存子活动内容
	 * 
	 * @param campaignRule
	 * 			子活动信息
	 * @param campRuleList
	 * 			子活动更新信息
	 * @throws Exception 
	 * 
	 */
	private void saveSubCampaign(Map<String, Object> map, CampaignRuleDTO campaignRule, List<Map<String, Object>> campRuleList) throws Exception {
		// 活动规则文件名
		campaignRule.setRuleFileName("RuleFileName");
		// 子活动规则
		campaignRule.setCampaignRule("CampaignRule");
		// 插入会员子活动表并返回子活动ID
		int campaignRuleId = binolcpcom02_Service.insertCampaignRule(campaignRule);
		campaignRule.setCampaignRuleId(campaignRuleId);
		// 新增
		campaignRule.setUpKbn("0");
		// 修改规则详细
		modifyRuleDetail(campaignRule);
		Map<String, Object> campRuleMap = new HashMap<String, Object>();
		// 会员子活动ID
		campRuleMap.put(CampConstants.SUB_CAMP_ID, campaignRuleId);
		// 会员子活动更新日时
		campRuleMap.put("ruleUpdateTime", campaignRule.getUpdateTime());
		// 会员子活动更新次数
		campRuleMap.put("ruleModifyCount", 0);
		if (!CherryChecker.isNullOrEmpty(campaignRule.getSubCampDetailNo(), true)) {
			// 会员子活动连番
			campRuleMap.put("subCampDetailNo", campaignRule.getSubCampDetailNo());
			// 会员子活动类型
			campRuleMap.put("subCampRuleType", campaignRule.getSubCampRuleType());
		}
		campRuleList.add(campRuleMap);
		// 活动档次添加子活动ID
		Map<String, Object> subMap = ActUtil.getCampMap(map, campaignRule.getSubCampDetailNo());
		if (null != subMap) {
			subMap.put(CampConstants.SUB_CAMP_ID, campaignRuleId);
		}
	}
	
	/**
	 * 修改规则详细
	 * 
	 * @param campaignRule
	 * 			子活动信息
	 * @throws Exception 
	 * 
	 */
	private void modifyRuleDetail(CampaignRuleDTO campaignRule) {
		if ("1".equals(campaignRule.getNeedHideId())) {
			// 规则详细
			List<Map<String,Object>> ruleDetailList = campaignRule.getRuleDetailList();
			if (null != ruleDetailList) {
				for (Map<String,Object> ruleDetailMap : ruleDetailList) {
					ruleDetailMap.put(CampConstants.SUB_CAMP_ID, String.valueOf(campaignRule.getCampaignRuleId()));
				}
			}
//			String ruleDetailNew = JSONUtil.serialize(ruleDetailList);
//			campaignRule.setRuleDetail(ruleDetailNew);
		}
	}
	/**
	 * 取得更新数据库用的子活动信息
	 * 
	 * @param campaignRuleList
	 * 			画面提交的子活动信息
	 * @param campRuleList
	 * 			已保存的子活动信息
	 * 
	 */
	private boolean getCampaignRuleDBuse (List<CampaignRuleDTO> campaignRuleList, List<Map<String, Object>> campRuleList) {
		boolean upFlag = false;
		// 是否是默认的子活动
		boolean isDefaultSub = false;
		if (1 == campaignRuleList.size()) {
			// 会员子活动信息
			CampaignRuleDTO campaignRuleDTO = campaignRuleList.get(0);
			// 默认的一个子活动
			if (CherryChecker.isNullOrEmpty(campaignRuleDTO.getSubCampRuleType())) {
				isDefaultSub = true;
				if (1 == campRuleList.size()) {
					Map<String, Object> campRuleInfo = campRuleList.get(0);
					// 设置子活动的更新信息
					addCampRuleUpInfo(campaignRuleDTO, campRuleInfo);
					// 更新
					campaignRuleDTO.setUpKbn("1");
					campRuleList = null;
				} else {
					// 新增
					campaignRuleDTO.setUpKbn("0");
					upFlag = true;
				}
			}
		}
		if (!isDefaultSub) {
			for (CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
				int campaignRuleId = 0;
				if (null != campaignRuleDTO.getCampaignRuleId()) {
					campaignRuleId = campaignRuleDTO.getCampaignRuleId();
				}
				if (0 == campaignRuleId) {
					upFlag = true;
					// 会员子活动连番
					String subCampDetailNo = campaignRuleDTO.getSubCampDetailNo();
					// 会员子活动类型
					String subCampRuleType = campaignRuleDTO.getSubCampRuleType();
					boolean flag = true;
					if (!CherryChecker.isNullOrEmpty(subCampRuleType, true) && 
							!CherryChecker.isNullOrEmpty(subCampDetailNo, true)) {
						for (int i = 0; i < campRuleList.size(); i++) {
							Map<String, Object> campRuleInfo = campRuleList.get(i);
							if (subCampRuleType.equals(campRuleInfo.get("subCampRuleType")) && 
									subCampDetailNo.equals(campRuleInfo.get("subCampDetailNo"))) {
								// 会员子活动ID
								campaignRuleDTO.setCampaignRuleId(Integer.parseInt(String.valueOf(campRuleInfo.get(CampConstants.SUB_CAMP_ID))));
								// 更新
								campaignRuleDTO.setUpKbn("1");
								// 设置子活动的更新信息
								addCampRuleUpInfo(campaignRuleDTO, campRuleInfo);
								// 修改规则详细
								modifyRuleDetail(campaignRuleDTO);
								campRuleList.remove(i);
								flag = false;
								
								break;
							}
						}
					}
					if (flag) {
						// 新增
						campaignRuleDTO.setUpKbn("0");
					}
					continue;
				}
				for (int i = 0; i < campRuleList.size(); i++) {
					Map<String, Object> campRuleInfo = campRuleList.get(i);
					// 会员子活动ID
					int campaignRuleId1 = Integer.parseInt(String.valueOf(campRuleInfo.get(CampConstants.SUB_CAMP_ID))); 
					if (campaignRuleId == campaignRuleId1) {
						// 更新
						campaignRuleDTO.setUpKbn("1");
						// 设置子活动的更新信息
						addCampRuleUpInfo(campaignRuleDTO, campRuleInfo);
						campRuleList.remove(i);
						break;
					}
				}
			}
		}
		if (null != campRuleList) {
			for (int i = 0; i < campRuleList.size(); i++) {
				Map<String, Object> campRuleInfo = campRuleList.get(i);
				CampaignRuleDTO campaignRule = new CampaignRuleDTO();
				// 设置子活动的更新信息
				addCampRuleUpInfo(campaignRule, campRuleInfo);
				// 删除
				campaignRule.setUpKbn("-1");
				campaignRuleList.add(campaignRule);
			}
		}
		return upFlag;
	} 
	
	/**
	 * 设置子活动的更新信息
	 * 
	 * @param campaignRuleDTO
	 * 			画面提交的子活动信息
	 * @param campaignRuleDTO
	 * 			已保存的子活动信息
	 */
	private void addCampRuleUpInfo(CampaignRuleDTO campaignRuleDTO, Map<String, Object> campRuleInfo){
		// 会员子活动ID
		int campaignRuleId = Integer.parseInt(String.valueOf(campRuleInfo.get(CampConstants.SUB_CAMP_ID))); 
		// 会员子活动更新次数
		int ruleModifyCountint = Integer.parseInt(String.valueOf(campRuleInfo.get("ruleModifyCount"))); 
		campaignRuleDTO.setCampaignRuleId(campaignRuleId);
		campaignRuleDTO.setRuleModifyCount(ruleModifyCountint);
		campaignRuleDTO.setRuleUpdateTime((String) campRuleInfo.get("ruleUpdateTime"));
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
			CreateRule_IF createRuleIF = (CreateRule_IF) map.get("createRuleIF");
			if (null != createRuleIF) {
				// 会员活动信息
				CampaignDTO campaignDTO = (CampaignDTO) map.get("campInfo");
				// 会员子活动信息
				List<CampaignRuleDTO> campaignRuleList = (List<CampaignRuleDTO>) map.get("campaignRuleList");
				if (null != campaignRuleList && !campaignRuleList.isEmpty()) {
					for (int i = 0; i < campaignRuleList.size(); i++) {
						// 停用子活动
						if ("-1".equals(campaignRuleList.get(i).getUpKbn())) {
							campaignRuleList.remove(i);
							i--;
						}
					}
					List<RuleFilterDTO> filterList = new ArrayList<RuleFilterDTO>();
					boolean resetFlg = false;
					// 规则主体
					StringBuffer buffer = new StringBuffer();
					// 规则模板集合
					Map<String, Object> ruleTemplates = new HashMap<String, Object>(); 
					// 模板查询条件集合
					Map<String, Object> tempSearch = new HashMap<String, Object>();
					// 所属品牌
					tempSearch.put("brandInfoId", campaignDTO.getBrandInfoId());
					// 所属组织
					tempSearch.put("organizationInfoId", campaignDTO.getOrganizationInfoId());
					// 会员活动类型
					tempSearch.put("campaignType", campaignDTO.getCampaignType());
					// 模板区分:文件头
					tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_0);
					// 模板类型:会员活动
					tempSearch.put("templateType", CherryConstants.TEMPLATE_TYPE_0);
					// 取得文件头的规则模板
					String headerTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
					if (CherryChecker.isNullOrEmpty(headerTemplate)) {
						throw new CherryException("ECP00005");
					}
					Map<String, Object> headerMap = new HashMap<String, Object>();
					int campRuleId = campaignDTO.getCampaignId();
					// 包名称
					String packageName = "PKG_" + campRuleId;
					headerMap.put("PACKAGE_NAME", packageName);
					String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
					if (CherryChecker.isNullOrEmpty(headerRule)) {
						throw new CherryException("ECP00006");
					}
					buffer.append(headerRule).append("\n\n");
					// 规则执行顺序
					int salience = CampUtil.MAX_SALIENCE;
					int index = 0;
					for (CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
						// 解析规则体详细信息(Map)
						List<Map<String, Object>> allCamTempList = campaignRuleDTO.getRuleDetailList();
						if (null != allCamTempList && !allCamTempList.isEmpty()) {
							// 取得会员子活动信息
							Map<String, Object> subCampInfo = binolcpcom02_Service.getSubCampaignRuleInfo(campaignRuleDTO);
							if (null != subCampInfo && !subCampInfo.isEmpty()) {
								// 子活动代号
								String subCampaignCode = (String) subCampInfo.get("subCampaignCode");
								campaignRuleDTO.setSubCampaignCode(subCampaignCode);
							}
							List<RuleBodyDTO> ruleBodyList = new ArrayList<RuleBodyDTO>();
							// 取得规则内容
							getRuleContents(allCamTempList, map, ruleBodyList);
							if (!ruleBodyList.isEmpty()) {
								RuleDTO ruleDTO = new RuleDTO();
								// 子活动ID
								ruleDTO.setCampaignRuleId(campaignRuleDTO.getCampaignRuleId());
								BaseDTO baseDTO = (BaseDTO) map.get("BaseDTO");
								if (null != baseDTO) {
									ConvertUtil.convertDTO(ruleDTO, baseDTO, true);
								}
								// 更新活动
								if ("1".equals(campaignRuleDTO.getUpKbn())) {
									// 停用规则
									binolcpcom02_Service.invalidRuleDTO(ruleDTO);
								}
								// 共通条件
								StringBuffer comCondBuffer = new StringBuffer();
								// 共通：LHS参数(条件参数)
								Map<String, Object> commLhsParams = new HashMap<String, Object>();
								// 共通：RHS参数(结果参数)
								Map<String, Object> commRhsParams = new HashMap<String, Object>();
								for (int i = 0; i < ruleBodyList.size(); i++) {
									RuleBodyDTO ruleBody = ruleBodyList.get(i);
									// 默认规则名
									String ruleName = "Camp_" + campRuleId + "_" + index;
									index++;
									ruleBody.setRuleName(ruleName);
									if (null != ruleBody.getCommLhsParams() && !ruleBody.getCommLhsParams().isEmpty()) {
										commLhsParams.putAll(ruleBody.getCommLhsParams());
									}
									if (null != ruleBody.getCommRhsParams() && !ruleBody.getCommRhsParams().isEmpty()) {
										commRhsParams.putAll(ruleBody.getCommRhsParams());
									}
									// 规则过滤器 DTO
									RuleFilterDTO filter = ruleBody.getRuleFilterVal();
									if (null != filter) {
										filter.setRuleName(ruleName);
										if (null != filter.getParams()) {
											// 活动ID
											filter.getParams().put("CAMPAIGNLOGID", campaignDTO.getCampaignId());
										}
										filterList.add(filter);
									}
									if (ruleBody.isCommCondition()) {
										if (!CherryChecker.isNullOrEmpty(ruleBody.getCondition())) {
											if (!CherryChecker.isNullOrEmpty(comCondBuffer.toString())) {
												comCondBuffer.append(CampUtil.HALF_COMMA);
											}
											String comConditon = ruleBody.getCondition().replaceAll(CampUtil.RULE_NAME_REGEX, ruleName);
											comCondBuffer.append(comConditon);
										}
										ruleBodyList.remove(i);
										i--;
									}
								}
								// 共通条件
								String comConditon = comCondBuffer.toString();
								for(int i = 0; i < ruleBodyList.size(); i++) {
									RuleBodyDTO ruleBody = ruleBodyList.get(i);
									// 页面模板内容
									Map<String, Object> pageTemp = ruleBody.getPageTemp();
									if (null == pageTemp || pageTemp.isEmpty()) {
										throw new CherryException("ECP00004");
									}
									// 规则内容
									String ruleContent = ruleBody.getDescription();
									// 默认规则名
									String ruleName = ruleBody.getRuleName();
									// 扩展参数集合
									Map<String, Object> extParams = ruleBody.getExtParams();
									if (null == extParams) {
										extParams = new HashMap<String, Object>();
									}
									// 规则过滤器 DTO
									RuleFilterDTO filter = ruleBody.getRuleFilterVal();
									// 规则ID
									String ruleId = (String) pageTemp.get("ruleId");
									// 规则内容不为空
									if (!CherryChecker.isNullOrEmpty(ruleContent)) {
										ruleDTO.setRuleContent(ruleContent);
										if (CherryChecker.isNullOrEmpty(ruleId)) {
											// 插入规则表返回规则ID
											int ruleIdNew = binolcpcom02_Service.insertRuleDTO(ruleDTO);
											if (0 == ruleIdNew) {
												throw new CherryException("ECP00004");
											}
											ruleId = String.valueOf(ruleIdNew);
											pageTemp.put("ruleId", ruleId);
											resetFlg = true;
										} else {
											ruleDTO.setRuleId(Integer.parseInt(ruleId));
											// 更新规则表
											int uprst = binolcpcom02_Service.updateRuleDTO(ruleDTO);
											if (0 == uprst) {
												throw new CherryException("ECP00004");
											}
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
									// 规则条件
									String condition = ruleBody.getCondition();
									StringBuffer condbuffer = new StringBuffer();
									if (ruleBody.isPriority()) {
										// 判断规则是否属于执行队列
										condbuffer.append(CampUtil.RULEKEYS_CHECK);
									}
									if (!CherryChecker.isNullOrEmpty(comConditon)) {
										if (!CherryChecker.isNullOrEmpty(condbuffer.toString())) {
											condbuffer.append(CampUtil.HALF_COMMA);
										}
										// 判断规则是否属于执行队列
										condbuffer.append(comConditon);
									}
									if (!CherryChecker.isNullOrEmpty(condition)) {
										boolean flag = !CherryChecker.isNullOrEmpty(condbuffer.toString());
										if (flag) {
											condbuffer.append(CampUtil.HALF_COMMA).append(CampUtil.LEFT_BRACKET);
										}
										// 判断规则是否属于执行队列
										condbuffer.append(condition);
										if (flag) {
											condbuffer.append(CampUtil.RIGHT_BRACKET);
										}
									}
									// 规则名称
									extParams.put("RULE_NAME", ruleName);
									// 活动ID
									extParams.put("CAMPAIGN_ID", campaignDTO.getCampaignId());
									// 规则执行顺序
									extParams.put("RULE_SALIENCE", salience);
									// 子活动代号
									extParams.put("SUBCAMPCODE", campaignRuleDTO.getSubCampaignCode());
									// 规则描述ID
									extParams.put("RULEDPT_ID", ruleId);
									// 包名
									extParams.put("PACKAGE_NAME", packageName);
									if (null != filter) {
										Map<String, Object> rhsParams = filter.getRhsParams();
										if (null != rhsParams && !rhsParams.isEmpty()) {
											// 规则名称
											rhsParams.put("RULE_NAME", ruleName);
											// 活动ID
											rhsParams.put("CAMPAIGN_ID", campaignDTO.getCampaignId());
											// 子活动ID
											rhsParams.put("SUBCAMPID", campaignRuleDTO.getCampaignRuleId());
											// 规则执行顺序
											rhsParams.put("RULE_SALIENCE", salience);
											// 子活动代号
											rhsParams.put("SUBCAMPCODE", campaignRuleDTO.getSubCampaignCode());
											// 规则描述ID
											rhsParams.put("RULEDPT_ID", ruleId);
											rhsParams.remove("RULECONTENT");
											if (!commRhsParams.isEmpty()) {
												rhsParams.putAll(commRhsParams);
											}
										}
										Map<String, Object> lhsParams = filter.getParams();
										if (!commLhsParams.isEmpty()) {
											if (null != lhsParams) {
												lhsParams.putAll(commLhsParams);
											}
										}
									}
									salience--;
									// 生成规则
									String rule = RuleToolUtil.getRuleCondition(condbuffer.toString(), ruleTemplate);
									rule = RuleToolUtil.getRule(extParams, rule);
									if (CherryChecker.isNullOrEmpty(rule)) {
										throw new CherryException("ECP00006");
									}
									buffer.append(rule).append("\n\n");
								}
							}
						}
					}
					if (!filterList.isEmpty()) {
						// 规则过滤器
						String ruleFilter = JSONUtil.serialize(filterList);
						campaignDTO.setRuleFilter(ruleFilter);
					}
					if (resetFlg) {
						String ruleDetailNew = JSONUtil.serialize(campaignDTO.getRuleDetailMap());
						// 规则体详细
						campaignDTO.setRuleDetail(ruleDetailNew);
					}
					// 规则文件内容
					String ruleFileContent = buffer.toString();
					// 规则内容不正确时
					if (!checkRuleContent(ruleFileContent)) {
						throw new CherryException("ECP00022");
					}
					// 规则文件内容 
					campaignDTO.setRuleFileContent(ruleFileContent);
					Map<String, Object> campSaveMap = (Map<String, Object>) map.get("campSaveMap");
					if (null == campSaveMap || campSaveMap.isEmpty()) {
						throw new CherryException("ECM00005");
					}
					// 会员子活动更新次数
					int campModifyCountint = 0;
					if (!CherryChecker.isNullOrEmpty(campSaveMap.get("campModifyCount"))) {
						campModifyCountint = Integer.parseInt(campSaveMap.get("campModifyCount").toString());
					}
					campaignDTO.setCampModifyCount(campModifyCountint);
					campaignDTO.setCampUpdateTime((String) campSaveMap.get("campUpdateTime"));
					// 更新规则文件
					int ruleResult = binolcpcom02_Service.updateRuleContent(campaignDTO);
					// 更新失败
					if (0 == ruleResult) {
						throw new CherryException("ECM00038");
					}
					// 会员活动更新次数
					campSaveMap.put("campModifyCount", ++campModifyCountint);
				}
			}
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
			throw new CherryException("ECP00001");
		}
	}
	
	/**
	 * 验证规则内容是否正确
	 * 
	 * @param ruleContent
	 * 			规则内容
	 * 
	 * @return boolean
	 * 			验证结果 true: 正确    false: 错误
	 */
	public boolean checkRuleContent(String ruleContent) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		if (!CherryChecker.isNullOrEmpty(ruleContent)) {
			InputStream in = null;
			try {
				// 加载配置规则
				in = new ByteArrayInputStream(ruleContent.getBytes("UTF-8"));
				kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
			} catch (Exception e) {
				logger.error("method: checkRuleContent, error message: " + e.getMessage(),e);
			} finally {
				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error("method: checkRuleContent, error message: " + e.getMessage(),e);
					}
				}
			}
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					logger.error("method: checkRuleContent, error message: " + error.getMessage(),error);
				}
			} else {
				if (kbuilder.getKnowledgePackages().size() != 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 取得规则内容
	 * 
	 * @param camTempList
	 * 			规则设置内容
	 * @param map
	 * 			参数集合
	 * @param ruleBodyList
	 * 			规则内容 
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void getRuleContents(List<Map<String, Object>> camTempList, Map<String, Object> map, List<RuleBodyDTO> ruleBodyList) throws Exception {
		if (null != camTempList) {
			// 循环规则设置内容
			for (Map<String, Object> camTemp : camTempList) {
				// 规则模板区分
				String ruleTempFlag = (String) camTemp.get("RULE_TEMP_FLAG");
				// 有规则模板
				if ("1".equals(ruleTempFlag)) {
					// 创建规则处理 IF
					CreateRule_IF createRuleIF = (CreateRule_IF) map.get("createRuleIF");
					if (null != createRuleIF) {
						// 集合标识符
						String groupCode = (String) camTemp.get("groupCode");
						RuleBodyDTO ruleBody = createRuleIF.invokeMd(groupCode + "_Rule", map, camTemp);
						if (null != ruleBody) {
							// 条件参数
							Map<String, Object> lhsParams = ruleBody.getLhsParams();
							// 结果参数
							Map<String, Object> rhsParams = ruleBody.getRhsParams();
							if (null != lhsParams && !lhsParams.isEmpty()) {
								ruleBody.getRuleFilter().getParams().putAll(lhsParams);
							}
							if (null != rhsParams && !rhsParams.isEmpty()) {
								ruleBody.getRuleFilter().getRhsParams().putAll(rhsParams);
								ruleBody.getExtParams().putAll(rhsParams);
							}
							ruleBody.setGroupCode((String) camTemp.get("groupCode"));
							ruleBody.setPageTemp(camTemp);
							ruleBodyList.add(ruleBody);
						}
					}
				} else {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
					if (null != combTempList && !combTempList.isEmpty()) {
						// 递归调用
						getRuleContents(combTempList, map, ruleBodyList);
					}
				}
			}
		}
	}
	
	/**
	 * 取得更新会员活动相关表时所需要的参数
	 * 
	 * 
	 * @param Map
	 *            更新数据库参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public void getCamTempParams(Map<String, Object> map) throws Exception {
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) map.get("campInfo");
		if (null == campaignDTO) {
			throw new CherryException("ECM00005");
		}
		// 所属组织
		campaignDTO.setOrganizationInfoId(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)));
		// 所属品牌
		campaignDTO.setBrandInfoId(String.valueOf(map.get(CherryConstants.BRANDINFOID)));
		// 会员活动
    	if (!CampConstants.TYPE_FLAG_1.equals(campaignDTO.getCampaignTypeFlag()) ||
    			"DHHD".equals(campaignDTO.getCampaignType())) {
    		// 会员俱乐部ID
    		campaignDTO.setMemberClubId(String.valueOf(map.get("memberClubId")));
    	} else {
    		campaignDTO.setMemberClubId(null);
    	}
		// 活动负责人
		campaignDTO.setCampaignLeader(campaignDTO.getCampaignSetBy());
		// 活动状态
		campaignDTO.setSaveStatus((String) map.get("saveStatus"));
		if (null != map.get("workFlowId")) {
			// 工作流实例ID
			campaignDTO.setWorkFlowId(String.valueOf(map.get("workFlowId")));
		}
		if (null != map.get("actionId")) {
			// 当前动作ID
			campaignDTO.setActionId(Integer.parseInt(String.valueOf(map.get("actionId"))));
		}
		// 规则体详细
		campaignDTO.setRuleDetail((String) map.get("ruleDetail"));
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
		// 积分类型
		baseParams.put("templateType", campaignDTO.getTemplateType());
		if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
			// 规则详细
			Map<String,Object> ruleDetailMap = (Map<String,Object>) 
			JSONUtil.deserialize(ruleDetail);
			baseParams.put(CampConstants.GROUP_FLAG, campaignDTO.getGroupFlag());
			// 规则详细中插入基本信息
			ruleDetailMap.put("baseParams", baseParams);
			// keyName移动到基本参数Map
			baseParams.put("keyName", ruleDetailMap.get("keyName"));
			ruleDetailMap.remove("keyName");
			map.put("ruleDetail", JSONUtil.serialize(ruleDetailMap));
			// 规则体详细
			campaignDTO.setRuleDetail((String) map.get("ruleDetail"));
			campaignDTO.setRuleDetailMap(ruleDetailMap);
			List<Map<String, Object>> allCamTempList = deseRuleDetailMap(ruleDetailMap);
			if (null != allCamTempList) {
				// 会员活动基础属性 List
				List<CampBasePropDTO> campBasePropList = binolcpcom02_Service.getCampBasePropList(map);
				// 会员活动规则条件明细 List
				List<CampRuleConditionDTO> campRuleCondList = new ArrayList<CampRuleConditionDTO>();
				// 会员活动规则结果明细 List
				List<CampRuleResultDTO> campRuleResultList = new ArrayList<CampRuleResultDTO>();
				// 会员子活动 List
				List<CampaignRuleDTO> campaignRuleList = new ArrayList<CampaignRuleDTO>();
				map.put("campBasePropList", campBasePropList);
				map.put("campRuleCondList", campRuleCondList);
				map.put("campRuleResultList", campRuleResultList);
				map.put("campaignRuleList", campaignRuleList);
				// 保存画面设置的参数
				saveParams(allCamTempList, map, null);
				if (campaignRuleList.isEmpty()) {
					CampaignRuleDTO campaignRule = new CampaignRuleDTO();
					campaignRule.setRuleDetailList(allCamTempList);
					campaignRuleList.add(campaignRule);
				} else {
					List<Map<String, Object>> newAllCamTempList = new ArrayList<Map<String, Object>>();
					for (Map<String, Object> allCamTempMap : allCamTempList) {
						newAllCamTempList.add(allCamTempMap);
					}
					for (int i = 0; i < campaignRuleList.size(); i++) {
						CampaignRuleDTO campaignRule = campaignRuleList.get(i);
						List<Map<String, Object>> ruleDetailList = null;
						campaignRule.setNeedHideId("1");
//						if (i != campaignRuleList.size() - 1) {
							ruleDetailList = campaignRule.getRuleDetailList();
							newAllCamTempList.removeAll(ruleDetailList);
//						} else {
//							ruleDetailList = newAllCamTempList;
//							campaignRule.setRuleDetailList(newAllCamTempList);
//						}
//						if (null != ruleDetailList) {
//							campaignRule.setRuleDetail(JSONUtil.serialize(ruleDetailList));
//						}
					}
					if (!newAllCamTempList.isEmpty()) {
						for (int i = 0; i < campaignRuleList.size(); i++) {
							CampaignRuleDTO campaignRule = campaignRuleList.get(i);
							campaignRule.getRuleDetailList().addAll(newAllCamTempList);
						}
					}
				}
				// 需要保存条件明细
				boolean isCondtion = !campRuleCondList.isEmpty();
				// 需要保存结果明细
				boolean isResult = !campRuleResultList.isEmpty();
				if (isCondtion || isResult) {
					for (CampaignRuleDTO campaignRule : campaignRuleList) {
						if (isCondtion) {
							// 会员活动规则条件明细
							campaignRule.getCampRuleCondList().addAll(campRuleCondList);
						}
						if (isResult) {
							// 会员活动规则结果明细
							campaignRule.getCampRuleResultList().addAll(campRuleResultList);
						}
					}
				}
				map.remove("campRuleCondList");
				map.remove("campRuleResultList");
			}
		}
	}
	
	/**
     * 保存画面设置的参数
     * 
     * @param camTempList
     * 			页面内容
     * @param map
     * 			更新数据库参数集合
	 * @throws Exception 
     */
    private void saveParams(List<Map<String, Object>> camTempList, Map<String, Object> map, CampaignRuleDTO campaignRule) throws Exception {
    	if (null != camTempList) {
			// 循环规则设置内容
			for (Map<String, Object> camTemp : camTempList) {
				// 模板编号
				String tempCode = (String) camTemp.get("tempCode");
				//campaignRule = null;
				// 会员子活动类型
				String subCampRuleType = (String) camTemp.get("SUBCAMP_RULETYPE");
				if (!CherryChecker.isNullOrEmpty(subCampRuleType)) {
					// 会员子活动连番
					String subCampDetailNo = (String) camTemp.get("SUBCAMP_DETAILNO");
					// 会员子活动 List
					List<CampaignRuleDTO> campaignRuleList = (List<CampaignRuleDTO>) map.get("campaignRuleList");
					boolean isMatch = false;
					if (!campaignRuleList.isEmpty()) {
						for (CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
							// 合并同一子活动
							if (subCampRuleType.equals(campaignRuleDTO.getSubCampRuleType()) &&
									(CherryChecker.isNullOrEmpty(subCampDetailNo) 
											&& CherryChecker.isNullOrEmpty(campaignRuleDTO.getSubCampDetailNo())
											|| !CherryChecker.isNullOrEmpty(subCampDetailNo) 
											&& subCampDetailNo.equals(campaignRuleDTO.getSubCampDetailNo()))) {
								campaignRule = campaignRuleDTO;
								isMatch = true;
								break;
							}
						}
					}
					if (!isMatch) {
						campaignRule = new CampaignRuleDTO();
						if (!CherryChecker.isNullOrEmpty(camTemp.get(CampConstants.SUB_CAMP_ID))) {
							// 会员子活动ID
							campaignRule.setCampaignRuleId(Integer.parseInt(String.valueOf(camTemp.get(CampConstants.SUB_CAMP_ID))));
						}
						// 会员子活动类型
						campaignRule.setSubCampRuleType(subCampRuleType);
						// 会员子活动连番
						campaignRule.setSubCampDetailNo(subCampDetailNo);
						
						// 规则体详细
						//campaignRule.setRuleDetail(JSONUtil.serialize(camTemp));
						campaignRuleList.add(campaignRule);
					}
					campaignRule.getRuleDetailList().add(camTemp);
				}
				boolean saveFlag = true;
				List<Map<String, Object>> campList = (List<Map<String, Object>>) camTemp.get("campList");
				if (null != campList && !campList.isEmpty()) {
					// 会员子活动类型
					String subRuleType = (String) campList.get(0).get("SUBCAMP_RULETYPE");
					if (!CherryChecker.isNullOrEmpty(subRuleType)) {
						saveFlag = false;
						for (Map<String, Object> subCampInfo : campList) {
							// 会员子活动连番
							String subCampDetailNo = (String) subCampInfo.get("SUBCAMP_DETAILNO");
							// 会员子活动 List
							List<CampaignRuleDTO> campaignRuleList = (List<CampaignRuleDTO>) map.get("campaignRuleList");
							boolean isMatch = false;
							if (!campaignRuleList.isEmpty()) {
								for (CampaignRuleDTO campaignRuleDTO : campaignRuleList) {
									// 合并同一子活动
									if (subRuleType.equals(campaignRuleDTO.getSubCampRuleType()) &&
											(CherryChecker.isNullOrEmpty(subCampDetailNo) 
													&& CherryChecker.isNullOrEmpty(campaignRuleDTO.getSubCampDetailNo())
													|| !CherryChecker.isNullOrEmpty(subCampDetailNo) 
													&& subCampDetailNo.equals(campaignRuleDTO.getSubCampDetailNo()))) {
										campaignRule = campaignRuleDTO;
										isMatch = true;
										break;
									}
								}
							}
							if (!isMatch) {
								campaignRule = new CampaignRuleDTO();
								if (!CherryChecker.isNullOrEmpty(subCampInfo.get(CampConstants.SUB_CAMP_ID))) {
									// 会员子活动ID
									campaignRule.setCampaignRuleId(Integer.parseInt(String.valueOf(subCampInfo.get(CampConstants.SUB_CAMP_ID))));
								}
								// 会员子活动类型
								campaignRule.setSubCampRuleType(subRuleType);
								// 会员子活动连番
								campaignRule.setSubCampDetailNo(subCampDetailNo);
								
								// 规则体详细
								campaignRuleList.add(campaignRule);
							}
							campaignRule.getRuleDetailList().add(subCampInfo);
							// 模板内容保存处理
							templateSave(tempCode + "_save", map, subCampInfo, campaignRule);
						}
					}
				}
				if (saveFlag) {
					// 模板内容保存处理
					templateSave(tempCode + "_save", map, camTemp, campaignRule);
				}
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
				if (null != combTempList && !combTempList.isEmpty()) {
					saveParams(combTempList, map, campaignRule);
				}
			}
		}
    }
	
	/**
     * 解析规则体详细信息
     * 
     * @param ruleDetail
     * 			规则体详细信息
     * @return
     * 		List
     * 			活动设定内容
	 * @throws Exception 
     */
    private List<Map<String, Object>> deseRuleDetail(String ruleDetail) throws Exception {
    	List<Map<String, Object>> allCamTempList = null;
		if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
			// 规则详细
			Map<String,Object> ruleDetailMap = (Map<String,Object>) 
			JSONUtil.deserialize(ruleDetail);
			deseRuleDetailMap(ruleDetailMap);
		}
		return allCamTempList;
    }
    
    /**
     * 解析规则体详细信息(Map)
     * 
     * @param ruleDetailMap
     * 			规则体详细信息(Map)
     * @return
     * 		List
     * 			活动设定内容
	 * @throws Exception 
     */
    @Override
    public List<Map<String, Object>> deseRuleDetailMap(Map<String, Object> ruleDetailMap) throws Exception {
    	List<Map<String, Object>> allCamTempList = null;
		if (null != ruleDetailMap && !ruleDetailMap.isEmpty()) {
			allCamTempList = new ArrayList<Map<String, Object>>();
			int i = 0;
			while(ruleDetailMap.containsKey("camTemps" + i)) {
				List<Map<String, Object>> camTempsList = (List<Map<String, Object>>) ruleDetailMap.get("camTemps" + i);
				allCamTempList.addAll(camTempsList);
				i++;
			}
		}
		return allCamTempList;
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
    	return binolcpcom02_Service.getRuleDetail(map);
    }
	
	/**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
	@Override
    public CampaignDTO getCampaignInfo(Map<String, Object> map) {
		// 取得规则体详细信息
		CampaignDTO dto = binolcpcom02_Service.getCampaignInfo(map);
		dto.setReferType(CampConstants.REFER_TYPE_0);
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
     * 取得会员保存信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
	public Map<String, Object> getCampSaveInfo(Map<String, Object> paramMap){
		 List<Map<String, Object>> campSaveList = (List<Map<String, Object>>) binolcpcom02_Service.getCampSaveList(paramMap);
		 if (null != campSaveList && !campSaveList.isEmpty()) {
			 Map<String, Object> campSaveMap = campSaveList.get(0);
			 Map<String, Object> campSaveInfo = new HashMap<String, Object>();
			 // 会员活动ID
			 campSaveInfo.put("campaignId", campSaveMap.get("campaignId"));
			 // 更新次数
			 campSaveInfo.put("campModifyCount", campSaveMap.get("campModifyCount"));
			 // 更新日时
			 campSaveInfo.put("campUpdateTime", campSaveMap.get("campUpdateTime"));
			 // 会员子活动保存信息
			 campSaveInfo.put("campRuleList", campSaveList);
			 return campSaveInfo;
		 }
		 return null;
	}
	
	/**
     * 取得等级信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
	public Map<String, Object> getDateMap(Map<String, Object> paramMap){
		return binolcpcom02_Service.getDateMap(paramMap);
	}
	
	/**
     * 取得品牌名称
     * 
     * @param map
     * @return
     * 		品牌名称
     */
    public String getBrandName(Map<String, Object> map) {
    	return binolcpcom02_Service.getBrandName(map);
    }
    
    /**
     * 模板初期化处理
     * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
     * @throws Exception 
     */
    public void templateInit(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
    	// 会员活动基础模板初始化处理
		TemplateInit_IF templateInitIF = (TemplateInit_IF) paramMap.get("templateInitIF");
		if (null != templateInitIF) {
	    	// 基础模板初期处理
			templateInitIF.invokeMd(mdName, paramMap, tempMap);
		}
    }
    
    /**
     * 模板内容保存处理
     * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
     * @throws Exception 
     */
    public void templateSave(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception {
    	// 会员活动基础模板初始化处理
		TemplateInit_IF templateInitIF = (TemplateInit_IF) paramMap.get("templateInitIF");
		if (null != templateInitIF) {
	    	// 基础模板初期处理
			templateInitIF.invokeSaveMd(mdName, paramMap, tempMap, campaignRule);
		}
    }
    
    /**
     * 取得组织品牌代码信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    @Override
    public Map<String, Object> getOrgBrandCodeInfo(Map<String, Object> map) {
        return (Map<String, Object>) binolcpcom02_Service.getOrgBrandCodeInfo(map);
    }
    
    /**
     * 取得当前配置规则ID
     * 
     * @param map
     * @return
     * 		String
     * 		规则配置ID
     */
    @Override
    public String getRuleConCount(Map<String, Object> map) {
        return binolcpcom02_Service.getRuleConCount(map);
    }
    
    /**
     * 取得规则配置中的优先级设置
     * 
     * @param map
     * @return
     * 		Map
     * 		某类型会员活动优先级信息
     */
    @Override
    public String getPriorityMap(Map<String, Object> map) {
        return  binolcpcom02_Service.getPriorityMap(map);
    }
    
    /**
     * 取得规则配置中的优先级设置
     * 
     * @param map
     * @return
     * 		Map
     * 		某类型会员活动优先级信息
     * @throws Exception 
     */
    @Override
    public List<Map<String, Object>> getPriorityByIdMap(String campaignId) throws Exception {
    	Map<String, Object> map = new HashMap();
    	map.put("campaignId", campaignId);
        String priorityDetail =  binolcpcom02_Service.getPriorityByIdMap(map);
        // 若存在配置信息，循环list查询当前ID是否进行过配置
		List<Map<String, Object>> priorityList = null;
        if (!CherryChecker.isNullOrEmpty(priorityDetail)) {
	        // 若存在配置信息，循环list查询当前ID是否进行过配置
			priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityDetail);
        }
		return priorityList;
    }
    
    /**
     * 更新会员活动表（停用）
     * 
     * @return
     * 		无
     * @throws Exception 
     */
    @Override
    public void tran_editValid(Map<String, Object> map) throws Exception{
    	// 更新停用标志
		int res = updateValid(map);
		// 更新报错处理
		if(0 == res){
			throw new CherryException("ECM00005");
		}
		// 停用成功后更新优先级信息
		// 查询该活动是否进行过优先级配置
		String campaignId = (String) map.get("campaignId");
		// 活动类型
		String campaignType = (String) map.get("campaignType");
		// 积分规则
		if ("3".equals(campaignType)) {
			// 取得规则配置列表
			List<Map<String, Object>> ruleConfList = binoljnman06_Service.getRuleConfList(map);
			if (null != ruleConfList && !ruleConfList.isEmpty()) {
				for (Map<String, Object> ruleConfMap : ruleConfList) {
					// 优先级信息
					String priorityInfo = (String) ruleConfMap.get("priorityRuleDetail");
					if (!CherryChecker.isNullOrEmpty(priorityInfo)) {
						List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityInfo);
						if (null != priorityList) {
							if (isContain(campaignId, priorityList)){
								for(int i = 0;i < priorityList.size();i++){
									Map<String, Object> priorityMap = priorityList.get(i);
									if(campaignId.equals(priorityMap.get("campaignId"))){
										priorityList.remove(i);
										i--;
									} else {
										// 活动ID
										List<String> keys = (List<String>) priorityMap.get("keys");
										if (null != keys) {
											for (String key : keys) {
												if (campaignId.equals(key)) {
													keys.remove(key);
													break;
												}
											}
										}
									}
								}
								// 更新后的优先级内容
								ruleConfMap.put("priorityRuleDetail", JSONUtil.serialize(priorityList));
								// 更新配置表的优先级信息
								int result = binoljnman06_Service.updateConfPriority(ruleConfMap);
								// 更新失败
								if (0 == result) {
									throw new CherryException("ECM00005");
								}
							}
						}
					}
				}
			}
		} else {
			List<Map<String, Object>> priorityList = getPriorityByIdMap(campaignId);
			if (isContain(campaignId, priorityList)){
				// 对于进行过优先级配置的活动，停用时要更新优先级配置信息
				// 在优先级配置信息中删除该活动Id
				for(int i = 0;i < priorityList.size();i++){
					Map<String, Object> priorityMap = priorityList.get(i);
					if(campaignId.equals(priorityMap.get("campaignId"))){
						priorityList.remove(i);
						i--;
					} else {
						// 活动ID
						List<String> keys = (List<String>) priorityMap.get("keys");
						if (null != keys) {
							for (String key : keys) {
								if (campaignId.equals(key)) {
									keys.remove(key);
									break;
								}
							}
						}
					}
				}
				// 规则配置优先级信息
				map.put("priorityMes", JSONUtil.serialize(priorityList));
				// 取得活动组ID
				String grpStr = getRuleConCount(map);
				map.put("grpId", grpStr);
				// 更新会员活动组表
				int result = updateCampaignConfig(map);
				// 更新失败
				if (0 == result) {
					throw new CherryException("ECM00005");
				}
		}
			// 刷新单个规则文件
			knowledgeEngine.refreshRule(Integer.parseInt(campaignId));
		}
	}
    
    /**
     * 插入会员活动组表（优先级配置）
     * 
     * @return
     * 		无
     */
    @Override
    public int insertCampaignConfig(Map<String, Object> map) {
       return binolcpcom02_Service.insertCampaignConfig(map);
    }
    
    /**
     * 更新会员活动组表（优先级配置）
     * 
     * @return
     * 		无
     */
    @Override
    public int updateCampaignConfig(Map<String, Object> map) {
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLCPCOM02");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLCPCOM02");
        return binolcpcom02_Service.updateCampaignConfig(map);
    }
    
    /**
     * 更新配置表的优先级信息
     * 
     * @return
     * 		int
     */
    @Override
    public int updateConfPriority(Map<String, Object> map) {
        return binoljnman06_Service.updateConfPriority(map);
    }
    
    /**
     * 更新会员活动表（停用）
     * 
     * @return
     * 		无
     */
    @Override
    public int updateValid(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		return binolcpcom02_Service.updateValid(paramMap);
	}
    
    /**
     * 取得等级信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
    @Override
	public Map<String, Object> getUpdateInfo(Map<String, Object> paramMap){
		return binolcpcom02_Service.getUpdateInfo(paramMap);
	}
    

	/**
	 * 验证优先级组是否包含指定活动
	 * 
	 * @param campaignId
	 * 			活动ID
	 * @param priorityList
	 * 			优先级List
	 * @return 验证结果 true : 包含, false: 不包含
	 * 
	 */
    @Override
	public boolean isContain(String campaignId, List<Map<String, Object>> priorityList) {
		if (null != priorityList) {
			for (Map<String, Object> priorityInfo : priorityList) {
				// 活动ID
				String campId = String.valueOf(priorityInfo.get("campaignId"));
				if (campaignId.equals(campId)) {
					return true;
				} else {
					// 活动ID
					List<String> keys = (List<String>) priorityInfo.get("keys");
					if (null != keys) {
						for (String key : keys) {
							if (campaignId.equals(key)) {
								return true;
							}
						}
					}
					// 活动ID
					List<String> combRules = (List<String>) priorityInfo.get("combRules");
					if (null != combRules) {
						for (String key : combRules) {
							if (campaignId.equals(key)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
    
    /**
     * 取得产品信息
     * 
     * @param map
     * @return
     * 		产品信息
     */
    @Override
	public Map<String, Object> getProInfo(Map<String, Object> paramMap){
		return binolcpcom02_Service.getProInfo(paramMap);
	}
    
    /**
     * 取得产品分类名称
     * 
     * @param map
     * @return
     * 		String
     */
    @Override
	public String getCateName(Map<String, Object> paramMap){
		return binolcpcom02_Service.getCateName(paramMap);
	}
    
    /**
	 * 取得规则配置列表
	 * 
	 * @param map
	 * @return
	 * 			List
	 * 			规则配置列表
	 */
    @Override
	public List<Map<String, Object>> getRuleConfList (Map<String, Object> map) {
		return binoljnman06_Service.getRuleConfList(map);
	}
    
    /**
	 * 删除当前页内容
	 * 
	 * @param allSubCamp
	 * 				前几页子活动的内容
	 * @param curContent
	 * 				当前页子活动的内容
	 * @return
	 * 			Map
	 * 			所有子活动的内容
     * @throws Exception 
	 */
    @Override
	public void delCurContent(Map<String, Object> allSubCamp, String curContent) throws Exception {
    	if (null == allSubCamp || allSubCamp.isEmpty() ||
    			CherryChecker.isNullOrEmpty(curContent, true)) {
    		return;
    	}
    	List<Map<String, Object>> curContentList = (List<Map<String, Object>>) 
    			JSONUtil.deserialize(curContent);
		// 取得当前页面的子活动的内容
		Map<String, Object> pageSubCamp = getSubCampList(curContentList);
		if (null != pageSubCamp && !pageSubCamp.isEmpty()) {
			// 模板编号
			String pageKey = pageSubCamp.get("tempCode") + "_KEYS";
			for(Map.Entry<String, Object> en : allSubCamp.entrySet()){
				Map<String, Object> subCampInfo = (Map<String, Object>) en.getValue();
				// 子活动已删除
				if (null != subCampInfo && subCampInfo.containsKey(pageKey)) {
					List<String> keys = (List<String>) subCampInfo.get(pageKey);
					if (null != keys) {
						for (String conkey : keys) {
							if ("SUBCAMP_DETAILNO".equals(conkey) || 
									"SUBCAMP_RULETYPE".equals(conkey) ||
									"campaignRuleId".equals(conkey)) {
								continue;
							}
							subCampInfo.remove(conkey);
						}
						subCampInfo.remove(pageKey);
					}
				}
			}
		}
    }
    
    /**
	 * 取得所有子活动的内容
	 * 
	 * @param allSubCamp
	 * 				前几页子活动的内容
	 * @param curContent
	 * 				当前页子活动的内容
	 * @return
	 * 			Map
	 * 			所有子活动的内容
     * @throws Exception 
	 */
    @Override
	public Map<String, Object> getAllsubCamp(Map<String, Object> preSubCamp, String curContent) throws Exception {
    	Map<String, Object> allSubCamp = preSubCamp;
    	// 当前页面提交的内容
    	if (!CherryChecker.isNullOrEmpty(curContent, true)) {
    		List<Map<String, Object>> curContentList = (List<Map<String, Object>>) 
        			JSONUtil.deserialize(curContent);
    		// 取得当前页面的子活动的内容
    		Map<String, Object> pageSubCamp = getSubCampList(curContentList);
    		if (null != pageSubCamp && !pageSubCamp.isEmpty()) {
    			// 模板编号
    			String pageKey = pageSubCamp.get("tempCode") + "_KEYS";
    			// 子活动列表
    			List<Map<String, Object>> subCampList = (List<Map<String, Object>>) pageSubCamp.get("campList");
	    		if (null != subCampList && !subCampList.isEmpty()) {
	    			if (null == allSubCamp) {
	    				allSubCamp = new HashMap<String, Object>();
	    			}
	    			String[] subKeys = new String[subCampList.size()];
	    			for (int i = 0; i < subCampList.size(); i++) {
	    				Map<String, Object> subCampMap = subCampList.get(i);
	    				// 子活动编号
	    				String detailNo = (String) subCampMap.get(CampConstants.CAMP_NO);
	    				if (!CherryChecker.isNullOrEmpty(detailNo)) {
	    					String key = CampConstants.SUBCAMP + detailNo;
	    					subKeys[i] = key;
	    					Map<String, Object> subCampInfo = (Map<String, Object>) allSubCamp.get(key);
	    					if (null == subCampInfo) {
	    						subCampInfo = new HashMap<String, Object>();
	    						allSubCamp.put(key, subCampInfo);
	    					}
	    					// 如果存在同一页的KEY,先对上次提交的内容进行删除
	    					if (subCampInfo.containsKey(pageKey)) {
	    						List<String> keys = (List<String>) subCampInfo.get(pageKey);
	    						if (null != keys) {
	    							for (String conkey : keys) {
	    								subCampInfo.remove(conkey);
	    							}
	    						}
	    					}
	    					subCampInfo.putAll(subCampMap);
	    					// 记录本次页面提交内容的KEY
	    					List<String> keyList = new ArrayList<String>();
	    					for(Map.Entry<String, Object> en : subCampMap.entrySet()) {
	    						keyList.add(en.getKey());
	    					}
	    					subCampInfo.put(pageKey, keyList);
	    				}
	    			}
	    			// 去除已删除的子活动
	    			if (null != preSubCamp && !preSubCamp.isEmpty()
	    					&& !allSubCamp.isEmpty()) {
	    				List<String> delKeyList = new ArrayList<String>();
	    				for(Map.Entry<String, Object> en : allSubCamp.entrySet()){
	    					String key = en.getKey();
	    					// 子活动已删除
	    					if (!ConvertUtil.isContain(subKeys, key)) {
	    						delKeyList.add(key);
	    					}
	    				}
	    				for (String delKey : delKeyList) {
	    					allSubCamp.remove(delKey);
	    				}
	    			}
	    		}
    		}
    	}
		return allSubCamp;
    }
    
    /**
	 * 取得当前页面的子活动的内容
	 * 
	 * @param curContentList
	 * 				当前页面内容
	 * @return
	 * 			List
	 * 			当前页面的子活动的内容
	 */
	public Map<String, Object> getSubCampList(List<Map<String, Object>> curContentList) {
		if (null != curContentList && !curContentList.isEmpty()) {
			for (Map<String, Object> curContent : curContentList) {
				// 包含子活动列表
				if (curContent.containsKey("campList")) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					// 模板编号
					resultMap.put("tempCode", curContent.get("tempCode"));
					// 子活动列表
					resultMap.put("campList", curContent.get("campList"));
					return resultMap;
				}
				// 包含子模板
				if (curContent.containsKey("combTemps")) {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) curContent.get("combTemps");
					// 递归调用
					Map<String, Object> resultMap = getSubCampList(combTempList);
					if (null != resultMap) {
						return resultMap;
					}
				}
			}
		}
		return null;
	}
	
	 /**
     * 取得子活动列表 
     * 
     * @param map
     * @return List
     * 		子活动列表 
     */
	@Override
    public List<Map<String, Object>> getSubCampList(Map<String, Object> map) {
    	// 取得子活动列表 
    	return binolcpcom02_Service.getSubCampList(map);
    }
	
	/**
     * 取得会员活动扩展信息
     * 
     * @param map
     */
	@Override
    public void getCampaignExtInfo(CampaignDTO campaignDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaignId", campaignDTO.getCampaignId());
		// 取得会员活动扩展信息
    	Map<String, Object> extInfo = binolcpcom02_Service.getCampaignExtInfo(map);
    	if (null != extInfo && !extInfo.isEmpty()) {
    		// 活动类型
    		campaignDTO.setSubCampType((String) extInfo.get("subCampaignType"));
    		// 是否采集活动获知方式
    		campaignDTO.setIsCollectInfo((String) extInfo.get("isCollectInfo"));
    		// 本地校验规则
    		campaignDTO.setLocalValidRule((String) extInfo.get("localValidRule"));
    		// 活动验证
    		campaignDTO.setSubCampaignValid((String) extInfo.get("subCampaignValid"));
    	}
    }
	/**
	 * 设置活动是否下发区分
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	private void setSendFlag(CampaignDTO dto){
		if("1".equals(dto.getCampaignTypeFlag())){
			Map<String,Object> codeMap = CodeTable.getCode("1174", dto.getCampaignType());
			String sendFlag = ConvertUtil.getString(codeMap.get("grade"));
			if("2".equals(sendFlag)){
				sendFlag = dto.getNeedBuyFlag();
			}
			dto.setSendFlag(sendFlag);
		}
	}
	
	/**
     * 取得可兑换积分截止日期LIST
     * 
     * @param map
     * @return List
     */
	@Override
    public List<Map<String, String>> getExPointDeadDateList(Map<String, Object> map) {
    	return binolcpcom02_Service.getExPointDeadDateList(map);
    }
	
	@Override
	public List<Integer> getActIdByName(String name, String brandInfoId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put(CampConstants.CAMP_NAME, name);
		return binolcpcom02_Service.getActIdByName(map);
	}
}
