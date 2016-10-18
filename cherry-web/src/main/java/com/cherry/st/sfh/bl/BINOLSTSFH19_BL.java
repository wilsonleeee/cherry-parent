package com.cherry.st.sfh.bl;

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

import com.cherry.bs.dep.bl.BINOLBSDEP02_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.common.service.BINOLSTCM10_Service;
import com.cherry.st.sfh.interfaces.BINOLSTSFH19_IF;
import com.cherry.st.sfh.service.BINOLSTSFH19_Service;

public class BINOLSTSFH19_BL implements BINOLSTSFH19_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH19_BL.class);
	
	@Resource(name="binOLSTSFH19_Service")
	private BINOLSTSFH19_Service binOLSTSFH19_Service;
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_BL binOLCM18_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="CodeTable")
	private CodeTable CodeTable;
	@Resource(name="binOLSTCM19_BL")
	private BINOLSTCM19_IF binOLSTCM19_BL;
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	@Resource(name="binOLBSDEP02_BL")
	private BINOLBSDEP02_BL binOLBSDEP02_BL;
	
	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.0";
	
	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH19_Service.getImportBatchCount(map);
	}

	@Override
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		return binOLSTSFH19_Service.getImportBatchList(map);
	}

	@Override
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception {
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
		// 销售数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.BACKSTAGESALE_SHEET_NAME.equals(st.getName().trim())) {
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
		// 销售数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.BACKSTAGESALE_SHEET_NAME });
		}
		
		// 销售数据sheet的行数
		int sheetLength = dateSheet.getRows();
		// sheet中的数据
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
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
				map.put("importBatchCode", importBatchCode);
			}
		} else {
			// 此处不进行重复校验（importBatchCode参数不是由EXCEL提供，已预校验过）
			importBatchCode = ConvertUtil.getString(map.get("importBatchCode"));
			String currentImportBatchCode = PropertiesUtil.getText("STM00029")+"【" + importBatchCode+"】！";
			map.put("currentImportBatchCode", currentImportBatchCode);
		}
		// 有效数据从第4行开始
		int r = 3;
		for(;r < sheetLength; r++) {
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
			// 销售部门编码
			String excelDepartCodeSale = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("excelDepartCodeSale", excelDepartCodeSale);
			// 销售部门名称
			String excelDepartNameSale = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("excelDepartNameSale", excelDepartNameSale);
			// 销售实体仓库名称
			String excelDepotNameSale = dateSheet.getCell(5, r).getContents().trim();
			rowMap.put("excelDepotNameSale", excelDepotNameSale);
			// 销售逻辑仓库名称
			String excelLogicInventoryNameSale = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("excelLogicInventoryNameSale", excelLogicInventoryNameSale);
			// 销售日期
			String excelSaleDate = dateSheet.getCell(7, r).getContents().trim();
			rowMap.put("excelSaleDate", excelSaleDate);
			// 订单类型
			String excelSaleBillType = dateSheet.getCell(8, r).getContents().trim();
			rowMap.put("excelSaleBillType", excelSaleBillType);
			// 客户类型
			String excelCustomerType = dateSheet.getCell(9, r).getContents().trim();
			rowMap.put("excelCustomerType", excelCustomerType);
			// 客户部门名称
			String excelDepartCodeCustomer = dateSheet.getCell(10, r).getContents().trim();
			rowMap.put("excelDepartCodeCustomer", excelDepartCodeCustomer);
			// 客户部门名称
			String excelDepartNameCustomer = dateSheet.getCell(11, r).getContents().trim();
			rowMap.put("excelDepartNameCustomer", excelDepartNameCustomer);
			// 客户实体仓库名称
			String excelDepotNameCustomer = dateSheet.getCell(12, r).getContents().trim();
			rowMap.put("excelDepotNameCustomer", excelDepotNameCustomer);
			// 客户逻辑仓库名称
			String excelLogicInventoryNameCustomer = dateSheet.getCell(13, r).getContents().trim();
			rowMap.put("excelLogicInventoryNameCustomer", excelLogicInventoryNameCustomer);
			// 送货日期
			String excelExpectFinishDate = dateSheet.getCell(14, r).getContents().trim();
			rowMap.put("excelExpectFinishDate", excelExpectFinishDate);
			// 联系人
			String excelContactPerson = dateSheet.getCell(15, r).getContents().trim();
			rowMap.put("excelContactPerson", excelContactPerson);
			// 送货地址
			String excelDeliverAddress = dateSheet.getCell(16, r).getContents().trim();
			rowMap.put("excelDeliverAddress", excelDeliverAddress);
			// 结算方式
			String excelSettlement = dateSheet.getCell(17, r).getContents().trim();
			rowMap.put("excelSettlement", excelSettlement);
			// 数量
			String excelQuantity = dateSheet.getCell(18, r).getContents().trim();
			rowMap.put("excelQuantity", excelQuantity);
			// 币种
			String excelCurrency = dateSheet.getCell(19, r).getContents().trim();
			rowMap.put("excelCurrency", excelCurrency);
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelUnitCode)
					&& CherryChecker.isNullOrEmpty(excelBarCode)
					&& CherryChecker.isNullOrEmpty(excelProductName)
					&& CherryChecker.isNullOrEmpty(excelDepartCodeSale)
					&& CherryChecker.isNullOrEmpty(excelDepartNameSale)
					&& CherryChecker.isNullOrEmpty(excelDepotNameSale)
					&& CherryChecker.isNullOrEmpty(excelLogicInventoryNameSale)
					&& CherryChecker.isNullOrEmpty(excelSaleDate)
					&& CherryChecker.isNullOrEmpty(excelSaleBillType)
					&& CherryChecker.isNullOrEmpty(excelCustomerType)
					&& CherryChecker.isNullOrEmpty(excelDepartCodeCustomer)
					&& CherryChecker.isNullOrEmpty(excelDepartNameCustomer)
					&& CherryChecker.isNullOrEmpty(excelDepotNameCustomer)
					&& CherryChecker.isNullOrEmpty(excelLogicInventoryNameCustomer)
					&& CherryChecker.isNullOrEmpty(excelExpectFinishDate)
					&& CherryChecker.isNullOrEmpty(excelContactPerson)
					&& CherryChecker.isNullOrEmpty(excelDeliverAddress)
					&& CherryChecker.isNullOrEmpty(excelSettlement)
					&& CherryChecker.isNullOrEmpty(excelQuantity)
					&& CherryChecker.isNullOrEmpty(excelCurrency)) {
				break;
			}
			// 数据基本格式校验
			this.checkData(rowMap);
			// 是否导入重复数据参数
			String importRepeat = ConvertUtil.getString(map.get("importRepeat"));
			if(!CherryChecker.isNullOrEmpty(importRepeat, true) && "0".equals(importRepeat)){
				// 第{0}行记录在系统中存在重复数据，请修改后重新导入！
				if(this.isRepeatData(rowMap,map)){
					throw new CherryException("BSL00014",new String[] { ConvertUtil.getString(r+1) });
				}
			}
			
			/**
			 * 根据销售部门code、销售部门实体仓库、销售部门逻辑仓库、销售日期、
			 * 销售单类型、客户类型、客户部门CODE、客户实体仓库、客户逻辑仓库、
			 * 送货日期、联系人、联系地址、结算方式、币种拆分为一单
			 */
			String importDatasKey = excelDepartCodeSale + excelDepotNameSale
					+ excelLogicInventoryNameSale + excelSaleDate
					+ excelSaleBillType + excelCustomerType
					+ excelDepartCodeCustomer + excelDepotNameCustomer
					+ excelLogicInventoryNameCustomer + excelExpectFinishDate
					+ excelContactPerson + excelDeliverAddress
					+ excelSettlement + excelCurrency;

			if (importDatas.containsKey(importDatasKey)) {
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) importDatas
						.get(importDatasKey);
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			} else {
				List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			}
			
		}
		if (r == 3) {
			throw new CherryException("EBS00035",
					new String[] { CherryConstants.BACKSTAGESALE_SHEET_NAME });
		}
		return importDatas;
	}

	/**
	 * 校验导入的数据是否已经存在于数据库中
	 * @param rowMap
	 * @param map
	 * @return
	 */
	private boolean isRepeatData(Map<String, Object> rowMap,
			Map<String, Object> map) {
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelDepartCodeSale = ConvertUtil.getString(rowMap.get("excelDepartCodeSale"));
		String excelDepotNameSale = ConvertUtil.getString(rowMap.get("excelDepotNameSale"));
		String excelLogicInventoryNameSale = ConvertUtil.getString(rowMap.get("excelLogicInventoryNameSale"));
		String excelSaleDate = ConvertUtil.getString(rowMap.get("excelSaleDate"));
		String excelSaleBillType = ConvertUtil.getString(rowMap.get("excelSaleBillType"));
		String excelCustomerType = ConvertUtil.getString(rowMap.get("excelCustomerType"));
		String excelDepartCodeCustomer = ConvertUtil.getString(rowMap.get("excelDepartCodeCustomer"));
		String excelDepotNameCustomer = ConvertUtil.getString(rowMap.get("excelDepotNameCustomer"));
		String excelLogicInventoryNameCustomer = ConvertUtil.getString(rowMap.get("excelLogicInventoryNameCustomer"));
		String excelExpectFinishDate = ConvertUtil.getString(rowMap.get("excelExpectFinishDate"));
		String excelContactPerson = ConvertUtil.getString(rowMap.get("excelContactPerson"));
		String excelDeliverAddress = ConvertUtil.getString(rowMap.get("excelDeliverAddress"));
		String excelSettlement = ConvertUtil.getString(rowMap.get("excelSettlement"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelCurrency = ConvertUtil.getString(rowMap.get("excelCurrency"));
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramMap.put("departCodeSale", excelDepartCodeSale);
		paramMap.put("depotNameSale", excelDepotNameSale);
		paramMap.put("logicInventoryNameSale", excelLogicInventoryNameSale);
		paramMap.put("saleDate", excelSaleDate);
		paramMap.put("saleBillType", excelSaleBillType);
		paramMap.put("customerType", excelCustomerType);
		paramMap.put("departCodeCustomer", excelDepartCodeCustomer);
		paramMap.put("depotNameCustomer", excelDepotNameCustomer);
		paramMap.put("logicInventoryNameCustomer", excelLogicInventoryNameCustomer);
		paramMap.put("expectFinishDate", excelExpectFinishDate);
		paramMap.put("contactPerson", excelContactPerson);
		paramMap.put("deliverAddress", excelDeliverAddress);
		paramMap.put("settlement", excelSettlement);
		paramMap.put("currency", excelCurrency);
		paramMap.put("unitCode", excelUnitCode);
		paramMap.put("quantity", excelQuantity);
		paramMap.put("barCode", excelBarCode);
		List<Map<String, Object>> repeatData = binOLSTSFH19_Service.getRepeatData(paramMap);
		if(repeatData != null && repeatData.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 对EXCEL导入的数据进行格式的初步校验，个别为空时取默认值的给予赋值其默认值
	 * @param rowMap
	 * @throws Exception
	 */
	private void checkData(Map<String, Object> rowMap) throws Exception {
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelProductName = ConvertUtil.getString(rowMap.get("excelProductName"));
		String excelDepartCodeSale = ConvertUtil.getString(rowMap.get("excelDepartCodeSale"));
		String excelDepartNameSale = ConvertUtil.getString(rowMap.get("excelDepartNameSale"));
		String excelDepotNameSale = ConvertUtil.getString(rowMap.get("excelDepotNameSale"));
		String excelLogicInventoryNameSale = ConvertUtil.getString(rowMap.get("excelLogicInventoryNameSale"));
		String excelSaleDate = ConvertUtil.getString(rowMap.get("excelSaleDate"));
		String excelSaleBillType = ConvertUtil.getString(rowMap.get("excelSaleBillType"));
		String excelCustomerType = ConvertUtil.getString(rowMap.get("excelCustomerType"));
		String excelDepartCodeCustomer = ConvertUtil.getString(rowMap.get("excelDepartCodeCustomer"));
		String excelDepartNameCustomer = ConvertUtil.getString(rowMap.get("excelDepartNameCustomer"));
		String excelDepotNameCustomer = ConvertUtil.getString(rowMap.get("excelDepotNameCustomer"));
		String excelLogicInventoryNameCustomer = ConvertUtil.getString(rowMap.get("excelLogicInventoryNameCustomer"));
		String excelExpectFinishDate = ConvertUtil.getString(rowMap.get("excelExpectFinishDate"));
		String excelContactPerson = ConvertUtil.getString(rowMap.get("excelContactPerson"));
		String excelDeliverAddress = ConvertUtil.getString(rowMap.get("excelDeliverAddress"));
		String excelSettlement = ConvertUtil.getString(rowMap.get("excelSettlement"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelCurrency = ConvertUtil.getString(rowMap.get("excelCurrency"));
		
		// 厂商编码
		if(CherryChecker.isNullOrEmpty(excelUnitCode, true)){
			// {0}sheet单元格[{1}]数据为空！
			throw new CherryException("EBS00031",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"A"+rowNumber});
		} else if(excelUnitCode.length() > 20) {
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"A"+rowNumber,"20"});
		}
		
		// 产品条码
		if(excelBarCode.length() > 13){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"B"+rowNumber,"13"});
		}
		
		// 产品名称【非必填，导入时只作参考】
		if(excelProductName.length() > 50){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"C"+rowNumber,"50"});
		}
		
		// 销售部门编码
		if(CherryChecker.isNullOrEmpty(excelDepartCodeSale, true)){
			// {0}sheet单元格[{1}]数据为空！
			throw new CherryException("EBS00031",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"D"+rowNumber});
		} else if(excelDepartCodeSale.length() > 15) {
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"D"+rowNumber,"15"});
		} else if(excelDepartCodeSale.equals(excelDepartCodeCustomer)) {
			// 第{0}行记录的销售部门不允许与客户部门相同！
			throw new CherryException("BSL00015",new String[]{rowNumber});
		}
		
		// 销售部门名称【非必填，导入时只作参考】
		if(excelDepartNameSale.length() > 50){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"E"+rowNumber,"50"});
		}
		
		// 销售部门实体仓库【非必填】
		if(excelDepotNameSale.length() > 30){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"F"+rowNumber,"30"});
		}
		
		// 销售部门逻辑仓库【非必填】
		if(excelLogicInventoryNameSale.length() > 30){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"G"+rowNumber,"30"});
		}
		
		// 销售日期【格式必须为YYYY-MM-DD，必填】
		if (CherryChecker.isNullOrEmpty(excelSaleDate, true)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.BACKSTAGESALE_SHEET_NAME, "H" + rowNumber });
		} else if (!CherryChecker.checkDate(excelSaleDate)
				|| !CherryChecker.checkDate(excelSaleDate, "yyyy-DD-mm")) {
			throw new CherryException("EBS00034", new String[] {
					CherryConstants.BACKSTAGESALE_SHEET_NAME, "H" + rowNumber });
		}
		
		// 销售单类型【目前CODE值中只有一个值，必填】
		boolean isSaleBillType = false;
		// 销售单类型必须是code值为1293对应的值
		if(!CherryChecker.isNullOrEmpty(excelSaleBillType, true)){
			List<Map<String, Object>> typeList = CodeTable.getCodes("1293");
			for(Map<String, Object> type : typeList){
				if(excelSaleBillType.equals(type.get("Value"))){
					isSaleBillType = true;
					rowMap.put("excelSaleBillType", type.get("CodeKey"));
					break;
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(excelSaleBillType, true) || !isSaleBillType){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"I"+rowNumber});
		}
		
		// 客户类型【非必填，默认类型为：往来单位销售】
		boolean isCustomerType = false;
		// 客户类型必须是code值为1297对应的值【为空时默认为往来单位销售】
		if(!CherryChecker.isNullOrEmpty(excelCustomerType, true)){
			List<Map<String, Object>> typeList = CodeTable.getCodes("1297");
			for(Map<String, Object> type : typeList){
				if(excelCustomerType.equals(type.get("Value"))){
					isCustomerType = true;
					rowMap.put("excelCustomerType", type.get("CodeKey"));
					break;
				}
			}
		} 
		if(CherryChecker.isNullOrEmpty(excelCustomerType, true)){
			// 为空时默认为往来单位销售
			rowMap.put("excelCustomerType", "2");
		} else if(!CherryChecker.isNullOrEmpty(excelCustomerType, true) && !isCustomerType) {
			throw new CherryException("EBS00034",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"J"+rowNumber});
		} 
		
		// 客户部门编码【必填】
		if(CherryChecker.isNullOrEmpty(excelDepartCodeCustomer, true)){
			// {0}sheet单元格[{1}]数据为空！
			throw new CherryException("EBS00031",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"K"+rowNumber});
		} else if(excelDepartCodeCustomer.length() > 15) {
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"K"+rowNumber,"15"});
		}
		
		// 客户部门名称【非必填，导入时只作参考】
		if(excelDepartNameCustomer.length() > 50){
			// {0}sheet单元格[{1}]数据长度超过上限{2}！
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"L"+rowNumber,"50"});
		}
		
		// 客户实体仓库名称【非必填】
		if(excelDepotNameCustomer.length() > 30){
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"M"+rowNumber,"30"});
		}
		
		// 客户逻辑仓库名称【非必填】
		if(excelLogicInventoryNameCustomer.length() > 30){
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"N"+rowNumber,"30"});
		}
		
		// 送货日期【非必填，默认与销售日期一致】
		if (CherryChecker.isNullOrEmpty(excelExpectFinishDate, true)) {
			// 送货日期为空时取销售日期
			rowMap.put("excelExpectFinishDate", rowMap.get("excelSaleDate"));
		} else if (!CherryChecker.checkDate(excelExpectFinishDate)
				|| !CherryChecker.checkDate(excelExpectFinishDate, "yyyy-DD-mm")) {
			throw new CherryException("EBS00034", new String[] {
					CherryConstants.BACKSTAGESALE_SHEET_NAME, "O" + rowNumber });
		}
		
		// 联系人【非必填】
		if(excelContactPerson.length() > 30){
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"P"+rowNumber,"30"});
		}
		
		// 送货地址【非必填】
		if(excelDeliverAddress.length() > 200){
			throw new CherryException("EBS00033",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"Q"+rowNumber,"200"});
		}
		
		// 结算方式【非必填，为空默认"现金"】
		boolean isSettlement = false;
		// 结算方式必须是code值为1175对应的值【为空时默认现金】
		if(!CherryChecker.isNullOrEmpty(excelSettlement, true)){
			List<Map<String, Object>> typeList = CodeTable.getCodes("1175");
			for(Map<String, Object> type : typeList){
				if(excelSettlement.equals(type.get("Value"))){
					isSettlement = true;
					rowMap.put("excelSettlement", type.get("CodeKey"));
					break;
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(excelSettlement, true)) {
			// 为空时默认现金
			rowMap.put("excelSettlement", "CA");
		} else if(!isSettlement){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"R"+rowNumber});
		}
		
		// 数量【必填，必须为大于0的整数且长度为超过9位】
		if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isNumeric(excelQuantity) ||  excelQuantity.length() > 9 || "0".equals(excelQuantity)){
			throw new CherryException("EBS00034",new String[]{CherryConstants.ORDER_SHEET_NAME,"S"+rowNumber});
		} else {
			// 将类似于01的数据转换为1【不将其认为为错误数据】
			rowMap.put("excelQuantity", ConvertUtil.getInt(excelQuantity));
		}
		
		// 币种【非必填，为空时默认为人民币】
		boolean isCurrency = false;
		// 币种必须是code值为1296对应的值【为空时默认为人民币】
		if(!CherryChecker.isNullOrEmpty(excelCurrency, true)){
			List<Map<String, Object>> typeList = CodeTable.getCodes("1296");
			for(Map<String, Object> type : typeList){
				if(excelCurrency.equals(type.get("Value"))){
					isCurrency = true;
					rowMap.put("excelCurrency", type.get("CodeKey"));
					break;
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(excelCurrency, true)) {
			// 为空时默认为人民币
			rowMap.put("excelCurrency", "1");
		} else if(!isCurrency){
			throw new CherryException("EBS00034",new String[]{CherryConstants.BACKSTAGESALE_SHEET_NAME,"T"+rowNumber});
		}
		
		
	}

	@Override
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> map)
			throws Exception {
		UserInfo userInfo = (UserInfo)map.get("userInfo");
		// 导入日期
		String sysDate = binOLSTSFH19_Service.getSYSDate();
		map.put("importDate", sysDate);
		// 插入导入批次信息返回导入批次ID
		int importBatchId = this.insertImportBatch(map);
		
		// 导入结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功的数量
		int successCount = 0;
		// 导入失败的数量
		int failCount = 0;
		// 导入失败的单据
		List<Map<String, Object>> errorSaleInfoList = new ArrayList<Map<String, Object>>();
		Iterator it = importDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry en = (Entry) it.next();
			// 明细数据
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) en.getValue();
			// 验证数据的有效性
			Map<String, Object> detailMap = this.validExcel(detailList, map);
			detailList = (List<Map<String, Object>>) detailMap.get("resultDetailList");
			// 主数据
			Map<String, Object> mainMap = new HashMap<String, Object>();
			if(null != detailList && detailList.size() > 0) {
				// 将校验并补充完整的数据填入主数据【此mainMap包含共通的map参数】
				mainMap.putAll(detailList.get(0));
				String billNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), CherryUtil.obj2int(map.get(CherryConstants.BRANDINFOID)), 
						ConvertUtil.getString(userInfo.getBIN_UserID()), CherryConstants.BILLTYPE_NS);
				mainMap.put("billNo", billNo);
				mainMap.put("billNoIF", billNo);
				// 总数量
				mainMap.put("totalQuantity", detailMap.get("TotalQuantity"));
				// 总金额
				mainMap.put("totalAmount", detailMap.get("TotalAmount"));
				mainMap.put("importDate", sysDate);
				mainMap.put("importBatchId", importBatchId);
				mainMap.put("importBatch", map.get("importBatchCode"));
			} else {
				continue;
			}
			
			if((Boolean) detailMap.get("ResultFlag")) {
				// 验证不通过，只将数据添加到订货（Excel导入）主表和明细表
				mainMap.put("importResult", "0");
				int BIN_BackstageSaleExcelID = binOLSTSFH19_Service
						.insertBackstageSaleExcel(mainMap);
				// 总数量过大时的错误信息
				String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
				for (Map<String, Object> mapDetail : detailList) {
					mapDetail.put("BIN_BackstageSaleExcelID", BIN_BackstageSaleExcelID);
					mapDetail.put("ErrorMsg", totalErrorMsg + mapDetail.get("ErrorMsg"));
					binOLSTSFH19_Service.insertBackstageSaleExcelDetail(mapDetail);
					errorSaleInfoList.add(mapDetail);
				}
				failCount++;
			} else {
				// 验证成功，合并相同产品明细
				Map<String, Object> detailListGroup = new HashMap<String, Object>();
				for (Map<String, Object> fm : detailList) {
					Map<String, Object> detailGroupMap = new HashMap<String, Object>();
					detailGroupMap.putAll(fm);
					String unitCode = ConvertUtil.getString(detailGroupMap.get("unitCode")).trim();
					String barCode = ConvertUtil.getString(detailGroupMap.get("barCode")).trim();
					// 以UnitCode与BarCode作为合并依据
					String mapKey = unitCode + barCode;
					if (detailListGroup.containsKey(mapKey)) {
						Map<String, Object> productMap = (Map<String, Object>) detailListGroup.get(mapKey);
						int quantity = ConvertUtil.getInt(detailGroupMap.get("quantity"))+ConvertUtil.getInt(productMap.get("quantity"));
						productMap.put("quantity", quantity);
						detailListGroup.put(mapKey, productMap);
					} else {
						detailListGroup.put(mapKey, detailGroupMap);
					}
				}
				
				// 合并后明细转换为后台销售单明细List
				List<Map<String, Object>> backstageSaleDetailList = new ArrayList<Map<String, Object>>();
				Iterator detailIt = detailListGroup.entrySet().iterator();
				while (detailIt.hasNext()) {
					Entry detailEn = (Entry) detailIt.next();
					backstageSaleDetailList.add((Map<String, Object>) detailEn.getValue());
				}
				
				/**提交销售单*/
				int saleID = this.insertBackstageSale(backstageSaleDetailList,mainMap);
				
				if(saleID == 0){
					//抛出自定义异常：操作失败！
					throw new CherryException("ISS00005");
				}
				//记录日志
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("BIN_BackstageSaleID", saleID);
				paramMap.put("BIN_EmployeeID", userInfo.getEmployeeCode());
				int saleHistoryID = binOLSTCM19_BL.insertSaleDataHistory(paramMap);
				// 准备参数，开始工作流
				Map<String, Object> pramMap = new HashMap<String, Object>();
				pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_NS);
				pramMap.put(CherryConstants.OS_MAINKEY_BILLID, saleID);
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
				pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
				pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
				pramMap.put("CurrentUnit", "BINOLSTSFH19");
				pramMap.put("UserInfo", userInfo);
				pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
				pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			    pramMap.put("BIN_BackstageSaleHistoryID", saleHistoryID);
			    
			    long workFlowId = 0;
			    workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
			    if (workFlowId == 0) {
					throw new CherryException("EWF00001");
				}
			    mainMap.put("importResult", "1");
				mainMap.put("WorkFlowID", workFlowId);
				// 将数据添加到发货单（Excel导入）主表和明细表
				int BIN_BackstageSaleExcelID = binOLSTSFH19_Service
						.insertBackstageSaleExcel(mainMap);
				for (Map<String, Object> mapDetail : detailList) {
					mapDetail.put("BIN_BackstageSaleExcelID", BIN_BackstageSaleExcelID);
					mapDetail.put("ErrorMsg", PropertiesUtil.getText("STM00014"));
					binOLSTSFH19_Service.insertBackstageSaleExcelDetail(mapDetail);
				}
				successCount++;
			}
		}
		
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		resultMap.put("errorSaleInfoList", errorSaleInfoList);
		return resultMap;
	}

	/**
	 * 将一条后台销售数据写入数据
	 * @param backstageSaleDetailList
	 * @param map
	 */
	private int insertBackstageSale(
			List<Map<String, Object>> backstageSaleDetailList,
			Map<String, Object> mainMap) {
		UserInfo userInfo = (UserInfo)mainMap.get("userInfo");
		int i = 0;
		int saleID = 0;
		String importBatch = ConvertUtil.getString(mainMap.get("importBatch"));
		String organizationId = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDSale"));
		String customerType = ConvertUtil.getString(mainMap.get("customerType"));
		String customerID = ConvertUtil.getString(mainMap.get("CustomerID"));
		String billType = ConvertUtil.getString(mainMap.get("saleBillType"));
		// 完结
		String billState = "99";
		String billCode = ConvertUtil.getString(mainMap.get("billNo"));
		String totalQuantity = ConvertUtil.getString(mainMap.get("totalQuantity"));
		String originalAmount = ConvertUtil.getString(mainMap.get("totalAmount"));
		/** 导入的数据没有折扣字段*/
		String discountRate = "";
		String discountAmount = "";
		String totalAmount = ConvertUtil.getString(mainMap.get("totalAmount"));
		String settlement = ConvertUtil.getString(mainMap.get("settlement"));
		String currency = ConvertUtil.getString(mainMap.get("currency"));
		String saleEmployee = ConvertUtil.getString(mainMap.get("BIN_EmployeeID"));
		String saleDate = ConvertUtil.getString(mainMap.get("saleDate"));
		/** 导入的数据没有销售时间字段*/
		String saleTime = "";
		String expectFinishDate = ConvertUtil.getString(mainMap.get("expectFinishDate"));
		String contactPerson = ConvertUtil.getString(mainMap.get("contactPerson"));
		String deliverAddress = ConvertUtil.getString(mainMap.get("deliverAddress"));
		// 客户部门原本的联系人
		String curPerson = ConvertUtil.getString(mainMap.get("curPerson"));
		// 客户部门原本的联系地址
		String curAddress = ConvertUtil.getString(mainMap.get("curAddress"));
		String comments = ConvertUtil.getString(mainMap.get("comments"));
		String customerDepot = ConvertUtil.getString(mainMap.get("BIN_InventoryInfoIDCustomer"));
		String customerLogicDepot = ConvertUtil.getString(mainMap.get("BIN_LogicInventoryInfoIDCustomer"));
		String saleDepot = ConvertUtil.getString(mainMap.get("BIN_InventoryInfoID"));
		String saleLogicDepot = ConvertUtil.getString(mainMap.get("BIN_LogicInventoryInfoID"));
		
		//定义主表数据Map
		Map<String,Object> mainData = new HashMap<String,Object>();
		//主表       组织ID
		mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
		//主表       品牌ID
		mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		/**主表       导入批次（只在导入时有此参数）*/
		mainData.put("ImportBatch", importBatch);
		//主表      销售部门
		mainData.put("BIN_OrganizationID", organizationId);
		//主表    客户类型
		mainData.put("CustomerType", customerType);
		//主表    客户唯一标识ID
		mainData.put("CustomerID", customerID);
		//主表    单据类型
		mainData.put("BillType", billType);
		//主表    单据状态
		mainData.put("BillState", billState);
		//主表    交易类型
		mainData.put("SaleType", "NS");
		//主表    单据号
		mainData.put("BillCode", billCode);
		//主表    销售总数量
		mainData.put("TotalQuantity", totalQuantity);
		//主表    整单折前金额
		mainData.put("OriginalAmount", originalAmount);
		//主表    整单折扣率
		mainData.put("DiscountRate", discountRate);
		//主表    整单折扣金额
		mainData.put("DiscountAmount", discountAmount);
		//主表    销售总金额
		mainData.put("TotalAmount", totalAmount);
		//主表    结算方式
		mainData.put("Settlement", settlement);
		//主表    货币
		mainData.put("Currency", currency);
		//主表    销售员工
		mainData.put("SaleEmployee", saleEmployee);
		//主表    销售日期
		mainData.put("SaleDate", saleDate);
		/**主表    销售时间(导入时不需要)*/
		mainData.put("SaleTime", saleTime);
		//主表    制单员工
		mainData.put("BillTicketEmployee", userInfo.getBIN_UserID());
		//主表    期望完成时间
		mainData.put("ExpectFinishDate", expectFinishDate);
		//主表    销售记录被修改次数
		mainData.put("ModifiedTimes", "0");
		//主表    数据来源
		mainData.put("DataSource", "Cherry");
		//主表    联系人
		mainData.put("ContactPerson", contactPerson);	
		//主表    送货地址
		mainData.put("DeliverAddress", deliverAddress);
		//主表    备注
		mainData.put("Comments", comments);
		if("1".equals(customerType)){
			//客户仓库
			mainData.put("BIN_InventoryInfoIDAccept", customerDepot);
			//客户逻辑仓库
			mainData.put("BIN_LogicInventoryInfoIDAccept", customerLogicDepot);
		}
		//销售部门仓库
		mainData.put("BIN_InventoryInfoID", saleDepot);
		//销售部门逻辑仓库
		mainData.put("BIN_LogicInventoryInfoID", saleLogicDepot);
		//主表    创建人
		mainData.put("CreatedBy", userInfo.getBIN_UserID());
		//主表    创建程序
		mainData.put("CreatePGM", "BINOLSTSFH19");
		//主表    修改人
		mainData.put("UpdatedBy", userInfo.getBIN_UserID());
		//主表   修改程序
		mainData.put("UpdatePGM", "BINOLSTSFH19");
		
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		if(null != backstageSaleDetailList && !backstageSaleDetailList.isEmpty()) {
			// 循环获取销售明细数据
			for(Map<String, Object> backstageSaleDetaiMap : backstageSaleDetailList) {
				i++;
				String productVendorID = ConvertUtil.getString(backstageSaleDetaiMap.get("productVendorId"));
				String unitCode = ConvertUtil.getString(backstageSaleDetaiMap.get("unitCode"));
				String barCode = ConvertUtil.getString(backstageSaleDetaiMap.get("barCode"));
				String detailQuantity = ConvertUtil.getString(backstageSaleDetaiMap.get("quantity"));
				String detailPrice = ConvertUtil.getString(backstageSaleDetaiMap.get("price"));
				String detailPricePay = ConvertUtil.getString(backstageSaleDetaiMap.get("price"));
				/** 无折扣字段 */
				String detailDiscountRate = "";
				String detailDiscountAmount = "";
				String detailPayAmount = ConvertUtil.getString(ConvertUtil.getInt(detailQuantity) * ConvertUtil.getFloat(detailPrice));
				/** 明细无说明字段 */
				String detailComment = "";
				
				Map<String, Object> saleMap = new HashMap<String, Object>();
				// 销售单ID
				saleMap.put("BIN_BackstageSaleID", saleID);
				// 产品厂商ID
				saleMap.put("BIN_ProductVendorID", productVendorID);
				// 厂商编码
				saleMap.put("UnitCode", unitCode);
				// 产品条码
				saleMap.put("BarCode", barCode);
				// 当前记录的序号
				saleMap.put("DetailNo", i);
				// 数量
				saleMap.put("Quantity", detailQuantity);
				// 零售单价
				saleMap.put("Price", detailPrice);
				// 销售单价
				saleMap.put("PricePay", detailPricePay);
				// 折扣率
				saleMap.put("DiscountRate", detailDiscountRate);
				// 折扣金额
				saleMap.put("DiscountAmount", detailDiscountAmount);
				// 折后金额
				saleMap.put("PayAmount", detailPayAmount);
				if("1".equals(customerType)){
					// 客户仓库
					saleMap.put("BIN_InventoryInfoIDAccept", customerDepot);
					// 客户逻辑仓库
					saleMap.put("BIN_LogicInventoryInfoIDAccept", customerLogicDepot);
				}
				// 销售部门仓库
				saleMap.put("BIN_InventoryInfoID", saleDepot);
				// 销售部门逻辑仓库
				saleMap.put("BIN_LogicInventoryInfoID", saleLogicDepot);
				// 备注
				saleMap.put("Comment", detailComment);
				// 创建人
				saleMap.put("CreatedBy", userInfo.getBIN_UserID());
				// 创建程序
				saleMap.put("CreatePGM", "BINOLSTSFH19");
				// 修改人
				saleMap.put("UpdatedBy", userInfo.getBIN_UserID());
				// 修改程序
				saleMap.put("UpdatePGM", "BINOLSTSFH19");
				
				detailList.add(saleMap);
			}
		}
		
		saleID = binOLSTCM19_BL.insertSaleData(mainData, detailList);
		
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationId", userInfo.getBIN_OrganizationInfoID());
		praMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		praMap.put("customerID", customerID);
		praMap.put("contactPerson", contactPerson);
		praMap.put("deliverAddress", deliverAddress);
		praMap.put("curPerson", curPerson);
		praMap.put("curAddress", curAddress);
		praMap.put("UpdatedBy", userInfo.getBIN_UserID());
		if("1".equals(customerType)){
			//更新组织部门联系人和联系地址
			binOLSTCM19_BL.updateOrganizationInfo(praMap);
		}else if("2".equals(customerType)){
			//更新往来单位部门联系人和联系地址
			binOLSTCM19_BL.updateBussinessPartnerInfo(praMap);
		}
		return saleID;
	}

	/**
	 * 对一笔单据数据进行验证补充成完整信息
	 * @param detailList
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> validExcel(
			List<Map<String, Object>> detailList, Map<String, Object> map) throws Exception {
		// 导入结果标记，默认成功
		boolean resultFlag = false;
		long totalQuantity = 0;
		float totalAmount = 0;
		// 验证结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 验证后明细数据
		List<Map<String, Object>> resultDetailList = new ArrayList<Map<String, Object>>();
		// 明细数
		int listSize = (detailList != null) ? detailList.size() : 0;
		// 主数据
		Map<String, Object> commonInfo = new HashMap<String, Object>();
		// 主数据错误信息
		Map<String, Object> commonErrorMap = new HashMap<String, Object>();
		
		/**========================校验主数据START===================================**/
		if(listSize > 0) {
			// 一单中的主数据只需一次校验
			Map<String, Object> mainMap = detailList.get(0);
			
			// 校验--销售部门名称【导入时此字段只作参考，在解析EXCEL文件时已经校验过】
			String departNameSale = ConvertUtil.getString(mainMap.get("excelDepartNameSale"));
			commonInfo.put("departNameSale", departNameSale);
			
			// 校验销售部门编码【格式的校验已经在解析EXCEL文件时校验过】
			String departCodeSale = ConvertUtil.getString(mainMap.get("excelDepartCodeSale"));
			commonInfo.put("departCodeSale", departCodeSale);
			
			// 定义一个校验部门CODE的MAP参数
			Map<String, Object> orgInfoParamMap = new HashMap<String, Object>();
			orgInfoParamMap.putAll(map);
			orgInfoParamMap.put("departCode", departCodeSale);
//			// 不包含权限
//			orgInfoParamMap.put("privilegeFlag", "0");
//			// 业务类型
//			orgInfoParamMap.put("businessType", "0");
//			// 操作类型
//			orgInfoParamMap.put("operationType", "1");
			// 根据部门CODE获取部门信息
			Map<String, Object> orgSaleMap = binOLSTSFH19_Service.getOrgInfoByCode(orgInfoParamMap);
			// 用完的参数字段去除以防影响后面对于参数的使用
			orgInfoParamMap.remove("departCode");
			if(null == orgSaleMap || orgSaleMap.isEmpty()) {
				resultFlag = true;
				// 指定部门不存在或者无权限操作
				commonErrorMap.put("departCodeSaleExist", true);
				commonErrorMap.put("departCodeSaleShow", departCodeSale);
			} else {
				// 校验正确，获取销售部门ID
				commonInfo.put("BIN_OrganizationIDSale", orgSaleMap.get("organizationId"));
				
				/** 只有当部门正确的情况下实体仓库才进行校验，否则校验无意义*/
				// 校验--销售部门实体仓库名称【为空时取默认实体仓库】
				String depotNameSale = ConvertUtil.getString(mainMap.get("excelDepotNameSale"));
				commonInfo.put("depotNameSale", depotNameSale);
				// 取得指定部门的仓库LIST
				List<Map<String, Object>> depotSaleList = binOLCM18_BL
						.getDepotsByDepartID(
								ConvertUtil.getString(orgSaleMap
										.get("organizationId")),
								ConvertUtil.getString(map
										.get(CherryConstants.SESSION_LANGUAGE)));
				
				if(null == depotSaleList || depotSaleList.size() == 0) {
					resultFlag = true;
					// 销售部门实体仓库信息不存在【数据库中不存在此类信息】
					commonErrorMap.put("depotInfoSaleExist", true);
				}
				// 销售部门的实体仓库
				Map<String, Object> inventoryInfo = null;
				if("".equals(depotNameSale)) {
					// 取默认实体仓库
					if(null != depotSaleList && depotSaleList.size() > 0) {
						inventoryInfo = depotSaleList.get(0);
						// 获取销售部门的实体仓库ID
						commonInfo.put("BIN_InventoryInfoID", inventoryInfo.get("BIN_DepotInfoID"));
					} 
				} else {
					// 校验此实体仓库是否存在
					if(null != depotSaleList && depotSaleList.size() > 0) {
						for(Map<String, Object> depotSaleMap : depotSaleList) {
							if(depotNameSale.equals(depotSaleMap.get("DepotName"))) {
								// 找到指定的实体仓库名称
								inventoryInfo = depotSaleMap;
								// 获取销售部门的实体仓库ID
								commonInfo.put("BIN_InventoryInfoID", inventoryInfo.get("BIN_DepotInfoID"));
								break;
							}
						}
						
						// 未找到指定实体仓库名称的实体仓库信息
						if(inventoryInfo == null) {
							// 导入的销售部门实体仓库名称不存在
							resultFlag = true;
							// {销售}实体仓库名称不存在
							commonErrorMap.put("depotNameSaleExist", true);
							commonErrorMap.put("depotNameSaleShow", depotNameSale);
						}
						
					} 
				}
			}
			
			// 调用共通获取逻辑仓库
	        Map<String,Object> pram =  new HashMap<String,Object>();
	        pram.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
	        pram.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_NS);
	        pram.put("ProductType", "1");
	        pram.put("language", map.get(CherryConstants.SESSION_LANGUAGE));
	        // 后台的逻辑仓库
	        pram.put("Type", "0");
	        /**获取逻辑仓库信息【校验客户逻辑仓库也可使用】*/
			List<Map<String, Object>> commonLogicDepotList = binOLCM18_BL.getLogicDepotByBusiness(pram);
			boolean isNullOrEmptyLogicDepot = (null != commonLogicDepotList && commonLogicDepotList.size() > 0) ? false : true;
			// 数据库中的逻辑仓库信息不存在
			if(isNullOrEmptyLogicDepot) {
				resultFlag = true;
				commonErrorMap.put("logicInventoryInfoExist", true);
			}
			// 销售逻辑仓库信息
			Map<String, Object> logicInventoryInfo = null;
			
			// 校验--销售部门逻辑仓库名称【为空时取默认逻辑仓库】
			String logicInventoryNameSale = ConvertUtil.getString(mainMap.get("excelLogicInventoryNameSale"));
			commonInfo.put("logicInventoryNameSale", logicInventoryNameSale);
			
			if("".equals(logicInventoryNameSale)) {
				// 逻辑仓库为空时取默认逻辑仓库
				if(!isNullOrEmptyLogicDepot) {
					logicInventoryInfo = commonLogicDepotList.get(0);
					// 获取销售部门的逻辑仓库ID
					commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
				} 
			} else {
				// 校验此逻辑仓库是否存在
				if(!isNullOrEmptyLogicDepot) {
					for(Map<String, Object> logicDepotSaleMap : commonLogicDepotList) {
						if(logicInventoryNameSale.equals(logicDepotSaleMap.get("LogicInventoryName"))) {
							// 找到指定的逻辑仓库名称
							logicInventoryInfo = logicDepotSaleMap;
							// 获取销售部门的逻辑仓库ID
							commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
							break;
						}
					}
					
					// 未找到指定逻辑仓库名称的逻辑仓库信息
					if(logicInventoryInfo == null) {
						// 导入的销售部门实体仓库名称不存在
						resultFlag = true;
						// 销售逻辑仓库名称不存在
						commonErrorMap.put("logicInventoryNameSaleExist", true);
						commonErrorMap.put("logicInventoryNameSaleShow", logicInventoryNameSale);
					}
				}
			}
			
			// 校验--销售日期【解析EXCEL时已经校验过】
			String saleDate = ConvertUtil.getString(mainMap.get("excelSaleDate"));
			commonInfo.put("saleDate", saleDate);
			
			// 校验--送货日期【基本格式已经校验过，这里校验其必须大于等于销售日期】
			String expectFinishDate = ConvertUtil.getString(mainMap.get("excelExpectFinishDate"));
			commonInfo.put("expectFinishDate", expectFinishDate);
			if(saleDate.compareTo(expectFinishDate) > 0) {
				// 送货日期不能小于销售日期
				resultFlag = true;
				commonErrorMap.put("expectFinishDateError", true);
				commonErrorMap.put("expectFinishDateShow", expectFinishDate);
			}
			
			// 校验--订单类型【已经在解析EXCEL时校验过,这里值为对应的key值】
			String saleBillType = ConvertUtil.getString(mainMap.get("excelSaleBillType"));
			commonInfo.put("saleBillType", saleBillType);
			
			// 校验--客户类型【已经在解析EXCEL时校验过,这里值为对应的key值】
			String customerType = ConvertUtil.getString(mainMap.get("excelCustomerType"));
			commonInfo.put("customerType", customerType);
			
			// 校验--客户名称【导入时此字段只作参考，在解析EXCEL文件时已经校验过】
			String departNameCustomer = ConvertUtil.getString(mainMap.get("excelDepartNameCustomer"));
			commonInfo.put("departNameCustomer", departNameCustomer);
			
			// 校验--客户编码
			String departCodeCustomer = ConvertUtil.getString(mainMap.get("excelDepartCodeCustomer"));
			commonInfo.put("departCodeCustomer", departCodeCustomer);
			
			// 校验--客户部门实体仓库名称【为空时取默认实体仓库】--只有客户编码无误时才进行真下的校验
			String depotNameCustomer = ConvertUtil.getString(mainMap.get("excelDepotNameCustomer"));
			commonInfo.put("depotNameCustomer", depotNameCustomer);
			
			orgInfoParamMap.put("departCode", departCodeCustomer);
			// 客户类型为往来单位的标记[SQL文中有用]
			if("2".equals(customerType)) {
				orgInfoParamMap.put("outOrgFlag", true);
			}
			// 根据部门CODE获取部门信息
			Map<String, Object> orgCustomerMap = binOLSTSFH19_Service.getOrgInfoByCode(orgInfoParamMap);
			if(null == orgCustomerMap || orgCustomerMap.isEmpty()) {
				resultFlag = true;
				// 客户部门不存在或者无权限操作
				commonErrorMap.put("departCodeCustomerExits", true);
				commonErrorMap.put("departCodeCustomerShow", departCodeCustomer);
			} else {
				// 校验正确，获取销售部门ID
				commonInfo.put("CustomerID", orgCustomerMap.get("organizationId"));
				
				/** 只有当部门正确且客户类型为"1"(内部销售)的情况下实体仓库才进行校验，否则校验无意义*/
				if("1".equals(customerType)) {
					
					// 取得指定部门的仓库LIST
					List<Map<String, Object>> depotCustomerList = binOLCM18_BL
							.getDepotsByDepartID(
									ConvertUtil.getString(orgCustomerMap
											.get("organizationId")),
									ConvertUtil.getString(map
											.get(CherryConstants.SESSION_LANGUAGE)));
					if(null == depotCustomerList || depotCustomerList.size() == 0) {
						resultFlag = true;
						// {客户部门}的实体仓库信息不存在【数据库中不存在此类信息】
						commonErrorMap.put("depotInfoCustomerExist", true);
					}
					
					Map<String, Object> inventoryCustomerInfo = null;
					if("".equals(depotNameCustomer)) {
						// 取默认实体仓库
						if(null != depotCustomerList && depotCustomerList.size() > 0) {
							inventoryCustomerInfo = depotCustomerList.get(0);
							// 获取客户部门的实体仓库ID
							commonInfo.put("BIN_InventoryInfoIDCustomer", inventoryCustomerInfo.get("BIN_DepotInfoID"));
							
						} 
					} else {
						// 校验此实体仓库是否存在
						if(null != depotCustomerList && depotCustomerList.size() > 0) {
							
							for(Map<String, Object> depotSaleMap : depotCustomerList) {
								if(depotNameCustomer.equals(depotSaleMap.get("DepotName"))) {
									// 找到指定的实体仓库名称
									inventoryCustomerInfo = depotSaleMap;
									// 获取客户部门的实体仓库ID
									commonInfo.put("BIN_InventoryInfoIDCustomer", inventoryCustomerInfo.get("BIN_DepotInfoID"));
									break;
								}
							}
							
							// 未找到指定实体仓库名称的实体仓库信息
							if(inventoryCustomerInfo == null) {
								// 导入的客户部门实体仓库名称不存在
								resultFlag = true;
								// {客户部门}实体仓库名称不存在
								commonErrorMap.put("depotNameCustomerExist", true);
								commonErrorMap.put("depotNameCustomerShow", depotNameCustomer);
							}
						} 
					}
				}
			}
			
			// 校验--客户逻辑仓库名称【基本格式已经在解析EXCEL文件时校验过】
			String logicInventoryNameCustomer = ConvertUtil.getString(mainMap.get("excelLogicInventoryNameCustomer"));
			commonInfo.put("logicInventoryNameCustomer", logicInventoryNameCustomer);
			
			/** 只有当客户类型为"1"(内部销售)的情况下逻辑仓库才进行校验，否则校验无意义*/
			if("1".equals(customerType)) {
				if("".equals(logicInventoryNameCustomer)) {
					// 客户逻辑仓库为空时取默认逻辑仓库
					if(!isNullOrEmptyLogicDepot) {
						logicInventoryInfo = commonLogicDepotList.get(0);
						// 获取客户部门的逻辑仓库ID
						commonInfo.put("BIN_LogicInventoryInfoIDCustomer", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
					}
				} else {
					// 校验此逻辑仓库是否存在
					if(!isNullOrEmptyLogicDepot) {
						
						for(Map<String, Object> logicDepotCustomerMap : commonLogicDepotList) {
							if(logicInventoryNameCustomer.equals(logicDepotCustomerMap.get("LogicInventoryName"))) {
								// 找到指定的逻辑仓库名称
								logicInventoryInfo = logicDepotCustomerMap;
								// 获取客户部门的逻辑仓库ID
								commonInfo.put("BIN_LogicInventoryInfoIDCustomer", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
								break;
							}
						}
						
						// 未找到指定逻辑仓库名称的逻辑仓库信息
						if(logicInventoryInfo == null) {
							// 导入的客户部门实体仓库名称不存在
							resultFlag = true;
							// {客户}逻辑仓库名称不存在
							commonErrorMap.put("logicInventoryNameCustomerExist", true);
							commonErrorMap.put("logicInventoryNameCustomerShow", logicInventoryNameCustomer);
						}
					} 
				}
			}
			
			// 校验--联系人
			String contactPerson = ConvertUtil.getString(mainMap.get("excelContactPerson"));
			commonInfo.put("contactPerson", contactPerson);
			
			// 校验--联系地址
			String deliverAddress = ConvertUtil.getString(mainMap.get("excelDeliverAddress"));
			commonInfo.put("deliverAddress", deliverAddress);
			
			/** ==============根据客户部门ID取得其原本的联系人与联系地址START========================*/
			boolean getEmployeeFlag = false;
			String defEmployeeName = "";
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("organizationId", commonInfo.get("CustomerID"));
			List<Map<String, Object>> contactList = binOLBSDEP02_BL.getDepartContactList(paramMap);
			for(Map<String,Object> contactMap : contactList){
				if(!"".equals(ConvertUtil.getString(contactMap.get("employeeName")))){
					defEmployeeName = ConvertUtil.getString(contactMap.get("employeeName"));
					if("1".equals(ConvertUtil.getString(contactMap.get("defaultFlag")))){
						commonInfo.put("curPerson", ConvertUtil.getString(contactMap.get("employeeName")));
						getEmployeeFlag = true;
					}
				}
			}
			// 没有默认显示联系人的情况下给出默认联系人
			if(getEmployeeFlag == false){
				commonInfo.put("curPerson", defEmployeeName);
			}
			// 获取部门送货地址
			List<Map<String, Object>> addressList = binOLBSDEP02_BL.getDepartAddressList(paramMap);
			for(Map<String,Object> addressMap : addressList){
				// 判断地址类型是否为送货地址，若是则获取地址信息
				if("1".equals(ConvertUtil.getString(addressMap.get("addressTypeId")))){
					commonInfo.put("curAddress", ConvertUtil.getString(addressMap.get("addressLine1")));
				}
			}
			/** ==============根据客户部门ID取得其原本的联系人与联系地址END========================*/
			
			// 校验--结算方式
			String settlement = ConvertUtil.getString(mainMap.get("excelSettlement"));
			commonInfo.put("settlement", settlement);
			
			// 校验--币种
			String currency = ConvertUtil.getString(mainMap.get("excelCurrency"));
			commonInfo.put("currency", currency);
		}
		/**=========================校验主数据END==============================**/
		
		/**=====================校验明细数据START===========================**/
		int detailNo=1;
		// 定义一个中间参数MAP
		Map<String, Object> detailPrm = new HashMap<String, Object>();
		for(Map<String, Object> detailMap : detailList) {
			// 定义一个map存放【主数据+明细】错误信息写入明细
			Map<String, Object> errorDetailMap = new HashMap<String, Object>();
			// 主错误信息
			errorDetailMap.putAll(commonErrorMap);
			// 插入数据时使用的共通参数
			detailMap.putAll(map);
			// 主数据信息
			detailMap.putAll(commonInfo);
			
			// 产品明细连番
			detailMap.put("detailNo", detailNo++ );
			
			// 校验--产品名称【此数据只作参考，格式方面已经在解析EXCEL时校验过】
			String productName = ConvertUtil.getString(detailMap.get("excelProductName"));
			detailMap.put("importProductName", productName);
			
			// 校验--产品厂商编码、产品条码
			String unitCode = ConvertUtil.getString(detailMap.get("excelUnitCode"));
			String barCode = ConvertUtil.getString(detailMap.get("excelBarCode"));
			
			// 1、产品条码格式已经校验过【此字段在明细表与excel明细表中都存在】
			detailMap.put("importBarCode", barCode);
			// 2、厂商编码除格式外还须校验是否在数据库存在
			detailMap.put("unitCode", unitCode);
			
			// 根据厂商编码查询此编码的产品信息
			detailPrm.clear();
			detailPrm.putAll(map);
			detailPrm.put("unitCode", unitCode);
			List<Map<String, Object>> prtInfoList = binOLSTSFH19_Service.getPrtInfo(detailPrm);
			
			if (prtInfoList == null || prtInfoList.size() == 0) {
				resultFlag = true;
				// 厂商编码不存在
				errorDetailMap.put("unitCodeExits", true);
			} else {
				detailMap.put("productId", prtInfoList.get(0).get("productId"));
//				detailMap.put("productName", prtInfoList.get(0).get("productName"));
				detailMap.put("productVendorId", prtInfoList.get(0).get("productVendorId"));
				// 校验产品barCode【一品多码时才校验，基本格式的校验已经在解析EXCEL时处理】
				if(prtInfoList.size() > 1){
					//一品多码
					if(CherryChecker.isNullOrEmpty(barCode)){
						resultFlag = true;
						// 此产品存在多个条码，请选择相应的条码！
						errorDetailMap.put("barCodeNull", true);
					}else{
						int index = indexOfBarCode(prtInfoList,barCode);
						if(index == -1){
							resultFlag = true;
							// 产品条码错误：与此产品的条码不相符！
							errorDetailMap.put("barCodeExits", true);
						}else{
							detailMap.put("barCode", prtInfoList.get(index).get("barCode"));
							detailMap.put("productVendorId", prtInfoList.get(index).get("productVendorId"));
						}
					}
				} else {
					if(CherryChecker.isNullOrEmpty(barCode) || barCode.equals(prtInfoList.get(0).get("barCode"))){
						detailMap.putAll(prtInfoList.get(0));
					}else{
						resultFlag = true;
						errorDetailMap.put("barCodeExits", true);
					}
				}
			}
			
			// 校验--销售数量【基本格式已经在解析EXCEL文件时校验过】
			String quantity = ConvertUtil.getString(detailMap.get("excelQuantity"));
			detailMap.put("quantity", quantity);
			
			// 产品价格从数据库中取得
			detailPrm.clear();
			detailPrm.putAll(map);
			detailPrm.put("BIN_ProductID", detailMap.get("productId"));
			String sysDate = ConvertUtil.getString(map.get("importDate"));
			detailMap.put("price",getPrice(binOLSTSFH19_Service.getPrtPrice(detailPrm),sysDate.substring(0, 10)));
			// 产品总数量和总金额
			totalQuantity += ConvertUtil.getInt(quantity);
			totalAmount += ConvertUtil.getInt(quantity) * ConvertUtil.getFloat(detailMap.get("price"));
			// 错误信息
			String errorMsg = this.getErrorMsg(errorDetailMap);
			if(errorMsg != null && errorMsg.length() > 200){
				errorMsg = errorMsg.subSequence(0, 195)+"...";
			}
			detailMap.put("ErrorMsg", errorMsg);
			resultDetailList.add(detailMap);
		}
		
		/**=====================校验明细数据END===========================**/
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
	 * 将错误信息转换为字符串
	 * 
	 * @param errorMap
	 * @return
	 */
	private String getErrorMsg(Map<String, Object> errorMap) {
		StringBuffer errorMsg = new StringBuffer();
		if (errorMap.get("departCodeSaleExist") != null) {
			// {销售部门编码}不存在或者无权限操作
			errorMsg.append(PropertiesUtil.getText("BSL00007",new String[]{PropertiesUtil.getText("PBL00001")}));
		}
		if (errorMap.get("depotInfoSaleExist") != null) {
			// {销售部门}不存在实体仓库【数据库中不存在此类信息】
			errorMsg.append(PropertiesUtil.getText("BSL00008",new String[]{PropertiesUtil.getText("PBL00003")}));
		}
		if (errorMap.get("depotNameSaleExist") != null) {
			// {销售}实体仓库名称不存在
			errorMsg.append(PropertiesUtil.getText("BSL00009",new String[]{PropertiesUtil.getText("PBL00005")}));
		}
		if (errorMap.get("logicInventoryInfoExist") != null) {
			// 逻辑仓库信息不存在【数据库中不存在此类信息】
			errorMsg.append(PropertiesUtil.getText("BSL00010"));
		}
		if (errorMap.get("logicInventoryNameSaleExist") != null) {
			// {销售}逻辑仓库名称不存在
			errorMsg.append(PropertiesUtil.getText("BSL00011",new String[]{PropertiesUtil.getText("PBL00005")}));
		}
		if (errorMap.get("expectFinishDateError") != null) {
			// 送货日期不能小于销售日期！
			errorMsg.append(PropertiesUtil.getText("BSL00012"));
		}
		if (errorMap.get("departCodeCustomerExits") != null) {
			// {客户部门编码}不存在或者无权限操作
			errorMsg.append(PropertiesUtil.getText("BSL00007",new String[]{PropertiesUtil.getText("PBL00002")}));
		}
		if (errorMap.get("depotInfoCustomerExist") != null) {
			// {客户部门}不存在实体仓库【数据库中不存在此类信息】
			errorMsg.append(PropertiesUtil.getText("BSL00008",new String[]{PropertiesUtil.getText("PBL00004")}));
		}
		if (errorMap.get("depotNameCustomerExist") != null) {
			// {客户}实体仓库名称不存在
			errorMsg.append(PropertiesUtil.getText("BSL00009",new String[]{PropertiesUtil.getText("PBL00006")}));
		}
		if (errorMap.get("logicInventoryNameCustomerExist") != null) {
			// {客户}逻辑仓库名称不存在！
			errorMsg.append(PropertiesUtil.getText("BSL00011",new String[]{PropertiesUtil.getText("PBL00006")}));
		}
		if (errorMap.get("unitCodeExits") != null) {
			// 厂商编码不存在！
			errorMsg.append(PropertiesUtil.getText("BSL00013"));
		}
		if (errorMap.get("barCodeNull") != null) {
			// 此产品存在多个条码，请选择相应的条码！
			errorMsg.append(PropertiesUtil.getText("STM00017"));
		}
		if (errorMap.get("barCodeExits") != null) {
			// 产品条码错误：与此产品的条码不相符！
			errorMsg.append(PropertiesUtil.getText("STM00016"));
		}
		
		String errorMsgStr = ConvertUtil.getString(errorMsg);
		if(CherryChecker.isNullOrEmpty(errorMsgStr)){
			errorMsg.append(PropertiesUtil.getText("STM00020"));
		}
		return errorMsg.toString();
	}

	/**
	 * 取得发货日期时的销售价格
	 * 
	 * @param priceList
	 *            按照开始时间排序的产品价格信息
	 * @param inDepotDate
	 *            发货时间
	 * @return
	 */
	public Object getPrice(List<Map<String, Object>> priceList, Object importDate) {
		int length = 0;
		if(!CherryChecker.isNullOrEmpty(priceList)){
			length = priceList.size();
		}else{
			return 0;
		}
		if(length == 0){
			return 0;
		} else if (length == 1) {
			return priceList.get(0).get("SalePrice");
		} else {
			for (int i = 0; i < length; i++) {
				Map<String, Object> map = priceList.get(i);
				String importDateStr = ConvertUtil.getString(importDate);
				// 当发货日期小于所有价格的日期
				if (i == 0
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("StartDate"))) <= 0) {
					return map.get("SalePrice");
				}
				if (CherryChecker.compareDate(importDateStr,
						ConvertUtil.getString(map.get("StartDate"))) >= 0
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("EndDate"))) <= 0) {
					return map.get("SalePrice");
				}
				// 当发货日期大于所有的价格日期
				if (i == (length - 1)
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("EndDate"))) >= 0) {
					return map.get("SalePrice");
				}
			}
		}
		return 0;

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
			if(barCode.equals(map.get("barCode"))){
				return index;
			}
		}
		return -1;
	}

	@Override
	public int insertImportBatch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("importBatchCodeIF",map.get("importBatchCode"));
		return binOLSTSFH19_Service.insertImportBatch(paramMap);
	}

}
