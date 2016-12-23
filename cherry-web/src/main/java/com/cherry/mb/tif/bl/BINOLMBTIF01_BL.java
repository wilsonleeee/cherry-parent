package com.cherry.mb.tif.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.BindResult;
import com.cherry.cm.cmbeans.MemQueryResult;
import com.cherry.cm.cmbeans.Member;
import com.cherry.cm.cmbeans.QueryResult;
import com.cherry.cm.cmbeans.RegisterResult;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.mb.tif.interfaces.BINOLMBTIF01_IF;
import com.cherry.mb.tif.service.BINOLMBTIF01_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.taobao.api.request.TmallMeiCrmCallbackPointChangeRequest;
import com.taobao.api.response.TmallMeiCrmCallbackPointChangeResponse;

public class BINOLMBTIF01_BL implements BINOLMBTIF01_IF {
	
	private List<Map<String, Object>> sourceList;
	
	private List<Map<String, Object>> brandList;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLMBTIF01_Service binOLMBTIF01_Service;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLMBTIF01_BL.class.getName());

	@Override
	public Map<String, Object> getDataSource(String brandName) {
		if (CherryChecker.isNullOrEmpty(brandName)) {
			return null;
		}
		if (null == sourceList || sourceList.isEmpty()) {
			sourceList = binOLMBTIF01_Service.getConfBrandInfoList(new HashMap());
		}
		if (null != sourceList) {
			for(Map<String, Object> sourceInfo : sourceList) {
				if (brandName.equals(sourceInfo.get("brandName"))) {
					Map<String, Object> rstMap = new HashMap<String, Object>();
					rstMap.put("sourceName", sourceInfo.get("sourceName"));
					rstMap.put("brandCode", sourceInfo.get("brandCode"));
					return rstMap;
				}
			}
		}
		return null;
	}
	
	@Override
	public Map<String, Object> getBrandInfo(String brandCode) {
		if (CherryChecker.isNullOrEmpty(brandCode)) {
			return null;
		}
		if (null == brandList || brandList.isEmpty()) {
			brandList = binOLMBTIF01_Service.getBrandInfoList(new HashMap());
		}
		if (null != brandList) {
			for(Map<String, Object> brandInfo : brandList) {
				if (brandCode.equals(brandInfo.get("brandCode"))) {
					Map<String, Object> rstMap = new HashMap<String, Object>();
					rstMap.put("organizationInfoId", brandInfo.get("organizationInfoId"));
					rstMap.put("brandInfoId", brandInfo.get("brandInfoId"));
					rstMap.put("brandCode", brandInfo.get("brandCode"));
					rstMap.put("orgCode", brandInfo.get("orgCode"));
					return rstMap;
				}
			}
		}
		return null;
	}
	
	private void memberSetting(Map<String, Object> map, Map<String, Object> memInfo, Member member) {
		int level = Integer.parseInt(memInfo.get("level").toString());
		if (0 == level) {
			map.put("brandInfoID", map.get("brandInfoId"));
			map.put("organizationInfoID", map.get("organizationInfoId"));
			level = binOLCM31_BL.getTmallDefaultLevel(map);
			level = level > 0? level : 1;
		}
		member.setLevel((long) level);
		double point = Double.parseDouble(memInfo.get("point").toString());
		long pointLong = 0L;
		if (point > 0) {
			pointLong = (long) point;
		}
		member.setPoint(pointLong);
		String mobilez = (String) map.get("mobile");
		if (!CherryChecker.isNullOrEmpty(mobilez)) {
			member.setMobile(mobilez);
		} else {
			String mobile = (String) memInfo.get("mPhone");
			if (null != mobile) {
				try {
					mobile = CherrySecret.decryptData((String) map.get("brandCode"), mobile);
				} catch (Exception e) {
					logger.error("******************************手机解密异常：" + e.getMessage(),e);
				}
			}
			member.setMobile(null == mobile? "" : mobile);
		}
	}

	@Override
	public QueryResult checkBind(Map<String, Object> map) {
		QueryResult rst = new QueryResult();
		List<Map<String, Object>> bindList = binOLMBTIF01_Service.getTmallBindList(map);
		// 可注册
		if (null == bindList || bindList.isEmpty()) {
			rst.setBind_code("E04");
		} else if (bindList.size() == 1){
			Map<String, Object> memInfo = (Map<String, Object>) bindList.get(0);
			String mobile = (String) memInfo.get("mPhone");
			if (CherryChecker.isNullOrEmpty(mobile)) {
				rst.setBind_code("E04");
			} else {
				rst.setBindable(true);
				memberSetting(map, memInfo, rst.getMember());
			}
		} else {
			rst.setBind_code("E03");
			logger.error(MTM00003 + map.get("mix_mobile"));
		}
		return rst;
	}

	@Override
	public BindResult tran_bind(Map<String, Object> map) throws Exception {
		String mixMobile = (String) map.get("mix_mobile");
		String brandCode = (String) map.get("brandCode");
		String mobilez = (String) map.get("mobile");
		boolean isNoMobile = CherryChecker.isNullOrEmpty(mobilez);
		BindResult rst = new BindResult();
		if (CherryChecker.isNullOrEmpty(mixMobile)) {
			String mixKey = TmallKeys.getMixKey(brandCode);
			if (isNoMobile) {
				logger.error("************************绑定请求参数有误！密文和明文手机号都为空");
				rst.setBind_code("E01");
				return rst;
			}
			mixMobile = DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobilez + mixKey));
			map.put("mix_mobile", mixMobile);
		}
		String type = (String) map.get("type");
		String taobao_nick = (String) map.get("taobao_nick");
		boolean isNick = !CherryChecker.isNullOrEmpty(taobao_nick);
		List<Map<String, Object>> bindList = binOLMBTIF01_Service.getTmallBindList(map);
		if (null == bindList || bindList.isEmpty()) {
			logger.error(MTM00004 + mixMobile);
			rst.setBind_code("E01");
			return rst;
		} else if (bindList.size() == 1) {
			Map<String, Object> bindMap = bindList.get(0);
			map.put("memberInfoId", bindMap.get("memberInfoId"));
			upCommMap(map);
			String memInfoRegFlg = String.valueOf(bindMap.get("memInfoRegFlg"));
			// 正式会员
			boolean isMember = "0".equals(memInfoRegFlg);
			// 绑定
			if ("1".equals(type)) {
				boolean isNeedMerge = TmallKeys.isNeedMerge(brandCode);
				boolean sendRecalFlag = false;
				if (isNeedMerge && isNick && isMember) {
					map.put("channelCode", "Tmall");
					map.put("UPNICKNAME", "1");
					List<Map<String, Object>> sameNickMemList = binOLMBTIF01_Service.getSameNickMemList(map);
					if (null != sameNickMemList && !sameNickMemList.isEmpty()) {
						String memCode = binOLCM31_BL.getMemCard(map);
						boolean needRecal = false;
						for (Map<String, Object> sameNickMap : sameNickMemList) {
							upCommMap(sameNickMap);
							sameNickMap.put("memCode", memCode);
							sameNickMap.put("memberInfoId", bindMap.get("memberInfoId"));
							// 删除假登陆会员信息
							binOLMBTIF01_Service.delPreMemberInfo(sameNickMap);
							// 删除假登陆会员卡信息
							binOLMBTIF01_Service.delPreMemCode(sameNickMap);
							// 删除假登陆会员积分信息 
							binOLMBTIF01_Service.delPreMemPoint(sameNickMap);
							// 变更会员使用化妆次数积分明细记录的假登陆会员的ID
							int uCount = binOLMBTIF01_Service.updateTmMemUsedMemId(sameNickMap);
							// 变更积分变化记录的假登陆会员的ID
							int zCount = binOLMBTIF01_Service.updateTmPointChangeMemId(sameNickMap);
							// 变更规则执行履历记录的假登陆会员的ID
							int rCount = binOLMBTIF01_Service.updateTmRuleRecordMemId(sameNickMap);
							// 变更销售表的假登陆会员的ID
							int sCount = binOLMBTIF01_Service.updateTmSaleRecordMemId(sameNickMap);
							// 变更订单表的假登陆会员的ID
							binOLMBTIF01_Service.updateESOrderMainMemId(sameNickMap);
							if (uCount > 0 || zCount > 0 || rCount > 0 || sCount > 0) {
								needRecal = true;
							}
						}
						if (needRecal) {
							// 查询新卡对应的会员产生的最早业务时间
							String reCalcDate = binOLMBTIF01_Service.getMinTicketDate(map);
							if (null != reCalcDate) {
								map.put("reCalcDate", reCalcDate);
								map.put("reCalcType", "0");
								// 插入重算信息表
								binOLMBTIF01_Service.insertReCalcInfo(map);
								sendRecalFlag = true;
							}
						}
					}
				}
				if (!isNick) {
					map.put("taobao_nick", bindMap.get("taobaoNick"));
				}
				map.put("tmallBindTime", bindMap.get("tmallBindTime"));
				String memberMode = TmallKeys.getMemberModel(brandCode);
				// 会员模式：通用版本
				boolean isCommMem = "1".equals(memberMode);
				if (isCommMem) {
					map.put("UPNICKNAME", "1");
				}
				int result = binOLMBTIF01_Service.updateBindInfo(map);
				if (isCommMem) {
					map.remove("UPNICKNAME");
				}
				if (result == 0) {
					rst.setBind_code("E01");
					logger.error(MTM00006 + mixMobile);
				} else {
					rst.setBind_code("SUC");
					memberSetting(map, bindMap, rst.getMember());
				}
				if (sendRecalFlag) {
					sendReCalcMsg(map);
				}
				// 解绑
			} else if ("2".equals(type)) {
				String mPhone = (String) bindMap.get("mPhone");
				if (!CherryChecker.isNullOrEmpty(mPhone)) {
					mPhone = CherrySecret.decryptData(brandCode, mPhone);
					if (!CherryChecker.isNullOrEmpty(mPhone)) {
						String mixKey = TmallKeys.getMixKey(brandCode);
						if (null != mixKey) {
							map.put("newTmallMixMobile", DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mPhone + mixKey)));
						}
					}
				} else {
					map.put("newTmallMixMobile", mixMobile);
					if (!isNoMobile) {
						map.put("mobileNew", mobilez);
					}
				}
				String taobaoNick = (String) bindMap.get("taobaoNick");
				if (CherryChecker.isNullOrEmpty(taobaoNick, true) && isNick) {
					taobaoNick = taobao_nick;
				}
				map.put("hisTaobaoNick", taobaoNick);
				String memberMode = TmallKeys.getMemberModel(brandCode);
				// 会员模式：通用版本
				boolean isCommMem = "1".equals(memberMode);
				if (isCommMem) {
					map.put("UPNICKNAME", "1");
				}
				int result = binOLMBTIF01_Service.updateUnbindInfo(map);
				if (isCommMem) {
					map.remove("UPNICKNAME");
				}
				if (result == 0) {
					rst.setBind_code("E01");
					logger.error(MTM00007 + mixMobile);
				} else {
					rst.setBind_code("SUC");
					memberSetting(map, bindMap, rst.getMember());
				}
			} else {
				rst.setBind_code("E01");
				logger.error(MTM00005 + mixMobile);
			}
			return rst;
		} else {
			logger.error(MTM00019 + mixMobile);
			rst.setBind_code("E01");
			return rst;
		}
	}
	
	private void upCommMap(Map<String, Object> map) {
		map.put(CherryConstants.CREATEPGM, "BINOLMBTIF01");
		map.put(CherryConstants.CREATEDBY, "BINOLMBTIF01");
		map.put(CherryConstants.UPDATEPGM, "BINOLMBTIF01");
		map.put(CherryConstants.UPDATEDBY, "BINOLMBTIF01");
	}

	@Override
	public MemQueryResult getMemberInfo(Map<String, Object> map) throws Exception {
		MemQueryResult rst = new MemQueryResult();
		String mixMobile = (String) map.get("mix_mobile");
		String mobilez = (String) map.get("mobile");
		String brandCode = (String) map.get("brandCode");
		boolean isNoMobile = CherryChecker.isNullOrEmpty(mobilez);
		if (CherryChecker.isNullOrEmpty(mixMobile)) {
			String mixKey = TmallKeys.getMixKey(brandCode);
			if (isNoMobile) {
				logger.error("************************会员查询请求参数有误！密文和明文手机号都为空");
				rst.setQuery_code("E04");
				return rst;
			}
			mixMobile = DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobilez + mixKey));
			map.put("mix_mobile", mixMobile);
		}
		List<Map<String, Object>> bindList = binOLMBTIF01_Service.getTmallBindList(map);
		if (null == bindList || bindList.isEmpty()) {
			rst.setQuery_code("E01");
		} else if (bindList.size() == 1){
			Map<String, Object> bindMap = bindList.get(0);
			if (CherryChecker.isNullOrEmpty(bindMap.get("tmallBindTime"))) {
				rst.setQuery_code("E02");
			} else {
				rst.setQuery_code("SUC");
				memberSetting(map, bindMap, rst.getMember());
			}
		} else {
			 rst.setQuery_code("E03");
			 logger.error(MTM00021 + mixMobile);
		}
		return rst;
	}

	@Override
	public RegisterResult tran_register(Map<String, Object> map) throws Exception {
		String mixMobile = (String) map.get("mix_mobile");
		String taobao_nick = (String) map.get("taobao_nick");
		boolean isNick = !CherryChecker.isNullOrEmpty(taobao_nick);
		RegisterResult rst = new RegisterResult();
		upCommMap(map);
		try {
			List<Map<String, Object>> bindList = binOLMBTIF01_Service.getTmallBindList(map);
			if (null != bindList && !bindList.isEmpty()) {
				if (bindList.size() == 1) {
					Map<String, Object> bindMap = bindList.get(0);
					if (!isNick) {
						map.put("taobao_nick", bindMap.get("taobaoNick"));
					}
					map.put("tmallBindTime", bindMap.get("tmallBindTime"));
					map.put("memberInfoId", bindMap.get("memberInfoId"));
					int result = binOLMBTIF01_Service.updateBindInfo(map);
					if (result == 0) {
						rst.setRegister_code("E01");
						logger.error(MTM00022 + mixMobile);
						return rst;
					}
					rst.setRegister_code("SUC");
					memberSetting(map, bindMap, rst.getMember());
					rst.getMember().setMobile("");
					return rst;
				} 
				rst.setRegister_code("E01");
				logger.error(MTM00022 + mixMobile);
				return rst;
			}
			int registId = binOLMBTIF01_Service.addMemRegisterInfo(map);
			map.put("memInfoRegFlg", "1");
			map.put("memberCode", "TM" + String.format("%010d", registId));
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				String[] arr = tmallCounters.split(",");
				map.put("counterCodeBelong", arr[0]);
			}
			String brandCode = (String) map.get("brandCode");
			String memberMode = TmallKeys.getMemberModel(brandCode);
			// 会员模式：通用版本
			if ("1".equals(memberMode)) {
				map.put("nickname", taobao_nick);
			}
			int memberId = binOLMBTIF01_Service.addMemberInfo(map);
			map.put("memberInfoId", memberId);
			binOLMBTIF01_Service.addMemCardInfo(map);
			map.put("memRegisterId", registId);
			binOLMBTIF01_Service.updateTempMemRegInfo(map);
		} catch(Exception e) {
			logger.error(MTM00009 + mixMobile);
			throw e;
		}
		rst.setRegister_code("SUC");
		Member member = rst.getMember();
		map.put("organizationInfoID", map.get("organizationInfoId"));
		map.put("brandInfoID", map.get("brandInfoId"));
		member.setLevel((long) binOLCM31_BL.getDefaultLevel(map));
		return rst;
	}
	
	@Override
	public void tran_pointErrLog(Map<String, Object> map) throws Exception {
		upCommMap(map);
		String errMsg = (String) map.get("TM_ERR");
		if (null != errMsg && errMsg.length() > 1000) {
			errMsg = errMsg.substring(0,1000);
		}
		map.put("errMsg", errMsg);
		binOLMBTIF01_Service.addTmallPointErrInfo(map);
	}
	@Override
	public void tran_pointChange(Map<String, Object> map) throws Exception {
		Long recordId = Long.parseLong(String.valueOf(map.get("record_id")));
		if (binOLMBTIF01_Service.getTmallPointCount(map) > 0) {
			logger.error("重复单据不接收！record_id: " + recordId);
			return;
		}
		String mixMobile = (String) map.get("mix_mobile");
		String extInfoStr = (String) map.get("ext_info");
		String order_id = null;
		if (!CherryChecker.isNullOrEmpty(extInfoStr)) {
			Map<String, Object> extInfo = CherryUtil.json2Map(extInfoStr);
			order_id = (String) extInfo.get("order_id");
			map.put("order_id", order_id);
			map.put("extInfoStr", extInfoStr);
		}
		String brandCode = (String) map.get("brandCode");
		if (CherryChecker.isNullOrEmpty(mixMobile)) {
			String mixKey = TmallKeys.getMixKey(brandCode);
			String mobilez = (String) map.get("mobile");
			if (CherryChecker.isNullOrEmpty(mobilez)) {
				logger.error("************************积分变更请求参数有误！密文和明文手机号都为空");
				return;
			}
			mixMobile = DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobilez + mixKey));
			map.put("mix_mobile", mixMobile);
		}
		String sysDate = binOLMBTIF01_Service.getSYSDate();
		map.put("pointTime", sysDate);
		upCommMap(map);
		// 取得会员信息
		Map<String, Object> memInfo = binOLMBTIF01_Service.getMemInfo(map);
		if (null == memInfo || memInfo.isEmpty()) {
			//memInfo = binOLMBTIF01_Service.getRegisterInfo(map);
			//if (null == memInfo || memInfo.isEmpty()) {
			map.put("ptFlag", 2);
			map.put("ptResult", 1);
			// 插入天猫积分兑换履历表
			binOLMBTIF01_Service.addTmallPointInfo(map);
			try {
				callbackTmall(mixMobile, recordId, "no-exsit-member", brandCode);
			} catch (Exception e) {
				map.put("ptFlag", 9);
				binOLMBTIF01_Service.updateTMPointInfo(map);
				logger.error("不存在的会员回调天猫失败！record_id: " + recordId);
			}
//			} else {
//				if (memInfo.get("memberInfoId") != null) {
//					binOLMBTIF01_Service.addRegHisInfo(memInfo);
//					binOLMBTIF01_Service.updateRegbindInfo(memInfo);
//				}
//				map.put("ptFlag", 0);
//				map.put("memRegisterId", memInfo.get("memRegisterId"));
//				// 插入天猫积分兑换履历表
//				binOLMBTIF01_Service.addTmallPointInfo(map);
//			}
		} else {
			int memberInfoId = Integer.parseInt(String.valueOf(memInfo.get("memberInfoId")));
			map.put("memberInfoId", memberInfoId);
			map.put("ptFlag", 1);
			map.put("ptResult", 0);
			// 插入天猫积分兑换履历表
			binOLMBTIF01_Service.addTmallPointInfo(map);
			sendPointsMQ(map);
		}
	}
	
	private void callbackTmall(String mixMobile, Long recordId, String errCode, String brandCode) throws Exception {
		TmallMeiCrmCallbackPointChangeRequest req =
				new TmallMeiCrmCallbackPointChangeRequest();
		req.setMixMobile(mixMobile);
		req.setRecordId(recordId);
		if (null == errCode) {
			req.setResult(0L);
		} else {
			req.setResult(1L);
			req.setErrorCode(errCode);
		}
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode(brandCode);
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		String appKey = tmallKey.getAppKey();
		String appSecret = tmallKey.getAppSecret();
		String sessionKey = tmallKey.getSessionKey();
		for (int i = 0; i < 4; i++) {
			try {
				TmallMeiCrmCallbackPointChangeResponse response = SignTool.pointChangeResponse(req, appKey, appSecret, sessionKey);
				if (response.isSuccess()) {
					break;
				}
				String errMsg = "Tmall response error: " + response.getSubCode() + " message: " + response.getSubMsg() + " 回调次数：" + (i + 1);
				if (i < 3) {
					logger.error(errMsg);
				} else {
					throw new Exception(errMsg);
				}
			} catch (Exception e) {
				String errMsg = "异常信息：" + e.getMessage() + " 回调次数：" + (i + 1);
				logger.error(errMsg,e);
				if (i == 3) {
					throw new Exception(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 将消息发送到积分维护的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private void sendPointsMQ(Map map) throws Exception {
		map.put("MemberCode", binOLCM31_BL.getMemCard(map));
		double point = Double.parseDouble(String.valueOf(map.get("point")));
		String type = String.valueOf(map.get("type"));
		String order_id = (String) map.get("order_id");
		String reason = null;
		if ("1".equals(type)) {
			point = -point;
		}
		String bizType = (String) map.get("biz_type");
		if ("gift_exchange".equals(bizType)) {
			reason = MTM00010;
		} else if ("cancel_exchange".equals(bizType)) {
			reason = MTM00011;
		} else if ("coupon_exchange".equals(bizType)) {
			reason = MTM00013;
		} else if ("cancel_coupon_exchange".equals(bizType)) {
			reason = MTM00014;
		} else if ("sign".equals(bizType)) {
			reason = MTM00017;
		} else if ("game".equals(bizType)) {
			reason = MTM00018;
		} else if ("red_packet_exchange".equals(bizType)) {
			reason = "红包";
		} else if ("cancel_red_packet_exchange".equals(bizType)) {
			reason = "红包取消";
		}  else if ("flow_wallet_exchange".equals(bizType)) {
			reason = "流量";
		} else if ("cancel_flow_wallet_exchange".equals(bizType)) {
			reason = "流量取消";
		} else {
			reason = MTM00016;
		}
		if (!CherryChecker.isNullOrEmpty(order_id)) {
			reason += "，订单号：" + order_id;
		}
		map.put("ModifyPoint", point);
		map.put("BusinessTime", map.get("pointTime"));
		map.put("Reason", reason);
		map.put("pointType", "2");
		map.put("Sourse", "Tmall");
		// 天猫记录ID
		map.put("TmRecordId", map.get("record_id"));
		binOLCM31_BL.sendPointsMQ(map);
	}
	
	/**
	 * 发送MQ重算消息进行实时重算
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendReCalcMsg(Map<String, Object> map) throws Exception {
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoId").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoId").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_MR;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MR);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1001);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 修改次数
		mainData.put("ModifyCounts", "0");
		// 会员ID
		mainData.put("memberInfoId", map.get("memberInfoId"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	private final String MTM00003 = "存在多个手机号相同的会员！手机号：";
	private final String MTM00004 = "会员绑定或解绑失败，会员不存在！手机号：";
	private final String MTM00005 = "会员绑定或解绑失败，类型参数不正确！手机号：";
	private final String MTM00006 = "会员绑定更新处理失败！手机号：";
	private final String MTM00007 = "会员解绑更新处理失败！手机号：";
	private final String MTM00009 = "会员注册失败！手机号：";
	private final String MTM00010 = "天猫礼品兑换";
	private final String MTM00011 = "天猫取消兑换";
	private final String MTM00013 = "天猫优惠券兑换";
	private final String MTM00014 = "天猫取消优惠券兑换";
	private final String MTM00016 = "其他";
	private final String MTM00017 = "签到增加积分";
	private final String MTM00018 = "玩游戏增加积分";
	private final String MTM00019 = "会员绑定或解绑失败，存在多个相同手机号的会员！手机号：";
	private final String MTM00021 = "会员查询失败，存在多个手机号相同的会员！手机号：";
	private final String MTM00022 = "会员注册失败！已经存在的会员！手机号：";
}
