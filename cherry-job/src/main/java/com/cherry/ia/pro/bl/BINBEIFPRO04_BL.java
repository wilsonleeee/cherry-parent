/*
 * @(#)BINBEIFPRO04_BL.java     1.0 2014/8/15
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
package com.cherry.ia.pro.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.ProductConstants;
import com.cherry.ia.pro.service.BINBEIFPRO01_Service;
import com.cherry.ia.pro.service.BINBEIFPRO04_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mqhelper.interfaces.MQHelper_IF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *产品下发(实时)BL
 * 
 * @author jijw
 * @version 1.0 2014/8/15
 */
public class BINBEIFPRO04_BL {

	/**
	 * 打印当前类的日志信息
	 */
	private static Logger loger = LoggerFactory.getLogger(BINBEIFPRO04_BL.class.getName());
	@Resource
	private BINBEIFPRO04_Service binbeifpro04_Service;

	@Resource
	private BINBEIFPRO01_Service binbeifpro01Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
 	@Resource(name = "CodeTable")
    private CodeTable codeTable;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;

	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;
	
	/** 产品功能开启时间处理总条数 */
	private int prtFunTotalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	/** 产品功能开启时间失败条数 */
	private int prtFunFailCount = 0;

	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";


	/**
	 * 产品列表的batch处理
	 *
	 * @param map
	 *
	 * @return Map
	 * @throws CherryBatchException
	 * @throws CherryException,Exception
	 */
	public Map<String,Object> tran_batchProductsMQSend(Map<String, Object> map)
			throws CherryBatchException, CherryException,Exception {

		Map<String,Object> resMap=new HashMap<String, Object>();

		try {
			// 备份产品下发数据
			map.remove("validFlagVal");
			binbeifpro04_Service.backProductIssue(map);

			// 产品表的表版本号在下发成功后+1
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(map);
			seqMap.put("type", "E");
			String newTVersion = binOLCM15_BL.getNoPadLeftSequenceId(seqMap);
			map.put("newTVersion", newTVersion);
			// 发送MQ
			// Step5: 调用MQHelper接口进行数据发送
			Map<String,Object> MQMap = getPrtNoticeMqMap(map, MessageConstants.MSG_SPRT_PRT);
			if(MQMap.isEmpty()){
				loger.error("产品实时下发通知组装异常");
				throw new Exception("产品实时下发通知组装异常");
			}
			//设定MQInfoDTO
			MQInfoDTO mqDTO = setMQInfoDTO(MQMap,map);
			mqDTO.setMsgQueueName("cherryToPosCMD");
			//调用共通发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqDTO,false);

		} catch (Exception e) {
			loger.error("产品下发失败",e);
			throw e;
		}
		resMap.put("flag", flag);
		return resMap;
	}
	/**
	 * 产品列表的batch处理
	 * 
	 * @param map
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 * @throws CherryException,Exception 
	 */
	public Map<String,Object> tran_batchProducts(Map<String, Object> map)
			throws CherryBatchException, CherryException,Exception {
		Map<String,Object> resMap=new HashMap<String, Object>();
		// 初始化
		Map<String,Object> comMap = init(map);
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 取得当前产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "E");
		String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("tVersion", tVersion);
		map.putAll(comMap);
		try {
			// 更新销售日期或价格生效日期在业务日期前后1天内的产品的版本号
			binbeifpro04_Service.updPrtVerByPrtSellDatePriceSaleDate(map);
		} catch (Exception e) {
			loger.error("更新销售日期或价格生效日期在业务日期前后1天内的产品的版本号失败",e);
			throw new Exception();
		}
		// 从接口数据库中分批取得产品列表，并处理
		while (true) {
			startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
			currentNum += 1;
			// 查询结束位置
			endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			map.put(CherryBatchConstants.SORT_ID, ProductConstants.UNITCODE);
			// 查询新后台产品列表
			List<Map<String, Object>> productsList = binbeifpro04_Service.getProductList(map);
			if (CherryBatchUtil.isBlankList(productsList)) {
				break;
			}
			// 统计总条数
			totalCount += productsList.size();
			// 更新接口数据库
			try{
				updIFDatabase(productsList,comMap);
			} catch(Exception e){
				loger.error("更新接口表数据失败",e);
				// 失败件数加
				failCount += productsList.size();
				fReason = "产品下发失败，详细日志文件。";
				throw e;
			}
			// 产品数据少于一页，跳出循环
			if (productsList.size() < CherryBatchConstants.DATE_SIZE) {
				break;
			}
		}
		// 根据指定Version取产品功能开启时间的产品信息List
		List<Map<String, Object>> prtFunDetailByVersionList = binbeifpro04_Service.getPrtFunDetailByVersionList(map);
		if (!CherryBatchUtil.isBlankList(prtFunDetailByVersionList)) {
			prtFunTotalCount += prtFunDetailByVersionList.size();

			for (Map<String, Object> prtFunDetailItemMap : prtFunDetailByVersionList) {
				prtFunDetailItemMap.putAll(map);
				prtFunDetailItemMap.put(ProductConstants.BRANDCODE, comMap.get(CherryBatchConstants.BRAND_CODE));
			}
			try{
				// 删除产品功能开启接口表(根据brandArr、PrtFunDateCode、产品厂商ID)
				binbeifpro04_Service.delIFPrtFunEnableAll(prtFunDetailByVersionList);
				// 插入产品功能开启接口表
				binbeifpro04_Service.addIFPrtFunEnableAll(prtFunDetailByVersionList);
			} catch(Exception e){
				// 失败件数加一
				prtFunFailCount += prtFunDetailByVersionList.size();
				loger.error("插入产品功能开启接口表时发生异常");
				throw new Exception();
			}
		}
		if(totalCount > 0 || prtFunTotalCount > 0) {
			// 备份产品下发数据
			map.put("IsSendMQ","1");
		}
		resMap.put("flag", flag);
		return resMap;
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
		String brandCode = ConvertUtil.getString(map.get(CherryBatchConstants.BRAND_CODE));
		mainData.put("BrandCode", brandCode);
		
