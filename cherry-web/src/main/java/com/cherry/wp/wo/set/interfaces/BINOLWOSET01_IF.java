package com.cherry.wp.wo.set.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 营业员管理IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public interface BINOLWOSET01_IF {
	
	public int getBAInfoCount(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map) throws Exception;
	
	public void tran_addBA(Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getBAInfo(Map<String, Object> map) throws Exception;
	
	public void tran_updBA(Map<String, Object> map) throws Exception;
	
	public void tran_delBA(Map<String, Object> map) throws Exception;
	
	public Map<String, Object> checkBAInfo(Map<String, Object> map) throws Exception;
	
	public void synaBa(Map<String,Object> map) throws Exception; 

}
