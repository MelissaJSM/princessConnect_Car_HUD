package com.example.pcr_hud;

import android.graphics.drawable.Drawable;

public class RecyclerViewItem {
    private String mainTitle;
    private String subTitle;

    private String lat;
    private String lon;

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setlat(String latTitle) {
        this.lat = latTitle;
    }

    public void setlon(String lonTitle) {
        this.lon = lonTitle;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}