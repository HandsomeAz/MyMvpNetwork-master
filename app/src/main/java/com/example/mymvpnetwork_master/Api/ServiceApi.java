package com.example.mymvpnetwork_master.Api;


import com.example.mymvpnetwork_master.bean.TestBean;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @package: com.example.mymvpnetwork_master.Api
 * 创建人： created by zlj
 * 时间：2022/03/27 17
 */
public interface ServiceApi {
    @GET("/version-data/update?")
    Observable<TestBean> getUpdateInfo(@QueryMap HashMap<String, Object> map);

    @GET
    @Streaming
    Observable<ResponseBody> downloadApk(@Url String url);

}
