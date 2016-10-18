package com.cherry.mb.cct.action;

import com.cherry.cm.core.BaseAction;
import com.cherry.mb.cct.form.BINOLMBCCT07_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT07_Action extends BaseAction implements ModelDriven<BINOLMBCCT07_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLMBCCT07_Form form = new BINOLMBCCT07_Form();

	public String init() throws Exception{
		return SUCCESS;
	}
	
	@Override
	public BINOLMBCCT07_Form getModel() {
		return form;
	}
}