		//单据号
		String tradeNoIf = binOLCM03_BL.getTicketNumber(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)), String.valueOf(map.get("loginName")), MessageConstants.MSG_PR);
		//如果单据号为空抛自定义异常：组装MQ消息体时失败，单据号取号失败！
		if(tradeNoIf==null||tradeNoIf.isEmpty()){
			loger.error("组装MQ消息体时失败，单据号取号失败！");
			throw new Exception();
		}
		mainData.put("TradeNoIF", tradeNoIf);
		//业务类型
		mainData.put("TradeType", MessageConstants.MSG_PR);
		//子类型
		mainData.put("SubType", ConvertUtil.getString(subType));
		// 表版本号
		mainData.put("TVersion", ConvertUtil.getString(map.get("newTVersion")));
		
		//一直向前增长的系统时间
		mainData.put("Time", ConvertUtil.getString(binbeifpro04_Service.getForwardSYSDate()));
		
		//操作者
		mainData.put("EmployeeId", ConvertUtil.getString(map.get("EmployeeId")));
		
		//将主数据放入dataLine中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		
		prtNoticeMqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		
		return prtNoticeMqMap;
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
	 * 更新接口数据库
	 * 
	 * @param list
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void updIFDatabase(List<Map<String, Object>> list,Map<String, Object> comMap) throws Exception {

		for (Map<String, Object> map : list) {
			// 取得产品分类list
			List<Map<String, Object>> cateList = (List<Map<String, Object>>) map.get("list");
			// 取得产品分类信息Map
			Map<String, Object> cateMap = getCateMap(cateList);
			//这里就是从code表中取出值
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put(CherryBatchConstants.BRAND_CODE, ConvertUtil.getString(comMap.get(CherryBatchConstants.BRAND_CODE)));
			//SAP产品类别，得到产品类别的值
			paraMap.put("codeType", "1245");
			paraMap.put("codeKey", map.get("ItemType"));
			//paraMap.put("language", language); 
			Map retMap = binbeifpro01Service.getCodeVal(paraMap);
			if(null!=retMap){
			String codeVal = ConvertUtil.getString(retMap.get("CodeVal"));
			// 品类名称
			cateMap.put("ItemTypeName", codeVal);
			}else{
				cateMap.put("ItemTypeName", null);
			}
			map.putAll(comMap);
			cateMap.putAll(map);
			try {
				// 保存接口产品表
				savePro(cateMap);
				// 保存接口BOM表
				saveProBom(map);
				// 插入产品条码对应关系表
				insertPrtBarCode(map);
			} catch (Exception e) {
				loger.error("产品下发失败");
				throw e;
			}
		}
	}

	/**
	 * 
	 * @param productMap
	 * @throws CherryBatchException 
	 */
	private void saveProBom(Map<String, Object> productMap) throws CherryBatchException,Exception {
		try { 
			// 删除该商品的BOMlist
			binbeifpro04_Service.delIFPrtBomSCS(productMap);
			List<Map<String, Object>> bomList = (List<Map<String, Object>>)productMap.get("BOMList");
			if(null != bomList && bomList.size() > 0) {
				for(Map<String, Object> bomMap : bomList) {
					bomMap.put(ProductConstants.BRANDCODE, productMap.get(ProductConstants.BRANDCODE));
				}
				// 删除后写入新的商品的BOMlist
				binbeifpro04_Service.insertIFPrtBomSCS(bomList);
			}

		} catch (Exception e) {
			// 品牌Code
			String brandCode = CherryBatchUtil.getString(productMap.get(ProductConstants.BRANDCODE));
			// 产品厂商ID
			String prtVendorId = CherryBatchUtil.getString(productMap.get("prtVendorId"));
			// 产品编码
			String unitCode = CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE));
			// 产品条码
			String barCode = CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE));
			// 中文名
			String nameTotal = CherryBatchUtil.getString(productMap.get(ProductConstants.NAMETOTAL));
			loger.error("插入产品BOM目录表时发生异常。品牌Code："+
					brandCode+"，产品厂商ID："+prtVendorId+"， 产品编码："+unitCode+"，产品条码："+barCode+"，产品名称："+nameTotal+"。");
			throw new Exception();
		}
	}

	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 * @throws CherryBatchException 
	 */
	private void insertPrtBarCode(Map<String, Object> map) throws CherryBatchException,Exception {
		// 查询产品条码对应关系件数
		int count = binbeifpro04_Service.getBarCodeCount(map);
		if (count == 0) {
			//如果这里是0的话，表明这个产品的条码是经过更改之后的条码
			try {
				// 找到更改之后的那条产品条码编码对应关系记录
				Map<String,Object> barCodeModifyMap = binbeifpro04_Service.getBarCodeModify(map);
				if(null != barCodeModifyMap){
					Map<String,Object> insertPrtSettingMap = new HashMap<String, Object>();
					insertPrtSettingMap.put("brand", map.get(CherryBatchConstants.BRAND_CODE)); // 品牌code
					insertPrtSettingMap.put("new_barcode", barCodeModifyMap.get("BarCode")); // 产品条码
					insertPrtSettingMap.put("new_unitcode", barCodeModifyMap.get("UnitCode")); // 产品编码
					insertPrtSettingMap.put("old_barcode", barCodeModifyMap.get("OldBarCode")); // 老产品条码
					insertPrtSettingMap.put("old_unitcode", barCodeModifyMap.get("OldUnitCode")); // 老产品编码
					insertPrtSettingMap.put("prt_id", barCodeModifyMap.get("BIN_ProductVendorID"));  // 产品厂商ID（新后台）
					insertPrtSettingMap.put("prt_type", "N"); // 产品（N）/促销品区分(P)
					insertPrtSettingMap.put("enable_time", map.get("enable_time")); // 新产品生效日期
					// 插入ProductSetting
					binbeifpro04_Service.addProductSetting(insertPrtSettingMap);
				}
				
				// 更新停用日时
				binbeifpro04_Service.updateClosingTime(map);
				// 插入产品条码对应关系表
				Map<String,Object> praMap = new HashMap<String,Object>();
				praMap.putAll(map);
				praMap.remove("validFlagVal");
				String validFlag = CherryBatchUtil.getString(praMap.get("ValidFlag"));
				//如果该产品有效的话
				if(!validFlag.equals(CherryConstants.VALIDFLAG_DISABLE)){
					binbeifpro04_Service.insertPrtBarCode(praMap);
				}
			} catch (Exception e) {
				// 品牌CODE
				String brandCode = CherryBatchUtil.getString(map.get(ProductConstants.BRANDCODE));
				// 产品厂商编码
				String prtVendorId = CherryBatchUtil.getString(map.get("prtVendorId"));
				// 产品编码
				String unitCode = CherryBatchUtil.getString(map.get(ProductConstants.UNITCODE));
				// 产品条码
				String barCode = CherryBatchUtil.getString(map.get(ProductConstants.BAR_CODE));
				// 中文名
				String nameTotal = CherryBatchUtil.getString(map.get(ProductConstants.NAMETOTAL));
				loger.error("插入产品条码对应表时发生异常。品牌Code："+
						brandCode+"，产品厂商ID："+prtVendorId+"，产品编码："+unitCode+"，产品条码："+barCode+"，产品名称："+nameTotal+"。");
				throw new Exception();
			}
		}
	}

	/**
	 * 取得产品分类信息Map
	 * @description 将该产品的分类信息List转换成Map的形式存储大分类，中分类，小分类
	 * @param list
	 * @return
	 */
	private Map<String, Object> getCateMap(List<Map<String, Object>> list) {
		Map<String, Object> cateMap = new HashMap<String, Object>();
		if (!CherryBatchUtil.isBlankList(list)) {
			for (Map<String, Object> map : list) {
				// 分类类型
				String cateType = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_TYPE));
				// 分类代码
				String cateCode = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_CODE));
				// 分类名
				String cateName = CherryBatchUtil.getString(map
						.get(ProductConstants.CATE_NAME));
				if (ProductConstants.CATE_TYPE_1.equals(cateType)) {
					// 大分类
					cateMap.put(ProductConstants.BCLASSCODE, cateCode);
					cateMap.put(ProductConstants.BCLASSNAME, cateName);
					
				} else if (ProductConstants.CATE_TYPE_3.equals(cateType)) {
					// 中分类
					cateMap.put(ProductConstants.MCLASSCODE, cateCode);
					cateMap.put(ProductConstants.MCLASSNAME, cateName);
				} else if (ProductConstants.CATE_TYPE_2.equals(cateType)) {
					// 小分类
					cateMap.put(ProductConstants.LCLASSCODE, cateCode);
					cateMap.put(ProductConstants.LCLASSNAME, cateName);
				}				
			}
		}
		return cateMap;
	}

	/**
	 * 插入接口数据库产品表
	 * 
	 * @param productMap
	 * @return
	 */
	private void savePro(Map<String, Object> productMap)
			throws CherryBatchException,Exception{
		try { 
			// 设置产品接口表的产品的状态值和计量单位
			getPrtScsStatus(productMap);
			// 查询是否有变化的unitcode barcode
			Map<String,Object> barCodeModifyMap = binbeifpro04_Service.getBarCodeModify(productMap);
			Map<String,Object> mergeRes = null;
			if(null != barCodeModifyMap){
				productMap.put("OldUnitCode", barCodeModifyMap.get("OldUnitCode"));
				productMap.put("OldBarCode", barCodeModifyMap.get("OldBarCode"));
				// 有变化，原编码条码在product_SCS对应的记录的status设为D
				// step1: 更新老的编码条码，将其在Product_SCS停用，version为当前tVersion+1
				Integer updRes = binbeifpro04_Service.disProductSCS(productMap);
			}
			// 无变化，编码条码没有变化的话将当前产品属性通过merge(brand,productId,unitcode,barcode)更新到product_scs上
			mergeRes = binbeifpro04_Service.mergeProductSCS(productMap);
			// 插入件数加一
			insertCount += 1;
		} catch (Exception e) {
			// 品牌CODE
			String brandCode = CherryBatchUtil.getString(productMap.get(ProductConstants.BRANDCODE));
			// 产品厂商编码
			String prtVendorId = CherryBatchUtil.getString(productMap.get("prtVendorId"));
			// 产品编码
			String unitCode = CherryBatchUtil.getString(productMap.get(ProductConstants.UNITCODE));
			// 产品条码
			String barCode = CherryBatchUtil.getString(productMap.get(ProductConstants.BAR_CODE));
			// 中文名
			String nameTotal = CherryBatchUtil.getString(productMap.get(ProductConstants.NAMETOTAL));
			loger.error("插入产品接口表时发生异常。品牌Code："+
					brandCode+"，产品厂商ID："+prtVendorId+"，产品编码："+unitCode+"，产品条码："+barCode+"，产品名称："+nameTotal+"。");
			throw new Exception();
		}
	}

	/**
	 * 设置产品接口表的状态值和计量单位值
	 *  处理逻辑 （实时下发接口状态值设值(0：正常销售、1：停用、2：下柜、3：未启用*)）
	 1、先判断validFlag数值 如果停用直接使用停用
	 2、如果validFlag=1,则判断是否下柜台，如果下柜台则使用下柜，如果未下柜，则判断是否在销售区间内，如果不在，则使用未启用

	 终端对product_SCS status定义：
	 E	表示	一个BARCODE对应多个UNITCODE时[product_scs往品牌的product导入时]，全部生效
	 D	表示	产品停用 	其他系统下发的数据，只有停用，无下柜和未启用状态
	 新增加下面两种状态：
	 H	表示	产品下柜	下发到witpos_xx数据库后都归为停用
	 G	表示	产品未启用 	下发到witpos_xx数据库后都归为停用

	 新后台对product_SCS status定义：
	 E：正常销售、D：停用、H：下柜、G：未启用
	 * @param productMap
	 */
	private void getPrtScsStatus(Map<String, Object> productMap){
		String prtscs_status = "E"; // Product_SCS 状态值,
		String ValidFlag = CherryBatchUtil.getString(productMap.get("ValidFlag"));
		String prtStatus = CherryBatchUtil.getString(productMap.get("status")); // 新后台产品状态 D：表明下柜产品； E：表明产品生效，可销售可订货；
		String sellDateFlag = CherryBatchUtil.getString(productMap.get("SellDateFlag")); // 是否不在销售日期区间  0:未过期 1:已过期
		
		if(CherryBatchConstants.VALIDFLAG_DISABLE.equals(ValidFlag)){
			// 产品无效
			prtscs_status = "D";
		}else{
			// 产品有效
			if(ProductConstants.PRODUCT_STATUS_D.equalsIgnoreCase(prtStatus)){
				// 产品下柜
				prtscs_status = "H";
			}else {
				//
				if(ProductConstants.SellDateFlag_1.equals(sellDateFlag)){
					// 产品未启用
					prtscs_status = "G";
				}else{
					// 有效，正常销售
					prtscs_status = "E";
				}
			}
		}
		productMap.put("prtscs_status", prtscs_status);
		// 转换ModuleCode的 key值为val值
		String moduleCode = CherryBatchUtil.getString(productMap.get("ModuleCode"));
		//以下找到计量单位的值
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("codeType", "1190");
		paraMap.put("codeKey", moduleCode);
		List<Map<String, Object>> codeList = binbeifpro04_Service.getCodeByKey(paraMap);
		
		String brandCode = CherryBatchUtil.getString(productMap.get(CherryBatchConstants.BRAND_CODE));
		String moduleValBrand = null;
		String moduleValOrg = null;
		
		for(Map<String, Object> codeMap : codeList){
			if(CherryBatchUtil.getString(codeMap.get("BrandCode")).equals(brandCode)){
				moduleValBrand = CherryBatchUtil.getString(codeMap.get("Value1"));
				break;
			}else if(CherryBatchUtil.getString(codeMap.get("BrandCode")).equals("-9999")){
				moduleValOrg = CherryBatchUtil.getString(codeMap.get("Value1"));
			}
		}
//		String codeValue = code.getVal("1190", moduleCode);
		productMap.put("ModuleCode", moduleValBrand != null ? moduleValBrand : moduleValOrg);
	}

	/**
	 * 输出处理结果信息
	 *
	 * @throws CherryBatchException
	 */
	public void outMessage() {
		int successCount = totalCount - failCount;
		//打印出总条数，
		loger.info("产品下发（实时）一共处理的条数为:" + totalCount);
		//成功条数
		loger.info("产品下发（实时）处理成功的条数为:" + successCount);
		//插入条数
		loger.info("产品下发（实时）处理成功的条数为:" + insertCount);
		//失败的条数
		loger.info("产品下发（实时）处理失败的条数为:" + failCount);
		String msg = "===========================产品下发（实时）";
		String endMsg = "结束===================================";
		switch (flag) {
			case CherryBatchConstants.BATCH_SUCCESS:
				loger.info(msg + "正常" + endMsg);
				break;
			case CherryBatchConstants.BATCH_WARNING:
				loger.warn(msg + "警告" + endMsg);
				break;
			case CherryBatchConstants.BATCH_ERROR:
				loger.error(msg + "失败" + endMsg);
			default:
				loger.info(msg + endMsg);
		}
	}
	
	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	public void tran_programEnd(Map<String,Object> paraMap) throws Exception{
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReason);
		paraMap.remove("validFlagVal");
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	private Map<String,Object> init(Map<String, Object> map) {
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binbeifpro04_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		map.put("businessDate", businessDate);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(bussDateMap.get("closeFlag"));
		// 业务日期+1
		String nextBussDate = DateUtil.addDateByDays(
				CherryBatchConstants.DATE_PATTERN, businessDate, 1);
		
		String sysdateTime = binbeifpro04_Service.getSYSDateTime();
		String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );
		// 停用日时
