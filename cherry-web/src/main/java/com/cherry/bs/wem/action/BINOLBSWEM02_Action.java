/*  
 * @(#)BINOLBSRES01_Action.java     1.0 2015/08/03      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.dto.OrderDiscountConf;
import com.cherry.bs.wem.form.BINOLBSWEM02_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM02_IF;
import com.cherry.bs.wem.interfaces.BINOLBSWEM04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 设置订货折扣
 * 
 * @author zhangbo
 * @version 1.0 2015/08/11
 */
public class BINOLBSWEM02_Action extends BaseAction implements ModelDriven<BINOLBSWEM02_Form> {
	
	private static final long serialVersionUID = -8300900314315505614L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM01_Action.class);

	private BINOLBSWEM02_Form form = new BINOLBSWEM02_Form();

	@Resource(name = "binOLBSWEM02_BL")
	private BINOLBSWEM02_IF binOLBSWEM02_BL;
	
	@Resource(name = "binOLBSWEM04_BL")
	private BINOLBSWEM04_IF binOLBSWEM04_BL;
	
	private List<OrderDiscountConf> orderDiscountConfList;
	
	private List<Map<String, Object>> discountConfList = new ArrayList<Map<String,Object>>();
	
    private List reginList;
		
	private List<Map<String, Object>> agentLevelList;
	
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	/**
	 * 初始化画面查询折扣
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String init() throws Exception {
		agentLevelList = codeTable.getCodesWithFilter("1000", "0|4");
		discountConfList = binOLBSWEM02_BL.getDiscountConfList();
		if(agentLevelList!=null && !agentLevelList.isEmpty() &&
				discountConfList!=null && !discountConfList.isEmpty()){
			for(Map<String, Object> am : agentLevelList){
				for(Map<String, Object> dm : discountConfList){
					if(am.get("CodeKey").equals(dm.get("departType"))){
						am.put("discountPer", dm.get("discountPer"));
					}
				}
			}
		}
		return SUCCESS;
	}
	

	/**
	 * 保存折扣率
	 */
	public void save() {
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		orderDiscountConfList = form.getOrderDiscountConfList();
		for(OrderDiscountConf o:orderDiscountConfList){
			Map<String, Object> map = new HashMap<String, Object>();
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLBSWEM02");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLBSWEM02");
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put("departType", o.getDepartType());
			map.put("discountPer", o.getDiscountPer());
			list.add(map);
		}
		binOLBSWEM02_BL.save(list);
	}
	

	@Override
	public BINOLBSWEM02_Form getModel() {
		return form;
	}

	public List<OrderDiscountConf> getOrderDiscountConfList() {
		return orderDiscountConfList;
	}

	public void setOrderDiscountConfList(
			List<OrderDiscountConf> orderDiscountConfList) {
		this.orderDiscountConfList = orderDiscountConfList;
	}

	public List<Map<String, Object>> getDiscountConfList() {
		return discountConfList;
	}

	public void setDiscountConfList(List<Map<String, Object>> discountConfList) {
		this.discountConfList = discountConfList;
	}

	public List<Map<String, Object>> getAgentLevelList() {
		return agentLevelList;
	}

	public void setAgentLevelList(List<Map<String, Object>> agentLevelList) {
		this.agentLevelList = agentLevelList;
	}
	
}
