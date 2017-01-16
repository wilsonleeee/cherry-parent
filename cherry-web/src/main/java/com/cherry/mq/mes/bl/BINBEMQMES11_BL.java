package com.cherry.mq.mes.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.service.BINBEMQMES08_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINBEMQMES11_BL implements CherryMessageHandler_IF{
	
	/** 会员初始数据采集信息接收处理Service **/
	@Resource
	private BINBEMQMES08_Service binBEMQMES08_Service;
	
	/** 消息数据接收共通处理Service */
	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/** 会员初始数据采集信息接收处理BL */
	@Resource
	private BINBEMQMES08_BL binBEMQMES08_BL;

	/** 会员消息数据接收处理BL **/
	@Resource
	private BINBEMQMES03_BL binBEMQMES03_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		// 修改次数(默认0)
		map.put("modifyCounts", "0");
		this.setInsertInfoMapKey(map);
		// 数据来源
		String sourse = (String)map.get("sourse");
		// 系统时间
		String sysDate = binBEMQMES08_Service.getForwardSYSDate();
		// 取得维护积分明细数据
		List detailList = (List)map.get("detailDataDTOList");
		String employeeCode = (String) ((Map)detailList.get(0)).get("employeeCode");
		// 员工代码
		map.put("BAcode", employeeCode);
		// 查询员工信息
		Map<String, Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(map);
		if(employeeInfo != null) {
			map.putAll(employeeInfo);
		}
		// 会员俱乐部ID
		int memberClubId = 0;
		if (!CherryChecker.isNullOrEmpty(((Map)detailList.get(0)).get("memberClubId"))) {
			memberClubId = Integer.parseInt(String.valueOf(((Map)detailList.get(0)).get("memberClubId")));
		}
		// 查询柜台部门信息
		Map<String, Object> counterInfo = null;
		String counterCode = (String) map.get("counterCode");
		if (!CherryChecker.isNullOrEmpty(counterCode, true)) {
			// 查询柜台部门信息
			counterInfo = binBEMQMES99_Service.selCounterDepartmentInfo(map);
			if(counterInfo != null) {
				map.putAll(counterInfo);
			}
		}
		// 推送消息
		Map<String, Object> pointRuleCalInfo = new HashMap<String, Object>();
		// 取得子业务类型
		String subTradeType = (String)map.get("subTradeType");
		String pointBillNo = (String)map.get("pointBillNo");
		// 积分类型
    	String maintainType = (String) map.get("maintainType");
    	// 积分兑换预约,积分兑换预约取消
    	String relevantNo = (String) map.get("relevantNo");
		//是否会员首单销售 为true时即为是
		String memberFirstSale = ConvertUtil.getString(map.get("memberFirstSale"));
    	if (!CherryChecker.isNullOrEmpty(relevantNo, true) && 
    			("5".equals(maintainType) || "7".equals(maintainType))) {
    		Map<String, Object> detailMap = (Map<String, Object>) detailList.get(0);
    		// 组织ID
			int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
			// 品牌ID
			int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
			// 设置组织ID
			detailMap.put("organizationInfoID", organizationInfoId);
			// 设置品牌ID
			detailMap.put("brandInfoID", brandInfoId);
			// 设置组织代码
			String orgCode = (String) map.get("orgCode");
			detailMap.put("orgCode", orgCode);
			// 设置品牌代码
			String brandCode = (String) map.get("brandCode");
			detailMap.put("brandCode", brandCode);
			// 查询会员信息
			Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
			// 会员信息不存在的场合，表示接收失败，结束处理
			if(memberInfo == null) {
				// 设定业务类型
				detailMap.put("tradeType", map.get("tradeType"));
				// 设定单据号
				detailMap.put("tradeNoIF", map.get("tradeNoIF"));
				MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_34);
			}
			detailMap.putAll(memberInfo);
			// 单据产生日期
			String businessTime = (String) detailMap.get("businessTime");
			// 单据产生日期
			detailMap.put("acquiTime", businessTime);
			// 设定重算次数为0
			detailMap.put("reCalcCount", "0");
			// 设定理由为积分
			detailMap.put("reason", DroolsConstants.REASON_0);
			// 设定计算日期
			detailMap.put("calcDate", sysDate);
			// 设定单据号
			detailMap.put("tradeNoIF", relevantNo);
			// 积分兑换预约处理
			if ("5".equals(maintainType)) {
				// 是否需要重算
				boolean isReCalcFlag = binBEMQMES08_BL.needReCalc(detailMap);
				// 不需要添加或更新重算信息的场合
				if(!isReCalcFlag) {
					// 会员信息ID
					int memberInfoId = Integer.parseInt(detailMap.get("memberInfoId").toString());
					// 会员卡号
					String memberCode = (String) detailMap.get("memberCode");
					// 员工ID
					Integer employeeId = null;
					if (null != employeeInfo) {
						employeeId = Integer.parseInt(employeeInfo.get("employeeID").toString());
					}
					// 组织结构ID
					Integer organizationId = null;
					if(counterInfo != null) {
						organizationId = Integer.parseInt(counterInfo.get("organizationID").toString());
					}
					// 关联单号
		        	detailMap.put("relevantNo", relevantNo);
		        	// 查询预约单信息
		        	List<Map<String, Object>> PBTicketList = binBEMQMES08_Service.getPBTicketList(detailMap);
		        	if (null != PBTicketList && !PBTicketList.isEmpty()) {
		        		Map<String, Object> orderTicketInfo = PBTicketList.get(0);
		        		String orderTradeType = (String) orderTicketInfo.get("orderTradeType");
		        		// 设定业务类型
		    			detailMap.put("tradeType", orderTradeType);
		        		// 预约总积分
		        		double totalPointRequired = Double.parseDouble(orderTicketInfo.get("totalPointRequired").toString());
		        		// 总数量
		        		double totalQuantity = Double.parseDouble(orderTicketInfo.get("totalQuantity").toString());
		        		// 总金额
		        		double totalAmout = Double.parseDouble(orderTicketInfo.get("totalAmout").toString());
		        		// 会员积分变化主记录
						PointChangeDTO pointChange = new PointChangeDTO();
						// 所属组织ID
				    	pointChange.setOrganizationInfoId(organizationInfoId);
				    	// 所属品牌ID
				    	pointChange.setBrandInfoId(brandInfoId);
				    	// 单据号
				    	pointChange.setTradeNoIF(relevantNo);
				    	// 业务类型
				    	pointChange.setTradeType(orderTradeType);
				    	// 会员信息ID
				    	pointChange.setMemberInfoId(memberInfoId);
				    	// 会员卡号
				    	pointChange.setMemCode(memberCode);
				    	// 积分变化日期
				    	pointChange.setChangeDate(businessTime);
				    	// 积分值 
				    	pointChange.setPoint(-totalPointRequired);
				    	// 组织结构ID
						pointChange.setOrganizationId(organizationId);
				    	// 员工ID
				    	pointChange.setEmployeeId(employeeId);
				    	// 整单金额
						pointChange.setAmount(totalAmout);
						// 整单数量
						pointChange.setQuantity(totalQuantity);
						// 机器号
						Object machineCodeObj = orderTicketInfo.get("machineCode");
						if (null != machineCodeObj) {
							pointChange.setMachineCode(String.valueOf(machineCodeObj));
						}
						double totalPointPB = 0;
						List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
						for (Map<String, Object> detailInfo : PBTicketList) {
							PointChangeDetailDTO pointChangeDetail = new PointChangeDetailDTO();
							
							// 厂商编码
							pointChangeDetail.setUnitCode((String) detailInfo.get("unitCode"));
							// 产品条码
							pointChangeDetail.setBarCode((String) detailInfo.get("barCode"));
							// 促销品/产品厂商ID
							pointChangeDetail.setPrmPrtVendorId(Integer.parseInt(String.valueOf(detailInfo.get("productVendorId"))));
							// 销售类型
							String saleType = (String) detailInfo.get("giftType");
							pointChangeDetail.setSaleType(saleType);
							// 定价
							double price = Double.parseDouble(detailInfo.get("amout").toString());
							// 数量
							double quantity = Double.parseDouble(detailInfo.get("quantity").toString());
							pointChangeDetail.setQuantity(quantity);
							double pointRequired = Double.parseDouble(detailInfo.get("pointRequired").toString());
							if (0 != pointRequired) {
								pointRequired = -DoubleUtil.mul(pointRequired, quantity);
								// 积分值
								pointChangeDetail.setPoint(pointRequired);
								totalPointPB = DoubleUtil.add(totalPointPB, pointRequired);
							}
							pointChangeDetail.setPrice(price);
							// 积分类型
							pointChangeDetail.setPointType(maintainType);
					    	// 理由
							pointChangeDetail.setReason(MessageConstants.MSG_INFO_01);
							changeDetailList.add(pointChangeDetail);
						}
						// 总积分有误
						if (totalPointPB != pointChange.getPoint()) {
							// 设定业务类型
							detailMap.put("tradeType", map.get("tradeType"));
							// 设定单据号
							detailMap.put("tradeNoIF", map.get("tradeNoIF"));
							MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_67);
						}
						pointChange.setChangeDetailList(changeDetailList);
		        		// 取得会员当前积分信息
						Map<String, Object> memPointInfo = binOLCM31_BL.getMemberPointInfo(detailMap);
						// 更新前积分
						double totalPoint = 0;
						// 更新后当前积分
						double curPoint = 0;
						// 更新前可兑换积分
						double ChangablePoint = 0;
						// 更新后可兑换积分
						double curChangablePoint = 0;
						// 更新前累计兑换积分
						double totalChanged = 0;
						// 更新后累计兑换积分
						double curTotalChanged = 0;
						// 前卡积分值
						double preCardPoint = 0;
						// 累计失效积分
						double totalDisablePoint = 0;
						// 本次将失效积分
						double curDisablePoint = 0;
						// 上回积分失效日期
						String preDisableDate = null;
						// 本次积分失效日期
						String curDealDate = null;
						if (null != memPointInfo && !memPointInfo.isEmpty()) {
							totalPoint = Double.parseDouble(memPointInfo.get("curTotalPoint").toString());
							ChangablePoint = Double.parseDouble(memPointInfo.get("curChangablePoint").toString());
							totalChanged = Double.parseDouble(memPointInfo.get("curTotalChanged").toString());
							preCardPoint = Double.parseDouble(memPointInfo.get("mbPreCardPoint").toString());
							totalDisablePoint = Double.parseDouble(memPointInfo.get("totalDisablePoint").toString());
							curDisablePoint = Double.parseDouble(memPointInfo.get("curDisablePoint").toString());
							preDisableDate = (String) memPointInfo.get("preDisableDate");
							curDealDate = (String) memPointInfo.get("curDealDate");
						}
						curPoint = DoubleUtil.sub(totalPoint, totalPointRequired);
						curChangablePoint = DoubleUtil.sub(ChangablePoint, totalPointRequired);
						curTotalChanged = DoubleUtil.add(totalChanged, totalPointRequired);
						// 验证是否是当前卡
						boolean isCurCard = binOLCM31_BL.isCurCard(memberInfoId, memberCode);
						if (!isCurCard) {
							preCardPoint = DoubleUtil.sub(preCardPoint, totalPointRequired);
						}
						// 会员DTO
						CampBaseDTO campBaseDTO = new CampBaseDTO();
						// 会员信息ID
						campBaseDTO.setMemberInfoId(memberInfoId);
						// 会员姓名
						campBaseDTO.setMemName((String) detailMap.get("memberName"));
						// 组织代码
						campBaseDTO.setOrgCode(orgCode);
						// 品牌代码
						campBaseDTO.setBrandCode(brandCode);
						// 组织信息ID
						campBaseDTO.setOrganizationInfoId(organizationInfoId);
						// 品牌ID
						campBaseDTO.setBrandInfoId(brandInfoId);
						// 计算时间
						campBaseDTO.setCalcDate(sysDate);
						if (!CherryChecker.isNullOrEmpty(counterCode, true)) {
							// 柜台号
							campBaseDTO.setCounterCode(counterCode);
							// 员工号
							campBaseDTO.setEmployeeCode(employeeCode);
						}
						// 积分信息DTO
						PointDTO pointInfo = new PointDTO();
				    	// 当前总积分
				    	pointInfo.setCurTotalPoint(curPoint);
				    	// 累计兑换积分
				    	pointInfo.setCurTotalChanged(curTotalChanged);
				    	// 可兑换积分
				    	pointInfo.setCurChangablePoint(curChangablePoint);
				    	// 累计失效积分
				    	pointInfo.setTotalDisablePoint(totalDisablePoint);
				    	// 本次将失效积分
				    	pointInfo.setCurDisablePoint(curDisablePoint);
				    	// 上回积分失效日期
				    	pointInfo.setPreDisableDate(preDisableDate);
				    	// 本次积分失效日期
				    	pointInfo.setCurDealDate(curDealDate);
				    	// 前卡积分值
				    	pointInfo.setPreCardPoint(preCardPoint);
				    	pointInfo.setPointChange(pointChange);
				    	campBaseDTO.setPointInfo(pointInfo);
				    	if (0 != memberClubId) {
							campBaseDTO.setMemberClubId(memberClubId);
							pointInfo.setMemberClubId(memberClubId);
							pointInfo.setClubIdStr(String.valueOf(memberClubId));
							pointChange.setMemberClubId(memberClubId);
							pointChange.setClubIdStr(pointInfo.getClubIdStr());
							campBaseDTO.setClubCode(binOLCM31_BL.getClubCode(detailMap));
						}
				    	// 处理会员积分变化信息
				    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
				    	// 当前积分
				    	detailMap.put("curTotalPoint", curPoint);
				    	// 可兑换积分
				    	detailMap.put("curChangablePoint", curChangablePoint);
				    	// 累计兑换积分
				    	detailMap.put("curTotalChanged", curTotalChanged);
				    	if (!isCurCard) {
					    	// 前卡积分值
					    	detailMap.put("preCardPoint", preCardPoint);
				    	}
				    	// 更新会员积分信息表
				    	binOLCM31_BL.updateMemberPointInfo(detailMap);
				    	if (totalPoint != curPoint) {
				    		// 插入规则执行履历表:累计积分
					    	// 履历区分 ：累计积分
					    	detailMap.put("recordKbn", DroolsConstants.RECORDKBN_5);
					    	// 更新前的值
							detailMap.put("oldValue", String.valueOf(totalPoint));
							// 更新后的值
							detailMap.put("newValue", String.valueOf(curPoint));
							// 插入规则执行履历表
							binBEMQMES08_Service.addRuleExecRecord(detailMap);
				    	}
				    	if (ChangablePoint != curChangablePoint) {
				    		// 插入规则执行履历表:可兑换积分
							// 履历区分 ：可兑换积分
					    	detailMap.put("recordKbn", DroolsConstants.RECORDKBN_7);
					    	// 更新前的值
							detailMap.put("oldValue", String.valueOf(ChangablePoint));
							// 更新后的值
							detailMap.put("newValue", String.valueOf(curChangablePoint));
							// 插入规则执行履历表
							binBEMQMES08_Service.addRuleExecRecord(detailMap);
				    	}
				    	if (totalChanged != curTotalChanged) {
				    		// 插入规则执行履历表:累计兑换积分
							// 履历区分 ：累计兑换积分
					    	detailMap.put("recordKbn", DroolsConstants.RECORDKBN_6);
					    	// 更新前的值
							detailMap.put("oldValue", String.valueOf(totalChanged));
							// 更新后的值
							detailMap.put("newValue", String.valueOf(curTotalChanged));
							// 插入规则执行履历表
							binBEMQMES08_Service.addRuleExecRecord(detailMap);
				    	}
				    	// 是否需要同步天猫会员
						if (binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), brandCode)) {
							List<Map<String, Object>> tmSyncInfoList = new ArrayList<Map<String, Object>>();
							Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
							tmSyncInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
							tmSyncInfo.put("brandCode", brandCode);
							tmSyncInfo.put("PgmName", "BINBEMQMES11");
							tmSyncInfoList.add(tmSyncInfo);
							map.put("TmSyncInfoList", tmSyncInfoList);
						}
				    	// 取得积分MQ消息体
						MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
						if(mqInfoDTO != null) {
							// 发送MQ消息处理
							binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
							// 组织代码
							pointRuleCalInfo.put("OrgCode", campBaseDTO.getOrgCode());
							// 品牌代码
							pointRuleCalInfo.put("BrandCode", campBaseDTO.getBrandCode());
							// 业务类型
							pointRuleCalInfo.put("TradeType", "PT");
							// 会员积分变化主ID
							pointRuleCalInfo.put("pointChangeId", pointChange.getPointChangeId());
							// 计算日期
							pointRuleCalInfo.put("changeDate", pointChange.getChangeDate());
							// 引起积分计算的单据号
							pointRuleCalInfo.put("billCode", pointChange.getTradeNoIF());
							// 引起积分计算的业务类型
							pointRuleCalInfo.put("billType", pointChange.getTradeType());
							// 购买金额
							pointRuleCalInfo.put("amount", pointChange.getAmount());
							// 购买数量
							pointRuleCalInfo.put("quantity", pointChange.getQuantity());
							// 获得积分
							pointRuleCalInfo.put("point", pointChange.getPoint());
							// 会员ID
							pointRuleCalInfo.put("memberInfoId", pointChange.getMemberInfoId());
							// 会员名称
							pointRuleCalInfo.put("name", campBaseDTO.getMemName());
							if(counterInfo != null) {
								// 部门ID
								pointRuleCalInfo.put("organizationId", organizationId);
								// 部门名称
								pointRuleCalInfo.put("departName", counterInfo.get("counterName"));
							}
							// 所属柜台ID
							pointRuleCalInfo.put("MemOrganizationID", detailMap.get("memOrganizationId"));
							// 组织ID
							String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
							// 品牌ID
							String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
							// 积分变化需要发送沟通MQ
							if(binOLCM14_BL.isConfigOpen("1088", orgIdStr, brandIdStr)) {
								// 发送积分变化沟通MQ的最低阀值
								String minPointStr = binOLCM14_BL.getConfigValue("1089", orgIdStr, brandIdStr);
								boolean gtFlag = true;
								if (!CherryChecker.isNullOrEmpty(minPointStr, true)) {
									minPointStr = minPointStr.trim();
									try {
										double minPoint = Double.parseDouble(minPointStr);
										// 当前积分小于最低阈值不发送沟通MQ
										if (pointInfo.getCurTotalPoint() < minPoint) {
											gtFlag = false;
										}
									} catch (Exception e) {
										gtFlag = false;
									}
								}
								if (gtFlag) {
									Map<String, Object> gtMap = new HashMap<String, Object>();
									// 组织ID
									gtMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
									// 品牌ID
									gtMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
									// 组织代码
									gtMap.put("orgCode", campBaseDTO.getOrgCode());
									// 品牌代码
									gtMap.put("brandCode", campBaseDTO.getBrandCode());
									// 事件ID
									gtMap.put("eventId", campBaseDTO.getMemberInfoId());
									// 事件类型:积分变化
									gtMap.put("eventType", "7");
									// 事件发生时间 
									gtMap.put("eventDate", pointChange.getChangeDate());
									// 信息内容:关联单号
									gtMap.put("messageContents", pointChange.getTradeNoIF());
									// 事件来源
									gtMap.put("sourse", "BINBEMQMES11");
									// 取得沟通短信消息体(实时)
									mqInfoDTO = binOLCM31_BL.getGTMQMessage(gtMap);
									// 发送MQ消息处理
									binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
								}
							}
						}
		        	}
				}
				// 积分预约取消进行重算处理
			} else {
				// 设定重算类型为等级和化妆次数重算
				detailMap.put("reCalcType", "0");
				// 插入重算信息表
				binBEMQMES08_Service.addReCalcInfo(detailMap);
				// 发送MQ重算消息进行实时重算
				binBEMQMES08_BL.sendReCalcMsg(detailMap);
			}
    	} else {
    		boolean flag = true;
    		if ("1".equals(maintainType) && !CherryChecker.isNullOrEmpty(relevantNo, true)) {
    			// 更新区分
    			String ptUpFlag = (String) map.get("ptUpFlag");
    			if (!CherryChecker.isNullOrEmpty(ptUpFlag, true)) {
    				flag = false;
    				Map<String, Object> detailMap = (Map<String, Object>) detailList.get(0);
    				this.setInsertInfoMapKey(detailMap);
    	    		// 组织ID
    				int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
    				// 品牌ID
    				int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
    				// 设置组织ID
    				detailMap.put("organizationInfoID", organizationInfoId);
    				// 设置品牌ID
    				detailMap.put("brandInfoID", brandInfoId);
    				// 设置组织代码
    				String orgCode = (String) map.get("orgCode");
    				detailMap.put("orgCode", orgCode);
    				// 设置品牌代码
    				String brandCode = (String) map.get("brandCode");
    				detailMap.put("brandCode", brandCode);
    				// 设定业务类型
					detailMap.put("tradeType", map.get("tradeType"));
					// 设定单据号
					detailMap.put("tradeNoIF", map.get("tradeNoIF"));
					if (!"0".equals(ptUpFlag)) {
	    				// 查询会员信息
	    				Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
	    				// 会员信息不存在的场合，表示接收失败，结束处理
	    				if(memberInfo == null || memberInfo.isEmpty()) {
	    					MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_34);
	    				}
	    				detailMap.putAll(memberInfo);
					}
    				// 单据产生日期
    				String businessTime = (String) detailMap.get("businessTime");
    				// 单据产生日期
    				detailMap.put("acquiTime", businessTime);
    				// 设定重算次数为0
    				detailMap.put("reCalcCount", "0");
    				// 备注信息
					String reasonText = (String) detailMap.get("reason");
					// 原因
					detailMap.put("reasonText", reasonText);
    				// 设定理由为积分
    				detailMap.put("reason", DroolsConstants.REASON_5);
    				// 设定计算日期
    				detailMap.put("calcDate", sysDate);
    				// 关联单据号
    				detailMap.put("relevantNo", relevantNo);
    				// 查询状态维护信息
    				Map<String, Object> usedInfo = binBEMQMES08_Service.getPTUsedInfo(detailMap);
    				boolean delFlag = false;
    				if (null == usedInfo || usedInfo.isEmpty()) {
    					if ("0".equals(ptUpFlag) && binBEMQMES08_Service.getPTCXCount(detailMap) > 0) {
    						return;
    					}
    					if (!"0".equals(ptUpFlag)) {
    						detailMap.put("VDFG0", "0");
    						usedInfo = binBEMQMES08_Service.getPTUsedInfo(detailMap);
    						if (null == usedInfo || usedInfo.isEmpty()) { 
    							MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_68);
    						} else {
    							delFlag = true;
    						}
    					}
    				}
    				detailMap.put("memUsedInfoId", usedInfo.get("memUsedInfoId"));
    				detailMap.put("memUsedDetailId", usedInfo.get("memUsedDetailId"));
    				// 原会员ID
    				int oldMemberId = 0;
    				if (null != usedInfo.get("oldMemberId")) {
    					oldMemberId = Integer.parseInt(usedInfo.get("oldMemberId").toString());
    				}
    				// 现会员ID
    				int memberId = 0;
    				if (null != detailMap.get("memberInfoId")) {
    					memberId = Integer.parseInt(detailMap.get("memberInfoId").toString());
    				}
    				// 删除原有记录
    				if ("0".equals(ptUpFlag)) {
    					binBEMQMES08_Service.delMemUsedInfo(detailMap);
    					if (oldMemberId != memberId) {
	    					//原会员ID
	    					detailMap.put("memberInfoId", usedInfo.get("oldMemberId"));
	    					//原会员卡号
	    					detailMap.put("memberCode", usedInfo.get("oldMemCode"));
    					}
    					// 更新原有记录
    				} else {
    					// 修改积分差值
						detailMap.put("usedTimes", detailMap.get("modifyPoint"));
						if (delFlag) {
							// 恢复状态维护信息
							binBEMQMES08_Service.validMemUsedInfo(detailMap);
						}
    					// 更新状态维护明细信息
						binBEMQMES08_Service.updateMemUsedDetailInfo(detailMap);
						if (oldMemberId != memberId) {
							// 设定重算类型为等级和化妆次数重算
	        				detailMap.put("reCalcType", "0");
	        				// 现会员卡号
							String memberCode = (String) detailMap.get("memberCode");
	        				if (!delFlag) {
								//原会员ID
		    					detailMap.put("memberInfoId", usedInfo.get("oldMemberId"));
		    					//原会员卡号
		    					detailMap.put("memberCode", usedInfo.get("oldMemCode"));
		        				// 插入重算信息表
		        				binBEMQMES08_Service.addReCalcInfo(detailMap);
		        				// 发送MQ重算消息进行实时重算
		        				binBEMQMES08_BL.sendReCalcMsg(detailMap);
	        				}
	        				//现会员ID
	    					detailMap.put("memberInfoId", memberId);
	    					//现会员卡号
	    					detailMap.put("memberCode", memberCode);
						}
    				}
    				// 设定重算类型为等级和化妆次数重算
    				detailMap.put("reCalcType", "0");
    				// 插入重算信息表
    				binBEMQMES08_Service.addReCalcInfo(detailMap);
    				// 发送MQ重算消息进行实时重算
    				binBEMQMES08_BL.sendReCalcMsg(detailMap);
    			}

    		}
    		if (flag) {
    			// 官网导入
    			if (null != sourse && "WEB".equals(sourse.trim())) {
    				Map<String, Object> detailMap = (Map<String, Object>)detailList.get(0);
    				Map<String, Object> checkMap = new HashMap<String, Object>();
    				// 组织ID
    				checkMap.put("organizationInfoID", map.get("organizationInfoID"));
    				// 品牌ID
    				checkMap.put("brandInfoID", map.get("brandInfoID"));
    				// 会员卡号
    				checkMap.put("memberCode", detailMap.get("memberCode"));
    				// 查询会员信息
					Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(checkMap);
					if(null != memberInfo && !memberInfo.isEmpty()) {
						// 会员信息ID
						checkMap.put("memberInfoId", memberInfo.get("memberInfoId"));
						// 积分变化时间
						checkMap.put("businessTime", detailMap.get("businessTime"));
						// 类型
						checkMap.put("tradeType", "PT");
						// 取得会员某时间的维护记录数
						int count = binBEMQMES08_Service.getUsedCount(checkMap);
						// 已在同一时间的维护记录，直接返回，不继续处理
						if (count > 0) {
							return;
						}
					}
    			}
    			boolean isTmpt = (null != map.get("tmRecordId"));
		    	// 插入会员状态修改主表
		    	int memUsedInfoId = binBEMQMES08_Service.addMemUsedInfo(map);
				// 循环处理维护积分明细数据
				for(int i = 0; i < detailList.size(); i++) {
					
					Map<String, Object> detailMap = (Map)detailList.get(i);
					// 组织ID
					int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
					// 品牌ID
					int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
					// 单据产生日期
					String businessTime = (String) detailMap.get("businessTime");
					// 主表ID
					detailMap.put("memUsedInfoId", memUsedInfoId);
					// 设置组织ID
					detailMap.put("organizationInfoID", organizationInfoId);
					// 设置品牌ID
					detailMap.put("brandInfoID", brandInfoId);
					// 设置组织代码
					String orgCode = (String) map.get("orgCode");
					detailMap.put("orgCode", orgCode);
					// 设置品牌代码
					String brandCode = (String) map.get("brandCode");
					detailMap.put("brandCode", brandCode);
					// 设定数据来源
					detailMap.put("channel", sourse);
					// 设定业务类型
					detailMap.put("tradeType", map.get("tradeType"));
					// 设定单据号
					detailMap.put("tradeNoIF", map.get("tradeNoIF"));
					// 备注信息
					String reasonText = (String) detailMap.get("reason");
					// 原因
					detailMap.put("reasonText", reasonText);
					// 设定重算次数为0
					detailMap.put("reCalcCount", "0");
					// 设定理由为积分
					detailMap.put("reason", DroolsConstants.REASON_5);
					// 设定计算日期
					detailMap.put("calcDate", sysDate);
					// 单据产生日期
					detailMap.put("acquiTime", businessTime);
					this.setInsertInfoMapKey(detailMap);
					// 取得维护积分数
					String modifyPoint = (String)detailMap.get("modifyPoint");
					// 查询会员信息
					Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
					// 会员信息不存在的场合，表示接收失败，结束处理
					if(memberInfo == null || memberInfo.isEmpty()) {
						boolean errFlag = true;
    					if (isTmpt) {
    						Map<String, Object> cardInfo = binBEMQMES08_Service.getTmallMember(detailMap);
    						if (null != cardInfo && !cardInfo.isEmpty()) {
    							String memCode = (String) cardInfo.get("memCode");
    							if (!CherryChecker.isNullOrEmpty(memCode, true)) {
    								detailMap.put("memberCode", memCode);
    								memberInfo = binBEMQMES08_Service.getMemberInfoID(detailMap);
    								if (memberInfo != null && !memberInfo.isEmpty()) {
    									errFlag = false;
    								}
    							}
    						}
    					}
    					if (errFlag) {
    						MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_34);
    					}
					}
					detailMap.putAll(memberInfo);
					// 会员信息ID
					int memberInfoId = Integer.parseInt(detailMap.get("memberInfoId").toString());
					// 设定履历区分为积分
		//			detailMap.put("recordKbn", DroolsConstants.RECORDKBN_5);
					// 取得变更前的积分
		//			Object oldPoint = binBEMQMES08_BL.getNewRuleExecRecord(detailMap);
		//			if(oldPoint == null) {
		//				oldPoint = "0";
		//			}
					// 取得会员当前积分信息
					Map<String, Object> memPointInfo = binOLCM31_BL.getMemberPointInfo(detailMap);
					// 修改前积分
					double totalPoint = 0;
					// 前卡积分值
					double preCardPoint = 0;
					// 累计失效积分
					double totalDisablePoint = 0;
					// 本次将失效积分
					double curDisablePoint = 0;
					// 上回积分失效日期
					String preDisableDate = null;
					// 本次积分失效日期
					String curDealDate = null;
					// 积分导入时间
					String initTime = null;
					if (null != memPointInfo && !memPointInfo.isEmpty()) {
						totalPoint = Double.parseDouble(memPointInfo.get("curTotalPoint").toString());
						preCardPoint = Double.parseDouble(memPointInfo.get("mbPreCardPoint").toString());
						totalDisablePoint = Double.parseDouble(memPointInfo.get("totalDisablePoint").toString());
						curDisablePoint = Double.parseDouble(memPointInfo.get("curDisablePoint").toString());
						preDisableDate = (String) memPointInfo.get("preDisableDate");
						curDealDate = (String) memPointInfo.get("curDealDate");
						initTime = (String) memPointInfo.get("initialMPTime");
					}
					// 修改积分总值的场合
					if("1".equals(subTradeType)) {
						// 修改积分总值
						detailMap.put("curPoints", modifyPoint);
						detailMap.put("oldCurPoints", String.valueOf(totalPoint));
					} else { // 修改积分差值的场合
						// 修改积分差值
						detailMap.put("usedTimes", modifyPoint);
					}
					
					// 插入会员状态修改明细表
					binBEMQMES08_Service.addMemUsedDetail(detailMap);
					boolean isExct = true;
					if (!CherryChecker.isNullOrEmpty(initTime, true)) {
						Calendar cal1 = Calendar.getInstance();
						if (CherryChecker.checkDate(businessTime)) {
							businessTime = businessTime + " 00:00:00";
						} else {
							int index = businessTime.indexOf(".");
							if (index > 0) {
								businessTime = businessTime.substring(0, index);
							}
						}
						cal1.setTime(DateUtil.coverString2Date(businessTime, DateUtil.DATETIME_PATTERN));
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(DateUtil.coverString2Date(initTime, DateUtil.DATETIME_PATTERN));
						// 比较单据时间是否在初始导入时间之前
						if (cal1.before(cal2)) {
							isExct = false;
							// 更新会员积分信息(历史积分调整)
							updateHistoryPoint(map, detailMap, memPointInfo);
						}
					}
					if (isExct) {
						String acquiTime = (String)detailMap.get("acquiTime");
						String initialDate = (String)detailMap.get("initialDate");
						// 业务日期比会员初始采集日期小的场合，不更新会员相关信息
						if(initialDate != null && acquiTime.substring(0,10).compareTo(initialDate) < 0) {
							continue;
						}
						//积分导入主表导入结果区分修改
						if(pointBillNo != null){
							// 更新正常
							detailMap.put("resultFlag", "1");
							detailMap.put("importResults",CherryConstants.IMPORTRESULT_1);
							binOLCM31_BL.updateMemPointImportDetail(detailMap);
						}
						if (isTmpt) {
							Map<String, Object> tmptInfo = new HashMap<String, Object>();
							tmptInfo.put("tmallRecallFlag", 9);
							tmptInfo.put("memUsedInfoId", detailMap.get("memUsedInfoId"));
							tmptInfo.put("updatedBy", "BINBEMQMES08");
							tmptInfo.put("updatePGM", "BINBEMQMES08");
							detailMap.put("TmptInfo", tmptInfo);
						}
						// 是否需要重算
						boolean isReCalcFlag = binBEMQMES08_BL.needReCalc(detailMap);
						
						// 不需要添加或更新重算信息的场合
						if(!isReCalcFlag) {
							// 可兑换积分
							double ChangablePoint = 0;
							// 当前累计兑换积分
							double curTotalChanged = 0;
							if (null != memPointInfo && !memPointInfo.isEmpty()) {
								ChangablePoint = Double.parseDouble(memPointInfo.get("curChangablePoint").toString());
								curTotalChanged = Double.parseDouble(memPointInfo.get("curTotalChanged").toString());
							}
							// 修改后当前积分
							double curPoint = 0;
							// 修改的积分值
							double modPoint = 0;
							// 积分差值
							double subPoint = 0;
							if (!CherryChecker.isNullOrEmpty(modifyPoint, true)) {
								modPoint = Double.parseDouble(modifyPoint);
							}
							// 积分类型
							String pointType = null;
							// 修改积分总值的场合
							if("1".equals(subTradeType)) {
								curPoint = modPoint;
								subPoint = DoubleUtil.sub(curPoint, totalPoint);
								pointType = DroolsConstants.POINTTYPE0;
							} else { // 修改积分差值的场合
								subPoint = modPoint;
								curPoint = DoubleUtil.add(totalPoint, subPoint);
								pointType = DroolsConstants.POINTTYPE99;
							}
							String mixMobile = null;
							int tmallPointId = 0;
							Long recordId = null;
							if (isTmpt) {
								recordId = Long.parseLong(map.get("tmRecordId").toString());
								Map<String, Object> tmPointInfo = binBEMQMES08_Service.getTMPointInfo(map);
								if (null == tmPointInfo || tmPointInfo.isEmpty()) {
									MessageUtil.addMessageWarning(detailMap, MessageConstants.MSG_ERROR_83);
								}
								mixMobile = (String) tmPointInfo.get("tmallMixMobile");
								tmallPointId = Integer.parseInt(tmPointInfo.get("tmallPointId").toString());
								// 积分不足
								if (curPoint < 0 && modPoint < 0) {
									String tmErrCode = "deduct-fail:point-no-enough";
									detailMap.put("tmallPointId", tmallPointId);
									detailMap.put("ptFlag", 2);
									detailMap.put("ptResult", 1);
									detailMap.put("tmErrCode", tmErrCode);
									// 更新会员天猫积分信息
									binBEMQMES08_Service.updateTMPointInfo(detailMap);
									detailMap.put("usedVdFlag", 0);
									detailMap.put("tmallRecallFlag", 0);
									// 更新积分变化主表
									binBEMQMES08_Service.updateTMUsedInfo(detailMap);
									Map<String, Object> tmRecallInfo = new HashMap<String, Object>();
									tmRecallInfo.put("mixMobile", mixMobile);
									tmRecallInfo.put("recordId", recordId);
									tmRecallInfo.put("tmErrCode", tmErrCode);
									tmRecallInfo.put("memUsedInfoId", memUsedInfoId);
									tmRecallInfo.put("brandCode", brandCode);
									map.put("TmRecallInfo", tmRecallInfo);
//									callbackTmall(mixMobile, recordId, "deduct-fail:point-no-enough", brandCode);
									//信息插入到MogoDB
									DBObject dbObject = new BasicDBObject();
									// 组织代号
									dbObject.put("OrgCode", map.get("orgCode"));
									// 品牌代码
									dbObject.put("BrandCode", map.get("brandCode"));
									// 业务类型
									dbObject.put("TradeType", map.get("tradeType"));
									// 单据号
									dbObject.put("TradeNoIF", map.get("tradeNoIF"));
									// 修改回数
									dbObject.put("ModifyCounts", map.get("modifyCounts"));
									// 业务主体
									dbObject.put("TradeEntity", "0");
									Map<String, Object> tempMap  = (Map<String, Object>)detailList.get(0);
									// 业务主体代号
									dbObject.put("TradeEntityCode", tempMap.get("memberCode"));
									//发生时间
									dbObject.put("OccurTime", tempMap.get("businessTime"));
									// 操作员工
									dbObject.put("UserCode", tempMap.get("employeeCode"));
									// 消息体
									dbObject.put("Content", map.get("messageBody"));
									map.put("dbObject", dbObject);
									return;
								}
							}
							// 当前可兑换积分
							double curChangePoint = DoubleUtil.add(ChangablePoint, subPoint);
							
							// 会员卡号
							String memberCode = (String) detailMap.get("memberCode");
							// 员工ID
							int employeeId = 0;
							if (null != map.get("employeeID")) {
								employeeId = Integer.parseInt(map.get("employeeID").toString());
							}
							// 验证是否是当前卡
							boolean isCurCard = binOLCM31_BL.isCurCard(memberInfoId, memberCode);
							if (!isCurCard) {
								preCardPoint = DoubleUtil.add(preCardPoint, subPoint);
							}
							// 会员DTO
							CampBaseDTO campBaseDTO = new CampBaseDTO();
							// 会员信息ID
							campBaseDTO.setMemberInfoId(memberInfoId);
							// 会员姓名
							campBaseDTO.setMemName((String) detailMap.get("memberName"));
							// 组织代码
							campBaseDTO.setOrgCode(orgCode);
							// 品牌代码
							campBaseDTO.setBrandCode(brandCode);
							// 组织信息ID
							campBaseDTO.setOrganizationInfoId(organizationInfoId);
							// 品牌ID
							campBaseDTO.setBrandInfoId(brandInfoId);
							// 计算时间
							campBaseDTO.setCalcDate(sysDate);
							if (null != detailMap.get("zRegFlg")) {
								campBaseDTO.setMemRegFlg(Integer.parseInt(String.valueOf(detailMap.get("zRegFlg"))));
							}
							// 积分信息DTO
							PointDTO pointInfo = new PointDTO();
							// 会员积分变化主记录
							PointChangeDTO pointChange = new PointChangeDTO();
							if (0 != memberClubId) {
								campBaseDTO.setMemberClubId(memberClubId);
								pointInfo.setMemberClubId(memberClubId);
								pointInfo.setClubIdStr(String.valueOf(memberClubId));
								pointChange.setMemberClubId(memberClubId);
								pointChange.setClubIdStr(pointInfo.getClubIdStr());
								campBaseDTO.setClubCode(binOLCM31_BL.getClubCode(detailMap));
							}
							// 会员积分变化明细记录
					    	List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
					    	// 当前总积分
					    	pointInfo.setCurTotalPoint(curPoint);
					    	// 累计兑换积分
					    	pointInfo.setCurTotalChanged(curTotalChanged);
					    	// 可兑换积分
					    	pointInfo.setCurChangablePoint(curChangePoint);
					    	// 累计失效积分
					    	pointInfo.setTotalDisablePoint(totalDisablePoint);
					    	// 本次将失效积分
					    	pointInfo.setCurDisablePoint(curDisablePoint);
					    	// 上回积分失效日期
					    	pointInfo.setPreDisableDate(preDisableDate);
					    	// 本次积分失效日期
					    	pointInfo.setCurDealDate(curDealDate);
					    	// 前卡积分值
					    	pointInfo.setPreCardPoint(preCardPoint);
					    	// 所属组织ID
					    	pointChange.setOrganizationInfoId(organizationInfoId);
					    	// 所属品牌ID
					    	pointChange.setBrandInfoId(brandInfoId);
					    	// 单据号
					    	pointChange.setTradeNoIF((String) map.get("tradeNoIF"));
					    	// 业务类型
					    	pointChange.setTradeType((String) map.get("tradeType"));
					    	// 会员信息ID
					    	pointChange.setMemberInfoId(memberInfoId);
					    	// 会员卡号
					    	pointChange.setMemCode(memberCode);
					    	// 积分变化日期
					    	pointChange.setChangeDate(businessTime);
					    	// 积分值 
					    	pointChange.setPoint(subPoint);
					    	// 员工ID
					    	pointChange.setEmployeeId(employeeId);
					    	// 组织结构ID
							Integer organizationId = null;
							if(counterInfo != null) {
								organizationId = Integer.parseInt(counterInfo.get("organizationID").toString());
							}
							pointChange.setOrganizationId(organizationId);
					    	// 会员积分变化明细
					    	PointChangeDetailDTO changeDetail = new PointChangeDetailDTO();
					    	// 积分值
					    	changeDetail.setPoint(subPoint);
					    	// 积分类型
					    	changeDetail.setPointType(pointType);
					    	// 理由
					    	changeDetail.setReason(reasonText);
					    	changeDetailList.add(changeDetail);
					    	pointChange.setChangeDetailList(changeDetailList);
					    	pointInfo.setPointChange(pointChange);
					    	campBaseDTO.setPointInfo(pointInfo);
					    	if (null != maintainType && !"".equals(maintainType)) {
					    		// 扩展信息
					    		Map<String, Object> extParams = changeDetail.getExtParams();
					    		if (null == extParams) {
					    			extParams = new HashMap<String, Object>();
					    			changeDetail.setExtParams(extParams);
					    		}
					    		extParams.put("PTMAINTYPE", maintainType);
					    	}
					    	// 设置组织ID
							detailMap.put("organizationInfoId", organizationInfoId);
							// 设置品牌ID
							detailMap.put("brandInfoId", brandInfoId);
					    	// 处理会员积分变化信息
					    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
					    	// 当前积分
					    	detailMap.put("curTotalPoint", curPoint);
					    	// 可兑换积分
					    	detailMap.put("curChangablePoint", curChangePoint);
					    	if (!isCurCard) {
						    	// 前卡积分值
						    	detailMap.put("preCardPoint", preCardPoint);
					    	}
					    	// 更新会员积分信息表
					    	binOLCM31_BL.updateMemberPointInfo(detailMap);
					    	// 插入规则执行履历表:累计积分
					    	// 履历区分 ：累计积分
					    	detailMap.put("recordKbn", DroolsConstants.RECORDKBN_5);
					    	// 更新前的值
							detailMap.put("oldValue", String.valueOf(totalPoint));
							// 更新后的值
							detailMap.put("newValue", curPoint);
							// 插入规则执行履历表
							binBEMQMES08_Service.addRuleExecRecord(detailMap);
							// 插入规则执行履历表:可兑换积分
							// 履历区分 ：可兑换积分
					    	detailMap.put("recordKbn", DroolsConstants.RECORDKBN_7);
					    	// 更新前的值
							detailMap.put("oldValue", String.valueOf(ChangablePoint));
							// 更新后的值
							detailMap.put("newValue", curChangePoint);
							// 插入规则执行履历表
							binBEMQMES08_Service.addRuleExecRecord(detailMap);
							// 是否需要同步天猫会员
							if (binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), brandCode)) {
								List<Map<String, Object>> tmSyncInfoList = new ArrayList<Map<String, Object>>();
								Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
								tmSyncInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
								tmSyncInfo.put("brandCode", brandCode);
								tmSyncInfo.put("PgmName", "BINBEMQMES11");
								tmSyncInfoList.add(tmSyncInfo);
								map.put("TmSyncInfoList", tmSyncInfoList);
							}
							// 取得积分MQ消息体
							MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
							if(mqInfoDTO != null) {
								// 发送MQ消息处理
								binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
								// 组织代码
								pointRuleCalInfo.put("OrgCode", campBaseDTO.getOrgCode());
								// 品牌代码
								pointRuleCalInfo.put("BrandCode", campBaseDTO.getBrandCode());
								// 业务类型
								pointRuleCalInfo.put("TradeType", "PT");
								// 会员积分变化主ID
								pointRuleCalInfo.put("pointChangeId", pointChange.getPointChangeId());
								// 计算日期
								pointRuleCalInfo.put("changeDate", pointChange.getChangeDate());
								// 引起积分计算的单据号
								pointRuleCalInfo.put("billCode", pointChange.getTradeNoIF());
								// 引起积分计算的业务类型
								pointRuleCalInfo.put("billType", pointChange.getTradeType());
								// 购买金额
								pointRuleCalInfo.put("amount", pointChange.getAmount());
								// 购买数量
								pointRuleCalInfo.put("quantity", pointChange.getQuantity());
								// 获得积分
								pointRuleCalInfo.put("point", pointChange.getPoint());
								// 会员ID
								pointRuleCalInfo.put("memberInfoId", pointChange.getMemberInfoId());
								// 会员名称
								pointRuleCalInfo.put("name", campBaseDTO.getMemName());
								if(counterInfo != null) {
									// 部门ID
									pointRuleCalInfo.put("organizationId", counterInfo.get("organizationId"));
									// 部门名称
									pointRuleCalInfo.put("departName", counterInfo.get("counterName"));
								}
								// 所属柜台ID
								pointRuleCalInfo.put("MemOrganizationID", detailMap.get("memOrganizationId"));
								// 组织ID
								String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
								// 品牌ID
								String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
								// 积分变化需要发送沟通MQ
								if(binOLCM14_BL.isConfigOpen("1088", orgIdStr, brandIdStr)) {
									// 发送积分变化沟通MQ的最低阀值
									String minPointStr = binOLCM14_BL.getConfigValue("1089", orgIdStr, brandIdStr);
									boolean gtFlag = true;
									if (!CherryChecker.isNullOrEmpty(minPointStr, true)) {
										minPointStr = minPointStr.trim();
										try {
											double minPoint = Double.parseDouble(minPointStr);
											// 当前积分小于最低阈值不发送沟通MQ
											if (pointInfo.getCurTotalPoint() < minPoint) {
												gtFlag = false;
											}
										} catch (Exception e) {
											gtFlag = false;
										}
									}
									if (gtFlag) {
										Map<String, Object> gtMap = new HashMap<String, Object>();
										// 组织ID
										gtMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
										// 品牌ID
										gtMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
										// 组织代码
										gtMap.put("orgCode", campBaseDTO.getOrgCode());
										// 品牌代码
										gtMap.put("brandCode", campBaseDTO.getBrandCode());
										// 事件ID
										gtMap.put("eventId", campBaseDTO.getMemberInfoId());
										// 事件类型:积分变化
										gtMap.put("eventType", "7");
										// 事件发生时间 
										gtMap.put("eventDate", pointChange.getChangeDate());
										// 信息内容:关联单号
										gtMap.put("messageContents", pointChange.getTradeNoIF());
										// 事件来源
										gtMap.put("sourse", "BINBEMQMES11");
										// 取得沟通短信消息体(实时)
										mqInfoDTO = binOLCM31_BL.getGTMQMessage(gtMap);
										// 发送MQ消息处理
										binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
									}
								}
							}
							if (isTmpt) {
								detailMap.put("tmallPointId", tmallPointId);
								detailMap.put("ptFlag", 2);
								detailMap.put("ptResult", 0);
								// 更新会员天猫积分信息
								binBEMQMES08_Service.updateTMPointInfo(detailMap);
								detailMap.put("tmallRecallFlag", 0);
								// 更新积分变化主表
								binBEMQMES08_Service.updateTMUsedInfo(detailMap);
								Map<String, Object> tmRecallInfo = new HashMap<String, Object>();
								tmRecallInfo.put("mixMobile", mixMobile);
								tmRecallInfo.put("recordId", recordId);
								tmRecallInfo.put("memUsedInfoId", memUsedInfoId);
								tmRecallInfo.put("brandCode", brandCode);
								map.put("TmRecallInfo", tmRecallInfo);
//								callbackTmall(mixMobile, recordId, null, brandCode);
							}
			//				// 更新前的值
			//				detailMap.put("oldValue", oldPoint);
			//				// 修改积分总值的场合
			//				if("1".equals(subTradeType)) {
			//					// 更新后的值
			//					detailMap.put("newValue", modifyPoint);
			//				} else { // 修改积分差值的场合
			//					// 当前积分
			//					String curPoint = String.valueOf(Double.parseDouble(oldPoint.toString()) + Double.parseDouble(modifyPoint));
			//					// 更新后的值
			//					detailMap.put("newValue", curPoint);
			//				}
			//				// 插入规则执行履历表
			//				binBEMQMES08_Service.addRuleExecRecord(detailMap);
						}
					}
				}
    		}
    	}

		if (memberFirstSale.equals("true")){
			Map<String, Object> detailMap = (Map<String, Object>) detailList.get(0);
			this.setInsertInfoMapKey(detailMap);
			// 组织ID
			int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
			// 品牌ID
			int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
			// 老会员推荐新会员销售需要发送沟通MQ
			if(binOLCM14_BL.isConfigOpen("1401", String.valueOf(organizationInfoId),
					String.valueOf(brandInfoId))) {
				map.put("eventType", "20");
				map.put("eventId", detailMap.get("memberIdCurrent"));
				map.put("eventDate",sysDate);
				map.put("sourse", "BINOLMBMBM11");
				map.put("organizationInfoID", organizationInfoId);
				map.put("brandInfoID", brandInfoId);
				// 发送会员入会沟通事件MQ消息
				binBEMQMES03_BL.sendGTMQ(map);
			}
		}
		//信息插入到MogoDB
		DBObject dbObject = new BasicDBObject();
		// 组织代号
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改回数
		dbObject.put("ModifyCounts", map.get("modifyCounts"));
		// 业务主体
		dbObject.put("TradeEntity", "0");
		Map<String, Object> tempMap  = (Map<String, Object>)detailList.get(0);
		// 业务主体代号
		dbObject.put("TradeEntityCode", tempMap.get("memberCode"));
		//发生时间
		dbObject.put("OccurTime", tempMap.get("businessTime"));
		// 操作员工
		dbObject.put("UserCode", tempMap.get("employeeCode"));
		// 消息体
		dbObject.put("Content", map.get("messageBody"));
		map.put("dbObject", dbObject);
		if (!pointRuleCalInfo.isEmpty()) {
			map.put("pointRuleCalInfo", pointRuleCalInfo);
		}
	}
	
	
	
	/**
	 * 更新会员积分信息(历史积分调整)
	 * @param map
	 * 			本次会员积分主记录
	 * @param detailMap
	 * 			本次会员积分维护明细记录
	 * @param memPointInfo
	 * 			当前会员积分信息
	 * @throws Exception 
	 * 
	 */
	private void updateHistoryPoint(Map<String, Object> map, Map<String, Object> detailMap, Map<String, Object> memPointInfo) throws Exception{
		// 修改前积分
		double totalPoint = Double.parseDouble(memPointInfo.get("curTotalPoint").toString());
		// 前卡积分值
		double preCardPoint = Double.parseDouble(memPointInfo.get("mbPreCardPoint").toString());
		// 累计失效积分
		double totalDisablePoint = Double.parseDouble(memPointInfo.get("totalDisablePoint").toString());
		// 本次将失效积分
		double curDisablePoint = Double.parseDouble(memPointInfo.get("curDisablePoint").toString());
		// 上回积分失效日期
		String preDisableDate = (String) memPointInfo.get("preDisableDate");
		// 本次积分失效日期
		String curDealDate = (String) memPointInfo.get("curDealDate");
		// 可兑换积分
		double ChangablePoint = Double.parseDouble(memPointInfo.get("curChangablePoint").toString());
		// 当前累计兑换积分
		double curTotalChanged = Double.parseDouble(memPointInfo.get("curTotalChanged").toString());
		// 初始导入总积分
		double initialPoint = Double.parseDouble(memPointInfo.get("initialMPPoint").toString());
		// 初始导入可兑换积分
		double initChangablePoint = Double.parseDouble(memPointInfo.get("initMPChangablePoint").toString());
		// 修改后当前积分
		double curPoint = 0;
		// 修改的积分值
		double modPoint = 0;
		// 积分差值
		double subPoint = 0;
		// 取得维护积分数
		String modifyPoint = (String)detailMap.get("modifyPoint");
		if (!CherryChecker.isNullOrEmpty(modifyPoint, true)) {
			modPoint = Double.parseDouble(modifyPoint);
		}
		// 取得子业务类型
		String subTradeType = (String)map.get("subTradeType");
		// 积分类型
		String pointType = null;
		// 修改积分总值的场合
		if("1".equals(subTradeType)) {
			curPoint = modPoint;
			subPoint = DoubleUtil.sub(curPoint, totalPoint);
			pointType = DroolsConstants.POINTTYPE0;
		} else { // 修改积分差值的场合
			subPoint = modPoint;
			curPoint = DoubleUtil.add(totalPoint, subPoint);
			pointType = DroolsConstants.POINTTYPE99;
		}
		// 当前可兑换积分
		double curChangePoint = DoubleUtil.add(ChangablePoint, subPoint);
		initialPoint = DoubleUtil.add(initialPoint, subPoint);
		initChangablePoint = DoubleUtil.add(initChangablePoint, subPoint);
		// 员工ID
		int employeeId = 0;
		if (null != map.get("employeeID")) {
			employeeId = Integer.parseInt(map.get("employeeID").toString());
		}
		// 会员信息ID
		int memberInfoId = Integer.parseInt(detailMap.get("memberInfoId").toString());
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get("organizationInfoID").toString());
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoID").toString());
		// 会员卡号
		String memberCode = (String) detailMap.get("memberCode");
		// 单据产生日期
		String businessTime = (String) detailMap.get("acquiTime");
		// 设置组织代码
		String orgCode = (String) map.get("orgCode");
		// 设置品牌代码
		String brandCode = (String) map.get("brandCode");
		// 系统时间
		String sysDate = (String) detailMap.get("calcDate");
		// 备注信息
		String reasonText = (String) detailMap.get("reasonText");
		// 积分类型
    	String maintainType = (String) map.get("maintainType");
		// 会员DTO
		CampBaseDTO campBaseDTO = new CampBaseDTO();
		// 会员信息ID
		campBaseDTO.setMemberInfoId(memberInfoId);
		// 会员姓名
		campBaseDTO.setMemName((String) detailMap.get("memberName"));
		// 组织代码
		campBaseDTO.setOrgCode(orgCode);
		// 品牌代码
		campBaseDTO.setBrandCode(brandCode);
		// 组织信息ID
		campBaseDTO.setOrganizationInfoId(organizationInfoId);
		// 品牌ID
		campBaseDTO.setBrandInfoId(brandInfoId);
		// 计算时间
		campBaseDTO.setCalcDate(sysDate);
		// 积分信息DTO
		PointDTO pointInfo = new PointDTO();
		// 会员积分变化主记录
		PointChangeDTO pointChange = new PointChangeDTO();
		// 会员积分变化明细记录
    	List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
    	// 当前总积分
    	pointInfo.setCurTotalPoint(curPoint);
    	// 累计兑换积分
    	pointInfo.setCurTotalChanged(curTotalChanged);
    	// 可兑换积分
    	pointInfo.setCurChangablePoint(curChangePoint);
    	// 累计失效积分
    	pointInfo.setTotalDisablePoint(totalDisablePoint);
    	// 本次将失效积分
    	pointInfo.setCurDisablePoint(curDisablePoint);
    	// 上回积分失效日期
    	pointInfo.setPreDisableDate(preDisableDate);
    	// 本次积分失效日期
    	pointInfo.setCurDealDate(curDealDate);
    	// 前卡积分值
    	pointInfo.setPreCardPoint(preCardPoint);
    	// 所属组织ID
    	pointChange.setOrganizationInfoId(organizationInfoId);
    	// 所属品牌ID
    	pointChange.setBrandInfoId(brandInfoId);
    	// 单据号
    	pointChange.setTradeNoIF((String) map.get("tradeNoIF"));
    	// 业务类型
    	pointChange.setTradeType((String) map.get("tradeType"));
    	// 会员信息ID
    	pointChange.setMemberInfoId(memberInfoId);
    	// 会员卡号
    	pointChange.setMemCode(memberCode);
    	// 积分变化日期
    	pointChange.setChangeDate(businessTime);
    	// 积分值 
    	pointChange.setPoint(subPoint);
    	// 员工ID
    	pointChange.setEmployeeId(employeeId);
    	// 组织结构ID
		Integer organizationId = null;
		if(map.get("organizationID") != null) {
			organizationId = Integer.parseInt(map.get("organizationID").toString());
		}
		pointChange.setOrganizationId(organizationId);
    	// 会员积分变化明细
    	PointChangeDetailDTO changeDetail = new PointChangeDetailDTO();
    	// 积分值
    	changeDetail.setPoint(subPoint);
    	// 积分类型
    	changeDetail.setPointType(pointType);
    	// 理由
    	changeDetail.setReason(reasonText);
    	changeDetailList.add(changeDetail);
    	pointChange.setChangeDetailList(changeDetailList);
    	pointInfo.setPointChange(pointChange);
    	campBaseDTO.setPointInfo(pointInfo);
    	if (null != maintainType && !"".equals(maintainType)) {
    		// 扩展信息
    		Map<String, Object> extParams = changeDetail.getExtParams();
    		if (null == extParams) {
    			extParams = new HashMap<String, Object>();
    			changeDetail.setExtParams(extParams);
    		}
    		extParams.put("PTMAINTYPE", maintainType);
    	}
    	// 设置组织ID
		detailMap.put("organizationInfoId", organizationInfoId);
		// 设置品牌ID
		detailMap.put("brandInfoId", brandInfoId);
    	// 处理会员积分变化信息
    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
    	// 当前积分
    	detailMap.put("curTotalPoint", curPoint);
    	// 可兑换积分
    	detailMap.put("curChangablePoint", curChangePoint);
    	// 初始导入总积分
    	detailMap.put("initialPoint", initialPoint);
    	// 初始导入可兑换积分
    	detailMap.put("initChangablePoint", initChangablePoint);
    	// 初始导入时间
    	String initialTime = (String) memPointInfo.get("initialMPTime");
    	detailMap.put("initialTime", initialTime);
    	// 初始导入ID
    	detailMap.put("initialId", memPointInfo.get("initialId"));
    	// 初始导入已兑换积分
    	detailMap.put("initTotalChanged", memPointInfo.get("initMPTotalChanged"));
    	// 初始导入最后更新时间
    	detailMap.put("initialLastdate", memPointInfo.get("initialLastdate"));
    	// 更新会员积分信息表(历史记录)
    	binOLCM31_BL.updateHistoryPointInfo(detailMap);
    	// 取得积分MQ消息体
		MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
		if(mqInfoDTO != null) {
			// 发送MQ消息处理
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
		}
		Map<String, Object> reCalcMap = new HashMap<String, Object>();
		// 组织代码
		reCalcMap.put("orgCode", orgCode);
		// 品牌代码
		reCalcMap.put("brandCode", brandCode);
		// 组织ID
		reCalcMap.put("organizationInfoID", organizationInfoId);
		// 品牌ID
		reCalcMap.put("brandInfoID", brandInfoId);
		// 会员信息ID
		reCalcMap.put("memberInfoId", memberInfoId);
		// 会员卡号
		reCalcMap.put("memberCode", memberCode);
		// 重算区分
		reCalcMap.put("reCalcType", DroolsConstants.RECALCTYPE0);
		// 重算时间
		reCalcMap.put("acquiTime", businessTime);
		// 插入重算信息表
		binBEMQMES08_BL.addReCalcInfo(reCalcMap);
		// 发送MQ重算消息进行实时重算
		binBEMQMES08_BL.sendReCalcMsg(reCalcMap);
	}
	
	public void setInsertInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "BINBEMQMES08");
		map.put("createPGM", "BINBEMQMES08");
		map.put("updatedBy", "BINBEMQMES08");
		map.put("updatePGM", "BINBEMQMES08");
	}
}
