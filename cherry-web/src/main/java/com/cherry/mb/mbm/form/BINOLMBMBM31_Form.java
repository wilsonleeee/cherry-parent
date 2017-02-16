package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

import java.util.List;
import java.util.Map;

public class BINOLMBMBM31_Form extends DataTable_BaseForm {
    /**规则名称*/
    private String ruleName;
    /**规则开始时间*/
    private String startTime;
    /**规则结束时间*/
    private String endTime;
    /**规则名称*/
    private String ruleNameSave;
    /**开始时间*/
    private String startTimeSave;
    /**截止时间*/
    private String endTimeSave;
    /**总积分*/
    private String totalPointSave;
    /**备注*/
    private String memoSave;
    /**规则Json*/
    private String ruleJsonSave;
    /**规则ID*/
    private String completeDegreeRuleID;
    /**规则明细信息*/
    private Map<String,Object> rule_map;

    private List<Map<String,Object>> ruleConditionList;

    private String ruleConditionListJson;

    private String newRuleFlag;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRuleNameSave() {
        return ruleNameSave;
    }

    public void setRuleNameSave(String ruleNameSave) {
        this.ruleNameSave = ruleNameSave;
    }

    public String getStartTimeSave() {
        return startTimeSave;
    }

    public void setStartTimeSave(String startTimeSave) {
        this.startTimeSave = startTimeSave;
    }

    public String getEndTimeSave() {
        return endTimeSave;
    }

    public void setEndTimeSave(String endTimeSave) {
        this.endTimeSave = endTimeSave;
    }

    public String getTotalPointSave() {
        return totalPointSave;
    }

    public void setTotalPointSave(String totalPointSave) {
        this.totalPointSave = totalPointSave;
    }

    public String getMemoSave() {
        return memoSave;
    }

    public void setMemoSave(String memoSave) {
        this.memoSave = memoSave;
    }

    public String getRuleJsonSave() {
        return ruleJsonSave;
    }

    public void setRuleJsonSave(String ruleJsonSave) {
        this.ruleJsonSave = ruleJsonSave;
    }

    public String getCompleteDegreeRuleID() {
        return completeDegreeRuleID;
    }

    public void setCompleteDegreeRuleID(String completeDegreeRuleID) {
        this.completeDegreeRuleID = completeDegreeRuleID;
    }

    public Map<String, Object> getRule_map() {
        return rule_map;
    }

    public void setRule_map(Map<String, Object> rule_map) {
        this.rule_map = rule_map;
    }

    public List<Map<String, Object>> getRuleConditionList() {
        return ruleConditionList;
    }

    public void setRuleConditionList(List<Map<String, Object>> ruleConditionList) {
        this.ruleConditionList = ruleConditionList;
    }

    public String getRuleConditionListJson() {
        return ruleConditionListJson;
    }

    public void setRuleConditionListJson(String ruleConditionListJson) {
        this.ruleConditionListJson = ruleConditionListJson;
    }

    public String getNewRuleFlag() {
        return newRuleFlag;
    }

    public void setNewRuleFlag(String newRuleFlag) {
        this.newRuleFlag = newRuleFlag;
    }
}
