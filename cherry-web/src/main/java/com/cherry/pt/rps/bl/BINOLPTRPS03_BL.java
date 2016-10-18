package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.rps.service.BINOLPTRPS03_Service;


/**
 * 
 * 发货单查询BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.03
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS03_BL {
	
	@Resource(name="BINOLPTRPS03_Service")
	private BINOLPTRPS03_Service binolptrps03_Service;
	
	/**
	 * 取得产品总数量和总金额
	 * 
	 * */
	public Map getSumInfo(Map<String,Object> map){
		return binolptrps03_Service.getSumInfo(map);
	}
	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List searchProductList(Map<String, Object> map) {
		// 取得发货单List
		return binolptrps03_Service.getProductList(map);
	}
	

	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchDeliverInfo(Map<String, Object> map) {
		// 取得发货单信息
		return binolptrps03_Service.getDeliverInfo(map);
	}

	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverDetailList(Map<String, Object> map) {
		// 取得发货单明细List
		return binolptrps03_Service.getDeliverDetailList(map);
	}
}
