/*
 * @(#)BINOLMOWAT03_BL.java     1.0 2011/6/27
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
package com.cherry.mo.common.bl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.common.service.BINOLMOCOM01_Service;
import com.cherry.synchro.mo.bl.PosSystemConfigSynchro;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 
 * Monitor共通BL
 * 
 * @author niushunjie
 * @version 1.0 2011.6.27
 */
public class BINOLMOCOM01_BL implements BINOLMOCOM01_IF {

    @Resource
    private CodeTable code;
    @Resource
    private BINOLMOCOM01_Service binOLMOCOM01_Service;
    
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource
	private BaseServiceImpl baseServiceImpl;
    
    @Resource(name="posSystemConfigSynchro")
	private PosSystemConfigSynchro posSystemConfigSynchro;
    
    @Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
    
    /**
     * 取得资源的值
     * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
     * @param language 语言
     * @param key 资源的键
     */
    @Override
    public String getResourceValue(String baseName, String language, String key) {
        try{
            String sysName = "";
            //为空时，查询共通语言资源文件
            String path = "i18n/common/commonText";
            if(null != baseName && !"".equals(baseName)){
            	if(baseName.equals("message")){
            		path = "i18n/message/message";
            	} else {
            		sysName = baseName.substring(5, 7);
            		//Linux下大小写敏感，传入资源的文件名称全是大写，截取的系统名也是大写
            		//但是实际文件夹的名称是小写，这样就取不到目标文件
            		//为了在兼容Linux，这里把截取后的系统名转成小写。
            		sysName = sysName.toLowerCase();
            		path = "i18n/"+sysName+"/"+baseName;
            	}
            }
            return LocalizedTextUtil.findResourceBundle(path, new Locale(language.substring(0, 2),language.substring(3,5))).getString(key);
        }catch(Exception e){
            return key;
        }
    }
    
