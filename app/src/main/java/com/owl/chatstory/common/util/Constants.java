package com.owl.chatstory.common.util;

import com.owl.chatstory.MainApplication;
import com.owl.chatstory.R;

import java.util.Locale;

/**
 * Created by lebron on 2017/11/22.
 */

public class Constants {
    public static final String LANGUAGE_ENGLISH = "english";
    public static final String LANGUAGE_CHINESE = "simplified";
    public static final String LANGUAGE_CHINESE_TW = "traditional";

    public static final int STATUS_REVIEWING = 0;//审核中
    public static final int STATUS_ONLINE = 1;//上线
    public static final int STATUS_OFFLINE = 2;//下线
    public static final int STATUS_VIOLATION = 3;//违规
    public static final int STATUS_CREATING = -1;//创作中

    public static final String OPERATION_DELETE = "delete";
    public static final String OPERATION_RECOVER = "recover";

    public static final String RELOAD_DATA_AFTER_LOGINED = "RELOAD_DATA_AFTER_LOGINED";

    public static String getStatus(int status) {
        switch (status) {
            case STATUS_REVIEWING:
                return MainApplication.getAppContext().getString(R.string.creation_status_reviewing);
            case STATUS_ONLINE:
                return MainApplication.getAppContext().getString(R.string.creation_status_released);
            case STATUS_OFFLINE:
                return MainApplication.getAppContext().getString(R.string.creation_status_offline);
            case STATUS_VIOLATION:
                return MainApplication.getAppContext().getString(R.string.creation_status_refused);
            case STATUS_CREATING:
                return MainApplication.getAppContext().getString(R.string.creation_status_creating);
            default:
                return MainApplication.getAppContext().getString(R.string.creation_status_reviewing);
        }
    }

    public static String getLanguage(String language) {
        switch (language) {
            case LANGUAGE_ENGLISH:
                return "English";
            case LANGUAGE_CHINESE:
                return "简体中文";
            case LANGUAGE_CHINESE_TW:
                return "繁體中文";
            default:
                return "繁體中文";
        }
    }

    public static String getLanguage() {
        try {
            String country = Locale.getDefault().getCountry().toLowerCase();
            if (country.equals("cn")) {
                return Constants.LANGUAGE_CHINESE;
            } else if (country.equals("tw")) {
                return Constants.LANGUAGE_CHINESE_TW;
            } else {
                return Constants.LANGUAGE_ENGLISH;
            }
        } catch (Exception e) {
            return Constants.LANGUAGE_CHINESE;
        }
    }
}
