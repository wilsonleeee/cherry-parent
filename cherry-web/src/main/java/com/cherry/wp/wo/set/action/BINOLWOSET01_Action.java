package com.cherry.wp.wo.set.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.wo.set.form.BINOLWOSET01_Form;
import com.cherry.wp.wo.set.interfaces.BINOLWOSET01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 营业员管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWOSET01_Action extends BaseAction implements ModelDriven<BINOLWOSET01_Form> {

	private static final long serialVersionUID = -4601103340577147768L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLWOSET01_Action.class);
	
	/** 营业员管理BL **/
	@Resource
	private BINOLWOSET01_IF binOLWOSET01_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 营业员管理画面初始化
	 * 
	 * @return 营业员管理画面
	 */
	public String init() {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoID = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = String.valueOf(userInfo.getBIN_BrandInfoID());
		String mode1 = binOLCM14_BL.getConfigValue("1066", organizationInfoID, brandInfoID);
		if(mode1 != null && "2".equals(mode1)) {
			String mode2 = binOLCM14_BL.getConfigValue("1293", organizationInfoID, brandInfoID);
			if(mode2 != null && "2".equals(mode2)) {
				mode = "3";
			} else {
				mode = "2";
			}
		} else {
			mode = "1";
		}
		return SUCCESS;
	}
	
	/**
	 * 查询营业员
	 * 
	 * @return 营业员一览画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		map.put("brandCode", userInfo.getBrandCode());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		int count = binOLWOSET01_BL.getBAInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			baInfoList = binOLWOSET01_BL.getBAInfoList(map);
		}
		
		// 所属组织ID
		String orgId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 手机号校验规则
		mobileRule = binOLCM14_BL.getConfigValue("1090", orgId, brandId);
		
		return SUCCESS;
	}
	
	/**
	 * 添加营业员初始画面
	 * 
	 */
	public String addInit() {
		return SUCCESS;
	}
	
	/**
	 * 添加营业员
	 * 
	 */
	public void add() throws Exception {
		
		String code = "ok";
		String errorMes = "";
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			map.put("organizationId", counterInfo.getOrganizationId());
			
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 组织代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 登录名
			map.put("loginName", userInfo.getLoginName());
    		
    		// 作成者
    		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
    		// 更新者
    		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
    		// 作成模块
    		map.put(CherryConstants.CREATEPGM, "BINOLWOSET01");
    		// 更新模块
    		map.put(CherryConstants.UPDATEPGM, "BINOLWOSET01");
			
			binOLWOSET01_BL.tran_addBA(map);
		} catch (Exception e) {
			code = "error";
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                errorMes = temp.getErrMessage();
            }else{
            	errorMes = getText("ECM00005");
            }
			logger.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
	 * 同步营业员
	 * 
	 */
	public void syncBa() throws Exception {
		
		String code = "ok";
		String errorMes = "";
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			map.put("organizationId", counterInfo.getOrganizationId());
			
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 组织代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 登录名
			map.put("loginName", userInfo.getLoginName());
			//组织ID
			map.put(CherryConstants.ORGANIZATIONID, userInfo.getBIN_OrganizationID());
    		
    		// 作成者
    		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
    		// 更新者
    		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
    		// 作成模块
    		map.put(CherryConstants.CREATEPGM, "BINOLWOSET01");
    		// 更新模块
    		map.put(CherryConstants.UPDATEPGM, "BINOLWOSET01");
			//同步BA数据
    		binOLWOSET01_BL.synaBa(map);
		} catch (Exception e) {
			code = "error";
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                errorMes = temp.getErrMessage();
            }else{
            	errorMes = getText("ECM00005");
            }
			logger.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	
	public void validateAdd() throws Exception {
		
		if(form.getEmployeeId() == null || "".equals(form.getEmployeeId())) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			if("2".equals(mode)) {
				map.put("identityCard", form.getIdentityCard());
				baInfo = binOLWOSET01_BL.checkBAInfo(map);
				if(baInfo != null) {
					this.addFieldError("identityCard", getText("EWP00002"));
				}
			} else if("3".equals(mode)) {
				map.put("mobilePhone", form.getMobilePhone());
				baInfo = binOLWOSET01_BL.checkBAInfo(map);
				if(baInfo != null) {
					this.addFieldError("mobilePhone", getText("EWP00003"));
				}
			}
		}
	}
	
	/**
	 * 编辑营业员初始画面
	 * 
	 */
	public String updateInit() throws Exception {
		// 登陆用户信息
					UserInfo userInfo = (UserInfo) session
							.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", form.getEmployeeId());
		map.put("brandCode", userInfo.getBrandCode());
		baInfo = binOLWOSET01_BL.getBAInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 编辑营业员
	 * 
	 */
	public void update() throws Exception {
		
		String code = "ok";
		String errorMes = "";
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 组织代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 登录名
			map.put("loginName", userInfo.getLoginName());
			
    		// 更新者
    		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
    		// 更新模块
    		map.put(CherryConstants.UPDATEPGM, "BINOLWOSET01");
			
			binOLWOSET01_BL.tran_updBA(map);
		} catch (Exception e) {
			code = "error";
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                errorMes = temp.getErrMessage();
            }else{
            	errorMes = getText("ECM00005");
            }
			logger.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	public void validateUpdate() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if("2".equals(mode)) {
			if(!form.getOldIdentityCard().equals(form.getIdentityCard())) {
				map.put("identityCard", form.getIdentityCard());
				baInfo = binOLWOSET01_BL.checkBAInfo(map);
				if(baInfo != null) {
					this.addFieldError("identityCard", getText("EWP00002"));
				}
			}
		} else if("3".equals(mode)) {
			if(!form.getOldMobilePhone().equals(form.getMobilePhone())) {
				map.put("mobilePhone", form.getMobilePhone());
				baInfo = binOLWOSET01_BL.checkBAInfo(map);
				if(baInfo != null) {
					this.addFieldError("mobilePhone", getText("EWP00003"));
				}
			}
		}
	}
	
	/**
	 * 删除营业员柜台关系
	 * 
	 */
	public void delete() throws Exception {
		
		String code = "ok";
		String errorMes = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employeeId", form.getEmployeeId());
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			map.put("organizationId", counterInfo.getOrganizationId());
			
			binOLWOSET01_BL.tran_delBA(map);
		} catch (Exception e) {
			code = "error";
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                errorMes = temp.getErrMessage();
            }else{
            	errorMes = getText("ECM00005");
            }
			logger.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	public String check() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌代码
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		if("1".equals(mode)) {
			map.put("employeeCode", form.getSearchKey());
			form.setEmployeeCode(form.getSearchKey());
			baInfo = binOLWOSET01_BL.checkBAInfo(map);
		} else if("2".equals(mode)) {
			map.put("identityCard", form.getSearchKey());
			form.setIdentityCard(form.getSearchKey());
			baInfo = binOLWOSET01_BL.checkBAInfo(map);
		} else if("3".equals(mode)) {
			map.put("mobilePhone", form.getSearchKey());
			form.setMobilePhone(form.getSearchKey());
			baInfo = binOLWOSET01_BL.checkBAInfo(map);
		}
		return SUCCESS;
	}
	
	/** 营业员信息List **/
	private List<Map<String, Object>> baInfoList;
	
	/** 营业员信息 **/
	private Map baInfo;
	
	/** BA管理模式 **/
	private String mode;
	
	/** 手机校验规则 **/
	private String mobileRule;
	
	public List<Map<String, Object>> getBaInfoList() {
		return baInfoList;
	}

	public void setBaInfoList(List<Map<String, Object>> baInfoList) {
		this.baInfoList = baInfoList;
	}

	public Map getBaInfo() {
		return baInfo;
	}

	public void setBaInfo(Map baInfo) {
		this.baInfo = baInfo;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
	}

	/** 营业员管理Form **/
	private BINOLWOSET01_Form form = new BINOLWOSET01_Form();

	@Override
	public BINOLWOSET01_Form getModel() {
		return form;
	}

}
