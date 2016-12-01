/*	
 * @(#)BINBAT152_BL.java     1.0 @2016-07-09		
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

package com.cherry.ss.pro.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.pro.service.BINBAT152_Service;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;

/**
 * 补录产品入出库成本(标准接口)BL
 * 
 * @author chenkuan
 * 
 * @version 2016-07-09
 * 
 */
public class BINBAT152_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT152_BL.class);

	@Resource(name = "binbat152_Service")
	private BINBAT152_Service binbat152_Service;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;


	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	

	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	private String fReason = ""; 
	/**
	 * 补录产品入出库成本
	 * 	第一步：只处理 接收退库：AR 和 调入申请:BG 的入库业务
	 * 	第二步：只处理关联退货（整单退和部分退，不包含空退）
	 * 	第三步：先处理明细中入库的数据且成本价为空 （只处理成本价为空且 除 接收退库：AR 和 调入申请:BG  以及关联退货     以外的入库数据，其他数据不处理） 
	 *  第四步：先处理明细中出库的数据且成本价为空 （只处理成本价为空的出库数据，其他数据直接插入不做修改） 
	 *  
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_binbat152(Map<String, Object> map) throws Exception {
    	try{
    		//参数初始化
	        init(map);
    	}catch(Exception e){
    		e.printStackTrace();
    		// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			fReason = String.format("程序参数初始化时失败,详细信息查看Log日志", e.getMessage());
			throw new CherryBatchException(batchExceptionDTO);
    	}
    	
    	
    	// 取得系统配置项是否记录产品入出库成本
    	String config = binOLCM14_BL.getConfigValue("1365", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
    	
		if(CherryBatchConstants.SYS_CONFIG_COSTPRICE_1.equals(config)){

            Map<String, Object> mainData = new HashMap<String, Object>();
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();

    		map.put("StockType", "0");//表示入库
    		
    		////////////////////////////////// 第一步：只处理 接收退库：AR 和 调入申请:BG 的入库业务  ///////////////////////////////////////////////////
    		
    		//step：1    查询产品入出库批次表的数据（即明细中有成本价为空，且是入库类型的数据）
	    	List<Map<String, Object>> proBatchInOutList3=binbat152_Service.getProBatchInOutList(map);
	    	if (!CherryBatchUtil.isBlankList(proBatchInOutList3)) {
	    		totalCount+=proBatchInOutList3.size();//处理总条数，主数据条数
	    		for(Map<String, Object> proBatchInOut:proBatchInOutList3){ 	    			
	    				String tradeType = ConvertUtil.getString(proBatchInOut.get("TradeType")); // 业务类型    		   		
    		
			    		// 调入(BG) -- 调入单成本价使用关联调出(LG)单的成本价
			        	if (CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)){
			        		
			        		List<Map<String,Object>> lgProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByBgRelevanceNo(proBatchInOut);; // 定义关联调出单List
			        		if(null != lgProductBatchInOutDetail && lgProductBatchInOutDetail.size() != 0){
			        			try {	
			        				
			        				//得到原单的明细
			        				List<Map<String,Object>> lgProductBatchDetail=binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
			        				
			        				binbat152_Service.deleteProBatchInOutDetailByMainId(proBatchInOut);
			        				
			        				for(Map<String, Object> lgProductBatchMap : lgProductBatchInOutDetail){
			        					
			        					lgProductBatchMap.put("CreatedBy", "BINBAT152");
			            				lgProductBatchMap.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
			            				lgProductBatchMap.put("UpdatedBy", "BINBAT152");
			            				lgProductBatchMap.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);
			        					
			        					lgProductBatchMap.put("isNewFlag", "1");//表示新数据
			        					
			        					lgProductBatchMap.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
			        					lgProductBatchMap.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
			        					
			        					lgProductBatchMap.put("BIN_InventoryInfoID", lgProductBatchDetail.get(0).get("BIN_InventoryInfoID"));
			                    		lgProductBatchMap.put("BIN_LogicInventoryInfoID", lgProductBatchDetail.get(0).get("BIN_LogicInventoryInfoID"));
			                    		lgProductBatchMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
			                    		
			        					// 插入【产品批次库存表】
			        					lgProductBatchMap.put("InQuantity", lgProductBatchMap.get("Quantity")); // 入库数量
			        					lgProductBatchMap.put("StockQuantity", lgProductBatchMap.get("Quantity")); // 当前批次剩余库存数量
			        					lgProductBatchMap.put("TradeNoIF", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号
			        					String costPrice = ConvertUtil.getString(lgProductBatchMap.get("CostPrice"));
			        					
			        					int productBatchStockID = 0; // 产品批次库存ID
			        					if(!CherryBatchUtil.isBlankString(costPrice)){
			        						productBatchStockID = binbat152_Service.insertProductNewBatchStock(lgProductBatchMap);
			        					}
			        					
			        					lgProductBatchMap.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
			        					lgProductBatchMap.put("StockType", proBatchInOut.get("StockType"));
			        					lgProductBatchMap.put("StockInOutTime", proBatchInOut.get("StockInOutTime"));
			        					lgProductBatchMap.put("RelevanceNo", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号
			        					
			        					binbat152_Service.insertProBatchInOutDetail(lgProductBatchMap);
			        				}
			        				
			        				int sum=binbat152_Service.getCostPriceIsNullAmount(proBatchInOut);
									if(sum==0){//表示该入出库批次下的明细都有成本价
										Map<String, Object> TotalCostPriceMap=binbat152_Service.getTotalCostPrice(proBatchInOut);//得到某一单的总成本价
										TotalCostPriceMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
										try {
											binbat152_Service.updateTotalCostPrice(TotalCostPriceMap);//修改某一单的总成本价									
										} catch (Exception e) {
											//修改产品入出库批次表的总成本价失败，产品入出库批次记录ID：（{0}）。
											BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
											batchExceptionDTO.setBatchName(this.getClass());
											batchExceptionDTO.setErrorCode("EOT00180");
											batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
											batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
											batchExceptionDTO.setException(e);
											throw new CherryBatchException(batchExceptionDTO);
										}
									}
			        				// 新后台数据源提交
									binbat152_Service.manualCommit();
			        				
								} catch (Exception e) {
									// 新后台数据源回滚
									binbat152_Service.manualRollback();
									failCount++;
									flag = CherryBatchConstants.BATCH_WARNING;
				
									BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
									// 补录产品入出库成本失败，产品入出库批次记录ID：（{0}），关联单号：（{1}）。
									batchLoggerDTO1.setCode("EOT00169");
									batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
									batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("RelevanceNo")));
									logger.BatchLogger(batchLoggerDTO1);
									logger.outExceptionLog(e);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
									fReason = String.format("补录产品入库成本失败,详细信息查看Log日志", e.getMessage());
								}
			        		}
			        	}
			        	// 接收退库(AR) -- 接收退库使用关联退库(RR)
			        	else if (CherryConstants.BUSINESS_TYPE_AR.equals(tradeType)){
			        		List<Map<String,Object>> rrProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByARRelevanceNo(proBatchInOut); // 定义关联退库单List
			        		if(null != rrProductBatchInOutDetail && rrProductBatchInOutDetail.size() != 0){			        			
			        			try {
			        							        				
			        				//得到原单的明细
			        				List<Map<String,Object>> lgProductBatchDetail=binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
			        				
			        				binbat152_Service.deleteProBatchInOutDetailByMainId(proBatchInOut);
									
			        				for(Map<String, Object> rrProductBatchMap : rrProductBatchInOutDetail){
			        					
			        					rrProductBatchMap.put("CreatedBy", "BINBAT152");
			        					rrProductBatchMap.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
			            				rrProductBatchMap.put("UpdatedBy", "BINBAT152");
			            				rrProductBatchMap.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);
			        					
			        					rrProductBatchMap.put("isNewFlag", "1");//表示新数据
			        					
			        					rrProductBatchMap.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
			        					rrProductBatchMap.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
			        					
			        					rrProductBatchMap.put("BIN_InventoryInfoID", lgProductBatchDetail.get(0).get("BIN_InventoryInfoID"));
			        					rrProductBatchMap.put("BIN_LogicInventoryInfoID", lgProductBatchDetail.get(0).get("BIN_LogicInventoryInfoID"));
			        					rrProductBatchMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
			                    		
			        					// 插入【产品批次库存表】
			        					rrProductBatchMap.put("InQuantity", rrProductBatchMap.get("Quantity")); // 入库数量
			        					rrProductBatchMap.put("StockQuantity", rrProductBatchMap.get("Quantity")); // 当前批次剩余库存数量
			        					rrProductBatchMap.put("TradeNoIF", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号
			        					String costPrice = ConvertUtil.getString(rrProductBatchMap.get("CostPrice"));
			        					
			        					int productBatchStockID = 0; // 产品批次库存ID
			        					if(!CherryBatchUtil.isBlankString(costPrice)){
			        						productBatchStockID = binbat152_Service.insertProductNewBatchStock(rrProductBatchMap);
			        					}
			        					
			        					rrProductBatchMap.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
			        					rrProductBatchMap.put("StockType", proBatchInOut.get("StockType"));
			        					rrProductBatchMap.put("StockInOutTime", proBatchInOut.get("StockInOutTime"));
			        					rrProductBatchMap.put("RelevanceNo", proBatchInOut.get("RelevanceNo")); // 入库的原始单据号
			        					
			        					binbat152_Service.insertProBatchInOutDetail(rrProductBatchMap);
			        					
			        				}
			        				
			        				int sum=binbat152_Service.getCostPriceIsNullAmount(proBatchInOut);
									if(sum==0){//表示该入出库批次下的明细都有成本价
										Map<String, Object> TotalCostPriceMap=binbat152_Service.getTotalCostPrice(proBatchInOut);//得到某一单的总成本价
										TotalCostPriceMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
										try {
											binbat152_Service.updateTotalCostPrice(TotalCostPriceMap);//修改某一单的总成本价									
										} catch (Exception e) {
											//修改产品入出库批次表的总成本价失败，产品入出库批次记录ID：（{0}）。
											BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
											batchExceptionDTO.setBatchName(this.getClass());
											batchExceptionDTO.setErrorCode("EOT00180");
											batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
											batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
											batchExceptionDTO.setException(e);
											throw new CherryBatchException(batchExceptionDTO);
										}
									}
			        				// 新后台数据源提交
									binbat152_Service.manualCommit();	
			        				
								} catch (Exception e) {
									// 新后台数据源回滚
									binbat152_Service.manualRollback();
									failCount++;
									flag = CherryBatchConstants.BATCH_WARNING;
				
									BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
									// 补录产品入出库成本失败，产品入出库批次记录ID：（{0}），关联单号：（{1}）。
									batchLoggerDTO1.setCode("EOT00169");
									batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
									batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("RelevanceNo")));
									logger.BatchLogger(batchLoggerDTO1);
									logger.outExceptionLog(e);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
									fReason = String.format("补录产品入库成本失败,详细信息查看Log日志", e.getMessage());
								}
			        		}
			        	}

                    // 处理各业务的单据号
//                    mainData.put("RelevanceNo",proBatchInOut.get("RelevanceNo"));
                    sumProductCost(proBatchInOut);
	    		}
	    	}
    		
    		////////////////////////////////// 第二步：只处理 关联退货（整单退和部分退，不包含空退） 入库业务  ///////////////////////////////////////////////////    		
    		
    		//step：1    查询产品入出库批次表的数据（即明细中有成本价为空，且是入库类型的数据）
	    	List<Map<String, Object>> proBatchInOutList1=binbat152_Service.getProBatchInOutList(map);
	    	if (!CherryBatchUtil.isBlankList(proBatchInOutList1)) {
	    		totalCount+=proBatchInOutList1.size();//处理总条数，主数据条数
	    		for(Map<String, Object> proBatchInOut:proBatchInOutList1){ 	    			
	    			try{
	    				String tradeType = ConvertUtil.getString(proBatchInOut.get("TradeType")); // 业务类型    		
	    				String stockInOut_SRrelevantNo = ConvertUtil.getString(proBatchInOut.get("BillCodePre"));//退货对应的关联单号，即销售单号
	    				
	    				//只处理关联退货 入库业务
	    				if(CherryConstants.BUSINESS_TYPE_SR.equals(tradeType) && !CherryBatchUtil.isBlankString(stockInOut_SRrelevantNo)){
	    					
							//step：2     根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
							List<Map<String, Object>> proBatchInOutDetailList=binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
							
							if(!CherryBatchUtil.isBlankList(proBatchInOutDetailList)){ 
								
								//根据产品入出库批次表ID删除 对应的产品入出库批次记录明细
								binbat152_Service.deleteProBatchInOutDetailByMainId(proBatchInOut);
								
								//每个产品入出库批次对应的明细序号
								int detailNo=0;
								for(Map<String, Object> proBatchInOutDetail:proBatchInOutDetailList){
																	
//									proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
//									proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
									
									String costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice")); // 明细成本价
									
									
									if(CherryBatchUtil.isBlankString(costPrice)){//成本价为空才会进行处理
											List<Map<String,Object>> saleProductBatchInOutDetail = null; // 定义关联退货单
							            	proBatchInOutDetail.put("BillCodePre", stockInOut_SRrelevantNo);
							            	saleProductBatchInOutDetail = binbat152_Service.getProductBatchInOutDetailByRelevanceNoAndPrt(proBatchInOutDetail);

							            	// 关联退货 关联退货可能存在出库时根据库存拆成多个单子的情况，此处也需要如此处理
							            	if(!CherryBatchUtil.isBlankList(saleProductBatchInOutDetail)){

							            		int quantity=ConvertUtil.getInt(proBatchInOutDetail.get("Quantity"));//明细数量
												int stockQuantity=0;//表示产品的库存数量

												for(Map<String, Object> proNewBatchStock: saleProductBatchInOutDetail){
													stockQuantity+=ConvertUtil.getInt(proNewBatchStock.get("RelSrResidualQuantity"));
												}
												if(stockQuantity<quantity){//表示库存不足，因此不处理
													proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
													proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
													binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
												}
												else{//剩余库存数满足
													for(Map<String, Object> proNewBatchStock: saleProductBatchInOutDetail){
														int amount=ConvertUtil.getInt(proNewBatchStock.get("RelSrResidualQuantity"));//库存数量

															if(quantity==0){
																break;
															}

																	proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
																	proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
																	proBatchInOutDetail.put("CreatedBy", "BINBAT152");
																	proBatchInOutDetail.put("CreatePGM", CherryBatchConstants.UPDATE_NAME);
																	proBatchInOutDetail.put("UpdatedBy", "BINBAT152");
																	proBatchInOutDetail.put("UpdatePGM", CherryBatchConstants.UPDATE_NAME);

															if(amount<quantity){//表示库存数小于明细
																	quantity=quantity-amount;
																	proBatchInOutDetail.put("isNewFlag", "1");//区分是否是新数据的
																	proBatchInOutDetail.put("Quantity",amount);
																	proBatchInOutDetail.put("InQuantity",amount);
																	proBatchInOutDetail.put("StockQuantity",amount);
																	proBatchInOutDetail.put("BIN_ProductBatchStockID",proNewBatchStock.get("BIN_ProductBatchStockID"));
																	proBatchInOutDetail.put("CostPrice",proNewBatchStock.get("CostPrice"));
																	proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
																	proBatchInOutDetail.put("TradeNoIF",proBatchInOutDetail.get("RelevanceNo"));
																	proNewBatchStock.put("Quantity", amount);

																	int productBatchStockID=0;//产品批次库存ID
																	productBatchStockID=binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入【产品批次库存表(新建)】并返回批次库存表ID

																	proBatchInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID

																	binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);//插入产品入出库批次记录明细表
																	binbat152_Service.updateRelSrResidualQuantity(proNewBatchStock);


															}else{



																	proBatchInOutDetail.put("isNewFlag", "1");//区分是否是新数据的
																	proBatchInOutDetail.put("Quantity",quantity);
																	proBatchInOutDetail.put("InQuantity",quantity);
																	proBatchInOutDetail.put("StockQuantity",quantity);
																	proBatchInOutDetail.put("BIN_ProductBatchStockID",proNewBatchStock.get("BIN_ProductBatchStockID"));
																	proBatchInOutDetail.put("CostPrice",proNewBatchStock.get("CostPrice"));
																	proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
																	proBatchInOutDetail.put("TradeNoIF",proBatchInOutDetail.get("RelevanceNo"));
																	proNewBatchStock.put("Quantity", quantity);

																	int productBatchStockID=0;//产品批次库存ID
																	productBatchStockID=binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入【产品批次库存表(新建)】并返回批次库存表ID

																	proBatchInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID

																	binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);//插入产品入出库批次记录明细表
																	binbat152_Service.updateRelSrResidualQuantity(proNewBatchStock);
																	quantity=0;
																	break;
															}
														}
												}

									        }else{
									        	proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
												proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
									        	binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
									        }


									}else{
										proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
										proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号	
										binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
									}
								}
								
								int sum=binbat152_Service.getCostPriceIsNullAmount(proBatchInOut);
								if(sum==0){//表示该入出库批次下的明细都有成本价
									Map<String, Object> TotalCostPriceMap=binbat152_Service.getTotalCostPrice(proBatchInOut);//得到某一单的总成本价
									TotalCostPriceMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
									try {
										binbat152_Service.updateTotalCostPrice(TotalCostPriceMap);//修改某一单的总成本价									
									} catch (Exception e) {
										//修改产品入出库批次表的总成本价失败，产品入出库批次记录ID：（{0}）。
										BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
										batchExceptionDTO.setBatchName(this.getClass());
										batchExceptionDTO.setErrorCode("EOT00180");
										batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
										batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
										batchExceptionDTO.setException(e);
										throw new CherryBatchException(batchExceptionDTO);
									}
								}
								
								// 新后台数据源提交
								binbat152_Service.manualCommit();
							}
		    			}
	    			}catch (Exception e) {
	    				// 新后台数据源回滚
						binbat152_Service.manualRollback();
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
	
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						// 补录产品入出库成本失败，产品入出库批次记录ID：（{0}），关联单号：（{1}）。
						batchLoggerDTO1.setCode("EOT00169");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("RelevanceNo")));
						logger.BatchLogger(batchLoggerDTO1);
						logger.outExceptionLog(e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						fReason = String.format("补录产品入库成本失败,详细信息查看Log日志", e.getMessage());
					}

                    // 处理各业务的单据号
//                    mainData.put("RelevanceNo",proBatchInOut.get("RelevanceNo"));
                    sumProductCost(proBatchInOut);
	    		}
	    	}
	    	
//////////////////////////////////第三步：只处理成本价为空且 除 接收退库：AR 和 调入申请:BG  以及关联退货     以外的入库数据  ///////////////////////////////////////////////////    		
	    	
	    	//step：1    查询产品入出库批次表的数据（即明细中有成本价为空，且是入库类型的数据）
	    	List<Map<String, Object>> proBatchInOutList2=binbat152_Service.getProBatchInOutList(map);
	    	if (!CherryBatchUtil.isBlankList(proBatchInOutList2)) {
	    		totalCount+=proBatchInOutList2.size();//处理总条数，主数据条数
	    		for(Map<String, Object> proBatchInOut:proBatchInOutList2){ 	    			
	    			try{
	    				String tradeType = ConvertUtil.getString(proBatchInOut.get("TradeType")); // 业务类型    		

	    				
	    				//只处理除 接收退库：AR 和 调入申请:BG 以外的其他入库业务
	    				if(!CherryConstants.BUSINESS_TYPE_AR.equals(tradeType) && !CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)){
	    					
							//step：2     根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
							List<Map<String, Object>> proBatchInOutDetailList=binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
														
							if(!CherryBatchUtil.isBlankList(proBatchInOutDetailList)){ 
								
								for(Map<String, Object> proBatchInOutDetail:proBatchInOutDetailList){
																	
									proBatchInOutDetail.put("BIN_OrganizationInfoID", proBatchInOut.get("BIN_OrganizationInfoID"));
									proBatchInOutDetail.put("BIN_BrandInfoID", proBatchInOut.get("BIN_BrandInfoID"));
																	
									String stockType = ConvertUtil.getString(proBatchInOutDetail.get("StockType")); // 明细出入库区分 ( 0：入库 1：出库 )
									String costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice")); // 明细成本价
									
									if(CherryBatchUtil.isBlankString(costPrice)){//成本价为空才会进行处理							
									
										// 入库处理(根据业务类型确定入库规则)
										if(CherryConstants.STOCK_TYPE_IN.equals(stockType)){//表示入库
							        		
							            	// 盘点(盘盈)(最后一笔成本价 )
							            	if(CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){
												// 盘点(盘盈)
												String costPriceCA = null;
							            		// 取得产品库存表指定仓库产品的首末次信息
							            		proBatchInOutDetail.put("stockInTimeSorting", "DESC"); // 排序方式
							            		Map<String,Object> topProductNewBatchStockMap = binbat152_Service.getProductNewBatchStock(proBatchInOutDetail);
							            		if(null != topProductNewBatchStockMap && topProductNewBatchStockMap.isEmpty()) {
													costPriceCA = ConvertUtil.getString(topProductNewBatchStockMap.get("CostPrice"));
												} else {
													/**
													 * 系统配置项[初始盘盈时的入库成本价使用的价格]:'':不处理; 'DistributionPrice':配送价;'StandardCost':结算价;
													 */
													String priceConfig = binOLCM14_BL.getConfigValue("1395", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
													// 根据产品厂商ID及入出库日期取得产品的价格想着信息
													Map<String,Object> productPrice = binbat152_Service.getProductPriceByID(proBatchInOutDetail);
													if(!"".equals(priceConfig) && null != productPrice && !productPrice.isEmpty()) {
														// 取指定价格
														costPriceCA = ConvertUtil.getString(productPrice.get(priceConfig));
														proBatchInOutDetail.put("Comments",priceConfig+","+costPriceCA);
													}
												}
												proBatchInOutDetail.put("CostPrice","".equals(costPriceCA) ? null : costPriceCA); // 成本价
							            	}
							            	// 退货
							            	else if (CherryConstants.BUSINESS_TYPE_SR.equals(tradeType)){
							            		String stockInOut_SRrelevantNo = ConvertUtil.getString(proBatchInOut.get("BillCodePre"));//退货对应的关联单号，即销售单号
							            		
							            		// 空退(空退的情况下以最近的批次作为成本价格。)
							            		if(CherryBatchUtil.isBlankString(stockInOut_SRrelevantNo)){
							            			 
							                		// 取得产品库存表指定仓库产品的首末次信息
							            			proBatchInOutDetail.put("stockInTimeSorting", "DESC"); // 排序方式
							                		Map<String,Object> topProductNewBatchStockMap = binbat152_Service.getProductNewBatchStock(proBatchInOutDetail);
							            			
							    					proBatchInOutDetail.put("CostPrice",
															(null != topProductNewBatchStockMap && !topProductNewBatchStockMap.isEmpty()) 
															? topProductNewBatchStockMap.get("CostPrice") : null); // 成本价
							            		} 
							            		// 关联退货(跳过这条数据不处理，前面已经处理过了)
							            		else{
							            			continue;
							            		}
							            	}
							            	// 其他入库业务（先进先出）
							            	else{
							            		proBatchInOutDetail.put("CostPrice", proBatchInOutDetail.get("Price")); // 成本价
							            	}
							            	
							            	init(proBatchInOutDetail);
							            																	            							            	
							            	// 插入【产品批次库存表】
							            	proBatchInOutDetail.put("InQuantity", proBatchInOutDetail.get("Quantity")); // 入库数量
							            	proBatchInOutDetail.put("StockQuantity", proBatchInOutDetail.get("Quantity")); // 当前批次剩余库存数量
							            	proBatchInOutDetail.put("TradeNoIF", proBatchInOutDetail.get("RelevanceNo")); // 入库的原始单据号
							            	costPrice = ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"));
							            		
							            	int productBatchStockID = 0; // 产品批次库存ID
							            	if(!CherryBatchUtil.isBlankString(costPrice)){
							            		productBatchStockID = binbat152_Service.insertProductNewBatchStock(proBatchInOutDetail);//插入产品批次库存表
							            		proBatchInOutDetail.put("BIN_ProductBatchStockID",productBatchStockID != 0 ? productBatchStockID : null);
							            		binbat152_Service.updateCostPriceByDetails(proBatchInOutDetail);//根据入出库明细id修改对应的成本价
							            			
							            	}
							            	
										}
										
										
									}
								}
								
								int sum=binbat152_Service.getCostPriceIsNullAmount(proBatchInOut);
								if(sum==0){//表示该入出库批次下的明细都有成本价
									Map<String, Object> TotalCostPriceMap=binbat152_Service.getTotalCostPrice(proBatchInOut);//得到某一单的总成本价
									TotalCostPriceMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
									try {
										binbat152_Service.updateTotalCostPrice(TotalCostPriceMap);//修改某一单的总成本价									
									} catch (Exception e) {
										//修改产品入出库批次表的总成本价失败，产品入出库批次记录ID：（{0}）。
										BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
										batchExceptionDTO.setBatchName(this.getClass());
										batchExceptionDTO.setErrorCode("EOT00180");
										batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
										batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
										batchExceptionDTO.setException(e);
										throw new CherryBatchException(batchExceptionDTO);
									}
								}
								
								// 新后台数据源提交
								binbat152_Service.manualCommit();
							}
		    			}
	    			}catch (Exception e) {
	    				// 新后台数据源回滚
						binbat152_Service.manualRollback();
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
	
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						// 补录产品入出库成本失败，产品入出库批次记录ID：（{0}），关联单号：（{1}）。
						batchLoggerDTO1.setCode("EOT00169");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("RelevanceNo")));
						logger.BatchLogger(batchLoggerDTO1);
						logger.outExceptionLog(e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						fReason = String.format("补录产品入库成本失败,详细信息查看Log日志", e.getMessage());
					}

