package com.cherry.wp.wr.krp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 库存报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWRKRP01_Form extends DataTable_BaseForm {
	
	/** 逻辑仓库ID **/
	private String logicInventoryInfoId;
	
	/** 有效无效区分（1：有效 0：无效） **/
	private String validFlag;
	
	/** 大分类ID **/
	private String bigClassId;
	
	/** 小分类ID **/
	private String smallClassId;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品ID */
	private String productId;
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 逻辑仓库名称 **/
	private String logicInventoryName;
	
	/** 大分类名称 **/
	private String bigClassName;
	
	/** 小分类名称 **/
	private String smallClassName;
	
	/** 字符编码 **/
	private String charset;
	
	/** 开始日期 **/
	private String startDate;
	
	/** 结束日期 **/
	private String endDate;
	
	/** 权限参数 **/
	private String params;

	public String getLogicInventoryInfoId() {
		return logicInventoryInfoId;
	}

	public void setLogicInventoryInfoId(String logicInventoryInfoId) {
		this.logicInventoryInfoId = logicInventoryInfoId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(String bigClassId) {
		this.bigClassId = bigClassId;
	}

	public String getSmallClassId() {
		return smallClassId;
	}

	public void setSmallClassId(String smallClassId) {
		this.smallClassId = smallClassId;
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

	public String getLogicInventoryName() {
		return logicInventoryName;
	}

	public void setLogicInventoryName(String logicInventoryName) {
		this.logicInventoryName = logicInventoryName;
	}

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}

	public String getSmallClassName() {
		return smallClassName;
	}

	public void setSmallClassName(String smallClassName) {
		this.smallClassName = smallClassName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
