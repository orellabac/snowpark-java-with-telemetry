package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public final class Customer {

    @JsonProperty("C_CUSTKEY")
    private Long C_CUSTKEY;

    @JsonProperty("C_NAME")
    private String C_NAME;

    @JsonProperty("C_ADDRESS")
    private String C_ADDRESS;

    @JsonProperty("C_NATIONKEY")
    private Long C_NATIONKEY;

    @JsonProperty("C_PHONE")
    private String C_PHONE;

    @JsonProperty("C_ACCTBAL")
    private BigDecimal C_ACCTBAL;

    @JsonProperty("C_MKTSEGMENT")
    private String C_MKTSEGMENT;

    @JsonProperty("C_COMMENT")
    private String C_COMMENT;

    // No-argument constructor
    public Customer() {
    }

    // All-arguments constructor
    public Customer(Long C_CUSTKEY, String C_NAME, String C_ADDRESS, Long C_NATIONKEY,
                    String C_PHONE, BigDecimal C_ACCTBAL, String C_MKTSEGMENT, String C_COMMENT) {
        this.C_CUSTKEY = C_CUSTKEY;
        this.C_NAME = C_NAME;
        this.C_ADDRESS = C_ADDRESS;
        this.C_NATIONKEY = C_NATIONKEY;
        this.C_PHONE = C_PHONE;
        this.C_ACCTBAL = C_ACCTBAL;
        this.C_MKTSEGMENT = C_MKTSEGMENT;
        this.C_COMMENT = C_COMMENT;
    }

    // Getters and Setters
    public Long getC_CUSTKEY() {
        return C_CUSTKEY;
    }

    public void setC_CUSTKEY(Long C_CUSTKEY) {
        this.C_CUSTKEY = C_CUSTKEY;
    }

    public String getC_NAME() {
        return C_NAME;
    }

    public void setC_NAME(String C_NAME) {
        this.C_NAME = C_NAME;
    }

    public String getC_ADDRESS() {
        return C_ADDRESS;
    }

    public void setC_ADDRESS(String C_ADDRESS) {
        this.C_ADDRESS = C_ADDRESS;
    }

    public Long getC_NATIONKEY() {
        return C_NATIONKEY;
    }

    public void setC_NATIONKEY(Long C_NATIONKEY) {
        this.C_NATIONKEY = C_NATIONKEY;
    }

    public String getC_PHONE() {
        return C_PHONE;
    }

    public void setC_PHONE(String C_PHONE) {
        this.C_PHONE = C_PHONE;
    }

    public BigDecimal getC_ACCTBAL() {
        return C_ACCTBAL;
    }

    public void setC_ACCTBAL(BigDecimal C_ACCTBAL) {
        this.C_ACCTBAL = C_ACCTBAL;
    }

    public String getC_MKTSEGMENT() {
        return C_MKTSEGMENT;
    }

    public void setC_MKTSEGMENT(String C_MKTSEGMENT) {
        this.C_MKTSEGMENT = C_MKTSEGMENT;
    }

    public String getC_COMMENT() {
        return C_COMMENT;
    }

    public void setC_COMMENT(String C_COMMENT) {
        this.C_COMMENT = C_COMMENT;
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(C_CUSTKEY, customer.C_CUSTKEY) &&
               Objects.equals(C_NAME, customer.C_NAME) &&
               Objects.equals(C_ADDRESS, customer.C_ADDRESS) &&
               Objects.equals(C_NATIONKEY, customer.C_NATIONKEY) &&
               Objects.equals(C_PHONE, customer.C_PHONE) &&
               Objects.equals(C_ACCTBAL, customer.C_ACCTBAL) &&
               Objects.equals(C_MKTSEGMENT, customer.C_MKTSEGMENT) &&
               Objects.equals(C_COMMENT, customer.C_COMMENT);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(C_CUSTKEY, C_NAME, C_ADDRESS, C_NATIONKEY, C_PHONE, C_ACCTBAL, C_MKTSEGMENT, C_COMMENT);
    }

    // toString
    @Override
    public String toString() {
        return "Customer{" +
                "C_CUSTKEY=" + C_CUSTKEY +
                ", C_NAME='" + C_NAME + '\'' +
                ", C_ADDRESS='" + C_ADDRESS + '\'' +
                ", C_NATIONKEY=" + C_NATIONKEY +
                ", C_PHONE='" + C_PHONE + '\'' +
                ", C_ACCTBAL=" + C_ACCTBAL +
                ", C_MKTSEGMENT='" + C_MKTSEGMENT + '\'' +
                ", C_COMMENT='" + C_COMMENT + '\'' +
                '}';
    }
}
