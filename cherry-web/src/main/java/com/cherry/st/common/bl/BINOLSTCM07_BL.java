package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.cmbussiness.bl.BINOLCM04_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.service.BINOLSTCM07_Service;
import com.cherry.st.common.interfaces.BINOLSTCM07_IF;

/**
 * 给柜台发货，发送MQ
 * 
 * @author dingyc
 * 
 */
public class BINOLSTCM07_BL implements BINOLSTCM07_IF {
	@Resource(name="binOLSTCM07_Service")
	private BINOLSTCM07_Service binolstcm07_service;

	@Resource(name="binOLCM04_BL")
	private BINOLCM04_BL binOLCM04_BL;

    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="messageSender")
	private MessageSender messageSender;

	/**
	 * 将发货单据信息组织成MQ消息 为兼顾老后台，相同的明细条目将被合并
	 * 
	 * @param promotionDeliverIDArr
	 *            收发货单据ID数组
	 * @param userInfo
	 */
	public void sendMQDeliverSend(int[] proDeliverIDArr, Map<String,String> pramMap) {
		// 整理传入的参数
		String organizationInfoID = pramMap.get("BIN_OrganizationInfoID");
		String brandInfoID = pramMap.get("BIN_BrandInfoID");
		String currentUnit = pramMap.get("CurrentUnit");
		String userID = pramMap.get("BIN_UserID");
		String brandCode = pramMap.get("BrandCode");
		String organizationCode = pramMap.get("OrganizationCode");
		String dataFrom = ConvertUtil.getString(pramMap.get("DataFrom"));//数据来源表，用于区分发货单还是入库单

		for (int i = 0; i < proDeliverIDArr.length; i++) {
			// 取得指定ID的收发货/入库单据信息（包括详细无数量）
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	         // 取得指定ID的发货单/入库单明细数量（合并同一产品）
            List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
			if(dataFrom.equals("Inventory.BIN_ProductInDepot")){
			    list = binolstcm07_service.getProInDepotAllGroupByID(proDeliverIDArr[i], organizationInfoID,brandInfoID);
			    allList= binolstcm07_service.getProInDepotDetailQuantityByID(proDeliverIDArr[i]);
			}else{
			    list = binolstcm07_service.getProDeliverAllGroupByID(proDeliverIDArr[i], organizationInfoID,brandInfoID);
			    allList= binolstcm07_service.getProDeliverDetailQuantityByID(proDeliverIDArr[i]);
			}
			Map<String, Object> deliverDetailMap = list.get(0);
			
			//把明细信息加入数量List
			for(int j=0;j<allList.size();j++){
			    for(int k=0;k<list.size();k++){
			        String allList_productVendorId = ConvertUtil.getString(allList.get(j).get("BIN_ProductVendorID"));
			        String list_productVendorId = ConvertUtil.getString(list.get(k).get("BIN_ProductVendorID"));
			        if(allList_productVendorId.equals(list_productVendorId)){
                        Map<String,Object> tem = list.get(k);
                        tem.put("mDeliverReceiveNo", tem.get("mDeliverNoIF"));
                        allList.get(j).putAll(tem);
			            list.remove(k);
			            break;
			        }
			    }
			}
			
			// 插入MQ日志表
			String msg = getDeliverMQMessage(allList, brandCode,organizationCode);
			Map<String, Object> map = new HashMap<String, Object>();

			// 数据插入方标志
			map.put("Source", "CHERRY");
			// 消息方向
			map.put("SendOrRece", "S");
			// 所属组织
			map.put("BIN_OrganizationInfoID", organizationInfoID);
			// 所属品牌ID
			map.put("BIN_BrandInfoID", brandInfoID);
			// 单据类型
			map.put("BillType", "IS");
			// 单据号
			map.put("BillCode", deliverDetailMap.get("mDeliverReceiveNo"));
			// 销售记录修改次数
			map.put("SaleRecordModifyCount", 0);
			// 柜台号
			map.put("CounterCode", deliverDetailMap.get("CounterCode"));
			// 交易日期
			map.put("TxdDate", null);
			// 交易时间
			map.put("TxdTime", null);
			// Transactionlog表开始写入时间
			map.put("BeginPuttime", null);
			// Transactionlog表结束写入时间
			map.put("EndPuttime", null);
			// 是否促销品
			map.put("isPromotionFlag", 1);
			// 批处理执行对象区分
			map.put("ReceiveFlag", 0);
            //消息发送队列名
            map.put("MsgQueueName", CherryConstants.CHERRYTOPOSMSGQUEUE);
			// 消息体
			map.put("Data", msg);
			// 有效区分
			map.put("ValidFlag", "1");
			map.put("CreatedBy", userID);
			map.put("CreatePGM", currentUnit);
			map.put("UpdatedBy", userID);
			map.put("UpdatePGM", currentUnit);
			binOLCM04_BL.insertMQLog(map);

			messageSender.sendMessage(msg);
		}
		// }
	}
	  
	/**
     * 金蝶K3专用。将发货单据信息组织成MQ消息 为兼顾老后台，相同的明细条目将被合并
     * 
     * @param promotionDeliverIDArr
     *            收发货单据ID数组
     * @param userInfo
     */
    public void sendMQDeliverSend_K3(int[] proDeliverIDArr, Map<String,String> pramMap) {
        // 整理传入的参数
        String organizationInfoID = pramMap.get("BIN_OrganizationInfoID");
        String brandInfoID = pramMap.get("BIN_BrandInfoID");
        String currentUnit = pramMap.get("CurrentUnit");
        String userID = pramMap.get("BIN_UserID");
        String brandCode = pramMap.get("BrandCode");
        String organizationCode = pramMap.get("OrganizationCode");
        String dataFrom = ConvertUtil.getString(pramMap.get("DataFrom"));//数据来源表，用于区分发货单还是入库单

        for (int i = 0; i < proDeliverIDArr.length; i++) {
            // 取得指定ID的收发货/入库单据信息（包括详细无数量）
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
             // 取得指定ID的发货单/入库单明细数量（合并同一产品）
            List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
            if(dataFrom.equals("Inventory.BIN_ProductInDepot")){
                list = binolstcm07_service.getProInDepotAllGroupByID(proDeliverIDArr[i], organizationInfoID,brandInfoID);
                allList= binolstcm07_service.getProInDepotDetailQuantityByID(proDeliverIDArr[i]);
            }else{
                list = binolstcm07_service.getProDeliverAllGroupByID(proDeliverIDArr[i], organizationInfoID,brandInfoID);
                allList= binolstcm07_service.getProDeliverDetailQuantityByID(proDeliverIDArr[i]);
            }
            Map<String, Object> deliverDetailMap = list.get(0);
            
            //把明细信息加入数量List
            for(int j=0;j<allList.size();j++){
                for(int k=0;k<list.size();k++){
                    String allList_productVendorId = ConvertUtil.getString(allList.get(j).get("BIN_ProductVendorID"));
                    String list_productVendorId = ConvertUtil.getString(list.get(k).get("BIN_ProductVendorID"));
                    if(allList_productVendorId.equals(list_productVendorId)){
                        Map<String,Object> tem = list.get(k);
                        tem.put("mDeliverReceiveNo", tem.get("mDeliverNoIF"));
                        allList.get(j).putAll(tem);
                        list.remove(k);
                        break;
                    }
                }
            }
            
            // 插入MQ日志表
            String msg = getDeliverMQMessage(allList, brandCode,organizationCode);
            Map<String, Object> map = new HashMap<String, Object>();

            // 数据插入方标志
            map.put("Source", "CHERRY");
            // 消息方向
            map.put("SendOrRece", "S");
            // 所属组织
            map.put("BIN_OrganizationInfoID", organizationInfoID);
            // 所属品牌ID
            map.put("BIN_BrandInfoID", brandInfoID);
            // 单据类型
            map.put("BillType", "IS");
            // 单据号
            map.put("BillCode", deliverDetailMap.get("mDeliverReceiveNo"));
            // 销售记录修改次数
            map.put("SaleRecordModifyCount", 0);
            // 柜台号
            map.put("CounterCode", deliverDetailMap.get("CounterCode"));
            // 交易日期
            map.put("TxdDate", null);
            // 交易时间
            map.put("TxdTime", null);
            // Transactionlog表开始写入时间
            map.put("BeginPuttime", null);
            // Transactionlog表结束写入时间
            map.put("EndPuttime", null);
            // 是否促销品
            map.put("isPromotionFlag", 0);
            // 批处理执行对象区分
            map.put("ReceiveFlag", 0);
            //消息发送队列名
            map.put("MsgQueueName", CherryConstants.CHERRYTOPOSMSGQUEUE);
            // 消息体
            map.put("Data", msg);
            // 有效区分
            map.put("ValidFlag", "1");
            map.put("CreatedBy", userID);
            map.put("CreatePGM", currentUnit);
            map.put("UpdatedBy", userID);
            map.put("UpdatePGM", currentUnit);
            binOLCM04_BL.insertMQLog(map);

            messageSender.sendMessage(msg);
        }
        // }
    }
	
	static String str1 = "\r\n";
	static String str2 = ",";

	public String getDeliverMQMessage(List<Map<String, Object>> list,String brandCode,String organizationCode) {
		StringBuffer buf = new StringBuffer();
		buf.append("[Version]");
		buf.append(str2);
		buf.append("AMQ.001.001");
		buf.append(str1);

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = CherryUtil.replaceMQSpecialChar(list.get(i));
			if (i == 0) {
				buf
						.append("[CommLine],BrandCode,TradeNoIF,ModifyCounts,CounerCode,RelevantCounterCode,TotalQuantity,"
								+ "TotalAmount,TradeType,SubType,RelevantNo,Reason,TradeDate,TradeTime,TotalAmountBefore,TotalAmountAfer,MemberCode,PlanArriveDate");
				buf.append(str1);
				buf.append("[MainDataLine]");
				buf.append(str2);
				// 品牌代码
				buf.append(brandCode);
				buf.append(str2);
				// 单据号
				buf.append(ConvertUtil.getString(map.get("mDeliverReceiveNo")));
				buf.append(str2);
				// 修改回数
				buf.append(str2);
				// 柜台号
				buf.append(ConvertUtil.getString(map.get("CounterCode")));
				buf.append(str2);
				// 关联柜台号/办事处代号
				buf.append(ConvertUtil.getString(organizationCode));
				buf.append(str2);
				// 总数量
				buf.append(ConvertUtil.getString(map.get("mTotalQuantity")));
				buf.append(str2);
				// 总金额
				buf.append(ConvertUtil.getString(map.get("mTotalAmount")));
				buf.append(str2);
				// 业务类型
				buf.append("IS");
				buf.append(str2);
				// 子类型(发货类型)
				buf.append(ConvertUtil.getString(map.get("mDeliverType")));
				buf.append(str2);
				// 关联单号
				buf.append(ConvertUtil.getString(map.get("mRelevanceNo")));
				buf.append(str2);
				// 出入库理由
				buf.append(ConvertUtil.getString(map.get("ReasonAll")));
				buf.append(str2);
				// 出入库日期
				String datetime = ConvertUtil.getString(map.get("mDeliverDate"));
				// buf.append(datetime.substring(0, 10));
				buf.append(datetime);
				buf.append(str2);
				// 出入库时间
				buf.append("00:00:00");
				buf.append(str2);
				// 入出库前柜台库存总金额
				buf.append(str2);
				// 入出库后柜台库存总金额
				buf.append(str2);
				// 会员号
			    buf.append(str2);
			    // 预计到货日期
			    buf.append(ConvertUtil.getString(map.get("mPlanArriveDate")));

				buf.append(str1);
				buf.append("[CommLine],TradeNoIF,ModifyCounts,BAcode,StockType,Barcode,Unitcode,InventoryTypeCode,"
						+ "Quantity,QuantityBefore,Price,Reason,ReferencePrice");
				buf.append(str1);
			}
			buf.append("[DetailDataLine]");
			buf.append(str2);
			// 单据号
			buf.append(ConvertUtil.getString(map.get("mDeliverReceiveNo")));
			buf.append(str2);
			// 修改回数
			buf.append(str2);
			// BA卡号
			buf.append(str2);
			// 入出库区分
			buf.append("0");
			buf.append(str2);
			// 商品条码
			buf.append(ConvertUtil.getString(map.get("BarCode")));
			buf.append(str2);
			// 厂商编码
			buf.append(ConvertUtil.getString(map.get("UnitCode")));
			buf.append(str2);
			// 仓库类型（逻辑仓库）
//			buf.append("0");
			buf.append(str2);
			// 数量
			buf.append(ConvertUtil.getString(map.get("Quantity")));
			buf.append(str2);
			// 入出库前该商品柜台账面数量（针对盘点）
			buf.append(str2);
			// 价格
			buf.append(ConvertUtil.getString(map.get("Price")));
			buf.append(str2);
			// 单个商品出入库理由
			buf.append(ConvertUtil.getString(map.get("Comments")));
			buf.append(str2);
            // 参考价格
            buf.append(ConvertUtil.getString(map.get("ReferencePrice")));
            //注意：保证CommLine和DetailDataLine的逗号数量一样，否则终端接收时会报错。
            
			buf.append(str1);
		}
		buf.append("[End]");
		System.out.print(buf.toString());
		return buf.toString();
	}

	/**
	 * 验证部门类型是否是柜台且柜台属性【是否有POS机】应为空或者非0
	 * 注：主要用于判断是否发送MQ
	 * 
	 * @param organizationId
	 * @return
	 */
	public boolean checkOrganizationType(String organizationId) {
		// 取得部门类型
		String organizationType = getOrganizationType(organizationId);
		// 部门类型是柜台的时候
		if (CherryConstants.ORGANIZATION_TYPE_FOUR.equals(organizationType)) {
		    //云POS不发MQ
		    Map<String,Object> paramMap = new HashMap<String,Object>();
		    paramMap.put("organizationId", organizationId);
		    Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramMap);
		    //是否有POS机 0：否  1：是
            if (null != counterInfo && ConvertUtil.getString(counterInfo.get("posFlag")).equals("0")) {
                return false;
            } else {
                return true;
            }
		}
		return false;
	}

	/**
	 * 取得部门类型
	 * 
	 * @param organizationId
	 * @return
	 */
	public String getOrganizationType(String organizationId) {
		// 取得部门类型
		String organizationType = binolstcm07_service.getOrganizationType(organizationId);
		return organizationType;
	}
}
