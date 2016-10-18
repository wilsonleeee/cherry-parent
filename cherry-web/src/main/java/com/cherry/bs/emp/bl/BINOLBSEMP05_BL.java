/*	
 * @(#)BINOLBSEMP05_BL.java     1.0 2011.05.17	
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

package com.cherry.bs.emp.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.common.service.BINOLBSCOM01_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP05_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.mo.interfaces.UdiskSynchro_IF;

/**
 * 
 * 停用启用员工BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.17
 */
public class BINOLBSEMP05_BL {
	
	/** 停用启用员工Service */
	@Resource
	private BINOLBSEMP05_Service binOLBSEMP05_Service;
	
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	@Resource
	private BINOLBSCOM01_Service binOLBSCOM01_Service;
	
	@Resource
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private CodeTable code;
	
	@Resource(name="binOLBSEMP04_BL")
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private UdiskSynchro_IF udiskSynchro;
	
	/**
	 * 停用启用员工
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void tran_updateEmployee(Map<String, Object> map) throws Exception {
		
		// 停用启用员工
		binOLBSEMP05_Service.updateEmployee(map);
		
		String validStatus = (String)map.get("validFlag");
		if(validStatus != null && "1".equals(validStatus)) {
			// 启用柜台主管对应的柜台主管部门
			binOLBSEMP05_Service.updateOrgValid(map);
		} else {
			// 停用不包含有效员工的柜台主管部门
			binOLBSEMP05_Service.updateOrgInvalid(map);
		}
		
		//到营业员信息表中查询出对应的营业员ID
		List<Map<String,Object>> baInfoIdList = binOLBSEMP05_Service.getBaInfoIdList(map);
		if(null != baInfoIdList && !baInfoIdList.isEmpty()){
			map.put("baInfoIdList", baInfoIdList);
			//更新营业员信息表
			binOLBSEMP05_Service.updateBaInfo(map);
		}
		
		//发送MQ消息
		List<String> employeeIdList = (List<String>) map.get("employeeId");
		//是否开启维护BAS时发送MQ消息配置项
		boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		Map<String,Object> MqParamMap = null;
		if(employeeIdList != null && employeeIdList.size() > 0){
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.putAll(map);
			for(String employeeId : employeeIdList){
				
				//取得该人员的基本信息包括岗位类别
				paraMap.put("employeeId", employeeId);
				//停用人员时若有U盘绑定则解除其绑定
				if(validStatus != null && "0".equals(validStatus)){
					// 根据员工ID取得U盘信息【序列号、ID以及品牌CODE】
					List<Map<String, Object>> udiskSNList = binOLBSEMP05_Service.getUdiskInfo(paraMap);
					if(!udiskSNList.isEmpty() && udiskSNList.size() > 0){
						// 若存在绑定的u盘则需要解绑
						this.unbindUdisk(map,udiskSNList);
					}
				}
				Map<String,Object> employeeInfo = binOLBSCOM01_Service.getEmployeeInfo(paraMap);
				String categoryCode = (String) employeeInfo.get("categoryCode");
				
				//如果是BA或者BAS则发送MQ消息
				if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)||(CherryConstants.CATRGORY_CODE_BAS.equals(categoryCode) && sendBasMQ)){
					//有效性区分，有效则绑定关系为新增，无效绑定关系为解除
					String validFlag = ConvertUtil.getString(map.get("validFlag"));
					if(MqParamMap == null){
						MqParamMap = new HashMap<String,Object>();
						//组织
						MqParamMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
						// 组织代号
						MqParamMap.put("orgCode", map.get("orgCode"));
						//登录名
						MqParamMap.put("loginName", map.get("loginName"));
						//品牌
						MqParamMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
						//创建者
						MqParamMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
						//创建程序名
						MqParamMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
						// 更新者
						MqParamMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
						// 更新程序名
						MqParamMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
						MqParamMap.put("validFlag", validFlag);
					}
					//如果是BA则取得关注和归属柜台，如果是BAS取得管辖和归属柜台
					paraMap.put("BasFlag", CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)? "":"true");
					List<Map<String,Object>> countersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(paraMap);
					if(countersList != null && !countersList.isEmpty()){
						for(Map<String,Object> counterInfo : countersList){
							// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
							//绑定关系	-1 解除；1新增；0绑定关系维持不变
							//counterInfo.put("Flag", "1".equals(validFlag)? CherryConstants.BA_COUNTERS_ADD : CherryConstants.BA_COUNTERS_DELETE);// 据MQ接口定义中BAS考勤信息，会员信息，BA信息文档在20121009的定义(定义为"20121009以前定的是按差分来发，现在只发当前有效的绑定关系，该字段去除")
							// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
							
							counterInfo.put("EmployeeCode", employeeInfo.get("employeeCode"));
						}
					}
					
					Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(paraMap, countersList,CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)?"BA":"BAS");
					if(MQMap.isEmpty()) return;
					//设定MQInfoDTO
					MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,MqParamMap);
					//调用共通发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
				}
			
			}
			
		}
		
		List<String> userId = (List)map.get("userId");
		if(userId != null && !userId.isEmpty()) {
			// 停用启用用户
			binOLBSEMP05_Service.updateUser(map);
			
			String orgId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
			String brandId = map.get(CherryConstants.BRANDINFOID).toString();
			// 对BI用户进行操作的场合
			if(binolbsemp04BL.isCreateBIUser(orgId, brandId)) {
				// BI帐号停用启用处理
				Map<String, Object> param = new HashMap<String, Object>();
				for(String s : userId) {
					param.put("userId", s);
					Map<String, Object> userInfo = binOLBSEMP05_Service.getUserInfo(param);
					// 有对应的BI账号的场合
					if(userInfo != null && userInfo.get("biFlag") != null && "1".equals(userInfo.get("biFlag").toString())) {
						// BI用户名
						String biLoginName = (String)userInfo.get("longinName");
						// BI用户组名
						String biGroupName = null;
						// 从CodeTable中取得用户名的前缀和用户组名
						Map<String, Object> codeMap = new HashMap<String, Object>();
						int brandInfoId = (Integer)userInfo.get("brandInfoId");
						if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
							codeMap = code.getCode("1179", brandInfoId);
						} else {
							String brandCode = binolbsemp04Service.getBrandCode(userInfo);
							codeMap = code.getCode("1179", brandCode);
						}
						if(codeMap != null && !codeMap.isEmpty()) {
							String pre = (String)codeMap.get("value1");
							if(pre != null && !"".equals(pre)) {
								biLoginName = pre + biLoginName;
							}
							biGroupName = (String)codeMap.get("value2");
						} else {
							throw new CherryException("EBS00062");
						}
						String validFlag = (String)map.get("validFlag");
						String brandCode = binOLCM05_BL.getBrandCode(Integer.parseInt(brandId));
						if(validFlag != null && "1".equals(validFlag)) {
							// 解密处理
							DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
							String passWord = des.decrypt((String)userInfo.get("passWord"));
							// 创建BI用户
							binolbsemp04Service.createBIUser(biLoginName, passWord, biGroupName,brandCode);
						} else {
							// 删除BI用户
							binOLBSEMP05_Service.dropBIUser(biLoginName,brandCode);
						}
					}
				}
			}
			
			// 停用启用用户（配置数据库）
			binOLBSEMP05_Service.updateUserConf(map);
		}
	}

	/**
	 * 停用人员时解除其所绑定的U盘
	 * 
	 * @param map
	 * @param udiskSNList
	 *            :停用人员（有绑定U盘）对应的U盘信息【序列号、ID以及品牌CODE】
	 * @throws Exception
	 */
	private void unbindUdisk(Map<String, Object> map,
			List<Map<String, Object>> udiskSNList) throws Exception {
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> mapOfList : udiskSNList) {
			mapOfList.putAll(map);
			//解除U盘绑定的参数
			mapOfList.put("employeeId", null);
			mapOfList.put("ownerRight", 0);
			updateList.add(mapOfList);
		}
		binOLBSEMP05_Service.empUnbindUdisk(updateList);

		// 调用存储过程进行同步老后台数据
		for (Map<String, Object> tempMap : udiskSNList) {
			Map<String, Object> synchroMap = new HashMap<String, Object>();
			synchroMap.put("UDiskSN", tempMap.get("udiskSn"));
			synchroMap.put("BrandCode", tempMap.get("brandCode"));
			udiskSynchro.unbindUdisk(synchroMap);
		}
	}

}
