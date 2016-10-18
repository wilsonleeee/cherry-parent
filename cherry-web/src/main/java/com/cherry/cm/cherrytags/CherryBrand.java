package com.cherry.cm.cherrytags;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryConstants;

public class CherryBrand extends TagSupport {
	
	private static final long serialVersionUID = 4709834631596397116L;
	
	private String id;
	private String name;
	private String cssclass;
	private String onchange;
	private boolean headeroption = true;
	private final static String[] title = {"品牌","品牌"};
	private final static String[] headerName = {"请选择","請選擇"};
	
	public int doStartTag() throws JspTagException {
		
		HttpSession session = pageContext.getSession();
		if(session != null) {
			UserInfo userInfo = (UserInfo)session.getAttribute(CherryConstants.SESSION_USERINFO);
			if(userInfo != null) {
				String language = (String)session.getAttribute(CherryConstants.SESSION_CHERRY_LANGUAGE);
				int index = 0;
				if(language != null && "zh_TW".equals(language)) {
					index = 1;
				}
				ServletContext servletContext = pageContext.getServletContext(); 
				WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext); 
				/* 从上下文中获取指定的Bean */ 
				BINOLCM05_BL binOLCM05_BL = (BINOLCM05_BL)wac.getBean("binOLCM05_BL");
				
				List<Map<String, Object>> brandInfoList = binOLCM05_BL.getBrandInfoList(userInfo);
				if(brandInfoList != null && !brandInfoList.isEmpty()) {
					JspWriter out = pageContext.getOut();
					try {
						out.print("<div class=\"commonBrand\"><strong><span class=\"ui-icon icon-ttl-section-search-result\"></span><span>"+title[index]+"</span></strong>");
						out.print("<span>");
						out.print("<select");
						if(id != null && !"".equals(id)) {
							out.print(" id='"+id+"'");
						} else {
							out.print(" id='commonBrandId'");
						}
						if(name != null && !"".equals(name)) {
							out.print(" name='"+name+"'");
						} else {
							out.print(" name='commonBrandId'");
						}
						if(cssclass != null && !"".equals(cssclass)) {
							out.print(" class='"+cssclass+"'");
						}
						if(onchange != null && !"".equals(onchange)) {
							out.print(" onchange='"+onchange+"'");
						}
						out.print(" >");
						if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
							if(headeroption) {
								out.print("<option value=\"\">"+headerName[index]+"</option>");
							}
						}
						String commonBrandId = (String)pageContext.getRequest().getAttribute("commonBrandId");
						for(int i = 0; i < brandInfoList.size(); i++) {
							Map<String, Object> brandInfoMap = brandInfoList.get(i);
							String brandId = String.valueOf(brandInfoMap.get("BIN_BrandInfoID"));
							String brandName = (String)brandInfoMap.get("BrandName");
							if(commonBrandId != null && commonBrandId.equals(brandId)) {
								out.print("<option value=\""+brandId+"\" selected>"+brandName+"</option>");
							} else {
								out.print("<option value=\""+brandId+"\">"+brandName+"</option>");
							}
						}
						out.print("</select>");
						out.print("</span>");
						out.print("</div>");
					} catch (IOException e) {
						throw new JspTagException("error occurd when create CherryBrand tag");
					}
				}
			}
		}
	    return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspTagException {
		
	    return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCssclass() {
		return cssclass;
	}

	public void setCssclass(String cssclass) {
		this.cssclass = cssclass;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public boolean isHeaderoption() {
		return headeroption;
	}

	public void setHeaderoption(boolean headeroption) {
		this.headeroption = headeroption;
	}

}
