package com.cherry.wp.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLWPCM01_Service extends BaseService{
	// 获取会员数量
	public int getMemberCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getMemberCount");
		return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
	}
	
	// 获取会员列表List（分页查询情况）
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMemberList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getMemberList");
		List<Map<String, Object>> memberInfoList = baseServiceImpl.getList(paramMap);
		String memOtherInfoFlag = (String)map.get("memOtherInfoFlag");
		if(memOtherInfoFlag == null || "".equals(memOtherInfoFlag)) {
			setMemOtherInfo(memberInfoList);
		}
		return memberInfoList;
    }
	
	// 获取会员列表List（不分页查询）
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getMemberInfoList");
		List<Map<String, Object>> memberInfoList = baseServiceImpl.getList(paramMap);
		String memOtherInfoFlag = (String)map.get("memOtherInfoFlag");
		if(memOtherInfoFlag == null || "".equals(memOtherInfoFlag)) {
			setMemOtherInfo(memberInfoList);
		}
		return memberInfoList;
    }
	
	/**
	 * 取得会员积分、购买、礼券等信息
	 * 
	 * @param map 查询条件
	 * @return 会员积分、购买、礼券等信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemOtherInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getMemOtherInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getMemberLevelInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 设置会员积分、购买、礼券等信息
	 * 
	 * @param memberInfoList 待设置的会员信息List
	 */
	public void setMemOtherInfo(List<Map<String, Object>> memberInfoList) {
		if(memberInfoList != null && !memberInfoList.isEmpty()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("list", memberInfoList);
			List<Map<String, Object>> memOtherInfoList = getMemOtherInfoList(paramMap);
			if(memOtherInfoList != null && !memOtherInfoList.isEmpty()) {
				for(int i = 0; i < memberInfoList.size(); i++) {
					Map<String, Object> memMap = memberInfoList.get(i);
					int memId = (Integer)memMap.get("memberInfoId");
					for(int j = 0; j < memOtherInfoList.size(); j++) {
						Map<String, Object> memOtherMap = memOtherInfoList.get(j);
						int _memId = (Integer)memOtherMap.get("memberInfoId");
						if(memId == _memId) {
							memMap.putAll(memOtherMap);
							memOtherInfoList.remove(j);
							break;
						}
					}
				}
			}
		}
	}
	
	// 获取柜台BA列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCounterBaList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getCounterBaList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 查询柜台营业员信息
	 * 
	 * @param map 查询条件
	 * @return 营业员信息List
	 */
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getBAInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询柜台当班的营业员信息
	 * 
	 * @param map 查询条件
	 * @return 当班的营业员信息List
	 */
	public List<Map<String, Object>> getActiveBAList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getActiveBAList");
		return baseServiceImpl.getList(paramMap);
	}

	public List<Map<String, Object>> getOrderCounterCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPCM01.getOrderCounterCode");
		return baseServiceImpl.getList(paramMap);
	}
}
