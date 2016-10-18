/*  
 * @(#)BINOLPTJCS19_BL.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS19_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS16_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS19_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS34_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS36_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


public class BINOLPTJCS19_BL extends SsBaseBussinessLogic implements BINOLPTJCS19_IF{
	@Resource
	private BINOLPTJCS19_Service binOLPTJCS19_Service;
	
	@Resource(name="binOLPTJCS16_Service")
	private BINOLPTJCS16_Service binOLPTJCS16_Service;
	
	@Resource(name="binOLPTJCS36_Service")
	private BINOLPTJCS36_Service binOLPTJCS36_Service;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Override
	public Map<String, Object> getSolutionInfo(Map<String, Object> map) {
		return binOLPTJCS19_Service.getSolutionInfo(map);
	}
	
	/**
	 * 取得方案明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getSolutionInfoDetailCount(Map<String, Object> map){
		return binOLPTJCS19_Service.getSolutionInfoDetailCount(map);
	}
	
	/**
	 * 取得方案明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getSolutionInfoDetailList(Map<String, Object> map){
		return binOLPTJCS19_Service.getSolutionInfoDetailList(map);
	}
	
	/**
	 * 保存添加的方案产品明细
	 * 
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_addPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS18_Form form) throws JSONException,Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 方案ID
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
        // 产品id
        String[] prtIdArr = form.getPrtIdArr();
        // 方案销售价格
        String[] salePriceArr = form.getSalePriceArr();
        // 方案会员价格
        String[] memPriceArr =form.getMemPriceArr();
        // 方案产品名称
        String[] soluProductNameArr =form.getSoluProductNameArr();
        
        // 销售最高价
        String[] minSalePriceArr = form.getMinSalePriceArr();
        // 销售最低价
        String[] maxSalePriceArr = form.getMaxSalePriceArr();
       
        map.put("soluDetailValidFlag",CherryConstants.VALIDFLAG_ENABLE);
        if(null != prtIdArr){
        	for(int i=0;i<prtIdArr.length;i++){
        		// 拼接priceJson
        		map.put("salePrice", salePriceArr[i]);
        		map.put("memPrice", memPriceArr[i]);
        		if(!CherryChecker.isNullOrEmpty(soluProductNameArr)){
        			map.put("soluProductName", soluProductNameArr[i]);
        		}
        		// 销售最低价
        		if(!CherryChecker.isNullOrEmpty(minSalePriceArr)){
        			map.put("minSalePrice", minSalePriceArr[i]);
        		}
        		// 销售最高价
        		if(!CherryChecker.isNullOrEmpty(maxSalePriceArr)){
        			map.put("maxSalePrice", maxSalePriceArr[i]);
        		}
        		
        		// 是否小店云系统模式 1:是  0:否
        		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
        		if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
        			String productID = prtIdArr[i];
        			map.put("productId", productID);
        			// 取得分类值List
        			List<Map<String, Object>> cateValList = binOLPTJCS36_Service.getCateList(map);
        			
        			for(Map<String, Object> cateVal : cateValList){
        				String teminalFlag = ConvertUtil.getString(cateVal.get("teminalFlag"));
        				if(teminalFlag.equals(ProductConstants.TEMINALFLAG_1)){
        					// 大分类属性值ID
        					map.put("prtCatePropValueBigID", cateVal.get("propValId"));
        				}else if (teminalFlag.equals(ProductConstants.TEMINALFLAG_3)){
        					// 中分类属性值ID
        					map.put("prtCatePropValueMediumID", cateVal.get("propValId"));
        				}else if (teminalFlag.equals(ProductConstants.TEMINALFLAG_2)){
        					// 小分类属性值ID
        					map.put("prtCatePropValueSmallID", cateVal.get("propValId"));
        				}
        			}
        		}
        		
//			Map<String, Object> priceMap = new HashMap<String, Object>();
//			priceMap.put("memPrice", memPriceArr[i]);
//			priceMap.put("salePrice", salePriceArr[i]);
//			List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
//			prtList.add(priceMap);
//			String priceJson = JSONUtil.serialize(prtList);
//			map.put("priceJson", priceJson);
        		map.put("productID", prtIdArr[i]);
        		
        		binOLPTJCS16_Service.mergePrtPriceSoluDetail(map);
        	}
        }
		
		return null;
	}
	
	
	/**
	 * 保存编辑的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_editPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS18_Form form)  throws JSONException,Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		map.put("productPriceSolutionDetailID", form.getProductPriceSolutionDetailID());
		map.put("productID", form.getProductID());
		
		// 拼接priceJson
//		Map<String, Object> priceMap = new HashMap<String, Object>();
//		priceMap.put("salePrice", form.getSalePrice());
		map.put("salePrice", form.getSalePrice());
//		priceMap.put("memPrice", form.getMemPrice());
		map.put("memPrice", form.getMemPrice());
		if(!CherryChecker.isNullOrEmpty(form.getSoluProductName())){
			map.put("soluProductName", form.getSoluProductName().trim());
		}
			
//		List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
//		prtList.add(priceMap);
//		String priceJson = JSONUtil.serialize(prtList);
//		map.put("priceJson", priceJson);
//		map.put("salePrice", form.getSalePrice());
//		map.put("memPrice", form.getMemPrice());
		
		binOLPTJCS16_Service.updPrtPriceSoluDetail(map);
		return null;
	}
	
	/**
	 * 批量无效方案产品明细
	 * @param map
	 * @throws Exception
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public void tran_delPrtPriceSoluDetail(Map<String, Object> map) throws Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		String validFlag = ConvertUtil.getString(map.get("validFlag"));
		
		map.put("soluDetailValidFlag",validFlag.equals(CherryConstants.VALIDFLAG_ENABLE) ? "1" : CherryConstants.VALIDFLAG_DISABLE);
		
		// 产品
		Object productInfoIds = (Object)map.get("productInfoIds");
		if(productInfoIds instanceof String[]){
			
			String [] prtIDsList = (String[]) productInfoIds;
			
			if(prtIDsList.length != 0){
				for(String prtIDs : prtIDsList){
					map.put("productID", prtIDs);
					binOLPTJCS16_Service.mergePrtPriceSoluDetail(map);
				}
			}
		} else if(productInfoIds instanceof String){
			
			String [] prtIDsList =  String.valueOf(productInfoIds).split(",");
			
			if(prtIDsList.length != 0){
				for(String prtIDs : prtIDsList){
					map.put("productID", prtIDs);
					binOLPTJCS16_Service.mergePrtPriceSoluDetail(map);
				}
			}
		}
	}
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	
	/**
	 * 产品方案明细添加产品分类
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_addPrtPriceSoluCateDetail(Map<String, Object> map,BINOLPTJCS18_Form form)  throws JSONException,Exception{
		
		String businessDate = binOLPTJCS19_Service.getBusDate(map);
		map.put("businessDate",businessDate);
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 方案ID
		map.put("productPriceSolutionID", form.getProductPriceSolutionID());
		
		
		// 删除产品方案绑定的产品分类
		binOLPTJCS19_Service.delPrtSoluDetailCate(map);
		
		// 添加产品方案画面当前所有的产品分类
		String cateInfo = form.getCateInfo();
		if (!CherryConstants.BLANK.equals(cateInfo)) {
			// 产品分类信息List
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(cateInfo);
			if (null != cateInfoList) {
				for (Map<String, Object> cate : cateInfoList) {
					if (!CherryChecker.isNull(cate.get(ProductConstants.PROPVALID))) {
						cate.putAll(map);
						// 插入方案明细添加产品分类 
//						binOLPTJCS03_Service.insertPrtCategory(cate);
						List<String> propValArrList = (List<String>) cate.get("propValArr");
						if(!CherryUtil.isBlankList(propValArrList)){
							for(String propVal : propValArrList){
								cate.put("propValId", propVal);
								binOLPTJCS19_Service.insertCate(cate);
							}
						}
					}
				}
			}
		}
		
		List<Map<String, Object>> prtForPrtSoluDetailDiff = binOLPTJCS19_Service.getPrtForPrtSoluDetailDiff(map);
		if (!CherryUtil.isBlankList(prtForPrtSoluDetailDiff)) {
			for(Map<String, Object> diffMap : prtForPrtSoluDetailDiff){
				// 将差异更新到产品方案明细表
				diffMap.putAll(map);
				String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
				
				// 取得当前方案及增加的产品,merge到产品方案明细表 validFlag = 1,version = tversion +1,isCate =1
				if("add".equals(modifyFlag)){
					diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
					diffMap.put("productId", diffMap.get("prtPD"));
					// 1: 插入产品方案明细表
					binOLPTJCS19_Service.mergeProductPriceSolutionDetail(diffMap);
					
				}
				// 取得当前方案明细减少的产品,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
				else if ("sub".equals(modifyFlag)){
					
					diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_DISABLE);
					diffMap.put("productId", diffMap.get("prtPDH"));
					// 1: 将方案明细表的产品数据无效掉
					binOLPTJCS19_Service.updPrtSoluDetail(diffMap);
				}
			}
			
		}
		
		
		return null;
		
	}
	
	/**
	 * 获取产品分类List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getCateList(Map<String, Object> map) {

		return binOLPTJCS19_Service.getCateList(map);
	}
}
