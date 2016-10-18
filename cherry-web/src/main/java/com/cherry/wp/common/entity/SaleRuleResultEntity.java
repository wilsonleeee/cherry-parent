package com.cherry.wp.common.entity;

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
	private double  planDiscountPrice ;//预计优惠金额
	private double  actualDiscountPrice;//实际优惠金额
	private String activityType ;//活动类型
	private double ruleamount;//规则赠送的总金额
	private int rulequantity;//规则赠送的数量限制，如果值为0，数量任意
	private int rulebcj;//默认值为0 ，1表示可以购买超出奖励金额的产品
	private String originalBrand;//产品自身品牌,（Code类别：1299）
	private int productNumber;//产品可选数量
	private int ZGQFlag;//是否使用资格券的标识
	private double  computePoint;//参加当前活动需要的积分
	private int fullFlag;//是否全部选中标识 0为不全选 1为全选
	private int couponFlag;//是否同时可以使用代金券 0为不可以 1为可以
	private int unitPoint;//兑换单位积分数
	private int maxPoint;//最大积分兑换数
	public SaleRuleResultEntity() {
		super();
	}
	
	public SaleRuleResultEntity(int flag, int times, int matchtimes,
			String maincode, String mainname, String ismust,
			String rulecondtype, String subcampaignvalid, int level,
			int checkflag, String activitycode, double planDiscountPrice,double actualDiscountPrice,
			String activityType,double ruleamount,int rulequantity,int rulebcj,String originalBrand,int productNumber,int ZGQFlag,double computePoint,int fullFlag,int couponFlag,int unitPoint,int maxPoint) {
		super();
		this.flag = flag;
		this.times = times;
		this.matchtimes = matchtimes;
		this.maincode = maincode;
		this.mainname = mainname;
		this.ismust = ismust;
		this.rulecondtype = rulecondtype;
		this.subcampaignvalid = subcampaignvalid;
		this.level = level;
		this.checkflag = checkflag;
		this.activitycode = activitycode;
		this.planDiscountPrice = planDiscountPrice;
		this.actualDiscountPrice=actualDiscountPrice;
		this.activityType = activityType;
		this.ruleamount=ruleamount;
		this.rulequantity=rulequantity;
		this.rulebcj=rulebcj;
		this.originalBrand=originalBrand;
		this.productNumber=productNumber;
		this.ZGQFlag=ZGQFlag;
		this.computePoint=computePoint;
		this.fullFlag=fullFlag;
		this.couponFlag=couponFlag;
		this.unitPoint=unitPoint;
		this.maxPoint=maxPoint;
	}

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

	public double getPlanDiscountPrice() {
		return planDiscountPrice;
	}

	public void setPlanDiscountPrice(double planDiscountPrice) {
		this.planDiscountPrice = planDiscountPrice;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public double getActualDiscountPrice() {
		return actualDiscountPrice;
	}

	public void setActualDiscountPrice(double actualDiscountPrice) {
		this.actualDiscountPrice = actualDiscountPrice;
	}

	public double getRuleamount() {
		return ruleamount;
	}

	public void setRuleamount(double ruleamount) {
		this.ruleamount = ruleamount;
	}

	public int getRulequantity() {
		return rulequantity;
	}

	public void setRulequantity(int rulequantity) {
		this.rulequantity = rulequantity;
	}

	public int getRulebcj() {
		return rulebcj;
	}

	public void setRulebcj(int rulebcj) {
		this.rulebcj = rulebcj;
	}

	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public int getZGQFlag() {
		return ZGQFlag;
	}

	public void setZGQFlag(int zGQFlag) {
		ZGQFlag = zGQFlag;
	}

	public double getComputePoint() {
		return computePoint;
	}

	public void setComputePoint(double computePoint) {
		this.computePoint = computePoint;
	}

	public int getFullFlag() {
		return fullFlag;
	}

	public void setFullFlag(int fullFlag) {
		this.fullFlag = fullFlag;
	}

	public int getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(int couponFlag) {
		this.couponFlag = couponFlag;
	}

	public int getUnitPoint() {
		return unitPoint;
	}

	public void setUnitPoint(int unitPoint) {
		this.unitPoint = unitPoint;
	}

	public int getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(int maxPoint) {
		this.maxPoint = maxPoint;
	}
	
	
}
