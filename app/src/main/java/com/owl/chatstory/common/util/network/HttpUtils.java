package com.owl.chatstory.common.util.network;

import android.util.Log;

import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.network.request.UserRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.homesource.model.CategoryModel;
import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.data.usersource.model.UserResponse;

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
    //private static final String BASE_URL = "http://47.94.243.139:8080/android/";
    private static final String BASE_URL = "http://52.15.164.29:8080/android/";
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;
    private static HttpUtils mInstance;

    private HttpUtils() {
        mRetrofit = new Retrofit.Builder()
                .client(getHttpClientBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    private OkHttpClient.Builder getHttpClientBuilder() {
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        HttpUrl originalHttpUrl = originalRequest.url();
                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("type", getType())
                                .build();
                        Request request = originalRequest.newBuilder()
                                .url(url)
                                .method(originalRequest.method(), originalRequest.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(httpLoggingInterceptor);
    }

    private String getType() {
        try {
            String country = Locale.getDefault().getCountry().toLowerCase();
            if (country.equals("cn")) {
                return "simplified";
            } else if (country.equals("tw")) {
                return "traditional";
            } else {
                return "traditional";
            }
        } catch (Exception e) {
            return "simplified";
        }
    }

    //获取单例
    public static HttpUtils getInstance() {
        if (mInstance == null) {
            mInstance = new HttpUtils();
        }
        return mInstance;
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

    public Subscription getFictionDetail(Subscriber<FictionModel> subscriber, String id) {
        Subscription subscription = mApiService.getFictionDetail(id)
                .map(new BaseResponseFunc<FictionModel>())
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

}
