package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO11_IF;
import com.cherry.mo.cio.service.BINOLMOCIO11_Service;
import com.cherry.mo.common.MonitorConstants;

public class BINOLMOCIO11_BL implements BINOLMOCIO11_IF {

	@Resource
	private BINOLMOCIO11_Service binOLMOCIO11_Service;
	
	@Override
	public Map<String, Object> getCheckPaper(Map<String, Object> map) {
		Map<String,Object> resultMap = binOLMOCIO11_Service.getCheckPaper(map);
//		for(Map.Entry<String, Object> mp:resultMap.entrySet()){
//			String value = ConvertUtil.getString(mp.getValue());
//			String newValue = CherryUtil.convertStrToJson(value);
//			mp.setValue(newValue);
//		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getCheckPaperGroup(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLMOCIO11_Service.getCheckPaperGroup(map);
//		for(int i = 0 ; i < resultList.size() ; i++){
//			Map<String,Object> mapOfList = resultList.get(i);
//			for(Map.Entry<String, Object> mp:mapOfList.entrySet()){
//				String value = ConvertUtil.getString(mp.getValue());
//				String newValue = CherryUtil.convertStrToJson(value);
//				mp.setValue(newValue);
//			}
//		}
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getCheckQuestion(Map<String, Object> map) {
		List<Map<String,Object>> newQuestionList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> questionList = binOLMOCIO11_Service.getCheckQuestion(map);
		for(int i = 0 ; i < questionList.size() ; i++){
			Map<String, Object> map1 = questionList.get(i);
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("questionType", map1.get("questionType"));
			map2.put("questionItem", map1.get("questionItem"));
			map2.put("displayOrder", map1.get("displayOrder"));
			map2.put("maxPoint", map1.get("maxPoint"));
			map2.put("minPoint", map1.get("minPoint"));
			map2.put("checkQuestionGroupId", map1.get("checkQuestionGroupId"));
			// 分别处理选择题和非选择题
			if (MonitorConstants.CHECKPAPER_QUESTIONTYPE_APFILL.equals(map1
					.get("questionType"))) {
				newQuestionList.add(map2);
			} else {
				for (Map.Entry<String, Object> en : map1.entrySet()) {
					String key = en.getKey();
					if (key.indexOf("option") > -1 && en.getValue() != null) {
						map2.put(key, en.getValue());
						// 将该选项对应的分值也放进map中
						String[] keyArr = key.split("");
						String optKey = "point" + keyArr[keyArr.length - 1];
						map2.put(optKey, map1.get(optKey));
					}
				}
				newQuestionList.add(map2);
			}
		}
//		for(int i = 0 ; i < newQuestionList.size() ; i++){
//			Map<String,Object> mapOfList = newQuestionList.get(i);
//			for(Map.Entry<String, Object> mp:mapOfList.entrySet()){
//				String value = ConvertUtil.getString(mp.getValue());
//				String newValue = CherryUtil.convertStrToJson(value);
//				mp.setValue(newValue);
//			}
//		}
		return newQuestionList;
	}

}
