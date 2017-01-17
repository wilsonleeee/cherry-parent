/*	
 * @(#)BINBAT122_BL.java     1.0 @2015-10-13		
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
package com.cherry.middledbout.stand.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchChecker;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.middledbout.stand.product.service.BINBAT122_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * 产品导入(标准接口)(IF_Product)BL
 * @author jijw
 *  * @version 1.0 2015.10.13
 *  updated by hsq
 *
 */
public class BINBAT122_BL {
	/**
	 * 打印当前类的日志信息
	 */
	private static Logger loger = LoggerFactory.getLogger(BINBAT122_BL.class.getName());
	/** 每批次(页)处理数量 1000 */
	private final int BATCH_SIZE = 1000;
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	@Resource
	private BINBAT122_Service binBAT122_Service;
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
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
	 * @param
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchExec(Map<String, Object> paraMap) throws CherryBatchException,Exception {
		// 初始化(包括取得大中小类)
		try{
			init(paraMap);
		}catch(Exception e){
			loger.error("初始化出错！",e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new Exception(e.getMessage(),e);
		}
		try {
			// 处理符合条件的产品信息数据
			handleNormalData(paraMap);
		} catch (Exception e) {
			loger.error("处理失败！",e);
			throw e;
		}finally {
			// 日志
			outMessage();
			// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
			programEnd(paraMap);
		}
		return flag;
	}

	/**
	 * 处理符合条件的产品信息数据
	 * @param paraMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException, Exception {
		try {
			//首先需要将接口表中SynchFlag为NULL的改为1
			binBAT122_Service.updateIFSynchFlagFromNullToOne(paraMap);
			binBAT122_Service.tpifManualCommit();
		} catch (Exception e) {
			loger.error("更新标准接口表产品数据从[synchFlag=null]更新为[synchFlag=1]失败", e);
			throw e;
		}
		while (true) {
			// 查询接口产品列表
			Map<String, Object> paraMap2 = new HashMap<String, Object>();
			paraMap2.putAll(paraMap);
			paraMap2.put("batchSize", BATCH_SIZE);
			List<Map<String, Object>> itemList = binBAT122_Service.getStandardProductList(paraMap2);
			if (CherryBatchUtil.isBlankList(itemList)) {
				break;
			}
			 // 统计总条数
			 totalCount += itemList.size();
			 // 更新新后台数据库产品相关表
			 updateBackEnd(itemList,paraMap);
			 // 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
			 if (itemList.size() < BATCH_SIZE) {
				 break;
			 }
		}
	}

	/**
	 * 
	 * @param itemList 产品集合
	 * @param map 参数
	 * @throws Exception
	 */
	private void updateBackEnd(List<Map<String, Object>> itemList,Map<String,Object> map) throws Exception {
		Map<String,Object> paraMap = new HashMap<String, Object>();
		//同步出错的原因
		String synchMsg = "";
		for (Map<String, Object> itemMap : itemList) {
			itemMap = CherryBatchUtil.removeEmptyVal(itemMap);
			// 数据字符截断转换等非SQL处理
			screenMap(itemMap);
			itemMap.putAll(comMap);
			paraMap.putAll(itemMap);
			try {
				// 接口中的产品有效区分
				String validFlag = CherryBatchUtil.getString(itemMap.get("ValidFlag")); // ValidFlag 1：启用 0：停用
				int oldPrtId = binBAT122_Service.searchProductId(itemMap);
				// 导入来的产品是停用的,用以下方式处理
				if("0".equals(validFlag)){
					if(oldPrtId == 0){
						// 如果是新增的产品，且产品为无效，就不导入到新后台，将这个记录的状态更新为2
						paraMap.put("synchFlag","2");
						paraMap.put("synchMsg","该产品在新后台中不存在");
						binBAT122_Service.updateIFSynchFlagFromOneToAnother(paraMap);
						binBAT122_Service.tpifManualCommit();
						continue;
					}
					// 更新时，停用新后台产品
					itemMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
					//存在一码多品的情况
					List<Map<String, Object>> prtVendorList = binBAT122_Service.getProductVendorInfo(itemMap);
					if(prtVendorList != null && !prtVendorList.isEmpty()){
						if(prtVendorList.size() > 1){
							loger.error("产品ID为"+oldPrtId+"的产品下存在多个条码！");
							synchMsg = "该产品下存在多个条码！";
							throw new Exception();
						}
						Map<String,Object> prtVendorMap = prtVendorList.get(0);
						//如果存在产品产商的数据的话，更改产品产商和产品编码条码对应关系表
						int proVendorId = ConvertUtil.getInt(prtVendorMap.get("proVendorId"));
						itemMap.put("prtVendorId", proVendorId);
						// 删除无效的产品厂商数据
						binBAT122_Service.delInvalidProVendors(itemMap);
						// 更新停用日时
						binBAT122_Service.updateClosingTime(itemMap);
						//这里没有更新产品价格表,不需要
					}
					// 删除无效的产品数据
					binBAT122_Service.delInvalidProducts(itemMap);
					// 更新件数加一
					updateCount += 1;
					//将这个记录的状态更新为2
					paraMap.put("synchFlag","2");
					binBAT122_Service.updateIFSynchFlagFromOneToAnother(paraMap);
					continue;
				}
				//如果是1的话
				itemMap.put("oldPrtId", oldPrtId);
				// 保存或更新产品信息
				saveOrUpdPro(itemMap);
				// 更新产品价格信息
				updPrtPrice(itemMap);
				// 更新产品条码信息
				updProVendor(itemMap);
				// 更新分类信息
				updPrtCategory(itemMap);
				//将这个记录的状态更新为2
				paraMap.put("synchFlag","2");
				binBAT122_Service.updateIFSynchFlagFromOneToAnother(paraMap);
				binBAT122_Service.manualCommit();
				binBAT122_Service.tpifManualCommit();
			} catch (Exception e) {
				loger.error("对该产品操作时发生异常。产品唯一性编码："+
						CherryBatchUtil .getString(itemMap.get("IFProductId"))+"，产品编码："+
						CherryBatchUtil .getString(itemMap.get("UnitCode"))+"，产品条码："+
						CherryBatchUtil .getString(itemMap.get("BarCode"))+"，产品名称："+
						CherryBatchUtil .getString(itemMap.get("ProductNameCN")),e);
				binBAT122_Service.manualRollback();
				// 记载导入新后台失败的ItemCode
				faildItemList.add(CherryBatchUtil.getString(itemMap.get("IFProductId")));
				// 失败件数加一
				failCount += 1;
				paraMap.put("synchFlag","3");
				paraMap.put("synchMsg",synchMsg);
				binBAT122_Service.updateIFSynchFlagFromOneToAnother(paraMap);
				binBAT122_Service.tpifManualCommit();
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param itemMap
	 * @return
	 */
	private void saveOrUpdPro(Map<String, Object> itemMap)
			throws Exception {
		// 记录查询产品ID
		int productId = 0;
		Map<String,Object> prtMap = new HashMap<String, Object>();
		int oldPrtId =ConvertUtil.getInt(itemMap.get("oldPrtId"));
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		// 产品编码唯一性验证开始
		prtMap.put("unitCode", itemMap.get("UnitCode"));
		boolean unicodeExist = checkUnicode(prtMap);
		if(unicodeExist){
			throw new CherryBatchException();
		}
		// 查询促销品中是否存在当前需要添加的barCode
		prtMap.put("barCode", itemMap.get("BarCode"));
		boolean barcodeExist = checkBarcode(prtMap);
		if(barcodeExist){
			throw new CherryBatchException();
		}
		try{
			if (0 == oldPrtId) {
				// 插入操作
				optFlag = 1;
				// 插入产品信息
				productId = binBAT122_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} else {
				// 更新操作
				optFlag = 2;
				productId = oldPrtId;
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 更新产品信息表
				binBAT122_Service.updateProductInfo(itemMap);
				// 更新件数加一
				updateCount += 1;
			}
		}catch(Exception e){
			loger.error("更新产品信息表时发生异常。产品唯一性编码："+
					CherryBatchUtil.getString(itemMap.get("IFProductId"))+"，中文名："+
					CherryBatchUtil.getString(itemMap.get("ProductNameCN"))+"。",e);
			throw e;
		}
	}

	/**
	 * 对编码唯一性进行验证
	 * @param prtMap
	 * @return
     */
	public boolean checkUnicode(Map<String, Object> prtMap) {
		int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);
		//如果编码已经存在的话
		if(unitCodeCount != 0){
			loger.error("操作产品信息表时发生异常，厂商编码已存在。产品唯一性编码："+
					CherryBatchUtil .getString(prtMap.get("IFProductId"))+"，产品编码："+
					CherryBatchUtil .getString(prtMap.get("UnitCode"))+"，产品条码："+
					CherryBatchUtil .getString(prtMap.get("BarCode"))+"，产品名称："+
					CherryBatchUtil .getString(prtMap.get("ProductNameCN")));
			return true;
		}
		return false;
	}

	/**
	 * 对条码唯一性进行验证
	 * @param prtMap
	 * @return
     */
	public boolean checkBarcode(Map<String, Object> prtMap){
		List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
		if(promPrtIdList.size() != 0){
			loger.error("操作产品厂商表时发生异常，厂商条码已存在。产品唯一性编码："+
					CherryBatchUtil .getString(prtMap.get("IFProductId"))+"，产品编码："+
					CherryBatchUtil .getString(prtMap.get("UnitCode"))+"，产品条码："+
					CherryBatchUtil .getString(prtMap.get("BarCode"))+"，产品名称："+
					CherryBatchUtil .getString(prtMap.get("ProductNameCN")));
			return true;
		}
		return false;
	}
	
	/**
	 * 更新产品价格信息
	 * 
	 * @param itemMap
	 * @throws CherryBatchException
	 */
	private void updPrtPrice(Map<String, Object> itemMap)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		if (!CherryBatchChecker.isNull(tempMap.get("SalePrice"))) {
			//如果SalPrice不是空的话
			try {
				// 新产品
				if(1 == optFlag){
					// 默认产品价格生效日期
					tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
					// 默认产品价格失效日期
					tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
					// 插入新的产品价格信息
					binBAT122_Service.insertProductPrice(tempMap);
					
				} 
				// 老产品
				else {
					tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
					binBAT122_Service.updProductPrice(tempMap);
				}
				
			} catch (Exception e) {
				if (optFlag == 1) {
					insertCount--;
				} else if (optFlag == 2) {
					updateCount--;
				}
				loger.error("操作产品价格表时发生异常。产品唯一性编码："+
						CherryBatchUtil.getString(itemMap.get("IFProductId"))+"，中文名："+
						CherryBatchUtil.getString(itemMap.get("ProductNameCN"))+"。",e);
				throw e;
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
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);
		try {
			if(1 == optFlag){
				// 添加产品厂商信息
				binBAT122_Service.insertProductVendor(tempMap);
				// 为什么在这里不添加产品条码对应关系信息，不需要添加
			}
			else if(2 == optFlag){
				// 编辑产品厂商信息
				List<Map<String, Object>> prtVendorList = binBAT122_Service.getProductVendorInfo(tempMap);
				if(prtVendorList != null && !prtVendorList.isEmpty()){
					if(prtVendorList.size() > 1){
						loger.error("产品ID为"+itemMap.get("IFProductId")+"的产品下存在多个条码！");
						throw new Exception();
					}
					Map<String,Object> prtVendorMap = prtVendorList.get(0);
					int proVendorId = ConvertUtil.getInt(prtVendorMap.get("proVendorId"));
					tempMap.put("prtVendorId", proVendorId);
					binBAT122_Service.updPrtVendor(tempMap);
					// 更新产品条码对应关系信息
					binBAT122_Service.updPrtBarCode(tempMap);
				}
			}
			
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			loger.error("操作产品厂商表时发生异常。产品唯一性编码："+
					CherryBatchUtil .getString(itemMap.get("IFProductId"))+"，产品编码："+
					CherryBatchUtil .getString(itemMap.get("UnitCode"))+"，产品条码："+
					CherryBatchUtil .getString(itemMap.get("BarCode")),e);
			throw e;
		}
	}
	
	
	/**
	 * 数据截断转换等非SQL处理
	 * 完善新后台产品表中所需要的字段
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap){
		// 截取产品名称(中英混合字符最大40--兼容老后台varchar数据类型)
		String itemDesc = CherryBatchUtil.getString(itemMap.get("ProductNameCN"));
		//得到系统配置项 产品名称长度
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = ConvertUtil.getInt(nameRule);
		// 使用系统配置项控制产品名称长度
		itemDesc = CherryBatchUtil.mixStrsub(itemDesc, nameRuleLen);
		// 接口表Status字段转换
		String validFlag = CherryBatchUtil.getString(itemMap.get("ValidFlag"));
		if("1".equals(validFlag)){
			itemMap.put("Status", "E"); 
		}else if("0".equals(validFlag)){
			itemMap.put("Status", "D");
		}
		
		// 产品类型，N：一般产品 P：促销品  替代Category
		String mode = CherryBatchUtil.getString(itemMap.get("Mode"));
		if(CherryBatchUtil.isBlankString(mode)){
			itemMap.put("Mode", "N");
		}
		
		// 是否积分兑换 0：不可 1：可以  默认为1
		String isExchanged = CherryBatchUtil.getString(itemMap.get("isExchanged"));
		if(CherryBatchUtil.isBlankString(isExchanged)){
			itemMap.put("isExchanged", "1");
		}
		
	}

	/**
	 * 更新产品分类
	 * 
	 * @param itemMap
	 */
	private void updPrtCategory(Map<String, Object> itemMap)
			throws Exception {
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		// 添加产品分类关系(大分类)
		String fName1 = ConvertUtil.getString(itemMap.get("ClassName1"));
		if(!CherryBatchUtil.isBlankString(fName1) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}	
		// 添加产品分类关系(中分类)
		String fName2 = ConvertUtil.getString(itemMap.get("ClassName2"));
		if(!CherryBatchUtil.isBlankString(fName2) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}		
		// 添加产品分类关系(小分类)
		String fName3 = ConvertUtil.getString(itemMap.get("ClassName3"));
		if(!CherryBatchUtil.isBlankString(fName3) ){
			Map<String, Object> temp = addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);			
			int catPropValID = CherryBatchUtil.Object2int(temp.get(ProductConstants.CATPROPVALID));
			if (catPropValID > 0) {
				tempList.add(temp);
			}
		}
		
		if (!tempList.isEmpty()) {
			// 删除产品分类属性关系信息
			binBAT122_Service.delPrtCategory(itemMap);
			// 循环插入
			for(Map<String,Object> temp:tempList) {
				// 插入产品分类关系表
				binBAT122_Service.insertPrtCategory(temp);
			}
		}
	}
	/**
	 * 添加产品分类关系
	 * 
	 * @param itemMap
	 * @param cateType
	 */
	private Map<String, Object> addPrtcategory(Map<String, Object> itemMap, String cateType)
			throws CherryBatchException,Exception {
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
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode1"));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName1"));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode2"));
			// 中分类属性名称q
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName2"));
		} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("ClassCode3"));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("ClassName3"));
		}
		try {
			// 取得分类属性值ID 
			int catePropValId = getCatPropValId(temp);
			// 基本属性预设值ID
			temp.put(ProductConstants.CATPROPVALID, catePropValId);
			String param = "";
			if (catePropValId > 0) {
				// 名称重复
			} else if (catePropValId == -1) {
				// 大分类
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					param = ProductConstants.CATE_NAME_B;
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					param = ProductConstants.CATE_NAME_M;
					
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					param = ProductConstants.CATE_NAME_L;
				}
				loger.error(param+"名称重复");
				// 名称和值不整合
			} else if (catePropValId == -2) {
				// 大分类
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					param = ProductConstants.CATE_NAME_B;
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					param =ProductConstants.CATE_NAME_M;
					
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					param = ProductConstants.CATE_NAME_L;
				}
				loger.error(param+"名称和值不整合。名称："+
						temp.get(ProductConstants.PROPVALUE_CN)+",值："+temp.get(ProductConstants.PROPVALUECHERRY));
			}
		} catch (Exception e) {
			if (optFlag == 1) {
				insertCount--;
			} else if (optFlag == 2) {
				updateCount--;
			}
			loger.error("操作产品类别相关表时发生异常。产品唯一性编码："+
					CherryBatchUtil.getString(itemMap.get("IFProductId"))+"，类别属性值名称"+
					CherryBatchUtil.getString(temp.get(ProductConstants.PROPVALUE_CN)),e);
			throw e;
		}
		return temp;
	}


	/**
	 * 取得分类属性值ID
	 * 
	 * @param map
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
		if (!CherryBatchUtil.isBlankString(propValueCN)) {
			//生成四位随机数字
			String propValue = getPropValue(map,ProductConstants.PROPVALUE);
			Map<String, Object>  propValueInfo = null;
			List<Map<String, Object>> propValueInfoLst = null;
			Map<String, Object>  propValueParam = new HashMap<String, Object>();
			propValueParam.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
			propValueParam.put(ProductConstants.PROPVALUE_CN, propValueCN);
			// 分类属性值4位【终端用】
			propValueParam.put(ProductConstants.PROPVALUECHERRY,propValueCherry);
			// 分类属性值空的场合，用随机生成4位填充【新后台用的属性值】
			if (CherryBatchUtil.isBlankString(propValueCherry)) {
				propValueParam.put(ProductConstants.PROPVALUECHERRY, propValue);
			}
			// 根据基本属性值ID和属性名称查询属性信息
			propValueInfoLst = binBAT122_Service.getCatPropValInfo(propValueParam);
			if(propValueInfoLst != null && !propValueInfoLst.isEmpty()) {
				// 属性值不存在的场合,名称重复
				if (propValueInfoLst.size() > 1) {
					return -1;
				}
				propValueInfo = propValueInfoLst.get(0);
				catPropValId = CherryUtil.obj2int(propValueInfo.get("propValId"));
			}
			
			if (catPropValId == 0) {
				// 分类属性值4位【终端用】
				map.put(ProductConstants.PROPVALUE,propValue);
				
				// 分类属性值空的场合，用随机生成4位填充【新后台用的属性值】
				if (CherryBatchUtil.isBlankString(propValueCherry)) {
					map.put(ProductConstants.PROPVALUECHERRY, propValue);
				} 
				
				// 添加分类属性值
				catPropValId = binBAT122_Service.addPropVal(map);
				
			} else {
				// 根据新后台用的属性值作为条件查询，若存在数据，抛错-2（属性值和已存在名称不匹配）
				String retPropValueCherry = CherryBatchUtil.getString(propValueInfo.get(ProductConstants.PROPVALUECHERRY));
				if (!CherryBatchUtil.isBlankString(propValueCherry) && !retPropValueCherry.equals(propValueCherry)) {
					return -2;
				}
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				//binBAT122_Service.updPropVal(map);
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
			int propValId = binBAT122_Service.getCatPropValId1(temp);
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
	private void init(Map<String, Object> map) throws CherryBatchException, Exception{
		comMap = getComMap(map);
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		comMap.put("JobCode", "BAT122");
		// 程序【开始运行时间】
		String runStartTime = binBAT122_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);

		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);

		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		
		// 业务日期
		String bussDate = binBAT122_Service.getBussinessDate(map);
		comMap.put("businessDate", bussDate);
		
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		comMap.put("nextBussDate", nextBussDate);
		
		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		map.putAll(comMap);
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
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT122");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT122");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE).toString());
		
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
		prtCatPropId_B = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binBAT122_Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binBAT122_Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binBAT122_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binBAT122_Service.updProp(temp);
			}
		}
		
	}
	/**
	 * 输出处理结果信息
	 *
	 * @throws CherryBatchException
	 */
	private void outMessage() {
		int successCount = totalCount - failCount;
		//打印出总条数，
		loger.info("产品信息导入(标准接口)一共处理的条数为:" + totalCount);
		//成功条数
		loger.info("产品信息导入(标准接口)处理成功的条数为:" + successCount);
		//失败的条数
		loger.info("产品信息导入(标准接口)处理失败的条数为:" + failCount);
		//更新的条数
		loger.info("产品信息导入(标准接口)更新的条数为:" + updateCount);
		//插入的条数
		loger.info("产品信息导入(标准接口)插入的条数为:" + insertCount);
		String msg = "===========================产品信息导入(标准接口)";
		String endMsg = "结束===================================";
		switch (flag) {
			case CherryBatchConstants.BATCH_SUCCESS:
				loger.info(msg + "正常" + endMsg);
				break;
			case CherryBatchConstants.BATCH_WARNING:
				loger.warn(msg + "警告" + endMsg);
				break;
			case CherryBatchConstants.BATCH_ERROR:
				loger.error(msg + "失败" + endMsg);
			default:
				loger.info(msg + endMsg);
		}
	}
}
