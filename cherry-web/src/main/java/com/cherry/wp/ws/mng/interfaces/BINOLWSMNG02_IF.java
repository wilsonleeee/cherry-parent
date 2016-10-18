package com.cherry.wp.ws.mng.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWSMNG02_IF {
	
    /**
     * 取得入库/收货单总数
     * 
     * @param map
     * @return 
     */
    public int getPrtGRRDCount(Map<String,Object> map);
    
    /**
     * 取得入库/收货单list
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtGRRDList(Map<String,Object> map);
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
    
    /**
     * 获取导出Excel
     * @param map
     * @return
     * @throws Exception
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception; 

    public int getDeliverCount(Map<String, Object> map);
	    
    public List<Map<String, Object>> getDeliverList(Map<String, Object> map);
    
    public void tran_doaction(Map<String,Object> sessionMap,Map<String,Object> billInformation) throws Exception;
}
