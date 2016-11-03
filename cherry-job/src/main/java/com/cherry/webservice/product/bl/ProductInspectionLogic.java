/*  
 * @(#)ProductInspectionLogic.java     1.0 2015/01/13      
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

package com.cherry.webservice.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.webservice.product.interfaces.ProductInspection_IF;
import com.cherry.webservice.product.service.ProductInspectionService;

/**
 * 
 * 查货宝业务 BL
 * 
 * @author niushunjie
 * @version 1.0 2014.12.11
 */
public class ProductInspectionLogic implements ProductInspection_IF{
   
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="productInspectionService")
    private ProductInspectionService productInspectionService;
    
    private static Logger logger = LoggerFactory.getLogger(ProductInspectionLogic.class.getName());
    
    /**
     * 货品查询及记录
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> tran_productInspection(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try{
            String ac = ConvertUtil.getString(paramMap.get("AC"));
            if(ac.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "AC必填。");
                return retMap;
            }
            String channel = ConvertUtil.getString(paramMap.get("Channel"));
            if(channel.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "Channel必填。");
                return retMap;
            }
            String employeeCode = ConvertUtil.getString(paramMap.get("EmployeeCode"));
            if(employeeCode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "EmployeeCode必填。");
                return retMap;
            }
            
            Map<String,Object> resultMap = new HashMap<String,Object>();
            
            int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
            int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
            Map<String,Object> searchParam = new HashMap<String,Object>();
            searchParam.put("BIN_OrganizationInfoID", organizationInfoID);
            searchParam.put("BIN_BrandInfoID", brandInfoID);
            searchParam.put("QRCodeCiphertext", ac);
            List<Map<String,Object>> productQRCodeList = productInspectionService.getProductQRCode(searchParam);
            if(null != productQRCodeList && productQRCodeList.size()>0){
                Map<String,Object> productQRCode =productQRCodeList.get(0);
                if(null != productQRCode && !productQRCode.isEmpty()){
                    resultMap.put("UnitCode", ConvertUtil.getString(productQRCode.get("UnitCode")));
                    resultMap.put("BarCode", ConvertUtil.getString(productQRCode.get("BarCode")));
                    resultMap.put("ProductName", ConvertUtil.getString(productQRCode.get("ProductName")));
                    resultMap.put("Time", ConvertUtil.getString(productQRCode.get("Time")));
                    resultMap.put("ResellerName", ConvertUtil.getString(productQRCode.get("ResellerName")));
                    
                    searchParam.put("EmployeeCode", employeeCode);
                    String employeeID = "";
                    Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(searchParam, false);
                    if(null != employeeInfo && !employeeInfo.isEmpty()){
                        employeeID = ConvertUtil.getString(employeeInfo.get("BIN_EmployeeID"));
                    }
                    
                    //查出结果后写入记录表
                    Map<String,Object> insertParam = new HashMap<String,Object>();
                    insertParam.put("BIN_OrganizationInfoID", organizationInfoID);
                    insertParam.put("BIN_BrandInfoID", brandInfoID);
                    insertParam.put("AC", ac);
                    insertParam.put("ProductType", "N");
                    insertParam.put("BIN_ProductVendorID", productQRCode.get("BIN_ProductVendorID"));
                    insertParam.put("BIN_ResellerInfoID", productQRCode.get("BIN_ResellerInfoID"));
                    insertParam.put("ChannelName", channel);
                    insertParam.put("BIN_EmployeeID", employeeID);
                    insertParam.put("ScanTime", productQRCode.get("Time"));
                    insertParam.put("CreatedBy", "ProductInspectionLogic");
                    insertParam.put("CreatePGM", "ProductInspectionLogic");
                    insertParam.put("UpdatedBy", "ProductInspectionLogic");
                    insertParam.put("UpdatePGM", "ProductInspectionLogic");

                    productInspectionService.insertProductQRCodeRecord(insertParam);
                }
            }

            retMap.put("ResultMap", resultMap);

            return retMap;
        }catch(Exception ex){
            try{
                //新后台品牌数据库回滚
                productInspectionService.manualRollback();
            }catch(Exception e){
                
            }
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }

    @Override
    public Map<String, Object> getProductInspectionList(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try{
            String startTime = ConvertUtil.getString(paramMap.get("StartTime"));
            if(startTime.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "StartTime必填。");
                return retMap;
            }
            String endTime = ConvertUtil.getString(paramMap.get("EndTime"));
            if(endTime.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "EndTime必填。");
                return retMap;
            }
            String channel = ConvertUtil.getString(paramMap.get("Channel"));
            if(channel.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "Channel必填。");
                return retMap;
            }
            String employeeCode = ConvertUtil.getString(paramMap.get("EmployeeCode"));
            if(employeeCode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "EmployeeCode必填。");
                return retMap;
            }
                        
            int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
            int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
            Map<String,Object> searchParam = new HashMap<String,Object>();
            searchParam.put("BIN_OrganizationInfoID", organizationInfoID);
            searchParam.put("BIN_BrandInfoID", brandInfoID);
            searchParam.put("StartTime", startTime);
            searchParam.put("EndTime", endTime);
            searchParam.put("ChannelName", channel);
            if(!employeeCode.equals("") && !employeeCode.toUpperCase().equals("ALL")){
                searchParam.put("EmployeeCode", employeeCode);
            }
            List<Map<String,Object>> resultContent = new ArrayList<Map<String,Object>>();
            List<Map<String,Object>> productQRCodeRecordList =productInspectionService.getProductQRCodeRecord(searchParam);
            if(null != productQRCodeRecordList && productQRCodeRecordList.size()>0){
                for(int i=0;i<productQRCodeRecordList.size();i++){
                    Map<String,Object> productQRCodeRecord =productQRCodeRecordList.get(i);
                    if(null != productQRCodeRecord && !productQRCodeRecord.isEmpty()){
                        Map<String,Object> resultContentMap = new HashMap<String,Object>();
                        resultContentMap.put("RecordID", ConvertUtil.getString(productQRCodeRecord.get("BIN_ProductQRCodeRecordID")));
                        resultContentMap.put("UnitCode", ConvertUtil.getString(productQRCodeRecord.get("UnitCode")));
                        resultContentMap.put("BarCode", ConvertUtil.getString(productQRCodeRecord.get("BarCode")));
                        resultContentMap.put("ProductName", ConvertUtil.getString(productQRCodeRecord.get("ProductName")));
                        resultContentMap.put("ResellerName", ConvertUtil.getString(productQRCodeRecord.get("ResellerName")));
                        resultContentMap.put("Time", ConvertUtil.getString(productQRCodeRecord.get("Time")));
                        resultContentMap.put("Channel", ConvertUtil.getString(productQRCodeRecord.get("ChannelName")));
                        resultContentMap.put("EmployeeName", ConvertUtil.getString(productQRCodeRecord.get("EmployeeName")));
                        resultContent.add(resultContentMap);
                    }
                }
            }

            retMap.put("ResultContent", resultContent);

            return retMap;
        }catch(Exception ex){
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }

    @Override
    public Map<String, Object> getProductInspectionReport(Map<String, Object> paramMap) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try{
            String startTime = ConvertUtil.getString(paramMap.get("StartTime"));
            if(startTime.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "StartTime必填。");
                return retMap;
            }
            String endTime = ConvertUtil.getString(paramMap.get("EndTime"));
            if(endTime.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "EndTime必填。");
                return retMap;
            }
            String employeeCode = ConvertUtil.getString(paramMap.get("EmployeeCode"));
            if(employeeCode.equals("")){
                retMap.put("ERRORCODE", "WSE9993");
                retMap.put("ERRORMSG", "EmployeeCode必填。");
                return retMap;
            }
                        
            int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
            int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
            Map<String,Object> searchParam = new HashMap<String,Object>();
            searchParam.put("BIN_OrganizationInfoID", organizationInfoID);
            searchParam.put("BIN_BrandInfoID", brandInfoID);
            searchParam.put("StartTime", startTime);
            searchParam.put("EndTime", endTime);
            if(!employeeCode.equals("") && !employeeCode.toUpperCase().equals("ALL")){
                searchParam.put("EmployeeCode", employeeCode);
            }
            List<Map<String,Object>> resultContent = new ArrayList<Map<String,Object>>();
            List<Map<String,Object>> productQRCodeRecordReportList =productInspectionService.getProductQRCodeRecordReport(searchParam);
            if(null != productQRCodeRecordReportList && productQRCodeRecordReportList.size()>0){
                for(int i=0;i<productQRCodeRecordReportList.size();i++){
                    Map<String,Object> productQRCodeRecordReport =productQRCodeRecordReportList.get(i);
                    if(null != productQRCodeRecordReport && !productQRCodeRecordReport.isEmpty()){
                        Map<String,Object> resultContentMap = new HashMap<String,Object>();
                        resultContentMap.put("Channel", ConvertUtil.getString(productQRCodeRecordReport.get("ChannelName")));
                        resultContentMap.put("Sum", ConvertUtil.getString(productQRCodeRecordReport.get("SumQuantity")));
                        resultContent.add(resultContentMap);
                    }
                }
            }

            retMap.put("ResultContent", resultContent);

            return retMap;
        }catch(Exception ex){
            logger.error("WS ERROR:", ex);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
            retMap.put("ERRORCODE", "WSE9999");
            retMap.put("ERRORMSG", "处理过程中发生未知异常。");
            return retMap;
        }
    }
}