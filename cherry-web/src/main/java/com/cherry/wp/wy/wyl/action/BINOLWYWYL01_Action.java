package com.cherry.wp.wy.wyl.action;

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
import com.cherry.webservice.activity.interfaces.Activity_IF;
import com.cherry.wp.wy.wyl.form.BINOLWYWYL01_Form;
import com.cherry.wp.wy.wyl.interfaces.BINOLWYWYL01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWYWYL01_Action extends BaseAction implements ModelDriven<BINOLWYWYL01_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5026630071228773847L;

	private BINOLWYWYL01_Form form = new BINOLWYWYL01_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWYWYL01_Action.class);
	
	@Resource(name="binOLWYWYL01_BL")
	private BINOLWYWYL01_IF binOLWYWYL01_IF;
	
	@Resource
	private Activity_IF activity_IF;
	
	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
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
			
			
			Map<String, Object> billsMap = activity_IF.getOrderInfo(map);
			if (null != billsMap && !billsMap.isEmpty()){
				List<Map<String, Object>> billList = (List<Map<String, Object>>)billsMap.get("ResultContent ");
				if (null != billList && !billList.isEmpty()){
					billList.size();
				}
			}
			
			
//			//取得数量
//			int count = binOLWYWYL01_IF.getReservationBillsCount(map);
//			if(count > 0){
//				List<Map<String, Object>> billList = binOLWYWYL01_IF.getReservationBillsList(map);
//				// 取得List
//				form.setBillList(billList);
//			}
//			// form表单设置
//			form.setITotalDisplayRecords(count);
//			form.setITotalRecords(count);
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
	
	public void getSubCampaignList() throws Exception{
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//登录用户组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//登录语言
		paramMap.put("language", userInfo.getLanguage());
		//登录用户所属品牌
		paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		paramMap.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		if("-9999".equals(userInfo.getBIN_BrandInfoID())){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("subCampInfoStr", form.getSubCampInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("state", form.getState());
		String resultStr = binOLWYWYL01_IF.getSubCampaignList(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	@Override
	public BINOLWYWYL01_Form getModel() {
		return form;
	}
}
