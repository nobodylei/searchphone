package com.lei.searchphone.mvp;

/**
 * Created by yanle on 2018/3/6.
 */

public interface MvpMainView extends MvpLoadingView{
    //显示信息
    void showToast(String msg);
    //更新界面
    void updateView();

}
