/*		
 * @(#)MqPP.java     1.0 2016-7-26 	
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.mq.mes.service.MqDG_Service;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.service.BINOLSSCM01_Service;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.webservice.client.WebserviceClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @ClassName: MqDG 
 * @Description: TODO(提货单) 
 * @author menghao
 * @version v1.0.0 2016-7-26 
 *
 */
public class MqDG implements MqReceiver_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(MqDG.class);
	
	@Resource(name="binBEMQMES96_BL")
	private BINBEMQMES96_BL binBEMQMES96_BL;
	
	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	/**促销品处理库存BL*/
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	@Resource(name="binOLSSCM01_Service")
	private BINOLSSCM01_Service binOLSSCM01_Service;
	
	@Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="mqDG_Service")
	private MqDG_Service mqDG_Service;
	
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {
		
		// 校验并设置相关参数
		this.checkAndSetData(map);
		
		// 设置操作程序名称
		this.setUpdateInfoMapKey(map);
		
		// 写提货主从表
		this.insertPickupBillAll(map);
		
		// 更新提货的原始预定已提货数量及下次提货相关信息
		this.updateOriginalNSBillInfo(map);
		
		// 处理其他业务类型数据 (需要分促销品和产品模块)
        List<Map<String, Object>> messageList = this.splitMessageMap(map);
        for (int i = 0; i < messageList.size(); i++) {
        	Map<String, Object> messageMap = messageList.get(i);
        	if ("1".equals(messageMap.get("isPromotionFlag"))) {
        		// 促销品库存处理
        		this.promotionInOutStock(messageMap);
        	} else {
        		// 产品库存处理
        		this.productInOutStock(messageMap);
        	}
        }
        
        String pickupMode = ConvertUtil.getString(map.get("pickupMode"));
		if("1".equals(pickupMode)) {
			// 微商城模式
			// 更新订单状态
			this.transferWebService(map);
			// 更新电商订单主表信息
			this.updateESOrderMainInfo(map);
			
		}
		
		this.addMessageLog(map);
        
        // 插入MongoDB
        this.addMongoDBBusLog(map);
        
        // 标记当前BL已经将MQ信息写入MongoDB与MQ收发日志表
        map.put("isInsertMongoDBBusLog", "1");
	}
	
	/**
	 * 更新电商订单主表信息
	 * @param map
	 */
	private void updateESOrderMainInfo(Map<String, Object> map) {
		// 更新订单相关信息
		Map<String, Object> updOrderMap = new HashMap<String, Object>();
		setUpdateInfoMapKey(updOrderMap);
		updOrderMap.put("brandInfoID", map.get("brandInfoID"));
		updOrderMap.put("organizationInfoID", map.get("organizationInfoID"));
		// originalNo:提货时关联的原始销售单
		updOrderMap.put("billCode", map.get("originalNo"));
		updOrderMap.put("departCode", map.get("departCode"));
		updOrderMap.put("employeeCode", map.get("employeeCode"));
		updOrderMap.put("tradeTime", map.get("tradeTime"));
		// 订单状态
		String subType = ConvertUtil.getString(map.get("subType"));
		// 0：已提货；1：退货
		updOrderMap.put("orderStatus", "DG".equals(subType) ? "0" : "1");
		updOrderMap.put("pushFlag", "1");
		mqDG_Service.updateESOrderMainInfo(updOrderMap);
		
	}

	/**
	 * 更新原始预定单的相关信息
	 * @param map
	 */
	private void updateOriginalNSBillInfo(Map<String, Object> map) {
		Map<String, Object> updateNSMap = new HashMap<String, Object>();
		int computeSign = "DG".equals(ConvertUtil.getString(map.get("subType"))) ? 1 : -1;
		updateNSMap.put("BIN_SaleRecordID",map.get("BIN_SaleRecordID"));
		updateNSMap.put("quantity",ConvertUtil.getDouble(map.get("quantity")) * computeSign);
		updateNSMap.put("pickupDate",ConvertUtil.getString(map.get("pickupDate")));
		updateNSMap.put(CherryConstants.UPDATEDBY, "-2");
		updateNSMap.put(CherryConstants.UPDATEPGM, "MqDG");
		// 更新原始预定单的状态,在SQL已经对于数量进行了判断
		mqDG_Service.updateOriginalNSBillInfo(updateNSMap);
		
		List<Map<String, Object>> pickupBillDetailList = (List<Map<String, Object>>)map.get("detailList");
		for(Map<String, Object> pickupBillDetailMap : pickupBillDetailList) {
			Map<String, Object> updateNSDetailMap = new HashMap<String, Object>();
			updateNSDetailMap.put("BIN_SaleRecordID", map.get("BIN_SaleRecordID"));
			updateNSDetailMap.put("productId", pickupBillDetailMap.get("productId"));
			updateNSDetailMap.put("quantity", ConvertUtil.getDouble(pickupBillDetailMap.get("quantity")) * computeSign);
			updateNSDetailMap.put(CherryConstants.UPDATEDBY, "-2");
			updateNSDetailMap.put(CherryConstants.UPDATEPGM, "MqDG");
			mqDG_Service.updateOriginalNSBillDetailInfo(updateNSDetailMap);
		}
	}

	/**
	 * 插入提货单主从表信息
	 * @param map
	 */
	private void insertPickupBillAll(Map<String, Object> map) {
		Map<String, Object> pickupBillMainMap = new HashMap<String, Object>();
		String subType = ConvertUtil.getString(map.get("subType"));
		pickupBillMainMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		pickupBillMainMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
		pickupBillMainMap.put("PickupBillNo", map.get("tradeNoIF"));
		pickupBillMainMap.put("RelevanceNo", ConvertUtil.getString(map.get("relevantNo")));
		pickupBillMainMap.put("Relevance_OrginalNo", map.get("originalNo"));
		pickupBillMainMap.put("BIN_SaleRecordID", map.get("BIN_SaleRecordID"));
		pickupBillMainMap.put("BIN_OrganizationID", map.get("organizationID"));
		pickupBillMainMap.put("CounterCode", map.get("departCode"));
		pickupBillMainMap.put("BIN_EmployeeID", map.get("employeeID"));
		pickupBillMainMap.put("EmployeeCode", map.get("employeeCode"));
		pickupBillMainMap.put("ConsumerType", map.get("consumerType"));
		pickupBillMainMap.put("BIN_MemberInfoID", ConvertUtil.getString(map.get("memberInfoID")));
		pickupBillMainMap.put("MemberCode", map.get("memberCode"));
		pickupBillMainMap.put("MemberLevel", ConvertUtil.getString(map.get("memberLevel")));
		pickupBillMainMap.put("PickupDate", map.get("tradeDate"));
		pickupBillMainMap.put("PickupTime", map.get("tradeTime"));
		pickupBillMainMap.put("PickupQuantity", map.get("quantity"));
		pickupBillMainMap.put("TransactionType", subType);
		pickupBillMainMap.put("ComputeSign","DG".equals(subType) ? "1" : "-1");
		pickupBillMainMap.put("Comments", map.get("comments"));
		pickupBillMainMap.put("SynchFlag", "1");
		this.setUpdateInfoMapKey(pickupBillMainMap);
		int pickupBillMainID = mqDG_Service.insertPickupBillMain(pickupBillMainMap);
		
		List<Map<String, Object>> pickupBillDetailList = (List<Map<String, Object>>)map.get("detailList");
		for(Map<String, Object> pickupBillDetailMap : pickupBillDetailList) {
			pickupBillDetailMap.put("pickupBillMainID", pickupBillMainID);
			this.setUpdateInfoMapKey(pickupBillDetailMap);
		}
		mqDG_Service.insertPickupBillDetail(pickupBillDetailList);
	}

	/**
	 * 接收数据写入MQ收发日志表
	 * @param map
	 * @throws Exception
	 */
	private void addMessageLog(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		paramMap.put("tradeType", map.get("tradeType"));
		paramMap.put("tradeNoIF", map.get("tradeNoIF"));
		paramMap.put("modifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		paramMap.put("counterCode", map.get("departCode"));
		paramMap.put("isPromotionFlag", map.get("isPromotionFlag"));
		paramMap.put("createdBy", "-2");
		paramMap.put("createPGM", "MqDG");
		paramMap.put("updatedBy", "-2");
		paramMap.put("updatePGM", "MqDG");
		// 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
	}
	
	private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
		// 插入MongoDB
        DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		// 业务主体
	    dbObject.put("TradeEntity", "1");
	    // 柜台号
	    dbObject.put("CounterCode", map.get("departCode"));
		//员工代码
		dbObject.put("UserCode", map.get("employeeCode"));
		// 发生时间
		dbObject.put("OccurTime", (String)map.get("tradeTime"));
        // 日志正文
		dbObject.put("Content", "提货");
		// 
		map.put("addMongoDBFlag", "0");
	    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	    map.put("addMongoDBFlag", "1");
		
	}
	
	/**
	 * 分割消息Map(促销品和产品)
	 * @param 
	 * @return 当明细中既有产品也有促销品，将会拆分产品与促销品，用isPromotionFlag字段标记，0：产品；1：促销品；
	 * 			注：1）盘单业务时，若没有明细，根据SubType判断是产品盘点还是促销品盘点(此时返回的是已经标记了isPromotionFlag字段的数据)，
	 * 					若不在盘点已定义的类型中则返回new ArrayList();
	 * 				2）其他业务类型时，若没有明细则直接返回new ArrayList()
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> splitMessageMap(Map<String, Object> productMsgMap) throws Exception{
		// 新建消息List
		List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> productDetailList = (List<Map<String, Object>>)productMsgMap.get("detailList");
		Map<String, Object> prmMsgMap = (Map<String, Object>)ConvertUtil.byteClone(productMsgMap);
		// 产品总数量
		int prtTotalQuantity =0;
		// 产品总金额
		BigDecimal prtTotalAmount =new BigDecimal(0);
		// 促销品总个数
		int prmTotalQuantity = 0;
		// 促销品总金额
		BigDecimal prmTotalAmount = new BigDecimal(0);
		List<Map<String, Object>> prmDetailList = (List<Map<String, Object>>)prmMsgMap.get("detailList");
		if(null != prmDetailList && prmDetailList.size()>0) {
			for (int i=0;i<prmDetailList.size();i++){
				Map<String, Object> prmDetailMap = prmDetailList.get(i);
				String isPromotionFlag = (String)prmDetailMap.get("isPromotionFlag");
				int prmQuantity = 0;
				if (!"".equals(ConvertUtil.getString(prmDetailMap.get("quantity")))){
					prmQuantity = (int)Math.floor(ConvertUtil.getFloat(prmDetailMap.get("quantity")));
				}
				
				BigDecimal prmAmount = new BigDecimal(0);
				if (!"".equals(ConvertUtil.getString(prmDetailMap.get("tagPrice")))){
					prmAmount = new BigDecimal(Double.parseDouble(ConvertUtil.getString(prmDetailMap.get("tagPrice"))));
				}
				// 该商品是正常产品
				if ("0".equals(isPromotionFlag)){
					// 产品总个数
					prtTotalQuantity +=prmQuantity;
					// 产品总金额
					prtTotalAmount = prtTotalAmount.add(prmAmount.multiply(new BigDecimal(prmQuantity)));		
					prmDetailList.remove(i);
					i--;
					continue;
				}else{
					// 促销品总个数
					prmTotalQuantity +=prmQuantity;
					// 促销品总金额
					prmTotalAmount = prmTotalAmount.add(prmAmount.multiply(new BigDecimal(prmQuantity)));
				}
				prmDetailMap.put("detailNo", i+1);
			}
		}
		
		
		// 产品的总数量
		productMsgMap.put("totalQuantity", prtTotalQuantity);
		// 促销产品总数量
		prmMsgMap.put("totalQuantity", prmTotalQuantity);
		if(null != productDetailList && productDetailList.size()>0) {
			for (int i = 0; i < productDetailList.size(); i++) {
				Map<String, Object> productDetailMap = productDetailList.get(i);
				String isPromotionFlag = ConvertUtil.getString(productDetailMap.get("isPromotionFlag"));
				if ("1".equals(isPromotionFlag)){
					productDetailList.remove(i);
					i--;
				}
				productDetailMap.put("detailNo", i+1);
			}
		}
		DecimalFormat df = new DecimalFormat("#0.00"); 
		
		
		// 如果有促销品数据
		if (null != prmDetailList && !prmDetailList.isEmpty()){
			prmMsgMap.put("isPromotionFlag", "1");
			prmMsgMap.put("totalAmount", df.format(prmTotalAmount));
			messageList.add(prmMsgMap);
		} 
		
		// 如果有产品数据
		if (null != productDetailList && !productDetailList.isEmpty()){
			productMsgMap.put("isPromotionFlag", "0");
			productMsgMap.put("totalAmount", df.format(prtTotalAmount));
			messageList.add(productMsgMap);
		} 
		
		return messageList;
	}

	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "-2");
		map.put("createPGM", "MqDG");
		map.put("updatedBy", "-2");
		map.put("updatePGM", "MqDG");
	}
	
	/**
	 * 促销品库存处理
	 * @param map
	 */
	private void promotionInOutStock(Map<String, Object> map) {
		Map<String,Object> mainData = new HashMap<String,Object>();
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
        String subType = ConvertUtil.getString(map.get("subType"));
        String stockType = "DG".equals(subType) ? "1" : "0";
        mainData.put("BIN_OrganizationInfoID", organizationInfoId);
        mainData.put("BIN_BrandInfoID", brandInfoId);
        // 入出库单据号还是以IO开头
        String tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,"-2","IO");
        // 入出库单据号
        mainData.put("TradeNo", tradeNo);
        mainData.put("TradeNoIF", tradeNo);
        //主表      关联单号
        mainData.put("RelevantNo",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        // 1:出库；0：入库
        mainData.put("StockType", stockType);
        mainData.put("TradeType", subType);
        mainData.put("Reason", map.get("comments"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("tradeDateTime"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        //主表     物流ID
        mainData.put("BIN_LogisticInfoID", "0");
        // 总数量与总金额通过明细计算得到
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        //入出库前促销品总金额
        mainData.put("TotalAmountBefore", null);
		//入出库后促销品总金额
        mainData.put("TotalAmountAfter", null);
        //主表    有效区分
        mainData.put("ValidFlag", "1");
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //insert 【促销产品入出库表】并返回自增长ID
		int promotionStockInOutID = binOLSSCM01_Service.insertPromotionStockInOutMain(mainData);
        List<Map<String, Object>> pickupBillDetailList = (List<Map<String, Object>>)map.get("detailList");
		
		for (int i = 0; i < pickupBillDetailList.size(); i++) {
			Map<String,Object> stockInOutDetailMap = pickupBillDetailList.get(i);
			Map<String, Object> detailData = new HashMap<String, Object>();
			detailData.put("BIN_PromotionStockInOutID", promotionStockInOutID);
			detailData.put("BIN_PromotionProductVendorID", stockInOutDetailMap.get("productId"));
			//明细表    明细连番
			detailData.put("DetailNo", i+1);
			detailData.put("Quantity", stockInOutDetailMap.get("quantity"));
			detailData.put("Price", stockInOutDetailMap.get("tagPrice"));
			detailData.put("BIN_ProductVendorPackageID", stockInOutDetailMap.get("productVendorPackageID"));
			detailData.put("StockType", stockType);
			detailData.put("BIN_InventoryInfoID", map.get("inventoryInfoID"));
			detailData.put("BIN_LogicInventoryInfoID", stockInOutDetailMap.get("logicInventoryInfoID"));
			detailData.put("BIN_StorageLocationInfoID", stockInOutDetailMap.get("storageLocationInfoID"));
			detailData.put("Reason", stockInOutDetailMap.get("reason"));
			detailData.put("CreatedBy", map.get("createdBy"));
			detailData.put("CreatePGM", map.get("createPGM"));
			detailData.put("UpdatedBy", map.get("updatedBy"));
			detailData.put("UpdatePGM", map.get("updatePGM"));
			
			binOLSSCM01_Service.insertPromotionStockDetail(detailData);
			detailData.clear();detailData=null;
		}
		
		binOLSSCM01_BL.updatePromotionStockByInOutID(promotionStockInOutID,"MqDG",-2);
		
	}
	
	/**
	 * 产品库存处理
	 * @param map
	 */
    private void productInOutStock(Map<String,Object> map){
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
        String subType = ConvertUtil.getString(map.get("subType"));
        String stockType = "DG".equals(subType) ? "1" : "0";
        mainData.put("BIN_OrganizationInfoID", organizationInfoId);
        mainData.put("BIN_BrandInfoID", brandInfoId);
        // 入出库单号还是以IO开头
        String tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,"-2","IO");
        // 入出库单据号
        mainData.put("TradeNo", tradeNo);
        mainData.put("TradeNoIF", tradeNo);
        mainData.put("RelevanceNo",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        // 1:出库；0：入库
        mainData.put("StockType", stockType);
        mainData.put("TradeType", subType);
        mainData.put("Comments", map.get("comments"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("tradeDateTime"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        // 这两个字段无值，为保留字段
        mainData.put("TotalAmountBefore", null);
        mainData.put("TotalAmountAfter", null);
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        List<Map<String, Object>> pickupBillDetailList = (List<Map<String, Object>>)map.get("detailList");
        for(int i=0;i<pickupBillDetailList.size();i++){
            Map<String,Object> detailDataMap = pickupBillDetailList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productId"));
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            // 数量如被转为0，将不写入出库记录
            int quantity = (int)Math.floor(ConvertUtil.getFloat(detailDataMap.get("quantity")));
            detailMap.put("Quantity", quantity);
            detailMap.put("Price", detailDataMap.get("tagPrice"));
            detailMap.put("StockType", stockType);
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", map.get("createdBy"));
            detailMap.put("CreatePGM", map.get("createPGM"));
            detailMap.put("UpdatedBy", map.get("updatedBy"));
            detailMap.put("UpdatePGM", map.get("updatePGM"));
            
            totalQuantity += quantity; 
            String price = ConvertUtil.getString(detailDataMap.get("tagPrice"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            detailList.add(detailMap);
        }
        // 总数量与总金额通过明细计算得到
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        
        //将产品入出库信息写入入出库主从表。
        int productInOutID = binOLSTCM01_BL.insertProductInOutAll(mainData, detailList);
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", productInOutID);
        praMap.put("CreatedBy", map.get("updatedBy"));
        praMap.put("CreatePGM", map.get("updatePGM"));
        //根据入出库单据修改库存。
        //该方法根据入出库记录的明细来更改【产品库存表】，如果该明细中的产品批次ID不为空，则还会更新【产品批次库存表】
        binOLSTCM01_BL.changeStock(praMap);
        
    }
	
	
	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {
		// 子类型。DG：提货  RG：取消提货（全退）
		String subType = ConvertUtil.getString(map.get("subType"));
		// 提货时，提货关联的预付单号必填
		if("DG".equals(subType)) {
			if("".equals(ConvertUtil.getString(map.get("originalNo")))) {
				MessageUtil.addMessageWarning(map,"提货业务，关联的预定单号必填！");
			} else {
				// 提货，关联单必需存在
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tradeNoIF", map.get("originalNo"));
				Map<String, Object> originalNoMap = mqDG_Service.getPrePayBillByNo(paramMap);
				paramMap.clear();paramMap=null;
				if(null == originalNoMap || originalNoMap.isEmpty()) {
					MessageUtil.addMessageWarning(map,"预定未成功时，又进行了关联提货业务！"+"预定单为\""+map.get("originalNo")+"\"");
				}
				// 提货时记录提货关联的预定单销售主ID
				map.put("BIN_SaleRecordID", originalNoMap.get("BIN_SaleRecordID"));
			}
		}
		
		// 取消业务时，关联单据号必填
		if("RG".equals(subType)) {
			if("".equals(ConvertUtil.getString(map.get("relevantNo")))) {
				MessageUtil.addMessageWarning(map,"取消提货，关联单必填！");
			} else {
				// 取消提货，关联单必需存在
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tradeNoIF", map.get("relevantNo"));
				Map<String, Object> relevantNoMap = mqDG_Service.getPickupBillByNo(paramMap);
				paramMap.clear();
				if(null == relevantNoMap || relevantNoMap.isEmpty()) {
					MessageUtil.addMessageWarning(map,"提货未成功时，又进行了关联提货取消业务！"+"关联单为\""+map.get("relevantNo")+"\"");
				}
				// 提货取消时也应更新原始预定单的状态
				map.put("BIN_SaleRecordID", relevantNoMap.get("BIN_SaleRecordID"));
				// 获取待取消的提货单明细，作为取消提货单据的明细
				paramMap.put("pickupBillMainID", relevantNoMap.get("BIN_PickupBillMainID"));
				List<Map<String, Object>> detailList = mqDG_Service.getPickupBillDetailByID(paramMap);
				paramMap.clear();paramMap=null;
				map.put("RelevantDetail", detailList);
			}
		}
		
		// 获取部门信息
		Map<String, Object> departInfo = binBEMQMES96_BL.getDepartInfo(map);
		
		String organizationID = "";
		if(null == departInfo || departInfo.isEmpty()) {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map,"部门号为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_06);
		} else {
			organizationID = ConvertUtil.getString(departInfo.get("organizationID"));
			// 设定部门ID
			map.put("organizationID", organizationID);
			// 设定部门名称
			map.put("departName", departInfo.get("departName"));
			// 用后即焚
			departInfo.clear();
			departInfo = null;
		}
		
		// 获取员工信息
		Map<String, Object> employeeInfo = binBEMQMES96_BL.getEmployeeInfo(map);
		if(null == employeeInfo || employeeInfo.isEmpty()) {
			// 没有查询到相关员工信息
			MessageUtil.addMessageWarning(map,"员工号为\""+map.get("employeeCode")+"\""+MessageConstants.MSG_ERROR_07);
		} else {
			// 设定员工ID
			map.put("employeeID", employeeInfo.get("employeeID"));
			// 设定员工姓名
			map.put("employeeName", employeeInfo.get("employeeName"));
			//设定岗位ID
			map.put("positionCategoryID", employeeInfo.get("positionCategoryID"));
			// 设定员工岗位
			map.put("categoryName", employeeInfo.get("categoryName"));
			// 用后即焚
			employeeInfo.clear();
			employeeInfo = null;
		}
		
		// 查询仓库信息
		List<Map<String, Object>> depotList = binBEMQMES96_BL.getDepotsByDepartID(organizationID);
//		List<Map<String, Object>> depotList = binOLCM18_BL.getDepotsByDepartID(organizationID, "");
		if(depotList != null && !depotList.isEmpty()){
			// 设定实体仓库ID
			map.put("inventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
			// 用后即焚
			depotList.clear();
			depotList = null;
		}
		
		if("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))){
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"部门为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_36);
		}
		
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		// 设定交易时间
		map.put("tradeDateTime",map.get("tradeTime"));
		if("".equals(tradeDate)) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeDate"));
		} else if(!DateUtil.checkDate(tradeDate,"yyyy-MM-dd")){
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_75, "TradeDate","yyyy-MM-dd"));
		}
		
		if("".equals(tradeTime)) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeTime"));
		} else if(!DateUtil.checkDate(tradeTime,"yyyy-MM-dd HH:mm:ss")) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_75, "TradeTime","yyyy-MM-dd HH:mm:ss"));
		}
		
		// 校验PickupDate格式
		String pickupDate = ConvertUtil.getString(map.get("pickupDate"));
		if(!"".equals(pickupDate) && !CherryChecker.checkDate(pickupDate,"yyyy-MM-dd")) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_75, "PickupDate","yyyy-MM-dd"));
		}
		// 消费者类型为会员时才查询会员信息
		String consumerType = ConvertUtil.getString(map.get("consumerType"));
		if("MP".equals(consumerType)) {
			String memberCode = ConvertUtil.getString(map.get("memberCode"));
			if (!"".equals(memberCode) && !MessageConstants.ON_MEMBER_CARD.equals(memberCode)){
				// 取得会员数据
				Map<String, Object> memberInfoMap = binBEMQMES99_Service.selMemberInfo(map);
				if (memberInfoMap != null && memberInfoMap.get("memberInfoID") != null) {
					// 设定会员ID
					map.put("memberInfoID", memberInfoMap.get("memberInfoID"));
					map.put("memName", memberInfoMap.get("memName"));
				}
			}
		}
		// 校验明细中的产品及仓库相关信息
		List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailList");
		if(null != detailDataList && !detailDataList.isEmpty()) {
			for(int i=0; i < detailDataList.size(); i++) {
				Map<String, Object> detailDataMap = detailDataList.get(i);
				//当明细中的数量、金额为空时设为0
	            if("".equals(ConvertUtil.getString(detailDataMap.get("quantity")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Quantity"));
	            }
				// 明细连番
	            detailDataMap.put("detailNo", i + 1);
				// 员工ID
	            detailDataMap.put("employeeID", map.get("employeeID"));
				// 实体仓库ID
	            detailDataMap.put("inventoryInfoID", map.get("inventoryInfoID"));
	
	            //逻辑仓库
	            Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
	            logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
	            logicInventoryInfoMap.put("LogicInventoryCode", detailDataMap.get("logicInventoryCode"));
	            logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
	            logicInventoryInfoMap.put("language", null);
	            Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
	            if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
	                int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
	                // 设定逻辑仓库ID
	                detailDataMap.put("logicInventoryInfoID", logicInventoryInfoID);
	            }else{
	                // 没有查询到相关逻辑仓库信息
	                MessageUtil.addMessageWarning(map,"逻辑仓库为\""+detailDataMap.get("logicInventoryCode")+"\""+MessageConstants.MSG_ERROR_37);
	            }
				
				binBEMQMES96_BL.setDetailProPrmIDInfo(detailDataMap, map);
				
				//设定包装类型ID  仓库库位ID
				detailDataMap.put("productVendorPackageID", 0);
				detailDataMap.put("storageLocationInfoID", 0);
				// 设定统一的产品、促销品ID的KEY
				String isPromotionFlag = ConvertUtil.getString(detailDataMap.get("isPromotionFlag"));
				String productIdKey = "0".equals(isPromotionFlag) ? "productVendorID" : "promotionProductVendorID";
				detailDataMap.put("productId", detailDataMap.get(productIdKey));
				
			}
		} else if("RG".equals(subType)) {
			// 取消类型支持无明细模式，此时取原单进行取消,将原单明细组装为MQ明细的格式
			map.put("detailList", map.get("RelevantDetail"));
			map.remove("RelevantDetail");
			detailDataList = (List<Map<String,Object>>)map.get("detailList");
			if(null == detailDataList || detailDataList.isEmpty()) {
				MessageUtil.addMessageWarning(map,"提货取消的原始提货单据明细为空！"+"原始提货单据为\""+map.get("relevantNo")+"\"");
			} else {
				for(int i=0; i < detailDataList.size(); i++) {
					Map<String, Object> detailDataMap = detailDataList.get(i);
					// 员工ID
		            detailDataMap.put("employeeID", map.get("employeeID"));
					// 实体仓库ID
		            detailDataMap.put("inventoryInfoID", map.get("inventoryInfoID"));
		            // 逻辑仓库
		            Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
		            logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
		            logicInventoryInfoMap.put("LogicInventoryCode", detailDataMap.get("logicInventoryCode"));
		            logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
		            logicInventoryInfoMap.put("language", null);
		            Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
		            logicInventoryInfoMap.clear();logicInventoryInfoMap=null;
		            if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
		                int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
		                // 设定逻辑仓库ID
		                detailDataMap.put("logicInventoryInfoID", logicInventoryInfoID);
					} else {
		                // 没有查询到相关逻辑仓库信息
		                MessageUtil.addMessageWarning(map,"逻辑仓库为\""+detailDataMap.get("InventoryTypeCode")+"\""+MessageConstants.MSG_ERROR_37);
		            }
		            
		            //设定包装类型ID  仓库库位ID
					detailDataMap.put("productVendorPackageID", 0);
					detailDataMap.put("storageLocationInfoID", 0);
					
				}
			}
			
		}
	}
	
	/**
	 * 调用微商城Webservice并把订单状态信息推送到微商城
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	private void transferWebService(Map<String, Object> map) throws Exception {
		Map<String, Object> wsMap = new HashMap<String, Object>();
		// 业务类型
		wsMap.put("TradeType", "UpdateOrderStatus");
		// 发货柜台
		wsMap.put("CounterCode", map.get("departCode"));
		// 发货BA
		wsMap.put("BaCode", map.get("employeeCode"));
		// 订单号
		wsMap.put("OrderSn", map.get("originalNo"));
		// 发货时间
		wsMap.put("DeliveryDate", map.get("tradeTime"));
		// 订单状态
		String subType = ConvertUtil.getString(map.get("subType"));
		// 0：已提货；1：退货
		wsMap.put("OrderStatus", "DG".equals(subType) ? "0" : "1");
		// 调用微商城接口
		logger.info("********************执行推送订单至微商城WS处理***************************");
		Map<String, Object> resultMap = WebserviceClient.accessWeshopWebService(wsMap);
		String errCode = ConvertUtil.getString(resultMap.get("ERRORCODE"));
		String errMsg = ConvertUtil.getString(resultMap.get("ERRORMSG"));
		if (!"0".equals(errCode)) {
			MessageUtil.addMessageWarning(map,"推送订单至微商城WS处理【UpdateOrderStatus】出现业务问题；订单号\""+map.get("originalNo")+"\";错误信息："+errMsg);
		}
		
	}

}
