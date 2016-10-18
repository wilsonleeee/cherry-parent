/*	
 * @(#)BINOLCM37_Action.java     1.0 2013/09/22		
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
import java.io.InputStream;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.FileUtil;

/**
 * 导出下载文件共通Action
 * 
 * @author WangCT
 * @version 1.0 2013/09/22
 */
public class BINOLCM37_Action extends BaseAction {

	private static final long serialVersionUID = -5749158716047096068L;
	
	/**
     * 导出下载文件
     */
    public String download() throws Exception {
    	
    	File file = new File(tempFilePath);
    	if(file.exists()) {
    		downloadFileName = file.getName();
        	inputStream = new FileInputStream(file);
        	
        	if(downloadFileName.endsWith(".zip")) {
        		return "downloadZip";
        	} else if(downloadFileName.endsWith(".csv")) {
        		return "downloadCsv";
        	} else if(downloadFileName.endsWith(".xls")) {
        		return "downloadExcel";
        	}
    	}
    	return null;
    }
	
	/** 文件输入流 */
    private InputStream inputStream;

    /** 下载文件名 */
    private String downloadFileName;
    
    /** 下载文件地址 */
    private String tempFilePath;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

}
