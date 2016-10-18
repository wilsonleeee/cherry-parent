/*  
 * @(#)BINOLPTUNQ03_BL    1.0 2016-06-17     
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

package com.cherry.pt.unq.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.unq.interfaces.BINOLPTUNQ03_IF;
import com.cherry.pt.unq.service.BINOLPTUNQ03_Service;

/**
 * 唯一码维护 BL
 * 
 * @author zw
 * @version 1.0 2016.06.17
 */
public class BINOLPTUNQ03_BL implements BINOLPTUNQ03_IF,Serializable {
	
	private static final long serialVersionUID = 1098978840361944706L;
	@Resource
	private BINOLPTUNQ03_Service binolptunq03_Service;		
	@Resource(name = "binOLCM05_Service")
	private transient BINOLCM05_Service binolcm05_Service;

	@Resource(name = "binOLCM05_BL")
	private transient BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLCM37_BL")
	private transient BINOLCM37_BL binOLCM37_BL;

	@Resource(name="CodeTable")
	private transient CodeTable CodeTable;
	private Map<String, Object> comMap;
	private static Logger logger = LoggerFactory.getLogger(BINOLPTUNQ03_BL.class.getName());
	private List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
	private final String cherry_clear = "cherry_clear";
    
    @Resource
    private transient  BINOLMOCOM01_IF binOLMOCOM01_BL;

	
	/**
	 * 
	 * 导入唯一码激活明细
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> resolveExcel(Map<String, Object> map) throws Exception {
		         // 取得共通参数
				comMap = getComMap(map);

				List<Map<String, Object>> errorInfos = new ArrayList<Map<String, Object>>();
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
					wb = Workbook.getWorkbook(inStream);
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
				// 唯一码维护明细数据sheet
				Sheet dateSheet = null;
				// String version = null;
				for (Sheet st : sheets) {
					if (CherryConstants.UNQDETAILS_SHEET_NAME.equals(st.getName().trim())) {
						dateSheet = st;
					}
				}
				// 重点系统活动内容数据sheet不存在
				if (null == dateSheet) {
					throw new CherryException("EBS00030",new String[] { CherryConstants.UNQDETAILS_SHEET_NAME });
				}

				//判断Excel里的版本号与常量里的版本号是否一致。
				String version = sheets[0].getCell(1, 0).getContents().trim();
				if (!CherryConstants.UNQDETAILS_EXCEL_VERSION.equals(version)) {
					throw new CherryException("EBS00103");
				}

				// 取得系统时间
				String sysDate = binolptunq03_Service.getSYSDate();
				// 系统时间设定
				map.put(CherryConstants.CREATE_TIME, sysDate);
				// 处理总件数
				int totalCount = 0;
				// 成功处理总件数
				int successCount = 0;
				// 失败总件数
				int failCount = 0;
//				// 是否是品牌帐号
//				boolean isBrandUser = (Boolean) map.get("isBrandUser");
				// 用户组织ID
				int organizationInfoId = (Integer) map.get(CherryConstants.ORGANIZATIONINFOID);

				int brandInfoId = 0;
				String brand_code = "";
				List<Map<String, Object>> brandInfoList = null;
//				// 如果登录用户为品牌用户则查询出该品牌信息，如果是组织帐号则查询出该组织下的所有品牌信息
//				if (isBrandUser) {
//					brandInfoId = (Integer) map.get(CherryConstants.BRANDINFOID);
//					brand_code = binolcm05_Service.getBrandCode(brandInfoId);
//					if (brandInfoId == 0 || "".equals(brand_code)) {
//						throw new CherryException("EBS00052");
//					}
//				} else {
//					Map<String, Object> paramMap = new HashMap<String, Object>();
//					// 所属组织
//					paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
//					// 语言
//					paramMap.put(CherryConstants.SESSION_LANGUAGE,map.get(CherryConstants.SESSION_LANGUAGE));
//					// 取得品牌List
//					brandInfoList = binOLCM05_BL.getBrandInfoList(paramMap);
//					if (null == brandInfoList || brandInfoList.isEmpty()) {
//						throw new CherryException("EBS00053");
//					}
//				}
				int sheetLength = dateSheet.getRows();

				/** 激活状态CODE值LIST */
				List<Map<String, Object>> keyUnqList = CodeTable.getCodes("1395");
				// 声明一个list用来存放解析过程中出错的唯一码明细，并记录下相应的错误。
				List<Map<String, Object>> errorUnqList = new ArrayList<Map<String, Object>>();
				// 声明一个list用来存放解析过程中未出错的唯一码明细
				List<Map<String, Object>> successUnqList = new ArrayList<Map<String, Object>>();
				// 导入空行数据限制
		        int  sum=0;
				// 循环导入重点系统活动内容数据
				for (int r = 3; r < sheetLength; r++) {

					// 错误信息
					Map<String, Object> errorInfo = new HashMap<String, Object>();

					// 错误区分，记录该行数据是否有错误，默认为没有错误
					boolean errorFlag = false;

					// 产商编码
					String unitCode = dateSheet.getCell(0, r).getContents().trim();
					// 产品条码
					String barCode = dateSheet.getCell(1, r).getContents().trim();
					// 商品名称
					String nameTotal = dateSheet.getCell(2, r).getContents().trim();
					// 积分唯一码
					String pointUniqueCode = dateSheet.getCell(3, r).getContents().trim();
					// 关联唯一码
					String relUniqueCode = dateSheet.getCell(4, r).getContents().trim();
					// 箱码
					String boxCode  = dateSheet.getCell(5, r).getContents().trim();
					// 激活状态
					String activationStatus = dateSheet.getCell(6, r).getContents().trim();
					
					// 连续十行数据为空，程序认为sheet内有效行读取结束
					if ( "".equals(unitCode)
							&& "".equals(barCode) 
							&& "".equals(nameTotal)
							&& "".equals(pointUniqueCode)
							&& "".equals(relUniqueCode)
							&& "".equals(boxCode)
							&& "".equals(activationStatus)
							) {
						sum++;
						if (sum >= 10) {
							break;
						} else {
							continue;
						}
					}
					
					sum=0;
					// 处理总件数+1
					totalCount++;
					Map<String, Object> countInfo = new HashMap<String, Object>();
					countInfo.putAll(map);
					
					// 厂商编码
					countInfo.put("unitCode", unitCode);
					// 产品条码
					countInfo.put("barCode", barCode);
					// 产品名称
					countInfo.put("nameTotal", nameTotal);
					// 积分唯一码
					countInfo.put("pointUniqueCode", pointUniqueCode);
					// 关联唯一码
					countInfo.put("relUniqueCode", relUniqueCode);
					// 箱码
					countInfo.put("boxCode", boxCode);
					// 激活状态
					countInfo.put("activationStatus", activationStatus);
					// 数据行号
					countInfo.put("rowNo", r + 1);

					// 处理品牌code（必填）
					countInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
					int optFlag = 1;
					
					// 唯一码基础信息
					Map<String, Object> unqInfo = new HashMap<String, Object>();
					// 产商编码基础信息
					Map<String, Object> unitCodeMap = new HashMap<String, Object>();
	
					// 校验积分唯一码和关联唯一码
			   		if(CherryChecker.isNullOrEmpty(pointUniqueCode) && CherryChecker.isNullOrEmpty(relUniqueCode)){
			   			errorFlag = true;
						// 校验积分唯一码和关联唯一码不能同时为空
						countInfo.put("pCodeAndRCodeNullError", true);
						errorInfo.put("pCodeAndRCodeNullError", true);
			   			
			   		}else {
			   			// 积分唯一码和关联唯一码不同时为空时，获取相关信息
			   			unqInfo = binolptunq03_Service.getUnqInfo(countInfo);
				   		if(CherryChecker.isNullOrEmpty(pointUniqueCode)){
	                        // 积分唯一码为空时校验关联唯一码
				   			if(unqInfo == null){
				   				errorFlag = true;
								// 关联唯一码在系统中不存在
								countInfo.put("relUniqueCodeError", true);
								errorInfo.put("relUniqueCodeError", true);
				   			}
			   			
			   		}else if(CherryChecker.isNullOrEmpty(relUniqueCode)){
			   		    // 关联唯一码为空时校验积分唯一码
			   			if(unqInfo == null){
			   				errorFlag = true;
							// 积分唯一码在系统中不存在
							countInfo.put("pointUniqueCodeError", true);
							errorInfo.put("pointUniqueCodeError", true);
			   			}
			   		}else{
			   		    // 积分唯一码，关联唯一码均不为空时必须一一对应
			   			if(unqInfo == null){
			   				errorFlag = true;
							// 积分唯一码和关联唯一码不匹配
							countInfo.put("pCodeAndRCodeError", true);
							errorInfo.put("pCodeAndRCodeError", true);
			   			}
			   		  }
			   	    }
			   		
			   		// 根据积分唯一码和关联唯一码渠道的数据不为空时，校验激活状态和使用状态
			   		if(unqInfo!=null){
			   		// 使用状态
			   		String  useStatusS = ConvertUtil.getString(unqInfo.get("useStatus"));
			   	    // 激活状态
			   		String activationStatusS = ConvertUtil.getString(unqInfo.get("activationStatus"));
			   		
			   		// 根据积分唯一码或者关联唯一码查询出的使用状态为已使用的无法维护
			   		if("1".equals(useStatusS)){
			   			errorFlag = true;
						// 使用状态为已使用的无法维护
						countInfo.put("useStatusError", true);
						errorInfo.put("useStatusError", true);
			   		}

			   		// 根据积分唯一码或者关联唯一码查询出的激活状态为已激活的无法维护
			   		if("1".equals(activationStatusS)){
			   			errorFlag = true;
						// 激活状态为已激活的无法维护
						countInfo.put("activationStatusSError", true);
						errorInfo.put("activationStatusSError", true);
			   		}
			   		}
			   	    // 导入的激活状态校验
				  checkCodeVal(optFlag, activationStatus, "activationStatus",countInfo, errorInfo, keyUnqList,true);
				
				    // 产商编码校验  
				  if(!CherryChecker.isNullOrEmpty(unitCode)){
					  // 厂商编码不为空时校验厂商编码在系统是否存在
					  unitCodeMap = binolptunq03_Service.getUnitCodeInfo(countInfo);
					  if(unitCodeMap == null){
							errorFlag = true;
							// 厂商编码在系统中不存在
							countInfo.put("unitCodeError", true);
							errorInfo.put("unitCodeError", true);
					  }else{
						  // 将产品厂商ID放入countInfo中便于后面唯一码维护
						  String BIN_ProductVendorID = ConvertUtil.getString(unitCodeMap.get("BIN_ProductVendorID"));
						  countInfo.put("BIN_ProductVendorID", BIN_ProductVendorID);
					  }
					  
				  }else{
						// unitCode导入厂商编码为空，导入激活状态为激活
				   		if("激活".equals(activationStatus)){
				   			errorFlag = true;
							// 产商编码为空时，导入激活状态不能为激活
							countInfo.put("unitCodeAndActError", true);
							errorInfo.put("unitCodeAndActError", true);
				   			
				   		}

					  // 产商编码为空将产品厂商ID改为空置
					  countInfo.put("BIN_ProductVendorID", null);
					  
				  }
			   		
					// 如果该行没有发生错误，则将唯一码维护信息写入新后台否则记录下错误的唯一码维护信息
					if (errorInfo.size()<=0) {
						// 唯一码维护信息处理
						try {
							int flag = binolptunq03_Service.updateUnqCode(countInfo);
							if (flag == 1) {
								successCount++;
								successUnqList.add(countInfo);
							} else {
								failCount++;
								String jsonStr = CherryUtil.map2Json(countInfo);
								countInfo.put("jsonStr", jsonStr);
								errorUnqList.add(countInfo);
							}
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							failCount++;
							String jsonStr = CherryUtil.map2Json(countInfo);
							countInfo.put("jsonStr", jsonStr);
							errorUnqList.add(countInfo);
						}
					} else {
						failCount++;
						String jsonStr = CherryUtil.map2Json(countInfo);
						countInfo.put("jsonStr", jsonStr);
						errorUnqList.add(countInfo);
					}
					errorInfo.put("rowNo", r + 1);
					errorInfos.add(errorInfo);
				}

