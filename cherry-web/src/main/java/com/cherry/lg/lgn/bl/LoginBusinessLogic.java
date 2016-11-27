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
package com.cherry.lg.lgn.bl;

import com.cherry.cm.cmbeans.ControlOrganization;
import com.cherry.cm.cmbeans.RoleInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.lg.lgn.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.*;
@SuppressWarnings("unchecked")
public class LoginBusinessLogic {
	private static Logger logger = LoggerFactory.getLogger(LoginBusinessLogic.class.getName());
	@Resource
	private LoginService loginservice;	
	
		public String userLogin(String name)throws Exception{
			List list = loginservice.getDBByName(name);
			if(null==list||list.size()<1){
				//账号不存在
				throw new CherryException("ECM00013");
			}
			Map map = (Map)list.get(0);			
			return String.valueOf(map.get("DataBaseName"));
		}
	public String getUserPwdDb(String name){
		List list = loginservice.checkAccount(name);
		//账号存在,取得账号标识ID
		String userID = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));
		
		//确定账号存在后，取得安全配置信息
		list = loginservice.getUserSecurityInfo(userID);		
		HashMap map = (HashMap)list.get(0);
		
		//密码
		String password = String.valueOf(map.get("PassWord"));
		return password;
	}
	public String  checkUser(String name,String psd, String csrftoken)throws Exception{
		//为了实现出错X次后锁定账号等相关的系列的功能，不能简单的在一个SQL文里完成账号验证，
		//分成了多步来进行操作，为了尽可能减少SQL执行次数，密码的验证也交由Java代码来实现		
		
		//检查账号是否存在
		List list = loginservice.checkAccount(name);
		if(list==null||list.size()==0){
			//账号不存在
			logger.error("异常登录，错误的账号密码:"+name);
			throw new CherryException("ECM00013");
		}
		//账号存在,取得账号标识ID
		String userID = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));
		
		//确定账号存在后，取得安全配置信息
		list = loginservice.getUserSecurityInfo(userID);		
		HashMap map = (HashMap)list.get(0);
		
		//密码
		String password = String.valueOf(map.get("PassWord"));
		//失败次数
		int failurecount = CherryUtil.string2int(String.valueOf(map.get("FailureCount")));
		//可重试的次数
		int retrytimes = CherryUtil.string2int(String.valueOf(map.get("RetryTimes")));
		//锁定的时间长度
		//int lockperiod = CherryUtil.string2int(String.valueOf(map.get("LockPeriod")));
		//密码是否加密
		//String isencryption = String.valueOf(map.get("IsEncryption"));
		//已经被锁定的秒数
		//int lockedsecond = CherryUtil.string2int(String.valueOf(map.get("LockedSecond")));
	
//================================================================Start		
		//检查是否处于锁定期,该功能暂时被注释掉，改为输入验证码		
		//已经被锁定的秒数如果是0秒，则表示该账号未被锁定过
		//已经被锁定的秒数如果不为0，则表示该账号曾今被锁定过，需要判断是否过了锁定时间
