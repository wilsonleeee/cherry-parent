/*  
 * @(#)BINOLCM26_Action.java     1.0.0 2011/12/09   
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.form.BINOLCM26_Form;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM26_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 工作流修改审核人Action
 * 
 * @author niushunjie
 * @version 1.0.0 2011.12.09
 */
public class BINOLCM26_Action extends BaseAction implements
		ModelDriven<BINOLCM26_Form> {

    private static final long serialVersionUID = 8446379840588162046L;

    private BINOLCM26_Form form = new BINOLCM26_Form();

	@Resource
	private BINOLCM26_IF binOLCM26_BL;

	@Override
	public BINOLCM26_Form getModel() {
		return form;
	}

    public String init() throws JSONException {
        //当前操作
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("workFlowId", form.getWorkFlowId());
        Map<String,Object> currentOperateInfo = binOLCM26_BL.getCurrentOperateInfo(map);
        String currentOperate = ConvertUtil.getString(currentOperateInfo.get("currentOperate"));
        String ruleType = ConvertUtil.getString(currentOperateInfo.get("ruleType"));      
        String currentOperateName = getText("OS.TaskName."+currentOperate);
        form.setCurrentOperateName(currentOperateName);
        form.setRuleType(ruleType);
        return SUCCESS;
    }
	
	/**
     * 根据身份类型取得身份信息
     * 
     * @return null
     */
    public String searchCodeByType() throws Exception {
        
        Map<String, Object> map = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 语言
        String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
        if(language != null) {
            map.put(CherryConstants.SESSION_LANGUAGE, language);
        }
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 品牌ID
        if(null == form.getBrandInfoId() || "".equals(form.getBrandInfoId())){
            map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        }else{
            map.put("brandInfoId", form.getBrandInfoId());
        }
        // 身份类型
        map.put("type", form.getAuditorType());
        // 根据身份类型取得身份信息
        List<Map<String, Object>> list = binOLCM26_BL.getCodeByType(map);
        
        ConvertUtil.setResponseByAjax(response, list);
        
        return null;
    }
	
    /**
     * 修改审核人
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("auditorType", form.getAuditorType());
        map.put("auditorID", form.getAuditorID());
        map.put("workFlowId", form.getWorkFlowId());
        map.put("bussinessType", form.getBussinessType());
        map.put("UserInfo", userInfo);
        map.put("ruleType", form.getRuleType());
        map.put("privilegeFlag", form.getPrivilegeFlag());
        try{
            binOLCM26_BL.tran_updateAuditor(map);
        }catch(Exception e){
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }
        }
        return SUCCESS;
    }
}
