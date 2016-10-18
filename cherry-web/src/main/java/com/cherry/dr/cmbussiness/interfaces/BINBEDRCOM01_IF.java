/*	
 * @(#)BINBEDRCOM01_IF.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.mq.MQLogDTO;

/**
 * 会员活动共通IF
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public interface BINBEDRCOM01_IF {
	
	/**
	 * 更新重算信息表
	 * 
	 * @param map 
	 * 				查询条件
	 * 
	 */
	public void updateReCalc(Map<String, Object> map);
	
	/**
	 * 更新重算信息表(MQ)
	 * 
	 * @param map 
	 * 				查询条件
	 */
	public void updateReCalcMQ(Map<String, Object> map) throws Exception;
	
	/**
	 * 验证是否是有效的会员
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 是  false: 否
	 * @throws Exception 
	 */
	public boolean isValidMember(Map<String, Object> map) throws Exception;
	
	/**
	 * 验证是否需要执行规则
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 需要  false: 不需要
	 * @throws Exception 
	 */
	public boolean isRuleExec(Map<String, Object> map) throws Exception;
	
	/**
	 * 验证是否需要执行规则(包含积分处理器)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return int 
	 * 				-1: 不执行   0: 执行等级积分  1: 仅执行积分
	 * @throws Exception 
	 */
	public int isRuleExecPT(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得当前会员信息(会员资料)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				当前会员信息
	 * @throws Exception 
	 */
	public CampBaseDTO getCurMemberInfoMB(Map<String, Object> map) throws Exception;
	
	/**
	 * 生成执行规则前的DTO
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				执行规则前的DTO
	 * @throws Exception 
	 */
	public CampBaseDTO getCampBaseDTO(Map<String, Object> map) throws Exception;
	
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
	 * 获取当前 等级和化妆次数
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	public void getCurNewValue(CampBaseDTO campBaseDTO);
	
	/**
	 * 获取累计金额, 等级
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	public void getNewValueLevel(CampBaseDTO campBaseDTO);
	
	/**
	 * 获取化妆次数, 可兑换金额(化妆次数用)
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	public void getNewValueBtimes(CampBaseDTO campBaseDTO);
	
	/**
	 * 插入规则执行履历表
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public void addRuleExecRecord(CampBaseDTO campBaseDTO, int recordKbn, boolean isChange) throws Exception;
	
	/**
	 * 取得等级和化妆次数MQ消息体
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public MQLogDTO getLevelBtimesMQMessage(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 取得等级MQ消息体
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public MQInfoDTO getLevelMQMessage(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 取得化妆次数MQ消息体
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public MQInfoDTO getBtimesMQMessage(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 取得积分MQ消息体(会员资料上传)
	 * 
	 * @param campBaseDTO
	 * 			会员信息DTO
	 * @param map
	 * 			参数集合
	 * @return MQInfoDTO
	 * 			MQ消息 DTO
	 * @throws Exception 
	 */
	public MQInfoDTO getPointMQMessageMB(CampBaseDTO campBaseDTO, Map<String, Object> map) throws Exception;
	
	/**
	 * 删除清零记录
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	public void removeZCRecord(CampBaseDTO campBaseDTO, int recordKbn) throws Exception;
	
	/**
	 * 取得累计金额
	 * 
	 * @param campBaseDTO
	 * @return double 累计金额
	 * @throws Exception
	 */
	public double getTotalAmount(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得购买次数
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return int 
	 * 			购买次数
	 * @throws Exception
	 */
	public int getBuyTimes(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新会员信息表（会员等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updMemberInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 更新会员信息表（会员等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updClubMemberInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 获取单号(BATCH特定业务的单号获取：清零、降级等)
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public String ticketNumberBatch(CampBaseDTO campBaseDTO);
	
	/**
	 * 取得原会员卡号
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			原会员卡号
	 */
	public Map<String, Object> getOldMemCodeInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 获取等级信息
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMemLevelcomList(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 设置条件：等级信息，购买产品等
	 * 
	 * @param campBaseDTO
	 * @throws Exception
	 */
	public void conditionSetting(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 插入重算信息表
	 * 
	 * @param map 重算信息
	 * @throws Exception 
	 */
	public void insertReCalcInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 发送MQ重算消息进行实时重算
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendReCalcMsg(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据业务类型，业务时间取得执行次数
	 * 
	 * @param map 查询条件
	 * @throws Exception 
	 */
	public int getCountByType(Map<String, Object> map);
	
	/**
	 * 验证是否需要重算(Batch处理)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 需要  false: 不需要
	 */
	public boolean needReCalcBatch(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询最后一次引起某一属性变化的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			单据信息
	 */
	public Map<String, Object> getLastChangeInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 查询重算日期信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			重算信息
	 * 
	 */
	public Map<String, Object> getReCalcDateInfo(Map<String, Object> map);
	
	/**
	 * 获取当前 属性值
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	public String getCurNewValueByKbn(CampBaseDTO campBaseDTO);
	
	/**
	 * 取得会员当前的积分信息
	 * @param map
	 * 			查询条件
	 * @return PointDTO
	 * 			会员当前的积分信息
	 */
	public PointDTO getCurMemPointInfo(Map<String, Object> map);
	
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
	public boolean isCurCard(int memberInfoId, String memCard);
	
	/**
	 * 查询有效期开始的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			单据信息
	 */
	public Map<String, Object> getValidStartInfo(CampBaseDTO campBaseDTO);
	
	/**
	 * 取得某个时间点的累计金额
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return double 
	 * 			某个时间点的累计金额
	 * @throws Exception
	 */
	public double getTotalAmountByTime(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得关联单据信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			销售记录
	 * 
	 */
	public Map<String, Object> getRelevantSaleInfo(Map<String, Object> map);
	
	/**
	 * 保存并发送重算MQ
	 * 
	 * @param map
	 * 			查询参数
	 * @throws Exception 
	 * 
	 */
	public void saveAndSendReCalcMsg (Map<String, Object> map) throws Exception;
	
	/**
	 * 取得重算信息记录数(BATCH)
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算信息记录数
	 * 
	 */
	public int getBTReCalcCount(Map<String, Object> map);
	
	/**
	 * 取得会员所属俱乐部列表
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			会员所属俱乐部列表
	 * 
	 */
	public List<Map<String, Object>> getMemClubLevelList(Map<String, Object> map);
	
	/**
	 * 取得当前会员信息(会员俱乐部)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				当前会员信息
	 * @throws CherryDRException 
	 */
	public CampBaseDTO getCurMemberInfoMZ(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询第一次成为正式等级的变更记录
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			第一次成为正式等级的变更记录
	 */
	public Map<String, Object> getFirstFormalLevelInfo(CampBaseDTO campBaseDTO);
}
