package com.cherry.webservice.member.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.MapBuilder;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.webservice.member.interfaces.TmUpdRegMemberMobleService_IF;
import com.cherry.webservice.member.service.TmUpdRegMemberMobleService;

public class TmUpdRegMemberMobleLogic implements TmUpdRegMemberMobleService_IF {

	/** 打印日志 */
	private Logger logger = LoggerFactory.getLogger(TmUpdRegMemberMobleLogic.class);

	@Resource(name = "tmUpdRegMemberMobleService")
	private TmUpdRegMemberMobleService tmUpdRegMemberMobleService;

	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;

	private Map<String, Object> commonMap = new HashMap<String, Object>();

	@Override
	public Map tmUpdRegMemberMoble(Map map) {
		logger.info("TmUpdRegMemberMobleLogic请求参数为：" + map);
		// 返回值
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 校验参数
		resultMap = validateParam(map);
		if (null != resultMap && !resultMap.isEmpty()) {
			return resultMap;
		}
		try {
			Map<String, Object> resultContentMap = processMemberInfo(map);
			if(null != resultContentMap && !resultContentMap.isEmpty()){
				resultMap.put("ResultContent", resultContentMap);
			}
		} catch (Exception e) {
			logger.error("更新线上会员手机号WS获取失败,相关参数为：" + map + ",异常信息为："+ ExceptionUtils.getStackTrace(e));
			logger.error("WS ERROR TradeType:"+ ConvertUtil.getString(map.get("TradeType")));
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
		}
		return resultMap;
	}

	/**
	 * 处理会员的基本信息操作
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> processMemberInfo(Map map) throws Exception {
		Map<String, Object> paramMap = MapBuilder.newInstance().put("tmallMixMobile", map.get("MobileEncryption")).build();
		paramMap.putAll(commonParam(map));
		Map<String, Object> onlineMemberMap = tmUpdRegMemberMobleService.getOnlineMemberInfo(paramMap);
		Map onlineMemberResultMap = processOnlineMemberBusiness(map,onlineMemberMap);
		if(null != onlineMemberResultMap && !onlineMemberResultMap.isEmpty()){
			return onlineMemberResultMap;
		}
		Map lineMemberMap = processLineMemberBusiness(map,paramMap,onlineMemberMap);
		if(null != lineMemberMap && !lineMemberMap.isEmpty()){
			return lineMemberMap;
		}
		return null;
	}
	/**
	 * 处理线上会员信息逻辑
	 * @param map
	 * @param onlineMap
	 * @return
	 * @throws Exception
	 */
	private Map processOnlineMemberBusiness(Map map,Map onlineMemberMap) throws Exception{
//		Map<String, Object> onlineMemberMap = tmUpdRegMemberMobleService.getOnlineMemberInfo(onlineMap);
		if (null != onlineMemberMap && !onlineMemberMap.isEmpty()) {
			// 比较线上会员天猫Nick和传输参数天猫Nick是否一致
			if ("2".equals(onlineMemberMap.get("BindFlag"))) {
				if (!map.get("NickName").equals(onlineMemberMap.get("BuyerNick"))) {
					return MapBuilder.newInstance().put("ERRORCODE", "EMB06006").put("ERRORMSG", "该会员已被使用").build();
				}
			}
			// 更新线上会员信息
			Map paramMap = MapBuilder.newInstance().put("tmallMixMobile", map.get("MobileEncryption"))
			.put("mobile",CherrySecret.encryptData(ConvertUtil.getString(map.get("BrandCode")),ConvertUtil.getString(map.get("Mobile")))).build();
			paramMap.putAll(commonParam(map));
			tmUpdRegMemberMobleService.updateOnineMobileByTmallMixMobile(paramMap);
		} else {
			logger.error("该会员在线上会员信息表不存在，相关会员参数加密手机号为："+map.get("MobileEncryption")+",明文手机号为："+map.get("Mobile")+",淘宝昵称为："+map.get("NickName"));
			return MapBuilder.newInstance().put("ERRORCODE", "EMB06007").put("ERRORMSG", "未查询到该会员信息").build();
		}
		return null;
	}
	/**
	 * 处理线下会员信息逻辑
	 * @param map
	 * @param lineMap
	 * @return
	 * @throws Exception
	 */
	private Map processLineMemberBusiness(Map map,Map lineMap,Map onlineMemberMap) throws Exception{
		Map<String, Object> memberMap = tmUpdRegMemberMobleService.getMemberInfo(lineMap);
		String memberId = "";
		String sourceFlag = "";
		if ((null == memberMap || memberMap.isEmpty())) {
			// 新增线上会员信息
			memberId = binOLMBMBM11_BL.tran_addMemberInfo(assembleMemberInfo(map));
			sourceFlag = "2";
		} else {
			// 获得已有会员信息的ID
			memberId = ConvertUtil.getString(memberMap.get("memberId"));
			// 比较线上会员天猫Nick和传输参数天猫Nick是否一致
			if (!map.get("NickName").equals(memberMap.get("TaobaoNick")) && "2".equals(memberMap.get("BindFlag"))) {
				return MapBuilder.newInstance().put("ERRORCODE", "EMB06006").put("ERRORMSG", "该会员已被使用").build();
			}
		}
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		if (!"2".equals(onlineMemberMap.get("BindFlag"))) {
			memberInfoMap = MapBuilder.newInstance().put("tmallMixMobile", map.get("MobileEncryption")).put("taobaonick", "").put("mixBuyerNick","")
					.put("mobile",CherrySecret.encryptData(ConvertUtil.getString(map.get("BrandCode")),ConvertUtil.getString(map.get("Mobile"))))
					.put("tmallBindTime","").put("memberId", memberId).put("sourceFlag", sourceFlag)
					.put("bindFlag", onlineMemberMap.get("BindFlag")).put("isSameMobile", "1").build();
		} else {
			memberInfoMap = MapBuilder.newInstance().put("tmallMixMobile", map.get("MobileEncryption")).put("taobaonick", map.get("NickName"))
					.put("mixBuyerNick",DESPlus.md5Encrypt(ConvertUtil.getString(map.get("NickName")),CherryConstants.TMALLENCRYPT))
					.put("mobile",CherrySecret.encryptData(ConvertUtil.getString(map.get("BrandCode")),ConvertUtil.getString(map.get("Mobile"))))
					.put("tmallBindTime", map.get("CreateTime")).put("memberId", memberId).put("sourceFlag", sourceFlag)
					.put("bindFlag", onlineMemberMap.get("BindFlag")).put("isSameMobile", "1").build();
		}
		// 更信息线下会员信息
		memberInfoMap.putAll(commonParam(map));
		tmUpdRegMemberMobleService.updateMemberInfoByTmMixMobile(memberInfoMap);
		return null;
	}

