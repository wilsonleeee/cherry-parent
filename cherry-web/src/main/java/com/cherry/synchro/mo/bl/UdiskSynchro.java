
/*  
 * @(#)UdiskSynchro.java    1.0 2011-10-31     
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
package com.cherry.synchro.mo.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.synchro.mo.interfaces.UdiskSynchro_IF;
import com.cherry.synchro.mo.service.UdiskSynchroService;

public class UdiskSynchro implements UdiskSynchro_IF {

	@Resource
	private UdiskSynchroService udiskSynchroService;
	
	@Override
	public void addUdisk(List<Map<String, Object>> paramList) throws Exception{
		// TODO Auto-generated method stub
		try{
			//遍历要添加的U盘
			for(int i = 0 ; i < paramList.size() ; i++){
				Map<String,Object> paramMap = paramList.get(i);
				Map<String,Object> udiskMap = new HashMap<String,Object>();
				//从参数中取得员工ID
				int employeeId = 0;
				String employee = ConvertUtil.getString(paramMap.get("employeeId"));
				
				//申明Map存放与U盘信息表相关的数据
				Map<String,Object> udiskInfo = new HashMap<String,Object>();
				//U盘SN号
				udiskInfo.put("UDiskSN", paramMap.get("udiskSn"));
				//品牌CODE
				udiskInfo.put("BrandCode", paramMap.get("brandCode"));
				//取得员工及其岗位信息
				if(!"".equals(employee)){
					employeeId = Integer.parseInt(employee);
					Map<String,Object> employeeInfo = this.getEmplyeeInfo(employeeId);
					int ownerRight = this.getOwnerRight(employeeInfo);
					//所属者姓名
					udiskInfo.put("OwnerName", employeeInfo.get("EmployeeName"));
					//员工CODE
					udiskInfo.put("employee_code", employeeInfo.get("EmployeeCode"));
					//所属者身份号；能唯一标识该人身份的号码
					udiskInfo.put("OwnerIdentity", employeeInfo.get("EmployeeCode"));
					//所属者权限级别；0为禁用U盘，1为BAS级别，2为BAS上一级别，级别越高数字越大
					udiskInfo.put("OwnerRight", ownerRight);
					List<Map<String,Object>> countersList = this.getCounterCode(employeeId);
					udiskMap.put("countersList", countersList);
				}else{
					//所属者权限级别；0为禁用U盘，1为BAS级别，2为BAS上一级别，级别越高数字越大
					udiskInfo.put("OwnerRight", 0);
				}
				udiskMap.put("udiskInfo", udiskInfo);
				paramMap.put("Result", "OK");
				paramMap.put("UdiskXml", this.getUdiskXmlDom4j(udiskMap));
				udiskSynchroService.addUdisk(paramMap);
				String ret = String.valueOf(paramMap.get("Result"));
				if(!"OK".equals(ret)){
					CherryException cex = new CherryException("ECM00035");
					cex.setErrMessage(cex.getErrMessage()+ret);
					throw cex;
				}
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
		
	}

	@Override
	public void bindUdisk(Map<String, Object> paramMap) throws Exception{
		// TODO Auto-generated method stub
		try{
			Map<String,Object> udiskMap = new HashMap<String,Object>();
			//从参数中取得员工ID
			int employeeId = Integer.parseInt(ConvertUtil.getString(paramMap.get("employeeId")));
			//申明Map存放与U盘信息表相关的数据
			Map<String,Object> udiskInfo = new HashMap<String,Object>();
			//U盘SN号
			udiskInfo.put("UDiskSN", paramMap.get("udiskSn"));
			//品牌CODE
			udiskInfo.put("BrandCode", paramMap.get("brandCode"));
			//取得员工及其岗位信息
			Map<String,Object> employeeInfo = this.getEmplyeeInfo(employeeId);
			int ownerRight = this.getOwnerRight(employeeInfo);
			//所属者姓名
			udiskInfo.put("OwnerName", employeeInfo.get("EmployeeName"));
			//员工CODE
			udiskInfo.put("employee_code", employeeInfo.get("EmployeeCode"));
			//所属者身份号；能唯一标识该人身份的号码
			udiskInfo.put("OwnerIdentity", employeeInfo.get("EmployeeCode"));
			//所属者权限级别；0为禁用U盘，1为BAS级别，2为BAS上一级别，级别越高数字越大
			udiskInfo.put("OwnerRight", ownerRight);
			List<Map<String,Object>> countersList = this.getCounterCode(employeeId);
			udiskMap.put("countersList", countersList);
			udiskMap.put("udiskInfo", udiskInfo);
			paramMap.put("Result", "OK");
			paramMap.put("UdiskXml", this.getUdiskXmlDom4j(udiskMap));
			udiskSynchroService.bindUdisk(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
		
	}

	/**
	 * 根据员工ID获取员工及其所属岗位(类别)信息
	 * 
	 * */
	private Map<String,Object> getEmplyeeInfo(int employeeId){
		return udiskSynchroService.getEmplyeeInfo(employeeId);
	}
	
	/**
	 * 根据员工ID获取该员工所管辖和关注的所有的柜台CODE
	 * 
	 * */
	private List<Map<String,Object>> getCounterCode(int employeeId){
		return udiskSynchroService.getCounterCode(employeeId);
	}
	
	/**
	 * 根据员工岗位(类别)设定U盘权限
	 * 
	 * */
	private int getOwnerRight(Map<String,Object> employeeInfo){
		//查询"柜台主管"对应的岗位级别
		Map<String,Object> basMap = new HashMap<String,Object>();
		basMap.put("BIN_OrganizationInfoID", employeeInfo.get("BIN_OrganizationInfoID"));
		basMap.put("BIN_BrandInfoID", employeeInfo.get("BIN_BrandInfoID"));
		basMap.put("CategoryCode", CherryConstants.CATRGORY_CODE_BAS);
		int basGrade = udiskSynchroService.getPositionGrade(basMap);
		
		//当前员工的岗位级别
		int _thisEmployeeGrade = (Integer)employeeInfo.get("Grade");
		
		//权限设定是以bas为基准,岗位为bas的员工权限级别为1,岗位级别越高权限越大,依次加1
		int ownerRight = (basGrade - _thisEmployeeGrade) + MonitorConstants.UpLoadUdisk_OwnerRight_Bas;
		
		return ownerRight;
	}
	
	/**
	 * 将U盘信息转化成XML格式的字符串
	 * 
	 * */
	@SuppressWarnings("unchecked")
	private String getUdiskXmlDom4j(Map<String,Object> udiskMap) throws Exception{
		String ret = "";
		try{
			Map<String,Object> uDiskMain = (Map<String, Object>) udiskMap.get("udiskInfo");
			List<Map<String,Object>> countersList = (List<Map<String, Object>>) udiskMap.get("countersList");
			
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element rootElement = doc.addElement("UDisk");
			
			//创建UDisk主要信息节点
			Element udiskMain = rootElement.addElement("UDiskMain");
			//U盘SN号
			udiskMain.addAttribute("UDiskSN", String.valueOf(uDiskMain.get("UDiskSN")));
			//品牌code
			udiskMain.addAttribute("BrandCode", String.valueOf(uDiskMain.get("BrandCode")));
			//所属者姓名
			udiskMain.addAttribute("OwnerName", String.valueOf(uDiskMain.get("OwnerName")));
			//所属者身份号；能唯一标识该人身份的号码
			udiskMain.addAttribute("OwnerIdentity", String.valueOf(uDiskMain.get("OwnerIdentity")));
			//所属者权限级别；0为禁用U盘，1为BAS级别，2为BAS上一级别，级别越高数字越大
			udiskMain.addAttribute("OwnerRight", String.valueOf(uDiskMain.get("OwnerRight")));
			//员工CODE
			udiskMain.addAttribute("employee_code", String.valueOf(uDiskMain.get("employee_code")));
			
			//创建柜台code节点
			if(null != countersList){
				for(int i = 0 ; i < countersList.size() ; i++){
					Map<String,Object> tempMap = countersList.get(i);
					Element counter = rootElement.addElement("Counters");
					counter.addAttribute("CounterCode", String.valueOf(tempMap.get("CounterCode")));
					counter.addAttribute("UDiskSN", String.valueOf(uDiskMain.get("UDiskSN")));
				}
			}
			ret = doc.asXML();
		}catch(Exception e){
			throw e;
		}
		return ret;
	}

	@Override
	public void deleteUdisk(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		try{
			paramMap.put("Result","OK");
			udiskSynchroService.unbindUdisk(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
	}

	@Override
	public void unbindUdisk(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		try{
			paramMap.put("Result","OK");
			udiskSynchroService.unbindUdisk(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		} catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}	
	}
}
