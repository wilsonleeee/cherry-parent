/*  
 * @(#)BINOLSTSFH05_Form.java     1.0 2011/09/14      
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
package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品发货单详细Form
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public class BINOLSTSFH05_Form extends DataTable_BaseForm{
    private String entryID;
    private String actionID;
    private String workFlowID;
    /**发货单主表ID*/
    private String productDeliverId;
    
    /**操作备注*/
    private String opComments;
    
    /**明细数量*/
    private String[] quantityArr;
    
    /**明细备注*/
    private String[] commentsArr;
    
    /**明细价格*/
    private String[] priceUnitArr;
    
    /**明细参考价格*/
    private String[] referencePriceArr;
    
    /**明细产品ID*/
    private String[] productVendorIDArr;
    
    /**发货部门ID*/
    private String outOrganizationID;
    
    /**发货仓库ID*/
    private String outDepotInfoID;
    
    /**发货类型*/
    private String deliverType;
    
    /**发货逻辑仓库ID*/
    private String outLogicInventoryInfoID;
    
    /**收货部门ID*/
    private String inOrganizationID;
    
    /**最近一次的更新时间*/
    private String updateTime;
    
    /**最新的更新次数*/
    private String modifyCount;
    
    /**有效性区分*/
    private String verifiedFlag;

    /** 画面操作区分 */
    private String operateType;
    
    /**收货步骤是否存在修改*/
    private String receiveEdit;
    
    /** 发货单概要信息 */
    @SuppressWarnings("unchecked")
    private Map productDeliverMainData;
    
    /** 发货单详细信息 */
    @SuppressWarnings("unchecked")
    private List productDeliverDetailData;
    
    /**收货部门实体仓库*/
    private String receiveDepot;
    
    /**收货逻辑仓库*/
    private String receiveLogiInven;
    
    /**收货备注*/
    private String comments;
    
    /**发货实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**发货逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
    
    /** 检查库存大于发货数量标志 */
    private String checkStockFlag;
    
    /** （配置项）产品发货使用价格 */
    private String sysConfigUsePrice;
    
    /** （配置项）实际执行价是否按成本价计算 */
    private String useCostPrice;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**库存锁定阶段*/
    private String lockSection;
    
    /**预计到货日期*/
    private String planArriveDate;
    
    /**总金额*/
    private String totalAmountCheck;
    
    public String getTotalAmountCheck() {
		return totalAmountCheck;
	}

	public void setTotalAmountCheck(String totalAmountCheck) {
		this.totalAmountCheck = totalAmountCheck;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getReceiveDepot() {
		return receiveDepot;
	}

	public void setReceiveDepot(String receiveDepot) {
		this.receiveDepot = receiveDepot;
	}

	public String getReceiveLogiInven() {
		return receiveLogiInven;
	}

	public void setReceiveLogiInven(String receiveLogiInven) {
		this.receiveLogiInven = receiveLogiInven;
	}

	public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getProductDeliverId() {
        return productDeliverId;
    }

    public void setProductDeliverId(String productDeliverId) {
        this.productDeliverId = productDeliverId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    @SuppressWarnings("unchecked")
    public Map getProductDeliverMainData() {
        return productDeliverMainData;
    }

    @SuppressWarnings("unchecked")
    public void setProductDeliverMainData(Map productDeliverMainData) {
        this.productDeliverMainData = productDeliverMainData;
    }

    @SuppressWarnings("unchecked")
    public List getProductDeliverDetailData() {
        return productDeliverDetailData;
    }

    @SuppressWarnings("unchecked")
    public void setProductDeliverDetailData(List productDeliverDetailData) {
        this.productDeliverDetailData = productDeliverDetailData;
    }

	/**
	 * @return the workFlowID
	 */
	public String getWorkFlowID() {
		return workFlowID;
	}

	/**
	 * @param workFlowID the workFlowID to set
	 */
	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	public String[] getQuantityArr() {
		return quantityArr;
	}

	public void setQuantityArr(String[] quantityArr) {
		this.quantityArr = quantityArr;
	}

	public String[] getCommentsArr() {
		return commentsArr;
	}

	public void setCommentsArr(String[] commentsArr) {
		this.commentsArr = commentsArr;
	}

	public String[] getPriceUnitArr() {
		return priceUnitArr;
	}

	public void setPriceUnitArr(String[] priceUnitArr) {
		this.priceUnitArr = priceUnitArr;
	}

	public String[] getProductVendorIDArr() {
		return productVendorIDArr;
	}

	public void setProductVendorIDArr(String[] productVendorIDArr) {
		this.productVendorIDArr = productVendorIDArr;
	}

	public String getOutOrganizationID() {
		return outOrganizationID;
	}

	public void setOutOrganizationID(String outOrganizationID) {
		this.outOrganizationID = outOrganizationID;
	}

	public String getOutDepotInfoID() {
		return outDepotInfoID;
	}

	public void setOutDepotInfoID(String outDepotInfoID) {
		this.outDepotInfoID = outDepotInfoID;
	}

	public String getOutLogicInventoryInfoID() {
		return outLogicInventoryInfoID;
	}

	public void setOutLogicInventoryInfoID(String outLogicInventoryInfoID) {
		this.outLogicInventoryInfoID = outLogicInventoryInfoID;
	}

	public String getInOrganizationID() {
		return inOrganizationID;
	}

	public void setInOrganizationID(String inOrganizationID) {
		this.inOrganizationID = inOrganizationID;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

    public List<Map<String, Object>> getDepotsInfoList() {
        return depotsInfoList;
    }

    public void setDepotsInfoList(List<Map<String, Object>> depotsInfoList) {
        this.depotsInfoList = depotsInfoList;
    }

    public List<Map<String, Object>> getLogicDepotsInfoList() {
        return logicDepotsInfoList;
    }

    public void setLogicDepotsInfoList(List<Map<String, Object>> logicDepotsInfoList) {
        this.logicDepotsInfoList = logicDepotsInfoList;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String getCheckStockFlag() {
        return checkStockFlag;
    }

    public void setCheckStockFlag(String checkStockFlag) {
        this.checkStockFlag = checkStockFlag;
    }

    public String[] getReferencePriceArr() {
        return referencePriceArr;
    }

    public void setReferencePriceArr(String[] referencePriceArr) {
        this.referencePriceArr = referencePriceArr;
    }

    public String getSysConfigUsePrice() {
        return sysConfigUsePrice;
    }

    public void setSysConfigUsePrice(String sysConfigUsePrice) {
        this.sysConfigUsePrice = sysConfigUsePrice;
    }

    public String getReceiveEdit() {
        return receiveEdit;
    }

    public void setReceiveEdit(String receiveEdit) {
        this.receiveEdit = receiveEdit;
    }

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

    public String getLockSection() {
        return lockSection;
    }

    public void setLockSection(String lockSection) {
        this.lockSection = lockSection;
    }

    public String getPlanArriveDate() {
        return planArriveDate;
    }

    public void setPlanArriveDate(String planArriveDate) {
        this.planArriveDate = planArriveDate;
    }

	public String getUseCostPrice() {
		return useCostPrice;
	}

	public void setUseCostPrice(String useCostPrice) {
		this.useCostPrice = useCostPrice;
	}
    
    
    
}
