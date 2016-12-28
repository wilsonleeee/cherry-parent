package com.cherry.wp.sal.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL07_Service extends BaseService{

	// 获取单据数量
	public int getFinishedBillsCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getFinishedBillsCount");
		return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
	}
	
	// 获取单据列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFinishedBillList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getFinishedBillList");
        return baseServiceImpl.getList(paramMap);
    }
	
	// 获取单据Map
	@SuppressWarnings("unchecked")
    public Map<String, Object> getFinishedBillMap(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getFinishedBillMap");
        return (Map<String,Object>)baseServiceImpl.get(paramMap);
    }
	
	// 获取退货单据数量
	public int getReturnHistoryBillCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getReturnHistoryBillCount");
		return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
	}
	
	// 获取单据列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getReturnHistoryBillList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getReturnHistoryBillList");
        return baseServiceImpl.getList(paramMap);
    }
	
	// 根据单据号获取单据明细列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillDetailListByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getBillDetailListByCode");
        return baseServiceImpl.getList(paramMap);
    }

	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getReturnDetailListByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getReturnDetailListByCode");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSrBillDetailByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getSrBillDetailByCode");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillDetailSavings(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getBillDetailSavings");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReturnBillDetailSavings(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getReturnBillDetailSavings");
        return baseServiceImpl.getList(paramMap);
	}
	
	public int insertSrBill(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.insertSrBill");
        return baseServiceImpl.saveBackId(paramMap);
    }
	
	public void insertSrBillDetail(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.insertSrBillDetail");
        baseServiceImpl.save(paramMap);
    }
	
	public void insertSrPayment(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.insertSrPayment");
        baseServiceImpl.save(paramMap);
    }

	// 更新单据
	public Map<String,Object> getSaleBillInfoBeforeUpdate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getSaleBillInfoBeforeUpdate");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}

	// 更新单据
	public int updateSaleBillInfo(Map<String, Object> map) {
		Map<String,Object> billInfo=this.getSaleBillInfoBeforeUpdate(map);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.putAll(billInfo);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.updateSaleBillInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	public int saveBillSrRecord(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.saveBillSrRecord");
        return baseServiceImpl.saveBackId(paramMap);
    }
	
	public void saveBillSrDetail(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.saveBillSrDetail");
        baseServiceImpl.save(paramMap);
    }

	public List<Map<String, Object>> getPaymentTypeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.paymentTypeList");
        return baseServiceImpl.getList(paramMap);
	}

	/** 获取会员当前总积分和对应销售所得积分，用于计算退货时积分是否足够 **/
	public Map<String,Object> getSaleMemPointInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getSaleMemPointInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}

//	public void getAllBillsToWebPos(Map<String, Object> map){
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.putAll(map);
//        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL07.getAllBillsToWebPos");
//        baseServiceImpl.save(paramMap);
//	}
}
