package com.owl.chatstory.common.util.network;


import com.owl.chatstory.common.util.network.request.UserRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.data.usersource.model.UserResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lebron on 2017/7/31.
 */

public interface ApiService {
    @GET("version/config")
    Observable<BaseResponse<UpdateModel>> checkUpdate();

    @GET("ifiction/category")
    Observable<BaseArrayResponse<CategoryModel>> getCategoryList();

    @GET("ifiction/chapter")
    Observable<BaseResponse<FictionModel>> getFictionDetail(@Query("id") String id);

    @GET("ifiction/list")
    Observable<BaseArrayResponse<FictionDetailModel>> getFictionList(@QueryMap Map<String, String> map);

    @POST("login/empower")
    Observable<BaseResponse<UserResponse>> signUpOrIn(@Body UserRequest request);

    @GET("history/add")
    Observable<BaseResponse> addToHistory(@Query("token") String token, @Query("book_id") String fictionId);

    @GET("history/list")
    Observable<BaseArrayResponse<FictionDetailModel>> getHistoryList(@QueryMap Map<String, String> map);

    @GET("history/clear")
    Observable<BaseResponse> clearHistory(@Query("token") String token);

}
