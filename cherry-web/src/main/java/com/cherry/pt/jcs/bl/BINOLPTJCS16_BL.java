/*
 * @(#)BINOLPTJCS14_BL.java v1.0 2014-6-12
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.common.PrtUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS16_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS17_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS03_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS04_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS06_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS14_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS16_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS17_Service;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 柜台产品价格维护BL
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS16_BL implements BINOLPTJCS16_IF {
	
	@Resource(name="binOLPTJCS16_Service")
	private BINOLPTJCS16_Service binOLPTJCS16_Service;
	
	@Resource(name="binOLPTJCS17_Service")
	private BINOLPTJCS17_Service binOLPTJCS17_Service;
	
	@Resource(name="binOLPTJCS14_Service")
	private BINOLPTJCS14_Service binOLPTJCS14_Service;
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS16_BL.class);
	
	/**
	 * 
	 * 获取导入的数据
	 * 
	 * @param map导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception {
		
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 方案数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.PRTSOLUDETAIL_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 方案数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.PRTSOLUDETAIL_SHEET_NAME });
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.PRODUCTSOLU_EXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
        }
		
        // 存入导入的所有数据
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		
		// 存入校验错误的行数据
		List<Map<String,Object>> errorList = new ArrayList<Map<String, Object>>();
        
		int sum = 0;
		for (int r = 1; r < dateSheet.getRows(); r++) {
			
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			
			// 厂商编码(unitCode)
			String unitCode = dateSheet.getCell(0, r).getContents().trim();
			// 产品名称（备用）
			String prtName = dateSheet.getCell(1, r).getContents().trim();
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			boolean saleMemPriceBol = true;
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_1.equals(config)){
				// 销售价格
				String salePrice = dateSheet.getCell(2, r).getContents().trim();
				rowMap.put("salePrice", getPrice(salePrice));
				// 会员价格
				String memPrice = dateSheet.getCell(3, r).getContents().trim();
				if(CherryChecker.isNullOrEmpty(memPrice)){
					rowMap.put("memPrice", rowMap.get("salePrice"));
				}else{
					rowMap.put("memPrice", memPrice);
				}
				
				// 验证销售价格会员价格是否为空
				saleMemPriceBol = CherryChecker.isNullOrEmpty(salePrice) && CherryChecker.isNullOrEmpty(memPrice);
			}
			
			// (连续10行)整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(unitCode) && CherryChecker.isNullOrEmpty(prtName)
					&&  saleMemPriceBol) {
				sum++;
				if (sum >= 10) {
					break;
				} else {
					continue;
				}
			}
			sum = 0;
			
			rowMap.put("unitCode", unitCode);
			rowMap.put("prtName", prtName);
			
			
			rowMap.put("rowNumber", r); // 行号
			
			// 数据基本格式校验
			this.checkData(rowMap, map, errorList);
			
			detailList.add(rowMap);
		}
		
		if (detailList.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { ProductConstants.DATE_SHEET_NAME });
		}
		
		importDatas.put("detailList", detailList);
		importDatas.put("errorList", errorList);
		
		return importDatas;
	}
	
	/**
	 * 
	 * 获取导入的数据(门店用)
	 * 
	 * @param map导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> ResolveExcelCnt(Map<String, Object> map) throws Exception {
		
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 方案数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.PRTSOLUDETAIL_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 方案数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.PRTSOLUDETAIL_SHEET_NAME });
		}
		
		//每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
		String version = sheets[0].getCell(1, 0).getContents().trim();
		if(!CherryConstants.PRODUCTSOLU_CNT_EXCEL_VERSION.equals(version)){
			throw new CherryException("EBS00103");
		}
		
		// 存入导入的所有数据
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		
		// 存入校验错误的行数据
		List<Map<String,Object>> errorList = new ArrayList<Map<String, Object>>();
		
		int sum = 0;
		for (int r = 1; r < dateSheet.getRows(); r++) {
			
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			
			// 厂商编码(unitCode)
			String unitCode = dateSheet.getCell(0, r).getContents().trim();
			// 产品名称（）
			String prtName = dateSheet.getCell(1, r).getContents().trim();
			// 取得系统配置项产品方案添加模式
			String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
			boolean saleMemPriceBol = true;
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_1.equals(config)){
				// 销售价格
				String salePrice = dateSheet.getCell(2, r).getContents().trim();
				rowMap.put("salePrice", getPrice(salePrice));
				// 会员价格
				String memPrice = dateSheet.getCell(3, r).getContents().trim();
				if(CherryChecker.isNullOrEmpty(memPrice)){
					rowMap.put("memPrice", rowMap.get("salePrice"));
				}else{
					rowMap.put("memPrice", memPrice);
				}
				
				// 验证销售价格会员价格是否为空
				saleMemPriceBol = CherryChecker.isNullOrEmpty(salePrice) && CherryChecker.isNullOrEmpty(memPrice);
			}
			
			// (连续10行)整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(unitCode) && CherryChecker.isNullOrEmpty(prtName)
					&&  saleMemPriceBol) {
				sum++;
				if (sum >= 10) {
					break;
				} else {
					continue;
				}
			}
			sum = 0;
			
			rowMap.put("unitCode", unitCode);
			rowMap.put("prtName", prtName);
			rowMap.put("soluProductName", prtName);
			
			
			rowMap.put("rowNumber", r); // 行号
			
			// 数据基本格式校验
			this.checkDataCnt(rowMap, map, errorList);
			
			detailList.add(rowMap);
		}
		
		if (detailList.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { ProductConstants.DATE_SHEET_NAME });
		}
		
		importDatas.put("detailList", detailList);
		importDatas.put("errorList", errorList);
		
		return importDatas;
	}
	
	/**
	 * 设置若价格为空默认为“0.00”
	 * @param price
	 * @return
	 */
	private String getPrice(String price){
		if(CherryChecker.isNullOrEmpty(price)){
			price = "0.00";
		}
		return price;
	}
	
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @param map
	 * @param errorList 存在有错误的行
	 * @return
	 * @throws Exception
	 * 返回值 true:校验不通过(有错误) ，false:校验通过
	 */
	private boolean checkDataCnt(Map<String, Object> rowMap,Map<String,Object> map,List<Map<String,Object>> errorList ) throws Exception {
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		
		String rowNumberStr = ConvertUtil.getString(rowMap.get("rowNumber"));
		Integer rowNumber = Integer.parseInt(rowNumberStr);
//		String barCode = ConvertUtil.getString(rowMap.get("barCode"));
		String unitCode = ConvertUtil.getString(rowMap.get("unitCode"));
		String prtName = ConvertUtil.getString(rowMap.get("prtName"));
		String salePrice = ConvertUtil.getString(rowMap.get("salePrice"));
		String memPrice = ConvertUtil.getString(rowMap.get("memPrice"));
		
		//错误区分，记录该行数据是否有错误，默认为没有错误
		boolean errorFlag = false;
		
		StringBuffer errStr = new StringBuffer();
		
		// unitCode校验
		if (CherryConstants.BLANK.equals(unitCode)) {
			// 单元格为空
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1) });
		} else if (unitCode.length() > 20) {
			// 代码长度错误
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
		}else {
			rowMap.putAll(map);
			Map<String,Object> prtMap = binOLPTJCS16_Service.getProductInfo(rowMap);
			if(null == prtMap || prtMap.isEmpty()){
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00137",new String[] {
						CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
			}else {
				rowMap.put("productID", prtMap.get("BIN_ProductID"));
			}
			
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
					|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
				rowMap.put("salePrice",prtMap.get("salePrice"));
				rowMap.put("memPrice",prtMap.get("memPrice"));
			}	
		}
		
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_1.equals(config)){
			// 销售价格校验
			if(CherryConstants.BLANK.equals(salePrice)){
				salePrice = ProductConstants.DEF_PRICE;
			}else if (!CherryChecker.isFloatValid(salePrice, 16, 2)) {
				// 产品价格格式有误
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00034", new String[] { ProductConstants.DATE_SHEET_NAME, "C" + (rowNumber + 1) });
			}
			
			if(CherryConstants.BLANK.equals(memPrice)){
				memPrice = salePrice;
			}else if (!CherryChecker.isFloatValid(memPrice, 16, 2)) {
				// 产品价格格式有误
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00034", new String[] { ProductConstants.DATE_SHEET_NAME, "D" + (rowNumber + 1) });
			}
			
		} 
		
		if(errorFlag){
			errorList.add(rowMap);
		}
		return errorFlag;
	}

	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @param map
	 * @param errorList 存在有错误的行
	 * @return
	 * @throws Exception
	 * 返回值 true:校验不通过(有错误) ，false:校验通过
	 */
	private boolean checkData(Map<String, Object> rowMap,Map<String,Object> map,List<Map<String,Object>> errorList ) throws Exception {
		
		// 取得系统配置项产品方案添加模式
		String config = binOLCM14_BL.getConfigValue("1288", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		
		String rowNumberStr = ConvertUtil.getString(rowMap.get("rowNumber"));
		Integer rowNumber = Integer.valueOf(rowNumberStr);
		String unitCode = ConvertUtil.getString(rowMap.get("unitCode"));
		String prtName = ConvertUtil.getString(rowMap.get("prtName"));
		String salePrice = ConvertUtil.getString(rowMap.get("salePrice"));
		String memPrice = ConvertUtil.getString(rowMap.get("memPrice"));
		
		//错误区分，记录该行数据是否有错误，默认为没有错误
		boolean errorFlag = false;
		
		StringBuffer errStr = new StringBuffer();
		
		// unitCode校验
		if (CherryConstants.BLANK.equals(unitCode)) {
			// 单元格为空
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1) });
		} else if (unitCode.length() > 20) {
			// 代码长度错误
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
		}else {
			rowMap.putAll(map);
			Map<String,Object> prtMap = binOLPTJCS16_Service.getProductInfo(rowMap);
			if(null == prtMap || prtMap.isEmpty()){
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00137",new String[] {
						CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
			}else {
				rowMap.put("productID", prtMap.get("BIN_ProductID"));
			}
			
			if(ProductConstants.SOLU_ADD_MODE_CONFIG_2.equals(config) 
					|| ProductConstants.SOLU_ADD_MODE_CONFIG_3.equals(config)){
				rowMap.put("salePrice",prtMap.get("salePrice"));
				rowMap.put("memPrice",prtMap.get("memPrice"));
			}	
		}
		
		if(ProductConstants.SOLU_ADD_MODE_CONFIG_1.equals(config)){
			// 销售价格校验
			if(CherryConstants.BLANK.equals(salePrice)){
				salePrice = ProductConstants.DEF_PRICE;
			}else if (!CherryChecker.isFloatValid(salePrice, 16, 2)) {
				// 产品价格格式有误
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00034", new String[] { ProductConstants.DATE_SHEET_NAME, "C" + (rowNumber + 1) });
			}
			
			if(CherryConstants.BLANK.equals(memPrice)){
				memPrice = salePrice;
			}else if (!CherryChecker.isFloatValid(memPrice, 16, 2)) {
				// 产品价格格式有误
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00034", new String[] { ProductConstants.DATE_SHEET_NAME, "D" + (rowNumber + 1) });
			}
			
		} 
		
		if(errorFlag){
			errorList.add(rowMap);
		}
		return errorFlag;
	}
	
	/**
	 * 方案导入处理
	 * 
	 * @param importDataMap
	 *            导入的数据
	 * @param sessionMap
	 *            登录用户参数
	 * @return 导入的结果
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception {
		// 操作结果统计map
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		// 产品操作成功数
		int optCount = 0;
		// 产品添加成功数
		int addCount = 0;
		// 产品更新成功数
		int updCount = 0;
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(sessionMap);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		sessionMap.put("tVersion", tVersion);
		
		
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) importDataMap.get("detailList");
		
		for (Map<String, Object> prtMap : list) {
			
			// 方案ID
			prtMap.put("productPriceSolutionID", sessionMap.get("productPriceSolutionID"));
			prtMap.put("tVersion", tVersion);
			
			// 拼接priceJson
//			Map<String, Object> priceMap = new HashMap<String, Object>();
//			priceMap.put("salePrice", prtMap.get("salePrice"));
//			priceMap.put("memPrice", prtMap.get("memPrice"));
//			List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
//			prtList.add(priceMap);
//			String priceJson = JSONUtil.serialize(prtList);
//			prtMap.put("priceJson", priceJson);
			
			// Step1.1: 新明细插入到方案明细表
			// *Step2.1: 新明细插入到方案明细表
			prtMap.put("soluDetailValidFlag",CherryConstants.VALIDFLAG_ENABLE);
			Map<String, Object> mergeMap = binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
//			binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
			String actionResult = ConvertUtil.getString(mergeMap.get("actionResult"));
			
			if(!actionResult.isEmpty()){
				if("UPDATE".equals(actionResult)){
					String delValidFlag = ConvertUtil.getString(mergeMap.get("delValidFlag"));
					if(delValidFlag.equals(CherryConstants.VALIDFLAG_DISABLE)){
						// 这种情况是在方案明细中被逻辑删除的产品。
						++addCount;
					}else{
						++updCount;
					}
				}else if("INSERT".equals(actionResult)){
					++addCount;
				}
			}
			
			// 产品操作成功数
			++optCount;
		}
		
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, optCount);
		infoMap.put(ProductConstants.UPDCOUNT, updCount);
		infoMap.put(ProductConstants.ADDCOUNT, addCount);
		
		return infoMap;
	}	
	/**
	 * 方案导入处理
	 * 
	 * @param importDataMap
	 *            导入的数据
	 * @param sessionMap
	 *            登录用户参数
	 * @return 导入的结果
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> tran_excelHandleCnt(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception {
		// 操作结果统计map
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		// 产品操作成功数
		int optCount = 0;
		// 产品添加成功数
		int addCount = 0;
		// 产品更新成功数
		int updCount = 0;
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(sessionMap);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		sessionMap.put("tVersion", tVersion);
		
		
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) importDataMap.get("detailList");
		
		for (Map<String, Object> prtMap : list) {
			
			// 方案ID
			prtMap.put("productPriceSolutionID", sessionMap.get("productPriceSolutionID"));
			prtMap.put("tVersion", tVersion);
			
			// 拼接priceJson
//			Map<String, Object> priceMap = new HashMap<String, Object>();
//			priceMap.put("salePrice", prtMap.get("salePrice"));
//			priceMap.put("memPrice", prtMap.get("memPrice"));
//			List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
//			prtList.add(priceMap);
//			String priceJson = JSONUtil.serialize(prtList);
//			prtMap.put("priceJson", priceJson);
			
			// Step1.1: 新明细插入到方案明细表
			// *Step2.1: 新明细插入到方案明细表
			prtMap.put("soluDetailValidFlag",CherryConstants.VALIDFLAG_ENABLE);
			
			Map<String, Object> mergeMap = binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
		
//			binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
			String actionResult = ConvertUtil.getString(mergeMap.get("actionResult"));
			
			if(!actionResult.isEmpty()){
				if("UPDATE".equals(actionResult)){
					String delValidFlag = ConvertUtil.getString(mergeMap.get("delValidFlag"));
					if(delValidFlag.equals(CherryConstants.VALIDFLAG_DISABLE)){
						// 这种情况是在方案明细中被逻辑删除的产品。
						++addCount;
					}else{
						++updCount;
					}
				}else if("INSERT".equals(actionResult)){
					++addCount;
				}
			}
			
			// 产品操作成功数
			++optCount;
		}
		
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, optCount);
		infoMap.put(ProductConstants.UPDCOUNT, updCount);
		infoMap.put(ProductConstants.ADDCOUNT, addCount);
		
		return infoMap;
	}	
	/**
	 * 方案导入处理
	 * 
	 * @param importDataMap
	 *            导入的数据
	 * @param sessionMap
	 *            登录用户参数
	 * @return 导入的结果
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> tran_excelHandleOld2(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception {
		// 操作结果统计map
		Map<String, Object> infoMap = new HashMap<String, Object>();
//		// 产品操作成功数
//		infoMap.put(ProductConstants.OPTCOUNT, 0);
//		// 产品更新成功数
//		infoMap.put(ProductConstants.UPDCOUNT, 0);
//		// 产品添加成功数
//		infoMap.put(ProductConstants.ADDCOUNT, 0);
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(sessionMap);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		sessionMap.put("tVersion", tVersion);
		
		// *Step1：产品方案明细与原分配的柜台（区域、渠道、柜台）的柜台产品表记录无效掉validFlag= 0,version=tVeresion+1
		List<Map<String, Object>> oldSoluDetailProductDepartPriceList = binOLPTJCS17_Service.getSoluDetailProductDepartPriceList(sessionMap);
		if(null != oldSoluDetailProductDepartPriceList && !oldSoluDetailProductDepartPriceList.isEmpty()){
			for(Map<String, Object> itemMap : oldSoluDetailProductDepartPriceList){
				itemMap.put("soluStartDate", itemMap.get("StartDate"));
				itemMap.put("soluEndDate", itemMap.get("EndDate"));
				itemMap.putAll(sessionMap);
				// 地点类型
				String placeType = (String)itemMap.get("PlaceType");
				String saveJson = (String)itemMap.get("SaveJson");
				List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				
				// 区域城市
				if(placeType.equals(PromotionConstants.LOTION_TYPE_REGION)){
					// 取得权限区域城市的柜台
					itemMap.put("city", 1);
					itemMap.put("cityList", saveJsonList);
					List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(itemMap);
					if(null != cntList && !cntList.isEmpty()){
						for(Map<String,Object> cntMap : cntList){
							itemMap.putAll(cntMap);
							// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
							binOLPTJCS17_Service.updProductDepart(itemMap);
							// Step1.2: 将方案原关联的柜台对应的部门产品价格删除。
							binOLPTJCS17_Service.delProductDepartPrice(itemMap);
						}
					}
				}
				// 按区域并且指定柜台 或 按渠道并且指定柜台
				else if (placeType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
						|| placeType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
					
					for(String cntCode : saveJsonList){
						itemMap.put("CounterCode", cntCode);
						// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
						binOLPTJCS17_Service.updProductDepart(itemMap);
						// Step1.2: 将方案原关联的柜台对应的部门产品价格删除。
						binOLPTJCS17_Service.delProductDepartPrice(itemMap);
					}
				}
				
				// 渠道
				else if(placeType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)){
					// 取得权限渠道的柜台
					itemMap.put("channel", 1);
					itemMap.put("channelList", saveJsonList);
					List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(itemMap);
					if(null != channelList && !channelList.isEmpty()){
						for(Map<String,Object> channelMap : channelList){
							itemMap.putAll(channelMap);
							// Step1.1：将方案原关联的柜台对应的柜台产品记录无效掉
							binOLPTJCS17_Service.updProductDepart(itemMap);
							// Step1.2：将方案原关联的柜台对应的部门产品价格删除。
							binOLPTJCS17_Service.delProductDepartPrice(itemMap);
						}
					}
				}
				
			}
		}
		
		
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) importDataMap.get("detailList");
		
		// Step1: 产品方案中原有数据删除
		// *Step2: 产品方案中原有数据删除
		binOLPTJCS16_Service.delPrtPriceSoluDetail(sessionMap);
		
		for (Map<String, Object> prtMap : list) {
			
			// 方案ID
			prtMap.put("productPriceSolutionID", sessionMap.get("productPriceSolutionID"));
			
			// 拼接priceJson
			Map<String, Object> priceMap = new HashMap<String, Object>();
			priceMap.put("salePrice", prtMap.get("salePrice"));
			priceMap.put("memPrice", prtMap.get("memPrice"));
//			List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
//			prtList.add(priceMap);
//			String priceJson = JSONUtil.serialize(prtList);
//			prtMap.put("priceJson", priceJson);

			// Step1.1: 新明细插入到方案明细表
			// *Step2.1: 新明细插入到方案明细表
			binOLPTJCS16_Service.insertPrtPriceSoluDetail(prtMap);
//			binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
			
		}
		
		/*  由*Step1代替
		// Step2: 将方案原关联的柜台对应的柜台产品记录无效掉。
		binOLPTJCS16_Service.updProductDepart(sessionMap);
		// Step2.1: 将方案原关联的柜台对应的部门产品价格删除。
		binOLPTJCS16_Service.delProductDepartPrice(sessionMap);
		*/
		
		// *Step3: 方案明细与新分配柜台（区域、渠道、柜台）对应的柜台产品 merge 到柜台产品表
		List<Map<String, Object>> newSoluDetailProductDepartPriceList = binOLPTJCS17_Service.getSoluDetailProductDepartPriceList(sessionMap);
		if(null != newSoluDetailProductDepartPriceList && !newSoluDetailProductDepartPriceList.isEmpty()){
			for(Map<String, Object> newItemMap : newSoluDetailProductDepartPriceList){
				newItemMap.put("soluStartDate", newItemMap.get("StartDate"));
				newItemMap.put("soluEndDate", newItemMap.get("EndDate"));
				newItemMap.putAll(sessionMap);
				// 地点类型
				String placeType = (String)newItemMap.get("PlaceType");
				String saveJson = (String)newItemMap.get("SaveJson");
				List<String> saveJsonList = (List<String>) JSONUtil.deserialize(saveJson);
				
				// 区域城市
				if(placeType.equals(PromotionConstants.LOTION_TYPE_REGION)){
					// 取得权限区域城市的柜台
					newItemMap.put("city", 1);
					newItemMap.put("cityList", saveJsonList);
					List<Map<String, Object>> cntList = binOLPTJCS17_Service.getCounterInfoList(newItemMap);
					if(null != cntList && !cntList.isEmpty()){
						for(Map<String,Object> cntMap : cntList){
							newItemMap.putAll(cntMap);
							// 设置部门产品表相关属性(status、validFlag)
//							binOLPTJCS17_IF.setProductDepart(newItemMap);
							
							// Step3.1: 插入产品部门表
							binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
							
							// Step3.2: 插入柜台价格到产品价格表中
							newItemMap.put("type", "3"); // 价格类型为部门价格
							newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
							newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
							newItemMap.put("departCode", newItemMap.get("CounterCode")); 
							optPriceNew(newItemMap);
						}
					}
				}
				// 按区域并且指定柜台 或 按渠道并且指定柜台
				else if (placeType.equals(PromotionConstants.LOTION_TYPE_CHANNELS_COUNTER)
						|| placeType.equals(PromotionConstants.LOTION_TYPE_REGION_COUNTER)) {
					
					for(String cntCode : saveJsonList){
						newItemMap.put("CounterCode", cntCode);
						// 设置部门产品表相关属性(status、validFlag)
//						binOLPTJCS17_IF.setProductDepart(newItemMap);
						
						// Step3.1: 插入产品部门表
						binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
						
						// Step3.2: 插入柜台价格到产品价格表中
						newItemMap.put("type", "3"); // 价格类型为部门价格
						newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
						newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
						newItemMap.put("departCode", newItemMap.get("CounterCode")); 
						optPriceNew(newItemMap);
					}
				}
				
				// 渠道
				else if(placeType.equals(PromotionConstants.LOTION_TYPE_CHANNELS)){
					// 取得权限渠道的柜台
					newItemMap.put("channel", 1);
					newItemMap.put("channelList", saveJsonList);
					List<Map<String, Object>> channelList = binOLPTJCS17_Service.getCounterInfoList(newItemMap);
					if(null != channelList && !channelList.isEmpty()){
						for(Map<String,Object> channelMap : channelList){
							newItemMap.putAll(channelMap);
							// 设置部门产品表相关属性(status、validFlag)
//							binOLPTJCS17_IF.setProductDepart(newItemMap);
							
							// Step3.1: 插入产品部门表
							binOLPTJCS17_Service.mergeProductDepartInfo(newItemMap);
							
							// Step3.2: 插入柜台价格到产品价格表中
							newItemMap.put("type", "3"); // 价格类型为部门价格
							newItemMap.put("priceJson", newItemMap.get("PriceJson")); 
							newItemMap.put("productId", newItemMap.get("BIN_ProductID")); 
							newItemMap.put("departCode", newItemMap.get("CounterCode")); 
							optPriceNew(newItemMap);
						}
					}
				}
				
				
			}
		}
		
