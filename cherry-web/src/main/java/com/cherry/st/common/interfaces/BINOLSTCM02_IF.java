package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTCM02_IF {
    /**
     * 将订单信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProductOrderAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改订单主表数据。
     * @param praMap
     * @return
     */
    public int updateProductOrderMain(Map<String,Object> praMap);
    
    /**
     * 更新订单明细表的TotalCostPrice
     * @param list
     * @return
     */
    public void updateProductOrderDetail(List<Map<String, Object>> list);
    
    /**
     * 给订货单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProductOrderMainData(int productOrderID,String language);
    
    /**
     * 给定订单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProductOrderDetailData(int productOrderID,String language,Map<String,Object> otherParam);
    
    /**
     * 通过判断工作流文件里action=1的bean.name来得知工作流FN是否是proFlowOD_YT_FN
     * @param workFlowName
     * @return
     */
    public boolean isProFlowOD_YT_FN(String workFlowName);
    
    /**
     * 通过判断工作流文件里action=1的参数brandCode来得知工作流文件是否属于某个品牌
     * @param workFlowName
     * @param brandCodes
     * @return
     */
    public boolean checkWorkFlowBrand(String workFlowName,String... brandCodes);
}
