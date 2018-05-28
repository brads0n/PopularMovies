package com.example.android.popularmovies.model;

import java.io.Serializable;
import java.net.URL;

public class Trailer implements Serializable {
    String id;
    String title;
    URL url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