    /**
     * 共通Excel导出
     * @param ep Excel参数
     * @throws Exception
     */
    @Override
    public byte[] getExportExcel(ExcelParam ep)
            throws Exception {
    	List<Map<String, Object>> dataList = ep.getDataList();
        try {
            Map<String,Object> map = ep.getMap();
            String[][] array = ep.getArray();
            String baseName = ep.getBaseName();
            String sheetLabel = ep.getSheetLabel();
            String[][] totalArr = ep.getTotalArr();
            String searchCondition = ep.getSearchCondition();
            boolean showDiffColor = ep.isShowDiffColor();
            boolean showBorder = ep.isShowBorder();
            //标题是否支持换行（包括背景与垂直居中）
            boolean showTitleStyle = ep.isShowTitleStyle();
            
            String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
            //取得资源的值
            String[] titleArray = new String[array.length];
            String sheetName = ConvertUtil.getString(getResourceValue(baseName,language,sheetLabel));
            HashMap hm = new HashMap();
            for(int i=0;i<array.length;i++){
                titleArray[i] = getResourceValue(baseName,language,array[i][1]);
                hm.put(array[i][0], i);
            }
            
            // 创建工作薄 
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            // 增加防止内存回收的设置
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);
            WritableWorkbook wwb = Workbook.createWorkbook(byteOut, workbookSettings);
            
            //每Sheet页最多显示行数，读系统配置项，取不到使用默认值
            String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoId"));
            String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
            String configValue = binOLCM14_BL.getConfigValue("1031", organizationInfoID, brandInfoID);
            if(null == configValue || "".equals(configValue)){
                configValue = CherryConstants.DEFAULT_EXCEL_MAXROW;
            }
            int perSheetRow = CherryUtil.obj2int(configValue);
            int totalSheetCount = dataList.size() / perSheetRow;
            if(dataList.size() % perSheetRow > 0){
                totalSheetCount = dataList.size() / perSheetRow+1;
            }
            /**
             * 保证在即使没有明细的情况下也能正常导出【只是没有明细数据，但是有标题栏】
             */
            int curSheetNum=0;
            while(true){
                int row = 1;//记录行数 
                //int sheetCount = 0;//工作表数
                int formIndex = curSheetNum * perSheetRow;
                int toIndex = dataList.size();
                if(dataList.size() > curSheetNum * perSheetRow + perSheetRow){
                    toIndex = curSheetNum * perSheetRow + perSheetRow;
                }
                if(totalSheetCount > 1 || "".equals(sheetName)){
                    sheetName = getResourceValue("", language,"excel.sheetName_1")
                            + (formIndex + 1)
                            + getResourceValue("", language,"excel.sheetName_2")
                            + toIndex
                            + getResourceValue("", language,"excel.sheetName_3");
                }
                WritableSheet ws = wwb.createSheet(sheetName, curSheetNum);

                //自定义数字格式
                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#0");  
                

                //设置右对齐格式
                jxl.write.WritableFont wf = new jxl.write.WritableFont(WritableFont.createFont("Arial"));
                //奇数行
                jxl.write.WritableCellFormat oddRwcfF = new jxl.write.WritableCellFormat(wf);
                //偶数行
                jxl.write.WritableCellFormat evenRwcfF = new jxl.write.WritableCellFormat(wf);
                oddRwcfF.setAlignment(jxl.format.Alignment.RIGHT);
                evenRwcfF.setAlignment(jxl.format.Alignment.RIGHT);

                //创建单元格格式：设置水平对齐为居中对齐
                jxl.write.WritableCellFormat CwcfF = new jxl.write.WritableCellFormat(wf);
                CwcfF.setAlignment(jxl.format.Alignment.CENTRE);
                
                //设置垂直对齐为居中对齐
                CwcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                
                //字体加粗加边框换行垂直居中
                WritableFont BoldTitleFont = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
                WritableCellFormat BoldBorderCenterTitle = new WritableCellFormat(); 
                BoldBorderCenterTitle.setFont(BoldTitleFont);
                BoldBorderCenterTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                BoldBorderCenterTitle.setAlignment(jxl.format.Alignment.JUSTIFY);
                BoldBorderCenterTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                
                //字体加粗加边框
                WritableCellFormat BoldBorderTitle = new WritableCellFormat(); 
                BoldBorderTitle.setFont(BoldTitleFont);
                BoldBorderTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                if(showTitleStyle){
                	//标题背景
                	BoldBorderTitle.setBackground(jxl.format.Colour.GREY_25_PERCENT);
                	//标题垂直居中
                    BoldBorderTitle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                    //支持换行\n
                    BoldBorderTitle.setAlignment(jxl.format.Alignment.JUSTIFY);
                }
                
                
                //明细行样式（奇数行）
                WritableCellFormat oddBorderTitle = new WritableCellFormat();
                //明细行样式（偶数行）
                WritableCellFormat evenBorderTitle = new WritableCellFormat();
                //明细行数字样式（奇数行）
                jxl.write.WritableCellFormat oddWcf = new jxl.write.WritableCellFormat(nf);
                //明细行数字样式（偶数行）
                jxl.write.WritableCellFormat evenWcf = new jxl.write.WritableCellFormat(nf);
                
                //是否给相邻明细行设置色差
                if(showDiffColor){
                	oddBorderTitle.setBackground(jxl.format.Colour.GREY_25_PERCENT);
                    oddWcf.setBackground(jxl.format.Colour.GREY_25_PERCENT);
                    oddRwcfF.setBackground(jxl.format.Colour.GREY_25_PERCENT);
                }
                //是否显示边框
                if(showBorder){
                	oddBorderTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                	evenBorderTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                	oddWcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                	evenWcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                	oddRwcfF.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                	evenRwcfF.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
                }
                
                //设置列宽
                for(int i=0;i<array.length;i++){
                    if(null != array[i][2] && !"".equals(array[i][2])){
                        ws.setColumnView(i, Integer.parseInt(array[i][2]));
                    }
                }
                
                //写入显示的查询条件
                if(null != searchCondition && !"".equals(searchCondition)){
                    ws.addCell(new jxl.write.Label (0, 0, searchCondition,BoldBorderCenterTitle));
                    //合并第一行
                    ws.mergeCells(0,0,titleArray.length-1,0);
                    //设置第一行高度
                    ws.setRowView(0, 600);
                    row ++;
                }
                
                //写入标题
                for(int i=0;i<array.length;i++){
                    ws.addCell(new Label(i, row-1, titleArray[i],BoldBorderTitle));
                }
                
                boolean isOdd = false;
                
                //写入数据
                for(Map<String, Object> dataMap:dataList.subList(formIndex, toIndex)){
                	isOdd = row%2==0? true:false;
                    for(Map.Entry<String,Object> en:dataMap.entrySet()){
                        if(hm.containsKey(en.getKey())){
                            if(null != en.getValue() && !"".equals(en.getValue())){
                                int index = Integer.parseInt(ConvertUtil.getString(hm.get(en.getKey())));
                                String value = ConvertUtil.getString(en.getValue());
                                if(null != array[index][4] && !"".equals(array[index][4])){
                                    String codeTableKey = array[index][4];
                                    ws.addCell(new Label(index,row,code.getVal(codeTableKey,value),isOdd? evenBorderTitle:oddBorderTitle));
                                }else if(null != array[index][3] && "number".equals(array[index][3])){
                                    jxl.write.Number nb = new jxl.write.Number(index,row,Double.parseDouble(value),isOdd? evenWcf:oddWcf);   
                                    ws.addCell(nb);                                
                                }else if(null != array[index][3] && "right".equals(array[index][3])){
                                    Label labelRight = new Label(index,row,value,isOdd? evenBorderTitle:oddBorderTitle);
                                    labelRight.setCellFormat(isOdd? evenRwcfF:oddRwcfF);
                                    ws.addCell(labelRight);
                                }else{
                                    ws.addCell(new Label(index,row,value,isOdd? evenBorderTitle:oddBorderTitle));
                                }
                            }else{
                            	int index = Integer.parseInt(ConvertUtil.getString(hm.get(en.getKey())));
                            	ws.addCell(new Label(index,row,"",isOdd? evenBorderTitle:oddBorderTitle));
                            }
                        }
                    }
                    row++;
                }
                
                //合计信息
                if(null != totalArr && totalArr.length>0){
                    int dataCount = row;//数据到第几行
                    int totalRow = row+2;//合计显示在表格的下两行
                    int totalCountRow = totalRow+1;//合计数显示在合计的下一行
                    //合并单元格
                    ws.mergeCells(0,totalRow,0,totalCountRow);
                    ws.addCell(new Label(0,totalRow,getResourceValue(baseName,language,"labelTotal"),CwcfF));

                    for(int i=0;i<totalArr.length;i++){
                        ws.addCell(new Label(Integer.parseInt(totalArr[i][2]),totalRow,totalArr[i][1]));
                        Formula f = new Formula(Integer.parseInt(totalArr[i][2]),totalCountRow,"COUNTIF("+totalArr[i][0]+"2:"+totalArr[i][0]+dataCount+",\""+totalArr[i][1]+"\")");
                        ws.addCell(f);
                    }
                }
                curSheetNum++;
                if(curSheetNum>=totalSheetCount) {
                	break;
                }
            }
            
            wwb.write();
            wwb.close();
            return  byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (RowsExceededException e) {
            e.printStackTrace();
            throw e;
        } catch (WriteException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally{
        	if(dataList != null){
        		dataList.clear();
        		dataList = null;
        	}
        }
    }
    
	/**
	 * Excel批量导出共通（数据库分页与List分批导出）
	 * 
	 * @param ep
	 *            Excel 参数
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getBatchExportExcel(ExcelParam ep) throws Exception {
		try {
			Map<String, Object> map = ep.getMap();//批量查询参数
			String[][] array = ep.getArray();
			String baseName = ep.getBaseName();
			String sheetLabel = ep.getSheetLabel();
			String[][] totalArr = ep.getTotalArr();
			String searchCondition = ep.getSearchCondition();
			boolean showDiffColor = ep.isShowDiffColor();
			boolean showBorder = ep.isShowBorder();
			// 标题是否支持换行（包括背景与垂直居中）
			boolean showTitleStyle = ep.isShowTitleStyle();

			String language = ConvertUtil.getString(map
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得资源的值
			String[] titleArray = new String[array.length];
			String sheetName = ConvertUtil.getString(getResourceValue(baseName,
					language, sheetLabel));
			Map<String, Object> hm = new HashMap<String, Object>();
			for (int i = 0; i < array.length; i++) {
				titleArray[i] = getResourceValue(baseName, language,
						array[i][1]);
				hm.put(array[i][0], i);
			}

			// 创建工作薄
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			// 增加防止内存回收的设置
	        WorkbookSettings workbookSettings = new WorkbookSettings();
	        workbookSettings.setGCDisabled(true);
	        WritableWorkbook wwb = Workbook.createWorkbook(byteOut, workbookSettings);

			// 每Sheet页最多显示行数，读系统配置项，取不到使用默认值
			String organizationInfoID = ConvertUtil.getString(map
					.get("organizationInfoId"));
			String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
			String configValue = binOLCM14_BL.getConfigValue("1031",
					organizationInfoID, brandInfoID);
			if (null == configValue || "".equals(configValue)) {
				configValue = CherryConstants.DEFAULT_EXCEL_MAXROW;
			}
			int perSheetRow = CherryUtil.obj2int(configValue);// 每张sheet最多写入数据行数
			// 需导出数据在总数
			int dataLen = map.containsKey("dataLen") ? CherryUtil.obj2int(map
					.get("dataLen")) : 0;
			int totalSheetCount = dataLen % perSheetRow == 0 ? (dataLen / perSheetRow)
					: (dataLen / perSheetRow + 1);// 总页数
			int start = 0;// 数据库分页查询起始位置
			int subSize = 0;// 累计写入excel的数据长度
			int recordRow = 0;// 记录excel行位置
			int curSheetNum = 0;// 当前sheet页
			int subSheetCount = 0;// 累计写入excel的页码数
			int recordSubSheetCount = 1;// 记录前一批数据写完后sheet的页码
			WritableSheet recordWs = null;//记录没写满的sheet
			// 批量处理条数（数据库分布查询条数）
			int batchPageLen = map.containsKey("batchPageLen") ? CherryUtil
					.obj2int(map.get("batchPageLen"))
					: CherryConstants.BATCH_PAGE_MAX_NUM;
			List<Map<String, Object>> batchList = null;//批查询所得数据
			boolean loopFlag = true;
			while (loopFlag) {
				map.put("START", start + 1);
				map.put("END", start + batchPageLen);
				start += batchPageLen;
				//清空上次查询所得List,减少内存消耗
				if(null != batchList) {
					batchList.clear();
					batchList = null;
				}
				//数据库分页查询
				batchList = baseServiceImpl.getList(map);
				if (null != batchList) {
					WritableSheet ws = null;
					subSize += batchList.size();
					curSheetNum = (subSize == batchList.size()) ? 0
							: recordSubSheetCount - 1;
					subSheetCount = subSize % perSheetRow == 0 ? (subSize / perSheetRow)
							: (subSize / perSheetRow + 1);
					if (batchList.size() > 0) {
						int startIndex = 0;// 截取一次批查询数据中所需数据的头下标
						int toIndex = 0;// 截取一次批查询数据中所需数据的尾下标
						for (int curSubNum = 0; curSheetNum < subSheetCount; curSheetNum++, curSubNum++) {
							int row = recordRow + 1;// 记录行数
							int formIndex = curSheetNum * perSheetRow;// 用于创建sheetName
							startIndex = (curSubNum == 0) ? curSubNum
									* perSheetRow : toIndex;
							toIndex = batchList.size();
							// 上一张sheet未写满
							if (recordRow != 0) {
								if (batchList.size() >= curSubNum * perSheetRow
										+ perSheetRow - (recordRow - 1)) {
									toIndex = curSubNum * perSheetRow
											+ perSheetRow - (recordRow - 1);
								}
							} else if (batchList.size() > startIndex
									+ perSheetRow) {
								toIndex = startIndex + perSheetRow;
							}
							if (recordRow == 0) {// 上次的sheet写满才新建sheet
								int totalIndex = dataLen;
								if (dataLen > curSheetNum * perSheetRow
										+ perSheetRow) {
									totalIndex = curSheetNum * perSheetRow
											+ perSheetRow;
								}
								if (totalSheetCount > 1 || "".equals(sheetName)) {
									sheetName = getResourceValue("", language,
											"excel.sheetName_1")
											+ (formIndex + 1)
											+ getResourceValue("", language,
													"excel.sheetName_2")
											+ totalIndex
											+ getResourceValue("", language,
													"excel.sheetName_3");
								}
								ws = wwb.createSheet(sheetName, curSheetNum);
							} else {
								ws = recordWs;
							}
							// 自定义数字格式
							jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
									"#0");
							// 设置右对齐格式
							jxl.write.WritableFont wf = new jxl.write.WritableFont(
									WritableFont.createFont("Arial"));
							// 奇数行
							jxl.write.WritableCellFormat oddRwcfF = new jxl.write.WritableCellFormat(
									wf);
							// 偶数行
							jxl.write.WritableCellFormat evenRwcfF = new jxl.write.WritableCellFormat(
									wf);
							oddRwcfF.setAlignment(jxl.format.Alignment.RIGHT);
							evenRwcfF.setAlignment(jxl.format.Alignment.RIGHT);

							// 创建单元格格式：设置水平对齐为居中对齐
							jxl.write.WritableCellFormat CwcfF = new jxl.write.WritableCellFormat(
									wf);
							CwcfF.setAlignment(jxl.format.Alignment.CENTRE);

							// 设置垂直对齐为居中对齐
							CwcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

							// 字体加粗加边框换行垂直居中
							WritableFont BoldTitleFont = new WritableFont(
									WritableFont.ARIAL, 10, WritableFont.BOLD);
							WritableCellFormat BoldBorderCenterTitle = new WritableCellFormat();
							BoldBorderCenterTitle.setFont(BoldTitleFont);
							BoldBorderCenterTitle.setBorder(
									jxl.format.Border.ALL,
									jxl.format.BorderLineStyle.THIN);
							BoldBorderCenterTitle
									.setAlignment(jxl.format.Alignment.JUSTIFY);
							BoldBorderCenterTitle
									.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

							// 字体加粗加边框
							WritableCellFormat BoldBorderTitle = new WritableCellFormat();
							BoldBorderTitle.setFont(BoldTitleFont);
							BoldBorderTitle.setBorder(jxl.format.Border.ALL,
									jxl.format.BorderLineStyle.THIN);
							if (showTitleStyle) {
								// 标题背景
								BoldBorderTitle
										.setBackground(jxl.format.Colour.GREY_25_PERCENT);
								// 标题垂直居中
								BoldBorderTitle
										.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
								// 支持换行\n
								BoldBorderTitle
										.setAlignment(jxl.format.Alignment.JUSTIFY);
							}

							// 明细行样式（奇数行）
							WritableCellFormat oddBorderTitle = new WritableCellFormat();
							// 明细行样式（偶数行）
							WritableCellFormat evenBorderTitle = new WritableCellFormat();
							// 明细行数字样式（奇数行）
							jxl.write.WritableCellFormat oddWcf = new jxl.write.WritableCellFormat(
									nf);
							// 明细行数字样式（偶数行）
							jxl.write.WritableCellFormat evenWcf = new jxl.write.WritableCellFormat(
									nf);

							// 是否给相邻明细行设置色差
							if (showDiffColor) {
								oddBorderTitle
										.setBackground(jxl.format.Colour.GREY_25_PERCENT);
								oddWcf.setBackground(jxl.format.Colour.GREY_25_PERCENT);
								oddRwcfF.setBackground(jxl.format.Colour.GREY_25_PERCENT);
							}
							// 是否显示边框
							if (showBorder) {
								oddBorderTitle.setBorder(jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
								evenBorderTitle.setBorder(
										jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
								oddWcf.setBorder(jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
								evenWcf.setBorder(jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
								oddRwcfF.setBorder(jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
								evenRwcfF.setBorder(jxl.format.Border.ALL,
										jxl.format.BorderLineStyle.THIN);
							}
							// 设置列宽
							for (int i = 0; i < array.length; i++) {
								if (null != array[i][2]
										&& !"".equals(array[i][2])) {
									ws.setColumnView(i,
											Integer.parseInt(array[i][2]));
								}
							}
							if (ws != recordWs) {//相同内容只写一次
								// 写入显示的查询条件
								if (null != searchCondition
										&& !"".equals(searchCondition)) {
									ws.addCell(new jxl.write.Label(0, 0,
											searchCondition,
											BoldBorderCenterTitle));
									// 合并第一行
									ws.mergeCells(0, 0, titleArray.length - 1,
											0);
									// 设置第一行高度
									ws.setRowView(0, 600);
									row++;
								}

								// 写入标题
								for (int i = 0; i < array.length; i++) {
									ws.addCell(new Label(i, row - 1,
											titleArray[i], BoldBorderTitle));
								}

								// 合计信息
								if (null != totalArr && totalArr.length > 0) {
									int dataCount = perSheetRow + 2;// 数据到第几行
									if (dataLen == subSize
											&& (toIndex - startIndex) < perSheetRow) {// 批数据为最后一批数据且为最后一页
										dataCount = (toIndex - startIndex) + 2;
									}
									int totalRow = dataCount + 2;// 合计显示在表格的下两行
									int totalCountRow = totalRow + 1;// 合计数显示在合计的下一行
									// 合并单元格
									ws.mergeCells(0, totalRow, 0, totalCountRow);
									ws.addCell(new Label(0, totalRow,
											getResourceValue(baseName,
													language, "labelTotal"),
											CwcfF));

									for (int i = 0; i < totalArr.length; i++) {
										ws.addCell(new Label(Integer
												.parseInt(totalArr[i][2]),
												totalRow, totalArr[i][1]));
										Formula f = new Formula(
												Integer.parseInt(totalArr[i][2]),
												totalCountRow, "COUNTIF("
														+ totalArr[i][0] + "2:"
														+ totalArr[i][0]
														+ dataCount + ",\""
														+ totalArr[i][1]
														+ "\")");
										ws.addCell(f);
									}
								}
							}
							
							boolean isOdd = false;
							// 写入数据
							for (Map<String, Object> dataMap : batchList
									.subList(startIndex, toIndex)) {
								isOdd = row % 2 == 0 ? true : false;
								for (Map.Entry<String, Object> en : dataMap
										.entrySet()) {
									if (hm.containsKey(en.getKey())) {
										if (null != en.getValue()
												&& !"".equals(en.getValue())) {
											int index = Integer
													.parseInt(ConvertUtil.getString(hm
															.get(en.getKey())));
											String value = ConvertUtil
													.getString(en.getValue());
											if (null != array[index][4]
													&& !"".equals(array[index][4])) {
												String codeTableKey = array[index][4];
												ws.addCell(new Label(
														index,
														row,
														code.getVal(
																codeTableKey,
																value),
														isOdd ? evenBorderTitle
																: oddBorderTitle));
											} else if (null != array[index][3]
													&& "number"
															.equals(array[index][3])) {
												jxl.write.Number nb = new jxl.write.Number(
														index,
														row,
														Double.parseDouble(value),
														isOdd ? evenWcf
																: oddWcf);
												ws.addCell(nb);
											} else if (null != array[index][3]
													&& "right"
															.equals(array[index][3])) {
												Label labelRight = new Label(
														index,
														row,
														value,
														isOdd ? evenBorderTitle
																: oddBorderTitle);
												labelRight
														.setCellFormat(isOdd ? evenRwcfF
																: oddRwcfF);
												ws.addCell(labelRight);
											} else {
												ws.addCell(new Label(
														index,
														row,
														value,
														isOdd ? evenBorderTitle
																: oddBorderTitle));
											}
										} else {
											int index = Integer
													.parseInt(ConvertUtil.getString(hm
															.get(en.getKey())));
											ws.addCell(new Label(index, row,
													"", isOdd ? evenBorderTitle
															: oddBorderTitle));
										}
									}
								}
								row++;
							}
							if (row < perSheetRow + 2) {
								// 记录未写满的sheet的行位置
								recordRow = row - 1;
								recordWs = ws;
							} else {//当前sheet写满了
								recordRow = 0;
								recordSubSheetCount += 1;
							}
						}
					} else if (batchList.size() < batchPageLen) {// 一次查询的数据量小于批处理单位条数则退出
						loopFlag = false;
					}
				} else {
					loopFlag = false;
				}
			}

			wwb.write();
			wwb.close();
			return byteOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (RowsExceededException e) {
			e.printStackTrace();
			throw e;
		} catch (WriteException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    
    /**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDeparyType(Map<String, Object> map) {
		
		// 根据部门ID取得部门类型
		return binOLMOCOM01_Service.getDeparyType(map);
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别等级
	 * 
	 * @param map 查询条件
	 * @return 岗位类别等级
	 */
	public String getPosCategoryGrade(Map<String, Object> map) {
		
		// 根据岗位类别ID取得岗位类别等级
		return binOLMOCOM01_Service.getPosCategoryGrade(map);
	}
	
	public int getEmployeeCount(Map<String, Object> map) {
		
		// 取得员工总数
		return binOLMOCOM01_Service.getEmployeeCount(map);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		
		// 取得员工List
		return binOLMOCOM01_Service.getEmployeeList(map);
	}

	@Override
	public List<Map<String, Object>> getRegionList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMOCOM01_Service.getRegionList(map);
	}
	
	@Override
	public void tran_updateConfigValue(Map<String, Object> map) throws Exception {
		// 新后台的POS配置项暂时先放着，存在的配置项更新，不存在的新增
		
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		Map<String, Object> synchroMap = new HashMap<String, Object>();
		synchroMap.putAll(map);
		synchroMap.put("BrandCode", brand_code);
		/**
		 * 通过存储过程直接修改终端系统配置项值
		 */
		// 调用存储过程更新相应的POS系统配置项
		posSystemConfigSynchro.updPosSystemConfig(synchroMap);
	}
}
