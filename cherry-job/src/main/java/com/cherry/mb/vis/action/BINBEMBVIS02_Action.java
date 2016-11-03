/*	
 * @(#)BINBEMBVIS02_Action.java     1.0 @2012-12-14		
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
package com.cherry.mb.vis.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.vis.bl.BINBEMBVIS02_BL;

/**
 *
 * 会员回访任务生成
 *
 * @author jijw
 *
 * @version  2012-12-14
 */
public class BINBEMBVIS02_Action extends BaseAction {

	private static final long serialVersionUID = 1036396252366799988L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS02_Action.class.getName());
	
	@Resource(name="binBEMBVIS02_BL")
	private BINBEMBVIS02_BL binBEMBVIS02_BL;

	/**
	 * 画面初期显示
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 会员回访任务生成exec
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembvis02Exec() throws Exception {
		
		logger.info("******************************会员回访任务生成BATCH处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
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
			// 查找N天后生日的会员
			map.put("AHEAD_DAY", AHEAD_DAY);
			// 任务的开始时间
			map.put("START_DAY", START_DAY);
			// 任务的结束时间
			map.put("END_DAY", END_DAY);
			// 绑定回访问卷ID
			map.put("PAPERID", PAPERID);
			// 会员同步BATCH处理
			flg = binBEMBVIS02_BL.tran_batchMemVistTask(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员回访任务生成BATCH处理正常终了");
				logger.info("******************************会员回访任务生成BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员回访任务生成BATCH处理警告终了");
				logger.info("******************************会员回访任务生成BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员回访任务生成BATCH处理异常终了");
				logger.info("******************************会员回访任务生成BATCH处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 查找N天后生日的会员 */
	private String AHEAD_DAY;
	
	/** 任务的开始时间:以会员生日为基准，提前X天开始，注意X不限正负  **/
	private String START_DAY;
	
	/** 任务的结束时间:以会员生日为基准，提前Y天结束，注意Y不限正负  **/
	private String END_DAY;
	
	/** 绑定回访问卷ID  **/
	private String PAPERID;

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
	
	public String getAHEAD_DAY() {
		return AHEAD_DAY;
	}

	public void setAHEAD_DAY(String aHEAD_DAY) {
		AHEAD_DAY = aHEAD_DAY;
	}

	public String getSTART_DAY() {
		return START_DAY;
	}

	public void setSTART_DAY(String sTART_DAY) {
		START_DAY = sTART_DAY;
	}

	public String getEND_DAY() {
		return END_DAY;
	}

	public void setEND_DAY(String eND_DAY) {
		END_DAY = eND_DAY;
	}

	public String getPAPERID() {
		return PAPERID;
	}

	public void setPAPERID(String pAPERID) {
		PAPERID = pAPERID;
	}
	
}
