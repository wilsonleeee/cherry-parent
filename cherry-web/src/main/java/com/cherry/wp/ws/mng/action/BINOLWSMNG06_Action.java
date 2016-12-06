/*
 * @(#)BINOLWSMNG06_Action.java     1.0 2014/10/20
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM20_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.bil.bl.BINOLSTBIL09_BL;
import com.cherry.st.bil.form.BINOLSTBIL10_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL10_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS05_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.ws.mng.form.BINOLWSMNG06_Form;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG06_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 自由盘点Action
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
@SuppressWarnings("unchecked")
public class BINOLWSMNG06_Action extends BaseAction implements ModelDriven<BINOLWSMNG06_Form> {

    private static final long serialVersionUID = -2677892288795624039L;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="CodeTable")
    private CodeTable codeTable;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_BL binOLCM19_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_BL binOLCM20_BL;
    
    @Resource(name="binOLSTIOS05_BL")
    private BINOLSTIOS05_IF binOLSTIOS05_BL;
    
//    @Resource(name="binOLSTBIL15_BL")
//    private BINOLSTBIL15_IF binOLSTBIL15_BL;
    
    @Resource(name="binOLSTCM06_BL")
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
//    @Resource(name="binOLSTBIL16_BL")
//    private BINOLSTBIL16_IF binOLSTBIL16_BL;
    
    @Resource(name="binOLSTBIL10_BL")
    private BINOLSTBIL10_IF binOLSTBIL10_BL;
    
//    @Resource(name="binOLSTCM14_BL")
//    private BINOLSTCM14_IF binOLSTCM14_BL;
    
    @Resource(name="binOLSTBIL09_BL")
    private BINOLSTBIL09_BL binOLSTBIL09_BL;
    
    @Resource(name="binOLWSMNG06_BL")
    private BINOLWSMNG06_IF binOLWSMNG06_BL;
    
    @Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;

    /** 参数FORM */
    private BINOLWSMNG06_Form form = new BINOLWSMNG06_Form();
    
    /**
     * 盘点一览画面初始化
     * 
     * @return 盘点一览画面
     * @throws Exception 
     */
    public String init() throws Exception {
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
        
        String organizationInfoCode = userInfo.getOrganizationInfoCode();
        String brandCode = userInfo.getBrandCode();
        
        // 根据盘点工作流文件定义，从code值为1322的盘点审核状态取出需要显示的审核状态，返回到画面上的Select框
        // 如果没有定义则取默认的审核状态（1007）
        String workFlowName = ConvertUtil.getWfName(organizationInfoCode, brandCode, "productStockTaking");
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workFlowName);
        Map<String, Object> metaAttributes = wd.getInitialAction(1).getMetaAttributes();
        List<Map<String,Object>> vfList = new ArrayList<Map<String,Object>>();
        vfList = codeTable.getCodes("1007");
        if (null != metaAttributes && !metaAttributes.isEmpty()) {
            String brandVerifiedFlagCA = ConvertUtil.getString(metaAttributes.get("BrandVerifiedFlagCA"));
            if (!brandVerifiedFlagCA.equals("")) {
                String[] showVFArr = brandVerifiedFlagCA.split("\\|");
                vfList = codeTable.getCodes("1322");
                Arrays.sort(showVFArr);
                for (int i = 0; i < vfList.size(); i++) {
                    Map<String,Object> codeMap = (Map<String,Object>) vfList.get(i);
                    String codeKey = ConvertUtil.getString(codeMap.get("CodeKey"));
                    int result = Arrays.binarySearch(showVFArr, codeKey);
                    //只保留在工作流里配置的审核状态
                    if (result < 0) {
                        vfList.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }
        form.setVerifiedFlagsCAList(vfList);
        
        return SUCCESS;
    }
    
    /**
     * 查询一览
     * @return
     * @throws Exception
     */
    public String search() throws Exception {
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得盘点单总数
        int count = binOLSTBIL09_BL.searchTakingCount(searchMap);
        if (count > 0) {
            // 取得盘点单List
            form.setTakingList(binOLSTBIL09_BL.searchTakingList(searchMap));
        }
        form.setSumInfo(binOLSTBIL09_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLWSMNG06_01";
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
        int billID = 0;
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        billID = CherryUtil.string2int(form.getStockTakingId());
        
        //取得盘点单概要信息 和详细信息
        Map<String,Object> mainDataMap = binOLSTCM06_BL.getStockTakingMainData(billID, language);
        String organizationInfoId = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainDataMap.get("BIN_BrandInfoID"));
        String detailOrderBy = binOLCM14_BL.getConfigValue("1092", organizationInfoId, brandInfoId);
        List<Map<String,Object>> detailList = binOLSTCM06_BL.getStockTakingDetailDataByOrder(billID, language,detailOrderBy);
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        //详细第一条调入仓库、调入逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("BIN_InventoryInfoID", detailList.get(0).get("BIN_InventoryInfoID"));
            mainDataMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        Map<String,Object> sumParam = new HashMap<String,Object>();
        sumParam.put("language", null);
        sumParam.put("billId", billID);
        Map<String,Object> sumInfo = binOLSTBIL10_BL.getSumInfo(sumParam);
        mainDataMap.put("Sumquantity", sumInfo.get("Sumquantity"));
        mainDataMap.put("SumrealQuantity", sumInfo.get("SumrealQuantity"));
        mainDataMap.put("SumrealAmount", sumInfo.get("SumrealAmount"));
        mainDataMap.put("ShortQuantity", sumInfo.get("ShortQuantity"));
        mainDataMap.put("ShortAmount", sumInfo.get("ShortAmount"));
        mainDataMap.put("OverQuantity", sumInfo.get("OverQuantity"));
        mainDataMap.put("OverAmount", sumInfo.get("OverAmount"));
        
        //取得是否盲盘
        String configValue = binOLCM14_BL.getConfigValue("1027", organizationInfoId, brandInfoId);
        if(ProductConstants.Blind_Close.equals(configValue)){
            form.setBlindFlag(ProductConstants.Blind_Close);
        }else{
            form.setBlindFlag(ProductConstants.Blind_Open);
        }
        
        BigDecimal sumrealQuantity = new BigDecimal(0);
        BigDecimal sumrealAmount = new BigDecimal(0);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            int realQuantity = CherryUtil.obj2int(temp.get("Quantity"))+CherryUtil.obj2int(temp.get("GainQuantity"));
            if(operateFlag.equals("2")){
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                pram.put("FrozenFlag", "1");
                pram.put("BIN_DepotInfoID", CherryUtil.obj2int(temp.get("BIN_InventoryInfoID")));
                pram.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoID")));
                int prtStock = binOLCM20_BL.getProductStock(pram);
                temp.put("StockQuantity", prtStock);
                temp.put("GainQuantity", realQuantity-prtStock);
            }
            BigDecimal dcPrice = new BigDecimal(Double.parseDouble(ConvertUtil.getString(temp.get("Price"))));
            String handleType = ConvertUtil.getString(temp.get("HandleType"));
            if(!handleType.equals("0")){
                sumrealQuantity = sumrealQuantity.add(BigDecimal.valueOf(realQuantity));
                sumrealAmount = sumrealAmount.add(BigDecimal.valueOf(realQuantity).multiply(dcPrice));
            }
        }
        mainDataMap.put("SumrealQuantity", sumrealQuantity);
        mainDataMap.put("SumrealAmount", sumrealAmount);
        
        form.setTakingInfo(mainDataMap);
        form.setTakingDetailList(detailList);
        
        //根据柜台的部门ID取出柜台号
        String counterDepartID = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationID"));
        Map<String,Object> paramCounterMap = new HashMap<String,Object>();
        paramCounterMap.put("organizationId", counterDepartID);
        Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramCounterMap);
        if(null != counterInfo){
            form.setCounterCode(ConvertUtil.getString(counterInfo.get("counterCode")));
        }
        
        //配置项 产品盘点使用价格（销售价格/会员价格）
        configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        String stockType = ConvertUtil.getString(mainDataMap.get("Type"));
        if(stockType.equals("P1") || stockType.equals("P2")){
            form.setAddType("all");
        }
        
        return "BINOLWSMNG06_02";
    }
    
    public void checkAddBillInit() throws Exception{
        //柜台信息
        CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
        String organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationID", organizationId);
        List<Map<String,Object>> list =binOLWSMNG06_BL.getAuditBill(paramMap);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("AuditBillCount", 0);
        if(null != list && list.size()>0){
            resultMap.put("AuditBillCount", list.size());
        }
        ConvertUtil.setResponseByAjax(response, resultMap);
    }
    
    /**
     * 新增盘点画面初始化
     * 
     * @return 盘点画面
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
        form.setOrganizationId(organizationId);
        
        //柜台号
        form.setCounterCode(counterInfo.getCounterCode());
        
        //BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", organizationId);
        List<Map<String,Object>> baInfoList = binOLWPCM01_BL.getBAInfoList(paramBAMap);
        form.setCounterBAList(baInfoList);
        
        //该柜台存在未审核的盘点或退库申请，不能盘点
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationID", organizationId);
        List<Map<String,Object>> list =binOLWSMNG06_BL.getAuditBill(paramMap);
        if(null != list && list.size()>0){
            this.addActionError(getText("EST00038"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        
        //实体仓库ID
        List<Map<String,Object>> departList = binOLCM18_BL.getDepotsByDepartID(organizationId, language);
        if(null == departList || departList.size()==0){
            form.setDepotId("");
        }else{
            form.setDepotId(ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID")));
        }
        
        //逻辑仓库ID
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_BrandInfoID", brandInfoId);
        praMap.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_CA);
        praMap.put("Type", "1");//终端
        praMap.put("ProductType", "1");
        praMap.put("language", "");
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
        if(null == logicList || logicList.size() == 0){
            form.setLogicinventId("");
        }else{
            form.setLogicinventId(ConvertUtil.getString(logicList.get(0).get("BIN_LogicInventoryInfoID")));
        }
        
        //取得是否盲盘
        String configValue = binOLCM14_BL.getConfigValue("1027", organizationInfoId, brandInfoId);
        if(ProductConstants.Blind_Close.equals(configValue)){
            form.setBlindFlag(ProductConstants.Blind_Close);
        }else{
            form.setBlindFlag(ProductConstants.Blind_Open);
        }
        
        //操作人
        form.setEmployeeName(userInfo.getEmployeeName());
        
        form.setOperateType("newBill");
        
        //配置项 产品盘点使用价格（销售价格/会员价格）
        configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoId, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        return "BINOLWSMNG06_02";
    }
    
    /**
     * 保存产品盘点
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            if(operateType.equals("2")){
                BINOLSTBIL10_Form form_STBIL10 = new BINOLSTBIL10_Form();
                form_STBIL10.setStockTakingId(form.getStockTakingId());
                form_STBIL10.setProductVendorIDArr(form.getProductVendorIDArr());
                form_STBIL10.setBookQuantityArr(form.getBookCountArr());
                form_STBIL10.setQuantityArr(form.getQuantityArr());
                form_STBIL10.setGainQuantityArr(form.getGainCountArr());
                form_STBIL10.setPriceUnitArr(form.getPriceArr());
                form_STBIL10.setHtArr(form.getHtArr());
                int length = form.getProductVendorIDArr().length;
                String[] inventoryInfoIDArr = new String[length];
                String[] logicInventoryInfoIDArr = new String[length];
                String[] productVendorPackageIDArr = new String[length];
                String[] storageLocationInfoIDArr = new String[length];
                for(int i=0;i<inventoryInfoIDArr.length;i++){
                    inventoryInfoIDArr[i] = form.getDepotId();
                    logicInventoryInfoIDArr[i] = form.getLogicinventId();
                    productVendorPackageIDArr[i] = "0";
                    storageLocationInfoIDArr[i] = "0";
                }
                form_STBIL10.setProductVendorPackageIDArr(productVendorPackageIDArr);
                // 主表仓库ID
                form_STBIL10.setDepotInfoID(form.getDepotId());
                form_STBIL10.setInventoryInfoIDArr(inventoryInfoIDArr);
                form_STBIL10.setLogicInventoryInfoIDArr(logicInventoryInfoIDArr);
                form_STBIL10.setStorageLocationInfoIDArr(storageLocationInfoIDArr);
                form_STBIL10.setCommentsArr(form.getCommentsArr());
                binOLSTBIL10_BL.tran_save(form_STBIL10, userInfo);
            }else{
                Map<String, Object> map = new HashMap<String, Object>();
                //用户ID
                map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
                //组织ID
                map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
                //所属品牌ID
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
                //部门ID
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("depotInfoId", form.getOrganizationId());
                map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
                //实际做业务的员工
                map.put("TradeEmployeeID", form.getTradeEmployeeID());
                // 作成者为当前用户
                map.put("createdBy", userInfo.getBIN_UserID());
                // 作成程序名为当前程序
                map.put("createPGM", "BINOLWSMNG06");
                // 更新者为当前用户
                map.put("updatedBy", userInfo.getBIN_UserID());
                //入库理由
                map.put("Comments", ConvertUtil.getString(form.getReason()));
                // 更新程序名为当前程序
                map.put("updatePGM", "BINOLWSMNG06");
                //是否盲盘
                map.put("blindFlag", form.getBlindFlag());
                //是否按批次盘点
                map.put("isBatchStockTaking", form.getIsBatchStockTaking());
                //实体仓库ID
                map.put("depotInfoId", form.getDepotId());
                //逻辑仓库ID
                map.put("logicInventoryInfoId", form.getLogicinventId());
                //自由盘点/商品盘点
                map.put("addType", form.getAddType());
                //产品厂商ID
                String[] productVendorIdArr = form.getProductVendorIDArr();
                
                //验证是否输入明细行
                if(null == productVendorIdArr || productVendorIdArr.length < 1){
                    this.addActionError(getText("EST00009"));
                    return CherryConstants.GLOBAL_ACCTION_RESULT;
                }
                
                //批次号
                String[] batchNoArr = new String[productVendorIdArr.length];
                for(int i=0;i<batchNoArr.length;i++){
                    batchNoArr[i] = "";
                }
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] commentsArr = form.getCommentsArr();
                //价格
                String[] priceArr = form.getPriceArr();
                //盘差
                String[] gainCountArr = form.getGainCountArr();
                //账面数量
                String[] bookCountArr = form.getBookCountArr();
                //盘点处理方式
                String[] htArr = form.getHtArr();
                List<String[]> arrList = new ArrayList<String[]>();
                arrList.add(productVendorIdArr);
                arrList.add(batchNoArr);
                arrList.add(quantityArr);
                arrList.add(commentsArr);
                arrList.add(priceArr);
                arrList.add(gainCountArr);
                arrList.add(bookCountArr);
                arrList.add(htArr);
                binOLSTIOS05_BL.tran_save(map,arrList, userInfo);
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
        try{
            //该柜台存在未审核的盘点或退库申请，不能盘点
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_OrganizationID", form.getOrganizationId());
            List<Map<String,Object>> list =binOLWSMNG06_BL.getAuditBill(paramMap);
            if(null != list && list.size()>0){
                this.addActionError(getText("EST00038"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            if(operateType.equals("2")){
                BINOLSTBIL10_Form form_STBIL10 = new BINOLSTBIL10_Form();
                form_STBIL10.setOperateType(form.getOperateType());
                form_STBIL10.setStockTakingId(form.getStockTakingId());
                form_STBIL10.setProductVendorIDArr(form.getProductVendorIDArr());
                form_STBIL10.setBookQuantityArr(form.getBookCountArr());
                form_STBIL10.setQuantityArr(form.getQuantityArr());
                form_STBIL10.setGainQuantityArr(form.getGainCountArr());
                form_STBIL10.setPriceUnitArr(form.getPriceArr());
                form_STBIL10.setHtArr(form.getHtArr());
                int length = form.getProductVendorIDArr().length;
                String[] inventoryInfoIDArr = new String[length];
                String[] logicInventoryInfoIDArr = new String[length];
                String[] productVendorPackageIDArr = new String[length];
                String[] storageLocationInfoIDArr = new String[length];
                for(int i=0;i<inventoryInfoIDArr.length;i++){
                    inventoryInfoIDArr[i] = form.getDepotId();
                    logicInventoryInfoIDArr[i] = form.getLogicinventId();
                    productVendorPackageIDArr[i] = "0";
                    storageLocationInfoIDArr[i] = "0";
                }
                form_STBIL10.setProductVendorPackageIDArr(productVendorPackageIDArr);
                // 主表仓库ID
                form_STBIL10.setDepotInfoID(form.getDepotId());
                form_STBIL10.setInventoryInfoIDArr(inventoryInfoIDArr);
                form_STBIL10.setLogicInventoryInfoIDArr(logicInventoryInfoIDArr);
                form_STBIL10.setStorageLocationInfoIDArr(storageLocationInfoIDArr);
                form_STBIL10.setCommentsArr(form.getCommentsArr());
                binOLSTBIL10_BL.tran_submit(form_STBIL10, userInfo);
            }else{
                Map<String, Object> map = new HashMap<String, Object>();
                //用户ID
                map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
                //组织ID
                map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
                //所属品牌ID
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
                //部门ID
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("depotInfoId", form.getOrganizationId());
                map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
                //实际做业务的员工
                map.put("TradeEmployeeID", form.getTradeEmployeeID());
                // 作成者为当前用户
                map.put("createdBy", userInfo.getBIN_UserID());
                // 作成程序名为当前程序
                map.put("createPGM", "BINOLWSMNG06");
                // 更新者为当前用户
                map.put("updatedBy", userInfo.getBIN_UserID());
                //盘点理由
                map.put("Comments", ConvertUtil.getString(form.getReason()));
                // 更新程序名为当前程序
                map.put("updatePGM", "BINOLWSMNG06");
                //是否盲盘
                map.put("blindFlag", form.getBlindFlag());
                //是否按批次盘点
                map.put("isBatchStockTaking", form.getIsBatchStockTaking());
                //实体仓库ID
                map.put("depotInfoId", form.getDepotId());
                //逻辑仓库ID
                map.put("logicInventoryInfoId", form.getLogicinventId());
                //自由盘点/商品盘点
                map.put("addType", form.getAddType());
                //产品厂商ID
                String[] productVendorIdArr = form.getProductVendorIDArr();
                
                //验证是否输入明细行
                if(null == productVendorIdArr || productVendorIdArr.length < 1){
                    this.addActionError(getText("EST00009"));
                    return CherryConstants.GLOBAL_ACCTION_RESULT;
                }
                
                //批次号
                String[] batchNoArr = new String[productVendorIdArr.length];
                for(int i=0;i<batchNoArr.length;i++){
                    batchNoArr[i] = "";
                }
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] commentsArr = form.getCommentsArr();
                //价格
                String[] priceArr = form.getPriceArr();
                //盘差
                String[] gainCountArr = form.getGainCountArr();
                //账面数量
                String[] bookCountArr = form.getBookCountArr();
                //盘点处理方式
                String[] htArr = form.getHtArr();
                List<String[]> arrList = new ArrayList<String[]>();
                arrList.add(productVendorIdArr);
                arrList.add(batchNoArr);
                arrList.add(quantityArr);
                arrList.add(commentsArr);
                arrList.add(priceArr);
                arrList.add(gainCountArr);
                arrList.add(bookCountArr);
                arrList.add(htArr);
                binOLSTIOS05_BL.tran_submit(map,arrList, userInfo);
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
     * 开始盘点
     * @return
     * @throws Exception 
     */
    public void stockTaking() throws Exception{
        Map<String,Object> praMap = new HashMap<String,Object>();
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        praMap.put(CherryConstants.ORGANIZATIONINFOID,organizationId);
        praMap.put(CherryConstants.BRANDINFOID,brandInfoId);
        praMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        //要盘点的部门
        praMap.put("BIN_OrganizationID", form.getOrganizationId());
        //要盘点的实体仓库
        praMap.put("BIN_InventoryInfoID", form.getDepotId());
        //要盘点的逻辑仓库
        praMap.put("BIN_LogicInventoryInfoID", form.getLogicinventId());
        
        List<Map<String,Object>> allCntPrtStockList = binOLWSMNG06_BL.getAllCntPrtStockList(praMap);
        ConvertUtil.setResponseByAjax(response, allCntPrtStockList);
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
        map.put("stockTakingNo", form.getStockTakingNo().trim());
        // 开始日期
        map.put("startDate", form.getStartDate());
        // 结束日期
        map.put("endDate", form.getEndDate());
        // 审核状态
        map.put("verifiedFlag", form.getVerifiedFlag());
        // 处理状态
        map.put("tradeStatus", "");
        // 员工ID
        map.put("employeeId", form.getEmployeeId());
        // 画面ID
        map.put("MENU_ID", "BINOLWSMNG06");
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
       
        return map;
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
        }
        return ret;
    }
    
    @Override
    public BINOLWSMNG06_Form getModel() {
        return form;
    }
}
