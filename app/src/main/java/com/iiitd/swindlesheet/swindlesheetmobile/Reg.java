package com.iiitd.swindlesheet.swindlesheetmobile;

/*
Model class for Regex expression used the application which is read from a Assets Resources
 */
public class Reg {
    String regEx,bank,ttype,cod,rec;



    public Reg(String regEx, String bank, String ttype, String cod, String rec) {
        this.regEx = regEx;
        this.bank = bank;
        this.ttype = ttype;
        this.cod = cod;
        this.rec=rec;
    }

    @Override
    public String toString() {
        return "Reg{" +
                "regEx='" + regEx + '\'' +
                ", bank='" + bank + '\'' +
                ", ttype='" + ttype + '\'' +
                ", cod='" + cod + '\'' +
                ", rec='" + rec + '\'' +
                '}';
    }
}
