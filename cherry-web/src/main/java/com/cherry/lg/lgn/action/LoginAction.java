/*  
 * @(#)LoginAction.java     1.0 2011/05/31      
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
package com.cherry.lg.lgn.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.RoleInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM10_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.gadget.interfaces.GadgetIf;
import com.cherry.cm.gadget.interfaces.GadgetPrivilegeIf;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.lg.lgn.bl.LoginBusinessLogic;
import com.cherry.lg.lgn.service.LoginService;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.cherry.webservice.common.WebserviceDataSource;
import com.googlecode.jsonplugin.JSONUtil;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.*;

@SuppressWarnings("serial")
public  class LoginAction extends BaseAction {
	public LoginAction (){
		super();
	}

	private static Logger logger = LoggerFactory.getLogger(LoginAction.class.getName());
	@Resource
	BINOLCM10_BL binOLCM10_BL;
	@Resource
	BINOLCM14_BL binOLCM14_BL;
	@Resource
	BINOLCM19_BL binolcm19_bl;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	@Resource
    private LoginBusinessLogic loginbusinesslogic;  
	@Resource
	private LoginService loginservice;
	@Resource
	GadgetIf gadgetBL;
	@Resource
	GadgetPrivilegeIf gadgetPrivilegeBL;

	@Resource(name = "webserviceDataSource")
	private WebserviceDataSource webserviceDataSource;

    @Resource(name="binOLPLUPM04_BL")
    private BINOLPLUPM04_BL binOLPLUPM04_BL;
    
    private String redirectURL;
	
	public String getRedirectURL() {
		return redirectURL;
	}


	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	private String codeText ;	
	public String doLogin() throws Exception {
		try {
			//浏览器不符合要求，禁止登陆
			String strBrowser = judgeBrowser();
			if(strBrowser.equals("IE8")||strBrowser.equals("IE9")||strBrowser.equals("IE10")||strBrowser.equals("IE11")||strBrowser.equals("Firefox")
					||strBrowser.equals("Chrome")||strBrowser.equals("Safari")){
				
			}else{
				throw new CherryException("ECM00059");
			}
			
			if (request.getParameterMap().containsKey("codeText")) {
				ImageCaptchaService instance = (ImageCaptchaService) session
						.get(CherryConstants.SESSION_CHECK_IMAGE);
				boolean bl = instance.validateResponseForID(request
						.getSession().getId(), codeText);
				if (!bl) {
					throw new CherryException("ECM00014");
				}
			}

			String language = "";
			if (session.get("WW_TRANS_I18N_LOCALE") == null) {
				language = request.getLocale().toString();
			} else {
				language = String.valueOf(session.get("WW_TRANS_I18N_LOCALE"));
			}
			// 将语言放入session中
			session.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
						
			language="zh_CN";		
			// 取消数据库多语言对应，固定设zh_CN
			session.put(CherryConstants.SESSION_LANGUAGE, language);
			//CustomerContextHolder.setCustomerDataSourceType("LOGIN");
			
			String dbname = loginbusinesslogic.userLogin(txtname);
			
			//datasource
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,dbname);

			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(dbname);
			
			String csrftoken = (String)session.get("csrftoken");			
			String userID = loginbusinesslogic.checkUser(txtname, txtpsd, csrftoken);

			//存放在用户其他的信息(如密码过期日期、开始提醒修改密码日、上次登录时间、IP)
			Map<String,Object> otherInfo = new HashMap<String,Object>();
			// 根据登录名初始化登录者的基本信息
			UserInfo userinfo = loginbusinesslogic.userInfoInitial(userID,
					txtname, language,otherInfo);
			String ip = request.getHeader("x-forwarded-for");
			if (CherryChecker.isNullOrEmpty(ip)) {
				ip = request.getRemoteAddr();
			} 	
			if(CherryChecker.isNullOrEmpty(ip)){
				ip = "0.0.0.0";
			}
			userinfo.setLoginIP(ip);
			// userinfo.setPassword(txtpsd);
			userinfo.setLoginTime(new Date());
		    userinfo.setUserAgent(request.getHeader("User-Agent"));
		    userinfo.setSessionID(request.getSession().getId());

			session.put(CherryConstants.SESSION_USERINFO, userinfo);
			
	        //更新登录信息（登录时间，登录IP）
            loginbusinesslogic.updateLoginInfo(userinfo);
			
			List<RoleInfo> roleList = userinfo.getRolelist();
			List<RoleInfo> canRoleList = new ArrayList<RoleInfo>();
			List<RoleInfo> forbidRoleList = new ArrayList<RoleInfo>();
			int[] roleArray = new int[roleList.size()];
			for (RoleInfo role : roleList) {
				int i = 0;
				if ("0".equals(role.getPrivilegeFlag())) {
					// 禁止权限的角色
					forbidRoleList.add(role);
				} else {
					// 允许权限的角色
					canRoleList.add(role);
				}
				roleArray[i] = role.getBIN_RoleID();
				i++;
			}
			int[] canRroleArray = new int[canRoleList.size()];
			int[] forbidRroleArray = new int[forbidRoleList.size()];

			for (int i = 0; i < canRoleList.size(); i++) {
				canRroleArray[i] = canRoleList.get(i).getBIN_RoleID();
			}
			for (int i = 0; i < forbidRoleList.size(); i++) {
				forbidRroleArray[i] = forbidRoleList.get(i).getBIN_RoleID();
			}
			// 取得用户的权限
			// loginbusinesslogic.getPrivilege(roleArray,
			// userinfo.getBIN_UserID(), request);

			// 取得用户可操作的菜单
			loginbusinesslogic.menuInitial(canRroleArray, forbidRroleArray,
					request);

//			// 取得任务
//	       	HashMap<String,Object> pramMap = new HashMap<String,Object>();
//	    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//	    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
//	    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
//	    	pramMap.put("language", userinfo.getLanguage());
//	    	List<CherryTaskInstance> retList = binolcm19_bl.getUserTasks(pramMap);
//	    	if(retList.size()>0){
//	    		for(int i=0;i<retList.size();i++){
//	    			CherryTaskInstance ret = binolcm19_bl.getTaskInfo(retList.get(i));
//	    			ret.setTaskName(getText("OS.TaskName.ProType."+ret.getProType())+getText("OS.TaskName."+ret.getCurrentOperate())); 
//	    		}
//	    	}
//			request.setAttribute("TaskList", retList);
			
			request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, "LG");
			
			// 岗位为门店岗位的场合
			if(userinfo.getCategoryCode() != null && ("50".equals(userinfo.getCategoryCode()) 
					|| "DZ".equals(userinfo.getCategoryCode()))) {
				if(userinfo.getDepartCode() != null 
						&& !"".equals(userinfo.getDepartCode()) 
						&& !"null".equals(userinfo.getDepartCode())) {
					CounterInfo counterInfo = new CounterInfo();
					counterInfo.setOrganizationId(userinfo.getBIN_OrganizationID());
					counterInfo.setCounterCode(userinfo.getDepartCode());
					counterInfo.setCounterName(userinfo.getDepartName());
					session.put(CherryConstants.SESSION_CHERRY_COUNTERINFO, counterInfo);
				} else {
					this.addActionError(getText("ECM00012"));
					return "false";
				}
			}
			
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
	    	// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userinfo.getBIN_OrganizationInfoID());
			// 不是总部的场合
			if(userinfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE ) {
				map.put(CherryConstants.BRANDINFOID, userinfo.getBIN_BrandInfoID());
			}
	    	// 当前用户不是最大岗位级别的场合
			if(userinfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE ||binOLCM00_BL.isMaxPosCategoryGrade(map, userinfo.getCategoryGrade())) {
				session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "0");
			} else {
				session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "1");
			}
			map.put("userId", userinfo.getBIN_UserID());
			map.put("brandCode", userinfo.getBrandCode());
			map.put("orgCode", userinfo.getOrganizationInfoCode());
			// 保存数据权限到MongoDB
			gadgetPrivilegeBL.savePrivilegeToMongoDB(map);
			
			// 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("LG");
	    	
			map.put("userId", userinfo.getBIN_UserID());
			map.put("pageId", "0");
			map.put("doc", doc);
			// 取得小工具信息
			Map<String, Object> gadgetInfoMap = gadgetBL.getGadgetInfoList(map);
			List<Map<String, Object>> gadgetInfoPLList = (List)gadgetInfoMap.get("gadgetInfoList");
			if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
				for(Map<String, Object> gadgetInfo : gadgetInfoPLList) {
					String gadgetCode = (String)gadgetInfo.get("gadgetCode");
					if("BINOLLGTOP01".equals(gadgetCode) || "BINOLLGTOP07".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.sale"));
					} else if("BINOLLGTOP02".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.attendance"));
					} else if("BINOLLGTOP03".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.task"));
					} else if("BINOLLGTOP04".equals(gadgetCode) || "BINOLLGTOP08".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.saleTargetRpt"));
					} else if("BINOLLGTOP05".equals(gadgetCode) || "BINOLLGTOP06".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.saleCountByHours"));
					} else if("BINOLLGTOP09".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.orderTask"));
					}
				}
			}
			gadgetInfoMap.put("userInfo", userinfo);
			gadgetInfoMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			gadgetInfo = JSONUtil.serialize(gadgetInfoMap);
			
	        //组织ID
	        String organizationId = String.valueOf(userinfo.getBIN_OrganizationInfoID());
	        //品牌ID
	        String brandInfoId = String.valueOf(userinfo.getBIN_BrandInfoID());
	        //配置项控制是否禁止同一账号同时登录
	        boolean sameTimeLoginFlag = binOLCM14_BL.isConfigOpen("1102", organizationId, brandInfoId);
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put("sameTimeLoginFlag", sameTimeLoginFlag);
	        
			//HttpSessionBindingListener必须实例化后放入某一个HttpSession中，才可以进行监听。
			//把UserInfo放入在线列表
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("onlineUserBindingListener", new OnlineUserBindingListener(userinfo,httpSession,paramMap));
			
			
			String brandCss = binOLCM14_BL.getConfigValue("1135", organizationId, brandInfoId);
			if(brandCss != null && !"".equals(brandCss) && !"null".equals(brandCss)) {
				session.put(CherryConstants.SESSION_CHERRY_BRANDCSS, brandCss);
			} else {
				session.remove(CherryConstants.SESSION_CHERRY_BRANDCSS);
			}
			
			//如果选中了记住密码
			if("1".equals(chkRemember)){
				Cookie cookieName = new Cookie("rememberusername",txtname);
				Cookie cookiePwd = new Cookie("rememberpassword",loginbusinesslogic.getUserPwdDb(txtname));
				
				response.addCookie(cookieName);
				response.addCookie(cookiePwd);
			}else{
				Cookie cookieName = new Cookie("rememberusername","");
				Cookie cookiePwd = new Cookie("rememberpassword","");
				cookieName.setMaxAge(0);
				cookiePwd.setMaxAge(0);
				response.addCookie(cookieName);
				response.addCookie(cookiePwd);
			}
			Cookie cookieRemember = new Cookie("rememberflag",chkRemember);
			response.addCookie(cookieRemember);
			
			//推送上次登录时间，登录IP
			pushLoginInfo(userinfo,otherInfo);
			
	        //判断密码是否临近过期日，如果OverdueTactic=1 或2，进行提示修改密码消息推送。
            checkOverdueTactic(userinfo,otherInfo);
			
			return "OK";

		} catch (Exception ex) {
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				if ("ECM00015".equals(temp.getErrCode())
						|| "ECM00014".equals(temp.getErrCode())) {
					request.setAttribute("validateimage", "true");
				}
				this.addActionError(temp.getErrMessage());
			} else {
				throw ex;
			}
			return "false";
		}
	}

	/**
	 * 第三方登录当前系统(winPOS专用)
	 * @return
	 * @throws Exception
	 */
	public String witPosLogin() throws Exception {
		try {				
			
			String language = "";
			if (session.get("WW_TRANS_I18N_LOCALE") == null) {
				language = request.getLocale().toString();
			} else {
				language = String.valueOf(session.get("WW_TRANS_I18N_LOCALE"));
			}
			// 将语言放入session中
			session.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
						
			language="zh_CN";		
			// 取消数据库多语言对应，固定设zh_CN
			session.put(CherryConstants.SESSION_LANGUAGE, language);
			//CustomerContextHolder.setCustomerDataSourceType("LOGIN");
			
			String dbname = loginbusinesslogic.userLogin(txtname);
			
			//datasource
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,dbname);

			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(dbname);
			String userID = loginbusinesslogic.checkUserForWinPOS(txtname, token);

			//存放在用户其他的信息(如密码过期日期、开始提醒修改密码日、上次登录时间、IP)
			Map<String,Object> otherInfo = new HashMap<String,Object>();
			// 根据登录名初始化登录者的基本信息
			UserInfo userinfo = loginbusinesslogic.userInfoInitial(userID,
					txtname, language,otherInfo);
			userinfo.setLoginIP(request.getRemoteHost());
			// userinfo.setPassword(txtpsd);
			userinfo.setLoginTime(new Date());
		    userinfo.setUserAgent(request.getHeader("User-Agent"));
		    userinfo.setSessionID(request.getSession().getId());

			session.put(CherryConstants.SESSION_USERINFO, userinfo);
			
	        //更新登录信息（登录时间，登录IP）
            loginbusinesslogic.updateLoginInfo(userinfo);
			
			List<RoleInfo> roleList = userinfo.getRolelist();
			List<RoleInfo> canRoleList = new ArrayList<RoleInfo>();
			List<RoleInfo> forbidRoleList = new ArrayList<RoleInfo>();
			int[] roleArray = new int[roleList.size()];
			for (RoleInfo role : roleList) {
				int i = 0;
				if ("0".equals(role.getPrivilegeFlag())) {
					// 禁止权限的角色
					forbidRoleList.add(role);
				} else {
					// 允许权限的角色
					canRoleList.add(role);
				}
				roleArray[i] = role.getBIN_RoleID();
				i++;
			}
			int[] canRroleArray = new int[canRoleList.size()];
			int[] forbidRroleArray = new int[forbidRoleList.size()];

			for (int i = 0; i < canRoleList.size(); i++) {
				canRroleArray[i] = canRoleList.get(i).getBIN_RoleID();
			}
			for (int i = 0; i < forbidRoleList.size(); i++) {
				forbidRroleArray[i] = forbidRoleList.get(i).getBIN_RoleID();
			}

			// 取得用户可操作的菜单
			loginbusinesslogic.menuInitial(canRroleArray, forbidRroleArray,	request);

			request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, "LG");
			
			// 岗位为门店岗位的场合
			if(userinfo.getCategoryCode() != null && ("50".equals(userinfo.getCategoryCode()) 
					|| "DZ".equals(userinfo.getCategoryCode()))) {
				if(userinfo.getDepartCode() != null 
						&& !"".equals(userinfo.getDepartCode()) 
						&& !"null".equals(userinfo.getDepartCode())) {
					CounterInfo counterInfo = new CounterInfo();
					counterInfo.setOrganizationId(userinfo.getBIN_OrganizationID());
					counterInfo.setCounterCode(userinfo.getDepartCode());
					counterInfo.setCounterName(userinfo.getDepartName());
					session.put(CherryConstants.SESSION_CHERRY_COUNTERINFO, counterInfo);
				} else {
					this.addActionError(getText("ECM00012"));
					return "false";
				}
			}
			
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
	    	// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userinfo.getBIN_OrganizationInfoID());
			// 不是总部的场合
			if(userinfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE ) {
				map.put(CherryConstants.BRANDINFOID, userinfo.getBIN_BrandInfoID());
			}
	    	// 当前用户不是最大岗位级别的场合
			if(userinfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE ||binOLCM00_BL.isMaxPosCategoryGrade(map, userinfo.getCategoryGrade())) {
				session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "0");
			} else {
				session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "1");
			}
			map.put("userId", userinfo.getBIN_UserID());
			map.put("brandCode", userinfo.getBrandCode());
			map.put("orgCode", userinfo.getOrganizationInfoCode());
			// 保存数据权限到MongoDB
			gadgetPrivilegeBL.savePrivilegeToMongoDB(map);
			
			// 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("LG");
	    	
			map.put("userId", userinfo.getBIN_UserID());
			map.put("pageId", "0");
			map.put("doc", doc);
			// 取得小工具信息
			Map<String, Object> gadgetInfoMap = gadgetBL.getGadgetInfoList(map);
			List<Map<String, Object>> gadgetInfoPLList = (List)gadgetInfoMap.get("gadgetInfoList");
			if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
				for(Map<String, Object> gadgetInfo : gadgetInfoPLList) {
					String gadgetCode = (String)gadgetInfo.get("gadgetCode");
					if("BINOLLGTOP01".equals(gadgetCode) || "BINOLLGTOP07".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.sale"));
					} else if("BINOLLGTOP02".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.attendance"));
					} else if("BINOLLGTOP03".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.task"));
					} else if("BINOLLGTOP04".equals(gadgetCode) || "BINOLLGTOP08".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.saleTargetRpt"));
					} else if("BINOLLGTOP05".equals(gadgetCode) || "BINOLLGTOP06".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.saleCountByHours"));
					} else if("BINOLLGTOP09".equals(gadgetCode)) {
						gadgetInfo.put("gadgetName", getText("home.gadget.orderTask"));
					}
				}
			}
			gadgetInfoMap.put("userInfo", userinfo);
			gadgetInfoMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			gadgetInfo = JSONUtil.serialize(gadgetInfoMap);
			
	        //组织ID
	        String organizationId = String.valueOf(userinfo.getBIN_OrganizationInfoID());
	        //品牌ID
	        String brandInfoId = String.valueOf(userinfo.getBIN_BrandInfoID());
	        //配置项控制是否禁止同一账号同时登录
	        boolean sameTimeLoginFlag = binOLCM14_BL.isConfigOpen("1102", organizationId, brandInfoId);
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put("sameTimeLoginFlag", sameTimeLoginFlag);
	        
			//HttpSessionBindingListener必须实例化后放入某一个HttpSession中，才可以进行监听。
			//把UserInfo放入在线列表
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("onlineUserBindingListener", new OnlineUserBindingListener(userinfo,httpSession,paramMap));
			
			
			String brandCss = binOLCM14_BL.getConfigValue("1135", organizationId, brandInfoId);
			if(brandCss != null && !"".equals(brandCss) && !"null".equals(brandCss)) {
				session.put(CherryConstants.SESSION_CHERRY_BRANDCSS, brandCss);
			} else {
				session.remove(CherryConstants.SESSION_CHERRY_BRANDCSS);
			}
			
			//如果选中了记住密码
			if("1".equals(chkRemember)){
				Cookie cookieName = new Cookie("rememberusername",txtname);
				Cookie cookiePwd = new Cookie("rememberpassword",loginbusinesslogic.getUserPwdDb(txtname));
				
				response.addCookie(cookieName);
				response.addCookie(cookiePwd);
			}else{
				Cookie cookieName = new Cookie("rememberusername","");
				Cookie cookiePwd = new Cookie("rememberpassword","");
				cookieName.setMaxAge(0);
				cookiePwd.setMaxAge(0);
				response.addCookie(cookieName);
				response.addCookie(cookiePwd);
			}
			Cookie cookieRemember = new Cookie("rememberflag",chkRemember);
			response.addCookie(cookieRemember);
			
			//推送上次登录时间，登录IP
			pushLoginInfo(userinfo,otherInfo);
			
	        //判断密码是否临近过期日，如果OverdueTactic=1 或2，进行提示修改密码消息推送。
            checkOverdueTactic(userinfo,otherInfo);
			
			return "OK";

		} catch (Exception ex) {
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				if ("ECM00015".equals(temp.getErrCode())
						|| "ECM00014".equals(temp.getErrCode())) {
					request.setAttribute("validateimage", "true");
				}
				this.addActionError(temp.getErrMessage());
			} else {
				throw ex;
			}
			return "false";
		}
	}
	
	/**
	 * 第三方登录（微信）
	 * @return
	 * @throws Exception
	 */
	public String thirdpartyLogin() throws Exception {
		try {				
			
			String language = "";
			if (session.get("WW_TRANS_I18N_LOCALE") == null) {
				language = request.getLocale().toString();
			} else {
				language = String.valueOf(session.get("WW_TRANS_I18N_LOCALE"));
			}
			// 将语言放入session中
			session.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
						
			language="zh_CN";		
			// 取消数据库多语言对应，固定设zh_CN
			session.put(CherryConstants.SESSION_LANGUAGE, language);
			//CustomerContextHolder.setCustomerDataSourceType("LOGIN");
			
			String dbname = loginbusinesslogic.userLogin(txtname);
			
			//datasource
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,dbname);

			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(dbname);
			String userID = loginbusinesslogic.checkUserThirdParty(txtname, txtpsd);

			//存放在用户其他的信息(如密码过期日期、开始提醒修改密码日、上次登录时间、IP)
			Map<String,Object> otherInfo = new HashMap<String,Object>();
			// 根据登录名初始化登录者的基本信息
			UserInfo userinfo = loginbusinesslogic.userInfoInitial(userID,
					txtname, language,otherInfo);
			userinfo.setLoginIP(request.getRemoteHost());
			// userinfo.setPassword(txtpsd);
			userinfo.setLoginTime(new Date());
		    userinfo.setUserAgent(request.getHeader("User-Agent"));
		    userinfo.setSessionID(request.getSession().getId());

			session.put(CherryConstants.SESSION_USERINFO, userinfo);
			
	        //更新登录信息（登录时间，登录IP）
            loginbusinesslogic.updateLoginInfo(userinfo);
			
			List<RoleInfo> roleList = userinfo.getRolelist();
			List<RoleInfo> canRoleList = new ArrayList<RoleInfo>();
			List<RoleInfo> forbidRoleList = new ArrayList<RoleInfo>();
			int[] roleArray = new int[roleList.size()];
			for (RoleInfo role : roleList) {
				int i = 0;
				if ("0".equals(role.getPrivilegeFlag())) {
					// 禁止权限的角色
					forbidRoleList.add(role);
				} else {
					// 允许权限的角色
					canRoleList.add(role);
				}
				roleArray[i] = role.getBIN_RoleID();
				i++;
			}
			int[] canRroleArray = new int[canRoleList.size()];
			int[] forbidRroleArray = new int[forbidRoleList.size()];

			for (int i = 0; i < canRoleList.size(); i++) {
				canRroleArray[i] = canRoleList.get(i).getBIN_RoleID();
			}
			for (int i = 0; i < forbidRoleList.size(); i++) {
				forbidRroleArray[i] = forbidRoleList.get(i).getBIN_RoleID();
			}

			// 取得用户可操作的菜单
			loginbusinesslogic.menuInitial(canRroleArray, forbidRroleArray,	request);

	        //组织ID
	        String organizationId = String.valueOf(userinfo.getBIN_OrganizationInfoID());
	        //品牌ID
	        String brandInfoId = String.valueOf(userinfo.getBIN_BrandInfoID());
	        //配置项控制是否禁止同一账号同时登录
	        boolean sameTimeLoginFlag = binOLCM14_BL.isConfigOpen("1102", organizationId, brandInfoId);
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put("sameTimeLoginFlag", sameTimeLoginFlag);
	        
			//HttpSessionBindingListener必须实例化后放入某一个HttpSession中，才可以进行监听。
			//把UserInfo放入在线列表
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("onlineUserBindingListener", new OnlineUserBindingListener(userinfo,httpSession,paramMap));
			String csrftoken = UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString();
			csrftoken =csrftoken.replace("-", "");
			redirectURL = redirectURL+"?code="+csrftoken+"&brandCode="+userinfo.getBrandCode() + "&userId="+userinfo.getBIN_UserID()+"&csrftoken="+csrftoken
					+ "&employeeName="+ URLEncoder.encode(userinfo.getEmployeeName(), "utf-8") + "&categoryCode="+userinfo.getCategoryCode()
					+"&employeeCode="+userinfo.getEmployeeCode()+"&txtname="+txtname+"&wcaid="+wcaid;
			request.getSession().setAttribute("hiscsrftoken",csrftoken+",");
			request.getSession().setAttribute("csrftoken",csrftoken);
			request.getSession().setAttribute("code",csrftoken);
//			request.getSession().setAttribute("code",csrftoken);
//			code = UUID.randomUUID().toString().replace("-", "");
//			brandCode = userinfo.getBrandCode();
//			userId = String.valueOf(userinfo.getBIN_UserID());
//			employeeName = userinfo.getEmployeeName();
			
			return "OK";

		} catch (Exception ex) {
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				redirectURL = redirectURL+"?errorCode="+temp.getErrCode();
			} else {
				redirectURL = redirectURL+"?errorCode=ECM00036";
			}
			return "OK";
		}
	}

	/**
	 * 第三方借道跳转至兑吧时使用的方法
	 * 针对会员，在此方法中验证会员信息，返回redirect的URL
	 * @return
	 * @throws Exception
	 */
	public String memberRedirectDuiba() throws Exception {
		try {
			String dbname = loginbusinesslogic.userLogin(txtname);

			Map retMap = new HashMap<String,String>();
			String retString="";
			//检查品牌代码，设定数据源
			if(!webserviceDataSource.setBrandDataSource(brandCode)){
				retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
				//retString = CherryUtil.map2Json(retMap);
				//logger.error(retString);
				return retString;
			}
			//解密参数
			String aeskey = webserviceDataSource.getAESKey(brandCode);
			Map<String,Object> paramMap = null;
			try{
				paramMap = CherryUtil.json2Map(CherryAESCoder.decrypt(paramJson, aeskey));
			}catch (Exception e){
				retMap.put("ERRORMSG", "参数paramJson错误。paramJson=" + paramJson);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(CherryUtil.map2Json(retMap));
				logger.error(retString);
			}

			Map membermap = loginbusinesslogic.getMemberInfoByOpenID(paramMap);
			if(null==membermap || CherryChecker.isNullOrEmpty(membermap.get("MemCode"))){
				retMap.put("ERRORMSG", "参数paramJson错误,未找到对应的会员信息。paramJson=" + paramJson);
				retString = CherryUtil.map2Json(retMap);
				this.addActionError(CherryUtil.map2Json(retMap));
				logger.error(retString);
			}
			//TODO:
//
//			redirectURL = redirectURL+"?code="+csrftoken+"&brandCode="+userinfo.getBrandCode() + "&userId="+userinfo.getBIN_UserID()+"&csrftoken="+csrftoken
//					+ "&employeeName="+ URLEncoder.encode(userinfo.getEmployeeName(), "utf-8") + "&categoryCode="+userinfo.getCategoryCode()
//					+"&employeeCode="+userinfo.getEmployeeCode()+"&txtname="+txtname+"&wcaid="+wcaid;
//			request.getSession().setAttribute("hiscsrftoken",csrftoken+",");
//			request.getSession().setAttribute("csrftoken",csrftoken);
//			request.getSession().setAttribute("code",csrftoken);

			return "OK";

		} catch (Exception ex) {
			if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				redirectURL = redirectURL+"?errorCode="+temp.getErrCode();
			} else {
				redirectURL = redirectURL+"?errorCode=ECM00036";
			}
			return "OK";
		}
	}
	// 呼叫中心登陆
	public String callCenterLogin() throws Exception {
		try {
			if (session.get(CherryConstants.SESSION_USERINFO) == null){
				String language = "";
				String userID = "";
				if(null==txtname || "".equals(txtname)){
					if(null!=crmId && !"".equals(crmId)){
						txtname = crmId;
					}else{
						if(null!=cno && !"".equals(cno)){
							txtname = cno;
						}
					}
				}
				if (session.get("WW_TRANS_I18N_LOCALE") == null) {
					language = request.getLocale().toString();
				} else {
					language = String.valueOf(session.get("WW_TRANS_I18N_LOCALE"));
				}
				// 将语言放入session中
				session.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
							
				language="zh_CN";		
				// 取消数据库多语言对应，固定设zh_CN
				session.put(CherryConstants.SESSION_LANGUAGE, language);
				
				String dbname = loginbusinesslogic.userLogin(txtname);
				
				//datasource
				session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,dbname);
				CustomerContextHolder.setCustomerDataSourceType(dbname);
				
				// 判断txtpsd密码参数是否为空，若为空则使用pwd密码参数
				if(null==txtpsd || "".equals(txtpsd)){
					if(null!=pwd && !"".equals(pwd)){
						//检查账号是否存在
						List list = loginservice.checkAccount(txtname);
						if(list==null||list.size()==0){
							//账号不存在
							throw new CherryException("ECM00013");
						}
						//账号存在,取得账号标识ID
						String uId = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));
						//确定账号存在后，取得安全配置信息
						list = loginservice.getUserSecurityInfo(uId);	
						HashMap map = (HashMap)list.get(0);
						//获取新后台密码后解密
						String password = decryptPwd(String.valueOf(map.get("PassWord")));
						//失败次数
						int failurecount = CherryUtil.string2int(String.valueOf(map.get("FailureCount")));
						//将解密后的密码使用MD5加密
						String md5psd = CherryMD5Coder.encryptMD5(password);
						if(pwd.equals(md5psd)){
							userID = uId;
							//密码正确
							if(failurecount!=0){
								//如果失败的次数不为0，则将其清0
								Map<String, Object> inmap = new HashMap<String, Object>();				
								inmap.put("BIN_UserID", userID);
								// 更新者
								inmap.put("updatedBy", userID);
								// 更新时间
								inmap.put("updateTime", new Date());
								// 更新模块
								inmap.put("updatePGM", "Login");
								loginservice.unLockUser(inmap);		
							}
						}else{
							throw new CherryException("ECM00013");
						}
					}else{
						userID = loginbusinesslogic.checkUserThirdParty(txtname, txtpsd);
					}
				}else{
					userID = loginbusinesslogic.checkUserThirdParty(txtname, txtpsd);
				}
	
				//存放在用户其他的信息(如密码过期日期、开始提醒修改密码日、上次登录时间、IP)
				Map<String,Object> otherInfo = new HashMap<String,Object>();
				// 根据登录名初始化登录者的基本信息
				UserInfo userinfo = loginbusinesslogic.userInfoInitial(userID,
						txtname, language,otherInfo);
				userinfo.setLoginIP(request.getRemoteHost());
				// userinfo.setPassword(txtpsd);
				userinfo.setLoginTime(new Date());
			    userinfo.setUserAgent(request.getHeader("User-Agent"));
			    userinfo.setSessionID(request.getSession().getId());
	
				session.put(CherryConstants.SESSION_USERINFO, userinfo);
				
		        //更新登录信息（登录时间，登录IP）
	            loginbusinesslogic.updateLoginInfo(userinfo);
				
				List<RoleInfo> roleList = userinfo.getRolelist();
				List<RoleInfo> canRoleList = new ArrayList<RoleInfo>();
				List<RoleInfo> forbidRoleList = new ArrayList<RoleInfo>();
				int[] roleArray = new int[roleList.size()];
				for (RoleInfo role : roleList) {
					int i = 0;
					if ("0".equals(role.getPrivilegeFlag())) {
						// 禁止权限的角色
						forbidRoleList.add(role);
					} else {
						// 允许权限的角色
						canRoleList.add(role);
					}
					roleArray[i] = role.getBIN_RoleID();
					i++;
				}
				int[] canRroleArray = new int[canRoleList.size()];
				int[] forbidRroleArray = new int[forbidRoleList.size()];
	
				for (int i = 0; i < canRoleList.size(); i++) {
					canRroleArray[i] = canRoleList.get(i).getBIN_RoleID();
				}
				for (int i = 0; i < forbidRoleList.size(); i++) {
					forbidRroleArray[i] = forbidRoleList.get(i).getBIN_RoleID();
				}
	
				// 取得用户可操作的菜单
				loginbusinesslogic.menuInitial(canRroleArray, forbidRroleArray,	request);
	
		        //组织ID
		        String organizationId = String.valueOf(userinfo.getBIN_OrganizationInfoID());
		        //品牌ID
		        String brandInfoId = String.valueOf(userinfo.getBIN_BrandInfoID());
		        //配置项控制是否禁止同一账号同时登录
		        boolean sameTimeLoginFlag = binOLCM14_BL.isConfigOpen("1102", organizationId, brandInfoId);
		        Map<String,Object> paramMap = new HashMap<String,Object>();
		        paramMap.put("sameTimeLoginFlag", sameTimeLoginFlag);
		        
				//HttpSessionBindingListener必须实例化后放入某一个HttpSession中，才可以进行监听。
				//把UserInfo放入在线列表
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("onlineUserBindingListener", new OnlineUserBindingListener(userinfo,httpSession,paramMap));
				String csrftoken = UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString();
				csrftoken=csrftoken.replace("-", "");
				request.getSession().setAttribute("hiscsrftoken",csrftoken+",");
				request.getSession().setAttribute("code",csrftoken);
			}
			if(null==session.get("code")){
				String csrftoken = String.valueOf(session.get("csrftoken"));
				request.getSession().setAttribute("code",csrftoken);
			}
			code=String.valueOf(session.get("code"));
			if(null!=callId && !"".equals(callId)){
				customerNumber=customerNumber.replace("-", "");
				return "CCT";
			}else{
				return "RPT";
			}
		} catch (Exception ex) {
			return "ERROR";
		}
	}
	
    /*
     * 初始化登录页面
     */
    public String initialLogin() throws Exception{
//    	if(logoutFlag == null || !"logoutFlag".equals(logoutFlag)) {
//    		if(session.get(CherryConstants.SESSION_USERINFO) != null) {
//        		csrftoken = (String)session.get("csrftoken");
//        		return "OK";
//        	}
//    	}
    	String language = "";
    	if (session.get("WW_TRANS_I18N_LOCALE") == null) {
			language = request.getLocale().toString();
		} else {
			language = String.valueOf(session.get("WW_TRANS_I18N_LOCALE"));
		}
		// 将语言放入session中
		session.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		String strBrowser = judgeBrowser();
		if(strBrowser.equals("IE8")||strBrowser.equals("IE9")||strBrowser.equals("IE10")||strBrowser.equals("IE11")||strBrowser.equals("Firefox")
				||strBrowser.equals("Chrome")||strBrowser.equals("Safari")){
			
		}else{
			this.addActionError(getText("ECM00059"));
		}

		Cookie[] cookies = request.getCookies();
		String rememberflag="";
		String rememberusername="";
		String rememberpassword="";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if("rememberflag".equals(cookies[i].getName())){
					rememberflag = cookies[i].getValue();
				}else if("rememberusername".equals(cookies[i].getName())){
					rememberusername = cookies[i].getValue();
				}else if("rememberpassword".equals(cookies[i].getName())){
					rememberpassword = cookies[i].getValue();
				}
			}
		}
 
		if("1".equals(rememberflag)){
			//如果要求记住密码
			request.setAttribute("rememberusername", rememberusername);
			request.setAttribute("rememberflag", rememberflag);
			if(null!=rememberpassword && !"".equals(rememberpassword)){
				request.setAttribute("rememberpassword", decryptPsd(rememberpassword));
			}
		}
		// 获取是否开启忘记密码功能的配置
		String retrievePwdFlag = PropertiesUtil.pps.getProperty("retrievePasswordFlag");
		request.setAttribute("retrievePwdFlag", retrievePwdFlag);
		
		return SUCCESS;
    }
    
    /**
     * 判断浏览器版本
     */
    private String judgeBrowser(){
    	//判断浏览器版本
		String userAgent = request.getHeader("User-Agent").toLowerCase();
		String browser = "";
		if(userAgent.indexOf("firefox")>-1){
			browser="Firefox";
		}else if(userAgent.indexOf("msie")>-1){	
			if(userAgent.indexOf("msie 6.0")>-1){
				//IE 6.0
				browser="IE6";
			}else if(userAgent.indexOf("msie 7.0")>-1){
				//IE 7.0
				browser="IE7";
			}else if(userAgent.indexOf("msie 8.0")>-1){
				//IE 8.0
				browser="IE8";
			}else if(userAgent.indexOf("msie 9.0")>-1){
				//IE 9.0
				browser="IE9";
			}else if(userAgent.indexOf("msie 10.0")>-1){
				//IE 10.0	
				browser="IE10";
			}
		}else if(userAgent.indexOf("rv:11.0")>-1 && userAgent.indexOf("trident")>-1){
            //IE 11.0   
            browser="IE11";
		}else if(userAgent.indexOf("chrome")>-1){
			browser="Chrome";			
		}else if(userAgent.indexOf("safari")>-1){
			browser="Safari";
		}else{
			browser="Unknown";
			
		}	
		this.setBrowser("browser");
		return browser;
	}

	/**
	 * 退出时，销毁HttpSession。
	 */
	public String doLogout() {
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			if(null==session.get("code") || "".equals(String.valueOf(session.get("code")))){
				httpSession.invalidate();
			}
		}
		return SUCCESS;
	}

	private String encryptPsd(String psd) throws Exception {
		// 加密
		if (psd != null && !"".equals(psd)) {
			// 加密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			psd = des.encrypt(psd);
		}
		return psd;
	}
	private String decryptPsd(String psd) throws Exception {
		// 加密
		String ret ="";
		if (psd != null && !"".equals(psd)) {
			// 加密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			ret = des.decrypt(psd);
		}
		return ret;
	}
	
	private void pushLoginInfo(UserInfo userInfo,Map<String,Object> otherInfo){
        String lastLogin = ConvertUtil.getString(otherInfo.get("LastLogin"));
        String loginIP = ConvertUtil.getString(otherInfo.get("LoginIP"));
        if(lastLogin.equals("") && loginIP.equals("")){
            return;
        }
	    Map<String, Object> map = new HashMap<String, Object>();
        map.put("TradeType", "loginInfo");
        map.put("LoginName",userInfo.getLoginName());
        map.put("LastLogin", lastLogin);
        map.put("LoginIP", loginIP);
        map.put("SessionID", userInfo.getSessionID());
        map.put("OrgCode", userInfo.getOrgCode());
        map.put("BrandCode", userInfo.getBrandCode());
        try {
            JQueryPubSubPush.push(map, "pushMsg", 3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 密码解密
	 * @param psd
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	private String decryptPwd(String psd) throws Exception{
			// 解密
			if(psd != null && !"".equals(psd)) {
				// 解密处理
				DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
				psd =  des.decrypt(psd);
			}			
			return psd;

	}
	
	/**
	 * 判断密码是否临近过期日，如果OverdueTactic=1 或2，进行提示修改密码消息推送。
	 * @return
	 */
	private void checkOverdueTactic(UserInfo userInfo,Map<String,Object> otherInfo){
	    String informDate = ConvertUtil.getString(otherInfo.get("InformDate"));
	    String expireDate = ConvertUtil.getString(otherInfo.get("ExpireDate"));
	    String today = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
	    //密码永不提醒过期
	    if(informDate.equals("")){
	        return;
	    }
	    //今天与开始提醒日比较是否需要提醒
	    int informDays = DateUtil.compareDate(today, informDate);
	    if(informDays >= 0){
	        Map<String,Object> paramMap = new HashMap<String,Object>();
	        paramMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	        paramMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
	        Map<String,Object> pwConfInfo = binOLPLUPM04_BL.getPwConfInfo(paramMap);
	        String overdueTactic = ConvertUtil.getString(pwConfInfo.get("overdueTactic"));
	        //无操作
	        if(overdueTactic.equals("0") || overdueTactic.equals("")){
	            return;
	        }
	        
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("TradeType", "overdueTactic");
            map.put("LoginName",userInfo.getLoginName());
            map.put("OverdueTactic", overdueTactic);
            map.put("ExpireDate", expireDate);
            map.put("SessionID", userInfo.getSessionID());
            map.put("OrgCode", userInfo.getOrgCode());
            map.put("BrandCode", userInfo.getBrandCode());
            //今天与密码过期日比较是否已过期
            int expireDays = DateUtil.compareDate(today, expireDate);
            if(expireDays >= 0){
                map.put("Message", "header_pwdExpire2");
            }else{
                map.put("Message", "header_pwdExpire1");
            }
            try {
                JQueryPubSubPush.push(map, "pushMsg", 3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
	    }
	}
	
    /**
     * 用戶名
     */
    private String txtname;
    
    /**
     * 密碼
     */
    private String txtpsd;
    
    private String gadgetInfo;
    
    private String browser;
    
    private String browserWarning;
    
    private String chkRemember;
    
    private String code;
    private String employeeName;
    private String  brandCode;
    private String  userId;
    
    private String cno;
    
    private String crmId;
    
    private String pwd;
    
    private String customerNumber;
    
    private String customerNumberType;
    
    private String customerAreaCode;
    
    private String calltype;
    
    private String callId;
    
    private String taskId;
    
    private String csrftoken;
    
    private String logoutFlag;
    
    private String wcaid;
	private String paramJson;
	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private String token;
    
    public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getEmployeeName() {
		return employeeName;
	}


	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public String getBrandCode() {
		return brandCode;
	}


	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getChkRemember() {
		return chkRemember;
	}

	public void setChkRemember(String chkRemember) {
		this.chkRemember = chkRemember;
	}

	public String getGadgetInfo() {
		return gadgetInfo;
	}

	public void setGadgetInfo(String gadgetInfo) {
		this.gadgetInfo = gadgetInfo;
	}

	/**
	 * @return the txtpsd
	 */
	public String getTxtpsd() {
		return txtpsd;
	}
	/**
	 * @param txtpsd the txtpsd to set
	 */
	public void setTxtpsd(String txtpsd) {
		this.txtpsd = txtpsd;
	}
	/**
     * @return the txtname
     */
    public String getTxtname() {
        return txtname;
    }
    /**
     * @param txtname the txtname to set
     */
    public void setTxtname(String txtname) {
        this.txtname = txtname;
    }


	/**
	 * @return the codeText
	 */
	public String getCodeText() {
		return codeText;
	}

	/**
	 * @param codeText the codeText to set
	 */
	public void setCodeText(String codeText) {
		this.codeText = codeText;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * @return the browserWarning
	 */
	public String getBrowserWarning() {
		return browserWarning;
	}

	/**
	 * @param browserWarning the browserWarning to set
	 */
	public void setBrowserWarning(String browserWarning) {
		this.browserWarning = browserWarning;
	}


	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String crmId) {
		this.crmId = crmId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerNumberType() {
		return customerNumberType;
	}

	public void setCustomerNumberType(String customerNumberType) {
		this.customerNumberType = customerNumberType;
	}

	public String getCustomerAreaCode() {
		return customerAreaCode;
	}

	public void setCustomerAreaCode(String customerAreaCode) {
		this.customerAreaCode = customerAreaCode;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getCsrftoken() {
		return csrftoken;
	}


	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}


	public String getLogoutFlag() {
		return logoutFlag;
	}


	public void setLogoutFlag(String logoutFlag) {
		this.logoutFlag = logoutFlag;
	}


	public String getWcaid() {
		return wcaid;
	}


	public void setWcaid(String wcaid) {
		this.wcaid = wcaid;
	}
	
}
