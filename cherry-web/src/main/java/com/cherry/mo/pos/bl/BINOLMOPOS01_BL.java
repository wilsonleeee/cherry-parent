package com.cherry.mo.pos.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pos.interfaces.BINOLMOPOS01_IF;
import com.cherry.mo.pos.service.BINOLMOPOS01_Service;

public class BINOLMOPOS01_BL implements BINOLMOPOS01_IF{
	@Resource(name="binOLMOPOS01_Service")
	private BINOLMOPOS01_Service binOLMOPOS01_BL;
	
	@Override
	public List<Map<String, Object>> getStorePayConfigList(Map<String, Object> map) {
		return binOLMOPOS01_BL.getStorePayConfigList(map);
	}
	@Override
	public int getListCount(Map<String, Object> map) {
		return binOLMOPOS01_BL.getListCount(map);
	}
	public void addStorePayConfig(List<Map<String,Object>> plist) {
		binOLMOPOS01_BL.addStorePayConfig(plist);
	}
	public List<Map<String, Object>> getStorePayCodeList(Map<String, Object> map) {
		return binOLMOPOS01_BL.getStorePayCodeList(map);
	}
	public void delStorePayConfig(Map<String, Object> map) {
		binOLMOPOS01_BL.delStorePayConfig(map);
	}
	public void editStorePayConfig(List<Map<String, Object>> list) {
		// 如果有默认支付方式且不是当前修改的默认支付方式，则取消原来的默认方式
		if(null!=list && !list.isEmpty()){
			for(Map<String, Object> m : list){
				if("default".equals(ConvertUtil.getString(m.get("flag")))){
					binOLMOPOS01_BL.editDefaultPay(m);
				}
			}
		}
		binOLMOPOS01_BL.editStorePayConfig(list);
	}

}
