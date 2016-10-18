/*  
 * @(#)BINOLSTSFH03_Action.java     1.0 2011/09/08      
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
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
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH03_IF;
import com.cherry.st.sfh.interfaces.BINOLSTSFH06_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品订货单详细Action
 * @author niushunjie
 * @version 1.0 2011.09.08
 */
public class BINOLSTSFH03_Action extends BaseAction implements
ModelDriven<BINOLSTSFH03_Form>{
	
    private static final long serialVersionUID = -5560607699075815605L;
    
    private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH03_Action.class);

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	/** 参数FORM */
	private BINOLSTSFH03_Form form = new BINOLSTSFH03_Form();
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	@Resource(name="binOLSTSFH03_BL")
	private BINOLSTSFH03_IF binOLSTSFH03_BL;
	
    @Resource(name="binOLSTCM03_BL")
    private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLSTCM15_BL")
	private BINOLSTCM15_IF binOLSTCM15_BL;

	@Resource(name="binOLSTSFH06_BL")
	private BINOLSTSFH06_IF binOLSTSFH06_BL;
	
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
        try{
            int productOrderID = 0;
            //判断是top页打开任务
            if(null == form.getProductOrderID() || "".equals(form.getProductOrderID())){
                //取得URL中的参数信息
                String entryID= request.getParameter("entryID");
                String billID= request.getParameter("mainOrderID");
                productOrderID = Integer.parseInt(billID);
                form.setWorkFlowID(entryID);
                form.setProductOrderID(billID);
            }else{
                productOrderID = CherryUtil.string2int(form.getProductOrderID());
            }
            
            String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
            //取得订单概要信息 和详细信息
            Map<String,Object> mainMap = binOLSTCM02_BL.getProductOrderMainData(productOrderID,language);
            String tradeStatus = ConvertUtil.getString(mainMap.get("TradeStatus"));
            // 已发货
            if("12".equals(tradeStatus) || "13".equals(tradeStatus)) {
            	// 根据产品订货单的工作流ID取得对应的发货单ID----发货单只有当订货单流程到了已发货之后（包括已收货）才需要进行
            	Map<String, Object> paramMap = new HashMap<String, Object>();
            	paramMap.put("WorkFlowID", mainMap.get("WorkFlowID"));
            	form.setProductDeliverID(binOLSTSFH03_BL.getDeliverIDByWorkFlowID(paramMap));
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
            String brandInfoId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
            
            Map<String,Object> otherParam = new HashMap<String,Object>();
            otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
            otherParam.put("BIN_BrandInfoID", brandInfoId);
            List<Map<String,Object>> detailList = binOLSTCM02_BL.getProductOrderDetailData(productOrderID,language,otherParam);
            
            //配置项  实际执行价是否按成本价计算
            //TODO: 修改配置项名称
            String configValue = binOLCM14_BL.getConfigValue("1374", organizationInfoID, brandInfoId);
            form.setUseCostPrice(configValue);
            
            //工作流相关操作  决定画面以哪种模式展现
            String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
            String operateFlag = getPageOperateFlag(workFlowID,mainMap);
            form.setWorkFlowID(workFlowID);
            form.setOperateType(operateFlag);
            
            /**发货之前是否修改过 */
            form.setModifiedFlag("0");//默认0没有被修改过，只有要发货的编辑页面才会改为1修改过
            
            //当前操作状态是审核标志
            boolean operateTypeAudit = false;
            if(CherryConstants.ENUMODAUDIT.isAudit(operateFlag)){
                operateTypeAudit = true;
            }
            mainMap.put("OperateTypeAudit", operateTypeAudit);
            
            //库存锁定阶段
            String lockSection = "";
            if(operateTypeAudit){
                lockSection = CherryConstants.AUDIT_FLAG_SUBMIT;
            }else if(CherryConstants.OPERATE_OD_EDIT.equals(operateFlag) || "2".equals(operateFlag)){
                lockSection = CherryConstants.AUDIT_FLAG_UNSUBMIT;
            }
            form.setLockSection(lockSection);
            
            int defaultDepotInfoID = 0;
            int defaultLogicInventoryInfoID = 0;
            if (operateTypeAudit || "2".equals(operateFlag) || CherryConstants.OPERATE_OD_EDIT.equals(operateFlag)
                    || CherryConstants.OPERATE_SD.equals(operateFlag)) {
                //取得发货实体仓库List
                String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDAccept"));
                List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
                form.setDepotsInfoList(depotsInfoList);
                
                //取得发货逻辑仓库List
                Map<String,Object> logicPram =  new HashMap<String,Object>();
                logicPram.put("BIN_BrandInfoID", brandInfoId);
//                logicPram.put("BusinessType", CherryConstants.OPERATE_OD);
                logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_OD);
                logicPram.put("ProductType", "1");
                logicPram.put("Type", "0");
                logicPram.put("language", language);
//                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
                List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
                
                form.setLogicDepotsInfoList(logicDepotsList);
            }
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
            
            List<Map<String,Object>> saleQuantityList = new ArrayList<Map<String,Object>>();
            Map<Integer,Object> saleQuantityMap = new HashMap<Integer,Object>();
            if(searchSaleQuantityFlag){
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("prtVendorId", prtIDArr);
                paramMap.put("organizationInfoId", organizationInfoID);
                paramMap.put("brandInfoId", brandInfoId);
                paramMap.put("organizationId", mainMap.get("BIN_OrganizationID"));
                String orderTime = ConvertUtil.getString(mainMap.get("Date"))+" "+ConvertUtil.getString(mainMap.get("OrderTime"));
                paramMap.put("saleTimeEnd", orderTime);
                saleQuantityList = binOLSTSFH03_BL.getSaleQuantity(paramMap);
                for(int i=0;i<saleQuantityList.size();i++){
                    saleQuantityMap.put(CherryUtil.obj2int(saleQuantityList.get(i).get("BIN_ProductVendorID")),saleQuantityList.get(i).get("SaleQuantity"));
                }
            }
            
            String maxPercentValue = binOLCM14_BL.getConfigValue("1116", organizationInfoID, brandInfoId);
            String minPercentValue = binOLCM14_BL.getConfigValue("1117", organizationInfoID, brandInfoId);
            form.setMaxPercent(maxPercentValue);
            form.setMinPercent(minPercentValue);
            for(int i=0;i<detailList.size();i++){
                Map<String,Object> detailListDTO = detailList.get(i);
                int quantity = CherryUtil.obj2int(detailListDTO.get("Quantity"));
                int suggestedQuantity = CherryUtil.obj2int(detailListDTO.get("SuggestedQuantity"));
                int maxQuantity = quantity;
                int minQuantity = quantity;
                if(!"".equals(maxPercentValue)){
                    maxQuantity = (int)Math.floor(suggestedQuantity*Double.parseDouble(maxPercentValue)/100.00);
                }
                if(!"".equals(minPercentValue)){
                    minQuantity = (int)Math.floor(suggestedQuantity*Double.parseDouble(minPercentValue)/100.00);
                }
                if(quantity >= minQuantity && quantity <= maxQuantity){
                    detailListDTO.put("abnormalQuantityFlag", "false");
                }else{
                    detailListDTO.put("abnormalQuantityFlag", "true");
                }
                
                //近30天销量
                if(searchSaleQuantityFlag){
                    int saleQuantity = CherryUtil.obj2int(saleQuantityMap.get(CherryUtil.obj2int(detailListDTO.get("BIN_ProductVendorID"))));
                    detailListDTO.put("SaleQuantity", saleQuantity);
                }
            }
            
            	
            //得到每个明细对应平均成本价和总成本价
            DecimalFormat df=new DecimalFormat("0.00");
            for(Map<String, Object> detail : detailList){
            	
            	detail.put("sort", "asc");
            	detail.put("depotInfoId", detail.get("BIN_InventoryInfoIDAccept"));
            	detail.put("BIN_LogicInventoryInfoID", detail.get("BIN_LogicInventoryInfoIDAccept"));
            	detail.put("productVendorId", detail.get("BIN_ProductVendorID"));
            	detail.put("quantity", detail.get("Quantity"));
                
            	List<Map<String,Object>> proNewBatchStockList =binOLSTSFH06_BL.getProNewBatchStockList(detail);
            	if(CherryUtil.isBlankList(proNewBatchStockList)){
            		detail.put("costPrice", "");
            		detail.put("totalCostPrice", "");
            	}else{
            		int quantity=ConvertUtil.getInt(detail.get("Quantity"));//订货单明细数量
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
    					detail.put("costPrice", df.format(totalCostPrice/tempQuantity));//默认四舍五入
    					detail.put("totalCostPrice", df.format(totalCostPrice));
    				}
            	}
            	
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
	
	@Override
	public BINOLSTSFH03_Form getModel() {
		return form;
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
	 * 非工作流中的保存订单
	 * @return
	 * @throws Exception 
	 */
	public String save() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    try{
	        binOLSTSFH03_BL.tran_save(form, userInfo);
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
	
	/**
	 * 暂存
	 * @return
	 * @throws Exception 
	 */
	public String saveTemp() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    try{
	    	binOLSTSFH03_BL.tran_saveTemp(form, userInfo);
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
        return CherryConstants.GLOBAL_ACCTION_RESULT; 
	}
	
	/**
     * 非工作流中的删除订单
     * @return
	 * @throws Exception 
     */
	public String delete() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTSFH03_BL.tran_delete(form,userInfo);
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

	/**
     * 非工作流中的提交订单
     * @return
	 * @throws Exception 
     */
	public String submit() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTSFH03_BL.tran_submit(form,userInfo);
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
	
	/**
	 * 工作流中的各种动作入口方法
	 * @return
	 * @throws Exception
	 */
	public String doaction() throws Exception{
	    try{
    		String entryID = request.getParameter("entryid").toString();
    		String actionID = request.getParameter("actionid").toString();
    		String csrftoken = request.getParameter("csrftoken").toString();
    		form.setEntryID(entryID);
    		form.setActionID(actionID);
            form.setCsrftoken(csrftoken);
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		
    		WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(Long.parseLong(entryID)));
    		ActionDescriptor ad = wd.getAction(CherryUtil.obj2int(actionID));
    		Map<String,Object> metaAttr = ad.getMetaAttributes();
    		String osNextStrutsAction = ConvertUtil.getString(metaAttr.get("OS_NextStrutsAction"));
            String operateCode = ConvertUtil.getString(metaAttr.get("OS_OperateCode"));
            String operateResultCode = ConvertUtil.getString(metaAttr.get("OS_OperateResultCode"));
            // OS_OperateResultCode 为这几个值时，需要检查库存 101审核通过 103再次提交 105已发货
            if ((operateResultCode.equals("101") || operateResultCode.equals("103") || operateResultCode.equals("105") 
                    || operateCode.equals("44")) && !validateStock()) {
                if(operateCode.equals("44")){
                    return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
                }else{
                    return CherryConstants.GLOBAL_ACCTION_RESULT;
                }
            }
    		binOLSTSFH03_BL.tran_doaction(form,userInfo);
    		
    		//当工作流的Action有元数据OS_NextStrutsAction时，根据OS_NextStrutsAction的值跳转
    		if("".equals(osNextStrutsAction)){
                this.addActionMessage(getText("ICM00002")); 
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                if("301".equals(actionID)){
                    PropertySet ps = workflow.getPropertySet(Long.parseLong(entryID));
                    //发货单ID
                    String mainOrderID = ConvertUtil.getString(ps.getInt("BIN_ProductDeliverID"));
                    form.setMainOrderID(mainOrderID);
                }
                return osNextStrutsAction;
            }
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
	}
	
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 通过Ajax取得指定仓库的库存（多选）
     * 如果要取得近30天销量，参数里要加searchSaleQuantity=true
     * 如果要取得建议订单数，参数里要加searchSuggestedQuantity=true及searchSaleQuantity=true
     * @throws Exception
     */
    public void getStockCountByAjax() throws Exception{
        String[] productVendorIdArr = request.getParameterValues("productVendorId");
        String[] currentIndexArr = request.getParameterValues("currentIndex");
        String searchSaleQuantity = ConvertUtil.getString(request.getParameter("searchSaleQuantity"));
        String searchSuggestedQuantity = ConvertUtil.getString(request.getParameter("searchSuggestedQuantity"));
        String operateType = ConvertUtil.getString(request.getParameter("operateType"));
        String[] quantityArr = request.getParameterValues("quantityArr");
        
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        if(null != productVendorIdArr){
            for(int i=0;i<productVendorIdArr.length;i++){
                map.put("FrozenFlag", "2");
                //发货方库存
                if(operateType.equals(CherryConstants.OPERATE_SD)){
                    //发货步骤显示发货方实际库存
                    map.put("FrozenFlag", "1");
                }
                String lockSection = ConvertUtil.getString(form.getLockSection());
                if(!lockSection.equals("")){
                    map.put("LockSection",lockSection);
                }
                map.put("BIN_ProductVendorID", productVendorIdArr[i]);
                map.put("BIN_DepotInfoID", CherryUtil.obj2int(request.getParameter("depotInfoId")));
                map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(request.getParameter("logicDepotsInfoId")));
                int stockCount = binOLCM20_BL.getProductStock(map);
                
                //订货方库存
                int orderStock = 0;
                if(null != request.getParameter("depotInfoIDIn") && null != request.getParameter("logicDepotsInfoIDIn")){
                    map.put("FrozenFlag", "2");
                    map.put("LockSection","");
                    map.put("BIN_DepotInfoID", CherryUtil.obj2int(request.getParameter("depotInfoIDIn")));
                    map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(request.getParameter("logicDepotsInfoIDIn")));
                    orderStock = binOLCM20_BL.getProductStock(map);
                }
                                
                
                
                Map<String,Object> resultMap = new HashMap<String,Object>();
                resultMap.put("currentIndex", currentIndexArr[i]);
                resultMap.put("Quantity", stockCount);
                resultMap.put("OrderStock", orderStock);
                resultMap.put("hasproductflag", 1);
                resultMap.put("BIN_ProductVendorID", productVendorIdArr[i]);
                
                
                if(quantityArr!=null){
	                /**成本价相关 start */
	            	map.put("productVendorId", productVendorIdArr[i]);
	                map.put("depotInfoId", CherryUtil.obj2int(request.getParameter("depotInfoId")));
	                map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(request.getParameter("logicDepotsInfoId")));
	                map.put("quantity", quantityArr[i]);//数量 
	                map.put("sort", "asc");//排序方式
	                List<Map<String,Object>> proNewBatchStockList = binOLSTSFH06_BL.getProNewBatchStockList(map);
	        		
	        		if(CherryUtil.isBlankList(proNewBatchStockList)){
	        			resultMap.put("costPrice", "");
	        			resultMap.put("totalCostPrice", "");
	        		}else {
	        			int quantity=ConvertUtil.getInt(quantityArr[i]);//发货单明细数量
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
	        			
	        			if(quantity==0){	
	        				resultMap.put("costPrice", "");
	        				resultMap.put("totalCostPrice", "");
	        			}else{
	        				resultMap.put("costPrice",totalCostPrice/tempQuantity);
	        				resultMap.put("totalCostPrice", totalCostPrice);
	        			}
	        		}
                }
        		/**成本价相关 end */
                resultList.add(resultMap);
            }
        }
        
        //参数里有searchSaleQuantity=true，需要查询近30天销量
        if(null != searchSaleQuantity && searchSaleQuantity.equals("true")){
            String organizationId = request.getParameter("organizationId");
            String brandInfoId = request.getParameter("brandInfoId");
            String orderTime = request.getParameter("orderTime");
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("prtVendorId", productVendorIdArr);
            paramMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
            paramMap.put("brandInfoId", brandInfoId);
            paramMap.put("organizationId", organizationId);
            paramMap.put("saleTimeEnd", orderTime);
            List<Map<String,Object>> saleQuantityList = binOLSTSFH03_BL.getSaleQuantity(paramMap);
            Map<String,Object> saleQuantityMap = new HashMap<String,Object>();
            for(int i=0;i<saleQuantityList.size();i++){
                Map<String,Object> saleQuantityDTO = saleQuantityList.get(i);
                saleQuantityMap.put(ConvertUtil.getString(saleQuantityDTO.get("BIN_ProductVendorID")), saleQuantityDTO.get("SaleQuantity"));
            }
            for(int i=0;i<resultList.size();i++){
                Map<String,Object> returnMap = resultList.get(i);
                returnMap.put("SaleQuantity", CherryUtil.obj2int(saleQuantityMap.get(productVendorIdArr[i])));
            }
            
            if(null != searchSuggestedQuantity && searchSuggestedQuantity.equals("true")){
                List<Map<String,Object>> lowestStockDaysList = binOLSTSFH03_BL.getLowestStockDays(paramMap);
                Map<String,Object> lowestStockDaysMap = new HashMap<String,Object>();
                for(int i=0;i<lowestStockDaysList.size();i++){
                    Map<String,Object> lowestStockDaysDTO= lowestStockDaysList.get(i);
                    lowestStockDaysMap.put(ConvertUtil.getString(lowestStockDaysDTO.get("BIN_ProductVendorID")), lowestStockDaysDTO.get("LowestStockDays"));
                }
                
                List<Map<String,Object>> adtCoefficientList = binOLSTSFH03_BL.getAdtCoefficient(paramMap);
                Map<String,Object> adtCoefficientMap = new HashMap<String,Object>();
                for(int i=0;i<adtCoefficientList.size();i++){
                    Map<String,Object> adtCoefficientDTO= adtCoefficientList.get(i);
                    adtCoefficientMap.put(ConvertUtil.getString(adtCoefficientDTO.get("BIN_ProductVendorID")), adtCoefficientDTO.get("AdtCoefficient"));
                }
                
                for(int i=0;i<resultList.size();i++){
                    Map<String,Object> returnMap = resultList.get(i);
                    //近30天日均销量：近30天销量/天数， 近 30 天销量 /30 天（固定以 30 天为基准做分母）。
                    BigDecimal bdAdSaleQuantity = new BigDecimal(CherryUtil.obj2int(ConvertUtil.getString(returnMap.get("SaleQuantity")))/30);
                    //安全库存天数
                    String lowestStockDays = ConvertUtil.getString(lowestStockDaysMap.get(returnMap.get("BIN_ProductVendorID")));
                    if(lowestStockDays.equals("")){
                        lowestStockDays = "30";
                    }
                    int safeDay = CherryUtil.obj2int(lowestStockDays);
                    String strAdtCoefficient = ConvertUtil.getString(adtCoefficientMap.get(returnMap.get("BIN_ProductVendorID")));
                    if(strAdtCoefficient.equals("")){
                        strAdtCoefficient = "1.0";
                    }
                    BigDecimal bdAdtCoefficient = new BigDecimal(strAdtCoefficient);
                    //建议订单数 = 近30天日均销量×安全库存天数（30） × 系数 - 当前库存
                    int suggestedQuantity = (int) Math.ceil(bdAdSaleQuantity
                            .multiply(new BigDecimal(safeDay))
                            .multiply(bdAdtCoefficient).doubleValue())
                            - CherryUtil.obj2int(returnMap.get("OrderStock"));
                    
                    if(suggestedQuantity < 0){
                        suggestedQuantity = 0;
                    }
                    returnMap.put("SuggestedQuantity", suggestedQuantity);
                }
            }
        }
        
        ConvertUtil.setResponseByAjax(response, resultList);
    }
    
    /**
     * 提交时验证数据
     */
    public boolean validateSubmit(){
        boolean isCorrect = true;
        if(null == form.getOutOrganizationID() || "".equals(form.getOutOrganizationID())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getDepotInfoIdAccept() || "".equals(form.getDepotInfoIdAccept())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00008")}));
            isCorrect = false;
            return isCorrect;
        }
        if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length <= 0){
            this.addActionError(getText("EST00022"));
            isCorrect = false;
            return isCorrect;
        }
        for(int i=0;i<form.getProductVendorIDArr().length;i++){
            if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i])){
                this.addActionError(getText("EST00008"));
                isCorrect = false;
                return isCorrect;
            }
        }
        return isCorrect;
    }
    
    /**
     * 验证数据
     */
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if ("501".equals(actionID) || "504".equals(actionID) || "521".equals(actionID) || "522".equals(actionID)
                || "523".equals(actionID) || "524".equals(actionID) || "525".equals(actionID) || "526".equals(actionID)
                || "581".equals(actionID) || "584".equals(actionID) || "585".equals(actionID) || "601".equals(actionID)
                || "611".equals(actionID) || "614".equals(actionID) || "615".equals(actionID) || "801".equals(actionID)
                || "803".equals(actionID) || "804".equals(actionID)) {
            if(null == form.getOutOrganizationID() || "".equals(form.getOutOrganizationID())){
                this.addActionError(getText("EST00021",new String[]{getText("PST00007")}));
                return;
            }
            if(null == form.getDepotInfoIdAccept() || "".equals(form.getDepotInfoIdAccept())){
                this.addActionError(getText("EST00021",new String[]{getText("PST00008")}));
                return;
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
        if("801".equals(actionID)){
            if(null != form.getDeliverNoIF() && !"".equals(form.getDeliverNoIF())){
                String deliverNoIF = form.getDeliverNoIF();
                // 发货单号不能超过40位验证
                if(deliverNoIF.length() > 40) {
                    this.addFieldError("deliverNoIF", getText("ECM00020",new String[]{getText("PST00028"),"40"}));
                    return;
                }
                // 发货单号英数字验证
                if(!CherryChecker.isAlphanumeric(deliverNoIF)) {
                    this.addFieldError("deliverNoIF", getText("ECM00031",new String[]{getText("PST00028")}));
                    return;
                }
                UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
                // 验证发货单号不存在
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("DeliverNoIF", deliverNoIF);
                paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
                paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
                List<Map<String,Object>> checkList = binOLSTCM03_BL.selPrtDeliverList(paramMap);
                if(null !=checkList && checkList.size()>0){
                    this.addFieldError("deliverNoIF", getText("ECM00032",new String[]{getText("PST00028")}));
                    return;
                }
            }
        }
        String expectDeliverDate = ConvertUtil.getString(form.getExpectDeliverDate());
        if(!"".equals(expectDeliverDate) && !CherryChecker.checkDate(expectDeliverDate)){
            this.addActionError(getText("ECM00008",new String[]{getText("PST00029")}));
            return;
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
        
        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getDepotInfoIdAccept()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getLogicDepotsInfoIdAccept()));
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
        
        //取得系统配置项 订货最小产品数量
        String minQuantityForbiddenString = ConvertUtil.getString(binOLCM14_BL.getConfigValue("1372", organizationInfoID, brandInfoId));
        if (!minQuantityForbiddenString.equals("")){
        	 int minQuantityForbidden = ConvertUtil.getInt(minQuantityForbiddenString);
             
             String[] QuantityArr = form.getQuantityArr();
             for (int i=0;i<QuantityArr.length;i++){
             	if (ConvertUtil.getInt(QuantityArr[i])<minQuantityForbidden){
             		String message=ConvertUtil.getString(minQuantityForbidden);
             		this.addActionError(getText("EST00046", new String[]{getText(message)}));
                     isCorrect = false;
                     return isCorrect;
             	}
             }
        }
       
        
        //产品订单数必须大于系统配置项所配置的金额
        String minAmountForbiddenString  = ConvertUtil.getString(binOLCM14_BL.getConfigValue("1373", organizationInfoID, brandInfoId));
        if (!minAmountForbiddenString.equals("")){
        	int minAmountForbidden = ConvertUtil.getInt(minAmountForbiddenString);
            double totalAmountCheck=ConvertUtil.getDouble((form.getTotalAmountCheck()));
            if (totalAmountCheck<minAmountForbidden){
            	String message=ConvertUtil.getString(minAmountForbidden);
            	this.addActionError(getText("EST00045", new String[]{getText(message)}));//要在message中增加消息字段
                isCorrect = false;
                return isCorrect;
            }
        }
        

        return isCorrect;
    }
}
