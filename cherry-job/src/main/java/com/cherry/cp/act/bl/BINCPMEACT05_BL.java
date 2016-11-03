package com.cherry.cp.act.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINCPMEACT05_Service;
import com.cherry.cp.common.CampConstants;

/**
 * batch会员等级变换生日礼替换
 * 
 * @author GeHequn
 * 
 */
public class BINCPMEACT05_BL {
	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINCPMEACT05_BL.class);
	@Resource(name = "bincpmeact05_Service")
	private BINCPMEACT05_Service ser5;
	
	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL cm33_bl;
	
	private int sumCount = 0;
	
	private int optCount = 0;
	// 设置batch处理标志
	private	boolean warningFlag = false;
	
	private	String CreatedBy = "BATCH";
	
	private	String UpdatedBy = "BATCH";
	
	private	String CreatePGM = "BINCPMEACT05";
	
	private	String UpdatePGM = "BINCPMEACT05";
	/**
	 * batch处理会员调整单据
	 * @return
	 */
	public int tran_handleOrder() throws CherryBatchException {
//		logger.outLog("**************等级变换会员生日礼替换Batch开始**************");
		//获取会员等级调整表中所有会员的ID
		List<Integer> changeMemIdList = ser5.getChangeMemIdList();
		if(null != changeMemIdList && !changeMemIdList.isEmpty()){
			sumCount = changeMemIdList.size();
			//获取操作的年份
			String sysDate = ser5.getSYSDate();
			String optYear = sysDate.substring(0,4);
			//处理每一个会员
			for(int memberId : changeMemIdList){
				//处理会员生日礼
				try {
					memLevelChange(memberId,optYear);
					ser5.manualCommit();
//					logger.outLog("**************会员："+memberId+" 处理完毕**************");
					optCount++;
				} catch (Exception e) {
					warningFlag = true;
					ser5.manualRollback();
					logger.outLog("",CherryBatchConstants.LOGGER_WARNING);
				}
			}
		}
		logger.outLog("**************需要调整的会员数："+sumCount+"**************");
		logger.outLog("**************已成功处理的会员数："+optCount+"**************");
		if(warningFlag){
			logger.outLog("**************等级变换会员生日礼替换Batch警告结束**************");
			return 1;
		} else{
			logger.outLog("**************等级变换会员生日礼替换Batch正常结束**************");
			return 0;
		}

		
		
	}
	
	/**
	 * 处理等级变化会员生日礼
	 * @param memberId
	 * @return
	 */
	private void memLevelChange(int memberId,String optYear) throws Exception{
		//取得每个会员已参加的会员生日礼单据
		List<Map<String,Object>> campOrderList = ser5.getMemAllCamp(memberId,optYear);
		if(null != campOrderList && !campOrderList.isEmpty()){
			for(Map<String,Object> order : campOrderList){
				//取得每个活动的查询参数
				String campCode = ConvertUtil.getString(order.get("campCode"));
				String mainCode = ConvertUtil.getString(order.get("mainCode"));
				int orderId = ConvertUtil.getInt(order.get("orderId"));
				//取得主活动的有效子活动list
				List<Map<String,Object>> subList = ser5.getSubCampList(campCode);
				//如果主题活动包含多级条件子活动，则判断是否要替换
				if(null != subList && subList.size()>1){
					//循环主题活动下的所有子活动
					for(Map<String,Object> subMap : subList){
						String subCampCode = ConvertUtil.getString(subMap.get("subCampCode"));
						if(!subCampCode.equals(mainCode)){
							//取得子活动的活动对象是否包含该会员
							boolean containFlag = isContainCouster(subMap,memberId);
							if(containFlag){
								// 根据子活动ID查询活动礼品List
								List<Map<String,Object>> prtList = ser5.getPrtList(subMap);
								if(null !=prtList && !prtList.isEmpty()){
									// 更换会员生日礼明细
									changeOrderInfo(orderId,prtList,subCampCode);
									break;
								}
							};
						}
					}
				}			
			}
		}
		// 删除等级变化的会员ID
		ser5.delMember(memberId);
		// 将会员等级变化需要调整单据的会员插入到历史表
		ser5.addHisMember(memberId);
	}
	
	/**
	 * 更换调整会员的生日礼
	 * @param orderId
	 * @param subMap
	 */
	private void changeOrderInfo(int orderId,List<Map<String,Object>> prtList,String subCampCode){
		// 删除订单明细
		ser5.delOrderInfo(orderId);
		// 插入新产品明细
		List<Map<String,Object>> insertOrderList = makeNewOrderDetail(orderId,prtList,subCampCode);
		ser5.insertOrder(insertOrderList);
		// 更新主单版本
		Map<String,Object> updateOrderMap = makeNewOrder(orderId,prtList);
		ser5.updateOrder(updateOrderMap);
	}
	
	/**
	 * 生成新的生日礼订单明细
	 * @param orderId
	 * @param prtList
	 * @param subCampCode
	 * @return
	 */
	private List<Map<String,Object>> makeNewOrderDetail(int orderId,List<Map<String,Object>> prtList,String subCampCode){
		List<Map<String,Object>> newOrderList = new ArrayList<Map<String,Object>>();

		for(Map<String,Object> item : prtList){
			item.put("orderId", orderId);
			item.put("mainCode", subCampCode);
			item.put("CreatedBy", CreatedBy);
			item.put("UpdatedBy", UpdatedBy);
			item.put("CreatePGM", CreatePGM);
			item.put("UpdatePGM", UpdatePGM);
			newOrderList.add(item);
		}
		return newOrderList;
	}
	
	/**
	 * 生成主订单的新替换项
	 * @param orderId
	 * @param prtList
	 * @param subCampCode
	 * @return
	 */
	private Map<String,Object> makeNewOrder(int orderId,List<Map<String,Object>> prtList){
		Map<String,Object> map = new HashMap<String,Object>();
		int quantity = prtList.size();
		float price = 0;
		for(Map<String,Object> item : prtList){
			float eachPrice = ConvertUtil.getFloat(item.get("price"));
			price += eachPrice;
		}
		map.put("price", price);
		map.put("quantity", quantity);
		map.put("orderId", orderId);
		return map;
	}
	
	/**
	 * 取得子活动的活动对象
	 * @param subMap
	 * @return
	 */
	private boolean isContainCouster(Map<String,Object> subMap,int memberId){
		//取得子活动条件类型
		String condType = ConvertUtil.getString(subMap.get("condType"));
		//取得子活动条件
		String memJson = ConvertUtil.getString(subMap.get("memJson"));
		//如果规则条件为【搜索条件】
		if(condType.equals(CampConstants.CAMP_MEB_TYPE_1)){
			try {
				Map<String, Object> conMap = CherryUtil.json2Map(memJson);
				conMap.put(CherryBatchConstants.START,1);
				conMap.put(CherryBatchConstants.END,1);
				// 排序字段
				conMap.put(CherryBatchConstants.SORT_ID,"memId");
				conMap.put("memberInfoId",memberId);
				//取得会员搜索结果
				int count = searchMemList(conMap);
				if(count > 0){
					return true;
				} else{
					return false;
				}
			} catch (Exception e) {
				logger.outLog(e.getMessage(),CherryBatchConstants.LOGGER_ERROR);
			}
		}//如果规则条件为【全部会员】【不限】
		else if(condType.equals(CampConstants.CAMP_MEB_TYPE_0) || 
				condType.equals(CampConstants.CAMP_MEB_TYPE_5)){
			return true;
		}
		return false;
	}
	
	/**
	 * 取得会员List
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private int searchMemList(Map<String, Object> map)
			throws Exception {
		List<String> selectors = new ArrayList<String>();
		// 会员Id
		selectors.add("memId");
		map.put("selectors", selectors);
		map.put("resultMode", "0");
		Map<String, Object> resultMap = cm33_bl.searchMemList(map);
		if (resultMap != null) {
			return ConvertUtil.getInt(resultMap.get("total"));
		}else{
			return 0;
		}
	}
}
