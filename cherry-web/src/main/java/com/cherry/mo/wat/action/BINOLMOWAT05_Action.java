/*  
 * @(#)BINOLMOWAT05_Action.java     1.0 2011/8/1      
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
import java.util.ArrayList;
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
import com.cherry.mo.wat.form.BINOLMOWAT05_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT05_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 考勤信息查询Action
 * 
 * @author niushunjie
 * @version 1.0 2011.8.1
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT05_Action  extends BaseAction implements
ModelDriven<BINOLMOWAT05_Form>{

    private static final long serialVersionUID = 145980601968101368L;

    /** 参数FORM */
    private BINOLMOWAT05_Form form = new BINOLMOWAT05_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    
    /** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOWAT05_IF binOLMOWAT05_BL;
    
    /** 节日 */
    private String holidays;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    /** 考勤信息List */
    private List attendanceInfoList;
    
    /**考勤员工信息MPA*/
    private Map attendanceEmpMap;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;

    /** 岗位类别信息List */
    private List positionCategoryList = new ArrayList();
    
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
        //总部的场合
        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
            // 取得品牌List
            brandInfoList = binOLCM05_BL.getBrandInfoList(map);
        }else{
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            // 取得岗位类别信息List
            positionCategoryList = binOLMOWAT05_BL.getPositionCategoryList(map);
        }

        // 查询假日
        setHolidays(binOLCM00_BL.getHolidays(map));
        // 开始日期
        form.setStartAttendanceDate(binOLCM00_BL.getFiscalDate(userInfo
          .getBIN_OrganizationInfoID(), new Date()));
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
        // 取得考勤统计信息查询总数
        int count = binOLMOWAT05_BL.getAttendanceCountNum(searchMap);
        if (count > 0) {
            // 取得考勤统计查询List
            attendanceInfoList = binOLMOWAT05_BL.getAttendanceCountList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOWAT05_1";
    }
    
    /**
     * 明细主画面
     * @return
     * @throws Exception
     */
    public String detail() throws Exception {
    	 Map<String, Object> map = new HashMap<String, Object>();
         map.put("employeeId", form.getEmployeeId());
         
         attendanceEmpMap = binOLMOWAT05_BL.getEmployeeInfoById(map);
         if(attendanceEmpMap == null) {
        	 attendanceEmpMap = new HashMap();
         }
         attendanceEmpMap.put("startAttendanceDate", form.getStartAttendanceDate());
         attendanceEmpMap.put("endAttendanceDate", form.getEndAttendanceDate());
         attendanceEmpMap.put("udiskSN", form.getUdiskSN());
         return "BINOLMOWAT05_2";
    }
    
    /**
     * 明细一览
     * @return
     * @throws Exception
     */
    public String detailList() throws Exception {
    	Map<String, Object> map = this.getListSearchMap();
    	// 取得考勤明细总数
        int count = binOLMOWAT05_BL.getAttendanceInfoCount(map);
        if (count > 0) {
            // 取得考勤明细List
            attendanceInfoList = binOLMOWAT05_BL.getAttendanceInfoList(map);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
    	return "BINOLMOWAT05_3";
    }
    
    private Map<String, Object> getListSearchMap() throws JSONException {
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
        map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        // 组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 员工ID
        map.put("employeeId", form.getEmployeeId());
        // 开始时间
        map.put("startAttendanceDate", form.getStartAttendanceDate());
        // 结束时间
        map.put("endAttendanceDate", form.getEndAttendanceDate());
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        return map;
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
        //U盘序列号
        map.put("udiskSN", form.getUdiskSN());
        //员工姓名
        map.put("employeeName", form.getEmployeeName());
        //岗位
        map.put("positionCategoryId", form.getPositionCategoryId());
        //考勤日期开始
        map.put("startAttendanceDate", form.getStartAttendanceDate());
        //考勤日期结束
        map.put("endAttendanceDate", form.getEndAttendanceDate());
        // 是否包含测试部门
        map.put("testFlag", form.getTestFlag());
        // 是否包含停用部门
        map.put("validFlag", form.getValidFlag());
        
        // 主页面负责统计巡柜数与巡柜次数，不再区分组织进行统计【加入人员权限控制】
        // 业务类型
        map.put(CherryConstants.BUSINESS_TYPE, "0");
        // 操作类型
        map.put(CherryConstants.OPERATION_TYPE, "1");
        // 是否带权限查询
        map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
        
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
     * AJAX 取得岗位类别并返回JSON对象
     * 
     * @throws Exception
     */
    public void queryPositionCategory() throws Exception {
        Map<String, Object> map = new HashMap();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        // 品牌
        if(null!=form.getBrandInfoId() && !"".equals(form.getBrandInfoId())){
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        }else{
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }

        // 取得岗位分类List
        List positionCategory = binOLMOWAT05_BL.getPositionCategoryList(map);
        // 响应JSON对象
        ConvertUtil.setResponseByAjax(response, positionCategory);
    }
    
    /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
    	 // 是否为主页面一览明细导出
        String flag = ConvertUtil.getString(form.getFlag());
        // 取得参数MAP
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("flag", flag);
        if("".equals(flag)) {
        	// 明细页面当中的导出功能
        	searchMap.putAll(this.getListSearchMap());
        } else {
        	// 此处为主页导出功能（包括明细与统计信息的导出）
        	searchMap.putAll(this.getSearchMap());
        }
        // 取得考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMOWAT05", language, "downloadFileName"+("".equals(flag) ? "1" : flag));
            if("1".equals(flag) || "".equals(flag)) {
            	// 明细的导出
            	setExcelStream(new ByteArrayInputStream(binOLMOWAT05_BL.exportExcel(searchMap)));
            } else if("2".equals(flag)) {
            	// 统计信息的导出
            	setExcelStream(new ByteArrayInputStream(binOLMOWAT05_BL.exportCountExcel(searchMap)));
            }
            
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLMOWAT05_excel";
    }
    
    @Override
    public BINOLMOWAT05_Form getModel() {
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

    public void setPositionCategoryList(List positionCategoryList) {
        this.positionCategoryList = positionCategoryList;
    }

    public List getPositionCategoryList() {
        return positionCategoryList;
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

	public Map getAttendanceEmpMap() {
		return attendanceEmpMap;
	}

	public void setAttendanceEmpMap(Map attendanceEmpMap) {
		this.attendanceEmpMap = attendanceEmpMap;
	}
}
