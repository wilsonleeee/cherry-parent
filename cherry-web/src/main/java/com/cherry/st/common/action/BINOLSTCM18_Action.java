/*	
 * @(#)BINOLSTCM18_Action     1.0 2013/09/02		
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
package com.cherry.st.common.action;

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
import com.cherry.st.common.form.BINOLSTCM18_Form;
import com.cherry.st.common.interfaces.BINOLSTCM18_IF;
import com.cherry.st.common.service.BINOLSTCM18_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品入库单弹出table共通Action
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public class BINOLSTCM18_Action extends BaseAction implements ModelDriven<BINOLSTCM18_Form>{	

    private static final long serialVersionUID = 1280434156364996657L;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLSTCM18_BL")
	private BINOLSTCM18_IF binOLSTCM18_BL;
    
    @Resource(name="binOLSTCM18_Service")
    private BINOLSTCM18_Service binOLSTCM18_Service;

	/** 参数FORM */
    private BINOLSTCM18_Form form = new BINOLSTCM18_Form();
	
    @Override
    public BINOLSTCM18_Form getModel() {
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
        
        int count = binOLSTCM18_BL.getInDepotCount(map);
        if(count>0){
            form.setInDepotList(binOLSTCM18_BL.getInDepotList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSTCM18_1";
    }
    
    /**
     * 取得入库单详细List
     * @return
     * @throws Exception 
     */
    public void getInDepotDetail() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductInDepotID", form.getInDepotID());
        map.put("depotInfoId", form.getDepotInfoId());
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        // 业务日期
        String bussDate = binOLSTCM18_Service.getBussinessDate(map);
        map.put(CherryConstants.BUSINESS_DATE, bussDate);
        List<Map<String,Object>> list = binOLSTCM18_BL.getInDepotDetailList(map);

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