package com.webconsole.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
import com.webconsole.form.ViewWorkFlow_Form;
import com.webconsole.service.BatchListService;

public class ViewWorkFlow extends BaseAction implements ModelDriven<ViewWorkFlow_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Resource
	private BatchListService batchListService;
	
	/** 参数FORM */
	private ViewWorkFlow_Form form = new ViewWorkFlow_Form();

	public String viewWorkFlow() throws Exception{
		List<Map<String, Object>> currentSteps = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到paramMap中，得到分页信息
		ConvertUtil.setForm(form, map);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryBatchConstants.SESSION_USERINFO);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String orgCode = userInfo.getOrgCode();
		String[] brandInfos = form.getBrandInfo().split("_");
		String brandCode = brandInfos[1];
		// 工作流名称
		String wfName = ConvertUtil.getWfName(orgCode, brandCode, "CherryBatchFlow");
		map.put("wfName", wfName);
		// 取得工作流总数
		int count = batchListService.getWorkFlowCount(map);
		if (count > 0) {
			// 取得工作流信息List
			currentSteps = (List) batchListService.getWorkFlowInfo(map);
			for(Map<String, Object> currentMap : currentSteps){
				Map<String, Object> wfNameInfo = (Map<String, Object>) JSONUtil.deserialize((String) currentMap.get("workFlowName"));
				currentMap.put("workFlowName", wfNameInfo.get("FC"));
			}
			form.setCurrentSteps(currentSteps);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		return "success";
	}



	@Override
	public ViewWorkFlow_Form getModel() {
		return form;
	}
}
