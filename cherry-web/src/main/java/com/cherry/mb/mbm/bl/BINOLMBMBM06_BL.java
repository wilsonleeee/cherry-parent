/*
 * @(#)BINOLMBMBM06_BL.java     1.0 2012.11.27
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
package com.cherry.mb.mbm.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM36_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM33_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.mb.mbm.service.BINOLMBMBM02_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM06_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mq.mes.bl.BINBEMQMES03_BL;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.service.BINBEMQMES03_Service;
import com.site.lookup.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 会员资料修改画面BL
 * 
 * @author WangCT
 * @version 1.0 2012.11.27
 */
public class BINOLMBMBM06_BL {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM06_BL.class);
	
	/** 会员详细画面Service */
	@Resource
	private BINOLMBMBM02_Service binOLMBMBM02_Service;
	
	/** 更新会员基本信息Service */
	@Resource
	private BINOLMBMBM06_Service binOLMBMBM06_Service;
	
	/** 会员检索画面共通Service **/
	@Resource
	private BINOLCM33_Service binOLCM33_Service;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	/** WebService共通BL */
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 会员信息修改履历管理BL */
	@Resource
	private BINOLCM36_BL binOLCM36_BL;
	
	/** 会员消息数据接收处理BL **/
	@Resource
	private BINBEMQMES03_BL binBEMQMES03_BL;
	
	/** 会员消息数据接收处理Service **/
	@Resource
	private BINBEMQMES03_Service binBEMQMES03_Service;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 规则处理共通接口 **/
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINOLCM02_BL binOLCM02_BL;
	/**
	 * 取得会员信息
	 * 
	 * @param map 检索条件
	 * @return 会员信息
	 * @throws Exception 
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception {
		
		// 查询会员基本信息
		Map<String, Object> memberInfoMap = binOLMBMBM02_Service.getMemberInfo(map);
		String birthYear = (String)memberInfoMap.get("birthYear");
		String birthDay = (String)memberInfoMap.get("birthDay");
		if(birthYear != null && !"".equals(birthYear) && birthDay != null && !"".equals(birthDay)) {
			memberInfoMap.put("birth", birthYear+"-"+birthDay.substring(0,2)+"-"+birthDay.substring(2,4));
		}
		// 最佳联络时间
		String connectTime = (String)memberInfoMap.get("connectTime");
		if(connectTime != null && !"".equals(connectTime)) {
			String[] connectTimes = connectTime.split(",");
			memberInfoMap.put("connectTime", connectTimes);
		}
		// 存在推荐会员的场合取得推荐会员的卡号
		Object referrerId = memberInfoMap.get("referrerId");
		if(referrerId != null && !"".equals(referrerId.toString())) {
			// 根据会员ID查询会员卡号
			String referrer = binOLMBMBM02_Service.getMemCode(memberInfoMap);
			if(referrer != null && !"".equals(referrer)) {
				memberInfoMap.put("referrer", referrer);
			}
		}
		// 查询会员持卡信息
		List<Map<String, Object>> memCardInfoList = binOLMBMBM02_Service.getMemCardInfoList(memberInfoMap);
		if(memCardInfoList != null && !memCardInfoList.isEmpty()) {
			boolean validFlag = false;
			for(int i = 0; i < memCardInfoList.size(); i++) {
				int cardValidFlag = (Integer)memCardInfoList.get(i).get("cardValidFlag");
				if(cardValidFlag == 1) {
					memberInfoMap.putAll(memCardInfoList.get(i));
					validFlag = true;
					break;
				}
			}
			if(!validFlag) {
				memberInfoMap.putAll(memCardInfoList.get(0));
			}
		}
		// 查询会员地址信息
		List<Map<String, Object>> memberAddressList = binOLMBMBM02_Service.getMemberAddress(memberInfoMap);
		if(memberAddressList != null && !memberAddressList.isEmpty()) {
			memberInfoMap.put("memberAddressInfo", memberAddressList.get(0));
		}
		// 查询会员问卷信息
		List<Map<String, Object>> memPaperList = binOLMBMBM02_Service.getMemPaperList(map);
		if(memPaperList != null && !memPaperList.isEmpty()) {
			for(int i = 0; i < memPaperList.size(); i++) {
				Map<String, Object> memPaperMap = memPaperList.get(i);
				String questionType = (String)memPaperMap.get("questionType");
				String answer = (String)memPaperMap.get("answer");
				if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(questionType)) {
					List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
					for(int j = 65; j <= 84; j++) {
						char ca = (char)j;
						String value = (String)memPaperMap.get("option"+ca);
						if(value != null && !"".equals(value)) {
							Map<String, Object> answerMap = new HashMap<String, Object>();
							answerMap.put("answer", value);
							if(answer != null && answer.equals(value)) {
								answerMap.put("checked", true);
							}
							answerList.add(answerMap);
						}
					}
					memPaperMap.put("answerList", answerList);
				} else if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(questionType)) {
					List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
					int x = 0;
					for(int j = 65; j <= 84; j++) {
						char ca = (char)j;
						String value = (String)memPaperMap.get("option"+ca);
						if(value != null && !"".equals(value)) {
							Map<String, Object> answerMap = new HashMap<String, Object>();
							answerMap.put("answer", value);
							if(answer != null && !"".equals(answer)) {
								if(answer.length() > x && "1".equals(answer.substring(x,x+1))) {
									answerMap.put("checked", true);
								}
							}
							answerList.add(answerMap);
							x++;
						}
					}
					memPaperMap.put("answerList", answerList);
				}
			}
			memberInfoMap.put("memPagerList", memPaperList);
		}
		return memberInfoMap;
	}
	
	/**
	 * 查询会员俱乐部信息
	 * 
	 * @param map 检索条件
	 * @return 会员俱乐部信息
	 */
	public Map<String, Object> getMemClubInfo(Map<String, Object> map) {
		Map<String, Object> resultMap = binOLMBMBM02_Service.getMemClubInfo(map);
		if (null != resultMap && !resultMap.isEmpty()) {
			// 存在推荐会员的场合取得推荐会员的卡号
			Object referrerId = resultMap.get("referrerIdClub");
			if(referrerId != null && !"".equals(referrerId.toString())) {
				resultMap.put("referrerId", referrerId);
				// 根据会员ID查询会员卡号
				String referrer = binOLMBMBM02_Service.getMemCode(resultMap);
				if(referrer != null && !"".equals(referrer)) {
					resultMap.put("referrerClub", referrer);
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 更新会员俱乐部信息
	 * 
	 * @param map 更新条件
	 */
	public void tran_updMemClubInfo(Map<String, Object> map) throws Exception {
		int clubLevelId = 0;
		String referrer = (String) map.get("referrerClub");
		String oldRefer = (String) map.get("clubReferIdOld");
		oldRefer = (null == oldRefer || "".equals(oldRefer))? null : oldRefer;
		// 根据会员卡号查询会员ID
		String referrerId = null;
		if (!CherryChecker.isNullOrEmpty(referrer)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
			paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			paramMap.put("memCode", referrer);
			// 根据会员卡号查询会员ID
			String referId = binOLCM33_Service.getMemberInfoId(paramMap);
			if(referId != null && !"".equals(referId)) {
				referrerId = referId;
			}
		}
		map.put("referrerId", referrerId);
		// 查询会员俱乐部扩展信息
		Map<String, Object> clubExtInfo = binOLMBMBM02_Service.getClubExtInfo(map);
		if (null != clubExtInfo && !clubExtInfo.isEmpty()) {
			clubLevelId = Integer.parseInt(clubExtInfo.get("clubLevelId").toString());
			String joinDay = (String) clubExtInfo.get("joinDay");
			String joinTime = (String) map.get("joinTimeClub");
			if (!CherryChecker.isNullOrEmpty(joinTime) && 
					!CherryChecker.isNullOrEmpty(joinDay) && 
					CherryChecker.compareDate(joinDay, joinTime) == 0) {
				map.put("joinTimeClub", clubExtInfo.get("joinTime"));
			}
		}
		if (0 == clubLevelId) {
			// 插入会员俱乐部扩展信息
			binOLMBMBM02_Service.addClubExtInfo(map);
		} else {
			map.put("clubLevelId", clubLevelId);
			// 更新会员俱乐部扩展信息
			binOLMBMBM02_Service.updateClubExtInfo(map);
		}
		//map.put("version", version);
		// 推荐人有变更
		if (null == oldRefer && null != referrerId || null != oldRefer && null == referrerId ||
				null != oldRefer &&  null != referrerId && oldRefer.equals(referrerId)) {
			String orgCode = (String)map.get("orgCode");
			String brandCode = (String)map.get("brandCode");
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
			if(campRuleExec != null) {
				binOLMBMBM11_BL.sendReCalcMQ(map);
			}
		}
		map.put("mzClubId", map.get("memberClubId"));
		// 发送会员扩展信息MQ消息
		binOLCM31_BL.sendAllMZMQMsg(map);
	}
	
	/**
	 * 更新会员基本信息
	 * 
	 * @param map 更新条件
	 */
	public void tran_updMemberInfo(Map<String, Object> map) throws Exception {
		
		String importMode = (String)map.get("importMode");
		// 非导入模式不进行加密处理（导入前已经进行过加密处理）
		if(importMode == null || !"1".equals(importMode)) {
			//会员信息加密 
			binOLMBMBM11_BL.memInfoEncrypt(map);
		}
		String sysDate = binOLMBMBM11_Service.getSYSDate();
		map.put("sysDate", sysDate);
		// 批量更新时，清空对应字段
		String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
		// 和老后台同步会员信息方式
		String synMemMode = (String)map.get("synMemMode");
		// 取得更新前会员信息
		Map<String, Object> oldMemInfo = binOLCM36_BL.getMemberInfo(map);

		// 添加会员入会时间，完善度程序需要
		if(!CollectionUtils.isEmpty(oldMemInfo)) {
			map.put("joinDate", oldMemInfo.get("joinDate"));
		}

		String birth = (String)map.get("birth");
		if(birth != null && !birth.isEmpty()) {
			if(cherryclear.equals(birth)){
				map.put("birthYear", cherryclear);
				map.put("birthDay", cherryclear);
			}else{
				map.put("birthYear", birth.substring(0,4));
				map.put("birthDay", birth.substring(5,7)+birth.substring(8, 10));
			}
		}
		// 激活状态
		String active = (String)map.get("active");
		// 原激活状态
		String activeOld = (String)map.get("activeOld");
		if(active != null && "1".equals(active)) {
			if(activeOld == null || "0".equals(activeOld)) {
				String activeDate = (String)map.get("activeDate");
				if(activeDate == null || "".equals(activeDate)) {
					map.put("activeDate", sysDate);
				}
			}
		}
		Object _referrerId = map.get("referrerId");
		if(_referrerId == null || "".equals(_referrerId)) {
			// 设置推荐会员ID
			String referrer = (String)map.get("referrer");
			if(referrer != null && !"".equals(referrer)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
				paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				paramMap.put("memCode", referrer);
				if(cherryclear.equals(referrer)){
					map.put("referrerId", cherryclear);
				}else{
					// 根据会员卡号查询会员ID
					String referrerId = binOLCM33_Service.getMemberInfoId(paramMap);
					if(referrerId != null && !"".equals(referrerId)) {
						map.put("referrerId", referrerId);
					}
				}
			}
		}
		
		String testType = (String)map.get("testType");
		if(testType == null || "".equals(testType)) {
			// 发卡柜台测试区分
			String counterKind = (String)map.get("counterKind");
			// 发卡柜台为测试柜台的场合设定该会员为测试会员
			if(counterKind != null && "1".equals(counterKind)) {
				map.put("testType", "0");
			} else {
				map.put("testType", "1");
			}
		}
		
		// 最佳联络时间
		List<String> connectTimeList = (List)map.get("connectTime");
		if(connectTimeList != null && !connectTimeList.isEmpty()) {
			String connectTime = "";
			for(int i = 0; i < connectTimeList.size(); i++) {
				if(i == 0) {
					connectTime = connectTimeList.get(i);
				} else {
					connectTime += "," + connectTimeList.get(i);
				}
			}
			map.put("connectTime", connectTime);
		}
		// 版本号
		String version = (String)map.get("version");
		if(version != null && !"".equals(version)) {
			map.put("version", Integer.parseInt(version)+1);
		} else {
			map.put("version", 1);
		}
		//会员通，添加相应参数
		binOLCM02_BL.addTmallMixMobile(map,"mobilePhone",2);
		// 更新会员基本信息
		int result = binOLMBMBM06_Service.updMemberInfo(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		
		// 换卡的场合如果存在两个不同的会员ID需要合并处理，该map设置合并处理的参数
		Map<String, Object> mergeMemMap = new HashMap<String, Object>();
		// 重算时间
		String reCalcDate = null;
		// 是否换卡
		boolean isChangeMemCode = false;
		// 新卡号
		String memCode = (String)map.get("memCode");
		// 老卡号
		String memCodeOld = (String)map.get("memCodeOld");
		// 新卡号和老卡号不相同进行换卡处理
		if(!memCode.equals(memCodeOld)) {
			isChangeMemCode = true;
			// 会员老卡停用处理
			binOLMBMBM06_Service.delMemCardInfo(map);
			String newMemberInfoId = (String)map.get("newMemberInfoId");
			if(newMemberInfoId != null && !"".equals(newMemberInfoId)) {
				String memberInfoId = (String)map.get("memberInfoId");
				if(memberInfoId.equals(newMemberInfoId)) {
					// 启用会员卡信息
					binOLMBMBM06_Service.updMemCardValidFlag(map);
				} else {
					// 老会员ID
					mergeMemMap.put("memberInfoID", map.get("memberInfoId"));
					// 新会员ID
					mergeMemMap.put("newMemberInfoID", newMemberInfoId);
					// 老卡回目数
					mergeMemMap.put("cardCount", map.get("cardCount"));
					// 地址ID
					mergeMemMap.put("newAddressInfoID", map.get("newAddressInfoId"));
					// 组织ID
					mergeMemMap.put("organizationInfoID", map.get("organizationInfoId"));
					// 品牌ID
					mergeMemMap.put("brandInfoID", map.get("brandInfoId"));
					// 组织代号
					mergeMemMap.put("orgCode", map.get("orgCode"));
					// 品牌代码
					mergeMemMap.put("brandCode", map.get("brandCode"));
					// 会员卡号
					mergeMemMap.put("newMemcode", map.get("memCode"));
					// 相同会员合并处理
					reCalcDate = binBEMQMES03_BL.mergeMember(mergeMemMap);
					// 系统时间作为换卡时间
					map.put("grantDate", sysDate.substring(0,4)+sysDate.substring(5,7)+sysDate.substring(8,10));
					// 更新会员卡信息
					binOLMBMBM06_Service.updMemCardInfo(map);
				}
			} else {
				// 换卡后新卡的卡回目数为老卡的卡回目数加1
				int cardCount = Integer.parseInt(map.get("cardCount").toString());
				map.put("cardCount", cardCount+1);
				// 系统时间作为换卡时间
				map.put("grantDate", sysDate.substring(0,4)+sysDate.substring(5,7)+sysDate.substring(8,10));
				// 发卡BA作为领卡BA
				map.put("memCodeEmpCode", map.get("employeeCode"));
				// 发卡柜台作为领卡柜台
				map.put("memCodeOrgCode", map.get("organizationCode"));
				// 添加会员持卡信息
				binOLMBMBM11_Service.addMemCardInfo(map);
			}
			// 发送会员扩展信息MQ消息(全部记录)
			binOLCM31_BL.sendAllMZMQMsg(map);
		}
		
		// 地址ID
		String addressInfoId = (String)map.get("addressInfoId");
		// 省ID
		String provinceId = (String)map.get("provinceId");
		// 城市ID
		String cityId = (String)map.get("cityId");
		// 区县ID
		String countyId = (String)map.get("countyId");
		// 地址
		String address = (String)map.get("address");
		// 邮编
		String postcode = (String)map.get("postcode");
		// 存在地址ID的场合，更新地址信息，否则添加地址
		if(addressInfoId != null && !"".equals(addressInfoId)) {
			// 省ID、城市ID、地址、邮编有一项不为空的场合更新会员地址信息
			if((provinceId != null && !"".equals(provinceId)) 
					|| (cityId != null && !"".equals(cityId)) 
					|| (address != null && !"".equals(address)) 
					|| (postcode != null && !"".equals(postcode))) {
				// 更新会员地址信息
				binOLMBMBM06_Service.updAddressInfo(map);
			} else {
				if(CherryChecker.isNullOrEmpty(synMemMode)){
					// 删除会员地址信息
					binOLMBMBM06_Service.delAddressInfo(map);
					// 删除会员地址关系
					binOLMBMBM06_Service.delMemberAddress(map);
				}
			}
		} else {
			// 省ID、城市ID、地址、邮编有一项不为空的场合添加会员地址信息
			if((provinceId != null && !"".equals(provinceId)) 
					|| (cityId != null && !"".equals(cityId)) 
					|| (address != null && !"".equals(address)) 
					|| (postcode != null && !"".equals(postcode))) {
					if(cherryclear.equals(provinceId)){
						map.put("provinceId", "");
					}
					if(cherryclear.equals(cityId)){
						map.put("cityId", "");
					}
					if(cherryclear.equals(countyId)){
						map.put("countyId", "");
					}
					if(cherryclear.equals(address)){
						map.put("address", "");
					}
					if(cherryclear.equals(postcode)){
						map.put("postcode", "");
					}
				// 添加地址信息
				int _addressInfoId = binOLMBMBM11_Service.addAddressInfo(map);
				map.put("addressInfoId", _addressInfoId);
				// 添加会员地址
				binOLMBMBM11_Service.addMemberAddress(map);
			}
		}
		
		// 更新会员问卷信息处理
		this.updatePaperAnswer(map);
		
		// 初始累计金额
		String initTotalAmount = (String)map.get("initTotalAmount");
		// 变更前初始累计金额
		String initTotalAmountOld = (String)map.get("initTotalAmountOld");
		// 初始累计金额是否变更
		boolean initTotalAmountFlag = false;
		if(initTotalAmount != null && !"".equals(initTotalAmount)) {
			if(cherryclear.equals(initTotalAmount)){
				initTotalAmountFlag = true;
			}else{
				if(initTotalAmountOld != null && !"".equals(initTotalAmountOld)) {
					if(Double.parseDouble(initTotalAmount)!=Double.parseDouble(initTotalAmountOld)) {
						initTotalAmountFlag = true;
					}
				} else {
					initTotalAmountFlag = true;
				}
			}
		} else {
			if(initTotalAmountOld != null && !"".equals(initTotalAmountOld)) {
				if(CherryChecker.isNullOrEmpty(synMemMode)) {
					initTotalAmountFlag = true;
				}
			}
		}
		//更新会员信息扩展表信息
		int updateCount =binOLMBMBM06_Service.updMemberExtInfoMain(map);
		if(updateCount == 0) {
			// 添加会员扩展信息
			binOLMBMBM11_Service.addMemberExtInfoMain(map);
		}

		// 初始累计金额变更的场合，更新初始累计金额
		if(initTotalAmountFlag) {
			// 更新会员扩展信息
			int updCount = binOLMBMBM06_Service.updMemberExtInfo(map);
			if(updCount == 0) {
				// 初始累计金额不为空的场合，把初始累计金额添加到会员扩展属性中
				if(initTotalAmount != null && !"".equals(initTotalAmount)) {
					if(cherryclear.equals(initTotalAmount)){
						map.put("initTotalAmount", "0.00");
					}
					// 添加会员扩展信息
					binOLMBMBM11_Service.addMemberExtInfo(map);
				}
			}
		}
		String status = (String)map.get("status");
		if(status != null && "0".equals(status)) {
			oldMemInfo.put("memCode", memCodeOld);
		}
		if(synMemMode != null && "1".equals(synMemMode)) {
			this.getMemInfoParam(map,oldMemInfo);
		}
		// 添加会员信息修改履历
		this.addMemRecord(map, oldMemInfo);
		
		// 无效会员更新的场合只做会员资料更新处理，不做下面的处理
		if(status != null && "0".equals(status)) {
			return;
		}
		// 获取系统配置项【1297】维护会员信息是否同步到老后台的值
		int organizationInfoId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandInfoId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		if(binOLCM14_BL.isConfigOpen("1297", organizationInfoId, brandInfoId)) {
			
			/**
			 * 需要发MQ与老后台同步数据时，增加了会员信息登记区分字段
			 * 
			 */
			// 非换卡的场合才发送MQ给老后台同步会员信息
			if(!isChangeMemCode) {
				// 通过会员卡号查询会员信息
//				Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
//				if(memberInfoMap != null && !memberInfoMap.isEmpty()) {
//					// 会员信息登记区分
//					String memInfoRegFlg = ConvertUtil.getString(memberInfoMap.get("memInfoRegFlg"));
//					// 登记区分字段为空，默认为"0"
//					map.put("memInfoRegFlg", ("".equals(memInfoRegFlg) ? "0" : memInfoRegFlg));
//				} else {
//					// 会员信息登记区分
//					map.put("memInfoRegFlg", "1");
//				}
				// 在新增和更新会员的时候下发会员资料肯定是非假登录的
				map.put("memInfoRegFlg", "0");
			}

			// 会员完善度信息填充
			map.put("income", map.get("income") == null ? "" : map.get("income"));
			map.put("returnVisit", map.get("returnVisit") == null ? "" : map.get("returnVisit"));
			map.put("skinType", map.get("skinType") == null ? "" : map.get("skinType"));
			map.put("profession", map.get("profession") == null ? "" : map.get("profession"));

			// MQ形式和老后台同步会员信息的场合
			if(synMemMode != null && "1".equals(synMemMode)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.putAll(map);
				paramMap.put("subType", "1");
				// 发送会员资料MQ消息和老后台同步会员信息
				binOLMBMBM11_BL.sendMemberMQ(paramMap);
			} else {
				// 换卡的场合
				if(isChangeMemCode) {
					// 调用WebService和老后台同步会员信息
					this.synMemberInfo(map);
				} else {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.putAll(map);
					paramMap.put("subType", "1");
					// 发送会员资料MQ消息和老后台同步会员信息
					binOLMBMBM11_BL.sendMemberMQ(paramMap);
				}
			}
		}
		
		// 是否进行过重算
		boolean noRecalcFalg = true;
		// 存在重算时间需要进行重算处理
		if(reCalcDate != null && !"".equals(reCalcDate)) {
			mergeMemMap.put("reCalcDate", reCalcDate);
			mergeMemMap.put("reCalcType", "0");
			// 插入重算信息表
			binBEMQMES03_Service.insertReCalcInfo(mergeMemMap);
			// 发送MQ重算消息进行实时重算
			binBEMQMES03_BL.sendReCalcMsg(mergeMemMap);
			noRecalcFalg = false;
		}
		// 生日变更或者推荐会员变更是否进行重算
		boolean isRecFlag = false;
		boolean birthChange = false;
		String birthOld = (String)oldMemInfo.get("birthDay");
		String birthDay = (String)map.get("birthDay");
		// 生日变更的场合
		if((birthDay != null && !birthDay.equals(birthOld)) 
				|| (birthOld != null && !birthOld.equals(birthDay))) {
			birthChange = true;
			String orgCode = (String)map.get("orgCode");
			String brandCode = (String)map.get("brandCode");
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
			if(campRuleExec != null) {
				isRecFlag = true;
			}
		}
		// 生日没有变更再看推荐会员是否变更
		if(!isRecFlag) {
			// 原推荐会员ID
			String referrerIdOld = (String)map.get("referrerIdOld");
			// 新推荐会员ID
			String referrerId = (String)map.get("referrerId");
			// 推荐会员发生变更的场合
			if((referrerId != null && !referrerId.equals(referrerIdOld)) 
					|| (referrerIdOld != null && !referrerIdOld.equals(referrerId))) {
				String orgCode = (String)map.get("orgCode");
				String brandCode = (String)map.get("brandCode");
				CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
				if(campRuleExec != null) {
					isRecFlag = true;
				}
			}
		}
		// 初始累计金额发生变化的场合需要进行重算
		if(initTotalAmountFlag) {
			isRecFlag = true;
		}
		if(isRecFlag) {
			// 发送MQ重算消息进行实时重算
			noRecalcFalg = binOLMBMBM11_BL.sendReCalcMQ(map);
		}
		
		// 换卡的场合
		if(isChangeMemCode) {
			map.put("cardChangeTime", sysDate.substring(0, 19));
			map.put("subType", "2");
			String orgCode = (String)map.get("orgCode");
			String brandCode = (String)map.get("brandCode");
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT02);
			if(campRuleExec != null) {
				// 需要进行换卡扣积分处理
				map.put("isMBPointExec", "1");
			}
			if(noRecalcFalg) {
				// 需要下发会员等级
				map.put("isMBRuleExec", "1");
			}
			if("1".equals(map.get("isMBRuleExec")) || "1".equals(map.get("isMBPointExec"))) {
				// 发送规则处理MQ消息
				binOLMBMBM11_BL.sendRuleMQ(map);
			}
		}

		// 获取系统配置最小完善度处理时间
		String minJoinDate = binOLCM14_BL.getConfigValue("1402", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));

		if(!StringUtils.isEmpty(minJoinDate)) { // 若未开启最小入会时间直接跳过完善度功能
			String joinDate = String.valueOf(map.get("joinDate")); // 会员入会时间
			if (joinDate != null && DateUtil.compareDate(joinDate, minJoinDate) >= 0) {
				// 更新会员完整度
				updMemberInfoComplete(map);
			}
		}
		binOLMBMBM06_Service.manualCommit();
		
		try {
			// 生日变更的场合
			if(birthChange) {
				// 非导入模式的场合
				if(importMode == null || !"1".equals(importMode)) {
					int orgId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
					int brandId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
					String orgCode = String.valueOf(map.get("orgCode"));
					String brandCode = String.valueOf(map.get("brandCode"));
					String memId = ConvertUtil.getString(map.get("memberInfoId"));
					com05IF.makeOrderMQ(orgId,brandId,orgCode,brandCode, Integer.parseInt(memId), "BIR");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 跟新会员完整度
	 *
	 */
	public void updMemberInfoComplete(Map<String, Object> map) throws Exception {
		// 组织id
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌id
		String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		// 会员卡号
		String memCode = ConvertUtil.getString(map.get("memCode"));

		List<String> keys = binOLCM31_BL.getMemCompleteRuleKey(organizationInfoId,brandInfoId);
		if(CollectionUtils.isEmpty(keys)) { // 若不存在规则直接跳过完善度功能
			return ;
		}

		// 获取所有规则设置属性的值
		Map<String,Object> params = new HashMap<String, Object>();
		for (String key : keys) {
			params.put(key, map.get(key));
		}

		// 获取会员最新百分比并更新会员百分比信息
		int percent = binOLCM31_BL.calMemCompletePercent(organizationInfoId, brandInfoId, params);
		binOLCM31_BL.updateCompletePercentByMemcode(organizationInfoId, brandInfoId, memCode , percent);

		// 获取会员最新积分并更新会员积分信息
		Map<String,Object> pointResult = binOLCM31_BL.calMemCompletePoint(organizationInfoId, brandInfoId, params, memCode);
		binOLCM31_BL.updateCompletePointByMemcode(organizationInfoId, brandInfoId, memCode, ConvertUtil.getInt(pointResult.get("pointTotal")), ConvertUtil.getString(pointResult.get("awardPoint")));

		int pointTotal = ConvertUtil.getInt(pointResult.get("pointTotal"));
		if(pointTotal != 0) { // 赠送积分不为0发送MQ同步会员积分
			map.put("ModifyPoint", pointTotal);
			binOLCM31_BL.sendPointsMQ(getPointMQParam(map));
		}

	}

	// 获取发送积分MQ参数
	private Map<String,Object> getPointMQParam(Map<String,Object> params) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.putAll(params);
		returnMap.put("MemberCode",params.get("memCode"));
		returnMap.put("BusinessTime", CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN));

		returnMap.put("Reason","会员积分完善度积分修改");

		Map<String, Object> comEmployeeInfo = binOLCM31_BL.getComEmployeeInfoById(params);
		if(!CollectionUtils.isEmpty(comEmployeeInfo)) {
			returnMap.put("EmployeeCode",comEmployeeInfo.get("employeeCode"));
		}
		returnMap.put("pointType","2"); // 1-总值维护，2-差值维护
		returnMap.put("MaintainType","1");
		return returnMap;
	}
	
	/**
	 * 更新会员问卷信息处理
	 * 
	 * @param map 会员信息
	 */
	public void updatePaperAnswer(Map<String, Object> map) {
		
		// 取得会员扩展属性
		List propertyInfoList = (List)map.get("propertyInfoList");
		if(propertyInfoList != null && !propertyInfoList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key = {"paperId"};
			keyList.add(key);
			ConvertUtil.convertList2DeepList(propertyInfoList,list,keyList,0);
			// 需要删除的会员答卷List
			List<Map<String, Object>> paperAnswerDelList = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> propertyMap = list.get(i);
				map.put("paperId", propertyMap.get("paperId"));
				// 查询会员答卷信息
				String paperAnswerId = binOLMBMBM06_Service.getPaperAnswerId(map);
				
				List<Map<String, Object>> propertyDetailList = (List)propertyMap.get("list");
				for(int j = 0; j < propertyDetailList.size(); j++) {
					Map<String, Object> propertyDetailMap = propertyDetailList.get(j);
					List<String> propertyValues = (List)propertyDetailMap.get("propertyValues");
					if(propertyValues == null || propertyValues.isEmpty() || "".equals(propertyValues.get(0))) {
						propertyDetailList.remove(j);
						j--;
						continue;
					}
				}
				// 答卷明细为空的场合，如果存在原答卷信息需要删除
				if(propertyDetailList.isEmpty()) {
					if(paperAnswerId != null && !"".equals(paperAnswerId)) {
						Map<String, Object> paperAnswerDelMap = new HashMap<String, Object>();
						paperAnswerDelMap.put("paperAnswerId", paperAnswerId);
						paperAnswerDelList.add(paperAnswerDelMap);
					}
				} else {
					// 不存在答卷信息的场合，添加答卷信息
					if(paperAnswerId == null || "".equals(paperAnswerId)) {
						// 添加答卷
						paperAnswerId = String.valueOf(binOLMBMBM11_Service.addPaperAnswer(map));
					} else {
						List<Map<String, Object>> delParamList = new ArrayList<Map<String,Object>>();
						Map<String, Object> delParam = new HashMap<String, Object>();
						delParam.put("paperAnswerId", paperAnswerId);
						delParamList.add(delParam);
						// 删除答卷明细信息
						binOLMBMBM06_Service.delPaperAnswerDetail(delParamList);
					}
					for(int j = 0; j < propertyDetailList.size(); j++) {
						Map<String, Object> propertyDetailMap = propertyDetailList.get(j);
						propertyDetailMap.put("paperAnswerId", paperAnswerId);
						List<String> propertyValues = (List)propertyDetailMap.get("propertyValues");
						String propertyType = (String)propertyDetailMap.get("propertyType");
						if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(propertyType)) {
							String answer = "";
							for(int x = 1; x <= 20; x++) {
								if(propertyValues.contains(String.valueOf(x))) {
									answer += "1";
								} else {
									answer += "0";
								}
							}
							propertyDetailMap.put("answer", answer);
						} else {
							propertyDetailMap.put("answer", propertyValues.get(0));
						}
					}
					// 添加答卷明细
					binOLMBMBM11_Service.addPaperAnswerDetail(propertyDetailList);
				}
			}
			if(!paperAnswerDelList.isEmpty()) {
				// 删除答卷信息
				binOLMBMBM06_Service.delPaperAnswer(paperAnswerDelList);
				// 删除答卷明细信息
				binOLMBMBM06_Service.delPaperAnswerDetail(paperAnswerDelList);
			}
		}
	}
	
	/**
	 * 添加会员信息修改履历
	 * 
	 * @param map 会员信息
	 * @param oldMemInfo 变更前会员信息
	 */
	public void addMemRecord(Map<String, Object> map, Map<String, Object> oldMemInfo) {
		
		Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
		memInfoRecordMap.put("oldMemInfo", oldMemInfo);
		memInfoRecordMap.put("memberInfoId", map.get("memberInfoId"));
		memInfoRecordMap.put("memCode", map.get("memCode"));
		memInfoRecordMap.put("organizationInfoId", map.get("organizationInfoId"));
		memInfoRecordMap.put("brandInfoId", map.get("brandInfoId"));
		memInfoRecordMap.put("modifyTime", map.get("sysDate"));
		// 新卡号
		String memCode = (String)map.get("memCode");
		// 老卡号
		String memCodeOld = (String)map.get("memCodeOld");
		// 是否换卡
		boolean changeCodeFlag = false;
		// 新卡号和老卡号不相同表示换卡
		if(!memCode.equals(memCodeOld)) {
			changeCodeFlag = true;
		}
		if(changeCodeFlag) {
			memInfoRecordMap.put("modifyType", "2");
		} else {
			memInfoRecordMap.put("modifyType", "1");
		}
		memInfoRecordMap.put("sourse", "Cherry");
		memInfoRecordMap.put("version", map.get("version"));
		String oldMemo1 = (String)oldMemInfo.get("memo1");
		String newMemo1 = (String)map.get("memo1");
		String oldMemo2 = (String)oldMemInfo.get("memo2");
		String newMemo2 = (String)map.get("memo2");
		String remark = null;
		if(newMemo1 != null && !"".equals(newMemo1) && !newMemo1.equals(oldMemo1)) {
			remark = newMemo1;
		}
		if(newMemo2 != null && !"".equals(newMemo2) && !newMemo2.equals(oldMemo2)) {
			if(remark != null && !"".equals(remark)) {
				remark = remark + CherryConstants.MESSAGE_SPLIT_FULL_COMMA + newMemo2;
			} else {
				remark = newMemo2;
			}
		}
		memInfoRecordMap.put("remark", remark);
		memInfoRecordMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		memInfoRecordMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		memInfoRecordMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		memInfoRecordMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		memInfoRecordMap.put("modifyEmployee", map.get("modifyEmployee"));
		memInfoRecordMap.put("modifyCounter", map.get("modifyCounter"));
		
		memInfoRecordMap.put("name", map.get("memName"));
//		memInfoRecordMap.put("nickname", map.get("nickname"));
//		memInfoRecordMap.put("creditRating", map.get("creditRating"));
		memInfoRecordMap.put("telephone", map.get("telephone"));
		memInfoRecordMap.put("mobilePhone", map.get("mobilePhone"));
		memInfoRecordMap.put("gender", map.get("gender"));
		memInfoRecordMap.put("birthYear", map.get("birthYear"));
		memInfoRecordMap.put("birthDay", map.get("birthDay"));
		memInfoRecordMap.put("email", map.get("email"));
		memInfoRecordMap.put("employeeId", map.get("employeeId"));
		memInfoRecordMap.put("organizationId", map.get("organizationId"));
		memInfoRecordMap.put("joinDate", map.get("joinDate"));
		memInfoRecordMap.put("referrerId", map.get("referrerId"));
		memInfoRecordMap.put("birthYearGetType", oldMemInfo.get("birthYearGetType"));
		memInfoRecordMap.put("blogId", map.get("blogId"));
		memInfoRecordMap.put("messageId", map.get("messageId"));
		memInfoRecordMap.put("identityCard", map.get("identityCard"));
		memInfoRecordMap.put("maritalStatus", map.get("maritalStatus"));
		memInfoRecordMap.put("active", map.get("active"));
		memInfoRecordMap.put("isReceiveMsg", map.get("isReceiveMsg"));
		memInfoRecordMap.put("profession", map.get("profession"));
		memInfoRecordMap.put("connectTime", map.get("connectTime"));
		memInfoRecordMap.put("memType", map.get("memType"));
		memInfoRecordMap.put("provinceId", map.get("provinceId"));
		memInfoRecordMap.put("cityId", map.get("cityId"));
		memInfoRecordMap.put("address", map.get("address"));
		memInfoRecordMap.put("zipCode", map.get("postcode"));
		memInfoRecordMap.put("initTotalAmount", map.get("initTotalAmount"));
		memInfoRecordMap.put("channelCode", map.get("channelCode"));
		memInfoRecordMap.put("skinType", map.get("skinType"));
		memInfoRecordMap.put("returnVisit", map.get("returnVisit"));
		memInfoRecordMap.put("income", map.get("income"));
		// 添加会员信息修改履历
		binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
	}
	
	/**
	 * 调用WebService和老后台同步会员信息
	 * 
	 * @param map 会员信息
	 */
	public void synMemberInfo(Map<String, Object> map) throws Exception {
		
		// 新卡号
		String memCode = (String)map.get("memCode");
		// 老卡号
		String memCodeOld = (String)map.get("memCodeOld");
		// 是否换卡
		boolean changeCodeFlag = false;
		// 新卡号和老卡号不相同表示换卡
		if(!memCode.equals(memCodeOld)) {
			changeCodeFlag = true;
		}
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		// 品牌代码
		memberInfoMap.put("BrandCode", map.get(CherryConstants.BRAND_CODE));
		// 业务类型
		memberInfoMap.put("BussinessType", "memberinfo");
		if(changeCodeFlag) {
			// 子类型
			memberInfoMap.put("SubType", "2");
		} else {
			// 子类型
			memberInfoMap.put("SubType", "1");
		}
		// 版本号
		memberInfoMap.put("Version", "1.0");
		List<Map<String, Object>> detailList = new ArrayList<Map<String,Object>>();
		Map<String, Object> detailMap = new HashMap<String, Object>();
		if(changeCodeFlag) {
			// 会员新卡号
			detailMap.put("NewMemcode", memCode);
			// 会员换卡时间
			detailMap.put("MemChangeTime", map.get("grantDate"));
		}
		// 会员卡号
		detailMap.put("MemberCode", memCodeOld);
		// 姓名
		detailMap.put("MemName", map.get("memName"));
		// 电话
		detailMap.put("MemPhone", map.get("telephone"));
		// 手机
		detailMap.put("MemMobile", map.get("mobilePhone"));
		// 性别
		detailMap.put("MemSex", map.get("gender"));
		// 省份
		Object provinceId = map.get("provinceId");
		if(provinceId != null && !"".equals(provinceId.toString())) {
			map.put("regionId", provinceId);
			String regionName = binOLMBMBM11_Service.getRegionName(map);
			detailMap.put("MemProvince", regionName);
		}
		// 城市
		Object cityId = map.get("cityId");
		if(cityId != null && !"".equals(cityId.toString())) {
			map.put("regionId", cityId);
			String regionName = binOLMBMBM11_Service.getRegionName(map);
			detailMap.put("MemCity", regionName);
		}
		// 地址
		detailMap.put("MemAddress", map.get("address"));
		// 邮编
		detailMap.put("MemPostcode", map.get("postcode"));
		// 生日
		String birth = (String)map.get("birth");
		if(birth != null && !"".equals(birth)) {
			detailMap.put("MemBirthday", birth.substring(0,4)+birth.substring(5,7)+birth.substring(8, 10));
		}
		// 邮箱
		detailMap.put("MemMail", map.get("email"));
		// 开卡时间
		String joinDate = (String)map.get("joinDate");
		detailMap.put("MemGranddate", joinDate.substring(0,4)+joinDate.substring(5,7)+joinDate.substring(8, 10));
		// 发卡BA
		detailMap.put("BAcode", map.get("employeeCode"));
		// 发卡柜台
		detailMap.put("CardCounter", map.get("organizationCode"));
		// 推荐会员卡号
		detailMap.put("Referrer", map.get("referrer"));
		// 是否愿意接收短信
		detailMap.put("IsReceiveMsg", map.get("isReceiveMsg"));
		// 是否测试会员
		detailMap.put("TestMemFlag", map.get("testType"));
		// 版本号
		detailMap.put("Version", map.get("version"));
		// 职业
		detailMap.put("profession", map.get("profession"));
		// 收入
		detailMap.put("income", map.get("income"));
		// 回访方式
		detailMap.put("returnVisit", map.get("returnVisit"));
		// 肤质
		detailMap.put("skinType", map.get("skinType"));
		detailList.add(detailMap);
		// 会员信息
		memberInfoMap.put("DetailList", detailList);
		
		Map<String, Object> resultMap = binOLCM27_BL.accessWebService(memberInfoMap);
		String State = (String)resultMap.get("State");
		String Data = (String)resultMap.get("Data");
		if(State.equals("ERROR")) {
			CherryException CherryException = new CherryException("ECM00005");
			CherryException.setErrMessage(Data);
			throw CherryException;
		}
	}
	/**
	 * 取得会员对应更新参数
	 * @param map
	 * @param oldMemInfo
	 */
	public void getMemInfoParam(Map<String, Object> map,Map<String, Object> oldMemInfo){
		Map<String, Object> tempMap =new HashMap<String, Object>();
		//批量更新时，清空对应字段
		String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
		if(CherryChecker.isNullOrEmpty(map.get("initTotalAmount"))){
			map.put("initTotalAmount", oldMemInfo.get("initTotalAmount"));
		}else if(cherryclear.equals(map.get("initTotalAmount"))){
			map.put("initTotalAmount", "0.00");
		}
		if(CherryChecker.isNullOrEmpty(map.get("cityId"))){
			map.put("cityId", ConvertUtil.getString(oldMemInfo.get("cityId")));
		}else if(cherryclear.equals(map.get("cityId"))){
			map.put("cityId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("provinceId"))){
			map.put("provinceId", ConvertUtil.getString(oldMemInfo.get("provinceId")));
		}else if(cherryclear.equals(map.get("provinceId"))){
			map.put("provinceId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("countyId"))){
			map.put("countyId", ConvertUtil.getString(oldMemInfo.get("countyId")));
		}else if(cherryclear.equals(map.get("countyId"))){
			map.put("countyId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("employeeId"))){
			map.put("employeeId", ConvertUtil.getString(oldMemInfo.get("employeeId")));
			tempMap.put("employeeId", oldMemInfo.get("employeeId"));
			map.put("employeeCode",binOLMBMBM06_Service.getEmployeeCode(tempMap));
		}else if(cherryclear.equals(map.get("employeeId"))){
			map.put("employeeId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("organizationId"))){
			map.put("organizationId", oldMemInfo.get("organizationId"));
			tempMap.put("organizationId", oldMemInfo.get("organizationId"));
			map.put("organizationCode",binOLMBMBM06_Service.getOrganizationCode(tempMap));
		}else if(cherryclear.equals(map.get("organizationId"))){
			map.put("organizationId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("memName"))){
			map.put("memName", oldMemInfo.get("name"));
		}else if(cherryclear.equals(map.get("memName"))){
			map.put("memName", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("telephone"))){
			map.put("telephone", oldMemInfo.get("telephone"));
		}else if(cherryclear.equals(map.get("telephone"))){
			map.put("telephone", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("mobilePhone"))){
			map.put("mobilePhone", oldMemInfo.get("mobilePhone"));
		}else if(cherryclear.equals(map.get("mobilePhone"))){
			map.put("mobilePhone", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("referrer"))){//会员推荐会员卡号
			map.put("referrerId",ConvertUtil.getString(oldMemInfo.get("referrerId")));
			tempMap.put("memberInfoId", oldMemInfo.get("referrerId"));
			map.put("referrer",binOLMBMBM06_Service.getMemberCode(tempMap));
		}else if(cherryclear.equals(map.get("referrer"))){
			map.put("referrerId", CherryConstants.BLANK);
			map.put("referrer", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("gender"))){
			map.put("gender", oldMemInfo.get("gender"));
		}else if(cherryclear.equals(map.get("gender"))){
			map.put("gender", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("birthYear"))){
			map.put("birthYear", oldMemInfo.get("birthYear"));
		}else if(cherryclear.equals(map.get("birthYear"))){
			map.put("birthYear", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("birthDay"))){
			map.put("birthDay", oldMemInfo.get("birthDay"));
		}else if(cherryclear.equals(map.get("birthDay"))){
			map.put("birthDay", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("birth"))){
			String oldbirth= ConvertUtil.getString(oldMemInfo.get("birthYear"))+ConvertUtil.getString(oldMemInfo.get("birthDay"));
			if(!CherryChecker.isNullOrEmpty(oldbirth) && oldbirth.length() == 8){
				String membirth = oldbirth.substring(0, 4) + "-"
						+ oldbirth.substring(4, 6) + "-"
						+ oldbirth.substring(6, 8);
				map.put("birth", membirth);
			}else{
				map.put("birth", CherryConstants.BLANK);
			}
		}else if(cherryclear.equals(map.get("birth"))){
			map.put("birth", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("email"))){
			map.put("email", oldMemInfo.get("email"));
		}else if(cherryclear.equals(map.get("email"))){
			map.put("email", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("isReceiveMsg"))){
			map.put("isReceiveMsg", oldMemInfo.get("isReceiveMsg"));
		}else if(cherryclear.equals(map.get("isReceiveMsg"))){
			map.put("isReceiveMsg", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("postcode"))){
			map.put("postcode", oldMemInfo.get("zipCode"));
		}else if(cherryclear.equals(map.get("postcode"))){
			map.put("postcode",CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("address"))){
			map.put("address", oldMemInfo.get("address"));
		}else if(cherryclear.equals(map.get("address"))){
			map.put("address", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("memo1"))){
			map.put("memo1", oldMemInfo.get("memo1"));
		}else if(cherryclear.equals(map.get("memo1"))){
			map.put("memo1", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("messageId"))){
			map.put("messageId", oldMemInfo.get("messageId"));
		}else if(cherryclear.equals(map.get("messageId"))){
			map.put("messageId", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("joinDate"))){
			map.put("joinDate", oldMemInfo.get("joinDate"));
		}else if(cherryclear.equals(map.get("joinDate"))){
			map.put("joinDate", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("joinTime"))){
			map.put("joinTime", oldMemInfo.get("joinTime"));
		}else if(cherryclear.equals(map.get("joinTime"))){
			map.put("joinTime", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("memAgeGetMethod"))){
			map.put("memAgeGetMethod", oldMemInfo.get("memAgeGetMethod"));
		}else if(cherryclear.equals(map.get("memAgeGetMethod"))){
			map.put("memAgeGetMethod", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("active"))){
			map.put("active", oldMemInfo.get("active"));
		}else if(cherryclear.equals(map.get("active"))){
			map.put("active", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("memberPassword"))){
			map.put("memberPassword", oldMemInfo.get("memberPassword"));
		}else if(cherryclear.equals(map.get("memberPassword"))){
			map.put("memberPassword", CherryConstants.BLANK);
		}
		if(CherryChecker.isNullOrEmpty(map.get("channelCode"))){
			map.put("channelCode", oldMemInfo.get("channelCode"));
		}else if(cherryclear.equals(map.get("channelCode"))){
			map.put("channelCode", CherryConstants.BLANK);
		}
	}
	
	public void tran_updMemMobile(Map<String, Object> map) throws Exception {
		
		String birthYear = (String)map.get("birthYear");
		String birthDay = (String)map.get("birthDay");
		if(birthYear != null && !"".equals(birthYear) && birthDay != null && !"".equals(birthDay)) {
			map.put("birth", birthYear+"-"+birthDay.substring(0,2)+"-"+birthDay.substring(2,4));
		}
		// 存在推荐会员的场合取得推荐会员的卡号
		Object referrerId = map.get("referrerId");
		if(referrerId != null && !"".equals(referrerId.toString())) {
			// 根据会员ID查询会员卡号
			String referrer = binOLMBMBM02_Service.getMemCode(map);
			if(referrer != null && !"".equals(referrer)) {
				map.put("referrer", referrer);
			}
		}
		
		// 会员【手机号】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			String brandCode = (String)map.get(CherryConstants.BRAND_CODE);
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		// 会员老卡停用处理
		binOLMBMBM06_Service.delMemCardInfo(map);
		// 换卡后新卡的卡回目数为老卡的卡回目数加1
		int cardCount = Integer.parseInt(map.get("cardCount").toString());
		map.put("cardCount", cardCount+1);
		String sysDate = binOLMBMBM11_Service.getSYSDate();
		// 系统时间作为换卡时间
		map.put("grantDate", sysDate.substring(0,4)+sysDate.substring(5,7)+sysDate.substring(8,10));
		// 发卡BA作为领卡BA
		map.put("memCodeEmpCode", map.get("employeeCode"));
		// 发卡柜台作为领卡柜台
		map.put("memCodeOrgCode", map.get("organizationCode"));
		// 添加会员持卡信息
		binOLMBMBM11_Service.addMemCardInfo(map);
		
		// 版本号
		Object version = map.get("version");
		if(version != null && !"".equals(version.toString())) {
			map.put("version", Integer.parseInt(version.toString())+1);
		} else {
			map.put("version", 1);
		}
		//会员通，添加相应参数
		binOLCM02_BL.addTmallMixMobile(map,"mobilePhone",2);
		// 更新会员手机号
		binOLMBMBM06_Service.updMemberMobile(map); 
		
		// 获取系统配置项【1297】维护会员信息是否同步到老后台的值
		int organizationInfoId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		int brandInfoId = ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID));
		if(binOLCM14_BL.isConfigOpen("1297", organizationInfoId, brandInfoId)) {
			this.synMemberInfo(map);
		}
		
		Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
		memInfoRecordMap.put("memberInfoId", map.get("memberInfoId"));
		memInfoRecordMap.put("memCode", map.get("memCode"));
		memInfoRecordMap.put("organizationInfoId", map.get("organizationInfoId"));
		memInfoRecordMap.put("brandInfoId", map.get("brandInfoId"));
		memInfoRecordMap.put("modifyTime", sysDate);
		memInfoRecordMap.put("modifyType", "2");
		memInfoRecordMap.put("sourse", "Cherry");
		memInfoRecordMap.put("version", map.get("version"));
		memInfoRecordMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		memInfoRecordMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		memInfoRecordMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		memInfoRecordMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));

		memInfoRecordMap.put("mobilePhone", map.get("mobilePhone"));
		Map<String, Object> oldMemInfo = new HashMap<String, Object>();
		oldMemInfo.put("mobilePhone", map.get("oldMobilePhone"));
		oldMemInfo.put("memCode", map.get("memCodeOld"));
		memInfoRecordMap.put("oldMemInfo", oldMemInfo);
		// 添加会员信息修改履历
		binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
		// 换卡重发等级和积分
		map.put("cardChangeTime", sysDate.substring(0, 19));
		map.put("subType", "2");
		// 需要下发会员等级
		map.put("isMBRuleExec", "1");
		// 发送规则处理MQ消息
		binOLMBMBM11_BL.sendRuleMQ(map);
	}
}
