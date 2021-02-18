package com.example.courseregistration.connection;

import com.google.gson.annotations.SerializedName;

public class ServiceInterface{
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("usrName")
    private String name;
    @SerializedName("usrID")
    private String id;
    @SerializedName("data")
    private String data;
    @SerializedName("major")
    private String major;
    @SerializedName("grade")
    private String grade;
    @SerializedName("credit")
    private int credit;
    @SerializedName("reserve")
    private int reserve;

    public String getAccessToken(){ return accessToken; }
    public String getUsrName(){ return name; }
    public String getUsrID(){ return id; }
    public String getData () {return data;}
    public String getMajor(){ return major; }
    public String getGrade(){ return grade; }
    public int getCredit () {return credit;}
    public int getReserve(){ return reserve; }


}


