/*	
 * @(#)BINBEDRJON08_BL.java     1.0 2013/03/26	
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
package com.cherry.dr.jon.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.jon.service.BINBEDRJON08_Service;

/**
 * 建档处理执行 BL
 * 
 * @author hub
 * @version 1.0 2013.03.26
 */
public class BINBEDRJON08_BL implements CampRuleExec_IF{
	
	@Resource
	private BINBEDRJON08_Service binbedrjon08_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRJON08_BL.class.getName());
	
	/**
	 * 建档处理执行
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		// 是否发送等级MQ
		boolean sendFlag = true;
		// 会员卡号
		String memCode = campBaseDTO.getMemCode();
		// 会员卡号以字母F开头
		if (memCode.toUpperCase().startsWith("F")) {
			List<Map<String, Object>> levelList = campBaseDTO.getMemberLevels();
			if (null != levelList && !levelList.isEmpty()) {
				// 是否建档
				boolean execFlag = false;
				for (Map<String, Object> levelInfo : levelList) {
					// 会员等级ID
					int levelId = Integer.parseInt(String.valueOf(levelInfo.get("levelId")));
					if (levelId == campBaseDTO.getCurLevelId()) {
						// 芳香会员等级
						if ("WMLC002".equals(levelInfo.get("levelCode"))) {
							execFlag = true;
						}
						break;
					}
				}
				if (execFlag) {
					// 取得会员信息
					Map<String, Object> memberInfo = binbedrjon08_Service.getMemberInfo(campBaseDTO);
					if (null == memberInfo || memberInfo.isEmpty()) {
						// 会员建档处理失败
						String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00022, new String[]{memCode});
						logger.error(errMsg);
						// 会员信息无记录
						errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00001, new String[]{memCode});
						throw new CherryDRException(errMsg);
					}
					// 会员标识
					String memInfoRegFlg = String.valueOf(memberInfo.get("memInfoRegFlg"));
					// 假登录的会员
					if ("1".equals(memInfoRegFlg)) {
						// 手机号码
						String phoneNum = memCode.substring(1);
						// 手机号码格式不正确，不进行建档
						if (CherryChecker.isNullOrEmpty(phoneNum) || phoneNum.length() != 11) {
							// 手机号码格式不正确
							String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00023, new String[]{memCode});
							logger.error(errMsg);
							// 不下发等级履历
							campBaseDTO.getExtArgs().put(DroolsConstants.LEVEL_MQ_KBN, DroolsConstants.NO_LEVEL_MQ);
							return;
						}
						// 手机号码
						memberInfo.put("mobilePhone", phoneNum);
						if (!binbedrjon08_Service.checkUniquePhone(memberInfo)) {
							// 手机号码重复
							String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00024, new String[]{memCode});
							logger.error(errMsg);
							// 不下发等级履历
							campBaseDTO.getExtArgs().put(DroolsConstants.LEVEL_MQ_KBN, DroolsConstants.NO_LEVEL_MQ);
							return;
						}
						// 名称
						memberInfo.put("memName", memCode);
						// 电话号码
						memberInfo.put("telephone", phoneNum);
						// 建档会员
						memberInfo.put("memInfoRegFlg", 0);
						campBaseDTO.setMemRegFlg(0);
						// 版本号
						int version = Integer.parseInt(memberInfo.get("version").toString());
						// 版本号增长
						version++;
						memberInfo.put("version", version);
						// 会员信息ID
						memberInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
						// 更新会员信息表
						int result = binbedrjon08_Service.updateMemberInfo(memberInfo);
						if (0 == result) {
							// 会员建档处理失败
							String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00022, new String[]{memCode});
							logger.error(errMsg);
							// 更新会员信息表错误
							errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00003, new String[]{memCode});
							throw new CherryDRException(errMsg);
						}
						// 组织ID
						memberInfo.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
						// 品牌ID
						memberInfo.put("brandInfoId", campBaseDTO.getBrandInfoId());
						// 组织代码
						memberInfo.put("orgCode", campBaseDTO.getOrgCode());
						// 品牌代码
						memberInfo.put("brandCode", campBaseDTO.getBrandCode());
						// 会员号
						memberInfo.put("memCode", memCode);
						// 会员等级
						memberInfo.put("memLevel", "WMLC002");
						// 发送会员资料MQ消息
						binOLCM31_BL.sendMEMQMsg(memberInfo);
						// 发送所有等级履历
						campBaseDTO.getExtArgs().put(DroolsConstants.SEND_RECORDS_KBN, DroolsConstants.SEND_ALL_RECORDS);
					}
				} else {
					// 重算的场合
					if (campBaseDTO.getReCalcFlg() != DroolsConstants.RECALCFLG_1 
							|| 1 == campBaseDTO.getMemRegFlg()) {
						sendFlag = false;
					}
				}
			}
		}
		if (!sendFlag) {
			// 不下发等级履历
			campBaseDTO.getExtArgs().put(DroolsConstants.LEVEL_MQ_KBN, DroolsConstants.NO_LEVEL_MQ);
		}
	}
	
	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		// 会员卡号
		String memCode = campBaseDTO.getMemCode();
		// 等级处理区分
		String lelExecFlag = "0";
		if (memCode.startsWith("8") 
				|| memCode.startsWith("9") 
				|| memCode.toUpperCase().startsWith("F")) {
			lelExecFlag = "1";
		}
		campBaseDTO.getExtArgs().put("LELEXECFLAG", lelExecFlag);
	}

	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
