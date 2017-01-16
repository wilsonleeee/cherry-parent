package com.cherry.mq.mes.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES03_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.mq.mes.service.MqST_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by menghao on 2017/1/13.
 *
 */
public class MqST implements MqReceiver_IF {

    private static final Logger logger = LoggerFactory.getLogger(MqST.class);

    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    @Resource(name="binBEMQMES03_Service")
    private BINBEMQMES03_Service binBEMQMES03_Service;
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    @Resource(name="mqST_Service")
    private MqST_Service mqST_Service;
    @Resource(name="binBEMQMES99_Service")
    private BINBEMQMES99_Service binBEMQMES99_Service;

    @Override
    public void tran_execute(Map<String, Object> map) throws Exception {
        // 校验并设置相关参数
        this.checkAndSetData(map);
        String targetDateType = ConvertUtil.getString(map.get("targetDateType"));
        // targetDateType无值或者M时:销售月目标;
        if("M".equalsIgnoreCase(targetDateType) || "".equals(targetDateType)) {
            // 更新/插入销售目标设定表
            this.insertORupdSaleTarget(map);
            map.put("content","终端设定销售目标");
        } else if("D".equalsIgnoreCase(targetDateType)) {
            // 更新/插入销售日目标设定表
            this.insertORupdSaleDayTarget(map);
            map.put("content","终端设定销售日目标");
        }

        String syncLogFlag = ConvertUtil.getString(map.get("syncLogFlag"));
        if ("".equals(syncLogFlag) || "1".equals(syncLogFlag)){
            // 插入MQ日志表
            this.addMessageLog(map);
        }

        //写入Mongo
        this.addMongoDBBusLog(map);
        // 标记当前BL已经将MQ信息写入MongoDB与MQ收发日志表
        map.put("isInsertMongoDBBusLog","1");

    }

    /**
     * 接收数据写入MQ收发日志表
     * @param map
     * @throws Exception
     */
    private void addMessageLog(Map<String, Object> map) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("organizationInfoID", map.get("organizationInfoID"));
        paramMap.put("brandInfoID", map.get("brandInfoID"));
        paramMap.put("tradeType", map.get("tradeType"));
        paramMap.put("tradeNoIF", map.get("tradeNoIF"));
        paramMap.put("modifyCounts", map.get("modifyCounts")==null
                ||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
        paramMap.put("counterCode", map.get("code"));
        paramMap.put("createdBy", "-2");
        paramMap.put("createPGM", "MqST");
        paramMap.put("updatedBy", "-2");
        paramMap.put("updatePGM", "MqST");
        // 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
    }

    /**
     * 插入MongoDB
     * @param map
     * @throws CherryMQException
     */
    private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
        //写入Mongo
        DBObject dbObject = new BasicDBObject();
        //组织代号
        dbObject.put("OrgCode", map.get("orgCode"));
        // 品牌代码，即品牌简称
        dbObject.put("BrandCode", map.get("brandCode"));
        // 业务类型
        dbObject.put("TradeType", map.get("tradeType"));
        // 单据号
        dbObject.put("TradeNoIF", map.get("tradeNoIF"));
        // 修改次数
        dbObject.put("ModifyCounts", map.get("modifyCounts"));
        // 业务主体
        dbObject.put("TradeEntity", "1");
        // 业务主体代号
        dbObject.put("TradeEntityCode", map.get("code"));
        // 业务主体名称
        dbObject.put("TradeEntityName", map.get("TradeEntityName"));
        // 业务主体
        dbObject.put("Content", map.get("content"));

