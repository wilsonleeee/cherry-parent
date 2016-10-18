package com.cherry.ss.prm.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF.ExcelParam;


public class BINOLSSPRM25_Action_TEST extends CherryJunitBase{
	private InputStream excelStream;
    private BINOLSSPRM25_Action action;
    private BINOLMOCOM01_IF bl;
    private void setUpExportTakingInfo() throws Exception {
    	String caseName = "testExportTakingInfo";
        action = createAction(BINOLSSPRM25_Action.class, "/ss", "BINOLSSPRM25_exportTakingInfo");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        DataUtil.getForm(this.getClass(), caseName, action.getModel());
        action.setSession(session);
    }
	@Test
    @Rollback
    public void testExportTakingInfo() throws Exception{
		 bl = applicationContext.getBean(BINOLMOCOM01_IF.class);
	 	setUpExportTakingInfo();
        Assert.assertEquals("success", proxy.execute());
        excelStream = action.getExcelStream();
      //Excel一览导出列数组
        ExcelParam ep = new ExcelParam();
        String array[][] = {
		// 1
		{ "stockTakingNo", "PRM25_stockTakingNo", "30", "", "" },
		// 2
		{ "departName", "PRM25_departName", "30", "", "" },
		// 3
		{ "inventoryName", "PRM25_inventName", "25", "", "" },
		// 4
		{ "sumrealQuantity", "PRM25_realQuantity", "12", "right", "" },
		// 5
		{ "summQuantity", "PRM25_gainQuantity", "12", "right", "" },
		// 6
		{ "summAmount", "PRM25_summAmount", "20", "right", "" },
		// 7
		{ "takingType", "PRM25_takingType", "12", "", "" },
		// 7
		{ "stockTakingDate", "PRM25_stockTakingDate", "12", "", "" },
		// 8
		{ "employeeName", "PRM25_employeeName", "12", "", "" }
		};
        ep.setArray(array);
        ep.setBaseName("BINOLSSPRM25");
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        Map<String,Object> dataMap = new HashMap<String,Object>();
        //导出一行数据
        dataMap.put("stockTakingNo","CA010031202210000074");
        dataMap.put("departName","(mgp)毛戈平品牌");
        dataMap.put("inventoryName","毛戈平品牌默认仓库");
        dataMap.put("sumrealQuantity","1111");
        dataMap.put("summQuantity","889");
        dataMap.put("summAmount","98777777777785.6800");
        dataMap.put("takingType", "F3");
        dataMap.put("stockTakingDate","2012-03-20");
        dataMap.put("employeeName","品牌老大");
        data.add(dataMap);
        ep.setDataList(data);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(CherryConstants.SESSION_LANGUAGE, "zh_CN");
        ep.setMap(paramMap);
        String searchCondition = "CA010031202210000074 ";
        ep.setSearchCondition(searchCondition);
        ep.setSheetLabel("");
        byte[] b = bl.getExportExcel(ep);
        // 创建工作薄 
		Workbook wwb = null;
		try{
		    InputStream is =new ByteArrayInputStream(b);
			wwb = Workbook.getWorkbook(is); 
		}catch (Exception e) {
			e.printStackTrace();
		}
		Sheet[] sheets =  wwb.getSheets();
        for(int i= 0 ; i < sheets.length; i++){
        	 int dataRow = sheets[i].getRows();
        	 String sheetName = "第"+(i*65000+1)+"～"+(dataRow-2)+"条";
             assertEquals(sheetName,sheets[i].getName());
             assertEquals(9,sheets[i].getColumns());
             assertEquals(3,sheets[i].getRows());
             assertEquals("CA010031202210000074 ", sheets[i].getCell(0, 0).getContents());
             assertEquals("CA010031202210000074", sheets[i].getCell(0, 2).getContents());
        }
       
	    }
	  private void setUpExport() throws Exception {
	    	String caseName = "testExport";
	        action = createAction(BINOLSSPRM25_Action.class, "/ss", "BINOLSSPRM25_export");
	        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
	        setSession(CherryConstants.SESSION_USERINFO, userInfo);
	        String language = userInfo.getLanguage();
	        setSession(CherryConstants.SESSION_LANGUAGE,language);
	        DataUtil.getForm(this.getClass(), caseName, action.getModel());
	        action.setSession(session);
	    }
		@Test
	    @Rollback
	    public void testExport() throws Exception{
			 bl = applicationContext.getBean(BINOLMOCOM01_IF.class);
		 	setUpExport();
	        Assert.assertEquals("success", proxy.execute());
	        excelStream = action.getExcelStream();
	      //Excel一览导出列数组
	        ExcelParam ep = new ExcelParam();
	        String array[][] = {
	        		// 1
	    			{ "regionName", "PRM26_regionName", "6", "", "" },
	    			// 2
	    			{ "cityName", "PRM26_cityName", "8", "", "" },
	    			// 3
	    			{ "stockTakingNoIF", "PRM26_stockTakingNoIF", "30", "", "" },
	    			// 4
	    			{ "departName", "PRM26_departName", "15", "", "" },
	    			// 5
	    			{ "depotName", "PRM26_inventName", "25", "", "" },
	    			// 6
	    			{ "nameTotal", "PRM26_nameTotal", "25", "", "" },
	    			// 7
	    			{ "unitCode", "PRM26_unitCode", "20", "", "" },
	    			// 8 
	    			{ "barCode", "PRM26_barCode", "20", "", "" }
	    			};
	        ep.setArray(array);
	        ep.setBaseName("BINOLSSPRM26");
	        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	        Map<String,Object> dataMap = new HashMap<String,Object>();
	        //导出一行数据
	        dataMap.put("regionName", "上海");
	        dataMap.put("cityName", "上海市");
	        dataMap.put("stockTakingNoIF","CA010031202210000074");
	        dataMap.put("departName","上海秉坤测试部门");
	        dataMap.put("depotName","上海秉坤默认仓库");
	        dataMap.put("nameTotal","促销品001");
	        dataMap.put("unitCode","AB0001");
	        dataMap.put("barCode","AB0001");
	        data.add(dataMap);
	        ep.setDataList(data);
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put(CherryConstants.SESSION_LANGUAGE, "zh_CN");
	        ep.setMap(paramMap);
	        String searchCondition = "CA010031202210000074";
	        ep.setSearchCondition(searchCondition);
	        ep.setSheetLabel("");
	        byte[] b = bl.getExportExcel(ep);
	        // 创建工作薄 
			Workbook wwb = null;
			try{
			    InputStream is =new ByteArrayInputStream(b);
				wwb = Workbook.getWorkbook(is); 
			}catch (Exception e) {
				e.printStackTrace();
			}
			Sheet[] sheets =  wwb.getSheets();
	        for(int i= 0 ; i < sheets.length; i++){
	        	 int dataRow = sheets[i].getRows();
	        	 String sheetName = "第"+(i*65000+1)+"～"+(dataRow-2)+"条";
	             assertEquals(sheetName,sheets[i].getName());
	             assertEquals(8,sheets[i].getColumns());
	             assertEquals(3,sheets[i].getRows());
	             assertEquals("CA010031202210000074", sheets[i].getCell(0, 0).getContents());
	             assertEquals("上海", sheets[i].getCell(0, 2).getContents());
	        }
	       
		    }

	 }
	 
