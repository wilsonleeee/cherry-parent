package com.cherry.middledbout.stand.member.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
/**
 * 标准接口:会员数据导出至标准接口表(IF_MemberInfo)service
 * @author lzs
 * 下午1:45:44
 */
public class MemberInfo_Service extends BaseService{
	/**
	 * 查询所有有效的会员数据
	 * @param map
	 * @return
	 */
	public List getMemberList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "MemberInfo.getMemberList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 根据新后台会员ID查询标准接口会员表的数据List
	 * @param map
	 * @return
	 */
	public Map<String,Object> getMemberByIFMemberId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "MemberInfo.getMemberByIFMemberId");
		return (Map<String,Object>)tpifServiceImpl.get(map);
	}
	/**
	 * 插入数据至标准接口会员表
	 * @param listMap
	 */
	public void insertIFMember(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "MemberInfo.insertIFMember");
		tpifServiceImpl.save(map);
	}
	/**
	 * 更新会员接口表已存在的数据
	 * @param map
	 * @return
	 */
	public int updMemberInfoByIFmemberId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "MemberInfo.updMemberInfoByIFmemberId");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 取得品牌code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"MemberInfo.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
}
