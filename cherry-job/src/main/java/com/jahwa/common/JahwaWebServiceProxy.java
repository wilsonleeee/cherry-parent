package com.jahwa.common;

import java.rmi.RemoteException;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_req;
import com.jahwa.pos.crm.Dt_PmsCpCampTran_res;
import com.jahwa.pos.crm.Dt_PmsCpGenTran_req;
import com.jahwa.pos.crm.Dt_PmsCpGenTran_res;
import com.jahwa.pos.crm.Dt_PmsCpListTran_req;
import com.jahwa.pos.crm.Dt_PmsCpListTran_res;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_req;
import com.jahwa.pos.crm.Dt_PmsCpStaTran_res;
import com.jahwa.pos.crm.Si_PmsCpCampTran_obProxy;
import com.jahwa.pos.crm.Si_PmsCpGenTran_obProxy;
import com.jahwa.pos.crm.Si_PmsCpListTran_obProxy;
import com.jahwa.pos.crm.Si_PmsCpStaTran_obProxy;
import com.jahwa.pos.ecc.Dt_GetMd_req;
import com.jahwa.pos.ecc.Dt_GetMd_res;
import com.jahwa.pos.ecc.Dt_Material_req;
import com.jahwa.pos.ecc.Dt_Material_res;
import com.jahwa.pos.ecc.Dt_Material_resRowItems;
import com.jahwa.pos.ecc.Dt_Price_req;
import com.jahwa.pos.ecc.Dt_Price_res;
import com.jahwa.pos.ecc.Si_GetMd_obProxy;
import com.jahwa.pos.ecc.Si_Material_obProxy;
import com.jahwa.pos.ecc.Si_Price_obProxy;
import com.jahwa.pos.ecc.TVKO;
import com.jahwa.pos.ecc.ZSAL_MD;
import com.jahwa.pos.ecc.ZSAL_PRICE;

public class JahwaWebServiceProxy {

	private static CherryBatchLogger logger = new CherryBatchLogger(
			JahwaWebServiceProxy.class);
	/** 产品webService代理 **/
	public static Si_Material_obProxy proxy1 = new Si_Material_obProxy();
	
	/** 产品价格webService代理 **/
	public static Si_Price_obProxy proxy4 = new Si_Price_obProxy();
	
	/** 门店webService代理 **/
	public static Si_GetMd_obProxy proxy2 = new Si_GetMd_obProxy();
	
	/** 促销webService代理 **/
	public static Si_PmsCpCampTran_obProxy proxy3 = new Si_PmsCpCampTran_obProxy();
	
	/**优惠券生成接受webService代理 **/
	public static Si_PmsCpGenTran_obProxy proxy5 = new Si_PmsCpGenTran_obProxy();
	
	/**优惠券状态调整webService代理 **/
	public static Si_PmsCpStaTran_obProxy proxy8 = new Si_PmsCpStaTran_obProxy();
	
	/**优惠券发放清单 webService代理 **/
	public static Si_PmsCpListTran_obProxy proxy9 = new Si_PmsCpListTran_obProxy();
	
	public static ZSAL_MD[] getDepartList(){
		ZSAL_MD[] arr = null;
		Dt_GetMd_req req = new Dt_GetMd_req();
		// 组织代码参数
		TVKO to = new TVKO(Config.VKORG);
		TVKO to1 = new TVKO(Config.VKORGC024);
		TVKO[] tos = new TVKO[2];
		tos[0] = to;
		tos[1] = to1;
		req.setZTVKO(tos);
		Dt_GetMd_res res = null;
		// 访问超时等异常时，重试次数
		int i = Config.RECONNECTION_TIMES;
		while(i > 0){
			try {
				res = proxy2.si_GetMd_ob(req);
				i = 0;
			} catch (RemoteException e) {
				i--;
				// 错误
				logger.outLog("门店webService访问异常：【" + e.getMessage() + "】", CherryBatchConstants.LOGGER_ERROR);
			}
		}
		if(null != res){
			arr = res.getINFO_MD();
		}
		return arr;
	}
	
