/*  
 * @(#)BINBEMQMES13_BL.java     1.0 2014/11/25     
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES08_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 会员初始数据采集信息接收处理BL
 * 
 * @author hub
 *
 */
public class BINBEMQMES13_BL {
	
	/** 消息数据接收共通处理Service */
	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/** 会员初始数据采集信息接收处理Service **/
	@Resource
	private BINBEMQMES08_Service binBEMQMES08_Service;
	
	/** 会员初始数据采集信息接收处理BL */
	@Resource
	private BINBEMQMES08_BL binBEMQMES08_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES13_BL.class);
	
	public void handleMessage(Map<String, Object> map) throws Exception {
		String tradeType = ConvertUtil.getString(map.get("tradeType"));
		if("MT".equalsIgnoreCase(tradeType)){
			// 推送消息
			Map<String, Object> pointRuleCalInfo = new HashMap<String, Object>();
			// 组织ID
			int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
			// 品牌ID
			int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
			// 员工代码
			map.put("BAcode", map.get("employeeCode"));
			this.setInsertInfoMapKey(map);
			// 查询员工信息
			Map<String, Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(map);
			if(employeeInfo != null) {
				map.putAll(employeeInfo);
			}
			String counterCode = (String) map.get("counterCode");
			// 查询柜台部门信息
			Map<String, Object> counterInfo = null;
			if (!CherryChecker.isNullOrEmpty(counterCode, true)) {
				// 查询柜台部门信息
				counterInfo = binBEMQMES99_Service.selCounterDepartmentInfo(map);
				if(counterInfo != null) {
					map.putAll(counterInfo);
				}
			}
			// 查询会员信息
			Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(map);
			// 会员信息不存在的场合，表示接收失败，结束处理
			if(memberInfo == null) {
				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_34);
			}
			map.putAll(memberInfo);
			// 当前操作时间
			String time = (String) map.get("time");
			// 初始录入时间
			String initialMTTime = (String) map.get("initTime");
			// 初始积分
			double initMTPoint = Double.parseDouble(String.valueOf(map.get("initPoint")));
			map.put("acquiTime", initialMTTime);
			map.put("initialMTTime", initialMTTime);
			map.put("initialMTPoint", initMTPoint);
			map.put("usedTimes", initMTPoint);
			// 会员信息ID
			int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
			// 取得会员当前积分信息
			Map<String, Object> memPointInfo = binOLCM31_BL.getMemberPointInfo(map);
			// 修改前积分
			double totalPoint = 0;
			// 前卡积分值
			double preCardPoint = 0;
			// 累计失效积分
			double totalDisablePoint = 0;
			// 本次将失效积分
			double curDisablePoint = 0;
			// 当前可兑换积分
			double curChangablePoint = 0;
			// 上回积分失效日期
			String preDisableDate = null;
			// 本次积分失效日期
			String curDealDate = null;
			// 设置组织ID
			map.put("organizationInfoId", organizationInfoId);
			// 设置品牌ID
			map.put("brandInfoId", brandInfoId);
			if (null != memPointInfo && !memPointInfo.isEmpty()) {
				totalPoint = Double.parseDouble(memPointInfo.get("curTotalPoint").toString());
				preCardPoint = Double.parseDouble(memPointInfo.get("mbPreCardPoint").toString());
				totalDisablePoint = Double.parseDouble(memPointInfo.get("totalDisablePoint").toString());
				curDisablePoint = Double.parseDouble(memPointInfo.get("curDisablePoint").toString());
				preDisableDate = (String) memPointInfo.get("preDisableDate");
				curDealDate = (String) memPointInfo.get("curDealDate");
				curChangablePoint = Double.parseDouble(memPointInfo.get("curChangablePoint").toString());
			}
			// 查询会员初始积分信息
			Map<String, Object> mtMap = binBEMQMES08_Service.getMTUsedInfo(map);
			if (null != mtMap && !mtMap.isEmpty()) {
				if (DateUtil.compDateTime(time, String.valueOf(mtMap.get("acquiTime"))) <= 0) {
					logger.error("当前操作时间小于或者等于已保存的采集时间，不处理！");
					return;
				}
				double point = Double.parseDouble(String.valueOf(mtMap.get("usedCount")));
				if (initMTPoint == point) {
					logger.error("初始积分未发生变化，不处理！");
					return;
				}
				map.put("memUsedInfoId", mtMap.get("memUsedInfoId"));
				map.put("memUsedDetailId", mtMap.get("memUsedDetailId"));
				// 更新会员初始积分主记录
				binBEMQMES08_Service.updateMemberMTInfo(map);
				// 更新会员初始积分明细记录
				binBEMQMES08_Service.updateMemberMTDtlInfo(map);
				// 当前积分
				map.put("curTotalPoint", totalPoint);
		    	// 可兑换积分
				map.put("curChangablePoint", curChangablePoint);
				// 更新会员积分信息表
		    	binOLCM31_BL.updateMemberPointInfo(map);
				String businessTime = (String) mtMap.get("businessTime");
				if (DateUtil.compDateTime(businessTime, initialMTTime) < 0) {
					map.put("acquiTime", businessTime);
				}
				// 设定重算类型为等级和化妆次数重算
				map.put("reCalcType", "0");
				// 插入重算信息表
				binBEMQMES08_Service.addReCalcInfo(map);
				// 发送MQ重算消息进行实时重算
				binBEMQMES08_BL.sendReCalcMsg(map);
			} else {
				// 插入会员状态修改主表
		    	int memUsedInfoId = binBEMQMES08_Service.addMemUsedInfo(map);
		    	// 主表ID
		    	map.put("memUsedInfoId", memUsedInfoId);
		    	// 插入会员状态修改明细表
				binBEMQMES08_Service.addMemUsedDetail(map);
		    	// 是否需要重算
				boolean isReCalcFlag = binBEMQMES08_BL.needReCalc(map);
				// 不需要添加或更新重算信息的场合
				if(!isReCalcFlag) {
					// 可兑换积分
					double ChangablePoint = 0;
					// 当前累计兑换积分
					double curTotalChanged = 0;
					if (null != memPointInfo && !memPointInfo.isEmpty()) {
						ChangablePoint = Double.parseDouble(memPointInfo.get("curChangablePoint").toString());
						curTotalChanged = Double.parseDouble(memPointInfo.get("curTotalChanged").toString());
					}
					// 修改后当前积分
					double curPoint = 0;
					// 积分类型
					String pointType = DroolsConstants.POINTTYPE99;
					curPoint = DoubleUtil.add(totalPoint, initMTPoint);
					// 当前可兑换积分
					double curChangePoint = DoubleUtil.add(ChangablePoint, initMTPoint);
					
					// 会员卡号
					String memberCode = (String) map.get("memberCode");
					// 员工ID
					int employeeId = 0;
					if (null != map.get("employeeID")) {
						employeeId = Integer.parseInt(map.get("employeeID").toString());
					}
					// 验证是否是当前卡
					boolean isCurCard = binOLCM31_BL.isCurCard(memberInfoId, memberCode);
					if (!isCurCard) {
						preCardPoint = DoubleUtil.add(preCardPoint, initMTPoint);
					}
					// 设置组织代码
					String orgCode = (String) map.get("orgCode");
					// 设置品牌代码
					String brandCode = (String) map.get("brandCode");
					// 系统时间
					String sysDate = binBEMQMES08_Service.getForwardSYSDate();
					// 会员DTO
					CampBaseDTO campBaseDTO = new CampBaseDTO();
					// 会员信息ID
					campBaseDTO.setMemberInfoId(memberInfoId);
					// 会员姓名
					campBaseDTO.setMemName((String) map.get("memberName"));
					// 组织代码
					campBaseDTO.setOrgCode(orgCode);
					// 品牌代码
					campBaseDTO.setBrandCode(brandCode);
					// 组织信息ID
					campBaseDTO.setOrganizationInfoId(organizationInfoId);
					// 品牌ID
					campBaseDTO.setBrandInfoId(brandInfoId);
					// 计算时间
					campBaseDTO.setCalcDate(sysDate);
					// 积分信息DTO
					PointDTO pointInfo = new PointDTO();
					// 会员积分变化主记录
					PointChangeDTO pointChange = new PointChangeDTO();
					// 会员积分变化明细记录
			    	List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
			    	// 当前总积分
			    	pointInfo.setCurTotalPoint(curPoint);
			    	// 累计兑换积分
			    	pointInfo.setCurTotalChanged(curTotalChanged);
			    	// 可兑换积分
			    	pointInfo.setCurChangablePoint(curChangePoint);
			    	// 累计失效积分
			    	pointInfo.setTotalDisablePoint(totalDisablePoint);
			    	// 本次将失效积分
			    	pointInfo.setCurDisablePoint(curDisablePoint);
			    	// 上回积分失效日期
			    	pointInfo.setPreDisableDate(preDisableDate);
			    	// 本次积分失效日期
			    	pointInfo.setCurDealDate(curDealDate);
			    	// 前卡积分值
			    	pointInfo.setPreCardPoint(preCardPoint);
			    	// 所属组织ID
			    	pointChange.setOrganizationInfoId(organizationInfoId);
			    	// 所属品牌ID
			    	pointChange.setBrandInfoId(brandInfoId);
			    	// 单据号
			    	pointChange.setTradeNoIF((String) map.get("tradeNoIF"));
			    	// 业务类型
			    	pointChange.setTradeType((String) map.get("tradeType"));
			    	// 会员信息ID
			    	pointChange.setMemberInfoId(memberInfoId);
			    	// 会员卡号
			    	pointChange.setMemCode(memberCode);
			    	// 积分变化日期
			    	pointChange.setChangeDate(initialMTTime);
			    	// 积分值 
			    	pointChange.setPoint(initMTPoint);
			    	// 员工ID
			    	pointChange.setEmployeeId(employeeId);
			    	// 组织结构ID
					Integer organizationId = null;
					if(counterInfo != null) {
						organizationId = Integer.parseInt(counterInfo.get("organizationID").toString());
					}
					pointChange.setOrganizationId(organizationId);
			    	// 会员积分变化明细
			    	PointChangeDetailDTO changeDetail = new PointChangeDetailDTO();
			    	// 积分值
			    	changeDetail.setPoint(initMTPoint);
			    	// 积分类型
			    	changeDetail.setPointType(pointType);
			    	// 理由
			    	changeDetail.setReason("初始积分录入");
			    	changeDetailList.add(changeDetail);
			    	pointChange.setChangeDetailList(changeDetailList);
			    	pointInfo.setPointChange(pointChange);
			    	campBaseDTO.setPointInfo(pointInfo);
			    	// 扩展信息
		    		Map<String, Object> extParams = changeDetail.getExtParams();
		    		if (null == extParams) {
		    			extParams = new HashMap<String, Object>();
		    			changeDetail.setExtParams(extParams);
		    		}
		    		extParams.put("PTMAINTYPE", "4");
			    	// 处理会员积分变化信息
			    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
			    	// 当前积分
			    	map.put("curTotalPoint", curPoint);
			    	// 可兑换积分
			    	map.put("curChangablePoint", curChangePoint);
			    	if (!isCurCard) {
				    	// 前卡积分值
			    		map.put("preCardPoint", preCardPoint);
			    	}
			    	// 更新会员积分信息表
			    	binOLCM31_BL.updateMemberPointInfo(map);
			    	// 设定重算次数为0
			    	map.put("reCalcCount", "0");
					// 设定理由为积分
			    	map.put("reason", DroolsConstants.REASON_5);
					// 设定计算日期
			    	map.put("calcDate", sysDate);
			    	// 插入规则执行履历表:累计积分
			    	// 履历区分 ：累计积分
			    	map.put("recordKbn", DroolsConstants.RECORDKBN_5);
			    	// 更新前的值
			    	map.put("oldValue", String.valueOf(totalPoint));
					// 更新后的值
			    	map.put("newValue", curPoint);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(map);
					// 插入规则执行履历表:可兑换积分
					// 履历区分 ：可兑换积分
					map.put("recordKbn", DroolsConstants.RECORDKBN_7);
			    	// 更新前的值
					map.put("oldValue", String.valueOf(ChangablePoint));
					// 更新后的值
					map.put("newValue", curChangePoint);
					// 插入规则执行履历表
					binBEMQMES08_Service.addRuleExecRecord(map);
					// 取得积分MQ消息体
					MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
					if(mqInfoDTO != null) {
						// 发送MQ消息处理
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
						// 组织代码
						pointRuleCalInfo.put("OrgCode", campBaseDTO.getOrgCode());
						// 品牌代码
						pointRuleCalInfo.put("BrandCode", campBaseDTO.getBrandCode());
						// 业务类型
						pointRuleCalInfo.put("TradeType", "MT");
						// 会员积分变化主ID
						pointRuleCalInfo.put("pointChangeId", pointChange.getPointChangeId());
						// 计算日期
						pointRuleCalInfo.put("changeDate", pointChange.getChangeDate());
						// 引起积分计算的单据号
						pointRuleCalInfo.put("billCode", pointChange.getTradeNoIF());
						// 引起积分计算的业务类型
						pointRuleCalInfo.put("billType", pointChange.getTradeType());
						// 购买金额
						pointRuleCalInfo.put("amount", pointChange.getAmount());
						// 购买数量
						pointRuleCalInfo.put("quantity", pointChange.getQuantity());
						// 获得积分
						pointRuleCalInfo.put("point", pointChange.getPoint());
						// 会员ID
						pointRuleCalInfo.put("memberInfoId", pointChange.getMemberInfoId());
						// 会员名称
						pointRuleCalInfo.put("name", campBaseDTO.getMemName());
						if(counterInfo != null) {
							// 部门ID
							pointRuleCalInfo.put("organizationId", counterInfo.get("organizationId"));
							// 部门名称
							pointRuleCalInfo.put("departName", counterInfo.get("counterName"));
						}
						// 所属柜台ID
						pointRuleCalInfo.put("MemOrganizationID", map.get("memOrganizationId"));
						// 组织ID
						String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
						// 品牌ID
						String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
						// 积分变化需要发送沟通MQ
						if(binOLCM14_BL.isConfigOpen("1088", orgIdStr, brandIdStr)) {
							// 发送积分变化沟通MQ的最低阀值
							String minPointStr = binOLCM14_BL.getConfigValue("1089", orgIdStr, brandIdStr);
							boolean gtFlag = true;
							if (!CherryChecker.isNullOrEmpty(minPointStr, true)) {
								minPointStr = minPointStr.trim();
								try {
									double minPoint = Double.parseDouble(minPointStr);
									// 当前积分小于最低阈值不发送沟通MQ
									if (pointInfo.getCurTotalPoint() < minPoint) {
										gtFlag = false;
									}
								} catch (Exception e) {
									gtFlag = false;
								}
							}
							if (gtFlag) {
								Map<String, Object> gtMap = new HashMap<String, Object>();
								// 组织ID
								gtMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
								// 品牌ID
								gtMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
								// 组织代码
								gtMap.put("orgCode", campBaseDTO.getOrgCode());
								// 品牌代码
								gtMap.put("brandCode", campBaseDTO.getBrandCode());
								// 事件ID
								gtMap.put("eventId", campBaseDTO.getMemberInfoId());
								// 事件类型:积分变化
								gtMap.put("eventType", "7");
								// 事件发生时间 
								gtMap.put("eventDate", pointChange.getChangeDate());
								// 信息内容:关联单号
								gtMap.put("messageContents", pointChange.getTradeNoIF());
								// 事件来源
								gtMap.put("sourse", "BINBEMQMES11");
								// 取得沟通短信消息体(实时)
								mqInfoDTO = binOLCM31_BL.getGTMQMessage(gtMap);
								// 发送MQ消息处理
								binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
							}
						}
					}
				}
			}
			//信息插入到MogoDB
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
			// 业务主体代号
			dbObject.put("TradeEntityCode", map.get("memberCode"));
			//发生时间
			dbObject.put("OccurTime", map.get("time"));
			// 操作员工
			dbObject.put("UserCode", map.get("employeeCode"));
			// 消息体
			dbObject.put("Content", map.get("messageBody"));
			map.put("dbObject", dbObject);
			if (!pointRuleCalInfo.isEmpty()) {
				map.put("pointRuleCalInfo", pointRuleCalInfo);
			}
		} else {
			// 没有此业务类型
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
		}
	}
	
	private void setInsertInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "BINBEMQMES13");
		map.put("createPGM", "BINBEMQMES13");
		map.put("updatedBy", "BINBEMQMES13");
		map.put("updatePGM", "BINBEMQMES13");
	}
}
