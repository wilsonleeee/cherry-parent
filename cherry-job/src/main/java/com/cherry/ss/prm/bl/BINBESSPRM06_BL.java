package com.cherry.ss.prm.bl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.service.BINBESSPRM06_Service;
import com.cherry.st.common.service.BINOLSTCM01_Service;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
 

public class BINBESSPRM06_BL {
	
		 /** 失败条数 */
	    private int failCount = 0;
	    
	    /** BATCH处理标志 */
	    private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
		/** 成功条数 */
		private int successCount = 0;

	 	@Resource
	    private CodeTable code;
	 
	    @Resource
	    private BINBESSPRM06_Service binBESSPRM06_Service;
	    
	    @Resource
	    private BINOLSTCM01_Service binOLSTCM01_Service;
	    
	    @Resource
	    private BINOLCM03_BL binOLCM03_BL;
	    
	    private String remark = "平促销品库存补入出库记录";
    
	    public List<Map<String,Object>> getDepotInfoList(Map<String, Object> map) {
	        return binBESSPRM06_Service.getDepotInfoList(map);
	    }
	    
	    public List<Map<String,Object>> getDiffStockList(Map<String, Object> map) {
	        List<Map<String,Object>> productList = new ArrayList<Map<String,Object>>();
	        
	        String departCodeName= ConvertUtil.getString(map.get("departCodeName"));
	        String depotCodeName= ConvertUtil.getString(map.get("depotCodeName"));
	        String logicCodeName= ConvertUtil.getString(map.get("logicCodeName"));
	        String organizationID = ConvertUtil.getString(map.get("BIN_OrganizationID"));
	        String inventoryInfoID = ConvertUtil.getString(map.get("BIN_InventoryInfoID"));
	        String logicInventoryInfoID = ConvertUtil.getString(map.get("BIN_LogicInventoryInfoID"));
	        
	        //结果已按促销品厂商ID排序
	        List<Map<String,Object>> stockList = binBESSPRM06_Service.getStockList(map);
	        List<Map<String,Object>> inOutStockList = binBESSPRM06_Service.getInOutStockList(map);
	        
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put("departCodeName", departCodeName);
	        paramMap.put("depotCodeName", depotCodeName);
	        paramMap.put("logicCodeName", logicCodeName);
	        paramMap.put("organizationID", organizationID);
	        paramMap.put("inventoryInfoID", inventoryInfoID);
	        paramMap.put("logicInventoryInfoID", logicInventoryInfoID);
	        
	        Map<String,Object> returnMap = new HashMap<String,Object>();
	        returnMap.put("TotalQuantity", 0);
	        returnMap.put("DetailNo", 0);
	        
	        //遍历库存List查找入出库List，当两个List的促销品厂商ID一致时，比较数量。入出库List中不存在当前促销品厂商ID时也加入，数量设为0。
	        int startJ =0;
	        for(int i=0;i<stockList.size();i++){
	            Map<String,Object> stockMap = stockList.get(i);
	            String promotionProductVendorID = ConvertUtil.getString(stockMap.get("BIN_PromotionProductVendorID"));
	            String actualStockQuantity = ConvertUtil.getString(stockMap.get("Quantity"));
	            String barCode = ConvertUtil.getString(stockMap.get("BarCode"));
	            String unitCode = ConvertUtil.getString(stockMap.get("UnitCode"));
	            String nameTotal = ConvertUtil.getString(stockMap.get("NameTotal"));
	            
	            paramMap.put("promotionProductVendorID", promotionProductVendorID);
	            paramMap.put("actualStockQuantity", actualStockQuantity);
	            paramMap.put("barCode", barCode);
	            paramMap.put("unitCode", unitCode);
	            paramMap.put("nameTotal", nameTotal);
	                        
	            boolean inOutFindFlag = false;
	            for(int j=startJ;j<inOutStockList.size();j++){
	                Map<String,Object> inOutStockMap = inOutStockList.get(j);
	                String curProductVendorID = ConvertUtil.getString(inOutStockMap.get("BIN_PromotionProductVendorID"));
	                String curQuantity = ConvertUtil.getString(inOutStockMap.get("Quantity"));
	                if(promotionProductVendorID.equals(curProductVendorID)){
	                    inOutFindFlag = true;
	                    if(!actualStockQuantity.equals(curQuantity)){
	                        paramMap.put("endInOutQuantity", curQuantity);
	                        productList.add(setProductInfo(paramMap,returnMap));
	                    }
	                    startJ = j+1;
	                    break;
	                }
	            }
	            if(!inOutFindFlag){
	                //入出库不存在
	                paramMap.put("endInOutQuantity", 0);
	                Map<String,Object> product = setProductInfo(paramMap,returnMap);
	                if(CherryUtil.obj2int(product.get("Quantity")) != 0){
	                    productList.add(product);
	                }
	            }
	        }

	        //遍历入出库List查找库存List，这里不比较两边都存在的促销品，当库存List不存在当前促销品厂商ID时加入，数量设为0。
	        startJ = 0;
	        for(int i=0;i<inOutStockList.size();i++){
	            Map<String,Object> inOutStockMap = inOutStockList.get(i);
	            String productVendorID = ConvertUtil.getString(inOutStockMap.get("BIN_PromotionProductVendorID"));
	            String endInOutQuantity = ConvertUtil.getString(inOutStockMap.get("Quantity"));
	            String barCode = ConvertUtil.getString(inOutStockMap.get("BarCode"));
	            String unitCode = ConvertUtil.getString(inOutStockMap.get("UnitCode"));
	            String nameTotal = ConvertUtil.getString(inOutStockMap.get("NameTotal"));
	            
	            paramMap.put("promotionProductVendorID", productVendorID);
	            paramMap.put("endInOutQuantity", endInOutQuantity);
	            paramMap.put("barCode", barCode);
	            paramMap.put("unitCode", unitCode);
	            paramMap.put("nameTotal", nameTotal);
	            
	            boolean stockFindFlag = false;
	            for(int j=startJ;j<stockList.size();j++){
	                Map<String,Object> stockMap = stockList.get(j);
	                String curProductVendorID = ConvertUtil.getString(stockMap.get("BIN_PromotionProductVendorID"));
	                if(productVendorID.equals(curProductVendorID)){
	                    stockFindFlag = true;
	                    startJ = j+1;
	                    break;
	                }
	            }
	            if(!stockFindFlag){
	                //库存不存在
	                paramMap.put("actualStockQuantity", 0);
	                Map<String,Object> product = setProductInfo(paramMap,returnMap);
	                if(CherryUtil.obj2int(product.get("Quantity"))!=0){
	                    productList.add(product);
	                }
	            }
	        }
	        
	        map.put("TotalQuantity", returnMap.get("TotalQuantity"));
	        
	        return productList;
	    }
	    
	    
	    /**
	     * 设置促销品明细
	     * @param paramMap
	     * @param returnMap
	     * @return
	     */
	    private Map<String,Object> setProductInfo(Map<String,Object> paramMap,Map<String,Object> returnMap){
	        int detailNo = CherryUtil.obj2int(returnMap.get("DetailNo"));
	        int totalQuantity = CherryUtil.obj2int(returnMap.get("TotalQuantity"));
	        
	        Map<String,Object> product = new HashMap<String,Object>();
	        product.put("BIN_PromotionProductVendorID", paramMap.get("promotionProductVendorID"));
	        product.put("BIN_OrganizationID", paramMap.get("organizationID"));
	        product.put("BIN_InventoryInfoID", paramMap.get("inventoryInfoID"));
	        product.put("BIN_LogicInventoryInfoID", paramMap.get("logicInventoryInfoID"));
	        product.put("productName", paramMap.get("nameTotal"));
	        product.put("unitCode", paramMap.get("unitCode"));
	        product.put("barCode", paramMap.get("barCode"));
	        product.put("DepartCodeName", paramMap.get("departCodeName"));
	        product.put("DepotCodeName", paramMap.get("depotCodeName"));
	        product.put("LogicCodeName", paramMap.get("logicCodeName"));
	        product.put("actualStockQuantity", paramMap.get("actualStockQuantity"));
	        product.put("endInOutQuantity", paramMap.get("endInOutQuantity"));
	        product.put("Price", 0);
	        product.put("DetailNo", detailNo+1);
	        
	        int actualStockQuantity = CherryUtil.obj2int(paramMap.get("actualStockQuantity"));
	        int endInOutquantity = CherryUtil.obj2int(paramMap.get("endInOutQuantity"));
	        int quantity = endInOutquantity - actualStockQuantity;
	        if(quantity<0){
	            product.put("StockType", "0");//入库
	            product.put("Quantity", 0-quantity);
	            totalQuantity += 0-quantity;
	        }else{
	            product.put("StockType", "1");//出库
	            product.put("Quantity", quantity);
	            totalQuantity -= quantity;
	        }
	        product.put("Comments", remark);
	        product.put("CreatedBy", "BATCH");
	        product.put("CreatePGM", "BINBESSPRM06");
	        product.put("UpdatedBy", "BATCH");
	        product.put("UpdatePGM", "BINBESSPRM06");
	        
	        returnMap.put("DetailNo", detailNo+1);
	        returnMap.put("TotalQuantity", totalQuantity);
	        return product;
	    }
	    
