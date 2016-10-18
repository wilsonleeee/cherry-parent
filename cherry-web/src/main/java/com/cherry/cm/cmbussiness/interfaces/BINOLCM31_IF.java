/*      
 * @(#)BINOLCM31_IF.java     1.0 2012/05/10        
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbeans.Member;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;

/**
 * 规则处理 IF
 * @author hub
 *
 */
public interface BINOLCM31_IF {
	
	/**
	 * 取得会员等级有效期开始日和结束日
	 * 
	 * @param tradeType
	 * 			业务类型
	 * @param ticketDate
	 * 			单据时间
	 * @param memberLevel
	 * 			当前会员等级
	 * @param reCalcDate
	 * 			重算日期（非重算调用设为null）
	 * @param memberId
	 * 			会员ID
	 * @return Map
	 * 			levelStartDate : 等级有效期开始日
	 * 			levelEndDate : 等级有效期结束日
	 * @throws Exception 
	 */
	public Map<String, Object> getLevelDateInfo(String tradeType, String ticketDate, int memberLevel, String reCalcDate, int memberId) throws Exception;
	
	/**
	 * 取得系统默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			
	 * @return int 
	 * 			系统默认等级
	 */
	public int getDefaultLevel(Map<String, Object> map);
	
	/**
	 * 取得规则ID
	 * @param code
	 * 			内容代号  			
	 * @return int 
	 * 			规则ID
	 */
	public int getRuleIdByCode(String code);
	
	/**
	 * 取得会员初始采集信息
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialMemLevel: int 初始会员等级
	 * 			initialDate: String 初始采集日期
	 */
	public Map<String, Object> getMemInitialInfo(int memberInfoId);
	
	/**
	 * 取得会员初始积分信息
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialTime: 初始导入时间
	 */
	public Map<String, Object> getMemPointInitInfo(int memberInfoId);
	
	/**
	 * 取得单据对应的BA卡号及柜台号信息
	 * 
	 * @param orgId
	 * 			组织信息ID
	 * @param brandId
	 * 			品牌信息ID
	 * @param ticketNo
	 * 			单据号
	 * @param tradeType
	 * 			业务类型
	 * @return Map
	 * 			baCode : BA卡号
	 * 			counterCode : 柜台号
	 * @throws Exception 
	 */
	public Map<String, Object> getBaCounterInfo(int orgId, int brandId, String ticketNo, String tradeType);
	
	/**
     * 更新会员信息扩展表
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			totalAmounts : 累计金额
     * 			curBtimes : 化妆次数
     * 			depositAmount : 储蓄卡金额
     */
    public void updateMemberExtInfo(Map<String, Object> map);
    
    /**
     * 更新会员积分信息表
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			curTotalPoint : 累计积分
     * 			curChangablePoint : 可兑换积分
     */
    public void updateMemberPointInfo(Map<String, Object> map);
    
    /**
     * 处理会员积分变化信息
     * 
     * @param pointChange
     * 		会员积分变化信息
     * 			
     */
    public void execPointChangeInfo(CampBaseDTO campBaseDTO) throws Exception;
    
    /**
     * 判断会员是否正在进行重算
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			brandCode : 品牌代码
     * 			orgCode : 组织代码
     * @return true : 正在重算中   false : 非重算中
     * @throws Exception 
     */
    public boolean isReCalcExec(Map<String, Object> map) throws Exception;
    
    /**
	 * 取得规则内容
	 * @param int
	 * 			规则ID			
	 * @return String 
	 * 			规则内容
	 */
	public String getRuleContentById(int ruleId);
	
	/**
	 * 取得柜台信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			柜台信息
	 * 			organizationId : 部门ID
	 * 			departName : 部门名称
	 * 			channelId : 渠道ID
	 * 			cityId : 柜台所属城市ID
	 */
    public Map<String, Object> getComCounterInfo(Map<String, Object> map);
    
