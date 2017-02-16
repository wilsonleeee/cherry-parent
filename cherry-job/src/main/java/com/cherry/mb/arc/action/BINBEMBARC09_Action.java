/*
 * @(#)BINBEMBTIF01_Action.java     1.0 2015/06/24
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
package com.cherry.mb.arc.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.arc.bl.BINBEMBARC09_BL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;

/**
 * 计算会员完善度处理Action
 *
 * @author nanjunbo
 * @version 1.0 2017/02/09
 */
public class BINBEMBARC09_Action extends BaseAction {

    private static final long serialVersionUID = 773508747863894895L;
    private static Logger logger = LoggerFactory.getLogger(BINBEMBARC09_Action.class.getName());

    /** 计算会员完善度处理BL */
    @Resource
    private BINBEMBARC09_BL binBEMBARC09_BL;



    /**
     * 计算会员完善度处理
     *
     * @param
     * @return String
     * */
    public String binbembarcExec() throws Exception {
        logger.info("******************************计算会员完善度开始***************************");
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
            //所属组织code
            map.put("orgCode",userInfo.getOrgCode());

            flg = binBEMBARC09_BL.tran_MemSync(map);
        } catch (CherryBatchException cbx) {
            flg = CherryBatchConstants.BATCH_WARNING;
        } catch (Exception e) {
            flg = CherryBatchConstants.BATCH_ERROR;
        } finally {
            if(flg == CherryBatchConstants.BATCH_SUCCESS) {
                this.addActionMessage("计算会员完善度正常终了");
                logger.info("******************************计算会员完善度正常终了***************************");
            } else if(flg == CherryBatchConstants.BATCH_WARNING) {
                this.addActionError("计算会员完善度警告终了");
                logger.info("******************************计算会员完善度警告终了***************************");
            } else if(flg == CherryBatchConstants.BATCH_ERROR) {
                this.addActionError("计算会员完善度异常终了");
                logger.info("******************************计算会员完善度异常终了***************************");
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
}
