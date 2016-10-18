/*
 * @(#)BINOLWSMNG07_Action.java     1.0 2015-10-29
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.cherry.st.bil.bl.BINOLSTBIL15_BL;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.ws.mng.form.BINOLWSMNG07_Form;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG07_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: BINOLWSMNG07_Action 
 * @Description: TODO(盘点申请Action) 
 * @author menghao
 * @version v1.0.0 2015-10-29 
 *
 */
public class BINOLWSMNG07_Action extends BaseAction implements ModelDriven<BINOLWSMNG07_Form> {

	private static final long serialVersionUID = 7785675178667810549L;
	
	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory.getLogger(BINOLWSMNG07_Action.class);

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
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;
    
    @Resource(name="binOLSTBIL16_BL")
    private BINOLSTBIL16_IF binOLSTBIL16_BL;
    
    @Resource(name="binOLSTBIL15_BL")
    private BINOLSTBIL15_BL binOLSTBIL15_BL;
    
    @Resource(name="binOLWSMNG07_BL")
    private BINOLWSMNG07_IF binOLWSMNG07_BL;
    
    @Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;

    /** 参数FORM */
    private BINOLWSMNG07_Form form = new BINOLWSMNG07_Form();
    
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
		
        // 根据盘点申请工作流文件定义，从code值为1238的盘点审核状态取出需要显示的审核状态，返回到画面上的Select框
        // 如果没有定义则取默认的审核状态（1007）
        List<Map<String,Object>> vfList = new ArrayList<Map<String,Object>>();
        vfList = codeTable.getCodes("1238");
        if(null == vfList || vfList.isEmpty()) {
        	vfList = codeTable.getCodes("1007");
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
        int count = binOLSTBIL15_BL.searchProStocktakeRequestCount(searchMap);
        if (count > 0) {
            // 取得盘点单List
            form.setTakingList(binOLSTBIL15_BL.searchProStocktakeRequestList(searchMap));
        }
        form.setSumInfo(binOLSTBIL15_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLWSMNG07_01";
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
        billID = CherryUtil.string2int(form.getProStocktakeRequestID());
        
        //取得盘点单概要信息 和详细信息
        Map<String,Object> mainDataMap = binOLSTCM14_BL.getProStocktakeRequestMainData(billID, language);
        String organizationInfoId = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainDataMap.get("BIN_BrandInfoID"));
        List<Map<String,Object>> detailList = binOLSTCM14_BL.getProStocktakeRequestDetailData(billID, language);
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        Map<String,Object> sumParam = new HashMap<String,Object>();
        sumParam.put("language", null);
        sumParam.put("billId", billID);
        // 根据明细来统计盘盈盘亏情况
        Map<String,Object> sumInfo = binOLSTBIL16_BL.getSumInfo(sumParam);
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
            // 盘点申请时填入的实盘数量
            int checkQuantity = CherryUtil.obj2int(temp.get("CheckQuantity"));
            if(operateFlag.equals("2")){
                Map<String,Object> pram = new HashMap<String,Object>();
                pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                pram.put("FrozenFlag", "1");
                pram.put("BIN_DepotInfoID", CherryUtil.obj2int(temp.get("BIN_InventoryInfoID")));
                pram.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoID")));
                int prtStock = binOLCM20_BL.getProductStock(pram);
                temp.put("BookQuantity", prtStock);
                // 实盘数量-库存数量=盘差
                temp.put("GainQuantity", checkQuantity-prtStock);
            }
            BigDecimal dcPrice = new BigDecimal(Double.parseDouble(ConvertUtil.getString(temp.get("Price"))));
            String handleType = ConvertUtil.getString(temp.get("HandleType"));
            if(!handleType.equals("0")){
                sumrealQuantity = sumrealQuantity.add(BigDecimal.valueOf(checkQuantity));
                sumrealAmount = sumrealAmount.add(BigDecimal.valueOf(checkQuantity).multiply(dcPrice));
            }
        }
        mainDataMap.put("SumrealQuantity", sumrealQuantity);
        mainDataMap.put("SumrealAmount", sumrealAmount);
        
