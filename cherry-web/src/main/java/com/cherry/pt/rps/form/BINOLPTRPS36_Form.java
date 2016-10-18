
package com.cherry.pt.rps.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * @ClassName: BINOLPTRPS36_Form 
 * @Description: TODO(柜台月度人效明细Form) 
 * @author menghao
 * @version v1.0.0 2015-1-13 
 *
 */
public class BINOLPTRPS36_Form extends DataTable_BaseForm{
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 统计月度 */
	private String month;
	
	/**柜台名称*/
	private String counterName;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
}