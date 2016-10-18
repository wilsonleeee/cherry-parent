/*  
 * @(#)BINOLSTBIL18_Action.java     1.0 2012/11/28      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL18_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品调拨申请单详细Action
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
public class BINOLSTBIL18_Action extends BaseAction implements
ModelDriven<BINOLSTBIL18_Form>{

    private static final long serialVersionUID = 8048708529558295989L;

    /** 参数FORM */
    private BINOLSTBIL18_Form form = new BINOLSTBIL18_Form();

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;

    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;

    @Resource(name="binOLSTBIL18_BL")
    private BINOLSTBIL18_IF binOLSTBIL18_BL;

    @Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
    
    @Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
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
        int productAllocationID = 0;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        //判断是top页打开任务
        if(null == form.getProductAllocationID() || "".equals(form.getProductAllocationID())){
            if(null != form.getProductAllocationInID() && !"".equals(form.getProductAllocationInID())){
                //进入调入单明细
                initProductAllocationIn();
                return "BINOLSTBIL18_1";
            }else if(null != form.getProductAllocationOutID() && !"".equals(form.getProductAllocationOutID())){
                //进入调出单明细
                initProductAllocationOut();
                return "BINOLSTBIL18_1";
            }else{
                //取得URL中的参数信息
                String entryID= request.getParameter("entryID");
                String billID= request.getParameter("mainOrderID");
                productAllocationID = Integer.parseInt(billID);
                form.setWorkFlowID(entryID);
                form.setProductAllocationID(billID);
            }
        }else{
            productAllocationID = CherryUtil.string2int(form.getProductAllocationID());
        }
        
        //取得调拨申请单概要信息 和详细信息
        Map<String,Object> mainDataMap = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID,language);
        List<Map<String,Object>> detailList = binOLSTCM16_BL.getProductAllocationDetailData(productAllocationID,language);		

        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        //详细第一条仓库、逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("DepotCodeNameIn", detailList.get(0).get("DepotCodeName"));
            mainDataMap.put("LogicInventoryCodeNameIn", detailList.get(0).get("LogicInventoryCodeName"));
            mainDataMap.put("BIN_InventoryInfoIDIn", detailList.get(0).get("BIN_InventoryInfoID"));
            mainDataMap.put("BIN_LogicInventoryInfoIDIn", detailList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        if(CherryConstants.OPERATE_BG.equals(operateFlag)){
            long entryID = Long.parseLong(workFlowID);
            IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(entryID);
            
            int productAllocationOutID = ps.getInt("BIN_ProductAllocationOutID");
            //取调出单明细，替换调拨单的明细
            detailList = binOLSTCM16_BL.getProductAllocationOutDetailData(productAllocationOutID,language);

            Map<String,Object> productAllocationOutMainData = binOLSTCM16_BL.getProductAllocationOutMainData(productAllocationOutID, language);
            mainDataMap.put("TotalQuantity", productAllocationOutMainData.get("TotalQuantity"));
            mainDataMap.put("TotalAmount", productAllocationOutMainData.get("TotalAmount"));
            
            //取得实体仓库List
            String organizationid = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationIDIn"));
            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
            form.setDepotsInfoListIn(depotsInfoList);
            
            String logicType = "0";//后台
            String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_BG;
            Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationid,language);
            if(null != departInfoMap){
                String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
                //终端
                if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                    logicType = "1";
                    bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_BG;
                }
            }
            
            Map<String,Object> logicPram =  new HashMap<String,Object>();
            logicPram.put("BIN_BrandInfoID", brandInfoId);
            logicPram.put("BusinessType", bussinessType);
            logicPram.put("Type", logicType);
            logicPram.put("language", language);
            logicPram.put("ProductType", "1");
            //取得逻辑仓库List
            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
            form.setLogicDepotsInfoListIn(logicDepotsList);
        }else if(CherryConstants.OPERATE_LG.equals(operateFlag)){
            //取得实体仓库List
            String organizationid = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationIDOut"));
            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
            form.setDepotsInfoListOut(depotsInfoList);
            
            String logicType = "0";//后台
            String bussinessType = CherryConstants.LOGICDEPOT_BACKEND_LG;
            Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(organizationid,language);
            if(null != departInfoMap){
                String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
                //终端
                if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                    logicType = "1";
                    bussinessType = CherryConstants.LOGICDEPOT_TERMINAL_LG;
                }
            }
            
            Map<String,Object> logicPram =  new HashMap<String,Object>();
            logicPram.put("BIN_BrandInfoID", brandInfoId);
            logicPram.put("BusinessType", bussinessType);
            logicPram.put("Type", logicType);
            logicPram.put("language", language);
            logicPram.put("ProductType", "1");
            //取得逻辑仓库List
            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
            form.setLogicDepotsInfoListOut(logicDepotsList);
            
            String inventoryInfo = (null == depotsInfoList || depotsInfoList.size() == 0) ? "" : ConvertUtil.getString(depotsInfoList.get(0).get("BIN_DepotInfoID"));
            String logicInventoryInfoID = (null == logicDepotsList || logicDepotsList.size() == 0) ? "" : ConvertUtil.getString(logicDepotsList.get(0).get("BIN_LogicInventoryInfoID"));
            // 获取产品明细的相应库存
            for(Map<String, Object> detailMap : detailList) {
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_ProductVendorID", CherryUtil.obj2int(detailMap.get("BIN_ProductVendorID")));
                if(null == inventoryInfo || "".equals(inventoryInfo)){
                    pram.put("BIN_DepotInfoID", 0);
                    detailMap.put("BIN_InventoryInfoID", 0);
                }else{
                    pram.put("BIN_DepotInfoID", inventoryInfo);
                }
                
                if(null == logicInventoryInfoID || "".equals(logicInventoryInfoID)){
                    pram.put("BIN_LogicInventoryInfoID", 0);
                    detailMap.put("BIN_LogicInventoryInfoID", 0);
                }else{
                    pram.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
                }
                //调出步骤显示调出方实际库存【不扣除冻结库存】
                pram.put("FrozenFlag", "1");
                int productQuantity = binOLCM20_BL.getProductStock(pram);
                detailMap.put("ProductQuantity", productQuantity);
            }
        }else if(CherryConstants.OPERATE_AC_AUDIT.equals(operateFlag)){
            //处理先有调出单然后再审核的单据
            IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(workFlowID));
            Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.INT);
            String productAllocationOutID = ConvertUtil.getString(propertyMap.get("BIN_ProductAllocationOutID"));
            if(!productAllocationOutID.equals("")){
                Map<String,Object> mainData_LG = binOLSTCM16_BL.getProductAllocationOutMainData(CherryUtil.obj2int(productAllocationOutID), language);
                List<Map<String,Object>> detailList_LG = binOLSTCM16_BL.getProductAllocationOutDetailData(CherryUtil.obj2int(productAllocationOutID),language);
                mainDataMap.put("TotalQuantity", mainData_LG.get("TotalQuantity"));
                mainDataMap.put("TotalAmount", mainData_LG.get("TotalAmount"));
                mainDataMap.put("UpdateTime", mainData_LG.get("UpdateTime"));
                mainDataMap.put("ModifyCount", mainData_LG.get("ModifyCount"));
                mainDataMap.put("VerifiedFlag", mainData_LG.get("VerifiedFlag"));
                String inventoryInfo = "0";
                String logicInventoryInfoID = "0";
                if(null != detailList_LG && detailList_LG.size()>0){
                    mainDataMap.put("DepotCodeNameOut", detailList_LG.get(0).get("DepotCodeName"));
                    mainDataMap.put("LogicInventoryCodeNameOut", detailList_LG.get(0).get("LogicInventoryCodeName"));
                    // 调出仓库与逻辑仓库
                    inventoryInfo = ConvertUtil.getString(detailList_LG.get(0).get("BIN_InventoryInfoID"));
                    logicInventoryInfoID = ConvertUtil.getString(detailList_LG.get(0).get("BIN_LogicInventoryInfoID"));
                    mainDataMap.put("BIN_InventoryInfoIDOut", inventoryInfo);
                    mainDataMap.put("BIN_LogicInventoryInfoIDOut", logicInventoryInfoID);
                }
                
                Map<String,Object> allocationQuantityMap = new HashMap<String,Object>();
                // 调入申请单
                for(int i=0;i<detailList.size();i++){
                    Map<String,Object> temp = detailList.get(i);
                    allocationQuantityMap.put(ConvertUtil.getString(temp.get("BIN_ProductVendorID")), ConvertUtil.getString(temp.get("Quantity")));
                }
                // 调出单
                for(int i=0;i<detailList_LG.size();i++){
                    Map<String,Object> temp = detailList_LG.get(i);
                    temp.put("ApplyQuantity", CherryUtil.obj2int(allocationQuantityMap.get(ConvertUtil.getString(temp.get("BIN_ProductVendorID")))));
                    // 调出方库存
                    Map<String,Object> pram = new HashMap<String,Object>();
                    pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                    pram.put("BIN_DepotInfoID", inventoryInfo);
                    pram.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
                    //调出步骤显示调出方实际库存【不扣除冻结库存】
                    pram.put("FrozenFlag", "1");
                    int productQuantity = binOLCM20_BL.getProductStock(pram);
                    temp.put("ProductQuantity", productQuantity);
                }
                detailList.clear();
                detailList.addAll(detailList_LG);
                form.setAuditLGFlag("YES");
                form.setProductAllocationOutID(productAllocationOutID);
                
            } else {
            	// 对于先审核后调出的流程，此审核是不对库存进行校验的
            	form.setAuditLGFlag("NO");
            }
        }
        
        form.setProductAllocationMainData(mainDataMap);
        form.setProductAllocationDetailData(detailList);
        
        return SUCCESS;
    }

    private void initProductAllocationIn(){
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        
        int billID = CherryUtil.obj2int(form.getProductAllocationInID());
        Map<String,Object> mainDataMap = binOLSTCM16_BL.getProductAllocationInMainData(billID,language);
        List<Map<String,Object>> detailList = binOLSTCM16_BL.getProductAllocationInDetailData(billID,language);
        
        //详细第一条仓库、逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("DepotCodeNameIn", detailList.get(0).get("DepotCodeName"));
            mainDataMap.put("LogicInventoryCodeNameIn", detailList.get(0).get("LogicInventoryCodeName"));
        }
        
        form.setProductAllocationMainData(mainDataMap);
        form.setProductAllocationDetailData(detailList);
        form.setTradeType(CherryConstants.OS_BILLTYPE_BG);
    }
    
    /**
     * 获取调出单详细
     * 1）将明细中的第一条仓库、逻辑仓库信息设置到主单中
     * 2）将调出方的产品库存设置到明细中
     */
    private void initProductAllocationOut(){
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        
        int billID = CherryUtil.obj2int(form.getProductAllocationOutID());
        Map<String,Object> mainDataMap = binOLSTCM16_BL.getProductAllocationOutMainData(billID,language);
        List<Map<String,Object>> detailList = binOLSTCM16_BL.getProductAllocationOutDetailData(billID,language);
        //详细第一条仓库、逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("DepotCodeNameOut", detailList.get(0).get("DepotCodeName"));
            mainDataMap.put("LogicInventoryCodeNameOut", detailList.get(0).get("LogicInventoryCodeName"));
        }
        
        form.setProductAllocationMainData(mainDataMap);
        form.setProductAllocationDetailData(detailList);
        form.setTradeType(CherryConstants.OS_BILLTYPE_LG);
    }
    
    /**
     * 通过Ajax取得指定仓库的库存
     * @throws Exception
     */
    public void getStockCountAjax() throws Exception {
    	String[] currentIndexArr = request.getParameterValues("currentIndex");
    	String[] prtVendorIdArr = form.getPrtVendorId();
    	Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        if(null != prtVendorIdArr){
            for(int i=0;i<prtVendorIdArr.length;i++){
                //调出步骤显示调出方实际库存
                map.put("FrozenFlag", "1");
                map.put("BIN_ProductVendorID", prtVendorIdArr[i]);
                map.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getDepotsInfoListOut()));
                map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicInventoryInfoIDOut()));
                int stockCount = binOLCM20_BL.getProductStock(map);
                
                Map<String,Object> resultMap = new HashMap<String,Object>();
                resultMap.put("currentIndex", currentIndexArr[i]);
                resultMap.put("ProductQuantity", stockCount);
                resultMap.put("BIN_ProductVendorID", prtVendorIdArr[i]);
                resultList.add(resultMap);
            }
        }
        
        ConvertUtil.setResponseByAjax(response, resultList);
    }
    
    @Override
    public BINOLSTBIL18_Form getModel() {
        return form;
    }

    /**
     * 该明细画面展现模式
     * 1：明细查看模式
     * 76：工作流审核模式
     * 70：工作流调入模式
     * 80：工作流调出模式
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
                if(CherryUtil.obj2int(mainData.get("BIN_EmployeeID")) == userInfo.getBIN_EmployeeID()){
                    //如果当前登录者和制单员为同一人，则为非工作流的编辑模式，否则只能查看
                    ret="2";
                }
            }
        }else{
            //取得当前可执行的action
            ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
            if (adArr != null && adArr.length > 0) {
                // 如果存在可执行action，说明工作流尚未结束
                // 取得当前的业务操作
                String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
                if (CherryConstants.OPERATE_AC_AUDIT.equals(currentOperation)) {
                    //调拨流程审核模式   按钮有【同意】【修改】【废弃】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }else if(CherryConstants.OPERATE_LG.equals(currentOperation)){
                    //调拨流程调出模式   按钮有【调出】【拒绝】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }else if(CherryConstants.OPERATE_BG.equals(currentOperation)){
                    //调拨流程调入模式   按钮有【调入】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }
            }
        }       
        return ret;
    }

    /**
     * 非工作流中的保存
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL18_BL.tran_save(form, userInfo);
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
     * 提交
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL18_BL.tran_submit(form, userInfo);
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
            String workFlowName = workflow.getWorkflowName(Long.parseLong(entryID));
            ActionDescriptor ad = workflow.getWorkflowDescriptor(workFlowName).getAction(CherryUtil.obj2int(actionID));
            Map<String,Object> metaAttributes = ad.getMetaAttributes();
            String operateResultCode = ConvertUtil.getString(metaAttributes.get("OS_OperateResultCode"));
            //先有调出单然后再审核的单据，需要对库存进行校验
            IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(entryID));
            Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.INT);
            String productAllocationOutID = ConvertUtil.getString(propertyMap.get("BIN_ProductAllocationOutID"));
            // OS_OperateResultCode= 101：审核通过（且已经先有调出单）；100：调出 几个值时，需要检查库存
			if (((operateResultCode.equals("101") && !""
					.equals(productAllocationOutID)) || "100"
					.equals(operateResultCode))
					&& !validateStock()) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTBIL18_BL.tran_doaction(form,userInfo);
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
     * 根据系统配置项是否需要验证库存大于发货数量
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateStock() {
        boolean isCorrect = true;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getInventoryInfoIDOut()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getLogicInventoryInfoIDOut()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "1");//产品
            paramMap.put("IDArr", form.getPrtVendorId());
            paramMap.put("QuantityArr", form.getQuantityArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
                this.addActionError(getText("EST00043"));
                return isCorrect;
            }
        }

        return isCorrect;
    }

    /**
     * 验证数据
     */
    public boolean validateDoaction() throws Exception{
        boolean flag = true;
        String actionID = request.getParameter("actionid").toString();
        if("801".equals(actionID)){
        	// 调出
            if(null == form.getInventoryInfoIDOut() || "".equals(form.getInventoryInfoIDOut())){
                this.addActionError(getText("EST00021", new String[]{getText("PST00022")}));
                return false;
            }
            if(null == form.getLogicInventoryInfoIDOut() || "".equals(form.getLogicInventoryInfoIDOut())){
                this.addActionError(getText("EST00021", new String[]{getText("PST00023")}));
                return false;
            }
        }else if("901".equals(actionID)){
        	// 调入
            if(null == form.getInventoryInfoIDIn() || "".equals(form.getInventoryInfoIDIn())){
                this.addActionError(getText("EST00021", new String[]{getText("PST00025")}));
                return false;
            }
            if(null == form.getLogicInventoryInfoIDIn() || "".equals(form.getLogicInventoryInfoIDOut())){
                this.addActionError(getText("EST00021", new String[]{getText("PST00026")}));
                return false;
            }
        }
        if(null == form.getPrtVendorId() || form.getPrtVendorId().length == 0){
            this.addActionError(getText("EST00022"));
            return false;
        }
        for(int i=0;i<form.getPrtVendorId().length;i++){
            if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i])){
                this.addActionError(getText("EST00008"));
                return false;
            }
        }
        return flag;
    }
}