/*
 * @(#)BINOTHONG01_Action.java     1.0 2014/09/04
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
package com.cherry.ot.hong.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.hong.interfaces.BINOTHONG01_IF;
import com.cherry.ot.hong.interfaces.BINOTHONG02_IF;

/**
 * 
 * 宏巍电商订单获取 ACTION
 * 
 * 处理没有产品的的订单的订单
 * 
 * @author jjw
 * @version 1.0 2015.12.04
 */
public class BINOTHONG02_Action extends BaseAction {

    private static final long serialVersionUID = 9083499603076886411L;

    @Resource(name="binOTHONG02_BL")
    private BINOTHONG02_IF binOTHONG02_BL;

    private static Logger logger = LoggerFactory.getLogger(BINOTHONG02_Action.class.getName());
    
    /** 组织Id */
    private String organizationInfoId;
    
    /** 品牌ID */
    private int brandInfoId;
    
    /** 品牌code */
    private String brandCode;
    
    /**
     * 宏巍电商订单获取
     * @param 无
     * @return String
     * 
     */
    public String binothong02Exec() throws Exception {
        logger.info("******************************宏巍电商订单(HONG02)获取开始***************************");
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
            
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			
            flg = binOTHONG02_BL.tran_batchOTHONG(map);
        } catch (CherryBatchException cbx) {
            flg = CherryBatchConstants.BATCH_WARNING;
            logger.info("=============WARN MSG================");
            logger.info(cbx.getMessage());
            logger.info("=====================================");
        } catch (Exception e) {
            flg = CherryBatchConstants.BATCH_ERROR;
            logger.error("=============ERROR MSG===============");
            logger.error(e.getMessage(),e);
            logger.error("=====================================");
        } finally {
            if (flg == CherryBatchConstants.BATCH_SUCCESS) {
                this.addActionMessage("宏巍电商订单(HONG02)获取开始正常终了");
                logger.info("******************************宏巍电商订单(HONG02)获取开始正常终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_WARNING) {
                this.addActionError("宏巍电商订单(HONG02)获取开始警告终了");
                logger.info("******************************宏巍电商订单(HONG02)获取开始警告终了***************************");
            } else if (flg == CherryBatchConstants.BATCH_ERROR) {
                this.addActionError("宏巍电商订单(HONG02)获取开始异常终了");
                logger.info("******************************宏巍电商订单(HONG02)获取开始异常终了***************************");
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