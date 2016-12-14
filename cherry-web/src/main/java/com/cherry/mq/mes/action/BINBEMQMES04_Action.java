package com.cherry.mq.mes.action;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.SpringBeanManager;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.bl.StandardMq_BL;
import com.cherry.mq.mes.common.*;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: 本Action在雅芳柜台积分计算中创建
 *
 * @author Wangminze
 * @version 2016/11/30
 * @description：查询数据时，参数和数据库字段名一致
 */
public class BINBEMQMES04_Action {

    private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES03_Action.class);

    @Resource(name="standardMq_BL")
    private StandardMq_BL standardMq_BL;

    @Resource(name="standardMqDataSource")
    private StandardMqDataSource standardMqDataSource;


    /**
     * TODO: 处理新后台发送到新后台的MQ消息
     * @antuor: Wangminze
     * @param:  map 消息体
     * @return:
     * @version: 2016/11/30
     * @throws : Exception
     **/
    public void receiveMessage(Map<String, Object> map) throws Exception {

        // 判断是否是重复的数据
        if(!judgeIfIsRepeatData(map)) {
            // 是重复数据
            MessageUtil.addMessageWarning(map, MessageConstants.MSG_REPEAT_DATA);
        }

        try {
            // 校验品牌code并设置品牌数据源
            String brandCode = ConvertUtil.getString(map.get("BrandCode"));
            if("".equals(brandCode)) {
                // 品牌CODE必填
                throw new CherryMQException(String.format(MessageConstants.MSG_ERROR_74, "BrandCode"));
            } else {
                // 根据BrandCode设置数据源
                if(!standardMqDataSource.setBrandDataSource(brandCode)) {
                    // 未找到相应的数据源配置
                    MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_14);
                }
            }

            // 根据品牌code值取得组织品牌信息
            if (CherryMqMsgReceiverImpl.brandMap.containsKey(brandCode)) {
                String[] tmpArr = CherryMqMsgReceiverImpl.brandMap
                        .get(brandCode);
                map.put("BIN_BrandInfoID", tmpArr[0]);
                map.put("BIN_OrganizationInfoID", tmpArr[1]);
                map.put("OrgCode", tmpArr[2]);
            } else {
                String[] tmpArr = new String[] { "", "", "" };
                // 查询品牌信息
                Map<String, Object> resultMap = getMessageOrganizationInfo(map);

                if (resultMap != null) {

                    tmpArr[0] = String
                            .valueOf(resultMap.get("BIN_BrandInfoID"));
                    tmpArr[1] = String.valueOf(resultMap
                            .get("BIN_OrganizationInfoID"));
                    tmpArr[2] = String.valueOf(resultMap.get("OrgCode"));

                    // 设定品牌ID
                    map.put("BIN_BrandInfoID", resultMap.get("BIN_BrandInfoID"));
                    // 设定组织ID
                    map.put("BIN_OrganizationInfoID",
                            resultMap.get("BIN_OrganizationInfoID"));
                    // 组织code
                    map.put("OrgCode", resultMap.get("OrgCode"));
                } else {
                    // 没有查询到相关组织品牌信息
                    MessageUtil.addMessageWarning(map,
                            MessageConstants.MSG_ERROR_05);
                }
                CherryMqMsgStandardReceiverImpl.brandMap.put(brandCode, tmpArr);
            }

            // 校验TradeType业务类型
            String tradeType = ConvertUtil.getString(map.get("TradeType"));
            if("".equals(tradeType)) {
                // 业务类型必填
                MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeType"));
            }

            // 校验成功后进入相应的处理消息的类
            // 根据业务类型取得相应的处理类
            // 业务类型的实现类名采用约定模式，一经约定不再更改
            Object ob = SpringBeanManager.getBean("mq"+tradeType);
            if(null == ob) {
                // 没有此业务类型
                MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_27);
            } else {
                ((MqReceiver_IF) ob).tran_execute(map);

                if(!"1".equals(ConvertUtil.getString(map.get("IsInsertMongoDBBusLog")))) {
                    // 在业务处理BL中未插入MongonDB，则在共通中需补上
                    //写入MQ收发日志表
                    this.addMessageLog(map);
                    // 插入MongoDB
                    this.addMongoDBBusLog(map);
                }
            }

        } catch(Exception e) {
            logger.error("*****************************MQ接收消息异常结束***************************",e);
            String addMongoDBFlag = (String)map.get("AddMongoDBFlag");
            if(addMongoDBFlag != null && "1".equals(addMongoDBFlag)) {
                // 删除MQ消息接收失败但已经写入到MongoDB的数据
                removeMongoDBData(map);
            }

            if(e instanceof CherryMQException) {

            } else {
                if(map != null) {
                    MessageUtil.addMessageWarning(map, e.getMessage());
                }
            }

            throw new CherryMQException(e.getMessage());
        } finally {
            // 清除数据源ThreadLocal变量
            CustomerContextHolder.clearCustomerDataSourceType();
        }

    }

    /**
     * 判断是否是重复的数据;
     * 主要通过查询mongodb中是否存在此MQ消息为判断依据
     *
     * @param map
     * @throws Exception
     * @throws CherryMQException
     * @return true:非重复,false:重复
     */
    public boolean judgeIfIsRepeatData(Map map) throws Exception{
        if(map.get("TradeNoIF")==null){
            return true;
        }
        DBObject dbObject = new BasicDBObject();
        dbObject.put("TradeNoIF", map.get("TradeNoIF"));
        dbObject.put("BrandCode", map.get("BrandCode"));
        dbObject.put("TradeType", map.get("TradeType"));
        dbObject.put("ModifyCounts",  map.get("ModifyCounts")==null
                ||map.get("ModifyCounts").equals("")?"0":map.get("ModifyCounts"));
        try {
            // 从指定的集合中判断记录是否存在
            if(MongoDB.isExist(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject)) {
                return false;
            }
        } catch (Exception e) {
            throw new CherryMQException(MessageConstants.MSG_ERROR_38);
        }
        return true;
    }

    /**
     * 接收数据写入MQ收发日志表
     * @param map
     * @throws Exception
     */
    private void addMessageLog(Map<String, Object> map) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
        paramMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
        paramMap.put("TradeType", map.get("TradeType"));
        paramMap.put("TradeNoIF", map.get("TradeNoIF"));
        paramMap.put("ModifyCounts", map.get("ModifyCounts")==null
                ||map.get("ModifyCounts").equals("")?"0":map.get("ModifyCounts"));
        paramMap.put("CounterCode", map.get("DepartCode"));
        paramMap.put("IsPromotionFlag", map.get("IsPromotionFlag"));
        paramMap.put("createdBy", map.get("CreatedBy"));
        paramMap.put("createPGM", map.get("CreatePGM"));
        paramMap.put("updatedBy", map.get("UpdatedBy"));
        paramMap.put("updatePGM", map.get("UpdatePGM"));
        // 插入MQ日志表（数据库SqlService）
        standardMq_BL.addMessageLog(paramMap);
    }

    /**
     * TODO : 添加MongoDB数据
     *
     * @description :
     * @param :  map
     * @return :
     * @antuor : Wangminze
     * @version : 2016/11/30
     **/
    private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
        // 插入MongoDB
        DBObject dbObject = new BasicDBObject();
        // 组织代码
        dbObject.put("OrgCode", map.get("OrgCode"));
        // 品牌代码，即品牌简称
        dbObject.put("BrandCode", map.get("BrandCode"));
        // 业务类型
        dbObject.put("TradeType", map.get("TradeType"));
        // 单据号
        dbObject.put("TradeNoIF", map.get("TradeNoIF"));
        // 修改次数
        dbObject.put("ModifyCounts", map.get("ModifyCounts")==null
                ||map.get("ModifyCounts").equals("")?"0":map.get("ModifyCounts"));
        // 业务主体
        dbObject.put("TradeEntity", "1");
        // 柜台号
        dbObject.put("CounterCode", map.get("DepartCode"));
        //员工代码
        dbObject.put("UserCode", map.get("EmployeeCode"));
        // 发生时间
        dbObject.put("OccurTime", (String)map.get("TradeTime"));
        // 日志正文
        dbObject.put("Content", map.get("Content"));
        //
        map.put("AddMongoDBFlag", "0");
        addMongoDBBusLog(dbObject);
        map.put("AddMongoDBFlag", "1");

    }

    /**
     * 插入消息信息(MongoDB)
     *
     * @param dbObject
     * @throws CherryMQException
     */
    public void addMongoDBBusLog (DBObject dbObject) throws CherryMQException{
        try{
            String modifyCounts = (String)dbObject.get("ModifyCounts");
            if(modifyCounts != null && !"".equals(modifyCounts)) {
                dbObject.put("ModifyCounts", String.valueOf(Integer.parseInt(modifyCounts)));
            } else {
                dbObject.put("ModifyCounts", "0");
            }
            // 如果第一次插入失败将尝试重新插入
            for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
                try {
                    MongoDB.insert(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject);
                    break;
                } catch (IllegalStateException ise) {
                    if (i == CherryConstants.MGO_MAX_RETRY) {
                        throw ise;
                    }
                    logger.error("**************************** Write mongodb fails! method : addMongoDBBusLog  time : " + (i + 1));
                    long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
                    // 延迟等待
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    StringBuffer bf = new StringBuffer();
                    bf.append("************ method addMongoDBBusLog throw exception! Exception Class : ")
                            .append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
                    logger.error(bf.toString(),e);
                    throw e;
                } catch (Throwable t) {
                    throw new Exception("method addMongoDBBusLog throw Throwable!!");
                }
            }
        } catch (Exception e) {
            throw new CherryMQException(MessageConstants.MSG_ERROR_12);
        }
    }

    /**
     * 删除MQ消息接收失败但已经写入到MongoDB的数据
     *
     * @param map
     * @throws Exception
     */
    public void removeMongoDBData(Map<String, Object> map) throws Exception {

        DBObject dbObject = new BasicDBObject();
        dbObject.put("TradeNoIF", map.get("TradeNoIF"));
        dbObject.put("BrandCode", map.get("BrandCode"));
        dbObject.put("TradeType", map.get("TradeType"));
        dbObject.put("ModifyCounts",  map.get("ModifyCounts")==null
                ||map.get("ModifyCounts").equals("")?"0":map.get("ModifyCounts"));
        // 如果第一次删除失败将尝试重新删除
        for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
            try {
                MongoDB.removeAll(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject);
                break;
            } catch (IllegalStateException ise) {
                if (i == CherryConstants.MGO_MAX_RETRY) {
                    throw ise;
                }
                logger.error("**************************** Write mongodb fails! method : removeMongoDBData  time : " + (i + 1));
                long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
                // 延迟等待
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                StringBuffer bf = new StringBuffer();
                bf.append("************ method removeMongoDBData throw exception! Exception Class : ")
                        .append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
                logger.error(bf.toString(),e);
                throw e;
            } catch (Throwable t) {
                throw new Exception("method removeMongoDBData throw Throwable!!");
            }
        }
    }

    /**
     * TODO : 根据品牌CODE查询组织品牌信息
     *
     * @description :
     * @param : map KEY['BrandCode']
     * @return : Map<String, Object> KEY['BIN_BrandInfoID','BIN_OrganizationInfoID','OrgCode']
     * @antuor : Wangminze
     * @version : 2016/11/30
     * @throws Exception
     **/
    public Map<String, Object> getMessageOrganizationInfo(Map<String, Object> map) throws Exception{

        // 查询品牌信息
        HashMap<String, Object> resultMap = standardMq_BL.selBrandInfo(map);

        if (resultMap == null || resultMap.isEmpty()) {
            // 没有查询到相关组织品牌信息
            MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_05);
        }
        return resultMap;

    }
}
