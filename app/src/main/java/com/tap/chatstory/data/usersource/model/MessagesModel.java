package com.tap.chatstory.data.usersource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2018/3/2.
 */

public class MessagesModel {
    @SerializedName("id")
    private String id;
    @SerializedName("ifiction_id")
    private String fictionId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("type")
    private int type;
    @SerializedName("content")
    private Content content;
    @SerializedName("unread")
    private int unread;
    @SerializedName("status")
    private int status;
    @SerializedName("createline")
    private Long createLine;
    @SerializedName("updateline")
    private Long updateLine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFictionId() {
        return fictionId;
    }

    public void setFictionId(String fictionId) {
        this.fictionId = fictionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getCreateLine() {
        return createLine;
    }

    public void setCreateLine(Long createLine) {
        this.createLine = createLine;
    }

    public Long getUpdateLine() {
        return updateLine;
    }

    public void setUpdateLine(Long updateLine) {
        this.updateLine = updateLine;
    }

    public class Content {
        @SerializedName("userinfo")
        private UserInfo userInfo;
        @SerializedName("ifiction")
        private FictionInfo fictionInfo;
        @SerializedName("chapter")
        private ChapterInfo chapterInfo;
        @SerializedName("reason")
        private String reason;

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public FictionInfo getFictionInfo() {
            return fictionInfo;
        }

        public void setFictionInfo(FictionInfo fictionInfo) {
            this.fictionInfo = fictionInfo;
        }

        public ChapterInfo getChapterInfo() {
            return chapterInfo;
        }

        public void setChapterInfo(ChapterInfo chapterInfo) {
            this.chapterInfo = chapterInfo;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public class UserInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("username")
        private String userName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public class FictionInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("writer_id")
        private String writerId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWriterId() {
            return writerId;
        }

        public void setWriterId(String writerId) {
            this.writerId = writerId;
        }
    }

    public class ChapterInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("ifiction_id")
        private String fictionId;
        @SerializedName("num")
        private int num;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFictionId() {
            return fictionId;
        }

        public void setFictionId(String fictionId) {
            this.fictionId = fictionId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
