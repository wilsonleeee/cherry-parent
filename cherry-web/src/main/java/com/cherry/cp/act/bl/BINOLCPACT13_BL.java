/*	
 * @(#)BINOLCPACT13_BL.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.act.interfaces.BINOLCPACT13_IF;
import com.cherry.cp.act.service.BINOLCPACT13_Service;

/**
 * 活动产品库存导入BL
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT13_BL implements BINOLCPACT13_IF {

	@Resource(name="binOLCPACT13_Service")
	private BINOLCPACT13_Service binOLCPACT13_Service;
	
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT13_BL.class);

	@Override
	public List<Map<String, Object>> resolveExcel(Map<String, Object> map) throws Exception {
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
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 数据sheet
		Sheet dateSheet = null;
		for (int i=0; i<sheets.length;i++) {
			Sheet st = sheets[i];
			String sheetName = st.getName().trim();
			String[] sheetNameStrings = null;
			if(i == 0){
				sheetNameStrings = sheetName.split("_");
				if((sheetNameStrings == null || sheetNameStrings.length == 1)) {
					// 模板版本不正确，请下载最新的模板进行导入！
					throw new CherryException("EBS00103");
				} else {
					if(!sheetNameStrings[1].equals(CherryConstants.CAMPAIGNSTOCK_EXCEL_VERSION)){
						// 模板版本不正确，请下载最新的模板进行导入！
						throw new CherryException("EBS00103");
					}
				}
			} else {
				if (CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME.equals(sheetName)) {
					dateSheet = st;
				}
			}
		}
		// 数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("MBM00042",
					new String[] { CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME});
		}
		int sheetLength = dateSheet.getRows();
		// 导入成功结果信息List
		List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
		// 逐行遍历Excel
		for (int r = 1; r < sheetLength; r++) {
			Map<String, Object> stockInfoMap = new HashMap<String, Object>();
			// 活动号
			String subCampCode = dateSheet.getCell(0, r).getContents().trim();
			stockInfoMap.put("subCampCode", subCampCode);
			// 活动名称
			String subCampaignName = dateSheet.getCell(1, r).getContents().trim();
			stockInfoMap.put("subCampaignName", subCampaignName);
			// 柜台号
			String counterCode = dateSheet.getCell(2, r).getContents().trim();
			stockInfoMap.put("counterCode", counterCode);
			// 柜台名称
			String counterName = dateSheet.getCell(3, r).getContents().trim();
			stockInfoMap.put("counterName", counterName);
			// 产品厂商编码
			String unitCode = dateSheet.getCell(4, r).getContents().trim();
			stockInfoMap.put("unitCode", unitCode);
			// 产品条码
			String barCode = dateSheet.getCell(5, r).getContents().trim();
			stockInfoMap.put("barCode", barCode);
			// 产品名称
			String productName = dateSheet.getCell(6, r).getContents().trim();
			stockInfoMap.put("productName", productName);
			// 分配数量
			String totalQuantity = dateSheet.getCell(7, r).getContents().trim();
			stockInfoMap.put("totalQuantity", totalQuantity);
			// 安全数量
			String safeQuantity = dateSheet.getCell(8, r).getContents().trim();
			stockInfoMap.put("safeQuantity", safeQuantity);
			// 行号，用于标记错误信息位置
			stockInfoMap.put("rowNumber", r+1);
			stockInfoMap.putAll(map);
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(subCampCode)
				&& CherryChecker.isNullOrEmpty(subCampaignName)
				&& CherryChecker.isNullOrEmpty(counterCode)
				&& CherryChecker.isNullOrEmpty(counterName)
				&& CherryChecker.isNullOrEmpty(totalQuantity)
				&& CherryChecker.isNullOrEmpty(safeQuantity)){
					break;
			}
			
			resulList.add(stockInfoMap);
		}
		// 没有数据，不操作
		if (resulList == null || resulList.isEmpty()) {
			// sheet单元格没有数据，请核查后再操作！
			throw new CherryException("MBM00038",
					new String[] { dateSheet.getName() });
		}
		/***============校验数据不能重复=================*/
		Map<String, Object> tempMap = new HashMap<String, Object>();
		for(Map<String, Object> detailMap : resulList) {
			this.validCampaignStockExcel(detailMap);
			/**=====重复数据不进行合并，因为无中间表存储这些信息，合并数据不可控=====**/
			String uniqueKey = ConvertUtil.getString(detailMap.get("uniqueKey"));
			String currentRowNumber = ConvertUtil.getString(detailMap.get("rowNumber"));
			if(!tempMap.containsKey(uniqueKey)) {
				tempMap.put(uniqueKey, currentRowNumber);
			} else {
				String oldRowNumber = ConvertUtil.getString(tempMap.get(uniqueKey));
				// {0}sheet第{1}行与第{2}行数据重复，请确认是否数据正确！
				throw new CherryException("ECP00034", new String[] {
						CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
						currentRowNumber, oldRowNumber });
			}
		}
		return resulList;
	}
	
	@Override
	public void tran_saveImportCampaignStock(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception {
		try {
			for(Map<String, Object> detailMap : list) {
				binOLCPACT13_Service.mergeCampaignStock(detailMap);
			}
		} catch(Exception e) {
			// 导入失败，系统发生异常错误！
			CherryException CherryException = new CherryException("MBM00036");
			// 更新失败的场合，打印错误日志
			logger.error(e.getMessage(), e);
			throw CherryException;
		}
	}
	
	/**
	 * Excel数据验证并补充数据
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public void validCampaignStockExcel(Map<String, Object> stockMap) 
			throws Exception {
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.putAll(stockMap);
		String rowNumber = ConvertUtil.getString(checkMap.get("rowNumber"));

		/** ==== ------------- 活动号验证 ----------------====== */
		String subCampCode = ConvertUtil.getString(stockMap.get("subCampCode"));
		String subCampaignName = ConvertUtil.getString(stockMap
				.get("subCampaignName"));
		// 根据活动号查询活动信息，校验此活动是否已经存在，不存在的报错，存在的将所对应的主题活动拿到
		Map<String, Object> dbCampaignInfo = binOLCPACT13_Service
				.getSubCampaignInfo(checkMap);
		if (CherryChecker.isNullOrEmpty(subCampCode)) {
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"A" + rowNumber });
		} else if (subCampCode.length() > 22) {
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"A" + rowNumber, "22" });
		} else if (null == dbCampaignInfo || dbCampaignInfo.isEmpty()) {
			// 导入的活动号不存在[{0}sheet单元格[{1}]数据无效或无权限操作！]
			throw new CherryException("EBS00032", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"A" + rowNumber });
		} else if (!"".equals(subCampaignName)) {
			// 活动号正确的情况下才去判断活动名称
			if (!subCampaignName.equals(dbCampaignInfo.get("subCampaignName"))) {
				// 柜台号要么为空，要么是柜台号对应的柜台名称
				// {0}sheet单元格[{1}]数据有误,请确认是否为活动号对应的名称！
				throw new CherryException("ECP00030", new String[] {
						CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
						"B" + rowNumber });
			}
		}
		// 子活动对应的主题活动CODE
		stockMap.put("campaignCode", dbCampaignInfo.get("campaignCode"));

		/** =====================校验柜台========================== */
		String counterCode = ConvertUtil.getString(stockMap.get("counterCode"));
		String counterName = ConvertUtil.getString(stockMap.get("counterName"));
		// 数据库对应的柜台信息
		Map<String, Object> dbCounterInfo = binOLCPACT13_Service
				.getCounterInfo(checkMap);

		// 判断柜台号与柜台名称【柜台号必填，柜台名称要么为空，否则必须是柜台号对应的柜台名称】
		if ("".equals(counterCode)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"C" + rowNumber });
		} else if (counterCode.length() > 15) {
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"C" + rowNumber, "15" });
		} else if (CherryChecker.isNullOrEmpty(dbCounterInfo)) {
			// 导入柜台不存在[{0}sheet单元格[{1}]数据无效或无权限操作！]
			throw new CherryException("EBS00032", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"C" + rowNumber });
		} else if (!"".equals(counterName)) {
			// 柜台号正确的情况下才去判断柜台名称
			if (!counterName.equals(dbCounterInfo.get("counterName"))) {
				// 柜台号要么为空，要么是柜台号对应的柜台名称
				// {0}sheet单元格[{1}]数据有误,请确认是否为柜台号对应的名称！
				throw new CherryException("EMO00083", new String[] {
						CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
						"D" + rowNumber });
			}
		}
		// 柜台对应的组织结构ID
		stockMap.put("organizationID", dbCounterInfo.get("organizationID"));

		/** ================= 校验产品 ========================== */
