package com.cherry.cp.point.validate;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.validate.BaseValidate;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.dianping.zebra.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PointValidate extends BaseValidate{
	
	/** 赠送积分模板索引 */
	private int BASE000015_INDEX;
	
	/** 赠送积分积分分段模板索引 */
	private int BASE000015_SEGME_INDEX;
	
	/** 升级模板索引 */
	private int BUS000040_INDEX;
	
	/**
	 * 赠送积分模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000015_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			// 积分/积分倍数
			String pointTxt = null;
			// 积分倍数
			if ("1".equals(map.get("calcuKbn"))) {
				pointTxt = PropertiesUtil.getText("PMB00026");
				// 积分
			} else {
				pointTxt = PropertiesUtil.getText("PMB00025");
			}
			// 积分分段设置
			List<Map<String, Object>> segmePointList = (List<Map<String, Object>>) map.get("segmePoints");
			if (null != segmePointList && !segmePointList.isEmpty()) {
				// 前一分段的金额上限
				double preht = -1;
				// 前一分段的金额上限区分
				String prehk = null;
				for (int i = 0; i < segmePointList.size(); i++) {
					Map<String, Object> segmePoint = segmePointList.get(i);
					// 积分奖励
					String rewardPoint = (String) segmePoint.get("rewardPoint");
					// 积分奖励为空验证
					if(CherryChecker.isNullOrEmpty(rewardPoint, true)){
						actionErrorList.add(new ActionErrorDTO(1, "rewardPoint-" + BASE000015_SEGME_INDEX, "ECM00009", new String[] { pointTxt })) ;
						isCorrect = false;
					} else {
						if (rewardPoint.indexOf("-") == 0) {
							rewardPoint = rewardPoint.replace("-", "");
						}
						// 积分奖励浮点数验证
						if(!CherryChecker.isFloatValid(rewardPoint, 10, 2)){
							actionErrorList.add(new ActionErrorDTO(1, "rewardPoint-" + BASE000015_SEGME_INDEX, "ECM00024", new String[] { pointTxt, "10", "2" })) ;
							isCorrect = false;
						}
					}
					// 金额下限区分
					String lk = (String) segmePoint.get("lowerLimit");
					// 金额上限区分
					String hk = (String) segmePoint.get("highLimit");
					// 金额下限
					String lowerAmount = (String) segmePoint.get("lowerAmount");
					// 金额上限
					String highAmount = (String) segmePoint.get("highAmount");
					boolean lowerFlag = true;
					boolean highFlag = true;
					// 金额下限为空验证
					if(CherryChecker.isNullOrEmpty(lowerAmount, true)){
						actionErrorList.add(new ActionErrorDTO(1, "lowerAmount-" + BASE000015_SEGME_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00047") })) ;
						isCorrect = false;
						lowerFlag = false;
						// 金额下限浮点数验证
					} else if (!CherryChecker.isFloatValid(lowerAmount, 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "lowerAmount-" + BASE000015_SEGME_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00047"), "10", "2" })) ;
						isCorrect = false;
						lowerFlag = false;
					}
					
					// 金额上限为空验证
					if(CherryChecker.isNullOrEmpty(highAmount, true)){
						if (i < segmePointList.size() - 1) {
							actionErrorList.add(new ActionErrorDTO(1, "highAmount-" + BASE000015_SEGME_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ;
							isCorrect = false;
						}
						highFlag = false;
						// 金额上限浮点数验证
					} else if (!CherryChecker.isFloatValid(highAmount, 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "highAmount-" + BASE000015_SEGME_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"), "10", "2" })) ;
						isCorrect = false;
						highFlag = false;
					}
					// 金额下限
					double lt = -1;
					// 金额上限
					double ht = -1;
					if (lowerFlag) {
						lt = Double.parseDouble(lowerAmount);
					}
					if (highFlag) {
						ht = Double.parseDouble(highAmount);
					}
					// 金额下限大于等于上限
					if (lowerFlag && highFlag && lt >= ht) {
						actionErrorList.add(new ActionErrorDTO(1, "lowerAmount-" + BASE000015_SEGME_INDEX, "ECM00051", new String[] { PropertiesUtil.getText("PMB00047"), PropertiesUtil.getText("PMB00029")})) ;
						isCorrect = false;
					}
					// 验证金额分段是否有重叠
					if (lowerFlag && preht != -1) {
						if (lt < preht || (lt == preht && "0".equals(lk) && "1".equals(prehk))) {
							actionErrorList.add(new ActionErrorDTO(1, "lowerAmount-" + BASE000015_SEGME_INDEX, "ECP00027", null)) ;
							isCorrect = false;
						}
					}
					preht = ht;
					prehk = hk;
					BASE000015_SEGME_INDEX++;
				}
			} else {
				// 积分奖励
				String multipleMark = (String) map.get("multipleMark");
				// 积分奖励为空验证
				if(CherryChecker.isNullOrEmpty(multipleMark, true)){
					actionErrorList.add(new ActionErrorDTO(1, "multipleMark-" + BASE000015_INDEX, "ECM00009", new String[] { pointTxt })) ;
					isCorrect = false;
				} else {
					if (multipleMark.indexOf("-") == 0) {
						multipleMark = multipleMark.replace("-", "");
					}
					// 积分奖励浮点数验证
					if(!CherryChecker.isFloatValid(multipleMark, 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "multipleMark-" + BASE000015_INDEX, "ECM00024", new String[] { pointTxt, "10", "2" })) ;
						isCorrect = false;
					}
				}
			}
		}
		BASE000015_INDEX++;
	}
	
	/**
	 * 活动范围模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000016_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			// 选择按等级时，等级不能为空
			if("1".equals(map.get("member"))){
				List<Map<String, Object>> memberList = (List<Map<String, Object>>) map.get("memberLevelList");
				boolean notChoiceFlag = true;
				for(Map<String, Object> memberInfo : memberList){
					if(memberInfo.containsKey("memberShowFlag")){
						notChoiceFlag = false;
						break;
					}
				}
				if(notChoiceFlag){
					actionErrorList.add(new ActionErrorDTO(1, "member", "ECM00054", new String[] { PropertiesUtil.getText("PMB00027") })) ; // 按等级选择时，会员等级必选
					isCorrect = false;
				}
			}
			// 是否精确到秒
			String timeSetting = (String) map.get("timeSetting");
			if ("1".equals(timeSetting)) {
				String timeRegex = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
				// 开始时
				String startHH = (String) map.get("startHH");
				// 开始分
				String startMM = (String) map.get("startMM");
				// 开始秒
				String startSS = (String) map.get("startSS");
				String startTime = null;
				boolean timeFlag = true;
				if (CherryChecker.isNullOrEmpty(startHH, true) || CherryChecker.isNullOrEmpty(startMM, true) ||
						CherryChecker.isNullOrEmpty(startSS, true)) {
					actionErrorList.add(new ActionErrorDTO(1, "startSS", "ECM00009", new String[] {  PropertiesUtil.getText("PCP00033") }));
					isCorrect = false;
					timeFlag = false;
				} else {
					startTime = startHH + ":" + startMM + ":" + startSS;
					if (!startTime.matches(timeRegex)) {
						actionErrorList.add(new ActionErrorDTO(1, "startSS", "ECM00008", new String[] { PropertiesUtil.getText("PCP00033") }));
						isCorrect = false;
						timeFlag = false;
					}
				}
				// 会员活动信息
				CampaignDTO campaignDTO = (CampaignDTO) baseMap.get("campInfo");
				if (!CherryChecker.isNullOrEmpty(campaignDTO.getCampaignToDate(), true)) {
					// 结束时
					String endHH = (String) map.get("endHH");
					// 结束分
					String endMM = (String) map.get("endMM");
					// 结束秒
					String endSS = (String) map.get("endSS");
					String endTime = null;
					if (CherryChecker.isNullOrEmpty(endHH, true) || CherryChecker.isNullOrEmpty(endMM, true) ||
							CherryChecker.isNullOrEmpty(endSS, true)) {
						actionErrorList.add(new ActionErrorDTO(1, "endSS", "ECM00009", new String[] { PropertiesUtil.getText("PCP00034") }));
						isCorrect = false;
						timeFlag = false;
					} else {
						endTime = endHH + ":" + endMM + ":" + endSS;
						if (!endTime.matches(timeRegex)) {
							actionErrorList.add(new ActionErrorDTO(1, "endSS", "ECM00008", new String[] { PropertiesUtil.getText("PCP00034") }));
							isCorrect = false;
							timeFlag = false;
						}
					}
					// 开始日结束日为同一天
					if (timeFlag && CherryChecker.compareDate(campaignDTO.getCampaignFromDate(), campaignDTO.getCampaignToDate()) == 0) {
						Date fromDate = DateUtil.coverString2Date(campaignDTO.getCampaignFromDate());
						String campFromDate = DateUtil.date2String(fromDate, DateUtil.DATE_PATTERN) + " " + startTime;
						// 带时分秒的开始日
						Date fromDateTime = DateUtil.coverString2Date(campFromDate, DateUtil.DATETIME_PATTERN);
						if (null == fromDateTime) {
							actionErrorList.add(new ActionErrorDTO(1, "startSS", "ECM00008", new String[] { PropertiesUtil.getText("PCP00033") }));
							isCorrect = false;
						}
						Date toDate = DateUtil.coverString2Date(campaignDTO.getCampaignToDate());
						String campToDate = DateUtil.date2String(toDate, DateUtil.DATE_PATTERN) + " " + endTime;
						// 带时分秒的开始日
						Date toDateTime = DateUtil.coverString2Date(campToDate, DateUtil.DATETIME_PATTERN);
						if (null == toDateTime) {
							actionErrorList.add(new ActionErrorDTO(1, "endSS", "ECM00008", new String[] { PropertiesUtil.getText("PCP00034") }));
							isCorrect = false;
						}
						// 开始日时间比结束日时间晚
						if (null != fromDateTime && null != toDateTime && fromDateTime.compareTo(toDateTime) > 0) {
							actionErrorList.add(new ActionErrorDTO(1, "endSS", "ECM00052", new String[] { PropertiesUtil.getText("PCP00033"), PropertiesUtil.getText("PCP00034") }));
							isCorrect = false;
						}
					}
				}
			}
			if ("2".equals(map.get("mPoint"))) {
				if (CherryChecker.isNullOrEmpty(map.get("minpt")) 
						|| CherryChecker.isNullOrEmpty(map.get("maxpt"))) {
					actionErrorList.add(new ActionErrorDTO(1, "maxpt", "ECM00009", new String[] { PropertiesUtil.getText("PMB00089") })) ;
					isCorrect = false;
				}
			}
			// 按柜台选择时，必须选择柜台
			if(!"0".equals(map.get("choicePlace")) && (map.get("checkNodes") == null || ((List<Map<String,Object>>) map.get("checkNodes")).isEmpty())){
				actionErrorList.add(new ActionErrorDTO(1, "choicePlace", "ECM00054", new String[] { PropertiesUtil.getText("PMB00028") })) ; // 必须选择柜台
				isCorrect = false;
			}
		}
	}
	
	/**
	 * 计算积分模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000017_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if("1".equals(map.get("oneLimit"))){
				if("0".equals(map.get("onePoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("maxMenoy"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "maxMenoy", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("maxMenoy"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "maxMenoy", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"),"10","2" })) ; 
						isCorrect = false;
					}
				}
				if("1".equals(map.get("onePoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("maxMenoyGive"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "maxMenoyGive", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("maxMenoyGive"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "maxMenoyGive", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"),"10","2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("multiple"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "multiple", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("multiple"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "multiple", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"),"10","2" })) ; 
						isCorrect = false;
					}
				}
				if("2".equals(map.get("onePoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("maxPoint"),true)){
							actionErrorList.add(new ActionErrorDTO(1, "maxPoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
							isCorrect = false;
					 }
					if(!CherryChecker.isFloatValid((String) map.get("maxPoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "maxPoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("3".equals(map.get("onePoint"))){ 
					if(CherryChecker.isNullOrEmpty(map.get("maxGivePoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "maxGivePoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("maxGivePoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "maxGivePoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("mulGive"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "mulGive", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("mulGive"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "mulGive", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
			}
			if("1".equals(map.get("allLimit"))){
				if("0".equals(map.get("allPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("allMaxMenoy"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "allMaxMenoy", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("allMaxMenoy"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "allMaxMenoy", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("1".equals(map.get("allPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("allMaxGive"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "allMaxGive", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("allMaxGive"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "allMaxGive", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"), "10", "2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("allMultiple"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "allMultiple", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("allMultiple"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "allMultiple", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("2".equals(map.get("allPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("allLimitPoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "allLimitPoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("allLimitPoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "allLimitPoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("3".equals(map.get("allPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("maxAllGivePoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "maxAllGivePoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("maxAllGivePoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "maxAllGivePoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("mulAllGive"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "mulAllGive", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("mulAllGive"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "mulAllGive", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
			}
			if("1".equals(map.get("actLimit"))){
				if("0".equals(map.get("actPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("actMaxMenoy"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actMaxMenoy", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("actMaxMenoy"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actMaxMenoy", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("1".equals(map.get("actPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("actMaxGive"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actMaxGive", "ECM00009", new String[] { PropertiesUtil.getText("PMB00029") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("actMaxGive"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actMaxGive", "ECM00024", new String[] { PropertiesUtil.getText("PMB00029"), "10", "2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("actMultiple"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actMultiple", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;	
					}
					if(!CherryChecker.isFloatValid((String) map.get("actMultiple"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actMultiple", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("2".equals(map.get("actPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("actLimitPoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actLimitPoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("actLimitPoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actLimitPoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
				if("3".equals(map.get("actPoint"))){
					if(CherryChecker.isNullOrEmpty(map.get("actLimitGivePoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actLimitGivePoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("actLimitGivePoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actLimitGivePoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })) ; 
						isCorrect = false;
					}
					if(CherryChecker.isNullOrEmpty(map.get("actGivePoint"),true)){
						actionErrorList.add(new ActionErrorDTO(1, "actGivePoint", "ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })) ; 
						isCorrect = false;
					}
					if(!CherryChecker.isFloatValid((String) map.get("actGivePoint"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "actGivePoint", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ; 
						isCorrect = false;
					}
				}
			}
			if("0".equals(map.get("validForever"))){
				if(CherryChecker.isNullOrEmpty(map.get("validMonth"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "validMonth", "ECM00009", new String[] { PropertiesUtil.getText("PMB00031") })) ; 
					isCorrect = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("validMonth"))){
					actionErrorList.add(new ActionErrorDTO(1, "validMonth", "ECM00021", new String[] { PropertiesUtil.getText("PMB00031") })) ; 
					isCorrect = false;
				}
			}
		}
	}
	
	/**
	 * 会员入会模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000018_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			List<Map<String, Object>> memberList = (List<Map<String, Object>>) map.get("combTemps");
			if (null != memberList) {
				String compareLevel = (String) memberList.get(0).get("memberLevelId");
				String levelIdArr = null;
				for(int i = 1; i < memberList.size();i++){
					Map<String, Object> member = memberList.get(i);
					levelIdArr = (String) member.get("memberLevelId");
					compareLevel = compareLevel + "_" + levelIdArr;
				}
				if(null != levelIdArr){
					String[] str = compareLevel.split("_");
					for(int i = str.length - 1;i > 0;i--){
						for(int j = i-1;j >= 0;j--){
							if(str[i].equals(str[j])){
								actionErrorList.add(new ActionErrorDTO(1, "memberLevelId-" + i, "ECM00032", new String[] { PropertiesUtil.getText("PMB00032") })) ;
								isCorrect = false;
								i = 0;
								break;
							}
						}
					}
				}
				for(int i = 0; i < memberList.size();i++){
					Map<String, Object> member = memberList.get(i);
					// 入会时间
					String jonDate = (String) member.get("jonDate");
					// 入会后(天)
					if("2".equals(jonDate)){
						// 天数
						String jonDateLimit = (String) member.get("jonDateLimit");
						// 天数为空验证
//						if(CherryChecker.isNullOrEmpty(jonDateLimit)){
//							actionErrorList.add(new ActionErrorDTO(1, "jonDateLimit-" + i, "ECM00009", new String[] { PropertiesUtil.getText("PMB00033") })) ;
//							isCorrect = false;
//							// 天数数字验证
//						} else 
						if(!CherryChecker.isNullOrEmpty(jonDateLimit) && !CherryChecker.isNumeric(jonDateLimit)){
							actionErrorList.add(new ActionErrorDTO(1, "jonDateLimit-" + i, "ECM00021", new String[] { PropertiesUtil.getText("PMB00033") })) ;
							isCorrect = false;
						}
						// 入会后(月)
					} else if ("3".equals(jonDate)) {
						// 月数
						String jonMonthLimit = (String) member.get("jonMonthLimit");
						// 月数为空验证
						if(CherryChecker.isNullOrEmpty(jonMonthLimit)){
							actionErrorList.add(new ActionErrorDTO(1, "jonMonthLimit-" + i, "ECM00009", new String[] { PropertiesUtil.getText("PMB00008") })) ;
							isCorrect = false;
							// 月数数字验证
						} else if(!CherryChecker.isNumeric(jonMonthLimit)){
							actionErrorList.add(new ActionErrorDTO(1, "jonMonthLimit-" + i, "ECM00021", new String[] { PropertiesUtil.getText("PMB00008") })) ;
							isCorrect = false;
						}
					}
					// 选择单次
					String firstBillSel = (String) member.get("firstBillSel");
					// 指定单次
					if ("3".equals(firstBillSel)) {
						// 单次
						String billTime = (String) member.get("billTime");
						// 单次为空验证
						if(CherryChecker.isNullOrEmpty(billTime)){
							actionErrorList.add(new ActionErrorDTO(1, "billTime-" + i, "ECM00009", new String[] { PropertiesUtil.getText("PCP00038") })) ;
							isCorrect = false;
							// 单次数字验证
						} else if(!CherryChecker.isNumeric(billTime)){
							actionErrorList.add(new ActionErrorDTO(1, "billTime-" + i, "ECM00021", new String[] { PropertiesUtil.getText("PCP00038") })) ;
							isCorrect = false;
						}
					}
				}
			}
		}
	}
	/**
	 * 会员生日模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000022_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			// 活动周期
			String birthday = (String) map.get("birthday");
			// 生日当周
			if ("2".equals(birthday)) {
				// 生日前天数
				String befDay = (String) map.get("befDay");
				// 生日后天数
				String aftDay = (String) map.get("aftDay");
				boolean isBefEmpty = CherryChecker.isNullOrEmpty(befDay);
				boolean isAftEmpty = CherryChecker.isNullOrEmpty(aftDay);
				// 生日前天数数字验证
				if(!isBefEmpty && !CherryChecker.isNumeric(befDay)){
					actionErrorList.add(new ActionErrorDTO(1, "befDay", "ECM00021", new String[] { PropertiesUtil.getText("PCP00039") })) ;
					isCorrect = false;
				}
				// 生日后天数数字验证
				if(!isAftEmpty && !CherryChecker.isNumeric(aftDay)){
					actionErrorList.add(new ActionErrorDTO(1, "aftDay", "ECM00021", new String[] { PropertiesUtil.getText("PCP00040") })) ;
					isCorrect = false;
				}
				if (isBefEmpty && isAftEmpty) {
					actionErrorList.add(new ActionErrorDTO(1, "aftDay", "ECP00028", null)) ;
					isCorrect = false;
				}
			}
			// 选择单次
			String firstBillSel = (String) map.get("firstBillSel");
			// 指定单次
			if ("3".equals(firstBillSel)) {
				// 单次
				String billTime = (String) map.get("billTime");
				// 单次为空验证
				if(CherryChecker.isNullOrEmpty(billTime)){
					actionErrorList.add(new ActionErrorDTO(1, "billTime", "ECM00009", new String[] { PropertiesUtil.getText("PCP00038") })) ;
					isCorrect = false;
					// 单次数字验证
				} else if(!CherryChecker.isNumeric(billTime)){
					actionErrorList.add(new ActionErrorDTO(1, "billTime", "ECM00021", new String[] { PropertiesUtil.getText("PCP00038") })) ;
					isCorrect = false;
				}
			}
		}
	}
	/**
	 * 首单模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000019_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			// 特定时间
			String firstBillDate = (String) map.get("firstBillDate");
			// 指定日期
			if("3".equals(firstBillDate)){
				boolean dayFlag = true; 
				if(CherryChecker.isNullOrEmpty(map.get("billStartMonth"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "billStartMonth", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(CherryChecker.isNullOrEmpty(map.get("billStartDay"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00009", new String[] { PropertiesUtil.getText("PMB00035") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("billStartMonth"))){
					actionErrorList.add(new ActionErrorDTO(1, "billStartMonth", "ECM00021", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("billStartDay"))){
					actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00021", new String[] { PropertiesUtil.getText("PMB00035") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(dayFlag){
					isCorrect = checkDay(Integer.parseInt((String) map.get("billStartMonth")), Integer.parseInt((String) map.get("billStartDay")));
				}
				// 指定时间段
			} else if ("4".equals(firstBillDate)) {
				
			} else if ("7".equals(firstBillDate)) {
				if(CherryChecker.isNullOrEmpty(map.get("saleStartTime"),true) || CherryChecker.isNullOrEmpty(map.get("saleEndTime"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "saleEndTime", "ECM00009", new String[] { PropertiesUtil.getText("PMB00091") })) ;
					isCorrect = false;
				}
				// 特定产品
				List<Map<String, Object>> camTempsValidates = ConvertUtil.json2List(ConvertUtil.getString(baseMap.get("camTempsValidate")));
				if(!CollectionUtils.isEmpty(camTempsValidates)) {
					Map<String, Object> combTemps = camTempsValidates.get(0);
					List<Map<String, Object>> productList = (List<Map<String, Object>>) combTemps.get("productList");
					if(null == productList||productList.size()==0) {
						actionErrorList.add(new ActionErrorDTO(1, "proCond", "ECM00009", new String[] { PropertiesUtil.getText("PMB00092") })) ;
						isCorrect = false;
					}
				}


			}
			// 选择单次
			String firstBillSel = (String) map.get("firstBillSel");
			// 指定单次
			if ("3".equals(firstBillSel)) {
				// 单次
				String billTime = (String) map.get("billTime");
				// 单次为空验证
				if(CherryChecker.isNullOrEmpty(billTime)){
					actionErrorList.add(new ActionErrorDTO(1, "billTime", "ECM00009", new String[] { PropertiesUtil.getText("PCP00038") })) ;
					isCorrect = false;
					// 单次数字验证
				} else if(!CherryChecker.isNumeric(billTime)){
					actionErrorList.add(new ActionErrorDTO(1, "billTime", "ECM00021", new String[] { PropertiesUtil.getText("PCP00038") })) ;
					isCorrect = false;
				}
			}
		}
	}

	
	/**
	 * 单次购买模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000020_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			// 单次购买大模块中的小模块集合
			List<Map<String, Object>> comList = (List<Map<String, Object>>) map.get("combTemps");
			for(int i = comList.size() - 1;i > 0;i--){
				Map<String, Object> comMap = comList.get(i);
				if(CherryChecker.isNullOrEmpty(comMap.get("minMoney"), true) && CherryChecker.isNullOrEmpty(comMap.get("maxMoney"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "minMoney-" + i, "PMB00024", new String[] { PropertiesUtil.getText("PMB00036") })) ;
					isCorrect = false;
					break;
				}
				if(!CherryChecker.isFloatValid((String) comMap.get("minMoney"), 10, 2)){
					actionErrorList.add(new ActionErrorDTO(1, "minMoney-" + i, "ECM00024", new String[] { PropertiesUtil.getText("PMB00037"), "10", "2" })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isFloatValid((String) comMap.get("maxMoney"), 10, 2)){
					actionErrorList.add(new ActionErrorDTO(1, "maxMoney-" + i, "ECM00024", new String[] { PropertiesUtil.getText("PMB00037"), "10", "2" })) ;
					isCorrect = false;
					break;
				}
				int min = 0;
				int max = 1000000000;
				if(null != comMap.get("minMoney")){
					min = Integer.parseInt((String) comMap.get("minMoney"));
				}
				if(null != comMap.get("minMoney")){
					max = Integer.parseInt((String) comMap.get("maxMoney"));
				}
				if(min > max){
					actionErrorList.add(new ActionErrorDTO(1, "maxMoney-" + i, "ECM00033", new String[] { PropertiesUtil.getText("PMB00029"), PropertiesUtil.getText("PMB00047")})) ;
					isCorrect = false;
					break;
				}
				for(int j = i-1;j >= 0;j--){
					Map<String, Object> compareMap = comList.get(j);
					if(min < Integer.parseInt((String) compareMap.get("maxMoney")) && max > Integer.parseInt((String) compareMap.get("minMoney"))){
						actionErrorList.add(new ActionErrorDTO(1, "maxMoney-" + i, "ECM00053", new String[] { PropertiesUtil.getText("PMB00038"),PropertiesUtil.getText("PMB00039") })) ;
						isCorrect = false;
					}
				}
			}
		}
	}
	
	/**
	 * 购买产品模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000021_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if(!"0".equals(map.get("product")) && CherryChecker.isNullOrEmpty(map.get("productList"), true)){
				actionErrorList.add(new ActionErrorDTO(1, "product", "ECM00054", new String[] { PropertiesUtil.getText("PMB00046") })) ;
				isCorrect = false;
			}
			if ("0".equals(map.get("rangeFlag"))) {
				String rangeNum = (String) map.get("rangeNum");
				if (!CherryChecker.isNullOrEmpty(rangeNum)) {
					// 单品数量上限数字验证
					if(!CherryChecker.isNumeric(rangeNum) || Integer.parseInt(rangeNum) <= 0){
						actionErrorList.add(new ActionErrorDTO(1, "rangeNum", "ECM00107", new String[] { PropertiesUtil.getText("PCP00056") })) ;
						isCorrect = false;
					}
				}
			}
		}
	}
	
	/**
	 * 会员日模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000023_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if("0".equals("dayFlag")){
				if(CherryChecker.isNullOrEmpty(map.get("monthText"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "monthText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("monthText"))){
					actionErrorList.add(new ActionErrorDTO(1, "monthText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
				}
				if(CherryChecker.isNullOrEmpty(map.get("dayText"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "dayText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00033") })) ;
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("dayText"))){
					actionErrorList.add(new ActionErrorDTO(1, "dayText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00033") })) ;
					isCorrect = false;
				}
			}
			if("1".equals("dayFlag")){
				if(CherryChecker.isNullOrEmpty(map.get("monthEveText"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "monthEveText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("monthEveText"))){
					actionErrorList.add(new ActionErrorDTO(1, "monthEveText", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
				}
			}
		}
	}
	
	/**
	 * 介绍人模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000025_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if("0".equals("giveFlag") && CherryChecker.isNullOrEmpty(map.get("beforeBill"), true)){
				actionErrorList.add(new ActionErrorDTO(1, "beforeBill", "ECM00009", new String[] { PropertiesUtil.getText("PMB00040") })) ;
				isCorrect = false;
			}
		}
	}
	/**
	 * 退货模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000018_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if("0".equals(map.get("retGoodsFlag"))){
				if(CherryChecker.isNullOrEmpty(map.get("marks"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "marks", "ECM00009", new String[] { PropertiesUtil.getText("PMB00025") })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isFloatValid((String) map.get("marks"), 10, 2)){
					actionErrorList.add(new ActionErrorDTO(1, "marks", "ECM00024", new String[] { PropertiesUtil.getText("PMB00025"), "10", "2" })) ;
					isCorrect = false;
				}
			}
			if("1".equals(map.get("retGoodsFlag"))){
				if(CherryChecker.isNullOrEmpty(map.get("multipleMark"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "multipleMark", "ECM00009", new String[] { PropertiesUtil.getText("PMB00041") })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isFloatValid((String) map.get("multipleMark"), 10, 2)){
					actionErrorList.add(new ActionErrorDTO(1, "multipleMark", "ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })) ;
					isCorrect = false;
				}
			}
		}
	}
	
	/**
	 * 积分清零模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000019_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(map != null){
			if("0".equals(map.get("pointClearFlag"))){
				if(CherryChecker.isNullOrEmpty(map.get("maxMonthUse"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthUse", "ECM00009", new String[] { PropertiesUtil.getText("PMB00042") })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("maxMonthUse"))){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthUse", "ECM00021", new String[] { PropertiesUtil.getText("PMB00042") })) ;
					isCorrect = false;
				}
				if(String.valueOf(map.get("maxMonthUse")).length() > 3){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthUse", "ECM00020", new String[] { PropertiesUtil.getText("PMB00042"), "3" })) ;
					isCorrect = false;
				}
			}
			if("1".equals(map.get("pointClearFlag"))){
				if(CherryChecker.isNullOrEmpty(map.get("maxMonthGet"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthGet", "ECM00009", new String[] { PropertiesUtil.getText("PMB00043") })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("maxMonthGet"))){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthGet", "ECM00021", new String[] { PropertiesUtil.getText("PMB00043") })) ;
					isCorrect = false;
				}
				if(((String) map.get("maxMonthGet")).length() > 3){
					actionErrorList.add(new ActionErrorDTO(1, "maxMonthGet", "ECM00020", new String[] { PropertiesUtil.getText("PMB00043"), "3" })) ;
					isCorrect = false;
				}
				boolean dayFlag = true; 
				if(CherryChecker.isNullOrEmpty(map.get("billStartMonth"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "billStartMonth", "ECM00009", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(CherryChecker.isNullOrEmpty(map.get("billStartDay"),true)){
					actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00009", new String[] { PropertiesUtil.getText("PMB00035") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("billStartMonth"))){
					actionErrorList.add(new ActionErrorDTO(1, "billStartMonth", "ECM00021", new String[] { PropertiesUtil.getText("PMB00034") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(!CherryChecker.isNumeric((String) map.get("billStartDay"))){
					actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00021", new String[] { PropertiesUtil.getText("PMB00035") })) ;
					isCorrect = false;
					dayFlag = false;
				}
				if(dayFlag){
					isCorrect = checkDay(Integer.parseInt((String) map.get("billStartMonth")), Integer.parseInt((String) map.get("billStartDay")));
				}
			
			}
		}
	}
	
	/**
	 * 升级积分模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000026_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			// 升级积分大模块中的小模块集合
			List<Map<String, Object>> memberList = (List<Map<String, Object>>) map.get("combTemps");
			for(int i = memberList.size() - 1;i > 0;i--){
				Map<String, Object> comMap = memberList.get(i);
				String from = (String) comMap.get("memberformLevelId");
				String to = (String) comMap.get("membertoLevelId");
				for(int j = i-1;j >= 0;j--){
					Map<String, Object> compareMap = memberList.get(j);
					if(from.equals(compareMap.get("memberformLevelId")) && to.equals(compareMap.get("membertoLevelId"))){
						actionErrorList.add(new ActionErrorDTO(1, "membertoLevelId-" + i, "ECM00032", new String[] { PropertiesUtil.getText("PMB00032") })) ;
						isCorrect = false;
					}
				}
			}
		}
	}
	
	/**
	 * 升级积分赠与模板
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000040_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			int fromGrade = Integer.parseInt((String) map.get("memberformLevelId_grade"));
			int toGrade = Integer.parseInt((String) map.get("membertoLevelId_grade"));
			if(fromGrade >= toGrade){
				actionErrorList.add(new ActionErrorDTO(1, "membertoLevelId-" + BUS000040_INDEX, "ECM00027", new String[] { PropertiesUtil.getText("PMB00044"),PropertiesUtil.getText("PMB00045") })) ;
				isCorrect = false;
			}
		}
		BUS000040_INDEX++;
	}
	
	/**
	 * 购买产品积分上限模块 
	 * 
	 * @param map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000046_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			if(null != map.get("buyProLimit") && "1".equals(map.get("buyProLimit"))){
				if(CherryChecker.isNullOrEmpty(map.get("pointPro"), true)){
					actionErrorList.add(new ActionErrorDTO(1, "pointPro", "ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })) ; 
					isCorrect = false;
				}
				if(!CherryChecker.isFloatValid((String) map.get("pointPro"), 12, 4)){
					actionErrorList.add(new ActionErrorDTO(1, "pointPro", "ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "12", "4" })) ; 
					isCorrect = false;
				}
			}
		}
	}
	
	private boolean checkDay(int month, int day){
		boolean rightFlag = true;
		int monthArr[] = {1,3,5,7,8,10,12};
		if(month < 0 && month > 12){
			actionErrorList.add(new ActionErrorDTO(1, "billStartMonth", "ECM00052", new String[] { PropertiesUtil.getText("PMB00034"),"12" })) ;
			rightFlag = false;
		}
		if(day < 0){
			actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00027", new String[] { PropertiesUtil.getText("PMB00035"),"0" })) ;
			rightFlag = false;
		}
		if(month == 2){
			if(day > 28){
				actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00052", new String[] { PropertiesUtil.getText("PMB00035"),"28" })) ;
				rightFlag = false;
			}
		}else{
			boolean monthFlag = false;
			for(int i = 0;i < monthArr.length;i++){
				if(month == monthArr[i]){
					monthFlag = true;
					break;
				}
			}
			if(monthFlag && day > 31){
				actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00052", new String[] {  PropertiesUtil.getText("PMB00035"),"31"  })) ;
				rightFlag = false;
			}
			if(!monthFlag && day > 30){
				actionErrorList.add(new ActionErrorDTO(1, "billStartDay", "ECM00052", new String[] {  PropertiesUtil.getText("PMB00035"),"30"  })) ;
				rightFlag = false;
			}
		}
		
		return rightFlag;
	}
}