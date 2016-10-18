/*  
 * @(#)BINOLSTSFH03_BL.java     1.0 2011/09/09      
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
package com.cherry.st.sfh.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM22_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.service.BINOLSTCM02_Service;
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH03_IF;
import com.cherry.st.sfh.service.BINOLSTSFH03_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;

/**
 * 
 * 订货单明细一览
 * @author niushunjie
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH03_BL extends SsBaseBussinessLogic implements BINOLSTSFH03_IF{
    @Resource(name="workflow")
    private Workflow workflow;
    
	@Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_BL binOLCM22_BL;
	
	@Resource(name="binOLSTSFH03_Service")
	private BINOLSTSFH03_Service binOLSTSFH03_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM02_Service")
    private BINOLSTCM02_Service binOLSTCM02_Service;
	
    @Resource(name="binOLSTCM03_BL")
    private BINOLSTCM03_IF binOLSTCM03_BL;
	
    @Override
    public int tran_delete(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductOrderID", form.getProductOrderID());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTSFH03");
        param.put("UpdateTime", form.getUpdateTime());
        param.put("ModifyCount", form.getModifyCount());
        //伦理删除该ID订单从表数据
        binOLSTSFH03_Service.deleteProductOrderDetailLogic(param);
        //伦理删除该ID订单主表数据
        return binOLSTSFH03_Service.deleteProductOrderLogic(param);
    }
    
    /**
     * 暂存发货单----对于已经删除的明细，只是将核准数量设置为0，不做真正的删除
     * @param form
     * @param userInfo
     * @return
     */
    private int saveOrderTemp(BINOLSTSFH03_Form form, UserInfo userInfo) {
    	
    	Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductOrderID", form.getProductOrderID());
        
        // 当前保存的产品明细删除重新插入----不在当前保存的产品明细，将核准数量更新为0；
        String[] productVendorIDArr = form.getProductVendorIDArr();
        
        param.put("prtVendorId", productVendorIDArr);
        // 物理删除该ID订单从表数据
        binOLSTSFH03_Service.deleteProductOrderDetail(param);
        // 剩余的需要将核准数置为0的明细数据量
        int surplusMapSize = 0;
        List<Map<String, Object>> surplusProductOrderDetail = binOLSTSFH03_Service.getOrderDetailPrtVentorID(param);
        if(null != surplusProductOrderDetail && surplusProductOrderDetail.size() > 0) {
        	surplusMapSize = surplusProductOrderDetail.size();
        	for(int i=0; i < surplusMapSize; i++) {
        		Map<String, Object> surplusMap = surplusProductOrderDetail.get(i);
        		surplusMap.put("DetailNo", i+1);
        		// 更新余下的该ID订单从表的核准数量
                binOLSTSFH03_Service.setProductOrderDetailZero(surplusMap);
        	}
        }
        
        // 是否编辑状态,ModifiedFlag默认为非编辑状态，为1时为编辑状态
		boolean isModifiedStatus = CherryChecker.isNullOrEmpty(
				form.getModifiedFlag(), true) ? false : "1".equals(form
				.getModifiedFlag());
        
        //一次订货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //建议总数量
        int suggestedTotalQuantity = 0;
        //申请总数量
        int applyTotalQuantity = 0;
        //插入订单明细数据
        //List list = new ArrayList();
        String[] suggestedQuantityArr = form.getSuggestedQuantityArr();
        String[] applyQuantityArr = form.getApplyQuantityArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] inventoryInfoIDAcceptArr = form.getInventoryInfoIDAcceptArr();
        String[] logicInventoryInfoIDAcceptArr = form.getLogicInventoryInfoIDAcceptArr();
        String[] commentsArr = form.getCommentsArr();
		for (int i = 0; i < productVendorIDArr.length; i++) {
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            suggestedTotalQuantity += CherryUtil.string2int(suggestedQuantityArr[i]);
            applyTotalQuantity += CherryUtil.string2int(applyQuantityArr[i]);
            
            Map<String,Object> productOrderDetail = new HashMap<String,Object>();
            productOrderDetail.put("BIN_ProductOrderID", form.getProductOrderID());
            productOrderDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productOrderDetail.put("DetailNo", surplusMapSize+i+1);
            productOrderDetail.put("SuggestedQuantity", suggestedQuantityArr[i]);
            productOrderDetail.put("ApplyQuantity", applyQuantityArr[i]);
            productOrderDetail.put("Quantity", quantityArr[i]);
            productOrderDetail.put("BIN_InventoryInfoID", inventoryInfoIDArr[i]);
            productOrderDetail.put("Price", priceUnitArr[i]);
            if(isModifiedStatus) {
            	// 编辑状态时将价格记录下来
            	productOrderDetail.put("EditPrice", priceUnitArr[i]);
            }
            productOrderDetail.put("BIN_ProductVendorPackageID", CherryUtil.obj2int(productVendorPackageIDArr[i]));
            productOrderDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(logicInventoryInfoIDArr[i]));
            productOrderDetail.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(storageLocationInfoIDArr[i]));
            productOrderDetail.put("BIN_InventoryInfoIDAccept", CherryUtil.obj2int(inventoryInfoIDAcceptArr[i]));
            productOrderDetail.put("BIN_LogicInventoryInfoIDAccept", CherryUtil.obj2int(logicInventoryInfoIDAcceptArr[i]));
            productOrderDetail.put("Comments", commentsArr[i]);
            productOrderDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("CreatePGM", "BINOLSTSFH03");
            productOrderDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("UpdatePGM", "BINOLSTSFH03");
            //list.add(productOrderDetail);
            binOLSTCM02_Service.insertProductOrderDetail(productOrderDetail);
        }
        
        //更新订单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTSFH03");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        param.put("BIN_OrganizationIDAccept",form.getOutOrganizationID());
        param.put("BIN_InventoryInfoIDAccept",form.getDepotInfoIdAccept());
        param.put("BIN_LogicInventoryInfoIDAccept",form.getLogicDepotsInfoIdAccept());
        param.put("OrderType", form.getOrderType());
        if(!ConvertUtil.getString(form.getExpectDeliverDate()).equals("")){
            param.put("ExpectDeliverDate", form.getExpectDeliverDate());
        }else{
            //清空ExpectDeliverDate
            param.put("EmptyExpectDeliverDate", "true");
        }
        param.put("SuggestedQuantity", suggestedTotalQuantity);
        param.put("ApplyQuantity", applyTotalQuantity);
        int ret = binOLSTCM02_BL.updateProductOrderMain(param);
        
        return ret;
    }
    
    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveOrder(BINOLSTSFH03_Form form, UserInfo userInfo){

        Map<String,Object> param = new HashMap<String,Object>();
        int productOrderIDOld = CherryUtil.obj2int(form.getProductOrderID());
        param.put("BIN_ProductOrderID", productOrderIDOld);
        //取得订货单明细表
        List<Map<String,Object>> oldOrderList = binOLSTCM02_BL.getProductOrderDetailData(productOrderIDOld, null, null);
        //逻辑删除该ID订单从表数据
        binOLSTSFH03_Service.deleteProductOrderDetail(param);
        
        // 是否编辑状态,ModifiedFlag默认为非编辑状态，为1时为编辑状态
 		boolean isModifiedStatus = CherryChecker.isNullOrEmpty(
 				form.getModifiedFlag(), true) ? false : "1".equals(form
 				.getModifiedFlag());
        //一次订货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //建议总数量
        int suggestedTotalQuantity = 0;
        //申请总数量
        int applyTotalQuantity = 0;
        //插入订单明细数据
        //List list = new ArrayList();
        String[] productVendorIDArr = form.getProductVendorIDArr();
        String[] suggestedQuantityArr = form.getSuggestedQuantityArr();
        String[] applyQuantityArr = form.getApplyQuantityArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] inventoryInfoIDAcceptArr = form.getInventoryInfoIDAcceptArr();
        String[] logicInventoryInfoIDAcceptArr = form.getLogicInventoryInfoIDAcceptArr();
        String[] commentsArr = form.getCommentsArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            suggestedTotalQuantity += CherryUtil.string2int(suggestedQuantityArr[i]);
            applyTotalQuantity += CherryUtil.string2int(applyQuantityArr[i]);
            
            Map<String,Object> productOrderDetail = new HashMap<String,Object>();
            productOrderDetail.put("BIN_ProductOrderID", form.getProductOrderID());
            productOrderDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productOrderDetail.put("DetailNo", i+1);
            productOrderDetail.put("SuggestedQuantity", suggestedQuantityArr[i]);
            productOrderDetail.put("ApplyQuantity", applyQuantityArr[i]);
            productOrderDetail.put("Quantity", quantityArr[i]);
            productOrderDetail.put("BIN_InventoryInfoID", inventoryInfoIDArr[i]);
            productOrderDetail.put("Price", priceUnitArr[i]);
            if(isModifiedStatus) {
            	// 编辑状态时将价格记录下来
            	productOrderDetail.put("EditPrice", priceUnitArr[i]);
            } else {
            	if(null != oldOrderList) {
	            	for(Map<String, Object> oldOrderDeatilMap : oldOrderList) {
	        			if(productVendorIDArr[i].equals(ConvertUtil.getString(oldOrderDeatilMap.get("BIN_ProductVendorID")))) {
	        				// 发货时取的总成本价更新到订货单中
	        				productOrderDetail.put("EditPrice",oldOrderDeatilMap.get("EditPrice"));
	        			}
	                }
            	}
            }
            productOrderDetail.put("BIN_ProductVendorPackageID", CherryUtil.obj2int(productVendorPackageIDArr[i]));
            productOrderDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(logicInventoryInfoIDArr[i]));
            productOrderDetail.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(storageLocationInfoIDArr[i]));
            productOrderDetail.put("BIN_InventoryInfoIDAccept", CherryUtil.obj2int(inventoryInfoIDAcceptArr[i]));
            productOrderDetail.put("BIN_LogicInventoryInfoIDAccept", CherryUtil.obj2int(logicInventoryInfoIDAcceptArr[i]));
            productOrderDetail.put("Comments", commentsArr[i]);
            productOrderDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("CreatePGM", "BINOLSTSFH03");
            productOrderDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("UpdatePGM", "BINOLSTSFH03");
            //list.add(productOrderDetail);
            binOLSTCM02_Service.insertProductOrderDetail(productOrderDetail);
        }
        
        //更新订单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTSFH03");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        param.put("BIN_OrganizationIDAccept",form.getOutOrganizationID());
        param.put("BIN_InventoryInfoIDAccept",form.getDepotInfoIdAccept());
        param.put("BIN_LogicInventoryInfoIDAccept",form.getLogicDepotsInfoIdAccept());
        param.put("OrderType", form.getOrderType());
        if(!ConvertUtil.getString(form.getExpectDeliverDate()).equals("")){
            param.put("ExpectDeliverDate", form.getExpectDeliverDate());
        }else{
            //清空ExpectDeliverDate
            param.put("EmptyExpectDeliverDate", "true");
        }
        param.put("SuggestedQuantity", suggestedTotalQuantity);
        param.put("ApplyQuantity", applyTotalQuantity);
        int ret = binOLSTCM02_BL.updateProductOrderMain(param);
        
        //启动工作流后点击保存按钮写操作日志
        String entryID = ConvertUtil.getString(form.getEntryID());
        if(!"".equals(entryID) && "".equals(form.getActionID())){
            int productOrderID = CherryUtil.obj2int(form.getProductOrderID());
            Map<String,Object> productOrderMainData = binOLSTCM02_BL.getProductOrderMainData(productOrderID, null);
            Map<String, Object> logMap = new HashMap<String, Object>();
            //  工作流实例ID
            logMap.put("WorkFlowID",entryID);
            //操作部门
            logMap.put("BIN_OrganizationID",userInfo.getBIN_OrganizationID());
            //操作员工
            logMap.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
            //操作业务类型
            logMap.put("TradeType",CherryConstants.OS_BILLTYPE_OD);
            //表名
            logMap.put("TableName", "Inventory.BIN_ProductOrder");
            //单据ID
            logMap.put("BillID",productOrderID);
            //单据编号
            logMap.put("BillNo", productOrderMainData.get("OrderNoIF"));
            // 1131：操作代码--32：订货单修改；42：发货单修改
            logMap.put("OpCode","32");
            // 1152：操作结果--106：修改单据
            logMap.put("OpResult","106");
            //作成者   
            logMap.put("CreatedBy",userInfo.getBIN_UserID());
            //作成程序名
            logMap.put("CreatePGM","BINOLSTSFH03");
            //更新者
            logMap.put("UpdatedBy",userInfo.getBIN_UserID());
            //更新程序名
            logMap.put("UpdatePGM","BINOLSTSFH03");
            binOLCM22_BL.insertInventoryOpLog(logMap);
        }
        
        return ret;
    }   
    
    @Override
    public int tran_save(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception{
        return saveOrder(form,userInfo);
    }
    
    @Override
    public int tran_saveTemp(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception{
    	return saveOrderTemp(form,userInfo);
    }

    @Override
    public int tran_submit(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception {
    	int ret =0;
    	if("2".equals(form.getOperateType())){
    		//非工作流编辑模式下提交，则先保存单据，再开始工作流
    		ret = saveOrder(form,userInfo);
    		if(ret==0){
    			throw new CherryException("ECM00038");
    		}
    		//准备参数，开始工作流
    		Map<String,Object> pramMap = new HashMap<String,Object>();
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProductOrderID());
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
    		pramMap.put("CurrentUnit", "BINOLSTSFH03");
    		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
    		pramMap.put("UserInfo", userInfo);
    		binOLSTCM00_BL.StartOSWorkFlow(pramMap);	
    	}
        return ret;
    }

	@Override
	public void tran_doaction(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception {
		//String ret = "0";
		if (CherryConstants.OPERATE_OD_AUDIT.equals(form.getOperateType())) {
			// 审核模式，推动工作流
            int count = saveOrder(form, userInfo);
            if (count == 0) {
                throw new CherryException("ECM00038");
            }
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTSFH03");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("OrderForm", form);
            pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());//品牌编号
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
            }
            pramMap.put("UpdateAudit", "true");
            pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
			//ret = String.valueOf(pramMap.get(CherryConstants.OS_CHANGE_COUNT));
        } else if (CherryConstants.ENUMODAUDIT.isAudit(form.getOperateType())) {
            //二审/三审/四审...模式，推动工作流
            // 先保存单据，推动工作流
		    int count = saveOrder(form, userInfo);
		    if (count == 0) {
		        throw new CherryException("ECM00038");
		    }
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTSFH03");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());//品牌编号
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
            }
            pramMap.put("OpComments", form.getOpComments());
            pramMap.put("OrderForm", form);
            binOLSTCM00_BL.DoAction(pramMap);
		}else if (CherryConstants.OPERATE_OD_EDIT.equals(form.getOperateType())) {
			// 编辑模式 【提交】【 删除】是读取工作流配置，【保存】按钮是固定有的，执行的是tran_save
			// 先保存单据，再推动工作流
//			int count = saveOrder(form, userInfo);
//			if (count == 0) {
//				throw new CherryException("ECM00038");
//			}
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTSFH03");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());//品牌编号
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
            }
            pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
		}else if(CherryConstants.OPERATE_SD_CREATE.equals(form.getOperateType()) 
		        || CherryConstants.OPERATE_OD_SUBMIT.equals(form.getOperateType())
		        || CherryConstants.OPERATE_SD.equals(form.getOperateType())){
		    //生成发货单模式 【生成发货单】【废弃订单】
		    //订单提交模式【提交】【废弃】
		    //发货模式【发货】
		    
            // 发货步骤（非发货Action）先保存，再推动工作流
            if(CherryConstants.OPERATE_SD.equals(form.getOperateType())){
                if(!"801".equals(form.getActionID())){
                    int count = saveOrder(form, userInfo);
                    if (count == 0) {
                        throw new CherryException("ECM00038");
                    }
                }
            }
		    
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTSFH03");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());//品牌编号
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
            }
            pramMap.put("OpComments", form.getOpComments());
            if(null != form.getDeliverNoIF() && !"".equals(form.getDeliverNoIF())){
                pramMap.put("DeliverNoIF", form.getDeliverNoIF());
            }
            pramMap.put("OrderForm", form);
            binOLSTCM00_BL.DoAction(pramMap);
		}
		
        if (CherryConstants.ENUMODAUDIT.isAudit(form.getOperateType())) {
		    //大陆幽兰自动发货写日志
		    long osID = Long.parseLong(form.getEntryID());
            String workflowName = workflow.getWorkflowName(osID);
            boolean isProFlowOD_YT_FN = binOLSTCM02_BL.isProFlowOD_YT_FN(workflowName);
            PropertySet ps = workflow.getPropertySet(osID);
            String currentOperate = ps.getString("OS_Current_Operate");
            boolean brandFlag = binOLSTCM02_BL.checkWorkFlowBrand(workflowName, "WET","ADLS");
            if((isProFlowOD_YT_FN || brandFlag) && CherryConstants.OPERATE_RD.equals(currentOperate)){
                int productDeliverID = ps.getInt("BIN_ProductDeliverID");
                Map<String,Object> mainData = binOLSTCM03_BL.getProductDeliverMainData(productDeliverID, null);
                
                //写入操作日志-生成发货单
                Map<String, Object> logMap = new HashMap<String, Object>();
                //  工作流实例ID
                logMap.put("WorkFlowID",osID);
                //操作部门
                logMap.put("BIN_OrganizationID",userInfo.getBIN_OrganizationID());
                //操作员工
                logMap.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
                //操作业务类型
                logMap.put("TradeType",CherryConstants.OS_BILLTYPE_OD);
                //表名
                logMap.put("TableName", "Inventory.BIN_ProductDeliver");
                //单据ID
                logMap.put("BillID",productDeliverID);
                //单据Code
                logMap.put("BillNo", mainData.get("DeliverNoIF"));
                //操作代码
                logMap.put("OpCode",CherryConstants.OPERATE_SD_CREATE);
                //操作结果
                logMap.put("OpResult",105);//已发货
                //作成者   
                logMap.put("CreatedBy",userInfo.getBIN_UserID()); 
                //作成程序名
                logMap.put("CreatePGM","BINOLSTSFH03");
                //更新者
                logMap.put("UpdatedBy",userInfo.getBIN_UserID());
                //更新程序名
                logMap.put("UpdatePGM","BINOLSTSFH03");
                binOLCM22_BL.insertInventoryOpLog(logMap);
            }
		}
	}
	
    /**
     * 取得近30天销量
     * @param paramMap
     * @return
     */
	@Override
	public List<Map<String,Object>> getSaleQuantity(Map<String,Object> paramMap){
        return binOLSTSFH03_Service.getSaleQuantity(paramMap);
	}

    /**
     * 取得安全库存天数
     * @param paramMap
     * @return
     */
    @Override
    public List<Map<String, Object>> getLowestStockDays(Map<String, Object> paramMap) {
        return binOLSTSFH03_Service.getLowestStockDays(paramMap);
    }

    /**
     * 取得系数
     * @param paramMap
     * @return
     */
    @Override
    public List<Map<String, Object>> getAdtCoefficient(Map<String, Object> paramMap) {
        return binOLSTSFH03_Service.getAdtCoefficient(paramMap);
    }

    /**
     * 根据工作流ID取得产品发货单ID
     * @param WorkFlowID
     * @return BIN_ProductDeliverID；若查询不到则返回""
     */
	@Override
	public String getDeliverIDByWorkFlowID(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTSFH03_Service.getDeliverIDByWorkFlowID(map);
	}
}
