package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
@SuppressWarnings("unchecked")
public class BINOLMBMBM03_Service extends BaseService{
	/**
	 * 会员信息
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@Resource
	private BaseServiceImpl baseServiceImpl;
	public Map<String,Object> getMemberInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getMemberInfo");
		Map<String, Object> resultMap = (Map<String, Object>)baseServiceImpl.get(map);
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId")) && null != resultMap && !resultMap.isEmpty()) {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getMemberClubLevelInfo");
			Map<String, Object> levelMap = (Map<String, Object>) baseServiceImpl.get(map);
			if (null != levelMap && !levelMap.isEmpty()) {
				resultMap.putAll(levelMap);
			} else {
				resultMap.put("MemberLevel", null);
				resultMap.put("memberLevelId", null);
				resultMap.put("LevelName", null);
				resultMap.put("levelcode", null);
				resultMap.put("totalAmount", null);
				resultMap.put("totalPoint", null);
				resultMap.put("initialTime", null);
				resultMap.put("clubInfoUdTime", "");
				resultMap.put("clubInfoMdCount", "");
			}
		}
		return resultMap;
	}
	/**
	 * 取得会员修改状态List
	 * 
	 * @param map 检索条件
	 * @return 会员修改状态List
	 */
	public List<Map<String, Object>> getMemUsedInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getMemUsedInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 取得会员积分修改List
	 * 
	 * @param map 检索条件
	 * @return 会员积分修改List
	 */
	public List<Map<String, Object>> getMemPointList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getMemPointList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 取得用户密码
	 * 
	 * @param map 查询条件
	 * @return 用户密码
	 */
	public String getUserPassWord (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getUserPassWord");
		return (String)baseServiceImpl.get(map);
	}
	/**
	 * 会员履历的备注
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map<String,Object> getReason(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getReason");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得积分时间
	 * 
	 * @param map 查询条件
	 * @return 时间
	 */
	public String getPointTime (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM03.getPointTime");
		return (String)baseServiceImpl.get(map);
	}
}
