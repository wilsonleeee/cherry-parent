package com.cherry.customize.member;

public interface IMemberPassword {
public String encrypt(String srcData) throws Exception;
public String decrypt(String encryptData) throws Exception;
}
