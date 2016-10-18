package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBMBM25_Form extends DataTable_BaseForm{
	/**导入名称*/
	private String importName;
	
	/**导入原因*/
	private String reason;
	
	/**会员俱乐部ID*/
	private String memberClubId;
	
	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
