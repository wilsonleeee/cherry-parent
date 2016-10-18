package com.cherry.st.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.form.StIndexForm;
import com.cherry.st.common.interfaces.BINOLSTCM10_IF;
import com.opensymphony.xwork2.ModelDriven;

/*  
 * @(#)StIndexAction.java    1.0 2011-8-15     
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
public class StIndexAction extends BaseAction implements ModelDriven<StIndexForm>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private BINOLCM19_IF binOLCM19_BL;
	@Resource
	private BINOLSTCM10_IF binOLSTCM10_BL;
	@Resource
	private BINOLCM01_BL binOLCM01_BL;
	@Resource
    private BINOLCM18_IF binOLCM18_BL;
	
	private List<Map<String,Object>> departList = new ArrayList<Map<String,Object>>();
	
	private StIndexForm form = new StIndexForm();
	
	public String initial(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

	public String getDepartInfo()throws Exception{
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> param = new HashMap<String, Object>();
			String businessType = form.getBusinessType();
			String inOutFlag = form.getInOutFlag();
			//实体仓库ID
			param.put("DepotID", Integer.parseInt(form.getDepotId().trim()));
			//业务类型
			param.put("BusinessType", businessType);
			//入出库区分
			param.put("InOutFlag", inOutFlag);
			//权限  操作类型
			param.put("operationType", "0");
			//权限 业务类型
			param.put("businessType", "1");
			//查找指定仓库能向哪些仓库订货/退库(不带权限控制)
			if(!(CherryConstants.OPERATE_OD.equals(businessType) && "IN".equals(inOutFlag)) 
			        && !(CherryConstants.OPERATE_RR.equals(businessType) && "OUT".equals(inOutFlag))){
			    //用户ID
			    param.put("BIN_UserID", userInfo.getBIN_UserID());
			}
			//组织ID
			param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			//品牌ID
			if (userInfo.getBIN_BrandInfoID() == -9999){
				param.put("BIN_BrandInfoID", form.getBrandInfoId());
			}else{
				param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			}
			// 画面查询条件
			if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
				param.put("inputString", form.getSSearch());
			}
			param.put("BIN_OrganizationID", form.getDepartId());
			// form参数设置到map中
			ConvertUtil.setForm(form, param);
			
			param.put("IDisplayLength", form.getIDisplayLength());
			Map<String, Object> resultMap = binOLSTCM10_BL.getDepartInfoByBusinessType(param);
			if(resultMap.isEmpty()){
				form.setITotalDisplayRecords(0);
				form.setITotalRecords(0);
				departList = new ArrayList<Map<String,Object>>();
			}else{
				form.setITotalDisplayRecords((Integer)resultMap.get("totalQuenlity"));
				form.setITotalRecords((Integer)resultMap.get("totalQuenlity"));
				departList = (List<Map<String, Object>>) resultMap.get("departList");
			}
			// form表单设置
			return "popDepartTableBusinessType_1";
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
	
	/**
	 * ajxa取得逻辑仓库
	 * 
	 * */
	public void getLogDepotByAjax() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> param = new HashMap<String, Object>();
		if(userInfo.getBIN_BrandInfoID() == -9999){
			param.put("BIN_BrandInfoID", form.getBrandInfoId());
		}else{
			param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		}
//		param.put("BusinessType", CherryConstants.OPERATE_CA);
		param.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_CA);
		param.put("ProductType", "1");
		param.put("language", userInfo.getLanguage());
		Map<String,Object> departInfo = binOLCM01_BL.getDepartmentInfoByID(form.getDepartId(), userInfo.getLanguage());
		param.put("Type", "4".equals(departInfo.get("Type"))? "1":"0");
//		List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(param);
		List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(param);
		ConvertUtil.setResponseByAjax(response, logicDepotsList);
	}
	
	
	@Override
	public StIndexForm getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Map<String, Object>> departList) {
		this.departList = departList;
	}

	
	
}
