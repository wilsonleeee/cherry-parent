package com.cherry.tao.lg.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.RoleInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.gadget.interfaces.GadgetPrivilegeIf;
import com.cherry.lg.lgn.bl.LoginBusinessLogic;
import com.cherry.lg.lgn.service.LoginService;
import com.cherry.tao.lg.bl.BINOLTAOLG01_BL;

public class BINOLTAOLG01_Action extends BaseAction {
	
	private static final long serialVersionUID = -8697520117911286925L;
	
	@Resource
	private BINOLTAOLG01_BL binOLTAOLG01_BL;
	
	@Resource
    private LoginBusinessLogic loginbusinesslogic;
	
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private GadgetPrivilegeIf gadgetPrivilegeBL;
	
	@Resource
	private LoginService loginservice;
	
	public String taoInit() {
		return SUCCESS;
	}
	
	public String taoLogin() throws Exception {
		
		String loginName = binOLTAOLG01_BL.login(code);
		if(loginName != null) {
			try {
				doLogin("brandadminjst", loginName);
			} catch (Exception e) {
				throw new Exception("处理过程发生异常，请重试！");
			}
		} else {
			throw new Exception("登录授权失败，请重试！");
		}
		return SUCCESS;
	}
	
	public void doLogin(String loginName, String taoLoginName) throws Exception {
		
		// 取消数据库多语言对应，固定设zh_CN
		String language="zh_CN";		
		session.put(CherryConstants.SESSION_LANGUAGE, language);
		
		String dbname = loginbusinesslogic.userLogin(loginName);
		session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,dbname);
		CustomerContextHolder.setCustomerDataSourceType(dbname);
				
		List list = loginservice.checkAccount(loginName);
		String userID = String.valueOf(((Map)list.get(0)).get("BIN_UserID"));

		// 存放在用户其他的信息(如密码过期日期、开始提醒修改密码日、上次登录时间、IP)
		Map<String,Object> otherInfo = new HashMap<String,Object>();
		// 根据登录名初始化登录者的基本信息
		UserInfo userinfo = loginbusinesslogic.userInfoInitial(userID,
				loginName, language, otherInfo);
		userinfo.setLoginIP(request.getRemoteHost());
		userinfo.setLoginTime(new Date());
	    userinfo.setUserAgent(request.getHeader("User-Agent"));
	    userinfo.setSessionID(request.getSession().getId());
	    userinfo.setLoginName(taoLoginName);
		session.put(CherryConstants.SESSION_USERINFO, userinfo);
		
        // 更新登录信息（登录时间，登录IP）
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
		loginbusinesslogic.menuInitial(canRroleArray, forbidRroleArray,
				request);
		
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, "LG");
		
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
    	// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userinfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userinfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE ) {
			map.put(CherryConstants.BRANDINFOID, userinfo.getBIN_BrandInfoID());
		}
    	// 当前用户不是最大岗位级别的场合
		if(userinfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE || binOLCM00_BL.isMaxPosCategoryGrade(map, userinfo.getCategoryGrade())) {
			session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "0");
		} else {
			session.put(CherryConstants.SESSION_PRIVILEGE_FLAG, "1");
		}
		map.put("userId", userinfo.getBIN_UserID());
		map.put("brandCode", userinfo.getBrandCode());
		map.put("orgCode", userinfo.getOrganizationInfoCode());
		// 保存数据权限到MongoDB
		gadgetPrivilegeBL.savePrivilegeToMongoDB(map);
	}
	
	private String code;
	
	private String state;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
