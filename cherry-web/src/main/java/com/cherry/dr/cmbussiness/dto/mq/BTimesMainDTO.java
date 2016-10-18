/*	
 * @(#)BTimesMainDTO.java     1.0 2011/05/12	
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
 * 化妆次数MQ主业务 DTO
 * 
 * @author WangCT
 * @version 1.0 2012.04.10
 */
public class BTimesMainDTO extends MQBaseDTO {

	/** 当前化妆次数 */
	private String curBtimes;
	
	/** 计算时间 */
	private String caltime;
	
	/** 化妆次数MQ明细业务List */
	private List<BTimesDetailDTO> btimesDetailList;

	public String getCurBtimes() {
		return curBtimes;
	}

	public void setCurBtimes(String curBtimes) {
		this.curBtimes = curBtimes;
	}

	public String getCaltime() {
		return caltime;
	}

	public void setCaltime(String caltime) {
		this.caltime = caltime;
	}

	public List<BTimesDetailDTO> getBtimesDetailList() {
		return btimesDetailList;
	}

	public void setBtimesDetailList(List<BTimesDetailDTO> btimesDetailList) {
		this.btimesDetailList = btimesDetailList;
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
			.append("AMQ.002.002")
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
			.append("[CommLine],BrandCode,TradeNoIF,MemberID,MemberCode,ModifyCounts,TradeType,SubType,CurBtimes,Caltime")
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
			.append(DroolsConstants.MQ_BILLTYPE_MG)
			.append(DroolsConstants.SPLIT1)
			// 子类型
			.append(convertNullVal(this.getSubType()))
			.append(DroolsConstants.SPLIT1)
			// 当前化妆次数
			.append(convertNullVal(this.getCurBtimes()))
			.append(DroolsConstants.SPLIT1)
			// 计算时间
			.append(convertNullVal(this.getCaltime()))
			.append(DroolsConstants.LINE_BREAK)
			// 明细字段名行
			.append("[CommLine],MemberCode,OperateType,BtimesOld,CurBtimesNew,DiffBtimes,CounterCode,EmployeeCode,BizType,RelevantTicketDate,RelevantNo,Channel,Reason,ReCalcCount")
			.append(DroolsConstants.LINE_BREAK);
		if (null != this.btimesDetailList) {
			for (BTimesDetailDTO btimesDetailDTO : btimesDetailList) {
				// 明细数据行
				buffer.append(DroolsConstants.MQ_DETAILDATALINE)
					.append(DroolsConstants.SPLIT1)
					// 会员号
					.append(convertNullVal(btimesDetailDTO.getMemberCode()))
					.append(DroolsConstants.SPLIT1)
					// 操作类型
					.append(convertNullVal(btimesDetailDTO.getOperateType()))
					.append(DroolsConstants.SPLIT1)
					// 变更前化妆次数
					.append(convertNullVal(btimesDetailDTO.getBtimesOld()))
					.append(DroolsConstants.SPLIT1)
					// 变更后化妆次数
					.append(convertNullVal(btimesDetailDTO.getCurBtimesNew()))
					.append(DroolsConstants.SPLIT1)
					// 化妆次数差分
					.append(convertNullVal(btimesDetailDTO.getDiffBtimes()))
					.append(DroolsConstants.SPLIT1)
					// 柜台号
					.append(convertNullVal(btimesDetailDTO.getCounterCode()))
					.append(DroolsConstants.SPLIT1)
					// 员工编号
					.append(convertNullVal(btimesDetailDTO.getEmployeeCode()))
					.append(DroolsConstants.SPLIT1)
					// 业务类型
					.append(convertNullVal(btimesDetailDTO.getBizType()))
					.append(DroolsConstants.SPLIT1)
					// 关联单据时间
					.append(convertNullVal(btimesDetailDTO.getRelevantTicketDate()))
					.append(DroolsConstants.SPLIT1)
					// 关联单号
					.append(convertNullVal(btimesDetailDTO.getRelevantNo()))
					.append(DroolsConstants.SPLIT1)
					// 变动渠道
					.append(convertNullVal(btimesDetailDTO.getChannel()))
					.append(DroolsConstants.SPLIT1)
					// 变化原因
					.append(convertNullVal(btimesDetailDTO.getReason()))
					.append(DroolsConstants.SPLIT1)
					// 重算次数
					.append(convertNullVal(btimesDetailDTO.getReCalcCount()))
					.append(DroolsConstants.LINE_BREAK);
			}
		}
		// 结尾
		buffer.append(DroolsConstants.MQ_END);
		return buffer.toString();
	}
}
