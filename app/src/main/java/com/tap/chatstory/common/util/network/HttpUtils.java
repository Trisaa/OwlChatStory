package com.tap.chatstory.common.util.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tap.chatstory.BuildConfig;
import com.tap.chatstory.MainApplication;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.common.util.DeviceUtils;
import com.tap.chatstory.common.util.PreferencesHelper;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lebron on 2017/7/31.
 */

public class HttpUtils {
    private static final String BASE_TEST_URL = "http://47.94.243.139:8080/android/";
    private static final String BASE_URL = "http://52.15.164.29:8080/android/";
    private static final int DEFAULT_TIMEOUT = 5;
    private static HttpUtils mInstance, mLanguageInstance;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    private HttpUtils(String language) {
        String url = BuildConfig.DEBUG ? BASE_TEST_URL : BASE_URL;
        mRetrofit = new Retrofit.Builder()
                .client(getHttpClientBuilder(language).build())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    private static String getType() {
        try {
            String country = Locale.getDefault().getCountry().toLowerCase();
            if (country.equals("cn")) {
                return Constants.LANGUAGE_CHINESE;
            } else if (country.equals("tw")) {
                return Constants.LANGUAGE_CHINESE_TW;
            } else {
                return Constants.LANGUAGE_CHINESE_TW;
            }
        } catch (Exception e) {
            return Constants.LANGUAGE_CHINESE;
        }
    }

    //获取单例
    public static HttpUtils getInstance() {
        if (mInstance == null) {
            mInstance = new HttpUtils(getType());
        }
        return mInstance;
    }

    public static HttpUtils getInstance(String language) {
        if (TextUtils.isEmpty(language)) {
            if (mInstance == null) {
                mInstance = new HttpUtils(getType());
            }
            return mInstance;
        } else {
            mLanguageInstance = new HttpUtils(language);
            return mLanguageInstance;
        }
    }

    private OkHttpClient.Builder getHttpClientBuilder(final String language) {
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        HttpUrl originalHttpUrl = originalRequest.url();
                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("type", language)
                                .addQueryParameter("version", DeviceUtils.getVersionName(MainApplication.getAppContext()))
                                .addQueryParameter("version_code", String.valueOf(DeviceUtils.getVersionCode(MainApplication.getAppContext())))
                                .build();
                        Request request = originalRequest.newBuilder()
                                .header("utoken", PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, ""))
                                //.header("utoken", "WlBWUDcyYXFkVGgrU2tFdWxKVEs5VXJIZi9hMXJzc3FIcXJkdEtLWndKZz0")
                                .url(url)
                                .method(originalRequest.method(), originalRequest.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(httpLoggingInterceptor);
    }

    private Gson getGson() {
        Gson gson = new GsonBuilder()
                .setLenient()  // 设置GSON的非严格模式setLenient()
                .create();
        return gson;
    }

    public Subscription checkUpdate(Subscriber<UpdateModel> subscriber) {
        Subscription subscription = mApiService.checkUpdate()
                .map(new BaseResponseFunc<UpdateModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getCategoryList(Subscriber<List<CategoryModel>> subscriber) {
        Subscription subscription = mApiService.getCategoryList()
                .map(new BaseArrayResponseFunc<CategoryModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getCreateCategoryList(Subscriber<List<CategoryModel>> subscriber) {
        Subscription subscription = mApiService.getCreateCategoryList()
                .map(new BaseArrayResponseFunc<CategoryModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getChapterDetail(Subscriber<FictionModel> subscriber, String id) {
        Subscription subscription = mApiService.getChapterDetail(id)
                .map(new BaseResponseFunc<FictionModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getFictionDetail(Subscriber<FictionDetailModel> subscriber, String id) {
        Subscription subscription = mApiService.getFictionDetail(id)
                .map(new BaseResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getFictionChapterList(Subscriber<List<ChapterModel>> subscriber, String id, int page) {
        Subscription subscription = mApiService.getFictionChapterList(id, page, 10)
                .map(new BaseArrayResponseFunc<ChapterModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getFictionList(Subscriber<List<FictionDetailModel>> subscriber, Map<String, String> map) {
        Subscription subscription = mApiService.getFictionList(map)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription signUpOrIn(Subscriber<UserResponse> subscriber, UserRequest request) {
        Subscription subscription = mApiService.signUpOrIn(request)
                .map(new BaseResponseFunc<UserResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription addToHistory(Subscriber subscriber, String id) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.addToHistory(token, id)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getHistoryList(Subscriber<List<FictionDetailModel>> subscriber, Map<String, String> map) {
        Subscription subscription = mApiService.getHistoryList(map)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription clearHistory(Subscriber subscriber) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.clearHistory(token)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription updateFictionBasicInfo(Subscriber<FictionResponse> subscriber, FictionDetailModel model) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        model.setToken(token);
        Log.i("Lebron", new Gson().toJson(model).toString());
        Subscription subscription = mApiService.updateFictionBasicInfo(model)
                .map(new BaseResponseFunc<FictionResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserFictionList(Subscriber<List<FictionDetailModel>> subscriber, String language) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.getUserFictionList(token, language)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription publishChapter(Subscriber subscriber, FictionModel model) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        model.setToken(token);
        Subscription subscription = mApiService.publishChapter(model)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getRoleList(Subscriber<List<ActorModel>> subscriber, String language, String id) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.getRoleList(token, language, id)
                .map(new BaseArrayResponseFunc<ActorModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription updateRoleList(Subscriber<List<ActorModel>> subscriber, RoleListRequest request) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        request.setToken(token);
        Subscription subscription = mApiService.updateRoleList(request)
                .map(new BaseArrayResponseFunc<ActorModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getChapterList(Subscriber<List<FictionModel>> subscriber, String language, String id) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.getChapterList(token, language, id)
                .map(new BaseArrayResponseFunc<FictionModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription operateChapter(Subscriber subscriber, OperationRequest request) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        request.setToken(token);
        Subscription subscription = mApiService.operateChapter(request)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription operateFiction(Subscriber subscriber, OperationRequest request) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        request.setToken(token);
        Subscription subscription = mApiService.operateFiction(request)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription collectFiction(Subscriber subscriber, String fictionId) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Log.i("Ok", " token " + token);
        Subscription subscription = mApiService.collectFiction(token, fictionId)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription uncollectFiction(Subscriber subscriber, String fictionId) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.uncollectFiction(token, fictionId)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getCollectionList(Subscriber<List<FictionDetailModel>> subscriber, FictionListRequest request) {
        Subscription subscription = mApiService.getCollectionList(request)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription isCollect(Subscriber subscriber, String fictionId) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.isCollect(token, fictionId)
                .map(new BaseResponseFunc<FictionStatusResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription searchData(Subscriber<SearchModel> subscriber, String keyword) {
        Subscription subscription = mApiService.searchData(keyword)
                .map(new BaseResponseFunc<SearchModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription updateUserinfo(Subscriber subscriber, UserRequest userRequest) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        userRequest.setToken(token);
        Subscription subscription = mApiService.updateUserInfo(userRequest)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserInfo(Subscriber<UserModel> subscriber) {
        String token = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_TOKEN, "");
        Subscription subscription = mApiService.getUserInfo(token)
                .map(new BaseResponseFunc<UserModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserPageInfo(Subscriber<UserPageModel> subscriber, String id) {
        Subscription subscription = mApiService.getUserPageInfo(id)
                .map(new BaseResponseFunc<UserPageModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserRelatedFictionList(Subscriber<List<FictionDetailModel>> subscriber, String id, int page) {
        Subscription subscription = mApiService.getUserRelatedFictionList(id, page, 10)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getOwnRelatedFictionList(Subscriber<List<FictionDetailModel>> subscriber, int page) {
        Subscription subscription = mApiService.getOwnRelatedFictionList(page, 10)
                .map(new BaseArrayResponseFunc<FictionDetailModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getOwnPageInfo(Subscriber<UserPageModel> subscriber) {
        Subscription subscription = mApiService.getOwnPageInfo()
                .map(new BaseResponseFunc<UserPageModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription likeFiction(Subscriber subscriber, int status, String id) {
        Subscription subscription = mApiService.likeFiction(status, id)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription prayUpdate(Subscriber subscriber, String id) {
        Subscription subscription = mApiService.prayUpdate(id)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription uploadDeviceToken(Subscriber subscriber, String deviceToken) {
        Subscription subscription = mApiService.uploadDeviceToken(deviceToken)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserMessageList(Subscriber<List<MessagesModel>> subscriber, int page) {
        Subscription subscription = mApiService.getMessageList(page, 10)
                .map(new BaseArrayResponseFunc<MessagesModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUnreadMessageCount(Subscriber<Integer> subscriber) {
        Subscription subscription = mApiService.getUnreadMessageCount()
                .map(new BaseResponseFunc<Integer>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription readMessage(Subscriber subscriber, String messageId) {
        Subscription subscription = mApiService.readMessage(messageId)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getUserCoins(Subscriber<WalletModel> subscriber) {
        Subscription subscription = mApiService.getUserCoins()
                .map(new BaseResponseFunc<WalletModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getRewards(Subscriber subscriber, String source) {
        Subscription subscription = mApiService.getRewards(source)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription inputInviteCode(Subscriber subscriber, String code) {
        Subscription subscription = mApiService.inputInviteCode(code)
                .map(new BaseResponseFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    public Subscription getTaskList(Subscriber<TaskModel> subscriber) {
        Subscription subscription = mApiService.getTaskList()
                .map(new BaseResponseFunc<TaskModel>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    private class BaseResponseFunc<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> httpResult) {
            if (httpResult.getCode() != 1) {
                Log.e("HttpError", httpResult.getMessage());
                throw new ApiException(httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }

    private class BaseArrayResponseFunc<T> implements Func1<BaseArrayResponse<T>, ArrayList<T>> {
        @Override
        public ArrayList<T> call(BaseArrayResponse<T> httpResult) {
            if (httpResult.getCode() != 1) {
                Log.e("HttpError", httpResult.getMessage());
                throw new ApiException(httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }

}
