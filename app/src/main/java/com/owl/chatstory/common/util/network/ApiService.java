package com.owl.chatstory.common.util.network;


import com.owl.chatstory.common.util.network.request.UserRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.RoleListRequest;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.data.usersource.model.UserResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lebron on 2017/7/31.
 */

public interface ApiService {
    /**
     * 检查更新
     *
     * @return
     */
    @GET("version/config")
    Observable<BaseResponse<UpdateModel>> checkUpdate();

    /**
     * 获取分类列表
     *
     * @return
     */
    @GET("ifiction/category")
    Observable<BaseArrayResponse<CategoryModel>> getCategoryList();

    /**
     * 获取小说详情
     */
    @GET("ifiction/detail")
    Observable<BaseResponse<FictionDetailModel>> getFictionDetail(@Query("id") String id);


    /**
     * 获取小说具体内容
     *
     * @return
     */
    @GET("ifiction/chapter")
    Observable<BaseResponse<FictionModel>> getChapterDetail(@Query("id") String id);

    /**
     * 获取分类下小说列表
     *
     * @return
     */
    @GET("ifiction/list")
    Observable<BaseArrayResponse<FictionDetailModel>> getFictionList(@QueryMap Map<String, String> map);

    /**
     * 登录注册
     *
     * @return
     */
    @POST("login/empower")
    Observable<BaseResponse<UserResponse>> signUpOrIn(@Body UserRequest request);

    /**
     * 添加到阅读记录
     *
     * @return
     */
    @GET("history/add")
    Observable<BaseResponse> addToHistory(@Query("token") String token, @Query("book_id") String fictionId);

    /**
     * 获取阅读记录
     *
     * @return
     */
    @GET("history/list")
    Observable<BaseArrayResponse<FictionDetailModel>> getHistoryList(@QueryMap Map<String, String> map);

    /**
     * 清除所有阅读记录
     *
     * @return
     */
    @GET("history/clear")
    Observable<BaseResponse> clearHistory(@Query("token") String token);

    /**
     * 更新小说基本信息
     *
     * @param model
     * @return
     */
    @POST("ifiction/manageifiction")
    Observable<BaseResponse<FictionDetailModel>> updateFictionBasicInfo(@Body FictionDetailModel model);

    /**
     * 获取用户创作的小说列表
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("ifiction/manageifictionlist")
    Observable<BaseArrayResponse<FictionDetailModel>> getUserFictionList(@Field("token") String token, @Field("language") String language);

    /**
     * 添加章节
     *
     * @param model
     * @return
     */
    @POST("ifiction/managechapter")
    Observable<BaseResponse> publishChapter(@Body FictionModel model);

    /**
     * 获取小说角色列表
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("actor/managelist")
    Observable<BaseArrayResponse<UserModel>> getRoleList(@Field("token") String token, @Field("type") String language, @Field("ifiction_id") String id);

    /**
     * 上传角色列表
     *
     * @param token
     * @param request
     * @return
     */
    @POST("actor/manage")
    Observable<BaseArrayResponse<UserModel>> updateRoleList(@Field("token") String token, @Body RoleListRequest request);

}
