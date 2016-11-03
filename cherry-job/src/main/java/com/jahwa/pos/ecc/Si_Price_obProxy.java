package com.jahwa.pos.ecc;

public class Si_Price_obProxy implements com.jahwa.pos.ecc.Si_Price_ob {
  private String _endpoint = null;
  private com.jahwa.pos.ecc.Si_Price_ob si_Price_ob = null;
  
  public Si_Price_obProxy() {
    _initSi_Price_obProxy();
  }
  
  public Si_Price_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_Price_obProxy();
  }
  
  private void _initSi_Price_obProxy() {
    try {
      si_Price_ob = (new com.jahwa.pos.ecc.Si_Price_obServiceLocator()).getsi_Price_obPort();
      if (si_Price_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_Price_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_Price_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_Price_ob != null)
      ((javax.xml.rpc.Stub)si_Price_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.ecc.Si_Price_ob getSi_Price_ob() {
    if (si_Price_ob == null)
      _initSi_Price_obProxy();
    return si_Price_ob;
  }
  
  public com.jahwa.pos.ecc.Dt_Price_res si_Price_ob(com.jahwa.pos.ecc.Dt_Price_req mt_Price_req) throws java.rmi.RemoteException{
    if (si_Price_ob == null)
      _initSi_Price_obProxy();
    return si_Price_ob.si_Price_ob(mt_Price_req);
  }
  
  
}