/*	
 * @(#)BINOTLS01_BL.java     1.0 @2014-11-18
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
package com.cherry.ot.ls.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;
import com.cherry.ot.ls.service.BINOTLS01_Service;

/**
 *
 * LotionSpa接口：产品导入BL
 * LS 使用完全覆盖
 *
 * @author jijw
 *
 * @version  2014-11-18
 */
public class BINOTLS01_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTLS01_BL.class);	
	@Resource
	private BINOTLS01_Service binOTLS01_Service;
	
	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 1000 */
	private final int BTACH_SIZE = 1000;
	
	/** 产品更新共通参数Map */
//	private Map<String, Object> paramsMap2;
	
	private Map<String, Object> comMap;
	
	/** 业务日期 */
//	private String bussDate;

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
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchOTLS01(Map<String, Object> map)
			throws CherryBatchException,Exception {

		// 初始化参数
		init(map);
		
		// 备份产品表
		backupProducts(map);
		
		// 查询接口产品列表
		List<Map<String, Object>> erpProductList = binOTLS01_Service.getErpProductList(map);
		
		totalCount += erpProductList.size();
		
		if (!CherryBatchUtil.isBlankList(erpProductList)) {
			// 更新新后台数据库产品
			updateBackEnd(erpProductList, map);
		}
		
		// 找出没有更新的数据，并逻辑删除（停用）
		String brandInfoId = CherryBatchUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID));
		delInvalidData(Integer.valueOf(brandInfoId));
		
		// 日志
		outMessage();
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
//			screenMap(itemMap);
			itemMap.putAll(comMap);
			
			try {
				// 校验产品属性
				chkPrt(itemMap);
				// 保存或更新产品信息
				insOrUpdPro(itemMap);
				// 更新产品价格信息
				updPrtPrice(itemMap);
				// 更新产品条码信息
				updProVendor(itemMap);
				// 更新分类信息
				updPrtCategory(itemMap);
				
				// 提交事务
				binOTLS01_Service.manualCommit();
				
			} catch (Exception e) {
				logger.outExceptionLog(e);
				// 回滚事务
				binOTLS01_Service.manualRollback();
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
		
		// 截取产品名称
		String itemDesc = CherryBatchUtil.getString(itemMap.get("Chinesename"));
		
		itemDesc = CherryBatchUtil.mixStrsub(itemDesc, 100); //由40改成100 --(WITPOSQA-12144)
		itemDesc = itemDesc.replace("'", " "); 
		itemMap.put("ItemDesc", itemDesc); 
		
	}
	
	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void insOrUpdPro(Map<String, Object> itemMap)
			throws CherryBatchException ,Exception{
		
		// 查询产品ID
		int productId = 0;
		int oldPrtId = binOTLS01_Service.searchProductId(itemMap);
		
		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 插入产品信息
				productId = binOTLS01_Service.insertProductInfo(itemMap);
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
				// 产品唯一性编码K3ProductId
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("K3ProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("Chinesename")));
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
				binOTLS01_Service.updateProductInfo(itemMap);
				
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
						.getString(itemMap.get("K3ProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(itemMap.get("Chinesename")));
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 校验产品属性
	 * @param itemMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void chkPrt(Map<String, Object> itemMap) throws CherryBatchException,Exception{
		
		// 截取产品名称
		String itemDesc = CherryBatchUtil.getString(itemMap.get("Chinesename"));
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = Integer.parseInt(nameRule);
		
		// 超过系统配置项“产品名称长度”，则报异常
		if(CherryBatchUtil.mixStrLength(itemDesc) > nameRuleLen){
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00058");
			batchExceptionDTO
					.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("K3ProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Unitcode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Barcode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Chinesename")));
//			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 查询产品ID
		int oldPrtId = binOTLS01_Service.searchProductId(itemMap);
		
		// ************************** 产品编码唯一性验证 **************************
		
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if(oldPrtId != 0){
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}
		
		try {
			prtMap.put("unitCode", itemMap.get("Unitcode"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);	
			
			if(unitCodeCount != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("K3ProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Unitcode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Barcode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Chinesename")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ************************** 产品编码唯一性验证 **************************
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
		try{
			// 查询促销品中是否存在当前需要添加的barCode
			prtMap.put("barCode", itemMap.get("Barcode"));
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if(promPrtIdList.size() != 0){
				throw new CherryBatchException();
			}
			
		} catch(CherryBatchException e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00058");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("K3ProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Unitcode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Barcode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil .getString(itemMap.get("Chinesename")));
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// ********************** 验证当前barCode在促销品中不存在   **********************
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
		
		if (!CherryBatchChecker.isNull(tempMap.get("Price"))) {
			try {
					
				// 新产品
				if(1 == optFlag){
					// 默认产品价格生效日期
					tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
					// 默认产品价格失效日期
					tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
					// 插入新的产品价格信息
					binOTLS01_Service.insertProductPrice(tempMap);
					
				} 
				// 老产品
				else {
					// 查询产品价格是否修改
					String productPriceID = binOTLS01_Service.selProductPrice(tempMap);
					
					// 产品价格已修改
					if(null == productPriceID){
						// 默认产品价格生效日期
						tempMap.put(ProductConstants.STRAT_DATE, tempMap.get("businessDate"));
						binOTLS01_Service.updProductPrice(tempMap);
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
						.getString(tempMap.get("K3ProductId")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil
						.getString(tempMap.get("Chinesename")));
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
				binOTLS01_Service.insertProductVendor(tempMap);
			} 
			else if(2 == optFlag){
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binOTLS01_Service.getProductVendorInfo(tempMap);
				int proVendorId = (Integer)prtVendorMap.get("proVendorId");
				tempMap.put("prtVendorId", proVendorId);
				binOTLS01_Service.updPrtVendor(tempMap);
				// 更新产品条码对应关系信息
				binOTLS01_Service.updPrtBarCode(tempMap);
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
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("K3ProductId")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("Unitcode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("BarCode")));
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
		binOTLS01_Service.delPrtCategory(itemMap);
		// 添加产品分类关系(大分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_1);
		// 添加产品分类关系(中分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_3);
		// 添加产品分类关系(小分类)
		addPrtcategory(itemMap, ProductConstants.CATE_TYPE_2);
		
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
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("Bclasscode"));
			// 大分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("Bclassname"));
		} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
			// 中分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
			// 中分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("Bclasscode"));
			// 中分类属性名称q
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("Bclassname"));
		} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
			// 小分类ID
			temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
			// 小分类属性值
			temp.put(ProductConstants.PROPVALUECHERRY,itemMap.get("Lclasscode"));
			// 小分类属性名称
			temp.put(ProductConstants.PROPVALUE_CN,itemMap.get("Lclassname"));
		}
		try {
			// 取得分类属性值ID
			int catePropValId = getCatPropValId(temp);
			if (catePropValId != 0) {
				temp.put(ProductConstants.CATPROPVALID, catePropValId);
				// 插入产品分类关系表
				binOTLS01_Service.insertPrtCategory(temp);
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
					.getString(itemMap.get("K3ProductId")));
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
			catPropValId = binOTLS01_Service.getCatPropValId1(map);
			if (catPropValId == 0) {
				
				// 如果接口编码大于4位，则将编码存入PropValueCherry，再随机生成4位唯一的编码存入PropValue;
				if(propValueCherry.length() <= 4){
					// 判断propValueCherry属性值在表中的propValue字段是否已经存在，若不存在，则使用propValueCherry，存在则随机生成4位
					Map<String,Object> tempMap = new HashMap<String, Object>();
					tempMap.put(ProductConstants.PRTCATPROPID, map.get(ProductConstants.PRTCATPROPID));
					tempMap.put(ProductConstants.PROPVALUE, propValueCherry);
					int propValIdByPV = binOTLS01_Service.getCatPropValId1(tempMap);
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
				catPropValId = binOTLS01_Service.addPropVal(map);
			} else {
				map.put(ProductConstants.CATPROPVALID, catPropValId);
				// 更新分类属性值
				binOTLS01_Service.updPropVal(map);
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
			int propValId = binOTLS01_Service.getCatPropValId1(temp);
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
		// 共通Map
		comMap = getComMap(map);
		
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("tVersion", tVersion);
		
		// 取得当前部门(柜台)产品表版本号
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);
		
		// 系统时间
		String sysDate = binOTLS01_Service.getSYSDate();
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, sysDate);
		
		// 业务日期
		String bussDate = binOTLS01_Service.getBussinessDate(map);
		comMap.put("businessDate", bussDate);
		
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		comMap.put("nextBussDate", nextBussDate);
		
		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		
		// 开始时间
		String	startTime = CherryUtil.suffixDate(bussDate, 0);
		comMap.put(ProductConstants.STARTTIME, startTime);
		
		// 取得大中小分类对应的分类Id
		getCatePropIds(comMap);
	}
	
	/**
	 * 备份产品表
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void backupProducts(Map<String, Object> map) throws CherryBatchException,Exception {
		
		try {
			// 更新世代番号
			binbeifpro01Service.updateBackupCount();
			// 删除世代番号超过上限的数据
			binbeifpro01Service.clearBackupData(CherryBatchConstants.COUNT);
			// 备份产品信息表
			binbeifpro01Service.backupProducts(comMap);
			// 提交事务
			binOTLS01_Service.manualCommit();
		} catch (Exception e) {
			// 事务回滚
			binOTLS01_Service.manualRollback();
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 伦理删除无效的数据
	 * 
	 * @throws CherryBatchException
	 */
	private void delInvalidData(int brandInfoId) throws CherryBatchException {
		// 找出没有更新的数据，并伦理删除
		// 取得要伦理删除的产品数据
		// 删除数据条数
		int delCount = 0;
		// 查询删除产品列表
		List<Map<String, Object>> delList = binbeifpro01Service
				.getDelList(brandInfoId);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			try {
				for (Map<String, Object> map : delList) {
					map.putAll(comMap);
				}
				// 删除无效的产品数据
				binbeifpro01Service.delInvalidProducts(delList);
				// 删除无效的产品分类数据
//				binbeifpro01Service.delInvalidProCate(delList);
				// 删除无效的产品厂商数据
				binbeifpro01Service.delInvalidProVendors(delList);
				// 删除无效的产品价格数据
//				binbeifpro01Service.delInvalidProPrices(delList);
				for (Map<String, Object> temp : delList) {
					temp.put(ProductConstants.CLOSINGTIME, comMap.get(ProductConstants.CLOSINGTIME));
					// 产品条码对应关系表增加停用时间
					binbeifpro01Service.setClosingTime(temp);
				}
				// 提交事务
				binbeifpro01Service.manualCommit();
				// 统计总条数
				delCount = delList.size();
			} catch (Exception e) {
				// 回滚事务
				binbeifpro01Service.manualRollback();
				// 总条数置0
				delCount = 0;
				flag = CherryBatchConstants.BATCH_ERROR;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EIF00010");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1, e);
			}
		}
		// logger 处理结果信息输出
		BatchLoggerDTO loggerDelDTO = new BatchLoggerDTO();
		loggerDelDTO.setCode("IIF00006");
		loggerDelDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO.addParam(String.valueOf(delCount));
		logger.BatchLogger(loggerDelDTO);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTLS01");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTLS01");
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
		prtCatPropId_B = binOTLS01_Service.getCatPropId2(temp);
		if (prtCatPropId_B == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_B = binOTLS01_Service.getCatPropId1(temp);
			if (prtCatPropId_B == 0) {
				// 添加大分类
				prtCatPropId_B = binOTLS01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_B);
				// 更新分类终端显示区分
				binOTLS01_Service.updProp(temp);
			}
		}
		// 中分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_M);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_3);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_M = binOTLS01_Service.getCatPropId2(temp);
		if (prtCatPropId_M == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_M = binOTLS01_Service.getCatPropId1(temp);
			if (prtCatPropId_M == 0) {
				// 添加中分类
				prtCatPropId_M = binOTLS01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_M);
				// 更新分类终端显示区分
				binOTLS01_Service.updProp(temp);
			}
		}
		// 小分类名
		temp.put(ProductConstants.PROPNAMECN, ProductConstants.CATE_NAME_L);
		temp.put(ProductConstants.TEMINALFLAG, ProductConstants.CATE_TYPE_2);
		// 根据分类终端显示区分查询分类Id
		prtCatPropId_L = binOTLS01_Service.getCatPropId2(temp);
		if (prtCatPropId_L == 0) {
			// 根据分类名查询分类Id
			prtCatPropId_L = binOTLS01_Service.getCatPropId1(temp);
			if (prtCatPropId_L == 0) {
				// 添加小分类
				prtCatPropId_L = binOTLS01_Service.addCatProperty(temp);
			} else {
				temp.put(ProductConstants.PRTCATPROPID, prtCatPropId_L);
				// 更新分类终端显示区分
				binOTLS01_Service.updProp(temp);
			}
		}
		
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
//		if(!CherryBatchUtil.isBlankList(faildItemList)){
//			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
//			batchLoggerDTO6.setCode("EOT00021");
//			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
//			batchLoggerDTO6.addParam(faildItemList.toString());
//			logger.BatchLogger(batchLoggerDTO6);
//		}
		
	}
	

}
