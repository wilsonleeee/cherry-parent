/*
 * 
 * @(#)BINOLSSPRM51_Action.java     1.0 2010/10/29		
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.ControlOrganization;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM51_BL;
import com.cherry.ss.prm.form.BINOLSSPRM51_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品收货
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM51_Action extends BaseAction implements ModelDriven<BINOLSSPRM51_Form>{
//	
//	private static final long serialVersionUID = 1L;
//	
	private BINOLSSPRM51_Form form = new BINOLSSPRM51_Form();
//
//	@Resource
//	private BINOLCM01_BL binolcm01BL;
//	
//	@Resource
//	private BINOLSSPRM51_BL binolssprm51BL;	
//
//	/**
//     * 画面初始化
//     * @return
//     */
//	public String init() {
//		try {
////			// 取得用户信息
////			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
////
////			// 从部门数据权限表中取得能操作的部门
////			List<Map<String, String>> list = binolcm01BL.getControlOrgListPrivilege(userInfo.getBIN_UserID(),CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,userInfo.getLanguage(),"1");
////			form.setInOrganizationList(list);
////
////			// 取得用户所属的第一个部门的仓库
////			if (list.size() > 0) {
////				form.setInDepotList(getDepotList(list.get(0).get("OrganizationID"), userInfo.getLanguage()));
////			}
//		} catch (Exception ex) {
//			this.addActionError(getText("ECM00036"));
//		}
//		return SUCCESS;
//	}
//    
//    /**
//     * 根据指定的部门ID和语言信息取得仓库信息
//     * @param organizationID
//     * @param language
//     * @return
//     */
//    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
//    	List<Map<String, Object>> ret = binolcm01BL.getDepotList(organizationID, language);
//    	return ret;
//    }
//      
//	/**
//	 * 改变部门下拉框后
//	 * 通过Ajax取得指定部门所拥有的仓库
//	 * @throws Exception 
//	 */
//	public void getDepotByAjax() throws Exception{
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		String organizationid = request.getParameter("organizationid");		
//		List resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
//		ConvertUtil.setResponseByAjax(response, resultTreeList);
//	}
//    
//
//    /**
//     * 打开弹出子画面"选择发货单据"
//     * @return
//     */
//    public String openPopup(){
//    	//收货部门
//    	String organizationID = form.getInOrganizationId();    	
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO); 
//    	binolcm01BL.completeUserInfo(userInfo, organizationID, "BINOLSSPRM51");
//    	//取得发货单
//    	List list =binolssprm51BL.getDeliverDataList(organizationID,userInfo);
//    	if(list==null||list.size()==0){
//    		this.addActionError(getText("ESS00016"));
//    	}
//    	form.setDeliverDataList(list);
//    	return SUCCESS;
//    }
//    /**
//     * 取得发货单的详细
//     * @throws Exception
//     */
//    public String getDeliverDetail() throws Exception{ 
//       try {
//    	   UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//        	List list = binolssprm51BL.getDeliverDataDetailList(form.getDeliverchkbox(),userInfo);
//        	if(list==null||list.size()==0){
//        		throw new Exception();
//        	}
//        	form.setDeliverDataDetailList(list);
//             
//        } catch(Exception e) {   
//        	this.addActionError(getText("ESS00015"));
//            e.printStackTrace();   
//            return CherryConstants.GLOBAL_ACCTION_RESULT;
//        }   
//        return SUCCESS;   
//    }
//
//    /**
//     * 主画面点击确定按钮，进行收货
//     * @return
//     * @throws Exception 
//     */
//    public String submit() throws Exception{
//    	try{
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//    	binolcm01BL.completeUserInfo(userInfo, form.getInOrganizationId(), "BINOLSSPRM51");
//    	binolssprm51BL.tran_receiving(form,userInfo);
//    	form.clear();
//    	//重新初始化画面
//    	this.init();    	
//    	this.addActionMessage(getText("ICM00002"));  	
//        return SUCCESS;    
//    	} catch(Exception ex) { 
//    		if (ex instanceof CherryException) {
//				CherryException temp = (CherryException) ex;
//				this.addActionError(temp.getErrMessage());
//			} else {
//				throw ex;
//			}			          
//            return CherryConstants.GLOBAL_ACCTION_RESULT;
//        }
//    }
//   
//    /**
//     * 使用工作流收货时，初始化弹出画面
//     * @return
//     */
//   
//	public String jbpmInit(){
//    	//部门ID
//		String organizationId = request.getParameter("parameter1");
//		//发货单ID
//		String deliverID =	request.getParameter(CherryConstants.JBPM_MAIN_ID);		
//		//任务实例ID
//		String taskInstanceID = request.getParameter("taskInstanceID");
//		
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO); 
//		userInfo.setCurrentUnit("BINOLSSPRM51");
//
//		List listDetail = binolssprm51BL.getDeliverDataDetailList(new String[]{deliverID+"_"+taskInstanceID},userInfo);
//    	form.setDeliverDataDetailList(listDetail);
//    	
//    	//取得用户所管辖的部门
//    	String inOrganizationIdHid="";
//    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//    	for(Iterator<ControlOrganization> it=userInfo.getControlOrganizationList().iterator();it.hasNext();){
//    		ControlOrganization temp = it.next();
//    		if(String.valueOf(temp.getBIN_OrganizationID()).equals(organizationId)){
//    			Map<String, String> ret = new HashMap<String, String>();
//    			ret.put("OrganizationID", organizationId);
//    			ret.put("DepartName", temp.getDepartName());
//    			inOrganizationIdHid=temp.getDepartName();
//    			list.add(ret);
//    			break;
//    		}
//    	}
//    	form.setInOrganizationIdHid(organizationId);
//    	form.setInOrganizationNameHid(inOrganizationIdHid);
//    	form.setInOrganizationList(list);
//
//    	//取得部门的仓库
//    	form.setInDepotList(getDepotList(organizationId,userInfo.getLanguage()));
//        return SUCCESS;
//    }
//    /**
//     * 点击确定按钮，进行收货
//     * @return
//     * @throws Exception 
//     */
//    public String jbpmSubmit() throws Exception{
//    	try{
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//    	binolcm01BL.completeUserInfo(userInfo, form.getInOrganizationId(), "BINOLSSPRM51");
//    	binolssprm51BL.tran_receiving(form,userInfo);
//    	form.clear();
//    	//重新初始化画面
//    	//this.init();    	
//    	this.addActionMessage(getText("ICM00002"));  	
//        return  CherryConstants.GLOBAL_ACCTION_RESULT;    
//    	} catch(Exception ex) { 
//    		if (ex instanceof CherryException) {
//				CherryException temp = (CherryException) ex;
//				this.addActionError(temp.getErrMessage());
//			} else {
//				throw ex;
//			}			          
//            return CherryConstants.GLOBAL_ACCTION_RESULT;
//        }
//    }
	@Override
	public BINOLSSPRM51_Form getModel() {
		return form;
	}

}