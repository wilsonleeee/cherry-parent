/*
 * @(#)CherrySecretServlet.java     1.0 2013/10/15
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
 * 系统启动时从数据库中读取key
 * @author dingyongchang
 * 
 */
@SuppressWarnings("unchecked")
public class CherryBatchSecretServlet extends HttpServlet {

	private static final long serialVersionUID = 632111374600003234L;

	private static Logger logger = LoggerFactory.getLogger(CherryBatchSecretServlet.class.getName());

	public CherryBatchSecretServlet() {
		super();
	}

	public void init(){
		ServletContext servletContext = this.getServletContext();
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		BaseConfServiceImpl bs = (BaseConfServiceImpl) appContext.getBean("baseConfServiceImpl");

		// 获取密钥
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getCherrySecret");
		List<Map<String, Object>> list = bs.getList(paramMap);
		
		if(list==null||list.size()==0){
			// 如果数据库中没配置密钥，则加密解密功能不开启
			logger.error("===========================注意 ==========================");
			logger.error("==                                                     ==");
			logger.error("== 没有配置密钥，请注意确认是否正确!                     ==");
			logger.error("==                                                     ==");
			logger.error("=========================================================");
			return;
		}
		
		for (Map<String, Object> map : list) {
			CherryKeyStore store = new CherryKeyStore();
			String brandCode = ConvertUtil.getString(map.get("BrandCode"));
			store.setOrgCode(ConvertUtil.getString(map.get("OrgCode")));
			store.setBrandCode(brandCode);
			store.setSecretKeyBase64(ConvertUtil.getString(map.get("SecretKey")));
			store.setPublicKeyBase64(ConvertUtil.getString(map.get("PublicKey")));
			store.setPrivateKeyBase64(ConvertUtil.getString(map.get("PrivateKey")));
			try {
				CherryBatchSecret.putKeyStore(store);
				CherrySecret.putKeyStore(store);
				String str1 ="abc原始消息体,./[])(*&^%$#@!";
				String str2 = CherryBatchSecret.decryptData(brandCode,CherryBatchSecret.encryptData(brandCode,str1));
				if(str1.equals(str2)){
					logger.error("=========================== 注意 ==========================");
					logger.error("==                                                      ==");
					logger.error("== 品牌【" + brandCode + "】加解密功能已正常开启，测试通过，请确认! ==");
					logger.error("==                                                      ==");
					logger.error("==========================================================");
				}else{
					store.exceptionFlag = true;
					logger.error("=========================== 错误 ==========================");
					logger.error("==                                                      ==");
					logger.error("== 品牌【" + brandCode + "】加解密功能已开启，测试未通过，请确认! ==");
					logger.error("==                                                      ==");
					logger.error("==========================================================");
				}
			} catch (Exception e) {
				store.exceptionFlag = true;
				//密钥配置错误，会引起异常
				logger.error("=========================== 错误 ==========================");
				logger.error("==                                                      ==");
				logger.error("== 品牌【" + brandCode + "】加解密加载出现异常，请确认配置的密钥!              ==");
				logger.error("==                                                      ==");
				logger.error("==========================================================");
				logger.error("CherrySecret Error",e);
				return;
			}
		}		
	}

	public void destory() {
		super.destroy();
	}
}
