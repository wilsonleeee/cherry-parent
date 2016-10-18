package com.cherry.wp.wy.wyl.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWYWYL01_IF {
	
	public String getSubCampaignList(Map<String, Object> map) throws Exception;
	
	public int getReservationBillsCount(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getReservationBillsList (Map<String, Object> map) throws Exception;
	
}
