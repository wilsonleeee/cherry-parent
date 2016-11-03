package com.jahwa.pos.crm;

public class Si_PmsCpStaTran_obProxy implements com.jahwa.pos.crm.Si_PmsCpStaTran_ob {
  private String _endpoint = null;
  private com.jahwa.pos.crm.Si_PmsCpStaTran_ob si_PmsCpStaTran_ob = null;
  
  public Si_PmsCpStaTran_obProxy() {
    _initSi_PmsCpStaTran_obProxy();
  }
  
  public Si_PmsCpStaTran_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_PmsCpStaTran_obProxy();
  }
  
  private void _initSi_PmsCpStaTran_obProxy() {
    try {
      si_PmsCpStaTran_ob = (new com.jahwa.pos.crm.Si_PmsCpStaTran_obServiceLocator()).getsi_PmsCpStaTran_obPort();
      if (si_PmsCpStaTran_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_PmsCpStaTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_PmsCpStaTran_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_PmsCpStaTran_ob != null)
      ((javax.xml.rpc.Stub)si_PmsCpStaTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.crm.Si_PmsCpStaTran_ob getSi_PmsCpStaTran_ob() {
    if (si_PmsCpStaTran_ob == null)
      _initSi_PmsCpStaTran_obProxy();
    return si_PmsCpStaTran_ob;
  }
  
  public com.jahwa.pos.crm.Dt_PmsCpStaTran_res si_PmsCpStaTran_ob(com.jahwa.pos.crm.Dt_PmsCpStaTran_req mt_PmsCpStaTran_req) throws java.rmi.RemoteException{
    if (si_PmsCpStaTran_ob == null)
      _initSi_PmsCpStaTran_obProxy();
    return si_PmsCpStaTran_ob.si_PmsCpStaTran_ob(mt_PmsCpStaTran_req);
  }
  
  
}