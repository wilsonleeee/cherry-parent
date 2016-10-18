/*  
 * @(#)PaperSynchro.java     1.0 2011/05/31      
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.synchro.mo.interfaces.PaperSynchro_IF;
import com.cherry.synchro.mo.service.PaperSynchroService;
/**
 * 问卷信息同步
 * 禁止柜台信息同步
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class PaperSynchro implements PaperSynchro_IF {

	@Resource
	private PaperSynchroService paperSynchroService;
	
	@Override
	public void addPaper(Map param) throws CherryException {
		try {
			param.put("PaperXml",this.getPaperXml(param));
			param.put("Result","OK");
			paperSynchroService.addPaper(param);
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
	public void updPaper(Map param)throws CherryException {		
		try {			
			param.put("Result","OK");
			paperSynchroService.updPaper(param);
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
	public void delPaper(Map param)throws CherryException {		
		try {
			paperSynchroService.delPaper(param);
			String ret = String.valueOf(param.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	@Override
	public void synchroForbiddenCounter(Map param)throws CherryException {
		try {
			List<String> list = (List<String>)param.get("CounterCodeList");
			
			String brandCode = String.valueOf(param.get("BrandCode"));
			String paperID = String.valueOf(param.get("PaperID"));
			
			
			param.put("CounterXml",getCounterXml(brandCode,paperID,list));
			param.put("Result","OK");
			paperSynchroService.synchroForbiddenCounter(param);
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
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getPaperXml(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("Paper");
			
			//问卷主要信息节点
			Element mainInfo = nodeRoot.addElement("MainInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			//问卷ID 
			mainInfo.addAttribute("PaperID", String.valueOf(param.get("PaperID")));
			//问卷名称
			mainInfo.addAttribute("PaperName", String.valueOf(param.get("PaperName")));
			//发布人
			mainInfo.addAttribute("Publisher", String.valueOf(param.get("Publisher")));
			//问卷的总分值
			mainInfo.addAttribute("TotalPoint", String.valueOf(param.get("TotalPoint")));
			//问卷的状态
			mainInfo.addAttribute("Enable", String.valueOf(param.get("Enable")));
			//问卷的类型
			mainInfo.addAttribute("PaperType", String.valueOf(param.get("PaperType")));
			
			ArrayList<HashMap<String,String>> questionMapList =  (ArrayList<HashMap<String,String>>)param.get("QuestionList");
			if(questionMapList!=null && questionMapList.size()>0){
				Iterator<HashMap<String,String>> it = questionMapList.iterator();
				HashMap<String,String> tempMap;
				Element question ;
				// 问题是否必须回答字段[默认为非必须回答]
				String isRequired = "0";
				while(it.hasNext()){
					 tempMap = it.next();
					 question = nodeRoot.addElement("Question");
					 question.addAttribute("PaperID",String.valueOf(tempMap.get("PaperID")));
					 question.addAttribute("QuestionNo",String.valueOf(tempMap.get("QuestionNo")));
					 question.addAttribute("QuestionType",String.valueOf(tempMap.get("QuestionType")));
					 question.addAttribute("QuestionItem",String.valueOf(tempMap.get("QuestionItem")));
					 // 问题是否必须回答字段
					 if(CherryChecker.isNullOrEmpty(tempMap.get("IsRequired"))){
						 isRequired = "0";
					 } else {
						 isRequired = String.valueOf(tempMap.get("IsRequired"));
					 }
					 question.addAttribute("IsRequired",isRequired);
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
					 question.addAttribute("StartTime",String.valueOf(tempMap.get("StartTime")));
					 question.addAttribute("EndTime",String.valueOf(tempMap.get("EndTime")));
					 question.addAttribute("DisplayOrder",String.valueOf(tempMap.get("DisplayOrder")));
					 question.addAttribute("Point",String.valueOf(tempMap.get("Point")));
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
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}

	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getCounterXml(String brandCode,String paperID,List<String> list) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("CounterForbidden");
			
			//主要信息节点
			Element mainInfo = nodeRoot.addElement("MainInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", brandCode);
			//问卷ID 
			mainInfo.addAttribute("PaperID", paperID);	
			
			if(list!=null && list.size()>0){
				Iterator<String> it = list.iterator();
				Element counter ;
				String tempStr;
				while(it.hasNext()){
					tempStr = it.next();
					 counter = nodeRoot.addElement("Counter");
					 counter.addAttribute("PaperID",paperID);
					 counter.addAttribute("CounterCode",tempStr);
				}
			}
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
}
