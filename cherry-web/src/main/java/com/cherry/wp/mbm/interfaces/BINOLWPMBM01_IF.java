package com.cherry.wp.mbm.interfaces;

import java.util.Map;

/**
 * 会员管理IF
 * 
 * @author WangCT
 * @version 1.0 2014/08/15
 */
public interface BINOLWPMBM01_IF {
	
	public int getMemberCount(Map<String, Object> map);
	
	public Map<String, Object> searchMemList(Map<String, Object> map)  throws Exception;
	
	public Map<String, Object> getMemReport(Map<String, Object> map);
	
	public void addMem(Map<String, Object> map) throws Exception;
	
	public void setContents(Map<String, Object> map,int type);
	
	public String couponCheck(String phoneNumber,String couponCode,Map<String, Object> map);
	
	public String cardChangeCheck(String phoneNumber,String couponCode,String phoneNumberOld,String couponCodeOld,Map<String, Object> map);
}
