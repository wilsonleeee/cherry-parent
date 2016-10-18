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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.service.BINBEMQMES02_Service;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.bl.BINOLSTCM03_BL;
import com.cherry.st.common.service.BINOLSTCM10_Service;
import com.cherry.st.sfh.interfaces.BINOLSTSFH12_IF;
import com.cherry.st.sfh.service.BINOLSTSFH12_Service;
import com.cherry.cm.activemq.bl.BINOLMQCOM02_BL;

public class BINOLSTSFH12_BL implements BINOLSTSFH12_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH12_BL.class);
	
	@Resource(name="binOLSTSFH12_Service")
	private BINOLSTSFH12_Service binOLSTSFH12_Service;
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_BL binOLCM18_BL;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_BL binOLSTCM03_BL;
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_BL binOLSTCM00_BL;
	@Resource(name="CodeTable")
	private CodeTable CodeTable;
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	@Resource(name="binOLSTCM10_Service")
	private BINOLSTCM10_Service binOLSTCM10_Service;
	@Resource(name="binOLSSCM05_BL")
	private BINOLSSCM05_BL binOLSSCM05_BL;
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	/** MQ发送 */
	@Resource(name="binOLMQCOM02_BL")
	private BINOLMQCOM02_BL binOLMQCOM02_BL;
	
	@Resource(name="binBEMQMES02_Service")
	private BINBEMQMES02_Service binBEMQMES02_Service;
	
	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.5";

	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		return binOLSTSFH12_Service.getImportBatchCount(map);
	}
	
	/**
	 * 查询订货单据（根据订单号）
	 * @param map
	 * @return
	 */
	private int getPrtOrderList(Map<String,Object> map){
        int resultInt = 0;
		Map<String,Object> orderParam = new HashMap<String,Object>();
        orderParam.put("OrderNoIF", map.get("relevanceNo"));
        List<Map<String,Object>> orderList = binBEMQMES02_Service.selPrtOrderList(orderParam);
        if(!CherryUtil.isBlankList(orderList)){
        	return orderList.size();
        }
        return resultInt;
        
	}

	@Override
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		return binOLSTSFH12_Service.getImportBatchList(map);
	}

	@SuppressWarnings("unchecked")
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
		// 发货数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.DELIVER_SHEET_NAME.equals(st.getName().trim())) {
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
		// 发货数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.DELIVER_SHEET_NAME });
		}
		
		// 发货数据sheet的行数
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
			// 此处不进行重复校验（importBatchCode参数不是由EXCEL提供）
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
			// 发货部门编码
			String excelDepartCodeFrom = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("excelDepartCodeFrom", excelDepartCodeFrom);
			// 发货部门名称
			String excelDepartNameFrom = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("excelDepartNameFrom", excelDepartNameFrom);
			// 收货部门编码
			String excelDepartCodeTo = dateSheet.getCell(5, r).getContents().trim();
			rowMap.put("excelDepartCodeTo", excelDepartCodeTo);
			// 收货部门名称
			String excelDepartNameTo = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("excelDepartNameTo", excelDepartNameTo);
			// 发货类型
			String excelDeliverType = dateSheet.getCell(7, r).getContents().trim();
			rowMap.put("excelDeliverType", excelDeliverType);
	         //执行价
            String excelPrice = dateSheet.getCell(8, r).getContents().trim();
            rowMap.put("excelPrice", excelPrice);
			// 产品数量
			String excelQuantity = dateSheet.getCell(9, r).getContents().trim();
			rowMap.put("excelQuantity", excelQuantity);
			// 逻辑仓库名称
			String excelLogicInventoryName = dateSheet.getCell(10, r).getContents().trim();
			rowMap.put("excelLogicInventoryName", excelLogicInventoryName);
			// 预计到货日期
            String excelPlanArriveDate = dateSheet.getCell(11, r).getContents().trim();
            rowMap.put("excelPlanArriveDate", excelPlanArriveDate);
            // 关联单号(订货单号)
            String relevanceNo = dateSheet.getCell(12, r).getContents().trim();
            rowMap.put("relevanceNo", relevanceNo);
            //金蝶码
            String erpCode = dateSheet.getCell(13, r).getContents().trim();
			//配置项【是否开启金蝶码】0：关闭，1：开启
			String configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(map.get("BIN_OrganizationInfoID")),ConvertUtil.getString(map.get("BIN_BrandInfoID")));
			if("1".equals(configVal)){
				 rowMap.put("erpCode", erpCode);
			}
			
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(excelUnitCode)
					&& CherryChecker.isNullOrEmpty(excelBarCode)
					&& CherryChecker.isNullOrEmpty(excelProductName)
					&& CherryChecker.isNullOrEmpty(excelDepartCodeFrom)
					&& CherryChecker.isNullOrEmpty(excelDepartNameFrom)
					&& CherryChecker.isNullOrEmpty(excelDepartCodeTo)
					&& CherryChecker.isNullOrEmpty(excelDepartNameTo)
					&& CherryChecker.isNullOrEmpty(excelDeliverType)
					&& CherryChecker.isNullOrEmpty(excelPrice)
					&& CherryChecker.isNullOrEmpty(excelQuantity)
					&& CherryChecker.isNullOrEmpty(excelLogicInventoryName)
					&& CherryChecker.isNullOrEmpty(excelPlanArriveDate)
					&& CherryChecker.isNullOrEmpty(relevanceNo)
					&& CherryChecker.isNullOrEmpty(erpCode)){
				break;

			}
			// 数据基本格式校验
			this.checkData(rowMap);
			// 是否导入重复数据参数
			String importRepeat = ConvertUtil.getString(map.get("importRepeat"));
			if(!CherryChecker.isNullOrEmpty(importRepeat, true) && "0".equals(importRepeat)){
				// 不导入重复数据时出现重复数据则报错--加入关联订单号后在判断是否为重复数据要加上此字段进行判断
				if(isRepeatData(rowMap,map)){
					throw new CherryException("STM00028",new String[] { ConvertUtil.getString(r+1) });
				}
			}
			// 根据发货部门code、收货部门CODE、发货类型、逻辑仓库、到货日期、关联订货单号拆分为单据
			String importDatasKey = excelDepartCodeFrom + excelDepartCodeTo + excelDeliverType + excelLogicInventoryName+excelPlanArriveDate;
			if(!ConvertUtil.isBlank(relevanceNo)) {
				// 有关联单号时合并单据时只看关联订货单号
				// 注：若出现同一关联订货单多个收货部门，将会只取第一条数据的收货部门
				importDatasKey = relevanceNo;
			}
			String priceValue = excelPrice;
			if (importDatas.containsKey(importDatasKey)) {
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) importDatas
						.get(importDatasKey);
				/**
				 * 在相同主单下的明细中不允许出现相同产品不同价格的数据，也不允许出现
				 */
				for(Map<String, Object> detailMap : detailList) {
					if(detailMap.get("excelUnitCode").equals(excelUnitCode) && detailMap.get("excelBarCode").equals(excelBarCode)){
						if(!ConvertUtil.getString(detailMap.get("excelPrice")).equals(priceValue)) {
							// 一条主单据中的相同产品的价格必须一致，发货部门号：【{0}】，入库部门号：【{1}】，发货类型：【{2}】，逻辑仓库：【{3}】，预计到货日期：【{4}】，关联订货单号：【{5}】厂商编码：【{6}】，产品条码：【{7}】
							throw new CherryException("STM00056",new String[]{excelDepartCodeFrom,excelDepartCodeTo,excelDeliverType,excelLogicInventoryName,excelPlanArriveDate,relevanceNo,excelUnitCode,excelBarCode});
						}
					}
					// 有关联订货单时，还需要校验收货部门必须是同一个
					if(!ConvertUtil.isBlank(relevanceNo) && !excelDepartCodeTo.equals(ConvertUtil.getString(detailMap.get("excelDepartCodeTo")))) {
						// 同一关联订货单号不允许对应多个收货部门，关联订货单号【{0}】
						throw new CherryException("STM00058",new String[]{relevanceNo});
					}
				}
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			} else {
				List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
				detailList.add(rowMap);
				importDatas.put(importDatasKey, detailList);
			}
		}
		if(r==3){
			throw new CherryException("EBS00035",new String[] { CherryConstants.DELIVER_SHEET_NAME });
		}
		return importDatas;
	}
	
	/**
	 * 将发货单/退库单的主数据和明细数据组装到一个MAP中，供调用MQHelper模块时使用
	 * @param mainMap 主数据行
	 * @param detailList 明细数据行
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> assemblingData(Map<String,Object> mainMap ,List<Map<String,Object>> detailList) throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//MQ版本号
		resultMap.put("Version", MessageConstants.MESSAGE_VERSION);
		
		//MQ消息体数据类型
		resultMap.put("Type", MessageConstants.MESSAGE_TYPE_SALE_STOCK);
		
		//消息主体数据key
		resultMap.put("MainLineKey", new String[]{"BrandCode","TradeNoIF","ModifyCounts",
				"CounterCode","RelevantCounterCode","TotalQuantity","TotalAmount","TradeType","SubType",
				"RelevantNo","Reason","TradeDate","TradeTime","TotalAmountBefore","TotalAmountAfter","MemberCode"});
		
		//消息主体数据
		String[] mainData = new String[((String[])resultMap.get("MainLineKey")).length];
		
		//品牌代码
		mainData[0] = ConvertUtil.getString(mainMap.get("BrandCode"));
		//单据号
		mainData[1] = ConvertUtil.getString(mainMap.get("DeliverNo"));
		//更改次数
		mainData[2] = null;
//		String invType = ConvertUtil.getString(mainMap.get("InvType"));
//		String toShop = ConvertUtil.getString(mainMap.get("ToShop"));
		
		//接受订货柜台（收货柜台）
		mainData[3] = ConvertUtil.getString(mainMap.get("excelDepartCodeTo"));
		//关联柜台号
		mainData[4] = ConvertUtil.getString(mainMap.get("excelDepartCodeFrom"));
		//总数量
		mainData[5] = ConvertUtil.getString(mainMap.get("TotalQuantity"));
		//总金额
		mainData[6] = ConvertUtil.getString(mainMap.get("TotalAmount"));
		//单据类型
		mainData[7] = "KS";
		//子类型 SD:发货单；RJ:退库单； 
		mainData[8]= "SD";
		
		//关联单号(订货单号)
		mainData[9] = ConvertUtil.getString(mainMap.get("relevanceNo"));
		//发货理由
		mainData[10] = "发货单Excel导入，走订发货流程";
		
		String BAcode = ConvertUtil.getString(mainMap.get("BAcode"));
		String sysDate = ConvertUtil.getString(mainMap.get("sysDateTime"));
		String TradeDate = DateUtil.getSpecificDate(sysDate, DateUtil.DATE_PATTERN);
		String TradeTime = DateUtil.getSpecificDate(sysDate, DateUtil.TIME_PATTERN);
		
		//日期
		mainData[11] = TradeDate;
		//时间
		mainData[12] = TradeTime;
		
		// 发货前柜台库存总金额
		mainData[13] = null;
		// 发货后柜台库存总金额
		mainData[14] = null;
		// 会员号
		mainData[15] = null;
		
		resultMap.put("MainDataLine", mainData);
		
		//明细数据Key
		resultMap.put("DetailLineKey", new String[]{"TradeNoIF","ModifyCounts","BAcode","StockType",
				"Barcode","Unitcode","InventoryTypeCode","Quantity","QuantityBefore","Price","Reason"});
		
		//明细数据
		List<String[]> detailData = new ArrayList<String[]>();
		for(int i = detailList.size()-1 ; i >= 0 ; i--){
			Map<String,Object> temMap = detailList.get(i);
			String[] temp = new String[((String[])resultMap.get("DetailLineKey")).length];
			
			// 单据号
			temp[0] = ConvertUtil.getString(mainMap.get("DeliverNo"));
			// 修改次数
			temp[1] = null;
			// 员工code--操作者
			temp[2] = BAcode;
			// 入出库区分
			temp[3] = null;
			// 产品条码
			temp[4] = ConvertUtil.getString(temMap.get("excelBarCode"));
			// 厂商编码
			temp[5] = ConvertUtil.getString(temMap.get("excelUnitCode"));
			// 仓库类型
			temp[6] = null;
			// 单品数量
			if("".equals(ConvertUtil.getString(temMap.get("Quantity")))){
				temp[7] = "0";
			}else{
				temp[7] = ConvertUtil.getString(temMap.get("Quantity"));
			}
			// 入出库前该商品柜台账面数量（针对盘点）
			temp[8] = null;
			//单品价格
			if("".equals(ConvertUtil.getString(temMap.get("Price")))){
				temp[9] = "0";
			}else{
				temp[9] = ConvertUtil.getString(temMap.get("Price"));
			}
			// 理由
			temp[10] = null;
			
			detailData.add(temp);
		}
		
		resultMap.put("DetailDataLine", detailData);
		
		//设定MQ_Log日志需要的数据
		Map<String, Object> mqLog = new HashMap<String,Object>();
		mqLog.put("BrandCode", mainData[0]);
		mqLog.put("BillType", "KS");
		mqLog.put("BillCode", mainData[1]);
		mqLog.put("CounterCode", mainData[3]);
		String date = mainData[11].replaceAll("-","");
		mqLog.put("Txddate", date);
		String time = mainData[12].replaceAll(":","");
		mqLog.put("Txdtime", time);
		mqLog.put("Source", "K3");
		mqLog.put("SendOrRece", "S");
		mqLog.put("ModifyCounts", 0);
		
		resultMap.put("Mq_Log", mqLog);
		
		return resultMap;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> sessionMap)
			throws Exception {
		// 导入日期
		String sysDate = binOLSTSFH12_Service.getSYSDate();
		sessionMap.put("ImportDate", sysDate);
		String sysDateTime = binOLSTSFH12_Service.getSYSDateTime();
		
		// 插入导入批次信息返回导入批次ID
		int importBatchId = this.insertImportBatch(sessionMap);
		
		// 导入结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功的数量
		int successCount = 0;
		// 导入失败的数量
		int errorCount = 0;
		// 导入失败的单据
		List<Map<String, Object>> errorPrtDeliverList = new ArrayList<Map<String, Object>>();
		Iterator it = importDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry en = (Entry) it.next();
			// 明细数据
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) en.getValue();
			// 验证数据的有效性
			Map<String, Object> detailMap = this.validExcel(detailList, sessionMap);
			detailList = (List<Map<String, Object>>) detailMap.get("resultDetailList");
			// 主数据
			Map<String, Object> mainMap = new HashMap<String, Object>();
			if (detailList != null && detailList.size() > 0) {
				mainMap.putAll(detailList.get(0));
				String billNo = getTicketNumber(sessionMap);
				mainMap.put("DeliverNo", billNo);
				mainMap.put("DeliverNoIF", billNo);
//				// 发货类型
//				mainMap.put("DeliverType", "1");
				mainMap.put("TotalQuantity", detailMap.get("TotalQuantity"));
				mainMap.put("TotalAmount", detailMap.get("TotalAmount"));
				mainMap.put("PreTotalQuantity", detailMap.get("TotalQuantity"));
				mainMap.put("PreTotalAmount", detailMap.get("TotalAmount"));
				mainMap.put("ImportDate", sysDate);
				//mainMap.put("PlanArriveDate", detailMap.get("PlanArriveDate"));
				mainMap.put("BIN_ImportBatchID", importBatchId);
				mainMap.put("ImportBatch", sessionMap.get("ImportBatchCode"));
			} else {
				continue;
			}
			
			if ((Boolean) detailMap.get("ResultFlag")) {
				// 验证不通过，只将数据添加到发货（Excel导入）主表和明细表
				mainMap.put("ImportResult", "0");
				int BIN_ProductDeliverExcelID = binOLSTSFH12_Service
						.insertPrtDeliverExcel(mainMap);
				String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductDeliverExcelID", BIN_ProductDeliverExcelID);
					map.put("ErrorMsg", totalErrorMsg+map.get("ErrorMsg"));
					binOLSTSFH12_Service.insertPrtDeliverDetailExcel(map);
					errorPrtDeliverList.add(map);
				}
				errorCount++;
			} else {
				// 验证成功，合并相同产品明细
				Map<String,Object> detailListGroup = new HashMap<String, Object>();
				for(Map<String, Object> fm : detailList){
					Map<String, Object> detailGroupMap = new HashMap<String, Object>();
					detailGroupMap.putAll(fm);
					String unitCode = ConvertUtil.getString(detailGroupMap.get("UnitCode")).trim();
					String barCode = ConvertUtil.getString(detailGroupMap.get("BarCode")).trim();
					String price = ConvertUtil.getString(detailGroupMap.get("Price")).trim();
					/****相同产品不同价格的明细不进行合并***/
					//以UnitCode+BarCode+Price作为合并依据
					String mapKey = unitCode + barCode + price;
					if(detailListGroup.containsKey(mapKey)){
						Map<String, Object> productMap = (Map<String, Object>) detailListGroup.get(mapKey);
						int quantity = ConvertUtil.getInt(detailGroupMap.get("Quantity"))+ConvertUtil.getInt(productMap.get("Quantity"));
						productMap.put("Quantity", quantity);
						detailListGroup.put(mapKey, productMap);
					}else{
						detailListGroup.put(mapKey, detailGroupMap);
					}
				}
				// 合并后明细转换为发货单明细List
				List<Map<String, Object>> prtDeliverdetailList = new ArrayList<Map<String,Object>>();
				Iterator detailIt = detailListGroup.entrySet().iterator();
				while (detailIt.hasNext()) {
					Entry detailEn = (Entry) detailIt.next();
					prtDeliverdetailList.add((Map<String, Object>) detailEn.getValue());
				}
				
				UserInfo userinfo = (UserInfo) sessionMap.get("userInfo");
				
				// 关联单号(订货单号)
				String relevanceNo = ConvertUtil.getString(mainMap.get("relevanceNo"));
				if(!ConvertUtil.isBlank(relevanceNo)){
					// 组装MQ，发送K3订发货消息
					mainMap.put("BAcode", userinfo.getEmployeeCode()); // 操作者
					mainMap.put("BrandCode", userinfo.getBrandCode()); // 品牌CODE
					mainMap.put("sysDateTime", sysDateTime); // 导入时间
					
					
					///////////////////////////////////////////
					Map<String,Object> propSendMQMap = assemblingData(mainMap, prtDeliverdetailList);
					binOLMQCOM02_BL.sendData(propSendMQMap, "posToCherryMsgQueue");
					// 成功导入
					mainMap.put("ImportResult", "1");
					
				}else {
					//主表     发货区分 1：未发货 2：已发货 3：已收货 4：已发货			
					mainMap.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);			
					//主表     业务类型
					mainMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
					//主表	审核区分
					mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
					//主表     关联单号
					mainMap.put("RelevantNo", null);	
					int prtDeliverId = 0;
					// 共通将数据添加到发货单主表和明细表
					prtDeliverId = binOLSTCM03_BL.insertProductDeliverAll(mainMap, prtDeliverdetailList);
					if (prtDeliverId == 0) {
						// 抛出自定义异常：操作失败！
						throw new CherryException("ISS00005");
					}
					
					String receiveOrganizationID = (String)mainMap.get("BIN_OrganizationIDReceive");
					// 如果收货部门是柜台，则要发送MQ消息
					boolean isCounter = binOLSSCM05_BL.checkOrganizationType(receiveOrganizationID);
					// 准备参数，开始工作流
					Map<String, Object> pramMap = new HashMap<String, Object>();
					pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
					pramMap.put(CherryConstants.OS_MAINKEY_BILLID, prtDeliverId);
					pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
					pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
					pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
					pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
					pramMap.put("CurrentUnit", "BINOLSTSFH12");
					pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
					pramMap.put("SendToCounter", isCounter ? "YES" : "NO");
					pramMap.put("UserInfo", userinfo);
					pramMap.put("BIN_OrganizationInfoID", userinfo.getBIN_OrganizationInfoID());
					pramMap.put("BrandCode", userinfo.getBrandCode());
					Map<String,Object> departmentInfo = binolcm01BL.getDepartmentInfoByID(ConvertUtil.getString(mainMap.get("BIN_OrganizationID")), null);
			        if(null != departmentInfo && !departmentInfo.isEmpty()){
			            pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
			        }
					pramMap.put("DeliverType", mainMap.get("DeliverType"));
					pramMap.put("BIN_OrganizationIDReceive", receiveOrganizationID);
					long workFlowId = 0;
					workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
					
					if (workFlowId == 0) {
						throw new CherryException("EWF00001");
					}
					mainMap.put("ImportResult", "1");
					mainMap.put("WorkFlowID", workFlowId);
				}
				
				// 将数据添加到发货单（Excel导入）主表和明细表
				int BIN_ProductDeliverExcelID = binOLSTSFH12_Service
						.insertPrtDeliverExcel(mainMap);
				for (Map<String, Object> map : detailList) {
					map.put("BIN_ProductDeliverExcelID", BIN_ProductDeliverExcelID);
					map.put("ErrorMsg", PropertiesUtil.getText("STM00014"));
					binOLSTSFH12_Service.insertPrtDeliverDetailExcel(map);
				}
				successCount++;
			}
		}
		resultMap.put("successCount", successCount);
		resultMap.put("errorCount", errorCount);
		resultMap.put("errorInDeportList", errorPrtDeliverList);
		return resultMap;
	}

	@Override
	public int insertImportBatch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("ImportBatchCodeIF",map.get("ImportBatchCode"));
		return binOLSTSFH12_Service.insertImportBatch(paramMap);
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
		String excelDepartCodeFrom = ConvertUtil.getString(rowMap.get("excelDepartCodeFrom"));
		String excelDepartNameFrom = ConvertUtil.getString(rowMap.get("excelDepartNameFrom"));
		String excelDepartCodeTo = ConvertUtil.getString(rowMap.get("excelDepartCodeTo"));
		String excelDepartNameTo = ConvertUtil.getString(rowMap.get("excelDepartNameTo"));
		String excelDeliverType = ConvertUtil.getString(rowMap.get("excelDeliverType"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelLogicInventoryName = ConvertUtil.getString(rowMap.get("excelLogicInventoryName"));
		String excelPlanArriveDate = ConvertUtil.getString(rowMap.get("excelPlanArriveDate"));
		// 关联单号(订货单号)
		String relevanceNo = ConvertUtil.getString(rowMap.get("relevanceNo"));
		
		
		if(CherryChecker.isNullOrEmpty(excelUnitCode, true) || excelUnitCode.length() > 20){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"A"+rowNumber});
		}
		if(excelBarCode.length() > 13){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"B"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelProductName, true) || excelProductName.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"C"+rowNumber});
		}
		// 当关联订单号为空时，发货部门是必填项
		if(ConvertUtil.isBlank(relevanceNo)){
			if(CherryChecker.isNullOrEmpty(excelDepartCodeFrom, true) || excelDepartCodeFrom.length() > 15){
				throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"D"+rowNumber});
			}
			if(CherryChecker.isNullOrEmpty(excelDepartNameFrom, true) || excelDepartNameFrom.length() > 50){
				throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"E"+rowNumber});
			}
		}
		// 收货部门是必填项，即使在有关联订货单号时也应该有（发送的MQ时需要用到）
		if(CherryChecker.isNullOrEmpty(excelDepartCodeTo, true) || excelDepartCodeTo.length() > 15){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"F"+rowNumber});
		}
		if(CherryChecker.isNullOrEmpty(excelDepartNameTo, true) || excelDepartNameTo.length() > 50){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"G"+rowNumber});
		}
