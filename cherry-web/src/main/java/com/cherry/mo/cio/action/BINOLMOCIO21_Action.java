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
import com.cherry.mo.cio.interfaces.BINOLMOCIO01_IF;
import com.cherry.mo.cio.interfaces.BINOLMOCIO21_IF;
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
public class BINOLMOCIO21_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO21_Form> {

	private static final long serialVersionUID = -4418167420145263258L;

	//打印异常日志
    private static final Logger logger = LoggerFactory.getLogger(BINOLMOCIO21_Action.class);

    /** 参数FORM */
    private BINOLMOCIO21_Form form = new BINOLMOCIO21_Form();
    
    /** 共通BL */
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    
    @Resource
    private BINOLMOCIO21_IF binOLMOCIO21_BL;
    
    /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    /** 部门消息List */
    private List<Map<String, Object>> departmentMessageList;
    
    /** 部门消息 */
    private Map<String, Object> departmentMessage;
    
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
	/** 上传的文件 */
	private File fileUp;
    
	/** 提示信息 */
	private String message;
	
	/** 上传的文件的名称 */
	private String fileUpFileName;
	
    @Override
    public BINOLMOCIO21_Form getModel() {
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
        int count = binOLMOCIO21_BL.getDepartmentMessageCount(searchMap);
        if (count > 0) {
            // 取得柜台消息List
            departmentMessageList = binOLMOCIO21_BL.getDepartmentMessageList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLMOCIO21_1";
    }
    
	/**
	 * 上传文件
	 * 
	 * @return String 上传结果信息画面
	 * 
	 * @throws Exception
	 */
	public String uploadFile() throws Exception {
		Map msgMap = new HashMap();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileUp);			
			// 保存地址
			String savePath = PropertiesUtil.pps.getProperty("virtualdirectory.message");
			String fileNameNew = renameFile();
			File targetFile = new File(savePath, fileNameNew); 
			// 保存文件
	        FileUtils.copyFile(fileUp, targetFile);	
	        msgMap.put("filePath", fileNameNew);
	        msgMap.put("fileName", fileUpFileName);
		} catch (Exception e) {
			msgMap.put("msg", "上传文件失败");
			msgMap.put("result", CherryConstants.result_NG);
			message = JSONUtil.serialize(msgMap);
			logger.error(e.getMessage(), e);
			return INPUT;
		} finally {
			if (null != fis) {
				fis.close();
			}
		}
		msgMap.put("msg", "上传文件成功");
		msgMap.put("result", CherryConstants.result_OK);
		message = JSONUtil.serialize(msgMap);
		return SUCCESS;
	}
	
	/**
	 * 文件重命名
	 * 
	 * @return String 文件新名字
	 * 
	 */
	private String renameFile() {
		// 文件扩展名
		String extName = "";
		if (fileUpFileName.lastIndexOf(".") >= 0) {
			extName = fileUpFileName.substring(fileUpFileName.lastIndexOf("."));
		}
		// 当前时间
		String nowTime = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		// 随机数
		Random random = new Random();
		int randNum = (int) (random.nextDouble() * 90000) + 10000;
		// 新文件名
		String newFileName = nowTime + randNum + extName;
		return newFileName;
	}
	
//	/**
//	 * 图片上传
//	 * 
//	 * @param imagePath
//	 * @throws Exception
//	 */
//	private void uploadFile(String fielPath) throws Exception {
//			// 原始地址
//			String scrPath = PropertiesUtil.pps
//					.getProperty("uploadFilePath.tempImagePath");
//			// 目标地址
//			String dstPath = PropertiesUtil.pps
//					.getProperty("uploadFilePath.upImagePath");			
//			// 原始文件
//			File srcFile = new File(scrPath, fielPath);
//			// 目标文件
//			File dstFile = new File(dstPath, fielPath);
//			// 复制文件
//			CherryUtil.copyFileByChannel(srcFile, dstFile);				
//	}

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
        map.put("messageType", form.getMessageType());
        //生效日期开始
        map.put("startDate", form.getStartDate());
        //生效日期截止
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
        
        map.put("fileName", form.getFileOrImageName());
        
        map.put("messageType", form.getMessageType());
        
        map.put("filePath", form.getFileOrImagePath());
