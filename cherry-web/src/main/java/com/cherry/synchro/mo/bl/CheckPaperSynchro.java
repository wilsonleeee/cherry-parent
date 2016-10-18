/*  
 * @(#)CheckPaperSynchro.java     1.0 2011/06/01      
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
package com.cherry.synchro.mo.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.synchro.mo.interfaces.CheckPaperSynchro_IF;
import com.cherry.synchro.mo.service.CheckPaperSynchroService;
/**
 * 考核问卷同步
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class CheckPaperSynchro implements CheckPaperSynchro_IF {

	@Resource
	private CheckPaperSynchroService checkPaperSynchroService;
	
	@Override
	public void addCheckPaper(Map param) throws CherryException {
		try {
			param.put("PaperXml",this.getPaperXmlDom4j(param));			
			param.put("Result","OK");
			checkPaperSynchroService.addCheckPaper(param);
			String ret = String.valueOf(param.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
	}
	@Override
	public void updCheckPaper(Map param)throws CherryException {
		try {
			param.put("Result","OK");
			checkPaperSynchroService.updCheckPaper(param);
			String ret = String.valueOf(param.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}

	@Override
	public void delCheckPaper(Map param)throws CherryException {		
		try {
			param.put("Result","OK");
			checkPaperSynchroService.delCheckPaper(param);
			String ret = String.valueOf(param.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	private String getPaperXmlDom4j(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element rootElement = doc.addElement("CheckPaper");	
			
			//问卷主要信息节点
			Element mainInfo = rootElement.addElement("MainInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			//问卷ID 
			mainInfo.addAttribute("CheckPaperID", String.valueOf(param.get("CheckPaperID")));
			//问卷名称
			mainInfo.addAttribute("CheckPaperName", String.valueOf(param.get("CheckPaperName")));
			//权限
			mainInfo.addAttribute("PaperRight", String.valueOf(param.get("PaperRight")));
			//问卷状态
			mainInfo.addAttribute("PaperStatus", String.valueOf(param.get("PaperStatus")));
			//问卷起始时间
			mainInfo.addAttribute("StartDate", String.valueOf(param.get("StartDate")));
			//问卷截止时间
			mainInfo.addAttribute("EndDate", String.valueOf(param.get("EndDate")));
			//问卷制作人
			//mainInfo.addAttribute("Producer", String.valueOf(param.get("Producer")));
			mainInfo.addAttribute("Producer", "test");
			
			//问卷问题
			ArrayList<HashMap<String,String>> questionMapList =  (ArrayList<HashMap<String,String>>)param.get("QuestionList");
			if(questionMapList!=null && questionMapList.size()>0){
				Iterator<HashMap<String,String>> it = questionMapList.iterator();
				HashMap<String,String> tempMap;
				Element question ;
				while(it.hasNext()){
					 tempMap = it.next();
					 question = rootElement.addElement("Question");
					 question.addAttribute("CheckQuestionID",String.valueOf(tempMap.get("CheckQuestionID")));
					 question.addAttribute("CheckPaperID",String.valueOf(tempMap.get("CheckPaperID")));
					 question.addAttribute("QuestionGroupID",String.valueOf(tempMap.get("QuestionGroupID")));
					 question.addAttribute("DisplayOrderAll",String.valueOf(CherryUtil.string2int(String.valueOf(tempMap.get("GroupDisplayOrder")))*10000+CherryUtil.string2int(String.valueOf(tempMap.get("QuestionDisplayOrder")))));
					 question.addAttribute("DisplayOrder",String.valueOf(tempMap.get("QuestionDisplayOrder")));
					 question.addAttribute("QuestionType",String.valueOf(tempMap.get("QuestionType")));
					 question.addAttribute("QuestionItem",String.valueOf(tempMap.get("QuestionItem")));
					 question.addAttribute("MaxPoint",String.valueOf(tempMap.get("MaxPoint")));
					 question.addAttribute("MinPoint",String.valueOf(tempMap.get("MinPoint")));
					 question.addAttribute("QuestionNo",String.valueOf(tempMap.get("QuestionNO")));
					 question.addAttribute("OptionA",String.valueOf(tempMap.get("OptionA")));
					 question.addAttribute("OptionB",String.valueOf(tempMap.get("OptionB")));
					 question.addAttribute("OptionC",String.valueOf(tempMap.get("OptionC")));
					 question.addAttribute("OptionD",String.valueOf(tempMap.get("OptionD")));
					 question.addAttribute("OptionE",String.valueOf(tempMap.get("OptionE")));
					 question.addAttribute("OptionF",String.valueOf(tempMap.get("OptionF")));
					 question.addAttribute("OptionG",String.valueOf(tempMap.get("OptionG")));
					 question.addAttribute("OptionH",String.valueOf(tempMap.get("OptionH")));
					 question.addAttribute("OptionI",String.valueOf(tempMap.get("OptionI")));
					 question.addAttribute("OptionJ",String.valueOf(tempMap.get("OptionJ")));
					 question.addAttribute("OptionK",String.valueOf(tempMap.get("OptionK")));
					 question.addAttribute("OptionL",String.valueOf(tempMap.get("OptionL")));
					 question.addAttribute("OptionM",String.valueOf(tempMap.get("OptionM")));
					 question.addAttribute("OptionN",String.valueOf(tempMap.get("OptionN")));
					 question.addAttribute("OptionO",String.valueOf(tempMap.get("OptionO")));
					 question.addAttribute("OptionP",String.valueOf(tempMap.get("OptionP")));
					 question.addAttribute("OptionQ",String.valueOf(tempMap.get("OptionQ")));
					 question.addAttribute("OptionR",String.valueOf(tempMap.get("OptionR")));
					 question.addAttribute("OptionS",String.valueOf(tempMap.get("OptionS")));
					 question.addAttribute("OptionT",String.valueOf(tempMap.get("OptionT")));
					 question.addAttribute("PointA",String.valueOf(tempMap.get("PointA")));
					 question.addAttribute("PointB",String.valueOf(tempMap.get("PointB")));
					 question.addAttribute("PointC",String.valueOf(tempMap.get("PointC")));
					 question.addAttribute("PointD",String.valueOf(tempMap.get("PointD")));
					 question.addAttribute("PointE",String.valueOf(tempMap.get("PointE")));
					 question.addAttribute("PointF",String.valueOf(tempMap.get("PointF")));
					 question.addAttribute("PointG",String.valueOf(tempMap.get("PointG")));
					 question.addAttribute("PointH",String.valueOf(tempMap.get("PointH")));
					 question.addAttribute("PointI",String.valueOf(tempMap.get("PointI")));
					 question.addAttribute("PointJ",String.valueOf(tempMap.get("PointJ")));
					 question.addAttribute("PointK",String.valueOf(tempMap.get("PointK")));
					 question.addAttribute("PointL",String.valueOf(tempMap.get("PointL")));
					 question.addAttribute("PointM",String.valueOf(tempMap.get("PointM")));
					 question.addAttribute("PointN",String.valueOf(tempMap.get("PointN")));
					 question.addAttribute("PointO",String.valueOf(tempMap.get("PointO")));
					 question.addAttribute("PointP",String.valueOf(tempMap.get("PointP")));
					 question.addAttribute("PointQ",String.valueOf(tempMap.get("PointQ")));
					 question.addAttribute("PointR",String.valueOf(tempMap.get("PointR")));
					 question.addAttribute("PointS",String.valueOf(tempMap.get("PointS")));
					 question.addAttribute("PointT",String.valueOf(tempMap.get("PointT")));
				}
			}
			//问卷分组
			ArrayList<HashMap<String,String>> groupList =  (ArrayList<HashMap<String,String>>)param.get("GroupList");
			if(groupList!=null && groupList.size()>0){
				Iterator<HashMap<String,String>> it = groupList.iterator();
				HashMap<String,String> tempMap;
				Element group ;
				while(it.hasNext()){
					 tempMap = it.next();
					 group = rootElement.addElement("Group");
					 group.addAttribute("CheckPaperID",String.valueOf(tempMap.get("CheckPaperID")));
					 group.addAttribute("QuestionGroupID",String.valueOf(tempMap.get("QuestionGroupID")));
					 group.addAttribute("GroupName",String.valueOf(tempMap.get("GroupName")));
					 group.addAttribute("DisplayOrder",String.valueOf(tempMap.get("DisplayOrder")));
					 group.addAttribute("TopScore",String.valueOf(tempMap.get("TopScore")));
				}
			}
			
			//问卷评分级别
			ArrayList<HashMap<String,String>> levelList =  (ArrayList<HashMap<String,String>>)param.get("PointLevelList");
			if(levelList!=null && levelList.size()>0){
				Iterator<HashMap<String,String>> it = levelList.iterator();
				HashMap<String,String> tempMap;
				Element level ;
				while(it.hasNext()){
					 tempMap = it.next();
					 level = rootElement.addElement("PointLevel");
					 level.addAttribute("CheckPaperID",String.valueOf(tempMap.get("CheckPaperID")));
					 level.addAttribute("PointLevelOrder",String.valueOf(tempMap.get("PointLevelOrder")));
					 level.addAttribute("PointLevelName",String.valueOf(tempMap.get("PointLevelName")));
					 level.addAttribute("Point",String.valueOf(tempMap.get("Point")));
				}
			}
			ret = doc.asXML();
			
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
}
