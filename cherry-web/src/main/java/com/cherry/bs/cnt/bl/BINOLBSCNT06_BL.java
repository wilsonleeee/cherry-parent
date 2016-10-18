/*	
 * @(#)BINOLBSCNT06_BL.java     1.0 2011/05/09		
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
package com.cherry.bs.cnt.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.bs.cha.interfaces.BINOLBSCHA04_IF;
import com.cherry.bs.cnt.service.BINOLBSCNT02_Service;
import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.cnt.service.BINOLBSCNT06_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
/*import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;*/
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
/*import com.cherry.pt.common.ProductConstants;*/
import com.cherry.synchro.bs.interfaces.CounterSynchro_IF;

/**
 * 
 * 	柜台Excel导入处理BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT06_BL {
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	/** 柜台Excel导入处理Service */
	@Resource(name="binOLBSCNT06_Service")
	private BINOLBSCNT06_Service binOLBSCNT06_Service;
	
	/** 添加部门画面Service */
	@Resource(name="binOLBSDEP04_Service")
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	@Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binolcm05Service;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	@Resource(name="CodeTable")
	private CodeTable CodeTable;
	
	/** 取得区域共通BL*/
	@Resource(name="binOLCM08_BL")
	private BINOLCM08_BL binOLCM08_BL;
	
	@Resource(name="binOLBSCHA04_BL")
	private BINOLBSCHA04_IF binOLBSCHA04_BL;
	
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binOLBSEMP04_Service;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource
	private CodeTable code;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource(name="counterSynchro")
	private CounterSynchro_IF counterSynchro;
	
	/** 柜台详细画面Service */
	@Resource(name="binOLBSCNT02_Service")
	private BINOLBSCNT02_Service binOLBSCNT02_Service;
	
	/** 创建柜台画面Service */
	@Resource(name="binOLBSCNT04_Service")
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	/** 发送MQ消息共通处理 IF */
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**WebService 共通BL*//*
	@Resource(name="binOLCM27_BL")
	private BINOLCM27_BL binOLCM27_BL;*/
	
	private final String cherry_clear = "cherry_clear";
	
	private Map<String, Object> comMap;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLBSCNT06_BL.class.getName());
	
	/** 创建柜台画面BL */
	@Resource(name="binOLBSCNT04_BL")
	private BINOLBSCNT04_BL binOLBSCNT04_BL;
	
	private List<Map<String,Object>> errorList = new ArrayList<Map<String,Object>>();
	
	/**
	 * 
	 * 导入柜台处理
	 * 
	 * @param map 导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception {
		// 取得共通参数
		comMap = getComMap(map);
		
		List<Map<String,Object>> errorInfos = new ArrayList<Map<String,Object>>();
		// 取得上传文件path
		File upExcel = (File)map.get("upExcel");
		if(upExcel == null || !upExcel.exists()){
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
			logger.error(e.getMessage(),e);
			throw new CherryException("EBS00041");
		} finally {
			if(inStream != null) {
				// 关闭流
				inStream.close();
			}
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 柜台数据sheet
		Sheet dateSheet = null;
//		String version = null;
		for (Sheet st : sheets) {
			if (CherryConstants.COUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
//			// 判断模板版本号
//			if("字段说明-v1.0.1".equals(st.getName().trim())){
//				version = "v1.0.1";
//			}
			
		}
//		// 导入文件版本不一致
//		if(null == version){
//			throw new CherryException("EBS00108");
//		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		
	    //每次文件有改动时，版本号加1，判断Excel里的版本号与常量里的版本号是否一致。
        String version = sheets[0].getCell(1, 0).getContents().trim();
        if(!CherryConstants.COUNTER_EXCEL_VERSION.equals(version)){
              throw new CherryException("EBS00103");
        }
		
		// 取得系统时间
		String sysDate = binOLBSCNT06_Service.getSYSDate();
		// 系统时间设定
		map.put(CherryConstants.CREATE_TIME, sysDate);
		//处理总件数
		int totalCount = 0;
		//成功处理总件数
		int successCount = 0;
		//失败总件数
		int failCount = 0;
		//是否是品牌帐号
		boolean isBrandUser = (Boolean) map.get("isBrandUser");
		//用户组织ID
		int organizationInfoId = (Integer)map.get(CherryConstants.ORGANIZATIONINFOID);
		
		int brandInfoId = 0;
		String brand_code = "";
		List<Map<String,Object>> brandInfoList = null;
		//如果登录用户为品牌用户则查询出该品牌信息，如果是组织帐号则查询出该组织下的所有品牌信息
		if(isBrandUser){
			brandInfoId = (Integer)map.get(CherryConstants.BRANDINFOID);
			brand_code = binolcm05Service.getBrandCode(brandInfoId);
			if(brandInfoId==0 || "".equals(brand_code)){
				throw new CherryException("EBS00052");
			}
		}else{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			// 所属组织
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			// 语言
			paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(paramMap);
			if(null == brandInfoList || brandInfoList.isEmpty()){
				throw new CherryException("EBS00053");
			}
		}
		int sheetLength = dateSheet.getRows();
		
		//声明一个list用来存放解析过程中出错的柜台信息，并记录下相应的错误。
		List<Map<String,Object>> errorCounterList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> tempMap = new HashMap<String,Object>();
		/**所属系统CODE值LIST*/
		List<Map<String, Object>> belongFactionList = CodeTable.getCodes("1309");
		/**柜台级别CODE值LIST*/
		List<Map<String, Object>> counterLevelList = CodeTable.getCodes("1032");
		/**柜台状态CODE值LIST*/
		List<Map<String, Object>> counterStatusList = CodeTable.getCodes("1030");
		
		List<String> sendBASMQList = new ArrayList<String>(); 
		// 循环导入柜台信息
		for (int r = 4; r < sheetLength; r++) {
			
			//错误信息
			Map<String, Object> errorInfo = new HashMap<String, Object>();
			
			//错误区分，记录该行数据是否有错误，默认为没有错误
			boolean errorFlag = false;
			// 品牌代码（A）
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 柜台编号（B）
			String counterCode = dateSheet.getCell(1, r).getContents().trim();
			// 柜台中文名称（C）
			String counterName = dateSheet.getCell(2, r).getContents().trim();
			//柜台类型 正式柜台\测试柜台（D）
			String counterKind = dateSheet.getCell(3, r).getContents().trim();
			//柜台级别 根据code1032（E）
			String counterLevel = dateSheet.getCell(4, r).getContents().trim();
			// 柜台状态 根据code1030（F）
			String status = dateSheet.getCell(5, r).getContents().trim();
			//柜台主管code（G）
			String basCode = dateSheet.getCell(6, r).getContents().trim();
			//柜台主管名称（H）
			String basName = dateSheet.getCell(7, r).getContents().trim();
			//区域code（I）
//			String regionCode = dateSheet.getCell(6, r).getContents().trim();
			//区域名称（I）
			String regionName = dateSheet.getCell(8, r).getContents().trim();
			//省code（K）
//			String provinceCode = dateSheet.getCell(7, r).getContents().trim();
			//省名称（J）
			String provinceName = dateSheet.getCell(9, r).getContents().trim();
			//城市code（M）
//			String cityCode = dateSheet.getCell(9, r).getContents().trim();
			//城市名称（K）
			String cityName = dateSheet.getCell(10, r).getContents().trim();
			// 所属系统名称(L)
			String belongFactionName = dateSheet.getCell(11, r).getContents().trim();
			//渠道名称（M）
			String channelName = dateSheet.getCell(12, r).getContents().trim();
			//经销商code（N）
			String resellerCode = dateSheet.getCell(13, r).getContents().trim();
			//经销商名称（O）
			String resellerName = dateSheet.getCell(14, r).getContents().trim();
			//柜台分类（P）
			String counterCategory = dateSheet.getCell(15, r).getContents().trim();
			//商场名称（Q）
			String mallName = dateSheet.getCell(16, r).getContents().trim();
			//柜台英文名称（R）
			String foreignName = dateSheet.getCell(17, r).getContents().trim();
			//柜台员工数（R）
			String employeeNum = dateSheet.getCell(18, r).getContents().trim();
			//柜台面积（S）
			String counterSpace = dateSheet.getCell(19, r).getContents().trim();
			//柜台地址（T）
			String address = dateSheet.getCell(20, r).getContents().trim();
			//柜台电话（U）
			String counterTelephone = dateSheet.getCell(21, r).getContents().trim();
			//地理位置经度（V）
			String longitude = dateSheet.getCell(22, r).getContents().trim();
			//地理位置纬度（W）
			String latitude = dateSheet.getCell(23, r).getContents().trim();
			// 是否有POS机(X)
			String posFlag = dateSheet.getCell(24, r).getContents().trim();
			// 商圈(X)
			String busDistrict = dateSheet.getCell(25, r).getContents().trim();
			//柜台类型
			String managingType2 = dateSheet.getCell(26,r).getContents().trim();
			// 银联设备号(AA)
			String equipmentCode = dateSheet.getCell(27, r).getContents().trim();
						
			// 整行数据为空，程序认为sheet内有效行读取结束
			if ("".equals(brandCode) && "".equals(counterCode)
					&& "".equals(counterName) && "".equals(counterKind)
					&& "".equals(basCode) && "".equals(basName)
					//&& "".equals(regionCode) 
					&& "".equals(regionName)
//					&& "".equals(provinceCode) 
					&& "".equals(provinceName)
//					&& "".equals(cityCode) 
					&& "".equals(cityName) && "".equals(belongFactionName)
					&& "".equals(channelName)&& "".equals(resellerCode)
					&& "".equals(resellerName)&& "".equals(counterCategory)
					&& "".equals(mallName)&& "".equals(foreignName)
					&& "".equals(address) && "".equals(counterTelephone)
					&& "".equals(longitude) && "".equals(latitude)
					&& "".equals(posFlag)&& "".equals(counterLevel)
					&& "".equals(status) && "".equals(employeeNum)
					&& "".equals(equipmentCode) && "".equals(managingType2)
					&& "".equals(busDistrict)) {
				break;
			}
			//处理总件数+1
			totalCount++;
			
			Map<String, Object> countInfo = new HashMap<String, Object>();
						
			countInfo.putAll(map);
			// 数据行号
			countInfo.put("rowNo", r+1);
			
			//处理品牌code（必填）
			if("".equals(brandCode)){
				//标志该行数据有错误
				errorFlag = true;
				//标志品牌代码有错误
				countInfo.put("brandCodeError", true);
				errorInfo.put("brandCodeError", true);
			}else{
				//如果是品牌帐号，则验证输入的品牌信息是否正确
				if(isBrandUser){
					//如果品牌代码不匹配则记录
					if(!brand_code.equals(brandCode)){
						//标志该行数据有错误
						errorFlag = true;
						//标志品牌代码有错误
						countInfo.put("brandCodeError", true);
						errorInfo.put("brandCodeError", true);
					}
				}else{
					boolean flag = false;
					//循环品牌list，比对是否匹配
					for(Map<String,Object>temp : brandInfoList){
						if(temp.get("brandCode").equals(brandCode)){
							flag = true;
							Map<String,Object> param = new HashMap<String,Object>();
							//组织ID
							param.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
							//品牌Code
							param.put("brandCode", brandCode);
							//调用共通取得品牌ID
							brandInfoId = binOLCM05_BL.getBrandInfoId(param);
							break;
						}
					}
					//如果匹配记录下brandcode，不匹配记录错误
					if(!flag){
						//标志该行数据有错误
						errorFlag = true;
						//标志品牌代码有错误
						countInfo.put("brandCodeError", true);
						errorInfo.put("brandCodeError", true);
					}
				}
			}
			countInfo.put("brandCode", brandCode);
			countInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			
			
			// 柜台编号生成规则
			String cntCodeRule = binOLCM14_BL.getConfigValue("1139", String.valueOf(organizationInfoId), String.valueOf(brandInfoId));
			
			//处理柜台名称（必填）
			counterName = CherryUtil.convertSpecStr(counterName);
			countInfo.put("counterName", counterName);
			if("".equals(counterName) || counterName.length() > 50 || CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterName)){
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台名称有错误
				countInfo.put("counterNameError", true);
				errorInfo.put("counterNameError", true);
				
				// 名称没有的情况，当前柜台必然不会在新后台进行处理。为了让下面的程序验证程序正常运行，将counterCode设值，无实际业务意义。
				countInfo.put("counterCode", "invalidCode");
			} else {
				// ======================== 柜台名称编码处理逻辑  ================================
				// **名称存在的情况下，确定Code是否存在 ********************//
				// *****Code 不存在时，通过名称到柜台表查询是否存在？       ********************
				// **********若存在则使用查询到的Code,*************************************
				// **********若不存在，则看系统配置项是否开户自动生成柜台代码功能，.*********
				// ***************若开启自动生成，*****************************************
				// ***************若未开启则报错******************************************
				// *****Code存在时，即使通过名称在柜台表查询到数据，也以xls的柜台Code为准*********
				
				// Code 不存在时，通过名称到柜台表查询是否存在？
				if("".equals(counterCode)){
					
					Map<String,Object> paraCouMap = new HashMap<String, Object>();
					paraCouMap.put("counterName", counterName);
					paraCouMap.putAll(comMap);
					Map<String,Object> resultCouMap = (Map<String,Object>)binOLBSCNT06_Service.getCounterId(countInfo);
					
					// 若存在则使用查询到的Code,
					if(null != resultCouMap){
						countInfo.put("counterCode", resultCouMap.get("CounterCode"));
					}
					// 若不存在，则看系统配置项是否开户自动生成柜台代码功能，
					else { 
						
						// 普通规则
						if(CherryConstants.CNTCODE_RULE1.equals(cntCodeRule)){
							boolean isConfigOpen = false;
							//查询系统配置中是否开启自动生成柜台代码功能
							isConfigOpen = binOLCM14_BL.isConfigOpen("1009",organizationInfoId,brandInfoId);
							if(!isConfigOpen){
								//标志该行数据有错误
								errorFlag = true;
								//标志柜台code有错误
								countInfo.put("counterCodeError", true);
								errorInfo.put("counterCodeError", true);
							}else {
								//调用共通自动生成柜台code
								Map<String, Object> codeMap = code.getCode("1120","2");
								Map<String,Object> autoMap = new HashMap<String,Object>();
								autoMap.put("type", "2");
								autoMap.put("length", codeMap.get("value2"));
								autoMap.putAll(comMap);
								String counterCodeAuto = (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(autoMap);
								
								countInfo.put("counterCode", counterCodeAuto);
							}
							
						}
						// 自然堂规则
						else if(CherryConstants.CNTCODE_RULE2.equals(cntCodeRule)){
							countInfo.put("counterCode", counterCode);
//							convertRPC(errorInfo, errorFlag, regionName, provinceName, cityName, countInfo, optFlag)
							errorFlag = validCityByCntCode(errorInfo,
									errorFlag, cityName, countInfo);
						}
						
					}
					
				} else {
					if(counterCode.length() > 50 || CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterCode)){
						//标志该行数据有错误
						errorFlag = true;
						//标志柜台code有错误
						countInfo.put("counterCodeError", true);
						errorInfo.put("counterCodeError", true);
						countInfo.put("counterCode", counterCode);
					} else {
						
						// 普通规则
						if(CherryConstants.CNTCODE_RULE1.equals(cntCodeRule)){
							// Code存在时，即使通过名称在柜台表查询到数据，也以xls的柜台Code为准
							countInfo.put("counterCode", counterCode);
						}
						// 自然堂规则
						else if(CherryConstants.CNTCODE_RULE2.equals(cntCodeRule)){
							
							Map<String,Object> pcouMap = new HashMap<String, Object>();
							pcouMap.put("counterCode", counterCode);
							pcouMap.putAll(comMap);
							Map<String,Object> couMap = (Map<String,Object>)binOLBSCNT06_Service.getCounterId(pcouMap);
							// xls填写的柜台编号在新后台存在，则使用。否则使用自然堂规则生成
							if((null != couMap)){
								countInfo.put("counterCode", counterCode);
							}else {
								countInfo.put("counterCode", counterCode);
								errorFlag = validCityByCntCode(errorInfo, errorFlag, cityName, countInfo);
							}
						}
					}
				}
			}
			errorInfo.put("counterCode", counterCode);
			
			// 查询新后台是否已经存在当前要导入的柜台：存在则更新柜台，反之新增
			Map<String,Object> pcouMap = new HashMap<String, Object>();
			pcouMap.put("counterCode", countInfo.get("counterCode"));
			int optFlag = 1;
			Map<String,Object> couMap = null;
			if(ConvertUtil.getString(countInfo.get("counterCode")).equals("") ){
				countInfo.put("counterCode", "");
				optFlag = 1;
			}else {
				pcouMap.putAll(comMap);
				couMap = (Map<String,Object>)binOLBSCNT06_Service.getCounterId(pcouMap);
				optFlag = (null == couMap) ? 1 : 2;
			}
			countInfo.put("optFlag", optFlag);
			// 添加柜台已绑定的BAS,用于发送BAS信息MQ
			if(optFlag == 2){
				String oldBasID = binOLBSEMP04_Service.getEmployeeDepart(couMap);
				if(null != oldBasID && !sendBASMQList.contains(oldBasID) && !CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(oldBasID)){
					sendBASMQList.add(oldBasID);
				}
			}
			
			//验证柜台测试区分（必填）0：正式柜台；1：测试柜台
			if(counterKind.equals(CherryConstants.COUNTERKIND_OFFICIAL)){
				countInfo.put("counterKind", 0);
			}else if(counterKind.equals(CherryConstants.COUNTERKIND_TEST)){
				countInfo.put("counterKind", 1);
			}else{
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台测试区分有错误
				countInfo.put("counterKindError", true);
				errorInfo.put("counterKindError", true);
			}
			countInfo.put("counterKindShow", counterKind);
			
			//验证柜台主管code和柜台主管名称，除非code和名称都不填，否则名称是必填项
			if(!"".equals(basName)||!"".equals(basCode)){ 
				if(2 == optFlag
						&& (CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(basName) || CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(basCode))){
					countInfo.put("employeeId", cherry_clear);
				} else {
					
					Map<String,Object> validateMap = new HashMap<String,Object>();
					validateMap.putAll(comMap);
					if("".equals(basName)){
						//标志该行数据有错误
						errorFlag = true;
						// 除非code和名称都不填，否则名称是必填项
						countInfo.put("basNameError", true);
						errorInfo.put("basNameError", true);
					} else {
						//如果basCode和basName都存在的场合同时去匹配，否则只用basName去匹配
						validateMap.put("employeeCode", !"".equals(basCode)? basCode:null);
						validateMap.put("employeeName", !"".equals(basName)? basName:null);
						//调用共通取得柜台主管信息
						List<Map<String,Object>> basInfo = binOLCM00_BL.getEmplyessInfo(validateMap);
						//如果没有查询到该主管
						if(null == basInfo || basInfo.isEmpty() || basInfo.size() > 1){
							//标志该行数据有错误
							errorFlag = true;
							//标志柜台主管code有错误或者柜台主管名称有错误
							countInfo.put("basCodeError", true);
							countInfo.put("basNameError", true);
							errorInfo.put("basCodeError", true);
							errorInfo.put("basNameError", true);
						}else{
							countInfo.put("employeeId", basInfo.get(0).get("employeeId"));
						}
					}
				}
			}
			countInfo.put("basCode", basCode);
			countInfo.put("basName", basName);
			
			errorFlag = convertRPC(errorInfo, errorFlag, regionName,
					provinceName, cityName, countInfo, optFlag);
			
			// 验证所属系统信息
			countInfo.put("belongFactionName", belongFactionName);
			if(!"".equals(belongFactionName)) {
				if (2 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(belongFactionName)) {
					// 更新状态下，所属系统名称为Cherry_Clear,则belongFaction更新为null
					countInfo.put("belongFaction", cherry_clear);
				} else if(1 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(belongFactionName)){
					// 新增状态下，名称为cherry_clear,则belongFaction属性为空
					countInfo.put("belongFaction", null);
				} else {
					// 其他情况：数据与“CHERRY_CLEAR”无关，判断是否为【1309】对应的VALUES值
					boolean isBelongFactionValue = false;
					for(Map<String, Object> belongFactionMap : belongFactionList){
						if(belongFactionName.equals(belongFactionMap.get("Value"))){
							isBelongFactionValue = true;
							countInfo.put("belongFaction", belongFactionMap.get("CodeKey"));
							break;
						}
					}
					if(!isBelongFactionValue) {
						// 不为【1309】CODE对就的值则报错
						//标志该行数据有错误
						errorFlag = true;
						//标志所属系统不存在
						countInfo.put("belongFactionNameError", belongFactionName);
						errorInfo.put("belongFactionNameError", true);
					}
				}
			}
			
			//验证渠道信息
			countInfo.put("channelName", channelName);
			if(!"".equals(channelName)){
				if (2 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(channelName)) {
					// 更新状态下，渠道名称为Cherry_Clear,则channelId更新为null
					countInfo.put("channelId", cherry_clear);
				} else if(1 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(channelName)){
					// 新增状态下，名称为cherry_clear,则channelId属性为空
					countInfo.put("channelId", null);
				} else {
					if(channelName.length() > 50){
						//标志该行数据有错误
						errorFlag = true;
						//标志渠道名称有错误
						countInfo.put("channelNameError", channelName);
						errorInfo.put("channelNameError", true);
					}else{
						Map<String,Object> validateMap = new HashMap<String,Object>();
						validateMap.putAll(comMap);
						//渠道名称
						validateMap.put("channelName", channelName);
						//调用共通取得渠道信息
						List<Map<String,Object>> channelInfoList = binOLCM00_BL.getChannelList(validateMap);
						
						if(channelInfoList != null && !channelInfoList.isEmpty()){
							if(channelInfoList.size()>1){
								//标志该行数据有错误
								errorFlag = true;
								//标志渠道名称有错误
								countInfo.put("channelNameError", channelName);
								errorInfo.put("channelNameError", true);
							}else{
								countInfo.put("channelId", channelInfoList.get(0).get("channelId"));
							}
						} else {
							// 取得渠道ID
							errorFlag = setChannelId(countInfo, errorInfo);
						}
					}
				}
			}
			
			// 验证经销商code和经销商名称，除非code和名称都不填，否则名称是必填项
			countInfo.put("resellerCode", resellerCode);
			countInfo.put("resellerName", resellerName);
			if(!"".equals(resellerCode) || !"".equals(resellerName)){
				
				if (2 == optFlag
						&& (CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerCode) 
								|| CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerName))) {
					// 更新状态下，经销商名称或经销商Code为Cherry_Clear,则resellerInfoId更新为null
					countInfo.put("resellerInfoId", cherry_clear);
				} else if (1 == optFlag
						&& (CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerCode) 
								|| CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerName))){
					// 新增状态下，经销商名称或经销商Code为Cherry_Clear,则resellerInfoId属性新增为null
					countInfo.put("resellerInfoId", null);
				} else {
					
					if("".equals(resellerName) || resellerName.length()>30){
						//标志该行数据有错误
						errorFlag = true;
						//标志经销商名称有错误
						countInfo.put("resellerNameError", resellerName);
						errorInfo.put("resellerNameError", true);
					}else{
						// name存在的情况下，确定code是否存在
						// 		// code存在的情况下，通过code查询是否存在于经销商表
						// 			 	// 存在，则使用通过code查询到的经销商ID
						// 				// 不存在，则新增一条经销商数据
						// 		// code不存在的情况下，通过name查询是否存在于经销商表
						//			// 存在，是否大于1条
						// 					// 是，报错
											// 否，则使用nbame查询 到的经销商ID
						//			// 不存在，则新增一条经销商数据(code自动生成)
						
						Map<String,Object> validateMap = new HashMap<String,Object>();
						validateMap.putAll(comMap);
						
						if(!"".equals(resellerCode)){
							/**
							 * 票号：WITPOSQA-16199
							 * 经销商CODE扩大为15位限制
							 */
							if(resellerCode.length()>15){
								//标志该行数据有错误
								errorFlag = true;
								//标志经销商名称有错误
								countInfo.put("resellerCodeError", resellerName);
								errorInfo.put("resellerCodeError", true);
							} else {
								// code存在的情况下，通过code查询是否存在于经销商表
								validateMap.put("resellerCode", resellerCode);
								List<Map<String,Object>> resellerList = binOLCM00_BL.getResellerInfoList(validateMap);
								
								if(resellerList != null && !resellerList.isEmpty()){
									// 存在，则使用通过code查询到的经销商ID
									countInfo.put("resellerInfoId", resellerList.get(0).get("resellerInfoId"));
								} else {
									// 不存在，则新增一条经销商数据
									errorFlag = setResellerId(countInfo, errorInfo);
								}
							}
						} else {
							// code不存在的情况下，通过name查询是否存在于经销商表
							validateMap.put("resellerName", resellerName);
							List<Map<String,Object>> resellerList = binOLCM00_BL.getResellerInfoList(validateMap);
							if(resellerList != null && !resellerList.isEmpty()){
								// 存在，是否大于1条
								if(resellerList.size()>1){
									// 是，报错
									
									//标志该行数据有错误
									errorFlag = true;
									//标志经销商名称有错误
									countInfo.put("resellerNameError", resellerName);
									errorInfo.put("resellerNameError", true);
								} else {
									// 否，则使用nbame查询 到的经销商ID
									countInfo.put("resellerInfoId", resellerList.get(0).get("resellerInfoId"));
								}
							} else {
								// 不存在，则新增一条经销商数据(code自动生成)
								errorFlag = setResellerId(countInfo, errorInfo);
							}
						}
						
					}
				}
				
			}

			//验证柜台分类
			if(!"".equals(counterCategory)&&counterCategory.length() > 100){
				//标志该行数据有错误
				errorFlag = true;
				//标志经柜台分类有错
				countInfo.put("counterCategoryError", counterCategory);
				errorInfo.put("counterCategoryError", true);
			}
			countInfo.put("counterCategory", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterCategory) ? cherry_clear :  counterCategory);
			
			//验证商场名称
			countInfo.put("mallName", mallName);
			if(!"".equals(mallName)){
				if(2 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(mallName)){
					// 更新状态下，商场名称为Cherry_Clear,则mallInfoId更新为null
					countInfo.put("mallInfoId", cherry_clear);
				} else if (1 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(mallName)){
					// 更新状态下，商场名称为Cherry_Clear,则mallInfoId属性为null
					countInfo.put("mallInfoId", null);
				} else {
					if(mallName.length()>30){
						//标志该行数据有错误
						errorFlag = true;
						//标志商场名称有错误
						countInfo.put("mallNameError", true);
						errorInfo.put("mallNameError", true);
					}else{
						Map<String,Object> validateMap = new HashMap<String,Object>();
						validateMap.putAll(comMap);
						validateMap.put("mallName", mallName);
						//调用共通取得商场信息
						List<Map<String,Object>> mallInfoList = binOLCM00_BL.getMallInfoList(validateMap);
						if(mallInfoList != null && !mallInfoList.isEmpty()){
							if(mallInfoList.size()>1){
								//标志该行数据有错误
								errorFlag = true;
								//标志商场名称有错误
								countInfo.put("mallNameError", true);
								errorInfo.put("mallNameError", true);
							}else{
								countInfo.put("mallInfoId", mallInfoList.get(0).get("mallInfoId"));
							}
						} else {
							errorFlag = setMallId(countInfo, errorInfo);
						}
					}
				}
			}
			
			//验证英文名称
			if(!"".equals(foreignName)&&foreignName.length()>50){
				//标志该行数据有错误
				errorFlag = true;
				//标志英文名称有错误
				countInfo.put("foreignNameError", true);
				errorInfo.put("foreignNameError", true);
			}
			countInfo.put("foreignName", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(foreignName) ? cherry_clear :  foreignName);
			
			//验证柜台地址
			if(!"".equals(address)&&address.length()>50){
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台地址有错误
				countInfo.put("addressError", true);
				errorInfo.put("addressError", true);
			}
			countInfo.put("address", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(address) ? cherry_clear :  address);
			
			// 验证柜台电话
			if(!"".equals(counterTelephone)){
				if((counterTelephone.length()>20) || !counterTelephone.matches("^[0-9-\\+()#]+$")){
					//标志该行数据有错误
					errorFlag = true;
					//标志柜台电话有错误
					countInfo.put("counterTelephoneError", true);
					errorInfo.put("counterTelephoneError", true);
				}
			}
			countInfo.put("counterTelephone", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterTelephone) ? cherry_clear :  counterTelephone);
			
			// 验证地理位置经度
			if(!"".equals(longitude)&&longitude.length()>32){
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台经度有错误
				countInfo.put("longitudeError", true);
				errorInfo.put("longitudeError", true);
			}
			countInfo.put("longitude", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(longitude) ? cherry_clear :  longitude);
			
			// 验证地理位置纬度
			if(!"".equals(latitude)&&latitude.length()>32){
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台纬度有错误
				countInfo.put("latitudeError", true);
				errorInfo.put("latitudeError", true);
			}
			countInfo.put("latitude", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(latitude) ? cherry_clear :  latitude);
			
			//是否有POS机 1：是 ， 0：否
			if(CherryChecker.isNullOrEmpty(posFlag)){
				//标志该行数据有错误
				errorFlag = true;
				//标志是否有POS机有错误
				countInfo.put("posFlagError", true);
				errorInfo.put("posFlagError", true);
			}
			if(posFlag.equals(CherryConstants.POSFLAG_YES)){
				countInfo.put(CherryConstants.POSFLAG, 1);
			}else if(posFlag.equals(CherryConstants.POSFLAG_NO)){
				countInfo.put(CherryConstants.POSFLAG, 0);
			}
			countInfo.put("posFlagExcel",posFlag);
			//验证商圈
			if(!"".equals(busDistrict)&&busDistrict.length()>50){
				//标志该行数据有错误
				errorFlag = true;
				//标志商圈有错误
				countInfo.put("busDistrictError", true);
				errorInfo.put("busDistrictError", true);
			}
			countInfo.put("busDistrict", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(busDistrict) ? cherry_clear :  busDistrict);
			
			//验证柜台类型
			if(!"".equals(managingType2)){
				//标志该行数据有错误
				errorFlag = true;
				//标志柜台类型有错误
				countInfo.put("managingType2Error", true);
				errorInfo.put("managingType2Error", true);
			}
			countInfo.put("managingType2", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(managingType2) ? cherry_clear :  managingType2);
			
			//验证银联设备号
			if(!"".equals(equipmentCode)&&equipmentCode.length()>50){
				//标志该行数据有错误
				errorFlag = true;
				//标志银联设备号有错误
				countInfo.put("equipmentCodeError", true);
				errorInfo.put("equipmentCodeError", true);
			}
			countInfo.put("equipmentCode", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(equipmentCode) ? cherry_clear :  equipmentCode);
			
