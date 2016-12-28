/*  
 * @(#)TopAction.java     1.0 2011/05/31      
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
package com.cherry.lg.top.action;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.OnlineUserList;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM10_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.gadget.interfaces.GadgetIf;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.lg.top.bl.Top_BL;
import com.cherry.lg.top.form.Top_Form;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public  class TopAction extends BaseAction implements ModelDriven<Top_Form>{
	public TopAction (){
		super();
	}
	
	@Resource
	BINOLCM10_BL binOLCM10_BL;
	@Resource
	BINOLCM19_BL binolcm19_bl;
	@Resource
	GadgetIf gadgetBL;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	
	/** 参数FORM */
    private Top_Form form = new Top_Form();
    
    /** 下载文件名 */
    private String downloadFileName;

    @Resource
    private Top_BL top_bl;
    
    public String getDownloadFileName() throws UnsupportedEncodingException {
    	//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
    
    /*
     * 初始化TOP页面
     */
    @SuppressWarnings("unchecked")
	public String initialTop() throws Exception{
    	UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
//    	//取得任务
//       	HashMap<String,Object> pramMap = new HashMap<String,Object>();
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
//    	pramMap.put("language", userinfo.getLanguage());
//    	List<CherryTaskInstance> retList = binolcm19_bl.getUserTasks(pramMap);
//    	if(retList.size()>0){
//    		for(int i=0;i<retList.size();i++){
//    			CherryTaskInstance ret = binolcm19_bl.getTaskInfo(retList.get(i));
//    			ret.setTaskName(getText("OS.TaskName.ProType."+ret.getProType())+getText("OS.TaskName."+ret.getCurrentOperate())); 
//    		}
//    	}
//    	request.setAttribute("TaskList", retList);
    	request.getSession().setAttribute(
				CherryConstants.SESSION_TOPMENU_CURRENT, "LG");
    	
    	// 取得所有权限
		Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		// 取得对应菜单下的权限
		CherryMenu doc = (CherryMenu)xmldocumentmap.get("LG");
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userinfo.getBIN_UserID());
		map.put("pageId", "0");
		map.put("doc", doc);
		// 取得小工具信息
		Map<String, Object> gadgetInfoMap = gadgetBL.getGadgetInfoList(map);
		List<Map<String, Object>> gadgetInfoPLList = (List)gadgetInfoMap.get("gadgetInfoList");
		if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
			for(Map<String, Object> gadgetInfo : gadgetInfoPLList) {
				String gadgetCode = (String)gadgetInfo.get("gadgetCode");
				if("BINOLLGTOP01".equals(gadgetCode) || "BINOLLGTOP07".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.sale"));
				} else if("BINOLLGTOP02".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.attendance"));
				} else if("BINOLLGTOP03".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.task"));
				} else if("BINOLLGTOP04".equals(gadgetCode) || "BINOLLGTOP08".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.saleTargetRpt"));
				} else if("BINOLLGTOP05".equals(gadgetCode) || "BINOLLGTOP06".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.saleCountByHours"));
				} else if("BINOLLGTOP09".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("home.gadget.orderTask"));
				}
			}
		}
		gadgetInfoMap.put("userInfo", userinfo);
		gadgetInfoMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		gadgetInfo = JSONUtil.serialize(gadgetInfoMap);
    	
        return SUCCESS;
    }
    
