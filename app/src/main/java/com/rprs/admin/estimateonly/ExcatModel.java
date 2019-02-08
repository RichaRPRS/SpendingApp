package com.rprs.admin.estimateonly;

public class ExcatModel {

    String id,name,pay_type;
    byte[] img;

    public ExcatModel(String id,String name, byte[] img) {
        this.id=id;
        this.name = name;
        this.img = img;
    }

    public ExcatModel(String id, String name, String pay_type, byte[] img) {
        this.id = id;
        this.name = name;
        this.pay_type = pay_type;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
