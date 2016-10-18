package com.cherry.mo.common.interfaces;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF.ExcelParam;
import com.cherry.mo.wat.action.BINOLMOWAT01_Action;

public class BINOLMOCOM01_IF_TEST extends CherryJunitBase{
    private BINOLMOWAT01_Action action;
    private BINOLMOCOM01_IF bl;
    private TESTCOM_Service testCOM_Service;

    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetResourceValue1() throws Exception{
        String caseName = "testGetResourceValue1";
        bl = applicationContext.getBean(BINOLMOCOM01_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        for(int index =0;index<dataList.size();index++){
            String baseName = ConvertUtil.getString(dataList.get(index).get("baseName"));
            String language = ConvertUtil.getString(dataList.get(index).get("language"));
            String key = ConvertUtil.getString(dataList.get(index).get("key"));
            String expected = ConvertUtil.getString(dataList.get(index).get("expected"));
            String value = bl.getResourceValue(baseName,language,key);
            assertEquals(expected,value);
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetExportExcel1() throws Exception{
        String caseName = "testGetExportExcel1";
        action = createAction(BINOLMOWAT01_Action.class, "/mo", "BINOLMOWAT01_export");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        action.setSession(session);
        bl = applicationContext.getBean(BINOLMOCOM01_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        
        ExcelParam ep = new ExcelParam();
        String array[][] = {
                { "machineCode", "WAT01_machineCode", "16", "", "" },
                { "connStatus", "WAT01_connStatus", "", "", "1122" },
                { "number", "测试1", "", "number", "" },
                { "right", "WAT01_connStatus", "", "right", "" },
        };
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT01");
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "AAA");
        dataMap.put("connStatus","1");
        dataMap.put("number","1");
        dataMap.put("right","right");
        data.add(dataMap);
        
        dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "BBB");
        dataMap.put("connStatus","2");
        dataMap.put("number","111");
        dataMap.put("right","right");
        
        data.add(dataMap);
        ep.setDataList(data);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(CherryConstants.SESSION_LANGUAGE, "zh_CN");
        ep.setMap(paramMap);
        String searchCondition = "测试A\nc测试B";
        ep.setSearchCondition(searchCondition);
        
        ep.setSheetLabel("测试用例Sheet");
        
        String[][] totalArr = {
                {"B","连接正常","1"},//连接正常
                {"B","失去连接","2"},//失去连接
                {"B","机器停用","3"}//机器停用
            };
        ep.setTotalArr(totalArr);
        byte[] b = bl.getExportExcel(ep);
        try {
            InputStream is =new ByteArrayInputStream(b);
            Workbook wb = null;
            wb = Workbook.getWorkbook(is);
            if (null != wb) {
                Sheet[] sheets = wb.getSheets();
                Sheet dataSheet = null;
                dataSheet = sheets[0];
                assertEquals("测试用例Sheet",dataSheet.getName());
                assertEquals(4,dataSheet.getColumns());
                assertEquals(searchCondition,dataSheet.getCell(0, 0).getContents());
                assertEquals("机器编号",dataSheet.getCell(0, 1).getContents());
                assertEquals("连接状态",dataSheet.getCell(1, 1).getContents());
                assertEquals("测试1",dataSheet.getCell(2, 1).getContents());
                assertEquals("连接状态",dataSheet.getCell(3, 1).getContents());
                
                assertEquals("AAA",dataSheet.getCell(0, 2).getContents());
//                assertEquals("连接正常",dataSheet.getCell(1, 2).getContents());
                assertEquals("1",dataSheet.getCell(2, 2).getContents());
                assertEquals("right",dataSheet.getCell(3, 2).getContents());
                
                assertEquals("合计",dataSheet.getCell(0, 6).getContents());
                assertEquals("连接正常",dataSheet.getCell(1, 6).getContents());
                assertEquals("失去连接",dataSheet.getCell(2, 6).getContents());
                assertEquals("机器停用",dataSheet.getCell(3, 6).getContents());
            }
            
//            //输出文件
//            FileOutputStream out=new FileOutputStream(this.getClass().getSimpleName()+".xls");
//            out.write(b);
//            out.close();
        }catch(Exception e){
            fail("导出Excel错误！");
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional
    public void testGetExportExcel2() throws Exception{
        String caseName = "testGetExportExcel2";
        testCOM_Service = (TESTCOM_Service) applicationContext.getBean("TESTCOM_Service");
        String sql = "UPDATE Tools.BIN_SystemConfig SET ConfigValue = '3' WHERE ConfigCode = '1031'";
        testCOM_Service.update(sql);
        
        action = createAction(BINOLMOWAT01_Action.class, "/mo", "BINOLMOWAT01_export");
        UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),caseName);
        setSession(CherryConstants.SESSION_USERINFO, userInfo);
        String language = userInfo.getLanguage();
        setSession(CherryConstants.SESSION_LANGUAGE,language);
        action.setSession(session);
        bl = applicationContext.getBean(BINOLMOCOM01_IF.class);
        List<Map<String,Object>> dataList = DataUtil.getDataList(this.getClass(),caseName);
        
        ExcelParam ep = new ExcelParam();
        String array[][] = {
                { "machineCode", "WAT01_machineCode", "16", "", "" },
                { "connStatus", "WAT01_connStatus", "", "", "1122" },
                { "number", "测试1", "", "number", "" },
                { "right", "WAT01_connStatus", "", "right", "" },
        };
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT01");
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "AAA");
        dataMap.put("connStatus","1");
        dataMap.put("number","1");
        dataMap.put("right","right");
        data.add(dataMap);
        
        dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "BBB");
        dataMap.put("connStatus","2");
        dataMap.put("number","111");
        dataMap.put("right","right");
        data.add(dataMap);
        
        dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "CCC");
        dataMap.put("connStatus","2");
        dataMap.put("number","111");
        dataMap.put("right","right");
        data.add(dataMap);
        
        dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "DDD");
        dataMap.put("connStatus","2");
        dataMap.put("number","111");
        dataMap.put("right","right");
        data.add(dataMap);
        
        dataMap = new HashMap<String,Object>();
        dataMap.put("machineCode", "EEE");
        dataMap.put("connStatus","2");
        dataMap.put("number","111");
        dataMap.put("right","right");
        data.add(dataMap);
        
        ep.setDataList(data);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(CherryConstants.SESSION_LANGUAGE, "zh_CN");
        ep.setMap(paramMap);
        String searchCondition = "测试A\nc测试B";
        ep.setSearchCondition(searchCondition);
        
        ep.setSheetLabel("测试用例Sheet");
        
        String[][] totalArr = {
                {"B","连接正常","1"},//连接正常
                {"B","失去连接","2"},//失去连接
                {"B","机器停用","3"}//机器停用
            };
        ep.setTotalArr(totalArr);
        byte[] b = bl.getExportExcel(ep);
        try {
            InputStream is =new ByteArrayInputStream(b);
            Workbook wb = null;
            wb = Workbook.getWorkbook(is);
            if (null != wb) {
                Sheet[] sheets = wb.getSheets();
                Sheet dataSheet = null;
                assertEquals(2,sheets.length);
                
                dataSheet = sheets[0];
                assertEquals("第1～3条",dataSheet.getName());
                
                dataSheet = sheets[1];
                assertEquals("第4～5条",dataSheet.getName());
            }
            
//            //输出文件
//            FileOutputStream out=new FileOutputStream(this.getClass().getSimpleName()+".xls");
//            out.write(b);
//            out.close();
        }catch(Exception e){
            fail("导出Excel错误！");
        }
    }
}
