package com.cherry.wp.sal.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM44_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.common.entity.SaleDetailEntity;
import com.cherry.wp.common.entity.SaleMainEntity;
import com.cherry.wp.common.entity.SaleProductDetailEntity;
import com.cherry.wp.common.entity.SaleRuleResultEntity;
import com.cherry.wp.sal.form.BINOLWPSAL08_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL08_IF;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL08_Action extends BaseAction implements ModelDriven<BINOLWPSAL08_Form> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL08_Action.class);
	
	private BINOLWPSAL08_Form form = new BINOLWPSAL08_Form();
	@Resource(name="binOLWPSAL08_BL")
	private BINOLWPSAL08_IF binOLWPSAL08_IF;
	@Resource
	private BINOLCM44_BL binOLCM44_BL;
	
	private Map<String, Object> promotionMap=new HashMap<String, Object>();
	
	public String init(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			// 根据是否存在会员ID判断客户类型是否为会员
			String customerType = "";
			String memberInfoId = ConvertUtil.getString(form.getMemberInfoId());
			if(null != memberInfoId && !"".equals(memberInfoId)){
				customerType = "MB";
			}			
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 客户类型
			map.put("customerType", customerType);
			// 会员卡号
			map.put("memberInfoId", form.getMemberInfoId());
			// 会员手机号
			map.put("mobilePhone", form.getMobilePhone());
			// 可兑换积分值
			map.put("changablePoint", form.getChangablePoint());
			// 所属组织
			map.put("organizationId", userInfo.getBIN_OrganizationID());
			// 发卡柜台
			map.put("counterCodeBelong", form.getCounterCodeBelong());
			// 首次购买柜台
			map.put("firstSaleCounter", form.getFirstSaleCounter());
			// 活动预约柜台
			map.put("orderCounterCode", form.getOrderCounterCode());
			List<Map<String, Object>> promotionList = binOLWPSAL08_IF.getPromotionList(map);
			// 设置促销列表
			form.setPromotionList(promotionList);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	public void getPromotionProduct() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// 会员所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 语言
			map.put("language", userInfo.getLanguage());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 活动类型
			map.put("activityType", form.getActivityType());
			// 活动编号（MainCode）
			map.put("activityCode", form.getActivityCode());
			// 所属组织
			map.put("organizationId", userInfo.getBIN_OrganizationID());
			
			Map<String, Object> promotionMap = new HashMap<String, Object>();
			// 获取活动信息
			Map<String, Object> promotionInfo = binOLWPSAL08_IF.getActivityInfo(map);
			
			if(null != promotionInfo && !promotionInfo.isEmpty()){
				if(null!=promotionInfo.get("CODEMESSAGE") && "TIMEEXPIRED".equals(promotionInfo.get("CODEMESSAGE").toString())){
					ConvertUtil.setResponseByAjax(response, "TIMEEXPIRED");
				}else {
					// 获取活动礼品
					List<Map<String, Object>> promotionProduct = binOLWPSAL08_IF.getActivityProduct(map);
					// 加入活动主记录
					promotionMap.put("promotionInfo", promotionInfo);
					// 加入活动礼品明细
					promotionMap.put("promotionProduct", promotionProduct);
					// 返回活动记录信息
					ConvertUtil.setResponseByAjax(response, promotionMap);
				}
			}else{
  				ConvertUtil.setResponseByAjax(response, "ERROR");
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}

	/**
	 * 用来获取产品领用的活动信息活动明细
	 * @throws Exception 
	 */
	public void getLYHDActivityInfo() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// 会员所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 语言
			map.put("language", userInfo.getLanguage());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 活动编号（MainCode）
			map.put("MainCode", form.getMaincode());
			// 所属组织
			map.put("organizationId", userInfo.getBIN_OrganizationID());
			map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//			map.put("MemCode", form.getMemberCode());
			map.put("subjectCode", form.getSubjectCode());
			// 会员ID存在的情况下带上会员ID查询
			if(!"".equals(ConvertUtil.getString(form.getMemberInfoId()))){
				map.put("MemberInfoId", form.getMemberInfoId());
			}else{
				map.put("MemCode", form.getMemberCode());
			}
			if(!"".equals(ConvertUtil.getString(form.getMemberCode())) || !"".equals(ConvertUtil.getString(form.getMemberInfoId()))){
				Map<String, Object> promotionMap = new HashMap<String, Object>();
				// 获取活动信息
				Map<String, Object> promotionInfo = binOLWPSAL08_IF.getLYHDProductInfo(map);
				if(null != promotionInfo && !promotionInfo.isEmpty()){
					List<Map<String, Object>> promotionDetail = binOLWPSAL08_IF.getLYHDProductDetail(map);
					if(null != promotionDetail && !promotionDetail.isEmpty()){
						promotionMap.put("promotionInfo", promotionInfo);
						promotionMap.put("promotionDetail", promotionDetail);
						ConvertUtil.setResponseByAjax(response, promotionMap);
					}else{
						ConvertUtil.setResponseByAjax(response, "ERROR");
					}
				}else{
					ConvertUtil.setResponseByAjax(response, "ERROR");
				}
			}else{
				ConvertUtil.setResponseByAjax(response, "NOTMEMBER");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}
	
	/**
	 * 用来初始化产品领用活动界面(无需验证的情况)
	 */
	public String initLYHD_0() {
		return SUCCESS;
	}
	/**
	 * 用来初始话产品领用活动界面(本地验证的情况)
	 */
	public String initLYHD_1() {
		return SUCCESS;
	}
	/**
	 * 用来初始化产品领用活动界面（手机号验证的情况）
	 */
	public String initLYHD_3() {
		return SUCCESS;
	}
	/**
	 * 用来初始化产品领用活动界面（会员卡号验证的情况）
	 */
	public String initLYHD_4() {
		return SUCCESS;
	}
	/**
	 * 用来初始化促销活动页面的action
	 * @return
	 */
	public String initMatchRule_CloudPos(){
		// 用户信息\r
		 UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		 form.setBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	/**
	 * 用来初始化错误页面的action
	 * @return
	 */
	public String initRuleError(){
		return SUCCESS;
	}
	
	/**
	 * 用来比对本地校验正则表达式方法
	 * @throws Exception 
	 */
	public void checkRegular() throws Exception{
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
							.get(CherryConstants.SESSION_USERINFO);
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("subjectCode", form.getSubjectCode());
		params.put("memcode", form.getMemberCode());
		// 会员所属组织
		params.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		params.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		params.put("customerText", form.getCustomerText());
		int check_flag=binOLWPSAL08_IF.checkRegular(params);
		ConvertUtil.setResponseByAjax(response, check_flag);
	}
	
	/**
	 * 校验单据中的手机号与会员卡号
	 * @throws Exception 
	 */
	public void checkMobilephoneMemcode() throws Exception{
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("subjectCode", form.getSubjectCode());
		params.put("memCode", form.getMemberCode());
		params.put("mobile", form.getMobilePhone());
		int check_flag=binOLWPSAL08_IF.checkMobilephoneMemcode(params);
		ConvertUtil.setResponseByAjax(response, check_flag);
	}
	/**
	 * 用来获得智能促销列表的ACTION
	 * @throws Exception 
	 */
	public void getMatchRule_CloudPos() throws Exception{
		
		try {
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			ArrayList<SaleMainEntity> inputSalemain=binOLWPSAL08_IF.saleMain2Entity(form, counterInfo.getCounterCode());
			List<Map<String,Object>> saleDetailMap_list=CherryUtil.json2ArryList(form.getSaleDetailList());
			binOLWPSAL08_IF.delPromotionMainInfo(saleDetailMap_list);
			ArrayList<SaleDetailEntity> inputSaledetail=binOLWPSAL08_IF.saleDetail2Entity(saleDetailMap_list);
			ArrayList<SaleMainEntity> outsalemain  = new ArrayList<SaleMainEntity>();
			ArrayList<SaleDetailEntity> outdetail  = new ArrayList<SaleDetailEntity>();
			ArrayList<SaleRuleResultEntity> outresult= new ArrayList<SaleRuleResultEntity>();
			//这里调用接口
			int num_back=binOLCM44_BL.cloud_MatchRule(userInfo.getBrandCode(),Integer.valueOf(counterInfo.getOrganizationId()).toString(), inputSalemain, inputSaledetail,
					outsalemain, outdetail, outresult);
			this.smartLog(num_back, 1);
			String resultflag="1";
			//没有返回活动的情况，在页面上直接跳转收款
			if(outresult.size() == 0){
				resultflag="0";
			}
			//获得页面显示的数据类型
			Map<String,Object> jSPEntityMap=binOLWPSAL08_IF.convert2JSPEntity(outdetail);
			Map<String,Object> param_map=new HashMap<String,Object>();
			param_map.put("promotion_all_old", jSPEntityMap.get("promotion_all_list_old"));
//			param_map.put("promotionInfo_old", jSPEntityMap.get("promotionInfo_list_old"));
			param_map.put("promotion_all_new", jSPEntityMap.get("promotion_all_list_new"));
			param_map.put("promotionInfo_new", jSPEntityMap.get("promotionInfo_list_new"));
			param_map.put("inputSaledetail", inputSaledetail);//页面上保存原始的购物车数据
			param_map.put("outresult", outresult);
			param_map.put("resultflag", resultflag);
			param_map.put("num_back", Integer.valueOf(num_back).toString());
			ConvertUtil.setResponseByAjax(response, param_map);
			binOLCM44_BL.cloud_Destroy(outsalemain,outdetail, outresult);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}
	/**
	 * 计算规则时需要调用的action
	 * @throws Exception 
	 */
	public void getComputeRule_CloudPos() throws Exception{
		try {
				// 用户信息
				UserInfo userInfo = (UserInfo) session     
						.get(CherryConstants.SESSION_USERINFO);
				// 用户信息
				CounterInfo counterInfo = (CounterInfo) session
						.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
				ArrayList<SaleMainEntity> outsalemain  = new ArrayList<SaleMainEntity>();
				ArrayList<SaleDetailEntity> outdetail  = new ArrayList<SaleDetailEntity>();
				ArrayList<SaleRuleResultEntity> outresult= new ArrayList<SaleRuleResultEntity>();
				//获取查询方法返回的数据
				List<Map<String, Object>> outdetail_back=CherryUtil.json2ArryList(form.getInputdetail_back());
				List<Map<String, Object>> outresult_back=CherryUtil.json2ArryList(form.getOutresult_back());
				List<Map<String, Object>> outproduct_back=new ArrayList<Map<String,Object>>();
				if(null!=form.getOutproduct_back() && !"".equals(ConvertUtil.getString(form.getOutproduct_back()))){
					outproduct_back=CherryUtil.json2ArryList(form.getOutproduct_back());
				}
				//根据查询方法的返回值，组装数据
				ArrayList<SaleMainEntity> input_main=binOLWPSAL08_IF.saleMain2Entity(form, counterInfo.getCounterCode());
				ArrayList<SaleDetailEntity> input_detail=binOLWPSAL08_IF.getSaleDetailEntityList(outdetail_back);
				ArrayList<SaleRuleResultEntity> input_result=binOLWPSAL08_IF.getSaleRuleResultlEntityList(outresult_back);
				ArrayList<SaleProductDetailEntity> input_product=binOLWPSAL08_IF.getSaleProductDetialEntityList(outproduct_back);
				//需要在input_result中通过maincode来找到对应的信息，进行选中或者取消选中的标识
				for(SaleRuleResultEntity r:input_result){
					if(form.getMaincode().equals(r.getMaincode())){
						r.setActivitycode(form.getActivitycode_check());
						r.setCheckflag(form.getCheckflag());
						r.setLevel(1);
					}
				}
				//这里调用接口
				int num_back=binOLCM44_BL.cloud_ComputeRule(userInfo.getBrandCode(), input_main, input_detail, input_result, input_product, outsalemain, outdetail, outresult);
				this.smartLog(num_back, 2);
				//获得页面显示的数据类型
				Map<String,Object> jSPEntityMap=binOLWPSAL08_IF.convert2JSPEntity(outdetail);
				
				Map<String,Object> param_map=new HashMap<String,Object>();
				param_map.put("promotion_all_old", jSPEntityMap.get("promotion_all_list_old"));
				param_map.put("promotionInfo_old", jSPEntityMap.get("promotionInfo_list_old"));
				param_map.put("promotion_all_new", jSPEntityMap.get("promotion_all_list_new"));
				param_map.put("promotionInfo_new", jSPEntityMap.get("promotionInfo_list_new"));
				param_map.put("outresult", outresult);
				param_map.put("num_back", Integer.valueOf(num_back).toString());
				ConvertUtil.setResponseByAjax(response, param_map);
				binOLCM44_BL.cloud_Destroy(outsalemain,outdetail, outresult);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
		
	}
	/**
	 * 用来打印智能促销查询规则方法返回所对应的日志
	 */
	private void smartLog(int num,int type){
		//查询日志
		if(type == 1){
			if(num == 0){
				logger.error("智能促销查询方法调用异常");
			}else if(num == 3){
				logger.error("智能促销查询方法数据库连接异常");
			}else if(num == 4){
				logger.error("智能促销查询方法更新最新的规则,稍后重试");
			}else if(num == 5){
				logger.error("智能促销查询方法socket通讯失败");
			}
		}else if(type == 2){
			if(num == 0){
				logger.error("智能促销计算方法调用异常");
			}else if(num == 3){
				logger.error("智能促销计算方法数据库连接异常");
			}else if(num == 4){
				logger.error("智能促销计算方法更新最新的规则,稍后重试");
			}else if(num == 5){
				logger.error("智能促销计算方法socket通讯失败");
			}
		}
	}
	
	@Override
	public BINOLWPSAL08_Form getModel() {
		return form;
	}


	public Map<String, Object> getPromotionMap() {
		return promotionMap;
	}

	public void setPromotionMap(Map<String, Object> promotionMap) {
		this.promotionMap = promotionMap;
	}


	
	
}
