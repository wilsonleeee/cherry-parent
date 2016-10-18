package com.cherry.webservice.unique.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.unique.service.UniqueCodeUpdateService;


/**
 * 保存扫码关注信息 接口BL
 * 
 * @author zhouwei
 * @version 2016-06-12 1.0.0
 */
public class UniqueCodeUpdateLogic {

	private static Logger logger = LoggerFactory.getLogger(UniqueCodeUpdateLogic.class.getName());
	
	@Resource
	private UniqueCodeUpdateService uniqueCodeUpdateService;

//	/** 回执Map **/
//	private Map<String, Object> resultMap = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
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
			Map<String, Object> pointUniqueCodeMap = uniqueCodeUpdateService.getPrtUnique(map) ;
			 // 未激活或者已使用用的积分唯一码无法使用
			if( pointUniqueCodeMap == null || ConvertUtil.isBlank(ConvertUtil.getString(pointUniqueCodeMap.get("BIN_PrtUniqueCodeDetailID")))){
				 resultMap.put("ERRORCODE", "WSE0088");
				 resultMap.put("ERRORMSG", "对不起，该唯一码不存在，请重新输入。");
				 return resultMap;
			 }else if("1".equals(ConvertUtil.getString(pointUniqueCodeMap.get("UseStatus")))){
				 resultMap.put("ERRORCODE", "WSE0089");
				 resultMap.put("ERRORMSG", "对不起，该唯一码已被使用，每个唯一码仅限使用一次。");
				 return resultMap;
			 }else if("0".equals(ConvertUtil.getString(pointUniqueCodeMap.get("ActivationStatus")))){
				 resultMap.put("ERRORCODE", "WSE0090");
				 resultMap.put("ERRORMSG", "积分唯一码未激活，请激活后再使用");
				 return resultMap;
			 }
			uniqueCodeUpdateService.updPrtUniqueInfo(map);
//			resultMap.put("ERRORCODE", "0");
//			resultMap.put("ERRORMSG", "OK");
			logger.info("更新新后台的唯一码使用状态WebService调用成功，积分唯一码PointUniqueCode="+PointUniqueCode);
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
			resultMap.put("ERRORMSG", "参数brandCode不能为空");
			return resultMap;
		}
		// 积分唯一码
		if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("PointUniqueCode")))) {
			resultMap.put("ERRORCODE", "WSE0063");
			resultMap.put("ERRORMSG", "参数pointUniqueCode不能为空");
			return resultMap;
		}
		// 业务类型
		String TradeType=ConvertUtil.getString(map.get("TradeType"));
		if(ConvertUtil.isBlank(TradeType) ||(null != TradeType && !"UpdPrtUniqueInfo".equals(TradeType))) {
			resultMap.put("ERRORCODE", "WSE9994");
			resultMap.put("ERRORMSG", "参数TradeType错误,TradeType=" + TradeType);
			return resultMap;
		}	
		
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "UniqueCodeUpdateLogic");
		// 更新者
		map.put(CherryConstants.UPDATEDBY,"UniqueCodeUpdateLogic");

		return resultMap;
	}

}
