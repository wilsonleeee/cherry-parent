
/*  
 * @(#)BINOLSTIOS02_Form.java    1.0 2011-8-31     
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
package com.cherry.st.ios.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTIOS02_Form extends DataTable_BaseForm {

	//厂商编码
	private String unitCode;
	//产品条码
	private String barCode;
	//部门Code
	private String departCode;
	//实体仓库ID
	private String depotInfoId;
	//部门ID
	private String departId;
	//出库逻辑仓库ID
	private String fromLogicInventoryInfoId;
	//入库逻辑仓库ID
	private String toLogicInventoryInfoId;
	//品牌
	private String brandInfoId;
	//备注
	private String comments;
	//产品厂商ID组
	private String[] productVendorIdArr;
	//产品数量组
	private String[] quantityArr;
	//备注组
	private String[] commentsArr;
	//产生编码组
	private String[] unitCodeArr;
	//产品条码组
	private String[] barCodeArr;
	//产品厂商ID
	private String productVendorId;
	//价格
	private String[] priceArr;
	
    /**支持终端移库标志*/
    private String counterShiftFlag;
	
	public String[] getPriceArr() {
		return priceArr;
	}
	public void setPriceArr(String[] priceArr) {
		this.priceArr = priceArr;
	}
	public String getDepotInfoId() {
		return depotInfoId;
	}
	public void setDepotInfoId(String depotInfoId) {
		this.depotInfoId = depotInfoId;
	}
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getFromLogicInventoryInfoId() {
		return fromLogicInventoryInfoId;
	}
	public void setFromLogicInventoryInfoId(String fromLogicInventoryInfoId) {
		this.fromLogicInventoryInfoId = fromLogicInventoryInfoId;
	}
	public String getToLogicInventoryInfoId() {
		return toLogicInventoryInfoId;
	}
	public void setToLogicInventoryInfoId(String toLogicInventoryInfoId) {
		this.toLogicInventoryInfoId = toLogicInventoryInfoId;
	}
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String[] getProductVendorIdArr() {
		return productVendorIdArr;
	}
	public void setProductVendorIdArr(String[] productVendorIdArr) {
		this.productVendorIdArr = productVendorIdArr;
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
	public String[] getUnitCodeArr() {
		return unitCodeArr;
	}
	public void setUnitCodeArr(String[] unitCodeArr) {
		this.unitCodeArr = unitCodeArr;
	}
	public String[] getBarCodeArr() {
		return barCodeArr;
	}
	public void setBarCodeArr(String[] barCodeArr) {
		this.barCodeArr = barCodeArr;
	}
	public String getProductVendorId() {
		return productVendorId;
	}
	public void setProductVendorId(String productVendorId) {
		this.productVendorId = productVendorId;
	}
	public String getDepartCode() {
		return departCode;
	}
	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
    public String getCounterShiftFlag() {
        return counterShiftFlag;
    }
    public void setCounterShiftFlag(String counterShiftFlag) {
        this.counterShiftFlag = counterShiftFlag;
    }
	
}
