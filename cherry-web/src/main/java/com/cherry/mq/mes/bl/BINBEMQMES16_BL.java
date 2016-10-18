/*  
 * @(#)BINBEMQMES14_BL.java     1.0 2014/11/25     
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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES16_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 线上购买线下提货订单状态变更接收处理BL
 * 
 * @author lzs
 * 
 */
public class BINBEMQMES16_BL {
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES16_BL.class);

	@Resource(name = "binBEMQMES16_Service")
	private BINBEMQMES16_Service binBEMQMES16_Service;

	@Resource(name = "binBEMQMES01_BL")
	private AnalyzeMessage_IF binBEMQMES01_BL;

	@Resource(name = "binBEMQMES02_BL")
	private AnalyzeMessage_IF binBEMQMES02_BL;

	@Resource(name = "binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	public void handleOrderChange(Map<String, Object> map) throws Exception {
		// 设置共同参数
		setInsertInfoMapKey(map);
		// 必输项校验
		veriFicationParam(map);
		// 判断业务类型
		String tradeType = ConvertUtil.getString(map.get("tradeType"));
		if ("NZ".equalsIgnoreCase(tradeType)) {
			// 接收MQ推送的订单信息，并更改订单状态信息，同时进行操作库存处理
			// 更新订单信息
			binBEMQMES16_Service.updateOrderInfo(map);
			// 获取主单相关信息
			Map<String, Object> saleRecordMap = binBEMQMES16_Service.getSaleRecordInfo(map);
			map.putAll(saleRecordMap);

			//更新主单信息 目前只支持提货时更新销售单信息，线下退货还不支持
			String orderStatus = ConvertUtil.getString(map.get("orderStatus"));
			if (orderStatus.equals("0")){
				binBEMQMES16_Service.updateSaleRecordInfo(map);
			}
			// 取得部门信息
			map.put("counterCode", map.get("takeCounterCode"));
			HashMap counterinfoMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

			if (counterinfoMap!=null && counterinfoMap.get("organizationID") != null) {
				// 设定部门ID
				map.put("organizationID", counterinfoMap.get("organizationID"));
				// 设定柜台名称
				map.put("counterName", counterinfoMap.get("counterName"));
				// 云POS 
				map.put("posFlag", counterinfoMap.get("posFlag"));
				// 所属渠道ID
				map.put("pushChannelId", counterinfoMap.get("channelId"));
				// 所属渠道名称
				map.put("pushChannelName", counterinfoMap.get("channelName"));
			} else {
				// 没有查询到相关部门信息
				MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
			}
			// 获取销售数据明细信息
			List<Map<String, Object>> saleRecordDetailList = binBEMQMES16_Service.getSaleDetailList(map);
			List<Map<String, Object>> cloneSaleRecordDetailList = (List<Map<String, Object>>) ConvertUtil.byteClone(saleRecordDetailList);
			Map<String, Object> cloneMap = (Map<String, Object>) ConvertUtil.byteClone(map);
			// 查询仓库信息
			List<Map<String, Object>> list = binOLCM18_BL.getDepotsByDepartID(map.get("organizationID").toString(), "");
			if (list != null && list.size() > 0) {
				Map<String, Object> resultMap = (HashMap) list.get(0);
				// 设定实体仓库ID
				map.put("inventoryInfoID", resultMap.get("BIN_DepotInfoID"));
			}
			if ("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))) {
				// 没有查询到相关仓库信息
				MessageUtil.addMessageWarning(map,"柜台号为\"" + map.get("counterCode") + "\""+ MessageConstants.MSG_ERROR_36);
			}
			// 交易类型
			String saleType = ConvertUtil.getString(saleRecordMap.get("saleType"));
			if ("NS".equals(saleType) || "SR".equals(saleType)) {
				// 仅退货类型 销售类型
				map.put("saleSRtype", "3");
			}
			map.put("cherry_tradeType", saleType);
			List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
			if (!CherryUtil.isBlankList(saleRecordDetailList)) {
				for (int i = 0; i < saleRecordDetailList.size(); i++) {
					HashMap productDetailMap = (HashMap)saleRecordDetailList.get(i);
					productDetailMap.put("modifyCounts", "1");
					productDetailMap.put("inventoryInfoID",map.get("inventoryInfoID"));
					// 逻辑仓库
					Map<String, Object> logicInventoryInfoMap = new HashMap<String, Object>();
					logicInventoryInfoMap.put("BIN_BrandInfoID",saleRecordMap.get("brandInfoID"));
					logicInventoryInfoMap.put("LogicInventoryCode",productDetailMap.get("inventoryTypeCode"));
					logicInventoryInfoMap.put("Type", "1");// 终端逻辑仓库
					logicInventoryInfoMap.put("language", null);
					Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
					if (logicInventoryInfo != null && !logicInventoryInfo.isEmpty()) {
						int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
						// 设定逻辑仓库ID
						productDetailMap.put("logicInventoryInfoID",logicInventoryInfoID);
					} else {
						// 没有查询到相关逻辑仓库信息
						MessageUtil.addMessageWarning(map,"逻辑仓库为\"" + productDetailMap.get("inventoryTypeCode") + "\""+ MessageConstants.MSG_ERROR_37);
					}
					// 入出库区分标识，说明：销售(NS)为出库，退货(SR),关联退货（SRR,原单必须存在）为入库
					if ("NS".equals(saleType)) {
						productDetailMap.put("stockType", "1");
					} else if ("SR".equals(saleType) || "SRR".equals(saleType)) {
						productDetailMap.put("stockType", "0");
					}
					// 是否促销品类型
					String isPromotion = ConvertUtil.getString(productDetailMap.get("saleType"));
					if ("P".equals(isPromotion)) {
						saleRecordDetailList.remove(i);
						i--;
					}
					map.put("isPromotion", "N");
					productDetailMap.put("detailNo", i+1);
				}
				map.put("detailDataDTOList", saleRecordDetailList);
				messageList.add(map);
				/*************************************** 促销数量 *********************************************************/
				for (int i = 0; i < cloneSaleRecordDetailList.size(); i++) {
					HashMap prmDetailMap = (HashMap)cloneSaleRecordDetailList.get(i);
					// 是否促销品类型
					String isPromotion = ConvertUtil.getString(prmDetailMap.get("saleType"));
					if ("N".equals(isPromotion)) {
						cloneSaleRecordDetailList.remove(i);
						i--;
					}
					cloneMap.put("isPromotion", "P");
					prmDetailMap.put("detailNo", i+1);
				}
				if (!CherryUtil.isBlankList(cloneSaleRecordDetailList)) {
					cloneMap.put("detailDataDTOList", cloneSaleRecordDetailList);
					messageList.add(cloneMap);
				}
				for (int i = 0; i < messageList.size(); i++) {
					Map<String, Object> messageMap = messageList.get(i);
					// 是否促销品类型
					String isPromotion = ConvertUtil.getString(messageMap.get("isPromotion"));
					// 促销品消息处理
					if ("P".equals(isPromotion)) {
						// 当产品为促销品时，则厂商ID为促销品的厂商ID
						map.put("promotionProductVendorID",messageMap.get("productVendorID"));
						if (saleType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							logger.info("********************执行促销品扣除库存处理***************************");
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES01_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					} else {
						if (saleType.equals(MessageConstants.MSG_TRADETYPE_SALE)) {
							logger.info("********************执行产品扣除库存处理***************************");
							// 处理销售/退货消息(库存处理)(新消息体Type=0007)
							binBEMQMES02_BL.analyzeSaleReturnStockData(messageMap);
						} else {
							// 没有此业务类型
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
						}
					}
				}
			}
			// 调用微商城WebService并把订单信息推送到微商城
			transferWebService(map);
		} else {
			// 没有此业务类型
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_27);
		}
	}

	/**
	 * 验证MQ传输参数
	 * 
	 * @param map
	 * @throws CherryException
	 * @throws CherryMQException
	 */
	public void veriFicationParam(Map<String, Object> map) throws CherryException, CherryMQException {
		// 订单号
		String tradeNoIF = ConvertUtil.getString(map.get("tradeNoIF"));
		if (ConvertUtil.isBlank(tradeNoIF)) {
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_85);
		} else {
			// 判断销售数据是否在新后台存在
			List<Map<String, Object>> saleDetailList = binBEMQMES16_Service.getSaleDetailList(map);
			if (saleDetailList.size() == 0) {
				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_84 + "\"" + tradeNoIF + "\"");
			}
		}
		// 操作时间
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		if (ConvertUtil.isBlank(tradeTime)) {
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_88);
		} else {
			// 判断操作时间是否符合时间格式
			if (!DateUtil.checkDate(tradeTime)) {
				MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_89);
			}
		}
		// 订单状态
		String orderStatus = ConvertUtil.getString(map.get("orderStatus"));
		if (ConvertUtil.isBlank(orderStatus)) {
			MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_90);
		}
	}

	/**
	 * 共通参数
	 * 
	 * @param map
	 */
	private void setInsertInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "BINBEMQMES16");
		map.put("createPGM", "BINBEMQMES16");
		map.put("updatedBy", "BINBEMQMES16");
		map.put("updatePGM", "BINBEMQMES16");
		// 设置固定参数
		map.put("modifyCounts", "1");
	}
	
	public void addMongoMsgInfo(Map map) throws CherryMQException {
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码，即品牌简称
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", map.get("tradeType"));
			// 单据号
			dbObject.put("TradeNoIF", map.get("tradeNoIF"));
			//将modifyCounts置为0
			map.put("modifyCounts", "0");
			// 修改次数
			dbObject.put("ModifyCounts", map.get("modifyCounts")==null
					||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(map.get("tradeType"))
					||MessageConstants.MSG_BIR_PRESENT.equals(map.get("tradeType"))){
				// 业务主体
			    dbObject.put("TradeEntity", "0");
			}else{
				// 业务主体
			    dbObject.put("TradeEntity", "1");
			}
			// 柜台号
			dbObject.put("DeptCode", map.get("counterCode"));
			// 柜台名称
			dbObject.put("DeptName", map.get("counterName"));
			// 发生时间
			dbObject.put("OccurTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
			
			if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)){			
					  if (map.get("saleSRtype")!=null){
							if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
								// 日志正文
								dbObject.put("Content", "销售");
						    }else{
						    	// 日志正文
								dbObject.put("Content", "退货");
						    }
					  }				 
			}
			map.put("dbObject", dbObject);
