package com.cherry.mb.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员推荐会员Form
 * 
 * @author menghao
 * 
 */
public class BINOLMBRPT06_Form extends DataTable_BaseForm {

	/** 品牌ID */
	private String brandInfoId;
	
	/** 开始日期 */
	private String startDate;

	/** 结束日期 */
	private String endDate;

	/** 主推荐会员 */
	private String memberCode;

	/** 被推荐会员 */
	private String recommendedMemCode;
	
	/**导出格式区分*/
	private String exportFormat;

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

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getRecommendedMemCode() {
		return recommendedMemCode;
	}

	public void setRecommendedMemCode(String recommendedMemCode) {
		this.recommendedMemCode = recommendedMemCode;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

}
