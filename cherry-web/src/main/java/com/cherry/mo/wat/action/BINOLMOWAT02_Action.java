/*  
 * @(#)BINOLMOWAT02_Action.java     1.0 2011/5/11      
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.form.BINOLMOWAT02_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT02_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 异常销售数据监控Action
 * 
 * @author niushunjie
 * @version 1.0 2011.5.11
 */
public class BINOLMOWAT02_Action  extends BaseAction implements
ModelDriven<BINOLMOWAT02_Form>{

    private static final long serialVersionUID = 4261180958320105226L;
    
    //打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOWAT02_Action.class);

    /** 参数FORM */
    private BINOLMOWAT02_Form form = new BINOLMOWAT02_Form();
    
    /** 共通 */
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLMOWAT02_BL")
    private BINOLMOWAT02_IF binOLMOWAT02_BL;
    
    /** 销售异常柜台List */
    private List<Map<String, Object>> counterInfoList;
    
    /** 节日 */
    private String holidays;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
    
    @Override
    public BINOLMOWAT02_Form getModel() {
        return form;
    }

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
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 语言类型
        String language = (String) session
                .get(CherryConstants.SESSION_LANGUAGE);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, language);
        // 查询假日
        holidays = binOLCM00_BL.getHolidays(map);
        
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
          .getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        String organid = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        String brandid = String.valueOf(userInfo.getBIN_BrandInfoID());
        String maxLimit = binOLCM14_BL.getConfigValue("1022", organid, brandid);
        String minLimit = binOLCM14_BL.getConfigValue("1023",organid, brandid);
        //销售金额过大阈值
        form.setMaxLimit(maxLimit);
        //销售金额过小阈值
        form.setMinLimit(minLimit);
        
        String maxQuantity = binOLCM14_BL.getConfigValue("1024", organid, brandid);
        String minQuantity = binOLCM14_BL.getConfigValue("1025", organid, brandid);
        //销售数量过大阈值
        form.setMaxQuantity(maxQuantity);
        //销售数量过小阈值
        form.setMinQuantity(minQuantity);
        
        return SUCCESS;
    }
    
    /**
     * <p>
     * AJAX异常销售数据查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得柜台总数
        int count = binOLMOWAT02_BL.searchCounterInfoCount(searchMap);
        if (count > 0) {
            // 取得柜台List
            setCounterInfoList(binOLMOWAT02_BL.searchCounterInfoList(searchMap));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOWAT02_1";
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
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
            //品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //销售金额或销售数量标志
        //map.put("radioRule", form.getRadioRule());
        //最大金额
        map.put("maxLimit",form.getMaxLimit());
        //最小金额
        map.put("minLimit", form.getMinLimit());
        //最大数量
        map.put("maxQuantity", form.getMaxQuantity());
        //最小数量
        map.put("minQuantity", form.getMinQuantity());
        //开始日期
        map.put("startDate", form.getStartDate());
        //结束日期
        map.put("endDate", form.getEndDate());

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
        String searchStartDate = form.getStartDate();
        // 结束时间
        String searchEndDate = form.getEndDate();
        
        boolean errorFlag = false;
        // 如果起始日期不为空
        if (null!=searchStartDate && !"".equals(searchStartDate)){
            // 不符合日期格式
            if (!CherryChecker.checkDate(searchStartDate)){
                this.addActionError(getText("ECM00008",new String[]{getText("PMO00007")}));
                errorFlag = true;
            }
        }
        
        // 如果结束日期不为空
        if (null != searchEndDate && !"".equals(searchEndDate)){
            // 不符合日期格式
            if (!CherryChecker.checkDate(searchEndDate)){
                this.addActionError(getText("ECM00008",new String[]{getText("PMO00008")}));
                errorFlag = true;
            }
        }

        // 如果结束日期不为空
        if (!errorFlag && null!=searchStartDate && !"".equals(searchStartDate) && null != searchEndDate && !"".equals(searchEndDate)){
            // 结束日期小于起始日期
            if (CherryChecker.compareDate(searchEndDate, searchStartDate)<0){
                this.addActionError(getText("ECM00019",new String[]{getText("PMO00008")}));
                errorFlag = true;
            }
            
            String filterValue = form.getSSearch();
            if (filterValue.indexOf("not_start")<0 && filterValue.indexOf("in_progress")<0 && filterValue.indexOf("past_due")<0){
                String maxDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, searchStartDate, Integer.parseInt(MonitorConstants.MAX_Date));
                if (CherryChecker.compareDate(searchEndDate, maxDate)>0){
                    this.addActionError(getText("EMO00019",new String[]{MonitorConstants.MAX_Date}));
                    errorFlag = true;
                }
            }
        }
        String maxLimit = form.getMaxLimit();
        String minLimit = form.getMinLimit();
        String maxQuantity = form.getMaxQuantity();
        String minQuantity = form.getMinQuantity();
        if (!errorFlag && !CherryChecker.isNullOrEmpty(maxLimit) && !CherryChecker.isDecimal(maxLimit, 14, 2)){
        	// （金额）最大阈值应为浮点数(可为负数)
            this.addActionError(getText("ECM00024",new String[]{getText("PMO00009"),"14","2"}));
            errorFlag = true;
        }
        if (!errorFlag && !CherryChecker.isNullOrEmpty(minLimit) && !CherryChecker.isDecimal(minLimit, 14, 2)){
        	// （金额）最小阈值应为浮点数(可为负数)
            this.addActionError(getText("ECM00024",new String[]{getText("PMO00010"),"14","2"}));
            errorFlag = true;
        }
        if(!CherryChecker.isNullOrEmpty(maxLimit) && !CherryChecker.isNullOrEmpty(minLimit)){
            if (!errorFlag && Float.parseFloat(maxLimit) <= Float.parseFloat(minLimit)){
            	// （金额）最大阈值应大于最小阈值
                this.addActionError(getText("EMO00020"));
                errorFlag = true;
            }
        }
        if (!errorFlag && !CherryChecker.isNullOrEmpty(maxQuantity) && !CherryChecker.isPositiveAndNegative(maxQuantity)){
        	// （数量）最大阈值应为整数(可为负数)
        	this.addActionError(getText("ECM00021",new String[]{getText("PMO00011")}));
            errorFlag = true;
        }
        if (!errorFlag && !CherryChecker.isNullOrEmpty(minQuantity) && !CherryChecker.isPositiveAndNegative(minQuantity)){
        	// （数量）最大阈值应为整数(可为负数)
        	this.addActionError(getText("ECM00021",new String[]{getText("PMO00012")}));
            errorFlag = true;
        }
        if(!CherryChecker.isNullOrEmpty(maxQuantity) && !CherryChecker.isNullOrEmpty(minQuantity)){
            if (!errorFlag && Float.parseFloat(maxQuantity) <= Float.parseFloat(minQuantity)){
            	// （数量）最大阈值应大于最小阈值
                this.addActionError(getText("EMO00021"));
                errorFlag = true;
            }
        }

        if(!errorFlag && CherryChecker.isNullOrEmpty(maxLimit) && CherryChecker.isNullOrEmpty(minLimit) && CherryChecker.isNullOrEmpty(maxQuantity) && CherryChecker.isNullOrEmpty(minQuantity)){
            // 值不能为空
        	this.addActionError(getText("EMO00023"));
            errorFlag = true;
        }

    }
    
    /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得柜台List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMOWAT02", language, "downloadFileName");
            setExcelStream(new ByteArrayInputStream(binOLMOWAT02_BL.exportExcel(searchMap)));
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLMOWAT02_excel";
    }
    
    public void setCounterInfoList(List<Map<String, Object>> counterInfoList) {
        this.counterInfoList = counterInfoList;
    }

    public List<Map<String, Object>> getCounterInfoList() {
        return counterInfoList;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public InputStream getExcelStream() throws UnsupportedEncodingException {
        return excelStream;
    }

    public void setDownloadFileName(String downloadFileName) throws UnsupportedEncodingException {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
    	//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

}
