/*
 * @(#)BINOLSSPRM66_BL.java     1.0 2013/09/17
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
package com.cherry.ss.prm.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.bl.BINOLSSCM09_BL;
import com.cherry.ss.prm.interfaces.BINOLSSPRM66_IF;
import com.cherry.ss.prm.service.BINOLSSPRM66_Service;
import com.cherry.st.ios.interfaces.BINOLSTIOS09_IF;

/**
 * 
 * 入库导入BL
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM66_BL implements BINOLSSPRM66_IF {
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM66_BL.class);
	
	@Resource(name="binOLSSPRM66_Service")
	private BINOLSSPRM66_Service binOLSSPRM66_Service;
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_BL binOLCM18_BL;
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="binOLSSCM09_BL")
	private BINOLSSCM09_BL binOLSSCM09_BL;
	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binOLSSCM00_BL;
	@Resource(name="binOLSTIOS09_BL")
	private BINOLSTIOS09_IF binOLSTIOS09_BL;

	/**
	 * 获取导入数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> resolveExcel(Map<String, Object> map) throws Exception {
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
		// 入库数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.PRMINDEPOT_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 入库数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030", new String[] { CherryConstants.PRMINDEPOT_SHEET_NAME });
		}
		// 入库数据sheet的行数
		int sheetLength = dateSheet.getRows();
		// sheet中的数据
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
		//是否从Excel中获取导入批次号
		String isChecked = ConvertUtil.getString(map.get("isChecked"));
		// 从Excel获取导入批次号
		String importBatchCode = dateSheet.getCell(1, 0).getContents().trim();
		//导入批次号校验
		if(!CherryChecker.isNullOrEmpty(isChecked, true) && "checked".equals(isChecked)){
			//空校验
			if(CherryChecker.isNullOrEmpty(importBatchCode, true)){
				throw new CherryException("ECM00009",new String[]{PropertiesUtil.getText("STM00012")});
			}
			String currentImportBatchCode = PropertiesUtil.getText("STM00029")+"【" + importBatchCode+"】！";
			map.put("currentImportBatchCode", currentImportBatchCode);
			if(!CherryChecker.isAlphanumeric(importBatchCode)){
				//数据格式校验
				throw new CherryException("ECM00031",new String[]{currentImportBatchCode+PropertiesUtil.getText("STM00012")});
			}else if(importBatchCode.length() > 25){
				//长度校验
				throw new CherryException("ECM00020",new String[]{currentImportBatchCode+PropertiesUtil.getText("STM00012"),"25"});
			}else {
				//重复校验
				map.put("importBatchCodeR", importBatchCode);
				if(binOLSTIOS09_BL.getImportBatchCount(map) > 0){
					throw new CherryException("ECM00032",new String[]{PropertiesUtil.getText("STM00012")+"("+importBatchCode+")"});
				}
				map.put("ImportBatchCode", importBatchCode);
			}
		}else{
			importBatchCode = ConvertUtil.getString(map.get("ImportBatchCode"));
			String currentImportBatchCode = PropertiesUtil.getText("STM00029")+"【" + importBatchCode+"】！";
			map.put("currentImportBatchCode", currentImportBatchCode);
		}
		
		int r = 3;
		for (r = 3; r < sheetLength; r++) {
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			// 产品厂商编码
			String excelUnitCode = dateSheet.getCell(0, r).getContents().trim();
			rowMap.put("excelUnitCode", excelUnitCode);
			// 产品条码
			String excelBarCode = dateSheet.getCell(1, r).getContents().trim();
			rowMap.put("excelBarCode", excelBarCode);
			// 产品名称
			String excelProductName = dateSheet.getCell(2, r).getContents().trim();
			rowMap.put("excelProductName", excelProductName);
			// 部门编码
			String excelDepartCode = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("excelDepartCode", excelDepartCode);
			// 部门名称
			String excelDepartName = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("excelDepartName", excelDepartName);
			// 入库日期
			String excelInvoiceDate = dateSheet.getCell(5, r).getContents().trim();
			rowMap.put("excelInvoiceDate", excelInvoiceDate);
			// 产品数量
			String excelQuantity = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("excelQuantity", excelQuantity);
			// 逻辑仓库名称
			String excelLogicInventoryName = dateSheet.getCell(7, r).getContents().trim();
			rowMap.put("excelLogicInventoryName", excelLogicInventoryName);
			//行编号
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelUnitCode) && CherryChecker.isNullOrEmpty(excelBarCode)
					&& CherryChecker.isNullOrEmpty(excelProductName) && CherryChecker.isNullOrEmpty(excelDepartCode)
					&& CherryChecker.isNullOrEmpty(excelDepartName) && CherryChecker.isNullOrEmpty(excelInvoiceDate)
					&& CherryChecker.isNullOrEmpty(excelQuantity)
					&& CherryChecker.isNullOrEmpty(excelLogicInventoryName)) {
				break;

			}
			//基本格式校验
			this.checkData(rowMap);
			String importRepeat = ConvertUtil.getString(map.get("importRepeat"));
			if(!CherryChecker.isNullOrEmpty(importRepeat, true) && "0".equals(importRepeat)){
				if(isRepeatData(rowMap,map)){
					throw new CherryException("STM00028",new String[] { ConvertUtil.getString(r+1) });
				}
			}
			// 根据部门code、入库日期、逻辑仓库拆分为单据
			String importDatasKey = excelDepartCode + excelInvoiceDate + excelLogicInventoryName;
			if (importDatas.containsKey(importDatasKey)) {
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) importDatas.get(importDatasKey);
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			} else {
				List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			}
		}
		if (importDatas == null || importDatas.isEmpty()) {
			throw new CherryException("EBS00035", new String[] { CherryConstants.PRMINDEPOT_SHEET_NAME });
		}
		return importDatas;
	}

	/**
	 * 导入数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap, Map<String, Object> sessionMap)
			throws Exception {
		// 导入日期
		String sysDate = binOLSSPRM66_Service.getSYSDate();
		sessionMap.put("ImportDate", sysDate);
		// 插入导入批次信息返回导入批次ID
		int importBatchId = binOLSTIOS09_BL.insertImportBatch(sessionMap);
		// 导入原因不写入入库单据表
		sessionMap.remove("Comments");
		// 导入结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功的数量
		int successCount = 0;
		// 导入失败的数量
		int errorCount = 0;
		// 导入失败的单据
		List<Map<String, Object>> errorInDeportList = new ArrayList<Map<String, Object>>();
		Iterator it = importDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry en = (Entry) it.next();
			// 明细数据
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) en.getValue();
			// 验证数据的有效性
			Map<String, Object> detailMap = validExcel(detailList, sessionMap);
			detailList = (List<Map<String, Object>>) detailMap.get("resultDetailList");
			// 主数据
			Map<String, Object> mainMap = new HashMap<String, Object>();
			if (detailList != null && detailList.size() > 0) {
				mainMap = detailList.get(0);
				String billNo = getTicketNumber(sessionMap);
				mainMap.put("BillNo", billNo);
				mainMap.put("BillNoIF", billNo);
				mainMap.put("TotalQuantity", detailMap.get("TotalQuantity"));
				mainMap.put("TotalAmount", detailMap.get("TotalAmount"));
				mainMap.put("PreTotalQuantity", detailMap.get("TotalQuantity"));
				mainMap.put("PreTotalAmount", detailMap.get("TotalAmount"));
				mainMap.put("ImportDate", sysDate);
				mainMap.put("BIN_ImportBatchID", importBatchId);
				mainMap.put("ImportBatch", sessionMap.get("ImportBatchCode"));
			} else {
				continue;
			}
			if ((Boolean) detailMap.get("ResultFlag")) {
				// 验证不通过，只将数据添加到入库单（Excel导入）主表和明细表
				mainMap.put("ImportResult", "0");
				int BIN_PrmInDepotExcelID = binOLSSPRM66_Service.insertPrmInDepotExcel(mainMap);
				String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
				for (Map<String, Object> map : detailList) {
					map.put("BIN_PrmInDepotExcelID", BIN_PrmInDepotExcelID);
					map.put("ErrorMsg", totalErrorMsg + map.get("ErrorMsg"));
					binOLSSPRM66_Service.insertPrmInDepotDetailExcel(map);
					errorInDeportList.add(map);
				}
				errorCount++;
			} else {
				// 验证成功，合并相同产品明细
				Map<String, Object> detailListGroup = new HashMap<String, Object>();
				for (Map<String, Object> fm : detailList) {
					Map<String, Object> detailGroupMap = new HashMap<String, Object>();
					detailGroupMap.putAll(fm);
					String unitCode = ConvertUtil.getString(detailGroupMap.get("UnitCode")).trim();
					String barCode = ConvertUtil.getString(detailGroupMap.get("BarCode")).trim();
					// 以UnitCode与BarCode作为合并依据
					String mapKey = unitCode + barCode;
					if (detailListGroup.containsKey(mapKey)) {
						Map<String, Object> productMap = (Map<String, Object>) detailListGroup.get(mapKey);
						int quantity = ConvertUtil.getInt(detailGroupMap.get("Quantity"))
								+ ConvertUtil.getInt(productMap.get("Quantity"));
						productMap.put("Quantity", quantity);
						productMap.put("PreQuantity", quantity);
						detailListGroup.put(mapKey, productMap);
					} else {
						detailListGroup.put(mapKey, detailGroupMap);
					}
				}
				// 明细转换为List
				List<Map<String, Object>> inDepotdetailList = new ArrayList<Map<String,Object>>();
				Iterator detailIt = detailListGroup.entrySet().iterator();
				while (detailIt.hasNext()) {
					Entry detailEn = (Entry) detailIt.next();
					inDepotdetailList.add((Map<String, Object>) detailEn.getValue());
				}
				mainMap.put("ImportBatch", mainMap.get("ImportBatch"));
				mainMap.put("BIN_LogisticInfoID", "0");
				// 入库状态
				mainMap.put("TradeStatus", CherryConstants.BILLTYPE_GR_UNDO);
				// 审核状态
				mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
				int prmInDepotId = 0;
				// 共通将数据添加到入库单主表和明细表
				prmInDepotId = binOLSSCM09_BL.insertPrmInDepotAll(mainMap, inDepotdetailList);
				if (prmInDepotId == 0) {
					throw new CherryException("ISS00005");
				}

				// 准备参数，开始工作流
				UserInfo userinfo = (UserInfo) sessionMap.get("userInfo");
				Map<String, Object> pramMap = new HashMap<String, Object>();
				pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
				pramMap.put(CherryConstants.OS_MAINKEY_BILLID, prmInDepotId);
				pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
				pramMap.put("CurrentUnit", "BINOLSSPRM66");
				pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
				pramMap.put("BrandCode", userinfo.getBrandCode());
				pramMap.put("UserInfo", userinfo);
				long workFlowId = 0;
				workFlowId = binOLSSCM00_BL.StartOSWorkFlow(pramMap);
				if (workFlowId == 0) {
					throw new CherryException("EWF00001");
				}
				mainMap.put("ImportResult", "1");
				mainMap.put("WorkFlowID", workFlowId);
				// 将数据添加到入库单（Excel导入）主表和明细表
				int BIN_PrmInDepotExcelID = binOLSSPRM66_Service.insertPrmInDepotExcel(mainMap);
				for (Map<String, Object> map : detailList) {
					map.put("BIN_PrmInDepotExcelID", BIN_PrmInDepotExcelID);
					map.put("ErrorMsg", PropertiesUtil.getText("SSM00014"));
					binOLSSPRM66_Service.insertPrmInDepotDetailExcel(map);
				}
				successCount++;
			}
		}
		resultMap.put("successCount", successCount);
		resultMap.put("errorCount", errorCount);
		resultMap.put("totalCount", errorCount + successCount);
		resultMap.put("errorInDeportList", errorInDeportList);
		return resultMap;
	}

	/**
	 * 验证导入数据
	 * @param detailList
	 * @param sessionMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> validExcel(List<Map<String, Object>> detailList, Map<String, Object> sessionMap)
			throws Exception {
		// 导入结果标记，默认成功
		boolean resultFlag = false;
		long totalQuantity = 0;
		float totalAmount = 0;
		// 验证结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 验证后明细数据
		List<Map<String, Object>> resultDetailList = new ArrayList<Map<String, Object>>();
		int listSize = 0;
		if (detailList != null) {
			listSize = detailList.size();
		}
		// 一单中相同数据
		Map<String, Object> commonInfo = new HashMap<String, Object>();
		Map<String, Object> commonErrorsMap = new HashMap<String, Object>();
		if (listSize > 0) {
			// 一单中部门信息、入库日期相同，只需一次验证
			Map<String, Object> infoMap = detailList.get(0);

			// 校验部门名称
			String departName = ConvertUtil.getString(infoMap.get("excelDepartName"));
			if (CherryChecker.isNullOrEmpty(departName)) {
				resultFlag = true;
				commonErrorsMap.put("departNameNull", true);
			} else if (departName.length() > 50) {
				resultFlag = true;
				commonErrorsMap.put("departNameError", true);
				commonErrorsMap.put("departName", departName);
			} else {
				commonInfo.put("ImportDepartName", departName);
			}
			// 校验部门code
			String departCode = ConvertUtil.getString(infoMap.get("excelDepartCode"));
			if (CherryChecker.isNullOrEmpty(departCode)) {
				resultFlag = true;
				commonErrorsMap.put("departCodeNull", true);
			} else if (departCode.length() > 15) {
				resultFlag = true;
				commonErrorsMap.put("departCodeLength", true);
				commonErrorsMap.put("departCode", departCode);
			} else {
				commonInfo.put("ImportDepartCode", departCode);
				sessionMap.put("DepartCode", departCode);
				//根据部门code获取部门信息
				Map<String, Object> orgMap = binOLSSPRM66_Service.getOrgByCode(sessionMap);
				if (orgMap == null) {
					resultFlag = true;
					commonErrorsMap.put("departCodeExits", true);
				} else {
					commonInfo.put("departType", orgMap.get("Type"));
					if ("4".equals(orgMap.get("Type"))) {
						//部门是柜台，则获取系统配置项，校验是否允许给柜台入库
						String brandInfoId = ConvertUtil.getString(sessionMap.get("brandInfoId"));
						String organizationInfoId = ConvertUtil.getString(sessionMap.get("organizationInfoId"));
						if (!binOLCM14_BL.isConfigOpen("1094", organizationInfoId, brandInfoId)) {
							resultFlag = true;
							commonErrorsMap.put("counterError", true);
						}
					}
					if (!commonInfo.containsKey("counterError")) {
						// 校验正确，取实体仓库
						commonInfo.put("BIN_OrganizationID", orgMap.get("BIN_OrganizationID"));
						List<Map<String, Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(
								ConvertUtil.getString(orgMap.get("BIN_OrganizationID")), "");
						if (inDepotList.size() > 0) {
							Map<String, Object> inventoryInfo = inDepotList.get(0);
							commonInfo.put("BIN_InventoryInfoID", inventoryInfo.get("BIN_DepotInfoID"));
							// 仓库库位
							commonInfo.put("BIN_StorageLocationInfoID", "0");
						} else {
							resultFlag = true;
							commonErrorsMap.put("inDepotError", true);
						}
					}
				}
			}

			// 校验逻辑仓库名称
			String logicInventoryName = ConvertUtil.getString(infoMap.get("excelLogicInventoryName"));
			if (CherryChecker.isNullOrEmpty(logicInventoryName)
					&& !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))) {
				//逻辑仓库为空，部门为柜台则取默认终端逻辑仓库，否则取默认后台数据库
				if ("4".equals(commonInfo.get("departType"))) {
					sessionMap.put("Type", "1");
				} else {
					sessionMap.put("Type", "0");
				}
				Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(sessionMap);
				commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
				commonInfo.put("LogicInventoryName", logicInventoryInfo.get("InventoryNameCN"));
			} else if (!CherryChecker.isNullOrEmpty(logicInventoryName) && logicInventoryName.length() > 30) {
				resultFlag = true;
				commonErrorsMap.put("logicInventoryNameLength", true);
				commonErrorsMap.put("logicInventoryName", logicInventoryName);
			} else if (!CherryChecker.isNullOrEmpty(logicInventoryName)
					&& !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))) {
				commonInfo.put("LogicInventoryName", logicInventoryName);
				//逻辑仓库不为空，验证逻辑仓库是否存在，部门为柜台则取终端逻辑仓库，否则取后台数据库
				if ("4".equals(commonInfo.get("departType"))) {
					sessionMap.put("Type", "1");
				} else {
					sessionMap.put("Type", "0");
				}
				sessionMap.put("InventoryName", logicInventoryName);
				Map<String, Object> logicInventoryInfo = binOLSSPRM66_Service.getLogicInventoryByName(sessionMap);
				if (CherryChecker.isNullOrEmpty(logicInventoryInfo)) {
					resultFlag = true;
					commonErrorsMap.put("logicInventoryNameExits", true);
				} else {
					commonInfo.putAll(logicInventoryInfo);
				}
			} else {
				commonInfo.put("LogicInventoryName", logicInventoryName);
			}

			// 入库日期校验
			String inDepotDate = ConvertUtil.getString(infoMap.get("excelInvoiceDate"));
			if (CherryChecker.isNullOrEmpty(inDepotDate, true)) {
				resultFlag = true;
				commonErrorsMap.put("inDepotDateNull", true);
			} else if (CherryChecker.checkDate(inDepotDate, "yyyy-MM-dd")) {
				commonInfo.put("InDepotDate", inDepotDate);
				String bussinessDate = binOLSSPRM66_Service.getBusDate(sessionMap);
				if (bussinessDate.compareTo(inDepotDate) < 0) {
					resultFlag = true;
					commonErrorsMap.put("inDepotDateError1", true);
					commonErrorsMap.put("bussinessDate", bussinessDate);
				}
			} else {
				resultFlag = true;
				commonErrorsMap.put("inDepotDateError", true);
				commonErrorsMap.put("inDepotDate", inDepotDate);
			}
		}
		int detailNo = 1;
		for (Map<String, Object> detailMap : detailList) {
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			errorsMap.putAll(commonErrorsMap);
			detailMap.putAll(sessionMap);
			detailMap.putAll(commonInfo);
			// 入库区分
			detailMap.put("StockType", CherryConstants.STOCK_TYPE_IN);
			// 产品明细连番
			detailMap.put("DetailNo", detailNo++);
			// 校验产品名称
			String productName = ConvertUtil.getString(detailMap.get("excelProductName"));
			if (CherryChecker.isNullOrEmpty(productName)) {
				resultFlag = true;
				errorsMap.put("productNameNull", true);
			} else if (productName.length() > 50) {
				resultFlag = true;
				errorsMap.put("productNameError", true);
				errorsMap.put("productName", productName);
			} else {
				detailMap.put("ImportNameTotal", productName);
			}
			String unitCode = ConvertUtil.getString(detailMap.get("excelUnitCode"));
			String barCode = ConvertUtil.getString(detailMap.get("excelBarCode"));
			if (barCode != null && barCode.length() > 13) {
				resultFlag = true;
				errorsMap.put("barCodeLength", true);
				errorsMap.put("barCode", barCode);
			} else {
				detailMap.put("BarCode", barCode);
			}
			// 校验产品unitCode
			if (CherryChecker.isNullOrEmpty(unitCode)) {
				resultFlag = true;
				errorsMap.put("unitCodeNull", true);
			} else if (unitCode.length() > 20) {
				resultFlag = true;
				errorsMap.put("unitCodeLength", true);
				errorsMap.put("unitCode", unitCode);
			} else {
				detailMap.put("ImportUnitCode", unitCode);
				detailMap.put("UnitCode", unitCode);
				sessionMap.put("UnitCode", unitCode);
				List<Map<String, Object>> prmInfoList = binOLSSPRM66_Service.getPrmInfoByUnitCode(sessionMap);
				if (prmInfoList == null || prmInfoList.size() == 0) {
					resultFlag = true;
					errorsMap.put("unitCodeExits", true);
				} else {
					detailMap.put("BIN_PromotionProductID", prmInfoList.get(0).get("BIN_PromotionProductID"));
					detailMap.put("NameTotal", prmInfoList.get(0).get("NameTotal"));
					detailMap.put("BIN_PromotionProductVendorID", prmInfoList.get(0)
							.get("BIN_PromotionProductVendorID"));
					// 校验产品barCode
					if (!CherryChecker.isNullOrEmpty(barCode) && barCode.length() > 13) {
						resultFlag = true;
						errorsMap.put("barCodeLength", true);
						detailMap.remove("BarCode");
						errorsMap.put("barCode", barCode);
					} else {
						if (prmInfoList.size() > 1) {
							// 一品多码
							if (CherryChecker.isNullOrEmpty(barCode)) {
								resultFlag = true;
								errorsMap.put("barCodeNull", true);
							} else {
								int index = indexOfBarCode(prmInfoList, barCode);
								if (index == -1) {
									resultFlag = true;
									errorsMap.put("barCodeExits", true);
								} else {
									detailMap.put("BIN_PromotionProductVendorID",
											prmInfoList.get(index).get("BIN_PromotionProductVendorID"));
								}
							}
						} else {
							if (CherryChecker.isNullOrEmpty(barCode)
									|| barCode.equals(prmInfoList.get(0).get("BarCode"))) {
								detailMap.putAll(prmInfoList.get(0));
							} else {
								resultFlag = true;
								errorsMap.put("barCodeExits", true);
							}
						}
					}
				}
			}
			// 入库数量校验
			String quantity = ConvertUtil.getString(detailMap.get("excelQuantity"));
			if (CherryChecker.isNullOrEmpty(quantity, true) || "0".equals(quantity)) {
				resultFlag = true;
				errorsMap.put("quantityNull", true);
			} else if (!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(quantity)) || quantity.length() > 9) {
				resultFlag = true;
				errorsMap.put("quantityError", true);
				errorsMap.put("quantity", quantity);
			} else {
				detailMap.put("Quantity", quantity);
				detailMap.put("PreQuantity", quantity);
			}
			// 产品价格
			if (errorsMap.containsKey("unitCodeNull") || errorsMap.containsKey("unitCodeLength")
					|| errorsMap.containsKey("unitCodeExits") || errorsMap.containsKey("inDepotDateError")) {
				detailMap.put("Price", 0);
			} else {
				sessionMap.put("BIN_PromotionProductID", detailMap.get("BIN_PromotionProductID"));
				String price = ConvertUtil.getString(binOLSSPRM66_Service.getPrmPrice(sessionMap));
				if(CherryChecker.isNullOrEmpty(price, true)){
					price = "0";
				}
				detailMap.put("Price", price);
			}
			// 产品总数量和总金额
			if (!errorsMap.containsKey("quantityError")) {
				totalQuantity += ConvertUtil.getInt(quantity);
				totalAmount += ConvertUtil.getInt(quantity) * ConvertUtil.getFloat(detailMap.get("Price"));
			}
			// 错误信息
			String errorMsg = getErrorMsg(errorsMap);
			if (errorMsg != null && errorMsg.length() > 200) {
				errorMsg = errorMsg.subSequence(0, 195) + "...";
			}
			detailMap.put("ErrorMsg", errorMsg);
			resultDetailList.add(detailMap);
		}
		if (resultFlag) {
			totalAmount = 0;
			totalQuantity = 0;
		} else if (ConvertUtil.getString(totalQuantity).length() > 9) {
			// 当一单总数量过大时
			resultFlag = true;
			resultMap.put("totalError", PropertiesUtil.getText("SSM00021"));
			totalAmount = 0;
			totalQuantity = 0;
		}
		resultMap.put("ResultFlag", resultFlag);
		resultMap.put("TotalAmount", totalAmount);
		resultMap.put("TotalQuantity", totalQuantity);
		resultMap.put("resultDetailList", resultDetailList);
		return resultMap;
	}

	/**
	 * 一品多码时，判断某个编码是否存在
	 * 
	 * @param prmInfoList
	 * @param barCode
	 * @return
	 */
	public int indexOfBarCode(List<Map<String, Object>> prmInfoList, String barCode) {
		for (int index = 0; index < prmInfoList.size(); index++) {
			Map<String, Object> map = prmInfoList.get(index);
			if (barCode.equals(map.get("BarCode"))) {
				return index;
			}
		}
		return -1;
	}


	/**
	 * 生成入库单据号
	 * 
	 * @param map
	 * @return
	 */
	public String getTicketNumber(Map<String, Object> map) {
		// 组织ID
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		// 程序ID
		String name = "BINOLSSPRM66";
		// 调用共通生成单据号
		return binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_GR);
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @return
	 * @throws Exception
	 */
	public boolean checkData(Map<String, Object> rowMap) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelProductName = ConvertUtil.getString(rowMap.get("excelProductName"));
		String excelDepartCode = ConvertUtil.getString(rowMap.get("excelDepartCode"));
		String excelDepartName = ConvertUtil.getString(rowMap.get("excelDepartName"));
		String excelInvoiceDate = ConvertUtil.getString(rowMap.get("excelInvoiceDate"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelLogicInventoryName = ConvertUtil.getString(rowMap.get("excelLogicInventoryName"));
		if(CherryChecker.isNullOrEmpty(excelUnitCode, true) || excelUnitCode.length() > 20){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"A"+rowNumber});
		}
		if(excelBarCode.length() > 13){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"B"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelProductName, true) || excelProductName.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"C"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelDepartCode, true) || excelDepartCode.length() > 15){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"D"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelDepartName, true) || excelDepartName.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"E"+rowNumber});
		}
		if(!CherryChecker.checkDate(excelInvoiceDate) || !CherryChecker.checkDate(excelInvoiceDate, "yyyy-DD-mm")){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"F"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isPositiveAndNegative(excelQuantity) ||  excelQuantity.length() > 9){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"G"+rowNumber});
		}
		if(excelLogicInventoryName.length()>30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"H"+rowNumber});
		}
		return true;
	}
	
	/**
	 * 是否是重复数据
	 * @param rowMap
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean isRepeatData(Map<String, Object> rowMap,Map<String, Object> map) throws Exception {
		String excelDepartCode = ConvertUtil.getString(rowMap.get("excelDepartCode"));
		String excelInvoiceDate = ConvertUtil.getString(rowMap.get("excelInvoiceDate"));
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramsMap.put("departCode", excelDepartCode);
		paramsMap.put("inDepotDate", excelInvoiceDate);
		paramsMap.put("unitCode", excelUnitCode);
		paramsMap.put("quantity", excelQuantity);
		paramsMap.put("barCode", excelBarCode);
		List<Map<String, Object>> repeartData = binOLSSPRM66_Service.getRepeatData(paramsMap);
		if(repeartData != null && repeartData.size() >0){
			return true;
		}
		return false;
	}

	/**
	 * 将错误信息转换为字符串
	 * 
	 * @param errorMap
	 * @return
	 */
	public String getErrorMsg(Map<String, Object> errorMap) {
		StringBuffer errorMsg = new StringBuffer();
		if (errorMap.get("departCodeExits") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00001"));
		}
		if (errorMap.get("departCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00002"));
		}
		if (errorMap.get("inDepotError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00003"));
		}
		if (errorMap.get("unitCodeExits") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00004"));
		}
		if (errorMap.get("unitCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00005"));
		}
		if (errorMap.get("inDepotDateError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00006"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("inDepotDate")));
			errorMsg.append("！");
		}
		if (errorMap.get("quantityError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00007"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("quantity")));
			errorMsg.append("！");
		}
		if (errorMap.get("departNameError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00008"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departName")));
			errorMsg.append("！");
		}
		if (errorMap.get("productNameError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00009"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("productName")));
			errorMsg.append("！");
		}
		if (errorMap.get("departCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00010"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("unitCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00011"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("unitCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("barCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00015"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("barCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("barCodeExits") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00016"));
		}
		if (errorMap.get("barCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00017"));
		}
		if (errorMap.get("logicInventoryNameLength") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00018"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("logicInventoryName")));
			errorMsg.append("！");
		}
		if (errorMap.get("logicInventoryNameExits") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00019"));
		}
		if (errorMap.get("counterError") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00022"));
		}
		if (errorMap.get("inDepotDateError1") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00023"));
			errorMsg.append(errorMap.get("bussinessDate"));
			errorMsg.append("！");
		}
		if (errorMap.get("inDepotDateNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00024"));
		}
		if (errorMap.get("quantityNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00025"));
		}
		if (errorMap.get("departNameNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00026"));
		}
		if (errorMap.get("productNameNull") != null) {
			errorMsg.append(PropertiesUtil.getText("SSM00027"));
		}
		String errorMsgStr = ConvertUtil.getString(errorMsg);
		if (CherryChecker.isNullOrEmpty(errorMsgStr)) {
			errorMsg.append(PropertiesUtil.getText("SSM00020"));
		}
		return errorMsg.toString();
	}
}
