package com.tap.chatstory.data.homesource;

import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.OnFictionListListener;
import com.tap.chatstory.data.homesource.model.CategoryModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/15.
 */

public interface ICategoryData {
    void clearSubscriptions();

    void getCategoryList(OnCategoryListListener listener);

    void getCreateCategoryList(OnCategoryListListener listener);

    void getFictionList(OnFictionListListener listener, FictionListRequest request, boolean refresh);

    interface OnCategoryListListener {
        void onCategoryList(List<CategoryModel> list);
    }
}
