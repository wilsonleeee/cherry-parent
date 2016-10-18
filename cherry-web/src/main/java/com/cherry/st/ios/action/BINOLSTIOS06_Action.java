/*  
 * @(#)BINOLSTIOS06_Action.java     1.0 2012/11/27      
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
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.ios.form.BINOLSTIOS06_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS06_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品调拨Action
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
public class BINOLSTIOS06_Action  extends BaseAction implements ModelDriven<BINOLSTIOS06_Form>{

    private static final long serialVersionUID = -6533826836444476255L;

    /** 参数FORM */
    private BINOLSTIOS06_Form form = new BINOLSTIOS06_Form();
    
    /** 共通BL */
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
        
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_IF binOLCM20_BL;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLSTIOS06_BL")
    private BINOLSTIOS06_IF binOLSTIOS06_BL;
	
    @Override
    public BINOLSTIOS06_Form getModel() {
        return form;
    }

    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //登录用户的所属品牌
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        //所属组织
        int organizationInfoId = userInfo.getBIN_OrganizationInfoID();
        //语言
        String language = userInfo.getLanguage();
        //所属部门
        Map<String,Object> map =  new HashMap<String,Object>();
        map.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        
        //调入方
        int organizationId = userInfo.getBIN_OrganizationID();
        String departCodeName = getDepartCodeName(ConvertUtil.getString(organizationId),language);
        
        //调入方仓库
        List<Map<String,Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationId), language);
        form.setInDepotList(inDepotList);
        
        //取得调入方逻辑仓库
        Map<String,Object> logicParam = new HashMap<String,Object>();
        logicParam.put("organizationId", organizationId);
        try{
            List<Map<String,Object>> inLogicDepotList = getLogicList(logicParam);
            form.setInLogicDepotList(inLogicDepotList);
        }catch(Exception e){
            this.addActionError(getText("ECM00036"));
        }
        
        //初始化默认显示调入方
        Map<String,Object> initInfoMap = new HashMap<String,Object>();
        initInfoMap.put("defaultDepartID", organizationId);
        initInfoMap.put("defaultDepartCodeName", departCodeName);
        form.setInitInfoMap(initInfoMap);
        
        return SUCCESS;
    }
    
    /**
     * 保存产品调拨
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
            if (!validateForm()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationID(),"BINOLSTIOS06");
            binOLSTIOS06_BL.tran_save(form,userInfo);
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }catch(Exception ex){
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(String.valueOf(ex.getMessage()));
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 提交产品调拨单
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        try {
            if (!validateForm()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationID(),"BINOLSTIOS06");
            int billId = binOLSTIOS06_BL.tran_submit(form, userInfo);
            if(billId==0){
                throw new CherryException("ISS00005");
            }else{
                //语言
                String language = userInfo.getLanguage();
                //取得订货单概要信息 和详细信息
                Map<String,Object> mainMap = binOLSTCM16_BL.getProductAllocationMainData(billId,language);
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
        } catch (Exception ex) {
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(String.valueOf(ex.getMessage()));
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        // 语言类型
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 取得指定仓库中指定产品的库存数量
     * @throws Exception 
     */
    public void getStockCount() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        //实体仓库
        map.put("BIN_DepotInfoID", form.getInDepotID());
        String inLoginDepotId = form.getInLogicDepotID();
        if(null == inLoginDepotId || "".equals(inLoginDepotId)){
            inLoginDepotId = "0";
        }
        //逻辑仓库
        map.put("BIN_LogicInventoryInfoID", inLoginDepotId);
        //是否取冻结
        map.put("FrozenFlag", '1');
        //产品厂商ID
        String[] prtIdArr = form.getProductVendorIDArr();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(null != prtIdArr){
            for(String prtId : prtIdArr){
                map.put("BIN_ProductVendorID", prtId);
                int stock = binOLCM20_BL.getProductStock(map);
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put(ProductConstants.PRT_VENDORID, prtId);
                temp.put("stock", stock);
                list.add(temp);
            }
        }
        ConvertUtil.setResponseByAjax(response, list);
    }

    /**
     * 取得部门编号名称
     * @param organizationID
     * @param language
     * @return
     */
    private String getDepartCodeName(String organizationID,String language){
        String departCodeName = "";
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationID,language);
        if(null != departInfoMap){
            String departCode = ConvertUtil.getString(departInfoMap.get("DepartCode"));
            String departName = ConvertUtil.getString(departInfoMap.get("DepartName"));
            if(!"".equals(departCode)){
                departCodeName = "("+departCode+")"+departName;
            }else{
                departCodeName = departName;
            }
        }
        return departCodeName;
    }
    
    /**
     * 取得逻辑仓库
     * @param param
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> getLogicList(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String language = userInfo.getLanguage();
        String logicType = "0";
        String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_BG;
        
        //调用共通获取逻辑仓库
        Map<String,Object> paramMap =  new HashMap<String,Object>();
        paramMap.put("BIN_BrandInfoID", brandInfoId);
        paramMap.put("BusinessType", bussinessType);
        paramMap.put("Type", logicType);
        paramMap.put("ProductType", "1");
        paramMap.put("language", language);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
        return logicList;
    }
    
    /**
     * 验证提交的参数
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateForm() {
        boolean isCorrect = true;
        if(null == form.getInOrganizationID() || "".equals(form.getInOrganizationID())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00018")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInDepotID() || "".equals(form.getInDepotID())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00019")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInLogicDepotID() || "".equals(form.getInLogicDepotID())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00020")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getOutOrganizationID() || "".equals(form.getOutOrganizationID ())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length == 0){
            this.addActionError(getText("EST00009"));
            isCorrect = false;
            return isCorrect;
        }
        for(int i=0;i<form.getProductVendorIDArr().length;i++){
            if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i])){
                this.addActionError(getText("EST00008"));
                isCorrect = false;
                return isCorrect;
            }
        }
        return isCorrect;
    }
}