package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * @ClassName: BINOLSTCM20_IF 
 * @Description: TODO(大仓加权平均成本价格表操作共通Interface) 
 * @author menghao
 * @version v1.0.0 2015-10-8 
 *
 */
public interface BINOLSTCM20_IF extends ICherryInterface{
	
	/**
	 * 根据当前入库单获取大仓加权平均成本价格表相关信息（计算加权平均价）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductWeightedAvgInfo(Map<String, Object> map);
    
    /**
     * 写入大仓加权平均成本价格表
     * @param mainData
     * @param detailList
     * @return 入出库主表的自增ID
     */
    public void insertWeightedAvgPrice(List<Map<String,Object>> list);
    
    /**
     * 根据入库主单ID做大仓产品加权平均价的相关处理
     * @param map
     */
    public void handleProductWeightedAvgPriceByIndepotBill(Map<String, Object> map);
    
}