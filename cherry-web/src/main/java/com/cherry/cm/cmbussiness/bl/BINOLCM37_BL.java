/*	
 * @(#)BINOLCM37_BL.java     1.0 2013/04/19		
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
package com.cherry.cm.cmbussiness.bl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 导出excel共通BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/19
 */
public class BINOLCM37_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM37_BL.class.getName());
	
	/** CodeTable **/
	@Resource
    private CodeTable code;
	
	/** 系统配置项 共通 **/
	@Resource
    private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 导出excel处理（导出数据需要分页查询）
	 * 
	 * @param map 查询条件
	 * @param fetchDataHandler 查询数据处理器
	 * @return 字节数组
	 */
	public byte[] exportExcel(Map<String, Object> map, BINOLCM37_IF fetchDataHandler) throws Exception {
		
		// 标题行格式数组
        String[][] titleRows = (String[][])map.get("titleRows");
        // sheet名称
        String sheetName = (String)map.get("sheetName");
        if(sheetName == null) {
        	sheetName = "";
        }
        // 文件头部信息
        String header = (String)map.get("header");
        // 标题行List
        List<String[][]> titleList = (List)map.get("titleList");
        
        // 查询条件
        Map<String, Object> conditionMap = (Map)map.get("conditionMap");
        if(conditionMap == null || conditionMap.isEmpty()) {
        	conditionMap = map;
        }
        
        // 创建工作薄 
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        // 增加防止内存回收的设置
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setGCDisabled(true);
        WritableWorkbook wb = Workbook.createWorkbook(byteOut, workbookSettings);
        WritableSheet ws = null;
        
        // 字体样式
        WritableFont fontStyle = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        // 浮点数格式
        NumberFormat floatFormat = new NumberFormat("0.00");
        WritableCellFormat floatStyle = new WritableCellFormat(floatFormat);
        floatStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 整数格式
        NumberFormat intFormat = new NumberFormat("0");
        WritableCellFormat intStyle = new WritableCellFormat(intFormat);
        intStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 文本格式
        WritableCellFormat stringStyle = new WritableCellFormat();
        stringStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 居右格式
        WritableCellFormat rightStyle = new WritableCellFormat();
        rightStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        rightStyle.setAlignment(jxl.format.Alignment.RIGHT);
        
        // 文件头部信息样式
 		WritableCellFormat headerStyle = new WritableCellFormat();
 		headerStyle.setFont(fontStyle);
 		headerStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
 		headerStyle.setAlignment(jxl.format.Alignment.JUSTIFY);
 		headerStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        
        // 标题行样式
        WritableCellFormat titleStyle = new WritableCellFormat();
        titleStyle.setFont(fontStyle);
        titleStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleStyle.setBackground(jxl.format.Colour.GRAY_25);
        
        // 标题行样式
        WritableCellFormat titleStyle2 = new WritableCellFormat();
        titleStyle2.setFont(fontStyle);
        titleStyle2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleStyle2.setAlignment(jxl.format.Alignment.CENTRE);
        titleStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleStyle2.setBackground(jxl.format.Colour.GRAY_25);
        
        // 每Sheet页最多显示行数，读系统配置项，取不到使用默认值
		String organizationInfoID = String.valueOf(map.get("organizationInfoId"));
		String brandInfoID = String.valueOf(map.get("brandInfoId"));
		String maxRowCountS = binOLCM14_BL.getConfigValue("1031", organizationInfoID, brandInfoID);
		if (null == maxRowCountS || "".equals(maxRowCountS)) {
			maxRowCountS = CherryConstants.DEFAULT_EXCEL_MAXROW;
		}
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		// 导出数据总数
		int count = 0;
        // 每sheet最大数据行
		int maxRowCount = Integer.parseInt(maxRowCountS);
        // 当前sheet行
        int curSheetRow = 0;
        // 当前sheet页
        int curSheetNum = 0;
        // 当前数据行
        int curDataRow = 0;

 		// 数据查询长度
 		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
 		// 数据抽出次数
 		int currentNum = 0;
 		// 查询开始位置
 		int startNum = 0;
 		// 查询结束位置
 		int endNum = 0;
 		while (true) {
 			// 查询开始位置
 			startNum = dataSize * currentNum + 1;
 			// 查询结束位置
 			endNum = startNum + dataSize - 1;
 			// 数据抽出次数累加
 			currentNum++;
 			// 查询开始位置
 			conditionMap.put(CherryConstants.START, startNum);
 			// 查询结束位置
 			conditionMap.put(CherryConstants.END, endNum);
 			
 			// 取得需要转成Excel文件的数据List
 			List<Map<String, Object>> dataList = fetchDataHandler.getDataList(conditionMap);
			if(dataList != null && !dataList.isEmpty()) {
				try{
					for(int i = 0; i < dataList.size(); i++) {
						// 达到Excel导出最大数据量时停止导出
						if(count == maxCount) {
							break;
						}
						count++;
						Map<String, Object> dataMap = dataList.get(i);
						if(curDataRow == maxRowCount) {
							ws.setName(sheetName+'('+(maxRowCount*curSheetNum+1)+'~'+(maxRowCount*curSheetNum+curDataRow)+')');
							curSheetRow = 0;
							curDataRow = 0;
							curSheetNum = curSheetNum + 1;
						}
						// 当前数据行为0时新建sheet，同时添加标题行
						if(curDataRow == 0) {
							ws = wb.createSheet(String.valueOf(curSheetNum), curSheetNum);
							// 存在文件头部信息的场合，在第一行添加头部信息
							if(header != null && !"".equals(header)) {
								ws.addCell(new Label(0, curSheetRow, header, headerStyle));
								// 合并第一行
								ws.mergeCells(0, curSheetRow, titleRows.length - 1, 0);
								// 设置第一行高度
								ws.setRowView(curSheetRow, 600);
								curSheetRow++;
							}
							// 存在标题行List的场合，设置标题行
							if(titleList != null && !titleList.isEmpty()) {
								for(int j = 0; j < titleList.size(); j++) {
									String[][] titles = titleList.get(j);
									int columnStart = 0;
									for(int y = 0; y < titles.length; y++) {
										// 标题名称
										String titleName = titles[y][0];
										// 合并列数
										int mergeColCount = Integer.parseInt(titles[y][1]);
										// 合并行数
										int mergeRowCount = Integer.parseInt(titles[y][2]);
										int columnEnd = columnStart+mergeColCount-1;
										int rowEnd = curSheetRow+mergeRowCount-1;
										if(titleName != null && !"".equals(titleName)) {
											ws.mergeCells(columnStart, curSheetRow, columnEnd, rowEnd);	
											ws.addCell(new Label(columnStart, curSheetRow, titleName, titleStyle2));	
										}
										columnStart = columnStart+mergeColCount;
									}
									curSheetRow++;
								}
							}
							for(int j = 0; j < titleRows.length; j++) {
								// 设置列宽度
								String columnWidth = titleRows[j][2];
								if(null != columnWidth && !"".equals(columnWidth)){
			                        ws.setColumnView(j, Integer.parseInt(columnWidth));
			                    }
								ws.addCell(new Label(j, curSheetRow, titleRows[j][1], titleStyle));
							}
							curSheetRow++;
						}
						// 添加数据行处理
						for(int j = 0; j < titleRows.length; j++) {
							String data = "";
							String key = titleRows[j][0];
							Object value = dataMap.get(key);
							if(value != null && !"".equals(value.toString())) {
								String codeType = titleRows[j][4];
								if(codeType != null && !"".equals(codeType)) {
									data = code.getVal(codeType, value);
								} else {
									data = value.toString();
								}
							}
							if(data != null && !"".equals(data)) {
								String dataStyle = titleRows[j][3];
								if("float".equals(dataStyle)) {
									ws.addCell(new Number(j, curSheetRow, Double.parseDouble(data), floatStyle));
								} else if("int".equals(dataStyle)) {
									ws.addCell(new Number(j, curSheetRow, Double.parseDouble(data), intStyle));
								} else if("right".equals(dataStyle)){
								    ws.addCell(new Label(j, curSheetRow, data, rightStyle));
								} else {
									ws.addCell(new Label(j, curSheetRow, data, stringStyle));
								}
							} else {
								ws.addCell(new Label(j, curSheetRow, "", stringStyle));
							}
						}
						curSheetRow++;
						curDataRow++;
					}
					// 达到Excel导出最大数据量时停止导出
					if(count == maxCount) {
						break;
					}
					if(dataList.size() < dataSize) {
	 					break;
	 				}
				}finally{
					if(dataList != null){
		        		dataList.clear();
		        		dataList = null;
		        	}
				}
			} else {
				break;
			}
 		}
 		if(ws != null) {
 			ws.setName(sheetName+'('+(maxRowCount*curSheetNum+1)+'~'+(maxRowCount*curSheetNum+curDataRow)+')');
 		}
 		wb.write();
        wb.close();
        
        return byteOut.toByteArray();
	}
	
	/**
	 * 导出excel处理
	 * 
	 * @param map 导出数据
	 * @return 字节数组
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		
		// 标题行格式数组
        String[][] titleRows = (String[][])map.get("titleRows");
        // sheet名称
        String sheetName = (String)map.get("sheetName");
        if(sheetName == null) {
        	sheetName = "";
        }
        // 文件头部信息
        String header = (String)map.get("header");
        // 标题行List
        List<String[][]> titleList = (List)map.get("titleList");
        
        // 取得需要转成Excel文件的数据List
        List<Map<String, Object>> dataList = (List)map.get("dataList");
        
        // 创建工作薄 
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
     // 增加防止内存回收的设置
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setGCDisabled(true);
        WritableWorkbook wb = Workbook.createWorkbook(byteOut, workbookSettings);
        WritableSheet ws = null;
        
        // 字体样式
        WritableFont fontStyle = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        // 浮点数格式
        NumberFormat floatFormat = new NumberFormat("0.00");
        WritableCellFormat floatStyle = new WritableCellFormat(floatFormat);
        floatStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 整数格式
        NumberFormat intFormat = new NumberFormat("0");
        WritableCellFormat intStyle = new WritableCellFormat(intFormat);
        intStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 文本格式
        WritableCellFormat stringStyle = new WritableCellFormat();
        stringStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        // 居右格式
        WritableCellFormat rightStyle = new WritableCellFormat();
        rightStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        rightStyle.setAlignment(jxl.format.Alignment.RIGHT);
        
        // 文件头部信息样式
 		WritableCellFormat headerStyle = new WritableCellFormat();
 		headerStyle.setFont(fontStyle);
 		headerStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
 		headerStyle.setAlignment(jxl.format.Alignment.JUSTIFY);
 		headerStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        
        // 标题行样式
        WritableCellFormat titleStyle = new WritableCellFormat();
        titleStyle.setFont(fontStyle);
        titleStyle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleStyle.setBackground(jxl.format.Colour.GRAY_25);
        
        // 标题行样式
        WritableCellFormat titleStyle2 = new WritableCellFormat();
        titleStyle2.setFont(fontStyle);
        titleStyle2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleStyle2.setAlignment(jxl.format.Alignment.CENTRE);
        titleStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleStyle2.setBackground(jxl.format.Colour.GRAY_25);
        
        // 每Sheet页最多显示行数，读系统配置项，取不到使用默认值
		String organizationInfoID = String.valueOf(map.get("organizationInfoId"));
		String brandInfoID = String.valueOf(map.get("brandInfoId"));
		String maxRowCountS = binOLCM14_BL.getConfigValue("1031", organizationInfoID, brandInfoID);
		if (null == maxRowCountS || "".equals(maxRowCountS)) {
			maxRowCountS = CherryConstants.DEFAULT_EXCEL_MAXROW;
		}
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		// 导出数据总数
		int count = 0;
        // 每sheet最大数据行
		int maxRowCount = Integer.parseInt(maxRowCountS);
        // 当前sheet行
        int curSheetRow = 0;
        // 当前sheet页
        int curSheetNum = 0;
        // 当前数据行
        int curDataRow = 0;
 		
 		if(dataList != null && !dataList.isEmpty()) {
 			try{
				for(int i = 0; i < dataList.size(); i++) {
					// 达到Excel导出最大数据量时停止导出
					if(count == maxCount) {
						break;
					}
					count++;
					Map<String, Object> dataMap = dataList.get(i);
					if(curDataRow == maxRowCount) {
						ws.setName(sheetName+'('+(maxRowCount*curSheetNum+1)+'~'+(maxRowCount*curSheetNum+curDataRow)+')');
						curSheetRow = 0;
						curDataRow = 0;
						curSheetNum = curSheetNum + 1;
					}
					// 当前数据行为0时新建sheet，同时添加标题行
					if(curDataRow == 0) {
						ws = wb.createSheet(String.valueOf(curSheetNum), curSheetNum);
						// 存在文件头部信息的场合，在第一行添加头部信息
						if(header != null && !"".equals(header)) {
							ws.addCell(new Label(0, curSheetRow, header, headerStyle));
							// 合并第一行
							ws.mergeCells(0, curSheetRow, titleRows.length - 1, 0);
							// 设置第一行高度
							ws.setRowView(curSheetRow, 600);
							curSheetRow++;
						}
						// 存在标题行List的场合，设置标题行
						if(titleList != null && !titleList.isEmpty()) {
							for(int j = 0; j < titleList.size(); j++) {
								String[][] titles = titleList.get(j);
								int columnStart = 0;
								for(int y = 0; y < titles.length; y++) {
									// 标题名称
									String titleName = titles[y][0];
									// 合并列数
									int mergeColCount = Integer.parseInt(titles[y][1]);
									// 合并行数
									int mergeRowCount = Integer.parseInt(titles[y][2]);
									int columnEnd = columnStart+mergeColCount-1;
									int rowEnd = curSheetRow+mergeRowCount-1;
									if(titleName != null && !"".equals(titleName)) {
										ws.mergeCells(columnStart, curSheetRow, columnEnd, rowEnd);	
										ws.addCell(new Label(columnStart, curSheetRow, titleName, titleStyle2));	
									}
									columnStart = columnStart+mergeColCount;
								}
								curSheetRow++;
							}
						}
						for(int j = 0; j < titleRows.length; j++) {
							// 设置列宽度
							String columnWidth = titleRows[j][2];
							if(null != columnWidth && !"".equals(columnWidth)){
		                        ws.setColumnView(j, Integer.parseInt(columnWidth));
		                    }
						}
						
					}
					// 添加数据行处理
					for(int j = 0; j < titleRows.length; j++) {
						String data = "";
						String key = titleRows[j][0];
						Object value = dataMap.get(key);
						if(value != null && !"".equals(value.toString())) {
							String codeType = titleRows[j][4];
							if(codeType != null && !"".equals(codeType)) {
								data = code.getVal(codeType, value);
							} else {
								data = value.toString();
							}
						}
						if(data != null && !"".equals(data)) {
							String dataStyle = titleRows[j][3];
							if("float".equals(dataStyle)) {
								ws.addCell(new Number(j, curSheetRow, Double.parseDouble(data), floatStyle));
							} else if("int".equals(dataStyle)) {
								ws.addCell(new Number(j, curSheetRow, Double.parseDouble(data), intStyle));
							} else if("right".equals(dataStyle)){
							    ws.addCell(new Label(j, curSheetRow, data, rightStyle));
							} else {
								ws.addCell(new Label(j, curSheetRow, data, stringStyle));
							}
						} else {
							ws.addCell(new Label(j, curSheetRow, "", stringStyle));
						}
					}
					curSheetRow++;
					curDataRow++;
				}
 			}finally{
 				if(dataList != null){
 	        		dataList.clear();
 	        		dataList = null;
 	        	}
 			}
		}
 		
 		if(ws != null) {
 			ws.setName(sheetName+'('+(maxRowCount*curSheetNum+1)+'~'+(maxRowCount*curSheetNum+curDataRow)+')');
 		}
 		wb.write();
        wb.close();
        
        return byteOut.toByteArray();
	}
	
	/**
	 * 导出CSV处理
	 * 
	 * @param map 查询条件
	 * @param fetchDataHandler 查询数据处理器
	 */
	public boolean exportCSV(Map<String, Object> map, BINOLCM37_IF fetchDataHandler) throws Exception {
		
		// 标题行格式数组
        String[][] titleRows = (String[][])map.get("titleRows");
        
		// 查询条件
        Map<String, Object> conditionMap = (Map)map.get("conditionMap");
        if(conditionMap == null || conditionMap.isEmpty()) {
        	conditionMap = map;
        }
        // 临时文件路径
		String tempFilePath = (String)map.get("tempFilePath");
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists()) {
			tempFile.mkdirs();
		}
		// 临时文件名
		String tempFileName = (String)map.get("tempFileName");
		
		// 临时文件全名
		String allFileName = tempFilePath+File.separator+tempFileName+".csv";
		File file = new File(allFileName);
		if(file.exists()) {
			file.delete();
		}
		String charset = (String)map.get("charset");
		if(charset == null || "".equals(charset)) {
			charset = "GBK";
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allFileName), charset));
		
		try {
			// 文件头部信息
	        String header = (String)map.get("header");
	        if(header != null && !"".equals(header)) {
	        	bw.write(header.replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", "")+"\r\n");
	        	bw.flush();
	        }
			
			StringBuffer str = new StringBuffer();
			// 写标题行处理
			for(int j = 0; j < titleRows.length; j++) {
				str.append(titleRows[j][1].replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", ""));
				if(j == titleRows.length-1) {
					str.append("\r\n");
				} else {
					str.append(",");
				}
			}
			bw.write(str.toString());
			bw.flush();
			
			// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			// 导出数据总数
			int count = 0;
			// 数据查询长度
	 		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
	 		// 数据抽出次数
	 		int currentNum = 0;
	 		// 查询开始位置
	 		int startNum = 0;
	 		// 查询结束位置
	 		int endNum = 0;
	 		while (true) {
	 			// 查询开始位置
	 			startNum = dataSize * currentNum + 1;
	 			// 查询结束位置
	 			endNum = startNum + dataSize - 1;
	 			// 数据抽出次数累加
	 			currentNum++;
	 			// 查询开始位置
	 			conditionMap.put(CherryConstants.START, startNum);
	 			// 查询结束位置
	 			conditionMap.put(CherryConstants.END, endNum);
	 			
	 			// 取得需要转成Excel文件的数据List
	 			List<Map<String, Object>> dataList = fetchDataHandler.getDataList(conditionMap);
	 			if(dataList != null && !dataList.isEmpty()) {
	 				try{
		 				for(int i = 0; i < dataList.size(); i++) {
		 					// 达到CSV导出最大数据量时停止导出
		 					if(count == maxCount) {
		 						break;
		 					}
		 					count++;
							Map<String, Object> dataMap = dataList.get(i);
							str.setLength(0);
							// 添加数据行处理
							for(int j = 0; j < titleRows.length; j++) {
								String data = "";
								String key = titleRows[j][0];
								Object value = dataMap.get(key);
								if(value != null && !"".equals(value.toString())) {
									String codeType = titleRows[j][4];
									if(codeType != null && !"".equals(codeType)) {
										data = code.getVal(codeType, value);
									} else {
										data = value.toString();
									}
								}
								if(data != null) {
									data = data.replaceAll(",", "，").replaceAll("\r", "").replaceAll("\n", "");
								}
								str.append(data);
								if(j == titleRows.length-1) {
									str.append("\r\n");
								} else {
									str.append(",");
								}
							}
							bw.write(str.toString());
						}
		 				bw.flush();
		 				// 达到CSV导出最大数据量时停止导出
	 					if(count == maxCount) {
	 						break;
	 					}
		 				if(dataList.size() < dataSize) {
		 					break;
		 				}
	 				}finally{
	 					if(dataList != null){
	 		        		dataList.clear();
	 		        		dataList = null;
	 		        	}
	 				}
//	 				dataList.clear();
	 			} else {
	 				break;
	 			}
	 		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			bw.close();
		}
		return true;
	}
	
	/**
	 * 压缩文件处理
	 * 
	 * @param byteArray 待压缩的字节数组
	 * @param fileName 待压缩的文件名
	 * @return 压缩后字节数组
	 */
	public byte[] fileCompression(byte[] byteArray, String fileName) throws Exception {
		
		InputStream byteIn = new ByteArrayInputStream(byteArray);
        ByteArrayOutputStream zipByteOut = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(zipByteOut); 
        zipOut.setEncoding("utf-8");
        zipOut.putNextEntry(new ZipEntry(fileName)); 
        int b; 
        while ((b = byteIn.read()) != -1) {
        	zipOut.write(b); 
        }
        byteIn.close();
        zipOut.flush(); 
        zipOut.close(); 
        return zipByteOut.toByteArray();
	}
	
	/**
	 * 压缩文件处理
	 * 
	 * @param file 待压缩的文件
	 * @param fileName 压缩包文件名
	 */
	public boolean fileCompression(File file, String fileName) throws Exception {
		
		BufferedInputStream bis = null;
		ZipOutputStream zos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file.getParent()+File.separator+fileName)));
			zos.setEncoding("utf-8");
			zos.putNextEntry(new ZipEntry(file.getName()));
			byte[] buf = new byte[1024];
			int len;
			while((len=bis.read(buf))!=-1) {
				zos.write(buf,0,len);
				zos.flush();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			if(bis != null) {
				bis.close();
			}
			if(zos != null) {
				zos.close();
			}
			try {
				file.delete();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return true;
	}
	
	/**
     * 取得资源的值
     * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
     * @param language 语言
     * @param key 资源的键
     */
    public String getResourceValue(String baseName, String language, String key) {
        try{
            //为空时，查询共通语言资源文件
            String path = "i18n/common/commonText";
            if(null != baseName && !"".equals(baseName)){
            	String sysName = baseName.substring(5, 7);
                //Linux下大小写敏感，传入资源的文件名称全是大写，截取的系统名也是大写
                //但是实际文件夹的名称是小写，这样就取不到目标文件
                //为了在兼容Linux，这里把截取后的系统名转成小写。
                sysName = sysName.toLowerCase();
                path = "i18n/"+sysName+"/"+baseName;
            }
            return LocalizedTextUtil.findResourceBundle(path, new Locale(language.substring(0, 2),language.substring(3,5))).getString(key);
        }catch(Exception e){
        	logger.error(e.getMessage(), e);
        }
        return key;
    }

}
