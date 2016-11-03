package com.cherry.soap.searchAgentCoupon;

public class SearchAgentCouponPortTypeProxy implements com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType {
  private String _endpoint = null;
  private com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType searchAgentCouponPortType = null;
  
  public SearchAgentCouponPortTypeProxy() {
    _initSearchAgentCouponPortTypeProxy();
  }
  
  public SearchAgentCouponPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initSearchAgentCouponPortTypeProxy();
  }
  
  private void _initSearchAgentCouponPortTypeProxy() {
    try {
      searchAgentCouponPortType = (new com.cherry.soap.searchAgentCoupon.SearchAgentCouponLocator()).getsearchAgentCouponPort();
      if (searchAgentCouponPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)searchAgentCouponPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)searchAgentCouponPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (searchAgentCouponPortType != null)
      ((javax.xml.rpc.Stub)searchAgentCouponPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.cherry.soap.searchAgentCoupon.SearchAgentCouponPortType getSearchAgentCouponPortType() {
    if (searchAgentCouponPortType == null)
      _initSearchAgentCouponPortTypeProxy();
    return searchAgentCouponPortType;
  }
  
  public void searchAgentCoupon(java.lang.String start_time, java.lang.String end_time, int page_no, int page_size, javax.xml.rpc.holders.BooleanHolder has_next, javax.xml.rpc.holders.StringHolder coupons, javax.xml.rpc.holders.IntHolder total_results) throws java.rmi.RemoteException{
    if (searchAgentCouponPortType == null)
      _initSearchAgentCouponPortTypeProxy();
    searchAgentCouponPortType.searchAgentCoupon(start_time, end_time, page_no, page_size, has_next, coupons, total_results);
  }
  
  
}