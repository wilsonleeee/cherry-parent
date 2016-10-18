package com.cherry.mo.cio.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.mo.cio.form.BINOLMOCIO11_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO11_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO11_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO11_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLMOCIO11_Form form = new BINOLMOCIO11_Form();
	
	@Resource
	private BINOLMOCIO11_IF binOLMOCIO11_BL;
	
	private Map<String,Object> paperMap;
	
	private List<Map<String,Object>> groupList;

	public String init() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paperId", form.getPaperId());
		paperMap = binOLMOCIO11_BL.getCheckPaper(map);
		groupList = binOLMOCIO11_BL.getCheckPaperGroup(map);
		String questionStr = JSONUtil.serialize(binOLMOCIO11_BL.getCheckQuestion(map));
		form.setQuestionStr(questionStr);
		return SUCCESS;
	}
	
	public Map<String, Object> getPaperMap() {
		return paperMap;
	}
	public void setPaperMap(Map<String, Object> paperMap) {
		this.paperMap = paperMap;
	}
	public List<Map<String, Object>> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Map<String, Object>> groupList) {
		this.groupList = groupList;
	}
	
	@Override
	public BINOLMOCIO11_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
