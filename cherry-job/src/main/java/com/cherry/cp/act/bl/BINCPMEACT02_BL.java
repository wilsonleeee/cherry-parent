package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINCPMEACT02_Service;
import com.cherry.cp.common.CampConstants;

/**
 * 预约单据下发BL
 * 
 * @author lipc
 * 
 */
public class BINCPMEACT02_BL {
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINCPMEACT02_BL.class);
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14_bl;
	
	@Resource
	private BINCPMEACT02_Service bincpmeact02_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 取得共通Map */
	private Map<String, Object> commMap = null;
	
	private String configFlag = null;

	private int totalCount = 0;

	/**
	 * 下发预约单据BL入口
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_publicOrder(Map<String, Object> map)
			throws CherryBatchException {
		// 设置共通Map
		setCommMap(map);
		// 数据下发批次
		int currentNum = 0;
		// 从接口数据库中分批取得产品列表，并处理
		while (true) {
			// 查询开始位置
			int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
			// 查询结束位置
			int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
			map.put(CherryBatchConstants.START, startNum);
			map.put(CherryBatchConstants.END, endNum);
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "orderId");
			// 查询预约单据
			List<Map<String, Object>> orderList = bincpmeact02_Service
					.getOrderList(map);
			if (CherryBatchUtil.isBlankList(orderList)) {
				break;
			}
			try {
				// 下发预约单据
				publicOrder(orderList);
				// 事务提交
				bincpmeact02_Service.manualCommit();
				bincpmeact02_Service.ifManualCommit();
			} catch (Exception e) {
				// 取数据下发批次
				currentNum += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
				logger.outLog(e.getMessage(), CherryBatchConstants.LOGGER_ERROR);
				// 事务回滚
				bincpmeact02_Service.manualRollback();
				bincpmeact02_Service.ifManualRollback();
			}
			
			// 预约单为空或预约单少于一页，跳出循环
			if (orderList.size() < CherryBatchConstants.DATE_SIZE) {
				break;
			}
		}
		logger.outLog("====操作预约单据总数：" + totalCount + " 条====");
		return flag;
	}

	/**
	 * 下发预约单据
	 * 
	 * @param map
	 */
	private void publicOrder(List<Map<String, Object>> orderList)
			throws Exception {
		String sysTime =  bincpmeact02_Service.getSYSDate();
		commMap.put("creatTime", sysTime);
		commMap.put("modifyTime", sysTime);
		for(Map<String, Object> order : orderList){
			String coupon = ConvertUtil.getString(order.get(CampConstants.COUPON_CODE));
			if("".equals(coupon) && "1".equals(configFlag)){
				order.put(CampConstants.COUPON_CODE, order.get("mobilePhone"));
			}
			order.putAll(commMap);
		}
		// 取得预约主单据List
		List<Map<String, Object>> orderMainList = CherryBatchUtil
				.getNotRepeatList(orderList, "orderNo");
		int size = orderMainList.size();
		logger.outLog("====正在处理第 " + (totalCount+1) + " ~ " + (totalCount + size) + " 条预约单据中====");
		// 统计总条数
		totalCount += size;
		// 取得需要再次下发预约主单据号List
		List<Map<String, Object>> orderMainList2 = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> order : orderMainList){
			// 下发区分
			String sendFlag = CherryBatchUtil.getString(order.get("sendFlag"));
			if ("2".equals(sendFlag)) {// 已下发需再次下发
				Map<String, Object> map = new HashMap<String, Object>(order);
				orderMainList2.add(map);
			}
		}
		// 删除需再次下发的预约单据主表明细表信息
		if(orderMainList2.size() > 0){
			// 删除需再次下发的预约单据【主表】息
			bincpmeact02_Service.delCouponMain(orderMainList2);
			// 删除需再次下发的预约单据【明细表】信息
			bincpmeact02_Service.delCouponDetail(orderMainList2);
		}
		// 下发所有预约信息【未下发，再次下发】
		// 主单据
		bincpmeact02_Service.addCouponMain(orderMainList);
		// 明细单据
		bincpmeact02_Service.addCouponDetail(orderList);
		// 更新新后台预约单据下发状态
		bincpmeact02_Service.updCampOrder(orderMainList);
		
	}

	/**
	 * 设置共通Map
	 * 
	 * @param map
	 */
	private void setCommMap(Map<String, Object> map) {
		commMap = new HashMap<String, Object>(map);
		commMap.put(CherryBatchConstants.CREATEDBY, "BATCH");
		commMap.put(CherryBatchConstants.UPDATEDBY, "BATCH");
		commMap.put(CherryBatchConstants.CREATEPGM, "BINCPMEACT02");
		commMap.put(CherryBatchConstants.UPDATEPGM, "BINCPMEACT02");
		// coupon下发内容
		configFlag = cm14_bl.getConfigValue("1138", ConvertUtil
						.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),
						ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
	}
}
