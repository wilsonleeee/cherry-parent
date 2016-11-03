package com.cherry.webservice.promotion.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.bl.BINOLMQCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ss.prm.bl.BINBESSPRM07_BL;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.promotion.service.SynchCampaignInfoService;
import com.googlecode.jsonplugin.JSONException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 微商城活动同步
 * @author lzs
 * 2016-06-22
 */
public class SynchCampaignInfoLogic implements IWebservice {
	
	private static Logger logger = LoggerFactory.getLogger(SynchCampaignInfoLogic.class.getName());
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_BL binOLMQCOM01_BL;
	
	/**共通 回执信息**/
	private Map<String,Object> resultMap=new HashMap<String, Object>();
	
	/**共通Map **/
	private Map<String,Object> comMap=new HashMap<String, Object>();
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	

	@Resource(name = "binbessprm07_BL")
	private BINBESSPRM07_BL binbessprm07_BL;
	
	@Resource
	private SynchCampaignInfoService synchCampaignInfoService;
	
	@Override
	public Map tran_execute(Map map) throws Exception {
		try {
			//验证传输参数有效性
			resultMap=veriFicationParam(map);			
			if(null!=resultMap && resultMap.size()!=0){
				return resultMap;
			}
			Map<String, Object> mainDataJsonMap = (Map<String, Object>)map.get("MainData");		
			map.putAll(mainDataJsonMap);
			setInsertInfoMapKey(map);
			//插入活动同步主表
			int campaignSynchInfoId = synchCampaignInfoService.insertCampaignSynchInfo(map);			
			map.put("campaignSynchInfoId", campaignSynchInfoId);			
			List<Map<String, Object>> campaignDetailJsonList = (List<Map<String, Object>>)map.get("CampaignDetail");
			if(!campaignDetailJsonList.isEmpty()){
				for(Map<String, Object> campaignDetailMap : campaignDetailJsonList){
					campaignDetailMap.putAll(map);
				}
				//插入活动同步明细表
				synchCampaignInfoService.insertCampaignSynchDetailInfo(campaignDetailJsonList);
			}
			resultMap.put("ERRORCODE", "0");
			resultMap.put("ERRORMSG", "");
			//执行数据下发，并发送通知MQ至终端
			issuedData(mainDataJsonMap,campaignDetailJsonList,map);
			return resultMap;	
		} catch (Exception e) {
            logger.error("WS ERROR:", e);
            logger.error("WS ERROR brandCode:"+ comMap.get("BrandCode"));
            logger.error("WS ERROR paramData:"+ comMap.get("OriginParamData"));
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			return resultMap;
		}
	}
	private Map<String,Object> veriFicationParam(Map<String,Object> map) throws JSONException{
		// 新后台未找到匹配数据
		if (CherryBatchUtil.isBlankString(ConvertUtil.getString(map.get("BIN_BrandInfoID")))) {
			resultMap.put("ERRORCODE", "WSE9998");
			resultMap.put("ERRORMSG", "参数brandCode错误");
			return resultMap;
		}
		//主数据为空
		Map<String, Object> mainDataJsonMap =  (Map<String,Object>)map.get("MainData");
		if(null != mainDataJsonMap && !mainDataJsonMap.isEmpty()){
			//主题活动编码不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("SubjectCode")))){
				resultMap.put("ERRORCODE", "WSE0091");
				resultMap.put("ERRORMSG", "主题活动编码不能为空");
				return resultMap;
			}
			
			//主题活动编码以ASW开头
			if(!ConvertUtil.getString(mainDataJsonMap.get("SubjectCode")).startsWith("ASW")){
				resultMap.put("ERRORCODE", "WSE0092");
				resultMap.put("ERRORMSG", "主题活动编码应以ASW开头");
				return resultMap;
			}	
			
			//主题活动不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("SubjectName")))){
				resultMap.put("ERRORCODE", "WSE0093");
				resultMap.put("ERRORMSG", "主题活动不能为空");
				return resultMap;
			}
			
