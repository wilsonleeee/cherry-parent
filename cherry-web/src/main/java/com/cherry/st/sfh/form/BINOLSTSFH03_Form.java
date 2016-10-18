/*  
 * @(#)BINOLSTSFH03_Form.java     1.0 2011/09/08      
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

public class BINOLSTSFH03_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
	
	private String csrftoken;
	
    /**订货单主表ID*/
    private String productOrderID;
    
    /**已发货的订货单对应的发货单主表ID*/
    private String productDeliverID;
    
    /**单据ID*/
    private String mainOrderID;
    
    /**操作备注*/
    private String opComments;
    
    /**
     * 画面操作区分 
     */
    private String operateType;

    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**审核状态*/
    private String verifiedFlag;
    
    /**订货理由*/
    private String comments;
    
    /**订货单类型*/
    private String orderType;
    
    /**产品厂商ID*/
    private String[] productVendorIDArr;
    
    /**建议数量*/
    private String[] suggestedQuantityArr;
    
    /**申请数量*/
    private String[] applyQuantityArr;
    
    /**核准数量*/
    private String[] quantityArr;
    
    /**价格*/
    private String[] priceUnitArr;
    
    /** 编辑过的价格，写入到订货单明细中的数据 */
    private String[] editPriceArr;
    
    public String[] getEditPriceArr() {
		return editPriceArr;
	}

	public void setEditPriceArr(String[] editPriceArr) {
		this.editPriceArr = editPriceArr;
	}

	/**参考价格*/
    private String[] referencePriceArr;
    
    /**包装类型ID*/
    private String[] productVendorPackageIDArr;
    
    /**订货仓库ID*/
    private String[] inventoryInfoIDArr;
    
    /**逻辑仓库ID*/
    private String[] logicInventoryInfoIDArr;
    
    /**订货仓库库位ID*/
    private String[] storageLocationInfoIDArr;
    
    /**接受订货的仓库ID*/
    private String[] inventoryInfoIDAcceptArr;
    
    /**接受订货的逻辑仓库ID*/
    private String[] logicInventoryInfoIDAcceptArr;
    
    /**备注*/
    private String[] commentsArr;
    
    /**主表发货部门ID*/
    private String outOrganizationID;
    
    /**主表发货实体仓库ID*/
    private String depotInfoIdAccept;
    
    /**主表发货逻辑仓库ID*/
    private String logicDepotsInfoIdAccept;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
    
    /**发货之前是否修改过 */
    private String modifiedFlag;
      
    /** 订货单概要信息 */
	@SuppressWarnings("unchecked")
    private Map productOrderMainData;
	
	/** 订货单详细信息 */
	@SuppressWarnings("unchecked")
    private List productOrderDetailData;
	
	/**发货单号*/
	private String deliverNoIF;
	
	/**建议订单数最大范围（百分比）*/
	private String maxPercent;
	
	/**建议订单数最小范围（百分比）*/
	private String minPercent;
	
	/** 期望发货日期 */
	private String expectDeliverDate;
	
    /**库存锁定阶段*/
    private String lockSection;
    
    /**总金额*/
    private String totalAmountCheck;   
    
    /** （配置项）实际执行价是否按成本价计算 */
    private String useCostPrice;

    public String getTotalAmountCheck() {
		return totalAmountCheck;
	}

	public void setTotalAmountCheck(String totalAmountCheck) {
		this.totalAmountCheck = totalAmountCheck;
	}

	public String getProductOrderID() {
        return productOrderID;
    }

    public void setProductOrderID(String productOrderID) {
        this.productOrderID = productOrderID;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getSuggestedQuantityArr() {
        return suggestedQuantityArr;
    }

    public void setSuggestedQuantityArr(String[] suggestedQuantityArr) {
        this.suggestedQuantityArr = suggestedQuantityArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public String[] getProductVendorPackageIDArr() {
        return productVendorPackageIDArr;
    }

    public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
        this.productVendorPackageIDArr = productVendorPackageIDArr;
    }

    public String[] getInventoryInfoIDArr() {
        return inventoryInfoIDArr;
    }

    public void setInventoryInfoIDArr(String[] inventoryInfoIDArr) {
        this.inventoryInfoIDArr = inventoryInfoIDArr;
    }

    public String[] getLogicInventoryInfoIDArr() {
        return logicInventoryInfoIDArr;
    }

    public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
        this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
    }

    public String[] getStorageLocationInfoIDArr() {
        return storageLocationInfoIDArr;
    }

    public void setStorageLocationInfoIDArr(String[] storageLocationInfoIDArr) {
        this.storageLocationInfoIDArr = storageLocationInfoIDArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public Map getProductOrderMainData() {
        return productOrderMainData;
    }

    public void setProductOrderMainData(Map productOrderMainData) {
        this.productOrderMainData = productOrderMainData;
    }

    public List getProductOrderDetailData() {
        return productOrderDetailData;
    }

    public void setProductOrderDetailData(List productOrderDetailData) {
        this.productOrderDetailData = productOrderDetailData;
    }

	/**
	 * @return the entryID
	 */
	public String getEntryID() {
		return entryID;
	}

	/**
	 * @param entryID the entryID to set
	 */
	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	/**
	 * @return the actionID
	 */
	public String getActionID() {
		return actionID;
	}

	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(String actionID) {
		this.actionID = actionID;
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

    public String[] getInventoryInfoIDAcceptArr() {
        return inventoryInfoIDAcceptArr;
    }

    public void setInventoryInfoIDAcceptArr(String[] inventoryInfoIDAcceptArr) {
        this.inventoryInfoIDAcceptArr = inventoryInfoIDAcceptArr;
    }

    public String[] getLogicInventoryInfoIDAcceptArr() {
        return logicInventoryInfoIDAcceptArr;
    }

    public void setLogicInventoryInfoIDAcceptArr(
            String[] logicInventoryInfoIDAcceptArr) {
        this.logicInventoryInfoIDAcceptArr = logicInventoryInfoIDAcceptArr;
    }

    public String getOutOrganizationID() {
        return outOrganizationID;
    }

    public void setOutOrganizationID(String outOrganizationID) {
        this.outOrganizationID = outOrganizationID;
    }

    public String getDepotInfoIdAccept() {
        return depotInfoIdAccept;
    }

    public void setDepotInfoIdAccept(String depotInfoIdAccept) {
        this.depotInfoIdAccept = depotInfoIdAccept;
    }

    public String getLogicDepotsInfoIdAccept() {
        return logicDepotsInfoIdAccept;
    }

    public void setLogicDepotsInfoIdAccept(String logicDepotsInfoIdAccept) {
        this.logicDepotsInfoIdAccept = logicDepotsInfoIdAccept;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCsrftoken() {
        return csrftoken;
    }

    public void setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
    }

    public String getMainOrderID() {
        return mainOrderID;
    }

    public void setMainOrderID(String mainOrderID) {
        this.mainOrderID = mainOrderID;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String getDeliverNoIF() {
        return deliverNoIF;
    }

    public void setDeliverNoIF(String deliverNoIF) {
        this.deliverNoIF = deliverNoIF;
    }

    public String getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(String maxPercent) {
        this.maxPercent = maxPercent;
    }

    public String getMinPercent() {
        return minPercent;
    }

    public void setMinPercent(String minPercent) {
        this.minPercent = minPercent;
    }

    public String getExpectDeliverDate() {
        return expectDeliverDate;
    }

    public void setExpectDeliverDate(String expectDeliverDate) {
        this.expectDeliverDate = expectDeliverDate;
    }

    public String[] getApplyQuantityArr() {
        return applyQuantityArr;
    }

    public void setApplyQuantityArr(String[] applyQuantityArr) {
        this.applyQuantityArr = applyQuantityArr;
    }

    public String[] getReferencePriceArr() {
        return referencePriceArr;
    }

    public void setReferencePriceArr(String[] referencePriceArr) {
        this.referencePriceArr = referencePriceArr;
    }

    public String getLockSection() {
        return lockSection;
    }

    public void setLockSection(String lockSection) {
        this.lockSection = lockSection;
    }

	public String getProductDeliverID() {
		return productDeliverID;
	}

	public void setProductDeliverID(String productDeliverID) {
		this.productDeliverID = productDeliverID;
	}

	public String getUseCostPrice() {
		return useCostPrice;
	}

	public void setUseCostPrice(String useCostPrice) {
		this.useCostPrice = useCostPrice;
	}

	public String getModifiedFlag() {
		return modifiedFlag;
	}

	public void setModifiedFlag(String modifiedFlag) {
		this.modifiedFlag = modifiedFlag;
	}

	
	
}