//		boolean isDeliverType = false;
		// 有关联订货单号时发货类型不是必填项
		if(ConvertUtil.isBlank(relevanceNo)){
			// 发货类型必须是code值为1168对应的值
			if(!CherryChecker.isNullOrEmpty(excelDeliverType, true)){
				List<Map<String, Object>> deliverTypeList = CodeTable.getCodes("1168");
				for(Map<String, Object> type : deliverTypeList){
					if(excelDeliverType.equals(type.get("Value"))){
//						isDeliverType = true;
						rowMap.put("excelDeliverType", type.get("CodeKey"));
						break;
					}
				}
			}
			
		    if(!CherryChecker.isNullOrEmpty(excelPrice, true) && !CherryChecker.isFloatValid(excelPrice,14,2)){
		        throw new CherryException("EBS00034",new String[]{CherryConstants.INDEPOT_SHEET_NAME,"I"+rowNumber});
		    }
		}
		
		if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isPositiveAndNegative(excelQuantity) ||  excelQuantity.length() > 9){
			// 数据格式有误
			if(ConvertUtil.isBlank(relevanceNo)){
				// 无关联订货单的直接报错
				throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"J"+rowNumber});
			} else {
				if(!CherryChecker.isNullOrEmpty(excelQuantity, true)) {
					// 有关联订货单且数量不为空时则一定是因为格式不对或者数据超长
					throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"J"+rowNumber});
				} else {
					// 有关联订货单且数量为空，将其他转化为0
					rowMap.put("excelQuantity", ConvertUtil.getInt(excelQuantity));
				}
			}
			
		} else {
			// 将类似于01的数据转换为1【不将其认为为错误数据----"asdf"将会转化为"0"】
			rowMap.put("excelQuantity", ConvertUtil.getInt(excelQuantity));
		}
		
		if(excelLogicInventoryName.length()>30){
			throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"K"+rowNumber});
		}
		
		if(!excelPlanArriveDate.equals("")){
		    if(!CherryChecker.checkDate(excelPlanArriveDate) || !CherryChecker.checkDate(excelPlanArriveDate, "yyyy-DD-mm")){
		        throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"L"+rowNumber});
		    }
		}
		
		if(!CherryChecker.isNullOrEmpty(relevanceNo)){
			// 关联单号(订货单号)
			if(!CherryChecker.isAlphanumeric(relevanceNo)){
				// 数据格式校验
				throw new CherryException("EBS00034",new String[]{CherryConstants.DELIVER_SHEET_NAME,"M"+rowNumber});
			}else if(relevanceNo.length() > 40){
				// 长度校验
				throw new CherryException("EBS00033",new String[]{CherryConstants.DELIVER_SHEET_NAME,"M"+rowNumber,"40"});
			}else{
				if(this.getPrtOrderList(rowMap) == 0){
					throw new CherryException("EBS00141",new String[]{CherryConstants.DELIVER_SHEET_NAME,"M"+rowNumber,"关联单号["+relevanceNo+"]"});
				}
			}
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
		String excelDepartCodeFrom = ConvertUtil.getString(rowMap.get("excelDepartCodeFrom"));
		String excelDepartCodeTo = ConvertUtil.getString(rowMap.get("excelDepartCodeTo"));
		String excelDeliverType = ConvertUtil.getString(rowMap.get("excelDeliverType"));
		String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
		String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
		String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
		String excelPrice = ConvertUtil.getString(rowMap.get("excelPrice"));
		String relevanceNo = ConvertUtil.getString(rowMap.get("relevanceNo"));
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramsMap.put("departCodeFrom", excelDepartCodeFrom);
		paramsMap.put("departCodeTo", excelDepartCodeTo);
		paramsMap.put("deliverType", excelDeliverType);
		paramsMap.put("unitCode", excelUnitCode);
		paramsMap.put("quantity", excelQuantity);
		paramsMap.put("barCode", excelBarCode);
		paramsMap.put("price", excelPrice);
		paramsMap.put("relevanceNo", relevanceNo);
		// 加入了关联订单号来判断是否为重复数据
		List<Map<String, Object>> repeartData = binOLSTSFH12_Service.getRepeatData(paramsMap);
		if(repeartData != null && repeartData.size() >0){
			return true;
		}
		return false;
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
		// 明细数
		int listSize = (detailList != null) ? detailList.size() : 0;
		// 主数据
		Map<String, Object> commonInfo = new HashMap<String, Object>();
		// 主数据错误信息
		Map<String, Object> commonErrorsMap = new HashMap<String, Object>();
		if (listSize > 0) {
			// 一单中发货部门信息、收货部门信息、发货类型相同，只需一次验证(主数据)
			Map<String, Object> infoMap = detailList.get(0);
			
			// 关联单号(订货单号)
			String relevanceNo = ConvertUtil.getString(infoMap.get("relevanceNo"));
			if(ConvertUtil.isBlank(relevanceNo)){
				
				// 校验发货部门名称
				String departNameFrom = ConvertUtil.getString(infoMap.get("excelDepartNameFrom"));
				if(CherryChecker.isNullOrEmpty(departNameFrom)){
					resultFlag = true;
					commonErrorsMap.put("departNameFromNull", true);
				}else if (departNameFrom.length() > 50) {
					resultFlag = true;
					commonErrorsMap.put("departNameFromError", true);
					commonErrorsMap.put("departNameFrom", departNameFrom);
				} else {
					commonInfo.put("ImportDepartNameFrom", departNameFrom);
				}
				// 校验发货部门code
				String departCodeFrom = ConvertUtil.getString(infoMap.get("excelDepartCodeFrom"));
				if (CherryChecker.isNullOrEmpty(departCodeFrom)) {
					resultFlag = true;
					commonErrorsMap.put("departCodeFromNull", true);
				} else if (departCodeFrom.length() > 15) {
					resultFlag = true;
					commonErrorsMap.put("departCodeFromLength", true);
					commonErrorsMap.put("departCodeFrom", departCodeFrom);
				} else{
					commonInfo.put("ImportDepartCodeFrom", departCodeFrom);
					Map<String, Object> orgParamMap = new HashMap<String, Object>();
					orgParamMap.putAll(sessionMap);
					orgParamMap.put("DepartCode", departCodeFrom);
					// 部门类型不能为柜台
					orgParamMap.put("departType", "4");
					// 部门权限
					if(null != sessionMap.get(CherryConstants.BRANDINFOID) && "-9999".equals(sessionMap.get(CherryConstants.BRANDINFOID))) {
						orgParamMap.put("privilegeFlg", "0");
					} else {
						orgParamMap.put("privilegeFlg", "1");
					}
					// 操作类型：0：更新 1：查询【此处是取可进行发货的部门，故操作类型为更新】
					orgParamMap.put("operationType", "0");
					// 业务类型：1：库存
					orgParamMap.put("businessType", "1");
					// 根据部门code获取部门信息【带权限且部门类型不能为‘Z’与柜台】
					Map<String, Object> orgMap = binOLSTSFH12_Service.getOrgByCode(orgParamMap);
					if (orgMap == null) {
						resultFlag = true;
						// 指定部门不存在或者无权限操作
						commonErrorsMap.put("departCodeFromExits", true);
						commonErrorsMap.put("departCodeFrom", departCodeFrom);
						// 校验收货部门时依据此字段判断是否进行校验
						commonInfo.put("BIN_OrganizationID", null);
					} else {
						// 校验正确，获取部门ID
						commonInfo.put("BIN_OrganizationID", orgMap.get("BIN_OrganizationID"));
						
						// 获取默认实体仓库LIST
						List<Map<String, Object>> depotFromList = binOLCM18_BL.getDepotsByDepartID(
								ConvertUtil.getString(orgMap.get("BIN_OrganizationID")), "");
						if (depotFromList.size() > 0) {
							Map<String, Object> inventoryInfo = depotFromList.get(0);
							// 获取实体仓库ID
							commonInfo.put("BIN_DepotInfoID", inventoryInfo.get("BIN_DepotInfoID"));
							// 【发货逻辑的明细数据】
							commonInfo.put("BIN_InventoryInfoID", inventoryInfo.get("BIN_DepotInfoID"));
							//仓库库位
							commonInfo.put("BIN_StorageLocationInfoID", "0");
							//包装类型
							commonInfo.put("BIN_ProductVendorPackageID", "0");
						} else {
							resultFlag = true;
							commonErrorsMap.put("depotFromError", true);
							// 用于后续是否校验收货仓库信息
							commonInfo.put("BIN_DepotInfoID", null);
						}
					}
				}
				
				/***************校验发货逻辑仓库信息********************/
				// 校验发货逻辑仓库名称
				String logicInventoryName = ConvertUtil.getString(infoMap.get("excelLogicInventoryName"));
				if(CherryChecker.isNullOrEmpty(logicInventoryName)){
					// 逻辑仓库为为空，取后台默认逻辑仓库
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.putAll(sessionMap);
					paramMap.put("Type", "0");
					paramMap.put("DefaultFlag", "1");
					// 获取逻辑仓库信息
					Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(paramMap);
					commonInfo.put("BIN_LogicInventoryInfoID", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
					commonInfo.put("LogicInventoryName", logicInventoryInfo.get("InventoryNameCN"));
				}else if(!CherryChecker.isNullOrEmpty(logicInventoryName) && logicInventoryName.length() > 30){
					resultFlag = true;
					commonErrorsMap.put("logicInventoryNameLength", true);
					commonErrorsMap.put("logicInventoryName", logicInventoryName);
				}else if(!CherryChecker.isNullOrEmpty(logicInventoryName)){
					commonInfo.put("LogicInventoryName", logicInventoryName);
					// 逻辑仓库不为空，根据发货部门类型获取指定类型的逻辑仓库，并判断逻辑仓库是否存在
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.putAll(sessionMap);
					paramMap.put("Type", "0");
					paramMap.put("InventoryName", logicInventoryName);
					Map<String, Object> logicInventoryInfo = binOLSTSFH12_Service.getLogicInventoryByName(paramMap);
					if(CherryChecker.isNullOrEmpty(logicInventoryInfo)){
						resultFlag = true;
						// 逻辑仓库不存在
						commonErrorsMap.put("logicInventoryNameExits", true);
					}else{
						commonInfo.putAll(logicInventoryInfo);
					}
				}else{
					commonInfo.put("LogicInventoryName", logicInventoryName);
				}
				
				/******************校验收货部门信息（须根据收货部门、实体仓库、逻辑仓库进行校验）***********/
				// 校验收货部门名称
				String departNameTo = ConvertUtil.getString(infoMap.get("excelDepartNameTo"));
				if(CherryChecker.isNullOrEmpty(departNameTo)){
					resultFlag = true;
					commonErrorsMap.put("departNameToNull", true);
				}else if (departNameTo.length() > 50) {
					resultFlag = true;
					commonErrorsMap.put("departNameToError", true);
					commonErrorsMap.put("departNameTo", departNameTo);
				} else {
					commonInfo.put("ImportDepartNameTo", departNameTo);
				}
				
				// 校验收货部门code
				String departCodeTo = ConvertUtil.getString(infoMap.get("excelDepartCodeTo"));
				if (CherryChecker.isNullOrEmpty(departCodeTo)) {
					resultFlag = true;
					commonErrorsMap.put("departCodeToNull", true);
				} else if (departCodeTo.length() > 15) {
					resultFlag = true;
					commonErrorsMap.put("departCodeToLength", true);
					commonErrorsMap.put("departCodeTo", departCodeTo);
				} else {
					commonInfo.put("ImportDepartCodeTo", departCodeTo);
					sessionMap.put("DepartCode", departCodeTo);
					// 根据收货部门code获取部门信息(可为柜台)
					Map<String, Object> orgMap = binOLSTSFH12_Service.getOrgByCode(sessionMap);
					if (orgMap == null) {
						resultFlag = true;
						commonErrorsMap.put("departCodeToExits", true);
					} else {
						// 当收货部门不存在时无需验证收货部门【收货部门的有效性是依据发货部门的】
						if(!CherryChecker.isNullOrEmpty(commonInfo.get("BIN_OrganizationID")) && !CherryChecker.isNullOrEmpty(commonInfo.get("BIN_DepotInfoID"))) {
							String BIN_OrganizationIDReceive = ConvertUtil.getString(orgMap.get("BIN_OrganizationID"));
							/***************接收部门必须是指定实体仓库与逻辑仓库可发往的部门***************/
							orgMap = this.getCompleteParam(sessionMap,commonInfo);
							
							//取得系统基本配置信息中的"仓库业务配置"
							String ret = binOLCM14_BL.getConfigValue("1028",String.valueOf(orgMap.get("BIN_OrganizationInfoID")),String.valueOf(orgMap.get("BIN_BrandInfoID")));
							int orgReceiveCount = 0;
							orgMap.put("BIN_OrganizationIDReceive", BIN_OrganizationIDReceive);
							//按部权限大小关系
							if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
								// 当仓库业务关系是按部门层级高低关系时根据仓库ID取得部门
								orgReceiveCount = this.getDepartCountByOrgRelationship(orgMap);
							}//按区域大小关系
							else if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
								// 当仓库业务关系是按实际业务配置设定时根据仓库ID取得部门
								orgReceiveCount = this.getDepotCountByRegionRelationship(orgMap);
							}
							if(orgReceiveCount == 0) {
								// 不存在 或者无权限操作此收货部门！
								resultFlag = true;
								commonErrorsMap.put("departCodeToExits", true);
								commonErrorsMap.put("departCodeTo", departCodeTo);
							} else {
								commonInfo.put("BIN_OrganizationIDReceive", BIN_OrganizationIDReceive);
							}
						}
						
					}
				}
				
				/******************校验发货类型（解析excel时已经验证，此处将参数写入主数据中）***********/
				String deliverType = ConvertUtil.getString(infoMap.get("excelDeliverType"));
				commonInfo.put("DeliverType", deliverType);
				
				//校验到货日期（解析excel时已经验证，此处将参数写入主数据中）
				String planArriveDate = ConvertUtil.getString(infoMap.get("excelPlanArriveDate"));
				commonInfo.put("PlanArriveDate", planArriveDate);
			} else {
				// 有关联订货单时不校验发货部门与收货部门，但是数据还需要写入到EXCEL导入记录表中
				commonInfo.put("ImportDepartNameFrom", infoMap.get("excelDepartNameFrom"));
				commonInfo.put("ImportDepartCodeFrom", infoMap.get("excelDepartCodeFrom"));
				commonInfo.put("ImportDepartNameTo", infoMap.get("excelDepartNameTo"));
				commonInfo.put("ImportDepartCodeTo", infoMap.get("excelDepartCodeTo"));
				commonInfo.put("DeliverType", infoMap.get("excelDeliverType"));
				commonInfo.put("PlanArriveDate", infoMap.get("excelPlanArriveDate"));
			}
			
		}
		
	    String organizationInfoID = ConvertUtil.getString(sessionMap.get(CherryConstants.ORGANIZATIONINFOID));
	    String brandInfoId = ConvertUtil.getString(sessionMap.get(CherryConstants.BRANDINFOID));
	    //配置项发货使用价格（销售价格/会员价格）
	    String priceColName = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
	    // 系统配置项增加了结算价与采购价（成本价）
	    if(null == priceColName || "".equals(priceColName)) {
	    	priceColName = "SalePrice";
	    }
		
		int detailNo=1;
		for(Map<String, Object> detailMap : detailList){
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			errorsMap.putAll(commonErrorsMap);
			detailMap.putAll(sessionMap);
			detailMap.putAll(commonInfo);
			// 产品明细连番
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
				List<Map<String, Object>> prtInfoList = binOLSTSFH12_Service.getPrtInfo(sessionMap);
				if (prtInfoList == null || prtInfoList.size() == 0) {
					resultFlag = true;
					errorsMap.put("unitCodeExits", true);
				} else {
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
			// 发货数量校验
			String quantity = ConvertUtil.getString(detailMap.get("excelQuantity"));
			if(CherryChecker.isNullOrEmpty(quantity, true) || "0".equals(quantity)){
				// 对于有关联订货单的明细，允许数量为空或者0
				if(!ConvertUtil.isBlank(ConvertUtil.getString(detailMap.get("relevanceNo")))) {
					quantity = String.valueOf(ConvertUtil.getInt(quantity));
					detailMap.put("Quantity", quantity);
				} else {
					resultFlag = true;
					errorsMap.put("quantityNull", true);
				}
			}else if (!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(quantity))||quantity.length() > 9) {
				resultFlag = true;
				errorsMap.put("quantityError", true);
				errorsMap.put("quantity", quantity);
			}else{
				detailMap.put("Quantity", quantity);
			}
			// 产品价格
	        String price = ConvertUtil.getString(detailMap.get("excelPrice"));
			if (errorsMap.containsKey("unitCodeNull") || errorsMap.containsKey("unitCodeLength")
					|| errorsMap.containsKey("unitCodeExits")) {
				detailMap.put("Price", price);
				detailMap.put("ReferencePrice",0);
			} else {
				sessionMap.put("BIN_ProductID", detailMap.get("BIN_ProductID"));
				//detailMap.put("Price",getPrice(binOLSTSFH12_Service.getPrtPrice(sessionMap),detailMap.get("ImportDate")));
				//验证价格，Excel的价格如果为空取数据库的价格
				if(!price.equals("") && !CherryChecker.isFloatValid(price,14,2)){
                    resultFlag = true;
                    errorsMap.put("priceError", true);
                    errorsMap.put("price", price);
				}else{
					String sysDate = ConvertUtil.getString(detailMap.get("ImportDate"));
	                String referencePrice = ConvertUtil.getString(getPrice(binOLSTSFH12_Service.getPrtPrice(sessionMap),sysDate.substring(0, 10),priceColName));
	                if(price.equals("")){
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
	
	private Map<String, Object> getCompleteParam(Map<String, Object> sessionMap,Map<String, Object> commonInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(sessionMap);
		paramMap.put("SORT_ID", "organizationId asc");
		paramMap.put("BusinessType", "40");
		paramMap.put("InOutFlag", "OUT");
		paramMap.put("businessType", "1");
		paramMap.put("operationType", "0");
		paramMap.put("BIN_OrganizationID", commonInfo.get("BIN_OrganizationID"));
		paramMap.put("DepotID", commonInfo.get("BIN_DepotInfoID"));
//		paramMap.put("IDisplayLength", value);
		paramMap.put("BIN_BrandInfoID", sessionMap.get("brandInfoId"));
		paramMap.put("BIN_OrganizationInfoID", sessionMap.get("organizationInfoId"));
		paramMap.put("BIN_UserID", sessionMap.get("userId"));
		return paramMap;
	}

	/**
	 * 当仓库业务关系是按区域大小设定时，根据仓库ID判断该收货部门是否存在
	 * @param orgMap
	 * @return
	 */
	private int getDepotCountByRegionRelationship(Map<String, Object> param) {
		//调用共通, 根据指定的仓库和业务类型，取得对方仓库信息
		List<Map<String,Object>> depotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
		if(depotList.isEmpty()){
			return 0;
		}
		// 分页查询
		int startIndex = 0;
		int stepLengh = 500;
		int endIndex = 0;
		//仓库参数数组
		Integer[] inventoryInfoID = new Integer[stepLengh];
		//查询参数Map
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.putAll(param);
		/**
		 * 票号：WITPOSQA-15997
		 * 仓库参数数组过长，报未知错误【参数分页查询】
		 */
		while(true) {
			if(depotList.size() <= startIndex) {
				return 0;
			}
			endIndex = startIndex + stepLengh;
			if(depotList.size() <= endIndex){
				endIndex = depotList.size();
				//仓库参数数组
				inventoryInfoID = new Integer[depotList.size()-startIndex];
			}
	
			//遍历depotList,根据仓库ID取得部门信息
			for(int index=startIndex,i=0 ; index<endIndex; index++,i++){
			    int curInventoryInfoID = CherryUtil.obj2int(depotList.get(index).get("BIN_DepotInfoID"));
				inventoryInfoID[i] = curInventoryInfoID;
			}
			
		    //仓库数组
		    paramMap.put("inventoryInfoID", inventoryInfoID);
			//TestType
			paramMap.put("TestType", depotList.get(0).get("TestType"));
			// 判断指定收货部门是否在仓库ID对应的部门内
	        int count = binOLSTSFH12_Service.getDepartInfoCount(paramMap);
	        if(count > 0) {
	        	return 1;
	        }
	        // 下一页仓库数组参数
	        startIndex += stepLengh;
		}
        
	}

	/**
	 * 当仓库业务关系是按部门层级关系时,根据仓库ID判断该收货部门是否存在
	 * @param orgMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int getDepartCountByOrgRelationship(Map<String, Object> paramMap) {
		//部门list
		List<Map<String,Object>> departList = null;;
		//总数
		int count = 0;
		// 取得该部门对应的类型
		Map<String,Object> departMap = binOLSTCM10_Service.getDepartTypeByID(ConvertUtil.getInt(paramMap.get("BIN_OrganizationID")));
		//取得该部门等级比它低的部门类型
		List<Map<String,Object>> codeList = CodeTable.getSubCodesByKey("1000", departMap.get("Type"));
		paramMap.put("TestType", departMap.get("TestType"));
		List<String> typeList = new ArrayList<String>();
		for(Map<String,Object> codeMap : codeList){
			typeList.add(ConvertUtil.getString(codeMap.get("CodeKey")));
		}
		paramMap.put("Type", typeList);
		
		count = binOLSTSFH12_Service.getChildDepartCount(paramMap);
		
		return count;
	}

	/**
	 * 将错误信息转换为字符串
	 * 
	 * @param errorMap
	 * @return
	 */
	public String getErrorMsg(Map<String, Object> errorMap) {
		StringBuffer errorMsg = new StringBuffer();
		if (errorMap.get("departCodeFromExits") != null) {
			// 不存在该编码的发货部门
			errorMsg.append(PropertiesUtil.getText("STM00031")+PropertiesUtil.getText("PST00007"));
		}
		if (errorMap.get("departCodeFromNull") != null) {
			// 发货部门编码不能为空
			errorMsg.append(PropertiesUtil.getText("PST00007")+PropertiesUtil.getText("STM00002"));
		}
		if (errorMap.get("departCodeToExits") != null) {
			// 不存在该编码的接收方部门
			errorMsg.append(PropertiesUtil.getText("STM00031")+PropertiesUtil.getText("PST00015"));
		}
		if (errorMap.get("departCodeToNull") != null) {
			// 接收方部门编码不能为空
			errorMsg.append(PropertiesUtil.getText("PST00015")+PropertiesUtil.getText("STM00002"));
		}
		if (errorMap.get("depotFromError") != null) {
			// 发货部门不存在实体仓库
			errorMsg.append(PropertiesUtil.getText("PST00007")+PropertiesUtil.getText("STM00033"));
		}
		if (errorMap.get("unitCodeExits") != null) {
			// 不存在该厂商编码的产品！
			errorMsg.append(PropertiesUtil.getText("STM00004"));
		}
		if (errorMap.get("unitCodeNull") != null) {
			// 产品厂商编码不能为空！
			errorMsg.append(PropertiesUtil.getText("STM00005"));
		}
		if (errorMap.get("quantityError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00035",new String[]{PropertiesUtil.getText("PST00030")}));
			errorMsg.append(ConvertUtil.getString(errorMap.get("quantity")));
			errorMsg.append("！");
		}
		if (errorMap.get("departNameFromError") != null) {
			errorMsg.append(PropertiesUtil.getText("PST00007")+PropertiesUtil.getText("STM00039"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departNameFrom")));
			errorMsg.append("！");
		}
		if (errorMap.get("departNameToError") != null) {
			errorMsg.append(PropertiesUtil.getText("PST00015")+PropertiesUtil.getText("STM00039"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departNameTo")));
			errorMsg.append("！");
		}
		if (errorMap.get("productNameError") != null) {
			errorMsg.append(PropertiesUtil.getText("STM00009"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("productName")));
			errorMsg.append("！");
		}
		if (errorMap.get("departCodeFromLength") != null) {
			// 发货部门编码长度不能超过15位，导入编码为：
			errorMsg.append(PropertiesUtil.getText("PST00007")+PropertiesUtil.getText("STM00040"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departCodeFrom")));
			errorMsg.append("！");
		}
		if (errorMap.get("departCodeToLength") != null) {
			// 收货部门编码长度不能超过15位，导入编码为：
			errorMsg.append(PropertiesUtil.getText("PST00015")+PropertiesUtil.getText("STM00040"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("departCodeTo")));
			errorMsg.append("！");
		}
		if (errorMap.get("unitCodeLength") != null) {
			// 产品厂商编码长度不能超过20位，导入编码为：
			errorMsg.append(PropertiesUtil.getText("STM00011"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("unitCode")));
			errorMsg.append("！");
		}	
		if (errorMap.get("barCodeLength") != null) {
			// 产品条码长度不能超过13位，导入条码为：
			errorMsg.append(PropertiesUtil.getText("STM00015"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("barCode")));
			errorMsg.append("！");
		}
		if (errorMap.get("barCodeExits") != null) {
			// 产品条码错误：与此产品的条码不相符！
			errorMsg.append(PropertiesUtil.getText("STM00016"));
		}
		if (errorMap.get("barCodeNull") != null) {
			// 此产品存在多个条码，请选择相应的条码！
			errorMsg.append(PropertiesUtil.getText("STM00017"));
		}
		if (errorMap.get("logicInventoryNameLength") != null) {
			// 逻辑仓库名称长度不能超过30位，导入名称为：
			errorMsg.append(PropertiesUtil.getText("STM00018"));
			errorMsg.append(ConvertUtil.getString(errorMap.get("logicInventoryName")));
			errorMsg.append("！");
		}
		if (errorMap.get("logicInventoryNameExits") != null) {
			// 不存在此逻辑仓库或该类型仓库不能用于给该部门{发货}！
			errorMsg.append(PropertiesUtil.getText("STM00041",new String[]{PropertiesUtil.getText("PST00030")}));
		}
        if(errorMap.get("priceError") != null){
            errorMsg.append(PropertiesUtil.getText("STM00030"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("price")));
            errorMsg.append("！");
        }
		if (errorMap.get("quantityNull") != null) {
			// 发货数量不能为0和空！
			errorMsg.append(PropertiesUtil.getText("STM00038",new String[]{PropertiesUtil.getText("PST00030")}));
		}
		if (errorMap.get("departNameFromNull") != null) {
			// 发货部门名称不能为空
			errorMsg.append(PropertiesUtil.getText("PST00007")+PropertiesUtil.getText("STM00042"));
		}
		if (errorMap.get("departNameToNull") != null) {
			// 接受方部门名称不能为空
			errorMsg.append(PropertiesUtil.getText("PST00015")+PropertiesUtil.getText("STM00042"));
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
	
	/**
	 * 生成发货单据号
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
		String name = "BINOLSTSFH12_BL";
		// 调用共通生成单据号
		return binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name,
				CherryConstants.BUSINESS_TYPE_SD);
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
	 * 取得发货日期时的销售价格/会员价格
	 * 
	 * @param priceList
	 *            按照开始时间排序的产品价格信息
	 * @param inDepotDate
	 *            发货时间
	 * @return
	 */
	public Object getPrice(List<Map<String, Object>> priceList, Object importDate,String priceColName) {
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
				String importDateStr = ConvertUtil.getString(importDate);
				// 当发货日期小于所有价格的日期
				if (i == 0
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("StartDate"))) <= 0) {
					return map.get(priceColName);
				}
				if (CherryChecker.compareDate(importDateStr,
						ConvertUtil.getString(map.get("StartDate"))) >= 0
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("EndDate"))) <= 0) {
					return map.get(priceColName);
				}
				// 当发货日期大于所有的价格日期
				if (i == (length - 1)
						&& CherryChecker.compareDate(importDateStr,
								ConvertUtil.getString(map.get("EndDate"))) >= 0) {
					return map.get(priceColName);
				}
			}
		}
		return 0;

	}
}
