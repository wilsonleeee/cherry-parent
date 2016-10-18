
/*  
 * @(#)IssuedPrtOrdParSynchro.java    1.0 2011-8-12     
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

package com.cherry.synchro.st.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.st.interfaces.IssuedPrtOrdParSynchro_IF;
import com.cherry.synchro.st.service.IssuedPrtOrdParSynchroService;

@SuppressWarnings("unchecked")
public class IssuedPrtOrdParSynchro implements IssuedPrtOrdParSynchro_IF {

	@Resource
	private IssuedPrtOrdParSynchroService issuedPrtOrdParSynchroService;
	
	@Override
	public void issPrtOrdParSynchro(Map map) throws Exception {
		try{
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("ParamXml", this.getXml(map));
			paramMap.put("Result", "OK");
			issuedPrtOrdParSynchroService.issuedPrtOrdParSynchro(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	public String getXml(Map map) throws Exception{
		String ret = "";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("ParamInfo");
			
			//品牌
			Element brandCode = nodeRoot.addElement("BrandCode");
			brandCode.addAttribute("brandCode", ConvertUtil.getString(map.get("BrandCode")));
			
			//产品订货参数
			Element productParam;
			
			List<Map<String,Object>> productParamList = (List<Map<String, Object>>) map.get("productParam");
			if(productParamList.size() > 0){
				for(int index = 0 ; index < productParamList.size() ; index ++){
					Map<String,Object> mapOfProductParam = productParamList.get(index);
					productParam = nodeRoot.addElement("ProductParam");
					String year = ConvertUtil.getString(mapOfProductParam.get("Year"));
					String _year = year.substring(2);
					productParam.addAttribute("year", _year);
					productParam.addAttribute("month", ConvertUtil.getString(mapOfProductParam.get("Month")));
					productParam.addAttribute("barcode", ConvertUtil.getString(mapOfProductParam.get("BarCode")));
					productParam.addAttribute("unitcode", ConvertUtil.getString(mapOfProductParam.get("UnitCode")));
					productParam.addAttribute("mtsl_modify_parameter", ConvertUtil.getString(mapOfProductParam.get("AdtCoefficient")));
				}
			}
			
			//柜台订货参数
			Element counterParam;
			
			List<Map<String,Object>> counterParamList = (List<Map<String, Object>>) map.get("counterParam");
			if(counterParamList.size() > 0){
				for(int index = 0 ; index < counterParamList.size() ; index ++){
					Map<String,Object> mapOfCounterParam = counterParamList.get(index);
					counterParam = nodeRoot.addElement("CounterParam");
					counterParam.addAttribute("counter_code", ConvertUtil.getString(mapOfCounterParam.get("DepartCode")));
					counterParam.addAttribute("next_order_days", ConvertUtil.getString(mapOfCounterParam.get("OrderDays")));
					counterParam.addAttribute("on_the_way_days", ConvertUtil.getString(mapOfCounterParam.get("IntransitDays")));
				}
			}
			
			//最低库存天数
			Element couPrtParam;
			
			List<Map<String,Object>> couPrtParamList = (List<Map<String, Object>>) map.get("couPrtParam");
			if(couPrtParamList.size() > 0){
				for(int index = 0 ;index < couPrtParamList.size() ; index ++){
					Map<String,Object> mapOfCouPrtParam = couPrtParamList.get(index);
					couPrtParam = nodeRoot.addElement("CouPrtParam");
					couPrtParam.addAttribute("counter_code", ConvertUtil.getString(mapOfCouPrtParam.get("DepartCode")));
					couPrtParam.addAttribute("barcode", ConvertUtil.getString(mapOfCouPrtParam.get("BarCode")));
					couPrtParam.addAttribute("unitcode", ConvertUtil.getString(mapOfCouPrtParam.get("UnitCode")));
					couPrtParam.addAttribute("lowest_stock_days", ConvertUtil.getString(mapOfCouPrtParam.get("LowestStockDays")));
				}
			}
			ret = doc.asXML();
			
		}catch(Exception e){
			throw e;
		}
		return ret;
	}

	@Override
	public void delPrtOrdParSynchro(Map map) throws Exception {
		try{
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("ParamXml", this.getDelXml(map));
			paramMap.put("Result", "OK");
			issuedPrtOrdParSynchroService.delPrtOrdParSynchro(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
		
	}

	private Object getDelXml(Map map) throws Exception{
		String ret = "";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("ParamInfo");
			
			//品牌
			Element brandCode = nodeRoot.addElement("BrandCode");
			brandCode.addAttribute("brandCode", ConvertUtil.getString(map.get("BrandCode")));
			
			ret = doc.asXML();
			
		}catch(Exception e){
			throw e;
		}
		return ret;
	}

}
