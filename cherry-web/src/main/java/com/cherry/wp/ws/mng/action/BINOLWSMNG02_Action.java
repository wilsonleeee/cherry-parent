package com.cherry.wp.ws.mng.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.bil.action.BINOLSTBIL01_Action;
import com.cherry.st.bil.interfaces.BINOLSTBIL02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.common.interfaces.BINOLSTCM15_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS01_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.ws.mng.form.BINOLWSMNG02_Form;
import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG02_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWSMNG02_Action extends BaseAction implements ModelDriven<BINOLWSMNG02_Form> {
	
    private static final long serialVersionUID = -3182073300524090826L;

    /**异常处理*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTBIL01_Action.class);
    
    @Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLCM20_BL")
    private BINOLCM20_IF binOLCM20_BL;
    
    @Resource(name="binOLWSMNG02_BL")
    private BINOLWSMNG02_IF binOLWSMNG02_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
    
    @Resource(name="binOLSTCM08_BL")
    private BINOLSTCM08_IF binOLSTCM08_BL;
    
    @Resource(name="binOLSTCM11_BL")
    private BINOLSTCM11_IF binOLSTCM11_BL;
    
    @Resource(name="binOLSTCM15_BL")
    private BINOLSTCM15_IF binOLSTCM15_BL;
    
    @Resource(name="binOLSTIOS01_BL")
    private BINOLSTIOS01_IF binOLSTIOS01_BL;
    
//    @Resource(name="binOLSTBIL01_BL")
//    private BINOLSTBIL01_IF binOLSTBIL01_BL;
    
    @Resource(name="binOLSTBIL02_BL")
    private BINOLSTBIL02_IF binOLSTBIL02_BL;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
    
    @Resource(name="binOLWPCM01_BL")
    private BINOLWPCM01_IF binOLWPCM01_BL;
    
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
    
    public String init() throws JSONException{
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
        
        //入库/收货部门
        form.setInOrganizationID(ConvertUtil.getString(counterInfo.getOrganizationId()));

        return SUCCESS;
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @param tableParamsDTO
     */
    private Map<String, Object> getSearchMap() throws Exception{
//        // 开始日
//        map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
//        // 结束日
//        map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
        
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
        map.put("billNoIF", form.getBillNoIF().trim());
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
        map.put("MENU_ID", "BINOLWSMNG02");
        //选中入库单据ID Arr
        map.put("checkedBillIdArrGR", form.getCheckedBillIdArrGR());
        //选中收货单据ID Arr
        map.put("checkedBillIdArrRD", form.getCheckedBillIdArrRD());
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        
        return map;
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
        // 取得入库/收货单总数
        int count = binOLWSMNG02_BL.getPrtGRRDCount(searchMap);
        if (count > 0) {
            // 取得入库/收货单List
            form.setPrtInDepotList(binOLWSMNG02_BL.getPrtGRRDList(searchMap));
        }
        form.setSumInfo(binOLWSMNG02_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLWSMNG02_01";
    }
    
    /**
     * 新增入库画面初始化
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
            form.setInInventoryInfoID("");
        }else{
            form.setInInventoryInfoID(ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID")));
        }
        
        //逻辑仓库ID
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_BrandInfoID", brandInfoId);
        praMap.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_GR);
        praMap.put("Type", "1");//终端
        praMap.put("ProductType", "1");
        praMap.put("language", "");
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(praMap);
        if(null == logicList || logicList.size() == 0){
            form.setInLogicInventoryInfoID("");
        }else{
            form.setInLogicInventoryInfoID(ConvertUtil.getString(logicList.get(0).get("BIN_LogicInventoryInfoID")));
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
        return "BINOLWSMNG02_02";
    }
    
    /**
     * <p>
     * 入库/收货单画面初期显示
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
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        billID = CherryUtil.string2int(form.getProductInDepotId());
        
        String tradeType = ConvertUtil.getString(form.getTradeType());
         
        //取得入库单概要信息 和详细信息
        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        if(tradeType.equals("GR")){
            mainDataMap = binOLSTCM08_BL.getProductInDepotMainData(billID,language);
            detailList = binOLSTCM08_BL.getProductInDepotDetailData(billID,language);
            
            //根据柜台的部门ID取出柜台号
            String counterDepartID = ConvertUtil.getString(mainDataMap.get("BIN_OrganizationID"));
            Map<String,Object> paramCounterMap = new HashMap<String,Object>();
            paramCounterMap.put("organizationId", counterDepartID);
            Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramCounterMap);
            if(null != counterInfo){
                form.setCounterCode(ConvertUtil.getString(counterInfo.get("counterCode")));
            }
        }else if(tradeType.equals("RD")){
            mainDataMap = binOLSTCM11_BL.getProductReceiveMainData(billID,language);
            detailList = binOLSTCM11_BL.getProductReceiveDetailData(billID,language);
            
            mainDataMap.put("InDepotDate", mainDataMap.get("ReceiveDate"));
        }

        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainDataMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainDataMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);

        //详细第一条仓库、逻辑仓库设置到主单
        if(null != detailList && detailList.size()>0){
            mainDataMap.put("BIN_InventoryInfoID", detailList.get(0).get("BIN_InventoryInfoID"));
            mainDataMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        //入库方库存
        if(operateFlag.equals("2")){
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
        
        form.setInDepotMainMap(mainDataMap);
        form.setInDepotDetailList(detailList);
        
        //配置项 产品入库使用价格（销售价格/会员价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", ConvertUtil.getString(mainDataMap.get("BIN_OrganizationInfoID")), brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        return "BINOLWSMNG02_02";
    }
    
    /**
     * 保存产品入库
     * @return
     * @throws Exception 
     */
    public String save() throws Exception{
        try{
            if (!validateSave()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            if(operateType.equals("2")){
                Map<String,Object> sessionMap = new HashMap<String,Object>();
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
                sessionMap.put("CreatePGM", "BINOLWSMNG02");
                sessionMap.put("UpdatePGM", "BINOLWSMNG02");
                //用户信息
                sessionMap.put("UserInfo", userInfo);
                sessionMap.put("OpComments",form.getOpComments());
                
                Map<String,Object> mainData = new HashMap<String,Object>();
                //实体仓库
                mainData.put("depotInfoId", form.getInInventoryInfoID());
                //逻辑仓库
                mainData.put("logicInventoryInfoId", form.getInLogicInventoryInfoID());
                //入库主记录ID
                mainData.put("BIN_ProductInDepotID", form.getProductInDepotId());
                //上次更新时间
                mainData.put("OldUpdateTime", form.getUpdateTime());
                //更新次数
                mainData.put("OldModifyCount", form.getModifyCount());
                
                List<String[]> detailList = new ArrayList<String[]>();
                //产品厂商ID
                String[] productVendorIDArr = form.getProductVendorIDArr();
                //产品批次号
                String[] batchNoArr = new String[form.getProductVendorIDArr().length];
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] commentsArr = form.getCommentsArr();
                //价格
                String[] priceArr = form.getPriceUnitArr();
                //参考价
                String[] referencePriceArr = form.getPriceUnitArr();
                detailList.add(productVendorIDArr);
                detailList.add(batchNoArr);
                detailList.add(quantityArr);
                detailList.add(commentsArr);
                detailList.add(priceArr);
                detailList.add(referencePriceArr);
                
                binOLSTBIL02_BL.tran_save(sessionMap, mainData, detailList);
            }else{   
                Map<String,Object> map = new HashMap<String,Object>();
                //用户ID
                map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
                //组织ID
                map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
                //所属品牌ID
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
                //部门ID
                map.put(CherryConstants.ORGANIZATIONID, form.getInOrganizationID());
                //实际做业务的员工
                map.put("TradeEmployeeID", form.getTradeEmployeeID());
                // 作成者为当前用户
                map.put("createdBy", userInfo.getBIN_UserID());
                // 作成程序名为当前程序
                map.put("createPGM", "BINOLWSMNG02");
                // 更新者为当前用户
                map.put("updatedBy", userInfo.getBIN_UserID());
                //入库理由
                map.put("comments", form.getReason());
                // 更新程序名为当前程序
                map.put("updatePGM", "BINOLWSMNG02");
                //实体仓库ID
                map.put("depotInfoId", form.getInInventoryInfoID());
                //逻辑仓库ID
                map.put("logicInventoryInfoId", form.getInLogicInventoryInfoID());
                //往来单位
                map.put("bussinessPartnerId", null);
                //入库日期
                map.put("inDepotDate", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
                //产品厂商ID
                String[] productVendorIdArr = form.getProductVendorIDArr();
                //批次号
                String[] batchNoArr = new String[form.getProductVendorIDArr().length];
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] reasonArr = form.getCommentsArr();
                //执行价
                String[] priceArr = form.getPriceUnitArr();
                //参考价
                String[] referencePriceArr = form.getPriceUnitArr();
                List<String[]> arrList = new ArrayList<String[]>();
                arrList.add(productVendorIdArr);
                arrList.add(batchNoArr);
                arrList.add(quantityArr);
                arrList.add(reasonArr);
                arrList.add(priceArr);
                arrList.add(referencePriceArr);
                binOLSTIOS01_BL.tran_save(map, arrList, userInfo);
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
     * 提交产品入库单
     * @return
     * @throws Exception 
     */
    public String submit() throws Exception{
        try{
            if (!validateSave()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            String operateType = ConvertUtil.getString(form.getOperateType());
            if(operateType.equals("2")){
                Map<String,Object> sessionMap = new HashMap<String,Object>();
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
                sessionMap.put("CreatePGM", "BINOLWSMNG02");
                sessionMap.put("UpdatePGM", "BINOLWSMNG02");
                //用户信息
                sessionMap.put("UserInfo", userInfo);
                sessionMap.put("OpComments",form.getOpComments());
                sessionMap.put("WEBPOS_SUBMIT", "YES");
                
                Map<String,Object> mainData = new HashMap<String,Object>();
                //实体仓库
                mainData.put("depotInfoId", form.getInInventoryInfoID());
                //逻辑仓库
                mainData.put("logicInventoryInfoId", form.getInLogicInventoryInfoID());
                //入库主记录ID
                mainData.put("BIN_ProductInDepotID", form.getProductInDepotId());
                //上次更新时间
                mainData.put("OldUpdateTime", form.getUpdateTime());
                //更新次数
                mainData.put("OldModifyCount", form.getModifyCount());
                
                List<String[]> detailList = new ArrayList<String[]>();
                //产品厂商ID
                String[] productVendorIDArr = form.getProductVendorIDArr();
                //产品批次号
                String[] batchNoArr = new String[form.getProductVendorIDArr().length];
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] commentsArr = form.getCommentsArr();
                //价格
                String[] priceArr = form.getPriceUnitArr();
                //参考价
                String[] referencePriceArr = form.getPriceUnitArr();
                detailList.add(productVendorIDArr);
                detailList.add(batchNoArr);
                detailList.add(quantityArr);
                detailList.add(commentsArr);
                detailList.add(priceArr);
                detailList.add(referencePriceArr);
                
                binOLSTBIL02_BL.tran_submit(sessionMap, mainData, detailList);
            }else{   
                Map<String,Object> map = new HashMap<String,Object>();
                //用户ID
                map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
                //组织ID
                map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
                //所属品牌ID
                map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
                //部门ID
                map.put(CherryConstants.ORGANIZATIONID, form.getInOrganizationID());
                //实际做业务的员工
                map.put("TradeEmployeeID", form.getTradeEmployeeID());
                // 作成者为当前用户
                map.put("createdBy", userInfo.getBIN_UserID());
                // 作成程序名为当前程序
                map.put("createPGM", "BINOLWSMNG02");
                // 更新者为当前用户
                map.put("updatedBy", userInfo.getBIN_UserID());
                //入库理由
                map.put("comments", form.getReason());
                // 更新程序名为当前程序
                map.put("updatePGM", "BINOLWSMNG02");
                //实体仓库ID
                map.put("depotInfoId", form.getInInventoryInfoID());
                //逻辑仓库ID
                map.put("logicInventoryInfoId", form.getInLogicInventoryInfoID());
                //往来单位
                map.put("bussinessPartnerId", null);
                //入库日期
                map.put("inDepotDate", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
                //由云POS提交
                map.put("WEBPOS_SUBMIT", "YES");
                //产品厂商ID
                String[] productVendorIdArr = form.getProductVendorIDArr();
                //批次号
                String[] batchNoArr = new String[form.getProductVendorIDArr().length];
                //数量
                String[] quantityArr = form.getQuantityArr();
                //备注
                String[] reasonArr = form.getCommentsArr();
                //执行价
                String[] priceArr = form.getPriceUnitArr();
                //参考价
                String[] referencePriceArr = form.getPriceUnitArr();
                List<String[]> arrList = new ArrayList<String[]>();
                arrList.add(productVendorIdArr);
                arrList.add(batchNoArr);
                arrList.add(quantityArr);
                arrList.add(reasonArr);
                arrList.add(priceArr);
                arrList.add(referencePriceArr);
                binOLSTIOS01_BL.tran_submit(map, arrList, userInfo);
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
     * 入库单一览明细导出
     * @return
     */
    public String export() {
        // 取得参数MAP 
        try {
            Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTBIL01", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLWSMNG02_BL.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                this.addActionError(((CherryException)e).getErrMessage());
            }else{
                this.addActionError(getText("EMO00022"));
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
    
    /**
     * 取得发货单List
     * @return
     * @throws Exception 
     */
    public String getDeliver() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> map  = new HashMap<String,Object>();
        ConvertUtil.setForm(form, map);
        map.put("BIN_OrganizationIDReceive", form.getInOrganizationID());
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        
        List<String> filterList = new ArrayList<String>();
        // 需要过滤的字段名
        filterList.add("T1.DeliverNoIF");
//        filterList.add("B.NameForeign");
//        filterList.add("G.NameForeign");
//        filterList.add("B.DepartCode");
//        filterList.add("B.DepartName");
//        filterList.add("G.DepartName");
//        filterList.add("G.DepartCode");
        map.put(CherryConstants.FILTER_LIST_NAME, filterList);
        
        int count = binOLWSMNG02_BL.getDeliverCount(map);
        if(count>0){
            form.setDeliverList(binOLWSMNG02_BL.getDeliverList(map));
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "BINOLWSMNG02_06";
    }
    
//    /**
//     * 查询可收货及已收货的单据
//     * @return
//     * @throws Exception
//     */
//    public String searchDeliver() throws Exception{
//        // 验证提交的参数
//        if (!validateForm()) {
//            return CherryConstants.GLOBAL_ACCTION_RESULT;
//        }
//        // 用户信息
//        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
//        // 取得参数MAP
//        Map<String, Object> searchMap = getSearchMap();
//        String userID = ConvertUtil.getString(userInfo.getBIN_UserID());
//        String positionID= ConvertUtil.getString(userInfo.getBIN_PositionCategoryID());
//        String organizationID = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
//        searchMap.put(CherryConstants.OS_ACTOR+"ID", CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RD);
//        searchMap.put("LoginUserID", userID);
//        searchMap.put("LoginPositionID", positionID);
//        searchMap.put("LoginOrganizationID", organizationID);
//        
//        // 取得发货单汇总信息
//        Map<String, Object> sumInfo = binOLWSMNG02_BL.getSumInfo(searchMap);
//        form.setSumInfo(sumInfo);
//        int count = (Integer)sumInfo.get("count");
//        // form表单设置
//        form.setITotalDisplayRecords(count);
//        form.setITotalRecords(count);
//        if (count > 0) {
//            // 取得发货单信息List
//            List<Map<String,Object>> deliverList = binOLWSMNG02_BL.getProductDeliverList(searchMap);
//            form.setDeliverList(deliverList);
//        }
//        // AJAX返回至dataTable结果页面
//        return SUCCESS;
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
        if (isCorrect && startDate != null && !"".equals(startDate)&& 
                endDate != null && !"".equals(endDate)) {
            // 开始日期在结束日期之后
            if(CherryChecker.compareDate(startDate, endDate) > 0) {
                this.addActionError(getText("ECM00019"));
                isCorrect = false;
            }
        }
        return isCorrect;
    }
    
    /**
	 * 收货画面初期显示
	 * @author zhanggl
	 * @version 1.0 2011.11.04
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String detailInit() throws Exception {
		int productDeliverID = CherryUtil.string2int(form.getProductDeliverId());
	    
	    String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		//取得发货单概要信息 和详细信息
		Map<String,Object> mainMap = binOLSTCM03_BL.getProductDeliverMainData(productDeliverID,language);
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
        
		Map<String,Object> otherParam = new HashMap<String,Object>();
        otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
        otherParam.put("BIN_BrandInfoID", brandInfoId);
		List<Map<String,Object>> detailList = binOLSTCM03_BL.getProductDeliverDetailData(productDeliverID,language,otherParam);		
        
		
        Map<String,Object> paramPrivilege = new HashMap<String,Object>();
        paramPrivilege.put("userInfo", userInfo);
        paramPrivilege.put("mainData", mainMap);
        String showRecStockFlag = binOLSTCM15_BL.getShowRecStockFlag(paramPrivilege);
        mainMap.put("showRecStockFlag", showRecStockFlag);
		form.setProductDeliverMainData(mainMap);
		
	    //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);
		
        //BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", mainMap.get("BIN_OrganizationIDReceive"));
        List<Map<String,Object>> baInfoList = binOLWPCM01_BL.getBAInfoList(paramBAMap);
        form.setCounterBAList(baInfoList);
        
        int defaultDepotInfoID = 0;
		int defaultLogicInventoryInfoID = 0;
		
		//TODO:工作流的审核以及编辑以及非工作流的编辑模式下 明细里面要带上库存，并且还要考虑已冻结库存
        if (CherryConstants.OPERATE_SD_AUDIT.equals(operateFlag)
                || CherryConstants.OPERATE_SD_AUDIT2.equals(operateFlag)
                || CherryConstants.OPERATE_SD_EDIT.equals(operateFlag)
                || "2".equals(operateFlag)
                || CherryConstants.OPERATE_OD_AUDIT_SEC.equals(operateFlag)
                || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag)
                || CherryConstants.OPERATE_OD_AUDIT.equals(operateFlag)
                || CherryConstants.OPERATE_SD.equals(operateFlag)) {
            List<Map<String,Object>> orderDetailList = new ArrayList<Map<String,Object>>();
            Map<String,Object> orderMainData = new HashMap<String,Object>();
            if (CherryConstants.OPERATE_OD_AUDIT_SEC.equals(operateFlag)
                    || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag)
                    || CherryConstants.OPERATE_OD_AUDIT.equals(operateFlag)
                    || CherryConstants.OPERATE_SD.equals(operateFlag)) {
                PropertySet propertyset = workflow.getPropertySet(Long.parseLong(workFlowID));
                //逻辑仓库的业务类型，订发货流程为接收订货OD，发货流程为发货SD。
                String logicBusinessType = CherryConstants.LOGICDEPOT_BACKEND_OD;
                if(propertyset.getKeys("BIN_ProductOrderID").size() > 0){
                    int productOrderID = propertyset.getInt("BIN_ProductOrderID");
                    orderMainData = binOLSTCM02_BL.getProductOrderMainData(productOrderID, language);
                    orderDetailList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID, language, null);
                    
                    form.getProductDeliverMainData().put("TestType", orderMainData.get("TestType"));
                    form.getProductDeliverMainData().put("WorkFlowCode", "proFlowOD");
                }else{
                    logicBusinessType = CherryConstants.LOGICDEPOT_BACKEND_SD;
                    form.getProductDeliverMainData().put("TestType", mainMap.get("InTestType"));
                }
                
//                //取得发货实体仓库List
//                String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationID"));
//                List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
//                form.setDepotsInfoList(depotsInfoList);
//                
//                //取得发货逻辑仓库List
//                Map<String,Object> logicPram =  new HashMap<String,Object>();
//                logicPram.put("BIN_BrandInfoID", brandInfoId);
////                logicPram.put("BusinessType", CherryConstants.OPERATE_OD);
//                logicPram.put("BusinessType", logicBusinessType);
//                logicPram.put("ProductType", "1");
//                logicPram.put("Type", "0");
//                logicPram.put("language", language);
////                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
//                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
//                form.setLogicDepotsInfoList(logicDepotsList);
            }
		    for(int i=0;i<detailList.size();i++){
	            Map<String,Object> temp = detailList.get(i);
	            Map<String,Object> pram = new HashMap<String,Object>();
	            pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
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
	            pram.put("FrozenFlag", "2");
	            if(operateFlag.equals(CherryConstants.OPERATE_SD)){
	                //发货步骤显示发货方实际库存
	                pram.put("FrozenFlag", "1");
	            }
	            int productQuantity = binOLCM20_BL.getProductStock(pram);
	            temp.put("ProductQuantity", productQuantity);
	            //订货单一审二审查询订货数量
	            if(CherryConstants.OPERATE_OD_AUDIT_SEC.equals(operateFlag) || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag) || CherryConstants.OPERATE_OD_AUDIT.equals(operateFlag)){
	                int orderQuantity =0;
	                int productVendorID = CherryUtil.obj2int(temp.get("BIN_ProductVendorID"));
	                for(int j=0;j<orderDetailList.size();j++){
	                    int curProductVendorID = CherryUtil.obj2int(orderDetailList.get(j).get("BIN_ProductVendorID"));
	                    if(curProductVendorID == productVendorID){
	                        orderQuantity =  CherryUtil.obj2int(orderDetailList.get(j).get("Quantity"));
	                        break;
	                    }
	                }
	                temp.put("OrderQuantity", orderQuantity);
	            }
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
		form.setProductDeliverDetailData(detailList);
		
		//判断是否是要进行产品收货,如是就取得收货部门的实体仓库和逻辑仓库
		if(CherryConstants.OPERATE_RD.equals(operateFlag)){
			//取得收货部门实体仓库
			String departID = String.valueOf(mainMap.get("BIN_OrganizationIDReceive"));
			form.setReceiveDepotList(binOLCM18_BL.getDepotsByDepartID(departID, language));
			
			//取得逻辑仓库
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			//终端逻辑仓库
			paramMap.put("Type", "1");
			paramMap.put("language", language);
			paramMap.put("BusinessType", CherryConstants.LOGICDEPOT_TERMINAL_GR);
			paramMap.put("ProductType", "1");
			form.setReceiveLogiInvenList(binOLCM18_BL.getLogicDepotByBusiness(paramMap));
		}
        
        //配置项 产品发货使用价格（销售价格/会员价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
		
		return SUCCESS;
	}
	
	/**
	 * 目前该明细画面有五种展现模式
	 * 1：明细查看模式
	 * 2：非工作流的编辑模式
	 * 40：工作流发货模式
	 * 41：工作流审核模式
	 * 42：工作流编辑模式
	 * 50: 工作流收货模式
	 * 32: 订单工作流发货单修改模式
	 * 34: 订单工作流订单二审模式 模式
	 * @author zhanggl
	 * @version 1.0 2011.11.04
	 * 
	 * @param workFlowID 工作流ID
	 * @param mainData 主单数据
	 * 
	 * @return
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
				if (CherryConstants.OPERATE_SD_AUDIT.equals(currentOperation)) {
					//发货单审核模式   按钮有【通过】【退回】【关闭】
					request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if (CherryConstants.OPERATE_SD_AUDIT2.equals(currentOperation)) {
                    //发货单二审模式   按钮有【通过】【退回】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
                }
				else if (CherryConstants.OPERATE_SD_EDIT.equals(currentOperation)) {
					//工作流中的编辑模式  按钮有【保存】【提交】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if(CherryConstants.OPERATE_SD.equals(currentOperation)){
                    //工作流中的发货模式  按钮有【发货】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}else if (CherryConstants.OPERATE_RD.equals(currentOperation)) {
					//工作流中的收货模式  按钮有【收货】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if(CherryConstants.OPERATE_OD_AUDIT_SEC.equals(currentOperation)){
                    //订单工作流中的二审模式  按钮有【同意】【拒绝】【编辑】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}else if(CherryConstants.OPERATE_OD_EDIT.equals(currentOperation)){
                    //订单工作流中的二审退回模式  按钮有【提交】【保存】【编辑】【关闭】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}else if(CherryConstants.OPERATE_OD_AUDIT.equals(currentOperation)){
                    //订单工作流中的一审模式  按钮有【提交】【自动执行同意】【修改】【废弃】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}else if(CherryConstants.OPERATE_GR_CONFIRM.equals(currentOperation)){
                    //入库工作流中的确认入库模式  按钮有确认收货
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
	    	Map<String,Object> billInformation = new HashMap<String,Object>();
	    	//取得入库主表信息
	    	Map<String,Object> mainData = new HashMap<String,Object>();
	    	//取得当前操作者信息
	    	Map<String,Object> sessionMap =  this.getSessionMap();;

    		String entryID = request.getParameter("entryid").toString();
    		String actionID = request.getParameter("actionid").toString();
    		mainData.put("entryID", entryID);
    		mainData.put("actionID", actionID);
    		billInformation.put("mainData", mainData);
    		//调用BL中的doaction方法
    		binOLWSMNG02_BL.tran_doaction(sessionMap,billInformation);
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
		sessionMap.put("CreatePGM", "BINOLWSMNG02");
		sessionMap.put("UpdatePGM", "BINOLWSMNG02");
		//用户信息
		sessionMap.put("UserInfo", userInfo);
		sessionMap.put("OpComments",form.getOpComments());
		return sessionMap;
	}
	
    /**
     * 验证数据
     */
    public boolean validateSave() throws Exception{
        boolean flag = true;
        if(null == form.getInOrganizationID() || "".equals(form.getInOrganizationID())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00032")}));
            return false;
        }
        if(null == form.getInInventoryInfoID() || "".equals(form.getInInventoryInfoID())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00033")}));
            return false;
        }
        if(null == form.getInLogicInventoryInfoID() || "".equals(form.getInLogicInventoryInfoID())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00034")}));
            return false;
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
	
	/** 参数FORM */
    private BINOLWSMNG02_Form form = new BINOLWSMNG02_Form();

	@Override
	public BINOLWSMNG02_Form getModel() {
		return form;
	}

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
        return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}