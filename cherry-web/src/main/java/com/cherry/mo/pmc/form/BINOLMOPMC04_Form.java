package com.cherry.mo.pmc.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * POS品牌菜单管理Form
 * @author menghao
 *
 */
public class BINOLMOPMC04_Form extends DataTable_BaseForm {
	 /**菜单配置ID**/
	 private Integer posMenuBrandID;
	 
	 /**菜单ID**/
	 private Integer posMenuID;
	 
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
	 
	 /** 菜单编码*/
	 private String menuCode;
	 
	 /** 菜单类型*/
	 private String menuType;
	 
	 /** 菜单链接地址*/
	 private String menuLink;
	 
	 /** 菜单说明*/
	 private String comment;
	 
	 /** 是否终结点*/
	 private String isLeaf;
	 
	 /** 菜单中文名*/
	 private String brandMenuNameCN;
	 
	 /** 菜单英文名*/
	 private String brandMenuNameEN;
	 
	 /**上级菜单List*/
	 private List<Map<String, Object>> posMenuList;
	 
	 /** 菜单提示STR*/
	 private String menuInfoStr;
	 
	 /** 菜单提示下拉框显示条数 */
	 private String number;
	 
	 /** 机器类型 */
	 private String machineType;
	 
	 /**补登销售记录的终端系统配置项值（只是临时性的解决方案）*/
	 private String configValue;

	public Integer getPosMenuBrandID() {
		return posMenuBrandID;
	}

	public void setPosMenuBrandID(Integer posMenuBrandID) {
		this.posMenuBrandID = posMenuBrandID;
	}

	public Integer getPosMenuID() {
		return posMenuID;
	}

	public void setPosMenuID(Integer posMenuID) {
		this.posMenuID = posMenuID;
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

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getBrandMenuNameCN() {
		return brandMenuNameCN;
	}

	public void setBrandMenuNameCN(String brandMenuNameCN) {
		this.brandMenuNameCN = brandMenuNameCN;
	}

	public String getBrandMenuNameEN() {
		return brandMenuNameEN;
	}

	public void setBrandMenuNameEN(String brandMenuNameEN) {
		this.brandMenuNameEN = brandMenuNameEN;
	}

	public List<Map<String, Object>> getPosMenuList() {
		return posMenuList;
	}

	public void setPosMenuList(List<Map<String, Object>> posMenuList) {
		this.posMenuList = posMenuList;
	}

	public String getMenuInfoStr() {
		return menuInfoStr;
	}

	public void setMenuInfoStr(String menuInfoStr) {
		this.menuInfoStr = menuInfoStr;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
}