//		// Step3: 新方案明细与关联柜台对应后 megre到柜台产品表。	
//		List<Map<String, Object>> newSoluDetailProductDepartPriceList = binOLPTJCS16_Service.getNewSoluDetailProductDepartPriceList(sessionMap);
//		if(null != newSoluDetailProductDepartPriceList && !newSoluDetailProductDepartPriceList.isEmpty()){
//			for(Map<String, Object> itemMap : newSoluDetailProductDepartPriceList){
//				
//				itemMap.putAll(sessionMap);
//				
//				/*
//				 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
//					1、先判断validFlag数值 如果停用直接使用停用
//					2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用
//
//				终端对product_SCS status定义：
//				E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效	
//				D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态	
//				新增加下面两种状态：	
//				H	表示	产品下柜	下发到witpos_xx数据库后都归为停用	
//				G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用
//				
//				新后台对product_SCS status定义：
//				E：正常销售、D：停用、H：下柜、G：未启用
//				*/
//				
//				String prtscs_status = "E"; // Product_SCS 状态值,
//				String pdValidFlag = "1"; // 部门产品表的有效状态
//				
//				String ValidFlag = ConvertUtil.getString(itemMap.get("ValidFlag"));
//				String prtStatus = ConvertUtil.getString(itemMap.get("Status")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
//				String sellDateFlag = ConvertUtil.getString(itemMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
//				
//				if(CherryConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
//					// 产品无效
//					prtscs_status = "D";
//					pdValidFlag = "0";
//				}else{
//					// 产品有效
//					pdValidFlag = "1";
//					
//					if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
//						// 产品下柜
//						prtscs_status = "H";
//					}else {
//						//
//						if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
//							// 产品未启用
//							prtscs_status = "G";
//						}else{
//							// 有效，正常销售
//							prtscs_status = "E";
//						}
//					}
//					
//				}
//				itemMap.put("prtscs_status", prtscs_status);
//				itemMap.put("pdValidFlag", pdValidFlag);
//				
//				// Step3.1: 插入产品部门表
//				binOLPTJCS16_Service.mergeProductDepartInfo(itemMap);
//				
//				// Step3.2: 插入柜台价格到产品价格表中
//				itemMap.put("type", "3"); // 价格类型为部门价格
//				itemMap.put("priceJson", itemMap.get("PriceJson")); 
//				itemMap.put("productId", itemMap.get("BIN_ProductID")); 
//				itemMap.put("departCode", itemMap.get("CounterCode")); 
//				optPriceNew(itemMap);
//			}
//		} 
		
		// 产品操作成功数
