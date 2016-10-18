/*
 * @(#)BINOLSTJCS11_BL.java     1.0 2014/06/20
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
package com.cherry.st.jcs.bl;

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
import com.cherry.st.jcs.interfaces.BINOLSTJCS11_IF;
import com.cherry.st.jcs.service.BINOLSTJCS11_Service;

/**
 * BB柜台维护BL
 * @author zhangle
 * @version 1.0 2014.06.20
 */
public class BINOLSTJCS11_BL implements BINOLSTJCS11_IF{
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTJCS11_BL.class);
	
    @Resource(name="binOLSTJCS11_Service")
	private BINOLSTJCS11_Service binOLSTJCS11_service;

	@Override
	public int getBBCounterCount(Map<String, Object> map) {
		return binOLSTJCS11_service.getBBCounterCount(map);
	}

	@Override
	public List<Map<String, Object>> getBBCounterList(Map<String, Object> map) {
		return binOLSTJCS11_service.getBBCounterList(map);
	}

	@Override
	public Map<String, Object> getBBCounter(Map<String, Object> map) {
		return binOLSTJCS11_service.getBBCounter(map);
	}

	@Override
	public void tran_save(Map<String, Object> map) throws Exception {
		String BBCounterInfoId = ConvertUtil.getString(map.get("BBCounterInfoId"));
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		paramsMap.put("organizationId", map.get("organizationId"));
		paramsMap.put("batchCode", map.get("batchCode"));
		List<Map<String, Object>> counterList = binOLSTJCS11_service.getAllBBCounterList(paramsMap);
		if(null != counterList && counterList.size() > 0){
			String startTime = ConvertUtil.getString(map.get("startTime"));
			String endTime = ConvertUtil.getString(map.get("endTime"));
			for(Map<String, Object> counterMap : counterList){
				String startTime1 = ConvertUtil.getString(counterMap.get("startDate")) + " " + ConvertUtil.getString(counterMap.get("startTime"));
				String endTime1 = ConvertUtil.getString(counterMap.get("endDate")) + " " + ConvertUtil.getString(counterMap.get("endTime"));
				String BBCounterInfoId1 = ConvertUtil.getString(counterMap.get("BBCounterInfoId"));
				if(!(startTime.compareTo(endTime1) > 0 || endTime.compareTo(startTime1) < 0) 
						&& !BBCounterInfoId.equals(BBCounterInfoId1)){
					//时间相同或交叉
					throw new CherryException("STM00052");
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(BBCounterInfoId, true)){
			binOLSTJCS11_service.addBBCounter(map);
		}else{
			binOLSTJCS11_service.updateBBcounter(map);
		}
	}

	@Override
	public Map<String, Object> tran_import(Map<String, Object> map) throws Exception {
		int batchCount = binOLSTJCS11_service.getBBCounterCount(map);
		if(batchCount > 0){
			//检查导入批次号是否存在
			throw new CherryException("ECM00032", new String[]{PropertiesUtil.getText("STM00012")});
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = this.ResolveExcel(map);
		binOLSTJCS11_service.addBBCounter(dataList);
		resultMap.put("successCount", dataList.size());
		return resultMap;
	}
	
	/**
	 * 从Excel中读取数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
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
		// BB柜台数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.BBCOUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// BB柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.BBCOUNTER_SHEET_NAME});
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.BBCOUNTER_EXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
        }
		
		// sheet的行数
		int sheetLength = dateSheet.getRows();
		// sheet中的数据
		List<Map<String, Object>> importDatas = new ArrayList<Map<String,Object>>();

		int r = 1;
		for (r = 1; r < sheetLength; r++) {
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			// 柜台编码
			String departCode = dateSheet.getCell(0, r).getContents().trim();
			rowMap.put("departCode", departCode);
			// 柜台名称
			String departName = dateSheet.getCell(1, r).getContents().trim();
			rowMap.put("departName", departName);
			// 开始时间
			String startTime = dateSheet.getCell(2, r).getContents().trim();
			rowMap.put("startTime", startTime);
			// 结束时间
			String endTime = dateSheet.getCell(3, r).getContents().trim();
			rowMap.put("endTime", endTime);
			// 备注
			String comments = dateSheet.getCell(4, r).getContents().trim();
			rowMap.put("comments", comments);
			rowMap.put("rowNumber", r+1);
			if (CherryChecker.isNullOrEmpty(departCode, true) && CherryChecker.isNullOrEmpty(departName, true)
					&& CherryChecker.isNullOrEmpty(startTime, true) && CherryChecker.isNullOrEmpty(endTime, true)
					&& CherryChecker.isNullOrEmpty(comments, true)) {
				break;
			}
			rowMap.putAll(map);
			// 数据基本格式校验
			this.checkData(rowMap);
			//若柜台号、开始时间、结束时间都重复或时间有交叉不导入
			this.isRepeatData(rowMap, importDatas);
			importDatas.add(rowMap);
		}
		if(r==1){
			throw new CherryException("EBS00035",new String[] { CherryConstants.BBCOUNTER_SHEET_NAME });
		}
		return importDatas;
	}
	
	/**
	 * 检查数据格式
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	private void checkData(Map<String, Object> map) throws Exception{
		String departCode = ConvertUtil.getString(map.get("departCode"));
		String departName = ConvertUtil.getString(map.get("departName"));
		String startTime = ConvertUtil.getString(map.get("startTime"));
		String endTime = ConvertUtil.getString(map.get("endTime"));
		String rowNumber = ConvertUtil.getString(map.get("rowNumber"));
		String comments = ConvertUtil.getString(map.get("comments"));
		//柜台校验
		if(CherryChecker.isNullOrEmpty(departCode, true)){
			throw new CherryException("EBS00031", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "A"+rowNumber, PropertiesUtil.getText("PBS00051")});
		} else{
			Map<String, Object> counterInfo = binOLSTJCS11_service.getCounterInfoByCode(map);
			if(null == counterInfo || CherryChecker.isNullOrEmpty(counterInfo.get("organizationId"), true)){
				throw new CherryException("STM00047", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "A"+rowNumber});
			}else if(CherryChecker.isNullOrEmpty(departName, true) || !departName.equals(counterInfo.get("counterName"))){
				throw new CherryException("EMO00083", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "B"+rowNumber});
			}else {
				map.putAll(counterInfo);
			}
		}
		//开始时间格式校验
		if(!CherryChecker.checkDate(startTime, "yyyy-MM-dd HH:mm:ss")){
			throw new CherryException("EBS00034", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "C"+rowNumber} );
		}
		//结束时间格式校验
		if(!CherryChecker.checkDate(endTime, "yyyy-MM-dd HH:mm:ss")){
			throw new CherryException("EBS00034", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "D"+rowNumber});
		}
		//结束时间必须大于开始时间
		if(endTime.compareTo(startTime) <= 0){
			throw new CherryException("STM00049", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "D"+rowNumber});
		}
		//备注信息格式错误
		if(!CherryChecker.isNullOrEmpty(comments, true) && comments.length() > 200){
			throw new CherryException("EBS00034", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, "E"+rowNumber});
		}
		
	}
	
	/**
	 * 柜台数据是否重复
	 * @param map
	 * @param dataList
	 * @return
	 * @throws Exception 
	 */
	private void isRepeatData(Map<String, Object> map, List<Map<String, Object>> dataList) throws Exception{
		String departCode = ConvertUtil.getString(map.get("departCode"));
		String startTime = ConvertUtil.getString(map.get("startTime"));
		String endTime = ConvertUtil.getString(map.get("endTime"));
		String rowNumber = ConvertUtil.getString(map.get("rowNumber"));
		if(null != dataList && dataList.size() > 0){
			for(Map<String, Object> rowMap :  dataList){
				if(departCode.equals(rowMap.get("departCode"))){
					String startTime1 = ConvertUtil.getString(rowMap.get("startTime"));
					String endTime1 = ConvertUtil.getString(rowMap.get("endTime"));
					String rowNumber1 = ConvertUtil.getString(rowMap.get("rowNumber"));
					if(startTime.equals(startTime1)
							&& endTime.equals(endTime1)){
						//重复
						throw new CherryException("STM00050", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, rowNumber, rowNumber1});
					}else if(!(startTime.compareTo(endTime1) > 0 
							|| endTime.compareTo(startTime1) < 0)){
						//时间交叉
						throw new CherryException("STM00051", new String[]{CherryConstants.BBCOUNTER_SHEET_NAME, rowNumber, rowNumber1});
					}
				}
			}
		}
		
		
	}

	/**
	 * 停用/启用BB柜台
	 */
	@Override
	public void tran_disabled(Map<String, Object> map) throws Exception {
		String[] allBBcounterInfoId = (String[]) map.get("allBBCounterInfoId");
		String validFlag = ConvertUtil.getString(map.get("validFlag"));
		if(null != allBBcounterInfoId && allBBcounterInfoId.length > 0){
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			for(String BBcounterInfoId : allBBcounterInfoId){
				Map<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("BBCounterInfoId", BBcounterInfoId);
				itemMap.put("validFlag", validFlag);
				dataList.add(itemMap);
			}
			binOLSTJCS11_service.updateBBcounter(dataList);
		}
	}
}

	

