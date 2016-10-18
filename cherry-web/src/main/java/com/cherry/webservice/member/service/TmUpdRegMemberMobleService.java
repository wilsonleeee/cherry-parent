package com.cherry.webservice.member.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: TmUpdRegMemberMobleService 
 * @Description: TODO(更新线上会员信息，新增线下会员信息的访问数据层的接口) 
 * @author lzs
 * @version v1.0.0 2016-09-20 
 *
 */
public class TmUpdRegMemberMobleService extends BaseService {
	/**
	 * 更新线下会员信息
	 */
	public int updateMemberInfoByTmMixMobile(Map<String, Object> paramMap) {
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TmUpdRegMemberMoble.updateMemberInfoByTmMixMobile");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 更新线上会员手机号
	 */
	public int updateOnineMobileByTmallMixMobile(Map<String, Object> paramMap) {
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TmUpdRegMemberMoble.updateOnineMobileByTmallMixMobile");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 获取线上会员信息
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getOnlineMemberInfo(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TmUpdRegMemberMoble.getOnlineMemberInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	/**
	 * 获取线下会员信息
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getMemberInfo(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "TmUpdRegMemberMoble.getMemberInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
}