	/**
	 * 产品价格
	 * @param KSCHL
	 *	 ZPFJ	批发价
	 *	 ZLSJ	零售价
	 *	 ZJZJ	价值价
	 *	 ZCBJ	成本价
	 * 	  为空时，全部
	 * @return
	 */
	public static ZSAL_PRICE[] getProductPriceList(String KSCHL){
		ZSAL_PRICE[] arr = null;
		Dt_Price_req req = new Dt_Price_req();
		// 组织代码参数
		req.setZVKORG(Config.VKORG);
		req.setZKSCHL(KSCHL);
		Dt_Price_res res = null;
		// 访问超时等异常时，重试次数
		int i = Config.RECONNECTION_TIMES;
		while(i > 0){
			try {
				res = proxy4.si_Price_ob(req);
				i = 0;
			} catch (RemoteException e) {
				i--;
				// 错误
				logger.outLog("产品价格webService访问异常：【" + e.getMessage() + "】", CherryBatchConstants.LOGGER_ERROR);
			}
		}
		if(null != res){
			arr = res.getINFO_PRICE();
		}
		return arr;
	}
	
	/**
	 * 产品
	 * @return
	 */
	public static Dt_Material_resRowItems[] getProductList(){
		Dt_Material_resRowItems[] arr = null;
		Dt_Material_req req = new Dt_Material_req();
		// 组织代码参数
		req.setVKORG(Config.VKORG);
		Dt_Material_res res = null;
		// 访问超时等异常时，重试次数
		int i = Config.RECONNECTION_TIMES;
		while(i > 0){
			try {
				res = proxy1.si_Material_ob(req);
				i = 0;
			} catch (RemoteException e) {
				i--;
				// 错误
				logger.outLog("产品webService访问异常：【" + e.getMessage() + "】", CherryBatchConstants.LOGGER_ERROR);
			}
		}
		if(null != res){
			arr = res.getRow();
		}
		return arr;
	}
	
	/**
	 * 家化活动获取
	 * @param tranFlag：传输标记【1：增量  2：指定活动重复传输】
	 * @param ruleCode活动代码【传输方式为2时为必填项目】
	 * @return
	 */
	public static Dt_PmsCpCampTran_res getCampList(String activityCode){
		Dt_PmsCpCampTran_req req = new Dt_PmsCpCampTran_req();
		// 组织代码参数
		req.setMKT_ORG(Config.VKORG);
		req.setSOURCE(Config.DATASOURCE);
		if(null == activityCode || "".equals(activityCode)){
			req.setZTRAN_FLAG(Config.ZTRAN_FLAG);
		}else{
			req.setEXTERNAL_ID(activityCode);
			req.setZTRAN_FLAG("2");
		}
		Dt_PmsCpCampTran_res res = null;
		// 访问超时等异常时，重试次数
		int i = Config.RECONNECTION_TIMES;
		while(i > 0){
			try {
				res = proxy3.si_PmsCpCampTran_ob(req);
				i = 0;
			} catch (RemoteException e) {
				i--;
				// 错误
				logger.outLog("产品webService访问异常：【" + e.getMessage() + "】", CherryBatchConstants.LOGGER_ERROR);
			}
		}
		return res;
	}
	
	/**
	 * CBI039-CRM-优惠券发放清单传输接口
	 * @param Dt_PmsCpListTran_req：请求参数
	 * @return Dt_PmsCpListTran_res 返回结果
	 */
	public static Dt_PmsCpListTran_res sendCouponList(Dt_PmsCpListTran_req req) throws Exception{
		Dt_PmsCpListTran_res res = proxy9.si_PmsCpListTran_ob(req);
		return res;
	}
	
	/**
	 * CBI037-CRM-优惠券生成接收接口
	 * @param Dt_PmsCpGenTran_req：请求参数
	 * @return Dt_PmsCpGenTran_res 返回结果
	 */
	public static Dt_PmsCpGenTran_res sendGenerate(Dt_PmsCpGenTran_req req) throws Exception{
		Dt_PmsCpGenTran_res res = proxy5.si_PmsCpGenTran_ob(req);
		return res;
	}
	
	/**
	 * CBI038-CRM-优惠券状态调整接收接口
	 * @param Dt_PmsCpStaTran_req：请求参数
	 * @return Dt_PmsCpStaTran_res 返回结果
	 */
	public static Dt_PmsCpStaTran_res sendUpdateCouponList( Dt_PmsCpStaTran_req req) throws Exception{
		Dt_PmsCpStaTran_res res = proxy8.si_PmsCpStaTran_ob(req);
		return res;
	}
}
