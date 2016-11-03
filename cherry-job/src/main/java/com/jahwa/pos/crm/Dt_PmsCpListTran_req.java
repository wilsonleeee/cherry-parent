/**
 * Dt_PmsCpListTran_req.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jahwa.pos.crm;

public class Dt_PmsCpListTran_req  implements java.io.Serializable {
    private java.lang.String MKT_ORG;

    private java.lang.String SOURCE;

    private java.lang.String ZCHANNEL;

    private java.lang.String EXTERNAL_ID1;

    private java.lang.String EXTERNAL_ID2;

    private java.lang.String ZCP_TYPE;

    private java.lang.String ZCP_YHQ;

    private java.lang.String ZCP_YHQDES;

    private java.math.BigDecimal LIST_NUM;

    private com.jahwa.pos.crm.Dt_PmsCpListTran_reqZLISTITEM[] ZLIST;

    public Dt_PmsCpListTran_req() {
    }

    public Dt_PmsCpListTran_req(
           java.lang.String MKT_ORG,
           java.lang.String SOURCE,
           java.lang.String ZCHANNEL,
           java.lang.String EXTERNAL_ID1,
           java.lang.String EXTERNAL_ID2,
           java.lang.String ZCP_TYPE,
           java.lang.String ZCP_YHQ,
           java.lang.String ZCP_YHQDES,
           java.math.BigDecimal LIST_NUM,
           com.jahwa.pos.crm.Dt_PmsCpListTran_reqZLISTITEM[] ZLIST) {
           this.MKT_ORG = MKT_ORG;
           this.SOURCE = SOURCE;
           this.ZCHANNEL = ZCHANNEL;
           this.EXTERNAL_ID1 = EXTERNAL_ID1;
           this.EXTERNAL_ID2 = EXTERNAL_ID2;
           this.ZCP_TYPE = ZCP_TYPE;
           this.ZCP_YHQ = ZCP_YHQ;
           this.ZCP_YHQDES = ZCP_YHQDES;
           this.LIST_NUM = LIST_NUM;
           this.ZLIST = ZLIST;
    }


    /**
     * Gets the MKT_ORG value for this Dt_PmsCpListTran_req.
     * 
     * @return MKT_ORG
     */
    public java.lang.String getMKT_ORG() {
        return MKT_ORG;
    }


    /**
     * Sets the MKT_ORG value for this Dt_PmsCpListTran_req.
     * 
     * @param MKT_ORG
     */
    public void setMKT_ORG(java.lang.String MKT_ORG) {
        this.MKT_ORG = MKT_ORG;
    }


    /**
     * Gets the SOURCE value for this Dt_PmsCpListTran_req.
     * 
     * @return SOURCE
     */
    public java.lang.String getSOURCE() {
        return SOURCE;
    }


    /**
     * Sets the SOURCE value for this Dt_PmsCpListTran_req.
     * 
     * @param SOURCE
     */
    public void setSOURCE(java.lang.String SOURCE) {
        this.SOURCE = SOURCE;
    }


    /**
     * Gets the ZCHANNEL value for this Dt_PmsCpListTran_req.
     * 
     * @return ZCHANNEL
     */
    public java.lang.String getZCHANNEL() {
        return ZCHANNEL;
    }


    /**
     * Sets the ZCHANNEL value for this Dt_PmsCpListTran_req.
     * 
     * @param ZCHANNEL
     */
    public void setZCHANNEL(java.lang.String ZCHANNEL) {
        this.ZCHANNEL = ZCHANNEL;
    }


    /**
     * Gets the EXTERNAL_ID1 value for this Dt_PmsCpListTran_req.
     * 
     * @return EXTERNAL_ID1
     */
    public java.lang.String getEXTERNAL_ID1() {
        return EXTERNAL_ID1;
    }


    /**
     * Sets the EXTERNAL_ID1 value for this Dt_PmsCpListTran_req.
     * 
     * @param EXTERNAL_ID1
     */
    public void setEXTERNAL_ID1(java.lang.String EXTERNAL_ID1) {
        this.EXTERNAL_ID1 = EXTERNAL_ID1;
    }


    /**
     * Gets the EXTERNAL_ID2 value for this Dt_PmsCpListTran_req.
     * 
     * @return EXTERNAL_ID2
     */
    public java.lang.String getEXTERNAL_ID2() {
        return EXTERNAL_ID2;
    }


    /**
     * Sets the EXTERNAL_ID2 value for this Dt_PmsCpListTran_req.
     * 
     * @param EXTERNAL_ID2
     */
    public void setEXTERNAL_ID2(java.lang.String EXTERNAL_ID2) {
        this.EXTERNAL_ID2 = EXTERNAL_ID2;
    }


    /**
     * Gets the ZCP_TYPE value for this Dt_PmsCpListTran_req.
     * 
     * @return ZCP_TYPE
     */
    public java.lang.String getZCP_TYPE() {
        return ZCP_TYPE;
    }


    /**
     * Sets the ZCP_TYPE value for this Dt_PmsCpListTran_req.
     * 
     * @param ZCP_TYPE
     */
    public void setZCP_TYPE(java.lang.String ZCP_TYPE) {
        this.ZCP_TYPE = ZCP_TYPE;
    }


    /**
     * Gets the ZCP_YHQ value for this Dt_PmsCpListTran_req.
     * 
     * @return ZCP_YHQ
     */
    public java.lang.String getZCP_YHQ() {
        return ZCP_YHQ;
    }


    /**
     * Sets the ZCP_YHQ value for this Dt_PmsCpListTran_req.
     * 
     * @param ZCP_YHQ
     */
    public void setZCP_YHQ(java.lang.String ZCP_YHQ) {
        this.ZCP_YHQ = ZCP_YHQ;
    }


    /**
     * Gets the ZCP_YHQDES value for this Dt_PmsCpListTran_req.
     * 
     * @return ZCP_YHQDES
     */
    public java.lang.String getZCP_YHQDES() {
        return ZCP_YHQDES;
    }


    /**
     * Sets the ZCP_YHQDES value for this Dt_PmsCpListTran_req.
     * 
     * @param ZCP_YHQDES
     */
    public void setZCP_YHQDES(java.lang.String ZCP_YHQDES) {
        this.ZCP_YHQDES = ZCP_YHQDES;
    }


    /**
     * Gets the LIST_NUM value for this Dt_PmsCpListTran_req.
     * 
     * @return LIST_NUM
     */
    public java.math.BigDecimal getLIST_NUM() {
        return LIST_NUM;
    }


    /**
     * Sets the LIST_NUM value for this Dt_PmsCpListTran_req.
     * 
     * @param LIST_NUM
     */
    public void setLIST_NUM(java.math.BigDecimal LIST_NUM) {
        this.LIST_NUM = LIST_NUM;
    }


    /**
     * Gets the ZLIST value for this Dt_PmsCpListTran_req.
     * 
     * @return ZLIST
     */
    public com.jahwa.pos.crm.Dt_PmsCpListTran_reqZLISTITEM[] getZLIST() {
        return ZLIST;
    }


    /**
     * Sets the ZLIST value for this Dt_PmsCpListTran_req.
     * 
     * @param ZLIST
     */
    public void setZLIST(com.jahwa.pos.crm.Dt_PmsCpListTran_reqZLISTITEM[] ZLIST) {
        this.ZLIST = ZLIST;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Dt_PmsCpListTran_req)) return false;
        Dt_PmsCpListTran_req other = (Dt_PmsCpListTran_req) obj;
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
            ((this.ZCHANNEL==null && other.getZCHANNEL()==null) || 
             (this.ZCHANNEL!=null &&
              this.ZCHANNEL.equals(other.getZCHANNEL()))) &&
            ((this.EXTERNAL_ID1==null && other.getEXTERNAL_ID1()==null) || 
             (this.EXTERNAL_ID1!=null &&
              this.EXTERNAL_ID1.equals(other.getEXTERNAL_ID1()))) &&
            ((this.EXTERNAL_ID2==null && other.getEXTERNAL_ID2()==null) || 
             (this.EXTERNAL_ID2!=null &&
              this.EXTERNAL_ID2.equals(other.getEXTERNAL_ID2()))) &&
            ((this.ZCP_TYPE==null && other.getZCP_TYPE()==null) || 
             (this.ZCP_TYPE!=null &&
              this.ZCP_TYPE.equals(other.getZCP_TYPE()))) &&
            ((this.ZCP_YHQ==null && other.getZCP_YHQ()==null) || 
             (this.ZCP_YHQ!=null &&
              this.ZCP_YHQ.equals(other.getZCP_YHQ()))) &&
            ((this.ZCP_YHQDES==null && other.getZCP_YHQDES()==null) || 
             (this.ZCP_YHQDES!=null &&
              this.ZCP_YHQDES.equals(other.getZCP_YHQDES()))) &&
            ((this.LIST_NUM==null && other.getLIST_NUM()==null) || 
             (this.LIST_NUM!=null &&
              this.LIST_NUM.equals(other.getLIST_NUM()))) &&
            ((this.ZLIST==null && other.getZLIST()==null) || 
             (this.ZLIST!=null &&
              java.util.Arrays.equals(this.ZLIST, other.getZLIST())));
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
        if (getZCHANNEL() != null) {
            _hashCode += getZCHANNEL().hashCode();
        }
        if (getEXTERNAL_ID1() != null) {
            _hashCode += getEXTERNAL_ID1().hashCode();
        }
        if (getEXTERNAL_ID2() != null) {
            _hashCode += getEXTERNAL_ID2().hashCode();
        }
        if (getZCP_TYPE() != null) {
            _hashCode += getZCP_TYPE().hashCode();
        }
        if (getZCP_YHQ() != null) {
            _hashCode += getZCP_YHQ().hashCode();
        }
        if (getZCP_YHQDES() != null) {
            _hashCode += getZCP_YHQDES().hashCode();
        }
        if (getLIST_NUM() != null) {
            _hashCode += getLIST_NUM().hashCode();
        }
        if (getZLIST() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getZLIST());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getZLIST(), i);
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
        new org.apache.axis.description.TypeDesc(Dt_PmsCpListTran_req.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", "dt_PmsCpListTran_req"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MKT_ORG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MKT_ORG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SOURCE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SOURCE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZCHANNEL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCHANNEL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID2"));
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
        elemField.setFieldName("ZCP_YHQDES");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZCP_YHQDES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LIST_NUM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LIST_NUM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZLIST");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZLIST"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://jahwa.com/pos/crm", ">>dt_PmsCpListTran_req>ZLIST>ITEM"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ITEM"));
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
