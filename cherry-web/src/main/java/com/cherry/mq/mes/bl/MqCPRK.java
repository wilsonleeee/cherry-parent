/*		
 * @(#)MqPDSH.java     1.0 2016-08-16		
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
package com.cherry.mq.mes.bl;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM22_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM25_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.st.common.bl.BINOLSTCM00_BL;
import com.cherry.st.common.bl.BINOLSTCM08_BL;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: MqCPRK
 * @Description: (产品入库MQ接收）
 * @author wangminze
 * @version  2016-11-08
 *
 */
public class MqCPRK implements MqReceiver_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(MqCPRK.class);

	@Resource(name="workflow")
    private Workflow workflow;

	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binOLCM01_BL;
	
	@Resource(name="binBEMQMES96_BL")
	private BINBEMQMES96_BL binBEMQMES96_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTCM14_BL")
	private BINOLSTCM14_IF binOLSTCM14_BL;
	
	@Resource(name="binOLSTCM06_BL")
	private BINOLSTCM06_IF binOLSTCM06_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_IF binOLCM22_BL;

	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;

	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	@Resource(name="binOLCM22_BL")
	private BINOLCM22_BL binolcm22_bl;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;    

	@Resource(name="binOLCM25_BL")
    private BINOLCM25_BL binOLCM25_BL;

	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_BL binOLSTCM08_BL;

	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_BL binOLSTCM00_BL;
    
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {

		logger.info("----------------------------产品入库MQ接收程序开始----------------------------");

		// 校验部门、员工、产品明细信息
		this.checkAndSetData(map);

		// 设置操作程序名称
		this.setUpdateInfoMapKey(map);

		//将云POS的产品入库信息插入数据库
		insertProductInDepot(map);

		logger.info("----------------------------产品入库MQ接收程序结束----------------------------");
	}


	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("CreatedBy", "-2");
		map.put("CreatePGM", "MqCPRK");
		map.put("UpdatedBy", "-2");
		map.put("UpdatePGM", "MqCPRK");
	}

	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {
		
		// 获取部门信息
		Map<String, Object> departInfo = binBEMQMES96_BL.getDepartInfo(map);
		String organizationID = "";
		if(null == departInfo || departInfo.isEmpty()) {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map,"部门号为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_06);
		} else {
			organizationID = ConvertUtil.getString(departInfo.get("organizationID"));
			// 设定部门ID
			map.put("organizationID", organizationID);
			// 设定部门名称
			map.put("departName", departInfo.get("departName"));
			// 用后即焚
			departInfo.clear();
			departInfo = null;

		}

		// 获取员工信息
		Map<String, Object> employeeInfo = binBEMQMES96_BL.getEmployeeInfo(map);
		if(null == employeeInfo || employeeInfo.isEmpty()) {
			// 没有查询到相关员工信息
			MessageUtil.addMessageWarning(map,"员工号为\""+map.get("employeeCode")+"\""+MessageConstants.MSG_ERROR_07);
		} else {
			// 设定员工ID
			map.put("employeeID", employeeInfo.get("employeeID"));
			// 设定员工姓名
			map.put("employeeName", employeeInfo.get("employeeName"));
			//设定岗位ID
			map.put("positionCategoryID", employeeInfo.get("positionCategoryID"));
			// 设定员工岗位
			map.put("categoryName", employeeInfo.get("categoryName"));
			// 用后即焚
			employeeInfo.clear();
			employeeInfo = null;
		}

		//获取当前柜台是否有POS机,有POS机说明是老后台的电子入库信息，否则是云POS的
		Map<String,Object> counterHasPosInfo = binBEMQMES96_BL.getCounterHasPosInfo(map);
		if( CherryChecker.isNullOrEmpty(counterHasPosInfo) ){
			MessageUtil.addMessageWarning(map,"部门号为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_92);
		}else if("1".equals(counterHasPosInfo.get("posFlag"))){
			map.put("hasPos","1");
		}else{
			map.put("hasPos","0");
		}


		// 查询仓库信息
		List<Map<String, Object>> depotList = binBEMQMES96_BL.getDepotsByDepartID(organizationID);
//		List<Map<String, Object>> depotList = binOLCM18_BL.getDepotsByDepartID(organizationID, "");
		if(depotList != null && !depotList.isEmpty()){
			// 设定实体仓库ID
			map.put("inventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
			// 用后即焚
			depotList.clear();
			depotList = null;
		}
		
		if("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))){
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"部门为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_36);
		}
		
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		if (!"".equals(tradeTime)) {
			//String tradeTimeHMS = tradeTime.substring(11, tradeTime.length());
			// 设定交易时间
			map.put("tradeDateTime",tradeDate + " "+tradeTime);
			// 设定HH:mm:SS格式的时间
			map.put("tradeTime", tradeTime);
		}


		// 校验明细中的产品及仓库相关信息
		List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailList");

		if(null == detailDataList || detailDataList.size() == 0) {

			MessageUtil.addMessageWarning(map, "产品入库时无明细数据");
		}

		for(int i=0; i < detailDataList.size(); i++) {
			Map<String, Object> detailDataMap = detailDataList.get(i);

			// 入库单据号
			if ("".equals(ConvertUtil.getString(detailDataMap.get("billNo")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "BillNo"));
			}
			// 厂商编码
			if ("".equals(ConvertUtil.getString(detailDataMap.get("unitcode")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Unitcode"));
			}else{
				Map<String,Object> retMap = binOLSTCM08_BL.selectProductIdByUnitCode(detailDataMap);
				if( retMap == null || retMap.size() ==0){
					//没有找到相应的产品信息
					MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_96);
				}
			}


			// 商品条码
			if ("".equals(ConvertUtil.getString(detailDataMap.get("barcode")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Barcode"));
			}else{
				Map<String,Object> retMap = binOLSTCM08_BL.selectProductVendorIdByBarCode(detailDataMap);
				if(retMap != null){
					detailDataMap.put("productVendorID",retMap.get("BIN_ProductVendorID"));
				}else{
					//没有找到相应的产品厂商信息
					MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_95);
				}

			}
			// 入库日期
			if ("".equals(ConvertUtil.getString(detailDataMap.get("inDepotDate")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "IndepotDate"));
			}else{
				map.put("inDepotDate",ConvertUtil.getString(detailDataMap.get("inDepotDate")));
			}
			// 价格
			if ("".equals(ConvertUtil.getString(detailDataMap.get("price")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Price"));
			}
			// 数量
			if ("".equals(ConvertUtil.getString(detailDataMap.get("quantity")))) {
				MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Quantity"));
			}

			//逻辑仓库
			if( CherryChecker.isNullOrEmpty(detailDataMap.get("logicInventoryCode")) ){

				//获取后台逻辑仓库默认仓库
				Map<String,Object> mapTemp = new HashMap<String,Object>();
				mapTemp.put("Type","0");
				mapTemp.put("OrderBy","1");
				List<Map<String,Object>> retList = binOLCM18_BL.getLogicDepotList(mapTemp);

				String defaultLogicDepot = ConvertUtil.getString(retList.get(0).get("LogicInventoryCode"));
				String defaultLogicDepotID = ConvertUtil.getString(retList.get(0).get("BIN_LogicInventoryInfoID"));
				detailDataMap.put("logicInventoryCode",defaultLogicDepot);//默认仓库类型
				detailDataMap.put("logicInventoryInfoID",defaultLogicDepotID);//默认仓库类型
			}

			Map<String,Object> retMap = binOLSTCM08_BL.selectInventoryIdByCounterCode(map);
			if( retMap != null ){
				detailDataMap.put("inventoryInfoID",retMap.get("BIN_InventoryInfoID"));
			}

			Map<String, Object> logicInventoryInfoMap = new HashMap<String, Object>();
			logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
			logicInventoryInfoMap.put("LogicInventoryCode", detailDataMap.get("logicInventoryCode"));

			logicInventoryInfoMap.put("Type", map.get("hasPos"));//老后台或新后台逻辑仓库

			logicInventoryInfoMap.put("language", null);
			Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
			if (logicInventoryInfo != null && !logicInventoryInfo.isEmpty()) {
				int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
				// 设定逻辑仓库ID
				detailDataMap.put("logicInventoryInfoID", logicInventoryInfoID);
			} else {
				// 没有查询到相关逻辑仓库信息
				MessageUtil.addMessageWarning(map, "逻辑仓库为\"" + detailDataMap.get("logicInventoryCode") + "\"" + MessageConstants.MSG_ERROR_37);
			}

		}
	}

	/**
	 * 将云POS的产品入库信息插入数据库
	 */
	public void insertProductInDepot(Map<String,Object> map) throws Exception {
		// 主数据
		Map<String, Object> mainMap = new HashMap<String, Object>();

		// 单据号
		String billNo = ConvertUtil.getString(map.get("billNo"));
		// 接口单据号
		String billNoIF = billNo;

		mainMap.put("organizationInfoID",map.get("organizationInfoID"));
		mainMap.put("brandInfoID",map.get("brandInfoID"));
		mainMap.put("organizationID",map.get("organizationID"));
		mainMap.put("employeeID",map.get("employeeID"));

		mainMap.put("billNo", billNo);
		mainMap.put("billNoIF", billNoIF);
		mainMap.put("inDepotDate",map.get("inDepotDate"));

		int quantityTemp = Math.round(ConvertUtil.getFloat(map.get("totalQuantity")));
		mainMap.put("totalQuantity", quantityTemp);
		mainMap.put("totalAmount", map.get("totalAmount"));
		mainMap.put("preTotalQuantity", quantityTemp);
		mainMap.put("preTotalAmount", map.get("totalAmount"));

		mainMap.put("logisticInfoID", "0");
		//入库状态
		mainMap.put("tradeStatus", "0");
		//审核状态
		mainMap.put("verifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);

		List<Map<String, Object>> inDepotdetailList = new ArrayList<Map<String,Object>>();
		inDepotdetailList = (List<Map<String, Object>>)map.get("detailList");

		int productInDepotId = 0;
		// 共通将数据添加到入库单主表和明细表
		productInDepotId = binOLSTCM08_BL.insertProductInDepotAllForMQ(mainMap, inDepotdetailList);

		//工作流
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, productInDepotId);

		pramMap.put("CurrentUnit", "MqRK");

		map.put("baCode",map.get("employeeCode"));
		Map<String,Object> employeeInfo = binBEMQMES99_Service.getEmployeeInfoByCode(map);
		map.put("employeeID",employeeInfo.get("employeeId"));
		//查询用户表获得用户ID
		Map userMap = binBEMQMES99_Service.selUserByEempID(map);

		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);//业务类型
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, productInDepotId);//
		pramMap.put("BIN_EmployeeID", map.get("employeeID"));//	员工ID
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//	员工ID
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		pramMap.put("BIN_OrganizationID", map.get("BIN_OrganizationID"));//接受订货的部门的部门ID
		pramMap.put("CurrentUnit", "MQ");//当前机能ID
		pramMap.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
		pramMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
		pramMap.put("BrandCode", map.get("brandCode"));//品牌编号
		Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(ConvertUtil.getString(map.get("BIN_OrganizationID")), null);
		if(null != departmentInfo && !departmentInfo.isEmpty()){
			pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));


		pramMap.put("UserInfo", userInfo);

		long workFlowId = 0;
		//开启工作流
		workFlowId = binOLSTCM00_BL.StartOSWorkFlow(pramMap);

		if (workFlowId == 0) {
			throw new CherryException("EWF00001");
		}

	}

}