//			//柜台级别 A：0 , B：1, C：2
//			if(counterLevel.equals(CherryConstants.COUNTER_LEVEL_A)){
//				countInfo.put(CherryConstants.COUNTER_LEVEL, 0);
//			}else if(counterLevel.equals(CherryConstants.COUNTER_LEVEL_B)){
//				countInfo.put(CherryConstants.COUNTER_LEVEL, 1);
//			}else if(counterLevel.equals(CherryConstants.COUNTER_LEVEL_C)){
//				countInfo.put(CherryConstants.COUNTER_LEVEL, 2);
//			}else {
//				countInfo.put(CherryConstants.COUNTER_LEVEL, null);
//			}
			
			if(!"".equals(counterLevel)) {
				if (2 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterLevel)) {
					// 更新状态下，所属系统名称为Cherry_Clear,则belongFaction更新为null
					countInfo.put("counterLevel", cherry_clear);
				} else if(1 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterLevel)){
					// 新增状态下，名称为cherry_clear,则belongFaction属性为空
					countInfo.put("counterLevel", null);
				} else {
					// 其他情况：数据与“CHERRY_CLEAR”无关，判断是否为【1032】对应的VALUES值
					boolean isCounterLevelValue = false;
					for(Map<String, Object> counterLevelMap : counterLevelList){
						if(counterLevel.equals(counterLevelMap.get("Value"))){
							isCounterLevelValue = true;
							countInfo.put("counterLevel", counterLevelMap.get("CodeKey"));
							break;
						}
					}
					if(!isCounterLevelValue) {
						// 不为【1032】CODE对就的值则报错
						//标志该行数据有错误
						errorFlag = true;
						//标志柜台级别不存在
						countInfo.put("counterLevelError", counterLevel);
						errorInfo.put("counterLevelError", true);
					}
				}
			}else {
				countInfo.put("counterLevel", null);
			}
			countInfo.put("counterLevelExcel", counterLevel);
			