//		if(lockedsecond!=0){			
//			if(lockperiod!=0){
//				//密码安全配置中的锁定期，如果为0，表示不锁定账号
//				if(lockedsecond<lockperiod*3600){
//					//如果被锁定的秒数少于限定的秒数，则提示被锁定中
//					throw new CherryException("ECM00015");
//				}else{
//					//如果被锁定的秒数>=限定的秒数，则解锁该用户
//					failurecount = 0;
//					Map<String, Object> inmap = new HashMap<String, Object>();				
//					inmap.put("BIN_UserID", userID);
//					// 更新者
//					inmap.put("updatedBy", userID);
//					// 更新时间
//					inmap.put("updateTime", new Date());
//					// 更新模块
//					inmap.put("updatePGM", "Login");				
//					loginservice.unLockUser(inmap);	
//				}
//			}			
//		}
//		
//		//根据密码是否加密对用户输入的密码进行处理
//		psd = changePsd( psd,isencryption);		
//		
//		if(password.equals(psd)){
//			//密码正确
//			if(failurecount!=0){
//				//如果失败的次数不为0，则将其清0
//				Map<String, Object> inmap = new HashMap<String, Object>();				
//				inmap.put("BIN_UserID", userID);
//				// 更新者
//				inmap.put("updatedBy", userID);
//				// 更新时间
//				inmap.put("updateTime", new Date());
//				// 更新模块
//				inmap.put("updatePGM", "Login");
//				
//				loginservice.unLockUser(inmap);		
//			}
//		}else{
//			//密码不正确次数超过上限，并且安全配置中设定了锁定时间		
//			if(failurecount+1>=retrytimes&&lockperiod!=0){
//				//若果失败的次数>=可重试次数,则锁定该账号
//				Map<String, Object> inmap = new HashMap<String, Object>();				
//				inmap.put("BIN_UserID", userID);
//				// 更新者
//				inmap.put("updatedBy", userID);
//				// 更新时间
//				inmap.put("updateTime", new Date());
//				// 更新模块
//				inmap.put("updatePGM", "Login");				
//				loginservice.lockUser(inmap);				
//				throw new CherryException("ECM00015");
//			}else if(failurecount<retrytimes&&lockperiod!=0){
//				//更新失败次数
//				Map<String, Object> inmap = new HashMap<String, Object>();	
//				inmap.put("FailureCount", failurecount+1);
//				inmap.put("BIN_UserID", userID);
//				// 更新者
//				inmap.put("updatedBy", userID);
//				// 更新时间
//				inmap.put("updateTime", new Date());
//				// 更新模块
//				inmap.put("updatePGM", "Login");				
//				loginservice.updateFailureCount(inmap);					
//				throw new CherryException("ECM00014",new String[]{String.valueOf(retrytimes),String.valueOf(failurecount+1)});
//			}
//			throw new CherryException("ECM00013");
//		}
		//================================================================End		
		//将密码进行加密比较
		//psd = changePsd(psd);	
		String csrftoken_Decrption = null;
		try {
			csrftoken_Decrption = JsclPBKDF2WithHMACSHA256.DecrptPBKDF2WithHMACSHA256(decryptPwd(password), psd);
		} catch (Exception e) {
			
		}
		
		if(csrftoken_Decrption != null && csrftoken_Decrption.equals(csrftoken)) {
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
			//密码不正确次数超过上限，并且安全配置中设定了锁定时间		
			if(failurecount+1>=retrytimes){
				//若果失败的次数>=可重试次数,则要求输入验证码
				Map<String, Object> inmap = new HashMap<String, Object>();				
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");				
				loginservice.lockUser(inmap);	
				//抛出异常，要求输入验证码
				logger.error("异常登录，错误的账号密码:"+name);
				throw new CherryException("ECM00015");
			}else if(failurecount<retrytimes){
				//更新失败次数
				Map<String, Object> inmap = new HashMap<String, Object>();	
				inmap.put("FailureCount", failurecount+1);
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");				
				loginservice.updateFailureCount(inmap);
			}
			logger.error("异常登录，错误的账号密码:"+name);
			throw new CherryException("ECM00013");
		}
		
		return userID;
	}
	
	public String  checkUserThirdParty(String name,String psd)throws Exception{
		//为了实现出错X次后锁定账号等相关的系列的功能，不能简单的在一个SQL文里完成账号验证，
		//分成了多步来进行操作，为了尽可能减少SQL执行次数，密码的验证也交由Java代码来实现		
		
		//检查账号是否存在
		List list = loginservice.checkAccount(name);
		if(list==null||list.size()==0){
			//账号不存在
			throw new CherryException("ECM00013");
		}
		//账号存在,取得账号标识ID
		String userID = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));
		
		//确定账号存在后，取得安全配置信息
		list = loginservice.getUserSecurityInfo(userID);		
		HashMap map = (HashMap)list.get(0);
		
		//密码
		String password = String.valueOf(map.get("PassWord"));
		//失败次数
		int failurecount = CherryUtil.string2int(String.valueOf(map.get("FailureCount")));
		//可重试的次数
		int retrytimes = CherryUtil.string2int(String.valueOf(map.get("RetryTimes")));
		
		if(psd.equals(decryptPwd(password))) {
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
			//密码不正确次数超过上限，并且安全配置中设定了锁定时间		
			if(failurecount+1>=retrytimes){
				//若果失败的次数>=可重试次数,则要求输入验证码
				Map<String, Object> inmap = new HashMap<String, Object>();				
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");				
				loginservice.lockUser(inmap);	
				//抛出异常，要求输入验证码
				logger.error("异常登录，错误的账号密码:"+name);
				throw new CherryException("ECM00015");
			}else if(failurecount<retrytimes){
				//更新失败次数
				Map<String, Object> inmap = new HashMap<String, Object>();	
				inmap.put("FailureCount", failurecount+1);
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");				
				loginservice.updateFailureCount(inmap);
			}
			logger.error("异常登录，错误的账号密码:"+name);
			throw new CherryException("ECM00013");
		}
		
		return userID;
	}


	/**
	 * 专供winPOS弹出Cherry的画面使用
	 * token = MD5（用户名+密码）
	 * @param name
	 * @param token
	 * @return
	 * @throws Exception
     */
	public String  checkUserForWinPOS(String name,String token)throws Exception{
		//为了实现出错X次后锁定账号等相关的系列的功能，不能简单的在一个SQL文里完成账号验证，
		//分成了多步来进行操作，为了尽可能减少SQL执行次数，密码的验证也交由Java代码来实现

		//检查账号是否存在
		List list = loginservice.checkAccount(name);
		if(list==null||list.size()==0){
			//账号不存在
			throw new CherryException("ECM00013");
		}
		//账号存在,取得账号标识ID
		String userID = String.valueOf(((HashMap)list.get(0)).get("BIN_UserID"));

		//确定账号存在后，取得安全配置信息
		list = loginservice.getUserSecurityInfo(userID);
		HashMap map = (HashMap)list.get(0);

		//密码
		String password = decryptPwd(String.valueOf(map.get("PassWord")));

		//将账号和密码拼接再MD5
		String ap = CherryMD5Coder.encryptMD5(name+ password);

		//失败次数
		int failurecount = CherryUtil.string2int(String.valueOf(map.get("FailureCount")));
		//可重试的次数
		int retrytimes = CherryUtil.string2int(String.valueOf(map.get("RetryTimes")));

		if(token.equalsIgnoreCase(ap)) {
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
			//密码不正确次数超过上限，并且安全配置中设定了锁定时间
			if(failurecount+1>=retrytimes){
				//若果失败的次数>=可重试次数,则要求输入验证码
				Map<String, Object> inmap = new HashMap<String, Object>();
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");
				loginservice.lockUser(inmap);
				//抛出异常，要求输入验证码
				logger.error("异常登录，错误的账号密码:"+name);
				throw new CherryException("ECM00015");
			}else if(failurecount<retrytimes){
				//更新失败次数
				Map<String, Object> inmap = new HashMap<String, Object>();
				inmap.put("FailureCount", failurecount+1);
				inmap.put("BIN_UserID", userID);
				// 更新者
				inmap.put("updatedBy", userID);
				// 更新时间
				inmap.put("updateTime", new Date());
				// 更新模块
				inmap.put("updatePGM", "Login");
				loginservice.updateFailureCount(inmap);
			}
			logger.error("异常登录，错误的账号密码:"+name);
			throw new CherryException("ECM00013");
		}

		return userID;
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

	/**
	 * 取得用户的基本信息
	 * @param loginName
	 * @return
	 * @throws Exception
	 */	
	public UserInfo userInfoInitial(String userID,String loginName,String language,Map<String,Object> otherInfo) throws Exception {
		UserInfo userinfo = new UserInfo();
		//登录账号
		userinfo.setLoginName(loginName);
		//用户ID
		userinfo.setBIN_UserID(CherryUtil.string2int(userID));
		//登录语言
		userinfo.setLanguage(language);
		
		//基本信息取得
		List list = loginservice.getUserInfo(userID,language);
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			userinfo.setBIN_BrandInfoID(CherryUtil.string2int(String.valueOf(map.get("BIN_BrandInfoID"))));
			userinfo.setBIN_EmployeeID(CherryUtil.string2int(String.valueOf(map.get("BIN_EmployeeID"))));
			userinfo.setBIN_OrganizationInfoID(CherryUtil.string2int(String.valueOf(map.get("BIN_OrganizationInfoID"))));
			userinfo.setBIN_OrganizationID(CherryUtil.string2int(String.valueOf(map.get("BIN_OrganizationID"))));
			userinfo.setBIN_PositionCategoryID(CherryUtil.string2int(String.valueOf(map.get("BIN_PositionCategoryID"))));
			userinfo.setOrganizationInfoCode(String.valueOf(map.get("OrgCode")));
			userinfo.setDepartCode(String.valueOf(map.get("DepartCode")));
			userinfo.setDepartName(String.valueOf(map.get("DepartName")));
			userinfo.setDepartType(String.valueOf(map.get("DepartType")));
			userinfo.setCategoryCode(String.valueOf(map.get("CategoryCode")));
			userinfo.setCategoryName(String.valueOf(map.get("CategoryName")));
			userinfo.setCategoryGrade(String.valueOf(map.get("CategoryGrade")));
			userinfo.setBrandName(String.valueOf(map.get("BrandName")));
			userinfo.setEmployeeCode(String.valueOf(map.get("EmployeeCode")));
			userinfo.setEmployeeName(String.valueOf(map.get("EmployeeName")));
			userinfo.setOrgName(String.valueOf(map.get("OrgName")));
			userinfo.setOrgCode(String.valueOf(map.get("OrgCode")));
			userinfo.setBrandCode(String.valueOf(map.get("BrandCode")));
			
			otherInfo.put("InformDate", ConvertUtil.getString(map.get("InformDate")));
			otherInfo.put("ExpireDate", ConvertUtil.getString(map.get("ExpireDate")));
			otherInfo.put("LastLogin", ConvertUtil.getString(map.get("LastLogin")));
			otherInfo.put("LoginIP", ConvertUtil.getString(map.get("LoginIP")));
		}
		//取得所拥有的岗位（可能会是多个品牌下的多个岗位）
		userinfo.setControlOrganizationList(this.getControlOrganizationList(userinfo.getBIN_EmployeeID(),language));
		
		//取得所拥有的角色
		userinfo.setRolelist(this.getRoleList(userinfo.getBIN_UserID(),userinfo.getBIN_OrganizationID(),userinfo.getBIN_PositionCategoryID()));
		return userinfo;
	}
	
	/**
	 * 根据雇员ID取得他管辖的部门信息
	 * @param employeeID
	 * @return
	 * @throws Exception
	 */
	private List<ControlOrganization> getControlOrganizationList(int employeeID,String lanuage) throws Exception {
		
		List<ControlOrganization> ret = new ArrayList<ControlOrganization>();
		
		List list = loginservice.getControlOrganizationList(employeeID,lanuage);
		Iterator it = list.iterator();
		ControlOrganization temp;
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			temp = new ControlOrganization();
			temp.setBIN_OrganizationID(CherryUtil.string2int(String.valueOf(map.get("BIN_OrganizationID"))));
			temp.setDepartCode(String.valueOf(map.get("DepartCode")));
			temp.setDepartName(String.valueOf(map.get("DepartName")));
			temp.setDepartType(String.valueOf(map.get("DepartType")));
			temp.setManageType(String.valueOf(map.get("ManageType")));
	        ret.add(temp);
		}		
		return ret;
	}
	/**
	 * 取得用户的角色信息
	 * 1用户角色      2部门角色    3岗位类别角色
	 * @param userID
	 * @param organizationID
	 * @param positionCategoryID
	 * @return
	 * @throws Exception
	 */
	private List<RoleInfo> getRoleList(int userID,int organizationID,int positionCategoryID) throws Exception {
		List<RoleInfo> ret = new ArrayList<RoleInfo>();
		
		List list = loginservice.getRoleList(userID,organizationID,positionCategoryID);
		Iterator it = list.iterator();
		RoleInfo temp;
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			temp = new RoleInfo();
			temp.setBIN_RoleID(Integer.valueOf(String.valueOf(map.get("BIN_RoleID"))));
			temp.setRoleKind(Integer.valueOf(String.valueOf(map.get("RoleKind"))));
			temp.setRoleName(String.valueOf(map.get("RoleName")));
			temp.setPrivilegeFlag(String.valueOf(map.get("PrivilegeFlag")));
	        ret.add(temp);
		}		
		return ret;
	}
	public void menuInitial(int[] canRroleArray,int[] forbidRroleArray,HttpServletRequest request) throws Exception {	
		try {
			// 取得允许操作的菜单List
			List<Map<String, Object>> canList = loginservice.getMenuList(canRroleArray);
			// 存在禁止权限的场合		
			if(forbidRroleArray != null && forbidRroleArray.length > 0) {
				// 取得所有的禁止菜单List
				List<Map<String, Object>> forbidList = loginservice.getMenuList(forbidRroleArray);
				if(forbidList != null && !forbidList.isEmpty()) {
					// 查询所有菜单的子菜单数
					List<Map<String, Object>> childMenuCountList = loginservice.getChildMenuCount();
					// 具有层级的菜单List
					List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
					// 把线性的数据转换成层级的数据
					convertList2DeepListMenu(forbidList, null, menuList);
					// 需要禁止的菜单List
					List<String> forbidMenuList = new ArrayList<String>();
					// 生成需要禁止的菜单List
					createForbidList(menuList, childMenuCountList, forbidMenuList);
					// 从允许操作的菜单中移除禁止的菜单
					Iterator it = canList.iterator();
					while (it.hasNext()) {
						Map<String, Object> map = (Map)it.next();
						String menuId = String.valueOf(map.get("MENU_ID"));
						if(forbidMenuList.contains(menuId) && !"LG".equals(menuId)) {
							it.remove();
						}
					}
				}
			}
			if(canList == null || canList.size() == 0) {
				throw new CherryException("ECM00104");
			}
			
			// 具有层级的菜单List
			List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
			// 把线性的数据转换成层级的数据
			convertList2DeepListMenu(canList, null, menuList);
			// 把允许菜单中除画面外没有子菜单的菜单移除
			filterMenuId(menuList);
			// Top菜单
			List<Map<String, Object>> topList = new ArrayList<Map<String, Object>>();
			// 左菜单
			Map<String, Object> leftMenuMap = new HashMap<String, Object>();	
			Iterator it = menuList.iterator();	
			while (it.hasNext()) {
				Map<String, Object> map = (Map)it.next();
				List<Map<String, Object>> list = (List)map.get("list");
				String menuid = String.valueOf(map.get("MENU_ID"));
				String menuType = String.valueOf(map.get("MENU_TYPE"));
				String menuName = String.valueOf(map.get("MENU_NM"));
				String menuURL = String.valueOf(map.get("MENU_URL"));
				String parentMenuID = String.valueOf(map.get("PARENT_MENU_ID"));
				String menuTarget = String.valueOf(map.get("MENU_INDICATION_TARGET"));
				String icon = String.valueOf(map.get("IconCSS"));
				CherryMenu cherryMenu = new CherryMenu();
				cherryMenu.setMenuID(menuid);
				cherryMenu.setMenuName(menuName);
				cherryMenu.setMenuType(menuType);
				cherryMenu.setMenuURL(menuURL);
				cherryMenu.setParentMenuID(parentMenuID);
				cherryMenu.setMenuTarget(menuTarget);
				cherryMenu.setIconCSS(icon);
				getLeftMenuDocument(list, cherryMenu);
				leftMenuMap.put(menuid, cherryMenu);
				map.remove("list");
				topList.add(map);
			}
			request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_LIST, topList);
			request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP, leftMenuMap);
		} catch (Exception ex) {
			//ex.g
			if (ex instanceof CherryException) {
				// 此登录用户无菜单抛出"该账号无权使用任何功能，请联系管理员进行授权！"的提示信息
				throw ex;
			} else {
				// 其他异常则抛出"菜单加载失败"提示信息
				throw new CherryException("ECM00010", ex);
			}
		}
	}
