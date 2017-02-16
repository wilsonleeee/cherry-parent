/*
 * @(#)BINOLMBRPT14_Action.java     1.0 2017/01/23
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
package com.cherry.mb.rpt.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT14_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT14_Form;
import com.cherry.mb.rpt.service.BINOLMBRPT14_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.FileUtil;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.util.StringUtils;

/**
 * 会员信息完善度报表Action
 *
 * @author nanjunbo
 * @version 1.0 2017/01/23
 */
public class BINOLMBRPT14_Action extends BaseAction implements ModelDriven<BINOLMBRPT14_Form> {

    private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT14_Action.class.getName());

    private List<Map<String, Object>> memberCompleteList;


    @Resource(name = "binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;

    /** 会员销售报表BL */
    @Resource
    private BINOLMBRPT14_BL binOLMBRPT14_BL;


    /**
     * 会员信息完善度报表
     */
    public String init() throws Exception {
        return SUCCESS;
    }

    /**
     * <p>
     * AJAX促销品查询
     * </p>
     *
     * @return
     */
    public String search() throws Exception {
        // 验证提交的参数
        if (!validateSearch()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        //规则名称
        searchMap.put("mobilePhone", form.getMobilePhone());
        searchMap.put("completeStart", form.getCompleteStart());
        searchMap.put("completeEnd", form.getCompleteEnd());
        // 取得规则总数
        int count = binOLMBRPT14_BL.searchMemberCompleteCount(searchMap);
        if (count > 0) {
            // 取得柜台消息List
            memberCompleteList = binOLMBRPT14_BL.searchMemberCompleteList(searchMap);
        }
        // 显示记录
        form.setITotalDisplayRecords(count);
        // 总记录
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return SUCCESS;
    }

    /**
     * 查询参数MAP取得
     *
     * @param
     */
    private Map<String, Object> getSearchMap() {

        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
                .getBIN_OrganizationInfoID());

        // 登陆用户所属品牌ID
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 登陆用户所属品牌Codes
        map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session
                .get(CherryConstants.SESSION_LANGUAGE));

        return map;
    }

    /**
     * 查询校验
     */
    public boolean validateSearch(){
        boolean isCorrect = true;
        //手机号码
        String mobilePhone = form.getMobilePhone();

        if (StringUtils.isEmpty(mobilePhone)&&StringUtils.isEmpty(form.getCompleteStart())&&StringUtils.isEmpty(form.getCompleteEnd())){
            this.addActionError(getText("ECP00038"));
            isCorrect = false;
            return isCorrect;
        }else{
            // 判断手机号不为空时校检手机号是否合法
            if (!CherryChecker.isEmptyString(ConvertUtil.getString(mobilePhone))) {
                if (!CherryChecker.isNumeric(mobilePhone)||mobilePhone.length()!=11){
                    this.addActionError(getText("ECM00008", new String[]{getText("PCP00044")}));
                    isCorrect = false;
                    return isCorrect;
                }
                if (!StringUtils.isEmpty(form.getCompleteStart())&&StringUtils.isEmpty(form.getCompleteEnd())){
                    this.addActionError(getText("ECP00036"));
                    isCorrect = false;
                    return isCorrect;
                }
                if (!StringUtils.isEmpty(form.getCompleteEnd())&&StringUtils.isEmpty(form.getCompleteStart())){
                    this.addActionError(getText("ECP00036"));
                    isCorrect = false;
                    return isCorrect;
                }
            }else{
                if (StringUtils.isEmpty(form.getCompleteStart())||StringUtils.isEmpty(form.getCompleteEnd())){
                    this.addActionError(getText("ECP00036"));
                    isCorrect = false;
                    return isCorrect;
                }
            }
        }
        if (!StringUtils.isEmpty(form.getCompleteStart())||!StringUtils.isEmpty(form.getCompleteEnd())){
            if(!CherryChecker.isNumeric(ConvertUtil.getString(form.getCompleteStart().trim()))||
                    !CherryChecker.isNumeric(ConvertUtil.getString(form.getCompleteEnd().trim()))){
                this.addActionError(getText("ECP00036"));
                isCorrect = false;
                return isCorrect;
            }else{
                if (ConvertUtil.getString(form.getCompleteStart()).length()>3||ConvertUtil.getString(form.getCompleteEnd()).length()>3){
                    this.addActionError(getText("ECP00036"));
                    isCorrect = false;
                    return isCorrect;
                }else{
                    String startNumber =  ConvertUtil.getString(form.getCompleteStart()).trim();
                    String endNumber =  ConvertUtil.getString(form.getCompleteEnd()).trim();

                    // 起始的完整度
                    int completeStart = ConvertUtil.getInt(startNumber);
                    // 结束完整度
                    int completeEnd = ConvertUtil.getInt(endNumber);

                    if (completeStart<0||completeStart>100){
                        this.addActionError(getText("ECP00036"));
                        isCorrect = false;
                        return isCorrect;
                    }
                    if (completeEnd<0||completeEnd>100){
                        this.addActionError(getText("ECP00036"));
                        isCorrect = false;
                        return isCorrect;
                    }
                    if (completeStart>completeEnd){
                        this.addActionError(getText("ECP00037"));
                        isCorrect = false;
                        return isCorrect;
                    }
                }
            }
        }

        return isCorrect;
    }
    /** 会员信息完善度报表Form */
    private BINOLMBRPT14_Form form = new BINOLMBRPT14_Form();

    @Override
    public BINOLMBRPT14_Form getModel() {
        return form;
    }

    public List<Map<String, Object>> getMemberCompleteList() {
        return memberCompleteList;
    }

    public void setMemberCompleteList(List<Map<String, Object>> memberCompleteList) {
        this.memberCompleteList = memberCompleteList;
    }
}
