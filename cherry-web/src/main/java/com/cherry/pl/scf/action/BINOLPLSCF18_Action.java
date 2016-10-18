package com.cherry.pl.scf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.bl.BINOLPLSCF18_BL;
import com.cherry.pl.scf.form.BINOLPLSCF18_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLPLSCF18_Action extends BaseAction implements ModelDriven<BINOLPLSCF18_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 基本配置管理Form */
	private BINOLPLSCF18_Form form = new BINOLPLSCF18_Form();
	
	/** 基本配置管理BL */
	@Resource
	private BINOLPLSCF18_BL binOLPLSCF18_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 基本配置管理画面初期化
	 * 
	 * @return 基本配置管理画面
	 */
	@SuppressWarnings("unchecked")
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		List<Map<String,Object>> brandInfoList = new ArrayList<Map<String,Object>>();
		//admin登录 场合
		if(isAdmin()){
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put(CherryConstants.BRANDINFOID,CherryConstants.BRAND_INFO_ID_VALUE);
            mapParam.put(CherryConstants.ORGANIZATIONINFOID,CherryConstants.ORGANIZATION_CODE_DEFAULT);
            // 取得基本配置信息List
            List<Map<String, Object>> systemConfigList = binOLPLSCF18_BL.getAdminSystemConfigList(mapParam);
            form.setSystemConfigList(systemConfigList);
		}else{
		      // 总部用户的场合
	        if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
	            // 所属品牌
	            map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
	            // 取得品牌List
	            brandInfoList = binOLCM05_BL.getBrandInfoList(map);
	        } else {
	            Map<String, Object> brandMap = new HashMap<String, Object>();
	            // 品牌ID
	            brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	            // 品牌名称
	            brandMap.put("brandName", userInfo.getBrandName());
	            brandInfoList.add(brandMap);
	        }
	        
	        // 所属品牌
	        if (brandInfoList!=null && !brandInfoList.isEmpty()){
	            map.put(CherryConstants.BRANDINFOID, brandInfoList.get(0).get(CherryConstants.BRANDINFOID));
	            // 取得基本配置信息List
	            List<Map<String,Object>> systemConfigList = binOLPLSCF18_BL.getSystemConfigList(map);
	            
	            form.setBrandInfoList(brandInfoList);
	            form.setSystemConfigList(systemConfigList);
	        }
		}

		return SUCCESS;
	}
	
	/**
	 * 通过品牌ID取得基本配置信息
	 * 
	 * @return 基本配置管理画面
	 */
	public String searchBsCf() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得基本配置信息List
		List<Map<String,Object>> systemConfigList = binOLPLSCF18_BL.getSystemConfigList(map);
		
		form.setSystemConfigList(systemConfigList);
		return SUCCESS;
	}
	
	/**
	 * 保存基本配置信息处理
	 * 
	 * @return 基本配置管理画面
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String saveBsCf() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
	    // 创建者
        map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
        // 创建程序名
        map.put(CherryConstants.CREATEPGM, "BINOLPLSCF01");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF01");
		try {
		    //Admin登录
		    if(isAdmin()){
		        //插入默认配置信息
		        binOLPLSCF18_BL.tran_updateSystemConfig(map);
		    }else{
		        //插入基本配置信息
	            binOLPLSCF18_BL.tran_insertSystemConfig(map); 
		    }

		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	private boolean isAdmin(){
	       // 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE && CherryConstants.ORGANIZATION_CODE_DEFAULT.equals(String.valueOf(userInfo.getBIN_OrganizationInfoID()))){
		    return true;
		}else{
		    return false;
		}
	}
	
	@Override
	public BINOLPLSCF18_Form getModel() {
		return form;
	}

}