//		String closingTime = DateUtil.suffixDate(businessDate, 1);
		String closingTime = businessDate + " " + sysHHSSMM;
		// 启用日时
//		String startTime = DateUtil.suffixDate(nextBussDate, 0);
		String startTime = businessDate + " " + sysHHSSMM;
		// 当天业务结束，下发业务日期下一天的价格，否则下发当天价格
		if("1".equals(closeFlag)){
			map.put("priceDate", nextBussDate);
		}else{
			map.put("priceDate", businessDate);
		}
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT095");
		// 程序【开始运行时间】
		map.put("RunStartTime", sysdateTime);
		// 取得更新共通信息map
		Map<String,Object> comMap = getComMap(map);
		// 启用日时
		comMap.put(ProductConstants.STARTTIME, startTime);
		// 停用日时
		comMap.put(ProductConstants.CLOSINGTIME, closingTime);
		String enable_time =  businessDate + " " + sysHHSSMM;
		comMap.put("enable_time", enable_time);
		return comMap;
	}

	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 品牌Code
		String branCode = binbeifpro04_Service.getBrandCode(map);
		// 系统时间
		String sysTime = binbeifpro04_Service.getSYSDate();

		// 品牌Code
		baseMap.put(ProductConstants.BRANDCODE, branCode);
		// 作成时间
		baseMap.put(CherryBatchConstants.CREATE_TIME, sysTime);
		// 更新时间
		baseMap.put(CherryBatchConstants.UPDATE_TIME, sysTime);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFPRO04");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFPRO04");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		
		//当程序通过WebService调用时，做成者或更新者均为【Cherry】
		if (!CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("programFlag")))) {
			// 作成者
			baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.CHERRY_UPDATE_NAME);
			// 更新者
			baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.CHERRY_UPDATE_NAME);
		}
		// 有效区分
		baseMap.put("validFlagVal", "0");
		return baseMap;
	}
}
