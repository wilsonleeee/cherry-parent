/*  
 * @(#)BINOLBSCHA01_Action.java     1.0 2011/05/31      
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
package com.webconsole.bl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cherry.cm.cmbeans.RoleInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BatchWebException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.JsclPBKDF2WithHMACSHA256;
import com.webconsole.service.LoginService;
@SuppressWarnings("unchecked")
public class LoginBusinessLogic {
	@Resource
	private LoginService loginservice;	
	
	public Map<String,String> userLogin(String name)throws Exception{
		List list = loginservice.getDBByName(name);
		if(null==list||list.size()<1){
			//账号或密码错误
			throw new BatchWebException("E001");
		}
		Map map = (Map)list.get(0);		
		Map<String,String> retMap = new HashMap();
		retMap.put("NewDataSource", String.valueOf(map.get("DataBaseName")));
		//retMap.put("OldDataSource", String.valueOf(map.get("OldDataSourceName")));
		return retMap;
	}
	
	public UserInfo  checkUser(String name,String psd, String csrftoken)throws Exception{
		//将密码进行加密比较
//		psd = changePsd(psd);		
//		
//		//检查账号是否正确
//		List list = loginservice.checkAccount(name,psd);
//		if(list==null||list.size()==0){
//			//账号或密码错误
//			throw new BatchWebException("E001");
//		}
		
		List<Map<String, Object>> list = loginservice.getUserInfoByName(name);
		if(list==null||list.size()==0){
			//账号或密码错误
			throw new BatchWebException("E001");
		}
		String password = (String)list.get(0).get("PassWord");
		String csrftoken_Decrption = null;
		try {
			csrftoken_Decrption = JsclPBKDF2WithHMACSHA256.DecrptPBKDF2WithHMACSHA256(decryptPwd(password), psd);
		} catch (Exception e) {
			
		}
		if(csrftoken_Decrption == null || !csrftoken_Decrption.equals(csrftoken)) {
			//账号或密码错误
			throw new BatchWebException("E001");
		}
		
		//账号存在,取得账号标识ID
		String userID = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));
		String privilageFlag = String.valueOf(((HashMap)list.get(0)).get("BIFlag"));
		String orgid = String.valueOf(((HashMap)list.get(0)).get("BIN_OrganizationInfoID"));
		String orgcode = String.valueOf(((HashMap)list.get(0)).get("OrgCode"));
		if(!"9".equals(privilageFlag)){
			//没有操作batch的权限
			throw new BatchWebException("E002");
		}
		UserInfo userinfo = new UserInfo();
		userinfo.setLoginName(name);
		userinfo.setBIN_UserID(Integer.parseInt(userID));
		userinfo.setBIN_OrganizationInfoID(Integer.parseInt(orgid));
		userinfo.setOrgCode(orgcode);
		return userinfo;
	}
	
	/**
	 * 密码加密
	 * @param psd
	 * @return
	 * @throws Exception 
	 */
	private String changePsd(String psd) throws Exception{
			//加密
			if(psd != null && !"".equals(psd)) {
				// 加密处理
				DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
				psd =  des.encrypt(psd);
			}			
			return psd;

	}
	
	/**
	 * 密码解密
	 * @param psd
	 * @return
	 * @throws Exception 
	 */
	private String decryptPwd(String psd) throws Exception{
			// 解密
			if(psd != null && !"".equals(psd)) {
				// 解密处理
				DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
				psd =  des.decrypt(psd);
			}			
			return psd;

	}
}
