package com.cherry.wp.common.entity;

public class SaleMainEntity  
{
	private String memcode;//会员卡号
	private String txddate;//交易日期
	private String txdtime;//交易时间
	private String bacode;//ba号
	private String countercode;//柜台号
	private double discount_zd;//整单折扣率
	private String memlevel;//会员等级ID
	private double mempoint;//会员积分
	
	public SaleMainEntity() {
		super();
	}

	public SaleMainEntity(String memcode, String txddate, String txdtime,
			String bacode, String countercode, double discount_zd,
			String memlevel,double mempoint) {
		super();
		this.memcode = memcode;
		this.txddate = txddate;
		this.txdtime = txdtime;
		this.bacode = bacode;
		this.countercode = countercode;
		this.discount_zd = discount_zd;
		this.memlevel = memlevel;
		this.mempoint= mempoint;
	}
	public String getMemcode() {
		return memcode;
	}
	public void setMemcode(String memcode) {
		this.memcode = memcode;
	}
	public String getTxddate() {
		return txddate;
	}
	public void setTxddate(String txddate) {
		this.txddate = txddate;
	}
	public String getTxdtime() {
		return txdtime;
	}
	public void setTxdtime(String txdtime) {
		this.txdtime = txdtime;
	}
	public String getBacode() {
		return bacode;
	}
	public void setBacode(String bacode) {
		this.bacode = bacode;
	}
	public String getCountercode() {
		return countercode;
	}
	public void setCountercode(String countercode) {
		this.countercode = countercode;
	}
	public double getDiscount_zd() {
		return discount_zd;
	}
	public void setDiscount_zd(double discount_zd) {
		this.discount_zd = discount_zd;
	}
	public String getMemlevel() {
		return memlevel;
	}
	public void setMemlevel(String memlevel) {
		this.memlevel = memlevel;
	}

	public double getMempoint() {
		return mempoint;
	}

	public void setMempoint(double mempoint) {
		this.mempoint = mempoint;
	}
	
}