	/**
	 * 组装新增的会员信息
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> assembleMemberInfo(Map map) throws Exception {
		Map<String, Object> assembleMemberMap = MapBuilder.newInstance().put("brandCode", map.get("BrandCode"))
		.put("memCode", map.get("Mobile")).put("memName", map.get("NickName")).put("gender", "")
		.put("telephone", "").put("mobilePhone",CherrySecret.encryptData(ConvertUtil.getString(map.get("BrandCode")),ConvertUtil.getString(map.get("Mobile"))))
		.put("email", "").put("messageId", "").put("birthDay", "").put("joinDate", map.get("CreateTime")).put("joinTime", "")
		.put("provinceCode", "").put("province", "").put("cityCode", "").put("city", "").put("countyCode", "").put("county", "")
		.put("address", "").put("postcode", "").put("employeeCode", "").put("CounterCode", "MF0TMALL").put("memLevel", "")
		.put("memAgeGetMethod", "").put("memo1", "").put("channelCode", "TB").put("active", "").put("activeDate", "")
		.put("activeChannel", "").put("recommenderMember", "").put("memberPassword", "").put("dataSource1", "").put("dataSource2", "")
		.put("dataSource3", "").put("memo3", "").put("importMode", "1").put("profession", "").put("interests", "").build();
		assembleMemberMap.putAll(commonParam(map));
		return assembleMemberMap;
	}
	/**
	 * 共通参数
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> commonParam(Map map) {
		commonMap = MapBuilder.newInstance().put("organizationInfoId", map.get("BIN_OrganizationInfoID"))
		.put("brandInfoId", map.get("BIN_BrandInfoID")).put("orgCode", map.get("OrgCode")).put("brandCode", map.get("BrandCode"))
		.put("createdBy", "Cherry").put("createPGM", "TmUpdRegMemberMobleLogic")
		.put("updatedBy", "Cherry").put("updatePGM", "TmUpdRegMemberMobleLogic").build();
		return commonMap;
	}
	/**
	 * 校验参数
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> validateParam(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 判断必填字段
		String nickName = ConvertUtil.getString(map.get("NickName")).trim();
		if (ConvertUtil.isBlank(nickName)) {
			return MapBuilder.newInstance().put("ERRORCODE", "EMB06001").put("ERRORMSG", "天猫Nick不能为空").build();
		}
		String mobileEncryption = ConvertUtil.getString(map.get("MobileEncryption")).trim();
		if (ConvertUtil.isBlank(mobileEncryption)) {
			return MapBuilder.newInstance().put("ERRORCODE", "EMB06002").put("ERRORMSG", "加密手机号不为空").build();
		}
		String createTime = ConvertUtil.getString(map.get("CreateTime"));
		if (ConvertUtil.isBlank(createTime)) {
			return MapBuilder.newInstance().put("ERRORCODE", "EMB06003").put("ERRORMSG", "创建时间不为空").build();
		}
		String mobile = ConvertUtil.getString(map.get("Mobile"));
		if (ConvertUtil.isBlank(mobile)) {
			return MapBuilder.newInstance().put("ERRORCODE", "EMB06004").put("ERRORMSG", "明文手机号不为空").build();
		}
		return resultMap;
	}
}
