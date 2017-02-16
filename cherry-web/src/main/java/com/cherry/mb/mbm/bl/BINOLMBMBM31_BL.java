
package com.cherry.mb.mbm.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.MapBuilder;
import com.cherry.mb.mbm.form.BINOLMBMBM31_Form;
import com.cherry.mb.mbm.service.BINOLMBMBM31_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@SuppressWarnings("unchecked")
public class BINOLMBMBM31_BL extends SsBaseBussinessLogic{
    @Resource
    private BINOLMBMBM31_Service binolmbmbm31_service;

    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;

    /**
     * 取得规则总数
     *
     * @param map
     * @return
     */
    public int searchRuleCount(Map<String, Object> map) {
        return binolmbmbm31_service.getRuleCount(map);
    }
    /**
     * 取得规则List
     *
     * @param map
     * @return
     */
    public List searchRuleList(Map<String, Object> map) {
        return binolmbmbm31_service.getRuleList(map);
    }


    public List searchMemberAttrList(Map<String, Object> map) {
        return binolmbmbm31_service.searchMemberAttrList(map);
    }

    public List getRuleListWithoutPage(Map<String, Object> map) {
        return binolmbmbm31_service.getRuleListWithoutPage(map);
    }



    /**
     * 删除竞争对手
     * @param map
     * @throws Exception
     */

    public void tran_deleteRule(Map<String, Object> map) throws Exception {
//        String startTime=ConvertUtil.getString(map.get("startTime"));
//        String endTime=ConvertUtil.getString(map.get("endTime"));
//        String sysTime= CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);
//        //当该规则还在执行时停用，将该规则的截止时间置为当前时间
//        if (CherryChecker.compareDate(startTime, sysTime) < 0&&CherryChecker.compareDate(endTime, sysTime) > 0){
//            binolmbmbm31_service.updateRuleEndTime(map);
//        }else if (CherryChecker.compareDate(startTime, sysTime) > 0){
            //当该规则还未启用，将该规则删除
            binolmbmbm31_service.deleteRule(map);
//        }
    }


    public void tran_saveMemCompleteRule(Map<String, Object> map){
        binolmbmbm31_service.saveMemCompleteRule(map);
    }

    public List<Map<String,Object>> convertRuleCondition(String json,List<Map<String,Object>> attrList){
        List<Map<String,Object>> result_list=new ArrayList<Map<String, Object>>();
        Map<String,Object> param_map=ConvertUtil.json2Map(json);
        List<Map<String,Object>> pointGroup=(List<Map<String,Object>>)param_map.get("pointGroup");
        List<Map<String,Object>> noProintGroup=(List<Map<String,Object>>)param_map.get("noPointGroup");
        for(Map<String,Object> pointGroupInfo:pointGroup){
            for (Map.Entry<String, Object> entry : pointGroupInfo.entrySet()) {
                Map<String,Object> unitInfo=new HashMap<String, Object>();
                String key=entry.getKey();
                Map<String,Object> convert_map=(Map<String,Object>)entry.getValue();
                if("".equals(ConvertUtil.getString(convert_map.get("point")))){
                    convert_map.put("point","");
                }
                unitInfo.putAll(convert_map);
                unitInfo.put("keyName",key);
                result_list.add(unitInfo);
            }
        }

        for(Map<String,Object> noPpointGroupInfo:noProintGroup){
            for (Map.Entry<String, Object> entry : noPpointGroupInfo.entrySet()) {
                Map<String,Object> unitInfo=new HashMap<String, Object>();
                String key=entry.getKey();
                Map<String,Object> convert_map=(Map<String,Object>)entry.getValue();
                if("".equals(ConvertUtil.getString(convert_map.get("point")))){
                    convert_map.put("point","");
                }
                unitInfo.putAll(convert_map);
                unitInfo.put("keyName",key);
                result_list.add(unitInfo);
            }
        }

        for(Map<String,Object> result_map:result_list){
            String keyName=ConvertUtil.getString(result_map.get("keyName")).trim();
            if("".equals(keyName)){
                continue;
            }
            for(Map<String,Object> attr_map:attrList){
                String memInfoKey=ConvertUtil.getString(attr_map.get("memInfoKey")).trim();
                String memInfoValue=ConvertUtil.getString(attr_map.get("memInfoValue")).trim();
                if(keyName.equals(memInfoKey)){
                    result_map.put("valueName",memInfoValue);
                }
            }
        }



        return result_list;
    }

