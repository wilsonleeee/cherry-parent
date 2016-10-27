package com.cherry.mo.mup.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.mup.form.BINOLMOMUP01_Form;
import com.cherry.mo.mup.interfaces.BINOLMOMUP01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOMUP01_Action extends BaseAction 
	implements ModelDriven<BINOLMOMUP01_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLMOMUP01_Form form = new BINOLMOMUP01_Form();
	
	@Resource
	private BINOLMOMUP01_IF binOLMOMUP01_BL;
	
	private List<Map<String,Object>> softVersionInfoList;
	
	/**
	 * 初始化页面
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);

        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        
        return SUCCESS;
	}
	
	/**
	 * 获取软件版本信息
	 * @return 返回页面的资源名称
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String search()throws Exception{
		try {
			
			//取得参数Map
			Map<String,Object> searchMap = getSearchMap();
			
			Map<String,Object> retMap = binOLMOMUP01_BL.getSoftVersionInfo(searchMap);
			
			int count = (Integer)retMap.get("sum");
			
			softVersionInfoList = (List<Map<String, Object>>) retMap.get("retList");
			
			// form表单设置
            form.setITotalDisplayRecords(count);
            form.setITotalRecords(count);
			
		} catch (Exception e) {
			if(e instanceof CherryException){
        		this.addActionError(((CherryException) e).getErrMessage());
        	}else{
        		this.addActionError(e.getMessage());
        	}
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 添加软件版本信息
	 * @return
	 */
	public String add(){
		return SUCCESS;
	}
	
	private Map<String,Object> getSearchMap(){
		
		// 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        
        //组织ID
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        
        //软件版本
        map.put("version", form.getVersion());
        //下载地址
        map.put("downloadUrl", form.getDownloadUrl());
        //有效标志
        map.put("validFlag", form.getValidFlag());
        
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
        
	}
	
	@Override
	public BINOLMOMUP01_Form getModel() {
		
		return form;
	}

	public List<Map<String, Object>> getSoftVersionInfoList() {
		return softVersionInfoList;
	}

	public void setSoftVersionInfoList(List<Map<String, Object>> softVersionInfoList) {
		this.softVersionInfoList = softVersionInfoList;
	}

}
