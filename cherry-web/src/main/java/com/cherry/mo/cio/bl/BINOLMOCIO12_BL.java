package com.cherry.mo.cio.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO12_IF;
import com.cherry.mo.cio.service.BINOLMOCIO10_Service;
import com.cherry.mo.cio.service.BINOLMOCIO11_Service;
import com.cherry.mo.cio.service.BINOLMOCIO12_Service;

public class BINOLMOCIO12_BL implements BINOLMOCIO12_IF {

	@Resource
	private BINOLMOCIO12_Service binOLMOCIO12_Service;
	@Resource
	private BINOLMOCIO10_Service binOLMOCIO10_Service;
	@Resource
	private BINOLMOCIO11_Service binOLMOCIO11_Service;
	
	@Override
	public void tran_updateCheckPaper(Map<String, Object> map,
			List<Map<String, Object>> list1, List<Map<String, Object>> list2,
			List<Map<String, Object>> list3) throws Exception {
		try{
			
			Map<String,Object> mapOfId = new HashMap<String,Object>();      //申明一个新的Map用于存放编辑的考核问卷的问卷ID，作为删除操作的参数
			
			/*申明一系列的变量并给其赋值，用以下面的引用操作*/
			String paperId = ConvertUtil.getString(map.get("paperId"));
			String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
			String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
			String createdBy = ConvertUtil.getString(map.get("createdBy"));
			String createPGM = ConvertUtil.getString(map.get("createPGM"));
			String updatedBy = ConvertUtil.getString(map.get("updatedBy"));
			String updatePGM = ConvertUtil.getString(map.get("updatePGM"));
			
			binOLMOCIO12_Service.updateCheckPaper(map);						//调用service中的方法进行问卷更新操作
			mapOfId.put("paperId", paperId);								
			
			/*调用service中方法将要更新的考核问卷的分组进行物理删除，然后再调用问卷添加的service中方法进行插入*/
			binOLMOCIO12_Service.deleteCheckPaperGroup(mapOfId);
			for(int i = 0; i<list1.size(); i++){							//遍历存放问卷分组的list并且在其中加入一些必要的key和value用以问卷分组插入操作
				Map<String,Object> mapOfLt1 = list1.get(i);
				mapOfLt1.put("paperId", paperId);
				mapOfLt1.put("organizationInfoId", organizationInfoId);
				mapOfLt1.put("brandInfoId", brandInfoId);
				mapOfLt1.put("createdBy", createdBy);
				mapOfLt1.put("createPGM", createPGM);
				mapOfLt1.put("updatedBy", updatedBy);
				mapOfLt1.put("updatePGM", updatePGM);
				int groupId = binOLMOCIO10_Service.insertNewGroup(mapOfLt1);	//调用问卷添加的service层的方法进行问卷分组插入操作并且返回新增问卷分组的ID
				mapOfLt1.put("groupId", groupId);							//将上一步返回的ID放入list2中，用以下面的问题插入操作
			}
			
			/*调用service中方法将要更新的考核问卷中问题进行物理删除，然后再调用问卷添加的service中方法进行插入*/
			binOLMOCIO12_Service.deleteCheckQuestion(mapOfId);
			int questionNo = 1;
			for(int j = 0 ; j < list2.size() ; j++){						//遍历问题list并且在其中加入一些必要的key和value用以问题的插入操作
				Map<String,Object> mapOfLt2 = list2.get(j);
				mapOfLt2.put("paperId", paperId);
				mapOfLt2.put("createdBy", createdBy);
				mapOfLt2.put("createPGM", createPGM);
				mapOfLt2.put("updatedBy", updatedBy);
				mapOfLt2.put("updatePGM", updatePGM);
				mapOfLt2.put("questionNo", questionNo);
				String groupId = ConvertUtil.getString(mapOfLt2.get("checkQuestionGroupId"));
				for(int k = 0 ; k < list1.size() ; k++){
					Map<String,Object> map1 = list1.get(k);
					if(ConvertUtil.getString(map1.get("index")).equals(groupId)){//将上面返回的新增的分组ID同问题对应起来，然后才能进行插入操作
						mapOfLt2.put("checkQuestionGroupId", map1.get("groupId"));
						break;
					}
				}
				questionNo++;
			}
			binOLMOCIO10_Service.insertNewQuestion(list2);
			
			/*调用service中方法将要更新的考核问卷的评分水平进行物理删除，然后再调用问卷添加的service中方法进行插入*/
			binOLMOCIO12_Service.deleteCheckPaperLevel(mapOfId);
			for(int n = 0 ; n < list3.size() ; n++){
				Map<String,Object> mapOfLt3 = list3.get(n);
				mapOfLt3.put("paperId", paperId);
				mapOfLt3.put("createdBy", createdBy);
				mapOfLt3.put("createPGM", createPGM);
				mapOfLt3.put("updatedBy", updatedBy);
				mapOfLt3.put("updatePGM", updatePGM);
			}
			binOLMOCIO10_Service.insertCheckPaperLevel(list3);
		}catch(Exception e){
			throw new CherryException("EMO00029", e);			    //抛出自定义异常
		}
	}

	@Override
	public List<Map<String, Object>> getCheckPaperLevel(Map<String, Object> map) {
		return binOLMOCIO12_Service.getCheckPaperLevel(map);
	}

	@Override
	public List<Map<String, Object>> getCheckPaperGroup(Map<String, Object> map) {
		List<Map<String,Object>> list = binOLMOCIO11_Service.getCheckPaperGroup(map);
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> mapOfList = list.get(i);
			String index = ConvertUtil.getString(mapOfList.get("checkQuestionGroupId"));
			mapOfList.put("index", index);
			mapOfList.remove("checkQuestionGroupId");
//			for(Map.Entry<String, Object> mp:mapOfList.entrySet()){
//				String value = ConvertUtil.getString(mp.getValue());
//				String newValue = CherryUtil.convertStrToJson(value);
//				mp.setValue(newValue);
//			}
		}
		return list;
	}
	
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
	public boolean isExsitSameNamePaper(Map<String, Object> map) {
		
		List<Map<String,Object>> papers = binOLMOCIO10_Service.isExsitSameNamePaper(map);
		
		if(!papers.isEmpty()){
			if(papers.size() > 1){
				return true;
			}else if(String.valueOf(papers.get(0).get("BIN_CheckPaperID")).equals(map.get("paperId"))){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

}
