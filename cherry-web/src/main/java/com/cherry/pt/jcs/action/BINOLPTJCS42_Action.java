/*  
 * @(#)BINOLPTJCS42_Action.java     1.0 2015/01/19      
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
package com.cherry.pt.jcs.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.jcs.form.BINOLPTJCS42_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS42_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品信息二维码维护Action
 * 
 * @author niushunjie
 * @version 1.0 2015.01.19
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS42_Action  extends BaseAction implements ModelDriven<BINOLPTJCS42_Form>{

    private static final long serialVersionUID = 7390056037480912034L;

    private static Logger logger = LoggerFactory.getLogger(BINOLPTJCS42_Action.class);

    /** 参数FORM */
    private BINOLPTJCS42_Form form = new BINOLPTJCS42_Form();
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
    
    /** 共通 */
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLPTJCS42_BL")
    private BINOLPTJCS42_IF binOLPTJCS42_BL;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    /** 产品信息二维码List */
    private List productQRCodeList;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, language);
        //总部的场合
        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
            // 取得品牌List
            brandInfoList = binOLCM05_BL.getBrandInfoList(map);
        }else{
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
        if(null != brandInfoList && brandInfoList.size()>0){
            paramMap.put("BIN_BrandInfoID", brandInfoList.get(0).get(CherryConstants.BRANDINFOID));
        }else{
            paramMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
        }
        List<Map<String,Object>> resellerList = binOLPTJCS42_BL.getResellerList(paramMap);
        form.setResellerList(resellerList);

        return SUCCESS;
    }
    
    /**
     * <p>
     * AJAX产品信息二维码查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得BA考勤信息查询总数
        int count = binOLPTJCS42_BL.getProductQRCodeCount(searchMap);
        if (count > 0) {
            // 取得BA考勤信息查询List
            productQRCodeList = binOLPTJCS42_BL.getProductQRCodeList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLPTJCS42_1";
    }
    
    public String reGenerate() throws Exception{
        try{
            // 用户信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
            paramMap.put(CherryConstants.USERID, userInfo.getBIN_UserID());
            paramMap.put("BaseURL", form.getPrefixURL());
            paramMap.put("BIN_ResellerInfoID", form.getResellerId());
            binOLPTJCS42_BL.tran_reGenerate(paramMap);
            this.addActionMessage(getText("ICM00002"));
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                this.addActionError(e.getMessage());
            } 
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;

    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     * @throws JSONException 
     */
    private Map<String, Object> getSearchMap() throws JSONException {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 所属品牌不存在的场合
        if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
            // 不是总部的场合
            if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
                // 所属品牌
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            }
        } else {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        }
        //组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        //产品厂商ID
        map.put("prtVendorId", form.getPrtVendorId());
        //产品名称
        map.put("productName", form.getProductName());
        //经销商Code
        map.put("resellerCode", form.getResellerCode());
        //经销商名称
        map.put("resellerName", form.getResellerName());
        //有效区分
        map.put("validFlag", form.getValidFlag());

        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
    }
    
    /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得产品信息二维码List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLPTJCS42", language, "downloadFileName");
            downloadFileName = fileName +".zip";
            setExcelStream(new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTJCS42_BL.exportExcel(searchMap), fileName+".xls")));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLPTJCS42_excel";
    }
    
    /**
     * 通过Ajax取得指定品牌所拥有的经销商
     * @throws Exception
     */
    public void getResellerList() throws Exception{
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        paramMap.put("BIN_BrandInfoID", form.getBrandInfoId());
        List<Map<String,Object>> list = binOLPTJCS42_BL.getResellerList(paramMap);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    @Override
    public BINOLPTJCS42_Form getModel() {
        return form;
    }
    
    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
    	//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public List getProductQRCodeList() {
        return productQRCodeList;
    }

    public void setProductQRCodeList(List productQRCodeList) {
        this.productQRCodeList = productQRCodeList;
    }
}