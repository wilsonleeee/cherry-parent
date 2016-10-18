/*	
 * @(#)BINOLSSCM01_BL.java     1.0 2010/10/29		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.service.BINOLSSCM01_Service;

/**
 * 促销品库存操作的共通机能
 * 操作流水表
 * 操作入出库表
 * 操作库存表
 * 促销品收发货单表
 * @author dingyc
 *
 */
public class BINOLSSCM01_BL {
	@Resource
	private BINOLSSCM01_Service binolsscm01_service;
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	//===========================================促销品业务操作流水表=========================================
	/**
	 * 插入数据到  【促销品库存操作流水表】，并返回一个流水号
	 * @param map
	 * @return
	 */
	public int insertPromotionInventoryLog(Map<String, Object> map){
		return binolsscm01_service.insertPromotionInventoryLog(map);
	}	
	
	//===========================================促销品入出库表，库存表=========================================	
	/**
	 * 根据收发货单的ID，将对应的数据写入入出库表
	 * @param promotionDeliverIDArr
	 * @param userInfo
	 */
	public int insertStockInOutByDeliverID(int promotionDeliverID,String currentUnit,int userID){
		//整理传入的参数
		String currentOrganizationInfoID = "";
		String currentBrandInfoID = "";
//		String currentUnit = userInfo.getCurrentUnit();
//		String loginName = userInfo.getLoginName();
	
		//取得指定ID的收发货单据信息（包括详细）
		Map<String,Object> deliverMap = new HashMap<String,Object>();
		deliverMap.put("BIN_PromotionDeliverID", promotionDeliverID);			
		List<Map<String,Object>> list = binolsscm01_service.getPromotionDeliverAllInfo(deliverMap);
		
		String stockType="";
		int promotionStockInOutID = 0;
		
        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细  
        for(int i=0;i<list.size();i++){
            Map<String,Object> deliverDetailMap = list.get(i);
            if(CherryUtil.obj2int(deliverDetailMap.get("Quantity")) == 0){
                list.remove(i);
                i--;
                continue;
            }
        }
        if(list.size()==0){
            return 0;
        }
		
		for(int n=0;n<list.size();n++){
			Map<String,Object> deliverDetailMap = list.get(n);
			if(n==0){
				currentOrganizationInfoID = String.valueOf(deliverDetailMap.get("BIN_OrganizationInfoID"));
				currentBrandInfoID = String.valueOf(deliverDetailMap.get("BIN_BrandInfoID"));
				//业务类型 1:仓库发货，2:仓库收货
				String tradeType = String.valueOf(deliverDetailMap.get("mTradeType"));
				String flag ="";
				if("1".equals(tradeType)){
					flag= CherryConstants.BUSINESS_TYPE_STORAGE_OUT;
				}else if("2".equals(tradeType)){
					flag= CherryConstants.BUSINESS_TYPE_STORAGE_IN;
				}
					
				//插入入出库表前，取得单据号
				String stockInOutNo = binOLCM03_BL.getTicketNumber(currentOrganizationInfoID,currentBrandInfoID,String.valueOf(userID), flag);
				
				//拼凑【促销产品入出库主表】数据
				Map<String,Object> stockInOutMainMap = new HashMap<String,Object>();

				//入出库区分 0：入库	1：出库
				stockType =CherryConstants.BUSINESS_TYPE_DELIVER_SEND.equals(tradeType)?CherryConstants.STOCK_TYPE_OUT:CherryConstants.STOCK_TYPE_IN;
				//主表       组织ID
				stockInOutMainMap.put("BIN_OrganizationInfoID", currentOrganizationInfoID);	
				//主表       品牌ID
				stockInOutMainMap.put("BIN_BrandInfoID", currentBrandInfoID);
				//主表      单据号			
				stockInOutMainMap.put("TradeNo", stockInOutNo);
				//主表      接口单据号
				stockInOutMainMap.put("TradeNoIF", stockInOutNo);
				//主表      入出库部门
				if(CherryConstants.STOCK_TYPE_OUT.equals(stockType)){
					//如果是出库
					stockInOutMainMap.put("BIN_OrganizationID", deliverDetailMap.get("mBIN_OrganizationID"));
				}else{
					//如果是入库
					stockInOutMainMap.put("BIN_OrganizationID", deliverDetailMap.get("mBIN_OrganizationIDReceive"));
				}
				//主表      制单员工
				stockInOutMainMap.put("BIN_EmployeeID", deliverDetailMap.get("mBIN_EmployeeID"));
				//主表      总数量
				stockInOutMainMap.put("TotalQuantity", deliverDetailMap.get("mTotalQuantity"));
				//主表      总金额
				stockInOutMainMap.put("TotalAmount", deliverDetailMap.get("mTotalAmount"));
				//主表      业务类型 1:仓库发货，2:仓库收货					
				stockInOutMainMap.put("TradeType", deliverDetailMap.get("mTradeType"));
				//主表      入出库区分 0：入库	1：出库
				stockInOutMainMap.put("StockType", stockType);
				//主表      关联单号
				stockInOutMainMap.put("RelevantNo", deliverDetailMap.get("mDeliverReceiveNo"));
				//主表     物流ID
				stockInOutMainMap.put("BIN_LogisticInfoID", deliverDetailMap.get("mBIN_LogisticInfoID"));
				//主表     入出库理由
				stockInOutMainMap.put("Reason", deliverDetailMap.get("mReason"));
				//主表     入出库日期
				stockInOutMainMap.put("StockInOutDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));				
				//主表     审核区分  已审核
				stockInOutMainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
				//入出库前促销品总金额
				stockInOutMainMap.put("TotalAmountBefore", null);
				//入出库后促销品总金额
				stockInOutMainMap.put("TotalAmountAfter", null);
				//主表    有效区分
				stockInOutMainMap.put("ValidFlag", "1");
				//主表    共通字段
				stockInOutMainMap.put("CreatedBy", userID);	
				stockInOutMainMap.put("CreatePGM", currentUnit);	
				stockInOutMainMap.put("UpdatedBy", userID);	
				stockInOutMainMap.put("UpdatePGM", currentUnit);
				//insert 【促销产品入出库表】并返回自增长ID
				promotionStockInOutID = binolsscm01_service.insertPromotionStockInOutMain(stockInOutMainMap);
			}			
			//【促销产品入出库明细表】和【促销产品收发货业务单据明细表】结构大致相同，仅需要整理下不同的几个字段即可
			//明细表    促销产品入出库记录ID
			deliverDetailMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
			//明细表    明细连番
			deliverDetailMap.put("DetailNo", n+1);
			//明细表    入出库区分
			deliverDetailMap.put("StockType", stockType);
			//明细表    有效区分
			deliverDetailMap.put("ValidFlag", "1");
			//明细表    共通字段
			deliverDetailMap.put("CreatedBy", userID);	
			deliverDetailMap.put("CreatePGM", currentUnit);	
			deliverDetailMap.put("UpdatedBy", userID);	
			deliverDetailMap.put("UpdatePGM", currentUnit);
			
			//促销产品厂商ID/数量 /价格/包装类型ID/实体仓库ID/逻辑仓库ID/仓库库位ID/入出库理由
			//以上字段已经存在于deliverDetailMap中，原样使用即可
			//insert 【促销产品入出库明细表】		
			binolsscm01_service.insertPromotionStockDetail(deliverDetailMap);
		}
		return promotionStockInOutID;
	} 

	/**传入一组收发货单据的ID，写入入出库表，返回主ID数组
	 * @param promotionDeliverIDArr
	 * @param userInfo
	 * @return
	 */
	public int[] insertStockInOutByDeliverIDArr(int[] promotionDeliverIDArr,UserInfo userInfo){	
		int[] ret = new int[promotionDeliverIDArr.length];		
		for(int i=0;i<promotionDeliverIDArr.length;i++){
			ret[i] = this.insertStockInOutByDeliverID(promotionDeliverIDArr[i], userInfo.getCurrentUnit(),userInfo.getBIN_UserID());
		}
		return ret;
	} 
	
	/**
	 * 根据调出确认单的ID，将对应的数据写入入出库表（出库方）
	 * @param promotionDeliverIDArr
	 * @param userInfo
	 */
	public int insertStockInOutByAllocationIDOut(int promotionDeliverID,String currentUnit,int userID){
		//整理传入的参数
		String currentOrganizationInfoID = "";
		String currentBrandInfoID = "";	
	
		//取得指定ID的调拨单据信息（包括详细）
		Map<String,Object> allocationMap = new HashMap<String,Object>();
		allocationMap.put("BIN_PromotionAllocationID", promotionDeliverID);			
		List<Map<String,Object>> list = binolsscm01_service.getPromotionAllocationAllInfo(allocationMap);
		int promotionStockInOutID = 0;
		
        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细  
        for(int i=0;i<list.size();i++){
            Map<String,Object> deliverDetailMap = list.get(i);
            if(CherryUtil.obj2int(deliverDetailMap.get("Quantity")) == 0){
                list.remove(i);
                i--;
                continue;
            }
        }
        if(list.size()==0){
            return 0;
        }
		
		for(int n=0;n<list.size();n++){
			Map<String,Object> deliverDetailMap = list.get(n);
			if(n==0){
				
				currentOrganizationInfoID = String.valueOf( deliverDetailMap.get("BIN_OrganizationInfoID"));
				currentBrandInfoID = String.valueOf(deliverDetailMap.get("BIN_BrandInfoID"));
				//插入入出库表前，取得单据号
				String stockInOutNo = binOLCM03_BL.getTicketNumber(currentOrganizationInfoID,currentBrandInfoID,String.valueOf(userID), CherryConstants.BUSINESS_TYPE_STORAGE_OUT);
				
				//拼凑【促销产品入出库主表】数据
				Map<String,Object> stockInOutMainMap = new HashMap<String,Object>();
				//主表       组织ID
				stockInOutMainMap.put("BIN_OrganizationInfoID", currentOrganizationInfoID);	
				//主表       品牌ID
				stockInOutMainMap.put("BIN_BrandInfoID", currentBrandInfoID);
				//主表      单据号			
				stockInOutMainMap.put("TradeNo", stockInOutNo);
				//主表      接口单据号
				stockInOutMainMap.put("TradeNoIF", stockInOutNo);
				//主表      出库部门
				stockInOutMainMap.put("BIN_OrganizationID", deliverDetailMap.get("mBIN_OrganizationIDAccept"));
			
				//主表      制单员工
				stockInOutMainMap.put("BIN_EmployeeID", deliverDetailMap.get("mBIN_EmployeeID"));
				//主表      总数量
				stockInOutMainMap.put("TotalQuantity", deliverDetailMap.get("mTotalQuantity"));
				//主表      总金额
				stockInOutMainMap.put("TotalAmount", deliverDetailMap.get("mTotalAmount"));
				//主表      业务类型 				
				stockInOutMainMap.put("TradeType", deliverDetailMap.get("mTradeType"));
				//主表      入出库区分 0：入库	1：出库
				stockInOutMainMap.put("StockType", CherryConstants.STOCK_TYPE_OUT);
				//主表      关联单号
				stockInOutMainMap.put("RelevantNo", deliverDetailMap.get("mAllocationNo"));
				//主表     物流ID
				stockInOutMainMap.put("BIN_LogisticInfoID", deliverDetailMap.get("mBIN_LogisticInfoID"));
				//主表     入出库理由
				stockInOutMainMap.put("Reason", deliverDetailMap.get("mReason"));
				//主表     入出库日期
				stockInOutMainMap.put("StockInOutDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));				
				//主表     审核区分  已审核
				stockInOutMainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
				//入出库前促销品总金额
				stockInOutMainMap.put("TotalAmountBefore", null);
				//入出库后促销品总金额
				stockInOutMainMap.put("TotalAmountAfter", null);
				//主表    有效区分
				stockInOutMainMap.put("ValidFlag", "1");
				//主表    共通字段
				stockInOutMainMap.put("CreatedBy", userID);	
				stockInOutMainMap.put("CreatePGM", currentUnit);	
				stockInOutMainMap.put("UpdatedBy", userID);	
				stockInOutMainMap.put("UpdatePGM", currentUnit);
				//insert 【促销产品入出库表】并返回自增长ID
				promotionStockInOutID = binolsscm01_service.insertPromotionStockInOutMain(stockInOutMainMap);
			}			
			//【促销产品入出库明细表】和【促销产品收发货业务单据明细表】结构大致相同，仅需要整理下不同的几个字段即可
			//明细表    促销产品入出库记录ID
			deliverDetailMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
			//明细表    明细连番
			deliverDetailMap.put("DetailNo", n+1);
			//明细表    入出库区分
			deliverDetailMap.put("StockType", CherryConstants.STOCK_TYPE_OUT);
			//明细表    有效区分
			deliverDetailMap.put("ValidFlag", "1");
			//明细表    共通字段
			deliverDetailMap.put("CreatedBy", userID);	
			deliverDetailMap.put("CreatePGM", currentUnit);	
			deliverDetailMap.put("UpdatedBy", userID);	
			deliverDetailMap.put("UpdatePGM", currentUnit);
			
			//促销产品厂商ID/数量 /价格/包装类型ID/实体仓库ID/逻辑仓库ID/仓库库位ID/入出库理由
			//以上字段已经存在于deliverDetailMap中，原样使用即可
			//insert 【促销产品入出库明细表】		
			binolsscm01_service.insertPromotionStockDetail(deliverDetailMap);
		}
		return promotionStockInOutID;
	} 
	
	/**
	 * 根据调出确认单的ID，将对应的数据写入入出库表（入库方）
	 * @param promotionDeliverIDArr
	 * @param userInfo
	 */
	public int insertStockInOutByAllocationIDIn(int promotionDeliverID,String currentUnit,int userID){
		//整理传入的参数
		String currentOrganizationInfoID = "";
		String currentBrandInfoID = "";
	
		//取得指定ID的调拨单据信息（包括详细）
		Map<String,Object> allocationMap = new HashMap<String,Object>();
		allocationMap.put("BIN_PromotionAllocationID", promotionDeliverID);			
		List<Map<String,Object>> list = binolsscm01_service.getPromotionAllocationAllInfo(allocationMap);
		int promotionStockInOutID = 0;
		
        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细  
        for(int i=0;i<list.size();i++){
            Map<String,Object> deliverDetailMap = list.get(i);
            if(CherryUtil.obj2int(deliverDetailMap.get("Quantity")) == 0){
                list.remove(i);
                i--;
                continue;
            }
        }
        if(list.size()==0){
            return 0;
        }
		
		for(int n=0;n<list.size();n++){
			Map<String,Object> deliverDetailMap = list.get(n);
			if(n==0){
				
				currentOrganizationInfoID = String.valueOf( deliverDetailMap.get("BIN_OrganizationInfoID"));
				currentBrandInfoID = String.valueOf(deliverDetailMap.get("BIN_BrandInfoID"));
				//插入入出库表前，取得单据号
				String stockInOutNo = binOLCM03_BL.getTicketNumber(currentOrganizationInfoID,currentBrandInfoID,String.valueOf(userID), CherryConstants.BUSINESS_TYPE_STORAGE_IN);
				
				//拼凑【促销产品入出库主表】数据
				Map<String,Object> stockInOutMainMap = new HashMap<String,Object>();
				//主表       组织ID
				stockInOutMainMap.put("BIN_OrganizationInfoID", currentOrganizationInfoID);	
				//主表       品牌ID
				stockInOutMainMap.put("BIN_BrandInfoID", currentBrandInfoID);
				//主表      单据号			
				stockInOutMainMap.put("TradeNo", stockInOutNo);
				//主表      接口单据号
				stockInOutMainMap.put("TradeNoIF", stockInOutNo);
				//主表      入库部门
				stockInOutMainMap.put("BIN_OrganizationID", deliverDetailMap.get("mBIN_OrganizationID"));
			
				//主表      制单员工
				stockInOutMainMap.put("BIN_EmployeeID", deliverDetailMap.get("mBIN_EmployeeID"));
				//主表      总数量
				stockInOutMainMap.put("TotalQuantity", deliverDetailMap.get("mTotalQuantity"));
				//主表      总金额
				stockInOutMainMap.put("TotalAmount", deliverDetailMap.get("mTotalAmount"));
				//主表      业务类型 				
				stockInOutMainMap.put("TradeType", deliverDetailMap.get("mTradeType"));
				//主表      入出库区分 0：入库	1：出库
				stockInOutMainMap.put("StockType", CherryConstants.STOCK_TYPE_IN);
				//主表      关联单号
				stockInOutMainMap.put("RelevantNo", deliverDetailMap.get("mAllocationNo"));
				//主表     物流ID
				stockInOutMainMap.put("BIN_LogisticInfoID", deliverDetailMap.get("mBIN_LogisticInfoID"));
				//主表     入出库理由
				stockInOutMainMap.put("Reason", deliverDetailMap.get("mReason"));
				//主表     入出库日期
				stockInOutMainMap.put("StockInOutDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));				
				//主表     审核区分  已审核
				stockInOutMainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
				//入出库前促销品总金额
				stockInOutMainMap.put("TotalAmountBefore", null);
				//入出库后促销品总金额
				stockInOutMainMap.put("TotalAmountAfter", null);
				//主表    有效区分
				stockInOutMainMap.put("ValidFlag", "1");
				//主表    共通字段
				stockInOutMainMap.put("CreatedBy", userID);	
				stockInOutMainMap.put("CreatePGM", currentUnit);	
				stockInOutMainMap.put("UpdatedBy", userID);	
				stockInOutMainMap.put("UpdatePGM", currentUnit);
				//insert 【促销产品入出库表】并返回自增长ID
				promotionStockInOutID = binolsscm01_service.insertPromotionStockInOutMain(stockInOutMainMap);
			}			
			//【促销产品入出库明细表】和【促销产品收发货业务单据明细表】结构大致相同，仅需要整理下不同的几个字段即可
			//明细表    促销产品入出库记录ID
			deliverDetailMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
			//明细表    明细连番
			deliverDetailMap.put("DetailNo", n+1);
			//明细表    入出库区分
			deliverDetailMap.put("StockType", CherryConstants.STOCK_TYPE_IN);
			//明细表    有效区分
			deliverDetailMap.put("ValidFlag", "1");
			//明细表    共通字段
			deliverDetailMap.put("CreatedBy", userID);	
			deliverDetailMap.put("CreatePGM", currentUnit);	
			deliverDetailMap.put("UpdatedBy", userID);	
			deliverDetailMap.put("UpdatePGM", currentUnit);
			
			//促销产品厂商ID/数量 /价格/包装类型ID/实体仓库ID/逻辑仓库ID/仓库库位ID/入出库理由
			//以上字段已经存在于deliverDetailMap中，原样使用即可
			//insert 【促销产品入出库明细表】		
			binolsscm01_service.insertPromotionStockDetail(deliverDetailMap);
		}
		return promotionStockInOutID;
	} 
	
	/**
	 * 根据入出库ID更新库存表
	 * @param promotionStockInOutID
	 * @param userInfo
	 */
	public void updatePromotionStockByInOutID(int promotionStockInOutID,String currentUnit,int userID){
		//取得指定ID的入出库信息（包括详细）
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);			
		List<Map<String,Object>> list = binolsscm01_service.getPromotionStockInOutAllInfo(pramMap);
		
		for(int i=0;i<list.size();i++){
			Map<String,Object> stockInOutDetailMap = list.get(i);
			String stockType = String.valueOf(stockInOutDetailMap.get("StockType"));
			
			//出入库数量现在改成带正负号的增量来更新库存表
			if(CherryConstants.STOCK_TYPE_OUT.equals(stockType)){
				stockInOutDetailMap.put("Quantity", 0-Integer.parseInt(String.valueOf(stockInOutDetailMap.get("Quantity"))));
			}
			stockInOutDetailMap.put("CreatedBy", userID);	
			stockInOutDetailMap.put("CreatePGM", currentUnit);	
			stockInOutDetailMap.put("UpdatedBy", userID);	
			stockInOutDetailMap.put("UpdatePGM", currentUnit);
			
			//以增量的方式修改库存表
			int count = binolsscm01_service.updatePromotionStockByIncrement(stockInOutDetailMap);
			if(count==0){
				//如果原库存表中没有相关数据，则插入
				binolsscm01_service.insertPromotionStock(stockInOutDetailMap);
			}
		}
	}
	//===========================================其他=========================================	
	/**
	 * 取得指定仓库中指定促销品的库存数量
	 * @param map
	 * @return
	 */
	public List getStockCount(Map<String,Object> map){
		return binolsscm01_service.getStockCount(map);
	}
	
	/**
	 * 根据部门ID取得部门名称
	 * 
	 * */
	public String getDepartName(Map<String, Object> map){
		return binolsscm01_service.getDepartName(map);
	}
	
	/**
	 * 取得指定促销品在指定部门下的库存
	 * @param departID
	 * @param proID
	 * @param flag 1：不扣除冻结库存   2：扣除冻结库存    （发货单表）
	 * @return
	 */
	public int getPrmStock(Map<String, Object> pramMap) {
		// FrozenFlag 1：不扣除冻结库存 2：扣除冻结库存 （发货单表）
		String flag = String.valueOf(pramMap.get("FrozenFlag"));
		Map<String, Object> tmp =null; 
		if ("1".equals(flag)) {
			tmp = binolsscm01_service.getPrmStock(pramMap);
		}else if("2".equals(flag)){
			tmp = binolsscm01_service.getPrmStockNofrozen(pramMap);
		}else{
			tmp = new HashMap<String, Object>();
		}
		int ret = CherryUtil.obj2int(tmp.get("TotalQuantity"));
		return ret;
	}
}
