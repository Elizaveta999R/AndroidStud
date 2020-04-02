package ru.tpu.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgressBar implements Parcelable {
    public String title;
    public String score;

    public ProgressBar(String t, String s) {
        this.title = t;
        this.score = s;
    }

    public ProgressBar(Parcel in) {
        title = in.readString();
        score = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(score);

    }

    public static final Creator<ProgressBar> CREATOR = new Creator<ProgressBar>() {
        @Override
        public ProgressBar createFromParcel(Parcel in) {
            return new ProgressBar(in);
        }

        @Override
        public ProgressBar[] newArray(int size) {
            return new ProgressBar[size];
        }
    };
}