//        String filePath = ConvertUtil.getString(form.getFileOrImagePath());
//        if (!filePath.equals("")){
//        	 uploadFile(filePath);
//        }
       
        try{
            binOLMOCIO21_BL.tran_addDepartmentMessage(map);
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
        map.put("departmentMessageId", form.getDepartmentMessageId());
        departmentMessage = binOLMOCIO21_BL.getDepartmentMessage(map);
        if(null != departmentMessage && !departmentMessage.isEmpty()){
        	String filePath = ConvertUtil.getString(departmentMessage.get("filePath"));
        	// 文件路径前缀
    		String headPath = PropertiesUtil.pps
    				.getProperty("virtualdirectory.message.url");
        	departmentMessage.put("filePathShow", headPath+CherryConstants.SLASH+filePath);
        }
        if(null == departmentMessage.get("messageTitle")){
        	departmentMessage.put("messageTitle", "");
        }
        if(null == departmentMessage.get("startValidDate")){
        	departmentMessage.put("startValidDate", "");
        }
        if(null == departmentMessage.get("endValidDate")){
        	departmentMessage.put("endValidDate", "");
        }
        if(null == departmentMessage.get("messageBody")){
        	departmentMessage.put("messageBody", "");
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
    public String detail() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("departmentMessageId", form.getDepartmentMessageId());
        departmentMessage = binOLMOCIO21_BL.getDepartmentMessage(map);
        if(null != departmentMessage && !departmentMessage.isEmpty()){
        	String filePath = ConvertUtil.getString(departmentMessage.get("filePath"));
        	// 文件路径前缀
    		String headPath = PropertiesUtil.pps
    				.getProperty("virtualdirectory.message.url");
        	departmentMessage.put("filePathShow", headPath+CherryConstants.SLASH+filePath);
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
        map.put("departmentMessageId",  form.getDepartmentMessageId());
        //消息类型
        map.put("messageType", form.getMessageType());
        //如果没有文件上传就用原来的文件路径及名称
        String filePath = ConvertUtil.getString(form.getFilePathEdit());
        String fileName = ConvertUtil.getString(form.getFileNameEdit());
        if (!ConvertUtil.getString(form.getFileOrImagePath()).equals("")
        		&&!ConvertUtil.getString(form.getFileOrImageName()).equals("")){
        	filePath=ConvertUtil.getString(form.getFileOrImagePath());
        	fileName=ConvertUtil.getString(form.getFileOrImageName());
        }
        map.put("filePath", filePath);
        map.put("fileName", fileName);
        try{
            binOLMOCIO21_BL.tran_modifyDepartmentMessage(map);
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
        map.put("departmentMessageId", form.getDepartmentMessageId());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
        // 更新程序名
        map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
        try{
            binOLMOCIO21_BL.tran_deleteDepartmentMessage(map);
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
			map.put("BIN_DepartmentMessageID",  form.getDepartmentMessageId());
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
				list = binOLMOCIO21_BL.getMessageRegion(map);
			}else if("2".equals(selMode)){
				//取得大区信息（供选择大区来显示渠道柜台树）
				list = binOLMOCIO21_BL.getRegionList(map);
			}else if("3".equals(selMode)){
					list = binOLMOCIO21_BL.getMessageOrganize(map);
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
			map.put("BIN_DepartmentMessageID", form.getCurrentMessageId());
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
			list = binOLMOCIO21_BL.getMessageChannel(map);
			
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
            map.put("departmentMessageId",  form.getDepartmentMessageId());
            // 更新者
            map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
            // 更新程序名
            map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO01);
            
            binOLMOCIO21_BL.tran_disableOrEnable(map);
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

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public File getFileUp() {
		return fileUp;
	}

	public void setFileUp(File fileUp) {
		this.fileUp = fileUp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileUpFileName() {
		return fileUpFileName;
	}

	public void setFileUpFileName(String fileUpFileName) {
		this.fileUpFileName = fileUpFileName;
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
