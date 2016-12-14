/*		
 * @(#)BINOLBSCOM01_BL.java     1.0 2010/10/27		
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

package com.cherry.bs.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.common.service.BINOLBSCOM01_Service;
import com.cherry.bs.emp.service.BINOLBSEMP02_Service;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 基础模块POPUP画面共通BL
 * 
 * @author WangCT
 *
 */
public class BINOLBSCOM01_BL {
	
	/** 基础模块POPUP画面共通Service */
	@Resource
	private BINOLBSCOM01_Service binOLBSCOM01_Service;
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	@Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		
		// 取得员工总数
		return binOLBSCOM01_Service.getEmployeeCount(map);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		
		// 取得员工List
		return binOLBSCOM01_Service.getEmployeeList(map);
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getDepartCount(Map<String, Object> map) {
		
		// 取得部门总数
		return binOLBSCOM01_Service.getDepartCount(map);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		
		// 取得部门List
		return binOLBSCOM01_Service.getDepartList(map);
	}
	
	/**
	 * 取得柜台上级部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getHigherOrgCount(Map<String, Object> map) {
		
		// 取得部门总数
		return binOLBSCOM01_Service.getHigherOrgCount(map);
	}
	
	/**
	 * 取得柜台上级部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getHigherOrgList(Map<String, Object> map) {
		
		// 取得部门List
		return binOLBSCOM01_Service.getHigherOrgList(map);
	}
	
	/**
	 * 取得部门联系人总数
	 * 
	 * @param map 查询条件
	 * @return 部门联系人总数
	 */
	public int getDepartEmpCount(Map<String, Object> map) {
		
		// 取得部门联系人总数
		return binOLBSCOM01_Service.getDepartEmpCount(map);
	}
	
	/**
	 * 取得部门联系人List
	 * 
	 * @param map 查询条件
	 * @return 部门联系人List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getDepartEmpList(Map<String, Object> map) throws Exception {
		// 取得部门联系人List
		List<Map<String, Object>> departEmpList = binOLBSCOM01_Service.getDepartEmpList(map);
		// 解密手机号码
		decryptListData(map,departEmpList);
		return departEmpList;
	}
	
	/**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDeparyType(Map<String, Object> map) {
		
		// 根据部门ID取得部门类型
		return binOLBSCOM01_Service.getDeparyType(map);
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别等级
	 * 
	 * @param map 查询条件
	 * @return 岗位类别等级
	 */
	public String getPosCategoryGrade(Map<String, Object> map) {
		
		// 根据岗位类别ID取得岗位类别等级
		return binOLBSCOM01_Service.getPosCategoryGrade(map);
	}
	
