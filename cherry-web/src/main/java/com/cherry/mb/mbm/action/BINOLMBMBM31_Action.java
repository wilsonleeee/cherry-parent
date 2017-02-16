
package com.cherry.mb.mbm.action;

import java.util.*;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.MapBuilder;
import com.cherry.mb.mbm.bl.BINOLMBMBM31_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM31_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BINOLMBMBM31_Action extends BaseAction implements
        ModelDriven<BINOLMBMBM31_Form>{


    private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM31_Action.class);
    @Resource
    private BINOLMBMBM31_BL binOLMBMBM31_BL;

    @Resource
    private BINOLCM05_BL binOLCM05_BL;

    /** 参数FORM */
    private BINOLMBMBM31_Form form = new BINOLMBMBM31_Form();

    private List<Map<String, Object>> brandInfoList;

    private String memberAttrList;

    private List<Map<String, Object>> ruleList;

    public List<Map<String, Object>> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Map<String, Object>> ruleList) {
        this.ruleList = ruleList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public String getMemberAttrList() {
        return memberAttrList;
    }

    public void setMemberAttrList(String memberAttrList) {
        this.memberAttrList = memberAttrList;
    }

    /**
     * <p>
     * 画面初期显示
     * </p>
     * @return String 跳转页面
     * @throws JSONException
     *
     */
    public String init() throws JSONException {
        return SUCCESS;
    }

    /**
     * 查询参数MAP取得
     *
     */
    private Map<String, Object> getSearchMap() {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 所属组织
        map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));

        return map;
    }

    /**
     * <p>
     *规则一览
     * </p>
     *
     * @return
     */
    public String search() throws Exception {
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        //规则名称
        searchMap.put("startTime", form.getStartTime());
        searchMap.put("endTime", form.getEndTime());
        searchMap.put("ruleName", form.getRuleName().trim());
        // 取得规则总数
        int count = binOLMBMBM31_BL.searchRuleCount(searchMap);
        if (count > 0) {
            // 取得柜台消息List
            ruleList = binOLMBMBM31_BL.searchRuleList(searchMap);
        }
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return SUCCESS;
    }

    /**
     * 新增规则页面初始化
     * @return
     * @throws JSONException
     */
    public String addInit() throws JSONException {
        Map<String, Object> map  = getSearchMap();
        memberAttrList = JSONUtil.serialize(binOLMBMBM31_BL.searchMemberAttrList(map));
        return SUCCESS;
    }



    /**
     * 逻辑删除竞争对手
     * @return
     * @throws Exception
     */
    public void deleteRule() throws Exception {
        try {
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            // 参数MAP
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("endTime",form.getEndTime());
            map.put("startTime",form.getStartTime());
            map.put("completeDegreeRuleID",form.getCompleteDegreeRuleID());
            // 更新者
            map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
            // 更新程序名
            map.put(CherryConstants.UPDATEPGM, "BINOLMBMB31");
            binOLMBMBM31_BL.tran_deleteRule(map);
            ConvertUtil.setResponseByAjax(response, MapBuilder.newInstance().put("resultCode","0").build());
        } catch (Exception e) {
            logger.error("删除会员完整度规则失败",e);
            ConvertUtil.setResponseByAjax(response, MapBuilder.newInstance().put("resultCode","-1").put("resultMsg","删除失败").build());
        }
    }

    @Override
    public BINOLMBMBM31_Form getModel() {
        return form;
    }


    /**
     * 保存会员完整度规则
     * @return
     * @throws Exception
     */
    public void saveIsMemCompleteRule() throws Exception {
        try {
            // 参数MAP
            Map<String, Object> map = new HashMap<String, Object>();
            // 用户信息
            UserInfo userInfo = (UserInfo) session
                    .get(CherryConstants.SESSION_USERINFO);
            // 所属组织
            map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
            map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
            // 作成者
            map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
            // 作成程序名
            map.put(CherryConstants.CREATEPGM, "BINOLMBMBM31");
            // 更新者
            map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
            // 更新程序名
            map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM31");
            Map<String,Object> result_map=binOLMBMBM31_BL.checkSaveMemCompleteRuleParam(form,map);
            if(result_map != null){
                ConvertUtil.setResponseByAjax(response, result_map);
                return;
            }
            binOLMBMBM31_BL.tran_saveMemCompleteRule(map);
            ConvertUtil.setResponseByAjax(response, MapBuilder.newInstance().put("resultCode","0").build());
        } catch (Exception e) {
            logger.error("保存会员完整度规则失败",e);
            ConvertUtil.setResponseByAjax(response, MapBuilder.newInstance().put("resultCode","-1").put("resultMsg","保存失败").build());
        }
    }

    /***
     * 获得规则结果详细信息
     * @return
     * @throws Exception
     */
    public String getRuleDetail()throws Exception{
        // 取得参数MAP
        Map<String, Object> paramMap = getSearchMap();
        paramMap.put("completeDegreeRuleID",form.getCompleteDegreeRuleID());
        List<Map<String,Object>> rule_list= binOLMBMBM31_BL.getRuleListWithoutPage(paramMap);
        Map<String,Object> rule_map=new HashMap<String, Object>();
        if(rule_list != null && rule_list.size() > 0){
            rule_map=rule_list.get(0);
        }
        String ruleConditionJson=ConvertUtil.getString(rule_map.get("ruleCondition"));
        if( !"".equals(ruleConditionJson)){
            //对格式进行页面显示转换
            List<Map<String,Object>> attrList= binOLMBMBM31_BL.searchMemberAttrList(paramMap);
            List<Map<String,Object>> ruleConditionList = binOLMBMBM31_BL.convertRuleCondition(ruleConditionJson,attrList);
            form.setRuleConditionList(ruleConditionList);
        }


        form.setRule_map(rule_map);

        return SUCCESS;
    }

    public String initEditMemComplete() throws Exception {
        // 取得参数MAP
        Map<String, Object> map = getSearchMap();
        Map<String,Object> param_map=MapBuilder.newInstance().build();
        param_map.putAll(map);
        param_map.put("completeDegreeRuleID",form.getCompleteDegreeRuleID());
        List<Map<String,Object>> rule_list =binOLMBMBM31_BL.getRuleListWithoutPage(param_map);
        Map<String,Object> rule_info=rule_list.get(0);
        String startTime=ConvertUtil.getString(rule_info.get("startTime"))+" 00:00:00";
        String endTime=ConvertUtil.getString(rule_info.get("endTime"))+" 23:59:59";
        String sysTime= CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);
        String ruleConditionJson=ConvertUtil.getString(rule_info.get("ruleCondition"));
        memberAttrList = JSONUtil.serialize(binOLMBMBM31_BL.searchMemberAttrList(param_map));
        if( !"".equals(ruleConditionJson)){
            //对格式进行页面显示转换
            List<Map<String,Object>> attrList= binOLMBMBM31_BL.searchMemberAttrList(param_map);
            List<Map<String,Object>> ruleConditionList = binOLMBMBM31_BL.convertRuleCondition(ruleConditionJson,attrList);
            String ruleConditionListJson=CherryUtil.obj2Json(ruleConditionList);
            form.setRuleConditionListJson(ruleConditionListJson);
            form.setRuleConditionList(ruleConditionList);
        }
        form.setRule_map(rule_info);

        if(CherryChecker.compareDate(sysTime, startTime) >= 0 && CherryChecker.compareDate(endTime, sysTime) >= 0 ){
            //规则已经开始，不可编辑页面,只能延长时间
            return "BINOLMBMBM31_05";
        }else{
            //规则没有开始，可以任意编辑
            return "BINOLMBMBM31_04";
        }
    }


    public void editMemCompleteRuleParam() throws Exception {
        try{
            // 取得参数MAP
            Map<String, Object> map = getSearchMap();
            Map<String,Object> param_map=MapBuilder.newInstance().build();
            param_map.putAll(map);
            param_map.put("completeDegreeRuleID",form.getCompleteDegreeRuleID());
//            param_map.put("exceptCompleteDegreeRuleID",form.getCompleteDegreeRuleID());
            param_map.put("endTimeSave",form.getEndTimeSave());
            Map<String,Object> result_map=binOLMBMBM31_BL.tran_editMemCompleteRule(form,param_map);
            ConvertUtil.setResponseByAjax(response,result_map);
        } catch (Exception e) {
            logger.error("修改会员完整度规则失败",e);
            ConvertUtil.setResponseByAjax(response, MapBuilder.newInstance().put("resultCode","-1").put("resultMsg","保存失败").build());
        }
    }




}
