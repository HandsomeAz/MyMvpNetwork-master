package com.example.mymvpnetwork_master.activity.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mymvpnetwork_master.R;
import com.example.mymvpnetwork_master.base.BaseActivity;
import com.example.mymvpnetwork_master.base.ViewInject;
import com.example.mymvpnetwork_master.bean.TestBean;

import butterknife.BindView;

@ViewInject(mainLayoutId = R.layout.activity_test)
public class TestActivity extends BaseActivity implements ITestActivityContract.IView{
    private ITestActivityContract.IPresenter mPresenter = new TestActivityPresenter(this);

    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindView(R.id.tv_test2)
    TextView tv_test2;
    @Override
    public void afterBindView() {

    }

    @Override
    public void processData() {
        mPresenter.getUpdateInfo(TestActivity.this);
    }

    @Override
    public void UpdateInfo(TestBean stringHttpBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_test.setText(stringHttpBean.toString());
            }
        });

        if (stringHttpBean.getCode() == 2000){
            if (stringHttpBean.getData()!=null){
                mPresenter.downloadApk(stringHttpBean.getData());
            }
        }
    }

    @Override
    public void downloadState(boolean isSuccess) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess){
                    tv_test2.setText("下载成功！");
                }else {
                    tv_test2.setText("下载失败！");
                }
            }
        });
    }
}