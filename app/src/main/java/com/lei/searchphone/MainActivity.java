package com.lei.searchphone;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lei.searchphone.model.Phone;
import com.lei.searchphone.mvp.MvpMainView;
import com.lei.searchphone.mvp.impl.MainPresenter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, MvpMainView {

    private EditText input_phone;
    private Button btn_search;
    private TextView result_phone;
    private TextView result_proince;
    private TextView result_type;
    private TextView result_carrier;

    private MainPresenter mainPresenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btn_search.setOnClickListener(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);

    }

    private void initView() {
        input_phone = findViewById(R.id.input_phone);
        btn_search = findViewById(R.id.btn_seatch);
        result_phone = findViewById(R.id.result_phone);
        result_proince = findViewById(R.id.result_province);
        result_type = findViewById(R.id.result_type);
        result_carrier = findViewById(R.id.result_carrier);
    }

    @Override
    public void onClick(View v) {
        //暂时不写逻辑
        //Toast.makeText(this, "点击了查询", Toast.LENGTH_SHORT).show();
        mainPresenter.sarchPhoneInfo(input_phone.getText().toString());
    }

    //mvpMainView接口的方法
    @Override
    public void showLoading() {
        if(progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", "正在加载...");
        } else if(progressDialog.isShowing()) {
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainPresenter.getPhoneInfo();
        result_phone.setText("手机号码:" + phone.getTelString());
        result_proince.setText("省份:" + phone.getProvince());
        result_type.setText("运营商:" + phone.getCatName());
        result_carrier.setText("归属运营商:" + phone.getCarrier());

    }
}
