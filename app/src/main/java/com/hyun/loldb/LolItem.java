package com.hyun.loldb;

public class LolItem {

    private int No;
    private  String itemname;
    private  String price;
    private  String grade;

    public LolItem(int no, String itemname, String price, String grade) {
        this.No = no;
        this.itemname = itemname;
        this.price = price;
        this.grade = grade;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getItemname() {

        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}
