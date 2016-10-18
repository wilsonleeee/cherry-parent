package com.cherry.bs.emp.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.cherry.bs.emp.interfaces.BINOLBSEMP07_IF;
import com.cherry.bs.emp.service.BINOLBSEMP07_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;

/**
 * 生成代理商优惠券BL
 * @author menghao
 * @version 1.0 2014-08-27
 */
public class BINOLBSEMP07_BL implements BINOLBSEMP07_IF {
	
	@Resource(name="binOLBSEMP07_Service")
	private BINOLBSEMP07_Service binOLBSEMP07_Service;

	@Override
	public int getBaModelCouponCount(Map<String, Object> map) throws Exception {
		return binOLBSEMP07_Service.getBaModelCouponCount(map);
	}

	@Override
	public List<Map<String, Object>> getBaModelCouponList(
			Map<String, Object> map) throws Exception {
		return binOLBSEMP07_Service.getBaModelCouponList(map);
	}

	@Override
	public int getBatchCount(Map<String, Object> map) throws Exception {
		return binOLBSEMP07_Service.getBatchCount(map);
	}

	@Override
	public List<Map<String, Object>> getBatchList(Map<String, Object> map)
			throws Exception {
		return binOLBSEMP07_Service.getBatchList(map);
	}
	
	@Override
	public Map<String, Object> getBatchCodeByCode(Map<String, Object> map)
			throws Exception {
		return binOLBSEMP07_Service.getBatchCodeByCode(map);
	}

	@Override
	public void tran_createBaCoupon(Map<String, Object> map) throws Exception {
		// 批次日期
		String sysDate = binOLBSEMP07_Service.getDateYMD();
		map.put("batchDate", sysDate);
		// 当前批次每个代理商生成的优惠券码数量
		int batchCouponCount = ConvertUtil.getInt(map.get("batchCouponCount"));
		// 此次生成的优惠券码LIST
		List<Map<String, Object>> baCouponList = new ArrayList<Map<String, Object>>();
		// 随机码数值
		Long num=0L;
		// 随机码
		String randomCode="000000000000";
		
		// 选择的需要生成优惠券的代理商LIST,selectMode为1时只取得选中的代理商的相应ID及code对应的BA的ID
		List<Map<String, Object>> selectedResellerList = binOLBSEMP07_Service.getSelectedResellerInfo(map);
		
		if(null == selectedResellerList || selectedResellerList.size() == 0) {
			// 未选择任何代理商，无法生成优惠券！
			throw new CherryException("EBS00130");
		}
		// 已经存在的优惠券码中的随机码(全局唯一)
		List<Long> baExistRandom = binOLBSEMP07_Service.getExistRandom(map);
		// 循环BA插入每个BA新生成的优惠券
		for(Map<String, Object> selectedReseller : selectedResellerList) {
			// 根据当前BA生成其批次优惠券【不包含验证券】
			selectedReseller.putAll(map);
			// 生成指定数量的优惠券
			for (int i = 0; i < batchCouponCount; i++) {
				// 随机码为0~1000000【不包含1000000】
				num = this.getRandomExcept(1000000000000L, baExistRandom);
				// 新生成的随机码归为已经存在的随机码序列中【避免生成重复的随机码】
				baExistRandom.add(num);

				// 数值补充为12位，不足位数补0
				randomCode = "000000000000" + num;
				// 取右边12位数
				randomCode = randomCode.substring(randomCode.length() - 12,
						randomCode.length());
				// 编码规则为"618"+12位随机码
				randomCode = "618"+randomCode;
				// 用于存放生成的优惠券码
				Map<String, Object> baCouponMapTemp = new HashMap<String, Object>();
				baCouponMapTemp.putAll(selectedReseller);
				// 将生成的优惠券码加入
				baCouponMapTemp.put("couponCode",randomCode);
				baCouponList.add(baCouponMapTemp);
			}
			
			// 将加入验证码后的优惠券码写入
			binOLBSEMP07_Service.insertBaCouponList(baCouponList);
			// 清空中间参数内容
			baCouponList.clear();
		}
	}
	
	/**
	 * 为优惠券码加入验证码
	 * @param couponCode
	 * @return
	 */
	private String addValidCodeForCoupon(String couponCode){
	    int result=0;
	    // 验证码规则为取各位数值之和后奇数取1，偶数取2
	    for(int i=0;couponCode.length()>i;i++){
	        result += new Integer(couponCode.substring(i,i+1)).intValue();
	    }
	    result = result%2 == 0 ? 2 : 1;
	    
	    return couponCode+result;
	} 
	
	/**
	 * 取除指定数内的随机数
	 * @param RandMax
	 * @param ExceptNums
	 * @return
	 */
	private Long getRandomExcept(Long RandMax,List<Long> ExceptNums){
		
        while(true){
        	Long num=(Long)Math.round(Math.random()*RandMax);
            if(ExceptNums.contains(num)){
                continue;
            } else{
                return num;
            }
        }
    }
	
	@Override
	public int getMaxBatchCount(Map<String, Object> map) throws Exception {
		return binOLBSEMP07_Service.getMaxBatchCount(map);
	}

	@Override
	public void tran_deleteBatchCoupon(Map<String, Object> map) throws Exception {
		// 查询要删除的代理商优惠券的同步状态【存在已同步的不能删除】
		int synchronizedCount = binOLBSEMP07_Service.getSynchronizedCount(map);
		if(synchronizedCount > 0) {
			// 存在已同步的优惠券，不能删除！
			throw new CherryException("EBS00131");
		}
		// 删除
		binOLBSEMP07_Service.deleteBatchCoupon(map);
	}

}
