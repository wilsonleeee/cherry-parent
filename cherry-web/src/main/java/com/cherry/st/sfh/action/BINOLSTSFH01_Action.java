/*  
 * @(#)BINOLSTSFH01_Action.java     1.0 2012/11/13      
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
package com.cherry.st.sfh.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.sfh.form.BINOLSTSFH01_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品订货Action
 * 
 * @author niushunjie
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH01_Action extends BaseAction implements ModelDriven<BINOLSTSFH01_Form>{

    private static final long serialVersionUID = -8909936511278556522L;

    private BINOLSTSFH01_Form form = new BINOLSTSFH01_Form();
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;

    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTSFH01_BL")
    private BINOLSTSFH01_IF binOLSTSFH01_BL;
    
    /**
     * 画面初始化
     * @return
     * @throws Exception 
     */	
    public String init() throws Exception{
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
        
        //业务日期
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizationInfoId", organizationInfoId);
        param.put("brandInfoId", brandInfoId);
        String bussinessDate = binOLSTSFH01_BL.getBusinessDate(param);
        form.setDate(bussinessDate);
        
        //订货方
        int organizationId = userInfo.getBIN_OrganizationID();
        String departCodeName = getDepartCodeName(ConvertUtil.getString(organizationId),language);
        
        //订货方仓库
        List<Map<String,Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationId), language);
        form.setInDepotList(inDepotList);
        int inDepotID = 0;
        if(null != inDepotList && inDepotList.size()>0){
            inDepotID = CherryUtil.obj2int(inDepotList.get(0).get("BIN_DepotInfoID"));
        }
        
        //取得订货方逻辑仓库
        Map<String,Object> logicParam = new HashMap<String,Object>();
        logicParam.put("organizationId", organizationId);
        try{
            List<Map<String,Object>> inLogicDepotList = getLogicList(logicParam);
            form.setInLogicDepotList(inLogicDepotList);
            
            //取得默认发货方
            Map<String,Object> outDepartParam = new HashMap<String,Object>();
            outDepartParam.put("inDepotID", inDepotID);
            Map<String,Object> outDepartInfo = getOutDepartInfo(outDepartParam);
            String outOrganizationId = ConvertUtil.getString(outDepartInfo.get("outOrganizationId"));
            String outDepartCodeName = ConvertUtil.getString(outDepartInfo.get("outDepartCodeName"));
            
            //初始化默认显示订货方、发货方
            Map<String,Object> initInfoMap = new HashMap<String,Object>();
            initInfoMap.put("defaultDepartID", organizationId);
            initInfoMap.put("defaultDepartCodeName", departCodeName);
            initInfoMap.put("defaultOutDepartID", outOrganizationId);
            initInfoMap.put("defaultOutDepartCodeName", outDepartCodeName);
            form.setInitInfoMap(initInfoMap);
        }catch(Exception e){
            this.addActionError(getText("ECM00036"));
        }
        
        return SUCCESS;
    }
       
    /**
     * 直接订货
     * @return
     * @throws Exception
     */
    public String submit() throws Exception{
        try {
            if (!validateForm("submit")) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH01");
            int billId = binOLSTSFH01_BL.tran_order(form, userInfo);
            if(billId==0){
                throw new CherryException("ISS00005");
            }else{
            	if(form.getFromPage() != null && "1".equals(form.getFromPage())) {
            		this.addActionMessage(getText("ICM00002"));
                    return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
            	} else {
            		//语言
                    String language = userInfo.getLanguage();
                    //取得订货单概要信息 和详细信息
                    Map<String,Object> mainMap = binOLSTCM02_BL.getProductOrderMainData(billId,language);
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
            }
        } catch (Exception ex) {
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 保存订货单
     * @return
     * @throws Exception
     */
    public String save() throws Exception{
        try{
            if (!validateForm("save")) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationId(),"BINOLSTSFH01");
            binOLSTSFH01_BL.tran_saveOrder(form,userInfo);
            if(form.getFromPage() != null && "1".equals(form.getFromPage())) {
        		this.addActionMessage(getText("ICM00002"));
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
        	} else {
        		this.addActionMessage(getText("ICM00002"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
        	}
        }catch(Exception ex){
            if(ex instanceof CherryException){
                this.addActionError(((CherryException)ex).getErrMessage());
            }else if(ex.getCause() instanceof CherryException){
                this.addActionError(((CherryException)ex.getCause()).getErrMessage());
            }else if(ex instanceof WorkflowException){
                this.addActionError(getText(ex.getMessage()));
            }else{
                this.addActionError(ex.getMessage());
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception 
     */
    public void getDepotByAjax() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> resultTreeList = getDepotList(organizationid,userInfo.getLanguage());
        ConvertUtil.setResponseByAjax(response, resultTreeList);
    }
    
    /**
     * 取得库存数量
     * @throws Exception 
     */
    public void getPrtVenIdAndStock() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        //实体仓库
        map.put("BIN_DepotInfoID", form.getInDepotId());
        String inLoginDepotId = form.getInLogicDepotId();
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
     * 取得逻辑仓库信息
     * @throws Exception
     */
    public void getLogicInfo()throws Exception{
        String organizationId = form.getInOrganizationId();
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("organizationId", organizationId);
        ConvertUtil.setResponseByAjax(response, getLogicList(param));
    }
    
    /**
     * Ajax取得发货方
     * @throws Exception 
     */
    public void getOutDepart() throws Exception{
        String inDepotID = form.getInDepotId();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("inDepotID", inDepotID);
        ConvertUtil.setResponseByAjax(response, getOutDepartInfo(param));
    }
    
    /**
     * 根据指定的部门ID和语言信息取得仓库信息
     * @param organizationID
     * @param language
     * @return
     */
    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	List<Map<String, Object>> ret = binOLCM18_BL.getDepotsByDepartID(organizationID, language);
    	return ret;
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
        String organizationId = ConvertUtil.getString(param.get("organizationId"));
        String logicType = "0";
        String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_RD;
        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationId,language);
        if(null != departInfoMap){
            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
            //终端
            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                logicType = "1";
                bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_OD;
            }
        }
        
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
     * 取得发货方信息
     * @param param
     * @return
     * @throws Exception
     */
    private Map<String,Object> getOutDepartInfo(Map<String,Object> param) throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String language = userInfo.getLanguage();
        int inDepotID = CherryUtil.obj2int(param.get("inDepotID"));
        String outOrganizationId = "";
        String outDepartCodeName = "";
        
        //取得默认发货方
        if(inDepotID != 0){
            Map<String,Object> outDeportMap = new HashMap<String,Object>();
            outDeportMap.put("BIN_OrganizationInfoID", organizationInfoId);
            outDeportMap.put("BIN_BrandInfoID", brandInfoId);
            outDeportMap.put("DepotID", inDepotID);
            outDeportMap.put("InOutFlag", "IN");
            outDeportMap.put("BusinessType", CherryConstants.OPERATE_OD);
            outDeportMap.put("language", language);
            try{
                List<Map<String,Object>> outDepotsList =  binOLCM18_BL.getOppositeDepotsByBussinessType(outDeportMap);
                if(null != outDepotsList && outDepotsList.size()>0){
                    outOrganizationId = ConvertUtil.getString(outDepotsList.get(0).get("BIN_OrganizationID"));
                }
            }catch(Exception e){
                this.addActionError(getText("ECM00036"));
            }
            outDepartCodeName = getDepartCodeName(ConvertUtil.getString(outOrganizationId),language);
        }
        
        Map<String,Object> outDepartInfo = new HashMap<String,Object>();
        outDepartInfo.put("outOrganizationId", outOrganizationId);
        outDepartInfo.put("outDepartCodeName", outDepartCodeName);
        return outDepartInfo;
    }
    
    /**
     * 验证提交的参数
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateForm(String methodName) {
        boolean isCorrect = true;
        if(null == form.getInOrganizationId() || "".equals(form.getInOrganizationId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00018")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInDepotId() || "".equals(form.getInDepotId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00019")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getInLogicDepotId() || "".equals(form.getInLogicDepotId())){
            this.addActionError(getText("EST00021", new String[]{getText("PST00020")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getOutOrganizationId() || "".equals(form.getOutOrganizationId())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length == 0){
            this.addActionError(getText("EST00009"));
            isCorrect = false;
            return isCorrect;
        }
        //保存时不校验数量
        if(!methodName.equals("save")){
            for(int i=0;i<form.getProductVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i])){
                    this.addActionError(getText("EST00008"));
                    isCorrect = false;
                    return isCorrect;
                }
            }
        }
        return isCorrect;
    }
    
    @Override
    public BINOLSTSFH01_Form getModel() {
        return form;
    }
}