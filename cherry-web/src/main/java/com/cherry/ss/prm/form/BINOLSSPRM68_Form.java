/*
 * @(#)BINOLSSPRM73_Form.java     1.0 2016/03/28
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
package com.cherry.ss.prm.form;

import com.cherry.cm.form.DataTable_BaseForm;
import java.io.File;
import java.util.List;
import java.util.Map;

public class BINOLSSPRM68_Form extends DataTable_BaseForm{

    /** 品牌ID */
    private String brandInfoId;

    /** 上传的文件 */
    private File upExcel;

    /** 导入模式 */
    private String upMode;

    /** 导入批次号 */
    private String searchCode;

    /** 导入批次号 */
    private String searchCodeBlack;

    /** 黑名单白名单区分 */
    private String filterType;

    /** 活动code */
    private String activityCode;

    /**黑白名单柜台list */
    private String counterList;

    /** 区分导入柜台，产品，活动 */
    private String operateType;
    /** 失败结果 */
    private List<Map<String, Object>> failList;

    /** 购物车已导入数据 */
    private String ruleCondProduct;

    /**区分产品导入类型*/
    private String execLoadType;

    /**产品奖励**/
    private String excelProductAward ;

    /**购物车产品*/
    private String excelProductShopping;

    /**原产品界面该产品类型的总共数量*/
    private  int productPageSize;

    public int getProductPageSize() {
        return productPageSize;
    }

    public void setProductPageSize(int productPageSize) {
        this.productPageSize = productPageSize;
    }

    public String getExcelProductShopping() {
        return excelProductShopping;
    }

    public void setExcelProductShopping(String excelProductShopping) {
        this.excelProductShopping = excelProductShopping;
    }

    public String getExcelProductAward() {
        return excelProductAward;
    }

    public void setExcelProductAward(String excelProductAward) {
        this.excelProductAward = excelProductAward;
    }

    public String getExecLoadType() {
        return execLoadType;
    }

    public void setExecLoadType(String execLoadType) {
        this.execLoadType = execLoadType;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    private String groupType;

    public List<Map<String, Object>> getFailList() {
        return failList;
    }

    public void setFailList(List<Map<String, Object>> failList) {
        this.failList = failList;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public File getUpExcel() {
        return upExcel;
    }

    public void setUpExcel(File upExcel) {
        this.upExcel = upExcel;
    }

    public String getUpMode() {
        return upMode;
    }

    public void setUpMode(String upMode) {
        this.upMode = upMode;
    }

    public String getCounterList() {
        return counterList;
    }

    public void setCounterList(String counterList) {
        this.counterList = counterList;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
    public String getSearchCodeBlack() {return searchCodeBlack;}

    public void setSearchCodeBlack(String searchCodeBlack) {this.searchCodeBlack = searchCodeBlack;}

    public String getRuleCondProduct() {
        return ruleCondProduct;
    }

    public void setRuleCondProduct(String ruleCondProduct) {
        this.ruleCondProduct = ruleCondProduct;
    }
}
