package com.cherry.cm.cmbeans;

public class QueryResult {
	
	private boolean bindable;
	
	private String bind_code;
	
	private Member member; 
	
	public QueryResult() {
		bind_code = "";
		member = new Member();
	}

	public boolean isBindable() {
		return bindable;
	}

	public void setBindable(boolean bindable) {
		this.bindable = bindable;
	}

	public String getBind_code() {
		return bind_code;
	}

	public void setBind_code(String bind_code) {
		this.bind_code = bind_code;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
