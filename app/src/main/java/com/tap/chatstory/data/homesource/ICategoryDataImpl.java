package com.tap.chatstory.data.homesource;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tap.chatstory.MainApplication;
import com.tap.chatstory.R;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.OnFictionListListener;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.homesource.model.CategoryModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/9/15.
 */

public class ICategoryDataImpl implements ICategoryData {
    private CompositeSubscription mSubscriptions;
    private Gson mGson;

    public ICategoryDataImpl() {
        mSubscriptions = new CompositeSubscription();
        mGson = new Gson();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void getCategoryList(final OnCategoryListListener listener) {
        String string = PreferencesHelper.getInstance().getString(PreferencesHelper.KEY_CATEGORY_LIST, "");
        List<CategoryModel> list = mGson.fromJson(string, new TypeToken<List<CategoryModel>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            if (listener != null) {
                listener.onCategoryList(list);
            }
        }

        Subscription subscription = HttpUtils.getInstance().getCategoryList(new Subscriber<List<CategoryModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " error " + e.toString());
                Toast.makeText(MainApplication.getAppContext(), R.string.common_network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<CategoryModel> list) {
                PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_CATEGORY_LIST, mGson.toJson(list));
                if (listener != null) {
                    listener.onCategoryList(list);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getCreateCategoryList(final OnCategoryListListener listener) {
        Subscription subscription = HttpUtils.getInstance().getCreateCategoryList(new Subscriber<List<CategoryModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " error " + e.toString());
                Toast.makeText(MainApplication.getAppContext(), R.string.common_network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<CategoryModel> list) {
                if (listener != null) {
                    listener.onCategoryList(list);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getFictionList(final OnFictionListListener listener, FictionListRequest request, final boolean refresh) {
        Subscription subscription = HttpUtils.getInstance().getFictionList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " getFictionList error " + e.toString());
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onNext(List<FictionDetailModel> list) {
                Log.i("Lebron", " size " + list.size());
                if (listener != null) {
                    listener.onFictionList(list, refresh);
                }
            }
        }, request.getQueryMap());
        mSubscriptions.add(subscription);
    }
}
