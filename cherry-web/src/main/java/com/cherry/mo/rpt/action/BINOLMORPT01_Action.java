/*  
 * @(#)BINOLMORPT01_Action.java     1.0 2011.10.21  
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
package com.cherry.mo.rpt.action;

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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.rpt.form.BINOLMORPT01_Form;
import com.cherry.mo.rpt.interfaces.BINOLMORPT01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 考核答卷一览Action
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT01_Action extends BaseAction implements ModelDriven<BINOLMORPT01_Form> {
	
	private static final long serialVersionUID = -1985512068094362657L;

    /** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	/** 考核答卷一览IF */
	@Resource
	private BINOLMORPT01_IF binOLMORPT01_BL;
	
	@Resource
	private BINOLCM00_BL binolcm00BL;
    
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
     * @return String 考核答卷一览画面
     * 
     */
    public String init() throws Exception {
    	
    	String sysDate = binolcm00BL.getDateYMD();
    	// 把系统日期作为考核结束时间
    	form.setCheckDateEnd(sysDate);
    	// 把系统日期所在月份的第一天的日期作为考核开始时间
    	form.setCheckDateStart(DateUtil.getFirstDateYMD(sysDate));
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
    	// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		
    	return SUCCESS;
    }

    /**
     * <p>
     * AJAX考核答卷信息查询
     * </p>
     * 
     * @return 考核答卷一览画面
     */
    public String search() throws Exception {
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 取得考核答卷信息总数
		int count = binOLMORPT01_BL.getCheckAnswerCount(map);
		if(count != 0) {
			// 取得考核答卷信息List
			checkAnswerList = binOLMORPT01_BL.getCheckAnswerList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
    	return SUCCESS;
    }
    
    /**
     * 
     * 开始与结束日期验证
     * 
     * @param 无
     * @return true日期合法，false日期非法
     */
    private boolean validateForm() {
		boolean isCorrect = true;
		// 考核开始日期
		String checkDateStart = form.getCheckDateStart().trim();
		// 考核结束日期
		String checkDateEnd = form.getCheckDateEnd().trim();
		/* 考核开始日期验证 */
		if (checkDateStart != null && !"".equals(checkDateStart)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(checkDateStart)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 考核结束日期验证 */
		if (checkDateEnd != null && !"".equals(checkDateEnd)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(checkDateEnd)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && checkDateStart != null && !"".equals(checkDateStart)
				&& checkDateEnd != null && !"".equals(checkDateEnd)) {
			// 考核开始日期在结束日期之后
			if (CherryChecker.compareDate(checkDateStart, checkDateEnd) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}
    
    /**
	 * 
	 * 查询参数MAP取得
     * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception {
		
		// 参数MAP
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		// 日期格式化处理
		String checkDateStart = (String)map.get("checkDateStart");
		String checkDateEnd = (String)map.get("checkDateEnd");
		Date startDate = DateUtil.coverString2Date(checkDateStart);
		Date endDate = DateUtil.coverString2Date(checkDateEnd);
		if(startDate != null) {
			checkDateStart = DateUtil.date2String(startDate, "yyyy-MM-dd");
		} else {
			checkDateStart = "";
		}
		if(endDate != null) {
			checkDateEnd = DateUtil.date2String(endDate, "yyyy-MM-dd");
		} else {
			checkDateEnd = "";
		}
		map.put("checkDateStart", checkDateStart);
		map.put("checkDateEnd", checkDateEnd);
		return map;
	}
    
    /**
     * 导出Excel
     * @throws Exception 
     */
    public String export() throws Exception{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLMORPT01", language, "downloadFileName");
            setExcelStream(new ByteArrayInputStream(binOLMORPT01_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLMORPT01_excel";
    }
	
    /** 考核答卷信息List */
    public List<Map<String, Object>> checkAnswerList;
    
    /** 假日信息 */
	private String holidays;
    
    public List<Map<String, Object>> getCheckAnswerList() {
		return checkAnswerList;
	}

	public void setCheckAnswerList(List<Map<String, Object>> checkAnswerList) {
		this.checkAnswerList = checkAnswerList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	/** 考核答卷一览Form */
	private BINOLMORPT01_Form form = new BINOLMORPT01_Form();
    
    @Override
    public BINOLMORPT01_Form getModel() {
        return form;
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
