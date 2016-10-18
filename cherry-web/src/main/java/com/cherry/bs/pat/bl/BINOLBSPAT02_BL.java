package com.cherry.bs.pat.bl;

import java.util.Map;
import javax.annotation.Resource;

import com.cherry.bs.pat.service.BINOLBSPAT02_Service;
import com.cherry.bs.pat.interfaces.BINOLBSPAT02_IF;
import com.cherry.ss.common.base.SsBaseBussinessLogic;


public class BINOLBSPAT02_BL extends SsBaseBussinessLogic implements BINOLBSPAT02_IF{
	//明细画面
	@Resource
	private BINOLBSPAT02_Service binOLBSPAT02_Service;
	
	@Override
	public Map<String, Object> partnerDetail(Map<String, Object> map) {
		return binOLBSPAT02_Service.partnerDetail(map);
	}
	//添加画面
	@Override
	public void tran_insertPartner(Map<String, Object> map) {
		binOLBSPAT02_Service.addPartner(map);
	}
		
	@Override
	public String getCount(Map<String, Object> map) {
			return binOLBSPAT02_Service.getCount(map);
		}
	@Override
	public String checkCode(Map<String, Object> map) {
			return binOLBSPAT02_Service.checkCode(map);
		}
	@Override
	public void tran_updatePartner(Map<String, Object> map) {
		binOLBSPAT02_Service.tran_Partnersave(map);
	}
}

