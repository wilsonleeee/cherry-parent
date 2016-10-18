package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTCM03_IF {
    /**
     * 将发货单信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProductDeliverAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改发货单据主表数据。
     * @param praMap
     * @return
     */
    public int updateProductDeliverMain(Map<String,Object> praMap);
    
    /**
     * 给发货单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProductDeliverMainData(int productDeliverID,String language);
    
    /**
     * 给定发货单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProductDeliverDetailData(int productDeliverID,String language,Map<String, Object> otherParam);
    
    /**
     * 根据订单的ID来自动生成发货单。
     * @param praMap
     * @return
     */
    public int createProductDeliverByOrder(Map<String,Object> praMap);
    
    /**
     * 根据发货单ID来自动生成出入库单
     * @param praMap
     * @return
     */
    public int createProductInOutByDeliverID(Map<String,Object> praMap);
    
    /**
     * 根据订货单更新发货单
     * @param praMap
     */
    @Deprecated
    public void updateProductDeliverByOrder(Map<String,Object> praMap);
    
    /**
     * 更新入出库单
     * @param praMap
     * @return
     */
    public int updateProductInOut(Map<String,Object> praMap);
    
    /**
     * 根据订单Form来生成主从发货单。
     * @param praMap
     * @return
     */
    public int createProductDeliverByForm(Map<String,Object> praMap);
    
    /**
     * 根据发货单Form来更新主从发货单。
     * @param praMap
     * @return
     */
    public void updateProductDeliverByForm(Map<String,Object> praMap);
    
    /**
     * 判断发货单号是否存在
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> selPrtDeliverList(Map<String,Object> praMap);
}
