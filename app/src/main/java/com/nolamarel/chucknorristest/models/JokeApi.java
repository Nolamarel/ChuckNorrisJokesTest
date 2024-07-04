package com.nolamarel.chucknorristest.models;

import com.google.gson.annotations.SerializedName;

public class JokeApi {

    @SerializedName("icon_url")
    public String iconUrl;
    @SerializedName("id")
    public String jokeId;
    @SerializedName("url")
    public String url;
    @SerializedName("value")
    public String value;

    public JokeApi(String iconUrl, String jokeId, String url, String value) {
        this.iconUrl = iconUrl;
        this.jokeId = jokeId;
        this.url = url;
        this.value = value;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getJokeId() {
        return jokeId;
    }

    public void setJokeId(String jokeId) {
        this.jokeId = jokeId;
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
}
