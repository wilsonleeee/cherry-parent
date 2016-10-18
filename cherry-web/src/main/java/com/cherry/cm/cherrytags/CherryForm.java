/*  
 * @(#)CherryForm.java     1.0 2011/05/31      
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
package com.cherry.cm.cherrytags;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CherryForm extends TagSupport{
	
	private String id;
	private String name;
	private String action;
	private String target;
	private String method = "post";
	private String enctype;
	private String cssclass;
	private String style;
	private String csrftoken ="true";
	private String submittoken ="true";
	private String onsubmit;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspTagException{

		JspWriter out = pageContext.getOut();
		try {
			out.print("<form ");
			if(id!=null && !"".equals(id)){
				out.print(" id='"+id+"' ");
			}
			if(name!=null && !"".equals(name)){
				out.print(" name='"+name+"' ");
			}
			if(action!=null && !"".equals(action)){
				out.print(" action='"+action+"' ");
			}
			if(target!=null && !"".equals(target)){
				out.print(" target='"+target+"' ");
			}
			if(method!=null && !"".equals(method)){
				out.print(" method='"+method+"' ");
			}
			if(enctype!=null && !"".equals(enctype)){
				out.print(" enctype='"+enctype+"' ");
			}
			if(cssclass!=null && !"".equals(cssclass)){
				out.print(" class='"+cssclass+"' ");
			}
			if(style!=null && !"".equals(style)){
				out.print(" style='"+style+"' ");
			}
			if(csrftoken!=null && !"".equals(csrftoken)){
				out.print(" csrftoken='"+csrftoken+"' ");
			}
			if(submittoken!=null && !"".equals(submittoken)){
				out.print(" submittoken='"+submittoken+"' ");
			}
			if(onsubmit!=null && !"".equals(onsubmit)){
				out.print(" onsubmit='"+onsubmit+"' ");
			}
			out.print(">");	
			
			out.println("");
			if(!"false".equals(csrftoken)){

				String temp = UUID.randomUUID().toString().replace("-", "");
//				if(null!=pageContext.getSession().getAttribute("hiscsrftoken")){
//					//历史token
//					String hisToken = pageContext.getSession().getAttribute("hiscsrftoken").toString();
//					if(hisToken.split(",").length<5){
//						pageContext.getSession().setAttribute("hiscsrftoken",hisToken+temp+",");
//					}else{
//						pageContext.getSession().setAttribute("hiscsrftoken",hisToken.substring(hisToken.indexOf(",")+1)+temp+",");
//					}
//				}else{
//					pageContext.getSession().setAttribute("hiscsrftoken",temp+",");
//				}
				pageContext.getSession().setAttribute("csrftoken",temp);
			out.println("<input type='hidden' id='csrftoken' name='csrftoken' value='"+temp+"'>");
			}
			if(!"false".equals(submittoken)){
				String temp1 = UUID.randomUUID().toString().replace("-", "");
				pageContext.getSession().setAttribute("submittoken",temp1);
				out.println("<input type='hidden' id='submittoken' name='submittoken' value='"+temp1+"'>");
			}
			
			//out.println("<input type='hidden' id='pageIdHidden' name='pageIdHidden' value='"+String.valueOf(pageContext.getSession().getAttribute("pageId"))+"'>");
			out.println("<input type='hidden' id='prePageIdHidden' name='prePageIdHidden' value='"+String.valueOf(pageContext.getSession().getAttribute("prePageId"))+"'>");
			//out.println("<input type='hidden' id='unitIdHidden' name='unitIdHidden' value='"+String.valueOf(pageContext.getSession().getAttribute("unitId"))+"'>");
			out.println("<input type='hidden' id='preUnitIdHidden' name='preUnitIdHidden' value='"+String.valueOf(pageContext.getSession().getAttribute("preUnitId"))+"'>");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new JspTagException("error occurd when create CherryForm tag");
		}
	    return EVAL_BODY_INCLUDE;

	  }

	  public int doEndTag() throws JspTagException{

	    try {	    
	      pageContext.getOut().println("</form>");
	    }

	    catch (Exception ex) {

	      throw new JspTagException("error occurd when create CherryForm tag");

	    }

	    return EVAL_PAGE;

	  }

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(String action) {
			this.action = action;
		}

		/**
		 * @param target the target to set
		 */
		public void setTarget(String target) {
			this.target = target;
		}

		/**
		 * @param method the method to set
		 */
		public void setMethod(String method) {
			this.method = method;
		}

		/**
		 * @param enctype the enctype to set
		 */
		public void setEnctype(String enctype) {
			this.enctype = enctype;
		}

		/**
		 * @param cssclass the cssclass to set
		 */
		public void setCssclass(String cssclass) {
			this.cssclass = cssclass;
		}

		/**
		 * @param style the style to set
		 */
		public void setStyle(String style) {
			this.style = style;
		}
		/**
		 * @return the csrftoken
		 */
		public String getCsrftoken() {
			return csrftoken;
		}

		/**
		 * @param csrftoken the csrftoken to set
		 */
		public void setCsrftoken(String csrftoken) {
			this.csrftoken = csrftoken;
		}

		/**
		 * @return the submittoken
		 */
		public String getSubmittoken() {
			return submittoken;
		}

		/**
		 * @param submittoken the submittoken to set
		 */
		public void setSubmittoken(String submittoken) {
			this.submittoken = submittoken;
		}

		/**
		 * @return the onsubmit
		 */
		public String getOnsubmit() {
			return onsubmit;
		}

		/**
		 * @param onsubmit the onsubmit to set
		 */
		public void setOnsubmit(String onsubmit) {
			this.onsubmit = onsubmit;
		}
}
