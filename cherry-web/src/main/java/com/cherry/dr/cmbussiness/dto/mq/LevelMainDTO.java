/*	
 * @(#)LevelMainDTO.java     1.0 2012.04.10
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
package com.cherry.dr.cmbussiness.dto.mq;

import java.util.List;

import com.cherry.dr.cmbussiness.util.DroolsConstants;

/**
 * 等级MQ主业务 DTO
 * 
 * @author WangCT
 * @version 1.0 2012.04.10
 */
public class LevelMainDTO extends MQBaseDTO {
	
	/** 当前等级 */
	private String member_level;
	
	/** 计算时间 */
	private String caltime;
	
	/** 柜台号 */
	private String countercode;
	
	/** Ba卡号 */
	private String bacode;
	
	/** 开卡等级 */
	private String grantMemberLevel;
	
	/** 上一次等级 */
	private String prevLevel;
	
	/** 本次等级变化时间 */
	private String levelAdjustTime;
	
	/** 入会时间 */
	private String joinDate;
	
	/** 会员俱乐部代号 */
	private String clubCode;
	
	/** 等级MQ明细业务List */
	private List<LevelDetailDTO> levelDetailList;

	public String getMember_level() {
		return member_level;
	}

	public void setMember_level(String memberLevel) {
		member_level = memberLevel;
	}

	public String getCaltime() {
		return caltime;
	}

	public void setCaltime(String caltime) {
		this.caltime = caltime;
	}

	public String getCountercode() {
		return countercode;
	}

	public void setCountercode(String countercode) {
		this.countercode = countercode;
	}

	public String getBacode() {
		return bacode;
	}

	public void setBacode(String bacode) {
		this.bacode = bacode;
	}

	public String getGrantMemberLevel() {
		return grantMemberLevel;
	}

	public void setGrantMemberLevel(String grantMemberLevel) {
		this.grantMemberLevel = grantMemberLevel;
	}

	public String getPrevLevel() {
		return prevLevel;
	}

	public void setPrevLevel(String prevLevel) {
		this.prevLevel = prevLevel;
	}

	public String getLevelAdjustTime() {
		return levelAdjustTime;
	}

	public void setLevelAdjustTime(String levelAdjustTime) {
		this.levelAdjustTime = levelAdjustTime;
	}

	public List<LevelDetailDTO> getLevelDetailList() {
		return levelDetailList;
	}