	/**
	 * 取得区域总数
	 * 
	 * @param map 查询条件
	 * @return 区域总数
	 */
	public int getRegionCount(Map<String, Object> map) {
		
		// 取得区域总数
		return binOLBSCOM01_Service.getRegionCount(map);
	}
	/**
	 * 取得省份或直辖市总数
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市
	 */
	public int getProvinceCount(Map<String, Object> map) {
		
		// 取得省份总数
		return binOLBSCOM01_Service.getProvinceCount(map);
	}
	/**
	 * 取得区域List
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		
		// 取得区域List
		return binOLBSCOM01_Service.getRegionList(map);
	}
	/**
	 * 取得省份或直辖市List
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市List
	 */
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map) {
		
		// 取得省份或直辖市List
		return binOLBSCOM01_Service.getProvinceList(map);
	}
	/**
	 * 查询区域中品牌节点
	 * 
	 * */
	public String getBrandRegionPath(Map<String,Object> map){
		return binOLBSCOM01_Service.getBrandRegionPath(map);
	}

	/**
	 * 查询区域节点插入位置
	 * 
	 * */
	public String getNewRegNode(Map<String,Object> map){
		return binOLBSCOM01_Service.getNewRegNode(map);
	}
	
	/**
	 * 
	 * 插入区域节点，并返回自增ID
	 * @return 区域ID
	 * 
	 */
	public int insertRegNode(Map<String, Object> map) {
		return binOLBSCOM01_Service.insertRegNode(map);
	}
	
	/**
	 * 插入商场信息，并返回自增ID
	 * 
	 * */
	public int insertMallInfo(Map<String, Object> map){
		return binOLBSCOM01_Service.insertMallInfo(map);
	}
	
	/**
	 * 插入经销商信息，并返回自增ID
	 * 
	 * */
	public int insertResellerInfo(Map<String, Object> map){
		return binOLBSCOM01_Service.insertResellerInfo(map);
	}
	
	/**
	 * 
	 * 取得组织结构表中品牌下的未知节点
	 * 
	 * @param map 查询条件
	 * @return 品牌下的未知节点
	 * 
	 */
	public Object getUnknownPath(Map<String, Object> map){
		return binOLBSCOM01_Service.getUnknownPath(map);
	}
	
	/**
	 * 取得员工所在部门信息
	 * 
	 * */
	public Map<String,Object> getEmployeeOrgInfo(Map<String,Object> map){
		return binOLBSCOM01_Service.getEmployeeOrgInfo(map);
	}
	
	/**
	 * 查询组织结构中的柜台信息
	 * 
	 * */
	public List<Map<String,Object>> getOrganizationId(Map<String,Object> map){
		return binOLBSCOM01_Service.getOrganizationId(map);
	}
	
	/**
	 * 插入部门信息
	 * 
	 * */
	public int insertDepart(Map<String,Object> map){
		return binOLBSCOM01_Service.insertDepart(map);
	}
	/**
	 * 
	 * 根据区域ID取得它的上级区域
	 * 
	 * */
	public List<Map<String,Object>> getSuperRegion(int regionID){
		return binOLBSCOM01_Service.getSuperRegion(regionID);
	}
	
	/**
	 * 根据员工code和员工名称取得员工信息
	 * 
	 * */
	public List getEmployeeInfoList(Map<String,Object> map){
		return binOLBSCOM01_Service.getEmployeeInfoList(map);
	}
	
	/**
	 * 根据营业员对应的员工ID取得关注和归属的柜台信息
	 * 
	 * */
	public List<Map<String,Object>> getCounterInfoByEmplyeeId(Map<String,Object> map){
		return binOLBSCOM01_Service.getCounterInfoByEmplyeeId(map);
	}
	
	/**
	 * 根据员工ID取得员工有效性区分
	 * 
	 * */
	public boolean isEmployeeValidFlag(int employeeId){
		String validFlag = binOLBSCOM01_Service.getEmployeeValidFlag(employeeId);
		return "1".equals(validFlag)? true:false;
	}
	
	/**
	 * 组装员工信息的MQ消息
	 * 
	 * */
	public Map<String,Object> getEmployeeMqMap(Map<String,Object> map,List<Map<String,Object>> detailList,String flag) throws Exception{
		
		//申明要返回的map
		Map<String,Object> employeeMqMap = new HashMap<String,Object>();
		
		//根据员工ID取得员工code，姓名，联系方式等
		Map<String,Object> employeeInfoMap = binOLBSCOM01_Service.getEmployeeInfo(map);
		
		if(employeeInfoMap != null && !employeeInfoMap.isEmpty()){
			//品牌代码
			String brandCode = ConvertUtil.getString(binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID))));
			//BA有效性区分	1 有效；0无效
			String validFlag = (String) map.get("validFlag");
			//子类型，BA或者BAS
			String subType = flag;
			//BACODE
			String employeeCode = ConvertUtil.getString(employeeInfoMap.get("employeeCode"));
			//BANAME
			String employeeName = ConvertUtil.getString(employeeInfoMap.get("employeeName"));
			//联系电话
			String phone = ConvertUtil.getString(employeeInfoMap.get("phone"));
			//手机
			String mobilePhone = ConvertUtil.getString(employeeInfoMap.get("mobilePhone"));
			//身份证
			String identityCard = ConvertUtil.getString(employeeInfoMap.get("identityCard"));
