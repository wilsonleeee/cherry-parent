package com.cherry.cm.cmbussiness.form;

public class SaleMainEntity  
{
	private String memcode;//会员卡号
	private String txddate;//交易日期
	private String txdtime;//交易时间
	private String bacode;//ba号
	private String countercode;//柜台号
	private double discount_zd;//整单折扣率
	

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
}
