/*		
 * @(#)BINBEMQMES08_BL.java     1.0 2011/08/24		
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

package com.cherry.mq.mes.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM31_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES08_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 会员初始数据采集信息接收处理BL
 * 
 * @author WangCT
 *
 */
public class BINBEMQMES08_BL implements AnalyzeMemberInitDataMessage_IF {
	
	/** 会员初始数据采集信息接收处理Service **/
	@Resource
	private BINBEMQMES08_Service binBEMQMES08_Service;
	
	/** 取得各种业务类型的单据流水号 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 消息数据接收共通处理Service */
	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 规则处理 BL */
	@Resource
	private BINOLCM31_BL binOLCM31_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 会员初始数据采集信息接收处理
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void analyzeMemberInitData(Map<String, Object> map) throws Exception {
		
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
		this.setInsertInfoMapKey(map);
		// 设定数据来源
		String sourse = (String)map.get("sourse");
		// 系统时间
		String sysDate = binBEMQMES08_Service.getForwardSYSDate();
		
		// 取得会员初始数据明细数据
		List detailList = (List)map.get("detailDataDTOList");
		// 柜台号
		map.put("counterCode", ((Map)detailList.get(0)).get("countercode"));
		// BA卡号
		map.put("BAcode", ((Map)detailList.get(0)).get("bacode"));
		// 查询员工信息
		Map<String, Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(map);
		if(employeeInfo != null) {
			map.putAll(employeeInfo);
		}
		// 查询柜台部门信息
		Map<String, Object> counterInfo = binBEMQMES99_Service.selCounterDepartmentInfo(map);
		if(counterInfo != null) {
			map.putAll(counterInfo);
		}
		// 插入会员使用化妆次数积分主表
		int memUsedInfoId = binBEMQMES08_Service.addMemUsedInfo(map);
		// 循环处理会员初始数据明细数据
		for(int i = 0; i < detailList.size(); i++) {
			
			Map detailMap = (Map)detailList.get(i);
			// 主表ID
			detailMap.put("memUsedInfoId", memUsedInfoId);
			// 设置组织ID
			detailMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 设置品牌ID
			detailMap.put("brandInfoID", map.get("brandInfoID"));
			// 设置组织代码
			detailMap.put("orgCode", map.get("orgCode"));
			// 设置品牌代码
			detailMap.put("brandCode", map.get("brandCode"));
			// 设定子类型
			detailMap.put("subType", map.get("subType"));
			// 设定数据来源
			detailMap.put("channel", sourse);
			// 设定业务类型
			detailMap.put("tradeType", map.get("tradeType"));
			// 设定单据号
			detailMap.put("tradeNoIF", map.get("tradeNoIF"));
			// 设定重算次数为0
			detailMap.put("reCalcCount", "0");
			// 设定理由为初始数据导入
			detailMap.put("reason", "1");
			// 设定计算日期
			detailMap.put("calcDate", sysDate);
			this.setInsertInfoMapKey(detailMap);
			// 查询会员信息
			Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
			// 会员信息不存在的场合，表示接收失败，结束处理
			if(memberInfo == null) {
				MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_34);
			}
			detailMap.putAll(memberInfo);
			
			// 取得会员当前等级代码
			String member_level = (String)detailMap.get("member_level");
			// 取得当前拥有的化妆次数
			String curBtimes = (String)detailMap.get("curBtimes");
			// 取得会员当前有效积分
			String curPoints = (String)detailMap.get("curPoints");
			// 取得会员累计销售金额
			String totalAmounts = (String)detailMap.get("totalAmounts");
			
			// 会员当前等级代码存在的场合
			if(member_level != null && !"".equals(member_level)) {
				// 通过会员等级代码取得会员等级ID
				String memberLevelId = binBEMQMES08_Service.getMemberLevelID(detailMap);
				detailMap.put("memberLevelId", memberLevelId);
			}
			// 会员入会时间
			String joinDate = (String)detailMap.get("joinDate");
			if(joinDate != null && !"".equals(joinDate)) {
				detailMap.put("curJoinDate", joinDate);
				// 更新会员入会时间
				binBEMQMES08_Service.updateJoinDate(detailMap);
			} else {
				detailMap.remove("joinDate");
			}
			
			// 插入会员使用化妆次数积分明细表
			binBEMQMES08_Service.addMemUsedDetail(detailMap);
			
			String acquiTime = (String)detailMap.get("acquiTime");
			String initialDate = (String)detailMap.get("initialDate");
			// 业务日期比会员初始采集日期小的场合，不更新会员相关信息
			if(initialDate != null && acquiTime.substring(0,10).compareTo(initialDate) < 0) {
				continue;
			}
			
			// 会员初始数据明细中是否存在任意一项数据
			boolean isInitDataFlg = false;
			if((member_level != null && !"".equals(member_level)) 
					|| (curBtimes != null && !"".equals(curBtimes))
					|| (curPoints != null && !"".equals(curPoints))
					|| (totalAmounts != null && !"".equals(totalAmounts))) {
				isInitDataFlg = true;
			}
			// 是否需要重算
			boolean isReCalcFlag = false;
			if(isInitDataFlg) {
				// 是否需要重算
				isReCalcFlag = needReCalc(detailMap);
			} else {
				continue;
			}
			
			// 不需要重算时
			if(!isReCalcFlag) {
				// 会员信息ID
				int memberInfoId = Integer.parseInt(detailMap.get("memberInfoId").toString());
				if((curBtimes != null && !"".equals(curBtimes)) 
						|| (totalAmounts != null && !"".equals(totalAmounts))) {
					Map<String, Object> memberExtInfo = new HashMap<String, Object>();
					memberExtInfo.put("memberInfoId", detailMap.get("memberInfoId"));
					if(totalAmounts != null && !"".equals(totalAmounts)) {
						memberExtInfo.put("totalAmounts", totalAmounts);
					}
					if(curBtimes != null && !"".equals(curBtimes)) {
						memberExtInfo.put("curBtimes", curBtimes);
					}
					// 更新会员信息扩展表
					binOLCM31_BL.updateMemberExtInfo(memberExtInfo);
				}
				// 会员当前等级代码存在的场合
				if(member_level != null && !"".equals(member_level)) {
					// 设定履历区分为等级
					detailMap.put("recordKbn", "0");
					String levelStatus = (String)detailMap.get("levelStatus");
					// 取得变更前等级ID
					Object oldMemberLevelId = detailMap.get("memberLevel");
					if(levelStatus != null && "1".equals(levelStatus)) {
						oldMemberLevelId = 0;
					}
					// 会员当前等级ID
					String memberLevelId = (String)detailMap.get("memberLevelId");
					// 变更前等级
					detailMap.put("oldValue", oldMemberLevelId);
					// 设定更新后的值为会员当前等级ID
					detailMap.put("newValue", memberLevelId);
					// 变更前等级ID
					detailMap.put("oldMemberLevelId", oldMemberLevelId);
					// 等级有变化的场合，更新会员等级信息
					if(oldMemberLevelId == null || !oldMemberLevelId.toString().equals(memberLevelId)) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						// 变更前等级ID
						paramMap.put("memberLevelId", oldMemberLevelId);
						// 业务发生时间
						paramMap.put("acquiTime", detailMap.get("acquiTime"));
						// 变更前会员等级级别
						int oldMemLevelGrade = 0;
						// 取得变更前会员等级级别
						Integer grade = binBEMQMES08_Service.getMemLevelGrade(paramMap);
						if(grade != null) {
							oldMemLevelGrade = grade;
						}
						// 变更后等级ID
						paramMap.put("memberLevelId", memberLevelId);
						// 取得变更后会员等级级别
						int memLevelGrade = 0;
						grade = binBEMQMES08_Service.getMemLevelGrade(paramMap);
						if(grade != null) {
							memLevelGrade = grade;
						}
						// 变更后会员等级级别比变更前等级级别大的场合，表示会员升级，否则表示降级
						if(memLevelGrade > oldMemLevelGrade) {
							// 变化类型
							detailMap.put("changeType", "1");
						} else {
							// 变化类型
							detailMap.put("changeType", "2");
						}
						
						// 取得会员等级有效期开始日和结束日
						Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo((String)detailMap.get("tradeType"), (String)detailMap.get("acquiTime"), Integer.parseInt(memberLevelId), sysDate, memberInfoId);
						if(levelDateInfo != null && !levelDateInfo.isEmpty()) {
							if(levelDateInfo.get("levelStartDate") != null && !"".equals(levelDateInfo.get("levelStartDate").toString())) {
								// 等级有效期开始日
								detailMap.put("levelStartDate", levelDateInfo.get("levelStartDate"));
							}
							if(levelDateInfo.get("levelEndDate") != null && !"".equals(levelDateInfo.get("levelEndDate").toString())) {
								// 等级有效期结束日
								detailMap.put("levelEndDate", levelDateInfo.get("levelEndDate"));
							}
						}
						// 会员等级状态
						detailMap.put("levelStatus", "2");
						// 更新会员等级
						binBEMQMES08_Service.updateMemberLevel(detailMap);
					}
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(detailMap);
					
					String curTotalAmount = "0";
					// 会员累计销售金额存在的场合
					if(totalAmounts != null && !"".equals(totalAmounts)) {
						curTotalAmount = totalAmounts;
					} else {
						// 设定履历区分为累积金额
						detailMap.put("recordKbn", "1");
						// 取得变更前累计金额
						Object oldTotalAmounts = this.getNewRuleExecRecord(detailMap);
						if(oldTotalAmounts == null) {
							oldTotalAmounts = "0";
						}
						curTotalAmount = oldTotalAmounts.toString();
					}
					detailMap.put("curTotalAmount", curTotalAmount);
					// 发送等级下发MQ消息
					this.sendLevelMes(detailMap);
					detailMap.remove("changeType");
				}
				// 当前拥有的化妆次数存在的场合
				if(curBtimes != null && !"".equals(curBtimes)) {
					// 设定履历区分为化妆次数
					detailMap.put("recordKbn", "2");
					// 取得变更前化妆次数
					Object oldBtimes = this.getNewRuleExecRecord(detailMap);
					if(oldBtimes == null) {
						oldBtimes = "0";
					}
					// 变更前化妆次数
					detailMap.put("oldValue", oldBtimes);
					// 设定更新后的值为当前拥有的化妆次数
					detailMap.put("newValue", curBtimes);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(detailMap);
					detailMap.put("oldBtimes", oldBtimes);
					// 发送化妆次数下发MQ消息
					this.sendBTimesMes(detailMap);
				}
				// 会员当前有效积分存在的场合
				if(curPoints != null && !"".equals(curPoints)) {
					// 设定履历区分为积分
					detailMap.put("recordKbn", "3");
					// 取得变更前积分
					Object oldPoints = this.getNewRuleExecRecord(detailMap);
					if(oldPoints == null) {
						oldPoints = "0";
					}
					// 变更前积分
					detailMap.put("oldValue", oldPoints);
					// 设定更新后的值为会员当前有效积分
					detailMap.put("newValue", curPoints);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(detailMap);
				}
				// 会员累计销售金额存在的场合
				if(totalAmounts != null && !"".equals(totalAmounts)) {
					// 设定履历区分为累积金额
					detailMap.put("recordKbn", "1");
					// 取得变更前累计金额
					Object oldTotalAmounts = this.getNewRuleExecRecord(detailMap);
					if(oldTotalAmounts == null) {
						oldTotalAmounts = "0";
					}
					// 变更前累计金额
					detailMap.put("oldValue", oldTotalAmounts);
					// 设定更新后的值为会员累计销售金额
					detailMap.put("newValue", totalAmounts);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(detailMap);
					
					// 组织代码
					String orgCode = (String)detailMap.get("orgCode");
					// 品牌代码
					String brandCode = (String)detailMap.get("brandCode");
					// 会员化妆次数规则处理
					CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE03);
					// 会员化妆次数规则处理器存在的场合，执行会员化妆次数规则处理
					if (null != campRuleExec) {
						// 重新计算可兑换金额
						CampBaseDTO campBaseDTO = new CampBaseDTO();
						// 作成者
						campBaseDTO.setCreatedBy("BINBEMQMES08");
						// 作成程序名
						campBaseDTO.setCreatePGM("BINBEMQMES08");
						// 更新者
						campBaseDTO.setUpdatedBy("BINBEMQMES08");
						// 更新程序名
						campBaseDTO.setUpdatePGM("BINBEMQMES08");
						// 所属组织ID
						campBaseDTO.setOrganizationInfoId(Integer.parseInt(detailMap.get("organizationInfoID").toString()));
						// 所属品牌ID
						campBaseDTO.setBrandInfoId(Integer.parseInt(detailMap.get("brandInfoID").toString()));
						// 会员信息ID
						campBaseDTO.setMemberInfoId(Integer.parseInt(detailMap.get("memberInfoId").toString()));
						// 会员卡号
						campBaseDTO.setMemCode((String)detailMap.get("memberCode"));
						// 单据号
						campBaseDTO.setBillId((String)detailMap.get("tradeNoIF"));
						// 业务类型
						campBaseDTO.setTradeType((String)detailMap.get("tradeType"));
						// 单据产生日期
						campBaseDTO.setTicketDate((String)detailMap.get("acquiTime"));
						// 计算日期
						campBaseDTO.setCalcDate((String)detailMap.get("calcDate"));
						// 设定履历区分为可税换金额
						detailMap.put("recordKbn", "4");
						// 取得变更前可税换金额
						Object oldBtimesAmount = this.getNewRuleExecRecord(detailMap);
						if(oldBtimesAmount != null) {
							campBaseDTO.setOldBtimesAmount(Double.parseDouble(oldBtimesAmount.toString()));
						} else {
							campBaseDTO.setOldBtimesAmount(0);
						}
						// 理由
						campBaseDTO.setReason(DroolsConstants.REASON_1);
						// 当前累积金额
						campBaseDTO.setCurTotalAmount(Double.parseDouble(totalAmounts));
						// 设定当前化妆次数
						if(curBtimes != null && !"".equals(curBtimes)) {
							campBaseDTO.setCurBtimes(Integer.parseInt(curBtimes));
						} else {
							// 设定履历区分为化妆次数
							detailMap.put("recordKbn", "2");
							// 取得变更前化妆次数
							Object oldBtimes = this.getNewRuleExecRecord(detailMap);
							if(oldBtimes == null) {
								oldBtimes = "0";
							}
							campBaseDTO.setCurBtimes(Integer.parseInt(oldBtimes.toString()));
						}
						// 重新计算可兑换金额
						campRuleExec.ruleExec(campBaseDTO);
					}
				}
			}
		}
	}
	
	/**
	 * 化妆次数使用接收处理
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void analyzeUsedTimes(Map<String, Object> map) throws Exception {
		
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
		this.setInsertInfoMapKey(map);
		// 数据来源
		String sourse = (String)map.get("sourse");
		// 系统时间
		String sysDate = binBEMQMES08_Service.getForwardSYSDate();
		
		// 取得化妆次数使用明细数据
		List detailList = (List)map.get("detailDataDTOList");
		// 柜台号
		map.put("counterCode", ((Map)detailList.get(0)).get("countercode"));
		// BA卡号
		map.put("BAcode", ((Map)detailList.get(0)).get("bacode"));
		// 查询员工信息
		Map<String, Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(map);
		if(employeeInfo != null) {
			map.putAll(employeeInfo);
		}
		// 查询柜台部门信息
		Map<String, Object> counterInfo = binBEMQMES99_Service.selCounterDepartmentInfo(map);
		if(counterInfo != null) {
			map.putAll(counterInfo);
		}
		// 插入会员使用化妆次数积分主表
		int memUsedInfoId = binBEMQMES08_Service.addMemUsedInfo(map);
		// 循环处理化妆次数使用明细数据
		for(int i = 0; i < detailList.size(); i++) {
			
			Map detailMap = (Map)detailList.get(i);
			// 主表ID
			detailMap.put("memUsedInfoId", memUsedInfoId);
			// 设置组织ID
			detailMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 设置品牌ID
			detailMap.put("brandInfoID", map.get("brandInfoID"));
			// 设置组织代码
			detailMap.put("orgCode", map.get("orgCode"));
			// 设置品牌代码
			detailMap.put("brandCode", map.get("brandCode"));
			// 设定子类型
			detailMap.put("subType", map.get("subType"));
			// 设定数据来源
			detailMap.put("channel", sourse);
			// 设定业务类型
			detailMap.put("tradeType", map.get("tradeType"));
			// 设定单据号
			detailMap.put("tradeNoIF", map.get("tradeNoIF"));
			// 设定重算次数为0
			detailMap.put("reCalcCount", "0");
			// 设定理由为化妆次数使用
			detailMap.put("reason", "3");
			// 设定计算日期
			detailMap.put("calcDate", sysDate);
			// 单据产生日期
			detailMap.put("acquiTime", detailMap.get("businessTime"));
			this.setInsertInfoMapKey(detailMap);
			// 查询会员信息
			Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
			// 会员信息不存在的场合，表示接收失败，结束处理
			if(memberInfo == null) {
				MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_34);
			}
			detailMap.putAll(memberInfo);
			// 插入会员使用化妆次数积分明细表
			binBEMQMES08_Service.addMemUsedDetail(detailMap);
			
			String acquiTime = (String)detailMap.get("acquiTime");
			String initialDate = (String)detailMap.get("initialDate");
			// 业务日期比会员初始采集日期小的场合，不更新会员相关信息
			if(initialDate != null && acquiTime.substring(0,10).compareTo(initialDate) < 0) {
				continue;
			}
			
			// 是否需要重算
			boolean isReCalcFlag = needReCalc(detailMap);
			
			// 不需要添加或更新重算信息的场合
			if(!isReCalcFlag) {
				// 取得化妆使用次数
				String usedTimes = (String)detailMap.get("usedTimes");
				if(usedTimes != null && !"".equals(usedTimes)) {
					// 设定履历区分为化妆次数
					detailMap.put("recordKbn", "2");
					// 取得变更前的化妆次数
					Object oldBtimes = this.getNewRuleExecRecord(detailMap);
					if(oldBtimes == null) {
						oldBtimes = "0";
					}
					// 当前化妆使用次数减去使用次数作为新的当前化妆使用次数
					String curBtimes = String.valueOf(Integer.parseInt(oldBtimes.toString()) + Integer.parseInt(usedTimes));
					// 更新前的值
					detailMap.put("oldValue", oldBtimes);
					// 更新后的值
					detailMap.put("newValue", curBtimes);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(detailMap);
					detailMap.put("oldBtimes", oldBtimes);
					detailMap.put("curBtimes", curBtimes);
					
					Map<String, Object> memberExtInfo = new HashMap<String, Object>();
					memberExtInfo.put("memberInfoId", detailMap.get("memberInfoId"));
					memberExtInfo.put("curBtimes", curBtimes);
					// 更新会员信息扩展表
					binOLCM31_BL.updateMemberExtInfo(memberExtInfo);
					// 发送化妆次数下发MQ消息
					this.sendBTimesMes(detailMap);
				}
			}
		}
	}
	
	/**
	 * 
	 * 验证是否需要重算
	 * 
	 * @param map 查询条件
	 * @return boolean 验证结果 true: 需要  false: 不需要
	 */
	public boolean needReCalc(Map<String, Object> map) throws Exception {
		// 是否需要重算
		boolean isReCalcFlag = false;
		// 设定重算类型为等级和化妆次数重算
		map.put("reCalcType", "0");
		// 取得日期大于单据产生日期的规则执行记录数
		int count = binBEMQMES08_Service.getRuleExecCount(map);
		if(count > 0) {
			isReCalcFlag = true;
		} else {
			// 查询重算日期
			int reCalCount = binBEMQMES08_Service.getReCalcCount(map);
			if(reCalCount > 0) {
				isReCalcFlag = true;
			}
		}
		if (!isReCalcFlag) {
			// 判断会员是否正在进行重算
			boolean isReCalcExec = binOLCM31_BL.isReCalcExec(map);
			isReCalcFlag = isReCalcExec;
		}
		if(isReCalcFlag) {
			Map<String, Object> tmptInfo = (Map<String, Object>) map.get("TmptInfo");
			if (null != tmptInfo && !tmptInfo.isEmpty()) {
				// 更新积分变化主表
				binBEMQMES08_Service.updateTMUsedInfo(tmptInfo);
			}
			// 设定重算类型为等级和化妆次数重算
			map.put("reCalcType", "0");
			// 插入重算信息表
			binBEMQMES08_Service.addReCalcInfo(map);
			// 发送MQ重算消息进行实时重算
			this.sendReCalcMsg(map);
		}
		return isReCalcFlag;
	}
	/**
	 * 
	 * 插入重算信息表
	 * 
	 * @param map
	 */
	public void addReCalcInfo(Map<String, Object> map){
		// 插入重算信息表
		binBEMQMES08_Service.addReCalcInfo(map);
	}
	
	/**
	 * 发送MQ重算消息进行实时重算
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendReCalcMsg(Map<String, Object> map) throws Exception {
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_MR;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
				Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MR);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1001);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 修改次数
		mainData.put("ModifyCounts", "0");
		// 会员ID
		mainData.put("memberInfoId", map.get("memberInfoId"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 
	 * 查询会员最新规则执行履历记录
	 * 
	 * @param map 查询条件
	 * 
	 */
	public Object getNewRuleExecRecord(Map<String, Object> map) throws Exception {
		
		Map<String, Object> newRuleExecRecord = binBEMQMES08_Service.getNewRuleExecRecord(map);
		if(newRuleExecRecord != null) {
			Object newValue = newRuleExecRecord.get("newValue");
			if(newValue != null) {
				return newValue;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 设置等级MQ明细业务
	 * 
	 * @param detailMap 初始数据明细信息
	 * @param levelDetailList 等级MQ明细业务数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setLevelDetailDTO(Map detailMap, List<LevelDetailDTO> levelDetailList) {
		
		LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
		// 重算次数
		levelDetailDTO.setReCalcCount(DroolsConstants.DEF_MODIFYCOUNTS);
		// 操作类型
		levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 变更前等级ID
		Object oldMemberLevelId = detailMap.get("oldMemberLevelId");
		if(oldMemberLevelId != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("memberLevelId", oldMemberLevelId);
			// 通过会员等级ID取得会员等级代码
			String levelCode = binBEMQMES08_Service.getLevelCode(paramMap);
			levelDetailDTO.setMemberlevelOld(levelCode);
		}
		// 变更后等级
		levelDetailDTO.setMemberlevelNew((String)detailMap.get("member_level"));
		// 变化类型
		levelDetailDTO.setChangeType((String)detailMap.get("changeType"));
		// 会员卡号
		levelDetailDTO.setMemberCode((String)detailMap.get("memberCode"));
		// 柜台号
		String countercode = (String)detailMap.get("countercode");
		levelDetailDTO.setCounterCode(countercode);
		if (null != countercode && !"".equals(countercode)) {
			// 员工编号
			levelDetailDTO.setEmployeeCode((String)detailMap.get("bacode"));
		}
		// 业务类型
		levelDetailDTO.setBizType((String)detailMap.get("tradeType"));
		// 关联单据时间
		levelDetailDTO.setRelevantTicketDate((String)detailMap.get("acquiTime"));
		// 关联单号
		levelDetailDTO.setRelevantNo((String)detailMap.get("tradeNoIF"));
		// 变动渠道
		levelDetailDTO.setChannel((String)detailMap.get("channel"));
		String reason = (String) detailMap.get("reasonText");
		if (CherryChecker.isNullOrEmpty(reason, true)) {
			reason = DroolsConstants.MQ_INITDATA_REASON;
		}
		// 变化原因
		levelDetailDTO.setReason(reason);
		// 累积金额
		levelDetailDTO.setTotalAmount((String)detailMap.get("curTotalAmount"));
		levelDetailList.add(levelDetailDTO);
	}
	
	/**
	 * 
	 * 设置化妆次数MQ明细业务
	 * 
	 * @param detailMap 初始数据明细信息
	 * @param btimesDetailList 化妆次数MQ明细业务数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setBTimesDetailDTO(Map detailMap, List<BTimesDetailDTO> btimesDetailList) {
		
		BTimesDetailDTO btimesDetailDTO = new BTimesDetailDTO();
		// 重算次数
		btimesDetailDTO.setReCalcCount(DroolsConstants.DEF_MODIFYCOUNTS);
		// 操作类型
		btimesDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 当前化妆次数
		String curBtimes = (String)detailMap.get("curBtimes");
		// 变更前化妆次数
		String oldBtimes = (String)detailMap.get("oldBtimes");
		// 化妆次数差分
		int diffBtimes = Integer.parseInt(curBtimes) - Integer.parseInt(oldBtimes);
		// 变更前化妆次数
		btimesDetailDTO.setBtimesOld(oldBtimes);
		// 变更后化妆次数
		btimesDetailDTO.setCurBtimesNew(curBtimes);
		// 化妆次数差分
		btimesDetailDTO.setDiffBtimes(String.valueOf(diffBtimes));
		// 会员卡号
		btimesDetailDTO.setMemberCode((String)detailMap.get("memberCode"));
		// 柜台号
		btimesDetailDTO.setCounterCode((String)detailMap.get("countercode"));
		// 员工编号
		btimesDetailDTO.setEmployeeCode((String)detailMap.get("bacode"));
		// 业务类型
		btimesDetailDTO.setBizType((String)detailMap.get("tradeType"));
		// 关联单据时间
		btimesDetailDTO.setRelevantTicketDate((String)detailMap.get("acquiTime"));
		// 关联单号
		btimesDetailDTO.setRelevantNo((String)detailMap.get("tradeNoIF"));
		// 变动渠道
		btimesDetailDTO.setChannel((String)detailMap.get("channel"));
		String tradeType = (String)detailMap.get("tradeType");
		// 业务类型为初始数据导入的场合
		if("MS".equals(tradeType)) {
			// 变化原因
			btimesDetailDTO.setReason(DroolsConstants.MQ_INITDATA_REASON);
		} else if("BU".equals(tradeType)) {// 业务类型为化妆次数使用的场合
			// 变化原因
			btimesDetailDTO.setReason(DroolsConstants.MQ_USEDTIMES_REASON);
		}
		btimesDetailList.add(btimesDetailDTO);
	}
	
	/**
	 * 
	 * 发送等级下发MQ消息
	 * 
	 * @param detailMap 初始数据明细信息
	 * @param levelDetailList 等级MQ明细业务数据List
	 * 
	 */
	public void sendLevelMes(Map<String, Object> detailMap) throws Exception {
		
		if(detailMap == null) {
			return;
		}
		// 等级MQ明细业务List
		List<LevelDetailDTO> levelDetailList = new ArrayList<LevelDetailDTO>();
		// 设置等级MQ明细业务
		this.setLevelDetailDTO(detailMap, levelDetailList);
		LevelDetailDTO levelDetailDTO = levelDetailList.get(0);
		LevelMainDTO levelMainDTO = new LevelMainDTO();
		// 品牌代码
		levelMainDTO.setBrandCode((String)detailMap.get("brandCode"));
		// 单据号
		String orgId = String.valueOf(detailMap.get("organizationInfoID"));
		String brandId = String.valueOf(detailMap.get("brandInfoID"));
		String ticketNumber = binOLCM03_BL.getTicketNumber(orgId, 
				brandId, (String)detailMap.get("createdBy"), DroolsConstants.MQ_BILLTYPE_ML);
		levelMainDTO.setTradeNoIF(ticketNumber);
		// 会员卡号
		levelMainDTO.setMemberCode(levelDetailDTO.getMemberCode());
		// 修改回数
		levelMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		levelMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_ML);
		// 当前等级
		levelMainDTO.setMember_level(levelDetailDTO.getMemberlevelNew());
		// 计算时间
		levelMainDTO.setCaltime((String)detailMap.get("calcDate"));
		// 柜台号
		levelMainDTO.setCountercode(levelDetailDTO.getCounterCode());
		// 员工编号
		levelMainDTO.setBacode(levelDetailDTO.getEmployeeCode());
		// 开卡等级
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 开卡等级ID
		paramMap.put("memberLevelId", detailMap.get("grantMemberLevel"));
		// 通过会员等级ID取得会员等级代码
		String levelCode = binBEMQMES08_Service.getLevelCode(paramMap);
		levelMainDTO.setGrantMemberLevel(levelCode);
		// 上一次等级
		levelMainDTO.setPrevLevel(levelDetailDTO.getMemberlevelOld());
		// 本次等级变化时间
		levelMainDTO.setLevelAdjustTime(levelDetailDTO.getRelevantTicketDate());
		// 入会时间调整准则
		String jnDateKbn = binOLCM14_BL.getConfigValue("1076", orgId, brandId);
		// 以首单销售为准
		if ("2".equals(jnDateKbn)) {
			// 入会时间
			levelMainDTO.setJoinDate(ConvertUtil.getString(detailMap.get("curJoinDate")));
		} else {
			// 入会时间
			levelMainDTO.setJoinDate("");
		}
		// 等级MQ明细业务List
		levelMainDTO.setLevelDetailList(levelDetailList);
		// MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(detailMap.get("organizationInfoID").toString()));
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(detailMap.get("brandInfoID").toString()));
		// 单据类型
		mqInfoDTO.setBillType(levelMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 取得MQ消息体
		String msg = null;
		// 消息体
		if (CherryChecker.isNullOrEmpty(detailMap.get("memberClubId"))) {
			msg = levelMainDTO.getMQMsg();
		} else {
			// 会员俱乐部代号
			levelMainDTO.setClubCode(binOLCM31_BL.getClubCode(detailMap));
			msg = levelMainDTO.getClubMQMsg();
		}
		mqInfoDTO.setData(msg);
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", detailMap.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", detailMap.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_ML);
		// 单据号
		dbObject.put("TradeNoIF", ticketNumber);
		// 修改次数
		dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务主体
	    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
	    // 业务主体代码
	    dbObject.put("TradeEntityCode", levelMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", detailMap.get("memberName"));
	    // 发生时间
	    dbObject.put("OccurTime", levelMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
		// MQ消息发送处理
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);

		int memberId = ConvertUtil.getInt(detailMap.get("memberInfoId"));
		//将等级调整会员插入到临时表
		binOLCM31_BL.addTempAdjustMember(memberId,mqInfoDTO.getOrganizationInfoId(),mqInfoDTO.getBrandInfoId());
	}
	
	/**
	 * 
	 * 发送化妆次数下发MQ消息
	 * 
	 * @param detailMap 初始数据明细信息
	 * @param btimesDetailList 化妆次数MQ明细业务数据List
	 * 
	 */
	public void sendBTimesMes(Map<String, Object> detailMap) throws Exception {
		
		if(detailMap == null) {
			return;
		}
		// 化妆次数MQ明细业务List
		List<BTimesDetailDTO> btimesDetailList = new ArrayList<BTimesDetailDTO>();
		// 设置化妆次数MQ明细业务
		this.setBTimesDetailDTO(detailMap, btimesDetailList);
		BTimesDetailDTO btimesDetailDTO = btimesDetailList.get(0);
		BTimesMainDTO btimesMainDTO = new BTimesMainDTO();
		// 品牌代码
		btimesMainDTO.setBrandCode((String)detailMap.get("brandCode"));
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(detailMap.get("organizationInfoID")), 
				String.valueOf(detailMap.get("brandInfoID")), (String)detailMap.get("createdBy"), DroolsConstants.MQ_BILLTYPE_MG);
		btimesMainDTO.setTradeNoIF(ticketNumber);
		// 会员卡号
		btimesMainDTO.setMemberCode(btimesDetailDTO.getMemberCode());
		// 修改回数
		btimesMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		btimesMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_MG);
		// 当前化妆次数
		btimesMainDTO.setCurBtimes(btimesDetailDTO.getCurBtimesNew());
		// 计算时间
		btimesMainDTO.setCaltime((String)detailMap.get("calcDate"));
		
		// 化妆次数MQ明细业务List
		btimesMainDTO.setBtimesDetailList(btimesDetailList);
		// MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(detailMap.get("organizationInfoID").toString()));
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(detailMap.get("brandInfoID").toString()));
		// 单据类型
		mqInfoDTO.setBillType(btimesMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(btimesMainDTO.getMQMsg());
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", detailMap.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", detailMap.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MG);
		// 单据号
		dbObject.put("TradeNoIF", ticketNumber);
		// 修改次数
		dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务主体
	    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
	    // 业务主体代码
	    dbObject.put("TradeEntityCode", btimesMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", detailMap.get("memberName"));
	    // 发生时间
	    dbObject.put("OccurTime", btimesMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
		// MQ消息发送处理
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException {
		
		DBObject dbObject = new BasicDBObject();
		// 组织代号
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改回数
		dbObject.put("ModifyCounts", map.get("modifyCounts"));
		// 业务主体
		dbObject.put("TradeEntity", "0");
		
		List detailList = (List)map.get("detailDataDTOList");
		if(detailList != null && !detailList.isEmpty()) {
			
			Map detailMap = (Map)detailList.get(0);
			// 会员卡号
			dbObject.put("TradeEntityCode", detailMap.get("memberCode"));
			// 会员姓名
			dbObject.put("TradeEntityName", detailMap.get("memberName"));
			// BA卡号
			dbObject.put("UserCode", detailMap.get("bacode"));
			// BA姓名
			dbObject.put("UserName", map.get("BAname"));
			// BA岗位
			dbObject.put("UserPost", map.get("categoryName"));
			// 柜台号
			dbObject.put("DeptCode", detailMap.get("countercode"));
			// 柜台名称
			dbObject.put("DeptName", map.get("counterName"));
			// 业务类型
			String tradeType = (String)map.get("tradeType");
			// 业务类型为会员初始数据时
			if("MS".equals(tradeType)) {
				// 会员当前等级代码
				dbObject.put("Member_level", detailMap.get("member_level"));
				// 当前拥有的化妆次数
				dbObject.put("CurBtimes", detailMap.get("curBtimes"));
				// 会员当前有效积分
				dbObject.put("CurPoints", detailMap.get("curPoints"));
				// 会员累计销售金额（仅体验会员采集）
				dbObject.put("TotalAmounts", detailMap.get("totalAmounts"));
				// 会员初始入会时间
				dbObject.put("JoinDate", detailMap.get("joinDate"));
				// 采集时间
				dbObject.put("AcquiTime", detailMap.get("acquiTime"));
			} else if("BU".equals(tradeType)) { // 业务类型为化妆次数使用时
				// 使用次数
				dbObject.put("UsedTimes", detailMap.get("usedTimes"));
				// 业务时间
				dbObject.put("BusinessTime", detailMap.get("businessTime"));
			}
		}
		map.put("dbObject", dbObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selMessageInfo(Map map) throws CherryMQException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDetailDataInfo(List detailDataList, Map map)
			throws CherryMQException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES08");
		map.put("createPGM", "BINBEMQMES08");
		map.put("updatedBy", "BINBEMQMES08");
		map.put("updatePGM", "BINBEMQMES08");
	}

	/**
	 * 会员修改属性处理
	 * @param map
	 */
	@Override
	public void updateMemberExtInfo(Map<String, Object> map) throws Exception {

		// 取得变更前等级ID
		String oldMemberLevelId = (String)map.get("oldMemberLevelId");
		// 取得变更后等级ID
		String memberLevelId = (String)map.get("memberLevelId");
		// 取得变更前入会时间
		String oldJoinDate = (String)map.get("oldJoinDate");
		// 取得变更后入会时间
		String joinDate = (String)map.get("joinDate");
		// 取得变更前化妆次数
		String oldBtimes = (String)map.get("oldBtimes");
		// 取得变更后化妆次数
		String btimes = (String)map.get("btimes");
		// 取得变更前累计金额
		String oldTotalAmount = (String)map.get("oldTotalAmount");
		// 取得变更后累计金额
		String totalAmount = (String)map.get("totalAmount");
		// 会员属性是否修改
		boolean isChange = false;
		Map<String, Object> detailMap = new HashMap<String, Object>();
		// 会员俱乐部ID
		int memberClubId = 0;
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			memberClubId = Integer.parseInt(String.valueOf(map.get("memberClubId")));
			detailMap.put("memberClubId", memberClubId);
		}
		// 会员等级变更的场合
		if(!memberLevelId.equals(oldMemberLevelId)) {
			detailMap.put("memberLevelId", memberLevelId);
			detailMap.put("oldMemberLevelId", oldMemberLevelId);
			isChange = true;
		}
//		// 会员入会时间变更的场合
		if(!joinDate.equals(oldJoinDate)) {
			detailMap.put("joinDate", joinDate);
			if(!"".equals(oldJoinDate)){
			detailMap.put("oldJoinDate", oldJoinDate);
			}
			isChange = true;
		}
		// 化妆次数变化的场合
		if(!btimes.equals(oldBtimes)) {
			if("".equals(btimes)) {
				btimes = "0";
			}
			if("".equals(oldBtimes)) {
				oldBtimes = "0";
			}
			detailMap.put("curBtimes", btimes);
			detailMap.put("oldBtimes", oldBtimes);
			isChange = true;
		}
		// 累积金额是否变更
		boolean isAmountChange = false;
		if(totalAmount != null && !"".equals(totalAmount)) {
			if(oldTotalAmount != null && !"".equals(oldTotalAmount)) {
				if(Double.parseDouble(totalAmount) != Double.parseDouble(oldTotalAmount)) {
					isAmountChange = true;
				}
			} else {
				isAmountChange = true;
			}
		} else {
			if(oldTotalAmount != null && !"".equals(oldTotalAmount)) {
				isAmountChange = true;
			}
		}
		// 累积金额变更的场合
		if(isAmountChange) {
			if("".equals(totalAmount)) {
				totalAmount = "0.00";
			}
			if("".equals(oldTotalAmount)) {
				oldTotalAmount = "0.00";
			}
			detailMap.put("totalAmounts", totalAmount);
			detailMap.put("oldTotalAmount", oldTotalAmount);
			isChange = true;
		}
		// 会员属性没有变更的场合，抛出错误信息
		if(!isChange) {
			throw new CherryException("EMB00006");
		}
		
		this.setInsertInfoMapKey(detailMap);
		// 系统时间
		String sysDate = binBEMQMES08_Service.getForwardSYSDate();
		// 业务类型
		String tradeType = "MS";
		// 单据号
		String tradeNoIF = tradeType + sysDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "") + "001";
		// 来源
		String sourse = "Cherry";
		// 设定计算日期
		detailMap.put("calcDate", sysDate);
		// 设定业务日期
		detailMap.put("acquiTime", sysDate);
		// 设定业务类型
		detailMap.put("tradeType", tradeType);
		// 设定单据号
		detailMap.put("tradeNoIF", tradeNoIF);
		// 来源
		detailMap.put("sourse", sourse);
		detailMap.put("channel", sourse);
		// 会员ID
		int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
		detailMap.put("memberInfoId", map.get("memberInfoId"));
		// 会员卡号
		detailMap.put("memberCode", map.get("memberCode"));
		// 会员信息更新时间
		detailMap.put("memInfoUdTime", map.get("memInfoUdTime"));
		// 会员信息更新次数
		detailMap.put("memInfoMdCount", map.get("memInfoMdCount"));
		// 会员扩展信息更新时间
		detailMap.put("extInfoUdTime", map.get("extInfoUdTime"));
		// 会员扩展信息更新次数
		detailMap.put("extInfoMdCount", map.get("extInfoMdCount"));
		// 会员俱乐部信息更新时间
		detailMap.put("clubInfoUdTime", map.get("clubInfoUdTime"));
		// 会员俱乐部信息更新次数
		detailMap.put("clubInfoMdCount", map.get("clubInfoMdCount"));
		// 组织ID
		detailMap.put("organizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌ID
		detailMap.put("brandInfoID", map.get(CherryConstants.BRANDINFOID));
		// 组织代码
		String orgCode = (String) map.get(CherryConstants.ORG_CODE);
		detailMap.put("orgCode", orgCode);
		// 品牌代码
		String brandCode = (String) map.get(CherryConstants.BRAND_CODE);
		detailMap.put("brandCode", brandCode);
		// 员工ID
		detailMap.put("employeeID", map.get("employeeId"));
		// 员工code
		detailMap.put("BAcode", map.get("employeeCode"));
		//detailMap.put("bacode", map.get("employeeCode"));
		// 备注
		detailMap.put("reasonText", map.get("reason"));
		
		// 查询会员信息
		Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
		// 会员信息不存在的场合，抛出错误信息
		if(memberInfo == null) {
			throw new CherryException("EMB00007");
		}
		detailMap.put("grantMemberLevel", memberInfo.get("grantMemberLevel"));
		detailMap.put("memberName", memberInfo.get("memberName"));
		detailMap.put("curJoinDate", memberInfo.get("curJoinDate"));
		// 会员等级变更或者会员入会时间变更的场合，更新会员信息
		if(!memberLevelId.equals(oldMemberLevelId)|| !oldJoinDate.equals(joinDate)) {
			// 会员等级变更的场合，设置更新等级相关的信息
			if(!memberLevelId.equals(oldMemberLevelId)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 变更前等级ID
				paramMap.put("memberLevelId", oldMemberLevelId);
				// 业务发生时间
				paramMap.put("acquiTime", sysDate);
				if (0 != memberClubId) {
					paramMap.put("memberClubId", memberClubId);
				}
				// 变更前会员等级级别
				int oldMemLevelGrade = 0;
				// 取得变更前会员等级级别
				Integer grade = binBEMQMES08_Service.getMemLevelGrade(paramMap);
				if(grade != null) {
					oldMemLevelGrade = grade;
				}
				// 变更后等级ID
				paramMap.put("memberLevelId", memberLevelId);
				// 取得变更后会员等级级别
				int memLevelGrade = 0;
				grade = binBEMQMES08_Service.getMemLevelGrade(paramMap);
				if(grade != null) {
					memLevelGrade = grade;
				}
				// 变更后会员等级级别比变更前等级级别大的场合，表示会员升级，否则表示降级
				if(memLevelGrade > oldMemLevelGrade) {
					// 变化类型
					detailMap.put("changeType", "1");
				} else {
					// 变化类型
					detailMap.put("changeType", "2");
				}
				
				// 取得会员等级有效期开始日和结束日
				Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(tradeType, sysDate, Integer.parseInt(memberLevelId), sysDate, memberInfoId);
				if(levelDateInfo != null && !levelDateInfo.isEmpty()) {
					if(levelDateInfo.get("levelStartDate") != null && !"".equals(levelDateInfo.get("levelStartDate").toString())) {
						// 等级有效期开始日
						detailMap.put("levelStartDate", levelDateInfo.get("levelStartDate"));
					}
					if(levelDateInfo.get("levelEndDate") != null && !"".equals(levelDateInfo.get("levelEndDate").toString())) {
						// 等级有效期结束日
						detailMap.put("levelEndDate", levelDateInfo.get("levelEndDate"));
					}
				}
				
				// 会员等级状态
				detailMap.put("levelStatus", "2");
			}
			// 更新会员信息
			int updCount = 0;
			if (0 == memberClubId) {
				updCount = binBEMQMES08_Service.updateMemberInfoExc(detailMap);
			} else {
				if (null == detailMap.get("clubInfoMdCount") 
						|| "".equals(String.valueOf(detailMap.get("clubInfoMdCount")))) {
					binBEMQMES08_Service.addMemClubLevelExc(detailMap);
					updCount = 1;
				} else {
					updCount = binBEMQMES08_Service.updateMemberClubInfoExc(detailMap);
				}
			}
			// 更新失败
			if(updCount == 0) {
				throw new CherryException("EMB00008");
			}
		}
		// 化妆次数变更或者累积金额变更的场合，更新会员扩展属性表
		if(!btimes.equals(oldBtimes) || isAmountChange) {
			// 更新时间
			String extInfoUdTime = (String)detailMap.get("extInfoUdTime");
			// 更新时间不存在表示会员的扩展属性为首次添加
			if(extInfoUdTime != null && !"".equals(extInfoUdTime)) {
				// 更新会员信息扩展表
				int updCount = binBEMQMES08_Service.updateMemberExtInfoExc(detailMap);
				// 更新失败
				if(updCount == 0) {
					throw new CherryException("EMB00008");
				}
			} else {
				try {
					// 插入会员信息扩展表
					binBEMQMES08_Service.addMemberExtInfo(detailMap);
				} catch (Exception e) {
					throw new CherryException("EMB00008");
				}
			}
		}
		
		// 插入会员使用化妆次数积分主表
		int memUsedInfoId = binBEMQMES08_Service.addMemUsedInfo(detailMap);
		// 主表ID
		detailMap.put("memUsedInfoId", memUsedInfoId);
		// 插入会员使用化妆次数积分明细表
		binBEMQMES08_Service.addMemUsedDetail(detailMap);
		
		// 设定重算次数为0
		detailMap.put("reCalcCount", "0");
		// 设定理由为初始数据导入
		detailMap.put("reason", "1");
		
		// 会员等级变更的场合
		if(!memberLevelId.equals(oldMemberLevelId)) {
			// 设定履历区分为等级
			detailMap.put("recordKbn", "0");
			// 变更前等级ID
			detailMap.put("oldValue", oldMemberLevelId);
			// 变更后等级ID
			detailMap.put("newValue", memberLevelId);
			// 插入规则执行履历表
			binBEMQMES08_Service.addRuleExecRecord(detailMap);
			// 当前累积金额
			detailMap.put("curTotalAmount", totalAmount);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("memberLevelId", memberLevelId);
			// 通过会员等级ID取得会员等级代码
			String levelCode = binBEMQMES08_Service.getLevelCode(paramMap);
			// 当前等级code
			detailMap.put("member_level", levelCode);
			// 积分规则处理
			CampRuleBatchExec_IF pointClearExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT04);
			// 需要将清零标识清空
			if (null != pointClearExec) {
				if (0 == memberClubId) {
					// 清除会员清零标识
					binOLCM31_BL.delClearFlag(memberInfoId);
				} else {
					binOLCM31_BL.delClubClearFlag(memberInfoId, memberClubId);
				}
			}
			// 发送等级下发MQ消息
			this.sendLevelMes(detailMap);
			// 需要同步天猫会员
			if (binOLCM31_BL.needSync(memberInfoId, brandCode)) {
				Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
				tmSyncInfo.put("memberInfoId", memberInfoId);
				tmSyncInfo.put("brandCode", brandCode);
				tmSyncInfo.put("PgmName", "BINBEMQMES08");
				// 同步天猫会员
				binOLCM31_BL.syncTmall(tmSyncInfo);
			}
			detailMap.remove("changeType");
		}
		// 化妆次数变更的场合
		if(!btimes.equals(oldBtimes)) {
			// 设定履历区分为化妆次数
			detailMap.put("recordKbn", "2");
			// 变更前化妆次数
			detailMap.put("oldValue", oldBtimes);
			// 变更后化妆次数
			detailMap.put("newValue", btimes);
			// 插入规则执行履历表
			binBEMQMES08_Service.addRuleExecRecord(detailMap);
			// 发送化妆次数下发MQ消息
			this.sendBTimesMes(detailMap);
		}
		// 累积金额变更的场合
		if(isAmountChange) {
			// 设定履历区分为累积金额
			detailMap.put("recordKbn", "1");
			// 变更前累计金额
			detailMap.put("oldValue", oldTotalAmount);
			// 变更后累计金额
			detailMap.put("newValue", totalAmount);
			// 插入规则执行履历表
			binBEMQMES08_Service.addRuleExecRecord(detailMap);
			
//			// 组织代码
//			String orgCode = (String)detailMap.get("orgCode");
//			// 品牌代码
//			String brandCode = (String)detailMap.get("brandCode");
			// 会员化妆次数规则处理
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE03);
			// 会员化妆次数规则处理器存在的场合，执行会员化妆次数规则处理
			if (null != campRuleExec) {
				// 重新计算可兑换金额
				CampBaseDTO campBaseDTO = new CampBaseDTO();
				// 作成者
				campBaseDTO.setCreatedBy("BINBEMQMES08");
				// 作成程序名
				campBaseDTO.setCreatePGM("BINBEMQMES08");
				// 更新者
				campBaseDTO.setUpdatedBy("BINBEMQMES08");
				// 更新程序名
				campBaseDTO.setUpdatePGM("BINBEMQMES08");
				// 所属组织ID
				campBaseDTO.setOrganizationInfoId(Integer.parseInt(detailMap.get("organizationInfoID").toString()));
				// 所属品牌ID
				campBaseDTO.setBrandInfoId(Integer.parseInt(detailMap.get("brandInfoID").toString()));
				// 会员信息ID
				campBaseDTO.setMemberInfoId(Integer.parseInt(detailMap.get("memberInfoId").toString()));
				// 会员卡号
				campBaseDTO.setMemCode((String)detailMap.get("memberCode"));
				// 单据号
				campBaseDTO.setBillId((String)detailMap.get("tradeNoIF"));
				// 业务类型
				campBaseDTO.setTradeType((String)detailMap.get("tradeType"));
				// 单据产生日期
				campBaseDTO.setTicketDate((String)detailMap.get("acquiTime"));
				// 计算日期
				campBaseDTO.setCalcDate((String)detailMap.get("calcDate"));
				// 设定履历区分为可税换金额
				detailMap.put("recordKbn", "4");
				// 取得变更前可税换金额
				Object oldBtimesAmount = this.getNewRuleExecRecord(detailMap);
				if(oldBtimesAmount != null) {
					campBaseDTO.setOldBtimesAmount(Double.parseDouble(oldBtimesAmount.toString()));
				} else {
					campBaseDTO.setOldBtimesAmount(0);
				}
				// 理由
				campBaseDTO.setReason(DroolsConstants.REASON_1);
				// 当前累积金额
				campBaseDTO.setCurTotalAmount(Double.parseDouble(totalAmount));
				if(!btimes.equals(oldBtimes)) {
					// 设定当前化妆次数
					campBaseDTO.setCurBtimes(Integer.parseInt(btimes));
				} else {
					if("".equals(oldBtimes)) {
						oldBtimes = "0";
					}
					// 设定当前化妆次数
					campBaseDTO.setCurBtimes(Integer.parseInt(oldBtimes));
				}
				// 重新计算可兑换金额
				campRuleExec.ruleExec(campBaseDTO);
			}
		}
	}
	
}
