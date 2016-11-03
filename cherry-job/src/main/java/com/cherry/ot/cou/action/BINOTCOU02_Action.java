/*
 * @(#)BINOTCOU02_Action.java     1.0 2014/11/13
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
package com.cherry.ot.cou.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.cou.interfaces.BINOTCOU02_IF;

/**
 * BATCH薇诺娜优惠劵获取Action
 * 
 * @author menghao
 * @version 2014.11.13
 */
public class BINOTCOU02_Action extends BaseAction {

	private static final long serialVersionUID = -5249786783468297786L;

	private static Logger logger = LoggerFactory
			.getLogger(BINOTCOU02_Action.class.getName());

	@Resource(name = "binOTCOU02_BL")
	private BINOTCOU02_IF binOTCOU02_BL;
	
	 /** 组织Id */
    private String organizationInfoId;
    
    /** 品牌ID */
    private int brandInfoId;
    
    /** 品牌code */
    private String brandCode;

	public String binotcou02Exec() throws Exception {
		logger.info("******************************薇诺娜优惠劵获取开始***************************");
		// 设置batch处理标志
        int flg = CherryBatchConstants.BATCH_SUCCESS;
        try {
        	Map<String, Object> map = new HashMap<String, Object>();
            UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
            // 所属组织
            map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
            //所属品牌
            map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
            // 品牌code
            map.put(CherryBatchConstants.BRAND_CODE, brandCode);
            flg = binOTCOU02_BL.tran_getCouponCode(map);
        } catch(CherryBatchException cbx) {
        	flg = CherryBatchConstants.BATCH_WARNING;
            logger.info("=============WARN MSG================");
            logger.info(cbx.getMessage());
            logger.info("=====================================");
        } catch(Exception e) {
        	 flg = CherryBatchConstants.BATCH_ERROR;
             logger.error("=============ERROR MSG===============");
             logger.error(e.getMessage(),e);
             logger.error("=====================================");
        } finally {
        	if (flg == CherryBatchConstants.BATCH_SUCCESS) {
                this.addActionMessage("薇诺娜优惠券获取开始正常终了");
                logger.info("******************************薇诺娜优惠券获取正常终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_WARNING) {
                this.addActionError("薇诺娜优惠券获取开始警告终了");
                logger.info("******************************薇诺娜优惠券获取开始警告终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_ERROR) {
                this.addActionError("薇诺娜优惠券获取开始异常终了");
                logger.info("******************************薇诺娜优惠券获取开始异常终了***************************");
            }
        }
		return "DOBATCHRESULT";
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	

}
