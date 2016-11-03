/*	
 * @(#)BINBECTSMG10_BL.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.ct.smg.interfaces.BINBECTSMG10_IF;
import com.cherry.ct.smg.service.BINBECTSMG10_Service;

/**
 * 短信模板管理BL
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG10_BL implements BINBECTSMG10_IF{
	
	@Resource
	private BINBECTSMG10_Service binBECTSMG10_Service;
	
	/** 模板内容 */
	private static Map<String, Object> templateMap = new HashMap<String, Object>();
	
	/**
     * 取得模板编号
     * 
     * @param brandCode 品牌代码
     * @param content 模板内容
     * @return String 模板编号
     */
	public String getTemplateCode(String brandCode, String content) {
		String key = brandCode + "_" + content;
		// 模板编号
		Map<String, Object> templateInfo = (Map<String, Object>) templateMap.get(key);
		if (null == templateInfo) {
			synchronized (templateMap) {
				templateInfo = (Map<String, Object>) templateMap.get(key);
				if (null == templateInfo) {
					// 取得品牌的短信模板编号
					templateInfo = getSmsTemplateCode(brandCode, content);
					if (null != templateInfo && !templateInfo.isEmpty()) {
						templateMap.put(key, templateInfo);
						return (String) templateInfo.get("tempCode");
					}
				}
			}
		} else {
			return (String) templateInfo.get("tempCode");
		}
		return null;
	}
	
	/**
     * 通过短信内容反推模板编号及变量值
     * 
     * @param brandCode 品牌代码
     * @param content 短信内容
     * @return Map 模板编号及变量值
     */
	public Map<String, Object> getTemplateInfo(String brandCode, String content) {
		// 取得品牌的短信模板编号
		Map<String, Object> templateInfo = getSmsTemplateCode(brandCode, content);
		if (null == templateInfo || templateInfo.isEmpty()) {
			// 取得品牌所有的短信模板
			List<Map<String, Object>> templateList = binBECTSMG10_Service.getBrandTemplateList(brandCode);
			if (null != templateList && !templateList.isEmpty()) {
				// 去除短信内容中的短信签名
				content = removeSign(content);
				// 参数集合
				Map<String, Object> paramsInfo = new HashMap<String, Object>();
				for (Map<String, Object> map : templateList) {
					paramsInfo.clear();
					// 数据库中的模板
					String tContent = (String) map.get("content");
					int index = tContent.indexOf("<#");
					if (index < 0) {
						continue;
					}
					tContent = removeSign(tContent);
					// 通过短信内容和短信模板反推变量值
					if (getParams(content, tContent, paramsInfo)) {
						templateInfo = new HashMap<String, Object>();
						templateInfo.put("tempCode", map.get("tempCode"));
						templateInfo.put("paramsInfo", paramsInfo);
						return templateInfo;
					}
				}
			}
		} else {
			return templateInfo;
		}
		return null;
	}
	
	/**
     * 通过短信内容和短信模板反推变量值
     * 
     * @param content 短信内容
     * @param tContent 短信模板
     * @return boolean true: 匹配成功 false: 匹配失败
     */
	private boolean getParams (String content, String tContent, Map<String, Object> params) {
		int index1 = tContent.indexOf("<#");
		if (index1 < 0) {
			return false;
		}
		if (index1 > 0) {
			// 变量左边的字符串
			String before = tContent.substring(0, index1);
			if (content.indexOf(before) != 0) {
				return false;
			}
			tContent = tContent.replace(before, "");
			content = content.replace(before, "");
		}
		int index2 = tContent.indexOf("#>");
		// 变量的key
		String key = tContent.substring(2, index2);
		if (index2 < tContent.length() - 2) {
			tContent = tContent.substring(index2 + 2);
			// 变量右边的字符串
			String after = null;
			int index3 = tContent.indexOf("<#");
			if (index3 < 0) {
				after = tContent;
			} else if (index3 == 0){
				return false;
			} else {
				after = tContent.substring(0, index3);
			}
			int zindex = content.indexOf(after);
			if (zindex < 0) {
				return false;
			}
			params.put(key, content.substring(0, zindex));
			if (index3 > 0) {
				tContent = tContent.replace(after, "");
				content = content.substring(zindex).replace(after, "");
				return getParams (content, tContent, params);
			}
		} else {
			params.put(key, content);
		}
		return true;
	}
	
	/**
     * 去除短信内容中的短信签名
     * 
     * @param content 短信内容
     * @return String 去除短信签名后的内容
     */
	private String removeSign(String content) {
		int index = content.indexOf("【");
		if (index > 0) {
			content = content.substring(0, index);
		} else if (index == 0){
			index = content.indexOf("】");
			if (content.length() > index + 1) {
				content = content.substring(index + 1);
			}
		}
		return content;
	}
	
	/**
     * 刷新品牌的短信模板
     * 
     * @param brandCode 品牌代码
     * @return
     */
	private void upTemplate(String brandCode, String content) {
		String key = brandCode + "_" + content;
		synchronized (templateMap) {
			// 取得品牌的短信签名
			templateMap.put(key, getSmsTemplateCode(brandCode, content));
		}
	}
	
	/**
     * 取得品牌的短信模板编号
     * 
     * @param map
     * @return String
     * 		短信模板编号
     */
    public Map<String, Object> getSmsTemplateCode(String brandCode, String content) {
    	return binBECTSMG10_Service.getSmsTemplateCode(brandCode, content);
    }
	
	/**
     * 取得品牌的短信模板列表
     * 
     * @param map
     * @return String
     * 		短信模板列表
     */
    public List<Map<String, Object>> getSmsTemplateList(Map<String, Object> map) {   
    	// 取得品牌的短信模板列表
    	return binBECTSMG10_Service.getSmsTemplateList(map);
    }
    
    /**
     * 更新短信模板
     * 
     * @param map
     * @return int
     * 		执行结果
     */
	public int tran_upTemplate(Map<String, Object> map) {
		commParamsForUp(map);
		int templateId = 0;
		if (map.get("templateId") != null) {
			templateId = Integer.parseInt(String.valueOf(map.get("templateId")));
		}
		if (0 == templateId) {
			// 新增短信模板
			binBECTSMG10_Service.addTemplateInfo(map);
		} else {
			// 更新短信模板
			binBECTSMG10_Service.updateTemplateInfo(map);
		}
		upTemplate((String) map.get("brandCode"), (String) map.get("content"));
		return CherryBatchConstants.BATCH_SUCCESS;
	}
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBECTSMG10");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBECTSMG10");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBECTSMG10");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBECTSMG10");
	}
}
