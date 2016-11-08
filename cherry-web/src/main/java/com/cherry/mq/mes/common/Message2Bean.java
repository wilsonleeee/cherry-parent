/*		
 * @(#)Message2Bean.java     1.0 2010/12/01		
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
package com.cherry.mq.mes.common;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.mq.mes.dto.BaDetailDataDTO;
import com.cherry.mq.mes.dto.BasDetailDataDTO;
import com.cherry.mq.mes.dto.MachMainDataDTO;
import com.cherry.mq.mes.dto.MemDetailDataDTO;
import com.cherry.mq.mes.dto.MemInitDataDTO;
import com.cherry.mq.mes.dto.MemMainDataDTO;
import com.cherry.mq.mes.dto.MemUsedDetailDTO;
import com.cherry.mq.mes.dto.MemVisitDetailDataDTO;
import com.cherry.mq.mes.dto.MonMainDataDTO;
import com.cherry.mq.mes.dto.QueDetailDataDTO;
import com.cherry.mq.mes.dto.QueMainDataDTO;
import com.cherry.mq.mes.dto.RivalSaleDetailDataDTO;
import com.cherry.mq.mes.dto.RivalSaleMainDataDTO;
import com.cherry.mq.mes.dto.SalDetailDataDTO;
import com.cherry.mq.mes.dto.SalMainDataDTO;
import com.cherry.mq.mes.dto.SaleReturnDetailDataDTO;
import com.cherry.mq.mes.dto.SaleReturnMainDataDTO;

/**
 * 将消息转换成bean
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class Message2Bean {

	/**
	 * 根据type类型将MQ消息转化为指定DTO【某一消息体新增字段时需要将此字段在DTO中设定】
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static Object parseMessage(String msg) throws Exception {
		if (!checkMessage(msg)) {
			return null;
		}

		Object mainDataObj = null;
		// 判断消息类型
		if (msg.indexOf(MessageConstants.MESSAGE_TYPE_SALE_STOCK) > 0) {
			SalMainDataDTO salMainDataDTO = new SalMainDataDTO();
			// 消息体
			salMainDataDTO.setMessageBody(msg);
			salMainDataDTO.setDetailDataDTOList(new ArrayList());

			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setSalMainDataDTO(dataStr, salMainDataDTO);
			}

			dataStrList = MessageUtil.matchMessageStr(1, msg);
			for (String dataStr : dataStrList) {
				setSalDetailDataDTO(dataStr, salMainDataDTO);
			}
			mainDataObj = salMainDataDTO;
		} else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_MEMBER_BA) > 0) {
			boolean fromCRM = false;
			// 判断是否来自CRM的消息体
			if(msg.indexOf("[DataType],CRM") > 0) {
				fromCRM = true;
			}
			MemMainDataDTO memMainDataDTO = new MemMainDataDTO();
			// 消息体
			memMainDataDTO.setMessageBody(msg);
			memMainDataDTO.setDetailDataDTOList(new ArrayList());
			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setMemMainDataDTO(dataStr, memMainDataDTO);
			}
			dataStrList = MessageUtil.matchMessageStr(1, msg);
			for (String dataStr : dataStrList) {
				// 来自CRM的消息体需要把消息内容进行URL解码处理
				if(fromCRM) {
					dataStr = URLDecoder.decode(dataStr, "UTF-8");
				}
				// 根据业务类型判断
				String tradeType = memMainDataDTO.getTradeType();
				if (MessageConstants.MSG_MEMBER.equals(tradeType)) {
					// 会员明细数据设置
					setMemDetailDataDTO(dataStr, memMainDataDTO);
				} else if (MessageConstants.MSG_BA_INFO.equals(tradeType)) {
					// BA明细数据设置
					setBaDetailDataDTO(dataStr, memMainDataDTO);
				} else if (MessageConstants.MSG_BAS_INFO.equals(tradeType)) {
					// BAS明细数据设置
					setBasDetailDataDTO(dataStr, memMainDataDTO);
				} else if (MessageConstants.MSG_MEMBER_MS.equals(tradeType)) {
					// 会员初始数据导入明细数据设置
					setMemInitDataDTO(dataStr, memMainDataDTO);
				} else if (MessageConstants.MSG_MEMBER_BU.equals(tradeType)) {
					// 化妆次数使用明细数据设置
					setMemUsedDetailDTO(dataStr, memMainDataDTO);
				} else if (MessageConstants.MSG_MEMBER_MV.equals(tradeType)) {
					// 会员回访明细数据设置
					setMemVisitDetailDTO(dataStr, memMainDataDTO);
				} else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("messageBody", msg);
					String strTradeType=tradeType==null?"":"。业务类型为\""+tradeType+"\"";
					String strTradeNoIF=memMainDataDTO.getTradeNoIF()==null?"":"，单据号为\""+memMainDataDTO.getTradeNoIF()+"\"";
					// 没有此业务类型
					MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27+strTradeType+strTradeNoIF);
				}
			}
			mainDataObj = memMainDataDTO;
		} else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_MACHINE) > 0) {
			MonMainDataDTO monMainDataDTO = new MonMainDataDTO();
			// 消息体
			monMainDataDTO.setMessageBody(msg);
			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setMonMainDataDTO(dataStr, monMainDataDTO);
			}
			mainDataObj = monMainDataDTO;
		} else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_MEMBER_QS) > 0){
			QueMainDataDTO queMainDataDTO = new QueMainDataDTO();
			// 消息体
			queMainDataDTO.setMessageBody(msg);
			queMainDataDTO.setDetailDataDTOList(new ArrayList());

			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setQueMainDataDTO(dataStr, queMainDataDTO);
			}

			dataStrList = MessageUtil.matchMessageStr(1, msg);
			for (String dataStr : dataStrList) {
				setQusDetailDataDTO(dataStr, queMainDataDTO);
			}
			mainDataObj = queMainDataDTO;
		} else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_MEMBER_RS) > 0){
			RivalSaleMainDataDTO rivalSaleMainDataDTO = new RivalSaleMainDataDTO();
			// 消息体
			rivalSaleMainDataDTO.setMessageBody(msg);
			rivalSaleMainDataDTO.setDetailDataDTOList(new ArrayList());

			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setRivalSaleMainDataDTO(dataStr,rivalSaleMainDataDTO);
			}

			dataStrList = MessageUtil.matchMessageStr(1, msg);
			for (String dataStr : dataStrList) {
				setRivalSaleDetailDataDTO(dataStr, rivalSaleMainDataDTO);
			}
			mainDataObj = rivalSaleMainDataDTO;
		}else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_MACH_MI) > 0){
			//机器信息
			MachMainDataDTO machMainDataDTO = new MachMainDataDTO();
			// 消息体
			machMainDataDTO.setMessageBody(msg);

			List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
			for (String dataStr : dataStrList) {
				setMachMainDataDTO(dataStr,machMainDataDTO);
			}
			mainDataObj = machMainDataDTO;
		}else if (msg.indexOf(MessageConstants.MESSAGE_TYPE_SALE_RETURN) > 0){
            SaleReturnMainDataDTO saleReturnMainDataDTO = new SaleReturnMainDataDTO();
            // 新销售退货消息体【旧销售消息体目前主要是库存相关的MQ】
            saleReturnMainDataDTO.setMessageBody(msg);
            saleReturnMainDataDTO.setDetailDataDTOList(new ArrayList());

            List<String> dataStrList = MessageUtil.matchMessageStr(0, msg);
            for (String dataStr : dataStrList) {
            	// 设定MQ的主数据字段值
                setSaleReturnMainDataDTO(dataStr, saleReturnMainDataDTO);
            }

            dataStrList = MessageUtil.matchMessageStr(1, msg);
            for (String dataStr : dataStrList) {
            	// 设定MQ的明细数据字段值
                setSaleReturnDetailDataDTO(dataStr, saleReturnMainDataDTO);
            }
            mainDataObj = saleReturnMainDataDTO;
		}else{
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("messageBody", msg);
			String[] str = msg.split("\r\n");
			String t = str.length>1?"。命令类型为\""+str[1]+"\"":"";
			// 没有此命令类型
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_28+t);
		}

		return mainDataObj;
	}

	/**
	 * 验证消息
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 * @throws CherryBatchException
	 */
	private static boolean checkMessage(String msg) throws CherryMQException {
		// 验证版本信息
		if (msg.indexOf(MessageConstants.MESSAGE_VERSION) < 0) {
			// 无版本
			throw new CherryMQException(MessageConstants.MSG_ERROR_01);
		}

		// 验证消息类型信息
		if (msg.indexOf(MessageConstants.MESSAGE_TYPE_SIGN) < 0) {
			// 无版本
			throw new CherryMQException(MessageConstants.MSG_ERROR_01);
		}

		// 验证主数据
		if (msg.indexOf(MessageConstants.MAIN_MESSAGE_SIGN) < 0) {
			// 主数据解析异常
			throw new CherryMQException(MessageConstants.MSG_ERROR_02);
		}
            
		 //TODO：原先是注销的，现暂时放开，当以后有不需要明细记录的消息时，再进行修改。
		 // 验证明细数据
//		 if (msg.indexOf(MessageConstants.DETAIL_MESSAGE_SIGN) < 0) {
//			 // 明细数据解析异常
//			 throw new CherryMQException(MessageConstants.MSG_ERROR_03);
//		 }

		// 验证消息完整性(结束符)
		if (!(msg.split("\n")[msg.split("\n").length - 1]).equals(MessageConstants.END_MESSAGE_SIGN)) {
			// 最后一行不是结束标记符
			throw new CherryMQException(MessageConstants.MSG_ERROR_04);
		}

		return true;
	}

	/**
	 * 设定SalMainDataDTO
	 * @param mainDataStr
	 * @param salMainDataDTO
	 */
	private static void setSalMainDataDTO(String mainDataStr, SalMainDataDTO salMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				salMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				salMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
				break;

			case 2:
				salMainDataDTO.setModifyCounts(mainDataStrArr[i]);
				break;

			case 3:
				salMainDataDTO.setCounterCode(mainDataStrArr[i]);
				break;

			case 4:
				salMainDataDTO.setRelevantCounterCode(mainDataStrArr[i]);
				break;

			case 5:
				salMainDataDTO.setTotalQuantity(mainDataStrArr[i]);
				break;

			case 6:
				salMainDataDTO.setTotalAmount(mainDataStrArr[i]);
				break;

			case 7:
				salMainDataDTO.setTradeType(mainDataStrArr[i]);
				break;

			case 8:
				salMainDataDTO.setSubType(mainDataStrArr[i]);
				break;

			case 9:
				salMainDataDTO.setRelevantNo(mainDataStrArr[i]);
				break;

			case 10:
				salMainDataDTO.setReason(mainDataStrArr[i]);
				break;

			case 11:
				salMainDataDTO.setTradeDate(mainDataStrArr[i]);
				break;

			case 12:
				salMainDataDTO.setTradeTime(mainDataStrArr[i]);
				break;

			case 13:
				salMainDataDTO.setTotalAmountBefore(mainDataStrArr[i]);
				break;

			case 14:
				salMainDataDTO.setTotalAmountAfter(mainDataStrArr[i]);
				break;

			case 15:
				salMainDataDTO.setMemberCode(mainDataStrArr[i]);
				break;

			case 16:
                if(MessageConstants.MSG_BIR_PRESENT.equals(salMainDataDTO.getTradeType())){
                    //礼品领用消息体-获知活动方式 
                    salMainDataDTO.setInformType(mainDataStrArr[i]);
                }else if(MessageConstants.MSG_PRO_ORDER.equals(salMainDataDTO.getTradeType())){
                    //产品订货单消息体-期望发货日期
                    salMainDataDTO.setExpectDeliverDate(mainDataStrArr[i]);
                }else if(MessageConstants.MSG_ALLOCATION_IN.equals(salMainDataDTO.getTradeType())){
                    //调入申请消息体-是否启用了工作流
                    salMainDataDTO.setWorkFlow(mainDataStrArr[i]);
                }else if(MessageConstants.MSG_ALLOCATION_OUT.equals(salMainDataDTO.getTradeType())){
                    //调出申请消息体-是否启用了工作流
                    salMainDataDTO.setWorkFlow(mainDataStrArr[i]);
                } else if(MessageConstants.MSG_STOCK_TAKING.equals(salMainDataDTO.getTradeType())){
                	// 盘点单支持盘点原因CODE
                    salMainDataDTO.setStockReason(mainDataStrArr[i]);
                } else if(MessageConstants.MSG_KS_DELIVER.equals(salMainDataDTO.getTradeType())){
                	// K3导入发货单/退库单的出库部门代号
                	salMainDataDTO.setOutDepartCode(mainDataStrArr[i]);
                } else {
                    salMainDataDTO.setCounter_ticket_code(mainDataStrArr[i]);
                }
                break;

			case 17:
				if(MessageConstants.MSG_STOCK_TAKING.equals(salMainDataDTO.getTradeType())) {
					// 盘点单无明细时需要在主单中给到BAcode
					salMainDataDTO.setBAcode(mainDataStrArr[i]);
				} else {
					salMainDataDTO.setCounter_ticket_code_pre(mainDataStrArr[i]);
				}
				break;

			case 18:
				salMainDataDTO.setTicket_type(mainDataStrArr[i]);
				break;

			case 19:
				salMainDataDTO.setSale_status(mainDataStrArr[i]);
				break;

			case 20:
				salMainDataDTO.setConsumer_type(mainDataStrArr[i]);
				break;

			case 21:
				salMainDataDTO.setMember_level(mainDataStrArr[i]);
				break;

			case 22:
				salMainDataDTO.setOriginal_amount(mainDataStrArr[i]);
				break;

			case 23:
				salMainDataDTO.setDiscount(mainDataStrArr[i]);
				break;

			case 24:
				salMainDataDTO.setPay_amount(mainDataStrArr[i]);
				break;

			case 25:
				salMainDataDTO.setDecrease_amount(mainDataStrArr[i]);
				break;

			case 26:
				salMainDataDTO.setCostpoint(mainDataStrArr[i]);
				break;

			case 27:
				salMainDataDTO.setCostpoint_amount(mainDataStrArr[i]);
				break;

			case 28:
				salMainDataDTO.setSale_ticket_time(mainDataStrArr[i]);
				break;

			case 29:
				salMainDataDTO.setData_source(mainDataStrArr[i]);
				break;

			case 30:
				salMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;

			case 31:
				salMainDataDTO.setSaleSRtype(mainDataStrArr[i]);
				break;
			}
		}
	}

	/**
	 * 设定SalDetailDataDTO
	 * @param detailDataStr
	 * @param salMainDataDTO
	 */
	private static void setSalDetailDataDTO(String detailDataStr, SalMainDataDTO salMainDataDTO) {
		List<SalDetailDataDTO> detailDataDTOList = salMainDataDTO.getDetailDataDTOList();
		SalDetailDataDTO salDetailDataDTO = new SalDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				salDetailDataDTO.setTradeNoIF(detailDataStrArr[i]);
				break;

			case 1:
				salDetailDataDTO.setModifyCounts(detailDataStrArr[i]);
				break;

			case 2:
				salDetailDataDTO.setBAcode(detailDataStrArr[i]);
				break;

			case 3:
				salDetailDataDTO.setStockType(detailDataStrArr[i]);
				break;

			case 4:
				salDetailDataDTO.setBarcode(detailDataStrArr[i]);
				break;

			case 5:
				salDetailDataDTO.setUnitcode(detailDataStrArr[i]);
				break;

			case 6:
				salDetailDataDTO.setInventoryTypeCode(detailDataStrArr[i]);
				break;

			case 7:
				salDetailDataDTO.setQuantity(detailDataStrArr[i]);
				break;

			case 8:
				salDetailDataDTO.setQuantityBefore(detailDataStrArr[i]);
				break;

			case 9:
				salDetailDataDTO.setPrice(detailDataStrArr[i]);
				break;

			case 10:
				salDetailDataDTO.setReason(detailDataStrArr[i]);
				break;

			case 11:
                if(MessageConstants.MSG_BIR_PRESENT.equals(salMainDataDTO.getTradeType())){
                    //礼品领用-礼品领用时的Coupon号 
                    salDetailDataDTO.setCouponCode(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_STOCK_IN_OUT.equals(salMainDataDTO.getTradeType())) {
                	// 入库/退库单明细的参考价格
                	salDetailDataDTO.setReferencePrice(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_PRO_ORDER.equals(salMainDataDTO.getTradeType())) {
                	// 订货单明细的建议订货数量
                	salDetailDataDTO.setSuggestedQuantity(detailDataStrArr[i]);
                } else{
                	// 库存业务并没有整单折扣这个字段【这是销售老格式遗留字段】
//                    salDetailDataDTO.setDiscount(detailDataStrArr[i]);
                	// 其他没有特殊情况的业务的序号为【11】的值固定为明细类型
                	salDetailDataDTO.setDetailType(detailDataStrArr[i]);
                }
                break;
				
			case 12:
                if(MessageConstants.MSG_BIR_PRESENT.equals(salMainDataDTO.getTradeType())){
                    //礼品领用-是否管理库存 
                    salDetailDataDTO.setIsStock(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_STOCK_IN_OUT.equals(salMainDataDTO.getTradeType())) {
                	// 入库/退库单--明细类型
                	salDetailDataDTO.setDetailType(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_PRO_ORDER.equals(salMainDataDTO.getTradeType())) {
                	// 订货单明细--明细类型
                	salDetailDataDTO.setDetailType(detailDataStrArr[i]);
                } else {
                	// 其他没有特殊情况的业务的序号为【12】的值固定为产品厂商ID
                	salDetailDataDTO.setProductId(detailDataStrArr[i]);
                }
                break;
			case 13 : 
				if(MessageConstants.MSG_BIR_PRESENT.equals(salMainDataDTO.getTradeType())){
                    //礼品领用--明细类型
                    salDetailDataDTO.setDetailType(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_STOCK_IN_OUT.equals(salMainDataDTO.getTradeType())) {
                	// 入库/退库单--产品厂商ID
                	salDetailDataDTO.setProductId(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_PRO_ORDER.equals(salMainDataDTO.getTradeType())) {
                	// 订货单明细--产品厂商ID
                	salDetailDataDTO.setProductId(detailDataStrArr[i]);
                } else {
                	// 其他没有特殊情况的业务的序号为【13】的值目前还没有
                }
				break;
			case 14 : 
				if(MessageConstants.MSG_BIR_PRESENT.equals(salMainDataDTO.getTradeType())){
                    //礼品领用-产品厂商ID
                    salDetailDataDTO.setProductId(detailDataStrArr[i]);
                } else if(MessageConstants.MSG_STOCK_IN_OUT.equals(salMainDataDTO.getTradeType())) {
                	// 入库/退库单--序号为【14】的字段目前还没有
                } else if(MessageConstants.MSG_PRO_ORDER.equals(salMainDataDTO.getTradeType())) {
                	// 订货单明细--序号为【14】的字段目前还没有
                } else {
                	// 其他没有特殊情况的业务的序号为【14】的值目前还没有
                }
				break;

			}
		}

		detailDataDTOList.add(salDetailDataDTO);

	}

	/**
	 * 设定MemMainDataDTO
	 * 
	 * @param mainDataStr
	 * @param memMainDataDTO
	 */
	private static void setMemMainDataDTO(String mainDataStr, MemMainDataDTO memMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				memMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				memMainDataDTO.setTradeType(mainDataStrArr[i]);
				break;

			case 2:
				memMainDataDTO.setSubType(mainDataStrArr[i]);
				break;

			case 3:
				memMainDataDTO.setSourse(mainDataStrArr[i]);
				break;

			case 4:
				memMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;
				
			case 5:
				memMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
				break;
				
			case 6:
				memMainDataDTO.setTradeCounterCode(mainDataStrArr[i]);
				break;
				
			case 7:
				memMainDataDTO.setTradeBAcode(mainDataStrArr[i]);
				break;	
			}
		}
	}

	/**
	 * 设定MemDetailDataDTO
	 * 
	 * @param detailDataStr
	 * @param memMainDataDTO
	 */
	private static void setMemDetailDataDTO(String detailDataStr, MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		MemDetailDataDTO memDetailDataDTO = new MemDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				memDetailDataDTO.setMemberCode(detailDataStrArr[i]);
				break;

			case 1:
				memDetailDataDTO.setMemName(detailDataStrArr[i]);
				break;

			case 2:
				memDetailDataDTO.setMemPhone(detailDataStrArr[i]);
				break;

			case 3:
				memDetailDataDTO.setMemMobile(detailDataStrArr[i]);
				break;

			case 4:
				memDetailDataDTO.setMemSex(detailDataStrArr[i]);
				break;

			case 5:
				memDetailDataDTO.setMemProvince(detailDataStrArr[i]);
				break;

			case 6:
				memDetailDataDTO.setMemCity(detailDataStrArr[i]);
				break;

			case 7:
				memDetailDataDTO.setMemAddress(detailDataStrArr[i]);
				break;

			case 8:
				memDetailDataDTO.setMemPostcode(detailDataStrArr[i]);
				break;

			case 9:
				memDetailDataDTO.setMemBirthday(detailDataStrArr[i]);
				break;

			case 10:
				memDetailDataDTO.setMemMail(detailDataStrArr[i]);
				break;

			case 11:
				memDetailDataDTO.setMemGranddate(detailDataStrArr[i]);
				break;

			case 12:
				memDetailDataDTO.setBAcode(detailDataStrArr[i]);
				break;

			case 13:
				memDetailDataDTO.setCountercode(detailDataStrArr[i]);
				break;

			case 14:
				memDetailDataDTO.setNewMemcode(detailDataStrArr[i]);
				break;

			case 15:
				memDetailDataDTO.setMemChangeTime(detailDataStrArr[i]);
				break;

			case 16:
				memDetailDataDTO.setMemLevel(detailDataStrArr[i]);
				break;

			case 17:
				memDetailDataDTO.setModifyBirthdayFlag(detailDataStrArr[i]);
				break;
			case 18:
				memDetailDataDTO.setJoinTime(detailDataStrArr[i]);
				break;
			case 19:
				memDetailDataDTO.setReferrer(detailDataStrArr[i]);
				break;	
			case 20:
				memDetailDataDTO.setMemAgeGetMethod(detailDataStrArr[i]);
				break;	
			case 21:
				memDetailDataDTO.setVersion(detailDataStrArr[i]);
				break;	
            case 22:
                memDetailDataDTO.setMemo1(detailDataStrArr[i]);
                break;
            case 23:
                memDetailDataDTO.setActive(detailDataStrArr[i]);
                break;
            case 24:
                memDetailDataDTO.setActiveDate(detailDataStrArr[i]);
                break;
            case 25:
                memDetailDataDTO.setMemLevelExt(detailDataStrArr[i]);
                break;
            case 26:
                memDetailDataDTO.setLevelStartDateExt(detailDataStrArr[i]);
                break;     
            case 27:
                memDetailDataDTO.setLevelEndDateExt(detailDataStrArr[i]);
                break;  
            case 28:
                memDetailDataDTO.setMemberPassword(detailDataStrArr[i]);
                break;  
            case 29:
                memDetailDataDTO.setMemo2(detailDataStrArr[i]);
                break;    
            case 30:
                memDetailDataDTO.setActiveChannel(detailDataStrArr[i]);
                break;
            case 31:
                memDetailDataDTO.setTmallAccount(detailDataStrArr[i]);
                break;
            case 32:
                memDetailDataDTO.setMemCounty(detailDataStrArr[i]);
                break;    
			}
		}
		detailDataDTOList.add(memDetailDataDTO);
	}

	/**
	 * 设定BaDetailDataDTO
	 * 
	 * @param detailDataStr
	 * @param memMainDataDTO
	 */
	private static void setBaDetailDataDTO(String detailDataStr, MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		BaDetailDataDTO baDetailDataDTO = new BaDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				baDetailDataDTO.setBAcode(detailDataStrArr[i]);
				break;

			case 1:
				baDetailDataDTO.setBaname(detailDataStrArr[i]);
				break;

			case 2:
				baDetailDataDTO.setFlag(detailDataStrArr[i]);
				break;

			case 3:
				baDetailDataDTO.setCountercode(detailDataStrArr[i]);
				break;

			case 4:
				baDetailDataDTO.setBaTel(detailDataStrArr[i]);
				break;
				
            case 5:
                baDetailDataDTO.setIdentityCard(detailDataStrArr[i]);
                break;

			}
		}
		detailDataDTOList.add(baDetailDataDTO);
	}

	/**
	 * 设定MonMainDataDTO
	 * 
	 * @param mainDataStr
	 * @param monMainDataDTO
	 */
	private static void setMonMainDataDTO(String mainDataStr, MonMainDataDTO monMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				monMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				monMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;

			case 2:
				monMainDataDTO.setUpdatetime(mainDataStrArr[i]);
				break;

			}
		}
	}
	
	/**
	 * 设定QueMainDataDTO
	 * 
	 * @param mainDataStr
	 * @param monMainDataDTO
	 */
	private static void setQueMainDataDTO(String mainDataStr, QueMainDataDTO queMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				queMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				queMainDataDTO.setTradeType(mainDataStrArr[i]);
				break;

			case 2:
				queMainDataDTO.setSubType(mainDataStrArr[i]);
				break;

			case 3:
				queMainDataDTO.setSourse(mainDataStrArr[i]);
				break;
			
			case 4:
				queMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;
				
			case 5:
				queMainDataDTO.setMemberCode(mainDataStrArr[i]);
				break;
				
			case 6:
				queMainDataDTO.setPaperID(mainDataStrArr[i]);
				break;
				
			case 7:
				queMainDataDTO.setCounterCode(mainDataStrArr[i]);
				break;
				
			case 8:
				queMainDataDTO.setBAcode(mainDataStrArr[i]);
				break;
				
			case 9:
				queMainDataDTO.setUDiskSN(mainDataStrArr[i]);
				break;
				
			case 10:
				queMainDataDTO.setCSName(mainDataStrArr[i]);
				break;
				
			case 11:
				queMainDataDTO.setBAName(mainDataStrArr[i]);
				break;
				
			case 12:
				queMainDataDTO.setMarketName(mainDataStrArr[i]);
				break;
				
			case 13:
				queMainDataDTO.setAdvice(mainDataStrArr[i]);
				break;
				
			case 14:
				queMainDataDTO.setOrderImprove(mainDataStrArr[i]);
				break;
				
			case 15:
				queMainDataDTO.setOrderImproveLastDate(mainDataStrArr[i]);
				break;
				
			case 16:
				queMainDataDTO.setCheckDate(mainDataStrArr[i]);
				break;
				
			case 17:
				queMainDataDTO.setCheckTime(mainDataStrArr[i]);
				break;
				
			case 18:
				queMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
				break;
			}
		}
	}
	
	/**
	 * 设定QusDetailDataDTO
	 * 
	 * @param detailDataStr
	 * @param memMainDataDTO
	 */
	private static void setQusDetailDataDTO(String detailDataStr, QueMainDataDTO queMainDataDTO) {
		List detailDataDTOList = queMainDataDTO.getDetailDataDTOList();
		QueDetailDataDTO queDetailDataDTO = new QueDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				queDetailDataDTO.setQuestionNo(detailDataStrArr[i]);
				break;

			case 1:
				queDetailDataDTO.setAnswer(detailDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(queDetailDataDTO);
	}
	
	
	/**
	 * 设定RivalSaleMainDataDTO
	 * 
	 * @param mainDataStr
	 * @param rivalSaleMainDataDTO
	 */
	private static void setRivalSaleMainDataDTO(String mainDataStr, RivalSaleMainDataDTO rivalSaleMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				rivalSaleMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				rivalSaleMainDataDTO.setTradeType(mainDataStrArr[i]);
				break;

			case 2:
				rivalSaleMainDataDTO.setSubType(mainDataStrArr[i]);
				break;

			case 3:
				rivalSaleMainDataDTO.setSourse(mainDataStrArr[i]);
				break;
			
			case 4:
				rivalSaleMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;
				
			case 5:
				rivalSaleMainDataDTO.setCounterCode(mainDataStrArr[i]);
				break;
				
			case 6:
				rivalSaleMainDataDTO.setMarketName(mainDataStrArr[i]);
				break;
				
			case 7:
				rivalSaleMainDataDTO.setSaleDate(mainDataStrArr[i]);
				break;
				
			case 8:
				rivalSaleMainDataDTO.setUploadDate(mainDataStrArr[i]);
				break;
				
			case 9:
				rivalSaleMainDataDTO.setUploadTime(mainDataStrArr[i]);
				break;
				
			case 10:
				rivalSaleMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
				break;
			}
		}
	}
	
	/**
	 * 设定RivalSaleDetailDataDTO
	 * 
	 * @param detailDataStr
	 * @param rivalSaleMainDataDTO
	 */
	private static void setRivalSaleDetailDataDTO(String detailDataStr, RivalSaleMainDataDTO rivalSaleMainDataDTO) {
		List detailDataDTOList = rivalSaleMainDataDTO.getDetailDataDTOList();
		RivalSaleDetailDataDTO rivalSaleDetailDataDTO = new RivalSaleDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				rivalSaleDetailDataDTO.setRivalNameCN(detailDataStrArr[i]);
				break;

			case 1:
				rivalSaleDetailDataDTO.setRivalNameEN(detailDataStrArr[i]);
				break;
			case 2:
				rivalSaleDetailDataDTO.setSaleMoney(detailDataStrArr[i]);
				break;
			case 3:
				rivalSaleDetailDataDTO.setSaleQuantity(detailDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(rivalSaleDetailDataDTO);
	}
	
	/**
	 * 设定setBasDetailDataDTO
	 * 
	 * @param detailDataStr
	 * @param MemMainDataDTO
	 */
	private static void setBasDetailDataDTO(String detailDataStr, MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		BasDetailDataDTO basDetailDataDTO = new BasDetailDataDTO();
		String[] detailDataStrArr = detailDataStr.split(",");
		for (int i = 0; i < detailDataStrArr.length; i++) {
			switch (i) {
			case 0:
				basDetailDataDTO.setUDiskSN(detailDataStrArr[i]);
				break;

			case 1:
				basDetailDataDTO.setBasName(detailDataStrArr[i]);
				break;
			case 2:
				basDetailDataDTO.setCountercode(detailDataStrArr[i]);
				break;
			case 3:
				basDetailDataDTO.setCounterName(detailDataStrArr[i]);
				break;
			case 4:
				basDetailDataDTO.setMachinecode(detailDataStrArr[i]);
				break;
			case 5:
				basDetailDataDTO.setAttDate(detailDataStrArr[i]);
				break;
			case 6:
				basDetailDataDTO.setArrTime(detailDataStrArr[i]);
				break;
			case 7:
				basDetailDataDTO.setLeaTime(detailDataStrArr[i]);
				break;
			case 8:
				basDetailDataDTO.setAttType(detailDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(basDetailDataDTO);
	}
	
	/**
	 * 设定MemInitDataDTO
	 * 
	 * @param mainDataStr
	 * @param memInitDataDTO
	 */
	private static void setMemInitDataDTO(String mainDataStr, MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		MemInitDataDTO memInitDataDTO = new MemInitDataDTO();
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				memInitDataDTO.setCountercode(mainDataStrArr[i]);
				break;
				
			case 1:
				memInitDataDTO.setBacode(mainDataStrArr[i]);
				break;
				
			case 2:
				memInitDataDTO.setMemberID(mainDataStrArr[i]);
				break;
				
			case 3:
				memInitDataDTO.setMemberCode(mainDataStrArr[i]);
				break;

			case 4:
				memInitDataDTO.setMember_level(mainDataStrArr[i]);
				break;
				
			case 5:
				memInitDataDTO.setCurBtimes(mainDataStrArr[i]);
				break;	

			case 6:
				memInitDataDTO.setCurPoints(mainDataStrArr[i]);
				break;
			
			case 7:
				memInitDataDTO.setTotalAmounts(mainDataStrArr[i]);
				break;
				
			case 8:
				memInitDataDTO.setJoinDate(mainDataStrArr[i]);
				break;
				
			case 9:
				memInitDataDTO.setAcquiTime(mainDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(memInitDataDTO);
	}
	
	/**
	 * 设定MemUsedDetailDTO
	 * 
	 * @param mainDataStr
	 * @param memInitDataDTO
	 */
	private static void setMemUsedDetailDTO(String mainDataStr, MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		MemUsedDetailDTO memUsedDetailDTO = new MemUsedDetailDTO();
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				memUsedDetailDTO.setCountercode(mainDataStrArr[i]);
				break;

			case 1:
				memUsedDetailDTO.setBacode(mainDataStrArr[i]);
				break;
				
			case 2:
				memUsedDetailDTO.setMemberID(mainDataStrArr[i]);
				break;	
				
			case 3:
				memUsedDetailDTO.setMemberCode(mainDataStrArr[i]);
				break;
			
			case 4:
				memUsedDetailDTO.setUsedTimes(mainDataStrArr[i]);
				break;
				
			case 5:
				memUsedDetailDTO.setBusinessTime(mainDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(memUsedDetailDTO);
	}
	
	/**
	 * 设定MachMainDataDTO
	 * 
	 * @param mainDataStr
	 * @param memMainDataDTO
	 */
	private static void setMachMainDataDTO(String mainDataStr, MachMainDataDTO machMainDataDTO) {
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				machMainDataDTO.setBrandCode(mainDataStrArr[i]);
				break;

			case 1:
				machMainDataDTO.setTradeType(mainDataStrArr[i]);
				break;

			case 2:
				machMainDataDTO.setMachineCode(mainDataStrArr[i]);
				break;
				
			case 3:
				machMainDataDTO.setSoftWareVersion(mainDataStrArr[i]);
				break;
				
			case 4:
				machMainDataDTO.setCapacity(mainDataStrArr[i]);
				break;
				
			case 5:
				machMainDataDTO.setSourse(mainDataStrArr[i]);
				break;
				
			case 6:
			    machMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
			    break;
			}
		}
	}
	private static void setMemVisitDetailDTO(String mainDataStr,MemMainDataDTO memMainDataDTO) {
		List detailDataDTOList = memMainDataDTO.getDetailDataDTOList();
		MemVisitDetailDataDTO memVisitDetailDTO = new MemVisitDetailDataDTO();
		String[] mainDataStrArr = mainDataStr.split(",");
		for (int i = 0; i < mainDataStrArr.length; i++) {
			switch (i) {
			case 0:
				memVisitDetailDTO.setCountercode(mainDataStrArr[i]);
				break;

			case 1:
				memVisitDetailDTO.setMemberCode(mainDataStrArr[i]);
				break;
				
			case 2:
				memVisitDetailDTO.setBAcode(mainDataStrArr[i]);
				break;	
				
			case 3:
				memVisitDetailDTO.setVisitBeginDate(mainDataStrArr[i]);
				break;
			
			case 4:
				memVisitDetailDTO.setVisitEndDate(mainDataStrArr[i]);
				break;
				
			case 5:
				memVisitDetailDTO.setVisitDate(mainDataStrArr[i]);
				break;
				
			case 6:
				memVisitDetailDTO.setVisitFlag(mainDataStrArr[i]);
				break;
				
			case 7:
				memVisitDetailDTO.setVisitCode(mainDataStrArr[i]);
				break;
				
			case 8:
				memVisitDetailDTO.setVisitTypeCode(mainDataStrArr[i]);
				break;
			
			case 9:
				memVisitDetailDTO.setVisitTaskID(mainDataStrArr[i]);
				break;
			}
		}
		detailDataDTOList.add(memVisitDetailDTO);
	}
	
    /**
     * 设定SaleReturnMainDataDTO
     * @param mainDataStr
     * @param saleReturnMainDataDTO
     */
    private static void setSaleReturnMainDataDTO(String mainDataStr, SaleReturnMainDataDTO saleReturnMainDataDTO) {
        String[] mainDataStrArr = mainDataStr.split(",");
        for (int i = 0; i < mainDataStrArr.length; i++) {
            switch (i) {
            case 0:
                saleReturnMainDataDTO.setBrandCode(mainDataStrArr[i]);
                break;

            case 1:
                saleReturnMainDataDTO.setTradeNoIF(mainDataStrArr[i]);
                break;

            case 2:
                saleReturnMainDataDTO.setModifyCounts(mainDataStrArr[i]);
                break;

            case 3:
                saleReturnMainDataDTO.setCounterCode(mainDataStrArr[i]);
                break;

            case 4:
                saleReturnMainDataDTO.setRelevantCounterCode(mainDataStrArr[i]);
                break;

            case 5:
                saleReturnMainDataDTO.setTotalQuantity(mainDataStrArr[i]);
                break;

            case 6:
                saleReturnMainDataDTO.setTotalAmount(mainDataStrArr[i]);
                break;

            case 7:
                saleReturnMainDataDTO.setTradeType(mainDataStrArr[i]);
                break;

            case 8:
                saleReturnMainDataDTO.setSubType(mainDataStrArr[i]);
                break;

            case 9:
                saleReturnMainDataDTO.setRelevantNo(mainDataStrArr[i]);
                break;

            case 10:
                saleReturnMainDataDTO.setReason(mainDataStrArr[i]);
                break;

            case 11:
                saleReturnMainDataDTO.setTradeDate(mainDataStrArr[i]);
                break;

            case 12:
                saleReturnMainDataDTO.setTradeTime(mainDataStrArr[i]);
                break;

            case 13:
                saleReturnMainDataDTO.setTotalAmountBefore(mainDataStrArr[i]);
                break;

            case 14:
                saleReturnMainDataDTO.setTotalAmountAfter(mainDataStrArr[i]);
                break;

            case 15:
                saleReturnMainDataDTO.setMemberCode(mainDataStrArr[i]);
                break;

            case 16:
                saleReturnMainDataDTO.setCounter_ticket_code(mainDataStrArr[i]);
                break;

            case 17:
                saleReturnMainDataDTO.setCounter_ticket_code_pre(mainDataStrArr[i]);
                break;

            case 18:
                saleReturnMainDataDTO.setTicket_type(mainDataStrArr[i]);
                break;

            case 19:
                saleReturnMainDataDTO.setSale_status(mainDataStrArr[i]);
                break;

            case 20:
                saleReturnMainDataDTO.setConsumer_type(mainDataStrArr[i]);
                break;

            case 21:
                saleReturnMainDataDTO.setMember_level(mainDataStrArr[i]);
                break;

            case 22:
                saleReturnMainDataDTO.setOriginal_amount(mainDataStrArr[i]);
                break;

            case 23:
                saleReturnMainDataDTO.setDiscount(mainDataStrArr[i]);
                break;

            case 24:
                saleReturnMainDataDTO.setPay_amount(mainDataStrArr[i]);
                break;

            case 25:
                saleReturnMainDataDTO.setDecrease_amount(mainDataStrArr[i]);
                break;

            case 26:
                saleReturnMainDataDTO.setCostpoint(mainDataStrArr[i]);
                break;

            case 27:
                saleReturnMainDataDTO.setCostpoint_amount(mainDataStrArr[i]);
                break;

            case 28:
                saleReturnMainDataDTO.setSale_ticket_time(mainDataStrArr[i]);
                break;

            case 29:
                saleReturnMainDataDTO.setData_source(mainDataStrArr[i]);
                break;

            case 30:
                saleReturnMainDataDTO.setMachineCode(mainDataStrArr[i]);
                break;

            case 31:
                saleReturnMainDataDTO.setSaleSRtype(mainDataStrArr[i]);
                break;
                
            case 32:
                saleReturnMainDataDTO.setBAcode(mainDataStrArr[i]);
                break;
                
            case 33:
                saleReturnMainDataDTO.setDepartCodeDX(mainDataStrArr[i]);
                break;
                
            case 34:
                saleReturnMainDataDTO.setEmployeeCodeDX(mainDataStrArr[i]);
                break;
            
            case 35:
                saleReturnMainDataDTO.setClubCode(mainDataStrArr[i]);
                break; 
                
            case 36:
                saleReturnMainDataDTO.setBillModel(mainDataStrArr[i]);
                break;
            case 37:
                saleReturnMainDataDTO.setOriginalDataSource(mainDataStrArr[i]);
                break;
            case 38:
            	saleReturnMainDataDTO.setInvoiceFlag(mainDataStrArr[i]);
            	break;
            	
            case 39:
            	saleReturnMainDataDTO.setSaleReason(mainDataStrArr[i]);
            	break;
            
            case 40:
            	saleReturnMainDataDTO.setIsPoint(mainDataStrArr[i]);
            	break;
            	
            case 41:
            	saleReturnMainDataDTO.setSensitiveSuggestVersion(mainDataStrArr[i]);
            	break;
            case 42:
            	saleReturnMainDataDTO.setDrySuggestVersion(mainDataStrArr[i]);
            	break;
            case 43:
            	saleReturnMainDataDTO.setStockFlag(mainDataStrArr[i]);
            	break;
            case 44:
            	saleReturnMainDataDTO.setStockCounter(mainDataStrArr[i]);
            	break;
            case 45:
            	saleReturnMainDataDTO.setPickupDate(mainDataStrArr[i]);
            	break;
            case 46:
            	saleReturnMainDataDTO.setMobilePhone(mainDataStrArr[i]);
            	break;
            case 47:
            	saleReturnMainDataDTO.setSaleReturnRequestCode(mainDataStrArr[i]);
            	break;
			case 48:
				saleReturnMainDataDTO.setMergeStoredValueFlag(mainDataStrArr[i]);
            }
        }
    }
	
	/**
     * 设定SaleReturnDetailDataDTO
     * @param detailDataStr
     * @param saleReturnMainDataDTO
     */
    private static void setSaleReturnDetailDataDTO(String detailDataStr, SaleReturnMainDataDTO saleReturnMainDataDTO) {
        List<SaleReturnDetailDataDTO> detailDataDTOList = saleReturnMainDataDTO.getDetailDataDTOList();
        SaleReturnDetailDataDTO saleReturnDetailDataDTO = new SaleReturnDetailDataDTO();
        String[] detailDataStrArr = detailDataStr.split(",");
        for (int i = 0; i < detailDataStrArr.length; i++) {
            switch (i) {
            case 0:
                saleReturnDetailDataDTO.setTradeNoIF(detailDataStrArr[i]);
                break;

            case 1:
                saleReturnDetailDataDTO.setModifyCounts(detailDataStrArr[i]);
                break;

            case 2:
                saleReturnDetailDataDTO.setDetailType(detailDataStrArr[i]);
                break;

            case 3:
                saleReturnDetailDataDTO.setPayTypeCode(detailDataStrArr[i]);
                break;

            case 4:
                saleReturnDetailDataDTO.setPayTypeID(detailDataStrArr[i]);
                break;

            case 5:
                saleReturnDetailDataDTO.setPayTypeName(detailDataStrArr[i]);
                break;

            case 6:
                saleReturnDetailDataDTO.setBAcode(detailDataStrArr[i]);
                break;

            case 7:
                saleReturnDetailDataDTO.setStockType(detailDataStrArr[i]);
                break;

            case 8:
                saleReturnDetailDataDTO.setBarcode(detailDataStrArr[i]);
                break;

            case 9:
                saleReturnDetailDataDTO.setUnitcode(detailDataStrArr[i]);
                break;

            case 10:
                saleReturnDetailDataDTO.setInventoryTypeCode(detailDataStrArr[i]);
                break;

            case 11:
                saleReturnDetailDataDTO.setQuantity(detailDataStrArr[i]);
                break;
                
            case 12:
                saleReturnDetailDataDTO.setQuantityBefore(detailDataStrArr[i]);
                break;
                
            case 13:
                saleReturnDetailDataDTO.setPrice(detailDataStrArr[i]);
                break;
                
            case 14:
                saleReturnDetailDataDTO.setReason(detailDataStrArr[i]);
                break;
                
            case 15:
                saleReturnDetailDataDTO.setDiscount(detailDataStrArr[i]);
                break;
                
            case 16:
                saleReturnDetailDataDTO.setMemberCodeDetail(detailDataStrArr[i]);
                break;
                
            case 17:
                saleReturnDetailDataDTO.setActivityMainCode(detailDataStrArr[i]);
                break;
                
            case 18:
                saleReturnDetailDataDTO.setActivityCode(detailDataStrArr[i]);
                break;
            
            case 19:
                saleReturnDetailDataDTO.setOrderID(detailDataStrArr[i]);
                break;
                
            case 20:
                saleReturnDetailDataDTO.setCouponCode(detailDataStrArr[i]);
                break;
            
            case 21:
                saleReturnDetailDataDTO.setIsStock(detailDataStrArr[i]);
                break;
                
            case 22:
                saleReturnDetailDataDTO.setInformType(detailDataStrArr[i]);
                break;
                
            case 23:
                saleReturnDetailDataDTO.setUniqueCode(detailDataStrArr[i]);
                break;
                
            case 24:
            	saleReturnDetailDataDTO.setSaleReason(detailDataStrArr[i]);
            	break;
            	
            case 25:
            	saleReturnDetailDataDTO.setProductId(detailDataStrArr[i]);
            	break;
            	
            case 26:
            	saleReturnDetailDataDTO.setTagPrice(detailDataStrArr[i]);
            	break;
                
            }
        }

        detailDataDTOList.add(saleReturnDetailDataDTO);

    }
}
