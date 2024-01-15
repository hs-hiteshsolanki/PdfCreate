package com.g.pdfcreate;

import java.util.jar.Attributes;

public class Model {
    String NAME;
    String NUMBER;
    String CREDIT;
    String DEBIT;

    public Model(String NAME, String NUMBER, String CREDIT, String DEBIT) {
        this.NAME = NAME;
        this.NUMBER = NUMBER;
        this.CREDIT = CREDIT;
        this.DEBIT = DEBIT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getCREDIT() {
        return CREDIT;
    }

    public void setCREDIT(String CREDIT) {
        this.CREDIT = CREDIT;
    }

    public String getDEBIT() {
        return DEBIT;
    }

    public void setDEBIT(String DEBIT) {
        this.DEBIT = DEBIT;
    }
}
