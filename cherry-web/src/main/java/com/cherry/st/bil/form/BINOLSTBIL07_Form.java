package com.cherry.st.bil.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLSTBIL07_Form extends BINOLCM13_Form{
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	/** 单号  */
	private String billNo;
	
	/** 审核区分  */
	private String verifiedFlag;

	private List<Map<String, Object>> shiftList;
	
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsMVList;
	
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

	public void setShiftList(List<Map<String, Object>> shiftList) {
		this.shiftList = shiftList;
	}

	public List<Map<String, Object>> getShiftList() {
		return shiftList;
	}
	
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}

    public List<Map<String, Object>> getVerifiedFlagsMVList() {
        return verifiedFlagsMVList;
    }

    public void setVerifiedFlagsMVList(List<Map<String, Object>> verifiedFlagsMVList) {
        this.verifiedFlagsMVList = verifiedFlagsMVList;
    }
}
