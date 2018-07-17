package com.iiitd.swindlesheet.swindlesheetmobile;


/*
model class fro expenditure database
 */
public class Spend {
    private String sender, sms_date, body, ttype, cord, rec, amount, latt, longi;

    public Spend() {
    }

    public Spend(String sender, String sms_date, String body, String ttype, String cord, String rec, String amount, String latt, String longi) {
        this.sender = sender;
        this.sms_date = sms_date;
        this.body = body;
        this.ttype = ttype;
        this.cord = cord;
        this.rec = rec;
        this.amount = amount;
        this.latt = latt;
        this.longi = longi;
    }

    public String getSender() {
        return sender;
    }

    public String getSms_date() {
        return sms_date;
    }

    public String getBody() {
        return body;
    }

    public String getTtype() {
        return ttype;
    }

    public String getCord() {
        return cord;
    }

    public String getRec() {
        return rec;
    }

    public String getAmount() {
        return amount;
    }

    public String getLatt() {
        return latt;
    }

    public String getLongi() {
        return longi;
    }
}