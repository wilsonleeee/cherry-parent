/*	
 * @(#)BINBAT139_BL.java     1.0 @2016-3-17
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
package com.cherry.ot.jh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.bl.BINBEIFPRO01_BL;
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;
import com.cherry.ot.jh.service.BINBAT139_Service;
import com.jahwa.common.JahwaWebServiceProxy;
import com.jahwa.pos.ecc.Dt_Material_resRowItems;
import com.jahwa.pos.ecc.ZSAL_PRICE;

/**
 * 
 * SAP接口(WSDL)：产品导入BL
 * 
 * WebService请求数据，并导入新后台
 * 
 * @author jijw
 * 
 * @version 2016-3-17
 */
public class BINBAT139_BL {

	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT139_BL.class);

	/** BATCH LOGGER */
	private static Logger loggerS = LoggerFactory.getLogger(BINBEIFPRO01_BL.class.getName());
	
	@Resource
	private BINBAT139_Service binBAT139_Service;

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** JOB执行相关共通 IF */
	@Resource(name = "binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;

	/** 各类编号取号共通BL */
	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	private Map<String, Object> comMap;

	/** 更新添加操作flag */
	private int optFlag = 0;

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
	public int tran_batchExecute(Map<String, Object> map)
			throws CherryBatchException, Exception {

		try {
			// === 初始化 ===
			init(map);
			
			// === 调用WSDL(产品、产品价格)取得产品及价格数据并整合到产品集合
			List<Map<String, Object>> productList = getProductByWSDL();
			totalCount += productList.size();

			if(!CherryBatchUtil.isBlankList(productList)){
				// === 世代备份 ===
				bakPrt(map);
				
				// === 更新新后台数据库产品相关表 ===
				updateWitpos(productList);
				
				// === 找出没有更新的数据，并伦理删除
				delInvalidData();
			}

		} 
		finally {
			// === 程序数据处理运行结果
			outMessage();
			// === 程序结束处理
			programEnd(map);
		}

		return flag;
	}
	
	/**
	 * 获取SAP接口提供的产品列表
	 * @return
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getProductByWSDL() throws CherryBatchException,Exception{
		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		
		Dt_Material_resRowItems[] productArr = null;
		try {
			// === 调用WSDL产品接口 ===
			productArr = JahwaWebServiceProxy.getProductList();
			if (null == productArr || productArr.length == 0) {
				
				flag = CherryBatchConstants.BATCH_WARNING;
				// 调用SAP Webservice（{0}）成功，返回的数据条件为0，请确认是否正常。
				fReason = "调用SAP Webservice（产品接口[JahwaWebServiceProxy.getProductList()]）成功，返回的数据条件为0，请确认是否正常。";
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00118");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam("产品接口[JahwaWebServiceProxy.getProductList()]");
				// 处理总件数
				logger.BatchLogger(batchLoggerDTO);
			}
			
		} catch (Exception e) {
			
			flag = CherryBatchConstants.BATCH_ERROR;
			// 调用SAP Webservice（{0}）失败。
			fReason = "调用SAP Webservice产品接口[JahwaWebServiceProxy.getProductList()]失败。";
					
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorCode("EOT00117");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("产品接口[JahwaWebServiceProxy.getProductList()]");
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		ZSAL_PRICE[] prtPriceArr = null;
		try {
			// === 调用WSDL产品价格接口 ===
			prtPriceArr = JahwaWebServiceProxy.getProductPriceList("ZLSJ");

			if (null == prtPriceArr || prtPriceArr.length == 0) {
				flag = CherryBatchConstants.BATCH_WARNING;
				// 调用SAP Webservice（{0}）成功，返回的数据条件为0，请确认是否正常。
				fReason = "调用SAP Webservice（产品价格接口[JahwaWebServiceProxy.getProductList()]）成功，返回的数据条件为0，请确认是否正常。";
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00118");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam("产品价格接口[JahwaWebServiceProxy.getProductList()]");
				// 处理总件数
				logger.BatchLogger(batchLoggerDTO);
			}
			
		} catch (Exception e) {
			
			flag = CherryBatchConstants.BATCH_ERROR;
			// 调用SAP Webservice（{0}）失败。
			fReason = "调用SAP Webservice产品价格接口[JahwaWebServiceProxy.getProductList()]失败。";
					
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorCode("EOT00117");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("产品价格接口[JahwaWebServiceProxy.getProductList()]");
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 根据产品编码分组产品价格集合
		Map<String, Object> grpPrtPriceMap = new HashMap<String, Object>();
		if (null != prtPriceArr && prtPriceArr.length != 0) {
			for (ZSAL_PRICE priceItem : prtPriceArr) {
				if(null != priceItem.getKSCHL() && "ZLSJ".equals(priceItem.getKSCHL())){
					// 将条件类型(ZKSCHL/KSCHL)为ZLSJ的产品价格数据分组，并匹配到对应的产品
					Map<String, Object> priceItemMap = ConvertUtil.bean2Map(priceItem);
					
					String matnr = (String)priceItemMap.get("MATNR");
					if (grpPrtPriceMap.containsKey(matnr)) {
//				List items = (List) grpPrtPriceMap.get(item.getMATNR());
						List<Map<String, Object>> priceItems = (List<Map<String, Object>>) grpPrtPriceMap.get(matnr);
						priceItems.add(priceItemMap);
					} else {
						List<Map<String, Object>> priceItems = new ArrayList<Map<String, Object>>();
						priceItems.add(priceItemMap);
						grpPrtPriceMap.put(matnr, priceItems);
					}
				}
			}
		}
		
		// 将产品价格部分属性整合到产品DTO
		if (null != prtPriceArr && prtPriceArr.length != 0) {
			for (Dt_Material_resRowItems productArrItem : productArr) {
				
				Map<String, Object> itemMap = ConvertUtil.bean2Map(productArrItem);
				itemMap = CherryBatchUtil.removeEmptyVal(itemMap);
				List<Map<String, Object>> priceItems = (List<Map<String, Object>>) grpPrtPriceMap.get(productArrItem.getMATNR());
				if(null != priceItems){
					itemMap.put("priceItems", priceItems);
				}
				
				productList.add(itemMap);
			}
		}
		
		return productList;
	}

	/**
	 * 世代管理
	 * 
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void bakPrt(Map<String, Object> map) throws CherryBatchException,
			Exception {
		try {
			// 更新世代番号
			binBAT139_Service.updateBackupCount();
			// 删除世代番号超过上限的数据
			binBAT139_Service.clearBackupData(CherryBatchConstants.COUNT);
			// 备份产品信息表
			binBAT139_Service.backupProducts(map);
			// 提交事务
			binBAT139_Service.manualCommit();
		} catch (Exception e) {
			// 事务回滚
			binBAT139_Service.manualRollback();
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00119");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 更新新后台数据库产品相关表
	 * 
	 * @param List
	 * @return 无
	 * @throws
	 * @throws CherryBatchException
	 */
	private void updateWitpos(List<Map<String, Object>> productList) {

		for (Map<String, Object> itemMap : productList) {

			try {

				itemMap.putAll(comMap);

				// 转换
				screenMap(itemMap);

				// 校验产品属性
				chkPrt(itemMap);

				String zStatus = CherryBatchUtil.getString(itemMap.get("ZSTATUS"));
				
				itemMap.put("unitCode",ConvertUtil.getString(itemMap.get("unitCode")));
				int oldPrtId = binBAT139_Service.searchProductId(itemMap);

				// 导入来的产品是停用的,用以下方式处理
				if ("1".equals(zStatus)) {
					if (oldPrtId == 0) {
						// 如果是新增的产品，且产品为无效，就不导入到新后台
						continue;
					} else {

						// 更新时，停用新后台产品
						itemMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
						Map<String, Object> prtVendorMap = binBAT139_Service.getProductVendorInfo(itemMap);
						int proVendorId = (Integer) prtVendorMap.get("proVendorId");
						itemMap.put("prtVendorId", proVendorId);

						// 删除无效的产品数据
						List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
						itemList.add(itemMap);
						binBAT139_Service.delInvalidProducts(itemList);
						// 删除无效的产品厂商数据
						binBAT139_Service.delInvalidProVendors(itemList);
						// 更新停用日时
						binBAT139_Service.setClosingTime(itemMap);

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
				// 提交事务
				binbeifpro01Service.manualCommit();

			}
			catch (Exception e) {

				faildItemList.add(ConvertUtil.getString(itemMap.get("unitCode")));
				
				// 回滚事务
				binbeifpro01Service.manualRollback();
				
				// 失败产品写入失败履历表
				Map<String,Object> falidMap = new HashMap<String, Object>();
				falidMap.putAll(comMap);
				falidMap.put("UnionIndex", itemMap.get("MATNR"));
				
				falidMap.put("ErrorMsg", ",{\"" + e.getMessage() + "\"}");
				binbecm01_IF.mergeJobRunFaildHistory(falidMap);

				loggerS.error(e.getMessage(), e);
				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	/**
	 * 校验产品属性
	 * 
	 * @param itemMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void chkPrt(Map<String, Object> productMap)
			throws CherryBatchException, Exception {

		// 产品名称
		String prtName = CherryBatchUtil.getString(productMap.get("prtName"));
		String nameRule = binOLCM14_BL.getConfigValue("1132", ConvertUtil.getString(productMap.get(CherryBatchConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(productMap.get(CherryBatchConstants.BRANDINFOID)));
		int nameRuleLen = Integer.parseInt(nameRule);

		if(CherryChecker.isNullOrEmpty(prtName)){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00120");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("产品名称");
			throw new CherryBatchException(batchExceptionDTO);
		}else{
			// 超过系统配置项“产品名称长度”，则报异常
			if (CherryBatchUtil.mixStrLength(prtName) > nameRuleLen) {

				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00020");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 厂商编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE)));
				// 产品条码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE)));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get("prtName")));
				// batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
		
		// 厂商编码
		String unitCode = CherryBatchUtil.getString(productMap.get("unitCode"));
		// 厂商编码必须入力验证
		if (CherryChecker.isNullOrEmpty(unitCode)) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00120");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("厂商编码");
			throw new CherryBatchException(batchExceptionDTO);
		} else if (unitCode.length() > 20) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00121");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("厂商编码");
			batchExceptionDTO.addErrorParam("20");
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// 厂商编码
		String barCode = CherryBatchUtil.getString(productMap.get("barCode"));
		// 厂商编码必须入力验证
		if (CherryChecker.isNullOrEmpty(barCode)) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00120");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("产品条码");
			throw new CherryBatchException(batchExceptionDTO);
		} else if (barCode.length() > 20) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00121");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.addErrorParam("产品条码");
			batchExceptionDTO.addErrorParam("20");
			throw new CherryBatchException(batchExceptionDTO);
		}

		// ************************** 产品编码唯一性验证 **************************
		// 查询产品ID
		int oldPrtId = binbeifpro01Service.searchProductId(productMap);

		Map<String, Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(productMap);
		if (oldPrtId != 0) {
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}

		try {
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);

			if (unitCodeCount != 0) {
				throw new CherryBatchException();
			}

		} catch (CherryBatchException e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00014");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(productMap.get("prtName")));
			throw new CherryBatchException(batchExceptionDTO);
		}

		// ************************** 产品编码唯一性验证 **************************

		// ********************** 验证当前barCode在促销品中不存在 **********************
		try {
			// 查询促销品中是否存在当前需要添加的barCode
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if (promPrtIdList.size() != 0) {
				throw new CherryBatchException();
			}

		} catch (CherryBatchException e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF00015");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get(ProductConstants.UNITCODE)));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get(ProductConstants.BAR_CODE)));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil
					.getString(productMap.get("prtName")));
			throw new CherryBatchException(batchExceptionDTO);
		}

		// ********************** 验证当前barCode在促销品中不存在 **********************
	}

	/**
	 * 数据转换
	 * 
	 * @param itemMap
	 */
	private void screenMap(Map<String, Object> itemMap) throws Exception {

		try{
			// 销售状态 (用0或1表示，1代表冻结，不可销售),空代表0
			// 转换为新后台的ValidFlag 及Status
			String zStatus = CherryBatchUtil.getString(itemMap.get("ZSTATUS"));
			if (null == zStatus || "0".equals(zStatus)) {
				itemMap.put("status", "E");
				itemMap.put("validFlag", "1");
			} else if ("1".equals(zStatus)) {
				itemMap.put("status", "D");
				itemMap.put("validFlag", "0");
			}
			
			// 条码
			String zbarcod = CherryBatchUtil.getString(itemMap.get("ZBARCOD"));
			String matnr = CherryBatchUtil.getString(itemMap.get("MATNR")); // 编码
			if(CherryBatchUtil.isBlankString(zbarcod)){
				// 当条码为空时，用编码替代
				zbarcod = matnr;
			}
			itemMap.put("barCode", zbarcod);
			itemMap.put("unitCode", matnr);
			
			// 价格为null,设置为0 ---等QA问题返回后，再处理
			String fSalePrice = ConvertUtil.getString(itemMap.get("FSalePrice"));
			if (CherryBatchUtil.isBlankString(fSalePrice)) {
				itemMap.put("FSalePrice", "0.00");
			}
			
			// 产品名称
			String maktx = CherryBatchUtil.getString(itemMap.get("MAKTX"));
			itemMap.put("prtName", maktx);
			
			// 保质期
			String mhdhb = CherryBatchUtil.getString(itemMap.get("MHDHB"));
			itemMap.put("shelfLife", mhdhb);
			
			// 品牌编码
			String spart = CherryBatchUtil.getString(itemMap.get("SPART"));
			itemMap.put("originalBrand", spart);
			
			// 物料类型
			String mtart = CherryBatchUtil.getString(itemMap.get("MTART"));
			itemMap.put("mode", mtart.equals("FERT") ? "N" : mtart);
			
		}catch(Exception e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00124");
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
	}

	/**
	 * 更新或插入产品信息
	 * 
	 * @param map
	 * @return
	 */
	private void saveOrUpdPro(Map<String, Object> itemMap)
			throws CherryBatchException,Exception {

		// 查询产品ID
		int productId = 0;
		// int oldPrtId = binbekdpro01_Service.searchProductId(itemMap);
		int oldPrtId = (Integer) itemMap.get("oldPrtId");

		// ************************** 产品编码唯一性验证 **************************

		Map<String, Object> prtMap = new HashMap<String, Object>();
		prtMap.putAll(itemMap);
		if (oldPrtId != 0) {
			prtMap.put(ProductConstants.PRODUCT_ID, oldPrtId);
		}

		try {
			prtMap.put("unitCode", itemMap.get("unitCode"));
			int unitCodeCount = binOLCM05_BL.getExistUnitCodeForPrtAndProm(prtMap);

			if (unitCodeCount != 0) {
				
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00122");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性（厂商编码）
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
				// 厂商编码
				throw new CherryBatchException(batchExceptionDTO);
			}

		} catch (CherryBatchException e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setException(e);
			batchExceptionDTO.setErrorCode("EOT00054");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性（厂商编码）
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("barCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("prtName")));
			throw new CherryBatchException(batchExceptionDTO);
		}

		// ************************** 产品编码唯一性验证 **************************

		// ********************** 验证当前barCode在促销品中不存在 **********************
		try {
			// 查询促销品中是否存在当前需要添加的barCode
			prtMap.put("barCode", itemMap.get("barCode"));
			List<Integer> promPrtIdList = binOLCM05_BL.getPromotionIdByBarCode(prtMap);
			if (promPrtIdList.size() != 0) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00123");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性（厂商编码）
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("barCode")));
				// 厂商编码
				throw new CherryBatchException(batchExceptionDTO);
			}

		} catch (CherryBatchException e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00055");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码K3Product
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("barCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("prtName")));
			throw new CherryBatchException(batchExceptionDTO);
		}

		// ********************** 验证当前barCode在促销品中不存在 **********************

		if (0 == oldPrtId) {
			// 插入操作
			optFlag = 1;
			try {
				// 插入产品信息
				productId = binBAT139_Service.insertProductInfo(itemMap);
				// 把产品Id设入条件map
				itemMap.put(ProductConstants.PRODUCT_ID, productId);
				// 插入件数加一
				insertCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00013");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("prtName")));
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
				binBAT139_Service.updateProductInfo(itemMap);

				// 更新件数加一
				updateCount += 1;
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EOT00014");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 产品唯一性编码
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("unitCode")));
				// 中文名
				batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemMap.get("prtName")));
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
	@SuppressWarnings("unchecked")
	private void updPrtPrice(Map<String, Object> itemMap)
			throws CherryBatchException {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);

		try {
			if (!CherryBatchChecker.isNullOrEmpty(itemMap.get("priceItems"))) {
				List<Map<String, Object>> priceItems = (List<Map<String, Object>>)itemMap.get("priceItems");
				// 新产品
				if (1 == optFlag) {
					for(Map<String, Object> priceItem : priceItems){
						// 产品价格
						String kbetr = ConvertUtil.getString(priceItem.get("KBETR"));
						tempMap.put("salePrice",CherryChecker.isEmptyString(kbetr) ? "0.00" : kbetr);
						// 产品价格生效日期
						tempMap.put(ProductConstants.STRAT_DATE,priceItem.get("DATAB"));
						// 产品价格失效日期
						tempMap.put(ProductConstants.END_DATE,priceItem.get("DATBI"));
						// 插入新的产品价格信息
						binBAT139_Service.insertProductPrice(tempMap);
					}
				}
				// 老产品
				else {
					// 清空产品对应的价格数据
					binBAT139_Service.deleteProductPrice(tempMap);
					
					for(Map<String, Object> priceItem : priceItems){
						// 产品价格
						String kbetr = ConvertUtil.getString(priceItem.get("KBETR"));
						tempMap.put("salePrice",CherryChecker.isEmptyString(kbetr) ? "0.00" : kbetr);
						// 产品价格生效日期
						tempMap.put(ProductConstants.STRAT_DATE,priceItem.get("DATAB"));
						// 产品价格失效日期
						tempMap.put(ProductConstants.END_DATE,priceItem.get("DATBI"));
						// 插入新的产品价格信息
						binBAT139_Service.insertProductPrice(tempMap);
					}
				}
			} 
			// 没有找到对应的产品价格时，默认给0.00
			else {
				
				// 清空产品对应的价格数据
				binBAT139_Service.deleteProductPrice(tempMap);
				
				// 默认产品价格
				tempMap.put("salePrice","0.00");
				// 默认产品价格生效日期
				tempMap.put(ProductConstants.STRAT_DATE,tempMap.get("businessDate"));
				// 默认产品价格失效日期
				tempMap.put(ProductConstants.END_DATE,ProductConstants.DEFAULT_END_DATE);
				binBAT139_Service.insertProductPrice(tempMap);
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
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 产品唯一性编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("unitCode")));
			// 中文名
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("prtName")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 更新产品条码信息
	 * 
	 * @param itemMap
	 * @throws CherryBatchException
	 */
	private void updProVendor(Map<String, Object> itemMap) throws CherryBatchException {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.putAll(itemMap);

		try {

			if (1 == optFlag) {
				// 添加产品厂商信息
				binBAT139_Service.insertProductVendor(tempMap);
			} else if (2 == optFlag) {
				// 编辑产品厂商信息
				Map<String, Object> prtVendorMap = binBAT139_Service.getProductVendorInfo(tempMap);
				int proVendorId = (Integer) prtVendorMap.get("proVendorId");
				tempMap.put("prtVendorId", proVendorId);
				binBAT139_Service.updPrtVendor(tempMap);
				// 更新产品条码对应关系信息
				binBAT139_Service.updPrtBarCode(tempMap);
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
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("unitCode")));
			// 厂商编码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("unitCode")));
			// 产品条码
			batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(tempMap.get("barCode")));
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 伦理删除无效的数据
	 * 
	 * @throws CherryBatchException
	 */
	private void delInvalidData() throws CherryBatchException,Exception {
		// 找出没有更新的数据，并伦理删除
		// 取得要伦理删除的产品数据
		// 删除数据条数
		int delCount = 0;
		// 查询删除产品列表
		int brandInfoId = ConvertUtil.getInt(comMap.get(CherryBatchConstants.BRANDINFOID));
		List<Map<String, Object>> delList = binBAT139_Service.getDelList(brandInfoId);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			try {
				for (Map<String, Object> map : delList) {
					map.putAll(comMap);
				}
				// 删除无效的产品数据
				binBAT139_Service.delInvalidProducts(delList);
				// 删除无效的产品厂商数据
				binBAT139_Service.delInvalidProVendors(delList);
				// 删除无效的产品价格数据
//				binbeifpro01Service.delInvalidProPrices(delList);
				for (Map<String, Object> temp : delList) {
					temp.put(ProductConstants.CLOSINGTIME, comMap.get(ProductConstants.CLOSINGTIME));
					// 产品条码对应关系表增加停用时间
					binBAT139_Service.setClosingTime(temp);
				}
				// 提交事务
				binBAT139_Service.manualCommit();
				// 统计总条数
				delCount = delList.size();
			} catch (Exception e) {
				// 回滚事务
				binBAT139_Service.manualRollback();
				// 总条数置0
				delCount = 0;
				flag = CherryBatchConstants.BATCH_ERROR;
				
				fReason = "删除产品相关表时发生异常。";
				
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF00010");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);				
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
	 * 程序初始化
	 * 
	 * @param map
	 */
	private void init(Map<String, Object> map) {

		comMap = getComMap(map);

		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
//		map.put("JobCode", "BAT139");
		comMap.put("JobCode", "BAT139");

		// 程序【开始运行时间】
		String runStartTime = binBAT139_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, runStartTime);
		// 更新日时
		comMap.put(CherryConstants.UPDATE_TIME, runStartTime);

		// 业务日期
		String bussDate = binBAT139_Service.getBussinessDate(map);
		comMap.put("businessDate", bussDate);
		map.put("businessDate", bussDate);

		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(CherryBatchConstants.DATE_PATTERN, bussDate, 1);
		comMap.put("nextBussDate", nextBussDate);

		// 停用时间
		String closingTime = CherryUtil.suffixDate(bussDate, 1);
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);

		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String prtTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("prtTVersion", prtTVersion);

		// 取得大中小分类对应的分类Id
		// getCatePropIds(comMap);
	}

	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
		paraMap.putAll(comMap);

		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", insertCount + updateCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
		binbecm01_IF.insertJobRunHistory(paraMap);
		binBAT139_Service.manualCommit();
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT139");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT139");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID,map.get(CherryBatchConstants.BRANDINFOID).toString());

		return baseMap;
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
		if (!CherryBatchUtil.isBlankList(faildItemList)) {
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00021");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildItemList.toString());
			logger.BatchLogger(batchLoggerDTO6);

			fReason = "产品导入部分数据处理失败，具体见log日志";
		}

	}

}
