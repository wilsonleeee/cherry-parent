/*  
 * @(#)BINOLSSPRM65_Action.java     1.0 2013/01/25      
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

import java.util.ArrayList;
import java.util.HashMap;
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
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.cherry.ss.prm.form.BINOLSSPRM65_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM65_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSSPRM65_Action extends BaseAction implements ModelDriven<BINOLSSPRM65_Form> {

    private static final long serialVersionUID = -4566880299341607103L;

    private BINOLSSPRM65_Form form = new BINOLSSPRM65_Form();
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_IF binOLSSCM09_BL;
    
    @Resource(name="binOLSSPRM65_BL")
    private BINOLSSPRM65_IF binOLSSPRM65_BL;
    
    @Resource(name="binOLSSCM01_BL")
    private BINOLSSCM01_BL binOLSSCM01_BL;
    
    /**
     * 画面初期显示
     * @param 无
     * @return String 跳转页面
     * @throws JSONException 
     * 
     */
    public String init() throws Exception {
        
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        
        int prmInDepotID = 0;
        //判断是top页打开任务
        if(null == form.getPrmInDepotID() || "".equals(form.getPrmInDepotID())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            prmInDepotID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setPrmInDepotID(billID);
        }else{
        	prmInDepotID = CherryUtil.string2int(form.getPrmInDepotID());
        }
        
        //取得入库单概要信息 和详细信息
        Map<String,Object> mainMap = binOLSSCM09_BL.getPrmInDepotMainData(prmInDepotID, language);
        List<Map<String,Object>> detailList = binOLSSCM09_BL.getPrmInDepotDetailData(prmInDepotID, language);
        //由于入库单据中的实体仓库和逻辑仓库只能有一个,所以取第一个即可
        mainMap.put("DepotCodeName", detailList.get(0).get("DepotCodeName"));
        mainMap.put("LogicInventoryName", detailList.get(0).get("LogicInventoryCodeName"));
        mainMap.put("BIN_InventoryInfoID", detailList.get(0).get("BIN_InventoryInfoID"));
        mainMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);
        
        int defaultDepotInfoID = 0;
        int defaultLogicInventoryInfoID = 0;
        if("2".equals(operateFlag) || CherryConstants.OPERATE_GR_AUDIT.equals(operateFlag) || CherryConstants.OPERATE_GR_EDIT.equals(operateFlag)){
            
        }
        form.setInDepotMainMap(mainMap);
        
        //TODO:审核模式下 明细里面要带上库存，并且还要考虑已冻结库存
        if(CherryConstants.OPERATE_GR_AUDIT.equals(operateFlag) || CherryConstants.OPERATE_GR_EDIT.equals(operateFlag)){
            for(int i=0;i<detailList.size();i++){
                Map<String,Object> temp = detailList.get(i);
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_PromotionProductVendorID", CherryUtil.obj2int(temp.get("BIN_PromotionProductVendorID")));
                String inventoryInfo = ConvertUtil.getString(temp.get("BIN_InventoryInfoID"));
                if(null == inventoryInfo || "".equals(inventoryInfo)){
                    pram.put("BIN_DepotInfoID", defaultDepotInfoID);
                    temp.put("BIN_InventoryInfoID", defaultDepotInfoID);
                }else{
                    pram.put("BIN_DepotInfoID", inventoryInfo);
                }
                String logicInventoryInfoID = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoID"));
                if(null == logicInventoryInfoID || "".equals(logicInventoryInfoID)){
                    pram.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
                    temp.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
                }else{
                    pram.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
                }
                pram.put("FrozenFlag", "1");
                int prmQuantity = binOLSSCM01_BL.getPrmStock(pram);
                temp.put("PrmQuantity", prmQuantity);
                detailList.set(i, temp);
            }
        }else{
            for(int i=0;i<detailList.size();i++){
                Map<String,Object> temp = detailList.get(i);
                String inventoryInfoID = ConvertUtil.getString(temp.get("BIN_InventoryInfoID"));
                if(null == inventoryInfoID || "".equals(inventoryInfoID)){
                    temp.put("BIN_InventoryInfoID", defaultDepotInfoID);
                }
                String logicInventoryInfoID = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoID"));
                if(null == logicInventoryInfoID || "".equals(logicInventoryInfoID)){
                    temp.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
                }
                detailList.set(i, temp);
            }
        }
        form.setInDepotDetailList(detailList);
        
        return SUCCESS;
    }
    
    /**
     * 目前该明细画面有四种展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 11：工作流审核模式
     * 12：工作流编辑模式
     * 
     * @param workFlowID 工作流ID
     * @param mainData 主单单据
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
             }else if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //无工作流直接入库（柜台无关联单号入库）
                 ret = "999";
             }
         }else{
             // 用户信息
             UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
             //取得当前可执行的action
             ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
             // 取得当前的业务操作
             String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
             if (adArr != null && adArr.length > 0) {
                // 如果存在可执行action，说明工作流尚未结束
                if (CherryConstants.OPERATE_GR_AUDIT.equals(currentOperation)) {
                    //订单审核模式   按钮有【通过】【退回】【删除】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                } else if (CherryConstants.OPERATE_GR_EDIT.equals(currentOperation)) {
                    //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }
                try{
                    mainData.put("OSEditFlag", "false");
                    for(int i=0;i<adArr.length;i++){
                        Map<String,Object> metaMap = adArr[i].getMetaAttributes();
                        if(null != metaMap && null != metaMap.get("OS_ButtonEdit")){
                            mainData.put("OSEditFlag", "true");//工作流文件该步骤有修改按钮
                            break;
                        }
                    }
                }catch(Exception e){
                    
                }
            }
            //工作流已经结束
            if(currentOperation.equals("999")){
                ret= currentOperation;
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
            Map<String,Object> billInformation = new HashMap<String,Object>();
            //取得入库主表信息
            Map<String,Object> mainData = new HashMap<String,Object>();
            //取得当前操作者信息
            Map<String,Object> sessionMap = this.getSessionMap();
            //取得入库明细信息
            List<String[]> detailList = this.getDetailList();
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            mainData.put("entryID", entryID);
            mainData.put("actionID", actionID);
            mainData.putAll(getMainData());
            billInformation.put("mainData", mainData);
            billInformation.put("detailList", detailList);
            billInformation.put("opComments", form.getOpComments());
            
            //调用BL中的doaction方法
            binOLSSPRM65_BL.tran_doaction(sessionMap,billInformation);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            throw e;
        }
    }

    /**
     * 非工作流模式下保存入库单
     * 
     * */
    public String saveIndepot() throws Exception{
        try{
            //取得入库主表信息
            Map<String,Object> mainData = this.getMainData();
            //取得当前操作者信息
            Map<String,Object> sessionMap = this.getSessionMap();
            //取得入库明细信息
            List<String[]> detailList = this.getDetailList();
            binOLSSPRM65_BL.tran_save(sessionMap, mainData, detailList);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            throw e;
        }
    }
    
    /**
     * 取得sessionMap
     * 
     * */
    public Map<String,Object> getSessionMap(){
        Map<String,Object> sessionMap = new HashMap<String,Object>();
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //当前操作人员的员工ID
        sessionMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        //当前操作人员登录ID
        sessionMap.put("userID", userInfo.getBIN_UserID());
        //当前操作人员的岗位
        sessionMap.put("positionID", userInfo.getBIN_PositionCategoryID());
        //当前操作人员的所属部门
        sessionMap.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        //当前操作人员的所属品牌
        sessionMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        //当前操作人员的所属组织
        sessionMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        sessionMap.put("CreatedBy", userInfo.getBIN_UserID());
        sessionMap.put("UpdatedBy", userInfo.getBIN_UserID());
        sessionMap.put("CreatePGM", "BINOLSSPRM65");
        sessionMap.put("UpdatePGM", "BINOLSSPRM65");
        //用户信息
        sessionMap.put("UserInfo", userInfo);
        return sessionMap;
    }
    
    /**
     * 取得入库主表信息
     * 
     * 
     * */
    public Map<String,Object> getMainData(){
        Map<String,Object> mainData = new HashMap<String,Object>();
        //实体仓库
        mainData.put("depotInfoId", form.getDepotInfoId());
        //逻辑仓库
        mainData.put("logicInventoryInfoId", form.getLogicDepotsInfoId());
        //入库主记录ID
        mainData.put("BIN_PrmInDepotID", form.getPrmInDepotID());
        //上次更新时间
        mainData.put("OldUpdateTime", form.getUpdateTime());
        //更新次数
        mainData.put("OldModifyCount", form.getModifyCount());
        return mainData;
    }
    
    /**
     * 取得入库详细List
     * 
     * */
    public List<String[]> getDetailList() throws Exception{
        List<String[]> detailList = new ArrayList<String[]>();
        //促销品厂商ID
        String[] productVendorIDArr = form.getPrmVendorIDArr();
        //促销产品批次号
//        String[] batchNoArr = form.getBatchNoArr();
        //数量
        String[] quantityArr = form.getQuantityArr();
        //备注
        String[] commentsArr = form.getCommentsArr();
        //价格
        String[] priceArr = form.getPriceUnitArr();
        detailList.add(productVendorIDArr);
//        detailList.add(batchNoArr);
        detailList.add(quantityArr);
        detailList.add(commentsArr);
        detailList.add(priceArr);
        
        return detailList;
    }
    
