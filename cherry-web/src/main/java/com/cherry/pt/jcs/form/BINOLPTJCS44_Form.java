package com.cherry.pt.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLPTJCS44_Form extends DataTable_BaseForm{
	
	/**产品全称*/
	private String nameTotal;
	/**产品条码*/
	private String barCode;
	/**产品规格*/
	private String spec;
	/**零售价格*/
	private String salePrice;
	/**会员价格*/
	private String memprice;
	/**有效性*/
	private String validFlag;
	/**柜台号**/
	private String counterCode;
	/**商品品牌*/
	private String originalBrand;
	public String getNameTotal() {
		return nameTotal;
	}
	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getMemprice() {
		return memprice;
	}
	public void setMemprice(String memprice) {
		this.memprice = memprice;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getOriginalBrand() {
		return originalBrand;
	}
	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}
	
	
}
