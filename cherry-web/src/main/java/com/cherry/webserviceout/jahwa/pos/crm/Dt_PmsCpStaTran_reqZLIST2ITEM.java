/**
 * Dt_PmsCpStaTran_reqZLIST2ITEM.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cherry.webserviceout.jahwa.pos.crm;

public class Dt_PmsCpStaTran_reqZLIST2ITEM  implements java.io.Serializable {
    private java.lang.String MKT_ORG;

    private java.lang.String SOURCE;

    private java.lang.String ZCP_TYPE;

    private java.lang.String ZCP_YHQ;

    private java.lang.String ZCP_NUM;

    private java.lang.String WB_ORDER_ID;

    private java.lang.String ZCP_PROD;

    private java.math.BigInteger QUANTITY;

    public Dt_PmsCpStaTran_reqZLIST2ITEM() {
    }

    public Dt_PmsCpStaTran_reqZLIST2ITEM(
           java.lang.String MKT_ORG,
           java.lang.String SOURCE,
           java.lang.String ZCP_TYPE,
           java.lang.String ZCP_YHQ,
           java.lang.String ZCP_NUM,
           java.lang.String WB_ORDER_ID,
           java.lang.String ZCP_PROD,
           java.math.BigInteger QUANTITY) {
           this.MKT_ORG = MKT_ORG;
           this.SOURCE = SOURCE;
           this.ZCP_TYPE = ZCP_TYPE;
           this.ZCP_YHQ = ZCP_YHQ;
           this.ZCP_NUM = ZCP_NUM;
           this.WB_ORDER_ID = WB_ORDER_ID;
           this.ZCP_PROD = ZCP_PROD;
           this.QUANTITY = QUANTITY;
    }


    /**
     * Gets the MKT_ORG value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return MKT_ORG
     */
    public java.lang.String getMKT_ORG() {
        return MKT_ORG;
    }


    /**
     * Sets the MKT_ORG value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param MKT_ORG
     */
    public void setMKT_ORG(java.lang.String MKT_ORG) {
        this.MKT_ORG = MKT_ORG;
    }


    /**
     * Gets the SOURCE value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return SOURCE
     */
    public java.lang.String getSOURCE() {
        return SOURCE;
    }


    /**
     * Sets the SOURCE value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param SOURCE
     */
    public void setSOURCE(java.lang.String SOURCE) {
        this.SOURCE = SOURCE;
    }


    /**
     * Gets the ZCP_TYPE value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return ZCP_TYPE
     */
    public java.lang.String getZCP_TYPE() {
        return ZCP_TYPE;
    }


    /**
     * Sets the ZCP_TYPE value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param ZCP_TYPE
     */
    public void setZCP_TYPE(java.lang.String ZCP_TYPE) {
        this.ZCP_TYPE = ZCP_TYPE;
    }


    /**
     * Gets the ZCP_YHQ value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return ZCP_YHQ
     */
    public java.lang.String getZCP_YHQ() {
        return ZCP_YHQ;
    }


    /**
     * Sets the ZCP_YHQ value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param ZCP_YHQ
     */
    public void setZCP_YHQ(java.lang.String ZCP_YHQ) {
        this.ZCP_YHQ = ZCP_YHQ;
    }


    /**
     * Gets the ZCP_NUM value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return ZCP_NUM
     */
    public java.lang.String getZCP_NUM() {
        return ZCP_NUM;
    }


    /**
     * Sets the ZCP_NUM value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param ZCP_NUM
     */
    public void setZCP_NUM(java.lang.String ZCP_NUM) {
        this.ZCP_NUM = ZCP_NUM;
    }


    /**
     * Gets the WB_ORDER_ID value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return WB_ORDER_ID
     */
    public java.lang.String getWB_ORDER_ID() {
        return WB_ORDER_ID;
    }


    /**
     * Sets the WB_ORDER_ID value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param WB_ORDER_ID
     */
    public void setWB_ORDER_ID(java.lang.String WB_ORDER_ID) {
        this.WB_ORDER_ID = WB_ORDER_ID;
    }


    /**
     * Gets the ZCP_PROD value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return ZCP_PROD
     */
    public java.lang.String getZCP_PROD() {
        return ZCP_PROD;
    }


    /**
     * Sets the ZCP_PROD value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param ZCP_PROD
     */
    public void setZCP_PROD(java.lang.String ZCP_PROD) {
        this.ZCP_PROD = ZCP_PROD;
    }


    /**
     * Gets the QUANTITY value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @return QUANTITY
     */
    public java.math.BigInteger getQUANTITY() {
        return QUANTITY;
    }


    /**
     * Sets the QUANTITY value for this Dt_PmsCpStaTran_reqZLIST2ITEM.
     * 
     * @param QUANTITY
     */
    public void setQUANTITY(java.math.BigInteger QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpStaTran_reqZLIST2ITEM)) return false;
        Dt_PmsCpStaTran_reqZLIST2ITEM other = (Dt_PmsCpStaTran_reqZLIST2ITEM) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MKT_ORG==null && other.getMKT_ORG()==null) || 
             (this.MKT_ORG!=null &&
              this.MKT_ORG.equals(other.getMKT_ORG()))) &&
            ((this.SOURCE==null && other.getSOURCE()==null) || 
             (this.SOURCE!=null &&
              this.SOURCE.equals(other.getSOURCE()))) &&
            ((this.ZCP_TYPE==null && other.getZCP_TYPE()==null) || 
             (this.ZCP_TYPE!=null &&
              this.ZCP_TYPE.equals(other.getZCP_TYPE()))) &&
            ((this.ZCP_YHQ==null && other.getZCP_YHQ()==null) || 
             (this.ZCP_YHQ!=null &&
              this.ZCP_YHQ.equals(other.getZCP_YHQ()))) &&
            ((this.ZCP_NUM==null && other.getZCP_NUM()==null) || 
             (this.ZCP_NUM!=null &&
              this.ZCP_NUM.equals(other.getZCP_NUM()))) &&
            ((this.WB_ORDER_ID==null && other.getWB_ORDER_ID()==null) || 
             (this.WB_ORDER_ID!=null &&
              this.WB_ORDER_ID.equals(other.getWB_ORDER_ID()))) &&
            ((this.ZCP_PROD==null && other.getZCP_PROD()==null) || 
             (this.ZCP_PROD!=null &&
              this.ZCP_PROD.equals(other.getZCP_PROD()))) &&
            ((this.QUANTITY==null && other.getQUANTITY()==null) || 
             (this.QUANTITY!=null &&
              this.QUANTITY.equals(other.getQUANTITY())));
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
        if (getMKT_ORG() != null) {
            _hashCode += getMKT_ORG().hashCode();
        }
        if (getSOURCE() != null) {
            _hashCode += getSOURCE().hashCode();
        }
        if (getZCP_TYPE() != null) {
            _hashCode += getZCP_TYPE().hashCode();
        }
        if (getZCP_YHQ() != null) {
            _hashCode += getZCP_YHQ().hashCode();
        }
        if (getZCP_NUM() != null) {
            _hashCode += getZCP_NUM().hashCode();
        }
        if (getWB_ORDER_ID() != null) {
            _hashCode += getWB_ORDER_ID().hashCode();
        }
        if (getZCP_PROD() != null) {
            _hashCode += getZCP_PROD().hashCode();
        }
        if (getQUANTITY() != null) {
            _hashCode += getQUANTITY().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Dt_PmsCpStaTran_reqZLIST2ITEM.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpStaTran_req>ZLIST2>ITEM"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MKT_ORG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MKT_ORG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SOURCE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SOURCE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCP_TYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCP_TYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCP_YHQ");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCP_YHQ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCP_NUM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCP_NUM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WB_ORDER_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WB_ORDER_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCP_PROD");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCP_PROD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QUANTITY");
        elemField.setXmlName(new javax.xml.namespace.QName("", "QUANTITY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
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
