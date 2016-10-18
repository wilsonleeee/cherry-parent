/*  
 * @(#)BINOLSTBIL14_BL.java     1.0 2012/7/24      
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
package com.cherry.st.bil.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.form.BINOLSTBIL20_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL20_IF;
import com.cherry.st.bil.service.BINOLSTBIL14_Service;
import com.cherry.st.bil.service.BINOLSTBIL20_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.service.BINOLSTCM13_Service;

/**
 * 
 * 销售退货申请单明细一览BL
 * @author nanjunbo
 * @version 1.0 2016.08.31
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL20_BL extends SsBaseBussinessLogic implements BINOLSTBIL20_IF{
	
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTCM13_Service")
    private BINOLSTCM13_Service binOLSTCM13_Service;
    
    @Resource(name="binOLSTBIL20_Service")
	private BINOLSTBIL20_Service binOLSTBIL20_Service;
	
	@Override
	public void tran_doaction(BINOLSTBIL20_Form form, UserInfo userInfo) throws Exception {
        if (CherryConstants.OPERATE_SA_AUDIT.equals(form.getOperateType())) {
			// 审核模式，推动工作流 【同意】【废弃】
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
    		pramMap.put("BIN_EmployeeIDAudit", userInfo.getBIN_EmployeeID());
			pramMap.put("CurrentUnit", "BINOLSTBIL20");
			pramMap.put("SaleReturnRequestForm", form);
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("BrandCode", userInfo.getBrandCode());
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//退库部门编号
            }
			pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
			pramMap.put("OpComments", form.getOpComments());
			pramMap.put("BIN_OrganizationID", form.getOrganizationID());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}

	/**
	 * 取得销售退货申请主单信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> searchSaleRerurnRequestInfo(Map<String, Object> map) {
		// 取得盘点单信息
		return binOLSTBIL20_Service.searchSaleRerurnRequestInfo(map);
	}

	/**
	 * 取得销售退货单明细List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchSaleRerurnRequestDetailList(Map<String, Object> map) {

		return  binOLSTBIL20_Service.searchSaleRerurnRequestDetailList(map);
	}
	
	/**
	 * 获取支付方式详细信息
	 * @param map
	 * @return list
	 */
	@Override
	public List<Map<String,Object>> getPayTypeDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTBIL20_Service.getPayTypeDetail(map);
	}
}