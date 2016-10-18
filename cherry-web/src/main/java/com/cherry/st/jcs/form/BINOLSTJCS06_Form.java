package com.cherry.st.jcs.form;

import java.util.List;
import java.util.Map;

public class BINOLSTJCS06_Form {

	/** 品牌 */
	private String brandInfoId;

	/** 组织ID */
	private String organizationId;

	/** 逻辑仓库ID */
	private String logInvId;
	
	/** 逻辑仓库代码 */
	private String logInvCode;
	
	/** 逻辑仓库名称 */
	private String logInvNameCN;
	
	/** 逻辑仓库名称英文 */
	private String logInvNameEN;
	
	/** 备注 */
	private String comments;
	
	/** 类型 */
	private String type;
	
	/** 排序 */
	private String orderNo;

	/** 默认仓库 */
	private String defaultFlag;
	
	/** 更新时间*/
	private String updateTime;
	
	/** 更新次数*/
	private String modifyCount ;
	
	/** 有效区分 */
	private String validFlag;

	/** 所属品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 逻辑仓库 */
	private Map<String,Object> logInv;
	
	/** 逻辑仓库list*/
	private List<Map<String,Object>> logInvList;
	
	public Map<String, Object> getLogInv() {
		return logInv;
	}

	public void setLogInv(Map<String, Object> logInv) {
		this.logInv = logInv;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getLogInvId() {
		return logInvId;
	}

	public void setLogInvId(String logInvId) {
		this.logInvId = logInvId;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public List<Map<String, Object>> getLogInvList() {
		return logInvList;
	}
	public void setLogInvList(List<Map<String, Object>> logInvList) {
		this.logInvList = logInvList;
	}
	

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getLogInvCode() {
		return logInvCode;
	}

	public void setLogInvCode(String logInvCode) {
		this.logInvCode = logInvCode;
	}


	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLogInvNameCN() {
		return logInvNameCN;
	}

	public void setLogInvNameCN(String logInvNameCN) {
		this.logInvNameCN = logInvNameCN;
	}

	public String getLogInvNameEN() {
		return logInvNameEN;
	}

	public void setLogInvNameEN(String logInvNameEN) {
		this.logInvNameEN = logInvNameEN;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
