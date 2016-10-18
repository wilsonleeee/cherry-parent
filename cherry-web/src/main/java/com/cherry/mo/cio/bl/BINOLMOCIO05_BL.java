package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.mo.cio.interfaces.BINOLMOCIO05_IF;
import com.cherry.mo.cio.service.BINOLMOCIO03_Service;
import com.cherry.mo.cio.service.BINOLMOCIO05_Service;
import com.cherry.mo.common.MonitorConstants;
import com.googlecode.jsonplugin.JSONUtil;

public class BINOLMOCIO05_BL implements BINOLMOCIO05_IF {

	@Resource
	private BINOLMOCIO05_Service binOLMOCIO05_Service;
	@Resource
	private BINOLMOCIO03_Service binOLMOCIO03_Service;

	/**
	 * 根据问卷ID获取问卷主表信息
	 * 
	 * @param map
	 *            存放的是要获取的问卷的ID
	 * @return map 返回问卷的信息
	 * 
	 * */
	@Override
	public Map<String, Object> getPaperForEdit(Map<String, Object> map) {

		Map<String,Object> resultMap = binOLMOCIO05_Service.getPaperForEdit(map);
		this.splitDataTime(resultMap);
		return resultMap;
	}

	/**
	 * 根据问卷ID获取问卷问题信息
	 * 
	 * @param map
	 *            存放的是要获取的问题所属的问卷ID
	 * @return list 返回问题List
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getPaperQuestion(Map<String, Object> map) {
		List<Map<String, Object>> questionList = binOLMOCIO05_Service
				.getPaperQuestion(map);
		List<Map<String, Object>> queList = new ArrayList<Map<String, Object>>();
		// 清理查询出来的结果
		if (questionList.size() > 0) {
			for (int i = 0; i < questionList.size(); i++) {
				Map<String, Object> map1 = questionList.get(i);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("questionType", map1.get("questionType"));
				map2.put("questionItem", map1.get("questionItem"));
				map2.put("displayOrder", map1.get("displayOrder"));
				map2.put("point", map1.get("point"));
				// 是否必须回答字段
				map2.put("isRequired", map1.get("isRequired"));
				map2.put("paperId", map1.get("paperId"));
				// 分别处理选择题和非选择题
				if (MonitorConstants.QUESTIONTYPE_APFILL.equals(map1
						.get("questionType"))
						|| MonitorConstants.QUESTIONTYPE_ESSAY.equals(map1
								.get("questionType"))) {
					queList.add(map2);
				} else {
					for (Map.Entry<String, Object> en : map1.entrySet()) {
						String key = en.getKey();
						if (null != en.getValue() && key.indexOf("option") > -1) {
							map2.put(key, en.getValue());
							// 将该选项对应的分值也放进map中
							String[] keyArr = key.split("");
							String optKey = "point" + keyArr[keyArr.length - 1];
							map2.put(optKey, map1.get(optKey));
						}
					}
					queList.add(map2);
				}
			}
		}
		return queList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void tran_savePaper(Map<String, Object> map) throws Exception {
		Map<String, Object> paperMap = new HashMap<String, Object>();
		List<Map<String, Object>> questionList = null;
		String editFlag = map.get("editFlag").toString();
		this.setDateTime(map);
		for (Map.Entry<String, Object> en : map.entrySet()) {
			String key = en.getKey();
			Object value = en.getValue();
			if (key.equals("questionList")) {
				String value1 = (String) value;
				questionList = (List<Map<String, Object>>) JSONUtil
						.deserialize(value1);
			} else {
				paperMap.put(key, value);
			}
		}
		if("0".equals(editFlag)){
			for(int i = 0 ; i< questionList.size(); i++){
				Map<String,Object> mapOfList = questionList.get(i);
				mapOfList.put("createdBy", map.get("createdBy"));
				mapOfList.put("createPGM", "BINOLMOCIO05");
				mapOfList.put("updatedBy", map.get("updatedBy"));
				mapOfList.put("updatePGM", "BINOLMOCIO05");
				mapOfList.put("startTime", map.get("startTime"));
				mapOfList.put("endTime", map.get("endTime"));
			}
			try{
				int modifiedCount = binOLMOCIO05_Service.updatePaper(paperMap);
				if(modifiedCount == 0){
					throw new CherryException("EMO00029");
				}
				binOLMOCIO05_Service.deleteQuestion(paperMap);
				binOLMOCIO03_Service.saveQuestion(questionList);
			}catch(Exception ex){
				throw new CherryException("EMO00029", ex);
			}
		}else if("1".equals(editFlag)){
			try {
				// 将map中的问卷信息插入到数据库中，并且返回paperId，供问卷下发和往问卷明细表中插入数据时使用
				int paperId = binOLMOCIO03_Service.savePaper(map);
				for(int i = 0 ; i< questionList.size(); i++){
					Map<String,Object> mapOfList = questionList.get(i);
					//复制问卷的id
					mapOfList.put("paperId", paperId);
					mapOfList.put("createdBy", map.get("createdBy"));
					mapOfList.put("createPGM", "BINOLMOCIO05");
					mapOfList.put("updatedBy", map.get("updatedBy"));
					mapOfList.put("updatePGM", "BINOLMOCIO05");
					mapOfList.put("startTime", map.get("startTime"));
					mapOfList.put("endTime", map.get("endTime"));
				}
				// 将问题插入问卷明细表中
				binOLMOCIO03_Service.saveQuestion(questionList);
			} catch (Exception ex) {
				throw new CherryException("EMO00029", ex);
			}
		}
	}

	@Override
	public Map<String, Object> getPaperForShow(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> resultList = binOLMOCIO05_Service.getPaperForShow(map);
		Map<String,Object> resultMap = resultList.get(0);
		
		if(null == resultMap.get("maxPoint") || ("").equals(resultMap.get("maxPoint"))){
			resultMap.put("maxPoint", "0.00");
		}
		
		return resultMap;
	}

	@Override
	public boolean isExsitSameNamePaper(Map<String, Object> map) {
		
		List<Map<String,Object>> papers = binOLMOCIO03_Service.isExsitSameNamePaper(map);
		
		if(!papers.isEmpty()){
			if(papers.size() > 1){
				return true;
			}else if(String.valueOf(papers.get(0).get("BIN_PaperID")).equals(map.get("paperId"))){
				if("1".equals(map.get("editFlag"))){//复制问卷的情况
					return true;
				} else {//编辑问卷的情况
					return false;
				}
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	//分割时间日期
	private Map<String,Object> splitDataTime(Map<String,Object> map){
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		
		String[] sArr = startTime.split("\\s");
		String[] eArr = endTime.split("\\s");
		
		map.put("startDate", sArr[0]);
		map.put("startHour", sArr[1].split(":")[0]);
		map.put("startMinute", sArr[1].split(":")[1]);
		map.put("startSecond", sArr[1].split(":")[2].split("\\.")[0]);
		map.put("endDate", eArr[0]);
		map.put("endHour", eArr[1].split(":")[0]);
		map.put("endMinute", eArr[1].split(":")[1]);
		map.put("endSecond", eArr[1].split(":")[2].split("\\.")[0]);
		
		return map;
	}

	//设定时间(拼接时间)
	private Map<String,Object> setDateTime(Map<String,Object> map){
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");
		
		if("".equals(map.get("startHour"))){
			startTime = startTime + " 00";
		}else{
			startTime = startTime + " " + map.get("startHour");
		}
		
		if("".equals(map.get("startMinute"))){
			startTime = startTime + ":00";
		}else{
			startTime = startTime + ":" + map.get("startMinute");
		}
		
		if("".equals(map.get("startSecond"))){
			startTime = startTime + ":00";
		}else{
			startTime = startTime + ":" + map.get("startSecond");
		}
		
		if("".equals(map.get("endHour"))){
			endTime = endTime + " 23";
		}else{
			endTime = endTime + " " + map.get("endHour");
		}
		
		if("".equals(map.get("endMinute"))){
			endTime = endTime + ":59";
		}else{
			endTime = endTime + ":" + map.get("endMinute");
		}
		
		if("".equals(map.get("endSecond"))){
			endTime = endTime + ":59.997";
		}else{
			endTime = endTime + ":" + map.get("endSecond") +".997";
		}
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		return map;
	}
}
