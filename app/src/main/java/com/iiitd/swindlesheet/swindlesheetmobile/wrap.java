package com.iiitd.swindlesheet.swindlesheetmobile;

import android.widget.ImageView;

/*
model class for the appbar of the navigation drawer
 */
public class wrap {

    ImageView iv;
    String url;

    wrap(){

    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageView getIv() {
        return iv;
    }

    public String getUrl() {
        return url;
    }
}
