/*
 * @(#)BINOLMBRPT01_BL.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT14_Service;

/**
 * 会员信息完善度报表BL
 *
 * @author nanjunbo
 * @version 1.0 2017/01/23
 */
public class BINOLMBRPT14_BL {

    /** 会员信息完善度报表Service **/
    @Resource
    private BINOLMBRPT14_Service binOLMBRPT14_Service;
    /**
     * 取得会员完善度信息总数
     *
     * @param map
     * @return
     */
    public int searchMemberCompleteCount(Map<String, Object> map) {
        return binOLMBRPT14_Service.searchMemberCompleteCount(map);
    }
    /**
     * 取得会员完善度信息List
     *
     * @param map
     * @return
     */
    public List searchMemberCompleteList(Map<String, Object> map) {
        return binOLMBRPT14_Service.searchMemberCompleteList(map);
    }

}
