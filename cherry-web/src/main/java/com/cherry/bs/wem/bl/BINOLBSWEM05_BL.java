package com.cherry.bs.wem.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM05_IF;
import com.cherry.bs.wem.service.BINOLBSWEM05_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

public class BINOLBSWEM05_BL implements BINOLBSWEM05_IF{

	@Resource(name="binOLBSWEM05_Service")
	private BINOLBSWEM05_Service binOLBSWEM05_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public List getAgentLevelList(List codeList) {
		List resultList = new ArrayList();
		if(null != codeList) {
			for(int i = 0; i < codeList.size(); i++) {
				Map tempMap = (Map) codeList.get(i);
				String value2 = ConvertUtil.getString(tempMap.get("value2"));
				if("1".equals(value2)) {
					resultList.add(tempMap);
				}
			}
		}
		Collections.sort(resultList, new CodeComparator());
		return resultList;
	}
		
	/**
	 * list比较器
	 * @author mo
	 *
	 */
	private class CodeComparator implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			Map<String, Object> map1 = (Map<String, Object>)o1;
			Map<String, Object> map2 = (Map<String, Object>)o2;
			int temp1 = CherryUtil.obj2int(map1.get("grade"));
			int temp2 = CherryUtil.obj2int(map2.get("grade"));
			if(temp1 > temp2){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
        List<Map<String, Object>> bonusList = binOLBSWEM05_Service.getBonusList(map);
        for(Map<String, Object> bonuMap : bonusList){
			String levelType = ConvertUtil.getString(bonuMap.get("levelType"));
			if("C".equals(levelType)){
				bonuMap.put("levelType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.currentLevelType"));
			}else{
				bonuMap.put("levelType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.lowerLevelType"));
			}
			String saleType = ConvertUtil.getString(bonuMap.get("saleType"));
			if("NS".equals(saleType)){
				bonuMap.put("saleType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.NS"));
			}else if("SR".equals(saleType)){
				bonuMap.put("saleType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.SR"));
			}else if("PX".equals(saleType)){
				bonuMap.put("saleType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.PX"));
			}
		}
        return bonusList;
	}

	@Override
	public int getBonusCount(Map<String, Object> map) {		
		return binOLBSWEM05_Service.getBonusCount(map);
	}

	@Override
	public List<Map<String, Object>> getBonusList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> bonusList = binOLBSWEM05_Service.getBonusList(map);
		return bonusList;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.employeeCode"), "20", "", "" },
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.employeeName"), "20", "", "" },
				{ "saleTime", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.saleTime"), "25", "", "" },
				{ "levelType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.levelType"), "20", "", "" },
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.billCode"), "25", "", "" },
				{ "saleEmployeeCode", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.saleEmployeeCode"), "20", "", "" },
				{ "saleEmployeeName", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.saleEmployeeName"), "20", "", "" },
				{ "saleType", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.saleType"), "20", "", "" },
				{ "incomeAmount", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.incomeAmount"), "20", "float", "" },
				{ "saleAmount", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.saleAmount"), "20", "float", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.quantity"), "20", "int", "" }
		};
		int dataLen = binOLBSWEM05_Service.getBonusCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLBSWEM05", language, "bswem05.downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "saleTime desc");
		return map;
	}
}
