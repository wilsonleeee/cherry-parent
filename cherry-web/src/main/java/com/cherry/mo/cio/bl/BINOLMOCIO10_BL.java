package com.cherry.mo.cio.bl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO10_IF;
import com.cherry.mo.cio.service.BINOLMOCIO10_Service;

public class BINOLMOCIO10_BL implements BINOLMOCIO10_IF {

	@Resource
	private BINOLMOCIO10_Service binOLMOCIO10_Service;
	
	
	/**
	 * 新增考核问卷
	 * @param map 存放的是操作员以及问卷的一些基本信息，包括所属组织，所属品牌，问卷名称等等
	 * @param list1 存放的是问卷分组信息
	 * @param list2 存放的是问卷问题的信息
	 * @param list3 存放的是问卷评分标准
	 * @throws Exception 抛出异常
	 * 
	 * */
	@Override
	public void tran_addNewCheckPaper(Map<String, Object> map,
			List<Map<String, Object>> list1, List<Map<String, Object>> list2,List<Map<String,Object>> list3)
			throws Exception {
		try{
			int paperId = binOLMOCIO10_Service.insertNewCheckPaper(map);		//调用Service中的函数向考核问卷主表插入新的问卷并且返回ID
			if(list1.size() > 0){												//先判断是否是分组试卷
				for(Iterator<Map<String, Object>> lt1 = list1.iterator();lt1.hasNext();){ //遍历整个list，在每个map中放入插入问题分组表中所需的字段
					 Map<String,Object> mapOfLt1 = lt1.next();
					 mapOfLt1.put("paperId", paperId);
					 mapOfLt1.put("organizationInfoId", map.get("organizationInfoId"));
					 mapOfLt1.put("brandInfoId", map.get("brandInfoId"));
					 mapOfLt1.put("createdBy", map.get("createdBy"));
					 mapOfLt1.put("createPGM", map.get("createPGM"));
					 mapOfLt1.put("updatedBy", map.get("updatedBy"));
					 mapOfLt1.put("updatePGM", map.get("updatePGM"));
					 int groupId = binOLMOCIO10_Service.insertNewGroup(mapOfLt1);     //调用Service中的函数向问题分组表中插入新增的分组并且返回ID
					 mapOfLt1.put("checkQuestionGroupId", groupId);
				}
				int questionNo = 1;
				for(Iterator<Map<String, Object>> lt2 = list2.iterator();lt2.hasNext();){
					Map<String,Object> mapOfLt2 = lt2.next();
					mapOfLt2.put("paperId", paperId);
					mapOfLt2.put("questionNo", questionNo);
					mapOfLt2.put("createdBy", map.get("createdBy"));
					mapOfLt2.put("createPGM", map.get("createPGM"));
					mapOfLt2.put("updatedBy", map.get("updatedBy"));
					mapOfLt2.put("updatePGM", map.get("updatePGM"));
					/*
					 * 遍历list1，将问题分组与问题相对应。PS：list2中的每个map都有一个“checkQuestionGroupId”字段，
					 * 由于在分组信息插入到数据库之前，系统并不知道每个分组对应的ID，但是分组的显示顺序是可以唯一确定每个分组的
					 * 所以"checkQuestionGroupId"字段对应的value值就是每个分组的index，因此现在只要把这两个对应起来然后再将
					 * list1中的"checkQuestionGroupId"赋值给list2就可以了。
					 * 
					 * */
					String groupId = ConvertUtil.getString(mapOfLt2.get("checkQuestionGroupId"));
					for(Iterator<Map<String, Object>> lt1 = list1.iterator();lt1.hasNext();){
						Map<String,Object> map1 = lt1.next();
						if(ConvertUtil.getString(map1.get("index")).equals(groupId)){
							mapOfLt2.put("checkQuestionGroupId", map1.get("checkQuestionGroupId"));
							break;
						}
					}
					questionNo++;
				}
			}else{																//如果不是分组试卷
				for(Iterator<Map<String, Object>> lt2 = list2.iterator();lt2.hasNext();){
					Map<String,Object> mapOfLt2 = lt2.next();
					mapOfLt2.put("paperId", paperId);
					mapOfLt2.put("createdBy", map.get("createdBy"));
					mapOfLt2.put("createPGM", map.get("createPGM"));
					mapOfLt2.put("updatedBy", map.get("updatedBy"));
					mapOfLt2.put("updatePGM", map.get("updatePGM"));
				}
			}
			if(list2.size() > 0){
				binOLMOCIO10_Service.insertNewQuestion(list2);			//调用Service中的函数将问题添加到问题明细表中
			}
			if(list3.size() > 0){
				for(int i = 0 ; i < list3.size() ; i++){
					Map<String,Object> mapOfLt3 = list3.get(i);
					mapOfLt3.put("paperId", paperId);
					mapOfLt3.put("createdBy", map.get("createdBy"));
					mapOfLt3.put("createPGM", map.get("createPGM"));
					mapOfLt3.put("updatedBy", map.get("updatedBy"));
					mapOfLt3.put("updatePGM", map.get("updatePGM"));
				}
				binOLMOCIO10_Service.insertCheckPaperLevel(list3);     //调用Service中的函数将考核问卷评分标准加入到相应的表中
			}
		}catch(Exception ex){
			throw new CherryException("EMO00029", ex);			    //抛出自定义异常
		}
	}


	@Override
	public boolean isExsitSameNamePaper(Map<String, Object> map) {
		List<Map<String,Object>> papers = binOLMOCIO10_Service.isExsitSameNamePaper(map);
		
		if(papers.isEmpty()){
			return false;
		}else{
			return true;
		}
	}


}
