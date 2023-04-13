package com.example.basicnote.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //@ColumnInfo(name = "title") Nếu muốn đặt tên Column thì xài @ColumnInfo và truyền vào name, không thì sẽ lấy mặc định tên thuộc tính
    private String title;
    private String desc;
    private Boolean done;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Note(String title, String desc, Boolean done) {
        this.title = title;
        this.desc = desc;
        this.done = done;
    }
}
