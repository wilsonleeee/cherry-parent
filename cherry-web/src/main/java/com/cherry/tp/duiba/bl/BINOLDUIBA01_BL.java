package com.cherry.tp.duiba.bl;


import com.cherry.bs.cnt.service.BINOLBSCNT08_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.tp.duiba.service.BINOLDUIBA01_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 兑吧BL
 */
public class BINOLDUIBA01_BL {

	private static final Logger logger = LoggerFactory.getLogger(BINOLDUIBA01_BL.class);


	@Resource(name = "binOLDUIBA01_Service")
	private BINOLDUIBA01_Service binOLDUIBA01_Service;

	private BaseService baseService;

	public Map getMemberInfoByOpenID(Map<String,Object> map){
		return binOLDUIBA01_Service.getMemberInfoByOpenID(map);
	}
	public Map getBIN_ExchangeRequest(Map<String,Object> map){

		return binOLDUIBA01_Service.getBIN_ExchangeRequest(map);
	}

	/**
	 * 更新兑换请求记录表
	 * @param map
	 * @return
	 */
	public int updateExchangeRequest(Map<String,Object> map){
		return binOLDUIBA01_Service.updateExchangeRequest(map);
	}

	public String getSYSDateTime(){
		baseService = new BaseService();
		return baseService.getSYSDateTime();
	}
	/**
	 * 插入兑吧兑换请求记录表
	 * @param map
	 * @throws Exception
	 */
	public void insertDuiBaExchangeRequest(Map<String, Object> map){
		binOLDUIBA01_Service.insertDuiBaExchangeRequest(map);
	}



}
