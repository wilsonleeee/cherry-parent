/*  
 * @(#)BINOLSTBIL14_Action.java     1.0 2012/7/24      
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品盘点申请单详细Action
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
public class BINOLSTBIL16_Action extends BaseAction implements
ModelDriven<BINOLSTBIL16_Form>{

    private static final long serialVersionUID = -207814615696509231L;

    /** 参数FORM */
    private BINOLSTBIL16_Form form = new BINOLSTBIL16_Form();

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;

    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;

    @Resource(name="binOLSTBIL16_BL")
    private BINOLSTBIL16_IF binOLSTBIL16_BL;

    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * 
     * @param 无
     * @return String 跳转页面
     * @throws JSONException 
     * 
     */
    public String init() throws Exception {
        int proStocktakeRequestID = 0;
        //判断是top页打开任务
        if(null == form.getProStocktakeRequestID() || "".equals(form.getProStocktakeRequestID())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            proStocktakeRequestID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setProStocktakeRequestID(billID);
        }else{
            proStocktakeRequestID = CherryUtil.string2int(form.getProStocktakeRequestID());
        }

        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        //取得盘点申请单概要信息 和详细信息
        Map<String,Object> mainDataMap = binOLSTCM14_BL.getProStocktakeRequestMainData(proStocktakeRequestID,language);
        mainDataMap.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        List<Map<String,Object>> detailList = binOLSTCM14_BL.getProStocktakeRequestDetailData(proStocktakeRequestID,language);		

        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

//        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

//        if("2".equals(operateFlag) || CherryConstants.OPERATE_RA_AUDIT.equals(operateFlag)){
//            //取得实体仓库List
//            String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDReceive"));
//            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
//            form.setDepotsInfoList(depotsInfoList);
            
//            Map<String,Object> logicPram =  new HashMap<String,Object>();
//            logicPram.put("BIN_BrandInfoID", brandInfoId);
//            logicPram.put("BusinessType", CherryConstants.OPERATE_RR);
//            logicPram.put("Type", "0");
//            logicPram.put("language", language);
//            //取得逻辑仓库List
//            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
//            form.setLogicDepotsInfoList(logicDepotsList);
//        }
        
        //汇总信息
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        form.setSumInfo(binOLSTBIL16_BL.getSumInfo(map));
        
        form.setProStocktakeRequestMainData(mainDataMap);
        form.setProStocktakeRequestDetailData(detailList);
        
        //数量允许负号
        String allowNegativeFlag = CherryConstants.SYSTEM_CONFIG_ENABLE;
        form.setAllowNegativeFlag(allowNegativeFlag);
        return SUCCESS;
    }

    @Override
    public BINOLSTBIL16_Form getModel() {
        return form;
    }

    /**
     * 该明细画面展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 141：工作流审核模式
     * 
     * @param workFlowID 工作流ID
     * @param mainData 主单数据
     */
    private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
    	// 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //查看明细模式  按钮有【关闭】
        String ret="1";
        if(null==workFlowID||"".equals(workFlowID)){
            //当审核状态为审核通过时为operateFlag=1，查看明细模式
            String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
            if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                //如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                //按钮有【保存】【提交】【删除】【关闭】
            	if(CherryUtil.obj2int(mainData.get("BIN_EmployeeIDDX")) == userInfo.getBIN_EmployeeID()){
            		ret="2";
            	}
            }
        }else{
            //取得当前可执行的action
            ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
            if (adArr != null && adArr.length > 0) {
                // 如果存在可执行action，说明工作流尚未结束
                // 取得当前的业务操作
                //判断当前单据的proStocktakeRequestID是否与PS表中的BIN_ProStocktakeRequestID的值是否一致
                //相同的话同时currentOperation又符合说明可以显示审核按钮。防止在申请单上审核确认单。
                String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
                int proStocktakeRequestID = CherryUtil.obj2int(mainData.get("BIN_ProStocktakeRequestID"));
                IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(workFlowID));
                int psProStocktakeRequestID = ps.getInt("BIN_ProStocktakeRequestID");
                if (CherryConstants.OPERATE_CR_AUDIT.equals(currentOperation) && proStocktakeRequestID == psProStocktakeRequestID) {
                    //盘点申请单审核模式   按钮有【同意】【修改】【废弃】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }else if(CherryConstants.OPERATE_CR_AUDIT2.equals(currentOperation) && proStocktakeRequestID == psProStocktakeRequestID) {
                    //盘点申请单二审模式   按钮有【同意】【修改】【废弃】【关闭】
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
            binOLSTBIL16_BL.tran_save(form, userInfo);
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
            if(!validateDoaction()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            form.setEntryID(entryID);
            form.setActionID(actionID);
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTBIL16_BL.tran_doaction(form,userInfo);
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
     * 验证数据
     */
    public boolean validateDoaction() throws Exception{
        boolean flag = true;
        if(null == form.getPrtVendorId() || form.getPrtVendorId().length == 0){
            this.addActionError(getText("EST00022"));
            flag = false;
            return flag;
        }
        for(int i=0;i<form.getPrtVendorId().length;i++){
            if(null == form.getCheckQuantityArr()[i] || "".equals(form.getCheckQuantityArr()[i])){
                this.addActionError(getText("EST00008"));
                flag = false;
                return flag;
            }
        }
        return flag;
    }
}
