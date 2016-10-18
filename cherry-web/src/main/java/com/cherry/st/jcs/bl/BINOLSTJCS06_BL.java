package com.cherry.st.jcs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.jcs.interfaces.BINOLSTJCS06_IF;
import com.cherry.st.jcs.service.BINOLSTJCS06_Service;
@SuppressWarnings("unchecked")
public class BINOLSTJCS06_BL implements BINOLSTJCS06_IF{

	@Resource
	private BINOLSTJCS06_Service binOLSTJCS06_Service;
	
	/**WebService 共通BL*/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 逻辑仓库WebService接口头文件数据操作标记：添加 */
	public static final String operateType_A = "A";
	
	/** 逻辑仓库WebService接口头文件数据操作标记：编辑 */
	public static final String operateType_U = "U";
	
	/** 逻辑仓库WebService接口头文件数据操作标记：删除 */
	public static final String operateType_D = "D";
	
	@Override
	public List<Map<String, Object>> getLogInvByBrand(Map<String,Object> map) {
		return binOLSTJCS06_Service.getLogInvByBrand(map);
	}

	@Override
	public Map<String,Object> getLogInvByLogInvId(Map<String, Object> map) {
		return binOLSTJCS06_Service.getLogInvByLogInvId(map);
	}

	@Override
	public int tran_updateLogInv(Map<String, Object> map) throws Exception {
		if(map.get("defaultFlag")!=null&&map.get("defaultFlag").equals("1")){
			cancleDefaultFlag(map);
		}
		
		int i = 0;
		
	   // 下发当前仓库类型为终端的逻辑仓库信息--需要组装修改前后的数据
		if(map.get("type").equals("1")){
			List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
			// 修改前的逻辑仓库数据
			map.put("Operate", operateType_D);
			detailList.addAll(0,binOLSTJCS06_Service.getLogInvByBrandWithWS(map));
		    i = binOLSTJCS06_Service.updateLogInv(map);
		    if(i != 0){
		    	// 修改后的逻辑仓库数据
		    	map.put("Operate", operateType_A);  // DetailList列表字段：操作
		    	detailList.addAll(1,binOLSTJCS06_Service.getLogInvByBrandWithWS(map));
		    	// 下发当前仓库类型为终端的逻辑仓库信息
		    	map.put("DetailList", detailList);
		    	issued_BL(map,operateType_U);
		    } 
		} else {
			i = binOLSTJCS06_Service.updateLogInv(map);
		}
	   return i;
	}

	@Override
	public int tran_insertLogInv(Map<String, Object> map) throws Exception {
		if(map.get("defaultFlag")!=null&&map.get("defaultFlag").equals("1")){
			cancleDefaultFlag(map);
		}
		int logInvId = binOLSTJCS06_Service.insertLogInv(map);
		map.put("logInvId", logInvId);
		
		// 下发当前仓库类型为终端的逻辑仓库信息
		if(map.get("type").equals("1")){
			map.put("Operate", operateType_A); // DetailList列表字段：操作
			List<Map<String,Object>> detailList = binOLSTJCS06_Service.getLogInvByBrandWithWS(map);
			map.put("DetailList", detailList);
			issued_BL(map,operateType_A);
		}
	    return logInvId;
	}
	
	/**取消原有默认仓库
	 * 
	 * @param
	 * @return
	 * */
	private void cancleDefaultFlag(Map map){
		binOLSTJCS06_Service.cancleDefaultFlag(map);
	}
	
	/**下发处理
	 * 
	 * @param
	 * @param operateType A:新增；U:修改；D:删除。
	 * @return
	 * @throws Exception 
	 * */
	private void issued_BL(Map<String, Object> map,String operateType) throws Exception{
		//是否调用Webservice进行逻辑仓库数据下发
		boolean issuedWS = binOLCM14_BL.isConfigOpen("1061",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		if(issuedWS){
			// 组装逻辑仓库WebService数据
			Map<String,Object> logInvData =	getLogInvWSMap(map,operateType);
			
			if(null != logInvData){
				//WebService方式台信息Map
				Map<String,Object> resultMap = binOLCM27_BL.accessWebService(logInvData);
				String state = ConvertUtil.getString(resultMap.get("State"));
				if(state.equals("ERROR")){
					throw new CherryException("ECM00035");
				}
			}
		}
	}
	
	/**
	 * 组装逻辑仓库WebService数据
	 * @param map
	 * @param operateType A:新增；U:修改；D:删除。
	 * @return
	 */
	private Map<String,Object> getLogInvWSMap(Map<String,Object> map,String operateType){
		
		Map<String,Object> logInvData = new HashMap<String,Object>();
		
		// 头文件
		Map<String,Object> dataHead = new HashMap<String,Object>();
		// 品牌代码
		dataHead.put("BrandCode", map.get(CherryConstants.BRAND_CODE));
		// 业务类型
		dataHead.put("BussinessType", "Warehouse");
		// 消息体版本号
		dataHead.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
		// SubType A:新增；U:修改；D:删除
		dataHead.put("SubType", operateType);
		
		// 明细数据行--逻辑仓库数据
		List<Map<String,Object>> detailList = (List<Map<String,Object>>)map.get("DetailList");
		
		if(detailList.size() == 0){
			return null;
		}
		
		logInvData.putAll(dataHead);
		// 明细数据行--逻辑仓库数据
		logInvData.put("DetailList", detailList);
		
		return logInvData;
	}

}
