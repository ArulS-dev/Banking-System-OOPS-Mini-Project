package com.example.demo4;

public class Transactions {
    private int  colAmount;
    private String colUnique, colType;

    public Transactions(String colType, String colUnique, int colAmount){
        this.colAmount = colAmount;
        this.colType = colType;
        this.colUnique = colUnique;
    }
    public int getColAmount(){
        return colAmount;
    }
    public String getColType(){
        return colType;
    }
    public String getColUnique(){
        return colUnique;
    }
}
