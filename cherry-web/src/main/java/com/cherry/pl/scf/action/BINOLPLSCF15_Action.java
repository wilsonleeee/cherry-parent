/*
 * @(#)BINOLPLSCF15_Action.java     1.0 2011/12/19
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

package com.cherry.pl.scf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.form.BINOLPLSCF15_Form;
import com.cherry.pl.scf.interfaces.BINOLPLSCF15_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 业务流程配置Action
 * 
 * @author niushunjie
 * @version 1.0 2011.12.19
 */
public class BINOLPLSCF15_Action extends BaseAction implements
        ModelDriven<BINOLPLSCF15_Form> {

    private static final long serialVersionUID = 1901329623751585647L;

    @Resource
    private CodeTable code;

    @Resource
    private BINOLCM05_BL binOLCM05_BL;

    @Resource
    private BINOLPLSCF15_IF binOLPLSCF15_BL;

    private BINOLPLSCF15_Form form = new BINOLPLSCF15_Form();

    public String init() {
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 语言
        String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
        Map<String, Object> map = new HashMap<String, Object>();
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());
        map.put(CherryConstants.SESSION_LANGUAGE, language);
        
        List<Map<String,Object>> brandInfoList = new ArrayList<Map<String,Object>>();
        // 所属品牌
        if (userInfo.getBIN_BrandInfoID() == -9999) {
            Map<String,Object> brandMap = new HashMap<String,Object>();
            // 品牌ID
            brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
            // 品牌名称
            brandMap.put("brandName", getText("PPL00006"));
            brandInfoList.add(brandMap);
            
            brandInfoList.addAll(binOLCM05_BL.getBrandInfoList(map));
        } else {
            Map<String, Object> brandMap = new HashMap<String, Object>();
            // 品牌ID
            brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
            // 品牌名称
            brandMap.put("brandName", userInfo.getBrandName());
            brandInfoList.add(brandMap);
        }
        form.setBrandInfoList(brandInfoList);

        //品牌取第一个
        String brandInfoId = ConvertUtil.getString(brandInfoList.get(0).get("brandInfoId"));
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));
        Map<String,Object> wfMap = new HashMap<String,Object>();
        wfMap.put("OrgCode", orgCode);
        wfMap.put("BrandCode", brandCode);
        List<Map<String, Object>> workflowInfoList = binOLPLSCF15_BL.getFlowList(wfMap);
        form.setWorkflowInfoList(workflowInfoList);
        
        return SUCCESS;
    }
    
    /**
     * 更换品牌，返回一览
     * @throws Exception 
     */
    public void changeBrand() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandInfoId = form.getBrandInfoId();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));
        Map<String,Object> wfMap = new HashMap<String,Object>();
        wfMap.put("OrgCode", orgCode);
        wfMap.put("BrandCode", brandCode);
        List<Map<String, Object>> workflowInfoList = binOLPLSCF15_BL.getFlowList(wfMap);
        ConvertUtil.setResponseByAjax(response, workflowInfoList);
    }

    @Override
    public BINOLPLSCF15_Form getModel() {
        return form;
    }

}
