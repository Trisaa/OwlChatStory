package com.tap.chatstory.common.util.network;


import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.common.util.network.request.UserRequest;
import com.tap.chatstory.data.chatsource.model.ActorModel;
import com.tap.chatstory.data.chatsource.model.ChapterModel;
import com.tap.chatstory.data.chatsource.model.FictionStatusResponse;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.FictionResponse;
import com.tap.chatstory.data.chatsource.model.OperationRequest;
import com.tap.chatstory.data.chatsource.model.RoleListRequest;
import com.tap.chatstory.data.homesource.model.CategoryModel;
import com.tap.chatstory.data.homesource.model.UpdateModel;
import com.tap.chatstory.data.searchsource.SearchModel;
import com.tap.chatstory.data.usersource.model.MessagesModel;
import com.tap.chatstory.data.usersource.model.TaskModel;
import com.tap.chatstory.data.usersource.model.UserModel;
import com.tap.chatstory.data.usersource.model.UserPageModel;
import com.tap.chatstory.data.usersource.model.UserResponse;
import com.tap.chatstory.data.usersource.model.WalletModel;

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
    @GET("version/info")
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
    @GET("ifiction/detailv2")
    Observable<BaseResponse<FictionDetailModel>> getFictionDetail(@Query("id") String id);

    @GET("ifiction/chapterlist")
    Observable<BaseArrayResponse<ChapterModel>> getFictionChapterList(@Query("ifiction_id") String id, @Query("page") int page, @Query("count") int count);

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
    Observable<BaseResponse<FictionResponse>> updateFictionBasicInfo(@Body FictionDetailModel model);

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
    Observable<BaseArrayResponse<ActorModel>> getRoleList(@Field("token") String token, @Field("language") String language, @Field("ifiction_id") String id);

    /**
     * 上传角色列表
     *
     * @param request
     * @return
     */
    @POST("actor/manage")
    Observable<BaseArrayResponse<ActorModel>> updateRoleList(@Body RoleListRequest request);

    /**
     * 获取章节列表
     *
     * @param token
     * @param language
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("ifiction/managechapterlist")
    Observable<BaseArrayResponse<FictionModel>> getChapterList(@Field("token") String token, @Field("language") String language, @Field("ifiction_id") String id);

    /**
     * 上下架小说
     *
     * @param request
     * @return
     */
    @POST("ifiction/ifictionstatus")
    Observable<BaseResponse> operateFiction(@Body OperationRequest request);

    /**
     * 上下架章节
     *
     * @param request
     * @return
     */
    @POST("ifiction/chapterstatus")
    Observable<BaseResponse> operateChapter(@Body OperationRequest request);

    @FormUrlEncoded
    @POST("collect/add")
    Observable<BaseResponse> collectFiction(@Field("token") String token, @Field("ifiction_id") String fictionId);

    @FormUrlEncoded
    @POST("collect/del")
    Observable<BaseResponse> uncollectFiction(@Field("token") String token, @Field("ifiction_id") String fictionId);

    @POST("collect/list")
    Observable<BaseArrayResponse<FictionDetailModel>> getCollectionList(@Body FictionListRequest request);

    @FormUrlEncoded
    @POST("collect/check")
    Observable<BaseResponse<FictionStatusResponse>> isCollect(@Field("token") String token, @Field("ifiction_id") String fictionId);

    @GET("search/suggest")
    Observable<BaseResponse<SearchModel>> searchData(@Query("kw") String keyword);

    @POST("user/update")
    Observable<BaseResponse> updateUserInfo(@Body UserRequest userRequest);

    @FormUrlEncoded
    @POST("user/info")
    Observable<BaseResponse<UserModel>> getUserInfo(@Field("token") String token);

    @GET("writer/info")
    Observable<BaseResponse<UserPageModel>> getUserPageInfo(@Query("id") String id);

    @GET("writer/works")
    Observable<BaseArrayResponse<FictionDetailModel>> getUserRelatedFictionList(@Query("id") String id, @Query("page") int page, @Query("count") int count);

    @GET("writer/user")
    Observable<BaseResponse<UserPageModel>> getOwnPageInfo();

    @GET("writer/userworks")
    Observable<BaseArrayResponse<FictionDetailModel>> getOwnRelatedFictionList(@Query("page") int page, @Query("count") int count);

    @GET("ifiction/switchcategory")
    Observable<BaseArrayResponse<CategoryModel>> getCreateCategoryList();

    @GET("thumbsup/like")
    Observable<BaseResponse> likeFiction(@Query("status") int status, @Query("ifiction_id") String id);

    @GET("index/prayupdate")
    Observable<BaseResponse> prayUpdate(@Query("ifiction_id") String id);

    @GET("index/devicetoken")
    Observable<BaseResponse> uploadDeviceToken(@Query("device_token") String deviceToken);

    @GET("messages/list")
    Observable<BaseArrayResponse<MessagesModel>> getMessageList(@Query("page") int page, @Query("count") int count);

    @GET("messages/unreadcount")
    Observable<BaseResponse<Integer>> getUnreadMessageCount();

    @GET("messages/read")
    Observable<BaseResponse> readMessage(@Query("id") String messageId);

    //完成任务，获取奖励
    @GET("task/reward")
    Observable<BaseResponse> getRewards(@Query("op") String source);

    //获取用户金币数
    @GET("user/getmcoin")
    Observable<BaseResponse<WalletModel>> getUserCoins();

    //填写邀请码
    @GET("user/invite")
    Observable<BaseResponse> inputInviteCode(@Query("code") String code);

    //获取任务列表
    @GET("task/list")
    Observable<BaseResponse<TaskModel>> getTaskList();
}
