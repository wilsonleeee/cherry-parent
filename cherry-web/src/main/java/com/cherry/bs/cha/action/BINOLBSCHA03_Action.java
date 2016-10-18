/*  
 * @(#)BINOLBSCHA03_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cha.form.BINOLBSCHA03_Form;
import com.cherry.bs.cha.interfaces.BINOLBSCHA03_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLBSCHA03_Action extends BaseAction implements ModelDriven<BINOLBSCHA03_Form>{
	@Resource
	private BINOLBSCHA03_IF binolbscha03if;
	
	private BINOLBSCHA03_Form form = new BINOLBSCHA03_Form();
	/**
	 * 
	 */
	private static final long serialVersionUID = -4167090629066699766L;
	
	private Map channelDetail;
	
	public void setChannelDetail(Map channelDetail) {
		this.channelDetail = channelDetail;
	}
	public Map getChannelDetail() {
		return channelDetail;
	}

	public String init()  throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("channelId", form.getChannelId());
		setChannelDetail(binolbscha03if.channelDetail(map));
		return SUCCESS;
	}

	public String save()  throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());

		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCHA03");	
		
		try{
			binolbscha03if.tran_updateChannel(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		
		//处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	public void validateSave() throws Exception {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
			.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put("channelName", form.getChannelName().trim());
			map.put("channelId", form.getChannelId());
			String count=binolbscha03if.getCount(map);
			if(count.equals("1")) {
				this.addFieldError("channelName",getText("ECM00032",new String[]{getText("PBS00050"),"20"}));
			}
		}
	
	@Override
	public BINOLBSCHA03_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}
