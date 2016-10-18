/*
 * @(#)BINOLSSPRM34_Action.java     1.0 2010/11/24
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM34_BL;
import com.cherry.ss.prm.form.BINOLSSPRM34_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 发货单编辑Action
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.24
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM34_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM34_Form> {

	private static final long serialVersionUID = -5217785441024157457L;

	@Resource
	private BINOLSSPRM34_BL binOLSSPRM34BL;

	@Resource
	private BINOLCM01_BL binOLCM01BL;

	/** 参数FORM */
	private BINOLSSPRM34_Form form = new BINOLSSPRM34_Form();

	/** 发货单信息 */
	private Map deliverInfo;

	/** 发货单明细List */
	private List deliverDetailList;

	/** 发货仓库List */
	private List inventoryList;

	/** 收货部门List */
	private List receiveDepList;

	public BINOLSSPRM34_Form getModel() {
		return form;
	}
	
	public Map getDeliverInfo() {
		return deliverInfo;
	}

	public void setDeliverInfo(Map deliverInfo) {
		this.deliverInfo = deliverInfo;
	}

	public List getDeliverDetailList() {
		return deliverDetailList;
	}

	public void setDeliverDetailList(List deliverDetailList) {
		this.deliverDetailList = deliverDetailList;
	}

	public List getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List inventoryList) {
		this.inventoryList = inventoryList;
	}

	public List getReceiveDepList() {
		return receiveDepList;
	}

	public void setReceiveDepList(List receiveDepList) {
		this.receiveDepList = receiveDepList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() {
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 促销产品收发货ID
		map.put("deliverId", form.getDeliverId());
		// 发货单信息
		deliverInfo = binOLSSPRM34BL.searchDeliverInfo(map);
		// 发货单明细List
		deliverDetailList = binOLSSPRM34BL.searchDeliverDetailList(map);
//		// 取得用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
		if (deliverInfo != null && !deliverInfo.isEmpty()) {
			// 发货部门ID
			String deliverDepId = String.valueOf(deliverInfo
					.get("deliverDepId"));
			// 取得发货仓库List
			inventoryList = binOLCM01BL.getDepotList(deliverDepId, language);
//			// 取得收货部门List
//			receiveDepList = binOLCM01BL.getConDepartList(deliverDepId, String
//					.valueOf(userInfo.getBIN_UserID()), "1", language, "0");
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
//	public String save() throws Exception {
//		// 登陆用户信息
//		UserInfo userInfo = (UserInfo) session
//				.get(CherryConstants.SESSION_USERINFO);
//		Map<String, Object> map = (Map<String, Object>) Bean2Map
//				.toHashMap(form);
//		// 需要发货处理或者保存处理
//		if ("1".equals(form.getDeliverKbn()) || "1".equals(form.getSaveKbn())) {
//			try {
//				// 保存促销品发货单和明细信息
//				binOLSSPRM34BL.tran_saveDeliver(map, userInfo);
//			} catch (CherryException e) {
//				this.addActionError(e.getErrMessage());
//				return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
//			}
//		}
//		// 处理成功
//		this.addActionMessage(getText("ICM00002"));
//		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
//	}
}
