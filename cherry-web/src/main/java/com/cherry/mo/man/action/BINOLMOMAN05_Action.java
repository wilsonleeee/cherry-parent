/*  
 * @(#)BINOLMAMAN05_Action.java    1.0 2011-7-29     
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

package com.cherry.mo.man.action;

import java.io.File;
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.man.form.BINOLMOMAN05_Form;
import com.cherry.mo.man.interfaces.BINOLMOMAN05_IF;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLMOMAN05_Action extends BaseAction implements
		ModelDriven<BINOLMOMAN05_Form> {

	private static final long serialVersionUID = 1L;

	private BINOLMOMAN05_Form form = new BINOLMOMAN05_Form();
	
	private Map resultMap = null;
	
	  /** 品牌List */
    private List<Map<String, Object>> brandInfoList;
    
    /**上传的文件*/
    private File upExcel;
    
    @Resource
	private BINOLCM05_BL binOLCM05_BL;
    @Resource
    private BINOLMOMAN05_IF  binOLMOMAN05_BL;
    
	public String init(){
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
    	return SUCCESS;
    }
	
	public String importUdisk() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.CREATEPGM, "BINOLMOMAN05");
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN05");
			binOLMOMAN05_BL.tran_importUdisk(upExcel, map);
			this.addActionMessage(getText("ICM00002"));  
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}catch(Exception ex){
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	public String getInformation(){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("udiskSn", form.getUdiskSn().trim());
			map.put("employeeCode", form.getEmployeeCode().trim());
			resultMap = binOLMOMAN05_BL.getInformation(map);
			//ConvertUtil.setResponseByAjax(response,resultMap);
		}catch(Exception ex){
			if(ex instanceof CherryException){
				if("EMO00042".equals(((CherryException) ex).getErrCode())){
					this.addFieldError("udiskSn",getText("EMO00042"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}else if("EMO00043".equals(((CherryException) ex).getErrCode())){
					this.addFieldError("employeeCode",getText("EMO00043"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}else if("EMO00044".equals(((CherryException) ex).getErrCode())){
					this.addFieldError("udiskSn",getText("EMO00044"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}else if("EMO00046".equals(((CherryException) ex).getErrCode())){
					this.addFieldError("employeeCode",getText("EMO00046"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}else{
					this.addFieldError("udiskSn",getText("EMO00056"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
		}
		return SUCCESS;
	}
	
	public String saveUdisk() throws Exception{
		try{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLMOMAN05");
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEPGM, "BINOLMOMAN05");
		String[] udiskSnArr = form.getUdiskSnArr();
		String[] employeeIdArr = form.getEmployeeIdArr();
		String[] gradeArr = form.getGradeArr();
		binOLMOMAN05_BL.tran_addUdisk(udiskSnArr, employeeIdArr, gradeArr, map);
		this.addActionMessage(getText("ICM00002"));  
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception ex){
			if(ex instanceof CherryException){
                CherryException temp = (CherryException)ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
            	throw ex;
            }
		}
	}
	
	@Override
	public BINOLMOMAN05_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}
	
	
}
