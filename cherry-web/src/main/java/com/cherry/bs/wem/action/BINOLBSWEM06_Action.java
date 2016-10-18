/*  
 * @(#)BINOLBSWEM04_Action.java     1.0 2015/08/18      
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
package com.cherry.bs.wem.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.form.BINOLBSWEM06_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM06_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 利润重新分摊
 * 
 * @author sk
 * @version 1.0 2015/10/15
 */
public class BINOLBSWEM06_Action extends BaseAction implements ModelDriven<BINOLBSWEM06_Form> {
	
	private static final long serialVersionUID = 1158366407329340456L;
	BINOLBSWEM06_Form form = new BINOLBSWEM06_Form();
	
	@Resource(name="binOLBSWEM06_BL")
	private BINOLBSWEM06_IF binOLBSWEM06_BL;
	
	public String init() {
		return SUCCESS;
	}
	public String search(){
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		ConvertUtil.setForm(form, map);
		map.put("employeeCode", form.getEmployeeCode());
		map.put("employeeName", form.getEmployeeName());
		map.put("billCode", form.getBillCode());
		map.put("channel", form.getChannel());
		map.put("saleType", form.getSaleType());
		map.put("startDate", form.getStartDate());
		map.put("endDate", form.getEndDate());
		map.put("rebateFlag", form.getRebateFlag());
		map = CherryUtil.removeEmptyVal(map);
		int count = binOLBSWEM06_BL.getSalListCount(map);
		if(count>0){
			form.setITotalRecords(count);
			form.setITotalDisplayRecords(count);
			form.setSalList(binOLBSWEM06_BL.search(map));
		}
		return SUCCESS;
	}
	public String profitRebateReset() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		if(form.getSaleRecordCodeList()!=null && form.getSaleRecordCodeList()!=""){
			String json = form.getSaleRecordCodeList().toString();
			List<Map<String, Object>> salerecordCodeList = CherryUtil.json2ArryList(json);
			if(salerecordCodeList.size()>0){
				for(Map<String, Object> m : salerecordCodeList){
					m.putAll(map);
				}
				try {
					binOLBSWEM06_BL.delRebateDivide(salerecordCodeList);
					binOLBSWEM06_BL.profitRebateReset(salerecordCodeList);
					this.addActionMessage(getText("WEM00004"));
				} catch (Exception e) {
					this.addActionError(e.getMessage());
					e.printStackTrace();
					this.addActionMessage(getText("WEM00003"));
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	@Override
	public BINOLBSWEM06_Form getModel() {
		return form;
	}
	
}
