/*  
 * @(#)BINOLMOCIO16_BL.java     1.0 2014/06/26      
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
package com.cherry.mo.cio.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO14_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO16_IF;
import com.cherry.mo.cio.service.BINOLMOCIO16_Service;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.common.service.BINOLMOCOM01_Service;

/**
 * 柜台消息EXCEL导入BL
 * @author menghao
 *
 */
public class BINOLMOCIO16_BL implements BINOLMOCIO16_IF {
	
	@Resource(name="binOLMOCIO16_Service")
	private BINOLMOCIO16_Service binOLMOCIO16_Service;
	
	@Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
	
	@Resource(name="binOLMOCOM01_Service")
	private BINOLMOCOM01_Service binOLMOCOM01_Service;
	    
	@Resource(name="binOLMOCIO14_BL")
	private BINOLMOCIO14_IF binOLMOCIO14_BL;
	    
	/** 导入柜台消息的EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.0";
	
	@Override
	public int getImportBatchCount(Map<String, Object> map) {
		return binOLMOCIO16_Service.getImportBatchCount(map);
	}

	@Override
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		return binOLMOCIO16_Service.getImportBatchList(map);
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
		// 柜台消息数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.COUNTERMESSAGE_SHEET_NAME.equals(st.getName().trim())) {
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
		// 销售目标数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.SALETARGET_SHEET_NAME });
		}
		
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		// 是否导入后即下发标记
		boolean isPublish = "0".equals(ConvertUtil.getString(map.get("isPublish"))) ? false : true;

		int sheetLength = dateSheet.getRows();
		// sheet中的数据
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
		//是否从Excel中获取导入批次号
		String isChecked = ConvertUtil.getString(map.get("isChecked"));
		// 从Excel获取导入批次号
		String importBatchCode = dateSheet.getCell(1, 0).getContents().trim();
		//导入批次号校验[对于直接从页面获取导入批次的校验已经在Action中预校验过了]
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
		
		// 循环导入柜台消息信息，从第4行开始为要导入的信息
		int r = 3;
		for (r = 3; r < sheetLength; r++) {
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			// 当前品牌CODE
			rowMap.put("BrandCode", brand_code);
			// 品牌CODE（A）
			String excelBrandCode = dateSheet.getCell(0, r).getContents().trim();
			rowMap.put("excelBrandCode", excelBrandCode);
			// 柜台号（B）
			String excelCounterCode = dateSheet.getCell(1, r).getContents().trim();
			rowMap.put("excelCounterCode", excelCounterCode);
			// 柜台名称（C）
			String excelCounterName = dateSheet.getCell(2, r).getContents().trim();
			rowMap.put("excelCounterName", excelCounterName);
			// 消息标题（D）
			String excelMessageTitle = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("excelMessageTitle", excelMessageTitle);
			// 生效开始日期（F）
			String excelStartValidDate = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("excelStartValidDate", excelStartValidDate);
			// 生效结束日期（G）
			String excelEndValidDate = dateSheet.getCell(5, r).getContents();
			rowMap.put("excelEndValidDate", excelEndValidDate);
			// 消息内容（E）
			String excelMessageBody = dateSheet.getCell(6, r).getContents().trim();
			rowMap.put("excelMessageBody", excelMessageBody);
			
			rowMap.put("rowNumber", r+1);
			// 整行数据为空，程序认为sheet内有效行读取结束
			if ("".equals(excelBrandCode) && "".equals(excelCounterCode)
					&& "".equals(excelCounterName) && "".equals(excelMessageTitle)
					&& "".equals(excelStartValidDate) && "".equals(excelEndValidDate)
					&& "".equals(excelMessageBody)) {
				break;
			}
			
			this.checkData(rowMap,isPublish);
			
			// 根据柜台消息标题+开始日期+结束日期+消息内容拆分为一条消息
			String importDatasKey = excelMessageTitle + excelStartValidDate + excelEndValidDate + excelMessageBody;
			if(importDatas.containsKey(importDatasKey)) {
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
		
		if(r==3){
			throw new CherryException("EBS00035",new String[] { CherryConstants.COUNTERMESSAGE_SHEET_NAME });
		}
		return importDatas;
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @return
	 * @throws Exception
	 */
	public boolean checkData(Map<String, Object> rowMap,boolean isPublish) throws Exception {
		// 当前品牌CODE
		String brand_code = ConvertUtil.getString(rowMap.get("BrandCode"));
		// 行数
		String rowNumber = ConvertUtil.getString(rowMap.get("rowNumber"));
		// 删除rowMap中的行序号信息[此信息用于标识错误信息]
		rowMap.remove("rowNumber");
		// 品牌CODE（A）
		String excelBrandCode =  ConvertUtil.getString(rowMap.get("excelBrandCode"));
		// 柜台号（B）
		String excelCounterCode = ConvertUtil.getString(rowMap.get("excelCounterCode"));
		// 柜台名称（C）
		String excelCounterName = ConvertUtil.getString(rowMap.get("excelCounterName"));
		// 消息标题（D）
		String excelMessageTitle = ConvertUtil.getString(rowMap.get("excelMessageTitle"));
		// 生效开始日期（F）
		String excelStartValidDate = ConvertUtil.getString(rowMap.get("excelStartValidDate"));
		// 生效结束日期（G）
		String excelEndValidDate = ConvertUtil.getString(rowMap.get("excelEndValidDate"));
		// 消息内容（E）
		String excelMessageBody = ConvertUtil.getString(rowMap.get("excelMessageBody"));
		
		if(CherryChecker.isNullOrEmpty(excelBrandCode, true)){
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "A" + rowNumber });
		} else if(excelBrandCode.length() > 10) {
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "A" + rowNumber, "10" });
		} else if(!excelBrandCode.equals(brand_code)) {
			// 出现不符合的品牌
			throw new CherryException("EBS00032", new String[] {CherryConstants.COUNTERMESSAGE_SHEET_NAME, "A" + rowNumber });
		}
		
		if(CherryChecker.isNullOrEmpty(excelCounterCode, true)){
			// 单元格为空【需要下发时柜台CODE才不能为空】
			if(isPublish) {
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + rowNumber });
			}
		} else if(excelCounterCode.length() > 15){
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "B" + rowNumber, "15" });
		} 
		
		if(excelCounterName.length() > 50){
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "C" + rowNumber, "50" });
		} 
		
		// 消息标题必填
		if (CherryChecker.isNullOrEmpty(excelMessageTitle, true)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "D" + rowNumber });
		} else if(excelMessageTitle.length() > 10){
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "D" + rowNumber, "10" });
		} 
		
		// 开始结束日期都是必填的
		if (CherryChecker.isNullOrEmpty(excelStartValidDate, true)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "E" + rowNumber });
		} else if (!CherryChecker.checkDate(excelStartValidDate)
				|| !CherryChecker.checkDate(excelStartValidDate, "yyyy-DD-mm")) {
			throw new CherryException("EBS00034", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "E" + rowNumber });
		}
		
		if (CherryChecker.isNullOrEmpty(excelEndValidDate, true)) {
			// 单元格为空
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "F" + rowNumber });
		} else if (!CherryChecker.checkDate(excelEndValidDate)
				|| !CherryChecker.checkDate(excelEndValidDate, "yyyy-DD-mm")) {
			throw new CherryException("EBS00034", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "F" + rowNumber });
		}
		
		// 消息内容可不填
		if(excelMessageBody.length() > 144){
			// 长度错误
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.COUNTER_SHEET_NAME, "G" + rowNumber, "144" });
		} 
		
		return true;
	}

	@Override
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> map)
			throws Exception {
		// 是否下发到柜台标记
		boolean isPublish = "0".equals(ConvertUtil.getString(map.get("isPublish"))) ? false : true;
		// 当前日期
		String currentDate = binOLMOCIO16_Service.getDateYMD();
		map.put("ImportDate", currentDate);
		// 插入导入批次信息返回导入批次ID
		int importBatchId = this.insertImportBatch(map);
		// 导入结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功的数量
		int successCount = 0;
		// 导入失败的数量
		int failCount = 0;
		// 导入失败的柜台消息
		Iterator it = importDataMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry en = (Entry) it.next();
			// 明细数据
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) en.getValue();
			// 验证数据的有效性
			Map<String, Object> detailMap = this.validExcel(detailList, map, isPublish);
			// 下发的柜台信息list
			detailList = (List<Map<String, Object>>) detailMap.get("resultDetailList");
			Map<String, Object> cntMsgMainMap = new HashMap<String, Object>();
			if(detailList != null && detailList.size() > 0) {
				// 柜台消息主数据[每条明细数据都有主数据]
				cntMsgMainMap.putAll(detailList.get(0));
				// 导入日期
				cntMsgMainMap.put("ImportDate", currentDate);
				// 导入批次ID
				cntMsgMainMap.put("BIN_ImportBatchID", importBatchId);
				// 导入批次号
				cntMsgMainMap.put("ImportBatch", map.get("ImportBatchCode"));
			} else {
				continue;
			}
			
			if ((Boolean) detailMap.get("ResultFlag")) {
				// 验证不通过,只将数据添加到柜台消息导入主表与明细表中
				cntMsgMainMap.put("ImportResult", "0");
				cntMsgMainMap.putAll(map);
				int BIN_CounterMessageImportID = binOLMOCIO16_Service.insertCounterMessageImport(cntMsgMainMap);
				for(Map<String, Object> counterMap : detailList) {
					counterMap.put("BIN_CounterMessageImportID", BIN_CounterMessageImportID);
					counterMap.putAll(map);
				}
				// 批量插入明细表
				binOLMOCIO16_Service.insertCounterMessageImportDetail(detailList);
				failCount++;
			} else {
				// 验证成功，合并相同柜台明细
				cntMsgMainMap.putAll(map);
				int counterMessageId = binOLMOCIO16_Service.addCounterMessage(cntMsgMainMap);
				if(isPublish) {
					// 需要下发到柜台时直接调用下发方法进行下发
					// 下发时默认【可发柜台】
					List<String> contraryIDList = new ArrayList<String>();
					// 去除重复项【即合并相同柜台明细】
					List<Map<String, Object>> allList = this.removeDuplicate(detailList);
					for(Map<String, Object> allowMap : allList) {
						contraryIDList.add(ConvertUtil.getString(allowMap.get("id")));
					}
					Map<String, Object> publishMap = new HashMap<String, Object>();
					publishMap.putAll(map);
					publishMap.put("contraryIDList", contraryIDList);
					// 取得导入柜台下发类型相对立的柜台【即若导入允许下发柜台则取得禁止下发柜台】
		    		List<Map<String, Object>> contraryList = binOLMOCOM01_Service.getContraryOrgID(publishMap);
		    		publishMap.put("radioControlFlag", MonitorConstants.ControlFlag_Allow);
		    		publishMap.put("counterMessageId", counterMessageId);
		    		// 批量下发柜台
		            binOLMOCIO14_BL.tran_publish(allList, contraryList, publishMap);
		            
		            // 写入柜台消息导入表
		            cntMsgMainMap.put("ImportResult", "1");
					cntMsgMainMap.put("counterMessageId", counterMessageId);
					int BIN_CounterMessageImportID = binOLMOCIO16_Service.insertCounterMessageImport(cntMsgMainMap);
					for(Map<String, Object> counterMap : detailList) {
						counterMap.putAll(map);
						counterMap.put("BIN_CounterMessageImportID", BIN_CounterMessageImportID);
						// 导入并下发成功！
						counterMap.put("ErrorMsg", PropertiesUtil.getText("EMO00095"));
					}
					// 批量插入明细表
					binOLMOCIO16_Service.insertCounterMessageImportDetail(detailList);
				} else {
					// 不进行下发时只导入柜台消息而不涉及下柜台
					cntMsgMainMap.put("ImportResult", "1");
					cntMsgMainMap.put("counterMessageId", counterMessageId);
					cntMsgMainMap.putAll(map);
					int BIN_CounterMessageImportID = binOLMOCIO16_Service.insertCounterMessageImport(cntMsgMainMap);
					cntMsgMainMap.put("BIN_CounterMessageImportID", BIN_CounterMessageImportID);
					// 只导入柜台消息成功！
					cntMsgMainMap.put("ErrorMsg", PropertiesUtil.getText("EMO00094"));
					binOLMOCIO16_Service.insertCounterMessageImportDetail(cntMsgMainMap);
				}
				
				successCount++;
			}
		}
		
		// 保存统计信息
		resultMap.put("totalCount", successCount+failCount);
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		return resultMap;
	}
	
	/**
	 * 去除LIST中的重复项
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List removeDuplicate(List list) {
		List resultList = new ArrayList();
		resultList.addAll(list);
		
		HashSet h = new HashSet(resultList);
		resultList.clear();
		resultList.addAll(h);
		return resultList;
	}

	
	/**
	 * 对一条柜台消息进行验证待下发的柜台信息并补充为要下发的完整信息
	 * @param detailList
	 * @param map
	 * @param isPublish : 是否在导入时同时下发
	 * @return 
	 * 			ResultFlag:一条柜台消息【多个柜台】验证结果
	 * 			resultDetailList：(柜台消息+柜台)LIST数据
	 */
	private Map<String, Object> validExcel(
			List<Map<String, Object>> detailList, Map<String, Object> map,
			boolean isPublish) {
		// 导入结果标记，默认成功
		boolean resultFlag = false;
		// 验证结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 验证后的柜台明细数据
		List<Map<String, Object>> resultDetailList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> commonInfo = new HashMap<String, Object>();
		Map<String, Object> commonErrorsMap = new HashMap<String, Object>();
		// 对于柜台消息的主数据（品牌、标题、生效起止日期、内容）已经验证过，无须再次验证
		if(null != detailList && detailList.size() > 0) {
			// 一条柜台消息中的起止日期只需一次验证
			Map<String, Object> infoMap = detailList.get(0);
			// 结束日期必须不小于开始日期
			String startValidDate = ConvertUtil.getString(infoMap.get("excelStartValidDate"));
			String endValidDate = ConvertUtil.getString(infoMap.get("excelEndValidDate"));
			
			if(startValidDate.compareTo(endValidDate) > 0) {
				resultFlag = true;
				commonErrorsMap.put("validDateError", true);
				
			} else {
				if(isPublish && endValidDate.compareTo(ConvertUtil.getString(map.get("ImportDate"))) < 0) {
					// 不能对已经过期的柜台消息进行下发
					resultFlag = true;
					// 过期错误
					commonErrorsMap.put("overdueError", true);
				}
			}
			// 无论日期验证是否通过都需要这两个参数(或用于显示或用于导入)
			commonInfo.put("startValidDate", startValidDate);
			commonInfo.put("endValidDate", endValidDate);
			
			// 品牌代码
			String brandCode = ConvertUtil.getString(infoMap.get("excelBrandCode"));
			commonInfo.put("brandCode", brandCode);
			// 消息标题
			String messageTitle = ConvertUtil.getString(infoMap.get("excelMessageTitle"));
			commonInfo.put("messageTitle", messageTitle);
			// 消息内容
			String messageBody = ConvertUtil.getString(infoMap.get("excelMessageBody"));
			commonInfo.put("messageBody", messageBody);
			
			// 导入柜台消息并下发时才处理柜台明细信息
			if(isPublish) {
				for(Map<String, Object> detailMap : detailList) {
					Map<String, Object> detailErrorsMap = new HashMap<String, Object>();
					detailErrorsMap.putAll(commonErrorsMap);
					
					// 校验柜台CODE是否存在
					String counterCode = ConvertUtil.getString(detailMap.get("excelCounterCode"));
					String counterName = ConvertUtil.getString(detailMap.get("excelCounterName"));
					detailMap.put("counterCode", counterCode);
					detailMap.put("counterName", counterName);
					map.put("counterCode", counterCode);
					Map<String, Object> counterInfo = binOLMOCOM01_Service.getCounterInfo(map);
					// 删除为map额外增加的参数【防止影响map共通参数的使用】
					map.remove("counterCode");
					if(null == counterInfo || counterInfo.isEmpty()) {
						resultFlag = true;
						detailErrorsMap.put("counterCodeExits", true);
						detailErrorsMap.put("counterCode", counterCode);
					} else {
						// 柜台名称有填写时则必须为柜台CODE对应的柜台名称
						if(!"".equals(counterName)) {
							if(!counterName.equals(counterInfo.get("counterName"))) {
								resultFlag = true;
								detailErrorsMap.put("counterNameError", true);
								detailErrorsMap.put("counterCode", counterCode);
							} else {
								// 柜台的组织ID用于下发时调用下发方法时使用
								detailMap.put("id", counterInfo.get("organizationID"));
							}
						} else {
							// 柜台的组织ID用于下发时调用下发方法时使用
							detailMap.put("id", counterInfo.get("organizationID"));
						}
					}
					// 明细数据错误信息显示
					String errorMsg = getErrorMsg(detailErrorsMap);
					detailMap.put("ErrorMsg", errorMsg);
					detailMap.putAll(commonInfo);
					resultDetailList.add(detailMap);
				}
			} else {
				// 主数据错误信息显示
				String errorMsg = getErrorMsg(commonErrorsMap);
				commonInfo.put("ErrorMsg", errorMsg);
				resultDetailList.add(commonInfo);
			}
			
		}
		resultMap.put("ResultFlag", resultFlag);
		resultMap.put("resultDetailList", resultDetailList);
		return resultMap;
	}

	/**
	 * 取得错误信息
	 * @param errorMap
	 * @return
	 */
	private String getErrorMsg(Map<String, Object> errorMap) {
		StringBuffer errorMsg = new StringBuffer();
		if(errorMap.get("validDateError") != null) {
			// 结束日期应该大于开始日期。
			errorMsg.append(PropertiesUtil.getText("ECM00074"));
		}
		if(errorMap.get("overdueError") != null) {
			// 过期柜台消息不能进行下发！
			errorMsg.append(PropertiesUtil.getText("EMO00093"));
		}
		if(errorMap.get("counterCodeExits") != null) {
			// 柜台编码不存在，导入的编码为【{0}】！
			errorMsg.append(PropertiesUtil.getText("EMO00090",new String[]{ConvertUtil.getString(errorMap.get("counterCode"))}));
		}
		if(errorMap.get("counterNameError") != null) {
			// 结束日期应该大于开始日期。
			errorMsg.append(PropertiesUtil.getText("EMO00091"));
		}
		String errorMsgStr = ConvertUtil.getString(errorMsg);
		if(CherryChecker.isNullOrEmpty(errorMsgStr)){
			errorMsg.append(PropertiesUtil.getText("STM00020"));
		}
		return errorMsg.toString();
	}

	@Override
	public int insertImportBatch(Map<String, Object> map) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.putAll(map);
		paramsMap.put("ImportBatchCodeIF",map.get("ImportBatchCode"));
		return binOLMOCIO16_Service.insertImportBatch(paramsMap);
	}

}
