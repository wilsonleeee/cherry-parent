package com.cherry.mb.ptm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.gadget.interfaces.GadgetIf;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLMBPTM01_Action extends BaseAction {
	
	private static final long serialVersionUID = 378130598932698145L;
	
	@Resource
	GadgetIf gadgetBL;
	
    /*
     * 初始化TOP页面
     */
    @SuppressWarnings("unchecked")
	public String init() throws Exception{
    	
    	UserInfo userinfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
    	// 取得所有权限
		Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		// 取得对应菜单下的权限
		CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
    	
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userinfo.getBIN_UserID());
		map.put("pageId", "1");
		map.put("doc", doc);
		// 取得小工具信息
		Map<String, Object> gadgetInfoMap = gadgetBL.getGadgetInfoList(map);
		List<Map<String, Object>> gadgetInfoPLList = (List)gadgetInfoMap.get("gadgetInfoList");
		if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
			for(Map<String, Object> gadgetInfo : gadgetInfoPLList) {
				String gadgetCode = (String)gadgetInfo.get("gadgetCode");
				if("BINOLMBPTM01_01".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.pointCalInfo"));
				} else if("BINOLMBPTM01_02".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.dayRuleCalCount"));
				} else if("BINOLMBPTM01_03".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.rulCalState"));
				} else if("BINOLMBPTM01_04".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.calMemCount"));
				} else if("BINOLMBPTM01_05".equals(gadgetCode)) {
					gadgetInfo.put("gadgetName", getText("member.gadget.calMemAmount"));
				}
			}
		}
		gadgetInfoMap.put("userInfo", userinfo);
		gadgetInfoMap.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		gadgetInfo = JSONUtil.serialize(gadgetInfoMap);
    	
        return SUCCESS;
    }
    
    private String gadgetInfo;
	
    public String getGadgetInfo() {
		return gadgetInfo;
	}

	public void setGadgetInfo(String gadgetInfo) {
		this.gadgetInfo = gadgetInfo;
	}

}
