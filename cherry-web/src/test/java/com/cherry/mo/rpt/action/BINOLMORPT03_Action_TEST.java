package com.cherry.mo.rpt.action;

import java.util.HashMap;
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
import com.cherry.cm.service.TESTCOM_Service;
import com.cherry.cm.util.DataUtil;
import com.cherry.mo.rpt.form.BINOLMORPT03_Form;

public class BINOLMORPT03_Action_TEST extends CherryJunitBase {

	private BINOLMORPT03_Action action;
	private BINOLMORPT03_Form form;

	@Resource
	private TESTCOM_Service testService;

	public void setUpGetPaperCount1() throws Exception {
		action = createAction(BINOLMORPT03_Action.class, "/mo",
				"BINOLMORPT03_getPaperCount");
		UserInfo userInfo = DataUtil.getUserInfo(this.getClass(),
				"testGetPaperCount1");
		form = action.getModel();
		DataUtil.getForm(this.getClass(), "testGetPaperCount1", form);
		List<Map<String, Object>> testDataList = DataUtil.getDataList(this.getClass(),
				"testGetPaperCount1");
		int staticId = 0;
		for (int i = 0; i < testDataList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) testDataList.get(i);
			if (i == 0) {
				// 插入问卷主表
				staticId = testService.insertTableData(map);
			} else {
				// 插入答卷主表
				map.put("BIN_PaperID", staticId);
				testService.insertTableData(map);
			}
		}
		form.setPaperId(staticId+"");
		setSession("userinfo", userInfo);
		setSession(CherryConstants.SESSION_USERINFO, userInfo);
		action.setSession(session);
	}

	@Test
	@Rollback(true)
	@SuppressWarnings("unchecked")
	@Transactional
	public void testGetPaperCount1() throws Exception{
		//设置
		setUpGetPaperCount1();
		//验证插入问卷主表是否正确
		Map<String, Object> paramMap= new HashMap<String, Object>();
		paramMap.put("tableName", "Monitor.BIN_Paper");
		paramMap.put("PaperName", "TEST2013010601");
		List<Map<String,Object>> paperList = testService.getTableData(paramMap);
		Map<String, Object> paperMap = paperList.get(0);
		Assert.assertEquals("问卷名称不正确","TEST2013010601",paperMap.get("PaperName") );
		Assert.assertEquals("问卷类型不正确","2",paperMap.get("PaperType") );
		
		//验证插入答卷主表是否正确
		Map<String, Object> paramAnsMap = new HashMap<String, Object>();
		paramAnsMap.put("tableName", "Monitor.BIN_PaperAnswer");
		paramAnsMap.put("BIN_PaperID", paperMap.get("BIN_PaperID"));
		List<Map<String, Object>> answerList = testService.getTableData(paramAnsMap);
		Map<String, Object> answerMap = answerList.get(0);
		Assert.assertEquals("问卷类型不正确","2",answerMap.get("PaperType"));
		
		//执行action中对应的方法
		proxy.execute();
//		response.setCharacterEncoding("utf-8");
//		action.setServletResponse(response);
//		action.setServletRequest(request);
		Assert.assertEquals("问卷数量不正确",1,action.getPaperAmount());
	}
}