    /**
	 * 取得员工信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			员工信息
	 * 			employeeId : 员工ID
	 */
    public Map<String, Object> getComEmployeeInfo(Map<String, Object> map);
    
    /**
	 * 取得会员卡号
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			会员卡号
	 * 
	 */
	public String getMemCard(Map<String, Object> map);
	
	/**
	 * 封装积分消息体
	 * 
	 * @param msg
	 * 			积分消息内容
	 * @return String
	 * 			封装好的积分消息体
	 */
	public String getPointData(String msg);
	
	/**
     * 取得规则名称列表
     * 
     * @param map
     * 			brandInfoId : 品牌ID
     * 			organizationInfoId : 组织ID
     * 			campaignType : 规则类型
     * @return
     * 		规则名称列表
     */
    public List<Map<String, Object>> getCampRuleNameList(Map<String, Object> map);
    
    /**
	 * 统计时间段内某规则的所有积分总和
	 * 
	 * @param map
	 * 			参数集合
	 * 			memberInfoId : 会员信息ID
	 * 			fromDate : 开始日期
	 * 			toDate : 结束日期
	 * 			searchIdKbn : 查询的规则ID区分
	 * 			ruleId : 规则ID
	 * @return Map
	 * 			积分总和信息
	 * 			totalPoint : 积分总和
	 */
    public Map<String, Object> getTotalPointInfo(Map<String, Object> map);
    
    /**
	 * 设置会员属性
	 * 
	 * @param c
	 * 			会员信息DTO
	 * 
	 */
    public void execMemberInfo(CampBaseDTO c);

    /**
     * 取得会员当前积分信息
     * 
     * @param Map
     * 			memberInfoId : 会员信息ID
     * @return Map
     * 			memberPointId : 会员积分ID
     * 			curTotalPoint : 总积分
	 * 			curTotalChanged : 可兑换积分
	 * 			curChangablePoint : 已兑换积分
     */
    public Map<String, Object> getMemberPointInfo(Map<String, Object> map);
    
    /**
	 * 取得积分MQ消息体(实时)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public MQInfoDTO getPointMQMessage(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
     * 取得会员当前积分信息(实体)
     * 
     * @param Map
     * 			memberInfoId : 会员信息ID
     * @return PointDTO
     * 			
     */
    public PointDTO getMemberPointDTO(Map<String, Object> map);
    
    /**
	 * 验证会员卡号是否为当前卡
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @param memCard
	 * 			会员卡号
	 * @return 判断结果
	 * 			true: 当前卡   false: 非当前卡
	 * 
	 */
	public boolean isCurCard(int memberInfoId, String memCode);
	
	/**
     *更新会员前卡积分值
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			preCardPoint : 前卡积分
     * @return int
     * 			更新件数
     */
    public int updatePreCardPoint(Map<String, Object> map);
    
    /**
	 * 验证会员是否只有一张卡
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @return 判断结果
	 * 			true: 只有一张卡   false: 多张卡
	 * 
	 */
	public boolean isSingleCard(int memberInfoId);
	
	/**
	 * 重新计算前卡积分
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @param memCode
	 * 			会员卡号
	 * @param curPoint
	 * 			当前总积分
	 * @param memberClubId
	 * 			会员俱乐部ID
	 * @return double
	 * 			重算后的前卡积分
	 * 
	 */
	public double recPreCardPoint(int memberInfoId, String memCode, double curPoint, int memberClubId);
	
	/**
	 * 取得会员等级有效期信息
	 * 
	 * @param map
	 * 			memberLevelId : 会员等级ID
	 * @return Map
	 * 			validity : 有效期信息
	 */
    public Map<String, Object> getLevelValidInfo(int levelId) throws Exception;
    
