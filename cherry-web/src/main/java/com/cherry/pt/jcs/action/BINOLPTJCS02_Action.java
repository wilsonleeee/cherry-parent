/*	
 * @(#)BINOLPTJCS02_Action.java     1.0 2012-7-25 		
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
package com.cherry.pt.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pt.jcs.form.BINOLPTJCS02_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 	产品扩展属性详细Action
 * 
 * @author jijw
 * @version 1.0 2012-7-25
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS02_Action extends BaseAction implements
ModelDriven<BINOLPTJCS02_Form>{

	private static final long serialVersionUID = 4902047775201921024L;
	
	/** 产品扩展属性list **/
	private List<Map<String, Object>> extPropertyList;
	
	/** 产品扩展属性Map **/
	private Map extPropertyMap;
	
	/** 产品扩展属性预设值list **/
	private List extDefValueList;
	
	/** 参数FORM */
	private BINOLPTJCS02_Form form = new BINOLPTJCS02_Form();
	
	/** 产品扩展属性BL **/
	@Resource
	private BINOLPTJCS02_IF binOLPTJCS02_IF;
	
	/**
	 * 
	 * 画面初期显示
	 * 
	 * @param 无
	 * 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 产品扩展属性
		map.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);

		form.setExtPropertyList(binOLPTJCS02_IF.getProductExtPropertyList(map));
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * 
	 * @return
	 */
	public String search() throws Exception {
		commonFresh();
		return SUCCESS;
	}
	
	/**
	 * 刷新
	 */
	public String refresh(){
		commonFresh();
		return SUCCESS;
	}
	
	/**
	 * 保存
	 */
	@SuppressWarnings("unchecked")
	public String save() throws Exception{
		Map<String, Object> map = getParamsMap();
		
//		if(!validateSave()){
//			return CherryConstants.GLOBAL_ACCTION_RESULT;
//		}
		
		// 扩展属性画面控件类型
		String viewType = form.getViewType();
		
		if(null != form.getExtendPropertyID()){
			map.put("extendPropertyID", form.getExtendPropertyID());
		}
		if(null != form.getUpdateTime()){
			map.put("updateTimeOld", form.getUpdateTime());
		}
		if(null != form.getModifyCount()){
			map.put("modifyCount", form.getModifyCount());
		}
		if(null != form.getPropertyKey() && !viewType.equals("checkbox")){
			map.put("propertyKey", form.getPropertyKey());
		}
		if(null != form.getGroupID()){
			map.put("groupID", form.getGroupID());
		}
		if(viewType.equals("checkbox")){
			map.put("groupName", form.getPropertyName());
			map.put("groupNameForeign", form.getPropertyNameForeign());
		}
		map.put("propertyName", form.getPropertyName());
		map.put("propertyNameForeign", form.getPropertyNameForeign());
		
		map.put("viewType", viewType);
		
		int i=0;
		try {
			if(null != form.getExtendPropertyID()){
				// 编辑
			   i = binOLPTJCS02_IF.tran_updateProductExtProperty(map);
			   if(i==0){
					this.addActionError(getText("ECM00038"));
					return CherryConstants.GLOBAL_ACCTION_RESULT; 
			   }
			}else{
				// 新增  7为扩展属性key
				String propertyKey = binOLPTJCS02_IF.getPropertyId(map,"7");
				if(viewType.equals("checkbox")){
					// checkbox
					map.put("groupID", propertyKey.substring(2)); 
				}

				// text radio select
				map.put("propertyKey", propertyKey);
				binOLPTJCS02_IF.tran_insertProductExtProperty(map);
			}
		} catch (DuplicateKeyException e) {
			this.addActionError(getText("ECM00032",new String[]{getText("PPT00001")}));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * save验证
	 * 
	 * @param
	 * @return
	 * */
	public void validateSave() throws Exception {
		Map<String, Object> map = getParamsMap();
        // 扩展属性名称空白
        if (CherryChecker.isNullOrEmpty(form.getPropertyName())) {
            this.addActionError(getText("ECM00009",new String[]{getText("PPT00001")}));
        }  else {
        	// 扩展属性名称重复
        	map.put("propertyName", form.getPropertyName());
        	List<Map<String,Object>> list = binOLPTJCS02_IF.getProductExtPropertyList(map);
        	int count = list.size();
        	
        	if(null != form.getExtendPropertyID()){
        		// 更新
        		if(count > 0){
        			String extendPropertyID = ((Map<String,Object>)list.get(0)).get("extendPropertyID").toString();
        			if(( count == 1 && !form.getExtendPropertyID().equals(extendPropertyID)) ) {
        				this.addActionError(getText("ECM00032",new String[]{getText("PPT00002"),"20"}));
        			}
        		}
        	} else{
        		// 新增
        		if(count > 0) {
        			this.addActionError(getText("ECM00032",new String[]{getText("PPT00002"),"20"}));
        		}
        	}
        }
	}
	
	/**
	 * 停用或启用
	 * @return
	 * @throws Exception
	 */
	public String disOrEnable() throws Exception {
		Map<String, Object> map = getParamsMap();
		String requestData = request.getParameter("requestData");
		String[] str=requestData.split(",");
		for(int i=0;i<str.length;i++){
			if(str[i]!=null&&!str[i].equals("")){
				String[] extPropIdData=str[i].split("join");
				map.put("extendPropertyID",extPropIdData[0]);
				
				map.put("data", extPropIdData[1]);//为0为停用，为1为启用
				map.put("groupID", extPropIdData.length == 3 ? extPropIdData[2] : null);//为0为停用，为1为启用
				
				// 处理checkbox 停用/启用
				if(null != map.get("groupID")){
					map.remove("extendPropertyID");
				}
				
				// 对选中的扩展属性执行停用/启用
				binOLPTJCS02_IF.tran_disOrEnableExtProp(map);
				
			}
		}
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 处理成功后，检索
	 */
	private void commonFresh(){
		Map<String,Object> map = getParamsMap();
		// 取得扩展属性List
		form.setExtPropertyList(binOLPTJCS02_IF.getProductExtPropertyList(map));
	}
	
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getParamsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 产品扩展属性
		map.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);
		
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS02");
		// 更新者
		map.put(CherryConstants.UPDATEDBY,  userInfo.getBIN_UserID());
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS02");
		
		return map;
	}

	@Override
	public BINOLPTJCS02_Form getModel() {
		return form;
	}
	
	/***###############################################  extendDefValue start  ######################################################***/
	
	/**
	 * 扩展属性预设值初期显示
	 * 
	 * @return code值管理详细画面
	 */
	public String defValueInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 产品扩展属性ID
		map.put("extendPropertyID",form.getExtendPropertyID());
		// 取得产品扩展属性
		extPropertyMap = binOLPTJCS02_IF.getProductExtPropertyList(map).get(0);
		
		// 扩展属性画面控件类型
		String viewType = form.getViewType();
		
		if(viewType.equals("checkbox")){
			extDefValueList =  binOLPTJCS02_IF.getProductExtDefValueListForChk(extPropertyMap);
			// 若checkbox 的复选项为1则在选产品扩展属性画面不显示。
			extDefValueList = extDefValueList.size() == 1 ? null :extDefValueList;
		} else {
			// 取得产品扩展属性预设值List
			extDefValueList = binOLPTJCS02_IF.getProductExtDefValueList(map);
		}
		
		form.setExtDefValueList(extDefValueList);
		
		return SUCCESS;
		
	}
	
	/**
	 * 返回按钮
	 * 
	 * 
	 * */
	public String doBack(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 产品扩展属性ID
		map.put("extendPropertyID",form.getExtendPropertyID());
		// 取得产品扩展属性
		extPropertyMap = binOLPTJCS02_IF.getProductExtPropertyList(map).get(0);
		
		// 扩展属性画面控件类型
		String viewType = form.getViewType();
		
		if(viewType.equals("checkbox")){
			extDefValueList =  binOLPTJCS02_IF.getProductExtDefValueListForChk(extPropertyMap);
			// 若checkbox 的复选项为1则在选产品扩展属性画面不显示。
			extDefValueList = extDefValueList.size() == 1 ? null :extDefValueList;
		} else {
			// 取得产品扩展属性预设值List
			extDefValueList = binOLPTJCS02_IF.getProductExtDefValueList(map);
		}
		
		form.setExtDefValueList(extDefValueList);
		
		return SUCCESS;
	}
	
	/**
	 * 保存编辑
	 * 
	 * */
	public String saveEdit() throws Exception{
		try{
			//用户及操作信息
			Map<String,Object> sessionMap = this.getSessionMap();
			//产品扩展属性预设值
			Map<String,Object> extDefValMap = new HashMap<String,Object>();
			extDefValMap.put("extendPropertyID", form.getExtendPropertyID());
			//选项值
			String viewType = form.getViewType().trim();
			extDefValMap.put("viewType",viewType);
			// checkbox key
			extDefValMap.put("groupID", form.getGroupID().trim());
			//checkbox groupName中文名称
			extDefValMap.put("groupName", form.getGroupName().trim());
			//checkbox groupName英文名称
			extDefValMap.put("groupNameForeign", form.getGroupNameForeign().trim());
//			//Value2说明
//			extDefValMap.put("value2", form.getValue2().trim());
//			//Value3说明
//			extDefValMap.put("value3", form.getValue3().trim());
//			// 排序
//			extDefValMap.put("orderNumber", form.getOrderNumber().trim());
			// 有效区分
			//extDefValMap.put("validFlag", form.getValidFlag().trim());
			
			List<String[]> extDefValList = new ArrayList<String[]>();
			extDefValList.add(form.getValue1Arr());
			extDefValList.add(form.getValue2Arr());
//			extDefValList.add(form.getValue3Arr());
//			extDefValList.add(form.getOrderNumberArr());
			
			String extendPropertyID = binOLPTJCS02_IF.tran_savaEdit(extDefValList, extDefValMap, sessionMap);
			
			Map<String,Object> searchMap = new HashMap<String,Object>();
			// 取得产品扩展属性list
			if(viewType.equals("checkbox")){
				searchMap.put("groupID", form.getGroupID().trim());
			} else {
				searchMap.put("extendPropertyID", extendPropertyID);
				// 产品扩展属性ID
				searchMap.put("extendPropertyID",form.getExtendPropertyID());
			}
			
			extPropertyMap = binOLPTJCS02_IF.getProductExtPropertyList(searchMap).get(0);
			
			// 取得产品扩展属性预设值List
			if(viewType.equals("checkbox")){
				extDefValueList =  binOLPTJCS02_IF.getProductExtDefValueListForChk(extPropertyMap);
				extDefValueList = extDefValueList.size() == 1 ? null :extDefValueList;
			} else {
				// 取得产品扩展属性预设值List
				extDefValueList = binOLPTJCS02_IF.getProductExtDefValueList(searchMap);
			}
			form.setExtDefValueList(extDefValueList);
			
		}catch(Exception e){
			CherryException temp = (CherryException) e;
			this.addActionError(temp.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	public Map<String,Object> getSessionMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.CREATEPGM, "BINOLPTJCS02");
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS02");
		return map;
	}
	
	/***###############################################  extendDefValue end  ######################################################***/
	
	public List<Map<String, Object>> getExtPropertyList() {
		return extPropertyList;
	}

	public void setExtPropertyList(List<Map<String, Object>> extPropertyList) {
		this.extPropertyList = extPropertyList;
	}
	
	public Map<String, Object> getExtPropertyMap() {
		return extPropertyMap;
	}

	public void setExtPropertyMap(Map<String, Object> extPropertyMap) {
		this.extPropertyMap = extPropertyMap;
	}

	public List<Map<String, Object>> getExtDefValueList() {
		return extDefValueList;
	}

	public void setExtDefValueList(List<Map<String, Object>> extDefValueList) {
		this.extDefValueList = extDefValueList;
	}
}
