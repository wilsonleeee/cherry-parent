/*		
 * @(#)BINBEMQMES01_BL.java     1.0 2010/12/01		
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES01_Service;
import com.cherry.mq.mes.service.BINBEMQMES02_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM07_IF;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.opensymphony.workflow.loader.ActionDescriptor;

/**
 * 促销品消息数据接收处理BL
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES01_BL implements AnalyzeMessage_IF{

	@Resource(name="binBEMQMES01_Service")
	private BINBEMQMES01_Service binBEMQMES01_Service;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binOLSSCM00_BL;
	
    @Resource(name="binOLSSCM03_BL")
    private BINOLSSCM03_BL binOLSSCM03_BL;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binOLSSCM04_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_BL binOLCM19_BL;
	
	@Resource(name="binBEMQMES02_Service")
	private BINBEMQMES02_Service binBEMQMES02_Service;
	
    @Resource(name="binOLSSCM07_BL")
    private BINOLSSCM07_IF binOLSSCM07_BL;
	
	@Resource(name="binOLSSCM08_BL")
	private BINOLSSCM08_IF binOLSSCM08_BL;
	
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_IF binOLSSCM09_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES01_BL.class);
	
	/**
	 * 对销售/退货数据进行处理
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void analyzeSaleStockData(Map map) {

		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		HashMap detailDataDTO = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		// 新后台业务类型
		map.put("cherry_tradeType", String.valueOf(detailDataDTO.get("stockType")).equals(MessageConstants.STOCK_TYPE_OUT) ? MessageConstants.CHERRY_TRADETYPE_SALE
				: MessageConstants.CHERRY_TRADETYPE_RETURN_PURCHASE);

		// 设定入出库区分
		if (map.get("saleSRtype")!=null){
			if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
				map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
			}else{
				map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
			}
		}
		
		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		
		// 设定入出库关联单据号(销售单)
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
		// 插入促销品入出库表,入出库明细表
		this.addPromotionStockInfo(detailDataList, map, organizationInfoID, brandInfoID);
		// 操作库存数据
		this.operateStockData(detailDataList, map);
	
	}

    /**
     * 对销售/退货数据进行处理(库存处理)(新消息体Type=0007)
     * 
     * @param map
     * @throws Exception
     */
    public void analyzeSaleReturnStockData(Map map) throws Exception {

        // 取得组织ID
        String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
        // 取得品牌ID
        String brandInfoID = String.valueOf(map.get("brandInfoID"));
        HashMap detailDataDTO = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
//        // 新后台业务类型
//        map.put("cherry_tradeType", String.valueOf(detailDataDTO.get("stockType")).equals(MessageConstants.STOCK_TYPE_OUT) ? MessageConstants.CHERRY_TRADETYPE_SALE
//                : MessageConstants.CHERRY_TRADETYPE_RETURN_PURCHASE);

        // 设定入出库区分
        if (map.get("saleSRtype")!=null){
            if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
                map.put("cherry_tradeType",MessageConstants.CHERRY_TRADETYPE_SALE);
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
            }else{
                map.put("cherry_tradeType",MessageConstants.CHERRY_TRADETYPE_RETURN_PURCHASE);
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
            }
        }
        
        // 插入促销品库存操作流水表
        this.addPromotionInventoryLog(map);
        // 明细数据List
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        
        // 设定入出库关联单据号(销售单)
        map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
        // 插入促销品入出库表,入出库明细表
        this.addPromotionStockInfoForSale(detailDataList, map, organizationInfoID, brandInfoID);
        // 操作库存数据
        this.operateStockDataForSale(detailDataList, map);
    
    }
	
	/**
	 * 对入库/退库数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeStockData(Map map) throws Exception {
		// 审核区分
		map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_AGREE);
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		HashMap detailDataDTO = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		String stockType = String.valueOf(detailDataDTO.get("stockType"));
		// 用于判断当前的MQ数据将走的哪一个业务
		String cherry_tradeType = "";
		
		String relevantNoFlag = "";
	    String workFlowID = "";
	    int relevantBillID = 0;
		//查询关联单号
        if(map.get("relevantNo") != null && !"".equals(map.get("relevantNo"))){
            // step1：【判断关联单号是否为发货单--收货业务】
            Map resultRecMap =  binBEMQMES01_Service.selPromotionDeliverInfo(map);
            if(resultRecMap != null && resultRecMap.size() > 0){
                // 收货（订发货/发货流程）
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_RECEIVE;
                map.put("sendOrganizationID", resultRecMap.get("sendOrganizationID"));
                map.put("workFlowID", resultRecMap.get("workFlowID"));
                relevantNoFlag = ConvertUtil.getString(map.get("relevantNo"));
            }else{
                // 促销品没有退库申请流程【注：促销品与产品不一致，产品是有退库申请流程的】
                // step2：【判断关联单号是否为入库单--确认入库】
                List<Map<String,Object>> inDepotList = binOLSSCM09_BL.selPrmInDepot(ConvertUtil.getString(map.get("relevantNo")));
                if(null != inDepotList && inDepotList.size() > 0){
                    // 入库流程
                    cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN;
                    workFlowID = ConvertUtil.getString(inDepotList.get(0).get("WorkFlowID"));
                    relevantNoFlag = ConvertUtil.getString(map.get("relevantNo"));
                    relevantBillID = CherryUtil.obj2int(inDepotList.get(0).get("BIN_PrmInDepotID"));
                    map.put("WorkFlowID", workFlowID);
                    map.put("BIN_PrmInDepotID", relevantBillID);
                }
            }
        }
		
        /**
         *  当关联单号确实存在，无需再根据subType判断；
         *  反之，在判断完此MQ的关联单号不在【发货单、入库单】中，则需要通过判断子类型来确定是【入库】还是【退库】业务。
         */
        // step3：【无关联单号或者关联单号不在指定单据范围内--通过子类型为判断是入库还是退库】
        if("".equals(relevantNoFlag)){
            String subType = ConvertUtil.getString(map.get("subType"));
            if(subType.equals(MessageConstants.BUSINESS_TYPE_GR)){
                // 入库
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN;
            }else if(subType.equals(MessageConstants.BUSINESS_TYPE_RR)){
                // 退库
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS;
            }
        }
        
		if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_RECEIVE)) {
			// 仓库收货
			map.put("cherry_tradeType", cherry_tradeType);
			map.put("stockInFlag", "4");
			map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);//设置入出库状态为入库
		}else if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS)){
			// 仓库退货
			map.put("cherry_tradeType", cherry_tradeType);
			map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);//设置入出库状态为出库
		}

		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");

		int promotionDeliverID = 0;
		if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_RECEIVE)) {
			//收货
//			//查询关联单号
//			if(map.get("relevantNo")!=null && !"".equals(map.get("relevantNo"))){
//				Map resultRecMap =  binBEMQMES01_Service.selPromotionDeliverInfo(map);
//				if(resultRecMap!=null&&resultRecMap.size()>0){
//					map.put("sendOrganizationID", resultRecMap.get("sendOrganizationID"));
//					map.put("workFlowID", resultRecMap.get("workFlowID"));
//				}
//			}
			
			// 收发货单据采番
			//String deliver_receiveNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", cherry_tradeType);
			map.put("deliver_receiveNo", map.get("tradeNoIF"));
			// 插入促销品收发货单据表
			promotionDeliverID = binBEMQMES01_Service.addPromotionDeliver(map);
			// 更新关联单据的入库区分
			binBEMQMES01_Service.updPromotionDeliver(map);
			
			// 循环明细数据List
			List deliverDetailList = (List) ConvertUtil.byteClone(detailDataList);
			//总数量
			int totalQuantity = 0;
			//总金额
			BigDecimal totalAmount = new BigDecimal(0);
			for (int i = 0; i < deliverDetailList.size(); i++) {
				HashMap detailDataDTOMap = (HashMap) deliverDetailList.get(i);
				// 设定促销产品收发货ID
				detailDataDTOMap.put("promotionDeliverID", promotionDeliverID);
				// 根据该条明细的入出库区分，把数量转成“入库，数量”。
				// 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到收货单据中；
				// 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到收货单据中。
				if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataDTOMap.get("stockType")))){
				    detailDataDTOMap.put("quantity", CherryUtil.obj2int(detailDataDTOMap.get("quantity"))*-1);
				}
				//同时存在入库出库，重新计算总数量、总金额。
				int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
				totalQuantity += quantity;
				if(null != detailDataDTOMap.get("price") && !"".equals(detailDataDTOMap.get("price"))){
				    BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailDataDTOMap.get("price")));
				    totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
				}
			}
			// 批量插入促销产品收发货业务单据明细表 
			binBEMQMES01_Service.addPromotionDeliverDetail(deliverDetailList);
			
			// 设定入出库关联单据号(收货单)
			map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
			
			//判断map的总数量、总金额和计算出的总数量、总金额是否一致，不一致更新主表、map。
			DecimalFormat df=new DecimalFormat("#0.00");
			String strTotalAmount = df.format(totalAmount);
			if(CherryUtil.obj2int(map.get("totalQuantity")) != totalQuantity || !ConvertUtil.getString(map.get("totalAmount")).equals(strTotalAmount)){
			    Map<String,Object> update = new HashMap<String,Object>();
			    update.put("UpdatedBy", map.get("updatedBy"));
			    update.put("UpdatePGM", map.get("updatePGM"));
			    update.put("TotalQuantity", totalQuantity);
			    update.put("TotalAmount", strTotalAmount);
			    update.put("BIN_PromotionDeliverID", promotionDeliverID);
			    binOLSSCM04_BL.updatePrmDeliverMain(update);
			    map.put("totalQuantity", totalQuantity);
			    map.put("totalAmount", strTotalAmount);
			}
		}else if(cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS)){
			// 退货单单据采番
			//String returnNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", cherry_tradeType);
			map.put("returnNo", map.get("tradeNoIF"));
			// 取得促销产品退货ID
			int promotionReturnID = binBEMQMES01_Service.addPromotionReturn(map);
			// 循环明细数据List
			List deliverDetailList = (List) ConvertUtil.byteClone(detailDataList);
			//总数量
			int totalQuantity = 0;
			//总金额
			BigDecimal totalAmount = new BigDecimal(0);
			for (int i = 0; i < deliverDetailList.size(); i++) {
				HashMap detailDataDTOMap = (HashMap) deliverDetailList.get(i);
				// 设定促销产品退库ID
				detailDataDTOMap.put("promotionReturnID", promotionReturnID);
				// 根据该条明细的入出库区分，把数量转成“出库，数量”。
				// 如果该明细的入出库区分是“入库”，那么需要将明细中的数量×-1后写到退库单据中；
				// 如果该明细的入出库区分是“出库”，那么直接将明细中的数量写到退库单据中。
				if(MessageConstants.STOCK_TYPE_IN.equals(ConvertUtil.getString(detailDataDTOMap.get("stockType")))){
				    detailDataDTOMap.put("quantity", CherryUtil.obj2int(detailDataDTOMap.get("quantity"))*-1);
				}
				//同时存在入库出库，重新计算总数量、总金额。
				int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
				totalQuantity += quantity;
                if(null != detailDataDTOMap.get("price") && !"".equals(detailDataDTOMap.get("price"))){
                    BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailDataDTOMap.get("price")));
                    totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
                }
			}
			// 批量插入促销产品退库业务单据明细表 
			binBEMQMES01_Service.addPromotionReturnDetail(deliverDetailList);
			// 设定入出库关联单据号(退库单)
			map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
			
            //判断map的总数量、总金额和计算出的总数量、总金额是否一致，不一致更新主表、map。
            DecimalFormat df=new DecimalFormat("#0.00");
            String strTotalAmount = df.format(totalAmount);
            if(CherryUtil.obj2int(map.get("totalQuantity")) != totalQuantity || !ConvertUtil.getString(map.get("totalAmount")).equals(strTotalAmount)){
                Map<String,Object> update = new HashMap<String,Object>();
                update.put("UpdatedBy", map.get("updatedBy"));
                update.put("UpdatePGM", map.get("updatePGM"));
                update.put("TotalQuantity", totalQuantity);
                update.put("TotalAmount", strTotalAmount);
                update.put("BIN_PromotionReturnID", promotionReturnID);
                binBEMQMES01_Service.updPromotionReturn(update);
                map.put("totalQuantity", totalQuantity);
                map.put("totalAmount", strTotalAmount);
            }
		}else if(cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN)){
		    //入库
            if(!"".equals(workFlowID)){
                //有工作流入库
                List<Map<String,Object>> cloneDetailList = (List<Map<String, Object>>) ConvertUtil.byteClone(detailDataList);
                this.prmInDepotOSWF(cloneDetailList,map);
            }else{
                //无工作流入库及操作入出库表、库存
                List<Map<String,Object>> cloneDetailList = (List<Map<String, Object>>) ConvertUtil.byteClone(detailDataList);
                this.prmInDepotNoOSWF(cloneDetailList,map);
            }
            return;
		}

		// 插入促销品入出库表,入出库明细表
		this.addPromotionStockInfo(detailDataList, map, organizationInfoID, brandInfoID);
		
		// 操作库存数据
		this.operateStockData(detailDataList, map);
		
		//if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_RECEIVE)) {
			//收货后，要来完成工作流
			Map<String,Object> mainData = new HashMap<String,Object>();
			mainData.put("DeliverReceiveNo", map.get("relevantNo"));
			mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
			mainData.put("BIN_EmployeeID", map.get("employeeID"));
			mainData.put("DeliverReceiveNoIF", map.get("tradeNoIF"));
			mainData.put("BIN_PromotionReceiveID", promotionDeliverID);
			mainData.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
			mainData.put("CreatedBy", map.get("createdBy"));
			mainData.put("UpdatedBy", map.get("updatedBy"));
			binOLSSCM00_BL.posReceiveFinishFlow(mainData);
		//}
	}
	
    /**
     * 终端做入库时无工作流
     * @throws Exception 
     */
	public void prmInDepotNoOSWF(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        //单据号采番
        //String billNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", MessageConstants.BUSINESS_TYPE_GR);
        //map.put("BillNoIF", map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("BillNo",map.get("tradeNoIF"));
        mainData.put("BillNoIF",map.get("tradeNoIF"));
        mainData.put("RelevanceNo",map.get("relevantNo"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("BIN_LogisticInfoID", 0);
        mainData.put("Comments", map.get("reason"));
        mainData.put("InDepotDate", map.get("tradeDate"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("TradeStatus", CherryConstants.BILLTYPE_GR_FINISH);
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        for(int i=0;i<cloneDetailList.size();i++){
            Map<String,Object> detailDataMap = (Map<String, Object>) cloneDetailList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            
            // 根据该条明细的入出库区分，把数量转成“入库，数量”。
            // 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到入库单据中；
            // 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到入库单据中。
            int quantity = CherryUtil.obj2int(detailDataMap.get("quantity"));
            if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
                quantity = quantity*-1;
            }
            //同时存在入库出库，重新计算总数量、总金额。
            totalQuantity += quantity;
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            
            detailMap.put("BIN_PromotionProductVendorID", detailDataMap.get("promotionProductVendorID"));
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            detailMap.put("Quantity", quantity);
            detailMap.put("PreQuantity", quantity);
            detailMap.put("Price", price);
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", strTotalAmount);
        mainData.put("PreTotalQuantity", totalQuantity);
        mainData.put("PreTotalAmount", strTotalAmount);
        map.put("totalQuantity", totalQuantity);
        map.put("totalAmount", strTotalAmount);
        
        //将促销品入库信息写入入库主从表。
        int prmInDepotID = binOLSSCM09_BL.insertPrmInDepotAll(mainData, detailList);
        this.prmInDepotStock(detailDataList, map);
	}
	
	/**
     * 终端做入库（无工作流）时，写出入出库表，并更改库存
     */
    public void prmInDepotStock(List<Map<String,Object>> detailDataList, Map<String,Object> map){
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
//        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
//        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
//        //单据号采番
//        String stockInOut_tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", MessageConstants.BUSINESS_TYPE_GR);
//        map.put("stockInOut_tradeNo", stockInOut_tradeNo);
//        map.put("stockInOut_tradeNoIF", stockInOut_tradeNo);
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
//        mainData.put("TradeNo",map.get("stockInOut_tradeNo"));
//        mainData.put("TradeNoIF",map.get("stockInOut_tradeNoIF"));
        mainData.put("RelevantNo",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        mainData.put("StockType", CherryConstants.STOCK_TYPE_IN);
        mainData.put("TradeType", MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN);//自由入库
//      mainData.put("BIN_LogisticInfoID", map.get(""));
        mainData.put("Comments", map.get("comments"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("tradeDateTime"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("TotalAmountBefore", map.get("totalAmountBefore"));
        mainData.put("TotalAmountAfter", map.get("totalAmountAfter"));
//      mainData.put("CloseFlag", "");
//      mainData.put("ChangeCount", "");
//      mainData.put("WorkFlowID", map.get(""));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            
            //排除不扣库存、数量为0的明细
            int quantity = CherryUtil.obj2int(detailDataMap.get("quantity"));
            boolean isNoStockFlag = detailDataMap.get("isStock")!=null&&detailDataMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
            if(isNoStockFlag || quantity ==0){
                continue;
            }
            
            //重新计算总数量、总金额。
            totalQuantity += quantity;
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_PromotionProductVendorID", detailDataMap.get("promotionProductVendorID"));
            detailMap.put("DetailNo", detailList.size()+1);
            detailMap.put("Quantity", quantity);
            detailMap.put("Price", price);
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("StockType", detailDataMap.get("stockType"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Comments", detailDataMap.get("comments"));
//          detailMap.put("ChangeCount", detailDataMap.get(""));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        if(detailList.size() == 0){
            return;
        }
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        //将促销品入出库信息写入入出库主从表。
        int prmInOutID = binOLSSCM07_BL.insertPromotionStockInOutAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_PromotionStockInOutID", prmInOutID);
        praMap.put("CreatedBy", map.get("updatedBy"));
        praMap.put("CreatePGM", map.get("updatePGM"));
        //根据入出库单据修改库存。
        //该方法根据入出库记录的明细来更改【促销品库存表】
        binOLSSCM07_BL.changeStock(praMap);
    }
    
    /**
     * 终端做入库时(入库流程)
     * @throws Exception 
     */
    public void prmInDepotOSWF(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
        int prmInDepotMainID = CherryUtil.obj2int(map.get("BIN_PrmInDepotID"));
        String workFlowID = ConvertUtil.getString(map.get("WorkFlowID"));
        
        //原入库申请明细
        List<Map<String,Object>> oldList = binOLSSCM09_BL.getPrmInDepotDetailData(prmInDepotMainID, null);
        int inventoryInfoID = 0;
//        int logicInventoryInfoID = 0;
        //实际入库明细与申请明细对比
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDTO = detailDataList.get(i);
            if(i == 0){
                inventoryInfoID = CherryUtil.obj2int(detailDTO.get("inventoryInfoID"));
//                logicInventoryInfoID = CherryUtil.obj2int(detailDTO.get("logicInventoryInfoID"));
            }
            int prmVendorID = CherryUtil.obj2int(detailDTO.get("promotionProductVendorID"));
            for(int j=0;j<oldList.size();j++){
                Map<String,Object> curdetailDTO = oldList.get(j);
                int curPrmVendorID = CherryUtil.obj2int(curdetailDTO.get("BIN_PromotionProductVendorID"));
                if(prmVendorID == curPrmVendorID){
                    detailDTO.put("PreQuantity", curdetailDTO.get("PreQuantity"));
                    oldList.remove(j);
                    break;
                }
            }
            detailDTO.put("PreQuantity", CherryUtil.obj2int(detailDTO.get("PreQuantity")));
        }
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        int detailNo = 0;
        for(int i=0;i<detailDataList.size();i++){
            detailNo = detailNo+1;
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_PrmInDepotID", prmInDepotMainID);
            detailMap.put("BIN_PromotionProductVendorID", detailDataMap.get("promotionProductVendorID"));
            detailMap.put("DetailNo", detailNo);
            // 根据该条明细的入出库区分，把数量转成“入库，数量”。
            // 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到入库单据中；
            // 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到入库单据中。
            int quantity = CherryUtil.obj2int(detailDataMap.get("quantity"));
            if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
                quantity = quantity*-1;
            }
            //同时存在入库出库，重新计算总数量、总金额。
            totalQuantity += quantity;
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            detailMap.put("Quantity", quantity);
            detailMap.put("PreQuantity", detailDataMap.get("PreQuantity"));
            detailMap.put("Price", detailDataMap.get("price"));
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        //申请明细存在，入库单不存在，入库数量为0。
        for(int i=0;i<oldList.size();i++){
            detailNo = detailNo+1;
            Map<String,Object> oldDetailMap = oldList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_PrmInDepotID", prmInDepotMainID);
            detailMap.put("BIN_PromotionProductVendorID", oldDetailMap.get("BIN_PromotionProductVendorID"));
            detailMap.put("DetailNo", detailNo);
            detailMap.put("Quantity", 0);
            detailMap.put("PreQuantity", oldDetailMap.get("PreQuantity"));
            detailMap.put("Price", oldDetailMap.get("Price"));
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", inventoryInfoID);
            detailMap.put("BIN_LogicInventoryInfoID", oldDetailMap.get("BIN_LogicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", oldDetailMap.get("Comments"));
            detailMap.put("CreatedBy", map.get("updatedBy"));
            detailMap.put("CreatePGM", map.get("updatePGM"));
            detailMap.put("UpdatedBy", map.get("updatedBy"));
            detailMap.put("UpdatePGM", map.get("updatePGM"));
            detailList.add(detailMap);
        }
        
        //删除原有明细
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_PrmInDepotID", prmInDepotMainID);
        binOLSSCM09_BL.delPrmInDepotDetailData(praMap);
        
        //最后的明细是申请明细+入库明细的并集
        binOLSSCM09_BL.insertPrmInDepotDetail(detailList);
        
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        
        //更新主表信息
        praMap = new HashMap<String,Object>();
        praMap.put("BIN_PrmInDepotID", prmInDepotMainID);
        praMap.put("TotalQuantity", totalQuantity);
        praMap.put("TotalAmount", strTotalAmount);
        praMap.put("InDepotDate", map.get("tradeDate"));
        praMap.put("RelevanceNo", map.get("tradeNoIF"));//原申请单的关联单号填写终端入库MQ的单据号。
        praMap.put("UpdatedBy", map.get("updatedBy"));
        praMap.put("UpdatePGM", map.get("updatePGM"));
        binOLSSCM09_BL.updatePrmInDepotMain(praMap);
        
        //调用工作流
        long osID = Long.parseLong(workFlowID);
        ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
        int actionID = 0;
        if(null != adArr && adArr.length>0){
            Map metaMapTMp = null;
            for (int j = 0; j < adArr.length; j++) {
                metaMapTMp = adArr[j].getMetaAttributes();
                //找到带有OS_DefaultAction元素的action
                if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
                    String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
                    if("autoAgree".equals(defaultAction)){
                        ActionDescriptor ad = adArr[j];
                        actionID = ad.getId();
                        break;
                    }
                }
            }
            if(actionID == 0){
                MessageUtil.addMessageWarning(map,"执行确认入库时，无法找到当前能执行Action");
            }
        }else{
            MessageUtil.addMessageWarning(map,"执行确认入库时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                    "涉及主要参数：工作流ID\""+osID+"\"");
        }
        
        UserInfo userInfo = new UserInfo();
        userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

        //查询用户表获得用户ID
        Map userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        }
        
        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        mainDataMap.put("entryID", osID);
        mainDataMap.put("actionID", actionID);
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
        mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
        mainDataMap.put("BrandCode", map.get("brandCode").toString());
        mainDataMap.put("CurrentUnit", "BINBEMQMES02");
        mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainDataMap.put("UserInfo", userInfo);
        mainDataMap.put("OrganizationCode",map.get("counterCode"));//入库部门编号
        binOLSSCM00_BL.DoAction(mainDataMap);
    }
	
	
	/**
	 * 对调入申请单数据进行处理
	 * @param map
	 */
	public void analyzeAllocationInData(Map map){
		// 审核区分
		map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_AGREE);
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		// 新后台业务类型
		String cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_ALLOCATION_IN_APPLY;
		map.put("cherry_tradeType",cherry_tradeType);
		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		// 调拨区分		
		map.put("allocationFlag", MessageConstants.ALLOCATION_FLAG_IN);
		this.addPromotionAllocationInfo(detailDataList, map, organizationInfoID, brandInfoID, cherry_tradeType);
	}
	
	/**
	 * 对调出确认单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeAllocationOutData(Map map) throws Exception{
		// 审核区分
		map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_AGREE);
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		// 新后台业务类型
		String cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_ALLOCATION_OUT_CONFIRM;
		map.put("cherry_tradeType",cherry_tradeType);
		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		// 调拨区分		
		map.put("allocationFlag", MessageConstants.ALLOCATION_FLAG_OUT);
		// 取得关联单号
		String relevantNo = (String)map.get("relevantNo");
		if (relevantNo!=null && !"".equals(relevantNo)){
			// 如果是终端发起的调拨业务【注意：此处的逻辑比较特殊，即认定终端发起的调入申请必定不是以“BG”开头】
			if (!relevantNo.substring(0, 2).equals("BG")){
				// 查询调入申请单新后台单据号
				HashMap resultMap = binBEMQMES01_Service.selPrmAllocationNo(map);
				if (resultMap!=null && resultMap.get("allocationNoIF")!=null){
					map.put("relevantNo", resultMap.get("allocationNoIF"));
					map.put("relevantEmployeeID", resultMap.get("employeeID"));
				}
			}
		}
		// 设定促销产品价格为调拨价格
		map.put("promotion_price", map.get("allocationPrice"));
		// 插入调拨单据表,调拨单据明细表
		this.addPromotionAllocationInfo(detailDataList, map, organizationInfoID, brandInfoID, cherry_tradeType);
		
		// 设定入出库关联单号(调拨单)
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
		map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);//设置入出库状态为出库
		// 插入入出库表(调出方)
		this.addPromotionStockInfo(detailDataList, map, organizationInfoID, brandInfoID);	
		// 设定调入方信息
		HashMap allocationInMap = (HashMap)ConvertUtil.byteClone(map);
		// 调入方部门ID
		allocationInMap.put("organizationID", allocationInMap.get("relevantOrganizationID"));
		// 员工ID
		allocationInMap.put("employeeID", allocationInMap.get("relevantEmployeeID"));
		
		List andetailDataList = (List)allocationInMap.get("detailDataDTOList");
		
		for (int i=0;i<andetailDataList.size();i++){
			HashMap andetailDataMap = (HashMap)andetailDataList.get(i);
			// 设定关联部门实体仓库ID
			if (andetailDataMap.get("relevantInventoryInfoID")!=null && !"".equals(andetailDataMap.get("relevantInventoryInfoID"))){
				andetailDataMap.put("inventoryInfoID", andetailDataMap.get("relevantInventoryInfoID"));
			}
			// 调入方库存类型为0(入库)
			andetailDataMap.put("stockType","0");
		}
		// 此处的关联单号为调入单的单号
		allocationInMap.put("stockInOut_relevantNo", map.get("relevantNo"));
		// 插入入出库表(调入方)
		allocationInMap.put("cherry_tradeType", MessageConstants.CHERRY_TRADETYPE_ALLOCATION_IN_APPLY);
		allocationInMap.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);//设置入出库状态为入库
		this.addPromotionStockInfo(andetailDataList, allocationInMap, organizationInfoID, brandInfoID);
		
		// 操作库存数据(调出方)
		this.operateStockData(detailDataList, map);
		
		// 操作库存数据(调入方)
		this.operateStockData(andetailDataList, allocationInMap);
		
		
	}
	
    @Override
    public void analyzeAllocationInConfirmData(Map<String, Object> map) throws Exception {}
	
	/**
	 * 对盘点单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeStockTakingData(Map map) throws Exception{
		// 审核区分
		map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_AGREE);
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		// 新后台业务类型
		String cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_TAKING;
		map.put("cherry_tradeType",cherry_tradeType);
		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		boolean isNullDetail = (null == detailDataList || detailDataList.size()==0);
		
		// 盘点单据采番
		//String stockTaking_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", cherry_tradeType);
		map.put("stockTaking_no", map.get("tradeNoIF"));
		
		// 插入促销品盘点业务单据表
		int promotionStockTakingID = binBEMQMES01_Service.addPromotionStockTaking(map);
		// 盘点单MQ没有明细时不写明细表及入出库表
		if(!isNullDetail) {
			// 循环明细数据List
			for (int i = 0; i < detailDataList.size(); i++) {
				HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
				// 促销品库存查询
				HashMap resultMap = binBEMQMES01_Service.selPrmStockNumInfo(detailDataDTOMap);
				// 如果库存结果不为空,则更新数据
				if (resultMap != null && resultMap.get("stockQuantity") != null) {
					int stockQuantity = CherryUtil.obj2int(String.valueOf(resultMap.get("stockQuantity")));
					
					int quantityBefore = CherryUtil.obj2int(detailDataDTOMap.get("quantityBefore").toString());
					if(quantityBefore!=stockQuantity){
						logger.info("盘点时，账面数量("+quantityBefore+")与库存数量("+stockQuantity+")不相等;" +
							        "其中盘差数为\""+detailDataDTOMap.get("quantity")+"\",单据号为\""+map.get("tradeNoIF")+
							        "\",厂商编码为\""+detailDataDTOMap.get("unitcode")+"\",产品条码为\""+detailDataDTOMap.get("barcode")+"\"");
					}
				}
				int quantity = CherryUtil.obj2int((String.valueOf(detailDataDTOMap.get("quantity"))));
				// 设定促销产品盘点ID
				detailDataDTOMap.put("promotionStockTakingID", promotionStockTakingID);
				// 如果是出库
				if (detailDataDTOMap.get("stockType").equals(MessageConstants.STOCK_TYPE_OUT)){
					if (detailDataDTOMap.get("quantity")!=null && !"".equals("quantity")){
						// 则盘查为负数
						int gainQuantity = quantity * (-1);
						detailDataDTOMap.put("gainQuantity", gainQuantity);
					}
				}else{
					detailDataDTOMap.put("gainQuantity", quantity);
				}
			}
			
			// 批量插入盘点详细表
			binBEMQMES01_Service.addPromotionTakingDetail(detailDataList);
			
			//盘点业务需要将入出库业务单据主表的总数量设定为正整数
			if(map.get("totalQuantity")!=null&&!map.get("totalQuantity").equals("")){
				int totalQuantity = CherryUtil.obj2int(map.get("totalQuantity").toString());
				if(totalQuantity<0){
					map.put("totalQuantity", String.valueOf(CherryUtil.obj2int(map.get("totalQuantity").toString())*(-1)));
					map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
					if(map.get("totalAmount")!=null&&!map.get("totalAmount").equals("")){
						BigDecimal bigdecimal = new BigDecimal((String)map.get("totalAmount"));
						map.put("totalAmount", bigdecimal.multiply(new BigDecimal(-1)));
			        }
				}else{
					map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
				}
			}
			 
			// 设定入出库关联单据号(盘点单)
			map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
			// 插入促销品入出库表,入出库明细表
			this.addPromotionStockInfo(detailDataList, map, organizationInfoID, brandInfoID);
			
			// 操作库存数据
			this.operateStockData(detailDataList, map);
		}
	}
	
	/**
	 * 对生日礼领用单数据进行处理
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void analyzeStockBirPresentData(Map map)throws Exception {
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		// 新后台业务类型
		String cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_BIRPRSSENT;
		map.put("cherry_tradeType",cherry_tradeType);
		// 插入促销品库存操作流水表
		this.addPromotionInventoryLog(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		
        int giftDrawID = 0;
        int detailNo = 0;
        String stockBirPresent_no = "";
        
        //判断预约单是否重复领用，对于重复领用的预约单抛错
        String campaignOrderID = "";
        String campaignOrderState = "";
        String activityType = MessageConstants.ACTIVITYTYPE_PROM;//0：促销活动
        List<Map<String,Object>> campaignOrderList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> campaignOrderDetailList = new ArrayList<Map<String,Object>>();
        Map<String,Object> mainCodeMap = new HashMap<String,Object>();
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        if(relevantNo.length()>0){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("TradeNoIF", relevantNo);
            campaignOrderList = binBEMQMES02_Service.getCampaignOrderList(paramMap);
            if(null != campaignOrderList && campaignOrderList.size()>0){
                campaignOrderState = ConvertUtil.getString(campaignOrderList.get(0).get("State"));
                if(campaignOrderState.equals(MessageConstants.CAMPAIGNORDER_STATE_OK)){
                    //预约单状态已领用，判断领用单据号是否存在，存在说明已经同时存在产品和促销品。
                    paramMap = new HashMap<String,Object>();
                    paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
                    paramMap.put("BIN_BrandInfoID", brandInfoID);
                    paramMap.put("TradeNoIF", map.get("tradeNoIF"));
                    List<Map<String,Object>> giftDrawList = binBEMQMES02_Service.selGiftDrawNoIF(paramMap);
                    if(null == giftDrawList || giftDrawList.size() == 0){
                        MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_72);
                    }
                }
                String campaignCode = ConvertUtil.getString(campaignOrderList.get(0).get("CampaignCode"));
                map.put("campaignCode", campaignCode);
                
                campaignOrderID = ConvertUtil.getString(campaignOrderList.get(0).get("BIN_CampaignOrderID"));
                paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_CampaignOrderID", campaignOrderID);
                //根据预约ID查出会员活动预约明细表的所有明细
                campaignOrderDetailList = binBEMQMES02_Service.getCampaignOrderDetailList(paramMap);
                
                //在会员活动表用campaignCode查询，如果找到就是会员活动。
                paramMap = new HashMap<String,Object>();
                paramMap.put("CampaignCode", campaignCode);
                List<Map<String,Object>> campaignList = binBEMQMES02_Service.getCampaignList(paramMap);
                if(null != campaignList && campaignList.size()>0){
                    activityType = MessageConstants.ACTIVITYTYPE_MEM;//1：会员活动
                }
                
                //取已插入会员活动履历表的MainCode
                paramMap = new HashMap<String,Object>();
                paramMap.put("CampaignCode", campaignCode);
                paramMap.put("TradeNoIF", map.get("tradeNoIF"));
                paramMap.put("TradeType",map.get("tradeType"));
                paramMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);
                List<Map<String,Object>> campaignHistoryList = binBEMQMES02_Service.getCampaignHistoryList(paramMap);
                for(int i=0;i<campaignHistoryList.size();i++){
                    mainCodeMap.put(ConvertUtil.getString(campaignHistoryList.get(i).get("MainCode")), null);
                }
            }
        }
        
        //判断礼品领用表是否已经存在，存在用相同giftDrawID
        Map<String,Object> giftDrawParam = new HashMap<String,Object>();
        giftDrawParam.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        giftDrawParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
        giftDrawParam.put("TradeNoIF", map.get("tradeNoIF"));
        List<Map<String,Object>> maxDetailList = binBEMQMES02_Service.getMaxSPDetailList(giftDrawParam);
        if(null != maxDetailList && maxDetailList.size()>0){
            giftDrawID = CherryUtil.obj2int(maxDetailList.get(0).get("BIN_GiftDrawID"));
            detailNo = CherryUtil.obj2int(maxDetailList.get(0).get("MaxDetailNo"));
        }else{
            // 生日礼领用单据采番
        	stockBirPresent_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", cherry_tradeType);
        	map.put("stockBirPresent_no", stockBirPresent_no);
        }

		// 设定入出库关联单据号(生日礼领用单)
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
		map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
		
        String couponCode = "";
        String memberInfoID = ConvertUtil.getString(map.get("memberInfoID"));

        //根据CouponCode取出CampaignCode（表BIN_CampaignOrder），MainCode（表BIN_CampaignOrderDetail）
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>)detailDataList.get(i);
            //couponCode在主表不支持一个消息体有多个couponCode
            //写入主表的couponCode只取第一次出现的couponCode
            if("".equals(couponCode)){
                couponCode = ConvertUtil.getString(detailDataDTOMap.get("couponCode"));
                map.put("couponCode", couponCode);
            }
            
            if(!"".equals(relevantNo)){
                //与查出的会员活动预约明细比较（UnitCode+BarCode），找到在消息体中存在的明细，设置ActivityType、MainCode
                if(null != campaignOrderDetailList && campaignOrderDetailList.size()>0){
                    String unitCode = ConvertUtil.getString(detailDataDTOMap.get("unitcode"));
                    String barCode = ConvertUtil.getString(detailDataDTOMap.get("barcode"));
                    detailDataDTOMap.put("ActivityType", activityType);//（0：促销活动，1：会员活动）
                    for(int j=0;j<campaignOrderDetailList.size();j++){
                        HashMap<String,Object> campaignOrderDetailDTOMap = (HashMap<String,Object>)campaignOrderDetailList.get(j);
                        String curUnitCode = ConvertUtil.getString(campaignOrderDetailDTOMap.get("UnitCode"));
                        String curBarCode = ConvertUtil.getString(campaignOrderDetailDTOMap.get("BarCode"));
                        String giftType = ConvertUtil.getString(campaignOrderDetailDTOMap.get("GiftType"));
                        if(MessageConstants.SALE_TYPE_PROMOTION_SALE.equals(giftType)){
                            if(unitCode.equals(curUnitCode) && barCode.equals(curBarCode)){
                                detailDataDTOMap.put("mainCode", campaignOrderDetailDTOMap.get("MainCode"));
                            }
                        }
                    }
                }
            }
        }
		
        if(!"".equals(stockBirPresent_no)){
            //插入礼品领用主表
            giftDrawID = binBEMQMES02_Service.addGiftDraw(map);
        }

        //会员参与活动履历表List
        List<Map<String,Object>> campaignHistory = new ArrayList<Map<String,Object>>();
        
        //系统配置项配置的是否扣库存，默认为扣库存。
        String isStockSysConfig = binOLCM14_BL.getConfigValue("1036", organizationInfoID, brandInfoID);
        
        //插入礼品领用明细表
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>)detailDataList.get(i);
            detailNo = detailNo+1;
            detailDataDTOMap.put("detailNo", detailNo);
            detailDataDTOMap.put("giftDrawID", giftDrawID);
            detailDataDTOMap.put("giftType", MessageConstants.SALE_TYPE_PROMOTION_SALE);
            //产品礼品领用和促销品礼品领用的ID用同一个ID
            detailDataDTOMap.put("productVendorID", detailDataDTOMap.get("promotionProductVendorID"));
            
            //促销品礼品领用是否扣库存以消息体上传上来的IsStock为准，IsStock没有的话看系统配置项的值。
            //如果系统配置项不扣库存，设置不扣库存。
            //如果系统配置项扣库存，需要根据该促销品的是否管理库存属性，来设置是否扣库存。
            String isStockMQ = ConvertUtil.getString(detailDataDTOMap.get("isStockMQ"));
            if(!"".equals(isStockMQ)){
                detailDataDTOMap.put("isStock", isStockMQ);
            }else{
                if(CherryConstants.SYSTEM_CONFIG_DISENABLE.equals(isStockSysConfig)){
                    detailDataDTOMap.put("isStock", isStockSysConfig);
                }
            }
            
            //会员领用且MainCode不为空时需要写会员参与活动履历表
            String mainCode = ConvertUtil.getString(detailDataDTOMap.get("mainCode"));
            if(!"".equals(memberInfoID) && !"".equals(mainCode)){
                //剔除MainCode重复
                if(!mainCodeMap.containsKey(mainCode)){
                    Map<String,Object> campaignHistoryDTO = new HashMap<String,Object>();
                    campaignHistoryDTO.put("OrgCode", map.get("orgCode"));
                    campaignHistoryDTO.put("BrandCode", map.get("brandCode"));
                    campaignHistoryDTO.put("BIN_MemberInfoID", map.get("memberInfoID"));
                    campaignHistoryDTO.put("CampaignType", detailDataDTOMap.get("ActivityType"));
                    campaignHistoryDTO.put("CampaignCode", map.get("campaignCode"));
                    campaignHistoryDTO.put("MainCode", detailDataDTOMap.get("mainCode"));
                    campaignHistoryDTO.put("TradeNoIF", map.get("tradeNoIF"));
                    campaignHistoryDTO.put("TradeType", map.get("tradeType"));
                    campaignHistoryDTO.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已经领用
                    campaignHistoryDTO.put("BIN_OrganizationID", map.get("organizationID"));
                    campaignHistoryDTO.put("ParticipateTime", map.get("tradeDateTime"));
                    campaignHistoryDTO.put("InformType", map.get("informType"));
                    this.setInsertInfoMapKey(campaignHistoryDTO);
                    campaignHistory.add(campaignHistoryDTO);
                    mainCodeMap.put(mainCode, null);
                }
            }
        }
        binBEMQMES02_Service.addGiftDrawDetail(detailDataList);
        
        // 插入促销品入出库表,入出库明细表
        this.addPromotionStockInfo(detailDataList, map, organizationInfoID, brandInfoID);
        // 操作库存数据
        this.operateStockData(detailDataList, map);
        
        //插入会员参与活动履历表
        if(null != campaignHistory && campaignHistory.size()>0){
            binBEMQMES02_Service.addCampaignHistory(campaignHistory);
        }

        if(!campaignOrderState.equals(MessageConstants.CAMPAIGNORDER_STATE_OK)){
            //更新会员活动预约主表的预约单状态
            Map<String,Object> updateMap = new HashMap<String,Object>();
            updateMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已经领用
            updateMap.put("FinishTime", map.get("tradeDateTime"));//领用时间
            updateMap.put("BIN_CampaignOrderID", campaignOrderID);
            updateMap.put("CounterGot", map.get("counterCode"));//领取柜台
            this.setInsertInfoMapKey(updateMap);
            binBEMQMES02_Service.updCampaignOrderState(updateMap);
        }
	}

	/**
	 * 插入促销品库存操作流水表
	 * @param map
	 */
	private void addPromotionInventoryLog(Map map){
		// 插入促销品库存操作流水表
//		int promotionInventoryLogID = binBEMQMES01_Service.addPromotionInventoryLog(map);
		// 设定促销品库存操作流水ID
		map.put("promotionInventoryLogID", 1);
	}
	
	/**
	 * 插入促销品入出库表,入出库明细表
	 * @param detailDataList
	 * @param map
	 * @param organizationInfoID
	 * @param brandInfoID
	 */
	private void addPromotionStockInfo (List detailDataList,Map map,String organizationInfoID,String brandInfoID){
		// 判断是否是修改销售记录
		String ticketType = (String)map.get("ticket_type");
		// 入出库时间
		map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
		if (ticketType!=null && ticketType.equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
			// 单据类型为修改销售记录
			
			// 查询促销产品入出库主表的数据
			Map historyStockInOutMap = binBEMQMES01_Service.selPrmProductInOut(map);
			// 设定新后台入出库单据号
//			map.put("stockInOut_tradeNo", historyStockInOutMap.get("stockInOut_tradeNo"));
//			map.put("stockInOut_tradeNoIf", historyStockInOutMap.get("stockInOut_tradeNoIF"));
			if(historyStockInOutMap!=null){
				// 查询促销产品入出库明细表的数据
				List historyStockInOutDetailList = binBEMQMES01_Service.selPrmProductInOutDetail(historyStockInOutMap);
				if(historyStockInOutDetailList!=null){
					// 插入历史促销产品入出库主数据表
					int productInOutHistoryID = binBEMQMES01_Service.addPrmProductInOutHistory(historyStockInOutMap);
					for (int i=0;i<historyStockInOutDetailList.size();i++){
						Map historyStockInOutDetailMap = (Map)historyStockInOutDetailList.get(i);
						// 设置历史促销产品入出库主表主ID
						historyStockInOutDetailMap.put("productInOutHistoryID", productInOutHistoryID);
					}
					// 插入历史产品入出库明细表
					binBEMQMES01_Service.addPrmProductInOutDetailHistory(historyStockInOutDetailList);
			    }
			}
			// 删除原有的产品入出库记录
//			binBEMQMES01_Service.delPrmProductInOut(historyStockInOutMap);
//			binBEMQMES01_Service.delPrmProductInOutDetail(historyStockInOutMap);
		}
		
        //当明细数量全是0，入出库主表从表都不插入，其他情况只插入数量不是0的明细
        int totalQuantity = 0;
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
            int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
            String price = ConvertUtil.getString(detailDataDTOMap.get("price"));
            boolean isNoStockFlag = detailDataDTOMap.get("isStock")!=null&&detailDataDTOMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
            if(CherryUtil.obj2int(detailDataDTOMap.get("quantity")) == 0){
                detailDataList.remove(i);
                i--;
                continue;
            }else if(isNoStockFlag){
                //根据促销品的是否管理库存属性，过滤属性为”否“的促销品。
                detailDataList.remove(i);
                i--;
                continue;
            }
            totalQuantity += quantity;
            if(null != price && !"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
        }
        if(detailDataList.size()==0){
            return;
        }
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        map.put("totalQuantity", totalQuantity);
        map.put("totalAmount", strTotalAmount);
		
		// 入出库单据采番
		String stockInOut_tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", "7");
		// 入出库时间
		map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
		map.put("stockInOut_tradeNo", stockInOut_tradeNo);
		String cherry_tradeType = (String)map.get("cherry_tradeType");
//		if (MessageConstants.CHERRY_TRADETYPE_BIRPRSSENT.equals(cherry_tradeType)){
//			map.put("stockInOut_tradeNoIf", map.get("tradeNoIF"));
//		}else{
//			map.put("stockInOut_tradeNoIf", stockInOut_tradeNo);
//		}
		//礼品领用主表已有，不需要再取接口消息体的单据号
		map.put("stockInOut_tradeNoIf", stockInOut_tradeNo);
		// 插入促销品入出库表
		int promotionStockInOutID = binBEMQMES01_Service.addPromotionStockInOut(map);

		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定促销产品入出库记录ID
			detailDataDTOMap.put("promotionStockInOutID", promotionStockInOutID);
			
			detailDataDTOMap.put("detailNo", i+1);
			
			//对修改销售记录，对冲数据中的reason设为“原单对冲”
			if(map.get("modifyCounts")!=null&&detailDataDTOMap.get("modifyCounts")!=null&&
					!map.get("modifyCounts").equals(detailDataDTOMap.get("modifyCounts"))){
				detailDataDTOMap.put("reason", "原单对冲");
			}
		}
		// 批量插入促销产品入出库明细表
		binBEMQMES01_Service.addPromotionStockDetail(detailDataList);
	}
	
    /**
     * 插入促销品入出库表,入出库明细表(新消息体Type=0007)
     * @param detailDataList
     * @param map
     * @param organizationInfoID
     * @param brandInfoID
     * @throws Exception 
     */
    private void addPromotionStockInfoForSale (List detailDataList,Map map,String organizationInfoID,String brandInfoID) throws Exception{
        // 判断是否是修改销售记录
        String ticketType = ConvertUtil.getString(map.get("ticket_type"));
        // 入出库时间
        map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
        Map<Integer,Object> quantityMap = new HashMap<Integer,Object>();
        String closeFlag = "";
        String old_stockInOut_tradeNo = "";
        String old_stockInOut_tradeNoIF = "";
        if(ticketType.equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE) || ticketType.equals(MessageConstants.PX_TYPE_1)){
            // 单据类型为修改销售记录
            
            // 查询促销产品入出库主表的数据
            Map<String,Object> historyStockInOutMap = binBEMQMES01_Service.selPrmProductInOut(map);
            // 设定新后台入出库单据号
//          map.put("stockInOut_tradeNo", historyStockInOutMap.get("stockInOut_tradeNo"));
//          map.put("stockInOut_tradeNoIf", historyStockInOutMap.get("stockInOut_tradeNoIF"));
            if(historyStockInOutMap!=null){
                // 查询促销产品入出库明细表的数据
                List<Map<String,Object>> historyStockInOutDetailList = binBEMQMES01_Service.selPrmProductInOutDetail(historyStockInOutMap);

                closeFlag = ConvertUtil.getString(historyStockInOutMap.get("closeFlag"));
                if(historyStockInOutDetailList!=null){
                    if(CherryConstants.CLOSEFLAG_NO.equals(closeFlag)){
                        //原入出库单据还未进行过月度库存计算
                        // 插入历史促销产品入出库主数据表
                        this.setInsertInfoMapKey(historyStockInOutMap);
                        int promoStockInOutHisID = binBEMQMES01_Service.addPrmProductInOutHistory(historyStockInOutMap);
                        for (int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> historyStockInOutDetailMap = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            int promotionProductVendorID = CherryUtil.obj2int(historyStockInOutDetailMap.get("productVendorID"));
                            // 设置历史促销产品入出库主表主ID
                            historyStockInOutDetailMap.put("promotionStockInOutID", promoStockInOutHisID);
                            historyStockInOutDetailMap.put("promotionProductVendorID", promotionProductVendorID);
                            this.setInsertInfoMapKey(historyStockInOutDetailMap);

                            String stockType = ConvertUtil.getString(historyStockInOutDetailMap.get("stockType"));
                            int quantity = CherryUtil.obj2int(historyStockInOutDetailMap.get("quantity"));
                            if(stockType.equals(CherryConstants.STOCK_TYPE_OUT)){
                                quantity = quantity*-1;
                            }
                            quantityMap.put(promotionProductVendorID, quantity);
                        }
                        // 插入历史促销产品入出库明细表
                        binBEMQMES01_Service.addPrmProductInOutDetailHistory(historyStockInOutDetailList);
                        
                        // 删除原有的促销品入出库记录
                        binBEMQMES01_Service.delPrmProductInOut(historyStockInOutMap);
                        binBEMQMES01_Service.delPrmProductInOutDetail(historyStockInOutMap);
                        
                        old_stockInOut_tradeNo = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNo"));
                        old_stockInOut_tradeNoIF = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNoIF"));
                    }else if(CherryConstants.CLOSEFLAG_YES.equals(closeFlag)){
                        //原入出库单据已经进行过月度库存计算
                        //原单对冲
                        String oldBillNoIF = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNoIF"));
                        String reason = "原单("+oldBillNoIF+")对冲";
                        int totalQuantity = 0;
                        BigDecimal totalAmount = new BigDecimal(0);
                        for(int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> temp = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            int quantity = CherryUtil.obj2int(temp.get("quantity"))*-1;
                            BigDecimal amount = new BigDecimal(Double.parseDouble(ConvertUtil.getString(temp.get("price"))));
                            temp.put("quantity", quantity);
                            temp.put("reason", reason);
                            totalQuantity += quantity;
                            totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
                        }
                        // 入出库对冲单据采番
                        String stockInOut_tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", "7");
                        historyStockInOutMap.put("stockInOut_tradeNo", stockInOut_tradeNo);
                        historyStockInOutMap.put("stockInOut_tradeNoIf", stockInOut_tradeNo);
                        historyStockInOutMap.put("totalQuantity", totalQuantity);
                        historyStockInOutMap.put("totalAmount", totalAmount);
                        historyStockInOutMap.put("reason", reason);
                        this.setInsertInfoMapKey(historyStockInOutMap);
                        int billID = binBEMQMES01_Service.addPromotionStockInOut(historyStockInOutMap);
                        for(int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> temp = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            temp.put("promotionStockInOutID", billID);
                            temp.put("promotionProductVendorID", temp.get("productVendorID"));
                            temp.put("promotion_price", temp.get("price"));
                            this.setInsertInfoMapKey(temp);
                            int quantity = CherryUtil.obj2int(temp.get("quantity"));
                            String stockType = ConvertUtil.getString(temp.get("stockType"));
                            if(stockType.equals(CherryConstants.STOCK_TYPE_IN)){
                                quantity = quantity*-1;
                            }
                            int promotionProductVendorID = CherryUtil.obj2int(temp.get("productVendorID"));
                            quantityMap.put(promotionProductVendorID, quantity);
                        }
                        binBEMQMES01_Service.addPromotionStockDetail(historyStockInOutDetailList);
                    }
                }
            }
        }
        
        String mainModifyCounts = ConvertUtil.getString(map.get("modifyCounts"));
        //把新单存在的促销品ID都放到Map，便于判断是否排除在对冲单存在，在新单不存在的明细。
        Map<String,Object> existNewDetailMap = new HashMap<String,Object>();
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            String detailModifyCounts = ConvertUtil.getString(detailDataDTOMap.get("modifyCounts"));
            if(mainModifyCounts.equals(detailModifyCounts)){
                String promotionProductVendorID = ConvertUtil.getString(detailDataDTOMap.get("promotionProductVendorID"));
                existNewDetailMap.put(promotionProductVendorID, null);
            }
        }
        
        //当明细数量全是0，入出库主表从表都不插入，其他情况只插入数量不是0的明细
        //排除对冲明细
        int totalQuantity = 0;
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
            String price = ConvertUtil.getString(detailDataDTOMap.get("price"));
            String detailModifyCounts = ConvertUtil.getString(detailDataDTOMap.get("modifyCounts"));
            String promotionProductVendorID = ConvertUtil.getString(detailDataDTOMap.get("promotionProductVendorID"));
            boolean isNoStockFlag = detailDataDTOMap.get("isStock")!=null&&detailDataDTOMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
            if(!mainModifyCounts.equals(detailModifyCounts)){
                if(existNewDetailMap.containsKey(promotionProductVendorID)){
                    detailDataList.remove(i);
                    i--;
                    continue;
                }else{
                    detailDataDTOMap.put("quantity", 0);
                }
            }
            if(isNoStockFlag){
                //根据促销品明细的IsStock，过滤值为0不管理的促销品。
                detailDataList.remove(i);
                i--;
                continue;
            }
            totalQuantity += quantity;
            if(null != price && !"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            //修改前入出库数量
            detailDataDTOMap.put("preQuantity", quantityMap.get(CherryUtil.obj2int(detailDataDTOMap.get("promotionProductVendorID"))));
            //修改后入出库数量
            detailDataDTOMap.put("postQuantity", detailDataDTOMap.get("quantity"));
        }
        
        //移除数量为0的明细
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        for(int i=0;i<cloneDetailList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) cloneDetailList.get(i);
            int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
            if(quantity == 0){
                cloneDetailList.remove(i);
                i--;
                continue;
            }
        }
        if(cloneDetailList.size()==0){
            return;
        }
        
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        map.put("totalQuantity", totalQuantity);
        map.put("totalAmount", strTotalAmount);
        
        if(CherryConstants.CLOSEFLAG_NO.equals(closeFlag) && !"".equals(old_stockInOut_tradeNo)){
            //如果原入出库单据还没有进行过月度库存计算，单据号和原单一样。
            map.put("stockInOut_tradeNo", old_stockInOut_tradeNo);
            map.put("stockInOut_tradeNoIf", old_stockInOut_tradeNoIF);
        }else{
            // 入出库单据采番
            String stockInOut_tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", "7");
            map.put("stockInOut_tradeNo", stockInOut_tradeNo);
            map.put("stockInOut_tradeNoIf", stockInOut_tradeNo);
        }
        // 入出库时间
        map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
        
        // 插入促销品入出库表
        int promotionStockInOutID = binBEMQMES01_Service.addPromotionStockInOut(map);

        // 循环明细数据List
        int detailNo = 1;
        for (int i = 0; i < cloneDetailList.size(); i++) {
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) cloneDetailList.get(i);
            // 设定促销产品入出库记录ID
            detailDataDTOMap.put("promotionStockInOutID", promotionStockInOutID);
            detailDataDTOMap.put("detailNo", detailNo);
            detailNo++;
        }
        // 批量插入促销产品入出库明细表
        binBEMQMES01_Service.addPromotionStockDetail(cloneDetailList);
    }
	
	/**
	 * 插入促销品调拨单据表,调拨单据明细表
	 * @param detailDataList
	 * @param map
	 * @param organizationInfoID
	 * @param brandInfoID
	 * @param cherry_tradeType
	 */
	private void addPromotionAllocationInfo(List detailDataList,Map map,String organizationInfoID,String brandInfoID,String cherry_tradeType){
		// 调拨单据采番
		//String allocation_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES01", cherry_tradeType);
		map.put("allocation_no", map.get("tradeNoIF"));
		if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_ALLOCATION_IN_APPLY)){
			// 调入部门
			map.put("inOrganizationID", map.get("organizationID"));
			// 调出部门
			map.put("outOrganizationID", map.get("relevantOrganizationID"));
			//1 未调出
			map.put("tradeStatus", CherryConstants.BILLTYPE_PRM_BG_UNRES);
		}else if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_ALLOCATION_OUT_CONFIRM)){
			// 调入部门
			map.put("inOrganizationID", map.get("relevantOrganizationID"));
			// 调出部门
			map.put("outOrganizationID", map.get("organizationID"));
			//2 已调出
			map.put("tradeStatus", CherryConstants.BILLTYPE_PRM_BGLG_RES);
			
            HashMap<String,Object> resultMap = binBEMQMES01_Service.selPrmAllocationNo(map);
            if (resultMap!=null && resultMap.get("allocationNoIF")!=null){
                String promotionAllocationInID = ConvertUtil.getString(resultMap.get("promotionAllocationID"));
                
                //更新调拨单状态
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_PromotionAllocationID", promotionAllocationInID);
                paramMap.put("TradeStatus", CherryConstants.BILLTYPE_PRM_BGLG_RES);
                paramMap.put("UpdatedBy", "BINBEMQMES01");
                paramMap.put("UpdatePGM", "BINBEMQMES01");
                binOLSSCM03_BL.updateAllocationMain(paramMap);
            }
		}
		// 插入促销产品调拨业务单据表
		int promotionAllocationID = binBEMQMES01_Service.addPromotionAllocation(map);
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定促销产品调拨ID
			detailDataDTOMap.put("promotionAllocationID", promotionAllocationID);
		}
		// 批量插入促销产品调拨业务单据明细表
		binBEMQMES01_Service.addPromotionAllocationDetail(detailDataList);
	}
	
	/**
	 * 操作库存数据
	 * 
	 * @param detailDataList
	 * @param map
	 */
	private void operateStockData(List detailDataList, Map map) {
		// 处理库存明细表
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			int quantity =0,updQuantity = 0;
				
			if (detailDataDTOMap.get("quantity")!=null && !"".equals(detailDataDTOMap.get("quantity"))){
				quantity = Integer.parseInt(String.valueOf(detailDataDTOMap.get("quantity")));
			}
			if (MessageConstants.STOCK_TYPE_OUT.equals(detailDataDTOMap.get("stockType"))) {
				updQuantity -= quantity;
			} else if (MessageConstants.STOCK_TYPE_IN.equals(detailDataDTOMap.get("stockType"))) {
				updQuantity += quantity;
			}
			detailDataDTOMap.put("updQuantity", updQuantity);
			int k = binBEMQMES01_Service.updPromotionStock(detailDataDTOMap);
			if (k==0){
				detailDataDTOMap.put("stockQuantity", updQuantity);
				binBEMQMES01_Service.addPromotionStock(detailDataDTOMap);
			}
		}
	}

    /**
     * 操作库存数据(新消息体Type=0007)
     * 
     * @param detailDataList
     * @param map
     */
    private void operateStockDataForSale(List detailDataList, Map map) {
        // 处理库存明细表
        for (int i = 0; i < detailDataList.size(); i++) {
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            int updQuantity = 0;
                
            //修改后入出库数量-修改前入出库数量
            int postQuantity = CherryUtil.obj2int(detailDataDTOMap.get("postQuantity"));
            int preQuantity = CherryUtil.obj2int(detailDataDTOMap.get("preQuantity"));
            if (MessageConstants.STOCK_TYPE_OUT.equals(detailDataDTOMap.get("stockType"))) {
                updQuantity = postQuantity*-1 - preQuantity;
            } else if (MessageConstants.STOCK_TYPE_IN.equals(detailDataDTOMap.get("stockType"))) {
                updQuantity = postQuantity - preQuantity;
            }
            detailDataDTOMap.put("updQuantity", updQuantity);
            int k = binBEMQMES01_Service.updPromotionStock(detailDataDTOMap);
            if (k==0){
                detailDataDTOMap.put("stockQuantity", updQuantity);
                binBEMQMES01_Service.addPromotionStock(detailDataDTOMap);
            }
        }
    }
	
	@Override
	public void analyzeSaleData(Map<String, Object> map) {}
	
	@Override
	public void analyzeProductOrderData(Map<String, Object> map)throws Exception {}
	
	@Override
	public void analyzeDeliverData(Map<String, Object> map){};
	
	@Override
	public void analyzeShiftData(Map<String, Object> map) throws CherryMQException{
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        List<Map<String,Object>> promotionShiftDetailList = new ArrayList<Map<String,Object>>();
        int detailNo = 1;
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
	    
        for(int i=0;i<detailDataDTOList.size();i++){
            Map<String,Object> detailDataDTO = detailDataDTOList.get(i);
            int promotionProductVendorID = CherryUtil.obj2int(detailDataDTO.get("promotionProductVendorID"));
            String stockType = ConvertUtil.getString(detailDataDTO.get("stockType"));
            String depotInfoID = ConvertUtil.getString(detailDataDTO.get("inventoryInfoID"));
            String logicInventoryInfoID = ConvertUtil.getString(detailDataDTO.get("logicInventoryInfoID"));
            int quantity = CherryUtil.obj2int(detailDataDTO.get("quantity"));
            BigDecimal price = new BigDecimal(0);
            if (detailDataDTO.get("price")!=null && !"".equals(detailDataDTO.get("price"))){
                price = new BigDecimal(Double.parseDouble((String)detailDataDTO.get("price")));
            }
            String unitCode = ConvertUtil.getString(detailDataDTO.get("unitcode"));
            String barCode = ConvertUtil.getString(detailDataDTO.get("barcode"));
            String comments = ConvertUtil.getString(detailDataDTO.get("reason"));
            String createdBy = ConvertUtil.getString(detailDataDTO.get("createdBy"));
            String createPGM = ConvertUtil.getString(detailDataDTO.get("createPGM"));
            String updatedBy = ConvertUtil.getString(detailDataDTO.get("updatedBy"));
            String updatePGM = ConvertUtil.getString(detailDataDTO.get("updatePGM"));
            boolean flag = false;
            for(int j=0;j<promotionShiftDetailList.size();j++){
                Map<String,Object> temp = promotionShiftDetailList.get(j);
                int curPromotionProductVendorID = CherryUtil.obj2int(temp.get("BIN_PromotionProductVendorID"));
                int curQuantity = CherryUtil.obj2int(temp.get("Quantity"));
                if(curPromotionProductVendorID == promotionProductVendorID){
                    if(quantity != curQuantity){
                        //移库单移出数量与移入数量不一致
                        MessageUtil.addMessageWarning(map, "厂商编码为\""+unitCode+"\"促销品条码为\""+barCode+"\""+MessageConstants.MSG_ERROR_50);
                    }
                    flag = true;
                    if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
                        temp.put("FromLogicInventoryInfoID", logicInventoryInfoID);
                    }else if(stockType.equals(MessageConstants.STOCK_TYPE_IN)){
                        temp.put("ToLogicInventoryInfoID", logicInventoryInfoID);
                    }
                }
            }
            
            if(!flag){
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("BIN_PromotionProductVendorID", promotionProductVendorID);
                temp.put("DetailNo", detailNo);
                temp.put("Quantity", quantity);
                temp.put("Price", price);
                temp.put("BIN_ProductVendorPackageID", 0);
                temp.put("FromDepotInfoID", depotInfoID);
                temp.put("ToDepotInfoID", depotInfoID);
                temp.put("FromStorageLocationInfoID", 0);
                temp.put("ToStorageLocationInfoID", 0);
                if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
                    temp.put("FromLogicInventoryInfoID", logicInventoryInfoID);
                }else if(stockType.equals(MessageConstants.STOCK_TYPE_IN)){
                    temp.put("ToLogicInventoryInfoID", logicInventoryInfoID);
                }
                temp.put("unitcode", unitCode);
                temp.put("barcode", barCode);
                temp.put("Comments", comments);
                temp.put("CreatedBy", createdBy);
                temp.put("CreatePGM", createPGM);
                temp.put("UpdatedBy", updatedBy);
                temp.put("UpdatePGM", updatePGM);
                promotionShiftDetailList.add(temp);
                detailNo++;
                
                totalQuantity += quantity;
                totalAmount = totalAmount.add(price.multiply(new BigDecimal(quantity)));
            }
        }
        
        //验证移出、移入逻辑仓库必须同时存在
        for(int i=0;i<promotionShiftDetailList.size();i++){
            Map<String,Object> temp = promotionShiftDetailList.get(i);
            String fromLogicInventoryInfoID = ConvertUtil.getString(temp.get("FromLogicInventoryInfoID"));
            String toLogicInventoryInfoID = ConvertUtil.getString(temp.get("ToLogicInventoryInfoID"));
            if("".equals(fromLogicInventoryInfoID)){
                //移出方的逻辑仓库不存在
                MessageUtil.addMessageWarning(map, "厂商编码为\""+temp.get("unitcode")+"\"促销品条码为\""+temp.get("barcode")+"\""+MessageConstants.MSG_ERROR_51);
            }else if("".equals(toLogicInventoryInfoID)){
                //移入方的逻辑仓库不存在
                MessageUtil.addMessageWarning(map, "厂商编码为\""+temp.get("unitcode")+"\"促销品条码为\""+temp.get("barcode")+"\""+MessageConstants.MSG_ERROR_52);
            }
        }
        
	    Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("BillNoIF", map.get("tradeNoIF"));
        mainData.put("BusinessType", CherryConstants.OS_BILLTYPE_MV);
        mainData.put("RelevanceNo", map.get("relevanceNo"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("Comments", map.get("reason"));
        mainData.put("OperateDate", map.get("tradeDate"));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
	    
	    //插入促销品移库主从表
	    int promotionShiftID = binOLSSCM08_BL.insertPrmShiftAll(mainData, promotionShiftDetailList);
	    
	    //插入入出库主从表，库存表
	    Map<String,Object> praMap = new HashMap<String,Object>();
	    praMap.put("BIN_PromotionShiftID", promotionShiftID);
	    praMap.put("CreatedBy", map.get("createdBy"));
	    praMap.put("CreatePGM", map.get("createPGM"));
	    praMap.put("UpdatedBy", map.get("updatedBy"));
	    praMap.put("UpdatePGM", map.get("updatePGM"));
	    binOLSSCM08_BL.changeStock(praMap);
	};
	
    /**
     * 对积分兑换（无需预约）数据进行库存处理
     * @param map
     * @throws Exception
     */
    @Override
    public void analyzePXStockData(Map<String, Object> map) throws Exception {
        // 取得组织ID
        String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
        // 取得品牌ID
        String brandInfoID = String.valueOf(map.get("brandInfoID"));
        // 新后台业务类型
        map.put("cherry_tradeType",MessageConstants.MSG_TRADETYPE_PX);
        
        // 设定入出库区分
        if (map.get("ticketType")!=null){
            if (MessageConstants.PX_TYPE_0.equals(((String)map.get("ticketType")))){
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
            }else{
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
            }
        }
        
        // 插入促销品库存操作流水表
        this.addPromotionInventoryLog(map);
        // 明细数据List
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        
        // 设定入出库关联单据号(销售单)
        map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
        // 插入促销品入出库表,入出库明细表
        this.addPromotionStockInfoForSale(cloneDetailList, map, organizationInfoID, brandInfoID);
        // 操作库存数据
        this.operateStockDataForSale(cloneDetailList, map);
    }
	
    @Override
    public void analyzeReturnRequestData(Map<String, Object> map) throws Exception {}
    
    @Override
    public void analyzeStocktakeRequestData(Map<String, Object> map) throws Exception {}
	
    @Override
    public void analyzeStocktakeConfirmData(Map<String, Object> map) throws Exception {}
    
    @Override
    public void analyzeSaleReturnData(Map<String, Object> map) {}

    @Override
    public void analyzePXData(Map<String, Object> map) throws Exception {}
    
    @Override
    public void analyzeChangeSaleState(Map map) throws Exception{}
    
	@Override
	public void selMessageInfo(Map map) throws CherryMQException{}
	
	@Override
	public void setDetailDataInfo(List detailDataList, Map map) throws CherryMQException{}
	
	@Override
	public void setInsertInfoMapKey (Map map){
	    map.put("createdBy", "BINBEMQMES01");
	    map.put("createPGM", "BINBEMQMES01");
	    map.put("updatedBy", "BINBEMQMES01");
	    map.put("updatePGM", "BINBEMQMES01");
	}
		
	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException{}


}