	    /**
	     * 取得资源的值
	     * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
	     * @param language 语言
	     * @param key 资源的键
	     */
	    public String getResourceValue(String baseName, String language, String key) {
	        try{
	            String sysName = "";
	            //为空时，查询共通语言资源文件
	            String path = "i18n/common/commonText";
	            if(null != baseName && !"".equals(baseName)){
	                sysName = baseName.substring(5, 7);
	                path = "i18n/"+sysName+"/"+baseName;
	            }
	            return LocalizedTextUtil.findResourceBundle(path, new Locale(language)).getString(key);
	        }catch(Exception e){
	            return key;
	        }
	    }
	    
	    /**
	     * Excel导出参数类
	     */
	    class ExcelParam{
	        /** 参数 需传入organizationInfoId、brandInfoId*/
	        private Map<String, Object> map;
	        
	        /** 生成Excel数组 格式{数据库字段,资源文件key,列宽,对齐/数字格式(right/number),CodeTable的值}... */
	        private String[][] array;
	        
	        /** 资源的基本名称 */
	        private String baseName;
	        
	        /** 数据List */
	        private List<Map<String, Object>> dataList;
	        
	        /** 合计数组 格式{列数,合计内容,显示在第几列} */
	        private String[][] totalArr;
	        
	        /** sheet标签 */
	        private String sheetLabel;
	        
