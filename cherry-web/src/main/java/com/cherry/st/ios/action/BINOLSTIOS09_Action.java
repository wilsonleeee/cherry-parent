/*  
 * @(#)BINOLSTIOS09_Action.java     1.0 2013/07/04     
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
package com.cherry.st.ios.action;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.st.ios.form.BINOLSTIOS09_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS09_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 入库单（Excel导入）Action
 * 
 * @author zhangle
 * @version 1.0 2013.07.04
 */
public class BINOLSTIOS09_Action extends BaseAction implements
		ModelDriven<BINOLSTIOS09_Form> {

	private static final long serialVersionUID = -6533826836444476255L;
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS09_Action.class);
	private BINOLSTIOS09_Form form = new BINOLSTIOS09_Form();
	@Resource(name="binOLSTIOS09_BL")
	private BINOLSTIOS09_IF binOLSTIOS09_BL;
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 入库（Excel导入）查询画面
	 * 
	 * @return 入库（Excel导入）查询画面
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();

			// 总部用户登录的时候
			if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
				// 取得所管辖的品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			}else {
				form.setBrandInfoId(ConvertUtil.getString(brandId));
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 入库（Excel导入）结果
	 * @return
	 */
	public String search() {
		try {
			Map<String, Object> map = getSearchMap();
			map.put("importStartTime", form.getImportStartTime());
			map.put("importEndTime", form.getImportEndTime());
			map.put("importBatchCode", form.getImportBatchCode());
			map.put("type", "GR");
			map.put("validFlag", "1");
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTIOS09_BL.getImportBatchCount(map);
			if(count != 0){
				productInDepotExcelBatchList = binOLSTIOS09_BL.getImportBatchList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 取得查询批次的参数【加入了人员权限控制】
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "0");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 是否带权限查询
		map.put(CherryConstants.SESSION_PRIVILEGE_FLAG, session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 批次类型
		map.put("type", "GR");
		return map;
	}
	
	
	/**
	 * 入库Excel导入画面初期显示
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String importInit() {
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 总部的场合
			if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
				// 参数MAP
				Map<String, Object> map = new HashMap<String, Object>();
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID,
						userInfo.getBIN_OrganizationInfoID());
				// 语言
				map.put(CherryConstants.SESSION_LANGUAGE,
						session.get(CherryConstants.SESSION_LANGUAGE));
				// 取得品牌List
				brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			} else {
				// 品牌信息
				Map<String, Object> brandInfo = new HashMap<String, Object>();
				brandInfoId = userInfo.getBIN_BrandInfoID();
				// 品牌ID
				brandInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
				// 品牌Code
				brandInfo.put("brandCode", userInfo.getBrandCode());
				// 品牌名称
				brandInfo.put("brandName", userInfo.getBrandName());
				// 品牌List
				brandInfoList = new ArrayList();
				brandInfoList.add(brandInfo);
			}
			
			//取下载文件路径
			String brandCode = ConvertUtil.getString(brandInfoList.get(0).get("brandCode"));
			form.setDownloadPath(getBrandDownloadPath(brandCode));
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 入库Excel导入
	 * @return
	 */
	public String importInDepot() {
		try {
			// 登录用户参数MAP
			Map<String, Object> sessionMap = getSessionMap();
			// 上传的文件
			sessionMap.put("upExcel", upExcel);
			//导入的数据
			Map<String, Object> importDataMap = binOLSTIOS09_BL.ResolveExcel(sessionMap);
			Map<String, Object> resultMap=binOLSTIOS09_BL.tran_excelHandle(importDataMap, sessionMap);
			resultMap.put("currentImportBatchCode", sessionMap.get("currentImportBatchCode"));
			setResultMap(resultMap);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00079"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}	
	}
	
	/**
	 * 验证导入条件
	 */
	public void validateImportInDepot() {
		String isChecked = form.getIsChecked();
		String importBatchCode = form.getImportBatchCode();
		if(CherryChecker.isNullOrEmpty(isChecked, true)){
			//若从画面输入
			if(!CherryChecker.isAlphanumeric(importBatchCode)){
				//数据格式校验
				this.addActionError(getText("ECM00031",new String[]{PropertiesUtil.getText("STM00012")}));
			}else if(importBatchCode.length() > 25){
				//长度校验
				this.addActionError(getText("ECM00020",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
			}else {
				//重复校验
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				map.put("importBatchCodeR", importBatchCode);
				map.put("type", "GR");
				if(binOLSTIOS09_BL.getImportBatchCount(map) > 0){
					this.addActionError(getText("ECM00032",new String[]{PropertiesUtil.getText("STM00012"),"25"}));
				}
			}
		}
		if(CherryChecker.isNullOrEmpty(form.getComments(), true)){
			this.addActionError(getText("ECM00009",new String[]{PropertiesUtil.getText("STM00013")}));
		}
	} 
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}
		map.put("language", userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		map.put("Comments", form.getComments());
		map.put("ImportBatchCode", form.getImportBatchCode());
		map.put("ImportBatchCodeIF", form.getImportBatchCode());
		map.put("CreatedBy", map.get(CherryConstants.USERID));
		map.put("CreatePGM", "BINOLSTIOS09");
		map.put("UpdatedBy", map.get(CherryConstants.USERID));
		map.put("UpdatePGM", "BINOLSTIOS09");
		map.put("Type", "GR");
		map.put("importRepeat", form.getImportRepeat());
		map.put("isChecked", form.getIsChecked());
		map.put("type", "GR");
		return map;
	}
	

    /**
     * 取得品牌自定义模板下载地址
     * @param brandCode
     * @param fileName
     * @return
     */
    private String getBrandDownloadPath(String brandCode){
        //判断是否有品牌自定义的模板，找不到取默认模板。
        String fileName = CherryConstants.PRODUCTINDEPOTEXCEL_FILENAME;
        String classPath = this.getClass().getClassLoader().getResource("").getPath();
        String downloadURL = "/download/" + brandCode + "/"+fileName;
        if(classPath.indexOf("target/classes") > -1){
            // Maven启动Jetty的路径（开发环境）
            String rootPath = classPath.substring(0, classPath.indexOf("target/classes"));
            File file = new File(rootPath + "src/main/webapp" + downloadURL);
            if (!file.exists()) {
                downloadURL = downloadURL.replace("/"+brandCode+"/", "/");
            }
        }else if(classPath.indexOf("WEB-INF/classes") > -1){
            //Jetty启动的路径（正式环境）
            String rootPath = classPath.substring(0,classPath.indexOf("WEB-INF/classes"));
            File file = new File(rootPath+downloadURL);
            if(!file.exists()){
                downloadURL = downloadURL.replace("/"+brandCode+"/", "/");
            }
        }
        return downloadURL;
    }
	
    public void getDownloadPathByAjax() throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        int brandInfoID = CherryUtil.obj2int(form.getBrandInfoId());
        String brandCode = binOLCM05_BL.getBrandCode(brandInfoID);
        resultMap.put("downloadURL", getBrandDownloadPath(brandCode));
        ConvertUtil.setResponseByAjax(response, resultMap);
    }
	
	/** 入库单（Excel导入）list*/
	private List<Map<String, Object>> productInDepotExcelBatchList;
	
	/**导入结果*/
	@SuppressWarnings("rawtypes")
	private Map resultMap;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 产品品牌ID */
	private int brandInfoId;

	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	


	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
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
	
	
	@Override
	public BINOLSTIOS09_Form getModel() {
		return form;
	}

	@SuppressWarnings("rawtypes")
	public Map getResultMap() {
		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}


	public List<Map<String, Object>> getProductInDepotExcelBatchList() {
		return productInDepotExcelBatchList;
	}

	public void setProductInDepotExcelBatchList(List<Map<String, Object>> productInDepotExcelBatchList) {
		this.productInDepotExcelBatchList = productInDepotExcelBatchList;
	}
}