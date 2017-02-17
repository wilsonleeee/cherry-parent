/*	
 * @(#)BINOLBSCNT03_BL.java     1.0 2011/05/09		
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

import com.cherry.bs.cnt.service.BINOLBSCNT03_Service;
import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.dep.service.BINOLBSDEP03_Service;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.dto.UdiskBindDetailDTO;
import com.cherry.cm.activemq.dto.UdiskBindMainDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.synchro.bs.interfaces.CounterSynchro_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import javax.annotation.Resource;
import java.util.*;

/**
 * 
 * 	更新柜台画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT03_BL {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT03_BL.class);
	
	/** 更新柜台画面Service */
	@Resource(name="binOLBSCNT03_Service")
	private BINOLBSCNT03_Service binOLBSCNT03_Service;
	
	/** 更新部门画面Service */
	@Resource(name="binOLBSDEP03_Service")
	private BINOLBSDEP03_Service binOLBSDEP03_Service;
	
	/** 添加员工画面Service */
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	/** 添加部门画面Service */
	@Resource(name="binOLBSDEP04_Service")
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 创建柜台画面Service */
	@Resource(name="binOLBSCNT04_Service")
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	@Resource(name="counterSynchro")
	private CounterSynchro_IF counterSynchro;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLBSCOM01_BL")
	private BINOLBSCOM01_BL binOLBSCOM01_BL;

	/** MongoDB 用户关注管辖部门履历表 **/
	public static final String MGO_DEPART_PRIVILEGE_LOG_NAME = "MGO_DepartPrivilegeLog";
	
	
	/**
	 * 更新柜台信息
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void tran_updateCounterInfo(Map<String, Object> map) throws Exception {
		
		// 取得数据库系统时间(YYYY-MM-DD HH：mm：SS)
		String sysTime = binOLBSCNT03_Service.getSYSDateTime();
		// 数据库系统时间(YYYY-MM-DD HH：mm：SS)
		map.put("systemTime", sysTime);
		// 数据库系统日期(YYYY-MM-DD)
		map.put("systemDate", sysTime.substring(0, 10));
		// 更新时间设定
		map.put(CherryConstants.UPDATE_TIME, sysTime);
		// 更新部门内容设定map
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.putAll(map);
		// 部门代码
		orgMap.put("departCode", map.get("counterCode"));
		// 更新时间（部门表）
		orgMap.put("modifyTime", map.get("modifyTimeDep"));
		// 更新次数（部门表）
		orgMap.put("modifyCount", map.get("modifyCountDep"));
		// 部门名称
		if(map.get("counterNameIF") != null && !"".equals(map.get("counterNameIF"))) {
			orgMap.put("departName", map.get("counterNameIF"));
		}
		// 部门简称
		if(map.get("counterNameShort") != null && !"".equals(map.get("counterNameShort"))) {
			orgMap.put("departNameShort", map.get("counterNameShort"));
		}
		// 部门英文名称
		if(map.get("nameForeign") != null && !"".equals(map.get("nameForeign"))) {
			orgMap.put("nameForeign", map.get("nameForeign"));
		}
		/****------去除了柜台英文简称，故柜台对应的部门的英文简称亦不用处理--------****/
