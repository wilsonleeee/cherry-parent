package com.cherry.wp.wy.wyl.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.wp.wy.wyl.interfaces.BINOLWYWYL01_IF;
import com.cherry.wp.wy.wyl.service.BINOLWYWYL01_Service;

public class BINOLWYWYL01_BL implements BINOLWYWYL01_IF{

	@Resource(name="binOLWYWYL01_Service")
	private BINOLWYWYL01_Service binOLWYWYL01_Service;
	
	@Override
	public String getSubCampaignList(Map<String, Object> map) throws Exception {
		// 查询活动列表
		List<Map<String,Object>> resultList = binOLWYWYL01_Service.getSubCampaignList(map);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < resultList.size() ; i++){
			Map<String,Object> tempMap = resultList.get(i);
			sb.append((String)tempMap.get("subCampaignCode"));
			sb.append("|");
			sb.append((String)tempMap.get("subCampaignName"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("subCampaignId")));
			if(i != resultList.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public int getReservationBillsCount(Map<String, Object> map)
			throws Exception {
		
		return 0;
	}

	@Override
	public List<Map<String, Object>> getReservationBillsList(
			Map<String, Object> map) throws Exception {
		
		return null;
	}
}
