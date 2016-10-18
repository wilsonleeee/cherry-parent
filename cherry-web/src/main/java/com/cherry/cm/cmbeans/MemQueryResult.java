package com.cherry.cm.cmbeans;

public class MemQueryResult {
	
	private Member member;
	
	private String query_code;
	
	public MemQueryResult() {
		query_code = "";
		member = new Member();
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getQuery_code() {
		return query_code;
	}

	public void setQuery_code(String query_code) {
		this.query_code = query_code;
	}
	
}
