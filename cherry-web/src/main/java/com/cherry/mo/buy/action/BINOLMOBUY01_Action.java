package com.cherry.mo.buy.action;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.buy.form.BINOLMOBUY01_Form;
import com.cherry.mo.buy.interfaces.BINOLMOBUY01_IF;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.interfaces.BINOLMOWAT05_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/*  
 * @(#)BINOLMOBUY01_Action.java    1.0 2012-5-28     
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
public class BINOLMOBUY01_Action extends BaseAction implements
ModelDriven<BINOLMOBUY01_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLMOBUY01_Form form = new BINOLMOBUY01_Form();
	
	@Resource
	private BINOLMOBUY01_IF binOLMOBUY01_BL;
	
	/** 共通BL */
    @Resource
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource
    private BINOLMOWAT05_IF binOLMOWAT05_BL;
    
    /** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	/** 节日 */
    private String holidays;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
    
    /** 考勤统计信息List*/
    private List udiskAttendanceStatisticsList;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;

    /** 岗位类别信息List */
    private List positionCategoryList = new ArrayList();
    
    /** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	/** 大区（部门）*/
	private List<Map<String, Object>> regionDepartList;
	
	/** 系统配置信息中设置的关键时间段*/
	private List<Map<String, Object>> importantTimeList;
	
    
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
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            //取得系统配置项中的关键时间段
			importantTimeList = binOLCM14_BL.getConfigValueByGroupNo(10, String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)));
			
			for(Map<String,Object> temp : importantTimeList){
				temp.put("startTime", temp.get("1"));
				temp.put("endTime", temp.get("2"));
			}
			
        }else{
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
            // 取得岗位类别信息List
            positionCategoryList = binOLMOWAT05_BL.getPositionCategoryList(map);
            
            // 取得区域List
			reginList = binolcm00BL.getReginList(map);
			// 取得渠道List
			channelList = binolcm00BL.getChannelList(map);
            //操作类型   0：更新（包括新增，删除）1：查询
			map.put(CherryConstants.OPERATION_TYPE, "1");
			//业务类型  0：基础数据；1：库存数据；2：会员数据； A:全部 等
			map.put(CherryConstants.BUSINESS_TYPE, "0");
			//部门类型
			map.put(CherryConstants.DEPART_TYPE, "5");
			regionDepartList = binOLCM00_BL.getDepartList(map);
			//取得系统配置项中的关键时间段
			importantTimeList = binOLCM14_BL.getConfigValueByGroupNo(10, String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)));
			
			for(Map<String,Object> temp : importantTimeList){
				temp.put("startTime", temp.get("1"));
				temp.put("endTime", temp.get("2"));
			}
			
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
     * 导出统计结果excel
     * 
     * */
    public String statisticsExport() throws Exception{
    	try {
	    	if(!_checkSearchData()){
				return "BINOLMOBUY0101_excel";
			}
	    	// 取得参数MAP
	        Map<String, Object> searchMap = getSearchMap();
	        //大区（部门）ID
	        searchMap.put("reginDepartID", form.getReginDepartID());
	        //城市ID
	        searchMap.put("cityID", form.getCityId());
	        //选中渠道区分
	        String channelFlag = form.getChannelFlag();
	        
	        if("1".equals(channelFlag)){
	        	//统计渠道ID
	            searchMap.put("ChannelArr", form.getCheckedChannelArr());
	        }else if("0".equals(channelFlag)){
	        	//禁止统计渠道ID
	            searchMap.put("ForbiddenChannelArr", form.getCheckedChannelArr());
	        }
	        //关键时间段开始时间
	        searchMap.put("importTimeStartArr", form.getImportTimeStartArr());
	        //关键时间段结束时间
	        searchMap.put("importTimeEndArr", form.getImportTimeEndArr());
	    	//业务类型   0：基础数据；1：库存数据；2：会员数据； A:全部 等
	        searchMap.put("businessType", "0");
	    	//操作类型   0：更新（包括新增，删除）；1：查询
	        searchMap.put("operationType", "1");
	        //统计柜台数量   部门类型为柜台 4
	        searchMap.put("DEPARTTYPE", "4");
	        
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMOBUY01", language, "downloadFileName1");
            setExcelStream(new ByteArrayInputStream(binOLMOBUY01_BL.statisticsExportExcel(searchMap)));
        } catch (Exception e) {
        	if(e instanceof CherryException){
        		this.addActionError(((CherryException) e).getErrMessage());
        	}else{
        		this.addActionError(getText("EMO00022"));
        	}
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        
    	return "BINOLMOBUY0101_excel";
    }
    
    /**
     * 取得考勤统计
     * 
     * 
     * */
	public String searchUdiskAttendanceStatisticsList() throws Exception{
    	try{
    		if(!_checkSearchData()){
    			return "BINOLMOBUY01_1";
    		}
        	// 取得参数MAP
            Map<String, Object> searchMap = getSearchMap();
            //大区（部门）ID
            searchMap.put("reginDepartID", form.getReginDepartID());
            //城市ID
            searchMap.put("cityID", form.getCityId());
            //选中渠道区分
            String channelFlag = form.getChannelFlag();
            
            if("1".equals(channelFlag)){
            	//统计渠道ID
                searchMap.put("ChannelArr", form.getCheckedChannelArr());
            }else if("0".equals(channelFlag)){
            	//禁止统计渠道ID
                searchMap.put("ForbiddenChannelArr", form.getCheckedChannelArr());
            }
            
            //关键时间段开始时间
            searchMap.put("importTimeStartArr", form.getImportTimeStartArr());
            //关键时间段结束时间
            searchMap.put("importTimeEndArr", form.getImportTimeEndArr());
        	//业务类型   0：基础数据；1：库存数据；2：会员数据； A:全部 等
            searchMap.put("businessType", "0");
        	//操作类型   0：更新（包括新增，删除）；1：查询
            searchMap.put("operationType", "1");
            //统计柜台数量   部门类型为柜台 4
            searchMap.put("DEPARTTYPE", "4");
        	
            Map<String,Object> retMap = binOLMOBUY01_BL.getUdiskAttendanceStatisticsList(searchMap);
            
            int count = (Integer)retMap.get("sum");
            udiskAttendanceStatisticsList = (List) retMap.get("retList");
            // form表单设置
            form.setITotalDisplayRecords(count);
            form.setITotalRecords(count);
    	}catch(Exception e){
    		if(e instanceof CherryException){
        		this.addActionError(((CherryException) e).getErrMessage());
        	}else{
        		this.addActionError(e.getMessage());
        	}
            return CherryConstants.GLOBAL_ACCTION_RESULT;
    	}
		
        return "BINOLMOBUY01_1";
    	
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
      //员工有效无效区分
        if("0".equals(form.getEmpValidFlag())){
        	map.put("empValidFlag", form.getEmpValidFlag());
        }else{
        	map.put("empValidFlag", "1");
        }
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
    }
	
    /**
     * ajax取得大区（部门），渠道信息
     * 
     * */
    public void getRegionDepartsAndChannels()throws Exception{
    	
    	 // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));
        //品牌ID
        map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        //组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    	
        // 取得区域List
		reginList = binolcm00BL.getReginList(map);
		// 取得渠道List
		channelList = binolcm00BL.getChannelList(map);
        //操作类型   0：更新（包括新增，删除）1：查询
		map.put(CherryConstants.OPERATION_TYPE, "1");
		//业务类型  0：基础数据；1：库存数据；2：会员数据； A:全部 等
		map.put(CherryConstants.BUSINESS_TYPE, "0");
		//部门类型
		map.put(CherryConstants.DEPART_TYPE, "5");
		
		//取得大区（部门）
		regionDepartList = binOLCM00_BL.getDepartList(map);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("reginList", reginList);
		resultMap.put("channelList", channelList);
		resultMap.put("regionDepartList", regionDepartList);
		
		ConvertUtil.setResponseByAjax(response, resultMap);
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 关键时间段验证
     * 
     * 
     * */
    private boolean _checkSearchData(){
    	
    	//关键时间段开始
    	String[] importTimeStartArr = form.getImportTimeStartArr();
    	//关键时间段结束
    	String[] importTimeEndArr = form.getImportTimeEndArr();
    	if(null != importTimeStartArr && null != importTimeEndArr){
    		if(importTimeStartArr.length > 0 && importTimeEndArr.length > 0){
        		int startLength = importTimeStartArr.length;
        		int endLength = importTimeEndArr.length;
        		if(startLength != endLength){
        			return false;
        		}else{
        			for(int i = 0 ; i < startLength ; i++){
                		String importTimeStart = importTimeStartArr[i].trim();
                		String importTimeEnd = importTimeEndArr[i].trim();
                		String[] importTimeStartA = importTimeStart.split(":");
                		String[] importTimeEndA = importTimeEnd.split(":");
                		if(importTimeStartA.length != 3){
                			return false;
                		}else{
                			if(ConvertUtil.getInt(importTimeStartA[0]) >23 || ConvertUtil.getInt(importTimeStartA[0]) < 0 || ConvertUtil.getInt(importTimeStartA[1])< 0 || ConvertUtil.getInt(importTimeStartA[1]) > 59 || ConvertUtil.getInt(importTimeStartA[2]) < 0 || ConvertUtil.getInt(importTimeStartA[2]) > 59){
                    			return false;
                    		}
                		}
                		
                		if(importTimeEndA.length != 3){
                			return false;
                		}else{
                			if(ConvertUtil.getInt(importTimeEndA[0]) >23 || ConvertUtil.getInt(importTimeEndA[0]) < 0 || ConvertUtil.getInt(importTimeEndA[1])< 0 || ConvertUtil.getInt(importTimeEndA[1]) > 59 || ConvertUtil.getInt(importTimeEndA[2]) < 0 || ConvertUtil.getInt(importTimeEndA[2]) > 59){
                    			return false;
                    		}
                		}
                	}
        		}
        	}
    	}
    	
    	return true;
    }
    
	
	@Override
	public BINOLMOBUY01_Form getModel() {
		return form;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
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

	public List getUdiskAttendanceStatisticsList() {
		return udiskAttendanceStatisticsList;
	}

	public void setUdiskAttendanceStatisticsList(List udiskAttendanceStatisticsList) {
		this.udiskAttendanceStatisticsList = udiskAttendanceStatisticsList;
	}


	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}


	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}


	public List getPositionCategoryList() {
		return positionCategoryList;
	}


	public void setPositionCategoryList(List positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}


	public List<Map<String, Object>> getReginList() {
		return reginList;
	}


	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}


	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}


	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}


	public List<Map<String, Object>> getRegionDepartList() {
		return regionDepartList;
	}


	public void setRegionDepartList(List<Map<String, Object>> regionDepartList) {
		this.regionDepartList = regionDepartList;
	}


	public List<Map<String, Object>> getImportantTimeList() {
		return importantTimeList;
	}


	public void setImportantTimeList(List<Map<String, Object>> importantTimeList) {
		this.importantTimeList = importantTimeList;
	}

	
}