//                    mainData.put("RelevanceNo",proBatchInOut.get("RelevanceNo"));
                    sumProductCost(proBatchInOut);
                }

	    	}
    		
    		////////////////////////////////// 第四步： 以下是出库逻辑  ///////////////////////////////////////////////////
	    	
    		map.put("StockType", "1");//表示出库
	    	//step：1    查询产品入出库批次表的数据（即明细中有成本价为空，且是出库类型的数据）
	    	List<Map<String, Object>> proBatchInOutList=binbat152_Service.getProBatchInOutList(map);
	    	
	    	if (!CherryBatchUtil.isBlankList(proBatchInOutList)) {
	    		totalCount+=proBatchInOutList.size();//处理总条数，主数据条数
	    		for(Map<String, Object> proBatchInOut:proBatchInOutList){ 
					try{
						//step：2     根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
						List<Map<String, Object>> proBatchInOutDetailList=binbat152_Service.getProBatchInOutDetailList(proBatchInOut);
						if(!CherryBatchUtil.isBlankList(proBatchInOutDetailList)){  
		
							//step：3       根据产品入出库批次表ID 删除产品入出库批次记录明细表
							binbat152_Service.deleteProBatchInOutDetailByMainId(proBatchInOut);
							
							//每个产品入出库批次对应的明细序号
							int detailNo=0;
							for(Map<String, Object> proBatchInOutDetail:proBatchInOutDetailList){							
								try {
									//step：3       根据明细ID删除产品入出库批次记录明细表
									//binbat152_Service.deleteProBatchInOutDetail(proBatchInOutDetail);
									
									//成本价不为空直接 插入产品入出库批次记录明细表，否则去产品批次库存表校验 (针对有成本价的入出库记录明细处理)
									if(proBatchInOutDetail.get("CostPrice")!=null && !(ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"))).equals("")){
										proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
										proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
										//插入产品入出库批次记录明细表
										try {							
											binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
										} catch (Exception e) {
											//插入产品入出库批次记录明细表失败。
											BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
											batchExceptionDTO.setBatchName(this.getClass());
											batchExceptionDTO.setErrorCode("EOT00171");
											batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
											batchExceptionDTO.setException(e);
											throw new CherryBatchException(batchExceptionDTO);
										}
									}
									// (针对没有成本价的出库记录明细处理)
									else if((proBatchInOutDetail.get("CostPrice")==null || (ConvertUtil.getString(proBatchInOutDetail.get("CostPrice"))).equals("")) && (ConvertUtil.getString(proBatchInOutDetail.get("StockType"))).equals("1")){
										proBatchInOutDetail.put("Sort", "asc");//排序方式升序排序，按照先进先出的原则进行
										//step：4     查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID查询）
										List<Map<String, Object>> proNewBatchStockList=binbat152_Service.getProNewBatchStockList(proBatchInOutDetail);
										if(!CherryBatchUtil.isBlankList(proNewBatchStockList)){
											
											int quantity=ConvertUtil.getInt(proBatchInOutDetail.get("Quantity"));//明细数量
											int stockQuantity=0;//表示产品的库存数量
											
											for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
												stockQuantity+=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));
											}
											if(stockQuantity<quantity){//表示库存不足，因此不处理
												proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
												proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
												//step：5     插入产品入出库批次记录明细表
												try {							
													binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
												} catch (Exception e) {
													//插入产品入出库批次记录明细表失败。
													BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
													batchExceptionDTO.setBatchName(this.getClass());
													batchExceptionDTO.setErrorCode("EOT00171");
													batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
													batchExceptionDTO.setException(e);
													throw new CherryBatchException(batchExceptionDTO);
												}
											}else{//产品批次库存逐条判断
												for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
													proNewBatchStock.put("oldStockQuantity", proNewBatchStock.get("StockQuantity"));//方便修改，防止别人修改过再次修改出现负库存
													int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
													if(amount>0){//表示库存数大于0
														if(amount<quantity){//表示库存数小于明细
															try {															
																int resultNumber=binbat152_Service.updateProNewBatchStock(proNewBatchStock); 
																quantity=quantity-amount;
																proBatchInOutDetail.put("isNewFlag", "1");//区分是否是新数据的
																proBatchInOutDetail.put("Quantity",amount);
																proBatchInOutDetail.put("RelSrResidualQuantity", amount);
																proBatchInOutDetail.put("BIN_ProductBatchStockID",proNewBatchStock.get("BIN_ProductBatchStockID"));
																proBatchInOutDetail.put("CostPrice",proNewBatchStock.get("CostPrice"));
																proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
																
																if(resultNumber==0){//表示该批次库存已被其他程序修改，抛出异常整个产品单子回滚
																	BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																	batchExceptionDTO.setBatchName(this.getClass());
																	batchExceptionDTO.setErrorCode("EOT00178");
																	batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																	batchExceptionDTO.addErrorParam(ConvertUtil.getString(proNewBatchStock.get("BIN_ProductBatchStockID")));
																	batchExceptionDTO.setException(new Exception("该批次库存已被其他程序修改，请重新操作！"));
																	throw new CherryBatchException(batchExceptionDTO);
																}
															} catch (Exception e) {
																//修改产品批次库存表库存数量失败。
																BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																batchExceptionDTO.setBatchName(this.getClass());
																batchExceptionDTO.setErrorCode("EOT00170");
																batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																batchExceptionDTO.setException(e);
																throw new CherryBatchException(batchExceptionDTO);
															}
															//step：5     插入产品入出库批次记录明细表
															try {							
																binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
															} catch (Exception e) {
																//插入产品入出库批次记录明细表失败。
																BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																batchExceptionDTO.setBatchName(this.getClass());
																batchExceptionDTO.setErrorCode("EOT00171");
																batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																batchExceptionDTO.setException(e);
																throw new CherryBatchException(batchExceptionDTO);
															}
														}else{
															try {													
																proNewBatchStock.put("StockQuantity", quantity);
																int resultNumber=binbat152_Service.updateProNewBatchStock(proNewBatchStock);
																proBatchInOutDetail.put("isNewFlag", "1");//区分是否是新数据的
																proBatchInOutDetail.put("Quantity",quantity);
																proBatchInOutDetail.put("RelSrResidualQuantity", quantity);
																proBatchInOutDetail.put("BIN_ProductBatchStockID",proNewBatchStock.get("BIN_ProductBatchStockID"));
																proBatchInOutDetail.put("CostPrice",proNewBatchStock.get("CostPrice"));
																proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
																
																if(resultNumber==0){//表示该批次库存已被其他程序修改，抛出异常整个产品单子回滚
																	BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																	batchExceptionDTO.setBatchName(this.getClass());
																	batchExceptionDTO.setErrorCode("EOT00178");
																	batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																	batchExceptionDTO.addErrorParam(ConvertUtil.getString(proNewBatchStock.get("BIN_ProductBatchStockID")));
																	batchExceptionDTO.setException(new Exception("该批次库存已被其他程序修改，请重新操作！"));
																	throw new CherryBatchException(batchExceptionDTO);
																}
															} catch (Exception e) {
																//修改产品批次库存表库存数量失败。
																BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																batchExceptionDTO.setBatchName(this.getClass());
																batchExceptionDTO.setErrorCode("EOT00170");
																batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																batchExceptionDTO.setException(e);
																throw new CherryBatchException(batchExceptionDTO);
															}
															
															//step：5     插入产品入出库批次记录明细表
															try {							
																binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
															} catch (Exception e) {
																//插入产品入出库批次记录明细表失败。
																BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
																batchExceptionDTO.setBatchName(this.getClass());
																batchExceptionDTO.setErrorCode("EOT00171");
																batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
																batchExceptionDTO.setException(e);
																throw new CherryBatchException(batchExceptionDTO);
															}
															break;
														}
													}
												}
											}
											
										}else{
											proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
											proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
											// 插入产品入出库批次记录明细表
											try {							
												binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
											} catch (Exception e) {
												//插入产品入出库批次记录明细表失败。
												BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
												batchExceptionDTO.setBatchName(this.getClass());
												batchExceptionDTO.setErrorCode("EOT00171");
												batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
												batchExceptionDTO.setException(e);
												throw new CherryBatchException(batchExceptionDTO);
											}
										}
										
									}
									else{//(表示成本价为空的入库数据，直接插入不处理)
										proBatchInOutDetail.put("isNewFlag", "0");//区分是否是新数据的
										proBatchInOutDetail.put("DetailNo", ++detailNo);//明细序号
										// 插入产品入出库批次记录明细表
										try {							
											binbat152_Service.insertProBatchInOutDetail(proBatchInOutDetail);
										} catch (Exception e) {
											//插入产品入出库批次记录明细表失败。
											BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
											batchExceptionDTO.setBatchName(this.getClass());
											batchExceptionDTO.setErrorCode("EOT00171");
											batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
											batchExceptionDTO.setException(e);
											throw new CherryBatchException(batchExceptionDTO);
										}
									}
																		
																												
								} catch (Exception e) {
									// 补录产品入出库成本失败，产品入出库批次记录明细ID：（{0}）。
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("EOT00179");
									batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOutDetail.get("BIN_ProductInOutDetailID")));
									batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									batchExceptionDTO.setException(e);
									throw new CherryBatchException(batchExceptionDTO);
								}
								
							}
							

							int sum=binbat152_Service.getCostPriceIsNullAmount(proBatchInOut);
							if(sum==0){//表示该入出库批次下的明细都有成本价
								Map<String, Object> TotalCostPriceMap=binbat152_Service.getTotalCostPrice(proBatchInOut);//得到某一单的总成本价
								TotalCostPriceMap.put("BIN_ProductBatchInOutID", proBatchInOut.get("BIN_ProductBatchInOutID"));
								try {
									binbat152_Service.updateTotalCostPrice(TotalCostPriceMap);//修改某一单的总成本价									
								} catch (Exception e) {
									//修改产品入出库批次表的总成本价失败，产品入出库批次记录ID：（{0}）。
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO(); 
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("EOT00180");
									batchExceptionDTO.addErrorParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
									batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									batchExceptionDTO.setException(e);
									throw new CherryBatchException(batchExceptionDTO);
								}
							}
							// 新后台数据源提交
							binbat152_Service.manualCommit();	
						}
					}catch(Exception e){
						// 新后台数据源回滚
						binbat152_Service.manualRollback();
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
	
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						// 补录产品入出库成本失败，产品入出库批次记录ID：（{0}），关联单号：（{1}）。
						batchLoggerDTO1.setCode("EOT00169");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("BIN_ProductBatchInOutID")));
						batchLoggerDTO1.addParam(ConvertUtil.getString(proBatchInOut.get("RelevanceNo")));
						logger.BatchLogger(batchLoggerDTO1);
						logger.outExceptionLog(e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						fReason = String.format("补录产品出库成本失败,详细信息查看Log日志", e.getMessage());
					}

                    // 处理各业务的单据号
