/*  
 * @(#)BINOLPTODR01_BL.java    1.0 2011-8-9     
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

package com.cherry.st.sfh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.sfh.interfaces.BINOLSTSFH10_IF;
import com.cherry.st.sfh.service.BINOLSTSFH10_Service;
import com.cherry.synchro.st.interfaces.IssuedPrtOrdParSynchro_IF;

@SuppressWarnings("unchecked")
public class BINOLSTSFH10_BL implements BINOLSTSFH10_IF {

	@Resource(name="binOLSTSFH10_Service")
	private BINOLSTSFH10_Service binOLSTSFH10_Service;

	@Resource(name="issuedPrtOrdParSynchro")
	private IssuedPrtOrdParSynchro_IF issuedPrtOrdParSynchro;
	
	@Override
	public void tran_editCouPrtParameter(Map<String, Object> map) throws Exception {
		
		binOLSTSFH10_Service.editCouPrtParameter(map);
	}
	
	@Override
	public void tran_editCounterParameter(Map<String, Object> map) throws Exception {

		binOLSTSFH10_Service.editCounterParameter(map);
	}

	@Override
	public void tran_editProductParameter(Map<String, Object> map) throws Exception {

		binOLSTSFH10_Service.editProductParameter(map);
	}
	
	@Override
	public void tran_editGlobalParameter(Map<String, Object> map)
			throws Exception {
		binOLSTSFH10_Service.editGlobalParameter(map);
	}

	@Override
	public void tran_setCouPrtParameter(Map<String, Object> map)
			throws Exception {
		
			//申明三个list类型的变量分别用来存放柜台、产品以及插入信息
			List<Map<String,Object>> listOfCounter = (List<Map<String, Object>>) map.get("counterList");
			List<Map<String,Object>> listOfProduct = (List<Map<String, Object>>) map.get("productList");
			map.remove("counterList");
			map.remove("productList");
			List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
			if(listOfCounter.size() > 0 && listOfProduct.size() > 0){
				//遍历柜台和产品list，将这两个list中的map整合到paramList中用于插入操作
				for(int index = 0 ; index < listOfCounter.size() ; index ++){
					Map<String,Object> mapOfCounter = listOfCounter.get(index);
					for(int index1 = 0 ; index1 < listOfProduct.size() ; index1 ++){
						Map<String,Object> mapOfProduct = listOfProduct.get(index1);
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.putAll(mapOfProduct);
						paramMap.putAll(mapOfCounter);
						paramMap.putAll(map);
						paramList.add(paramMap);
					}
				}
				binOLSTSFH10_Service.deleteCouPrtParameter(paramList);
				//调用Service
				binOLSTSFH10_Service.setCouPrtParameter(paramList);
			}else{
				//抛出没有柜台或产品信息异常
				throw new CherryException("EPT00003");
			}
	}
	
	@Override
	public void tran_setGlobalParameter(Map<String, Object> map) throws Exception {
		// 插入前先根据品牌删除已有的全局订货参数表中的数据
		binOLSTSFH10_Service.deleteGlobalParameter(map);
		// 写入新增的全局订货参数数据
		binOLSTSFH10_Service.setGlobalParameter(map);
	}

	@Override
	public void tran_setCounterParameter(Map<String, Object> map,List<Map<String,Object>> list)
			throws Exception {
		
		//申明list变量用来存放所有的柜台信息
		List<Map<String,Object>> paramList = list;
		
		if(paramList.size() > 0){
			//遍历paramList
			for(int index = 0 ; index < paramList.size() ; index ++){
				Map<String,Object> mapOfList = paramList.get(index);
				mapOfList.putAll(map);
			}
			//在插入之前先将该品牌下的所有的柜台订货参数信息
			binOLSTSFH10_Service.deleteCounterParameter(paramList);
			//调用Service
			binOLSTSFH10_Service.setCounterParameter(paramList);
		}else{
			//抛出未查询出柜台信息异常
			throw new CherryException("EPT00002");
		}

	}

	@Override
	public void tran_setProductParameter(Map<String,Object> map,String date,List<Map<String,Object>> list)
			throws Exception {

		//申明list变量用来存放所有的产品信息
		List<Map<String,Object>> paramList = list;
		
		//将参数date分成“年”和“月”两个部分date的格式为YYYYmm
		String year = date.substring(0, 4);
		String month = date.substring(4);
		//将year和month放到map做为插入的参数
		map.put("year", year);
		map.put("month", month);
		
		if(paramList.size() > 0){
			//遍历paramList
			for(int index = 0 ; index < paramList.size() ; index ++){
				Map<String,Object> mapOfList = paramList.get(index);
				mapOfList.putAll(map);
			}
			//在插入之前先将该品牌下的所有的产品订货参数信息
			binOLSTSFH10_Service.deleteProductParameter(paramList);
			//调用Service
			binOLSTSFH10_Service.setProductParameter(paramList);
		}else{
			//抛出未查询出商品信息异常
			throw new CherryException("EPT00001");
		}
		
	}

	@Override
	public int getCouPrtParameterCount(Map<String, Object> map) {
		return binOLSTSFH10_Service.getCouPrtParameterCount(map);
	}

	@Override
	public List<Map<String, Object>> getCouPrtParameterList(
			Map<String, Object> map) {
		return binOLSTSFH10_Service.getCouPrtParameterList(map);
	}

	@Override
	public int getCounterParameterCount(Map<String, Object> map) {
		return binOLSTSFH10_Service.getCounterParameterCount(map);
	}

	@Override
	public List<Map<String, Object>> getCounterParameterList(
			Map<String, Object> map) {
		return binOLSTSFH10_Service.getCounterParameterList(map);
	}

	@Override
	public int getProductParameterCount(Map<String, Object> map,String date) {
		if(null != date && !"".equals(date)){
			//将参数date分成“年”和“月”两个部分date的格式为YYYYmm
			String year = date.substring(0, 4);
			String month = date.substring(4);
			//将year和month放到map做为插入的参数
			map.put("year", year);
			map.put("month", month);
		}
		return binOLSTSFH10_Service.getProductParameterCount(map);
	}

	@Override
	public List<Map<String, Object>> getProductParameterList(
			Map<String, Object> map,String date) {
		if(null != date && !"".equals(date)){
			//将参数date分成“年”和“月”两个部分date的格式为YYYYmm
			String year = date.substring(0, 4);
			String month = date.substring(4);
			//将year和month放到map做为插入的参数
			map.put("year", year);
			map.put("month", month);
		}
		
		return binOLSTSFH10_Service.getProductParameterList(map);
	}
	
	@Override
	public List<Map<String, Object>> getGlobalParameterList(
			Map<String, Object> map) {
		return binOLSTSFH10_Service.getGlobalParameterList(map);
	}

	@Override
	public int getGlobalParameterCount(Map<String, Object> map) {
		return binOLSTSFH10_Service.getGlobalParameterCount(map);
	}

	/**
	 * 下发订货参数
	 * 
	 * 
	 * */
	@Override
	public void issumOrderParam(Map<String, Object> map)
			throws Exception {
		// 下发参数
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		
		// 产品参数设定下发分批处理参数
		Map<String, Object> productBatchMap = new HashMap<String, Object>();
		productBatchMap.putAll(map);
		//产品参数设定数据默认不为空
		boolean productFlag = true;
		
		// 最低天数设定下发分批处理参数
		Map<String, Object> couPrtBatchMap = new HashMap<String, Object>();
		couPrtBatchMap.putAll(map);
		//最低天数设定数据默认不为空
		boolean couPrtFlag = true;
		
		String brandCode = binOLSTSFH10_Service.getBrandCode(map);
		paramMap.put("BrandCode",brandCode); 
		// 下发前将老数据清除
		issuedPrtOrdParSynchro.delPrtOrdParSynchro(paramMap);
		
		List<Map<String, Object>> nullList = new ArrayList<Map<String, Object>>();
		// 柜台参数设定数据【不进行】分页处理
		List<Map<String ,Object>> counterList = binOLSTSFH10_Service.getIssCouParamList(map);
		
		// 产品参数设定分页取数据进行下发
		productBatchMap.put("SORT_ID", "productOrderParameterId desc");
		// 最低库存设定分页取数据进行下发
		couPrtBatchMap.put("SORT_ID", "counterPrtOrParameterId desc");
		int start = 0;
		List<Map<String ,Object>> productList = null;
		List<Map<String ,Object>> couPrtList = null;
		while (true) {
			paramMap.clear();
			paramMap.put("BrandCode", brandCode);
			if(0 == start) {
				// 第一批处理时将柜台参数设定下发
				paramMap.put("counterParam", counterList);
			} else {
				paramMap.put("counterParam", nullList);
			}
			
			if(productFlag) {
				// 产品参数设定分页处理参数
				productBatchMap.put("START", start + 1);
				productBatchMap.put("END", start + CherryConstants.BATCH_PAGE_MAX_NUM);
				productList = binOLSTSFH10_Service.getIssPrtParamList(productBatchMap);
				paramMap.put("productParam", productList);
				if(null == productList || 0 == productList.size()) {
					productFlag = false;
				}
			} else {
				paramMap.put("productParam", nullList);
			}
			
			if(couPrtFlag) {
				// 最低库存设定分页处理参数
				couPrtBatchMap.put("START", start + 1);
				couPrtBatchMap.put("END", start + CherryConstants.BATCH_PAGE_MAX_NUM);
				couPrtList = binOLSTSFH10_Service.getIssCouPrtParamList(couPrtBatchMap);
				paramMap.put("couPrtParam", couPrtList);
				if(null == couPrtList || 0 == couPrtList.size()) {
					couPrtFlag = false;
				}
			} else {
				paramMap.put("couPrtParam", nullList);
			}
			
			if(!productFlag && !couPrtFlag) {
				// 产品参数设定与最低库存设定数据都已取完后跳出循环
				break;
			}
			// 调用下发接口下发数据
			issuedPrtOrdParSynchro.issPrtOrdParSynchro(paramMap);
			
			start += CherryConstants.BATCH_PAGE_MAX_NUM;
		}
		
	}

	/**
	 * 获取产品树形结构的所有节点
	 * 
	 * */
	@Override
	public List<Map<String,Object>> getProductTreeNodes(Map<String, Object> map) throws Exception {
		// 语言类型
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		//调用service中的方法获取产品类别树形显示顺序
		List<Map<String,Object>> showSeqList = binOLSTSFH10_Service.getCatPropListSeq(map);
		
		if(showSeqList.size()==2){
			int firstFloorId = (Integer) showSeqList.get(0).get("BIN_PrtCatPropertyID");
			int secondFloorId = (Integer)showSeqList.get(1).get("BIN_PrtCatPropertyID");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("firstFloorId", firstFloorId);
			paramMap.put("secondFloorId", secondFloorId);
			paramMap.putAll(map);
			//调用service中的方法获取所有的产品及其分类情况
			List<Map<String,Object>> resultList = binOLSTSFH10_Service.getPrtAndCatList(paramMap);
			
			List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = { "firstId", "firstName" };
			String[] keys2 = { "secondId", "secondName" };
			String[] keys3 = { "productVendorId", "productName" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			//将上面获取的list处理成支持树形结构的list
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
			// 添加一个全选节点
			return this.addNodeForCheckAll(resultTreeList, language);
		}else{
			throw new CherryException("EST00018");
		}
		
	}
	
	/**
	 * 获取柜台树节点
	 * 
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getCounterTreeNodes(Map<String, Object> map)
			throws Exception {
		// 语言类型
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		// 调用service获取所有的非柜台部门信息，按照path排序
		List<Map<String, Object>> list = binOLSTSFH10_Service.getCuntAndDeptList(map);
		List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
		// 将获取的list转换成树形结构需要的形式
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "regionId", "regionName" };
		String[] keys2 = { "provinceId", "provinceName" };
		String[] keys3 = { "cityId", "cityName" };
		String[] keys4 = { "counterId", "counterName"};
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys4);
		ConvertUtil.jsTreeDataDeepList(list, resultTreeList, keysList, 0);
		// 添加一个全选节点
		return this.addNodeForCheckAll(resultTreeList, language);
	}
	
	/**
	 * 往树结构中添加一个全选的节点用于全选所有节点
	 * @param list
	 * @param language
	 * @return
	 */
	private List<Map<String, Object>> addNodeForCheckAll(List<Map<String, Object>> list,String language) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 增加的全选节点
		Map<String, Object> addNode = new HashMap<String, Object>();
		addNode.put("id", "all");
		addNode.put("name",  CherryUtil.getResourceValue("", language, "global.page.selectAll"));
		addNode.put("nodes", list != null ? list : new ArrayList<Map<String, Object>>());
		resultList.add(addNode);
		return resultList;
	}

	/**
	 * 递归处理list，将不需要的key清除
	 * 
	 * @param list
	 * @return list
	 * 
	 * */
	public List<Map<String, Object>> dealJsDepartList(
			List<Map<String, Object>> list) {
		if (list.size() > 0) {
			for (int index = 0; index < list.size(); index++) {
				list.get(index).remove("path");
				list.get(index).remove("level");
				if (null != list.get(index).get("nodes")) {
					List<Map<String, Object>> childList = (List<Map<String, Object>>) list
							.get(index).get("nodes");
					dealJsDepartList(childList);
				}
			}
		}
		return list;
	}

}