//			// 柜台状态  0：营业中,1：筹备中,2：停业中,3：装修中,4：关店
//			if(status.equals(CherryConstants.COUNTER_STATUS_0) || CherryChecker.isNullOrEmpty(status)){
//				countInfo.put(CherryConstants.COUNTER_STATUS, 0);
//			}else if(status.equals(CherryConstants.COUNTER_STATUS_1)){
//				countInfo.put(CherryConstants.COUNTER_STATUS, 1);
//			}else if(status.equals(CherryConstants.COUNTER_STATUS_2)){
//				countInfo.put(CherryConstants.COUNTER_STATUS, 2);
//			}else if(status.equals(CherryConstants.COUNTER_STATUS_3)){
//				countInfo.put(CherryConstants.COUNTER_STATUS, 3);
//			}else if(status.equals(CherryConstants.COUNTER_STATUS_4)){
//				countInfo.put(CherryConstants.COUNTER_STATUS, 4);
//			}
			
			if(!"".equals(status)) {
				if (2 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(status)) {
					// 更新状态下，所属系统名称为Cherry_Clear,则status更新为null
					countInfo.put("status", cherry_clear);
				} else if(1 == optFlag
						&& CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(status)){
					// 新增状态下，名称为cherry_clear,则status属性为空
					countInfo.put("status", null);
				} else {
					// 其他情况：数据与“CHERRY_CLEAR”无关，判断是否为【1030】对应的VALUES值
					boolean isStatusValue = false;
					for(Map<String, Object> counterStatusMap : counterStatusList){
						if(status.equals(counterStatusMap.get("Value"))){
							isStatusValue = true;
							countInfo.put("status", counterStatusMap.get("CodeKey"));
							break;
						}
					}
					if(!isStatusValue) {
						// 不为【1030】CODE对就的值则报错
						//标志该行数据有错误
						errorFlag = true;
						//标志柜台状态不存在
						countInfo.put("statusError", status);
						errorInfo.put("statusError", true);
					}
				}
			}else {
				countInfo.put("status", null);
			}
			countInfo.put("statusExcel", status);
			
			// 验证柜台员工数
			if(!"".equals(employeeNum) && !CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(employeeNum)){
				int num = 0;
				try{
					num = Integer.parseInt(employeeNum);
					countInfo.put("employeeNum", num);
				}catch(Exception e){
					logger.error(e.getMessage(),e);
					//标志该行数据有错误
					errorFlag = true;
					//标志柜台面积有错误
					countInfo.put("employeeNumError", true);
					errorInfo.put("employeeNumError", true);
					
					countInfo.put("employeeNum", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(employeeNum) ? cherry_clear :  employeeNum);
				}
			}else {
				countInfo.put("employeeNum", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(employeeNum) ? cherry_clear :  employeeNum);
			}
			
			//验证柜台面积
			if(!"".equals(counterSpace) && !CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterSpace)){
				double space = 0.0;
				try{
					space = Double.parseDouble(counterSpace);
					countInfo.put("counterSpace", space);
				}catch(Exception e){
					logger.error(e.getMessage(),e);
					//标志该行数据有错误
					errorFlag = true;
					//标志柜台面积有错误
					countInfo.put("counterSpaceError", true);
					errorInfo.put("counterSpaceError", true);
					
					countInfo.put("counterSpace", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterSpace) ? cherry_clear :  counterSpace);
				}
			}else {
				countInfo.put("counterSpace", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterSpace) ? cherry_clear :  counterSpace);
			}
			
			//如果该行没有发生错误则将柜台信息写入新后台并且将数据下发，否则记录下错误的柜台信息
			if(!errorFlag){
				// 导入柜台处理
				try{
					int flag = updateCounter(countInfo,tempMap);
					if(flag==1){
						successCount++;
						
						// 收集有BAS信息的柜台数据，发送BAS信息MQ
						String basId = ConvertUtil.getString(countInfo.get("employeeId"));
						if(!"".equals(basId) && !CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(basId)){
							if(!sendBASMQList.contains(basId)){
								sendBASMQList.add(basId);
							}
						}
					}else{
						failCount++;
						String jsonStr = CherryUtil.map2Json(countInfo);
						countInfo.put("jsonStr", jsonStr);
						errorCounterList.add(countInfo);
					}
				}catch(Exception e){
					logger.error(e.getMessage(),e);
					failCount++;
					String jsonStr = CherryUtil.map2Json(countInfo);
					countInfo.put("jsonStr", jsonStr);
					errorCounterList.add(countInfo);
				}
			}else{
				failCount++;
				String jsonStr = CherryUtil.map2Json(countInfo);
				countInfo.put("jsonStr", jsonStr);
				errorCounterList.add(countInfo);
			}
			errorInfo.put("rowNo", r+1);
			errorInfos.add(errorInfo);
		}
		
		//保存统计信息
		Map<String,Object> statisticsInfo = new HashMap<String,Object>();
		statisticsInfo.put("totalCount", totalCount);
		statisticsInfo.put("successCount", successCount);
		statisticsInfo.put("failCount", failCount);
		
		//作为结果返回
		Map<String,Object> resutlMap = new HashMap<String,Object>();
		resutlMap.put("statisticsInfo", statisticsInfo);
		resutlMap.put("errorCounterList", errorCounterList.isEmpty()? null:errorCounterList);
		resutlMap.put("errorInfo", errorInfos);
		
		//errorList 赋值，zcf
		errorList=errorCounterList;
		
		// 柜台主管对应的柜台发送MQ
		if(sendBASMQList.size() != 0){
			setMQ(sendBASMQList,map);
		}
		
		return resutlMap;
	}

	/**
	 * 验证根据城市ID自动生成柜台编号(自然堂)
	 * @param errorInfo
	 * @param errorFlag
	 * @param cityName
	 * @param countInfo
	 * @return
	 */
	private boolean validCityByCntCode(Map<String, Object> errorInfo,
			boolean errorFlag, String cityName, Map<String, Object> countInfo) {
		//验证城市名称
		if(!"".equals(cityName)){
			Map<String,Object> validateMap = new HashMap<String,Object>();
			validateMap.putAll(comMap);
			//区域类型：省会城市、城市、县级市【RegionType NOT IN (0,1)】
			validateMap.put("regionTypeCityFlag", "1");
			
			if(cityName.length() <= 50){
				
				if(CherryConstants.CHERRY_CLEAR.equals(cityName)){
					// 城市名称不能清空
					errorFlag = true;
					countInfo.put("cityNameNoClearError", true);
					errorInfo.put("cityNameNoClearError", true);
				}else{
					
					validateMap.put("regionName", cityName);
					
					//调用共通取得大区信息
					List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
					if(null != regionInfoList && !regionInfoList.isEmpty()){
						if(regionInfoList.size()==1){
							String cityId = ConvertUtil.getString(regionInfoList.get(0).get("regionId"));
							
							Map<String,Object> selMap = new HashMap<String, Object>();
							selMap.putAll(comMap);
							selMap.put("cityId", cityId);
							
							// 取得City区号
							String teleCode = binOLBSCNT04_BL.getCntTeleCode(selMap);
							
							StringBuffer counterCodeSbf = new StringBuffer(20);
							if(null != teleCode){
								
								// 取得递增序号
								selMap.put("cntCodePreFive", "2"+teleCode.trim());
								String seq = binOLBSCNT04_BL.getCntCodeRightTree(selMap);
								
								counterCodeSbf.append("2").append(teleCode.trim()).append(seq);
								countInfo.put("counterCode", counterCodeSbf.toString());
							}else {
								counterCodeSbf.append("");
								// 城市区号没有，请先添加城市区号
								errorFlag = true;
								countInfo.put("cityTeleCodeError", true);
								errorInfo.put("cityTeleCodeError", true);
							}
							
							
						}
					}else {
						// 城市不存在，无法生成柜台
						errorFlag = true;
						countInfo.put("cityNoExistsError", true);
						errorInfo.put("cityNoExistsError", true);
					}
				}
				
			}else{
				//城市名称越长，无法生成柜台编号
				errorFlag = true;
				countInfo.put("cityNameSoLongError", true);
				errorInfo.put("cityNameSoLongError", true);
			}
		}else {
			// 提示必须有城市名称，柜台编号才能生成
			errorFlag = true;
			countInfo.put("cityNameNoExistsError", true);
			errorInfo.put("cityNameNoExistsError", true);
		}
		return errorFlag;
	}

	/**
	 * 处理区域省份城市
	 * @param errorInfo
	 * @param errorFlag
	 * @param regionName
	 * @param provinceName
	 * @param cityName
	 * @param countInfo
	 * @param optFlag
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean convertRPC(Map<String, Object> errorInfo,
			boolean errorFlag, String regionName, String provinceName,
			String cityName, Map<String, Object> countInfo, int optFlag) {
		// 更新状态下，区域、省份、城市任意一个值为"Cherry_Clear"则全部为"Cherry_Clear"，即清除
		if (2 == optFlag
				&& (CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(regionName)
						|| CherryConstants.CHERRY_CLEAR
								.equalsIgnoreCase(provinceName) || CherryConstants.CHERRY_CLEAR
							.equalsIgnoreCase(cityName))) {
			countInfo.put("departRegionId", cherry_clear);
			countInfo.put("departProvinceId", cherry_clear);
			countInfo.put("departCityId", cherry_clear);
		} 
		// 更新状态下，区域、省份、城市都为空白时则不更新区域、省份、城市
		else if (2 == optFlag
				&& "".equals(regionName) && "".equals(provinceName)
				&& "".equals(cityName)) {
			countInfo.put("departRegionId", null);
			countInfo.put("departProvinceId", null);
			countInfo.put("departCityId", null);
		} 
		// 新增状态下，区域、省份、城市任意一个值为"Cherry_Clear"则这些属性不添加
		else if (1 == optFlag
				&& (CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(regionName)
						|| CherryConstants.CHERRY_CLEAR
								.equalsIgnoreCase(provinceName) || CherryConstants.CHERRY_CLEAR
							.equalsIgnoreCase(cityName))) {
			countInfo.put("departRegionId", null);
			countInfo.put("departProvinceId", null);
			countInfo.put("departCityId", null);
		}
		else {
			//验证区域名称
			if (!"".equals(regionName)) {
				Map<String, Object> validateMap = new HashMap<String, Object>();
				validateMap.putAll(comMap);
				// 区域类型：大区
				validateMap.put("regionType", "0");
				
				// 此时regionName有值
				if (regionName.length() > 50) {
					// 标志该行数据有错误
					errorFlag = true;
					// 标志区域Name有错误
					countInfo.put("regionNameError", true);
					errorInfo.put("regionNameError", true);
				} else {
					// 区域名称
					validateMap.put("regionName", regionName);
					// 调用共通取得大区信息[此时用regionName模糊查询]
					List<Map<String, Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
					if (null != regionInfoList && !regionInfoList.isEmpty()) {
						if (regionInfoList.size() > 1) {
							// 标志该行数据有错误
							errorFlag = true;
							// 标志区域Name有错误
							countInfo.put("regionNameError", true);
							errorInfo.put("regionNameError", true);
						} else {
							countInfo.put("_regionId", regionInfoList.get(0).get("regionId"));
							countInfo.put("regionPath", regionInfoList.get(0).get("path"));
							countInfo.put("regionCode", regionInfoList.get(0).get("regionCode"));
						}
					} else {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志区域Name有错误
						countInfo.put("regionNameError", true);
						errorInfo.put("regionNameError", true);
					}
				}
			}
			countInfo.put("regionName", regionName);
			
			//验证省名称
			if(!"".equals(provinceName)){
				Map<String,Object> validateMap = new HashMap<String,Object>();
				validateMap.putAll(comMap);
				//区域类型：省
				validateMap.put("regionType", "1");
				
				//此时省Name有值
				if(provinceName.length() > 50){
					//标志该行数据有错误
					errorFlag = true;
					//标志省名称有错误
					countInfo.put("provinceNameError", true);
					errorInfo.put("provinceNameError", true);
				}else{
					validateMap.put("regionName", provinceName);
					
					//调用共通取得大区信息【以省Name模糊查询】
					List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
					if(null != regionInfoList && !regionInfoList.isEmpty()){
						if(regionInfoList.size()>1){
							//标志该行数据有错误
							errorFlag = true;
							//标志省Name有错误
							countInfo.put("provinceNameError",true);
							errorInfo.put("provinceNameError", true);
						}else{
							countInfo.put("provinceId", regionInfoList.get(0).get("regionId"));
							countInfo.put("provincePath", regionInfoList.get(0).get("path"));
						}
					} else {
						//标志该行数据有错误
						errorFlag = true;
						//标志省Name有错误
						countInfo.put("provinceNameError",true);
						errorInfo.put("provinceNameError", true);
					}
				}
			}
			countInfo.put("provinceName", provinceName);
			
			//验证城市名称
			if(!"".equals(cityName)){
				Map<String,Object> validateMap = new HashMap<String,Object>();
				validateMap.putAll(comMap);
				//区域类型：省会城市、城市、县级市【RegionType NOT IN (0,1)】
				validateMap.put("regionTypeCityFlag", "1");
				
				if(cityName.length() > 50){
					//标志该行数据有错误
					errorFlag = true;
					//标志省城市名称有错误
					countInfo.put("cityNameError", true);
					errorInfo.put("cityNameError", true);
				}else{
					validateMap.put("regionName", cityName);
					
					//调用共通取得大区信息
					List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
					if(null != regionInfoList && !regionInfoList.isEmpty()){
						if(regionInfoList.size()>1){
							//标志该行数据有错误
							errorFlag = true;
							//标志省城市名称有错误
							countInfo.put("cityNameError", true);
							errorInfo.put("cityNameError", true);
						}else{
							countInfo.put("cityId", regionInfoList.get(0).get("regionId"));
							countInfo.put("cityPath", regionInfoList.get(0).get("path"));
						}
					} else {
						//标志该行数据有错误
						errorFlag = true;
						//标志省城市名称有错误
						countInfo.put("cityNameError", true);
						errorInfo.put("cityNameError", true);
					}
				}
			}
			countInfo.put("cityName", cityName);
			
			// 设置及区域、省份、城市的上级并校验是否与excel中输入的相同
			setRegionIdAndValid(countInfo, errorInfo);
			errorFlag = (Boolean)countInfo.get("errorFlag") ? true : errorFlag;
		}
		return errorFlag;
	}
	
	/**
	 * 填充数据,往counterMap中添加渠道ID\经销商ID\商场ID
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	private void fillData(Map<String,Object> counterMap) throws Exception{
		//渠道名称
		String channelName = ConvertUtil.getString(counterMap.get("channeName"));
		//经销商code
		String resellerCode = ConvertUtil.getString(counterMap.get("resellerCode"));
		//经销商名称
		String resellerName = ConvertUtil.getString(counterMap.get("resellerName"));
		//商场名称
		String mallName = ConvertUtil.getString(counterMap.get("mallName"));
		//区域名称
		String regionName = ConvertUtil.getString(counterMap.get("regionName"));
		//省名称
		String provinceName = ConvertUtil.getString(counterMap.get("provinceName"));
		//城市名称
		String cityName = ConvertUtil.getString(counterMap.get("cityName"));
		// 柜台分类
		String counterCategory = ConvertUtil.getString(counterMap.get("counterCategory"));
		// 柜台英文名称
		String foreignName = ConvertUtil.getString(counterMap.get("foreignName"));
		// 柜台员工数
		String employeeNum = ConvertUtil.getString(counterMap.get("employeeNum"));
		// 柜台面积
		String counterSpace = ConvertUtil.getString(counterMap.get("counterSpace"));
		// 柜台地址
		String address = ConvertUtil.getString(counterMap.get("address"));
		
		// 柜台电话
		String counterTelephone = ConvertUtil.getString(counterMap.get("counterTelephone"));
		// 地理位置经度
		String longitude = ConvertUtil.getString(counterMap.get("longitude"));
		// 地理位置纬度
		String latitude = ConvertUtil.getString(counterMap.get("latitude"));
		
		// 所属系统
		String belongFaction = ConvertUtil.getString(counterMap.get("belongFaction"));
		
		
		// 查询新后台是否已经存在当前要导入的柜台
		Map<String,Object> pcouMap = new HashMap<String, Object>();
		pcouMap.put("counterCode", counterMap.get("counterCode"));
		pcouMap.putAll(comMap);
		Map<String,Object> couMap = (Map<String,Object>)binOLBSCNT06_Service.getCounterId(pcouMap);
		if(null != couMap){
			String oldBasID = binOLBSEMP04_Service.getEmployeeDepart(couMap);
			if(null != oldBasID){
				counterMap.put("oldBasID", oldBasID);
			}
		}
		// BAS "-1"值可能出现在xls中输入cherry_clear，失败后页面保存向后台传值时出现。
		String employeeId = ConvertUtil.getString(counterMap.get("employeeId"));
		if("-1".equals(employeeId)){
			counterMap.put("employeeId", CherryConstants.CHERRY_CLEAR);
		}
		
		//验证区域code和区域名称，除非code和名称都不填，否则名称是必填项
		if(!"".equals(regionName)){
			Map<String,Object> validateMap = new HashMap<String,Object>();
			validateMap.putAll(counterMap);
			//区域类型：大区
			validateMap.put("regionType", "0");
			//区域名称
			validateMap.put("regionName", regionName);
			//调用共通取得大区信息
			List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
			if(null != regionInfoList && !regionInfoList.isEmpty()){
				if(regionInfoList.size()>1){
				}else{
					counterMap.put("_regionId", regionInfoList.get(0).get("regionId"));
					counterMap.put("regionPath", regionInfoList.get(0).get("path"));
				}
			}
		}
		
		//验证省code和省名称，除非code和名称都不填，否则名称是必填项
		if(!"".equals(provinceName)){
			Map<String,Object> validateMap = new HashMap<String,Object>();
			validateMap.putAll(counterMap);
			//区域类型：省
			validateMap.put("regionType", "1");
			//区域code
//			validateMap.put("regionCode", "".equals(provinceCode)? null:provinceCode);
			//区域名称
//			if(provinceName.contains(CherryConstants.REGIONTYPE_PROVINCE)){
//				validateMap.put("regionName", provinceName.substring(0, provinceName.length()-1));
//			}else{
			validateMap.put("regionName", provinceName);
//			}
			
			//调用共通取得大区信息
			List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
			if(null != regionInfoList && !regionInfoList.isEmpty()){
				if(regionInfoList.size()>1){
				}else{
					counterMap.put("provinceId", regionInfoList.get(0).get("regionId"));
					counterMap.put("provincePath", regionInfoList.get(0).get("path"));
				}
			}
		}
		
		//验证城市code和城市名称，除非code和名称都不填，否则名称是必填项
		if(!"".equals(cityName)){
			Map<String,Object> validateMap = new HashMap<String,Object>();
			validateMap.putAll(counterMap);
			//区域类型：省会城市
			validateMap.put("regionType", "2");
			//区域code
//			validateMap.put("regionCode", "".equals(cityCode)? null:cityCode);
			//区域名称
//			if(cityName.contains(CherryConstants.REGIONTYPE_CITY)){
//				validateMap.put("regionName", cityName.substring(0, cityName.length()-1));
//			}else{
			validateMap.put("regionName", cityName);
//			}
			
			//调用共通取得大区信息
			List<Map<String,Object>> regionInfoList = binOLCM08_BL.getRegionInfoList(validateMap);
			//区域类型：城市
			validateMap.put("regionType", "3");
			List<Map<String,Object>> regionInfoList1 = binOLCM08_BL.getRegionInfoList(validateMap);
			regionInfoList.addAll(regionInfoList1);
			if(null != regionInfoList && !regionInfoList.isEmpty()){
				if(regionInfoList.size()>1){
				}else{
					counterMap.put("cityId", regionInfoList.get(0).get("regionId"));
					counterMap.put("cityPath", regionInfoList.get(0).get("path"));
				}
			}
		}
		
		Map<String, Object> errorInfo = new HashMap<String, Object>();
		setRegionIdAndValid(counterMap, errorInfo);
		
		//如果渠道名称不为空
		if(!"".equals(channelName)){
			if(CherryConstants.CHERRY_CLEAR.equals(channelName)){
				counterMap.put("channelId", cherry_clear);
			} else {
				Map<String,Object> validateMap = new HashMap<String,Object>();
				validateMap.putAll(counterMap);
				//渠道名称
				validateMap.put("channelName", channelName);
				//调用共通取得渠道信息
				List<Map<String,Object>> channelInfoList = binOLCM00_BL.getChannelList(validateMap);
				if(channelInfoList != null && !channelInfoList.isEmpty()){
					if(channelInfoList.size()>1){
						throw new CherryException("EBS00056");
					}else{
						counterMap.put("channelId", channelInfoList.get(0).get("channelId"));
					}
				}
			}
		}
		
		// 如果经销商不为空
		if(!"".equals(resellerCode)||!"".equals(resellerName)){
			
			if(CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerCode) 
					|| CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(resellerName)){
				counterMap.put("resellerInfoId", cherry_clear);
			} else {
				
				Map<String,Object> validateMap = new HashMap<String,Object>();
				validateMap.putAll(counterMap);
//			validateMap.put("resellerCode", "".equals(resellerCode)? null:resellerCode);
				validateMap.put("resellerName", resellerName);
				//调用共通取得经销商信息
				List<Map<String,Object>> resellerList = binOLCM00_BL.getResellerInfoList(validateMap);
				if(resellerList != null && !resellerList.isEmpty()){
					if(resellerList.size()>1){
						throw new CherryException("EBS00057");
					}else{
						counterMap.put("resellerInfoId", resellerList.get(0).get("resellerInfoId"));
					}
				}
			}
		}
		
		if(!"".equals(mallName)){
			
			if(CherryConstants.CHERRY_CLEAR.equals(mallName)){
				counterMap.put("mallInfoId", cherry_clear);
			}else {
				Map<String,Object> validateMap = new HashMap<String,Object>();
				validateMap.putAll(counterMap);
				validateMap.put("mallName", mallName);
				//调用共通取得商场信息
				List<Map<String,Object>> mallInfoList = binOLCM00_BL.getMallInfoList(validateMap);
				if(mallInfoList != null && !mallInfoList.isEmpty()){
					if(mallInfoList.size() > 1){
						throw new CherryException("EBS00058");
					}else{
						counterMap.put("mallInfoId", mallInfoList.get(0).get("mallInfoId"));
					}
				}
			}
		}
		
		counterMap.put("address", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(address) ? cherry_clear :  address);
		counterMap.put("foreignName", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(foreignName) ? cherry_clear :  foreignName);
		// 在JS上已有校验
		counterMap.put("employeeNum", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(employeeNum) ? cherry_clear :  employeeNum);
		counterMap.put("counterSpace", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterSpace) ? cherry_clear :  counterSpace);
		counterMap.put("counterCategory", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterCategory) ? cherry_clear :  counterCategory);
		
		counterMap.put("counterTelephone", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(counterTelephone) ? cherry_clear :  counterTelephone);
		counterMap.put("longitude", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(longitude) ? cherry_clear :  longitude);
		counterMap.put("latitude", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(latitude) ? cherry_clear :  latitude);
		
		counterMap.put("belongFaction", CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(belongFaction) ? cherry_clear :  belongFaction);
	}
	
	/**
	 * 保存编辑
	 * @param counterMap
	 * @return
	 * @throws Exception
	 */
	public boolean tran_saveEdit(Map<String,Object> counterMap) throws Exception{
		comMap = getComMap(counterMap);
		//填充数据
		fillData(counterMap);
		int result = updateCounter(counterMap,new HashMap<String,Object>());
		if(result == 0){
			throw new CherryException("ECM00005");
		}else{
			// 发送BAS信息MQ
			String employeeId = ConvertUtil.getString(counterMap.get("employeeId"));
			List<String> sendBASMQList = new ArrayList<String>();
			if(!"".equals(employeeId) && !CherryConstants.CHERRY_CLEAR.equals(employeeId)){
				sendBASMQList.add(employeeId);
			}
			String oldBasID = ConvertUtil.getString(counterMap.get("oldBasID"));
			if(!"".equals(oldBasID)){
				sendBASMQList.add(oldBasID);
			}
			setMQ(sendBASMQList, counterMap);
			
			return true;
		}
	}
	
	/**
	 * 
	 * 导入柜台处理
	 * 
	 * @param counterMap 柜台信息
	 * @return null
	 * 
	 */
	@SuppressWarnings("unchecked")
	private int updateCounter(Map<String, Object> counterMap,Map<String, Object> tempMap) throws Exception{
		// 查询柜台ID
		Map<String, Object> paraCouMap = new HashMap<String, Object>();
		paraCouMap.put("counterCode", counterMap.get("counterCode"));
		paraCouMap.putAll(comMap);
		Map<String, Object> counterIdMap = (Map<String, Object>)binOLBSCNT06_Service.getCounterId(paraCouMap);
		
		Object counterId = (null == counterIdMap) ? null : counterIdMap.get("BIN_CounterInfoID");
		
		//记录柜台信息
		Map<String,Object> counterInfoMap = new HashMap<String,Object>();
		counterInfoMap.putAll(comMap);
		counterInfoMap.put("orgCode", counterMap.get("orgCode"));
		counterInfoMap.put("loginName", counterMap.get("loginName"));
		//柜台编号
		counterInfoMap.put("counterCode", counterMap.get("counterCode"));
		//柜台名称
		counterInfoMap.put("counterName", counterMap.get("counterName"));
		//柜台英文名称
		counterInfoMap.put("nameForeign", counterMap.get("foreignName"));
		//柜台地址
		counterInfoMap.put("counterAddress", counterMap.get("address"));
		//柜台类别
		counterInfoMap.put("counterCategory", counterMap.get("counterCategory"));
		//测试区分
		counterInfoMap.put("counterKind", counterMap.get("counterKind"));
		//柜台员工数
		counterInfoMap.put("employeeNum", "".equals(ConvertUtil.getString(counterMap.get("employeeNum")))? null: ConvertUtil.getString(counterMap.get("employeeNum")));
		//柜台面积
		counterInfoMap.put("counterSpace", "".equals(ConvertUtil.getString(counterMap.get("counterSpace")))? null: ConvertUtil.getString(counterMap.get("counterSpace")));
		//取得城市ID
//		int regionId = getRegionId(counterMap);
		counterInfoMap.put("departRegionId", ConvertUtil.getString(counterMap.get("departRegionId")));
		counterInfoMap.put("departProvinceId", ConvertUtil.getString(counterMap.get("departProvinceId")));
		counterInfoMap.put("departCityId", ConvertUtil.getString(counterMap.get("departCityId")));
		counterInfoMap.put("regionId", ConvertUtil.getString(counterMap.get("departCityId")));
		//取得商场ID
//		int mallInfoId = getMallId(counterMap);
//		counterInfoMap.put("mallInfoId", mallInfoId==0? null:mallInfoId);
		counterInfoMap.put("mallInfoId", ConvertUtil.getString(counterMap.get("mallInfoId")));
		
		// 取得所属系统
		counterInfoMap.put("belongFaction", ConvertUtil.getString(counterMap.get("belongFaction")));
		
		//取得渠道ID
//		int channelId = getChannelId(counterMap);
//		counterInfoMap.put("channelId", channelId==0? null:channelId);
		counterInfoMap.put("channelId", ConvertUtil.getString(counterMap.get("channelId")));
		//取得经销商ID
//		int resellerInfoId = getResellerId(counterMap);
//		counterInfoMap.put("resellerInfoId", resellerInfoId==0? null:resellerInfoId);
		counterInfoMap.put("resellerInfoId", ConvertUtil.getString(counterMap.get("resellerInfoId")));
		
		// 柜台联系电话
		counterInfoMap.put("counterTelephone", ConvertUtil.getString(counterMap.get("counterTelephone")));
		// 地理位置经度
		counterInfoMap.put("longitude", ConvertUtil.getString(counterMap.get("longitude")));
		// 地理位置纬度
		counterInfoMap.put("latitude", ConvertUtil.getString(counterMap.get("latitude")));
		
		counterInfoMap.put("posFlag", ConvertUtil.getString(counterMap.get("posFlag")));
		counterInfoMap.put("counterLevel", ConvertUtil.getString(counterMap.get("counterLevel")));
		counterInfoMap.put("status", ConvertUtil.getString(counterMap.get("status")));
		
		int organizationId = getDepartIdNew(counterMap,tempMap);
		counterInfoMap.put("couNodeId", ConvertUtil.getString(counterMap.get("couNodeId")));
		//商圈
		counterInfoMap.put("busDistrict", counterMap.get("busDistrict"));
		//柜台类型
		counterInfoMap.put("managingType2", counterMap.get("managingType2"));
		//银联设备号
		counterInfoMap.put("equipmentCode", counterMap.get("equipmentCode"));
				
		// 柜台数据不存在时，插入柜台信息
		if (null == counterId) {
			// 把部门ID设置到柜台信息中
			counterInfoMap.put("orgId", organizationId);	
			// 插入柜台信息
			counterId = binOLBSCNT06_Service.insertCounterInfo(counterInfoMap);
			// 设置柜台信息ID
			counterInfoMap.put("counterId", counterId);
			// 设置事件名称ID 默认值需预先在code值表中设定
			counterInfoMap.put("eventNameID", 0);
			// 设置日期
			counterInfoMap.put("counterDate", counterMap.get(CherryConstants.CREATE_TIME));
			// 插入柜台开始事件信息
			binOLBSCNT06_Service.insertCounterEvent(counterInfoMap);
			
			//柜台下发
			String organizationInfoId = ConvertUtil.getString(counterInfoMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoId =ConvertUtil.getString(counterInfoMap.get(CherryConstants.BRANDINFOID));
			if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
				counterInfoMap.put("counterInfoId", counterId);
				counterMap.put("counterInfoId", counterId);
				//取得下发柜台数据
				Map<String,Object> synchroInfo = counterSynchro.assemblingSynchroInfo(counterMap);
				if(null != synchroInfo){
					// 操作类型--新增更新
					synchroInfo.put("Operate", "IUE");
					counterSynchro.synchroCounter(synchroInfo);
				}
			}
			
			/* 暂时领用，因为大批量频繁调用会导致WebService处理失败
			//是否调用Webservice进行柜台数据同步
			
			//通过WebService方式将柜台信息下发
			Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(counterInfoMap);
			//WebService返回信息Map
			Map<String,Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
			String State = ConvertUtil.getString(resultMap.get("State"));
			String Data = ConvertUtil.getString(resultMap.get("Data"));
			if(State.equals("ERROR")){
				CherryException CherryException = new CherryException("");
				CherryException.setErrMessage(Data);
				throw CherryException;
			}
		  }
		  **/
		} else {
			// 更新柜台信息表
			binOLBSCNT06_Service.updateCounterInfo(counterInfoMap);
			// 更新在组织结构中的柜台
			binOLBSCNT06_Service.updateCouOrg(counterInfoMap);
			
			//柜台下发
			String organizationInfoId = ConvertUtil.getString(counterInfoMap.get(CherryConstants.ORGANIZATIONINFOID));
			String brandInfoId =ConvertUtil.getString(counterInfoMap.get(CherryConstants.BRANDINFOID));
			if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
				counterInfoMap.put("counterInfoId", counterId);
				counterMap.put("counterInfoId", counterId);
				//取得下发柜台数据
				Map<String,Object> synchroInfo = counterSynchro.assemblingSynchroInfo(counterInfoMap);
				if(null != synchroInfo){
					// 操作类型--新增更新
					synchroInfo.put("Operate", "IUE");
					counterSynchro.synchroCounter(synchroInfo);
				}
			}
			/* 暂时领用，因为大批量频繁调用会导致WebService处理失败
			
			if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
			//通过WebService方式将柜台信息下发
			Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(counterInfoMap);
			Map<String,Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
			String State = ConvertUtil.getString(resultMap.get("State"));
			String Data = ConvertUtil.getString(resultMap.get("Data"));
			if(State.equals("ERROR")){
				CherryException CherryException = new CherryException("");
				CherryException.setErrMessage(Data);
				throw CherryException;
			}
		  }
		  */
		}
		return 1;
	}
	
	/**
	 * 取得区域ID（区域ID、省份ID、城市ID）
	 * 		设置区域、省份、城市
	 * 		校验区域、省份、城市
	 * @param counterMap 
	 * @param errorInfo 错误信息
	 * @return
	 */
	private void setRegionIdAndValid(Map<String,Object> counterMap,Map<String, Object> errorInfo){
		
		//错误区分，记录该行数据是否有错误，默认为没有错误
		boolean errorFlag = false;
		
		// 区域ID(通过excel名称查询取得)
		String _regionId = ConvertUtil.getString(counterMap.get("_regionId"));
		// 省份ID(通过excel名称查询取得)
		String provinceId = ConvertUtil.getString(counterMap.get("provinceId"));
		// 城市ID(通过excel名称查询取得)
		String cityId = ConvertUtil.getString(counterMap.get("cityId"));
		
		// ===================== 从城市开始逐个判断其在新后台的省份与区域是否与xls导入的相同  Start =====================================
		if(!"".equals(cityId)){
			int regionId = ConvertUtil.getInt(cityId);
			List<Map<String,Object>> regionList = binOLBSCOM01_BL.getSuperRegion(regionId);
			
			// 通过cityId在新后台取得对应省份
			String departProvinceId = "";
			// 通过cityId在新后台取得对应区域
			String departRegionId = "";
			
			if(!regionList.isEmpty()){
				for(Map<String,Object> temp : regionList){
					//大区
					if(ConvertUtil.getInt(temp.get("RegionType"))==0){
//						counterMap.put("departRegionId", temp.get("id"));
						departRegionId = ConvertUtil.getString(temp.get("id"));
					}//省
					else if(ConvertUtil.getInt(temp.get("RegionType"))==1){
//						counterMap.put("departProvinceId", temp.get("id"));
						departProvinceId = ConvertUtil.getString(temp.get("id"));
					}
				}
			}
			
			// 校验excel输入省份与通过cityId在新后台取得的省份是否一致
			// (如果provinceId为空则说明excel输入的省份在新后台不存在，也就没有比较的必要，柜台直接使用通过cityID查询出来的省份ID即 可)
			if(!"".equals(provinceId) && !provinceId.equals(departProvinceId)){
				// 导入的省份与通过城市在系统查询出来的省份不一致
				errorFlag = false;
				counterMap.put("compareProviceByCityError", true);
				errorInfo.put("compareProviceByCityError", true);
			}
			
			// 校验excel输入区域与通过cityId在新后台取得的区域是否一致
			// (如果_regionId为空则说明excel输入的区域在新后台不存在，也就没有比较的必要，柜台直接使用通过cityID查询出来的区域ID即 可)
			if(!"".equals(_regionId) && !_regionId.equals(departRegionId)){
				// 导入的区域与通过城市在系统查询出来的区域不一致
				errorFlag = false;
				counterMap.put("compareRegionByCityError", true);
				errorInfo.put("compareRegionByCityError", true);
			}
			
			counterMap.put("departRegionId", departRegionId);
			counterMap.put("departProvinceId", departProvinceId);
			counterMap.put("departCityId", cityId);
			
		} 
		else if(!"".equals(provinceId)){
			int regionId = ConvertUtil.getInt(provinceId);
			List<Map<String,Object>> regionList = binOLBSCOM01_BL.getSuperRegion(regionId);
			
			// 通过cityId在新后台取得对应区域
			String departRegionId = "";
			
			if(!regionList.isEmpty()){
				for(Map<String,Object> temp : regionList){
					//大区
					if(ConvertUtil.getInt(temp.get("RegionType"))==0){
//						counterMap.put("departRegionId", temp.get("id"));
						departRegionId = ConvertUtil.getString(temp.get("id"));
					}
				}
			}
			
			// 校验excel输入区域与通过cityId在新后台取得的区域是否一致
			// (如果_regionId为空则说明excel输入的区域在新后台不存在，也就没有比较的必要，柜台直接使用通过cityID查询出来的区域ID即 可)
			if(!"".equals(_regionId) && !_regionId.equals(departRegionId)){
				// 导入的区域与通过省份在系统查询出来的区域不一致
				errorFlag = false;
				counterMap.put("compareRegionByProvError", true);
				errorInfo.put("compareRegionByProvError", true);
			}
			
			counterMap.put("departRegionId", departRegionId);
			counterMap.put("departProvinceId", provinceId);
			counterMap.put("departCityId", "".equals(cityId) ? null : cityId);
		} 
		else {
			
			counterMap.put("departRegionId", "".equals(_regionId) ? null : _regionId);
			counterMap.put("departProvinceId", "".equals(provinceId) ? null : provinceId);
			counterMap.put("departCityId", "".equals(cityId) ? null : cityId);
		}
		
		// ===================== 从城市开始逐个判断其在新后台的省份与区域是否与xls导入的相同  End =====================================
		
		// 设置校验标记errorFlag
		counterMap.put("errorFlag", errorFlag);
	}

	/**
	 * 取得商场ID
	 * 
	 * */
	private boolean setMallId(Map<String,Object> counterMap,Map<String, Object> errorInfo) throws Exception{
		boolean errorFlag = false;
		
		String mallName = ConvertUtil.getString(counterMap.get("mallName"));
		//如果商场不存在，添加商场
		if(!"".equals(mallName)){
			try{
				Map<String,Object> insertMap = new HashMap<String,Object>();
				insertMap.put("mallName", mallName);
				insertMap.putAll(comMap);
				int id = binOLBSCOM01_BL.insertMallInfo(insertMap);
				
				counterMap.put("mallInfoId", String.valueOf(id));
			}catch(Exception e){
				logger.error(e.getMessage(),e);
				errorFlag = true;
				counterMap.put("mallNameError", mallName);
				errorInfo.put("mallNameError", true);
				throw e;
			}
		}
		return errorFlag;
	}
	
	/**
	 * 取得渠道ID
	 * 
	 * */
	private boolean setChannelId(Map<String,Object> counterMap,Map<String, Object> errorInfo) throws Exception{
		
		boolean errorFlag = false;
		
		//如果渠道ID不存在且渠道名称不为Cherry_Clear，添加渠道
		String channelName = ConvertUtil.getString(counterMap.get("channelName"));
		try{
			Map<String,Object> insertMap = new HashMap<String,Object>();
			insertMap.put("channelName", channelName);
			insertMap.putAll(comMap);
			//调用接口进行渠道插入
			int id = binOLBSCHA04_BL.tran_insertChannel(insertMap);
			counterMap.put("channelId", String.valueOf(id));
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			errorFlag = true;
			counterMap.put("channelNameError", channelName);
			errorInfo.put("channelNameError", true);
			throw e;
		}
		
		return errorFlag;
	}
	
	/**
	 * 取得经销商ID
	 * @param counterMap
	 * @param errorInfo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private boolean setResellerId(Map<String,Object> counterMap,Map<String, Object> errorInfo) throws Exception{
		
		boolean errorFlag = false;
		
		String resellerCode = ConvertUtil.getString(counterMap.get("resellerCode"));
		String resellerName = ConvertUtil.getString(counterMap.get("resellerName"));
		
		try{
			//如果经销商code为空,调用共通生成经销商code
			if("".equals(resellerCode)){
				//调用共通自动生成经销商code
				Map codeMap = code.getCode("1120","6");
				Map<String,Object> autoMap = new HashMap<String,Object>();
				autoMap.put("type", "6");
				autoMap.put("length", codeMap.get("value2"));
				autoMap.putAll(comMap);
				
				resellerCode = (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(autoMap);
				//调用共通自动生成柜台code
				counterMap.put("resellerCode", resellerCode);
			}
			Map<String,Object> insertMap = new HashMap<String,Object>();
			insertMap.put("resellerName", resellerName);
			insertMap.put("resellerCode", resellerCode);
			insertMap.putAll(comMap);
			
			int id = binOLBSCOM01_BL.insertResellerInfo(insertMap);
			counterMap.put("resellerInfoId",String.valueOf(id));
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			errorFlag = true;
			counterMap.put("resellerCodeError", resellerCode);
			errorInfo.put("resellerCodeError", true);
			counterMap.put("resellerNameError", resellerName);
			errorInfo.put("resellerNameError", true);
			
//			throw e;
		}
			
		
		return errorFlag;
	}
	
	/**
	 * 取得部门ID,如果部门不存在则创建部门,将部门节点放在柜台主管所在部门的下面,
	 * 如果没有柜台主管则将部门放在未知节点下面,并创建仓库和部门仓库关系
	 * @param counterMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private int getDepartIdNew(Map<String,Object> counterMap ,Map<String,Object> paramMap) throws Exception{
		
		int organizationId = 0;
		
		//如果有柜台主管就将柜台部门挂在柜台主管所在部门下面,否则就将柜台部门挂在未知下面
		String basId = ConvertUtil.getString(counterMap.get("employeeId"));
		
		//上级节点为未知节点区分
		boolean unknownFlag = false;
		String newHigherPath = null;
		if(!"".equals(basId) && !CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(basId)){
			//查询出员工所在部门
			Map<String,Object> paramMap1 = new HashMap<String,Object>();
			paramMap1.put("employeeId", basId);
			//调用共通查询出员工所在部门信息
			Map<String,Object> orgInfo = binOLBSCOM01_BL.getEmployeeOrgInfo(paramMap1);
			//
			if(null != orgInfo){
				newHigherPath = (String)orgInfo.get("path");
			}else{
				unknownFlag = true;
			}
		}else{
			newHigherPath = (String)binOLBSCOM01_BL.getUnknownPath(counterMap);
			unknownFlag = true;
		}
		
		//查询柜台在部门表中信息
		List<Map<String,Object>> departInfo = binOLBSCOM01_BL.getOrganizationId(counterMap);
		if(null == departInfo || departInfo.isEmpty()){
			
			counterMap.put("testType", counterMap.get("counterKind"));
			//如果柜台主管所属部门存在
			if(!unknownFlag){
				// 设置柜台的父节点,柜台的父节点为柜台主管所属部门的节点
				counterMap.put("agencyNodeId", newHigherPath);
				// 查询柜台在组织结构表中的插入位置
				counterMap.put("counterNodeId", binOLBSCNT06_Service.getCounterNodeId(counterMap));
				// 到期日expiringDate
				counterMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
				// 在组织结构表中插入柜台节点
				organizationId = (Integer)binOLBSCNT06_Service.insertCouOrg(counterMap);
				
			}else{
				//添加在未知节点下
				// 取得未知节点来作为部门的上级节点
				Object unknownPath = paramMap.get("unknownPath");
				if(null == unknownPath){
					//取得组织结构表中品牌下的未知节点
					unknownPath = binOLBSCOM01_BL.getUnknownPath(counterMap);
					//如果没有查询出未知节点,创建
					if(null == unknownPath){
						// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
						Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
						unknownOrgMap.putAll(comMap);
						// 未知节点添加在品牌节点下
						unknownOrgMap.put("agencyNodeId", binOLBSCNT06_Service.getFirstPath(unknownOrgMap));
						// 取得未知节点path
						unknownPath = binOLBSCNT06_Service.getCounterNodeId(unknownOrgMap);
						unknownOrgMap.put("nodeId", unknownPath);
						// 未知节点的部门代码
						unknownOrgMap.put("departCode",CherryConstants.UNKNOWN_DEPARTCODE);
						// 未知节点的部门名称
						unknownOrgMap.put("departName", CherryConstants.UNKNOWN_DEPARTNAME);
						// 未知节点的部门类型
						unknownOrgMap.put("type", CherryConstants.UNKNOWN_DEPARTTYPE);
						// 未知节点的到期日expiringDate
						unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
						binOLBSCOM01_BL.insertDepart(unknownOrgMap);
					}
					paramMap.put("unknownPath", unknownPath);
				}
				counterMap.put("agencyNodeId", unknownPath);
				// 查询柜台在组织结构表中的插入位置
				counterMap.put("counterNodeId", binOLBSCNT06_Service.getCounterNodeId(counterMap));
				counterMap.put("testType", counterMap.get("counterKind"));
				// 到期日expiringDate
				counterMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
				// 在组织结构表中插入柜台节点
				organizationId = (Integer)binOLBSCNT06_Service.insertCouOrg(counterMap);
			}
			
			// 所属部门
			counterMap.put("organizationId", organizationId);
			// 缺省仓库区分
			counterMap.put("defaultFlag", CherryConstants.IVT_DEFAULTFLAG);
			Map codeMap = code.getCode("1120","3");
			// 自动生成编码类型（仓库为3）
			counterMap.put("type", "3");
			// 仓库编码最小长度
			counterMap.put("length", codeMap.get("value2"));
			// 自动生成仓库编码
			counterMap.put("inventoryCode", (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(counterMap));
			// 仓库名称
			counterMap.put("inventoryNameCN", counterMap.get("counterName")+CherryConstants.IVT_NAME_CN_DEFAULT);
			// 设定仓库类型为柜台仓库
			counterMap.put("depotType", "02");
			// 添加仓库
			int depotInfoId = binOLBSDEP04_Service.addDepotInfo(counterMap);
			counterMap.put("depotInfoId", depotInfoId);
			// 添加部门仓库关系
			binOLBSDEP04_Service.addInventoryInfo(counterMap);
			
		}else{
			organizationId = (Integer)departInfo.get(0).get("organizationId");
			
			// 所属部门
			counterMap.put("organizationId", organizationId);
			// 仓库名称
			counterMap.put("inventoryNameCN", counterMap.get("counterName")+CherryConstants.IVT_NAME_CN_DEFAULT);
			
			// 更新柜台仓库名称
			binOLBSDEP04_Service.updateDepotInfo(counterMap);
			
			if("".equals(basId)){
				// 更新状态下，当前BAS为空时，关系不变（不处理BAS与柜台的关系）
				return organizationId;
			}
			
			String oldHigherPath = (String)departInfo.get(0).get("couHigherPath");
			//如果新的父节点与老的父节点不一致,更新
			if(null != oldHigherPath && !oldHigherPath.equals(newHigherPath)){
				counterMap.put("agencyNodeId", newHigherPath);
				if(!"".equals(basId)){
					// BAS空白时，忽略更新
					counterMap.put("couNodeId", binOLBSCNT06_Service.getCounterNodeId(counterMap));
				}
			}
		}
		
		//设定柜台与柜台主管之间的关系
		if(!"".equals(basId)){
			Map<String,Object> relationShip = new HashMap<String,Object>();
			relationShip.putAll(comMap);
			//员工ID
			relationShip.put("employeeId", basId);
			//部门ID
			relationShip.put("organizationId", organizationId);
			//部门类型	0：总部； 1：品牌 2：各级办事处 3：经销商 4：柜台 5：其他
			relationShip.put("departType", "4");
			//管理类型
			relationShip.put("manageType", 1);
			
			//先删除可能存在的对应关系
			List<Map<String,Object>> organizationIdList = new ArrayList<Map<String,Object>>();
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("organizationId", organizationId);
			organizationIdList.add(temp);
			Map<String,Object> delMap = new HashMap<String,Object>();
			delMap.put("organizationIdList", organizationIdList);
			binOLBSEMP04_Service.delEmployeeDepart(delMap);
			
			if(!CherryConstants.CHERRY_CLEAR.equalsIgnoreCase(basId)){
				// 如果basId为Cherry_Clear，则是清除BAS与柜台的关系，不需要执行下面新增的程序
				List<Map<String,Object>> rel = new ArrayList<Map<String,Object>>();
				rel.add(relationShip);
				binOLBSEMP04_Service.insertEmployeeDepart(rel);
			}
			
		}
		
		return organizationId;
	}
	
	/**
	 * 查询柜台主管对应柜台并发送MQ
	 * @param map
	 * @throws Exception
	 */
	private void setMQ(List<String> sendBASMQList,Map<String,Object> paraMap) throws Exception{
		
		for(String employeeId : sendBASMQList){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("employeeId", employeeId);
			//查询出该柜台主管对应柜台
			map.put(CherryConstants.BRANDINFOID, paraMap.get(CherryConstants.BRANDINFOID));
			map.put(CherryConstants.ORGANIZATIONINFOID, paraMap.get(CherryConstants.ORGANIZATIONINFOID));
			map.put("BasFlag", true);
			Map<String,Object> employeeMap = binOLBSCNT04_Service.getEmployeeInfo(map);
			if(null != employeeMap){
				map.put("employeeCode", employeeMap.get("employeeCode"));// 柜台主管员工Code
				map.put("validFlag", employeeMap.get("validFlag"));// 柜台主管数据有效区分
				
				List<Map<String,Object>> countersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
				if(countersList != null && !countersList.isEmpty()){
					for(Map<String,Object> counterInfo : countersList){
						counterInfo.put("EmployeeCode", employeeMap.get("employeeCode"));
					}
				}
				
				Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(map, countersList,"BAS");
				if(MQMap.isEmpty()) return;
				//设定MQInfoDTO
				MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
				//调用共通发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
			}
		}
	}
	
	/**
	 * 取得管辖该柜台的员工信息
	 * 
	 * */
	public List<Map<String,Object>> getEmployeeList(Map<String,Object> map){
		return binOLBSCNT02_Service.getEmployeeList(map);
	}
	
	/**
	 * 共通参数
	 * @param map
	 * @return
	 */
	private Map<String,Object> getComMap(Map<String,Object> map){
		Map<String,Object> baseMap = new HashMap<String, Object>();
		// 作成者
		baseMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		baseMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		baseMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
		// 更新模块
		baseMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
		baseMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		baseMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		baseMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		return baseMap;
	}
	
	/**
	 * 导出柜台信息Excel
	 * @author ZhaoChaoFan 6.5
	 * @param map
	 * @return 返回导出柜台信息List
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
	    List<Map<String,Object>> aerrorList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> errorListMap : errorList) {
			//主管名称
			String basNameA=errorListMap.get("basName").toString();
			//主管Code
			String basCodeA=errorListMap.get("basCode").toString();
			//经销商Code
			String resellerCodeA=errorListMap.get("resellerCode").toString();
			//经销商名称
			String resellerNameA=errorListMap.get("resellerName").toString();
			String basCodeName ="";
			String resellerCodeName ="";
			//主管(由主管名称合主管Code组成)
			if("".equals(basNameA) || "".equals(basCodeA)){
				basCodeName=basCodeA+basNameA;
			}else {
				basCodeName="（"+basCodeA+")"+basNameA;
			}
			errorListMap.put("basCodeName",basCodeName);
			//经销商（由经销商名称和经销商Code组成）
			if("".equals(resellerCodeA) || "".equals(resellerNameA)){
				resellerCodeName=resellerCodeA+resellerNameA;
			}else {
				resellerCodeName="（"+resellerCodeA+")"+resellerNameA;
			}
			errorListMap.put("resellerCodeName",resellerCodeName);
			aerrorList.add(errorListMap);
			}
			
		List<Map<String, Object>> dataList = errorList;
		
	    String[][] array = {
	    		{ "brandCode", "counter.brandNameChinese", "15", "", "" },//品牌名称
	    		{ "counterCode", "counter.counterCode", "15", "", "" },//柜台编号
	    		{ "counterName", "counter.counterNameIF", "20", "", "" },//柜台名称
	    		{ "counterKind", "counter.testType", "15", "", "1031" },//柜台类型
	    		{ "basCodeName", "counter.counterHeader", "18", "", "" },//柜台主管 （basName）employeeId
	    		{ "regionName", "counter.region", "10", "", "" },//大区（区域）
	    		{ "provinceName", "counter.province", "10", "", "" },//所属省份
	    		{ "cityName", "counter.city", "10", "", "" },//所属城市
	    		{ "counterCategory", "counter.category", "10", "", "" },//柜台分类
	    		{ "belongFactionName", "counter.belongFaction", "15", "", "" },//所属系统
	    		{ "channelName", "counter.channelName", "20", "", "" },//所属渠道
	    		{ "resellerCodeName", "counter.reseller", "30", "", "" },//经销商（resellerName）
	    		{ "mallName", "counter.mall", "15", "", "" },//商场
	    		{ "foreignName", "counter.nameForeign", "15", "", "" },//柜台英文名称
	    		{ "address", "counter.counterAddress", "30", "", "" },//柜台地址
	    		{ "employeeNum", "counter.employeeNum", "10", "", "" },//员工数
	    		{ "counterSpace", "counter.space", "15", "right", "" },//面积
	    		{ "counterTelephone", "counter.telephone", "15", "", "" },//柜台电话
	    		{ "statusExcel", "counter.status", "15", "", "" },//柜台状态
	    		{ "counterLevelExcel", "counter.counterLevel", "15", "", "" },//柜台级别 
	    		{ "posFlagExcel", "counter.posFlag", "15", "", "" },//是否有POS机
	    		{ "longitude", "counter.longitude", "15", "", "" },//经度
	    		{ "latitude", "counter.latitude", "15", "", "" },//纬度
	    		{ "managingType2", "counter.managingType2", "50", "", "1403" },//柜台类型
	    		{ "equipmentCode", "counter.equipmentCode", "50", "", "" },//银联设备号
	    		{ "errorInfoList", "counter.error", "150", "", "" },//错误原因
	    };
	    
	    BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
	    ep.setMap(map);
	    ep.setArray(array);
	    ep.setBaseName("BINOLBSCNT01");
	    ep.setSheetLabel("sheetName");
	    ep.setDataList(dataList);
	    return binOLMOCOM01_BL.getExportExcel(ep);
	}
}