	        /** 显示查询条件 Excel第一行显示的内容，为空时不显示这一行。可使用\n换行 */
	        private String searchCondition;
	        
	        public Map<String, Object> getMap() {
	            return map;
	        }
	        public void setMap(Map<String, Object> map) {
	            this.map = map;
	        }
	        public String[][] getArray() {
	            return array;
	        }
	        public void setArray(String[][] array) {
	            this.array = array;
	        }
	        public String getBaseName() {
	            return baseName;
	        }
	        public void setBaseName(String baseName) {
	            this.baseName = baseName;
	        }
	        public List<Map<String, Object>> getDataList() {
	            return dataList;
	        }
	        public void setDataList(List<Map<String, Object>> dataList) {
	            this.dataList = dataList;
	        }
	        public String[][] getTotalArr() {
	            return totalArr;
	        }
	        public void setTotalArr(String[][] totalArr) {
	            this.totalArr = totalArr;
	        }
	        public void setSheetLabel(String sheetLabel) {
	            this.sheetLabel = sheetLabel;
	        }
	        public String getSheetLabel() {
	            return sheetLabel;
	        }
	        public String getSearchCondition() {
	            return searchCondition;
	        }
	        public void setSearchCondition(String searchCondition) {
	            this.searchCondition = searchCondition;
	        }
	    }
	    
