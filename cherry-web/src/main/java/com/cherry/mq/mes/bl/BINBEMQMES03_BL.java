/**		
 * @(#)BINBEMQMES01_BL.java     1.0 2011/05/25		
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

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.dto.UdiskBindDetailDTO;
import com.cherry.cm.activemq.dto.UdiskBindMainDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.*;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.privilege.bl.BINOLCMPL03_BL;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.customize.member.MemberPassword;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMemberMessage_IF;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.service.BINBEMQMES03_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.site.lookup.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员消息数据接收处理BL
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES03_BL implements AnalyzeMemberMessage_IF {
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM31_BL")
	private BINOLCM31_IF binOLCM031_BL;
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binBEMQMES03_Service")
	private BINBEMQMES03_Service binBEMQMES03_Service;

	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="binOLCMPL03_BL")
	private BINOLCMPL03_BL binOLCMPL03_BL;
	
	@Resource(name="binBEMQMES97_BL")
	private BINBEMQMES97_BL binBEMQMES97_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource(name="binBEMQMES98_BL")
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	/** 会员信息修改履历管理BL */
	@Resource
	private BINOLCM36_BL binOLCM36_BL;
	
    @Resource(name = "binolcpcom05IF")
    private BINOLCPCOM05_IF binOLCPCOM05_IF;
    
    /** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	@Resource
	private BINOLCM02_BL binOLCM02_BL;

	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES03_BL.class);

	/**
	 * 处理会员数据
	 * @throws CherryMQException 
	 */
	@Override
	public void analyzeMemberData(Map<String, Object> map) throws Exception {
		
		String subType = (String) map.get("subType");
		// 取得会员详细信息Map
		Map memberDetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		// 会员信息登记区分
		Object memInfoRegFlg = memberDetailMap.get("memInfoRegFlg");
		// 初始入会日期区分
		String initJnDateFlg = "0";
		if (memInfoRegFlg != null && (Integer)memInfoRegFlg == 1) {
			initJnDateFlg = "1";
			map.put("MEMINFO_REGFLG", "1");
		}
		Map<String, Object> memInfoRecordMap = new HashMap<String, Object>();
		// 是否添加会员资料变更履历
		boolean isAddRecord = false;
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		// 会员入会生成初始密码规则：0：不生成密码；1：生成密码不发送短信；2：生成密码并发送短信
		String passwordConfigRule = binOLCM14_BL.getConfigValue("1315", organizationInfoID, brandInfoID);
		// 是否发送会员信息查询（会员密码查询）的沟通MQ（即发送密码给会员）; 实际生成密码后再置为true;
		boolean isSendPassWordToMember = false;
		// 换卡且MQ消息不是最新的场合，使用新卡对应的会员信息更新老卡对应的会员信息
		Map<String, Object> newMemInfoMap = new HashMap<String, Object>();
		Map<String, Object> oldMemInfo = new HashMap<String, Object>();
		
		if(null != memberDetailMap.get("memberInfoID")){
			memInfoRecordMap.put("memberInfoId", memberDetailMap.get("memberInfoID"));
			// 取得更新前会员信息
			oldMemInfo = binOLCM36_BL.getMemberInfo(memInfoRecordMap);
			memberDetailMap.put("joinDate",oldMemInfo.get("joinDate")); // 设置会员入会时间，会员完善度使用
		}
		// 入会时间调整准则
		String jnDateKbn = binOLCM14_BL.getConfigValue("1076", map.get("organizationInfoID").toString(), map.get("brandInfoID").toString());
		memberDetailMap.put("jnDateKbn", jnDateKbn);
		memberDetailMap.put("initJnDateFlg", initJnDateFlg);
		//组织代号
		String orgCode = (String) map.get("orgCode");
		// 品牌代码，即品牌简称
		String brandCode = (String) map.get("brandCode");
		memberDetailMap.put("orgCode", orgCode);
		memberDetailMap.put("brandCode", brandCode);
		// 单据日期 
		String tradeNo = (String) map.get("tradeNoIF");
		memberDetailMap.put("tradeNoIF", tradeNo);
		// 首次上传时间
		String firstUpTime = null;
		if (!CherryChecker.isNullOrEmpty(tradeNo)) {
			int startIndex = tradeNo.length() - 17;
			int endIndex = tradeNo.length() - 3;
			if (startIndex > 0) {
				// 单据日期 
				firstUpTime = tradeNo.substring(startIndex, endIndex);
			}
		}
		// 验证单据日期格式是否正确
		if (CherryChecker.checkDate(firstUpTime, "yyyyMMddHHmmss")) {
			Date ticketDate = DateUtil.coverString2Date(firstUpTime, "yyyyMMddHHmmss");
			SimpleDateFormat dateFm = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
			firstUpTime = dateFm.format(ticketDate);
		} else {
			firstUpTime = null;
		}
		boolean noRecalcFalg = true;
		// 是否入会
		boolean isJoinFlag = false;
		Map<String, Object> memInfo = null;
		// 会员换卡的场合
		if (MessageConstants.MEM_TYPE_CARD_CHANGE.equals(subType)) {
			// 换卡扣积分
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT02);
			memInfoRecordMap.put("modifyType", "2");
			if(null != memberDetailMap.get("memberInfoID")){
				// 将旧卡设置成无效
				memberDetailMap.put("cardValidFlagUpd", "0");
				binBEMQMES03_Service.updMemberCardStatus(memberDetailMap);
				// 查询新卡对应的会员信息数据
				Map<String, Object> memInfoMap = binBEMQMES03_Service.selMemInfoByNewMemcode(memberDetailMap);
				// 新卡存在新会员ID的场合
				if(memInfoMap != null && !memInfoMap.isEmpty()) {
					Map<String, Object> curMemCodeMap = new HashMap<String, Object>();
					curMemCodeMap.put("memberInfoID", memInfoMap.get("newMemberInfoID"));
					// 查询会员的最新卡号
					String curMemCode = binBEMQMES03_Service.getCurMemCode(curMemCodeMap);
					map.put("tradeEntityCode", curMemCode);
					// 新卡和老卡对应的会员ID为不同的场合，做会员合并处理
					if(!memberDetailMap.get("memberInfoID").toString().equals(memInfoMap.get("newMemberInfoID").toString())) {
						if (null != campRuleExec) {
							// 需要进行换卡扣积分处理
							map.put("isMBPointExec", "1");
						}
						// 设置新卡对应的会员ID
						memberDetailMap.put("newMemberInfoID", memInfoMap.get("newMemberInfoID"));
						// 设置新卡对应的地址ID
						memberDetailMap.put("newAddressInfoID", memInfoMap.get("newAddressInfoID"));
						map.put("oldVersion", memInfoMap.get("version"));
						// 最新的MQ消息的场合，做会员更新处理，否则把新卡对应的会员信息更新到老卡对应的会员信息中
						if(isLatestMes(map)) {
							// 设置会员地址信息
							this.setMemberAddress(memberDetailMap);
							memberDetailMap.put("messageId", memInfoMap.get("messageId"));
							//会员通，添加相应参数
							binOLCM02_BL.addTmallMixMobile(memberDetailMap,"memMobile",2);				
							// 更新会员信息表
							binBEMQMES03_Service.updMemberInfo(memberDetailMap);
							//更新会员扩展信息表
							binBEMQMES03_Service.updMemberExtInfo(memberDetailMap);
							this.updMemberExtInfo(memberDetailMap);
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.putAll(memberDetailMap);
							paramMap.put("memberInfoID", memInfoMap.get("newMemberInfoID"));
							paramMap.put("memberCode", memberDetailMap.get("newMemcode"));
							paramMap.put("cardValidFlag", memInfoMap.get("cardValidFlag"));
							// 更新会员卡信息
		    				this.updMemCardValid(paramMap, map);
						} else {
							Integer _memInfoRegFlg = (Integer)memInfoMap.get("memInfoRegFlg");
							if(_memInfoRegFlg == null || _memInfoRegFlg == 0) {
								memInfoMap.put("memberInfoID", memberDetailMap.get("memberInfoID"));
								memInfoMap.put("addressInfoID", memberDetailMap.get("addressInfoID"));
								// 设置会员地址信息
								this.setMemberAddress(memInfoMap);
								memInfoMap.put("jnDateKbn", jnDateKbn);
								memInfoMap.put("initJnDateFlg", initJnDateFlg);
								//会员通，添加相应参数
								binOLCM02_BL.addTmallMixMobile(memInfoMap,"memMobile",2);
								// 更新会员信息表
								binBEMQMES03_Service.updMemberInfo(memInfoMap);
								//更新会员扩展信息表
								this.updMemberExtInfo(memInfoMap);
								newMemInfoMap.putAll(memInfoMap);
							}
						}
						//组织代号
						memberDetailMap.put("orgCode", map.get("orgCode"));
						// 品牌代码，即品牌简称
						memberDetailMap.put("brandCode", map.get("brandCode"));
						// 相同会员合并处理
						noRecalcFalg = mergeMemberInfo(memberDetailMap);
						isAddRecord = true;
					} else {
						// 最新的MQ消息的场合，做会员更新处理，否则不做
						if(isLatestMes(map)) {
							memInfoRecordMap.put("modifyType", "1");
							isAddRecord = true;
							// 设置会员地址信息
							this.setMemberAddress(memberDetailMap);
							//会员通，添加相应参数
							binOLCM02_BL.addTmallMixMobile(memberDetailMap,"memMobile",2);
							// 更新会员信息表
							binBEMQMES03_Service.updMemberInfo(memberDetailMap);
							//更新会员扩展信息表
							this.updMemberExtInfo(memberDetailMap);
							
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.putAll(memberDetailMap);
							paramMap.put("memberCode", memberDetailMap.get("newMemcode"));
							paramMap.put("cardValidFlag", memInfoMap.get("cardValidFlag"));
							// 更新会员卡信息
		    				this.updMemCardValid(paramMap, map);
						}
					}
				} else {
					if (null != campRuleExec) {
						// 需要进行换卡扣积分处理
						map.put("isMBPointExec", "1");
					}
					// 取得会员卡回目
					String cardCount = String.valueOf(memberDetailMap.get("cardCount"));
					// 设定会员卡回目
					memberDetailMap.put("cardCount", Integer.parseInt(cardCount) + 1);
					// 添加会员新卡信息
					binBEMQMES03_Service.addMemberCardInfo(memberDetailMap);
					// 设置会员地址信息
					this.setMemberAddress(memberDetailMap);
					//会员通，添加相应参数
					binOLCM02_BL.addTmallMixMobile(memberDetailMap,"memMobile",2);
					// 更新会员信息表
					binBEMQMES03_Service.updMemberInfo(memberDetailMap);
					//更新会员扩展信息表
					this.updMemberExtInfo(memberDetailMap);
					// 需要下发会员等级
					map.put("isMBRuleExec", "1");
					isAddRecord = true;
				}
				Map<String, Object> mzMap = new HashMap<String, Object>();
				mzMap.putAll(map);
				mzMap.put("memberInfoId", memberDetailMap.get("memberInfoID"));
				mzMap.put("organizationInfoId", map.get("organizationInfoID"));
				mzMap.put("brandInfoId", map.get("brandInfoID"));
				// 发送会员扩展信息MQ消息(全部记录)
				binOLCM031_BL.sendAllMZMQMsg(mzMap);
			} else {
				// 没有查询到相关会员信息
				MessageUtil.addMessageWarning(map, "会员号为\""+memberDetailMap.get("memberCode")+"\""+MessageConstants.MSG_ERROR_34);
			}
			
		} else {
			// 没有已经登录的会员
			if (null == memberDetailMap.get("memberInfoID")) {
				memInfoRecordMap.put("modifyType", "0");
				isAddRecord = true;
				isJoinFlag = true;
				if(memberDetailMap.get("memberLevel") == null) {
					// 取得默认等级信息
					Map<String, Object> deftLevelInfo = getDeftLevelInfo(memberDetailMap);
					if (null != deftLevelInfo && !deftLevelInfo.isEmpty()) {
						memberDetailMap.putAll(deftLevelInfo);
						// 需要下发会员等级
						map.put("isMBRuleExec", "1");
					}
				}
				
				if("1".equals(passwordConfigRule) || "2".equals(passwordConfigRule)) {
					String memberPassword = (String)map.get("memberPassword");
					if(memberPassword == null || "".equals(memberPassword)) {
						// 生成6位随机密码
						memberPassword = MemberPassword.encrypt(brandCode,CherryUtil.generateSalt(6));
						memberDetailMap.put("memberPassword", memberPassword);
						if("2".equals(passwordConfigRule)) {
							// 需要发送会员信息查询（会员密码查询）的沟通MQ--发送短信给会员
							isSendPassWordToMember = true;
						}
					}
				}
				if (null != firstUpTime) {
					memberDetailMap.put("firstUpTime", firstUpTime);
				}
				//会员通，添加相应参数
				binOLCM02_BL.addTmallMixMobile(memberDetailMap,"memMobile",1);
				// 添加会员信息
				int memberInfoID = binBEMQMES03_Service.addMemberInfo(memberDetailMap);
				// 插入会员持卡信息表
				memberDetailMap.put("memberInfoID", memberInfoID);
				// 设置会员地址信息
				this.setMemberAddress(memberDetailMap);
				// 插入会员持卡信息表
				binBEMQMES03_Service.addMemberCardInfo(memberDetailMap);
				//更新会员扩展信息表
				this.updMemberExtInfo(memberDetailMap);
				// 删除卡号的场合
        		if (MessageConstants.MEM_TYPE_DEL_MEMBER.equals(subType)) {
        			// 将卡号设置成无效
					memberDetailMap.put("cardValidFlagUpd", "0");
					binBEMQMES03_Service.updMemberCardStatus(memberDetailMap);
					// 把会员设置成无效
					memberDetailMap.put("memValidFlag", "0");
					binBEMQMES03_Service.updMemValidFlag(memberDetailMap);
        		}
				//添加天猫加密手机号到会员信息表(巧迪会员通)
				binOLCM02_BL.addMixMobile(map,"memMobile");
			} else {
				Map<String, Object> curMemCodeMap = new HashMap<String, Object>();
				curMemCodeMap.put("memberInfoID", memberDetailMap.get("memberInfoID"));
				// 查询会员的最新卡号
				String curMemCode = binBEMQMES03_Service.getCurMemCode(curMemCodeMap);
				map.put("tradeEntityCode", curMemCode);
				// 最新的MQ消息的场合，做会员更新处理，否则不做
            	if(isLatestMes(map)) {
            		boolean isNoReg = (memInfoRegFlg != null && (Integer)memInfoRegFlg == 1);
            		// 会员资料为假登录的会员设置会员更新履历类型为新增
            		if (isNoReg) {
            			memInfoRecordMap.put("modifyType", "0");
            			
        				if("1".equals(passwordConfigRule) || "2".equals(passwordConfigRule)) {
        					String memberPassword = (String)map.get("memberPassword");
        					if(memberPassword == null || "".equals(memberPassword)) {
        						// 生成6位随机密码
        						memberPassword = MemberPassword.encrypt(brandCode,CherryUtil.generateSalt(6));
        						memberDetailMap.put("memberPassword", memberPassword);
        						if("2".equals(passwordConfigRule)) {
        							// 需要发送会员信息查询（会员密码查询）的沟通MQ
        							isSendPassWordToMember = true;
        						}
        					}
        				}
        				if (null != firstUpTime) {
        					memberDetailMap.put("firstUpTime", firstUpTime);
        				}
        				memberDetailMap.put("OrigUpFlg", "1");
        				memberDetailMap.put("origJoinDate", memberDetailMap.get("memGranddate"));
        				memberDetailMap.put("origEmployeeId", memberDetailMap.get("employeeID"));
        				memberDetailMap.put("origBaCode", memberDetailMap.get("BAcode"));
        				memberDetailMap.put("origOrganizationId", memberDetailMap.get("organizationID"));
        				memberDetailMap.put("origCounterCode", memberDetailMap.get("countercode"));
            		} else {
            			memInfoRecordMap.put("modifyType", "1");
            		}
            		isAddRecord = true;
            		// 设置会员地址信息
    				this.setMemberAddress(memberDetailMap);
    				//会员通，添加相应参数
					binOLCM02_BL.addTmallMixMobile(memberDetailMap,"memMobile",2);
    				// 更新会员信息表
    				binBEMQMES03_Service.updMemberInfo(memberDetailMap);

					//更新会员扩展信息表
					this.updMemberExtInfo(memberDetailMap);

    				if (isNoReg) {
    					memberDetailMap.remove("OrigUpFlg");
						// 是否以首单销售时间作为入会时间
						if(binOLCM14_BL.isConfigOpen("1319", organizationInfoID, brandInfoID)) {
							Map<String, Object> memMap = new HashMap<String, Object>();
							memMap.put("memberInfoId", memberDetailMap.get("memberInfoID"));
							memMap.put("organizationInfoId", organizationInfoID);
							memMap.put("brandInfoId", brandInfoID);
							memMap.put("orgCode", map.get("orgCode"));
							memMap.put("brandCode", map.get("brandCode"));
							memMap.put("BASEJDATE", binOLCM14_BL.getConfigValue("1330", organizationInfoID, brandInfoID));
							memInfo = binBEMQMES99_Service.upAndSendMem(memMap);
							if (null != memInfo && !memInfo.isEmpty()) {
								memberDetailMap.put("memGranddate", memInfo.get("joinDate"));
								memberDetailMap.put("employeeID", memInfo.get("employID"));
								memberDetailMap.put("BAcode", memInfo.get("employeeCode"));
								memberDetailMap.put("organizationID", memInfo.get("organizationId"));
								memberDetailMap.put("countercode", memInfo.get("organizationCode"));
								memberDetailMap.put("version", memInfo.get("version"));
								map.put("tradeCounterCode", memInfo.get("organizationCode"));
								map.put("tradeBAcode", memInfo.get("employeeCode"));
							}
						}
    				}
            		// 删除卡号的场合
            		if (MessageConstants.MEM_TYPE_DEL_MEMBER.equals(subType)) {
            			// 将卡号设置成无效
						memberDetailMap.put("cardValidFlagUpd", "0");
						binBEMQMES03_Service.updMemberCardInfo(memberDetailMap);
						// 查询会员的有效卡数量
						int memCardCount = binBEMQMES03_Service.getMemCardCount(memberDetailMap);
						if(memCardCount == 0) {
							memInfoRecordMap.put("modifyType", "3");
							// 把会员设置成无效
							memberDetailMap.put("memValidFlag", "0");
							binBEMQMES03_Service.updMemValidFlag(memberDetailMap);
						}
            		} else {
        				// 更新会员卡信息
        				this.updMemCardValid(memberDetailMap, map);
            		}
            	}
			}
		}
		
		if(isAddRecord) {
			memInfoRecordMap.put("oldMemInfo", oldMemInfo);
			memInfoRecordMap.put("memberInfoId", memberDetailMap.get("memberInfoID"));
			// 取得更新后会员信息
			Map<String, Object> newMemInfo = binOLCM36_BL.getMemberInfo(memInfoRecordMap);
			memInfoRecordMap.putAll(newMemInfo);
			if (MessageConstants.MEM_TYPE_CARD_CHANGE.equals(subType)) {
				memInfoRecordMap.put("memCode", memberDetailMap.get("newMemcode"));
			} else {
				memInfoRecordMap.put("memCode", memberDetailMap.get("memberCode"));
			}
			String billCode = (String)map.get("tradeNoIF");
			if(billCode != null && billCode.length() > 17) {
				String subBillCode = billCode.substring(billCode.length()-17);
				String tradeGrantDate = subBillCode.substring(0, 4)+"-"+subBillCode.substring(4,6)+"-"+subBillCode.substring(6,8);
				String tradeGrantTime = subBillCode.substring(8, 10)+":"+subBillCode.substring(10, 12)+":"+subBillCode.substring(12, 14);
				memInfoRecordMap.put("modifyTime", tradeGrantDate+" "+tradeGrantTime);
			}
			memInfoRecordMap.put("organizationInfoId", map.get("organizationInfoID"));
			memInfoRecordMap.put("brandInfoId", map.get("brandInfoID"));
			memInfoRecordMap.put("modifyCounter", map.get("tradeCounterCode"));
			memInfoRecordMap.put("modifyEmployee", map.get("tradeBAcode"));
			memInfoRecordMap.put("sourse", map.get("sourse"));
			memInfoRecordMap.put("version", map.get("version"));
			
			String oldMemo1 = (String)oldMemInfo.get("memo1");
			String newMemo1 = (String)memInfoRecordMap.get("memo1");
			String remark = null;
			if(newMemo1 != null && !"".equals(newMemo1) && !newMemo1.equals(oldMemo1)) {
				remark = newMemo1;
			}
			memInfoRecordMap.put("remark", remark);
			
//			String oldMemo1 = (String)oldMemInfo.get("memo1");
//			String newMemo1 = (String)memberDetailMap.get("memo1");
//			String remark = null;
//			if(newMemo1 != null && !"".equals(newMemo1) && !newMemo1.equals(oldMemo1)) {
//				remark = newMemo1;
//			}
//			memInfoRecordMap.put("remark", remark);
//			String memGranddate = (String)memberDetailMap.get("memGranddate");
//			if(memGranddate != null && memGranddate.length() == 8) {
//				memGranddate = memGranddate.substring(0, 4)+"-"+memGranddate.substring(4, 6)+"-"+memGranddate.substring(6, 8);
//			}
//			if(!newMemInfoMap.isEmpty()) {
//				newMemo1 = (String)newMemInfoMap.get("memo1");
//				remark = null;
//				if(newMemo1 != null && !"".equals(newMemo1) && !newMemo1.equals(oldMemo1)) {
//					remark = newMemo1;
//				}
//				memInfoRecordMap.put("remark", remark);
//				
//				memInfoRecordMap.put("name", newMemInfoMap.get("memName"));
//				memInfoRecordMap.put("telephone", newMemInfoMap.get("memPhone"));
//				memInfoRecordMap.put("mobilePhone", newMemInfoMap.get("memMobile"));
//				memInfoRecordMap.put("gender", newMemInfoMap.get("memSex"));
//				memInfoRecordMap.put("birthYear", newMemInfoMap.get("birthYear"));
//				memInfoRecordMap.put("birthDay", newMemInfoMap.get("birthDay"));
//				memInfoRecordMap.put("email", newMemInfoMap.get("memMail"));
//				memInfoRecordMap.put("employeeId", newMemInfoMap.get("employeeID"));
//				memInfoRecordMap.put("organizationId", newMemInfoMap.get("organizationID"));
//				memInfoRecordMap.put("joinDate", newMemInfoMap.get("memGranddate"));
//				memInfoRecordMap.put("referrerId", newMemInfoMap.get("referrerID"));
//				memInfoRecordMap.put("birthYearGetType", newMemInfoMap.get("memAgeGetMethod"));
//				memInfoRecordMap.put("blogId", oldMemInfo.get("blogId"));
//				memInfoRecordMap.put("messageId", oldMemInfo.get("messageId"));
//				memInfoRecordMap.put("identityCard", oldMemInfo.get("identityCard"));
//				memInfoRecordMap.put("maritalStatus", oldMemInfo.get("maritalStatus"));
//				Object active = newMemInfoMap.get("active");
//				if(active != null && !"".equals(active.toString())) {
//					memInfoRecordMap.put("active", active);
//				} else {
//					memInfoRecordMap.put("active", oldMemInfo.get("active"));
//				}
//				memInfoRecordMap.put("isReceiveMsg", oldMemInfo.get("isReceiveMsg"));
//				memInfoRecordMap.put("profession", oldMemInfo.get("profession"));
//				memInfoRecordMap.put("connectTime", oldMemInfo.get("connectTime"));
//				memInfoRecordMap.put("memType", oldMemInfo.get("memType"));
//				memInfoRecordMap.put("provinceId", newMemInfoMap.get("provinceID"));
//				memInfoRecordMap.put("cityId", newMemInfoMap.get("cityID"));
//				memInfoRecordMap.put("address", newMemInfoMap.get("memAddress"));
//				memInfoRecordMap.put("zipCode", newMemInfoMap.get("memPostcode"));
//				String modifyType = (String)memInfoRecordMap.get("modifyType");
//				if (modifyType != null && "0".equals(modifyType)) {
//					memInfoRecordMap.put("channelCode", newMemInfoMap.get("sourse"));
//        		} else {
//        			memInfoRecordMap.put("channelCode", oldMemInfo.get("channelCode"));
//        		}
//			} else {
//				memInfoRecordMap.put("name", memberDetailMap.get("memName"));
//				memInfoRecordMap.put("telephone", memberDetailMap.get("memPhone"));
//				memInfoRecordMap.put("mobilePhone", memberDetailMap.get("memMobile"));
//				memInfoRecordMap.put("gender", memberDetailMap.get("memSex"));
//				memInfoRecordMap.put("birthYear", memberDetailMap.get("birthYear"));
//				memInfoRecordMap.put("birthDay", memberDetailMap.get("birthDay"));
//				memInfoRecordMap.put("email", memberDetailMap.get("memMail"));
//				memInfoRecordMap.put("employeeId", memberDetailMap.get("employeeID"));
//				memInfoRecordMap.put("organizationId", memberDetailMap.get("organizationID"));
//				memInfoRecordMap.put("joinDate", memGranddate);
//				memInfoRecordMap.put("referrerId", memberDetailMap.get("referrerID"));
//				memInfoRecordMap.put("birthYearGetType", memberDetailMap.get("memAgeGetMethod"));
//				memInfoRecordMap.put("blogId", oldMemInfo.get("blogId"));
//				memInfoRecordMap.put("messageId", oldMemInfo.get("messageId"));
//				memInfoRecordMap.put("identityCard", oldMemInfo.get("identityCard"));
//				memInfoRecordMap.put("maritalStatus", oldMemInfo.get("maritalStatus"));
//				String active = (String)memberDetailMap.get("active");
//				if(active != null && !"".equals(active)) {
//					memInfoRecordMap.put("active", active);
//				} else {
//					memInfoRecordMap.put("active", oldMemInfo.get("active"));
//				}
//				memInfoRecordMap.put("isReceiveMsg", oldMemInfo.get("isReceiveMsg"));
//				memInfoRecordMap.put("profession", oldMemInfo.get("profession"));
//				memInfoRecordMap.put("connectTime", oldMemInfo.get("connectTime"));
//				memInfoRecordMap.put("memType", oldMemInfo.get("memType"));
//				memInfoRecordMap.put("provinceId", memberDetailMap.get("provinceID"));
//				memInfoRecordMap.put("cityId", memberDetailMap.get("cityID"));
//				memInfoRecordMap.put("address", memberDetailMap.get("memAddress"));
//				memInfoRecordMap.put("zipCode", memberDetailMap.get("memPostcode"));
//				String modifyType = (String)memInfoRecordMap.get("modifyType");
//				if (modifyType != null && "0".equals(modifyType)) {
//					memInfoRecordMap.put("channelCode", map.get("sourse"));
//        		} else {
//        			memInfoRecordMap.put("channelCode", oldMemInfo.get("channelCode"));
//        		}
//			}
//			if(jnDateKbn != null && "2".equals(jnDateKbn)) {
//				String oldJoinDate = (String)oldMemInfo.get("joinDate");
//				if(oldJoinDate != null && !"".equals(oldJoinDate)) {
//					memInfoRecordMap.put("joinDate", oldJoinDate);
//				}
//			}
			this.setInsertInfoMapKey(memInfoRecordMap);
			// 添加会员信息修改履历
			binOLCM36_BL.addMemberInfoRecord(memInfoRecordMap);
			
			// 取得等级、积分重算处理器
			CherryMessageHandler_IF cherryMessageHandler = binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, MessageConstants.MESSAGE_TYPE_RU);
			if(cherryMessageHandler != null) {
				// 生日变更或者推荐会员变更是否进行重算
				boolean isRecFlag = false;
				// 旧的生日
				String oldBirthDay = (String) oldMemInfo.get("birthDay");
				// 新的生日
				String newBirthDay = (String) memberDetailMap.get("birthDay");
				// 生日发生改变
				if (null != newBirthDay && !newBirthDay.equals(oldBirthDay) 
						|| null != oldBirthDay && !oldBirthDay.equals(newBirthDay)){
					CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
					if(campRuleExec != null) {
						isRecFlag = true;
					}
				}
				// 生日没有变更再看推荐会员是否变更
				if(!isRecFlag) {
					// 原推荐会员
					Object oldReferrerId = oldMemInfo.get("referrerId");
					// 新推荐会员
					Object newReferrerId = memberDetailMap.get("referrerID");
					// 推荐会员是否发生改变
					boolean isReferChange = false;
					if(newReferrerId != null && !"".equals(newReferrerId.toString())) {
						if(oldReferrerId != null && !"".equals(oldReferrerId.toString())) {
							if(!newReferrerId.toString().equals(oldReferrerId.toString())) {
								isReferChange = true;
							}
						} else {
							isReferChange = true;
						}
					} else {
						if(oldReferrerId != null && !"".equals(oldReferrerId.toString())) {
							isReferChange = true;
						}
					}
					if (isReferChange){
						CampRuleExec_IF campRuleExec05 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
						if(campRuleExec05 != null) {
							isRecFlag = true;
						}
					}
				}
				if (isRecFlag) {
					// 查询会员的最早销售时间
					String saleTime = binBEMQMES03_Service.getMinSaleTime(memberDetailMap);
					if(saleTime != null) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						// 组织ID
						paramMap.put("organizationInfoID", map.get("organizationInfoID"));
						// 品牌ID
						paramMap.put("brandInfoID", map.get("brandInfoID"));
						// 组织代号
						paramMap.put("orgCode", map.get("orgCode"));
						// 品牌代码，即品牌简称
						paramMap.put("brandCode", map.get("brandCode"));
						// 
						paramMap.put("memberInfoID", memberDetailMap.get("memberInfoID"));
						String newMemcode = (String)memberDetailMap.get("newMemcode");
						if(newMemcode != null && !"".equals(newMemcode)) {
							paramMap.put("newMemcode", newMemcode);
						} else {
							paramMap.put("newMemcode", memberDetailMap.get("memberCode"));
						}
						paramMap.put("reCalcDate", saleTime);
						paramMap.put("reCalcType", "0");
						// 插入重算信息表
						binBEMQMES03_Service.insertReCalcInfo(paramMap);
						// 发送MQ重算消息进行实时重算
						sendReCalcMsg(paramMap);
						noRecalcFalg = false;
					}
				}
			}
			
			if (noRecalcFalg) {
				// 初始为假登录的会员
				if (memInfoRegFlg != null && (Integer)memInfoRegFlg == 1) {
					// 需要下发会员等级
					map.put("isMBRuleExec", "1");
				}
				if(memberDetailMap.get("memberLevel") == null) {
					if (null != memberDetailMap.get("preMemLevel")) {
						// 当前等级
						int preMemLevel = Integer.parseInt(memberDetailMap.get("preMemLevel").toString());
						if (0 == preMemLevel) {
							// 取得默认等级信息
							Map<String, Object> deftLevelInfo = getDeftLevelInfo(memberDetailMap);
							if (null != deftLevelInfo && !deftLevelInfo.isEmpty()) {
								memberDetailMap.putAll(deftLevelInfo);
								// 需要下发会员等级
								map.put("isMBRuleExec", "1");
								// 更新会员等级
								binBEMQMES03_Service.updMemLevelInfo(memberDetailMap);
							}
						}
					}
				}
			} else {
				// 重算过的场合不需要下发会员等级
				map.remove("isMBRuleExec");
			}
			
			if(isJoinFlag || (memInfoRegFlg != null && (Integer)memInfoRegFlg == 1)) {
				// 会员入会需要发送沟通MQ
				if(binOLCM14_BL.isConfigOpen("1086", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")))) {
					map.put("eventType", "5");
					map.put("eventId", memberDetailMap.get("memberInfoID"));
					if(billCode != null && billCode.length() > 17) {
						String subBillCode = billCode.substring(billCode.length()-17);
						String tradeGrantDate = subBillCode.substring(0, 4)+"-"+subBillCode.substring(4,6)+"-"+subBillCode.substring(6,8);
						String tradeGrantTime = subBillCode.substring(8, 10)+":"+subBillCode.substring(10, 12)+":"+subBillCode.substring(12, 14);
						map.put("eventDate", tradeGrantDate+" "+tradeGrantTime);
					}
					map.put("sourse", "BINBEMQMES03");
					// 发送会员入会沟通事件MQ消息
					this.sendGTMQ(map);
				}
			}
		}
		if (null != memInfo && !memInfo.isEmpty()) {
			binOLCM031_BL.sendMEMQMsg(memInfo);
		}
		
		if(isSendPassWordToMember) {
			// 发送密码短信给会员
			map.put("eventType", "13");
			map.put("eventId", memberDetailMap.get("memberInfoID"));
			
			if(tradeNo != null && tradeNo.length() > 17) {
				String subBillCode = tradeNo.substring(tradeNo.length()-17);
				String tradeGrantDate = subBillCode.substring(0, 4)+"-"+subBillCode.substring(4,6)+"-"+subBillCode.substring(6,8);
				String tradeGrantTime = subBillCode.substring(8, 10)+":"+subBillCode.substring(10, 12)+":"+subBillCode.substring(12, 14);
				map.put("eventDate", tradeGrantDate+" "+tradeGrantTime);
			}
			map.put("sourse", "BINBEMQMES03");
			// 发送会员入会沟通事件MQ消息
			this.sendGTMQ(map);
		}
	}

	/**
	 * 设定会员地址
	 * 
	 * @param memberDetailMap
	 */
	private void setMemberAddress(Map memberDetailMap) {
		// 会员地址信息不存在的场合
		if (null == memberDetailMap.get("addressInfoID")) {
			String memAddress = (String)memberDetailMap.get("memAddress");
			String memPostcode = (String)memberDetailMap.get("memPostcode");
			String memCity = (String)memberDetailMap.get("memCity");
			String memProvince = (String)memberDetailMap.get("memProvince");
			// 存在地址信息的场合添加地址
			if(memAddress != null && !"".equals(memAddress) 
					|| memPostcode != null && !"".equals(memPostcode)
					|| memCity != null && !"".equals(memCity)
					|| memProvince != null && !"".equals(memProvince)) {
				// 添加地址信息
				int addressInfoID = binBEMQMES03_Service.addAddressInfo(memberDetailMap);
				// 设置地点ID
				memberDetailMap.put("addressInfoID", addressInfoID);
				// 插入会员地址表
				binBEMQMES03_Service.addMemberAddress(memberDetailMap);
			}
		} else {
			// 更新地址信息表
			binBEMQMES03_Service.updAddressInfo(memberDetailMap);
		}
	}

	/**
	 * 处理BA数据
	 * @throws Exception 
	 */
	@Override
	public void analyzeBaInfoData(Map<String, Object> map) throws Exception {	

		Object orgInfoId = map.get("organizationInfoID");
		Object brandId = map.get("brandInfoID");
		// 取得BA管理模式
		String configValue = binOLCM14_BL.getConfigValue("1066", orgInfoId.toString(), brandId.toString());
		List detailDataList = (List) map.get("detailDataDTOList");
		String employeeID=null;
		Map superPath = new HashMap<String,Object>();
		//查询原柜台主管ID
		Map oldEmployMap = binBEMQMES03_Service.selSuperPath(map);
		String oldEmployeeID = null;
		if(oldEmployMap!=null&&oldEmployMap.get("employeeID")!=null){
			oldEmployeeID = String.valueOf(oldEmployMap.get("employeeID"));
		}
		// 柜台主管是否变化
		boolean isChange = false;
		String orgSuperID="";
		for (int i = 0; i < detailDataList.size(); i++) {
			Map detailDataMap = (HashMap) detailDataList.get(i);
			if(null!=detailDataMap.get("flag")&&detailDataMap.get("flag").equals("2")){
//				if(detailDataMap.get("baname")==null||detailDataMap.get("baname").equals("")){
//					break;
//				}
				//BAS code 放在baname中
				String BASCode = (String) detailDataMap.get("baname");
				
				Map employMapParam = new HashMap<String,Object>();
				employMapParam.put("organizationInfoID", detailDataMap.get("organizationInfoID"));
				employMapParam.put("brandInfoID", detailDataMap.get("brandInfoID"));
				employMapParam.put("BAcode", BASCode);
				
                //（1）如果该条明细是BAS信息，则先用Baname字段的值去匹配新后台员工表中的名字，如果匹配到一个有效的，则确定就是该员工；
                //如果匹配到多条，则日志中打出错误信息，舍弃该条明细(注意是舍弃明细，不是舍弃整条MQ)
                //（2）如果（1）中没有匹配到任何值，则用Baname字段的值去匹配新后台员工表中的员工代号字段。
                //如匹配到，则确定就是该员工，如果匹配不到，则打印错误日志，舍弃该条明细；
                List<Map<String,Object>> employeeList = binBEMQMES03_Service.selEmployeeInfoByName(employMapParam);
                Map<String,Object> employMap = new HashMap<String,Object>();
                if(null != employeeList && employeeList.size() == 1){
                    employMap.putAll(employeeList.get(0));
                }else if(null != employeeList && employeeList.size()>1){
                    logger.error("单据号为\""+map.get("tradeNoIF")+"\"，BAS名字为\""+BASCode+"\"匹配到多个员工");
                    continue;
                }else {
                    //用员工号查询员工
                    employMap = binBEMQMES03_Service.selEmployeeInfo(employMapParam);
                }
				
				if(employMap==null||employMap.get("employeeID")==null){
					
					if(oldEmployMap!=null&&oldEmployMap.get("employeeID")!=null){
						superPath.put("superPath", oldEmployMap.get("superPath"));
					    employeeID = String.valueOf(oldEmployMap.get("employeeID"));
					}
					
                    logger.error("单据号为\""+map.get("tradeNoIF")+"\"，BAS卡号为\""+BASCode+"\"匹配不到员工");
                    continue;
//					
//					superPath.put("superPath", "/");
//					//查询子节点newNodeId
//					Map resultMap = binBEMQMES03_Service.selChildNodeID(superPath);
//					
//					resultMap.put("organizationInfoID",detailDataMap.get("organizationInfoID"));
//					resultMap.put("brandInfoID", detailDataMap.get("brandInfoID"));
//					resultMap.put("organizationID", detailDataMap.get("organizationID"));
//					resultMap.put("positionCategoryID", detailDataMap.get("positionCategoryID"));
//					resultMap.put("BAcode", BASCode);
//					resultMap.put("baname", detailDataMap.get("baname"));
//					resultMap.put("baTel", detailDataMap.get("baTel"));
//					setInsertInfoMapKey(resultMap);
//					// 插入员工信息表
//				    employeeID = String.valueOf(binBEMQMES03_Service.addEmployee(resultMap));
//				    superPath.put("superPath", resultMap.get("newNodeId"));
//				    flagBAS = 1;
				}else{
					orgSuperID = String.valueOf(employMap.get("organizationID"));
					superPath.put("superPath", employMap.get("path"));
				    employeeID = String.valueOf(employMap.get("employeeID"));
				    
					if(employeeID != null && !"".equals(employeeID.toString())) {
						if(oldEmployeeID != null && oldEmployeeID.equals(employeeID.toString())) {
							isChange = false;
						} else {
							isChange = true;
						}
					} else {
						if(oldEmployeeID != null && !"".equals(oldEmployeeID)) {
							isChange = true;
						}
					}
					try {
						if(isChange){
							//对BAS数据权限进行处理
							Map<String,Object> newMap = new HashMap<String,Object>();
							newMap.put("organizationId", map.get("organizationID"));
							newMap.put("employeeId", employeeID);
							newMap.put("oldEmployeeId", oldEmployeeID);
							newMap.put("organizationInfoId", map.get("organizationInfoID"));
							newMap.put("brandInfoId", map.get("brandInfoID"));
							binOLCMPL03_BL.updateCounterHeader(newMap);
						}
					} catch (Exception e) {
						throw new CherryMQException(e.getMessage()+";There is an error that has occured in BINOLCMPL03_BL's updateCounterHeader");
					}
				}
				break;
			}
		}
		if(employeeID == null) {
			if(oldEmployMap!=null&&oldEmployMap.get("employeeID")!=null){
				superPath.put("superPath", oldEmployMap.get("superPath"));
			    employeeID = String.valueOf(oldEmployMap.get("employeeID"));
			}
		}
		
		boolean isChangeOrgSuper = false;
		String oldOrgId = "";
		if(isChange){
			Map<String,Object> tempMapOrg =binBEMQMES03_Service.selOrgSuperOrgID(map);
//			if(tempMapOrg!=null){
				oldOrgId = tempMapOrg!=null?String.valueOf(tempMapOrg.get("BIN_OrganizationID")):"";
				if(!orgSuperID.equals("")&&!orgSuperID.equals("null")&&!oldOrgId.equals(orgSuperID)){
					Map<String,Object> newSuperPath = new HashMap<String,Object>();
					newSuperPath.put("organizationID", orgSuperID);
					newSuperPath = binBEMQMES03_Service.selOrgSuperOrgPath(newSuperPath);
					if(newSuperPath!=null&&!newSuperPath.isEmpty()){
						//查询部门新节点
						Map<String,Object> tempMap = binBEMQMES03_Service.selOrgChildNodeID(newSuperPath);
						tempMap.put("organizationID", map.get("organizationID"));
						tempMap.put("organizationInfoID", map.get("organizationInfoID"));
						tempMap.put("brandInfoID", map.get("brandInfoID"));
						setInsertInfoMapKey(tempMap);
						//更新该部门对应的上级path
						binBEMQMES03_Service.updOrgNodeID(tempMap);
						
						isChangeOrgSuper = true;
					}
				}
//			}
		}
		
		// 查询BA的原上级BAS
		List<String> oldBASList = binBEMQMES03_Service.getOldBASList(map);
		
		//查询BAS下对应的所有直接下属信息。此处逻辑为：由于每次BAS更改柜台或者没有更改柜台，那么对应的BA的path要么更新，要么不变。
		//                                          当然如果BA不属于BAS的下属，那么始终更新。
		//                                          这样做的好处是避免path无限扩大或者将扩大趋势减慢。

		//先对该柜台下的所有BA对应的员工信息中的部门ID设置为空
		binBEMQMES03_Service.updEmpOrgIdISNull(map);
		
		List<Map<String,Object>> list = null;
		if(employeeID!=null){
			Map<String,Object> newMap1 = new HashMap<String,Object>();
			newMap1.put("employeeID", employeeID);
			list = binBEMQMES03_Service.selEmployeesByBAS(newMap1);
		}
		String baEmployeeID = "";
		boolean underChange = false;
		for (int i = 0; i < detailDataList.size(); i++) {
			Map detailDataMap = (HashMap) detailDataList.get(i);
			//bas在上面已经处理过,此处跳过
			if(null!=detailDataMap.get("flag")&&detailDataMap.get("flag").equals("2")){
				continue;
			}
			if(detailDataMap.get("BAcode") == null || "".equals(detailDataMap.get("BAcode"))){
				continue;
			}
			String BAcode = (String) detailDataMap.get("BAcode");
			int isUnder = 0;//用于判断是否是BAS下属
			for(int j=0;list!=null&&j<list.size();j++){
				Map<String,Object> tempMap = list.get(j);
				if(tempMap!=null&&tempMap.get("employeeCode")!=null&&!tempMap.get("employeeCode").equals("")){
					if(BAcode.equals(tempMap.get("employeeCode"))){
						 //此明细记录中的BA信息对应的员工已经是此BAS的下属，所以直接更新，无需更新NodeID信息
						 //更新员工信息表
						 baEmployeeID = String.valueOf(tempMap.get("employeeID"));
						 Map<String,Object> parMap = new HashMap<String,Object>();
						 parMap.put("employeeID", baEmployeeID);
						 // 校验模式的场合不更新BA姓名
						 if(configValue != null && "1".equals(configValue)) {
							 
						 } else {
						     parMap.put("BAcode", BAcode);
							 parMap.put("baname", detailDataMap.get("baname"));
						 }
						 parMap.put("baTel", detailDataMap.get("baTel"));
	                     parMap.put("identityCard", detailDataMap.get("identityCard"));
						 parMap.put("organizationID", map.get("organizationID"));
						 
						 String validFlag = (String)tempMap.get("validFlag");
						 if("0".equals(validFlag)) {
							 binBEMQMES03_Service.insertEmployeeQuit(parMap);
						 }
						 
						 binBEMQMES03_Service.updateEmployee(parMap);
						 isUnder = 1;
					     break;
					}
				}
			}
			
			if(isUnder==0){
				underChange = true;
				//下面对原先不属于BAS下属的处理
				
				if(superPath.isEmpty()||superPath.get("superPath")==null||superPath.get("superPath").equals("")){
					superPath.put("superPath", "/");
				}
				//查询子节点
				Map resultMap = binBEMQMES03_Service.selChildNodeID(superPath);
				// 设置节点ID
				detailDataMap.put("newNodeId", resultMap.get("newNodeId"));
				
				Map employMapParam = new HashMap<String,Object>();
				employMapParam.put("organizationInfoID", detailDataMap.get("organizationInfoID"));
				employMapParam.put("brandInfoID", detailDataMap.get("brandInfoID"));
				employMapParam.put("BAcode", detailDataMap.get("BAcode"));
				
				//查询员工
				Map<String,Object> employMap = binBEMQMES03_Service.selEmployeeInfo(employMapParam);
				if(employMap!=null&&employMap.get("employeeID")!=null){
					 //更新员工信息表
					 baEmployeeID = String.valueOf(employMap.get("employeeID"));
					 Map<String,Object> parMap = new HashMap<String,Object>();
					 parMap.put("employeeID", baEmployeeID);
					 // 校验模式的场合不更新BA姓名
					 if(configValue != null && "1".equals(configValue)) {
						 
					 } else {
					     parMap.put("BAcode", BAcode);
						 parMap.put("baname", detailDataMap.get("baname"));
					 }
					 parMap.put("baTel", detailDataMap.get("baTel"));
                     parMap.put("identityCard", detailDataMap.get("identityCard"));
					 parMap.put("organizationID", map.get("organizationID"));
					 parMap.put("newNodeId", detailDataMap.get("newNodeId"));
					 
					 String validFlag = (String)employMap.get("validFlag");
					 if("0".equals(validFlag)) {
						 binBEMQMES03_Service.insertEmployeeQuit(parMap);
					 }
					 
					 binBEMQMES03_Service.updateEmployee(parMap);
				}else{
					 // 插入员工信息表
					 baEmployeeID = String.valueOf(binBEMQMES03_Service.addEmployee(detailDataMap));
		 		}
			 }
			
				
			// 设定员工ID
			detailDataMap.put("employeeID", baEmployeeID);
			
			//查询BA信息表
			Map<String,Object> baMap = binBEMQMES03_Service.selBaInfo(detailDataMap);
			if(baMap!=null&&baMap.get("baInfoID")!=null){
				// 校验模式的场合不更新BA姓名
				 if(configValue != null && "1".equals(configValue)) {
					 
				 } else {
					 baMap.put("baname", detailDataMap.get("baname"));
				 }
				baMap.put("baTel", detailDataMap.get("baTel"));
                baMap.put("identityCard", detailDataMap.get("identityCard"));
				baMap.put("organizationID", map.get("organizationID"));
				//更新BA信息表
				binBEMQMES03_Service.updateBaInfo(baMap);
			}else{
				// 插入BA信息表
				binBEMQMES03_Service.addBaInfo(detailDataMap);
			}
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("organizationInfoId", map.get("organizationInfoID"));
		paramMap.put("brandInfoId", map.get("brandInfoID"));
		paramMap.put("orgCode", map.get("orgCode"));
		paramMap.put("brandCode", map.get("brandCode"));
		List<String> depPrivilegeList = new ArrayList<String>();
		// 柜台主管变化的场合，需要刷新部门权限
		if(isChange) {
			if(employeeID != null){
				depPrivilegeList.add(employeeID);
			}
			if(oldEmployeeID != null) {
				depPrivilegeList.add(oldEmployeeID);
			}
		}
		paramMap.put("depPrivilegeList", depPrivilegeList);
		// 存在部门结构发生变化的场合，需要刷新
		if(isChangeOrgSuper){
			List<String> higherOrgList = new ArrayList<String>();
			higherOrgList.add(orgSuperID);
			if(!oldOrgId.equals("")&&!oldOrgId.equals("null")){
		    	higherOrgList.add(oldOrgId);
			}
			paramMap.put("higherOrgList", higherOrgList);
		}
		List<String> empPrivilegeList = new ArrayList<String>();
		// 存在BA的上级发生变化的场合，需要刷新人员权限
		if(underChange) {
			if(oldBASList != null && !oldBASList.isEmpty()){
				empPrivilegeList.addAll(oldBASList);
			}
			if(employeeID != null && !empPrivilegeList.contains(employeeID)){
				empPrivilegeList.add(employeeID);
			}
		}
		paramMap.put("empPrivilegeList", empPrivilegeList);
		
		try {
			// 更新人员数据权限
			binOLCMPL03_BL.updatePrivilege(paramMap);
		} catch (Exception e) {
			throw new CherryMQException(e.getMessage()+";There is an error that has occured in BINOLCMPL03_BL's updatePrivilege");
		}
		
		if(isChange){
			//存在柜台主管变更     需发送MQ消息到终端       之所以放在此处，以免新后台发生回滚，而MQ消息却已经发送到了终端，在此处发送回滚的可能性最低
			
			Map<String,Object> ucMap = new HashMap<String,Object>();
			ucMap.put("newEmployeeBas", employeeID);
			ucMap.put("oldEmployeeBas", oldEmployeeID);
			ucMap.put("organizationInfoID", map.get("organizationInfoID"));
			ucMap.put("brandInfoID", map.get("brandInfoID"));
			ucMap.put("counterCode", map.get("counterCode"));
			ucMap.put("brandCode", map.get("brandCode"));
			ucMap.put("orgCode", map.get("orgCode"));
			sendUC(ucMap);
		}
	}

	/**
	 * 处理BAS考勤数据
	 */
	@Override
	public void analyzeBasAttInfoData(Map<String, Object> map) {
		// 取得BAS详细信息Map
		Map BASDetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		// 更新BAS考勤表
		int count = binBEMQMES03_Service.updBasAttInfo(BASDetailMap);
		if(count==0){
			// 插入BAS考勤表
			binBEMQMES03_Service.addBasAttInfo(BASDetailMap);
		}
	}
	
	/**
     * 处理普通考勤数据
	 * @throws CherryMQException 
     */
    @Override
    public void analyzeAttendanceData(Map<String, Object> map) throws Exception {
        //柜台的部门ID
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        paramMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
        paramMap.put("CounterCode", map.get("counterCode"));
        Map<String,Object> departInfo= binBEMQMES97_BL.getOrganizationInfo(paramMap, true);
        map.put("organizationID", departInfo.get("BIN_OrganizationID"));
        map.put("counterName", departInfo.get("CounterNameIF"));
        //员工ID
        paramMap.put("EmployeeCode", map.get("employeeCode"));
        Map<String,Object> empInfo= binBEMQMES97_BL.getEmployeeInfo(paramMap, true);
        map.put("employeeID", empInfo.get("BIN_EmployeeID"));
        map.put("employeeName", empInfo.get("EmployeeName"));
        map.put("categoryName", empInfo.get("CategoryName"));
        
        //检查必填
        checkRequired(map,map,"SubType");
        checkRequired(map,map,"AttendanceTime");
        checkRequired(map,map,"AttendanceType");
        //检查时间格式
        if(!CherryChecker.checkDate(ConvertUtil.getString(map.get("attendanceTime")),CherryConstants.DATE_PATTERN_24_HOURS)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "AttendanceTime", "YYYY-MM-DD HH:mm:SS"));
        }
        
        //插入BA考勤表
        Map<String,Object> addParam = new HashMap<String,Object>();
        addParam.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        addParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
        addParam.put("BIN_EmployeeID", map.get("employeeID"));
        addParam.put("BIN_OrganizationID", map.get("organizationID"));
        addParam.put("AttendanceDateTime", map.get("attendanceTime"));
        addParam.put("AttendanceType", map.get("attendanceType"));
        setInsertInfoMapKey(addParam);
        binBEMQMES03_Service.addBAAttendance(addParam);
        
        map.put("modifyCounts","0");
    }
	
	/**
	 * 处理会员回访数据
	 */
	@Override
	public void analyzeMemVisitInfoData(Map<String,Object> map){
		Map visitDetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		int count = binBEMQMES03_Service.updateMemVitInfo(visitDetailMap);
		if(count==0){
		    binBEMQMES03_Service.InsertMemVisitInfo(visitDetailMap);
		}
		String visitTaskID = (String)visitDetailMap.get("visitTaskID");
		//VisitTaskID字段有值，则更新【回访任务表】
        if(!"".equals(visitTaskID)){
            Map<String,Object> visitTaskMap = new HashMap<String,Object>();
            visitTaskMap.put("BIN_VisitTaskID", visitTaskID);
            visitTaskMap.put("VisitTime", visitDetailMap.get("visitDate"));
            visitTaskMap.put("VisitResult", visitDetailMap.get("visitFlag"));
            setInsertInfoMapKey(visitTaskMap);
            binBEMQMES03_Service.updateVisitTask(visitTaskMap);
        }
	}
	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException {
		    //获得明细list
		    List<Map> list = (List<Map>) map.get("detailDataDTOList");
		    Map DetailMap = new HashMap();
		    String attType = "";
		    if(null != list && list.size()>0){
		        // 取得详细信息Map
		        DetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		        attType = (String)DetailMap.get("attType");
		    }
			DBObject dbObject = new BasicDBObject();
			//组织代号
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码，即品牌简称
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", map.get("tradeType"));
			// 单据号
			dbObject.put("TradeNoIF", map.get("tradeNoIF"));
			// 修改次数
			dbObject.put("ModifyCounts", map.get("modifyCounts")==null
					||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
			
			if(MessageConstants.MSG_MEMBER.equals(map.get("tradeType"))){
				// 业务主体
				dbObject.put("TradeEntity", "0");
				if("2".equals(map.get("subType"))){
					 // 业务主体代号
					 dbObject.put("TradeEntityCode", DetailMap.get("newMemcode"));
				}else{
					 // 业务主体代号
					 dbObject.put("TradeEntityCode", DetailMap.get("memberCode"));
				}
				// 业务主体名称
				dbObject.put("TradeEntityName", DetailMap.get("memName"));
				//操作人员代号
				dbObject.put("UserCode", DetailMap.get("BAcode"));
				//操作人员名称
				dbObject.put("UserName", DetailMap.get("employeeName"));
				//岗位名称名称
				dbObject.put("UserPost", DetailMap.get("categoryName"));
				// 柜台名称
				dbObject.put("DeptCode", DetailMap.get("counterCode"));
				// 柜台名称
				dbObject.put("DeptName", map.get("counterName"));
				//发生时间
				dbObject.put("OccurTime", DetailMap.get("memGranddate"));
				// 日志正文
				if("0".equals(map.get("subType"))){
				     dbObject.put("Content", "入会");
				}else if ("1".equals(map.get("subType"))){
					 dbObject.put("Content", "修改了档案");
				}else{
					 dbObject.put("Content", "会员卡变更");
				}
				// 截取单据号的后17位保存到MongoDb，用来判断消息的时间顺序
				String billCode = (String)map.get("tradeNoIF");
				if(billCode != null && billCode.length() > 17) {
					dbObject.put("MbTime", billCode.substring(billCode.length()-17));
				}
			} 
			else if(MessageConstants.MSG_BA_INFO.equals(map.get("tradeType"))) {
				// 业务主体
			    dbObject.put("TradeEntity", "1");
			    String BAcode="";
			    String baname="";
			    for(int i=0;i<list.size();i++){
			    	Map mapBA = list.get(i);
			    	mapBA.get("BAcode");
			    	mapBA.get("baname");
			    	if(i==list.size()-1){
			    		BAcode +=mapBA.get("BAcode")==null?"":mapBA.get("BAcode");
			        	baname +=mapBA.get("baname")==null?"":mapBA.get("baname");
			    	}
			    	else{
			    	    BAcode +=mapBA.get("BAcode")==null?"":mapBA.get("BAcode")+";";
			    	    baname +=mapBA.get("baname")==null?"":mapBA.get("baname")+";";
			    	}
			    	
			    }
			    // 业务主体代号
				dbObject.put("TradeEntityCode", BAcode);
				// 业务主体名称
				dbObject.put("TradeEntityName", baname);
				//员工代码
				dbObject.put("UserCode", DetailMap.get("BAcode"));
				//员工名称
				dbObject.put("UserName", DetailMap.get("baname"));
				// 柜台号
				dbObject.put("DeptCode", DetailMap.get("counterCode"));
				// 柜台名称
				dbObject.put("DeptName", map.get("counterName"));
				// 发生时间
				dbObject.put("OccurTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
			    // 日志正文
			    dbObject.put("Content", "ba管理");
			}
			else if(MessageConstants.MSG_BAS_INFO.equals(map.get("tradeType"))){
				// 业务主体
			    dbObject.put("TradeEntity", "1");
				// 业务主体代号
				dbObject.put("TradeEntityCode", DetailMap.get("employeeCode"));
				// 业务主体名称
				dbObject.put("TradeEntityName", DetailMap.get("employeeName"));
				//员工代码
				dbObject.put("UserCode", DetailMap.get("employeeCode"));
				//员工名称
				dbObject.put("UserName", DetailMap.get("employeeName"));
				//岗位名称名称
				dbObject.put("UserPost", DetailMap.get("categoryName"));
				// 柜台名称
				dbObject.put("DeptCode", DetailMap.get("counterCode"));
				// 柜台名称
				dbObject.put("DeptName", DetailMap.get("counterName"));
				if("1".equals(attType)){
					// 到柜时间
					dbObject.put("OccurTime", DetailMap.get("arrTime"));
					// 日志正文
					dbObject.put("Content", "到达了");
				}else{
					// 离柜时间
				    dbObject.put("OccurTime", DetailMap.get("leaTime"));
				    // 日志正文
				    dbObject.put("Content", "离开了");
				}
			}
			else if(MessageConstants.MSG_MEMBER_MV.equals(map.get("tradeType"))){
				// 业务主体
				dbObject.put("TradeEntity", "0");
				// 业务主体代号
				dbObject.put("TradeEntityCode", DetailMap.get("memberCode"));
				// 业务主体名称
				dbObject.put("TradeEntityName", DetailMap.get("memName"));
				//操作人员代号
				dbObject.put("UserCode", DetailMap.get("BAcode"));
				//操作人员名称
				dbObject.put("UserName", DetailMap.get("employeeName"));
				//岗位名称名称
				dbObject.put("UserPost", DetailMap.get("categoryName"));
				// 柜台名称
				dbObject.put("DeptCode", DetailMap.get("counterCode"));
				// 柜台名称
				dbObject.put("DeptName", map.get("counterName"));
				//发生时间
				dbObject.put("OccurTime", DetailMap.get("visitDate"));
				dbObject.put("Content", "会员回访");
			}else if(MessageConstants.MSG_MEMBER_POINT_MY.equals(map.get("tradeType"))){
	             // 业务主体
                dbObject.put("TradeEntity", "0");
                // 业务主体代号
                dbObject.put("TradeEntityCode", map.get("membercode"));
                // 业务主体名称
                dbObject.put("TradeEntityName", map.get("memName"));
                //发生时间
                dbObject.put("OccurTime", map.get("caltime"));
                dbObject.put("Content", "会员积分");
			}else if(MessageConstants.MSG_BAATTENDANCE.equals(map.get("tradeType"))){
		        // 业务主体
		        dbObject.put("TradeEntity", "1");
	            // 业务主体代号
                dbObject.put("TradeEntityCode", map.get("employeeCode"));
                // 业务主体名称
                dbObject.put("TradeEntityName", map.get("employeeName"));
		        // 操作人员代号
		        dbObject.put("UserCode", map.get("employeeCode"));
		        // 操作人员名称
		        dbObject.put("UserName", map.get("employeeName"));
		        // 岗位名称名称
		        dbObject.put("UserPost", map.get("categoryName"));
		        // 柜台号
		        dbObject.put("DeptCode", map.get("counterCode"));
		        // 柜台名称
		        dbObject.put("DeptName", map.get("counterName"));
		        // 发生时间
		        dbObject.put("OccurTime", map.get("attendanceTime"));
		        String attendanceType = ConvertUtil.getString(map.get("attendanceType"));
		        // 日志正文
		        if(attendanceType.equals("1")){
		            dbObject.put("Content", "普通考勤信息-上班");
		        }else if(attendanceType.equals("0")){
		            dbObject.put("Content", "普通考勤信息-下班");
		        }
			}else if(MessageConstants.MSG_MEMBER_ACTIVE.equals(map.get("tradeType"))){
			    // 业务主体
			    dbObject.put("TradeEntity", "0");
			    // 业务主体代号
			    dbObject.put("TradeEntityCode", map.get("memberCode"));
			    //发生时间
			    dbObject.put("OccurTime", map.get("activeTime"));
			    dbObject.put("Content", "会员激活");
			}else if(MessageConstants.MSG_MEMBER_DXCJ.equals(map.get("tradeType"))){
                // 业务主体
                dbObject.put("TradeEntity", "0");
                // 业务主体代号
                dbObject.put("TradeEntityCode", map.get("memberCode"));
                //发生时间
                dbObject.put("OccurTime", map.get("replyTime"));
                dbObject.put("Content", "短信回复信息采集");
            }
			map.put("dbObject", dbObject);
//			binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	}

	@Override
	public void selMessageInfo(Map map) throws CherryMQException {
		String tradeType = (String) map.get("tradeType");
		List detailDataDTOList = (List) map.get("detailDataDTOList");
		Map memberDetailMap = (HashMap) detailDataDTOList.get(0);
		// 设定柜台Code
		map.put("counterCode", memberDetailMap.get("countercode"));
		// 设定会员卡号
		map.put("memberCode", memberDetailMap.get("memberCode"));
		
		if (MessageConstants.MSG_MEMBER.equals(tradeType)){
			String countercode = (String)memberDetailMap.get("countercode");
			if(countercode != null && !"".equals(countercode)) {
				// 取得部门信息
				Map resultMemMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

				if (resultMemMap != null && resultMemMap.get("organizationID") != null) {
					// 设定部门ID
					map.put("organizationID", resultMemMap.get("organizationID"));
					map.put("counterName", resultMemMap.get("counterName"));
					map.put("counterKind", resultMemMap.get("counterKind"));
				} else {
					// 没有查询到相关部门信息
					MessageUtil.addMessageWarning(map, "柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
				}
			}
		} else {
			// 取得部门信息
			Map resultMemMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

			if (resultMemMap != null && resultMemMap.get("organizationID") != null) {
				// 设定部门ID
				map.put("organizationID", resultMemMap.get("organizationID"));
				map.put("counterName", resultMemMap.get("counterName"));
				map.put("counterKind", resultMemMap.get("counterKind"));
			} else {
				// 没有查询到相关部门信息
				MessageUtil.addMessageWarning(map, "柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
			}
		}

		// 查询岗位类别ID
		if (MessageConstants.MSG_BA_INFO.equals(tradeType)){
			List resultList = (List)binBEMQMES03_Service.selPositionCategoryList(map);
			if (null!=resultList && !resultList.isEmpty()){
				for (int i=0;i<resultList.size();i++){
					Map resultMap = (Map)resultList.get(i);
					if ("01".equals(resultMap.get("categoryCode"))){
						// BA的岗位类别ID
						map.put("baPositionID", resultMap.get("positionCategoryID"));
					}else if ("02".equals(resultMap.get("categoryCode"))){
						// BAS的岗位类别ID
						map.put("basPositionID", resultMap.get("positionCategoryID"));
					}
				}
			}
			
		}
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
		setInsertInfoMapKey(map);
	}

	@Override
	public void setDetailDataInfo(List detailDataList, Map map) throws CherryMQException {
		String tradeType = (String) map.get("tradeType");
		Map resultMemMap = null;
		// 循环明细信息
		for (int i = 0; i < detailDataList.size(); i++) {
			Map memberDetailMap = (HashMap) detailDataList.get(i);
			this.setInsertInfoMapKey(memberDetailMap);
			// 设置组织ID
			memberDetailMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 设置品牌ID
			memberDetailMap.put("brandInfoID", map.get("brandInfoID"));
			// 设定子类型
			memberDetailMap.put("subType", map.get("subType"));
			// 设定数据来源
			memberDetailMap.put("sourse", map.get("sourse"));

			memberDetailMap.put("counterCode", memberDetailMap.get("countercode"));

			// 设定部门信息
			memberDetailMap.put("organizationID", map.get("organizationID"));

			if (MessageConstants.MSG_MEMBER.equals(tradeType)) {
				// 会员类型消息

				// 查询会员详细信息
				resultMemMap = binBEMQMES03_Service.selMemberInfo(memberDetailMap);

				if (resultMemMap != null) {
					memberDetailMap.putAll(resultMemMap);
					map.put("oldVersion", resultMemMap.get("oldVersion"));
				}
				String version = (String)memberDetailMap.get("version");
				if(version != null && !"".equals(version)) {
					map.put("version", version);
				} else {
					memberDetailMap.remove("version");
				}
				
				// 推荐会员
				String Referrer = (String)memberDetailMap.get("referrer");
				if(Referrer != null && !"".equals(Referrer)) {
					// 查询推荐会员ID
					Map<String, Object> referrerMap = binBEMQMES03_Service.selReferrerID(memberDetailMap);
					if(referrerMap != null && !referrerMap.isEmpty()) {
						memberDetailMap.putAll(referrerMap);
					}
				}
				// 是否测试柜台
				Object counterKind = map.get("counterKind");
				// 如果会员发卡柜台为测试柜台那么该会员设为测试会员
				if(counterKind != null && "1".equals(counterKind.toString())) {
					memberDetailMap.put("testType", "0");
				} else {
					memberDetailMap.put("testType", "1");
				}
				
				String tradeCounterCode = (String)map.get("tradeCounterCode");
				if(tradeCounterCode != null && !"".equals(tradeCounterCode)) {
					// 领卡柜台
					memberDetailMap.put("tradeCounterCode", tradeCounterCode);
				} else {// 领卡柜台不存在的场合用发卡柜台代替
					memberDetailMap.put("tradeCounterCode", memberDetailMap.get("countercode"));
				}
				String tradeBAcode = (String)map.get("tradeBAcode");
				if(tradeBAcode != null && !"".equals(tradeBAcode)) {
					// 业务操作BA代码
					memberDetailMap.put("tradeBAcode", tradeBAcode);
				} else {// 领卡BA不存在的场合用发卡BA代替
					memberDetailMap.put("tradeBAcode", memberDetailMap.get("BAcode"));
				}
				
//				String billCode = (String)map.get("tradeNoIF");
//				if(billCode != null && billCode.length() > 17) {
//					String subBillCode = billCode.substring(billCode.length()-17);
//					String tradeGrantDate = subBillCode.substring(0, 8);
//					String tradeGrantTime = subBillCode.substring(8, 10)+":"+subBillCode.substring(10, 12)+":"+subBillCode.substring(12, 14);
//					// 领卡日期
//					memberDetailMap.put("tradeGrantDate", tradeGrantDate);
//					// 领卡时间
//					memberDetailMap.put("tradeGrantTime", tradeGrantTime);
//				}
				
				String memMobile = (String)memberDetailMap.get("memMobile");
				if(memMobile == null || "".equals(memMobile)) {
					memberDetailMap.remove("memMobile");
				}
				
				// 设定性别code值
				memberDetailMap.put("genderCode", memberDetailMap.get("memSex"));

				String memChangeTime = (String) memberDetailMap.get("memChangeTime");
				if (memChangeTime != null && !"".equals(memChangeTime)) {
					memberDetailMap.put("memChangeDate", memChangeTime.split(" ")[0].replace("-", ""));
					memberDetailMap.put("memChangeTime", memChangeTime.split(" ")[1]);
				}
				String memGranddate = (String) memberDetailMap.get("memGranddate");
				if (memGranddate != null && !"".equals(memGranddate) && memGranddate.length() > 10) {
					memGranddate = memGranddate.substring(0, 10);
					memberDetailMap.put("memGranddate", memGranddate.replaceAll("-", ""));
				}
				// 领卡日期
				memberDetailMap.put("tradeGrantDate", memberDetailMap.get("memGranddate"));
				// 领卡时间
				memberDetailMap.put("tradeGrantTime", memberDetailMap.get("joinTime"));

				String memProvince = (String)memberDetailMap.get("memProvince");
				String memCity = (String)memberDetailMap.get("memCity");
				String memCounty = (String)memberDetailMap.get("memCounty");
				boolean regionError = false;
				if(memCity != null && memCity.contains("/")) {
					String[] memCitys = memCity.split("/");
					memCity = memCitys[memCitys.length-1];
					memberDetailMap.put("memCity", memCity);
				}
				if(memCounty != null && memCounty.contains("/")) {
					String[] memCountys = memCounty.split("/");
					memCounty = memCountys[memCountys.length-1];
					memberDetailMap.put("memCounty", memCounty);
				}
				if(memCounty != null && !"".equals(memCounty)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCountyName(memProvince, memCity, memCounty);
					if(regionInfo != null) {
						// 设置省ID
						memberDetailMap.put("provinceID", regionInfo.get("provinceId"));
						// 设置市ID
						memberDetailMap.put("cityID", regionInfo.get("cityId"));
						// 设置区县ID
						memberDetailMap.put("countyID", regionInfo.get("countyId"));
						memberDetailMap.put("regionID", regionInfo.get("cityId"));
					} else {
						regionError = true;
					}
				} else if(memCity != null && !"".equals(memCity)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(memProvince, memCity);
					if(regionInfo != null) {
						// 设置省ID
						memberDetailMap.put("provinceID", regionInfo.get("provinceId"));
						// 设置市ID
						memberDetailMap.put("cityID", regionInfo.get("cityId"));
						memberDetailMap.put("regionID", regionInfo.get("cityId"));
					} else {
						regionError = true;
					}
				} else if(memProvince != null && !"".equals(memProvince)) {
					Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByProvinceName(memProvince);
					if(regionInfo != null) {
						// 设置省ID
						memberDetailMap.put("provinceID", regionInfo.get("provinceId"));
					} else {
						regionError = true;
					}
				}
				if(regionError) {
					try {
						map.put("errType", "1");
						//没有查询到相关省市信息
						MessageUtil.addMessageWarning(map, "省份为\""+memProvince+"\""+"城市为\""+memCity+"\""+"区县为\""+(memCounty == null ? "":memCounty)+"\""+MessageConstants.MSG_ERROR_65);
					} catch (Exception e) {
						
					} finally {
						map.remove("errType");
					}
				}
//				if(memCity != null && !"".equals(memCity)) {
//					if(memCity.contains("/")) {
//						String[] memCitys = memCity.split("/");
//						memberDetailMap.put("memCity", memCitys[memCitys.length-1]);
//					}
//					// 查询区域省市id
//					List resultList = binBEMQMES03_Service.selProvinceCityID(memberDetailMap);
//					if (resultList != null && !resultList.isEmpty()) {
//						Map resultMap = (HashMap) resultList.get(0);
//						// 设置省ID
//						memberDetailMap.put("provinceID", resultMap.get("provinceID"));
//						// 设置市ID
//						memberDetailMap.put("cityID", resultMap.get("cityID"));
//						memberDetailMap.put("regionID", resultMap.get("cityID"));
//					} else {
//						try {
//							map.put("errType", "1");
//							//没有查询到相关省市信息
//							MessageUtil.addMessageWarning(map, "省份为\""+memProvince+"\""+"城市为\""+memCity+"\""+MessageConstants.MSG_ERROR_65);
//						} catch (Exception e) {
//							
//						} finally {
//							map.remove("errType");
//						}
//					}
//				} else {
//					if(memProvince != null && !"".equals(memProvince)) {
//						// 查询区域省id
//						List resultList = binBEMQMES03_Service.selProvinceID(memberDetailMap);
//						if (resultList != null && !resultList.isEmpty()) {
//							Map resultMap = (HashMap) resultList.get(0);
//							// 设置省ID
//							memberDetailMap.put("provinceID", resultMap.get("provinceID"));
//						} else {
//							try {
//								map.put("errType", "1");
//								//没有查询到相关省市信息
//								MessageUtil.addMessageWarning(map, "省份为\""+memProvince+"\""+"城市为\""+memCity+"\""+MessageConstants.MSG_ERROR_65);
//							} catch (Exception e) {
//								
//							} finally {
//								map.remove("errType");
//							}
//						}
//					}
//				}
				
				String birthDay = (String) memberDetailMap.get("memBirthday");
				if (birthDay != null && !"".equals(birthDay)) {
					// 设置会员生日年份
					memberDetailMap.put("birthYear", birthDay.substring(0, 4));
					// 设置会员生日月日
					memberDetailMap.put("birthDay", birthDay.substring(4, 8));
				}
				
				String bacode = (String)memberDetailMap.get("BAcode");
				if(bacode != null && !"".equals(bacode)) {
					//查询BA岗位信息
					Map resultEmpMap = binBEMQMES03_Service.selEmpAndPosinfo(memberDetailMap);
					if(resultEmpMap!=null){
						memberDetailMap.put("employeeID", resultEmpMap.get("employeeID"));
						memberDetailMap.put("employeeName", resultEmpMap.get("employeeName"));
						memberDetailMap.put("categoryCode", resultEmpMap.get("categoryCode"));
						memberDetailMap.put("categoryName", resultEmpMap.get("categoryName"));
					}else{
						//没有查询到相关员工信息
						MessageUtil.addMessageWarning(map, "员工号为\""+memberDetailMap.get("BAcode")+"\""+MessageConstants.MSG_ERROR_07);
					}
				}
				
				// 外部导入的不是新后台维护的等级
				String memLevelExt = (String)memberDetailMap.get("memLevelExt");
				if(memLevelExt != null && !"".equals(memLevelExt)) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.putAll(memberDetailMap);
					param.put("memLevel", memLevelExt);
					//查询会员等级信息
					Map resultLevelMap = binBEMQMES03_Service.selMemberLevel(param);
					if(resultLevelMap != null) {
						memberDetailMap.put("memberLevel", resultLevelMap.get("memLevel"));
					} else {
						//没有查询到对应等级信息
						MessageUtil.addMessageWarning(map, "会员等级为\""+memLevelExt+"\""+MessageConstants.MSG_ERROR_71);
					}
				}
				// 等级有效日期设置
				String levelStartDateExt = (String)memberDetailMap.get("levelStartDateExt");
				String levelEndDateExt = (String)memberDetailMap.get("levelEndDateExt");
				if(levelStartDateExt != null && !"".equals(levelStartDateExt)) {
					memberDetailMap.put("levelStartDate", levelStartDateExt);
				}
				if(levelEndDateExt != null && !"".equals(levelEndDateExt)) {
					memberDetailMap.put("levelEndDate", levelEndDateExt);
				}
				
				String active = (String)memberDetailMap.get("active");
				if(active == null || "".equals(active)) {
					memberDetailMap.remove("active");
				}
				String activeDate = (String)memberDetailMap.get("activeDate");
				if(activeDate == null || "".equals(activeDate)) {
					memberDetailMap.remove("activeDate");
				}
			} else if (MessageConstants.MSG_BA_INFO.equals(tradeType)) {
				// BA类型消息
				
				// 设定岗位类型ID
				String baFlag = (String)memberDetailMap.get("flag");
				if (null!=baFlag && !"".equals(baFlag)){
					memberDetailMap.put("positionCategoryID", baFlag.equals("1")?String.valueOf(map.get("baPositionID")):String.valueOf(map.get("basPositionID")));
				}
				
			} else if (MessageConstants.MSG_BAS_INFO.equals(tradeType)) {
				// BAS类型消息

				String attDate = (String) memberDetailMap.get("attDate");//考勤日期
				String arrTime = (String) memberDetailMap.get("arrTime");//到柜时间
				String leaTime = (String) memberDetailMap.get("leaTime");//离柜时间
				if (null != attDate && !"".equals(attDate)){
					if(null != arrTime && !"".equals(arrTime)) {
						// 设定到柜时间
						memberDetailMap.put("arrTime", attDate.substring(0, 4) + "-" + attDate.substring(4, 6) + "-" + attDate.substring(6, 8) +" "+
								arrTime.substring(0, 2) + ":" + arrTime.substring(2, 4) + ":"+ arrTime.substring(4, 6));
				   }
				   if(null != leaTime && !"".equals(leaTime)) {
						// 设定离柜时间
					   memberDetailMap.put("leaTime", attDate.substring(0, 4) + "-" + attDate.substring(4, 6) + "-" + attDate.substring(6, 8) +" "+
								leaTime.substring(0, 2) + ":" + leaTime.substring(2, 4) + ":"+ leaTime.substring(4, 6));
				   }
				}
				
				//查询U盘对应的员工信息
				Map resultEmpMap = binBEMQMES99_Service.selUdiskInfo(memberDetailMap);
				if(resultEmpMap!=null){
					memberDetailMap.put("employeeID", resultEmpMap.get("employeeID"));
					memberDetailMap.put("employeeCode", resultEmpMap.get("employeeCode"));
					memberDetailMap.put("employeeName", resultEmpMap.get("employeeName"));
					memberDetailMap.put("categoryCode", resultEmpMap.get("categoryCode"));
					memberDetailMap.put("categoryName", resultEmpMap.get("categoryName"));
				}else{
					//U盘没有关联到相关员工
					MessageUtil.addMessageWarning(map, "U盘系列号为\""+memberDetailMap.get("UDiskSN")+"\""+MessageConstants.MSG_ERROR_29);
				}
			} else if (MessageConstants.MSG_MEMBER_MV.equals(tradeType)) {
				resultMemMap = binBEMQMES99_Service.selMemberInfo(memberDetailMap);
				if (resultMemMap != null) {
					memberDetailMap.putAll(resultMemMap);
				}else{
	            	// 没有查询到相关会员信息
	    			MessageUtil.addMessageWarning(map, "会员号为\""+memberDetailMap.get("memberCode")+"\""+MessageConstants.MSG_ERROR_34);
				}
				//查询员工信息,以及岗位信息
				Map resultEmpMap = binBEMQMES03_Service.selEmpAndPosinfo(memberDetailMap);
				if(resultEmpMap!=null){
					memberDetailMap.put("employeeID", resultEmpMap.get("employeeID"));
					memberDetailMap.put("employeeName", resultEmpMap.get("employeeName"));
					memberDetailMap.put("categoryCode", resultEmpMap.get("categoryCode"));
					memberDetailMap.put("categoryName", resultEmpMap.get("categoryName"));
				}else{
					//没有查询到相关员工信息
					MessageUtil.addMessageWarning(map, "员工号为\""+memberDetailMap.get("BAcode")+"\""+MessageConstants.MSG_ERROR_29);
				}
			}
		}
//		judgeIfIsRepeatData(map);
	}

	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES03");
		map.put("createPGM", "BINBEMQMES03");
		map.put("updatedBy", "BINBEMQMES03");
		map.put("updatePGM", "BINBEMQMES03");
        map.put("CreatedBy", "BINBEMQMES03");
        map.put("CreatePGM", "BINBEMQMES03");
        map.put("UpdatedBy", "BINBEMQMES03");
        map.put("UpdatePGM", "BINBEMQMES03");
	}
	@SuppressWarnings("unused")
	private void judgeIfIsRepeatData(Map map) throws CherryMQException {

			// 取得会员详细信息Map
		Map memberDetailMap = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		if(map.get("tradeType").equals("MB")){
				String subType = (String) map.get("subType");
			if (MessageConstants.MEM_TYPE_NEW_MEMBER.equals(subType)) {
				 // 会员
				 int count =  binBEMQMES03_Service.selMemberIsRepeatData(memberDetailMap);
				 if(count>0){
					//是重复的会员新增数据
					MessageUtil.addMessageWarning(map, "会员号为\""+memberDetailMap.get("memberCode")+"\""+MessageConstants.MSG_ERROR_25);
				 }
		    }
		}else if (MessageConstants.MSG_BAS_INFO.equals(map.get("tradeType"))) {
			 // bas考勤
			 int count =  binBEMQMES03_Service.selBasIsRepeatData(memberDetailMap);
			 if(count>0){
				//是重复的BAS考勤数据
				MessageUtil.addMessageWarning(map, "U盘系列号为\""+memberDetailMap.get("UDiskSN")+"\""+MessageConstants.MSG_ERROR_31);
			 }
	    }
	}
	
	/**
	 * 发送U盘绑定信息    新后台到终端
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 * */
	private void sendUC(Map map) throws Exception{
		String 	organizationInfoID = map.get("organizationInfoID").toString();
		String 	brandInfoID = map.get("brandInfoID").toString();
		//指定code查询该配置项是禁用还是启用
		if(binOLCM14_BL.isConfigOpen("1030", organizationInfoID, brandInfoID)){
			
			//查询新柜台主管对应的U盘序列号
			map.put("employeeID", map.get("oldEmployeeBas"));
			Map<String,Object> oldUCMap = binBEMQMES99_Service.selUdiskSNByEempID(map);
			//查询新柜台主管对应的U盘序列号
			map.put("employeeID", map.get("newEmployeeBas"));
			Map<String,Object> newUCMap = binBEMQMES99_Service.selUdiskSNByEempID(map);
			//MQ消息 DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			//U盘绑定信息MQ主业务 DTO
			UdiskBindMainDTO udiskMainDTO = new UdiskBindMainDTO();
			//U盘绑定信息MQ明细业务  List
			List<UdiskBindDetailDTO> udiskDetailDTOs = new ArrayList<UdiskBindDetailDTO>();
	
			List<String> countercodes = new ArrayList<String>();
			countercodes.add(map.get("counterCode").toString());
			//需删除的绑定
			UdiskBindDetailDTO udiskDetailDTO1 = new UdiskBindDetailDTO();
			//设置柜台号
			udiskDetailDTO1.setCountercode(countercodes);
			udiskDetailDTO1.setFlag(MessageConstants.FLAG_0_U_C);
			if(oldUCMap!=null&&oldUCMap.get("UdiskSN")!=null){
				udiskDetailDTO1.setUdiskSN(oldUCMap.get("UdiskSN").toString().trim());
				udiskDetailDTOs.add(udiskDetailDTO1);
			}
			//需增加的绑定
			UdiskBindDetailDTO udiskDetailDTO2 = new UdiskBindDetailDTO();
			//设置柜台号
			udiskDetailDTO2.setCountercode(countercodes);
			udiskDetailDTO2.setFlag(MessageConstants.FLAG_1_U_C);
			if(newUCMap!=null&&newUCMap.get("UdiskSN")!=null){
				udiskDetailDTO2.setUdiskSN(newUCMap.get("UdiskSN").toString().trim());
				udiskDetailDTOs.add(udiskDetailDTO2);
			}
			if(!udiskDetailDTOs.isEmpty()){
				udiskMainDTO.setUdiskBindDetailDTOList(udiskDetailDTOs);//U盘绑定信息MQ明细业务 List
				udiskMainDTO.setBrandCode(map.get("brandCode").toString());//品牌
				udiskMainDTO.setTradeType(MessageConstants.BUSINESS_TYPE_U_C);//业务类型
				String billCode = binOLCM03_BL.getTicketNumber(map.get("organizationInfoID").toString(), map.get("brandInfoID").toString(), "BINBEMQMES03", MessageConstants.BUSINESS_TYPE_U_C);
				udiskMainDTO.setTradeNoIF(billCode);//单据号
				mqInfoDTO.setData(udiskMainDTO.getMQMsg());//设定发送的消息
				
				//设置插入MQ_LOG 需要的值
				mqInfoDTO.setOrganizationInfoId(Integer.parseInt(organizationInfoID));
				mqInfoDTO.setBrandInfoId(Integer.parseInt(brandInfoID));
				mqInfoDTO.setBillType(MessageConstants.BUSINESS_TYPE_U_C);
				mqInfoDTO.setBillCode(billCode);
				mqInfoDTO.setCounterCode(map.get("counterCode").toString());
				mqInfoDTO.setCreatedBy("BINBEMQMES03");
				mqInfoDTO.setCreatePGM("BINBEMQMES03");
				mqInfoDTO.setUpdatedBy("BINBEMQMES03");
				mqInfoDTO.setUpdatePGM("BINBEMQMES03");
				//设置插入MongDB需要的值
				DBObject dbObject = new BasicDBObject();
				dbObject.put("OrgCode", map.get("orgCode"));
				dbObject.put("BrandCode", map.get("brandCode"));
				dbObject.put("TradeType", MessageConstants.BUSINESS_TYPE_U_C);
				dbObject.put("TradeNoIF", billCode);
				dbObject.put("ModifyCounts", "0");
				dbObject.put("OccurTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				dbObject.put("Content", udiskMainDTO.getMQMsg());
				mqInfoDTO.setDbObject(dbObject);	
				try {
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				} catch (Exception e) {
					throw new CherryMQException(e.getMessage()+";There is an error that has occured in BINOLMQCOM01_BL's sendMQMsg");
				}
			}
		}
	}
	
	/**
	 * 相同会员合并处理
	 * 
	 * @param map
	 * @return boolean true: 不需要重算  false: 需要重算
	 * @throws Exception 
	 * */
	public boolean mergeMemberInfo(Map<String, Object> map) throws Exception {
		// 相同会员合并处理
		String reCalcDate = mergeMember(map);
		
		//组织代号
		String orgCode = (String) map.get("orgCode");
		// 品牌代码，即品牌简称
		String brandCode = (String) map.get("brandCode");
		// 取得等级、积分重算处理器
		CherryMessageHandler_IF cherryMessageHandler = binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, MessageConstants.MESSAGE_TYPE_RU);
		if(cherryMessageHandler != null) {
			// 新卡存在业务数据的场合，合并会员后需要对原来的会员进行等级重算
			if(reCalcDate != null) {
				map.put("reCalcDate", reCalcDate);
				map.put("reCalcType", "0");
				// 插入重算信息表
				binBEMQMES03_Service.insertReCalcInfo(map);
				// 发送MQ重算消息进行实时重算
				sendReCalcMsg(map);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 相同会员合并处理
	 * 
	 * @param map
	 * @return String 重算时间
	 * @throws Exception 
	 * */
	public String mergeMember(Map<String, Object> map) throws Exception {
		// 把新会员卡号合并到老会员卡号中
		binBEMQMES03_Service.updNewMemberCardInfo(map);
		// 把新会员对应的销售记录的会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updateSaleMemId(map);
		// 把新会员对应的活动履历记录的会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updateCampaignHistoryMemId(map);
		// 把新会员对应的会员使用化妆次数积分明细记录的会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updateMemUsedMemId(map);
		// 查询新会员对应的答卷信息
		Map<String, Object> paperAnswerMap = binBEMQMES03_Service.getNewMemPaperAnswer(map);
		// 新会员存在答卷信息的场合
		if(paperAnswerMap != null) {
			// 把老会员对应的答卷信息更新成无效
			binBEMQMES03_Service.updatePaperAnswerValid(map);
			// 把新会员对应的答卷信息的会员ID更新成老会员对应的会员ID
			binBEMQMES03_Service.updatePaperAnswer(map);
		}
		// 把新会员对应的推荐会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updateReferrerID(map);
		// 查询新会员的扩展信息
		Map<String, Object> memberExtInfo = binBEMQMES03_Service.getMemberExtInfo(map);
		if(memberExtInfo != null) {
			memberExtInfo.put("memberInfoId", map.get("memberInfoID"));
			this.setInsertInfoMapKey(memberExtInfo);
			// 更新会员扩展属性
			binBEMQMES99_Service.updMemberExtInfo(memberExtInfo);
			// 删除新会员扩展信息
			binBEMQMES03_Service.delNewMemberExtInfo(map);
		}
		// 删除地址信息
		binBEMQMES03_Service.delAddressInfo(map);
		// 删除新会员地址信息
		binBEMQMES03_Service.delNewMemberAddress(map);
		// 删除新会员信息
		binBEMQMES03_Service.delNewMemberInfo(map);
		// 积分初始导入时间
		String initialTime = null;
		// 查询新会员的初始积分信息
		Map<String, Object> memberPointMap = binBEMQMES03_Service.getMemberPoint(map);
		// 新会员积分信息存在的场合，把新会员积分信息合并到老会员中
		if(memberPointMap != null && !memberPointMap.isEmpty()) {
			memberPointMap.put("memberInfoID", map.get("memberInfoID"));
			memberPointMap.put("newMemberInfoID", map.get("newMemberInfoID"));
			this.setInsertInfoMapKey(memberPointMap);

			// 更新会员积分信息
			int result = binBEMQMES03_Service.updateMemberPoint(memberPointMap);
			if(result == 0) {
				// 把新会员积分信息更新成老会员积分信息
				binBEMQMES03_Service.changeMemberPoint(map);
			} else {
				// 删除新会员积分信息
				binBEMQMES03_Service.delNewMemberPoint(map);
			}
			initialTime = (String)memberPointMap.get("initialTime");
		}
		// 查询新卡对应的会员产生的最早业务时间
		String reCalcDate = binBEMQMES03_Service.getMinTicketDate(map);
		// 重算时间取最早业务时间和初始积分导入时间中更早的那个
		if(reCalcDate != null && !"".equals(reCalcDate)) {
			if(initialTime != null && !"".equals(initialTime)) {
				if(reCalcDate.compareTo(initialTime) > 0) {
					reCalcDate = initialTime;
				}
			}
		} else {
			if(initialTime != null && !"".equals(initialTime)) {
				reCalcDate = initialTime;
			}
		}
		// 把新会员对应的规则执行履历记录的会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updateRuleRecordMemId(map);
		// 把新会员对应的积分变化记录的会员ID更新成老会员对应的会员ID
		binBEMQMES03_Service.updatePointChangeMemId(map);
		// 新卡存在业务数据的场合，合并会员后需要对原来的会员进行等级重算，返回重算时间
		return reCalcDate;
	}
	
	/**
	 * 判断是否最新的会员单MQ消息
	 * 
	 * @param map
	 * @throws Exception 
	 * */
	public boolean isLatestMes(Map<String, Object> map) throws Exception {
		
		DBObject condition = new BasicDBObject();
		condition.put("OrgCode", map.get("orgCode"));
		condition.put("BrandCode", map.get("brandCode"));
		condition.put("TradeType", map.get("tradeType"));
		condition.put("TradeEntityCode", map.get("tradeEntityCode"));
		String billCode = (String)map.get("tradeNoIF");
		if(billCode != null && billCode.length() > 17) {
			condition.put("MbTime", new BasicDBObject("$gt", billCode.substring(billCode.length()-17)));
			DBObject dbObject = MongoDB.findOne(MessageConstants.MQ_BUS_LOG_COLL_NAME, condition);
			if(dbObject != null) {
				return false;
			}
		}
		if ("1".equals(map.get("MEMINFO_REGFLG"))) {
			return true;
		}
		String version = (String)map.get("version");
		Object oldVersion = map.get("oldVersion");
		if(version != null && !"".equals(version)) {
			if(oldVersion != null && !"".equals(oldVersion.toString())) {
				if(Integer.parseInt(version) <= Integer.parseInt(oldVersion.toString())) {
					return false;
				}
			}
		} else {
//			if(oldVersion != null && !"".equals(oldVersion.toString())) {
//				return false;
//			}
		}
		return true;
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
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_MR;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
				Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
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
		mainData.put("memberInfoId", map.get("memberInfoID"));
		mainData.put("memberClubId", map.get("memberClubID"));
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
	
	/**
	 * 更新会员卡信息
	 * 
	 * @param map 会员信息
	 * @throws Exception 
	 */
	public void updMemCardValid(Map<String, Object> memMap, Map<String, Object> map) throws Exception {
		
		// 卡有效区分
		int cardValidFlag = (Integer)memMap.get("cardValidFlag");
		// 将卡号设置成有效
		memMap.put("cardValidFlagUpd", "1");
//		// 卡为无效的场合，需要把卡更新成有效
//		if(cardValidFlag == 0) {
//			// 查询会员的有效卡数量
//			int memCardCount = binBEMQMES03_Service.getMemCardCount(memMap);
//			// 会员不存在有效卡，把会员卡更新成有效，否则报错
//			if(memCardCount == 0) {
//				// 更新会员卡信息
//				binBEMQMES03_Service.updMemberCardInfo(memMap);
//			} else {
//				logger.error(MessageConstants.MSG_ERROR_56);
////				// 会员已经存在新卡，不能再激活老卡
////				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_56);
//			}
//		} else {
//			// 更新会员卡信息
//			binBEMQMES03_Service.updMemberCardInfo(memMap);
//		}
		
		// 卡为无效的场合，需要把卡更新成有效，并且把其他卡更新成无效
		if(cardValidFlag == 0) {
			// 把会员卡都更新成无效
			binBEMQMES03_Service.delOldMemberCard(memMap);
		}
		// 更新会员卡信息
		binBEMQMES03_Service.updMemberCardInfo(memMap);
	}

	/**
	 * 更新会员扩展信息表信息，如果没有则新增
	 *
	 * @param memMap 会员信息
	 * @throws Exception
	 */
	public void updMemberExtInfo(Map<String, Object> memMap) throws Exception {
		//pos发送MQ消息，key值映射
		convertToDegreeeAttr(memMap);

		//更新会员信息扩展表信息
		int updateCount =binBEMQMES03_Service.updMemberExtInfo(memMap);
		if(updateCount == 0) {
			// 添加会员扩展信息
			binBEMQMES03_Service.insertMemberExtInfo(memMap);
		}

		// 更改会员完善度
		String minJoinDate = binOLCM14_BL.getConfigValue("1402", ConvertUtil.getString(memMap.get("organizationInfoID")),ConvertUtil.getString(memMap.get("brandInfoID")));

		if(!StringUtils.isEmpty(minJoinDate)) { // 若未开启最小入会时间直接跳过完善度功能
			String joinDate = ConvertUtil.getString(memMap.get("memGranddate")); // 会员入会时间
			joinDate = StringUtils.isEmpty(joinDate) ? ConvertUtil.getString(memMap.get("joinDate")) : joinDate;
			if (joinDate != null && DateUtil.compareDate(joinDate, minJoinDate) >= 0) {
				// 更新会员完善度
				updMemberInfoComplete(memMap);
			}
		}
	}

	/**
	 * pos发送MQ消息，key值映射
	 * @param map
     */
	private void convertToDegreeeAttr(Map<String,Object> map) {
		map.put("mobilePhone", map.get("memMobile"));
		map.put("telephone", map.get("memPhone"));
		map.put("provinceId", map.get("provinceID"));
		map.put("cityId", map.get("cityID"));
		map.put("gender", map.get("genderCode"));
		map.put("address", map.get("memAddress"));
		map.put("email", map.get("memMail"));
	}

	/**
	 * 更新会员完善度
	 *
	 */
	public void updMemberInfoComplete(Map<String, Object> map) throws Exception {
		// 组织id
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
		// 品牌id
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
		// 会员卡号
		String memCode = ConvertUtil.getString(map.get("memberCode"));

		List<String> keys = binOLCM31_BL.getMemCompleteRuleKey(organizationInfoId,brandInfoId);
		if(CollectionUtils.isEmpty(keys)) { // 若不存在规则直接跳过完善度功能
			return ;
		}

		// 获取所有规则设置属性的值
		Map<String,Object> params = new HashMap<String, Object>();
		for (String key : keys) {
			params.put(key, map.get(key));
		}

		// 获取会员最新百分比并更新会员百分比信息
		int percent = binOLCM31_BL.calMemCompletePercent(organizationInfoId, brandInfoId, params);
		binOLCM31_BL.updateCompletePercentByMemcode(organizationInfoId, brandInfoId, memCode , percent);

		// 获取会员最新积分并更新会员积分信息
		Map<String,Object> pointResult = binOLCM31_BL.calMemCompletePoint(organizationInfoId, brandInfoId, params, memCode);
		binOLCM31_BL.updateCompletePointByMemcode(organizationInfoId, brandInfoId, memCode, ConvertUtil.getInt(pointResult.get("pointTotal")), ConvertUtil.getString(pointResult.get("awardPoint")));

		int pointTotal = ConvertUtil.getInt(pointResult.get("pointTotal"));
		if(pointTotal != 0) { // 赠送积分不为0发送MQ同步会员积分
			map.put("ModifyPoint", pointTotal);
			binOLCM31_BL.sendPointsMQ(getPointMQParam(map));
		}

	}

	// 获取发送积分MQ参数
	private Map<String,Object> getPointMQParam(Map<String,Object> params) {
		logger.debug("BINBEMQMES03_BL getPointMQParam params -- >" + params.toString());
		params.put("employeeId",params.get("employeeID"));
		params.put("organizationInfoId",params.get("organizationInfoID"));
		params.put("brandInfoId",params.get("brandInfoID"));
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.putAll(params);
		returnMap.put("MemberCode",params.get("memberCode"));
		returnMap.put("BusinessTime", CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN));
		returnMap.put("Reason","会员积分完善度积分修改");

		Map<String, Object> comEmployeeInfo = binOLCM31_BL.getComEmployeeInfoById(params);
		if(!CollectionUtils.isEmpty(comEmployeeInfo)) {
			returnMap.put("EmployeeCode",comEmployeeInfo.get("employeeCode"));
		}
		returnMap.put("pointType","2"); // 1-总值维护，2-差值维护
		returnMap.put("MaintainType","1");
		logger.debug("BINBEMQMES03_BL getPointMQParam returnMap -- >" + returnMap.toString());
		return returnMap;
	}

	/**
     * 对会员回访数据带问卷进行处理
     * @throws CherryMQException 
     */
    @Override
    public void analyzeMemberMV(Map<String,Object> map) throws CherryMQException{
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        String counterCode = ConvertUtil.getString(map.get("counterCode"));
        String memberCode = ConvertUtil.getString(map.get("memberCode"));
        String employeeCode = ConvertUtil.getString(map.get("BAcode"));
        String visitTaskID = ConvertUtil.getString(map.get("visitTaskID"));
        String paperID = ConvertUtil.getString(map.get("paperID"));
        String visitResult = ConvertUtil.getString(map.get("visitResult"));
        String visitDate = ConvertUtil.getString(map.get("visitDate"));
        
        //查会员ID
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        map.put("MemCode", memberCode);
        Map<String,Object> memberInfo = binBEMQMES97_BL.getMemberInfo(map, true);
        int memberInfoID = CherryUtil.obj2int(memberInfo.get("BIN_MemberInfoID"));
        map.put("MemName", ConvertUtil.getString(memberInfo.get("Name")));
        
        //查部门ID
        map.put("CounterCode", counterCode);
        Map<String,Object> organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
        int organizationID = CherryUtil.obj2int(organizationInfo.get("BIN_OrganizationID"));
        map.put("CounterNameIF", ConvertUtil.getString(organizationInfo.get("CounterNameIF")));
        
        //查员工ID
        map.put("EmployeeCode", employeeCode);
        Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, true);
        int employeeID = CherryUtil.obj2int(employeeInfo.get("BIN_EmployeeID"));
        map.put("EmployeeName", ConvertUtil.getString(employeeInfo.get("EmployeeName")));
        map.put("CategoryName", ConvertUtil.getString(employeeInfo.get("CategoryName")));
        
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        //PaperID不为空，且明细中有数据，则说明使用了问卷，要将答卷信息写入【答卷主表】及【答卷明细表】
        int paperAnswerID = 0;
        if(!"".equals(paperID) && null != detailDataList && detailDataList.size()>0){
            Map<String,Object> paperAnswerMap = new HashMap<String,Object>();
            paperAnswerMap.put("BIN_PaperID", paperID);
            paperAnswerMap.put("BIN_OrganizationID", organizationID);
            paperAnswerMap.put("PaperType", MessageConstants.MSG_QUESTION_PERSON);
            paperAnswerMap.put("BACode", employeeCode);
            paperAnswerMap.put("BIN_MemberInfoID", memberInfoID);
            setInsertInfoMapKey(paperAnswerMap);
            paperAnswerID = binBEMQMES03_Service.insertPaperAnswer(paperAnswerMap);
            for(int i=0;i<detailDataList.size();i++){
                Map<String,Object> temp = detailDataList.get(i);
                temp.put("BIN_PaperAnswerID", paperAnswerID);
                temp.put("Answer", temp.get("answer"));
                
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BIN_PaperID", paperID);
                param.put("DisplayOrder", temp.get("questionNo"));
                int paperQuestionID = binBEMQMES97_BL.getQuestionID(map, param, true);
                temp.put("BIN_PaperQuestionID", paperQuestionID);
                setInsertInfoMapKey(temp);
            }
            binBEMQMES03_Service.insertPaperAnswerDetail(detailDataList);
        }
        
        //将回访信息插入【会员回访表】
        Map<String,Object> memberVisitMap = new HashMap<String,Object>();
        memberVisitMap.put("BIN_OrganizationInfoID", organizationInfoID);
        memberVisitMap.put("BIN_BrandInfoID", brandInfoID);
        memberVisitMap.put("BIN_OrganizationID", organizationID);
        memberVisitMap.put("BIN_EmployeeID", employeeID);
        memberVisitMap.put("BIN_MemberInfoID", memberInfoID);
        if(!"".equals(visitTaskID)){
            memberVisitMap.put("BIN_VisitTaskID", visitTaskID);
        }
        if(!"".equals(paperID) && null != detailDataList && detailDataList.size()>0){
            memberVisitMap.put("BIN_PaperAnswerID", paperAnswerID);
        }
        memberVisitMap.put("VisitBeginTime", ConvertUtil.getString(map.get("visitBeginDate")));
        memberVisitMap.put("VisitEndTime", ConvertUtil.getString(map.get("visitEndDate")));
        memberVisitMap.put("VisitTime", visitDate);
        memberVisitMap.put("VisitFlag", visitResult);
        memberVisitMap.put("VisitCode", ConvertUtil.getString(map.get("visitCode")));
        memberVisitMap.put("VisitTypeCode", ConvertUtil.getString(map.get("visitTypeCode")));
        memberVisitMap.put("Sourse", ConvertUtil.getString(map.get("sourse")));
        setInsertInfoMapKey(memberVisitMap);
        binBEMQMES03_Service.insertMemberVisit(memberVisitMap);
        
        //VisitTaskID字段有值，则更新【回访任务表】
        if(!"".equals(visitTaskID)){
            Map<String,Object> visitTaskMap = new HashMap<String,Object>();
            visitTaskMap.put("BIN_VisitTaskID", visitTaskID);
            visitTaskMap.put("VisitTime", visitDate);
            visitTaskMap.put("VisitResult", visitResult);
            if(paperAnswerID != 0){
                visitTaskMap.put("BIN_PaperAnswerID", paperAnswerID);
            }
            setInsertInfoMapKey(visitTaskMap);
            binBEMQMES03_Service.updateVisitTask(visitTaskMap);
        }
        
        // 修改次数(默认0)
        map.put("modifyCounts", "0");
        setInsertInfoMapKey(map);
    }
    
    /**
     * 对终端设定销售目标进行处理
     * @throws CherryMQException 
     */
    @Override
    public void analyzeSaleTarget(Map<String,Object> map) throws CherryMQException{
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        //子类型，BA：BA的销售目标，CT：柜台的销售目标。
        String subType = ConvertUtil.getString(map.get("subType"));
        //如果SubType的值为BA，这里对应的Code是BA的工号，如果SubType的值为CT，这里对应的Code是柜台编号
        String code = ConvertUtil.getString(map.get("code"));
        //销售单位类型 1：区域，2：柜台，3：营业员。
        String type = "";
        //配合Type使用
        //当Type=1时，该字段存放区域ID（这里不接收Type=1）
        //当Type=2时，该字段存放柜台在组织结构表中的ID
        //当Type=3时，该字段存放营业员对应的EmployerID
        int parameterID = 0;
        if(subType.equals("BA")){
            //查员工ID
            type = "3";//营业员
            map.put("EmployeeCode", code);
            Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, true);
            parameterID = CherryUtil.obj2int(employeeInfo.get("BIN_EmployeeID"));
            map.put("TradeEntityName", ConvertUtil.getString(employeeInfo.get("EmployeeName")));
        }else if(subType.equals("CT")){
            //查部门ID
            type = "2";//柜台
            map.put("CounterCode", code);
            Map<String,Object> organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
            parameterID = CherryUtil.obj2int(organizationInfo.get("BIN_OrganizationID"));
            map.put("TradeEntityName", ConvertUtil.getString(organizationInfo.get("CounterNameIF")));
        }else{
            MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_73);
        }
        
        //目标日期必填，格式为YYYYMM
        String targetDate = ConvertUtil.getString(map.get("targetDate"));
        if(targetDate.equals("")){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, "TargetDate"));
        }
        if(!CherryChecker.checkDate(targetDate,CherryConstants.DATEYYYYMM)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "TargetDate", "YYYYMM"));
        }
        //目标设定时间必填，格式YYYY-MM-DD HH:mm:SS
        String targetSetTime = ConvertUtil.getString(map.get("targetSetTime"));
        if(targetSetTime.equals("")){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, "TargetSetTime"));
        }
        if(!CherryChecker.checkDate(targetSetTime,CherryConstants.DATE_PATTERN_24_HOURS)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "TargetSetTime", "YYYY-MM-DD HH:mm:SS"));
        }
        
        //更新/插入销售目标设定表
        Map<String,Object> saleTargetMap = new HashMap<String,Object>();
        saleTargetMap.put("BIN_OrganizationInfoID", organizationInfoID);
        saleTargetMap.put("BIN_BrandInfoID", brandInfoID);
        saleTargetMap.put("Type", type);
        saleTargetMap.put("Parameter", parameterID);
        saleTargetMap.put("TargetType", "PRO");//目前写入的都是产品
        saleTargetMap.put("TargetDate", targetDate);
        saleTargetMap.put("TargetMoney",ConvertUtil.getString(map.get("targetMoney")));
        saleTargetMap.put("TargetQuantity", ConvertUtil.getString(map.get("targetQuantity")));
        saleTargetMap.put("SynchroFlag", "1");//1：已同步
        saleTargetMap.put("Source", CherryConstants.SALETARGET_SOURCE_TERMINAL);//终端设定
        saleTargetMap.put("TargetSetTime", ConvertUtil.getString(map.get("targetSetTime")));
        setInsertInfoMapKey(saleTargetMap);
        List<Map<String,Object>> resultMap = binBEMQMES03_Service.getSaleTarget(saleTargetMap);
        if(null!=resultMap && resultMap.size()>0){
            String sourceDB = ConvertUtil.getString(resultMap.get(0).get("Source"));
            String targetSetTimeDB = ConvertUtil.getString(resultMap.get(0).get("TargetSetTime"));
            if(DateUtil.compareDate(targetSetTime, targetSetTimeDB) > 0){
                //时序控制 消息体的时间必须大于数据库中这条记录的设定时间
                //配置项 是否允许终端设定覆盖后台设定（终端设定销售目标），1：允许，0：不允许
                //如果允许终端设定覆盖后台设定，可以更新。
                //如果不允许允许终端设定覆盖后台设定，当数据库中Source字段，为2：终端设定可以更新记录，为1：后台设定不能更新记录。
                String configValue = binOLCM14_BL.getConfigValue("1136", organizationInfoID, brandInfoID);
                boolean canCoverFlag = false;
                if(configValue.equals("1") || (configValue.equals("0") && sourceDB.equals(CherryConstants.SALETARGET_SOURCE_TERMINAL))){
                    canCoverFlag = true;
                }
                if(canCoverFlag){
                    binBEMQMES03_Service.updateSaleTarget(saleTargetMap);
                }
            }
        }else{
            binBEMQMES03_Service.insertSaleTarget(saleTargetMap);
        }

        // 修改次数(默认0)
        map.put("modifyCounts", "0");
        setInsertInfoMapKey(map);
        
        //写入Mongo
        DBObject dbObject = new BasicDBObject();
        //组织代号
        dbObject.put("OrgCode", map.get("orgCode"));
        // 品牌代码，即品牌简称
        dbObject.put("BrandCode", map.get("brandCode"));
        // 业务类型
        dbObject.put("TradeType", map.get("tradeType"));
        // 单据号
        dbObject.put("TradeNoIF", map.get("tradeNoIF"));
        // 修改次数
        dbObject.put("ModifyCounts", map.get("modifyCounts"));
        // 业务主体
        dbObject.put("TradeEntity", "1");
        // 业务主体代号
        dbObject.put("TradeEntityCode", map.get("code"));
        // 业务主体名称
        dbObject.put("TradeEntityName", map.get("TradeEntityName"));
        dbObject.put("Content", "终端设定销售目标");
        map.put("dbObject", dbObject);
    }
    
    /**
     * 处理会员积分数据
     * @throws CherryMQException 
     */
    @Override
    public void analyzeMemberPointData(Map<String, Object> map) throws Exception {
        //检测消息体
        checkMemberPointData(map);
        
        if(ConvertUtil.getString(map.get("modifyCounts")).equals("")){
            map.put("modifyCounts", "0");
        }
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        paramMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
        paramMap.put("MemCode", map.get("membercode"));
        //查询会员ID
        Map<String,Object> memberInfo = binBEMQMES97_BL.getMemberInfo(paramMap, true);
        paramMap.put("BIN_MemberInfoID", memberInfo.get("BIN_MemberInfoID"));
        map.put("memName", memberInfo.get("Name"));
        List<Map<String,Object>> memberPointlist = binBEMQMES03_Service.selMemberPointInfo(paramMap);
        paramMap.put("TotalPoint", map.get("totalPoint"));
        paramMap.put("TotalChanged", map.get("totalChanged"));
        paramMap.put("ChangablePoint", map.get("changablePoint"));
        paramMap.put("FreezePoint", map.get("freezePoint"));
        paramMap.put("TotalDisablePoint", map.get("totalDisablePoint"));
        paramMap.put("CurDisablePoint", map.get("curDisablePoint"));
        paramMap.put("PreCardPoint", map.get("preCardPoint"));
        paramMap.put("LastChangeTime", map.get("caltime"));
        paramMap.put("PreDisableDate", map.get("preDisableDate"));
        paramMap.put("CurDealDate", map.get("curDealDate"));
        paramMap.put("PreDisPointTime",map.get("preDisPointTime"));
        if(null != memberPointlist && memberPointlist.size()>0){
            //通过MainData里的Caltime判断，如果晚于会员积分表的最后的积分变化时间
            //（Caltime对应该字段，每次接收成功后，都去更新）
            //则更新当前积分表，并将明细插入到积分变化主从表
            setInsertInfoMapKey(paramMap);
            //根据时序控制是否更新
            binBEMQMES03_Service.updateMemberPointByTimeSequence(paramMap);
        }else{
            //没有积分记录增加一条
            setInsertInfoMapKey(paramMap);
            binBEMQMES03_Service.insertMemberPoint(paramMap);
        }
        //单据主记录集合
        List<Map<String,Object>> tradeDataList = (List<Map<String, Object>>) map.get("tradeDataList");
        //单据明细记录集合
        List<Map<String,Object>> detailDataList = (List<Map<String, Object>>) map.get("detailDataList");
        Map<String,Object> tradeDataParam = new HashMap<String,Object>();
        tradeDataParam.putAll(map);
        tradeDataParam.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        tradeDataParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
        //会员code与会员ID对照关系
        Map<String,Object> memberCodeIDMap = new HashMap<String,Object>();
        for(int i=0;i<tradeDataList.size();i++){
            Map<String,Object> tradeDataDTO = tradeDataList.get(i);
            String billid = ConvertUtil.getString(tradeDataDTO.get("billid"));
            String membercode = ConvertUtil.getString(tradeDataDTO.get("membercode"));
            tradeDataParam.put("MemCode", membercode);
            tradeDataParam.put("TradeNoIF", billid);
            List<Map<String,Object>> pointChangeInfo = binBEMQMES03_Service.selPointChangeInfo(tradeDataParam);
            
            //查询部门ID
            tradeDataParam.put("CounterCode", tradeDataDTO.get("countercode"));
            Map<String,Object> counterInfo = binBEMQMES97_BL.getOrganizationInfo(tradeDataParam,false);
            String organizationID = ConvertUtil.getString(counterInfo.get("BIN_OrganizationID"));
            
            //查询会员ID
            String memberInfoID = ConvertUtil.getString(memberCodeIDMap.get(membercode));
            if(memberInfoID.equals("")){
                Map<String,Object> tradeMemberInfo = binBEMQMES97_BL.getMemberInfo(tradeDataParam, true);
                memberInfoID = ConvertUtil.getString(tradeMemberInfo.get("BIN_MemberInfoID"));
                memberCodeIDMap.put(membercode, memberInfoID);
            }
            
            String bacode = "";
            String employeeID = "";
            for(int j=0;j<detailDataList.size();j++){
                Map<String,Object> detailDataDTO = detailDataList.get(j);
                //找出明细中相同BillId的第一条
                if(billid.equals(detailDataDTO.get("billid"))){
                    bacode = ConvertUtil.getString(detailDataDTO.get("bacode"));
                    tradeDataParam.put("EmployeeCode", bacode);
                    Map<String,Object> employeeInfo =  binBEMQMES97_BL.getEmployeeInfo(tradeDataParam,false);
                    if(null != employeeInfo && !employeeInfo.isEmpty()){
                        employeeID = ConvertUtil.getString(employeeInfo.get("BIN_EmployeeID"));
                    }
                    break;
                }
            }
            
            Map<String,Object> pointChange = new HashMap<String,Object>();
            pointChange.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            pointChange.put("BIN_BrandInfoID", map.get("brandInfoID"));
            pointChange.put("BIN_OrganizationID", organizationID);
            pointChange.put("TradeNoIF", billid);
            pointChange.put("TradeType", tradeDataDTO.get("bizType"));
            pointChange.put("BIN_MemberInfoID", memberInfoID);
            pointChange.put("MemCode", membercode);
            pointChange.put("ChangeDate", ConvertUtil.getString(tradeDataDTO.get("tradeDate")) +" " + ConvertUtil.getString(tradeDataDTO.get("tradeTime")));
            pointChange.put("Point", tradeDataDTO.get("points"));
            pointChange.put("Amount", tradeDataDTO.get("totalAmount"));
            pointChange.put("Quantity", tradeDataDTO.get("totalQuantity"));
            pointChange.put("BIN_EmployeeID", employeeID);
            pointChange.put("ReCalcCount", tradeDataDTO.get("reCalcCount"));
            
            int pointChangeID = 0;
            boolean insertDetailDataFlag = true;
            if(null != pointChangeInfo && pointChangeInfo.size()>0){
                pointChangeID = CherryUtil.obj2int(pointChangeInfo.get(0).get("BIN_PointChangeID"));
                int reCalcCountDB = CherryUtil.obj2int(pointChangeInfo.get(0).get("ReCalcCount"));
                int reCalcCountDBMQ =  CherryUtil.obj2int(tradeDataDTO.get("reCalcCount"));
                if(reCalcCountDBMQ > reCalcCountDB){
                    //存在 比较MQ中的ReCalcCount是否大于数据库中的，大于需要更新主表，删除明细，再增加明细
                    setInsertInfoMapKey(pointChange);
                    binBEMQMES03_Service.updatePointChange(pointChange);
                    
                    pointChange.put("BIN_PointChangeID", pointChangeID);
                    binBEMQMES03_Service.delPointChangeDetail(pointChange);
                }else{
                    //如果表中的值大于等于MQ中的值，表示数据库已是最新的记录，明细不处理
                    insertDetailDataFlag = false;
                }
            }else{
                //不存在 新增一条 会员积分变化主表
                setInsertInfoMapKey(pointChange);
                pointChangeID = binBEMQMES03_Service.insertPointChange(pointChange);
            }
            if(insertDetailDataFlag){
                List<Map<String,Object>> pointChangeDetailList = new ArrayList<Map<String,Object>>();
                for(int j=0;j<detailDataList.size();j++){
                    Map<String,Object> detailDataDTO = detailDataList.get(j);
                    if(billid.equals(detailDataDTO.get("billid"))){
                        String saleType = ConvertUtil.getString(detailDataDTO.get("saleType")).toUpperCase();
                        String prmPrtVendorID = null;
                        
                        Map<String,Object> prmPrtParam = new HashMap<String,Object>();
                        prmPrtParam.put("BIN_OrganizationID", map.get("organizationInfoID"));
                        prmPrtParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
                        prmPrtParam.put("BarCode", detailDataDTO.get("barcode"));
                        prmPrtParam.put("UnitCode", detailDataDTO.get("unitcode"));
                        prmPrtParam.put("TradeDateTime", detailDataDTO.get("tradeDate") +" "+detailDataDTO.get("tradeTime"));
                        
                        if(saleType.equals("N")){
                            prmPrtVendorID = ConvertUtil.getString(binBEMQMES97_BL.getProductVendorID(map, prmPrtParam, false));
                        }else if(saleType.equals("P")){
                            Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(map, prmPrtParam, false);
                            prmPrtVendorID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
                        }
                        if(prmPrtVendorID.equals("0") || prmPrtVendorID.equals("") ){
                            prmPrtVendorID = null;
                        }
                        
                        Map<String,Object> pointChangeDetailDTO = new HashMap<String,Object>();
                        pointChangeDetailDTO.put("BIN_PointChangeID", pointChangeID);
                        pointChangeDetailDTO.put("UnitCode",detailDataDTO.get("unitcode"));
                        pointChangeDetailDTO.put("BarCode", detailDataDTO.get("barcode"));
                        pointChangeDetailDTO.put("BIN_PrmPrtVendorID", prmPrtVendorID);
                        pointChangeDetailDTO.put("SaleType", saleType);
                        pointChangeDetailDTO.put("Point", detailDataDTO.get("point"));
                        pointChangeDetailDTO.put("ValidMonths", detailDataDTO.get("validMonths"));
                        pointChangeDetailDTO.put("Price", detailDataDTO.get("price"));
                        pointChangeDetailDTO.put("Quantity", detailDataDTO.get("quantity"));
                        pointChangeDetailDTO.put("PointType", detailDataDTO.get("pointType"));
                        pointChangeDetailDTO.put("Reason", detailDataDTO.get("reason"));
                        setInsertInfoMapKey(pointChangeDetailDTO);
                        pointChangeDetailList.add(pointChangeDetailDTO);
                    }
                }
                binBEMQMES03_Service.insertPointChangeDetail(pointChangeDetailList);
            }
        }
    }
    
    /**
     * 处理会员激活数据
     * @throws CherryMQException 
     */
    @Override
    public void analyzeMemberActive(Map<String, Object> map) throws Exception {
        //检查必填
        checkRequired(map,map,"MemberCode");
        checkRequired(map,map,"ActiveTime");
        checkRequired(map,map,"ActiveChannel");
        //检查时间格式
        if(!CherryChecker.checkDate(ConvertUtil.getString(map.get("activeTime")),CherryConstants.DATE_PATTERN_24_HOURS)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "ActiveTime", "YYYY-MM-DD HH:mm:SS"));
        }
        
        // 查询会员详细信息
        Map<String,Object> resultMemMap = binBEMQMES03_Service.selMemberInfo(map);
        if(null != resultMemMap && !resultMemMap.isEmpty()){
            map.put("memberInfoID", resultMemMap.get("memberInfoID"));
        }else{
            // 没有查询到相关会员信息
            MessageUtil.addMessageWarning(map, "会员号为\""+map.get("memberCode")+"\""+MessageConstants.MSG_ERROR_34);
        }
        Map<String,Object> updateMap = new HashMap<String,Object>();
        updateMap.put("activeTime", map.get("activeTime"));
        updateMap.put("activeChannel", map.get("activeChannel"));
        updateMap.put("memberInfoID", resultMemMap.get("memberInfoID"));
        setInsertInfoMapKey(updateMap);
        int cnt = binBEMQMES03_Service.updateMemberActive(updateMap);
        if(cnt == 0){
            MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_80);
        }
        // 修改次数(默认0)
        map.put("modifyCounts", "0");
        setInsertInfoMapKey(map);
    }
    
    /**
     * 处理短信回复信息采集
     * @throws Exception 
     */
    @Override
    public void analyzeMemberDXCJ(Map<String, Object> map) throws Exception {
        //检查必填
        checkRequired(map,map,"SubType");
        checkRequired(map,map,"MobilePhone");
        checkRequired(map,map,"MsgContent");
        checkRequired(map,map,"ReplyTime");
        //检查时间格式
        if(!CherryChecker.checkDate(ConvertUtil.getString(map.get("replyTime")),CherryConstants.DATE_PATTERN_24_HOURS)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "ReplyTime", "YYYY-MM-DD HH:mm:SS"));
        }
        
        int orgId = CherryUtil.obj2int(map.get("organizationInfoID"));
        int brandId = CherryUtil.obj2int(map.get("brandInfoID"));
        String orgCode = ConvertUtil.getString(map.get("orgCode"));
        String brandCode = ConvertUtil.getString(map.get("brandCode"));
        String subType = ConvertUtil.getString(map.get("subType"));
        String memberInfoID = "";
        String memberCode = "";
        String addressInfoID = "";
        String memAddressStandardCity = "";
        String memAddressStandardProvince = "";
        String memAddressZipCode = "";
        if(subType.equals("JBXX")){
            String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
            String encryptMobilePhone =  CherrySecret.encryptData(brandCode,mobilePhone);
            
            // 查询会员详细信息
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            paramMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
            paramMap.put("MobilePhone", encryptMobilePhone);
            List<Map<String,Object>> resultMemList = binBEMQMES03_Service.selMemberInfoByMobilePhone(paramMap);
            if(null != resultMemList){
                if(resultMemList.size() == 1){
                    memberInfoID = ConvertUtil.getString(resultMemList.get(0).get("BIN_MemberInfoID"));
                    memberCode = ConvertUtil.getString(resultMemList.get(0).get("MemCode"));
                    addressInfoID = ConvertUtil.getString(resultMemList.get(0).get("BIN_AddressInfoID"));
                    memAddressStandardCity = ConvertUtil.getString(resultMemList.get(0).get("MemAddressStandardCity"));
                    memAddressStandardProvince = ConvertUtil.getString(resultMemList.get(0).get("MemAddressStandardProvince"));
                    memAddressZipCode = ConvertUtil.getString(resultMemList.get(0).get("MemAddressZipCode"));
                    
                    map.put("memberInfoID", memberInfoID);
                    map.put("memberCode", memberCode);
                }else if(resultMemList.size() >1){
                    // 手机号查到多个会员，打印到错误日志
                    logger.error("短信回复信息采集，单据号为"+ConvertUtil.getString(map.get("tradeNoIF")+" 手机号为\""+mobilePhone+"\""+"匹配到多个会员"));
                }
            }
        }
        
        String dataValidFlag = "1";//数据有效区分，默认为1有效
        String msgContent = ConvertUtil.getString(map.get("msgContent"));
        //判断是否更新会员地址的逻辑：SubType=JBXX，MsgContent长度大于10且含有中文字符，才认为内容是有效地址。这个逻辑暂时对应雅芳的需求，将来要改。
        boolean hasChinese = false;
        if(subType.equals("JBXX")){
            String chinese = "[\u4E00-\u9FA5]";
            for (int i = 0; i < msgContent.length(); i++) {
                //获取一个字符
                String temp = msgContent.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                    hasChinese = true;
                    break;
                }
            }
            if(hasChinese && msgContent.length()>10 && memberInfoID.length()>0 && !memberInfoID.equals("0")){
                dataValidFlag = "1";
            }else{
                dataValidFlag = "0";
            }
        }
        
        Map<String,Object> insertMap = new HashMap<String,Object>();
        insertMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        insertMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
        insertMap.put("BillNo", map.get("tradeNoIF"));
        insertMap.put("TradeType", subType);//短信回复记录表的TradeType对应MQ中的subtype
        insertMap.put("EventType", map.get("eventType"));
        insertMap.put("MessageCodePre", map.get("messageCode"));
        insertMap.put("BIN_MemberInfoID", memberInfoID);
        insertMap.put("MobilePhone", map.get("mobilePhone"));
        insertMap.put("MsgContent", map.get("msgContent"));
        insertMap.put("ReplyTime", map.get("replyTime"));
        insertMap.put("BatchID", map.get("batchID"));
        insertMap.put("Comment", map.get("comment"));
        insertMap.put("DataSource", map.get("dataSource"));
        insertMap.put("DataValidFlag", dataValidFlag);
        setInsertInfoMapKey(insertMap);
        binBEMQMES03_Service.addMobileMsgReply(insertMap);
        
