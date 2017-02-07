package com.cherry.st.common.bl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.service.BINOLSTCM01_Service;

public class BINOLSTCM01_BL implements BINOLSTCM01_IF {
	
    @Resource
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource
    private BINOLSTCM01_Service binOLSTCM01_Service;
    
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private transient BINOLCM14_BL binOLCM14_BL;
    
    
    /**
     * 将产品入出库信息写入入出库主从表，并更新相应的库存记录。
     * @param mainData
     * @param detailList
     * @return 入出库主表的自增ID
     */
    @Override
    public int insertProductInOutAll(
            Map<String, Object> mainData, List<Map<String, Object>> detailList) {
        int productInOutId = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String tradeNo = ConvertUtil.getString(mainData.get("TradeNo"));
        String tradeNoIF = ConvertUtil.getString(mainData.get("TradeNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        
        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productInOutDetail = detailList.get(i);
            if(CherryUtil.obj2int(productInOutDetail.get("Quantity")) == 0){
                detailList.remove(i);
                i--;
                continue;
            }
        }
        if(detailList.size() == 0){
            return 0;
        }
        
        //如果tradeNo不存在调用共通生成单据号
        if("".equals(tradeNo)){
            tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,"IO");
            mainData.put("TradeNo", tradeNo);
        }
        if("".equals(tradeNoIF)){
            mainData.put("TradeNoIF", tradeNo);
        }
        if(null == mainData.get("StockInOutDate")){
            mainData.put("StockInOutDate", binOLSTCM01_Service.getDateYMD());
        }
        if(null == mainData.get("StockInOutTime")){
            mainData.put("StockInOutTime", binOLSTCM01_Service.getSYSDateConf());
        }
        //插入产品入出库表
        productInOutId = binOLSTCM01_Service.insertProductStockInOut(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productInOutDetail = detailList.get(i);
            productInOutDetail.put("BIN_ProductInOutID", productInOutId); 
            
            if(null == productInOutDetail.get("BIN_ProductVendorPackageID")){
                productInOutDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == productInOutDetail.get("BIN_LogicInventoryInfoID")){
                productInOutDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productInOutDetail.get("BIN_StorageLocationInfoID")){
                productInOutDetail.put("BIN_StorageLocationInfoID", 0);
            }
            
            productInOutDetail.put("DetailNo",i+1);
            
            //插入产品入出库明细表
            binOLSTCM01_Service.insertProductStockDetail(productInOutDetail);
        }
        
        // 是否记录产品入出库成本
        boolean isConfigOpen = binOLCM14_BL.isConfigOpen("1365", organizationInfoId, brandInfoId);
        if(isConfigOpen){
        	// 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
        	handleProductInOutBatch(mainData, detailList);

        }
        
        return productInOutId;
    }
    
    /**
     * 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
     * @param mainData
     * @param detailList
     * @return
     * @throws Exception 
     */
	@Override
    public int handleProductInOutBatch(Map<String, Object> mainData, List<Map<String, Object>> detailList){
    	String tradeType = ConvertUtil.getString(mainData.get("TradeType")); // 业务类型
		String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));

        //插入【产品入出库批次记录表】
        int productBatchInOutID = binOLSTCM01_Service.insertProductBatchInOut(mainData);
    	
        int detailNo = 0;
        
        boolean succDetailCP = true; // 定义所有明细成本价都成功写入  
