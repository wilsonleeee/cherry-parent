package com.cherry.ss.prm.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.ss.prm.dto.CouponEngineDTO;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.googlecode.jsonplugin.JSONUtil;

public class CouponEngine implements InitializingBean{
	
	private static Logger logger = LoggerFactory
			.getLogger(CouponEngine.class.getName());
	
	/** 优惠券规则Service */
	@Resource
	private BINOLSSPRM73_Service binOLSSPRM73_Service;
	
	/** 规则执行session的集合 */
	private Map<String, Object> ksessionMap;
	
	public CouponEngine() {
		this.ksessionMap = new HashMap<String, Object>();
	}
	
	public List<CouponEngineDTO> getRuleList(String orgCode, String brandCode) {
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		Map<String, Object> resultMap = (Map<String, Object>) ksessionMap.get(key);
		if (null != resultMap) {
			return (List<CouponEngineDTO>) resultMap.get("ruleList");
		}
		return null;
	}
	
	public CouponEngineDTO getRule(String orgCode, String brandCode, String ruleCode) {
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		Map<String, Object> resultMap = (Map<String, Object>) ksessionMap.get(key);
		if (null != resultMap) {
			Map<String, Object> ruleMap = (Map<String, Object>) resultMap.get("ruleMap");
			if (null != ruleMap) {
				return (CouponEngineDTO) ruleMap.get(ruleCode);
			}
		}
		return null;
	}
	
	public synchronized void reloadAllRule(String orgCode, String brandCode) throws Exception {
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		// 加载规则
		Map<String, Object> ruleMap = loadRulesDB(orgCode, brandCode);
		if (null != ruleMap) {
			ksessionMap.put(key, ruleMap);
		} else {
			ksessionMap.remove(key);
		}
	}
	
	/**
	 * 刷新单个规则文件
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @throws Exception 
	 * 
	 */
	public synchronized void refreshRule(String ruleCode) throws Exception {
		Map<String, Object> ruleMap = binOLSSPRM73_Service.getCouponRuleExecInfo(ruleCode);
		CouponEngineDTO couponEngineDTO = null;
		if (null != ruleMap && !ruleMap.isEmpty()) {
			couponEngineDTO = getRuleEngine(ruleMap);
		}
		if (null == couponEngineDTO) {
			return;
		}
		String orgCode = (String) ruleMap.get("orgCode");
		String brandCode = (String) ruleMap.get("brandCode");
		CouponEngineDTO ruleEngine = getRule(orgCode, brandCode, ruleCode);
		removeRule(ruleCode, orgCode, brandCode);
		addRule(couponEngineDTO, orgCode, brandCode);
	}
	
	public synchronized void removeRule(String ruleCode, String orgCode, String brandCode) throws Exception {
		CouponEngineDTO ruleEngine = getRule(orgCode, brandCode, ruleCode);
		if (null != ruleEngine) {
			removeRule(ruleEngine, orgCode, brandCode);
		}
	}
	
	private void removeRule(CouponEngineDTO ruleEngine, String orgCode, String brandCode) {
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		Map<String, Object> resultMap = (Map<String, Object>) ksessionMap.get(key);
		if (null == resultMap) {
			return;
		}
		List<CouponEngineDTO> ruleList = (List<CouponEngineDTO>) resultMap.get("ruleList");
		if (null == ruleList || ruleList.isEmpty()) {
			return;
		}
		Map<String, Object> ruleMap = (Map<String, Object>) resultMap.get("ruleMap");
		ruleMap.remove(ruleEngine);
		ruleList.remove(ruleEngine);
	}
	
	private void addRule(CouponEngineDTO ruleEngine, String orgCode, String brandCode) {
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		Map<String, Object> resultMap = (Map<String, Object>) ksessionMap.get(key);
		if (null == resultMap) {
			resultMap = new HashMap<String, Object>();
			ksessionMap.put(key, resultMap);
		}
		List<CouponEngineDTO> ruleList = (List<CouponEngineDTO>) resultMap.get("ruleList");
		Map<String, Object> ruleMap = (Map<String, Object>) resultMap.get("ruleMap");
		if (null == ruleList) {
			ruleList = new ArrayList<CouponEngineDTO>();
			resultMap.put("ruleList", ruleList);
		}
		if (null == ruleMap) {
			ruleMap = new HashMap<String, Object>();
			resultMap.put("ruleMap", ruleMap);
		}
		ruleList.add(ruleEngine);
		ruleMap.put(ruleEngine.getRuleCode(), ruleEngine);
	}
	
	/**
	 * 从数据库中读取所有规则文件
	 * 
	 * 
	 * @throws Exception
	 * 
	 */
	public void readKnowledgeBaseDB () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得配置数据库品牌List
		List<Map<String, Object>> confBrandList = binOLSSPRM73_Service.getConfBrandInfoList(map);
		
		// 保存当前进程中的datasource type,用于junit测试，因为在junit测试时在beforeclass设置的datasource type会被这个方法消除掉
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		
		// 品牌信息
		if (null != confBrandList) {
			for (Map<String, Object> brandInfo : confBrandList) {
				try {
					// 新后台品牌数据源
					String dataSource = (String) brandInfo.get("dataSourceName");
					if (CherryChecker.isNullOrEmpty(dataSource)) {
						continue;
					}
					CustomerContextHolder.setCustomerDataSourceType(dataSource);
					String orgCode = (String) brandInfo.get("orgCode");
					String brandCode = (String) brandInfo.get("brandCode");
					// 加载规则
					Map<String, Object> ruleMap = loadRulesDB(orgCode, brandCode);
					if (null != ruleMap) {
						String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
						ksessionMap.put(key, ruleMap);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				} finally {
					if (null != dataSourceType) {
						// 恢复保存的datasource type,使得junit测试能够顺利进行
						CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
					} else {
						String cusDateSource = CustomerContextHolder.getCustomerDataSourceType();
						if (null != cusDateSource) {
							// 清除新后台品牌数据源
							CustomerContextHolder.clearCustomerDataSourceType();
						}
					}
				}
			}
		}
	}
	
