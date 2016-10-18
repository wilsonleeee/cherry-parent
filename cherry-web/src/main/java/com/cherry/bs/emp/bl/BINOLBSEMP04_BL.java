/*		
 * @(#)BINOLBSEMP04_BL.java     1.0 2010/12/22		
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

import com.cherry.bs.cnt.bl.BINOLBSCNT03_BL;
import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.dep.service.BINOLBSDEP03_Service;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP05_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 添加员工 BL
 * 
 * @author lipc
 * @version 1.0 2010.12.22
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP04_BL {

	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	/** 添加部门画面Service */
	@Resource(name="binOLBSDEP04_Service")
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 更新部门画面Service */
	@Resource(name="binOLBSDEP03_Service")
	private BINOLBSDEP03_Service binOLBSDEP03_Service;
	
	/** 停用启用员工Service */
	@Resource(name="binOLBSEMP05_Service")
	private BINOLBSEMP05_Service binOLBSEMP05_Service;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 创建柜台画面Service */
	@Resource
	private BINOLBSCNT04_Service binOLBSCNT04_Service;

	@Resource(name="binOLPLUPM04_BL")
	private BINOLPLUPM04_BL binOLPLUPM04_BL;
	
	/** 更新柜台画面BL */
	@Resource(name="binOLBSCNT03_BL")
	private BINOLBSCNT03_BL binOLBSCNT03_BL;
	
	/** MongoDB 用户关注管辖部门履历表 **/
	public static final String MGO_DEPART_PRIVILEGE_LOG_NAME = "MGO_DepartPrivilegeLog";
	
	
	/***
	 * 加密身份证号、电话、email数据信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void encryptData(Map<String, Object> map) throws Exception {
		// 所谓品牌Code，【加密参数】
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// String brandCode = "-9999";
		if (!CherryChecker.isNullOrEmpty(map.get("identityCard"), true)) {
			String identityCard = ConvertUtil
					.getString(map.get("identityCard"));
			map.put("identityCard",
					CherrySecret.encryptData(brandCode, identityCard));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("phone"), true)) {
			String phone = ConvertUtil.getString(map.get("phone"));
			map.put("phone", CherrySecret.encryptData(brandCode, phone));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone",
					CherrySecret.encryptData(brandCode, mobilePhone));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("email"), true)) {
			String email = ConvertUtil.getString(map.get("email"));
			map.put("email", CherrySecret.encryptData(brandCode, email));
		}
	}
	
	/**
	 * 添加员工信息
	 * 
	 * @param map
	 * @return
	 */
	public void tran_insertEmployee(UserInfo userInfo, Map<String, Object> map)
			throws CherryException, Exception {
		
		// 上级不存在的场合，设上级为默认的根节点
		if(map.get("higher") == null || "".equals(map.get("higher"))) {
			map.put("path", CherryConstants.DUMMY_VALUE);
		} else {
			map.put("path", map.get("higher"));
		}
		// 取得新节点
		String newEmpNodeId = binolbsemp04Service.getNewEmpNodeId(map);
		map.put("newNodeId", newEmpNodeId);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 组织代号
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		map.put("loginName", userInfo.getLoginName());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
		// 数据库系统时间
		String sysDate = binolbsemp04Service.getSYSDate();
		// 数据库系统时间(YYYY-MM-DD HH：mm：SS)
		map.put("systemTime", sysDate.substring(0, 19));
		// 数据库系统日期(YYYY-MM-DD)
		map.put("systemDate", sysDate.substring(0, 10));
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		//语言
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 【加密】人员的身份证、电话、email信息
		this.encryptData(map);
		
		String creatOrgFlag = (String)map.get("creatOrgFlag");
		// 需要自动创建柜台主管部门的场合，添加柜台主管部门处理
		if(creatOrgFlag != null) {
			int departId = creatOrgHandle(map);
			map.put("departId", departId);
		}
		
		// 插入员工信息表，返回员工ID
		int employeeId = binolbsemp04Service.insertEmployee(map);
		// 员工ID
		map.put("employeeId", employeeId);
		
		// 员工地址信息
		String addressInfo = (String) map.get("addressInfo");
		if (!CherryChecker.isNullOrEmpty(addressInfo, true)) {
			// 员工地址List
			List<Map<String, Object>> addrList = (List<Map<String, Object>>) JSONUtil
					.deserialize(addressInfo);
			if (null != addrList) {
				for (Map<String, Object> addr : addrList) {
					Map<String, Object> addrMap = CherryUtil
							.removeEmptyVal(addr);
					if(null != addrMap.get("address")){
						addrMap.putAll(map);
						// 插入地址信息表，返回地址信息ID
						int addressInfoId = binolbsemp04Service
								.insertAddrInfo(addrMap);
						addrMap.put("addressInfoId", addressInfoId);
						// 插入员工地址信息表
						binolbsemp04Service.insertEmpAddress(addrMap);
					}
				}
			}
		}
		// 插入员工入职信息
		binolbsemp04Service.insertEmpQuit(map);
		
		// 员工管辖部门对应List
		List<Map<String, Object>> departList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> followDepartList = new ArrayList<Map<String,Object>>();
		List<String> followCounterList = new ArrayList<String>();
		List<String> followOrganizeList = new ArrayList<String>();
		List<String> likeOrganizeList = new ArrayList<String>();
		Map<String, Object> departInfo = binolbsemp04Service.getDepartInfo(map);
		if(departInfo != null){
			Map<String, Object> thisDepartMap = new HashMap<String, Object>();
			thisDepartMap.putAll(map);
			thisDepartMap.put("organizationId", map.get("departId"));
			thisDepartMap.put("departType", departInfo.get("type"));
			thisDepartMap.put("manageType", "2");
			departList.add(thisDepartMap);
		}
		// 取得员工管辖部门信息
		String followDepart = (String)map.get("followDepart");
		if(followDepart != null && !"".equals(followDepart)) {
			List<Map<String, Object>> orgList = (List<Map<String, Object>>) JSONUtil
					.deserialize(followDepart);
			for(int i = 0; i < orgList.size(); i++) {
				Map<String, Object> orgMap = orgList.get(i);
				orgMap.putAll(map);
				orgMap.put("manageType", "1");
				departList.add(orgMap);
				followDepartList.add(orgMap);
				String departType = (String)orgMap.get("departType");
				String organizationId = (String)orgMap.get("organizationId");
				if(departType != null && "4".equals(departType)) {
					followCounterList.add(organizationId);
				}
				// 管辖部门ID
				followOrganizeList.add(organizationId);
			}
			if(followDepartList != null && !followDepartList.isEmpty()) {
				map.put("organizationIdList", followDepartList);
				binolbsemp04Service.delEmployeeDepart(map);
				if(departInfo != null){
					List<Map<String, Object>> moveCounterList = binolbsemp04Service.getMoveCounterList(map);
					if(moveCounterList != null && !moveCounterList.isEmpty()) {
						for(int i = 0; i < moveCounterList.size(); i++) {
							Map<String, Object> moveCounterMap = moveCounterList.get(i);
							moveCounterMap.put("newNodeId", binOLBSDEP04_Service.getNewNodeId(departInfo));
							binOLBSDEP03_Service.updateOrganizationNode(moveCounterMap);
						}
					}
				}
			}
			if(followCounterList != null && !followCounterList.isEmpty()) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("counterHead", String.valueOf(employeeId));
				paramMap.put("followDepartList", followCounterList);
				binOLBSCNT03_BL.updateBaSuperiors(paramMap);
			}
		}
		
		// 管辖的部门IDList（写Mongo用）
		map.put("followOrganizeList", followOrganizeList);
		
		// 取得员工关注部门信息
		String likeDepart = (String)map.get("likeDepart");
		if(likeDepart != null && !"".equals(likeDepart)) {
			List<Map<String, Object>> orgList = (List<Map<String, Object>>) JSONUtil
					.deserialize(likeDepart);
			for(int i = 0; i < orgList.size(); i++) {
				Map<String, Object> orgMap = orgList.get(i);
				orgMap.putAll(map);
				orgMap.put("manageType", "0");
				String organizationId = (String)orgMap.get("organizationId");
				departList.add(orgMap);
				// 关注的部门ID
				likeOrganizeList.add(organizationId);
			}
		}
		// 关注的部门IDList(写MONGO用)
		map.put("likeOrganizeList", likeOrganizeList);
		// 插入员工管辖部门对应表
		binolbsemp04Service.insertEmployeeDepart(departList);
		
		List<String> likeEmpList = (List)map.get("likeEmployeeId");
		List<Map<String, Object>> _likeEmpList = new ArrayList<Map<String,Object>>();
		if(likeEmpList != null && !likeEmpList.isEmpty()) {
			for(int i = 0; i < likeEmpList.size(); i++) {
				Map<String, Object> likeEmpMap = new HashMap<String, Object>();
				likeEmpMap.putAll(map);
				likeEmpMap.put("likeEmployeeId", likeEmpList.get(i));
				_likeEmpList.add(likeEmpMap);
			}
			// 插入关注员工表
			binolbsemp04Service.insertLikeEmployee(_likeEmpList);
		}
		
		// 取得登录帐号
		String longinName = (String)map.get("longinName");
		// 登录帐号存在的场合，生成系统帐号处理
		if(longinName != null && !"".equals(longinName)) {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.putAll(map);
			userMap.put("longinName", longinName);
			userMap.put("longinPhone", map.get("mobilePhone"));
			userMap.put("longinEmail", map.get("email"));
			String password = (String)map.get("password");
			// 加密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			userMap.put("passWord", des.encrypt(password));
			
		    //设置【密码更改通知日期】【密码失效日】
	        binOLPLUPM04_BL.setPwdExpireDate(userMap);
			
			binolbsemp04Service.insertUser(userMap);
			userMap.put("dataBaseName", CustomerContextHolder.getCustomerDataSourceType());
			
			String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
			String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
			// 对BI用户进行操作的场合
			if(this.isCreateBIUser(orgId, brandId)) {
				// BI用户名
				String biLoginName = longinName;
				// BI用户组名
				String biGroupName = null;
				// 从CodeTable中取得用户名的前缀和用户组名
				Map<String, Object> codeMap = new HashMap<String, Object>();
				String brandInfoId = (String)map.get("brandInfoId");
				if (CherryConstants.BRAND_INFO_ID_VALUE == Integer.parseInt(brandInfoId)) {
					codeMap = code.getCode("1179", brandInfoId);
				} else {
					String brandCode = binolbsemp04Service.getBrandCode(map);
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
				// BI账号区分
				String biFlag = (String)map.get("biFlag");
				// 有对应的BI账号的场合
				if(biFlag != null && "1".equals(biFlag)) {
					// 创建BI用户
					binolbsemp04Service.createBIUser(biLoginName, password, biGroupName,userInfo.getBrandCode());
				} else {
					// 删除BI用户
					binOLBSEMP05_Service.dropBIUser(biLoginName,userInfo.getBrandCode());
				}
			}
			
			// 登录帐号到配置数据库
			binolbsemp04Service.insertUserConf(userMap);
		}
		
		//如果添加的员工是营业员，还要处理营业员信息，并发送MQ到终端
		Map<String,Object> valiMap = new HashMap<String,Object>();
		//语言
		valiMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		//岗位ID
		valiMap.put("positionCategoryId", map.get("positionCategoryId"));
		//取得岗位信息
		Map<String,Object> positionInfo = binolbsemp04Service.getPositionCategoryInfo(valiMap);
		if(null != positionInfo && !positionInfo.isEmpty()){
			String categoryCode = ConvertUtil.getString(positionInfo.get("categoryCode"));
			//是否开启维护BAS同时发送MQ消息配置
			boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
			if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)||(CherryConstants.CATRGORY_CODE_BAS.equals(categoryCode) && sendBasMQ)){
				
				//如果添加的BA同时维护营业员信息表
				if(CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)){
					//插入营业员信息表
					map.put("employeeId", employeeId);
					map.put("bacode", map.get("employeeCode"));
					map.put("baname", map.get("employeeName"));
					map.put("baNameForeign", map.get("employeeNameForeign"));
					//数据来源	0:后台添加；1:终端上传
					map.put("originFlag", "0");
					binolbsemp04Service.insertBaInfo(map);
				}
				
				//发送MQ消息
				//BA有效性	0 有效；1 无效
				map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
				map.put("BasFlag", CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)?"":"true");
				//如果是BA则取得关注和归属柜台，如果是BAS取得管辖和归属柜台
				List<Map<String,Object>> countersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
				if(countersList != null && !countersList.isEmpty()){
					for(Map<String,Object> counterInfo : countersList){
						
						// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
						//绑定关系	-1 解除；1新增；0绑定关系维持不变
						//counterInfo.put("Flag", CherryConstants.BA_COUNTERS_ADD); // 据MQ接口定义中BAS考勤信息，会员信息，BA信息文档在20121009的定义(定义为"20121009以前定的是按差分来发，现在只发当前有效的绑定关系，该字段去除")
						// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
						
						counterInfo.put("EmployeeCode", map.get("employeeCode"));
					}
				}
				Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(map, countersList,CherryConstants.CATRGORY_CODE_BA.equals(categoryCode)?"BA":"BAS");
				if(MQMap.isEmpty()) return;
				//设定MQInfoDTO
				MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
				//调用共通发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
				
			}
		}
		
		/**
		 * 将涉及到的用户关注管辖部门与人员的具体内容写入Mongo
		 * 问题票：NEWWITPOS-2053
		 */
		this.insertMongoDepartPrivilegeLog(userInfo, map);
		
	}
	
	/**
	 * 将涉及到的用户关注管辖部门与人员的具体内容写入Mongo
	 * @param map
	 * @throws Exception
	 */
	private void insertMongoDepartPrivilegeLog(UserInfo userInfo, Map<String, Object> map) throws Exception {
		DBObject dbObject= new BasicDBObject();
		// 组织Code
		dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
		// 品牌Code
		dbObject.put("BrandCode", userInfo.getBrandCode());
		// 操作发生的日期
		dbObject.put("OperateDate", ConvertUtil.getString(map.get("systemDate")));
		// 操作发生的时间
		dbObject.put("OperateTime", ConvertUtil.getString(map.get("systemTime")));
		// 操作者的员工ID
		dbObject.put("OperateEmployeeID", ConvertUtil.getString(userInfo.getBIN_EmployeeID()));
		// 操作途径(depart:柜台 employee：人员)
		dbObject.put("OperateChannel", "employee");
		// 员工ID
		dbObject.put("MainID", ConvertUtil.getString(map.get("employeeId")));
		// 上司、关注人员、管辖部门、关注部门信息
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Superior", ConvertUtil.getString(map.get("higherEmployeeId")));
		// 关注人员ID
		jsonMap.put("FollowEmployee", (null == map.get("likeEmployeeId")) ? (new ArrayList<String>()) : map.get("likeEmployeeId"));
		// 管辖部门ID
		jsonMap.put("ControlDepart", map.get("followOrganizeList"));
		// 关注部门ID
		jsonMap.put("FollowDepart", map.get("likeOrganizeList"));
		// 涉及到的用户关注管辖部门的具体内容
		dbObject.put("JsonString", JSONUtil.serialize(jsonMap));
		
		// 写入Mongo
		MongoDB.insert(MGO_DEPART_PRIVILEGE_LOG_NAME, dbObject);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		
		// 取得岗位类别信息List
		return binolbsemp04Service.getPositionCategoryList(map);
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别信息
	 * 
	 * */
	public Map<String,Object> getPositionCategoryInfo(Map<String,Object> map){
		return binolbsemp04Service.getPositionCategoryInfo(map);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getOrgList(Map<String, Object> map) {
		
		// 取得部门List
		return binolbsemp04Service.getOrgList(map);
	}
	
	/**
	 * 验证用户代码是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public String getEmployeeId(Map<String, Object> map) {
		return binolbsemp04Service.getEmployeeId(map);
	}
	
	/**
	 * 验证登录帐号是否唯一
	 * 
	 * @param 查询条件
	 * @return 用户ID
	 */
	public String getUserIdByLgName(Map<String, Object> map) {
		return binolbsemp04Service.getUserIdByLgName(map);
	}
	
	/**
	 * 验证手机是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public List<String> getEmployeeIdByMobile(Map<String, Object> map) {
		return binolbsemp04Service.getEmployeeIdByMobile(map);
	}
	
	/**
	 * 验证邮箱是否唯一
	 * 
	 * @param 查询条件
	 * @return 员工ID
	 */
	public List<String> getEmployeeIdByEmail(Map<String, Object> map) {
		return binolbsemp04Service.getEmployeeIdByEmail(map);
	}
	
	/**
	 * 取得密码安全配置信息
	 * 
	 * @param 查询条件
	 * @return 密码安全配置信息
	 */
	public Map getPassWordConfig(Map<String, Object> map) {
		return binolbsemp04Service.getPassWordConfig(map);
	}
	
	/**
	 * 取得用户密码
	 * 
	 * @param map 查询条件
	 * @return 用户密码
	 */
	public String getUserPassWord(Map<String, Object> map) throws Exception {
		
		// 取得用户密码
		String password = binolbsemp04Service.getUserPassWord(map);
		// 解密处理
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		password =  des.decrypt(password);
		return password;
	}
	
	/**
	 * 验证登录帐号是否唯一
	 * 
	 * @param 查询条件
	 * @return 用户账号
	 */
	public String getLoginConfigByLgName(Map<String, Object> map) {
		return binolbsemp04Service.getLoginConfigByLgName(map);
	}
	
	/**
	 * 是否对BI用户进行操作
	 * 
	 * @param orgId 组织ID
	 * @param brandId 品牌ID
	 * @return true：是，false：否
	 */
	public boolean isCreateBIUser(String orgId, String brandId) {
		return binOLCM14_BL.isConfigOpen("1039", orgId, brandId);
	}
	
	/**
	 * 添加柜台主管部门处理
	 * 
	 * @param map
	 * @return 柜台主管部门ID
	 */
	public int creatOrgHandle(Map<String, Object> map) {
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		orgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 作成者
		orgMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 更新者
		orgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 作成模块
		orgMap.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
		// 更新模块
		orgMap.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
		// 上级员工节点
		String higher = (String)map.get("higher");
		// 上级员工所属部门节点
		String orgPath = "";
		if(higher != null && !"".equals(higher)) {
			// 根据上级员工节点取得上级员工所属部门节点
			orgPath = binolbsemp04Service.getOrgPathByEmpPath(map);
		}
		if(orgPath == null || "".equals(orgPath)) {
			// 取得品牌下的未知节点
			orgPath = binOLBSCNT04_Service.getUnknownPath(orgMap);
			if(orgPath == null || "".equals(orgPath)) {
				// 在品牌下添加一个未知节点来作为没有上级部门的柜台的上级节点
				Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
				// 组织ID
				unknownOrgMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
				// 品牌ID
				unknownOrgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				// 作成者
				unknownOrgMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
				// 更新者
				unknownOrgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
				// 作成模块
				unknownOrgMap.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
				// 更新模块
				unknownOrgMap.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
				// 未知节点添加在品牌节点下
				unknownOrgMap.put("path", binOLBSCNT04_Service.getFirstPath(orgMap));
				// 取得未知节点path
				String unknownPath = binOLBSDEP04_Service.getNewNodeId(unknownOrgMap);
				unknownOrgMap.put("newNodeId", unknownPath);
				// 未知节点的部门代码
				unknownOrgMap.put("departCode", CherryConstants.UNKNOWN_DEPARTCODE);
				// 未知节点的部门名称
				unknownOrgMap.put("departName", CherryConstants.UNKNOWN_DEPARTNAME);
				// 未知节点的部门类型
				unknownOrgMap.put("type", CherryConstants.UNKNOWN_DEPARTTYPE);
				// 未知节点的到期日expiringDate
				unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
				// 添加未知节点
				binOLBSDEP04_Service.addOrganization(unknownOrgMap);
				orgPath = unknownPath;
			}
		}
		orgMap.put("path", orgPath);
		String newNodeId = binOLBSDEP04_Service.getNewNodeId(orgMap);
		orgMap.put("newNodeId", newNodeId);
		
		Map codeMap = code.getCode("1120","0");
		orgMap.put("type", "0");
		orgMap.put("length", codeMap.get("value2"));
		// 自动生成部门代码
		String departCode = (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(orgMap);
		orgMap.put("departCode", departCode);
		orgMap.put("departName", map.get("employeeName"));
		orgMap.put("type", "6");
		orgMap.put("testType", map.get("creatOrgFlag"));
		// 添加柜台主管部门
		return binOLBSDEP04_Service.addOrganization(orgMap);
	}
	

	
	/**
	 * 验证身份证是否唯一
	 * @param map
	 * @return
	 */
	public  List<String> validateIdentityCard(Map<String, Object> map){
		return binolbsemp04Service.validateIdentityCard(map);
	}
	
}
