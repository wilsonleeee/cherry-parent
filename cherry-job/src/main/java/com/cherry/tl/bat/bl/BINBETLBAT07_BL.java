/*
 * @(#)BINBETLBAT07_BL.java     1.0 2013/10/16
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
package com.cherry.tl.bat.bl;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.tl.bat.service.BINBETLBAT07_Service;

/**
 * 
 * 清除临时文件处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/10/16
 */
public class BINBETLBAT07_BL {
	
	/** 清除临时文件处理Service */
	@Resource
	private BINBETLBAT07_Service binBETLBAT07_Service;
	
	/**
	 * 清除临时文件处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 */
	public int clearTempFiles(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			// 业务日期
			String bussinessDate = binBETLBAT07_Service.getBussinessDate(map);
			
			// 临时文件存放目录
			String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
			File f = new File(tempFilePath);
			if(f.exists() && f.isDirectory()) {
				File[] delFile = f.listFiles();
				for(int i = 0; i < delFile.length; i++) {
					Date date = new Date(delFile[i].lastModified());
					String fileDate = DateUtil.date2String(date, "yyyy-MM-dd");
					// 清除小于业务日期的临时文件
					if(fileDate.compareTo(bussinessDate) < 0) {
						// 删除目录（文件夹）以及目录下的文件 
						boolean result = this.deleteDirectory(delFile[i].getAbsolutePath());
						if(!result) {
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("ETL00012");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							cherryBatchLogger.BatchLogger(batchLoggerDTO1);
							return flag;
						}
					}
				}
			}
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00012");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("ETL00013");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
	}
	
	/** 
	 * 删除单个文件
	 * 
	 * @param sPath 被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false
	 */  
	public boolean deleteFile(String sPath) {
	    boolean flag = false;
	    File file = new File(sPath);
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {
	        file.delete();
	        flag = true;
	    }
	    return flag;
	}
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * 
	 * @param sPath 被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false 
	 */  
	public boolean deleteDirectory(String sPath) {
	    // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
	    if (!sPath.endsWith(File.separator)) {
	        sPath = sPath + File.separator;
	    }
	    File dirFile = new File(sPath);
	    // 如果dir对应的文件不存在，或者不是一个目录，则退出
	    if (!dirFile.exists() || !dirFile.isDirectory()) {
	        return false;
	    }
	    boolean flag = true;
	    // 删除文件夹下的所有文件(包括子目录)
	    File[] files = dirFile.listFiles();
	    for (int i = 0; i < files.length; i++) {
	        // 删除子文件
	        if (files[i].isFile()) {
	            flag = deleteFile(files[i].getAbsolutePath());
	            if (!flag) break;
	        }
	        else {// 删除子目录
	            flag = deleteDirectory(files[i].getAbsolutePath());
	            if (!flag) break;
	        }
	    }
	    if (!flag) return false;
	    // 删除当前目录
	    if (dirFile.delete()) {
	        return true;
	    } else {
	        return false;
	    }
	}

}
