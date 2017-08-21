/**
 * ManageDAOResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.capgemini.www.StubbingCommon;

public class ManageDAOResponse  implements java.io.Serializable {
    private boolean isUpdate;

    private com.capgemini.www.StubbingCommon.RowType[] resultSet;

    public ManageDAOResponse() {
    }

    public ManageDAOResponse(
           boolean isUpdate,
           com.capgemini.www.StubbingCommon.RowType[] resultSet) {
           this.isUpdate = isUpdate;
           this.resultSet = resultSet;
    }


    /**
     * Gets the isUpdate value for this ManageDAOResponse.
     * 
     * @return isUpdate
     */
    public boolean isIsUpdate() {
        return isUpdate;
    }


    /**
     * Sets the isUpdate value for this ManageDAOResponse.
     * 
     * @param isUpdate
     */
    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }


    /**
     * Gets the resultSet value for this ManageDAOResponse.
     * 
     * @return resultSet
     */
    public com.capgemini.www.StubbingCommon.RowType[] getResultSet() {
        return resultSet;
    }


    /**
     * Sets the resultSet value for this ManageDAOResponse.
     * 
     * @param resultSet
     */
    public void setResultSet(com.capgemini.www.StubbingCommon.RowType[] resultSet) {
        this.resultSet = resultSet;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ManageDAOResponse)) return false;
        ManageDAOResponse other = (ManageDAOResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.isUpdate == other.isIsUpdate() &&
            ((this.resultSet==null && other.getResultSet()==null) || 
             (this.resultSet!=null &&
              java.util.Arrays.equals(this.resultSet, other.getResultSet())));
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
        _hashCode += (isIsUpdate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getResultSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResultSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResultSet(), i);
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
        new org.apache.axis.description.TypeDesc(ManageDAOResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.capgemini.com/StubbingCommon/", ">ManageDAOResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isUpdate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isUpdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultSet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ResultSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.capgemini.com/StubbingCommon/", "RowType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Row"));
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
