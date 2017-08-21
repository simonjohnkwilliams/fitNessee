package com.capgemini.www.StubbingCommon;

public class CommonServicesProxy implements com.capgemini.www.StubbingCommon.CommonServices_PortType {
  private String _endpoint = null;
  private com.capgemini.www.StubbingCommon.CommonServices_PortType commonServices_PortType = null;
  
  public CommonServicesProxy() {
    _initCommonServicesProxy();
  }
  
  public CommonServicesProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonServicesProxy();
  }
  
  private void _initCommonServicesProxy() {
    try {
      commonServices_PortType = (new com.capgemini.www.StubbingCommon.CommonServices_ServiceLocator()).getCommonServicesSOAP();
      if (commonServices_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonServices_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonServices_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonServices_PortType != null)
      ((javax.xml.rpc.Stub)commonServices_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.capgemini.www.StubbingCommon.CommonServices_PortType getCommonServices_PortType() {
    if (commonServices_PortType == null)
      _initCommonServicesProxy();
    return commonServices_PortType;
  }
  
  public java.lang.String createStub(java.lang.String hostName, java.lang.String matchString, java.lang.String response, java.lang.Integer HTTPReturnCode) throws java.rmi.RemoteException{
    if (commonServices_PortType == null)
      _initCommonServicesProxy();
    return commonServices_PortType.createStub(hostName, matchString, response, HTTPReturnCode);
  }
  
  public com.capgemini.www.StubbingCommon.StubComplexType[] retrieveStub(java.lang.String hostName, java.lang.String matchString) throws java.rmi.RemoteException{
    if (commonServices_PortType == null)
      _initCommonServicesProxy();
    return commonServices_PortType.retrieveStub(hostName, matchString);
  }
  
  public java.lang.String updateStub(com.capgemini.www.StubbingCommon.StubComplexType[] stub) throws java.rmi.RemoteException{
    if (commonServices_PortType == null)
      _initCommonServicesProxy();
    return commonServices_PortType.updateStub(stub);
  }
  
  public void manageDAO(java.lang.String userName, java.lang.String password, java.lang.String sqlString, com.capgemini.www.StubbingCommon.ParameterType[] parameter, javax.xml.rpc.holders.BooleanHolder isUpdate, com.capgemini.www.StubbingCommon.holders.ResultSetTypeHolder resultSet) throws java.rmi.RemoteException{
    if (commonServices_PortType == null)
      _initCommonServicesProxy();
    commonServices_PortType.manageDAO(userName, password, sqlString, parameter, isUpdate, resultSet);
  }
  
  
}