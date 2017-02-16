package com.cherry.cp.act.bl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM09_Service;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.act.service.BINCPMEACT01_Service;

/**
 * 会员活动下发BL
 * 
 * @author lipc
 * 
 */
@SuppressWarnings("unchecked")
public class BINCPMEACT01_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINCPMEACT01_BL.class);
	
	private int totalCount = 0;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;
	
	@Resource
	private BINOLCM09_Service binOLCM09_Service;

	@Resource
	private BINCPMEACT01_Service bincpmeact01_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 新后台事务 */
	private boolean DO_TRANSACTION = false;
	
	/** 接口事务 */
	private boolean DO_TRANSACTION_IF = false;
	
	/** 取得共通Map */
	private Map<String, Object> commMap = null;
	
	private String busDate = null;
	
	private String priceFlag = null;

	/** 需要下发到接口的会员活动 */
	private List<Map<String, Object>> allActList = null;
	
	/** 无需下发到接口的会员活动 */
	private List<Map<String, Object>> disActList = null;

	/** 全部的会员柜台活动 */
	private List<Map<String, Object>> allCntActList = null;

	/** 历史下发的会员柜台活动 */
	private List<Map<String, Object>> hisCntActList = null;

	/** 新增的会员活动 */
	private List<Map<String, Object>> addActList = null;

	/** 删除的会员活动 */
	private List<Map<String, Object>> delActList = null;

	private String key_subjectCode = "subjectCode";

	private String key_mainCode = "mainCode";

	private String key_state = "state";
	
	private String key_startTime = "startTime";
	
	private String key_endTime = "endTime";

	private String key_counterOrgId = "counterOrgId";

	private String key_prtType = "prtType";

	private String key_prmPrtVendorId = "prmPrtVendorId";

	/**
	 * 下发会员活动BL入口
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_publicActive(Map<String, Object> map)
			throws CherryBatchException {
		// 活动下发初始化
		init(map);
		try {
			logger.outLog("====操作柜台活动[optionCntActivity]开始====");
			// 操作会员柜台活动
			optionCntActivity();
			logger.outLog("====操作会员活动[optionActivity]开始====");
			// 操作会员活动
			optionActivity();
			logger.outLog("====操作会员主题活动[optionSubjectActivity]开始====");
			// 操作主题会员活动
			optionSubjectActivity();
			logger.outLog("====操作会员活动总数：" + totalCount + " ====");
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
			// 事务回滚
			doTransAction(-1);
		}
		commMap = null;
		allActList = null;
		disActList = null;
		allCntActList = null;
		hisCntActList = null;
		addActList = null;
		delActList = null;
		return flag;
	}

	/**
	 * 活动下发初始化
	 * 
	 * @param map
	 */
	private void init(Map<String, Object> map) {
		allCntActList = new ArrayList<Map<String, Object>>();
		String orgInfoId = ConvertUtil.getString(map.get(CherryBatchConstants.ORGANIZATIONINFOID));
		String brandInfoId = ConvertUtil.getString(map.get(CherryBatchConstants.BRANDINFOID));
		map.put("bin_OrganizationInfoID",orgInfoId);
		map.put("brandInfoID", brandInfoId);
		// 活动价小数处理配置项
		priceFlag = cm14_bl.getConfigValue("1285",orgInfoId,brandInfoId);
		// 取得日结状态确定的业务日期
		busDate = bincpmeact01_Service.getBusDate(map);
		String sysdateTime = bincpmeact01_Service.getSYSDateTime();
		String sysHHSSMM = DateUtil.getSpecificDate(sysdateTime,DateUtil.TIME_PATTERN );

		map.put("optTime", sysdateTime);
		map.put("busDate", busDate);
		map.put("busDateTime", busDate + " " + sysHHSSMM);
		commMap = new HashMap<String, Object>(map);
		commMap.put(CherryBatchConstants.CREATEDBY, "BATCH");
		commMap.put(CherryBatchConstants.UPDATEDBY, "BATCH");
		commMap.put(CherryBatchConstants.CREATEPGM, "BINCPMEACT01");
		commMap.put(CherryBatchConstants.UPDATEPGM, "BINCPMEACT01");
		// 取得无效的用户List
		List<Integer> invalidUserList = binOLCM09_Service.getInvalidUserList();
		// 历史下发的会员柜台活动
		hisCntActList = bincpmeact01_Service.getActHisList(map);
		// 本次要下发的会员活动
		allActList = bincpmeact01_Service.getActiveInfoList(map);
		// 无需下发到接口的会员活动【但需要更新活动状态】
		disActList = bincpmeact01_Service.getDisSendList(map);
		// 取得本次下发给终端执行的所有会员柜台活动
		if (null != allActList) {
			for (int i = 0; i < allActList.size(); i++) {
				int start = allCntActList.size();
				Map<String, Object> act = allActList.get(i);
				act.put("mainName", 
						ConvertUtil.getString(act.get("mainName")).replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", ""));
				act.put("subjectName", 
						ConvertUtil.getString(act.get("subjectName")).replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", ""));
				act.put(CherryBatchConstants.BRAND_CODE, 
						commMap.get(CherryBatchConstants.BRAND_CODE));
				String endTime = ConvertUtil.getString(act.get(key_endTime));
				if(endTime.length() <= 10){
					act.put(key_endTime,endTime + " 23:59:59");
				}
				// 活动设置者
				int userId = Integer.parseInt(act.get(CherryBatchConstants.USERID).toString());
				// 活动设置者被停用
				if(invalidUserList.contains(userId)){
					getActivityData(act);
				}else{
					// 取得单个活动数据(ActivityTable_CHY数据)
					getActivityData(act, map);
				}
				
				int end = allCntActList.size();
				// 当前活动下无活动柜台
				if (start == end) {
					allActList.remove(i);
					i--;
				}
			}
			totalCount = allActList.size();
		}
		if(null != disActList){
			totalCount += disActList.size();
		}
	}
	/**
	 * 取得无效用户设定的活动数据
	 * 
	 * @param map
	 */
	private void getActivityData(Map<String, Object> act) {
		String mainCode = CherryBatchUtil.getString(act.get("mainCode"));
		if(null != hisCntActList){
			for(Map<String, Object> his :hisCntActList){
				String hisMainCode = CherryBatchUtil.getString(his.get("mainCode"));
				if(hisMainCode.equals(mainCode)){
					allCntActList.add(his);
				}
			}
		}
	}
	
	/**
	 * 取得单个活动数据(ActivityTable_CHY数据)
	 * 
	 * @param map
	 */
	private void getActivityData(Map<String, Object> actMap,
			Map<String, Object> map) {
		Map<String, Object> parmMap = new HashMap<String, Object>(map);
		Map<String, Object> cityMap = new HashMap<String, Object>();
		Map<String, Object> channelMap = new HashMap<String, Object>();
		Map<String, Object> factionMap = new HashMap<String, Object>();
		// 查询活动条件和结果
		List<Map<String, Object>> actConResultList = bincpmeact01_Service
				.getActConResultList(actMap);
		if (null != actConResultList) {
			for (Map<String, Object> actConResult : actConResultList) {
				// 市ID
				String cityID = CherryBatchUtil.getString(actConResult
						.get("cityID"));
				// 渠道ID
				String channelID = CherryBatchUtil.getString(actConResult
						.get("channelID"));
				// 所属系统ID
				String factionID = CherryBatchUtil.getString(actConResult
						.get("factionID"));
				// 柜台ID
				String counterID = CherryBatchUtil.getString(actConResult
						.get("counterID"));
				// 追加主题活动码信息
				actConResult.put("subjectCode", actMap.get("subjectCode"));
				// 追加子活动开始日期
				actConResult.put("startTime", actMap.get("startTime"));
				// 追加子活动结束日期
				actConResult.put("endTime", actMap.get("endTime"));
				actConResult.put("memberClubId", actMap.get("memberClubId"));
				actConResult.put("clubCode", actMap.get("clubCode"));
				if (!"".equals(counterID)) {// 活动地点为柜台
					allCntActList.add(actConResult);
				} else if (!"".equals(cityID)) {// 活动地点为市
					// 全部柜台
					if ("all".equalsIgnoreCase(cityID)) {
						// 柜台码
						actConResult.put("counterID", "ALL");
						// 柜台ID
						actConResult.put("counterOrgId", "-8888");
						allCntActList.add(actConResult);
					} else {
						List<Map<String, Object>> counterList = (List<Map<String, Object>>) cityMap
								.get(cityID);
						if (null == counterList) {
							parmMap.put("cityID", cityID);
							// 根据市ID取得柜台信息
							counterList = binOLCM09_Service
									.getCounterByIdCity(parmMap);
							if (null != counterList && counterList.size() > 0) {
								// 缓存
								cityMap.put(cityID, counterList);
							}
						}
						if (null != counterList && counterList.size() > 0) {
							for (Map<String, Object> cntMap : counterList) {
								Map<String, Object> temp = new HashMap<String, Object>(
										actConResult);
								temp.putAll(cntMap);
								allCntActList.add(temp);
							}
						}
					}
				} else if (!"".equals(channelID)) {// 活动地点渠道
					List<Map<String, Object>> counterList = (List<Map<String, Object>>) channelMap
							.get(channelID);
					if (null == counterList) {
						// 根据渠道ID查询柜台
						parmMap.put("channelID", channelID);
						counterList = binOLCM09_Service
								.getCounterByIdChannel(parmMap);
						if (null != counterList && counterList.size() > 0) {
							// 缓存
							channelMap.put(channelID, counterList);
						}
					}
					if (null != counterList && counterList.size() > 0) {
						for (Map<String, Object> cntMap : counterList) {
							Map<String, Object> temp = new HashMap<String, Object>(
									actConResult);
							temp.putAll(cntMap);
							allCntActList.add(temp);
						}
					}
				} else if (!"".equals(factionID)) {// 活动地点所属系统
					List<Map<String, Object>> counterList = (List<Map<String, Object>>) factionMap
							.get(factionID);
					if (null == counterList) {
						// 根据渠道ID查询柜台
						parmMap.put("factionID", factionID);
						counterList = binOLCM09_Service
								.getCounterByIdFaction(parmMap);
						if (null != counterList && counterList.size() > 0) {
							// 缓存
							channelMap.put(factionID, counterList);
						}
					}
					if (null != counterList && counterList.size() > 0) {
						for (Map<String, Object> cntMap : counterList) {
							Map<String, Object> temp = new HashMap<String, Object>(
									actConResult);
							temp.putAll(cntMap);
							allCntActList.add(temp);
						}
					}
				}
			}
		}
	}

	/**
	 * 下发柜台活动
	 * 
	 * @param map
	 */
	private void optionCntActivity() {
		// 新增的柜台活动
		List<Map<String, Object>> addCntActList = new ArrayList<Map<String, Object>>();
		// 删除的柜台活动初始化=历史下发的柜台活动
		List<Map<String, Object>> delCntActList = new ArrayList<Map<String, Object>>(
				hisCntActList);
		// 更新的柜台活动
		List<Map<String, Object>> updCntActList = new ArrayList<Map<String, Object>>();
		// 取得新增/删除的柜台活动
		getCntActList(addCntActList, delCntActList,updCntActList);
		// 取得新增/删除的活动
		getActList(addCntActList, delCntActList);
		// =======停用的柜台活动（接口表）==========
		if (null != delCntActList && delCntActList.size() > 0) {
			for (Map<String, Object> act : delCntActList) {
				act.putAll(commMap);
			}
			// 停用柜台活动（接口表）
			bincpmeact01_Service.clearActivityCouter(delCntActList);
			DO_TRANSACTION_IF = true;
			// 无效下发历史
			bincpmeact01_Service.setActivityHisDisabled(delCntActList);
			DO_TRANSACTION = true;
		}
		// ========更新柜台活动（接口表）==========
		if(null != updCntActList && updCntActList.size() > 0){
			bincpmeact01_Service.updCntActivity(updCntActList);
			DO_TRANSACTION_IF = true;
		}
		// ========追加新增的柜台活动（接口表）==========
		if (null != addCntActList && addCntActList.size() > 0) {
			int count = addCntActList.size();
			// 取得柜台活动码
			List<String> ticketList = binOLCM03_BL.getTicketNumberList(commMap,
					count);
			// 追加共通信息以及柜台活动码
			for (int i = 0; i < count; i++) {
				Map<String, Object> newAct = addCntActList.get(i);
				newAct.putAll(commMap);
				newAct.put("activeCode", ticketList.get(i));
				// 会员活动
				newAct.put("activityTypeFlag", "1");
			}
			// 下发会员柜台活动
			binOLCM09_Service.addActivityTable_CHY(addCntActList);
			DO_TRANSACTION_IF = true;
			// 增加下发历史
			binOLCM09_Service.addActivityTransHis(addCntActList);
			DO_TRANSACTION = true;
		}
	}

	/**
	 * 下发活动
	 * 
	 * @param map
	 */
	private void optionActivity() {
		if (null != delActList && delActList.size() > 0) {
			// 停用会员活动追加共通信息
			for (Map<String, Object> act : delActList) {
				act.putAll(commMap);
			}
			// 停用会员活动
			bincpmeact01_Service.clearActivityAssociate(delActList);
			DO_TRANSACTION_IF = true;
		}
		// ========更新活动（接口表）==========
		if(null != allActList && allActList.size() > 0){
			bincpmeact01_Service.updActivity(allActList);
			DO_TRANSACTION_IF = true;
		}
		if (null != addActList && addActList.size() > 0) {
			// 新增的会员活动追加共通信息
			for (Map<String, Object> act : addActList) {
				act.putAll(commMap);
			}
			// 下发会员活动
			binOLCM09_Service.addActivityAssociateTable_CHY(addActList);
			DO_TRANSACTION_IF = true;
		}
		// 更新会员活动状态
		optionCampRuleState(allActList, delActList,disActList);
	}

	/**
	 * 下发主题活动
	 * 
	 * @param map
	 */
	private void optionSubjectActivity() {
		if (null != allActList) {
			// 新增的主题活动
			List<Map<String, Object>> addMainActList = CherryBatchUtil
					.getNotRepeatList(addActList, key_subjectCode);
			// 删除的主题活动
			List<Map<String, Object>> delMainActList = CherryBatchUtil
					.getNotRepeatList(delActList, key_subjectCode);
			// 取得当前的主题活动
			List<Map<String, Object>> currMainActList = CherryBatchUtil.getNotRepeatList(
					allActList, key_subjectCode);
			// 剔除当前的主题活动
			if (delMainActList.size() > 0) {
				// 取得当前的主题活动KEY
				List<String> currActKeyList = CherryBatchUtil.getKeyList(
						currMainActList, key_subjectCode, false);
				if (currMainActList.size() > 0) {
					for (int i = 0; i < delMainActList.size(); i++) {
						Map<String, Object> act = delMainActList.get(i);
						String subjectCode = CherryBatchUtil.getString(act
								.get(key_subjectCode));
						if (currActKeyList.contains(subjectCode)) {
							delMainActList.remove(i);
							i--;
						}
					}
				}
				// 清空已下发的会员主题活动
				bincpmeact01_Service.clearActivitySubject(delMainActList);
				DO_TRANSACTION_IF = true;
			}
			// ========更新主题活动（接口表）==========
			if(null != currMainActList && currMainActList.size() > 0){
				bincpmeact01_Service.updActivitySubject(currMainActList);
				DO_TRANSACTION_IF = true;
			}
			// 新增的主题活动剔除历史
			if (addMainActList.size() > 0) {
				// 取得历史主题活动
				List<String> hisMainActList = CherryBatchUtil.getKeyList(
						hisCntActList, key_subjectCode, false);
				if (hisMainActList.size() > 0) {
					for (int i = 0; i < addMainActList.size(); i++) {
						Map<String, Object> act = addMainActList.get(i);
						String subjectCode = CherryBatchUtil.getString(act
								.get(key_subjectCode));
						if (hisMainActList.contains(subjectCode)) {
							addMainActList.remove(i);
							i--;
						}
					}
				}
				// 下发会员主题活动
				binOLCM09_Service.addActAssSubject_SCS(addMainActList);
				DO_TRANSACTION_IF = true;
			}
		}
		// 更新会员主题活动状态
		bincpmeact01_Service.updCampState(commMap);
		DO_TRANSACTION = true;
	}

	/**
	 * 更新会员活动状态
	 */
	private void optionCampRuleState(List<Map<String, Object>> allList,
			List<Map<String, Object>> clList,List<Map<String, Object>> disActList) {
		// 需要更新状态的会员活动
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 查找准备中（未下发）的会员活动
		if (null != allList) {
			for (Map<String, Object> act : allList) {
				String state = CherryBatchUtil.getString(act.get(key_state));
				// 不是进行中
				if(!"1".equals(state)){
					act.put(key_state, "1");
					list.add(act);
				}
			}
		}
		// 更新为已过期
		if (null != clList) {
			for (Map<String, Object> act : clList) {
				act.put(key_state, "2");
				list.add(act);
			}
		}
		// 无需下发到接口，但需要更新活动状态
		if (null != disActList) {
			for (Map<String, Object> act : disActList) {
				String startTime = CherryBatchUtil.getString(act.get(key_startTime));
				String endTime = CherryBatchUtil.getString(act.get(key_endTime));
				// 业务日期大于活动结束日期
				if(CherryChecker.compareDate(busDate, endTime) > 0){
					act.put(key_state, "2");
					list.add(act);
				}else if(CherryChecker.compareDate(busDate, startTime) >= 0){
					String state = CherryBatchUtil.getString(act.get(key_state));
					// 不是进行中
					if(!"1".equals(state)){
						act.put(key_state, "1");
						list.add(act);
					}
				}
			}
		}
		// 更新会员活动状态
		if (list.size() > 0) {
			// 追加共通信息
			for (Map<String, Object> map : list) {
				map.putAll(commMap);
			}
			bincpmeact01_Service.updSubCampState(list);
			DO_TRANSACTION = true;
		}
	}

	/**
	 * 取得新增/删除的柜台活动
	 */
	private void getCntActList(List<Map<String, Object>> addCntActList,
			List<Map<String, Object>> delCntActList,
			List<Map<String, Object>>updCntActList) {

		if (null == delCntActList || delCntActList.size() == 0) {
			addCntActList.addAll(allCntActList);
		} else {
			for (int i = 0; i < allCntActList.size(); i++) {
				boolean isNew = true;
				Map<String, Object> act = allCntActList.get(i);
				if("1".equals(priceFlag)){
					// 价格处理
					int price = (int) Math.round(Double.parseDouble(String
							.valueOf(act.get("price"))));
					act.put("price", price);
				}
				act.put(CherryBatchConstants.BRAND_CODE, 
						commMap.get(CherryBatchConstants.BRAND_CODE));
				String mainCode_i = CherryBatchUtil.getString(act
						.get(key_mainCode));
				String departId_i = CherryBatchUtil.getString(act
						.get(key_counterOrgId));
				String prtType_i = CherryBatchUtil.getString(act
						.get(key_prtType));
				String id_i = CherryBatchUtil.getString(act
						.get(key_prmPrtVendorId));
				for (int j = 0; j < delCntActList.size(); j++) {
					Map<String, Object> actHis = delCntActList.get(j);
					String mainCode_j = CherryBatchUtil.getString(actHis
							.get(key_mainCode));
					String departId_j = CherryBatchUtil.getString(actHis
							.get(key_counterOrgId));
					String prtType_j = CherryBatchUtil.getString(actHis
							.get(key_prtType));
					String id_j = CherryBatchUtil.getString(actHis
							.get(key_prmPrtVendorId));
					// 已下发
					if (mainCode_i.equals(mainCode_j)
							&& departId_i.equals(departId_j)
							&& prtType_i.equals(prtType_j) && id_i.equals(id_j)) {
						isNew = false;
						act.put("activeCode", actHis.get("activeCode"));
						updCntActList.add(act);
						delCntActList.remove(j);
						break;
					}
				}
				if (isNew) {
					addCntActList.add(act);
				}
			}
		}
	}

	/**
	 * 提取新增/删除的活动码List
	 * 
	 * @param newCntAcList
	 * @param delCntAcList
	 */
	private void getActList(List<Map<String, Object>> newCntAcList,
			List<Map<String, Object>> delCntAcList) {

		// 取得删除的活动--mainCode
		delActList = CherryBatchUtil.getNotRepeatList(delCntAcList,
				key_mainCode);
		// 新增的活动
		addActList = new ArrayList<Map<String, Object>>();
		// 取得新增的活动--mainCode
		List<String> addActKeyList = CherryBatchUtil.getKeyList(newCntAcList,
				key_mainCode, false);
		if (addActKeyList.size() > 0) {
			for (Map<String, Object> act : allActList) {
				String mainCode = CherryBatchUtil.getString(act
						.get(key_mainCode));
				if (addActKeyList.contains(mainCode)) {
					addActList.add(act);
				}
			}
		}
		// 剔除当前下发的活动
		if (delActList.size() > 0) {
			// 取得当前下发的活动--mainCode
			List<String> currActList = CherryBatchUtil.getKeyList(allActList,
					key_mainCode, false);
			if (currActList.size() > 0) {
				for (int i = 0; i < delActList.size(); i++) {
					Map<String, Object> act = delActList.get(i);
					String mainCode = CherryBatchUtil.getString(act
							.get(key_mainCode));
					if (currActList.contains(mainCode)) {
						delActList.remove(i);
						i--;
					}
				}
			}
		}
		// 剔除历史上存在的活动
		if (addActList.size() > 0) {
			// 取得历史的活动--mainCode
			List<String> hisActList = CherryBatchUtil.getKeyList(hisCntActList,
					key_mainCode, false);
			if (hisActList.size() > 0) {
				for (int i = 0; i < addActList.size(); i++) {
					Map<String, Object> act = addActList.get(i);
					String mainCode = CherryBatchUtil.getString(act
							.get(key_mainCode));
					if (hisActList.contains(mainCode)) {
						addActList.remove(i);
						i--;
					}
				}
			}
		}
	}
	/**
	 * 事务处理
	 * @param flag
	 */
	private void doTransAction(int flag){
		// commit
		if(flag == 1){
			if(DO_TRANSACTION){
				bincpmeact01_Service.manualCommit();
			}
			if(DO_TRANSACTION_IF){
				bincpmeact01_Service.ifManualCommit();
			}
		}
		// rollback
		if(flag == -1){
			// 事务回滚
			if(DO_TRANSACTION){
				bincpmeact01_Service.manualRollback();
			}
			if(DO_TRANSACTION_IF){
				bincpmeact01_Service.ifManualRollback();
			}
		}
	}
}
