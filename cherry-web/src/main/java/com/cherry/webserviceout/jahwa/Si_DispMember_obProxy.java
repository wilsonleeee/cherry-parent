package com.cherry.webserviceout.jahwa;

public class Si_DispMember_obProxy implements com.cherry.webserviceout.jahwa.Si_DispMember_ob {
  private String _endpoint = null;
  private com.cherry.webserviceout.jahwa.Si_DispMember_ob si_DispMember_ob = null;
  
  public Si_DispMember_obProxy() {
    _initSi_DispMember_obProxy();
  }
  
  public Si_DispMember_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_DispMember_obProxy();
  }
  
  private void _initSi_DispMember_obProxy() {
    try {
      si_DispMember_ob = (new com.cherry.webserviceout.jahwa.Si_DispMember_obServiceLocator()).getsi_DispMember_obPort();
      if (si_DispMember_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_DispMember_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_DispMember_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_DispMember_ob != null)
      ((javax.xml.rpc.Stub)si_DispMember_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.cherry.webserviceout.jahwa.Si_DispMember_ob getSi_DispMember_ob() {
    if (si_DispMember_ob == null)
      _initSi_DispMember_obProxy();
    return si_DispMember_ob;
  }
  
  public com.cherry.webserviceout.jahwa.Dt_DispMember_res si_DispMember_ob(com.cherry.webserviceout.jahwa.Dt_DispMember_req mt_DispMember_req) throws java.rmi.RemoteException{
    if (si_DispMember_ob == null)
      _initSi_DispMember_obProxy();
    return si_DispMember_ob.si_DispMember_ob(mt_DispMember_req);
  }
  
  
}