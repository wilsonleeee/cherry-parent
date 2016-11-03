/**
 * Dt_Price_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.ecc;

public class Dt_Price_req  implements java.io.Serializable {
    private java.lang.String ZVKORG;

    private java.lang.String ZKSCHL;

    public Dt_Price_req() {
    }

    public Dt_Price_req(
           java.lang.String ZVKORG,
           java.lang.String ZKSCHL) {
           this.ZVKORG = ZVKORG;
           this.ZKSCHL = ZKSCHL;
    }


    /**
     * Gets the ZVKORG value for this Dt_Price_req.
     * 
     * @return ZVKORG
     */
    public java.lang.String getZVKORG() {
        return ZVKORG;
    }


    /**
     * Sets the ZVKORG value for this Dt_Price_req.
     * 
     * @param ZVKORG
     */
    public void setZVKORG(java.lang.String ZVKORG) {
        this.ZVKORG = ZVKORG;
    }


    /**
     * Gets the ZKSCHL value for this Dt_Price_req.
     * 
     * @return ZKSCHL
     */
    public java.lang.String getZKSCHL() {
        return ZKSCHL;
    }


    /**
     * Sets the ZKSCHL value for this Dt_Price_req.
     * 
     * @param ZKSCHL
     */
    public void setZKSCHL(java.lang.String ZKSCHL) {
        this.ZKSCHL = ZKSCHL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_Price_req)) return false;
        Dt_Price_req other = (Dt_Price_req) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZVKORG==null && other.getZVKORG()==null) || 
             (this.ZVKORG!=null &&
              this.ZVKORG.equals(other.getZVKORG()))) &&
            ((this.ZKSCHL==null && other.getZKSCHL()==null) || 
             (this.ZKSCHL!=null &&
              this.ZKSCHL.equals(other.getZKSCHL())));
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
        if (getZVKORG() != null) {
            _hashCode += getZVKORG().hashCode();
        }
        if (getZKSCHL() != null) {
            _hashCode += getZKSCHL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_Price_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/ecc", "dt_Price_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZVKORG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZVKORG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZKSCHL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZKSCHL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
