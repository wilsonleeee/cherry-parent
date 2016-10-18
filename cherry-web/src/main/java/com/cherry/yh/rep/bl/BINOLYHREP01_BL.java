package com.cherry.yh.rep.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.yh.rep.interfaces.BINOLYHREP01_IF;
import com.cherry.yh.rep.service.BINOLYHREP01_Service;

/**
 * 按订单详细报表BL
 * 
 * @author menghao
 * 
 */
public class BINOLYHREP01_BL implements BINOLYHREP01_IF {
	
	@Resource(name="binOLYHREP01_Service")
	private BINOLYHREP01_Service binOLYHREP01_Service;

	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> sumInfo = binOLYHREP01_Service.getSumInfo(map);
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		sumInfo.putAll(binOLYHREP01_Service.getSumNoUsedInfo(map));
		sumInfo.put("count", count+ConvertUtil.getInt(sumInfo.get("count")));
		return sumInfo;
	}

	@Override
	public int getSaleOrderDetailCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLYHREP01_Service.getSaleOrderDetailCount(map);
	}

	@Override
	public List<Map<String, Object>> getSaleOrderDetailList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLYHREP01_Service.getSaleOrderDetailList(map);
	}

}
