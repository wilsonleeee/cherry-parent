/*
 * @(#)CherryFileStore.java     1.0 2011/11/21
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
package com.cherry.cm.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM30_IF;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;

/**
 * 文件储存
 * 
 * @author hub
 * @version 1.0 2011.11.21
 */
public class CherryFileStore implements InitializingBean, BINOLCM30_IF{
	
	protected static final Logger logger = LoggerFactory.getLogger(CherryFileStore.class);
	
	/** 储存文件Map */
	private Map<String, Object> fileStoresMap;
	
	@Resource
	private BaseConfServiceImpl baseConfServiceImpl;
	
	/** 组织全体共通*/
	static final int ORG_CODE_ALL = -9999;
	
	/** 品牌全体共通*/
	static final int Brand_CODE_ALL = -9999;
	
	/** SqlIDkey */
	static final String IBATIS_SQL_ID = "ibatis_sql_id";	
	
	/** 程序名 */
	static final String PGM_NAME = "CherryFileStore";
	
	/** 更新成功*/
	static final int EXECUTE_SUCCESS = 1;
	
	/** 更新失败*/
	static final int EXECUTE_ERROR = -1;
	
	private void setFileStoresMap(List<FileStoreDTO> fileStoreList) {
		if (null == this.fileStoresMap) {
			this.fileStoresMap = new HashMap<String, Object>();
		}
		if (null != fileStoreList) {
			for (FileStoreDTO fileStoreDTO : fileStoreList) {
				// 组织代码+品牌代码+文件代码
				String key = fileStoreDTO.getOrgCode() + "_" +
								fileStoreDTO.getBrandCode() + "_" +
								fileStoreDTO.getFileCode();
				this.fileStoresMap.put(key, fileStoreDTO);
			}
		}
	}

