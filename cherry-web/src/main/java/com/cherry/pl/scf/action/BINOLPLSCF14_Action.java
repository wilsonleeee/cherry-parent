/*
 * @(#)BINOLPLSCF01_Action.java     1.0 2010/10/27
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

package com.cherry.pl.scf.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM30_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.bl.BINOLPLSCF14_BL;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;

/**
 * 文件管理Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF14_Action extends BaseAction{
	
	private static final long serialVersionUID = -7697985562316587723L;
	
	/** 文件管理一览BL */
	@Resource
	private BINOLPLSCF14_BL binOLPLSCF14_BL;
	@Resource
	private BINOLCM30_IF binolcm30IF;

	/** code值管理 */
	@Resource
	private CodeTable CodeTable;
	
	/* 组织List*/
	private List<Map<String, Object>> orgInfoList;
	
	/* 品牌List*/
	private List<Map<String, Object>> brandInfoList;
	
	/* 文件信息List*/
	private String fileInfoStr;
	
	/* 导入文件路径*/
	private File upfile;
	
	/* 文件名*/
	private String fileName;
	
	/* 组织code*/
	private String orgCode;
	
	public File getUpfile() {
		return upfile;
	}

	public void setUpfile(File upfile) {
		this.upfile = upfile;
	}

	public List<Map<String, Object>> getOrgInfoList() {
		return orgInfoList;
	}

	public void setOrgInfoList(List<Map<String, Object>> orgInfoList) {
		this.orgInfoList = orgInfoList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getFileInfoStr() {
		return fileInfoStr;
	}

	public void setFileInfoStr(String fileInfoStr) {
		this.fileInfoStr = fileInfoStr;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 文件管理画面初期化
	 * 
	 * @return 文件管理画面
	 */
	public String init() {
		return "success";
	}
	
	/**
	 * 导入文件一览
	 * 
	 * @return 文件信息
	 * @throws Exception 
	 */
	public String importFile() throws Exception{
		// 保存地址
		String path = PropertiesUtil.pps.getProperty("CHERRY_HOME") + System.getProperty("file.separator") + "fileContent";
		File file = new File(path);
		// 创建目录文件夹
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		// 保存文件到指定目录
		String fileNameNew = fileName;
		File targetFile = new File(path, fileNameNew); 
		// 保存文件
        FileUtils.copyFile(upfile, targetFile);	
		OrgBrandInit();
		return "success";
	}
	
	/**
	 * 初期化组织品牌下拉框
	 * 
	 * @return null
	 */
	public void OrgBrandInit() {
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织Code
		String orgCode = userInfo.getOrganizationInfoCode();
		Map<String, Object> defaultMap = new HashMap<String, Object>();
		 // 默认品牌Code
		defaultMap.put("brandCode", String.valueOf(CherryConstants.Brand_CODE_ALL));
		 // 默认品牌名称
		defaultMap.put("brandName", getText("PPL00030"));
		Map<String, Object> orgMap = new HashMap<String, Object>();
		// 超级管理员登录的时候
		if (String.valueOf(CherryConstants.ORG_CODE_ALL).equals(orgCode)) {
			// 取得所有组织List
			orgInfoList = binOLPLSCF14_BL.getOrgInfoList();
			// 默认组织Code
			orgMap.put("orgCode", String.valueOf(CherryConstants.ORG_CODE_ALL));
			// 默认组织名称
			orgMap.put("orgName", getText("PPL00029"));
			if (null != orgInfoList && !orgInfoList.isEmpty()) {
				orgInfoList.add(0, orgMap);
			} else {
				orgInfoList = new ArrayList<Map<String, Object>>();
				orgInfoList.add(orgMap);
			}
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(defaultMap);
		} else {
			// 组织Code
			orgMap.put("orgCode", orgCode);
			// 组织名称
			orgMap.put("orgName", userInfo.getOrgName());
			orgInfoList = new ArrayList<Map<String, Object>>();
			orgInfoList.add(orgMap);
			// 品牌Code
			String brandCode = userInfo.getBrandCode();
			// 总部用户登录的时候
			if (String.valueOf(CherryConstants.Brand_CODE_ALL).equals(brandCode)) {
				// 查询品牌CodeList
				brandInfoList = binOLPLSCF14_BL.getBrandCodeList(orgMap);
				if (null != brandInfoList && !brandInfoList.isEmpty()) {
					brandInfoList.add(0, defaultMap);				
				} else {
					brandInfoList = new ArrayList<Map<String, Object>>();
					brandInfoList.add(defaultMap);				
				}
			} else {
				// 品牌Code
				defaultMap.put("brandCode", userInfo.getBrandCode());
				 // 品牌名称
				defaultMap.put("brandName", userInfo.getBrandName());
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(defaultMap);
			}
		}
	}

	/**
	 * 根据组织查询品牌List
	 * 
	 * @return null
	 */
	public String searchBrand() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put("orgCode", orgCode);
		// 查询品牌CodeList
		List<Map<String, Object>>list = binOLPLSCF14_BL.getBrandCodeList(map);
		Map<String, Object> defaultMap = new HashMap<String, Object>();
		 // 默认品牌ID
		defaultMap.put("brandCode", String.valueOf(CherryConstants.Brand_CODE_ALL));
		 // 默认品牌名称
		defaultMap.put("brandName", getText("PPL00030"));
		if (null != list && !list.isEmpty()) {
			list.add(0, defaultMap);				
		} else {
			list = new ArrayList<Map<String, Object>>();
			list.add(defaultMap);				
		}
		ConvertUtil.setResponseByAjax(response, list);
		return null;
	}
	
	/**
	 * 保存文件
	 * @return null
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public String saveFile() throws Exception{
		try{
		// 文件信息list
		List<Map<String, Object>> fileList = (List<Map<String, Object>>) JSONUtil.deserialize(fileInfoStr);
		// 插入文件信息List
		List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
		// 更新文件信息List
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		// 存放list
		Map<String, Object> fileMap = new HashMap<String, Object>();
		// 验证文件
		if (!validateForm(fileList)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 设置文件类型和取得文件内容
		for(Map<String, Object> fileInfo : fileList){
			// 根据文件后缀名得出文件类型
			int fileIndex = ((String) fileInfo.get("fileName")).lastIndexOf(".");
			String fileSuffix = ((String) fileInfo.get("fileName")).substring(fileIndex+1);
			// 从code值中取得对应文件类型
			String fileType = CodeTable.getVal("1150", fileSuffix);
			fileInfo.put("fileType", fileType);
			// 存放文件内容
			String fileContent = "";
			// 文件地址
			String path = PropertiesUtil.pps.getProperty("CHERRY_HOME") + System.getProperty("file.separator") + 
				"fileContent" + System.getProperty("file.separator") + (String) fileInfo.get("fileName");
			FileInputStream in = null;
			try {
				// 读文件
				File file = new File(path);
		        in = new FileInputStream(file);
		        // 取得文件内容
		        fileContent = IOUtils.toString(in, "UTF-8");
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				IOUtils.closeQuietly(in);
			}
			// 对应Map中插入文件内容
			fileInfo.put("fileContent", fileContent);
			// 取得文件表中的文件id
			Map<String, Object> hasFileMap = binOLPLSCF14_BL.getFileCount(fileInfo);
			// 存在对应文件ID，则做更新操作，反之则插入
			if(null == hasFileMap){
				insertList.add(fileInfo);
			}else{
				// 更新对应Id内容
				fileInfo.put("fileId", hasFileMap.get("fileId"));
				updateList.add(fileInfo);
			}
		}
		// 参数Map中插入List信息
		fileMap.put("insertList", insertList);
		fileMap.put("updateList", updateList);
		// 处理List，更新和插入操作
		binOLPLSCF14_BL.tran_handleFile(fileMap);
		// 重新加载文件存储list
		binolcm30IF.reloadAllFiles();
		}catch (Exception e){
			this.addActionError(e.getMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}finally{
			// 删除文件夹
			File f = new File(PropertiesUtil.pps.getProperty("CHERRY_HOME") + System.getProperty("file.separator") + "fileContent");
			FileUtils.deleteDirectory(f);
		}
		this.addActionMessage(getText("PPL00043"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
//	private boolean isAdmin(){
//	       // 登陆用户信息
//        UserInfo userInfo = (UserInfo) session
//                .get(CherryConstants.SESSION_USERINFO);
//        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE && CherryConstants.ORGANIZATION_CODE_DEFAULT.equals(String.valueOf(userInfo.getBIN_OrganizationInfoID()))){
//            return true;
//        }else{
//            return false;
//        }
//	}

	private boolean validateForm(List<Map<String, Object>> fileList){
		// 验证结果
		boolean isCorrect = true;
		// 验证文件信息
		for(Map<String, Object> fileInfo : fileList){
			boolean continueFlag = true;
			// 验证是否有重复
			for(Map<String, Object> fileCompare : fileList){
				if(!fileInfo.get("indexFlag").equals(fileCompare.get("indexFlag")) &&
						fileInfo.get("fileCode").equals(fileCompare.get("fileCode")) &&
						fileInfo.get("orgCode").equals(fileCompare.get("orgCode")) &&
						fileInfo.get("brandCode").equals(fileCompare.get("brandCode"))){
					this.addActionError(getText("PPL00041", new String[]{(String) fileInfo.get("fileCode")}));
					isCorrect = false;
					continueFlag = false;
					break;
				}
			}
			if(!continueFlag){
				break;
			}
		}
		return isCorrect;
	}
}
