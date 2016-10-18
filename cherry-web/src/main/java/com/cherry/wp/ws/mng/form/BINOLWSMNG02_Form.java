package com.cherry.wp.ws.mng.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLWSMNG02_Form extends BINOLCM13_Form {
    
    /**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /** 产品名称 */
    private String nameTotal;
    
    /** 单号  */
    private String billNoIF;
    
    /** 审核区分  */
    private String verifiedFlag;
    
    /** 关联单号  */
    private String relevanceNo;
    
    /** 导入批次*/
    private String importBatch;
    
    /**业务类型*/
    private String tradeType;
    
    /** 入库状态*/
    private String tradeStatus;
    
    /**员工ID*/
    private String employeeId;
    
    /**员工姓名*/
    private String employeeName;
    
    /** 总数量，总金额 */
    private Map<String,Object> sumInfo;
    
    /**入库单list*/
    private List<Map<String,Object>> prtInDepotList;
    
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsGRList;
    
    /**业务日期*/
    private String bussinessDate;
    
    /**申请日期*/
    private String applyDate;
    
    /** 入库部门 */
    private String inOrganizationID;
    
    /** 入库实体仓库 */
    private String inInventoryInfoID;
    
    /** 入库逻辑仓库*/
    private String inLogicInventoryInfoID;
    
    /** 入库理由*/
    private String reason;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    
    /**产品厂商ID Arr*/
    private String[] prtVendorId;
    
//    /** 厂商编码*/
//    private String[] unitCodeArr;
    
//    /** 条码*/
//    private String[] barCodeArr;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 数量*/
    private String[] quantityArr;
    
    /** 备注*/
    private String[] commentsArr;
    
    /**参考价*/
    private String[] referencePriceArr;
    
    /** 价格*/
    private String[] priceUnitArr;
    
    /**入库单ID*/
    private String productInDepotId;

    /**入库单主表信息*/
    private Map inDepotMainMap;
    
    /**入库单明细信息*/
    private List inDepotDetailList;
    
    /***/
    private String entryID;
    
    /***/
    private String actionID;
    
    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**操作备注*/
    private String opComments;
    
    /** 发货单号 */
    private String deliverNo;
    
    /** 发货单List */
    private List<Map<String,Object>> deliverList;
   
    /**发货单主表ID*/
    private String productDeliverId;
    
    /** 发货单概要信息 */
    private Map productDeliverMainData;
    
    /** 发货单详细信息 */
    private List<Map<String, Object>> productDeliverDetailData;
    
    /** （配置项）产品入库/发货使用价格 */
    private String sysConfigUsePrice;
    
    private String workFlowID;
    
    /** 画面操作区分 */
    private String operateType;
    
    /**收货部门实体仓库List*/
  	private List<Map<String,Object>> receiveDepotList;
  	
  	/**收货部门逻辑仓库List*/
  	private List<Map<String,Object>> receiveLogiInvenList;
  	
    /**实际做业务的员工*/
    private String tradeEmployeeID;
  	
  	 /**BAList*/
    private List<Map<String,Object>> counterBAList;
    
    /**柜台号*/
    private String counterCode;
    
    /**选中入库单据ID*/
    private String[] checkedBillIdArrGR;

    /**选中收货单据ID*/
    private String[] checkedBillIdArrRD;
    
	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Map<String, Object>> getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List<Map<String, Object>> deliverList) {
		this.deliverList = deliverList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getProductDeliverId() {
		return productDeliverId;
	}

	public void setProductDeliverId(String productDeliverId) {
		this.productDeliverId = productDeliverId;
	}

	public Map getProductDeliverMainData() {
		return productDeliverMainData;
	}

	public void setProductDeliverMainData(Map productDeliverMainData) {
		this.productDeliverMainData = productDeliverMainData;
	}

	public List<Map<String, Object>> getProductDeliverDetailData() {
		return productDeliverDetailData;
	}

	public void setProductDeliverDetailData(
			List<Map<String, Object>> productDeliverDetailData) {
		this.productDeliverDetailData = productDeliverDetailData;
	}

	public String getSysConfigUsePrice() {
		return sysConfigUsePrice;
	}

	public void setSysConfigUsePrice(String sysConfigUsePrice) {
		this.sysConfigUsePrice = sysConfigUsePrice;
	}

	public String getWorkFlowID() {
		return workFlowID;
	}

	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
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

    public String getNameTotal() {
        return nameTotal;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getBillNoIF() {
        return billNoIF;
    }

    public void setBillNoIF(String billNoIF) {
        this.billNoIF = billNoIF;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getRelevanceNo() {
        return relevanceNo;
    }

    public void setRelevanceNo(String relevanceNo) {
        this.relevanceNo = relevanceNo;
    }

    public String getImportBatch() {
        return importBatch;
    }

    public void setImportBatch(String importBatch) {
        this.importBatch = importBatch;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public List<Map<String, Object>> getPrtInDepotList() {
        return prtInDepotList;
    }

    public void setPrtInDepotList(List<Map<String, Object>> prtInDepotList) {
        this.prtInDepotList = prtInDepotList;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBussinessDate() {
        return bussinessDate;
    }

    public void setBussinessDate(String bussinessDate) {
        this.bussinessDate = bussinessDate;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
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

    public String[] getReferencePriceArr() {
        return referencePriceArr;
    }

    public void setReferencePriceArr(String[] referencePriceArr) {
        this.referencePriceArr = referencePriceArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public String getProductInDepotId() {
        return productInDepotId;
    }

    public void setProductInDepotId(String productInDepotId) {
        this.productInDepotId = productInDepotId;
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

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String getInOrganizationID() {
        return inOrganizationID;
    }

    public void setInOrganizationID(String inOrganizationID) {
        this.inOrganizationID = inOrganizationID;
    }

    public String getInInventoryInfoID() {
        return inInventoryInfoID;
    }

    public void setInInventoryInfoID(String inInventoryInfoID) {
        this.inInventoryInfoID = inInventoryInfoID;
    }

    public String getInLogicInventoryInfoID() {
        return inLogicInventoryInfoID;
    }

    public void setInLogicInventoryInfoID(String inLogicInventoryInfoID) {
        this.inLogicInventoryInfoID = inLogicInventoryInfoID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Map getInDepotMainMap() {
        return inDepotMainMap;
    }

    public void setInDepotMainMap(Map inDepotMainMap) {
        this.inDepotMainMap = inDepotMainMap;
    }

    public List getInDepotDetailList() {
        return inDepotDetailList;
    }

    public void setInDepotDetailList(List inDepotDetailList) {
        this.inDepotDetailList = inDepotDetailList;
    }

    public List<Map<String, Object>> getVerifiedFlagsGRList() {
        return verifiedFlagsGRList;
    }

    public void setVerifiedFlagsGRList(List<Map<String, Object>> verifiedFlagsGRList) {
        this.verifiedFlagsGRList = verifiedFlagsGRList;
    }

    public void setPrtVendorId(String[] prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String[] getPrtVendorId() {
        return prtVendorId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public List<Map<String, Object>> getCounterBAList() {
        return counterBAList;
    }

    public void setCounterBAList(List<Map<String, Object>> counterBAList) {
        this.counterBAList = counterBAList;
    }

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String[] getCheckedBillIdArrGR() {
        return checkedBillIdArrGR;
    }

    public void setCheckedBillIdArrGR(String[] checkedBillIdArrGR) {
        this.checkedBillIdArrGR = checkedBillIdArrGR;
    }

    public String[] getCheckedBillIdArrRD() {
        return checkedBillIdArrRD;
    }

    public void setCheckedBillIdArrRD(String[] checkedBillIdArrRD) {
        this.checkedBillIdArrRD = checkedBillIdArrRD;
    }

}
