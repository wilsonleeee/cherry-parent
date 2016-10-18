package com.cherry.wp.common.entity;

public class SaleActivityDetailEntity {

	/**规则code*/
	private String maincode;
	/**产品条码*/
	private String barcode;
	/**产商编码*/
	private String unitcode;
	
	public SaleActivityDetailEntity(){
		super();
	}
	
	public SaleActivityDetailEntity(String maincode, String barcode,
			String unitcode) {
		super();
		this.maincode = maincode;
		this.barcode = barcode;
		this.unitcode = unitcode;
	}
	public String getMaincode() {
		return maincode;
	}
	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getUnitcode() {
		return unitcode;
	}
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
	
	
	
}