    public Map<String,Object> tran_editMemCompleteRule(BINOLMBMBM31_Form form,Map<String,Object> param) throws Exception {
        //分为两种情况
        //（1）系统时间在该修改规则的有效期内（规则已经生效）-- 只能延长规则的截止时间并且需要与数据库中其他规则(排除自身)做时间区间的比较
        //（2）系统时间在该修改规则的有效期之外（规则还没有生效）-- 都可以进行修改并且需要与数据库中其他规则(排除自身)做时间区间的比较
        Map<String,Object> result_map=new HashMap<String, Object>();
        //获取规则info
        List<Map<String,Object>> rule_list =this.getRuleListWithoutPage(param);
        if(rule_list != null && rule_list.size()>0){
            Map<String,Object> rule_info=rule_list.get(0);
            String startTime=ConvertUtil.getString(rule_info.get("startTime"));
//            String endTime=ConvertUtil.getString(rule_info.get("endTime"));
            String sysTime= CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);
            String endTimeSave=ConvertUtil.getString(param.get("endTimeSave"));
            if(CherryChecker.compareDate(startTime, sysTime) < 0){
                String minJoinDate = binOLCM14_BL.getConfigValue("1402", ConvertUtil.getString(param.get("organizationInfoId")),
                        ConvertUtil.getString(param.get("brandInfoId")));
                if (StringUtils.isEmpty(minJoinDate)){
                    result_map.put("resultCode",-1);
                    result_map.put("resultMsg","请配置计算完善度最早入会日期限定此系统配置项");
                    return result_map;
                }
                if (!CherryChecker.checkDate(minJoinDate,DateUtil.DATE_PATTERN)){
                    result_map.put("resultCode",-1);
                    result_map.put("resultMsg","您配置的计算完善度最早入会日期限定的值不符合格式");
                    return result_map;
                }

                //系统时间在该规则的时间区间范围内,为上述（1）的情况，只允许修改延长截止时间（设定的截止时间要大于当前规则设定的截止时间）可以提前至今日
                if(CherryChecker.compareDate(endTimeSave+" 23:59:59", sysTime) < 0){
                    result_map.put("resultCode",-1);
                    result_map.put("resultMsg","已经生效的规则，只能将结束时间修改为当天或延后结束时间");
                    return result_map;
                }
                Map<String,Object> all_param=new HashMap<String, Object>();
                all_param.putAll(param);
                all_param.put("exceptCompleteDegreeRuleID",all_param.get("completeDegreeRuleID"));
                all_param.remove("completeDegreeRuleID");
                List<Map<String,Object>> ruleAll_list =this.getRuleListWithoutPage(all_param);
                for(Map<String,Object> rule:ruleAll_list){
                    String startTime_data = ConvertUtil.getString(rule.get("startTime"));
                    String endTime_data = ConvertUtil.getString(rule.get("endTime"));
                    // 判断不交叉的两种情况取反即为交叉情景
                    if (!(DateUtil.compareDate(startTime_data, endTimeSave) > 0 || DateUtil.compareDate(endTime_data, startTime) < 0)) {
                        result_map.put("resultCode",-1);
                        result_map.put("resultMsg","您填写的时间区间与已设置的活动时间存在冲突");
                        return result_map;
                    }
                }
                //校验通过更新操作
                param.put("updateAll","0");
                binolmbmbm31_service.updateMemCompleteRule(param);
            }else{
                //系统时间不在该规则的时间区间范围内,为上述（2）的情况
                Map<String,Object> result= this.checkSaveMemCompleteRuleParam(form, param);
                if(result == null){
                    //校验通过更新操作
                    param.put("updateAll","1");
                    binolmbmbm31_service.updateMemCompleteRule(param);
                }else{
                    return result;
                }
            }
        }else{
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","没有查询到相关规则信息");
            return result_map;
        }
        result_map.put("resultCode",0);
        return result_map;
    }


    /**
     * 校验方法（newRuleFlag为0表示新增规则校验）
     * @param form
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String,Object> checkSaveMemCompleteRuleParam(BINOLMBMBM31_Form form, Map<String,Object> map) throws Exception {
        Map<String,Object> result_map=new HashMap<String, Object>();
        String sysDate=CherryUtil.getSysDateTime(DateUtil.DATE_PATTERN);

        String minJoinDate = binOLCM14_BL.getConfigValue("1402", ConvertUtil.getString(map.get("organizationInfoId")), ConvertUtil.getString(map.get("brandInfoId")));
        if (StringUtils.isEmpty(minJoinDate)){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","请配置计算完善度最早入会日期限定此系统配置项");
            return result_map;
        }
        if (!CherryChecker.checkDate(minJoinDate,DateUtil.DATE_PATTERN)){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您配置的计算完善度最早入会日期限定的值不符合格式");
            return result_map;
        }


        //规则名称校验
        if(CherryChecker.isNullOrEmpty(form.getRuleNameSave())){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的规则名称为空");
            return result_map;
        }
        String startTimeSave = form.getStartTimeSave();
        String endTimeSave = form.getEndTimeSave();
        if(CherryChecker.isNullOrEmpty(startTimeSave)){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的规则开始时间为空");
            return result_map;
        }
        if(CherryChecker.isNullOrEmpty(endTimeSave)){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的规则截止时间为空");
            return result_map;
        }

        if (CherryChecker.compareDate(startTimeSave, sysDate) <= 0) {
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的开始时间必须大于当前系统时间");
            return result_map;
        }

        // 日期比较验证
        if (CherryChecker.compareDate(startTimeSave, endTimeSave) > 0) {
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的截止时间必须大于或等于开始时间");
            return result_map;
        }

        //总积分校验
        if(CherryChecker.isNullOrEmpty(form.getTotalPointSave())){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的总积分为空");
            return result_map;
        }
        if (ConvertUtil.getString(form.getTotalPointSave()).length()>6){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","输入的总积分长度不能大于6位");
            return result_map;
        }

        //Json相关校验
        if(CherryChecker.isNullOrEmpty(form.getRuleJsonSave())){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","您输入的积分明细为空");
            return result_map;
        }

        Map<String, Object> detail_map = CherryUtil.json2Map(form.getRuleJsonSave());
        List<Map<String,Object>> pointGroup= (List<Map<String,Object>>)detail_map.get("pointGroup");
        List<Map<String,Object>> noPointGroup= (List<Map<String,Object>>)detail_map.get("noPointGroup");
        //perecent为必填值
        //填写的积分数要<=总积分数
        //所有完整度相加必须等于100
        int totalPointSave=Integer.parseInt(form.getTotalPointSave());
        int totalPercentSave=Integer.parseInt(form.getTotalPointSave());
        int totalPoint=0;
        int totalPercent=0;
        Set<String> totalAttrSet=new HashSet<String>();
        int totalAttrCount=0;
        for(Map<String,Object> detail_info :pointGroup){
            Set<Map.Entry<String,Object>> detail_set=detail_info.entrySet();
            Map<String,Object> convert_map=new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : detail_info.entrySet()) {
                convert_map=(Map<String,Object>)entry.getValue();
                String key=entry.getKey();
                totalAttrSet.add(key);
                totalAttrCount ++;
            }
            String point=ConvertUtil.getString(convert_map.get("point"));
            String percent=ConvertUtil.getString(convert_map.get("percent"));
            if("".equals(percent)){
                result_map.put("resultCode",-1);
                result_map.put("resultMsg","您输入的积分明细中有完整度百分比为空的数据");
                return result_map;
            }
            if("".equals(point)){
                result_map.put("resultCode",-1);
                result_map.put("resultMsg","保存失败请确认pointGroup相关格式");
                return result_map;
            }
            totalPoint += Integer.parseInt(point);
            totalPercent += Integer.parseInt(percent);
        }

        for(Map<String,Object> detail_info :noPointGroup){
            Set<Map.Entry<String,Object>> detail_set=detail_info.entrySet();
            Map<String,Object> convert_map=new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : detail_info.entrySet()) {
                convert_map=(Map<String,Object>)entry.getValue();
                String key=entry.getKey();
                totalAttrSet.add(key);
                totalAttrCount ++;
            }
            String point=ConvertUtil.getString(convert_map.get("point"));
            String percent=ConvertUtil.getString(convert_map.get("percent"));
            if("".equals(percent)){
                result_map.put("resultCode",-1);
                result_map.put("resultMsg","您输入的积分明细中有完整度百分比为空的数据");
                return result_map;
            }
            if(!"".equals(point)){
                result_map.put("resultCode",-1);
                result_map.put("resultMsg","保存失败请确认noPointGroup相关格式");
                return result_map;
            }
            totalPercent += Integer.parseInt(percent);
        }

        if(totalAttrSet.size() != totalAttrCount){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","会员完善属性存在重复");
            return result_map;
        }

        //如果未分组为空的话，总的积分要等于设置积分数
        if((noPointGroup == null || noPointGroup.size() == 0) && totalPoint != totalPointSave){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","单项奖励积分之和必须等于总奖励积分");
            return result_map;
        }


        if(totalPoint > totalPointSave){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","奖励积分合计不能大于总奖励积分");
            return result_map;
        }

        if(totalPercent != 100){
            result_map.put("resultCode",-1);
            result_map.put("resultMsg","完成比例合计必须等于100%");
            return result_map;
        }
        //时间区间校验
        List<Map<String,Object>> rule_list= new ArrayList<Map<String, Object>>();
        if(Integer.parseInt(form.getNewRuleFlag()) == 1){
            //需要取用全部的规则
            rule_list =this.getRuleListWithoutPage(MapBuilder.newInstance().put("organizationInfoId",map.get("organizationInfoId")).put("brandInfoId",map.get("brandInfoId")).build());
        }else{
            //排除自身以外全部的规则
            rule_list =this.getRuleListWithoutPage(MapBuilder.newInstance().put("organizationInfoId",map.get("organizationInfoId")).put("brandInfoId",map.get("brandInfoId")).put("exceptCompleteDegreeRuleID",form.getCompleteDegreeRuleID()).put("exceptCompleteDegreeRuleID",form.getCompleteDegreeRuleID()).build());
        }


        for(Map<String,Object> rule_info:rule_list){
            String startTime = ConvertUtil.getString(rule_info.get("startTime"));
            String endTime = ConvertUtil.getString(rule_info.get("endTime"));
            // 判断不交叉的两种情况取反即为交叉情景
            if (!(DateUtil.compareDate(startTime, endTimeSave) > 0 || DateUtil.compareDate(endTime, startTimeSave) < 0)) {
                result_map.put("resultCode",-1);
                result_map.put("resultMsg","您填写的时间区间与已设置的活动时间存在冲突");
                return result_map;
            }
        }

        // 参数MAP
        map.put("ruleNameSave",form.getRuleNameSave());
        map.put("startTimeSave",form.getStartTimeSave());
        map.put("endTimeSave",form.getEndTimeSave());
        map.put("endTimeSave",form.getEndTimeSave());
        map.put("totalPointSave",form.getTotalPointSave());
        map.put("memoSave",form.getMemoSave());
        map.put("ruleJsonSave",form.getRuleJsonSave());
        map.put("exceptPoint",totalPointSave - totalPoint);

        return null;
    }


}
