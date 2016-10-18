package com.cherry.shindig.gadgets.container.handler.mb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.shindig.gadgets.service.mb.MemDetailInfoService;
import com.googlecode.jsonplugin.JSONUtil;

@Service(name = "memInfo")
public class MemInfoHandler {
	
	/** 查看会员详细信息Service **/
	@Resource
	private MemDetailInfoService memDetailInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map gadgetParam = (Map)paramMap.get("gadgetParam");
		// 查询会员基本信息
		Map<String, Object> memDetailInfo = memDetailInfoService.getMemberInfo(gadgetParam);
		// 返回结果
		Map resultData = new HashMap();
		resultData.put("memDetailInfoMes", memDetailInfo);
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
