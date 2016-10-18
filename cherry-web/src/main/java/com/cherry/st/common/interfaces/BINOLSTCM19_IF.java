package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTCM19_IF {
	/**
     * 插入销售记录
     * @param mainData
     * @param detailList
     * @return
     */
	public int insertSaleData(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	/**
     * 更新销售记录
     * @param saleData
     * @param 
     * @return
     */
	public void updateSaleData(Map<String,Object> saleData);
	
	/**
     * 删除销售记录（逻辑删除）
     * @param paramMap
     * @param 
     * @return
     */
	public int deleteSaleDataLogic(Map<String,Object> paramMap);
	
    /**
     * 给后台销售单主ID，取得概要信息。
     * @param backstageSaleID
     * @return
     */
    public Map<String,Object> getBackstageSaleMainData(int backstageSaleID,String language);
    
    /**
     * 给后台销售单主ID，取得明细信息。
     * @param backstageSaleID
     * @return
     */
    public List<Map<String,Object>> getBackstageSaleDetailData(int backstageSaleID,String language);

    /**
     * 修改后台销售主表数据。
     * @param praMap
     * @return
     */
    public int updateBackstageSale(Map<String,Object> praMap);
    
    /**
     * 根据后台销售单ID来自动生成出入库单，并修改库存
     * @param praMap
     * @return
     */
    public int createProductInOutByBackstageSaleID(Map<String,Object> praMap);
    
    /**
     * 插入后台销售业务数据主表、从表（履历）
     * @param paramMap map里必须有BIN_BackstageSaleID,BIN_EmployeeID
     * @return
     */
    public int insertSaleDataHistory(Map<String,Object> paramMap);
    
    /**
     * 给后台销售单（履历）主ID，取得概要信息。
     * @param backstageSaleHistoryID
     * @return
     */
    public Map<String,Object> getBackstageSaleHistoryMainData(long backstageSaleHistoryID,String language);
    
    /**
     * 给后台销售单（履历）主ID，取得明细信息。
     * @param backstageSaleHistoryID
     * @return
     */
    public List<Map<String,Object>> getBackstageSaleDetailHistoryData(long backstageSaleHistoryID,String language);
    
    /**
     * 根据后台销售单据ID取得该后台销售单据的所有履历
     * @param backstageSaleHistoryID
     * @return
     */
    public List<Map<String,Object>> getAllBackstageSaleHistory(int backstageSaleID,String language);

    /**
     * 修改后台销售主表（履历）数据。
     * @param praMap
     * @return
     */
    public int updateBackstageSaleHistory(Map<String,Object> praMap);
    
    /**
     * 修改组织结构表联系人和联系地址。
     * @param praMap
     * @return
     */
    public void updateOrganizationInfo(Map<String,Object> praMap);
    
    /**
     * 修改往来单位联系人和联系地址。
     * @param praMap
     * @return
     */
    public void updateBussinessPartnerInfo(Map<String,Object> praMap);
    
}
