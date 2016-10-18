/*  
 * @(#)BINOLPTJCS19_BL.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS39_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS16_Service;
import com.cherry.pt.jcs.service.BINOLPTJCS39_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


public class BINOLPTJCS39_BL extends SsBaseBussinessLogic implements BINOLPTJCS39_IF{
	@Resource
	private BINOLPTJCS39_Service binOLPTJCS39_Service;
	
	@Resource(name="binOLPTJCS16_Service")
	private BINOLPTJCS16_Service binOLPTJCS16_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTJCS39_BL.class);
	
	/** 取得系统各类编号 */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Override
	public Map<String, Object> getPrtFunInfo(Map<String, Object> map) {
		return binOLPTJCS39_Service.getPrtFunInfo(map);
	}
	
	/**
	 * 取得方案明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getPrtFunInfoDetailCount(Map<String, Object> map){
		return binOLPTJCS39_Service.getPrtFunInfoDetailCount(map);
	}
	
	/**
	 * 取得方案明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getPrtFunInfoDetailList(Map<String, Object> map){
		return binOLPTJCS39_Service.getPrtFunInfoDetailList(map);
	}
	
	/**
	 * 保存添加的方案产品明细
	 * 
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_addPrtFunDetail(Map<String, Object> map,BINOLPTJCS38_Form form) throws JSONException,Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 产品功能开启时间ID
		map.put("productFunctionID", form.getProductFunctionID());
        // 产品id
        String[] prtIdArr = form.getPrtIdArr();
       // 产品功能开启时间主表有效区分
        String pfValidFlag = ConvertUtil.getString(map.get("pfValidFlag"));
        // 主表无效的话，添加的明细也设为无效
        map.put("prtFunDetailValidFlag",CherryConstants.VALIDFLAG_DISABLE.equals(pfValidFlag)? CherryConstants.VALIDFLAG_DISABLE : CherryConstants.VALIDFLAG_ENABLE);
        if(null != prtIdArr){
        	for(int i=0;i<prtIdArr.length;i++){
        		
        		map.put("productID", prtIdArr[i]);
        		binOLPTJCS39_Service.mergePrtFunDetail(map);
        	}
        }
		
		return null;
	}
	
	
	/**
	 * 保存编辑的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_editPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS38_Form form)  throws JSONException,Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		
		// 产品功能开启时间ID
		map.put("productFunctionID", form.getProductFunctionID());
		map.put("productFunctionDetailID", form.getProductFunctionDetailID());
		map.put("productID", form.getProductID());
		
		binOLPTJCS16_Service.updPrtPriceSoluDetail(map);
		return null;
	}
	
	/**
	 * 批量无效方案产品明细
	 * @param map
	 * @throws Exception
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public void tran_delPrtPriceSoluDetail(Map<String, Object> map) throws Exception{
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 产品
		String productInfoIds = (String)map.get("productInfoIds");
		String [] prtIDsList = productInfoIds.split(",");
		
		map.put("prtFunDetailValidFlag",CherryConstants.VALIDFLAG_DISABLE);
		if(prtIDsList.length != 0){
			for(String prtIDs : prtIDsList){
				map.put("productID", prtIDs);
				binOLPTJCS39_Service.mergePrtFunDetail(map);
			}
		}
	}
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	
	/**
	 * 产品方案明细添加产品分类
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryProductCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> tran_addPrtPriceSoluCateDetail(Map<String, Object> map,BINOLPTJCS38_Form form)  throws JSONException,Exception{
		
		String businessDate = binOLPTJCS39_Service.getBusDate(map);
		map.put("businessDate",businessDate);
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		map.put("tVersion", tVersion);
		
		// 产品功能开启时间ID
		map.put("productFunctionID", form.getProductFunctionID());
		
		
		// 删除产品方案绑定的产品分类
		binOLPTJCS39_Service.delPrtSoluDetailCate(map);
		
		// 添加产品方案画面当前所有的产品分类
		String cateInfo = form.getCateInfo();
		if (!CherryConstants.BLANK.equals(cateInfo)) {
			// 产品分类信息List
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(cateInfo);
			if (null != cateInfoList) {
				for (Map<String, Object> cate : cateInfoList) {
					if (!CherryChecker.isNull(cate.get(ProductConstants.PROPVALID))) {
						cate.putAll(map);
						// 插入方案明细添加产品分类 
//						binOLPTJCS03_Service.insertPrtCategory(cate);
						List<String> propValArrList = (List<String>) cate.get("propValArr");
						if(!CherryUtil.isBlankList(propValArrList)){
							for(String propVal : propValArrList){
								cate.put("propValId", propVal);
								binOLPTJCS39_Service.insertCate(cate);
							}
						}
					}
				}
			}
		}
		
		List<Map<String, Object>> prtForPrtSoluDetailDiff = binOLPTJCS39_Service.getPrtForPrtSoluDetailDiff(map);
		if (!CherryUtil.isBlankList(prtForPrtSoluDetailDiff)) {
			for(Map<String, Object> diffMap : prtForPrtSoluDetailDiff){
				// 将差异更新到产品方案明细表
				diffMap.putAll(map);
				String modifyFlag = (String)diffMap.get("modifyFlag"); // modifyFlag  add 增加的柜台 、sub减少的柜台
				
				// 取得当前方案及增加的产品,merge到产品方案明细表 validFlag = 1,version = tversion +1,isCate =1
				if("add".equals(modifyFlag)){
					diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_ENABLE);
					diffMap.put("productId", diffMap.get("prtPD"));
					// 1: 插入产品方案明细表
					binOLPTJCS39_Service.mergeProductPricePrtFunDetail(diffMap);
					
				}
				// 取得当前方案明细减少的产品,merge到产品方案部门关系表 validFlag = 0,version = tversion +1
				else if ("sub".equals(modifyFlag)){
					
					diffMap.put("ValidFlag", CherryConstants.VALIDFLAG_DISABLE);
					diffMap.put("productId", diffMap.get("prtPDH"));
					// 1: 将方案明细表的产品数据无效掉
					binOLPTJCS39_Service.updPrtSoluDetail(diffMap);
				}
			}
			
		}
		
		
		return null;
		
	}
	
	/**
	 * 获取产品分类List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getCateList(Map<String, Object> map) {

		return binOLPTJCS39_Service.getCateList(map);
	}
	
	/**
	 * 
	 * 获取导入的数据
	 * 
	 * @param map导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception {
		
		Map<String, Object> importDatas = new HashMap<String, Object>();
		
		// 取得上传文件path
		File upExcel = (File) map.get("upExcel");
		if (upExcel == null || !upExcel.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		InputStream inStream = null;
		Workbook wb = null;
		try {
			inStream = new FileInputStream(upExcel);
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 方案数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.PRTFUNDETAIL_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 方案数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.PRTFUNDETAIL_SHEET_NAME });
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.PRTFUN_EXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
        }
		
        // 存入导入的所有数据
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		
		// 存入校验错误的行数据
		List<Map<String,Object>> errorList = new ArrayList<Map<String, Object>>();
        
		int sum = 0;
		for (int r = 1; r < dateSheet.getRows(); r++) {
			
			// 每一行数据
			Map<String, Object> rowMap = new HashMap<String, Object>();
			
			// 厂商编码(unitCode)
			String unitCode = dateSheet.getCell(0, r).getContents().trim();
			// 产品名称（备用）
			String prtName = dateSheet.getCell(1, r).getContents().trim();
			
			// (连续10行)整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(unitCode) 
					&& CherryChecker.isNullOrEmpty(prtName)) {
				sum++;
				if (sum >= 10) {
					break;
				} else {
					continue;
				}
			}
			sum = 0;
			
			rowMap.put("unitCode", unitCode);
			rowMap.put("prtName", prtName);
			
			rowMap.put("rowNumber", r); // 行号
			
			// 数据基本格式校验
			this.checkData(rowMap, map, errorList);
			
			detailList.add(rowMap);
		}
		
		if (detailList.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { ProductConstants.DATE_SHEET_NAME });
		}
		
		importDatas.put("detailList", detailList);
		importDatas.put("errorList", errorList);
		
		return importDatas;
	}
	
	/**
	 * 导入数据基本格式校验
	 * @param rowMap
	 * @param map
	 * @param errorList 存在有错误的行
	 * @return
	 * @throws Exception
	 * 返回值 true:校验不通过(有错误) ，false:校验通过
	 */
	public boolean checkData(Map<String, Object> rowMap,Map<String,Object> map,List<Map<String,Object>> errorList ) throws Exception {
		
		String rowNumberStr = ConvertUtil.getString(rowMap.get("rowNumber"));
		Integer rowNumber = Integer.valueOf(rowNumberStr);
		String unitCode = ConvertUtil.getString(rowMap.get("unitCode"));
		String prtName = ConvertUtil.getString(rowMap.get("prtName"));
		
		//错误区分，记录该行数据是否有错误，默认为没有错误
		boolean errorFlag = false;
		
		StringBuffer errStr = new StringBuffer();
		
		// unitCode校验
		if (CherryConstants.BLANK.equals(unitCode)) {
			// 单元格为空
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00031", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1) });
		} else if (unitCode.length() > 20) {
			// 代码长度错误
			errorFlag = true;
			errStr.append("").append(","); // 存在错误信息
			throw new CherryException("EBS00033", new String[] {
					CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
		}else {
			rowMap.putAll(map);
			Map<String,Object> prtMap = binOLPTJCS16_Service.getProductInfo(rowMap);
			if(null == prtMap || prtMap.isEmpty()){
				errorFlag = true;
				errStr.append("").append(","); // 存在错误信息
				throw new CherryException("EBS00137",new String[] {
						CherryConstants.PRTSOLUDETAIL_SHEET_NAME, "A" + (rowNumber + 1), "20" });
			}else {
				rowMap.put("productID", prtMap.get("BIN_ProductID"));
			}
			
		}
		
		if(errorFlag){
			errorList.add(rowMap);
		}
		return errorFlag;
	}
	
	/**
	 * 产品功能开启时间明细数据导入处理
	 * 
	 * @param importDataMap
	 *            导入的数据
	 * @param sessionMap
	 *            登录用户参数
	 * @return 导入的结果
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception {
		// 操作结果统计map
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		// 产品操作成功数
		int optCount = 0;
		// 产品添加成功数
		int addCount = 0;
		// 产品更新成功数
		int updCount = 0;
		
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(sessionMap);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		sessionMap.put("tVersion", tVersion);
		
		// 产品List
		List<Map<String, Object>> list = (List<Map<String, Object>>) importDataMap.get("detailList");
		
		for (Map<String, Object> prtMap : list) {
			
			// 方案ID
			prtMap.put("productFunctionID", sessionMap.get("productFunctionID"));
			prtMap.put("tVersion", tVersion);
			
	        // 产品功能开启时间主表有效区分
	        String pfValidFlag = ConvertUtil.getString(sessionMap.get("pfValidFlag"));
	        // 主表无效的话，添加的明细也设为无效
	        prtMap.put("prtFunDetailValidFlag",CherryConstants.VALIDFLAG_DISABLE.equals(pfValidFlag)? CherryConstants.VALIDFLAG_DISABLE : CherryConstants.VALIDFLAG_ENABLE);
			
	        Map<String, Object> mergeMap = binOLPTJCS39_Service.mergePrtFunDetail(prtMap);
//			binOLPTJCS16_Service.mergePrtPriceSoluDetail(prtMap);
			String actionResult = ConvertUtil.getString(mergeMap.get("actionResult"));
			
			if(!actionResult.isEmpty()){
				if("UPDATE".equals(actionResult)){
					String delValidFlag = ConvertUtil.getString(mergeMap.get("delValidFlag"));
					if(delValidFlag.equals(CherryConstants.VALIDFLAG_DISABLE)){
						// 这种情况是在方案明细中被逻辑删除的产品。
						++addCount;
					}else{
						++updCount;
					}
				}else if("INSERT".equals(actionResult)){
					++addCount;
				}
			}
			
			// 产品操作成功数
			++optCount;
		}
		
		// 产品操作成功数
		infoMap.put(ProductConstants.OPTCOUNT, optCount);
		infoMap.put(ProductConstants.UPDCOUNT, updCount);
		infoMap.put(ProductConstants.ADDCOUNT, addCount);
		
		return infoMap;
	}	
}
