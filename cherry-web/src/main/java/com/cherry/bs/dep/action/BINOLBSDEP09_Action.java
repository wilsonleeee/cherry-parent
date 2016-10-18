/*
 * @(#)BINOLBSDEP09_Action.java     1.0 2011.2.10
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

import com.cherry.bs.dep.bl.BINOLBSDEP09_BL;
import com.cherry.bs.dep.form.BINOLBSDEP09_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 组织删除画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP09_Action extends BaseAction implements ModelDriven<BINOLBSDEP09_Form> {
	
	private static final long serialVersionUID = 9107632756466364989L;
	
	/** 组织删除画面Form */
	private BINOLBSDEP09_Form form = new BINOLBSDEP09_Form();
	
	/** 组织删除画面BL */
	@Resource
	private BINOLBSDEP09_BL binOLBSDEP09_BL;
	
	/**
	 * 删除组织处理
	 * 
	 * @return 删除完了画面 
	 */
	public String deleteOrganization() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP09");
		try {
			// 删除组织处理
			binOLBSDEP09_BL.tran_deleteOrganization(map);
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
	public BINOLBSDEP09_Form getModel() {
		return form;
	}

}