	public void setLevelDetailList(List<LevelDetailDTO> levelDetailList) {
		this.levelDetailList = levelDetailList;
	}
	
	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
	public String getClubCode() {
		return clubCode;
	}

	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}

	/**
	 * 取得MQ消息体
	 * 
	 * @param
	 * @return String
	 * 			MQ消息体
	 * 
	 */
	public String getMQMsg() {
		// 修改回数
		String modifyCounts = this.getModifyCounts();
		if (null == modifyCounts || "".equals(modifyCounts)) {
			modifyCounts = DroolsConstants.DEF_MODIFYCOUNTS;
		}
		StringBuffer buffer = new StringBuffer();
		// 版本号
		buffer.append("[Version]")
			.append(DroolsConstants.SPLIT1)
			.append("AMQ.004.002")
			.append(DroolsConstants.LINE_BREAK)
			// 命令类型
			.append("[Type]")
			.append(DroolsConstants.SPLIT1)
			.append("")
			.append(DroolsConstants.LINE_BREAK)
			// 数据类型
			.append("[DataType]")
			.append(DroolsConstants.SPLIT1)
			.append("text/plain")
			.append(DroolsConstants.LINE_BREAK)
			// 主数据字段名行
			.append("[CommLine],BrandCode,TradeNoIF,MemberID,MemberCode,ModifyCounts,TradeType,SubType,Member_level,Caltime,Countercode,BAcode,GrantMemberLevel,PrevLevel,LevelAdjustTime,JoinDate")
			.append(DroolsConstants.LINE_BREAK)
			// 主数据行
			.append(DroolsConstants.MQ_MAINDATALINE)
			.append(DroolsConstants.SPLIT1)
			// 品牌代码
			.append(convertNullVal(this.getBrandCode()))
			.append(DroolsConstants.SPLIT1)
			// 单据号
			.append(convertNullVal(this.getTradeNoIF()))
			.append(DroolsConstants.SPLIT1)
			// 会员唯一号
			.append(convertNullVal(this.getMemberID()))
			.append(DroolsConstants.SPLIT1)
			// 会员卡号
			.append(convertNullVal(this.getMemberCode()))
			.append(DroolsConstants.SPLIT1)
			// 修改回数
			.append(modifyCounts)
			.append(DroolsConstants.SPLIT1)
			// 业务类型
			.append(DroolsConstants.MQ_BILLTYPE_ML)
			.append(DroolsConstants.SPLIT1)
			// 子类型
			.append(convertNullVal(this.getSubType()))
			.append(DroolsConstants.SPLIT1)
			// 当前等级
			.append(convertNullVal(this.getMember_level()))
			.append(DroolsConstants.SPLIT1)
			// 计算时间
			.append(convertNullVal(this.getCaltime()))
			.append(DroolsConstants.SPLIT1)
			// 柜台号
			.append(convertNullVal(this.getCountercode()))
			.append(DroolsConstants.SPLIT1)
			// Ba卡号
			.append(convertNullVal(this.getBacode()))
			.append(DroolsConstants.SPLIT1)
			// 开卡等级
			.append(convertNullVal(this.getGrantMemberLevel()))
			.append(DroolsConstants.SPLIT1)
			// 上一次等级
			.append(convertNullVal(this.getPrevLevel()))
			.append(DroolsConstants.SPLIT1)
			// 本次等级变化时间
			.append(convertNullVal(this.getLevelAdjustTime()))
			.append(DroolsConstants.SPLIT1)
			// 本次等级变化时间
			.append(convertNullVal(this.getJoinDate()))
			.append(DroolsConstants.LINE_BREAK)
			// 明细字段名行
			.append("[CommLine],MemberCode,OperateType,MemberlevelOld,MemberlevelNew,ChangeType,CounterCode,EmployeeCode,BizType,RelevantTicketDate,RelevantNo,Channel,Reason,ReCalcCount,TotalAmount")
			.append(DroolsConstants.LINE_BREAK);
		if (null != this.levelDetailList) {
			for (LevelDetailDTO levelDetailDTO : levelDetailList) {
				// 明细数据行
				buffer.append(DroolsConstants.MQ_DETAILDATALINE)
					.append(DroolsConstants.SPLIT1)
					// 会员号
					.append(convertNullVal(levelDetailDTO.getMemberCode()))
					.append(DroolsConstants.SPLIT1)
					// 操作类型
					.append(convertNullVal(levelDetailDTO.getOperateType()))
					.append(DroolsConstants.SPLIT1)
					// 变更前等级
					.append(convertNullVal(levelDetailDTO.getMemberlevelOld()))
					.append(DroolsConstants.SPLIT1)
					// 变更后等级
					.append(convertNullVal(levelDetailDTO.getMemberlevelNew()))
					.append(DroolsConstants.SPLIT1)
					// 变化类型
					.append(convertNullVal(levelDetailDTO.getChangeType()))
					.append(DroolsConstants.SPLIT1)
					// 柜台号
					.append(convertNullVal(levelDetailDTO.getCounterCode()))
					.append(DroolsConstants.SPLIT1)
					// 员工编号
					.append(convertNullVal(levelDetailDTO.getEmployeeCode()))
					.append(DroolsConstants.SPLIT1)
					// 业务类型
					.append(convertNullVal(levelDetailDTO.getBizType()))
					.append(DroolsConstants.SPLIT1)
					// 关联单据时间
					.append(convertNullVal(levelDetailDTO.getRelevantTicketDate()))
					.append(DroolsConstants.SPLIT1)
					// 关联单号
					.append(convertNullVal(levelDetailDTO.getRelevantNo()))
					.append(DroolsConstants.SPLIT1)
					// 变动渠道
					.append(convertNullVal(levelDetailDTO.getChannel()))
					.append(DroolsConstants.SPLIT1)
					// 变化原因
					.append(convertNullVal(levelDetailDTO.getReason()))
					.append(DroolsConstants.SPLIT1)
					// 重算次数
					.append(convertNullVal(levelDetailDTO.getReCalcCount()))
					.append(DroolsConstants.SPLIT1)
					// 累积金额
					.append(convertNullVal(levelDetailDTO.getTotalAmount()))
					.append(DroolsConstants.LINE_BREAK);
			}
		}
		// 结尾
		buffer.append(DroolsConstants.MQ_END);
		return buffer.toString();
	}
	
	/**
	 * 取得MQ消息体(会员俱乐部)
	 * 
	 * @param
	 * @return String
	 * 			MQ消息体
	 * 
	 */
	public String getClubMQMsg() {
		// 修改回数
		String modifyCounts = this.getModifyCounts();
		if (null == modifyCounts || "".equals(modifyCounts)) {
			modifyCounts = DroolsConstants.DEF_MODIFYCOUNTS;
		}
		StringBuffer buffer = new StringBuffer();
		// 版本号
		buffer.append("[Version]")
			.append(DroolsConstants.SPLIT1)
			.append("AMQ.004.003")
			.append(DroolsConstants.LINE_BREAK)
			// 命令类型
			.append("[Type]")
			.append(DroolsConstants.SPLIT1)
			.append("")
			.append(DroolsConstants.LINE_BREAK)
			// 数据类型
			.append("[DataType]")
			.append(DroolsConstants.SPLIT1)
			.append("text/plain")
			.append(DroolsConstants.LINE_BREAK)
			// 主数据字段名行
			.append("[CommLine],BrandCode,ClubCode,TradeNoIF,MemberID,MemberCode,ModifyCounts,TradeType,SubType,Member_level,Caltime,Countercode,BAcode,GrantMemberLevel,PrevLevel,LevelAdjustTime,JoinDate")
			.append(DroolsConstants.LINE_BREAK)
			// 主数据行
			.append(DroolsConstants.MQ_MAINDATALINE)
			.append(DroolsConstants.SPLIT1)
			// 品牌代码
			.append(convertNullVal(this.getBrandCode()))
			.append(DroolsConstants.SPLIT1)
			// 会员俱乐部代号
			.append(convertNullVal(this.getClubCode()))
			.append(DroolsConstants.SPLIT1)
			// 单据号
			.append(convertNullVal(this.getTradeNoIF()))
			.append(DroolsConstants.SPLIT1)
			// 会员唯一号
			.append(convertNullVal(this.getMemberID()))
			.append(DroolsConstants.SPLIT1)
			// 会员卡号
			.append(convertNullVal(this.getMemberCode()))
			.append(DroolsConstants.SPLIT1)
			// 修改回数
			.append(modifyCounts)
			.append(DroolsConstants.SPLIT1)
			// 业务类型
			.append(DroolsConstants.MQ_BILLTYPE_ML)
			.append(DroolsConstants.SPLIT1)
			// 子类型
			.append(convertNullVal(this.getSubType()))
			.append(DroolsConstants.SPLIT1)
			// 当前等级
			.append(convertNullVal(this.getMember_level()))
			.append(DroolsConstants.SPLIT1)
			// 计算时间
			.append(convertNullVal(this.getCaltime()))
			.append(DroolsConstants.SPLIT1)
			// 柜台号
			.append(convertNullVal(this.getCountercode()))
			.append(DroolsConstants.SPLIT1)
			// Ba卡号
			.append(convertNullVal(this.getBacode()))
			.append(DroolsConstants.SPLIT1)
			// 开卡等级
			.append(convertNullVal(this.getGrantMemberLevel()))
			.append(DroolsConstants.SPLIT1)
			// 上一次等级
			.append(convertNullVal(this.getPrevLevel()))
			.append(DroolsConstants.SPLIT1)
			// 本次等级变化时间
			.append(convertNullVal(this.getLevelAdjustTime()))
			.append(DroolsConstants.SPLIT1)
			// 本次等级变化时间
			.append(convertNullVal(this.getJoinDate()))
			.append(DroolsConstants.LINE_BREAK)
			// 明细字段名行
			.append("[CommLine],MemberCode,OperateType,MemberlevelOld,MemberlevelNew,ChangeType,CounterCode,EmployeeCode,BizType,RelevantTicketDate,RelevantNo,Channel,Reason,ReCalcCount,TotalAmount")
			.append(DroolsConstants.LINE_BREAK);
		if (null != this.levelDetailList) {
			for (LevelDetailDTO levelDetailDTO : levelDetailList) {
				// 明细数据行
				buffer.append(DroolsConstants.MQ_DETAILDATALINE)
					.append(DroolsConstants.SPLIT1)
					// 会员号
					.append(convertNullVal(levelDetailDTO.getMemberCode()))
					.append(DroolsConstants.SPLIT1)
					// 操作类型
					.append(convertNullVal(levelDetailDTO.getOperateType()))
					.append(DroolsConstants.SPLIT1)
					// 变更前等级
					.append(convertNullVal(levelDetailDTO.getMemberlevelOld()))
					.append(DroolsConstants.SPLIT1)
					// 变更后等级
					.append(convertNullVal(levelDetailDTO.getMemberlevelNew()))
					.append(DroolsConstants.SPLIT1)
					// 变化类型
					.append(convertNullVal(levelDetailDTO.getChangeType()))
					.append(DroolsConstants.SPLIT1)
					// 柜台号
					.append(convertNullVal(levelDetailDTO.getCounterCode()))
					.append(DroolsConstants.SPLIT1)
					// 员工编号
					.append(convertNullVal(levelDetailDTO.getEmployeeCode()))
					.append(DroolsConstants.SPLIT1)
					// 业务类型
					.append(convertNullVal(levelDetailDTO.getBizType()))
					.append(DroolsConstants.SPLIT1)
					// 关联单据时间
					.append(convertNullVal(levelDetailDTO.getRelevantTicketDate()))
					.append(DroolsConstants.SPLIT1)
					// 关联单号
					.append(convertNullVal(levelDetailDTO.getRelevantNo()))
					.append(DroolsConstants.SPLIT1)
					// 变动渠道
					.append(convertNullVal(levelDetailDTO.getChannel()))
					.append(DroolsConstants.SPLIT1)
					// 变化原因
					.append(convertNullVal(levelDetailDTO.getReason()))
					.append(DroolsConstants.SPLIT1)
					// 重算次数
					.append(convertNullVal(levelDetailDTO.getReCalcCount()))
					.append(DroolsConstants.SPLIT1)
					// 累积金额
					.append(convertNullVal(levelDetailDTO.getTotalAmount()))
					.append(DroolsConstants.LINE_BREAK);
			}
		}
		// 结尾
		buffer.append(DroolsConstants.MQ_END);
		return buffer.toString();
	}
}
