package com.cherry.cm.cmbeans;

public class RegisterResult {
	
	private String register_code;
	
	private Member member;
	
	public RegisterResult() {
		register_code = "";
		member = new Member();
	}

	public String getRegister_code() {
		return register_code;
	}

	public void setRegister_code(String register_code) {
		this.register_code = register_code;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
