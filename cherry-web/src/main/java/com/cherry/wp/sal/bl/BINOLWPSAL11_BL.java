package com.cherry.wp.sal.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.wp.mbm.interfaces.BINOLWPMBM01_IF;
import com.cherry.wp.sal.interfaces.BINOLWPSAL11_IF;

public class BINOLWPSAL11_BL implements BINOLWPSAL11_IF{

	@Resource(name="binOLWPMBM01_BL")
	private BINOLWPMBM01_IF binOLWPMBM01_BL;
	
	@Override
	public String addMember(Map<String, Object> map) throws Exception {
		binOLWPMBM01_BL.addMem(map);
		return "SUCCESS";
	}
}