	private CouponEngineDTO getRuleEngine(Map<String, Object> ruleMap) throws Exception{
		CouponEngineDTO couponEngineDTO = new CouponEngineDTO();
		String ruleCode = String.valueOf(ruleMap.get("ruleCode"));
		couponEngineDTO.setRuleCode(ruleCode);
		couponEngineDTO.setRuleName(String.valueOf(ruleMap.get("ruleName")));
		couponEngineDTO.setSendStartTime(String.valueOf(ruleMap.get("sendStartTime")));
		couponEngineDTO.setSendEndTime(String.valueOf(ruleMap.get("sendEndTime")));
		couponEngineDTO.setSumQuantity(Integer.parseInt(String.valueOf(ruleMap.get("sumQuantity"))));
		couponEngineDTO.setLimitQuantity(Integer.parseInt(String.valueOf(ruleMap.get("limitQuantity"))));
		couponEngineDTO.setQuantity(Integer.parseInt(String.valueOf(ruleMap.get("quantity"))));
		couponEngineDTO.setValidMode(String.valueOf(ruleMap.get("validMode")));
		couponEngineDTO.setIsGive(String.valueOf(ruleMap.get("isGive")));
		couponEngineDTO.setVersion(Integer.parseInt(String.valueOf(ruleMap.get("modifyCount"))));;
		try {
			// 使用券时间JSON
			String useTimeJson = String.valueOf(ruleMap.get("useTimeJson"));
			if (!CherryChecker.isNullOrEmpty(useTimeJson)) {
				Map<String, Object> useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTimeJson);
				couponEngineDTO.setUseTimeInfo(useTimeInfo);
			}
		} catch (Exception e) {
			logger.error("活动码：" + ruleCode + "，使用券时间JSON转换失败：" + e.getMessage(),e);
			return null;
		}
		try {
			// 券内容
			String content = String.valueOf(ruleMap.get("content"));
			int couponNum = 1;
			if (!CherryChecker.isNullOrEmpty(content)) {
				List<Map<String, Object>> contentList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
				couponEngineDTO.setContentList(contentList);
				couponNum = contentList.size();
			}
			couponEngineDTO.setCouponNum(couponNum);
		} catch (Exception e) {
			logger.error("活动码：" + ruleCode + "，券内容JSON转换失败：" + e.getMessage(),e);
			return null;
		}
		try {
			// 发券门槛JSON
			String sendCond = String.valueOf(ruleMap.get("sendCond"));
			if (!CherryChecker.isNullOrEmpty(sendCond)) {
				Map<String, Object> sendCondInfo = (Map<String, Object>) JSONUtil.deserialize(sendCond);
				couponEngineDTO.setSendCondInfo(sendCondInfo);
			}
		} catch (Exception e) {
			logger.error("活动码：" + ruleCode + "，发券门槛JSON转换失败：" + e.getMessage(),e);
			return null;
		}
		try {
			// 使用门槛JSON
			String useCond = String.valueOf(ruleMap.get("useCond"));
			if (!CherryChecker.isNullOrEmpty(useCond)) {
				Map<String, Object> useCondInfo = (Map<String, Object>) JSONUtil.deserialize(useCond);
				couponEngineDTO.setUseCondInfo(useCondInfo);
			}
		} catch (Exception e) {
			logger.error("活动码：" + ruleCode + "，使用门槛JSON转换失败：" + e.getMessage(),e);
			return null;
		}
		return couponEngineDTO;
	}
	
	/**
	 * 从数据库中加载规则文件
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> loadRulesDB(String orgCode, String brandCode) throws Exception {
		Map<String, Object> brandMap = new HashMap<String, Object>();
		// 组织Code
		brandMap.put("orgCode", orgCode);
		// 品牌code
		brandMap.put("brandCode", brandCode);
		// 取得品牌信息
		Map<String, Object> osbrandInfo = binOLSSPRM73_Service.getOSBrandInfo(brandMap);
		if (null == osbrandInfo) {
			throw new Exception("coupon rule load fail, can not get the brand message!");
		}
		List<Map<String, Object>> ruleExecList = binOLSSPRM73_Service.getCouponRuleExecList(osbrandInfo);
		if (null != ruleExecList && !ruleExecList.isEmpty()) {
			List<CouponEngineDTO> ruleList = new ArrayList<CouponEngineDTO>();
			Map<String, Object> ruleVersion = new HashMap<String, Object>();
			for (Map<String, Object> ruleMap : ruleExecList) {
				CouponEngineDTO couponEngineDTO = getRuleEngine(ruleMap);
				if (null == couponEngineDTO) {
					continue;
				}
				ruleList.add(couponEngineDTO);
				ruleVersion.put(couponEngineDTO.getRuleCode(), couponEngineDTO);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("ruleList", ruleList);
			resultMap.put("ruleMap", ruleVersion);
			return resultMap;
		}
		return null;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		readKnowledgeBaseDB();
	}
}
