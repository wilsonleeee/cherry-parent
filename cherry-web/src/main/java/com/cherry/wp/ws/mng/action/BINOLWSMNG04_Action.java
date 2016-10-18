/*
 * @(#)BINOLWSMNG04_Action.java     1.0 2014/10/10
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL17_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL18_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.ios.form.BINOLSTIOS06_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS06_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.ws.mng.form.BINOLWSMNG04_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 调入Action
 * 
 * @author niushunjie
 * @version 1.0 2014.10.10
 */
@SuppressWarnings("unchecked")
public class BINOLWSMNG04_Action extends BaseAction implements ModelDriven<BINOLWSMNG04_Form> {

    private static final long serialVersionUID = -2768553189869928737L;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name ="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL binOLCM19_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLSTIOS06_BL")
    private BINOLSTIOS06_IF binOLSTIOS06_BL;
    
    @Resource(name="binOLSTBIL17_BL")
    private BINOLSTBIL17_IF binOLSTBIL17_BL;
    
    @Resource(name="binOLSTBIL18_BL")
    private BINOLSTBIL18_IF binOLSTBIL18_BL;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    /** 参数FORM */
    private BINOLWSMNG04_Form form = new BINOLWSMNG04_Form();
    
    /**
     * 调入画面初始化
     * 
     * @return 调入画面
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
        String inputStockState = binOLCM14_BL.getWebposConfigValue("9045", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
        if(null == inputStockState || "".equals(inputStockState)){
        	inputStockState = "Y";
		}
        form.setInputStockState(inputStockState);
        
        
        return SUCCESS;
    }

    /**
     * 查询一览
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
        // 取得调拨申请单总数
        int count = binOLSTBIL17_BL.searchProductAllocationCount(searchMap);
        if (count > 0) {
            // 取得调拨申请单List
            form.setProductAllocationList(binOLSTBIL17_BL.searchProductAllocationList(searchMap));
        }
        form.setSumInfo(binOLSTBIL17_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        //调入审核状态是否可见
        String inputStockState = binOLCM14_BL.getWebposConfigValue("9045", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
        if(null == inputStockState || "".equals(inputStockState)){
        	inputStockState = "Y";
		}
        form.setInputStockState(inputStockState);
        
        // AJAX返回至dataTable结果页面
        return "BINOLWSMNG04_01";
    }
    
    /**
     * 新增调入画面初始化
     * 
     * @return 订货画面
     * @throws Exception 
     */
    public String addBillInit() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        
        String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        //业务日期
        Map<String,Object> bussinessDateParamMap = new HashMap<String,Object>();
        bussinessDateParamMap.put("organizationInfoId", organizationInfoId);
        bussinessDateParamMap.put("brandInfoId", brandInfoId);
        String bussinessDate = binOLCM00_BL.getBussinessDate(bussinessDateParamMap);
        form.setBussinessDate(bussinessDate);
        
        //申请日期
        form.setApplyDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        
        //柜台信息
        CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
        String organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
        form.setInOrganizationID(organizationId);
        
        //柜台号
        form.setCounterCode(counterInfo.getCounterCode());
        
        //BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", organizationId);
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
        
        //实体仓库ID
        List<Map<String,Object>> departList = binOLCM18_BL.getDepotsByDepartID(organizationId, language);
        if(null == departList || departList.size()==0){
            form.setInDepotID("");
        }else{
            form.setInDepotID(ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID")));
        }
        
        //逻辑仓库ID
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_BrandInfoID", brandInfoId);
        praMap.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_BG);
        praMap.put("Type", "1");//终端
        praMap.put("ProductType", "1");
        praMap.put("language", "");
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
        if(null == logicList || logicList.size() == 0){
            form.setInLogicDepotID("");
        }else{
            form.setInLogicDepotID(ConvertUtil.getString(logicList.get(0).get("BIN_LogicInventoryInfoID")));
        }
        
      //配置项 产品入库使用价格（销售价格/会员价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        //操作人
        form.setEmployeeName(userInfo.getEmployeeName());
        
        form.setOperateType("newBill");
        return "BINOLWSMNG04_02";
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
        
        //调入方库存
        if(operateFlag.equals("2") || operateFlag.equals(CherryConstants.OPERATE_BG) || operateFlag.equals(CherryConstants.OPERATE_LG)){
            for(int i=0;i<detailList.size();i++){
                Map<String,Object> temp = detailList.get(i);
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                pram.put("FrozenFlag", "1");
                pram.put("BIN_DepotInfoID", CherryUtil.obj2int(temp.get("BIN_InventoryInfoID")));
                pram.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoID")));
                int orderStock = binOLCM20_BL.getProductStock(pram);
                temp.put("StockQuantity", orderStock);
            }
        }
        
        //调入确认
        if(operateFlag.equals(CherryConstants.OPERATE_BG)){
            long entryID = Long.parseLong(workFlowID);
            IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(entryID);
            
            int productAllocationOutID = ps.getInt("BIN_ProductAllocationOutID");
            //取调出单明细，替换调拨单的明细
            detailList = binOLSTCM16_BL.getProductAllocationOutDetailData(productAllocationOutID,language);
            
            Map<String,Object> productAllocationOutMainData = binOLSTCM16_BL.getProductAllocationOutMainData(productAllocationOutID, language);
            mainDataMap.put("TotalQuantity", productAllocationOutMainData.get("TotalQuantity"));
            mainDataMap.put("TotalAmount", productAllocationOutMainData.get("TotalAmount"));
        }
        
        if(!"".equals(workFlowID)){
            //存在调入单，显示调入数量
            long entryID = Long.parseLong(workFlowID);
            IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(entryID);
            Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.INT);
            if(null != propertyMap && !ConvertUtil.getString(propertyMap.get("BIN_ProductAllocationInID")).equals("")){
                mainDataMap.put("ShowInQuantity", "YES");
                int productAllocationInID = ps.getInt("BIN_ProductAllocationInID");
                Map<String,Object> productAllocationInMainData = binOLSTCM16_BL.getProductAllocationInMainData(productAllocationInID, language);
                mainDataMap.put("InTotalQuantity", productAllocationInMainData.get("TotalQuantity"));
                mainDataMap.put("InTotalAmount", productAllocationInMainData.get("TotalAmount"));
                List<Map<String,Object>> productAllocationInDetailData = binOLSTCM16_BL.getProductAllocationInDetailData(productAllocationInID, language);
                Map<String,Object> inQuantityMap = new HashMap<String,Object>();
                for(int i=0;i<productAllocationInDetailData.size();i++){
                    Map<String,Object> inDetailDTO = productAllocationInDetailData.get(i);
                    inQuantityMap.put(ConvertUtil.getString(inDetailDTO.get("BIN_ProductVendorID")), ConvertUtil.getString(inDetailDTO.get("Quantity")));
                }
                for(int i=0;i<detailList.size();i++){
                    Map<String,Object> applyDetailDTO = detailList.get(i);
                    int inQuantity = CherryUtil.obj2int(inQuantityMap.get(ConvertUtil.getString(applyDetailDTO.get("BIN_ProductVendorID"))));
                    applyDetailDTO.put("InQuantity", inQuantity);
                }
            }
        }
        
        form.setProductAllocationMainData(mainDataMap);
        form.setProductAllocationDetailData(detailList);
        
        //根据柜台的部门ID取出柜台号
        String counterDepartID = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationIDIn"));
        Map<String,Object> paramCounterMap = new HashMap<String,Object>();
        paramCounterMap.put("organizationId", counterDepartID);
        Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramCounterMap);
        if(null != counterInfo){
            form.setCounterCode(ConvertUtil.getString(counterInfo.get("counterCode")));
        }
        
        return "BINOLWSMNG04_02";
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
        map.put("allocationrNo", form.getAllocationrNo().trim());
        // 开始日期
        map.put("startDate", form.getStartDate());
        // 结束日期
        map.put("endDate", form.getEndDate());
        // 审核状态
        map.put("verifiedFlag", form.getVerifiedFlag());
        // 处理状态
        map.put("tradeStatus", form.getTradeStatus());
        // 员工ID
        map.put("employeeId", form.getEmployeeId());
        // 画面ID
        map.put("MENU_ID", "BINOLWSMNG04");
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
       
        return map;
    }

    /**
     * 保存产品调拨
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
            if (!validateSave()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationID(),"BINOLWSMNG04");
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            //把BINOLWSMNG04_From转成相应的from
            if(operateType.equals("2")){
                BINOLSTBIL18_Form form_STBIL18 = new BINOLSTBIL18_Form();
                form_STBIL18.setProductAllocationID(form.getProductAllocationID());
                form_STBIL18.setInventoryInfoIDIn(form.getInDepotID());
                form_STBIL18.setLogicInventoryInfoIDIn(form.getInLogicDepotID());
                form_STBIL18.setPrtVendorId(form.getProductVendorIDArr());
                form_STBIL18.setQuantityArr(form.getQuantityArr());
                form_STBIL18.setPriceUnitArr(form.getPriceUnitArr());
                form_STBIL18.setCommentsArr(form.getCommentsArr());
                binOLSTBIL18_BL.tran_save(form_STBIL18, userInfo);
            }else{
                BINOLSTIOS06_Form form_IOS06 = new BINOLSTIOS06_Form();
                form_IOS06.setTradeEmployeeID(form.getTradeEmployeeID());
                form_IOS06.setInOrganizationID(form.getInOrganizationID());
                form_IOS06.setInDepotID(form.getInDepotID());
                form_IOS06.setInLogicDepotID(form.getInLogicDepotID());
                form_IOS06.setOutOrganizationID(form.getOutOrganizationID());
                form_IOS06.setProductVendorIDArr(form.getProductVendorIDArr());
                form_IOS06.setQuantityArr(form.getQuantityArr());
                form_IOS06.setPriceUnitArr(form.getPriceUnitArr());
                form_IOS06.setCommentsArr(form.getCommentsArr());
                binOLSTIOS06_BL.tran_save(form_IOS06, userInfo);
            }
            
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }
    }
    
    /**
     * 提交产品调拨单
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        try {
            if (!validateSave()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLCM01_BL.completeUserInfo(userInfo,form.getInOrganizationID(),"BINOLSTIOS06");
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            //把BINOLWSMNG04_From转成相应的from
            if(operateType.equals("2")){
                BINOLSTBIL18_Form form_STBIL18 = new BINOLSTBIL18_Form();
                form_STBIL18.setProductAllocationID(form.getProductAllocationID());
                form_STBIL18.setInventoryInfoIDIn(form.getInDepotID());
                form_STBIL18.setLogicInventoryInfoIDIn(form.getInLogicDepotID());
                form_STBIL18.setPrtVendorId(form.getProductVendorIDArr());
                form_STBIL18.setQuantityArr(form.getQuantityArr());
                form_STBIL18.setPriceUnitArr(form.getPriceUnitArr());
                form_STBIL18.setCommentsArr(form.getCommentsArr());
                binOLSTBIL18_BL.tran_submit(form_STBIL18, userInfo);
            }else{
                BINOLSTIOS06_Form form_IOS06 = new BINOLSTIOS06_Form();
                form_IOS06.setTradeEmployeeID(form.getTradeEmployeeID());
                form_IOS06.setInOrganizationID(form.getInOrganizationID());
                form_IOS06.setInDepotID(form.getInDepotID());
                form_IOS06.setInLogicDepotID(form.getInLogicDepotID());
                form_IOS06.setOutOrganizationID(form.getOutOrganizationID());
                form_IOS06.setProductVendorIDArr(form.getProductVendorIDArr());
                form_IOS06.setQuantityArr(form.getQuantityArr());
                form_IOS06.setPriceUnitArr(form.getPriceUnitArr());
                form_IOS06.setCommentsArr(form.getCommentsArr());
                binOLSTIOS06_BL.tran_submit(form_IOS06, userInfo);
            }
            
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
     * 一览csv导出
     * @return
     * @throws Exception
     */
    public String exportCsv() throws Exception {
//        Map<String, Object> msgParam = new HashMap<String, Object>();
//        // 登陆用户信息
//        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//        try {
//            // 取得参数MAP
//            Map<String, Object> map = getSearchMap();
//            // 设置排序ID（必须）
//            map.put("SORT_ID", "changeDate desc");
//            // 语言
//            map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
//            // sessionId
//            map.put("sessionId", request.getSession().getId());
//            map.put("charset", form.getCharset());
//            
//            msgParam.put("TradeType", "exportMsg");
//            msgParam.put("SessionID", userInfo.getSessionID());
//            msgParam.put("LoginName", userInfo.getLoginName());
//            msgParam.put("OrgCode", userInfo.getOrgCode());
//            msgParam.put("BrandCode", userInfo.getBrandCode());
//            
//            int count = binOLMBPTM02_BL.getExportDetailCount(map);
//            int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
//            if(count > maxCount) {
//                // 明细数据量大于CSV导出最大数据量时给出提示
//                msgParam.put("exportStatus", "0");
//                msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
//            } else {
//                String tempFilePath = binOLMBPTM02_BL.exportCSV(map);
//                if(tempFilePath != null) {
//                    msgParam.put("exportStatus", "1");
//                    msgParam.put("message", getText("ECM00096"));
//                    msgParam.put("tempFilePath", tempFilePath);
//                } else {
//                    msgParam.put("exportStatus", "0");
//                    msgParam.put("message", getText("ECM00094"));
//                }
//            }
//        } catch(Exception e) {
//            logger.error(e.getMessage(), e);
//            msgParam.put("exportStatus", "0");
//            msgParam.put("message", getText("ECM00094"));
//        }
//        JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
        return null;
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
     * 验证数据
     */
    public boolean validateSave() throws Exception{
        boolean flag = true;
        if(null == form.getOutOrganizationID() || "".equals(form.getOutOrganizationID())){
            this.addActionError(getText("EST00037"));
            return false;
        }
        if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length == 0){
            this.addActionError(getText("EST00022"));
            return false;
        }
        for(int i=0;i<form.getProductVendorIDArr().length;i++){
            if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i])){
                this.addActionError(getText("EST00008"));
                return false;
            }
        }
        return flag;
    }
    
    /**
     * 该明细画面展现模式
     * 1：明细查看模式
     * 2 :  修改模式
     * 70 : 调入确认模式
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
    public BINOLWSMNG04_Form getModel() {
        return form;
    }
}
