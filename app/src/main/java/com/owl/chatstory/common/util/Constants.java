package com.owl.chatstory.common.util;

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

    public static final String OPERATION_DELETE = "delete";
    public static final String OPERATION_RECOVER = "recover";

    public static String getStatus(int status) {
        switch (status) {
            case STATUS_REVIEWING:
                return "审核中";
            case STATUS_ONLINE:
                return "已发布";
            case STATUS_OFFLINE:
                return "已下架";
            case STATUS_VIOLATION:
                return "违规下架";
            default:
                return "审核中";
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
}
