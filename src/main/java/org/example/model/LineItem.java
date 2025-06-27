package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class LineItem {
    private Long L_ORDERKEY;
    private Long L_PARTKEY;
    private Long L_SUPPKEY;
    private Long L_LINENUMBER;
    private BigDecimal L_QUANTITY;
    private BigDecimal L_EXTENDEDPRICE;
    private BigDecimal L_DISCOUNT;
    private BigDecimal L_TAX;
    private String L_RETURNFLAG;
    private String L_LINESTATUS;
    private String L_SHIPDATE;
    private Long L_COMMITDATE;
    private Long L_RECEIPTDATE;
    private String L_SHIPINSTRUCT;
    private String L_SHIPMODE;
    private String L_COMMENT;

    // No-args constructor
    public LineItem() {
    }

    // All-args constructor
    public LineItem(Long L_ORDERKEY, Long L_PARTKEY, Long L_SUPPKEY, Long L_LINENUMBER,
                    BigDecimal L_QUANTITY, BigDecimal L_EXTENDEDPRICE, BigDecimal L_DISCOUNT, BigDecimal L_TAX,
                    String L_RETURNFLAG, String L_LINESTATUS, String L_SHIPDATE,
                    Long L_COMMITDATE, Long L_RECEIPTDATE, String L_SHIPINSTRUCT,
                    String L_SHIPMODE, String L_COMMENT) {
        this.L_ORDERKEY = L_ORDERKEY;
        this.L_PARTKEY = L_PARTKEY;
        this.L_SUPPKEY = L_SUPPKEY;
        this.L_LINENUMBER = L_LINENUMBER;
        this.L_QUANTITY = L_QUANTITY;
        this.L_EXTENDEDPRICE = L_EXTENDEDPRICE;
        this.L_DISCOUNT = L_DISCOUNT;
        this.L_TAX = L_TAX;
        this.L_RETURNFLAG = L_RETURNFLAG;
        this.L_LINESTATUS = L_LINESTATUS;
        this.L_SHIPDATE = L_SHIPDATE;
        this.L_COMMITDATE = L_COMMITDATE;
        this.L_RECEIPTDATE = L_RECEIPTDATE;
        this.L_SHIPINSTRUCT = L_SHIPINSTRUCT;
        this.L_SHIPMODE = L_SHIPMODE;
        this.L_COMMENT = L_COMMENT;
    }

    // Getters and Setters

    public Long getL_ORDERKEY() {
        return L_ORDERKEY;
    }

    public void setL_ORDERKEY(Long l_ORDERKEY) {
        L_ORDERKEY = l_ORDERKEY;
    }

    public Long getL_PARTKEY() {
        return L_PARTKEY;
    }

    public void setL_PARTKEY(Long l_PARTKEY) {
        L_PARTKEY = l_PARTKEY;
    }

    public Long getL_SUPPKEY() {
        return L_SUPPKEY;
    }

    public void setL_SUPPKEY(Long l_SUPPKEY) {
        L_SUPPKEY = l_SUPPKEY;
    }

    public Long getL_LINENUMBER() {
        return L_LINENUMBER;
    }

    public void setL_LINENUMBER(Long l_LINENUMBER) {
        L_LINENUMBER = l_LINENUMBER;
    }

    public BigDecimal getL_QUANTITY() {
        return L_QUANTITY;
    }

    public void setL_QUANTITY(BigDecimal l_QUANTITY) {
        L_QUANTITY = l_QUANTITY;
    }

    public BigDecimal getL_EXTENDEDPRICE() {
        return L_EXTENDEDPRICE;
    }

    public void setL_EXTENDEDPRICE(BigDecimal l_EXTENDEDPRICE) {
        L_EXTENDEDPRICE = l_EXTENDEDPRICE;
    }

    public BigDecimal getL_DISCOUNT() {
        return L_DISCOUNT;
    }

    public void setL_DISCOUNT(BigDecimal l_DISCOUNT) {
        L_DISCOUNT = l_DISCOUNT;
    }

    public BigDecimal getL_TAX() {
        return L_TAX;
    }

    public void setL_TAX(BigDecimal l_TAX) {
        L_TAX = l_TAX;
    }

    public String getL_RETURNFLAG() {
        return L_RETURNFLAG;
    }

    public void setL_RETURNFLAG(String l_RETURNFLAG) {
        L_RETURNFLAG = l_RETURNFLAG;
    }

    public String getL_LINESTATUS() {
        return L_LINESTATUS;
    }

    public void setL_LINESTATUS(String l_LINESTATUS) {
        L_LINESTATUS = l_LINESTATUS;
    }

    public String getL_SHIPDATE() {
        return L_SHIPDATE;
    }

    public void setL_SHIPDATE(String l_SHIPDATE) {
        L_SHIPDATE = l_SHIPDATE;
    }

    public Long getL_COMMITDATE() {
        return L_COMMITDATE;
    }

    public void setL_COMMITDATE(Long l_COMMITDATE) {
        L_COMMITDATE = l_COMMITDATE;
    }

    public Long getL_RECEIPTDATE() {
        return L_RECEIPTDATE;
    }

    public void setL_RECEIPTDATE(Long l_RECEIPTDATE) {
        L_RECEIPTDATE = l_RECEIPTDATE;
    }

    public String getL_SHIPINSTRUCT() {
        return L_SHIPINSTRUCT;
    }

    public void setL_SHIPINSTRUCT(String l_SHIPINSTRUCT) {
        L_SHIPINSTRUCT = l_SHIPINSTRUCT;
    }

    public String getL_SHIPMODE() {
        return L_SHIPMODE;
    }

    public void setL_SHIPMODE(String l_SHIPMODE) {
        L_SHIPMODE = l_SHIPMODE;
    }

    public String getL_COMMENT() {
        return L_COMMENT;
    }

    public void setL_COMMENT(String l_COMMENT) {
        L_COMMENT = l_COMMENT;
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineItem)) return false;
        LineItem lineItem = (LineItem) o;
        return Objects.equals(L_ORDERKEY, lineItem.L_ORDERKEY) &&
               Objects.equals(L_PARTKEY, lineItem.L_PARTKEY) &&
               Objects.equals(L_SUPPKEY, lineItem.L_SUPPKEY) &&
               Objects.equals(L_LINENUMBER, lineItem.L_LINENUMBER) &&
               Objects.equals(L_QUANTITY, lineItem.L_QUANTITY) &&
               Objects.equals(L_EXTENDEDPRICE, lineItem.L_EXTENDEDPRICE) &&
               Objects.equals(L_DISCOUNT, lineItem.L_DISCOUNT) &&
               Objects.equals(L_TAX, lineItem.L_TAX) &&
               Objects.equals(L_RETURNFLAG, lineItem.L_RETURNFLAG) &&
               Objects.equals(L_LINESTATUS, lineItem.L_LINESTATUS) &&
               Objects.equals(L_SHIPDATE, lineItem.L_SHIPDATE) &&
               Objects.equals(L_COMMITDATE, lineItem.L_COMMITDATE) &&
               Objects.equals(L_RECEIPTDATE, lineItem.L_RECEIPTDATE) &&
               Objects.equals(L_SHIPINSTRUCT, lineItem.L_SHIPINSTRUCT) &&
               Objects.equals(L_SHIPMODE, lineItem.L_SHIPMODE) &&
               Objects.equals(L_COMMENT, lineItem.L_COMMENT);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(L_ORDERKEY, L_PARTKEY, L_SUPPKEY, L_LINENUMBER, L_QUANTITY,
                L_EXTENDEDPRICE, L_DISCOUNT, L_TAX, L_RETURNFLAG, L_LINESTATUS,
                L_SHIPDATE, L_COMMITDATE, L_RECEIPTDATE, L_SHIPINSTRUCT, L_SHIPMODE, L_COMMENT);
    }

    // toString
    @Override
    public String toString() {
        return "LineItem{" +
                "L_ORDERKEY=" + L_ORDERKEY +
                ", L_PARTKEY=" + L_PARTKEY +
                ", L_SUPPKEY=" + L_SUPPKEY +
                ", L_LINENUMBER=" + L_LINENUMBER +
                ", L_QUANTITY=" + L_QUANTITY +
                ", L_EXTENDEDPRICE=" + L_EXTENDEDPRICE +
                ", L_DISCOUNT=" + L_DISCOUNT +
                ", L_TAX=" + L_TAX +
                ", L_RETURNFLAG='" + L_RETURNFLAG + '\'' +
                ", L_LINESTATUS='" + L_LINESTATUS + '\'' +
                ", L_SHIPDATE='" + L_SHIPDATE + '\'' +
                ", L_COMMITDATE=" + L_COMMITDATE +
                ", L_RECEIPTDATE=" + L_RECEIPTDATE +
                ", L_SHIPINSTRUCT='" + L_SHIPINSTRUCT + '\'' +
                ", L_SHIPMODE='" + L_SHIPMODE + '\'' +
                ", L_COMMENT='" + L_COMMENT + '\'' +
                '}';
    }
}
