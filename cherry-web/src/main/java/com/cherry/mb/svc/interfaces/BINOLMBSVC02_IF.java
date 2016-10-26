package com.cherry.mb.svc.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLMBSVC02_IF extends BINOLCM37_IF{
	/**
	 * 储值卡页面查询
	 */
	public Map<String, Object> getCardCountInfo(Map<String, Object> map);
	/**
	 *储值卡获取集合 
	 */
	public List<Map<String, Object>> getCardList(Map<String, Object> map);
	/**
	 * 停用储值卡
	 */
	public int stopCard(Map<String,Object> map);
	/**
	 * 储值卡信息获取导出参数
	 */
	public Map<String, Object> getCardExportParam(Map<String, Object> map);
	/**
	 * 储值卡导出CSV
	 */
	
	public String cardExportCSV(Map<String, Object> map) throws Exception;
	/**
	 * 储值卡交易明细页面查询
	 */
	public Map getSaleCountInfo(Map<String, Object> map);
	/**
	 *储值卡交易明细获取集合 
	 */
	public List<Map<String, Object>> getSaleList(Map<String, Object> map);
	/**
	 * 储值卡销售获取导出参数
	 */
	public Map<String, Object> getSaleExportParam(Map<String, Object> map);
	/**
	 * 储值卡销售导出CSV
	 */
	public String saleExportCSV(Map<String, Object> map) throws Exception;
	/**
	 * 储值卡批量开卡业务
	 */
	public Map<String,Object> SavingsCardAddCard(Map<String,Object> card_map)  throws Exception;
	/**
	 * 查看交易明细
	 */
	public List<Map<String, Object>> getServiceList(Map<String, Object> map);
	/**
	 * 服务页面查询
	 */
	public Map<String, Object> getServiceCountInfo(Map<String, Object> map);
	/*
	 * 根据billCode查询储值卡交易明细
	 */
	public Map<String, Object> getSaleByBillCode(Map<String, Object> map);
	/**
	 * 根据billCode查询储值卡交易明细
	 */
	public Map<String, Object> getSaleDetailCountInfo(Map<String, Object> map);
	/**
	 * 根据cardCode查询储值卡交易明细
	 */
	public List<Map<String, Object>> getSaleByCardCode(Map<String, Object> map);
	/**
	 * 根据cardCode和柜台号发送短信验证码
	 */
	public String getCouponCode(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据cardCode设置新的密码
	 */
	public Map<String,Object> getNewPassword(Map<String, Object> map) throws Exception;
	
	/**
	 * 废弃储值卡
	 */
	public int abandonCard(Map<String, Object> map);
	
	/**
	 * 发送储值卡重置密码短信
	 * @throws Exception 
	 */
	public Map<String, Object> sendResetMessage(Map<String, Object> map) throws Exception;
}
