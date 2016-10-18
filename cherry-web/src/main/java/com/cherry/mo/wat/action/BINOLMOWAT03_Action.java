/*  
 * @(#)BINOLMOWAT03_Action.java     1.0 2011/6/24      
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
import com.cherry.mo.wat.form.BINOLMOWAT03_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT03_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 会员异常数据监控Action
 * 
 * @author niushunjie
 * @version 1.0 2011.6.24
 */
public class BINOLMOWAT03_Action  extends BaseAction implements
ModelDriven<BINOLMOWAT03_Form>{

    private static final long serialVersionUID = 7983649569540657285L;

    /** 参数FORM */
    private BINOLMOWAT03_Form form = new BINOLMOWAT03_Form();

    /** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLCM00_BL binOLCM00_BL;

    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource
    private BINOLMOWAT03_IF binOLMOWAT03_BL;

    /** 会员异常数据List */
    private List<Map<String, Object>> memberInfoList;

    /** 节日 */
    private String holidays;

    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;

    @Override
    public BINOLMOWAT03_Form getModel() {
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
        String maxCount = binOLCM14_BL.getConfigValue("1026", organid, brandid);
        //购买次数过大阈值
        form.setMaxCount(maxCount);

        return SUCCESS;
    }

    /**
     * <p>
     * AJAX会员异常数据查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得会员异常数据总数
        int count = binOLMOWAT03_BL.searchMemberInfoCount(searchMap);
        if (count > 0) {
            // 取得会员异常数据List
            setMemberInfoList(binOLMOWAT03_BL.searchMemberInfoList(searchMap));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOWAT03_1";
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
        //最大购买次数
        map.put("maxCount",form.getMaxCount());
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

        String maxLimit = form.getMaxCount();
        if(!errorFlag && CherryChecker.isNullOrEmpty(maxLimit)){
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
        // 取得会员异常数据List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMOWAT03", language, "downloadFileName");
            setExcelStream(new ByteArrayInputStream(binOLMOWAT03_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLMOWAT03_excel";
    }

    public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

    public List<Map<String, Object>> getMemberInfoList() {
        return memberInfoList;
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
