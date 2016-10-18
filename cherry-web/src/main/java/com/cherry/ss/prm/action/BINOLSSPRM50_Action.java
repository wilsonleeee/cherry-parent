/*
 * 
 * @(#)BINOLSSPRM50_Action.java     1.0 2010/10/29		
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
package com.cherry.ss.prm.action;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM50_BL;
import com.cherry.ss.prm.form.BINOLSSPRM50_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品发货Excel
 * 
 * @author dingyc
 * 
 */
public class BINOLSSPRM50_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM50_Form> {

	private static final long serialVersionUID = 1L;

	private BINOLSSPRM50_Form form = new BINOLSSPRM50_Form();
//
//	@Resource
//	private BINOLCM01_BL binolcm01BL;
//
//	@Resource
//	private BINOLSSPRM50_BL binolssprm50BL;
//
//	/** 上传的文件 */
//	private File upExcel;
//	/** 上传的文件名，不包括路径 */
//	private String upExcelFileName;
//	/** buffer size */
//	private static final int BUFFER_SIZE = 16 * 1024;
//
//	/**
//	 * 画面初始化
//	 * 
//	 * @return
//	 */
//	public String init() {
//		try {
//			// 取得用户信息
//			UserInfo userInfo = (UserInfo) session
//					.get(CherryConstants.SESSION_USERINFO);
////			// 取得用户所属岗位对应的部门
////			//List<Map<String, String>> list = binolcm01BL.getControlOrganizationList(userInfo);
////	    	
////	    	//TODO:后期放开，从部门数据权限表中取得能操作的部门
////			List<Map<String, String>> list = binolcm01BL.getControlOrgListPrivilege(userInfo.getBIN_UserID(),CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,userInfo.getLanguage(),"1");
////	    	
////			form.setOutOrganizationList(list);
////
////			// 取得用户所属的第一个部门的仓库
////			if(list.size()>0){
////				form.setOutDepotList(getDepotList(list.get(0).get("OrganizationID"), userInfo.getLanguage()));
////			}
//			form.setBrandCode(userInfo.getBrandCode());	
//		} catch (Exception ex) {
//			this.addActionError(getText("ECM00036"));
//		}
//		return SUCCESS;
//	}
//
//	/**
//	 * 点击发货按钮
//	 * @return
//	 * @throws Exception
//	 */
//	public String submit() throws Exception {
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		binolcm01BL.completeUserInfo(userInfo, form.getOutOrganizationId(), "BINOLSSPRM50");
//		int ret = binolssprm50BL.tran_deliver(form, userInfo);
//		form.clear();
//		// 重新初始化画面
//		this.init();
//		//显示执行结果
//		if(ret==0){
//			this.addActionMessage(getText("ISS00002"));
//		}else{
//			this.addActionMessage(getText("ISS00003"));
//		}
//		return CherryConstants.GLOBAL_ACCTION_RESULT;
//	}
//
//	public String save() throws Exception {
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		binolcm01BL.completeUserInfo(userInfo, form.getOutOrganizationId(), "BINOLSSPRM50");
//		binolssprm50BL.tran_saveDeliver(form, userInfo);
//
//		// binolssprm17BL.droolsFlow(id);
//		form.clear();
//		// 重新初始化画面
//		this.init();
//		this.addActionMessage(getText("ICM00002"));
//		return CherryConstants.GLOBAL_ACCTION_RESULT;
//
//	}
//
//	/**
//	 * 通过Ajax取得指定部门所拥有的仓库
//	 * 
//	 * @throws Exception
//	 */
//	public void getDepotByAjax() throws Exception {
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		String organizationid = request.getParameter("organizationid");
//		List resultTreeList = getDepotList(organizationid, userInfo
//				.getLanguage());
//		ConvertUtil.setResponseByAjax(response, resultTreeList);
//	}
//
//	/**
//	 * 根据指定的部门ID和语言信息取得仓库信息
//	 * 
//	 * @param organizationID
//	 * @param language
//	 * @return
//	 */
//	private List<Map<String, Object>> getDepotList(String organizationID,
//			String language) {
//		List<Map<String, Object>> ret = binolcm01BL.getDepotList(
//				organizationID, language);
//		return ret;
//	}
//
//	/**
//	 * 打开弹出子画面
//	 * 
//	 * @return
//	 */
//	public String openPopup() {
//		return SUCCESS;
//	}
//
//	/**
//	 * 上传并解析文件
//	 * 
//	 * @return
//	 */
//	public String read() {
//		try {
//			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//			List<Map<String, Object>> list = binolssprm50BL.parsefile(upExcel, form, userInfo);
//			form.setReasonArr(null);
//			request.setAttribute("parseddata", list);
//			//session.put(CherryConstants.SESSION_UPLOAD_FILENAME, newFileFull);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			this.addActionError(getText("ESS00011"));
//			return CherryConstants.GLOBAL_ACCTION_RESULT;
//		}
//		return SUCCESS;
//	}
//
	@Override
	public BINOLSSPRM50_Form getModel() {
		return form;
	}
//
//	/**
//	 * @return the upExcel
//	 */
//	public File getUpExcel() {
//		return upExcel;
//	}
//
//	/**
//	 * @param upExcel
//	 *            the upExcel to set
//	 */
//	public void setUpExcel(File upExcel) {
//		this.upExcel = upExcel;
//	}
//
//	/**
//	 * @return the upExcelFileName
//	 */
//	public String getUpExcelFileName() {
//		return upExcelFileName;
//	}
//
//	/**
//	 * @param upExcelFileName
//	 *            the upExcelFileName to set
//	 */
//	public void setUpExcelFileName(String upExcelFileName) {
//		this.upExcelFileName = upExcelFileName;
//	}

}