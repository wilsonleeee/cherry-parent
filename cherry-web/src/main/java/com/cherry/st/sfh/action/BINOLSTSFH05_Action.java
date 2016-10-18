/*  
 * @(#)BINOLSTSFH05_Action.java     1.0 2011/09/14      
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
package com.cherry.st.sfh.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM15_IF;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH05_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品发货单详细Action
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public class BINOLSTSFH05_Action extends BaseAction implements
ModelDriven<BINOLSTSFH05_Form>{

    private static final long serialVersionUID = 3465795688430695938L;
    
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH05_Action.class);

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
    @Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
    
	/** 参数FORM */
	private BINOLSTSFH05_Form form = new BINOLSTSFH05_Form();
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLSTSFH05_BL")
	private BINOLSTSFH05_IF binOLSTSFH05_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTCM15_BL")
    private BINOLSTCM15_IF binOLSTCM15_BL;
	
	//当前操作员工ID
	private String optionEmployeeId;
	
	//收货部门实体仓库List
	private List<Map<String,Object>> receiveDepotList = null;
	
	//收货部门逻辑仓库List
	private List<Map<String,Object>> receiveLogiInvenList = null;
	
	/**
	 * 画面初期显示
	 * @author zhanggl
	 * @version 1.0 2011.11.04
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws Exception {
		int productDeliverID = 0;
	    if(null == form.getProductDeliverId() || "".equals(form.getProductDeliverId())){
	        //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            productDeliverID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setProductDeliverId(billID);
		}else{
		    productDeliverID = CherryUtil.string2int(form.getProductDeliverId());
		}
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
		
//		//设置收货地址当该部门是柜台时才取
//		int organizationId =  CherryUtil.obj2int(mainMap.get("BIN_OrganizationIDReceive"));
	    // 用户信息
//        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        
        optionEmployeeId = String.valueOf(userInfo.getBIN_EmployeeID());
        
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put(CherryConstants.ORGANIZATIONID, organizationId);
//		paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
//		List<Map<String,Object>> addressList = binOLSTSFH05_BL.getReceiveDepartAddressList(paramMap);
//		String address = "";
//		if(null != addressList && addressList.size()>0){
//		    address = ConvertUtil.getString(addressList.get(0).get("CounterAddress"));
//		}
//		mainMap.put("ReceiveDepartAddress", address);
		
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
		
        //库存锁定阶段
        String lockSection = "";
        if(CherryConstants.OPERATE_SD_AUDIT.equals(operateFlag) || CherryConstants.OPERATE_OD_AUDIT.equals(operateFlag) || CherryConstants.OPERATE_OD_AUDIT_SEC.equals(operateFlag)){
            lockSection = CherryConstants.AUDIT_FLAG_SUBMIT;
        }else if(CherryConstants.OPERATE_SD_AUDIT2.equals(operateFlag)){
            lockSection = CherryConstants.SDAUDIT_FLAG_SUBMIT2;
        }else if(CherryConstants.OPERATE_SD_EDIT.equals(operateFlag) || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag) || "2".equals(operateFlag)){
            lockSection = CherryConstants.AUDIT_FLAG_UNSUBMIT;
        }
        form.setLockSection(lockSection);
        
        int defaultDepotInfoID = 0;
		int defaultLogicInventoryInfoID = 0;
//		String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
//		String brandInfoId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
		
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
                
                //取得发货实体仓库List
                String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationID"));
                List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
                form.setDepotsInfoList(depotsInfoList);
                
                //取得发货逻辑仓库List
                Map<String,Object> logicPram =  new HashMap<String,Object>();
                logicPram.put("BIN_BrandInfoID", brandInfoId);
//                logicPram.put("BusinessType", CherryConstants.OPERATE_OD);
                logicPram.put("BusinessType", logicBusinessType);
                logicPram.put("ProductType", "1");
                logicPram.put("Type", "0");
                logicPram.put("language", language);
//                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
                form.setLogicDepotsInfoList(logicDepotsList);
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
	            pram.put("LockSection", lockSection);
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
        
        for(int i=0;i<detailList.size();i++){
        	 Map<String,Object> detailListDTO = detailList.get(i);
             int quantity = CherryUtil.obj2int(detailListDTO.get("Quantity"));
             int productQuantity = CherryUtil.obj2int(detailListDTO.get("ProductQuantity"));

             if(quantity >= productQuantity){
                 detailListDTO.put("abnormalQuantityFlag", "true");
             }else{
                 detailListDTO.put("abnormalQuantityFlag", "false");
             }
        }
        
        DecimalFormat df=new DecimalFormat("0.00");
        for(Map<String, Object> detail:detailList){
        	if(detail.get("totalCostPrice1")==null || detail.get("totalCostPrice1") ==""){
        		detail.put("costPrice1", "");
        	}else{
        		Double totalCostPrice1 = Double.valueOf(detail.get("totalCostPrice1").toString());
        		int quantity = Integer.valueOf(detail.get("Quantity").toString());
        		if(quantity == 0){
        			detail.put("costPrice1", "");
				}else{					
					detail.put("costPrice1", df.format(totalCostPrice1/quantity));//默认四舍五入
				}        		
        	}
        	
        	detail.put("sort", "asc");
        	detail.put("depotInfoId", form.getProductDeliverMainData().get("BIN_DepotInfoID"));
        	detail.put("BIN_LogicInventoryInfoID", form.getProductDeliverMainData().get("BIN_LogicInventoryInfoID"));
        	detail.put("productVendorId", detail.get("BIN_ProductVendorID"));
        	detail.put("quantity", detail.get("Quantity"));
            
        	List<Map<String,Object>> proNewBatchStockList =binOLSTSFH06_BL.getProNewBatchStockList(detail);
        	if(CherryUtil.isBlankList(proNewBatchStockList)){
        		detail.put("costPrice", "");
        		detail.put("totalCostPrice", "");
        	}else{
        		int quantity=ConvertUtil.getInt(detail.get("quantity"));//发货单明细数量
				int tempQuantity=0;//总数量				
				Double totalCostPrice = 0.00;//总成本价
				
				for(Map<String, Object> proNewBatchStock: proNewBatchStockList){
					int amount=ConvertUtil.getInt(proNewBatchStock.get("StockQuantity"));//库存数量
					Double costPrice = ConvertUtil.getDouble(proNewBatchStock.get("CostPrice"));//成本价
					
					if(amount<quantity){//表示库存不够扣减
						totalCostPrice+=amount*costPrice;
						tempQuantity+=amount;
						quantity=quantity-amount;
					}
					else{//表示库存不够扣减此时停止循环
						totalCostPrice+=quantity*costPrice;
						tempQuantity+=quantity;
						break;
					}
				}	
				if(quantity == 0){
					detail.put("costPrice", "");
					detail.put("totalCostPrice", "");
				}else{					
					detail.put("costPrice", df.format(totalCostPrice/tempQuantity));
					detail.put("totalCostPrice", df.format(totalCostPrice));
				}
        	}
        	
        }
        
		form.setProductDeliverDetailData(detailList);
		
		//判断是否是要进行产品收货,如是就取得收货部门的实体仓库和逻辑仓库
		if(CherryConstants.OPERATE_RD.equals(operateFlag)){
			//取得收货部门实体仓库
			String departID = String.valueOf(mainMap.get("BIN_OrganizationIDReceive"));
			receiveDepotList = binOLCM18_BL.getDepotsByDepartID(departID, language);
			
			//判断是后台部门还是柜台
			String logicType = "0";
			String businessType = CherryConstants.LOGICDEPOT_BACKEND_RD;
	        Map<String,Object> departInfoMap = binOLCM01_BL.getDepartmentInfoByID(departID,language);
	        if(null != departInfoMap){
	            String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
	            //终端
	            if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
	                logicType = "1";
	                businessType = CherryConstants.LOGICDEPOT_TERMINAL_GR;
	            }
	        }
			
			//取得逻辑仓库
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			//0后台逻辑仓库 1终端逻辑仓库
			paramMap.put("Type", logicType);
			paramMap.put("language", language);
			paramMap.put("BusinessType", businessType);
			paramMap.put("ProductType", "1");
//			receiveLogiInvenList = binOLCM18_BL.getLogicDepotByBusinessType(paramMap);
			receiveLogiInvenList = binOLCM18_BL.getLogicDepotByBusiness(paramMap);
		}
		
        //取得系统配置项库存是否允许为负
        String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
        form.setCheckStockFlag(configValue);
        
        //配置项 产品发货使用价格（销售价格/会员价格）
        configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
		
      //配置项  实际执行价是否按成本价计算
        configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoID, brandInfoId);
        form.setUseCostPrice(configValue);
        
		return SUCCESS;
	}
	
	@Override
	public BINOLSTSFH05_Form getModel() {
		return form;
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
            if(!validateForm()){
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
            // OS_OperateResultCode =101审核通过 =103再次提交 =105已发货 为这几个值时，需要检查库存
            if ((operateResultCode.equals("101") || operateResultCode.equals("103") || operateResultCode.equals("105"))
                    &&!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTSFH05_BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
    }
    
    /**
     * 发货单保存
     * @author zhanggl
     * @version 1.0 2011-11-04
     * 
     * */
    public String save() throws Exception{
    	 try{
             if(!validateForm()){
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }
             UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
             binOLSTSFH05_BL.tran_save(form,userInfo);
             this.addActionMessage(getText("ICM00002")); 
             return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
         }catch(Exception e){
             logger.error(e.getMessage(),e);
             if(e instanceof CherryException){
                 CherryException temp = (CherryException)e;
                 this.addActionError(temp.getErrMessage());
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }else{
                 //系统发生异常，请联系管理人员。
                 this.addActionError(getText("ECM00036"));
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }
         }
    }
    
    /**
     * 发货单提交
     * @author zhanggl
     * @version 1.0 2011-11-04 
     * 
     * */
    public String submit() throws Exception{
    	 try{
             if(!validateForm()){
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }
             if (!validateStock()) {
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }
             UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
             binOLSTSFH05_BL.tran_submit(form,userInfo);
             this.addActionMessage(getText("ICM00002")); 
             return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
         }catch(Exception e){
             logger.error(e.getMessage(),e);
             if(e instanceof CherryException){
                 CherryException temp = (CherryException)e;
                 this.addActionError(temp.getErrMessage());
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }else{
                 //系统发生异常，请联系管理人员。
                 this.addActionError(getText("ECM00036"));
                 return CherryConstants.GLOBAL_ACCTION_RESULT;
             }
         }
    }

    /**
     * 非工作流中的删除单据
     * @return
     * @throws Exception 
     */
    public String delete() throws Exception{
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTSFH05_BL.tran_delete(form,userInfo);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            // 自定义异常的场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
        this.addActionMessage(getText("ICM00002"));
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
    }
    
	public String getOptionEmployeeId() {
		return optionEmployeeId;
	}

	public void setOptionEmployeeId(String optionEmployeeId) {
		this.optionEmployeeId = optionEmployeeId;
	}
    
    public List<Map<String, Object>> getReceiveDepotList() {
		return receiveDepotList;
	}

	public void setReceiveDepotList(List<Map<String, Object>> receiveDepotList) {
		this.receiveDepotList = receiveDepotList;
	}

	public List<Map<String, Object>> getReceiveLogiInvenList() {
		return receiveLogiInvenList;
	}

	public void setReceiveLogiInvenList(
			List<Map<String, Object>> receiveLogiInvenList) {
		this.receiveLogiInvenList = receiveLogiInvenList;
	}

	/**
     * 验证数据
     */
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if ("501".equals(actionID) || "502".equals(actionID) || "61".equals(actionID) || "521".equals(actionID)
                || "522".equals(actionID) || "601".equals(actionID)  || "801".equals(actionID) || "803".equals(actionID)
                || "804".equals(actionID) || "805".equals(actionID)) {
            if("521".equals(actionID) || "522".equals(actionID) || "601".equals(actionID) || "501".equals(actionID)){
                if(null == form.getOutOrganizationID() || "".equals(form.getOutOrganizationID())){
                    this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
                    return;
                }
                if(null == form.getOutDepotInfoID() || "".equals(form.getOutDepotInfoID())){
                    this.addActionError(getText("EST00021",new String[]{getText("PST00008")}));
                    return;
                }
            }
            if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            boolean allZeroFlag = true;
            for(int i=0;i<form.getProductVendorIDArr().length;i++){
                String quantity = ConvertUtil.getString(form.getQuantityArr()[i]);
                if("".equals(quantity) || !CherryChecker.isPositiveAndNegative(quantity)){
                    this.addActionError(getText("EST00008"));
                    return;
                }
                if(!quantity.equals("0")){
                    allZeroFlag = false;
                }
            }
            // 人工通过/发货时，明细全是0，报错。
            if (allZeroFlag) {
                String entryID = request.getParameter("entryid").toString();
                WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(Long.parseLong(entryID)));
                ActionDescriptor ad = wd.getAction(CherryUtil.obj2int(actionID));
                Map<String, Object> metaAttr = ad.getMetaAttributes();
                if (null != metaAttr && !metaAttr.isEmpty()) {
                    String operateResultCode = ConvertUtil.getString(metaAttr.get("OS_OperateResultCode"));
                    if (operateResultCode.equals("101") || operateResultCode.equals("105")) {
                        this.addActionError(getText("EST00008"));
                        return;
                    }
                }
            }
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
        
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
        
        //产品订单数必须大于系统配置项所配置的金额
        String minAmountForbiddenString = ConvertUtil.getString(binOLCM14_BL.getConfigValue("1373", organizationInfoID, brandInfoId));
        if (!minAmountForbiddenString.equals("")){
            int minAmountForbidden = ConvertUtil.getInt(minAmountForbiddenString);
            double totalAmountCheck=ConvertUtil.getDouble(form.getTotalAmountCheck());
            if (totalAmountCheck<minAmountForbidden){
            	String message=ConvertUtil.getString(minAmountForbidden);
            	this.addActionError(getText("EST00045", new String[]{getText(message)}));//要在message中增加消息字段
                isCorrect = false;
                return isCorrect;
            }
        }

        //产品订单数必须大于配置项所配置的数量
        String minQuantityForbiddenString  = ConvertUtil.getString(binOLCM14_BL.getConfigValue("1372", organizationInfoID, brandInfoId));
        if (!minQuantityForbiddenString.equals("")){
        	int minQuantityForbidden = ConvertUtil.getInt(minQuantityForbiddenString);
            
            String[] QuantityArr = form.getQuantityArr();
            for (int i=0;i<QuantityArr.length;i++){
            	if (ConvertUtil.getInt(QuantityArr[i])<minQuantityForbidden){
            		String message=ConvertUtil.getString(minQuantityForbidden);
            		this.addActionError(getText("EST00046", new String[]{getText(message)}));//要在message中增加消息字段
                    isCorrect = false;
                    return isCorrect;
            	}
            }
        }
        

        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutDepotInfoID()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getOutLogicInventoryInfoID()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "1");//产品
            paramMap.put("IDArr", form.getProductVendorIDArr());
            paramMap.put("QuantityArr", form.getQuantityArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
                this.addActionError(getText("EST00034"));
                return isCorrect;
            }
        }
        
       
        

        return isCorrect;
    }
    
    private boolean validateForm(){
        boolean isCorrect = true;
        String planArriveDate = ConvertUtil.getString(form.getPlanArriveDate());
        if(!planArriveDate.equals("")){
            if(!CherryChecker.checkDate(planArriveDate)){
                   this.addActionError(getText("ECM00008", new String[]{getText("PST00036")}));
                    isCorrect = false;
            }
        }
        return isCorrect;
    }
}
