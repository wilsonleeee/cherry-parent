/*	
 * @(#)BINOLSSCM05_BL.java     1.0 2010/10/29		
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
package com.cherry.ss.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.cmbussiness.bl.BINOLCM04_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.service.BINOLSSCM05_Service;

/**
 * 发送MQ消息
 * @author dingyc
 *
 */
public class BINOLSSCM05_BL {
	@Resource(name="binOLSSCM05_Service")
	private BINOLSSCM05_Service binolsscm05_service;
	
	@Resource(name="binOLCM04_BL")
	private BINOLCM04_BL binOLCM04_BL;
	
	@Resource(name="messageSender")
	private MessageSender messageSender;
	/**
	 * 将发货单据信息组织成MQ消息
	 * 为兼顾老后台，相同的明细条目将被合并
	 * @param promotionDeliverIDArr 收发货单据ID数组
	 * @param userInfo
	 */
	public void sendMQDeliverSend(int[] promotionDeliverIDArr,int userID){
	    Map<String,String> pramMap = new HashMap<String,String>();
	    pramMap.put("DataFrom", "Inventory.BIN_PromotionDeliver");
	    sendMQDeliverSend(promotionDeliverIDArr,userID,pramMap);
	}
	
	/**
     * 将发货单/入库单据信息组织成MQ消息
     * 为兼顾老后台，相同的明细条目将被合并
	 * @param promotionDeliverIDArr
	 * @param userID
	 * @param pramMap
	 */
	public void sendMQDeliverSend(int[] promotionDeliverIDArr,int userID,Map<String,String> pramMap){
        String dataFrom = ConvertUtil.getString(pramMap.get("DataFrom"));//数据来源表，用于区分发货单还是入库单
        for (int i = 0; i < promotionDeliverIDArr.length; i++) {
            // 取得指定ID的收发货单据信息（包括详细）
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
            Map<String, Object> praMap = new HashMap<String, Object>();
            Map<String, Object> ret = new HashMap<String,Object>();
            if(dataFrom.equals("Inventory.BIN_PrmInDepot")){
                list = binolsscm05_service.getPrmInDepotAllGroupByID(promotionDeliverIDArr[i]);
                praMap.put("BIN_PrmInDepotID",promotionDeliverIDArr[i]);
                ret = binolsscm05_service.getPrmInDepotForMQ(praMap);
                ret.put("DepartCode","");
            }else{
                list = binolsscm05_service.getPromotionDeliverAllGroupByID(promotionDeliverIDArr[i]);
                praMap.put("BIN_PromotionDeliverID",promotionDeliverIDArr[i]);
                ret = binolsscm05_service.getPromotionDeliverForMQ(praMap);
            }
            Map<String, Object> deliverDetailMap = list.get(0);
            
            // 插入MQ日志表
            String msg = getDeliverMQMessage(list, String.valueOf(ret.get("BrandCode")),String.valueOf(ret.get("DepartCode")));
            Map<String, Object> map = new HashMap<String, Object>();

            // 数据插入方标志
            map.put("Source", "CHERRY");
            // 消息方向
            map.put("SendOrRece", "S");
            // 所属组织
            map.put("BIN_OrganizationInfoID", ret.get("BIN_OrganizationInfoID"));
            // 所属品牌ID
            map.put("BIN_BrandInfoID", ret.get("BIN_BrandInfoID"));
            // 单据类型
            map.put("BillType", "IS");
            // 单据号
            map.put("BillCode", deliverDetailMap.get("mDeliverReceiveNo"));
            // 销售记录修改次数
            map.put("SaleRecordModifyCount", 0);
            // 柜台号
            map.put("CounterCode", deliverDetailMap.get("CounterCode"));
            // 交易日期
            map.put("TxdDate", null);
            // 交易时间
            map.put("TxdTime", null);
            // Transactionlog表开始写入时间
            map.put("BeginPuttime", null);
            // Transactionlog表结束写入时间
            map.put("EndPuttime", null);
            // 是否促销品
            map.put("isPromotionFlag", 1);
            // 批处理执行对象区分
            map.put("ReceiveFlag", 0);
            //消息发送队列名
            map.put("MsgQueueName", CherryConstants.CHERRYTOPOSMSGQUEUE);
            // 消息体
            map.put("Data", msg);
            // 有效区分
            map.put("ValidFlag", "1");
            map.put("CreatedBy", userID);
            map.put("CreatePGM", "BINOLSSCM05");
            map.put("UpdatedBy", userID);
            map.put("UpdatePGM", "BINOLSSCM05");
            binOLCM04_BL.insertMQLog(map);

            messageSender.sendMessage(msg);
        }
	}
	