//        BigDecimal totalCostPrice = new BigDecimal(0); // 总成本
        
		Map<String,Object> newMainData = new HashMap<String, Object>();
		newMainData.putAll(mainData);
		
		// 调入(BG) -- 调入单成本价使用关联调出(LG)单的成本价
    	if (CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)){
    		
    		List<Map<String,Object>> lgProductBatchInOutDetail = binOLSTCM01_Service.getProductBatchInOutDetailByBgRelevanceNo(newMainData); // 定义关联调出单List
    		if(null != lgProductBatchInOutDetail && lgProductBatchInOutDetail.size() != 0){
    			for(Map<String, Object> lgProductBatchMap : lgProductBatchInOutDetail){
    				
    				lgProductBatchMap.put("CreatedBy", newMainData.get("CreatedBy"));
    				lgProductBatchMap.put("CreatePGM", newMainData.get("CreatePGM"));
    				lgProductBatchMap.put("UpdatedBy", newMainData.get("UpdatedBy"));
    				lgProductBatchMap.put("UpdatePGM", newMainData.get("UpdatePGM"));
    				lgProductBatchMap.put("BIN_OrganizationInfoID", newMainData.get("BIN_OrganizationInfoID"));
    				lgProductBatchMap.put("BIN_BrandInfoID", newMainData.get("BIN_BrandInfoID"));
    				
            		lgProductBatchMap.put("BIN_InventoryInfoID", detailList.get(0).get("BIN_InventoryInfoID"));
            		lgProductBatchMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
    				
            		// 插入【产品批次库存表】
    				lgProductBatchMap.put("InQuantity", lgProductBatchMap.get("Quantity")); // 入库数量
    				lgProductBatchMap.put("StockQuantity", lgProductBatchMap.get("Quantity")); // 当前批次剩余库存数量
    				lgProductBatchMap.put("TradeNoIF", newMainData.get("RelevanceNo")); // 入库的原始单据号
            		String costPrice = ConvertUtil.getString(lgProductBatchMap.get("CostPrice"));
            		
            		int productBatchStockID = 0; // 产品批次库存ID
            		if(!ConvertUtil.isBlank(costPrice)){
            			productBatchStockID = binOLSTCM01_Service.insertProductNewBatchStock(lgProductBatchMap);
            		}else{
            			succDetailCP = false;
            		}
    				
            		lgProductBatchMap.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
            		lgProductBatchMap.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
            		lgProductBatchMap.put("RelevanceNo", newMainData.get("RelevanceNo")); // 入库的原始单据号
            		lgProductBatchMap.put("StockType", newMainData.get("StockType"));
            		lgProductBatchMap.put("StockInOutTime", newMainData.get("StockInOutTime"));
            		
            		binOLSTCM01_Service.insertProductBatchInOutDetail(lgProductBatchMap);
    			}
    		}
    	}
    	// 接收退库(AR) -- 接收退库使用关联退库(RR)
    	else if (CherryConstants.BUSINESS_TYPE_AR.equals(tradeType)){
    		List<Map<String,Object>> rrProductBatchInOutDetail = binOLSTCM01_Service.getProductBatchInOutDetailByARRelevanceNo(newMainData); // 定义关联退库单List
    		if(null != rrProductBatchInOutDetail && rrProductBatchInOutDetail.size() != 0){
    			for(Map<String, Object> rrProductBatchMap : rrProductBatchInOutDetail){
    				
    				rrProductBatchMap.put("CreatedBy", newMainData.get("CreatedBy"));
    				rrProductBatchMap.put("CreatePGM", newMainData.get("CreatePGM"));
    				rrProductBatchMap.put("UpdatedBy", newMainData.get("UpdatedBy"));
    				rrProductBatchMap.put("UpdatePGM", newMainData.get("UpdatePGM"));
    				rrProductBatchMap.put("BIN_OrganizationInfoID", newMainData.get("BIN_OrganizationInfoID"));
    				rrProductBatchMap.put("BIN_BrandInfoID", newMainData.get("BIN_BrandInfoID"));
    				
            		rrProductBatchMap.put("BIN_InventoryInfoID", detailList.get(0).get("BIN_InventoryInfoID"));
            		rrProductBatchMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
    				
            		// 插入【产品批次库存表】
    				rrProductBatchMap.put("InQuantity", rrProductBatchMap.get("Quantity")); // 入库数量
    				rrProductBatchMap.put("StockQuantity", rrProductBatchMap.get("Quantity")); // 当前批次剩余库存数量
    				rrProductBatchMap.put("TradeNoIF", newMainData.get("RelevanceNo")); // 入库的原始单据号
            		String costPrice = ConvertUtil.getString(rrProductBatchMap.get("CostPrice"));
            		
            		int productBatchStockID = 0; // 产品批次库存ID
            		if(!ConvertUtil.isBlank(costPrice)){
            			productBatchStockID = binOLSTCM01_Service.insertProductNewBatchStock(rrProductBatchMap);
            		} else{
            			succDetailCP = false;
            		}
    				
            		rrProductBatchMap.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
            		rrProductBatchMap.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
            		rrProductBatchMap.put("RelevanceNo", newMainData.get("RelevanceNo")); // 入库的原始单据号
            		rrProductBatchMap.put("StockType", newMainData.get("StockType"));
            		rrProductBatchMap.put("StockInOutTime", newMainData.get("StockInOutTime"));

            		binOLSTCM01_Service.insertProductBatchInOutDetail(rrProductBatchMap);
    				
    			}
    		}
    	}
    	
    	// 调拨及退库之外其他业务
    	else {
    		
//    		List<String> productInOutDetailIDBySR = new ArrayList<String>(); // 记录关联退货时，已经被用完的销售明细（防止一个产品有多个销售明细且价格不一致时，出现退货入库价格问题） 
    		
    		for(int i=0;i<detailList.size();i++){
    			Map<String,Object> itemDetailMap = detailList.get(i);
    			Map<String,Object> productInOutDetail = new HashMap<String, Object>();
    			productInOutDetail.putAll(itemDetailMap);
    			productInOutDetail.put("BIN_OrganizationInfoID", newMainData.get("BIN_OrganizationInfoID"));
    			productInOutDetail.put("BIN_BrandInfoID", newMainData.get("BIN_BrandInfoID"));
    			productInOutDetail.put("StockInOutTime", newMainData.get("StockInOutTime")); // 入出库时间
				productInOutDetail.put("StockInOutDate", newMainData.get("StockInOutDate"));// 入出库日期
    			
    			String stockType = ConvertUtil.getString(productInOutDetail.get("StockType")); // 明细出入库区分 ( 0：入库 1：出库 )
    			
    			// 入库处理(根据业务类型确定入库规则)
    			if(CherryConstants.STOCK_TYPE_IN.equals(stockType)){
    				
    				List<Map<String,Object>> saleProductBatchInOutDetail = null; // 定义关联退货单List
    				
    				// 盘点(盘盈)(最后一笔成本价 )
    				if(CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){

						String costPriceCA = null;
						// 取得产品批次库存表指定仓库产品的末次信息
						Map<String,Object> topProductNewBatchStockMap = binOLSTCM01_Service.getProductNewBatchStock(productInOutDetail);

						if(null != topProductNewBatchStockMap && !topProductNewBatchStockMap.isEmpty()){
							costPriceCA = ConvertUtil.getString(topProductNewBatchStockMap.get("CostPrice"));
							productInOutDetail.put("CostPrice", !"".equals(costPriceCA) ? costPriceCA : null);
						} else{
							/**
							 * 系统配置项[初始盘盈时的入库成本价使用的价格]:'':不处理; 'DistributionPrice':配送价;'StandardCost':结算价;
							 */
							String priceConfig = binOLCM14_BL.getConfigValue("1395", organizationInfoId, brandInfoId);
							// 根据产品厂商ID及入出库日期取得产品的价格想着信息
							Map<String,Object> productPrice = binOLSTCM01_Service.getProductPriceByID(productInOutDetail);
							if(!"".equals(priceConfig) && null != productPrice && !productPrice.isEmpty()) {
								// 取指定价格
								costPriceCA = ConvertUtil.getString(productPrice.get(priceConfig));
								productInOutDetail.put("Comments",priceConfig+","+costPriceCA);
							} else {
								costPriceCA = "";
							}
							productInOutDetail.put("CostPrice", !"".equals(costPriceCA) ? costPriceCA : null);

						}
						// costPriceCA无值时
						succDetailCP = !"".equals(costPriceCA);
    					
    				}
    				// 退货
    				else if (CherryConstants.BUSINESS_TYPE_SR.equals(tradeType)){
    					String stockInOut_SRrelevantNo = ConvertUtil.getString(newMainData.get("stockInOut_SrRelevantNo"));
    					
    					// 空退(空退的情况下以最近的批次作为成本价格。)
    					if(ConvertUtil.isBlank(stockInOut_SRrelevantNo)){
    						
    						// 取得产品批次库存表指定仓库产品的末次信息
    						Map<String,Object> topProductNewBatchStockMap = binOLSTCM01_Service.getProductNewBatchStock(productInOutDetail);
    						
        					if(null != topProductNewBatchStockMap && !topProductNewBatchStockMap.isEmpty()){
        						productInOutDetail.put("CostPrice", topProductNewBatchStockMap.get("CostPrice"));
        					} else{
        						productInOutDetail.put("CostPrice", null);
        						
        						succDetailCP = false;
        					}
    						
    					} 
    					// 关联退货(关联退货的情况下按照销售单的成本)
    					else{
    						productInOutDetail.put("RelevanceNo", stockInOut_SRrelevantNo);
    						saleProductBatchInOutDetail = binOLSTCM01_Service.getProductBatchInOutDetailByRelevanceNoAndPrt(productInOutDetail);
    					}
    				}
    				// 其他入库业务（先进先出）
    				else{
    					// 取得产品库存表指定仓库产品的首末次信息
//            		productInOutDetail.put("stockInTimeSorting", "ASC"); // 排序方式
//            		Map<String,Object> topProductNewBatchStockMap = binOLSTCM01_Service.getProductNewBatchStock(productInOutDetail);
    					productInOutDetail.put("CostPrice", productInOutDetail.get("Price")); // 成本价
    				}
    				
    				// 关联退货 关联退货可能存在出库时根据库存拆成多个单子的情况，此处也需要如此处理
    				if(CherryConstants.BUSINESS_TYPE_SR.equals(tradeType) && !CherryUtil.isBlankList(saleProductBatchInOutDetail)){
    					
    					int srQuantity = ConvertUtil.getInt(productInOutDetail.get("Quantity")); // 退货产品数量
//            		String costPrice = ConvertUtil.getString(saleProductBatchInOutDetail.get(0).get("CostPrice"));
//            		productInOutDetail.put("CostPrice", costPrice); // 成本价
    					for(Map<String,Object> salePrtBatchInOutDetail : saleProductBatchInOutDetail){
    						String costPrice = ConvertUtil.getString(salePrtBatchInOutDetail.get("CostPrice"));
    						int salePrtBatchInOutQuantity = ConvertUtil.getInt(salePrtBatchInOutDetail.get("RelSrResidualQuantity"));
    						String productInOutDetailID =  ConvertUtil.getString(salePrtBatchInOutDetail.get("BIN_ProductInOutDetailID"));
    						
    						// 销售有多条相同产品明细时，如果该明细已经被使用完，则跳出继续取该产品的下一次明细进出入库
//    						if(productInOutDetailIDBySR.contains(productInOutDetailID)){
//    							continue; 
//    						}
    						
    						if(srQuantity == 0){
    							break;
    						} 
    						else if(salePrtBatchInOutQuantity <= srQuantity){ //表示关联销售的出库记录数量小于退货明细
    							srQuantity = srQuantity - salePrtBatchInOutQuantity;
    							productInOutDetail.put("Quantity", salePrtBatchInOutQuantity); //  数量
    							
    							// 记录关联销售明细的产品已经完全退掉的入出库明细ID
//    							productInOutDetailIDBySR.add(productInOutDetailID);
    							
    							// 更新退货关联的销售入出库明细表的剩余可退货数量
    							Map<String,Object> updMap = new HashMap<String, Object>();
    							updMap.put("BIN_ProductInOutDetailID", productInOutDetailID);
    							updMap.put("Quantity", salePrtBatchInOutQuantity);
    							updMap.put("updatedBy", newMainData.get("UpdatedBy"));
    							updMap.put("updatePGM", newMainData.get("UpdatePGM"));
    							binOLSTCM01_Service.updProductBatchInOutDetail(updMap);
    						} 
    						else{
    							productInOutDetail.put("Quantity",srQuantity); // 数量
    							
    							// 更新退货关联的销售入出库明细表的剩余可退货数量
    							Map<String,Object> updMap = new HashMap<String, Object>();
    							updMap.put("BIN_ProductInOutDetailID", productInOutDetailID);
    							updMap.put("Quantity", srQuantity);
    							updMap.put("updatedBy", newMainData.get("UpdatedBy"));
    							updMap.put("updatePGM", newMainData.get("UpdatePGM"));
    							binOLSTCM01_Service.updProductBatchInOutDetail(updMap);
    							
    							// 当前明细退货数量全部完成，置0
    							srQuantity = 0;
    						}
    						
    						productInOutDetail.put("CostPrice", !ConvertUtil.isBlank(costPrice) ? costPrice : null); // 成本价
    						
    						// 插入【产品批次库存表】
    						productInOutDetail.put("InQuantity", productInOutDetail.get("Quantity")); // 入库数量
    						productInOutDetail.put("StockQuantity", productInOutDetail.get("Quantity")); // 当前批次剩余库存数量
    						
    						productInOutDetail.put("TradeNoIF", newMainData.get("RelevanceNo")); // 入库的原始单据号(退货单号)
    						int productBatchStockID = 0;
    						if(!ConvertUtil.isBlank(costPrice)){
    							productBatchStockID = binOLSTCM01_Service.insertProductNewBatchStock(productInOutDetail);
    						} else{
    							succDetailCP = false;
    						}
    						
    						// 插入【产品入出库批次明细记录表】
    						productInOutDetail.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
    						productInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
    						productInOutDetail.put("RelevanceNo", newMainData.get("RelevanceNo")); // 入库的原始单据号(退货单号)
    						
    						// 计算总成本 
//                		totalCostPrice = totalCostPrice.add(new BigDecimal(costPrice));
    						
    						detailNo++;
    						productInOutDetail.put("DetailNo", detailNo);
    						
    						binOLSTCM01_Service.insertProductBatchInOutDetail(productInOutDetail);
    					}
    					
    				}else{
    					// 插入【产品批次库存表】
    					productInOutDetail.put("InQuantity", productInOutDetail.get("Quantity")); // 入库数量
    					productInOutDetail.put("StockQuantity", productInOutDetail.get("Quantity")); // 当前批次剩余库存数量
    					productInOutDetail.put("TradeNoIF", newMainData.get("RelevanceNo")); // 入库的原始单据号
    					String costPrice = ConvertUtil.getString(productInOutDetail.get("CostPrice"));
    					
    					int productBatchStockID = 0; // 产品批次库存ID
    					if(!ConvertUtil.isBlank(costPrice)){
    						productBatchStockID = binOLSTCM01_Service.insertProductNewBatchStock(productInOutDetail);
    					} else{
    						succDetailCP = false;
    					}
    					
    					//插入【产品入出库批次明细记录表】
    					productInOutDetail.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
    					productInOutDetail.put("BIN_ProductBatchStockID", productBatchStockID != 0 ? productBatchStockID : null); // 产品库存批次ID 
    					productInOutDetail.put("RelevanceNo", newMainData.get("RelevanceNo")); // 入库的原始单据号
    					
    					// 计算总成本 
//            		totalCostPrice = totalCostPrice.add(new BigDecimal(ConvertUtil.getDouble(productInOutDetail.get("CostPrice"))));
    					
    					detailNo++;
    					productInOutDetail.put("DetailNo", detailNo);
    					
    					binOLSTCM01_Service.insertProductBatchInOutDetail(productInOutDetail);
    				}
    				
    			} 
    			// 出库处理
    			else if(CherryConstants.STOCK_TYPE_OUT.equals(stockType)){
    				
    				// 检查出库要求的批次库存是否满足
//            	Map<String, Object> checkProductNewBatchStock = binOLSTCM01_Service.checkProductNewBatchStock(productInOutDetail);
    				productInOutDetail.put("TradeType", newMainData.get("TradeType"));
    				productInOutDetail.put("outQuantity", productInOutDetail.get("Quantity")); // 出库数量
    				productInOutDetail.put("stockInTimeSorting", "ASC"); // 排序方式
    				List<Map<String,Object>> productNewBatchStockList = binOLSTCM01_Service.getProductNewBatchStockListForOutStock(productInOutDetail);
    				
    				if(null != productNewBatchStockList && productNewBatchStockList.size() != 0){
    					
    					List<Map<String,Object>> preProductNewBatchStockList = new ArrayList<Map<String,Object>>(); // 预存回撤批次库存的集合
    					List<Map<String,Object>> preProductBatchInOutDetailList = new ArrayList<Map<String,Object>>(); // 预存插入到入出库批次明细表的集合数据
    					
//            		String countBatchSize =  ConvertUtil.getString(checkProductNewBatchStock.get("CountBatchSize")); // 满足条件的批次库存条数
    					
    					// 取得产品库存表指定仓库产品的集合
//            		productInOutDetail.put("stockInTimeSorting", "ASC"); // 排序方式
//            		productInOutDetail.put("countBatchSize", countBatchSize); // 取得集合的件数
    					// List<Map<String,Object>> productNewBatchStockList2 = binOLSTCM01_Service.getProductNewBatchStockList(productInOutDetail);
    					
    					int outQuantity = ConvertUtil.getInt(productInOutDetail.get("Quantity")); // 出库数量
    					
    					Map<String, Object> oldProductInOutDetail = new HashMap<String, Object>(); // 定义原出库明细（拆分失败后，直接使用该明细写入【入出库批次明细表】）
    					oldProductInOutDetail.putAll(productInOutDetail);
    					oldProductInOutDetail.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
    					oldProductInOutDetail.put("RelevanceNo", newMainData.get("RelevanceNo")); // 出库的原始单据号
    					oldProductInOutDetail.put("DetailNo", detailNo + 1);
    					
    					boolean splitResult = true; // 拆分结果 
    					int splitStockNum = 0;
    					for(Map<String, Object> proNewBatchStock : productNewBatchStockList){
    						
    						Map<String,Object> newProductInOutDetail = new HashMap<String, Object>();
    						newProductInOutDetail.putAll(productInOutDetail);
    						
    						splitStockNum++;
//						detailNo = oldDetailNo;
    						
    						int stockQuantity= ConvertUtil.getInt(proNewBatchStock.get("StockQuantity")); 
    						proNewBatchStock.put("OldStockQuantity", stockQuantity); // 取出时的批次库存数量
    						
    						boolean isGoOn = true; // 是否还需要继续拆分
    						if(stockQuantity < outQuantity){ //表示库存数小于明细出库数量
    							outQuantity -= stockQuantity;
    							// update时 -JSHuistockQuantity
    							proNewBatchStock.put("NewStockQuantity", stockQuantity); // 需要扣除的批次库存数量
    							preProductNewBatchStockList.add(proNewBatchStock);
    						}
    						else{
    							// update时 -JSHuistockQuantity
    							proNewBatchStock.put("NewStockQuantity", outQuantity); // 需要扣除的批次库存数量
    							preProductNewBatchStockList.add(proNewBatchStock);
    							isGoOn = false;
    						}
    						
    						// 扣减批次库存
    						proNewBatchStock.put("updatedBy", productInOutDetail.get("UpdatedBy"));
    						proNewBatchStock.put("updatePGM", productInOutDetail.get("UpdatePGM"));
    						int updCount = binOLSTCM01_Service.updProductNewBatchStock(proNewBatchStock);
    						if(updCount == 0){ // 扣减库存失败，放弃本次出库成本记录
    							splitResult = false;
    							break;
    						}
    						
    						// 写入出库批次明细表(出库)
    						newProductInOutDetail.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
    						newProductInOutDetail.put("BIN_ProductBatchStockID",proNewBatchStock.get("BIN_ProductBatchStockID")); // 产品批次库存ID
    						newProductInOutDetail.put("RelevanceNo", newMainData.get("RelevanceNo")); // 出库的原始单据号
    						
    						newProductInOutDetail.put("CostPrice", proNewBatchStock.get("CostPrice")); // 产品批次库存ID
    						newProductInOutDetail.put("Quantity", proNewBatchStock.get("NewStockQuantity")); // 拆分的库存数量 
    						
    						newProductInOutDetail.put("DetailNo", detailNo + splitStockNum);
//                		detailNo++;
    						
    						// 计算总成本 
//	            		totalCostPrice = totalCostPrice.add(new BigDecimal(ConvertUtil.getDouble(newProductInOutDetail.get("CostPrice"))));
    						
    						preProductBatchInOutDetailList.add(newProductInOutDetail);
    						// binOLSTCM01_Service.insertProductBatchInOutDetail(productInOutDetail);
    						
    						if(!isGoOn){ // 拆分成功，可以了
    							break;
    						}
    					}
    					
    					if(splitResult){ // 拆分成本成功
    						// 循环写入【入出库批次明细记录】
    						for(Map<String, Object> productInOutDetailMap : preProductBatchInOutDetailList){
    							binOLSTCM01_Service.insertProductBatchInOutDetail(productInOutDetailMap);
    						}
    						detailNo =  detailNo + splitStockNum;
    						
    					} 
    					// 拆分失败
    					else {
    						
    						// 撤回已拆分成功的批次库存preProductNewBatchStockList
    						if(!CherryUtil.isBlankList(preProductNewBatchStockList)){
    							for(Map<String, Object> proNewBatchStock : preProductNewBatchStockList){
    								int newStockQuantity = ConvertUtil.getInt(proNewBatchStock.get("NewStockQuantity"));
    								proNewBatchStock.put("NewStockQuantity", -newStockQuantity);
    								int updCount = binOLSTCM01_Service.updProductNewBatchStock(proNewBatchStock);
    							}
    						}
    						
    						succDetailCP = false; // 成本价写入失败
    						
    						// 原批次明细写入【入出库批次明细表】）
    						binOLSTCM01_Service.insertProductBatchInOutDetail(oldProductInOutDetail);
    					}
    					
    				} 
    				// 不满足扣减库存（ 直接写入入出库明细批次主从表）--不写成本及批次库存ID
    				else{
    					// 写入出库批次明细表(出库)
    					productInOutDetail.put("BIN_ProductBatchInOutID", productBatchInOutID); // 产品入出库批次记录ID
//					productInOutDetail.put("BIN_ProductBatchStockID", proNewBatchStock.get("BIN_ProductBatchStockID")); // 产品批次库存ID
    					productInOutDetail.put("RelevanceNo", newMainData.get("RelevanceNo")); // 出库的原始单据号
    					
//					productInOutDetail.put("CostPrice", proNewBatchStock.get("CostPrice")); // 产品批次库存ID
    					productInOutDetail.put("Quantity", productInOutDetail.get("Quantity")); // 原出库的数量
    					
    					detailNo++;
    					productInOutDetail.put("DetailNo", detailNo);
    					
    					binOLSTCM01_Service.insertProductBatchInOutDetail(productInOutDetail);
    					
    					succDetailCP = false; // 成本价写入失败
    				}
    			}
    		}
    	}
		if(succDetailCP){ // 更新成本价成功
			newMainData.put("BIN_ProductBatchInOutID", productBatchInOutID);
			binOLSTCM01_Service.updProductBatchInOut(newMainData);
		}
        
    	// 通过产品入出库批次表将成本写入对应单据明细表
    	handleProductCosByProductInOutBatch(mainData, detailList);
    	
    	return 1;
    	
    }

    /**
     * 修改入出库单据主表数据。
     * @param praMap
     * @return 更新影响的数据行数
     */
    @Override
    public int updateProductInOutMain(Map<String, Object> praMap) {
        return binOLSTCM01_Service.updateProductInOutMain(praMap);
    }

    /**
     * 根据入出库单据修改库存。
     * @param praMap
     */
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int productInOutId = CherryUtil.string2int(ConvertUtil.getString(praMap.get("BIN_ProductInOutID")));
        String updatedBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String updatePGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        List<Map<String,Object>> list = getProductInOutDetailData(productInOutId,null);
        for(int i=0;i<list.size();i++){
            Map<String,Object> productStock = list.get(i);
            if(null == productStock.get("BIN_LogicInventoryInfoID")){
                productStock.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productStock.get("BIN_StorageLocationInfoID")){
                productStock.put("BIN_StorageLocationInfoID", 0);
            }
            if(null == productStock.get("BIN_ProductVendorPackageID")){
                productStock.put("BIN_ProductVendorPackageID", 0);
            }
            String productBatchId = ConvertUtil.getString(productStock.get("BIN_ProductBatchID"));
            productStock.put("UpdatedBy", updatedBy);
            productStock.put("UpdatePGM", updatePGM);
            int cnt = binOLSTCM01_Service.updateProductStock(productStock);
            productStock.put("CreatedBy", updatedBy);
            productStock.put("CreatePGM", updatePGM);
            if(cnt<1){
                binOLSTCM01_Service.insertProductStock(productStock);
            }
            
            //操作【产品批次库存表】
            if(!"".equals(productBatchId)){
                int count = binOLSTCM01_Service.updateProductBatchStock(productStock);
                if(count<1){
                    binOLSTCM01_Service.insertProductBatchStock(productStock);
                }
            }
        }
    }
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param productInOutMainID
     * @return
     */
    @Override
    public Map<String,Object> getProductInOutMainData (
            int productInOutMainID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductInOutID", productInOutMainID);
        map.put("language", language);
        return binOLSTCM01_Service.getProductInOutMainData(map);
    }

    /**
     * 给定入出库单主ID，取得明细信息。
     * @param productInOutMainID
     * @return
     */
    @Override
    public List<Map<String, Object>> getProductInOutDetailData(
            int productInOutMainID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductInOutID", productInOutMainID);
        map.put("language", language);
        return binOLSTCM01_Service.getProductInOutDetailData(map);
    }
    
    

    
    /**
     * 将单据产品总成本写入到具体单据单明细表中
     * @param mainData
     * @param detailList
     * @return
     */
	@Override
    public void handleProductCosByProductInOutBatch(Map<String, Object> mainData, List<Map<String, Object>> detailList){
    	
    	String tradeType = ConvertUtil.getString(mainData.get("TradeType")); // 业务类型
    	// 业务类型为发货时，插入到入出库批次表后，将想要成本计算出来更新到产品发货单明细表中
    	if(CherryConstants.BUSINESS_TYPE_SD.equals(tradeType)){
    		// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
    		List<Map<String,Object>> productInfoList = binOLSTCM01_Service.getProductInfoListByDeliverNum(mainData);
    		// 发货单带有成本信息不为空，
    		if(null != productInfoList && productInfoList.size() != 0){
    			for(Map<String, Object> productInfo : productInfoList){
    				productInfo.put("UpdatedBy", "BINOLSTCM01");
    				productInfo.put("UpdatePGM", "BINOLSTCM01");
    				// 更新发货单明细中的总成本

					binOLSTCM01_Service.updateProductDeliverDetail(productInfo);
    			}
    			
    		}
    	}
    	else if(CherryConstants.BUSINESS_TYPE_RR.equals(tradeType)){
    		// 根据退库单单号获取带有成本的每单信息（根据退库单单号产品仓库逻辑仓库汇总成本）
    		List<Map<String,Object>> productInfoList = binOLSTCM01_Service.getCostPriceByRR(mainData);
    		// 退库单带有成本信息不为空，
    		if(null != productInfoList && productInfoList.size() != 0){
    			for(Map<String, Object> productInfo : productInfoList){
    				productInfo.put("UpdatedBy", "BINOLSTCM01");
    				productInfo.put("UpdatePGM", "BINOLSTCM01");
    				// 更新退库单明细中的总成本
    				binOLSTCM01_Service.updateProductReturnDetail(productInfo);
    			}
    			
    		}
    	}
    	
    	// 业务类型为盘点时，插入到入出库批次表后，将想要成本计算出来更新到产品盘点单明细表中
    	else if(CherryConstants.BUSINESS_TYPE_CA.equals(tradeType)){
    		// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
    		List<Map<String,Object>> productInfoList = binOLSTCM01_Service.getProductInfoListByStockTakingNum(mainData);
    		// 盘点单单带有成本信息不为空，
    		if(null != productInfoList && productInfoList.size() != 0){
    			for(Map<String, Object> productInfo : productInfoList){
    				productInfo.put("UpdatedBy", "BINOLSTCM01");
    				productInfo.put("UpdatePGM", "BINOLSTCM01");
    				// 更新盘点单明细中的总成本
    				binOLSTCM01_Service.updateProductStockTakingDetail(productInfo);
    			}
    			
    		}
    	}
    	
    	// 业务类型为调拨时（调出确认），插入到入出库批次表后，将想要成本计算出来更新到产品调出单据明细表中
    	else if(CherryConstants.BUSINESS_TYPE_LG.equals(tradeType)){
    		// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
    		List<Map<String,Object>> productInfoList = binOLSTCM01_Service.getProductInfoListByAllocationOutIDNum(mainData);
    		// 调出单带有成本信息不为空，
    		if(null != productInfoList && productInfoList.size() != 0){
    			for(Map<String, Object> productInfo : productInfoList){
    				productInfo.put("UpdatedBy", "BINOLSTCM01");
    				productInfo.put("UpdatePGM", "BINOLSTCM01");
    				// 更新调出单明细中的总成本
    				binOLSTCM01_Service.updateProductAllocationOutDetail(productInfo);
    			}
    			
    		}
    	}
    	
    	// 业务类型为调拨时（调入确认），插入到入出库批次表后，将想要成本计算出来更新到产品调入单据明细表中
    	else if(CherryConstants.BUSINESS_TYPE_BG.equals(tradeType)){
    		// 根据单号获取带有成本的每单信息（更具单号产品仓库逻辑仓库汇总成本）
    		List<Map<String,Object>> productInfoList = binOLSTCM01_Service.getProductInfoListByAllocationInIDNum(mainData);
    		// 调出单带有成本信息不为空，
    		if(null != productInfoList && productInfoList.size() != 0){
    			for(Map<String, Object> productInfo : productInfoList){
    				productInfo.put("UpdatedBy", "BINOLSTCM01");
    				productInfo.put("UpdatePGM", "BINOLSTCM01");
    				// 更新调出单明细中的总成本
    				binOLSTCM01_Service.updateProductAllocationInDetail(productInfo);
    			}
    			
    		}
    	}
    	
    }
    
}
