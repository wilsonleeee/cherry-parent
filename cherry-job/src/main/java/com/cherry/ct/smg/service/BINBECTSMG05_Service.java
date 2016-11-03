package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINBECTSMG05_Service extends BaseService{
	/**
     * 获取沟通触发事件设置信息List
     * 
     * @param map
     * @return
     * 		沟通设置List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getEventSetList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getEventSetList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 根据批次号查询活动参与会员总数
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getActivityCustomerCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getActivityCustomerCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据批次号查询活动参与会员明细
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getActivityCustomerList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getActivityCustomerList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
	 * 记录需要延时的触发事件的MQ信息
	 * 
	 * @param map
	 * @return
	 */
	public void addDelayEventInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBECTSMG05.addDelayEventInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询延时设置
	 * 
	 * @param map
	 * @return 延时设置List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelaySetList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getDelaySetList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 查询事件类型列表
	 * 
	 * @param map
	 * @return 延时事件List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEventTypeList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getEventTypeList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 查询延时事件信息
	 * 
	 * @param map
	 * @return 延时事件List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelayEventInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getDelayEventInfoList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 
	 * 更新延时事件运行信息
	 * 
	 * @param map 更新条件和内容
	 * 
	 */
	public void updateDelayEventRunInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.updateDelayEventRunInfo");
		baseServiceImpl.update(paramMap);
	}
	
	 /**
     * 根据会员号查询入会会员总数
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getJoinMemberInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getJoinMemberInfoCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
	/**
     * 根据会员号查询入会会员信息
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getJoinMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getJoinMemberInfoList");
        return baseServiceImpl.getList(paramMap);
    }
	
    /**
     * 根据批次号查询转柜会员总数
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getChangeCounterMemberCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getChangeCounterMemberCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据批次号查询转柜会员明细
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChangeCounterMemberList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getChangeCounterMemberList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 获取导致积分变化的业务类型
     * 
     * @param map
     * @return
     * 		业务类型List
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointChangeTypeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getPointChangeTypeList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 获取积分变化的会员信息
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointChangeMemberList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getPointChangeMemberList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据批次号查询资料变更会员总数
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getChangeInfoMemberCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getChangeInfoMemberCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据批次号查询资料变更会员会员明细
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChangeInfoMemberList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getChangeInfoMemberList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据信息发送编号获取需要重发的信息内容
     * 
     * @param map
     * @return
     * 		信息内容
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getRetransmissionMsg(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getRetransmissionMsg");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }

    /**
     * 根据信息编号获取需要重发信息的会员
     * 
     * @param map
     * @return
     * 		会员信息
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRetransmissionMsgMember(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getRetransmissionMsgMember");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据手机号码和活动编号获取需要重发的信息内容
     * 
     * @param map
     * @return
     * 		信息内容
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getRessMsgByMobile(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getRessMsgByMobile");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据手机号码和活动编号获取需要重发信息的会员
     * 
     * @param map
     * @return
     * 		会员信息
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRessMsgMemberByMobile(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getRessMsgMemberByMobile");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据信息发送编号从Coupon生成记录表中获取前一条信息对应的活动编号
     * 
     * @param map
     * @return
     * 		沟通模板内容
     */
    public String getCampCodeFromCreateCouponLog(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getCampCodeFromCreateCouponLog");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据会员ID查询升级会员的总数
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getLevelUpMemberInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getLevelUpMemberInfoCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
	/**
     * 根据会员ID查询升级会员列表
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLevelUpMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getLevelUpMemberInfoList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索编号和手机号查询客户数量
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getSearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getSearchResultCountByCode");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
    /**
     * 根据登陆用户的手机号查询登陆用户
     * 
     * @param map
     * @return
     * 		会员总数
     */
    public int getUserInfoByMobileCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getUserInfoByMobileCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
	/**
     * 根据登陆用户的手机号查询登陆用户
     * 
     * @param map
     * @return
     * 		会员明细
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfoByMobileList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getUserInfoByMobileList");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据活动表【campaignOrder】中的手机号来查询获取生日礼会员的验证码及其详细信息
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemInfoByMobile(Map<String, Object> map){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.getMemInfoByMobile");
    	return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据手机号来验证会员表【Members.BIN_MemberInfo】中是否存在会员信息
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> checkMemExistByMobilePhone(Map<String, Object> map){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG05.checkMemExistByMobilePhone");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
}
