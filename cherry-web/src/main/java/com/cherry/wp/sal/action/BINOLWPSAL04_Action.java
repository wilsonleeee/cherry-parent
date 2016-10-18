package com.cherry.wp.sal.action;

import com.cherry.cm.core.BaseAction;
import com.cherry.wp.sal.form.BINOLWPSAL04_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL04_Action extends BaseAction implements ModelDriven<BINOLWPSAL04_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLWPSAL04_Form form = new BINOLWPSAL04_Form();
	
	public String init(){
		return SUCCESS;
	}
	
	@Override
	public BINOLWPSAL04_Form getModel() {
		return form;
	}
}
