package com.cherry.mq.mes.interfaces;

import java.util.Map;

/**
 * 解析消息数据
 * @author huzude
 *
 */
public interface AnalyzeMessage_IF extends BaseMessage_IF{
	
	/**
	 * 对销售/退货数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeSaleData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对销售/退货数据进行库存处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeSaleStockData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对入库/退库数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeStockData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对调入申请单数据进行处理
	 * @param map
	 */
	public void analyzeAllocationInData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对调出确认单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeAllocationOutData(Map<String,Object> map) throws Exception;
	
    /**
     * 对调入确认单数据进行处理
     * @param map
     */
    public void analyzeAllocationInConfirmData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对盘点单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeStockTakingData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对生日礼领用单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeStockBirPresentData(Map<String,Object> map) throws Exception;
	
	/**
	 * 对产品订货单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	public void analyzeProductOrderData(Map<String,Object> map) throws Exception;
	
	/**
     * 对金蝶K3导入发货单/退库单数据进行处理
     * @param map
     * @throws Exception 
     */
    public void analyzeDeliverData(Map<String,Object> map) throws Exception;
    
    /**
     * 对移库单数据进行处理
     * @param map
     * @throws Exception 
     */
    public void analyzeShiftData(Map<String,Object> map) throws Exception;
    
    /**
     * 对退库申请单数据进行处理
     * @param map
     * @throws Exception 
     */
    public void analyzeReturnRequestData(Map<String,Object> map) throws Exception;
    
    /**
     * 对盘点申请单数据进行处理
     * @param map
     * @throws Exception 
     */
    public void analyzeStocktakeRequestData(Map<String,Object> map) throws Exception;
    
    /**
     * 对盘点确认数据进行处理
     * @param map
     * @throws Exception 
     */
    public void analyzeStocktakeConfirmData(Map<String,Object> map) throws Exception;

    /**
     * 对销售/退货数据进行库存处理(新消息体Type=0007)
     * @param map
     * @throws Exception 
     */
    public void analyzeSaleReturnStockData(Map<String,Object> map) throws Exception;
    
    /**
     * 对销售/退货数据进行处理(新消息体Type=0007)
     * @param map
     * @throws Exception 
     */
    public void analyzeSaleReturnData(Map<String,Object> map) throws Exception;
    
    /**
     * 对积分兑换（无需预约）进行处理（Type=0014）
     * @param map
     * @throws Exception
     */
    public void analyzePXData(Map<String,Object> map) throws Exception;
    
    /**
     * 对积分兑换（无需预约）数据进行库存处理（Type=0014）
     * @param map
     * @throws Exception 
     */
    public void analyzePXStockData(Map<String,Object> map) throws Exception;
    
    /**
     * 更改单据状态（Type=0017）
     * @param map
     * @throws Exception 
     */
    public void analyzeChangeSaleState(Map<String,Object> map) throws Exception;
}