        map.put("addMongoDBFlag", "0");
        binBEMQMES99_Service.addMongoDBBusLog(dbObject);
        map.put("addMongoDBFlag", "1");

    }

    /**
     * 插入或者更新销售日目标数据
     * @param map
     */
    private void insertORupdSaleDayTarget(Map<String, Object> map) {
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        Map<String,Object> saleDayTargetMap = new HashMap<String,Object>();
        saleDayTargetMap.put("BIN_OrganizationInfoID", organizationInfoID);
        saleDayTargetMap.put("BIN_BrandInfoID", brandInfoID);
        saleDayTargetMap.put("Type", map.get("type"));
        saleDayTargetMap.put("Parameter", map.get("parameterID"));
        saleDayTargetMap.put("TargetType", "PRO");//目前写入的都是产品
        saleDayTargetMap.put("TargetDate", map.get("targetDate"));
        saleDayTargetMap.put("TargetMoney",ConvertUtil.getString(map.get("targetMoney")));
        saleDayTargetMap.put("TargetQuantity", ConvertUtil.getString(map.get("targetQuantity")));
        saleDayTargetMap.put("SynchroFlag", "1");//1：已同步
        saleDayTargetMap.put("Source", CherryConstants.SALETARGET_SOURCE_TERMINAL);//终端设定
        // 设置时间
        String targetSetTime = ConvertUtil.getString(map.get("targetSetTime"));
        saleDayTargetMap.put("TargetSetTime", targetSetTime);
        setUpdateInfoMapKey(saleDayTargetMap);

        List<Map<String,Object>> resultMap = mqST_Service.getSaleDayTarget(saleDayTargetMap);
        if(null!=resultMap && resultMap.size()>0){
            String sourceDB = ConvertUtil.getString(resultMap.get(0).get("Source"));
            String targetSetTimeDB = ConvertUtil.getString(resultMap.get(0).get("TargetSetTime"));
            if(DateUtil.compareDate(targetSetTime, targetSetTimeDB) > 0){
                //时序控制 消息体的时间必须大于数据库中这条记录的设定时间
                //配置项 是否允许终端设定覆盖后台设定（终端设定销售目标），1：允许，0：不允许
                //如果允许终端设定覆盖后台设定，可以更新。
                //如果不允许允许终端设定覆盖后台设定，当数据库中Source字段，为2：终端设定可以更新记录，为1：后台设定不能更新记录。
                String configValue = binOLCM14_BL.getConfigValue("1136", organizationInfoID, brandInfoID);
                boolean canCoverFlag = false;
                if(configValue.equals("1") ||
                        (configValue.equals("0") && sourceDB.equals(CherryConstants.SALETARGET_SOURCE_TERMINAL))){
                    canCoverFlag = true;
                }
                if(canCoverFlag){
                    mqST_Service.updateSaleDayTarget(saleDayTargetMap);
                }
            } else {
                logger.error("[销售日目标]消息体的时间小于数据库中这条记录的设定时间,不予更改;消息体设置时间:"+map.get("targetSetTime")
                        +"数据库中的设定时间:"+targetSetTimeDB+";subType="+map.get("subType")+",code="+map.get("code"));
            }
        }else{
            mqST_Service.insertSaleDayTarget(saleDayTargetMap);
        }
    }

    /**
     * 插入或者更新销售月目标数据
     * @param map
     */
    private void insertORupdSaleTarget(Map<String, Object> map) {
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        Map<String,Object> saleTargetMap = new HashMap<String,Object>();
        saleTargetMap.put("BIN_OrganizationInfoID", organizationInfoID);
        saleTargetMap.put("BIN_BrandInfoID", brandInfoID);
        saleTargetMap.put("Type", map.get("type"));
        saleTargetMap.put("Parameter", map.get("parameterID"));
        saleTargetMap.put("TargetType", "PRO");//目前写入的都是产品
        saleTargetMap.put("TargetDate", map.get("targetDate"));
        saleTargetMap.put("TargetMoney",ConvertUtil.getString(map.get("targetMoney")));
        saleTargetMap.put("TargetQuantity", ConvertUtil.getString(map.get("targetQuantity")));
        saleTargetMap.put("SynchroFlag", "1");//1：已同步
        saleTargetMap.put("Source", CherryConstants.SALETARGET_SOURCE_TERMINAL);//终端设定
        // 设置时间
        String targetSetTime = ConvertUtil.getString(map.get("targetSetTime"));
        saleTargetMap.put("TargetSetTime", targetSetTime);

        List<Map<String,Object>> resultMap = binBEMQMES03_Service.getSaleTarget(saleTargetMap);
        if(null!=resultMap && resultMap.size()>0){
            String sourceDB = ConvertUtil.getString(resultMap.get(0).get("Source"));
            String targetSetTimeDB = ConvertUtil.getString(resultMap.get(0).get("TargetSetTime"));
            if(DateUtil.compareDate(targetSetTime, targetSetTimeDB) > 0){
                //时序控制 消息体的时间必须大于数据库中这条记录的设定时间
                //配置项 是否允许终端设定覆盖后台设定（终端设定销售目标），1：允许，0：不允许
                //如果允许终端设定覆盖后台设定，可以更新。
                //如果不允许允许终端设定覆盖后台设定，当数据库中Source字段，为2：终端设定可以更新记录，为1：后台设定不能更新记录。
                String configValue = binOLCM14_BL.getConfigValue("1136", organizationInfoID, brandInfoID);
                boolean canCoverFlag = false;
                if(configValue.equals("1") ||
                        (configValue.equals("0") && sourceDB.equals(CherryConstants.SALETARGET_SOURCE_TERMINAL))){
                    canCoverFlag = true;
                }
                if(canCoverFlag){
                    setUpdateInfoMapKey(saleTargetMap);
                    binBEMQMES03_Service.updateSaleTarget(saleTargetMap);
                }
            } else {
                logger.error("消息体的时间小于数据库中这条记录的设定时间,不予更改;消息体设置时间:"+map.get("targetSetTime")
                        +"数据库中的设定时间:"+targetSetTimeDB+";subType="+map.get("subType")+",code="+map.get("code"));
            }
        }else{
            // 为了使用binBEMQMES03_Service.insertSaleTarget方法,特殊定义这些个字段
            saleTargetMap.put("CreatedBy","-2");
            saleTargetMap.put("CreatePGM","MqST");
            saleTargetMap.put("UpdatedBy","-2");
            saleTargetMap.put("UpdatePGM","MqST");
            binBEMQMES03_Service.insertSaleTarget(saleTargetMap);
        }

    }

    /**
     * 校验参数设置必要的参数
     * @param map
     */
    private void checkAndSetData(Map<String, Object> map) throws Exception{
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        map.put("BIN_OrganizationInfoID", organizationInfoID);
        map.put("BIN_BrandInfoID", brandInfoID);
        //子类型，BA：BA的销售目标，CT：柜台的销售目标。
        String subType = ConvertUtil.getString(map.get("subType"));
        //如果SubType的值为BA，这里对应的Code是BA的工号，如果SubType的值为CT，这里对应的Code是柜台编号
        String code = ConvertUtil.getString(map.get("code"));
        // 如果TargetDateType为空或者M:销售月目标,D:销售日目标.
        String targetDateType = ConvertUtil.getString(map.get("targetDateType"));
        //销售单位类型 1：区域，2：柜台，3：营业员。
        //配合Type使用
        //当Type=1时，该字段存放区域ID（这里不接收Type=1）
        //当Type=2时，该字段存放柜台在组织结构表中的ID
        //当Type=3时，该字段存放营业员对应的EmployerID
        if("BA".equals(subType)){
            //查员工ID
            map.put("type","3");//营业员
            map.put("EmployeeCode", code);
            Map<String,Object> employeeInfo = binBEMQMES97_BL.getEmployeeInfo(map, true);
            map.put("parameterID",CherryUtil.obj2int(employeeInfo.get("BIN_EmployeeID")));
            map.put("TradeEntityName", ConvertUtil.getString(employeeInfo.get("EmployeeName")));
        }else if("CT".equals(subType)){
            //查部门ID
            map.put("type","2");//柜台
            map.put("CounterCode", code);
            Map<String,Object> organizationInfo = binBEMQMES97_BL.getOrganizationInfo(map, true);
            map.put("parameterID",CherryUtil.obj2int(organizationInfo.get("BIN_OrganizationID")));
            map.put("TradeEntityName", ConvertUtil.getString(organizationInfo.get("CounterNameIF")));
        }else{
            MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_73);
        }

        //目标日期必填，格式视销售目标日期类型而定
        String targetDate = ConvertUtil.getString(map.get("targetDate"));
        if("".equals(targetDate)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, "TargetDate"));
        }

        // targetDateType无值或者M时:销售月目标;
        if("M".equalsIgnoreCase(targetDateType) || "".equals(targetDateType)) {
            if(!CherryChecker.checkDate(targetDate, CherryConstants.DATEYYYYMM)){
                MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "TargetDate", "YYYYMM"));
            }
        } else if("D".equalsIgnoreCase(targetDateType)) {
            if(!CherryChecker.checkDate(targetDate, CherryConstants.DATE_YYMMDD)){
                MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "TargetDate", "yyyyMMdd"));
            }
        }

    }

    /**
     * 设置操作程序名称
     * @param map
     */
    public void setUpdateInfoMapKey(Map<String,Object> map) {
        map.put("createdBy", "-2");
        map.put("createPGM", "MqST");
        map.put("updatedBy", "-2");
        map.put("updatePGM", "MqST");
    }
}
