/*	
 * @(#)DataTable_BaseForm.java     1.0 2010/10/12		
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
package com.cherry.cm.form;

import java.util.List;

/**
 * dataTable共通form
 * 
 * @author lipc
 * @version 1.0 2010/10/12
 */
@SuppressWarnings("unchecked")
public class DataTable_BaseForm extends BaseForm {

	/** dateTable定义的参数，需要原样返回 */
	private int sEcho;

	/** 数据查询开始位置 */
	private int iDisplayStart;

	/** 数据查询长度 */
	private int iDisplayLength;

	/** 需要排序的列号 */
	private int iSortCol_0;

	/** 所有列的列名 "列1,列2,.." */
	private String sColumns;

	/** 排序方式 */
	private String sSortDir_0;

	/** 具体列的排序(列名 + 排序方式) */
	private String sort;
	
	/** 过滤前总记录数 */
	private int iTotalRecords;
	 
	/** 过滤后总记录数  */
	private int iTotalDisplayRecords;
	 
	/** 过滤查询 */
	private String sSearch;
	
	/** 弹出datatable 促销产品信息 **/
	private List popPrmProductInfoList; 
	
	/** 弹出datatable 产品信息 **/
	private List popProductInfoList;
	
	/** 弹出datatable 产品信息 **/
	private List popProductInfoOneList;
	
	/** 弹出datatable 产品分类信息 **/
	private List popCateInfoList;
	
	/** 弹出datatable 会员信息 **/
	private List popMemberInfoList; 
	
	/** 弹出datatable 对象批次信息 **/
	private List popObjBatchList;

	public int getITotalRecords() {
		return iTotalRecords;
	}

	public void setITotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getITotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setITotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public int getSEcho() {
		return sEcho;
	}

	public void setSEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public int getIDisplayStart() {
		return iDisplayStart;
	}

	public void setIDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getIDisplayLength() {
		return iDisplayLength;
	}

	public void setIDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public int getISortCol_0() {
		return iSortCol_0;
	}

	public void setISortCol_0(int iSortCol_0) {
		this.iSortCol_0 = iSortCol_0;
	}

	public String getSColumns() {
		return sColumns;
	}

	public void setSColumns(String sColumns) {
		this.sColumns = sColumns;
	}

	public String getSSortDir_0() {
		return sSortDir_0;
	}

	public void setSSortDir_0(String sSortDir_0) {
		this.sSortDir_0 = sSortDir_0;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSSearch() {
		return sSearch;
	}

	public void setSSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public List getPopPrmProductInfoList() {
		return popPrmProductInfoList;
	}

	public void setPopPrmProductInfoList(List popPrmProductInfoList) {
		this.popPrmProductInfoList = popPrmProductInfoList;
	}

	public List getPopProductInfoList() {
		return popProductInfoList;
	}

	public void setPopProductInfoList(List popProductInfoList) {
		this.popProductInfoList = popProductInfoList;
	}

	public List getPopCateInfoList() {
		return popCateInfoList;
	}

	public void setPopCateInfoList(List popCateInfoList) {
		this.popCateInfoList = popCateInfoList;
	}
	public List getPopMemberInfoList() {
		return popMemberInfoList;
	}

	public void setPopMemberInfoList(List popMemberInfoList) {
		this.popMemberInfoList = popMemberInfoList;
	}
	
	public List getPopObjBatchList() {
		return popObjBatchList;
	}

	public void setPopObjBatchList(List popObjBatchList) {
		this.popObjBatchList = popObjBatchList;
	}

	public List getPopProductInfoOneList() {
		return popProductInfoOneList;
	}

	public void setPopProductInfoOneList(List popProductInfoOneList) {
		this.popProductInfoOneList = popProductInfoOneList;
	}
	
}