        form.setTakingInfo(mainDataMap);
        form.setTakingReqDetailList(detailList);
        
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
        
        String stockType = ConvertUtil.getString(mainDataMap.get("StocktakeType"));
        if(stockType.equals("P1") || stockType.equals("P2")){
            form.setAddType("all");
        }
        
        return "BINOLWSMNG07_02";
    }
    
    /**
     * 校验当前柜台是否可以进行盘点申请
     * @throws Exception
     */
    public void checkAddBillInit() throws Exception{
        //柜台信息
        CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
        String organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationID", organizationId);
        List<Map<String,Object>> list =binOLWSMNG07_BL.getAuditBill(paramMap);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("AuditBillCount", 0);
        if(null != list && list.size()>0){
            resultMap.put("AuditBillCount", list.size());
        }
        ConvertUtil.setResponseByAjax(response, resultMap);
    }
    
    /**
     * 新增盘点申请画面初始化
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
        
        //该柜台存在未审核的盘点或退库申请，不能盘点
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("BIN_OrganizationID", organizationId);
        List<Map<String,Object>> list =binOLWSMNG07_BL.getAuditBill(paramMap);
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
        // 暂时先用盘点的业务类型来做盘点申请，后期再进行修改
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
        
        return "BINOLWSMNG07_02";
    }
    
    /**
     * 获取保存或者提交的相关参数
     * @return
     */
    private Map<String, Object> getComSubmitMap(UserInfo userInfo) {
         //柜台信息
         CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 
    	 //BAList
         Map<String,Object> paramBAMap = new HashMap<String,Object>();
         paramBAMap.put("organizationId", counterInfo.getOrganizationId());
         List<Map<String,Object>> baInfoList = binOLWPCM01_BL.getBAInfoList(paramBAMap);
         // 获取制单人员的岗位ID，提交单据需要有制单人员的岗位信息
         for(Map<String, Object> baInfoMap : baInfoList) {
        	 if(ConvertUtil.getString(baInfoMap.get("baInfoId")).equals(form.getTradeEmployeeID())) {
        		 map.put("positionCategoryID", baInfoMap.get("positionCategoryID"));
        		 break;
        	 }
         }
         //用户ID--同一柜台下的BA使用同一个云POS账号，用户ID都一样
         map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
         //组织ID
         map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
         //所属品牌ID
         map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
         // 部门CODE
         map.put("counterCode", counterInfo.getCounterCode());
         // 部门ID
         map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
         //实际做业务的员工
         map.put("TradeEmployeeID", form.getTradeEmployeeID());
         // 作成者为当前用户
         map.put("createdBy", userInfo.getBIN_UserID());
         // 作成程序名为当前程序
         map.put("createPGM", "BINOLWSMNG07");
         // 更新者为当前用户
         map.put("updatedBy", userInfo.getBIN_UserID());
         //盘点理由
         map.put("Comments", ConvertUtil.getString(form.getReason()));
         // 更新程序名为当前程序
         map.put("updatePGM", "BINOLWSMNG07");
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
         
         return map;
    }
    
    /**
     * 保存产品盘点申请
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
        	// 人员信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        	// 用于判断是暂存还是提交
            String operateType = ConvertUtil.getString(form.getOperateType());
            if(operateType.equals("2")){
                BINOLSTBIL16_Form form_STBIL16 = new BINOLSTBIL16_Form();
                form_STBIL16.setOperateType(form.getOperateType());
            	form_STBIL16.setProStocktakeRequestID(form.getProStocktakeRequestID());
            	form_STBIL16.setPrtVendorId(form.getProductVendorIDArr());
            	form_STBIL16.setBookQuantityArr(form.getBookCountArr());
                // 实盘数量
            	form_STBIL16.setCheckQuantityArr(form.getCheckQuantityArr());
            	form_STBIL16.setGainQuantityArr(form.getGainCountArr());
            	form_STBIL16.setPriceUnitArr(form.getPriceArr());
            	form_STBIL16.setHandleTypeArr(form.getHtArr());
            	form_STBIL16.setInventoryInfoID(form.getDepotId());
            	form_STBIL16.setLogicInventoryInfoID(form.getLogicinventId());
                form_STBIL16.setCommentsArr(form.getCommentsArr());
                form_STBIL16.setActionID("BINOLWSMNG07");
                // 更新盘点申请单
                binOLSTBIL16_BL.tran_save(form_STBIL16, userInfo);
            }else{
                Map<String, Object> map = this.getComSubmitMap(userInfo);
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
                String[] quantityArr = form.getCheckQuantityArr();
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
                // 新增一条盘点申请
                binOLWSMNG07_BL.tran_save(map,arrList, userInfo);
            }
            
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception ex){
        	logger.error(ex.getMessage(),ex);
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
     * 提交产品盘点申请单
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        try{
            //该柜台存在未审核的盘点或退库申请，不能盘点
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_OrganizationID", form.getOrganizationId());
            // 提交之前还需要进行一次校验，保证没有待审核的盘点申请单
            paramMap.put("submit", "1");
            List<Map<String,Object>> list =binOLWSMNG07_BL.getAuditBill(paramMap);
            if(null != list && list.size()>0){
                this.addActionError(getText("EST00038"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            // 人员信息
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            Map<String, Object> map = this.getComSubmitMap(userInfo);
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
            String[] quantityArr = form.getCheckQuantityArr();
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
            // 对于已经暂存过的盘点申请单，再次提交时参数里有单据ID
            map.put("proStocktakeRequestID", form.getProStocktakeRequestID());
            map.put("StockTakingNoIF", form.getStockTakingNoIF());
            binOLWSMNG07_BL.tran_submit(map,arrList, userInfo);
            
            this.addActionMessage(getText("ICM00002"));
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception ex){
        	logger.error(ex.getMessage(),ex);
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
    public void takingRequest() throws Exception{
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
        
        List<Map<String,Object>> allCntPrtStockList = binOLWSMNG07_BL.getAllCntPrtStockList(praMap);
        ConvertUtil.setResponseByAjax(response, allCntPrtStockList);
    }
    
    public void givenRequest() throws Exception{
    	Map<String,Object> praMap = new HashMap<String,Object>();
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        praMap.put(CherryConstants.ORGANIZATIONINFOID,organizationId);
        praMap.put(CherryConstants.BRANDINFOID,brandInfoId);
        praMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        
        //要盘点的实体仓库
        praMap.put("BIN_InventoryInfoID", form.getDepotId());
        //要盘点的逻辑仓库
        praMap.put("BIN_LogicInventoryInfoID", form.getLogicinventId());
        
        
        List<Map<String, Object>> givenCntPrtStockList = binOLWSMNG07_BL.getGivenCntPrtStockList(praMap);
        ConvertUtil.setResponseByAjax(response, givenCntPrtStockList);
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
        //业务类型(原始盘点申请单)
        map.put("tradeType", "CR");
        // 处理状态
        map.put("tradeStatus", "");
        // 员工ID
        map.put("employeeId", form.getEmployeeId());
        // 画面ID
        map.put("MENU_ID", "BINOLWSMNG07");
        
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
        //查看明细模式  按钮有【关闭】
        String ret="1";
        if(null==workFlowID||"".equals(workFlowID)){
            //当审核状态为审核通过时为operateFlag=1，查看明细模式
            String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
            if(CherryConstants.AUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)){
                // 如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
                String organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
            	if(ConvertUtil.getString(mainData.get("BIN_OrganizationID")).equals(organizationId)){
            		// 当前登录者所属部门为盘点申请部门即可进入编辑模式，否则只能查看
            		ret="2";
            	}
            }
        }      
        return ret;
    }
    
    @Override
    public BINOLWSMNG07_Form getModel() {
        return form;
    }
}
