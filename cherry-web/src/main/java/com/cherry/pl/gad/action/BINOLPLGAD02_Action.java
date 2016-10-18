/*	
 * @(#)BINOLPLGAD02_Action.java     1.0 2013.8.29		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.pl.gad.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.mbm.action.BINOLMBMBM06_Action;
import com.cherry.pl.gad.form.BINOLPLGAD02_Form;
import com.cherry.pl.gad.interfaces.BINOLPLGAD02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 小工具布局配置Action
 *
 * @author WangCT
 * @version 1.0 2013.8.29	
 */
public class BINOLPLGAD02_Action extends BaseAction implements ModelDriven<BINOLPLGAD02_Form> {

	private static final long serialVersionUID = -398127413467439275L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPLGAD02_Action.class);
	
	/** 小工具布局配置BL **/
	@Resource
	private BINOLPLGAD02_IF binOLPLGAD02_BL;
	
	/**
	 * 小工具布局配置画面
	 * 
	 * @return 小工具布局配置画面
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 小工具布局配置画面
	 * 
	 * @return 小工具布局配置画面
	 */
	public String search() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 查询某个画面的所有小工具List
		gadgetInfoList = binOLPLGAD02_BL.getGadgetInfoList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 保存小工具布局配置
	 * 
	 * @return 小工具布局配置画面
	 */
	public String save() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		try {
			// 保存小工具布局配置
			binOLPLGAD02_BL.saveGadgetInfo(map);
		} catch (Exception e) {
			this.addActionError(getText("ECM00005")); 
			logger.error(e.getMessage(), e);
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 
	 * 保存小工具布局配置前字段验证处理
	 * 
	 */
	public void validateSave() throws Exception {
		
		// 行不能为空且必须是大于0的整数
		if(form.getRowNumbers() != null) {
			for(int i = 0; i < form.getRowNumbers().length; i++) {
				if(form.getRowNumbers()[i] == null || "".equals(form.getRowNumbers()[i])) {
					this.addFieldError("rowNumbers_"+i, getText("ECM00009",new String[]{getText("PPL00045")}));
				} else {
					if(!CherryChecker.isNumeric(form.getRowNumbers()[i])) {
						this.addFieldError("rowNumbers_"+i, getText("EPL00021",new String[]{getText("PPL00045")}));
					} else if(Integer.parseInt(form.getRowNumbers()[i]) <= 0) {
						this.addFieldError("rowNumbers_"+i, getText("EPL00021",new String[]{getText("PPL00045")}));
					}
				}
			}
		}
		// 列不能为空且必须是大于0的整数
		if(form.getColumnNumbers() != null) {
			for(int i = 0; i < form.getColumnNumbers().length; i++) {
				if(form.getColumnNumbers()[i] == null || "".equals(form.getColumnNumbers()[i])) {
					this.addFieldError("columnNumbers_"+i, getText("ECM00009",new String[]{getText("PPL00046")}));
				} else {
					if(!CherryChecker.isNumeric(form.getColumnNumbers()[i])) {
						this.addFieldError("columnNumbers_"+i, getText("EPL00021",new String[]{getText("PPL00046")}));
					} else if(Integer.parseInt(form.getColumnNumbers()[i]) <= 0) {
						this.addFieldError("columnNumbers_"+i, getText("EPL00021",new String[]{getText("PPL00046")}));
					}
				}
			}
		}
	}
	
	/** 小工具List **/
	private List<Map<String, Object>> gadgetInfoList;
	
	public List<Map<String, Object>> getGadgetInfoList() {
		return gadgetInfoList;
	}

	public void setGadgetInfoList(List<Map<String, Object>> gadgetInfoList) {
		this.gadgetInfoList = gadgetInfoList;
	}

	/** 小工具布局配置Form **/
	private BINOLPLGAD02_Form form = new BINOLPLGAD02_Form();

	@Override
	public BINOLPLGAD02_Form getModel() {
		return form;
	}

}
