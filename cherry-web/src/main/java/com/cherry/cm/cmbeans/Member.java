package com.cherry.cm.cmbeans;

public class Member {
	
	private Long point;
	
	private Long level;
	
	private String extend;
	
	private String mobile;
	
	public Member() {
		extend = "{}";
		mobile = "";
		level = 0L;
		point = 0L;
	}
	
	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
