package com.cherry.cm.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.BaseResult;
import com.cherry.cm.cmbeans.BindResult;
import com.cherry.cm.cmbeans.MemQueryResult;
import com.cherry.cm.cmbeans.QueryResult;
import com.cherry.cm.cmbeans.RegisterResult;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.SignTool;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
@SuppressWarnings("serial")
public class CherrySignInterceptor extends AbstractInterceptor{
	
	private static Logger logger = LoggerFactory
			.getLogger(CherrySignInterceptor.class.getName());
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
		String body = SignTool.getBody(request);
		String action = invocation.getProxy().getActionName();
		String appSecret = null;
		Map<String, Object> params = null;
		try {
			logger.info("*********************Action: " + action + " 请求参数:" + body);
			params = CherryUtil.json2Map(body);
			if (null != params && !params.isEmpty()) {
				TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandName((String) params.get("seller_name"));
				if (tmallKey != null) {
					appSecret = tmallKey.getAppSecret();
				}
			}
		} catch (Exception e) {
			logger.error("*********************天猫请求参数转换失败，异常信息：" + e.getMessage(),e);
		}
		if(!SignTool.checkSign(request,body, appSecret)){
			logger.error("*********************天猫验证签名失败");
			if (appSecret == null) {
				logger.error("无法获取天猫appSecret! body: " + body);
			}
			String errJson = null;
			if (SignTool.ACTION_QUERY.equals(action)) {
				QueryResult rst = new QueryResult();
				rst.setBind_code("E05");
				errJson = CherryUtil.obj2Json(rst);
			} else if (SignTool.ACTION_BIND.equals(action)) {
				BindResult rst = new BindResult();
				rst.setBind_code("E01");
				errJson = CherryUtil.obj2Json(rst);
			} else if (SignTool.ACTION_REGISTER.equals(action)) {
				RegisterResult rst = new RegisterResult();
				rst.setRegister_code("E01");
				errJson = CherryUtil.obj2Json(rst);
			} else if (SignTool.ACTION_MEMQUERY.equals(action)) {
				MemQueryResult rst = new MemQueryResult();
				rst.setQuery_code("E04");
				errJson = CherryUtil.obj2Json(rst);
			} else {
				BaseResult rst = new BaseResult();
				errJson = CherryUtil.obj2Json(rst);
			}
			HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
			ConvertUtil.setResponseByAjax(response, errJson);
			return null;
		}
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("params", params);
		invocation.getInvocationContext().setParameters(parameters);
		return invocation.invoke();
	}

}
