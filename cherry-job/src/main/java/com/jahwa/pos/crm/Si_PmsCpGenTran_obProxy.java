package com.jahwa.pos.crm;

public class Si_PmsCpGenTran_obProxy implements com.jahwa.pos.crm.Si_PmsCpGenTran_ob {
  private String _endpoint = null;
  private com.jahwa.pos.crm.Si_PmsCpGenTran_ob si_PmsCpGenTran_ob = null;
  
  public Si_PmsCpGenTran_obProxy() {
    _initSi_PmsCpGenTran_obProxy();
  }
  
  public Si_PmsCpGenTran_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_PmsCpGenTran_obProxy();
  }
  
  private void _initSi_PmsCpGenTran_obProxy() {
    try {
      si_PmsCpGenTran_ob = (new com.jahwa.pos.crm.Si_PmsCpGenTran_obServiceLocator()).getsi_PmsCpGenTran_obPort();
      if (si_PmsCpGenTran_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_PmsCpGenTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_PmsCpGenTran_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_PmsCpGenTran_ob != null)
      ((javax.xml.rpc.Stub)si_PmsCpGenTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.crm.Si_PmsCpGenTran_ob getSi_PmsCpGenTran_ob() {
    if (si_PmsCpGenTran_ob == null)
      _initSi_PmsCpGenTran_obProxy();
    return si_PmsCpGenTran_ob;
  }
  
  public com.jahwa.pos.crm.Dt_PmsCpGenTran_res si_PmsCpGenTran_ob(com.jahwa.pos.crm.Dt_PmsCpGenTran_req mt_PmsCpGenTran_req) throws java.rmi.RemoteException{
    if (si_PmsCpGenTran_ob == null)
      _initSi_PmsCpGenTran_obProxy();
    return si_PmsCpGenTran_ob.si_PmsCpGenTran_ob(mt_PmsCpGenTran_req);
  }
  
  
}