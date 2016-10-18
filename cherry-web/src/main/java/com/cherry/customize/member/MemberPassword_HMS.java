package com.cherry.customize.member;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;

/**
 * 汇美舍的会员密码  DES加密，和用户密码加密相同
 * @author dingyongchang
 *
 */
public class MemberPassword_HMS implements IMemberPassword {

	private DESPlus des ;
	
	@Override
	public String encrypt(String srcData) throws Exception {
		if(null==des){
			des = new DESPlus(CherryConstants.CUSTOMKEY);
		}
		return des.encrypt(srcData);
	}

	@Override
	public String decrypt(String encryptData)throws Exception {
		if(null==des){
			des = new DESPlus(CherryConstants.CUSTOMKEY);
		}
		return des.decrypt(encryptData);
	}
}