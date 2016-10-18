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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.form.BINOLMOCIO01_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO01_IF;
import com.cherry.mo.common.MonitorConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 柜台消息管理Action
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO01_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO01_Form> {

    private static final long serialVersionUID = 4991697432758172804L;
    
    //打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO01_Action.class);

    /** 参数FORM */
    private BINOLMOCIO01_Form form = new BINOLMOCIO01_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource
    private BINOLMOCIO01_IF binOLMOCIO01_BL;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    /** 柜台消息List */
    private List<Map<String, Object>> counterMessageList;
    
    /** 柜台消息 */
    private Map<String, Object> counterMessage;
    
    @Override
    public BINOLMOCIO01_Form getModel() {
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
					// 未开始
					searchMap.put("dateStatus", 4);
				}
			}
		} else {
			// 默认设置显示进行中
			searchMap.put("dateStatus", 1);
		}
        // 取得柜台消息总数
        int count = binOLMOCIO01_BL.getCounterMessageCount(searchMap);
        if (count > 0) {
            // 取得柜台消息List
            counterMessageList = binOLMOCIO01_BL.getCounterMessageList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOCIO01_1";
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
        //消息标题或内容
        map.put("message", form.getMessageTitleOrBody());
        // 导入批次
        map.put("importBatch", form.getImportBatch());
        //发布日期开始
        map.put("startDate", form.getStartDate());
        //发布日期截止
        map.put("endDate", form.getEndDate());
        
        return map;
    }

    /**
     * <p>
     * 初始化新增柜台消息
     * </p>
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String addInit() throws Exception {
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
        return SUCCESS;
    }
    
    
    /**
     * <p>
     * 新增柜台消息
     * </p>
     * 
     * @return
     */
    public String add() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 作成者
        map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
        // 作成程序名
        map.put(CherryConstants.CREATEPGM, MonitorConstants.BINOLMOCIO01);
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        // 更新程序名
        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        if(null != form.getBrandInfoId() && !"".equals(form.getBrandInfoId())) {
            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        } else {
            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        }
        //消息标题
        map.put("messageTitle", form.getMessageTitle());
        //柜台消息生效开始日期
        map.put("startValidDate", form.getStartValidDate());
        //柜台消息生效截止日期
        map.put("endValidDate", form.getEndValidDate());
        //消息内容
        map.put("messageBody", form.getMessageBody());
        try{
            binOLMOCIO01_BL.tran_addCounterMessage(map);
        }catch(Exception e){
        	logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }
        }
        return SUCCESS;
    }
    
    /**
     * <p>
     * 初始化编辑柜台消息
     * </p>
     * 
     * @return
     */
    public String updateInit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("counterMessageId", form.getCounterMessageId());
        counterMessage = binOLMOCIO01_BL.getCounterMessage(map);
        if(null == counterMessage.get("messageTitle")){
            counterMessage.put("messageTitle", "");
        }
        if(null == counterMessage.get("startValidDate")){
        	counterMessage.put("startValidDate", "");
        }
        if(null == counterMessage.get("endValidDate")){
        	counterMessage.put("endValidDate", "");
        }
        if(null == counterMessage.get("messageBody")){
            counterMessage.put("messageBody", "");
        }
        return SUCCESS;
    }
    
    /**
     * <p>
     * 编辑柜台消息
     * </p>
     * 
     * @return
     */
    public String update() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        // 更新程序名
        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
        //消息标题
        map.put("messageTitle", form.getMessageTitle());
        //开始日期
        map.put("startValidDate", form.getStartValidDate());
        //结束日期
        map.put("endValidDate", form.getEndValidDate());
        //消息内容
        map.put("messageBody", form.getMessageBody());
        //柜台消息ID
        map.put("counterMessageId", form.getCounterMessageId());
        // 更新日时
        map.put(CherryConstants.MODIFY_TIME, form.getModifyTime());
        // 更新次数
        map.put(CherryConstants.MODIFY_COUNT, form.getModifyCount());
        
        try{
            binOLMOCIO01_BL.tran_modifyCounterMessage(map);
        }catch(Exception e){
        	logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
            }else{
                throw e;
            }
        }
        return SUCCESS;
    }
    
    /**
     * <p>
     * 删除柜台消息
     * </p>
     * 
     * @return
     */
    public String delete() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        //柜台消息ID
        map.put("counterMessageId", form.getCounterMessageId());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        // 更新程序名
        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
        try{
            binOLMOCIO01_BL.tran_deleteCounterMessage(map);
        }catch(Exception e){
        	logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
            }else{
                throw e;
            }
        }
        return SUCCESS;
    }

    /**
     * 取得某条已下发的消息下发的柜台及其所属区域
     * 
     * 
     * */
    public String getMessageRegion(){
    	try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String,Object>> list = null;
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 语言
			map.put("language", userInfo.getLanguage());
			// 所属组织
	        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        // 所属品牌
	        if(null != form.getBrandInfoId() && !"".equals(form.getBrandInfoId())) {
	            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
	        } else {
	            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	        }
			// 柜台消息ID
			map.put("BIN_CounterMessageID", form.getCounterMessageId());
			if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
				// 业务类型
				map.put("businessType", "0");
				// 操作类型
				map.put("operationType", "1");
				// 是否带权限查询
				map.put("privilegeFlag", form.getPrivilegeFlag());
			}
			//显示模式
			String selMode = form.getSelMode();
			
			if(CherryChecker.isNullOrEmpty(selMode) || "1".equals(selMode)){
				list = binOLMOCIO01_BL.getMessageRegion(map);
			}else if("2".equals(selMode)){
				//取得大区信息（供选择大区来显示渠道柜台树）
				list = binOLMOCIO01_BL.getRegionList(map);
			}else if("3".equals(selMode)){
				list = binOLMOCIO01_BL.getMessageOrganize(map);
			}
			
			ConvertUtil.setResponseByAjax(response, list);
			return null;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
    }
    
    /***
     * 根据选择的大区去显示渠道柜台树
     * @return
     */
    public String getMessageChannel(){
    	try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String,Object>> list = null;
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 语言
			map.put("language", userInfo.getLanguage());
			// 所属组织
	        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        // 所属品牌
	        if(null != form.getBrandInfoId() && !"".equals(form.getBrandInfoId())) {
	            map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
	        } else {
	            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	        }
			// 当前柜台消息ID
			map.put("BIN_CounterMessageID", form.getCurrentMessageId());
			// 大区ID
			map.put("regionId", form.getChannelRegionId());
			if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
				// 业务类型
				map.put("businessType", "0");
				// 操作类型
				map.put("operationType", "1");
				// 是否带权限查询
				map.put("privilegeFlag", form.getPrivilegeFlag());
			}
			list = binOLMOCIO01_BL.getMessageChannel(map);
			
			ConvertUtil.setResponseByAjax(response, list);
			return null;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
    }
    
    /**
     * 停用、启用柜台消息
     * @return
     * @throws Exception
     */
    public String disableOrEnable() throws Exception{
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
            // 登陆用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            //品牌code
            map.put("BrandCode", userInfo.getBrandCode());
            //柜台消息状态（0：停用，1：启用）
            map.put("status", form.getStatus());
            //柜台消息ID
            map.put("counterMessageId", form.getCounterMessageId());
            // 更新者
            map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
            // 更新程序名
            map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
            
            binOLMOCIO01_BL.tran_disableOrEnable(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
    	return SUCCESS;
    }
    
    
    public void validateAdd() throws Exception{
        int maxMsgTitle = 10;
        int maxMsgBody = 200;
        if (CherryChecker.isNullOrEmpty(form.getMessageTitle())){
            this.addFieldError("messageTitle", getText("ECM00009",new String[]{getText("PMO00014")}));
        }else{
            if(form.getMessageTitle().length() > maxMsgTitle) {
                this.addFieldError("messageTitle", getText("ECM00020",new String[]{getText("PMO00014"),String.valueOf(maxMsgTitle)}));
            }
        }
        //新增时起止日期为必填项的后台验证
        if(CherryChecker.isNullOrEmpty(form.getStartValidDate())){
        	this.addFieldError("startValidDate", getText("ECM00009",new String[]{getText("PMO00017")}));
        }
        if(CherryChecker.isNullOrEmpty(form.getEndValidDate())){
        	this.addFieldError("endValidDate", getText("ECM00009",new String[]{getText("PMO00018")}));
        }
        // {0}最大长度不能超过{1}个字节(1个汉字占两个字节)，请重新输入！
        if(CherryChecker.isByteLength(form.getMessageBody(), maxMsgBody)) {
        	this.addFieldError("messageBody", getText("ECM00058",new String[]{getText("PMO00015"),String.valueOf(maxMsgBody)}));
        }
    }
    
    public void validateUpdate() throws Exception{
        int maxMsgTitle = 10;
        int maxMsgBody = 200;
        if (CherryChecker.isNullOrEmpty(form.getMessageTitle())){
            this.addFieldError("messageTitle", getText("ECM00009",new String[]{getText("PMO00014")}));
        }else{
            if(form.getMessageTitle().length() > maxMsgTitle) {
                this.addFieldError("messageTitle", getText("ECM00020",new String[]{getText("PMO00014"),String.valueOf(maxMsgTitle)}));
            }
        }
        //编辑时起止日期为必填项的后台验证
        if(CherryChecker.isNullOrEmpty(form.getStartValidDate())){
        	this.addFieldError("startValidDate", getText("ECM00009",new String[]{getText("PMO00017")}));
        }
        if(CherryChecker.isNullOrEmpty(form.getEndValidDate())){
        	this.addFieldError("endValidDate", getText("ECM00009",new String[]{getText("PMO00018")}));
        }
        // {0}最大长度不能超过{1}个字节(1个汉字占两个字节)，请重新输入！
        if(CherryChecker.isByteLength(form.getMessageBody(), maxMsgBody)) {
        	this.addFieldError("messageBody", getText("ECM00058",new String[]{getText("PMO00015"),String.valueOf(maxMsgBody)}));
        }
    }
    
    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setCounterMessageList(List<Map<String, Object>> counterMessageList) {
        this.counterMessageList = counterMessageList;
    }

    public List<Map<String, Object>> getCounterMessageList() {
        return counterMessageList;
    }

    public void setCounterMessage(Map<String, Object> counterMessage) {
        this.counterMessage = counterMessage;
    }

    public Map<String, Object> getCounterMessage() {
        return counterMessage;
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