    /**
	 * 查询有效期开始的单据信息
	 * 
	 * @param memberLevel
	 * 			会员等级ID
	 * @param memberId
	 * 			会员ID
	 * @param lelEndTime
	 * 			截止时间
	 * @return Map
	 * 			单据信息
	 * @throws Exception 
	 */
	public Map<String, Object> getValidStartInfo(int memberLevel, int memberId, String lelEndTime) throws Exception;
	
	
	/**
     * 发送会员资料MQ消息
     * 
     * @param map 会员信息
     * @throws Exception 
     */
    public void sendMEMQMsg(Map<String, Object> map) throws Exception;
    
    
    /**
	 * 查询等级所有变化履历
	 * 
	 * @param map
	 * 			参数集合
	 * @return List
	 * 			等级所有变化履历
	 */
	public List<Map<String, Object>> getLevelAllRecordList(Map<String, Object> map);
	
    /**
     * 取得所有等级变化明细
     * 
     * @param campBaseDTO 
     * 				会员实体
     * @param levelDetailList 
     * 				等级变化明细
	 * @return List
     * 				所有等级变化明细
     * @throws Exception 
     */
	public List<LevelDetailDTO> getLevelAllRecords(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList) throws Exception;
	
	/**
     * 取得会员等级列表
     * 			
     * @param campBaseDTO
     * 			会员实体
     * @return List
     * 			会员等级列表
     */
    public List<Map<String, Object>> getAllLevelList(CampBaseDTO campBaseDTO);
    
    /**
     * 取得规则理由描述信息
     * 			
     * @param ruleIds
     * 			规则理由ID
     * @return String
     * 			规则理由描述信息
     */
    public String getRuleReason(String ruleIds);
    
