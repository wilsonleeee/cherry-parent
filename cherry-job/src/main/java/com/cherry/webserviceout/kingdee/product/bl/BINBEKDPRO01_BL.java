/*	
 * @(#)BINOTYIN01_BL.java     1.0 @2015-4-29
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
package com.cherry.webserviceout.kingdee.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchChecker;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;
import com.cherry.webserviceout.kingdee.WebServiceKingdee;
import com.cherry.webserviceout.kingdee.product.service.BINBEKDPRO01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 *
 * Kingdee接口：产品导入BL
 * 
 * WebService请求数据，并导入新后台
 * @author jijw
 *
 * @version  2015-4-29
 */
public class BINBEKDPRO01_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEKDPRO01_BL.class);	
	@Resource
	private BINBEKDPRO01_Service binbekdpro01_Service;
	
	/** Kingdee WebService共通接口 */
	@Resource(name="webServiceKingdee")
	private WebServiceKingdee webServiceKingdee;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 产品实时下发 */
	@Resource(name="binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 1000 */
//	private final int BTACH_SIZE = 1000;
	
	private Map<String, Object> comMap;
	
	/** 更新添加操作flag */
	private int optFlag = 0;
	
	/** 产品大分类ID */
	private int prtCatPropId_B = 0;
	/** 产品中分类ID */
	private int prtCatPropId_M = 0;
	/** 产品小分类ID */
	private int prtCatPropId_L = 0;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 导入失败的itemCode */
	private List<String> faildItemList = new ArrayList<String>();
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchkdpro01(Map<String, Object> map)
			throws CherryBatchException,Exception {
		
		// 初始化
		init(map);
		
		// 电商第三方接口取得获取产品列表对应的数据
		map.put("ESCode", "kingdee");
		map.put("tradeCode", "getKisProductList"); // Get_ICItems_info
		
		// Kingdee导入产品数据是最后修改时间
//		String FModifyTime = null;
		
		// 临时变量，可能会放到DB中去
//		map.put("aPPnum", "APP002258"); // 应用Code
//		map.put("AccountDB", "AIS20150506155849"); // 账套
		
		try{
			
			Map<String, Object> resultMap = null;
			try{
				
				// 将查询出的结果集转换成json，dataJson --业务端Json数据包，业务数据参数请参考具体业务API说明
				Map<String, Object> esMap = binbekdpro01_Service.getESInterfaceInfo(map);
		    	String extJson = ConvertUtil.getString(esMap.get("ExtJson"));
		    	Map<String, Object> extMap = (Map<String, Object>) JSONUtil.deserialize(extJson);
		    	map.put("ES_Method", extMap.get("Method"));
		    	
		    	// 条件中插入参数p_dataJson
		    	Map<String, Object> dataJsonMap = new HashMap<String, Object>();
		    	dataJsonMap.put("FModifyTime", extMap.get("FModifyTime"));
		    	String fModifyTimeLog = "【获取产品列表】调用WebService(Get_ICItems_info)的FModifyTime条件为: " + ConvertUtil.getString(extMap.get("FModifyTime"));
		    	fReasonBuffer.append(fModifyTimeLog).append("\n");
//		    	logger.outLog("【获取产品列表】调用WebService(Get_ICItems_info)的FModifyTime条件为: " + ConvertUtil.getString(extMap.get("FModifyTime")),CherryBatchConstants.LOGGER_INFO);
				String p_dataJsonStr = CherryUtil.map2Json(dataJsonMap);
				map.put("p_dataJson",p_dataJsonStr);
				
				// 调用KIS WebService接口获取数据
				resultMap = webServiceKingdee.accessServerResult(map);
				
			}catch(Exception e){
				flag = CherryBatchConstants.BATCH_ERROR;
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00059");
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("tradeCode")));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO, e);
				
				fReason = String.format("调用Kingdee Webservice（%1$s）失败。",ConvertUtil.getString(map.get("tradeCode")));
				
				throw e;
			}
			
			// kis接口方法名
			String kisMethod = ConvertUtil.getString(map.get("kisMethod")); 
			
			if(null != resultMap && !resultMap.isEmpty()){
				// WS返回结果代码
				String result = ConvertUtil.getString(resultMap.get("Result"));
        		if(WebServiceKingdee.Result_200.equals(result)){
        			
        			//  返回业务处理结果数据，详情参考具体API说明
	        		Map<String,Object> resultDataJsonMap = (Map<String, Object>) resultMap.get("DataJson");
	        		String dataJsonResult = ConvertUtil.getString(resultDataJsonMap.get("Result"));
	        		// 业务接口 返回结果代码
	        		if(WebServiceKingdee.Result_200.equals(dataJsonResult)){
	        			
	        			// 产品列表
	        			if (resultDataJsonMap.get("Data") instanceof List) {
	        				List<Map<String,Object>> productList = (List<Map<String, Object>>) resultDataJsonMap.get("Data"); 
	        				if(!CherryBatchUtil.isBlankList(productList)){
	        					// 取得kingdee系统最后修改产品的时间
	        					String FModifyTime = ConvertUtil.getString(productList.get(0).get("FModifyTime"));
	        					map.put("FModifyTime", FModifyTime);
	        					
	        					// 备份Kingdee取得的产品数据  
	        					List<Map<String,Object>> invalidProductList = bakKingDeeProductList(productList, map);
//	        				List<Map<String,Object>> invalidProductList = productList;
	        					
	        					// 导入到新后台
	        					if (!CherryBatchUtil.isBlankList(invalidProductList)) {
	        						totalCount += invalidProductList.size();
	        						// 更新新后台数据库产品
	        						updateBackEnd(invalidProductList,map);
	        					}
	        					
	        					// GC
	        					productList = null;
	        					invalidProductList = null;
	        				}
	        				
	        			}else{
	        				// 产品列表为null ？？？？ ******************待处理*****************
	        				Object productObj = resultDataJsonMap.get("Data");
	        				flag = CherryBatchConstants.BATCH_ERROR; 
	        				fReason = "WebService(获取产品列表)返回的DataJson.Data不符合要求: " + ConvertUtil.getString(productObj);
	        				logger.outLog(fReason,CherryBatchConstants.LOGGER_ERROR);
	        			}
	        			
	        		}else{
	        			// 数据返回异常 （非200）
	        			String errMsg = ConvertUtil.getString(resultDataJsonMap.get("ErrMsg"));
	        			
	        			fReason = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod,dataJsonResult,errMsg);
						logger.outLog(fReason, CherryBatchConstants.LOGGER_ERROR);
	        			flag = CherryBatchConstants.BATCH_ERROR;
	        			
	        		}
        		}else{
        			// 非200
        			String errMsg = ConvertUtil.getString(resultMap.get("ErrMsg"));
        			
    				fReason = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod,result,errMsg);
					logger.outLog(fReason, CherryBatchConstants.LOGGER_ERROR);
					flag = CherryBatchConstants.BATCH_ERROR;
					
        		}
	        		
			}else {
				// 异常处理  ******************待处理*****************
				// 共通里已打日志，此处不再处理
				flag = CherryBatchConstants.BATCH_ERROR;
				logger.outLog("调用WebService返回内容为空。",CherryBatchConstants.LOGGER_ERROR);
			}
			
			// GC
			resultMap = null;
			
		}catch(Exception e){
			flag = CherryBatchConstants.BATCH_ERROR;
			binbekdpro01_Service.manualRollback();
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EOT00060");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO, e);
			fReason = "产品导入(Kingdee)方法失败";
		}
		
		// 程序数据处理运行结果
		outMessage();
		// 程序结束处理
		programEnd(map);
		
		/* 此处调用出现问题，改为从FN、Action层调用
		// 调用产品实时下发
		try{
			Map<String,Object> issPrtMap = new HashMap<String, Object>();
			issPrtMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID));
			issPrtMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID));
			// Job运行履历表的运行方式
			issPrtMap.put("RunType", map.get("RunType"));
			// 是否实时发送MQ 1:是、 0或空：否
			issPrtMap.put("IsSendMQ", "1");
			
			binbeifpro04_BL.tran_batchProducts(issPrtMap);
		}catch(Exception e){
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EKD00016");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO, e);
		}
		*/
		
		return flag;
	}
	
	/**
	 * 更新新后台数据库产品
	 * @param itemList
	 */
	private void updateBackEnd(List<Map<String, Object>> itemList,Map<String,Object> map) {
		
		for (Map<String, Object> itemMap : itemList) {
			
			itemMap = CherryBatchUtil.removeEmptyVal(itemMap);
			// 数据字符截断转换等非SQL处理
			screenMap(itemMap);
			itemMap.putAll(comMap);
			
			try {
				String fDeleteD = CherryBatchUtil.getString(itemMap.get("FDeleteD")); // FDeleteD 1：停用 0：启用
				Boolean fIsStopPDAShopManage =  (Boolean)itemMap.get("FIsStopPDAShopManage") ; // 是否停用店务通产品： true 是、 false 否
				itemMap.put("FItemID", ConvertUtil.getString(itemMap.get("FItemID")));
				// ItemCode不能超过中英混合200
//				String fItemID = ConvertUtil.getString(itemMap.get("FItemID"));
//				if(CherryBatchUtil.isBlankString(fItemID) || CherryBatchUtil.mixStrLength(fItemID) > 64){
//					continue;
//				}
//				if(fItemID.equals("639")){
//					System.out.println("");
//				}
				int oldPrtId = binbekdpro01_Service.searchProductId(itemMap);
				
				// 导入来的产品是停用的,用以下方式处理
				if(fIsStopPDAShopManage){
//				if("1".equals(fDeleteD)){
					if(oldPrtId == 0){
						// 如果是新增的产品，且产品为无效，就不导入到新后台
						continue;
					}else{
						
						// 更新时，停用新后台产品  
						itemMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
						Map<String, Object> prtVendorMap = binbekdpro01_Service.getProductVendorInfo(itemMap);
						int proVendorId = (Integer)prtVendorMap.get("proVendorId");
						itemMap.put("prtVendorId", proVendorId);
						
						// 删除无效的产品数据
						binbekdpro01_Service.delInvalidProducts(itemMap);
						// 删除无效的产品厂商数据
						binbekdpro01_Service.delInvalidProVendors(itemMap);
						// 更新停用日时
						binbekdpro01_Service.updateClosingTime(itemMap);
						
						// 更新件数加一
						updateCount += 1;
						
						continue;
					}
				}
				itemMap.put("oldPrtId", oldPrtId);
				// 保存或更新产品信息
				saveOrUpdPro(itemMap);
				// 更新产品价格信息
				updPrtPrice(itemMap);
				// 更新产品条码信息
				updProVendor(itemMap);
				// 更新分类信息
				updPrtCategory(itemMap);
				
				binbekdpro01_Service.manualCommit();
				
			} catch (Exception e) {
				binbekdpro01_Service.manualRollback();
				// 记载导入新后台失败的FItemID
				faildItemList.add(CherryBatchUtil.getString(itemMap.get("FItemID")));
				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}
	
	/**
	 * 数据截断转换等非SQL处理
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap){
		
		// 接口表Status字段转换
		String fDeleteD = CherryBatchUtil.getString(itemMap.get("FDeleteD"));
		if("0".equals(fDeleteD)){
			itemMap.put("Status", "E"); 
			itemMap.put("validFlag", "1"); // 仅用于促销品
		}else if("1".equals(fDeleteD)){
			itemMap.put("Status", "D");
			itemMap.put("validFlag", "0"); // 仅用于促销品
		}
		
		// FSalePrice为null,设置为0
		String fSalePrice = ConvertUtil.getString(itemMap.get("FSalePrice"));
		if(CherryBatchUtil.isBlankString(fSalePrice)){
			itemMap.put("FSalePrice", 0);
		}
		
		// 分类为空的情况，设置为NA
		String FNumber1 = ConvertUtil.getString(itemMap.get("FNumber1"));
		if(CherryBatchUtil.isBlankString(FNumber1)){
			itemMap.put("FNumber1", "NA");
		}
		String FName1 = ConvertUtil.getString(itemMap.get("FName1"));
		if(CherryBatchUtil.isBlankString(FName1)){
			itemMap.put("FName1", "NA");
		}
		
		String FNumber2 = ConvertUtil.getString(itemMap.get("FNumber2"));
		if(CherryBatchUtil.isBlankString(FNumber2)){
			itemMap.put("FNumber2", "NA");
		}
		String FName2 = ConvertUtil.getString(itemMap.get("FName2"));
		if(CherryBatchUtil.isBlankString(FName2)){
			itemMap.put("FName2", "NA");
		}
		
		String FNumber3 = ConvertUtil.getString(itemMap.get("FNumber3"));
		if(CherryBatchUtil.isBlankString(FNumber3)){
			itemMap.put("FNumber3", "NA");
		}
		String FName3 = ConvertUtil.getString(itemMap.get("FName3"));
		if(CherryBatchUtil.isBlankString(FName3)){
			itemMap.put("FName3", "NA");
		}
		
		String FNumber4 = ConvertUtil.getString(itemMap.get("FNumber4"));
		if(CherryBatchUtil.isBlankString(FNumber4)){
			itemMap.put("FNumber4", "NA");
		}
		String FName4 = ConvertUtil.getString(itemMap.get("FName4"));
		if(CherryBatchUtil.isBlankString(FName4)){
			itemMap.put("FName4", "NA");
		}
		
		String FNumber5 = ConvertUtil.getString(itemMap.get("FNumber5"));
		if(CherryBatchUtil.isBlankString(FNumber5)){
			itemMap.put("FNumber5", "NA");
		}
		String FName5 = ConvertUtil.getString(itemMap.get("FName5"));
		if(CherryBatchUtil.isBlankString(FName5)){
			itemMap.put("FName5", "NA");
		}
		
	}
	
	/**
	 * 备份Kingdee导入的产品，并返回新后台可用的产品集合
	 * @return
	 */
	private List<Map<String,Object>> bakKingDeeProductList(List<Map<String,Object>> productList,Map<String,Object> paraMap){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> productMap : productList){
			
			productMap.put("businessDate", comMap.get("businessDate"));
			productMap.put(CherryBatchConstants.UPDATEPGM, comMap.get(CherryBatchConstants.UPDATEPGM));
			productMap.put(CherryConstants.CREATE_TIME, comMap.get(CherryConstants.CREATE_TIME));
			productMap.put("FIsStopPDAShopManageStr", ConvertUtil.getString(productMap.get("FIsStopPDAShopManage")));
			productMap.put("filterResult", null);
			
			// ItemCode不能超过中英混合200
			String fItemID = ConvertUtil.getString(productMap.get("FItemID"));
			if(CherryBatchUtil.isBlankString(fItemID) || CherryBatchUtil.mixStrLength(fItemID) > 64){
				productMap.put("filterResult", "FItemID超过了新后台ItemCode定义的nvarchar(32)限制。");
				continue;
			}
			
//			if(fItemID.equals("639")){
//				productMap.put("FDeleteD", "1");
//			}
			
			// UnitCode不能超过20
			String fNumber = ConvertUtil.getString(productMap.get("FNumber"));
			if(CherryBatchUtil.isBlankString(fNumber) || CherryBatchUtil.mixStrLength(fNumber) > 20){
				if(CherryBatchUtil.isBlankString(fNumber)){
					productMap.put("filterResult", "FNumber为空，不符合新后台UnitCode的定义。");
				}
				if(CherryBatchUtil.mixStrLength(fNumber) > 20){
					productMap.put("filterResult", "FNumber超过了新后台UnitCode定义的20位长度限制。");
				}
				continue;
			}else if(!CherryChecker.isProCode(fNumber)){
				productMap.put("filterResult", "FNumber不符合新后台UnitCode的数据类型规范。");
				continue;
			}
			
			// BarCode不能超过13
			String fBarCode = ConvertUtil.getString(productMap.get("FBarCode"));
			if(CherryBatchUtil.isBlankString(fBarCode)){
				if(CherryBatchUtil.mixStrLength(fNumber) > 13){
					// 如果 barcode为空，截取右13位
					productMap.put("FBarCode", fNumber.substring(fNumber.length() - 13 ,fNumber.length()));
				}else{
					productMap.put("FBarCode", fNumber);
				}
			}else if( CherryBatchUtil.mixStrLength(fBarCode) > 13){
					productMap.put("filterResult", "FBarCode超过了新后台Barcode定义的13位长度限制。");
				continue;
			}else if(!CherryChecker.isProCode(fBarCode)){
				productMap.put("filterResult", "FBarCode不符合新后台Barcode的数据类型规范。");
				continue;
			}
			
			// FName不能超过20
			String fname = ConvertUtil.getString(productMap.get("FName"));
			if(CherryBatchUtil.isBlankString(fname) || CherryBatchUtil.mixStrLength(fname) > 200){
				if(CherryBatchUtil.isBlankString(fname)){
					productMap.put("filterResult", "FName为空，不符合新后台NameTotal的定义。");
				}
				if(CherryBatchUtil.mixStrLength(fname) > 200){
					productMap.put("filterResult", "FName超过了新后台NameTotal定义的nvarchar(100)限制。");
				}
				continue;
			}
			
			// FSalePrice不能为null
//			String fSalePrice = ConvertUtil.getString(productMap.get("FSalePrice"));
//			if(CherryBatchUtil.isBlankString(fSalePrice)){
//				productMap.put("filterResult", "FSalePrice超过了新后台NameTotal定义的不能为空。");
//				continue;
//			}
			
			// FModel长度不大于30
			String fModel = ConvertUtil.getString(productMap.get("FModel"));
			if(!CherryBatchUtil.isBlankString(fModel) && CherryBatchUtil.mixStrLength(fModel) > 180){
				productMap.put("filterResult", "FModel超过了新后台Spec定义的nvarchar(90)限制。");
				continue;
			}
			resultList.add(productMap);
		}
		// 备份
		binbekdpro01_Service.backProductImpKingdee(productList);
		
		return resultList;
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdPro(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 查询产品ID
		int productId = 0;
//		int oldPrtId = binbekdpro01_Service.searchProductId(itemMap);
		int oldPrtId = (Integer)itemMap.get("oldPrtId");
		
		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		
		try {
			prtMap.put("unitCode", itemMap.get("FNumber"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FItemID")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FNumber")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FBarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FName")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ************************** 产品编码唯一性验证 **************************
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		try{
			// 查询促销品中是否存在当前需要添加的barCode
			prtMap.put("barCode", itemMap.get("FBarCode"));
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if(promPrtIdList.size() != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00055");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FItemID")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FNumber")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FBarCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("FName")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 插入产品信息
				productId = binbekdpro01_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00013");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("FItemID")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("FName")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		} else {
			// 更新操作
			optFlag = 2;
			productId = oldPrtId;
			itemMap.put(ProductConstants.PRODUCT_ID, productId);
			try {
				// 更新产品信息表
				binbekdpro01_Service.updateProductInfo(itemMap);
				
				// 产品变动后更新产品方案明细表的version字段
				binbeifpro01Service.updPrtSolutionDetail(itemMap);
				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00014");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("FItemID")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("FName")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 更新产品价格信息
	 * 
	 * @param map
	 * @throws CherryBatchException
	 */
	private void updPrtPrice(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		
		if (!CherryBatchChecker.isNull(tempMap.get("FSalePrice"))) {
			try {
					
				// 新产品
				if(1 == optFlag){
					// 默认产品价格生效日期
					tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
					// 默认产品价格失效日期
					tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
					// 插入新的产品价格信息
					binbekdpro01_Service.insertProductPrice(tempMap);
					
				} 
				// 老产品
				else {
					// 查询产品价格是否修改
					String productPriceID = binbekdpro01_Service.selProductPrice(tempMap);
					
					// 产品价格已修改
					if(null == productPriceID){
						// 默认产品价格生效日期
						tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
						binbekdpro01_Service.updProductPrice(tempMap);
					}
				}
				
			} catch (Exception e) {
				if (optFlag == 1) {
					insertCount--;
				} else if (optFlag == 2) {
					updateCount--;
				}
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00015");
				batchExceptionDTO
						.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(tempMap.get("FItemID")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(tempMap.get("FName")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 更新产品条码信息
	 * 
	 * @param itemMap
	 * @throws CherryBatchException
	 */
	private void updProVendor(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		
		try {
			
			if(1 == optFlag){
				// 添加产品厂商信息
				binbekdpro01_Service.insertProductVendor(tempMap);
			} 
			else if(2 == optFlag){
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binbekdpro01_Service.getProductVendorInfo(tempMap);
				int proVendorId = (Integer)prtVendorMap.get("proVendorId");
				tempMap.put("prtVendorId", proVendorId);
				binbekdpro01_Service.updPrtVendor(tempMap);
				// 更新产品条码对应关系信息
				binbekdpro01_Service.updPrtBarCode(tempMap);
			}
			
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00016");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(tempMap.get("FItemID")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("FNumber")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap
					.get("FBarCode")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 更新产品分类
	 * 
	 * @param itemMap
	 */
	private void updPrtCategory(Map<String, Object> itemMap)
			throws CherryBatchException {
		
		// 删除产品分类属性关系信息
		binbekdpro01_Service.delPrtCategory(itemMap);
		// 添加产品分类关系(大分类)
		String fnumber1 = ConvertUtil.getString(itemMap.get("FNumber1"));
		String fName1 = ConvertUtil.getString(itemMap.get("FName1"));
		if(!CherryBatchUtil.isBlankString(fnumber1) && !CherryBatchUtil.isBlankString(fName1) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);
		}
		
		// 添加产品分类关系(中分类)
		String fnumber2 = ConvertUtil.getString(itemMap.get("FNumber2"));
		String fName2 = ConvertUtil.getString(itemMap.get("FName2"));
		if(!CherryBatchUtil.isBlankString(fnumber2) && !CherryBatchUtil.isBlankString(fName2) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);
		}
		
		// 添加产品分类关系(小分类)
		String fnumber3 = ConvertUtil.getString(itemMap.get("FNumber3"));
		String fName3 = ConvertUtil.getString(itemMap.get("FName3"));
		if(!CherryBatchUtil.isBlankString(fnumber3) && !CherryBatchUtil.isBlankString(fName3) ){
			addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);
		}
		
		/*
		// 添加产品动态分类关系D1（U_SubCategory）
		addPrtcategoryDynaCate(itemMap, "D1");
		// 添加产品动态分类关系D2（U_LineMF）
		addPrtcategoryDynaCate(itemMap, "D2");
		// 添加产品动态分类关系D3（U_LineCategory）
		addPrtcategoryDynaCate(itemMap, "D3");
		*/
		
	}
	
	/**
	 * 添加产品分类关系
	 * 
	 * @param itemMap
	 * @param cateType
	 */
	private void addPrtcategory(Map<String, Object> itemMap, String cateType)
			throws CherryBatchException {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(comMap);
		// 产品ID
		temp.put(ProductConstants.PRODUCT_ID,itemMap.get(ProductConstants.PRODUCT_ID));
		// 分类类型
		temp.put(ProductConstants.CATE_TYPE, cateType);
		// 大分类
		if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
			// 大分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
			// 大分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("FNumber1"));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("FName1"));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("FNumber2"));
			// 中分类属性名称q
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("FName2"));
		} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("FNumber3"));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("FName3"));
		}
		try {
			// 取得分类属性值ID
			int catePropValId = getCatPropValId(temp);
			if (catePropValId != 0) {
				temp.put(ProductConstants.CATPROPVALID, catePropValId);
				// 插入产品分类关系表
				binbekdpro01_Service.insertPrtCategory(temp);
			}
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00017");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(itemMap.get("FItemID")));
			// 分类属性名称
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(temp
					.get(ProductConstants.PROPVALUE_CN)));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 取得分类属性值ID
	 * 
	 * @param Map
	 * @return
	 */
	private int getCatPropValId(Map<String, Object> map) throws Exception {
		// 分类属性值ID
		int catPropValId = 0;
		// 分类属性值
		String propValueCherry = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUECHERRY));
		
		// 分类属性值名称
		String propValueCN = CherryBatchUtil.getString(map.get(ProductConstants.PROPVALUE_CN));
		
		// 分类属性值,名不为空
		if (!CherryBatchConstants.BLANK.equals(propValueCN) && !CherryBatchConstants.BLANK.equals(propValueCherry)) {
			// 根据属性【值】查询分类属性值ID
			catPropValId = binbekdpro01_Service.getCatPropValId1(map);
			if (catPropValId == 0) {
				
				// 如果接口编码大于4位，则将编码存入PropValueCherry，再随机生成4位唯一的编码存入PropValue;
				if(propValueCherry.length() <= 4){
					// 判断propValueCherry属性值在表中的propValue字段是否已经存在，若不存在，则使用propValueCherry，存在则随机生成4位
					Map<String,Object> tempMap = new HashMap<String, Object>();
					tempMap.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
					tempMap.put(ProductConstants.PROPVALUE, propValueCherry);
					int propValIdByPV = binbekdpro01_Service.getCatPropValId1(tempMap);
					if(0 == propValIdByPV){
						// 分类属性值【终端用】
						map.put(ProductConstants.PROPVALUE, propValueCherry);
					} else {
						// 分类属性值4位【终端用】
						String propValue = getPropValue(map,ProductConstants.PROPVALUE);
						map.put(ProductConstants.PROPVALUE,propValue);
					}
					
				} else {
					// 分类属性值4位【终端用】
					String propValue = getPropValue(map,ProductConstants.PROPVALUE);
					map.put(ProductConstants.PROPVALUE,propValue);
				}
				
				// 添加分类属性值
				catPropValId = binbekdpro01_Service.addPropVal(map);
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				binbekdpro01_Service.updPropVal(map);
			}
		}
		return catPropValId;
	}
	
	/**
	 * 生成品牌下不重复的分类属性值【4位随机】
	 * 
	 * @param map
	 * @return
	 */
	private String getPropValue(Map<String, Object> map,String key) {
		// 添加产品分类选项值
		Map<String, Object> temp = new HashMap<String, Object>();
		// 分类类别ID
		temp.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
		while (true) {
			// 随机产生4位的字符串
			String randomStr = CherryUtil.getRandomStr(ProductConstants.CATE_LENGTH);
			temp.put(key, randomStr);
			// 取得分类属性值ID
			int propValId = binbekdpro01_Service.getCatPropValId1(temp);
			// 随机产生的4位字符串不重复
			if (propValId == 0) {
				return randomStr;
			}
		}
	}
	
	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map){
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT104");
		
		// 程序【开始运行时间】
		String runStartTime = binbekdpro01_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);
		
		// 系统时间
//		String sysDate = binbekdpro01_Service.getSYSDate();
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		
		// 业务日期
		String bussDate = binbekdpro01_Service.getBussinessDate(map);
		comMap.put("businessDate", bussDate);
		map.put("businessDate", bussDate);
		
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		comMap.put("nextBussDate", nextBussDate);
		
		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		
		// 取得大中小分类对应的分类Id
		getCatePropIds(comMap);
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		paraMap.putAll(comMap);
		
		// 更新电商接口表kingdee获取产品接口的extJson中的FModifyTime   
		String FModifyTime = ConvertUtil.getString(paraMap.get("FModifyTime"));
		if(!CherryBatchUtil.isBlankString(FModifyTime)){
			// {"Method":"Get_ICItems_info","FModifyTime":""}
			Map<String, Object> extMap = new HashMap<String, Object>();
			extMap.put("Method", paraMap.get("ES_Method"));
			extMap.put("FModifyTime", paraMap.get("FModifyTime"));
			String extJsonStr = CherryUtil.map2Json(extMap);
			paraMap.put("ExtJson", extJsonStr);
			binbekdpro01_Service.updateESInterfaceInfo(paraMap);
		}
		
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEKDPRO01");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEKDPRO01");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		return baseMap;
	}
	
	/**
	 * 取得大中小分类对应的分类Id
	 */
	private void getCatePropIds(Map<String, Object> paramsMap){
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.putAll(paramsMap);
		// 大分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_B);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_1);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_B = binbekdpro01_Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binbekdpro01_Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binbekdpro01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binbekdpro01_Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binbekdpro01_Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binbekdpro01_Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binbekdpro01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binbekdpro01_Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binbekdpro01_Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binbekdpro01_Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binbekdpro01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binbekdpro01_Service.updProp(temp);
			}
		}
		
		/*
		
		// 动态分类:U_SubCategory
		temp.put(ProductConstants.PROP_PROPERTY, "U_SubCategory");
		temp.put(ProductConstants.PROPNAMECN, "U_SubCategory");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D1 = binbekdpro01_Service.getCatPropId3(temp);
		if (prtCatPropId_D1 == 0) {
			// 添加动态分类:U_SubCategory
			prtCatPropId_D1 = binbekdpro01_Service.addCatProperty(temp);
		}
		
		// 动态分类:U_LineMF
		temp.put(ProductConstants.PROP_PROPERTY, "U_LineMF");
		temp.put(ProductConstants.PROPNAMECN, "U_LineMF");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D2 = binbekdpro01_Service.getCatPropId3(temp);
		if (prtCatPropId_D2 == 0) {
			// 添加动态分类:U_LineMF
			prtCatPropId_D2 = binbekdpro01_Service.addCatProperty(temp);
		}
		
		// 动态分类:U_LineCategory
		temp.put(ProductConstants.PROP_PROPERTY, "U_LineCategory");
		temp.put(ProductConstants.PROPNAMECN, "U_LineCategory");
		temp.put(ProductConstants.TEMINALFLAG, "0");
		prtCatPropId_D3 = binbekdpro01_Service.getCatPropId3(temp);
		if (prtCatPropId_D3 == 0) {
			// 添加动态分类:U_LineCategory
			prtCatPropId_D3 = binbekdpro01_Service.addCatProperty(temp);
		}
		
		*/
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
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		
		// 失败ItemCode集合
		if(!CherryBatchUtil.isBlankList(faildItemList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00021");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "产品导入部分数据处理失败，具体见log日志";
		}
		
	}
	

}
