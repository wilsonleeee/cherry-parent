package com.cherry.bs.pat.interfaces;


import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLBSPAT02_IF extends ICherryInterface{
	
	public Map<String, Object> partnerDetail(Map<String, Object> map);
	
    public void tran_insertPartner(Map<String, Object> map);
	
    public String getCount(Map<String, Object> map);

	public void tran_updatePartner(Map<String, Object> map);

	public String checkCode(Map<String, Object> map);
	
	
}
