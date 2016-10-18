/*	
 * @(#)BINOLSSCM06_BL     1.0 2011/11/21		
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
import com.cherry.ss.common.bl.BINOLSSCM06_BL;
import com.cherry.ss.common.form.BINOLSSCM06_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销产品发货单弹出table共通Action
 * @author niushunjie
 *
 */
public class BINOLSSCM06_Action extends BaseAction implements ModelDriven<BINOLSSCM06_Form>{	

    private static final long serialVersionUID = 8744066279654756557L;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLSSCM06_BL")
	private BINOLSSCM06_BL binOLSSCM06_BL;

	/** 参数FORM */
    private BINOLSSCM06_Form form = new BINOLSSCM06_Form();
	
    @Override
    public BINOLSSCM06_Form getModel() {
        return form;
    }

    /**
     * 取得发货单List
     * @return
     * @throws Exception 
     */
    public String getDeliver() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = this.getSessionInfo();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);

        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("T1.DeliverReceiveNoIF");
        filterList.add("B.NameForeign");
        filterList.add("G.NameForeign");
        filterList.add("B.DepartCode");
        filterList.add("B.DepartName");
        filterList.add("G.DepartName");
        filterList.add("G.DepartCode");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
       
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        
        //测试类型
        String testType = binOLCM00_BL.getDepartTestType(request.getParameter("organizationId"));
        map.put("testType", testType);
        
        int count = binOLSSCM06_BL.getDeliverCount(map);
        if(count>0){
            form.setDeliverList(binOLSSCM06_BL.getDeliverList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLSSCM06_1";
    }
    
    /**
     * 取得发货单详细List
     * @return
     * @throws Exception 
     */
    public void getDeliverDetail() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_PromotionDeliverID", form.getDeliverId());
        map.put("depotInfoId", form.getDepotInfoId());
        map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicDepotInfoId()));
        List<Map<String,Object>> list = binOLSSCM06_BL.getDeliverDetailList(map);

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
