package com.cherry.wp.sal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.form.BINOLWPSAL06_Form;
import com.cherry.wp.sal.interfaces.BINOLWPSAL06_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWPSAL06_Action extends BaseAction implements ModelDriven<BINOLWPSAL06_Form> {
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLWPSAL06_Form form = new BINOLWPSAL06_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPSAL06_Action.class);
	
	@Resource(name="binOLWPSAL06_BL")
	private BINOLWPSAL06_IF binOLWPSAL06_IF;
	
	@Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	public String init(){
		return SUCCESS;
	}
	
	public String search(){
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 登陆用户
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			//订单号
			map.put("hangBillCode", form.getHangBillCode());
			//单据交易开始时间
			map.put("billHangDateStart", form.getBillHangDateStart());
			//单据交易截止时间
			map.put("billHangDateEnd", form.getBillHangDateEnd());
			//是否只显示重试单据信息
			map.put("retryDataFlag", form.getRetryDataFlag());
			//取得模板数量
			int count = binOLWPSAL06_IF.getBillsCount(map);
			if(count > 0){
				List<Map<String, Object>> billList = binOLWPSAL06_IF.getBillList(map);
				// 取得List
				form.setBillList(billList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
	
	public void getBill() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			
			// 用户信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			//语言
	        String language = userInfo.getLanguage();
	        
	        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
	        String isSocket=binOLCM14_BL.getWebposConfigValue("9015", organizationInfoId, brandInfoId);
			// 挂单ID
			map.put("hangBillId", form.getHangBillId());
			
			// 提单
			Map<String, Object> resultMap=null;
			if("N".equals(isSocket)){
				//实体仓库ID
				String organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
				List<Map<String,Object>> departList = binOLCM18_BL.getDepotsByDepartID(organizationId, language);
				String entitySocketId=null;
				if(null == departList || departList.size()==0){
					entitySocketId="";
				}else{
					entitySocketId=ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID"));
				}
				resultMap = binOLWPSAL06_IF.tran_getBillDetailAddSocket(map, entitySocketId);
			}else{
				resultMap = binOLWPSAL06_IF.tran_getBillDetail(map);
			}
			if(null != resultMap && !resultMap.isEmpty()){
				ConvertUtil.setResponseByAjax(response, resultMap);
			}else{
				ConvertUtil.setResponseByAjax(response, "ERROR");
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
	
	@Override
	public BINOLWPSAL06_Form getModel() {
		return form;
	}

}
