package com.cherry.cm.cmbeans;

public class BaseResult {
	
	private boolean success;
	
	private String error_code;
	
	public BaseResult() {
		error_code = "";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
}