//    /**
//     * 取得任务列表
//     * @return
//     * @throws Exception
//     */
//    @Deprecated
//    public String getTaskList() throws Exception{
//    	UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
//    	//取得任务
//       	HashMap<String,Object> pramMap = new HashMap<String,Object>();
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
//    	pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
//    	pramMap.put("language", userinfo.getLanguage());
//    	
//    	List<CherryTaskInstance> retList = binolcm19_bl.getUserTasks(pramMap);
//    	if(retList.size()>0){
//    		for(int i=0;i<retList.size();i++){
//    			CherryTaskInstance ret = binolcm19_bl.getTaskInfo(retList.get(i));
//    			ret.setTaskName(getText("OS.TaskName.ProType."+ret.getProType())+getText("OS.TaskName."+ret.getCurrentOperate()));    	
//    		}
//    	}
//    	request.setAttribute("TaskList", retList);
//    	
//        return SUCCESS;
//    }
    
    /**
     * 获取在线用户列表设入弹出框中
     * 
     * @return
     * @throws Exception
     */
    public String getOnlineUser() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        //获取在线用户列表
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        int count = onlineUserList.getUserCount();
        int fromIndex = CherryUtil.obj2int(map.get("START"))-1;
        int toIndex = CherryUtil.obj2int(map.get("END"));
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userAgentLiteFlag", true);
        List<Map<String,Object>> subOnlineUserInfoList = onlineUserList.getSubOnlineUserInfoList(fromIndex,toIndex,param);
        form.setOnlineUserInfoList(subOnlineUserInfoList);
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "popOnlineUser_1";
    }
    
    /**
     * 导出全部在线用户List
     * @return
     */
    public String export(){
        try {
            //获取在线用户列表
            OnlineUserList onlineUserList = OnlineUserList.getInstance();
            int fromIndex = 0;
            int toIndex = onlineUserList.getUserCount();
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("userAgentLiteFlag", false);
            List<Map<String,Object>> subOnlineUserInfoList = onlineUserList.getSubOnlineUserInfoList(fromIndex,toIndex,param);
            
            UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
            String language = ConvertUtil.getString(userinfo.getLanguage());
            downloadFileName = binOLMOCOM01_BL.getResourceValue("", language, "onlineUser.downloadFileName");
            
            Map<String,Object> searchMap = new HashMap<String,Object>();
            searchMap.put(CherryConstants.SESSION_LANGUAGE, language);
            searchMap.put("dataList", subOnlineUserInfoList);
            searchMap.put(CherryConstants.ORGANIZATIONINFOID, userinfo.getBIN_OrganizationInfoID());
            searchMap.put(CherryConstants.BRANDINFOID, userinfo.getBIN_BrandInfoID());
            form.setExcelStream(new ByteArrayInputStream(exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "TopAction_excel";
	}
	
	private byte[] exportExcel(Map<String,Object> searchMap) throws Exception{
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) searchMap.get("dataList");
        String[][] array = {
                { "LoginName", "onlineUser.LoginName", "16", "", "" },
                { "LoginIP", "onlineUser.LoginIP", "18", "", "" },
                { "LoginTime", "onlineUser.LoginTime", "20", "", "" },
                { "UserAgent", "onlineUser.UserAgent", "130", "", "" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(searchMap);
        ep.setArray(array);
        ep.setBaseName("");//取common的资源文件
        ep.setSheetLabel("onlineUser.sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
    public String topmain(){
        return SUCCESS;
    }    

    /**
     * 获取消息列表设入弹出框中
     * 
     * @return
     * @throws Exception
     */
    public String getMsgList() throws Exception {       
        UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
        
        Map<String,Object> map = new HashMap<String,Object>();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        //获取消息列表
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("LoginName", userinfo.getLoginName());
        param.put("SessionID", userinfo.getSessionID());
        List<Map<String,Object>> msgList = onlineUserList.getMsgList(param);
        
        if(null == msgList){
            msgList = new ArrayList<Map<String,Object>>();
        }
        int count = msgList.size();
        int fromIndex = CherryUtil.obj2int(map.get("START"))-1;
        int toIndex = CherryUtil.obj2int(map.get("END"));
        if(fromIndex < -1){
            fromIndex = 0;
        }
        if(toIndex > msgList.size()){
            toIndex = msgList.size();
        }
        List<Map<String,Object>> subMessageList = msgList.subList(fromIndex, toIndex);
        for(int i=0;i<subMessageList.size();i++){
            Map<String,Object> messageDTO = subMessageList.get(i);
            if(messageDTO.get("type").equals("export")){
                String content = ConvertUtil.getString(messageDTO.get("content"));
                messageDTO.put("filename", content.substring(content.lastIndexOf("\\") + 1));
            }
        }
        form.setMsgList(subMessageList);
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return "popMsgList_1";
    }

    public String getMsgList2Init() throws Exception{



        return SUCCESS;
    }

    /**
     * 获取柜台通知消息列表设入弹出框中
     *
     * @return
     * @throws Exception
     */
    public String getMsgList2() throws Exception {

        CounterInfo counterInfo = (CounterInfo)session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);

        Map<String,Object> map = new HashMap<String,Object>();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);

        map.put("organizationId", counterInfo.getOrganizationId());

        List<Map<String,Object>> msgList;

        int count = top_bl.getMsgList2Count(map);

        if(count != 0){
            msgList = top_bl.getMsgList2(map);
            form.setMsgList(msgList);
        }

        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        return SUCCESS;
    }
    
    /**
     * 设置消息已读（一条/全部），返回未读数。
     * @throws Exception 
     */
    public void setMsgRead() throws Exception{
        UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);

        String messageID = ConvertUtil.getString(form.getMessageID());
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("MessageID", messageID);
        param.put("SessionID", userinfo.getSessionID());
        param.put("LoginName", userinfo.getLoginName());
        OnlineUserList onlineUserList = OnlineUserList.getInstance();
        int unReadCount = onlineUserList.setMsgRead(param);
        ConvertUtil.setResponseByAjax(response, unReadCount);
    }
    
    private String gadgetInfo;
	
    public String getGadgetInfo() {
		return gadgetInfo;
	}

	public void setGadgetInfo(String gadgetInfo) {
		this.gadgetInfo = gadgetInfo;
	}

    @Override
    public Top_Form getModel() {
        return form;
    }
}
