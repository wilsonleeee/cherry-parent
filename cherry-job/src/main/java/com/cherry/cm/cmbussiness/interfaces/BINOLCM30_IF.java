/*      
 * @(#)BINOLCM18_IF.java     1.0 2011/08/30             
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.FileStoreDTO;

/**
 * 修改OSWorkFlow文件
 * @author dingyongchang
 *
 */
public interface BINOLCM30_IF {

	/**
	 * 修改工作流xml文件。
	 * 参数map中的key有:
	 * orgCode: 组织代码
	 * brandCode: 品牌代码
	 * FileName:工作流文件的名称 
	 * RuleJSONString：规则字符串，JSON格式
	 * RuleOnStep：int数组，存放step的ID
	 * RuleOnAction：int数组，存放action的ID
	 * 该方法的作用是将RuleJSONString所代表的字符串，以<meta name="OS_Rule">元素的形式添加到RuleOnStep
	 * 指定的step下(或者是RuleOnAction指定的action下)
	 * @param map 相关参数，具体说明请参见方法描述
	 * @return int 执行结果  1: 成功    -1：失败
	 */
	public int updateWorkFlowFile(Map map);		
	
	/**
	 * <p>
	 * 重新加载所有文件
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */	
	public void reloadAllFiles() throws Exception;
	
	/**
	 * <p>
	 * 根据文件类别取得对应的储存文件List
	 * </p>
	 * 
	 * @param String 文件类别
	 * @param String 组织代码
	 * @param String 品牌代码
	 * @return List 储存文件List
	 * 
	 */	
	public List<FileStoreDTO> getFileStoresByCate(String fileCategory, String orgCode, String brandCode);
	
	/**
	 * <p>
	 * 根据文件类别取得对应的储存文件List
	 * </p>
	 * 
	 * @param String 文件代码
	 * @return FileStoreDTO 储存文件
	 * 
	 */	
	public List<FileStoreDTO> getFileStoresByCate(String fileCategory);
	
	/**
	 * <p>
	 * 根据文件代码取得对应的储存文件
	 * </p>
	 * 
	 * @param String 文件代码
	 * @param String 组织代码
	 * @param String 品牌代码
	 * @return FileStoreDTO 储存文件
	 * 
	 */	
	public FileStoreDTO getFileStoreByCode(String fileCode, String orgCode, String brandCode);
	
	/**
	 * <p>
	 * 根据文件代码取得对应的储存文件
	 * </p>
	 * 
	 * @param String 文件代码
	 * @return FileStoreDTO 储存文件
	 * 
	 */	
	public FileStoreDTO getFileStoreByCode(String fileCode);
	
	/**
	 * <p>
	 * 保存文件
	 * </p>
	 * 
	 * @param FileStoreDTO 储存文件
	 * 
	 */	
	public int saveFileContent(FileStoreDTO fileStoreDTO);
	
}
