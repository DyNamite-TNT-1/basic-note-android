package com.example.basicnote.models;

public class Note {
    private String id;
    private String title;
    private String desc;
    private Boolean done;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Note() {};

    public Note(String id, String title, String desc, Boolean done) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.done = done;
    }
}
