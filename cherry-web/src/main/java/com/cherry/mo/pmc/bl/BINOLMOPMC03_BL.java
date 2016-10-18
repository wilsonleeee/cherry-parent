package com.cherry.mo.pmc.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM05_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.interfaces.BINOLMOPMC03_IF;
import com.cherry.mo.pmc.service.BINOLMOPMC03_Service;
import com.cherry.synchro.mo.bl.PosMenuSynchro;

public class BINOLMOPMC03_BL implements BINOLMOPMC03_IF {

	private static Logger logger = LoggerFactory.getLogger(BINOLMOPMC03_BL.class.getName());
	@Resource(name="binOLMOPMC03_Service")
	private BINOLMOPMC03_Service binOLMOPMC03_Service;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	@Resource(name="binOLCM05_Service")
	private BINOLCM05_Service binOLCM05_Service;
	@Resource(name="posMenuSynchro")
	private PosMenuSynchro posMenuSynchro;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getCounterConfTree(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
		String selMode = ConvertUtil.getString(map.get("selMode"));
		List<Map<String, Object>> resultList = null;
		/**
		 * 不同菜单组可分配给相同柜台【原来的逻辑是一个柜台只能有一个菜单组配置】
		 * 票号：WITPOSQA-14460
		 * 
		 */
//		List<String> counterIDList = binOLMOPMC03_Service.getHaveOtherMenuGrpCnt(map);
//		map.put("counterIDList", counterIDList);
		if("1".equals(selMode)){
			resultList = binOLMOPMC03_Service.getRegionCntList(map);
			List keysList = new ArrayList();
			String[] keys1 = { "regionId", "regionName" };
			String[] keys2 = { "provinceId", "provinceName" };
			String[] keys3 = { "cityId", "cityName" };
			String[] keys4 = { "organizationId", "counterName", "isConfigCnt", "counterInfoId" };
			keysList.add(keys1);
			keysList.add(keys2);
			keysList.add(keys3);
			keysList.add(keys4);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,
					keysList, 0);
			// 标记已经配置当前菜单组的柜台
			checkConfCounter(resultTreeList);
		} else if("3".equals(selMode)){
			resultList = binOLMOPMC03_Service.getCounterConfInfo(map);
			resultTreeList = ConvertUtil.getTreeList(resultList, "nodes");
			// 标记已经配置当前菜单组的柜台
			checkConfCounter(resultTreeList);
		}
		return resultTreeList;
	}
	
	/**
	 * 保存菜单配置的柜台并将数据下发到老后台
	 * @param map
	 * @param list:菜单组配置的柜台LIST
	 * 
	 */
	@Override
	public void tran_saveCounterConfig(Map<String, Object> map,
			List<Map<String, Object>> list) throws Exception {
		/**
		 *  取共通的时间，用于对终端specials表的定位，此次升级后需要先将
		 *  新后台的数据与终端specials表的数据进行同步处理后才可使用
		 */
		String sysDate = binOLMOPMC03_Service.getSYSDateTime();
		map.put(CherryConstants.CREATE_TIME, sysDate);
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		
		// 取得指定菜单组的菜单配置[无机器类型的菜单不列入管理范围]
		List<Map<String, Object>> comMenuConf = binOLMOPMC03_Service.getGrpMenuConf(map);
		boolean isNullMenuConf = (null == comMenuConf || comMenuConf.size() == 0) ? true : false;
		// 品牌代码
		String BrandCode = binOLCM05_BL.getBrandCode(ConvertUtil.getInt(map
				.get(CherryConstants.BRANDINFOID)));
		// 操作参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 取得当前菜单组对应的旧（操作前）柜台LIST---旧柜台CODE组
		List<String> oldCounterList = binOLMOPMC03_Service.getOldMenuGrpCnt(paramMap);
		
		/**
		 * ====不同菜单组分配给相同柜台：
		 * ========在新后台的差分表中加菜单组ID字段，方便对指定菜单组的老柜台菜单配置信息进行删除
		 * ========新后台的差分表只在首次使用柜台个性化菜单功能时用于同步终端数据时有用，其他情况无用【故可将菜单组ID为空的都删除】。
		 */
		// ==============step1：删除旧的品牌柜台菜单特殊配置信息表【包括终端的相应信息】======================
		// 删除此次取消配置的柜台
		//paramMap.put("oldCounterList", oldCounterList);
		// 取得当前菜单组对应的差分信息表中的创建与更新时间用于定位终端的相应信息
		Map<String, Object> menuGrpmodifyTime = binOLMOPMC03_Service.getModifyTimeForSpecials(paramMap);
				
		// 改为根据菜单组ID进行删除
		binOLMOPMC03_Service.delPosMenuBrandCounter(paramMap);
		
		// 新后台差分表信息删除后即开始删除终端specials表中的老数据
		Map<String, Object> delParam = new HashMap<String, Object>();
		delParam.put("BrandCode", BrandCode);
		delParam.put("machineType", map.get("machineType"));
		// 之前下发的柜台
		delParam.put("OldCounterList", oldCounterList);
		/**对于已经过期的柜台菜单配置，如果再为此柜台重新配置个性化菜单不删除此柜台的旧配置信息*/
		delParam.put("StartTime", map.get("startTime"));
		delParam.put("EndTime", map.get("endTime"));
		// 调用存储过程删除老柜台的菜单配置
		/*** 
		 * 此处的存储过程需要修改，只删除当前菜单组的个性化配置信息
		 * ========因终端specials表无菜单组ID,
		 * ========暂用终端的created、modified字段【这两个字段必须与新后台的差分表严格一致】
		 * ========来定位要删除的配置信息
		 * ***/
		if(menuGrpmodifyTime != null){
			delParam.put(CherryConstants.CREATE_TIME, ConvertUtil.getString(menuGrpmodifyTime.get("createTime")));
			delParam.put(CherryConstants.UPDATE_TIME,ConvertUtil.getString(menuGrpmodifyTime.get("updateTime")));
		} else {
			// 存储过程中的相关字段必须有值
			delParam.put(CherryConstants.CREATE_TIME, "1900-01-01 00:00:00.000");
			delParam.put(CherryConstants.UPDATE_TIME, "1900-01-01 00:00:00.000");
		}
		// 调用一次存储过程将之前下发的当前菜单组的下发信息进行删除
		posMenuSynchro.delPosMenuBrandCounter(delParam);
		
		// ===========================step2 : 更新柜台菜单组对应表================================
		// 插入前先删除分组菜单柜台对应表
		binOLMOPMC03_Service.delPosMenuGrpCounter(paramMap);
		// 将共通参数加入到柜台LIST中
		for(Map<String, Object> insertMap : list) {
			insertMap.putAll(paramMap);
		}
		// 将配置的柜台写入分组菜单柜台对应表
		if(!isNullMenuConf){
			binOLMOPMC03_Service.insertPosMenuGrpCounter(list);
		}
		
		// ===================step3 : 将数据反应到品牌柜台菜单差分表中（Monitor.BIN_PosMenuBrandCounter）===========
		// 用于下发的柜台参数(新配置的柜台CODE组)---新柜台CODE组
		List<String> listCounter = new ArrayList<String>();
		
		if(!isNullMenuConf){
			// 将分组菜单柜台对应表的信息反应到品牌菜单差分表中【此处的LIST中已经包含了map参数 】
			for(Map<String, Object> listMap : list) {
				// 取得柜台对应的（当前菜单组）的菜单配置【菜单配置都是一样的，不同的是柜台】
				List<Map<String, Object>> counterMenuConf = binOLMOPMC03_Service.getCounterMenuConf(listMap);
				if(null != counterMenuConf && counterMenuConf.size() > 0){
					// 插入柜台的品牌柜台菜单特殊配置信息--【仅当前菜单组的特殊配置信息】
					binOLMOPMC03_Service.insertPosMenuBrandCounter(counterMenuConf);
					// 记录新配置的柜台CODE组
					listCounter.add(ConvertUtil.getString(counterMenuConf.get(0).get("CounterCode")));
				}
			}
		
		// ===================step4 : 下发品牌柜台菜单差分表的数据到终端表（WP3PCSA_menu_brand_special），须调用存储过程=======
		
			/**
			 * 集合计算：取得需删除的柜台组与需新增的柜台组【未变化的柜台组不进行变动】
			 * 存储过程合并在cherry_base_publishPosMenuBrandCounter中
			 * 存储过程逻辑：删除取消的柜台的个性化配置信息，插入新增的柜台的个性化配置信息。
			 * 
			 * */
//			List<String> retainCounterList = new ArrayList<String>();
//			retainCounterList.addAll(oldCounterList);
//			// 取得新老柜台组的交集
//			retainCounterList.retainAll(listCounter);
//			// 老柜台组中剔除新老柜台交集后取得要删除的柜台组差分信息
//			oldCounterList.removeAll(retainCounterList);
//			// 取得新柜台组中剔除新老柜台交集后取得要新增的柜台组差分信息
//			listCounter.removeAll(retainCounterList);
			
			/**
			 * 终端差分表无菜单组ID字段，用差分信息的创建、更新时间来代替，可定位是哪个菜单组的id
			 * 
			 * */
			// 调用存储过程进行下发
			for(Map<String, Object> comMenuMap : comMenuConf){
				Map<String, Object> param = new HashMap<String, Object>();
				// 取得菜单ID对应的名称及CODE
				Map<String, Object> menuCodeName = binOLMOPMC03_Service.getPosMenuCodeName(comMenuMap);
				param.put("BrandCode", BrandCode);
				param.put("MenuCode", menuCodeName.get("MenuCode"));
				param.put("MachineType", paramMap.get("machineType"));
				param.put("MenuNameCN", menuCodeName.get("MenuNameCN"));
				param.put("MenuNameEN", menuCodeName.get("MenuNameEN"));
				param.put("MenuStatus", comMenuMap.get("MenuStatus"));
				param.put("StartTime", paramMap.get("startTime"));
				param.put("EndTime", paramMap.get("endTime"));
				param.put(CherryConstants.CREATE_TIME, sysDate);
				param.put(CherryConstants.UPDATE_TIME, sysDate);
				// 须删除的柜台差分信息【用于删除】
//				param.put("OldCounterList", oldCounterList);
				// 须新增的柜台差分信息
				param.put("CounterCodeList", listCounter);
				// 调用一次存储过程将此柜台组的信息下发(只有一条菜单变动信息)
				posMenuSynchro.publishPosMenuBrandCounter(param);
			}
			
			// 设置发布时间
			Map<String, Object> mapPublishDateParam = new HashMap<String, Object>();
			mapPublishDateParam.putAll(map);
			String publishDate = binOLMOPMC03_Service.getPublishDate();
			mapPublishDateParam.put("publishDate",publishDate);
			binOLMOPMC03_Service.modifyPublishDate(mapPublishDateParam);
		}
		
	}
	
	/**
	 * 取得渠道柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
		// 取得已经配置过其他菜单组的柜台
		/**
		 * 不同菜单组可分配给相同柜台【原来的逻辑是一个柜台只能有一个菜单组配置】
		 * 票号：WITPOSQA-14460
		 * 
		 */
