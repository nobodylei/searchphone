package com.lei.searchphone.mvp.impl;


import com.google.gson.Gson;
import com.lei.searchphone.business.HttpUtil;
import com.lei.searchphone.model.Phone;
import com.lei.searchphone.mvp.MvpMainView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanle on 2018/3/6.
 */

public class MainPresenter extends BasePresenter{
    private String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private MvpMainView mvpMainView;
    Phone mPhone;
    public MainPresenter(MvpMainView mainView) {
        mvpMainView = mainView;
    }
    //获取phone手机信息的方法
    public Phone getPhoneInfo() {
        return mPhone;
    }

    public void sarchPhoneInfo(String phone) {
        if(phone.length() != 11) {//校验手机号码
            mvpMainView.showToast("请输入正确的手机号码");
            return;
        }
        mvpMainView.showLoading();
        //http请求的处理逻辑
        sendHttp(phone);
    }
    //负责发送的私有方法
    private void sendHttp(String phone) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tel",phone);
        HttpUtil httpUtil = new HttpUtil(new HttpUtil.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index, json.length());
                //使用JSONObject
                //mPhone = parseModelWithOregJson(json);
                //Gson
                //mPhone = parseModelWithGson(json);
                //FastJson
                mPhone = parseModelWithFastJaon(json);

                mvpMainView.hidenLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUtil.sendGetHttp(mUrl,map);
    }
    //解析JSON
    private Phone parseModelWithOregJson(String json) {
        Phone phone = new Phone();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);
            value = jsonObject.getString("province");//省份
            phone.setProvince(value);
            value = jsonObject.getString("catName");//运营商
            phone.setCatName(value);
            value = jsonObject.getString("carrier");//归属运营商
            phone.setCarrier(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }

    private Phone parseModelWithGson(String json) {
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json, Phone.class);
        return phone;
    }

    private Phone parseModelWithFastJaon(String json) {
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json, Phone.class);
        return phone;
    }

}
