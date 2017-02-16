package com.cherry.cm.cmbussiness.bl;

import com.cherry.cm.cmbussiness.service.BINOLCM09_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@SuppressWarnings("unchecked")
public class BINOLCM09_BL {

	private static Logger logger = LoggerFactory.getLogger(BINOLCM09_BL.class
			.getName());

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name = "binOLCM09_Service")
	private BINOLCM09_Service ser;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

//	/** 成功条数 */
//	private int successCount = 0;
//
//	/** 失败条数 */
//	private int failCount = 0;

	/** 取得共通Map */
	Map<String, Object> baseMap = null;

	/** 所有有效柜台 */
	Map<String, Map<String, Object>> allCntMap = null;

	/** 规则基础属性-时间ID */
	int basePropTimeId = 0;

	String orgInfoId = null;

	String brandInfoId = null;

	String brandCode = null;
	
	String stopTime = null;
	
	private String priceFlag = null;

	private final String key_mainCode = "mainCode";

	private final String key_counterOrgId = "counterOrgId";

	private final String key_prtType = "prtType";
	
	private final String key_endTime = "endTime";

	private final String key_prmPrtVendorId = "prmPrtVendorId";

	/**
	 * 将促销活动下发给终端接口表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int tran_publicProActive(Map<String, Object> map)
			throws CherryBatchException {
		// 取得共通Map
		baseMap = getBaseMap(map);
		// 活动价小数处理配置项
		priceFlag = cm14_bl.getConfigValue("1285",orgInfoId,brandInfoId);
		// 取得无效用户List
		List<Integer> invalidUserList = ser.getInvalidUserList();
		// 查询活动信息数据(ActivityAssociateTable_CHY数据)
		List<Map<String, Object>> actList = ser.getActiveInfoList(map);
		try {
			
			if (null != actList && actList.size() > 0) {
				// 更新正在进行中及过期补登的促销活动，促销活动组接口表
//				logger.info("==更新正在进行中及过期补登的促销活动，促销活动组接口表-START==");
				updateAct_SCS(actList);
//				logger.info("==更新正在进行中及过期补登的促销活动，促销活动组接口表-END==");
				for (int i = 0; i < actList.size(); i++) {
					Map<String, Object> act = actList.get(i);
					act.put("mainName", 
							ConvertUtil.getString(act.get("mainName")).replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", ""));
//					logger.info("==正在处理【" + act.get("mainName")+ "】==");
					// 活动设置者
					int userId = ConvertUtil.getInt(act.get("userID"));
					if (!invalidUserList.contains(userId)) {
						// 活动数据(ActivityTable_CHY数据)
						getActiveData(act);
					}
				}
			}
			String mainCode = ConvertUtil.getString(map.get("mainCode"));
			// 更新停用的促销活动sendFlag=1
			ser.updatePrmActivity2(brandInfoId,mainCode);
			// 查询需要停用的促销活动
			List<Map<String, Object>> stopActList = null;
			if("".equals(mainCode)){
				logger.info("===========正在处理停用的促销活动===========");
				// 查询需要停用的促销活动
				stopActList = ser.getStopActList(map);
				if (null != stopActList && stopActList.size() > 0) {
					for (Map<String, Object> stopAct : stopActList) {
						stopAct.put("brandInfoID", brandInfoId);
						stopAct.put("brandCode", brandCode);
					}
					// 停止柜台活动接口表
					ser.stopActivityTable(stopActList);
					// 停止柜台活动下发历史
					ser.stopActivityTransHis(stopActList);
					// 停止活动接口表
					ser.stopActivityAssociateTable(stopActList);
					// 更新停用的促销活动sendFlag=4
					ser.updatePrmActivity(brandInfoId);
				}
			}
			if ((null != actList && actList.size() > 0) 
					|| (null != stopActList && stopActList.size() > 0)) {
				ser.manualCommit();
				// 事务提交
				ser.ifManualCommit();
			}
		} catch (Exception e) {
			try {
				ser.manualRollback();
				// 事务回滚
				ser.ifManualRollback();
			} catch (Throwable e1) {
				logger.warn("事务回滚警告");
			}
			StringWriter writer = new StringWriter();
			e.printStackTrace();
			e.printStackTrace(new PrintWriter(writer, true));
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EIF00009");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
					this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			flag = CherryBatchConstants.BATCH_WARNING;
		} catch (Throwable t) {
			logger.error("&&&&&&BINOLCM09_BL_Throwable="+t.getMessage());
			t.printStackTrace();
			try {
				ser.manualRollback();
				// 事务回滚
				ser.ifManualRollback();
			} catch (Throwable e1) {
				logger.warn("事务回滚警告");
			}
			// 失败件数加一
//			failCount++;
			flag = CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}

	/**
	 * 取得活动数据(ActivityTable_CHY数据)
	 * 
	 * @param act
	 */
	private void getActiveData(Map<String, Object> act) {
		// 柜台活动LIST
		List<Map<String, Object>> cntActList = new ArrayList<Map<String, Object>>();
		// 新增加的柜台活动
		List<Map<String, Object>> newCntActList = new ArrayList<Map<String, Object>>();
		// 活动与奖励LIST
		List<Map<String, Object>> actGiftList = new ArrayList<Map<String, Object>>();
		// 当前活动参数Map
		Map<String, Object> parmMap = new HashMap<String, Object>(act);
		// 已下发过的柜台活动
		List<Map<String, Object>> cntActHisList = ser.getActHisList(parmMap);
		// 取得活动礼品
		List<Map<String, Object>> giftList = ser.getGiftList(parmMap);
		if (null != giftList && giftList.size() > 0) {
			for (Map<String, Object> gift : giftList) {
				if("1".equals(priceFlag)){
					// 价格处理
					float price = ConvertUtil.getFloat(gift.get("price"));
					gift.put("price", Math.round(price));
				}
				gift.putAll(act);
				actGiftList.add(gift);
			}
			// 取得活动柜台
			List<Map<String, Object>> cntList = getCntList(parmMap);
			if (null != cntList && cntList.size() > 0) {
				for (Map<String, Object> cnt : cntList) {
					for (Map<String, Object> actGift : actGiftList) {
						Map<String, Object> cntAct = new HashMap<String, Object>(actGift);
						cntAct.putAll(cnt);
						cntActList.add(cntAct);
					}
				}
				// 设置柜台活动activityCode
				setCntActList(cntActList, newCntActList, cntActHisList);
			} else {
				logger.warn("===活动【" + act.get("mainName") + "】无活动柜台===");
			}
			if(cntActList.size() > 0){
				// ①物理清除接口及历史记录
				ser.clearActivityTable(cntActList);
				ser.clearActivityTransHis(cntActList);
			}
			if(cntActHisList.size() > 0){
				for(Map<String,Object> his : cntActHisList){
					his.put("stopTime", stopTime);
					his.put("brandInfoID", brandInfoId);
					his.put("brandCode", brandCode);
				}
				// ②停用柜台活动
				ser.stopActivityTable(cntActHisList);
				ser.stopActivityTransHis(cntActHisList);
			}
			if(cntActList.size() > 0 || newCntActList.size() > 0){
				// ③插入需要更新及新增的柜台活动到接口及历史表
				cntActList.addAll(newCntActList);
				ser.addActivityTable_CHY(cntActList);
				ser.addActivityTransHis(cntActList);
			}
		}
	}

	/**
	 * 设置柜台活动actvityCode
	 * 
	 * @param cntActList：return[更新]
	 * @param newCntActList return[新增]
	 *  @param cntActHisList return[停用]
	 */
	private void setCntActList(List<Map<String, Object>> cntActList,
			List<Map<String, Object>> newCntActList,
			List<Map<String, Object>> cntActHisList) {
		for (int i = 0; i < cntActList.size(); i++) {
			boolean isNew = true;
			Map<String, Object> act = cntActList.get(i);
			String mainCode_i = CherryBatchUtil
					.getString(act.get(key_mainCode));
			int departId_i = ConvertUtil.getInt(act
					.get(key_counterOrgId));
			String prtType_i = CherryBatchUtil.getString(act.get(key_prtType));
			int id_i = ConvertUtil.getInt(act.get(key_prmPrtVendorId));
			String endTime_i = CherryBatchUtil
					.getString(act.get(key_endTime));
			for (int j = 0; j < cntActHisList.size(); j++) {
				Map<String, Object> actHis = cntActHisList.get(j);
				String mainCode_j = CherryBatchUtil.getString(actHis
						.get(key_mainCode));
				int departId_j = ConvertUtil.getInt(actHis
						.get(key_counterOrgId));
				String prtType_j = CherryBatchUtil.getString(actHis
						.get(key_prtType));
				int id_j = ConvertUtil.getInt(actHis
						.get(key_prmPrtVendorId));
				String endTime_j = CherryBatchUtil
						.getString(actHis.get(key_endTime));
				// 已下发
				if (mainCode_i.equals(mainCode_j)&& departId_i==departId_j
						&& prtType_i.equals(prtType_j) && id_i==id_j) {
					isNew = false;
					if(endTime_i.equals(endTime_j)){//活动截止时间也相同
						cntActList.remove(i);
						i--;
					}else{
						act.put("activeCode", actHis.get("activeCode"));
					}
					cntActHisList.remove(j);
					j--;
					break;
				}
			}
			if (isNew) {
				newCntActList.add(act);
				cntActList.remove(i);
				i--;
			}
		}
		if (null != newCntActList && newCntActList.size() > 0) {
			Map<String, Object> codes = binOLCM03_BL.getActivityCodeList(
					orgInfoId, brandInfoId, "", "D", newCntActList.size());
			// 取得一组单据号
			List<String> codeList = getTicketNumberList(codes);
			int i = 0;
			for (Map<String, Object> newCntAct : newCntActList) {
				if (null == newCntAct.get("activeCode")) {
					newCntAct.put("activeCode", codeList.get(i));
					i++;
				}
			}
		}
	}

	/**
	 * 取得活动柜台
	 * 
	 * @param map
	 * @return
	 */
	private List<Map<String, Object>> getCntList(Map<String, Object> map) {
		// 取得活动柜台
		List<Map<String, Object>> cntList = null;
		// 查询活动条件-活动地点
		List<Map<String, String>> placeList = ser.getPlaceList(map);
		if (null != placeList && placeList.size() > 0) {
			cntList = new ArrayList<Map<String, Object>>();
			for (Map<String, String> place : placeList) {
				String propName = place.get("propName");
				String propVal = place.get("propVal");
				List<Map<String, Object>> tempList = null;
				if ("baseProp_city".equals(propName)) {// 城市
					// 全部柜台
					if ("ALL".equalsIgnoreCase(propVal)) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						// 柜台码
						resultMap.put("counterID", "ALL");
						// 柜台ID
						resultMap.put("counterOrgId", "-8888");
						cntList.add(resultMap);
					} else {
						map.put("cityID", propVal);
						tempList = ser.getCounterByIdCity(map);
						cntList.addAll(tempList);
					}
				} else if ("baseProp_channal".equals(propName)) {// 渠道
					// 根据渠道ID查询柜台
					map.put("channelID", propVal);
					tempList = ser.getCounterByIdChannel(map);
					cntList.addAll(tempList);
				}else if ("baseProp_faction".equals(propName)) {// 所属系统
					// 根据所属系统ID查询柜台
					map.put("factionID", propVal);
					tempList = ser.getCounterByIdFaction(map);
					cntList.addAll(tempList);
				}else if("baseProp_organization".equals(propName)){// 所属组织
					map.put("organizationID", propVal);
					tempList = ser.getCounterByIdOrganization(map);
					cntList.addAll(tempList);
				} 
				else if ("baseProp_counter".equals(propName)) {// 柜台
					if (null == allCntMap || allCntMap.isEmpty()) {
						allCntMap = new HashMap<String, Map<String, Object>>();
						// 所有有效柜台
						List<Map<String,Object>>allCntList = ser.getAllCntList(map);
						// LIST TO MAP
						if (null != allCntList) {
							for (Map<String, Object> cnt : allCntList) {
								String cntCode = CherryBatchUtil.getString(cnt.get("counterID"));
								allCntMap.put(cntCode.toUpperCase(), cnt);
							}
						}
					}
					if(null != allCntMap && !allCntMap.isEmpty()){
						Map<String,Object> cntMap = allCntMap.get(propVal.toUpperCase());
						if(null != cntMap){
							cntList.add(cntMap);
						}
					}
				}
			}
			if (null != cntList && cntList.size() > 0){
				List<Map<String,Object>>blackCntList = ser.getBlackCntList(map);
				if (null != blackCntList && blackCntList.size() > 0){
					//如果为全部柜台，则将全部柜台取出
					if (ConvertUtil.getString(cntList.get(0).get("counterID")).equals("ALL")){
						cntList=ser.getAllValidCntList(map);
					}
					for (int i = 0; i < cntList.size(); i++){
						String counterID = ConvertUtil.getString(cntList.get(i).get("counterID"));
						for (int j = 0; j < blackCntList.size(); j++){
							String blackCntId = ConvertUtil.getString(blackCntList.get(j).get("counterCode"));
							if (counterID.equals(blackCntId)){
								cntList.remove(i);
								i--;
								break;
							}
						}
					}
				}
			}
		}
		return cntList;
	}

	/**
	 * 取得共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getBaseMap(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		orgInfoId = CherryBatchUtil
				.getString(map.get("bin_OrganizationInfoID"));
		brandInfoId = CherryBatchUtil.getString(map.get("brandInfoID"));
		brandCode = CherryBatchUtil.getString(map.get("brandCode"));
		// 品牌ID
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put("brandCode", brandCode);
		// 取得业务日期
		String busDate = ser.getBusDate(paramMap);
		String busDate2 = ser.getBussinessDate(paramMap);
		// 取得业务日期：不计日结状态 + ‘时分秒’
		stopTime = DateUtil.date2String(new Date(), DateUtil.DATETIME_PATTERN);
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 组织ID
		baseMap.put("bin_OrganizationInfoID", orgInfoId);
		// 品牌ID
		baseMap.put("brandInfoID", brandInfoId);
		// 品牌code
		baseMap.put("brandCode", brandCode);
		// 促销活动
		baseMap.put("activityTypeFlag", "0");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOLCM09");
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOLCM09");
		// 业务日期
		baseMap.put("busDate", busDate);
		baseMap.put("busDate2", busDate2);
		// 业务日期
		map.put("busDate", busDate);
		map.put("busDate2", busDate2);
		return baseMap;
	}

	/**
	 * 取得一组单据号
	 * 
	 * @param ticketNumbers
	 */
	private List<String> getTicketNumberList(Map<String, Object> ticketNumbers) {
		if (null != ticketNumbers && !ticketNumbers.isEmpty()) {
			List<String> ticketNumberList = new ArrayList<String>();
			// 最小数
			int minnum = Integer.parseInt(ticketNumbers.get("minnum")
					.toString());
			// 最大数
			int maxnum = Integer.parseInt(ticketNumbers.get("maxnum")
					.toString());
			for (int i = minnum; i <= maxnum; i++) {
				// 流水号
				String ticketNumber = getStrIdentity(i, 10);
				ticketNumberList.add(ticketNumber);
			}
			return ticketNumberList;
		}
		return null;
	}

	private String getStrIdentity(int identity, int len) {
		String temp = String.valueOf(identity);
		while (temp.length() < len) {
			temp = "0" + temp;
		}
		return temp;
	}

	/**
	 * 更新正在进行中的促销活动，促销活动组接口表
	 * 
	 * @param actList
	 */
	private void updateAct_SCS(List<Map<String, Object>> actList) {
		if (null != actList && actList.size() > 0) {
			for (Map<String, Object> act : actList) {
				act.putAll(baseMap);
			}
			// 取得活动组LIST
			List<Map<String, Object>> mainList = CherryBatchUtil
					.getNotRepeatList(actList, "mainCode");
			// 删除促销活动接口表
			ser.clearActivityAssociateTable(mainList);
			// 插入促销活动接口表
			ser.addActivityAssociateTable_CHY(mainList);
			// 取得活动组LIST
			List<Map<String, Object>> actGroupList = CherryBatchUtil
					.getNotRepeatList(actList, "subjectCode");
			// 删除促销活动接口表
			ser.deleteActivityAssociateSubject(actGroupList);
			// 插入促销活动接口表
			ser.addActAssSubject_SCS(actGroupList);
		}
	}
}
