/*  
 * @(#)BINOLMOWAT01_Action.java     1.0 2011/4/27      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.form.BINOLMOWAT01_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 终端实时监控Action
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT01_Action  extends BaseAction implements
ModelDriven<BINOLMOWAT01_Form>{

    private static final long serialVersionUID = 3990565708573842846L;

    /** 参数FORM */
    private BINOLMOWAT01_Form form = new BINOLMOWAT01_Form();
    
    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOWAT01_Action.class);
    
    /** 共通BL */
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLMOWAT01_BL")
    private BINOLMOWAT01_IF binOLMOWAT01_BL;
    
    /** 机器List */
    private List<Map<String, Object>> machineInfoList;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;

    /** 连接数统计 */
    private Map<String, Object> connectInfo;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
    
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
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            //总部的场合
            if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
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
    
                // 取得品牌List
                brandInfoList = binOLCM05_BL.getBrandInfoList(map);
            }
            // 查询假日
            form.setHolidays(binOLCM00_BL.getHolidays(map));
            form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
                    .getBIN_OrganizationInfoID(), new Date()));
            form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }

        return SUCCESS;
    }
    
    /**
     * <p>
     * AJAX终端实时监控查询
     * 特殊情况：
     * 			对于WITPOSIII,WITSERVER,WITPOSIV类型的机器，
     * 			需要过滤掉新后台机器对照表中末位码为‘9’的数据【否则会得到重复数据】
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        try{
            // 取得参数MAP
            Map<String, Object> searchMap = getSearchMap();
            // 取得机器总数
            int count = binOLMOWAT01_BL.searchMachineInfoCount(searchMap);
            if (count > 0) {
                searchMap.put("machineCount", count);
                // 取得机器List
                machineInfoList = binOLMOWAT01_BL.searchMachineInfoList(searchMap);
                setConnectInfo(binOLMOWAT01_BL.getSumInfo(searchMap));
            }
            // form表单设置
            form.setITotalDisplayRecords(count);
            form.setITotalRecords(count);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
        // AJAX返回至dataTable结果页面
        return "BINOLMOWAT01_1";
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     */
    private Map<String, Object> getSearchMap() {
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
        // 所属品牌不存在的场合
        if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
            // 不是总部的场合
            if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
                // 所属品牌
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
                map.put("BrandCode", userInfo.getBrandCode());
            }
        } else {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
            map.put("BrandCode", binOLCM05_BL.getBrandCode(CherryUtil.obj2int(form.getBrandInfoId())));
        }
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        map.put("OrgCode", userInfo.getOrganizationInfoCode());
        //最后联络时差（分）
        map.put("dateDiff", form.getDateDiff());
        //WITPOSIII
        map.put("WITPOSIII", MonitorConstants.MachineType_WITPOSIII);
        //WITSERVER
        map.put("WITSERVER", MonitorConstants.MachineType_WITSERVER);
        //WITPOSIV
        map.put("WITPOSIV", MonitorConstants.MachineType_WITPOSIV);
        //搜索类型
        map.put("searchType", form.getSearchType());
        //天数
        map.put("day", form.getNuberOfDays());
        //开始日期
        map.put("startDate", form.getStartDate());
        //结束日期
        map.put("endDate", form.getEndDate());
        //模糊查询机器编号
        map.put("machineCode", form.getMachineCode());
        //模糊查询柜台编号或名称
        map.put("counterCodeName", form.getCounterCodeName());
        return map;
    }

    /**
     * 导出Excel
     */
    public String export(){
        try {
            // 取得参数MAP
            Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            setDownloadFileName(binOLMOCOM01_BL.getResourceValue("BINOLMOWAT01", language, "downloadFileName"));
            setExcelStream(new ByteArrayInputStream(binOLMOWAT01_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //导出Excel失败
                this.addActionError(getText("EMO00022"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
        return "BINOLMOWAT01_excel";
    }
    
    @Override
    public BINOLMOWAT01_Form getModel() {
        return form;
    }
    
    public void setMachineInfoList(List<Map<String, Object>> machineInfoList) {
        this.machineInfoList = machineInfoList;
    }

    public List<Map<String, Object>> getMachineInfoList() {
        return machineInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
    	//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

    public Map<String, Object> getConnectInfo() {
        return connectInfo;
    }

    public void setConnectInfo(Map<String, Object> connectInfo) {
        this.connectInfo = connectInfo;
    }
    
    /**
     * 查询校验
     */
    public void validateSearch(){
        String searchType = form.getSearchType();
        if(searchType.equals("date")){
            // 开始日期
            String searchStartDate = ConvertUtil.getString(form.getStartDate());
            // 结束日期
            String searchEndDate = ConvertUtil.getString(form.getEndDate());
            
            // 如果开始日期为空
            if ("".equals(searchStartDate)){
                this.addActionError(getText("ECM00009",new String[]{getText("PCM00001")}));
                return;
            }
            // 不符合日期格式
            if(!CherryChecker.checkDate(searchStartDate)){
                this.addActionError(getText("ECM00008",new String[]{getText("PCM00001")}));
                return;
            }
            //符合日期格式但格式不是yyyy-MM-dd需要转一下。
            searchStartDate = DateUtil.date2String(DateUtil.coverString2Date(searchStartDate));
            form.setStartDate(searchStartDate);
            
            // 如果结束日期为空
            if ("".equals(searchEndDate)){
                this.addActionError(getText("ECM00009",new String[]{getText("PCM00002")}));
                return;
            }
            // 不符合日期格式
            if(!CherryChecker.checkDate(searchEndDate)){
                this.addActionError(getText("ECM00008",new String[]{getText("PCM00002")}));
                return;
            }
            //符合日期格式但格式不是yyyy-MM-dd需要转一下。
            searchEndDate = DateUtil.date2String(DateUtil.coverString2Date(searchEndDate));
            form.setEndDate(searchEndDate);
            
            // 结束日期小于起始日期
            if (CherryChecker.compareDate(searchEndDate, searchStartDate)<0){
                this.addActionError(getText("ECM00019"));
                return;
            }
        }
    }
}
