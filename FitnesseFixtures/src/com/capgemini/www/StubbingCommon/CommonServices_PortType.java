/**
 * CommonServices_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.capgemini.www.StubbingCommon;

public interface CommonServices_PortType extends java.rmi.Remote {
    public java.lang.String createStub(java.lang.String hostName, java.lang.String matchString, java.lang.String response, java.lang.Integer HTTPReturnCode) throws java.rmi.RemoteException;
    public com.capgemini.www.StubbingCommon.StubComplexType[] retrieveStub(java.lang.String hostName, java.lang.String matchString) throws java.rmi.RemoteException;
    public java.lang.String updateStub(com.capgemini.www.StubbingCommon.StubComplexType[] stub) throws java.rmi.RemoteException;
    public void manageDAO(java.lang.String userName, java.lang.String password, java.lang.String sqlString, com.capgemini.www.StubbingCommon.ParameterType[] parameter, javax.xml.rpc.holders.BooleanHolder isUpdate, com.capgemini.www.StubbingCommon.holders.ResultSetTypeHolder resultSet) throws java.rmi.RemoteException;
}
