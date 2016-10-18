package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOMAN10_Form extends DataTable_BaseForm{
	
	/**菜单配置ID**/
	 private Integer posMenuBrandID;
	 
	 /**组织Code**/
	 private Integer orgCode;
	 
	 /**品牌代码**/
	 private String brandCode;
	 
	 /**菜单ID**/
	 private Integer posMenuID;
	 
	 /**品牌菜单名**/
	 private String brandMenuName;
	 
	 /**父级菜单ID**/
	 private Integer parentMenuID;
	 
	 /**菜单对应的容器**/
	 private String container;
	 
	 /**同一父级节点下的排序**/
	 private Integer menuOrder;
	 
	 /**菜单状态**/
	 private String menuStatus;
	 
	 /**菜单的属性**/
	 private String menuValue;
	 
	 /**有效区分**/
	 private String validFlag;

	public Integer getPosMenuBrandID() {
		return posMenuBrandID;
	}

	public void setPosMenuBrandID(Integer posMenuBrandID) {
		this.posMenuBrandID = posMenuBrandID;
	}

	public Integer getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Integer orgCode) {
		this.orgCode = orgCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public Integer getPosMenuID() {
		return posMenuID;
	}

	public void setPosMenuID(Integer posMenuID) {
		this.posMenuID = posMenuID;
	}

	public String getBrandMenuName() {
		return brandMenuName;
	}

	public void setBrandMenuName(String brandMenuName) {
		this.brandMenuName = brandMenuName;
	}

	public Integer getParentMenuID() {
		return parentMenuID;
	}

	public void setParentMenuID(Integer parentMenuID) {
		this.parentMenuID = parentMenuID;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getMenuValue() {
		return menuValue;
	}

	public void setMenuValue(String menuValue) {
		this.menuValue = menuValue;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
