package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH21_Form extends DataTable_BaseForm {

	/** 品牌ID */
	private String brandInfoId;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品名称 */
	private String nameTotal;
	
	private String exportFormat;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
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

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}
	
}
