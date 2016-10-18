/*	
 * @(#)PointDTO.java     1.0 2012/02/22	
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.dr.cmbussiness.dto.core;


/**
 * 积分 DTO
 * 
 * @author hub
 * @version 1.0 2011.02.22
 */
public class PointDTO extends BaseDTO{
	
	/** 所属组织ID */
	private int organizationInfoId;
	
	/** 所属品牌ID */
	private int brandInfoId;
	
	/** 会员积分ID */
	private int memberPointId;
	
	/** 会员积分类别ID */
	private String pointTypeId;
	
	/** 会员信息ID */
	private int memberInfoId;
	
	/** 当前累计积分 */
	private double curTotalPoint;
	
	/** 当前累计兑换积分 */
	private double curTotalChanged;
	
	/** 当前可兑换积分 */
	private double curChangablePoint;
	
	/** 当前冻结积分 */
	private double freezePoint;
	
	/** 改变前的累计积分 */
	private double oldTotalPoint;
	
	/** 改变前的累计兑换积分 */
	private double oldTotalChanged;
	
	/** 改变前的可兑换积分 */
	private double oldChangablePoint;
	
	/** 改变前的累计失效积分 */
	private double oldTotalDisPoint;
	
	/** 累计失效积分 */
	private double totalDisablePoint;
	
	/** 本次将失效积分 */
	private double curDisablePoint;
	
	/** 前卡积分 */
	private double preCardPoint;
	
	/** 初始导入总积分 */
	private double initialPoint;
	
	/** 初始导入可兑换积分 */
	private double initChangablePoint;
	
	/** 初始导入总兑换积分 */
	private double initTotalChanged;
	
	/** 会员积分变化主 DTO */
	private PointChangeDTO pointChange;
	
	/** 上回积分失效日期 */
	private String preDisableDate;
	
	/** 上回积分失效单据截止日期 */
	private String prePCBillTime;
	
	/** 本次积分失效日期 */
	private String curDealDate;
	
	/** 更新处理区分 */
	private String upPTKbn;
	
	/** 积分最后变化时间区分 */
	private String lcTimeKbn;
	
	/** 会员俱乐部ID */
	private int memberClubId;
	
	/** 会员俱乐部ID(查询用) */
	private String clubIdStr;
	
	public PointChangeDTO getPointChange() {
		return pointChange;
	}

	public void setPointChange(PointChangeDTO pointChange) {
		this.pointChange = pointChange;
	}

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public int getMemberPointId() {
		return memberPointId;
	}

	public void setMemberPointId(int memberPointId) {
		this.memberPointId = memberPointId;
	}

	public int getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(int memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getPointTypeId() {
		return pointTypeId;
	}

	public void setPointTypeId(String pointTypeId) {
		this.pointTypeId = pointTypeId;
	}

	public double getCurTotalPoint() {
		return curTotalPoint;
	}

	public void setCurTotalPoint(double curTotalPoint) {
		this.curTotalPoint = curTotalPoint;
	}

	public double getCurTotalChanged() {
		return curTotalChanged;
	}

	public void setCurTotalChanged(double curTotalChanged) {
		this.curTotalChanged = curTotalChanged;
	}

	public double getCurChangablePoint() {
		return curChangablePoint;
	}

	public void setCurChangablePoint(double curChangablePoint) {
		this.curChangablePoint = curChangablePoint;
	}

	public double getOldTotalPoint() {
		return oldTotalPoint;
	}

	public void setOldTotalPoint(double oldTotalPoint) {
		this.oldTotalPoint = oldTotalPoint;
	}

	public double getOldTotalChanged() {
		return oldTotalChanged;
	}

	public void setOldTotalChanged(double oldTotalChanged) {
		this.oldTotalChanged = oldTotalChanged;
	}

	public double getOldChangablePoint() {
		return oldChangablePoint;
	}

	public void setOldChangablePoint(double oldChangablePoint) {
		this.oldChangablePoint = oldChangablePoint;
	}

	public double getTotalDisablePoint() {
		return totalDisablePoint;
	}

	public void setTotalDisablePoint(double totalDisablePoint) {
		this.totalDisablePoint = totalDisablePoint;
	}

	public double getCurDisablePoint() {
		return curDisablePoint;
	}

	public void setCurDisablePoint(double curDisablePoint) {
		this.curDisablePoint = curDisablePoint;
	}

	public double getFreezePoint() {
		return freezePoint;
	}

	public void setFreezePoint(double freezePoint) {
		this.freezePoint = freezePoint;
	}

	public String getPreDisableDate() {
		return preDisableDate;
	}

	public void setPreDisableDate(String preDisableDate) {
		this.preDisableDate = preDisableDate;
	}

	public String getCurDealDate() {
		return curDealDate;
	}

	public void setCurDealDate(String curDealDate) {
		this.curDealDate = curDealDate;
	}

	public double getPreCardPoint() {
		return preCardPoint;
	}

	public void setPreCardPoint(double preCardPoint) {
		this.preCardPoint = preCardPoint;
	}

	public double getInitialPoint() {
		return initialPoint;
	}

	public void setInitialPoint(double initialPoint) {
		this.initialPoint = initialPoint;
	}

	public double getInitChangablePoint() {
		return initChangablePoint;
	}

	public void setInitChangablePoint(double initChangablePoint) {
		this.initChangablePoint = initChangablePoint;
	}

	public double getInitTotalChanged() {
		return initTotalChanged;
	}

	public void setInitTotalChanged(double initTotalChanged) {
		this.initTotalChanged = initTotalChanged;
	}

	public String getPrePCBillTime() {
		return prePCBillTime;
	}

	public void setPrePCBillTime(String prePCBillTime) {
		this.prePCBillTime = prePCBillTime;
	}

	public double getOldTotalDisPoint() {
		return oldTotalDisPoint;
	}

	public void setOldTotalDisPoint(double oldTotalDisPoint) {
		this.oldTotalDisPoint = oldTotalDisPoint;
	}

	public String getUpPTKbn() {
		return upPTKbn;
	}

	public void setUpPTKbn(String upPTKbn) {
		this.upPTKbn = upPTKbn;
	}

	public String getLcTimeKbn() {
		return lcTimeKbn;
	}

	public void setLcTimeKbn(String lcTimeKbn) {
		this.lcTimeKbn = lcTimeKbn;
	}

	public int getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(int memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getClubIdStr() {
		return clubIdStr;
	}

	public void setClubIdStr(String clubIdStr) {
		this.clubIdStr = clubIdStr;
	}
}
