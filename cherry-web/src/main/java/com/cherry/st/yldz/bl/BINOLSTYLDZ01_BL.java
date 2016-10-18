package com.cherry.st.yldz.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.st.sfh.bl.BINOLSTSFH17_BL;
import com.cherry.st.sfh.service.BINOLSTSFH17_Service;
import com.cherry.st.yldz.interfaces.BINOLSTYLDZ01_IF;
import com.cherry.st.yldz.service.BINOLSTYLDZ01_Service;

public class BINOLSTYLDZ01_BL  implements BINOLSTYLDZ01_IF{
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTYLDZ01_BL.class);
	@Resource
	private BINOLSTYLDZ01_Service binOLSTYLDZ01_Service;
	@Resource(name="binOLSTSFH17_Service")
	private BINOLSTSFH17_Service binOLSTSFH17_Service;
	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0";
	@Override
	public int getSaleListCount(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.getSaleListCount(map);
	}

	@Override
	public List<Map<String,Object>> getSaleList(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.getSaleList(map);
	}

	@Override
	public Map<String, Object> editInit(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.editInit(map);
	}

	@Override
	public int delete(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.delete(map);
	}

	@Override
	public void addBankBill(Map<String, Object> map) {
		binOLSTYLDZ01_Service.addBankBill(map);
	}

	@Override
	public int updateBankBill(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.updateBankBill(map);
	}
	
	@Override
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> map) throws Exception {
		
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
		// 字段说明SHEET
		Sheet descriptSheet = null;
		// 订货数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.BANKBILL_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 判断模板版本号
		if (null == descriptSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.DESCRIPTION_SHEET_NAME });
		} else {
			// 版本号（B）
			String version = descriptSheet.getCell(1, 0).getContents().trim();
			if (!EXCEL_VERSION.equals(version)) {
				// 模板版本不正确，请下载最新的模板进行导入！
				throw new CherryException("EBS00103");
			}
		}
		// 订货数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.BANKBILL_SHEET_NAME });
		}
		
		// 订货数据sheet的行数
		int sheetLength = dateSheet.getRows();
		
		// 是否从Excel中获取导入批次号
		String isChecked = ConvertUtil.getString(map.get("isChecked"));
		// 从Excel获取导入批次号
		String importBatchCode = dateSheet.getCell(1, 0).getContents().trim();
		// 导入批次号校验
		if(!CherryChecker.isNullOrEmpty(isChecked, true) && "checked".equals(isChecked)){
			// 空校验
			if(CherryChecker.isNullOrEmpty(importBatchCode, true)){
				throw new CherryException("ECM00009",new String[]{PropertiesUtil.getText("STM00012")});
			}
			// 您导入的批次号为+【importBatchCode】
			String currentImportBatchCode = PropertiesUtil.getText("STM00029")+"【" + importBatchCode+"】！";
			map.put("currentImportBatchCode", currentImportBatchCode);
			if(!CherryChecker.isAlphanumeric(importBatchCode)){
				// 数据格式校验
				throw new CherryException("ECM00031",new String[]{currentImportBatchCode+PropertiesUtil.getText("STM00012")});
			}else if(importBatchCode.length() > 25){
				// 长度校验
				throw new CherryException("ECM00020",new String[]{currentImportBatchCode+PropertiesUtil.getText("STM00012"),"25"});
			}else {
				// 重复校验
				map.put("importBatchCodeR", importBatchCode);
				if(this.getImportBatchCount(map) > 0){
					throw new CherryException("ECM00032",new String[]{PropertiesUtil.getText("STM00012")+"("+importBatchCode+")"});
				}
				map.put("ImportBatchCode", importBatchCode);
			}
		}else{
			// 此处不进行重复校验（importBatchCode参数不是由EXCEL提供，已预校验过）
			importBatchCode = ConvertUtil.getString(map.get("ImportBatchCode"));
			if(this.getImportBatchCount(map) > 0){
				throw new CherryException("ECM00032",new String[]{PropertiesUtil.getText("STM00012")+"("+importBatchCode+")"});
			}
			String currentImportBatchCode = PropertiesUtil.getText("STM00029")+"【" + importBatchCode+"】！";
			map.put("currentImportBatchCode", currentImportBatchCode);
		}
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		int r = 3;
		for (r = 3; r < sheetLength; r++) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			// 交易日期
			String excelTradeDate = dateSheet.getCell(0, r).getContents().trim();
			rowMap.put("tradeDate", excelTradeDate);
			// 交易时间
			String excelTradeTime = dateSheet.getCell(1, r).getContents().trim();
			rowMap.put("tradeTime", excelTradeTime);
			// 卡号
			String excelCardCode = dateSheet.getCell(2, r).getContents().trim();
			rowMap.put("cardCode", excelCardCode);
			// 商户号
			String excelCompanyCode = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("companyCode", excelCompanyCode);
			// 商户名
			String excelCompanyName = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("companyName", excelCompanyName);
			// 终端号
			String excelPosCode = dateSheet.getCell(5, r).getContents().trim();
			rowMap.put("posCode", excelPosCode);
			// 终端流水号
			String excelPosBillCode = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("posBillCode", excelPosBillCode);
			// 系统流水号
			String excelSysBillCode = dateSheet.getCell(7, r).getContents().trim();
			rowMap.put("sysBillCode", excelSysBillCode);
			// 冲正流水号
			String excelHedgingBillCode = dateSheet.getCell(8, r).getContents().trim();
			rowMap.put("hedgingBillCode", excelHedgingBillCode);
			// 参考号
			String excelReferenceCode = dateSheet.getCell(9, r).getContents().trim();
			rowMap.put("referenceCode", excelReferenceCode);
			// 交易金额
			String excelAmount = dateSheet.getCell(10, r).getContents().trim();
			if(null!=excelAmount && !"".equals(excelAmount)){
				rowMap.put("amount", excelAmount);
			}else {
				rowMap.put("amount", "0");
			}
			// 交易类型
			String excelTradeType = dateSheet.getCell(11, r).getContents().trim();
			rowMap.put("tradeType", excelTradeType);
			// 交易结果
			String excelTradeResult = dateSheet.getCell(12, r).getContents().trim();
			rowMap.put("tradeResult", excelTradeResult);
			// 交易应答
			String excelTradeAnswer = dateSheet.getCell(13, r).getContents().trim();
			rowMap.put("tradeAnswer", excelTradeAnswer);
			rowMap.put("importRepeat", map.get("importRepeat"));
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelPosBillCode)
					&& CherryChecker.isNullOrEmpty(excelSysBillCode)
					&& CherryChecker.isNullOrEmpty(excelTradeType)) {
				break;
			}
			// 数据基本格式校验
			this.checkData(rowMap,detailList);
			detailList.add(rowMap);
		}
		if(r==3){
			throw new CherryException("EBS00035",new String[] { CherryConstants.BANKBILL_SHEET_NAME });
		}
		return detailList;
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @return
	 * @throws Exception
	 */
	public boolean checkData(Map<String, Object> rowMap,List<Map<String, Object>> detailList) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		String tradeDate = ConvertUtil.getString(rowMap.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(rowMap.get("tradeTime"));
		String cardCode = ConvertUtil.getString(rowMap.get("cardCode"));
		String companyCode = ConvertUtil.getString(rowMap.get("companyCode"));
		String companyName = ConvertUtil.getString(rowMap.get("companyName"));
		String posCode = ConvertUtil.getString(rowMap.get("posCode"));
		String posBillCode = ConvertUtil.getString(rowMap.get("posBillCode"));
		String sysBillCode = ConvertUtil.getString(rowMap.get("sysBillCode"));
		String hedgingBillCode = ConvertUtil.getString(rowMap.get("hedgingBillCode"));
		String referenceCode = ConvertUtil.getString(rowMap.get("referenceCode"));
		String amount = ConvertUtil.getString(rowMap.get("amount"));
		String tradeType = ConvertUtil.getString(rowMap.get("tradeType"));
		String tradeResult = ConvertUtil.getString(rowMap.get("tradeResult"));
		String tradeAnswer = ConvertUtil.getString(rowMap.get("tradeAnswer"));
		String importRepeat = ConvertUtil.getString(rowMap.get("importRepeat"));
		if(CherryChecker.isNullOrEmpty(tradeDate, true) || !CherryChecker.checkDate(tradeDate)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"A"+rowNumber});
		}
		if(!CherryChecker.isNullOrEmpty(tradeTime, true) && !CherryChecker.checkTime(tradeTime)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"B"+rowNumber});
		}
		if(cardCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"C"+rowNumber});
		}
		if(companyCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"D"+rowNumber});
		}
		if(companyName.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"E"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(posCode, true) || posCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"F"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(posBillCode, true) || posBillCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"G"+rowNumber});
		}
		if(sysBillCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"H"+rowNumber});
		}
		if(hedgingBillCode.length() > 30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"I"+rowNumber});
		}
		if(referenceCode.length() > 50){
			throw new CherryException("STM00060",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"J"+rowNumber});
		}
		if(!CherryChecker.isDecimal(amount, 6, 2)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"K"+rowNumber});
		}
		if(tradeType.length() > 20){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"L"+rowNumber});
		}
		if(tradeResult.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"M"+rowNumber});
		}
		if(tradeAnswer.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"N"+rowNumber});
		}
		if(!"1".equals(importRepeat) && this.checkBillCode(rowMap)>0){
			throw new CherryException("STM00061",new String[]{CherryConstants.BANKBILL_SHEET_NAME,"J"+rowNumber});
		}
		if(detailList!=null && !detailList.isEmpty()){
			for(Map<String, Object> m : detailList){
				String posCodes = ConvertUtil.getString(m.get("posCode"));
				String posBillCodes = ConvertUtil.getString(m.get("posBillCode"));
				String tradeDates = ConvertUtil.getString(m.get("tradeDate"));
				String rowNumbers = ConvertUtil.getString(m.get("rowNumber"));
				if(posCodes.equals(posCode) && posBillCodes.equals(posBillCode)  && tradeDates.equals(tradeDate)){
					if("1".equals(importRepeat)){
						m = rowMap;
					}else {
						throw new CherryException("STM00060",new String[]{CherryConstants.BANKBILL_SHEET_NAME,rowNumber+","+rowNumbers});
					}
				}
			}
		}
		return true;
	}
	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		return binOLSTSFH17_Service.getImportBatchCount(map);
	}
	@Override
	public int checkBillCode(Map<String, Object> map) {
		return binOLSTYLDZ01_Service.checkBillCode(map);
	}
	@Override
	public Map<String, Object> tran_excelHandle(List<Map<String, Object>> importDataList,Map<String, Object> sessionMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入失败的单据
		List<Map<String, Object>> errorSaleInfoList = new ArrayList<Map<String, Object>>();
		int successCount=0;
		int failCount=0;
		// 插入导入批次信息返回导入批次ID
		int importBatchId = binOLSTSFH17_Service.insertImportBatch(sessionMap);
		try {
			for(Map<String, Object> map : importDataList){
				map.put("updatedBy", ConvertUtil.getString(sessionMap.get("UpdatedBy")));
				map.put("updatePGM", ConvertUtil.getString(sessionMap.get("UpdatePGM")));
				map.put("createdBy", ConvertUtil.getString(sessionMap.get("CreatedBy")));
				map.put("createPGM", ConvertUtil.getString(sessionMap.get("CreatePGM")));
				map.put("batchId", importBatchId);
				map.put("brandInfoId", sessionMap.get("brandInfoId"));
				map.put("organizationInfoId", sessionMap.get("organizationInfoId"));
				try {
					binOLSTYLDZ01_Service.addBankBill(map);
					successCount++;
				} catch (Exception e) {
					failCount++;
					errorSaleInfoList.add(map);
					logger.error("错误："+e.getMessage(),e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		resultMap.put("errorSaleInfoList", errorSaleInfoList);
		return resultMap;
	}
}
