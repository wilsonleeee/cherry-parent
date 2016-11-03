package com.jahwa.pos.ecc;

public class Si_GetMd_obProxy implements com.jahwa.pos.ecc.Si_GetMd_ob {
  private String _endpoint = null;
  private com.jahwa.pos.ecc.Si_GetMd_ob si_GetMd_ob = null;
  
  public Si_GetMd_obProxy() {
    _initSi_GetMd_obProxy();
  }
  
  public Si_GetMd_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_GetMd_obProxy();
  }
  
  private void _initSi_GetMd_obProxy() {
    try {
      si_GetMd_ob = (new com.jahwa.pos.ecc.Si_GetMd_obServiceLocator()).getsi_GetMd_obPort();
      if (si_GetMd_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_GetMd_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_GetMd_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_GetMd_ob != null)
      ((javax.xml.rpc.Stub)si_GetMd_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.ecc.Si_GetMd_ob getSi_GetMd_ob() {
    if (si_GetMd_ob == null)
      _initSi_GetMd_obProxy();
    return si_GetMd_ob;
  }
  
  public com.jahwa.pos.ecc.Dt_GetMd_res si_GetMd_ob(com.jahwa.pos.ecc.Dt_GetMd_req mt_GetMd_req) throws java.rmi.RemoteException{
    if (si_GetMd_ob == null)
      _initSi_GetMd_obProxy();
    return si_GetMd_ob.si_GetMd_ob(mt_GetMd_req);
  }
  
  
}