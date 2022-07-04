package com.hurhco.tvsafari.models;

public class SubModel {
    public int channelId;
    public int sortId;
    public String link;
    public String channelName;
    public String pic;
    public String des;
    public String country;
    public String topic;
    public String frequency;
    public String referer;

    public SubModel(int channelId, String link, String channelName, int sortId, String pic, String des, String country, String topic, String frequency, String referer) {
        this.channelId = channelId;
        this.sortId = sortId;
        this.link = link;
        this.channelName = channelName;
        this.pic = pic;
        this.des = des;
        this.country = country;
        this.topic = topic;
        this.frequency = frequency;
        this.referer = referer;
    }

    public String getLink() {
        return link;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getPic () {
        return pic;
    }

    public String getDes() {
        return des;
    }

    public String getCountry() {
        return country;
    }

    public String getTopic() {
        return topic;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getReferer() {
        return referer;
    }
}
