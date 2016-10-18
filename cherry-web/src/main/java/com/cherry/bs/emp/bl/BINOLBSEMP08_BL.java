package com.cherry.bs.emp.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.interfaces.BINOLBSEMP08_IF;
import com.cherry.bs.emp.service.BINOLBSEMP08_Service;
import com.cherry.cm.core.CherryException;

public class BINOLBSEMP08_BL implements BINOLBSEMP08_IF {
	
	@Resource(name="binOLBSEMP08_Service")
	private BINOLBSEMP08_Service binOLBSEMP08_Service;

	@Override
	public int getBaCouponCount(Map<String, Object> map) throws Exception {
		return binOLBSEMP08_Service.getBaCouponCount(map);
	}

	@Override
	public List<Map<String, Object>> getBaCouponList(Map<String, Object> map)
			throws Exception {
		return binOLBSEMP08_Service.getBaCouponList(map);
	}

	@Override
	public void tran_deleteBaCoupon(Map<String, Object> map) throws Exception {
		// 删除
		int successCount = binOLBSEMP08_Service.deleteBaCoupon(map);
		if(successCount != 1) {
			// 删除失败！
			throw new CherryException("ECM00011");
		}
		
	}

}
