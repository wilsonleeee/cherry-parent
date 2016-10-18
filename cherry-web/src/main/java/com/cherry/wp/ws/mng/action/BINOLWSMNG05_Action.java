/*
 * @(#)BINOLWSMNG05_Action.java     1.0 2014/10/10
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
package com.cherry.wp.ws.mng.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS05_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL17_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.ws.mng.form.BINOLWSMNG05_Form;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG05_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
/**
 * 
 * 调出Action
 * 
 * @author niushunjie
 * @version 1.0 2014.10.10
 */
@SuppressWarnings("unchecked")
public class BINOLWSMNG05_Action extends BaseAction implements ModelDriven<BINOLWSMNG05_Form> {

    private static final long serialVersionUID = -628894457005835439L;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL binOLCM19_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLSTBIL17_BL")
    private BINOLSTBIL17_IF binOLSTBIL17_BL;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLWSMNG05_BL")
    private BINOLWSMNG05_IF binOLWSMNG05_BL;
    
    @Resource(name="binOLPTRPS05_BL")
    private BINOLPTRPS05_IF binOLPTRPS05_BL;
    
    @Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;
    
    /** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
    /** 参数FORM */
    private BINOLWSMNG05_Form form = new BINOLWSMNG05_Form();
    
    /**
     * 调入画面初始化
     * 
     * @return 订货画面
     * @throws JSONException 
     */
    public String init() throws JSONException {
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        // 柜台信息
        CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("businessType", "1");
        params.put("operationType", "1");
        params.put("departId", counterInfo.getOrganizationId());
        params.put("testType", binOLCM00_BL.getDepartTestType(ConvertUtil.getString(counterInfo.getOrganizationId())));
        form.setParams(JSONUtil.serialize(params));
        
        //调入审核状态是否可见
        String outputStockState = binOLCM14_BL.getWebposConfigValue("9046", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
        if(null == outputStockState || "".equals(outputStockState)){
        	outputStockState = "Y";
		}
        form.setOutputStockState(outputStockState);
        
        return SUCCESS;
    }
    
    /**
     * 调出单查询一览
     * @return
     * @throws Exception
     */
    public String search() throws Exception {
    	// 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得调出单总数
        int count = binOLPTRPS05_BL.getAllocationCount(searchMap);
        if (count > 0) {
            // 取得调出单List
            form.setProductAllocationOutList(binOLPTRPS05_BL.getAllocationList(searchMap));
        }
        form.setSumInfo(binOLPTRPS05_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        
      //调入审核状态是否可见
        String outputStockState = binOLCM14_BL.getWebposConfigValue("9046", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
        if(null == outputStockState || "".equals(outputStockState)){
        	outputStockState = "Y";
		}
        form.setOutputStockState(outputStockState);
        // AJAX返回至dataTable结果页面
        return "BINOLWSMNG05_01";
    }
    
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
    public String initDetail() throws Exception {
        int productAllocationID = 0;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        productAllocationID = CherryUtil.string2int(form.getProductAllocationID());
        String productAllocationOutID = ConvertUtil.getString(form.getProductAllocationOutID());
        
        //获取是否负库存操作
        String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
     	String nsOperation = binOLCM14_BL.getConfigValue("1109", organizationInfoId, brandInfoId);
     	form.setNsOperation(nsOperation);
     	//云POS调出是否存在拒绝操作
     	String outBackFlag = binOLCM14_BL.getWebposConfigValue("9042", organizationInfoId, brandInfoId);
     	form.setOutBackFlag(outBackFlag);
        //取得调拨申请单概要信息 和详细信息
        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        if(!productAllocationOutID.equals("")){
            mainDataMap = binOLSTCM16_BL.getProductAllocationOutMainData(ConvertUtil.getInt(productAllocationOutID),language);
            detailList = binOLSTCM16_BL.getProductAllocationOutDetailData(ConvertUtil.getInt(productAllocationOutID),language);
        }else{
            mainDataMap = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID,language);
            detailList = binOLSTCM16_BL.getProductAllocationDetailData(productAllocationID,language);
        }

        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        //详细第一条调入仓库、调入逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("DepotCodeNameIn", detailList.get(0).get("DepotCodeName"));
            mainDataMap.put("LogicInventoryCodeNameIn", detailList.get(0).get("LogicInventoryCodeName"));
            mainDataMap.put("BIN_InventoryInfoIDIn", detailList.get(0).get("BIN_InventoryInfoID"));
            mainDataMap.put("BIN_LogicInventoryInfoIDIn", detailList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        //取得调出实体仓库List
        String organizationIDOut = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationIDOut"));
        int depotInfoIDOut = 0;
        List<Map<String,Object>> depotsInfoOutList = binOLCM18_BL.getDepotsByDepartID(organizationIDOut, language);
        if(null != depotsInfoOutList && depotsInfoOutList.size()>0){
            mainDataMap.put("BIN_InventoryInfoIDOut", depotsInfoOutList.get(0).get("BIN_DepotInfoID"));
            depotInfoIDOut = CherryUtil.obj2int(depotsInfoOutList.get(0).get("BIN_DepotInfoID"));
        }
        
        Map<String,Object> logicPram =  new HashMap<String,Object>();
        logicPram.put("BIN_BrandInfoID", brandInfoId);
        logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_LG);
        logicPram.put("Type", "1");//终端
        logicPram.put("language", language);
        logicPram.put("ProductType", "1");
        //取得调出逻辑仓库List
        List<Map<String,Object>> logicDepotsOutList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
        int logicInventoryInfoIDOut = 0;
        if(null != logicDepotsOutList && logicDepotsOutList.size()>0){
            mainDataMap.put("BIN_LogicInventoryInfoIDOut", logicDepotsOutList.get(0).get("BIN_LogicInventoryInfoID"));
            logicInventoryInfoIDOut = CherryUtil.obj2int(logicDepotsOutList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        //调出方库存
        if(operateFlag.equals("2") || operateFlag.equals(CherryConstants.OPERATE_BG) || operateFlag.equals(CherryConstants.OPERATE_LG)){
            for(int i=0;i<detailList.size();i++){
                Map<String,Object> temp = detailList.get(i);
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                pram.put("FrozenFlag", "1");
                pram.put("BIN_DepotInfoID", depotInfoIDOut);
                pram.put("BIN_LogicInventoryInfoID", logicInventoryInfoIDOut);
                int orderStock = binOLCM20_BL.getProductStock(pram);
                temp.put("StockQuantity", orderStock);
            }
        }
        
        //调出日期
        form.setApplyDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        //BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", organizationIDOut);
        // 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
  		String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", organizationInfoId, brandInfoId);
  		if(null == attendanceFlag || "".equals(attendanceFlag)){
  			attendanceFlag = "N";
  		}
  		List<Map<String, Object>> baInfoList= null;
  		if("N".equals(attendanceFlag)){
  			baInfoList = binOLWPCM01_BL.getBAInfoList(paramBAMap);
  		}else{
  			baInfoList = binOLWPCM01_BL.getActiveBAList(paramBAMap);
  		}
        form.setCounterBAList(baInfoList);
        
        form.setProductAllocationMainData(mainDataMap);
        form.setProductAllocationDetailData(detailList);
        
        //根据柜台的部门ID取出柜台号
        String counterDepartID = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationIDOut"));
        Map<String,Object> paramCounterMap = new HashMap<String,Object>();
        paramCounterMap.put("organizationId", counterDepartID);
        Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramCounterMap);
        if(null != counterInfo){
            form.setCounterCode(ConvertUtil.getString(counterInfo.get("counterCode")));
        }
        
        return "BINOLWSMNG05_02";
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     * @throws JSONException 
     */
    private Map<String, Object> getSearchMap() throws JSONException {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        //所属组织
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        map.put("brandInfoId",userInfo.getBIN_BrandInfoID());
        // 单号
        map.put("allocationNo", form.getAllocationrNo().trim());
        // 开始日期
        map.put("startDate", form.getStartDate());
        // 结束日期
        map.put("endDate", form.getEndDate());
        // 审核状态-通过
        //map.put("verifiedFlag", form.getVerifiedFlag());
        //map.put("verifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        // 处理状态-未处理
        //map.put("tradeStatus", form.getTradeStatus());
        //map.put("tradeStatus", "10");
        // 员工ID
        map.put("employeeId", form.getEmployeeId());
        //调拨单类型-调出单
        map.put("allocationType", "1");
        // 画面ID
        map.put("MENU_ID", "BINOLWSMNG05");
        //选中单据ID Arr
        map.put("checkedBillIdArr", form.getCheckedBillIdArr());
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
       
        return map;
    }
    
    /**
     * 取得调入单List
     * @return
     * @throws Exception 
     */
    public String getProductAllocation() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> map  = new HashMap<String,Object>();
        ConvertUtil.setForm(form, map);
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 柜台信息
        CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);        
        map.put("BIN_OrganizationIDOut", counterInfo.getOrganizationId());
        
        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("T1.AllocationNoIF");
//        filterList.add("B.NameForeign");
//        filterList.add("G.NameForeign");
//        filterList.add("B.DepartCode");
//        filterList.add("B.DepartName");
//        filterList.add("G.DepartName");
//        filterList.add("G.DepartCode");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
        
        int count = binOLWSMNG05_BL.getProductAllocationCount(map);
        if(count>0){
            form.setProductAllocationList(binOLWSMNG05_BL.getProductAllocationList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLWSMNG05_06";
    }
    
//    /**
//     * 工作流中的各种动作入口方法
//     * @return
//     * @throws Exception
//     */
//    public String doaction() throws Exception{
//        try{
//            if(!validateDoaction()){
//                return CherryConstants.GLOBAL_ACCTION_RESULT;
//            }
//            String entryID = request.getParameter("entryid").toString();
//            String actionID = request.getParameter("actionid").toString();
////            form.setEntryID(entryID);
////            form.setActionID(actionID);
//            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//            
//            BINOLSTBIL18_Form form_STBIL18 = new BINOLSTBIL18_Form();
//            form_STBIL18.setEntryID(entryID);
//            form_STBIL18.setActionID(actionID);
//            form_STBIL18.setOperateType(form.getOperateType());
//            form_STBIL18.setOpComments(form.getOpComments());
//            
//            
//            
//            binOLSTBIL18_BL.tran_doaction(form_STBIL18,userInfo);
//            this.addActionMessage(getText("ICM00002")); 
//            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
//        }catch(Exception e){
//            if (e instanceof CherryException) {
//                CherryException temp = (CherryException) e;
//                this.addActionError(temp.getErrMessage());
//                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
//            }
//            throw e;
//        }
//    }
    
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
        // 开始日期
        String startDate = form.getStartDate();
        // 结束日期
        String endDate = form.getEndDate();
        /*开始日期验证*/
        if (startDate != null && !"".equals(startDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(startDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
                isCorrect = false;
            }
        }
        /*结束日期验证*/
        if (endDate != null && !"".equals(endDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(endDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
                isCorrect = false;
            }
        }
        if (isCorrect && startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
            // 开始日期在结束日期之后
            if(CherryChecker.compareDate(startDate, endDate) > 0) {
                this.addActionError(getText("ECM00019"));
                isCorrect = false;
            }
        }
        return isCorrect;
    }
    
    /**
     * 该明细画面展现模式
     * 1：明细查看模式
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
                    //如果当前登录者和下单员为同一人，则为非工作流的编辑模式，否则只能查看
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
    
    @Override
    public BINOLWSMNG05_Form getModel() {
        return form;
    }
}
