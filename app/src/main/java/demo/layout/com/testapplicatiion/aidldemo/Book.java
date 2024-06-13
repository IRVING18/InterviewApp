package demo.layout.com.testapplicatiion.aidldemo;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.system.ErrnoException;
import android.system.Os;
import android.system.StructPollfd;
import android.view.View;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import androidx.annotation.NonNull;

/**
 * 有道领世
 * Description: 注意和aidl中的Book.aidl要在同一报名下才行
 * Created by wangzheng on 2024/3/20 20:10
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
public class Book implements Parcelable {

    private String name;
    private int type;

    public Book(String name, int type) {
        this.name = name;
        this.type = type;
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {

        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }


        public Book[] newArray(int size) {
            return new Book[size];
        }
    };


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(type);
    }


    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", type=" + type +
                "}\n";
    }
}