/*  
 * @(#)MemberPointInfoLogic.java     1.0 2014/08/01      
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
package com.cherry.webservice.member.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.member.service.MemberNextAmountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 下发俱乐部BL
 * 
 * @author GeHeqnu
 * @version 1.0 2016.10.20
 */
public class MemberNextAmountLogic implements IWebservice{

	/** 打印日志 */
	private Logger logger = LoggerFactory.getLogger(MemberNextAmountLogic.class);
	
	@Resource(name="memberNextAmountService")
	private MemberNextAmountService memberNextAmountService;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon02BL;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	private String curLevelCode=null;
	private String curLevelName=null;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public Map tran_execute(Map map) throws Exception {

		// 返回值
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = validateParam(map);
			// 校验不通过时直接返回
			if (null != resultMap && !resultMap.isEmpty()) {
				return resultMap;
			}
			Map<String,Object> paramMap = new HashMap<String, Object>();
			String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
			String orgId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
			String orgCode = ConvertUtil.getString(map.get("OrgCode"));
			String brandCode = ConvertUtil.getString(map.get("BrandCode"));
			String memCode = ConvertUtil.getString(map.get("MemCode"));
			
//			logger.info("MemberNextAmountLogic计算差额会员:"+memCode);
			paramMap.put("memCode", memCode);
			paramMap.put("brandInfoId", brandId);
			paramMap.put("organizationInfoId", orgId);
			paramMap.put("orgCode", orgCode);
			paramMap.put("brandCode", brandCode);
			//取得会员信息
			Map<String,Object> memInfo = memberNextAmountService.getMemberInfoMap(paramMap);
			
			//判断是否存在会员
			if (null == memInfo || memInfo.isEmpty()) {
				resultMap.put("ERRORCODE", "WMB0000");
				resultMap.put("ERRORMSG", "查不到会员信息");
				return resultMap;
			}
			
			//会员当前等级id
			int curLevel = ConvertUtil.getInt(memInfo.get("memberLevel"));		
			//取得会员等级levelList
			List<Map<String,Object>> levelList = memberNextAmountService.getMemberLevelList(paramMap);
			
			//循环list将periodValidity转为map
			for(Map<String,Object> levelMap :levelList){
				String per = ConvertUtil.getString(levelMap.get("periodValidity"));
				levelMap.put("periodValidity", CherryUtil.json2Map(per));
//				if (curLevel == ConvertUtil.getInt(levelMap.get("levelId"))) {
//					curLevelCode = ConvertUtil.getString(levelMap.get("levelCode"));
//					curLevelName = ConvertUtil.getString(levelMap.get("levelName"));
//					isContain = true;
//					break;
//					
//				}
			}
			//设置会员等级code以及等级name
			setLevelInfo(levelList,curLevel);
			
			memInfo.put("brandInfoID",brandId);
			memInfo.put("organizationInfoID", orgId);
			memInfo.put("orgCode", orgCode);
			memInfo.put("brandCode", brandCode);
			//判断是否为最低等级
			int isLowestLevel = lowestLevel(curLevel, levelList);
			if(isLowestLevel != 0) {
				Map<String,Object> resultContent = new HashMap<String, Object>();
//				double balance = 200.00;
				String balance = binOLCM14_BL.getConfigValue("1400", orgId, brandId);
				if (CherryChecker.isNullOrEmpty(balance)){
					balance = "200";
				}
				resultContent.put("Balance", balance);
				resultContent.put("CurLevelCode", curLevelCode);
				resultContent.put("CurLevelName", curLevelName);
				resultMap.put("ResultContent", resultContent);
				return resultMap;
			}
			
			//判断是否为最高等级
			if (!isHighestLevel(curLevel, levelList)) {
				//升级金额
				double upLevelAmount = 0;
				
				CampBaseDTO campBaseDTO = binbedrcom01BL.getCampBaseDTO(memInfo);
				
				campBaseDTO.setMemberLevels(levelList);
				
				Map<String, Object> newExtArgs = new HashMap<String, Object>();
				//将会员信息存储到DTO中
				setBaseDTO(campBaseDTO,memInfo,newExtArgs,curLevel);
				// 会员升降级规则处理
				binbedrjon02BL.ruleExec(campBaseDTO);
				// 提取本次计算的升级所需金额
				Object upAmountObj = campBaseDTO.getExtArgs().get("UPLELAMOUNT");
				if (null != upAmountObj) {
					upLevelAmount = Double.parseDouble(upAmountObj.toString());
				}
//				resultMap.put("ERRORCODE", 0);
				Map<String,Object> resultContent = new HashMap<String, Object>();
				resultContent.put("Balance", upLevelAmount);
				resultContent.put("CurLevelCode", curLevelCode);
				resultContent.put("CurLevelName", curLevelName);
				resultMap.put("ResultContent", resultContent);
				logger.info("MemberNextAmountLogic计算会员:［"+memCode+"］的下一级所需金额为:"+upLevelAmount);
			} else {
				resultMap.put("ERRORCODE", "-1");
				resultMap.put("ERRORMSG", "该会员已经为最高等级!");
			} 
			
		} catch (Exception e) {
			logger.error("WebService发生未知异常。");
			logger.error(e.getMessage(),e);
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "WebService处理过程中发生未知异常");
		}
		return resultMap;
	}
	
	private void setBaseDTO(CampBaseDTO campBaseDTO,Map<String,Object> map,Map<String, Object> newExtArgs,int memberLevle) throws Exception{

		newExtArgs.put("NO_AFTEXEC", "1");
		// 需要计算升级所需金额的标志 1为需要计算
		newExtArgs.put("CALCUPATKBN","1");
		// 等级计算时累计金额统计方式
		String tjkbn = binOLCM14_BL.getConfigValue("1137", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
		newExtArgs.put("TJKBN", tjkbn);
		// 等级初始日期
		String zdate = null;
		// 品牌特殊参数
		String zparams = binOLCM14_BL.getConfigValue("1361", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
		if (!CherryChecker.isNullOrEmpty(zparams, true)) {
			Map<String, Object> params = CherryUtil.json2Map(zparams.trim());
			zdate = (String) params.get("zdate");
		}
		newExtArgs.put("ZDL", zdate);
		//设置BrandCode
		campBaseDTO.setBrandCode(ConvertUtil.getString(map.get("brandCode")));
		//设置orgCode
		campBaseDTO.setOrgCode(ConvertUtil.getString(map.get("orgCode")));
		campBaseDTO.setMemberClubId(0);
		campBaseDTO.setExtArgs(newExtArgs);

	}
	
	/**
	 * 校验参数
	 * @param map
	 * @return
	 */
	private Map<String,Object> validateParam(Map<String,Object> map){
		// 返回值
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 判断必填字段是否为空
		if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("TradeType")))) {
			resultMap.put("ERRORCODE", "WBM8001");
			resultMap.put("ERRORMSG", "TradeType不能为空");
			return resultMap;
		}
		if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("MemCode")))){
			resultMap.put("ERRORCODE", "WBM8002");
			resultMap.put("ERRORMSG", "会员卡号不能为空");
			return resultMap;
		}
		return resultMap;
	}
	private int lowestLevel(int levelId,List<Map<String,Object>> levelList){
		if (null != levelList) {
			// 最低等级
			int lowestLevelId = 99;
			// 最低级别
			int lowestGrade = 99;
			for (int i = 0; i < levelList.size(); i++) {
				Map<String, Object> levelInfo = levelList.get(i);
				// 会员等级
				int memLevelId = Integer.parseInt(levelInfo.get("levelId").toString());
				// 级别
				int grade = Integer.parseInt(levelInfo.get("grade").toString());
				if (grade < lowestGrade) {
					lowestLevelId = memLevelId;
					lowestGrade = grade;
				}
			
				if (levelId == lowestLevelId) {
					return levelId;
				}
			}
		}
		return 0;
	}
	/**
	 * 验证是否是最高等级
	 * 
	 * @param levelId
	 * 			会员等级ID
	 * @param levelList
	 * 			会员等级列表
	 * @return boolean
	 * 			true: 是    false: 否
	 */
	private boolean isHighestLevel(int levelId, List<Map<String, Object>> levelList){
		if (null != levelList) {
			// 最高等级
			int highestLevelId = -1;
			// 最高级别
			int highestGrade = -1;
			for (int i = 0; i < levelList.size(); i++) {
				Map<String, Object> levelInfo = levelList.get(i);
				// 会员等级
				int memLevelId = Integer.parseInt(levelInfo.get("levelId").toString());
				// 级别
				int grade = Integer.parseInt(levelInfo.get("grade").toString());
				if (grade > highestGrade) {
					highestLevelId = memLevelId;
					highestGrade = grade;
				}
			}
			if (levelId == highestLevelId) {
				return true;
			}
		}
		return false;
	}
	
	private void setLevelInfo(List<Map<String, Object>> levelList,int curLevel){
		
		@SuppressWarnings("unused")
		int lowestLevleId = 10;
		
		int lowestGrade = 10;
		for (int i = 0; i < levelList.size(); i++) {
			Map<String, Object> levelInfo = levelList.get(i);
			// 会员等级
			int memLevelId = Integer.parseInt(levelInfo.get("levelId").toString());
			// 级别
			int grade = Integer.parseInt(levelInfo.get("grade").toString());
			if (memLevelId == curLevel) {
				curLevelCode = ConvertUtil.getString(levelInfo.get("levelCode"));
				curLevelName = ConvertUtil.getString(levelInfo.get("levelName"));
				break;
			}
			if (grade < lowestGrade) {
				lowestLevleId = memLevelId;
				lowestGrade = grade;
				curLevelCode = ConvertUtil.getString(levelInfo.get("levelCode"));
				curLevelName = ConvertUtil.getString(levelInfo.get("levelName"));
			}
		}
	}
//	
//	/**
//	 * 返回会员下一等级
//	 * @param levelGrade
//	 * @param levelList
//	 * @return
//	 */
//	private String nextLevelCode (int levelGrade, List<Map<String, Object>> levelList) {
//		Map<String, Object> levelMap = new HashMap<String, Object>();
//		Integer limitLevel = Integer.MAX_VALUE;
//		for(Map<String, Object> map :levelList){
//			int tempGrade = ConvertUtil.getInt(map.get("grade"));
//			if (tempGrade>levelGrade && tempGrade<limitLevel){
//				limitLevel = tempGrade;
//				levelMap = map;
//			}
//		}
//		return ConvertUtil.getString(levelMap.get("levelCode"));
//	}
}
