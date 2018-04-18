package com.tap.chatstory.common.util;

import com.tap.chatstory.MainApplication;
import com.tap.chatstory.R;

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

    public static final String OPERATION_DELETE = "delete";//下线
    public static final String OPERATION_RECOVER = "recover";//上线
    public static final String OPERATION_DROP = "drop";//删除

    public static final int MESSAGE_FICTION = 100; //小说
    public static final int MESSAGE_LIKE = 1;//点赞
    public static final int MESSAGE_ONLINE = 2;//上线
    public static final int MESSAGE_VIOLATION = 3;//违规
    public static final int MESSAGE_STAR = 4;//收藏
    public static final int MESSAGE_PARY_UPDATE = 5;//求更新

    public static final String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgE9eaDJn1L3j8c5EapELV6l06Fl8xwGh2D6H7h6Up7FUh4UjK9UY2AxlGW9wU+sQNN4695AhI/dj96ORAydjVKpexWTckw+MDBZl/xWpiV3nnqPJXiQpzkcrsewnVnBceIyGDrCI43tBbLPv/62+h9r3kll6ihymMSP0LBqOz78WVekE1u4U7JR2GjJC7kDKfYxwDvTAp/w+t6MpK0Vpe0O0I5BDk5KwygmiwVLn9IOdrigGLEL+aIwELtw79Puf1M2T03SzadjVD/HLxZsYYzUJWXJuKDDQAswA/Cecc+DIQ9YRgLiheyq5mXUgJT4CsRI5Wyf/jJDNJrhwk6D5qwIDAQAB";
    public static final String WEEK_SKU = "week_vip1.99";
    public static final String ONE_MONTH_SKU = "month_vip3.99";
    public static final String THREE_MONTHS_SKU = "3months_vip10.99";
    public static final String YEAR_SKU = "1year_vip32.99";

    public static final String RELOAD_DATA_AFTER_LOGINED = "RELOAD_DATA_AFTER_LOGINED";
    public static final String EVENT_GOTO_READACTIVITY = "EVENT_GOTO_READACTIVITY";

    public static final String ADMOB_REWARDED_VIDEO_ID = "ca-app-pub-8805953710729771/1560215991";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-8805953710729771/8714094930";

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
