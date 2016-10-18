/*  
 * @(#)BINOLSTSFH05_BL.java     1.0 2011/09/14      
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM22_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.common.service.BINOLSTCM03_Service;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH05_IF;
import com.cherry.st.sfh.service.BINOLSTSFH05_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;

/**
 * 
 * 产品发货单详细BL
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public class BINOLSTSFH05_BL extends SsBaseBussinessLogic implements BINOLSTSFH05_IF{
	
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
	@Resource(name="binOLSTSFH05_Service")
    private BINOLSTSFH05_Service binOLSTSFH05_Service;
	
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM03_Service")
    private BINOLSTCM03_Service binOLSTCM03_Service;
    
    @Resource(name="binOLSTCM11_BL")
    private BINOLSTCM11_IF binOLSTCM11_BL;
    
    @Resource(name="binOLSTCM03_BL")
    private BINOLSTCM03_IF binOLSTCM03_BL;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_BL binOLCM22_BL;
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    /**
     * 工作流中的各种动作
     * @author zhanggl
     * @version 1.0 2011.11.04
     * 
     * */
    @Override
    public void tran_doaction(BINOLSTSFH05_Form form, UserInfo userInfo)
            throws Exception {
//    	//发货时和收货时不保存发货单。订单二审通过、订单二审退回、订单二审退回后再次提交不在这里保存发货单。
//    	if(!"801".equals(form.getActionID())&&!"101".equals(form.getActionID())&&!"521".equals(form.getActionID())&&!"522".equals(form.getActionID())&&!"601".equals(form.getActionID())){
//    	    //不论是审核通过还是审核退回都要先保存订单
//            int ret = 0;
//            ret = this.saveProductDeliver(form, userInfo);
//            if(ret == 0){
//                throw new CherryException("ECM00038");
//            }
//    	}
        
        //判断是否发货工作流
        boolean updatedSDDetailFlag = false;
        long workFlowID = Long.parseLong(form.getEntryID());
        if (workflow.getWorkflowName(workFlowID).indexOf("productDeliver") > 0) {
            if (CherryConstants.OPERATE_SD_AUDIT.equals(form.getOperateType())
                    || CherryConstants.OPERATE_SD_AUDIT2.equals(form.getOperateType())) {
                Map<String, Object> pramData = new HashMap<String, Object>();
                // 发货单主表ID
                pramData.put("BIN_ProductDeliverID", form.getProductDeliverId());
                pramData.put("UpdatedBy", userInfo.getBIN_UserID());
                pramData.put("UpdatePGM", "BINOLSTSHF06");
                pramData.put("BINOLSTSFH05_Form", form);
                binOLSTCM03_BL.updateProductDeliverByForm(pramData);
                updatedSDDetailFlag = true;
            }
        }
    	
        //订单流程后台收货
        try{
            PropertySet propertyset = workflow.getPropertySet(Long.parseLong(form.getEntryID()));
            int productOrderID = propertyset.getInt("BIN_ProductOrderID");
            if(productOrderID >0 && "101".equals(form.getActionID())){
                int maindeliverid = receiveDeliverByForm(form, "BINOLSTSFH05",userInfo.getBIN_UserID(),userInfo.getBIN_EmployeeID());
                
                //将收货数据写入入出库表
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_ProductReceiveID", maindeliverid);
                paramMap.put("CreatedBy", userInfo.getBIN_UserID());
                paramMap.put("CreatePGM", "BINOLSTSFH05");
                binOLSTCM11_BL.createProductInOutByReceiveID(paramMap);
                
                //收货单ID写入PS表
                propertyset.setInt("BIN_ProductReceiveID", maindeliverid);
            }
        }catch(Exception e){
            
        }
        
        Map<String, Object> pramMap = new HashMap<String, Object>();
        pramMap.put("entryID", form.getEntryID());
        pramMap.put("actionID", form.getActionID());
        pramMap.put("DeliverType", form.getDeliverType());
        pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put("CurrentUnit", "BINOLSTSFH05");
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BIN_OrganizationID", userInfo.getBIN_OrganizationID());
        pramMap.put("BrandCode",userInfo.getBrandCode());
        pramMap.put("OrganizationCode",userInfo.getDepartCode());
        pramMap.put("Date", binOLSTSFH05_Service.getDateYMD());
        pramMap.put("BIN_UserID", userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProductDeliverId());
        pramMap.put("BINOLSTSFH05_Form", form);
        pramMap.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_BACK);
        pramMap.put("OpComments", form.getOpComments());
        if (CherryConstants.OPERATE_OD_AUDIT.equals(form.getOperateType())) {
            pramMap.put("UpdateAudit", "true");
        }
        pramMap.put("updatedSDDetailFlag", updatedSDDetailFlag);
        binOLSTCM00_BL.DoAction(pramMap);
    }
    
    /**
     * 保存单据,在编辑,审核模式都要先进行保存然后再做后面的操作
     * @author zhanggl
     * @version 1.0 2011.11.04
     * 
     * */
    private int saveProductDeliver(BINOLSTSFH05_Form form, UserInfo userInfo){
    	
    	//存放主表信息
    	Map<String,Object> mainDate = new HashMap<String,Object>();
    	//存放明细信息
    	List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
    	
    	//产品厂商ID
    	String[] productVendorIDArr = form.getProductVendorIDArr();
    	//数量
    	String[] quantityArr = form.getQuantityArr();
    	//参考价格
    	String[] referencePriceArr = form.getReferencePriceArr();
    	//价格
    	String[] priceUnitArr = form.getPriceUnitArr();
    	//备注
    	String[] commentsArr = form.getCommentsArr();
    	
    	//发货单主表ID
    	String productDeliverId = form.getProductDeliverId();
    	//发货实体仓库ID
    	String outDepotInfoID = form.getOutDepotInfoID();
    	//发货逻辑仓库ID
    	String outLogicInventoryInfoID = form.getOutLogicInventoryInfoID();
    	
    	
    	//总金额
        double totalAmount = 0.0;
        //总数量
        int totalQuantity = 0;
        
        //组织发货明细
        for(int i = 0 ; i < productVendorIDArr.length ; i++){
        	//单个产品数量
        	int quantity = Integer.parseInt(quantityArr[i]);
        	//单个产品的价格
        	double amount = Double.parseDouble(priceUnitArr[i]);
        	totalAmount += amount * quantity;
        	totalQuantity += quantity;
        	
        	Map<String , Object> detailMap = new HashMap<String , Object>();
        	//发货单ID
        	detailMap.put("BIN_ProductDeliverID", productDeliverId);
        	//产品厂商ID
        	detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
        	//番号
        	detailMap.put("DetailNo", i+1);
        	//数量
        	detailMap.put("Quantity", quantityArr[i]);
        	//发货实体仓库
        	detailMap.put("BIN_InventoryInfoID", outDepotInfoID);
        	//价格
        	detailMap.put("Price", priceUnitArr[i]);
        	//参考价格
        	detailMap.put("ReferencePrice", referencePriceArr[i]);
        	//发货逻辑仓库ID
        	detailMap.put("BIN_LogicInventoryInfoID", outLogicInventoryInfoID);
        	//包装类型
        	detailMap.put("BIN_ProductVendorPackageID", 0);
        	//仓库库位
        	detailMap.put("BIN_StorageLocationInfoID", 0);
        	//备注
        	detailMap.put("Comments", commentsArr[i]);
        	//更新者
        	detailMap.put("UpdatedBy", userInfo.getBIN_UserID());
        	//创建者
        	detailMap.put("CreatedBy", userInfo.getBIN_UserID());
        	//更新程序
        	detailMap.put("UpdatePGM", "BINOLSTSHF06");
        	//创建程序
        	detailMap.put("CreatePGM", "BINOLSTSHF06");
        	
        	detailList.add(detailMap);
        }
        
        //组织发货单主表信息
        //发货单ID
        mainDate.put("BIN_ProductDeliverID", productDeliverId);
        //发货类型
        mainDate.put("DeliverType", form.getDeliverType());
        //发货部门ID
        mainDate.put("BIN_OrganizationID",form.getOutOrganizationID());
        //发货实体仓库ID
        mainDate.put("BIN_DepotInfoID", outDepotInfoID);
        //发货逻辑仓库ID
        mainDate.put("BIN_LogicInventoryInfoID", outLogicInventoryInfoID);
        if(ConvertUtil.getString(form.getPlanArriveDate()).equals("")){
            //预计到货日期清空
            mainDate.put("EmptyPlanArriveDate","true");
        }else{
            //预计到货日期
            mainDate.put("PlanArriveDate",form.getPlanArriveDate());
        }
        //上一次的更新时间
        mainDate.put("OldUpdateTime", form.getUpdateTime());
        //更新次数
        mainDate.put("OldModifyCount", form.getModifyCount());
        //总数量
        mainDate.put("TotalQuantity", totalQuantity);
        //总金额
        mainDate.put("TotalAmount", totalAmount);
        //更新程序
        mainDate.put("UpdatePGM", "BINOLSTSHF06");
    	//更新者
        mainDate.put("UpdatedBy", userInfo.getBIN_UserID());
    	
        //删除明细
        binOLSTSFH05_Service.deleteProductDeliverDetail(mainDate);
        //插入明细
        for(Map<String,Object> map : detailList){
        	binOLSTCM03_Service.insertProductDeliverDetail(map);
        }
        
    	return binOLSTCM03_Service.updateProductDeliverMain(mainDate);
    }

    /**
     * 保存发货单
     * @author zhanggl
     * @version 1.0 2011.11.04
     * 
     * */
	@Override
	public void tran_save(BINOLSTSFH05_Form form, UserInfo userInfo)
			throws Exception {
		int ret = 0;
		//保存
		ret = this.saveProductDeliver(form, userInfo);
		
        //启动工作流后保存写操作日志
        String entryID = ConvertUtil.getString(form.getEntryID());
        if(!"".equals(entryID)){
            int productDeliverID = CherryUtil.obj2int(form.getProductDeliverId());
            Map<String,Object> productDeliverMainData = binOLSTCM03_BL.getProductDeliverMainData(productDeliverID, null);
            Map<String, Object> logMap = new HashMap<String, Object>();
            //  工作流实例ID
            logMap.put("WorkFlowID",entryID);
            //操作部门
            logMap.put("BIN_OrganizationID",userInfo.getBIN_OrganizationID());
            //操作员工
            logMap.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
            //操作业务类型
            logMap.put("TradeType",CherryConstants.OS_BILLTYPE_SD);
            //表名
            logMap.put("TableName", "Inventory.BIN_ProductDeliver");
            //单据ID
            logMap.put("BillID",productDeliverID);
            //单据编号
            logMap.put("BillNo", productDeliverMainData.get("DeliverNoIF"));
            //操作代码
            logMap.put("OpCode","42");
            //操作结果
            logMap.put("OpResult","106");
            //作成者   
            logMap.put("CreatedBy",userInfo.getBIN_UserID()); 
            //作成程序名
            logMap.put("CreatePGM","BINOLSTSFH05");
            //更新者
            logMap.put("UpdatedBy",userInfo.getBIN_UserID()); 
            //更新程序名
            logMap.put("UpdatePGM","BINOLSTSFH05");
            binOLCM22_BL.insertInventoryOpLog(logMap);
        }
		
		if(ret == 0){
			throw new CherryException("ECM00038");
		}
	}

	/**
	 * 提交发货单
     * @author zhanggl
     * @version 1.0 2011.11.04
     * 
     * */
	@Override
	public int tran_submit(BINOLSTSFH05_Form form, UserInfo userInfo)
			throws Exception {
		// TODO Auto-generated method stub
		int ret = 0;
		//保存提交
		ret = this.saveProductDeliver(form, userInfo);
		
		if(ret == 0){
			throw new CherryException("ECM00038");
		}
		
		//准备参数，开始工作流
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID,form.getProductDeliverId());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put("CurrentUnit", "BINOLSTSHF06");
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("BIN_OrganizationIDReceive", form.getInOrganizationID());
		pramMap.put("DeliverType", form.getDeliverType());
		pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode",userInfo.getBrandCode());
        Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
        if(null != departmentInfo && !departmentInfo.isEmpty()){
            pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
        }
		binOLSTCM00_BL.StartOSWorkFlow(pramMap);
		
		return 0;
	}
	
    @Override
    public int tran_delete(BINOLSTSFH05_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductDeliverID", form.getProductDeliverId());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTSFH05");
        param.put("UpdateTime", form.getUpdateTime());
        param.put("ModifyCount", form.getModifyCount());
        //伦理删除该ID订单从表数据
        binOLSTSFH05_Service.deleteProductDeliverDetailLogic(param);
        //伦理删除该ID订单主表数据
        return binOLSTSFH05_Service.deleteProductDeliverLogic(param);
    }
	
	/**
	 * 非柜台收货
	 * @author zhanggl
     * @version 1.0 2011.12.02
	 * */
	public int receiveDeliverByForm(BINOLSTSFH05_Form form,String currentUnit,int userID,int employeeID){
		
		//发货单ID
		String productDeliverId = form.getProductDeliverId();
		//发货部门ID
		String outOrganizationId = form.getOutOrganizationID();
		//收货部门ID
		String receiveDepartId = form.getInOrganizationID();
		//收货实体仓库
		String receiveDepotId = form.getReceiveDepot();
		//收货逻辑仓库
		String receiveLogiInvenId = form.getReceiveLogiInven();
		//画面上是否有编辑的收货单
		String receiveEdit = ConvertUtil.getString(form.getReceiveEdit());
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("BIN_ProductDeliverID", productDeliverId);
		//根据发货单ID取得发货单明细信息
		List<Map<String,Object>> deliverDetailList = binOLSTCM03_BL.getProductDeliverDetailData(Integer.parseInt(productDeliverId),null,null);
		//根据发货单ID取得发货单主表信息
		Map<String,Object> deliverMainData = binOLSTCM03_BL.getProductDeliverMainData(Integer.parseInt(productDeliverId),null);
		
		//存放收货单主表信息
		Map<String,Object> receiveMainData = new HashMap<String,Object>();
		//存放收货单明细信息
		List<Map<String,Object>> receiveDetailList = new ArrayList<Map<String,Object>>();
		
		//主表. 组织ID
		receiveMainData.put("BIN_OrganizationInfoID", deliverMainData.get("BIN_OrganizationInfoID"));
		//主表. 品牌ID
		receiveMainData.put("BIN_BrandInfoID", deliverMainData.get("BIN_BrandInfoID"));
		//主表. 单据号
		receiveMainData.put("ReceiveNo", null);
		//主表. 接口单据号
		receiveMainData.put("ReceiveNoIF", null);
		//主表. 关联单据号
		receiveMainData.put("RelevanceNo", deliverMainData.get("DeliverNoIF"));
		//主表. 发货部门ID
		receiveMainData.put("BIN_OrganizationIDSend", outOrganizationId);
		//主表. 收货部门ID
		receiveMainData.put("BIN_OrganizationIDReceive", receiveDepartId);
		//主表. 制表人员ID
		String tradeEmployeeID = ConvertUtil.getString(form.getTradeEmployeeID());
		if(tradeEmployeeID.equals("")){
		      receiveMainData.put("BIN_EmployeeID", employeeID);
		}else{
		      receiveMainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
		}
		//主表. 下单部门ID
		receiveMainData.put("BIN_OrganizationIDDX", receiveDepartId);
		//主表. 下单员工ID
        receiveMainData.put("BIN_EmployeeIDDX", employeeID);
		//主表. 审核人员ID
		receiveMainData.put("BIN_EmployeeIDAudit", employeeID);
		//主表. 总数量
		receiveMainData.put("TotalQuantity", deliverMainData.get("TotalQuantity"));
		//主表. 总金额
		receiveMainData.put("TotalAmount", deliverMainData.get("TotalAmount"));
		//主表. 审核区分.审核通过
		receiveMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		//主表. 数据同步区分.待同步
		receiveMainData.put("SynchFlag", CherryConstants.BILL_SYNCHFLAG_1);
		//主表. 单据状态.已入库
		receiveMainData.put("TradeStatus", CherryConstants.PROM_RECEIVE_STOCK_IN);
		//主表. 物流ID
		receiveMainData.put("BIN_LogisticInfoID", null);
		//主表. 备注
		receiveMainData.put("Comments", form.getComments().trim());
		//主表. 收货日期
		receiveMainData.put("ReceiveDate", binOLSTSFH05_Service.getDateYMD());
		//工作流ID
        receiveMainData.put("WorkFlowID", deliverMainData.get("WorkFlowID"));
		//主表. 单据创建者ID
		receiveMainData.put("CreatedBy", employeeID);
		//主表. 创建程序
		receiveMainData.put("CreatePGM", currentUnit);
		//主表. 更新者ID
		receiveMainData.put("UpdatedBy", employeeID);
		//主表. 更新程序
		receiveMainData.put("UpdatePGM", currentUnit);
		
		if(receiveEdit.equals("YES")){
		    int totalQuantity = 0;
		    Double totalAmount = 0.0;
	        for(int i=0 ; i<form.getProductVendorIDArr().length; i++){
	            Map<String,Object> receiveMap = new HashMap<String,Object>();
	            //明细.产品厂商ID
	            receiveMap.put("BIN_ProductVendorID", form.getProductVendorIDArr()[i]);
	            //明细.番号
	            receiveMap.put("DetailNo", i+1);
	            //明细.数量
	            receiveMap.put("Quantity", form.getQuantityArr()[i]);
	            //明细.价格
	            receiveMap.put("Price", form.getPriceUnitArr()[i]);
                int quantity = CherryUtil.obj2int(form.getQuantityArr()[i]);
                double price = CherryUtil.string2double(form.getPriceUnitArr()[i]);
	            totalQuantity += quantity;
	            totalAmount += price*quantity;
	            //明细 参考价格
	            receiveMap.put("ReferencePrice", form.getReferencePriceArr()[i]);
	            //明细.产品包装ID
	            receiveMap.put("BIN_ProductVendorPackageID", "0");
	            //明细.收货实体仓库
	            receiveMap.put("BIN_InventoryInfoID", receiveDepotId);
	            //明细.收货逻辑仓库
	            receiveMap.put("BIN_LogicInventoryInfoID", receiveLogiInvenId);
	            //明细.库位
	            receiveMap.put("BIN_StorageLocationInfoID", "0");
	            //明细.备注
	            receiveMap.put("Comments", null);
	            //明细. 单据创建者ID
	            receiveMap.put("CreatedBy", employeeID);
	            //明细. 创建程序
	            receiveMap.put("CreatePGM", currentUnit);
	            //明细. 更新者ID
	            receiveMap.put("UpdatedBy", employeeID);
	            //明细. 更新程序
	            receiveMap.put("UpdatePGM", currentUnit);
	            
	            receiveDetailList.add(receiveMap);
	        }
	        //主表. 总数量
	        receiveMainData.put("TotalQuantity", totalQuantity);
	        //主表. 总金额
	        receiveMainData.put("TotalAmount", totalAmount);
		}else{
		      for(int index=0 ; index<deliverDetailList.size(); index++){
		            //发货单Map
		            Map<String,Object> deliverMap = deliverDetailList.get(index);
		            
		            Map<String,Object> receiveMap = new HashMap<String,Object>();
		            //明细.产品厂商ID
		            receiveMap.put("BIN_ProductVendorID", deliverMap.get("BIN_ProductVendorID"));
		            //明细.番号
		            receiveMap.put("DetailNo", deliverMap.get("DetailNo"));
		            //明细.数量
		            receiveMap.put("Quantity", deliverMap.get("Quantity"));
		            //明细.价格
		            receiveMap.put("Price", deliverMap.get("Price"));
		            //明细 参考价格
		            receiveMap.put("ReferencePrice", deliverMap.get("ReferencePrice"));
		            //明细.产品包装ID
		            receiveMap.put("BIN_ProductVendorPackageID", deliverMap.get("BIN_ProductVendorPackageID"));
		            //明细.收货实体仓库
		            receiveMap.put("BIN_InventoryInfoID", receiveDepotId);
		            //明细.收货逻辑仓库
		            receiveMap.put("BIN_LogicInventoryInfoID", receiveLogiInvenId);
		            //明细.库位
		            receiveMap.put("BIN_StorageLocationInfoID", null);
		            //明细.备注
		            receiveMap.put("Comments", null);
		            //明细. 单据创建者ID
		            receiveMap.put("CreatedBy", employeeID);
		            //明细. 创建程序
		            receiveMap.put("CreatePGM", currentUnit);
		            //明细. 更新者ID
		            receiveMap.put("UpdatedBy", employeeID);
		            //明细. 更新程序
		            receiveMap.put("UpdatePGM", currentUnit);
		            
		            receiveDetailList.add(receiveMap);
		        }
		}
		

		
		return binOLSTCM11_BL.insertProductReceiveAll(receiveMainData, receiveDetailList);
	}
	
}