//			//柜台主管code
//			String basCode = "";
//			//柜台主管名称
//			String basName = "";
//			//取得直属上级
//			Map<String,Object> highterInfo = binOLBSCOM01_Service.getHighterInfo(map);
//			if(highterInfo != null && !highterInfo.isEmpty()){
//				basCode = ConvertUtil.getString(highterInfo.get("employeeCode"));
//				basName = ConvertUtil.getString(highterInfo.get("employeeName"));
//			}
			
			//验证BRANDCODE是否为空
			if("".equals(brandCode)){
				throw new CherryException("EBS00068");
			}
			//验证BACODE是否为空
			if("".equals(employeeCode)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出人员编码！
				throw new CherryException("EBS00065");
			}
			
			//验证BANAME是否为空
			if("".equals(employeeName)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出人员名称！
				throw new CherryException("EBS00066");
			}
			
			//验证BA有效性区分是否正确
			if(!CherryConstants.VALIDFLAG_ENABLE.equals(validFlag)&&
					!CherryConstants.VALIDFLAG_DISABLE.equals(validFlag)){
				//抛出自定义异常：组装MQ消息体时失败，人员有效性区分不正确！
				throw new CherryException("EBS00067");
			}
			
			//组装消息体版本	Version
			employeeMqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.006.001");
			//组装消息体数据类型	DataType
			employeeMqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
			
			Map<String,Object> dataLine = new HashMap<String,Object>();
			//组装消息体主数据	MainData
			Map<String,Object> mainData = new HashMap<String,Object>();
			//品牌代码
			mainData.put("BrandCode", brandCode);
			//单据号
			String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_BAI_INFO);
			//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
			if(tradeNoIf==null||tradeNoIf.isEmpty()){
				throw new CherryException("EBS00069");
			}
			mainData.put("TradeNoIF", tradeNoIf);
			//业务类型
			mainData.put("TradeType", MessageConstants.MSG_BAI_INFO);
			//子类型
			mainData.put("SubType", subType);
			//BACODE
			mainData.put("EmployeeCode", employeeCode);
			//BANAME
			mainData.put("EmployeeName", employeeName);
//			//柜台主管code
//			mainData.put("BasCode", basCode);
//			//柜台主管名称
//			mainData.put("BasName", basName);
			//BA有效性区分
			mainData.put("ValidFlag", validFlag);
			//设定联系方式：如果有手机号码填写手机号码否则填写联系电话
