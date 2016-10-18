package com.cherry.mo.cio.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO09_IF;
import com.cherry.mo.cio.service.BINOLMOCIO06_Service;
import com.cherry.mo.cio.service.BINOLMOCIO09_Service;
import com.cherry.synchro.mo.interfaces.CheckPaperSynchro_IF;

public class BINOLMOCIO09_BL implements BINOLMOCIO09_IF {

	@Resource
	private BINOLMOCIO09_Service binOLMOCIO09_Service;
	@Resource
	private BINOLMOCIO06_Service binOLMOCIO06_Service;
	@Resource
	private CheckPaperSynchro_IF checkPaperSynchro;
	
	@Override
	public int getPaperCount(Map<String, Object> map) {
		return binOLMOCIO09_Service.getPaperCount(map);
	}

	@Override
	public List<Map<String, Object>> getPaperList(Map<String, Object> map) {
		return binOLMOCIO09_Service.getPaperList(map);
	}

	/**
	 * 根据问卷ID进行问卷的启用操作，包括问卷状态在新后台的更新以及往老后台下发操作
	 * @param map 存放的是问卷启用所需的信息，包括问卷ID以及操作人等等
	 * @exception 抛出自定义异常
	 * 
	 * */
	@Override
	public void tran_enableCheckPaper(Map<String, Object> map) throws Exception {
		Map<String, Object> publisher = binOLMOCIO06_Service.getPublisher(map);
		map.put("publisher", publisher.get("EmployeeName"));		//调用问卷下发的Serice中的获取发布人函数获取考核问卷下发发布人，并放到map中
		binOLMOCIO09_Service.enableCheckPaper(map);					//更新新后台数据库，将问卷状态更新为“2”
		String issuedStatus = ConvertUtil.getString(map.get("issuedStatus"));
		/*以下的操作是调用接口往老后台写数据*/
		Map<String,Object> checkPaperMap = binOLMOCIO09_Service.getCheckPaper(map);					//调用Serivce层的函数获取问卷主表信息
		if(issuedStatus.equals("0")){
			List<Map<String,Object>> GroupList = binOLMOCIO09_Service.getCheckPaperGroup(map);			//调用Serivce层的函数获取问卷分组信息
			List<Map<String,Object>> QuestionList = binOLMOCIO09_Service.getCheckPaperQuestion(map);	//调用Service层的函数获取问卷问题信息
			List<Map<String,Object>> PointLevelList = binOLMOCIO09_Service.getCheckPaperLevel(map);		//调用Service层的函数获取问卷评分标准
			List<Map<String,Object>> newQuestionList = new ArrayList<Map<String,Object>>();
			newQuestionList.addAll(QuestionList);														//将问卷问题list赋值给一个新的list，在下面的遍历中使用
			
			Map<String, Object> producer  = binOLMOCIO06_Service.getPublisher(checkPaperMap);
			checkPaperMap.put("Producer", producer.get("EmployeeName"));
			
			for(int i = 0 ; i < GroupList.size() ; i++){												//遍历分组list，在其中加入分组总分以及在问题list中加入分组显示顺序
				int TopScore = 0;																	//申明一个int类型的变量，用于存放每个分组的总分
				Map<String,Object> mapOfGroupList = GroupList.get(i);
				String QuestionGroupID = ConvertUtil.getString(mapOfGroupList.get("QuestionGroupID"));	//申明一个变量用来存放问题分组ID，用于后面的比较
				for(int j = 0 ; j < newQuestionList.size() ; j ++){
					Map<String,Object> mapOfnewQuestionList = newQuestionList.get(j);
					String groupId = ConvertUtil.getString(mapOfnewQuestionList.get("QuestionGroupID"));
					if(groupId.equals(QuestionGroupID)){												//将问题与分组对应起来
						String point = ConvertUtil.getString(mapOfnewQuestionList.get("MaxPoint"));
						double questionPoint = Double.parseDouble(point);
						TopScore = TopScore+(int)questionPoint;									//累加得到分组总分
						mapOfnewQuestionList.put("GroupDisplayOrder", mapOfGroupList.get("DisplayOrder"));//将组的显示顺序写到问题list中
						newQuestionList.remove(j);														//将操作过的问题list中map删除已达到减少下一次循环次数的目的
						j--;
					}
				}
				mapOfGroupList.put("TopScore", String.valueOf(TopScore));
			}
			
			checkPaperMap.put("PointLevelList", PointLevelList);
			checkPaperMap.put("GroupList", GroupList);
			checkPaperMap.put("QuestionList", QuestionList);
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1=new SimpleDateFormat("yyyyMMdd");
			String StartDate = ConvertUtil.getString(checkPaperMap.get("StartDate"));
			Date sd = sf.parse(StartDate);
			checkPaperMap.put("StartDate", sf1.format(sd));
			String EndDate = ConvertUtil.getString(checkPaperMap.get("EndDate"));
			Date ed = sf.parse(EndDate);
			checkPaperMap.put("EndDate", sf1.format(ed));
			
			/*调用接口往老后台写数据*/
			checkPaperSynchro.addCheckPaper(checkPaperMap);
		}else{
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1=new SimpleDateFormat("yyyyMMdd");
			String StartDate = ConvertUtil.getString(checkPaperMap.get("StartDate"));
			Date sd = sf.parse(StartDate);
			checkPaperMap.put("StartDate", sf1.format(sd));
			String EndDate = ConvertUtil.getString(checkPaperMap.get("EndDate"));
			Date ed = sf.parse(EndDate);
			checkPaperMap.put("EndDate", sf1.format(ed));
			/*调用接口往老后台写数据*/
			checkPaperSynchro.updCheckPaper(checkPaperMap);
		}
		
	}

	/**
	 * 根据问卷ID进行问卷的停用操作，包括问卷状态在新后台的更新以及往老后台下发操作
	 * @param map 存放的是问卷停用所需的信息，包括问卷ID以及操作人等等
	 * @exception 抛出自定义异常
	 * 
	 * */
	@Override
	public void tran_disableCheckPaper(Map<String, Object> map)
			throws Exception {
		/*更新新后台数据库，将问卷状态更新为“1”*/
		binOLMOCIO09_Service.disableCheckPaper(map);
		String issuedStatus = ConvertUtil.getString(map.get("issuedStatus"));
		if(issuedStatus.equals("1")){
			Map<String,Object> checkPaperMap = binOLMOCIO09_Service.getCheckPaper(map);
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1=new SimpleDateFormat("yyyyMMdd");
			String StartDate = ConvertUtil.getString(checkPaperMap.get("StartDate"));
			Date sd = sf.parse(StartDate);
			checkPaperMap.put("StartDate", sf1.format(sd));
			String EndDate = ConvertUtil.getString(checkPaperMap.get("EndDate"));
			Date ed = sf.parse(EndDate);
			checkPaperMap.put("EndDate", sf1.format(ed));
			/*调用接口往老后台写数据*/
			checkPaperSynchro.updCheckPaper(checkPaperMap);
		}
	}

	@Override
	public void tran_deleteCheckPaper(Map<String,Object> map,List<Map<String,Object>> list) throws Exception {
		for(int i = 0 ; i < list.size() ; i ++){
			Map<String,Object> mapOfList = list.get(i);
			for(Map.Entry<String, Object> en:map.entrySet()){
				mapOfList.put(en.getKey(), en.getValue());
			}
		}
		binOLMOCIO09_Service.deleteCheckPaper(list);
	}

}
