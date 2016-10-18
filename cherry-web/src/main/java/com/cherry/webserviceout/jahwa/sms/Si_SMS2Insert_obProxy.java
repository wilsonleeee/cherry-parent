package com.cherry.webserviceout.jahwa.sms;

public class Si_SMS2Insert_obProxy implements com.cherry.webserviceout.jahwa.sms.Si_SMS2Insert_ob {
  private String _endpoint = null;
  private com.cherry.webserviceout.jahwa.sms.Si_SMS2Insert_ob si_SMS2Insert_ob = null;
  
  public Si_SMS2Insert_obProxy() {
    _initSi_SMS2Insert_obProxy();
  }
  
  public Si_SMS2Insert_obProxy(String endpoint) {
    _endpoint = endpoint;
    _initSi_SMS2Insert_obProxy();
  }
  
  private void _initSi_SMS2Insert_obProxy() {
    try {
      si_SMS2Insert_ob = (new com.cherry.webserviceout.jahwa.sms.Si_SMS2Insert_obServiceLocator()).getsi_SMS2Insert_obPort();
      if (si_SMS2Insert_ob != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)si_SMS2Insert_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)si_SMS2Insert_ob)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (si_SMS2Insert_ob != null)
      ((javax.xml.rpc.Stub)si_SMS2Insert_ob)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.cherry.webserviceout.jahwa.sms.Si_SMS2Insert_ob getSi_SMS2Insert_ob() {
    if (si_SMS2Insert_ob == null)
      _initSi_SMS2Insert_obProxy();
    return si_SMS2Insert_ob;
  }
  
  public com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_res si_SMS2Insert_ob(com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_req mt_SMSInsert_req) throws java.rmi.RemoteException{
    if (si_SMS2Insert_ob == null)
      _initSi_SMS2Insert_obProxy();
    return si_SMS2Insert_ob.si_SMS2Insert_ob(mt_SMSInsert_req);
  }
  
  
}