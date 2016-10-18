/*	
 * @(#)BINOLPTRPS25_Action.java     1.0.0 2011/10/17		
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;

/**
 * 产品在途库存详细Action
 * 
 * @author lipc
 * @version 1.0.0 2011.10.17
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS26_Action extends BaseAction {

	private static final long serialVersionUID = 5779285107926696629L;

	@Resource
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	private int prtDeliverId;

	/** 发货单概要信息 */
	private Map mainMap;
	
	/** 发货单详细信息 */
	List<Map<String,Object>> detailList;
	
	public int getPrtDeliverId() {
		return prtDeliverId;
	}

	public void setPrtDeliverId(int prtDeliverId) {
		this.prtDeliverId = prtDeliverId;
	}

	public Map getMainMap() {
		return mainMap;
	}

	public void setMainMap(Map mainMap) {
		this.mainMap = mainMap;
	}

	public List<Map<String, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
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
	public String init() throws Exception {
		//取得发货单概要信息 和详细信息
	    String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		mainMap = binOLSTCM03_BL.getProductDeliverMainData(prtDeliverId,language);
//		// 用户信息
//		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
//        String brandInfoId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
//        // 用于获取系统配置项【库存业务单据产品明细展示顺序】
//		Map<String,Object> otherParam = new HashMap<String,Object>();
//        otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
//        otherParam.put("BIN_BrandInfoID", brandInfoId);
		detailList = binOLSTCM03_BL.getProductDeliverDetailData(prtDeliverId,language,null);
		return SUCCESS;
	}
}
