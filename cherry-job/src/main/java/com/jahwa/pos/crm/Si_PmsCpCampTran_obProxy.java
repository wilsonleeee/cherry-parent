package com.jahwa.pos.crm;

public class Si_PmsCpCampTran_obProxy implements com.jahwa.pos.crm.Si_PmsCpCampTran_ob {
  private String _endpoint = null;
  private com.jahwa.pos.crm.Si_PmsCpCampTran_ob si_PmsCpCampTran_ob = null;
  
  public Si_PmsCpCampTran_obProxy() {
    _initSi_PmsCpCampTran_obProxy();
  }
  
  public Si_PmsCpCampTran_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_PmsCpCampTran_obProxy();
  }
  
  private void _initSi_PmsCpCampTran_obProxy() {
    try {
      si_PmsCpCampTran_ob = (new com.jahwa.pos.crm.Si_PmsCpCampTran_obServiceLocator()).getsi_PmsCpCampTran_obPort();
      if (si_PmsCpCampTran_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_PmsCpCampTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_PmsCpCampTran_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_PmsCpCampTran_ob != null)
      ((javax.xml.rpc.Stub)si_PmsCpCampTran_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.jahwa.pos.crm.Si_PmsCpCampTran_ob getSi_PmsCpCampTran_ob() {
    if (si_PmsCpCampTran_ob == null)
      _initSi_PmsCpCampTran_obProxy();
    return si_PmsCpCampTran_ob;
  }
  
  public com.jahwa.pos.crm.Dt_PmsCpCampTran_res si_PmsCpCampTran_ob(com.jahwa.pos.crm.Dt_PmsCpCampTran_req mt_PmsCpCampTran_req) throws java.rmi.RemoteException{
    if (si_PmsCpCampTran_ob == null)
      _initSi_PmsCpCampTran_obProxy();
    return si_PmsCpCampTran_ob.si_PmsCpCampTran_ob(mt_PmsCpCampTran_req);
  }
  
  
}