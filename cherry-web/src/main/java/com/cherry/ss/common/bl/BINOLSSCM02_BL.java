/*	
 * @(#)BINOLSSCM02_BL.java     1.0 2010/10/29		
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
import com.cherry.ss.common.service.BINOLSSCM02_Service;

/**
 * 库存操作的共通机能
 * @author dingyc
 *
 */
public class BINOLSSCM02_BL {
	@Resource
	private BINOLSSCM02_Service binolsscm02_service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	/**
	 * 指定组织ID和品牌ID取得促销品所有大分类
	 * @param map
	 * @return
	 */
	public List getPrimaryCategory(UserInfo userinfo){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("BIN_OrganizationInfoID", userinfo.getBIN_OrganizationInfoID());
		map.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
		map.put("language", userinfo.getLanguage());		
		return binolsscm02_service.getPrimaryCategory(map);
	}	
	
	
	/**
	 * 取得指定组织，品牌，大分类下的所有中分类 
	 * @param map
	 * @return
	 */
	public List getSecondryCategory(UserInfo userinfo,String primaryCategoryCode){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("BIN_OrganizationInfoID", userinfo.getBIN_OrganizationInfoID());
		map.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
		map.put("PrimaryCategoryCode", primaryCategoryCode);
		map.put("language", userinfo.getLanguage());
		return binolsscm02_service.getSecondryCategory(map);
	}
	
	/**
	 * 取得指定组织，品牌，大分类，中分类下的所有小分类 
	 * @param map
	 * @return
	 */
	public List getSmallCategory(UserInfo userinfo,String primaryCategoryCode,String secondryCategoryCode){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("BIN_OrganizationInfoID", userinfo.getBIN_OrganizationInfoID());
		map.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
		map.put("PrimaryCategoryCode", primaryCategoryCode);
		map.put("SecondryCategoryCode", secondryCategoryCode);
		map.put("language", userinfo.getLanguage());
		return binolsscm02_service.getSmallCategory(map);
	}
	
	/**
	 * 取得待盘点的产品列表
	 * @param userinfo
	 * @param map
	 * @return
	 */
	public List getStocktakingPromotionList(UserInfo userinfo,Map<String,Object> map){
		map.put("BIN_OrganizationID", userinfo.getCurrentOrganizationID());
		map.put("BIN_OrganizationInfoID", userinfo.getBIN_OrganizationInfoID());
		map.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
		map.put("language", userinfo.getLanguage());
		if("0".equals(userinfo.getCurrentOrganizationType())){
			//总部盘点
			map.put("OrganizationType", "0");
		}else{
			//其他部门盘点
			map.put("OrganizationType", "1");			
		}
		List<Map<String,Object>> StocktakingPromotionList = binolsscm02_service.getStocktakingPromotionList(map);
		 //如果IsStock为0（不需要管理库存），就过滤掉
        for(int i=0;i<StocktakingPromotionList.size();i++){
            Map<String,Object> StocktakingPromotionMap = StocktakingPromotionList.get(i);
            String IsStock =(String)StocktakingPromotionMap.get("isStock");
            if("0".equals(IsStock)){
            	StocktakingPromotionList.remove(i);
                i--;
                continue;
            }
        }
        return StocktakingPromotionList;
	}
	
	/**
	 * 取得指定仓库中指定促销品的库存数量
	 * @param map
	 * @return
	 */
	public List getStockCount(Map<String,Object> map){
		return binolsscm02_service.getStockCount(map);
	}
	
	/**
	 * 取得指定促销产品厂商Id是否需要管理库存状态 
	 * @param map
	 * @return
	 */
	public List getIsStock(Map<String,Object> map){
		return binolsscm02_service.getIsStock(map);
	}
	
	/**
	 * 插入【促销产品盘点业务单据表】
	 * @param userinfo
	 * @param map
	 * @return
	 */
	public int insertPromotionStockTaking(Map<String,Object> map){
		return binolsscm02_service.insertPromotionStockTaking(map);
	}
	
	/**
	 * 插入【促销产品盘点业务单据明细表】
	 * @param userinfo
	 * @param map
	 * @return
	 */
	public void insertPromotionTakingDetail(List<Map<String, Object>> list){
		for(int i=0;i<list.size();i++){
			Map<String, Object> temp=list.get(i);
			if(temp.get("BIN_ProductVendorPackageID")==null){
				temp.put("BIN_ProductVendorPackageID","0");
			}
			if(temp.get("BIN_LogicInventoryInfoID")==null){
				temp.put("BIN_LogicInventoryInfoID","0");
			}
			if(temp.get("BIN_StorageLocationInfoID")==null){
				temp.put("BIN_StorageLocationInfoID","0");
			}
		}
		 binolsscm02_service.insertPromotionTakingDetail(list);
	}
	
