package com.cherry.synchro.mo.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.mo.interfaces.PosMenuSynchro_IF;
import com.cherry.synchro.mo.service.PosMenuSynchroService;

public class PosMenuSynchro implements PosMenuSynchro_IF {

	@Resource(name="posMenuSynchroService")
	private PosMenuSynchroService posMenuSynchroService;
	@Override
	public void publishPosMenuBrandCounter(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			param.put("ParamXml",getPosMenuCountersXml(param));
			posMenuSynchroService.publishPosMenuBrandCounter(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
		
	}
	
	@Override
	public void delPosMenuBrandCounter(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			param.put("ParamXml",getCountersXml(param));
			posMenuSynchroService.delPosMenuBrandCounter(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
		
	}

	/**
	 * 增加POS菜单信息
	 * 
	 */
	@Override
	public void addPosMenu(Map map) throws CherryException {
		Map param = new HashMap();
		param.putAll(map);
		try {
			param.put("Result","OK");
			param.put("ParamXml", this.getPosMenuXml(param));
			posMenuSynchroService.addPosMenu(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		} catch(CherryException ex) {
			throw ex;
		} catch(Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;
		}
	}

	/**
	 * 增加品牌菜单管理信息
	 * 
	 */
	@Override
	public void addPosMenuBrand(Map map) throws CherryException {
		Map param = new HashMap();
		param.putAll(map);
		try {
			param.put("Result","OK");
			param.put("ParamXml", this.getPosMenuBrandXml(param));
			posMenuSynchroService.addPosMenuBrand(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		} catch(CherryException ex) {
			throw ex;
		} catch(Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;
		}
	}
	
	@Override
	public void updPosMenuBrand(Map param) throws CherryException {
		try {
			param.put("Result","OK");
			param.put("ParamXml", this.getUpdatePosMenuXml(param));
			posMenuSynchroService.updPosMenuBrand(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		} catch(CherryException ex) {
			throw ex;
		} catch(Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;
		}
	}
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getCountersXml(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element nodeRoot = doc.addElement("ParamXml");
			
			// 菜单配置（显示状态）信息节点
			Element mainInfo = nodeRoot.addElement("PosMenuStatusInfo");
			// 品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			// 机器类型
			mainInfo.addAttribute("MachineType", String.valueOf(param.get("machineType")));
			// 开始时间
			mainInfo.addAttribute("StartTime", String.valueOf(param.get("StartTime")));
			// 结束时间
			mainInfo.addAttribute("EndTime", String.valueOf(param.get("EndTime")));
			// 创建时间
			mainInfo.addAttribute("created", String.valueOf(param.get(CherryConstants.CREATE_TIME)));
			// 更新时间
			mainInfo.addAttribute("modified", String.valueOf(param.get(CherryConstants.UPDATE_TIME)));
			
			// 之前配置此菜单配置的柜台
			ArrayList<String> OldCounterList = (ArrayList<String>)param.get("OldCounterList");
			if(OldCounterList!=null && OldCounterList.size()>0){
				Iterator<String> it = OldCounterList.iterator();
				String tempMap;
				Element oldCounterCode ;
				while(it.hasNext()){
					 tempMap = it.next();
					 oldCounterCode = nodeRoot.addElement("OldCounterCode");
					 oldCounterCode.addAttribute("OldCounterCode",tempMap);
				}
			}
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getPosMenuCountersXml(Map param) throws Exception{
		String ret="";
		try{
			// 创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			// 创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			// 菜单配置（显示状态）信息节点
			Element mainInfo = nodeRoot.addElement("PosMenuStatusInfo");
			// 品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			// 菜单CODE 
			String menuCode = String.valueOf(param.get("MenuCode"));
			mainInfo.addAttribute("MenuCode", menuCode);
			// 机器类型【cherry_base_publishPosMenuBrandCounter存储过程有修改】
			mainInfo.addAttribute("MachineType", String.valueOf(param.get("MachineType")));
			// 菜单中文名称
			mainInfo.addAttribute("MenuNameCN", String.valueOf(param.get("MenuNameCN")));
			// 菜单英文名称
			mainInfo.addAttribute("MenuNameEN", String.valueOf(param.get("MenuNameEN")));
			// 生效开始时间
			mainInfo.addAttribute("StartTime", String.valueOf(param.get("StartTime")));
			// 生效结束时间
			mainInfo.addAttribute("EndTime", String.valueOf(param.get("EndTime")));
			// 创建时间
			mainInfo.addAttribute("created", String.valueOf(param.get(CherryConstants.CREATE_TIME)));
			// 更新时间
			mainInfo.addAttribute("modified", String.valueOf(param.get(CherryConstants.UPDATE_TIME)));
			// 菜单显示状态
			String menuStatus = String.valueOf(param.get("MenuStatus"));
			
			mainInfo.addAttribute("MenuStatus", menuStatus);
			
			// 配置此菜单配置的应删除柜台
//			ArrayList<String> OldCounterList = (ArrayList<String>)param.get("OldCounterList");
//			if(OldCounterList!=null && OldCounterList.size()>0){
//				Iterator<String> it = OldCounterList.iterator();
//				String tempMap;
//				Element oldCounterCode ;
//				while(it.hasNext()){
//					 tempMap = it.next();
//					 oldCounterCode = nodeRoot.addElement("OldCounterCode");
//					 oldCounterCode.addAttribute("OldCounterCode",tempMap);
//				}
//			}
			
			// 配置此菜单配置的新增柜台
			ArrayList<String> CounterCodeList =  (ArrayList<String>)param.get("CounterCodeList");
			if(CounterCodeList!=null && CounterCodeList.size()>0){
				Iterator<String> it = CounterCodeList.iterator();
				String tempMap;
				Element counterMenuStatus;
				while(it.hasNext()){
					 tempMap = it.next();
					 counterMenuStatus = nodeRoot.addElement("CounterMenuStatus");
					 counterMenuStatus.addAttribute("MenuCode",menuCode);
					 counterMenuStatus.addAttribute("MenuStatus",menuStatus);
					 counterMenuStatus.addAttribute("CounterCode",tempMap);
				}
			}
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将POS菜单的参数转换成XML格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getPosMenuXml(Map param) throws Exception{
		String ret = "";
		try{
			// 创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			// 创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			// 菜单配置（显示状态）信息节点
			Element mainInfo = nodeRoot.addElement("PosMenuInfo");
			// 菜单CODE 
			mainInfo.addAttribute("MenuCode", String.valueOf(param.get("menuCode")));
			// 菜单类型
			mainInfo.addAttribute("MenuType", String.valueOf(param.get("menuType")));
			// 菜单链接
			mainInfo.addAttribute("MenuLink", ConvertUtil.getString(param.get("menuLink")));
			// 菜单备注
			String comment = CherryChecker.isNullOrEmpty(param.get("comment")) ? ConvertUtil
					.getString(param.get("brandMenuNameCN")) : ConvertUtil
					.getString(param.get("comment"));
			mainInfo.addAttribute("Comment", comment);
			// 是否终结点
			mainInfo.addAttribute("IsLeaf", ConvertUtil.getString(param.get("isLeaf")));
			// 机器类型
			mainInfo.addAttribute("MachineType", ConvertUtil.getString(param.get("machineType")));
			// 菜单ID
			mainInfo.addAttribute("PosMenuID",ConvertUtil.getString(param.get("posMenuID")));
			
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将POS菜单的参数转换成XML格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getPosMenuBrandXml(Map param) throws Exception{
		String ret = "";
		try{
			// 创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			// 创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			// 菜单配置（显示状态）信息节点
			Element mainInfo = nodeRoot.addElement("PosMenuBrandInfo");
			// 菜单CODE 
			mainInfo.addAttribute("MenuCode", String.valueOf(param.get("menuCode")));
			// 品牌代码 
			mainInfo.addAttribute("BrandCode",ConvertUtil.getString(param.get("BrandCode")));
			// 父菜单ID
			mainInfo.addAttribute("ParentMenuID",ConvertUtil.getString(param.get("parentMenuID")));
			// 菜单ID
			mainInfo.addAttribute("PosMenuID",ConvertUtil.getString(param.get("posMenuID")));
			// 菜单中文名
			mainInfo.addAttribute("BrandMenuNameCN",String.valueOf(param.get("brandMenuNameCN")));
			// 菜单英文名
			mainInfo.addAttribute("BrandMenuNameEN",String.valueOf(param.get("brandMenuNameEN")));
			// 菜单容器
			mainInfo.addAttribute("Container",ConvertUtil.getString(param.get("container")));
			// 菜单在同级菜单中的顺序
			mainInfo.addAttribute("MenuOrder",ConvertUtil.getString(param.get("menuOrder")));
			// 菜单显示状态
			mainInfo.addAttribute("MenuStatus",ConvertUtil.getString(param.get("menuStatus")));
			// 菜单属性
			mainInfo.addAttribute("MenuValue",ConvertUtil.getString(param.get("menuValue")));
			
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将更新POS菜单的参数转换为XML格式
	 * @param param
	 * @return
	 */
	private String getUpdatePosMenuXml(Map param) throws Exception {
		String ret = "";
		try{
			// 创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			// 创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			// 菜单配置（显示状态）信息节点
			Element mainInfo = nodeRoot.addElement("PosMenuInfo");
			// 菜单ID
			mainInfo.addAttribute("PosMenuID",ConvertUtil.getString(param.get("posMenuID")));
			// 菜单CODE
			mainInfo.addAttribute("MenuCode",ConvertUtil.getString(param.get("menuCode")));
			// 菜单类型
			mainInfo.addAttribute("MenuType", String.valueOf(param.get("menuType")));
			// 菜单链接
			mainInfo.addAttribute("MenuLink", ConvertUtil.getString(param.get("menuLink")));
			// 菜单备注
			mainInfo.addAttribute("Comment", ConvertUtil.getString(param.get("comment")));
			// 机器类型
			mainInfo.addAttribute("MachineType", ConvertUtil.getString(param.get("machineType")));
			
			// 品牌代码 
			mainInfo.addAttribute("BrandCode",ConvertUtil.getString(param.get("BrandCode")));
			// 菜单中文名
			mainInfo.addAttribute("BrandMenuNameCN",String.valueOf(param.get("brandMenuNameCN")));
			// 菜单英文名
			mainInfo.addAttribute("BrandMenuNameEN",String.valueOf(param.get("brandMenuNameEN")));
			// 菜单容器
			mainInfo.addAttribute("Container",ConvertUtil.getString(param.get("container")));
			// 菜单在同级菜单中的顺序
			mainInfo.addAttribute("MenuOrder",ConvertUtil.getString(param.get("menuOrder")));
			// 菜单显示状态
			mainInfo.addAttribute("MenuStatus",ConvertUtil.getString(param.get("menuStatus")));
			// 菜单属性
			mainInfo.addAttribute("MenuValue",ConvertUtil.getString(param.get("menuValue")));
			
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}

}
