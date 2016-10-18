package com.cherry.wp.wr.krp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 进销存查询Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWRKRP02_Form extends DataTable_BaseForm {
	
	/** 开始日期 **/
	private String startDate;
	
	/** 结束日期 **/
	private String endDate;
	
	/** 逻辑仓库ID **/
	private String lgcInventoryId;
	
	/** 大分类ID **/
	private String catePropValId;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品ID */
	private String productId;
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 有效无效区分（1：有效 0：无效） **/
	private String validFlag;
	
	/** 库存统计方式 */
	private String type;
	
	/** 权限参数 **/
	private String params;

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

	public String getLgcInventoryId() {
		return lgcInventoryId;
	}

	public void setLgcInventoryId(String lgcInventoryId) {
		this.lgcInventoryId = lgcInventoryId;
	}

	public String getCatePropValId() {
		return catePropValId;
	}

	public void setCatePropValId(String catePropValId) {
		this.catePropValId = catePropValId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
