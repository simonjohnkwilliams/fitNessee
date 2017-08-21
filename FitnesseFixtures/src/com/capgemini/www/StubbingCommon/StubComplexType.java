/**
 * StubComplexType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.capgemini.www.StubbingCommon;

public class StubComplexType  implements java.io.Serializable {
    private java.lang.String stubId;

    private boolean isDisabled;

    private java.lang.String hostName;

    private java.lang.String matchString;

    private java.lang.String response;

    private int HTTPReturnCode;

    public StubComplexType() {
    }

    public StubComplexType(
           java.lang.String stubId,
           boolean isDisabled,
           java.lang.String hostName,
           java.lang.String matchString,
           java.lang.String response,
           int HTTPReturnCode) {
           this.stubId = stubId;
           this.isDisabled = isDisabled;
           this.hostName = hostName;
           this.matchString = matchString;
           this.response = response;
           this.HTTPReturnCode = HTTPReturnCode;
    }


    /**
     * Gets the stubId value for this StubComplexType.
     * 
     * @return stubId
     */
    public java.lang.String getStubId() {
        return stubId;
    }


    /**
     * Sets the stubId value for this StubComplexType.
     * 
     * @param stubId
     */
    public void setStubId(java.lang.String stubId) {
        this.stubId = stubId;
    }


    /**
     * Gets the isDisabled value for this StubComplexType.
     * 
     * @return isDisabled
     */
    public boolean isIsDisabled() {
        return isDisabled;
    }


    /**
     * Sets the isDisabled value for this StubComplexType.
     * 
     * @param isDisabled
     */
    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }


    /**
     * Gets the hostName value for this StubComplexType.
     * 
     * @return hostName
     */
    public java.lang.String getHostName() {
        return hostName;
    }


    /**
     * Sets the hostName value for this StubComplexType.
     * 
     * @param hostName
     */
    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }


    /**
     * Gets the matchString value for this StubComplexType.
     * 
     * @return matchString
     */
    public java.lang.String getMatchString() {
        return matchString;
    }


    /**
     * Sets the matchString value for this StubComplexType.
     * 
     * @param matchString
     */
    public void setMatchString(java.lang.String matchString) {
        this.matchString = matchString;
    }


    /**
     * Gets the response value for this StubComplexType.
     * 
     * @return response
     */
    public java.lang.String getResponse() {
        return response;
    }


    /**
     * Sets the response value for this StubComplexType.
     * 
     * @param response
     */
    public void setResponse(java.lang.String response) {
        this.response = response;
    }


    /**
     * Gets the HTTPReturnCode value for this StubComplexType.
     * 
     * @return HTTPReturnCode
     */
    public int getHTTPReturnCode() {
        return HTTPReturnCode;
    }


    /**
     * Sets the HTTPReturnCode value for this StubComplexType.
     * 
     * @param HTTPReturnCode
     */
    public void setHTTPReturnCode(int HTTPReturnCode) {
        this.HTTPReturnCode = HTTPReturnCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StubComplexType)) return false;
        StubComplexType other = (StubComplexType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.stubId==null && other.getStubId()==null) || 
             (this.stubId!=null &&
              this.stubId.equals(other.getStubId()))) &&
            this.isDisabled == other.isIsDisabled() &&
            ((this.hostName==null && other.getHostName()==null) || 
             (this.hostName!=null &&
              this.hostName.equals(other.getHostName()))) &&
            ((this.matchString==null && other.getMatchString()==null) || 
             (this.matchString!=null &&
              this.matchString.equals(other.getMatchString()))) &&
            ((this.response==null && other.getResponse()==null) || 
             (this.response!=null &&
              this.response.equals(other.getResponse()))) &&
            this.HTTPReturnCode == other.getHTTPReturnCode();
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
        if (getStubId() != null) {
            _hashCode += getStubId().hashCode();
        }
        _hashCode += (isIsDisabled() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getHostName() != null) {
            _hashCode += getHostName().hashCode();
        }
        if (getMatchString() != null) {
            _hashCode += getMatchString().hashCode();
        }
        if (getResponse() != null) {
            _hashCode += getResponse().hashCode();
        }
        _hashCode += getHTTPReturnCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StubComplexType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.capgemini.com/StubbingCommon/", "StubComplexType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stubId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StubId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDisabled");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isDisabled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HostName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchString");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MatchString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("response");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Response"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HTTPReturnCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HTTPReturnCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
