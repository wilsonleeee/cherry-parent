package com.cherry.cm.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cherry.cm.util.DateUtil;

/**
 * 
 * @ClassName: ThirdPartyConfig 
 * @Description: TODO(初始化加载penkonws项目的Webservice接入方配置) 
 * @author menghao
 * @version v1.0.0 2016-7-15 
 *
 */
public class ThirdPartyConfig implements InitializingBean{
	
	protected static final Logger logger = LoggerFactory.getLogger(ThirdPartyConfig.class);
	
	/** SqlIDkey */
	static final String IBATIS_SQL_ID = "ibatis_sql_id";
	
	@Resource
	private BaseConfServiceImpl baseConfServiceImpl;
	
	private static List<ThirdPartyConfigDTO> appList;

	@Override
	public void afterPropertiesSet() throws Exception {
		loadAllAppID();
	}
	
	public List<ThirdPartyConfigDTO> getThirdPartyConfig() {
		return appList;
	}
	
	/**
	 * appID在配置文件中以前缀的形式写死，在配置数据库中的appID是此前缀+'_'+brandCode
	 * appID在dbo.BIN_ThirdPartyConfig中有唯一索引
	 * @param appID
	 * @return
	 */
	public ThirdPartyConfigDTO getThirdPartyConfigByAppID(String appID) {
		if (null != appList && !CherryChecker.isNullOrEmpty(appID)) {
			for (ThirdPartyConfigDTO thirdPartyConfig : appList) {
				if (appID.trim().equals(thirdPartyConfig.getAppID())) {
					return thirdPartyConfig;
				}
			}
		}
		return null;
	}
	
	/**
	 * 此方法中的appID只是前缀，拼上品牌code才是真正的配置的appID(可支持多品牌配置)
	 * @param appID
	 * @param brandCode
	 * @return
	 */
	public String getDynamicAESKey(String appID, String brandCode) {
		String virtualAppID = appID + "_" + brandCode;
		String brandDynamicAESKey = getDynamicAESKey(virtualAppID);
		if("".equals(brandDynamicAESKey)) {
			// 当前品牌找不到则取默认的密钥
			return getDynamicAESKey(appID);
		}
		return brandDynamicAESKey;
	}
	
	/**
	 * 取得指定appID的密钥
	 * @param appID
	 * @return
	 */
	public String getDynamicAESKey(String appID) {
		// 检查Webservice接入方列表信息，如果不存在，则尝试刷新一次
		if(null == appList || appList.isEmpty()) {
			try {
				// Webservice接入方列表信息，尝试重新获取一次
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(IBATIS_SQL_ID, "ThirdPartyConfig.getThirdPartyConfig");
				// 取得penkonws项目的Webservice接入方列表
				appList = baseConfServiceImpl.getList(paramMap);
			} catch (Exception e) {
				logger.error("获取Webservice接入方列表信息出现异常：" + e.getMessage(),e);
				return "";
			}
		}
		if (null != appList && !CherryChecker.isNullOrEmpty(appID)) {
			boolean isExistsAppID = false;
			for (ThirdPartyConfigDTO thirdPartyConfig : appList) {
				if (appID.trim().equals(thirdPartyConfig.getAppID())) {
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
					String nowtime = format.format(calendar.getTime());
					// 密钥不能是过期的
					if(thirdPartyConfig.getaESKeyExpireTime().compareTo(nowtime) >= 0) {
						isExistsAppID = true;
						return thirdPartyConfig.getDynamicAESKey();
					}
				}
			}
			if(!isExistsAppID) {
				// 如果不存在的指定appID的密钥或者已过期,尝试重新获取一次
				try {
					// Webservice接入方列表信息，尝试重新获取一次
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put(IBATIS_SQL_ID, "ThirdPartyConfig.getThirdPartyConfig");
					// 取得penkonws项目的Webservice接入方列表
					appList = baseConfServiceImpl.getList(paramMap);
					if (null != appList && !CherryChecker.isNullOrEmpty(appID)) {
						for (ThirdPartyConfigDTO thirdPartyConfig : appList) {
							if (appID.trim().equals(thirdPartyConfig.getAppID())) {
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
								String nowtime = format.format(calendar.getTime());
								// 密钥不能是过期的
								if(thirdPartyConfig.getaESKeyExpireTime().compareTo(nowtime) >= 0) {
									return thirdPartyConfig.getDynamicAESKey();
								} else {
									logger.error("密钥无效或已过期");
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("获取Webservice接入方列表信息出现异常：" + e.getMessage(),e);
				}
			}
		} 
		return "";
	}
	
	/**
	 * <p>
	 * Webservice接入方列表
	 * </p>
	 * 
	 * 
	 */	
	private void loadAllAppID() throws Exception {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(IBATIS_SQL_ID, "ThirdPartyConfig.getThirdPartyConfig");
			// 取得penkonws项目的Webservice接入方列表
			appList = baseConfServiceImpl.getList(paramMap);
		} catch (Exception e) {
			logger.error("获取Webservice接入方列表信息出现异常：" + e.getMessage(),e);
			throw e;
		}
	}

}