    /**
	 * 
	 * 取得会员保级信息
	 * 
	 * @param campBaseDTO 
	 * 				会员实体
	 * @return Map
	 * 				会员保级信息
	 * 
	 */
	public Map<String, Object> getKeepMemLevelInfo(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 取得积分MQ消息体(历史积分)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public MQInfoDTO getPointMQMessageHist(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 查询首单时间
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			首单时间
	 * 
	 */
	public String getFirstBillDate(CampBaseDTO campBaseDTO);
	
	/**
	 * 查询指定时间段的积分情况
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			指定时间段的积分情况
	 * 
	 */
	public List<Map<String, Object>> getPointChangeTimesList(Map<String, Object> map);
	/**
     * 更新积分导入明细表
     * 
     * @param map
     * 		参数集合
     * 			memCode : 会员信息ID
     * 			pointTime : 积分时间
     */
    public void updateMemPointImportDetail(Map<String, Object> map);
    
    /**
	 * 
	 * 取得会员等级维护信息
	 * 
	 * @param campBaseDTO 
	 * 				会员实体
	 * @return Map
	 * 				会员等级维护信息
	 * 
	 */
	public Map<String, Object> getMemLevelChangeInfo(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
     * 验证某个单号是否是首单
     * 
     * @param map
     * @return boolean
     * 			true: 是    false: 否
     */
    public boolean checkFirstBill(Map<String, Object> map);
    
    /**
     * 取得当前业务日期对应的可兑换积分截止日期
     * 
     * @param brandInfoId
     * 			品牌ID
     * @param busDate
     * 			业务日期
     * @return String
     * 			可兑换积分截止日期
     */
    public String getExPointDateFromCamp(int brandInfoId, String busDate);
    
    /**
     * 清除会员清零标识
     * 
     * @param memberId
     * 			会员ID
     */
    public void delClearFlag(int memberId);
    
    /**
	 * 取得沟通短信消息体(实时)
	 * 
	 * @param map
	 * 			参数集合
	 * @throws Exception
	 */
	public MQInfoDTO getGTMQMessage(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得微信短信消息体(实时)
	 * 
	 * @param map
	 * 			参数集合
	 * @throws Exception
	 */
	public MQInfoDTO getWXMQMessage(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员最近一次积分变化信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最近一次积分变化信息
	 */
	public Map<String, Object> getLastPointChangeInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 查询指定时间段的积分情况(包含积分为0的记录)
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			指定时间段的积分情况
	 * 
	 */
	public List<Map<String, Object>> getPCTimesList(Map<String, Object> map);
	
	/**
	 * 整单全退的单据
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			整单全退的单据
	 * 
	 */
	public List<Map<String, Object>> getPCTimesSRList(Map<String, Object> map);
	
	/**
	 * 取得业务日期
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 业务日期
	 */
	public String getBusDate(CampBaseDTO campBaseDTO);
	
	/**
	 * 取得会员生日变更履历
	 * 
	 * @param map 查询条件
	 * @return 会员生日变更履历
	 */
	public List<Map<String, Object>> getBirthModyList(Map<String, Object> map);
	
	/**
	 * 
	 * 取得会员生日类规则ID
	 * 
	 * @param map 查询参数
	 * @return 会员生日类规则ID
	 * @throws Exception 
	 * 
	 */
	public String[] getBirthRuleArr(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 取得会员生日规则匹配的履历
	 * 
	 * @param map 查询参数
	 * @return 销售记录List
	 * 
	 */
	public Map<String, Object> getBirthPointDeatilInfo(Map<String, Object> map);
	
	/**
	 * 重设生日
	 * 
	 * @param campBaseDTO 查询条件
	 * 
	 */
	public void resetBirth (CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 
	 * 取得规则条件
	 * 
	 * @param map 查询参数
	 * @return 规则条件
	 * @throws Exception 
	 * 
	 */
	public Map<String, Object> getRuleCond(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 取得规则条件列表
	 * 
	 * @param map 查询参数
	 * @return 规则条件列表
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, Object>> getRuleCondList(Map<String, Object> map) throws Exception;
	
	/**
     * 更新会员积分信息表(历史记录)
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			curTotalPoint : 累计积分
     * 			curChangablePoint : 可兑换积分
     */
    public void updateHistoryPointInfo(Map<String, Object> map);
    
    /**
	 * 
	 * 调整会员等级(珀莱雅)
	 * 
	 * @param map 
	 * 				查询参数
	 * @return int
	 * 				调整结果
	 * @throws Exception 
	 * 
	 */
	public int levelAdjust(Map<String, Object> map) throws Exception;
	
	/**
     * 取得会员等级列表
     * 
     * @param map
     * @return
     * 		会员等级列表
	 * @throws Exception 
     */
    public List<Map<String, Object>> getLevelList(Map<String, Object> map) throws Exception;
    
    /**
	 * 取得规则执行次数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			规则执行次数
	 * 
	 */
	public int getRuleExecCount(Map<String, Object> map);
	
	/**
	 * 取得等级(根据入会途径)
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			sourse: 入会途径
	 * @return int 
	 * 			相应等级
	 */
	public int getLevelByChannel(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员积分明细总数
	 * @param memberInfoId
	 * 			会员ID
	 * @return int 
	 * 			积分明细总数
	 */
	public int getTotalPtlNum(int memberInfoId);
	
	/**
	 * 取得会员俱乐部当前等级信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员俱乐部当前等级信息
	 */
	public Map<String, Object> getClubCurLevelInfo(Map<String, Object> map);
	
	/**
	 * 取得会员初始采集信息(会员俱乐部)
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialMemLevel: 初始会员等级
	 * 			initialDate: 初始采集日期
	 */
	public Map<String, Object> getClubMemInitialInfo(int memberInfoId, int memberClubId);
	
	/**
     * 更新会员俱乐部等级信息扩展表
     * 
     * @param map
     * 		参数集合
     * 			clubLevelId : 等级ID
     * 			memberInfoId : 会员信息ID
     * 			memberClubId : 会员俱乐部ID
     * 			totalAmounts : 累计金额
     * 			upLevelAmount : 化妆次数
     */
    public void updateMemberClubExtInfo(Map<String, Object> map);
    
    /**
	 * 
	 * 更新会员俱乐部扩展属性
	 * 
	 * @param map 更新条件
	 */
	public void updateClubExtInfo(Map<String, Object> map);
    
    /**
	 * 取得会员俱乐部代号
	 * 
	 * @param map
	 * 			参数集合
	 * @return String
	 * 			会员俱乐部代号
	 */
	public String getClubCode(Map<String, Object> map);
	
	/**
	 * 查询会员俱乐部ID
	 * @param map
	 *			参数集合
	 * @return Integer
	 * 			会员俱乐部ID
	 */
	public Integer selMemClubId(Map map);
	
	/**
     * 发送会员扩展信息MQ消息
     * 
     * @param map 会员扩展信息
     * @throws Exception 
     */
	public void sendMZMQMsg(Map<String, Object> map) throws Exception;
	
	/**
     * 发送会员扩展信息MQ消息(全部记录)
     * 
     * @param map 会员扩展信息
     * @throws Exception 
     */
	public void sendAllMZMQMsg(Map<String, Object> map) throws Exception;
	
	/**
	 * 验证柜台是否需要执行规则
	 * @param map
	 *			参数集合
	 * @return boolean
	 * 			true: 执行  false: 不执行
	 */
	public boolean isRuleCounter(Map<String, Object> map) throws Exception;
	
	/**
     * 取得一段时间内的购买金额
     * 
     * @param map
     * @return
     * 		购买金额
     */
    public double getTtlAmount(Map<String, Object> map);
    
    /**
	 * 取得会员初始积分信息(会员俱乐部)
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialTime: 初始导入时间
	 */
	public Map<String, Object> getClubMemPointInitInfo(int memberInfoId, int memberClubId);
	
	/**
	 * 查询会员俱乐部规则列表
	 * @param map
	 * @return
	 * 		会员俱乐部规则列表
	 */
	public List<Map<String, Object>> selClubRuleList(Map<String, Object> map);
	
	 /**
     * 清除会员清零标识
     * 
     * @param memberId
     * 			会员ID
     */
    public void delClubClearFlag(int memberId, int clubId);
    
    /**
     * 取得会员信息
     * 
     * @param map
     * @return
     * 		会员信息
     * @throws Exception 
     */
	public void getMember(Map<String, Object> map, Member member) throws Exception;
	
	 /**
     * 取得会员信息
     * 
     * @param map
     * @return
     * 		会员信息
	 * @throws Exception 
     */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception;
	
	/**
     * 是否需要同步天猫会员
     * 
     * @param memberInfoId 会员ID
     * @param brandCode 品牌代码
     * 
     * @return boolean true: 需要同步 false：不需要同步
     */
	public boolean needSync(int memberInfoId, String brandCode);
	
	/**
     * 同步天猫会员
     * 
     * @param map
     * @return
	 * @throws Exception 
     */
	public void syncTmall(Map<String, Object> map) throws Exception;
	
	/**
     * 判断会员是否正在进行重算
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			brandCode : 品牌代码
     * 			orgCode : 组织代码
     * @return true : 正在重算中   false : 非重算中
     * @throws Exception 
     */
    public boolean isMemReCalcExec(Map<String, Object> map) throws Exception;
    
    /**
	 * 将消息发送到积分维护的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void sendPointsMQ(Map map) throws Exception;
	
	/**
	 * 取得俱乐部默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			mClubId: 会员俱乐部ID
	 * @return int 
	 * 			俱乐部默认等级
	 */
	public int getClubDefaultLevel(Map<String, Object> map);
	
	/**
	 * 
	 * 通过销售更新会员俱乐部属性
	 * 
	 * @param map 更新条件
	 * @throws Exception 
	 */
	public void updateClubInfoBySale(Map<String, Object> map) throws Exception;
	
	/**
	 * 等级调整会员插入到临时表
	 * @param memberId
	 */
	public void addTempAdjustMember(int memberId,int orgId,int brandId);
	
	/**
	 * 取得系统天猫默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			
	 * @return int 
	 * 			天猫默认等级
	 */
	public int getTmallDefaultLevel(Map<String, Object> map);
}
