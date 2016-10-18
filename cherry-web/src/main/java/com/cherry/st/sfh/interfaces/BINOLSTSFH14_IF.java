package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.st.sfh.form.BINOLSTSFH14_Form;

public interface BINOLSTSFH14_IF {

	public int tran_submitSaleBill(BINOLSTSFH14_Form form, UserInfo userInfo) throws Exception;
	
	public int tran_saveSaleBill(BINOLSTSFH14_Form form, UserInfo userInfo) throws Exception;
	
	public List<Map<String, Object>> ResolveExcel(Map<String, Object> map,List<Map<String, Object>> curSaleList) throws Exception;
}