//		// 部门英文简称
//		if(map.get("nameShortForeign") != null && !"".equals(map.get("nameShortForeign"))) {
//			orgMap.put("nameShortForeign", map.get("nameShortForeign"));
//		}
		// 状态
		if(map.get("status") != null && !"".equals(map.get("status"))) {
			orgMap.put("status", map.get("status"));
		}
		// 部门类型
		orgMap.put("type", "4");
		// 测试区分
		String counterKind = (String)map.get("counterKind");
		if(counterKind != null && "1".equals(counterKind)) {
			orgMap.put("testType", 1);
		} else {
			orgMap.put("testType", 0);
		}
		// 所属区域
		if(map.get("regionId") != null && !"".equals(map.get("regionId"))) {
			orgMap.put("regionId", map.get("regionId"));
		}
		// 所属省份
		if(map.get("provinceId") != null && !"".equals(map.get("provinceId"))) {
			orgMap.put("provinceId", map.get("provinceId"));
		}
		// 所属城市
		if(map.get("cityId") != null && !"".equals(map.get("cityId"))) {
			orgMap.put("cityId", map.get("cityId"));
		}
		// 所属县级市
		if(map.get("countyId") != null && !"".equals(map.get("countyId"))) {
			orgMap.put("countyId", map.get("countyId"));
		}
		// 到期日
		orgMap.put("expiringDate", map.get("expiringDate"));
		
		// 判断柜台是否已经过期
		String expiringDate = ConvertUtil.getString(map.get("expiringDate"));
		if(!"".equals(expiringDate)){
			int compare = DateUtil.compareDate((String)map.get("expiringDate"), sysTime);
			if(compare <= 0){
				orgMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_DISABLE);
				map.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_DISABLE);
				orgMap.put("status", "4");
				map.put("status", "4");
			} else {
				orgMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
				map.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
				if(map.get("status") != null && "4".equals(ConvertUtil.getString(map.get("status")))) {
					orgMap.put("status", "0");
					map.put("status", "0");
				}
			}
		}
		
		// 更新部门信息
		int result = binOLBSDEP03_Service.updateOrganizationInfo(orgMap);
		
		// 更新柜台仓库
		orgMap.put("inventoryNameCN", orgMap.get("departName")+CherryConstants.IVT_NAME_CN_DEFAULT);
		binOLBSDEP04_Service.updateDepotInfo(orgMap);
		
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		// 所属区域设定
		if(map.get("countyId") != null && !"".equals(map.get("countyId"))) {
			map.put("regionId", map.get("countyId"));
		} else if(map.get("cityId") != null && !"".equals(map.get("cityId"))) {
			map.put("regionId", map.get("cityId"));
		}
		// 更新柜台信息
		int counterResult = binOLBSCNT03_Service.updateCounterInfo(map);
		// 更新0件的场合
		if(counterResult == 0) {
			throw new CherryException("ECM00038");
		}
		
		// 更新部门协同区分
		if (!ConvertUtil.isBlank(ConvertUtil.getString(map.get("counterSynergyFlag")))) {
			// 更新部门协同区分
			binOLBSCNT03_Service.updateOrganizationSynergyFlag(map);
		}
		// 取得柜台状态
		String status = (String)map.get("status");
		// 取得原柜台状态
		String oldStatus = (String)map.get("oldStatus");
		// 柜台状态变更的场合
		if(status != null && !"".equals(status) && !status.equals(oldStatus)) {
			// 事件名称ID
			map.put("eventNameId", status);
			// 事件开始日
			map.put("fromDate", sysTime);
			// 事件终了日
			map.put("toDate", sysTime);
//			// 事件原因
//			map.put("eventReason", "");
			// 添加柜台事件
			binOLBSCNT04_Service.addCounterEvent(map);
		}
		
		// 删除柜台主管和关注柜台的人
		binOLBSCNT03_Service.delEmployeeDepart(map);
		List<Map<String, Object>> departList = new ArrayList<Map<String,Object>>();
		List<String> employeeList = new ArrayList<String>();
		// 取得柜台主管
		String counterHead = (String)map.get("counterHead");
		// 柜台主管存在的场合
		if(counterHead != null && !"".equals(counterHead)) {
			Map<String, Object> departMap = new HashMap<String, Object>();
			departMap.putAll(map);
			departMap.put("employeeId", counterHead);
			departMap.put("departType", "4");
			departMap.put("manageType", "1");
			departList.add(departMap);
			employeeList.add(counterHead);
		}
		
		// 取得关注该柜台的人
		List<String> likeCounterEmp = (List)map.get("likeCounterEmp");
		// 关注该柜台的人存在的场合
		if(likeCounterEmp != null && !likeCounterEmp.isEmpty()) {
			for(String employee : likeCounterEmp) {
				Map<String, Object> departMap = new HashMap<String, Object>();
				departMap.putAll(map);
				departMap.put("departType", "4");
				departMap.put("manageType", "0");
				departMap.put("employeeId", employee);
				departList.add(departMap);
				if(!employeeList.contains(employee)) {
					employeeList.add(employee);
				}
			}
		}
		if(!departList.isEmpty()) {
			binolbsemp04Service.insertEmployeeDepart(departList);
		}
		
		// 取得原柜台主管
		String oldCounterHead = (String)map.get("oldCounterHead");
		// 柜台主管是否变化
		boolean isMove = false;
		if(counterHead != null) {
			if(oldCounterHead != null && oldCounterHead.equals(counterHead)) {
				isMove = false;
			} else {
				isMove = true;
			}
		} else {
			if(oldCounterHead != null) {
				isMove = true;
			}
		}
		// 柜台主管变更的场合
		if(isMove) {
			// 上级部门节点
			String path = null;
			if(counterHead != null && !"".equals(counterHead)) {
				// 取得柜台主管的所属部门
				path = binOLBSCNT04_Service.getCounterHeaderDep(map);
			}
			if(path != null && !"".equals(path)) {
				map.put("path", path);
			} else {
				// 取得品牌下的未知节点
				path = binOLBSCNT04_Service.getUnknownPath(map);
				// 未知节点不为空的场合，该节点作为柜台的上级节点
				if(path != null && !"".equals(path)) {
					map.put("path", path);
				} else {
					// 在品牌下添加一个未知节点来作为没有上级部门的柜台的上级节点
					Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
					unknownOrgMap.putAll(map);
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
					map.put("path", unknownPath);
				}
			}
			// 取得新部门节点
			String newNodeId = binOLBSDEP04_Service.getNewNodeId(map);
			map.put("newNodeId", newNodeId);
			// 组织结构节点移动
			binOLBSDEP03_Service.updateOrganizationNode(map);
		}
		// 更新BA上级
		this.updateBaSuperiors(map);
		
		String organizationInfoId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
		String brandInfoId = (String)map.get("brandInfoId");
		// 开启U盘绑定功能的场合
		if(binOLCM14_BL.isConfigOpen("1030", organizationInfoId, brandInfoId)) {
			// 取得原柜台主管和关注柜台的人
			List<String> oldHeadLikeEmployee = (List)map.get("oldHeadLikeEmployee");
			if(oldHeadLikeEmployee != null && !oldHeadLikeEmployee.isEmpty()) {
				HashSet set = new HashSet(oldHeadLikeEmployee);
				oldHeadLikeEmployee.clear();
				oldHeadLikeEmployee.addAll(set);
			}
			// 拆分原柜台主管、关注柜台的人和新柜台主管、关注柜台的人
			if(employeeList != null && !employeeList.isEmpty()) {
				for(int i = 0; i < employeeList.size(); i++) {
					if(oldHeadLikeEmployee == null || oldHeadLikeEmployee.isEmpty()) {
						break;
					}
					for(int j = 0; j < oldHeadLikeEmployee.size(); j++) {
						if(employeeList.get(i).equals(oldHeadLikeEmployee.get(j))) {
							oldHeadLikeEmployee.remove(j);
							employeeList.remove(i);
							i--;
							break;
						}
					}
				}
			}
			List<UdiskBindDetailDTO> udiskBindDetailDTOList = new ArrayList<UdiskBindDetailDTO>();
			// 存在新柜台主管或新关注柜台的人，需要U盘绑定
			if(employeeList != null && !employeeList.isEmpty()) {
				Map<String, Object> employeeMap = new HashMap<String, Object>();
				employeeMap.put("employeeList", employeeList);
				// 根据员工ID取得U盘序列号List
				List<String> udiskSNList = binOLBSCNT04_Service.getUdiskSNList(employeeMap);
				if(udiskSNList != null && !udiskSNList.isEmpty()) {
					for(String udiskSN : udiskSNList) {
						UdiskBindDetailDTO udiskBindDetailDTO = new UdiskBindDetailDTO();
						udiskBindDetailDTO.setUdiskSN(udiskSN);
						udiskBindDetailDTO.setFlag("1");
						List<String> countercodeList = new ArrayList<String>();
						countercodeList.add((String)map.get("counterCode"));
						udiskBindDetailDTO.setCountercode(countercodeList);
						udiskBindDetailDTOList.add(udiskBindDetailDTO);
					}
				}
			}
			// 存在原柜台主管和原关注柜台的人，需要解除U盘绑定
			if(oldHeadLikeEmployee != null && !oldHeadLikeEmployee.isEmpty()) {
				Map<String, Object> employeeMap = new HashMap<String, Object>();
				employeeMap.put("employeeList", oldHeadLikeEmployee);
				// 根据员工ID取得U盘序列号List
				List<String> udiskSNList = binOLBSCNT04_Service.getUdiskSNList(employeeMap);
				if(udiskSNList != null && !udiskSNList.isEmpty()) {
					for(String udiskSN : udiskSNList) {
						UdiskBindDetailDTO udiskBindDetailDTO = new UdiskBindDetailDTO();
						udiskBindDetailDTO.setUdiskSN(udiskSN);
						udiskBindDetailDTO.setFlag("0");
						List<String> countercodeList = new ArrayList<String>();
						countercodeList.add((String)map.get("counterCode"));
						udiskBindDetailDTO.setCountercode(countercodeList);
						udiskBindDetailDTOList.add(udiskBindDetailDTO);
					}
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
				String createdby = map.get(CherryConstants.CREATEDBY).toString();
				udiskBindMainDTO.setTradeNoIF(binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, createdby, "UC"));
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
				// 取得组织代号
				String orgCode = (String)map.get("orgCode");
				// 组织代号
				dbObject.put("OrgCode", orgCode);
				// 品牌代码，即品牌简称
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", udiskBindMainDTO.getTradeType());
				// 单据号
				dbObject.put("TradeNoIF", udiskBindMainDTO.getTradeNoIF());
				// 修改次数
				dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
			    // 发生时间
			    dbObject.put("OccurTime", sysTime);
			    // 事件内容
			    dbObject.put("Content", mqInfoDTO.getData());
			    // 业务流水
			    mqInfoDTO.setDbObject(dbObject);
			    // 发送MQ消息处理
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}
		}
		//是否调用Webservice进行柜台数据同步
		if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
