package com.cherry.st.bil.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLSTBIL01_Form extends BINOLCM13_Form{
	
	/**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /**入库单list*/
    private List<Map<String,Object>> prtInDepotList;
    
    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	/** 单号  */
	private String billNoIF;
	
	/** 审核区分  */
	private String verifiedFlag;
	
	/** 总数量，总金额 */
	private Map<String,Object> sumInfo;
	
	/** 关联单号  */
	private String relevanceNo;
	
	/** 导入批次*/
	private String importBatch;
	
    /** 入库状态*/
    private String tradeStatus;
    
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsGRList;
	
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

	public List<Map<String, Object>> getPrtInDepotList() {
		return prtInDepotList;
	}

	public void setPrtInDepotList(List<Map<String, Object>> prtInDepotList) {
		this.prtInDepotList = prtInDepotList;
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

	public String getBillNoIF() {
		return billNoIF;
	}

	public void setBillNoIF(String billNoIF) {
		this.billNoIF = billNoIF;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getRelevanceNo() {
		return relevanceNo;
	}

	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
	}

	public String getImportBatch() {
		return importBatch;
	}

	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public List<Map<String, Object>> getVerifiedFlagsGRList() {
        return verifiedFlagsGRList;
    }

    public void setVerifiedFlagsGRList(List<Map<String, Object>> verifiedFlagsGRList) {
        this.verifiedFlagsGRList = verifiedFlagsGRList;
    }
}
