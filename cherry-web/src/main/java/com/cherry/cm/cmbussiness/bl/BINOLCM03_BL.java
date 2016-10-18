/*		
 * @(#)BINOLCM03_BL.java     1.0 2010/11/08		
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
package com.cherry.cm.cmbussiness.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.annota.TimeLog;
import com.cherry.cm.cmbussiness.service.BINOLCM03_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 * 共通处理：
 * 取得各种业务类型的单据流水号
 * @author dingyc
 *
 */
public class BINOLCM03_BL {
	
	@Resource
	private BINOLCM03_Service binolcm03_service;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM03_BL.class);
	
	private static final Map<String,String> PREFIX_MAP;
	
	static {
		PREFIX_MAP = new HashMap<String, String>();
		PREFIX_MAP.put("1","SD");
		PREFIX_MAP.put("2","RD");
		PREFIX_MAP.put("3","RR");
		PREFIX_MAP.put("4","AR");
		PREFIX_MAP.put("5","BG");
		PREFIX_MAP.put("6","LG");
		PREFIX_MAP.put("7","GR");
		PREFIX_MAP.put("8","OT");
		PREFIX_MAP.put("9","MC");
		PREFIX_MAP.put("P","CA");
		PREFIX_MAP.put("N","NS");
		PREFIX_MAP.put("R","SR");
		PREFIX_MAP.put("O","OD");
		PREFIX_MAP.put("S","SP");
		PREFIX_MAP.put("D","HD");
		PREFIX_MAP.put("CP","CP");
		PREFIX_MAP.put("CB","CB");
	}

	/**
	 * 取得单据号
	 * @param orgId 组织ID
	 * @param brandId 品牌ID
	 * @param userName 用户登录名
	 * @param type 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@TimeLog
	public String getTicketNumber(String orgId,String brandId,String userName,String type){
		Map<String, Object> map = new HashMap<String, Object>();
		String prefix = getPrefix(type);
		map.put("BIN_OrganizationInfoID", orgId);
		map.put("BIN_BrandInfoID", brandId);
		map.put("Type", prefix);
		map.put("PrefixCode", prefix);
		map.put("createdBy", userName);
		map.put("createPGM", "BINOLCM03");
		map.put("updatedBy", userName);
		map.put("updatePGM", "BINOLCM03");
		
        List bussinessDateList = binolcm03_service.getBussinessDateNum(map);
        String bussinessDate;
        if(bussinessDateList!=null&&bussinessDateList.size()>0){
            Map<String,Object> temp = ((Map<String, Object>) bussinessDateList.get(0));
            bussinessDate = String.valueOf(temp.get("BussinessDate"));
        }else{
            SimpleDateFormat dateFm = new SimpleDateFormat("yyMMdd"); //格式化当前系统日期
            bussinessDate = dateFm.format(new java.util.Date());
            logger.warn("业务日期取不到，取当前的系统日期。");
        }
        map.put("ControlDate", bussinessDate);
		
//		//binolcm03_service.startTransaction();
//		int identity = binolcm03_service.insertTicketNumber(map);
//		//binolcm03_service.commitTransaction();
//		map.put("BIN_TicketNumberID", identity);
		List list = binolcm03_service.getTicketNumber(map);
		String maxNo = CherryUtil.getMaxNoInList(list);
		
		Map temp = null;;

		String strIdentity=getStrIdentity(Integer.parseInt(maxNo),7);
		String strBrandID=getStrIdentity(Integer.parseInt(brandId),3);
		
//		list = binolcm03_service.getBussinessDateNum(map);
		String dateTime;
		if(bussinessDateList!=null&&bussinessDateList.size()>0){
			temp = (Map)(bussinessDateList.get(0));
			dateTime= String.valueOf(temp.get("BussinessDate"));
			dateTime=dateTime.substring(2, 8);
		}else{
			SimpleDateFormat dateFm = new SimpleDateFormat("yyMMdd"); //格式化当前系统日期
			dateTime = dateFm.format(new java.util.Date());
		}
		
		if(orgId.length()==1){
			orgId="0"+orgId;
		}
		if("9".equals(type)){
			//活动编码
			return prefix+dateTime+strIdentity;	
		}else if("D".equals(type)){
			//活动编码
			return prefix+dateTime+strIdentity.substring(2, 6);	
		} else if ("AS".equals(type) || "AT".equals(type)) {
//			try {
//				// 取得前进的系统时间
//				String sysDate = binolcm03_service.getForwardSYSDate();
//				Date date = DateUtil.coverString2Date(sysDate, DateUtil.DATETIME_PATTERN);
//				SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
//				String sysTime = dateFm.format(date);
//				StringBuffer buffer = new StringBuffer();
//				buffer.append("A").append(prefix).append("CR").append(sysTime).append(strIdentity.substring(4));
//				return buffer.toString();
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//				return null;
//			}
			//活动编码
			return prefix+dateTime+strIdentity;	
		}
		
		//单据号前缀(2位)+组织ID(2位)+品牌ID(3位)+日期（YYMMDD)+单据号连番（7位）
		return prefix+orgId+strBrandID+dateTime+strIdentity;
	}
	
	/**
	 * 取得单据号
	 * @param orgId 组织ID
	 * @param brandId 品牌ID
	 * @param userName 用户登录名
	 * @param type 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTicketNumber(int orgId,int brandId,String userName,String type){	
		return getTicketNumber(String.valueOf(orgId),String.valueOf(brandId),userName,type);
	}
	/**
	 * 取得一个单号区间，从BIN_SequenceCode中取�?
	 * @param orgId
	 * @param brandId
	 * @param userName
	 * @param type
	 * @param count
	 * @return
	 */
	public Map getActivityCodeList(String orgId,String brandId,String userName,String type,int count){
		//业务类型N和R有相同的单据号前缀，所以会取出重复的单据号，这里做下对�?
	
		HashMap<String,Object> retMap = new HashMap<String,Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BIN_OrganizationInfoID", orgId);
		map.put("BIN_BrandInfoID", brandId);
		map.put("Type", type);
		map.put("createdBy", userName);
		map.put("createPGM", "BINOLCM03");
		map.put("updatedBy", userName);
		map.put("updatePGM", "BINOLCM03");
		map.put("numcounts", count);
		
		//binolcm03_service.startTransaction();
		List list = binolcm03_service.getSequenceCode(map);
		int iIdentity = Integer.parseInt(CherryUtil.getMaxNoInList(list));
		retMap.put("minnum", iIdentity-count+1);
		retMap.put("maxnum", iIdentity);
		return retMap;
	}
	/**
	 * 根据类型值返回单据前缀
	 * @param type
	 * @return
	 */
	private String getPrefix(String type){
		type = type.toUpperCase();
		//1:仓库发货，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库(退货) 9:活动主码, O:订货 ,MV:移库
	    //KS:金蝶K3导入发货单 ML:下发等级  MG:下发化妆次数 AS:会员活动 AT:会员子活动
		//OT:订发货类型，SE:BA信息下发
	    //RA：退库申请，RJ：退库审核
	    //KS:金蝶K3导入发货单 ML:下发等级  MG:下发化妆次数 AS:会员活动 AT:会员子活动
		//OT:订发货类型，SE:BA信息下发，CT:柜台信息下发
		//MP:下发积分
	    //CR：盘点申请，CJ：盘点审核
		//PT: 积分维护
		//SM：短信发送EM：邮件发送 EC：人员批次
		//CP：优惠券前缀
		//CB：优惠券BATCH批次前缀
		String prefix = PREFIX_MAP.get(type);
		return null != prefix ? prefix : type;
	}
	/**
	 * 根据流水号返回len位长度的流水号字符串
	 * @param type
	 * @return
	 */
	private String getStrIdentity(int identity,int len){
		String temp = String.valueOf(identity);
		while(temp.length()<len){
			temp = "0"+temp;
		}
		return temp;
	}
	
	public String getBussinessNameByBillNo(String billNo){
		
		String ret = "global.BussinessName.Unknown";
		if(null==billNo||"".equals(billNo)){
			return ret;
		}
		if(billNo.length()>2){
			//1:SD 2:RD 3：RR 4：AR  5:BG  6:LG  7：GR 8:OT  P:CA  N：NS R:SR 9:MC 
			//S:SP O:OD
			//MG:MG  MV：MV  LS:LS
			String prefix = billNo.substring(0, 2);
			if("SD".equals(prefix)){
				ret = "global.BussinessName.SD";
			}else if("RD".equals(prefix)){
				ret = "global.BussinessName.RD";
			}else if("RR".equals(prefix)){
				ret = "global.BussinessName.RR";
			}else if("AR".equals(prefix)){
				ret = "global.BussinessName.AR";
			}else if("BG".equals(prefix)){
				ret = "global.BussinessName.BG";
			}else if("LG".equals(prefix)){
				ret = "global.BussinessName.LG";
			}else if("GR".equals(prefix)){
				ret = "global.BussinessName.GR";
			}else if("OT".equals(prefix)){
				ret = "global.BussinessName.OT";
			}else if("CA".equals(prefix)){
				ret = "global.BussinessName.CA";
			}else if("NS".equals(prefix)){
				ret = "global.BussinessName.NS";
			}else if("SR".equals(prefix)){
				ret = "global.BussinessName.SR";
			}else if("MC".equals(prefix)){
				ret = "global.BussinessName.MC";
			}else if("SP".equals(prefix)){
				ret = "global.BussinessName.SP";
			}else if("OD".equals(prefix)){
				ret = "global.BussinessName.OD";
			}else if("MG".equals(prefix)){
				ret = "global.BussinessName.MG";
			}else if("MV".equals(prefix)){
				ret = "global.BussinessName.MV";
			}else if("LS".equals(prefix)){
				ret = "global.BussinessName.LS";
			}			
		}
		return ret;
	}
	
	/**
	 * 取得一组单据号
	 * 
	 * @param actConResultList
	 */
	public List<String> getTicketNumberList(Map<String, Object> map,String type,int count) {
		String orgInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		String businessDate = ConvertUtil.getString(map.get(CherryConstants.BUSINESS_DATE));
		
		int ticketNum = getTicketNumList(orgInfoId, brandInfoId,businessDate, "", type, count);
		if(ticketNum != 0){
			businessDate = businessDate.replaceAll("-", "").substring(2, 8);
			int start = ticketNum - count + 1;
			List<String> ticketNumberList = new ArrayList<String>();
			for (int i=start; i<=ticketNum; i++) {
				// 流水号
				String ticketNumber = getTicket(orgInfoId,brandInfoId,businessDate,i,type);
				ticketNumberList.add(ticketNumber);
			}
			return ticketNumberList;
		}
		return null;
	}
	
	public int getTicketNumList(String orgId,String brandId,String bussinessDate,String userName,String type,int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		String prefix = getPrefix(type);
		map.put("BIN_OrganizationInfoID", orgId);
		map.put("BIN_BrandInfoID", brandId);
		map.put("Type", prefix);
		map.put("PrefixCode", prefix);
		map.put("createdBy", userName);
		map.put("createPGM", "BINOLCM03");
		map.put("updatedBy", userName);
		map.put("updatePGM", "BINOLCM03");
		map.put("numcounts", count);
		map.put("ControlDate", bussinessDate);
		return binolcm03_service.getTicketNum(map);
	}
	
	private String getTicket(String orgInfoId,String brandId,String businessDate,int num,String type){
		String strIdentity=getStrIdentity(num,7);
		String strBrandID=getStrIdentity(Integer.parseInt(brandId),3);
		String prefix = getPrefix(type);
		if(orgInfoId.length()==1){
			orgInfoId="0"+orgInfoId;
		}
		if("9".equals(type)){
			//活动编码
			return prefix+businessDate+strIdentity;	
		}else if("D".equals(type)){
			//活动编码
			return prefix+businessDate+strIdentity.substring(2, 6);	
		} else if ("AS".equals(type) || "AT".equals(type) || "CP".equals(type)) {
			//活动编码
			return prefix+businessDate+strIdentity;	
		}
		//单据号前缀(2位)+组织ID(2位)+品牌ID(3位)+日期（YYMMDD)+单据号连番（7位）
		return prefix+orgInfoId+strBrandID+businessDate+strIdentity;
	}
}