//			String phoneNo = !"".equals(mobilePhone)? mobilePhone:"";
//			phoneNo = "".equals(phoneNo)? phone:phoneNo;

			mainData.put("Phone", phone);
			mainData.put("Mobilephone", mobilePhone);
			mainData.put("IdentityCard", identityCard);
			
			//一直向前增长的系统时间
			mainData.put("Time", binOLBSCOM01_Service.getForwardSYSDate());
			//将主数据放入dataLine中
			dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
			//将明细数据放入dataLine中
			
			// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) start *********//
			// BAS发明细行，BA不发明细行
			if("BAS".equals(flag)){
				if(detailList != null && !detailList.isEmpty()){
					dataLine.put(MessageConstants.DETAILDATA_MESSAGE_SIGN, detailList);
				}
			}
			// ********* 2012-10-12 新后台BA、BAS维护改进，发送MQ(NEWWITPOS-1623) end *********//
			
			employeeMqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		}else{
			//抛出自定义异常：组装MQ消息体时失败，未查询出相应的人员信息！
			throw new CherryException("EBS00064");
		}
		
		return employeeMqMap;
	}
	
	/**
	 * 组装产品下发通知的MQ消息
	 * @param map
	 * @param subType 子类型：PRT、DPRT	 必填，用于区分该消息体发送的是产品信息还是柜台产品信息
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getPrtNoticeMqMap(Map<String,Object> map,String subType) throws Exception{
		
		//申明要返回的map
		Map<String,Object> prtNoticeMqMap = new HashMap<String,Object>();
		
		//组装消息体版本	Version
		prtNoticeMqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.014.001");
		//组装消息体数据类型	DataType
		prtNoticeMqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
		
		Map<String,Object> dataLine = new HashMap<String,Object>();
		//组装消息体主数据	MainData
		Map<String,Object> mainData = new HashMap<String,Object>();
		//品牌代码
		String brandCode = ConvertUtil.getString(binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID))));
		mainData.put("BrandCode", brandCode);
		
		//单据号
		String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_PR);
		//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
		if(tradeNoIf==null||tradeNoIf.isEmpty()){
			throw new CherryException("EBS00069");
		}
		mainData.put("TradeNoIF", tradeNoIf);
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_PR);
		//子类型
		mainData.put("SubType", subType);
		// 表版本号
		mainData.put("TVersion", map.get("newTVersion"));
		
		//一直向前增长的系统时间
		mainData.put("Time", binOLBSCOM01_Service.getForwardSYSDate());
		
		//操作者
		mainData.put("EmployeeId", map.get("EmployeeId"));
		
		//将主数据放入dataLine中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		
		prtNoticeMqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		
		return prtNoticeMqMap;
	}
	
	/**
	 * 组装柜台信息的WebService
	 * 
	 * 
	 * */
	public Map<String,Object> getCounterWSMap(Map<String,Object> map) throws Exception{
		Map<String,Object> counterWSMap = new HashMap<String,Object>();
		// 取得柜台信息(新老后台交互时使用)【增加了柜台地址与柜台电话】
		Map<String, Object> counterInfo = binOLBSCNT04_Service.getCounterInfo(map);
		if(counterInfo != null && !counterInfo.isEmpty()){
			//品牌编码
			String brandCode = ConvertUtil.getString(counterInfo.get("BrandCode"));
			if("".equals(brandCode)){
				//抛出自定义异常：组装消息体时失败，品牌代码为空！
				throw new CherryException("EBS00068");
			}
			//柜台代码
			String counterCode = ConvertUtil.getString(counterInfo.get("CounterCode"));
			if("".equals(counterCode)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台编码！
				throw new CherryException("EBS00071"); 
			}
			//柜台名称
			String counterName = ConvertUtil.getString(counterInfo.get("CounterName"));
			if("".equals(counterName)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台名称！
				throw new CherryException("EBS00072"); 
			}
			//柜台协同区分
			String counterSynergyFlag =ConvertUtil.getString(counterInfo.get("CounterSynergyFlag"));
			if("".equals(counterSynergyFlag)){
				counterSynergyFlag="0";
			}
			//柜台类型
			String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
			if("".equals(counterKind)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出柜台类型！
				throw new CherryException("EBS00075"); 
			}
			//柜台有效性区分
			String validFlag = ConvertUtil.getString(counterInfo.get("ValidFlag"));
			if("".equals(validFlag)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台有效性区分！
				throw new CherryException("EBS00074"); 
			}
			// 到期日
			String expiringDate = ConvertUtil.getString(counterInfo.get("ExpiringDate"));
			if("".equals(expiringDate)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台的到期日！
				throw new CherryException("EBS00100"); 
			}
			
			//品牌代码
			counterWSMap.put("BrandCode", brandCode);
			//业务类型
			counterWSMap.put("BussinessType", "Counter");
			//消息体版本号
			counterWSMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
			//柜台代码
			counterWSMap.put("CounterCode", counterCode);
			//柜台名称
			counterWSMap.put("CounterName", counterName);
			//区域代码
			counterWSMap.put("RegionCode", ConvertUtil.getString(counterInfo.get("RegionCode")));
			//区域名称
			counterWSMap.put("RegionName", ConvertUtil.getString(counterInfo.get("RegionName")));
			//城市代码
			counterWSMap.put("CityCode", ConvertUtil.getString(counterInfo.get("Citycode")));
			//城市名称
			counterWSMap.put("CityName", ConvertUtil.getString(counterInfo.get("CityName")));
			//渠道名称
			counterWSMap.put("ChannelName", ConvertUtil.getString(counterInfo.get("Channel")));
			//经销商编码
			counterWSMap.put("AgentCode", ConvertUtil.getString(counterInfo.get("AgentCode")));
			//经销商名称
			counterWSMap.put("AgentName", ConvertUtil.getString(counterInfo.get("AgentName")));
			//柜台密码
			counterWSMap.put("PassWord", ConvertUtil.getString(counterInfo.get("PassWord")));
			//柜台地址
			counterWSMap.put("CounterAddress", ConvertUtil.getString(counterInfo.get("CounterAddress")));
			//柜台电话
			counterWSMap.put("CounterTelephone", ConvertUtil.getString(counterInfo.get("CounterTelephone")));
			//柜台协同区分
			counterWSMap.put("CounterSynergyFlag", counterSynergyFlag);
			//柜台类型
			counterWSMap.put("CounterKind", counterKind);
			//柜台有效性区分
			counterWSMap.put("ValidFlag", validFlag);
			//到期日
			counterWSMap.put("ExpiringDate", expiringDate);
		}else{
			//抛出自定义异常：组装MQ消息体是出错，没有查询出员工信息！
			throw new CherryException("EBS00070");
		}
		
		return counterWSMap;
	}
	
	
	/**
	 * 设定MQInfoDTO
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public MQInfoDTO setMQInfoDTO(Map<String,Object> MQMap,Map<String,Object> paramMap){
		
		Map<String,Object> mainData = (Map<String, Object>) ((Map<String, Object>) MQMap.get(MessageConstants.DATALINE_JSON_XML)).get(MessageConstants.MAINDATA_MESSAGE_SIGN);
		
		MQInfoDTO mqDTO = new MQInfoDTO();
		//数据源
		mqDTO.setSource(CherryConstants.MQ_SOURCE_CHERRY);
		//消息方向
		mqDTO.setSendOrRece(CherryConstants.MQ_SENDORRECE_S);
		//组织ID
		mqDTO.setOrganizationInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.ORGANIZATIONINFOID)));
		//所属品牌
		mqDTO.setBrandInfoId(ConvertUtil.getInt(paramMap.get(CherryConstants.BRANDINFOID)));
		//单据类型
		mqDTO.setBillType((String)mainData.get("TradeType"));
		//单据号
		mqDTO.setBillCode((String)mainData.get("TradeNoIF"));
		//队列名
		mqDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
		//消息体数据（未封装）
		mqDTO.setMsgDataMap(MQMap);
		//作成者
		mqDTO.setCreatedBy(String.valueOf(paramMap.get(CherryConstants.CREATEDBY)));
		//做成模块
		mqDTO.setCreatePGM(String.valueOf(paramMap.get(CherryConstants.CREATEPGM)));
		//更新者
		mqDTO.setUpdatedBy(String.valueOf(paramMap.get(CherryConstants.UPDATEDBY)));
		//更新模块
		mqDTO.setUpdatePGM(String.valueOf(paramMap.get(CherryConstants.UPDATEPGM)));
		
		//业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代码
		dbObject.put("OrgCode", paramMap.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", mainData.get("BrandCode"));
		// 业务类型
		dbObject.put("TradeType", mqDTO.getBillType());
		// 单据号
		dbObject.put("TradeNoIF", mqDTO.getBillCode());
		// 修改次数
		dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
		//MQ队列名
		dbObject.put("MsgQueueName", mqDTO.getMsgQueueName());
		 // 业务流水
		mqDTO.setDbObject(dbObject);
		
		return mqDTO;
	}
	/**
	 * 解密指定的人员信息List
	 * 
	 * @param employeeList
	 * @throws Exception
	 */
	private void decryptListData(Map<String, Object> map,List<Map<String, Object>> employeeList)
			throws Exception {
		if (null != employeeList && employeeList.size() > 0) {
			for (Map<String, Object> employeeInfo : employeeList) {
				// 品牌Code
				String  brandCode = ConvertUtil.getString(map.get("brandCode"));
				// 手机号码解密
				if (!CherryChecker.isNullOrEmpty(employeeInfo.get("mobilePhone"), true)) {
					String  mobilePhone = ConvertUtil.getString(employeeInfo.get("mobilePhone"));
					employeeInfo.put("mobilePhone",CherrySecret.decryptData(brandCode,mobilePhone));
				}
				// 电话解密
				if(!CherryChecker.isNullOrEmpty(employeeInfo.get("phone"),true)){
					String phone = ConvertUtil.getString(employeeInfo.get("phone"));
					employeeInfo.put("phone", CherrySecret.decryptData(brandCode,phone));
				}
				// email解密
				if(!CherryChecker.isNullOrEmpty(employeeInfo.get("email"),true)){
					String phone = ConvertUtil.getString(employeeInfo.get("email"));
					employeeInfo.put("email", CherrySecret.decryptData(brandCode,phone));
				}
			}
		}
	}
}