	static String str1 ="\r\n";
	static String str2 =",";

	public  String getDeliverMQMessage(List<Map<String,Object>> list,String brandCode,String departCode){
		StringBuffer buf = new StringBuffer();
		buf.append("[Version]");
		buf.append(str2);
		buf.append("AMQ.001.001");
		buf.append(str1);
		
		for(int i =0;i<list.size();i++){
			Map<String,Object> map = CherryUtil.replaceMQSpecialChar(list.get(i));
			if(i==0){
				buf.append("[CommLine],BrandCode,TradeNoIF,ModifyCounts,CounerCode,RelevantCounterCode,TotalQuantity," +
				"TotalAmount,TradeType,SubType,RelevantNo,Reason,TradeDate,TradeTime,TotalAmountBefore,TotalAmountAfer,MemberCode,PlanArriveDate");
				buf.append(str1);
				buf.append("[MainDataLine]");
				buf.append(str2);
				//品牌代码
				buf.append(brandCode);
				buf.append(str2);
				//单据号
				buf.append(String.valueOf(map.get("mDeliverReceiveNo")));
				buf.append(str2);				
				//修改回数
				buf.append(str2);
				//柜台号
				buf.append(String.valueOf(map.get("CounterCode")));
				buf.append(str2);
				//关联柜台号/办事处代号
				buf.append(departCode);
				buf.append(str2);
				//总数量
				buf.append(String.valueOf(map.get("mTotalQuantity")));
				buf.append(str2);
				//总金额
				buf.append(String.valueOf(map.get("mTotalAmount")));
				buf.append(str2);
				//业务类型
				buf.append("IS");
				buf.append(str2);
				//子类型
				buf.append(str2);
				//关联单号
				buf.append(str2);
				//出入库理由
				buf.append(str2);
				//出入库日期
				String datetime = String.valueOf(map.get("mDeliverDate"));
				//buf.append(datetime.substring(0, 10));
				buf.append(datetime);
				buf.append(str2);
				//出入库时间
				buf.append("00:00:00");
				buf.append(str2);
				//入出库前柜台库存总金额
				buf.append(str2);
				//入出库后柜台库存总金额
				buf.append(str2);
				//会员号
				buf.append(str2);
				//预计到货日期
                buf.append(ConvertUtil.getString(map.get("mPlanArriveDate")));
				
				buf.append(str1);
				buf.append("[CommLine],TradeNoIF,ModifyCounts,BAcode,StockType,Barcode,Unitcode,InventoryTypeCode," +
						"Quantity,QuantityBefore,Price,Reason");
				buf.append(str1);
			}
			buf.append("[DetailDataLine]");
			buf.append(str2);
			//单据号
			buf.append(String.valueOf(map.get("mDeliverReceiveNo")));
			buf.append(str2);
			//修改回数
			buf.append(str2);
			//BA卡号
			buf.append(str2);
			//入出库区分
			buf.append("0");
			buf.append(str2);
			//商品条码
			buf.append(String.valueOf(map.get("BarCode")));
			buf.append(str2);
			//厂商编码
			buf.append(String.valueOf(map.get("UnitCode")));
			buf.append(str2);
			//仓库类型（逻辑仓库）
//			buf.append("0");
			buf.append(str2);
			//数量
			buf.append(String.valueOf(map.get("Quantity")));
			buf.append(str2);
			//入出库前该商品柜台账面数量（针对盘点）
			buf.append(str2);
			//价格
			buf.append(String.valueOf(map.get("Price")));
			buf.append(str2);
			//单个商品出入库理由
			//buf.append(str2);
			//注意：保证CommLine和DetailDataLine的逗号数量一样，否则终端接收时会报错。
			
			buf.append(str1);
		}
		buf.append("[End]");
		System.out.print(buf.toString());
		return buf.toString();		
	}
	
	/**
	 * 验证部门类型是否是柜台
	 * @param organizationId
	 * @return
	 */
	public boolean checkOrganizationType (String organizationId){
		// 取得部门类型
		String organizationType =getOrganizationType(organizationId);
		// 部门类型是柜台的时候
		if (CherryConstants.ORGANIZATION_TYPE_FOUR.equals(organizationType)){
			return true;	
		}
		return false;		
	}
	/**
	 * 取得部门类型
	 * @param organizationId
	 * @return
	 */
	public String getOrganizationType (String organizationId){
		// 取得部门类型
		String organizationType = binolsscm05_service.getOrganizationType(organizationId);		
		return organizationType;		
	}
}
