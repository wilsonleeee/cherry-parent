/*
 * @(#)BINOLSTIOS13_BL.java     1.0 2015-10-9
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
package com.cherry.st.ios.bl;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.bl.BINOLSTCM08_BL;
import com.cherry.st.ios.interfaces.BINOLSTIOS13_IF;
import com.cherry.st.ios.service.BINOLSTIOS13_Service;

/**
 * 
 * @ClassName: BINOLSTIOS13_BL 
 * @Description: TODO(大仓入库（Excel导入）BL) 
 * @author menghao
 * @version v1.0.0 2015-10-9 
 *
 */
public class BINOLSTIOS13_BL implements BINOLSTIOS13_IF{

	private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS13_BL.class);
	
	@Resource(name="binOLSTIOS13_Service")
	private BINOLSTIOS13_Service binOLSTIOS13_Service;
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_BL binOLCM18_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_BL binOLSTCM08_BL;
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_BL binOLSTCM00_BL;
	@Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
	
	/**
	 * 获取入库单（Excel导入）批次信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		return binOLSTIOS13_Service.getImportBatchList(map);
	}

	/**
	 * 获取入库单（Excel导入）批次总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		return binOLSTIOS13_Service.getImportBatchCount(map);
	}

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
			if (CherryConstants.INDEPOT_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 入库数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.INDEPOT_SHEET_NAME });
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.HQ_PRODUCTINDEPOTEXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
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
				throw new CherryException("ECM00013",new String[]{PropertiesUtil.getText("STM00012")});
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
				if(this.getImportBatchCount(map) > 0){
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
            // 入库单据号
            String excelBillNoIF = dateSheet.getCell(0, r).getContents().trim();
            rowMap.put("excelBillNoIF", excelBillNoIF);
            // 产品厂商编码
            String excelUnitCode = dateSheet.getCell(1, r).getContents().trim();
            rowMap.put("excelUnitCode", excelUnitCode);
            // 产品条码
            String excelBarCode = dateSheet.getCell(2, r).getContents().trim();
            rowMap.put("excelBarCode", excelBarCode);
            // 产品名称
            String excelProductName = dateSheet.getCell(3, r).getContents().trim();
            rowMap.put("excelProductName", excelProductName);
            // 入库日期
 			String excelInvoiceDate = dateSheet.getCell(4, r).getContents().trim();
 			rowMap.put("excelInvoiceDate", excelInvoiceDate);
 			//执行价
 			String excelPrice = dateSheet.getCell(5, r).getContents().trim();
 			rowMap.put("excelPrice", excelPrice);
 			// 产品数量
 			String excelQuantity = dateSheet.getCell(6, r).getContents().trim();
 			rowMap.put("excelQuantity", excelQuantity);
            rowMap.put("rowNumber", r+1);
            if (CherryChecker.isNullOrEmpty(excelBillNoIF)
            		&& CherryChecker.isNullOrEmpty(excelUnitCode)
                    && CherryChecker.isNullOrEmpty(excelBarCode)
                    && CherryChecker.isNullOrEmpty(excelProductName)
                    && CherryChecker.isNullOrEmpty(excelInvoiceDate)
                    && CherryChecker.isNullOrEmpty(excelPrice)
                    && CherryChecker.isNullOrEmpty(excelQuantity)) {
                break;

            }
			// 数据基本格式校验
			this.checkData(rowMap);
			
			/**校验填写的入库单据号是否在【入库单主表】中已经存在*/
			if(!CherryChecker.isNullOrEmpty(excelBillNoIF, true)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
				paramMap.put("billNoIF", excelBillNoIF);
				List<Map<String, Object>> repeatList = binOLSTIOS13_Service.getRepeatBillNo(paramMap);
				if(null != repeatList && repeatList.size() > 0) {
					/***导入失败的单据号可重新导入（导入失败的单据只写入库单（Excel）表）**/
					// 第{0}行记录的入库单据号已经存在，请修改后重新导入！
					throw new CherryException("STM00045",new String[] { ConvertUtil.getString(r+1) });
				}
				
			}
			String importRepeat = ConvertUtil.getString(map.get("importRepeat"));
			if(!CherryChecker.isNullOrEmpty(importRepeat, true) && "0".equals(importRepeat)){
				if(isRepeatData(rowMap,map)){
					// 因存在重复数据故此时此数据必定存在
					List<Map<String, Object>> repeatDateList = (List<Map<String, Object>>)rowMap.get("repeatDateList");
					if(repeatDateList.size() > 1) {
						// 重复数据大于两条则只显示两条
						throw new CherryException("STM00054", new String[] {
								ConvertUtil.getString(r + 1),
								ConvertUtil.getString(repeatDateList.get(0).get("importBatch")),
								ConvertUtil.getString(repeatDateList.get(0).get("billNoIF")),
								ConvertUtil.getString(repeatDateList.get(1).get("importBatch")),
								ConvertUtil.getString(repeatDateList.get(1).get("billNoIF"))
								});
					} else {
						throw new CherryException("STM00053",new String[] { 
								ConvertUtil.getString(r+1), 
								ConvertUtil.getString(repeatDateList.get(0).get("importBatch")),
								ConvertUtil.getString(repeatDateList.get(0).get("billNoIF"))
								});
					}
				}
			}
			// 入库日期与单据号拆分单据
			String importDatasKey = excelInvoiceDate + excelBillNoIF;
			if (importDatas.containsKey(importDatasKey)) {
				// 一条主单中的明细
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) importDatas
						.get(importDatasKey);
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			} else {
				if(importDatas.containsKey(excelBillNoIF)) {
					// 入库单据号：【{0}】对应的入库日期必须一致，请核对后重新导入！
					throw new CherryException("STM00059",new String[]{excelBillNoIF});
				}
				List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
				
				if(!CherryChecker.isNullOrEmpty(excelBillNoIF)) {
					importDatas.put(excelBillNoIF, excelBillNoIF);
				}
			}
		}
		if(r==3){
			throw new CherryException("EBS00035",new String[] { CherryConstants.INDEPOT_SHEET_NAME });
		}
		return importDatas;
	}

	/**
	 * 入库导入处理
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
		// 导入日期
		String sysDate = binOLSTIOS13_Service.getSYSDate();
		sessionMap.put("ImportDate", sysDate);
		// 插入导入批次信息返回导入批次ID
		int importBatchId = this.insertImportBatch(sessionMap);
		//导入原因不写入入库单据表
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
			if(!(en.getValue() instanceof List)) {
				// 此数据是单据号标记，不进行单据明细的处理
				continue;
			}
			// 明细数据
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) en.getValue();
			// 验证数据的有效性
			Map<String, Object> detailMap = this.validExcel(detailList, sessionMap);
			detailList = (List<Map<String, Object>>) detailMap.get("resultDetailList");
			// 主数据
			Map<String, Object> mainMap = new HashMap<String, Object>();
			if (detailList != null && detailList.size() > 0) {
				mainMap = detailList.get(0);
				/***填写的单据号是写入BillNoIF字段***/
				// 接口单据号
				String billNoIF = ConvertUtil.getString(mainMap.get("BillNoIF"));
				// 系统生成的单据号
				String billNo = getTicketNumber(sessionMap);
				// 导入数据未填写单据号数据时，采用系统生成的单据号
				if("".equals(billNoIF)) {
					billNoIF = billNo;
				}
				mainMap.put("BillNo", billNo);
				mainMap.put("BillNoIF", billNoIF);
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
				int BIN_ProductInDepotExcelID = binOLSTIOS13_Service
						.insertProductInDepotExcel(mainMap);
				String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductInDepotExcelID", BIN_ProductInDepotExcelID);
					map.put("ErrorMsg", totalErrorMsg+map.get("ErrorMsg"));
					binOLSTIOS13_Service.insertProductInDepotDetailExcel(map);
					errorInDeportList.add(map);
				}
				errorCount++;
			} else {
				//验证成功，合并相同产品明细
				Map<String,Object> detailListGroup = new HashMap<String, Object>();
				for(Map<String, Object> fm : detailList){
					Map<String, Object> detailGroupMap = new HashMap<String, Object>();
					detailGroupMap.putAll(fm);
					String unitCode = ConvertUtil.getString(detailGroupMap.get("UnitCode")).trim();
					String barCode = ConvertUtil.getString(detailGroupMap.get("BarCode")).trim();
					/****相同产品不同价格的明细：数量相加，价格取平均***/
					/**
					 * 票号：WITPOSQA-16893：相同产品不同价格，价格取平均值（平均值=总金额/总数量）
					 */
					String mapKey = unitCode + barCode;
					if(detailListGroup.containsKey(mapKey)){
						Map<String, Object> productMap = (Map<String, Object>) detailListGroup.get(mapKey);
						int quantity = ConvertUtil.getInt(detailGroupMap.get("Quantity"))+ConvertUtil.getInt(productMap.get("Quantity"));
						double amount = ConvertUtil.getInt(productMap.get("Quantity")) * ConvertUtil.getFloat(productMap.get("Price"))
															+ConvertUtil.getInt(detailGroupMap.get("Quantity"))*ConvertUtil.getFloat(detailGroupMap.get("Price"));
						double priceAvg = 0.0;
						if(quantity != 0) {
							priceAvg = amount/quantity;
						}
						productMap.put("Quantity", quantity);
						productMap.put("PreQuantity", quantity);
						productMap.put("Price", priceAvg);
						detailListGroup.put(mapKey, productMap);
					}else{
						detailListGroup.put(mapKey, detailGroupMap);
					}
				}
				// 合并后明细转换为入库单明细List
				List<Map<String, Object>> inDepotdetailList = new ArrayList<Map<String,Object>>();
				Iterator detailIt = detailListGroup.entrySet().iterator();
				while (detailIt.hasNext()) {
					Entry detailEn = (Entry) detailIt.next();
					inDepotdetailList.add((Map<String, Object>) detailEn.getValue());
				}
				mainMap.put("BIN_LogisticInfoID", "0");
				//入库状态
				mainMap.put("TradeStatus", "0");
				//审核状态
				mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
				int productInDepotId = 0;
				// 共通将数据添加到入库单主表和明细表
				productInDepotId = binOLSTCM08_BL.insertProductInDepotAll(mainMap, inDepotdetailList);
				if (productInDepotId == 0) {
					throw new CherryException("ISS00005");
				}
				
				// 准备参数，开始工作流
				UserInfo userinfo = (UserInfo) sessionMap.get("userInfo");
				Map<String, Object> pramMap = new HashMap<String, Object>();
				pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
				pramMap.put(CherryConstants.OS_MAINKEY_BILLID, productInDepotId);
				pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE,
						userinfo.getBIN_EmployeeID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION,
						userinfo.getBIN_PositionCategoryID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
				pramMap.put("CurrentUnit", "BINOLSTIOS13");
				pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
				pramMap.put("UserInfo", userinfo);
				pramMap.put("BrandCode", userinfo.getBrandCode());
				// 总仓入库
				pramMap.put("HQ_SUBMIT", "YES");
				long workFlowId = 0;
				workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
				
				if (workFlowId == 0) {
					throw new CherryException("EWF00001");
				}
				mainMap.put("ImportResult", "1");
				mainMap.put("WorkFlowID", workFlowId);
				// 将数据添加到入库单（Excel导入）主表和明细表
				int BIN_ProductInDepotExcelID = binOLSTIOS13_Service
						.insertProductInDepotExcel(mainMap);
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductInDepotExcelID", BIN_ProductInDepotExcelID);
					map.put("ErrorMsg", PropertiesUtil.getText("STM00014"));
					binOLSTIOS13_Service.insertProductInDepotDetailExcel(map);
				}
				successCount++;
			}
		}
		resultMap.put("successCount", successCount);
		resultMap.put("errorCount", errorCount);
		resultMap.put("errorInDeportList", errorInDeportList);
		return resultMap;
	}

	/**
	 * 对一单数据进行验证补充成完整信息
	 * 
	 * @return 一单详细信息
	 * @throws Exception
	 */
	public Map<String, Object> validExcel(List<Map<String, Object>> detailList,
			Map<String, Object> sessionMap) throws Exception {
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
		Map<String, Object> commonInfo = new HashMap<String, Object>();
		Map<String, Object> commonErrorsMap = new HashMap<String, Object>();
		if (listSize > 0) {
			// 一单中入库日期相同，只需一次验证
			Map<String, Object> infoMap = detailList.get(0);
			//获取品牌总部的部门信息
			Map<String, Object> orgMap = binOLCM01_BL.getBrandDepartInfo(sessionMap);
			if (orgMap == null) {
				resultFlag = true;
				commonErrorsMap.put("departCodeExits", true);
			} else {
				// 将品牌总部部门信息设置进产品入库导入EXCEL表中
				String departName = ConvertUtil.getString(orgMap.get("DepartName"));
				String departCode = ConvertUtil.getString(orgMap.get("DepartCode"));
				// 入库部门--固定为品牌总部部门
				infoMap.put("excelDepartName", departName);
				infoMap.put("excelDepartCode", departCode);
				commonInfo.put("ImportDepartName", departName);
				commonInfo.put("ImportDepartCode", departCode);
				// 取部门实体仓库
				commonInfo.put("BIN_OrganizationID", orgMap.get("BIN_OrganizationID"));
				List<Map<String, Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(
						ConvertUtil.getString(orgMap.get("BIN_OrganizationID")), "");
				if (inDepotList.size() > 0) {
					Map<String, Object> inventoryInfo = inDepotList.get(0);
					commonInfo.put("BIN_InventoryInfoID", inventoryInfo.get("BIN_DepotInfoID"));
					//仓库库位
					commonInfo.put("BIN_StorageLocationInfoID", "0");
					//包装类型
					commonInfo.put("BIN_ProductVendorPackageID", "0");
				} else {
					resultFlag = true;
					commonErrorsMap.put("inDepotError", true);
				}
			}
			
			// 获取逻辑仓库
			sessionMap.put("Type", "0");
			Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(sessionMap);
			commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
			commonInfo.put("LogicInventoryName", logicInventoryInfo.get("InventoryNameCN"));
			
			// 入库日期校验
			String inDepotDate = ConvertUtil.getString(infoMap.get("excelInvoiceDate"));
			if(CherryChecker.isNullOrEmpty(inDepotDate, true)){
				resultFlag = true;
				commonErrorsMap.put("inDepotDateNull", true);
			}else if (CherryChecker.checkDate(inDepotDate, "yyyy-MM-dd")) {
				commonInfo.put("InDepotDate", inDepotDate);
				String bussinessDate = binOLSTIOS13_Service.getBussinessDate(sessionMap);
				if(bussinessDate.compareTo(inDepotDate) < 0){
					resultFlag = true;
					commonErrorsMap.put("inDepotDateError1", true);
					commonErrorsMap.put("bussinessDate", bussinessDate);
				}
			} else {
				resultFlag = true;
				commonErrorsMap.put("inDepotDateError", true);
				commonErrorsMap.put("inDepotDate", inDepotDate);
			}
			
			// 接口单据号校验
			String billNoIF = ConvertUtil.getString(infoMap.get("excelBillNoIF"));
			if (!CherryChecker.isNullOrEmpty(billNoIF, true) && billNoIF.length() > 40) {
				resultFlag = true;
				commonErrorsMap.put("billNoIFLengthError", true);
				commonErrorsMap.put("billNoIF", billNoIF);
			} else {
				commonInfo.put("BillNoIF", billNoIF);
				commonInfo.put("ImportBillNoIF", billNoIF);
			}
		}
		int detailNo=1;
		for(Map<String, Object> detailMap : detailList){
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			errorsMap.putAll(commonErrorsMap);
			detailMap.putAll(sessionMap);
			detailMap.putAll(commonInfo);
			//产品明细连番
			detailMap.put("DetailNo", detailNo++ );
			// 校验产品名称
			String productName = ConvertUtil.getString(detailMap.get("excelProductName"));
			if(CherryChecker.isNullOrEmpty(productName)){
				resultFlag = true;
				errorsMap.put("productNameNull", true);
			}else if (productName.length() > 50) {
				resultFlag = true;
				errorsMap.put("productNameError", true);
				errorsMap.put("productName", productName);
			} else {
				detailMap.put("ImportNameTotal", productName);
			}
			String unitCode = ConvertUtil.getString(detailMap.get("excelUnitCode"));
			String barCode = ConvertUtil.getString(detailMap.get("excelBarCode"));
			if(barCode != null && barCode.length() > 13){
				resultFlag = true;
				errorsMap.put("barCodeLength", true);
				errorsMap.put("barCode", barCode);
			}else{
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
				List<Map<String, Object>> prtInfoList = binOLSTIOS13_Service.getPrtInfo(sessionMap);
				// 厂商编码不存在，则此条记录导入失败
				if(prtInfoList == null || prtInfoList.size() == 0) {
					resultFlag = true;
					errorsMap.put("unitCodeExits", true);
					errorsMap.put("unitCode", unitCode);
				} else {
					detailMap.put("ImportUnitCode", prtInfoList.get(0).get("UnitCode"));
					detailMap.put("UnitCode", prtInfoList.get(0).get("UnitCode"));
					
					detailMap.put("BIN_ProductID", prtInfoList.get(0).get("BIN_ProductID"));
					detailMap.put("NameTotal", prtInfoList.get(0).get("NameTotal"));
					detailMap.put("BIN_ProductVendorID", prtInfoList.get(0).get("BIN_ProductVendorID"));
					// 校验产品barCode
					if(!CherryChecker.isNullOrEmpty(barCode) && barCode.length() > 13){
						resultFlag = true;
						errorsMap.put("barCodeLength", true);
						detailMap.remove("BarCode");
						errorsMap.put("barCode", barCode);
					}else{
						if(prtInfoList.size() > 1){
							//一品多码
							if(CherryChecker.isNullOrEmpty(barCode)){
								resultFlag = true;
								errorsMap.put("barCodeNull", true);
							}else{
								int index = indexOfBarCode(prtInfoList,barCode);
								if(index == -1){
									resultFlag = true;
									errorsMap.put("barCodeExits", true);
								}else{
									detailMap.put("BIN_ProductVendorID", prtInfoList.get(index).get("BIN_ProductVendorID"));
								}
							}
						}else{
							if(CherryChecker.isNullOrEmpty(barCode) || barCode.equals(prtInfoList.get(0).get("BarCode"))){
								detailMap.putAll(prtInfoList.get(0));
							}else{
								resultFlag = true;
								errorsMap.put("barCodeExits", true);
							}
						}
					}
				}
			}
			// 入库数量校验
			String quantity = ConvertUtil.getString(detailMap.get("excelQuantity"));
			if(CherryChecker.isNullOrEmpty(quantity, true) || "0".equals(quantity)){
				resultFlag = true;
				errorsMap.put("quantityNull", true);
			}else if (!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(quantity))||quantity.length() > 9) {
				resultFlag = true;
				errorsMap.put("quantityError", true);
				errorsMap.put("quantity", quantity);
			}else{
				detailMap.put("Quantity", quantity);
				detailMap.put("PreQuantity", quantity);
			}
			// 产品价格
			String price = ConvertUtil.getString(detailMap.get("excelPrice"));
			if (errorsMap.containsKey("unitCodeNull") || errorsMap.containsKey("unitCodeLength")
					|| errorsMap.containsKey("unitCodeExits")
					|| errorsMap.containsKey("inDepotDateError")) {
				detailMap.put("Price", price);
				detailMap.put("ReferencePrice",0);
			} else {
				sessionMap.put("BIN_ProductID", detailMap.get("BIN_ProductID"));
				//验证价格，Excel的价格如果为空取数据库的价格
				if(!price.equals("") && !CherryChecker.isFloatValid(price,14,2)){
				    resultFlag = true;
	                errorsMap.put("priceError", true);
	                errorsMap.put("price", price);
				}else{
					// 价格为空的情况下取采购价
				    String referencePrice = ConvertUtil.getString(binOLSTIOS13_Service.getPrtOrderPrice(sessionMap).get("orderPrice"));
				    if("".equals(price)){
				        price = referencePrice;
				    }
				    detailMap.put("Price",price);
				    detailMap.put("ReferencePrice",referencePrice);
				}
			}
			// 产品总数量和总金额
			if (!errorsMap.containsKey("quantityError")) {
				totalQuantity += ConvertUtil.getInt(quantity);
				if(!errorsMap.containsKey("priceError")){
				    totalAmount += ConvertUtil.getInt(quantity) * ConvertUtil.getFloat(detailMap.get("Price"));
				}
			}
			// 错误信息
			String errorMsg = getErrorMsg(errorsMap);
			if(errorMsg != null && errorMsg.length() > 200){
				errorMsg = errorMsg.subSequence(0, 195)+"...";
			}
			detailMap.put("ErrorMsg", errorMsg);
			resultDetailList.add(detailMap);
		
		}
		if (resultFlag) {
			totalAmount = 0;
			totalQuantity = 0;
		}else if(ConvertUtil.getString(totalQuantity).length() > 9){
			//当一单总数量过大时
			resultFlag = true;
			resultMap.put("totalError", PropertiesUtil.getText("STM00021"));
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
		String name = "BINOLSTIOS13_BL";
		// 调用共通生成单据号
		return binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name,
				CherryConstants.BUSINESS_TYPE_GR);
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @return
	 * @throws Exception
	 */
	public boolean checkData(Map<String, Object> rowMap) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		// 厂商编码
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		// 产品条码
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelProductName = ConvertUtil.getString(rowMap.get("excelProductName"));
		String excelInvoiceDate = ConvertUtil.getString(rowMap.get("excelInvoiceDate"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelBillNoIF = ConvertUtil.getString(rowMap.get("excelBillNoIF"));
		
        if(CherryChecker.isNullOrEmpty(excelUnitCode, true) || excelUnitCode.length() > 20){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"B"+rowNumber});
        }
        // 校验厂商编码
        if(CherryChecker.isNullOrEmpty(excelBarCode, true) || excelBarCode.length() > 13){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"C"+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelProductName, true) || excelProductName.length() > 50){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"D"+rowNumber});
        }
        if(!CherryChecker.checkDate(excelInvoiceDate) || !CherryChecker.checkDate(excelInvoiceDate, "yyyy-DD-mm")){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"E"+rowNumber});
        }
        if(!CherryChecker.isNullOrEmpty(excelPrice, true) && !CherryChecker.isFloatValid(excelPrice,14,2)){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"F"+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isPositiveAndNegative(excelQuantity) ||  excelQuantity.length() > 9){
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"G"+rowNumber});
        }
        if(excelBillNoIF.length() > 40) {
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"A"+rowNumber});
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
		String excelInvoiceDate = ConvertUtil.getString(rowMap.get("excelInvoiceDate"));
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramsMap.put("inDepotDate", excelInvoiceDate);
		paramsMap.put("unitCode", excelUnitCode);
		paramsMap.put("quantity", excelQuantity);
		paramsMap.put("barCode", excelBarCode);
		paramsMap.put("price", excelPrice);
		List<Map<String, Object>> repeartData = binOLSTIOS13_Service.getRepeatData(paramsMap);
		if(repeartData != null && repeartData.size() >0){
			rowMap.put("repeatDateList", repeartData);
			return true;
		}
		return false;
	}
	
	/**
	 * 插入导入批次
	 * @param sessionMap
	 * @return
	 */
	@Override
	public int insertImportBatch(Map<String, Object> sessionMap) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(sessionMap);
		paramsMap.put("ImportBatchCodeIF",sessionMap.get("ImportBatchCode"));
		return binOLSTIOS13_Service.insertImportBatch(paramsMap);
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
			errorMsg.append(PropertiesUtil.getText("STM00001"));
		}
		if (errorMap.get("departCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00002"));
		}
		if (errorMap.get("inDepotError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00003"));
		}
		if (errorMap.get("unitCodeExits") != null) {
			// 不存在该厂商编码或者备用厂商编码的产品！
			errorMsg.append(PropertiesUtil.getText("STM00046"));
			errorMsg.append("【"+ConvertUtil.getString(errorMap.get("unitCode"))+"】、【"+ConvertUtil.getString(errorMap.get("unitCodeReference"))+"】");
			errorMsg.append("！");
		}
		if (errorMap.get("unitCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00005"));
		}
		if (errorMap.get("inDepotDateError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00006"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("inDepotDate")));
			errorMsg.append("！");
		}
		if(errorMap.get("priceError") != null){
	        errorMsg.append(PropertiesUtil.getText("STM00030"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("price")));
            errorMsg.append("！");
		}
		if (errorMap.get("quantityError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00007"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("quantity")));
			errorMsg.append("！");
		}
		if (errorMap.get("departNameError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00008"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departName")));
			errorMsg.append("！");
		}
		if (errorMap.get("productNameError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00009"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("productName")));
			errorMsg.append("！");
		}
		if (errorMap.get("departCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00010"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("unitCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00011"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("unitCode")));
			errorMsg.append("！");
		}	
		if (errorMap.get("barCodeLength") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00015"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("barCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("barCodeExits") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00016"));
		}
		if (errorMap.get("barCodeNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00017"));
		}
		if (errorMap.get("logicInventoryNameLength") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00018"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("logicInventoryName")));
			errorMsg.append("！");
		}
		if (errorMap.get("logicInventoryNameExits") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00019"));
		}
		// 单据号长度不能超过40位
		if (errorMap.get("billNoIFLengthError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00044"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("billNoIF")));
			errorMsg.append("！");
		}
		if (errorMap.get("counterError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00022"));
		}
		if (errorMap.get("inDepotDateError1") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00023"));
			errorMsg.append(errorMap.get("bussinessDate"));
			errorMsg.append("！");
		}
		if (errorMap.get("inDepotDateNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00024"));
		}
		if (errorMap.get("quantityNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00025"));
		}
		if (errorMap.get("departNameNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00026"));
		}
		if (errorMap.get("productNameNull") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00027"));
		}
		String errorMsgStr = ConvertUtil.getString(errorMsg);
		if(CherryChecker.isNullOrEmpty(errorMsgStr)){
			errorMsg.append(PropertiesUtil.getText("STM00020"));
		}
		return errorMsg.toString();
	}
}
