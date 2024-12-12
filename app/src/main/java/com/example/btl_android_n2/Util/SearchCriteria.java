package com.example.btl_android_n2.Util;

public class SearchCriteria {
    private String location;
    private int peopleNumber;

    public SearchCriteria(String location, int peopleNumber) {
        this.location = location;
        this.peopleNumber = peopleNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }
}

