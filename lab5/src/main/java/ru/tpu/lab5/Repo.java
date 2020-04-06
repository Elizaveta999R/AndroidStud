package ru.tpu.lab5;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Repo implements Parcelable{

    public String fullName;
    public String description;
    public String url;
    public List<String> issueTitle;
    public List<String> commitsMessage;
    public Repo(String fullName, String description,String url){// List<String> issue, List<String> commits) {
        this.fullName= fullName;
        this.description = description;
        this.url = url;
    }

    protected Repo(Parcel in) {
        fullName = in.readString();
        description = in.readString();
        url = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(description);
        dest.writeString(url);

     }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {

        @Override
        public Repo createFromParcel(Parcel source) {
            return new Repo(source);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };
}
