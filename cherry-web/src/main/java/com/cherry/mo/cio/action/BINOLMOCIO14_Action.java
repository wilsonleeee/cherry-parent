/*  
 * @(#)BINOLMOCIO14_Action.java     1.0 2011/06/14      
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
package com.cherry.mo.cio.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.bl.BINOLMOCIO14_BL;
import com.cherry.mo.cio.form.BINOLMOCIO14_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO14_IF;
import com.cherry.mo.common.MonitorConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 柜台消息发布Action
 * 
 * @author niushunjie
 * @version 1.0 2011.06.14
 */
public class BINOLMOCIO14_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO14_Form> {
		    
    private static final long serialVersionUID = 1904184553963915058L;
    
    private static Logger logger = LoggerFactory.getLogger(BINOLMOCIO14_Action.class.getName());

    /** 参数FORM */
    private BINOLMOCIO14_Form form = new BINOLMOCIO14_Form();
    
    @Resource(name="binOLMOCIO14_BL")
    private BINOLMOCIO14_IF binOLMOCIO14_BL;
    
    private Map counterMessage;
    
    /** 上传的文件 */
	private File upExcel;
    
    @Override
    public BINOLMOCIO14_Form getModel() {
        return form;
    }
    
    public String init(){
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("counterMessageId", form.getCounterMessageId());
        param.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        param.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
        param.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        counterMessage = binOLMOCIO14_BL.getCounterMessage(param);
        //设置radio checked
        form.setRadioControlFlag(binOLMOCIO14_BL.getControlFlag(param));
        return SUCCESS;
    }
    
    /**
     * <p>
     * 取得树
     * </p>
     * 
     * @return
     */
    public void getTree() throws Exception{
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 取得session信息
        Map<String, Object> map  = new HashMap<String, Object>();
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        map.put("counterMessageId", form.getCounterMessageId());
        //柜台显示模式（1：按区域；2：按渠道；3：按组织结构）
        map.put("selMode", form.getSelMode());
        if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
        
        if("1".equals(form.getSelMode()) || "3".equals(form.getSelMode())) {
			// 查询区域信息List
        	List<Map<String, Object>> allTree = binOLMOCIO14_BL.getAllTree(map);
			ConvertUtil.setResponseByAjax(response, allTree);
		} else if("2".equals(form.getSelMode())) {
			// 查询大区信息List
			List<Map<String, Object>> regionList = binOLMOCIO14_BL.getRegionList(map);
			ConvertUtil.setResponseByAjax(response, regionList);
		} 
        
    }
    
    /**
     * 根据大区取得渠道柜台树
     * @throws Exception
     */
    public void getChannelCntTree() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("counterMessageId", form.getCounterMessageId());
		//大区
		map.put("regionId", form.getChannelRegionId());
		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "0");
			// 操作类型
			map.put("operationType", "1");
			// 是否带权限查询
			map.put("privilegeFlag", form.getPrivilegeFlag());
		}
		// 查询渠道信息List
		List<Map<String, Object>> channelList = binOLMOCIO14_BL.getChannelCntList(map);
		ConvertUtil.setResponseByAjax(response, channelList);
    }
    
    /**
     * <p>
     * 发布
     * </p>
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String publish() throws Exception{
    	// 发布参数
    	Map<String, Object> map = this.getPublishComMap();
        String allowArray = form.getAllowNodesArray();
        String forbiddenArray = form.getForbiddenNodesArray();
        // 将参数由String类型装换成json类型
        List<Map<String, Object>> allowList = (List<Map<String, Object>>) JSONUtil.deserialize(allowArray);
        List<Map<String, Object>> forbiddenList = (List<Map<String, Object>>) JSONUtil.deserialize(forbiddenArray);

        try{
            binOLMOCIO14_BL.tran_publish(allowList, forbiddenList, map);
            // 处理成功
            this.addActionMessage(getText("ICM00002"));
            return "BINOLMOCIO14";
        }catch(Exception e){
			logger.error(e.getMessage(), e);
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return "BINOLMOCIO14";
            }else{
                throw e;
            }
        }
    }
    
    /**
     * 设置发布的共通参数
     * @return
     * @throws Exception
     */
	private Map<String, Object> getPublishComMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put("radioControlFlag", form.getRadioControlFlag());
		map.put("counterMessageId", form.getCounterMessageId());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		map.put(CherryConstants.CREATEPGM, MonitorConstants.BINOLMOCIO14);
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		map.put(CherryConstants.UPDATEPGM, MonitorConstants.BINOLMOCIO14);

		return map;
	}
    
    /**
     * 导入柜台
     * @return
     * @throws Exception
     */
    public String importCounter() throws Exception {
    	// 解析成功后的可收柜台LIST
    	List<Map<String, Object>> allowList = null;
    	// 解析成功后的禁收柜台LIST
    	List<Map<String, Object>> forbiddenList = null;
    	try{
    		// 用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 参数MAP
    		Map<String, Object> map = this.getPublishComMap();
    		if(form.getPrivilegeFlag() != null && "1".equals(form.getPrivilegeFlag())) {
    			// 用户ID
    			map.put("userId", userInfo.getBIN_UserID());
    			// 业务类型
    			map.put("businessType", "0");
    			// 操作类型
    			map.put("operationType", "1");
    			// 是否带权限查询
    			map.put("privilegeFlag", form.getPrivilegeFlag());
    		}
    		String radioControlFlag = ConvertUtil.getString(map.get("radioControlFlag"));
    		// 解析导入的柜台信息[处理后只含有柜台的组织ID信息]
    		List<Map<String, Object>> list = binOLMOCIO14_BL.parseFile(upExcel, map);
    		
    		// 去除重复的数据，保证重复的数据只记录一条
    		this.makeListUnique(list);
    		// 取得导入柜台id参数
    		List<String> contraryIDList = new ArrayList<String>();
    		for(Map<String, Object> listMap : list) {
    			contraryIDList.add(ConvertUtil.getString(listMap.get("id")));
    		}
    		map.put("contraryIDList", contraryIDList);
    		// 取得导入柜台下发类型相对立的柜台【即若导入允许下发柜台则取得禁止下发柜台】
    		List<Map<String, Object>> contraryList = binOLMOCIO14_BL.getContraryOrgID(map);
    		
     		// 导入柜台不是可收柜台就是禁收柜台，二者取其一
    		if("1".equals(radioControlFlag)){
    			// 可收柜台
    			allowList = list;
    			forbiddenList = contraryList;
    		}else if("2".equals(radioControlFlag)) {
    			// 禁收柜台
    			forbiddenList = list;
    			allowList = contraryList;
    		}
    		
    		// 批量下发柜台
            binOLMOCIO14_BL.tran_publish(allowList, forbiddenList, map);
            // 处理成功(替换整张页面)
            this.addActionMessage(getText("ICM00002"));
            return "BINOLMOCIO14";
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
            // 更新失败场合
            if(e instanceof CherryException){
                CherryException temp = (CherryException)e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                throw e;
            }
    	}
    }
    
    /**
     * 去除LIST中重复的数据
     * @param list
     */
    private void makeListUnique(List list){
    	//set方法去除list中重复的数据 set中插入重复的值只保留一个
    	HashSet h = new HashSet(list);
    	list.clear();
    	list.addAll(h);
    }
    
    public void setCounterMessage(Map counterMessage) {
        this.counterMessage = counterMessage;
    }

    public Map getCounterMessage() {
        return counterMessage;
    }

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
}
