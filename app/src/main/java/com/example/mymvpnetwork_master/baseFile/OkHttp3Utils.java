package com.example.mymvpnetwork_master.baseFile;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @package: com.example.mymvpnetwork_master.baseFile
 * 创建人： created by zlj
 * 时间：2022/03/27 17
 */
public class OkHttp3Utils {
    private OkHttpClient mOkHttpClient;
    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //添加网络连接器
                    //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
//                    .cache(cache)//设置缓存
                    .retryOnConnectionFailure(true)//自动重试
                    .build();
        }
        return mOkHttpClient;
    }


    //日志
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message);
        }
    });
}