//                    mainData.put("RelevanceNo",proBatchInOut.get("RelevanceNo"));
                    sumProductCost(proBatchInOut);
	    		}			
		}
    	
    	}

		programEnd(map);
		outMessage();
		return flag;
	}

    /**
     * 处理各业务的单据号
     * @param mainData
     */
    private void sumProductCost(Map<String, Object> mainData) {

        String tradeType = ConvertUtil.getString(mainData.get("TradeType")); // 业务类型

        // 业务类型为发货时，插入到入出库批次表后，将想要成本计算出来更新到产品发货单明细表中
        if(CherryConstants.BUSINESS_TYPE_SD.equals(tradeType)){
            mainData.put("DeliverNoIF", mainData.get("RelevanceNo"));
        }// 根据退库单单号获取带有成本的每单信息（根据退库单单号产品仓库逻辑仓库汇总成本）
        else if(CherryConstants.BUSINESS_TYPE_RR.equals(tradeType)){
            mainData.put("ReturnNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为盘点时，插入到入出库批次表后，将想要成本计算出来更新到产品盘点单明细表中
        else if(CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){
            mainData.put("StockTakingNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为调拨时（调出确认），插入到入出库批次表后，将想要成本计算出来更新到产品调出单据明细表中
        else if(CherryConstants.BUSINESS_TYPE_LG.equals(tradeType)){
            mainData.put("AllocationNoIF", mainData.get("RelevanceNo"));
        }// 业务类型为调拨时（调入确认），插入到入出库批次表后，将想要成本计算出来更新到产品调入单据明细表中
        else if (CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)) {
            mainData.put("AllocationOutNoIF", mainData.get("RelevanceNo"));
        } else {
            return;
        }
        binOLSTCM01_BL.handleProductCosByProductInOutBatch(mainData,null);
    }

	/**
	 * 将单据产品总成本写入到具体单据单明细表中
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	public void handleProductCosByProductInOutBatch(Map<String, Object> mainData, List<Map<String, Object>> detailList){

		String tradeType = ConvertUtil.getString(mainData.get("TradeType")); // 业务类型
		// 业务类型为发货时，插入到入出库批次表后，将想要成本计算出来更新到产品发货单明细表中
		if(CherryConstants.BUSINESS_TYPE_SD.equals(tradeType)){
			// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
			List<Map<String,Object>> productInfoList = binbat152_Service.getProductInfoListByDeliverNum(mainData);
			// 发货单带有成本信息不为空，
			if(null != productInfoList && productInfoList.size() != 0){
				for(Map<String, Object> productInfo : productInfoList){
					productInfo.put("UpdatedBy", "BINOLSTCM01");
					productInfo.put("UpdatePGM", "BINOLSTCM01");
					// 更新发货单明细中的总成本
					binbat152_Service.updateProductDeliverDetail(productInfo);
				}

			}
		}
		else if(CherryConstants.BUSINESS_TYPE_RR.equals(tradeType)){
			// 根据退库单单号获取带有成本的每单信息（根据退库单单号产品仓库逻辑仓库汇总成本）
			List<Map<String,Object>> productInfoList = binbat152_Service.getCostPriceByRR(mainData);
			// 退库单带有成本信息不为空，
			if(null != productInfoList && productInfoList.size() != 0){
				for(Map<String, Object> productInfo : productInfoList){
					productInfo.put("UpdatedBy", "BINOLSTCM01");
					productInfo.put("UpdatePGM", "BINOLSTCM01");
					// 更新退库单明细中的总成本
					binbat152_Service.updateProductReturnDetail(productInfo);
				}

			}
		}

		// 业务类型为盘点时，插入到入出库批次表后，将想要成本计算出来更新到产品盘点单明细表中
		else if(CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){
			// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
			List<Map<String,Object>> productInfoList = binbat152_Service.getProductInfoListByStockTakingNum(mainData);
			// 盘点单单带有成本信息不为空，
			if(null != productInfoList && productInfoList.size() != 0){
				for(Map<String, Object> productInfo : productInfoList){
					productInfo.put("UpdatedBy", "BINOLSTCM01");
					productInfo.put("UpdatePGM", "BINOLSTCM01");
					// 更新盘点单明细中的总成本
					binbat152_Service.updateProductStockTakingDetail(productInfo);
				}

			}
		}

		// 业务类型为调拨时（调出确认），插入到入出库批次表后，将想要成本计算出来更新到产品调出单据明细表中
		else if(CherryConstants.BUSINESS_TYPE_LG.equals(tradeType)){
			// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
			List<Map<String,Object>> productInfoList = binbat152_Service.getProductInfoListByAllocationOutIDNum(mainData);
			// 调出单带有成本信息不为空，
			if(null != productInfoList && productInfoList.size() != 0){
				for(Map<String, Object> productInfo : productInfoList){
					productInfo.put("UpdatedBy", "BINOLSTCM01");
					productInfo.put("UpdatePGM", "BINOLSTCM01");
					// 更新调出单明细中的总成本
					binbat152_Service.updateProductAllocationOutDetail(productInfo);
				}

			}
		}

		// 业务类型为调拨时（调入确认），插入到入出库批次表后，将想要成本计算出来更新到产品调入单据明细表中
		else if(CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)){
			// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
			List<Map<String,Object>> productInfoList = binbat152_Service.getProductInfoListByAllocationInIDNum(mainData);
			// 调出单带有成本信息不为空，
			if(null != productInfoList && productInfoList.size() != 0){
				for(Map<String, Object> productInfo : productInfoList){
					productInfo.put("UpdatedBy", "BINOLSTCM01");
					productInfo.put("UpdatePGM", "BINOLSTCM01");
					// 更新调出单明细中的总成本
					binbat152_Service.updateProductAllocationInDetail(productInfo);
				}

			}
		}

	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
			// 程序结束时，插入Job运行履历表
			paraMap.put("flag", flag);
			paraMap.put("TargetDataCNT", totalCount);
			paraMap.put("SCNT", totalCount - failCount);
			paraMap.put("FCNT", failCount);
			paraMap.put("FReason", fReason);
			binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private void init(Map<String, Object> map) {
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT152");

		// 程序【开始运行时间】
		String runStartTime = binbat152_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT152");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT152");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		//是否测试模式（若是则包含测试部门）
		String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("testMod", testMod);
	}

	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 导出件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00007");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
	}
}