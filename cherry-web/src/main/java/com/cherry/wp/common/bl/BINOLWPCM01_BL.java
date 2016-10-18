package com.cherry.wp.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.common.service.BINOLWPCM01_Service;

public class BINOLWPCM01_BL implements BINOLWPCM01_IF{
	
	@Resource(name="binOLWPCM01_Service")
	private BINOLWPCM01_Service binOLWPCM01_Service;

	@Override
	public List<Map<String, Object>> getOrderCounterCode(Map<String, Object> map)
			throws Exception {
		// 获取搜索字符串作为会员卡号
		String memberCode = ConvertUtil.getString(map.get("searchStr"));
		// 获取搜索字符串作为手机号
		String mobilePhone = ConvertUtil.getString(map.get("searchStr"));
		// 获取品牌编号
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 根据会员卡号查询会员数量
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberCode", memberCode);
		paramMap.putAll(map);
		List<Map<String, Object>> OrderCounterCodeList = binOLWPCM01_Service.getOrderCounterCode(paramMap);
		if(OrderCounterCodeList == null || OrderCounterCodeList.isEmpty()){
			try{
				if (!CherryChecker.isNullOrEmpty(mobilePhone, true)) {
					mobilePhone = CherrySecret.encryptData(brandCode, mobilePhone);
				}
			}catch(Exception e){}
			// 会员卡号查不到会员记录时通过会员手机号查询
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put("mobilePhone", mobilePhone);
			prmMap.putAll(map);
			OrderCounterCodeList = binOLWPCM01_Service.getOrderCounterCode(prmMap);
		}
		return OrderCounterCodeList;
	}
	
	@Override
	public int getMemberCount(Map<String, Object> map) throws Exception {
		// 获取搜索字符串作为会员卡号
		String memberCode = ConvertUtil.getString(map.get("searchStr"));
		// 获取搜索字符串作为手机号
		String mobilePhone = ConvertUtil.getString(map.get("searchStr"));
		// 获取品牌编号
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 根据会员卡号查询会员数量
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberCode", memberCode);
		paramMap.putAll(map);
		int memberCount = binOLWPCM01_Service.getMemberCount(paramMap);
		if(memberCount < 1){
			try{
				if (!CherryChecker.isNullOrEmpty(mobilePhone, true)) {
					mobilePhone = CherrySecret.encryptData(brandCode, mobilePhone);
				}
			}catch(Exception e){}
			// 会员卡号查不到会员记录时通过会员手机号查询
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put("mobilePhone", mobilePhone);
			prmMap.putAll(map);
			memberCount = binOLWPCM01_Service.getMemberCount(prmMap);
		}
		return memberCount;
	}

	@Override
	public List<Map<String, Object>> getMemberList(Map<String, Object> map)
			throws Exception {
		// 获取搜索字符串作为会员卡号
		String memberCode = ConvertUtil.getString(map.get("searchStr"));
		// 获取搜索字符串作为手机号
		String mobilePhone = ConvertUtil.getString(map.get("searchStr"));
		// 获取品牌编号
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 根据会员卡号查询会员数量
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberCode", memberCode);
		paramMap.putAll(map);
		List<Map<String, Object>> memberList = binOLWPCM01_Service.getMemberList(paramMap);
		if(memberList == null || memberList.isEmpty()){
			try{
				if (!CherryChecker.isNullOrEmpty(mobilePhone, true)) {
					mobilePhone = CherrySecret.encryptData(brandCode, mobilePhone);
				}
			}catch(Exception e){}
			// 会员卡号查不到会员记录时通过会员手机号查询
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put("mobilePhone", mobilePhone);
			prmMap.putAll(map);
			memberList = binOLWPCM01_Service.getMemberList(prmMap);
		}
		return memberList;
	}

	@Override
	public Map<String, Object> getMemberInfo(Map<String, Object> map)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 获取搜索字符串作为会员卡号
		String memberCode = ConvertUtil.getString(map.get("searchStr"));
		// 获取搜索字符串作为手机号
		String mobilePhone = ConvertUtil.getString(map.get("searchStr"));
		// 获取品牌编号
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 根据会员卡号查询会员数量
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberCode", memberCode);
		paramMap.putAll(map);
		List<Map<String, Object>> memberList = binOLWPCM01_Service.getMemberInfoList(paramMap);
		if(memberList == null || memberList.isEmpty()){
			try{
				if (!CherryChecker.isNullOrEmpty(mobilePhone, true)) {
					mobilePhone = CherrySecret.encryptData(brandCode, mobilePhone);
				}
			}catch(Exception e){}
			// 会员卡号查不到会员记录时通过会员手机号查询
			Map<String, Object> prmMap = new HashMap<String, Object>();
			prmMap.put("mobilePhone", mobilePhone);
			prmMap.putAll(map);
			memberList = binOLWPCM01_Service.getMemberInfoList(prmMap);
		}
		// 查询到会员列表后返回第一条记录
		if(memberList != null && !memberList.isEmpty()){
			for(Map<String,Object> memberInfo : memberList){
				if(memberInfo != null && !memberList.isEmpty()){
					returnMap.putAll(memberInfo);
					break;
				}
			}
		}
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getMemberLevelInfoList(
			Map<String, Object> map) {
		return binOLWPCM01_Service.getMemberLevelInfoList(map);
	}
	
	@Override
	public List<Map<String, Object>> getCounterBaList(Map<String, Object> map)
			throws Exception {
		return binOLWPCM01_Service.getCounterBaList(map);
	}

	@Override
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map) {
		return binOLWPCM01_Service.getBAInfoList(map);
	}
	
	@Override
	public List<Map<String, Object>> getActiveBAList(Map<String, Object> map) {
		return binOLWPCM01_Service.getActiveBAList(map);
	}
	
	@Override
	public String getBussinessDate(Map<String, Object> map){
		return binOLWPCM01_Service.getBussinessDate(map);
	}
	
	@Override
	public String getSYSDate(){
		return binOLWPCM01_Service.getSYSDate();
	}

}
