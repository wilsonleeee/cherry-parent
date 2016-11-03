package com.cherry.webservice.counter.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.counter.service.CounterInfoService;

public class CounterInfoLogic implements IWebservice {	
	
    private static Logger logger = LoggerFactory.getLogger(CounterInfoLogic.class.getName());
    
    /**柜台信息查询**/
    @Resource(name = "counterInfoService")
    private CounterInfoService counterInfoService;  
    
    @Override
	public Map<String, Object> tran_execute(Map map) throws Exception  {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("BIN_OrganizationInfoID"));
			paramMap.put(CherryConstants.BRANDINFOID, map.get("BIN_BrandInfoID"));
			// 品牌CODE						
			paramMap.put("BrandCode", map.get("BrandCode"));
			// 柜台编号
			paramMap.put("CounterCode", map.get("CounterCode"));
			//业务验证
			String TradeType=ConvertUtil.getString(map.get("TradeType"));
			if(CherryBatchUtil.isBlankString(TradeType) ||(null != TradeType && !"GetCounter".equals(TradeType))) {
				retMap.put("ERRORCODE", "WSE9994");
				retMap.put("ERRORMSG", "参数TradeType错误,TradeType=" + TradeType);
				return retMap;
			}						
			resultList = counterInfoService.getCounterList(paramMap);						
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}		
		
//		if(CherryBatchUtil.isBlankList(resultList)){
//			retMap.put("ERRORCODE", "WSE0003");
//			retMap.put("ERRORMSG", "未找到指定的部门(柜台)");
//		}
		
		retMap.put("ResultContent", resultList);		
		return retMap;
	}
}
