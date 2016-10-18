/*
 * @(#)BINOLSTIOS09_BL.java     1.0 2013/07/04
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
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.bl.BINOLSTCM08_BL;
import com.cherry.st.ios.interfaces.BINOLSTIOS09_IF;
import com.cherry.st.ios.service.BINOLSTIOS09_Service;

/**
 * 
 * 入库单（Excel导入）BL
 * 
 * @author zhangle
 * @version 1.0 2013.07.04
 */
public class BINOLSTIOS09_BL implements BINOLSTIOS09_IF{

	private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS09_BL.class);
	
	@Resource(name="binOLSTIOS09_Service")
	private BINOLSTIOS09_Service binOLSTIOS09_Service;
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_BL binOLCM18_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_BL binOLSTCM08_BL;
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_BL binOLSTCM00_BL;
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 获取入库单（Excel导入）批次信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		return binOLSTIOS09_Service.getImportBatchList(map);
	}

	/**
	 * 获取入库单（Excel导入）批次总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		return binOLSTIOS09_Service.getImportBatchCount(map);
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
        if(!CherryConstants.PRODUCTINDEPOTEXCEL_VERSION.equals(version)){
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
		
	    Map<String,Object> detailOrderMap = new HashMap<String,Object>();
	    int sheet_0_Legth = sheets[0].getRows();
	    //读取自定义明细的顺序
	    int columnsLength = sheets[0].getColumns();
	    for(int i=3;i<sheet_0_Legth;i++){
	        //有明细顺序列才查找顺序
	        if(columnsLength>5){
	            int index = CherryUtil.obj2int(sheets[0].getCell(5,i).getContents().trim());
	            if(index>0){
	                String columnName = sheets[0].getCell(1,i).getContents().trim();
	                detailOrderMap.put(columnName,index);
	            }
	        }
	    }
	    //兼容没有定义明细顺序的模板
	    if(null == detailOrderMap || detailOrderMap.isEmpty()){
	        detailOrderMap = new HashMap<String,Object>();
	        detailOrderMap.put("excelBillNoIF",detailOrderMap.size()+1);
	        detailOrderMap.put("excelUnitCode",detailOrderMap.size()+1);
	        detailOrderMap.put("excelBarCode",detailOrderMap.size()+1);
	        detailOrderMap.put("excelProductName",detailOrderMap.size()+1);
	        detailOrderMap.put("excelDepartCode",detailOrderMap.size()+1);
	        detailOrderMap.put("excelDepartName",detailOrderMap.size()+1);
	        detailOrderMap.put("excelInvoiceDate",detailOrderMap.size()+1);
	        detailOrderMap.put("excelPrice",detailOrderMap.size()+1);
	        detailOrderMap.put("excelQuantity",detailOrderMap.size()+1);
	        detailOrderMap.put("excelLogicInventoryName",detailOrderMap.size()+1);
	        detailOrderMap.put("excelReferenceUnitCode",detailOrderMap.size()+1);
	    }
		
		int r = 3;
		for (r = 3; r < sheetLength; r++) {
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			
			//目前导入字段顺序按照Excel定义的顺序，没有定义按照雅芳的顺序
		    //雅芳字段：入库单编号 产品厂商编码 产品条码 产品名称 部门编码 部门名称 入库日期 价格 数量 
		    //其他字段：逻辑仓库名称 厂商编码（备用）
			
	        // 入库单据号
			String excelBillNoIF = "";
			int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelBillNoIF"))-1;
			if(rowIndex >= 0){
			    excelBillNoIF = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
            rowMap.put("excelBillNoIF", excelBillNoIF);
            
			// 产品厂商编码
            String excelUnitCode = "";
            rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelUnitCode"))-1;
            if(rowIndex >= 0){
                excelUnitCode = dateSheet.getCell(rowIndex, r).getContents().trim();
            }
			rowMap.put("excelUnitCode", excelUnitCode);
			
			// 产品条码
		    String excelBarCode = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelBarCode"))-1;
			if(rowIndex >= 0){
			    excelBarCode = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelBarCode", excelBarCode);
			
			// 产品名称
		    String excelProductName = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelProductName"))-1;
			if(rowIndex >= 0){
			    excelProductName = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelProductName", excelProductName);
			
			// 部门编码
            String excelDepartCode = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelDepartCode"))-1;
			if(rowIndex >= 0){
			    excelDepartCode = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelDepartCode", excelDepartCode);
			
			// 部门名称
            String excelDepartName = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelDepartName"))-1;
			if(rowIndex >= 0){
			    excelDepartName = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelDepartName", excelDepartName);
			
			// 入库日期
			String excelInvoiceDate = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelInvoiceDate"))-1;
			if(rowIndex >= 0){
			    excelInvoiceDate = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelInvoiceDate", excelInvoiceDate);
			
			//执行价
			String excelPrice = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelPrice"))-1;
			if(rowIndex >= 0){
			    excelPrice = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelPrice", excelPrice);
			
			// 产品数量
			String excelQuantity = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelQuantity"))-1;
			if(rowIndex >= 0){
			    excelQuantity = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelQuantity", excelQuantity);
			
			// 逻辑仓库名称
			String excelLogicInventoryName = "";
			rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelLogicInventoryName"))-1;
			if(rowIndex >= 0){
			    excelLogicInventoryName = dateSheet.getCell(rowIndex, r).getContents().trim();
			}
			rowMap.put("excelLogicInventoryName", excelLogicInventoryName);
			
            // 厂商编码(备用)
            String excelReferenceUnitCode = "";
            rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelReferenceUnitCode"))-1;
            if(rowIndex >= 0){
                excelReferenceUnitCode = dateSheet.getCell(rowIndex, r).getContents().trim();
            }
            rowMap.put("excelReferenceUnitCode", excelReferenceUnitCode);
			
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelUnitCode)
					&& CherryChecker.isNullOrEmpty(excelReferenceUnitCode)
					&& CherryChecker.isNullOrEmpty(excelBarCode)
					&& CherryChecker.isNullOrEmpty(excelProductName)
					&& CherryChecker.isNullOrEmpty(excelDepartCode)
					&& CherryChecker.isNullOrEmpty(excelDepartName)
					&& CherryChecker.isNullOrEmpty(excelInvoiceDate)
					&& CherryChecker.isNullOrEmpty(excelPrice)
					&& CherryChecker.isNullOrEmpty(excelQuantity)
					&& CherryChecker.isNullOrEmpty(excelLogicInventoryName)
					&& CherryChecker.isNullOrEmpty(excelBillNoIF)) {
				break;

			}
			// 数据基本格式校验
			this.checkData(rowMap,detailOrderMap);
			
			/**校验填写的入库单据号是否在【入库单主表】中已经存在*/
			if(!CherryChecker.isNullOrEmpty(excelBillNoIF, true)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
				paramMap.put("billNoIF", excelBillNoIF);
				List<Map<String, Object>> repeatList = binOLSTIOS09_Service.getRepeatBillNo(paramMap);
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
			/**
			 * 票号：NEWWITPOS-2059
			 * 新增了【入库单据号】列：
			 * 1、拆分单据时需加上入库单据号
			 * 2、校验当有单据号时，相同入库单据号的其入库部门、入库日期、入库逻辑仓库必须相同
			 * 
			 */
			// 根据部门code、入库日期、逻辑仓库拆分为单据
			String importDatasKey = excelDepartCode + excelInvoiceDate + excelLogicInventoryName + excelBillNoIF;
			if (importDatas.containsKey(importDatasKey)) {
				// 一条主单中的明细
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) importDatas
						.get(importDatasKey);
				/**
				 * 在相同主单下的明细中不允许出现相同产品不同价格的数据
				 * 票号：WITPOSQA-16893：相同产品不同价格，价格取平均值（平均值=总金额/总数量）
				 */
//				for(Map<String, Object> detailMap : detailList) {
//					if(detailMap.get("excelUnitCode").equals(excelUnitCode) && detailMap.get("excelBarCode").equals(excelBarCode)){
//						if(!ConvertUtil.getString(detailMap.get("excelPrice")).equals(priceValue)) {
//							// 一条主单据中的相同产品的价格必须一致，入库部门号：【{0}】，入库日期：【{1}】，入库逻辑仓库：【{2}】，入库单据号：【{3}】，厂商编码：【{4}】，产品条码：【{5}】
//							throw new CherryException("STM00055",new String[]{excelDepartCode,excelInvoiceDate,excelLogicInventoryName,excelBillNoIF,excelUnitCode,excelBarCode});
//						}
//					}
//				}
				
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			} else {
				if(importDatas.containsKey(excelBillNoIF)) {
					// 入库单据号：【{0}】对应的入库部门、入库日期、入库逻辑仓库必须一致，请核对后重新导入！
					throw new CherryException("STM00043",new String[]{excelBillNoIF});
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
		String sysDate = binOLSTIOS09_Service.getSYSDate();
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
				int BIN_ProductInDepotExcelID = binOLSTIOS09_Service
						.insertProductInDepotExcel(mainMap);
				String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductInDepotExcelID", BIN_ProductInDepotExcelID);
					map.put("ErrorMsg", totalErrorMsg+map.get("ErrorMsg"));
					binOLSTIOS09_Service.insertProductInDepotDetailExcel(map);
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
//					String price = ConvertUtil.getString(detailGroupMap.get("Price")).trim();
					/****相同产品不同价格的明细：数量相加，价格取平均***/
					//以UnitCode+BarCode作为合并依据
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
				//合并后明细转换为入库单明细List
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
				pramMap.put("CurrentUnit", "BINOLSTIOS09");
				pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
				pramMap.put("UserInfo", userinfo);
				pramMap.put("BrandCode", userinfo.getBrandCode());
				long workFlowId = 0;
				workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
				
				if (workFlowId == 0) {
					throw new CherryException("EWF00001");
				}
				mainMap.put("ImportResult", "1");
				mainMap.put("WorkFlowID", workFlowId);
				// 将数据添加到入库单（Excel导入）主表和明细表
				int BIN_ProductInDepotExcelID = binOLSTIOS09_Service
						.insertProductInDepotExcel(mainMap);
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductInDepotExcelID", BIN_ProductInDepotExcelID);
					map.put("ErrorMsg", PropertiesUtil.getText("STM00014"));
					binOLSTIOS09_Service.insertProductInDepotDetailExcel(map);
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
		String brandInfoId = ConvertUtil.getString(sessionMap.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(sessionMap.get("organizationInfoId"));
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
			// 一单中部门信息、入库日期相同，只需一次验证
			Map<String, Object> infoMap = detailList.get(0);
			// 校验部门名称
			String departName = ConvertUtil.getString(infoMap.get("excelDepartName"));
			if(CherryChecker.isNullOrEmpty(departName)){
				resultFlag = true;
				commonErrorsMap.put("departNameNull", true);
			}else if (departName.length() > 50) {
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
			} else{
				commonInfo.put("ImportDepartCode", departCode);
				sessionMap.put("DepartCode", departCode);
				//根据部门code获取部门信息
				Map<String, Object> orgMap = binOLSTIOS09_Service.getOrgInfo(sessionMap);
				if (orgMap == null) {
					resultFlag = true;
					commonErrorsMap.put("departCodeExits", true);
				} else {
					commonInfo.put("departType", orgMap.get("Type"));
					//部门类型为柜台，根据系统配置项判断是否允许给柜台入库
					if("4".equals(orgMap.get("Type"))){
						if(!binOLCM14_BL.isConfigOpen("1094",organizationInfoId,brandInfoId)){
							resultFlag = true;
							commonErrorsMap.put("counterError", true);
						}
					}
					if(!commonInfo.containsKey("counterError")){
						// 校验正确，取实体仓库
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
				}
			}
			
			//校验逻辑仓库名称
			String logicInventoryName = ConvertUtil.getString(infoMap.get("excelLogicInventoryName"));
			if(CherryChecker.isNullOrEmpty(logicInventoryName) && !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))){
				//逻辑仓库为为空，根据部门类型获取指定类型逻辑仓库
				if("4".equals(commonInfo.get("departType"))){
					sessionMap.put("Type", "1");
				}else{
					sessionMap.put("Type", "0");
				}
				Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(sessionMap);
				commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
				commonInfo.put("LogicInventoryName", logicInventoryInfo.get("InventoryNameCN"));
			}else if(!CherryChecker.isNullOrEmpty(logicInventoryName) && logicInventoryName.length() > 30){
				resultFlag = true;
				commonErrorsMap.put("logicInventoryNameLength", true);
				commonErrorsMap.put("logicInventoryName", logicInventoryName);
			}else if(!CherryChecker.isNullOrEmpty(logicInventoryName) && !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))){
				commonInfo.put("LogicInventoryName", logicInventoryName);
				//逻辑仓库不为空，根据部门类型获取指定类型的逻辑仓库，并判断逻辑仓库是否存在
				if("4".equals(commonInfo.get("departType"))){
					sessionMap.put("Type", "1");
				}else{
					sessionMap.put("Type", "0");
				}
				sessionMap.put("InventoryName", logicInventoryName);
				Map<String, Object> logicInventoryInfo = binOLSTIOS09_Service.getLogicInventoryByName(sessionMap);
				if(CherryChecker.isNullOrEmpty(logicInventoryInfo)){
					resultFlag = true;
					commonErrorsMap.put("logicInventoryNameExits", true);
				}else{
					commonInfo.putAll(logicInventoryInfo);
				}
			}else{
				commonInfo.put("LogicInventoryName", logicInventoryName);
			}
			
			// 入库日期校验
			String inDepotDate = ConvertUtil.getString(infoMap.get("excelInvoiceDate"));
			if(CherryChecker.isNullOrEmpty(inDepotDate, true)){
				resultFlag = true;
				commonErrorsMap.put("inDepotDateNull", true);
			}else if (CherryChecker.checkDate(inDepotDate, "yyyy-MM-dd")) {
				commonInfo.put("InDepotDate", inDepotDate);
				String bussinessDate = binOLSTIOS09_Service.getBussinessDate(sessionMap);
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
//				commonInfo.put("BillNo", billNoIF);
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
			// 备用的厂商编码
			String unitCodeReference = ConvertUtil.getString(detailMap.get("excelReferenceUnitCode"));
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
				sessionMap.put("UnitCodeReference", unitCodeReference);
				/****产品厂商编码在系统中不存在，则采用备用的厂商编码进行查询*****/
				List<Map<String, Object>> prtInfoList = binOLSTIOS09_Service.getPrtInfo(sessionMap);
				// 厂商编码(包括备用厂商编码)都不存在，则此条记录导入失败
				if(prtInfoList == null || prtInfoList.size() == 0) {
					resultFlag = true;
					errorsMap.put("unitCodeExits", true);
					errorsMap.put("unitCode", unitCode);
					errorsMap.put("unitCodeReference", unitCodeReference);
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
					//配置项 产品发货入库使用价格（销售价格/会员价格/结算价）
		            String priceColName = binOLCM14_BL.getConfigValue("1130", organizationInfoId,brandInfoId);
				    String referencePrice = ConvertUtil.getString(getPrice(binOLSTIOS09_Service.getPrtPrice(sessionMap),detailMap.get("InDepotDate"), priceColName));
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
	 * 取得入库日期时的销售价格
	 * 
	 * @param priceList
	 *            按照开始时间排序的产品价格信息
	 * @param inDepotDate
	 *            入库时间
	 * @param priceColName
	 * 			    价格字段名
	 * @return
	 */
	public Object getPrice(List<Map<String, Object>> priceList, Object inDepotDate, String priceColName) {
		if(priceColName.equals("")){
            priceColName = "SalePrice";
        }
		int length = 0;
		if(!CherryChecker.isNullOrEmpty(priceList)){
			length = priceList.size();
		}else{
			return 0;
		}
		if(length == 0){
			return 0;
		} else if (length == 1) {
			return priceList.get(0).get(priceColName);
		} else {
			for (int i = 0; i < length; i++) {
				Map<String, Object> map = priceList.get(i);
				String inDepotDateStr = ConvertUtil.getString(inDepotDate);
				// 当入库日期小于所有价格的日期
				if (i == 0
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("StartDate"))) <= 0) {
					return map.get(priceColName);
				}
				if (CherryChecker.compareDate(inDepotDateStr,
						ConvertUtil.getString(map.get("StartDate"))) >= 0
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("EndDate"))) <= 0) {
					return map.get(priceColName);
				}
				// 当入库日期大于所有的价格日期
				if (i == (length - 1)
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("EndDate"))) >= 0) {
					return map.get(priceColName);
				}
			}
		}
		return 0;

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
		String name = "BINOLSTIOS09_BL";
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
	public boolean checkData(Map<String, Object> rowMap,Map<String,Object> detailOrderMap) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		// 厂商编码
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		// 备用厂商编码
		String excelReferenceUnitCode = ConvertUtil.getString(rowMap.get("excelReferenceUnitCode"));
		// 产品条码
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelProductName = ConvertUtil.getString(rowMap.get("excelProductName"));
		String excelDepartCode = ConvertUtil.getString(rowMap.get("excelDepartCode"));
		String excelDepartName = ConvertUtil.getString(rowMap.get("excelDepartName"));
		String excelInvoiceDate = ConvertUtil.getString(rowMap.get("excelInvoiceDate"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelLogicInventoryName = ConvertUtil.getString(rowMap.get("excelLogicInventoryName"));
		String excelBillNoIF = ConvertUtil.getString(rowMap.get("excelBillNoIF"));
		
        if(CherryChecker.isNullOrEmpty(excelUnitCode, true) || excelUnitCode.length() > 20){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelUnitCode"))-1;
            if(rowIndex <0){
                  throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelUnitCode"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(excelReferenceUnitCode.length() > 20){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelReferenceUnitCode"))-1;
            if(rowIndex <0){
                  throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelReferenceUnitCode"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        // 校验厂商编码与备用厂商编码至少有一个
        if(excelBarCode.length() > 13){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelBarCode"))-1;
            if(rowIndex <0){
                  throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelBarCode"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelProductName, true) || excelProductName.length() > 50){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelProductName"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelProductName"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelDepartCode, true) || excelDepartCode.length() > 15){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelDepartCode"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelDepartCode"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelDepartName, true) || excelDepartName.length() > 50){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelDepartName"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelDepartName"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(!CherryChecker.checkDate(excelInvoiceDate) || !CherryChecker.checkDate(excelInvoiceDate, "yyyy-DD-mm")){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelInvoiceDate"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelInvoiceDate"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(!CherryChecker.isNullOrEmpty(excelPrice, true) && !CherryChecker.isFloatValid(excelPrice,14,2)){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelPrice"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelPrice"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isPositiveAndNegative(excelQuantity) ||  excelQuantity.length() > 9){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelQuantity"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelQuantity"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(excelLogicInventoryName.length()>30){
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelLogicInventoryName"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelLogicInventoryName"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
        }
        if(excelBillNoIF.length() > 40) {
            int rowIndex = CherryUtil.obj2int(detailOrderMap.get("excelBillNoIF"))-1;
            if(rowIndex <0){
                throw new CherryException("EBS00121",new String[]{CherryConstants.DESCRIPTION_SHEET_NAME,"excelBillNoIF"});
            }
            char c = (char) (65 + rowIndex);
            throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,c+rowNumber});
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
		// 备用厂商编码
		String excelUnitCodeReference = ConvertUtil.getString(rowMap.get("excelReferenceUnitCode"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramsMap.put("departCode", excelDepartCode);
		paramsMap.put("inDepotDate", excelInvoiceDate);
		paramsMap.put("unitCode", excelUnitCode);
		paramsMap.put("unitCodeReference", excelUnitCodeReference);
		paramsMap.put("quantity", excelQuantity);
		paramsMap.put("barCode", excelBarCode);
		paramsMap.put("price", excelPrice);
		List<Map<String, Object>> repeartData = binOLSTIOS09_Service.getRepeatData(paramsMap);
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
		return binOLSTIOS09_Service.insertImportBatch(paramsMap);
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
