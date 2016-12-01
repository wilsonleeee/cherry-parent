package com.cherry.cm.activemq.bl;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.activemq.dto.MQBaseDTO;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.service.BINOLMQCOM01_Service;
import com.cherry.cm.core.CherryConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import javax.annotation.Resource;
import java.util.List;

/**
 *此类中的方法，仅限于从新后台发出的MQ时(最新版的纯JSON格式)使用
 * TODO：系统中MQ的设计、使用将还会有进一步的改进
 * Created by dingyongchang on 2016/11/29.
 */
public class StandardMqSender {
    @Resource
    private MessageSender messageSender;

    @Resource
    private BINOLMQCOM01_Service binOLMQCOM01_Service;
    /**
     * 此方法会根据MQBaseDTO的SyncLogFlag字段来处理日志
     * @param dto
     */
    public void send(MQBaseDTO dto) throws Exception{
        if("1".equals(dto.getSyncLogFlag())){
            //记入SQLserver数据库
            insertSqlserverDBForSync(dto);
        }else if("2".equals(dto.getSyncLogFlag())){
            //记入MongoDB，以备MQ同步程序使用
            insertMongoDBForSync(dto);
        }
        messageSender.sendMessage(dto.getOriginalMsg(),dto.getQueueName());

        //判断是否要记入发送日志表(MongoDB)
        if(dto.isNeedSaveLog()){
            insertMongoDBForLog(dto);
        }
    }

    /**
     * 此方法不记录同步日志，使用时要注意。两种情况下可以用此方法：
     * （1）消息类型属于不重要的消息，比如通知之类的，丢失了可以不计较的
     * （2）插入同步日志的动作已经另行做完。（某些情况下为保证事务的安全性，插入日志表这样的操作放在前面一起做掉
     * 成功后再来调用发送。）
     *
     * @param dto
     */
    public void sendNoSyncLog(MQBaseDTO dto) throws Exception{
        messageSender.sendMessage(dto.getOriginalMsg(),dto.getQueueName());
        if(dto.isNeedSaveLog()){
            insertMongoDBForLog(dto);
        }
    }

    /**
     * 此方法不记录同步日志，使用时要注意。两种情况下可以用此方法：
     * （1）消息类型属于不重要的消息，比如通知之类的，丢失了可以不计较的
     * （2）插入同步日志的动作已经另行做完。（某些情况下为保证事务的安全性，插入日志表这样的操作放在前面一起做掉
     * 成功后再来调用发送。）
     *
     * @param dtoList
     */
    public void sendNoSyncLog(List<MQBaseDTO> dtoList) throws Exception{
        for(MQBaseDTO dto:dtoList) {
            messageSender.sendMessage(dto.getOriginalMsg(), dto.getQueueName());
            if(dto.isNeedSaveLog()){
                insertMongoDBForLog(dto);
            }
        }
    }


    /**
     * 此方法仅用于记录同步用的日志
     * @param dtoList
     */
    public void insertLogForSync(List<MQBaseDTO> dtoList) throws Exception{
        //TODO:目前此方法只是简单循环调用插入，可优化
        for(MQBaseDTO dto:dtoList) {
            if("1".equals(dto.getSyncLogFlag())){
                //记入SQLserver数据库
                insertSqlserverDBForSync(dto);
            }else if("2".equals(dto.getSyncLogFlag())){
                //记入MongoDB，以备MQ同步程序使用
                insertMongoDBForSync(dto);
            }
        }
    }


    private void insertMongoDBForSync(MQBaseDTO dto) throws Exception{
        binOLMQCOM01_Service.addMongoDBMqLog(convertDtoForMongo(dto));
    }

    private void insertMongoDBForLog(MQBaseDTO dto) throws Exception{
        binOLMQCOM01_Service.addMongoDBBusLog(convertDtoForMongo(dto));
    }

    private void insertSqlserverDBForSync(MQBaseDTO dto){
        binOLMQCOM01_Service.insertSqlMQLog(convertDtoForSql(dto));
    }

    private DBObject convertDtoForMongo(MQBaseDTO dto){
        DBObject mqLog = new BasicDBObject();
        //TODO: 数据插入方标志 ,暂时沿用，后续要再研究是否可去除
        mqLog.put("Source", CherryConstants.MQ_SOURCE_CHERRY);
        // 消息方向
        mqLog.put("SendOrRece", CherryConstants.MQ_SENDORRECE_S);
        // 组织代号
        mqLog.put("OrgCode", dto.getOrgCode());
        // 品牌代码
        mqLog.put("BrandCode", dto.getBrandCode());
        // 单据类型
        mqLog.put("BillType", dto.getTradeType());
        // 单据号
        mqLog.put("BillCode", dto.getTradeNoIF());
        //TODO: 修改次数,将要逐步废弃掉
        mqLog.put("ModifyCount", "0");
        // 插入时间
        mqLog.put("InsertTime", binOLMQCOM01_Service.getSYSDate());

        mqLog.put("ReceiveFlag", CherryConstants.MQ_RECEIVEFLAG_0);
        // 消息发送队列名
        mqLog.put("MsgQueueName", dto.getQueueName());
        // 消息体
        mqLog.put("Data", dto.getOriginalMsg());
        // JMS协议头中的JMSGROUPID
        mqLog.put("JmsGroupId", dto.getBrandCode());
        return mqLog;
    }

    private MQInfoDTO convertDtoForSql(MQBaseDTO dto){
        MQInfoDTO retDto = new MQInfoDTO();
        retDto.setSource("CHERRY");
        retDto.setSendOrRece(CherryConstants.MQ_SENDORRECE_S);
        retDto.setOrganizationInfoId(dto.getOrganizationInfoID());
        retDto.setBrandInfoId(dto.getBrandInfoID());
        retDto.setBillType(dto.getTradeType());
        retDto.setBillCode(dto.getTradeNoIF());
        retDto.setSaleRecordModifyCount(0);
        retDto.setReceiveFlag(CherryConstants.MQ_RECEIVEFLAG_0);
        retDto.setMsgQueueName(dto.getQueueName());
        retDto.setData(dto.getOriginalMsg());
        return retDto;
    }
}
