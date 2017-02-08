package com.cherry.webservice.member.bl;

import com.cherry.bs.emp.bl.BINOLBSEMP02_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.customize.member.MemberPassword;
import com.cherry.mb.mbm.bl.BINOLMBMBM02_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM03_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM06_BL;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.webservice.member.interfaces.MemberInfo_IF;
import com.cherry.webservice.member.service.MemberInfoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemberInfoLogic implements MemberInfo_IF {

	private static Logger logger = LoggerFactory.getLogger(MemberInfoLogic.class.getName());

	@Resource(name = "binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;

	/**
	 * 获取对应员工的信息
	 */
	@Resource(name="binOLBSEMP02_BL")
	private BINOLBSEMP02_BL binolbsemp02BL;

	/** 取得各种业务类型的单据流水号 */
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name = "memberInfoService")
	private MemberInfoService memberInfoService;

	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;

	/** 会员资料修改画面BL **/
	@Resource
	private BINOLMBMBM06_BL binOLMBMBM06_BL;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 会员属性调整BL */
	@Resource
	private BINOLMBMBM03_BL binOLMBMBM03_BL;

	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;

	/** 会员详细画面BL */
	@Resource
	private BINOLMBMBM02_BL binOLMBMBM02_BL;

	@Resource(name="CodeTable")
	private CodeTable codeTable;

	@SuppressWarnings("unchecked")
	@Override
	public Map getMemberInfo(Map paramMap) {

		// 查询会员基本信息
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(CherryChecker.isNullOrEmpty(paramMap.get("ConditionType"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ConditionType异常。ConditionType="+String.valueOf(paramMap.get("ConditionType")));
			return retMap;
		}
		String conditionType = String.valueOf(paramMap.get("ConditionType"));
		if(!conditionType.equals("MemberID")&& !conditionType.equals("MemberCode")&& !conditionType.equals("MessageID")&& !conditionType.equals("MobilePhone")&& !conditionType.equals("Email")){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ConditionType异常。ConditionType="+conditionType);
			return retMap;
		}

		if(CherryChecker.isNullOrEmpty(paramMap.get("Condition"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Condition异常。Condition为空。");
			return retMap;
		}
		String brandCode = String.valueOf(paramMap.get("BrandCode"));
		//如果查询条件是手机号，则进行加密处理后再查询（目前仅个别品牌手机号是加密的）
		if(conditionType.equals("MobilePhone")){
			try {
				paramMap.put("Condition",CherrySecret.encryptData(brandCode,String.valueOf(paramMap.get("Condition"))));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				retMap.put("ERRORCODE", "WSE9999");
				retMap.put("ERRORMSG", "处理过程中发生未知异常");
				return retMap;
			}
		}


		if("MessageID".equals(conditionType)){
			//若微信ID做为查询条件，先查询绑定关系表中是否有记录，若有则转换为会员ID查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
			params.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
			params.put("WechatID", paramMap.get("Condition"));
			params.put("ValidFlag", "1");
			List<Map<String, Object>> relation = memberInfoService.getMemBindRelation(params);
			if(null != relation && relation.size() > 0){
				paramMap.put("ConditionType", "MemberID");
				paramMap.put("Condition", relation.get(0).get("MemberInfoID"));
			}
		}

		List<Map<String, Object>> memList = memberInfoService.getMemberInfo(paramMap);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "未查询到符合条件的会员。");
		} else {
			Map<String, Object> tmp;
			try {
				for (int i = 0; i < memList.size(); i++) {
					tmp = memList.get(i);
					tmp.put("MobilePhone", CherrySecret.decryptData(brandCode, (String)tmp.get("MobilePhone")));
					tmp.put("Email", CherrySecret.decryptData(brandCode, (String)tmp.get("Email")));

					Object isDimensionCode = paramMap.get("IsDimensionCode");
					if(isDimensionCode != null && "2".equals(String.valueOf(isDimensionCode))) {
						Map<String,Object> returnMap = generateDimensionCode((String)tmp.get("MemberCode"));
						tmp.putAll(returnMap);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("ERRORCODE", "WSE9999");
				retMap.put("ERRORMSG", "处理过程中发生未知异常");
				return retMap;
			}
			retMap.put("ResultContent", memList);
		}
		return retMap;
	}

	/**
	 * 生成二维码
	 */
	private Map<String,Object>  generateDimensionCode(String memCode) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int memCodeLength = memCode.length();
		String strMemCodeLength = memCodeLength < 10 ? "0" + memCodeLength : String.valueOf(memCodeLength);
		String strCurrentTimeSeconds = String.valueOf(System.currentTimeMillis()/1000);
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		String strCurrentTime = sf.format(new Date());

		StringBuffer oneDimensionCode = new StringBuffer();
		String encrptData = CherryUtil.generateOneDimesionEncrptData(memCode,strCurrentTime);
		String compressTimestamp = CherryUtil.compressOneDimesion(strCurrentTime);
		String compressMemCodeLength = CherryUtil.compressOneDimesion(strMemCodeLength);
		oneDimensionCode.append(encrptData).append(compressMemCodeLength).append(memCode).append(compressTimestamp);
		logger.info("oneDimensionCode: " + oneDimensionCode.toString() + " memCode：" + memCode + " currentTimeSeconds: " + strCurrentTime );

		StringBuffer twoDimensionCode  = new StringBuffer();
		twoDimensionCode.append("{\"C\":\"").append(memCode).append("\",\"T\":\"").append(strCurrentTimeSeconds).append("\"}");
		DESPlusNew desPlus = new DESPlusNew();
		String encryptTwoDimensionCode = desPlus.encrypt(twoDimensionCode.toString());

		returnMap.put("OneDimensionCode",oneDimensionCode.toString());
		returnMap.put("TwoDimensionCode",encryptTwoDimensionCode);
		return returnMap;

	}

	@Override
	public Map getMemberPoint(Map paramMap) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MemberID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberID异常。MemberID="+String.valueOf(paramMap.get("MemberID")));
			return retMap;
		}

		retMap.put("ResultMap", memberInfoService.getMemberPoint(paramMap));
		return retMap;
	}

	/*
	 * (non-Javadoc) 检查微信ID是否已经对应着一个会员，如果未对应或对应多个，则报错
	 * 
	 * @see
	 * com.cherry.webservice.member.interfaces.MemberInfo_IF#authWeixinID(java
	 * .util.Map)
	 */
	@Override
	public Map checkWechatID(Map paramMap) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MessageID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
		List memList = memberInfoService.getMemberInfoByMessageID(paramMap);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0009");
			retMap.put("ERRORMSG", "未查询到符合条件的会员。");
		} else if (memList.size() == 1) {
			retMap.put("ResultString", ((Map) memList.get(0)).get("BIN_MemberInfoID"));
		} else {
			retMap.put("ERRORCODE", "WSE0010");
			retMap.put("ERRORMSG", "查询到多个会员。");
		}
		return retMap;
	}

	@Override
	public Map authenticateWechatRequest(Map paramMap) throws Exception {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MessageID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
//		if (CherryChecker.isNullOrEmpty(paramMap.get("MemberCode"))) {
//			retMap.put("ERRORCODE", "WSE9993");
//			retMap.put("ERRORMSG", "参数MemberCode是必须的");
//			return retMap;
//		}
		if (CherryChecker.isNullOrEmpty(paramMap.get("MobilePhone"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		}

		String isNotIncMemReg = ConvertUtil.getString(paramMap.get("IsNotIncMemReg")); // ""表示包含假登录会员；1-不包含假登录会员

		//检索会员信息
		int memID = 0;
		List memList = memberInfoService.getMemberInfoOAuth(paramMap);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0009");
			retMap.put("ERRORMSG", "未查询到符合条件的会员。");
			return retMap;
		} else if (memList.size() == 1) {
			Map tmp = (Map) memList.get(0);

			// 增加“是否包含假登录会员”标识，若不包含假登录会员，则判断当前会员是否为假登录会员
			int memInfoRegFlg = ConvertUtil.getInt(tmp.get("MemInfoRegFlg")); // "是否为假登录"标识. 0:会员信息登记齐  1：会员情报缺失会员（假登记会员）
			if("1".equals(isNotIncMemReg) && (memInfoRegFlg == 1) ) {
				retMap.put("ERRORCODE", "WSE0117");
				retMap.put("ERRORMSG", "该会员为假登录会员。");
				return retMap;
			}

			Map ret = new HashMap<String,Object>();
			ret.put("MemberID", tmp.get("BIN_MemberInfoID"));
			ret.put("MemberPassword", tmp.get("MemberPassword"));
			ret.put("CouponCode", tmp.get("CouponCode"));
			memID = Integer.parseInt(tmp.get("BIN_MemberInfoID").toString());
			retMap.put("ResultMap", ret);

		} else {

			if("1".equals(isNotIncMemReg)) {
				Map<String,Object> memInfo = null ;
				int memRegedCount = 0 ;
				for (Object memInfoObj : memList) { // 获取正式会员的数量
					Map<String,Object> temp = (Map<String,Object>)memInfoObj;
					int memInfoRegFlg = ConvertUtil.getInt(temp.get("MemInfoRegFlg"));
					if(memInfoRegFlg == 0) {
						memRegedCount ++ ;
						memInfo = temp ;
					}
				}
				if(memRegedCount == 1) { // 若正式会员数量为1，返回该会员
					Map ret = new HashMap<String,Object>();
					ret.put("MemberID", memInfo.get("BIN_MemberInfoID"));
					ret.put("MemberPassword", memInfo.get("MemberPassword"));
					ret.put("CouponCode", memInfo.get("CouponCode"));
					memID = Integer.parseInt(memInfo.get("BIN_MemberInfoID").toString());
					retMap.put("ResultMap", ret);
				} else {
					retMap.put("ERRORCODE", "WSE0010");
					retMap.put("ERRORMSG", "查询到多个会员。");
					return retMap;
				}
			} else {
				retMap.put("ERRORCODE", "WSE0010");
				retMap.put("ERRORMSG", "查询到多个会员。");
				return retMap;
			}

		}


		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(paramMap.get("BrandCode").toString());

		String billType = CherryConstants.MESSAGE_TYPE_ES;

		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"))),
				Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"))), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);

		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", paramMap.get("BrandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 事件类型
		mainData.put("EventType", "11");
		// 事件ID
		mainData.put("EventId", memID+"&"+paramMap.get("MobilePhone").toString());
		// 沟通时间
		mainData.put("EventDate", memberInfoService.getSYSDate());
		// 数据来源
		mainData.put("Sourse", "Mulberry");

		dataLine.put("MainData", mainData);

		msgDataMap.put("DataLine", dataLine);

		mqInfoDTO.setMsgDataMap(msgDataMap);
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
		return retMap;
	}

	@Override
	public Map tran_BindWechat(Map paramMap) throws Exception {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MessageID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
//		if (CherryChecker.isNullOrEmpty(paramMap.get("MemberCode"))) {
//			retMap.put("ERRORCODE", "WSE9993");
//			retMap.put("ERRORMSG", "参数MemberCode是必须的");
//			return retMap;
//		}
		if (CherryChecker.isNullOrEmpty(paramMap.get("MobilePhone"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		}
		if (CherryChecker.isNullOrEmpty(paramMap.get("CouponCode"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数CouponCode是必须的");
			return retMap;
		}
		String bindTime = ConvertUtil.getString(paramMap.get("BindTime"));
		if (CherryChecker.isNullOrEmpty(bindTime)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime是必须的");
			return retMap;
		} else if(!CherryChecker.checkDate(bindTime, "yyyy-MM-dd HH:mm:ss")){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime的格式必须为yyyy-MM-dd HH:mm:ss");
			return retMap;
		}
		//验证coupon
		String messageID = paramMap.get("MessageID").toString();
		String mobilePhone = paramMap.get("MobilePhone").toString();
		String couponCode = paramMap.get("CouponCode").toString();
//		String memberCode = paramMap.get("MemberCode").toString();
		String memberCode = "";
		String memberID="";
		String sysTime = memberInfoService.getSYSDate();
		Map<String,Object> tmpMap = new HashMap<String,Object>();
		tmpMap.put("MobilePhone", mobilePhone);
		tmpMap.put("CouponCode", couponCode);
		tmpMap.put("CampaignCode", "GETVERIFICATIONCOUPON");
		List<Map<String,Object>> retList =memberInfoService.oAuthCoupon(tmpMap);
		if(retList!=null && retList.size()==1){
			String seconds = String.valueOf(retList.get(0).get("Seconds"));
			if(seconds.indexOf("-")<0){
				//验证码已过期
				retMap.put("ERRORCODE", "WSE0016");
				retMap.put("ERRORMSG", "验证码已过期");
				return retMap;
			}

			//验证会员是否已绑定微信（不允许绑定多个微信）
			tmpMap.clear();
			tmpMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
			tmpMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
			tmpMap.put("ConditionType", "MobilePhone");
			String brandCode = String.valueOf(paramMap.get("BrandCode"));
			//如果查询条件是手机号，则进行加密处理后再查询（目前仅个别品牌手机号是加密的）
			try {
				tmpMap.put("Condition",CherrySecret.encryptData(brandCode,mobilePhone));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				retMap.put("ERRORCODE", "WSE9999");
				retMap.put("ERRORMSG", "处理过程中发生未知异常");
				return retMap;
			}
			List<Map<String,Object>> memList = memberInfoService.getMemberInfo(tmpMap);//根据卡号查询
			String isNotIncMemReg = ConvertUtil.getString(paramMap.get("IsNotIncMemReg")); // ""表示包含假登录会员；1-不包含假登录会员
			Map<String,Object> memInfo = null ;
			if(memList == null || memList.isEmpty()) {
				retMap.put("ERRORCODE", "WSE0001");
				retMap.put("ERRORMSG", "未找到指定的会员");
				return retMap;
			} else if(memList.size() > 1) {
				if("1".equals(isNotIncMemReg)) {
					int memRegedCount = 0 ;
					for (Object memInfoObj : memList) { // 获取正式会员的数量
						Map<String, Object> temp = (Map<String, Object>) memInfoObj;
						int memInfoRegFlg = ConvertUtil.getInt(temp.get("MemInfoRegFlg"));
						if (memInfoRegFlg == 0) {
							memRegedCount ++ ;
							memInfo = temp;
						}
					}
					if(memRegedCount == 1) { // 若仅仅包含一条真会员记录，则绑定该会员
						String _messageID = ConvertUtil.getString(memInfo.get("MessageID"));
						if(!CherryChecker.isNullOrEmpty(_messageID) && !_messageID.equals(messageID)) {
							retMap.put("ERRORCODE", "WSE0030");
							retMap.put("ERRORMSG", "该会员已绑定其他微信");
							return retMap;
						}
					} else { // 否则返回错误
						retMap.put("ERRORCODE", "WSE0010");
						retMap.put("ERRORMSG", "查询到多个会员");
						return retMap;
					}
				} else { // 老逻辑，查询到多条记录直接返回错误
					retMap.put("ERRORCODE", "WSE0010");
					retMap.put("ERRORMSG", "查询到多个会员");
					return retMap;
				}
			} else {
				memInfo = memList.get(0);
				// 增加“是否包含假登录会员”标识，若不包含假登录会员，则判断当前会员是否为假登录会员
				int memInfoRegFlg = ConvertUtil.getInt(memInfo.get("MemInfoRegFlg")); // "是否为假登录"标识. 0:会员信息登记齐  1：会员情报缺失会员（假登记会员）
				if("1".equals(isNotIncMemReg) && (memInfoRegFlg == 1) ) { // 若为新逻辑判断该会员是否为假登录会员，若是返回错误
					retMap.put("ERRORCODE", "WSE0117");
					retMap.put("ERRORMSG", "该会员为假登录会员。");
					return retMap;
				}

				String _messageID = ConvertUtil.getString(memInfo.get("MessageID"));
				if(!CherryChecker.isNullOrEmpty(_messageID) && !_messageID.equals(messageID)) {
					retMap.put("ERRORCODE", "WSE0030");
					retMap.put("ERRORMSG", "该会员已绑定其他微信");
					return retMap;
				}
			}
			memberID=String.valueOf(memInfo.get("MemberID"));
			memberCode = String.valueOf(memInfo.get("MemberCode"));

			//验证码检测通过
			tmpMap.clear();
			tmpMap.put("MessageID", messageID);
			tmpMap.put("BIN_MemberInfoID", memberID);
			tmpMap.put("MemberPassword", paramMap.get("MemberPassword"));
			tmpMap.put("BindTime", bindTime);
			int cnt = memberInfoService.updateMessageId(tmpMap);
			if (cnt != 1) {
				throw new Exception();
			}

			// 添加会员微信绑定履历
			Map<String, Object> memBindRecordMap = new HashMap<String, Object>();
			memBindRecordMap.put("organizationInfoId", paramMap.get("BIN_OrganizationInfoID"));
			memBindRecordMap.put("brandInfoId", paramMap.get("BIN_BrandInfoID"));
			memBindRecordMap.put("memberInfoId", memberID);
			memBindRecordMap.put("wechatSeviceCode", paramMap.get("OriginalWechatCode"));
			memBindRecordMap.put("wechatId", paramMap.get("MessageID"));
			memBindRecordMap.put("recordType", "1");
			memBindRecordMap.put("recordDateTime", bindTime);
			memBindRecordMap.put("createdBy", "WeChat");
			memBindRecordMap.put("createPGM", "MemberInfoLogic");
			memBindRecordMap.put("updatedBy", "WeChat");
			memBindRecordMap.put("updatePGM", "MemberInfoLogic");
			memberInfoService.addMemBindRecord(memBindRecordMap);

			// TODO:会员资料修改履历,会员激活，同步激活状态到第三方
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			mqInfoDTO.setBrandCode(paramMap.get("BrandCode").toString());

			String billType = CherryConstants.MESSAGE_TYPE_MA;

			String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"))),
					Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"))), "", billType);
			// 业务类型
			mqInfoDTO.setBillType(billType);
			// 单据号
			mqInfoDTO.setBillCode(billCode);
			// 所属品牌
			mqInfoDTO.setBrandInfoId(Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"))));
			// 所属组织
			mqInfoDTO.setOrganizationInfoId(Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"))));
			// 消息发送队列名
			mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);

			// 设定消息内容
			Map<String,Object> msgDataMap = new HashMap<String,Object>();
			// 设定消息版本号
			msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MA);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			// 设定消息的数据行
			Map<String,Object> dataLine = new HashMap<String,Object>();
			// 消息的主数据行
			Map<String,Object> mainData = new HashMap<String,Object>();
			// 品牌代码
			mainData.put("BrandCode", paramMap.get("BrandCode"));
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 会员卡号
			mainData.put("MemberCode", memberCode);
			// 激活时间
			mainData.put("ActiveDate", bindTime);
			// 激活途径
			mainData.put("ActiveChannel", CherryConstants.ACTIVECHANNEL);
			//会员密码
			mainData.put("MemberPassword", ConvertUtil.getString(paramMap.get("MemberPassword")));
			dataLine.put("MainData", mainData);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 设定插入到MongoDB的信息
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", paramMap.get("OrganizationCode"));
			// 品牌代码
			dbObject.put("BrandCode", paramMap.get("BrandCode"));
			// 业务类型
			dbObject.put("TradeType", billType);
			// 单据号
			dbObject.put("TradeNoIF", billCode);
			// 系统时间
			dbObject.put("OccurTime", sysTime);
			// 修改回数
			dbObject.put("ModifyCounts", "0");
			// 数据来源
			dbObject.put("Source", CherryConstants.ACTIVECHANNEL);
			mqInfoDTO.setDbObject(dbObject);
			// 发送MQ消息
			try {
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}

			//发送会员资料MQ消息
			try {
				this.sendMemberMQ(paramMap);
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}

			try {
				this.sendPtMsg(paramMap);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}

			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("MemberID", memberID);
			infoMap.put("MemberCode", memberCode);
			retMap.put("ResultMap", infoMap);
		}else{
			retMap.put("ERRORCODE", "WSE0009");
			retMap.put("ERRORMSG", "验证码错误");
		}
		return retMap;
	}

	public void sendPtMsg(Map paramMap) throws Exception {
		String rewardPointsJson = ConvertUtil.getString(paramMap.get("RewardPoints"));
		if(!CherryChecker.isNullOrEmpty(rewardPointsJson, true)){//若存在奖励积分的参数，则需积分维护
			Map<String, Object> rewardPoints = ConvertUtil.json2Map(rewardPointsJson);
			List<Map<String,Object>> retList =memberInfoService.getMemberInfoOAuth(paramMap);
			if(retList == null || retList.isEmpty()) {
				logger.error("会员信息不存在，奖励积分维护失败");
				return;
			}
			int point = ConvertUtil.getInt(rewardPoints.get(retList.get(0).get("LevelCode")));
			if(point > 0){
				//当同一个会员在同一个公众号中已经绑定过，则再次绑定不赠送积分
				Map<String, Object> recordMap = new HashMap<String, Object>();
				recordMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
				recordMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
				recordMap.put("WechatSeviceCode", paramMap.get("OriginalWechatCode"));
				recordMap.put("BIN_MemberInfoID", retList.get(0).get("BIN_MemberInfoID"));
				List<Map<String, Object>> recordList = memberInfoService.getMemBindRecord(recordMap);
				if(null != recordList && recordList.size() > 1 ){
					logger.error("重复绑定，不赠送积分");
					return;
				}
				//发送积分维护的信息
				//MQ需要用到的数据
				//会员信息
				String memberCode = ConvertUtil.getString(retList.get(0).get("MemCode"));
				String modifyPoint = ConvertUtil.getString(point);
				int brandId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID")));
				int orgId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID")));
				String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
				String orgCode = ConvertUtil.getString(paramMap.get("OrganizationCode"));
				String billType = CherryConstants.MESSAGE_TYPE_PT;
				String billCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", billType);
				String ticketDateStr = ConvertUtil.getString(paramMap.get("BindTime"));
				if(ticketDateStr == null) {
					ticketDateStr = memberInfoService.getSYSDateTime();
				}
				//设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 所属品牌
				mqInfoDTO.setBrandInfoId(brandId);
				mqInfoDTO.setBrandCode(brandCode);
				// 所属组织
				mqInfoDTO.setOrganizationInfoId(orgId);
				mqInfoDTO.setOrgCode(orgCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
				// 设定消息命令类型
				msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定插入到MongoDB的信息
				DBObject dbObject = new BasicDBObject();
				// 组织代码
				dbObject.put("OrgCode", orgCode);
				// 品牌代码
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", billType);
				// 单据号
				dbObject.put("TradeNoIF", billCode);
				// 数据来源
				dbObject.put("Sourse", "Wecaht");
				mqInfoDTO.setDbObject(dbObject);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 业务类型
				mainData.put("TradeType", billType);
				//修改模式
				mainData.put("SubTradeType","2");
				//积分类型
				mainData.put("MaintainType","8"); //code 1214绑定送积分
				// 单据号
				mainData.put("TradeNoIF", billCode);
				// 数据来源
				mainData.put("Sourse", "Wechat");
				//积分维护明细数据
				List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
				Map<String,Object> detailMap = new HashMap<String,Object>();
				//会员卡号
				detailMap.put("MemberCode", memberCode);
				//修改的积分
				detailMap.put("ModifyPoint", modifyPoint);
				//理由
				detailMap.put("Reason", "");
				//业务时间
				detailMap.put("BusinessTime", ticketDateStr);
				detailDataList.add(detailMap);
				dataLine.put("MainData", mainData);
				dataLine.put("DetailDataDTOList", detailDataList);
				msgDataMap.put("DataLine", dataLine);
				mqInfoDTO.setMsgDataMap(msgDataMap);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}
		}
	}

	//发送会员资料MQ消息
	public void sendMemberMQ(Map paramMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> retList = memberInfoService.getMemberInfoOAuth(paramMap);
		if(null == retList || retList.isEmpty()) {
			logger.error("会员信息不存在，发送会员资料失败！");
			return;
		}
		//会员信息
		Map<String, Object> memMap = new HashMap<String, Object>();
		String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
		String orgCode = ConvertUtil.getString(paramMap.get("OrganizationCode"));
		int organizationInfoId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID")));
		int brandInfoId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID")));
		String subType = "1";//子类型，修改
		String memberInfoId = ConvertUtil.getString(retList.get(0).get("BIN_MemberInfoID"));
		String memCode = ConvertUtil.getString(retList.get(0).get("MemCode"));
		//查询会员信息
		memMap.put("organizationInfoId", organizationInfoId);
		memMap.put("brandInfoId", brandInfoId);
		memMap.put("memCode", memCode);
		memMap.put("memberInfoId", memberInfoId);
		Map<String, Object> memInfo = binOLMBMBM02_BL.getMemberInfo(memMap);
		if(!CherryChecker.isNullOrEmpty(memInfo, true)) {
			String memName = ConvertUtil.getString(memInfo.get("name"));
			String telephone = ConvertUtil.getString(memInfo.get("telephone"));
			String mobilePhone = ConvertUtil.getString(memInfo.get("mobilePhone"));
			String gender = ConvertUtil.getString(memInfo.get("gender"));
			//地址信息
			Map<String, Object> memAddressInfo = (Map<String, Object>) memInfo.get("memberAddressInfo");
			if(!CherryChecker.isNullOrEmpty(memAddressInfo, true)) {
				String provinceId = ConvertUtil.getString(memAddressInfo.get("provinceId"));
				String provinceName = ConvertUtil.getString(memAddressInfo.get("provinceName"));
				String cityId = ConvertUtil.getString(memAddressInfo.get("cityId"));
				String cityName = ConvertUtil.getString(memAddressInfo.get("cityName"));
				String countyId = ConvertUtil.getString(memAddressInfo.get("countyId"));
				String countyName = ConvertUtil.getString(memAddressInfo.get("countyName"));
				String address = ConvertUtil.getString(memAddressInfo.get("addressLine1"));
				String postcode = ConvertUtil.getString(memAddressInfo.get("zipCode"));

				//地址信息
				map.put("provinceId", provinceId);
				map.put("provinceName", provinceName);
				map.put("cityId", cityId);
				map.put("cityName", cityName);
				map.put("countyId", countyId);
				map.put("countyName", countyName);
				map.put("address", address);
				map.put("postcode", postcode);
			}
			String birth = ConvertUtil.getString(memInfo.get("birth"));
			String birthYear = ConvertUtil.getString(memInfo.get("birthYear"));
			String birthDay = ConvertUtil.getString(memInfo.get("birthDay"));
			String email = ConvertUtil.getString(memInfo.get("email"));

			String joinDate = ConvertUtil.getString(memInfo.get("joinDate"));
			String employeeCode = ConvertUtil.getString(memInfo.get("employeeCode"));//发卡BA
			String organizationCode = ConvertUtil.getString(memInfo.get("counterCode"));//发卡柜台
			String referrer = ConvertUtil.getString(memInfo.get("referrer"));

			String isReceiveMsg = ConvertUtil.getString(memInfo.get("isReceiveMsg"));
			String testType = ConvertUtil.getString(memInfo.get("testType"));
			String version = ConvertUtil.getString(memInfo.get("version"));
			String memo1 = ConvertUtil.getString(memInfo.get("memo1"));
			String memberPassword = ConvertUtil.getString(memInfo.get("memberPassword"));

			String active = ConvertUtil.getString(memInfo.get("active"));
			String activeDate = ConvertUtil.getString(memInfo.get("activeDate"));
			String activeChannel = ConvertUtil.getString(memInfo.get("activeChannel"));
			String messageId = ConvertUtil.getString(memInfo.get("messageId"));
			String wechatBindTime = ConvertUtil.getString(memInfo.get("wechatBindTime"));
			String memInfoRegFlg = ConvertUtil.getString(memInfo.get("memInfoRegFlg"));

			map.put("brandCode", brandCode);
			map.put("orgCode", orgCode);
			map.put("organizationInfoId", organizationInfoId);
			map.put("brandInfoId", brandInfoId);
			map.put("subType", subType);

			map.put("memCode", memCode);
			map.put("memName", memName);
			map.put("telephone", telephone);
			map.put("mobilePhone", mobilePhone);
			map.put("gender", gender);

			map.put("birth", birth);
			map.put("birthYear", birthYear);
			map.put("birthDay", birthDay);
			map.put("email", email);

			map.put("joinDate", joinDate);//开卡时间
			map.put("employeeCode", employeeCode);//发卡BA
			map.put("organizationCode", organizationCode);//发卡柜台
			map.put("referrer", referrer);//推荐会员卡号

			map.put("isReceiveMsg", isReceiveMsg);
			map.put("testType", testType);
			map.put("version", version);
			map.put("memo1", memo1);
			map.put("memberPassword", memberPassword);

			map.put("active", active);
			map.put("activeDate", activeDate);
			map.put("activeChannel", activeChannel);

			map.put("messageId", messageId);
			map.put("wechatBindTime", wechatBindTime);
			map.put("memInfoRegFlg", memInfoRegFlg);

			binOLMBMBM11_BL.sendMemberMQ(map);
		}
	}

	@Override
	public Map tran_BindWechatNoAuth(Map paramMap) throws Exception{
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MessageID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
		String bindTime = ConvertUtil.getString(paramMap.get("BindTime"));
		if (CherryChecker.isNullOrEmpty(bindTime)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime是必须的");
			return retMap;
		} else if(!CherryChecker.checkDate(bindTime, "yyyy-MM-dd HH:mm:ss")){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime的格式必须为yyyy-MM-dd HH:mm:ss");
			return retMap;
		}
		int count = 0;
		if(CherryChecker.isNullOrEmpty(paramMap.get("MemberName"))){
			count++;
		}
		if(CherryChecker.isNullOrEmpty(paramMap.get("MemberCode"))){
			count++;
		}
		if(CherryChecker.isNullOrEmpty(paramMap.get("MobilePhone"))){
			count++;
		} else {
			String brandCode = String.valueOf(paramMap.get("BrandCode"));
			// 如果条件是手机号，则进行加密处理后再查询（目前仅个别品牌手机号是加密的）
			try {
				paramMap.put("MobilePhone", CherrySecret.encryptData(brandCode, String.valueOf(paramMap.get("MobilePhone"))));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("ERRORCODE", "WSE9999");
				retMap.put("ERRORMSG", "处理过程中发生未知异常");
				return retMap;
			}
		}

		if(count == 3){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数错误，MemberName、MemberCode、MobilePhone至少需要一个！");
			return retMap;
		}
		String messageID = paramMap.get("MessageID").toString();
		List<Map<String,Object>> retList =memberInfoService.getMemberInfoOAuth(paramMap);
		if(retList.size()<1){
			retMap.put("ERRORCODE", "WSE9992");
			retMap.put("ERRORMSG", "未能匹配到指定的会员。");
			return retMap;
		}else if(retList.size()>1){
			retMap.put("ERRORCODE", "WSE9991");
			retMap.put("ERRORMSG", "共匹配到"+retList.size()+"个会员。");
			return retMap;
		}
		Map<String,Object> tmpMap = new HashMap<String,Object>();
		tmpMap.put("MessageID", messageID);
		tmpMap.put("BIN_MemberInfoID", retList.get(0).get("BIN_MemberInfoID"));
		tmpMap.put("BindTime", bindTime);
		memberInfoService.bindWechat(tmpMap);

		// 添加会员微信绑定履历
		Map<String, Object> memBindRecordMap = new HashMap<String, Object>();
		memBindRecordMap.put("organizationInfoId", paramMap.get("BIN_OrganizationInfoID"));
		memBindRecordMap.put("brandInfoId", paramMap.get("BIN_BrandInfoID"));
		memBindRecordMap.put("memberInfoId", tmpMap.get("BIN_MemberInfoID"));
		memBindRecordMap.put("recordType", "1");
		memBindRecordMap.put("recordDateTime", bindTime);
		memBindRecordMap.put("wechatSeviceCode", paramMap.get("OriginalWechatCode"));
		memBindRecordMap.put("wechatId", paramMap.get("MessageID"));
		memBindRecordMap.put("createdBy", "WeChat");
		memBindRecordMap.put("createPGM", "MemberInfoLogic");
		memBindRecordMap.put("updatedBy", "WeChat");
		memBindRecordMap.put("updatePGM", "MemberInfoLogic");
		memberInfoService.addMemBindRecord(memBindRecordMap);
		//能否获取绑定奖励积分标志（0：可以获取奖励，1：不能获取奖励）
		int rewardFlag = 0;
		// 添加会员绑定关系
		String originalWechatCode = ConvertUtil.getString(paramMap.get("OriginalWechatCode"));
		if(!CherryChecker.isNullOrEmpty(originalWechatCode, true)){
			Map<String, Object> memBindRelationMap = new HashMap<String, Object>();
			memBindRelationMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
			memBindRelationMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
			memBindRelationMap.put("WechatSeviceCode", paramMap.get("OriginalWechatCode"));
			memBindRelationMap.put("WechatID", paramMap.get("MessageID"));
			memBindRelationMap.put("createdBy", "WeChat");
			memBindRelationMap.put("createPGM", "MemberInfoLogic");
			memBindRelationMap.put("updatedBy", "WeChat");
			memBindRelationMap.put("updatePGM", "MemberInfoLogic");
			List<Map<String, Object>> mbrList = memberInfoService.getMemBindRelation(memBindRelationMap);
			memBindRelationMap.put("ValidFlag", "1");
			if(mbrList != null && mbrList.size() > 0){ //绑定过，更新
				String memberId = ConvertUtil.getString(tmpMap.get("BIN_MemberInfoID"));
				String rmemberId =  ConvertUtil.getString(mbrList.get(0).get("MemberInfoID"));
				if(memberId.equals(rmemberId)){
					rewardFlag = 1;
				}else{
					memBindRelationMap.put("BIN_MemberInfoID", memberId);
				}
				memberInfoService.updateMemBindRelation(memBindRelationMap);
			}else{ //未绑定过，新增
				memBindRelationMap.put("BIN_MemberInfoID", tmpMap.get("BIN_MemberInfoID"));
				memberInfoService.addMemBindRelation(memBindRelationMap);
			}
		}

		//发送会员资料MQ消息
		try {
			this.sendMemberMQ(paramMap);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}

		int point = 0;
		String rewardPointsJson = ConvertUtil.getString(paramMap.get("RewardPoints"));
		if(!CherryChecker.isNullOrEmpty(rewardPointsJson, true)){//若存在奖励积分的参数，则需积分维护
			Map<String, Object> rewardPoints = ConvertUtil.json2Map(rewardPointsJson);
			String initialDate =  ConvertUtil.getString(retList.get(0).get("InitialDate"));
			int newConfig =   ConvertUtil.getInt(rewardPoints.get("NEWCFG"));
			//initialDate 为空时，认为为新会员
			if(CherryChecker.isNullOrEmpty(initialDate, true) && newConfig == 1){
				//根据配置获取新会员的奖励积分
				point =  ConvertUtil.getInt(rewardPoints.get("NEW"));
			}else{
				//老会员根据配置获取不同等级需要赠送的积分
				String levelCode =  ConvertUtil.getString(retList.get(0).get("LevelCode"));
				point = ConvertUtil.getInt(rewardPoints.get(levelCode));
			}
			if(point == 0){//当奖励的积分为0时，不需要维护积分
				rewardFlag = 1;
			}else if(rewardFlag == 0){
				//当同一个会员在同一个公众号中已经绑定过，则再次绑定不赠送积分
				Map<String, Object> recordMap = new HashMap<String, Object>();
				recordMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
				recordMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
				recordMap.put("WechatSeviceCode", paramMap.get("OriginalWechatCode"));
				recordMap.put("BIN_MemberInfoID", retList.get(0).get("BIN_MemberInfoID"));
				List<Map<String, Object>> recordList = memberInfoService.getMemBindRecord(recordMap);
				if(null != recordList && recordList.size() > 1 ){
					rewardFlag = 1;
				}
			}
			if(rewardFlag == 0){
				//发送积分维护的信息
				//MQ需要用到的数据
				//会员信息
				String memberCode = ConvertUtil.getString(retList.get(0).get("MemCode"));
				String modifyPoint = ConvertUtil.getString(point);
				int brandId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID")));
				int orgId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID")));
				String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
				String orgCode = ConvertUtil.getString(paramMap.get("OrganizationCode"));
				String billType = CherryConstants.MESSAGE_TYPE_PT;
				String billCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", billType);
				String ticketDateStr = bindTime;
				if (null == ticketDateStr) {
					ticketDateStr = memberInfoService.getForwardSYSDate();
				}
				//设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 所属品牌
				mqInfoDTO.setBrandInfoId(brandId);
				mqInfoDTO.setBrandCode(brandCode);
				// 所属组织
				mqInfoDTO.setOrganizationInfoId(orgId);
				mqInfoDTO.setOrgCode(orgCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
				// 设定消息命令类型
				msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定插入到MongoDB的信息
				DBObject dbObject = new BasicDBObject();
				// 组织代码
				dbObject.put("OrgCode", orgCode);
				// 品牌代码
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", billType);
				// 单据号
				dbObject.put("TradeNoIF", billCode);
				// 数据来源
				dbObject.put("Sourse", "Wecaht");
				mqInfoDTO.setDbObject(dbObject);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 业务类型
				mainData.put("TradeType", billType);
				//修改模式
				mainData.put("SubTradeType","2");
				//积分类型
				mainData.put("MaintainType","8"); //code 1214绑定送积分
				// 单据号
				mainData.put("TradeNoIF", billCode);
				// 数据来源
				mainData.put("Sourse", "Wechat");
				//积分维护明细数据
				List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
				Map<String,Object> detailMap = new HashMap<String,Object>();
				//会员卡号
				detailMap.put("MemberCode", memberCode);
				//修改的积分
				detailMap.put("ModifyPoint", modifyPoint);
				//理由
				detailMap.put("Reason", paramMap.get("OriginalWechatCode"));
				//业务时间
				detailMap.put("BusinessTime", ticketDateStr);
				detailDataList.add(detailMap);
				dataLine.put("MainData", mainData);
				dataLine.put("DetailDataDTOList", detailDataList);
				msgDataMap.put("DataLine", dataLine);
				mqInfoDTO.setMsgDataMap(msgDataMap);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}else{
				point = 0;
			}
		}
		//返回绑定的信息
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("MemberID", retList.get(0).get("BIN_MemberInfoID"));
		infoMap.put("MemberCode", retList.get(0).get("MemCode"));
		infoMap.put("Point", point);
		retMap.put("ResultMap", infoMap);
		return retMap;
	}

	@Override
	public Map getPointChange(Map paramMap) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MemberID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberID是必须的");
			return retMap;
		}

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("memberId", paramMap.get("MemberID"));
		String startPage = (String)paramMap.get("StartPage");
		String pageSize = (String)paramMap.get("PageSize");
		if(startPage == null || "".equals(startPage)) {
			startPage = "1";
		}
		if(pageSize == null || "".equals(pageSize)) {
			pageSize = "20";
		}
		int start = (Integer.parseInt(startPage) - 1) * Integer.parseInt(pageSize) + 1;
		int end = start + Integer.parseInt(pageSize) - 1;
		String sort = "ChangeDate desc";
		searchMap.put("SORT_ID", sort);
		searchMap.put("START", start);
		searchMap.put("END", end);

		int count = memberInfoService.getPointChangeCount(searchMap);
		retMap.put("ResultTotalCNT", count);
		if(count > 0) {
			List<Map<String, Object>> retList = memberInfoService.getPointChange(searchMap);
			if (null != retList && !retList.isEmpty()) {
				// 组织代码
				String orgCode = ConvertUtil.getString(paramMap.get("OrgCode"));
				// 品牌代码
				String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
				if (!CherryChecker.isNullOrEmpty(orgCode)
						&& !CherryChecker.isNullOrEmpty(brandCode)) {
					Map<String, String> nameMap = new HashMap<String, String>();
					for (Map<String, Object> retInfo : retList) {
						List<Map<String, Object>> detailList = (List<Map<String, Object>>) retInfo.get("DetailList");
						if (null != detailList) {
							for (Map<String, Object> detailInfo : detailList) {
								// 明细中的积分类型
								String pointType = ConvertUtil.getString(detailInfo.get("pointType"));
								if (!CherryChecker.isNullOrEmpty(pointType)) {
									// 积分类型名称
									String pointTypeName = null;
									if (nameMap.containsKey(pointType)) {
										pointTypeName = nameMap.get(pointType);
									} else {
										// 积分类型名称
										pointTypeName = codeTable.getValueByKey("1214", pointType, orgCode, brandCode);
										nameMap.put(pointType, pointTypeName);
									}
									detailInfo.put("pointTypeName", pointTypeName);
								}
							}
						}
					}
				}
			}
			retMap.put("ResultContent", retList);
		}
		return retMap;
	}

	@Override
	public Map modifyMemberInfo(Map map) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put(CherryConstants.ORG_CODE, map.get("OrganizationCode"));
		String brandCode = (String)map.get("BrandCode");
		paramMap.put(CherryConstants.BRAND_CODE, brandCode);
		// 作成者
		paramMap.put(CherryConstants.CREATEDBY, "cherryws");
		// 作成程序名
		paramMap.put(CherryConstants.CREATEPGM, "MemberInfoLogic");
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, "cherryws");
		// 更新程序名
		paramMap.put(CherryConstants.UPDATEPGM, "MemberInfoLogic");
		paramMap.put("memCode", map.get("MemberCode"));
		if (!CherryChecker.isNullOrEmpty(map.get("NickName"), true)) {
			// 会员昵称
			paramMap.put("nickname", map.get("NickName"));
		}
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(paramMap);
		String subType = (String)map.get("SubType");
		if("0".equals(subType)) {
			String memLevel = (String)map.get("MemLevel");
			if(memLevel != null && !"".equals(memLevel)) {
				paramMap.put("memLevel", memLevel);
				Map<String, Object> memberLevelMap = memberInfoService.selMemberLevel(paramMap);
				if(memberLevelMap != null) {
					paramMap.put("memberLevel", String.valueOf(memberLevelMap.get("memLevel")));
				} else {
					retMap.put("ERRORCODE", "WSE0014");
					retMap.put("ERRORMSG", "未找到指定的等级");
					return retMap;
				}
			}
			if(memberInfoMap != null) {
				Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
				if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
					Object memberLevel = memberInfoMap.get("memberLevel");
					if(memberLevel != null) {
						String levelName = (String)memberInfoMap.get("levelName");
						paramMap.put("oldLevelName", levelName);
						paramMap.put("oldMemberLevel", memberLevel.toString());
					}
					String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
					paramMap.put("memberInfoId", memberInfoId);
				} else {
					retMap.put("ERRORCODE", "WSE0010");
					retMap.put("ERRORMSG", "指定会员已存在");
					return retMap;
				}
			}
		} else {
			if(memberInfoMap != null) {
				Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
				if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
					retMap.put("ERRORCODE", "WSE0001");
					retMap.put("ERRORMSG", "未找到指定的会员");
					return retMap;
				}
				paramMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
				Object addressInfoId = memberInfoMap.get("addressInfoId");
				if(addressInfoId != null && !"".equals(addressInfoId.toString())) {
					paramMap.put("addressInfoId", addressInfoId.toString());
				}
				Object version = memberInfoMap.get("version");
				if(version != null && !"".equals(version.toString())) {
					paramMap.put("version", version.toString());
				}
				Object referrerIdOld = memberInfoMap.get("referrerId");
				if(referrerIdOld != null && !"".equals(referrerIdOld.toString())) {
					paramMap.put("referrerIdOld", referrerIdOld.toString());
				}
				paramMap.put("memberPasswordOld", memberInfoMap.get("memberPassword"));
				paramMap.put("memCodeOld", map.get("MemberCode"));
			} else {
				retMap.put("ERRORCODE", "WSE0001");
				retMap.put("ERRORMSG", "未找到指定的会员");
				return retMap;
			}
		}

		String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
		String counterCode = (String)map.get("CounterCode");
		if(counterCode != null && counterCode.toLowerCase().equals(cherryclear)) {
			paramMap.put("organizationCode", cherryclear);
			paramMap.put("organizationId", cherryclear);
			counterCode = null;
		}
		if(counterCode != null && !"".equals(counterCode)) {
			paramMap.put("organizationCode", counterCode);
			Map<String, Object> counterInfo = memberInfoService.selCounterInfo(paramMap);
			if(counterInfo != null) {
				paramMap.put("organizationId", counterInfo.get("organizationId"));
				Object counterKind = counterInfo.get("counterKind");
				if(counterKind != null && "1".equals(counterKind.toString())) {
					paramMap.put("testType", "0");
				} else {
					paramMap.put("testType", "1");
				}
			} else {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
		}

		String employeeCode = (String)map.get("EmployeeCode");
		if(employeeCode != null && employeeCode.toLowerCase().equals(cherryclear)) {
			paramMap.put("employeeCode", cherryclear);
			paramMap.put("employeeId", cherryclear);
			employeeCode = null;
		}
		if(employeeCode != null && !"".equals(employeeCode)) {
			paramMap.put("employeeCode", employeeCode);
			Map<String, Object> employeeInfo = memberInfoService.selEmployeeInfo(paramMap);
			if(employeeInfo != null) {
				paramMap.put("employeeId", employeeInfo.get("employeeId"));
			} else {
				retMap.put("ERRORCODE", "WSE0002");
				retMap.put("ERRORMSG", "未找到指定的员工");
				return retMap;
			}
		}

		String provinceCode = (String)map.get("ProvinceCode");
		String cityCode = (String)map.get("CityCode");
		String countyCode = (String)map.get("CountyCode");
		if((provinceCode != null && !"".equals(provinceCode))
				|| (cityCode != null && !"".equals(cityCode))
				|| (countyCode != null && !"".equals(countyCode))) {
			if(provinceCode != null && provinceCode.toLowerCase().equals(cherryclear)) {
				paramMap.put("provinceId", cherryclear);
				paramMap.put("cityId", cherryclear);
				paramMap.put("countyId", cherryclear);
				provinceCode = null;
				cityCode = null;
				countyCode = null;
			} else if(cityCode != null && cityCode.toLowerCase().equals(cherryclear)) {
				paramMap.put("cityId", cherryclear);
				paramMap.put("countyId", cherryclear);
				cityCode = null;
				countyCode = null;
			} else if(countyCode != null && countyCode.toLowerCase().equals(cherryclear)) {
				paramMap.put("countyId", cherryclear);
				countyCode = null;
			}
			boolean regionError = false;
			if(countyCode != null && !"".equals(countyCode)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCountyCode(provinceCode, cityCode, countyCode);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					// 设置市ID
					paramMap.put("cityId", regionInfo.get("cityId"));
					// 设置区县ID
					paramMap.put("countyId", regionInfo.get("countyId"));
					paramMap.put("provinceName", regionInfo.get("provinceName"));
					paramMap.put("cityName", regionInfo.get("cityName"));
					paramMap.put("countyName", regionInfo.get("countyName"));
				} else {
					regionError = true;
				}
			} else if(cityCode != null && !"".equals(cityCode)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityCode(provinceCode, cityCode);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					// 设置市ID
					paramMap.put("cityId", regionInfo.get("cityId"));
					paramMap.put("provinceName", regionInfo.get("provinceName"));
					paramMap.put("cityName", regionInfo.get("cityName"));
					if("1".equals(subType)) {
						paramMap.put("countyId", cherryclear);
					}
				} else {
					regionError = true;
				}
			} else if(provinceCode != null && !"".equals(provinceCode)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByProvinceCode(provinceCode);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					paramMap.put("provinceName", regionInfo.get("provinceName"));
					if("1".equals(subType)) {
						paramMap.put("cityId", cherryclear);
						paramMap.put("countyId", cherryclear);
					}
				} else {
					regionError = true;
				}
			}
			if(regionError) {
				retMap.put("ERRORCODE", "WSE0005");
				retMap.put("ERRORMSG", "未找到指定的省市县");
				return retMap;
			}
		} else {
			String memProvince = (String)map.get("Province");
			String memCity = (String)map.get("City");
			String memCounty = (String)map.get("County");
			if(memProvince != null && memProvince.toLowerCase().equals(cherryclear)) {
				paramMap.put("provinceId", cherryclear);
				paramMap.put("cityId", cherryclear);
				paramMap.put("countyId", cherryclear);
				memProvince = null;
				memCity = null;
				memCounty = null;
			} else if(memCity != null && memCity.toLowerCase().equals(cherryclear)) {
				paramMap.put("cityId", cherryclear);
				paramMap.put("countyId", cherryclear);
				memCity = null;
				memCounty = null;
			} else if(memCounty != null && memCounty.toLowerCase().equals(cherryclear)) {
				paramMap.put("countyId", cherryclear);
				memCounty = null;
			}
			boolean regionError = false;
			if(memCity != null && memCity.contains("/")) {
				String[] memCitys = memCity.split("/");
				memCity = memCitys[memCitys.length-1];
			}
			if(memCounty != null && memCounty.contains("/")) {
				String[] memCountys = memCounty.split("/");
				memCounty = memCountys[memCountys.length-1];
			}
			if(memCounty != null && !"".equals(memCounty)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCountyName(memProvince, memCity, memCounty);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					// 设置市ID
					paramMap.put("cityId", regionInfo.get("cityId"));
					// 设置区县ID
					paramMap.put("countyId", regionInfo.get("countyId"));
					paramMap.put("provinceName", memProvince);
					paramMap.put("cityName", memCity);
					paramMap.put("countyName", memCounty);
				} else {
					regionError = true;
				}
			} else if(memCity != null && !"".equals(memCity)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(memProvince, memCity);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					// 设置市ID
					paramMap.put("cityId", regionInfo.get("cityId"));
					paramMap.put("provinceName", memProvince);
					paramMap.put("cityName", memCity);
					if("1".equals(subType)) {
						paramMap.put("countyId", cherryclear);
					}
				} else {
					regionError = true;
				}
			} else if(memProvince != null && !"".equals(memProvince)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByProvinceName(memProvince);
				if(regionInfo != null) {
					// 设置省ID
					paramMap.put("provinceId", regionInfo.get("provinceId"));
					paramMap.put("provinceName", memProvince);
					if("1".equals(subType)) {
						paramMap.put("cityId", cherryclear);
						paramMap.put("countyId", cherryclear);
					}
				} else {
					regionError = true;
				}
			}
			if(regionError) {
				retMap.put("ERRORCODE", "WSE0005");
				retMap.put("ERRORMSG", "未找到指定的省市县");
				return retMap;
			}
		}

		// 推荐会员
		String recommenderMember = (String)map.get("RecommenderMember");
		if(recommenderMember != null && recommenderMember.toLowerCase().equals(cherryclear)) {
			paramMap.put("referrerId", cherryclear);
			recommenderMember = null;
		}
		if(recommenderMember != null && !"".equals(recommenderMember)) {
			paramMap.put("recommenderMember", recommenderMember);
			// 查询推荐会员ID
			Map<String, Object> referrerMap = memberInfoService.selReferrerID(paramMap);
			if(referrerMap != null && !referrerMap.isEmpty()) {
				paramMap.put("referrerId", String.valueOf(referrerMap.get("referrerId")));
				paramMap.put("referrer", referrerMap.get("memCode"));
			} else {
				retMap.put("ERRORCODE", "WSE0006");
				retMap.put("ERRORMSG", "未找到指定的推荐会员");
				return retMap;
			}
		}

		String name = (String)map.get("Name");
		if(name != null && name.toLowerCase().equals(cherryclear)) {
			paramMap.put("memName", cherryclear);
		} else {
			paramMap.put("memName", name);
		}
		String gender = (String)map.get("Gender");
		if(gender != null && gender.toLowerCase().equals(cherryclear)) {
			paramMap.put("gender", cherryclear);
		} else {
			paramMap.put("gender", gender);
		}
		String telephone = (String)map.get("Telephone");
		if(telephone != null && telephone.toLowerCase().equals(cherryclear)) {
			paramMap.put("telephone", cherryclear);
		} else {
			if(telephone != null && !"".equals(telephone)) {
				try {
					paramMap.put("telephone", CherrySecret.encryptData(brandCode,telephone));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					if("0".equals(subType)) {
						retMap.put("ERRORCODE", "WSE0011");
						retMap.put("ERRORMSG", "添加会员失败");
					} else {
						retMap.put("ERRORCODE", "WSE0012");
						retMap.put("ERRORMSG", "更新会员失败");
					}
					return retMap;
				}
			}
		}
		String mobilePhone = (String)map.get("MobilePhone");
		if(mobilePhone != null && mobilePhone.toLowerCase().equals(cherryclear)) {
			paramMap.put("mobilePhone", cherryclear);
		} else {
			if(mobilePhone != null && !"".equals(mobilePhone)) {
				try {
					paramMap.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					if("0".equals(subType)) {
						retMap.put("ERRORCODE", "WSE0011");
						retMap.put("ERRORMSG", "添加会员失败");
					} else {
						retMap.put("ERRORCODE", "WSE0012");
						retMap.put("ERRORMSG", "更新会员失败");
					}
					return retMap;
				}
			}
		}
		String email = (String)map.get("Email");
		if(email != null && email.toLowerCase().equals(cherryclear)) {
			paramMap.put("email", cherryclear);
		} else {
			if(email != null && !"".equals(email)) {
				try {
					paramMap.put("email", CherrySecret.encryptData(brandCode,email));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					if("0".equals(subType)) {
						retMap.put("ERRORCODE", "WSE0011");
						retMap.put("ERRORMSG", "添加会员失败");
					} else {
						retMap.put("ERRORCODE", "WSE0012");
						retMap.put("ERRORMSG", "更新会员失败");
					}
					return retMap;
				}
			}
		}
		String messageId = (String)map.get("MessageId");
		if(messageId != null && messageId.toLowerCase().equals(cherryclear)) {
			paramMap.put("messageId", cherryclear);
		} else {
			paramMap.put("messageId", messageId);
		}
		String birthYear = (String)map.get("BirthYear");
		if(birthYear != null && birthYear.toLowerCase().equals(cherryclear)) {
			paramMap.put("birthYear", cherryclear);
		} else {
			paramMap.put("birthYear", birthYear);
		}
		String birthDay = (String)map.get("BirthDay");
		if(birthDay != null && birthDay.toLowerCase().equals(cherryclear)) {
			paramMap.put("birthDay", cherryclear);
		} else {
			paramMap.put("birthDay", birthDay);
		}
		String joinDate = (String)map.get("JoinDate");
		if(joinDate != null && joinDate.toLowerCase().equals(cherryclear)) {
			paramMap.put("joinDate", cherryclear);
		} else {
			paramMap.put("joinDate", joinDate);
		}
		String joinTime = (String)map.get("JoinTime");
		if(joinTime != null && joinTime.toLowerCase().equals(cherryclear)) {
			paramMap.put("joinTime", cherryclear);
		} else {
			paramMap.put("joinTime", joinTime);
		}
		String address = (String)map.get("Address");
		if(address != null && address.toLowerCase().equals(cherryclear)) {
			paramMap.put("address", cherryclear);
		} else {
			paramMap.put("address", address);
		}
		String postcode = (String)map.get("Postcode");
		if(postcode != null && postcode.toLowerCase().equals(cherryclear)) {
			paramMap.put("postcode", cherryclear);
		} else {
			paramMap.put("postcode", postcode);
		}
		String memAgeGetMethod = (String)map.get("MemAgeGetMethod");
		if(memAgeGetMethod != null && memAgeGetMethod.toLowerCase().equals(cherryclear)) {
			paramMap.put("memAgeGetMethod", cherryclear);
		} else {
			paramMap.put("memAgeGetMethod", memAgeGetMethod);
		}
		String memo1 = (String)map.get("Memo1");
		if(memo1 != null && memo1.toLowerCase().equals(cherryclear)) {
			paramMap.put("memo1", cherryclear);
		} else {
			if(memo1 != null && !"".equals(memo1)) {
				try {
					paramMap.put("memo1", CherrySecret.encryptData(brandCode,memo1));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					if("0".equals(subType)) {
						retMap.put("ERRORCODE", "WSE0011");
						retMap.put("ERRORMSG", "添加会员失败");
					} else {
						retMap.put("ERRORCODE", "WSE0012");
						retMap.put("ERRORMSG", "更新会员失败");
					}
					return retMap;
				}
			}
		}
		String active = (String)map.get("Active");
		if(active != null && active.toLowerCase().equals(cherryclear)) {
			paramMap.put("active", cherryclear);
		} else {
			paramMap.put("active", active);
		}
		String activeDate = (String)map.get("ActiveDate");
		if(activeDate != null && activeDate.toLowerCase().equals(cherryclear)) {
			paramMap.put("activeDate", cherryclear);
		} else {
			paramMap.put("activeDate", activeDate);
		}
		String ActiveChannel = (String)map.get("ActiveChannel");
		if(ActiveChannel != null && ActiveChannel.toLowerCase().equals(cherryclear)) {
			paramMap.put("activeChannel", cherryclear);
		} else {
			paramMap.put("activeChannel", ActiveChannel);
		}
		String memberPassword = (String)map.get("MemberPassword");
		if(memberPassword != null && memberPassword.toLowerCase().equals(cherryclear)) {
			paramMap.put("memberPassword", cherryclear);
		} else {
			paramMap.put("memberPassword", memberPassword);
		}
		paramMap.put("synMemMode", "1");

		//入会途径
		String channelCode = (String)map.get("JoinChannel");
		if(channelCode != null && channelCode.toLowerCase().equals(cherryclear)) {
			paramMap.put("channelCode", cherryclear);
		} else {
			paramMap.put("channelCode", channelCode);
		}

		//数据来源分类1
		String dataSource1 = (String)map.get("DataSource1");
		if(dataSource1 != null && dataSource1.toLowerCase().equals(cherryclear)) {
			paramMap.put("dataSource1", cherryclear);
		} else {
			paramMap.put("dataSource1", dataSource1);
		}
		//数据来源分类2
		String dataSource2 = (String)map.get("DataSource2");
		if(dataSource2 != null && dataSource2.toLowerCase().equals(cherryclear)) {
			paramMap.put("dataSource2", cherryclear);
		} else {
			paramMap.put("dataSource2", dataSource2);
		}
		//数据来源分类3
		String dataSource3 = (String)map.get("DataSource3");
		if(dataSource3 != null && dataSource3.toLowerCase().equals(cherryclear)) {
			paramMap.put("dataSource3", cherryclear);
		} else {
			paramMap.put("dataSource3", dataSource3);
		}
		//会员备注信息3
		String memo3 = (String)map.get("Memo3");
		if(memo3 != null && memo3.toLowerCase().equals(cherryclear)) {
			paramMap.put("memo3", cherryclear);
		} else {
			paramMap.put("memo3", memo3);
		}
		//是否立即生成入会礼，99：立即生成入会礼，其他不生成
		String BeMemGift = ConvertUtil.getString(map.get("BeMemGift"));
		if("99".equals(BeMemGift)){
			paramMap.put("importMode", BeMemGift);
		}else{
			paramMap.put("importMode", "1");
		}
		//职业
		String profession = (String)map.get("Profession");
		if(profession != null && profession.toLowerCase().equals(cherryclear)) {
			paramMap.put("profession", cherryclear);
		} else {
			paramMap.put("profession", profession);
		}
		//兴趣爱好
		String interests = (String)map.get("Interests");
		if(interests != null && interests.toLowerCase().equals(cherryclear)) {
			paramMap.put("interests", cherryclear);
		} else {
			paramMap.put("interests", interests);
		}
		if("0".equals(subType)) {
			try {

				binOLMBMBM11_BL.tran_addMemberInfo(paramMap);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("ERRORCODE", "WSE0011");
				retMap.put("ERRORMSG", "添加会员失败");
				return retMap;
			}
		} else {
			try {
				binOLMBMBM06_BL.tran_updMemberInfo(paramMap);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				retMap.put("ERRORCODE", "WSE0012");
				retMap.put("ERRORMSG", "更新会员失败");
				return retMap;
			}
		}
		return retMap;
	}

	@Override
	public Map modifyMemberPoint(Map map) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put(CherryConstants.ORG_CODE, map.get("OrganizationCode"));
		paramMap.put(CherryConstants.BRAND_CODE, map.get("BrandCode"));
		paramMap.put("MemberCode", map.get("MemberCode"));
		paramMap.put("memCode", map.get("MemberCode"));
		Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(paramMap);
		if(memberInfoMap != null) {
			Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
			if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
				retMap.put("ERRORCODE", "WSE0001");
				retMap.put("ERRORMSG", "未找到指定的会员");
				return retMap;
			}
		} else {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "未找到指定的会员");
			return retMap;
		}

		String counterCode = (String)map.get("CounterCode");
		if(counterCode != null && !"".equals(counterCode)) {
			paramMap.put("organizationCode", counterCode);
			Map<String, Object> counterInfo = memberInfoService.selCounterInfo(paramMap);
			if(counterInfo == null) {
				retMap.put("ERRORCODE", "WSE0003");
				retMap.put("ERRORMSG", "未找到指定的柜台");
				return retMap;
			}
		}
		paramMap.put("CounterCode", counterCode);

		String employeeCode = (String)map.get("EmployeeCode");
		if(employeeCode != null && !"".equals(employeeCode)) {
			paramMap.put("employeeCode", employeeCode);
			Map<String, Object> employeeInfo = memberInfoService.selEmployeeInfo(paramMap);
			if(employeeInfo == null) {
				retMap.put("ERRORCODE", "WSE0002");
				retMap.put("ERRORMSG", "未找到指定的员工");
				return retMap;
			}
		}
		String clubCode = (String) map.get("ClubCode");
		if (!CherryChecker.isNullOrEmpty(clubCode)) {
			Integer clubId = memberInfoService.selMemClubId(clubCode);
			if (null == clubId) {
				retMap.put("ERRORCODE", "WSE0004");
				retMap.put("ERRORMSG", "未找到指定的俱乐部");
				return retMap;
			}
			paramMap.put("memberClubId", clubId);
		}
		paramMap.put("EmployeeCode", employeeCode);

		paramMap.put("pointType", map.get("MaintType"));
		paramMap.put("ModifyPoint", map.get("MaintPoint"));
		paramMap.put("BusinessTime", map.get("BusinessTime"));
		paramMap.put("Sourse", map.get("Sourse"));
		paramMap.put("Reason", map.get("Comment"));
		if(!CherryChecker.isNullOrEmpty(map.get("MaintainType"), true)){
			paramMap.put("MaintainType", map.get("MaintainType"));
		}
		try {
			binOLMBMBM03_BL.sendPointsMQ(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0013");
			retMap.put("ERRORMSG", "积分维护失败");
			return retMap;
		}
		return retMap;
	}

	@Override
	public Map getMemSaleRecord(Map map) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("memCode", map.get("MemCode"));
		paramMap.put("saleDateStart", map.get("SaleDateStart"));
		paramMap.put("saleDateEnd", map.get("SaleDateEnd"));
		String topCount = (String)map.get("TopCount");
		if(topCount !=null && CherryChecker.isNumeric(topCount)) {
			paramMap.put("COUNT", topCount);
		}
		List<Map<String, Object>> memSaleRecordList = memberInfoService.selMemSaleRecord(paramMap);
		if(memSaleRecordList != null && !memSaleRecordList.isEmpty()) {
			List<Map<String, Object>> _memSaleRecordList = new ArrayList<Map<String, Object>>();
			String oldBillCode = null;
			List<Map<String, Object>> memSaleRecordDetailList = null;
			for(int i = 0; i < memSaleRecordList.size(); i++) {
				Map<String, Object> memSaleRecordMap = memSaleRecordList.get(i);
				Map<String, Object> memSaleRecordDetailMap = new HashMap<String, Object>();
				memSaleRecordDetailMap.put("UnitCode", memSaleRecordMap.get("UnitCode"));
				memSaleRecordDetailMap.put("BarCode", memSaleRecordMap.get("BarCode"));
				memSaleRecordDetailMap.put("PrtName", memSaleRecordMap.get("PrtName"));
				memSaleRecordDetailMap.put("PrtType", memSaleRecordMap.get("PrtType"));
				memSaleRecordDetailMap.put("PricePay", memSaleRecordMap.get("PricePay"));
				memSaleRecordDetailMap.put("QuantityDetail", memSaleRecordMap.get("QuantityDetail"));
				String billCode = (String)memSaleRecordMap.get("BillCode");
				if(oldBillCode == null || !oldBillCode.equals(billCode)) {
					oldBillCode = billCode;
					Map<String, Object> _memSaleRecordMap = new HashMap<String, Object>();
					memSaleRecordDetailList = new ArrayList<Map<String, Object>>();
					memSaleRecordDetailList.add(memSaleRecordDetailMap);
					_memSaleRecordMap.put("BillCode", billCode);
					_memSaleRecordMap.put("SaleType", memSaleRecordMap.get("SaleType"));
					_memSaleRecordMap.put("BillState", memSaleRecordMap.get("BillState"));
					_memSaleRecordMap.put("SaleTime", memSaleRecordMap.get("SaleTime"));
					_memSaleRecordMap.put("CounterCode", memSaleRecordMap.get("CounterCode"));
					_memSaleRecordMap.put("CounterName", memSaleRecordMap.get("CounterName"));
					_memSaleRecordMap.put("Amount", memSaleRecordMap.get("Amount"));
					_memSaleRecordMap.put("Quantity", memSaleRecordMap.get("Quantity"));
					_memSaleRecordMap.put("Point", memSaleRecordMap.get("Point"));
					_memSaleRecordMap.put("SaleDetail", memSaleRecordDetailList);
					_memSaleRecordList.add(_memSaleRecordMap);
				} else {
					memSaleRecordDetailList.add(memSaleRecordDetailMap);
				}
			}
			retMap.put("ResultContent", _memSaleRecordList);
		} else {
			retMap.put("ERRORCODE", "WSE0015");
			retMap.put("ERRORMSG", "未查询到符号条件的会员销售记录。");
		}
		return retMap;
	}

	@Override
	public Map GetMemCouponCodes(Map map) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("ConditionType", map.get("ConditionType"));
		paramMap.put("Condition", map.get("Condition"));
		// 获取会员优惠信息
		List<Map<String, Object>> memSaleRecordList = memberInfoService.getCampaignOrderList(paramMap);
		if(memSaleRecordList != null && !memSaleRecordList.isEmpty()) {
			retMap.put("ResultContent", memSaleRecordList);
		} else {
			retMap.put("ERRORCODE", "WSE0018");
			retMap.put("ERRORMSG", "未查询到符号条件的会员优惠信息。");
		}
		return retMap;
	}

	@Override
	public Map tran_unbindWeChatUser(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(map.get("MessageID"), true)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memId", map.get("MemId"));
		paramMap.put("updatedBy", "WeChat");
		paramMap.put("updatePGM", "MemberInfoLogic");
		// 微信用户解绑
		int result = memberInfoService.unbindMessageId(paramMap);
		if(result == 0) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "未找到指定的会员");
			return retMap;
		}
		// 添加会员微信绑定履历
		Map<String, Object> memBindRecordMap = new HashMap<String, Object>();
		memBindRecordMap.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
		memBindRecordMap.put("brandInfoId", map.get("BIN_BrandInfoID"));
		memBindRecordMap.put("memberInfoId", map.get("MemId"));
		memBindRecordMap.put("wechatSeviceCode", map.get("OriginalWechatCode"));
		memBindRecordMap.put("wechatId", map.get("MessageID"));
		memBindRecordMap.put("recordType", "0");
		memBindRecordMap.put("recordDateTime", map.get("UnBindTime"));
		memBindRecordMap.put("createdBy", "WeChat");
		memBindRecordMap.put("createPGM", "MemberInfoLogic");
		memBindRecordMap.put("updatedBy", "WeChat");
		memBindRecordMap.put("updatePGM", "MemberInfoLogic");
		memberInfoService.addMemBindRecord(memBindRecordMap);

		//更新会员微信绑定关系
		String wechatSeviceCode = ConvertUtil.getString(map.get("OriginalWechatCode"));
		if(!CherryChecker.isNullOrEmpty(wechatSeviceCode, true)){
			Map<String, Object> memBindRelationMap = new HashMap<String, Object>();
			memBindRelationMap.put("WechatSeviceCode", wechatSeviceCode);
			memBindRelationMap.put("WechatID", map.get("MessageID"));
			memBindRelationMap.put("ValidFlag", "0");
			memBindRelationMap.put("createdBy", "WeChat");
			memBindRelationMap.put("createPGM", "MemberInfoLogic");
			memBindRelationMap.put("updatedBy", "WeChat");
			memBindRelationMap.put("updatePGM", "MemberInfoLogic");
			memberInfoService.updateMemBindRelation(memBindRelationMap);
		}

		return retMap;
	}

	@Override
	public Map<String, Object> SearchCouponWechatRequest(Map<String, Object> map)
			throws Exception {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(map.get("MessageID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MessageID是必须的");
			return retMap;
		}
		if (CherryChecker.isNullOrEmpty(map.get("MemberCode"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberCode是必须的");
			return retMap;
		}
		if (CherryChecker.isNullOrEmpty(map.get("MobilePhone"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		}
		if (CherryChecker.isNullOrEmpty(map.get("MemberName"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberName是必须的");
			return retMap;
		}
		//检索会员信息
		List<Map<String, Object>> memList = memberInfoService.getMemberInfoOAuth(map);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0020");
			retMap.put("ERRORMSG", "未查询到符合条件的会员。");
			return retMap;
		} else if (memList.size() == 1) {
			Map<String, Object> tmp = (Map<String, Object>) memList.get(0);
			// 查询验证码
			Map<String,Object> praMap = new HashMap<String,Object>();
			praMap.put("CampaignCode", "GETVERIFICATIONCOUPON");
			praMap.put("MemberID", tmp.get("BIN_MemberInfoID"));
			praMap.put("MobilePhone", map.get("MobilePhone"));
			Map<String, Object> couponMap = memberInfoService.getNewestCouponCode(praMap);
			if(couponMap != null) {
				// 对查询到的验证码过期时间进行延长
				String sysTime = memberInfoService.getSYSDateTime();
				String expiredTime = this.addDateSecond(sysTime, CherryUtil.obj2int(600));
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("couponId", couponMap.get("couponId"));
				paramMap.put("expiredTime", expiredTime);
				memberInfoService.extendCouponExpiredTime(paramMap);

				Map<String, Object> ret = new HashMap<String, Object>();
				ret.put("MemberID", tmp.get("BIN_MemberInfoID"));
				ret.put("CouponCode", couponMap.get("couponCode"));
				ret.put("expiredTime", expiredTime);
				retMap.put("ResultMap", ret);
				return retMap;
			}else{
				retMap.put("ERRORCODE", "WSE0022");
				retMap.put("ERRORMSG", "没有找到可以使用的验证号");
				return retMap;
			}
		} else {
			retMap.put("ERRORCODE", "WSE0021");
			retMap.put("ERRORMSG", "查询到多个会员。");
			return retMap;
		}
	}

	/**
	 * 根据秒钟添加日期
	 * @param dateTime
	 * @param second
	 * @return
	 */
	public String addDateSecond (String dateTime,int second){
		try{
			SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = spf.parse(dateTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.SECOND, second);
			String strDate = spf.format(cal.getTime());
			return strDate;
		}catch(Exception ex){
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getMemberPassword(Map paramMap) throws Exception {

		// 查询会员基本信息
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(CherryChecker.isNullOrEmpty(paramMap.get("ConditionType"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ConditionType异常。ConditionType="+String.valueOf(paramMap.get("ConditionType")));
			return retMap;
		}
		String conditionType = String.valueOf(paramMap.get("ConditionType"));
		if(!conditionType.equals("MemberID")&& !conditionType.equals("MemberCode")
				&& !conditionType.equals("MobilePhone")){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数ConditionType异常。ConditionType="+conditionType);
			return retMap;
		}

		if(CherryChecker.isNullOrEmpty(paramMap.get("Condition"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Condition异常。Condition="+conditionType);
			return retMap;
		}

		//如果查询条件是手机号，则进行加密处理后再查询（目前仅个别品牌手机号是加密的）
		if(conditionType.equals("MobilePhone")){
			try {
				String brandCode = String.valueOf(paramMap.get("BrandCode"));
				paramMap.put("Condition",CherrySecret.encryptData(brandCode,String.valueOf(paramMap.get("Condition"))));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				retMap.put("ERRORCODE", "WSE9999");
				retMap.put("ERRORMSG", "处理过程中发生未知异常");
				return retMap;
			}
		}

		List<Map<String, Object>> memList = memberInfoService.getMemberPassword(paramMap);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "未查询到符合条件的会员。");
		} else if(memList.size() > 1){
			retMap.put("ERRORCODE", "WSE0021");
			retMap.put("ERRORMSG", "查询到多个符合条件的会员。");
		} else{
			Map<String, Object> memMap = (Map<String, Object>) memList.get(0);
			String memberPassword = ConvertUtil.getString(memMap.get("MemberPassword"));
			if(CherryChecker.isNullOrEmpty(memberPassword, true)){
				//会员密码为空，需要创建新密码
				if("Y".equalsIgnoreCase(ConvertUtil.getString(paramMap.get("CreateFlag")))){
					//生成6位随机密码
					memberPassword = MemberPassword.encrypt(paramMap.get("BrandCode").toString(),CherryUtil.generateSalt(6));
					paramMap.put("MemberPassword", memberPassword);
					paramMap.put("BIN_MemberInfoID", memMap.get("MemberID"));
					//修改会员密码
					memberInfoService.updateMemberPassword(paramMap);
				}
			}
			//需要推送密码
			if("Y".equalsIgnoreCase(ConvertUtil.getString(paramMap.get("SendMsgFlag")))
					&& !CherryChecker.isNullOrEmpty(memberPassword, true)){
				try{
					String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
					int organizationInfoId = ConvertUtil.getInt(paramMap.get("BIN_OrganizationInfoID"));
					int brandInfoId = ConvertUtil.getInt(paramMap.get("BIN_BrandInfoID"));
					String customerSysId = ConvertUtil.getString(memMap.get("MemberID"));
					// 发送MQ
					MQInfoDTO mqInfoDTO = new MQInfoDTO();
					// 品牌代码
					mqInfoDTO.setBrandCode(brandCode);
					String billType = CherryConstants.MESSAGE_TYPE_ES;
					String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
					// 业务类型
					mqInfoDTO.setBillType(billType);
					// 单据号
					mqInfoDTO.setBillCode(billCode);
					// 消息发送队列名
					mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);
					// 设定消息内容
					Map<String,Object> msgDataMap = new HashMap<String,Object>();
					// 设定消息版本号
					msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
					// 设定消息命令类型
					msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
					// 设定消息数据类型
					msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
					// 设定消息的数据行
					Map<String,Object> dataLine = new HashMap<String,Object>();
					// 消息的主数据行
					Map<String,Object> mainData = new HashMap<String,Object>();
					// 品牌代码
					mainData.put("BrandCode", brandCode);
					// 业务类型
					mainData.put("TradeType", billType);
					// 单据号
					mainData.put("TradeNoIF", billCode);
					// 事件类型
					mainData.put("EventType", "13");
					// 事件ID
					mainData.put("EventId", customerSysId);
					// 沟通时间
					mainData.put("EventDate", memberInfoService.getSYSDate());
					// 数据来源
					mainData.put("Sourse", "WS");
					dataLine.put("MainData", mainData);
					msgDataMap.put("DataLine", dataLine);
					mqInfoDTO.setMsgDataMap(msgDataMap);
					// 发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("MemberPassword", memberPassword);
			retMap.put("ResultMap", resultMap);
		}
		return retMap;
	}

	@Override
	public Map checkMemberPassword(Map paramMap) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(CherryChecker.isNullOrEmpty(paramMap.get("MemberCode"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberCode是必须的");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(paramMap.get("MemberPassword"))){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberPassword是必须的");
			return retMap;
		}
		List<Map<String, Object>> memList = memberInfoService.getMemberInfoOAuth(paramMap);
		if (memList == null || memList.size() == 0) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "未查询到符合条件的会员");
		} else if(memList.size() > 1){
			retMap.put("ERRORCODE", "WSE0021");
			retMap.put("ERRORMSG", "查询到多个符合条件的会员");
		} else{
			try{
				String memberPassword = MemberPassword.encrypt(paramMap.get("BrandCode").toString(),ConvertUtil.getString(paramMap.get("MemberPassword")));
				Map<String, Object> memMap = (Map<String, Object>) memList.get(0);
				if(!memberPassword.equals(ConvertUtil.getString(memMap.get("MemberPassword")))){
					retMap.put("ERRORCODE", "WSE0008");
					retMap.put("ERRORMSG", "密码不正确 ");
				}
			}catch (Exception e) {
				retMap.put("ERRORCODE", "WSE9995");
				retMap.put("ERRORMSG", "加密异常 ");
			}
		}
		return retMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map tran_wechatBindCreate(Map paramMap) throws Exception {
		String brandInfoId =  ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
		String organizationInfoId =  ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
		String orgCode = ConvertUtil.getString(paramMap.get("OrganizationCode"));
		String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
		String openId = ConvertUtil.getString(paramMap.get("OpenID"));
		String mobilePhone = ConvertUtil.getString(paramMap.get("MobilePhone"));
		String birthday = ConvertUtil.getString(paramMap.get("Birthday"));
		String employeeCode = ConvertUtil.getString(paramMap.get("EmployeeCode"));
		String memberName = ConvertUtil.getString(paramMap.get("MemberName"));
		String memberCode = ConvertUtil.getString(paramMap.get("MemberCode"));
		String gender = ConvertUtil.getString(paramMap.get("Gender"));
		String counterCode = ConvertUtil.getString(paramMap.get("CounterCode"));
		String email = ConvertUtil.getString(paramMap.get("Email"));
		String originalWechatCode = ConvertUtil.getString(paramMap.get("OriginalWechatCode"));
		String memberLevelCode = ConvertUtil.getString(paramMap.get("MemberLevelCode"));
		String bindTime = ConvertUtil.getString(paramMap.get("BindTime"));
		String referee = ConvertUtil.getString(paramMap.get("Referee"));
		String referrerId = null;
		String authFlag = (String)paramMap.get("AuthFlag");
		String couponCode = (String)paramMap.get("CouponCode");

		if(CherryChecker.isNullOrEmpty(bindTime, true)){
			bindTime = memberInfoService.getSYSDateTime();
		}
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(CherryChecker.isNullOrEmpty(openId)){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数OpenID是必须的");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(mobilePhone)){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(birthday)){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Birthday是必须的");
			return retMap;
		}
		if(!DateUtil.checkDate(birthday, "yyyy-MM-dd")){
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Birthday格式必须为yyyy-MM-dd");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(memberCode)){//会员号为空，手机号默认为会员号
			memberCode = mobilePhone;
		}
		if(CherryChecker.isNullOrEmpty(memberName)){//会员名字为空，手机号默认为会员名字
			memberName = mobilePhone;
		}
		//需要短信验证码，验证验证码
		if(null != authFlag && "1".equals(authFlag)) {
			if(null != couponCode && !"".equals(couponCode)) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("MobilePhone", mobilePhone);
				tempMap.put("CouponCode", couponCode);
				tempMap.put("CampaignCode", "GETVERIFICATIONCOUPON");
				List<Map<String,Object>> retList =memberInfoService.oAuthCoupon(tempMap);
				if(null != retList && retList.size() == 1) {
					String seconds = String.valueOf(retList.get(0).get("Seconds"));
					if(seconds.indexOf("-")<0) {
						//验证码已过期
						retMap.put("ERRORCODE", "WSE0016");
						retMap.put("ERRORMSG", "验证码已过期");
						return retMap;
					}
				}else {
					retMap.put("ERRORCODE", "WSE0009");
					retMap.put("ERRORMSG", "验证码错误");
					return retMap;
				}
			}else {
				retMap.put("ERRORCODE", "WSE0009");
				retMap.put("ERRORMSG", "验证码错误");
				return retMap;
			}
		}

		if(!CherryChecker.isNullOrEmpty(referee)) {//如果填写了推荐人
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("BIN_BrandInfoID", brandInfoId);
			params.put("BIN_OrganizationInfoID", organizationInfoId);
			params.put("MobilePhone", referee);
			List<Map<String, Object>> memList = memberInfoService.getMemInfoListByMobilePhone(params);
			if(null == memList || memList.size() < 1) {//会员信息不存在
				retMap.put("ERRORCODE", "WSE9992");
				retMap.put("ERRORMSG", "未找到指定的推荐人Referee=" + referee);
				return retMap;
			} else if (memList.size() > 1){
				retMap.put("ERRORCODE", "WSE9991");
				retMap.put("ERRORMSG", "查询到多条推荐人信息Referee=" + referee);
				return retMap;
			} else {
				Map<String, Object> refMap = memList.get(0);
				if(null != refMap) {
					referrerId = String.valueOf(refMap.get("BIN_MemberInfoID"));
				}
			}
		}
		//检查openId是否绑定过会员
		if(CherryChecker.isNullOrEmpty(originalWechatCode)){//不对应多公众号
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("BIN_BrandInfoID", brandInfoId);
			params.put("BIN_OrganizationInfoID", organizationInfoId);
			params.put("MessageID", openId);
			List<Map<String, Object>> memList = memberInfoService.getMemberInfoByMessageID(params);
			if(null != memList && memList.size() > 0){
				retMap.put("ERRORCODE", "WSE0029");
				retMap.put("ERRORMSG", "该微信用户已绑定过会员");
				return retMap;
			}
		}else{//对应多公众号
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("BIN_BrandInfoID", brandInfoId);
			params.put("BIN_OrganizationInfoID", organizationInfoId);
			params.put("WechatSeviceCode", originalWechatCode);
			params.put("WechatID", openId);
			params.put("ValidFlag", "1");
			List<Map<String, Object>> memList = memberInfoService.getMemBindRelation(params);
			if(null != memList && !memList.isEmpty()){
				retMap.put("ERRORCODE", "WSE0029");
				retMap.put("ERRORMSG", "该微信用户已绑定过会员");
				return retMap;
			}
		}

		//检查手机号是否已存在
		Map<String, Object> mobileMap = new HashMap<String, Object>();
		mobileMap.put("BIN_BrandInfoID", brandInfoId);
		mobileMap.put("BIN_OrganizationInfoID", organizationInfoId);
		mobileMap.put("MobilePhone", mobilePhone);
		List<Map<String, Object>> mobileList = memberInfoService.getMemInfoListByMobilePhone(mobileMap);
		//手机号已存在
		if(null != mobileList && mobileList.size() > 0) {
			retMap.put("ERRORCODE", "WSE0030");
			retMap.put("ERRORMSG", "手机号已存在");
			return retMap;
		}

		//检查会员是否被注册
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brandInfoId", brandInfoId);
		params.put("organizationInfoId", organizationInfoId);
		params.put("memCode", memberCode);
		Map<String, Object> memMap = memberInfoService.getMemInfoByMemCode(params);
		Map<String, Object> member = null;
		String memInfoRegFlg = null;
		String memberInfoId = null;
		if(memMap != null) {
			memInfoRegFlg = String.valueOf(memMap.get("MemInfoRegFlg"));
			memberInfoId = String.valueOf(memMap.get("memberInfoId"));
		}
		if(null != memInfoRegFlg && "0".equals(memInfoRegFlg)) {
			retMap.put("ERRORCODE", "WSE0030");
			retMap.put("ERRORMSG", "该会员已绑定其他微信");
			return retMap;
		} else{//会员没注册或假登录情况（20151126），注册会员
			Map<String, Object> memberAddMap = new HashMap<String, Object>();
			memberAddMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			memberAddMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			memberAddMap.put(CherryConstants.ORG_CODE, orgCode);
			memberAddMap.put(CherryConstants.BRAND_CODE, brandCode);
			// 作成者
			memberAddMap.put(CherryConstants.CREATEDBY, "cherryws");
			// 作成程序名
			memberAddMap.put(CherryConstants.CREATEPGM, "MemberInfoLogic");
			// 更新者
			memberAddMap.put(CherryConstants.UPDATEDBY, "cherryws");
			// 更新程序名
			memberAddMap.put(CherryConstants.UPDATEPGM, "MemberInfoLogic");
			if(null != memberInfoId && !"null".equalsIgnoreCase(memberInfoId) && !"".equals(memberInfoId)) {
				memberAddMap.put("memberInfoId", memberInfoId);
			}
			//employeeCode相关
			if(!CherryChecker.isNullOrEmpty(employeeCode)){
				Map<String, Object> mapTemp = new HashMap<String,Object>();
				mapTemp.put("employeeCode", employeeCode);
				Map<String, Object> empInfo = 	binolbsemp02BL.getEmployeeInfo(mapTemp);
				if(empInfo == null || empInfo.isEmpty()){
					retMap.put("ERRORCODE", "EWC07014");
					retMap.put("ERRORMSG", "未找到指定的员工");
					return retMap;
				}else{
					memberAddMap.put("employeeCode", employeeCode);
					memberAddMap.put("employeeId", empInfo.get("employeeId"));
				}
				if(CherryChecker.isNullOrEmpty(counterCode)){
					memberAddMap.put("organizationCode", empInfo.get("departCode"));
					memberAddMap.put("organizationId", empInfo.get("organizationId"));
				}
			}
			if(!CherryChecker.isNullOrEmpty(counterCode)){
				memberAddMap.put("organizationCode", counterCode);
				Map<String, Object> counterInfo = memberInfoService.selCounterInfo(memberAddMap);
				if(counterInfo == null || counterInfo.isEmpty()){
					/*retMap.put("ERRORCODE", "WSE0003");
					retMap.put("ERRORMSG", "未找到指定的柜台");
					return retMap;*/
					//为了避免柜台传参错误导致注册失败，所以将organizationId赋值为空
					memberAddMap.put("organizationCode", "");
					memberAddMap.put("organizationId", "");
				}else{
					memberAddMap.put("organizationId", counterInfo.get("organizationId"));
					String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
					if(counterKind != null && "1".equals(counterKind)) {
						memberAddMap.put("testType", "0");
					} else {
						memberAddMap.put("testType", "1");
					}
				}
			}
			if(!CherryChecker.isNullOrEmpty(memberLevelCode, true)){
				//会员等级
				Map<String, Object> memberLevelSelMap = new HashMap<String, Object>();
				memberLevelSelMap.put("memLevel", memberLevelCode);
				memberLevelSelMap.put("brandInfoId", brandInfoId);
				memberLevelSelMap.put("organizationInfoId", organizationInfoId);
				Map<String, Object> memberLevelMap = memberInfoService.selMemberLevel(memberLevelSelMap);
				if(memberLevelMap != null) {
					memberAddMap.put("memberLevel", String.valueOf(memberLevelMap.get("memLevel")));
				} else {
					retMap.put("ERRORCODE", "WSE0014");
					retMap.put("ERRORMSG", "未找到指定的等级");
					return retMap;
				}
			}
			memberAddMap.put("memCode", memberCode);
			memberAddMap.put("memName", memberName);
			memberAddMap.put("gender", gender);
			String[] birth = birthday.split("-");
			memberAddMap.put("birthYear", birth[0]);
			memberAddMap.put("birthDay", birth[1]+birth[2]);
			String joinDate = DateUtil.coverTime2YMD(bindTime, "yyyy-MM-dd HH:mm:ss");
			memberAddMap.put("joinDate", joinDate);
			memberAddMap.put("mobilePhone", mobilePhone);
			memberAddMap.put("email", email);
			memberAddMap.put("channelCode", "wechat");
			if(null != referrerId && !CherryChecker.isNullOrEmpty(referrerId)) {
				memberAddMap.put("referrerId", referrerId);
			}
			memberAddMap.put("messageId", openId);
			memberAddMap.put("wechatBindTime", bindTime);
			memberAddMap.put("active", "1");
			memberAddMap.put("activeDate", bindTime);
			memberAddMap.put("activeChannel", "Wechat");
			//添加会员
			binOLMBMBM11_BL.tran_addMemberInfo(memberAddMap);

			Map<String, Object> _params = new HashMap<String, Object>();
			_params.put("BIN_BrandInfoID", brandInfoId);
			_params.put("BIN_OrganizationInfoID", organizationInfoId);
			_params.put("MemberCode", memberCode);
			//再获取会员信息
			List<Map<String, Object>> memList = memberInfoService.getMemberInfoOAuth(_params);
			member = memList.get(0);
		}
//		//会员绑定
//		Map<String,Object> tmpMap = new HashMap<String,Object>();
//		tmpMap.put("MessageID", openId);
//		tmpMap.put("BIN_MemberInfoID", member.get("BIN_MemberInfoID"));
//		tmpMap.put("BindTime", bindTime);
//		memberInfoService.bindWechat(tmpMap);
		// 添加会员微信绑定履历
		Map<String, Object> memBindRecordMap = new HashMap<String, Object>();
		memBindRecordMap.put("organizationInfoId", organizationInfoId);
		memBindRecordMap.put("brandInfoId", brandInfoId);
		memBindRecordMap.put("memberInfoId", member.get("BIN_MemberInfoID"));
		memBindRecordMap.put("recordType", "1");
		memBindRecordMap.put("recordDateTime", bindTime);
		memBindRecordMap.put("wechatSeviceCode", originalWechatCode);
		memBindRecordMap.put("wechatId", openId);
		memBindRecordMap.put("createdBy", "cherryws");
		memBindRecordMap.put("createPGM", "MemberInfoLogic");
		memBindRecordMap.put("updatedBy", "cherryws");
		memBindRecordMap.put("updatePGM", "MemberInfoLogic");
		memberInfoService.addMemBindRecord(memBindRecordMap);
		// 添加会员绑定关系
		if(!CherryChecker.isNullOrEmpty(originalWechatCode, true)){
			Map<String, Object> memBindRelationMap = new HashMap<String, Object>();
			memBindRelationMap.put("BIN_OrganizationInfoID", organizationInfoId);
			memBindRelationMap.put("BIN_BrandInfoID", brandInfoId);
			memBindRelationMap.put("WechatSeviceCode", originalWechatCode);
			memBindRelationMap.put("WechatID", openId);
			memBindRelationMap.put("createdBy", "cherryws");
			memBindRelationMap.put("createPGM", "MemberInfoLogic");
			memBindRelationMap.put("updatedBy", "cherryws");
			memBindRelationMap.put("updatePGM", "MemberInfoLogic");
			List<Map<String, Object>> mbrList = memberInfoService.getMemBindRelation(memBindRelationMap);
			memBindRelationMap.put("ValidFlag", "1");
			if(mbrList != null && mbrList.size() > 0){ //绑定过，更新
				String memberId = ConvertUtil.getString(member.get("BIN_MemberInfoID"));
				memBindRelationMap.put("BIN_MemberInfoID", memberId);
				memberInfoService.updateMemBindRelation(memBindRelationMap);
			}else{ //未绑定过，新增
				memBindRelationMap.put("BIN_MemberInfoID", member.get("BIN_MemberInfoID"));
				memberInfoService.addMemBindRelation(memBindRelationMap);
			}
		}
		int point = 0;
		//能否获取绑定奖励积分标志（0：可以获取奖励，1：不能获取奖励）
		int rewardFlag = 0;
		String rewardPointsJson = ConvertUtil.getString(paramMap.get("RewardPoints"));
		if(!CherryChecker.isNullOrEmpty(rewardPointsJson, true)){//若存在奖励积分的参数，则需积分维护
			Map<String, Object> rewardPoints = ConvertUtil.json2Map(rewardPointsJson);
			String initialDate =  ConvertUtil.getString(member.get("InitialDate"));
			int newConfig =   ConvertUtil.getInt(rewardPoints.get("NEWCFG"));
			//initialDate 为空时，认为为新会员
			if(CherryChecker.isNullOrEmpty(initialDate, true) && newConfig == 1){
				//根据配置获取新会员的奖励积分
				point =  ConvertUtil.getInt(rewardPoints.get("NEW"));
			}else{
				//老会员根据配置获取不同等级需要赠送的积分
				String levelCode =  ConvertUtil.getString(member.get("LevelCode"));
				point = ConvertUtil.getInt(rewardPoints.get(levelCode));
			}
			if(point == 0){//当奖励的积分为0时，不需要维护积分
				rewardFlag = 1;
			}else if(rewardFlag == 0){
				//当同一个会员在同一个公众号中已经绑定过，则再次绑定不赠送积分
				Map<String, Object> recordMap = new HashMap<String, Object>();
				recordMap.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
				recordMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
				recordMap.put("WechatSeviceCode", paramMap.get("OriginalWechatCode"));
				recordMap.put("BIN_MemberInfoID", member.get("BIN_MemberInfoID"));
				List<Map<String, Object>> recordList = memberInfoService.getMemBindRecord(recordMap);
				if(null != recordList && recordList.size() > 1 ){
					rewardFlag = 1;
				}
			}
			if(rewardFlag == 0){
				//发送积分维护的信息
				//MQ需要用到的数据
				//会员信息
				String modifyPoint = ConvertUtil.getString(point);
				int brandId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_BrandInfoID")));
				int orgId = Integer.parseInt(ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID")));
				String billType = CherryConstants.MESSAGE_TYPE_PT;
				String billCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", billType);
				String ticketDateStr = bindTime;
				if (null == ticketDateStr) {
					ticketDateStr = memberInfoService.getForwardSYSDate();
				}
				//设定MQ消息DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 业务类型
				mqInfoDTO.setBillType(billType);
				// 单据号
				mqInfoDTO.setBillCode(billCode);
				// 所属品牌
				mqInfoDTO.setBrandInfoId(brandId);
				mqInfoDTO.setBrandCode(brandCode);
				// 所属组织
				mqInfoDTO.setOrganizationInfoId(orgId);
				mqInfoDTO.setOrgCode(orgCode);
				// 消息发送队列名
				mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
				// 设定消息内容
				Map<String,Object> msgDataMap = new HashMap<String,Object>();
				// 设定消息版本号
				msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
				// 设定消息命令类型
				msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
				// 设定消息数据类型
				msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
				// 设定插入到MongoDB的信息
				DBObject dbObject = new BasicDBObject();
				// 组织代码
				dbObject.put("OrgCode", orgCode);
				// 品牌代码
				dbObject.put("BrandCode", brandCode);
				// 业务类型
				dbObject.put("TradeType", billType);
				// 单据号
				dbObject.put("TradeNoIF", billCode);
				// 数据来源
				dbObject.put("Sourse", "Wecaht");
				mqInfoDTO.setDbObject(dbObject);
				// 设定消息的数据行
				Map<String,Object> dataLine = new HashMap<String,Object>();
				// 消息的主数据行
				Map<String,Object> mainData = new HashMap<String,Object>();
				// 品牌代码
				mainData.put("BrandCode", brandCode);
				// 业务类型
				mainData.put("TradeType", billType);
				//修改模式
				mainData.put("SubTradeType","2");
				//积分类型
				mainData.put("MaintainType","8"); //code 1214绑定送积分
				// 单据号
				mainData.put("TradeNoIF", billCode);
				// 数据来源
				mainData.put("Sourse", "Wechat");
				//积分维护明细数据
				List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
				Map<String,Object> detailMap = new HashMap<String,Object>();
				//会员卡号
				detailMap.put("MemberCode", memberCode);
				//修改的积分
				detailMap.put("ModifyPoint", modifyPoint);
				//理由
				detailMap.put("Reason", paramMap.get("OriginalWechatCode"));
				//业务时间
				detailMap.put("BusinessTime", ticketDateStr);
				detailDataList.add(detailMap);
				dataLine.put("MainData", mainData);
				dataLine.put("DetailDataDTOList", detailDataList);
				msgDataMap.put("DataLine", dataLine);
				mqInfoDTO.setMsgDataMap(msgDataMap);
				// 发送MQ消息
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}else{
				point = 0;
			}
		}
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("MemberID", member.get("BIN_MemberInfoID"));
		infoMap.put("MemberCode", memberCode);
		infoMap.put("Point", point);
		retMap.put("ResultMap", infoMap);
		return retMap;
	}

	@Override
	public Map getMemberSaleInfo(Map paramMap) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(paramMap.get("MemberID"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemberID是必须的");
			return retMap;
		}

		Map<String, Object> resultMap = memberInfoService.selMemSaleInfo(paramMap);
		retMap.put("ResultMap", resultMap);
		return retMap;
	}

	@Override
	public Map updateMemMobile(Map map) throws Exception {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("memCode", map.get("MemCode"));
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = memberInfoService.getMemberInfoByCode(paramMap);
		if(memberInfoMap == null) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "会员不存在");
			return retMap;
		}
		paramMap.put("memCode", map.get("NewMobile"));
		Map<String, Object> _memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(paramMap);
		if(_memberInfoMap != null) {
			retMap.put("ERRORCODE", "WSE0010");
			retMap.put("ERRORMSG", "手机号已存在");
			return retMap;
		}
		memberInfoMap.put("memCode", map.get("NewMobile"));
		memberInfoMap.put("mobilePhone", map.get("NewMobile"));
		memberInfoMap.put("organizationInfoId", map.get("BIN_OrganizationInfoID"));
		memberInfoMap.put("orgCode", map.get("OrgCode"));
		memberInfoMap.put("brandInfoId", map.get("BIN_BrandInfoID"));
		memberInfoMap.put("brandCode", map.get("BrandCode"));

		// 作成者
		memberInfoMap.put(CherryConstants.CREATEDBY, "cherryws");
		// 作成程序名
		memberInfoMap.put(CherryConstants.CREATEPGM, "MemberInfoLogic");
		// 更新者
		memberInfoMap.put(CherryConstants.UPDATEDBY, "cherryws");
		// 更新程序名
		memberInfoMap.put(CherryConstants.UPDATEPGM, "MemberInfoLogic");

		try {
			binOLMBMBM06_BL.tran_updMemMobile(memberInfoMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE0012");
			retMap.put("ERRORMSG", "更新会员失败");
			return retMap;
		}
		return retMap;
	}

	@Override
	public Map checkMember(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("memCode", map.get("MemCode"));
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = memberInfoService.getMemInfoByMemCode(paramMap);
		if(memberInfoMap != null) {
			retMap.put("ResultMap", memberInfoMap);
		}
		return retMap;
	}

	@Override
	public Map memActive(Map map) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("memCode", map.get("MemCode"));
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = memberInfoService.getMemInfoByMemCode(paramMap);
		if(memberInfoMap == null) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "会员不存在");
			return retMap;
		}
		paramMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
		paramMap.put("Active", map.get("Active"));
		paramMap.put("ActiveDate", map.get("ActiveDate"));
		paramMap.put("ActiveChannel", map.get("ActiveChannel"));
		// 作成者
		paramMap.put(CherryConstants.CREATEDBY, "cherryws");
		// 作成程序名
		paramMap.put(CherryConstants.CREATEPGM, "MemberInfoLogic");
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, "cherryws");
		// 更新程序名
		paramMap.put(CherryConstants.UPDATEPGM, "MemberInfoLogic");

		memberInfoService.updateMemActive(paramMap);

		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(map.get("BrandCode").toString());

		String billType = CherryConstants.MESSAGE_TYPE_MA;

		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("BIN_OrganizationInfoID"))),
				Integer.parseInt(ConvertUtil.getString(map.get("BIN_BrandInfoID"))), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(ConvertUtil.getString(map.get("BIN_BrandInfoID"))));
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(ConvertUtil.getString(map.get("BIN_OrganizationInfoID"))));
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);

		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MA);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("BrandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 会员卡号
		mainData.put("MemberCode", map.get("MemCode"));
		// 激活时间
		mainData.put("ActiveDate", map.get("ActiveDate"));
		// 激活途径
		mainData.put("ActiveChannel", map.get("ActiveChannel"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("OrganizationCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("BrandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 系统时间
		dbObject.put("OccurTime", map.get("ActiveDate"));
		// 修改回数
		dbObject.put("ModifyCounts", "0");
		// 数据来源
		dbObject.put("Source", map.get("ActiveChannel"));
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		try {
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		return retMap;
	}

	@Override
	public Map getMemSaleList(Map map) {
		// 检查参数
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (CherryChecker.isNullOrEmpty(map.get("MemCode"))) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MemCode是必须的");
			return retMap;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		paramMap.put("memCode", map.get("MemCode"));
		// 通过会员卡号查询会员信息
		Map<String, Object> memberInfoMap = memberInfoService.getMemInfoByMemCode(paramMap);
		if(memberInfoMap == null) {
			retMap.put("ERRORCODE", "WSE0001");
			retMap.put("ERRORMSG", "会员不存在");
			return retMap;
		}

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("memberInfoId", memberInfoMap.get("memberInfoId"));
		String startPage = (String)map.get("StartPage");
		String pageSize = (String)map.get("PageSize");
		if(startPage == null || "".equals(startPage)) {
			startPage = "1";
		}
		if(pageSize == null || "".equals(pageSize)) {
			pageSize = "20";
		}
		int start = (Integer.parseInt(startPage) - 1) * Integer.parseInt(pageSize) + 1;
		int end = start + Integer.parseInt(pageSize) - 1;
		String sort = "SaleTime desc";
		searchMap.put("SORT_ID", sort);
		searchMap.put("START", start);
		searchMap.put("END", end);

		searchMap.put("saleDateStart", map.get("SaleDateStart"));
		searchMap.put("saleDateEnd", map.get("SaleDateEnd"));
		searchMap.put("saleType", map.get("SaleType"));

		int count = memberInfoService.selMemSaleCount(searchMap);
		retMap.put("ResultTotalCNT", count);
		if(count > 0) {
			List<Map<String, Object>> retList = memberInfoService.selMemSaleList(searchMap);
			retMap.put("ResultContent", retList);
		}
		return retMap;
	}

	@Override
	public Map getMemInfoList(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		String startPage = (String)map.get("StartPage");
		String pageSize = (String)map.get("PageSize");
		String sort = (String)map.get("Sort");
		String query = (String)map.get("Query");
		if(query == null || "".equals(query)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数Query是必须的");
			return retMap;
		}
		Map<String, Object> queryMap = ConvertUtil.json2Map(query);
		if(startPage == null || "".equals(startPage)) {
			startPage = "1";
		}
		if(pageSize == null || "".equals(pageSize)) {
			pageSize = "20";
		}
		if(sort == null || "".equals(sort)) {
			sort = "memId desc";
		}
		int start = (Integer.parseInt(startPage) - 1) * Integer.parseInt(pageSize) + 1;
		int end = start + Integer.parseInt(pageSize) - 1;
		searchMap.put("SORT_ID", sort);
		searchMap.put("START", start);
		searchMap.put("END", end);
		searchMap.putAll(queryMap);
		int count = memberInfoService.selMemInfoCount(searchMap);
		retMap.put("ResultTotalCNT", count);
		if(count > 0) {
			List<Map<String, Object>> retList = memberInfoService.selMemInfoList(searchMap);
			if(retList != null && !retList.isEmpty()) {
				try {
					String brandCode = String.valueOf(map.get("BrandCode"));
					for (int i = 0; i < retList.size(); i++) {
						Map<String, Object> tmp = retList.get(i);
						tmp.put("mobilePhone", CherrySecret.decryptData(brandCode, (String)tmp.get("mobilePhone")));
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					retMap.put("ERRORCODE", "WSE9999");
					retMap.put("ERRORMSG", "处理过程中发生未知异常");
					return retMap;
				}
			}
			retMap.put("ResultContent", retList);
		}
		return retMap;
	}

	/**
	 * 绑定或新增会员（珀莱雅需求）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map tran_BindOrCreateWechat(Map paramMap) throws Exception {
		String memberId = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		String brandInfoId =  ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
		String organizationInfoId =  ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
		String orgCode = ConvertUtil.getString(paramMap.get("OrganizationCode"));
		String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
		String openId = ConvertUtil.getString(paramMap.get("OpenID"));
		String mobilePhone = ConvertUtil.getString(paramMap.get("MobilePhone"));
		String bindTime = ConvertUtil.getString(paramMap.get("BindTime"));
		String counterCode = ConvertUtil.getString(paramMap.get("CounterCode"));
		//检查参数
		if (CherryChecker.isNullOrEmpty(openId)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数OpenID是必须的");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(mobilePhone)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(bindTime)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime是必须的");
			return retMap;
		}
		if(!CherryChecker.checkDate(bindTime, "yyyy-MM-dd HH:mm:ss")) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数BindTime的格式必须为yyyy-MM-dd HH:mm:ss");
			return retMap;
		}
		//检查手机号是否存在
		Map<String, Object> mobileMap = new HashMap<String, Object>();
		mobileMap.put("BIN_BrandInfoID", brandInfoId);
		mobileMap.put("BIN_OrganizationInfoID", organizationInfoId);
		mobileMap.put("MobilePhone", mobilePhone);
		List<Map<String, Object>> memInfoList = memberInfoService.getMemInfoListByMobilePhone(mobileMap);
		//根据手机号查询到会员存在
		if(null != memInfoList && memInfoList.size() > 0) {
			//如果存在多条数据，则返回错误
			if(memInfoList.size() > 1) {
				retMap.put("ERRORCODE", "WSE9991");
				retMap.put("ERRORMSG", "参数MobilePhone查询到多个会员");
				return retMap;
			} else {
				//只有一条会员数据，作绑定会员处理
				Map<String, Object> memBindMap = new HashMap<String, Object>();
				Map<String, Object> member = memInfoList.get(0);
				memberId = ConvertUtil.getString(member.get("BIN_MemberInfoID"));
				memBindMap.put("MessageID", openId);
				memBindMap.put("BIN_MemberInfoID", memberId);
				memBindMap.put("BindTime", bindTime);
				int cnt = memberInfoService.updateMessageId(memBindMap);
				if (cnt != 1) {
					//会员绑定失败
					retMap.put("ERRORCODE", "WSE9999");
					retMap.put("ERRORMSG", "会员绑定失败");
					return retMap;
				}
			}
		} else {
			//作会员注册处理，将手机号作为卡号进行判断
			Map<String, Object> memCodeMap = new HashMap<String, Object>();
			memCodeMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
			memCodeMap.put(CherryConstants.BRANDINFOID, brandInfoId);
			memCodeMap.put("memCode", mobilePhone);
			//通过会员卡号查询会员信息
			Map<String, Object> memInfoByMemCodeMap = memberInfoService.getMemInfoByMemCode(memCodeMap);
			String memInfoRegFlg = null;
			String memberInfoId = null;
			if(null != memInfoByMemCodeMap) {
				memInfoRegFlg = String.valueOf(memInfoByMemCodeMap.get("MemInfoRegFlg"));
				memberInfoId = String.valueOf(memInfoByMemCodeMap.get("memberInfoId"));
			}
			if(null != memInfoRegFlg && "0".equals(memInfoRegFlg)) {
				retMap.put("ERRORCODE", "WSE0010");
				retMap.put("ERRORMSG", "会员卡号为参数MobilePhone的会员已存在，会员注册失败");
				return retMap;
			} else {//会员信息不存在或者假登录情况，做会员注册处理
				Map<String, Object> memAddMap = new HashMap<String, Object>();
				memAddMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
				memAddMap.put(CherryConstants.BRANDINFOID, brandInfoId);
				memAddMap.put(CherryConstants.ORG_CODE, orgCode);
				memAddMap.put(CherryConstants.BRAND_CODE, brandCode);
				memAddMap.put(CherryConstants.CREATEDBY, "cherryws");
				memAddMap.put(CherryConstants.CREATEPGM, "MemberInfoLogic");
				memAddMap.put(CherryConstants.UPDATEDBY, "cherryws");
				memAddMap.put(CherryConstants.UPDATEPGM, "MemberInfoLogic");
				if(null != memberInfoId && !"".equals(memberInfoId)) {
					memAddMap.put("memberInfoId", memberInfoId);
				}
				if(!CherryChecker.isNullOrEmpty(counterCode)){
					memAddMap.put("organizationCode", counterCode);
					Map<String, Object> counterInfo = memberInfoService.selCounterInfo(memAddMap);
					if(counterInfo == null || counterInfo.isEmpty()){
						retMap.put("ERRORCODE", "WSE0003");
						retMap.put("ERRORMSG", "未找到指定的柜台");
						return retMap;
					}else{
						memAddMap.put("organizationId", counterInfo.get("organizationId"));
						String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
						if(counterKind != null && "1".equals(counterKind)) {
							memAddMap.put("testType", "0");
						} else {
							memAddMap.put("testType", "1");
						}
					}
				}
				memAddMap.put("memCode", mobilePhone);//手机号作为会员卡号
				memAddMap.put("joinDate", bindTime);
				memAddMap.put("mobilePhone", mobilePhone);
				memAddMap.put("channelCode", "wechat");
				memAddMap.put("messageId", openId);
				memAddMap.put("wechatBindTime", bindTime);
				memAddMap.put("active", "1");
				memAddMap.put("activeDate", bindTime);
				memAddMap.put("activeChannel", "Wechat");
				//添加会员
				try {
					memberId = binOLMBMBM11_BL.tran_addMemberInfo(memAddMap);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					logger.error("会员注册失败：memAddMap = " + memAddMap.toString());
					retMap.put("ERRORCODE", "WSE9999");
					retMap.put("ERRORMSG", "会员注册失败");
					return retMap;
				}
//				//绑定会员
//				Map<String, Object> memBindMap = new HashMap<String, Object>();
//				memBindMap.put("MessageID", openId);
//				memBindMap.put("BIN_MemberInfoID", memberId);
//				memBindMap.put("BindTime", bindTime);
//				int cnt = memberInfoService.updateMessageId(memBindMap);
//				if (cnt != 1) {
//					//会员绑定失败
//					retMap.put("ERRORCODE", "WSE9999");
//					retMap.put("ERRORMSG", "会员绑定失败");
//					return retMap;
//				}
			}
		}
		//添加会员微信绑定履历
		Map<String, Object> memBindRecordMap = new HashMap<String, Object>();
		memBindRecordMap.put("organizationInfoId", organizationInfoId);
		memBindRecordMap.put("brandInfoId", brandInfoId);
		memBindRecordMap.put("memberInfoId", memberId);
		memBindRecordMap.put("wechatId", openId);
		memBindRecordMap.put("recordType", "1");
		memBindRecordMap.put("recordDateTime", bindTime);
		memBindRecordMap.put("createdBy", "WeChat");
		memBindRecordMap.put("createPGM", "MemberInfoLogic");
		memBindRecordMap.put("updatedBy", "WeChat");
		memBindRecordMap.put("updatePGM", "MemberInfoLogic");
		memberInfoService.addMemBindRecord(memBindRecordMap);

		//发送会员资料MQ消息
		try {
			this.sendMemberMQ(paramMap);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			this.sendPtMsg(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		retMap.put("ERRORCODE", "0");
		retMap.put("ERRORMSG", "执行成功");
		return retMap;
	}

	/**
	 * 注册潜在会员
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map tran_AddPotentialMem(Map paramMap) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String openID = (String)paramMap.get("OpenID");
		String name = (String)paramMap.get("Name");
		String mobilePhone = (String)paramMap.get("MobilePhone");
		String registerTime = (String)paramMap.get("RegisterTime");
		String point = (String)paramMap.get("Point");
		String expiryTime = (String)paramMap.get("ExpiryTime");
		String benefitsDetail = (String)paramMap.get("BenefitsDetail");
		if(CherryChecker.isNullOrEmpty(openID)) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数OpenID是必须的");
			return retMap;
		} else if(openID.length() > 50) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数OpenID不能超过50位");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(name)) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数Name是必须的");
			return retMap;
		} else if(name.length() > 40) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数Name不能超过40位");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(mobilePhone)) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return retMap;
		} else if(!CherryChecker.isPhoneValid(mobilePhone, "(^1\\d{10}$)")) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数MobilePhone格式不正确");
			return retMap;
		}
		if(CherryChecker.isNullOrEmpty(registerTime)) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数RegisterTime是必须的");
			return retMap;
		} else if(!CherryChecker.checkDate(registerTime, "yyyy-MM-dd HH:mm:ss")) {
			retMap.put("ERRORCODE", "WSE0031");
			retMap.put("ERRORMSG", "参数RegisterTime格式不正确");
			return retMap;
		}
		if(!CherryChecker.isNullOrEmpty(point)) {
			if(!CherryChecker.isFloatValid(point, 14, 2)) {
				retMap.put("ERRORCODE", "WSE0031");
				retMap.put("ERRORMSG", "参数Point格式不正确");
				return retMap;
			}
		}
		if(!CherryChecker.isNullOrEmpty(expiryTime)) {
			if(!CherryChecker.checkDate(expiryTime, "yyyy-MM-dd HH:mm:ss")) {
				retMap.put("ERRORCODE", "WSE0031");
				retMap.put("ERRORMSG", "参数ExpiryTime格式不正确");
				return retMap;
			}
		}
		if(!CherryChecker.isNullOrEmpty(benefitsDetail)) {
			if(benefitsDetail.length() > 1000) {
				retMap.put("ERRORCODE", "WSE0031");
				retMap.put("ERRORMSG", "参数BenefitsDetail不能超过1000位");
				return retMap;
			}
		}
		int count = memberInfoService.getPotentialMemInfoCountByMobile(paramMap);
		if(count > 0) {
			retMap.put("ERRORCODE", "WSE0086");
			retMap.put("ERRORMSG", "手机号已经被注册");
			return retMap;
		} else {
			count = memberInfoService.getPotentialMemInfoCountByOpenID(paramMap);
			if(count > 0) {
				retMap.put("ERRORCODE", "WSE0087");
				retMap.put("ERRORMSG", "微信号已经被注册");
				return retMap;
			} else {
				paramMap.put("IsMember", "0");
				paramMap.put("IsExchange", "0");
				paramMap.put("createdBy", "WeChat");
				paramMap.put("createPGM", "MemberInfoLogic");
				paramMap.put("updatedBy", "WeChat");
				paramMap.put("updatePGM", "MemberInfoLogic");
				memberInfoService.addPotentialMemInfo(paramMap);
			}
		}

		retMap.put("ERRORCODE", "0");
		return retMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map tran_memberPaperAnswer(Map map) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			//验证传输参数有效性
			resultMap = validMemberPaperAnswerParam(map);
			if(!CherryChecker.isNullOrEmpty(resultMap) && !resultMap.isEmpty()){
				return resultMap;
			}
			getParam(map);
			//检索会员，并获取Id
			Map<String, Object> memMap = memberInfoService.getMemInfoByMemCode(map);
			if (memMap == null || memMap.isEmpty()){
				resultMap.put("ERRORCODE", "EMB1807");
				resultMap.put("ERRORMSG", "未找到指定的会员");
				return resultMap;
			}
			map.put("memberInfoId",memMap.get("memberInfoId"));

			//检索柜台，并获取组织Id
			if (!CherryChecker.isNullOrEmpty(map.get("CounterCode"))){
				map.put("organizationCode",map.get("CounterCode"));
				Map<String, Object> counterMap = memberInfoService.selCounterInfo(map);
				if (counterMap == null || counterMap.isEmpty()){
					resultMap.put("ERRORCODE", "EMB1808");
					resultMap.put("ERRORMSG", "未找到指定的柜台");
					return resultMap;
				}
				map.put("organizationId",counterMap.get("organizationId"));
			}

			//根据会员Id和问卷Id查询答案记录
			List<Map<String,Object>> answerList = memberInfoService.getPaperAnswerByPaperId(map);
			int paperAnswerId = 0;
			map.put("paperType","1");

			List<Map<String,Object>> detailList = (List) map.get("DetailList");
			if (CherryUtil.isBlankList(answerList)){
				//插入答卷主表
				paperAnswerId = memberInfoService.insertPaperAnswer(map);
				map.put("paperAnswerId",paperAnswerId);
				//转换答案List
				for (Map detailMap : detailList){
					detailMap.putAll(map);
					//获取答案对应的问题ID
					Map paperQuestionMap = memberInfoService.getPaperQuestion(detailMap);
					if (paperQuestionMap == null || paperQuestionMap.isEmpty()){
						resultMap.put("ERRORCODE", "EMB1809");
						resultMap.put("ERRORMSG", "题号为" + detailMap.get("QuestionNo") + "的问题不存在！");
						return resultMap;
					}
					detailMap.put("paperQuestionId",paperQuestionMap.get("BIN_PaperQuestionID"));
					//单选题直接将问题的文本存进数据库，为了和现在的会员新增，修改保持一致。比较坑
					if("1".equals(paperQuestionMap.get("QuestionType"))){
						for(int j = 65; j <= 84; j++) {
							char ca = (char)j;
							String tempValue = String.valueOf(ca);
							String value = (String)paperQuestionMap.get("option"+ca);
							if(tempValue.equals(ConvertUtil.getString(detailMap.get("Answer")))) {
								detailMap.put("answer",value);
								break;
							}
						}
					}else if ("1".equals(paperQuestionMap.get("QuestionType")) || "2".equals(paperQuestionMap.get("QuestionType"))){//将多选题的字母转成20位的二进制字符串
						detailMap.put("answer",ConvertUtil.letterToBinary((String)detailMap.get("Answer")));
					}else {
						detailMap.put("answer",detailMap.get("Answer"));
					}
				}
				//批量插入问卷答案
				memberInfoService.insertPaperAnswerDetail(detailList);

				//会员第一次填写问卷，奖励积分
				String businessTime = memberInfoService.getSYSDate();
				String point = binOLCM14_BL.getConfigValue("1397",String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)));
				map.put("MaintType","2");
				map.put("MaintPoint",point);
				map.put("MaintainType","1");
				map.put("BusinessTime",businessTime);
				map.put("Comment","微信会员完善信息问卷奖励积分");
				modifyMemberPoint(map);
			}else{
				paperAnswerId = Integer.valueOf(String.valueOf(answerList.get(0).get("BIN_PaperAnswerID")));
				//该问卷已被该会员完成过，更新答卷主表
				map.put("paperAnswerId",paperAnswerId);
				memberInfoService.updatePaperAnswer(map);

				for (Map detailMap : detailList){
					detailMap.putAll(map);
					//获取答案对应的问题ID
					Map paperQuestionMap = memberInfoService.getPaperQuestion(detailMap);
					if (paperQuestionMap == null || paperQuestionMap.isEmpty()){
						resultMap.put("ERRORCODE", "EMB1809");
						resultMap.put("ERRORMSG", "题号为" + detailMap.get("QuestionNo") + "的问题不存在！");
						return resultMap;
					}
					detailMap.put("paperQuestionId",paperQuestionMap.get("BIN_PaperQuestionID"));
					//获取答案对应的BIN_PaperAnswerDetailID
					for (Map answerMap : answerList){
						if (answerMap.get("BIN_PaperQuestionID").equals(paperQuestionMap.get("BIN_PaperQuestionID"))){
							detailMap.put("BIN_PaperAnswerDetailID",answerMap.get("BIN_PaperAnswerDetailID"));
						}
					}

					//单选题直接将问题的文本存进数据库，为了和现在的会员新增，修改保持一致。比较坑
					if("1".equals(paperQuestionMap.get("QuestionType"))){
						for(int j = 65; j <= 84; j++) {
							char ca = (char)j;
							String tempValue = String.valueOf(ca);
							String value = (String)paperQuestionMap.get("option"+ca);
							if(tempValue.equals(ConvertUtil.getString(detailMap.get("Answer")))) {
								detailMap.put("answer",value);
								break;
							}
						}
					}else if ("2".equals(paperQuestionMap.get("QuestionType"))){//将多选题的字母转成20位的二进制字符串
						detailMap.put("answer",ConvertUtil.letterToBinary((String)detailMap.get("Answer")));
					}else {
						detailMap.put("answer",detailMap.get("Answer"));
					}
				}
				//批量更新问卷答案
				memberInfoService.updatePaperAnswerDetail(detailList);
			}
		} catch (Exception e) {
			logger.error("会员答卷接口执行失败。",e);
			logger.error(e.getMessage(),e);
			logger.error("WS ERROR TradeType:"+ ConvertUtil.getString(map.get("TradeType")));
			logger.error("WS ERROR paramData:"+ map.toString());
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map getMemberPaperAnswer(Map map) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			//验证传输参数有效性
			resultMap = validGetMemberPaperAnswerParam(map);
			if(!CherryChecker.isNullOrEmpty(resultMap) && !resultMap.isEmpty()){
				return resultMap;
			}
			map.put("memCode",map.get("MemberCode"));
			map.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			map.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			//检索会员，并获取Id
			Map<String, Object> memMap = memberInfoService.getMemInfoByMemCode(map);
			if (memMap == null || memMap.isEmpty()){
				resultMap.put("ERRORCODE", "EMB1903");
				resultMap.put("ERRORMSG", "未找到指定的会员");
				return resultMap;
			}
			map.put("memberInfoId",memMap.get("memberInfoId"));
			//根据会员Id和问卷Id查询答案记录
			List<Map<String,Object>> answerList = memberInfoService.getPaperAnswerByPaperId(map);
			if (CherryUtil.isBlankList(answerList)){
				resultMap.put("ERRORCODE", "EMB1904");
				resultMap.put("ERRORMSG", "未找到对应的答案");
				return resultMap;
			}else{
				List<Map<String,Object>> answers = new ArrayList<Map<String, Object>>();
				for (Map answerMap : answerList){
					Map<String,Object> resMap = new HashMap<String, Object>();
					resMap.put("QuestionNo",answerMap.get("QuestionNo"));
					if ("1".equals(answerMap.get("QuestionType")) || "2".equals(answerMap.get("QuestionType"))){
						resMap.put("Answer",ConvertUtil.binaryToLetter((String)answerMap.get("Answer")));
					}else {
						resMap.put("Answer",answerMap.get("Answer"));
					}
					answers.add(resMap);
				}
				resultMap.put("ResultContent", answers);
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("获取会员答卷接口执行失败。",e);
			logger.error(e.getMessage(),e);
			logger.error("WS ERROR TradeType:"+ ConvertUtil.getString(map.get("TradeType")));
			logger.error("WS ERROR paramData:"+ map.toString());
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			return resultMap;
		}
	}

	private Map<String,Object> validGetMemberPaperAnswerParam(Map map) {
		Map<String,Object> resultMap=new HashMap<String, Object>();
		//验证会员卡号是否为空
		if(CherryChecker.isNullOrEmpty(map.get("MemberCode"))){
			resultMap.put("ERRORCODE", "EMB1901");
			resultMap.put("ERRORMSG", "参数MemberCode是必须的！");
			return resultMap;
		}
		//验证PaperID是否为空
		if(CherryChecker.isNullOrEmpty(map.get("PaperID"))){
			resultMap.put("ERRORCODE", "EMB1902");
			resultMap.put("ERRORMSG", "参数PaperID是必须的！");
			return resultMap;
		}
		return resultMap;
	}

	private void getParam(Map map) {
		map.put("memCode",map.get("MemberCode"));
		map.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
		map.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
		// 作成者
		map.put(CherryConstants.CREATEDBY, "cherryws");
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "MemberPaperAnswer");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "cherryws");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "MemberPaperAnswer");
	}

	private Map<String,Object> validMemberPaperAnswerParam(Map map) {
		Map<String,Object> resultMap=new HashMap<String, Object>();
		//验证会员卡号是否为空
		if(CherryChecker.isNullOrEmpty(map.get("MemberCode"))){
			resultMap.put("ERRORCODE", "EMB1801");
			resultMap.put("ERRORMSG", "参数MemberCode是必须的！");
			return resultMap;
		}
		//验证PaperID是否为空
		if(CherryChecker.isNullOrEmpty(map.get("PaperID"))){
			resultMap.put("ERRORCODE", "EMB1802");
			resultMap.put("ERRORMSG", "参数PaperID是必须的！");
			return resultMap;
		}
		//验证Sourse是否为空
		if(CherryChecker.isNullOrEmpty(map.get("Sourse"))){
			resultMap.put("ERRORCODE", "EMB1803");
			resultMap.put("ERRORMSG", "参数Sourse是必须的！");
			return resultMap;
		}

		List<Map<String,Object>> detailList = null;
		try{
			detailList = (List) map.get("DetailList");
		}catch(Exception ex){
			//数据格式错误
			resultMap.put("ERRORCODE", "EMB1804_0");
			resultMap.put("ERRORMSG", "参数DetailList是格式错误！");
			return resultMap;
		}

		//验证DetailList是否为空
		if(CherryUtil.isBlankList(detailList)){
			resultMap.put("ERRORCODE", "EMB1804");
			resultMap.put("ERRORMSG", "参数DetailList是必须的！");
			return resultMap;
		}

		for (Map detailMap : detailList){
			//验证QuestionNo是否为空
			if(CherryChecker.isNullOrEmpty(detailMap.get("QuestionNo"))){
				resultMap.put("ERRORCODE", "EMB1805");
				resultMap.put("ERRORMSG", "参数QuestionNo是必须的！");
				return resultMap;
			}
			//验证Answer是否为空
			if(CherryChecker.isNullOrEmpty(detailMap.get("Answer"))){
				resultMap.put("ERRORCODE", "EMB1806");
				resultMap.put("ERRORMSG", "参数Answer是必须的！");
				return resultMap;
			}
		}
		return resultMap;
	}
}