//    /**
//     * 取得冻结库存
//     * 
//     * */
//    public void getProductStock()throws Exception{
//        int defaultDepotInfoID = 0;
//        int defaultLogicInventoryInfoID = 0;
//        Map<String,Object> pram = new HashMap<String,Object>();
//        pram.put("BIN_PromotionProductVendorID", form.getPrmVendorId());
//        if(null == form.getDepotInfoId() || "".equals(form.getDepotInfoId())){
//            pram.put("BIN_DepotInfoID", defaultDepotInfoID);
//        }else{
//            pram.put("BIN_DepotInfoID", form.getDepotInfoId());
//        }
//        if(null == form.getLogicDepotsInfoId() || "".equals(form.getLogicDepotsInfoId())){
//            pram.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
//        }else{
//            pram.put("BIN_LogicInventoryInfoID", form.getLogicDepotsInfoId());
//        }
//        pram.put("FrozenFlag", "2");
//        int productQuantity = binOLCM20_BL.getProductStock(pram);
//        ConvertUtil.setResponseByAjax(response, productQuantity);
//    }
    
    /**
     * 数据验证
     * 
     * 
     * */
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if("501".equals(actionID) || "502".equals(actionID) || "701".equals(actionID)){
            if(null == form.getPrmVendorIDArr() || form.getPrmVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            for(int i=0;i<form.getPrmVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i])){
                    this.addActionError(getText("EST00008"));
                    return;
                }
            }
        }
    }
    
    @Override
    public BINOLSSPRM65_Form getModel() {
        return form;
    }
}