	    /**
	     * Excel导出
	     * @param map
	     * @return
	     * @throws Exception
	     */
	    public byte[] exportExcel(Map<String, Object> map) throws Exception{
	        String organizationInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.ORGANIZATIONINFOID));
	        String brandInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.BRANDINFOID));
	        Map<String,Object> departParam = new HashMap<String,Object>();
	        departParam.put("BIN_OrganizationInfoID", organizationInfoID);
	        departParam.put("BIN_BrandInfoID", brandInfoID);
	        departParam.put("TestType", map.get("TestType"));
	        List<Map<String,Object>> departList = getDepotInfoList(departParam);
	        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	        for(int i=0;i<departList.size();i++){
	            Map<String,Object> param = new HashMap<String,Object>();
	            param.put("BIN_OrganizationID", departList.get(i).get("BIN_OrganizationID"));
	            param.put("BIN_InventoryInfoID", departList.get(i).get("BIN_InventoryInfoID"));
	            param.put("BIN_LogicInventoryInfoID", departList.get(i).get("BIN_LogicInventoryInfoID"));
	            param.put("organizationInfoId", organizationInfoID);
	            param.put("brandInfoId", brandInfoID);
	            param.put("departCodeName", departList.get(i).get("DepartCodeName"));
	            param.put("depotCodeName", departList.get(i).get("DepotCodeName"));
	            param.put("logicCodeName", departList.get(i).get("LogicCodeName"));
	            List<Map<String,Object>> differStockList = getDiffStockList(param);
	            if(null != differStockList && differStockList.size()>0){
	                dataList.addAll(differStockList);
	            }
	        }
	        
	        String[][] array = {
	                { "DepartCodeName", "DepartCodeName", "25", "", "" },
	                { "DepotCodeName", "DepotCodeName", "20", "", "" },
	                { "LogicCodeName", "LogicCodeName", "20", "", "" },
	                { "productName", "productName", "20", "", "" },
	                { "unitCode", "unitCode", "15", "", "" },
	                { "barCode", "barCode", "15", "", "" },
	                { "actualStockQuantity", "actualStockQuantity", "15", "right", "" },
	                { "endInOutQuantity", "endInOutQuantity", "15", "right", "" }
	        };
	        
	        ExcelParam ep = new ExcelParam();
	        ep.setMap(map);
	        ep.setArray(array);
	        ep.setBaseName("BINBESSPRM06");
	        ep.setSheetLabel("sheetName");
	        ep.setDataList(dataList);
	        return getExportExcel(ep);
	    }
	    
	    /**
	     * 共通Excel导出
	     * @param ep Excel参数
	     * @throws Exception
	     */
	    public byte[] getExportExcel(ExcelParam ep)
	            throws Exception {
	        try {
	            Map<String,Object> map = ep.getMap();
	            String[][] array = ep.getArray();
	            String baseName = ep.getBaseName();
	            String sheetLabel = ep.getSheetLabel();
	            List<Map<String, Object>> dataList = ep.getDataList();
	            String[][] totalArr = ep.getTotalArr();
	            String searchCondition = ep.getSearchCondition();
	            
	            String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
	            //取得资源的值
	            String[] titleArray = new String[array.length];
	            String sheetName = ConvertUtil.getString(getResourceValue(baseName,language,sheetLabel));
	            HashMap<String,Object> hm = new HashMap<String,Object>();
	            for(int i=0;i<array.length;i++){
	                titleArray[i] = getResourceValue(baseName,language,array[i][1]);
	                hm.put(array[i][0], i);
	            }
	            
	            // 创建工作薄 
	            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	            WritableWorkbook wwb = Workbook.createWorkbook(byteOut);
	            
	            //每Sheet页最多显示行数，读系统配置项，取不到使用默认值
	            String configValue = "50000";
	            int perSheetRow = CherryUtil.obj2int(configValue);
	            int totalSheetCount = dataList.size() / perSheetRow;
	            if(dataList.size() % perSheetRow > 0){
	                totalSheetCount = dataList.size() / perSheetRow+1;
	            }
	            if(totalSheetCount == 0 ){
	                totalSheetCount = 1;
	            }
	            for(int curSheetNum=0;curSheetNum<totalSheetCount;curSheetNum++){
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
	                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#");  
	                jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(nf);

	                //设置右对齐格式
	                jxl.write.WritableFont wf = new jxl.write.WritableFont(WritableFont.createFont("Arial"));
	                jxl.write.WritableCellFormat RwcfF = new jxl.write.WritableCellFormat(wf);
	                RwcfF.setAlignment(jxl.format.Alignment.RIGHT);

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
	                
	                //写入数据
	                for(Map<String, Object> dataMap:dataList.subList(formIndex, toIndex)){
	                    for(Map.Entry<String,Object> en:dataMap.entrySet()){
	                        if(hm.containsKey(en.getKey())){
	                            if(null != en.getValue() && !"".equals(en.getValue())){
	                                int index = Integer.parseInt(ConvertUtil.getString(hm.get(en.getKey())));
	                                String value = ConvertUtil.getString(en.getValue());
	                                if(null != array[index][4] && !"".equals(array[index][4])){
	                                    String codeTableKey = array[index][4];
	                                    ws.addCell(new Label(index,row,code.getVal(codeTableKey,value)));
	                                }else if(null != array[index][3] && "number".equals(array[index][3])){
	                                    jxl.write.Number nb = new jxl.write.Number(index,row,Double.parseDouble(value),wcf);   
	                                    ws.addCell(nb);                                
	                                }else if(null != array[index][3] && "right".equals(array[index][3])){
	                                    Label labelRight = new Label(index,row,value);
	                                    labelRight.setCellFormat(RwcfF);
	                                    ws.addCell(labelRight);
	                                }else{
	                                    ws.addCell(new Label(index,row,value));
	                                }
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
	        }
	    }
	    
	    /**
	     * 平库
	     * @param map
	     * @return
	     * @throws CherryBatchException
	     */
	    public int tran_insert(Map<String, Object> map) throws CherryBatchException{
	        //总计数
	        int totalCount = 0;
	        String organizationInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.ORGANIZATIONINFOID));
	        String brandInfoID = ConvertUtil.getString(map.get(CherryBatchConstants.BRANDINFOID));
	        String stockInOutDate = ConvertUtil.getString(map.get("stockInOutDate"));
	        Map<String,Object> departParam = new HashMap<String,Object>();
	        departParam.put("BIN_OrganizationInfoID", organizationInfoID);
	        departParam.put("BIN_BrandInfoID", brandInfoID);
	        departParam.put("TestType", map.get("TestType"));
	        List<Map<String,Object>> departList = getDepotInfoList(departParam);
	        for(int i=0;i<departList.size();i++){
	            try{
	                Map<String,Object> param = new HashMap<String,Object>();
	                param.put("BIN_OrganizationID", departList.get(i).get("BIN_OrganizationID"));
	                param.put("BIN_InventoryInfoID", departList.get(i).get("BIN_InventoryInfoID"));
	                param.put("BIN_LogicInventoryInfoID", departList.get(i).get("BIN_LogicInventoryInfoID"));
	                param.put("organizationInfoId", organizationInfoID);
	                param.put("brandInfoId", brandInfoID);
	                param.put("departCodeName", departList.get(i).get("DepartCodeName"));
	                param.put("depotCodeName", departList.get(i).get("DepotCodeName"));
	                param.put("logicCodeName", departList.get(i).get("LogicCodeName"));
	                List<Map<String,Object>> differStockList = getDiffStockList(param);
	                if(null != differStockList && differStockList.size()>0){
	                    Map<String,Object> mainData = new HashMap<String,Object>();
	                    mainData.put("BIN_OrganizationInfoID", organizationInfoID);
	                    mainData.put("BIN_BrandInfoID", brandInfoID);
	                    mainData.put("CreatedBy", "BATCH");
	                    mainData.put("CreatePGM", "BINBESSPRM06");
	                    mainData.put("UpdatedBy", "BATCH");
	                    mainData.put("UpdatePGM", "BINBESSPRM06");
	                    mainData.put("BIN_OrganizationID", departList.get(i).get("BIN_OrganizationID"));
	                    mainData.put("BIN_EmployeeID", "-9999");
	                    mainData.put("TradeType", CherryConstants.BUSINESS_TYPE_STOCKTAKE);
	                    mainData.put("Comments", remark);
	                    mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
	                    mainData.put("CloseFlag", "0");
	                    mainData.put("StockInOutDate", stockInOutDate);
	                    int totalQuantity = CherryUtil.obj2int(param.get("TotalQuantity"));
	                    if(totalQuantity>0){
	                        mainData.put("StockType", "0");
	                        mainData.put("TotalQuantity", totalQuantity);
	                    }else{
	                        mainData.put("StockType", "1");
	                        mainData.put("TotalQuantity", 0-totalQuantity);
	                    }
	                    mainData.put("TotalAmount", 0);

	                    try{
	                        totalCount += differStockList.size();
	                        insertProductInOutAll(mainData, differStockList);
	                        // 事务提交
	                        binBESSPRM06_Service.manualCommit();
	                        successCount += differStockList.size();
	                    }catch(Exception e){
	                        BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
	                        batchExceptionDTO.setBatchName(this.getClass());
	                        batchExceptionDTO.setErrorCode("ESS00026");
	                        batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
	                        batchExceptionDTO.setException(e);
	                        //组织ID：
	                        batchExceptionDTO.addErrorParam(organizationInfoID);
	                        //品牌ID：
	                        batchExceptionDTO.addErrorParam(brandInfoID);
	                        //部门ID：
	                        batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(departList.get(i).get("BIN_OrganizationID")));
	                        //接口单据号
	                        batchExceptionDTO.addErrorParam("");
	                        //入出库部门
	                        batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(departList.get(i).get("BIN_OrganizationID")));
	                        throw new CherryBatchException(batchExceptionDTO);
	                    }
	                    for(int index=0;index<differStockList.size();index++){
	                        //库存变化写入日志
	                        BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
	                        batchLoggerDTO.setCode("ISS00009");
	                        batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
	                        //部门
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("DepartCodeName")));
	                        //促销品名称
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("productName")));
	                        //厂商编码
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("unitCode")));
	                        //促销品条码
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("barCode")));
	                        //实体仓库
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("DepotCodeName")));
	                        //逻辑仓库
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("LogicCodeName")));
	                        //实盘库存
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("actualStockQuantity")));
	                        //期末库存
	                        batchLoggerDTO.addParam(CherryBatchUtil.getString(differStockList.get(index).get("endInOutQuantity")));
	                        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
	                        cherryBatchLogger.BatchLogger(batchLoggerDTO);
	                    }
	                }
	            }catch(Exception e){
	                e.printStackTrace();
	                // 事务回滚
	                binBESSPRM06_Service.manualRollback();
	                failCount ++;
	                flag = CherryBatchConstants.BATCH_WARNING;
	            }
	        }
	        
	        // 处理总件数
	        BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
	        batchLoggerDTO2.setCode("IIF00001");
	        batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
	        batchLoggerDTO2.addParam(String.valueOf(totalCount));
	        // 成功件数
	        BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
	        batchLoggerDTO3.setCode("IIF00002");
	        batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
	        batchLoggerDTO3.addParam(String.valueOf(successCount));
	        // 失败件数
	        BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
	        batchLoggerDTO5.setCode("IIF00005");
	        batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
	        batchLoggerDTO5.addParam(String.valueOf(failCount));
	        
	        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
	        // 处理总件数
	        cherryBatchLogger.BatchLogger(batchLoggerDTO2);
	        // 成功件数
	        cherryBatchLogger.BatchLogger(batchLoggerDTO3);
	        // 失败件数
	        cherryBatchLogger.BatchLogger(batchLoggerDTO5);
	        
	        return flag;
	    }
	    
	    /**
	     * 将促销品入出库信息写入入出库主从表，并更新相应的库存记录。
	     * @param mainData
	     * @param detailList
	     * @return 入出库主表的自增ID
	     */
	    public int insertProductInOutAll(
	            Map<String, Object> mainData, List<Map<String, Object>> detailList) {
	        int productInOutId = 0;
	        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
	        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
	        String tradeNo = ConvertUtil.getString(mainData.get("TradeNo"));
	        String tradeNoIF = ConvertUtil.getString(mainData.get("TradeNoIF"));
	        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
	        
	        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细
	        for(int i=0;i<detailList.size();i++){
	            Map<String,Object> productInOutDetail = detailList.get(i);
	            if(CherryUtil.obj2int(productInOutDetail.get("Quantity")) == 0){
	                detailList.remove(i);
	                i--;
	                continue;
	            }
	        }
	        if(detailList.size() == 0){
	            return 0;
	        }
	        
	        //如果tradeNo不存在调用共通生成单据号
	        if("".equals(tradeNo)){
	            tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,"IO");
	            mainData.put("TradeNo", tradeNo);
	        }
	        if("".equals(tradeNoIF)){
	            mainData.put("TradeNoIF", tradeNo);
	        }
	        if(null == mainData.get("StockInOutDate")){
	            mainData.put("StockInOutDate", binOLSTCM01_Service.getDateYMD());
	        }
	        if(null == mainData.get("StockInOutTime")){
	            mainData.put("StockInOutTime", binOLSTCM01_Service.getSYSDateConf());
	        }
	        //插入促销品入出库表
	        productInOutId = binBESSPRM06_Service.insertPromotionStockInOut(mainData);
	        
	        for(int i=0;i<detailList.size();i++){
	            Map<String,Object> productInOutDetail = detailList.get(i);
	            productInOutDetail.put("BIN_PromotionStockInOutID", productInOutId); 
	            
	            if(null == productInOutDetail.get("BIN_ProductVendorPackageID")){
	                productInOutDetail.put("BIN_ProductVendorPackageID", 0);
	            }
	            if(null == productInOutDetail.get("BIN_LogicInventoryInfoID")){
	                productInOutDetail.put("BIN_LogicInventoryInfoID", 0);
	            }
	            if(null == productInOutDetail.get("BIN_StorageLocationInfoID")){
	                productInOutDetail.put("BIN_StorageLocationInfoID", 0);
	            }
	            
	            productInOutDetail.put("DetailNo",i+1);
	            
	            //插入促销品入出库明细表
	            binBESSPRM06_Service.insertPromotionStockDetail(productInOutDetail);
	        }
	        
	        return productInOutId;
	    }
}
