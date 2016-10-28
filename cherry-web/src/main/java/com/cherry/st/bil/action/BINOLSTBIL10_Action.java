/*
 * @(#)BINOLSTBIL10_Action.java     1.0 2010/11/05
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
package com.cherry.st.bil.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL10_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL10_IF;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点单明细Action
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.11.05
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL10_Action extends BaseAction
		implements ModelDriven<BINOLSTBIL10_Form> {

	private static final long serialVersionUID = 657072601835066344L;

    @Resource
    private BINOLCM19_IF binOLCM19_BL;
	
    @Resource
    private BINOLCM20_IF binOLCM20_BL;
	
	@Resource
	private BINOLSTBIL10_IF binOLSTBIL10_BL;
	
    /** 共通BL */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
			
	/** 参数FORM */
	private BINOLSTBIL10_Form form = new BINOLSTBIL10_Form();
	
	/** 盘点单信息 */
	private Map takingInfo;
	
	/** 盘点单明细List */
	private List takingDetailList;
	
	/** 汇总信息 */
    private Map<String, Object> sumInfo;
	
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	@Override
	public BINOLSTBIL10_Form getModel() {
		return form;
	}
	
	public Map getTakingInfo() {
		return takingInfo;
	}

	public void setTakingInfo(Map takingInfo) {
		this.takingInfo = takingInfo;
	}

	public List getTakingDetailList() {
		return takingDetailList;
	}

	public void setTakingDetailList(List takingDetailList) {
		this.takingDetailList = takingDetailList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init(){
	    int stockTakingId = 0;
	    //判断是top页打开任务
        if(null == form.getStockTakingId() || "".equals(form.getStockTakingId())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            stockTakingId = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setStockTakingId(billID);
        }else{
            stockTakingId = CherryUtil.string2int(form.getStockTakingId());
        }
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        form.setBrandInfoId(brandInfoId);
	    
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 产品盘点ID
		map.put("billId", stockTakingId);
		// 盈亏
		String profitKbn = form.getProfitKbn();
		map.put("profitKbn", profitKbn);
		if(CherryChecker.isNullOrEmpty(profitKbn)){
			// 无盈亏参数时，盘点单明细按商品盘点的商品排序字段
	        String detailOrderBy = binOLCM14_BL.getConfigValue("1092", organizationId, brandInfoId);
	        map.put("detailOrderBy", detailOrderBy);
		}
		// 盘点单信息
		takingInfo = binOLSTBIL10_BL.searchTakingInfo(map);
		// 盘点单明细List
		takingDetailList = binOLSTBIL10_BL.searchTakingDetailList(map);
		//汇总信息sumInfo
		sumInfo = binOLSTBIL10_BL.getSumInfo(map);

        //实盘数量是否允许负号
        String allowNegativeFlag = binOLCM14_BL.getConfigValue("1388",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
        form.setAllowNegativeFlag(allowNegativeFlag);

		//工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(takingInfo.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,takingInfo);
        
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);
		
		return SUCCESS;
	}
	
	/**
     * 目前该明细画面有四种展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 91：工作流审核模式
     * 92：工作流编辑模式
     * 
     * @param workFlowID 工作流ID
     * @param mainData 主单数据
     * @return
     */
    private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
        //查看明细模式  按钮有【关闭】
        String ret="1";
         if(null==workFlowID||"".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                 //按钮有【保存】【提交】【删除】【关闭】
                 ret="2";
             }
         }else{
             // 用户信息
             UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
             //取得当前可执行的action
             ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
             if (adArr != null && adArr.length > 0) {
                 // 如果存在可执行action，说明工作流尚未结束
                 // 取得当前的业务操作
                 String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
                 if (CherryConstants.OPERATE_CA_AUDIT.equals(currentOperation)) {
                     //盘点单审核模式   按钮有【通过】【退回】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 } else if (CherryConstants.OPERATE_CA_EDIT.equals(currentOperation)) {
                     //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 }else if (CherryConstants.OPERATE_CA_AUDIT2.equals(currentOperation)) {
                     //盘点单二审模式   按钮有【通过】【退回】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 } 
             }
         }       
         return ret;
    }
	
    /**
     * 非工作流中的保存订单
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL10_BL.tran_save(form, userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
    }
    
    /**
     * 非工作流中的删除订单
     * @return
     * @throws Exception 
     */
    public String delete() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL10_BL.tran_delete(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
    }
    
    /**
     * 非工作流中的提交订单
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL10_BL.tran_submit(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
    }
    
    /**
     * 工作流中的各种动作入口方法
     * @return
     * @throws Exception
     */
    public String doaction() throws Exception{
        try{
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            form.setEntryID(entryID);
            form.setActionID(actionID);
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTBIL10_BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }
            throw e;
        }
    }
	
    /**
     * 通过Ajax取得指定仓库的库存
     * @throws Exception
     */
    public void getStockCountByAjax() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductVendorID", request.getParameter("productVendorId"));
        map.put("BIN_DepotInfoID", request.getParameter("depotInfoId"));
        map.put("BIN_LogicInventoryInfoID", request.getParameter("logicDepotsInfoId"));
        map.put("FrozenFlag", "1");
        int stockCount = binOLCM20_BL.getProductStock(map);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("currentIndex", request.getParameter("currentIndex"));
        resultMap.put("Quantity", stockCount);
        resultMap.put("hasproductflag", 1);
        resultList.add(resultMap);
        ConvertUtil.setResponseByAjax(response, resultList);
    }
}
