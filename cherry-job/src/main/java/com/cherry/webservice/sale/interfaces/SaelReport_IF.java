package com.cherry.webservice.sale.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 销售报表IF
 * 
 * @author WangCT
 * @version 1.0 2014.11.24
 */
public interface SaelReport_IF {
	
	/**
     * 业绩通报表(柜台)
     * 
     * @param map 查询条件
     * @return 业绩通报表(柜台)
     */
    public Map<String, Object> getCounterSaleAchievement(Map<String, Object> map);
       
    /**
     *柜台销售记录
     * 
     * @author ZhaoCF
     * @version 1.0 2015.04.16
     * 
     * @param map 查询条件
     * @return 柜台销售记录
     */
	public Map<String, Object> getCounterSaleRecord(Map<String, Object> map) ;

}