//			binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	}

	/**
	 * 调用微商城Webservice并把订单状态信息推送到微商城
	 * 
	 * @param map
	 * @return
	 * @throws CherryException
	 */
	private void transferWebService(Map<String, Object> map) throws CherryException {
		Map<String, Object> updOrderMap = new HashMap<String, Object>();
		Map<String, Object> wsMap = new HashMap<String, Object>();
		// 业务类型
		wsMap.put("TradeType", "UpdateOrderStatus");
		// 发货柜台
		wsMap.put("CounterCode", map.get("takeCounterCode"));
		// 发货BA
		wsMap.put("BaCode", map.get("takeBACode"));
		// 订单号
		wsMap.put("OrderSn", map.get("tradeNoIF"));
		// 发货时间
		wsMap.put("DeliveryDate", map.get("tradeTime"));
		// 订单状态
		wsMap.put("OrderStatus", map.get("orderStatus"));
		// 调用微商城接口
		try {
			logger.info("********************执行推送订单至微商城WS处理***************************");
			Map<String, Object> resultMap = WebserviceClient.accessWeshopWebService(wsMap);
			String errCode = ConvertUtil.getString(resultMap.get("ERRORCODE"));
			String errMsg = ConvertUtil.getString(resultMap.get("ERRORMSG"));
			if ("0".equals(errCode)) {
				// 推送成功
				updOrderMap.put("pushFlag", "1");
			} else {
				// 推送失败，业务问题
				updOrderMap.put("pushFlag", "2");
				// 失败信息
				updOrderMap.put("pushFailMsg", errMsg);
			}
			// 更新单据信息
		} catch (Exception e) {
			// 推送失败，程序异常
			updOrderMap.put("pushFlag", "3");
			// 失败信息
			updOrderMap.put("pushFailMsg", e.getMessage());

		}
		// 更新订单相关信息
		setInsertInfoMapKey(updOrderMap);
		updOrderMap.put("brandInfoID", map.get("brandInfoID"));
		updOrderMap.put("organizationInfoID", map.get("organizationInfoID"));
		updOrderMap.put("tradeNoIF", map.get("tradeNoIF"));
		binBEMQMES16_Service.updateOrderInfo(updOrderMap);
	}
}
