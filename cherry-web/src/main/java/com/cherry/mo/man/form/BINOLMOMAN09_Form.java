package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOMAN09_Form extends DataTable_BaseForm{
	
	 /**菜单ID**/
	 private Integer posMenuID;
	 
	 /**菜单ID集合**/
	 private String[] posMenuIDs;
	 
	 /**组织ID**/
	 private Integer organizationInfoId;
	 
	 /**品牌ID**/
	 private String brandInfoId;
	 
	 /**菜单编号**/
	 private String menuCode;
	 
	 /**菜单类型**/
	 private String menuType;
	 
	 /**菜单连接地址**/
	 private String menuLink;
	 
	 /**说明**/
	 private String comment;
	 
	 /**是否有效区分**/
	 private String validFlag;
	 
	 /**是否终结点  1：是  0：否**/
	 private String isLeaf;

	public String[] getPosMenuIDs() {
		return posMenuIDs;
	}

	public void setPosMenuIDs(String[] posMenuIDs) {
		this.posMenuIDs = posMenuIDs;
	}

	public Integer getPosMenuID() {
		return posMenuID;
	}

	public void setPosMenuID(Integer posMenuID) {
		this.posMenuID = posMenuID;
	}

	public Integer getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(Integer organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuLink() {
		return menuLink;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	 
}