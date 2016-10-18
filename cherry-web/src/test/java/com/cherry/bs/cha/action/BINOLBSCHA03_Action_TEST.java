package com.cherry.bs.cha.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.CherryJunitBase;
import com.cherry.bs.cha.form.BINOLBSCHA03_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;

public class BINOLBSCHA03_Action_TEST extends CherryJunitBase {
	private BINOLBSCHA03_Action initAction1;
	private BINOLBSCHA03_Form form;

	@Resource
	private TESTCOM_Service testService;

	private void setUpInit1() throws Exception {
		initAction1 = createAction(BINOLBSCHA03_Action.class, "/basis",
				"BINOLBSCHA03_init");
		List<Map<String, Object>> testList = DataUtil.getDataList(
				this.getClass(), "testInit1");
		Map<String, Object> channelMap = testList.get(0);
		//插入一条渠道信息
		int channelId = testService.insertTableData(channelMap);
		
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),
				"testInit1");
		form = initAction1.getModel();
		DataUtil.getForm(this.getClass(), "testInit1", form);
		form.setChannelId(channelId+"");
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		initAction1.setSession(session);
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testInit1() throws Exception {
		setUpInit1();
		//验证插入的渠道信息
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", "Basis.BIN_Channel");
		paramMap.put("BIN_ChannelID", form.getChannelId());
		List<Map<String, Object>> resultList = testService
				.getTableData(paramMap);
		Assert.assertEquals("渠道数量不正确", 1, resultList.size());
		
		Map<String, Object> channelData = resultList.get(0);
		Assert.assertEquals("组织信息ID不正确", 1212,
				channelData.get("BIN_OrganizationInfoID"));
		Assert.assertEquals("品牌ID不正确", 121201, channelData.get("BIN_BrandInfoID"));
		Assert.assertEquals("渠道名不正确", "连121201",
				channelData.get("ChannelName"));
		Assert.assertEquals("渠道更新次数不正确", 1212,
				channelData.get("ModifyCount"));
		Assert.assertEquals("success", proxy.execute());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testExclusiveUpdate() throws Exception {
		setUpInit1();
		Assert.assertEquals("success", proxy.execute());
		//编辑提交的数据
		form.setChannelName("testASDF1212");
		Map channelDetail = initAction1.getChannelDetail();
		form.setModifyCount(channelDetail.get("modifyCount").toString());
		form.setModifyTime(channelDetail.get("modifyTime").toString());
		//未有其他用户修改信息的情况
		initAction1.save();
		Assert.assertEquals(true,initAction1.getActionMessages().contains(initAction1.getText("ICM00002")));
		//该信息已修改，未重新读取直接操作的情况
		form.setChannelName("testASDF1213");
		initAction1.save();
		Assert.assertEquals(true, initAction1.getActionErrors().contains(initAction1.getText("ECM00038")));
	}

}
