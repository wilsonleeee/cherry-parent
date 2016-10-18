package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBMBM25_Service extends BaseService{
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 * 
	 * 查询会员Id
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Object getMemberId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getMemberInfoId");
		return baseServiceImpl.get(paramMap);
	}
	/**
	 * 
	 * 取得会员等级Id
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Object getLevelId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getLevelId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
     * 插入导入主表，返回总表ID
     * @param map
     * @return
     */
    public int insertMemberImport(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.insertMemberImport");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    /**
	 * 
	 * 更新积分导入明细表,相同卡号不导入
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateImportDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.updateImportDetail");
		return baseServiceImpl.update(paramMap);
	}
    /**
	 * 取得会员等级List
	 * 
	 * @param brandInfoId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLevelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getLevelList");
		return baseServiceImpl.getList(map);
	}
	/**
     * 取得成功信息List
     * 
     * @param map 查询条件
     * 
     */
    public List<Map<String, Object>> getImportSucList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getImportSucList");
        return baseServiceImpl.getList(parameterMap);
    }
	/**
	 * 更新导入明细表sendflag=1
	 * @param map
	 * @return
	 */
	public int updateSendflag(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.updateSendflag");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 更新导入明细表ResultFlag=1
	 * @param map
	 * @return
	 */
	public int updateResultFlag(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.updateResultFlag");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 取得明细表已经存在的会员记录
	 * @param map
	 * @return
	 */
	public String getImpMemCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getImpMemCount");
		return (String)baseServiceImpl.get(parameterMap);
	}

	/**
	 * 
	 * 更新导入明细表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemImportDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.updateMemImportDetail");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 通过会员卡号查询会员信息
	 * 
	 * @param map 查询条件
	 * @return 会员信息
	 */
	public Map<String, Object> getMemberInfoByMemCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		if (CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getMemberInfoByMemCode");
		} else {
			paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM25.getMemberClubInfoByMemCode");
			Map<String, Object> resultMap = (Map)baseServiceImpl.get(paramMap);
			if (null == resultMap || resultMap.isEmpty()) {
				paramMap.put("NOCLUB", "1");
			} else {
				return resultMap;
			}
		}
		return (Map)baseServiceImpl.get(paramMap);
	}
	/**
	 * 用批处理插入一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void saveAll(final List<Map<String, Object>> list) {
		if (null != list && !list.isEmpty()) {
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			int size = list.size();
			while (true) {
				startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum;
				// 查询结束位置
				endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM;
				if (endNum > size) {
					endNum = size;
				}
				// 数据抽出次数累加
				currentNum++;	
				List<Map<String, Object>> tempList = list.subList(startNum, endNum);
				baseServiceImpl.saveAll(tempList, "BINOLMBMBM25.insertMemberImportDetail");
				if (endNum == size) {
					break;
				}
			}
		}
	}
}
