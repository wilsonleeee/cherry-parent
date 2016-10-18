/*  
 * @(#)BINOLSTSFH22_Form.java     1.0 2016/09/07     
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
 * 订货（浓妆淡抹）Form
 * 
 * @author zw
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH23_Form extends DataTable_BaseForm {
   
    /**订货业务日期 */
    private String date;
    
    /** 期望发货日期 */
    private String expectDeliverDate;
    
    /** 期望发货日期 */
    private String deliverAddress;

	/**订货类型*/
    private String orderType;

    /**订货方*/
    private String inOrganizationId;
    
    /**订货仓库ID*/
    private String inDepotId ;
    
    /**订货逻辑仓库ID*/
    private String inLogicDepotId ;
    
    /**发货方*/
    private String outOrganizationId;
    
    /**订货仓库ID List */
    private List<Map<String, Object>> inDepotList;
    
    /**订货逻辑仓库ID List */
    private List<Map<String, Object>> inLogicDepotList;
    

	/**订货理由  主表*/
    private String reasonAll;
    
    /**产品厂商ID */
    private String[] productVendorIDArr;
    
    /**产品单价 */
    private String[] priceUnitArr;
    
    /**订货数量  基本单位*/
    private String[] quantityArr;
    
    /**价格*/
    private String[] priceArr;
        
    /**订货理由  明细*/
    private String[] reasonArr;
    
    /**逻辑仓库ID */
    private String[] logicInventoryInfoIDArr;
    
    /**包装类型ID */
    private String[] productVendorPackageIDArr;
    
    /**操作订单号 */
    private String  checkOrderNoId;
    	
	private String checkOrderNoIdCopy;
    
	/**画面初始化信息*/
    private Map<String,Object> initInfoMap;
    
    /**来源（1：WEBPOS null：其他）*/
    private String fromPage;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**订单单号*/
    private String orderNum;
    
    /**订货天数*/
    private String orderDayNum;
    
    /**订货总数量*/
    private String totalQuantity;
    
    /**订货总金额*/
    private String totalAmount;
    
	/** 查询开始日期 */
	private String fromDate ;
	
	/** 查询结束日期 */
	private String  toDate ;
	
	/** 订单状态 */
	private String orderStatus;
	
	/** 订货客户*/
	private String employeeName;

	/**订单主表信息 List */
    private List<Map<String, Object>> orderMainInfoList;

    /**是否允许负库存*/
    private String IsAllowNegativeInventory;
    
	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderDayNum() {
		return orderDayNum;
	}

	public void setOrderDayNum(String orderDayNum) {
		this.orderDayNum = orderDayNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInOrganizationId() {
        return inOrganizationId;
    }

    public void setInOrganizationId(String inOrganizationId) {
        this.inOrganizationId = inOrganizationId;
    }

    public String getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(String inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getInLogicDepotId() {
        return inLogicDepotId;
    }

    public void setInLogicDepotId(String inLogicDepotId) {
        this.inLogicDepotId = inLogicDepotId;
    }

    public String getOutOrganizationId() {
        return outOrganizationId;
    }

    public void setOutOrganizationId(String outOrganizationId) {
        this.outOrganizationId = outOrganizationId;
    }

    public List<Map<String, Object>> getInDepotList() {
        return inDepotList;
    }

    public void setInDepotList(List<Map<String, Object>> inDepotList) {
        this.inDepotList = inDepotList;
    }

    public List<Map<String, Object>> getInLogicDepotList() {
        return inLogicDepotList;
    }

    public void setInLogicDepotList(List<Map<String, Object>> inLogicDepotList) {
        this.inLogicDepotList = inLogicDepotList;
    }

    public String getReasonAll() {
        return reasonAll;
    }

    public void setReasonAll(String reasonAll) {
        this.reasonAll = reasonAll;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
    }

    public String[] getLogicInventoryInfoIDArr() {
        return logicInventoryInfoIDArr;
    }

    public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
        this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
    }

    public String[] getProductVendorPackageIDArr() {
        return productVendorPackageIDArr;
    }

    public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
        this.productVendorPackageIDArr = productVendorPackageIDArr;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
    }

    public String getExpectDeliverDate() {
        return expectDeliverDate;
    }

    public void setExpectDeliverDate(String expectDeliverDate) {
        this.expectDeliverDate = expectDeliverDate;
    }

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}



    public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

    
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<Map<String, Object>> getOrderMainInfoList() {
		return orderMainInfoList;
	}

	public void setOrderMainInfoList(List<Map<String, Object>> orderMainInfoList) {
		this.orderMainInfoList = orderMainInfoList;
	}

	public String getCheckOrderNoId() {
		return checkOrderNoId;
	}

	public void setCheckOrderNoId(String checkOrderNoId) {
		this.checkOrderNoId = checkOrderNoId;
	}

	public String getCheckOrderNoIdCopy() {
		return checkOrderNoIdCopy;
	}

	public void setCheckOrderNoIdCopy(String checkOrderNoIdCopy) {
		this.checkOrderNoIdCopy = checkOrderNoIdCopy;
	}
	
	
    public String getIsAllowNegativeInventory() {
		return IsAllowNegativeInventory;
	}

	public void setIsAllowNegativeInventory(String isAllowNegativeInventory) {
		IsAllowNegativeInventory = isAllowNegativeInventory;
	}

}
