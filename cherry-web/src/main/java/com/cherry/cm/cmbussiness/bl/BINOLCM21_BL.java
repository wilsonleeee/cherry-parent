
/*  
 * @(#)BINOLCM21_BL.java    1.0 2011-9-16     
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM21_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM20_Service;
import com.cherry.cm.cmbussiness.service.BINOLCM21_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;

public class BINOLCM21_BL implements BINOLCM21_IF {

	@Resource
	private BINOLCM21_Service binOLCM21_Service;
	
	@Resource
	private BINOLCM20_Service bINOLCM20_Service;
	
	@Resource(name="binOLCM02_BL")
	private BINOLCM02_BL binOLCM02_BL;
	
	@Resource
	private CodeTable code;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public String getCounterInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getCounterInfo(map);
		
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("counterCode"));
				sb.append("|");
				sb.append((String)tempMap.get("counterName"));
				
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}else if("codeName".equals(map.get("selected"))){
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				String codeName = "("+(String)tempMap.get("counterCode")+")"+(String)tempMap.get("counterName");
				sb.append(codeName);
				sb.append("|");
				sb.append((String)tempMap.get("counterName"));
				sb.append("|");
				sb.append((String)tempMap.get("counterCode"));
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}else{
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("counterName"));
				sb.append("|");
				sb.append((String)tempMap.get("counterCode"));
				
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String getProductInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
	    String businessDate = binOLCM21_Service.getBussinessDate(map);
	    map.put("businessDate", businessDate);
		List<Map<String,Object>> resultList = binOLCM21_Service.getProductInfo(map);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
//			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
			sb.append((String)tempMap.get("nameTotal"));
			sb.append("|");
			sb.append((String)tempMap.get("unitCode"));
			sb.append("|");
			sb.append((String)tempMap.get("barCode"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productVendorId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productId")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("salePrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("memPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("standardCost")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("orderPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("IsExchanged")));
            sb.append("|"); 
            sb.append("");// 置空，没有意思，只做占位使用。
            sb.append("|"); 
            sb.append("");// 置空，没有意思，只做占位使用。
            sb.append("|");
            sb.append("");// 置空，没有意思，只做占位使用。
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("platinumPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("tagPrice"))); // 吊牌价
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	
	@Override
	public String getProductInfoNZDM(Map<String, Object> map) {
		// TODO Auto-generated method stub
	    String businessDate = binOLCM21_Service.getBussinessDate(map);
	    map.put("businessDate", businessDate);
		List<Map<String,Object>> resultList = binOLCM21_Service.getProductInfo(map);
		
		List<Map<String, Object>> tempProductInfoList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> tempMap:resultList){
			tempProductInfoList.add(tempMap);

		}
		List<Map<String, Object>> inventoryList = new ArrayList<Map<String,Object>>();//库存集合List
		
		StringBuffer sb = new StringBuffer();
		
		if(resultList != null && !resultList.isEmpty()) {

			for(int i=0;i<tempProductInfoList.size();i++){
				Map<String, Object> tempMap = tempProductInfoList.get(i);
				if(CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){
					tempProductInfoList.remove(i--);
				}
			}
			

			if(!tempProductInfoList.isEmpty() && tempProductInfoList.size()>0){//去除ItemCode为空的产品以后的List
				String param = null;//调金蝶库存接口的参数
				for(int i=0;i<tempProductInfoList.size();i++){
					if(i==0){
						param = ConvertUtil.getString(tempProductInfoList.get(i).get("ItemCode")); 
					}else{
						param +=","+ConvertUtil.getString(tempProductInfoList.get(i).get("ItemCode"));

					}
				}
					
				if(!CherryChecker.isNullOrEmpty(param)){//参数不为空
					Map<String, Object> paramMap= new HashMap<String, Object>();
					paramMap.put("ItemCode", param);
					inventoryList= binOLCM02_BL.getInventoryByItemCode(paramMap);//返回的库存信息List
				}
			}
			
			for(Map<String, Object> tempMap:resultList){
				
				if(!inventoryList.isEmpty() && inventoryList.size()>0){//库存集合不为空的情况
					for(Map<String, Object> stockMap:inventoryList){//给每个产品设置库存
						int stockAmount=0;
						if(!CherryChecker.isNullOrEmpty(stockMap.get("IFProductId")) && !CherryChecker.isNullOrEmpty(tempMap.get("ItemCode"))){									
							if(ConvertUtil.getString(stockMap.get("IFProductId")).equals(ConvertUtil.getString(tempMap.get("ItemCode")))){
								if(!CherryChecker.isNullOrEmpty(stockMap.get("Quantity"))){
									stockAmount= ConvertUtil.getInt(stockMap.get("Quantity"));
								}
							}
						}				
						tempMap.put("stockAmount", stockAmount);
						// stockAmount不为零，表示获取到金蝶库存跳出循环
						if(0!=stockAmount){
							break;
						}
					}
				}else{//库存集合为空的情况
					tempMap.put("stockAmount", 0);
				}
			}
		}
		
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);

//			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
			sb.append((String)tempMap.get("nameTotal"));
			sb.append("|");
			sb.append((String)tempMap.get("unitCode"));
			sb.append("|");
			sb.append((String)tempMap.get("barCode"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productVendorId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productId")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("salePrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("memPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("standardCost")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("orderPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("IsExchanged")));
            sb.append("|"); 
            sb.append(String.valueOf(tempMap.get("distributionPrice")));
            sb.append("|"); 
            sb.append(String.valueOf(tempMap.get("stockAmount")));
            sb.append("|");
            sb.append("");// 置空，没有意思，只做占位使用。
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("platinumPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("tagPrice"))); // 吊牌价
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
//	@Override
//	public String getCntProductInfo(Map<String, Object> map) {
//		
//		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
//		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		// 产品方案添加产品模式 1:标准模式 2:颖通模式
//		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		// 是否小店云系统模式 1:是  0:否
//		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
////		isPosCloud = "1";
//		map.put("isPosCloud", isPosCloud);
//		// TODO Auto-generated method stub
//		String businessDate = binOLCM21_Service.getBussinessDate(map);
//		map.put("businessDate", businessDate);
//		
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		
//		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
//			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
//			map.put("cntPrtModeConf", cntPrtModeConf);
//			map.put("soluAddModeConf", soluAddModeConf);
//			resultList = binOLCM21_Service.getCntProductInfo(map);
//		}else{
//			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
//				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
//				resultList = binOLCM21_Service.getProductInfo(map);
//			}else{
//				// 检查柜台是否有分配可用的方案
//				resultList = binOLCM21_Service.chkCntSoluData(map);
//				if(CherryUtil.isBlankList(resultList)){
//					// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
//					resultList = binOLCM21_Service.getProductInfo(map);
//				}else{
//					// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
//					map.put("cntPrtModeConf", cntPrtModeConf);
//					map.put("soluAddModeConf", soluAddModeConf);
//					resultList = binOLCM21_Service.getCntProductInfo(map);
//				}
//				
//			}
//		}
//		
//		
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0 ; i < resultList.size() ; i++){
//			Map<String,Object> tempMap = resultList.get(i);
////			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
//			
//			// 如果是小店云系统模式，产品名称则使用方案中的方案产品名称
//			if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
//				sb.append((String)tempMap.get("soluProductName"));
//			}else if (CherryConstants.IS_POSCLOUD_0.equals(isPosCloud)){
//				sb.append((String)tempMap.get("nameTotal"));
//			}
//			sb.append("|");
//			sb.append((String)tempMap.get("unitCode"));
//			sb.append("|");
//			sb.append((String)tempMap.get("barCode"));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("productVendorId")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("productId")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("salePrice")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("memPrice")));
//			sb.append("|");
//            sb.append(String.valueOf(tempMap.get("standardCost")));
//            sb.append("|");
//            sb.append(String.valueOf(tempMap.get("IsExchanged")));
//            sb.append("|");
//            sb.append(isPosCloud);
//            sb.append("|");
//            sb.append(""); // 置空，没有意思，只做占位使用。
//            sb.append("|"); 
//            sb.append(""); // 置空，没有意思，只做占位使用。
//            sb.append("|");
//            sb.append(tempMap.get("platinumPrice"));
//            sb.append("|");
//            sb.append(tempMap.get("tagPrice")); // 吊牌价
//			if(i != resultList.size()){
//				sb.append("\n");
//			}
//		}
//		return sb.toString();
//	}
	
	@Override
	public String getCntProductInfo(Map<String, Object> map) {
		
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否开启产品特价 1:开启  0:关闭
		String isOpen = binOLCM14_BL.getConfigValue("1362", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		isPosCloud = "1";
		map.put("isPosCloud", isPosCloud);
		// TODO Auto-generated method stub
		String businessDate = binOLCM21_Service.getBussinessDate(map);
		map.put("businessDate", businessDate);
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
			map.put("cntPrtModeConf", cntPrtModeConf);
			map.put("soluAddModeConf", soluAddModeConf);
			resultList = binOLCM21_Service.getCntProductInfo(map);
		}else{
			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
				resultList = binOLCM21_Service.getProductInfo(map);
			}else{				
				if(isOpen.equals(CherryConstants.IS_POSCLOUD_1)){//表示开启产品特价
					// 检查柜台是否有分配可用的产品特价方案明细
					resultList=binOLCM21_Service.getPrtBySpeSoluDetail(map);
					if(CherryUtil.isBlankList(resultList)){
						// 检查柜台是否有分配可用的柜台产品方案
						resultList = binOLCM21_Service.chkCntSoluData(map);
						if(CherryUtil.isBlankList(resultList)){
							// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
							resultList = binOLCM21_Service.getProductInfo(map);
						}else{
							// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
							map.put("cntPrtModeConf", cntPrtModeConf);
							map.put("soluAddModeConf", soluAddModeConf);
							resultList = binOLCM21_Service.getCntProductInfo(map);
						}
					}else{
						resultList = binOLCM21_Service.getSpeProductInfo(map);
					}
				}else{
					// 检查柜台是否有分配可用的柜台产品方案
					resultList = binOLCM21_Service.chkCntSoluData(map);
					if(CherryUtil.isBlankList(resultList)){
						// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
						resultList = binOLCM21_Service.getProductInfo(map);
					}else{
						// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
						map.put("cntPrtModeConf", cntPrtModeConf);
						map.put("soluAddModeConf", soluAddModeConf);
						resultList = binOLCM21_Service.getCntProductInfo(map);
					}
					
				}
				
				
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
//			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
			
			// 如果是小店云系统模式，产品名称则使用方案中的方案产品名称
			if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
				sb.append((String)tempMap.get("soluProductName"));
			}else if (CherryConstants.IS_POSCLOUD_0.equals(isPosCloud)){
				sb.append((String)tempMap.get("nameTotal"));
			}
			sb.append("|");
			sb.append((String)tempMap.get("unitCode"));
			sb.append("|");
			sb.append((String)tempMap.get("barCode"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productVendorId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("salePrice")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("memPrice")));
			sb.append("|");
            sb.append(String.valueOf(tempMap.get("standardCost")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("IsExchanged")));
            sb.append("|");
            sb.append(isPosCloud);
            sb.append("|");
            sb.append(""); // 置空，没有意思，只做占位使用。
            sb.append("|"); 
            sb.append(""); // 置空，没有意思，只做占位使用。
            sb.append("|");
            sb.append(tempMap.get("platinumPrice"));
            sb.append("|");
            sb.append(tempMap.get("tagPrice")); // 吊牌价
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
    @Override
    public String getPrmProductInfo(Map<String, Object> map) {
        List<Map<String, Object>> resultList = binOLCM21_Service.getPrmProductInfo(map);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> tempMap = resultList.get(i);
            sb.append((String) tempMap.get("nameTotal"));
            sb.append("|");
            sb.append((String) tempMap.get("unitCode"));
            sb.append("|");
            sb.append((String) tempMap.get("barCode"));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("prmProductVendorId")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("prmProductId")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("standardCost")));
            if (i != resultList.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

	@Override
	public String getDepartInfo(Map<String, Object> map) {
		
		List<Map<String,Object>> resultList = binOLCM21_Service.getDepartInfo(map);
		
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("departCode"));
				sb.append("|");
				sb.append((String)tempMap.get("name"));
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}else{
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				sb.append((String)tempMap.get("departCode"));
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获得产品分类
	 */
	@Override
	public String getProductCategory(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getProductCategory(map);
		this.getString(map, resultList);
		return this.getString(map, resultList);
	}

	@Override
	public String getDeportInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getDeportInfo(map);
		
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("code"));
				sb.append("|");
				sb.append((String)tempMap.get("name"));
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}else{
			for(int i = 0 ; i < resultList.size() ; i++){
				Map<String,Object> tempMap = resultList.get(i);
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				sb.append((String)tempMap.get("code"));
				if(i != resultList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getEmployeeInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getEmployeeInfo(map);
		return this.getString(map, resultList);
	}
	
	@Override
	public String getBaInfo(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCM21_Service.getBaInfo(map);
		return this.getString(map, resultList);
	}
	
	@Override
	public String getResellerInfo(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCM21_Service.getResellerInfo(map);
		return this.getString(map, resultList);
	}
	
	@Override
	public String getSalesStaffInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getSalesStaffInfo(map);
		return this.getString(map, resultList);
	}

	@Override
	public String getBussinessPartnerInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getBussinessPartnerInfo(map);
		return this.getString(map, resultList);
	}
	
	@Override
	public String getRegionInfo(Map<String, Object> map) {
		
		List<Map<String,Object>> regionList = binOLCM21_Service.getRegionInfo(map);
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for(int i = 0 ; i < regionList.size() ; i++){
				Map<String,Object> tempMap = regionList.get(i);
				String code = (String)tempMap.get("code");
				if(code != null && !"".equals(code)) {
					sb.append(code);
				} else {
					sb.append("");
				}
				sb.append("|");
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if(i != regionList.size()){
					sb.append("\n");
				}
			}
		}else{
			for(int i = 0 ; i < regionList.size() ; i++){
				Map<String,Object> tempMap = regionList.get(i);
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				String code = (String)tempMap.get("code");
				if(code != null && !"".equals(code)) {
					sb.append(code);
				} else {
					sb.append("");
				}
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if(i != regionList.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	public String getString(Map<String,Object> map,List<Map<String,Object>> list){
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for(int i = 0 ; i < list.size() ; i++){
				Map<String,Object> tempMap = list.get(i);
				sb.append((String)tempMap.get("code"));
				sb.append("|");
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if(i != list.size()){
					sb.append("\n");
				}
			}
		}else{
			for(int i = 0 ; i < list.size() ; i++){
				Map<String,Object> tempMap = list.get(i);
				sb.append((String)tempMap.get("name"));
				sb.append("|");
				sb.append((String)tempMap.get("code"));
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if(i != list.size()){
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String getLogicInventoryInfo(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCM21_Service.getLogicInventoryInfo(map);
		return this.getString(map, resultList);
	}
	
	@Override
	public String getChannelInfo(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCM21_Service.getChannelInfo(map);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("nameTotal"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public String getIssueInfo(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLCM21_Service.getIssueInfo(map);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("id"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("nameTotal")));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getOrganizationDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLCM21_Service.getOrganizationDetail(map);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("departCode"));
			sb.append("|");
			sb.append((String)tempMap.get("name"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("contactPerson")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("address")));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getCounterDetail(Map<String, Object> map) {
		String conditionType = ConvertUtil.getString(map.get("conditionType"));
		String conditionValue = ConvertUtil.getString(map.get("conditionValue"));
		if("1".equals(conditionType)){
			map.put("channelId", conditionValue);
		}else if("2".equals(conditionType)){
			map.put("regionId", conditionValue);
		}
		List<Map<String,Object>> resultList = binOLCM21_Service.getCounterDetail(map);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("counterCode"));
			sb.append("|");
			sb.append((String)tempMap.get("counterName"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("counterId")));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 指定codeType及values模糊查询获取Code集合
	 * @param map
	 * @return
	 */
	public String getCodes(Map<String,Object> map){
		String codeType = ConvertUtil.getString(map.get("codeType"));
		String originalBrandStr = ConvertUtil.getString(map.get("originalBrandStr"));
		List<Map<String,Object>> resultList = code.getLikeCodes(codeType,originalBrandStr);
//		if(resultList.isEmpty()){
//			Map<String,Object> emptyMap = new HashMap<String, Object>();
//			emptyMap.put("codeKey", "");
//			emptyMap.put("value1", "");
//			resultList.add(emptyMap);
//		}
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("codeKey"));
			sb.append("|");
			sb.append((String)tempMap.get("value1"));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

//	@Override
//	public String getCntProductInfoAddSocket(String entitySocketId,Map<String, Object> map) throws Exception {
//		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
//		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		// 产品方案添加产品模式 1:标准模式 2:颖通模式
//		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
//		// 是否小店云系统模式 1:是  0:否
//		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
////		isPosCloud = "1";
//		map.put("isPosCloud", isPosCloud);
//		// TODO Auto-generated method stub
//		String businessDate = binOLCM21_Service.getBussinessDate(map);
//		map.put("businessDate", businessDate);
//		
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		
//		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
//			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
//			map.put("cntPrtModeConf", cntPrtModeConf);
//			map.put("soluAddModeConf", soluAddModeConf);
//			resultList = binOLCM21_Service.getCntProductInfo(map);
//			
//		}else{
//			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
//				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
//				resultList = binOLCM21_Service.getProductInfo(map);
//			}else{
//				// 检查柜台是否有分配可用的方案
//				resultList = binOLCM21_Service.chkCntSoluData(map);
//				if(CherryUtil.isBlankList(resultList)){
//					// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
//					resultList = binOLCM21_Service.getProductInfo(map);
//				}else{
//					// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
//					map.put("cntPrtModeConf", cntPrtModeConf);
//					map.put("soluAddModeConf", soluAddModeConf);
//					resultList = binOLCM21_Service.getCntProductInfo(map);
//				}
//				
//			}
//		}
//		
//		
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0 ; i < resultList.size() ; i++){
//			Map<String,Object> tempMap = resultList.get(i);
////			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
//			
//			// 如果是小店云系统模式，产品名称则使用方案中的方案产品名称
//			if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
//				sb.append((String)tempMap.get("soluProductName"));
//			}else if (CherryConstants.IS_POSCLOUD_0.equals(isPosCloud)){
//				sb.append((String)tempMap.get("nameTotal"));
//			}
//			sb.append("|");
//			sb.append((String)tempMap.get("unitCode"));
//			sb.append("|");
//			sb.append((String)tempMap.get("barCode"));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("productVendorId")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("productId")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("salePrice")));
//			sb.append("|");
//			sb.append(String.valueOf(tempMap.get("memPrice")));
//			sb.append("|");
//            sb.append(String.valueOf(tempMap.get("standardCost")));
//            sb.append("|");
//            sb.append(String.valueOf(tempMap.get("IsExchanged")));
//            sb.append("|");
//            sb.append(isPosCloud);
//            sb.append("|");
//            sb.append(getStock(entitySocketId, String.valueOf(tempMap.get("productVendorId"))));
//            sb.append("|");
//            sb.append(String.valueOf(tempMap.get("IsStock")));
//            sb.append("|");
//            sb.append(String.valueOf(tempMap.get("platinumPrice")));
//            sb.append("|");
//            sb.append(String.valueOf(tempMap.get("tagPrice"))); // 吊牌价
//			if(i != resultList.size()){
//				sb.append("\n");
//			}
//		}
//		return sb.toString();
//	}
	
	
	@Override
	public String getCntProductInfoAddSocket(String entitySocketId,Map<String, Object> map) throws Exception {
		// 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品
		String cntPrtModeConf = binOLCM14_BL.getConfigValue("1294", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 产品方案添加产品模式 1:标准模式 2:颖通模式
		String soluAddModeConf = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否小店云系统模式 1:是  0:否
		String isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 是否开启产品特价 1:开启  0:关闭
		String isOpen = binOLCM14_BL.getConfigValue("1362", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		//		isPosCloud = "1";
		map.put("isPosCloud", isPosCloud);
		// TODO Auto-generated method stub
		String businessDate = binOLCM21_Service.getBussinessDate(map);
		map.put("businessDate", businessDate);
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(isPosCloud.equals(CherryConstants.IS_POSCLOUD_1)){
			// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
			map.put("cntPrtModeConf", cntPrtModeConf);
			map.put("soluAddModeConf", soluAddModeConf);
			resultList = binOLCM21_Service.getCntProductInfo(map);
			
		}else{
			if(ProductConstants.CNT_PRT_MODE_CONIFG_3.equals(cntPrtModeConf)){
				// 柜台产品使用模式为“标准产品”时，则直接查询产品，与柜台无关
				resultList = binOLCM21_Service.getProductInfo(map);
			}else{				
				if(isOpen.equals(CherryConstants.IS_POSCLOUD_1)){//表示开启产品特价
					// 检查柜台是否有分配可用的产品特价方案明细
					resultList=binOLCM21_Service.getPrtBySpeSoluDetail(map);
					if(CherryUtil.isBlankList(resultList)){
						// 检查柜台是否有分配可用的柜台产品方案
						resultList = binOLCM21_Service.chkCntSoluData(map);
						if(CherryUtil.isBlankList(resultList)){
							// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
							resultList = binOLCM21_Service.getProductInfo(map);
						}else{
							// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
							map.put("cntPrtModeConf", cntPrtModeConf);
							map.put("soluAddModeConf", soluAddModeConf);
							resultList = binOLCM21_Service.getCntProductInfo(map);
						}
					}else{
						resultList = binOLCM21_Service.getSpeProductInfo(map);
					}
				}else{
					// 检查柜台是否有分配可用的柜台产品方案
					resultList = binOLCM21_Service.chkCntSoluData(map);
					if(CherryUtil.isBlankList(resultList)){
						// 柜台没有分配方案，或方案不在有效期及停用时，取得标准产品表数据
						resultList = binOLCM21_Service.getProductInfo(map);
					}else{
						// 柜台产品使用模式 “严格校验”、“补充校验” 时，通过SQL动态拼接查询。
						map.put("cntPrtModeConf", cntPrtModeConf);
						map.put("soluAddModeConf", soluAddModeConf);
						resultList = binOLCM21_Service.getCntProductInfo(map);
					}
					
				}
				
				
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
//			String str = String.valueOf(tempMap.get("unitCode"))+"**"+String.valueOf(tempMap.get("barCode"))+"**"+String.valueOf(tempMap.get("nameTotal"))+"**"+String.valueOf(tempMap.get("productVendorId"));
			
			// 如果是小店云系统模式，产品名称则使用方案中的方案产品名称
			if(CherryConstants.IS_POSCLOUD_1.equals(isPosCloud)){
				sb.append((String)tempMap.get("soluProductName"));
			}else if (CherryConstants.IS_POSCLOUD_0.equals(isPosCloud)){
				sb.append((String)tempMap.get("nameTotal"));
			}
			sb.append("|");
			sb.append((String)tempMap.get("unitCode"));
			sb.append("|");
			sb.append((String)tempMap.get("barCode"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productVendorId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("productId")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("salePrice")));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("memPrice")));
			sb.append("|");
            sb.append(String.valueOf(tempMap.get("standardCost")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("IsExchanged")));
            sb.append("|");
            sb.append(isPosCloud);
            sb.append("|");
            sb.append(getStock(entitySocketId, String.valueOf(tempMap.get("productVendorId"))));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("IsStock")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("platinumPrice")));
            sb.append("|");
            sb.append(String.valueOf(tempMap.get("tagPrice"))); // 吊牌价
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	private  Integer getStock(String entitySocketId,String productVendorId) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
        map.put("BIN_DepotInfoID", entitySocketId);//实体仓库ID
        map.put("BIN_ProductVendorID", productVendorId);//产品厂商ID
        Map<String,Object> quantityMap=bINOLCM20_Service.getProductStock(map);
        Integer totalQuantity=(Integer)quantityMap.get("TotalQuantity");
        return totalQuantity;
	}

	@Override
	public String getBaByCounter(Map<String, Object> map) throws Exception {		
		List<Map<String,Object>> resultList = binOLCM21_Service.getBaByCounter(map);
		
		StringBuffer sbf = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sbf.append((String)tempMap.get("baName"));
			sbf.append("|");
			sbf.append((String)tempMap.get("baCode"));
			if(i != resultList.size()){
				sbf.append("\n");
			}
		}
		return sbf.toString();
		
	}
}
