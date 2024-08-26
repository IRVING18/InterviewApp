package com.wz.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/26 11:08
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
public class MsgBean implements Parcelable {
    public int id;
    public String msg;
    public int type;
    public float baseType;//基本类型
    public List<String> listType;//list类型
    public Map<String, String> mapType; // map类型

    public MsgBean(){}

    protected MsgBean(Parcel in) {
        id = in.readInt();
        msg = in.readString();
        type = in.readInt();
        baseType = in.readFloat();
        listType = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(msg);
        dest.writeInt(type);
        dest.writeFloat(baseType);
        dest.writeStringList(listType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MsgBean> CREATOR = new Creator<MsgBean>() {
        @Override
        public MsgBean createFromParcel(Parcel in) {
            return new MsgBean(in);
        }

        @Override
        public MsgBean[] newArray(int size) {
            return new MsgBean[size];
        }
    };
}
