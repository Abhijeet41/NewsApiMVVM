
package com.abhi41.newsapi.response;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Source {

    @Expose
    private String id;
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
