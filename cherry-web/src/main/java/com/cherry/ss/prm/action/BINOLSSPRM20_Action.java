/*	
 * @(#)BINOLSSPRM20_Action.java     1.0 2010/12/03		
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
import com.cherry.ss.prm.bl.BINOLSSPRM20_BL;
import com.cherry.ss.prm.form.BINOLSSPRM20_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品调拨
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM20_Action extends BaseAction implements ModelDriven<BINOLSSPRM20_Form>{
//	
//	private static final long serialVersionUID = 1L;
//	
	private BINOLSSPRM20_Form form = new BINOLSSPRM20_Form();
//
//	@Resource
//	private BINOLCM01_BL binolcm01BL;
//	
//	@Resource
//	private BINOLSSPRM20_BL binolssprm20BL;
//
//	/**
//     * 画面初始化
//     * @return
//	 * @throws Exception 
//     */	
//    public String init() throws Exception{
//    	//取得用户信息
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);    	
//    	
//    	//取得用户所拥有的岗位--部门信息
//    	//List<Map<String, String>> list = binolcm01BL.getControlOrganizationList(userInfo);
//    	
//    	//TODO:后期放开，从部门数据权限表中取得能操作的部门
////    	List<Map<String, String>> list = binolcm01BL.getControlOrgListPrivilege(userInfo.getBIN_UserID(),CherryConstants.BUSINESS_TYPE1,CherryConstants.OPERATION_TYPE0,userInfo.getLanguage(),"2");
////    	if(list.size()>0){
//       	
////    	//取得用户能执行调出的部门
////		Map<String, Object> paramMap = new HashMap<String, Object>();
////		// 部门ID
////		paramMap.put("BIN_OrganizationID", list.get(0).get("OrganizationID"));
////		// 语言类型
////		paramMap.put("language", userInfo.getLanguage());
////		// 用户ID
////		paramMap.put("BIN_UserID", userInfo.getBIN_UserID());
////		// 业务类型
////		paramMap.put("BusinessType", CherryConstants.BUSINESS_TYPE1);
////		// 操作类型
////		paramMap.put("OperationType", CherryConstants.OPERATION_TYPE0);
////		// 是否包含本部门,为1排除，否则不排除
////		paramMap.put("excludeself", "0");
////		
////    	List<Map<String, Object>> list2 = binolcm01BL.getManagerDepartsByOrgIDP(paramMap);
////    	if(list2.size()<1){
////    		this.addActionError(getText("ECM00034"));
////    		return SUCCESS;
////    	}
////    	form.setOutOrganizationList(list);
//////    取得第一个调出部门的仓库
////    	form.setOutDepotList(getDepotList(String.valueOf(list.get(0).get("OrganizationID")),userInfo.getLanguage()));
////    	}
//        return SUCCESS;
//    }
//    /**
//     * 过滤，只有BA,BAS可进行调出确认
//     * @param list
//     * @return
//     */
//    private List<Map<String, String>> organizationCombListFilter(List<Map<String, String>> list){    	
//    	Iterator<Map<String, String>> it =  list.iterator();
//    	List<Map<String, String>> retList = new ArrayList<Map<String, String>>();    	
//    	while(it.hasNext()){
//    		Map<String, String> temp = it.next();
//    		String categoryCode = temp.get("CategoryCode");
//    		if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)||
//    				CherryConstants.CATRGORY_CODE_BAS.equals(categoryCode)){
//    			retList.add(temp);
//    		}    	
//    	}
//    	return retList ;
//    }
//    
//    /**
//     * 选择岗位后，取得调出部门
//     * @throws Exception
//     */
//    public void getOutDepart() throws Exception{
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		//String positionId = request.getParameter("positionId");
//		//binolcm01BL.completeUserInfoByPosition(userInfo, positionId,"BINOLSSPRM20");
//		
//		//取得用户能执行调入的部门
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		// 部门ID
//		paramMap.put("BIN_OrganizationID", userInfo.getCurrentOrganizationID());
//		// 语言类型
//		paramMap.put("language", userInfo.getLanguage());
//		// 用户ID
//		paramMap.put("BIN_UserID", userInfo.getBIN_UserID());
//		// 业务类型
//		paramMap.put("BusinessType", CherryConstants.BUSINESS_TYPE1);
//		// 操作类型
//		paramMap.put("OperationType", CherryConstants.OPERATION_TYPE0);
//		// 是否包含本部门,为1排除，否则不排除
//		paramMap.put("excludeself", "1");
//    	List<Map<String, Object>> list = binolcm01BL.getManagerDepartsByOrgIDP(paramMap);
//    	ConvertUtil.setResponseByAjax(response, list);
//    }
//    
//	/**
//	 * 通过Ajax取得指定部门所拥有的仓库
//	 * @throws Exception 
//	 */
//	public void getDepot() throws Exception{
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		String organizationid = request.getParameter("outOrganizationId");
//		binolcm01BL.completeUserInfo(userInfo, organizationid, null);
//		List resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
//		ConvertUtil.setResponseByAjax(response, resultTreeList);
//	}
//	
//	/**
//	 * 打开弹出框以选择调入申请单
//	 * @return
//	 */
//	public String openChildWindow(){
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//		String outOrganizationId = request.getParameter("outOrganizationId");
//		List list =binolssprm20BL.getAllOrder(outOrganizationId,userInfo);
//		if(list==null||list.size()<1){
//			this.addActionError(getText("ESS00028"));
//		}
//		form.setAllocationList(list);
//		return SUCCESS;
//	}
//	/**
//	 * 取得所选择的单据生成的详细列表画面
//	 * @return
//	 */	
//	public String getAllocationDetail(){
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//	       try {	     	  
//	        	List list = binolssprm20BL.getAllocationDetailList(form.getAllocationchkbox(),userInfo);
//	        	if(list==null||list.size()==0){
//	        		throw new Exception();
//	        	}
//	        	form.setAllocationDetailList(list);
//	             
//	        } catch(Exception e) {   
//	        	this.addActionError(getText("ESS00027"));
//	            e.printStackTrace();   
//	            return CherryConstants.GLOBAL_ACCTION_RESULT;
//	        }   
//	        return SUCCESS;  
//	}
//    /**
//     * 调出确认
//     * @return
//     * @throws JSONException
//     */
//    public String save() throws Exception{
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//    	//binolcm01BL.completeUserInfoByPosition(userInfo, form.getPositionId(), "BINOLSSPRM20");
//    	try {
//    		binolssprm20BL.tran_allocation(form,userInfo);  
//    	} catch(Exception e) { 
//    		if(e instanceof CherryException){
//    			this.addActionError(((CherryException)e).getErrMessage());
//    			e.printStackTrace();   
//    	        return CherryConstants.GLOBAL_ACCTION_RESULT;
//    		}
//          throw e;
//        }
//    	form.clear();
//    	this.addActionMessage(getText("ICM00002"));    	
//        return CherryConstants.GLOBAL_ACCTION_RESULT;        
//    }
//    
//    /**
//     * 使用工作流调出确认时，初始化弹出画面
//     * @return
//     */
//    public String jbpmInit(){
//
//		//申请单ID
//		String allocationID =	request.getParameter(CherryConstants.JBPM_MAIN_ID);		
//		//任务实例ID
//		String taskInstanceID = request.getParameter("taskInstanceID");
//		
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO); 
//		userInfo.setCurrentUnit("BINOLSSPRM20");
//
//		// 参数MAP
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 调拨记录Id
//		map.put("proAllocationId", allocationID);		
//		// 调拨记录信息
//		form.setReturnInfo(binolssprm20BL.getAllocationInfo(map));
//		
//		List listDetail = binolssprm20BL.getAllocationDetailList(new String[]{allocationID+"_"+taskInstanceID},userInfo);
//    	form.setAllocationDetailList(listDetail);
//    	
//    	Map<String ,Object> retMap = (Map<String ,Object>)listDetail.get(0);    	
//    	
//    	//调出部门
//    	String organizationId = String.valueOf(retMap.get("BIN_OrganizationIDAccept"));
//    	binolcm01BL.completeUserInfo(userInfo,organizationId,"BINOLSSPRM20");
//    	
//    	//取得用户所属管辖的部门
//    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//    	for(Iterator<ControlOrganization> it=userInfo.getControlOrganizationList().iterator();it.hasNext();){
//    		ControlOrganization temp = it.next();
//    		if(String.valueOf(temp.getBIN_OrganizationID()).equals(organizationId)){
//    			Map<String, String> ret = new HashMap<String, String>();
//    			ret.put("OrganizationID", organizationId);
//    			ret.put("DepartName", temp.getDepartName());
//    			list.add(ret);
//    			break;
//    		}
//    	}
//    	form.setOutOrganizationList(list);
//
//    	//取得部门的仓库
//    	form.setOutDepotList(getDepotList(organizationId,userInfo.getLanguage()));
//        return SUCCESS;
//    }
//    /**
//     * 点击确定按钮，进行调出确认
//     * @return
//     * @throws Exception 
//     */
//    public String jbpmSubmit() throws Exception{
//    	try{
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//    	binolssprm20BL.tran_allocationJBPM(form, userInfo);
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
	@Override
	public BINOLSSPRM20_Form getModel() {
		return form;
	}
}