//		List<String> counterIDList = binOLMOPMC03_Service
//				.getHaveOtherMenuGrpCnt(map);
//		map.put("counterIDList", counterIDList);
		// 查询区域信息List
		List<Map<String, Object>> channelList = binOLMOPMC03_Service
				.getChannelCntList(map);
		List<String[]> keysList = new ArrayList<String[]>();
		String[] keys1 = { "channelId", "channelName" };
		String[] keys2 = { "organizationId", "departName", "counterInfoId",
				"isConfigCnt" };
		keysList.add(keys1);
		keysList.add(keys2);
		List<Map<String, Object>> channelTreeList = new ArrayList<Map<String, Object>>();
		// 把线性的结构转化为树结构
		ConvertUtil.jsTreeDataDeepList(channelList, channelTreeList, keysList,
				0);
		checkConfCounter(channelTreeList);
		return channelTreeList;
	}

	/**
	 * 标记已经配置该菜单组的柜台
	 * @param list
	 */
	private boolean checkConfCounter(List<Map<String, Object>> list) {
		boolean flag = false;
		if(null == list || 0 == list.size()){
			return false;
		}
		for(Map<String, Object> map : list) {
			if(map.containsKey("nodes")) {
//				map.put("open", true);
				List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map
						.get("nodes");
				if(checkConfCounter(nodesList)) {
					map.put("checked", true);
					flag = true;
				}
			} else {
				if("0".equals(map.get("isConfigCnt"))){
					// 终节点不为已经配置过的柜台
					flag = false;
				} else if("1".equals(map.get("isConfigCnt"))) {
					map.put("checked", true);
					flag = true;
				}
			}
		}
		return flag;
		
	}

	@Override
	public List<Map<String, Object>> parseFile(File file,
			Map<String, Object> map) throws CherryException, Exception {
		if (file == null || !file.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 用于验证柜台号是否存在
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.putAll(map);
		// 品牌Code
		String brand_code = binOLCM05_Service.getBrandCode(ConvertUtil.getInt(map.get(CherryConstants.BRANDINFOID)));
		// 取得已经配置过其他菜单组的柜台
//		List<String> counterIDList = binOLMOPMC03_Service
//				.getHaveOtherMenuGrpCnt(checkMap);
		// upExcel
		Workbook wb = null;
		try {
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(file, workbookSettings);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 上传文件解析失败，请检查文件扩展名是否为.xls！
			throw new CherryException("EBS00041");
		}
		if (null == wb) {
			throw new CherryException("EBS00041");
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// 产品数据sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (CherryConstants.COUNTER_SHEET_NAME.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// 柜台数据sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		for (int r = 1; r < dateSheet.getRows(); r++) {
			// 品牌代码
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			// 柜台编码
			String counterCode = dateSheet.getCell(1, r).getContents().trim();
			// 柜台名称
			String counterName = dateSheet.getCell(2, r).getContents().trim();
			
			// 整行数据为空，程序认为sheet内有效行读取结束
			if (CherryChecker.isNullOrEmpty(brandCode)
					&& CherryChecker.isNullOrEmpty(counterCode)
					&& CherryChecker.isNullOrEmpty(counterName)) {
				break;
			}
			checkMap.put("counterCode", counterCode);
			// 数据库对应的柜台信息
			Map<String, Object> dbCounterInfo = binOLMOPMC03_Service.getCounterInfo(checkMap);
			if(!CherryChecker.isNullOrEmpty(dbCounterInfo)){
				// 数据库中柜台CODE对应的ID
				/**
				 * 新逻辑允许对已经配置过其他菜单组的柜台再次配置菜单组
				 */
//				String dbCounterInfoID = ConvertUtil.getString(dbCounterInfo.get("counterInfoID"));
//				for(String counterID : counterIDList){
//					if(counterID.equals(dbCounterInfoID)){
//						// 此柜台已经配置过其他菜单组，不能进行此操作！
//						throw new CherryException("EBS00104", new String[] {
//								CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
//					}
//				}
			}
			
			if("".equals(brandCode)) {
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			} else if(!brand_code.equals(brandCode)) {
				// 出现不符合的品牌
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "A" + (r + 1) });
			}
			// 判断柜台号与柜台名称【柜台号必填，柜台名称要么为空，否则必须是柜台号对应的柜台名称】
			if("".equals(counterCode)) {
				// 单元格为空
				throw new CherryException("EBS00031", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(counterCode.length() > 15) {
				// 长度错误
				throw new CherryException("EBS00033", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1), "1" });
				
			} else if(CherryChecker.isNullOrEmpty(dbCounterInfo)) {
				// 导入的发布柜台不存在[{0}sheet单元格[{1}]数据无效或无权限操作！]
				throw new CherryException("EBS00032", new String[] {
						CherryConstants.COUNTER_SHEET_NAME, "B" + (r + 1) });
			} else if(!"".equals(counterName)) {
				// 柜台号正确的情况下才去判断柜台名称
				if(!counterName.equals(dbCounterInfo.get("counterName"))){
					// 柜台号要么为空，要么是柜台号对应的柜台名称
					// {0}sheet单元格[{1}]数据有误,请确认是否为柜台号对应的名称！
					throw new CherryException("EMO00083", new String[] {
							CherryConstants.COUNTER_SHEET_NAME, "C" + (r + 1) });
				}
			}
			
			/** 组装柜台数据***/
			Map<String, Object> listMap = new HashMap<String, Object>();
			// 只取柜台ID
			listMap.put("counterInfoID", dbCounterInfo.get("counterInfoID"));
			list.add(listMap);
		}
		if(list.size() == 0) {
			throw new CherryException("EBS00035",
					new String[] { CherryConstants.COUNTER_SHEET_NAME });
		}
		// 去除重复的数据，保证重复的数据只记录一条
		this.makeListUnique(list);
		return list;
	}

	/**
	 * 去除重复数据
	 * @param list
	 */
	private void makeListUnique(List<Map<String, Object>> list) {
		//set方法去除list中重复的数据 set中插入重复的值只保留一个
    	HashSet h = new HashSet(list);
    	list.clear();
    	list.addAll(h);
	}

}
