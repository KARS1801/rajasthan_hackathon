package com.example.nishant.fire_test;

/**
 * Created by nishant on 29/11/17.
 */

public class ListItem {

    private String count;
    private String item;
    private String mUserId;


    public ListItem(String item,String count,String mUserId){
        this.item=item;
        this.count=count;
        this.mUserId=mUserId;
    }

    public void setCount(String count){
        this.count=count;
    }
    public String getCount(){
        return count;
    }
    public void setitem(String item){
        this.item=item;
    }
    public String getitem(){
        return item;
    }

    public void getmUserId(String mUserId){ this.mUserId=mUserId;}
    public String setmUserId(){ return mUserId;}
}
