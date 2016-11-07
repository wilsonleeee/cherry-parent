package com.cherry.wp.sal.interfaces;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.wp.sal.form.BINOLWPSAL03_Form;

import java.util.List;
import java.util.Map;

public interface BINOLWPSAL03_IF {

	public String tran_collect(BINOLWPSAL03_Form form, UserInfo userInfo) throws Exception;

	public List<Map<String, Object>> getPayPartnerConfig(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getPaymentDetailList(Map<String, Object> map) throws Exception;
	
	public int getCZKPayStateCount(Map<String,Object> map);
	
	public void updateHangBillCollectState(Map<String,Object> map) throws Exception;
	
	public void getHangBillSetForm(Map<String,Object> param_Map,BINOLWPSAL03_Form form)  throws Exception;

	public  Map<String,Object> getHangBillInfo(Map<String,Object> map);
}