//		String productName = ConvertUtil.getString(stockMap.get("productName"));
//		if (CherryChecker.isNullOrEmpty(productName)) {
//			// 单元格为空
//			throw new CherryException("EBS00031", new String[] {
//					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
//					"G" + rowNumber });
//		} else if (productName.length() > 50) {
//			// 长度错误
//			throw new CherryException("EBS00033", new String[] {
//					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
//					"G" + rowNumber, "50" });
//		}
		String unitCode = ConvertUtil.getString(stockMap.get("unitCode"));
		String barCode = ConvertUtil.getString(stockMap.get("barCode"));
		if (barCode != null && barCode.length() > 13) {
			// 长度错误----------产品条码
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"F" + rowNumber, "13" });
		}
		// 校验产品unitCode
		if (CherryChecker.isNullOrEmpty(unitCode)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"E" + rowNumber });
		} else if (unitCode.length() > 20) {
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"E" + rowNumber, "20" });
		} else {
			List<Map<String, Object>> prtInfoList = binOLCPACT13_Service
					.getPrtInfo(checkMap);
			if (prtInfoList == null || prtInfoList.size() == 0) {
				// {0}sheet单元格[{1}]数据无效或无权限操作！-----厂商编码
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
						"E" + rowNumber });
			} else {
				// 产品厂商ID
				stockMap.put("productVendorId",
						prtInfoList.get(0).get("BIN_ProductVendorID"));
				stockMap.put("prtType", prtInfoList.get(0).get("PrtType"));
				if (prtInfoList.size() > 1) {
					// 一品多码
					if (CherryChecker.isNullOrEmpty(barCode)) {
						// 单元格为空
						throw new CherryException(
								"EBS00031",
								new String[] {
										CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
										"F" + rowNumber });
					} else {
						int index = indexOfBarCode(prtInfoList, barCode);
						if (index == -1) {
							// {0}sheet单元格[{1}]数据有误,请确认商品条码是否正确！
							throw new CherryException("ECP00031",
									new String[] {CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
													"F" + rowNumber });
						} else {
							stockMap.put("productVendorId",
									prtInfoList.get(index).get("BIN_ProductVendorID"));
							stockMap.put("prtType", prtInfoList.get(index).get("PrtType"));
						}
					}
				} else {
					if (CherryChecker.isNullOrEmpty(barCode)
							|| barCode
									.equals(prtInfoList.get(0).get("BarCode"))) {
						stockMap.put("productVendorId",
								prtInfoList.get(0).get("BIN_ProductVendorID"));
						stockMap.put("prtType", prtInfoList.get(0).get("PrtType"));
					} else {
						// {0}sheet单元格[{1}]数据有误,请确认商品条码是否正确！
						throw new CherryException("ECP00031",
								new String[] {CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
											"F" + rowNumber });
					}
				}
			}
		}

		/*** ================校验分配数量与安全数量=================== **/
		String totalQuantity = ConvertUtil.getString(stockMap
				.get("totalQuantity"));
		String safeQuantity = ConvertUtil.getString(stockMap
				.get("safeQuantity"));
		// 分配数量
		if (CherryChecker.isNullOrEmpty(totalQuantity, true)) {
			// 分配数量为空，默认为0
			stockMap.put("totalQuantity", 0);
		} else if (!CherryChecker.isPositiveAndNegative(totalQuantity)
				|| totalQuantity.length() > 9) {
			// {0}sheet单元格[{1}]数据有误,请确认【分配数量】必须为正数且小于9位数！
			throw new CherryException("ECP00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"H" + rowNumber, PropertiesUtil.getText("PCP00055") });
		} else {
			stockMap.put("totalQuantity", totalQuantity);
		}

		// 安全数量
		if (CherryChecker.isNullOrEmpty(safeQuantity, true)) {
			// 安全数量为空时默认为0
			stockMap.put("safeQuantity", 0);
		} else if (!CherryChecker.isPositiveAndNegative(safeQuantity)
				|| safeQuantity.length() > 9) {
			// {0}sheet单元格[{1}]数据有误,请确认【安全数量】必须为正数且小于9位数！
			throw new CherryException("ECP00033", new String[] {
					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
					"I" + rowNumber, PropertiesUtil.getText("PCP00054") });
//		} else if (ConvertUtil.getInt(safeQuantity) > ConvertUtil
//				.getInt(totalQuantity)) {
//			// {0}sheet单元格[{1}]数据有误,请确认安全数量必须小于分配数量！
//			throw new CherryException("ECP00032", new String[] {
//					CherryConstants.CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME,
//					"I" + rowNumber });
		} else {
			stockMap.put("safeQuantity", safeQuantity);
		}
		
		/**=============增加一个字段用于标识数据===============**/
		String uniqueKey = subCampCode+counterCode+unitCode+barCode;
		stockMap.put("uniqueKey", uniqueKey);
		
	}
	
	/**
	 * 一品多码时，判断某个编码是否存在
	 * @param prtInfoList
	 * @param barCode
	 * @return
	 */
	public int indexOfBarCode(List<Map<String, Object>> prtInfoList, String barCode) {
		for(int index = 0; index < prtInfoList.size(); index++){
			Map<String, Object> map = prtInfoList.get(index);
			if(barCode.equals(map.get("BarCode"))){
				return index;
			}
		}
		return -1;
	}

}
