package com.cherry.lg.top.bl;

import com.cherry.cm.core.CherryConstants;
import com.cherry.lg.top.service.Top_Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Wangminze
 * @version 2016/12/22
 * @description
 */
public class Top_BL {

    @Resource
    private Top_Service top_service;

    /**
     * 取得柜台消息
     *
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMsgList2(Map<String, Object> map){

        List<Map<String,Object>> messageList = top_service.getMsgList2(map);

        if(null != messageList && messageList.size() > 0){
            int size = messageList.size();
            for(int i = 0 ; i < size ; i++){
                Map<String,Object> temp = messageList.get(i);

                //取得消息体
                String messageBody = (String)temp.get("MessageBody");
                String messageBody_temp = "";
                char[] messageBodyArr = messageBody.toCharArray();
                //取得消息体的长度
                int  messageBodyLength = messageBodyArr.length;
                //
                int count = 0;

                for(int j = 0 ; j < messageBodyLength ; j++){

                    //控制在30个字节长度之内
                    if(count > 30){
                        messageBody_temp = messageBody.substring(0, j)+" ...";
                        break;
                    }
                    //如果是汉字则加2，否则加1
                    if(messageBodyArr[j] >= 0x0391 && messageBodyArr[j] <= 0xFFE5){
                        count += 2;
                    }else{
                        count ++;
                    }
                }

                if("".equals(messageBody_temp)){
                    messageBody_temp = messageBody;
                }

                temp.put("messageBody_temp", messageBody_temp);
            }
        }

        return messageList;
    }

    /**
     * 取得柜台消息数量
     * @param map
     * @return
     */
    public int getMsgList2Count(Map<String, Object> map){
        return top_service.getMsgList2Count(map);
    }

}
