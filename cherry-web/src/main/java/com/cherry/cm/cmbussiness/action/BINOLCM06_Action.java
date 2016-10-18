/*
 * @(#)BINOLCM06_Action.java     1.0 2010/12/09
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
package com.cherry.cm.cmbussiness.action;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 上传文件共通 Action
 * 
 * @author hub
 * @version 1.0 2010.12.09
 */
@SuppressWarnings("unchecked")
public class BINOLCM06_Action extends BaseAction {

	private static final long serialVersionUID = 1953629274853567908L;

	/** 上传的文件 */
	private File fileUp;

	/** 上传的文件的名称 */
	private String fileUpFileName;

	/** 提示信息 */
	private String message;

	public File getFileUp() {
		return fileUp;
	}

	public void setFileUp(File fileUp) {
		this.fileUp = fileUp;
	}
	
	public String getFileUpFileName() {
		return fileUpFileName;
	}

	public void setFileUpFileName(String fileUpFileName) {
		this.fileUpFileName = fileUpFileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 上传文件
	 * 
	 * @return String 上传结果信息画面
	 * 
	 * @throws Exception
	 */
	public String uploadImage() throws Exception {
		Map msgMap = new HashMap();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileUp);
			// 上传的图片超过最大字节数时
			if (fis.available() > CherryConstants.IMAGE_MAX_SIZE * 1048576) {
				msgMap.put("msg", "上传的图片大于" + CherryConstants.IMAGE_MAX_SIZE
						+ "M");
				msgMap.put("result", CherryConstants.result_NG);
				message = JSONUtil.serialize(msgMap);
				return INPUT;
			}
			// 验证上传图片的类型
			if (!checkFileType(CherryConstants.IMAGE_TYPES)) {
				msgMap.put("msg", "上传的图片类型不正确");
				msgMap.put("result", CherryConstants.result_NG);
				message = JSONUtil.serialize(msgMap);
				return INPUT;
			}
			// 保存地址
			String savePath = PropertiesUtil.pps.getProperty("uploadFilePath.tempImagePath");
			String fileNameNew = renameFile();
			File targetFile = new File(savePath, fileNameNew); 
			// 保存文件
	        FileUtils.copyFile(fileUp, targetFile);	
	        msgMap.put("imagePath", fileNameNew);
		} catch (Exception e) {
			msgMap.put("msg", "上传图片失败");
			msgMap.put("result", CherryConstants.result_NG);
			message = JSONUtil.serialize(msgMap);
			return INPUT;
		} finally {
			if (null != fis) {
				fis.close();
			}
		}
		msgMap.put("msg", "上传图片成功");
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

	/**
	 * 验证文件类型是否正确
	 * 
	 * @return boolean 验证结果
	 * 
	 */
	private boolean checkFileType(String types) {
		if (null != types && !"".equals(types)) {
			// 文件扩展名
			String extName = "";
			if (fileUpFileName.lastIndexOf(".") >= 0) {
				extName = fileUpFileName.substring(fileUpFileName.lastIndexOf(".") + 1);
			}
			String[] typeArr = types.split(",");
			for (String type : typeArr) {
				if (extName.toLowerCase().equals(type)) {
					return true;
				}
			}
		}
		return false;
	}
}
