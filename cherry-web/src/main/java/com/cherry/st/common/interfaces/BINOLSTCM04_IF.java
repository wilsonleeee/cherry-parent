package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLSTCM04_IF {

	/**
	 * 将移库单据的信息写入移库单主从表
	 * @param mainDate 移库主表数据
	 * @param detailList 移库明细表数据，因为是多条，故以list形式提供
	 * 
	 * */
	public int insertProductShiftAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	/**
	 * 修改移库单据的主表
	 * @param mainDate 移库主表数据
	 * 
	 * */
	public int updateProductShiftMain(Map<String,Object> mainData);
	
	/**
	 * 写入出库表，更改库存
	 * @param praMap
	 */
	public void changeStock(Map<String,Object> praMap);
	
	/**
	 * 取得移库单概要信息
	 * @param productShiftMainID
	 * @return
	 */
	public Map<String,Object> getProductShiftMainData(int productShiftMainID,String... language);
	
	/**
	 * 取得移库单明细信息
	 * @param productShiftMainID
	 * @return
	 */
	public List<Map<String,Object>> getProductShiftDetailData(int productShiftMainID,String... language);
}