//		int optCount = Integer.valueOf(String.valueOf(infoMap.get(ProductConstants.OPTCOUNT)));
//		infoMap.put(ProductConstants.OPTCOUNT, ++optCount);
		return infoMap;
	}	
	/**
	 * 方案导入处理
	 * 
	 * @param importDataMap
	 *            导入的数据
	 * @param sessionMap
	 *            登录用户参数
	 * @return 导入的结果
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> tran_excelHandleOld(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception {
		// 操作结果统计map
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, 0);
		// 产品更新成功数
		infoMap.put(ProductConstants.UPDCOUNT, 0);
		// 产品添加成功数
		infoMap.put(ProductConstants.ADDCOUNT, 0);
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(sessionMap);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		sessionMap.put("tVersion", tVersion);
		
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) importDataMap.get("detailList");
		
		
		// Step1: 产品方案中原有数据删除
		binOLPTJCS16_Service.delPrtPriceSoluDetail(sessionMap);
		
		for (Map<String, Object> prtMap : list) {
			
			// 方案ID
			prtMap.put("productPriceSolutionID", sessionMap.get("productPriceSolutionID"));
			
			// 拼接priceJson
			Map<String, Object> priceMap = new HashMap<String, Object>();
			priceMap.put("salePrice", prtMap.get("salePrice"));
			priceMap.put("memPrice", prtMap.get("memPrice"));
			List<Map<String, Object>> prtList = new ArrayList<Map<String,Object>>();
			prtList.add(priceMap);
			String priceJson = JSONUtil.serialize(prtList);
			prtMap.put("priceJson", priceJson);
			
			// Step1.1: 新明细插入到方案明细表
//			binOLPTJCS16_Service.insertPrtPriceSoluDetail(prtMap);
			binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
			
		}
		
		// Step2: 将方案原关联的柜台对应的柜台产品记录无效掉。
		binOLPTJCS16_Service.updProductDepart(sessionMap);
		// Step2.1: 将方案原关联的柜台对应的部门产品价格删除。
		binOLPTJCS16_Service.delProductDepartPrice(sessionMap);
		
		// Step3: 新方案明细与关联柜台对应后 megre到柜台产品表。	
		List<Map<String, Object>> newSoluDetailProductDepartPriceList = binOLPTJCS16_Service.getNewSoluDetailProductDepartPriceList(sessionMap);
		if(null != newSoluDetailProductDepartPriceList && !newSoluDetailProductDepartPriceList.isEmpty()){
			for(Map<String, Object> itemMap : newSoluDetailProductDepartPriceList){
				
				itemMap.putAll(sessionMap);
				
				/*
				 处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
					1、先判断validFlag数值 如果停用直接使用停用
					2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

				终端对product_SCS status定义：
				E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效	
				D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态	
				新增加下面两种状态：	
				H	表示	产品下柜	下发到witpos_xx数据库后都归为停用	
				G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用
				
				新后台对product_SCS status定义：
				E：正常销售、D：停用、H：下柜、G：未启用
				 */
				
				String prtscs_status = "E"; // Product_SCS 状态值,
				String pdValidFlag = "1"; // 部门产品表的有效状态
				
				String ValidFlag = ConvertUtil.getString(itemMap.get("ValidFlag"));
				String prtStatus = ConvertUtil.getString(itemMap.get("Status")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
				String sellDateFlag = ConvertUtil.getString(itemMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
				
				if(CherryConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
					// 产品无效
					prtscs_status = "D";
					pdValidFlag = "0";
				}else{
					// 产品有效
					pdValidFlag = "1";
					
					if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
						// 产品下柜
						prtscs_status = "H";
					}else {
						//
						if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
							// 产品未启用
							prtscs_status = "G";
						}else{
							// 有效，正常销售
							prtscs_status = "E";
						}
					}
					
				}
				itemMap.put("prtscs_status", prtscs_status);
				itemMap.put("pdValidFlag", pdValidFlag);
				
				// Step3.1: 插入产品部门表
				binOLPTJCS16_Service.mergeProductDepartInfo(itemMap);
				
				// Step3.2: 插入柜台价格到产品价格表中
				itemMap.put("type", "3"); // 价格类型为部门价格
				itemMap.put("priceJson", itemMap.get("PriceJson")); 
				itemMap.put("productId", itemMap.get("BIN_ProductID")); 
				itemMap.put("departCode", itemMap.get("CounterCode")); 
				optPriceNew(itemMap);
			}
		}
		
		// 产品操作成功数
		int optCount = Integer.valueOf(String.valueOf(infoMap.get(ProductConstants.OPTCOUNT)));
		infoMap.put(ProductConstants.OPTCOUNT, ++optCount);
		return infoMap;
	}	
	
	/**
	 * 取得产品价格方案List
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map) {
		return binOLPTJCS16_Service.getPrtPriceSolutionList(map);
	}
	
	/**
	 * 价格处理
	 * @param prtSoluMap
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private void optPriceNew(Map<String, Object> prtSoluMap) throws JSONException{
		// 价格
		String priceInfo = ConvertUtil.getString(prtSoluMap.get("priceJson"));
		
		if (!CherryConstants.BLANK.equals(priceInfo)) {
			List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(priceInfo);
			
			// 价格按折扣率计算
			setPriceRate(priceInfoList, prtSoluMap);
			
			if (null != priceInfoList) {
				int size = priceInfoList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> priceMap = priceInfoList.get(i);
					priceMap = CherryUtil.removeEmptyVal(priceMap);
					// 操作
					String option = ConvertUtil.getString(priceMap.get(ProductConstants.OPTION));
					
					priceMap.putAll(prtSoluMap);
					priceMap.put(ProductConstants.PRICESTARTDATE, prtSoluMap.get("soluStartDate"));
					priceMap.put(ProductConstants.PRICEENDDATE, prtSoluMap.get("soluEndDate"));
					
					if (CherryConstants.BLANK.equals(option) || ProductConstants.OPTION_1.equals(option)) {
						// 插入产品销售价格
						binOLPTJCS16_Service.insertProductPrice(priceMap);
					} 
				}
			}
		}
		
	}
	
	/**
	 * 价格按折扣率计算
	 * @param priceInfoList
	 * @param map
	 */
	private void setPriceRate(List<Map<String, Object>> priceInfoList,Map<String,Object> map){
		
		String priceMode = ConvertUtil.getString(map.get("priceMode")); 
		
		if("2".equals(priceMode)){
			float saleRate = Float.parseFloat(ConvertUtil.getString(map.get("saleRate"))); 
			for(Map<String, Object> priceInfoMap : priceInfoList){
				float salePrice = Float.parseFloat(ConvertUtil.getString(priceInfoMap.get("salePrice")));
				salePrice = salePrice*saleRate/100;
				BigDecimal sp = new BigDecimal(salePrice);
				salePrice = sp.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				priceInfoMap.put("salePrice", salePrice);
			}
		}
	}
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public String getBussinessDate(Map<String, Object> map) {
		return binOLPTJCS14_Service.getBusDate(map);
	}
	

}
