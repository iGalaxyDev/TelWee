package com.hurhco.tvsafari.models;

public class MainModel {
    public String pic;
    public String name;
    public int id;

    public MainModel(String pic, String name, int id) {
        this.pic = pic;
        this.name = name;
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

}