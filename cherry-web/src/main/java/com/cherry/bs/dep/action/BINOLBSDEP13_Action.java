/*
 * @(#)BINOLBSDEP13_Action.java     1.0 2011.2.10
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

package com.cherry.bs.dep.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP13_BL;
import com.cherry.bs.dep.form.BINOLBSDEP13_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 品牌删除画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP13_Action extends BaseAction implements ModelDriven<BINOLBSDEP13_Form> {

	private static final long serialVersionUID = -2297131008237549794L;
	
	/** 品牌删除画面Form */
	private BINOLBSDEP13_Form form = new BINOLBSDEP13_Form();
	
	/** 品牌删除画面BL */
	@Resource
	private BINOLBSDEP13_BL binOLBSDEP13_BL;
	
	/**
	 * 删除品牌处理
	 * 
	 * @return 删除完了画面 
	 */
	public String deleteBrandInfo() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP13");
		try {
			// 删除品牌处理
			binOLBSDEP13_BL.tran_deleteBrandInfo(map);
		} catch (Exception e) {
			// 删除失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());    
            }else{
                throw e;
            }    
		}
		return SUCCESS;
		
	}

	@Override
	public BINOLBSDEP13_Form getModel() {
		return form;
	}

}