				// 保存统计信息
				Map<String, Object> statisticsInfo = new HashMap<String, Object>();
				statisticsInfo.put("totalCount", totalCount);
				statisticsInfo.put("successCount", successCount);
				statisticsInfo.put("failCount", failCount);

				// 作为结果返回
				Map<String, Object> resutlMap = new HashMap<String, Object>();
				resutlMap.put("statisticsInfo", statisticsInfo);
				resutlMap.put("errorUnqList", errorUnqList.isEmpty() ? null: errorUnqList);
				resutlMap.put("errorInfo", errorInfos);
				resutlMap.put("successUnqList", successUnqList.isEmpty() ? null: successUnqList);

				// errorList 赋值，zcf
				errorList = errorUnqList;
				return resutlMap;
	}
	
	/**
	 * 共通参数
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 作成者
		baseMap.put(CherryConstants.CREATEDBY,map.get(CherryConstants.CREATEDBY));
		// 更新者
		baseMap.put(CherryConstants.UPDATEDBY,map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTUNQ03");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTUNQ03");
		baseMap.put(CherryConstants.ORGANIZATIONINFOID,map.get(CherryConstants.ORGANIZATIONINFOID));
		baseMap.put(CherryConstants.BRANDINFOID,map.get(CherryConstants.BRANDINFOID));
		baseMap.put(CherryConstants.SESSION_LANGUAGE,map.get(CherryConstants.SESSION_LANGUAGE));
		return baseMap;
	}

	/**
	 * CODE检查
	 * 
	 * @param optFlag
	 * @param codeVal
	 * @param codeName
	 * @param countInfo
	 * @param errorInfo
	 * @param codeList
	 * @param mustFlag
	 * @return
	 */
	private boolean checkCodeVal(int optFlag, String codeVal, String codeName,
			Map<String, Object> countInfo, Map<String, Object> errorInfo,
			List<Map<String, Object>> codeList,boolean mustFlag) {
		boolean errorFlag = false;
		
		
		// CODE验证
		if (!CherryChecker.isNullOrEmpty(codeVal)) {
			if (2 == optFlag
					&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(codeVal)) {
				// 更新状态下，名称为Cherry_Clear,则belongFaction更新为null
				countInfo.put(codeName, cherry_clear);
			} else if (1 == optFlag
					&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(codeVal)) {
				// 新增状态下，名称为cherry_clear,则belongFaction属性为空
				countInfo.put(codeName, null);
			} else {
				// 其他情况：数据与“CHERRY_CLEAR”无关，判断是否为CODE对应的VALUES值
				boolean isCodeValue = false;
				for (Map<String, Object> codeMap : codeList) {
					if (codeVal.equals(codeMap.get("Value"))) {
						isCodeValue = true;
						countInfo.put(codeName, codeMap.get("CodeKey"));
						break;
					}
				}
				if (!isCodeValue) {
					// 不为CODE对就的值则报错
					// 标志该行数据有错误
					errorFlag = true;
					// 标志柜台级别不存在
					countInfo.put(codeName + "Error", codeVal);
					errorInfo.put(codeName + "Error", true);
				}
			}
		} else {
			if (mustFlag) {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志柜台等级有错误
				countInfo.put(codeName + "Error", codeVal);
				errorInfo.put(codeName + "Error", true);
			} else {
				countInfo.put(codeName, null);				
			}
			
		}
		countInfo.put(codeName + "Excel", codeVal);
		return errorFlag;
	}

	@SuppressWarnings("unchecked")
	public byte[] exportExcel(Map<String, Object> map,List errorList, int totalCount, int successCount, int failCount) throws Exception {
		List<Map<String, Object>> dataList = errorList;
	    String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
		    		{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.manufacturerCode"), "15", "", "" },
		            { "barCode",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productBarCode"), "15", "", "" },
		            { "nameTotal",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.productName"), "20", "", "" },
		            { "pointUniqueCode", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.pointUnqCode" ), "20", "", "" },
		            { "relUniqueCode",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.relUnqCode"), "20", "", "" },
		            { "boxCode",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.boxCode"), "20", "", "" },
		            { "activationStatus", binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language,  "PTUNQ.activationStatus"), "10", "", "1395" },
		            { "errorInfoList",  binOLCM37_BL.getResourceValue("BINOLPTUNQ03", language, "PTUNQ.errorInfoList"), "50", "", "" }
		    };
		    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		    ep.setMap(map);
		    ep.setArray(array);
		    ep.setBaseName("BINOLPTUNQ03");
		    ep.setSheetLabel("sheetName");
		    ep.setSearchCondition("总共维护数据"+totalCount+"条，成功维护"+successCount+"条，维护失败"+failCount+"条，以下是维护失败详细数据和原因");
		    ep.setDataList(dataList);
		    return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
	
}
