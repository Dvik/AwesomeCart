package com.transferret.whizzbuy.model;

/**
 * Created by Divya on 1/7/2016.
 */
public class Tasks {

    public String name;
    public Boolean isChecked;

    public Tasks(String name, Boolean isChecked)
    {
        this.name = name;
        this.isChecked = isChecked;
    }


    public void setChecked(Boolean b)
    {
        this.isChecked = b;
    }
    public void setName(String n)
    {
        this.name = n;
    }


}
