package com.cherry.webservice.unique.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.unique.service.UniqueCodeSearchService;


/**
 * 唯一码查询 接口BL
 * 
 * @author zhouwei
 * @version 2016-06-12 1.0.0
 */
public class UniqueCodeSearchLogic {

	private static Logger logger = LoggerFactory.getLogger(UniqueCodeSearchLogic.class.getName());
	
	@Resource
	private UniqueCodeSearchService uniqueCodeSearchService;

	/** 回执 **/
	private Map<String, Object> returnParam = new HashMap<String, Object>();

	/** 回执Map **//*
	private Map<String, Object> resultMap = new HashMap<String, Object>();*/

	public Map<String, Object> tran_execute(Map map) throws Exception  {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 参数校验
			resultMap = veriFicationParam(map);
			// 积分唯一码
			String PointUniqueCode = ConvertUtil.getString(map.get("PointUniqueCode"));
			// 若参数没通过校验，返回带有参数错误的Map
			if (null != resultMap && !resultMap.isEmpty() && resultMap.containsKey("ERRORCODE")) {
				return resultMap;
			}
		    // 判断输入的唯一码在系统中是否存在
			 Map<String, Object> pointUniqueCodeMap = uniqueCodeSearchService.getPrtUnique(map) ;
			 if( pointUniqueCodeMap == null || ConvertUtil.isBlank(ConvertUtil.getString(pointUniqueCodeMap.get("BIN_PrtUniqueCodeDetailID")))){
				 resultMap.put("ERRORCODE", "WSE0088");
				 resultMap.put("ERRORMSG", "参数PointUniqueCode错误");
				 return resultMap;
			 }
			returnParam = uniqueCodeSearchService.getPrtUniqueInfo(map);
//			resultMap.put("ERRORCODE", "0");
//			resultMap.put("ERRORMSG", "OK");
			logger.info("查询唯一码WebService调用成功，积分唯一码PointUniqueCode="+PointUniqueCode);
			resultMap.put("ResultMap", returnParam);
			return resultMap;
	    } catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("ERRORCODE", "WSE9999");
			resultMap.put("ERRORMSG", "处理过程中发生未知异常");
			return resultMap;
		}			
		    
	 }
	/**
	 * 验证传输参数有效性
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> veriFicationParam(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 品牌代码
		if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("BrandCode")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "必填参数不能为空 ");
			return resultMap;
		}
		// 积分唯一码
		if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("PointUniqueCode")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "必填参数不能为空 ");
			return resultMap;
		}
		// 业务类型
		String TradeType=ConvertUtil.getString(map.get("TradeType"));
		if(ConvertUtil.isBlank(TradeType) ||(null != TradeType && !"GetPrtUniqueInfo".equals(TradeType))) {
			resultMap.put("ERRORCODE", "WSE9994");
			resultMap.put("ERRORMSG", "参数TradeType错误,TradeType=" + TradeType);
			return resultMap;
		}	
		
		return resultMap;
	}

	
}
