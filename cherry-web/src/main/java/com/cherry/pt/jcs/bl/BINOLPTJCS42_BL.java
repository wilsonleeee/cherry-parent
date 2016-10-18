/*
 * @(#)BINOLPTJCS42_BL.java     1.0 2015/01/19
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
package com.cherry.pt.jcs.bl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS42_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS42_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.webservice.common.WebserviceDataSource;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 产品信息二维码维护BL
 * 
 * @author niushunjie
 * @version 1.0 2015.01.19
 */
public class BINOLPTJCS42_BL  extends SsBaseBussinessLogic implements BINOLPTJCS42_IF{

    @Resource(name = "webserviceDataSource")
    private WebserviceDataSource webserviceDataSource;
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLPTJCS42_Service")
    private BINOLPTJCS42_Service binOLPTJCS42_Service;
    
    
    /**
     * 取得产品信息二维码总数
     * 
     * @param map
     * @return 产品信息二维码总数
     */
    @Override
    public int getProductQRCodeCount(Map<String, Object> map) {
        return binOLPTJCS42_Service.getProductQRCodeCount(map);
    }

    /**
     * 取得产品信息二维码List
     * 
     * @param map
     * @return 产品信息二维码List
     */
    @Override
    public List<Map<String, Object>>getProductQRCodeList(Map<String, Object> map) {
        return binOLPTJCS42_Service.getProductQRCodeList(map);
    }

    /**
     * 取得品牌经销商List
     * 
     * @param map
     * @return 品牌经销商List
     */
    @Override
    public List<Map<String, Object>>getResellerList(Map<String, Object> map) {
        return binOLPTJCS42_Service.getAllResellerList(map);
    }
    
    /**
     * 导出产品信息二维码Excel
     * 
     * @param map
     * @return 返回导出产品信息二维码List
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLPTJCS42_Service.getProductQRCodeListExcel(map);
        String[][] array = {
                { "productName", "JCS42_productName", "20", "", "" },
                { "unitCode", "JCS42_unitCode", "20", "", "" },
                { "barCode", "JCS42_barCode", "20", "", "" },
                { "resellerCodeName", "JCS42_resellerName", "40", "", "" },
                { "qrCodeCiphertext", "JCS42_qrCodeCiphertext", "150", "", "" },
                { "wholeURL", "JCS42_wholeURL", "200", "", "" },
                { "validFlag", "JCS42_validFlag", "15", "", "1137" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLPTJCS42");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }

    @Override
    public int tran_reGenerate(Map<String, Object> map) throws Exception {
        String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
        String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
        String brandCode = binOLCM05_BL.getBrandCode(ConvertUtil.getInt(brandInfoId));
        String userID = ConvertUtil.getString(map.get(CherryConstants.USERID));
        String baseURL = ConvertUtil.getString(map.get("BaseURL")).trim();
        String resellerInfoID = ConvertUtil.getString(map.get("BIN_ResellerInfoID"));
        if(baseURL.indexOf("?")>-1){
            baseURL += "&ak=chb&ac=";
        }else{
            baseURL += "?ak=chb&ac=";
        }
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", organizationInfoId);
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BIN_ResellerInfoID", resellerInfoID);
        List<Map<String,Object>> allProductList = binOLPTJCS42_Service.getAllProductList(paramMap);
        List<Map<String,Object>> allResellerList = binOLPTJCS42_Service.getAllResellerList(paramMap);
        
        //查询AES密钥
        String aesKey = webserviceDataSource.getAESKey(brandCode);
        if(CherryChecker.isNullOrEmpty(aesKey)){
            throw new CherryException("ECM00106");
        }
        
        //插入前先删除临时表数据
        binOLPTJCS42_Service.deleteProductQRCodeTemp(paramMap);
        
        if(null != allResellerList && allResellerList.size()>0 && null != allProductList && allProductList.size()>0){
            for(int i=0;i<allResellerList.size();i++){
                List<Map<String,Object>> qrCodeList = new ArrayList<Map<String,Object>>();
                Map<String,Object> resellerDTO = allResellerList.get(i);
                for(int j=0;j<allProductList.size();j++){
                    Map<String,Object> productDTO = allProductList.get(j);
                    Map<String,Object> insertMap = new HashMap<String,Object>();
                    insertMap.put("BIN_OrganizationInfoID", organizationInfoId);
                    insertMap.put("BIN_BrandInfoID", brandInfoId);
                    insertMap.put("ProductType", "N");//暂时都是产品
                    insertMap.put("BIN_ProductVendorID", productDTO.get("BIN_ProductVendorID"));
                    insertMap.put("BIN_ResellerInfoID", resellerDTO.get("BIN_ResellerInfoID"));
                    String qrCodePlaintext = getQRCodePlaintext(resellerDTO,productDTO);
                    insertMap.put("QRCodePlaintext", qrCodePlaintext);
                    String qrCodeCiphertext = getQRCodeCiphertext(aesKey,qrCodePlaintext);
                    insertMap.put("QRCodeCiphertext", qrCodeCiphertext);
                    insertMap.put("WholeURL", baseURL+qrCodeCiphertext);
                    insertMap.put("CreatedBy", userID);
                    insertMap.put("CreatePGM", "BINOLPTJCS42_BL");
                    insertMap.put("UpdatedBy", userID);
                    insertMap.put("UpdatePGM", "BINOLPTJCS42_BL");

                    qrCodeList.add(insertMap);
                }
                //插入临时表
                binOLPTJCS42_Service.insertProductQRCodeTemp(qrCodeList);
            }
        }
        //用MERGE 处理产品信息二维码表
        //目标表存在记录，更新产品信息二维码表
        //目标表不存在记录，插入产品信息二维码表
        //来源表不存在记录，产品信息二维码表的记录设为无效，（防止产品、经销商无效后，再次生成后，无法查到记录）
        Map<String,Object> margeMap = new HashMap<String,Object>();
        margeMap.put("BIN_OrganizationInfoID", organizationInfoId);
        margeMap.put("BIN_BrandInfoID", brandInfoId);
        margeMap.put("BIN_ResellerInfoID", resellerInfoID);
        margeMap.put("CreatedBy", userID);
        margeMap.put("CreatePGM", "BINOLPTJCS42_BL");
        margeMap.put("UpdatedBy", userID);
        margeMap.put("UpdatePGM", "BINOLPTJCS42_BL");
        int cnt = binOLPTJCS42_Service.mergeProductQRCode(margeMap);
        
        return cnt;
    }

    @Override
    public String getQRCodePlaintext(Map<String, Object> resellerInfo, Map<String, Object> productInfo) {
        String qrCodePlaintext = "";
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("U", ConvertUtil.getString(productInfo.get("UnitCode")));
        jsonMap.put("B", ConvertUtil.getString(productInfo.get("BarCode")));
        jsonMap.put("PID", ConvertUtil.getString(productInfo.get("BIN_ProductID")));
        jsonMap.put("RC", ConvertUtil.getString(resellerInfo.get("ResellerCode")));
        try {
            qrCodePlaintext = JSONUtil.serialize(jsonMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qrCodePlaintext;
    }

    @Override
    public String getQRCodeCiphertext(String key,String plaintext) throws Exception {
        //AES加密
        String qrCodeCiphertext = CherryAESCoder.encrypt(plaintext, key);
        //把密文进行URL转码
        qrCodeCiphertext = URLEncoder.encode(qrCodeCiphertext, "UTF-8");
        return qrCodeCiphertext;
    }
}