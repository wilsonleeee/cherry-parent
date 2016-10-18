/*  
 * @(#)BINOLMOMAN05_BL.java    1.0 2011-7-29     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
 
package com.cherry.mo.man.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.man.interfaces.BINOLMOMAN05_IF;
import com.cherry.mo.man.service.BINOLMOMAN05_Service;
import com.cherry.synchro.mo.interfaces.UdiskSynchro_IF;
import com.cherry.synchro.mo.service.UdiskSynchroService;

public class BINOLMOMAN05_BL implements BINOLMOMAN05_IF {
	
	@Resource
    private BINOLMOMAN05_Service binOLMOMAN05_Service;
	@Resource
	private UdiskSynchro_IF udiskSynchro;
	@Resource
	private UdiskSynchroService udiskSynchroService;
	
	/**
	 * 将excel中的内容解析并加数据添加到数据库
	 * @param file excel文件
	 * @param map 存放的是一些用户操作信息
	 * 
	 * */
	@Override
	public void tran_importUdisk(File file,Map<String,Object> map) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> brandInfoList = null;
		String createBy = ConvertUtil.getString(map.get("createdBy"));
		String createPGM = ConvertUtil.getString(map.get("createPGM"));
		String updatedBy = ConvertUtil.getString(map.get("updatedBy"));
		String updatePGM = ConvertUtil.getString(map.get("updatePGM"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		int organizationInfoId = Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))) ;
		//判断登录账号的所属组织，如果是组织账号获取该账号下的所有的品牌信息
		if(brandInfoId == "-9999"){
			brandInfoList = binOLMOMAN05_Service.getBrandInfoList(map);
		}
		Workbook wb = null;
		if (file == null || !file.exists()) {
			// 上传文件不存在
			throw new CherryException("EBS00042");
		}
		try {
			// 防止GC内存回收的设置
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setGCDisabled(true);
			wb = Workbook.getWorkbook(file, workbookSettings);
		} catch (Exception e) {
			throw new CherryException("EBS00041");
		}
		if (null == wb) {
			throw new CherryException("EBS00041");
		}
		// 获取sheet
		Sheet[] sheets = wb.getSheets();
		// U盘信息sheet
		Sheet dateSheet = null;
		for (Sheet st : sheets) {
			if (MonitorConstants.UpLoadUdisk_Sheet_Name.equals(st.getName().trim())) {
				dateSheet = st;
			}
		}
		// U盘信息sheet不存在
		if (null == dateSheet) {
			throw new CherryException("EBS00030",
					new String[] { MonitorConstants.UpLoadUdisk_Sheet_Name });
		}
		//申明一个String类型的数组，用于存放解析出的UdiskSN，作用是比较excel文件中有没有重复的编号和判断该文件是否为空
		String[] tem_udisk = new String[100000];
		for(int r = 1 ; r < dateSheet.getRows() ; r ++){
			Map<String,Object> mapOfSheet = new HashMap<String,Object>();
			mapOfSheet.put("createdBy", createBy);
			mapOfSheet.put("createPGM", createPGM);
			mapOfSheet.put("updatedBy", updatedBy);
			mapOfSheet.put("updatePGM", updatePGM);
			mapOfSheet.put("organizationInfoId", organizationInfoId);
			//获取品牌Code
			String brandCode = dateSheet.getCell(0, r).getContents().trim();
			//获取U盘序列号
			String udiskSn = dateSheet.getCell(1, r).getContents().trim();
			//获取员工代号
			String employeeCode = dateSheet.getCell(2,r).getContents().trim();
			//获取员工名称
			String employeeName = dateSheet.getCell(3,r).getContents().trim();
			//获取员工岗位
			String position = dateSheet.getCell(4,r).getContents().trim();
			//判断品牌代码是否为空
			if("".equals(brandCode) || brandCode == null){
				//如果该行的所有数据都为空就表明读取结束，跳出循环；否则抛出输入的品牌代码为空异常。
				if(("".equals(udiskSn) || null == udiskSn)&&("".equals(employeeCode) || null == employeeCode)&&("".equals(brandCode) || null == brandCode)&&("".equals(employeeName) || null == employeeName)&&("".equals(position) || null == position)){
					break;
				}else{
					throw new CherryException("EBS00031", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "A" + (r + 1) });
				}
			}
			int num = 0;
			//如果登录的账号为组织账号，那么可以导入该组织下的所有品牌的U盘
			if(brandInfoId == "-9999"){
				//遍历该组织下的所有的品牌CODE，用于验证该行数据中的品牌CODE是否属于该组织
				for(int i = 0 ; i < brandInfoList.size() ; i++){
					if(ConvertUtil.getString(brandInfoList.get(i).get("brandCode")).equals(brandCode)){
						mapOfSheet.put("brandInfoId", brandInfoList.get(i).get("brandInfoId"));
						num ++;
						break;
					}
				}
				//如果验证的结果是不匹配，则抛出品牌代码输入错误异常
				if(num ==0){
					throw new CherryException("EMO00040", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "A" + (r + 1) });
				}
				num = 0;
			}else{
				//如果该登录账号属于品牌账号，那么只要检验一下输入的品牌CODE是否正确即可
				String brandId = binOLMOMAN05_Service.isBrandExist(brandCode);
				//如果验证的结果是不匹配，则抛出品牌代码输入错误异常
				if("".equals(brandId) || brandId == null){
					throw new CherryException("EMO00040", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "A" + (r + 1) });
				}else{
					mapOfSheet.put("brandInfoId", Integer.parseInt(brandId));
				}
			}
			//判断该行U盘序号是否为空
			if("".equals(udiskSn) || null == udiskSn){
				//如果员工代号也为空则认为读取结束，跳出循环，否则抛出异常
				if(("".equals(employeeCode) || null == employeeCode)&&("".equals(brandCode) || null == brandCode)&&("".equals(employeeName) || null == employeeName)&&("".equals(position) || null == position)){
					break;
				}else{
					throw new CherryException("EBS00031", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "B" + (r + 1) });
				}
			}else{
				//验证UdiskSN是否合法，否则抛出异常
				if(!vailString(udiskSn)){
					throw new CherryException("EMO00050", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "B" + (r + 1) });
				}
				//检验是否有重复的UdiskSN
				for(int i = 0 ; i < r; i++){
					if(udiskSn.equals(tem_udisk[i])){
						throw new CherryException("EMO00048", new String[] {
								MonitorConstants.UpLoadUdisk_Sheet_Name, "B" + (i + 2),"B" + (r + 1) });
					}
				}
				//判断该UdiskSN是否已经存在于数据库中
				String isExist = null;
				mapOfSheet.put("udiskSn", udiskSn);
				isExist = binOLMOMAN05_Service.isUdiskExist(mapOfSheet);
				if(isExist != null && !"".equals(isExist)){
					throw new CherryException("EMO00041", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "B" + (r + 1) });
				}
				int r1 = r-1;
				tem_udisk[r1]=udiskSn;
			}
			
			if(employeeCode != null && !"".equals(employeeCode)){
				mapOfSheet.put("employeeCode", employeeCode);
				Map<String,Object> map1 = new HashMap<String,Object>();
				map1 = binOLMOMAN05_Service.isEmployeeExist(mapOfSheet);
			//	emCode = ConvertUtil.getString(map1.get("employeeCode"));
				if(null==map1||map1.isEmpty()){
					throw new CherryException("EMO00040", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "C" + (r + 1) });
				}else{
					//根据岗位决定U盘权限
					int ownerRight = this.getOwnerRight(map1);
					
					if(ownerRight < 1){
						throw new CherryException("EMO00046", new String[] {
								MonitorConstants.UpLoadUdisk_Sheet_Name, "C" + (r + 1) });
					}
					mapOfSheet.put("employeeId", map1.get("employeeId"));
					mapOfSheet.put("ownerRight", ownerRight);
				}
			}else{
				//如果员工CODE为空，则岗位名称也必须为空
				if(position != null && !"".equals(position)){
					throw new CherryException("EBS00031", new String[] {
							MonitorConstants.UpLoadUdisk_Sheet_Name, "C" + (r + 1) });
				}
				//如果没有输入员工CODE，则设置U盘权限为0
				mapOfSheet.put("ownerRight", MonitorConstants.UpLoadUdisk_OwnerRight_Disable);
			}
			list.add(mapOfSheet);
		}
		//判断该excel文件是否为空
		if("".equals(tem_udisk[0]) || null == tem_udisk[0]){
			throw new CherryException("EMO00049");
		}
		binOLMOMAN05_Service.insertUdiskInfo(list);
		
		//调用接口下发到老后台
		List<Map<String,Object>> synchroList = new ArrayList<Map<String,Object>>();
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			Map<String,Object> synchroMap = binOLMOMAN05_Service.getUdiskInfo(tempMap);
			synchroList.add(synchroMap);
		}
		udiskSynchro.addUdisk(synchroList);
	}

	/**
	 * @param udiskSnArr        存放UdiskSN的数组
	 * @param employeeIdArr		存放员工ID的数组
	 * @param gradeArr	                     存放岗位级别的数组
	 * @param map				存放用户操作信息
	 * 
	 * */
	@Override
	public void tran_addUdisk(String[] udiskSnArr,String[] employeeIdArr,String[] gradeArr,Map<String,Object> map) throws Exception {
		//验证UdiskSN是否为空
		if(udiskSnArr.length <= 0){
			throw new CherryException("EMO00024",new String[]{ MonitorConstants.ADDUDISK_UDISKSN__CHINESE });
		}else{
			//查询"柜台主管"对应的岗位级别
			Map<String,Object> basMap = new HashMap<String,Object>();
			basMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
			basMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
			basMap.put("CategoryCode", CherryConstants.CATRGORY_CODE_BAS);
			int basGrade = udiskSynchroService.getPositionGrade(basMap);
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			//遍历udiskSnArr数组，并且将个参数中传递过来的数组数据合并
			for(int index = 0 ;index < udiskSnArr.length ; index++){
				Map<String,Object> map1 = new HashMap<String,Object>();
				map1.putAll(map);
				String udiskSn = udiskSnArr[index];
				map1.put("udiskSn", udiskSn);
				if(null != employeeIdArr[index] && !"".equals(employeeIdArr[index])){
					int employeeId = Integer.parseInt(employeeIdArr[index]);
					map1.put("employeeId", employeeId);
					
					int _thisGrade = Integer.parseInt(gradeArr[index]);
					//权限设定是以bas为基准,岗位为bas的员工权限级别为1,岗位级别越高权限越大,依次加1
					int owerRight = (basGrade - _thisGrade) + MonitorConstants.UpLoadUdisk_OwnerRight_Bas;
					
					map1.put("ownerRight", owerRight);
					
				}else{
					//如果没有员工CODE，则设置U盘权限为0
					map1.put("ownerRight", 0);
				}
				list.add(map1);
			}
			binOLMOMAN05_Service.insertUdiskInfo(list);
			
			//调用接口下发到老后台
			List<Map<String,Object>> synchroList = new ArrayList<Map<String,Object>>();
			for(int i = 0 ; i < list.size() ; i++){
				Map<String,Object> tempMap = list.get(i);
				Map<String,Object> synchroMap = binOLMOMAN05_Service.getUdiskInfo(tempMap);
				synchroList.add(synchroMap);
			}
			udiskSynchro.addUdisk(synchroList);
		}
	}
	
	/**
	 * @param map 存放的是一些查询信息，包括UDISKSN、员工CODE、岗位名称
	 * @return map 返回查询结果，保存UDISKSN、员工CODE、员工ID、员工名称、岗位名称等
	 * 
	 * 
	 * */
	public Map<String,Object> getInformation(Map<String,Object> map)throws Exception{
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//判断输入的UdiskSN是否为空
		if(null == ConvertUtil.getString(map.get("udiskSn")) || "".equals(map.get("udiskSn"))){
			throw new CherryException("EMO00044");
		}
		//验证输入的UdiskSN是否合法
		if(!vailString(ConvertUtil.getString(map.get("udiskSn")))){
			throw new CherryException("EMO00056");
		}
		//检验输入的UdiskSN是否已经存在于数据库中
		String udisk = null;
		udisk = binOLMOMAN05_Service.isUdiskExist(map);
		if(null!=udisk && !"".equals(udisk)){
			throw new CherryException("EMO00042");
		}
		String employeeCode = ConvertUtil.getString(map.get("employeeCode"));
		if(null != employeeCode && !"".equals(employeeCode)){
			//验证输入的员工CODE是否有效
			returnMap = binOLMOMAN05_Service.isEmployeeExist(map);
			if(null==returnMap||returnMap.isEmpty()){
				throw new CherryException("EMO00043");
			}
		}
		returnMap.put("udiskSn", map.get("udiskSn"));
		return returnMap;
	}
	
	/**
	 * 验证输入的U盘号是不是字母和数字，并且最大长度不能超过100
	 * 
	 * 
	 * */
	public boolean vailString(String str){
		String check = "^\\w{1,100}$";
		Pattern regex  = Pattern.compile(check);
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 根据员工岗位(类别)设定U盘权限
	 * 
	 * */
	private int getOwnerRight(Map<String,Object> employeeInfo){
		//查询"柜台主管"对应的岗位级别
		Map<String,Object> basMap = new HashMap<String,Object>();
		basMap.put("BIN_OrganizationInfoID", employeeInfo.get("organizationInfoId"));
		basMap.put("BIN_BrandInfoID", employeeInfo.get("brandInfoId"));
		basMap.put("CategoryCode", CherryConstants.CATRGORY_CODE_BAS);
		int basGrade = udiskSynchroService.getPositionGrade(basMap);
		
		//当前员工的岗位级别
		int _thisEmployeeGrade = (Integer)employeeInfo.get("grade");
		
		//权限设定是以bas为基准,岗位为bas的员工权限级别为1,岗位级别越高权限越大,依次加1
		int ownerRight = (basGrade - _thisEmployeeGrade) + MonitorConstants.UpLoadUdisk_OwnerRight_Bas;
		
		return ownerRight;
	}
	
}
