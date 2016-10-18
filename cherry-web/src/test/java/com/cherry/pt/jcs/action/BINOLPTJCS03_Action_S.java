package com.cherry.pt.jcs.action;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cherry.cm.core.BaseTest;
import com.cherry.cm.util.DataUtil;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class BINOLPTJCS03_Action_S extends TestCase{

	private Selenium selenium;
	
	@Before
	public void setUp() throws Exception {
		
		selenium = new DefaultSelenium("127.0.0.1", 4444, "*iexplore", "http://127.0.0.1:8080/");
        selenium.start();
       
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSave() throws Exception{
		//系统登录
		List dataList = DataUtil.getDataList(this.getClass());
		Map loginMap = (Map) dataList.get(0);
		BaseTest.login(selenium, loginMap);
		
		//打开基础数据子系统
		selenium.click("link=基础数据");
		selenium.waitForPageToLoad("30000");
		
		//打开产品维护模块
		selenium.click("//a[@id='amenuA6']/strong");
		selenium.click("amenuA6B1");
		selenium.click("//a[@class='add']");
		
		//打开子窗口
		selenium.waitForPopUp(selenium.getAllWindowNames()[1], "8000");
		selenium.selectWindow(selenium.getAllWindowNames()[1]);
		
		selenium.type("salePrice","200");
		
		//增加keyup事件，用于触发验证
		selenium.fireEvent("salePrice","keyup");
		//增加focusout事件，用于触发验证
		selenium.fireEvent("salePrice","focusout");

		assertTrue(selenium.getValue("//input[@id='salePrice']").equals("200.00"));
		assertTrue(selenium.getValue("//input[@id='memPrice']").equals("200.00"));
		
		selenium.click("//span[@class='calculator']");
		selenium.type("memRate","85");
		selenium.fireEvent("memRate","keyup");
		selenium.fireEvent("memRate","blur");
		assertTrue(selenium.getValue("//input[@id='memPrice']").equals("170.00"));
	}
	
	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}

}
