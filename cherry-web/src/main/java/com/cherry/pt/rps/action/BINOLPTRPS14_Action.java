/*  
 * @(#)BINOLPTRPS14_Action.java     1.0 2011/05/31      
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
package com.cherry.pt.rps.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS14_BL;
import com.cherry.pt.rps.form.BINOLPTRPS14_Form;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings({ "unchecked", "serial" })
public class BINOLPTRPS14_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS14_Form> {

	private BINOLPTRPS14_Form form = new BINOLPTRPS14_Form();

    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLPTRPS14_BL")
	private BINOLPTRPS14_BL binolptrps14Bl;
	
	// 销售记录单据详细信息
	private Map getSaleRecordDetail;
	// 销售记录中商品详细信息
	private List getSaleRecordProductDetail;
	// 操作员
	private Map getEmployeeName;
	// 支付明细
	private List getPayTypeDetail;

	public List getGetPayTypeDetail() {
		return getPayTypeDetail;
	}

	public void setGetPayTypeDetail(List getPayTypeDetail) {
		this.getPayTypeDetail = getPayTypeDetail;
	}

	public Map getGetEmployeeName() {
		return getEmployeeName;
	}

	public void setGetEmployeeName(Map getEmployeeName) {
		this.getEmployeeName = getEmployeeName;
	}

	public Map getGetSaleRecordDetail() {
		return getSaleRecordDetail;
	}

	public void setGetSaleRecordDetail(Map getSaleRecordDetail) {
		this.getSaleRecordDetail = getSaleRecordDetail;
	}

	public List getGetSaleRecordProductDetail() {
		return getSaleRecordProductDetail;
	}

	public void setGetSaleRecordProductDetail(List getSaleRecordProductDetail) {
		this.getSaleRecordProductDetail = getSaleRecordProductDetail;
	}

	@Override
	public BINOLPTRPS14_Form getModel() {
		return form;
	}

	public String init() throws Exception {
		// 取得map
		Map<String, Object> map = this.getMap();
		// 获取销售记录住单据详细信息
		getSaleRecordDetail = binolptrps14Bl.getSaleRecordDetail(map);
		if (getSaleRecordDetail != null) {
			map.put("saleRecordId", getSaleRecordDetail.get("saleRecordId"));
			// 获取销售记录商品详细信息
			getSaleRecordProductDetail = binolptrps14Bl
					.getSaleRecordProductDetail(map);
			// 获取支付方式详细信息
			getPayTypeDetail = binolptrps14Bl.getPayTypeDetail(map);
			if (getSaleRecordProductDetail != null && getSaleRecordProductDetail.size() > 0) {
				//每条销售记录详细的操作员一致
				map.put("employeeCode",
						((Map<String, Object>) getSaleRecordProductDetail
								.get(0)).get("employeeCode"));
				// 获取操作员姓名
				getEmployeeName = binolptrps14Bl.getEmployeeName(map);
			}
		}
		
		String organizationInfoID = ConvertUtil.getString(getSaleRecordDetail.get("organizationInfoID"));
		String brandInfoID = ConvertUtil.getString(getSaleRecordDetail.get("brandInfoID"));
		//是否展示唯一码
		String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
		form.setSysConfigShowUniqueCode(configValue);
		
		return SUCCESS;
	}

	// 取得map
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("saleRecordId", form.getSaleRecordId());
		map.put("billCode", form.getBillCode());
		return map;
	}

}