//        if(subType.equals("JBXX")){
//            //更新会员地址（数据有效才更新）
//            if(dataValidFlag.equals("1")){
//                if(addressInfoID.length()>0 && !addressInfoID.equals("0")){
//                    Map<String,Object> paramMap = new HashMap<String,Object>();
//                    paramMap.put("addressInfoID", addressInfoID);
//                    paramMap.put("memAddress", msgContent);
//                    paramMap.put("cityID", memAddressStandardCity);
//                    paramMap.put("provinceID", memAddressStandardProvince);
//                    paramMap.put("memPostcode", memAddressZipCode);
//                    setInsertInfoMapKey(paramMap);
//                    // 更新地址信息表
//                    binBEMQMES03_Service.updAddressInfo(paramMap);
//                }else{
//                    Map<String,Object> paramMap = new HashMap<String,Object>();
//                    paramMap.put("memAddress", msgContent);
//                    setInsertInfoMapKey(paramMap);
//                    // 添加地址信息
//                    int resultAddressInfoID = binBEMQMES03_Service.addAddressInfo(paramMap);
//                    paramMap.put("memberInfoID", memberInfoID);
//                    // 设置地点ID
//                    paramMap.put("addressInfoID", resultAddressInfoID);
//                    // 插入会员地址表
//                    binBEMQMES03_Service.addMemberAddress(paramMap);
//                }
//                // 调用活动预约方法
//                if(memberInfoID.length()>0 && !memberInfoID.equals("0")){
//                    binOLCPCOM05_IF.tran_applyCoupon(orgId, brandId, orgCode, brandCode, CherryUtil.obj2int(memberInfoID));
//                }
//            } 
//        }
        
        // 修改次数(默认0)
        map.put("modifyCounts", "0");
        setInsertInfoMapKey(map);
    }
    
    /**
     * 会员回访带问卷插入消息信息(MongoDB)
     * @param map
     * @throws CherryMQException
     */
    @Override
    public void addMongoMsgInfoMV(Map map) throws CherryMQException {
        DBObject dbObject = new BasicDBObject();
        //组织代号
        dbObject.put("OrgCode", map.get("orgCode"));
        // 品牌代码，即品牌简称
        dbObject.put("BrandCode", map.get("brandCode"));
        // 业务类型
        dbObject.put("TradeType", map.get("tradeType"));
        // 单据号
        dbObject.put("TradeNoIF", map.get("tradeNoIF"));
        // 修改次数
        dbObject.put("ModifyCounts", map.get("modifyCounts")==null
                ||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
        
        if(MessageConstants.MSG_MEMBER_MV.equals(map.get("tradeType"))){
            // 业务主体
            dbObject.put("TradeEntity", "0");
            // 业务主体代号
            dbObject.put("TradeEntityCode", map.get("MemCode"));
            // 业务主体名称
            dbObject.put("TradeEntityName", map.get("MemName"));
            //操作人员代号
            dbObject.put("UserCode", map.get("BAcode"));
            //操作人员名称
            dbObject.put("UserName", map.get("EmployeeName"));
            //岗位名称名称
            dbObject.put("UserPost", map.get("CategoryName"));
            // 柜台名称
            dbObject.put("DeptCode", map.get("CounterCode"));
            // 柜台名称
            dbObject.put("DeptName", map.get("CounterNameIF"));
            //发生时间
            dbObject.put("OccurTime", map.get("visitDate"));
            dbObject.put("Content", "会员回访");
        }
        map.put("dbObject", dbObject);
    }
    
    /**
	 * 取得默认等级信息
	 * 
	 * @param map 消息信息
	 * 			orgCode : 组织代号
	 * 			brandCode : 品牌代码
	 * 			newMemcode : 新卡
	 * 			memberCode : 旧卡
	 * 			brandInfoID : 品牌ID
	 * 			organizationInfoID : 组织ID
	 * 			tradeNoIF : 单据号
	 * 			memGranddate : 入会日期(yyyyMMdd)
	 * 			joinTime : 入会时间(HH:mm:ss)
	 * @return Map
	 * 			默认等级信息
	 * @throws Exception 
	 */
	private Map<String, Object> getDeftLevelInfo(Map<String, Object> map) throws Exception {
		// 是否设置默认等级
		boolean defLevelFlag = true;
		//组织代号
		String orgCode = (String) map.get("orgCode");
		// 品牌代码，即品牌简称
		String brandCode = (String) map.get("brandCode");
		// 建档处理
		CampRuleExec_IF campRuleExec08 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
		if (null != campRuleExec08) {
			// 会员卡号
			String memCode = (String) map.get("newMemcode");
			if (CherryChecker.isNullOrEmpty(memCode, true)) {
				memCode = (String) map.get("memberCode");
			}
			if (null != memCode && !memCode.startsWith("8") && !memCode.startsWith("9")) {
				defLevelFlag = false;
			}
		}
		if (defLevelFlag) {
			// 取得等级(根据入会途径)
			int memberLevel = binOLCM031_BL.getLevelByChannel(map);
			if (0 == memberLevel) {
				// 取得系统默认等级
				memberLevel = binOLCM031_BL.getDefaultLevel(map);
			}
			if (0 != memberLevel) {
				// 默认等级信息
				Map<String, Object> deftLevelInfo = new HashMap<String, Object>();
				// 会员当前等级
				deftLevelInfo.put("memberLevel", memberLevel);
				// 会员等级状态
				deftLevelInfo.put("levelStatus", CherryConstants.LEVELSTATUS_1);
				// 会员入会等级
				deftLevelInfo.put("grantMemberLevel", memberLevel);
				// 单据号
				String ticketNo = (String) map.get("tradeNoIF");
				// 单据日期 
				String ticketDateStr = null;
				if (!CherryChecker.isNullOrEmpty(ticketNo)) {
					int startIndex = ticketNo.length() - 17;
					int endIndex = ticketNo.length() - 3;
					if (startIndex > 0) {
						// 单据日期 
						ticketDateStr = ticketNo.substring(startIndex, endIndex);
					}
				}
				// 将入会日期作为等级变化日
				String joinDate = (String) map.get("memGranddate");
				String levelAdjustDay = null;
				if (joinDate != null && !"".equals(joinDate)) {
					if (joinDate.length() == 8 && CherryChecker.checkDate(joinDate)) {
						levelAdjustDay = joinDate.substring(0, 4) + "-" + joinDate.substring(4, 6) + "-" + joinDate.substring(6, 8);
					} else if (joinDate.indexOf("-") > 0 && joinDate.length() >= 10) {
						if (joinDate.length() > 10) {
							joinDate = joinDate.substring(0, 10);
						}
						if (CherryChecker.checkDate(joinDate)) {
							levelAdjustDay = joinDate;
						}
					}
				}
				if (null != levelAdjustDay) {
					// 入会时间
					String joinTime = (String) map.get("joinTime");
					if (null != joinTime && !"".equals(joinTime)) {
						levelAdjustDay += " " + joinTime;
					} else {
						levelAdjustDay += " 00:00:00";
					}
				} else {
					// 验证单据日期格式是否正确
					if (CherryChecker.checkDate(ticketDateStr, "yyyyMMddHHmmss")) {
						Date ticketDate = DateUtil.coverString2Date(ticketDateStr, "yyyyMMddHHmmss");
						SimpleDateFormat dateFm = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
						levelAdjustDay = dateFm.format(ticketDate);
					}
				}
				if (null != levelAdjustDay) {
					// 升级时间
					deftLevelInfo.put("levelAdjustDay", levelAdjustDay);
				}
				return deftLevelInfo;
			}
		}
		return null;
	}
	
	/**
	 * 发送会员入会沟通事件MQ消息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void sendGTMQ(Map<String, Object> map) throws Exception {
		
		// 组织ID
		Object orgId = map.get("organizationInfoID");
		// 品牌ID
		Object brandId = map.get("brandInfoID");
		// 组织代码
		String orgCode = (String)map.get("orgCode");
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode(orgCode);
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(orgId.toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandId.toString()));
		String billType = CherryConstants.MESSAGE_TYPE_ES;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(orgId.toString()), 
				Integer.parseInt(brandId.toString()), "", billType);
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
		msgDataMap.put("DataType", CherryConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("BrandCode", brandCode);
		mainData.put("TradeType", billType);
		mainData.put("TradeNoIF", billCode);
		mainData.put("EventType", map.get("eventType"));
		mainData.put("EventId", map.get("eventId"));
		mainData.put("EventDate", map.get("eventDate"));
		mainData.put("Sourse", map.get("sourse"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
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
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
    /**
     * 检查必填
     * @param map
     * @param dataMap
     * @param fieldName
     * @throws CherryMQException
     */
    private void checkRequired(Map<String,Object> map,Map<String,Object> dataMap,String fieldName) throws CherryMQException{
        String keyName = fieldName;
        if (fieldName.length() == 1) {
            keyName = fieldName.toLowerCase();
        } else if (!Character.isUpperCase(fieldName.charAt(1))) {
            keyName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        }
        
        String fieldValue = ConvertUtil.getString(dataMap.get(keyName));
        if(fieldValue.equals("")){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, fieldName));
        }
    }
    
    /**
     * 检测会员积分
     * @param map
     * @throws CherryMQException
     */
    private void checkMemberPointData(Map<String,Object> map) throws CherryMQException{
        //主数据
        checkRequired(map,map,"Membercode");
        checkRequired(map,map,"TotalPoint");
        checkRequired(map,map,"TotalChanged");
        checkRequired(map,map,"ChangablePoint");
        checkRequired(map,map,"FreezePoint");
        checkRequired(map,map,"Caltime");
        
        //单据主记录的明细行
        List<Map<String,Object>> tradeDataList = (List<Map<String, Object>>) map.get("tradeDataList");
        if(null == tradeDataList || tradeDataList.size() == 0){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "TradeDataList"));
        }
        for(int i=0;i<tradeDataList.size();i++){
            Map<String,Object> detailDTO = tradeDataList.get(i);
            checkRequired(map,detailDTO,"Billid");
            checkRequired(map,detailDTO,"BizType");
            checkRequired(map,detailDTO,"TradeDate");
            checkRequired(map,detailDTO,"TradeTime");
            checkRequired(map,detailDTO,"Points");
            checkRequired(map,detailDTO,"ReCalcCount");
            checkRequired(map,detailDTO,"ValidFlag");
        }
        
        //单据明细记录的明细行
        List<Map<String,Object>> detailDataList = (List<Map<String, Object>>) map.get("detailDataList");
        if(null == detailDataList || detailDataList.size() == 0){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "DetailDataList"));
        }
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDTO = detailDataList.get(i);
            checkRequired(map,detailDTO,"billid");
            checkRequired(map,detailDTO,"tradeDate");
            checkRequired(map,detailDTO,"tradeTime");
            checkRequired(map,detailDTO,"point");
            checkRequired(map,detailDTO,"pointType");
            checkRequired(map,detailDTO,"reason");
        }
    }
}
