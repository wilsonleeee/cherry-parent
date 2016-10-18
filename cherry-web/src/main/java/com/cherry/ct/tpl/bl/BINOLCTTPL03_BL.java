package com.cherry.ct.tpl.bl;

import java.util.Map;
import javax.annotation.Resource;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL03_IF;
import com.cherry.ct.tpl.service.BINOLCTTPL03_Service;

public class BINOLCTTPL03_BL implements BINOLCTTPL03_IF {

	@Resource
	private BINOLCTTPL03_Service binOLCTTPL03_Service;

	@Override
	public void tran_updateVariable(Map<String, Object> map) {
		binOLCTTPL03_Service.updateVariable(map);
	}

	@Override
	public int disOrEnable(Map<String, Object> map) {
		return binOLCTTPL03_Service.disOrEnable(map);
	}

}
