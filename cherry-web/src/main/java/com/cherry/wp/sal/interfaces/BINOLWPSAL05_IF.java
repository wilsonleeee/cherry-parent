package com.cherry.wp.sal.interfaces;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.wp.sal.form.BINOLWPSAL05_Form;

public interface BINOLWPSAL05_IF {
	
	public String tran_hangBill(BINOLWPSAL05_Form form, UserInfo userInfo) throws Exception;
	
}
