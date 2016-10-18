/*  
 * @(#)BINOLMOCIO01_Action.java     1.0 2011/06/09      
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
package com.cherry.mo.cio.action;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.form.BINOLMOCIO01_Form;
import com.cherry.mo.cio.form.BINOLMOCIO21_Form;
import com.cherry.mo.cio.form.BINOLMOCIO23_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO01_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO21_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO23_IF;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.pt.common.ProductConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 订货消息管理Action
 * 
 * @author nanjunbo
 * @version 1.0 2016.09.19
 */
public class BINOLMOCIO23_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO23_Form> {

	private static final long serialVersionUID = 3090380688159913449L;

	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO23_Action.class);

    /** 参数FORM */
    private BINOLMOCIO23_Form form = new BINOLMOCIO23_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource
    private BINOLMOCIO23_IF binOLMOCIO23_BL;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    /** 部门消息List */
    private List<Map<String, Object>> departmentMessageList;
    
    /** 部门消息 */
    private Map<String, Object> departmentMessage;
    
	
    @Override
    public BINOLMOCIO23_Form getModel() {
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
    @SuppressWarnings("unchecked")
    public String init() throws Exception {
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
        // 开始日期
        //form.setStartDate(binolcm00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        //form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        return SUCCESS;
    }

    /**
     * <p>
     * AJAX柜台消息查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 验证提交的参数
        if (!validateSearch()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        
		String validStatus = "";
		if (searchMap.get("FILTER_VALUE") != null) {
			// 生效状态
			validStatus = searchMap.get("FILTER_VALUE").toString();
			if (validStatus.equals("in_progress")
					|| validStatus.equals("past_due")
					|| validStatus.equals("not_start")
					|| validStatus.equals("not_release")) {
				if (validStatus.equals("in_progress")) {
					// 进行中
					searchMap.put("dateStatus", 1);
				} else if (validStatus.equals("past_due")) {
					// 已过期
					searchMap.put("dateStatus", 2);
				} else if (validStatus.equals("not_start")) {
					// 未开始
					searchMap.put("dateStatus", 3);
				} else if(validStatus.equals("not_release")) {
					// 其他
					searchMap.put("dateStatus", 4);
				}
			}
		} else {
			// 默认设置显示进行中
			searchMap.put("dateStatus", 1);
		}
        // 取得柜台消息总数
        int count = binOLMOCIO23_BL.getDepartmentMessageCount(searchMap);
        if (count > 0) {
            // 取得柜台消息List
            departmentMessageList = binOLMOCIO23_BL.getDepartmentMessageList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOCIO23_1";
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
            }
        } else {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        }
        //组织ID
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        map.put("organizationId", userInfo.getBIN_OrganizationID());
        //消息标题或内容
        map.put("message", form.getMessageTitleOrBody());
        // 导入批次
        map.put("messageType", form.getMessageType());
        //生效日期开始
        map.put("startDate", form.getStartDate());
        //生效日期截止
        map.put("endDate", form.getEndDate());
        
        return map;
    }
    
    /**
     * <p>
     * 初始化编辑柜台消息
     * </p>
     * 
     * @return
     */
    public String detail() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("departmentMessageId", form.getDepartmentMessageId());
        departmentMessage = binOLMOCIO23_BL.getDepartmentMessage(map);
        if(null != departmentMessage && !departmentMessage.isEmpty()){
        	String filePath = ConvertUtil.getString(departmentMessage.get("filePath"));
        	// 文件路径前缀
    		String headPath = PropertiesUtil.pps
    				.getProperty("virtualdirectory.message.url");
        	departmentMessage.put("filePathShow", headPath+CherryConstants.SLASH+filePath);
        }
        return SUCCESS;
    }

  
    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }
    
    public List<Map<String, Object>> getDepartmentMessageList() {
		return departmentMessageList;
	}

	public void setDepartmentMessageList(
			List<Map<String, Object>> departmentMessageList) {
		this.departmentMessageList = departmentMessageList;
	}

	public Map<String, Object> getDepartmentMessage() {
		return departmentMessage;
	}

	public void setDepartmentMessage(Map<String, Object> departmentMessage) {
		this.departmentMessage = departmentMessage;
	}	

	

	/**
     * 查询校验
     */
    public boolean validateSearch(){
        boolean isCorrect = true;
        // 开始日期
        String startDate = form.getStartDate();
        // 结束日期
        String endDate = form.getEndDate();
        /*开始日期验证*/
        if (startDate != null && !"".equals(startDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(startDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
                isCorrect = false;
            }
        }
        /*结束日期验证*/
        if (endDate != null && !"".equals(endDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(endDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
                isCorrect = false;
            }
        }
        if (isCorrect && startDate != null && !"".equals(startDate)&& 
                endDate != null && !"".equals(endDate)) {
            // 开始日期在结束日期之后
            if(CherryChecker.compareDate(startDate, endDate) > 0) {
                this.addActionError(getText("ECM00019"));
                isCorrect = false;
            }
        }
        return isCorrect;
    }

}
