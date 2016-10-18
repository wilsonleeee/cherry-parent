package com.cherry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;

@ContextConfiguration(locations = { "classpath:conf/spring-conf-test/applicationContextTest.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)

public class CherryJunitBase extends StrutsSpringTransactionalTests {
	private static final String CONFIG_PROPERTIES = "conf/properties/cherry.properties";
	protected ActionProxy proxy;
	protected static final String dbname = "CherryBrandDB";
	protected static MockServletContext servletContext = new MockServletContext();
	protected Map<String, Object> session = new HashMap<String, Object>();
	protected MockHttpServletRequest request = new MockHttpServletRequest();
	protected MockHttpServletResponse response = new MockHttpServletResponse();

	@BeforeClass
	public static void beforeClass() throws Exception {
		setconfigProperties();
		CustomerContextHolder.setCustomerDataSourceType(dbname);
	}

	@AfterClass
	public static void afterClass() throws Exception {
//		CustomerContextHolder.clearCustomerDataSourceType();
	}
	

	/**
	 * 娓呴櫎 Context
	 * 
	 * @throws Exception
	 */
	@After
	public void after() throws Exception {
		// applicationContext = null;
		// servletContext = null;
		session.clear();
		// request.removeAllParameters();
		request.clearAttributes();
	//	CustomerContextHolder.clearCustomerDataSourceType();
	}

	/**
	 * session娣诲姞key-value,鍚屾httpSession涓巗ession
	 * 
	 */
	public void setSession(String key, Object value) {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(key, value);
		session.put(key, value);
	}

	/**
	 * Created action class based on namespace and name
	 * 
	 * @param clazz
	 *            Class for which to create Action
	 * @param namespace
	 *            Namespace of action
	 * @param name
	 *            Action name
	 * @return Action class
	 * @throws Exception
	 *             Catch-all exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> T createAction(Class<T> actionClass, String namespace,
			String name) throws Exception {
		System.out.println("=========================createAction=========================");
		// create a proxy class which is just a wrapper around the action call.
		// The proxy is created by checking the namespace and name against the
		// struts.xml configurationet
		proxy = getActionProxy(actionClass, namespace, name);
		// by default, don't pass in any request parameters
		ActionContext actionContext = proxy.getInvocation()
				.getInvocationContext();
		actionContext.setParameters(new HashMap());
		// set token
		request.setParameter("csrftoken", "csrftokencsrftokencsrftokencsrftoken");
		setSession("csrftoken", "csrftokencsrftokencsrftokencsrftoken");
		setSession("hiscsrftoken",",csrftokencsrftokencsrftokencsrftoken,");
		actionContext.setSession(session);
		// do not execute the result after executing the action
		proxy.setExecuteResult(true);
		// set the actions context to the one which the proxy is using
		ServletActionContext.setContext(actionContext);
		ServletActionContext.setRequest(request);
		ServletActionContext.setResponse(response);
		ServletActionContext.setServletContext(servletContext);
		// // 鍔犺浇鏁版嵁婧�
//		setSession(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY, dbname);
//		 CustomerContextHolder.setCustomerDataSourceType(dbname);
		return (T) proxy.getAction();
	}

	/**
	 * create a proxy class
	 * 
	 * @param actionClass
	 * @param namespace
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ActionProxy getActionProxy(Class actionClass, String namespace,
			String name) throws Exception {
		System.out.println("=========================getActionProxy=========================");
		// 鍔犺浇閰嶇疆鏂囦欢
		
		setContext();
		// action package name
		String actionPackages = actionClass.getPackage().getName();
		// Dispatcher is the guy that actually handles all requests. Pass in
		// an empty. Map as the parameters but if you want to change stuff like
		// what config files to read, you need to specify them here. Here's how
		// to
		// scan packages for actions (thanks to Hardy Ferentschik - Comment 66)
		// (see Dispatcher's source code)
		HashMap params = new HashMap();
		params.put("actionPackages", actionPackages);
		Dispatcher dispatcher = new Dispatcher(servletContext, params);
		dispatcher.init();
		Dispatcher.setInstance(dispatcher);
		try{ActionProxyFactory factory = dispatcher.getContainer().getInstance(
				ActionProxyFactory.class);
		return factory.createActionProxy(namespace, name, null, null, true,
				false);
		}
		catch(Exception e){
			System.out.println("=========================getActionProxy  failed=========================");
			throw e;
		}

		
	}

	/**
	 * read cherry.properties
	 */
	private static void setconfigProperties() {
		System.out.println("=========================setconfigProperties=========================");
		if (PropertiesUtil.pps == null) {
			PropertiesUtil.pps = new Properties();
			URL url = null;
			try {
				url = Thread.currentThread().getContextClassLoader()
						.getResource(CONFIG_PROPERTIES);
				InputStream in = url.openStream();
				InputStreamReader read = new InputStreamReader(in, "UTF-8");
				PropertiesUtil.pps.load(read);
				// 鏍圭洰褰�
				String cherryHome = PropertiesUtil.pps
						.getProperty(CherryConstants.CHERRY_HOME);
				Set<Map.Entry<Object, Object>> set = PropertiesUtil.pps
						.entrySet();
				Iterator<Map.Entry<Object, Object>> it = set.iterator();
				while (it.hasNext()) {
					Map.Entry<Object, Object> entry = it.next();
					String value = (String) entry.getValue();
					// 鍊间腑甯︽湁鏍圭洰褰曞弬鏁�
					if (value != null
							&& value.indexOf("${" + CherryConstants.CHERRY_HOME
									+ "}") >= 0) {
						String repVal = value
								.replace("${" + CherryConstants.CHERRY_HOME
										+ "}", cherryHome);
						// 灏嗗叿浣撶殑鏍圭洰褰曡矾寰勮缃埌鍙橀噺閲�
						PropertiesUtil.pps.setProperty((String) entry.getKey(),
								repVal);
					}
				}
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * set codeTable
	 */
	private void setCodeTable() {
		if (servletContext.getAttribute("CodeTable") == null) {
			BaseConfServiceImpl baseConfServiceImpl = (BaseConfServiceImpl) applicationContext
					.getBean("baseConfServiceImpl");
			CodeTable codeTable = (CodeTable) applicationContext
					.getBean("CodeTable");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.IBATIS_SQL_ID,
					"BINOLCMINC99.getCodeList");
			// 鍙栧緱CODE鍊间竴瑙�
			List codeList = baseConfServiceImpl.getList(paramMap);
			codeTable.setCodesMap(codeList);
			servletContext.setAttribute("CodeTable", codeTable);
		}
	}

	/**
	 * 鍔犺浇 Context
	 * 
	 * @param actionPackages
	 * @throws Exception
	 */
	protected void setContext() throws Exception {
		servletContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				applicationContext);
		setCodeTable();
		// Struts JSP support servlet (for Freemarker)
		new JspSupportServlet().init(new MockServletConfig(servletContext));
		// 鍔犺浇鏁版嵁婧�
//		CustomerContextHolder.setCustomerDataSourceType(dbname);
		setSession(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY, dbname);
	}
}
