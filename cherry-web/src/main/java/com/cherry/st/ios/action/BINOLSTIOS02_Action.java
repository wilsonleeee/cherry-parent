
/*  
 * @(#)BINOLSTIOS02_Action.java    1.0 2011-8-31     
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
package com.cherry.st.ios.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.ios.form.BINOLSTIOS02_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS02_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTIOS02_Action extends BaseAction implements
		ModelDriven<BINOLSTIOS02_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLSTIOS02_Form form = new BINOLSTIOS02_Form();
	
	//逻辑仓库list
	private List<Map<String,Object>> logicInventoryList = null;
	
	//初始化实体仓库
	private List<Map<String,Object>> depotList = null;
	
	//用户所属部门ID
	private int organizationId;
	
	//用户所属部门名称
	private String departName;
	
	List<Map<String,Object>> logicList =null;
	
	//取逻辑仓库和实体仓库的共同
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTIOS02_BL")
	private BINOLSTIOS02_IF binOLSTIOS02_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_BL bINOLCM20_BL;
	
	@Resource(name="binOLSTCM04_BL")
	private BINOLSTCM04_IF binOLSTCM04_BL;
	
	@Resource(name="binOLSTIOS03_BL")
	private BINOLSTIOS03_IF binOLSTIOS03_BL;
	
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
	private String brandInfoId;
	
	/**
	 * 页面期初加载
	 * @throws Exception 
	 * 
	 * */
	public String init() throws Exception{
		try{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		userInfo.setCurrentUnit("BINOLSTIOS02");
		//登录用户的所属品牌
		brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		String organizationInfoID = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		organizationId = userInfo.getBIN_OrganizationID();
		//语言
		String language = userInfo.getLanguage();
		Map<String,Object> pram1 =  new HashMap<String,Object>();
		if(organizationId != 0){
			pram1.put("BIN_OrganizationID", organizationId);
		    departName = ConvertUtil.getString(binOLSTIOS03_BL.getDepart(pram1));
			depotList = binOLCM18_BL.getDepotsByDepartID(String.valueOf(organizationId), language);
		}
		
        //支持终端移库标志
        boolean flag = binOLCM14_BL.isConfigOpen("1059", organizationInfoID, brandInfoId);
        form.setCounterShiftFlag(String.valueOf(flag));
		
		//调用共通获取逻辑仓库
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationId);
		logicInventoryList = getLogicList(param);
		form.setBrandInfoId(brandInfoId);
		} catch (Exception ex) {			
			this.addActionError(getText("ECM00036"));
		}
		return SUCCESS;
	}
	
	public void getDapotInfo()throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		map.put("departCode", form.getDepartCode());
		String organizationId = String.valueOf(binOLSTIOS02_BL.getOrganizationId(map));
		//语言
		String language = userInfo.getLanguage();
		List<Map<String, Object>> dapotList = binOLCM18_BL.getDepotsByDepartID(organizationId,language);
		ConvertUtil.setResponseByAjax(response, dapotList);
	}
	
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
    	try{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
            }
        }
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_MV);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "1");//产品
        paramMap.put("language", language);
        logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
    	} catch (Exception e) {	
    		this.addActionError(getText("ECM00036"));
		}
        return logicList;
    }
    
    /**
     * 取得逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationId = form.getDepartId();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationId);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
    }
	
	public void getPrtVenIdAndStock() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		
		//实体仓库
		map.put("BIN_DepotInfoID", form.getDepotInfoId());
		String fromlogicInventId = (String)form.getFromLogicInventoryInfoId();
		if(null== fromlogicInventId || "".equals(fromlogicInventId)){
			fromlogicInventId="0";
		}
		//逻辑仓库
		map.put("BIN_LogicInventoryInfoID",fromlogicInventId);
		//是否取冻结
		map.put("FrozenFlag", '1');
		
		//厂商ID
		String[] prtIdArr = form.getProductVendorIdArr();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(null != prtIdArr){
            for(String prtId : prtIdArr){
                map.put("BIN_ProductVendorID", prtId);
                int stock = bINOLCM20_BL.getProductStock(map);
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put(ProductConstants.PRT_VENDORID, prtId);
                temp.put("stock", stock);
                list.add(temp);
            }
        }
        ConvertUtil.setResponseByAjax(response, list);
		
	}
	
	/**
	 * 保存
	 * 
	 * */
	public String save() throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 作成者为当前用户
			map.put("createdBy", userInfo.getBIN_UserID());
			// 作成程序名为当前程序
			map.put("createPGM", "BINOLSTIOS02");
			// 更新者为当前用户
			map.put("updatedBy", userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put("updatePGM", "BINOLSTIOS02");
			map.put("depotInfoId", form.getDepotInfoId());
			map.put("organizationId", form.getDepartId());
			userInfo.setCurrentOrganizationID(form.getDepartId());
			map.put("comments", form.getComments());
			map.put("fromLogicInventoryInfoId", form.getFromLogicInventoryInfoId());
			map.put("toLogicInventoryInfoId", form.getToLogicInventoryInfoId());
			String[] productVendorIdArr = form.getProductVendorIdArr();
			String[] quantityArr = form.getQuantityArr();
			String[] commentsArr = form.getCommentsArr();
			String[] priceArr = form.getPriceArr();
			List<String[]> list = new ArrayList<String[]>();
			list.add(productVendorIdArr);
			list.add(quantityArr);
			list.add(commentsArr);
			list.add(priceArr);
			int billId = 0;
			billId = binOLSTIOS02_BL.tran_saveShiftRecord(map, list,userInfo);
			
			boolean flag = true;
			if(billId == 0){
				//抛出自定义异常：操作失败！
				throw new CherryException("ISS00005");
			}else{
				//语言
				String language = userInfo.getLanguage();
				//取得移库单概要信息
				Map<String,Object> mainMap = binOLSTCM04_BL.getProductShiftMainData(billId,language);
				//申明一个Map用来存放要返回的ActionMessage
				Map<String,Object> messageMap = new HashMap<String,Object>();
				//是否要显示工作流程图标志：设置为true
				messageMap.put("ShowWorkFlow",true);
				//工作流ID
				messageMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
				//消息：操作已成功！
				messageMap.put("MessageBody", getText("ICM00002"));
				//将messageMap转化成json格式字符串然后添加到ActionMessage中
				this.addActionMessage(JSONUtil.serialize(messageMap));
				//返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}
	
	/**
	 * 暂存
	 * 
	 * */
	public String saveTemp() throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 作成者为当前用户
			map.put("createdBy", userInfo.getBIN_UserID());
			// 作成程序名为当前程序
			map.put("createPGM", "BINOLSTIOS02");
			// 更新者为当前用户
			map.put("updatedBy", userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put("updatePGM", "BINOLSTIOS02");
			map.put("depotInfoId", form.getDepotInfoId());
			map.put("organizationId", form.getDepartId());
			userInfo.setCurrentOrganizationID(form.getDepartId());
			map.put("comments", form.getComments());
			map.put("fromLogicInventoryInfoId", form.getFromLogicInventoryInfoId());
			map.put("toLogicInventoryInfoId", form.getToLogicInventoryInfoId());
			String[] productVendorIdArr = form.getProductVendorIdArr();
			String[] quantityArr = form.getQuantityArr();
			String[] commentsArr = form.getCommentsArr();
			String[] priceArr = form.getPriceArr();
			List<String[]> list = new ArrayList<String[]>();
			list.add(productVendorIdArr);
			list.add(quantityArr);
			list.add(commentsArr);
			list.add(priceArr);
			int billId = 0;
			billId = binOLSTIOS02_BL.tran_saveShiftRecordTemp(map, list,userInfo);
			
			boolean flag = true;
			if(billId == 0){
				//抛出自定义异常：操作失败！
				throw new CherryException("ISS00005");
			}else{
				this.addActionMessage(getText("ICM00002"));  
				//返回MESSAGE共通页
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}
	
	public List<Map<String, Object>> getLogicList() {
		return logicList;
	}

	public void setLogicList(List<Map<String, Object>> logicList) {
		this.logicList = logicList;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}
	
	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}
	
	@Override
	public BINOLSTIOS02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getDepotList() {
		return depotList;
	}

	public void setDepotList(List<Map<String, Object>> depotList) {
		this.depotList = depotList;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}
}
