package com.yahoo.googleimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jhkao on 8/5/15.
 */
public class ImageSearchSettings implements Parcelable {
    private int size;
    private int color;
    private int type;
    private String site;

    public ImageSearchSettings() {
        this.size = 0;
        this.color = 0;
        this.type = 0;
        this.site = "";
    }

    public ImageSearchSettings(Parcel in) {
        this.size = in.readInt();
        this.color = in.readInt();
        this.type = in.readInt();
        this.site = in.readString();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
        dest.writeInt(color);
        dest.writeInt(type);
        dest.writeString(site);
    }

    public static final Creator<ImageSearchSettings> CREATOR = new Creator<ImageSearchSettings>() {

        @Override
        public ImageSearchSettings createFromParcel(Parcel source) {
            return new ImageSearchSettings(source);
        }

        @Override
        public ImageSearchSettings[] newArray(int size) {
            return new ImageSearchSettings[0];
        }
    };
}
