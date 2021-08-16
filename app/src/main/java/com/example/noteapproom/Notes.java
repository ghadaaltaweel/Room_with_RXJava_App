package com.example.noteapproom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Notes {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "body")
    private String body;

    public Notes()
    {

    }
    public Notes(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
