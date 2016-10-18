package com.cherry.bs.emp.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.emp.interfaces.BINOLBSEMP06_IF;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP06_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

public class BINOLBSEMP06_BL implements BINOLBSEMP06_IF {

	@Resource(name = "binOLBSEMP06_Service")
	private BINOLBSEMP06_Service binOLBSEMP06_Service;
	
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	

	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Resource(name = "binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource(name = "CodeTable")
	private CodeTable CodeTable;

	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	/** 取得BA关注与归属柜台 */
	@Resource(name = "binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;

	/** 发送MQ消息共通处理 IF */
	@Resource(name = "binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;

	/** 共通参数 */
	private Map<String, Object> comMap;

	/** 导入EXCEL的版本号 */
	private static final String EXCEL_VERSION = "V1.0.1";

	private static Logger logger = LoggerFactory
			.getLogger(BINOLBSEMP06_BL.class.getName());

	@Override
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception {
		comMap = getComMap(map);
		// 错误字段信息LIST
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
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(inStream, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 上传文件解析失败，请检查文件扩展名是否为.xls！
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
		// BA数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.DESCRIPTION_SHEET_NAME.equals(st.getName()
					.trim())) {
				descriptSheet = st;
			} else if (CherryConstants.BA_SHEET_NAME
					.equals(st.getName().trim())) {
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
		// BA数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.BA_SHEET_NAME });
		}

		// 处理总件数
		int totalCount = 0;
		// 成功处理总件数
		int successCount = 0;
		// 失败总件数
		int failCount = 0;
		// 当前日期
		String sysDate = binOLBSEMP06_Service.getDateYMD();
		// 是否是品牌帐号
		boolean isBrandUser = (Boolean) map.get("isBrandUser");
		// 用户组织ID
		int organizationInfoId = (Integer) map
				.get(CherryConstants.ORGANIZATIONINFOID);

		int brandInfoId = 0;
		String brand_code = "";
		List<Map<String, Object>> brandInfoList = null;
		// 如果登录用户为品牌用户则查询出该品牌信息，如果是组织帐号则查询出该组织下的所有品牌信息
		if (isBrandUser) {
			brandInfoId = (Integer) map.get(CherryConstants.BRANDINFOID);
			brand_code = binOLCM05_Service.getBrandCode(brandInfoId);
			if (brandInfoId == 0 || "".equals(brand_code)) {
				throw new CherryException("EBS00052");
			}
		} else {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 所属组织
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			// 语言
			paramMap.put(CherryConstants.SESSION_LANGUAGE,
					map.get(CherryConstants.SESSION_LANGUAGE));
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(paramMap);
			if (null == brandInfoList || brandInfoList.isEmpty()) {
				throw new CherryException("EBS00053");
			}
		}
		// 声明一个list用来存放解析过程中出错的BA信息，并记录下相应的错误。
		List<Map<String, Object>> errorBaList = new ArrayList<Map<String, Object>>();

		// 循环导入BA信息
		for (int r = 3; r < dateSheet.getRows(); r++) {
			// 错误信息
			Map<String, Object> errorInfo = new HashMap<String, Object>();
			// 错误区分，记录该行数据是否有错误，默认为没有错误
			boolean errorFlag = false;
			// 新增标志
			boolean isAddFlag = false;
			// 品牌代码（A）
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// BA编码（B）
			String baCode = dateSheet.getCell(1, r).getContents().trim();
			// BA名称（C）
			String baName = dateSheet.getCell(2, r).getContents().trim();
			// 所属柜台CODE（D）
			String counterCode = dateSheet.getCell(3, r).getContents().trim();
			// 所属柜台名称（E）
			String counterName = dateSheet.getCell(4, r).getContents().trim();
			// 身份证号（F）
			String identityCard = dateSheet.getCell(5, r).getContents().trim();
			// 手机号（G）
			String mobilePhone = dateSheet.getCell(6, r).getContents().trim();
			// 入职日期（H）
			String commtDate = dateSheet.getCell(7, r).getContents().trim();
			// 离职日期（I）
			String depDate = dateSheet.getCell(8, r).getContents().trim();

			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(baCode)
					&& CherryChecker.isNullOrEmpty(baName)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)
					&& CherryChecker.isNullOrEmpty(identityCard)
					&& CherryChecker.isNullOrEmpty(mobilePhone)
					&& CherryChecker.isNullOrEmpty(commtDate)
					&& CherryChecker.isNullOrEmpty(depDate)) {
				break;
			}
			// 处理总件数+1
			totalCount++;

			Map<String, Object> baInfo = new HashMap<String, Object>();
			baInfo.putAll(comMap);

			// 处理品牌CODE（必填）
			if ("".equals(brandCode) || brandCode.length() > 10) {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志品牌代码有错误
				baInfo.put("brandCodeError", true);
				errorInfo.put("brandCodeError", true);
			} else {
				// 如果是品牌帐号，则验证输入的品牌信息是否正确
				if (isBrandUser) {
					// 如果品牌代码不匹配则记录
					if (!brand_code.equals(brandCode)) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志品牌代码有错误
						baInfo.put("brandCodeError", true);
						errorInfo.put("brandCodeError", true);
					}
				} else {
					boolean flag = false;
					// 循环品牌list，比对是否匹配
					for (Map<String, Object> temp : brandInfoList) {
						if (temp.get("BrandCode").equals(brandCode)) {
							flag = true;
							Map<String, Object> param = new HashMap<String, Object>();
							// 组织ID
							param.put(CherryConstants.ORGANIZATIONINFOID,
									organizationInfoId);
							// 品牌Code
							param.put("brandCode", brandCode);
							// 调用共通取得品牌ID
							brandInfoId = binOLCM05_BL.getBrandInfoId(param);
							break;
						}
					}
					// 如果匹配记录下brandcode，不匹配记录错误
					if (!flag) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志品牌代码有错误
						baInfo.put("brandCodeError", true);
						errorInfo.put("brandCodeError", true);
					}
				}
			}

