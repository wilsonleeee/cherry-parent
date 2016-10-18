/*  
 * @(#)BINOLMOCIO07_BL.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO07_IF;
import com.cherry.mo.cio.service.BINOLMOCIO07_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.synchro.mo.interfaces.SaleTargetSynchro_IF;

@SuppressWarnings("unchecked")
public class BINOLMOCIO07_BL extends SsBaseBussinessLogic implements BINOLMOCIO07_IF{
	@Resource(name="binOLMOCIO07_Service")
	private BINOLMOCIO07_Service binOLMOCIO07_Service;
	@Resource(name="binOLMOCIO07_01_BL")
	private BINOLMOCIO07_01_BL binOLMOCIO07_01_BL;

    @Resource(name="saleTargetSynchro")
    private SaleTargetSynchro_IF saleTargetSynchro_IF;
    @Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binolcm00Service;
    @Resource(name="CodeTable")
    private CodeTable codeTable;
    @Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
    
    /** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.5";
	/**
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getSaleTargetCount(Map<String, Object> map) {
		return binOLMOCIO07_Service.getSaleTargetCount(map);
	}
	
	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchSaleTargetList(Map<String, Object> map) {
		return binOLMOCIO07_Service.searchSaleTargetList(map);
	}
	
	/**
	 * 获取柜台树节点
	 * 
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getTreeNodes(Map<String, Object> map)	throws Exception {
		// 调用service获取按共通部门/人员权限取得树节点（区域、柜台、美容顾问），按照path排序
		List<Map<String, Object>> list = binOLMOCIO07_Service.getTreeNodes(map);
		// 将获取的list转换成树形结构需要的形式
		List<Map<String, Object>> resultList = ConvertUtil.getTreeList(list, "nodes");
		// 调用递归函数，处理resultList中的数据，将不需要节点清除
		resultList = dealJsDepartList(resultList);
		if(null != resultList && map.get("type").equals("3")){
			// 取得最大岗位级别
			String grade = binolcm00Service.getMaxPosCategoryGrade(map);
			if(grade != null && !"".equals(grade)) {
				List<Map<String, Object>> resultTempList = new ArrayList<Map<String, Object>>();
				for(int i=0; i<resultList.size(); i++){
					Object gradeTemp = resultList.get(i).get("grade");
					if(gradeTemp == null || !grade.equals(gradeTemp.toString())) {
						resultTempList.add(resultList.get(i));
						resultList.remove(i);
						i--;
					}
				}
                if (null != resultTempList && resultTempList.size() > 0) {
                    Map<String, Object> noHigherMap = new HashMap<String, Object>();
                    noHigherMap.put("name", CherryConstants.UNKNOWN_DEPARTNAME);
                    noHigherMap.put("nodes", resultTempList);
                    resultList.add(noHigherMap);
                }
			}
		}
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
		if (null != list && list.size() > 0) {
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
	
	/**
	 * 下发订货参数
	 * 
	 * 
	 * */
	@Override
	public void down(Map<String, Object> map) throws Exception {
		
		String targetType = ConvertUtil.getString(map.get("targetType"));
		if("".equals(targetType) || "PRM".equals(targetType)) {
			// 只能下发目标类型为产品的销售目标，请重新选择进行下发！
			throw new CherryException("EMO00086");
		}
		String brandInfoId = (String) map.get("brandInfoId");
		String brandCode=binOLMOCIO07_Service.getBrandCode(brandInfoId);
		
		List<Map<String ,Object>> downList = binOLMOCIO07_Service.searchDownList(map);
		Map<String,Object> mapIF = new HashMap<String,Object>();
		if(null != downList && downList.size() > 0){
			//遍历柜台和产品list，将这两个list中的map整合到paramList中用于插入操作
			for(int i = 0 ; i < downList.size() ; i ++){
				Map<String,Object> paramMap = downList.get(i);
				String unitType = ConvertUtil.getString(paramMap.get("Type"));
				Map<String,Object> param = binOLMOCIO07_Service.searchParameter(paramMap);
				if("1".equals(unitType)){
					// 单位类型为区域【终端regiontarget表的取的是单位名称】
					int year=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(2, 4));
                	int month=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(4, 6));
                	mapIF.put("Year", year);
                	mapIF.put("Month",month);
                	mapIF.put("BrandCode",brandCode);
                	// 单位类型
                	mapIF.put("Type", paramMap.get("Type"));
                	mapIF.put("TargetMoney", paramMap.get("TargetMoney"));
                	mapIF.put("TargetQuantity", paramMap.get("TargetQuantity"));
                	// 单位名称
                	mapIF.put("Param",param.get("param"));
                	// 目标类型【目前只下发产品】
                	mapIF.put("TargetType", paramMap.get("TargetType"));
                	// 活动CODE
                	mapIF.put("ActivityCode", paramMap.get("ActivityCode"));
                	// 活动名称
                	mapIF.put("ActivityName", paramMap.get("ActivityName"));
                	mapIF.put("BIN_SaleTargetID",paramMap.get("BIN_SaleTargetID"));
                	mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	saleTargetSynchro_IF.synchroSaleTarget(mapIF);
                	binOLMOCIO07_Service.downUpdate(mapIF);
				} else if("2".equals(unitType) || "3".equals(unitType)) {
					// 单位类型为柜台或者美容顾问
					int year=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(2, 4));
                	int month=CherryUtil.string2int(((String)paramMap.get("TargetDate")).substring(4, 6));
                	mapIF.put("Year", year);
                	mapIF.put("Month",month);
                	mapIF.put("BrandCode",brandCode);
                	mapIF.put("Type", paramMap.get("Type"));
                	mapIF.put("TargetMoney", paramMap.get("TargetMoney"));
                	mapIF.put("TargetQuantity", paramMap.get("TargetQuantity"));
                	mapIF.put("Param",param.get("paramID"));
                	// 目标类型【目前只下发产品】
                	mapIF.put("TargetType", paramMap.get("TargetType"));
                	// 活动CODE
                	mapIF.put("ActivityCode", paramMap.get("ActivityCode"));
                	// 活动名称
                	mapIF.put("ActivityName", paramMap.get("ActivityName"));
                	mapIF.put("BIN_SaleTargetID",paramMap.get("BIN_SaleTargetID"));
                	mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	saleTargetSynchro_IF.synchroSaleTarget(mapIF);
                	binOLMOCIO07_Service.downUpdate(mapIF);
				} else {
					// 单位类型为办事处、经销商、柜台主管
					mapIF.put("BIN_SaleTargetID",paramMap.get("BIN_SaleTargetID"));
					mapIF.put("updatedBy", map.get("updatedBy"));
                	mapIF.put("updatePGM", map.get("updatePGM"));
                	// 只更新新后台相关数据的状态，不实际下发数据
                	binOLMOCIO07_Service.downUpdate(mapIF);
				}
			}
		}else{
			throw new CherryException("IMO00057");
		}
	}

	@Override
	public void tran_setSaleTarget(Map<String, Object> map) throws CherryException {
		Map<String, Object> targetInfoMap = new HashMap<String, Object>();
		
		String[] parameterArr = (String[]) map.get("parameterArr");
		if ("".equals(ConvertUtil.getString(map.get("targetDate")))) {
			map.put("targetDate",
					CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		}
		if ("".equals(ConvertUtil.getString(map.get("targetMoney")))) {
			map.put("targetMoney", "0");
		}
		if ("".equals(ConvertUtil.getString(map.get("targetQuantity")))) {
			map.put("targetQuantity", "0");
		}
        for(int i=0;i<parameterArr.length;i++){
        	if(("").equals(parameterArr[i]) || null == parameterArr[i]){
        		i++;
        	} else {
        		targetInfoMap.put("type", map.get("type"));
        		targetInfoMap.put("parameter", parameterArr[i]);
        		targetInfoMap.put("targetDate", map.get("targetDate"));
        		targetInfoMap.put("targetType", map.get("targetType"));
        		
        		targetInfoMap.put("targetMoney", map.get("targetMoney"));
        		targetInfoMap.put("targetQuantity", map.get("targetQuantity"));
        		targetInfoMap.put("activityCode", map.get("activityCode"));
        		targetInfoMap.put("activityName", map.get("activityName"));
        		targetInfoMap.put("Source", CherryConstants.SALETARGET_SOURCE_BACKEND);
        		
        		targetInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
    					map.get(CherryConstants.ORGANIZATIONINFOID));
    			targetInfoMap.put(CherryConstants.BRANDINFOID,
    					map.get(CherryConstants.BRANDINFOID));
    			
    			targetInfoMap.put(CherryConstants.CREATEPGM, map.get("createPGM"));
    			targetInfoMap.put(CherryConstants.CREATEDBY, map.get("createdBy"));
    			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("createPGM"));
    			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("createdBy"));
    			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("updatedBy"));
    			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("updatePGM"));
            	binOLMOCIO07_Service.mergeSaleTarget(targetInfoMap);
            }
        }
	}
	
	/**
	 * 导入销售目标处理(取得一条数据处理一条)
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> resolveExcel(Map<String, Object> map)
			throws Exception {
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
			throw new CherryException("EBS00041");
		} finally {
			if (inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 字段说明SHEET
		Sheet descriptSheet = null;
		// 订货数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.SALETARGET_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 判断模板版本号
		if (null == descriptSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.DESCRIPTION_SHEET_NAME });
		} else {
			// 版本号（B）
			String version = descriptSheet.getCell(1, 0).getContents().trim();
			if (!EXCEL_VERSION.equals(version)) {
				// 模板版本不正确，请下载最新的模板进行导入！
				throw new CherryException("EBS00103");
			}
		}
		// 销售目标数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.SALETARGET_SHEET_NAME });
		}

		// 处理总件数
		int totalCount = 0;
		// 成功处理总件数
		int successCount = 0;
		// 失败总件数
		int failCount = 0;

		int sheetLength = dateSheet.getRows();

		// 声明一个list用来存放解析过程中出错的销售目标信息，并记录下相应的错误。
		List<Map<String, Object>> errorTargetList = new ArrayList<Map<String, Object>>();

		// 循环导入柜台信息，从第6开始为要导入的信息
		for (int r = 5; r < sheetLength; r++) {
			// 错误区分，记录该行数据是否有错误，默认为没有错误
			boolean errorFlag = false;

			// 类型（A）
			String type = dateSheet.getCell(0, r).getContents().trim();
			// 目标代号（B）
			String targetCode = dateSheet.getCell(1, r).getContents().trim();
			// 目标名称（C）
			String targetName = dateSheet.getCell(2, r).getContents().trim();
			// 目标金额（D）
			String targetType = dateSheet.getCell(3, r).getContents().trim();
			// 目标数量（E）
			String activityCode = dateSheet.getCell(4, r).getContents();
			// 目标金额（F）
			String activityName = dateSheet.getCell(5, r).getContents().trim();
			// 目标金额（G）
			String targetMoney = dateSheet.getCell(6, r).getContents().trim();
			// 目标数量（H）
			String targetQuantity = dateSheet.getCell(7, r).getContents().trim();
			// 目标年月（I）
			String targetDate = dateSheet.getCell(8, r).getContents().trim();
			
			/**=================日目标对应的金额与数量=============================**/
			Map<String, Object> targetRowMap = new HashMap<String, Object>();
			int targetMoneyOrQuantityCount = 0;
			for(int i=1; i<=31; i++) {
				if(!CherryChecker.isNullOrEmpty(dateSheet.getCell(8+i, r).getContents(), true)) {
					targetMoneyOrQuantityCount++;
				}
				if(!CherryChecker.isNullOrEmpty(dateSheet.getCell(39+i, r).getContents(), true)) {
					targetMoneyOrQuantityCount++;
				}
				
				targetRowMap.put("targetMoney"+i, dateSheet.getCell(8+i, r).getContents().trim());
				targetRowMap.put("targetQuantity"+i, dateSheet.getCell(39+i, r).getContents().trim());
			}
			
			/**====================================================================**/
			
			// 整行数据为空，程序认为sheet内有效行读取结束
			if ("".equals(type) && "".equals(targetDate)
					&& "".equals(targetCode) && "".equals(targetName)
					&& "".equals(targetType) && "".equals(activityCode)
					&& "".equals(activityName) && "".equals(targetMoney)
					&& "".equals(targetQuantity) && targetMoneyOrQuantityCount == 0) {
				break;
			}
			// 处理总件数+1
			totalCount++;

			Map<String, Object> targetInfo = new HashMap<String, Object>();
			targetInfo.putAll(map);
			
			boolean isCorrectType = false;
			//【列1】 处理销售目标单位类型（必填）1：区域；2：柜台；3：美容顾问 4：办事处 5：经销商 6：柜台主管
			if(!CherryChecker.isNullOrEmpty(type, true)){
				List<Map<String, Object>> typeList = codeTable.getCodes("1124");
				for(Map<String, Object> typeMap : typeList){
					if(type.equals(typeMap.get("Value"))){
						isCorrectType = true;
						targetInfo.put("type", typeMap.get("CodeKey"));
						break;
					}
				}
				if(!isCorrectType) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标类型有错误
					targetInfo.put("typeError", true);
					targetInfo.put("type", -1);
				}
			} else {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志销售目标类型有错误
				targetInfo.put("typeError", true);
				targetInfo.put("type", -1);
			}
			targetInfo.put("typeShow", type);

			// 用于判断在不同类型下的不同操作
			String typeStr = targetInfo.get("type").toString();
			// 【列3、4】类型为柜台、人员、柜台主管时：代码必填,名称选填（填入则必须为代号对应的名称）
			if ("2".equals(typeStr) || "3".equals(typeStr) || "6".equals(typeStr)) {
				// 处理销售目标code（必填）
				if (CherryChecker.isNullOrEmpty(targetCode)) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标code有错误
					targetInfo.put("targetCodeError", true);
				} else {
					targetInfo.put("targetCode", targetCode);
					// 查询代号对应的id(带数据权限)
					Map<String, Object> parameterMap = binOLMOCIO07_Service
							.searchParameterID(targetInfo);
					if (null == parameterMap) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志销售目标code有错误
						targetInfo.put("targetCodeError", true);
					} else if (!CherryChecker.isNullOrEmpty(targetName)
							&& !parameterMap.get("targetName").equals(
									targetName)) {
						// 标志该行数据有错误(code对应的Name不正确)
						errorFlag = true;
						// 标志销售目标名称有误（不是code对应的名称）
						targetInfo.put("targetCodeNameError", true);
					} else {
						targetInfo.put("parameter",
								parameterMap.get("parameter"));
					}
				}
			} else {
				// 类型为区域、办事处、经销商时：名称必填，代号选填(填入则必须为名称对应的代号)
				// 处理销售目标名称（必填）
				if (CherryChecker.isNullOrEmpty(targetName)) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标名称有错误
					targetInfo.put("targetNameError", true);
				} else {
					targetInfo.put("targetName", targetName);
					// 查询名称对应的id（带部门权限）
					Map<String, Object> parameterMap = binOLMOCIO07_Service
							.searchParameterID(targetInfo);
					if (null == parameterMap) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志销售目标名称有错误
						targetInfo.put("targetNameError", true);
					} else if (!CherryChecker.isNullOrEmpty(targetCode)
							&& !parameterMap.get("targetCode").equals(
									targetCode)) {
						// 标志该行数据有错误(Name对应的code不正确)
						errorFlag = true;
						// 标志销售目标代号有误（不是名称对应的代号）
						targetInfo.put("targetNameCodeError", true);
					} else {
						targetInfo.put("parameter",
								parameterMap.get("parameter"));
					}
				}
			}
			// 只用于有错误信息时的信息显示
			targetInfo.put("targetCode", targetCode);
			targetInfo.put("targetName", targetName);
			
			isCorrectType = false;
			//【列5】 处理销售目标产品类型（必填）PRO：产品；PRM：促销品
			if(!CherryChecker.isNullOrEmpty(targetType, true)){
				List<Map<String, Object>> targetTypeList = codeTable.getCodes("1300");
				for(Map<String, Object> targetTypeMap : targetTypeList){
					if(targetType.equals(targetTypeMap.get("Value"))){
						isCorrectType = true;
						targetInfo.put("targetType", targetTypeMap.get("CodeKey"));
						break;
					}
				}
				if(!isCorrectType) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标类型有错误
					targetInfo.put("targetTypeError", true);
					targetInfo.put("targetType", -1);
				}
			} else {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志销售目标类型有错误
				targetInfo.put("targetTypeError", true);
				targetInfo.put("targetType", -1);
			}
			targetInfo.put("targetTypeShow", targetType);
			
			// 【列6、7】处理销售目标对应的活动
			if(!CherryChecker.isNullOrEmpty(activityCode, true)) {
				// 目标CODE不为空时，名称必填且CODE必须是名称对应的代号
				if(CherryChecker.isNullOrEmpty(activityName, true)) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标名称有错误
					targetInfo.put("activityNameNullError", true);
				} else {
					// 校验活动CODE是否为活动名称对应的代号
					Map<String, Object> actParamMap = new HashMap<String, Object>();
					actParamMap.putAll(map);
					actParamMap.put("activityName", activityName);
					List<Map<String, Object>> actInfo = binOLMOCIO07_Service.getActivityInfoByName(actParamMap);
					if(null == actInfo || actInfo.size() != 1) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志活动名称不存在
						targetInfo.put("activityNameExistError", true);
					} else {
						if(!actInfo.get(0).get("campaignCode").equals(activityCode)) {
							// 标志该行数据有错误
							errorFlag = true;
							// 标志活动代号不是活动名称对应的代号
							targetInfo.put("activityCodeNameError", true);
						} else {
							targetInfo.put("activityCode", actInfo.get(0).get("campaignCode"));
							targetInfo.put("activityName", actInfo.get(0).get("campaignName"));
						}
						
					}
				}
			} else {
				// 活动CODE为空时只看活动名称
				if(!CherryChecker.isNullOrEmpty(activityName, true)) {
					Map<String, Object> actParamMap = new HashMap<String, Object>();
					actParamMap.putAll(map);
					actParamMap.put("activityName", activityName);
					List<Map<String, Object>> actInfo = binOLMOCIO07_Service.getActivityInfoByName(actParamMap);
					// 判断活动名称对应的活动信息是否存在
					if(null == actInfo || actInfo.size() == 0) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志活动名称不存在
						targetInfo.put("activityNameExistError", true);
					} else {
						targetInfo.put("activityName", actInfo.get(0).get("campaignName"));
						targetInfo.put("activityCode", actInfo.get(0).get("campaignCode"));
					}
				}
			}
			
			// 只用于有错误信息时的信息显示
			targetInfo.put("activityCodeShow", activityCode);
			targetInfo.put("activityNameShow", activityName);

			// 【列8】处理销售目标金额（必填,整数部分限定9位，小数部分限定2位）
			if (!CherryChecker.isNullOrEmpty(targetMoney)) {
				if (CherryChecker.isDecimal(targetMoney, 9, 2)) {
					targetInfo.put("targetMoney", targetMoney);
				} else {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标金额有错误
					targetInfo.put("targetMoneyError", true);
					targetInfo.put("targetMoney", targetMoney);
				}
			} else {
				// 为空默认为0.00
				targetInfo.put("targetMoney", 0);
			}

			// 【列9】处理销售目标数量（必填,数量最高限定为9位数）
			if (!CherryChecker.isNullOrEmpty(targetQuantity)) {
				if (CherryChecker.isNumeric(targetQuantity)) {
					targetInfo.put("targetQuantity", targetQuantity);
				} else {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志销售目标数量有错误
					targetInfo.put("targetQuantityError", true);
					targetInfo.put("targetQuantity", targetQuantity);
				}
			} else {
				// 为空默认为0
				targetInfo.put("targetQuantity", 0);
			}
			
			// 【列2】处理销售目标年月（必填，YYYYMM或者YYYYmmDD）
			if (!CherryChecker.isNullOrEmpty(targetDate) && (CherryChecker.checkDate(targetDate,CherryConstants.DATEYYYYMM))) {
				targetInfo.put("targetDate", targetDate);
			} else {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志销售目标年月有错误
				targetInfo.put("targetDateError", true);
				// 此时用于显示错误信息
				targetInfo.put("targetDate", targetDate);
			}
			
			// 有日销售目标时间
			if(targetMoneyOrQuantityCount > 0) {
				// 有日目标数据时不再看月销售目标数据
				targetInfo.remove("targetQuantity");
				targetInfo.remove("targetMoney");
				// 标记此目标类型为日销售目标
				targetInfo.put("targetDateType", "2");
				Iterator<Entry<String, Object>> it = targetRowMap.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, Object> en = it.next();
					String key = en.getKey();
					String value = ConvertUtil.getString(en.getValue());
					if(key.startsWith("targetMoney")) {
						// 日目标金额
						if (!CherryChecker.isNullOrEmpty(value)) {
							if (CherryChecker.isDecimal(value, 9, 2)) {
								targetInfo.put(key, value);
							} else {
								// 标志该行数据有错误
								errorFlag = true;
								// 标志销售目标金额有错误
								targetInfo.put(key+"Error", key.substring("targetMoney".length(),key.length()));
								targetInfo.put(key, value);
							}
						} else {
							// 为空默认为0.00
							targetInfo.put(key, 0);
						}
					} else if(key.startsWith("targetQuantity")) {
						// 日目标数量
						if (!CherryChecker.isNullOrEmpty(value)) {
							if (CherryChecker.isNumeric(value)) {
								targetInfo.put(key, value);
							} else {
								// 标志该行数据有错误
								errorFlag = true;
								// 标志销售目标数量有错误
								targetInfo.put(key+"Error", key.substring("targetQuantity".length(),key.length()));
								targetInfo.put(key, value);
							}
						} else {
							// 为空默认为0
							targetInfo.put(key, 0);
						}
					}
					
				}
			} else {
				// 标记此目标为月销售目标
				targetInfo.put("targetDateType", "1");
			}

			// 如果该行没有发生错误则将销售目标信息写入新后台，否则记录下错误的销售目标信息
			if (!errorFlag) {
				// 导入柜台处理(取出一条excel数据处理一条)
				try {
					String targetDateType = ConvertUtil.getString(targetInfo.get("targetDateType"));
					int flag = 0;
					if("".equals(targetDateType) || "1".equals(targetDateType)) {
						flag = this.handleSetSaleTarget(targetInfo);
					} else if("2".equals(targetDateType)) {
						flag = binOLMOCIO07_01_BL.handleSetSaleTarget(targetInfo);
					}
					
					
					if (flag == 1) {
						successCount++;
					} else {
						failCount++;
						targetInfo.put("unknownError", true);
						errorTargetList.add(targetInfo);
					}
				} catch (Exception e) {
					failCount++;
					errorTargetList.add(targetInfo);
				}
			} else {
				failCount++;
				errorTargetList.add(targetInfo);
			}
		}

		// 作为结果返回
		Map<String, Object> resutlMap = new HashMap<String, Object>();
		// 保存统计信息
		resutlMap.put("totalCount", totalCount);
		resutlMap.put("successCount", successCount);
		resutlMap.put("failCount", failCount);
		// 错误信息详细List
		resutlMap.put("errorTargetList", errorTargetList.isEmpty() ? null
				: errorTargetList);

		return resutlMap;
	}

	/**
	 * 导入销售目标处理
	 * 
	 * @param map
	 * @return 正常更新或者插入则返回1
	 * @throws Exception
	 */
	public int handleSetSaleTarget(Map<String, Object> map) throws Exception {
		// 记录销售目标信息
		Map<String, Object> targetInfoMap = new HashMap<String, Object>();
		String parameter = map.get("parameter").toString();
		// 未设置销售目标年月则默认为当月
		if (CherryChecker.isNullOrEmpty(map.get("targetDate"))) {
			targetInfoMap.put("targetDate",
					CherryUtil.getSysDateTime(CherryConstants.DATEYYYYMM));
		}
		targetInfoMap.put("type", map.get("type"));
		targetInfoMap.put("parameter", parameter);
		targetInfoMap.put("targetDate", map.get("targetDate"));
		targetInfoMap.put("targetType", map.get("targetType"));
		// 目标活动
		targetInfoMap.put("activityCode", map.get("activityCode"));
		targetInfoMap.put("activityName", map.get("activityName"));
		// 销售指标
		targetInfoMap.put("targetMoney", map.get("targetMoney"));
		targetInfoMap.put("targetQuantity", map.get("targetQuantity"));
		targetInfoMap.put("Source", CherryConstants.SALETARGET_SOURCE_BACKEND);
		try {
			targetInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
					map.get(CherryConstants.ORGANIZATIONINFOID));
			targetInfoMap.put(CherryConstants.BRANDINFOID,
					map.get(CherryConstants.BRANDINFOID));
			
			targetInfoMap.put(CherryConstants.CREATEPGM, map.get("createPGM"));
			targetInfoMap.put(CherryConstants.CREATEDBY, map.get("createdBy"));
			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("createPGM"));
			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("createdBy"));
			targetInfoMap.put(CherryConstants.UPDATEDBY, map.get("updatedBy"));
			targetInfoMap.put(CherryConstants.UPDATEPGM, map.get("updatePGM"));
			// 目前活动不起作用，故不实现不同活动不同目标的功能【此设定逻辑放在SQL文中】
			binOLMOCIO07_Service.mergeSaleTarget(targetInfoMap);
		} catch (Exception e) {
			return 0;
		}
		// 正常更新或者插入则返回1
		return 1;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "different", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_different"), "15", "", "" },
				{ "parameterName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_parameter"), "20", "", "" },
				{ "type", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_type"), "15", "", "1124" },
				{ "targetDate", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetDate"), "20", "", "" },
				{ "targetType", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetType"), "15", "", "1300" },
				{ "activity", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_activity"), "20", "", "" },
				{ "targetQuantity", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetQuantity"), "15", "", "" },
				{ "targetMoney", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetMoney"), "15", "", "" },
				{ "synchroFlag", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_downFlag"), "15", "", "1177" },
				{ "source", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_source"), "15", "", "1304" },
				{ "targetSetTime", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "CIO07_targetSetTime"), "30", "", "" }
		};
		int dataLen = this.getSaleTargetCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "targetSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMOCIO07", language, "targetDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "targetSetTime desc");
		return map;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return this.searchSaleTargetList(map);
	}
}