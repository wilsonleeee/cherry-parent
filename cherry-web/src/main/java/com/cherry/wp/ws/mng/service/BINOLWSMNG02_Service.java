package com.cherry.wp.ws.mng.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWSMNG02_Service extends BaseService {
	
    /**
     * 取得入库/收货单总数
     * 
     * @param map
     * @return 
     */
    public int getPrtGRRDCount(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getPrtGRRDCount");
        return (Integer) baseServiceImpl.get(map);
    }
    
    /**
     * 取得入库/收货单list
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtGRRDList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getPrtGRRDList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 入库/收货汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getSumInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 获取产品入库/收货单记录明细总数
     * @param map
     * @return
     */
    public int getExportDetailCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getExportDetailCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得产品入库/收货单详细导出List
     * @param map
     * @return
     */
    public List<Map<String, Object>> getPrtInDepotExportList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG02.getPrtInDepotExportList");
        return baseServiceImpl.getList(parameterMap);
    }
	
    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    public int getDeliverCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLWSMNG02.getDeliverCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLWSMNG02.getDeliverList");
        return baseServiceImpl.getList(map);
    }
}