	/**
	 * <p>
	 * 保存配置文件
	 * </p>
	 * 
	 * @param
	 * @return
	 * 
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		loadAllFiles();
	}
	
	/**
	 * <p>
	 * 加载所有文件
	 * </p>
	 * 
	 * 
	 */	
	private void loadAllFiles() throws Exception {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(IBATIS_SQL_ID, "CherryFileStore.getFileStoreList");
			// 取得储存文件一览
			List<FileStoreDTO> fileStoreList = baseConfServiceImpl.getList(paramMap);
			setFileStoresMap(fileStoreList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * <p>
	 * 重新加载所有文件
	 * </p>
	 * 
	 * @throws Exception 
	 * 
	 */	
	@Override
	public void reloadAllFiles() throws Exception {
		loadAllFiles();
	}
	
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
	@Override
	public List<FileStoreDTO> getFileStoresByCate(String fileCategory, String orgCode, String brandCode) {
		if (null != this.fileStoresMap && 
				null != fileCategory && !"".equals(fileCategory) &&
				null != orgCode && !"".equals(orgCode) &&
				null != brandCode && !"".equals(brandCode)) {
			List<FileStoreDTO> fileStores = new ArrayList<FileStoreDTO>();
			for(Map.Entry<String, Object> en : this.fileStoresMap.entrySet()){
				// 文件储存 DTO
				FileStoreDTO fileStoreDTO = (FileStoreDTO) en.getValue();
				String key = en.getKey();
				// 文件类别匹配成功
				if (fileCategory.equals(fileStoreDTO.getFileCategory())) {
					// 组合key
					String combKey = orgCode + "_" + brandCode + "_" + fileStoreDTO.getFileCode();
					if (!key.equals(combKey)) {
						if (this.fileStoresMap.containsKey(combKey)) {
							continue;
						}
						// 组合key(品牌Code为全体共通)
						combKey = orgCode + "_"
								+ Brand_CODE_ALL + "_" + fileStoreDTO.getFileCode();
						if (!key.equals(combKey)) {
							if (this.fileStoresMap.containsKey(combKey)) {
								continue;
							}
						}
					}
					fileStores.add(fileStoreDTO);
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据文件类别取得对应的储存文件List
	 * </p>
	 * 
	 * @param String 文件代码
	 * @return FileStoreDTO 储存文件
	 * 
	 */	
	@Override
	public List<FileStoreDTO> getFileStoresByCate(String fileCategory) {
		Map<String, Object> commMap = getCommCode();
		if (commMap != null) {
			// 组织Code
			String orgCode = (String) commMap.get("orgCode");
			// 品牌Code
			String brandCode = (String) commMap.get("brandCode");
			return getFileStoresByCate(fileCategory, orgCode, brandCode);
		}
		return null;
	}
	
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
	@Override
	public FileStoreDTO getFileStoreByCode(String fileCode, String orgCode, String brandCode) {
		if (null != this.fileStoresMap && 
				null != fileCode && !"".equals(fileCode) &&
				null != orgCode && !"".equals(orgCode) &&
				null != brandCode && !"".equals(brandCode)) {
			// 组合key
			String combKey = orgCode + "_" + brandCode + "_" + fileCode;
			if (!this.fileStoresMap.containsKey(combKey)) {
				// 组合key(品牌Code为全体共通)
				combKey = orgCode + "_"
						+ Brand_CODE_ALL + "_" + fileCode;
				if (!this.fileStoresMap.containsKey(combKey)) {
					// 组合key(组织Code和品牌Code为全体共通)
					combKey = ORG_CODE_ALL + "_"
							+ Brand_CODE_ALL + "_" + fileCode;
				}
			}
			return (FileStoreDTO) this.fileStoresMap.get(combKey);
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据文件代码取得对应的储存文件
	 * </p>
	 * 
	 * @param String 文件代码
	 * @return FileStoreDTO 储存文件
	 * 
	 */	
	@Override
	public FileStoreDTO getFileStoreByCode(String fileCode) {
		Map<String, Object> commMap = getCommCode();
		if (commMap != null) {
			// 组织Code
			String orgCode = (String) commMap.get("orgCode");
			// 品牌Code
			String brandCode = (String) commMap.get("brandCode");
			return getFileStoreByCode(fileCode, orgCode, brandCode);
		}
		return null;
	}
	
	/**
	 * <p>
	 * 保存文件
	 * </p>
	 * 
	 * @param FileStoreDTO 储存文件
	 * 
	 */	
	@Override
	public int saveFileContent(FileStoreDTO fileStoreDTO) {
		// 保存结果
		int result = 0;
		if (null != fileStoreDTO) {
			// 作成程序名
			fileStoreDTO.setCreatePGM(PGM_NAME);
			// 更新程序名
			fileStoreDTO.setUpdatePGM(PGM_NAME);
			// 作成者
			fileStoreDTO.setCreatedBy(PGM_NAME);
			// 更新者
			fileStoreDTO.setUpdatedBy(PGM_NAME);
			if (0 != fileStoreDTO.getFileStoreId()) {
				// 更新文件
				result = baseConfServiceImpl.update(fileStoreDTO, "CherryFileStore.updateFileContent");
			} else {
				// 新增文件
				baseConfServiceImpl.save(fileStoreDTO, "CherryFileStore.insertFileStore");
				if (fileStoreDTO.getFileStoreId() > 0) {
					// 组织代码+品牌代码+文件代码
					String key = fileStoreDTO.getOrgCode() + "_" +
									fileStoreDTO.getBrandCode() + "_" +
									fileStoreDTO.getFileCode();
					this.fileStoresMap.put(key, fileStoreDTO);
					result = 1;
				}
			}
		}
		if (result > 0) {
			int modifyCount = fileStoreDTO.getModifyCount();
			modifyCount++;
			// 更新次数增加
			fileStoreDTO.setModifyCount(modifyCount);
		}
		return result;
	}
	
	/**
	 * <p>
	 * 取得组织和品牌代码
	 * </p>
	 * 
	 * 
	 * @return Map 组织和品牌代码
	 * 
	 */	
	private Map<String, Object> getCommCode() {
		Map<String, Object> commMap = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		if (null != request) {
			HttpSession session = request.getSession();
			if (null != session) {
				// 用户信息
				UserInfo userInfo = (UserInfo) session
						.getAttribute("userinfo");
				// 组织Code
				String orgCode = null;
				// 品牌Code
				String brandCode = null;
				if (null != userInfo) {
					// 组织Code
					orgCode = userInfo.getOrganizationInfoCode();
					// 品牌Code
					brandCode = userInfo.getBrandCode();
				} else {
					// 组织Code
					orgCode = (String) session.getAttribute("orgCode");
					// 品牌Code
					brandCode = (String) session.getAttribute("brandCode");
				}
				commMap.put("orgCode", orgCode);
				commMap.put("brandCode", brandCode);
			}
		}
		return commMap;
	}
	
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
	@Override
	public int updateWorkFlowFile(Map map) {
		// 规则字符串
		String ruleJSONString = (String) map.get("RuleJSONString");
		ruleJSONString = null == ruleJSONString? "" : ruleJSONString;
		// 组织代码
		String orgCode = (String) map.get("orgCode");
		// 品牌代码
		String brandCode = (String) map.get("brandCode");
		// 工作流文件的名称 
		String fileName = (String) map.get("FileName");
		// 根据文件代码取得对应的储存文件
		FileStoreDTO fileStoreDTO = getFileStoreByCode(fileName, orgCode, brandCode);
		if (null == fileStoreDTO || CherryChecker.isNullOrEmpty(fileStoreDTO.getFileContent())) {
			// 更新失败
			return EXECUTE_ERROR;
		}
		// 将工作流文件转换为文档类型
		Document document = FileUtil.string2Doc(fileStoreDTO.getFileContent());
		if (null == document) {
			return EXECUTE_ERROR;
		}
		// 存放step的ID
		int[] steps = (int[]) map.get("RuleOnStep");
		// 存放action的ID
		int[] actions = (int[]) map.get("RuleOnAction");
		// step的XPath表达式
		String stepXPath = getXPath(steps, 0);
		// action的XPath表达式
		String actionXPath = getXPath(actions, 1);
		StringBuffer xpathBuffer = new StringBuffer();
		if (null != stepXPath) {
			xpathBuffer.append(stepXPath);
		}
		if (null != actionXPath) {
			if (null != stepXPath) {
				xpathBuffer.append("|");
			}
			xpathBuffer.append(actionXPath);
		}
		// XPath表达式
		String xpath = xpathBuffer.toString();
		if ("".equals(xpath)) {
			return EXECUTE_ERROR;
		}
		List<Element> list = document.selectNodes(xpath);
		if (null == list) {
			return EXECUTE_ERROR;
		}
		for (Element ele : list) {
			List<Element> elements = ele.elements();
			if (null != elements) {
				for (Element e : elements) {
					if ("OS_Rule".equals(e.attributeValue("name"))) {
						elements.remove(e);
						break;
					}
				}
				Element e = DocumentHelper.createElement("meta");
				e.addAttribute("name", "OS_Rule");
				e.addText(ruleJSONString);
				elements.add(0, e);
			}
		}
		// 更新后的工作流文件内容
		String fileContentNew = FileUtil.Doc2String(document);
		FileStoreDTO fileStoreNew = null;
		if (null != fileStoreDTO.getOrgCode() && fileStoreDTO.getOrgCode().equals(orgCode) 
				&& null != fileStoreDTO.getBrandCode() && fileStoreDTO.getBrandCode().equals(brandCode)) {
			fileStoreNew = fileStoreDTO;
		} else {
			try {
				fileStoreNew = new FileStoreDTO();
				// 复制一份新的文件储存 DTO
				ConvertUtil.convertDTO(fileStoreNew, fileStoreDTO, true);
				fileStoreNew.setFileStoreId(0);
				fileStoreNew.setOrgCode(orgCode);
				fileStoreNew.setBrandCode(brandCode);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return EXECUTE_ERROR;
			}
		}
		// 刷新内存的工作流文件内容
		fileStoreNew.setFileContent(fileContentNew);
		// 更新数据库的文件内容
		int result = saveFileContent(fileStoreNew);
		if (0 == result) {
			return EXECUTE_ERROR;
		}
		// 更新成功
		return EXECUTE_SUCCESS;
	}
	
	/**
	 * 将字符串类型的文档内容生成文档
	 * 
	 * @param nodes int数组 存放step或者action的ID
	 * @param flag 0: 表示step  1: 表示action
	 * @return XPath表达式
	 * 
	 */
	private String getXPath(int[] nodes, int flag) {
		if (null != nodes && nodes.length > 0) {
			StringBuffer buffer = new StringBuffer();
			// 节点名称
			String nodeName = (0 == flag)? "step" : "action";
			buffer.append("//").append(nodeName).append("[");
			for (int i = 0; i < nodes.length; i++) {
				if (0 != i) {
					buffer.append(" or ");
				}
				buffer.append("@id='").append(nodes[i]).append("'");
			}
			buffer.append("]");
			return buffer.toString();
		}
		return null;
	}
}