//	public CherryMenu getLeftMenuDocumentNew(int[] canRroleArray,int[] forbidRroleArray,String argMenuId,HttpServletRequest request)throws Exception {
//	    //允许操作的菜单
//		List canList = loginservice.getLeftMenu(canRroleArray,argMenuId);
//		//禁止操作的菜单		
//		List forbidList = new ArrayList();
//		if(forbidRroleArray!=null&&forbidRroleArray.length>0){
//			forbidList = loginservice.getLeftMenu(forbidRroleArray,argMenuId);
//		}	
//		//取出的list中菜单是按照MENU_ID的长度来排序的,且第一个是子系统ID，两位的
//		//
//		for(int i =0;i<forbidList.size();i++){
//			HashMap map = (HashMap) forbidList.get(i);
//			String menuType = String.valueOf(map.get("MENU_TYPE"));		
//			String menuId = String.valueOf(map.get("MENU_ID"));	
//			if (CherryConstants.MENUTYPE_LEFTCHILD.equals(menuType)||CherryConstants.MENUTYPE_POPUPPAGE.equals(menuType)) {
//				//如果是画面，则需要判断是禁止到画面还是禁止到画面上的按钮
//				boolean controlFlag = true;
//				for(int j=i;j<forbidList.size();j++){
//					HashMap map2 = (HashMap) forbidList.get(j);
//					String pageId = String.valueOf(map2.get("MENU_ID"));
//					String parentId = String.valueOf(map2.get("PARENT_MENU_ID"));
//					if(menuId.equals(parentId)){
//						controlFlag = false;
//						//控件的父ID是画面ID，去允许列表里将其移除
//						Iterator canIt = canList.iterator();
//						while (canIt.hasNext()) {
//							HashMap canmap = (HashMap) canIt.next();
//							String menuId2 = String.valueOf(canmap.get("MENU_ID"));
//							if (menuId2.equals(pageId)) {
//								canIt.remove();
//								break;
//							}
//						}
//					}
//				}
//
//				//如果是禁止到画面,则移除允许列表中的画面，该画面下的控件还是存在的，但是在后面生成xml时会被屏蔽掉
//				if(controlFlag){
//					Iterator canIt = canList.iterator();
//					while (canIt.hasNext()) {
//						HashMap canmap = (HashMap) canIt.next();
//						String menuId2 = String.valueOf(canmap.get("MENU_ID"));
//						if (menuId2.equals(menuId)) {
//							canIt.remove();
//							break;
//						}
//					}
//				}
//			}
//		}		
//		
//
//		//过滤后，如果一个子系统下没有画面，则返回null
//		Iterator canIt = canList.iterator();
//		int pageCount = 0;
//		while(canIt.hasNext()){
//			HashMap map = (HashMap) canIt.next();
//			String menuType = String.valueOf(map.get("MENU_TYPE"));
//			if(CherryConstants.MENUTYPE_LEFTCHILD.equals(menuType)){
//				//只要存在一个画面，就可以跳出
//				pageCount =1;
//				break;
//			}			
//		}
//		if(pageCount==0){
//			return null;
//		}
//		
//		CherryMenu miniMenu1 = new CherryMenu();
//
//		CherryMenu miniMenu2;
//		CherryMenu miniMenu3;
//		CherryMenu miniMenu4;
//
//		String menuid;
//		String menuType;
//		String menuName;
//		String menuURL;
//		String parentMenuID;
//		String menuTarget;
//
//		for (int i = 0; i < canList.size(); i++) {
//			HashMap map = (HashMap) canList.get(i);
//			menuid = String.valueOf(map.get("MENU_ID"));
//			menuType = String.valueOf(map.get("MENU_TYPE"));
//			menuName = String.valueOf(map.get("MENU_NM"));
//			menuURL = String.valueOf(map.get("MENU_URL"));
//			parentMenuID = String.valueOf(map.get("PARENT_MENU_ID"));
//			menuTarget = String.valueOf(map.get("MENU_INDICATION_TARGET"));
//
//			if (i == 0) {
//				miniMenu1.setMenuID(menuid);
//				miniMenu1.setMenuName(menuName);
//				miniMenu1.setMenuType(menuType);
//				miniMenu1.setMenuURL(menuURL);
//				miniMenu1.setParentMenuID(parentMenuID);
//				miniMenu1.setMenuTarget(menuTarget);
//				continue;
//			}
//
//			if ("2".equals(menuType)) {
//				miniMenu2 = new CherryMenu();
//				miniMenu2.setMenuID(menuid);
//				miniMenu2.setMenuName(menuName);
//				miniMenu2.setMenuType(menuType);
//				miniMenu2.setMenuURL(menuURL);
//				miniMenu2.setParentMenuID(parentMenuID);
//				miniMenu2.setParentMenuName(miniMenu1.getMenuName());
//				miniMenu2.setParentMenu(miniMenu1);
//				miniMenu2.setMenuTarget(menuTarget);
//				miniMenu1.addChildMenu(miniMenu2);
//				continue;
//			}
//
//			if ("3".equals(menuType)) {
//				List<CherryMenu> childList = miniMenu1.getChildList();
//				for (int k = 0; k < childList.size(); k++) {
//					CherryMenu tmp = childList.get(k);
//					if (tmp.getMenuID().equals(parentMenuID)) {
//						miniMenu3 = new CherryMenu();
//						miniMenu3.setMenuID(menuid);
//						miniMenu3.setMenuName(menuName);
//						miniMenu3.setMenuType(menuType);
//						miniMenu3.setMenuURL(menuURL);
//						miniMenu3.setParentMenuID(parentMenuID);
//						miniMenu3.setParentMenuName(tmp.getMenuName());
//						miniMenu3.setMenuTarget(menuTarget);
//						miniMenu3.setParentMenu(tmp);
//						tmp.addChildMenu(miniMenu3);
//					}
//				}
//				continue;
//			}
//			
//			if ("4".equals(menuType)) {
//				CherryMenu tmp = miniMenu1.getChildMenuByID(parentMenuID);
//					if (null!=tmp) {
//						miniMenu4 = new CherryMenu();
//						miniMenu4.setMenuID(menuid);
//						miniMenu4.setMenuName(menuName);
//						miniMenu4.setMenuType(menuType);
//						miniMenu4.setMenuURL(menuURL);
//						miniMenu4.setMenuTarget(menuTarget);
//						miniMenu4.setParentMenuID(parentMenuID);
//						miniMenu4.setParentMenuName(tmp.getMenuName());
//						miniMenu4.setParentMenu(tmp);
//						tmp.addChildMenu(miniMenu4);
//				}
//				continue;
//			}
//		}
//		return miniMenu1;
//	}
	public Document getLeftMenuDocument(int[] canRroleArray,int[] forbidRroleArray,String argMenuId,HttpServletRequest request)throws Exception {
	    //允许操作的菜单
		List canList = loginservice.getLeftMenu(canRroleArray,argMenuId);
		//禁止操作的菜单		
		List forbidList = new ArrayList();
		if(forbidRroleArray!=null&&forbidRroleArray.length>0){
			forbidList = loginservice.getLeftMenu(forbidRroleArray,argMenuId);
		}	
		//取出的list中菜单是按照MENU_ID的长度来排序的,且第一个是子系统ID，两位的
		//
		for(int i =0;i<forbidList.size();i++){
			HashMap map = (HashMap) forbidList.get(i);
			String menuType = String.valueOf(map.get("MENU_TYPE"));		
			String menuId = String.valueOf(map.get("MENU_ID"));	
			if (CherryConstants.MENUTYPE_LEFTCHILD.equals(menuType)||CherryConstants.MENUTYPE_POPUPPAGE.equals(menuType)) {
				//如果是画面，则需要判断是禁止到画面还是禁止到画面上的按钮
				boolean controlFlag = true;
				for(int j=i;j<forbidList.size();j++){
					HashMap map2 = (HashMap) forbidList.get(j);
					String pageId = String.valueOf(map2.get("MENU_ID"));
					String parentId = String.valueOf(map2.get("PARENT_MENU_ID"));
					if(menuId.equals(parentId)){
						controlFlag = false;
						//控件的父ID是画面ID，去允许列表里将其移除
						Iterator canIt = canList.iterator();
						while (canIt.hasNext()) {
							HashMap canmap = (HashMap) canIt.next();
							String menuId2 = String.valueOf(canmap.get("MENU_ID"));
							if (menuId2.equals(pageId)) {
								canIt.remove();
								break;
							}
						}
					}
				}

				//如果是禁止到画面,则移除允许列表中的画面，该画面下的控件还是存在的，但是在后面生成xml时会被屏蔽掉
				if(controlFlag){
					Iterator canIt = canList.iterator();
					while (canIt.hasNext()) {
						HashMap canmap = (HashMap) canIt.next();
						String menuId2 = String.valueOf(canmap.get("MENU_ID"));
						if (menuId2.equals(menuId)) {
							canIt.remove();
							break;
						}
					}
				}
			}
		}		
		

		//过滤后，如果一个子系统下没有画面，则返回null
		Iterator canIt = canList.iterator();
		int pageCount = 0;
		while(canIt.hasNext()){
			HashMap map = (HashMap) canIt.next();
			String menuType = String.valueOf(map.get("MENU_TYPE"));
			if(CherryConstants.MENUTYPE_LEFTCHILD.equals(menuType)){
				//只要存在一个画面，就可以跳出
				pageCount =1;
				break;
			}			
		}
		if(pageCount==0){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();
		createNodeList1(canList,doc);
		
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		//transformer.setOutputProperty("encoding","utf-8");
		transformer.transform(new DOMSource(doc), new StreamResult(baos));
		
//		String strXML = baos.toString();		
//       String sessionId = request.getSession().getId();
//       String filepath = request.getSession().getServletContext().getRealPath("/");
		
		//TODO 采用新的处理方式后不用生成xml文件了
//		String filename= filepath + sessionId + argMenuId + ".xml";		
//		FileOutputStream fileOutputStream= new FileOutputStream(filename);		
//		//OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");	
//		OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream);	
//		BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
//		bufferedWriter.write(strXML);		
//		bufferedWriter.close();
//		outputStreamWriter.close();
//		fileOutputStream.close();
		
		//request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENU_XMLDOCUMENT, doc);
//		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENU_XMLSTRING, strXML);
		//去掉没有子节点且自己没有URL的节点
//		List<Node> delList1 = new ArrayList<Node>();
//		List<Node> delList2 = new ArrayList<Node>();
//		if(doc!=null){
//			Node root=doc.getDocumentElement(); 
//			/** 如果root有子元素 */ 
//			if(root.hasChildNodes()){
//				//模块级的节点
//				NodeList list1 = root.getChildNodes();
//				for(int i=0;i<list1.getLength();i++){					
//					 Node tempNode1 = list1.item(i);				
//					 if(tempNode1.hasChildNodes()){
//						 //功能级节点
//						 NodeList list2 = tempNode1.getChildNodes();
//						 for(int j=0;j<list2.getLength();j++){
//							 Node tempNode2 = list2.item(j);
//							 NamedNodeMap map2 = tempNode2.getAttributes();
//							 String url2 = map2.getNamedItem("URL").getNodeValue();
//							 if(!tempNode2.hasChildNodes()&&(url2==null||url2.equals("null"))){
//								 delList2.add(tempNode2);
//							 }
//						 }	
//						 //删除功能级的空节点
//						 for(Node deltemp:delList2){
//							 tempNode1.removeChild(deltemp);							 
//						 }
//						 delList2.clear();
//					 }
//				}	
//				
//				//删除模块级的空节点
//				list1 = root.getChildNodes();
//				for(int i=0;i<list1.getLength();i++){					
//					 Node tempNode1 = list1.item(i);
//					 NamedNodeMap map1 = tempNode1.getAttributes();
//					 String url1 = map1.getNamedItem("URL").getNodeValue();
//					 if(!tempNode1.hasChildNodes()&&(url1==null||url1.equals("null"))){
//						 delList1.add(tempNode1);
//					 }			 
//				}
//				 for(Node deltemp:delList1){
//					 root.removeChild(deltemp);
//				}	
//			}
//		}
		return doc;
		
	}
	private void createNodeList1(List ls,Document doc)throws Exception{	    	
	    	String menuid;
	    	String menuType;
	    	List<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();
	    	List<HashMap<String,String>> list2 = new ArrayList<HashMap<String,String>>();
	    	List<HashMap<String,String>> list3 = new ArrayList<HashMap<String,String>>();	
	    	List<HashMap<String,String>> list4 = new ArrayList<HashMap<String,String>>();	
	    	for(int i=0;i<ls.size();i++){
	    		HashMap map = (HashMap)ls.get(i);
	    		HashMap<String,String> tempMap = new HashMap<String,String>();
	    		//取得菜单ID，名字，对应的URL
	    		menuid = String.valueOf(map.get("MENU_ID"));
	    		menuType = String.valueOf(map.get("MENU_TYPE"));
	    		
	    		tempMap.put("MENU_ID", menuid);
	    		tempMap.put("MENU_NM", String.valueOf(map.get("MENU_NM")));
	    		tempMap.put("MENU_URL", String.valueOf(map.get("MENU_URL")));
	    		tempMap.put("PARENT_MENU_ID", String.valueOf(map.get("PARENT_MENU_ID")));
	    		tempMap.put("MENU_INDICATION_TARGET", String.valueOf(map.get("MENU_INDICATION_TARGET")));
	    		tempMap.put("MENU_TYPE", menuType);
	    		
	    		
	    		if(CherryConstants.MENUTYPE_TOPMENU.equals(menuType)){
	    			list1.add(tempMap);
	    		}else if(CherryConstants.MENUTYPE_LEFTPARENT.equals(menuType)){
	    			list2.add(tempMap);
	    		}else if(CherryConstants.MENUTYPE_LEFTCHILD.equals(menuType)||CherryConstants.MENUTYPE_POPUPPAGE.equals(menuType)){
	    			list3.add(tempMap);
	    		}else if(CherryConstants.MENUTYPE_LEFTCONTROL.equals(menuType)){
	    			list4.add(tempMap);
	    		}
	    		//addTreeNode(menuid,menuname,menuurl,parentmeuid,doc);
	    	}
    		createDocumentTree(list1,"TreeL0",doc);
    		createDocumentTree(list2,"TreeL1",doc);
    		createDocumentTree(list3,"TreeL3",doc);
    		createDocumentTree(list4,"TreeL4",doc);
	    }
	private void createDocumentTree(List<HashMap<String,String>> list,String level,Document doc){
    	String menuid;
    	String menuname;
    	String menuurl;
    	String parentmeuid;
    	String menutype;
    	String menutarget;
    	
		for(int i=0;i<list.size();i++){
			HashMap<String,String> tempMap = list.get(i);
    		menuid = tempMap.get("MENU_ID");
    		menuname = tempMap.get("MENU_NM");
    		menuurl = tempMap.get("MENU_URL");
    		parentmeuid = tempMap.get("PARENT_MENU_ID");
    		menutype = tempMap.get("MENU_TYPE");
    		menutarget = tempMap.get("MENU_INDICATION_TARGET");
    		if("TreeL0".equals(level)){
    			//子系统菜单
    			Element Node_L0 = doc.createElement(menuid);
    	    	Node_L0.setAttribute("MENU_ID", menuid);
    	    	Node_L0.setAttribute("MENU_NM",menuname);
    	    	Node_L0.setAttribute("MENU_TARGET",menutarget);
    	    	Node_L0.setAttribute("URL",menuurl);
    	    	Node_L0.setAttribute("MENU_TYPE",menutype);
    	    	doc.appendChild(Node_L0);
    		}else{
    			Element Node_L1 = doc.createElement(menuid);
    			Node_L1.setAttribute("MENU_ID", menuid);
    			Node_L1.setAttribute("MENU_NM",menuname);
    			Node_L1.setAttribute("MENU_TARGET",menutarget);
    	    	Node_L1.setAttribute("URL",menuurl);
    	    	Node_L1.setAttribute("MENU_TYPE",menutype);
    	    	NodeList nodeList0 = doc.getElementsByTagName(parentmeuid);
    	    	if(nodeList0!=null&&nodeList0.getLength()>0){
    	    		nodeList0.item(0).appendChild(Node_L1);	
    	    	}
    		}
		}
	} 
	
	/**
	 * 
	 * 生成CherryMenu对象
	 * 
	 * @param 
	 * 		canList 菜单List
	 * 		cherryMenu 需要生成的CherryMenu对象
	 */
	public void getLeftMenuDocument(List<Map<String, Object>> canList, CherryMenu cherryMenu) {
		for(int i = 0; i < canList.size(); i++) {
			Map<String, Object> map = canList.get(i);
			List<Map<String, Object>> nextList = (List)map.get("list");
			String menuid = String.valueOf(map.get("MENU_ID"));
			String menuType = String.valueOf(map.get("MENU_TYPE"));
			String menuName = String.valueOf(map.get("MENU_NM"));
			String menuURL = String.valueOf(map.get("MENU_URL"));
			String parentMenuID = String.valueOf(map.get("PARENT_MENU_ID"));
			String menuTarget = String.valueOf(map.get("MENU_INDICATION_TARGET"));
			String icon = String.valueOf(map.get("IconCSS"));
			CherryMenu cherryMenuChild = new CherryMenu();
			cherryMenuChild.setMenuID(menuid);
			cherryMenuChild.setMenuName(menuName);
			cherryMenuChild.setMenuType(menuType);
			cherryMenuChild.setMenuURL(menuURL);
			cherryMenuChild.setIconCSS(icon);
			cherryMenuChild.setParentMenuID(parentMenuID);
			cherryMenuChild.setMenuTarget(menuTarget);
			cherryMenuChild.setParentMenuName(cherryMenu.getMenuName());
			cherryMenuChild.setParentMenu(cherryMenu);
			cherryMenu.addChildMenu(cherryMenuChild);
			if(nextList != null && !nextList.isEmpty()) {
				getLeftMenuDocument(nextList, cherryMenuChild);
			}
		}
	}
	
	/**
	 * 
	 * 把线性的数据转换成层级的数据
	 * 
	 * @param 
	 * 		list 线性数据List
	 * 		menuId 每一层的父节点ID
	 *      resultList 层级数据List
	 */
	public void convertList2DeepListMenu(List<Map<String, Object>> list, String menuId, List<Map<String, Object>> resultList) {
		if(list == null || list.isEmpty()) {
			return;
		}
		// 父节点ID为null时，取菜单类型为1（TOPMENU）的数据作为第一层数据
		if(menuId == null) {
			for (int i = 0; i < list.size(); i++) {
				if("1".equals(list.get(i).get("MENU_TYPE"))) {
					resultList.add(list.get(i));
					list.remove(i);
					i--;
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				// 把相同父节点ID的数据作为一组数据
				if(menuId.equals(list.get(i).get("PARENT_MENU_ID"))) {
					resultList.add(list.get(i));
					list.remove(i);
					i--;
				}
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			// 菜单类型为4（画面控件）的数据作为最后一层数据
			if(!"4".equals(resultList.get(0).get("MENU_TYPE"))) {
				for(int i = 0; i < resultList.size(); i++) {
					if(list == null || list.isEmpty()) {
						break;
					}
					String deepMenuId = (String)resultList.get(i).get("MENU_ID");
					List<Map<String, Object>> deepResultList = new ArrayList<Map<String,Object>>();
					resultList.get(i).put("list", deepResultList);
					// 递归取得当前层的下层结构数据
					convertList2DeepListMenu(list,deepMenuId,deepResultList);
				}
			}
		}
	}
	
	/**
	 * 
	 * 生成需要禁止的菜单List
	 * 
	 * @param 
	 * 		list 所有的禁止菜单List（层级结构的）
	 * 		childMenuCountList 所有菜单的子菜单数
	 *      removeList 需要禁止的菜单List
	 */
	public void createForbidList(List<Map<String, Object>> list, List<Map<String, Object>> childMenuCountList, List<String> removeList) {
		if(list == null || list.isEmpty()) {
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String menuId = (String)map.get("MENU_ID");
			List nextList = (List)map.get("list");
			// 不存在子菜单的场合，表示该菜单需要禁止
			if(nextList == null || nextList.isEmpty()) {
				removeList.add(menuId);
			} else {
				String menuType = (String)map.get("MENU_TYPE");
				if("1".equals(menuType) || "2".equals(menuType)) {
					createForbidList(nextList, childMenuCountList, removeList);
				} else {
					// 根据菜单ID取得该菜单的子菜单数
					int size = getChildMenuCountByMenuId(childMenuCountList, menuId);
					// 子菜单数和禁止菜单的子菜单数一样的场合，表示该菜单需要禁止，否则对子菜单进行递归处理
					if(nextList.size() == size) {
						removeList.add(menuId);
					} else {
						createForbidList(nextList, childMenuCountList, removeList);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 根据菜单ID取得该菜单的子菜单数
	 * 
	 * @param 
	 * 		childMenuCountList 所有菜单的子菜单数
	 * 		menuId 菜单ID
	 * @return
	 * 		子菜单数
	 */
	public int getChildMenuCountByMenuId(List<Map<String, Object>> childMenuCountList, String menuId) {
		
		int count = 0;
		for(Map<String, Object> map : childMenuCountList) {
			String menuIdTemp = (String)map.get("menuId");
			if(menuId.equals(menuIdTemp)) {
				count = (Integer)map.get("count");
				break;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * 把允许菜单中除画面外没有子菜单的菜单移除
	 * 
	 * @param 
	 * 		menuList 允许菜单List
	 */
	public void filterMenuId(List<Map<String, Object>> menuList) {
		
		for(int i = 0; i < menuList.size(); i++) {
			Map<String, Object> menuMap = menuList.get(i);
			String menuType = (String)menuMap.get("MENU_TYPE");
			if("1".equals(menuType) || "2".equals(menuType)) {
				List nextList = (List)menuMap.get("list");
				if(nextList != null && !nextList.isEmpty()) {
					filterMenuId(nextList);
					if(nextList == null || nextList.isEmpty()) {
						menuList.remove(i);
						i--;
					}
				} else {
					menuList.remove(i);
					i--;
				}
			} else {
				return;
			}
		}
	}
	
	/**
	 * 更新登录信息
	 */
	public void updateLoginInfo(UserInfo userInfo){
        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("BIN_UserID", userInfo.getBIN_UserID());
        updateMap.put("LastLogin", userInfo.getLoginTime());
        updateMap.put("LoginIP", userInfo.getLoginIP());
        // 更新者
        updateMap.put("updatedBy", userInfo.getBIN_UserID());
        // 更新模块
        updateMap.put("updatePGM", "Login");
        
        loginservice.updateLoginInfo(updateMap);
	}
}
