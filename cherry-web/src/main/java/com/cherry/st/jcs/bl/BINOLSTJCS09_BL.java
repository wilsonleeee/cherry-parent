/*
 * @(#)BINOLSTJCS01_BL.java     1.0 2011/08/25
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
package com.cherry.st.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.jcs.interfaces.BINOLSTJCS09_IF;
import com.cherry.st.jcs.service.BINOLSTJCS09_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@SuppressWarnings("unchecked")
public class BINOLSTJCS09_BL implements BINOLSTJCS09_IF{
    @Resource
	private BINOLSTJCS09_Service binOLSTJCS09_service;
    @Resource
	private CodeTable CodeTable;
    @Resource
    private BINOLCM05_BL binOLCM05_BL;
    @Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
    
	/**WebService 共通BL*/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 逻辑仓库WebService接口头文件数据操作标记：添加 */
	public static final String operateType_A = "A";
	
	/** 逻辑仓库WebService接口头文件数据操作标记：编辑 */
	public static final String operateType_U = "U";
	
	/** 逻辑仓库WebService接口头文件数据操作标记：删除 */
	public static final String operateType_D = "D";
	
	/**
	 * 取得逻辑仓库关系List
	 * 
	 * @param map
	 * @return
	 */
    @Override
	public List<Map<String, Object>> searchLogicDepotList(Map<String, Object> map) {
    	List<Map<String, Object>> list = binOLSTJCS09_service.getLogicDepotList(map);
		return list;
	}
    
    /**
	 * 取得逻辑仓库总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchLogicDepotCount(Map<String, Object> map) {
		return binOLSTJCS09_service.getLogicDepotCount(map);
	}
	
	@Override
	public void tran_deleteLogicDepot(Map<String, Object> map) throws Exception{
		
		Map<String,Object> logicDepotMap = binOLSTJCS09_service.getLogicDepots(map);
		map.putAll(logicDepotMap);
		// 如果删除的数据业务配置信息为终端配置，则发送WebService消息
		if("1".equals(map.get("logicType"))){
			// 下发当前仓库类型为终端的逻辑仓库信息
			map.put("Operate", operateType_D); // DetailList列表字段：操作
			List<Map<String,Object>> detailList = binOLSTJCS09_service.getLogInvBusByBrandWithWS(map);
			map.put("DetailList", detailList);
			binOLSTJCS09_service.deleteLogicDepot(map);
			issued_BL(map);
		} else {
			binOLSTJCS09_service.deleteLogicDepot(map);
		}
	}
	
	 /**
     * 新增仓库逻辑关系
     * 
     * */
	public void tran_addLogicDepot(Map<String, Object> map) throws Exception {
		try{
			//验证该条逻辑仓库配置信息是否存在
			Map<String,Object> valitMap = new HashMap<String,Object>();
			valitMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
			valitMap.put("logicType", map.get("logicType"));
			valitMap.put("BusinessType", map.get("businessType"));
			String subType = ConvertUtil.getString(map.get("subType"));
			valitMap.put("SubType", subType);
			valitMap.put("ProductType", map.get("productType"));
			valitMap.put("BIN_LogicInventoryInfoID", map.get("logicInvId"));
			
			//如果不存在新增配置信息，如果该配置信息已存在抛出自定义异常警告
			if(!isExist(valitMap)){
				
				//调用service将新增配置关系写的数据库中
				int logicDepotId = binOLSTJCS09_service.addLogicDepot(map);
				map.put("logicDepotId", logicDepotId);
				
				// 下发当前仓库类型为终端的逻辑仓库信息
				if(map.get("logicType").equals("1")){
					map.put("Operate", operateType_A); // DetailList列表字段：操作
					List<Map<String,Object>> detailList = binOLSTJCS09_service.getLogInvBusByBrandWithWS(map);
					map.put("DetailList", detailList);
					issued_BL(map);
				}

			}else{
				throw new CherryException("EMO00066");
			}
		}catch(Exception e){
			throw e;
		}
		
	 }
	
	 /**
     * 编辑保存逻辑仓库业务关系
     * 
     * */
	@Override
	public void tran_EditInfosave(Map<String, Object> map) throws Exception {
		// 更新结果标记
		boolean updateFlag = false;
		
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		if(map.get("logicType").equals("1")){
			// 修改前的逻辑仓库数据
			map.put("Operate", operateType_D);
			detailList.addAll(0,binOLSTJCS09_service.getLogInvBusByBrandWithWS(map));
		}
		
		//验证该条逻辑仓库配置信息是否存在
		Map<String,Object> valitMap = new HashMap<String,Object>();
		valitMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
		valitMap.put("logicType", map.get("logicType"));
		valitMap.put("BusinessType", map.get("businessType"));
		valitMap.put("ProductType", map.get("productType"));
		valitMap.put("BIN_LogicInventoryInfoID", map.get("logicInvId"));
		String subType = ConvertUtil.getString(map.get("subType"));
		valitMap.put("SubType", subType);
		//查询是否已经存在该种配置关系
		List<Map<String,Object>> valitList = binOLSTJCS09_service.isExist(valitMap);
		if(null != valitList && !valitList.isEmpty()){
			//已经存在的配置信息的ID
			for(int i = 0 ; i < valitList.size() ; i++){
				
				//被编辑的配置信息ID
				String logicDepotId = ConvertUtil.getString(map.get("logicDepotId"));
				//系统中存在的相同配置信息的ID
				String BIN_LogicDepotBusinessID = ConvertUtil.getString(valitList.get(i).get("BIN_LogicDepotBusinessID"));
				
				//如果两个ID不相同只要删除说明新配置的相在数据库中已存在，抛出自定义异常警告
				if(!logicDepotId.equals(BIN_LogicDepotBusinessID)){
					
					throw new CherryException("EMO00066");
					
				} else {
					binOLSTJCS09_service.updateLogicDepotInfo(map);
					updateFlag = true;
				}
				
			}
		}else{
			binOLSTJCS09_service.updateLogicDepotInfo(map);
			updateFlag = true;
			
		}
		
		// 若更新成功且当前逻辑仓库业务所属为终端则下发当前仓库类型的业务配置信息
		if(updateFlag && map.get("logicType").equals("1")){
			
	    	// 修改后的逻辑仓库数据
	    	map.put("Operate", operateType_A);  // DetailList列表字段：操作
	    	detailList.addAll(1,binOLSTJCS09_service.getLogInvBusByBrandWithWS(map));
	    	// 下发当前仓库类型为终端的逻辑仓库信息
	    	map.put("DetailList", detailList);
	    	issued_BL(map);
		}
		
	}
	
	/**
	 * 设定MQInfoDTO
	 * 
	 * 
	 * */
	private MQInfoDTO setMQInfoDTO(Map<String,Object> MQMap,Map<String,Object> paramMap){
		
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
	 * 组装MQMap
	 * 
	 * */
	private Map<String,Object> getMQMap(Map<String,Object> map,UserInfo userInfo){
		
		//取得所有的订发货类型
		List<Map<String,Object>> allOrdDelType = CodeTable.getCodes("1168");
		
		if(allOrdDelType.isEmpty()) return new HashMap<String,Object>();
		
		Map<String,Object> MQMap = new HashMap<String,Object>();
		//Version
		MQMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.005.001");
		//DataType 数据类型
		MQMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
		
		Map<String,Object> dataLine = new HashMap<String,Object>();
		
		//主数据MAP
		Map<String,Object> mainData = new HashMap<String,Object>();
		//品牌代码
		mainData.put("BrandCode", binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID))));
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_DEPOT_ORDERDELIVERTYPE);
		//调用共通生成单据号
		mainData.put("TradeNoIF", binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), userInfo.getLoginName(), MessageConstants.MSG_DEPOT_ORDERDELIVERTYPE));
		//将主数据放到MQMap中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		
		//查询条件
		Map<String,Object> paraMap = new HashMap<String,Object>();
		//品牌
		paraMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
		//逻辑仓库类型：0后台，1终端	
		paraMap.put("Type", "1");
		//业务类型
		paraMap.put("BusinessType", map.get("businessType"));
		//产品类型
		paraMap.put("ProductType", 1);
		List<Map<String,Object>> configList = binOLSTJCS09_service.getConfigForSend(paraMap);
		
		List<Map<String,Object>> detailDataDTOList = new ArrayList<Map<String,Object>>();
		
		for(int i = 0 ; i < allOrdDelType.size() ; i++){
			Map<String,Object> temp = allOrdDelType.get(i);
			Map<String,Object> MQDetail = new HashMap<String,Object>();
			//订发货类型编码
			MQDetail.put("TypeCode", temp.get("CodeKey"));
			//中文名
			MQDetail.put("TypeNameCN", temp.get("Value"));
			//英文名
			MQDetail.put("TypeNameEN", "");
			//显示顺序
			MQDetail.put("DisplayOrder", String.valueOf(i+1));
			//逻辑仓库编码
			if(configList.isEmpty()){
				
				MQDetail.put("LogicInventoryCode", "");
				
			}else{
				
				//给当前循环加上标签便于break
				label:
				for(int j = 0 ; j < configList.size() ; j++){
					
					Map<String,Object> configTemp = configList.get(j);
					
					//将匹配的逻辑仓库放到MQDetail中，并跳出当前循环
					if(configTemp.get("SubType").equals(temp.get("CodeKey"))){
						MQDetail.put("LogicInventoryCode", configTemp.get("LogicInventoryCode"));
						configList.remove(j);
						break label;
					}
					
					//如果比较到最后一个仍然未匹配到，那么将LogicInventoryCode设置为空
					if(j == configList.size() - 1){
						MQDetail.put("LogicInventoryCode", "");
					}
					
				}
			}
			detailDataDTOList.add(MQDetail);
		}
		
		dataLine.put(MessageConstants.DETAILDATA_MESSAGE_SIGN, detailDataDTOList);
		
		MQMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		
		return MQMap;
	}
	
	 public Map getLogicDepots(Map<String, Object> map) {
		return binOLSTJCS09_service.getLogicDepots(map);
	 }
	
	/**
	 * 根据参数取得逻辑仓库信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getLogicDepotByPraMap(Map<String,Object> map){

		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
		paraMap.put("Type", map.get("Type"));
		paraMap.put("language", map.get("language"));
		
		List<Map<String,Object>> resultList = binOLSTJCS09_service.getLogicDepotByPraMap(paraMap);
		
		return null == resultList ? new ArrayList<Map<String,Object>>() : resultList;
	}
	
	
	
	/**
	 * 检查指的逻辑仓库配置信息是否存在
	 * @param map 存放了BIN_BrandInfoID（品牌ID），BusinessType（业务类型），ProductType（产品类型），
	 * BIN_LogicInventoryInfoID（逻辑仓库ID），SubType（子类型）
	 * 
	 * @return boolean 返回true说明存在，否则不存在
	 * 
	 * */
	private boolean isExist(Map<String,Object> map){
		
		List<Map<String,Object>> resultList = binOLSTJCS09_service.isExist(map);
		
		//resultList为空返回false，不为空返回true
		return (null == resultList || resultList.isEmpty())? false : true;
		
	}
	
	/**下发处理
	 * 
	 * @param
	 * @param operateType A:新增；U:修改；D:删除。
	 * @return
	 * @throws Exception 
	 * */
	private void issued_BL(Map<String, Object> map) throws Exception{
		//是否调用Webservice进行逻辑仓库业务配置数据下发
		boolean issuedWS = binOLCM14_BL.isConfigOpen("1062",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(issuedWS){
			Map<String,Object> logInvData =	getLogInvWSMap(map);
			
			if(null != logInvData){
				//WebService方式台信息Map
				Map<String,Object> resultMap = binOLCM27_BL.accessWebService(logInvData);
				String state = ConvertUtil.getString(resultMap.get("State"));
				if(state.equals("ERROR")){
					throw new CherryException("ECM00035");
				}
			}
		}
	}
	
	/**
	 * 组装逻辑仓库WebService数据
	 * @param map
	 * @return
	 */
	private Map<String,Object> getLogInvWSMap(Map<String,Object> map){
		
		Map<String,Object> logInvData = new HashMap<String,Object>();
		
		// 头文件
		Map<String,Object> dataHead = new HashMap<String,Object>();
		// 品牌代码
		dataHead.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dataHead.put("BussinessType", "BizWarehouse");
		// 消息体版本号
		dataHead.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
		// 1：不带子类型，写入逻辑仓库业务配置表；2：带子类型，写入code表
		// 目前只有终端入库具备子类型(2012-10-09)		
		dataHead.put("SubType", map.get("logicType").equals("1") && map.get("businessType").equals("OD") ? 2 : 1);
		
		// 明细数据行--逻辑仓库数据
		List<Map<String,Object>> detailList = (List<Map<String,Object>>)map.get("DetailList");
		
		if(detailList.size() == 0){
			return null;
		} else {
			setSubTypeData(detailList);
		}
		
		logInvData.putAll(dataHead);
		logInvData.put("DetailList", detailList);
		
		return logInvData;
	}
	
	/**
	 * 处理终端具有子类型的数据
	 * 
	 * @param detailList
	 */
	private void setSubTypeData(List<Map<String,Object>> detailList){
		
		for(Map<String,Object> detailMap :  detailList){
			String subType = (String)detailMap.get("subType");
			
			detailMap.put("SubBizTypeCode", subType); // 子类型代码
			detailMap.put("SubBizTypeName", null != subType ? CodeTable.getVal("1168", subType) : null); // 子类型名称
			detailMap.remove("subType");
		}
	}
	
}

	

