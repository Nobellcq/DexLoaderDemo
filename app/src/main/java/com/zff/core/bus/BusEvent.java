package com.zff.core.bus;

import android.util.Log;

public class BusEvent<T> {

    private int type;

    private T data;

    public BusEvent(int type) {
        this(type, null);
    }

    public BusEvent(int type, T data) {
        this.type = type;
        this.data = data;
        Log.d("wdnmd", "BusEvent: 正在被设置");
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        Log.d("wdnmd", "getType: 正在被getType");
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
