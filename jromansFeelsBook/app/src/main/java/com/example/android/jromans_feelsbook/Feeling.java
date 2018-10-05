package com.example.android.jromans_feelsbook;

import java.util.Date;

public class Feeling {
    private String emotion;
    private String date;
    private String comment;

    public Feeling(String emotion) {
        this.emotion = emotion;
        this.date = new Date(System.currentTimeMillis()).toString();
    }
    public Feeling(String emotion, String date, String comment) {
        this.emotion = emotion;
        this.date = date;
        this.comment = comment;
    }

    public Feeling(String emotion, String date) {
        this.emotion = emotion;
        this.date = date;
    }

    public String getEmotion() {
        return this.emotion;
    }

    public String getDate() {
        return this.date;
    }

    public String getComment() {
        return this.comment;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
