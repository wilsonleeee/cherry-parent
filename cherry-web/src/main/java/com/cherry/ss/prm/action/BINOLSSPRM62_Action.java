/*	
 * @(#)BINOLSSPRM62_Action.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.prm.form.BINOLSSPRM62_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM62_IF;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品移库详细Action
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM62_Action extends BaseAction implements ModelDriven<BINOLSSPRM62_Form> {

    private static final long serialVersionUID = 4489831525366852090L;

    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    @Resource(name="binOLSSPRM62_BL")
    private BINOLSSPRM62_IF binOLSSPRM62_BL;
    
    private BINOLSSPRM62_Form form = new BINOLSSPRM62_Form();
    
    @Override
    public BINOLSSPRM62_Form getModel() {
        return form;
    }

    public String init(){
        int promotionShiftID = 0;
        //判断是top页打开任务
        if(null == form.getPromotionShiftID() || "".equals(form.getPromotionShiftID())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            promotionShiftID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setPromotionShiftID(billID);
        }else{
            promotionShiftID = CherryUtil.string2int(form.getPromotionShiftID());
        }
        
        Map<String,Object> mapMain = binOLSSCM08_BL.getPrmShiftMainData(promotionShiftID);
        List<Map<String,Object>> listDetail = binOLSSCM08_BL.getPrmShiftDetailData(promotionShiftID);
    
        if(null != listDetail && listDetail.size()>0){
            mapMain.put("BIN_DepotInfoID", listDetail.get(0).get("FromDepotInfoID"));
            mapMain.put("FromLogicInventoryInfoID", listDetail.get(0).get("FromLogicInventoryInfoID"));
            mapMain.put("FromStorageLocationInfoID", listDetail.get(0).get("FromStorageLocationInfoID"));
            mapMain.put("ToLogicInventoryInfoID", listDetail.get(0).get("ToLogicInventoryInfoID"));
            mapMain.put("ToStorageLocationInfoID", listDetail.get(0).get("ToStorageLocationInfoID"));
            mapMain.put("DepotCodeName", listDetail.get(0).get("DepotCodeName"));
            mapMain.put("FromLogicInventoryName", listDetail.get(0).get("FromLogicInventoryName"));
            mapMain.put("ToLogicInventoryName", listDetail.get(0).get("ToLogicInventoryName"));
            mapMain.put("BIN_ProductVendorPackageID", listDetail.get(0).get("BIN_ProductVendorPackageID"));
        }
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mapMain.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mapMain);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        form.setShiftMainData(mapMain);
        form.setShiftDetailData(listDetail);
        
        return SUCCESS;
    }
    
    /**
     * 目前该明细画面有四种展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 31：工作流审核模式
     * 32：工作流编辑模式
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
                 if (CherryConstants.OPERATE_MV_AUDIT.equals(currentOperation)) {
                     //报损单审核模式   按钮有【通过】【退回】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 } else if (CherryConstants.OPERATE_MV_EDIT.equals(currentOperation)) {
                     //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 }
             }
         }       
         return ret;
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
            binOLSSPRM62_BL.tran_doaction(form,userInfo);
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
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if("501".equals(actionID)){
            if(null == form.getPrmVendorIDArr() || form.getPrmVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            for(int i=0;i<form.getPrmVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i]) ){
                    this.addActionError(getText("EST00008"));
                    return;
                }
            }
        }
    }
}