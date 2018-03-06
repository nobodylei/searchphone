package com.lei.searchphone.mvp.impl;

import android.content.Context;

/**
 * Created by yanle on 2018/3/6.
 */

public class BasePresenter {
    Context mContext;
    public void attach(Context context) {
        mContext = context;
    }
    public void onPause() {}
    public void onResume() {}
    public void onDestroy() {
        mContext = null;
    }
}