//			//通过WebService方式将柜台信息下发
//			Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(map);
//			if(WSMap.isEmpty()) return;
//			//WebService返回值Map
//			Map<String, Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
//			String State = ConvertUtil.getString(resultMap.get("State"));
//			String Data = ConvertUtil.getString(resultMap.get("Data"));
//			if(State.equals("ERROR")){
//				CherryException CherryException = new CherryException("");
//				CherryException.setErrMessage(Data);
//				throw CherryException;
//			}
			
			//柜台下发
			Map<String,Object> synchroInfo = counterSynchro.assemblingSynchroInfo(map);
			if(null != synchroInfo){
				// 操作类型--新增更新
				synchroInfo.put("Operate", "IUE");
				counterSynchro.synchroCounter(synchroInfo);
			}
		}
		
		// BAS变更日志 start
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ ConvertUtil.getString(map.get("counterCode")) +"柜台主管变更start  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(null == oldCounterHead){
			// 柜台主管从无到有
			if(null != counterHead){
				// 查询柜台主管对应柜台并发送MQ（新柜台主管）
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> (柜台主管从无到有) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.info("原主管id（employeeId）: " + oldCounterHead);
				logger.info("新主管id（employeeId）: " + counterHead);
			}
		}else {
			// 柜台主管从有到无
			if(null == counterHead){
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> (柜台主管从有到无) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.info("原主管id（employeeId）: " + oldCounterHead);
				logger.info("新主管id（employeeId）: " + counterHead);
			}
			// 柜台主管换人
			else {
				if(!oldCounterHead.equals(counterHead)){
					logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> (柜台主管换人) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					logger.info("原主管id（employeeId）: " + oldCounterHead);
					logger.info("新主管id（employeeId）: " + counterHead);
				}
			}
		}
		logger.info("参数(map): " + ConvertUtil.getString(map));
		logger.info("更新人(updatedBy): " + ConvertUtil.getString(map.get(CherryConstants.UPDATEDBY)));
		logger.info("更新程序(updatePGM): " + ConvertUtil.getString(map.get(CherryConstants.UPDATEPGM)));
		logger.info("更新时间(updateTime): " + ConvertUtil.getString(map.get("systemTime")));
		
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ ConvertUtil.getString(map.get("counterCode")) +"柜台主管变更end  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// BAS变更日志 end
		
		// 处理柜台主管MQ
		//是否开启维护BAS同时发送MQ消息配置
		boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(sendBasMQ){
			if(null == oldCounterHead){
				// 柜台主管从无到有
				if(null != counterHead){
					// 查询柜台主管对应柜台并发送MQ（新柜台主管）
					Map<String,Object> counterHeadMap = new HashMap<String, Object>();
					counterHeadMap.put("employeeId", counterHead);
					counterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
					counterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					setMQ(counterHeadMap);
				}
			}else {
				// 柜台主管从有到无
				if(null == counterHead){
					// 查询柜台主管对应柜台并发送MQ（原柜台主管）
					Map<String,Object> oldCounterHeadMap = new HashMap<String, Object>();
					oldCounterHeadMap.put("employeeId", oldCounterHead);
					oldCounterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
					oldCounterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					setMQ(oldCounterHeadMap);
				}
				// 柜台主管换人
				else {
					if(!oldCounterHead.equals(counterHead)){
						// 查询柜台主管对应柜台并发送MQ（原柜台主管）
						Map<String,Object> counterHeadMap = new HashMap<String, Object>();
						counterHeadMap.put("employeeId", oldCounterHead);
						counterHeadMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
						counterHeadMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
						setMQ(counterHeadMap);
						
						// 查询柜台主管对应柜台并发送MQ（新柜台主管）
						Map<String,Object> eToNeMap = new HashMap<String, Object>();
						eToNeMap.put("employeeId", counterHead);
						eToNeMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
						eToNeMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
						setMQ(eToNeMap);
					}
				}
			}
			
		}
		
		/**
		 * 编辑柜台保存后（无论是否发生改变）将数据写入MongoDB中
		 * 
		 * */
		this.insertMongoDepartPrivilegeLog(map);
		
		/*
		Map<String, Object> param = new HashMap<String, Object>();
		// 取得柜台信息(新老后台交互时使用)
		Map<String, Object> counterInfo = binOLBSCNT04_Service.getCounterInfo(map);
		// 品牌code
		param.put("BrandCode", (String)counterInfo.get("BrandCode"));
		// 柜台号
		param.put("CounterCode", (String)counterInfo.get("CounterCode"));
		// 柜台名
		if(counterInfo.get("CounterName") != null && !"".equals(counterInfo.get("CounterName"))) {
			param.put("CounterName", counterInfo.get("CounterName").toString());
		} else {
			param.put("CounterName", "null");
		}
		// 区域代码
		if(counterInfo.get("RegionCode") != null && !"".equals(counterInfo.get("RegionCode"))) {
			param.put("RegionCode", counterInfo.get("RegionCode").toString());
		} else {
			param.put("RegionCode", null);
		}
		// 区域名称
		if(counterInfo.get("RegionName") != null && !"".equals(counterInfo.get("RegionName"))) {
			param.put("RegionName", counterInfo.get("RegionName").toString());
		} else {
			param.put("RegionName", "null");
		}
		// 渠道
		if(counterInfo.get("Channel") != null && !"".equals(counterInfo.get("Channel"))) {
			param.put("Channel", counterInfo.get("Channel").toString());
		} else {
			param.put("Channel", null);
		}
		// 城市代码
		if(counterInfo.get("Citycode") != null && !"".equals(counterInfo.get("Citycode"))) {
			param.put("Citycode", counterInfo.get("Citycode").toString());
		} else {
			param.put("Citycode", null);
		}
		// 代理商编码
		if(counterInfo.get("AgentCode") != null && !"".equals(counterInfo.get("AgentCode"))) {
			param.put("AgentCode", counterInfo.get("AgentCode").toString());
		} else {
			param.put("AgentCode", null);
		}
		// 柜台类型
		if(counterInfo.get("counterKind") != null) {
			param.put("CounterKind", counterInfo.get("counterKind"));
		} else {
			param.put("CounterKind", 0);
		}
		counterSynchro.updCounter(param);
		*/
	}
	
	/**
	 * 将涉及到的用户关注管辖部门的具体内容写入Mongo
	 * @param map
	 * @throws Exception 
	 */
	private void insertMongoDepartPrivilegeLog(Map<String, Object> map) throws Exception {
		DBObject dbObject= new BasicDBObject();
		// 用户信息
		UserInfo userInfo = (UserInfo)map.get(CherryConstants.SESSION_USERINFO);
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
		dbObject.put("OperateChannel", "depart");
		// 部门ID
		dbObject.put("MainID", ConvertUtil.getString(map.get("organizationId")));
		// 柜台主管、关注该柜台的人员
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Manager", ConvertUtil.getString(map.get("counterHead")));
		// 关注人员IDList
		jsonMap.put("FollowedEmployee", (null == map.get("likeCounterEmp")) ? new ArrayList<String>() : map.get("likeCounterEmp"));
		// 涉及到的用户关注管辖部门的具体内容
		dbObject.put("JsonString", JSONUtil.serialize(jsonMap));
		
		// 写入Mongo
		MongoDB.insert(MGO_DEPART_PRIVILEGE_LOG_NAME, dbObject);
	}
	
	
	/**
	 * 查询柜台主管对应柜台并发送MQ
	 * @param map
	 * @throws Exception
	 */
	private void setMQ(Map<String,Object> map) throws Exception{
		//查询出该柜台主管对应柜台
		map.put("BasFlag", "true");
		Map<String,Object> employeeMap = binOLBSCNT04_Service.getEmployeeInfo(map);
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
	
	/**
	 * 更新BA上级
	 * 
	 * @param map
	 */
	public void updateBaSuperiors(Map<String,Object> map) {
		// 取得所属部门的员工
		List<Map<String, Object>> employeeInDepartList = binOLBSCNT03_Service.getEmployeeInDepartList(map);
		if(employeeInDepartList != null && !employeeInDepartList.isEmpty()) {
			String counterHead = (String)map.get("counterHead");
			String counterHeadPath = "/";
			if(counterHead != null && !"".equals(counterHead)) {
				counterHeadPath = binOLBSCNT03_Service.getEmployeePath(map);	
			}
			for(int i = 0; i < employeeInDepartList.size(); i++) {
				Map<String, Object> employeeInDepartMap = employeeInDepartList.get(i);
				Object superEmpId = employeeInDepartMap.get("superEmpId");
				// 判断BA的上级是否发生变化
				boolean isEqual = false;
				if(superEmpId != null && !"".equals(superEmpId.toString())) {
					if(counterHead != null && !"".equals(counterHead)) {
						if(superEmpId.toString().equals(counterHead)) {
							isEqual = true;
						}
					}
				} else {
					if(counterHead == null || "".equals(counterHead)) {
						isEqual = true;
					}
				}
				if(!isEqual) {
					employeeInDepartMap.put("path", counterHeadPath);
					employeeInDepartMap.put("newNodeId", binolbsemp04Service.getNewEmpNodeId(employeeInDepartMap));
					// 更新员工节点信息
					binOLBSCNT03_Service.updateEmpSuperiors(employeeInDepartMap);
				}
			}
		}
	}

}
