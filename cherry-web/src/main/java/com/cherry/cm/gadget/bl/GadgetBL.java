package com.cherry.cm.gadget.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.gadget.interfaces.GadgetIf;
import com.cherry.cm.gadget.service.GadgetService;

public class GadgetBL implements GadgetIf {
	
	@Resource
	private GadgetService gadgetService;

	@Override
	public Map<String, Object> getGadgetInfoList(Map<String, Object> map) throws Exception {
		
		Map<String, Object> gadgetInfo = new HashMap<String, Object>();
		// 取得用户自定义配置的小工具List
		List<Map<String, Object>> gadgetInfoList = gadgetService.getUserGadgetInfoList(map);
		if(gadgetInfoList == null || gadgetInfoList.isEmpty()) {
			// 取得系统默认配置的小工具List
			gadgetInfoList = gadgetService.getSysGadgetInfoList(map);
		}
		
		List<Map<String, Object>> gadgetInfoPLList = new ArrayList<Map<String,Object>>();
		if(gadgetInfoList != null && !gadgetInfoList.isEmpty()) {
			String plFlag = (String)map.get("plFlag");
			if(plFlag != null && "0".equals(plFlag)) {
				gadgetInfoPLList.addAll(gadgetInfoList);
			} else {
				CherryMenu doc = (CherryMenu)map.get("doc");
				if(doc != null) {
					for(int i = 0; i < gadgetInfoList.size(); i++) {
						String gadgetCode = (String)gadgetInfoList.get(i).get("gadgetCode");
						if(doc.getChildMenuByID(gadgetCode)!= null) {
							gadgetInfoPLList.add(gadgetInfoList.get(i));
						}
					}
				}
			}
			if(gadgetInfoPLList != null && !gadgetInfoPLList.isEmpty()) {
				// 小工具画面列数
				int gadgetColumn = 1;
				for(int i = 0; i < gadgetInfoPLList.size(); i++) {
					int columnNumber = (Integer)gadgetInfoPLList.get(i).get("columnNumber");
					if(columnNumber+1 > gadgetColumn) {
						gadgetColumn = columnNumber+1;
					}
				}
				// 小工具数量小于列数的场合，把小工具数量作为列的数量
				if(gadgetInfoPLList.size() < gadgetColumn) {
					gadgetColumn = gadgetInfoPLList.size();
					for(int i = 0; i < gadgetInfoPLList.size(); i++) {
						gadgetInfoPLList.get(i).put("columnNumber", i);
					}
				}
				// 小工具宽度总比例
				int gadgetContext = Integer.parseInt(PropertiesUtil.pps.getProperty("gadget.context"));
				gadgetInfo.put("gadgetContext", gadgetContext);
				// 小工具宽度
				int gadgetWidth = gadgetContext/gadgetColumn;
				gadgetInfo.put("gadgetWidth", gadgetWidth);
				StringBuffer gadgetLayout = new StringBuffer();
				for(int i = 0; i < gadgetColumn; i++) {
					gadgetLayout.append("<div id=\"gadget_column"+i+"\" class=\"gadgets_gadget_column\" style=\"width:"+gadgetWidth+"%;\">&nbsp;</div>");
				}
				// 小工具布局设定
				gadgetInfo.put("gadgetLayout", gadgetLayout.toString());
				// 小工具信息List设定
				gadgetInfo.put("gadgetInfoList", gadgetInfoPLList);
				// 小工具端口设定
				gadgetInfo.put("gadgetPort", PropertiesUtil.pps.getProperty("gadget.port"));
			}
		}
		return gadgetInfo;
	}

}
