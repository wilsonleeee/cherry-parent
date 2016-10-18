package com.cherry.customize.member;

import com.cherry.cm.core.CherryAESCoder;

public class MemberPassword_AVON implements IMemberPassword {

	@Override
	public String encrypt(String srcData) throws Exception {
		return CherryAESCoder.encrypt(srcData, "a+tk0fZuvsCDMA/pysohFA==");
	}

	@Override
	public String decrypt(String encryptData) throws Exception {
		return CherryAESCoder.decrypt(encryptData, "a+tk0fZuvsCDMA/pysohFA==");
	}

}
