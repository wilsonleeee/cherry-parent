package com.cherry.wp.sal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.form.BINOLWPSAL10_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL10_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL10_Action extends BaseAction implements ModelDriven<BINOLWPSAL10_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285944958012057505L;
	
	private BINOLWPSAL10_Form form = new BINOLWPSAL10_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL10_Action.class);
	
	@Resource(name="binOLWPSAL10_BL")
	private BINOLWPSAL10_IF binOLWPSAL10_IF;
	
	public String init(){
		return SUCCESS;
	}
	
	public void checkCoupon() throws Exception{
		try{
			boolean checkFlag = true;
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			// 活动类型
			map.put("activityType", form.getActivityType());
			// 主活动编号
			map.put("subjectCode", form.getSubjectCode());
			// 活动编号（MainCode）
			map.put("activityCode", form.getActivityCode());
			// 判断是否有Coupon号，若有则根据Coupon号查询活动单据，若没有则根据手机号查询活动单据
			if(!"".equals(ConvertUtil.getString(form.getCouponCode()))){
				// Coupon号
				map.put("couponCode", form.getCouponCode());
				// 会员ID存在的情况下带上会员ID查询
				if(!"".equals(ConvertUtil.getString(form.getMemberInfoId()))){
					// 会员ID
					map.put("memberInfoId", form.getMemberInfoId());
				}else{
					if(!"".equals(ConvertUtil.getString(form.getMobilePhone()))){
						// 会员手机号
						map.put("mobilePhone", form.getMobilePhone());
					}
				}
			}else{
				if(!"".equals(ConvertUtil.getString(form.getMemberInfoId()))){
					// 会员ID
					map.put("memberInfoId", form.getMemberInfoId());
				}else{
					if(!"".equals(ConvertUtil.getString(form.getMobilePhone()))){
						// 会员手机号
						map.put("mobilePhone", form.getMobilePhone());
					}else{
						map.put("memberInfoId", -9999);
						checkFlag = false;
					}
				}
			}
			
			if(checkFlag){
				Map<String, Object> promotionMap = new HashMap<String, Object>();
				// 获取活动信息
				Map<String, Object> couponOrderInfo = binOLWPSAL10_IF.getCouponOrderInfo(map);
				
				if(null != couponOrderInfo && !couponOrderInfo.isEmpty()){
					// 获取活动礼品
					List<Map<String, Object>> couponOrderProduct = binOLWPSAL10_IF.getCouponOrderProduct(map);
					if(null != couponOrderProduct && !couponOrderProduct.isEmpty()){
						// 加入活动主记录
						promotionMap.put("couponOrderInfo", couponOrderInfo);
						// 加入活动礼品明细
						promotionMap.put("couponOrderProduct", couponOrderProduct);
						// 返回活动记录信息
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
	
	@Override
	public BINOLWPSAL10_Form getModel() {
		return form;
	}
}