			//子活动编码不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("MainCode")))){
				resultMap.put("ERRORCODE", "WSE0094");
				resultMap.put("ERRORMSG", "子活动编码不能为空");
				return resultMap;
			}
			
			//MCW开头
			if(!ConvertUtil.getString(mainDataJsonMap.get("MainCode")).startsWith("MCW")){
				resultMap.put("ERRORCODE", "WSE0095");
				resultMap.put("ERRORMSG", "子活动编码应以WCW开头");
				return resultMap;
			}	
			
			//子活动名称不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("MainName")))){
				resultMap.put("ERRORCODE", "WSE0096");
				resultMap.put("ERRORMSG", "子活动名称不能为空");
				return resultMap;
			}
			
			//柜台活动编码不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("ActivityCode")))){
				resultMap.put("ERRORCODE", "WSE0097");
				resultMap.put("ERRORMSG", "柜台活动编码不能为空");
				return resultMap;
			}
			
			//W开头
			if(!ConvertUtil.getString(mainDataJsonMap.get("ActivityCode")).startsWith("W")){
				resultMap.put("ERRORCODE", "WSE0098");
				resultMap.put("ERRORMSG", "柜台活动编码应以W开头");
				return resultMap;
			}
			
			//柜台活动名称不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("ActivityName")))){
				resultMap.put("ERRORCODE", "WSE0099");
				resultMap.put("ERRORMSG", "柜台活动名称不能为空");
				return resultMap;
			}
			
			//活动开始时间不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("StartTime")))){
				resultMap.put("ERRORCODE", "WSE0100");
				resultMap.put("ERRORMSG", "活动开始时间不能为空");
				return resultMap;
			}	
			
			//String pattern="^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
			String pattern="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
			//活动开始时间格式验证
			if(!ConvertUtil.getString(mainDataJsonMap.get("StartTime")).matches(pattern)){
				resultMap.put("ERRORCODE", "WSE0101");
				resultMap.put("ERRORMSG", "活动开始时间格式错误");
				return resultMap;
			}	
			
			//活动结束时间不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("EndTime")))){
				resultMap.put("ERRORCODE", "WSE0102");
				resultMap.put("ERRORMSG", "活动结束时间不能为空");
				return resultMap;
			}
			//活动结束时间格式验证
			if(!ConvertUtil.getString(mainDataJsonMap.get("StartTime")).matches(pattern)){
				resultMap.put("ERRORCODE", "WSE0103");
				resultMap.put("ERRORMSG", "活动结束时间格式错误");
				return resultMap;
			}
			
			//活动来源不能为空
			if(CherryBatchUtil.isBlankString(ConvertUtil.getString(mainDataJsonMap.get("DataSource")))){
				resultMap.put("ERRORCODE", "WSE0104");
				resultMap.put("ERRORMSG", "活动来源不能为空");
				return resultMap;
			}						
			
			//活动来源固定为Wechat
			if(!ConvertUtil.getString(mainDataJsonMap.get("DataSource")).equals("Wechat")){
				resultMap.put("ERRORCODE", "WSE0105");
				resultMap.put("ERRORMSG", "活动来源固定为Wechat");
				return resultMap;
			}			
		} else {
			resultMap.put("ERRORCODE", "WSE9998");
			resultMap.put("ERRORMSG", "主数据不能为空");
			return resultMap;
		}
		
		// 活动数据明细行为空
		List<Map<String, Object>> campaignDetailJsonList = (List<Map<String,Object>>)map.get("CampaignDetail");
		if(!CherryBatchUtil.isBlankList(campaignDetailJsonList)){
			for(Map<String, Object> campaignDetailJsonMap : campaignDetailJsonList){
				String barcode = ConvertUtil.getString(campaignDetailJsonMap.get("Barcode"));
				String Unitcode = ConvertUtil.getString(campaignDetailJsonMap.get("Unitcode"));
				//获得共通参数
				campaignDetailJsonMap.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
				campaignDetailJsonMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
				//商品条码不能为空
				if(CherryBatchUtil.isBlankString(barcode)){
					resultMap.put("ERRORCODE", "WSE0106");
					resultMap.put("ERRORMSG", "商品条码不能为空");
					return resultMap;
				}
				//厂商编码不能为空
				if(CherryBatchUtil.isBlankString(Unitcode)){
					resultMap.put("ERRORCODE", "WSE0107");
					resultMap.put("ERRORMSG", "厂商编码不能为空");
					return resultMap;
				}								
				
				//数量不能为空
				if(CherryBatchUtil.isBlankString(ConvertUtil.getString(campaignDetailJsonMap.get("ActivityQty")))){
					resultMap.put("ERRORCODE", "WSE0110");
					resultMap.put("ERRORMSG", "数量不能为空");
					return resultMap;
				}
				
				//价格不能为空
				if(CherryBatchUtil.isBlankString(ConvertUtil.getString(campaignDetailJsonMap.get("Price")))){
					resultMap.put("ERRORCODE", "WSE0111");
					resultMap.put("ERRORMSG", "价格不能为空");
					return resultMap;
				}
				
				//PromotionType 为DHCP时 积分值不能为空
				if(ConvertUtil.getString(campaignDetailJsonMap.get("PromotionType")).equals("DHCP")&&CherryBatchUtil.isBlankString(ConvertUtil.getString(campaignDetailJsonMap.get("Point")))){
					resultMap.put("ERRORCODE", "WSE0112");
					resultMap.put("ERRORMSG", "促销类型为DHCP时 积分值不能为空");
					return resultMap;
				}
				
				//ProType为P时促销类型不能为空
				if(ConvertUtil.getString(campaignDetailJsonMap.get("ProType")).equals("P")&&CherryBatchUtil.isBlankString(ConvertUtil.getString(campaignDetailJsonMap.get("PromotionType")))){
					resultMap.put("ERRORCODE", "WSE0113");
					resultMap.put("ERRORMSG", "产品类型为P时促销类型不能为空");
					return resultMap;
				}
								
				//产品类型不能为空
				if(CherryBatchUtil.isBlankString(ConvertUtil.getString(campaignDetailJsonMap.get("ProType")))){
					resultMap.put("ERRORCODE", "WSE0114");
					resultMap.put("ERRORMSG", "产品类型不能为空");
					return resultMap;
				}
				
				//产品类型格式验证
				if(!(ConvertUtil.getString(campaignDetailJsonMap.get("ProType")).equals("N")||ConvertUtil.getString(campaignDetailJsonMap.get("ProType")).equals("P"))){
					resultMap.put("ERRORCODE", "WSE0115");
					resultMap.put("ERRORMSG", "产品类型只能为N或P");
					return resultMap;
				}
				//验证商品
				if(!CherryBatchUtil.isBlankString(barcode) && !CherryBatchUtil.isBlankString(Unitcode)){
					//当为产品时，判断是否存在并返回产品厂商ID
					if(ConvertUtil.getString(campaignDetailJsonMap.get("ProType")).equals("N")){			
						String proVendorId = synchCampaignInfoService.getProductVendorId(campaignDetailJsonMap);
						if(CherryBatchUtil.isBlankString(proVendorId)){
							resultMap.put("ERRORCODE", "WSE0108");
							resultMap.put("ERRORMSG", "产品不存在");
							return resultMap;
						}
						campaignDetailJsonMap.put("proVendorId", proVendorId);
					} 
					//当为促销品时，判断是否存在并数据正确
					if(ConvertUtil.getString(campaignDetailJsonMap.get("ProType")).equals("P")){
						Map<String, Object>  promotionProduct = synchCampaignInfoService.getPromotionProduct(campaignDetailJsonMap);
						if(null !=promotionProduct && !promotionProduct.isEmpty()){
							if(!ConvertUtil.getString(promotionProduct.get("Point")).equals(ConvertUtil.getString(campaignDetailJsonMap.get("Point")))){
								resultMap.put("ERRORCODE", "WSE0109");
								resultMap.put("ERRORMSG", "促销品积分不匹配");
								return resultMap;
							}
						} else {
							//插入促销品
							try {
							int proVendorId = savePrmBackId(mainDataJsonMap,campaignDetailJsonMap);
							if(proVendorId != 0){
								campaignDetailJsonMap.put("proVendorId", proVendorId);
							}
							} catch (Exception e) {
								logger.error(e.getMessage(),e);
							}
						}
					}
				}
				
			}
		} else {
			resultMap.put("ERRORCODE", "WSE9998");
			resultMap.put("ERRORMSG", "活动数据明细不能为空");
			return resultMap;
		} 
		return resultMap;
	}
	/**
	 * 下发数据至老后台，并发送MQ通知终端进行导入
	 * @param mainDataMap 主单数据
	 * @param detailList  对应明细数据
	 * @throws Exception
	 */
	public void issuedData(Map<String,Object> mainDataMap,List<Map<String,Object>> detailList,Map<String,Object> paramMap) {
		try {
			Map<String,Object> commonMap = new HashMap<String, Object>();
			//品牌代码
			commonMap.put("Brand", paramMap.get("BrandCode"));
			//活动状态
			commonMap.put("status", "CL");
			//活动构成是否可修改
			commonMap.put("pos_modify", "0");
			//创建时间
			commonMap.put("created", synchCampaignInfoService.getSYSDate());
			//修改时间
			commonMap.put("modified", synchCampaignInfoService.getSYSDate());
			
			//接收主数据，明细数据分别插入ActivityAssociateSubject_scs，ActivityAssociateTable_SCS，ActivityTable_scs三张表，程序无需做逻辑判断处理,数据下发到中间库
			Map<String,Object> activitySubjectMap = new HashMap<String,Object>();
			activitySubjectMap.putAll(commonMap);
			//主题活动编码
			activitySubjectMap.put("subject_code", mainDataMap.get("SubjectCode"));
			//主题活动名称
			activitySubjectMap.put("name", mainDataMap.get("SubjectName"));
			//数据录入来源
			activitySubjectMap.put("data_source", mainDataMap.get("DataSource"));
			//活动类别
			activitySubjectMap.put("type", "DHHD");
			int result = synchCampaignInfoService.getActivityAssociateSubjectInfo(activitySubjectMap);
			if (result == 0) {
				logger.info("*******************主活动接口表不存在该数据，执行插入操作*********************");
				//执行数据插入操作
				synchCampaignInfoService.insertActivitySubject(activitySubjectMap);
			}
			
			Map<String,Object> activityAssociateMap = new HashMap<String, Object>();
			activityAssociateMap.putAll(commonMap);
			//主题活动编码
			activityAssociateMap.put("subject_code", mainDataMap.get("SubjectCode"));
			//子活动编码
			activityAssociateMap.put("MainCode", mainDataMap.get("MainCode"));
			//子活动名称
			activityAssociateMap.put("MainName", mainDataMap.get("MainName"));
			//可用名称
			activityAssociateMap.put("Function", "-1");
			//主活动最大领用量 -1表示无限制
			activityAssociateMap.put("MaxReceiveQty", "-1");
			//主活动组合修改
			activityAssociateMap.put("MainModify", "0");
			//子活动的活动类型
			activityAssociateMap.put("type", "PX");
			//活动类别
			activityAssociateMap.put("type_sub", "DHHD");
			//执行数据插入操作
			synchCampaignInfoService.insertActivityAssociateTable(activityAssociateMap);
			
			for (Map<String, Object> detailMap : detailList) {
				Map<String,Object> activityTableMap = new HashMap<String, Object>();
				activityTableMap.putAll(commonMap);
				//活动编码
				activityTableMap.put("ActivityCode", mainDataMap.get("ActivityCode"));
				//活动名称
				activityTableMap.put("ActivityName", mainDataMap.get("ActivityName"));
				//活动开始时间
				activityTableMap.put("StartTime", mainDataMap.get("StartTime"));
				//活动结束时间
				activityTableMap.put("EndTime", mainDataMap.get("EndTime"));
				//活动相关的促销品条码
				activityTableMap.put("Barcode", detailMap.get("Barcode"));
				//活动相关的厂商编码
				activityTableMap.put("Unitcode", detailMap.get("Unitcode"));
				//活动促销价格
				activityTableMap.put("Price", detailMap.get("Price"));
				//活动产品数量
				activityTableMap.put("ActivityQty", detailMap.get("ActivityQty"));
				//活动柜台
				activityTableMap.put("ActCounter", "ALL");
				//活动状态
				activityTableMap.put("Status", "0");
				//活动进入接口时间
				activityTableMap.put("Puttime", synchCampaignInfoService.getSYSDate());
				//数据来源标识
				activityTableMap.put("CHYFlag", "1");
				synchCampaignInfoService.insertActivityTable(activityTableMap);
			}
			//老后台数据源提交
			synchCampaignInfoService.ifManualCommit();
			//更改活动单据状态为已下发
			paramMap.put("issued", "1");
			synchCampaignInfoService.updateIssued(paramMap);
			//新后台数据源提交
			synchCampaignInfoService.manualCommit();
			//执行促销品实时下发接口,
			Map<String,Object> comMap = setInsertInfoMapKey(paramMap);
			paramMap.putAll(comMap);
			// 操作者 暂定
			paramMap.put("EmployeeId", "");
			// 是否发送MQ
			paramMap.put("sendMQFlag", true);
			paramMap.put("brandCode", paramMap.get("BrandCode"));
			binbessprm07_BL.tran_batchPromPrt(paramMap);
			//执行发送MQ操作,并通知终端获取数据
			mainDataMap.putAll(paramMap);
			sendMqNotice(mainDataMap);
		} catch (Exception e) {
			try {
				//老后台数据源回滚
				synchCampaignInfoService.ifManualRollback();
				//新后台数据源回滚
				synchCampaignInfoService.manualRollback();
			} catch (Exception ex) {
				logger.error(ex.getMessage(),ex);
			}
			logger.error(e.getMessage(),e);
		}
	} 
	/**
	 * 新增促销品信息，并返回所属促销品厂商ID
	 * @param mainDataMap
	 * @param detailDataList
	 * @returns
	 * @throws Exception
	 */
	public int savePrmBackId(Map<String, Object> mainDataMap, Map<String, Object> detailDataMap) throws Exception {
		try {
			//设置共通参数
			Map<String, Object> commonMap = setInsertInfoMapKey(detailDataMap);
			Map<String, Object> promotionMap = new HashMap<String, Object>();
			promotionMap.putAll(commonMap);
			promotionMap.put("unitCode", detailDataMap.get("Unitcode"));// 促销厂商编码
			promotionMap.put("nameTotal", mainDataMap.get("SubjectName"));// 促销品全称
			promotionMap.put("isBOMCompatible", "0");// 可否作为BOM的组成
			promotionMap.put("isReplenish", "0");// 可否补货
			promotionMap.put("status", "E");// 促销品状态
			promotionMap.put("mode", "DHCP");// 促销品类型
			promotionMap.put("isExchanged", "1");// 可否用于积分兑换 0:不可，1：可以
			promotionMap.put("promotionCateCD", "DHCP");// 促销产品类别
			promotionMap.put("isStock", "0");// 是否需要管理库存
			promotionMap.put("point", detailDataMap.get("Point"));//兑换所需积分
			// 取得促销品当前表版本号
			Map<String, Object> seqMap = new HashMap<String, Object>();
			seqMap.putAll(commonMap);
			seqMap.put("type", "H");
			String tVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
			promotionMap.put("tVersion", tVersion);
			promotionMap.put("isPosIss", "1");// 是否下发：0：不下发，1：下发
			// 新增促销品基本信息
			int prmId = synchCampaignInfoService.insertPromotionProductBackId(promotionMap);

			Map<String, Object> promotionVendorMap = new HashMap<String, Object>();
			promotionVendorMap.putAll(commonMap);
			promotionVendorMap.put("promProductId", prmId); // 促销品ID
			promotionVendorMap.put("manuFactId", 1); // 生产厂商ID
			promotionVendorMap.put("barCode", detailDataMap.get("Barcode"));// 产品条码
			// 新增促销品厂商基本信息
			int prmVendorID = synchCampaignInfoService.insertPromProductVendor(promotionVendorMap);
			//新后台数据源提交
			synchCampaignInfoService.manualCommit();
			return prmVendorID;
		} catch (Exception e) {
			try {
				//新后台数据源回滚
				synchCampaignInfoService.manualRollback();
			} catch (Exception ex) {
				logger.error(e.getMessage(),e);
			}
			logger.error(e.getMessage(),e);
		}
		return 0;
	}
	/**
	 * 组装产品下发通知的MQ消息
	 * @param map
	 * @param subType 子类型：PRT、DPRT	 必填，用于区分该消息体发送的是产品信息还是柜台产品信息
	 */
	public void sendMqNotice(Map<String,Object> map){
		logger.info("*********向终端发送导入通知*********");
		Map<String, Object> mainData = new HashMap<String, Object>();
		//品牌代码
		mainData.put("BrandCode",map.get("BrandCode"));
		//业务类型
		mainData.put("TradeType", "ACT");
		//子类型 暂定
		mainData.put("SubType", "PRM");
		//主题活动码
		mainData.put("SubjectCode", ConvertUtil.getString(map.get("SubjectCode")));
		//子活动码
		mainData.put("MainCode", ConvertUtil.getString(map.get("MainCode")));
		//操作者 暂定
		mainData.put("EmployeeId", ConvertUtil.getString(map.get("EmployeeId")));
		//本次消息发送的时间
		mainData.put("Time", DateUtil.date2String(new Date(),DateUtil.DATETIME_PATTERN));
		//申明要返回的map
		Map<String,Object> mqMap = new HashMap<String,Object>();
		//组装消息体版本	Version
		mqMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "AMQ.015.001");
		//组装消息体数据类型	DataType
		mqMap.put(MessageConstants.MESSAGE_DATATYPE_TITLE, MessageConstants.DATATYPE_APPLICATION_JSON);
		Map<String,Object> dataLine = new HashMap<String,Object>();
		//将主数据放入dataLine中
		dataLine.put(MessageConstants.MAINDATA_MESSAGE_SIGN, mainData);
		mqMap.put(MessageConstants.DATALINE_JSON_XML, dataLine);
		//设定MQInfoDTO
		MQInfoDTO mqDTO = setMQInfoDTO(mqMap,map);
		//调用共通发送MQ消息
		mqDTO.setMsgQueueName("cherryToPosCMD");
		try {
			binOLMQCOM01_BL.sendMQMsg(mqDTO,false);
		} catch (Exception e) {
			//发送失败记录下
			logger.error(e.getMessage(),e);
		}
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
	 * 共通参数
	 * @param map
	 */
	private Map<String, Object> setInsertInfoMapKey(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.ORGANIZATIONINFOID,map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryBatchConstants.BRANDINFOID,map.get("BIN_BrandInfoID"));
		// 作成者
		paramMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		paramMap.put(CherryBatchConstants.CREATEPGM, "SynchCampaignInfoLogic");
		// 更新者
		paramMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		paramMap.put(CherryBatchConstants.UPDATEPGM, "SynchCampaignInfoLogic");
		return paramMap;
	}
	/**
	 * 查询操作员的信息 暂时不使用此方法
	 * @param map
	 * @return
	 */
	private String getEmployeeId(Map<String,Object> map){
		 //查询员工ID
        map.put("EmployeeCode", "G00001");
        List<Map<String,Object>> employeeList = synchCampaignInfoService.getEmployeeInfo(map);
        String employeeID = "";
        if(null != employeeList && employeeList.size()>0){
            employeeID = ConvertUtil.getString(employeeList.get(0).get("BIN_EmployeeID"));
        }
        return ConvertUtil.getString(employeeID);
	}
}
