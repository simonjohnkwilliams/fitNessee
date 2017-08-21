/**
 * ManageDAO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.capgemini.www.StubbingCommon;

public class ManageDAO  implements java.io.Serializable {
    private java.lang.String userName;

    private java.lang.String password;

    private java.lang.String sqlString;

    private com.capgemini.www.StubbingCommon.ParameterType[] parameter;

    public ManageDAO() {
    }

    public ManageDAO(
           java.lang.String userName,
           java.lang.String password,
           java.lang.String sqlString,
           com.capgemini.www.StubbingCommon.ParameterType[] parameter) {
           this.userName = userName;
           this.password = password;
           this.sqlString = sqlString;
           this.parameter = parameter;
    }


    /**
     * Gets the userName value for this ManageDAO.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this ManageDAO.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the password value for this ManageDAO.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ManageDAO.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the sqlString value for this ManageDAO.
     * 
     * @return sqlString
     */
    public java.lang.String getSqlString() {
        return sqlString;
    }


    /**
     * Sets the sqlString value for this ManageDAO.
     * 
     * @param sqlString
     */
    public void setSqlString(java.lang.String sqlString) {
        this.sqlString = sqlString;
    }


    /**
     * Gets the parameter value for this ManageDAO.
     * 
     * @return parameter
     */
    public com.capgemini.www.StubbingCommon.ParameterType[] getParameter() {
        return parameter;
    }


    /**
     * Sets the parameter value for this ManageDAO.
     * 
     * @param parameter
     */
    public void setParameter(com.capgemini.www.StubbingCommon.ParameterType[] parameter) {
        this.parameter = parameter;
    }

    public com.capgemini.www.StubbingCommon.ParameterType getParameter(int i) {
        return this.parameter[i];
    }

    public void setParameter(int i, com.capgemini.www.StubbingCommon.ParameterType _value) {
        this.parameter[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ManageDAO)) return false;
        ManageDAO other = (ManageDAO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.sqlString==null && other.getSqlString()==null) || 
             (this.sqlString!=null &&
              this.sqlString.equals(other.getSqlString()))) &&
            ((this.parameter==null && other.getParameter()==null) || 
             (this.parameter!=null &&
              java.util.Arrays.equals(this.parameter, other.getParameter())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getSqlString() != null) {
            _hashCode += getSqlString().hashCode();
        }
        if (getParameter() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameter(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ManageDAO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.capgemini.com/StubbingCommon/", ">ManageDAO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sqlString");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sqlString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Parameter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.capgemini.com/StubbingCommon/", "ParameterType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
