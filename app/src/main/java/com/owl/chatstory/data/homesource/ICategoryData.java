package com.owl.chatstory.data.homesource;

import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.OnFictionListListener;
import com.owl.chatstory.data.homesource.model.CategoryModel;

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
