package com.cherry.mq.mes.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMachMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES09_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 机器信息接收处理BL
 * 
 * @author zhhuyi
 *
 */

@SuppressWarnings("unchecked")
public class BINBEMQMES09_BL implements AnalyzeMachMessage_IF{

    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="binBEMQMES99_Service")
    private BINBEMQMES99_Service binBEMQMES99_Service;
    
	@Resource(name="binBEMQMES09_Service")
	private BINBEMQMES09_Service binBEMQMES09_Service;
	
	@Override
	public void analyzeMachInfoData(Map<String, Object> map) throws Exception {
		setInsertInfoMapKey(map);
        //MI类型的单据号规则是：机器号+YYYYMMDDHHmmSS+3位流水号，截取时去掉后三位，再从尾部截取14位即可得到YYYYMMDDHHmmSS。
		String tradeNoIF = ConvertUtil.getString(map.get("tradeNoIF"));
		String dateTimePattern = "yyyyMMddHHmmss";//时间格式
		int sequenceLength = 3;//流水号长度
		int minLength = dateTimePattern.length()+sequenceLength;//单据号最少长度
        if(!"".equals(tradeNoIF) && tradeNoIF.length()>=minLength){
            String lastStartTime = tradeNoIF.substring(tradeNoIF.length()-minLength, tradeNoIF.length()-sequenceLength);
            if(DateUtil.checkDate(lastStartTime, dateTimePattern)){
                Date date = DateUtil.coverString2Date(lastStartTime, dateTimePattern);
                map.put("lastStartTime", DateUtil.date2String(date, DateUtil.DATETIME_PATTERN));
            }
        }
        //lastStartTime为NULL，更新为系统时间
        if(null == map.get("lastStartTime")){
            map.put("lastStartTime", binBEMQMES09_Service.getSYSDateTime());
        }
		//更新机器信息
		int updCount = binBEMQMES09_Service.updateMachInfo(map);
		if (updCount == 0){
			Object object = map.get("machineCode");
			if(map.get("machineCode")!=null&&!map.get("machineCode").equals("")){
				String machineCode = map.get("machineCode").toString();
				char[] c = machineCode.toCharArray();
				c[c.length-1] = '9';
				String newmachineCode = String.valueOf(c);
				map.put("machineCode", newmachineCode);
				// 继续更新机器信息
				updCount = binBEMQMES09_Service.updateMachInfo(map);
				if (updCount != 0){
			    	return;
			    }
			}
			MessageUtil.addMessageWarning(map,"机器号为\""+object+"\""+MessageConstants.MSG_ERROR_39);
		}
	}

    @Override
    public void analyzeMachCounterData(Map<String, Object> map) throws Exception {
        setInsertInfoMapKey(map);
        // 取得柜台ID
        HashMap<String,Object> resultMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);
        if (resultMap!=null && resultMap.get("counterInfoID") != null) {
            map.put("BIN_CounterInfoID", resultMap.get("counterInfoID"));
            int updCount = binBEMQMES09_Service.updateMachineCodeCollate(map);
            if(updCount == 0){
                MessageUtil.addMessageWarning(map,"机器号为\""+map.get("machineCode")+"\""+MessageConstants.MSG_ERROR_59);
            }
        } else {
            // 没有查询到相关部门信息
            MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
        }
    }
	
    @Override
    public void analyzeExhibitQuantityData(Map<String, Object> map) throws Exception {
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        //柜台的部门ID
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
        paramMap.put("BIN_BrandInfoID", brandInfoID);
        paramMap.put("CounterCode", map.get("counterCode"));
        Map<String,Object> departInfo = binBEMQMES97_BL.getOrganizationInfo(paramMap, true);
        String organizationID = ConvertUtil.getString(departInfo.get("BIN_OrganizationID"));
        map.put("organizationID", organizationID);
        map.put("counterName", departInfo.get("CounterNameIF"));
        if(null == map.get("detailList") || !(map.get("detailList") instanceof List)){
            MessageUtil.addMessageWarning(map,"DetailList必须存在且是List");
        }
        List<Map<String,Object>> detailList = (List<Map<String, Object>>) map.get("detailList");
        if(detailList.size() == 0){
            MessageUtil.addMessageWarning(map,"DetailList不能为空");
        }

        Map<String,Object> prmPrtParam = new HashMap<String,Object>();
        prmPrtParam.put("BIN_OrganizationInfoID", organizationInfoID);
        prmPrtParam.put("BIN_BrandInfoID", brandInfoID);
        prmPrtParam.put("BIN_OrganizationID", organizationID);
        prmPrtParam.put("TradeDateTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        
        Map<String,Object> insertUpdateParam = new HashMap<String,Object>();
        insertUpdateParam.put("BIN_OrganizationInfoID", organizationInfoID);
        insertUpdateParam.put("BIN_BrandInfoID", brandInfoID);
        insertUpdateParam.put("BIN_OrganizationID", organizationID);
        setInsertInfoMapKey(insertUpdateParam);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            
            //查找产品厂商ID
            prmPrtParam.put("BarCode", detailDTO.get("barCode"));
            prmPrtParam.put("UnitCode", detailDTO.get("unitCode"));
            int productVendorID = binBEMQMES97_BL.getProductVendorID(map, prmPrtParam, true);
            
            //陈列数必填
            String exhibitQuantity = ConvertUtil.getString(detailDTO.get("exhibitQuantity"));
            if(exhibitQuantity.equals("")){
                MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, "ExhibitQuantity"));
            }
            
            //更新柜台产品订货参数表，没有记录，新增一条记录
            insertUpdateParam.put("BIN_ProductVendorID", productVendorID);
            insertUpdateParam.put("ExhibitQuantity", exhibitQuantity);
            int cnt = binBEMQMES09_Service.updateCounterPrtOrParameter(insertUpdateParam);
            if(cnt ==0){
                binBEMQMES09_Service.insertCounterPrtOrParameter(insertUpdateParam);
            }
        }
        map.put("modifyCounts","0");
    }
    
	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES09");
		map.put("createPGM", "BINBEMQMES09");
		map.put("updatedBy", "BINBEMQMES09");
		map.put("updatePGM", "BINBEMQMES09");
	}
	@Override
	public void selMessageInfo(Map map) throws CherryMQException {}

	@Override
	public void setDetailDataInfo(List detailDataList, Map map)throws CherryMQException {}
	
    @Override
    public void addMongoMsgInfo(Map map) throws CherryMQException {
        //兼容老格式的消息体，没有单据号不插入MongoDB。
        if(ConvertUtil.getString(map.get("tradeNoIF")).equals("")){
            return;
        }
        DBObject dbObject = new BasicDBObject();
        // 组织代码
        dbObject.put("OrgCode", map.get("orgCode"));
        // 品牌代码，即品牌简称
        dbObject.put("BrandCode", map.get("brandCode"));
        // 业务类型
        dbObject.put("TradeType", map.get("tradeType"));
        // 单据号
        dbObject.put("TradeNoIF", map.get("tradeNoIF"));
        // 修改次数
        dbObject.put("ModifyCounts", map.get("modifyCounts")==null
                ||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
        // 机器号
        dbObject.put("MachineCode", map.get("machineCode"));
        if(map.get("tradeType").equals(MessageConstants.MSG_MACHINE_INFO)){
            // 日志正文
            dbObject.put("Content", "机器信息");
        }else if(map.get("tradeType").equals(MessageConstants.MSG_MACHINE_COUNTER)){
            // 柜台号
            dbObject.put("CounterCode", map.get("counterCode"));
            // 日志正文
            dbObject.put("Content", "机器绑定柜台信息");
        }else if(map.get("tradeType").equals(MessageConstants.MSG_EXHIBIT_QUANTITY)){
            // 柜台号
            dbObject.put("CounterCode", map.get("counterCode"));
            // 日志正文
            dbObject.put("Content", "柜台产品陈列数");
        }
        
        map.put("dbObject", dbObject);
    }
}
