package com.cherry.wp.sal.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.wp.common.entity.SaleDetailEntity;
import com.cherry.wp.common.entity.SaleMainEntity;
import com.cherry.wp.common.entity.SaleProductDetailEntity;
import com.cherry.wp.common.entity.SaleRuleResultEntity;
import com.cherry.wp.sal.form.BINOLWPSAL08_Form;


public interface BINOLWPSAL08_IF {

	public List<Map<String, Object>> getPromotionList (Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getActivityProduct (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getActivityInfo (Map<String, Object> map) throws Exception;
	
	public String getActivityType (Map<String, Object> map);

	/**
	 * 根据jni返回的Bacode和unicode来查询出智能促销接口拿不到的属性
	 */
	public Map<String,Object> getPromotionProductinfo(Map<String,Object> paramMap);
	
	/**
	 * 获取前端传递过来的Detail json字符串，组装数据
	 * @param outmain_back
	 * @return
	 */
	public ArrayList<SaleDetailEntity> getSaleDetailEntityList(List<Map<String,Object>> outdetail_back);
	
	/**
	 * 获取前端传递过来的Result json字符串，组装数据
	 * @param outresult_back
	 * @return
	 */
	public ArrayList<SaleRuleResultEntity> getSaleRuleResultlEntityList(List<Map<String,Object>> outresult_back);
	
	/**
	 * 获取前端传递过来的product json字符串，组装数据
	 * @param outresult_back
	 * @return
	 */
	public ArrayList<SaleProductDetailEntity> getSaleProductDetialEntityList(List<Map<String,Object>> outproduct_back);
	
	/**
	 * 那传递过来的saledetail转换为SaleDetailEntity集合，封装数据
	 */
	public ArrayList<SaleDetailEntity> saleDetail2Entity(List<Map<String, Object>> saleDetailMap_list);
	
	/**
	 * 处理接口返回的数据，封装成页面显示需要的数据类型
	 */
	public Map<String,Object> convert2JSPEntity(ArrayList<SaleDetailEntity> outdetail);
	
	/**
	 * 用于用户输入活动信息时去除活动主单信息，保留活动明细信息
	 */
	public List<Map<String,Object>> delPromotionMainInfo(List<Map<String,Object>> saleDetailMap_list);
	
	public ArrayList<SaleMainEntity> saleMain2Entity(BINOLWPSAL08_Form form,String counterCode);
	
	/**
	 * 获取领用类型的productInfo
	 */
	public Map<String,Object> getLYHDProductInfo(Map<String,Object> params);
	/**
	 * 获取领用类型的productDeail
	 */
	public List<Map<String, Object>> getLYHDProductDetail(Map<String, Object> params);
	/**
	 * 获取单据中的正则表达式，进行比对
	 */
	public Integer checkRegular(Map<String,Object> params);
	/**
	 * 校验单据中的手机号与会员卡号
	 */
	public Integer checkMobilephoneMemcode(Map<String,Object> params);
}
