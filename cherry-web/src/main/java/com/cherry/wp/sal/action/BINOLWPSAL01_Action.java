package com.cherry.wp.sal.action;

import com.cherry.cm.core.BaseAction;
import com.cherry.wp.sal.form.BINOLWPSAL01_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL01_Action extends BaseAction implements ModelDriven<BINOLWPSAL01_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLWPSAL01_Form form = new BINOLWPSAL01_Form();
	
	public String init(){
		return SUCCESS;
	}
	
	@Override
	public BINOLWPSAL01_Form getModel() {
		return form;
	}
}
