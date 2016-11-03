package com.cherry.cm.core;

/**
 * 一个用户能管多个品牌，是通过岗位来实现的
 * 即一个用户能拥有多个品牌下的岗位
 * @author dingyc
 *
 */
public class OrgBrandPosition{

	/**
	 * 组织结构ID
	 */
	private int BIN_OrganizationID;
	/**
	 * 组织ID
	 */
	private int BIN_OrganizationInfoID;
	/**
	 * 组织名称
	 */
	private String OrgNameChinese;
	/**
	 * 品牌ID
	 */
	private int BIN_BrandInfoID;
	/**
	 * 品牌名称
	 */
	private String BrandNameChinese;
	
	/**
	 * 岗位ID
	 */
	private int BIN_PositionID;
	/**
	 * 岗位名称
	 */
	private String PositionName;
	
	/**
	 * 岗位类别
	 */
	private int BIN_PositionCategoryID;
	/**
	 * @return the bIN_PositionCategoryID
	 */
	public int getBIN_PositionCategoryID() {
		return BIN_PositionCategoryID;
	}
	/**
	 * @param bIN_PositionCategoryID the bIN_PositionCategoryID to set
	 */
	public void setBIN_PositionCategoryID(int bIN_PositionCategoryID) {
		BIN_PositionCategoryID = bIN_PositionCategoryID;
	}
	/**
	 * @return the bIN_OrganizationID
	 */
	public int getBIN_OrganizationID() {
		return BIN_OrganizationID;
	}
	/**
	 * @param bIN_OrganizationID the bIN_OrganizationID to set
	 */
	public void setBIN_OrganizationID(int bIN_OrganizationID) {
		BIN_OrganizationID = bIN_OrganizationID;
	}
	/**
	 * @return the bIN_OrganizationInfoID
	 */
	public int getBIN_OrganizationInfoID() {
		return BIN_OrganizationInfoID;
	}
	/**
	 * @param bIN_OrganizationInfoID the bIN_OrganizationInfoID to set
	 */
	public void setBIN_OrganizationInfoID(int bIN_OrganizationInfoID) {
		BIN_OrganizationInfoID = bIN_OrganizationInfoID;
	}
	/**
	 * @return the orgNameChinese
	 */
	public String getOrgNameChinese() {
		return OrgNameChinese;
	}
	/**
	 * @param orgNameChinese the orgNameChinese to set
	 */
	public void setOrgNameChinese(String orgNameChinese) {
		OrgNameChinese = orgNameChinese;
	}
	/**
	 * @return the bIN_BrandInfoID
	 */
	public int getBIN_BrandInfoID() {
		return BIN_BrandInfoID;
	}
	/**
	 * @param bIN_BrandInfoID the bIN_BrandInfoID to set
	 */
	public void setBIN_BrandInfoID(int bIN_BrandInfoID) {
		BIN_BrandInfoID = bIN_BrandInfoID;
	}
	/**
	 * @return the brandNameChinese
	 */
	public String getBrandNameChinese() {
		return BrandNameChinese;
	}
	/**
	 * @param brandNameChinese the brandNameChinese to set
	 */
	public void setBrandNameChinese(String brandNameChinese) {
		BrandNameChinese = brandNameChinese;
	}
	/**
	 * @return the bIN_PositionID
	 */
	public int getBIN_PositionID() {
		return BIN_PositionID;
	}
	/**
	 * @param bIN_PositionID the bIN_PositionID to set
	 */
	public void setBIN_PositionID(int bIN_PositionID) {
		BIN_PositionID = bIN_PositionID;
	}
	/**
	 * @return the positionName
	 */
	public String getPositionName() {
		return PositionName;
	}
	/**
	 * @param positionName the positionName to set
	 */
	public void setPositionName(String positionName) {
		PositionName = positionName;
	}
}
