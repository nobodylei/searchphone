package com.lei.searchphone.business;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yanle on 2018/3/6.
 */

public class HttpUtil {
    private String mUrl;
    private Map<String, String> mParam;
    private HttpResponse mHttpResponse;

    private Handler myHandel = new Handler(Looper.getMainLooper());

    private final OkHttpClient client = new OkHttpClient();

    //回调接口，拿到数据
    public interface HttpResponse {
        //请求成功
        void onSuccess(Object object);
        //请求失败
        void onFail(String error);
    }

    public HttpUtil(HttpResponse response) {
        mHttpResponse = response;
    }

    //负责发送post的请求方法
    public void sendPostHttp(String url, Map<String, String> param) {
        sendHttp(url, param, true);
    }
    //负责发送get的请求方法
    public void sendGetHttp(String url, Map<String, String> param) {
        sendHttp(url, param, false);
    }
    //私有的发送方法
    private void sendHttp(String url, Map<String, String> param, boolean isPost) {
        mUrl = url;
        mParam = param;
        //编写http请求逻辑
        run(isPost);
    }

    private void run(boolean isPost) {
        //request请求创建
        Request request = createRequest(isPost);
        //创建请求队列
        client.newCall(request).enqueue(new Callback() {
            @Override//请求失败
            public void onFailure(Call call, IOException e) {
                if(mHttpResponse != null) {
                    myHandel.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(mHttpResponse == null) return;
                myHandel.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful()) {
                            mHttpResponse.onFail("请求失败:code" + response);
                        } else {
                            try {
                                mHttpResponse.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                mHttpResponse.onFail("结果转换失败");
                            }
                        }
                    }
                });
            }
        });
    }
    // /创建Request请求
    private Request createRequest(boolean isPost) {
        Request request;
        if (isPost) {//判断是否为Post请求，
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            //设置请求类型
            requestBodyBuilder.setType(MultipartBody.FORM);
            //遍历map请求参数
            Iterator<Map.Entry<String, String>> iterator = mParam.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entity = iterator.next();
                //参数添加到请求数据中
                requestBodyBuilder.addFormDataPart(entity.getKey(), entity.getValue());
            }
            request = new Request.Builder().url(mUrl)
                    .post(requestBodyBuilder.build()).build();
        } else {
            String urlStr = mUrl + "?" + MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }

    //拼接地址
    private String MapParamToString(Map<String, String> param) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey()).
                    append("=").
                    append(entry.getValue()).
                    append("&");
        }
        //最后一个符号去掉
        String str = builder.toString().substring(0, builder.length() - 1);
        return str;
    }
}
