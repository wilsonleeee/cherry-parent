package com.jahwa.pos.crm;

public class Si_PmsCpListTran_obProxy implements com.jahwa.pos.crm.Si_PmsCpListTran_ob {
  private String _endpoint = null;
  private com.jahwa.pos.crm.Si_PmsCpListTran_ob si_PmsCpListTran_ob = null;
  
  public Si_PmsCpListTran_obProxy() {
    _initSi_PmsCpListTran_obProxy();
  }
  
  public Si_PmsCpListTran_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_PmsCpListTran_obProxy();
  }
  
  private void _initSi_PmsCpListTran_obProxy() {
    try {
      si_PmsCpListTran_ob = (new com.jahwa.pos.crm.Si_PmsCpListTran_obServiceLocator()).getsi_PmsCpListTran_obPort();
      if (si_PmsCpListTran_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_PmsCpListTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_PmsCpListTran_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_PmsCpListTran_ob != null)
      ((javax.xml.rpc.Stub)si_PmsCpListTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.crm.Si_PmsCpListTran_ob getSi_PmsCpListTran_ob() {
    if (si_PmsCpListTran_ob == null)
      _initSi_PmsCpListTran_obProxy();
    return si_PmsCpListTran_ob;
  }
  
  public com.jahwa.pos.crm.Dt_PmsCpListTran_res si_PmsCpListTran_ob(com.jahwa.pos.crm.Dt_PmsCpListTran_req mt_PmsCpListTran_req) throws java.rmi.RemoteException{
    if (si_PmsCpListTran_ob == null)
      _initSi_PmsCpListTran_obProxy();
    return si_PmsCpListTran_ob.si_PmsCpListTran_ob(mt_PmsCpListTran_req);
  }
  
  
}