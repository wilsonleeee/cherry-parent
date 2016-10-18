/*
 * @(#)BINOLJNMAN03_BL.java     1.0 2011/4/18
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
package com.cherry.jn.man.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.jn.common.interfaces.BINOLJNCOM01_IF;
import com.cherry.jn.common.service.BINOLJNCOM01_Service;
import com.cherry.jn.man.interfaces.BINOLJNMAN03_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 确认与创建BL
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN03_BL implements BINOLJNMAN03_IF{
	
	@Resource
	private BINOLJNCOM01_Service binoljncom01_Service;
	
	@Resource
    private BINOLJNCOM01_IF binoljncom01IF;
	
	/**
	 * 取得更新会员活动相关表时所需要的参数
	 * 
	 * 
	 * @param Map
	 *            更新数据库参数集合
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void getCamTempParams(Map<String, Object> map) throws Exception {
		// 会员活动DTO
		CampaignDTO campaignInfo = (CampaignDTO) map.get("campaignInfo");	
		if (null == campaignInfo) {
			return;
		}
		BaseDTO baseDto = new BaseDTO();
		// 系统时间
		String sysDate = binoljncom01_Service.getSYSDate();
		// 作成日时
		baseDto.setCreateTime(sysDate);
		// 更新日时
		baseDto.setUpdateTime(sysDate);
		// 作成程序名
		baseDto.setCreatePGM("BINOLJNMAN03");
		// 更新程序名
		baseDto.setUpdatePGM("BINOLJNMAN03");
		// 作成者
		baseDto.setCreatedBy((String) map.get(CherryConstants.CREATEDBY));
		// 更新者
		baseDto.setUpdatedBy((String) map.get(CherryConstants.UPDATEDBY));
		map.put("baseDTO", baseDto);
		// 子活动DTO
		CampaignRuleDTO campaignRuleInfo = campaignInfo.getCampaignRule();
		if (null != campaignRuleInfo) {
			// 规则体详细
			campaignRuleInfo.setRuleDetail((String) map.get("camTemps"));
			ConvertUtil.convertDTO(campaignRuleInfo, baseDto, true);
		}
		// 操作区分
		int operKbn = campaignInfo.getOperKbn();
		// 新建入会活动
		if (1 == operKbn) {
			// 会员活动代号
			String campainCode = binoljncom01_Service.createCampaignCode("AASCR", "001");
			campaignInfo.setCampaignCode(campainCode);
			// 会员活动类型
			campaignInfo.setCampaignType("9");
			// 会员活动名称
			campaignInfo.setCampaignName("会员入会");
			// 活动设定者
//			campaignInfo.setCampaignSetBy("1");
//			// 活动负责人
//			campaignInfo.setCampaignLeader("1");
			// 活动状态
			campaignInfo.setState("0");
			ConvertUtil.convertDTO(campaignInfo, baseDto, true);
			if (null != campaignRuleInfo) {
				// 子活动代号
				String subCampainCode = binoljncom01_Service.createCampaignCode("AATCR", "001");
				campaignRuleInfo.setSubCampaignCode(subCampainCode);
				// 活动规则文件名
				campaignRuleInfo.setRuleFileName("RuleFileName");
				// 子活动规则
				campaignRuleInfo.setCampaignRule("CampaignRule");
				// 活动状态
				campaignRuleInfo.setState("0");
				ConvertUtil.convertDTO(campaignRuleInfo, baseDto, true);
			}
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
	public void tran_saveCampaign(Map<String, Object> map) throws Exception {
		// 会员活动保存处理
		String rule = binoljncom01IF.saveCampaign(map);
		if (null != rule && !"".equals(rule)){
			// 会员活动信息
			CampaignDTO campaignInfo = (CampaignDTO) map.get("campaignInfo");
			if (null != campaignInfo) {
				// 会员子活动DTO
				CampaignRuleDTO campaignRuleInfo = campaignInfo.getCampaignRule();
				if (null != campaignRuleInfo) {
					DBObject dbObject= new BasicDBObject();
					// 规则ID
					dbObject.put("CampJoinID", ConvertUtil.getString(campaignRuleInfo.getCampaignRuleId()));
					MongoDB.removeAll("MGO_CampJoinDetail", dbObject);
					// 设定规则Drl
					dbObject.put("RuleDrl", rule);
					// 会员活动ID
					dbObject.put("CampaignID", ConvertUtil.getString(campaignInfo.getCampaignId()));
					// 所属组织
					dbObject.put("OrganizationInfoId", ConvertUtil.getString(campaignInfo.getOrganizationInfoId()));
					// 所属品牌
					dbObject.put("BrandInfoId", ConvertUtil.getString(campaignInfo.getBrandInfoId()));
					// 开始日期
//					dbObject.put("FromDate", DateUtil.date2String(campaignInfo.getCampaignFromDate(), "yyyy-MM-dd"));
//					// 截止日期
//					dbObject.put("ToDate", DateUtil.date2String(campaignInfo.getCampaignToDate(), "yyyy-MM-dd"));
					MongoDB.insert("MGO_CampJoinDetail", dbObject);
				}
			}
		}
	}
}
