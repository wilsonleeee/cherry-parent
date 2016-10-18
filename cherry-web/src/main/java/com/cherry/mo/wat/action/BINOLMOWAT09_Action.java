/*  
 * @(#)BINOLMOWAT09_Action.java     1.0 2014/12/17      
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
package com.cherry.mo.wat.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.form.BINOLMOWAT09_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT09_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * BA考勤信息查询Action
 * 
 * @author niushunjie
 * @version 1.0 2014.12.17
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT09_Action  extends BaseAction implements ModelDriven<BINOLMOWAT09_Form>{

    private static final long serialVersionUID = -8453887345143158487L;

    /** 参数FORM */
    private BINOLMOWAT09_Form form = new BINOLMOWAT09_Form();
    
    /** 共通BL */
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    /** 共通 */
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLMOWAT09_BL")
    private BINOLMOWAT09_IF binOLMOWAT09_BL;
    
    /** 节日 */
    private String holidays;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    /** BA考勤信息List */
    private List attendanceInfoList;
    
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

        // 查询假日
        setHolidays(binOLCM00_BL.getHolidays(map));
        // 开始日期
        form.setStartAttendanceDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndAttendanceDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));

        return SUCCESS;
    }
    
    /**
     * <p>
     * AJAX考勤信息查询查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得BA考勤信息查询总数
        int count = binOLMOWAT09_BL.getAttendanceInfoCount(searchMap);
        if (count > 0) {
            // 取得BA考勤信息查询List
            attendanceInfoList = binOLMOWAT09_BL.getAttendanceInfoList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOWAT09_1";
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
        //BA编号
        map.put("baCode", form.getBaCode());
        //BA姓名
        map.put("baName", form.getBaName());
        //考勤日期开始
        map.put("startAttendanceDate", form.getStartAttendanceDate());
        //考勤日期结束
        map.put("endAttendanceDate", form.getEndAttendanceDate());

        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
    }

    /**
     * 查询校验
     */
    public void validateSearch(){
        // 起始时间
        String searchStartDate = form.getStartAttendanceDate();
        // 结束时间
        String searchEndDate = form.getEndAttendanceDate();

        if(null != searchStartDate && !"".equals(searchStartDate) && null != searchEndDate && !"".equals(searchEndDate)) {
            // 结束日期小于起始日期
            if (CherryChecker.compareDate(searchEndDate, searchStartDate)<0){
                this.addActionError(getText("ECM00019",new String[]{getText("PMO00008")}));
            }
        }
    }
    
    /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得BA考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMOWAT09", language, "downloadFileName");
            setExcelStream(new ByteArrayInputStream(binOLMOWAT09_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLMOWAT09_excel";
    }
    
    @Override
    public BINOLMOWAT09_Form getModel() {
        return form;
    }
    
    public void setAttendanceInfoList(List attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }

    public List getAttendanceInfoList() {
        return attendanceInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getHolidays() {
        return holidays;
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
}