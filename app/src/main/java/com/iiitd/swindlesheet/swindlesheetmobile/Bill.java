package com.iiitd.swindlesheet.swindlesheetmobile;

/*
Model class for Bill database
 */
public class Bill {
    // various column field of database
    private String sender,landno,sms_date,bill_date,due_date,recv_date,rec,amount,body;

    public Bill(String sender, String landno, String sms_date, String bill_date, String due_date, String recv_date,String body, String rec, String amount) {
        this.sender = sender;
        this.landno = landno;
        this.sms_date = sms_date;
        this.bill_date = bill_date;
        this.due_date = due_date;
        this.recv_date = recv_date;
        this.rec = rec;
        this.amount = amount;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLandno() {
        return landno;
    }

    public void setLandno(String landno) {
        this.landno = landno;
    }

    public String getSms_date() {
        return sms_date;
    }

    public void setSms_date(String sms_date) {
        this.sms_date = sms_date;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getRecv_date() {
        return recv_date;
    }

    public void setRecv_date(String recv_date) {
        this.recv_date = recv_date;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
