package com.rprs.admin.estimateonly;

import java.io.Serializable;

public class TransModel implements Serializable{

    int amount;
    String tr_id, date, note,catg_id,categ_name,pay_id1;
    byte[] img;

    public TransModel() {
    }

    public TransModel(String tr_id,int amount, String categ_id,String categ_name, String pay_id1, String date, String note,byte[] img) {
        this.tr_id=tr_id;
        this.amount = amount;
        this.catg_id = categ_id;
        this.categ_name=categ_name;
        this.pay_id1 = pay_id1;
        this.date = date;
        this.note = note;
        this.img=img;
    }

    public String getTr_id() {
        return tr_id;
    }

    public void setTr_id(String tr_id) {
        this.tr_id = tr_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(String catg_id) {
        this.catg_id = catg_id;
    }

    public String getCateg_name() {
        return categ_name;
    }

    public void setCateg_name(String categ_name) {
        this.categ_name = categ_name;
    }

    public String getPay_id1() {
        return pay_id1;
    }

    public void setPay_id1(String pay_id1) {
        this.pay_id1 = pay_id1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
