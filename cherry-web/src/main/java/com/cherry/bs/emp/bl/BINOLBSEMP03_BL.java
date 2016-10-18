/*		
 * @(#)BINOLBSEMP03_BL.java     1.0 2010/12/30		
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.cnt.bl.BINOLBSCNT03_BL;
import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.dep.service.BINOLBSDEP03_Service;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP02_Service;
import com.cherry.bs.emp.service.BINOLBSEMP03_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP05_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.dto.UdiskBindDetailDTO;
import com.cherry.cm.activemq.dto.UdiskBindMainDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
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
 * 员工编辑 BL
 * 
 * @author lipc
 * @version 1.0 2010.12.30
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP03_BL {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP03_BL.class);

	@Resource(name="binOLBSEMP03_Service")
	private BINOLBSEMP03_Service binolbsemp03Service;
	
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	@Resource(name="binOLBSEMP02_Service")
	private BINOLBSEMP02_Service binolbsemp02Service;
	
	/** 停用启用员工Service */
	@Resource(name="binOLBSEMP05_Service")
	private BINOLBSEMP05_Service binOLBSEMP05_Service;
	
	/** 添加部门画面Service */
	@Resource(name="binOLBSDEP04_Service")
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 更新部门画面Service */
	@Resource(name="binOLBSDEP03_Service")
	private BINOLBSDEP03_Service binOLBSDEP03_Service;
	
	/** 创建柜台画面Service */
	@Resource(name="binOLBSCNT04_Service")
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLBSEMP04_BL")
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	@Resource(name="binOLPLUPM04_BL")
	private BINOLPLUPM04_BL binOLPLUPM04_BL;
	
	/** 更新柜台画面BL */
	@Resource(name="binOLBSCNT03_BL")
	private BINOLBSCNT03_BL binOLBSCNT03_BL;
	
	/** MongoDB 用户关注管辖部门履历表 **/
	public static final String MGO_DEPART_PRIVILEGE_LOG_NAME = "MGO_DepartPrivilegeLog";
	
	/**
	 * 员工ID
	 * 
	 * @param map
	 * @return int
	 */
	public String getEmployeeId(Map<String, Object> map) {
		return binolbsemp03Service.getEmployeeId(map);
	}

	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map) throws Exception {

		// 取得员工信息
		Map<String, Object> employeeInfo = binolbsemp02Service.getEmployeeInfo(map);
		if(employeeInfo != null && !employeeInfo.isEmpty()) {
			if(employeeInfo.get("higher") != null && !CherryConstants.DUMMY_VALUE.equals(employeeInfo.get("higher"))) {
				map.put("higher", employeeInfo.get("higher"));
				// 取得直属上级信息
				Map<String, Object> supervisor = binolbsemp02Service.getSupervisor(map);
				if(supervisor != null && !supervisor.isEmpty()) {
					employeeInfo.put("higherName", supervisor.get("employeeName"));
					// 上司员工信息
					employeeInfo.put("higherEmployeeId", supervisor.get("BIN_EmployeeID"));
				}
			} else {
				employeeInfo.remove("higher");
			}
			// 解密员工的电话、邮箱、身份证等加密信息
			decryptData(employeeInfo);
		}
		return employeeInfo;
	}
	
	/**
	 * 取得员工地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpAddressList (Map<String, Object> map) {
		
		return binolbsemp03Service.getEmpAddressList(map);
	}
	
	/**
	 * 取得员工入离职List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpQuitList (Map<String, Object> map) {
		
		return binolbsemp03Service.getEmpQuitList(map);
	}
	
	/**
	 * 取得员工部门、岗位信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPostDistList (Map<String, Object> map) {
		
		return binolbsemp03Service.getPostDistList(map);
	}
	
	/**
	 * 员工信息更新
	 * 
	 * @param map
	 * @return
	 */
	public void tran_updateEmployee(UserInfo userInfo, Map<String, Object> map)
			throws CherryException, Exception {
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 组织代号
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		map.put("loginName", userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 创建者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP03");
		// 创建模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSEMP03");
		// 数据库系统时间
		String sysDate = binolbsemp03Service.getSYSDate();
		// 数据库系统时间(YYYY-MM-DD HH：mm：SS)
		map.put("systemTime", sysDate.substring(0, 19));
		// 数据库系统日期(YYYY-MM-DD)
		map.put("systemDate", sysDate.substring(0, 10));
		// 创建时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 【加密】画面上的电话、邮箱、身份证信息
		this.encryptData(map);
		
		String creatOrgFlag = (String)map.get("creatOrgFlag");
		// 需要自动创建柜台主管部门的场合，添加柜台主管部门处理
		if(creatOrgFlag != null) {
			int departId = binolbsemp04BL.creatOrgHandle(map);
			map.put("departId", departId);
		}
		
		//编辑的信息是否与BA有关
		boolean editBaFlag = false;
		//编辑的信息是否与BAS有关
		boolean editBasFlag = false;
		//编辑前后岗位类别变化区分(美容顾问)
		String positionFlag = "-2";
		//编辑前后岗位类别变化区分(柜台主管)
		String positionFlag1 = "-2";
		
		boolean employeeValidFlag = binOLBSCOM01_BL.isEmployeeValidFlag(ConvertUtil.getInt(map.get("employeeId")));
		
		//存放美容顾问与柜台之间的关系 
		//List<Map<String,Object>> oldCountersList = null; // 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623)
		//存放柜台主管与柜台之间的关系 
		//List<Map<String,Object>> oldCountersList1 = null; // 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623)
		//如果编辑的员工是营业员，还要处理营业员信息
		Map<String,Object> valiMap = new HashMap<String,Object>();
		//语言
		valiMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		//编辑前的岗位ID
		valiMap.put("positionCategoryId", map.get("oldPositionCategoryId"));
		//取得编辑前的岗位信息
		Map<String,Object> oldPositionInfo = binolbsemp04Service.getPositionCategoryInfo(valiMap);
		//编辑前的岗位类别编码
		String oldCategoryCode = ConvertUtil.getString(oldPositionInfo.get("categoryCode"));
		//编辑后的岗位ID
		valiMap.put("positionCategoryId", map.get("positionCategoryId"));
		//取得编辑后的岗位信息
		Map<String,Object> positionInfo = binolbsemp04Service.getPositionCategoryInfo(valiMap);
		//编辑后的岗位类别编码
		String newCategoryCode = ConvertUtil.getString(positionInfo.get("categoryCode"));
		
		//编辑前后岗位类别不变并且都是美容顾问
		if(CherryConstants.CATRGORY_CODE_BA.equals(newCategoryCode) && oldCategoryCode.equals(newCategoryCode)){
			editBaFlag = true;
			positionFlag = "0";
			//取得编辑前的BA与柜台的绑定信息
			//oldCountersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
		}//编辑前岗位类不是美容顾问，编辑后岗位类别为美容顾问
		else if(!CherryConstants.CATRGORY_CODE_BA.equals(oldCategoryCode) && CherryConstants.CATRGORY_CODE_BA.equals(newCategoryCode)){
			editBaFlag = true;
			positionFlag = "1";
			
		}//编辑前岗位类别为美容顾问，编辑后岗位类别不是美容顾问
		else if(CherryConstants.CATRGORY_CODE_BA.equals(oldCategoryCode) && !CherryConstants.CATRGORY_CODE_BA.equals(newCategoryCode)){
			editBaFlag = true;
			positionFlag = "-1";
			//取得编辑前的BA与柜台的绑定信息
			//oldCountersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
			
		}
		
		//是否开启维护BAS同时发送MQ消息配置
		boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		Map<String,Object> basMap = null;
		if(sendBasMQ){
			basMap = new HashMap<String,Object>(map);
			basMap.put("BasFlag", "true");
			//编辑前后岗位类别不变并且都是柜台主管
			if(CherryConstants.CATRGORY_CODE_BAS.equals(newCategoryCode) && oldCategoryCode.equals(newCategoryCode)){
				editBasFlag = true;
				positionFlag1 = "0";
				//取得编辑前的BA与柜台的绑定信息
				//oldCountersList1 = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(basMap);
			}//编辑前岗位类不是柜台主管，编辑后岗位类别为柜台主管
			else if(!CherryConstants.CATRGORY_CODE_BAS.equals(oldCategoryCode) && CherryConstants.CATRGORY_CODE_BAS.equals(newCategoryCode)){
				editBasFlag = true;
				positionFlag1 = "1";
				
			}//编辑前岗位类别为柜台主管，编辑后岗位类别不是柜台主管
			else if(CherryConstants.CATRGORY_CODE_BAS.equals(oldCategoryCode) && !CherryConstants.CATRGORY_CODE_BAS.equals(newCategoryCode)){
				editBasFlag = true;
				positionFlag1 = "-1";
				//取得编辑前的BA与柜台的绑定信息
				//oldCountersList1 = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(basMap);
			}
		}
		
		//根据编辑的员工有效性状态，更新BA信息表中的BA信息状态
		
		// 更新员工信息表
		int count = binolbsemp03Service.updEmpInfo(map);
		if(count == 0){
			throw new CherryException("ECM00038");
		}
		
		// 员工地址信息
		String addressInfo = (String) map.get("addressInfo");
		if (!CherryChecker.isNullOrEmpty(addressInfo, true)) {
			// 员工地址List
			List<Map<String, Object>> addrList = (List<Map<String, Object>>) JSONUtil
					.deserialize(addressInfo);
			if(null != addrList){
				for (Map<String, Object> addr : addrList) {
					// 添加,更新,删除操作(0:删除,1:更新,其它:添加)
					String option = (String)addr.get("option");
					addr.putAll(map);
					// 操作类型为删除，或者操作类型为更新但地址为空
					if ("0".equals(option)|| ("1".equals(option) 
							&& null == addr.get("address"))) { // 删除操作
						// 删除员工地址信息
						binolbsemp03Service.delEmpAddr(addr);
						// 删除地址信息
						binolbsemp03Service.delAddrInfo(addr);
					}else if("1".equals(option)){						// 更新操作
						// 更新员工地址信息
						binolbsemp03Service.updEmpAddr(addr);
						// 更新地址信息
						binolbsemp03Service.updAddrInfo(addr);
					}else{												// 添加操作
						if(null != addr.get("address")){
							// 插入地址信息表，返回地址信息ID
							int addressInfoId = binolbsemp03Service
									.insertAddrInfo(addr);
							addr.put("addressInfoId", addressInfoId);
							// 插入员工地址信息表
							binolbsemp03Service.insertEmpAddress(addr);
						}
					}
				}
			}
		}
		// 员工入退职信息
		String quitInfo = (String) map.get("quitInfo");
		if (!CherryChecker.isNullOrEmpty(quitInfo, true)) {
			// 员工入退职信息List
			List<Map<String, Object>> quitList = (List<Map<String, Object>>) JSONUtil
					.deserialize(quitInfo);
			if(null != quitList){
				for (Map<String, Object> quit : quitList) {
					if(quit == null || quit.isEmpty()) {
						continue;
					}
					// 添加,更新,删除操作
					String option = (String)quit.get("option");
					quit.putAll(map);
					if("0".equals(option)||("1".equals(option)
							&& null == quit.get("commtDate")
							&& null == quit.get("depDate")
							&& null == quit.get("depReason"))){			// 删除操作
						// 删除员工入退职信息
						binolbsemp03Service.delQuitInfo(quit);
					}else if("1".equals(option)){						// 更新操作
						// 更新员工入退职信息
						binolbsemp03Service.updQuitInfo(quit);
					}else{												// 添加操作
						// 插入员工入退职信息
						binolbsemp03Service.insertEmpQuit(quit);
					}
				}
			}
		}
		// 员工上司是否变化
		boolean isMove = false;
		if(map.get("higher") != null) {
			if(map.get("oldHigher") != null && map.get("oldHigher").equals(map.get("higher"))) {
				isMove = false;
			} else {
				isMove = true;
			}
		} else {
			if(map.get("oldHigher") != null) {
				isMove = true;
			}
		}
		// 员工上司变化的场合，移动该员工到新上司下
		if(isMove) {
			if(map.get("higher") != null && !"".equals(map.get("higher"))) {
				map.put("path", map.get("higher"));
			} else {
				map.put("path", CherryConstants.DUMMY_VALUE);
			}
			// 取得新节点
			String newEmpNodeId = binolbsemp04Service.getNewEmpNodeId(map);
			map.put("newNodeId", newEmpNodeId);
			binolbsemp03Service.updateEmpNode(map);
		}
		
		// 删除员工现有的管辖部门信息
		binolbsemp03Service.delEmployeeDepart(map);
		// 员工管辖部门对应List
		List<Map<String, Object>> departList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> followDepartList = new ArrayList<Map<String,Object>>();
		List<String> followLikeDepartList = new ArrayList<String>();
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
		
		// 用户管辖部门变更日志 start
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ ConvertUtil.getString(map.get("employeeId")) +"用户管辖部门变更start  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("原管辖部门： " + ConvertUtil.getString(map.get("oldfollowDepart")));
		logger.info("员工管辖部门：  " + ConvertUtil.getString(map.get("followDepart")));
		
		
		logger.info("参数(map): " + ConvertUtil.getString(map));
		logger.info("更新人(updatedBy): " + ConvertUtil.getString(map.get(CherryConstants.UPDATEDBY)));
		logger.info("更新程序(updatePGM): " + ConvertUtil.getString(map.get(CherryConstants.UPDATEPGM)));
		logger.info("更新时间(updateTime): " + ConvertUtil.getString(map.get("systemTime")));
		
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ ConvertUtil.getString(map.get("employeeId")) +"用户管辖部门变更end  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		
		// 取得员工管辖部门信息
		String followDepart = (String)map.get("followDepart");
		// 取得原管辖柜台信息
		List<String> oldfollowDepart = (List)map.get("oldfollowDepart");
		if(followDepart != null && !"".equals(followDepart)) {
			List<Map<String, Object>> orgList = (List<Map<String, Object>>) JSONUtil
					.deserialize(followDepart);
			for(int i = 0; i < orgList.size(); i++) {
				Map<String, Object> orgMap = orgList.get(i);
				orgMap.putAll(map);
				orgMap.put("manageType", "1");
				departList.add(orgMap);
				followDepartList.add(orgMap);
				String organizationId = (String)orgMap.get("organizationId");
				if(oldfollowDepart != null && !oldfollowDepart.isEmpty()) {
					oldfollowDepart.remove(organizationId);
				}
				String departType = (String)orgMap.get("departType");
				if(departType != null && "4".equals(departType)) {
					followLikeDepartList.add(organizationId);
				}
				// 管辖的部门ID
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
			if(followLikeDepartList != null && !followLikeDepartList.isEmpty()) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("counterHead", String.valueOf(map.get("employeeId")));
				paramMap.put("followDepartList", followLikeDepartList);
				binOLBSCNT03_BL.updateBaSuperiors(paramMap);
			}
		}
		
		// 管辖的部门IDList（写MONGO用）
		map.put("followOrganizeList", followOrganizeList);
		
		if(oldfollowDepart != null && !oldfollowDepart.isEmpty()) {
			map.put("oldfollowDepart", oldfollowDepart);
			List<Map<String, Object>> oldfollowCouList = binolbsemp04Service.getOldfollowCouList(map);
			if(oldfollowCouList != null && !oldfollowCouList.isEmpty()) {
				Map<String, Object> unHigherMap = new HashMap<String, Object>();
				// 取得品牌下的未知节点
				String path = binOLBSCNT04_Service.getUnknownPath(map);
				// 未知节点不为空的场合，该节点作为柜台的上级节点
				if(path != null && !"".equals(path)) {
					unHigherMap.put("path", path);
				} else {
					// 在品牌下添加一个未知节点来作为没有上级部门的柜台的上级节点
					Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
					// 所属组织
					unknownOrgMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
					// 所属品牌
					unknownOrgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					// 作成者
					unknownOrgMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
					// 作成程序名
					unknownOrgMap.put(CherryConstants.CREATEPGM, "BINOLBSEMP03");
					// 更新者
					unknownOrgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
					// 更新程序名
					unknownOrgMap.put(CherryConstants.UPDATEPGM, "BINOLBSEMP03");
					// 未知节点添加在品牌节点下
					unknownOrgMap.put("path", binOLBSCNT04_Service.getFirstPath(map));
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
					unHigherMap.put("path", unknownPath);
				}
				for(int i = 0; i < oldfollowCouList.size(); i++) {
					Map<String, Object> moveCounterMap = oldfollowCouList.get(i);
					moveCounterMap.put("newNodeId", binOLBSDEP04_Service.getNewNodeId(unHigherMap));
					binOLBSDEP03_Service.updateOrganizationNode(moveCounterMap);
				}
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("followDepartList", oldfollowDepart);
			binOLBSCNT03_BL.updateBaSuperiors(paramMap);
		}
		
		// 取得员工关注部门信息
		String likeDepart = (String)map.get("likeDepart");
		if(likeDepart != null && !"".equals(likeDepart)) {
			List<Map<String, Object>> orgList = (List<Map<String, Object>>) JSONUtil
					.deserialize(likeDepart);
			for(int i = 0; i < orgList.size(); i++) {
				Map<String, Object> orgMap = orgList.get(i);
				orgMap.putAll(map);
				orgMap.put("manageType", "0");
				departList.add(orgMap);
				String organizationId = (String)orgMap.get("organizationId");
				String departType = (String)orgMap.get("departType");
				if(departType != null && "4".equals(departType)) {
					if(!followLikeDepartList.contains(organizationId)) {
						followLikeDepartList.add(organizationId);
					}
				}
				// 关注的部门ID
				likeOrganizeList.add(organizationId);
			}
		}
		// 关注的部门IDList(写MONGO用)
		map.put("likeOrganizeList", likeOrganizeList);
		// 插入员工管辖部门对应表
		binolbsemp04Service.insertEmployeeDepart(departList);
		
		// 删除员工现有的关注用户信息
		binolbsemp03Service.delLikeEmployee(map);
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
		
		String organizationInfoId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
		String brandInfoId = (String)map.get("brandInfoId");
		// 开启U盘绑定功能而且不是总部用户的场合
		if(binOLCM14_BL.isConfigOpen("1030", organizationInfoId, brandInfoId) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId)) {
			// 根据员工ID取得U盘序列号
			String udiskSN = binolbsemp03Service.getUdiskSN(map);
			if(udiskSN != null && !"".equals(udiskSN)) {
				// 取得原管辖和关注柜台
				List<String> oldfollowLikeDepart = (List)map.get("oldfollowLikeDepart");
				if(oldfollowLikeDepart != null && !oldfollowLikeDepart.isEmpty()) {
					HashSet set = new HashSet(oldfollowLikeDepart);
					oldfollowLikeDepart.clear();
					oldfollowLikeDepart.addAll(set);
				}
				// 拆分原管辖、关注的柜台和新管辖、关注的柜台
				if(followLikeDepartList != null && !followLikeDepartList.isEmpty()) {
					for(int i = 0; i < followLikeDepartList.size(); i++) {
						if(oldfollowLikeDepart == null || oldfollowLikeDepart.isEmpty()) {
							break;
						}
						for(int j = 0; j < oldfollowLikeDepart.size(); j++) {
							if(followLikeDepartList.get(i).equals(oldfollowLikeDepart.get(j))) {
								oldfollowLikeDepart.remove(j);
								followLikeDepartList.remove(i);
								i--;
								break;
							}
						}
					}
				}
				List<UdiskBindDetailDTO> udiskBindDetailDTOList = new ArrayList<UdiskBindDetailDTO>();
				// 存在新管辖、关注的柜台，需要U盘绑定
				if(followLikeDepartList != null && !followLikeDepartList.isEmpty()) {
					Map<String, Object> departMap = new HashMap<String, Object>();
					departMap.put("departList", followLikeDepartList);
					// 根据部门ID取得柜台号List
					List<String> countercodeList = binolbsemp03Service.getCountercodeList(departMap);
					if(countercodeList != null && !countercodeList.isEmpty()) {
						UdiskBindDetailDTO udiskBindDetailDTO = new UdiskBindDetailDTO();
						udiskBindDetailDTO.setUdiskSN(udiskSN);
						udiskBindDetailDTO.setFlag("1");
						udiskBindDetailDTO.setCountercode(countercodeList);
						udiskBindDetailDTOList.add(udiskBindDetailDTO);
					}
				}
				// 存在原管辖、关注的柜台，需要解除U盘绑定
				if(oldfollowLikeDepart != null && !oldfollowLikeDepart.isEmpty()) {
					Map<String, Object> departMap = new HashMap<String, Object>();
					departMap.put("departList", oldfollowLikeDepart);
					// 根据部门ID取得柜台号List
					List<String> countercodeList = binolbsemp03Service.getCountercodeList(departMap);
					if(countercodeList != null && !countercodeList.isEmpty()) {
						UdiskBindDetailDTO udiskBindDetailDTO = new UdiskBindDetailDTO();
						udiskBindDetailDTO.setUdiskSN(udiskSN);
						udiskBindDetailDTO.setFlag("0");
						udiskBindDetailDTO.setCountercode(countercodeList);
						udiskBindDetailDTOList.add(udiskBindDetailDTO);
					}
				}
				// 存在需要U盘绑定或者解除的数据时，发送MQ消息处理
				if(udiskBindDetailDTOList != null && !udiskBindDetailDTOList.isEmpty()) {
					UdiskBindMainDTO udiskBindMainDTO = new UdiskBindMainDTO();
					udiskBindMainDTO.setUdiskBindDetailDTOList(udiskBindDetailDTOList);
					// 根据品牌ID取得品牌code
					String brandCode = binOLBSCNT04_Service.getBrandCode(map);
					udiskBindMainDTO.setBrandCode(brandCode);
					udiskBindMainDTO.setTradeType("UC");
					udiskBindMainDTO.setTradeNoIF(binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, userInfo.getLoginName(), "UC"));
					// MQ消息 DTO
					MQInfoDTO mqInfoDTO = new MQInfoDTO();
					// 单据号
					mqInfoDTO.setBillCode(udiskBindMainDTO.getTradeNoIF());
					// 单据类型
					mqInfoDTO.setBillType(udiskBindMainDTO.getTradeType());
					// 所属品牌
					mqInfoDTO.setBrandInfoId(Integer.parseInt(brandInfoId));
					// 所属组织
					mqInfoDTO.setOrganizationInfoId(Integer.parseInt(organizationInfoId));
					// 消息体
					mqInfoDTO.setData(udiskBindMainDTO.getMQMsg());
					// 创建者
					String createdby = map.get(CherryConstants.CREATEDBY).toString();
					mqInfoDTO.setCreatedBy(createdby);
					// 更新者
					String updatedby = map.get(CherryConstants.UPDATEDBY).toString();
					mqInfoDTO.setUpdatedBy(updatedby);
					// 创建模块
					String createpgm = map.get(CherryConstants.CREATEPGM).toString();
					mqInfoDTO.setCreatePGM(createpgm);
					// 更新模块
					String updatepgm = map.get(CherryConstants.UPDATEPGM).toString();
					mqInfoDTO.setUpdatePGM(updatepgm);
					// 业务流水
					DBObject dbObject = new BasicDBObject();
					// 组织代号
					dbObject.put("OrgCode", userInfo.getOrganizationInfoCode());
					// 品牌代码，即品牌简称
					dbObject.put("BrandCode", brandCode);
					// 业务类型
					dbObject.put("TradeType", udiskBindMainDTO.getTradeType());
					// 单据号
					dbObject.put("TradeNoIF", udiskBindMainDTO.getTradeNoIF());
					// 修改次数
					dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
				    // 发生时间
				    dbObject.put("OccurTime", sysDate);
				    // 事件内容
				    dbObject.put("Content", mqInfoDTO.getData());
				    // 业务流水
				    mqInfoDTO.setDbObject(dbObject);
				    // 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
			}
		}
		
		String userId = (String)map.get("userId");
		if(userId != null && !"".equals(userId)) {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.putAll(map);
			userMap.put("longinName", map.get("longinName"));
			userMap.put("longinPhone", map.get("mobilePhone"));
			userMap.put("longinEmail", map.get("email"));
			String password = (String)map.get("password");
			if(password != null && !"".equals(password)) {
				// 加密处理
				DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
				userMap.put("passWord", des.encrypt(password));
				
		        //设置【密码更改通知日期】【密码失效日】
		        binOLPLUPM04_BL.setPwdExpireDate(userMap);
			}
			// 更新用户信息
			binolbsemp03Service.updateUser(userMap);			

			//↓↓↓↓↓↓↓↓↓↓WITPOSQA-19293 密码修改时是否清除微信绑定登录信息  BY Liyuan 2016/08/04 START↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			// 销售帮用户绑定信息是否可以清除
			String isClearFlag = binOLCM14_BL.getConfigValue("1369", String.valueOf(userInfo.getBIN_OrganizationInfoID()), brandInfoId);
						
			if("1".equals(isClearFlag) && !ConvertUtil.isBlank(ConvertUtil.getString(userMap.get("password")))) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.putAll(userMap);
				tempMap.put("validFlag", CherryConstants.VALIDFLAG_DISABLE);
				binolbsemp03Service.updateBangUserValidFlag(tempMap);
			}
			//↑↑↑↑↑↑↑↑↑↑WITPOSQA-19293 密码修改时是否清除微信绑定登录信息  BY Liyuan 2016/08/04 END↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
			
			String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
			String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
			// 对BI用户进行操作的场合
			if(binolbsemp04BL.isCreateBIUser(orgId, brandId)) {
				// BI用户名
				String biLoginName = (String)map.get("longinName");
				// BI用户组名
				String biGroupName = null;
				// 从CodeTable中取得用户名的前缀和用户组名
				Map<String, Object> codeMap = new HashMap<String, Object>();
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
				// 用户有效区分
				String validFlag = (String)map.get("validFlag");
				// 有对应的BI账号而且用户有效的场合
				if(biFlag != null && "1".equals(biFlag) && validFlag != null && "1".equals(validFlag)) {
					String oldPassword = binolbsemp04BL.getUserPassWord(map);
					// 更新BI用户
					binolbsemp04Service.updateBIUser(biLoginName, oldPassword, password, biGroupName,userInfo.getBrandCode());
				} else {
					// 删除BI用户
					binOLBSEMP05_Service.dropBIUser(biLoginName,userInfo.getBrandCode());
				}
			}
			
			// 更新用户信息(配置数据库)
			binolbsemp03Service.updateUserConf(userMap);
		} else {
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
				binolbsemp04Service.insertUser(userMap);
				userMap.put("dataBaseName", CustomerContextHolder.getCustomerDataSourceType());
				
				String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
				String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
				// 对BI用户进行操作的场合
				if(binolbsemp04BL.isCreateBIUser(orgId, brandId)) {
					// BI用户名
					String biLoginName = longinName;
					// BI用户组名
					String biGroupName = null;
					// 从CodeTable中取得用户名的前缀和用户组名
					Map<String, Object> codeMap = new HashMap<String, Object>();
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
		}
		
		//如果编辑的是营业员信息
		if(editBaFlag){
			
			//根据员工ID取得营业员ID
			int baInfoId = binolbsemp03Service.getBaInfoIdByEmployeeId(ConvertUtil.getInt(map.get("employeeId")));
			map.put("bacode", map.get("employeeCode"));
			map.put("baname", map.get("employeeName"));
			map.put("baNameForeign", map.get("employeeNameForeign"));
			//如果营业员信息表中不存在则插入，否则编辑
			if(baInfoId <= 0){
				//插入营业员信息表
				map.put("employeeId", map.get("employeeId"));
				//数据来源	0:后台添加；1:终端上传
				map.put("originFlag", "0");
				binolbsemp04Service.insertBaInfo(map);
			}else{
				map.put("baInfoId", baInfoId);
				binolbsemp04Service.updateBaInfo(map);
			}
			
			//根据编辑的员工有效性状态同步更新营业员信息表中的状态
			if("-1".equals(positionFlag) || "1".equals(positionFlag)){
				//根据员工ID取得营业员ID
				int baInfoId1 = binolbsemp03Service.getBaInfoIdByEmployeeId(ConvertUtil.getInt(map.get("employeeId")));
				if(baInfoId != 0){
					Map<String,Object> enableMap = new HashMap<String,Object>();
					enableMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
					enableMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
					Map<String,Object> enTempMap = new HashMap<String,Object>();
					enTempMap.put("baInfoId", baInfoId1);
					List<Map<String,Object>> baInfoIdList = new ArrayList<Map<String,Object>>();
					baInfoIdList.add(enTempMap);
					enableMap.put("baInfoIdList", baInfoIdList);
					//根据员工有效性更新BA信息表中的数据有效性
					if("1".equals(positionFlag)){
						enableMap.put("validFlag", employeeValidFlag? "1":"0");
					}else{
						enableMap.put("validFlag", "0");
					}
					//更新营业员信息表
					binOLBSEMP05_Service.updateBaInfo(enableMap);
				}
			}
			
			//如果编辑的是无效营业员，则不发送MQ消息
			if(employeeValidFlag){
				//发送MQ消息
				//查询出该员工关注和归属柜台
//				List<Map<String,Object>> countersList = this.getCountersList(map, oldCountersList, positionFlag);
				List<Map<String,Object>> countersList = this.getCountersList(map, positionFlag);
				//调用共通取得MQ数据
				Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(map, countersList,"BA");
				if(MQMap.isEmpty()) return;
				//设定MQInfoDTO
				MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
				//调用共通发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
			}
		}
		
		//如果编辑与柜台主管并且员工有效有关发送MQ消息
		if(editBasFlag && employeeValidFlag && sendBasMQ){
			//取得该柜台主管管辖和归属柜台
//			List<Map<String,Object>> countersList = this.getCountersList(basMap, oldCountersList1, positionFlag1);
			List<Map<String,Object>> countersList = this.getCountersList(basMap, positionFlag1);
			//调用共通取得MQ数据
			map.put("validFlag", basMap.get("validFlag"));
			Map<String,Object> MQMap = binOLBSCOM01_BL.getEmployeeMqMap(map, countersList,"BAS");
			if(MQMap.isEmpty()) return;
			//设定MQInfoDTO
			MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
			//调用共通发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
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
	 * 加密身份证号、电话、email数据信息
	 * @param map
	 * @throws Exception 
	 */
	private void encryptData(Map<String, Object> map) throws Exception {
		// 该 人员所属品牌Code【加密参数】
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
//		String brandCode = "-9999";
		if(!CherryChecker.isNullOrEmpty(map.get("identityCard"),true)){
			String identityCard = ConvertUtil.getString(map.get("identityCard"));
			map.put("identityCard", CherrySecret.encryptData(brandCode,identityCard));
		}
		if(!CherryChecker.isNullOrEmpty(map.get("phone"),true)){
			String phone = ConvertUtil.getString(map.get("phone"));
			map.put("phone", CherrySecret.encryptData(brandCode,phone));
		}
		if(!CherryChecker.isNullOrEmpty(map.get("mobilePhone"),true)){
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		if(!CherryChecker.isNullOrEmpty(map.get("email"),true)){
			String email = ConvertUtil.getString(map.get("email"));
			map.put("email", CherrySecret.encryptData(brandCode,email));
		}
	}
	
	/**
	 * 解密身份证号、电话、email数据信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void decryptData(Map<String, Object> map) throws Exception {
		//该人员所属品牌（品牌不可编辑为解密参数）
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
//		String brandCode = "-9999";
		// 对身份证、电话、email解密显示
		if (!CherryChecker.isNullOrEmpty(map.get("identityCard"), true)) {
			String identityCard = ConvertUtil
					.getString(map.get("identityCard"));
			map.put("identityCard",
					CherrySecret.decryptData(brandCode, identityCard));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("phone"), true)) {
			String phone = ConvertUtil.getString(map.get("phone"));
			map.put("phone", CherrySecret.decryptData(brandCode, phone));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone",
					CherrySecret.decryptData(brandCode, mobilePhone));
		}
		if (!CherryChecker.isNullOrEmpty(map.get("email"), true)) {
			String email = ConvertUtil.getString(map.get("email"));
			map.put("email", CherrySecret.decryptData(brandCode, email));
		}
	}

	/**
	 * 取得员工与柜台之间的关系
	 * 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623)
	 * 
	 * */
	private List<Map<String,Object>> getCountersList(Map<String,Object> map , String positionFlag){
		//存放返回结果
		List<Map<String,Object>> countersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
		if(countersList != null && !countersList.isEmpty()){
			
			for(Map<String,Object> counterInfo : countersList){
				
				counterInfo.put("EmployeeCode", map.get("employeeCode"));
			}
		}
		
		//编辑前后岗位类别不变并且都是美容顾问（柜台主管）
		if("0".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
		}
		//如果编辑前不是美容顾问（柜台主管），编辑后是美容顾问（柜台主管）
		else if("1".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
		}
		//如果编辑前是美容顾问（柜台主管），编辑后不是美容顾问（柜台主管）
		else if("-1".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_DISABLE);
		}
		
		return countersList;
	}
	
	
	/**
	 * 取得员工与柜台之间的关系(废除)
	 * 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623)
	 * 据MQ接口定义中BAS考勤信息，会员信息，BA信息文档在20121009的定义(定义为"20121009以前定的是按差分来发，现在只发当前有效的绑定关系，该字段去除")
	 * */
	@Deprecated
	private List<Map<String,Object>> getCountersList(Map<String,Object> map ,List<Map<String,Object>> oldCountersList, String positionFlag){
		//存放返回结果
		List<Map<String,Object>> countersList = new ArrayList<Map<String,Object>>();
		
		//编辑前后岗位类别不变并且都是美容顾问（柜台主管），MQ明细的绑定关系要明确指出哪些是新增，哪些是解除，哪些是保持不变
		if("0".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
			List<Map<String,Object>> newCountersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
			//遍历新的绑定关系，确定新增的柜台和维持不变的柜台
			for(int i =0 ; i <= newCountersList.size()-1 ; i++){
				Map<String,Object> newTemp = newCountersList.get(i);
				Map<String,Object> counterMap = new HashMap<String,Object>();
				String counterCode = (String)newTemp.get("CounterCode");
				boolean flag = false;
				label:
				for(int j = 0 ; j <= oldCountersList.size()-1 ; j++){
					Map<String,Object> oldTemp = oldCountersList.get(j);
					//如果新的绑定关系中有但是老的绑定关系没有，则为新增
					if(oldTemp.containsValue(counterCode)){
						flag = true;
						//绑定关系	-1 解除；1新增；0绑定关系维持不变
						counterMap.put("Flag", CherryConstants.BA_COUNTERS_KEEP);
						counterMap.put("EmployeeCode", map.get("employeeCode"));
						counterMap.put("CounterCode", counterCode);
						countersList.add(counterMap);
						
						oldCountersList.remove(j);
						newCountersList.remove(i);
						j--;
						i--;
						break label;
					}
				}
				if(!flag){
					//绑定关系	-1 解除；1新增；0绑定关系维持不变
					counterMap.put("Flag", CherryConstants.BA_COUNTERS_ADD);
					counterMap.put("EmployeeCode", map.get("employeeCode"));
					counterMap.put("CounterCode", counterCode);
					countersList.add(counterMap);
					newCountersList.remove(i);
					i--;
				}
			}
			//遍历老的绑定关系，确定解除的柜台
			for(int i = 0 ; i <= oldCountersList.size()-1 ; i++){
				Map<String,Object> oldTemp = oldCountersList.get(i);
				Map<String,Object> counterMap = new HashMap<String,Object>();
				String counterCode = (String)oldTemp.get("CounterCode");
				boolean flag = false;
				label:
				for(int j = 0 ; j <= newCountersList.size()-1 ; j++){
					Map<String,Object> newTemp = newCountersList.get(j);
					//如果老的绑定关系中有但是新的绑定关系中没有，则删除
					if(!newTemp.containsValue(counterCode)){
						flag = true;
						newCountersList.remove(j);
						j--;
						break label;
					}
				}
				if(!flag){
					//绑定关系	-1 解除；1新增；0绑定关系维持不变
					counterMap.put("Flag", CherryConstants.BA_COUNTERS_DELETE);
					counterMap.put("EmployeeCode", map.get("employeeCode"));
					counterMap.put("CounterCode", counterCode);
					countersList.add(counterMap);
				}
			}
		}//如果编辑前不是美容顾问（柜台主管），编辑后是美容顾问（柜台主管），将编辑后的所有的柜台绑定关系都设定为新增
		else if("1".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_ENABLE);
			List<Map<String,Object>> newCountersList = binOLBSCOM01_BL.getCounterInfoByEmplyeeId(map);
			//如果绑定关系全是新增的
			if(newCountersList != null && !newCountersList.isEmpty()){
				for(Map<String,Object> counterInfo : newCountersList){
					//绑定关系	-1 解除；1新增；0绑定关系维持不变
					counterInfo.put("Flag", CherryConstants.BA_COUNTERS_ADD);
					counterInfo.put("EmployeeCode", map.get("employeeCode"));
				}
				countersList.addAll(newCountersList);
			}
		}//如果编辑前是美容顾问（柜台主管），编辑后不是美容顾问（柜台主管），将编辑前的所有柜台绑定关系设定为解除
		else if("-1".equals(positionFlag)){
			//BA有效性区分    0 有效；1 无效
			map.put("validFlag", CherryConstants.VALIDFLAG_DISABLE);
			if(oldCountersList != null && !oldCountersList.isEmpty()){
				for(Map<String,Object> counterInfo : oldCountersList){
					//绑定关系	-1 解除；1新增；0绑定关系维持不变
					counterInfo.put("Flag", CherryConstants.BA_COUNTERS_DELETE);
					counterInfo.put("EmployeeCode", map.get("employeeCode"));
				}
			}
			countersList.addAll(oldCountersList);
		}
		
		return countersList;
	}
}
