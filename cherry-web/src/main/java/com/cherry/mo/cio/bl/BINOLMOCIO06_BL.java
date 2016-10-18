package com.cherry.mo.cio.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO06_IF;
import com.cherry.mo.cio.service.BINOLMOCIO03_Service;
import com.cherry.mo.cio.service.BINOLMOCIO06_Service;
import com.cherry.synchro.mo.interfaces.PaperSynchro_IF;

@SuppressWarnings("unchecked")
public class BINOLMOCIO06_BL implements BINOLMOCIO06_IF {

	private static Logger logger = LoggerFactory
			.getLogger(BINOLMOCIO06_BL.class.getName());
	@Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
	@Resource(name="binOLMOCIO06_Service")
	private BINOLMOCIO06_Service binOLMOCIO06_Service;
	@Resource(name="binOLMOCIO03_Service")
	private BINOLMOCIO03_Service binOLMOCIO03_Service;
	@Resource(name="paperSynchro")
	private PaperSynchro_IF paperSynchro;
	
	@Override
	public List<Map<String,Object>>  getAllCounterAndRegion(Map<String, Object> map) {
		List<Map<String,Object>> resultList = binOLMOCIO06_Service.getAllCounterAndRegion(map);
		List<Map<String,Object>> counterOrganiztionIdList = binOLMOCIO06_Service.getCounterOrganiztionId(map);
		List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
//		String str = null;
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "regionId", "regionName" };
		String[] keys2 = { "provinceId", "provinceName" };
		String[] keys3 = { "cityId", "cityName" };
		String[] keys4 = { "counterId", "counterName"};
		keysList.add(keys1);
		keysList.add(keys2);
		keysList.add(keys3);
		keysList.add(keys4);
		ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList, keysList, 0);
		if(counterOrganiztionIdList.size() > 0){
			for(int i = 0; i< resultTreeList.size() ; i++){
				Map<String,Object> regionMap = resultTreeList.get(i);
				List<Map<String, Object>> provinceList = (List<Map<String, Object>>) regionMap.get("nodes");
				for(int j=0 ; j < provinceList.size(); j++){
					Map<String,Object> provinceMap = provinceList.get(j);
					List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get("nodes");
					for(int n = 0; n < cityList.size() ; n++){
						Map<String,Object> cityMap = cityList.get(n);
						List<Map<String, Object>> CounterList = (List<Map<String, Object>>) cityMap.get("nodes");
						for(int k=0 ; k < CounterList.size() ; k++){
							for(int l = 0; l<counterOrganiztionIdList.size();l++){
								if(CounterList.get(k).get("id").equals(counterOrganiztionIdList.get(l).get("organizationId"))){
									CounterList.get(k).put("checked", true);
									cityMap.put("checked", true);
									provinceMap.put("checked", true);
									regionMap.put("checked", true);
								}
							}
						}
					}
				}
			}
		}
