package com.cherry.webservice.agent.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM43_BL;
import com.cherry.cm.cmbussiness.dto.AgentInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.webservice.agent.interfaces.AgentInfo_IF;
import com.cherry.webservice.agent.service.AgentInfoService;

/**
 * 微商接口BL
 * 
 * @author WangCT
 * @version 2015-08-04 1.0.0
 */
public class AgentInfoLogic implements AgentInfo_IF {
	
	/** 微商接口Service */
	@Resource
	private AgentInfoService agentInfoService;
	
	/** 微商管理BL */
	@Resource
	private BINOLCM43_BL binOLCM43_BL;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private CodeTable codeTable;
	
	/**
	 * 微商申请接口
	 * 
	 */
	@Override
	public Map<String, Object> tran_agentApply(Map<String, Object> map) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String applyType = (String)map.get("ApplyType");
		String billCode = (String)map.get("BillCode");
		String applyTime = (String)map.get("ApplyTime");
		String applyMobile = (String)map.get("ApplyMobile");
		String superMobile = (String)map.get("SuperMobile");
		String oldSuperMobile = (String)map.get("OldSuperMobile");
		String applyLevel = (String)map.get("ApplyLevel");
		// 必填字段验证
		if(applyType == null || "".equals(applyType)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ApplyType必填。");
			return retMap;
		}
		if(billCode == null || "".equals(billCode)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BillCode必填。");
			return retMap;
		}
		if(applyTime == null || "".equals(applyTime)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ApplyTime必填。");
			return retMap;
		}
		if(applyMobile == null || "".equals(applyMobile)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ApplyMobile必填。");
			return retMap;
		}
		// 申请类型为修改上级申请时上级手机号和原上级手机号必填
		if("3".equals(applyType)) {
			/*修改上级时上级手机号改为可不填-2015.09.11
			if(superMobile == null || "".equals(superMobile)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数SuperMobile必填。");
				return retMap;
			}*/
			if(oldSuperMobile == null || "".equals(oldSuperMobile)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数OldSuperMobile必填。");
				return retMap;
			}
		}
		// 申请类型为升级申请时申请等级必填
		if("4".equals(applyType) || "5".equals(applyType)) {
			if(applyLevel == null || "".equals(applyLevel)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数ApplyLevel必填。");
				return retMap;
			}
		}
		// 申请手机号是否为微商验证（初始申请时必须为不存在的微商，修改上级和申请升级时必须是存在的微商）
		if("1".equals(applyType) || "2".equals(applyType)) {
			Map<String, Object> agentExist = binOLCM43_BL.getAgentExistByMobile(applyMobile);
			if(agentExist != null) {
				retMap.put("ERRORCODE", "WSE1000");
				retMap.put("ERRORMSG", "申请的微商已存在。");
				return retMap;
			}
			String applyOpenID = (String)map.get("ApplyOpenID");
			if(applyOpenID != null && !"".equals(applyOpenID)) {
				agentExist = binOLCM43_BL.getAgentExistByOpenID(applyOpenID);
				if(agentExist != null) {
					retMap.put("ERRORCODE", "WSE1001");
					retMap.put("ERRORMSG", "使用的微信号已绑定过微商。");
					return retMap;
				}
			}
		} else {
			Map<String, Object> agentExist = binOLCM43_BL.getAgentExistByMobile(applyMobile);
			if(agentExist == null) {
				retMap.put("ERRORCODE", "WSE1002");
				retMap.put("ERRORMSG", "申请的微商不存在。");
				return retMap;
			}
		}
		// 上级手机号是否为微商验证（必须为存在的微商）
		if(superMobile != null && !"".equals(superMobile)) {
			Map<String, Object> agentInfo = binOLCM43_BL.getAgentInfo(superMobile);
			if(agentInfo == null) {
				retMap.put("ERRORCODE", "WSE1003");
				retMap.put("ERRORMSG", "上级手机号不存在相应的微商。");
				return retMap;
			}
		}
		// 单据号已经存在，重复提交
		Map<String, Object> agentApplyInfo = agentInfoService.getAgentApplyInfo(map);
		if(agentApplyInfo != null) {
			retMap.put("ERRORCODE", "WSE1004");
			retMap.put("ERRORMSG", "单据号已经存在，重复提交。");
			return retMap;
		}
		// 申请的手机号有未完成的申请单据，不能再次申请
		List<Map<String, Object>> agentApplyExist = agentInfoService.getAgentApplyExist(map);
		if(agentApplyExist != null && !agentApplyExist.isEmpty()) {
			retMap.put("ERRORCODE", "WSE1005");
			retMap.put("ERRORMSG", "申请的手机号有未完成的申请单据，不能再次申请。");
			return retMap;
		}
		
