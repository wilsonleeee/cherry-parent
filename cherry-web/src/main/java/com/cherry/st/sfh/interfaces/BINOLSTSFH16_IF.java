package com.cherry.st.sfh.interfaces;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.form.BINOLSTSFH16_Form;

public interface BINOLSTSFH16_IF {

	/**工作流中的各种动作
     * @param form
     * @param userInfo
     * @return
     * @throws Exception 
     */
    public void tran_doaction(BINOLSTSFH16_Form form, UserInfo userInfo) throws Exception;
    
	public void tran_submit(BINOLSTSFH16_Form form, UserInfo userInfo) throws Exception;
	
	public void tran_save(BINOLSTSFH16_Form form, UserInfo userInfo) throws Exception;
	
	public void tran_delete(BINOLSTSFH16_Form form, UserInfo userInfo) throws Exception;
	
}