			baInfo.put("brandCode", brandCode);
			baInfo.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 此条记录的品牌以此品牌ID为准
			comMap.put(CherryConstants.BRANDINFOID, brandInfoId);

			// 处理BA名称(必填)
			baInfo.put("baName", baName);
			if ("".equals(baName) || baName.length() > 50) {
				// 标志该行数据有错误
				errorFlag = true;
				// 标志柜台名称有错误
				baInfo.put("baNameError", true);
				errorInfo.put("baNameError", true);
			} else {
				// ======================== BA名称编码处理逻辑
				// ================================
				// **名称存在的情况下，确定Code是否存在 ********************//
				// *****Code 不存在时，通过名称到BA表查询是否存在？ ********************
				// **********若存在则使用查询到的Code,*************************************
				// **********若不存在，则看系统配置项是否开启自动生成BA代码功能，.*********
				// ***************若开启自动生成，*****************************************
				// ***************若未开启则报错******************************************
				// *****Code存在时，即使通过名称在BA表查询到数据，也以xls的柜台Code为准*********
				// ***************再用此code去查询BA表中是否存在此CODE的数据******************
				// *********************若存在则更新此数据,反之则新增数据*********************

				// Code 不存在时，通过名称到柜台表查询是否存在
				if ("".equals(baCode)) {
					Map<String, Object> paramBaMap = new HashMap<String, Object>();
					paramBaMap.put("baName", baName);
					paramBaMap.putAll(comMap);
					Map<String, Object> resultBaMap = binOLBSEMP06_Service
							.getBaInfoByNameCode(paramBaMap);

					// 若存在则使用查询到的Code
					if (null != resultBaMap) {
						baInfo.put("baCode", resultBaMap.get("baCode"));
					} else {// 若不存在 ，则看系统配置项是否开户自动生成BA代码功能
						boolean isConfigOpen = false;
						// 查询系统配置中是否开启自动生成员工代码功能
						isConfigOpen = binOLCM14_BL.isConfigOpen("1008",
								organizationInfoId, brandInfoId);
						if (!isConfigOpen) {
							// 标志该行数据有错误
							errorFlag = true;
							// 标志柜台名称有错误
							baInfo.put("baCodeError", true);
							errorInfo.put("baCodeError", true);
						} else {
							// 调用共通自动 生成BaCode
							Map<String, Object> codeMap = CodeTable.getCode(
									"1120", "1");
							Map<String, Object> autoMap = new HashMap<String, Object>();
							autoMap.put("type", "1");
							autoMap.put("length", codeMap.get("value2"));
							autoMap.putAll(comMap);
							// 自动生成员工code
							String baCodeAuto = (String) codeMap.get("value1")
									+ binOLCM15_BL.getSequenceId(autoMap);
							baCode = baCodeAuto;
							baInfo.put("baCode", baCodeAuto);
						}
					}
				} else {
					// xls中的BA编码存在时
					if (baCode.length() > 50) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志BaCode有错误
						baInfo.put("baCodeError", true);
						errorInfo.put("baCodeError", true);
						baInfo.put("baCode", baCode);
					} else if (!CherryChecker.isEmployeeCode(baCode)) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志BaCode有错误
						baInfo.put("baCodeError", true);
						errorInfo.put("baCodeError", true);
						baInfo.put("baCode", baCode);
					} else {
						// Code存在时，即使通过名称在BA表中查询到数据，也以XLS的人员code为准
						baInfo.put("baCode", baCode);
						// 查找在非BA的员工表中是否存在此CODE,若存在亦要报错（后面插入BA的员工表时有冲突）
						int empCodeCount = binOLBSEMP06_Service
								.getEmpCodeCount(baInfo);
						if (empCodeCount != 0) {
							// 标志该行数据有错误
							errorFlag = true;
							// 标志BaCode有错误
							baInfo.put("baCodeError", true);
							errorInfo.put("baCodeError", true);
						}
					}
				}

				// 验证BA编码是否符合编码规则
				// BA编码规则
				if (!CherryChecker.isNullOrEmpty(baCode)) {
					String basPattern = binOLCM14_BL.getConfigValue("1075",
							ConvertUtil.getString(organizationInfoId),
							ConvertUtil.getString(brandInfoId));
					Pattern p = Pattern.compile(basPattern);
					Matcher m = p.matcher(baCode);
					if (!m.matches()) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志BaCode有错误
						baInfo.put("baCodeRuleError", true);
						errorInfo.put("baCodeRuleError", true);
						baInfo.put("baCode", baCode);
					}
				}
			}
			//查询BaCode是否存在
			Map<String, Object> baIdMap = binOLBSEMP06_Service
					.getBaInfoByNameCode(baInfo);

			String baInfoID = ConvertUtil.getString((null == baIdMap) ? null
					: baIdMap.get("baInfoID"));			
			// 是新增的场合
			isAddFlag = ConvertUtil.isBlank(baInfoID);
			
			// 用于将错误信息显示出来
			errorInfo.put("baCode", baInfo.get("baCode"));

			// 验证所属柜台code和所属柜台名称，除非code和名称都不填，否则名称是必填项
			if (!"".equals(counterName) || !"".equals(counterCode)) {
				Map<String, Object> validateMap = new HashMap<String, Object>();
				validateMap.putAll(comMap);
				if ("".equals(counterName)) {
					// 标志该行数据有错误
					errorFlag = true;
					// 除非code和名称都不填，否则名称是必填项
					baInfo.put("counterNameError", true);
					errorInfo.put("counterNameError", true);
				} else {
					// 如果柜台编码与柜台名称都存在的场合同时去匹配，否则只用counterName去匹配。
					validateMap.put("counterName",
							!"".equals(counterName) ? counterName : null);
					validateMap.put("counterCode",
							!"".equals(counterCode) ? counterCode : null);
					// 取得匹配的柜台信息
					List<Map<String, Object>> counterInfo = binOLBSEMP06_Service
							.getCounterInfo(validateMap);
					if (null == counterInfo || counterInfo.isEmpty()
							|| counterInfo.size() > 1) {
						// 标志该行数据有错误
						errorFlag = true;
						// 标志柜台code有错误或者柜台名称有错误
						baInfo.put("counterCodeError", true);
						baInfo.put("counterNameError", true);
						errorInfo.put("counterCodeError", true);
						errorInfo.put("counterNameError", true);
					} else {
						baInfo.put("organizationID",
								counterInfo.get(0).get("organizationID"));
					}

				}
			}
			baInfo.put("counterCode", counterCode);
			baInfo.put("counterName", counterName);
			
			/**
			 * **************新增身份证号**********
			 * **************票号：WITPOSQA-14221*******************
			 * 
			 */
			String identityCardRule = binOLCM14_BL.getConfigValue("1364", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));

			//BA管理身份证/手机必填校验模式
			boolean mustInputFlag = binOLCM14_BL.isConfigOpen("1384", organizationInfoId, brandInfoId);	
			// 校验--身份证号 
			if("".equals(identityCard)) {
				//新增的场合，必填
				if(isAddFlag && mustInputFlag) {
					// 标志该行数据有错误
					errorFlag = true;
					// 导入的BA手机号不是合法的手机号
					baInfo.put("identityCardMustError", true);
					errorInfo.put("identityCardMustError", true);					
				}				
			} else {
				Pattern p = Pattern.compile(identityCardRule);
				Matcher m = p.matcher(identityCard);
				if(!m.matches()){
					// 不符合身份证号格式
					// 标志该行数据有错误
					errorFlag = true;
					// 导入的BA身份证号不是合法的身份证号
					baInfo.put("identityCardError", true);
					errorInfo.put("identityCardError", true);
				} else {
					// 校验身份证号是否在其他BA中已经存在
					Map<String, Object> identityCardParam = new HashMap<String, Object>();
					identityCardParam.put("organizationInfoId", organizationInfoId);
					identityCardParam.put("brandInfoId", brandInfoId);
					identityCardParam.put("identityCard", identityCard);
					if(!isAddFlag) {
						identityCardParam.put("employeeID", baIdMap.get("employeeID"));						
					}
					// 验证身份证是否唯一
					List<String> identityCardList = binolbsemp04Service.validateIdentityCard(map);		
					if (identityCardList.size() != 0){
						// 标志该行数据有错误
						errorFlag = true;
						// 导入的BA身份证号不是合法的身份证号
						baInfo.put("identityCardExistError", true);
						errorInfo.put("identityCardExistError", true);
					}
				}
			}
			baInfo.put("identityCard", identityCard);
			
			/**
			 * **************新增手机号、入职日期、离职日期**********
			 * **************票号：WITPOSQA-14221*******************
			 * 
			 */
			
			String mobileRule = binOLCM14_BL.getConfigValue("1090", ConvertUtil.getString(organizationInfoId), ConvertUtil.getString(brandInfoId));
						
			// 校验--手机号 
			if("".equals(mobilePhone)) {
				//新增的场合，必填
				if(isAddFlag && mustInputFlag) {
					// 标志该行数据有错误
					errorFlag = true;
					// 导入的BA手机号不是合法的手机号
					baInfo.put("mobilePhoneMustError", true);
					errorInfo.put("mobilePhoneMustError", true);					
				}				
			} else if(!CherryChecker.isPhoneValid(mobilePhone, mobileRule)){
				// 不符合手机格式
				// 标志该行数据有错误
				errorFlag = true;
				// 导入的BA手机号不是合法的手机号
				baInfo.put("mobilePhoneError", true);
				errorInfo.put("mobilePhoneError", true);
			} else {
				// 校验手机号是否在其他BA中已经存在
				Map<String, Object> mobileParam = new HashMap<String, Object>();
				mobileParam.put("mobilePhone", mobilePhone);
				// 验证手机是否唯一
				List<String> empIdList = this.getBaCodeByMobile(mobileParam);
				if (empIdList != null && !empIdList.isEmpty()) {
					for(String empId : empIdList) {
						if(empId != null && !empId.equals(baCode)) {
							// 标志该行数据有错误
							errorFlag = true;
							// 导入的BA手机号不是合法的手机号
							baInfo.put("mobilePhoneExistError", true);
							errorInfo.put("mobilePhoneExistError", true);
							break;
						}
					}
				}
			}
			baInfo.put("mobilePhone", mobilePhone);
			
			// 校验--入职日期
			if("".equals(commtDate)) {
				// 入职日期为空时默认为导入当天
				baInfo.put("commtDate", sysDate);
			} else if(!CherryChecker.checkDate(commtDate, "yyyy-DD-mm")) {
				// 标志该行数据有错误
				errorFlag = true;
				// 导入的入职日期不是合法的日期
				baInfo.put("commtDateError", true);
				errorInfo.put("commtDateError", true);
			}
			baInfo.put("commtDate", commtDate);
			
			// 校验--离职日期
			if("".equals(depDate)) {
				// 允许不填入职日期
			} else if(!CherryChecker.checkDate(depDate, "yyyy-DD-mm")) {
				// 标志该行数据有错误
				errorFlag = true;
				// 导入的离职日期不是合法的日期
				baInfo.put("depDateError", true);
				errorInfo.put("depDateError", true);
			} else if(!CherryChecker.isNullOrEmpty(baInfo.get("commtDate")) && CherryChecker.compareDate(ConvertUtil.getString(baInfo.get("commtDate")), depDate) > 0) {
				// 标志该行数据有错误
				errorFlag = true;
				// 离职日期不能小于入职日期
				baInfo.put("depDateValidError", true);
				errorInfo.put("depDateValidError", true);
			} else {
				if(CherryChecker.compareDate(sysDate,depDate)>=0) {
					// 当前日期大于等于离职日期，设置人员状态为4：离职
					baInfo.put("employeeStatus", "4");
				} else {
					baInfo.put("employeeStatus", "1");
				}
			}
			baInfo.put("depDate", depDate);
			
			// 如果该行没有发生错误则将BA信息写入新后台并且将数据下发，否则记录下错误的BA信息
			if (!errorFlag) {
				// 导入BA处理
				try {
					int flag = handleImportBA(baInfo);
					if (1 == flag) {
						successCount++;
						// 系统配置项设置要维护BA信息时才发送MQ消息
						boolean isMaintainBA = binOLCM14_BL.isConfigOpen(
								"1038", organizationInfoId, brandInfoId);
						if (isMaintainBA) {
							// 发送MQ
							Map<String, Object> paramEmpCntMap = new HashMap<String, Object>();
							paramEmpCntMap.putAll(comMap);
							// 登录用户为总部岗位时可选择品牌
							paramEmpCntMap.put(CherryConstants.BRANDINFOID,
									brandInfoId);
							paramEmpCntMap.put("employeeId",
									baInfo.get("employeeID"));
							// BA有效性 0 有效；1 无效
							paramEmpCntMap.put("validFlag",
									CherryConstants.VALIDFLAG_ENABLE);
							// BA取得关注和归属柜台
							paramEmpCntMap.put("BasFlag", "");
							List<Map<String, Object>> countersList = binOLBSCOM01_BL
									.getCounterInfoByEmplyeeId(paramEmpCntMap);
							if (countersList != null && !countersList.isEmpty()) {
								for (Map<String, Object> counterInfo : countersList) {
									counterInfo.put("EmployeeCode",
											baInfo.get("baCode"));
								}
							}
							Map<String, Object> MQMap = binOLBSCOM01_BL
									.getEmployeeMqMap(paramEmpCntMap,
											countersList, "BA");
							if (!MQMap.isEmpty()) {
								// 设定MQInfoDTO
								MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(
										MQMap, paramEmpCntMap);
								// 调用共通发送MQ消息
								binOLMQCOM01_BL.sendMQMsg(mqDTO, true);
							}
						}
					} else {
						failCount++;
						String jsonStr = CherryUtil.map2Json(baInfo);
						baInfo.put("jsonStr", jsonStr);
						errorBaList.add(baInfo);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					failCount++;
					String jsonStr = CherryUtil.map2Json(baInfo);
					baInfo.put("jsonStr", jsonStr);
					errorBaList.add(baInfo);
				}

			} else {
				failCount++;
				String jsonStr = CherryUtil.map2Json(baInfo);
				baInfo.put("jsonStr", jsonStr);
				errorBaList.add(baInfo);
			}
			errorInfos.add(errorInfo);
		}

		// 保存统计信息
		Map<String, Object> staticInfo = new HashMap<String, Object>();
		staticInfo.put("totalCount", totalCount);
		staticInfo.put("successCount", successCount);
		staticInfo.put("failCount", failCount);

		// 作为结果返回
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("staticInfo", staticInfo);
		resultMap
				.put("errorBaList", errorBaList.isEmpty() ? null : errorBaList);
		resultMap.put("errorInfo", errorInfos);
		return resultMap;
	}

	/**
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_saveEdit(Map<String, Object> baMap) throws Exception {
		comMap = getComMap(baMap);
		// 当前日期大于等于离职日期则员工状态为离职
		String sysDate = binOLBSEMP06_Service.getDateYMD();
		String depDate = ConvertUtil.getString(baMap.get("depDate"));
		if(!"".equals(depDate) && CherryChecker.compareDate(sysDate,depDate) >= 0) {
			// 当前日期大于等于离职日期，设置人员状态为4：离职
			baMap.put("employeeStatus", "4");
		} else {
			baMap.put("employeeStatus", "1");
		}
		
		int result = handleImportBA(baMap);
		if (result != 1) {
			// 保存失败
			throw new CherryException("ECM00005");
		} else {
			// 系统配置项设置要维护BA信息时才发送MQ消息
			boolean isMaintainBA = binOLCM14_BL.isConfigOpen("1038",
					ConvertUtil.getString(baMap
							.get(CherryConstants.ORGANIZATIONINFOID)),
					ConvertUtil.getString(baMap
							.get(CherryConstants.BRANDINFOID)));
			if (isMaintainBA) {
				// 发送MQ
				Map<String, Object> paramEmpCntMap = new HashMap<String, Object>();
				paramEmpCntMap.putAll(comMap);
				paramEmpCntMap.put("employeeId", baMap.get("employeeID"));
				// BA有效性 0 有效；1 无效
				paramEmpCntMap.put("validFlag",
						CherryConstants.VALIDFLAG_ENABLE);
				// BA取得关注和归属柜台
				paramEmpCntMap.put("BasFlag", "");
				List<Map<String, Object>> countersList = binOLBSCOM01_BL
						.getCounterInfoByEmplyeeId(paramEmpCntMap);
				if (countersList != null && !countersList.isEmpty()) {
					for (Map<String, Object> counterInfo : countersList) {
						counterInfo.put("EmployeeCode", baMap.get("baCode"));
					}
				}
				Map<String, Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(
						paramEmpCntMap, countersList, "BA");
				if (!MQMap.isEmpty()) {
					// 设定MQInfoDTO
					MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,
							paramEmpCntMap);
					// 调用共通发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqDTO, true);
				}
			}
		}

	}

	/**
	 * 处理BA的导入
	 * 
	 * @param baMap
	 * @param tempMap
	 * @return
	 * @throws Exception
	 */
	private int handleImportBA(Map<String, Object> baInfo) throws Exception {
		// 查询BA的ID及对应的employeeID
		Map<String, Object> paramBaMap = new HashMap<String, Object>();
		paramBaMap.put("baCode", baInfo.get("baCode"));
		paramBaMap.putAll(comMap);
		Map<String, Object> baIdMap = binOLBSEMP06_Service
				.getBaInfoByNameCode(paramBaMap);
		String baInfoID = ConvertUtil.getString((null == baIdMap) ? null
				: baIdMap.get("baInfoID"));
		String employeeID = ConvertUtil.getString((null == baIdMap) ? null
				: baIdMap.get("employeeID"));

		// 记录BA所属柜台信息与品牌信息
		Map<String, Object> baInfoMap = new HashMap<String, Object>();
		baInfoMap.putAll(comMap);
		// 所属品牌
		baInfoMap.put(CherryConstants.BRANDINFOID,
				baInfo.get(CherryConstants.BRANDINFOID));
		// BA编号
		baInfoMap.put("baCode", baInfo.get("baCode"));
		// BA名称
		baInfoMap.put("baName", baInfo.get("baName"));
		// 所属柜台
		baInfoMap.put("organizationID", baInfo.get("organizationID"));
		/**
		 * 新增手机号、入职日期、离职日期
		 * 票号：WITPOSQA-14221
		 * 
		 */
		// 手机号须加密
		String brandCode_key = ConvertUtil.getString(baInfo.get("brandCode"));
		if(!CherryChecker.isNullOrEmpty(baInfo.get("mobilePhone"),true)){
			String mobilePhone = ConvertUtil.getString(baInfo.get("mobilePhone"));
			baInfoMap.put("mobilePhone", CherrySecret.encryptData(brandCode_key,mobilePhone));
		}
		// 入职日期
		baInfoMap.put("commtDate", baInfo.get("commtDate"));
		// 离职日期
		baInfoMap.put("depDate", baInfo.get("depDate"));
		// 员工状态
		baInfoMap.put("employeeStatus", baInfo.get("employeeStatus"));
		
		// 剔除baInfoMap中的空值
		baInfoMap = CherryUtil.removeEmptyVal(baInfoMap);
		// 取得员工ID(code对应的BA表中有数据，员工表中必定有数据)
		int newEmpID = this.getEmployeeIdNew(baInfoMap, employeeID);
		// 记录BA在员工表中的ID
		baInfo.put("employeeID", newEmpID);
		// BAcode在BA表中不存在则插入数据，存在则更新相关信息
		if ("".equals(baInfoID)) {
			baInfoMap.put("newEmpID", newEmpID);
			// 来源区分（0：后台添加，1：终端上传）
			baInfoMap.put("originFlag", "0");
			// 插入营业员信息
			binOLBSEMP06_Service.insertBaInfo(baInfoMap);
		} else {
			// 更新营业员信息
			baInfoMap.put("baInfoID", baInfoID);
			// 更新
			binOLBSEMP06_Service.updateBaInfo(baInfoMap);
		}

		return 1;
	}

	/**
	 * 取得员工ID,如果员工不存在则创建员工，将员工节点放在未知节点下（因为导入时未设置上司）
	 * **********反之则更新此员工的所属柜台及名称信息。
	 * 
	 * @param baMap
	 *            : 导入的BA信息(含有共通参数getComMap)
	 * @param employeeID
	 *            ：BA编号对应的员工ID（用于判断是更新还是新建员工信息）
	 * @return
	 * @throws Exception
	 */
	private int getEmployeeIdNew(Map<String, Object> baMap, String employeeID)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(baMap);
		int newEmployeeID = ConvertUtil.getInt(employeeID);
		// employeeID为空是新增一条员工 信息反之则更新
		if ("".equals(employeeID)) {
			// BA岗位CODE
			map.put("categoryCode", CherryConstants.CATRGORY_CODE_BA);
			// 根据岗位CODE取得岗位ID
			map.put("positionCategoryID",
					binOLBSEMP06_Service.getPositionCategoryIdByCode(map));
			// 设上级为默认的根节点
			map.put("path", CherryConstants.DUMMY_VALUE);
			// 取得新节点
			String newEmpNodeId = binOLBSEMP06_Service.getNewEmpNodeId(map);
			map.put("newEmpNodeId", newEmpNodeId);
			map.put("employeeCode", baMap.get("baCode"));
			map.put("employeeName", baMap.get("baName"));
			// 插入员工信息表，返回员工ID
			newEmployeeID = binOLBSEMP06_Service.insertEmployee(map);

			map.put("employeeID", newEmployeeID);
			// 插入员工入职信息
			binOLBSEMP06_Service.insertEmpQuit(map);

			// 插入员工管辖部门对应表（此处为所属柜台）
			if (!CherryChecker.isNullOrEmpty(map.get("organizationID"))) {
				Map<String, Object> empDepartMap = new HashMap<String, Object>();
				empDepartMap.putAll(comMap);
				empDepartMap.put("employeeId", newEmployeeID);
				empDepartMap.put("organizationId", map.get("organizationID"));
				// 部门类型为柜台
				empDepartMap.put("departType", "4");
				// 管理类型为所属部门
				empDepartMap.put("manageType", "2");
				binOLBSEMP06_Service.insertEmployeeDepart(empDepartMap);
			}
		} else {
			// 对于已经存在的员工信息更新相关内容
			map.put("employeeName", baMap.get("baName"));
			map.put("employeeID", employeeID);
			// 更新员工表的名称与所属柜台
			binOLBSEMP06_Service.updateEmployeeInfo(map);
			// 更新员工入退职表
			binOLBSEMP06_Service.updateEmployeeQuit(map);
			// 该BA的员工柜台对应表信息数量
			int count = binOLBSEMP06_Service.getEmployeeDepartCount(map);
			// 更新员工管辖部门表
			if (count > 0) {
				if (!CherryChecker.isNullOrEmpty(map.get("organizationID"))) {
					binOLBSEMP06_Service.updateEmployeeDepart(map);
				} else {
					// 未指定部门则认为要删除这对应关系
					binOLBSEMP06_Service.delEmployeeDepart(map);
				}
			}
		}

		return newEmployeeID;
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
		baseMap.put(CherryConstants.CREATEDBY,
				map.get(CherryConstants.CREATEDBY));
		// 更新者
		baseMap.put(CherryConstants.UPDATEDBY,
				map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		baseMap.put(CherryConstants.CREATEPGM,
				map.get(CherryConstants.CREATEPGM));
		// 更新模块
		baseMap.put(CherryConstants.UPDATEPGM,
				map.get(CherryConstants.UPDATEPGM));
		baseMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		baseMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		baseMap.put(CherryConstants.SESSION_LANGUAGE,
				map.get(CherryConstants.SESSION_LANGUAGE));
		baseMap.put(CherryConstants.ORG_CODE, map.get(CherryConstants.ORG_CODE));
		baseMap.put(CherryConstants.BRAND_CODE, map.get(CherryConstants.BRAND_CODE));
		// 取得系统时间
		String sysDate = binOLBSEMP06_Service.getSYSDate();
		// 作成时间
		baseMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间
		baseMap.put(CherryConstants.UPDATE_TIME, sysDate);
		return baseMap;
	}
	
	/**
	 * 根据手机号取得人员ID
	 * @param map
	 * @return
	 */
	@Override
	public List<String> getBaCodeByMobile(Map<String, Object> map) {
		return binOLBSEMP06_Service.getBaCodeByMobile(map);
	}

}
