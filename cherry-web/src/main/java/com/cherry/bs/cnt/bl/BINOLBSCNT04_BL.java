/*	
 * @(#)BINOLBSCNT04_BL.java     1.0 2011/05/09		
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

import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP04_BL;
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
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.synchro.bs.interfaces.CounterSynchro_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.cache.annotation.CacheEvict;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 	创建柜台画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT04_BL {
	
	/** 创建柜台画面Service */
	@Resource(name="binOLBSCNT04_Service")
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	/** 添加部门画面Service */
	@Resource(name="binOLBSDEP04_Service")
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 添加员工画面Service */
	@Resource(name="binOLBSEMP04_Service")
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	@Resource(name="counterSynchro")
	private CounterSynchro_IF counterSynchro;
	
	/** 添加部门画面BL */
	@Resource(name="binOLBSDEP04_BL")
	private BINOLBSDEP04_BL binOLBSDEP04_BL;
	
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
	 * 添加柜台信息
	 * 
	 * @param map 添加内容
	 */
	@SuppressWarnings("unchecked")
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void tran_addCounterInfo(Map<String, Object> map) throws Exception {
		
		// 取得系统时间
		String sysDate = binOLBSCNT04_Service.getSYSDate();
		// 取得系统时间(YYYY-MM-DD HH：mm：SS)
		map.put("systemTime", sysDate.substring(0, 19));
		// 取得系统日期(YYYY-MM-DD)
		map.put("systemDate", sysDate.substring(0, 10));
		// 系统时间设定
		map.put(CherryConstants.CREATE_TIME, sysDate);
		
		String counterHead = (String)map.get("counterHead");
		
		Map<String, Object> orgMap = new HashMap<String, Object>();
		
		// 测试区分
		String counterKind = (String)map.get("counterKind");
		if(counterKind != null && "1".equals(counterKind)) {
			orgMap.put("testType", 1);
		} else {
			orgMap.put("testType", 0);
		}
		//organizationId
		int organizationId;
		//查询柜台在部门表中信息 不存在添加
		List<Map<String,Object>> departInfoList = binOLBSCOM01_BL.getOrganizationId(map);
		if(CherryUtil.isBlankList(departInfoList)){
				// 取得上级部门节点
				String path = null;
				// 取得柜台主管
				if(counterHead != null && !"".equals(counterHead)) {
					// 取得柜台主管的所属部门
					path = binOLBSCNT04_Service.getCounterHeaderDep(map);
				}
				// 上级部门节点存在的场合
				if(path != null && !"".equals(path)) {
					orgMap.put("path", path);
				} else {
					// 取得品牌下的未知节点
					path = binOLBSCNT04_Service.getUnknownPath(map);
					// 未知节点不为空的场合，该节点作为柜台的上级节点
					if(path != null && !"".equals(path)) {
						orgMap.put("path", path);
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
						orgMap.put("path", unknownPath);
					}
				}
				// 取得新部门节点
				String newNodeId = binOLBSDEP04_Service.getNewNodeId(orgMap);
				orgMap.putAll(map);
				orgMap.put("newNodeId", newNodeId);
				// 所属品牌
				orgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				// 部门代码
				orgMap.put("departCode", map.get("counterCode"));
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
				// 部门英文简称
		//		if(map.get("nameShortForeign") != null && !"".equals(map.get("nameShortForeign"))) {
		//			orgMap.put("nameShortForeign", map.get("nameShortForeign"));
		//		}
				// 部门状态
				if(map.get("status") != null && !"".equals(map.get("status"))) {
					orgMap.put("status", map.get("status"));
				}
				// 部门类型
				orgMap.put("type", "4");
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
				// 部门协同区分设定
				orgMap.put("counterSynergyFlag", map.get("counterSynergyFlag"));
				// 添加部门
				organizationId = binOLBSDEP04_Service.addOrganization(orgMap);
		}else{
				//部门信息存在直接获取部门信息
				organizationId = ConvertUtil.getInt(departInfoList.get(0).get("organizationId"));
		}
		
		// 添加仓库内容设定map
		Map<String, Object> ivtMap = new HashMap<String, Object>();
		// 作成者
		ivtMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名
		ivtMap.put(CherryConstants.CREATEPGM, "BINOLBSCNT04");
		// 更新者
		ivtMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		ivtMap.put(CherryConstants.UPDATEPGM, "BINOLBSCNT04");
		// 所属组织
		ivtMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
		ivtMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 所属部门
		ivtMap.put("organizationId", organizationId);
		// 缺省仓库区分
		ivtMap.put("defaultFlag", CherryConstants.IVT_DEFAULTFLAG);
		// 仓库名称
		ivtMap.put("inventoryNameCN", map.get("counterNameIF")+CherryConstants.IVT_NAME_CN_DEFAULT);
		// 设定仓库类型为柜台仓库
		ivtMap.put("depotType", "02");
		// 测试区分
		ivtMap.put("testType", orgMap.get("testType"));
		// 添加默认仓库处理
		binOLBSDEP04_BL.addDefaultDepot(ivtMap);
		
		// 新增的柜台的部门ID参数
		map.put("organizationId", organizationId);
		// 所属区域设定
		if(map.get("countyId") != null && !"".equals(map.get("countyId"))) {
			map.put("regionId", map.get("countyId"));
		} else if(map.get("cityId") != null && !"".equals(map.get("cityId"))) {
			map.put("regionId", map.get("cityId"));
		}
		// 添加柜台
		int counterInfoId = binOLBSCNT04_Service.addCounterInfo(map);
		map.put("counterInfoId", counterInfoId);
		// 事件名称ID
		map.put("eventNameId", map.get("status"));
		// 事件开始日
		map.put("fromDate", sysDate);
		// 事件终了日
		map.put("toDate", sysDate);
//		// 事件原因
//		map.put("eventReason", "");
		// 添加柜台事件
		binOLBSCNT04_Service.addCounterEvent(map);
		
		List<Map<String, Object>> departList = new ArrayList<Map<String,Object>>();
		List<String> employeeList = new ArrayList<String>();
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
				departMap.put("employeeId", employee);
				departMap.put("departType", "4");
				departMap.put("manageType", "0");
				departList.add(departMap);
				if(!employeeList.contains(employee)) {
					employeeList.add(employee);
				}
			}
		}
		if(!departList.isEmpty()) {
			binolbsemp04Service.insertEmployeeDepart(departList);
		}
		
		String organizationInfoId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
		String brandInfoId = (String)map.get("brandInfoId");
		// 开启U盘绑定功能的场合
		if(binOLCM14_BL.isConfigOpen("1030", organizationInfoId, brandInfoId)) {
			// 存在柜台主管或关注柜台的人，需要U盘绑定
			if(employeeList != null && !employeeList.isEmpty()) {
				Map<String, Object> employeeMap = new HashMap<String, Object>();
				employeeMap.put("employeeList", employeeList);
				// 根据员工ID取得U盘序列号List
				List<String> udiskSNList = binOLBSCNT04_Service.getUdiskSNList(employeeMap);
				if(udiskSNList != null && !udiskSNList.isEmpty()) {
					List<UdiskBindDetailDTO> udiskBindDetailDTOList = new ArrayList<UdiskBindDetailDTO>();
					for(String udiskSN : udiskSNList) {
						UdiskBindDetailDTO udiskBindDetailDTO = new UdiskBindDetailDTO();
						udiskBindDetailDTO.setUdiskSN(udiskSN);
						udiskBindDetailDTO.setFlag("1");
						List<String> countercodeList = new ArrayList<String>();
						countercodeList.add((String)map.get("counterCode"));
						udiskBindDetailDTO.setCountercode(countercodeList);
						udiskBindDetailDTOList.add(udiskBindDetailDTO);
					}
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
		//是否调用Webservice进行柜台数据同步
		if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
//			//通过MQ将柜台信息下发
//			Map<String,Object> WSMap = binOLBSCOM01_BL.getCounterWSMap(map);
//			if(WSMap.isEmpty()) return;
//			//WebService返回值Map
//			Map<String,Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
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
		
		// 柜台主管存在的场合
		if(counterHead != null && !"".equals(counterHead)) {
			//是否开启维护BAS同时发送MQ消息配置
			boolean sendBasMQ = binOLCM14_BL.isConfigOpen("1048",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));

			if(sendBasMQ){
				
				//查询出该员工关注和归属柜台
				map.put("employeeId", counterHead);
				map.put("BasFlag", true);
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
		}
		
		/**
		 *  将涉及到的用户关注管辖部门的具体内容写入Mongo
		 *  问题票：NEWWITPOS-2053
		 */
		this.insertMongoDepartPrivilegeLog(map);
		
//		//设定MQInfoDTO
//		MQInfoDTO mqDTO = binOLBSCOM01_BL.setMQInfoDTO(MQMap,map);
//		//调用共通发送MQ消息
//		binOLMQCOM01_BL.sendMQMsg(mqDTO,true);
		
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
		counterSynchro.addCounter(param);
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
	 * 柜台号唯一验证
	 * 
	 * @param map 查询条件
	 */
	public String getCounterInfoId(Map<String, Object> map) {
		
		// 柜台号唯一验证
		return binOLBSCNT04_Service.getCounterInfoId(map);
	}
	
	/**
	 * 取得城市区号
	 * @param map
	 * @return
	 */
	public String getCntTeleCode(Map<String, Object> map) {
		return binOLBSCNT04_Service.getCntTeleCode(map);
	}
	
	/**
	 * 取得城市区号 
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getCntCodeRightTree(Map<String, Object> map){
		return binOLBSCNT04_Service.getCntCodeRightTree(map);
	}

}
