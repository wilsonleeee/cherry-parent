/*  
 * @(#)BINOLBSRES01_Action.java     1.0 2015/08/03      
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
package com.cherry.bs.wem.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.dto.RebateDivideConf;
import com.cherry.bs.wem.form.BINOLBSWEM03_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM03_IF;
import com.cherry.bs.wem.interfaces.BINOLBSWEM04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 设置订货折扣
 * 
 * @author zhangbo
 * @version 1.0 2015/08/11
 */
public class BINOLBSWEM03_Action extends BaseAction implements ModelDriven<BINOLBSWEM03_Form> {
	
	private static final long serialVersionUID = -8300900314315505614L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM01_Action.class);

	private BINOLBSWEM03_Form form = new BINOLBSWEM03_Form();
	@Resource(name = "binOLBSWEM04_BL")
	private BINOLBSWEM04_IF binOLBSWEM04_BL;

	@Resource(name = "binOLBSWEM03_BL")
	private BINOLBSWEM03_IF binOLBSWEM03_BL;
	
	private List<RebateDivideConf> rebateDivideConfList;
	
	private List<Map<String, Object>> divideConfList = new ArrayList<Map<String,Object>>();
	
    private List reginList;
		
	private List<Map<String, Object>> agentLevelList;
	
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	/**
	 * 初始化画面查询返点分成
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String init() throws Exception {
		agentLevelList = codeTable.getCodesWithFilter("1000", "0|4");
		divideConfList = binOLBSWEM03_BL.getDivideConfList();
		if(agentLevelList!=null && !agentLevelList.isEmpty() &&
				divideConfList!=null && !divideConfList.isEmpty()){
			for(Map<String, Object> am : agentLevelList){
				for(Map<String, Object> dm : divideConfList){
					if(am.get("CodeKey").equals(dm.get("departType"))){
						am.put("dividePer", dm.get("dividePer"));
					}
				}
			}
		}
		return SUCCESS;
	}
	/**
	 * 保存返点分成
	 */
	public void save() {
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		rebateDivideConfList = form.getRebateDivideConfList();
		for(RebateDivideConf r:rebateDivideConfList){
			Map<String, Object> map = new HashMap<String, Object>();
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLBSWEM03");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLBSWEM03");
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put("departType", r.getDepartType());
			map.put("dividePer", r.getDividePer());
			list.add(map);
		}
		binOLBSWEM03_BL.save(list);
	}
	

	@Override
	public BINOLBSWEM03_Form getModel() {
		return form;
	}

	public List<RebateDivideConf> getRebateDivideConfList() {
		return rebateDivideConfList;
	}

	public void setRebateDivideConfList(
			List<RebateDivideConf> rebateDivideConfList) {
		this.rebateDivideConfList = rebateDivideConfList;
	}
	public List<Map<String, Object>> getDivideConfList() {
		return divideConfList;
	}

	public void setDivideConfList(List<Map<String, Object>> divideConfList) {
		this.divideConfList = divideConfList;
	}
		
	public List<Map<String, Object>> getAgentLevelList() {
		return agentLevelList;
	}

	public void setAgentLevelList(List<Map<String, Object>> agentLevelList) {
		this.agentLevelList = agentLevelList;
	}
	
}
