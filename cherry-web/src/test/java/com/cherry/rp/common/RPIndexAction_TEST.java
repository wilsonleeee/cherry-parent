package com.cherry.rp.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

public class RPIndexAction_TEST extends CherryJunitBase {
	private RPIndexAction action;
	@Resource
	private TESTCOM_Service testService;
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	@Transactional 
	public void testopenBIWindow()throws Exception {
		List userlist = (List) DataUtil.getDataList(this.getClass(), "testopenBIWindow");
		action = createAction(RPIndexAction.class,"/rp" , "openBIWindow");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),"testopenBIWindow");
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		//往数据库新曾用户信息
		for(int i=0;i<userlist.size();i++){
			Map map= (Map)userlist.get(i);
			map.putAll(map);
			testService.insertTableData(map);
			String LonginName =(String)map.get("LonginName");
			userInfo.setLoginName(LonginName);
		}
		Assert.assertEquals("success", proxy.execute());
		//取得BI用户名和密码
		String userlogin = action.getBiUserName();
		String password = action.getBiPassWord();
		// 加密处理
		DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
		String psd =  des.encrypt(password);
		//检验BI登陆用户信息		
		Assert.assertEquals("用户名称不正确","BI账户前缀TestBI001",userlogin);
		Assert.assertEquals("密码称不正确","93cccaad26995ffba2aed522e0b9f7ce",psd);
	}
	
	
}
