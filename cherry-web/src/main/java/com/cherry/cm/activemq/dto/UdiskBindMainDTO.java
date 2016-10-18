/*	
 * @(#)UdiskBindMainDTO.java     1.0 2011/12/14	
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
package com.cherry.cm.activemq.dto;

import java.util.List;

import com.cherry.cm.core.CherryConstants;

/**
 * U盘绑定信息MQ主业务 DTO
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class UdiskBindMainDTO extends MQBaseDTO {
	
	/** U盘绑定信息MQ明细业务 List */
	private List<UdiskBindDetailDTO> udiskBindDetailDTOList;
	
	public List<UdiskBindDetailDTO> getUdiskBindDetailDTOList() {
		return udiskBindDetailDTOList;
	}

	public void setUdiskBindDetailDTOList(
			List<UdiskBindDetailDTO> udiskBindDetailDTOList) {
		this.udiskBindDetailDTOList = udiskBindDetailDTOList;
	}

	/**
	 * 
	 * 取得MQ消息体
	 * 
	 * @return String
	 * 			MQ消息体
	 */
	public String getMQMsg() {
		
		StringBuffer buffer = new StringBuffer();
		// 版本号
		buffer.append(CherryConstants.MESSAGE_VERSION)
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			.append(CherryConstants.MESSAGE_TYPE_UDISKBIND)
			.append(CherryConstants.MESSAGE_LINE_BREAK)
			// 主数据字段名行
			.append(CherryConstants.MESSAGE_COMMLINE)
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			.append("BrandCode,TradeType,TradeNoIF")
			.append(CherryConstants.MESSAGE_LINE_BREAK)
			// 主数据行
			.append(CherryConstants.MAIN_MESSAGE_SIGN)
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			// 品牌代码
			.append(convertNullVal(this.getBrandCode()))
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			// 业务类型
			.append(convertNullVal(this.getTradeType()))
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			// 单据号
			.append(convertNullVal(this.getTradeNoIF()))
			.append(CherryConstants.MESSAGE_LINE_BREAK)
			// 明细字段名行
			.append(CherryConstants.MESSAGE_COMMLINE)
			.append(CherryConstants.MESSAGE_SPLIT_COMMA)
			.append("UDiskSN,Flag,Countercode")
			.append(CherryConstants.MESSAGE_LINE_BREAK);
		if (this.getUdiskBindDetailDTOList() != null && !this.getUdiskBindDetailDTOList().isEmpty()) {
			for (UdiskBindDetailDTO udiskBindDetailDTO : this.getUdiskBindDetailDTOList()) {
				// 明细数据行
				buffer.append(CherryConstants.DETAIL_MESSAGE_SIGN)
					.append(CherryConstants.MESSAGE_SPLIT_COMMA)
					// U盘序列号
					.append(convertNullVal(udiskBindDetailDTO.getUdiskSN()))
					.append(CherryConstants.MESSAGE_SPLIT_COMMA)
					// 区分
					.append(convertNullVal(udiskBindDetailDTO.getFlag()))
					.append(CherryConstants.MESSAGE_SPLIT_COMMA);
				// 柜台号按全角冒号分隔符连接成字符串
				List<String> countercodeList = udiskBindDetailDTO.getCountercode();
				StringBuffer countercodes = new StringBuffer();
				if(countercodeList != null && !countercodeList.isEmpty()) {
					for(String countercode : countercodeList) {
						if(countercode != null && !"".equals(countercode)) {
							countercodes.append(countercode).append(CherryConstants.MESSAGE_SPLIT_COLON);
						}
					}
				}
				String countercodeStr = countercodes.toString();
				if(countercodeStr.length() > 0) {
					countercodeStr = countercodeStr.substring(0, countercodeStr.length()-1);
				}
				buffer.append(countercodeStr)
					.append(CherryConstants.MESSAGE_LINE_BREAK);
			}
		}
		// 结尾
		buffer.append(CherryConstants.END_MESSAGE_SIGN);
		return buffer.toString();
	}

}
