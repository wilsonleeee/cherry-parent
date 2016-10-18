package com.cherry.mb.mbm.form;


public class BINOLMBMBM03_Form {
	/** 会员ID */
	private int memberInfoId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 变更前会员等级Id */
	private String oldmemberLevelId;
	
	/** 变更前入会时间*/
	private String oldjoinDate;

	/** 变更前化妆次数*/
	private String oldbtimes;
	
	/** 变更前累计金额*/
	private String oldtotalAmount;
	
	/** 变更后会员等级Id */
	private String memberLevelId;
	
	/** 变更后入会时间*/
	private String joinDate;

	/** 变更后化妆次数*/
	private String btimes;
	
	/** 变更后累计金额*/
	private String totalAmount;
	
	/** 累计积分*/
	private String totalPoint;
	
	/** 备注*/
	private String comments;
	
	/** 会员信息表更新时间*/
	private String memInfoUdTime;
	
	/** 会员信息表更新次数*/
	private String memInfoMdCount;
	
	/** 会员扩展表更新时间*/
	private String extInfoUdTime;
	
	/** 会员扩展表更新次数*/
	private String extInfoMdCount;
	
	/** 会员履历表明细Id*/
	private String memUsedDetailId;
	
	/** 会员积分表更新时间*/
	private String pointUdTime;
	
	/** 会员积分表更新次数*/
	private String pointMdCount;
	
	/** 总积分指定日期*/
	private String dateTime;
	
	/** 总积分指定时间（时）*/
	private String startHH;
	
	/** 总积分指定时间（分）*/
	private String startMM;
	
	/** 总积分指定时间（秒）*/
	private String startSS;
	
	/** 积分差值*/
	private String difPoint;
	
	/** 差值积分指定日期*/
	private String difdateTime;
	
	/** 差值积分指定时间（时）*/
	private String startHour;
	
	/** 差值积分指定时间（分）*/
	private String startMinute;
	
	/** 差值积分指定时间（秒）*/
	private String startSecond;

	/** 积分类型*/
	private String pointType;
	
	/** 会员业务类型*/
	private String tradeType;
	
	/** 积分修改明细Id*/
	private String MemPointId;
	
	/** 会员俱乐部ID */
	private String memberClubId;
	
	public String getMemPointId() {
		return MemPointId;
	}
	public void setMemPointId(String memPointId) {
		MemPointId = memPointId;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getMemUsedDetailId() {
		return memUsedDetailId;
	}
	public void setMemUsedDetailId(String memUsedDetailId) {
		this.memUsedDetailId = memUsedDetailId;
	}
	public int getMemberInfoId() {
		return memberInfoId;
	}
	public String getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(String memberLevelId) {
		this.memberLevelId = memberLevelId;
	}

	public void setMemberInfoId(int memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getBtimes() {
		return btimes;
	}
	public void setBtimes(String btimes) {
		this.btimes = btimes;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getOldmemberLevelId() {
		return oldmemberLevelId;
	}
	public void setOldmemberLevelId(String oldmemberLevelId) {
		this.oldmemberLevelId = oldmemberLevelId;
	}
	public String getOldjoinDate() {
		return oldjoinDate;
	}
	public void setOldjoinDate(String oldjoinDate) {
		this.oldjoinDate = oldjoinDate;
	}
	public String getOldbtimes() {
		return oldbtimes;
	}
	public void setOldbtimes(String oldbtimes) {
		this.oldbtimes = oldbtimes;
	}
	public String getOldtotalAmount() {
		return oldtotalAmount;
	}
	public void setOldtotalAmount(String oldtotalAmount) {
		this.oldtotalAmount = oldtotalAmount;
	}
	public String getMemInfoUdTime() {
		return memInfoUdTime;
	}
	public void setMemInfoUdTime(String memInfoUdTime) {
		this.memInfoUdTime = memInfoUdTime;
	}
	public String getMemInfoMdCount() {
		return memInfoMdCount;
	}
	public void setMemInfoMdCount(String memInfoMdCount) {
		this.memInfoMdCount = memInfoMdCount;
	}
	public String getExtInfoUdTime() {
		return extInfoUdTime;
	}
	public void setExtInfoUdTime(String extInfoUdTime) {
		this.extInfoUdTime = extInfoUdTime;
	}
	public String getExtInfoMdCount() {
		return extInfoMdCount;
	}
	public void setExtInfoMdCount(String extInfoMdCount) {
		this.extInfoMdCount = extInfoMdCount;
	}
	public String getPointUdTime() {
		return pointUdTime;
	}
	public void setPointUdTime(String pointUdTime) {
		this.pointUdTime = pointUdTime;
	}
	public String getPointMdCount() {
		return pointMdCount;
	}
	public void setPointMdCount(String pointMdCount) {
		this.pointMdCount = pointMdCount;
	}
	public String getTotalPoint() {
		return totalPoint;
	}
	public void setTotalPoint(String totalPoint) {
		this.totalPoint = totalPoint;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getStartHH() {
		return startHH;
	}
	public void setStartHH(String startHH) {
		this.startHH = startHH;
	}
	public String getStartMM() {
		return startMM;
	}
	public void setStartMM(String startMM) {
		this.startMM = startMM;
	}
	public String getStartSS() {
		return startSS;
	}
	public void setStartSS(String startSS) {
		this.startSS = startSS;
	}
	public String getDifPoint() {
		return difPoint;
	}
	public void setDifPoint(String difPoint) {
		this.difPoint = difPoint;
	}
	public String getDifdateTime() {
		return difdateTime;
	}
	public void setDifdateTime(String difdateTime) {
		this.difdateTime = difdateTime;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	public String getStartSecond() {
		return startSecond;
	}
	public void setStartSecond(String startSecond) {
		this.startSecond = startSecond;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getMemberClubId() {
		return memberClubId;
	}
	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