	/**
	 * 将一组盘点单据ID对应的数据写入到【促销产品入出库表】和【促销产品入出库明细表】
	 * @param promotionStockTakingIDArr 盘点单据ID数组
	 * @param userInfo
	 */
	public void insertStockInOutAndDetail(int[] promotionStockTakingIDArr,UserInfo userInfo){
		//整理传入的参数
		String currentOrganizationInfoID = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String currentBrandInfoID = String.valueOf(userInfo.getBIN_BrandInfoID());
		String currentUnit = userInfo.getCurrentUnit();
		String loginName = userInfo.getLoginName();		
		
		for(int i=0;i<promotionStockTakingIDArr.length;i++){
			//取得指定ID的盘点单据信息（包括详细）
			List<Map<String,Object>> list = binolsscm02_service.getStockTakingInfoByID(promotionStockTakingIDArr[i]);		
			String deliverNo ="";
			int promotionStockInOutID = 0;
			
	        //当明细盘差全是0，入出库主表从表都不插入，其他情况只插入盘差不是0的明细  
	        for(int j=0;j<list.size();j++){
	            Map<String,Object> deliverDetailMap = list.get(j);
	            if(CherryUtil.obj2int(deliverDetailMap.get("GainQuantity")) == 0){
	                list.remove(j);
	                j--;
	                continue;
	            }
	        }
			
			for(int n=0;n<list.size();n++){
				Map<String,Object> deliverDetailMap = list.get(n);
				if(n==0){
					int countTotal = CherryUtil.obj2int(deliverDetailMap.get("mTotalQuantity"));
					String flag ="";
					if(countTotal>=0){
						flag= CherryConstants.BUSINESS_TYPE_STORAGE_IN;
					}else{
						flag= CherryConstants.BUSINESS_TYPE_STORAGE_OUT;
					}
					//【促销产品入出库表】				
					//插入入出库表前，取得单据号
					deliverNo = binOLCM03_BL.getTicketNumber(currentOrganizationInfoID,currentBrandInfoID,loginName, flag);
					Map<String,Object> stockInOutMap = new HashMap<String,Object>();
	
					//主表      单据号			
					stockInOutMap.put("TradeNo", deliverNo);
					//主表      接口单据号
					stockInOutMap.put("TradeNoIF", deliverNo);
					//主表       组织ID
					stockInOutMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
					//主表       品牌ID
					stockInOutMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
					//主表      入出库部门
					stockInOutMap.put("BIN_OrganizationID", deliverDetailMap.get("mBIN_OrganizationID"));
					//主表      制单员工
					stockInOutMap.put("BIN_EmployeeID", deliverDetailMap.get("mBIN_EmployeeID"));
					//主表      总数量

					stockInOutMap.put("TotalQuantity", Math.abs(countTotal));
					//主表      总金额
					stockInOutMap.put("TotalAmount", deliverDetailMap.get("mTotalAmount"));
					//主表      业务类型  P:盘点
					stockInOutMap.put("TradeType", deliverDetailMap.get("mTradeType"));
					//主表      入出库区分 0：入库	1：出库     TODO: 盘点统一设置为0					
					stockInOutMap.put("StockType", countTotal>=0?"0":"1");
					//主表      关联单号  即为盘点单号
					stockInOutMap.put("RelevantNo", deliverDetailMap.get("mStockTakingNo"));
					//主表     TODO:物流ID
					stockInOutMap.put("BIN_LogisticInfoID", null);
					//主表     入出库理由
					stockInOutMap.put("Reason", deliverDetailMap.get("mReason"));
					//主表     入出库日期
					stockInOutMap.put("StockInOutDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));				
					//主表     审核区分
					stockInOutMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
					//入出库前促销品总金额
					stockInOutMap.put("TotalAmountBefore", null);
					//入出库后促销品总金额
					stockInOutMap.put("TotalAmountAfter", null);
					//主表    有效区分
					stockInOutMap.put("ValidFlag", "1");	
					//主表    共通字段
					stockInOutMap.put("CreatedBy", loginName);	
					stockInOutMap.put("CreatePGM", currentUnit);	
					stockInOutMap.put("UpdatedBy", loginName);	
					stockInOutMap.put("UpdatePGM", currentUnit);
					//insert 【促销产品入出库表】并返回自增长ID
					promotionStockInOutID = binolsscm02_service.insertPromotionStockInOut(stockInOutMap);
				}			
				//【促销产品入出库明细表】和【促销产品收发货业务单据明细表】结构大致相同，仅需要整理下不同的几个字段即可
				//明细表    促销产品入出库记录ID
				deliverDetailMap.put("BIN_PromotionStockInOutID", promotionStockInOutID);
				//明细表    明细连番
				deliverDetailMap.put("DetailNo", n+1);
				//明细表    入出库区分 0：入库	1：出库
				int gainQuantity = CherryUtil.string2int(String.valueOf(deliverDetailMap.get("GainQuantity")));
				if(gainQuantity>=0){
					deliverDetailMap.put("StockType", "0");
					//明细表    入出库数量
					deliverDetailMap.put("Quantity", gainQuantity);
				}else{
					deliverDetailMap.put("StockType", "1");
					//明细表    入出库数量
					deliverDetailMap.put("Quantity", 0-gainQuantity);
				}
				//明细表    有效区分
				deliverDetailMap.put("ValidFlag", "1");
				//明细表    共通字段
				deliverDetailMap.put("CreatedBy", loginName);	
				deliverDetailMap.put("CreatePGM", currentUnit);	
				deliverDetailMap.put("UpdatedBy", loginName);	
				deliverDetailMap.put("UpdatePGM", currentUnit);
				
				//促销产品厂商ID/数量 /价格/包装类型ID/实体仓库ID/逻辑仓库ID/仓库库位ID/入出库理由
				//以上字段已经存在于deliverDetailMap中，原样使用即可
				//TODO:insert 【促销产品入出库明细表】  性能优化时可尝试不在循环中插入，而是在外层插入list			
				binolsscm02_service.insertPromotionStockDetail(deliverDetailMap);				
				//TODO:修改库存
				int cnt = binolsscm02_service.updatePromotionStock(deliverDetailMap);
				if(cnt<1){
					binolsscm02_service.insertPromotionStock(deliverDetailMap);
				}
			}		
		}
	} 
	
}
