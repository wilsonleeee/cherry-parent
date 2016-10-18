/*  
 * @(#)BINOLMOWAT10_Action.java     1.0 2015/12/11      
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
import com.cherry.mo.wat.form.BINOLMOWAT11_Form;
import com.cherry.mo.wat.interfaces.BINOLMOWAT11_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * Job失败履历一览
 * 
 * @author lzs
 * @version 1.0 2015.12.10
 */
public class BINOLMOWAT11_Action  extends BaseAction implements ModelDriven<BINOLMOWAT11_Form>{

	private static final long serialVersionUID = -8149882541218416046L;

	/** 参数FORM */
    private BINOLMOWAT11_Form form = new BINOLMOWAT11_Form();
    
    @Resource(name="binOLMOWAT11_BL")
    private BINOLMOWAT11_IF binOLMOWAT11_BL;
    
    @Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
    
    
    /** Job失败履历List */
    private List<Map<String, Object>> jobRunFaildList;
    
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
    	Map<String,Object> map = getCommonMap();
    	
		String brandInfoIdtemp = ConvertUtil.getString(form.getBrandInfoId());
		// 取得品牌List
		if(!"".equals(brandInfoIdtemp)) {
			// 其他页面调用此页面的情况
		} else if ("-9999".equals(map.get(CherryConstants.BRANDINFOID))) {
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
			brandMap.put("brandInfoId", map.get(CherryConstants.BRANDINFOID));
			// 品牌名称
			brandMap.put("brandName", map.get(CherryConstants.BRAND_NAME));
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
     * Job失败履历信息查询
     * 
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
    	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    	map.putAll(getCommonMap());

        // form参数设置到map中
        ConvertUtil.setForm(form, map);
        // 取得柜台总数
        int count = binOLMOWAT11_BL.getCountJobRunFaildHistory(map);
        if (count > 0) {
            // 取得柜台List
        	jobRunFaildList = binOLMOWAT11_BL.getJobRunFaildHistoryList(map);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return SUCCESS;
    }
    /**
     * 共通参数
     * @return
     */
	private Map<String, Object> getCommonMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌名称
		map.put(CherryConstants.BRAND_NAME, userInfo.getBrandName());
		return map;
	}
	@Override
    public BINOLMOWAT11_Form getModel() {
        return form;
    }
	
    public List<Map<String, Object>> getJobRunFaildList() {
		return jobRunFaildList;
	}

	public void setJobRunFaildList(List<Map<String, Object>> jobRunFaildList) {
		this.jobRunFaildList = jobRunFaildList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

}
