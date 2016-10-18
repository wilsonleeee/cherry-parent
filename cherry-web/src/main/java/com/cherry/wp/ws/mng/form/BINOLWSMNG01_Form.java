package com.cherry.wp.ws.mng.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWSMNG01_Form extends DataTable_BaseForm {
	
	/** 订单单号 */
	private String orderNo;
	
	/** 处理状态 */
	private String tradeStatus;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	/** 共通条参数 */
	private String params;
	
	/**订货单主表ID*/
    private String productOrderID;
    
    /** 画面操作区分 */
    private String operateType;
    
    private String workFlowID;
    
    /** 订货单概要信息 */
    private Map productOrderMainData;
   	
   	/** 订货单详细信息 */
    private List<Map<String,Object>> productOrderDetailData;
	
	private Map<String, Object> initInfoMap = new HashMap<String, Object>();
	
	/**BAList*/
	private List<Map<String,Object>> counterBAList;
	
	/**柜台号*/
	private String counterCode;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
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

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Map<String, Object> getInitInfoMap() {
		return initInfoMap;
	}

	public void setInitInfoMap(Map<String, Object> initInfoMap) {
		this.initInfoMap = initInfoMap;
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

	public String getWorkFlowID() {
		return workFlowID;
	}

	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	public Map getProductOrderMainData() {
		return productOrderMainData;
	}

	public void setProductOrderMainData(Map productOrderMainData) {
		this.productOrderMainData = productOrderMainData;
	}

	public List<Map<String, Object>> getProductOrderDetailData() {
		return productOrderDetailData;
	}

	public void setProductOrderDetailData(
			List<Map<String, Object>> productOrderDetailData) {
		this.productOrderDetailData = productOrderDetailData;
	}

    public List<Map<String, Object>> getCounterBAList() {
        return counterBAList;
    }

    public void setCounterBAList(List<Map<String, Object>> counterBAList) {
        this.counterBAList = counterBAList;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

}
