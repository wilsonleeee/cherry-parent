package com.cherry.pt.rps.action;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pt.rps.form.BINOLPTRPS33_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 电商拦截订单一览Action
 * 
 * @author chenkuan
 * @version 1.0 2015.12.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS40_Action extends BaseAction implements ModelDriven<BINOLPTRPS33_Form> {

    private static final long serialVersionUID = 5582161952782895825L;
    
  
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;

    
	/** 参数FORM */
	private BINOLPTRPS33_Form form = new BINOLPTRPS33_Form();

    @Override
    public BINOLPTRPS33_Form getModel() {
        return form;
    }

    public String init() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 业务类型
        map.put(CherryConstants.BUSINESS_TYPE, "3");
        // 操作类型
        map.put(CherryConstants.OPERATION_TYPE, "1");
        String date = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
        // 开始日期
        form.setStartDate(date);
        // 截止日期
        form.setEndDate(date);
        //是否是异常单 0表示异常
        form.setExceptionList("0");
        // 查询假日
        form.setHolidays(binOLCM00_BL.getHolidays(map));
        
        return SUCCESS;
    }

}