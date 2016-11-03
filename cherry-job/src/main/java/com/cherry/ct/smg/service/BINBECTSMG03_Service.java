package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBECTSMG03_Service extends BaseService{
    /**
	 * 增加短信沟通明细信息
	 * 
	 * @param map
	 * @return
	 */
	public void addSmsSendDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG03.addSmsSendDetail");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 记录客户沟通日志
	 * 
	 * @param map
	 * @return
	 */
	public void addCommunicationLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG03.addCommunicationLog");
		baseServiceImpl.save(map);
	}
	
	/**
     * 查找调度最后运行时间
     * 
     * @param map
     * @return
     * 		调度最后运行时间
     */
    public String getSmsSendTable(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG03.getSmsSendTable");
        return (String) smsServiceImpl.get(paramMap);
    }
    
    /**
	 * 向短信接口写入信息
	 * 
	 * @param map
	 * @return
	 */
	public void addMsgtoSmsInterface(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG03.addMsgtoSmsInterface");
		smsServiceImpl.save(map);
	}
	
	/**
	 * 记录客户沟通日志
	 * 
	 * @param map
	 * @return
	 */
	public void addErrorMsgLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG03.addErrorMsgLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 记录沟通程序生成的Coupon信息
	 * 
	 * @param map
	 * @return
	 */
	public void addCouponCreateLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG03.addCouponCreateLog");
		baseServiceImpl.save(map);
	}
}
