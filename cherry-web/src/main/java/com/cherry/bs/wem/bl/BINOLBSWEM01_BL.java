/*
 * @(#)BINOLBSWEM01_BL.java     1.0 2014/10/29
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
package com.cherry.bs.wem.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.interfaces.BINOLBSWEM01_IF;
import com.cherry.bs.wem.service.BINOLBSWEM01_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.client.WebserviceClient;


/**
 * 微商申请一览
 * 
 * @author hujh
 * @version 1.0 2015/08/03
 */
public class BINOLBSWEM01_BL implements BINOLBSWEM01_IF{
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM01_BL.class);
	
	@Resource(name="binOLBSWEM01_Service")
	private BINOLBSWEM01_Service binOLBSWEM01_Service;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public int getWechatMerchantApplyCount(Map<String, Object> map) {
		
		return binOLBSWEM01_Service.getWechatMerchantApplyCount(map);
	}

	@Override
	public List<Map<String, Object>> getWechatMerchantApplyList(Map<String, Object> map) {
		
		return binOLBSWEM01_Service.getWechatMerchantApplyList(map);
	}

	@Override
	public int getEmpCount(Map<String, Object> map) {
		
		return binOLBSWEM01_Service.getEmpCount(map);
	}
	
	@Override
	public List<Map<String, Object>> getEmpList(Map<String, Object> map) {
		
		return binOLBSWEM01_Service.getEmpList(map);
	}
	
	@Override
	public Map<String, Object> getApplyInfoById(Map<String, Object> map) {
		
		return binOLBSWEM01_Service.getApplyInfoById(map);
	}
	

	/**
	 * 分配上级
	 */
	@Override
	public void tran_assignSuperMobile(Map<String, Object> map) throws Exception {
		Map<String, Object> applyInfoMap = new HashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		String agentApplyId = ConvertUtil.getString(map.get("agentApplyId"));
		String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
		try {
			if(!CherryChecker.isNullOrEmpty(agentApplyId, true) && !CherryChecker.isNullOrEmpty(mobilePhone, true)) {
				applyInfoMap = this.getApplyInfoById(map);
				if(null != applyInfoMap && !applyInfoMap.isEmpty()) {
					binOLBSWEM01_Service.updateAssignInfo(map);
					//调用php的接口将该上级写入微商城的数据库中
					paramsMap.put("TradeType", "agentAudit");
					paramsMap.put("BillCode", ConvertUtil.getString(map.get("billCode")));
					paramsMap.put("AuditType", "0");//业务类型，0，指定上级；1，审批通过；2，审批拒绝
					paramsMap.put("AuditMobile", ConvertUtil.getString(map.get("assigner")));//操作人手机号，此处为操作人employeeCode
					paramsMap.put("SuperiorMobile", ConvertUtil.getString(map.get("mobilePhone")));
					paramsMap.put("ApplyMobile", ConvertUtil.getString(applyInfoMap.get("applyMobile")));
					takeAgentInterface(paramsMap);
					//记录到log表
					applyInfoMap = this.getApplyInfoById(map);
					String billCode = ConvertUtil.getString(applyInfoMap.get("billCode"));
					String status = ConvertUtil.getString(applyInfoMap.get("status"));
					String applyType = ConvertUtil.getString(applyInfoMap.get("applyType"));
					String applyMobile = ConvertUtil.getString(applyInfoMap.get("applyMobile"));
					if(!CherryChecker.isNullOrEmpty(billCode, true) && !CherryChecker.isNullOrEmpty(status, true)){
						map.put("billCode", billCode);
						map.put("status", status);//单据状态（分配上级之后的状态）
						map.put("logType", "2");//1：申请 2：分配 3：审核
						map.put("logSource", "2");//1：微商 2：总部
						map.put("applyType", applyType);//申请类型1：初始申请（无上级）2：初始申请（有上级） 3：修改上级申请 4：升级申请（无上级） 5：升级申请（有上级）
						map.put("applyMobile", applyMobile);//申请人手机号
						this.tran_addToLog(map);//分配操作添加到log表
					}
				}
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * 调用php的接口将该上级写入微商城的数据库中
	 * @param map
	 * @throws Exception 
	 */
	private void takeAgentInterface(Map<String, Object> map) throws Exception {
		//调用接口
		Map<String, Object> resultMap = WebserviceClient.accessWeshopWebService(map);
		String errCode = ConvertUtil.getString(resultMap.get("ERRORCODE"));
		String errMsg = ConvertUtil.getString(resultMap.get("ERRORMSG"));
		//调用失败
		if(!"0".equals(errCode)) {
			throw new Exception("调用php的接口失败！返回码：[" +errCode + "]，返回信息：["+errMsg+"]");
		}
	}
	
	/**
	 * 审核
	 * @throws Exception 
	 */
	@Override
	public void tran_audit(Map<String, Object> map) throws Exception {
		try {
			
			Map<String, Object> applyInfoMap = new HashMap<String, Object>();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			String agentApplyId = ConvertUtil.getString(map.get("agentApplyId"));
			applyInfoMap = this.getApplyInfoById(map);
			String auditFlag = ConvertUtil.getString(map.get("auditFlag"));
			
			paramsMap.put("TradeType", "agentAudit");
			paramsMap.put("BillCode", applyInfoMap.get("billCode"));
			paramsMap.put("AuditType", auditFlag);//业务类型，0，指定上级；1，审批通过；2，审批拒绝
			paramsMap.put("AuditMobile", map.get("auditEmployeeCode"));//操作人employeeCode
			paramsMap.put("SuperiorMobile", map.get("superMobile"));
			paramsMap.put("ApplyMobile", map.get("agentMobile"));
			paramsMap.put("AuditLevel", map.get("agentLevel"));
			takeAgentInterface(paramsMap);
			
			//将单据单据状态修改为审核通过或者不通过
			paramsMap.clear();
			paramsMap.put("agentApplyId", agentApplyId);
			paramsMap.put("applyName", map.get("agentName"));
			paramsMap.put("applyMobile", map.get("agentMobile"));
			paramsMap.put("applyProvince", map.get("agentProvince"));
			paramsMap.put("applyCity", map.get("agentCity"));
			paramsMap.put("superMobile", map.get("superMobile"));
			paramsMap.put("oldSuperMobile", map.get("oldSuperMobile"));
			paramsMap.put("reason", map.get("reason"));
			if("1".equals(auditFlag)) {
				paramsMap.put("status", "3");//审核通过
			} else if("2".equals(auditFlag)) {
				paramsMap.put("status", "4");//审核不通过
			}
			paramsMap.put("auditor", map.get("auditEmployeeCode"));//审核人
			paramsMap.put("auditTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
			paramsMap.put("auditLevel", map.get("agentLevel"));//审核等级
			paramsMap.put("updateBy", map.get("auditEmployeeCode"));
			paramsMap.put("updatePGM", "BINOLBSWEM01_BL");
			binOLBSWEM01_Service.audit(paramsMap);
			
			//操作记录到Log表中
			paramsMap.clear();
			applyInfoMap = this.getApplyInfoById(map);
			paramsMap.put("organizationInfoId", map.get("organizationInfoId"));
			paramsMap.put("brandInfoId", map.get("brandInfoId"));
			paramsMap.put("billCode", applyInfoMap.get("billCode"));
			paramsMap.put("status",  applyInfoMap.get("status"));//单据状态，1：待分配 2：待审核 3：审核通过 4：审核不通过
			paramsMap.put("logType", "3");//操作类型。1：申请 2：分配 3：审核
			paramsMap.put("logSource", "2");//1：微商 2：总部
			paramsMap.put("applyMobile", map.get("agentMobile"));
			paramsMap.put("auditor", map.get("auditEmployeeCode"));
			paramsMap.put("applyType", applyInfoMap.get("applyType"));
			paramsMap.put("superMobile", map.get("superMobile"));
			paramsMap.put("auditLevel", map.get("agentLevel"));
			paramsMap.put("reason", map.get("reason"));
			paramsMap.put("createdBy", map.get("auditEmployeeCode"));
			this.tran_addToLog(paramsMap);		
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 操作记录添加到Log表中
	 * @param map
	 */
	private void tran_addToLog(Map<String, Object> map) {
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String billCode = ConvertUtil.getString(map.get("billCode"));
		String status = ConvertUtil.getString(map.get("status"));
		String logType = ConvertUtil.getString(map.get("logType"));
		String logTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
		String logSource = ConvertUtil.getString(map.get("logSource"));
		String applyMobile = ConvertUtil.getString(map.get("applyMobile"));
		String assigner = ConvertUtil.getString(map.get("assigner"));//员工代码
		String auditor = ConvertUtil.getString(map.get("auditor"));//手机号
		String applyType = ConvertUtil.getString(map.get("applyType"));
		String superMobile = ConvertUtil.getString(map.get("superMobile"));
		String auditLevel = ConvertUtil.getString(map.get("auditLevel"));
		String reason = ConvertUtil.getString(map.get("reason"));//不通过理由
		String createdBy = ConvertUtil.getString(map.get("createdBy"));
		if(!CherryChecker.isNullOrEmpty(organizationInfoId, true) && !CherryChecker.isNullOrEmpty(brandInfoId, true) && 
				!CherryChecker.isNullOrEmpty(billCode, true) && !CherryChecker.isNullOrEmpty(status, true) && 
				!CherryChecker.isNullOrEmpty(logType, true)) {
			tempMap.put("organizationInfoId", organizationInfoId);
			tempMap.put("brandInfoId", brandInfoId);
			tempMap.put("billCode", billCode);
			tempMap.put("status", status);
			tempMap.put("logType", logType);
			tempMap.put("logTime", logTime);
			tempMap.put("logSource", logSource);
			tempMap.put("applyMobile", applyMobile);
			tempMap.put("assigner", assigner);
			tempMap.put("auditor", auditor);
			tempMap.put("applyType", applyType);
			tempMap.put("superMobile", superMobile);
			tempMap.put("auditLevel", auditLevel);
			tempMap.put("reason", reason);
			// 作成者
			tempMap.put(CherryConstants.CREATEDBY, createdBy);
			// 作成程序名
			tempMap.put(CherryConstants.CREATEPGM, "BINOLBSWEM01_BL");	
			// 作成日时
			tempMap.put(CherryConstants.CREATE_TIME,CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
			binOLBSWEM01_Service.addToLog(tempMap);
			
		}
	}

	/**
	 * 获取直接下级人数
	 */
	@Override
	public int getSubAmount(String employeeId) {
		int sum = 0;
		if(null != employeeId && !"".equals(employeeId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employeeId", employeeId);
			sum = binOLBSWEM01_Service.getSubAmount(map);
		} 
		return sum;
	}
	
	/**
	 * 只获取下级的employeeId
	 * @param employeeId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSubEmployeeIdList(String employeeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", employeeId);
		return binOLBSWEM01_Service.getSubEmployeeIdList(map);
	}

	/**
	 * @param employeeId
	 * @param organizationInfoId
	 * @param brandInfoId
	 * 获取该员工对应级别的限制人数
	 */
	@Override
	public int getLimitAmout(Map<String, Object> map) throws Exception{
		int limit = 999999;
		String employeeId = ConvertUtil.getString(map.get("employeeId"));
		if(null != employeeId && !"".equals(employeeId)) {
			Map<String, Object> configMap = getConfigMap(map);
			if(null != configMap && !configMap.isEmpty()) {
				Map<String, Object> resultMap = binOLBSWEM01_Service.getEmpAgentLevel(map);//获取员工部门类型，即微商等级
				if(null != resultMap && !configMap.isEmpty()) {
					String agentLevel = ConvertUtil.getString(resultMap.get("agentLevel"));
					if(null != agentLevel && !"".equals(agentLevel)) {
						limit = ConvertUtil.getInt(configMap.get(agentLevel));//获取对应等级的限制人数
					}
				}
			}
		}
		return limit;
	}

	/**
	 * 获取配置项中设定的值
	 * @param map
	 * @return
	 */
	private Map<String, Object> getConfigMap(Map<String, Object> map) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String configStr = binOLCM14_BL.getConfigValue("1331", organizationInfoId, brandInfoId);
		if(null != configStr && !"".equals(configStr)) {
			String [] configArr = configStr.split("_");//格式：A:1000_B:1000_C:1000_D:1000_E:1000
			for(int i = 0; i < configArr.length; i++) {
				String[] arr = configArr[i].split(":");
				tempMap.put(arr[0], arr[1]);
			}
		}
		return tempMap;
	}	
	
}