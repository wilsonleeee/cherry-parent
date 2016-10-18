package com.cherry.wp.ws.mng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
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
import com.cherry.cm.util.DateUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM15_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH01_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH02_IF;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.wr.common.service.BINOLWRCOM01_Service;
import com.cherry.wp.ws.mng.form.BINOLWSMNG01_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWSMNG01_Action extends BaseAction implements ModelDriven<BINOLWSMNG01_Form> {
	
	private static final long serialVersionUID = -6316791890093285854L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLWSMNG01_Action.class);
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLSTSFH02_BL")
	private BINOLSTSFH02_IF binOLSTSFH02IF;
	
	@Resource(name="binOLSTSFH01_BL")
    private BINOLSTSFH01_IF binOLSTSFH01_BL;
	
	@Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
	
	@Resource(name="binOLWRCOM01_Service")
	private BINOLWRCOM01_Service binOLWRCOM01_Service;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM15_BL")
	private BINOLSTCM15_IF binOLSTCM15_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_BL;

	/**
	 * 订货单查询画面初始化
	 * 
	 * @return 订货单查询画面
	 */
	public String init() throws Exception {
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("businessType", "1");
		params.put("operationType", "1");
		params.put("departId", counterInfo.getOrganizationId());
	    params.put("testType", binOLCM00_BL.getDepartTestType(ConvertUtil.getString(counterInfo.getOrganizationId())));
		form.setParams(JSONUtil.serialize(params));
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String sysDate = binOLWRCOM01_Service.getDateYMD();
		// 开始日期
		form.setStartDate(binOLWRCOM01_Service.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), 
				DateUtil.coverString2Date(sysDate, DateUtil.DATE_PATTERN)));
		// 截止日期
		form.setEndDate(sysDate);
		
		return SUCCESS;
	}
	
	/**
	 * 订货单查询
	 * 
	 * @return 订货单查询画面
	 */
	public String search() throws Exception {
		
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		// 取得总数
		int count = binOLSTSFH02IF.searchProductOrderCount(searchMap);
		if (count > 0) {
			productOrderList = binOLSTSFH02IF.searchProductOrderList(searchMap);
		}
		sumInfo = binOLSTSFH02IF.getSumInfo(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return SUCCESS;
	}
	
	/**
     * 画面初始化
     * @return
     * @throws Exception 
     */	
    public String addInit() throws Exception{
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
        
        //订货方
 		CounterInfo counterInfo = (CounterInfo) session
 				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
        int organizationId = counterInfo.getOrganizationId();
        String departCodeName = counterInfo.getCounterName();
        
        //柜台号
        form.setCounterCode(counterInfo.getCounterCode());
        
        //BAList
        Map<String,Object> paramBAMap = new HashMap<String,Object>();
        paramBAMap.put("organizationId", organizationId);
        
        // 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
		String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", String.valueOf(organizationInfoId), brandInfoId);
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
        
        //订货方仓库
        List<Map<String,Object>> inDepotList = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationId), language);
        int inDepotID = 0;
        if(null != inDepotList && inDepotList.size()>0){
            inDepotID = CherryUtil.obj2int(inDepotList.get(0).get("BIN_DepotInfoID"));
        }
        
        //取得订货方逻辑仓库
        Map<String,Object> logicParam = new HashMap<String,Object>();
        logicParam.put("organizationId", organizationId);
        try{
            List<Map<String,Object>> inLogicDepotList = getLogicList(logicParam);
            int inLgDepotID = 0;
            if(null != inLogicDepotList && inLogicDepotList.size()>0){
            	inLgDepotID = CherryUtil.obj2int(inLogicDepotList.get(0).get("BIN_LogicInventoryInfoID"));
            }
            
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
            initInfoMap.put("bussinessDate", bussinessDate);
            initInfoMap.put("inDepotID", inDepotID);
            initInfoMap.put("inLgDepotID", inLgDepotID);
            initInfoMap.put("employeeName", userInfo.getEmployeeName());
            form.setInitInfoMap(initInfoMap);
            form.setOperateType("newBill");
        }catch(Exception e){
            this.addActionError(getText("ECM00036"));
        }
        
        return SUCCESS;
    }
    
    public String detailInit() throws Exception {
        try{
            int productOrderID = CherryUtil.string2int(form.getProductOrderID());
            
            String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
            //取得订单概要信息 和详细信息
            Map<String,Object> mainMap = binOLSTCM02_BL.getProductOrderMainData(productOrderID,language);
            
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
            String brandInfoId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
            
            Map<String,Object> otherParam = new HashMap<String,Object>();
            otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
            otherParam.put("BIN_BrandInfoID", brandInfoId);
            List<Map<String,Object>> detailList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID,language,otherParam);
            
            //工作流相关操作  决定画面以哪种模式展现
            String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
            String operateFlag = getPageOperateFlag(workFlowID,mainMap);
            form.setWorkFlowID(workFlowID);
            form.setOperateType(operateFlag);
            
            //当前操作状态是审核标志
            boolean operateTypeAudit = false;
            if(CherryConstants.ENUMODAUDIT.isAudit(operateFlag)){
                operateTypeAudit = true;
            }
            mainMap.put("OperateTypeAudit", operateTypeAudit);
            
            int defaultDepotInfoID = 0;
            int defaultLogicInventoryInfoID = 0;
//            if (operateTypeAudit || "2".equals(operateFlag) || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag)
//                    || CherryConstants.OPERATE_SD.equals(operateFlag)) {
//                //取得发货实体仓库List
//                String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDAccept"));
//                List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
//                form.setDepotsInfoList(depotsInfoList);
//                
//                //取得发货逻辑仓库List
//                Map<String,Object> logicPram =  new HashMap<String,Object>();
//                logicPram.put("BIN_BrandInfoID", brandInfoId);
////                logicPram.put("BusinessType", CherryConstants.OPERATE_OD);
//                logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_OD);
//                logicPram.put("ProductType", "1");
//                logicPram.put("Type", "0");
//                logicPram.put("language", language);
////                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
//                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
//                
//                form.setLogicDepotsInfoList(logicDepotsList);
//            }
            Map<String,Object> paramPrivilege = new HashMap<String,Object>();
            paramPrivilege.put("userInfo", userInfo);
            paramPrivilege.put("mainData", mainMap);
            String showRecStockFlag = binOLSTCM15_BL.getShowRecStockFlag(paramPrivilege);
            mainMap.put("showRecStockFlag", showRecStockFlag);
            form.setProductOrderMainData(mainMap);
            
            boolean searchSaleQuantityFlag = false;
            Integer[] prtIDArr = new Integer[detailList.size()];
            
            //TODO:审核模式下 明细里面要带上库存，并且还要考虑已冻结库存
            if (operateTypeAudit || CherryConstants.OPERATE_OD_SUBMIT.equals(operateFlag) || CherryConstants.OPERATE_SD.equals(operateFlag)) {
                searchSaleQuantityFlag = true;
                for(int i=0;i<detailList.size();i++){
                    Map<String,Object> temp = detailList.get(i);
                    //发货方库存
                    Map<String,Object> pram = new HashMap<String,Object>();
                    pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
                    String inventoryInfoIDAccept = ConvertUtil.getString(temp.get("BIN_InventoryInfoIDAccept"));
                    if(null == inventoryInfoIDAccept || "".equals(inventoryInfoIDAccept)){
                        pram.put("BIN_DepotInfoID", defaultDepotInfoID);
                        temp.put("BIN_InventoryInfoIDAccept", defaultDepotInfoID);
                    }else{
                        pram.put("BIN_DepotInfoID", CherryUtil.obj2int(temp.get("BIN_InventoryInfoIDAccept")));
                    }
                    String logicInventoryInfoIDAccept = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoIDAccept"));
                    if(null == logicInventoryInfoIDAccept || "".equals(logicInventoryInfoIDAccept)){
                        pram.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
                        temp.put("BIN_LogicInventoryInfoIDAccept", defaultLogicInventoryInfoID);
                    }else{
                        pram.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoIDAccept"));
                    }
                    pram.put("FrozenFlag", "2");
                    if(operateFlag.equals(CherryConstants.OPERATE_SD)){
                        //发货步骤显示发货方实际库存
                        pram.put("FrozenFlag", "1");
                    }
                    int productQuantity = binOLCM20_BL.getProductStock(pram);
                    temp.put("ProductQuantity", productQuantity);
                    
                    //订货方库存
                    pram.put("FrozenFlag", "2");
                    pram.put("BIN_DepotInfoID", CherryUtil.obj2int(temp.get("BIN_InventoryInfoID")));
                    pram.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoID")));
                    int orderStock = binOLCM20_BL.getProductStock(pram);
                    temp.put("OrderStock", orderStock);
                    
                    prtIDArr[i] = CherryUtil.obj2int(temp.get("BIN_ProductVendorID"));
                    
                    detailList.set(i, temp);
                }
            }else{
                for(int i=0;i<detailList.size();i++){
                    Map<String,Object> temp = detailList.get(i);
                    String inventoryInfoIDAccept = ConvertUtil.getString(temp.get("BIN_InventoryInfoIDAccept"));
                    if(null == inventoryInfoIDAccept || "".equals(inventoryInfoIDAccept)){
                        temp.put("BIN_InventoryInfoIDAccept", defaultDepotInfoID);
                    }
                    String logicInventoryInfoIDAccept = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoIDAccept"));
                    if(null == logicInventoryInfoIDAccept || "".equals(logicInventoryInfoIDAccept)){
                        temp.put("BIN_LogicInventoryInfoIDAccept", defaultLogicInventoryInfoID);
                    }
                    detailList.set(i, temp);
                }
            }
            
            //根据柜台的部门ID取出柜台号
            String counterDepartID = ConvertUtil.getString(mainMap.get("BIN_OrganizationID"));
            Map<String,Object> paramCounterMap = new HashMap<String,Object>();
            paramCounterMap.put("organizationId", counterDepartID);
            Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramCounterMap);
            if(null != counterInfo){
                form.setCounterCode(ConvertUtil.getString(counterInfo.get("counterCode")));
            }
            
            form.setProductOrderDetailData(detailList);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            // 自定义异常的场合
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
            } else {
                // 系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
            }
        }
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
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    //查看明细模式  按钮有【关闭】
		String ret="1";
		 if(null==workFlowID||"".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.ODAUDIT_FLAG_AGREE.equals(verifiedFlag)){
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
				
                if (CherryConstants.ENUMODAUDIT.isAudit(currentOperation)
                        || CherryConstants.OPERATE_SD.equals(currentOperation)) {
                    //订单审核模式   按钮有【通过】【退回】【删除】【关闭】
                    //新的流程（通用）是在发货单页面审核，部分品牌是在订单画面审核、发货，这里是否隐藏操作按钮根据当前任务表的RelevanceTableName判断。
                    String workflowName = workflow.getWorkflowName(Long.parseLong(workFlowID));
                    if(workflowName.indexOf("proFlowOD") < 0){
                        request.setAttribute("ActionDescriptor", adArr);
                    }else{
                        Map<String,Object> param = new HashMap<String,Object>();
                        param.put("WorkFlowID", workFlowID);
                        List<Map<String,Object>> userTaskList = binOLCM19_BL.getInventoryUserTaskByOSID(param);
                        if(null != userTaskList && userTaskList.size()>0){
                            String relevanceTableName = ConvertUtil.getString(userTaskList.get(0).get("RelevanceTableName"));
                            if(relevanceTableName.equals("Inventory.BIN_ProductOrder")){
                                request.setAttribute("ActionDescriptor", adArr);
                            }
                        }
                    }
                    ret= currentOperation;
				} else if (CherryConstants.OPERATE_OD_EDIT.equals(currentOperation)) {
					//工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if(CherryConstants.OPERATE_SD_CREATE.equals(currentOperation)){
				    //工作流的生成发货单模式，按钮有【生成发货单】【废弃订单】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}else if(CherryConstants.OPERATE_OD_SUBMIT.equals(currentOperation)){
                    //工作流的订单提交模式，按钮有【提交】【废弃】
                    request.setAttribute("ActionDescriptor", adArr);
                    ret= currentOperation;
				}
			}
		 }		 
		 return ret;
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
	 * 查询参数MAP取得
	 * 
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
	    // 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		//订单编号
		map.put("orderNo", form.getOrderNo());
		//订单状态
		map.put("tradeStatus",form.getTradeStatus());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());

		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
	}
	
	/** 汇总信息 */
	private Map sumInfo;
	
	private List<Map<String, Object>> productOrderList;

	public Map getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map sumInfo) {
		this.sumInfo = sumInfo;
	}

	public List<Map<String, Object>> getProductOrderList() {
		return productOrderList;
	}

	public void setProductOrderList(List<Map<String, Object>> productOrderList) {
		this.productOrderList = productOrderList;
	}

	private BINOLWSMNG01_Form form = new BINOLWSMNG01_Form();
	
	@Override
	public BINOLWSMNG01_Form getModel() {
		return form;
	}

}
