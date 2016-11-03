package com.jahwa.pos.ecc;

public class Si_Material_obProxy implements com.jahwa.pos.ecc.Si_Material_ob {
  private String _endpoint = null;
  private com.jahwa.pos.ecc.Si_Material_ob si_Material_ob = null;
  
  public Si_Material_obProxy() {
    _initSi_Material_obProxy();
  }
  
  public Si_Material_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_Material_obProxy();
  }
  
  private void _initSi_Material_obProxy() {
    try {
      si_Material_ob = (new com.jahwa.pos.ecc.Si_Material_obServiceLocator()).getsi_Material_obPort();
      if (si_Material_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_Material_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_Material_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_Material_ob != null)
      ((javax.xml.rpc.Stub)si_Material_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.ecc.Si_Material_ob getSi_Material_ob() {
    if (si_Material_ob == null)
      _initSi_Material_obProxy();
    return si_Material_ob;
  }
  
  public com.jahwa.pos.ecc.Dt_Material_res si_Material_ob(com.jahwa.pos.ecc.Dt_Material_req mt_Material_req) throws java.rmi.RemoteException{
    if (si_Material_ob == null)
      _initSi_Material_obProxy();
    return si_Material_ob.si_Material_ob(mt_Material_req);
  }
  
  
}