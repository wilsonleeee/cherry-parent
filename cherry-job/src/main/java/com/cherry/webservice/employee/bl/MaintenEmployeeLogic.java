package com.cherry.webservice.employee.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.employee.service.MaintenEmployeeService;

/**
 * 员工信息
 * 
 * @author lzs 下午5:14:45
 */
public class MaintenEmployeeLogic implements IWebservice {

	private static Logger logger = LoggerFactory.getLogger(MaintenEmployeeLogic.class.getName());

	/** 员工信息查询 **/
	@Resource(name = "maintenEmployeeService")
	private MaintenEmployeeService maintenEmployeeService;

	@Resource(name = "binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 员工新增标识 **/
	private static final String SUBTYPE_ADD="C";
	
	/** 员工更新标识 **/
	private static final String SUBTYPE_UPD="U";
	
	/** 员工逻辑删除标识 **/
	private static final String SUBTYPE_DEL="D";
	
	/** 员工新增/更新标识 **/
	private static final String SUBTYPE_IU="IU";
	
	/** 共通 **/
	private Map<String, Object> comMap = new HashMap<String, Object>();

	/** 回执员工信息 **/
	private Map<String, Object> returnParam = new HashMap<String, Object>();

	/** 回执Map **/
	private Map<String, Object> resultMap = new HashMap<String, Object>();

	@Override
	public Map tran_execute(Map map) throws Exception {
		try {
				// 验证传输参数有效性
				resultMap = veriFicationParam(map);
				if (null != resultMap && !resultMap.isEmpty()) {
					return resultMap;
				}
				String subType = ConvertUtil.getString(map.get("SubType"));
				// 创建员工
				if (SUBTYPE_ADD.equals(subType)) {
					resultMap = createEmpolyeeInfo(comMap);
					if (null != resultMap && !resultMap.isEmpty()) {
						return resultMap;
					}
				}
				// 更新时或逻辑删除时验证新后台是否已有数据
				if(SUBTYPE_UPD.equals(subType) || SUBTYPE_DEL.equals(subType)){
					resultMap = upExistEmployeeInfo(comMap);
					if (null != resultMap && !resultMap.isEmpty()) {
						return resultMap;
					}
					// 根据已有的员工代码查询已有的岗位代码及员工ID
					Map<String, Object> categoryCodeMap = maintenEmployeeService.getEmployeeByEmployeeCode(comMap);
						comMap.putAll(categoryCodeMap);
				}
				// 更新已有员工信息
				if (SUBTYPE_UPD.equals(subType)) {
					resultMap=updEmployeeInfo(comMap);
					return resultMap;
				}
				// 逻辑删除已有员工信息
				if (SUBTYPE_DEL.equals(subType)) {
					try {
						// 更新已有的员工信息和营业员信息
						Map<String, Object> deleteEmployeeMap = new HashMap<String, Object>();
						deleteEmployeeMap.putAll(comMap);
						deleteEmployeeMap.put(CherryBatchConstants.VALID_FLAG,CherryBatchConstants.VALIDFLAG_DISABLE);// 数据有效更改为无效
						deleteEmployeeMap.put("employeeStatus", "4");// 员工状态 4:离职
						maintenEmployeeService.updateBaInfo(deleteEmployeeMap);
						maintenEmployeeService.updateEmployee(deleteEmployeeMap);
		
						// 新后台数据源提交
						maintenEmployeeService.manualCommit();
						returnParam.put("EmployeeID", comMap.get("employeeID"));
						returnParam.put("EmployeeCode", comMap.get("EmployeeCode"));
						resultMap.put("ResultMap", returnParam);
						return resultMap;
					} catch (Exception e) {
						try {
							// 新后台数据源回滚
							maintenEmployeeService.manualRollback();
						} catch (Exception ex) {
							
						}
						logger.error("WS ERROR:"+ e.getMessage(),e);
			            logger.error("WS ERROR brandCode:"+ comMap.get("BrandCode"));
			            logger.error("WS ERROR paramData:"+ comMap.get("OriginParamData"));
						resultMap.put("ERRORCODE", "WSE069");
						resultMap.put("ERRORMSG", "删除员工信息时发生未知异常");
						return resultMap;
		
					}
				}
			 //新增或更新员工信息
			 if(SUBTYPE_IU.equals(subType)){
				 resultMap=checkEmployeeInfo(comMap);
				 if(null!=resultMap && !resultMap.isEmpty() && !resultMap.containsKey("identity")){
					 return resultMap;
				 }else{
					 String identity=ConvertUtil.getString(resultMap.get("identity"));
					 if(!CherryBatchUtil.isBlankString(identity)){
						 //创建员工信息
						 if(SUBTYPE_ADD.equals(identity)){
							 resultMap=createEmpolyeeInfo(comMap);//当标识符为【C】时代表需创建员工
							 return resultMap;
						 }
						 //更新已有员工信息
						 if(SUBTYPE_UPD.equals(identity)){
							 Map<String, Object> categoryCodeMap = maintenEmployeeService.getEmployeeByEmployeeCode(comMap);
							 comMap.putAll(categoryCodeMap);
							 resultMap=updEmployeeInfo(comMap);
							 return resultMap;
						 }
					 }else{
						 resultMap.put("ERRORCODE", "WSE9999");
						 resultMap.put("ERRORMSG", "处理过程中发生未知异常");
						 return resultMap;
					 }
				 }
			 }
		} catch (Exception e) {
			 logger.error("WS ERROR:"+ e.getMessage(),e);
	         logger.error("WS ERROR brandCode:"+ comMap.get("BrandCode"));
	         logger.error("WS ERROR paramData:"+ comMap.get("OriginParamData"));
			 resultMap.put("ERRORCODE", "WSE9999");
			 resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			 return resultMap;
		}
		return resultMap;
	}

	/**
	 * 验证传输参数有效性
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> veriFicationParam(Map<String, Object> map) {
		// 新后台未找到匹配数据
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("BIN_BrandInfoID")))) {
			resultMap.put("ERRORCODE", "WSE9998");
			resultMap.put("ERRORMSG", "参数brandCode错误");
			return resultMap;
		}
		// 根据部门代码查询部门ID
		String organizationId = "0";
		if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("DepartCode")))) {
			organizationId = maintenEmployeeService.getOrganizationId(map);
			// 部门代码不为空时查询新后台数据，未找到匹配数据则返回以下回执码
			if (CherryBatchUtil.isBlankString(organizationId)) {
				resultMap.put("ERRORCODE", "WSE0061");
				resultMap.put("ERRORMSG", "参数DepartCode错误");
				return resultMap;
			}
		}
		// 获取手机号验证规则配置项
		String mobileRule = binOLCM14_BL.getConfigValue("1090",ConvertUtil.getString(map.get("BIN_OrganizationInfoID")),ConvertUtil.getString(map.get("BIN_BrandInfoID")));
		// 判断手机号不为空时校检手机号是否合法
		if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("MobilePhone")))) {
			if (!CherryChecker.isPhoneValid(ConvertUtil.getString(map.get("MobilePhone")),mobileRule)) {
				resultMap.put("ERRORCODE", "WSE0031");
				resultMap.put("ERRORMSG", "参数MobilePhone不合法");
				return resultMap;
			}
		}
		// 判断必填字段是否为空
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("SubType"))) || 
			CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("DataSource")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "必填参数不能为空");
			return resultMap;
		}
		// 判断子类型是否是可以识别到的类型
		if (!SUBTYPE_UPD.equals(ConvertUtil.getString(map.get("SubType"))) && 
			!SUBTYPE_DEL.equals(ConvertUtil.getString(map.get("SubType"))) && 
			!SUBTYPE_ADD.equals(ConvertUtil.getString(map.get("SubType"))) &&
			!SUBTYPE_IU.equals(ConvertUtil.getString(map.get("SubType")))) {
			resultMap.put("ERRORCODE", "WSE0062");
			resultMap.put("ERRORMSG", "未能识别的子类型");
			return resultMap;
		}
		if (SUBTYPE_UPD.equals(ConvertUtil.getString(map.get("SubType"))) || SUBTYPE_DEL.equals(ConvertUtil.getString(map.get("SubType")))) {
			// 更新员工信息和逻辑删除员工时判断员工代码是否为空
			if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("EmployeeCode")))) {
				resultMap.put("ERRORCODE", "WSE0071");
				resultMap.put("ERRORMSG", "未指定员工代码");
				return resultMap;
			}
		}
		if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("ParentEmployeeCode")))) {
			String path=maintenEmployeeService.getNodeEmployeePathByCode(map);
			if(CherryBatchUtil.isBlankString(path)){
				resultMap.put("ERRORCODE", "WSE0064");
				resultMap.put("ERRORMSG", "未找到指定的上级员工");
				return resultMap;
			}
			map.put("path",path);
		} else {
			// 上级员工代码为空默认取根节点
			map.put("path", CherryConstants.DUMMY_VALUE);
		}
		//校检身份证格式和值是否为空
		String identityCard=ConvertUtil.getString(map.get("IdentityCard"));
		if(!CherryBatchUtil.isBlankString(identityCard)){
			if(!CherryChecker.isICardValid(identityCard)){
				resultMap.put("ERRORCODE", "WSE0073");
				resultMap.put("ERRORMSG", "身份证号码参数不合法");
				return resultMap;
			}
		}
		String newNodeId = maintenEmployeeService.getNewEmpNodeId(map);
		map.put("NodeID", newNodeId);
		map.put("organizationId", organizationId);// 部门ID
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "MaintenEmployeeLogic");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "MaintenEmployeeLogic");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,"MaintenEmployeeLogic");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,"MaintenEmployeeLogic");
		comMap.putAll(map);
		return resultMap;
	}

	/**
	 * 【更新员工||逻辑删除员工】根据员工代码，公众号和手机号查询员工数据是否存在
	 * 
	 * @param paramMap
	 * @param map
	 * @return
	 */
	private Map<String, Object> upExistEmployeeInfo(Map<String, Object> paramMap) {
		try {
			String employeeCode = maintenEmployeeService.getEmployeeCode(paramMap);
			if (!CherryBatchUtil.isBlankString(employeeCode)) {
					if(CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("OpenID"))) && 
					   CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
						return resultMap;
					}
					Map<String,Object> commonMap=new HashMap<String, Object>();
					commonMap.put("BIN_BrandInfoID",paramMap.get("BIN_BrandInfoID"));
					commonMap.put("BIN_OrganizationInfoID",paramMap.get("BIN_OrganizationInfoID"));
					commonMap.put("existsEmployeeCodes", employeeCode);
					if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("OpenID")))){
						Map<String, Object> openIdMap = new HashMap<String, Object>();
						openIdMap.putAll(commonMap);
						openIdMap.put("openID", paramMap.get("OpenID"));
						// 根据已存在的员工代码和公众号查询数据是否存在
						String employeeCodeByOpenId = maintenEmployeeService.getEmployeeCode(openIdMap);
						if (!CherryBatchUtil.isBlankString(employeeCodeByOpenId)) {
							resultMap.put("ERRORCODE", "WSE0029");
							resultMap.put("ERRORMSG", "参数中的OpenID在系统中已存在");
							return resultMap;
						}else{
							if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
								Map<String, Object> phoneMap1 = new HashMap<String, Object>();
								phoneMap1.putAll(commonMap);
								phoneMap1.put("openIds",paramMap.get("OpenID"));
								phoneMap1.put("mobilePhones", paramMap.get("MobilePhone"));
								// 根据已存在的员工代码和手机号查询数据是否存在
								String employeeCodeByMobilePhone = maintenEmployeeService.getEmployeeCode(phoneMap1);
								if (CherryBatchUtil.isBlankString(employeeCodeByMobilePhone)) {
									return resultMap;// 代表数据已存在，其他唯一性参数未被其他数据占用可进行更新或逻辑删除操作
								} else {
									resultMap.put("ERRORCODE", "WSE0059");
									resultMap.put("ERRORMSG", "指定的MobilePhone已被其他数据使用");
									return resultMap;
								}
							}else{
								return resultMap;
							}
						}
					}else{
						if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
							Map<String, Object> phoneMap = new HashMap<String, Object>();
							phoneMap.putAll(commonMap);
							phoneMap.put("mobilePhone", paramMap.get("MobilePhone"));
							// 根据已存在的员工代码和手机号查询数据是否存在
							String employeeCodeByMobilePhone = maintenEmployeeService.getEmployeeCode(phoneMap);
							if (CherryBatchUtil.isBlankString(employeeCodeByMobilePhone)) {
								return resultMap;// 代表数据已存在，其他唯一性参数未被其他数据占用可进行更新或逻辑删除操作
							} else {
								resultMap.put("ERRORCODE", "WSE0059");
								resultMap.put("ERRORMSG", "指定的MobilePhone已被其他数据使用");
								return resultMap;
							}
						}else{
							return resultMap;
						}
					}
			} else {
				resultMap.put("ERRORCODE", "WSE0002");
				resultMap.put("ERRORMSG", "未找到指定的员工");
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("WS ERROR:"+ e.getMessage(),e);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
			resultMap.put("ERRORCODE", "WSE0070");
			resultMap.put("ERRORMSG", "校验员工信息在其他数据中是否被占用时,发生未知异常");
			return resultMap;
		}

	}
	/**
	 * 【新增/更新】员工信息时根据唯一性判断数据是否存在，存在：更新，不存在：创建
	 * @param paramMap
	 * @return
	 */
	private Map<String, Object> checkEmployeeInfo(Map<String, Object> paramMap) {
		try {
			//以下判断代表当传输参数或值均不为空时的情况下执行
			String employeeCode = ConvertUtil.getString(paramMap.get("EmployeeCode"));
			if(!CherryBatchUtil.isBlankString(employeeCode)){
				employeeCode=maintenEmployeeService.getEmployeeCode(paramMap);
			}
			if (CherryBatchUtil.isBlankString(employeeCode) && 
				CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("OpenID"))) && 
				CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))) {
				resultMap.put("identity", "C");
				return resultMap;// 返回标识【C】代表数据可做新增操作
			}
			Map<String,Object> commonMap=new HashMap<String, Object>();
			commonMap.put("existsEmployeeCode", employeeCode);
			commonMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
			commonMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
			// 判断查询到的员工代码为空时，执行以下判断逻辑
			if (CherryBatchUtil.isBlankString(employeeCode)) {
				String employeeCodeByOpenId = null;
				if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("OpenID")))) {
					Map<String, Object> openIdMap = new HashMap<String, Object>();
					openIdMap.putAll(commonMap);
					openIdMap.put("openId", paramMap.get("OpenID"));
					employeeCodeByOpenId = maintenEmployeeService.getEmployeeCode(openIdMap);
					if (!CherryBatchUtil.isBlankString(employeeCodeByOpenId)) {// 如果查询数据不为空，排除公众号，则根据手机号进行查询数据
						if(CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
							comMap.put("EmployeeCode", employeeCodeByOpenId);
							resultMap.put("identity", "U");// 返回标识【U】代表可更新该数据【此返回只做为当员工代码和手机号均为空，但根据公众号查询到的数据不为空的情况下执行】
							return resultMap;
						}else{
							Map<String, Object> mobilePhoneMap = new HashMap<String, Object>();
							mobilePhoneMap.putAll(commonMap);
							mobilePhoneMap.put("existsMobilePhone",paramMap.get("MobilePhone"));
							mobilePhoneMap.put("existsOpenId",paramMap.get("OpenID"));
							String employeeCodeByMobilePhone = maintenEmployeeService.getEmployeeCode(mobilePhoneMap);
							if (CherryBatchUtil.isBlankString(employeeCodeByMobilePhone)) {
								comMap.put("EmployeeCode", employeeCodeByOpenId);
								resultMap.put("identity", "U");
								return resultMap;// 返回标识【U】代表可更新该数据【此返回只做为当员工代码为空，公众号和手机号均不为空的情况下执行】
							} else {
								resultMap.put("ERRORCODE", "WSE0059");
								resultMap.put("ERRORMSG", "指定的MobilePhone已被其他数据使用");
								return resultMap;
							}
						}
					} else {
						if (CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))) {
							resultMap.put("identity", "C");// 返回标识【C】代表可新增数据【此返回只做为当员工代码和手机号均为空，公众号查询数据为空的情况下执行】
							return resultMap;
						}
					}
				}
				// 当查询出的公众号为空且手机号不为空时，执行以下代码
				if (CherryBatchUtil.isBlankString(employeeCodeByOpenId) && !CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))) {
					Map<String, Object> mobilePhoneMap1 = new HashMap<String, Object>();
					mobilePhoneMap1.putAll(commonMap);
					mobilePhoneMap1.put("existsSingleMobilePhone",paramMap.get("MobilePhone"));
					String employeeCodeByMobilePhone1 = maintenEmployeeService.getEmployeeCode(mobilePhoneMap1);
					if (CherryBatchUtil.isBlankString(employeeCodeByMobilePhone1)) {
						resultMap.put("identity", "C");
						return resultMap;// 返回标识【C】代表可新增数据【此返回只做为当员工代码和公众号均为空，手机号不为空的情况下执行】
					} else {
						comMap.put("EmployeeCode", employeeCodeByMobilePhone1);
						resultMap.put("identity", "U");
						return resultMap;// 返回标识【U】代表可更新该数据【此返回只做为当员工代码和公众号均为空，手机号不为空的情况下执行】
					}
				}
			}
			// 判断查询到的员工代码不为空时，执行以下判断逻辑
			if(!CherryBatchUtil.isBlankString(employeeCode)){
				if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("OpenID")))){
					Map<String,Object> openIdMap1=new HashMap<String, Object>();
					openIdMap1.putAll(commonMap);
					openIdMap1.put("openId", paramMap.get("OpenID"));
					String employeeCodeByOpenId1 = maintenEmployeeService.getEmployeeCode(openIdMap1);
					if(!CherryBatchUtil.isBlankString(employeeCodeByOpenId1)){//如果查询数据不为空，排除员工代码，则根据公众号进行查询数据
						resultMap.put("ERRORCODE", "WSE0029");
						resultMap.put("ERRORMSG", "参数中的OpenID在系统中已存在");
						return resultMap;
					}else{
						if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
							Map<String,Object> mobilePhoneMap2=new HashMap<String, Object>();
							mobilePhoneMap2.putAll(commonMap);
							mobilePhoneMap2.put("existsOpenId", paramMap.get("OpenID"));
							mobilePhoneMap2.put("existsMobilePhone", paramMap.get("MobilePhone"));
							String employeeCodeByMobilePhone2 = maintenEmployeeService.getEmployeeCode(mobilePhoneMap2);
							if(!CherryBatchUtil.isBlankString(employeeCodeByMobilePhone2)){
								resultMap.put("ERRORCODE", "WSE0059");
								resultMap.put("ERRORMSG", "指定的MobilePhone已被其他数据使用");
								return resultMap;
							}else{
								resultMap.put("identity", "U");
								return resultMap;//返回标识【U】更新该数据【此返回只做为当查询的员工代码和手机号及公众号均不为空的情况下执行】
							}
						}else{
							resultMap.put("identity", "U");
							return resultMap;//返回标识【U】更新该数据【此返回只做为当查询的员工代码和公众号不为空，手机号为空的情况下执行】
						}
					}	
				}else{
					if(!CherryBatchUtil.isBlankString(ConvertUtil.getString(paramMap.get("MobilePhone")))){
						Map<String,Object> mobilePhoneMap3=new HashMap<String, Object>();
						mobilePhoneMap3.putAll(commonMap);
						mobilePhoneMap3.put("singleMobilePhone", paramMap.get("MobilePhone"));
						String employeeCodeByMobile3 = maintenEmployeeService.getEmployeeCode(mobilePhoneMap3);
						if(!CherryBatchUtil.isBlankString(employeeCodeByMobile3)){
							resultMap.put("ERRORCODE", "WSE0059");
							resultMap.put("ERRORMSG", "指定的MobilePhone已被其他数据使用");
							return resultMap;
						}else{
							resultMap.put("identity", "U");
							return resultMap;//返回标识【U】更新该数据【此返回只做为当查询的员工代码和手机号不为空，公众号为空的情况下执行】
						}
					}else{
						resultMap.put("identity", "U");
						return resultMap;//返回标识【U】更新该数据【此返回只做为当查询的员工代码不为空，手机号和公众号均为空的情况下执行】
					}
				}
			}
		} catch (Exception e) {
			logger.error("WS ERROR:"+ e.getMessage(),e);
            logger.error("WS ERROR brandCode:"+ paramMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ paramMap.get("OriginParamData"));
			resultMap.put("ERRORCODE", "WSE0072");
			resultMap.put("ERRORMSG", "新增/更新时校验员工信息在其他数据中是否被占用时,发生未知异常");
			return resultMap;
		}
		return resultMap;

	}
	/**
	 * 新增员工信息【新增员工时判断员工号是否存在，存在则执行插入数据相关操作，不存在则自动生成员工代码】
	 * @param map
	 * @return
	 */
	private Map<String,Object> createEmpolyeeInfo(Map<String,Object> map){
		// 创建员工时员工代码为空时，系统自动生成员工代码
		String employeeCode = "0";
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("EmployeeCode")))) {
			try {
				Map<String, Object> employeeCodeMap = new HashMap<String, Object>();
				// 所属组织ID
				employeeCodeMap.put(CherryConstants.ORGANIZATIONINFOID,map.get("BIN_OrganizationInfoID"));
				// 品牌ID
				employeeCodeMap.put(CherryConstants.BRANDINFOID,map.get("BIN_BrandInfoID"));

				employeeCodeMap.put(CherryConstants.ORG_CODE,map.get("OrgCode"));
				employeeCodeMap.put(CherryConstants.BRAND_CODE,map.get("BrandCode"));
				employeeCodeMap.putAll(map);
				employeeCodeMap.put("CodeType", "1120");
				employeeCodeMap.put("CodeKey", "1");
				Map<String, Object> codeMap = maintenEmployeeService.getCoderByCodeType(employeeCodeMap);
				if (null == codeMap || codeMap.isEmpty()) {
					// 所属组织ID
					employeeCodeMap.put(CherryConstants.ORG_CODE, ConvertUtil.getString(CherryConstants.ORG_CODE_ALL));
					// 品牌IDorganizationInfoId
					employeeCodeMap.put(CherryConstants.BRAND_CODE, ConvertUtil.getString(CherryConstants.Brand_CODE_ALL));
					codeMap = maintenEmployeeService.getCoderByCodeType(employeeCodeMap);
				}
				employeeCodeMap.put("type", "1");
				employeeCodeMap.put("length", codeMap.get("Value2"));
				// 自动生成员工代码
				employeeCode = (String) codeMap.get("Value1") + binOLCM15_BL.getSequenceId(employeeCodeMap);
			} catch (Exception e) {
				logger.error("WS ERROR:" + e.getMessage(), e);
				logger.error("WS ERROR brandCode:" + comMap.get("BrandCode"));
				logger.error("WS ERROR paramData:" + comMap.get("OriginParamData"));
				resultMap.put("ERRORCODE", "WSE0066");
				resultMap.put("ERRORMSG", "员工代码自动生成时发生未知异常");
				return resultMap;
			}
		} else {
			employeeCode = ConvertUtil.getString(map.get("EmployeeCode"));
		}
		comMap.put("EmployeeCode", employeeCode);
		// 添加时判断查询新后台数据是否存在，不存在则新增
		Integer count = maintenEmployeeService.getEmployeeNum(comMap);
		if (count > 0) {
			resultMap.put("ERRORCODE", "WSE0057");
			resultMap.put("ERRORMSG", "指定的员工已经存在");
			return resultMap;
		} else {
			String categoryId = null;
			if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("PositionCode")))) {
				// 查询岗位代码Id
				categoryId = maintenEmployeeService.getCategoryCodeByCategoryID(comMap);
				// 查询岗位Id在新后台是否存在，不存在则返回以下回执码
				if (CherryBatchUtil.isBlankString(categoryId)) {
					resultMap.put("ERRORCODE", "WSE0060");
					resultMap.put("ERRORMSG", "未找到指定的岗位代码");
					return resultMap;
				}
			}
			try {
				// 新增员工信息
				Map<String, Object> addEmployeeMap = new HashMap<String, Object>();
				addEmployeeMap.putAll(map);
				addEmployeeMap.put("positionCategoryId", categoryId);
				addEmployeeMap.put("employeeStatus", "1");// 员工状态 1:正常
				int employeeId = maintenEmployeeService.insertEmployee(addEmployeeMap);
				if (employeeId != 0) {
					// 岗位代码为BA时，维护营业员信息
					if (CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("PositionCode")))) {
						// 新增营业员信息
						Map<String, Object> addBaInfoMap = new HashMap<String, Object>();
						addBaInfoMap.putAll(map);
						addBaInfoMap.put("employeeID", employeeId);
						maintenEmployeeService.insertBaInfo(addBaInfoMap);
					}
					// 新后台数据源提交
					maintenEmployeeService.manualCommit();
					returnParam.put("EmployeeID", employeeId);
					returnParam.put("EmployeeCode", employeeCode);
					resultMap.put("ResultMap", returnParam);
					return resultMap;
				}
			} catch (Exception e) {
				try {
					// 新后台数据源回滚
					maintenEmployeeService.manualRollback();
				} catch (Exception ex) {

				}

				logger.error("WS ERROR:" + e.getMessage(), e);
				logger.error("WS ERROR brandCode:" + comMap.get("BrandCode"));
				logger.error("WS ERROR paramData:" + comMap.get("OriginParamData"));
				resultMap.put("ERRORCODE", "WSE0067");
				resultMap.put("ERRORMSG", "新增员工信息时发生未知异常");
				return resultMap;
			}
		}
		return resultMap;
	}
	/**
	 * 更新员工信息
	 * @param map
	 * @return
	 */
	private Map<String,Object> updEmployeeInfo(Map<String,Object> map){

		try {
			Map<String, Object> updBaInfoMap = new HashMap<String, Object>();
			updBaInfoMap.putAll(map);
			if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("PositionCode")))) {
				// 原数据为BA，新传输参数为BA
				if (CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("CategoryCode"))) && 
					CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("PositionCode")))) {
					// 查询营业员数据是否存在，存在更新，不存在新增
					if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("baInfoId")))) {
						// 更新营业员信息
						updBaInfoMap.put(CherryBatchConstants.VALID_FLAG,CherryBatchConstants.VALIDFLAG_ENABLE);
						maintenEmployeeService.updateBaInfo(updBaInfoMap);
					} else {
						// 新增营业员信息
						maintenEmployeeService.insertBaInfo(updBaInfoMap);
					}
				}
				// 原数据为BA，新传输参数为非BA
				if (CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("CategoryCode"))) && 
					!CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("PositionCode")))) {
					// 查询营业员数据是否存在，存在更新，岗位代码和数据有效更改为无效，
					updBaInfoMap.put(CherryBatchConstants.VALID_FLAG,CherryBatchConstants.VALIDFLAG_ENABLE);
					maintenEmployeeService.updateBaInfo(updBaInfoMap);
				}
				// 原数据为非BA，新传输参数为BA
				if (!CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("CategoryCode"))) && 
					CherryBatchConstants.CATRGORY_CODE_BA.equals(ConvertUtil.getString(map.get("PositionCode")))) {
					// 查询营业员数据是否存在,不存在，新增营业员信息,存在，更新营业员信息
					if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("baInfoId")))) {
						// 更新营业员信息(更改数据有效性为有效)
						updBaInfoMap.put(CherryBatchConstants.VALID_FLAG,CherryBatchConstants.VALIDFLAG_ENABLE);
						maintenEmployeeService.updateBaInfo(updBaInfoMap);
					} else {
						// 新增营业员信息(更改数据有效性为有效)
						maintenEmployeeService.insertBaInfo(updBaInfoMap);
					}
				}
			}
			// 更新员工信息（数据有效性更改为有效，离职员工状态更改为正常）
			updBaInfoMap.put(CherryBatchConstants.VALID_FLAG,CherryBatchConstants.VALIDFLAG_ENABLE);
			updBaInfoMap.put("employeeStatus",1);
			maintenEmployeeService.updateEmployee(updBaInfoMap);

			// 新后台数据源提交
			maintenEmployeeService.manualCommit();
			returnParam.put("EmployeeID", map.get("employeeID"));
			returnParam.put("EmployeeCode", map.get("EmployeeCode"));
			resultMap.put("ResultMap", returnParam);
			return resultMap;
		} catch (Exception e) {
			try {
				// 新后台数据源回滚
				maintenEmployeeService.manualRollback();
			} catch (Exception ex) {
				
			}
			logger.error("WS ERROR:"+ e.getMessage(),e);
            logger.error("WS ERROR brandCode:"+ map.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ map.get("OriginParamData"));
			resultMap.put("ERRORCODE", "WSE0068");
			resultMap.put("ERRORMSG", "更新员工信息时发生未知异常");
			return resultMap;
		}
	
	}
}
