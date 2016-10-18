package com.cherry.bs.emp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.emp.form.BINOLBSEMP08_Form;
import com.cherry.bs.emp.interfaces.BINOLBSEMP08_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 一个生成优惠券批次明细查询Action
 * @author menghao
 *
 */
public class BINOLBSEMP08_Action extends BaseAction implements
		ModelDriven<BINOLBSEMP08_Form> {

	private static final long serialVersionUID = -8910183036311258157L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSEMP08_Action.class);

	private BINOLBSEMP08_Form form = new BINOLBSEMP08_Form();
	
	@Resource(name="binOLBSEMP08_BL")
	private BINOLBSEMP08_IF binOLBSEMP08_BL;
	
	private List<Map<String, Object>> baCouponList;
	
	/**
	 * 页面初始化
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSearchMap();
			int count = binOLBSEMP08_BL.getBaCouponCount(map);
			if(count > 0) {
				baCouponList = binOLBSEMP08_BL.getBaCouponList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 对不起，查询发生异常，请重试。
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
	}
	
	/**
	 * 删除代理商优惠券
	 * @return
	 * @throws Exception
	 */
	public String deleteBaCoupon() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		//组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP07");
		map.put("baCouponId", form.getBaCouponId());
		try {
			binOLBSEMP08_BL.tran_deleteBaCoupon(map);
			this.addActionMessage(getText("ICM00002"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 失败场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
			} else {
				throw e;
			}
		}
		
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 查询共通参数
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 批次号
		map.put("batchCode", form.getBatchCode());
		// 一级代理商CODE
		map.put("parentResellerCode", form.getParentResellerCode());
		// 二级代理商CODE
		map.put("resellerCode", form.getResellerCode());
        // 优惠券
		map.put("couponCode", form.getCouponCode());
		// 同步状态
		map.put("synchFlag", form.getSynchFlag());
		
		return map;
	}
	@Override
	public BINOLBSEMP08_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBaCouponList() {
		return baCouponList;
	}

	public void setBaCouponList(List<Map<String, Object>> baCouponList) {
		this.baCouponList = baCouponList;
	}

}
