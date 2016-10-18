/*
 * @(#)DBVersionServlet.java     1.0 2012/06/12
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * DBVersionServlet
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2012.06.12
 */
@SuppressWarnings("unchecked")
public class DBVersionServlet extends HttpServlet {

	private static final long serialVersionUID = 632111374600003235L;

	private static Logger logger = LoggerFactory
			.getLogger(DBVersionServlet.class.getName());

	public DBVersionServlet() {
		super();
	}

	public void init() {
		String brandVersion = ConvertUtil.getString(
				PropertiesUtil.pps.getProperty("BRAND_DB_VERSION")).trim();
		String configVersion = ConvertUtil.getString(
				PropertiesUtil.pps.getProperty("CONFIG_DB_VERSION")).trim();
		// 品牌数据库版本号验证
		if (!CherryChecker.checkDBVersion(brandVersion)) {
			logger.error("===================================================");
			logger.error("============品牌数据库版本号格式验证不通过===========");
			logger.error("===================================================");
		}else if(!CherryChecker.checkDBVersion(configVersion)){
			logger.error("===================================================");
			logger.error("============配置数据库版本号格式验证不通过===========");
			logger.error("===================================================");
		}else {
			ServletContext servletContext = this.getServletContext();
			ApplicationContext appContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			BaseConfServiceImpl bs = (BaseConfServiceImpl) appContext
					.getBean("baseConfServiceImpl");
			// 共通参数
			Map<String, Object> commMap = getCommMap(bs);
			// 更新配置数据库版本号
			Map<String, Object> configMap = new HashMap<String, Object>(commMap);
			configMap.put("orgCode", CherryConstants.ORGANIZATION_CODE_DEFAULT);
			configMap.put("brandCode", "CONFIG");
			updateDBVersion(bs, configMap, configVersion);
			
			// 更新品牌数据库版本号
			// 取得品牌数据库对应关系List
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.IBATIS_SQL_ID,
					"BINOLCMINC99.getBrandDBList");
			List<Map<String, Object>> list = bs.getList(paramMap);
			if (null != list) {
				for (Map<String, Object> map : list) {
					map.putAll(commMap);
					updateDBVersion(bs, map, brandVersion);
				}
			}
		}
	}
	
	/**
	 * 更新数据库版本号
	 * 
	 * @param bs
	 * @param map
	 */
	private void updateDBVersion(BaseConfServiceImpl bs, Map<String, Object> map,String newVersion){
		map.put("version", newVersion);
		String oldVersion = ConvertUtil.getString(
				bs.get(map,"BINOLCMINC99.getDBVersion")).trim();
		if ("".equals(oldVersion)) {
			// 保存品牌当前数据库版本
			bs.save(map,"BINOLCMINC99.insertDBVersion");
		} else if (!newVersion.equals(oldVersion)) {
			// 更新品牌当前数据库版本
			bs.update(map,"BINOLCMINC99.updateDBVersion");
		}
	}

	private Map<String, Object> getCommMap(BaseConfServiceImpl bs) {
		Map<String, Object> map = new HashMap<String, Object>();
		String time = bs.getSYSDate();
		// 作成者
		map.put(CherryConstants.CREATEDBY, "SYSTEM");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "SYSTEM");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "DBVersionServlet");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "DBVersionServlet");
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, time);
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, time);
		return map;
	}

	public void destory() {
		super.destroy();
	}
}
