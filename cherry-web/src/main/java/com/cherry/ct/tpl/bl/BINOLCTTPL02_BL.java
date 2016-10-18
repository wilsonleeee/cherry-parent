package com.cherry.ct.tpl.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL02_IF;
import com.cherry.ct.tpl.service.BINOLCTTPL02_Service;

public class BINOLCTTPL02_BL implements BINOLCTTPL02_IF {

	@Resource
	private BINOLCTTPL02_Service binOLCTTPL02_Service;
	
	@Override
	public void saveTemplate(Map<String, Object> map, String type)
			throws Exception {
		// 插表时的共通字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		// 系统时间
		String sysDate = binOLCTTPL02_Service.getSYSDate();
		// 作成日时
		insertMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		insertMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		insertMap.put(CherryConstants.CREATEPGM, "BINOLCTTPL02");
		// 更新程序名
		insertMap.put(CherryConstants.UPDATEPGM, "BINOLCTTPL02");
		// 增加共通字段
		map.putAll(insertMap);
		if(type.equals("INSERT")){
			binOLCTTPL02_Service.insertTemplate(map);
		}else{
			binOLCTTPL02_Service.updateTemplate(map);
		}
	}

	@Override
	public Map<String, Object> getTemplateInfo(Map<String, Object> map) throws Exception {
		// 获取模板详细信息
		return binOLCTTPL02_Service.getTemplateInfo(map);
	}
	
}
