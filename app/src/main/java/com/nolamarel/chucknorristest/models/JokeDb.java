package com.nolamarel.chucknorristest.models;

public class JokeDb {
    private String icon_url;
    private int id;
    private String url;
    private String value;
    private String jokeId;

    public JokeDb(String icon_url, int id, String url, String value, String jokeId) {
        this.icon_url = icon_url;
        this.id = id;
        this.url = url;
        this.value = value;
        this.jokeId = jokeId;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getJokeId() {
        return jokeId;
    }

    public void setJokeId(String jokeId) {
        this.jokeId = jokeId;
    }
}
