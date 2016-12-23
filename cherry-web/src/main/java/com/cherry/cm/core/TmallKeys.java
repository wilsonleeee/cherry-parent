package com.cherry.cm.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class TmallKeys implements InitializingBean{
	
	protected static final Logger logger = LoggerFactory.getLogger(TmallKeys.class);
	
	/** SqlIDkey */
	static final String IBATIS_SQL_ID = "ibatis_sql_id";
	
	@Resource
	private BaseConfServiceImpl baseConfServiceImpl;
	
	private static List<TmallKeyDTO> keyList;

	@Override
	public void afterPropertiesSet() throws Exception {
		loadAllKeys();
	}
	
	public List<TmallKeyDTO> getBrandKeys() {
		return keyList;
	}
	public static TmallKeyDTO getTmallKeyBybrandName(String brandName) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandName)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandName.trim().equals(tmallKey.getBrandName())) {
					return tmallKey;
				}
			}
		}
		return null;
	}
	
	public static TmallKeyDTO getTmallKeyBybrandCode(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey;
				}
			}
		}
		return null;
	}
	
	public static String getTmallCounters(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey.getTmallCounters();
				}
			}
		}
		return null;
	}
	
	public static String getNoExecCounters(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey.getNoExecCounts();
				}
			}
		}
		return null;
	}
	
	public static String getMixKey(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey.getMixKey();
				}
			}
		}
		return null;
	}
	
	public static boolean isNeedMerge(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return "1".equals(tmallKey.getMergeFlag());
				}
			}
		}
		return false;
	}
	
	public static String getMemberModel(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey.getMemberModel();
				}
			}
		}
		return null;
	}

	public static String getExtJson(String brandCode) {
		if (null != keyList && !CherryChecker.isNullOrEmpty(brandCode)) {
			for (TmallKeyDTO tmallKey : keyList) {
				if (brandCode.trim().equals(tmallKey.getBrandCode())) {
					return tmallKey.getExtJson();
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * 加载所有key
	 * </p>
	 * 
	 * 
	 */	
	private void loadAllKeys() throws Exception {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(IBATIS_SQL_ID, "TmallKeys.getTmallKeyList");
			// 取得天猫key列表
			keyList = baseConfServiceImpl.getList(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

}
