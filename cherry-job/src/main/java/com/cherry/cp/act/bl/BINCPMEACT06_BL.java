package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINCPMEACT04_Service;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.cp.common.service.BINOLCPCOM05_Service;

/**
 * batch活动单据生成
 * 
 * @author lipc
 * 
 */
public class BINCPMEACT06_BL {
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINCPMEACT06_BL.class);
	@Resource(name = "bincpmeact04_Service")
	private BINCPMEACT04_Service ser4;

	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03_bl;

	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnIF;

	@Resource(name = "binolcpcom05_Service")
	private BINOLCPCOM05_Service comSer5;

	/**
	 * 保存活动单据
	 * 
	 * @param subCamp
	 * @param memList
	 * @param prtList
	 * @throws Exception
	 */
	public void tran_saveCampOrder(Map<String, Object> comMap,Map<String, Object> subCamp, List<Map<String, Object>> orderList,
			List<Map<String, Object>> prtList)throws Exception{
		String couponType = CherryBatchUtil.getString(subCamp.get("couponType"));
		List<String> couponList = null;
		if (!"3".equals(couponType)) { // 非导入coupon活动
			couponList = getCouponList(comMap,subCamp, orderList.size());
		}
		saveCampOrder(comMap,subCamp, orderList, prtList, couponList);
	}

	/**
	 * 获取couponList
	 * 
	 * @param subCamp
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private List<String> getCouponList(Map<String, Object> comMap,Map<String, Object> subCamp, int count)
			throws Exception {
		List<String> couponList = null;
		// 活动验证方式
		String subCampValid = ConvertUtil.getString(
				subCamp.get(CampConstants.SUBCAMPAIGN_VALID)).trim();
		// 在线验证
		if (("2".equals(subCampValid) || "5".equals(subCampValid))) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put(CherryBatchConstants.ORGANIZATIONINFOID, 
					comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
			param.put(CherryBatchConstants.BRANDINFOID, 
					comMap.get(CherryBatchConstants.BRANDINFOID));
			param.put("couponCount", count);
			param.put(CampConstants.CAMP_CODE,
					subCamp.get(CampConstants.CAMP_CODE));
			try {
				couponList = cpnIF.generateCoupon(param);
			} catch (Exception e) {
				// 获取COUPON码异常
				throw new Exception("获取COUPON码异常");
			}
		}
		return couponList;
	}
	/**
	 * 保存活动单据
	 * 
	 * @param subCamp
	 * @param memList
	 * @param prtList
	 * @throws Exception
	 */
	private void saveCampOrder(Map<String, Object> comMap, Map<String, Object> subCamp,
			List<Map<String, Object>> orderList, List<Map<String, Object>> prtList, List<String> couponList) {
		long start = System.currentTimeMillis();
		String tradeType = ConvertUtil.getString(subCamp.get("tradeType"));
		// 批量生成单据号
		List<String> orderNoList = cm03_bl.getTicketNumberList(comMap, tradeType, orderList.size());
		// 设置单据号及coupon码
		setOrder(orderList, orderNoList, comMap,subCamp, couponList);
		// 插入活动预约主单据
		comSer5.addCampOrder(orderList);
		List<Integer> campOrderIdList = ser4.getCampOrderIdList(orderNoList);
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		// 明细追加主单据ID
		for (Integer id : campOrderIdList) {
			for (Map<String, Object> prt : prtList) {
				Map<String, Object> detail = new HashMap<String, Object>(prt);
				detail.put("campOrderId", id);
				detail.put("subCampCode", subCamp.get("subCampCode"));
				detailList.add(detail);
			}
		}
		// 插入活动预约明细数据
		comSer5.addCampOrdDetail(detailList);
		// 备份单据
		comSer5.addCampHistory(orderList);
		orderNoList = null;
		detailList = null;
		campOrderIdList = null;
		logger.outLog("＊＊＊＊＊saveCampOrder执行时间［" + (System.currentTimeMillis() - start) + "］毫秒＊＊＊＊＊");
	}

	/**
	 * 处理提前生成coupon的活动单据
	 * 
	 * @param subCamp
	 * @param prtList
	 * @throws Exception
	 */
	public void tran_makeCouponOrder(Map<String, Object> comMap, Map<String, Object> subCamp,
			List<Map<String, Object>> hisList, List<Map<String, Object>> prtList) throws Exception {
		// 清除提前生成的coupon信息
		cpnIF.delCouponHisList(hisList);
		saveCampOrder(comMap, subCamp, hisList, prtList, null);

	}
	
	/**
	 * 单据状态修改为过期
	 * 
	 * @param billList
	 * @throws Exception
	 */
	public void tran_updCampOrderNG(List<Map<String, Object>> billList) throws Exception{
		long start1 = System.currentTimeMillis();
		// 更新活动单据状态
		ser4.updCampOrderNG(billList);
		long start2 = System.currentTimeMillis();
		logger.outLog("*updCampOrderNG［"+ (start2-start1) + "］毫秒*");
		// 更新活动履历表
		ser4.updCampOrderHisNG(billList);
		logger.outLog("*updCampOrderHisNG［"+ (System.currentTimeMillis()-start2) + "］毫秒*");
	}

	/**
	 * 设置单据号及coupon码
	 * 
	 * @param memList
	 * @param orderNoList
	 * @param subCamp
	 * @throws Exception
	 */
	private void setOrder(List<Map<String, Object>> orderList, List<String> orderNoList, Map<String, Object> comMap,
			Map<String, Object> subCamp, List<String> couponList) {
		int i = 0;
		for (Map<String, Object> order : orderList) {
			order.put(CampConstants.BILL_NO, orderNoList.get(i));
			if (null != couponList) {
				order.put(CampConstants.COUPON_CODE, couponList.get(i));
			}
			order.putAll(subCamp);
			order.putAll(comMap);
			i++;
		}
	}
}