//		try {
//			str = JSONUtil.serialize(resultTreeList);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		return resultTreeList;
	}

	public String getControlFlag(Map<String,Object> map) throws Exception{
		List<Map<String,Object>> list = binOLMOCIO06_Service.getCounterOrganiztionId(map);
		String str = "0";
		if(list.size() > 0){
			str = ConvertUtil.getString(list.get(0).get("controlFlag"));
		}
		return str;
	}

	/**
	 * 问卷下发操作
	 * @param  map 存放的是一些基本信息，包括问卷的基本信息，操作人的基本信息等
	 * @param checkedList 记录插入问卷禁止表中的一些信息的List
	 * @param unCheckedList 记录禁止的柜台号的List<String>
	 * 
	 * */
	@Override
	public void tran_issuedPaper(Map<String, Object> map,List<Map<String,Object>> checkedList,List<String> unCheckedList) throws Exception {
			
			String controlFlag = ConvertUtil.getString(map.get("controlFlag"));
			List<Map<String,Object>> insertList = new ArrayList<Map<String,Object>>();
			List<String> forbiddenList = new ArrayList<String>();
			if(checkedList.size() == 0){
				if(controlFlag.equals("0")){
					map.put("controlFlag", "1");
				}else{
					map.put("controlFlag", "0");
				}
				for(int index = 0 ; index < unCheckedList.size() ; index++){
					Map<String,Object> mapOfList = new HashMap<String,Object>();
//					String id = unCheckedList.get(index);
//					int id1 = Integer.parseInt(id);
//					Integer organizationId = new Integer(id1);
					mapOfList.put("organizationId", unCheckedList.get(index));
					mapOfList.putAll(map);
					insertList.add(mapOfList);
				}
			}else{
				insertList.addAll(checkedList);
				for(int i = 0; i<insertList.size() ; i++){
					Map<String,Object> insertOfMap = insertList.get(i);
					insertOfMap.putAll(map);
				}
			}
			if(controlFlag.equals("0")){
				for(int index = 0 ; index < checkedList.size() ; index++){
					Map<String,Object> mapOfList = checkedList.get(index);
					String organizationId = ConvertUtil.getString(mapOfList.get("organizationId"));
					String counterCode = binOLMOCIO06_Service.getCounterCode(organizationId);
					forbiddenList.add(counterCode);
				}
			}else{
				for(int index = 0 ; index < unCheckedList.size() ; index++){
					String organizationId = ConvertUtil.getString(unCheckedList.get(index));
					String counterCode = binOLMOCIO06_Service.getCounterCode(organizationId);
					forbiddenList.add(counterCode);
				}
			}
//			forbiddenList = (List<String>) JSONUtil.deserialize(str2);
//			for(int i = 0; i<insertList.size() ; i++){
//				Map<String,Object> insertOfMap = insertList.get(i);
//				insertOfMap.putAll(map);
//				insertOfMap.put("organizationId", insertOfMap.get("id"));
//			}
			Map<String, Object> publisher = binOLMOCIO06_Service.getPublisher(map);
			map.put("publisher", publisher.get("EmployeeName"));
			binOLMOCIO06_Service.updatePaper(map);							//更新问卷主表信息
			if(map.get("isIssued").equals("0")){                      //判断在问卷禁止信息表中是否已经存在该问卷的信息，如果存在则先将信息物理删除，然后再插入新的信息；如果不存在则直接进行信息插入
				binOLMOCIO06_Service.insertPaperForbidden(insertList);
			}else{
				binOLMOCIO06_Service.deletePaper(map);
				binOLMOCIO06_Service.insertPaperForbidden(insertList);
			}
			
			String brandCode = binOLMOCIO03_Service.getBrandCode(map);    // 调用BINOLMOCIO03_Service中的方法查询出brandCode
			Map<String,Object> issuedMap = new HashMap<String,Object>();
			issuedMap.put("BrandCode", brandCode);
			issuedMap.put("CounterCodeList", forbiddenList);
			issuedMap.put("PaperID", map.get("paperId"));
			if (map.get("isIssued").equals("0")) {					//判断该问卷是否是第一次下发
				Map<String, Object> paperMap = binOLMOCIO03_Service.getPaper(map); // 申明一个map用于存放要下发的问卷的主要信息
				paperMap.put("BrandCode", brandCode);
				List<Map<String,Object>> quesList = binOLMOCIO03_Service.getQuestion(map); //调用Service获取问卷的问题List
				paperMap.put("QuestionList", quesList);
				paperSynchro.addPaper(paperMap);								//调用接口将问卷的信息添加到老后台中
			}
			paperSynchro.synchroForbiddenCounter(issuedMap);
			
	}
	
	/**
	 * 下发问卷时新后台的操作
	 * @param map 存放的是要下发的问卷更新主表的信息，判断是否要去问卷禁止表中删除数据的字段以及删除数据需要的信息
	 * @param list 存放的插入问卷禁止表的信息
	 * 
	 * */
	@SuppressWarnings("unused")
	private void optionInNewSystem(Map<String,Object> map ,List<Map<String,Object>> list) throws Exception{
		Map<String, Object> publisher = binOLMOCIO06_Service.getPublisher(map);
		map.put("publisher", publisher.get("EmployeeName"));
		binOLMOCIO06_Service.updatePaper(map);							//更新问卷主表信息
		if(map.get("isIssued").equals("0")){                      //判断在问卷禁止信息表中是否已经存在该问卷的信息，如果存在则先将信息物理删除，然后再插入新的信息；如果不存在则直接进行信息插入
			binOLMOCIO06_Service.insertPaperForbidden(list);
		}else{
			binOLMOCIO06_Service.deletePaper(map);					//先删除在问卷禁止表中该问卷的信息
			binOLMOCIO06_Service.insertPaperForbidden(list);		//向问卷中插入信息
		}
	}
	
	/**
	 * 下发问卷时对于老后台的操作
	 * @param map 存放 的是问卷的主表信息
	 * @param list 禁止的柜台号列表
	 * 
	 * */
	@SuppressWarnings("unused")
	private void optionInOldSystem(Map<String,Object> map,List<Map<String,Object>> list) throws Exception{
		
		String brandCode = binOLMOCIO03_Service.getBrandCode(map);    // 调用BINOLMOCIO03_Service中的方法查询出brandCode
		Map<String,Object> issuedMap = new HashMap<String,Object>();
		issuedMap.put("BrandCode", brandCode);
		issuedMap.put("CounterCodeList", list);
		issuedMap.put("PaperID", map.get("paperId"));
		if (map.get("isIssued").equals("0")) {					//判断该问卷是否是第一次下发
			Map<String, Object> paperMap = binOLMOCIO03_Service.getPaper(map); // 申明一个map用于存放要下发的问卷的主要信息
			paperMap.put("BrandCode", brandCode);
			List<Map<String,Object>> quesList = binOLMOCIO03_Service.getQuestion(map); //调用Service获取问卷的问题List
			paperMap.put("QuestionList", quesList);
			paperSynchro.addPaper(paperMap);								//调用接口将问卷的信息添加到老后台中
		}
		paperSynchro.synchroForbiddenCounter(issuedMap);				//调用接口进行问卷下发
		
	}

	@Override
	public List<Map<String, Object>> parseFile(File file,
			Map<String, Object> map) throws CherryException, Exception {
		if (file == null || !file.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用于验证柜台号是否存在
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.putAll(map);
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		// upExcel
		Workbook wb = null;
		try {
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(file, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 上传文件解析失败，请检查文件扩展名是否为.xls！
			throw new CherryException("EBS00041");
		}
		if (null == wb) {
			throw new CherryException("EBS00041");
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 产品数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.COUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		for (int r = 1; r < dateSheet.getRows(); r++) {
			// 品牌代码
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 柜台编码
			String counterCode = dateSheet.getCell(1, r).getContents().trim();
			// 柜台名称
			String counterName = dateSheet.getCell(2, r).getContents().trim();
			
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)) {
				break;
			}
			checkMap.put("counterCode", counterCode);
			// 数据库对应的柜台信息【验证的范围为"页面上的柜台树的柜台节点"】
			Map<String, Object> dbCounterInfo = binOLMOCIO06_Service.getCounterInfo(checkMap);
			
			if("".equals(brandCode)) {
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			} else if(!brand_code.equals(brandCode)) {
				// 出现不符合的品牌
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			}
			// 判断柜台号与柜台名称【柜台号必填，柜台名称要么为空，否则必须是柜台号对应的柜台名称】
			if("".equals(counterCode)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(counterCode.length() > 15) {
				// 长度错误
				throw new CherryException("EBS00033", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1), "1" });
				
			} else if(CherryChecker.isNullOrEmpty(dbCounterInfo)) {
				// 导入的发布柜台不存在[{0}sheet单元格[{1}]数据无效或无权限操作！]
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(!"".equals(counterName)) {
				// 柜台号正确的情况下才去判断柜台名称
				if(!counterName.equals(dbCounterInfo.get("counterName"))){
					// 柜台号要么为空，要么是柜台号对应的柜台名称
					// {0}sheet单元格[{1}]数据有误,请确认是否为柜台号对应的名称！
					throw new CherryException("EMO00083", new String[] {
							CherryConstants.COUNTER_SHEET_NAME, "C" + (r + 1) });
				}
			}
			
			/** 组装柜台数据***/
			Map<String, Object> listMap = new HashMap<String, Object>();
			// 只取柜台对应的部门ID
			listMap.put("organizationId", dbCounterInfo.get("organizationID"));
			list.add(listMap);
		}
		if(list.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		//set方法去除list中重复的数据 set中插入重复的值只保留一个
    	HashSet h = new HashSet(list);
    	list.clear();
    	list.addAll(h);
		return list;
	}
	
	/**
	 * 取得导入柜台的补集（此处的全集为"页面上的柜台树的柜台节点"）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<String> getContraryOrgID(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLMOCIO06_Service.getContraryOrgID(map);
	}
	
}
