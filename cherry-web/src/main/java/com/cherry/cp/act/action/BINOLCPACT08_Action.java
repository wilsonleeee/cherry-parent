/*	
 * @(#)BINOLCPACT08_Action.java     1.0 @2013-07-16		
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
package com.cherry.cp.act.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.form.BINOLCPACT08_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT08_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 礼品领用详细一览Action
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT08_Action extends BaseAction implements
		ModelDriven<BINOLCPACT08_Form> {

	private static final long serialVersionUID = 7923159111288271440L;

	/** 参数FORM */
	private BINOLCPACT08_Form form = new BINOLCPACT08_Form();

	/** 接口 */
	@Resource(name = "binOLCPACT08_IF")
	private BINOLCPACT08_IF binOLCPACT08_IF;

	/** 礼品领用单据基本信息 */
	private Map getGiftDrawDetail;

	/** 礼品领用产品信息List */
	private List getGiftDrawPrtDetail;

	public String init() throws Exception {
		Map<String, Object> map = this.getMap();
		// 取得此礼品领用单据的基本信息
		getGiftDrawDetail = binOLCPACT08_IF.getGiftDrawDetail(map);
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【手机号码】字段加密
		if (!CherryChecker.isNullOrEmpty(getGiftDrawDetail.get("memberPhone"), true)) {
			String memberPhone = ConvertUtil.getString(getGiftDrawDetail.get("memberPhone"));
			getGiftDrawDetail.put("memberPhone", CherrySecret.decryptData(brandCode,memberPhone));
		}
		if (null != getGiftDrawDetail && getGiftDrawDetail.size() > 0) {
			map.put("giftDrawId", getGiftDrawDetail.get("giftDrawId"));
			getGiftDrawPrtDetail = binOLCPACT08_IF.getGiftDrawPrtDetail(map);
		}
		return "BINOLCPACT08";
	}

	private Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的所属品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// map.put("billNo", form.getBillNo());
		map.put("giftDrawId", form.getGiftDrawId());
		return map;
	}

	public Map getGetGiftDrawDetail() {
		return getGiftDrawDetail;
	}

	public void setGetGiftDrawDetail(Map getGiftDrawDetail) {
		this.getGiftDrawDetail = getGiftDrawDetail;
	}

	public List getGetGiftDrawPrtDetail() {
		return getGiftDrawPrtDetail;
	}

	public void setGetGiftDrawPrtDetail(List getGiftDrawPrtDetail) {
		this.getGiftDrawPrtDetail = getGiftDrawPrtDetail;
	}

	@Override
	public BINOLCPACT08_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
