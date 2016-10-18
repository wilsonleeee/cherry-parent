/*	
 * @(#)BINOLSSCM10_Action     1.0 2013/09/02		
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
package com.cherry.ss.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.form.BINOLSSCM10_Form;
import com.cherry.ss.common.interfaces.BINOLSSCM10_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品入库单弹出table共通Action
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public class BINOLSSCM10_Action extends BaseAction implements ModelDriven<BINOLSSCM10_Form>{	

    private static final long serialVersionUID = 1280434156364996657L;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLSSCM10_BL")
	private BINOLSSCM10_IF binOLSSCM10_BL;

	/** 参数FORM */
    private BINOLSSCM10_Form form = new BINOLSSCM10_Form();
	
    @Override
    public BINOLSSCM10_Form getModel() {
        return form;
    }

    /**
     * 取得入库单List
     * @return
     * @throws Exception 
     */
    public String getInDepot() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = this.getSessionInfo();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);

        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("T1.BillNoIF");
        filterList.add("B.NameForeign");
        filterList.add("B.DepartCode");
        filterList.add("B.DepartName");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
       
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        
        //测试类型
        String testType = binOLCM00_BL.getDepartTestType(request.getParameter("organizationId"));
        map.put("testType", testType);
        
        int count = binOLSSCM10_BL.getInDepotCount(map);
        if(count>0){
            form.setInDepotList(binOLSSCM10_BL.getInDepotList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSSCM10_1";
    }
    
    /**
     * 取得入库单详细List
     * @return
     * @throws Exception 
     */
    public void getInDepotDetail() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_PrmInDepotID", form.getInDepotID());
        map.put("depotInfoId", form.getDepotInfoId());
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        List<Map<String,Object>> list = binOLSSCM10_BL.getInDepotDetailList(map);

        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 取得session的信息
     * @param map
     * @throws Exception 
     */
    private Map<String,Object> getSessionInfo() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
        // 取得所属组织
        map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        String brandInfoID = (String)(userInfo.getCurrentBrandInfoID());
        if (brandInfoID!=null && !brandInfoID.equals("-9999")){
            // 取得所属品牌
            map.put("brandInfoID", brandInfoID);
        }
        // 取得语言区分
        map.put("language", userInfo.getLanguage());
        // 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 操作类型
        map.put("operationType", "1");
        // 业务类型
        map.put("businessType", "1");
        return map;
    }
}