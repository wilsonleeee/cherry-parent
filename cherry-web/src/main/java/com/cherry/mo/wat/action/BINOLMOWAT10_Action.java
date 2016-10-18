/*  
 * @(#)BINOLMOWAT10_Action.java     1.0 2015/7/1      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.wat.form.BINOLMOWAT10_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT10_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * Job运行履历查询
 * 
 * @author ZCF
 * @version 1.0 2015.06.26
 */
public class BINOLMOWAT10_Action  extends BaseAction implements
ModelDriven<BINOLMOWAT10_Form>{

    private static final long serialVersionUID = 4261180958320105226L;
    
    /** 参数FORM */
    private BINOLMOWAT10_Form form = new BINOLMOWAT10_Form();
    
    @Resource(name="binOLMOWAT10_BL")
    private BINOLMOWAT10_IF binOLMOWAT10_BL;
    
    @Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
    
    /** 节日 */
    private String holidays;
    
    /** Job履历List */
    private List<Map<String, Object>> jobRunList;
    
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
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		String brandInfoIdtemp = ConvertUtil.getString(form.getBrandInfoId());
		// 取得品牌List
		if(!"".equals(brandInfoIdtemp)) {
			// 其他页面调用此页面的情况
		} else if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
      
        return SUCCESS;
    }
    
    /**
     * Job运行履历表查询
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    	// 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE){
            //品牌ID
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        //开始时间
        String runStartTime=(String) map.get("putTimeStart");
		// 结束时间
		String runEndTime=(String) map.get("putTimeEnd");
		
		map.put("runStartTime", DateUtil.suffixDate(runStartTime, 0));
		map.put("runEndTime", DateUtil.suffixDate(runEndTime, 1)); 
		
        // form参数设置到map中
        ConvertUtil.setForm(form, map);
        // 取得柜台总数
        int count = binOLMOWAT10_BL.getCountJobRun(map);
        if (count > 0) {
            // 取得柜台List
        	jobRunList = binOLMOWAT10_BL.getJobRunList(map);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return SUCCESS;
    }
    
    @Override
    public BINOLMOWAT10_Form getModel() {
        return form;
    }
    
    public List<Map<String, Object>> getJobRunList() {
		return jobRunList;
	}

	public void setJobRunList(List<Map<String, Object>> jobRunList) {
		this.jobRunList = jobRunList;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getHolidays() {
        return holidays;
    }

}
