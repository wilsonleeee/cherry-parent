package com.cherry.mq.mes.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 竞争对手日销售消息数据接收处理Service
 * @author zhhuyi
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES06_Service extends BaseService{
	
	/**
	 * 查询竞争对手ID
	 * @param map
	 * @return
	 */
	public Map selRivalID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES06.selRivalID");
		return (Map) baseServiceImpl.get(map);
	}
	
    /**
     * 插入竞争对手
     * @param map
     * @return 
     * */
	public int addRival(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES06.addRival");
		return baseServiceImpl.saveBackId(map);
	}
	/**
	 * 插入竞争对手日销售数据
	 * @param detailDataList
	 * 
	 * */
	public void addRivalDaySale(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES06.addRivalDaySale");	
	}
	/**
	 * 删除已存在的竞争对手日销售信息
	 * @param map
	 * 
	 * */
	public int delOldRivalDaySale(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES06.delOldRivalDaySale");
		return baseServiceImpl.remove(map);
	}
}
