package com.cherry.cm.cmbussiness.form;

public class SaleRuleResultEntity
{
	private int flag;//是否存在需要交互的产品，0表示不需要交互，1表示需要交互
	private int times;//活动数量
	private int matchtimes;//最大匹配数量
	private String maincode;//活动主码
	private String mainname;//主活动名称
	private String ismust;//可选不可选  0：可选；1：不可选 
	private String rulecondtype;//促销条件类型 1：整单类，2：非整单类
	private String subcampaignvalid;//校验方式[0(无需校验) 1（本地校验）2（在线校验）]
	private int level;//规则等级
	private int checkflag;//是否选中，0是未选中，1是选中
	private String activitycode;//活动code
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getMatchtimes() {
		return matchtimes;
	}

	public void setMatchtimes(int matchtimes) {
		this.matchtimes = matchtimes;
	}

	public String getMaincode() {
		return maincode;
	}

	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}

	public String getMainname() {
		return mainname;
	}

	public void setMainname(String mainname) {
		this.mainname = mainname;
	}

	public String getIsmust() {
		return ismust;
	}

	public void setIsmust(String ismust) {
		this.ismust = ismust;
	}

	public String getRulecondtype() {
		return rulecondtype;
	}

	public void setRulecondtype(String rulecondtype) {
		this.rulecondtype = rulecondtype;
	}

	public String getSubcampaignvalid() {
		return subcampaignvalid;
	}

	public void setSubcampaignvalid(String subcampaignvalid) {
		this.subcampaignvalid = subcampaignvalid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(int checkflag) {
		this.checkflag = checkflag;
	}

	public String getActivitycode() {
		return activitycode;
	}

	public void setActivitycode(String activitycode) {
		this.activitycode = activitycode;
	}
	
}
