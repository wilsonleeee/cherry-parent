/*	
 * @(#)BINBEMBLEL02_Action.java     1.0 2014/03/21		
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
package com.cherry.mb.lel.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryChecker;
import com.cherry.mb.lel.bl.BINBEMBLEL02_BL;

/**
 * 推算等级变化明细(雅漾)处理Action
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL02_Action extends BaseAction{

	private static final long serialVersionUID = -7075187394497295407L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBLEL02_Action.class.getName());
	
	/** 推算等级变化明细(雅漾)处理BL */
	@Resource
	private BINBEMBLEL02_BL binBEMBLEL02_BL;
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 推算等级变化明细处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbemblelExec() throws Exception {
		logger.info("******************************计算某个时间点的会员等级处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 0)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 计算某个时间点的会员等级处理
			flg = binBEMBLEL02_BL.tran_CalcLevel(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("计算某个时间点的会员等级处理正常终了");
					logger.info("******************************计算某个时间点的会员等级处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("计算某个时间点的会员等级处理警告终了");
					logger.info("******************************计算某个时间点的会员等级处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("计算某个时间点的会员等级处理异常终了");
					logger.info("******************************计算某个时间点的会员等级处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 推算等级变化明细处理
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembDetailExec() throws Exception {
		logger.info("******************************会员等级变化明细计算处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		boolean checkFlag = true;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 参数验证
			if (!checkParams(map, 1)) {
				checkFlag = false;
				return "DOBATCHRESULT";
			}
			// 推算等级变化明细处理
			flg = binBEMBLEL02_BL.tran_LevelDetail(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(),cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (checkFlag) {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("会员等级变化明细计算处理正常终了");
					logger.info("******************************会员等级变化明细计算处理正常终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("会员等级变化明细计算处理警告终了");
					logger.info("******************************会员等级变化明细计算处理警告终了***************************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("会员等级变化明细计算处理异常终了");
					logger.info("******************************会员等级变化明细计算处理异常终了***************************");
				}
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 参数验证
	 * 
	 * @param map
	 * 			参数集合
	 * @param flag
	 * 			参数区分 0: 等级计算   1: 明细生成
	 * @return boolean
	 * 			true: 参数正确  false: 参数有误
	 * */
	public boolean checkParams(Map<String, Object> map, int flag) throws Exception {
		// 开始会员ID
		memIdStr = memIdStr.trim();
		// 结束会员ID
		memIdEnd = memIdEnd.trim();
		// 数字验证
		if (!CherryChecker.isNullOrEmpty(memIdStr) 
				&& !CherryChecker.isNumeric(memIdStr)) {
			this.addActionError("开始会员ID不是数字！");
			return false;
		} else if (!CherryChecker.isNullOrEmpty(memIdEnd, true) 
				&& !CherryChecker.isNumeric(memIdEnd)) {
			this.addActionError("结束会员ID不是数字！");
			return false;
		}
		if (!"".equals(memIdStr)) {
			map.put("memIdStr", memIdStr);
		}
		if (!"".equals(memIdEnd)) {
			map.put("memIdEnd", memIdEnd);
		}
		map.put("rangeKbn", rangeKbn);
		//  检查是否需要继续执行
		if ("0".equals(rangeKbn)) {
			if (!binBEMBLEL02_BL.checkExec(map)) {
				this.addActionError("上一次BATCH执行还未结束！请待完全结束后再执行！");
				return false;
			}
			if (CherryChecker.isNullOrEmpty(joinLimit)) {
				this.addActionError("入会日期不能为空！请以期末时间作为入会时间上限！");
				return false;
			} else if (!CherryChecker.checkDate(joinLimit)){
				this.addActionError("入会日期格式不正确！");
				return false;
			}
			map.put("joinDateLimit", joinLimit);
		}
		// 等级计算参数验证
		if (flag == 0) {
			// 等级日期
			levelDate = levelDate.trim();
			if (CherryChecker.isNullOrEmpty(levelDate)) {
				this.addActionError("等级日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(levelDate)){
				this.addActionError("等级日期格式不正确！");
				return false;
			}
			map.put("endDate", levelDate);
		} else {
			// 期初
			beginDate = beginDate.trim();
			// 期末
			endDate = endDate.trim();
			if (CherryChecker.isNullOrEmpty(beginDate)) {
				this.addActionError("期初日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(beginDate)){
				this.addActionError("期初日期格式不正确！");
				return false;
			}
			if (CherryChecker.isNullOrEmpty(endDate)) {
				this.addActionError("期末日期不能为空！");
				return false;
			} else if (!CherryChecker.checkDate(endDate)){
				this.addActionError("期末日期格式不正确！");
				return false;
			}
			map.put("beginDate", beginDate);
			map.put("endDate", endDate);
		}
		if (null != memFlag && !"".equals(memFlag)) {
			map.put("memFlag", memFlag);
		}
		if (!"".equals(dateKbn)) {
			map.put("dateKbn", dateKbn);
		}
		return true;
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 范围区分 */
	private String rangeKbn;
	
	/** 开始会员ID */
	private String memIdStr;
	
	/** 结束会员ID */
	private String memIdEnd;
	
	/** 入会时间 */
	private String joinLimit;
	
	/** 会员标识 */
	private String memFlag;
	
	/** 等级日期 */
	private String levelDate;
	
	/** 类别 */
	private String dateKbn;
	
	/** 期初 */
	private String beginDate;
	
	/** 期末 */
	private String endDate;
	
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	
	public String getRangeKbn() {
		return rangeKbn;
	}

	public void setRangeKbn(String rangeKbn) {
		this.rangeKbn = rangeKbn;
	}

	public String getMemIdStr() {
		return memIdStr;
	}

	public void setMemIdStr(String memIdStr) {
		this.memIdStr = memIdStr;
	}

	public String getMemIdEnd() {
		return memIdEnd;
	}

	public void setMemIdEnd(String memIdEnd) {
		this.memIdEnd = memIdEnd;
	}

	public String getLevelDate() {
		return levelDate;
	}

	public void setLevelDate(String levelDate) {
		this.levelDate = levelDate;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMemFlag() {
		return memFlag;
	}

	public void setMemFlag(String memFlag) {
		this.memFlag = memFlag;
	}

	public String getDateKbn() {
		return dateKbn;
	}

	public void setDateKbn(String dateKbn) {
		this.dateKbn = dateKbn;
	}

	public String getJoinLimit() {
		return joinLimit;
	}

	public void setJoinLimit(String joinLimit) {
		this.joinLimit = joinLimit;
	}
}
