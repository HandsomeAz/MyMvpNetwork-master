package com.example.mymvpnetwork_master.activity.Test;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mymvpnetwork_master.base.BaseMvpPresenter;
import com.example.mymvpnetwork_master.baseFile.UserNetWork;
import com.example.mymvpnetwork_master.bean.TestBean;
import com.example.mymvpnetwork_master.utils.MyFileUtils;
import com.example.mymvpnetwork_master.utils.MyToast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @package: com.example.mymvpnetwork_master.activity.Test
 * 创建人： created by zlj
 * 时间：2022/03/27 20
 */
public class TestActivityPresenter extends BaseMvpPresenter<ITestActivityContract.IView> implements ITestActivityContract.IPresenter{
    private Activity mActivity;
    public TestActivityPresenter(ITestActivityContract.IView view) {
        super(view);
    }

    @Override
    protected ITestActivityContract.IView getEmptyView() {
        return ITestActivityContract.emptyView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void getUpdateInfo(Activity activity) {
        this.mActivity = activity;
        UserNetWork userNetWork = new UserNetWork();
        getUpdate(userNetWork,activity);
    }

    @Override
    public void downloadApk(String url) {
        UserNetWork userNetWork = new UserNetWork();
        getDownloadApk(userNetWork,url);

    }

    private boolean isSuccess =false;
    private void getDownloadApk(UserNetWork userNetWork, String url) {
        isSuccess =false;
        userNetWork.toDownloadApk(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //用于Activity销毁时清除所有的网络请求
                addDisposable(d);
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeFile(responseBody);
                    }
                }).start();


            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(mActivity, "下载出错", Toast.LENGTH_SHORT).show();
                Log.d("TestActivity_log",e.toString());
            }

            @Override
            public void onComplete() {

            }
        }, url);
    }

    private void getUpdate(UserNetWork userNetWork, Activity mActivity) {
        userNetWork.toGetUpdateInfo(new Observer<TestBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //用于Activity销毁时清除所有的网络请求
                addDisposable(d);
            }

            @Override
            public void onNext(@NonNull TestBean stringHttpBean) {
                Toast.makeText(mActivity, "请求成功", Toast.LENGTH_SHORT).show();
                Log.d("TestActivity_log",stringHttpBean.toString());
                if (stringHttpBean !=null){
                    getView().UpdateInfo(stringHttpBean);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(mActivity, "请求错误", Toast.LENGTH_SHORT).show();
                Log.d("TestActivity_log",e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void writeFile(ResponseBody responseBody) {

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        File faceDir = MyFileUtils.getNewVersionDirectory();
        File file = new File(faceDir, "Test.apk");
        try {
            in = new BufferedInputStream(responseBody.byteStream());
            out = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) !=-1) {
                out.write(bytes, 0, len);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyToast.showMyToast(mActivity,"下载出错，请重新下载。",Toast.LENGTH_SHORT);
                }
            });
        }

        finally {
            try {
                if (in!=null){
                    in.close();
                }
                if (out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