		//如果单据类型为“3、4、5”，则设置单据为待审核，否则根据手机号进行判断，上级手机号存在设置单据为待审核状态，否则设置为待分配状态
		if("3".equals(applyType) || "4".equals(applyType) || "5".equals(applyType)) {
			map.put("Status", "2");
		} else {
			if(superMobile != null && !"".equals(superMobile)) {
				map.put("Status", "2");
			} else {
				map.put("Status", "1");
			}
		}
		
		this.setCommonParam(map);
		// 添加微商申请单据
		agentInfoService.addAgentApply(map);
		
		// 添加微商申请操作履历
		map.put("LogType", "1");
		map.put("LogTime", map.get("ApplyTime"));
		map.put("LogSource", "1");
		agentInfoService.addAgentApplyLog(map);
		
		return retMap;
	}

	/**
	 * 微商审批接口
	 * 
	 */
	@Override
	public Map<String, Object> tran_agentAudit(Map<String, Object> map) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String billCode = (String)map.get("BillCode");
		String auditResult = (String)map.get("AuditResult");
		String auditor = (String)map.get("Auditor");
		String auditTime = (String)map.get("AuditTime");
		String auditLevel = (String)map.get("AuditLevel");
		// 必填字段验证
		if(billCode == null || "".equals(billCode)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BillCode必填。");
			return retMap;
		}
		if(auditResult == null || "".equals(auditResult)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AuditResult必填。");
			return retMap;
		}
		if(auditor == null || "".equals(auditor)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Auditor必填。");
			return retMap;
		}
		if(auditTime == null || "".equals(auditTime)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AuditTime必填。");
			return retMap;
		}
		if(auditResult != null && "1".equals(auditResult)) {
			if(auditLevel == null || "".equals(auditLevel)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数AuditLevel必填。");
				return retMap;
			}
		}
		// 审核的单据不存在
		Map<String, Object> agentApplyInfo = agentInfoService.getAgentApplyInfo(map);
		if(agentApplyInfo == null) {
			retMap.put("ERRORCODE", "WSE1006");
			retMap.put("ERRORMSG", "审核的单据不存在。");
			return retMap;
		}
		String status = (String)agentApplyInfo.get("status");
		if(status != null && !"2".equals(status)) {
			retMap.put("ERRORCODE", "WSE1007");
			retMap.put("ERRORMSG", "非审核单据，不能进行审核操作，请确认单据是否已审核。");
			return retMap;
		}
		// 审核人手机号不存在相应的微商
		Map<String, Object> agentInfo = binOLCM43_BL.getAgentInfo(auditor);
		if(agentInfo == null) {
			retMap.put("ERRORCODE", "WSE1008");
			retMap.put("ERRORMSG", "审核人手机号不存在相应的微商。");
			return retMap;
		}
		
		String applyMobile = (String)agentApplyInfo.get("applyMobile");
		if(auditResult != null && "1".equals(auditResult)) {
			int orgId = (Integer)map.get("BIN_OrganizationInfoID");
			int brandId = (Integer)map.get("BIN_BrandInfoID");
			String applyName = (String)agentApplyInfo.get("applyName");
			String applyOpenID = (String)agentApplyInfo.get("applyOpenID");
			String applyProvince = (String)agentApplyInfo.get("applyProvince");
			String applyCity = (String)agentApplyInfo.get("applyCity");
			String orgPath = (String)agentInfo.get("orgPath");
			String empPath = (String)agentInfo.get("empPath");
			AgentInfo agentBean = new AgentInfo();
			agentBean.setOrganizationInfoId(orgId);
			agentBean.setBrandInfoId(brandId);
			agentBean.setAgentMobile(applyMobile);
			agentBean.setAgentName(applyName);
			agentBean.setAgentProvince(applyProvince);
			agentBean.setAgentCity(applyCity);
			agentBean.setAgentLevel(auditLevel);
			agentBean.setAgentOpenID(applyOpenID);
			agentBean.setSupOrgPath(orgPath);
			agentBean.setSupEmpPath(empPath);
			agentBean.setCreatedBy("cherryws");
			agentBean.setCreatePGM("AgentInfoLogic");
			agentBean.setUpdatedBy("cherryws");
			agentBean.setUpdatePGM("AgentInfoLogic");
			// 创建微商处理
			binOLCM43_BL.tran_createAgent(agentBean);
			
			map.put("Status", "3");
		} else {
			map.put("Status", "4");
		}
		
		this.setCommonParam(map);
		// 审核单据处理
		agentInfoService.auditAgentApply(map);
		
		// 添加微商审核操作履历
		map.put("LogType", "3");
		map.put("LogTime", auditTime);
		map.put("LogSource", "1");
		map.put("ApplyMobile", applyMobile);
		agentInfoService.addAgentApplyLog(map);
		
		return retMap;
	}

	/**
	 * 查询微商信息接口
	 * 
	 */
	@Override
	public Map<String, Object> getAgentInfo(Map<String, Object> map) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String agentMobile = (String)map.get("AgentMobile");
		// 必填字段验证
		if(agentMobile == null || "".equals(agentMobile)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AgentMobile必填。");
			return retMap;
		}
		Map<String,Object> agentInfo = binOLCM43_BL.getAgentInfo(agentMobile);
		/*修改为根据employeeCode查询20151117
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("EmployeeCode", agentMobile);
		Map<String, Object> agentInfo = agentInfoService.getAgentInfoByCode(tempMap);
		*/
		if(agentInfo != null) {
			retMap.put("ResultMap", agentInfo);
		}
		return retMap;
	}
	
	/**
	 * 微商绑定接口
	 * 
	 */
	@Override
	public Map<String, Object> tran_agentBind(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String agentMobile = (String)map.get("AgentMobile");
		String agentOpenID = (String)map.get("AgentOpenID");
		// 必填字段验证
		if(agentMobile == null || "".equals(agentMobile)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AgentMobile必填。");
			return retMap;
		}
		if(agentOpenID == null || "".equals(agentOpenID)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AgentOpenID必填。");
			return retMap;
		}
		Map<String, Object> agentExist = binOLCM43_BL.getAgentExistByMobile(agentMobile);
		if(agentExist == null) {
			retMap.put("ERRORCODE", "WSE1010");
			retMap.put("ERRORMSG", "微商不存在。");
			return retMap;
		}
		String openID = (String)agentExist.get("openID");
		if(openID != null && !"".equals(openID)) {
			if(agentOpenID.equals(openID)) {
				return retMap;
			} else {
				retMap.put("ERRORCODE", "WSE1011");
				retMap.put("ERRORMSG", "微商已与其他微信号绑定。");
				return retMap;
			}
		}
		Map<String, Object> agentExistByOpenID = binOLCM43_BL.getAgentExistByOpenID(agentOpenID);
		if(agentExistByOpenID != null) {
			retMap.put("ERRORCODE", "WSE1012");
			retMap.put("ERRORMSG", "使用的微信号已绑定过其他微商。");
			return retMap;
		}
		
		map.put("employeeId", agentExist.get("employeeId"));
		agentInfoService.agentBind(map);
		
		return retMap;
	}
	
	/**
	 * 共通参数设置
	 * 
	 */
	public void setCommonParam(Map<String, Object> map) {
		// 作成者
		map.put(CherryConstants.CREATEDBY, "cherryws");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "AgentInfoLogic");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "cherryws");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "AgentInfoLogic");
	}
	
	/**
	 * 获取微店名称
	 */
	public Map<String, Object> getAgentShopName(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String agentMobile = (String)map.get("AgentMobile");
		// 必填字段验证
		if(agentMobile == null || "".equals(agentMobile)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AgentMobile必填。");
			return retMap;
		}
		try {
			String brandInfoId =  ConvertUtil.getString(map.get("BIN_BrandInfoID"));
			String organizationInfoId =  ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			tempMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			String shopName = getShopName(tempMap);
			//更新柜台名称，部门名称等为微店名称
			Map<String, Object> agentInfoMap = binOLCM43_BL.getAgentInfo(agentMobile);
			if(null != agentInfoMap && !agentInfoMap.isEmpty()) {
				AgentInfo agentInfo = CampRuleUtil.map2Bean(agentInfoMap, AgentInfo.class);
				agentInfo.setDepartName(shopName);
				binOLCM43_BL.tran_updateAgent(agentInfo);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("ShopName", shopName);
			retMap.put("ResultMap", resultMap);
			return retMap;
		} catch (Exception e) {
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "获取微店名称失败");
			return retMap;
		}
	} 

	private String getShopName(Map<String, Object> map) {
		String shopCode = null;
		String shopName = null;
		String brandInfoId =  ConvertUtil.getString(map.get("brandInfoId"));
		String organizationInfoId =  ConvertUtil.getString(map.get("organizationInfoId"));
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		tempMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		tempMap.put("type", "L");
		while(!checkedShopCode(shopCode, brandInfoId, organizationInfoId)) {
			shopCode = getSequenceId(tempMap);
		}
		//获取前后缀，拼接微店名称
		String pre_suffix = binOLCM14_BL.getConfigValue("1335", organizationInfoId, brandInfoId);
		String []pre_suffixArr = pre_suffix.split("/");//配置项通过"/"分隔
		String prefix = pre_suffixArr[0];
		String suffix = pre_suffixArr[1];
		shopName = prefix + shopCode + suffix;//微店名称
		return shopName;
	} 
	
	
	//获取code
	private String getSequenceId(Map<String, Object> map) {
		String temp = binOLCM15_BL.getNoPadLeftSequenceId(map);
		//如果含有4，将含有4的位数+1并更新到数据库中
		if(temp.contains("4")) {
			char []arr = temp.toCharArray();
			for(int i = 0; i < arr.length; i++) {
				if('4' == arr[i]) {
					arr[i]++;
				}
			}
			temp = String.valueOf(arr);
			map.put("TicketNo", temp);
			setCommonParam(map);
			agentInfoService.updateSequenceId(map);//更新取号表
		}
		return temp;
	}
	
	private boolean checkedShopCode(String shopCode, String brandInfoId, String organizationInfoId) {
		boolean flag = false;
		//不能包含数字4
		if(!CherryChecker.isNullOrEmpty(shopCode)) {
			if(!shopCode.contains("4")) {
				//判断是否是预留号
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(CherryConstants.BRANDINFOID, brandInfoId);
				map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
				map.put("ReservedCode", shopCode);
				Map<String, Object> retMap = agentInfoService.getReservedCode(map);
				if(null == retMap) {
					flag = true;
				}
			}
		}
		return flag;
	}

	
	/** 修改微商手机号或者帐户信息*/
	@SuppressWarnings("unchecked")
	public Map<String, Object> tran_updateAgentMobOrAccInfo (Map<String, Object> map) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = null;
		String organizationInfoId = String.valueOf(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = String.valueOf(map.get("BIN_BrandInfoID"));
		String agentCode = (String)(map.get("AgentCode"));
		String mobilePhone = (String)(map.get("MobilePhone"));
		String account = (String)(map.get("Account"));
		String accountName = (String)(map.get("AccountName"));
		String accountType = (String)(map.get("AccountType"));
		String bankInfo = (String)map.get("BankInfo");
		String provinceName = (String)map.get("ProvinceName");
		String cityName = (String)map.get("CityName");
		
		int count = 0;
		//参数校验
		if(CherryChecker.isNullOrEmpty(agentCode)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数AgentCode必填。");
			return retMap;
		}
		
		//判断微商是否存在
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("BIN_OrganizationInfoID", organizationInfoId);
		tempMap.put("BIN_BrandInfoID", brandInfoId);
		tempMap.put("EmployeeCode", agentCode);
		Map<String, Object> empExistsMap = agentInfoService.getEmpExistsMap(tempMap);
		String employeeId = null;
		String existMobilePhone = null;
		if(null == empExistsMap) {
			retMap.put("ERRORCODE", "WSE0002");
			retMap.put("ERRORMSG", "未找到指定的微商，AgentCode=" + agentCode);
			return retMap;
		} else {
			employeeId = String.valueOf(empExistsMap.get("BIN_EmployeeID"));
			existMobilePhone = (String)(empExistsMap.get("MobilePhone"));
		}
		
		//手机号校验
		if(!CherryChecker.isNullOrEmpty(mobilePhone)) {
			String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			if(!CherryChecker.isPhoneValid(mobilePhone, mobileRule)) {
				retMap.put("ERRORCODE", "WSE0031");
				retMap.put("ERRORMSG", "参数MobilePhone参数不合法。MobilePhone=" + mobilePhone);
				return retMap;
			}
			
			if(!(mobilePhone).equals(existMobilePhone)) {//如果和当前手机号不同
				//判断手机号是否存在
				Map<String, Object> mobileExists = binOLCM43_BL.getAgentExistByMobile(mobilePhone);
				if(null != mobileExists) {
					retMap.put("ERRORCODE", "WSE1000");
					retMap.put("ERRORMSG", "手机号" + mobilePhone + "已存在");
					return retMap;
				}
			}
			
		} else {
			count++;
		}
		
		//验证帐户,暂不判断帐号是否存在
		if(!CherryChecker.isNullOrEmpty(account)) {
			if(CherryChecker.isNullOrEmpty(accountName)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "若填写了收款帐号，参数账户名称AccountName必填");
				return retMap;
			}
			//如果帐户类型为空，则取默认的帐户类型BC
			if(CherryChecker.isNullOrEmpty(accountType)) {
				accountType = "BC";
			} else {
				//验证帐户类型是否存在
				List accountTypeList = codeTable.getCodesByBrand("1335", organizationInfoId, brandInfoId);
				if(null == accountTypeList || accountTypeList.isEmpty()) {
					retMap.put("ERRORCODE", "WSE9999");
					retMap.put("ERRORMSG", "系统未配置员工帐户类型");
					return retMap;
				} else {
					boolean flag = false;
					for(int i = 0; i < accountTypeList.size(); i ++) {
						Map<String, Object> accountTemp = (Map<String, Object>) accountTypeList.get(i);
						String codeKey = String.valueOf(accountTemp.get("CodeKey"));
						if(!CherryChecker.isNullOrEmpty(codeKey) && codeKey.equals(accountType)) {
							flag = true;
 						}
					}
					if(!flag) {
						retMap.put("ERRORCODE", "WSE9993");
						retMap.put("ERRORMSG", "未找到指定的账户类型AccountType=" + accountType);
						return retMap;
					}
				}
			}
			//如果不是微信帐号或支付宝帐号，则对帐号进行校验
			if (!"ALIPAY".equals(accountType) && !"WEPAY".equals(accountType)) {
				String bankCardRule = binOLCM14_BL.getConfigValue("1337", organizationInfoId, brandInfoId);
				if(!CherryChecker.isBankCardValid(account, bankCardRule)) {
					retMap.put("ERRORCODE", "WSE9993");
					retMap.put("ERRORMSG", "参数帐号Account格式不正确。账户类型AccountType=" + accountType + "，帐号Account=" + account);
					return retMap;
				}
			}
		} else {
			count++;
		}
		
		if(count == 2) {//手机号，帐户均为空
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone和Account均为空，微商信息不更新。参数Map=" + map);
			return retMap;
		}
		
		paramMap = new HashMap<String, Object>();
		paramMap.put("BIN_OrganizationInfoID", organizationInfoId);
		paramMap.put("BIN_BrandInfoID", brandInfoId);
		setCommonParam(paramMap);
		if(!CherryChecker.isNullOrEmpty(mobilePhone) && !(mobilePhone).equals(existMobilePhone)) {
			paramMap.put("EmployeeCode", agentCode);//更新微商手机号
			paramMap.put("MobilePhone", mobilePhone);
			agentInfoService.updateAgentMobile(paramMap);
		}
		
		if(!CherryChecker.isNullOrEmpty(employeeId) && !CherryChecker.isNullOrEmpty(account) && !CherryChecker.isNullOrEmpty(accountName)) {
			paramMap.put("BIN_EmployeeID", employeeId);//更新微商账户
			agentInfoService.deleteAccInfo(paramMap);//删除原有的账户信息
			paramMap.put("Account", account);
			paramMap.put("AccountName", accountName);
			paramMap.put("AccountType", accountType);
			paramMap.put("BankInfo", bankInfo);
			paramMap.put("ProvinceName", provinceName);
			paramMap.put("CityName", cityName);
			
			agentInfoService.updateAgentMobOrAccInfo(paramMap);
		}
		
		return retMap;
	}
	
}
