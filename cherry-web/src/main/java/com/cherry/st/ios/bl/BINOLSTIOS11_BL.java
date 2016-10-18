/*
 * @(#)BINOLSTIOS10_BL.java     1.0 2015/02/04
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
package com.cherry.st.ios.bl;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.service.BINOLSTCM10_Service;
import com.cherry.st.ios.interfaces.BINOLSTIOS11_IF;
import com.cherry.st.ios.service.BINOLSTIOS11_Service;

/**
 * 
 * 退库申请（Excel导入）BL
 * 
 * @author niushunjie
 * @version 1.0 2015.02.04
 */
public class BINOLSTIOS11_BL implements BINOLSTIOS11_IF {
    
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS11_BL.class);
    
    @Resource(name="binOLSTIOS11_Service")
    private BINOLSTIOS11_Service binOLSTIOS11_Service;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_BL binOLSTCM00_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLSTCM10_Service")
    private BINOLSTCM10_Service binOLSTCM10_Service;
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;

    @Override
    public int getImportBatchCount(Map<String, Object> map) {
        return binOLSTIOS11_Service.getImportBatchCount(map);
    }

    @Override
    public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
        return binOLSTIOS11_Service.getImportBatchList(map);
    }

    @Override
    public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception {
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
        // 退库申请数据sheet
        Sheet dateSheet = null;
        for (Sheet st : sheets) {
            if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName().trim())) {
                descriptSheet = st;
            } else if (CherryConstants.PRORETURNREQUEST_SHEET_NAME.equals(st.getName().trim())) {
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
            if (!CherryConstants.PRORETURNREQUEST_EXCEL_VERSION.equals(version)) {
                // 模板版本不正确，请下载最新的模板进行导入！
                throw new CherryException("EBS00103");
            }
        }
        // 退库申请数据sheet不存在
        if (null == dateSheet) {
            throw new CherryException("EBS00030", new String[] { CherryConstants.PRORETURNREQUEST_SHEET_NAME });
        }
        
        // 退库申请数据sheet的行数
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
                map.put("ImportBatchCode", importBatchCode);
            }
        }else{
            // 此处不进行重复校验（importBatchCode参数不是由EXCEL提供，已预校验过）
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
            // 退库部门编码
            String excelDepartCodeFrom = dateSheet.getCell(3, r).getContents().trim();
            rowMap.put("excelDepartCodeFrom", excelDepartCodeFrom);
            // 退库部门名称
            String excelDepartNameFrom = dateSheet.getCell(4, r).getContents().trim();
            rowMap.put("excelDepartNameFrom", excelDepartNameFrom);
            // 接受退库部门编码
            String excelDepartCodeTo = dateSheet.getCell(5, r).getContents().trim();
            rowMap.put("excelDepartCodeTo", excelDepartCodeTo);
            // 接受退库部门名称
            String excelDepartNameTo = dateSheet.getCell(6, r).getContents().trim();
            rowMap.put("excelDepartNameTo", excelDepartNameTo );
            //价格
            String excelPrice = dateSheet.getCell(7,r).getContents().trim();
            rowMap.put("excelPrice", excelPrice);
            // 产品数量
            String excelQuantity = dateSheet.getCell(8, r).getContents().trim();
            rowMap.put("excelQuantity", excelQuantity);
            // 逻辑仓库名称
            String excelLogicInventoryName = dateSheet.getCell(9, r).getContents().trim();
            rowMap.put("excelLogicInventoryName", excelLogicInventoryName);
            rowMap.put("rowNumber", r+1);
            if (CherryChecker.isNullOrEmpty(excelUnitCode)
                    && CherryChecker.isNullOrEmpty(excelBarCode)
                    && CherryChecker.isNullOrEmpty(excelProductName)
                    && CherryChecker.isNullOrEmpty(excelDepartCodeFrom)
                    && CherryChecker.isNullOrEmpty(excelDepartNameFrom)
                    && CherryChecker.isNullOrEmpty(excelDepartCodeTo)
                    && CherryChecker.isNullOrEmpty(excelDepartNameTo)
                    && CherryChecker.isNullOrEmpty(excelPrice)
                    && CherryChecker.isNullOrEmpty(excelQuantity)
                    && CherryChecker.isNullOrEmpty(excelLogicInventoryName)) {
                break;

            }
            // 数据基本格式校验
             this.checkData(rowMap);
            // 是否导入重复数据参数
            String importRepeat = ConvertUtil.getString(map.get("importRepeat"));
            if(!CherryChecker.isNullOrEmpty(importRepeat, true) && "0".equals(importRepeat)){
                // 不导入重复数据时出现重复数据则报错
                if(isRepeatData(rowMap,map)){
                    throw new CherryException("STM00028",new String[] { ConvertUtil.getString(r+1) });
                }
            }
            // 根据退库部门code、接收退库部门CODE、逻辑仓库拆分为单据
            String importDatasKey = excelDepartCodeFrom + excelDepartCodeTo  + excelLogicInventoryName;
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
        if(r==3){
            throw new CherryException("EBS00035",new String[] { CherryConstants.PRORETURNREQUEST_SHEET_NAME });
        }
        return importDatas;
    }

    @Override
    public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap, Map<String, Object> sessionMap) throws Exception {
        UserInfo userInfo = (UserInfo)sessionMap.get("userInfo");
        // 导入日期
        String sysDate = binOLSTIOS11_Service.getSYSDate();
        sessionMap.put("ImportDate", sysDate);
        // 插入导入批次信息返回导入批次ID
        int importBatchId = this.insertImportBatch(sessionMap);
        
        // 导入结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 导入成功的数量
        int successCount = 0;
        // 导入失败的数量
        int errorCount = 0;
        // 导入失败的单据
        List<Map<String, Object>> errorBillList = new ArrayList<Map<String, Object>>();
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
                mainMap.put("BillNo", billNo);
                mainMap.put("BillNoIF", billNo);
                // 总数量
                mainMap.put("TotalQuantity", detailMap.get("TotalQuantity"));
                // 总金额
                mainMap.put("TotalAmount", detailMap.get("TotalAmount"));
                mainMap.put("ImportDate", sysDate);
                mainMap.put("BIN_ImportBatchID", importBatchId);
                mainMap.put("ImportBatch", sessionMap.get("ImportBatchCode"));
            } else {
                continue;
            }
            
            if ((Boolean) detailMap.get("ResultFlag")) {
                // 验证不通过，只将数据添加到退库申请（Excel导入）主表和明细表
                mainMap.put("ImportResult", "0");
                int proReturnRequestExcelID = binOLSTIOS11_Service.insertProReturnRequestExcel(mainMap);
                String totalErrorMsg = ConvertUtil.getString(detailMap.get("totalError"));
                for (Map<String, Object> map : detailList) {
                    map.put("BIN_ProReturnRequestExcelID", proReturnRequestExcelID);
                    map.put("ErrorMsg", totalErrorMsg+map.get("ErrorMsg"));
                    binOLSTIOS11_Service.insertProReturnReqExcelDetail(map);
                    errorBillList.add(map);
                }
                errorCount++;
            } else {
                // 验证成功，合并相同产品明细
                Map<String,Object> detailListGroup = new HashMap<String, Object>();
                for(Map<String, Object> fm : detailList){
                    Map<String, Object> detailGroupMap = new HashMap<String, Object>();
                    //退库申请明细表对应字段进行赋值
                    fm.put("BIN_InventoryInfoID", mainMap.get("BIN_InventoryInfoIDFrom"));
                    fm.put("BIN_LogicInventoryInfoID", mainMap.get("BIN_LogicInventoryInfoIDFrom"));
                    fm.put("BIN_InventoryInfoIDReceive", mainMap.get("BIN_InventoryInfoIDTo"));
                    fm.put("BIN_LogicInventoryInfoIDReceive", mainMap.get("BIN_LogicInventoryInfoIDTo"));
                    detailGroupMap.putAll(fm);
                    String unitCode = ConvertUtil.getString(detailGroupMap.get("UnitCode")).trim();
                    String barCode = ConvertUtil.getString(detailGroupMap.get("BarCode")).trim();
                    String price = ConvertUtil.getString(detailGroupMap.get("Price")).trim();
                    /**相同产品不同价格的明细不进行合并*/
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
                // 合并后明细转换为退库申请单明细List
                List<Map<String, Object>> returnReqDetailList = new ArrayList<Map<String,Object>>();
                Iterator detailIt = detailListGroup.entrySet().iterator();
                while (detailIt.hasNext()) {
                    Entry detailEn = (Entry) detailIt.next();
                    returnReqDetailList.add((Map<String, Object>) detailEn.getValue());
                }
                mainMap.put("BIN_OrganizationID", mainMap.get("BIN_OrganizationIDFrom"));
                mainMap.put("BIN_InventoryInfoID", mainMap.get("BIN_InventoryInfoIDFrom"));
                mainMap.put("BIN_LogicInventoryInfoID", mainMap.get("BIN_LogicInventoryInfoIDFrom"));
                mainMap.put("BIN_OrganizationIDReceive", mainMap.get("BIN_OrganizationIDTo"));
                mainMap.put("BIN_InventoryInfoIDReceive", mainMap.get("BIN_InventoryInfoIDTo"));
                mainMap.put("BIN_LogicInventoryInfoIDReceive", mainMap.get("BIN_LogicInventoryInfoIDTo"));
                mainMap.put("TradeType", CherryConstants.OS_BILLTYPE_RA);
                //主表    审核区分
                mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
                //主表   物流ID
                mainMap.put("BIN_LogisticInfoID", 0);
                //主表     日期
                mainMap.put("TradeDate", sysDate.substring(0, 10));
                //主表 模式（短流程）
                mainMap.put("Model", "M2");
                
                 // 准备参数，开始工作流
                Map<String, Object> pramMap = new HashMap<String, Object>();
                pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);
                pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
                pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
                pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
                pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
                pramMap.put("CurrentUnit", "BINOLSTSFH17");
                pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
                pramMap.put("UserInfo", userInfo);
                pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
                pramMap.put("BrandCode", userInfo.getBrandCode());
                pramMap.put("BIN_OrganizationID", ConvertUtil.getString(mainMap.get("BIN_OrganizationIDFrom")));
                Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(ConvertUtil.getString(mainMap.get("BIN_OrganizationIDFrom")), null);
                if(null != departmentInfo && !departmentInfo.isEmpty()){
                    pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//退库部门编号
                }
                pramMap.put("returnReqMainData", mainMap);
                pramMap.put("returnReqDetailList", returnReqDetailList);
                long workFlowId = 0;
                workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);
                
                if (workFlowId == 0) {
                    throw new CherryException("EWF00001");
                }
                mainMap.put("ImportResult", "1");
                mainMap.put("WorkFlowID", workFlowId);
                // 将数据添加到退库申请单（Excel导入）主表和明细表
                int proReturnRequestExcelID = binOLSTIOS11_Service.insertProReturnRequestExcel(mainMap);
                for (Map<String, Object> map : detailList) {
                    map.put("BIN_ProReturnRequestExcelID", proReturnRequestExcelID);
                    map.put("ErrorMsg", PropertiesUtil.getText("STM00014"));
                    binOLSTIOS11_Service.insertProReturnReqExcelDetail(map);
                }
                successCount++;
            }
        }
        resultMap.put("successCount", successCount);
        resultMap.put("errorCount", errorCount);
        resultMap.put("errorBillList", errorBillList);
        return resultMap;
    }

    /**
     * 生成退库申请单据号
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
        String name = "BINOLSTIOS11_BL";
        // 调用共通生成单据号
        return binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, "RA");
    }

    @Override
    public int insertImportBatch(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put("ImportBatchCodeIF",map.get("ImportBatchCode"));
        return binOLSTIOS11_Service.insertImportBatch(paramMap);
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
        String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
        String excelLogicInventoryName = ConvertUtil.getString(rowMap.get("excelLogicInventoryName"));
        if(CherryChecker.isNullOrEmpty(excelUnitCode, true) || excelUnitCode.length() > 20){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"A"+rowNumber});
        }
        if(excelBarCode.length() > 13){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"B"+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelProductName, true) || excelProductName.length() > 50){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"C"+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelDepartCodeFrom, true) || excelDepartCodeFrom.length() > 15){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"D"+rowNumber});
        }
        if(excelDepartNameFrom.length() > 50){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"E"+rowNumber});
        }
        if(excelDepartCodeTo.length() > 15){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"F"+rowNumber});
        }
        if(excelDepartNameTo.length() > 50){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"G"+rowNumber});
        }
        if(CherryChecker.isNullOrEmpty(excelQuantity, true) || !CherryChecker.isNumeric(excelQuantity) ||  excelQuantity.length() > 9){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"I"+rowNumber});
        } else {
            // 将类似于01的数据转换为1【不将其认为为错误数据】
            rowMap.put("excelQuantity", ConvertUtil.getInt(excelQuantity));
        }
        if(excelLogicInventoryName.length()>30){
            throw new CherryException("EBS00034",new String[]{CherryConstants.PRORETURNREQUEST_SHEET_NAME,"J"+rowNumber});
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
        String excelUnitCode = ConvertUtil.getString(rowMap.get("excelUnitCode"));
        String excelQuantity = ConvertUtil.getString(rowMap.get("excelQuantity"));
        String excelBarCode = ConvertUtil.getString(rowMap.get("excelBarCode"));
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
        paramsMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
        paramsMap.put("departCodeFrom", excelDepartCodeFrom);
        paramsMap.put("departCodeTo", excelDepartCodeTo);
        paramsMap.put("unitCode", excelUnitCode);
        paramsMap.put("quantity", excelQuantity);
        paramsMap.put("barCode", excelBarCode);
        List<Map<String, Object>> repeatData = binOLSTIOS11_Service.getRepeatData(paramsMap);
        if(repeatData != null && repeatData.size() >0){
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
    public Map<String, Object> validExcel(List<Map<String, Object>> detailList, Map<String, Object> sessionMap) throws Exception {
    	String brandInfoId = ConvertUtil.getString(sessionMap.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(sessionMap.get("organizationInfoId"));
		//取得系统配置项库存是否允许为负
        boolean isAllowMinusStockFlag = binOLCM14_BL.isConfigOpen("1109", organizationInfoId, brandInfoId);
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
            // 一单中退库部门信息、接收退库部门信息，只需一次验证(主数据)
            Map<String, Object> infoMap = detailList.get(0);
            // 校验退库部门名称
            String departNameFrom = ConvertUtil.getString(infoMap.get("excelDepartNameFrom"));
            if(CherryChecker.isNullOrEmpty(departNameFrom)){
//              resultFlag = true;
//              commonErrorsMap.put("departNameOrderNull", true);
            }else if (departNameFrom.length() > 50) {
                resultFlag = true;
                commonErrorsMap.put("departNameFromError", true);
                commonErrorsMap.put("departNameFrom", departNameFrom);
            } else {
                commonInfo.put("ImportDepartNameFrom", departNameFrom);
            }
            // 校验退库部门code
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
                // 根据部门code获取部门信息
                Map<String, Object> orgMap = binOLSTIOS11_Service.getOrgByCode(orgParamMap);
                if (orgMap == null) {
                    resultFlag = true;
                    // 指定部门不存在或者无权限操作
                    commonErrorsMap.put("departCodeFromExits", true);
                    commonErrorsMap.put("departCodeFrom", departCodeFrom);
                    // 校验接收退库部门时依据此字段判断是否进行校验
                    commonInfo.put("BIN_OrganizationIDFrom", null);
                } else {
                    commonInfo.put("departType", orgMap.get("Type"));
                    // 校验正确，获取退库货部门ID
                    commonInfo.put("BIN_OrganizationIDFrom", orgMap.get("BIN_OrganizationID"));
                    // 获取默认实体仓库LIST
                    List<Map<String, Object>> depotOrderList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(orgMap.get("BIN_OrganizationID")),ConvertUtil.getString(sessionMap.get(CherryConstants.SESSION_LANGUAGE)));
                    
                    if (null != depotOrderList && depotOrderList.size() > 0) {
                        Map<String, Object> inventoryInfo = depotOrderList.get(0);
                        // 获取实体仓库ID
                        //【退库申请逻辑的主数据与明细数据，校验接收退库部门】
                        commonInfo.put("BIN_InventoryInfoIDFrom", inventoryInfo.get("BIN_DepotInfoID"));
                        // 仓库库位ID
                        commonInfo.put("BIN_StorageLocationInfoID", "0");
                        // 包装类型
                        commonInfo.put("BIN_ProductVendorPackageID", "0");
                    } else {
                        resultFlag = true;
                        commonErrorsMap.put("depotFromError", true);
                    }
                }
            }
            
            /***************校验退库逻辑仓库信息********************/
            // 校验退库逻辑仓库名称
            String logicInventoryName = ConvertUtil.getString(infoMap.get("excelLogicInventoryName"));
            if(CherryChecker.isNullOrEmpty(logicInventoryName) && !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))){
                //逻辑仓库为为空，根据部门类型获取指定类型逻辑仓库
                String logicType = "";
                if("4".equals(commonInfo.get("departType"))){
                    sessionMap.put("Type", "1");
                    logicType = "1";
                }else{
                    sessionMap.put("Type", "0");
                    logicType = "0";
                }
                
                Map<String, Object> outLogicParam = new HashMap<String, Object>();
                outLogicParam.put("BIN_BrandInfoID", sessionMap.get(CherryConstants.BRANDINFOID));
                outLogicParam.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_RR);
                outLogicParam.put("Type", logicType);
                outLogicParam.put("ProductType", "1");
                outLogicParam.put("language", "");
                List<Map<String,Object>> inLogicList = binOLCM18_BL.getLogicDepotByBusiness(outLogicParam);
                Map<String, Object> logicInventoryInfo = inLogicList.get(0);
                commonInfo.put("BIN_LogicInventoryInfoIDFrom", logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
                commonInfo.put("LogicInventoryNameFrom", logicInventoryInfo.get("InventoryNameCN"));
            }else if(!CherryChecker.isNullOrEmpty(logicInventoryName) && logicInventoryName.length() > 30){
                resultFlag = true;
                commonErrorsMap.put("logicInventoryNameLength", true);
                commonErrorsMap.put("logicInventoryName", logicInventoryName);
            }else if(!CherryChecker.isNullOrEmpty(logicInventoryName) && !CherryChecker.isNullOrEmpty(commonInfo.get("departType"))){
                commonInfo.put("LogicInventoryNameFrom", logicInventoryName);
                //逻辑仓库不为空，根据部门类型获取指定类型的逻辑仓库，并判断逻辑仓库是否存在
                if("4".equals(commonInfo.get("departType"))){
                    sessionMap.put("Type", "1");
                }else{
                    sessionMap.put("Type", "0");
                }
                sessionMap.put("InventoryName", logicInventoryName);
                Map<String, Object> logicInventoryInfo = binOLSTIOS11_Service.getLogicInventoryByName(sessionMap);
                if(CherryChecker.isNullOrEmpty(logicInventoryInfo)){
                    resultFlag = true;
                    // 逻辑仓库不存在
                    commonErrorsMap.put("logicInventoryNameExits", true);
                }else{
                    commonInfo.putAll(logicInventoryInfo);
                }
            }else{
                commonInfo.put("LogicInventoryNameFrom", logicInventoryName);
            }
            
            /******************校验接收退库部门信息（须根据退库部门、实体仓库、逻辑仓库进行校验）***********/
            // 校验接收退库部门名称
            String departNameTo = ConvertUtil.getString(infoMap.get("excelDepartNameTo"));
            if(CherryChecker.isNullOrEmpty(departNameTo)){
//              resultFlag = true;
//              commonErrorsMap.put("departNameAcceptNull", true);
            }else if (departNameTo.length() > 50) {
                resultFlag = true;
                commonErrorsMap.put("departNameToError", true);
                commonErrorsMap.put("departNameTo", departNameTo);
            } else {
                commonInfo.put("ImportDepartNameTo", departNameTo);
            }
            
            /**=================== 校验接收退库部门code=======================*/
            // 接收退库部门是否取默认接收退库部门标记
            boolean defaultFlag = false;
            String departCodeTo = ConvertUtil.getString(infoMap.get("excelDepartCodeTo"));
            if (CherryChecker.isNullOrEmpty(departCodeTo)) {
                // 未填写接收部门CODE则取默认接收退库部门
                defaultFlag = true;
            } else if (departCodeTo.length() > 15) {
                resultFlag = true;
                commonErrorsMap.put("departCodeToLength", true);
                commonErrorsMap.put("departCodeTo", departCodeTo);
            } else {
                commonInfo.put("ImportDepartCodeTo", departCodeTo);
                sessionMap.put("DepartCode", departCodeTo);
                // 根据接收退库部门code获取部门信息(可为柜台)
                Map<String, Object> orgToMap = binOLSTIOS11_Service.getOrgByCode(sessionMap);
                if (orgToMap == null) {
                    resultFlag = true;
                    commonErrorsMap.put("departCodeToExits", true);
                } else {
                    // 当退库部门不存在时无需验证接收退库部门【接收退库部门的有效性是依据退库部门的】
                    if(!CherryChecker.isNullOrEmpty(commonInfo.get("BIN_OrganizationIDFrom"))) {
                        String organizationIDTo = ConvertUtil.getString(orgToMap.get("BIN_OrganizationID"));
                        /***************接收部门必须是指定实体仓库与逻辑仓库可退库的部门***************/
                        orgToMap = this.getCompleteParam(sessionMap,commonInfo);
                        
                        // 取得系统基本配置信息中的"仓库业务配置"
                        String ret = binOLCM14_BL.getConfigValue("1028",String.valueOf(sessionMap.get("BIN_OrganizationInfoID")),String.valueOf(sessionMap.get("BIN_BrandInfoID")));
                        int orgAcceptCount = 0;
                        // 接受退库部门ID
                        orgToMap.put("BIN_OrganizationIDTo", organizationIDTo);
                        // 按部权限大小关系
                        /** 参考BINOLSTCM10_BL.getDepartInfoByBusinessType */
                        if(CherryConstants.DEPOTBUSINESS_DEPART.equals(ret)){
                            // 当仓库业务关系是按部门层级高低关系时根据仓库ID取得部门
                            orgAcceptCount = this.getDepartCountByOrgRelationship(orgToMap);
                        }// 按区域大小关系
                        else if(CherryConstants.DEPOTBUSINESS_REGION.equals(ret)){
                            // 当仓库业务关系是按实际业务配置设定时根据仓库ID取得部门
                            orgAcceptCount = this.getDepotCountByRegionRelationship(orgToMap);
                        }
                        if(orgAcceptCount == 0) {
                            // 不存在 或者无权限操作此接收退库部门！
                            resultFlag = true;
                            commonErrorsMap.put("departCodeToExits", true);
                            commonErrorsMap.put("departCodeTo", departCodeTo);
                        } else {
                            commonInfo.put("BIN_OrganizationIDTo", organizationIDTo);
                        }
                    }
                    
                }
            }
            
            /**=======================接收退库方仓库======================================*/
            // 当退库部门不存在时无法取得接收退库部门及仓库【接收退库部门及仓库是根据退库部门及仓库的】
            if(!CherryChecker.isNullOrEmpty(commonInfo.get("BIN_OrganizationIDFrom")) && !CherryChecker.isNullOrEmpty(commonInfo.get("BIN_InventoryInfoIDFrom"))) {
                //调用共通取得接受退库方的所有仓库，调用共通取得退库方仓库对应的接收退库方仓库，比较这两个List，取出在两边都存在的仓库ID作为发货方仓库
                int inventoryInfoIDTo = 0;
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BIN_OrganizationInfoID", sessionMap.get(CherryConstants.ORGANIZATIONINFOID));
                param.put("BIN_BrandInfoID", sessionMap.get(CherryConstants.BRANDINFOID));
                param.put("DepotID", commonInfo.get("BIN_InventoryInfoIDFrom"));
                param.put("InOutFlag", "OUT");
                param.put("BusinessType", CherryConstants.OPERATE_RR);
                param.put("language", "");
                // 退库仓库可退库的[接收退库仓库]
                List<Map<String,Object>> inDepotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
                if(defaultFlag) {
                    // 接收退库方部门为空时，取默认接收退库方部门
                    commonInfo.put("BIN_OrganizationIDTo", inDepotList.get(0).get("BIN_OrganizationID"));
                }
                // 接收退库部门使用的所有实体仓库信息
                List<Map<String,Object>> inDepotByDepartList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(commonInfo.get("BIN_OrganizationIDTo")),"");
                if(null != inDepotList && inDepotList.size()>0){
                    inventoryInfoIDTo = CherryUtil.obj2int(inDepotList.get(0).get("BIN_DepotInfoID"));
                    if(null != inDepotByDepartList && inDepotByDepartList.size()>0){
                        for(int i=0;i<inDepotList.size();i++){
                            int curDepotID = CherryUtil.obj2int(inDepotList.get(i).get("BIN_DepotInfoID"));
                            for(int j=0;j<inDepotByDepartList.size();j++){
                                int curDepotByDepartID = CherryUtil.obj2int(inDepotByDepartList.get(j).get("BIN_DepotInfoID"));
                                if(curDepotID == curDepotByDepartID){
                                    inventoryInfoIDTo = curDepotID;
                                    break;
                                }
                            }
                        }
                    }
                }
                // 接收退库的仓库ID
                commonInfo.put("BIN_InventoryInfoIDTo", inventoryInfoIDTo);
                
                // 接收退库方逻辑仓库
                int logicInventoryInfoIDTo = 0;
                Map<String, Object> inLogicParam = new HashMap<String, Object>();
                inLogicParam.put("BIN_BrandInfoID", sessionMap.get(CherryConstants.BRANDINFOID));
                inLogicParam.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
                inLogicParam.put("Type", "0");
                inLogicParam.put("ProductType", "1");
                inLogicParam.put("language", "");
                List<Map<String,Object>> inLogicList = binOLCM18_BL.getLogicDepotByBusiness(inLogicParam);
                if(null != inLogicList && inLogicList.size()>0){
                    logicInventoryInfoIDTo = CherryUtil.obj2int(inLogicList.get(0).get("BIN_LogicInventoryInfoID"));
                }
                // 接收退库的逻辑仓库ID
                commonInfo.put("BIN_LogicInventoryInfoIDTo", logicInventoryInfoIDTo);
            }
        }
        int detailNo=1;
        for(Map<String, Object> detailMap : detailList){
            Map<String, Object> errorsMap = new HashMap<String, Object>();
            errorsMap.putAll(commonErrorsMap);
            detailMap.putAll(sessionMap);
            detailMap.putAll(commonInfo);
            // 产品明细连番
            detailMap.put("DetailNo", detailNo++ );
            /** ------------------校验产品名称-----------------------------------------**/
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
            /** ------------------校验产品厂商编码、产品条码-----------------------------------------**/
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
                List<Map<String, Object>> prtInfoList = binOLSTIOS11_Service.getPrtInfo(sessionMap);
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
            // 退库数量校验
            String quantity = ConvertUtil.getString(detailMap.get("excelQuantity"));
            if(CherryChecker.isNullOrEmpty(quantity, true) || "0".equals(quantity)){
                resultFlag = true;
                errorsMap.put("quantityNull", true);
            }else if (!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(quantity))||quantity.length() > 9) {
                resultFlag = true;
                errorsMap.put("quantityError", true);
                errorsMap.put("quantity", quantity);
            }else{
                detailMap.put("Quantity", quantity);
            }
            
            if(!resultFlag && !isAllowMinusStockFlag) {
            	// 该条数据无错误时，还需要根据系统配置项的【是否允许负库存操作】来校验数量
            	Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(commonInfo.get("BIN_InventoryInfoIDFrom")));
                paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(commonInfo.get("BIN_LogicInventoryInfoIDFrom")));
                paramMap.put("FrozenFlag", "1");//不扣除冻结库存
                paramMap.put("BIN_ProductVendorID", detailMap.get("BIN_ProductVendorID"));
                int stockQuantity = binOLCM20_BL.getProductStock(paramMap);
                if(stockQuantity - ConvertUtil.getInt(quantity) < 0) {
                	// 退库数量大于当前库存数量
                	resultFlag = true;
                	errorsMap.put("quantityStockError", true);
                    errorsMap.put("quantity", quantity);
                    errorsMap.put("stockQuantity", stockQuantity);
                }
            }
            
            /**---------------------- 产品价格如果没有，从数据库中取得----------------------*/
            sessionMap.put("BIN_ProductID", detailMap.get("BIN_ProductID"));
            String sysDate = ConvertUtil.getString(detailMap.get("ImportDate"));
            String excelPrice = ConvertUtil.getString(detailMap.get("excelPrice"));
            if(!excelPrice.equals("")){
                detailMap.put("Price",excelPrice);
            }else{
            	//配置项 产品发货入库使用价格（销售价格/会员价格/结算价）
	            String priceColName = binOLCM14_BL.getConfigValue("1130", organizationInfoId,brandInfoId);
                detailMap.put("Price",getPrice(binOLSTIOS11_Service.getPrtPrice(sessionMap),sysDate.substring(0, 10),priceColName));
            }
            // 产品总数量和总金额
            if (!errorsMap.containsKey("quantityError")) {
                totalQuantity += ConvertUtil.getInt(quantity);
                totalAmount += ConvertUtil.getInt(quantity) * ConvertUtil.getFloat(detailMap.get("Price"));
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
    
    /**
     * 当仓库业务关系是按区域大小设定时，根据仓库ID判断该接受退库部门是否存在
     * @param orgMap
     * @return
     */
    private int getDepotCountByRegionRelationship(Map<String, Object> param) {
        //调用共通, 根据指定的仓库和业务类型，取得对方仓库信息
        List<Map<String,Object>> depotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
        if(depotList.isEmpty()){
            return 0;
        }
        //仓库参数数组
        Integer[] inventoryInfoID = new Integer[depotList.size()];

        //遍历depotList,根据仓库ID取得部门信息
        for(int index=0 ; index<depotList.size(); index++){
            int curInventoryInfoID = CherryUtil.obj2int(depotList.get(index).get("BIN_DepotInfoID"));
            inventoryInfoID[index] = curInventoryInfoID;
        }
        //查询参数Map
        Map<String,Object> paramMap = new HashMap<String,Object>();
        
        //仓库数组
        paramMap.put("inventoryInfoID", inventoryInfoID);
        //TestType
        paramMap.put("TestType", depotList.get(0).get("TestType"));
        paramMap.putAll(param);
        // 判断指定接收退库部门是否在仓库ID对应的部门内
        int count = binOLSTIOS11_Service.getDepartInfoCount(paramMap);
        
        return count;
    }

    /**
     * 当仓库业务关系是按部门层级关系时,根据仓库ID判断接收退库部门是否存在
     * @param orgMap
     * @return
     */
    private int getDepartCountByOrgRelationship(Map<String, Object> paramMap) {
        // 总数
        int count = 0;
        // 取得该部门对应的类型
        Map<String,Object> departMap = binOLSTCM10_Service.getDepartTypeByID(ConvertUtil.getInt(paramMap.get("BIN_OrganizationID")));
        // 取得该部门等级比它低的部门类型
        paramMap.put("TestType", departMap.get("TestType"));
        
        count = binOLSTIOS11_Service.getSupDepartCount(paramMap);
        
        return count;
    }

    /**
     * 完善接收部门必须是指定实体仓库与逻辑仓库可接收的部门相关查询的参数
     * @param sessionMap
     * @param commonInfo
     * @return
     */
    private Map<String, Object> getCompleteParam(Map<String, Object> sessionMap,Map<String, Object> commonInfo) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(sessionMap);
        paramMap.put("SORT_ID", "organizationId asc");
        // 业务类型
        paramMap.put("BusinessType", CherryConstants.OPERATE_RR);
        // 入出库区分
        paramMap.put("InOutFlag", "OUT");
        // 权限 业务类型
        paramMap.put("businessType", "1");
        // 权限  操作类型
        paramMap.put("operationType", "0");
        // 退库部门ID
        paramMap.put("BIN_OrganizationID", commonInfo.get("BIN_OrganizationIDFrom"));
        // 退库仓库ID
        paramMap.put("DepotID", commonInfo.get("BIN_InventoryInfoIDFrom"));
        //品牌ID
        paramMap.put("BIN_BrandInfoID", sessionMap.get("brandInfoId"));
        //组织ID
        paramMap.put("BIN_OrganizationInfoID", sessionMap.get("organizationInfoId"));

        return paramMap;
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
     * 取得退库日期时的销售价格
     * 
     * @param priceList
     *            按照开始时间排序的产品价格信息
     * @param importDate
     *            退库时间
      * @param priceColName
	 * 			    价格字段名
	 * @return
	 */
	public Object getPrice(List<Map<String, Object>> priceList, Object inDepotDate, String priceColName) {
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
				String inDepotDateStr = ConvertUtil.getString(inDepotDate);
				// 当入库日期小于所有价格的日期
				if (i == 0
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("StartDate"))) <= 0) {
					return map.get(priceColName);
				}
				if (CherryChecker.compareDate(inDepotDateStr,
						ConvertUtil.getString(map.get("StartDate"))) >= 0
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("EndDate"))) <= 0) {
					return map.get(priceColName);
				}
				// 当入库日期大于所有的价格日期
				if (i == (length - 1)
						&& CherryChecker.compareDate(inDepotDateStr,
								ConvertUtil.getString(map.get("EndDate"))) >= 0) {
					return map.get(priceColName);
				}
			}
		}
		return 0;

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
            // 不存在该编码的退库方
            errorMsg.append(PropertiesUtil.getText("STM00031")+PropertiesUtil.getText("PST00003"));
            errorMsg.append("！");
        }
        if (errorMap.get("departCodeFromNull") != null) {
            // 退库方编码不能为空
            errorMsg.append(PropertiesUtil.getText("PST00003")+PropertiesUtil.getText("STM00002"));
        }
        if (errorMap.get("departCodeToExits") != null) {
            // 不存在该编码的退库部门
            errorMsg.append(PropertiesUtil.getText("STM00031")+PropertiesUtil.getText("PST00003"));
            errorMsg.append("！");
        }
        if (errorMap.get("depotFromError") != null) {
            // 退库方不存在实体仓库
            errorMsg.append(PropertiesUtil.getText("PST00003")+PropertiesUtil.getText("STM00033"));
            errorMsg.append("！");
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
            // {退库}数量必须为整数，且长度不能超过9位，导入数量为：
            errorMsg.append(PropertiesUtil.getText("STM00035",new String[]{PropertiesUtil.getText("PST00035")}));
            errorMsg.append(ConvertUtil.getString(errorMap.get("quantity")));
            errorMsg.append("！");
        }
        
        if(errorMap.get("quantityStockError") != null) {
        	// {退库}数量必须小于当前库存数量，导入数量为：{1}，当前库存数量为：{2}
            errorMsg.append(PropertiesUtil.getText("STM00062",new String[]{PropertiesUtil.getText("PST00035"),ConvertUtil.getString(errorMap.get("quantity")),ConvertUtil.getString(errorMap.get("stockQuantity"))}));
            errorMsg.append("！");
        }
        
        if (errorMap.get("departNameFromError") != null) {
            // 退库方名称长度不能超过50个字符，导入名称为：
            errorMsg.append(PropertiesUtil.getText("PST00003")+PropertiesUtil.getText("STM00039"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("departNameFrom")));
            errorMsg.append("！");
        }
        if (errorMap.get("departNameAcceptError") != null) {
            // 接收方部门名称长度不能超过50个字符，导入名称为：
            errorMsg.append(PropertiesUtil.getText("PST00004")+PropertiesUtil.getText("STM00039"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("departNameTo")));
            errorMsg.append("！");
        }
        if (errorMap.get("productNameError") != null) {
            errorMsg.append(PropertiesUtil.getText("STM00009"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("productName")));
            errorMsg.append("！");
        }
        if (errorMap.get("departCodeFromLength") != null) {
            // 退库方编码长度不能超过15位，导入编码为：
            errorMsg.append(PropertiesUtil.getText("PST00018")+PropertiesUtil.getText("STM00040"));
            errorMsg.append(ConvertUtil.getString(errorMap.get("departCodeFrom")));
            errorMsg.append("！");
        }
        if (errorMap.get("departCodeToLength") != null) {
            // 接收退库部门编码长度不能超过15位，导入编码为：
            errorMsg.append(PropertiesUtil.getText("PST00004")+PropertiesUtil.getText("STM00040"));
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
            // 不存在此逻辑仓库或该类型仓库不能用于给该部门{退库}！
            errorMsg.append(PropertiesUtil.getText("STM00041",new String[]{PropertiesUtil.getText("PST00035")}));
        }
        if (errorMap.get("quantityNull") != null) {
            // 退库数量不能为0和空！
            errorMsg.append(PropertiesUtil.getText("STM00038",new String[]{PropertiesUtil.getText("PST00035")}));
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

    @Override
    public int getBillExcelCount(Map<String, Object> map) {
        return binOLSTIOS11_Service.getBillExcelCount(map);
    }

    @Override
    public List<Map<String, Object>> getBillExcelList(Map<String, Object> map) {
        return binOLSTIOS11_Service.getBillExcelList(map);
    }

    @Override
    public List<Map<String, Object>> getBillExcelDetailList(Map<String, Object> map) {
        return binOLSTIOS11_Service.getBillExcelDetailList(map);
    }

    @Override
    public Map<String, Object> getBillExcelInfo(Map<String, Object> map) {
        return binOLSTIOS11_Service.getBillExcelInfo(map);
    }

    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        String[][] array = { 
                { "importBatch", "IOS11_importBatch", "15", "", "" },
                { "unitCode", "IOS11_unitCode", "20", "", "" },
                { "barCode", "IOS11_barCode", "13", "", "" },
                { "nameTotal", "IOS11_nameTotal", "30", "", "" },
                { "departCodeFrom", "IOS11_departCodeFrom", "15", "", "" },
                { "departNameFrom", "IOS11_departNameFrom", "30", "", "" },
                { "departCodeTo", "IOS11_departCodeTo", "20", "", "" },
                { "departNameTo", "IOS11_departNameTo", "30", "", "" },
                { "importDate", "IOS11_importDate", "15", "", "" },
                { "price", "IOS11_price", "15", "", "" },
                { "quantity", "IOS11_quanlity", "15", "", "" },
                { "logicInventoryNameFrom", "IOS11_logicInventoryNameFrom", "30", "", "" },
                { "importResult", "IOS11_importResult", "15", "", "1250" },
                { "errorMsg", "IOS11_errorMsg", "50", "", "" }

        };
        int dataLen = binOLSTIOS11_Service.getExportDataCount(map);
        map.put("dataLen", dataLen);
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        map.put(CherryConstants.SORT_ID, "billNo");
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getExportDataList");
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLSTIOS11");
        ep.setShowTitleStyle(true);
        ep.setShowBorder(true);
        ep.setSheetLabel("sheetName");
        return binOLMOCOM01_BL.getBatchExportExcel(ep);
    }
}